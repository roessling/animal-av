package generators.misc;

import java.awt.Color;
import java.awt.Font;

import translator.Translator;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.variables.Variable;
import animal.variables.VariableRoles;

public class WagnerWithin {

    /**
     * The concrete language object used for creating output
     */
    private Language lang;
	
	/**
	 * 2-Dimensionales Array der Inputwerte
	 * [0] = Bedarf der Periode
	 * [1] = Produktionskosten pro Einheit der Periode
	 * [2] = Rüstkosten eines Rüstvorgangs der Periode
	 * [3] = Bestandsführungskosten je Einheit der Periode
	 */
	private int[][] input; 
	private int[][] output;// Ergebnismatrix
	private int[] costs_of_period;
	private int[][] best_production_periods;
	private int no_of_periods;
	private int lastproductionperiod=0;
	private StringMatrix OutputStringMatrix;
	private StringMatrix OutputLabelTop;
	private StringMatrix OutputLabelLeft;
	private String[][] Stringoutput;
	private String[][] StringOutputLabelLeft;
	private String[][] StringOutputLabelTop;
	private StringMatrix InputStringMatrix;
	private StringMatrix InputLabelTop;
	private StringMatrix InputLabelLeft;
	private String[][] StringInput;
	private String[][] StringInputLabelLeft;
	private String[][] StringInputLabelTop;
	private Text Berechnungen;
	private Text Berechnungen2;
	private Text title;
	private Text Berechnungstitel;
	private int [][] Ergebnis;
	private StringMatrix ResultMatrix;
	private Text Ergebnistitel;
	private Variables variables;
	private TwoValueCounter InputCounter;
	private TwoValueCounter OutputCounter;
	private TwoValueView InputCounterView;
	private TwoValueView OutputCounterView;
	private Text OutputCounterTitle;
	private Text InputCounterTitle;
	
	
    /**
     * Default constructor
     * @param l the conrete language object used for creating output
     */
    public WagnerWithin(Language l) {
	// Store the language object
	lang = l;
	// This initializes the step mode. Each pair of subsequent steps has to
	// be divdided by a call of lang.nextStep();
	lang.setStepMode(true);
    }
    
    public void wagner_within(int[][] inputmatrix,MatrixProperties ResultMatrixprops,MatrixProperties InputMatrixprops, MatrixProperties InputLabelLeftprops,MatrixProperties InputLabelTopprops, MatrixProperties OutputMatrixprops,MatrixProperties OutputLabelTopprops, MatrixProperties OutputLabelLeftprops,SourceCodeProperties sourceCodeprops,SourceCodeProperties bigsourceCodeprops, SourceCodeProperties descriptionprops, Translator translator ){
		
		//Initialisierung der Variablen
		input = inputmatrix;
		no_of_periods= input[0].length;
		output = new int[no_of_periods][no_of_periods];
		Stringoutput = new String[no_of_periods+1][no_of_periods];
		StringOutputLabelLeft = new String[no_of_periods+2][1];
		StringOutputLabelTop = new String[1][no_of_periods];
		StringInput = new String[4][no_of_periods];
		StringInputLabelLeft = new String[5][1];
		StringInputLabelTop = new String[1][no_of_periods];
		costs_of_period = new int [no_of_periods];
		best_production_periods = new int [no_of_periods][no_of_periods];
		
		//Initialiserung der Properties und Primitive
		//Titel als Textfeld
		TextProperties titleprops = new TextProperties();
		TextProperties titleprops2 = new TextProperties();
		titleprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", 
			    Font.BOLD, 24));
		title = lang.newText(new Coordinates(20,10), translator.translateMessage("Titel"), "title", null, titleprops);
		
		// Create SourceCode: coordinates, name, display options, 
		// default properties
		    
		//description for first page
		// first, set the visual properties for the source code
		    
		// now, create the source code entity
		lang.nextStep();
		SourceCode desc = lang.newSourceCode(new Coordinates(30, 50), "Description",null, descriptionprops);
		
		//Beschreibung als Code
		desc.addCodeLine(translator.translateMessage("desc1"), null, 0, null);
		desc.addCodeLine(translator.translateMessage("desc2"), null, 0, null);
		desc.addCodeLine(translator.translateMessage("desc3"), null, 0, null);
		desc.addCodeLine(translator.translateMessage("desc4"), null, 0, null);
		desc.addCodeLine(translator.translateMessage("desc5"), null, 0, null);
		desc.addCodeLine(translator.translateMessage("desc6"), null, 0, null);
		desc.addCodeLine(translator.translateMessage("desc7"), null, 0, null);
		desc.addCodeLine(translator.translateMessage("desc8"), null, 0, null);
		desc.addCodeLine(translator.translateMessage("desc9"), null, 0, null);
		desc.addCodeLine(translator.translateMessage("desc10"), null, 0, null);
		desc.addCodeLine(translator.translateMessage("desc11"), null, 0, null);
		desc.addCodeLine(translator.translateMessage("desc12"), null, 0, null);
		desc.addCodeLine(translator.translateMessage("desc13"), null, 0, null);
		desc.addCodeLine(translator.translateMessage("desc14"), null, 0, null);
		desc.addCodeLine(translator.translateMessage("desc15"), null, 0, null);

		lang.nextStep(translator.translateMessage("Einleitung"));
		desc.hide();
		SourceCode bigsc = lang.newSourceCode(new Coordinates(30, 50), "Pseudo-Quellcode",null, bigsourceCodeprops);
		
		bigsc.addCodeLine(translator.translateMessage("Codeg1"), null, 0, null);
		bigsc.addCodeLine(translator.translateMessage("Codeg2"), null, 0, null);
		bigsc.addCodeLine(translator.translateMessage("Codeg3"), null, 0, null); //0
		bigsc.addCodeLine(translator.translateMessage("Codeg4"), null, 1, null); //1
		bigsc.addCodeLine(translator.translateMessage("Codeg5"), null, 1, null); //2
		bigsc.addCodeLine(translator.translateMessage("Codeg6"), null, 0, null); //3
		bigsc.addCodeLine(translator.translateMessage("Codeg7"), null, 1, null); //4
		bigsc.addCodeLine(translator.translateMessage("Codeg8"), null, 2, null); //5
		bigsc.addCodeLine(translator.translateMessage("Codeg9"), null, 2, null); //6
		bigsc.addCodeLine(translator.translateMessage("Codeg10"), null, 2, null); //7
		bigsc.addCodeLine(translator.translateMessage("Codeg11"), null, 3, null); //8
		bigsc.addCodeLine(translator.translateMessage("Codeg12"), null, 4, null); //9
		bigsc.addCodeLine(translator.translateMessage("Codeg13"), null, 3, null); //10
		bigsc.addCodeLine(translator.translateMessage("Codeg14"), null, 4, null); //11
		bigsc.addCodeLine(translator.translateMessage("Codeg15"), null, 3, null); //12
		bigsc.addCodeLine(translator.translateMessage("Codeg16"), null, 4, null); //13
		bigsc.addCodeLine(translator.translateMessage("Codeg17"), null, 3, null); //14
		bigsc.addCodeLine(translator.translateMessage("Codeg18"), null, 1, null); //15
		bigsc.addCodeLine(translator.translateMessage("Codeg19"), null, 2, null); //16
		bigsc.addCodeLine(translator.translateMessage("Codeg20"), null, 2, null); //17
		
		bigsc.addCodeLine("", null, 0, null);
		lang.nextStep(translator.translateMessage("Quellcode"));
		
		bigsc.hide();
		// now, create the source code entity
		SourceCode sc = lang.newSourceCode(new Coordinates((275+(no_of_periods*25)), 40), "sourceCode",
						   null, sourceCodeprops);
		
		sc.addCodeLine(translator.translateMessage("Codeg1"), null, 0, null); //0
		sc.addCodeLine(translator.translateMessage("Codeg2"), null, 1, null); //1
		sc.addCodeLine(translator.translateMessage("Codeg3"), null, 1, null); //2
		sc.addCodeLine(translator.translateMessage("Codeg4"), null, 0, null); //3
		sc.addCodeLine(translator.translateMessage("Codeg5"), null, 1, null); //4
		sc.addCodeLine(translator.translateMessage("Codeg6"), null, 2, null); //5
		sc.addCodeLine(translator.translateMessage("Codeg7"), null, 2, null); //6
		sc.addCodeLine(translator.translateMessage("Codeg8"), null, 2, null); //7
		sc.addCodeLine(translator.translateMessage("Codeg9"), null, 3, null); //8
		sc.addCodeLine(translator.translateMessage("Codeg10"), null, 4, null); //9
		sc.addCodeLine(translator.translateMessage("Codeg11"), null, 3, null); //10
		sc.addCodeLine(translator.translateMessage("Codeg12"), null, 4, null); //11
		sc.addCodeLine(translator.translateMessage("Codeg13"), null, 3, null); //12
		sc.addCodeLine(translator.translateMessage("Codeg14"), null, 4, null); //13
		sc.addCodeLine(translator.translateMessage("Codeg15"), null, 3, null); //14
		sc.addCodeLine(translator.translateMessage("Codeg16"), null, 1, null); //15
		sc.addCodeLine(translator.translateMessage("Codeg17"), null, 2, null); //16
		sc.addCodeLine(translator.translateMessage("Codeg18"), null, 2, null); //17
		
		//Matrix mit berechneten Kosten
		//StringMatrix als Ergebnismatrix
		OutputStringMatrix = lang.newStringMatrix(new Coordinates(153,78), Stringoutput, "Output", null, OutputMatrixprops);
		OutputLabelLeft = lang.newStringMatrix(new Coordinates(20,50), StringOutputLabelLeft, "OutputLabelLeft",null, OutputLabelLeftprops);
		OutputLabelTop = lang.newStringMatrix(new Coordinates(153,50), StringOutputLabelTop, "OutputLabelTop",null, OutputLabelTopprops);
		
		//initialize Outputmatrix
		for (int i=0; i<no_of_periods+1; i++){
			for(int j=0; j<no_of_periods; j++){
				OutputStringMatrix.put(i, j, "", null, null);
			}
		}
		
		OutputLabelLeft.put(0, 0, translator.translateMessage("Planungsperiode"), null, null);
		for (int i=0;i<no_of_periods;i++){
			OutputLabelLeft.put(i+1,0,String.valueOf(i+1),null,null);			
			OutputLabelTop.put(0,i,String.valueOf(i+1),null,null);
		}
		OutputLabelLeft.put(no_of_periods+1, 0, translator.translateMessage("Prod.-periode"), null, null);
		
		InputStringMatrix = lang.newStringMatrix(new Coordinates(408+(no_of_periods*25),178+(no_of_periods+2)*20), StringInput, "Input", null, InputMatrixprops);
		InputLabelLeft = lang.newStringMatrix(new Coordinates(275+(no_of_periods*25),150+(no_of_periods+2)*20), StringInputLabelLeft, "InputLabelLeft",null, InputLabelLeftprops);
		InputLabelTop = lang.newStringMatrix(new Coordinates(408+(no_of_periods*25),150+(no_of_periods+2)*20), StringInputLabelTop, "InputLabelTop",null, InputLabelTopprops);
		
		//Initialize InputMatrix
		InputLabelLeft.put(0, 0, translator.translateMessage("Planungsperiode"), null, null);
		InputLabelLeft.put(1, 0, translator.translateMessage("Bedarf"), null, null);
		InputLabelLeft.put(2, 0, translator.translateMessage("Prod.-kosten"), null, null);
		InputLabelLeft.put(3, 0, translator.translateMessage("Ruestkosten"), null, null);
		InputLabelLeft.put(4, 0, translator.translateMessage("Lagerkosten"), null, null);
		
		variables = lang.newVariables();
		for(int i=0; i< no_of_periods;i++){
			InputLabelTop.put(0,i,String.valueOf(i+1),null,null);
		}
		
		for (int i=0; i<input.length;i++){
			for (int j=0; j<inputmatrix[0].length;j++){
				InputStringMatrix.put(i, j, String.valueOf(input[i][j]), null, null);
			}
		}
		
		//Erstellen der Textfelder für Hinweise und Berechnungen
		Berechnungstitel = lang.newText(new Coordinates(20,(int) (178+(no_of_periods+2)*20.5)), translator.translateMessage("Berechnungstitel"), "Berechnungstitel", null, titleprops2);
		Berechnungen = lang.newText(new Coordinates(20,(int) (198+(no_of_periods+2)*20.5)), "", "Berechnungen", null, titleprops2);
		Berechnungen2 = lang.newText(new Coordinates(20,(int) (238+(no_of_periods+2)*20.5)), "", "Berechnungen2", null, titleprops2);
		
		//visuelle Zähler einbauen
		OutputCounter = lang.newCounter(OutputStringMatrix); // Zaehler anlegen
		InputCounter = lang.newCounter(InputStringMatrix); // Zaehler anlegen
		CounterProperties cp = new CounterProperties(); // Zaehler-Properties anlegen
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // gefuellt...
		cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE); // ...mit Blau
		InputCounterTitle = lang.newText( new Coordinates(540+(no_of_periods*25*2), 220+(no_of_periods+2)*20), translator.translateMessage("InputCounter"), "InputCounterTitle", null, titleprops2);
		InputCounterView = lang.newCounterView(InputCounter, new Coordinates(540+(no_of_periods*25*2), 240+(no_of_periods+2)*20), cp, true,false);
		OutputCounterTitle = lang.newText(new Coordinates(540+(no_of_periods*25*2),150+(no_of_periods+2)*20), translator.translateMessage("OutputCounter"), "OutputCounterTitle", null, titleprops2);
		OutputCounterView = lang.newCounterView(OutputCounter, new Coordinates(540+(no_of_periods*25*2),170+(no_of_periods+2)*20), cp, true,false);
		
		lang.nextStep(translator.translateMessage("Algorithmus"));
		//Variablen deklarieren
		variables.declare("int", translator.translateMessage("tvariable"),"1",animal.variables.Variable.getRoleString(VariableRoles.STEPPER));
		variables.declare("int", translator.translateMessage("pvariable"),"0", animal.variables.Variable.getRoleString(VariableRoles.STEPPER));
		variables.declare("int", translator.translateMessage("letztePeriode"),"1", animal.variables.Variable.getRoleString(VariableRoles.FOLLOWER));
		variables.declare("int", translator.translateMessage("minimum"), "0", animal.variables.Variable.getRoleString(VariableRoles.TEMPORARY));
		//ein Periodenproblem betrachten
		sc.highlight(0);
		sc.highlight(1);
		OutputLabelLeft.highlightCell(1, 0, null, null);
		OutputLabelTop.highlightCell(0, 0, null, null);
		lang.nextStep("1. "+translator.translateMessage("Iteration"));
		OutputStringMatrix.highlightCell(0, 0, null, null);
		
		InputStringMatrix.highlightCell(2, 0, null, null);
		Berechnungen.setText(String.valueOf(input[2][0]), null, null);
		lang.nextStep();
		InputStringMatrix.unhighlightCell(2, 0, null, null);
		OutputStringMatrix.highlightCell(0, 0, null, null);
		Berechnungen.setText(Berechnungen.getText()+"="+input[2][0], null, null);
		//lang.nextStep();
		
		output[0][0]=input[2][0];
		OutputStringMatrix.put(0, 0, InputStringMatrix.getElement(2, 0), null, null);
		lang.nextStep();
		Berechnungen.setText("", null, null);
		costs_of_period[0] = output[0][0];
		//lang.nextStep();
		sc.unhighlight(1);
		sc.highlight(2);
		OutputLabelLeft.unhighlightCell(1, 0, null, null);
		OutputStringMatrix.unhighlightCell(0, 0, null, null);
		OutputStringMatrix.highlightCell(OutputStringMatrix.getNrRows()-1, 0, null, null);
		OutputLabelLeft.highlightCell(OutputLabelLeft.getNrRows()-1, 0, null, null);
		OutputStringMatrix.put(OutputStringMatrix.getNrRows()-1, 0, String.valueOf(1), null, null);
		lang.nextStep();
		OutputStringMatrix.unhighlightCell(OutputStringMatrix.getNrRows()-1, 0, null, null);
		OutputLabelLeft.unhighlightCell(OutputLabelLeft.getNrRows()-1, 0, null, null);
		sc.unhighlight(0);
		sc.unhighlight(2);
		
		lang.nextStep();
		sc.highlight(3);
		sc.highlight(4);
		lang.nextStep();
		//Füllen der Outputtabelle mit den Kosten (verkürzter Horizont)
		for(int i=1;i<no_of_periods;i++){
			variables.set(translator.translateMessage("tvariable"), String.valueOf(i+1));
			OutputLabelTop.unhighlightCell(0, i-1, null, null);
			OutputLabelTop.highlightCell(0, i, null, null);
			sc.highlight(4);
			sc.highlight(5);
			lang.nextStep(String.valueOf(i+1)+". "+translator.translateMessage("Iteration"));
			sc.unhighlight(5);
			sc.highlight(6);

			for(int j=0;j<=i;j++){
				variables.set(translator.translateMessage("pvariable"), String.valueOf(j+1));
				OutputLabelLeft.highlightCell(j+1, 0, null, null);
				lang.nextStep();
				sc.unhighlight(14);
				//lang.nextStep();
				sc.unhighlight(6);
				sc.highlight(7);
				//lang.nextStep();

				if(j>=lastproductionperiod){
					if (i==j){
						sc.highlight(10);
						sc.highlight(11);
						OutputStringMatrix.highlightCell(j, i, null, null);
						output[j][i]=Integer.valueOf(OutputStringMatrix.getElement(lastproductionperiod, i-1))+Integer.valueOf(InputStringMatrix.getElement(2, i));
						lang.nextStep();
						OutputStringMatrix.highlightCell(lastproductionperiod, i-1, null, null);
						Berechnungen.setText(String.valueOf(costs_of_period[i-1]), null, null);
						lang.nextStep();
						OutputStringMatrix.unhighlightCell(lastproductionperiod,i-1, null, null);
						InputStringMatrix.highlightCell(2, i, null, null);
						Berechnungen.setText(Berechnungen.getText()+"+"+input[2][i], null, null);
						lang.nextStep();						
						InputStringMatrix.unhighlightCell(2, i, null, null);
						Berechnungen.setText(Berechnungen.getText()+"="+output[i][j], null, null);						
						OutputStringMatrix.put(j, i, String.valueOf(output[i][j]), null, null);
						lang.nextStep();
						OutputStringMatrix.unhighlightCell(j, i, null, null);
						sc.unhighlight(10);
						sc.unhighlight(11);
					}
					else{
						sc.highlight(12);
						sc.highlight(13);
						OutputStringMatrix.highlightCell(j, i, null, null);
						lang.nextStep();
						int sum_h = sum_stock_costs(j, i);
						int value = Integer.valueOf(OutputStringMatrix.getElement(j, i-1))+sum_h*Integer.valueOf(InputStringMatrix.getElement(0, i));
						Berechnungen.setText(String.valueOf(output[j][i-1]), null, null);
						OutputStringMatrix.highlightCell(j, i-1, null, null);
						lang.nextStep();
						OutputStringMatrix.unhighlightCell(j, i-1, null, null);
						//lang.nextStep();
						InputStringMatrix.highlightElemColumnRange(3, j, i-1, null, null);
						Berechnungen.setText(Berechnungen.getText()+"+"+sum_h, null, null);
						lang.nextStep();
						InputStringMatrix.unhighlightElemColumnRange(3, j, i-1, null, null);
						InputStringMatrix.highlightCell(0, i, null, null);
						Berechnungen.setText(Berechnungen.getText()+"*"+input[0][i], null, null);
						lang.nextStep();
						InputStringMatrix.unhighlightCell(0, i, null, null);
						output[j][i]=value;
						OutputStringMatrix.highlightCell(j, i, null, null);
						OutputStringMatrix.put(j, i, String.valueOf(output[j][i]), null, null);
						Berechnungen.setText(Berechnungen.getText()+"="+output[j][i], null, null);
						lang.nextStep();
						OutputStringMatrix.unhighlightCell(j, i, null, null);
						sc.unhighlight(12);
						sc.unhighlight(13);
						
					}
				}
				else{
					//Text, dass verkürzter Horizont und somit irrelevant
					sc.highlight(8);
					sc.highlight(9);
					OutputStringMatrix.highlightCell(j, i, null, null);
					Berechnungen.setText(translator.translateMessage("verkuerzter-Horizont"), null, null);
					lang.nextStep();
					OutputStringMatrix.unhighlightCell(j, i, null, null);
					Berechnungen.setText("", null, null);
					lang.nextStep();
					sc.unhighlight(8);
					sc.unhighlight(9);
					
				}
				OutputLabelLeft.unhighlightCell(j+1, 0, null, null);
				Berechnungen.setText("", null, null);
				//lang.nextStep();
				sc.highlight(14);



			}
			variables.set(translator.translateMessage("pvariable"), "0");

			sc.unhighlight(14);
			sc.unhighlight(7);
			sc.unhighlight(9);
			sc.unhighlight(4);
			OutputLabelLeft.highlightCell(no_of_periods+1, 0, null, null);
			sc.highlight(15);
			//Minimum bestimmen
			sc.highlight(16);
			String Minimum = "Min(";
			for(int k=0;k<=i;k++){
				if(output[k][i]>0){//Integer.valueOf(OutputStringMatrix.getElement(k, i))>0){
					Minimum= Minimum + String.valueOf(output[k][i]);
					if(k<no_of_periods-1 && output[k+1][i]>0){//Integer.valueOf(OutputStringMatrix.getElement(k+1, i))>0){
						Minimum = Minimum + ",";
					}
				}
			}
			Minimum = Minimum + ") = ";
			int min_index= get_minimum_index(i);
			Minimum= Minimum+output[min_index][i];
			costs_of_period[i]=output[min_index][i];
			lastproductionperiod=min_index;
			variables.set(translator.translateMessage("letztePeriode"), String.valueOf(min_index+1));
			variables.set(translator.translateMessage("minimum"), String.valueOf(min_index+1));
			OutputLabelLeft.highlightCell(min_index+1, 0, null, null);
			Berechnungen.setText(Minimum, null, null);
			Berechnungen2.setText("=> "+translator.translateMessage("prod.-in-Periode")+String.valueOf(min_index+1), null, null);
			lang.nextStep();
			variables.set("i", "0");

			OutputLabelLeft.unhighlightCell(min_index+1, 0, null, null);
			sc.unhighlight(16);
			sc.highlight(17);
			
			OutputStringMatrix.highlightCell(no_of_periods,i, null, null);
			
			OutputStringMatrix.put(no_of_periods, i, String.valueOf(min_index+1), null, null);		
			lang.nextStep();
			OutputLabelLeft.unhighlightCell(no_of_periods+1, 0, null, null);
			OutputStringMatrix.unhighlightCell(no_of_periods,i, null, null);
			OutputLabelTop.unhighlightCell( 0,no_of_periods-1, null, null);
			sc.unhighlight(15);
			sc.unhighlight(17);
			Berechnungen.setText("", null, null);
			Berechnungen2.setText("", null, null);
			//best_production_periods
			
			
		}
		
		lang.nextStep();
		//Berechnung der Ergebnisse
		/*
		OutputLabelLeft.hide();
		OutputLabelTop.hide();
		OutputStringMatrix.hide();
		InputLabelLeft.hide();
		InputLabelTop.hide();
		InputStringMatrix.hide();
		//lang.nextStep();
		 * 
		InputStringMatrix = lang.newStringMatrix(new Coordinates(408+(no_of_periods*25),178+(no_of_periods+2)*20), StringInput, "Input", null, InputMatrixprops);
		InputLabelLeft = lang.newStringMatrix(new Coordinates(275+(no_of_periods*25),150+(no_of_periods+2)*20), StringInputLabelLeft, "InputLabelLeft",null, InputLabelLeftprops);
		InputLabelTop = lang.newStringMatrix(new Coordinates(408+(no_of_periods*25),150+(no_of_periods+2)*20), StringInputLabelTop, "InputLabelTop",null, InputLabelTopprops);
		SourceCode sc = lang.newSourceCode(new Coordinates((275+(no_of_periods*25)), 40), "sourceCode",
						   null, sourceCodeprops);
		*/

		sc.hide();
		Berechnungen.hide();
		Berechnungen2.hide();
		Berechnungstitel.hide();
		InputCounterTitle.hide();
		InputCounterView.hide();
		OutputCounterTitle.hide();
		OutputCounterView.hide();
		//Verschiebung der vorhandenen Elemente an Position für Ergebnisseite
		Coordinates c =(Coordinates)InputLabelLeft.getUpperLeft();
		InputLabelLeft.moveBy(null, 0, 200-c.getY(), null, null);
		InputLabelTop.moveBy(null, 0, 200-c.getY(), null, null);
		InputStringMatrix.moveBy(null, 0, 200-c.getY(), null, null);
		Coordinates c2 = (Coordinates)OutputLabelLeft.getUpperLeft();
		OutputLabelLeft.moveBy(null, 0, 200-c2.getY(), null, null);
		OutputLabelTop.moveBy(null, 0, 200-c2.getY(), null, null);
		OutputStringMatrix.moveBy(null, 0, 200-c2.getY(), null, null);
		//Anzahl der Produktionen berechnen
		int no_of_productions =1;
		for(int i=1; i<no_of_periods;i++){
			if(Integer.valueOf(OutputStringMatrix.getElement(no_of_periods, i-1))!=Integer.valueOf(OutputStringMatrix.getElement(no_of_periods, i))){
				no_of_productions++;
			}
		}
		
		//Ergebnisseite erstellen
		//Initialisierung der Ergebnismatrix
		String[][] ErgebnisMatrix= new String[4][no_of_productions+1];
		ErgebnisMatrix[0][0] = translator.translateMessage("Ergebnis1");
		ErgebnisMatrix[1][0] = translator.translateMessage("Ergebnis2");
		ErgebnisMatrix[2][0] = translator.translateMessage("Ergebnis3");
		ErgebnisMatrix[3][0] = translator.translateMessage("Ergebnis4");
		
		for(int i=1;i<ErgebnisMatrix[0].length;i++){
			ErgebnisMatrix[0][i]="";
			ErgebnisMatrix[1][i]="";
			ErgebnisMatrix[2][i]="";
			ErgebnisMatrix[3][i]="";
		}
		
		TextProperties titleprops3 = new TextProperties();
		titleprops3.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 20));
		ResultMatrix = lang.newStringMatrix(new Coordinates(20,60), ErgebnisMatrix, "Ergebnis", null, ResultMatrixprops);
		Text Ergebnistitel = lang.newText(new Coordinates(20,40), translator.translateMessage("Ergebnistitel"), "Ergebnistitel", null, titleprops3);
		lang.nextStep(translator.translateMessage("Ergebnis"));

		//Berechnung der Ergebnisse
		Ergebnis = new int[no_of_periods][5];
		int start=1;
		int end=1;
		boolean written=false;
		int currentcostsum = 0;
		int bedarfpos=0;
		
		for (int i=0; i<no_of_periods;i++){
			written = false;
			if( i!=0 && Integer.valueOf(OutputStringMatrix.getElement(no_of_periods, i-1))!=Integer.valueOf(OutputStringMatrix.getElement(no_of_periods, i))){
				currentcostsum = costs_of_period[end-1];

				int sum=0;
				for(int j=start-1; j<end;j++){
					sum+=input[0][j];
					//Simulation eines Zugriffs für Zähler
					InputStringMatrix.getElement(0, 0);
				}
				
				Ergebnis[bedarfpos][0]=start;
				Ergebnis[bedarfpos][1]=end;
				Ergebnis[bedarfpos][2]=sum;
				if(bedarfpos>0){
					Ergebnis[bedarfpos][3]=currentcostsum;//-Ergebnis[bedarfpos-1][3];
				}
				else{
					Ergebnis[bedarfpos][3]=currentcostsum;
				}
				Ergebnis[bedarfpos][4]=Integer.valueOf(OutputStringMatrix.getElement(no_of_periods, i-1));
				start=i+1;
				end=i+1;
				written=true;
				bedarfpos++;
			}
			else {
				
				end=i+1;
			}

		}
		
		if(written){
			int sum=0;
			for(int j=start-1; j<end;j++){
				sum+=input[0][j];
				//Simulation eines Zugriffs für Zähler
				InputStringMatrix.getElement(0, 0);
			}
			currentcostsum = costs_of_period[end-1];
			Ergebnis[bedarfpos][0]=start;
			Ergebnis[bedarfpos][1]=end;
			Ergebnis[bedarfpos][2]=sum;
			Ergebnis[bedarfpos][3]=currentcostsum;
			Ergebnis[bedarfpos][4]=Integer.valueOf(OutputStringMatrix.getElement(no_of_periods, end-1));
		}
		lang.nextStep();
		
		//Schreiben der Ergebnisse in die Ergebnismatrix
		for (int i=0;i<no_of_productions;i++) {
			
			if(Ergebnis[i][0]==Ergebnis[i][1]){
				ErgebnisMatrix[0][i+1] = String.valueOf(Ergebnis[i][0]);
				ResultMatrix.put(0, i+1, String.valueOf(Ergebnis[i][0]), null, null);
				
			}
			else{
				ErgebnisMatrix[0][i+1] = String.valueOf(Ergebnis[i][0])+" - "+String.valueOf(Ergebnis[i][1]);
				ResultMatrix.put(0, i+1, String.valueOf(Ergebnis[i][0])+" - "+String.valueOf(Ergebnis[i][1]), null, null);
			}
			OutputStringMatrix.highlightCellColumnRange(no_of_periods, Ergebnis[i][0]-1, Ergebnis[i][1]-1, null, null);
			lang.nextStep();
			OutputStringMatrix.unhighlightCellColumnRange(no_of_periods, Ergebnis[i][0]-1, Ergebnis[i][1]-1, null, null);
			ErgebnisMatrix[1][i+1] = String.valueOf(Ergebnis[i][4]);
			ResultMatrix.put(1, i+1, String.valueOf(Ergebnis[i][4]), null, null);
			OutputStringMatrix.highlightCell(no_of_periods, Ergebnis[i][1]-1, null, null);
			lang.nextStep();
			OutputStringMatrix.unhighlightCell(no_of_periods, Ergebnis[i][1]-1, null, null);
			ErgebnisMatrix[2][i+1] = String.valueOf(Ergebnis[i][2]);
			ResultMatrix.put(2, i+1, String.valueOf(Ergebnis[i][2]), null, null);
			InputStringMatrix.highlightCellColumnRange(0, Ergebnis[i][0]-1, Ergebnis[i][1]-1, null, null);
			lang.nextStep();
			InputStringMatrix.unhighlightCellColumnRange(0, Ergebnis[i][0]-1, Ergebnis[i][1]-1, null, null);
			ErgebnisMatrix[3][i+1] = String.valueOf(Ergebnis[i][3]);
			ResultMatrix.put(3, i+1, String.valueOf(Ergebnis[i][3]), null, null);
			OutputStringMatrix.highlightCell(Ergebnis[i][4]-1, Ergebnis[i][1]-1, null, null);
			lang.nextStep();
			OutputStringMatrix.unhighlightCell(Ergebnis[i][4]-1, Ergebnis[i][1]-1, null, null);

		}

		lang.nextStep(translator.translateMessage("Endergebnis"));
	}
	
	public int get_minimum_index(int period){
		
		int index =lastproductionperiod;
		for(int i=lastproductionperiod;i<=period;i++){
			if(Integer.valueOf(OutputStringMatrix.getElement(i, period))<Integer.valueOf(OutputStringMatrix.getElement(index, period))){
				index = i;
			}
		}
		return index;
	}
	
	public int sum_stock_costs(int from, int to){
		
		int result=0;
		for(int i=from; i<to;i++){
			result+=input[3][i];//
			//Simulation eines Zugriffs für Zähler
			InputStringMatrix.getElement(0, 0);
		}
		return result;
	}
}
