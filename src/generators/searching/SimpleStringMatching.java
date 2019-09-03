/*
 * SimpleStringMatching.java
 * Kevin Kocon, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class SimpleStringMatching implements Generator {
    private Language lang;
    private String Wort;
    private RectProperties CodeKasten;
    private TextProperties ErlaeuterungsText;
    private RectProperties ErlaeuterungsKasten;
    private ArrayProperties AktuelleMatchesArray;
    private ArrayProperties WortArray;
    private RectProperties HintergrundKasten;
    private String Text;
    private ArrayProperties ErgebnisArray;
    private ArrayMarkerProperties ArrayMarkerEigenschaften;
    private SourceCodeProperties Code;
    private ArrayProperties TextArray;

	private SourceCode sc;
	private StringArray textArr; 
	private StringArray wordArr;
	private ArrayList<Integer> result;
	
	/**
	 * Diese Methode initialisiert alles was fuer den Generator anschlie�end wichtig ist.
	 */
    public void init(){
        lang = new AnimalScript("Simple String Matching", "Kevin Kocon", 800, 600);
        lang.setStepMode(true);
    }

    /**
     * Diese Methode generiert den Generator
     */
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        Wort = (String)primitives.get("Wort");
        CodeKasten = (RectProperties)props.getPropertiesByName("CodeKasten");
        ErlaeuterungsText = (TextProperties)props.getPropertiesByName("ErlaeuterungsText");
        ErlaeuterungsKasten = (RectProperties)props.getPropertiesByName("ErlaeuterungsKasten");
        AktuelleMatchesArray = (ArrayProperties)props.getPropertiesByName("AktuelleMatchesArray");
        WortArray = (ArrayProperties)props.getPropertiesByName("WortArray");
        HintergrundKasten = (RectProperties)props.getPropertiesByName("HintergrundKasten");
        Text = (String)primitives.get("Text");
        ErgebnisArray = (ArrayProperties)props.getPropertiesByName("ErgebnisArray");
        ArrayMarkerEigenschaften = (ArrayMarkerProperties)props.getPropertiesByName("ArrayMarkerEigenschaften");
        Code = (SourceCodeProperties)props.getPropertiesByName("Code");
        TextArray = (ArrayProperties)props.getPropertiesByName("TextArray");
        ErlaeuterungsText.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 13));
        result = new ArrayList<Integer>();
        
        match(Text,Wort);
        return lang.toString();
    }

    /**
     * Diese Methode bereitet alle Dinge fuer die Animation vor, die vor bzw. nach dem letzten Schritt des Algos. gebraucht werden und ruft diesen auf
     * @param text, String in welchem ein String gesucht werden soll
     * @param word, String der gesucht werden soll
     */
	private void match(String text,String word){
		//Erstelle Titel
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		Text header = lang.newText(new Coordinates(20, 30), "Simple String Matching","header", null, headerProps);
		    
		//Erstelle Umrandung fuer Titel
		RectProperties rectPropsT = new RectProperties();
		rectPropsT.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectPropsT.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectPropsT.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		Rect headerRect = lang.newRect(new Offset(-5, -5, "header",AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",null, rectPropsT);
		
		//Erstelle Einleitungstitel
		TextProperties codeTitleProps = new TextProperties();
		codeTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
		Text einleitungsTitel = lang.newText(new Offset(0,20,"header",AnimalScript.DIRECTION_SW), "Einleitung","EinleitungsTitle", null, codeTitleProps);
		
		//Erstelle Umrandung fuer Einleitungstitel
		RectProperties rectPropsCT = new RectProperties();
		rectPropsCT.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectPropsCT.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectPropsCT.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		Rect EinTitleRect = lang.newRect(new Offset(-5, -5, "EinleitungsTitle",AnimalScript.DIRECTION_NW), new Offset(5, 5, "EinleitungsTitle", "SE"), "ETRect",null, rectPropsCT);
		
		//Erstelle Hintergrund Einleitung/Fazit
		Rect backgroundE = lang.newRect(new Coordinates(0,0),new Coordinates(800,650), "EBRect",null, HintergrundKasten);
		
		//ErlaeuterungsKasten Einleitung/Fazit
		Rect desRectE = lang.newRect(new Offset(0, 0, "ETRect",AnimalScript.DIRECTION_NW), new Offset(-40, -50, "EBRect", "SE"), "DERect",null, ErlaeuterungsKasten);
		
		//Erstelle Erlaeuterung
		Text d1 = lang.newText(new Offset(30,90,"ETRect","NW"), "Beim 'Simple String Matching' handelt es sich um einen Algorithmus, mit dem man, wie der Name schon","d1", null, ErlaeuterungsText);
		Text d2 = lang.newText(new Offset(0,7,"d1","SW"), "sagt, relativ einfach einen (Such-) String in einem (Gesamt-) String finden kann. Im Alltag wird solch ein","d2", null, ErlaeuterungsText);
		Text d3 = lang.newText(new Offset(0,7,"d2","SW"), "Algorithmus beispielsweise verwendet, um ein bestimmtes Wort in einem Text zu finden.","d3", null, ErlaeuterungsText);
		Text d4 = lang.newText(new Offset(0,7,"d3","SW"), "Als Eingabe erhält der Algorithmus also zwei Strings.","d4", null, ErlaeuterungsText);
		Text d5 = lang.newText(new Offset(0,7,"d4","SW"), "In der folgenden Animation wird 'Text' als der gesamte String und 'Wort' als der Suchstring angenommen.","d5", null, ErlaeuterungsText);
		Text d6 = lang.newText(new Offset(0,7,"d5","SW"), "Des Weiteren werden während dem Algorithmus die aktuell möglichen Kandidaten für einen Treffer als","d6", null, ErlaeuterungsText);
		Text d7 = lang.newText(new Offset(0,7,"d6","SW"), "Tupel (i,x) in 'aktuelle Matches' gespeichert. Bei diesem Tupel steht das i für die Position des möglichen","d7", null, ErlaeuterungsText);
		Text d8 = lang.newText(new Offset(0,7,"d7","SW"), "Anfangs des 'Wortes' im 'Text' und x für die Anzahl der nach dem Anfangsbuchstaben weiteren","d8", null, ErlaeuterungsText);
		Text d9 = lang.newText(new Offset(0,7,"d8","SW"), "Übereinstimmungen des Wortes mit dem Text. Außerdem werden vollständig gefundene Anfangspositionen in","d9", null, ErlaeuterungsText);
		Text d10 = lang.newText(new Offset(0,7,"d9","SW"), "'Ergebnisse' gespeichert. Als Ergebnis liefert der Algorithmus alle Stellen, an welchen der gesuchte","d10", null, ErlaeuterungsText);
		Text d11 = lang.newText(new Offset(0,7,"d10","SW"), "String im kompletten String beginnt, was in unserem Fall 'Ergebnisse' ist.","d11", null, ErlaeuterungsText);
		
		lang.nextStep("Einleitung");
		
		//Verwerfe alles aus der Erlaeuterung
		einleitungsTitel.hide();
		EinTitleRect.hide();
		backgroundE.hide();
		desRectE.hide();
		d1.hide();
		d2.hide();
		d3.hide();
		d4.hide();
		d5.hide();
		d6.hide();
		d7.hide();
		d8.hide();
		d9.hide();
		d10.hide();
		d11.hide();
		
		//Erstelle "Codetitel"
		Text codeTitle = lang.newText(new Offset(0,20,"header",AnimalScript.DIRECTION_SW), "Code","CodeTitle", null, codeTitleProps);
		  
		//Erstelle Umrandung fuer CodeTitel
		Rect CodeTitleRect = lang.newRect(new Offset(-5, -5, "CodeTitle",AnimalScript.DIRECTION_NW), new Offset(5, 5, "CodeTitle", "SE"), "CTRect",null, rectPropsCT);
			  
		sc = lang.newSourceCode(new Offset(0,20,"CodeTitle","SW"), "sourceCode",null, Code);
		sc.addCodeLine("public void SimpleStringMatching(String text, String word)", null, 0,null);	// 0
		sc.addCodeLine("{", null, 0, null); 														// 1
		sc.addCodeLine("ArrayList<Integer> result = new ArrayList<Integer>();", null, 1, null);		// 2
	    sc.addCodeLine("ArrayList<Integer[]> actual = new ArrayList<Integer[]>();", null, 1, null); // 3
	    sc.addCodeLine("for (int i=0;i<text.length();i++) {", null, 1, null); 						// 4
		sc.addCodeLine("for (int j=0;j<actual.size();j++) {", null, 2, null); 						// 5
		sc.addCodeLine("Integer[] tupel=actual.get(j);", null, 3, null);							// 6
		sc.addCodeLine("if (text.charAt(i)==word.charAt(tupel[1]+1)) {", null, 3, null); 			// 7
		sc.addCodeLine("tupel[1]++;", null, 4, null); 												// 8
		sc.addCodeLine("}", null, 3, null);															// 9
		sc.addCodeLine("else {", null, 3, null); 													// 10	
		sc.addCodeLine("actual.remove(tupel);", null, 4, null); 									// 11
		sc.addCodeLine("j--;", null, 4, null); 														// 12
		sc.addCodeLine("}", null, 3, null); 														// 13
		sc.addCodeLine("}", null, 2, null);															// 14
		sc.addCodeLine("if (text.charAt(i)==word.charAt(0)) {", null, 2, null); 					// 15
		sc.addCodeLine("Integer[] newTupel= {i,0};", null, 3, null); 								// 16
		sc.addCodeLine("actual.add(newTupel);", null, 3, null);										// 17
		sc.addCodeLine("}", null, 2, null); 														// 18
		sc.addCodeLine("if (actual.size()!=0) {", null, 2, null); 									// 19
		sc.addCodeLine("Integer[] oldest=actual.get(0);", null, 3, null); 							// 20
		sc.addCodeLine("if (oldest[1]+1==word.length()) {", null, 3, null); 						// 21
		sc.addCodeLine("result.add(oldest[0]);", null, 4, null); 									// 22
		sc.addCodeLine("actual.remove(oldest);", null, 4, null); 									// 23
		sc.addCodeLine("}", null, 3, null); 														// 24
		sc.addCodeLine("}", null, 2, null); 														// 25
		sc.addCodeLine("}", null, 1, null); 														// 26
		sc.addCodeLine("}", null, 0, null); 														// 27
		
		//Erstelle Umrandung fuer Code
		Rect CodeRect = lang.newRect(new Offset(0, 0, "CTRect",AnimalScript.DIRECTION_NW), new Offset(5, 5, "sourceCode", "SE"), "CRect",null, CodeKasten);
		
		//Wandle Strings in Array um
		String[] textS = new String[text.length()];
		String[] wordS = new String[word.length()];
		  
		for(int i=0;i<text.length();i++){
			textS[i]=Character.toString(text.charAt(i));
			}
		
		for(int i=0;i<word.length();i++){
			wordS[i]=Character.toString(word.charAt(i));
			}

		//Erstelle "Erlaeuterung des Arrays fuer den Text"
		TextProperties ArrDesProp = new TextProperties();
		ArrDesProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
		Text textArrDes = lang.newText(new Offset(30,10,"CRect","NE"), "Text:","TextArrayDescription", null, ArrDesProp);
		
		//Erstelle Textarray
		textArr = lang.newStringArray(new Offset(10,0,"TextArrayDescription","NE"), textS, "Textarray",null, TextArray);
		
		//Erstelle "Erlaeuterung des Arrays fuer das zu suchende Wort"
		Text wordArrDes = lang.newText(new Offset(0,70,"TextArrayDescription","SW"), "Wort:","WordArrayDescription", null, ArrDesProp);
		
		//Erstelle Wortarray
		wordArr = lang.newStringArray(new Offset(10,0,"WordArrayDescription",AnimalScript.DIRECTION_NE), wordS, "Wordarray",null, WortArray);
		
		//Erstelle "Erlaeuterung des Arrays fuer aktuelle Matches"
		Text matArrDes = lang.newText(new Offset(0,60,"WordArrayDescription","SW"), "Aktuelle Matches:","MatchArrayDescription", null, ArrDesProp);
		
		//Erstelle "Erlaeuterung des Arrays fuer Ergebnisse"
		Text resArrDes = lang.newText(new Offset(0,30,"MatchArrayDescription","SW"), "Ergebnisse:","ResultArrayDescription", null, ArrDesProp);
		
		//Erstelle "Erlaeuterungtitel"
		Text desTitle = lang.newText(new Offset(0,30,"ResultArrayDescription",AnimalScript.DIRECTION_SW), "Erläuterung","DescriptionTitle", null, codeTitleProps);
		  
		//Erstelle Umrandung fuer CodeTitel
		Rect desTitleRect = lang.newRect(new Offset(-5, -5, "DescriptionTitle",AnimalScript.DIRECTION_NW), new Offset(5, 5, "DescriptionTitle", "SE"), "DTRect",null, rectPropsCT);
		
		//Erstelle Umrandung fuer Erlaeuterung
		Text hid = lang.newText(new Offset(30,60,"DTRect","NW"), "Text aus dem aktuellen Tupel (10,10) um eins hoch.","hid", null, ErlaeuterungsText);
		hid.hide();
		Rect desRect = lang.newRect(new Offset(0, 0, "DTRect",AnimalScript.DIRECTION_NW), new Offset(30, 164, "hid", "SE"), "DRect",null, ErlaeuterungsKasten);
		
		//Hintegrund
		Offset rightest=new Offset(50,70,"DRect","SE");
		if(textArr.getLength()>29)rightest =new Offset(50,530,"Textarray","SE");
		Rect background = lang.newRect(new Coordinates(0,0),rightest, "BRect",null, HintergrundKasten);
		
		
		//Rufe den Algorithmus auf
		simpleStringMatching(text,word);
		
		//Verwerfe alles und zeige nur relevante Dinge fuer das Fazit
		lang.hideAllPrimitives();
		header.show();
		headerRect.show();
		einleitungsTitel.setText("Fazit", null, null);
		einleitungsTitel.show();
		EinTitleRect.show();
		backgroundE.show();
		desRectE.show();
		
		// Ergebnis in String fassen fuer das Fazit
		String stellen;
		StringBuffer sb = new StringBuffer();
		for(int i =0;i<result.size();i++){
			sb.append(result.get(i));
			if(i==(result.size()-2))sb.append(" und ");
			else if (i<(result.size()-2))sb.append(", ");
			else sb.append(".");
		}
		stellen=sb.toString();
		
		// Ende des Satzes
		String ende;
		if(result.size()==0)ende=".";
		else ende =" und zwar an den Stellen "+stellen;
		
		//Fazit erstellen
		Text f1 = lang.newText(new Offset(30,90,"ETRect","NW"), "Da jeder Buchstabe aus dem 'Text' betrachtet worden ist, ist nun der Algorithmus zu Ende. Unser 'Wort'","f1", null, ErlaeuterungsText);
		Text f2 = lang.newText(new Offset(0,7,"f1","SW"), "wurde "+result.size()+"-Mal im Text gefunden"+ende,"f2", null, ErlaeuterungsText);
		Text f3 = lang.newText(new Offset(0,7,"f2","SW"), "Der 'Simple String Matching' Algorithmus hat im Worst-Case eine Laufzeit von O(n*m), wobei n die Länge","f3", null, ErlaeuterungsText);
		Text f4 = lang.newText(new Offset(0,7,"f3","SW"), "des Textes ist und m die maximale Anzahl der gleichzeitig möglichen aktuellen Kandidaten.","f4", null, ErlaeuterungsText);
		Text f5 = lang.newText(new Offset(0,7,"f4","SW"), "Der 'Simple String Matching' Algorithmus ist sowohl relativ einfach zu verstehen als auch zu programmieren.","f5", null, ErlaeuterungsText);
		Text f6 = lang.newText(new Offset(0,7,"f5","SW"), "Jedoch ist das Anwenden von finiten Zustandsautomaten durchaus üblicher, um solch ein String Matching","f6", null, ErlaeuterungsText);
		Text f7 = lang.newText(new Offset(0,7,"f6","SW"), "Problem zu lösen.","f7", null, ErlaeuterungsText);
		
		lang.nextStep("Fazit");
	  }
	  
	/**
	 * Diese Methode ist der String- Matching Algorithmus inklusive der Animation zu jeden Schritt
     * @param text, String in welchem ein String gesucht werden soll
     * @param word, String der gesucht werden soll
	 */
	private void simpleStringMatching(String text, String word){
		//Arraylist welche fuer den Algorithmus benoetigt wird fuer actuelle Matches
		ArrayList<Integer[]> actual = new ArrayList<Integer[]>();
		
		//Fuer Visualisierung benoetigte Datenstrukturen
		StringArray actualMatches = null;
		IntArray actualResult = null;
		String[] actualArr= new String[0];
		int[]resultArr = new int[0];
		
		//Erstelle erste Erlaeuterung
		Text des11 = lang.newText(new Offset(30,60,"DRect","NW"), "Erstelle zwei beliebige Datentypen, in denen die","des11", null, ErlaeuterungsText);
		Text des12 = lang.newText(new Offset(0,7,"des11","SW"), "aktuellen Matches bzw. die (zwischenzeitlichen)","des12", null, ErlaeuterungsText);
		Text des13 = lang.newText(new Offset(0,7,"des12","SW"), "Endergebnisse gespeichert werden sollen.","des13", null, ErlaeuterungsText);
		// Highlighte passende Code Zeilen
		sc.highlight(2);
		sc.highlight(3);
		lang.nextStep();
		
		//Highlighte richtige Zeilen und erstelle Erlaeuterung
		sc.unhighlight(2);
		sc.unhighlight(3);
		des11.hide();
		des12.hide();
		des13.hide();
		
		sc.highlight(4);
		sc.highlight(26);
		Text des21 = lang.newText(new Offset(30,60,"DRect","NW"), "Gehe durch jedes Zeichen aus dem String","des21", null, ErlaeuterungsText);
		Text des22 = lang.newText(new Offset(0,7,"des21","SW"), "des Gesammttextes...","des22", null, ErlaeuterungsText);
		lang.nextStep();
		
		//Verwerfe Highlights und Erlaeuterung aus vorherigen Schritt, Highlighte richtige Zeilen und erstelle Erlaeuterung
		sc.unhighlight(4);
		sc.unhighlight(26);
		des21.hide();
		des22.hide();
		sc.highlight(5);
		sc.highlight(14);
		Text des31 = lang.newText(new Offset(30,60,"DRect","NW"), "... und betrachte dann jeweils jedes Tupel","des31", null, ErlaeuterungsText);
		Text des32 = lang.newText(new Offset(0,7,"des31","SW"), "aus dem Datentypen der aktuellen Matches.","des32", null, ErlaeuterungsText);
		lang.nextStep();
		
		
		//Verwerfe Highlights aus vorherigen Schritt
		sc.unhighlight(5);
		sc.unhighlight(14);
		des31.hide();
		des32.hide();
		
		//Erstelle Pfeil ueber Gesammttextarray
		ArrayMarkerProperties arrMProp = new ArrayMarkerProperties();
	    arrMProp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
	    arrMProp.set(AnimationPropertiesKeys.COLOR_PROPERTY,ArrayMarkerEigenschaften.get(AnimationPropertiesKeys.COLOR_PROPERTY));
	    ArrayMarker arrM = lang.newArrayMarker(textArr, 0, "i",null, arrMProp);
	    
	    //Pfeil ueber Wortarray
		ArrayMarkerProperties arrMProp2 = new ArrayMarkerProperties();
	    arrMProp2.set(AnimationPropertiesKeys.LABEL_PROPERTY, "Vergleiche");
	    arrMProp2.set(AnimationPropertiesKeys.COLOR_PROPERTY, ArrayMarkerEigenschaften.get(AnimationPropertiesKeys.COLOR_PROPERTY));
	    arrMProp2.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, false);
	    ArrayMarker arrM2 = lang.newArrayMarker(wordArr, 0, "vergleiche",null, arrMProp2);
	    
	    //Pfeif ueber Matcharray
		ArrayMarkerProperties arrMProp3 = new ArrayMarkerProperties();
	    arrMProp3.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
	    arrMProp3.set(AnimationPropertiesKeys.COLOR_PROPERTY, ArrayMarkerEigenschaften.get(AnimationPropertiesKeys.COLOR_PROPERTY));
	    arrMProp3.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, false);
	    ArrayMarker arrM3=null;
	    
	    //Betrachte nun jedes Zeichen aus dem Textarray
	    for(int i=0;i<text.length();i++){
	    	//Bewege Pfeil zum richtigen Zeichen und Highlighte die entspechende Zell
			arrM.move(i, null, null);
			textArr.highlightCell(i, null, null);
			textArr.highlightElem(i, null, null);
			
			//Betrachte nun jedes Tupel aus den aktuellen Matches
			for(int j=0;j<actual.size();j++){
				Integer[]arr=actual.get(j);
			    //Setze Pfeil ueber entspechende Zelle
				if(j==0){
			    	arrM3 = lang.newArrayMarker(actualMatches, 0, "j",null, arrMProp3);
			    	}
			    if(arrM3.getPosition()!=j)arrM3.move(j, null, null);
				
			    //Bewege Pfeil ueber dem Wort zum Buchstaben, welcher aktuelle verglichen werden soll, highlighte entsprechende Zellen
				arrM2.move(arr[1]+1, null, null);
				wordArr.highlightCell(arr[1]+1, null, null);
				wordArr.highlightElem(arr[1]+1, null, null);
				actualMatches.highlightCell(j, null, null);
				actualMatches.highlightElem(j, null, null);
				
				//Highlighte richtige Zeilen und erstelle Erlaeuterung
				Text des61 = lang.newText(new Offset(30,60,"DRect","NW"), "Prüfe ob aktuell betrachteter Buchstabe "+text.charAt(i),"des61", null, ErlaeuterungsText);
				Text des62 = lang.newText(new Offset(0,7,"des61","SW"), "zum nächsten Buchtaben des Wortes aus dem","des62", null, ErlaeuterungsText);
				Text des63 = lang.newText(new Offset(0,7,"des62","SW"), "aktuellen Tupel ("+arr[0]+","+arr[1]+") passt.","des63", null, ErlaeuterungsText);
				sc.highlight(7);
				sc.highlight(9);
				
				lang.nextStep();
				
				//Verwerfe Highlights und Erlaeuterung aus vorherigen Schritt
				sc.unhighlight(7);
				sc.unhighlight(9);
				des61.hide();
				des62.hide();
				des63.hide();
				textArr.unhighlightCell(i, null, null);
				textArr.unhighlightElem(i, null, null);
				wordArr.unhighlightCell(arr[1]+1, null, null);
				wordArr.unhighlightElem(arr[1]+1, null, null);
				actualMatches.unhighlightCell(j, null, null);
				actualMatches.unhighlightElem(j, null, null);

				//Pruefe ob aktueller Buchstabe des Textes zum naechsten Buchstaben des Wortes aus dem aktuellen Tupel passen wueder
				if(text.charAt(i)==word.charAt(arr[1]+1)){

					//Erstelle die Erlaeuterung, dass es passt und highlighte richtige Codezeile 
						Text des81b = lang.newText(new Offset(30,60,"DRect","NW"), "Passt! Also zähle die Anzahl der Treffer im","des81b", null, ErlaeuterungsText);
						Text des82b = lang.newText(new Offset(0,7,"des81b","SW"), "Text aus dem aktuellen Tupel ("+arr[0]+","+arr[1]+") um eins hoch.","des82b", null, ErlaeuterungsText);
						sc.highlight(8);
						
						//Zaehle Anzahl der Treffer nach dem ersten Bustaben des Wortes mit dem Text um 1 hoch
						arr[1]++;
						
						//Passe dies auch an der Visualisierung an, highlighte dazu das veraenderte Tupel
						actualArr[j]="("+arr[0]+","+arr[1]+")";
						actualMatches.put(j, "("+arr[0]+","+arr[1]+")", null, null);
						actualMatches.highlightCell(j, null, null);
						actualMatches.highlightElem(j, null, null);
						
						lang.nextStep();
						
						//Verwerfe Highlights und Erlaeuterung aus vorherigen Schritt
						actualMatches.unhighlightCell(j, null, null);
						actualMatches.unhighlightElem(j, null, null);
						des81b.hide();
						des82b.hide();
						sc.unhighlight(8);
				}
				
				
				else{
					//Erstelle Erlaeuterung, dass der naechste Buchstabe nicht mehr passt und highlighte entsprechende Codezeilen
					Text des71b = lang.newText(new Offset(30,60,"DRect","NW"), "Passt nicht! Also entferne das aktuelle Tupel ("+arr[0]+","+arr[1]+")","des71b", null, ErlaeuterungsText);
					Text des72b = lang.newText(new Offset(0,7,"des71b","SW"), "und sorge dafür, dass der Zähler j der Schleife","des72b", null, ErlaeuterungsText);
					Text des73b = lang.newText(new Offset(0,7,"des72b","SW"), "zum nächsten Durchlauf nicht erhöht wird.","des73b", null, ErlaeuterungsText);
					sc.highlight(10);
					sc.highlight(11);
					sc.highlight(12);
					sc.highlight(13);
					
					//Schmeiss das Tupel aus dem zu visualisierendem Array raus
					actualMatches.hide();
					actualArr=deleteElementAt(actualArr, j);
					if(actualArr.length>0)actualMatches= lang.newStringArray(new Offset(10,0,"MatchArrayDescription","NE"), actualArr, "Matcharray",null, AktuelleMatchesArray);
					
					//und ebenfalls aus Datenstruktur des Algorithmus
					actual.remove(arr);
					j--;
					
					lang.nextStep();
					
					//Verwerfe Highlights und Erlaeuterung aus vorherigen Schritt
					des71b.hide();
					des72b.hide();
					des73b.hide();
					sc.unhighlight(10);
					sc.unhighlight(11);
					sc.unhighlight(12);
					sc.unhighlight(13);
				}
			}
			//Highlighte nun das erste Element des Wortes und das aktuelle des Textes...
			if(arrM2.getPosition()!=0)arrM2.move(0, null,null);
			wordArr.highlightCell(0, null, null);
			wordArr.highlightElem(0, null, null);
			textArr.highlightCell(i, null, null);
			textArr.highlightElem(i, null, null);
			if(arrM3!=null)arrM3.hide();
			
			//Erstelle Erlaeuterung und highlighte passende Codezeilen
			Text des41 = lang.newText(new Offset(30,60,"DRect","NW"), "Prüfe ob nulltes Zeichen aus dem zu","des41", null, ErlaeuterungsText);
			Text des42 = lang.newText(new Offset(0,7,"des41","SW"), "suchenden Wort mit dem "+i+". Zeichen des","des42", null, ErlaeuterungsText);
			Text des43 = lang.newText(new Offset(0,7,"des42","SW"), "Gesammttextes übereinstimmt.","des43", null, ErlaeuterungsText);
			sc.highlight(15);
			sc.highlight(18);
			lang.nextStep();
			
			//Verwerfe Highlights und Erlaeuterung aus vorherigen Schritt
			wordArr.unhighlightCell(0, null, null);
			wordArr.unhighlightElem(0, null, null);
			textArr.unhighlightCell(i, null, null);
			textArr.unhighlightElem(i, null, null);
			sc.unhighlight(15);
			sc.unhighlight(18);
			des41.hide();
			des42.hide();
			des43.hide();
			
			//....und pruefe ob die Beiden Zeichen passen
			if(text.charAt(i)==word.charAt(0)){
				//Passt also erstelle neues Tupel und fuege es in aktuelle Matches ein
				Integer[] neu = {i,0};
				actual.add(neu);
				
				//Highlighte entsprechende Codezeilen und erstelle Beschreibung dafuer
				sc.highlight(16);
				sc.highlight(17);
				Text des51 = lang.newText(new Offset(30,60,"DRect","NW"), "Ja, also erstelle nun neues Tupel ("+i+",0) und","des51", null, ErlaeuterungsText);
				Text des52 = lang.newText(new Offset(0,7,"des51","SW"), "speichere es in Datentyp für aktuelle Matches.","des52", null, ErlaeuterungsText);
				
				//Passe ebenfalls zu visualisierendes Array mit Matches an
				if(actualMatches!=null)actualMatches.hide();
				actualArr=insertStringAt(actualArr,"("+i+",0)",-1);
				actualMatches= lang.newStringArray(new Offset(10,0,"MatchArrayDescription","NE"), actualArr, "Matcharray",null, AktuelleMatchesArray);
				actualMatches.highlightCell(actualMatches.getLength()-1, null, null);
				actualMatches.highlightElem(actualMatches.getLength()-1, null, null);
				lang.nextStep();
				
				//Verwerfe Highlights und Erlaeuterung aus vorherigen Schritt
				sc.unhighlight(16);
				sc.unhighlight(17);
				actualMatches.unhighlightCell(actualMatches.getLength()-1, null, null);
				actualMatches.unhighlightElem(actualMatches.getLength()-1, null, null);
				des51.hide();
				des52.hide();
			}
			
			//Wenn ein Zeichen in aktuelle Matches enthalten ist...
			if(actual.size()!=0){
				//.. dann hole das aelteste heraus....
				Integer[]oldest=actual.get(0);
				
				//Erstelle Beschreibung und highlighte entsprechende Zellen
				Text des90 = lang.newText(new Offset(30,60,"DRect","NW"), "Betrachte nun das in den aktuellen Matches am","des90", null, ErlaeuterungsText);
				Text des91 = lang.newText(new Offset(0,7,"des90","SW"), "längsten enthaltene Tupel ("+oldest[0]+","+oldest[1]+") und","des91", null, ErlaeuterungsText);
				Text des92 = lang.newText(new Offset(0,7,"des91","SW"), "prüfe, ob dafür schon das komplette gesuchte","des92", null, ErlaeuterungsText);
				Text des93 = lang.newText(new Offset(0,7,"des92","SW"), "Wort gefunden wurde.","des93", null, ErlaeuterungsText);
				sc.highlight(20);
				sc.highlight(21);
				sc.highlight(24);
				actualMatches.highlightCell(0, null, null);
				actualMatches.highlightElem(0, null, null);
				lang.nextStep();
				
				//Highlighte richtige Zeilen und erstelle Erlaeuterung
				sc.unhighlight(20);
				sc.unhighlight(21);
				sc.unhighlight(24);
				des90.hide();
				des91.hide();
				des92.hide();
				des93.hide();
				actualMatches.unhighlightCell(0, null, null);
				actualMatches.unhighlightElem(0, null, null);
				
				//... und pruefe ob das Wort dort schon komplett gefunden wurde.
				if(oldest[1]+1==word.length()){
					
					//Gefunden! Highlighte entsprechende Codezeilen und erstelle Erlaeuterung
					sc.highlight(22);
					sc.highlight(23);
					Text des81a = lang.newText(new Offset(30,60,"DRect","NW"), "Auch das ist der Fall, also füge die Stelle des","des81a", null, ErlaeuterungsText);
					Text des82a = lang.newText(new Offset(0,7,"des81a","SW"), "ersten Treffers, aus dem Tupel ("+oldest[0]+","+oldest[1]+") in den","des82a", null, ErlaeuterungsText);
					Text des83a = lang.newText(new Offset(0,7,"des82a","SW"), "Datentypen für (zwischenzeitliche) Ergebnisse","des83a", null, ErlaeuterungsText);
					Text des84a = lang.newText(new Offset(0,7,"des83a","SW"), "und entferne das Tupel.","des84a", null, ErlaeuterungsText);
					
					//Fuege Stelle des ersten Matches in zu Visualisierenden Ergebnissarray ein
					if(actualResult!=null)actualResult.hide();
					resultArr=insertIntAtLast(resultArr, oldest[0]);
					actualResult=lang.newIntArray(new Offset(10,0,"ResultArrayDescription","NE"), resultArr, "Resultarray",null, ErgebnisArray);
					actualResult.highlightCell(actualResult.getLength()-1, null, null);
					actualResult.highlightElem(actualResult.getLength()-1, null, null);
					
					//und loesche Tupel aus aktuellen Matches
					actualMatches.hide();
					actualArr=deleteElementAt(actualArr, 0);
					if(actualArr.length!=0)actualMatches=lang.newStringArray(new Offset(10,0,"MatchArrayDescription","NE"), actualArr, "Matcharray",null, AktuelleMatchesArray);
					
					//Mache dies auch fuer Datenstrukturen des Algorithmus
					result.add(oldest[0]);
					actual.remove(oldest);
					
					lang.nextStep("Das gesuchte Wort wurde zum "+result.size()+".Mal an der Stelle "+result.get(result.size()-1)+" gefunden!");
					
					//Highlighte richtige Zeilen und erstelle Erlaeuterung
					actualResult.unhighlightCell(actualResult.getLength()-1, null, null);
					actualResult.unhighlightElem(actualResult.getLength()-1, null, null);
					des81a.hide();
					des82a.hide();
					des83a.hide();
					des84a.hide();
					sc.unhighlight(22);
					sc.unhighlight(23);
					
				}
			}
		}
	}
	
	/**
	 * Diese Methode loescht ein String in einem StringArray und gibt diesen ohne "Luecke" wieder aus
	 * @param array, in welchem was geloescht werden soll
	 * @param at, Stelle an welcher der Eintrag geloescht werden soll
	 * @return den Array ohne zu loeschende Stelle
	 */
	public String[] deleteElementAt(String[] array,int at){
		String[]result= new String[array.length-1];
		for(int i=0;i<at;i++)result[i]=array[i];
		for(int i=at;i<array.length-1;i++)result[i]=array[i+1];
		return result;
	}
	
	/**
	 * Diese Methode fuegt einen String an einer Stelle eines Arrays ein
	 * @param array, in welchem etwas eingefuegt werden soll
	 * @param string, welcher eingefuegt werden soll
	 * @param at Stelle an welcher der String eingefuegt werden soll, wobei -1 fuer ein Einfuegen an der letzten Stelle steht
	 * @return den Array in welchem der String an entsprechender Stelle eingefuegt worden ist
	 */
	public String[] insertStringAt(String[] array,String string,int at){
		String[]result= new String[array.length+1];
		if(at==-1){
			for(int i=0;i<array.length;i++)result[i]=array[i];
			result[array.length]=string;
		}
		else{
			for(int i=0;i<at;i++)result[i]=array[i];
			result[at]=string;
			for(int i=at;i<array.length;i++)result[i+1]=array[i];
		}
		return result;
	}
	
	/**
	 * Diese Methode fuegt eine Zahl am ende eines int-Arrays ein
	 * @param array, in welchem die Zahl eingefuegt werden soll
	 * @param e einzufuegende Zahl
	 * @return den Array mit eingefuegter Zahl an letzter Stelle
	 */
	public int[] insertIntAtLast(int[] array,int e){
		int[]result= new int[array.length+1];
		for(int i=0;i<array.length;i++)result[i]=array[i];
		result[array.length]=e;
		return result;
	}
	
	/**
	 * Diese Methode gibt den Namen des Generators aus
	 */
    public String getName() {
        return "Simple String Matching";
    }

    /**
     * Diese Methode gibt den Namen des Algorithmus aus
     */
    public String getAlgorithmName() {
        return "Simple String Matching";
    }

    /**
     * Diese Methode gibt den Namen des Authors des Algos. aus
     */
    public String getAnimationAuthor() {
        return "Kevin Kocon";
    }

    /**
     * Diese Methode gibt die Beschreibung des Algos. aus
     */
    public String getDescription(){
        return "Beim  \"Simple String Matching\" handelt es sich um einen Algorithmus, mit dem man, wie der Name schon sagt, relativ einfach einen (Such-) String in einem (Gesamt-) String finden kann. Im Alltag wird solch ein Algorithmus beispielsweise verwendet, um ein bestimmtes Wort in einem Text zu finden."
 +"\n"
 +"Als Eingabe erhält der Algorithmus also zwei Strings."
 +"\n"
 +"Als Ergebnis liefert der Algorithmus alle Stellen, an welchen der gesuchte String im kompletten String beginnt. Die Stellen werden als Zahlen, welche den Positionen der Anfänge entsprechen, ausgegeben.";
    }

    /**
     * Diese Methode gibt ein Codebeispiel fuer den Algo. aus
     */
    public String getCodeExample(){
        return "public void SimpleStringMatching(String text, String word){"
 +"\n"
 +" "
 +"\n"
 +" ArrayList<Integer> result = new ArrayList<Integer>();"
 +"\n"
 +" ArrayList<Integer[]> actual = new ArrayList<Integer[]>();"
 +"\n"
 +" for (int i=0;i<text.length();i++) {"
 +"\n"
 +"  for (int j=0;j<actual.size();j++) {"
 +"\n"
 +"   Integer[] tupel=actual.get(j);"
 +"\n"
 +"   if (text.charAt(i)==word.charAt(tupel[1]+1)) {"
 +"\n"
 +"    tupel[1]++;"
 +"\n"
 +"   }"
 +"\n"
 +"   else {"
 +"\n"
 +"    actual.remove(tupel);"
 +"\n"
 +"    j--;"
 +"\n"
 +"   }"
 +"\n"
 +"  }"
 +"\n"
 +"  if (text.charAt(i)==word.charAt(0)) {"
 +"\n"
 +"   Integer[] newTupel= {i,0};"
 +"\n"
 +"   actual.add(newTupel);"
 +"\n"
 +"  }"
 +"\n"
 +"  if (actual.size()!=0) {"
 +"\n"
 +"   Integer[] oldest=actual.get(0);"
 +"\n"
 +"   if (oldest[1]+1==word.length()) {"
 +"\n"
 +"    result.add(oldest[0]);"
 +"\n"
 +"    actual.remove(oldest);"
 +"\n"
 +"   }"
 +"\n"
 +"  }"
 +"\n"
 +" }"
 +"\n"
 +"}";
    }

    /**
     * Diese Methode gibt den Dateitypen des generierten Generators aus
     */
    public String getFileExtension(){
        return "asu";
    }

    /**
     * Diese Methode gibt die (natuerlicher) Sprache in welcher der Generator ist
     */
    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    /**
     * Diese Methode gibt den Typen des Generators aus
     */
    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
    }

    /**
     * Diese Methode gibt die Programmiersprache aus anhand welcher der Generator den Algo erklaert
     */
    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

}