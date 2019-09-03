/*
 * NWayCacheLRU.java
 * Christopher Ries, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.hardware;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Locale;
import java.util.Hashtable;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import algoanim.animalscript.AnimalScript;

import algoanim.primitives.Primitive;
import algoanim.primitives.generators.Language;
import algoanim.primitives.Text;
import algoanim.primitives.Rect;
import algoanim.primitives.Polyline;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;

import algoanim.properties.TextProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.SourceCodeProperties;

import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.TrueFalseQuestionModel;

import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.DisplayOptions;
import algoanim.util.Timing;
import algoanim.util.TicksTiming;
import algoanim.util.MsTiming;


public class NWayCacheLRU implements Generator {

    private Language lang;
    private TextProperties headerProps;
	private TextProperties cacheHeaderProps;
	private RectProperties headerRectProps;	
 	private RectProperties rectElemProps;
	private SourceCodeProperties pseudoCodeProps;
	private MatrixProperties cacheProps;
	private MatrixProperties lruProps;
	private ArrayProperties insertArrayProps;
	private int[] insertValues;

	int[][] matrix = new int[4][2];
	int[][] lruInit = new int[4][1];
	MsTiming longDur;
	Text insertElem;
	Text modulo;

 public void init(){
        lang = new AnimalScript("NWayCacheLRU", "Christopher Ries", 800, 600);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		lang.setStepMode(true);
    }
	
	private static final String DESCRIPTION = 
		"Ein 2-Way Set Associative Cache ist eine schnelle Speicherstruktur, die Zugriffe auf ein langsameres \n"
		+"Speichermedium (bspw. die Festplatte) zu vermeiden hilft. Dabei bleiben Daten, die geladen wurden so \n"
		+"lange im Cache bis sie verdraengt werden und koennen dadurch schneller abgerufen werden. \n"
		+"Darueberhinaus wird nicht nur das benoetigte Datum welches vom Benutzer direkt oder von einem \n"
		+"ausgefuehrten Programm angefordert wurde, sondern Datenbloecke (eine festeglegte Anzahl Daten, die \n"
		+"bei einem Zugriff uebertragen werden) in den Cache geladen. Grund dafuer ist die hohe Chance, dass \n"
		+"die Daten, die nahe des benoetigten Datums gespeichert wurden im Anschluss auch benoetigt werden, \n"
		+"was beispielsweise bei Schleifen und Arrays der Fall ist. \n"
		+"Als Wege (Ways) werden die verschiedenen Datenbloecke im gleichen Set benannt. Bei einem \n"
		+"2-Way Cache mit 4 Sets wie in der Animation zu sehen koennen so 8 Datenbloecke gespeichert werden. \n"
		+"Aufgrund der besseren Uebersicht werden in der folgenden Animation jedoch nur die gelesenen \n"
		+"Speicheradressen und keine Datenbloecke im Cache gespeichert. \n"
		+"LRU bezeichnet eine Verdraengungsstrategie, die angewandt wird wenn sich an der Stelle, an der ein \n"
		+"neuer Wert in den Cache geschrieben werden soll bereits ein Wert befindet. \n"
		+"Dabei steht LRU fuer Least Recently Used, sprich der 'LRU'-Wert ist der Eintrag der zeitlich am \n"
		+"laengsten nicht gelesen wurde.\n"
		+"Diese Verdraengungsstrategie hat den Vorteil, dass die Werte aus dem Cache verdraengt werden, \n"
		+"die nicht so oft gelesen werden und so zeitliche und raeumliche Lokalitaet ausgenutzt werden. \n";
	
	private static final String PSEUDOCODE = 
		"Pruefen ob sich die gelesene Speicheradresse \n"
		+"\t\t\tim Cache befindet \n"
		+"\tWenn 'ja': LRU gegebenenfalls aktualisieren \n"
		+"\tWenn 'nein': Pruefen ob ein Weg im Set frei ist \n"
		+"\t\tWenn 'ja': Wert in freien Weg einfuegen \n"
		+"\t\t\t\t\t\t\t und wenn noetig 'LRU' setzen \n"
		+"\t\tWenn 'nein': Ersetzte 'LRU'-Wert in Cache \n"
		+"\t\t\t\t\t\t\t\t und aktualisiere 'LRU'";
	
	private static final String FAZIT = 
		"Diese Verdraengungsstrategie hat den Vorteil, dass der Aufwand der Implementierung und der Verwaltung \n"
		+"ueberschaubar ist. Außerdem passt sich die Strategie auf die jeweiligen Gegebenheiten bei \n"
		+"verschiedenen Zugriffsfolgen an. Deshalb werden Daten die vor kurzer Zeit gebraucht wurden\n"
		+"im Cache gehalten und muessen bei erneutem Gebrauch nicht nochmals aus dem langsamen Speichermedium \n"
		+"gelesen werden";
	
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
		
		insertValues = (int[]) primitives.get("accessAdresses");
		
		longDur = new MsTiming ((int) primitives.get("highlightTime"));
		
		// header
        headerProps = (TextProperties)props.getPropertiesByName("header");
		Font headerFont = (Font) headerProps
				.get(AnimationPropertiesKeys.FONT_PROPERTY);
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				headerFont.getName(), Font.BOLD, 24));
		
		// headerRect
		headerRectProps = new RectProperties();
		headerRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		headerRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		headerRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		
		// cacheHeader
		cacheHeaderProps = (TextProperties)props.getPropertiesByName("cacheHeader");
		Font cacheFont = (Font) cacheHeaderProps
				.get(AnimationPropertiesKeys.FONT_PROPERTY);
		cacheHeaderProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				cacheFont.getName(), Font.BOLD, 16));
		
		pseudoCodeProps = (SourceCodeProperties) props
				.getPropertiesByName("pseudoCode");
				
		// rectElem
		rectElemProps = new RectProperties();
		rectElemProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		rectElemProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectElemProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		
		// insertArray
		insertArrayProps = new ArrayProperties();	
		insertArrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		insertArrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		insertArrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		insertArrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				headerFont.getName(), Font.BOLD, 14));
		insertArrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);		
		
		// cacheMatrix
		cacheProps = (MatrixProperties)props.getPropertiesByName("matrixProps");
		cacheProps.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
		cacheProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		cacheProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				headerFont.getName(), Font.BOLD, 14));
		//cacheProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 27));		
		
		// fifoMatrix
		lruProps = (MatrixProperties)props.getPropertiesByName("fifoMatrixProps");
		lruProps.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
		lruProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
	
		
		startCache();
		lang.finalizeGeneration();
        return lang.toString();
    }
	
	
	// #################################################
	// STEP by STEP
	// #################################################
	
	public void startCache(){
		
		MultipleChoiceQuestionModel whichSet = new MultipleChoiceQuestionModel("whichSet");
		MultipleChoiceQuestionModel whatIsLRU = new MultipleChoiceQuestionModel("whatIsLRU");
		MultipleChoiceQuestionModel whyLRU = new MultipleChoiceQuestionModel("whyLRU");
		
		// STEP 1
		// /////////////////////////////////////////////
		// Einleitung
		
		Text title = lang.newText(new Coordinates(30, 30), "2-Way Cache mit LRU Verdrängungsstrategie",
				"title", null, headerProps);
				
		Rect rectBackgroundTitle = lang.newRect(new Offset(-10, -10, title,
				AnimalScript.DIRECTION_NW), new Offset(10, 10, title,
				AnimalScript.DIRECTION_SE), "rectBackgroundTitle", null,
				headerRectProps);
				
		SourceCode description = lang.newSourceCode(new Coordinates(30, 100),
				"description", null, pseudoCodeProps);

		setTextToSourceCode(description, DESCRIPTION);
		
		// STEP 2
		// /////////////////////////////////////////////
		// Gerüst aufbauen
		
		lang.nextStep("Erklaerung");
		description.hide();		
		
		// Grundgerüst
		
		SourceCode pseudoCode = lang.newSourceCode(new Coordinates(30, 150), "pseudoCode", null, pseudoCodeProps);
		setTextToSourceCode(pseudoCode, PSEUDOCODE);		
		
		lang.newPolyline(new Node[]{new Offset(10, -30, pseudoCode, AnimalScript.DIRECTION_NE),
				new Offset(10, 270, pseudoCode, AnimalScript.DIRECTION_NE)}, "seperatorV", null);
				
		lang.newPolyline(new Node[]{new Offset(-10, 10, pseudoCode, AnimalScript.DIRECTION_SW),
				new Offset(-10, 10, pseudoCode, AnimalScript.DIRECTION_SE)}, "seperatorH", null);
		
		insertElem = lang.newText(new Offset(0, 30, pseudoCode, AnimalScript.DIRECTION_SW), "Gelesene Speicheradresse:", "insertElem", null, cacheHeaderProps);
		modulo = lang.newText(new Offset(0, 20, insertElem, AnimalScript.DIRECTION_SW), "Speicheradresse modulo 4 = Set", "modulo", null, cacheHeaderProps);		
		
		Text picks = lang.newText(new Offset(0, 90, modulo, AnimalScript.DIRECTION_SW), "Speicherzugriffe:", "picks", null, cacheHeaderProps);
		IntArray insertArray = lang.newIntArray(new Offset(10, 0, picks, AnimalScript.DIRECTION_NE), insertValues, "insertArray", null, insertArrayProps);
		SourceCode expl = lang.newSourceCode(new Offset(0, 5, insertArray, AnimalScript.DIRECTION_SW), "expl", null, pseudoCodeProps);
		expl.addCodeLine("Gelb: schon gelesen       Rot: aktuell bearbeitet", null, 0, null);
		
		Text setHeader = lang.newText(new Offset(30, -10, pseudoCode, AnimalScript.DIRECTION_NE), "Set", "setHeader", null, cacheHeaderProps);
		Text set0 = lang.newText(new Offset(35, 60, pseudoCode, AnimalScript.DIRECTION_NE), "0", "set0", null, cacheHeaderProps);
		Text set1 = lang.newText(new Offset(35, 115, pseudoCode, AnimalScript.DIRECTION_NE), "1", "set1", null, cacheHeaderProps);
		Text set2 = lang.newText(new Offset(35, 170, pseudoCode, AnimalScript.DIRECTION_NE), "2", "set2", null, cacheHeaderProps);
		Text set3 = lang.newText(new Offset(35, 225, pseudoCode, AnimalScript.DIRECTION_NE), "3", "set3", null, cacheHeaderProps);
		
		// Cache-Matrix mit Initialisierung
		IntMatrix cache = lang.newIntMatrix(new Offset(40, -22, set0, AnimalScript.DIRECTION_NE), matrix, "cache", null, cacheProps);
		cache.put(0, 0, 0, null, null);
		cache.put(0, 1, 0, null, null);
		cache.put(1, 0, 0, null, null);
		cache.put(1, 1, 0, null, null);
		cache.put(2, 0, 0, null, null);
		cache.put(2, 1, 0, null, null);
		cache.put(3, 0, 0, null, null);
		cache.put(3, 1, 0, null, null);
		
		Text way0Header = lang.newText(new Offset(35, -50, cache, AnimalScript.DIRECTION_NW), "Way 0", "way0Header", null, cacheHeaderProps);
		Text way1Header = lang.newText(new Offset(150, -50, cache, AnimalScript.DIRECTION_NW), "Way 1", "way1Header", null, cacheHeaderProps);
		
		
		// LRU-Matrix mit Initialisierung
		IntMatrix lruCol = lang.newIntMatrix(new Offset(30, 0, cache, AnimalScript.DIRECTION_NE), lruInit, "lruCol", null, lruProps);
		lruCol.put(0,0,0,null,null);
		lruCol.put(1,0,0,null,null);
		lruCol.put(2,0,0,null,null);
		lruCol.put(3,0,0,null,null);

		Text fifoHeader  = lang.newText(new Offset(10, -50, lruCol, AnimalScript.DIRECTION_NW), "LRU",   "fifoHeader",  null, cacheHeaderProps);
		
		// für for-Schleife
		int value;
		int inSet;
		int i;
		
		// STEP 3
		// /////////////////////////////////////////////
		// alle Steps zum Füllen des Cache

		for (i=0; i<insertValues.length; i++){
			if(i==0){
				lang.nextStep("Leerer Cache");
			}else{
				lang.nextStep();
			}	
			// alten Step aufräumen
			pseudoCode.unhighlight(2);
			pseudoCode.unhighlight(4);
			pseudoCode.unhighlight(5);
			pseudoCode.unhighlight(6);
			pseudoCode.unhighlight(7);
			clearElem();
			
			// neuen Wert vorbereiten
			value =  insertValues[i];
			inSet = value%4;
			
			if(i==6){
				whichSet.setPrompt("In welches Set wird der naechste Wert " + value + " eingefuegt ?");
				switch (inSet){
					case 0: whichSet.addAnswer("0", 1, "Richtig!");
							whichSet.addAnswer("1", 0, "Falsch. Die richtige Antwort waere 0");
							whichSet.addAnswer("2", 0, "Falsch. Die richtige Antwort waere 0");
							whichSet.addAnswer("3", 0, "Falsch. Die richtige Antwort waere 0");
							break;
					case 1: whichSet.addAnswer("1", 1, "Richtig!");
							whichSet.addAnswer("0", 0, "Falsch. Die richtige Antwort waere 1");
							whichSet.addAnswer("2", 0, "Falsch. Die richtige Antwort waere 1");
							whichSet.addAnswer("3", 0, "Falsch. Die richtige Antwort waere 1");
							break;
					case 2: whichSet.addAnswer("2", 1, "Richtig!");
							whichSet.addAnswer("1", 0, "Falsch. Die richtige Antwort waere 2");
							whichSet.addAnswer("0", 0, "Falsch. Die richtige Antwort waere 2");
							whichSet.addAnswer("3", 0, "Falsch. Die richtige Antwort waere 2");
							break;
					case 3: whichSet.addAnswer("3", 1, "Richtig!");
							whichSet.addAnswer("1", 0, "Falsch. Die richtige Antwort waere 3");
							whichSet.addAnswer("2", 0, "Falsch. Die richtige Antwort waere 3");
							whichSet.addAnswer("0", 0, "Falsch. Die richtige Antwort waere 3");
							break;		
				}
				lang.addMCQuestion(whichSet);
				lang.nextStep();
			}
			if(i==3){
				whatIsLRU.setPrompt("Fuer was steht die Abkuerzung LRU ?");
				whatIsLRU.addAnswer("Least Recently Used", 1, "Richtig!");
				whatIsLRU.addAnswer("Last Recently Used", 0, "Falsch. Die richtige Antwort waere 'Least Recently Used'");
				whatIsLRU.addAnswer("Last Recently Unified", 0, "Falsch. Die richtige Antwort waere 'Least Recently Used'");
				whatIsLRU.addAnswer("Least Received Unit", 0, "Falsch. Die richtige Antwort waere 'Least Recently Used'");
				lang.addMCQuestion(whatIsLRU);
				lang.nextStep();
			}
			setElem(String.valueOf(value));
			if(i>0){
				insertArray.unhighlightElem(i-1, null, null);
				insertArray.highlightCell(i-1, null, null);
			}	
			insertArray.highlightElem(i, null, null);
			
			// Prüfen
			pseudoCode.highlight(0);
			pseudoCode.highlight(1);
			lang.nextStep("Einfuegen Wert " + value);
			pseudoCode.unhighlight(0);
			pseudoCode.unhighlight(1);
			if(cache.getElement(inSet, 0) == value || cache.getElement(inSet, 1) == value){
				pseudoCode.highlight(2);			// Wert ist im Cache
				updateLRU(value, cache, lruCol);	// LRU aktualisieren wenn nötig
			}else{
				pseudoCode.highlight(3);			// Wert NICHT im Cache
				lang.nextStep();
				pseudoCode.unhighlight(3);
				if(cache.getElement(inSet, 0) == 0 || cache.getElement(inSet, 1) == 0){
					pseudoCode.highlight(4);		// Weg im Cache frei
					pseudoCode.highlight(5);
				}else{	
					pseudoCode.highlight(6);		// kein Weg im Cache frei
					pseudoCode.highlight(7);
				}
				insert(value, cache, lruCol);		// neuen Wert einfügen
			}
		}
		
		
		// STEP 4
		// /////////////////////////////////////////////
		// Alle Werte abgearbeitet
				
		lang.nextStep();
		pseudoCode.unhighlight(2);
		pseudoCode.unhighlight(4);
		pseudoCode.unhighlight(5);
		pseudoCode.unhighlight(6);
		pseudoCode.unhighlight(7);
		clearElem();
		insertArray.unhighlightElem(i-1, null, null);
		insertArray.highlightCell(i-1, null, null);
			
		whyLRU.setPrompt("Was ist KEIN Vorteil von LRU als Verdraengungsstrategie ?");
		whyLRU.addAnswer("Ausnutzen zeitlicher Lokalitaet", 0, "Falsch. Das Ausnutzen zeitlicher und raeumlicher Lokalitaet sind Vorteile von LRU.");
		whyLRU.addAnswer("Ausnutzen raeumlicher Lokalitaet", 0, "Falsch. Das Ausnutzen zeitlicher und raeumlicher Lokalitaet sind Vorteile von LRU.");
		whyLRU.addAnswer("Ausnutzen guenstiger Lokale", 0, "Richtig! Dies ist kein Vorteil von LRU.");
		lang.addMCQuestion(whyLRU);
		lang.nextStep("Alle Zugriffe abgearbeitet.");
		
		// STEP 5
		// /////////////////////////////////////////////
		// Fazit
		
		lang.hideAllPrimitives();
		cache.hide();
		lruCol.hide();
		
		Text titleEnd = lang.newText(new Coordinates(30, 30), "2-Way Cache mit LRU Verdrängungsstrategie - Fazit",
				"titleEnd", null, headerProps);
		Rect rectBackgroundTitleEnd = lang.newRect(new Offset(-10, -10, titleEnd,
				AnimalScript.DIRECTION_NW), new Offset(10, 10, titleEnd,
				AnimalScript.DIRECTION_SE), "rectBackgroundTitleEnd", null,
				headerRectProps);
		SourceCode fazit = lang.newSourceCode(new Coordinates(30, 100),
				"fazit", null, pseudoCodeProps);

		setTextToSourceCode(fazit, FAZIT);
		lang.nextStep("Fazit");
	}

    public String getName() {
        return "2-Way Cache (LRU) [DE]";
    }

    public String getAlgorithmName() {
        return "2-Way Cache (LRU)";
    }

    public String getAnimationAuthor() {
        return "Christopher Ries";
    }

    public String getDescription(){
        return DESCRIPTION;
    }

    public String getCodeExample(){
         return "Pruefen ob sich die gelesene Speicheradresse im Cache befindet \n"
		+"\tWenn 'ja': LRU gegebenenfalls aktualisieren \n"
		+"\tWenn 'nein': Pruefen ob ein Weg im Set frei ist \n"
		+"\t\tWenn 'ja': Wert in freien Weg einfuegen und wenn noetig 'LRU' setzen \n"
		+"\t\tWenn 'nein': Ersetzte 'LRU'-Wert in Cache und aktualisiere 'LRU'";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

	private void setTextToSourceCode(SourceCode sourceCode, String text) {
		String[] lines = text.split("\n");
		for (String line : lines) {								// ForEach line
			int tabs = line.split("\t").length - 1;
			sourceCode.addCodeLine(line, null, tabs, null);
		}
	}
	
	// Anzeige für Element und Set
	private void setElem(String value){
		int intVal = Integer.parseInt(value);
		String setVal = String.valueOf(intVal%4);
		Text actElem  = lang.newText(new Offset(15, 0, insertElem, AnimalScript.DIRECTION_NE), value,  "Elem"+ value,  null, cacheHeaderProps);
		Text set = lang.newText(new Offset(-25, 10, modulo, AnimalScript.DIRECTION_SE), setVal,  "Elem"+ value,  null, cacheHeaderProps);
	}
	
	// Element und Set verdecken
	private void clearElem(){
		Rect rectElem = lang.newRect(new Offset(10, -5, insertElem, AnimalScript.DIRECTION_NE), new Offset(50, 25, insertElem, AnimalScript.DIRECTION_NE),
				"rectElem", null, rectElemProps);
		Rect rectSet = lang.newRect(new Offset(-30, 5, modulo, AnimalScript.DIRECTION_SE), new Offset(0, 35, modulo, AnimalScript.DIRECTION_SE),
				"rectSet", null, rectElemProps);
	}
	
	private void flash(int set, int way, IntMatrix mat2F){
		mat2F.highlightCell(set, way, null, null); 
		mat2F.unhighlightCell(set, way, longDur, null); 
	}
	
	private void updateLRU(int value, IntMatrix cache, IntMatrix lruCol){	
		int inSet = value%4;
		switch (inSet){
			case 0: if(cache.getElement(0,0) == value && cache.getElement(0,1) != lruCol.getElement(0,0)){		// Wert in Way 0, LRU nicht Wert in Way 1
						lruCol.put(0,0, cache.getElement(0,1), null, null);										// LRU aktualisieren
						flash(0,0, lruCol);
					}else{
						if(cache.getElement(0,1) == value && cache.getElement(0,0) != lruCol.getElement(0,0)){	// Wert in Way 1, LRU nicht Wert in Way 0
							lruCol.put(0,0, cache.getElement(0,0), null, null);									// LRU aktualisieren
							flash(0,0, lruCol);
						}
					}																							// sonst LRU schon richtig -> kein flash
					break;
			case 1: if(cache.getElement(1,0) == value && cache.getElement(1,1) != lruCol.getElement(1,0)){
						lruCol.put(1,0, cache.getElement(1,1), null, null);
						flash(1,0, lruCol);
					}else{
						if(cache.getElement(1,1) == value && cache.getElement(1,0) != lruCol.getElement(1,0)){
							lruCol.put(1,0, cache.getElement(1,0), null, null);
							flash(1,0, lruCol);
						}
					}
					break;
			case 2: if(cache.getElement(2,0) == value && cache.getElement(2,1) != lruCol.getElement(2,0)){
						lruCol.put(2,0, cache.getElement(2,1), null, null);
						flash(2,0, lruCol);
					}else{
						if(cache.getElement(2,1) == value && cache.getElement(2,0) != lruCol.getElement(2,0)){
							lruCol.put(2,0, cache.getElement(2,0), null, null);
							flash(2,0, lruCol);
						}
					}
					break;
			case 3: if(cache.getElement(3,0) == value && cache.getElement(3,1) != lruCol.getElement(3,0)){
						lruCol.put(3,0, cache.getElement(3,1), null, null);
						flash(3,0, lruCol);
					}else{
						if(cache.getElement(3,1) == value && cache.getElement(3,0) != lruCol.getElement(3,0)){
							lruCol.put(3,0, cache.getElement(3,0), null, null);
							flash(3,0, lruCol);
						}
					}
					break;
		}
	}
	
	
	private void insert(int value, IntMatrix cache, IntMatrix lruCol){
	
		int inSet = value%4;																		// in welches Set gehört der Wert
		if(cache.getElement(inSet, 0) == 0){														// Way 0 leer -> kein LRU setzen
			cache.put(inSet, 0, value, null, null);
			flash(inSet, 0, cache);
		}else{
			if(cache.getElement(inSet, 1) == 0){													// Way 1 leer
				cache.put(inSet, 1, value, null, null);
				flash(inSet, 1, cache);
				lruCol.put(inSet,0, cache.getElement(inSet, 0), null, null);						// LRU setzten
				flash(inSet, 0, lruCol);
			}else{																					// beide Wege voll
				switch (inSet){
					case 0: if(cache.getElement(0,0) == lruCol.getElement(0,0)){					// LRU in Way 0
								cache.put(0,0, value, null, null);									// neuen Wert in Way 0	
								lruCol.put(0,0, cache.getElement(0,1), null, null);					// LRU aktualisieren (jetzt Wert in Way 1)
								flash(0, 0, cache);													// neuen Wert flashen
							}else{
								cache.put(0,1, value, null, null);									// neuen Wert in Way 1
								lruCol.put(0,0, cache.getElement(0,0), null, null);					// LRU aktualisieren (jetzt Wert in Way 0)
								flash(0, 1, cache);													// neuen Wert flashen
							}
							flash(0, 0, lruCol);													// neuen LRU-Wert flashen
							break;
					case 1: if(cache.getElement(1,0) == value || cache.getElement(1,1) == value){
							}else{
								if(cache.getElement(1,0) == lruCol.getElement(1,0)){
									cache.put(1,0, value, null, null);
									lruCol.put(1,0, cache.getElement(1,1), null, null);
									flash(1, 0, cache);
								}else{
									cache.put(1,1, value, null, null);
									lruCol.put(1,0, cache.getElement(1,0), null, null);
									flash(1, 1, cache);
								}
								flash(0, 1, lruCol);
							}
							break;
					case 2: if(cache.getElement(2,0) == value || cache.getElement(2,1) == value){
							}else{
								if(cache.getElement(2,0) == lruCol.getElement(2,0)){
									cache.put(2,0, value, null, null);
									lruCol.put(2,0, cache.getElement(2,1), null, null);
									flash(2, 0, cache);
								}else{
									cache.put(2,1, value, null, null);
									lruCol.put(2,0, cache.getElement(2,0), null, null);
									flash(2, 1, cache);
								}
								flash(0, 2, lruCol);
							}						
							break;
					case 3: if(cache.getElement(3,0) == value || cache.getElement(3,1) == value){
							}else{
								if(cache.getElement(3,0) == lruCol.getElement(3,0)){
									cache.put(3,0, value, null, null);
									lruCol.put(3,0, cache.getElement(3,1), null, null);
									flash(3, 0, cache);
								}else{
									cache.put(3,1, value, null, null);
									lruCol.put(3,0, cache.getElement(3,0), null, null);
									flash(3, 1, cache);
								}
								flash(0, 3, lruCol);
							}	
							break;
				}
			}
		}	
	}
}