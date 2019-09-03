package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.helpers.AdaptedSourceCode;
import generators.misc.helpers.CustomerWarehouseDiagram;
import generators.misc.helpers.DeliveryCost;
import interactionsupport.models.MultipleSelectionQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class DropHeuristik implements Generator {
    private Language lang;
    private RectProperties MinimaleKosten;
    
    private int[] Warenhausfixkosten;

    private RectProperties HervorhebungWarenhausGeschlossen;
    private RectProperties ZweitGeringstenKosten;
    private RectProperties HervorhebungWarenhausOffen;
    private int[][] TransportkostenMatrix;
    
	private Color WarenhausGeschlossenDiagram;
    private Color WarenhausOffenDiagram;
    private Color PfeilFarbeZugeordnet;    
    private Color PfeilFarbeVerbunden;
    
    /**
	 * The concrete language object used for creating output
	 */

	private int r, l;
	
	private int rRise,lRise;

	private int rowHeight, colWidth, lineMarginX, lineMarginY;

	private TextProperties dropIntoHeader,dropIntoHeader2,dropTableHeader, dropCost_TableHeadProp,
			dropCostProp,infoLineProp;
	private RectProperties minRectProp, secMinRectProp, lineMarkRectProp, delMarkRectProp,backgroundHeaderRectProp;
	
	private Text infoText1,infoText2, iterationText, targetFunctionValueText;
	
	private AdaptedSourceCode sourceCode;
	
	private CustomerWarehouseDiagram CWD;
	
	/*public static void main(String[] args) {
		String code = "";

		dropHeuristik d = new dropHeuristik();
		// code = d.generate(arg0, arg1);
		System.out.println(code);
		String filename = "drop.asu";
		// Output the .asu file in ./bin folder		
		BufferedWriter out;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),"UTF8"));

			try {
				out.write(code);
				 out.close();
					System.out.println(filename + " abgespeichert im Ordner");
					System.out.println(System.getProperty("user.dir"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 		
	}
	*/
	
    
	public void init() {
		System.out.println("Init");
		// Store the language object
	  lang = new AnimalScript("Drop-Heuristik [DE]", "Julian Bonrath, Benjamin Björngen-Schmidt", 1000, 600);
		// This initializes the step mode. Each pair of subsequent steps has to
		// be divdided by a call of lang.nextStep();
		lang.setStepMode(true);

		// Fragen support
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		
		// Settings for tables
		rowHeight = 20;
		colWidth = 30;
		lineMarginX = 5;
		lineMarginY = 5;
		
		// Text Properties Table_Header
		dropIntoHeader= new TextProperties();
		dropIntoHeader
				.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		dropIntoHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 28));
		
		
		
		// Text Properties Table_Header
		dropIntoHeader2= new TextProperties();
		dropIntoHeader2
				.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		dropIntoHeader2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 16));
		


		// Text Properties Table_Header
		dropTableHeader = new TextProperties();
		dropTableHeader
				.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		dropTableHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 16));

		// Text Properties Table_Header
		dropCost_TableHeadProp = new TextProperties();

		// Text Properties Table
		dropCostProp = new TextProperties();
		dropCostProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		dropCostProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 12));
		
		// Text Info line Properties
		infoLineProp =  new TextProperties();
		infoLineProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		infoLineProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 12));
				
		// matrixProp.set(AnimationPropertiesKeys.t, Color.BLACK);

		minRectProp = MinimaleKosten;
		/*
		minRectProp = new RectProperties();
		minRectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
		minRectProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.YELLOW);
		minRectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		minRectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
		*/
		secMinRectProp = ZweitGeringstenKosten;
		/*
		secMinRectProp = new RectProperties();
		secMinRectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
		secMinRectProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.ORANGE);
		secMinRectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		secMinRectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
		*/
		
		lineMarkRectProp = HervorhebungWarenhausOffen;
		/*
		 lineMarkRectProp= new RectProperties();
		lineMarkRectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);
		lineMarkRectProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
		lineMarkRectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		lineMarkRectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
		*/

		delMarkRectProp = HervorhebungWarenhausGeschlossen;
		/*
		delMarkRectProp = new RectProperties();
		delMarkRectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
		delMarkRectProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		delMarkRectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		delMarkRectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
		*/
		
		backgroundHeaderRectProp = new RectProperties();
		backgroundHeaderRectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
		backgroundHeaderRectProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		backgroundHeaderRectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		backgroundHeaderRectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
		
		
		
	}

	/**
	 * 
	 * @param costs
	 * @param fixWarehouseCost
	 */
	public void solve(int[][] costs, int[] fixWarehouseCost) {

		// Create Array: coordinates, data, name, display options,
		// default properties

		// Zielfunktionswert zf
		int zf;
		int i, j;
		int k = 1;
		int m=0;
		int closingPos;

		Text[][] costsTable;
		Text[][] deliverRiseTable;

		// Matrix für cheapest and 2nd cheapest costs
		DeliveryCost[][] bestCostToCustomers;

		
		int[][] w = new int[r][l];
		// deliveryCostRise
		int[] e = new int[l]; // Sum of Cost per Warehouse
		int[] f = fixWarehouseCost; // Fix Warehouse Cost
		int f_sum;
		int[] d = new int[l]; // Sum of Costsaving per Warehpuse
		int d_sum;
	

		int[] warehouseOpen = new int[l]; // List of closed (-1), not sure (0)
											// and opened(1) warehouses
		int[] customerAssigned = new int[r]; // Customer is assigned to a
												// Warehouse
		Vector<Rect> markerMinCosts,markerAsigned;


		for (i = 0; i < l; i++) {
			warehouseOpen[i] = 0;
		}
		for (j = 0; j < r; j++) {
			customerAssigned[j] = -1;
		}
		lang.newText(new Coordinates(10,40), "Der Algorithmus", "dropCost_into_Header23", null,
				dropIntoHeader);
		/*
	    TrueFalseQuestionModel tfq = new TrueFalseQuestionModel(
	            "trueFalseQuestion", true, 5);
	        tfq.setPrompt("Is MoodleConnect cool?");
	        tfq.setGroupID("Second question group");

	        lang.addTFQuestion(tfq);
	      */  

		
		Node leftTopOffset,rightBottomOffset;
		leftTopOffset = new Offset(-5,-5, "dropCost_into_Header23",AnimalScript.DIRECTION_NW);
		rightBottomOffset =new Offset(7,5, "dropCost_into_Header23",AnimalScript.DIRECTION_SE);
		lang.newRect(leftTopOffset, rightBottomOffset, "backgroundRectAlgo", null,backgroundHeaderRectProp);

		// Start
		lang.newText(new Offset(25,50,"dropCost_into_Header23",AnimalScript.DIRECTION_NW), "Kosten", "dropCost_Header", null,
				dropTableHeader);
		
		//displayBeginingSlides();

		//sCode sourceCode = new sCode()
		sourceCode =  new AdaptedSourceCode((r+2)*colWidth+4*lineMarginY+40+r*25+20,-20,lang);
		
		

		// InfoZeilen
		infoText1 = lang.newText(new Offset(0,8*lineMarginX+2*(l+3)*rowHeight+40,"dropCost_Header",AnimalScript.DIRECTION_NW), "1", "infoText1", null,infoLineProp);
		infoText2 = lang.newText(new Offset(0,10,"infoText1",AnimalScript.DIRECTION_SW), "2", "infoText2", null,infoLineProp);
	
		
		// Create and Draw Costs
		costsTable = drawCostTable();
		//markerMinCosts = createMinMarker();
		lang.nextStep();
		
		deliverRiseTable = drawDilveryRiseTable();
		lang.nextStep();
	
		CWD = new CustomerWarehouseDiagram((r+2)*colWidth+4*lineMarginY+10,-20,lang,r,l,WarenhausOffenDiagram,WarenhausGeschlossenDiagram,PfeilFarbeZugeordnet,PfeilFarbeVerbunden,infoLineProp);
		
		// Insert Min Costs		
		// Calc deliveryCost to cheapest and 2nd cheapest Warehouse
		bestCostToCustomers = calcBestCostToCustomers(costs, warehouseOpen,
				customerAssigned);
		insertDilveryRiseF(deliverRiseTable,f,customerAssigned,warehouseOpen);
		
		// Display Costs and mark them
		insertCosts(costsTable,costs,customerAssigned,warehouseOpen);
		infoText1.setText("Füge Kosten c_ij in Kostentabelle ein", null, null);
		infoText2.setText("Füge Fixkosten f_i der Lagerhäuser in Kostensteigerungstabelle ein", null, null);
		lang.nextStep();
		
		markerMinCosts = markMinCosts(bestCostToCustomers,customerAssigned,false);

		iterationText.setText("Iteration " + k + " " + "a:", null, null);
		sourceCode.hightlightNextStep();
		lang.nextStep("Iteration 1: a)");
		
		insertMinCosts(costsTable, bestCostToCustomers, customerAssigned,false);
		lang.nextStep();		
	
		markerMinCosts.addAll(markMinimaCostsLine());
		lang.nextStep();


		targetFunctionValueText = lang.newText(new Offset(350,200,"dropCost_Header",AnimalScript.DIRECTION_NW), "", "targetFunctionValueText", null,dropTableHeader);
		// Iterate now
		while(true) {
		//	System.out.println("\n--- Starte iteration");

			f_sum = calcSumWarehouseCost(f, warehouseOpen);
			d_sum = calcDeliveryCost(bestCostToCustomers);
			zf = f_sum + d_sum;
			if(m==0){
				m=zf;
			}
			refreshCosts ( f_sum,  d_sum,  zf);
			infoText1.setText("Berechne aktuelle Kosten neu", null, null);
			infoText2.setText("Summe Fixe Lagerkosten von f_i " + f_sum + " und Summe Transportkosten c_1,j von " + d_sum, null, null);

			lang.nextStep();
					
			// a) Calc Rising of delivery cost w if warehouse i is closed
			w = calcDeliveryCostRise(bestCostToCustomers, warehouseOpen);

			// b) Calc total cost Saving d, for closing warehouse i
			e = calcDeliveryCostRiseSum(w, warehouseOpen);

			d = calcCostSaving(e, f, warehouseOpen);
			iterationText.setText("Iteration " + k + " " + "b:", null, null);
			sourceCode.hightlightNextStep();			
			insertDilveryRiseW(deliverRiseTable,w,customerAssigned,warehouseOpen);
			lang.nextStep("Iteration " + k + ": b)");
			
			sourceCode.hightlightNextStep();
			insertDilveryRiseE(deliverRiseTable,e,customerAssigned,warehouseOpen);
			lang.nextStep();
			
			insertDilveryRiseD(deliverRiseTable,d,customerAssigned,warehouseOpen);
			iterationText.setText("Iteration " + k + " " + "c:", null, null);
			sourceCode.hightlightNextStep();
			lang.nextStep("Iteration " + k + ": c)");

			// if d_i <=0 then Warehouse is alawys open

			demarkLinexExceptCostCustomer(markerMinCosts);
			
			markerAsigned = markOpenedWarehouses(warehouseOpen,d);
			lang.nextStep();
			markerAsigned.addAll(markAssignedCustomer(warehouseOpen,customerAssigned,w,d));
			openWarehouseAssignCustomer(warehouseOpen,customerAssigned,w,d);
			lang.nextStep();
			
			// c) Close warehouse k with maximum saving
			
			closingPos = closeWarehouse(warehouseOpen,d);
			questionCloseWarehouse(closingPos,warehouseOpen);
			markerAsigned.addAll(markClosedWarehouses(closingPos,bestCostToCustomers));
			sourceCode.hightlightNextStep();
			iterationText.setText("Iteration " + k + " " + "d:", null, null);
			lang.nextStep("Iteration " + k + ": d)");

			sourceCode.hightlightNextStep();
			iterationText.setText("Iteration " + k + " " + "e:", null, null);
			if(!abort(warehouseOpen))
			{

				iterationText.setText("Abbruch nach Iteration: " + k, null, null);
				infoText1.setText("Wir haben nun den Algorithmus komplett durchlaufen", null, null);
				infoText2.setText("Eine Auslieferung an alle Kunden kostet " + zf + "Einheiten", null, null);

				refreshCosts ( f_sum,  d_sum,  zf);
				
				break;
			}
			infoText1.setText("Es existieren noch nicht geschlossende Lagerhäuser,", null, null);
			infoText2.setText("die eine potenzielle Kostensteigerung (d_i > 0) aufweisen", null, null);
			lang.nextStep("Iteration " + k + ": e)");
			
			// Preperation for next Iteration

			clearDilveryRiseTable(deliverRiseTable);
			insertDilveryRiseF(deliverRiseTable,f,customerAssigned,warehouseOpen);
			
			demarkLinexExceptCostCustomer(markerAsigned);
			insertCosts(costsTable,costs,customerAssigned,warehouseOpen);
			clearMinCostsTable(costsTable);
			
			iterationText.setText("Vorbereitung für nächste Iteration", null, null);	
			sourceCode.hightlightNextStep();
			lang.nextStep();
			demarkAllLines(markerAsigned);

			// d) Update i1,i2
			bestCostToCustomers = calcBestCostToCustomers(costs, warehouseOpen,
								customerAssigned);
			
			// Display Costs and mark them
			markerMinCosts = markMinCosts(bestCostToCustomers,customerAssigned,false);
			k++;
			sourceCode.hightlightNextStep();
			iterationText.setText("Iteration " + k + " " + "a:", null, null);
			lang.nextStep("Iteration " + k + ": a)");
			
			insertMinCosts(costsTable, bestCostToCustomers, customerAssigned,false);
			lang.nextStep();	
			
			markerMinCosts.addAll(markMinimaCostsLine());
			lang.nextStep();

			// Cancel if d_i <=0 for all i
			
			
		} 
		lang.nextStep("Iteration " + k + ": e)");
		
		// Make End Slides
		sourceCode.hide();
				
		//customer
		int[] foo = new int [r];
		for (j = 0; j < r; j++) {
			foo[j] = -1;
		}
		
		// warehouse
		int[] bar = new int [l];
		for (j = 0; j < l; j++) {
			bar[j] = warehouseOpen[j]==-1?-1:0;
		}
		
		demarkLinexExceptCostCustomer(markerAsigned);
		demarkAllLines(markerAsigned);
		
		bestCostToCustomers = calcBestCostToCustomers(costs, bar,
				foo);
		
		w = calcDeliveryCostRise(bestCostToCustomers, bar);
		e = calcDeliveryCostRiseSum(w, bar);
		d = calcCostSaving(e, f, bar);
		
		insertCosts(costsTable, costs, foo,warehouseOpen);
		insertMinCosts(costsTable, bestCostToCustomers, foo,true);
		
		insertDilveryRiseW(deliverRiseTable, w, foo, bar);
		insertDilveryRiseE(deliverRiseTable, e, customerAssigned, bar);
		insertDilveryRiseF(deliverRiseTable, f, customerAssigned, bar);
		insertDilveryRiseD(deliverRiseTable, d, customerAssigned, bar);
		
		markerMinCosts = markMinCosts(bestCostToCustomers,foo,true);
		
		iterationText.setText("Endkostentabelle", null, null);
		infoText1.setText("Füge nur notwendige Daten zur Veranschaulichung der Lösung ein", null, null);
		infoText2.setText("", null, null);

		lang.newText(new Offset(0,20,"infoText2",AnimalScript.DIRECTION_NW), "Erläuterung", "dropCost_endInfoHeader",null, dropTableHeader);
		lang.newText(new Offset(10,30,"dropCost_endInfoHeader",AnimalScript.DIRECTION_NW), "Die Markierungen in der oberen Tabellen stehen für die Zuordnung von Warenhaus i zu Kunde j ", "dropCost_endInfo1",null, infoLineProp);
		lang.newText(new Offset(0,15,"dropCost_endInfo1",AnimalScript.DIRECTION_NW), "wobei die markierten Zahlen für die anfallenden Kosten stehen. Zählt man diese zusammen so ", "dropCost_endInfo1",null, infoLineProp);
		lang.newText(new Offset(0,30,"dropCost_endInfo1",AnimalScript.DIRECTION_NW), "ergeben sich Transportkosten von " + d_sum + " Einheiten", "dropCost_endInfo1",null, infoLineProp);
		lang.newText(new Offset(0,45,"dropCost_endInfo1",AnimalScript.DIRECTION_NW), "Zählt man in der unteren Tabelle die Warenhausfixkosten f_i der geöffneten Warenhäuser zusammen so", "dropCost_endInfo1",null, infoLineProp);
		lang.newText(new Offset(0,60,"dropCost_endInfo1",AnimalScript.DIRECTION_NW), "ergeben sich Warenhauskosten von " + f_sum + " Einheiten", "dropCost_endInfo1",null, infoLineProp);
		lang.newText(new Offset(0,75,"dropCost_endInfo1",AnimalScript.DIRECTION_NW), "In Summe macht das " + zf + " Einheiten für Betrieb der Warenhäuser und Transport der Waren", "dropCost_endInfo1",null, infoLineProp);
		//lang.newText(new Offset(0,90,"dropCost_endInfo1",AnimalScript.DIRECTION_NW), "ergeben sich Transportkosten von " + d_sum + " Einheiten", "dropCost_endInfo1",null, infoLineProp);
		//lang.newText(new Offset(0,105,"dropCost_endInfo1",AnimalScript.DIRECTION_NW), "ergeben sich Transportkosten von " + d_sum + " Einheiten", "dropCost_endInfo1",null, infoLineProp);
		lang.nextStep("Lösungserläuterung");
		lang.hideAllPrimitives();
		//lang.newText(new Offset(10,30,"dropCost_endInfoHeader",AnimalScript.DIRECTION_NW), "Die Markierungen in der oberen Tabellen stehen für die Zuordnung von Warenhaus i zu Kunde j ", "dropCost_endInfo1",null, infoLineProp);
		
		int l=0;
		for (Integer n: warehouseOpen){
			if (n==-1)
				l++;
		}
		m -=zf;
		//System.out.println(m);
		ending( fixWarehouseCost, k, l, m);
		
		
		
		//System.out.println(" " + k);

	}

	
	
	private void questionCloseWarehouse(int closingPos, int[] warehouseOpen) {
        int i;
		
		MultipleSelectionQuestionModel msq = new MultipleSelectionQuestionModel(
                "multipleSelectionQuestion"+closingPos);
            
        
        msq.setPrompt("Welches Lagerhaus wird geschlossen?");
        for (i = 1; i<= l; i++){
        	if(i== (closingPos+1)){
			msq.addAnswer("Lagerhaus " + i, 1, "Richtig, Lagerhaus " + i + " bringt die größte Kostenersparnis");
        	}else if(warehouseOpen[i-1]==-1){
    		msq.addAnswer("Lagerhaus " + i, 1, "Falsch, Lagerhaus " + i + " ist schon geschlossen");
        	}else if(warehouseOpen[i-1]==1){
    		msq.addAnswer("Lagerhaus " + i, 1, "Falsch, Lagerhaus " + i + " ist schon offen");
        	}else{
    		msq.addAnswer("Lagerhaus " + i, 1, "Falsch, Lagerhaus " + i + " bringt nicht die größte Kostenersparnis");
        	}
        }
            msq.setGroupID("ClosingWarehouse");
            lang.addMSQuestion(msq);

		
	}

	private void refreshCosts (int f_sum, int d_sum, int zf){
		targetFunctionValueText.setText("Gesamtkosten: " + f_sum + "  + " + d_sum + " = " + zf, null, null);
	}

	private void demarkAllLines(Vector<Rect> markerAsigned) {
		for (Rect rect : markerAsigned) {
			rect.hide();		
		}
	}

	private Vector<Rect> markOpenedWarehouses(int[] warehouseOpen, int[] d) {
		Vector<Rect>  markerAsigned = new Vector<Rect>();   
		Node leftTopOffset,rightBottomOffset;
		String warehouses ="";
		int j;
		
		for (j = 0; j < l; j++) {
			if (d[j] <= 0 && warehouseOpen[j] == 0) {
				// Mark Line Delivery Rise
				leftTopOffset = new Offset(-15,-1, "dilveryRise_TableLeft_"+(j+1) ,AnimalScript.DIRECTION_NW);
				rightBottomOffset =new Offset(20,-1, "dilveryRise" + (r+2) + ":"+j,AnimalScript.DIRECTION_SW);
				markerAsigned.add(lang.newRect(leftTopOffset, rightBottomOffset, "markedOpenWarehouse_"+j, null, lineMarkRectProp));
				warehouses += " & " + (j+1);
				
				//Demark Number Delivery Rise		
				leftTopOffset = new Offset(-8,6, "dilveryRise_TableLeft_"+(j+1) ,AnimalScript.DIRECTION_NW);		
				rightBottomOffset =new Offset(10,-6, "dilveryRise_TableLeft_"+(j+1),AnimalScript.DIRECTION_SW);
				lang.newRect(leftTopOffset, rightBottomOffset, "markedOpenWarehouse_r"+j, null, lineMarkRectProp);
						
				//Demark Number Cost
				leftTopOffset = new Offset(-8,6, "dropCost_TableLeft_"+(j+1) ,AnimalScript.DIRECTION_NW);	
				rightBottomOffset =new Offset(10,-6, "dropCost_TableLeft_"+(j+1),AnimalScript.DIRECTION_SW);
				lang.newRect(leftTopOffset, rightBottomOffset, "markedOpenWarehouse_c"+j, null, lineMarkRectProp);
			}

		}
		if (warehouses.length()>0){
		warehouses = warehouses.substring(2);
		infoText1.setText("Makiere Lagerhaus " + warehouses, null, null);
		infoText2.setText("Dieses ist absofort immer geöffnet", null, null);
		}
		return markerAsigned;
	}

	private Vector<Rect> markAssignedCustomer(int[] warehouseOpen,
			int[] customerAssigned, int[][] w,int[] d) {
		Vector<Rect>  markerAsigned = new Vector<Rect>();
		Node leftTopOffset,rightBottomOffset;  
	int i,j;
	String name;
	
	for (j = 0; j < l; j++) {
		if (d[j] <= 0 && warehouseOpen[j] == 0) {
			for (i = 0; i < r; i++) {
	
				if (w[i][j] > 0) {
					name = "markedRiseCustomer_"+i;
					leftTopOffset = new Offset(-6,-1, "dilveryRise_TableHead_"+(i+1) ,AnimalScript.DIRECTION_NW);
					rightBottomOffset =new Offset(16,5, "dilveryRise"+i+":"+(l-1),AnimalScript.DIRECTION_SW);
					markerAsigned.add(lang.newRect(leftTopOffset, rightBottomOffset, name, null, lineMarkRectProp));
					
					name = "markedCostCustomer_"+i;
					leftTopOffset = new Offset(-6,-1, "dropCost_TableHead_"+(i+1) ,AnimalScript.DIRECTION_NW);
					rightBottomOffset =new Offset(16,2, "dropCostMin"+i+":1",AnimalScript.DIRECTION_SW);
					markerAsigned.add(lang.newRect(leftTopOffset, rightBottomOffset, name, null, lineMarkRectProp));
				}
			}
	
		}
	
	}
	return markerAsigned;
		
	}

	private Vector<Rect> markClosedWarehouses(int closingPos, DeliveryCost[][] bestCostToCustomers) {
		Vector<Rect>  markerAsigned = new Vector<Rect>();   
		Node leftTopOffset,rightBottomOffset;
		int i;
		if(closingPos>0){
		//delMarkRectProp
		// dilveryRise4:1
		//Demark Whole line Delivery Rise
		leftTopOffset = new Offset(-15,6, "dilveryRise_TableLeft_"+(closingPos+1) ,AnimalScript.DIRECTION_NW);
		rightBottomOffset =new Offset(20,-6, "dilveryRise"+(r+2)+":"+closingPos,AnimalScript.DIRECTION_SW);
		markerAsigned.add(lang.newRect(leftTopOffset, rightBottomOffset, "markedClosedWarehouse_"+(closingPos+1), null, delMarkRectProp));
		
		
		//Demark Number Delivery Rise		
		leftTopOffset = new Offset(-8,6, "dilveryRise_TableLeft_"+(closingPos+1) ,AnimalScript.DIRECTION_NW);		
		rightBottomOffset =new Offset(10,-6, "dilveryRise_TableLeft_"+(closingPos+1),AnimalScript.DIRECTION_SW);
		lang.newRect(leftTopOffset, rightBottomOffset, "markedClosedWarehouse_r"+(closingPos+1), null, delMarkRectProp);
				
		//Demark Number Cost
		leftTopOffset = new Offset(-8,6, "dropCost_TableLeft_"+(closingPos+1) ,AnimalScript.DIRECTION_NW);	
		rightBottomOffset =new Offset(10,-6, "dropCost_TableLeft_"+(closingPos+1),AnimalScript.DIRECTION_SW);
		lang.newRect(leftTopOffset, rightBottomOffset, "markedClosedWarehouse_c"+(closingPos+1), null, delMarkRectProp);
				
		
		
		infoText1.setText("Schliese Lagerhaus " +( closingPos+1)  + " für immer", null, null);
		infoText2.setText("Das Schliesen des Lagerhauses verringert die Gesamtenkosten ZF am meisten.", null, null);
		
		
			//System.out.println("Schliese Lagerhaus: " + (closingPos+1));
			CWD.closeWarehouse(closingPos);
			for (i = 0; i<r; i++){
				if (bestCostToCustomers[i][0].getWarehouse()==closingPos){
					CWD.hideConnection(closingPos, i);
				}
				
			}
			
		}
		else{
			infoText1.setText("Es wird kein Lagerhaus geschlossen", null, null);
			infoText2.setText("", null, null);
		}
		return markerAsigned;
	}


	private void clearDilveryRiseTable(Text[][] deliverRiseTable) {
		int i,j;
		// Insert/Update CostRise
		for (j = 0; j < lRise; j++) {
			for (i = 0; i < rRise; i++) {

				deliverRiseTable[i + 1][j + 1].setText("" , null,
						null);
			}
		}
		
		infoText1.setText("Bereite Tabelle für erneute Iteration vor", null, null);
		infoText2.setText("Leere Kostensteigerungstabelle", null, null);
		
			

	}

	private void clearMinCostsTable(Text[][] costsTable) {
		int i,j;
		
		// Insert/Update minCosts 
		for (j = l; j < l+2; j++) {
			for (i = 0; i < r; i++) {

				costsTable[i + 1][j + 1].setText("" , null,
						null);
			}
		}
		infoText1.setText("Füge nur Kosten für nicht zugeordnete Kunden und", null, null);
		infoText2.setText("nicht geschlossene Lagerhäuser ein", null, null);
	}

	private Vector<Rect> markMinimaCostsLine() {
		Vector<Rect> markedMiniCostLine = new Vector<Rect>();
		Node leftTopOffset,rightBottomOffset;
		leftTopOffset = new Offset(-5,-1, "dropCost_TableLeft_"+(l+2),AnimalScript.DIRECTION_NW);
		rightBottomOffset =new Offset(20,1, "dropCostMin"+(r-1)+":0",AnimalScript.DIRECTION_SW);
		markedMiniCostLine.add(lang.newRect(leftTopOffset, rightBottomOffset, "markedMinCosts", null, minRectProp));
		
		leftTopOffset = new Offset(-5,-1, "dropCost_TableLeft_"+(l+3),AnimalScript.DIRECTION_NW);
		rightBottomOffset =new Offset(20,1, "dropCostMin"+(r-1)+":1",AnimalScript.DIRECTION_SW);
		markedMiniCostLine.add(lang.newRect(leftTopOffset, rightBottomOffset, "markedSecMinCosts", null, secMinRectProp));
		return markedMiniCostLine;
	}



	private void demarkLinexExceptCostCustomer(Vector<Rect> markerMinCosts) {
		for (Rect rect : markerMinCosts) {
			if(!rect.getName().contains("markedCostCustomer_")){
			rect.hide();
			}
		
		}
		
	}

	private Vector<Rect> markMinCosts(DeliveryCost[][] bestCostToCustomers, int[] customerAssigned, Boolean end) {
		int i,customer,warehouse;
		String rectName,markName;
		Node leftTopOffset,rightBottomOffset;
		Vector<Rect> markedMinCosts = new Vector<Rect>();
		//CWD.hideAllConnection();
		for (i=0; i<r;i++){
			customer = bestCostToCustomers[i][0].getCustomer();
			warehouse = bestCostToCustomers[i][0].getWarehouse();
						
			CWD.showConnection(warehouse, customer);
			if (customerAssigned[i]==-1){
				// Mark min
				customer = bestCostToCustomers[i][0].getCustomer();
				warehouse = bestCostToCustomers[i][0].getWarehouse();
				rectName = "dropCost" + customer + ":" + warehouse;
				markName = "markMin"+customer+":"+warehouse;
				leftTopOffset = new Offset(-5,-1, rectName,AnimalScript.DIRECTION_NW);
				rightBottomOffset =new Offset(20,1, rectName,AnimalScript.DIRECTION_SW);
				markedMinCosts.add(lang.newRect(leftTopOffset, rightBottomOffset, markName, null, minRectProp)); 
	
				if(!end){
				// Mark 2nd min
				customer = bestCostToCustomers[i][1].getCustomer();
				warehouse = bestCostToCustomers[i][1].getWarehouse();
				rectName = "dropCost" + customer + ":" + warehouse;
				markName = "markMin"+customer+":"+warehouse;
				leftTopOffset = new Offset(-5,-1, rectName,AnimalScript.DIRECTION_NW);
				rightBottomOffset =new Offset(20,1, rectName,AnimalScript.DIRECTION_SW);
				markedMinCosts.add(lang.newRect(leftTopOffset, rightBottomOffset,markName, null, secMinRectProp)); 
				}
			}
			
		}
		
		infoText1.setText("Markiere minimale Kosten", null, null);
		infoText2.setText("", null, null);
		return markedMinCosts;
	}

	private int closeWarehouse(int[] warehouseOpen, int[] d) {
		int closingPos = findMax(d);
		if (closingPos >= 0) {
			warehouseOpen[closingPos] = -1;
		}
		return closingPos;
	}

	private void openWarehouseAssignCustomer(int[] warehouseOpen,
			int[] customerAssigned, int[][] w,int[] d) {
	int i,j;
	
	int k = 0;
	for (j = 0; j < l; j++) {
		if (d[j] <= 0 && warehouseOpen[j] == 0) {
			warehouseOpen[j] = 1;
			CWD.openWarehouse(j);
			for (i = 0; i < r; i++) {

				if (w[i][j] > 0) {

					k++;
					customerAssigned[i] = j;
					CWD.markConnection(j, i);
				}
			}

		}

	}
	infoText1.setText("Markiere " + k + " Kunden die nun fest Lagerhäusern zugeordnet sind", null, null);
	infoText2.setText("Diese Kunden vom markierten Lagerhaus zu beliefern ist am günstigsten.", null, null);
	
		
	}

	/*
	private void insertDilveryRise(Text[][] deliverRiseTable, int[][]w, int[] e, int[] f,
			int[] d, int[] customerAssigned, int[] warehouseOpen) {
		int i,j;
		String[] cost = new String[rRise];
		// Insert/Update CostRise
		for (j = 0; j < lRise; j++) {
			for (i = 0; i < rRise-3; i++) {

				if ((customerAssigned[i] != -1)||(warehouseOpen[j] != 0)|| (w[i][j]<0)) {
					cost[i] = "";

				} else {
					cost[i] = ""+ w[i][j];

				}
				deliverRiseTable[i + 1][j + 1].setText("" + cost[i], null,
						null);
			}
			

			if (warehouseOpen[j] != 0) {
				cost[rRise-3] = "";
				cost[rRise-2] = "";
				cost[rRise-1] = "";

			} else {
				cost[rRise-3] = ""+e[j];
				cost[rRise-2] = ""+f[j];
				cost[rRise-1] = ""+d[j];
			}
			
			deliverRiseTable[rRise-2][j + 1].setText("" + cost[rRise-3], null,
					null);
			deliverRiseTable[rRise-1][j + 1].setText("" + cost[rRise-2], null,
					null);
			
			deliverRiseTable[rRise-0][j + 1].setText("" + cost[rRise-1], null,
					null);
			}
			
			
		}
	
	*/
	
	private void insertDilveryRiseW(Text[][] deliverRiseTable, int[][]w, int[] customerAssigned, int[] warehouseOpen) {
		int i,j;
		String[] cost = new String[rRise];
		// Insert/Update CostRise
		for (j = 0; j < lRise; j++) {
			for (i = 0; i < rRise-3; i++) {

				if ((customerAssigned[i] != -1)||(warehouseOpen[j] != 0)|| (w[i][j]<0)) {
					cost[i] = "";

				} else {
					cost[i] = ""+ w[i][j];

				}
				deliverRiseTable[i + 1][j + 1].setText("" + cost[i], null,
						null);
			}
			
			
		}
		infoText1.setText("Bereche Kostensteigerung w_ij", null, null);
		//infoText2.setText("w_ij = c_i1,j - c_i2,j", null, null);
		infoText2.setText("w_ij entspricht Erhöhung der Kosten wenn das günstigste Lagerhaus für Kunde j schliest", null, null);
		
	}

	private void insertDilveryRiseE(Text[][] deliverRiseTable, int[] e,
			int[] customerAssigned, int[] warehouseOpen) {
		int j;
		// Insert/Update CostRise
		for (j = 0; j < lRise; j++) {
			if (warehouseOpen[j] == 0) {
				deliverRiseTable[rRise - 2][j + 1].setText("" + e[j], null,
						null);
			}

		}
		infoText1.setText("Bereche Summe der zusätzlichen Kosten E_i wenn Lagerhaus i geschlossen wird", null, null);
		infoText2.setText("E_i = Summe aller w_ij pro Zeile", null, null);

	}
	
	private void insertDilveryRiseF(Text[][] deliverRiseTable, int[] f,
			int[] customerAssigned, int[] warehouseOpen) {
		int  j;
		// Insert/Update CostRise
		for (j = 0; j < lRise; j++) {
			if (warehouseOpen[j] == 0) {
				deliverRiseTable[rRise - 1][j + 1].setText("" + f[j], null,
						null);
			}

		}

	}
	
	private void insertDilveryRiseD(Text[][] deliverRiseTable, int[] d,
			int[] customerAssigned, int[] warehouseOpen) {
		int  j;
		// Insert/Update CostRise
		for (j = 0; j < lRise; j++) {
			if (warehouseOpen[j] == 0) {
				deliverRiseTable[rRise - 0][j + 1].setText("" + d[j], null,
						null);
			}

		}
		infoText1.setText("Bereche gesamte Kostensteigerung d_i wenn Lagerhaus i geschlossen wird", null, null);
		infoText2.setText("d_i = f_i - E_i   =>   Koststeigerung = Gesparte Lagerhauskosten - Zusätzliche Transportwege", null, null);

	}
		
		
	

	private Text[][] drawDilveryRiseTable() {
		int i, j;

		int difCostDilveryHeader;

		Text[][] dilveryRiseTable = new Text[rRise+1][lRise+1];
		String[] tableHead ={"E","f_i","d_i"};
		
		difCostDilveryHeader = 4*lineMarginX+(l+4)*rowHeight+20;

		// Display Table Topic
		iterationText = lang.newText(new Offset(0,	difCostDilveryHeader, "dropCost_Header",AnimalScript.DIRECTION_NW), "Kostensteigerung","dilveryRise_Header", null, dropTableHeader);
				
		// Display Table Head/Left
		dilveryRiseTable[0][0] = lang.newText(new Offset(25,
				10, "dilveryRise_Header",
				AnimalScript.DIRECTION_SW), "w_ij",
				"dilveryRise_TableHead_0", null, dropCostProp);

		// Zeiche Tabelle
		// Kopfzeile
		Node[] dilveryRise_TableHeadLineNodes = {
				new Offset(0, lineMarginY, "dilveryRise_TableHead_0",
						AnimalScript.DIRECTION_SW),
				new Offset(rRise * colWidth + 2 * lineMarginX, lineMarginY,
						"dilveryRise_TableHead_0", AnimalScript.DIRECTION_SE) };
		lang.newPolyline(dilveryRise_TableHeadLineNodes, "dilveryRise_TableHeadLine",
				null);

		

		// Fülle Tabelle
		// Kopfzeile
		for (i = 0; i < rRise-3; i++) {
			dilveryRiseTable[i + 1][0] = lang.newText(new Offset((int) ((i + 0.5)
					* colWidth + 2 * lineMarginX), 0, "dilveryRise_TableHead_0",
					AnimalScript.DIRECTION_NE), "" + (i + 1),
					"dilveryRise_TableHead_" + (i + 1), null,
					dropCost_TableHeadProp);
		}
		
		for (i = rRise-3; i < rRise; i++) {
			dilveryRiseTable[i + 1][0] = lang.newText(new Offset((int) ((i + 0.5)
					* colWidth + 2 * lineMarginX), 0, "dilveryRise_TableHead_0",
					AnimalScript.DIRECTION_NE), "" +tableHead[i-(rRise-3)],
					"dilveryRise_TableHead_" + (i + 1), null,
					dropCost_TableHeadProp);
		}
		// Linke Spalte
		for (j = 0; j < lRise; j++) {
			dilveryRiseTable[0][j + 1] = lang.newText(new Offset(0, (int) ((j + 0.0)
					* rowHeight + 2 * lineMarginY), "dilveryRise_TableHead_0",
					AnimalScript.DIRECTION_S), "" + (j + 1),
					"dilveryRise_TableLeft_" + (j + 1), null, dropCostProp);
		}
				
		// Vertical Line
		Node[] dropCost_TableLeftLineNodes = {
				new Offset(lineMarginX, 0, "dilveryRise_TableHead_0",
						AnimalScript.DIRECTION_NE),
				new Offset(lineMarginX, (lRise+1) * rowHeight + 2 * lineMarginY,
						"dilveryRise_TableHead_0", AnimalScript.DIRECTION_NE) };
		lang.newPolyline(dropCost_TableLeftLineNodes,
				"dilveryRise_TableLeftLineNodes", null);
		
		// E
		Node[] dropCost_TableLeftLineNodesE = {
				new Offset(3*lineMarginX+(l+2)*colWidth, 0, "dilveryRise_TableHead_0",
						AnimalScript.DIRECTION_NE),
				new Offset(3*lineMarginX+(l+2)*colWidth, (lRise+1) * rowHeight + 2 * lineMarginY,
						"dilveryRise_TableHead_0", AnimalScript.DIRECTION_NE) };
		lang.newPolyline(dropCost_TableLeftLineNodesE,
				"dilveryRise_TableLeftLineNodesE", null);
		
		// F
		Node[] dropCost_TableLeftLineNodesF = {
				new Offset(3*lineMarginX+(l+3)*colWidth, 0, "dilveryRise_TableHead_0",
						AnimalScript.DIRECTION_NE),
				new Offset(3*lineMarginX+(l+3)*colWidth, (lRise+1) * rowHeight + 2 * lineMarginY,
						"dilveryRise_TableHead_0", AnimalScript.DIRECTION_NE) };
		lang.newPolyline(dropCost_TableLeftLineNodesF,
				"dilveryRise_TableLeftLineNodesF", null);
		
		// D
		Node[] dropCost_TableLeftLineNodesD = {
				new Offset(3*lineMarginX+(l+4)*colWidth, 0, "dilveryRise_TableHead_0",
						AnimalScript.DIRECTION_NE),
				new Offset(3*lineMarginX+(l+4)*colWidth, (lRise+1) * rowHeight + 2 * lineMarginY,
						"dilveryRise_TableHead_0", AnimalScript.DIRECTION_NE) };
		lang.newPolyline(dropCost_TableLeftLineNodesD,
				"dilveryRise_TableLeftLineNodesD", null);
		
		//Costs
		for (j = 0; j < lRise; j++) {
			for (i = 0; i < rRise; i++) {
				dilveryRiseTable[i + 1][j + 1] = lang.newText(new Offset(
						(int) ((i + 0.5) * colWidth + 2 * lineMarginX),
						(int) ((j + 0.0) * rowHeight + 2 * lineMarginY),
						"dilveryRise_TableHead_0", AnimalScript.DIRECTION_SE), "",
						"dilveryRise" + i + ":" + j, null, dropCostProp);
			}
		}
		
		infoText1.setText("Erstelle Kostensteigerungstabelle", null, null);
		infoText2.setText("", null, null);
		return dilveryRiseTable;
	}

	private void insertCosts(Text[][] costsTable, int[][] costs, int[] customerAssigned, int[] warehouseOpen) {
		int i,j;
		String cost = "";
		// Insert/Update Cost
		for (j = 0; j < l; j++) {
			for (i = 0; i < r; i++) {

				if (customerAssigned[i] != -1 || warehouseOpen[j]== -1) {
					cost = "";

				} else {
					cost = ""+ costs[i][j];

				}
				costsTable[i + 1][j + 1].setText("" + cost, null,
						null);
			}
		}
		

	}
	
	private void insertMinCosts(Text[][] costsTable, DeliveryCost[][] bestCostToCustomers, int[] customerAssigned, boolean end) {
		int i,j;
		String cost = "";
		
		// Insert/Update minCosts 
		for (j = l; j < l+2; j++) {
			for (i = 0; i < r; i++) {

				if (customerAssigned[i] != -1 || (end && j == l+1)) {
					cost = "";

				} else {
					cost = ""+ bestCostToCustomers[i][j-l].getCost();

				}
				costsTable[i + 1][j + 1].setText("" + cost, null,
						null);
			}
		}
		infoText1.setText("Füge Minimalekosten in c_i1 und c_i2 Zeile ein", null, null);
		infoText2.setText("", null, null);

	}

	private Text[][] drawCostTable() {
		int i, j;
		Text[][] costsTable = new Text[r + 1][l + 3];
		// Display Table Head/Left

		costsTable[0][0] = lang.newText(new Offset(10, 0, "dropCost_Header",
				AnimalScript.DIRECTION_SW), "c_ij*r_j", "dropCost_TableHead_0",
				null, dropCost_TableHeadProp);
		lang.nextStep();
		// Zeiche Tabelle
		// Kopfzeile
		Node[] dropCost_TableHeadLineNodes = {
				new Offset(0, lineMarginY, "dropCost_TableHead_0",
						AnimalScript.DIRECTION_SW),
				new Offset(r * colWidth + 2 * lineMarginX, lineMarginY,
						"dropCost_TableHead_0", AnimalScript.DIRECTION_SE) };
		lang.newPolyline(dropCost_TableHeadLineNodes, "dropCost_TableHeadLine",
				null);

		// Fusszeile
		Node[] dropCost_TableFootLineNodes = {
				new Offset(0, (int) (2 * lineMarginY + (l) * rowHeight),
						"dropCost_TableHead_0", AnimalScript.DIRECTION_SW),
				new Offset(r * colWidth + 2 * lineMarginX,
						(int) (2 * lineMarginY + (l) * rowHeight),
						"dropCost_TableHead_0", AnimalScript.DIRECTION_SE) };
		lang.newPolyline(dropCost_TableFootLineNodes, "dropCost_TableFootLine",
				null);

		// Fülle Tabelle
		// Kopfzeile
		for (i = 0; i < r; i++) {
			costsTable[i + 1][0] = lang.newText(new Offset((int) ((i + 0.5)
					* colWidth + 2 * lineMarginX), 0, "dropCost_TableHead_0",
					AnimalScript.DIRECTION_NE), ((i==0)?"j=":"") + (i + 1),
					"dropCost_TableHead_" + (i + 1), null,
					dropCost_TableHeadProp);
		}
		// Linke Spalte
		for (j = 0; j < l; j++) {
			costsTable[0][j + 1] = lang.newText(new Offset(0, (int) ((j + 0.0)
					* rowHeight + 2 * lineMarginY), "dropCost_TableHead_0",
					AnimalScript.DIRECTION_S), ((j==0)?"i=":"") + (j + 1),
					"dropCost_TableLeft_" + (j + 1), null, dropCostProp);
		}
		// Fusszeile
		costsTable[l + 1][0] = lang.newText(new Offset(0, (int) ((l + 0.0)
				* rowHeight + 3 * lineMarginY), "dropCost_TableHead_0",
				AnimalScript.DIRECTION_SW), "c_i1,j", "dropCost_TableLeft_"
				+ (l + 2), null, dropCostProp);
		costsTable[l + 2][0] = lang.newText(new Offset(0, (int) ((l + 1 + 0.0)
				* rowHeight + 3 * lineMarginY), "dropCost_TableHead_0",
				AnimalScript.DIRECTION_SW), "c_i2,j", "dropCost_TableLeft_"
				+ (l + 3), null, dropCostProp);
		
		// Vertical Line
		Node[] dropCost_TableLeftLineNodes = {
				new Offset(lineMarginX, 0, "dropCost_TableHead_0",
						AnimalScript.DIRECTION_NE),
				new Offset(lineMarginX, (l + 3) * rowHeight + 2 * lineMarginY,
						"dropCost_TableHead_0", AnimalScript.DIRECTION_NE) };
		lang.newPolyline(dropCost_TableLeftLineNodes,
				"dropCost_TableLeftLineNodes", null);
		//Costs
		for (j = 0; j < l; j++) {
			for (i = 0; i < r; i++) {
				costsTable[i + 1][j + 1] = lang.newText(new Offset(
						(int) ((i + 0.5) * colWidth + 2 * lineMarginX),
						(int) ((j + 0.0) * rowHeight + 2 * lineMarginY),
						"dropCost_TableHead_0", AnimalScript.DIRECTION_SE), "",
						"dropCost" + i + ":" + j, null, dropCostProp);
			}
		}
		// min Cost
		for (j = l; j < l+2; j++) {
			for (i = 0; i < r; i++) {
				costsTable[i + 1][j + 1] = lang.newText(new Offset(
						(int) ((i + 0.5) * colWidth + 2 * lineMarginX),
						(int) ((j + 0.0) * rowHeight + 3 * lineMarginY),
						"dropCost_TableHead_0", AnimalScript.DIRECTION_SE), "",
						"dropCostMin" + i + ":" + (j-l), null, dropCostProp);
			}
		}

		infoText1.setText("Erstelle Kostentabelle", null, null);
		infoText2.setText("", null, null);
		return costsTable;
	}

	/**
	 * Calculates the Minimum costs to delivery all Customers in the current
	 * state
	 * 
	 * @param bestCostToCustomers
	 *            an array with the minimum and second minimum transport costs
	 *            to an customer
	 * @return the sum of the acutally minimum transport costs
	 */
	private int calcDeliveryCost(DeliveryCost[][] bestCostToCustomers) {
		int sum = 0;
		int r = bestCostToCustomers.length;
		int i;
		for (i = 0; i < r; i++) {
			sum += bestCostToCustomers[i][0].getCost();
		}
		return sum;
	}

	/**
	 * Calculates the sum of costs for all currently opend warehouses
	 * 
	 * @param f
	 *            the fixcosts that falls apart if an warehouse is open
	 * @param warehouseOpen
	 *            Array for descriping if an warehouse is open oder closed
	 * @return the sum of all currentyl open warehouses
	 */
	private int calcSumWarehouseCost(int[] f, int[] warehouseOpen) {
		int sum = 0;
		int l = f.length;
		int j;
		for (j = 0; j < l; j++) {
			if (warehouseOpen[j] >= 0) {
				sum += f[j];
			}
		}
		return sum;
	}

	/**
	 * Calculates the minimum and second minimum Cost to delivery a customer
	 * from an warehouse
	 * 
	 * @param costs
	 *            matrix of delivery cost from each warehouse to each custimer
	 * @param warehouseOpen
	 *            Array for descriping if an warehouse is open oder closed
	 * @param customerAssigned
	 *            is customer delivery from an specific warehouse
	 * @return
	 */
	private DeliveryCost[][] calcBestCostToCustomers(int[][] costs,
			int[] warehouseOpen, int[] customerAssigned) {

		
		int r = costs.length;
		DeliveryCost[][] bestCostToCustomers = new DeliveryCost[r][2];
		int j;
		
		
		for (j = 0; j < r; j++) {
			// if (customerAssigned[j] == -1) {
			bestCostToCustomers[j][0] = findMin(costs[j], j, warehouseOpen);
			bestCostToCustomers[j][1] = findSecondMin(costs[j], j,
					warehouseOpen);
		
						/*
			 * } else { bestCostToCustomers[j][0] = new deliveryCost(0, 0, 0);
			 * bestCostToCustomers[j][1] = new deliveryCost(0, 0, 0); }
			 */
		}
		return bestCostToCustomers;
	}

	/**
	 * Check if alle Warehouses are closed or defenitly open
	 * 
	 * @param warehouseOpen
	 *            Array for descriping if an warehouse is open oder closed
	 * @return false if all warehouses are openor closed
	 */
	private boolean abort(int[] warehouseOpen) {
		int r = warehouseOpen.length;
		int i;
		for (i = 0; i < r; i++) {
			if (warehouseOpen[i] == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Finds the maximum in an Array d
	 * 
	 * @param d
	 *            one-dimensional Array
	 * @return the position in array d of the maximum
	 */
	private int findMax(int[] d) {
		int max = Integer.MIN_VALUE;
		int maxPos = -1;
		int i;
		for (i = 0; i < d.length; i++) {
			if (max < d[i] && d[i] > 0) {
				max = d[i];
				maxPos = i;
			}
		}

		return maxPos;

	}

	/**
	 * Calculate the rise in delivery costs if an warehouse j is closed
	 * 
	 * @param w
	 *            the matrix with delivery Cost rise for customer i if warehouse
	 *            j is closed
	 * @param warehouseOpen
	 *            Array for descriping if an warehouse is open oder closed
	 * @return an Int-Array with Costs if warehouse j is closed
	 */
	private int[] calcDeliveryCostRiseSum(int[][] w, int[] warehouseOpen) {
		int r = w.length;
		int l = w[0].length;

		int[] e = new int[l];

		int i, j;
		for (j = 0; j < l; j++) {
			e[j] = 0;
			if (warehouseOpen[j] != 0)
				continue;

			for (i = 0; i < r; i++) {
				if(w[i][j] >=0){
				e[j] += w[i][j];
				}
			}

		}
		return e;

	}

	/**
	 * Calculates the cost saving or rising if an warehouse j is closed
	 * 
	 * @param e
	 *            the sum of cost rising if warehouse j is closed
	 * @param f
	 *            the fix costs per Warehouse
	 * @param warehouseOpen
	 *            the open or closed warehouses
	 * @return an array with cost saving/rising per warehouse
	 */
	private int[] calcCostSaving(int[] e, int[] f, int[] warehouseOpen) {

		int l = e.length;

		int[] d = new int[l];

		int j;
		for (j = 0; j < l; j++) {
			if (warehouseOpen[j] != 0)
				continue;
			d[j] = f[j] - e[j];
		}

		return d;
	}

	/**
	 * 
	 * @param arr
	 * @param j
	 * @param warehouseOpen
	 * @return
	 */
	private DeliveryCost findMin(int[] cost, int j, int[] warehouseOpen) {
		int min = Integer.MAX_VALUE;
		int minPos = -1;
		int i;
		for (i = 0; i < cost.length; i++) {
			if (warehouseOpen[i] < 0)
				continue;
			if (min >= cost[i]) {
				min = cost[i];
				minPos = i;
			}
		}

		return new DeliveryCost(min, j, minPos);

	}

	/**
	 * 
	 * @param arr
	 * @param j
	 * @param warehouseOpen
	 * @return
	 */
	private DeliveryCost findSecondMin(int[] arr, int j, int[] warehouseOpen) {
		int firstMin = Integer.MAX_VALUE;
		int secondMin = Integer.MAX_VALUE;
		int firstMinPos = -1;
		int secondMinPos = -1;
		int i;
		for (i = 0; i < arr.length; i++) {
			if (warehouseOpen[i] < 0)
				continue;
			// new firstMininum
			if (arr[i] <= firstMin) {
				secondMin = firstMin;
				secondMinPos = firstMinPos;
				firstMin = arr[i];
				firstMinPos = i;

			}
			// new secondMinimum
			else if ((firstMin < arr[i]) && (arr[i] < secondMin)) {
				secondMin = arr[i];
				secondMinPos = i;
			}
		}

		return new DeliveryCost(secondMin, j, secondMinPos);

	}

	/**
	 * 
	 * @param r
	 * @param l
	 * @param bestCostToCustomer
	 * @param warehouseOpen
	 * @return
	 */
	private int[][] calcDeliveryCostRise(DeliveryCost[][] bestCostToCustomer,
			int[] warehouseOpen) {
		int i, j;
		int[][] deliverCostRise = new int[r][l];
		for (i = 0; i < r; i++) {

			for (j = 0; j < l; j++) {
				// Is warehouse closed or open? -> continue
				if (warehouseOpen[j] != 0)
					continue;
				if (bestCostToCustomer[i][0].getWarehouse() == j) {
					deliverCostRise[i][j] = bestCostToCustomer[i][1].getCost()
							- bestCostToCustomer[i][0].getCost();

				}

				else {
					deliverCostRise[i][j] = -1;
				}
			}
		}
		return deliverCostRise;
	}

	private void introduction(int[][] deliveryCost, int[] fixWarehouseCost) {
		// Start

		int i;
		List<Text> introList = new ArrayList<Text>();
		introList.add(lang.newText(new Coordinates(10,40), "Einleitung", "dropCost_into_Header", null,
				dropIntoHeader));
		
		Node leftTopOffset,rightBottomOffset;
		leftTopOffset = new Offset(-5,-5, "dropCost_into_Header",AnimalScript.DIRECTION_NW);
		rightBottomOffset =new Offset(7,5, "dropCost_into_Header",AnimalScript.DIRECTION_SE);
		Rect background = lang.newRect(leftTopOffset, rightBottomOffset, "backgroundRectInto", null,backgroundHeaderRectProp);
		
		
		
		
		introList.add(	lang.newText(new Offset(25,40,"dropCost_into_Header",AnimalScript.DIRECTION_NW), "Grundidee des Drop-Verfahrens", "dropCost_into_Header2", null,
						dropIntoHeader2));
								
				introList.add(	lang.newText(new Offset(10,20,"dropCost_into_Header2",AnimalScript.DIRECTION_NW), "Der Drop-Algorithmus ist eine Heuristik zur Lösung eines diskreten Warehouse Location Problem.", "dropCost_introProblem1",null, infoLineProp));
				introList.add(	lang.newText(new Offset(0,15,"dropCost_introProblem1",AnimalScript.DIRECTION_NW), "In diesem Problem geht es um die Fragen welche Lagerhäuser, von einer gegeben", "dropCost_introProblem2",null, infoLineProp));
				introList.add(	lang.newText(new Offset(0,30,"dropCost_introProblem1",AnimalScript.DIRECTION_NW), "Menge an Lagerhäuser weiterhin betrieben werden soll. Das Ziel ", "dropCost_introProblem3",null, infoLineProp));
				introList.add(	lang.newText(new Offset(0,45,"dropCost_introProblem1",AnimalScript.DIRECTION_NW), "ist die minimierung von fixen Kosten für das Betreiben von Lagerhäusern sowie", "dropCost_introProblem4",null, infoLineProp));
				introList.add(	lang.newText(new Offset(0,60,"dropCost_introProblem1",AnimalScript.DIRECTION_NW), "den anfallenden Transportkosten von Lagerhäusern zu Kunden.", "dropCost_introProblem5",null, infoLineProp));
				
				
				
				introList.add(lang.newText(new Offset(10,115,"dropCost_into_Header2",AnimalScript.DIRECTION_NW), "Der Drop-Algorithmus geht von der Startsituation aus, dass alle potentiellen", "dropCost_introIdea1",null, infoLineProp));
				introList.add(lang.newText(new Offset(0,15,"dropCost_introIdea1",AnimalScript.DIRECTION_NW), "Lagerstandorte  vorläufig einbezogen sind. Zu Beginn entspricht der Ziel-", "dropCost_introIdea2",null, infoLineProp));
				introList.add(lang.newText(new Offset(0,30,"dropCost_introIdea1",AnimalScript.DIRECTION_NW), "funktionswert der Summe der Fixkosten aller Standorte und der minimalen", "dropCost_introIdea3",null, infoLineProp));
				introList.add(lang.newText(new Offset(0,45,"dropCost_introIdea1",AnimalScript.DIRECTION_NW), "Transportkosten.", "dropCost_introIdea4",null, infoLineProp));
				introList.add(lang.newText(new Offset(0,60,"dropCost_introIdea1",AnimalScript.DIRECTION_NW), "Mit jeder Iteration wird nun derjenige Standort endgultig ausgeschlossen, durch", "dropCost_introIdea5",null, infoLineProp));
				introList.add(lang.newText(new Offset(0,75,"dropCost_introIdea1",AnimalScript.DIRECTION_NW), "dessen Schließung die größtmögliche Verringerung des Zielfunktionswertes erlangt wird.", "dropCost_introIdea6",null, infoLineProp));
				introList.add(lang.newText(new Offset(0,90,"dropCost_introIdea1",AnimalScript.DIRECTION_NW), "Der Algorithmus endet, sobald durch die Schließung eines weiteren", "dropCost_introIdea7",null, infoLineProp));
				introList.add(lang.newText(new Offset(0,105,"dropCost_introIdea1",AnimalScript.DIRECTION_NW), "Standortes keine zusatzliche Senkung des Zielfunktionswertes erreicht werden", "dropCost_introIdea8",null, infoLineProp));
				introList.add(lang.newText(new Offset(0,120,"dropCost_introIdea1",AnimalScript.DIRECTION_NW), "kann. Alle Standorte, die nicht ausgeschlossen wurden, werden nun endgültig", "dropCost_introIdea9",null, infoLineProp));
				introList.add(lang.newText(new Offset(0,135,"dropCost_introIdea1",AnimalScript.DIRECTION_NW), "einbezogen. [SCHILDT 1994]", "dropCost_introIdea10",null, infoLineProp));
				// Warehouse-Location-Problem
				
				
				
				lang.nextStep("Einleitung");
				for (i=0; i< introList.size();i++){
					introList.get(i).hide();
				}
				background.hide();
	}
	
	private void ending(int[] fixWarehouseCost,int k, int n, int m) {
		// Start

		
		List<Text> endingList = new ArrayList<Text>();
		
		endingList.add(lang.newText(new Coordinates(10,40), "Abschlussfolie", "dropCost_end_Header", null,
						dropIntoHeader));
		
		Node leftTopOffset,rightBottomOffset;
		leftTopOffset = new Offset(-5,-5, "dropCost_end_Header",AnimalScript.DIRECTION_NW);
		rightBottomOffset =new Offset(7,5, "dropCost_end_Header",AnimalScript.DIRECTION_SE);
		lang.newRect(leftTopOffset, rightBottomOffset, "backgroundRectEnding", null,backgroundHeaderRectProp);
		
		endingList.add(	lang.newText(new Offset(25,40,"dropCost_end_Header",AnimalScript.DIRECTION_NW), "Zusammenfassung", "dropCost_end_Header2", null,
						dropIntoHeader2));
								
		endingList.add(	lang.newText(new Offset(10,20,"dropCost_end_Header2",AnimalScript.DIRECTION_NW), "Der Drop-Algorithmus hat innerhalb von " + k + " Iterationen eine Lösung zu unseren Problem gefunden.", "dropCost_endProblem1",null, infoLineProp));
		endingList.add(	lang.newText(new Offset(0,15,"dropCost_endProblem1",AnimalScript.DIRECTION_NW), "Dabei haben wir " + n + " Lagerhäuser geschlossen und unsere Kosten ingesamt um " + m + " Einheiten reduziert.", "dropCost_endProblem2",null, infoLineProp));
		endingList.add(	lang.newText(new Offset(0,30,"dropCost_endProblem1",AnimalScript.DIRECTION_NW), "Somit beliefern wir unsere " + l + "Kunden aus " + (r-n) + "Warenhäusern.", "dropCost_endProblem3",null, infoLineProp));
				
		endingList.add(lang.newText(new Offset(10,110,"dropCost_end_Header2",AnimalScript.DIRECTION_NW), "Wir hoffen euch ist die funktionsweise des Algorithmuses nun besser verständlich", "dropCost_endIdea1",null, infoLineProp));
		endingList.add(lang.newText(new Offset(0,15,"dropCost_endIdea1",AnimalScript.DIRECTION_NW), "und ihr seid in der Lage diesen nun auf Papier durch zuführen", "dropCost_endIdea2",null, infoLineProp));
		endingList.add(lang.newText(new Offset(0,30,"dropCost_endIdea1",AnimalScript.DIRECTION_NW), "", "dropCost_endIdea3",null, infoLineProp));
		endingList.add(lang.newText(new Offset(0,45,"dropCost_endIdea1",AnimalScript.DIRECTION_NW), "Wir bedanken uns für deine Aufmerksamkeit und wünschen dir viel Spass mit Animal", "dropCost_endIdea4",null, infoLineProp));
				// Warehouse-Location-Problem
				
				
				
		lang.nextStep("Ende");
		endingList.add(lang.newText(new Offset(50,130,"dropCost_endIdea1",AnimalScript.DIRECTION_NW), "ENDE", "dropCost_end_Finsh", null,dropIntoHeader));
				/*for (i=0; i< endingList.size();i++){
					endingList.get(i).hide();
				}
				*/
	}

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
 	
    	 System.out.println("Generate");
    	
         MinimaleKosten = (RectProperties)props.getPropertiesByName("MinimaleKosten");
         WarenhausGeschlossenDiagram = (Color)primitives.get("WarenhausGeschlossenDiagram");
         PfeilFarbeZugeordnet = (Color)primitives.get("PfeilFarbeZugeordnet");
         WarenhausOffenDiagram = (Color)primitives.get("WarenhausOffenDiagram");
         ZweitGeringstenKosten = (RectProperties)props.getPropertiesByName("ZweitGeringstenKosten");
         PfeilFarbeVerbunden = (Color)primitives.get("PfeilFarbeVerbunden");
         HervorhebungWarenhausGeschlossen = (RectProperties)props.getPropertiesByName("HervorhebungWarenhausGeschlossen");
         Warenhausfixkosten = (int[])primitives.get("Warenhausfixkosten");
         HervorhebungWarenhausOffen = (RectProperties)props.getPropertiesByName("HervorhebungWarenhausOffen");
         TransportkostenMatrix = (int[][])primitives.get("TransportkostenMatrix");
        
        // Create a new animation

     		//dropHeuristik d = new dropHeuristik();
      //  System.out.println(primitives.get("Transportkosten Matrix").toString());
     		
       
        
         //Tansform TransportkostenMatrix into deliveryCost

         int rTran = TransportkostenMatrix[1].length;
         int lTran = TransportkostenMatrix.length;
         int i,j;

         // Delivery Costs seperated by Customer
         int[][] deliveryCost = new int[rTran][lTran];


         for (i = 0 ; i< rTran;i++){

        	 for (j = 0 ; j< lTran;j++){
        		 deliveryCost[i][j] = TransportkostenMatrix[j][i];
        	 }
         }

        	 /*{ { 1, 2, 7, 6, 6 }, { 2, 9, 6, 5, 4 },
     				{ 10, 0, 1, 10, 6 }, { 9, 7, 5, 2, 3 }, { 6, 3, 3, 6, 7 },
     				{ 7, 6, 10, 3, 2 }, { 3, 10, 5, 6, 6 } };
        	  */


     		// Fix costs if warehoue is open
     		int[] fixWarehouseCost = Warenhausfixkosten;//{ 5, 7, 5, 6, 5 };
     		//Count Customers
    		r = deliveryCost.length;
    		// Count Warehouses
    		l = deliveryCost[1].length;

    		System.out.println("Kunden: " + r);
    		
    		System.out.println("Warenhäuser: " + l);
    		
    		
    		
    		//Size of DilveryRiseTable
    		rRise = r+3;
    		lRise = l;	
     		init();

     		introduction(deliveryCost, fixWarehouseCost);
     		// Solve Problwem
     		solve(deliveryCost, fixWarehouseCost);


     		// Output the .asu file in ./bin folder		
     		
     		//saveToFile("drop.asu");

        
     	lang.finalizeGeneration();
        return lang.toString();
    }

  // private void saveToFile(String filename){
  //
  // filename = "drop.asu";
  // BufferedWriter out;
  // try {
  // out = new BufferedWriter(new OutputStreamWriter(new
  // FileOutputStream(filename),"UTF8"));
  //
  // try {
  // out.write(lang.toString());
  // out.close();
  // System.out.println(filename + " abgespeichert im Ordner");
  // System.out.println(System.getProperty("user.dir"));
  // } catch (IOException e) {
  //
  // e.printStackTrace();
  // }
  // } catch (UnsupportedEncodingException e) {
  //
  // e.printStackTrace();
  // } catch (FileNotFoundException e) {
  //
  // e.printStackTrace();
  // }
  //
  // }
    public String getName() {
        return "Drop-Heuristik [DE]";
    }

    public String getAlgorithmName() {
        return "Drop-Heuristik";
    }

    public String getAnimationAuthor() {
        return "Julian Bonrath, Benjamin Björngen-Schmidt";
    }

    public String getDescription(){
        return "<h1> Drop-Heuristik zum diskreten Warehouse-Location-Problem</h1>"
 +"\n"
 +"\n"
 +"Der Drop-Algorithmus ist eine Heuristik zur Lösung eines diskreten Warehouse Location Problem.<br>"
 +"\n"
 +"In diesem Problem geht es um die Fragen welche Lagerhäuser, von einer gegeben<br>"
 +"\n"
 +"Menge an Lagerhäuser weiterhin betrieben werden soll. Das Ziel<br>"
 +"\n"
 +"ist die minimierung von fixen Kosten für das Betreiben von Lagerhäusern sowie<br>"
 +"\n"
 +"den anfallenden Transportkosten von Lagerhäusern zu Kunden.<br>";
    }

    public String getCodeExample(){
        return "Erläuterung:"
 +"\n"
 +"  Menge aller Lagerhäuser I"
 +"\n"
 +"  c_i1,j := Transportkosten von Kunde j zum 'nächstgelegenen' Lager i1"
 +"\n"
 +"  c_i2,j := Transportkosten von Kunde j zu 'übernächstem' Lager i2"
 +"\n"
 +"\n"
 +"\n"
 +"Iteration:"
 +"\n"
 +"  a) Minimale Kosten"
 +"\n"
 +"    Finde minimale Kosten c_i1, c_i2 für alle j und i aus I"
 +"\n"
 +"    Trage geringste  Kosten in Zeilen c_i1 und "
 +"\n"
 +"    zweitgeringste Kosten in Zeilen c_i2 ein"
 +"\n"
 +"  b) Kostenerhöhung"
 +"\n"
 +"    Berechnung der Transportkostenerhöhung w_ij (zu Kunde j) bei"
 +"\n"
 +"    Schließung von Lager i aus I:"
 +"\n"
 +"    w_ij = c_i2 - c_i1 , falls i = i1 (Differenz der geringsten und zweitgeringsten Kosten)"
 +"\n"
 +"    w_ij = 0           , sonst"
 +"\n"
 +"	"
 +"\n"
 +"    Bereche Transportkostenerhöhung bei Schließung des Lagers i"
 +"\n"
 +"    E_i = Summe_i (w_ij) für alle i aus I"
 +"\n"
 +"  c) Kostenersparniss"
 +"\n"
 +"    Berechne Gesamtkostenersparniss d_i bei Schließung des Lagers i"
 +"\n"
 +"    d_i = f_i - E_i    für alle nicht geschlossen Lager"
 +"\n"
 +"    Falls d_i <= 0 dann bleibt Lager i immer offen"
 +"\n"
 +"  d) Schließen"
 +"\n"
 +"    Schließung des Lagers k mit der größten Einsparung"
 +"\n"
 +"  k = argmax {d_i} für die gilt d_i > 0 und i aus I"
 +"\n"
 +"  I := I /k"
 +"\n"
 +"  e) Abbruch?"
 +"\n"
 +"    falls d_i <= 0 für alle i aus I";
    }

    public String getFileExtension(){
        return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

    public Locale getContentLocale() {
        return Locale.GERMANY;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}