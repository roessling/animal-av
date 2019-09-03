/**
 * Demo.java
 * Nora Wester, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

package generators.graph.MinimierungDFA;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.InfoBox;
import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;

public class Demo {

	protected Language lang;
	
	private SourceCode scOne;
	
	private SourceCode scTwo;
	
	private SourceCodeProperties scp;
	
	private Text header;
	
	private Text wrongClass;
	
	private Rect hRect;
	
	//-----------
	
	private StringMatrix aequviClass;
	
	private StringMatrix adja; 
	
	private MatrixProperties mP;
	
	private MatrixProperties aCP;
	
	
	//---------
	boolean valAndHighTogether = false;
	//--------

	private TextProperties tPT;

	

	private int oldS;

	private InfoBox first;

	private InfoBox second;

	private InfoBox third;

	private int height;

	private TextProperties tP;

	private ArrayProperties aP;

	private StringArray statesArray;

	private String[][] legende;

	private boolean wrongClassBoolean;

	private Text wrongClassT;

	private GraphProperties gP;
	
	//--Constructors-------
	
	public Demo(Language l){
		lang = l;
		lang.setStepMode(true);
	}
	
	public Demo() {
		// TODO Auto-generated constructor stub
		lang = new AnimalScript("Minimierung eines DFA", "Nora Wester", 800, 1000);
		lang.setStepMode(true);
	}

	//--init----
	public void init(String[][] translations, String[] states, int[] finalStatesPosition, int startStatePosition, String[] alphabet){
	    
	    lang.nextStep("Einleitung");
	    
	    initDescription();
	    
	    lang.nextStep("Anfang des Algorithmuses");
	    
	    lang.hideAllPrimitives();
	    header.show();
	    hRect.show();
	    
	    initGraphics(translations, states, finalStatesPosition, startStatePosition, alphabet);
	    firstText();
	    
	    lang.nextStep("Ueberpruefung FinalState-kein FinalState");
	    
	    first.hide();
	    initSourceCode();
	    scOne.highlight(0);
	    scOne.highlight(1);
	    
	}
	private void firstText() {
		// TODO Auto-generated method stub
		first = new InfoBox(lang, new Coordinates(500, 70), 4, "Erster Schritt");
		List<String> temp = new LinkedList<String>();
		temp.add("Zunaechst wird nach Zustandspaaren mit ");
		temp.add("Finalstate und nicht Finalstate gesucht.");
		temp.add("Diese Paare nennt man unterscheidbar und sie werden in der");
		temp.add("Aequivalenzklassen-Matrix mit einem x markiert");
		first.setText(temp);
//		TextProperties tP = new TextProperties();
//	    tP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
//	        Font.SANS_SERIF, Font.PLAIN, 16));
//	    first = lang.newText(new Coordinates(500, 70),
//	        "Als erstes wird nach Zustandspaaren gesucht, bei denen",
//		//diese nennt man dann unterscheidbar",
//	        "description21", null, tP);
//	    lang.newText(new Offset(0, 25, "description21",
//	        AnimalScript.DIRECTION_NW),
//	        "einer ein Finalstate ist und der andere nicht",
//	        "description22", null, tP);
//	    lang.newText(new Offset(0, 25, "description22", AnimalScript.DIRECTION_NW), 
//	    	"Diese nennt man dann unterscheidbar und werden in der", "description23", null, tP);
//	    lang.newText(new Offset(0, 25, "description23", AnimalScript.DIRECTION_NW), 
//	    	"Aequivalenzklassen-Matrix mit einem x markiert", "description24", null, tP);
	}

	//-------
	//---Description---
	private void initDescription(){
			
		if(tP == null){
			tP = new TextProperties();
			tP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
						Font.SANS_SERIF, Font.PLAIN, 16));
		}
		lang.newText(new Coordinates(10, 100),
	        "Diese Animation behandelt die Minimierung eines deterministischen endlichen Automatens (DFA).",
	        "description1", null, tP);
		lang.newText(new Offset(0, 25, "description1",
	        AnimalScript.DIRECTION_NW),
	        "Dabei verwendet man den Satz von Myhill-Nerode und verfeinert die Aequivalenzklassen des DFA schrittweise, ",		        "description2", null, tP);
		lang.newText(new Offset(0, 25, "description2", AnimalScript.DIRECTION_NW),
		   	"bis keine weitere Verfeinerung mehr moeglich ist.", "description3", null, tP);
		lang.newText(new Offset(0, 25, "description3", AnimalScript.DIRECTION_NW), 
	    	"Die uebrig gebliebenen nicht unterscheidbaren Zustandspaare, werden dann vereint.", "description4", null, tP);
	}
	//------------	
	//---SourceCode---
	private void initSourceCode(){
		
		if(scp == null){
			scp = new SourceCodeProperties();
			scp.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
			scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));		    
			scp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);   
			scp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		}
		// now, create the source code entity
		scOne = lang.newSourceCode(new Coordinates(500, 70), "sourceCodeOne", null, scp);
		
		// Add the lines to the SourceCode object.
		// Line, name, indentation, display dealy
	 
		scOne.addCodeLine("public void Minimierung(){", null, 0, null);
		scOne.addCodeLine("boolean[][] aequivClassArray = new boolean[numberOfStates][numberOfStates];", null, 1, null);
		scOne.addCodeLine("for(int i=0; i<numberOfStates-1; i++){", null, 1, null);
		scOne.addCodeLine("for(int j=i+1; j<numberOfStates; j++){", null, 2, null);
		scOne.addCodeLine("if(isFinalState(states[i])^isFinalState(states[j])){", null, 3, null);
		scOne.addCodeLine("aequivClassArray[i][j] = true;", null, 4, null);
		scOne.addCodeLine("aequivClassArray[j][i] = true;", null, 4, null);
		scOne.addCodeLine("}", null, 3, null);
		scOne.addCodeLine("}", null, 2, null);
		scOne.addCodeLine("}", null, 1, null);
		
		scTwo = lang.newSourceCode(new Coordinates(500, 70), "sourceCodeTwo", null, scp);
		scTwo.addCodeLine("public void Minimierung(){...", null, 0, null);
		scTwo.addCodeLine("while(run){", null, 1, null);
		scTwo.addCodeLine("run = false;", null, 2, null);
		scTwo.addCodeLine("for(int i=0; i<numberOfStates-1; i++)", null, 2, null);
		scTwo.addCodeLine("for(int j=i+1; j<numberOfStates; j++)", null, 3, null);
		scTwo.addCodeLine("if(!aequivClassArray[i][j]){", null, 4, null);
		scTwo.addCodeLine("int alphaN = 0;", null, 5, null);
		scTwo.addCodeLine("while(alphaN > -1 && alphaN < alphabet.length){", null, 5, null);
		scTwo.addCodeLine("int statePositionOne = getTargetStateToLetter(i, alphabet[alphaN]);", null, 6, null);
		scTwo.addCodeLine("int statePositionTwo = getTargetStateToLetter(j, alphabet[alphaN]);", null, 6, null);
		scTwo.addCodeLine("if(aequivClassArray[statePositionOne][statePositionTwo]){", null, 6, null);
		scTwo.addCodeLine("aequivClassArray[i][j] = true;", null, 7, null);
		scTwo.addCodeLine("aequivClassArray[j][i] = true;", null, 7, null);
		scTwo.addCodeLine("run = true;", null, 7, null);
		scTwo.addCodeLine("alphaN = -1;", null, 7, null);
		scTwo.addCodeLine("}", null, 6, null);
		scTwo.addCodeLine("else", null, 6, null);
		scTwo.addCodeLine("alphaN++;", null, 7, null);
		scTwo.addCodeLine("}", null, 6, null);
		scTwo.addCodeLine("}", null, 5, null);
		scTwo.addCodeLine("}", null, 4, null);
		scTwo.addCodeLine("}", null, 3, null);
		scTwo.addCodeLine("combineNonMarkedStates();", null, 1, null);
		scTwo.addCodeLine("}", null, 0, null);
		
		scTwo.hide();
	}
	//--------
	//---Graphics---
	private void initGraphics(String[][] translation, String[] states, int[] finalStatesPosition, int startStatePosition, String[] alphabet){
		
		oldS = 0;
		
		for(int i=0; i<alphabet.length; i++){
			oldS = oldS+alphabet[i].length();
		}
		
		oldS = oldS*8+16;
		
		//falls ein Zustandsname l�nger ist, als die maximale alphabet-L�nge
		int[] widths = getStatesWidth(states);
		for(int i = 0; i<widths.length; i++){
			if(widths[i]>oldS){
				oldS = widths[i];
			}
		}
		
//		String[][] temp = new String[translation.length+1][translation.length];
//		for(int i=0; i<translation.length; i++)
//			temp[0][i] = states[i];
//		
//		for(int i=1; i<temp.length; i++)
//			for(int j=0; j<temp[i].length; j++){
//				temp[i][j] = translation[i-1][j];
//				if(temp[i][j].compareTo("") == 0)
//					temp[i][j] = "{}";
//			}
		
		if(mP == null){
			mP = new MatrixProperties();
			mP.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.yellow);
			mP.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.red);
			mP.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
		}
		
		mP.set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, 20);
		mP.set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, oldS);
		
		String[] temps = new String[states.length];
		for(int i=0; i<temps.length; i++){
			String l = "  "+states[i]+"  ";
			temps[i] = l;
		}
		
		if(aP == null){
			aP= new ArrayProperties();
			aP.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
			aP.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.yellow);
		}
		
		aP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		        Font.SANS_SERIF, Font.PLAIN, 16));
		
		statesArray = lang.newStringArray(new Coordinates(40, 70), temps, "zustand", null, aP);
		
		
		adja = lang.newStringMatrix(new Offset(0, 30, "zustand", AnimalScript.DIRECTION_SW), translation, "adja", null, mP);
		
		//leer Felder durch {} ersetzen
		for(int i=0; i<adja.getNrRows(); i++)
			for(int j=0; j<adja.getNrCols(); j++)
				if(adja.getElement(i, j).compareTo("") == 0)
					adja.put(i, j, "{}", null, null);
		
		RectProperties finalST = new RectProperties();
		finalST.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
		
		RectProperties startST = new RectProperties();
		startST.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.green);
		
		int[] actualWidth = getActualWidth(widths, startStatePosition);
		
		lang.newRect(new Coordinates(40+actualWidth[0]*5/4, 70), new Coordinates(40+actualWidth[1]*5/4, 70+20*5/4), "", null, startST);
		
		//finalStates markieren
		for(int i=0; i<finalStatesPosition.length; i++){
			actualWidth = getActualWidth(widths, finalStatesPosition[i]);
			lang.newRect(new Coordinates(40+2+actualWidth[0]*5/4,72), new Coordinates(40-2+actualWidth[1]*5/4, 70+20*5/4-2), "", null, finalST);
		}
		
		height = 200 + translation.length*20 + height*2;
		
		tPT = tP;
		tPT.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		        Font.SANS_SERIF, Font.PLAIN, 12));
		
		lang.newText(new Offset(50, 0, "adja", AnimalScript.DIRECTION_NE), 
				"Startzustand gruen markiert", "sState", null, tPT);
		
		lang.newText(new Offset(0, 15, "sState", AnimalScript.DIRECTION_NW), 
				"Zielzustaende rot markiert", "fStates", null, tPT);
		lang.newText(new Offset(0, 15, "fStates", AnimalScript.DIRECTION_NW), 
				"farbig unterlegte Elementen", "gelb", null, tPT);
		lang.newText(new Offset(10, 15, "gelb", AnimalScript.DIRECTION_NW), 
				"werden (dann) untersucht", "gelbTT", null, tPT);
		
		// aequivalenzklassenmatrix
		
		int s = 0;
		
		for(int i = 0; i<widths.length; i++){
			if(widths[i]>s){
				s = widths[i];
			}
		}
		
		if(aCP == null){
			aCP = new MatrixProperties();
			aCP.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		}
		
		aCP.set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, s);
		aCP.set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, 20);
		
		String[][] aClass = new String[states.length+1][states.length+1];
		for(int i=1; i<states.length+1; i++){
			for(int j=1; j<states.length+1; j++){
				aClass[i][0] = states[i-1];
				aClass[0][i] = states[i-1];
				aClass[i][j] = " ";
			}
		}
		
		aClass[0][0] = "~0";
		aequviClass = lang.newStringMatrix(new Offset(0, 50, "adja", AnimalScript.DIRECTION_SW), aClass, "aequviClass", null, aCP);
	
		wrongClass = lang.newText(new Offset(20, 0, "aequviClass", AnimalScript.DIRECTION_E), "gehoert erst zur naechsten", "wrongClass", null, tPT);
		wrongClassT = lang.newText(new Offset(10, 15, "wrongClass", AnimalScript.DIRECTION_NW), "Aequivalenzklasse", "", null, tPT);
		
		wrongClass.hide();
		wrongClassT.hide();
		wrongClassBoolean = false;
	}
	//--------
	//--start----
	//0 -> upper left
	//1 -> lower right
	private int[] getActualWidth(int[] widths, int position) {
		// TODO Auto-generated method stub
		int[] total = new int[2];
		for(int i=0; i<position; i++){
			total[0] = total[0]+widths[i];
		}
		total[1] = total[0]+widths[position];
		return total;
	}

	private int[] getStatesWidth(String[] states){
		int[] w = new int[states.length];
		for(int i=0; i<states.length; i++){
			String t = states[i];
			int komma = 0;
			while(t.indexOf(",") != -1){
				t = t.substring(t.indexOf(",")+1);
				komma++;
			}
			w[i] = komma*3 + (states[i].length()-komma)*8 + 4*4;
		}
			
		return w;
	}
	
	
	public void changeClass(String string) {
		// TODO Auto-generated method stub
		aequviClass.put(0, 0, string, null, null);
	}
	
	public void unhighlightAdja(int one, int two){
		statesArray.unhighlightCell(one, null, null);
		statesArray.unhighlightCell(two, null, null);
		if(wrongClassBoolean){
			wrongClassBoolean = false;
			wrongClass.hide();
			wrongClassT.hide();
		}
	}
	
	public void highAequivClass(int position, String side, boolean highlight, boolean b){
		if(b){
			if(side.compareTo("i")==0)
				if(highlight)
					aequviClass.highlightCell(position+1, 0, null, null);
				else
					aequviClass.unhighlightCell(position+1, 0, null, null);
			else
				if(highlight)
					aequviClass.highlightCell(0, position+1, null, null);
				else
					aequviClass.unhighlightCell(0, position+1, null, null);
		}
	}
	
	public void goThrow(String letter, int positionOne, int positionTwo){
		lang.nextStep();
		int duration = 1000;
		int i = 0;
		boolean foundOne = false;
		boolean foundTwo = false;
		while(i<adja.getNrCols()||!(foundOne && foundTwo)){
			if(!foundOne){
				adja.highlightCell(positionOne, i, new MsTiming(i*duration), new MsTiming(duration));
				adja.unhighlightCell(positionOne, i, new MsTiming((i+1)*duration), new MsTiming(0));
			}
			if(!foundTwo){
				adja.highlightCell(positionTwo, i, new MsTiming(i*duration), new MsTiming(duration));
				adja.unhighlightCell(positionTwo, i, new MsTiming((i+1)*duration), new MsTiming(0));
			}
			
			if(adja.getElement(positionOne, i).contains(letter) && !foundOne){
				adja.highlightElem(positionOne, i, new MsTiming(i*duration), new MsTiming(duration));
				adja.unhighlightElem(positionOne, i, new MsTiming((i+1)*duration), new MsTiming(0));
				statesArray.highlightCell(i, new MsTiming((i+1)*duration), new MsTiming(0));
				foundOne = true;
			}
			
			if(adja.getElement(positionTwo, i).contains(letter) && !foundTwo){
				adja.highlightElem(positionTwo, i, new MsTiming(i*duration), new MsTiming(duration));
				adja.unhighlightElem(positionTwo, i, new MsTiming((i+1)*duration), new MsTiming(0));
				statesArray.highlightCell(i, new MsTiming((i+1)*duration), new MsTiming(0));
				foundTwo = true;
			}
			i++;
		}
	}
	
	public void moveMarker(int positionH, int positionU, boolean unhighlight, int[] newH, int[] oldH) {
		// TODO Auto-generated method stub
		
		if(!valAndHighTogether)
			lang.nextStep();
			
		if(positionH == -1){
			valAndHighTogether = true;
		}
		else{
			statesArray.highlightCell(positionH, null, null);
			valAndHighTogether = false;
		}
		if(unhighlight)
			statesArray.unhighlightCell(positionU, null, null);
		
		if(newH == null && oldH == null)
			return;
		
		valAndHighTogether = true;
		setHighlight(newH, oldH);
		
	}
	
	
	public void setValue(int i, int j, int[] newH, int[] oldH) {
		// TODO Auto-generated method stub
		lang.nextStep();
		aequviClass.put(i+1, j+1, "x", null, null);
		aequviClass.put(j+1, i+1, "x", null, null);
		valAndHighTogether = true;
		setHighlight(newH, oldH);
	}
	
	public void setLabel(boolean b, int clas, int[] newH, int[] oldH) {
		// TODO Auto-generated method stub
		lang.nextStep("Klasse "+ clas);
		valAndHighTogether = true;
		setHighlight(newH, oldH);
		
	}
	
	public void setHighlight(int[] newH, int[] oldH) {
		// TODO Auto-generated method stub
		if(!valAndHighTogether)
			lang.nextStep();
		else
			valAndHighTogether = false;
		
		for(int i=0; i<newH.length; i++){
			if(newH[i]<7)
				scOne.highlight(newH[i]);
			else
				scTwo.highlight(newH[i]-7);
		}
		
		for(int i=0; i<oldH.length; i++){
			if(oldH[i]<7)
				scOne.unhighlight(oldH[i]);
			else
				scTwo.unhighlight(oldH[i]-7);
		}
	}
	
	public void secondPhase(){
		lang.nextStep("Ueberpruefung der Translationen");
		valAndHighTogether = false;
		scOne.hide();
		secondText();
		lang.nextStep();
		second.hide();
		scTwo.show();
		scTwo.highlight(0);
				
	}

	private void secondText() {
		// TODO Auto-generated method stub
		//nun muss man vergleichen, ob es Zustandspaare gibt, die durch das gleiche Ereignis (Buchstabe)
		//in ein unterscheidbares Zustandspaar  kommen.
		//(q1,a,q4) und (q2,a,q3) da {q3,q4} schon unterscheidbar, ist auch {q1,q2} unterscheidbar
		//Dies solange machen, bis sich nichts mehr �ndert
		second = new InfoBox(lang, new Coordinates(500, 70), 6, "Zweiter Schritt");
		List<String> temp = new LinkedList<String>();
		temp.add("Als naechsten Schritt vergleicht man, ob es Zustandspaare gibt, die durch das gleiche");
		temp.add("Ereignis (Buchstabe) in ein schon als unterscheidbar markiertes Zustandspaar ueberfuehrt werden");
		temp.add("Dabei muessen die markierten Zustandspaare zu einer vollstaendigen Aequivalenzklasse gehoeren.");
		temp.add("Beispiel: Es gilt (q1,a,q4) und (q2,a,q3) und das Paar {q3,q4} ist als unterscheidbar markiert");
		temp.add("so ist auch das Zustandspaar {q1,q2} als unterscheidbar zu markieren");
		temp.add("Diesen Schritt wiederholt man solange bis keine neuen Zustandspaare mehr gefunden werden");
		second.setText(temp);
		
//		 TextProperties tP = new TextProperties();
//		    tP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
//		        Font.SANS_SERIF, Font.PLAIN, 16));
//		    lang.newText(new Coordinates(500, 70),
//		        "Als naechstes vergleicht man, ob es Zustandspaare gibt, die durch das gleiche",
//		        "description31", null, tP);
//		    lang.newText(new Offset(0, 25, "description31",
//		        AnimalScript.DIRECTION_NW),
//		        "Ereignis (Buchstabe) in ein schon als unterscheidbar markiertes Zustandspaar ueberfuehrt werden",
//		        "description32", null, tP);
//		    lang.newText(new Offset(0, 25, "description32", AnimalScript.DIRECTION_NW), 
//		    	"Beispiel: Es gilt (q1,a,q4) und (q2,a,q3) und das Paar {q3,q4} ist als unterscheidbar makiert", "description33", null, tP);
//		    lang.newText(new Offset(0, 25, "description33", AnimalScript.DIRECTION_NW),
//		    	"so ist auch das Zustandspaar {q1,q2} als unterscheidbar zu markieren", "description34", null,	tP);
//		    lang.newText(new Offset(0, 25, "description34", AnimalScript.DIRECTION_NW),	
//		    	"Dies macht man solange bis sich nichts mehr aendert", "description35", null, tP);
	}

	public void thirdPhase(ArrayList<Integer> delete, ArrayList<Integer> get) {
		// TODO Auto-generated method stub
		lang.nextStep("nicht unterscheidbare Paare");
		scTwo.hide();
		List<String> temp = new LinkedList<String>();
		temp.add("Folgende Zustandspaare sind nicht unterscheidbar (da nicht markiert) und koennen verbunden werden:");
//		TextProperties tP = new TextProperties();
//	    tP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
//	        Font.SANS_SERIF, Font.PLAIN, 16));
//	    lang.newText(new Coordinates(500, 70), "Folgende Zustandspaare sind nicht unterscheidbar und koennen verbunden werden:", "description41", null, tP);
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<delete.size(); i++){
			sb.append("{");
			sb.append(aequviClass.getElement(0, delete.get(i)+1));
			sb.append(", ");
			sb.append(aequviClass.getElement(0, get.get(i)+1));
			sb.append("} ");
		}
		
		temp.add(sb.toString());
		temp.add("Beim Verbinden wird einer der beiden Zustaende geloescht und dessen ankommende Ereignisse");
		temp.add("dem nicht geloeschten Zustand zugeordnet");
		
//		lang.newText(new Offset(0, 25, "description41", AnimalScript.DIRECTION_NW), sb.toString(), "description42", null, tP);
//	    lang.newText(new Offset(0, 25, "description42", AnimalScript.DIRECTION_NW), 
//	    	"Beim Verbinden wird einer der beiden Zustaende geloescht und dessen ankommende Ereignisse", "description43", null, tP);
//		lang.newText(new Offset(0, 25, "description43", AnimalScript.DIRECTION_NW), 
//				"sind dann neue ankommenden Ereignisse des nicht geloeschten Zustandes", "description44", null, tP);
		third = new InfoBox(lang, new Coordinates(500, 70), temp.size(), "Letzer Schritt");
		third.setText(temp);
		
	}

	public void setFinal(String[][] newAdjacencyMatrix, String[] newStates, int[] newFinalStatesPosition, int newStartStatePosition, String[] alphabet) {
		// TODO Auto-generated method stub
		lang.nextStep("Minimaler, aequivalenter DFA");
		aequviClass.hide();
//		String[][] aClass = new String[newStates.length+1][newStates.length];
//		for(int i=1; i<newStates.length+1; i++){
//			for(int j=0; j<newStates.length; j++){
//				aClass[0][j] = newStates[j];
//				aClass[i][j] = newAdjacencyMatrix[i-1][j];
//				if(aClass[i][j].compareTo("") == 0)
//					aClass[i][j] = "{}";
//			}
//		}
		String[] temps = new String[newStates.length];
		for(int i=0; i<temps.length; i++){
			String l = "  "+newStates[i]+"  ";
			temps[i] = l;
		}
		
		StringArray newStatesArray = lang.newStringArray(new Coordinates(40, height), temps, "newStatesArray", null, aP);
		
		StringMatrix newDFA = lang.newStringMatrix(new Offset(0, 30, "newStatesArray", AnimalScript.DIRECTION_SW), newAdjacencyMatrix, "newTranslationen", null, mP);
		
		RectProperties finalST = new RectProperties();
		finalST.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
		
		RectProperties startST = new RectProperties();
		startST.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.green);
		
		int[] widths = getStatesWidth(newStates);
		
		int[] actualWidth = getActualWidth(widths, newStartStatePosition);
		lang.newRect(new Coordinates((40+actualWidth[0]*5/4), height), new Coordinates(40+actualWidth[1]*5/4, (height+20*5/4)), "", null, startST);
		
		//finalStates markieren
		for(int i=0; i<newFinalStatesPosition.length; i++){
			actualWidth = getActualWidth(widths, newFinalStatesPosition[i]);
			lang.newRect(new Coordinates(40+2+actualWidth[0]*5/4, height+2), new Coordinates(40-2+actualWidth[1]*5/4, height+20*5/4-2), "", null, finalST);
		}
		
		lang.nextStep("Graphen");
		lang.hideAllPrimitives();
		adja.hide();
		statesArray.hide();
		newStatesArray.hide();
		newDFA.hide();
		third.hide();
		header.show();
		hRect.show();
		
		setGraphs(newAdjacencyMatrix, newStates, alphabet);
		
	}
	
	private void setGraphs(String[][] newAdjacencyMatrix, String[] newStates,
			String[] alphabet) {
		// TODO Auto-generated method stub
		setLegende(alphabet);
		
		String[][] adjaString = new String[adja.getNrRows()][adja.getNrCols()];
		for(int i=0; i<adjaString.length; i++)
			for(int j=0; j<adjaString[i].length; j++)
				adjaString[i][j] = adja.getElement(i, j);
		
		int[][] intOld = getIntAdja(adjaString);
		int[][] intNew = getIntAdja(newAdjacencyMatrix);
		
		String[] st = new String[statesArray.getLength()];
		for(int i=0; i<statesArray.getLength(); i++)
			st[i] = statesArray.getData(i);
		
		if(gP == null){
			gP = new GraphProperties();
			gP.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
			gP.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
			gP.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
		}
		
		Graph graphOld = lang.newGraph("DFA", intOld, getGraphNodes(intOld.length, 90, 100), st, null, gP);
		lang.newText(new Offset(0, 15, "DFA", AnimalScript.DIRECTION_S), "DFA", "", null, tPT);
			
		Graph graphNew = lang.newGraph("minimalerDFA", intNew, getGraphNodes(intNew.length, 90+(intOld.length/2+1)*150, 100), newStates, null, gP);
		lang.newText(new Offset(0, 15, "minimalerDFA", AnimalScript.DIRECTION_S), "aequivalenter, minimaler DFA", "", null, tPT);
		
		mP.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		
		StringMatrix leg = lang.newStringMatrix(new Offset(0, 40, "DFA", AnimalScript.DIRECTION_SE), legende, "Legende", null, mP);
		lang.newText(new Offset(0, 15, "Legende", AnimalScript.DIRECTION_SW), "Legende", "", null, tPT);
		
		lang.nextStep("Schlusswort");
		lang.hideAllPrimitives();
		graphOld.hide();
		graphNew.hide();
		leg.hide();
		header.show();
		hRect.show();
		
		//noch ein bisschen Text
	    lang.newText(new Coordinates(10, 100),
	        "Der Algorithmus waere schneller, wenn man nicht darauf achtet, das die markierten ",
	        "description3Final1", null, tP);
	    lang.newText(new Offset(0, 25, "description3Final1", AnimalScript.DIRECTION_NW), 
	    	"Zustandspaare in einer vollstaendigen Aequivalenzklasse enthalten ist, ", "description3Final2", null, tP);
	    lang.newText(new Offset(0, 25, "description3Final2", AnimalScript.DIRECTION_NW), 
	    	"da sich das Ergebnis dadurch nicht aendern wuerde", "description3Final3", null, tP);
	}
	
	private int findLetterPosition(String letter){
		for(int i=0; i<legende[0].length; i++)
			if(legende[0][i].compareTo(letter) == 0)
				return i;
		
		return -1;
	}
	
	private int[][] getIntAdja(String[][] ad){
		int[][] intAdja = new int[ad.length][ad.length];
		for(int i=0; i<ad.length; i++)
			for(int j=0; j<ad.length; j++){
				int position = findLetterPosition(ad[i][j]);
				if(position != -1)
					intAdja[i][j] = Integer.parseInt(legende[1][position]);
			}
		return intAdja;
	}
	
	private Node[] getGraphNodes(int length, int startX, int startY) {
		// TODO Auto-generated method stub
		Node[] nodes = new Node[length];
		int x = startX;
		int y = startY;
		for(int i=0; i<length; i++){
			nodes[i] = new Coordinates(x, y);
			if(i%2 == 0){
				y = y+200;
			}
			else{
				y = y-200;
				x = x+150;
			}
				
		}
		return nodes;
	}

	private void setLegende(String[] alphabet){
		legende = new String[2][alphabet.length];
		int number = 1;
		for(int i=0; i<alphabet.length; i++){
			legende[0][i] = alphabet[i];
			legende[1][i] = Integer.toString(number);
			number++;
		}
	}

	public Language getLang(){
		return lang;
	}

	public void setMProperties(MatrixProperties adjaProps) {
		// TODO Auto-generated method stub
		mP = adjaProps;
	}

	public void setACProperties(MatrixProperties aequiClassProps) {
		// TODO Auto-generated method stub
		aCP = aequiClassProps;
	}
	
	public void setTProperties(TextProperties textProps) {
		// TODO Auto-generated method stub
		tP = textProps;
	}

	public void setSCProperties(SourceCodeProperties sourceCodeProps) {
		// TODO Auto-generated method stub
		scp = sourceCodeProps;
	}
	
	public void setAProperties(ArrayProperties statesArrayProps){
		aP = statesArrayProps;
	}

	public void nextRound() {
		// TODO Auto-generated method stub
		lang.nextStep();
		wrongClass.show();
		wrongClassT.show();
		wrongClassBoolean = true;
	}
	
	public void setGProperties(GraphProperties graphProps){
		gP = graphProps;
	}
	
	public void setHeader(RectProperties hRectP, TextProperties hTP){
		//make Header
		TextProperties hP;
		
		if(hTP == null)
			hP = new TextProperties();
		
		hP = hTP;
		hP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
			Font.SANS_SERIF, Font.BOLD, 24));
	    header = lang.newText(new Coordinates(20, 30), "Myhill-Konstruktion",
		   "header", null, hP);
	    
	    RectProperties rP;
	    if(hRectP == null){
			    rP = new RectProperties();
			    rP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			    rP.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	    }
		rP = hRectP;
		rP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
	    
		hRect = lang.newRect(new Offset(-5, -5, "header",
			AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect", null, rP);
	}

}
