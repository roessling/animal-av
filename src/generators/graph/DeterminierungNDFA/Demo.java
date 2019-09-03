/**
 * Demo.java
 * Nora Wester, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

package generators.graph.DeterminierungNDFA;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;



import java.util.LinkedList;
import java.util.List;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.InfoBox;
import algoanim.primitives.ArrayBasedQueue;
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
import algoanim.properties.QueueProperties;
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
	
	//1 -> scOne // 2 -> scTwo
	private int actualSC;
	
	private SourceCodeProperties scp;

	private boolean valAndHighTogether = false;
	
	private int width;
	
	private int height;
	
	private int heightNewAdja;
	
	private int widthNewAdja;
	
	private int x;
	
	private int y;
	
	private int[] oldFinalStatesPosition;
	
	private Rect actualRect;
	
	private StringMatrix adja;
	
	private MatrixProperties mP;
	
	private ArrayBasedQueue<String> newStates;
	
	private ArrayBasedQueue<String> tempStates;
	
	private QueueProperties qP;
	
	private RectProperties rP;
	
	private TextProperties tP;
	
	private TextProperties tPT;
	
	private int size;
	
	private Text header;
	
	private Rect hRect;

	private Text startT;

	private Text finalT;

	private StringArray statesArray;

	private ArrayProperties aP;

	private int[] widthsOld;

	private String[][] legende;

	private GraphProperties gP;
	
	//-------
	
	
	//---constructors-----
	
	public Demo(Language l){
		lang = l;
		lang.setStepMode(true);
	}
	
	public Demo() {
		// TODO Auto-generated constructor stub
		lang = new AnimalScript("Myhill-Konstruktion", "Nora Wester", 800, 1000);
		lang.setStepMode(true);
	}
	
	//--------
	
	//--init---
	public void init(String[][] translations, String[] states, int[] finalStatesPosition, String[] alphabet, int startStatePosition){
	    
	    lang.nextStep("Einleitung");
	    
	    initDescription();
	    
	    lang.nextStep("Anfang des Algorithmuses");
	   
	    lang.hideAllPrimitives();
	    header.show();
	    hRect.show();
	    
	    initGraph(translations, states, finalStatesPosition, alphabet, startStatePosition);
	    
	    descriptionTwo();
	    
	    initSourceCode();
	    scOne.highlight(0);
	    scOne.highlight(1);
	    
	}
	private void descriptionTwo() {
		// TODO Auto-generated method stub
		InfoBox info = new InfoBox(lang, new Coordinates(500, 70), 6, "Erklaerung");
		List<String> text = new LinkedList<String>();
		text.add("Beginnend mit dem Startzustand werden fuer jeden Buchstaben des Alphabetes alle Folgezustaende ermittelt");
		text.add("Die Menge pro Buchstabe ist der neue Folgezustand vom Startzustand in Bezug auf diesen Buchstaben");
		text.add("Ist diese Menge zum ersten mal als neuer Folgezustand berechnet worden, kommt er in die Liste der neuen Zustaende");
		text.add("Diese Berechnung wird fuer jedes Element aus der Liste der neuen Zustaende durchgefuehrt");
		text.add("Dabei werden feur den jeweiligen Buchstaben die Folgezustaende von jedem Zustand aus der Menge des neuen Zustandes");
		text.add("ermittelt und zusammengefuegt ");
		info.setText(text);
		lang.nextStep();
		info.hide();
	}

	//---------
	//---Description---
	private void initDescription(){
		
		if(tP == null){
			tP = new TextProperties();
			tP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
					Font.SANS_SERIF, Font.PLAIN, 16));
		}	
	    lang.newText(new Coordinates(10, 100), 
	    	"Diese Animation behandelt die Umwandlung eines nichtdeterministischen endlichen Automatens (NDFA)", 
	    	"description", null, tP);
	    lang.newText(new Offset(0, 25, "description", AnimalScript.DIRECTION_NW),
	        "in einen deterministischen endlichen Automaten (DFA) mittels der Myhill-Konstruktion oder auch Potenzmengenkonstruktion.",
	        "description1", null, tP);
	    lang.newText(new Offset(0, 25, "description1",
	        AnimalScript.DIRECTION_NW),
	        "Dabei gibt jeder Zustand vom aequivalenten DFA die Zustaende an, "
	        + "in denen sich der NDFA zu einem bestimmten Zeitpunkt befinden koennte.",
	        "description2", null, tP);
	    lang.newText(new Offset(0, 25, "description2", AnimalScript.DIRECTION_NW), 
	    	"Die Zustaende des konstruierten Automaten sind somit Elemente der Potenzmenge der Zustaende des Ausgangsautomaten.", 
	    	"description3", null, tP);
	}
	
	private void finalText(){
		lang.newText(new Coordinates(10, 100), 
				"Bei diesem Algorithmus koennen bis zu (2^n)-1 neue Zustaende fuer den DFA entstehen. Dabei ist n die Anzahl der Zustaende des NDFA.", "descriptionF1", null, tP);
		lang.newText(new Offset(0, 25, "descriptionF1", AnimalScript.DIRECTION_NW), 
				"Es gilt -1, weil der Startzustand immer dazugehoert und deshalb keine leere Menge entstehen kann.", "descriptionF2", null, tP);
		lang.newText(new Offset(0, 25, "descriptionF2", AnimalScript.DIRECTION_NW), 
				"Fuer jeden einzelnen Zustand eines neuen Zustandes muessen fuer jeden Buchstaben im Alphabet die Folgezustaende gefunden werden", "descriptionF3", null, tP);
		lang.newText(new Offset(0, 25, "descriptionF3", AnimalScript.DIRECTION_NW),
				"Somit sind das im worst case n*(2^(n-1))*AnzahlBuchstaben Abfragen nach Folgezustaenden.", "descriptionF4", null, tP);
		lang.newText(new Offset(0, 40, "descriptionF4", AnimalScript.DIRECTION_NW), 
				"Jede von einem NDFA akzeptierte Sprache ist auch durch einen "
				+ "aequivalenten DFA akzeptierbar.", "descriptionF5", null, tP);
	}
	//--------
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

		scOne.addCodeLine("public void Determine(){", null, 0, null);
		scOne.addCodeLine("PriorityQueue<String> nextNewStates = new PriorityQueue<String>();", null, 1, null);
		scOne.addCodeLine("nextNewStates.add(getStartState());", null, 1, null);
		scOne.addCodeLine("while(!nextNewStates.isEmpty()){", null, 1, null);
		scOne.addCodeLine("String state = nextNewStates.poll();", null, 2, null);
		scOne.addCodeLine("newStates.add(state);", null, 2, null);
		scOne.addCodeLine("for(int j=0; j<alphabet.length; j++){", null, 2, null);
		scOne.addCodeLine("String newState = calculateNewState(state, alphabet[i])", null, 3, null);
		scOne.addCodeLine("setNewTranslation(state, alphabet[i], newState);", null, 3, null);
		scOne.addCodeLine("if(isNewState(newState))", null, 3, null);
		scOne.addCodeLine("nextNewStates.add(newState);", null, 4, null);
		scOne.addCodeLine("}", null, 2, null);
		scOne.addCodeLine("}", null, 1, null);
		scOne.addCodeLine("}", null, 0, null);
		
		scTwo = lang.newSourceCode(new Coordinates(500, 70), "sourceCodeTwo", null, scp); 
		scTwo.addCodeLine("public String calculateNewState(String state, String letter){", null, 0, null);
		scTwo.addCodeLine("ArrayList<String> singleStatesOld = getSingleStates(state);", null, 1, null);
		scTwo.addCodeLine("ArrayList<String> singleStatesNew = new ArrayList<String>();", null, 1, null);
		scTwo.addCodeLine("for(int i=0; i<singleStatesOld.size(); i++){", null, 1, null);
		scTwo.addCodeLine("int position = getPositionOfState(singleStateOld.get(i));", null, 2, null);
		scTwo.addCodeLine("// suche nach den Zustaenden, die man von dem einen Zustand mit dem Ereignis 'letter' erreicht", null, 2, null);
		scTwo.addCodeLine("singleStatesNew.add(searchTranslations(position, letter))", null, 3, null);
		scTwo.addCodeLine("}", null, 1, null);
		scTwo.addCodeLine("return singleStatesNew.toString();", null, 1, null);
		scTwo.addCodeLine("}", null, 0, null);
		
		scTwo.hide();
		actualSC = 1;
	}		
	
	//------
	//-Graph---
	private void initGraph(String[][] translation, String[] states, int[] finalStatesPosition, String[] alphabet, int startStateposition){
		
		oldFinalStatesPosition = finalStatesPosition;
		
		int s = 0;
		
		for(int i=0; i<alphabet.length; i++){
			s = s+alphabet[i].length();
		}
		
		s = s*8+16+(alphabet.length-1)*3;
		
		//falls ein Zustandsname l�nger ist, als die maximale alphabet-L�nge
		int[] widths = getStatesWidth(states);
		for(int i = 0; i<widths.length; i++){
			if(widths[i]>s){
				s = widths[i];
			}
		}
		
		if(mP == null){
			mP = new MatrixProperties();
			mP.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.yellow);
			mP.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.red);
			mP.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
		}
		
		mP.set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, 20);
		mP.set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, s);
				
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
		
//		String[][] temp = new String[translation.length][translation.length];
//		
//		for(int i=0; i<temp.length; i++)
//			for(int j=0; j<temp[i].length; j++){
//				temp[i][j] = translation[i-1][j];
//				if(temp[i][j].compareTo("") == 0)
//					temp[i][j] = "{}";
//			}
		
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
		
		int[] actualWidth = getActualWidth(widths, startStateposition);
		
		lang.newRect(new Coordinates(40+actualWidth[0]*5/4, 70), new Coordinates(40+actualWidth[1]*5/4, 70+20*5/4), "", null, startST);
		
		//finalStates markieren
		for(int i=0; i<finalStatesPosition.length; i++){
			actualWidth = getActualWidth(widths, finalStatesPosition[i]);
			lang.newRect(new Coordinates(40+2+actualWidth[0]*5/4,72), new Coordinates(40-2+actualWidth[1]*5/4, 70+20*5/4-2), "", null, finalST);
		}
		
		widthsOld = widths; 
		tPT = tP;
		tPT.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		        Font.SANS_SERIF, Font.PLAIN, 12));
		
		startT = lang.newText(new Offset(50, 0, "zustand", AnimalScript.DIRECTION_NE), 
				"Startzustand gruen markiert", "sState", null, tPT);
		
		finalT = lang.newText(new Offset(0, 15, "sState", AnimalScript.DIRECTION_NW), 
				"Zielzustaende rot markiert", "fStates", null, tPT);
		lang.newText(new Offset(0, 15, "fStates", AnimalScript.DIRECTION_NW), 
				"farbig unterlegte Elementen", "gelb", null, tPT);
		lang.newText(new Offset(10, 15, "gelb", AnimalScript.DIRECTION_NW), 
				"werden (dann) untersucht", "gelbTT", null, tPT);
		
		qP = new QueueProperties();
		qP.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.white);
		
		lang.newText(new Offset(50, 0, "adja", AnimalScript.DIRECTION_SE), 
				"nextNewStates", "temp", null, tPT);
		
		newStates = lang.newArrayBasedQueue(new Offset(0, -20, "temp", AnimalScript.DIRECTION_NW), 
				new ArrayList<String>(), "newStates", null, qP, alphabet.length+1);
		
		if(rP == null){
			rP = new RectProperties();
			rP.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		}
		size = alphabet.length+1;
		width = calculateMaxWidth(states);
		height = 20;
		
		x = 40;
		y = 200 + translation.length*20 + height*2;
		
		actualRect = lang.newRect(new Coordinates(x, y-height), 
				new Coordinates(x+width, y), "", null, rP); 
		
		x = x+width;
		
		widthNewAdja = x-width;
		heightNewAdja = y-height;
		for(int i=1; i<size; i++){
			
			actualRect = lang.newRect(new Coordinates(x, y-height), new Coordinates(x+width, y), "", null, rP);
			String word = alphabet[i-1];
			lang.newText(new Coordinates(x+getDeltaX(word), y-height+2), word, "", null, tP);
			x = x+width;
		}
		
		lang.newText(new Coordinates(x+width,y), 
				"singleStatesNew", "tST", null, tPT);
		
		tempStates = lang.newArrayBasedQueue(new Offset(0, -20, "tST", AnimalScript.DIRECTION_NW), 
				new ArrayList<String>(), "tempStates", null, qP, states.length+1);
		
	}
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
	
	private int getDeltaX(String word) {
		// TODO Auto-generated method stub
		//width = (komma*3+(length-komma)*8)+16+2*deltaX
		String t = word;
		int komma = 0;
		while(t.indexOf(",") != -1){
			t = t.substring(t.indexOf(",")+1);
			komma++;
		}
		int length = komma*3 + (word.length()-komma)*8 ;
		return width/2-length/2;
	}

	
	private int calculateMaxWidth(String[] states) {
		// TODO Auto-generated method stub
		int size = 0;
		for(int i=0; i<states.length; i++){
			size = size+states[i].length();
		}
		return size*8+16+(states.length-1)*3;
	}

	//---------
	//--start-----
	private void setNewRow(String state){
		
		actualRect = lang.newRect(new Coordinates(x-(size)*width, y), new Coordinates(x-((size-1)*width), y+height), "", null, rP);
		x = x-((size-1)* width);
		y = y + height;
		lang.newText(new Coordinates(x+getDeltaX(state)-width, y-height+2), state, "", null, tP);
		
	}
	
	public void setQueueValue(String in, boolean out, int[] newH, int[] oldH){
		
		if(out)
			lang.nextStep("neuer Zustand "+newStates.front());
		else
			lang.nextStep();
		
		if(out){
			String s = newStates.dequeue();
			setNewRow(s);
		}
		
		if(in != null){
			newStates.enqueue(in);
		}
		
		valAndHighTogether = true;
		setHighlight(newH, oldH);
	}
	
	public void goThrowAdja(String letter, int oldCol) {
		// TODO Auto-generated method stub
		lang.nextStep();
		scTwo.unhighlight(4);
		scTwo.unhighlight(3);
		scTwo.highlight(5);
		scTwo.highlight(6);
		int duration = 1000;
		for(int i=0; i<adja.getNrCols(); i++){
			adja.highlightCell(oldCol, i, new MsTiming(i*duration), new MsTiming(duration));
			adja.unhighlightCell(oldCol, i, new MsTiming((i+1)*duration), new MsTiming(0));
			if(adja.getElement(oldCol, i).contains(letter)){
				adja.highlightElem(oldCol, i, new MsTiming(i*duration), new MsTiming(duration));
				adja.unhighlightElem(oldCol, i, new MsTiming((i+1)*duration), new MsTiming(0));
				String nS = statesArray.getData(i);
				if(!tempStates.getQueue().contains(nS))
					tempStates.enqueue(nS);
			}
		}
		
	}
	
	public void setAdjaHigh(int colN, boolean highlight, int[] newH, int[] oldH){
		
		lang.nextStep();
		
		if(highlight)
			statesArray.highlightCell(colN, null, null);
		else
			statesArray.unhighlightCell(colN, null, null);
		
		valAndHighTogether = true;
		setHighlight(newH, oldH);
		
	}
	
	public void setValue(String value, int[] newH, int[] oldH) {
		// TODO Auto-generated method stub
		lang.nextStep();
		
		while(!tempStates.isEmpty())
			tempStates.dequeue();
		
		x = ((Coordinates) actualRect.getLowerRight()).getX();
		y = ((Coordinates) actualRect.getLowerRight()).getY();
		actualRect = lang.newRect(new Coordinates(x, y-height), new Coordinates(x+width, y), "", null, rP);
		lang.newText(new Coordinates(x+getDeltaX(value), y-height+2), value, "", null, tP);
		
		x = x+width;
		
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
			if(newH[i]<20){
				if(actualSC == 2){
					scTwo.hide();
					scOne.show();
					actualSC = 1;
				}
				scOne.highlight(newH[i]);
			}
			else{
				if(actualSC == 1){
					scOne.hide();
					scTwo.show();
					actualSC = 2;
				}
				scTwo.highlight(newH[i]-20);
			}	
		}
		
		for(int i=0; i<oldH.length; i++){
			if(oldH[i]<20){
				if(actualSC == 2){
					scTwo.hide();
					scOne.show();
					actualSC = 1;
				}
				scOne.unhighlight(oldH[i]);
			}
			else{
				scTwo.unhighlight(oldH[i]-20);
			}
		}
		
	}
	
	public Language getLang(){
		return lang;
	}
	//---------
	
	public void finalSlides(String[][] temp, String[] states,
			int[] finalStatesPosition, int startStatePosition, String[] alphabet) {
		// TODO Auto-generated method stub
		lang.nextStep("Adjazenzmatrix des aequivalenten DFA");
		lang.hideAllPrimitives();
		statesArray.show();
		adja.show();
		header.show();
		hRect.show();
		startT.show();
		finalT.show();
		
		//create new Adja
		int s = 0; 
		
		int[] widths = getStatesWidth(states);
		for(int i = 0; i<widths.length; i++){
			if(widths[i]>s){
				s = widths[i];
			}
		}
		
		mP = new MatrixProperties();
		mP.set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, 20);
		mP.set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, s);
		mP.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
//		String[][] adjaTemp = new String[temp.length+1][temp.length];
//		for(int i=0; i<temp.length; i++)
//			adjaTemp[0][i] = states[i];
//		
//		for(int i=1; i<adjaTemp.length; i++)
//			for(int j=0; j<adjaTemp[i].length; j++){
//				adjaTemp[i][j] = temp[i-1][j];
//				if(adjaTemp[i][j].compareTo("") == 0)
//					adjaTemp[i][j] = "{}";
//			}
		
		String[] temps = new String[states.length];
		for(int i=0; i<temps.length; i++){
			String l = "  "+states[i]+"  ";
			temps[i] = l;
		}

		StringArray newS = lang.newStringArray(new Coordinates(widthNewAdja, heightNewAdja), temps, "zustandneu", null, aP);
		
		StringMatrix newDFA = lang.newStringMatrix(new Offset(0, 30, "zustandneu", AnimalScript.DIRECTION_SW), temp, "newadja", null, mP);
		
		lang.newText(new Offset(0, 5, "newadja", AnimalScript.DIRECTION_SW),  "aequivalenter DFA", "", null, tPT);
		
		RectProperties finalST = new RectProperties();
		finalST.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
		
		RectProperties startST = new RectProperties();
		startST.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.green);
		
		//old Adja spezial-states-MArkierungen
		
		int[] actualWidth = getActualWidth(widthsOld, startStatePosition);
		lang.newRect(new Coordinates(40+actualWidth[0]*5/4, 70), new Coordinates(40+actualWidth[1]*5/4, 70+20*5/4), "", null, startST);
		
		//finalStates markieren
		for(int i=0; i<finalStatesPosition.length; i++){
			actualWidth = getActualWidth(widthsOld, oldFinalStatesPosition[i]);
			lang.newRect(new Coordinates(40+2+actualWidth[0]*5/4,72), new Coordinates(40-2+actualWidth[1]*5/4, 70+20*5/4-2), "", null, finalST);
		}
		//----
		
		actualWidth = getActualWidth(widths, startStatePosition);
		lang.newRect(new Coordinates((widthNewAdja+actualWidth[0]*5/4), heightNewAdja), new Coordinates(widthNewAdja+actualWidth[1]*5/4, (heightNewAdja+20*5/4)), "", null, startST);
		
		//finalStates markieren
		for(int i=0; i<finalStatesPosition.length; i++){
			actualWidth = getActualWidth(widths, finalStatesPosition[i]);
			lang.newRect(new Coordinates(widthNewAdja+2+actualWidth[0]*5/4, heightNewAdja+2), new Coordinates(widthNewAdja-2+actualWidth[1]*5/4, heightNewAdja+20*5/4-2), "", null, finalST);
		}
			
		lang.newText(new Coordinates(500, 70), "Somit ergibt sich der nebenstehende aequivalente DFA", "", null, tP);
		
		lang.nextStep("Graphen");

		lang.hideAllPrimitives();
		adja.hide();
		newDFA.hide();
		newS.hide();
		
		header.show();
		hRect.show();
		
		setGraphs(temp, states, alphabet);
		
	
	}

	
	private void setGraphs(String[][] newAdja, String[] newS, String[] alphabet) {
		// TODO Auto-generated method stub
		setLegende(alphabet);
		int[][] intOld = getIntAdja();
		int[][] intNew = getIntAdja(newAdja);
		
		String[] st = new String[statesArray.getLength()];
		for(int i=0; i<statesArray.getLength(); i++)
			st[i] = statesArray.getData(i);
		
		
		if(gP == null){
			gP = new GraphProperties();
			gP.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
			gP.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
			gP.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
		}
		
		Graph graphOld = lang.newGraph("NDFA", intOld, getGraphNodes(intOld.length, 90, 100), st, null, gP);
		lang.newText(new Offset(0, 15, "NDFA", AnimalScript.DIRECTION_S), "NDFA", "", null, tPT);
			
		Graph graphNew = lang.newGraph("DFA", intNew, getGraphNodes(intNew.length, 90+(intOld.length/2+1)*150, 100), newS, null, gP);
		lang.newText(new Offset(0, 15, "DFA", AnimalScript.DIRECTION_S), "aequivalenter DFA", "", null, tPT);
		
		mP.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		
		StringMatrix leg = lang.newStringMatrix(new Offset(0, 40, "NDFA", AnimalScript.DIRECTION_SE), legende, "Legende", null, mP);
		lang.newText(new Offset(0, 15, "Legende", AnimalScript.DIRECTION_SW), "Legende", "", null, tPT);
		
		lang.nextStep("Schlusswort");
		lang.hideAllPrimitives();
		graphOld.hide();
		graphNew.hide();
		leg.hide();
		header.show();
		hRect.show();
		finalText();
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
	
	private int[][] getIntAdja(){
		int[][] intAdja = new int[adja.getNrRows()][adja.getNrCols()];
		for(int i=0; i<intAdja.length; i++)
			for(int j=0; j<intAdja[i].length; j++){
				String word = adja.getElement(i, j);
				int position = findLetterPosition(word);
				if(position != -1)
					intAdja[i][j] = Integer.parseInt(legende[1][position]);
				else{
					String alt = dosome(word);
					if(alt.compareTo("") != 0)
						intAdja[i][j] = Integer.parseInt(alt); 
				}
			}
		return intAdja;
	}

	private String dosome(String word){
		boolean run = true;
		boolean last = false;
		StringBuffer sb = new StringBuffer();
		while(run){
			int index = word.indexOf(",");
			if(index  == -1)
				index = word.indexOf("+");
		
			if(index != -1 || last){
				int p = 0;
				if(!last)
					p = findLetterPosition(word.substring(0, index));
				else
					p = findLetterPosition(word);
						
				if(p != -1)
					sb.append(legende[1][p]);
				if(!last){
					word = word.substring(index+1);
					if(word.length() == 1)
						last = true;
				}
				else
					run = false;
			}
			else{
				run = false;
			}	
		}
		return sb.toString();
	}
	
	public void setMProperties(MatrixProperties mPu){
		mP = mPu;
	}

	public void setTProperties(TextProperties textProps) {
		// TODO Auto-generated method stub
		tP = textProps;
	}

	public void setRProperties(RectProperties newStatesProps) {
		// TODO Auto-generated method stub
		rP = newStatesProps;
	}

	public void setSCProperties(SourceCodeProperties sourceCodeProps) {
		// TODO Auto-generated method stub
		scp = sourceCodeProps;
	}
	
	public void setAProperties(ArrayProperties statesArrayProps){
		aP = statesArrayProps;
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
