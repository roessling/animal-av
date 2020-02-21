/*
 * Belady.java
 * Ozan Agtas, Steffen Lott, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.hardware;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class Belady implements ValidatingGenerator {
   
	// AnimalScript Objekte
	private Language lang;
    private Translator transe;
    private SourceCode srcCode;
    
    // Variablen
    private ArrayList<Integer> pageTable;
	private int[] pageTableArray;
	private int[] sequence;
	private int pageTableMaxSize;
	
	// GUI 
	private IntArray sequenceDisplay;
	private IntArray pageTableDisplay;
	private ArrayMarker arrayMarker;
	private TwoValueCounter counterQueue;

	// Properties
	private SourceCodeProperties sourceCodeProps;
	private TextProperties headerProps;
	private RectProperties rectProps;
	private TextProperties textProps;
	private ArrayProperties arrayProps;
	private CounterProperties counterProps;
	
	public Belady(String path, Locale l) {
    	this.transe = new Translator(path, l);
    }

//	#-----------------------------------------------#
//	|				AnimalAPI Methoden				|
//	#-----------------------------------------------#
    
	public void init(){
        this.lang = new AnimalScript("Belady's Optimal Page Replacement", "Ozan Agtas, Steffen Lott", 800, 600);
        
		headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		
		counterProps = new CounterProperties();
		counterProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 14));
    }

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		int[] pageTableI = (int[])primitives.get("pageFrames");
	    this.sequence = (int[])primitives.get("AccessSequence");
	    this.pageTable = new ArrayList<Integer>(pageTableI.length);
	    this.pageTableMaxSize = pageTableI.length;
	    
	    // Sequence darf nur positive Zahlen haben
	    for (int i = 0; i < sequence.length; i++) {
			if (sequence[i] < 0) {
				return false;
			}
	    }
	    
	    pageTableArray = new int[this.pageTableMaxSize];
	    for (int i = 0; i < pageTableI.length; i++) {
			if (pageTableI[i] < -1) { 
				return false;
			}
			pageTableArray[i] = pageTableI[i];
			
			
			this.pageTable.add(pageTableI[i]);
		}
	    
	    // Properties
	    this.sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProperties");
	    this.rectProps = (RectProperties)props.getPropertiesByName("rectProperties");
	    this.textProps = (TextProperties)props.getPropertiesByName("textProperties");
	    this.arrayProps = (ArrayProperties)props.getPropertiesByName("arrayProperties");
	    return true;
	}

	@Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		this.lang.setStepMode(true);
		Node baseOffset = new Coordinates(10, 10);
		ArrayList<Primitive> gui = new ArrayList<Primitive>(5);		
		
		// Titel und Beschreibung
		Text header = this.lang.newText(baseOffset, this.getAlgorithmName(), "header", null, headerProps);
		gui.add(header);
		Rect headRect = this.lang.newRect(new Offset(-5, -5, baseOffset, AnimalScript.DIRECTION_NW), new Offset(5, 5, header, AnimalScript.DIRECTION_SE), "hRect", null, rectProps);
		gui.add(headRect);
		
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 18));
		
		// Einleitungsseite
		this.lang.newText(new Offset(0, 20, header, AnimalScript.DIRECTION_SW), transe.translateMessage("algoStep0"), "algoStep0", null, headerProps);
		this.lang.newText(new Offset(0, 20, "algoStep0", AnimalScript.DIRECTION_SW), transe.translateMessage("algoStep1"), "algoStep1", null, textProps);
		this.lang.newText(new Offset(0, 10, "algoStep1", AnimalScript.DIRECTION_SW), transe.translateMessage("algoStep2"), "algoStep2", null, textProps);
		this.lang.newText(new Offset(0, 10, "algoStep2", AnimalScript.DIRECTION_SW), transe.translateMessage("algoStep3"), "algoStep3", null, textProps);
		this.lang.newText(new Offset(0, 10, "algoStep3", AnimalScript.DIRECTION_SW), transe.translateMessage("algoStep4"), "algoStep4", null, textProps);
		this.lang.newText(new Offset(0, 10, "algoStep4", AnimalScript.DIRECTION_SW), transe.translateMessage("algoStep5"), "algoStep5", null, textProps);
		
		// AlgoMeta
		this.lang.newText(new Offset(0, 50, "algoStep4", AnimalScript.DIRECTION_SW), transe.translateMessage("algoMeta1"), "algoMeta1", null, textProps);
		this.lang.newText(new Offset(0, 10, "algoMeta1", AnimalScript.DIRECTION_SW), transe.translateMessage("algoMeta2"), "algoMeta2", null, textProps);
		
		
		this.lang.nextStep();
		this.lang.hideAllPrimitivesExcept(gui);
		
		// SourceCode erstellen
		srcCode = this.lang.newSourceCode(new Offset(600, 0, baseOffset, AnimalScript.DIRECTION_C), "srcCode", null, sourceCodeProps);
		generateSourcecode();
		
		// Array und ArrayMarker fuer Sequence erstellen
		Text ptLabel = this.lang.newText(new Offset(0, 30, header, AnimalScript.DIRECTION_SW), "Sequence", "seqLabel", null, headerProps);
		sequenceDisplay = this.lang.newIntArray(new Offset(0, 50, ptLabel, AnimalScript.DIRECTION_SW), sequence, "sequence", null, arrayProps);
		arrayMarker = this.lang.newArrayMarker(sequenceDisplay, 0, "seqPointer", null);
		
		// Hit- und Misscounter + Text erstellen
		this.lang.newText(new Offset(0, 50, sequenceDisplay, AnimalScript.DIRECTION_SW), "Hit", "hitLabel", null, textProps);
		this.lang.newText(new Offset(0, 5, "hitLabel", AnimalScript.DIRECTION_SW), "Miss", "MissLabel", null, textProps);
		counterQueue = this.lang.newCounter(sequenceDisplay);		
		counterQueue.activateCounting();
		TwoValueView viewQ = this.lang.newCounterView(counterQueue, new Offset(-30, 0, "hitLabel", AnimalScript.DIRECTION_NE), counterProps, true, true);
		viewQ.hideText();
		
		// Array fuer PageTable erstellen
		this.lang.newText(new Offset(0, 20, "MissLabel", AnimalScript.DIRECTION_SW), "Pagetable", "ptLabel", null, headerProps);
		pageTableDisplay = this.lang.newIntArray(new Offset(0, 10, "ptLabel", AnimalScript.DIRECTION_SW), pageTableArray, "pt", null, arrayProps);
		
		
		belady();
		
		this.lang.nextStep("Summary");
		this.lang.hideAllPrimitivesExcept(gui);
		
		this.lang.newText(new Offset(0, 20, header, AnimalScript.DIRECTION_SW), transe.translateMessage("algoSumm0"), "algoSumm0", null, headerProps);
		this.lang.newText(new Offset(0, 20, "algoSumm0", AnimalScript.DIRECTION_SW), transe.translateMessage("algoSumm1"), "algoSumm1", null, textProps);
		this.lang.newText(new Offset(0, 10, "algoSumm1", AnimalScript.DIRECTION_SW), transe.translateMessage("algoSumm2"), "algoSumm2", null, textProps);
		
		this.lang.finalizeGeneration();
		return this.lang.toString();
	}

//	#-----------------------------------------------#
//	|				Belady Algorithmus				|
//	#-----------------------------------------------#
	
	void belady() 
	{
		/*
		 * counterQueue assignMents = hit Counter
		 * counterQueue accessInc = miss Counter 
		 */

		for (int index = 0; index < sequence.length; index++) {
			arrayMarker.move(index, null, null);
			int requestedPage = sequence[index];
			toggle_PageHit(false);
			
			lang.nextStep("checkForHit_"+Integer.toString(index)); toggle_checkForHit(true);
			// Check, obs in den Pageframes bereits vorhanden ist:
			if (pageTable.contains(requestedPage)) {
				// Page HIT:
				// Animal Animationsteps:
				sequenceDisplay.highlightCell(index, null, null);
				pageTableDisplay.highlightCell(pageTable.indexOf(requestedPage), null, null);
				lang.nextStep();
				toggle_checkForHit(false); toggle_PageHit(true);
				counterQueue.assignmentsInc(1);
				lang.nextStep();
				sequenceDisplay.unhighlightCell(index, null, null);
				pageTableDisplay.unhighlightCell(pageTable.indexOf(requestedPage), null, null);
				// Actual Algorithmsteps:
				continue;
			}
			// Page MISS:
			lang.nextStep("pageMiss_"+Integer.toString(index));
			toggle_checkForHit(false); toggle_checkPageTableEmpty(true);
			counterQueue.accessInc(1);
			
			
			if (pageTable.contains(-1)) {
				
				// Noch Platz in der Pageframe vorhanden:
				int indexToSet = pageTable.size();
				if (pageTable.indexOf(-1) != -1) {
					indexToSet = pageTable.indexOf(-1);
				}
				
				lang.nextStep("pageTableIsEmpty_"+Integer.toString(index));
				toggle_checkPageTableEmpty(false); toggle_pageTableEmpty(true);
				
				pageTableDisplay.highlightCell(indexToSet, null, null);
				pageTableDisplay.put(indexToSet, requestedPage, null, null);
				pageTable.set(indexToSet, requestedPage);
				
				
				lang.nextStep();
				pageTableDisplay.unhighlightCell(0, 2, null, null);
				toggle_pageTableEmpty(false);
				continue;
			}
			else {
				lang.nextStep("replacePage_start_"+Integer.toString(index));
				toggle_checkPageTableEmpty(false); toggle_FindPageToReplace(true);
				ArrayList<Text> texts = new ArrayList<Text>();
				
				// Eine Page im Speicher muss ersetzt werden:
				int res = -1, farthest = index; 
				for (int i = 0; i < pageTable.size(); i++) { 
					pageTableDisplay.highlightElem(i, null, null);
					int j; 
					for (j = index; j < sequence.length; j++) {
						if (pageTable.get(i) == sequence[j]) {
							if (j > farthest) { 
								farthest = j; 
								res = i; 
							}
							break; 
						}
					}
					
					int nextUse = j - index;
					boolean notUsed = false;
					// Die erste Page, die nie in der Zukunft verwendet wird, wird ersetzt. 
					if (j == sequence.length) {
						lang.nextStep("PageNotUsedInFuture"+Integer.toString(index));
						toggle_FindPageToReplace(false); toggle_NotUsedInFuture(true);
						res = i;
						texts.add(this.lang.newText(new Offset(5, 20+i*20, pageTableDisplay, AnimalScript.DIRECTION_SW), 
								transe.translateMessage("nextUseNever", new Object[] {pageTable.get(i)} ),"nextUse", null));
						
						notUsed = true;
						
					} else {
						texts.add(this.lang.newText(new Offset(5, 20+i*20, pageTableDisplay, AnimalScript.DIRECTION_SW), 
								transe.translateMessage("nextUseSteps", new Object[] {pageTable.get(i), nextUse} ), "nextUse", null));

					}
					
					lang.nextStep();
					pageTableDisplay.unhighlightElem(i, null, null);
					if (notUsed) break;
				}
				
				pageTableDisplay.highlightCell(res, null, null);
				toggle_NotUsedInFuture(false); toggle_FindPageToReplace(false); toggle_pageTableEmpty(false); 
				srcCode.highlight("setpageTable");
				lang.nextStep("PageReplace_"+Integer.toString(index));
				// Page ersetzten
				
				pageTableDisplay.put(res, requestedPage, null, null);
				
				pageTable.set(res, requestedPage);
				pageTableDisplay.unhighlightCell(res, null, null);
				srcCode.unhighlight("setpageTable");
				for (Text t : texts) {
					t.hide();
				}
			}
		}
	}
	
//	#-----------------------------------------------#
//	|			AnimalScript Hilfsmethoden			|
//	#-----------------------------------------------#

	void generateSourcecode() {
		srcCode.addCodeLine("void belady(Page[] sequence, int currentIndex, List<Page> pageTable)", null, 0, null); 	// 00
		srcCode.addCodeLine("{", null, 0, null); 																		// 01
		srcCode.addCodeLine("Page requestedPage = sequence[currentIndex];", null, 1, null);								// 02
		srcCode.addCodeLine("// "+ transe.translateMessage("commentPTExist") , null, 1, null);							// 03
		srcCode.addCodeLine("if (pageTable.contains(requestedPage)) {", null, 1, null);									// 04
		srcCode.addCodeLine("// Page HIT: "+ transe.translateMessage("pageHit"),null, 2, null);							// 05
		srcCode.addCodeLine("return;", null, 2, null);																	// 06
		srcCode.addCodeLine("}", null, 1, null);																		// 07
		srcCode.addCodeLine("// Page MISS:", null, 1, null);															// 08
		srcCode.addCodeLine("if (pageTable.size() < pageTableMaxSize) {", null, 1, null);								// 09
		srcCode.addCodeLine("// "+ transe.translateMessage("ptEmpty"), null, 2, null);									// 10
		srcCode.addCodeLine("pageTable.add(requestedPage); return;", null, 2, null);									// 11
		srcCode.addCodeLine("}", null, 1, null);																		// 12
		srcCode.addCodeLine("else {", null, 1, null);																	// 13
		srcCode.addCodeLine("// "+ transe.translateMessage("ptNotEmpty"), null, 2, null);								// 14
		srcCode.addCodeLine("int indexToReplace = -1, farthest = currentIndex;", null, 2, null);							// 15
		srcCode.addCodeLine("for (int i = 0; i < pageTable.size(); i++) { ", null, 2, null);							// 16
		srcCode.addCodeLine("int j;", null, 3, null);																	// 17
		srcCode.addCodeLine("for (j = currentIndex; j < sequence.length; j++) {", null, 3, null);							// 18
		srcCode.addCodeLine("if (pageTable.get(i) == sequence[j]) {", null, 4, null);									// 19
		srcCode.addCodeLine("if (j > farthest) { ", null, 5, null);														// 20
		srcCode.addCodeLine("farthest = j; indexToReplace = i;", null, 6, null);										// 21
		srcCode.addCodeLine("}", null, 5, null);																		// 22
		srcCode.addCodeLine("break;", null, 5, null);																	// 23
		srcCode.addCodeLine("}", null, 4, null);																		// 24
		srcCode.addCodeLine("}", null, 3, null);																		// 25
		srcCode.addCodeLine("// "+ transe.translateMessage("pageNotUsedInFuture"), null, 3, null);						// 26
		srcCode.addCodeLine("if (j == sequence.length) {", null, 3, null);												// 27
		srcCode.addCodeLine("indexToReplace = i;", null, 4, null);														// 28
		srcCode.addCodeLine("break;", null, 4, null);																	// 29
		srcCode.addCodeLine("}", null, 3, null);																		// 30
		srcCode.addCodeLine("}", null, 2, null);																		// 31
		srcCode.addCodeLine("}", null, 1, null);																		// 32
		srcCode.addCodeLine("// "+ transe.translateMessage("replacePage"), null, 1, null);								// 33
		srcCode.addCodeLine("pageTable.set(indexToReplace, requestedPage);", null, 1, null);							// 34
		srcCode.addCodeLine("}", null, 0, null);																		// 35
		
		srcCode.registerLabel("start", 2);
		srcCode.registerLabel("lookForPage", 4);
		srcCode.registerLabel("checkForEmptyFrame", 9);
		srcCode.registerLabel("EmptyFrameAvailable", 11);
		srcCode.registerLabel("startPageReplacementLookup", 13);
		srcCode.registerLabel("pageNotUsedInFuture", 27);
		srcCode.registerLabel("setpageTable", 34);
	}

	void toggle_checkForHit(boolean enable) {
		if (enable) {
			srcCode.highlight(3);
			srcCode.highlight(4);
		}
		else {
			srcCode.unhighlight(3);
			srcCode.unhighlight(4);
		}
	}
	void toggle_PageHit(boolean enable) {
		if (enable) {
			srcCode.highlight(5);
			srcCode.highlight(6);
		} else {
			srcCode.unhighlight(5);
			srcCode.unhighlight(6);
		}
	}
	void toggle_checkPageTableEmpty(boolean enable) {
		if (enable) {
			srcCode.highlight(8);
			srcCode.highlight(9);
		} 
		else {
			srcCode.unhighlight(8);
			srcCode.unhighlight(9);
		}
	}
	void toggle_pageTableEmpty(boolean enable) {
		if (enable) {
			srcCode.highlight(10);
			srcCode.highlight(11);
		} 
		else {
			srcCode.unhighlight(10);
			srcCode.unhighlight(11);
		}
	}
	void toggle_FindPageToReplace(boolean enable) {
		if (enable) {
			srcCode.highlight(14);
			srcCode.highlight(15);
			srcCode.highlight(16);
			srcCode.highlight(17);
			srcCode.highlight(18);
			srcCode.highlight(19);
			srcCode.highlight(20);
			srcCode.highlight(21);
			srcCode.highlight(22);
			srcCode.highlight(23);
			srcCode.highlight(24);
			srcCode.highlight(25);
		} 
		else {
			srcCode.unhighlight(14);
			srcCode.unhighlight(15);
			srcCode.unhighlight(16);
			srcCode.unhighlight(17);
			srcCode.unhighlight(18);
			srcCode.unhighlight(19);
			srcCode.unhighlight(20);
			srcCode.unhighlight(21);
			srcCode.unhighlight(22);
			srcCode.unhighlight(23);
			srcCode.unhighlight(24);
			srcCode.unhighlight(25);
		}
	}
	void toggle_NotUsedInFuture(boolean enable) {
		if (enable) {
			srcCode.highlight(26);
			srcCode.highlight(27);
			srcCode.highlight(28);
			srcCode.highlight(29);
		} 
		else {
			srcCode.unhighlight(26);
			srcCode.unhighlight(27);
			srcCode.unhighlight(28);
			srcCode.unhighlight(29);
		}
	}

//	#-----------------------------------------------#
//	|					Metadaten					|
//	#-----------------------------------------------#

    public String getName() {
        return "Belady's Optimal Page Replacement (OPT)";
    }

    public String getAlgorithmName() {
        return "Optimal Page Replacement";
    }

    public String getAnimationAuthor() {
        return "Ozan Agtas, Steffen Lott";
    }

    public String getDescription(){
        return transe.translateMessage("description"); 
    }

    public String getCodeExample(){
        return "	void belady(Page[] sequence, int currentIndex, List<Page> pageTable) \r\n" + 
        		"	{\r\n" + 
        		"		Page requestedPage = sequence[currentIndex];\r\n" + 
        		"		\r\n" + 
        		"		// " + transe.translateMessage("commentPTExist") + "\r\n" + 
        		"		if (pageTable.contains(requestedPage)) {\r\n" + 
        		"			// Page HIT: "+ transe.translateMessage("pageHit") +"\r\n" + 
        		"			return;\r\n" + 
        		"		}\r\n" + 
        		"		// Page MISS:\r\n" + 
        		"		if (pageTable.size() < pageTableMaxSize) {\r\n" + 
        		"			// "+ transe.translateMessage("ptEmpty")  +"\r\n" + 
        		"			pageTable.add(requestedPage);\r\n" + 
        		"			return;\r\n" + 
        		"		}\r\n" + 
        		"		else {\r\n" + 
        		"			// "+transe.translateMessage("ptNotEmpty")+"\r\n" + 
        		"			int indexToReplace = -1, farthest = currentIndex; \r\n" + 
        		"			for (int i = 0; i < pageTable.size(); i++) { \r\n" + 
        		"				int j; \r\n" + 
        		"				for (j = currentIndex; j < sequence.length; j++) {\r\n" + 
        		"					if (pageTable.get(i) == sequence[j]) {\r\n" + 
        		"						if (j > farthest) { \r\n" + 
        		"							farthest = j; \r\n" + 
        		"							indexToReplace = i; \r\n" + 
        		"						} \r\n" + 
        		"						break; \r\n" + 
        		"					} \r\n" + 
        		"				}\r\n" + 
        		"				// "+ transe.translateMessage("pageNotUsedInFuture") +"\r\n" + 
        		"				if (j == sequence.length) {\r\n" + 
        		"					indexToReplace = i;\r\n" + 
        		"					break;\r\n" + 
        		"				}\r\n" + 
        		"			}	\r\n" + 
        		"		}\r\n" + 
        		"		\r\n" + 
        		"		// "+ transe.translateMessage("replacePage") +"\r\n" + 
        		"		pageTable.set(indexToReplace, requestedPage);\r\n" + 
        		"	}";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return transe.getCurrentLocale();
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_HARDWARE);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

}