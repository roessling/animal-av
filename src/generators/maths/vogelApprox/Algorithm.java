package generators.maths.vogelApprox;

import algoanim.primitives.generators.Language;

public class Algorithm {

	private Animation myAnimation;
	private MatrixElement[] supply;	// Angebot
	private MatrixElement[] demand;	// Nachfrage
	
	private int row; //Aktuelle Spalte
	private int column;	// Aktuelle Zeile
	
	private MatrixElement[][] tableau;	 // Transporttableau
	private MatrixElement[][] cost;	 // Kostenmatrix
	private MatrixElement[] costDifColumn; // Kosten-Differenz-Spalte (unten)
	private MatrixElement[] costDifRow;	// Kosten-Differenz-Zeile (rechts)
	
	
	public Algorithm(int[] supply, int[] demand, int[][] cost, Language animationScript) {
		validateInput(supply, demand, cost);
		
		// Init supply
		this.supply = new MatrixElement[supply.length];
		for (int i=0; i<supply.length; i++) {
			this.supply[i] = new MatrixElement(supply[i],true);
		}
	
		// Init demand
		this.demand = new MatrixElement[demand.length];
		for (int i=0; i<demand.length; i++) {
			this.demand[i] = new MatrixElement(demand[i],true);
		}
		
		// Init cost
		this.cost = new MatrixElement[cost.length][cost[0].length];
		for (int i=0; i<cost.length; i++) {
			for (int j=0; j<cost[0].length; j++) {
				this.cost[i][j] = new MatrixElement(cost[i][j], true);
			}
		}
		
		// Init costDifColumn
		this.costDifColumn = new MatrixElement[demand.length];
		for (int i=0; i<costDifColumn.length; i++) {
			costDifColumn[i] = new MatrixElement();
		}
		
		// Init costDifRow
		this.costDifRow = new MatrixElement[supply.length];
		for (int i=0; i<costDifRow.length; i++) {
			costDifRow[i] = new MatrixElement();
		}
		
		// Init tableau
		this.tableau =  new MatrixElement[supply.length][demand.length];
		for (int i=0; i<tableau.length; i++) {
			for (int j=0; j<tableau[i].length; j++) {
				tableau[i][j] = new MatrixElement(0, false);
			}
		}
		
		myAnimation = new Animation(this.supply, this.demand, tableau, this.cost, costDifColumn, costDifRow, animationScript);
		
	}
	
	
	public void animate() {
		try{	
			myAnimation.buildDefaultViews();
			myAnimation.codeLine0();
			myAnimation.getMyAnimationScript().nextStep();
			
			startAlgorithm();
		}
		catch(IllegalArgumentException iae){
			myAnimation.buildExceptionFrame("Fehler aufgetreten: " + iae.getMessage());
		}
	}
	
	
	public void validateInput(int[] supply, int[] demand, int[][] cost) throws IllegalArgumentException{
		if (demand ==null || demand.length == 0){
			throw new IllegalArgumentException("Array an Nachfrage-Werten nicht valide.");			
		}
		if(supply == null || supply.length == 0){
			throw new IllegalArgumentException("Array an Angebots-Werten nicht valide.");
		}
		if(cost == null || cost.length != supply.length){
			throw new IllegalArgumentException("Array an Kosten-Werten nicht valide.");
		}
		
		for (int[] is : cost) {
			if(is == null || is.length != demand.length){
				throw new IllegalArgumentException("Array an Kosten-Werten nicht valide.");
			}
		}
		
		if(sum(demand) != sum(supply)){
			throw new IllegalArgumentException("Summe der Angebote und Summe der Nachfrage müssen sich gleichen.");
		}
	}
	
	public void startAlgorithm(){
		
		while (numberFreeColumns() > 1 && numberFreeRows() > 1) {
			myAnimation.codeLine0();
			// 3) Berechne Differenzen der Kostenmatrix
			
			calcCostDifColumn();
			calcCostDifRow();
			
			
			
			// Wähle Zeile oder Spalte mit größter Differenz
			myAnimation.codeLine1();
			int i = getBiggest(costDifColumn); 	//unten
			int j = getBiggest(costDifRow);		//rechts
			if (costDifColumn[i].getValue() >= costDifRow[j].getValue()) {
				
				// Berechne Minimum der Spalte
				int k = getSmallest(invertArray(cost)[i]);
				row = k;
				column = i;
				myAnimation.animationLine2(i, column, row);				
			} else {
				// Berechne Minimum der Zeile
				int k = getSmallest(cost[j]);
				//System.out.println(cost[j][k]);
				row = j;
				column = k;
				// Markiere Zeile
				myAnimation.animationLine3(j, column, row);
			}

			
			// 5+6) Kleineren Wert aus supply und demand in position des tableaus eintragen
			if (demand[column].getValue() <= supply[row].getValue()) {
				tableau[row][column].setValue(demand[column].getValue());
				tableau[row][column].setActive(true);
				// Wert abziehen
				supply[row].setValue(supply[row].getValue() - demand[column].getValue());
				demand[column].setValue(0);
				
				myAnimation.codeLine3();
				myAnimation.animationLine6(row, column);
				myAnimation.codeLine5();
				myAnimation.animationLine4(column);
				
				for (MatrixElement c : invertArray(cost)[column]) {
					c.setActive(false);
				}			
				
				
			} else {
				tableau[row][column].setValue(supply[row].getValue());
				tableau[row][column].setActive(true);
				// Wert abziehen
				demand[column].setValue(demand[column].getValue() - supply[row].getValue());
				supply[row].setValue(0);

				myAnimation.codeLine3();
				myAnimation.animationLine6(row, column);
				myAnimation.codeLine5();
				myAnimation.animationLine5(row);
				for (MatrixElement c : cost[row]) {
					c.setActive(false);
				}
			}
			
			
			
		}
		myAnimation.codeLine6();
		myAnimation.codeLine7();
		
		// 7) Markiere Spalte / Zeile in Kostenmatrix
		//		Berechner erneut wie schritt 3
		for (int i=0; i<supply.length; i++) {
			for (int j=0; j<demand.length; j++) {
				if (supply[i].getValue() > 0 && demand[j].getValue() > 0) {
					int min = Math.min(supply[i].getValue(), demand[j].getValue());
					supply[i].setValue(supply[i].getValue() - min);
					demand[j].setValue(demand[j].getValue()-  min);
					tableau[i][j].setValue(min);
					tableau[i][j].setActive(true);
					myAnimation.animationLine7(i, j);
				}
			}
		}
		
		myAnimation.animationLine8();
		
	}
	
	
	private int numberFreeRows() {
		
		int i = 0;
		for (MatrixElement[] c1 : cost) {
			int j = 0;
			for (MatrixElement c2 : c1) {
				if (c2.isActive()) j++;
			}
			if (j>0) i++;
		}
		
		return i;
	}
	
	private int numberFreeColumns() {
		
		MatrixElement[][] icost = invertArray(cost);
		int i = 0;
		for (MatrixElement[] c1 : icost) {
			int j = 0;
			for (MatrixElement c2 : c1) {
				if (c2.isActive()) j++;
			}
			if (j>0) i++;
		}
		
		return i;
	}
	
	// unten
	private void calcCostDifColumn() {
		MatrixElement[][] icost = invertArray(cost);
		for (int i = 0; i<icost.length; i++) {
			Integer pos_min = getSmallest(icost[i]);
			Integer pos_min2 = get2ndSmallest(icost[i]);
			if (pos_min != null && pos_min2 != null) {
				int smallest = icost[i][pos_min].getValue();
				int smallest2 = icost[i][pos_min2].getValue();
				int dif = smallest2 - smallest;
				costDifColumn[i].setValue(dif);
				myAnimation.animationLine0(i, pos_min, pos_min2);
			} else {
				costDifColumn[i].setActive(false);
			}
		}
	}
	
	// rechts
	private void calcCostDifRow() {
		
		for (int i = 0; i<cost.length; i++) {
			Integer pos_min = getSmallest(cost[i]);
			Integer pos_min2 = get2ndSmallest(cost[i]);
			if (pos_min != null && pos_min2 != null) {
				int smallest = cost[i][pos_min].getValue();
				int smallest2 = cost[i][pos_min2].getValue();
				int dif = smallest2 - smallest;
				costDifRow[i].setValue(dif);
				myAnimation.animationLine1(i, pos_min, pos_min2);
			} else {
				costDifRow[i].setActive(false);
			}
		}
	}
	
	
	private int getBiggest(MatrixElement[] array) {
		int j = Integer.MIN_VALUE;
		Integer position = null;
		
		for (int i=0; i<array.length; i++) {
			if (array[i].isActive() && array[i].getValue() > j) {
				j = array[i].getValue();
				position = i;
			}
		}
		
		return position;
	}
	
	private Integer getSmallest(MatrixElement[] array) {
		int j = Integer.MAX_VALUE;
		Integer position = null;
		
		for (int i=0; i<array.length; i++) {
			if (array[i].isActive() && array[i].getValue() < j) {
				j = array[i].getValue();
				position = i;
			}
		}
		
		return position;
	}
	
	private Integer get2ndSmallest(MatrixElement[] array) {
        Integer i = getSmallest(array);
        	if (i==null) return null;
        
        int j = array[i].getValue();
        array[i].modifyValue(Integer.MAX_VALUE);
        Integer min = getSmallest(array);
        array[i].modifyValue(j);
		return min;
	}
	
	
	public static MatrixElement[][] invertArray(MatrixElement[][] array) {
		
		int imax = array.length;
		int jmax = array[0].length;
		
		MatrixElement[][]  iarray = new MatrixElement[jmax][imax];
		
		for (int i=0; i<imax; i++) {
			for (int j=0; j<jmax; j++) {
				iarray[j][i] = array[i][j];
			}
		}
		
		return iarray;
	}
	
	public MatrixElement[] getSupply() {
		return supply;
	}

	public MatrixElement[] getDemand() {
		return demand;
	}
	
	public MatrixElement[][] getCost() {
		return cost;
	}
	
	private int sum(int[] intArray){
		int sum = 0;
		for(int i: intArray){
			sum += i;
		}
		return sum;
	}
	
	
	public String getMyAnimationScript() {
		return myAnimation.getMyAnimationScript().toString();
	}
	
}
