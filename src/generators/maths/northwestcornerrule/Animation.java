package generators.maths.northwestcornerrule;

import generators.maths.northwestcornerrule.views.CodeView;
import generators.maths.northwestcornerrule.views.GridView;
import generators.maths.northwestcornerrule.views.VariableView;
import interactionsupport.models.FillInBlanksQuestionModel;

import java.awt.Color;

import algoanim.primitives.generators.Language;
import algoanim.util.Coordinates;


public class Animation {
	
	private Language myAnimationScript;
	private DrawingUtils myDrawingUtils;
	
	private CodeView myCodeViewHandler;
	private VariableView myVariableViewHandler;
	private GridView myGridViewHandler;
	
	private static String TXT_ANIMATION_TITLE = "Nordwest-Ecken-Regel";
	private static String TXT_INTRO = "Das Nord-West-Ecken-Verfahren ist ein Verfahren aus dem Operations Research,<br>" +
			"das eine zulässige Anfangslösung für das Transportproblem liefern soll.<br> " +
			"Von dieser Lösung aus startet der Optimierungsalgorithmus des Transportproblems.<br>" +
			"Gegeben sind eine Menge an Anbietern (Supplier) mir der jeweiligen Liefermenge. <br>" +
			"Ebenso sind eine Menge an Nachfragern (Demander) gegeben mit einer gewissen Nachfrage. <br>" +
			"Ziel ist es die Angebotsmenge auf die Nachfrage zu verteilen. <br>" +
			"Vorsicht: Die gefundene Aufteilung ist nicht notwendigerweise eine optimale Lösung. <br>" +
			"Sie stellt eine mögliche Lösung (Basislösung) dar, auf der andere Verfahren aufbauen können,<br>" +
			"um eine optimale Lösung des Transportproblems zu kalkulieren.<br>" +
			"Voraussetzung: Außerdem gilt als Grundvoraussetzung, dass die kumulierte Nachfrage dem <br>" +
			"kumulierten Angebot gleichen muss.<br><br>" +
			"Folgende Anbieter bzw. Angebotsmengen wurden ausgewählt: <br>" +
			"Folgende Nachfrager bzw. Nachfragemengen wurden ausgewählt: <br>"; 
	private static String TXT_LASTFRAME = "Durch das Nord-West-Ecken-Verfahren wurde eine Basislösung erzeugt. <br>" +
			"Aufbauend auf dieser Lösung kann z.B. der Simplex-Algorithmus angewendet werden.";
	
	
	public Animation(Language animationScript) {
		// Store the language object
		myAnimationScript = animationScript;
		// This initializes the step mode. Each pair of subsequent steps has to be divided by a call of lang.nextStep();
		myAnimationScript.setStepMode(true);
		myAnimationScript.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		
		// Initialize drawing utilities.
		myDrawingUtils = new DrawingUtils(myAnimationScript);
		
		// Initialize all view handlers for the animation
		myCodeViewHandler = new CodeView(myAnimationScript, myDrawingUtils);
		myVariableViewHandler = new VariableView(myAnimationScript, myDrawingUtils);
		myGridViewHandler = new GridView(myAnimationScript, myDrawingUtils);	
	}
	
	public void buildIntroFrame(int[] supply, int[] demand){
		
		myAnimationScript.nextStep("Einleitung");
		// Build Title and introduction text
		myDrawingUtils.drawHeader(new Coordinates(5,20), TXT_ANIMATION_TITLE);
		myDrawingUtils.buildText(new Coordinates(100,100), TXT_INTRO);
		
		// Draw array with parameters of algorithm
		myAnimationScript.newIntArray(new Coordinates(600, 405),supply, "", null, AnimProps.ARRAY_PROPS);
		myAnimationScript.newIntArray(new Coordinates(600, 430),demand, "", null, AnimProps.ARRAY_PROPS);

	}
	
	public void buildDefaultViews(int[] supply, int[] demand){
		myAnimationScript.nextStep("Initialisierung");
		
		myAnimationScript.hideAllPrimitives();
		myDrawingUtils.drawHeader(new Coordinates(5,20), TXT_ANIMATION_TITLE);
		
		myCodeViewHandler.setupView();
		myVariableViewHandler.setupView();
		myGridViewHandler.setupView(supply, demand);
		
		
	}
	
	public void buildLastFrame(Integer[][] base){
		
		
		myGridViewHandler.getBasisGrid().unhighlightAll(0);
		myGridViewHandler.getRightGrid().unhighlightAll(0);
		myGridViewHandler.getBottomGrid().unhighlightAll(0);
		myCodeViewHandler.unhighlightAll();
		
		myAnimationScript.nextStep("Ergebniss");
		
		myAnimationScript.hideAllPrimitives();
		myDrawingUtils.drawHeader(new Coordinates(5,20), TXT_ANIMATION_TITLE);
		myDrawingUtils.buildText(new Coordinates(100,100), TXT_LASTFRAME);
		
		myGridViewHandler.createLastFrameGrid(base);
		
	}
	
	public void buildExceptionFrame(String message){
		
		myDrawingUtils.drawTextWithBox(new Coordinates(200, 200), message);
		
	}
	
	// public int nordwestEckenRegel (int[] angebot, int[] nachfrage){
	public void buildLine0Animation(){
		myCodeViewHandler.highlight(0, 1);
		myAnimationScript.nextStep();
	}
	
	// int i = 0;
	public void buildLine2Animation(){
		myCodeViewHandler.highlight(2);
		myAnimationScript.nextStep();
	}
	
	// int j = 0;
	public void buildLine3Animation(){
		myCodeViewHandler.highlight(3);
		myAnimationScript.nextStep();
	}
	int counter = 1;
	// while (i<= angebot.length && j <=nachfrage.length) {
	public void buildLine4Animation(int supply_i, int demand_j){
		myAnimationScript.nextStep(counter + ". Iteration");
		counter ++;
		myGridViewHandler.getBasisGrid().unhighlightAll(0);
		myGridViewHandler.getRightGrid().unhighlightAll(0);
		myGridViewHandler.getBottomGrid().unhighlightAll(0);
		
		myGridViewHandler.getBasisGrid().highlightRow(supply_i, Color.YELLOW, 500);
		myGridViewHandler.getBasisGrid().highlightColumn(demand_j, Color.YELLOW, 1000);
		myGridViewHandler.getBasisGrid().highlightCell(demand_j,supply_i, Color.red, 1500);
		
		myGridViewHandler.getRightGrid().highlightRow(supply_i, Color.YELLOW, 500);
		myGridViewHandler.getRightGrid().highlightLastLabeledCellInRow(supply_i, Color.red, 1500);
		
		myGridViewHandler.getBottomGrid().highlightColumn(demand_j, Color.YELLOW, 1000);
		myGridViewHandler.getBottomGrid().highlightLastLabeledCellInColumn(demand_j, Color.red, 1500);
		
		myCodeViewHandler.highlight(4, 5);
		myAnimationScript.nextStep();
		
	}
	
	// x = Math.min(angebot[i],nachfrage[i]);
	public void buildLine6Animation(int i, int j,int supply, int demand, int x){
		myCodeViewHandler.highlight(6);
		
		FillInBlanksQuestionModel fibQuestion = new FillInBlanksQuestionModel("");
		fibQuestion.addAnswer("" + Math.min(supply, demand),1, "Wie groß ist die zu liefernde Menge von Anbieter "+ i + "an Nachfrager " +j + " ?");
		myAnimationScript.addFIBQuestion(fibQuestion);
		
		
		myGridViewHandler.getRightGrid().blinkLastLabeledCellInRow(i, Color.red, 0);
		myGridViewHandler.getBottomGrid().blinkLastLabeledCellInColumn(j, Color.red, 0);
		myVariableViewHandler.alter_variable_x(supply, demand, Math.min(supply, demand));
		
		if(supply <= demand){
			myGridViewHandler.getRightGrid().highlightLastLabeledCellInRow(i, Color.green, 3000);
			myGridViewHandler.getBottomGrid().highlightLastLabeledCellInColumn(j, Color.red, 3000);
		}
		else{
			myGridViewHandler.getBottomGrid().highlightLastLabeledCellInColumn(j, Color.green, 3000);
			myGridViewHandler.getRightGrid().highlightLastLabeledCellInRow(i, Color.red, 3000);
		}
		
		
		myAnimationScript.nextStep();
	}
	
	// saveToBasis(i, j, x);
	public void buildLine7Animation(int i, int j, int x){
		myCodeViewHandler.highlight(7);
		myGridViewHandler.getBasisGrid().setLabel(j, i,"" + x );
		myGridViewHandler.getBasisGrid().highlightCell(j, i, Color.green, 0);
		myAnimationScript.nextStep();
	}

	// angebot[i] -= x;
	public void buildLine8Animation(int supply_i, int x, int i, int j){
		myGridViewHandler.getRightGrid().setNextLabelInRow(i, "" + supply_i);
		
		myCodeViewHandler.highlight(8);
		myVariableViewHandler.alter_variable_angebot_i(supply_i, x, i);
		myAnimationScript.nextStep();
	}

	// nachfrage[j] -= x;
	public void buildLine9Animation(int demand_j, int x, int j){
		myGridViewHandler.getBottomGrid().setNextLabelInColumn(j, "" + demand_j);
		
		myCodeViewHandler.highlight(9);
		myVariableViewHandler.alter_variable_nachfrage_j(demand_j, x, j);
		myAnimationScript.nextStep();
	}
	
	// if(angebot[i] == 0)
	public void buildLine10Animation(){
		myCodeViewHandler.highlight(10);
		myAnimationScript.nextStep();
	}

	// i++;
	public void buildLine11Animation(int i){
		myCodeViewHandler.highlight(11);
		myVariableViewHandler.alter_variable_i(i);
		
	}
	
	// else
	public void buildLine12Animation(){
		myCodeViewHandler.highlight(12);
		myAnimationScript.nextStep();
	}
	
	// j++:
	public void buildLine13Animation(int j){
		myCodeViewHandler.highlight(13);
		myVariableViewHandler.alter_variable_j(j);
		
	}
	
	public void printScript(){
		System.out.println(myAnimationScript);
	}
	
	public Language getMyAnimationScript() {
		return myAnimationScript;
	}

}
