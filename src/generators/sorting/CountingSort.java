package generators.sorting;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Hidden;
import algoanim.util.Offset;

/**
 * @author Jan Stolzenburg <jan.stolzenburg@arcor.de>
 */
public class CountingSort implements generators.framework.Generator {
	
	private Language language;
	private int[] sourceArray = new int[]{2, 7, 1, 8, 2, 8, 1, 8, 2, 8, 4, 5, 9, 0, 5};
	private int minimum = 0; //Meant as "inclusive this value"
	private int maximum = 9; //Meant as "inclusive this value"
	private SourceCode pseudoCodeText;
	private int windowSizeX = 1000;
	private int windowSizeY = 600;
	private boolean useCodeMarker;
	private String codeMarker = "-->";
	private Color textHighlightColor;
	private Color codeHighlightColor;
	private Color arrayCellHighlightColor;
	private Color arrayElementHighlightColor;
	private Text[] highlightArrows;
	private int currentHighlight;
	private IntArray visualSourceArray;
	private IntArray visualCounterArray;
	private IntArray visualTargetArray;
	private Text visualMinimum;
	private Text visualMaximum;
	private ArrayProperties sourceArrayProperties;
	private ArrayProperties counterArrayProperties;
	private ArrayProperties targetArrayProperties;
	private TextProperties normalTextProperties;
	private static final String[] pseudoCodeString = new String[]{
		"Ermittle den Wertebereich der zu sortierenden Zahlen, falls er nicht vorgegeben ist.",
		"Erzeuge eine neue Liste('Zählliste'), die einen Eintrag für jedes Element des Wertebereiches hat.",
			"Initialisiere alle Elemente der Zählliste mit 0.",
		"Für jedes Element der zu sortierenden Liste:",
			"Erhöhe den entsprechenden Eintrag in der Zählliste um eins.",
		"Für jedes Element der Zählliste, beginnend von vorne:",
			"Ersetze das Element durch die Summe des aktuellen und des vorherigen Elementes.",
		"Erzeuge eine neue Liste von der Größe der zu sortierenden Liste, die Zielliste.",
		"Für jedes Element der zu sortierenden Liste, beginnend von hinten:", //Von hinten, damit das Verfahren stabil ist.
			"Interpretiere den Betrag als Index für die Zählliste.",
			"Der dort gespeicherte Wert ist wiederum der Index für die Zielliste. (Indexiert beginnend bei 1.)",
			"Kopiere das aktuelle Element der zu sortierenden Liste an die ermittelte Stelle der Zielliste.",
			"Erniedrige den Wert in der Zählliste um eins.",
	};
	private static final int[] pseudoCodeIndention = new int[]{0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1};
	
	public CountingSort() {
		super();
		if (pseudoCodeString.length != pseudoCodeIndention.length)
			throw new RuntimeException("The sizes of the pseudo code and its indention are unequal!");
	}
	
	public String generate() {
		this.language = new AnimalScript("Counting-Sort", "Jan Stolzenburg <jan.stolzenburg@arcor.de>", this.getWindowSizeX(), this.getWindowSizeY());
		this.language.setStepMode(true);
		this.makeAnimation();
		return this.language.toString();
	}
	
	private void makeAnimation() {
		this.makeHeader();
		this.language.nextStep();
		this.makeCode();
		this.language.nextStep();
		this.makeSort();
	}
	
	private void makeHeader() {
		TextProperties titleProperties = new TextProperties();
		titleProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		titleProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
		Text title = this.language.newText(new Coordinates(20, 30), "Counting Sort", "title", null, titleProperties);
		
		RectProperties titleBoxProperties = new RectProperties();
		titleBoxProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		titleBoxProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		titleBoxProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		titleBoxProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		this.language.newRect(new Offset(-5, -5, title, AnimalScript.DIRECTION_NW), new Offset(5, 5, title, AnimalScript.DIRECTION_SE), "titleBox", null, titleBoxProperties);
	}
	
	private void makeCode() {
		SourceCodeProperties pseudoCodeTextProperties = new SourceCodeProperties();
		pseudoCodeTextProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		pseudoCodeTextProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, this.codeHighlightColor);
		pseudoCodeTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 16));
		this.pseudoCodeText = this.language.newSourceCode(new Coordinates(30, 80), "Counting-Sort Pseude Code", null, pseudoCodeTextProperties);
		this.highlightArrows = new Text[pseudoCodeString.length];
		if (! this.useCodeMarker)
			this.codeMarker = "";
		for (int i = 0; i < pseudoCodeString.length; i++) {
			this.pseudoCodeText.addCodeLine(pseudoCodeString[i], "line" + String.valueOf(i), pseudoCodeIndention[i], null);
			this.highlightArrows[i] = this.language.newText(new Coordinates(5, 94 + (20 * i)), this.codeMarker, "arrowCode"   + String.valueOf(i), new Hidden());
		}
	}
	
	private void prepareSort() {
		this.sourceArrayProperties = new ArrayProperties();
		this.sourceArrayProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, this.arrayCellHighlightColor);
		this.sourceArrayProperties.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, this.arrayElementHighlightColor);
		this.sourceArrayProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		this.sourceArrayProperties.set(AnimationPropertiesKeys.DIRECTION_PROPERTY, true); //vertical and not horizontal
		this.counterArrayProperties = new ArrayProperties();
		this.counterArrayProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, this.arrayCellHighlightColor);
		this.counterArrayProperties.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, this.arrayElementHighlightColor);
		this.counterArrayProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		this.counterArrayProperties.set(AnimationPropertiesKeys.DIRECTION_PROPERTY, true);
		this.targetArrayProperties = new ArrayProperties();
		this.targetArrayProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, this.arrayCellHighlightColor);
		this.targetArrayProperties.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, this.arrayElementHighlightColor);
		this.targetArrayProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		this.targetArrayProperties.set(AnimationPropertiesKeys.DIRECTION_PROPERTY, true);
		this.normalTextProperties = new TextProperties();
		this.normalTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
	}
	
	private void makeSort() {
		this.prepareSort();
		this.showListAndRange();
		this.language.nextStep();
		this.showCounterList();
		this.language.nextStep();
		this.showCount();
		this.language.nextStep();
		this.showSummation();
		this.language.nextStep();
		this.showTargetList();
		this.language.nextStep();
		this.showFillTargetList();
	}
	
	private void showListAndRange() {
		this.visualSourceArray = this.language.newIntArray(new Coordinates(30, 450), this.sourceArray, "sourceArray", null, this.sourceArrayProperties);
		this.language.nextStep();
		this.highlightPseudoCode(0);
		this.language.newText(new Offset(0, -75, this.visualSourceArray, AnimalScript.DIRECTION_NW), "Gegeben:", "given", null, this.normalTextProperties);
		this.visualMinimum = this.language.newText(new Offset(0, -50, this.visualSourceArray, AnimalScript.DIRECTION_NW), "  Minimum: " + String.valueOf(this.minimum), "minimum", null, this.normalTextProperties);
		this.visualMaximum = this.language.newText(new Offset(0, -25, this.visualSourceArray, AnimalScript.DIRECTION_NW), "  Maximum: " + String.valueOf(this.maximum), "maximum", null, this.normalTextProperties);
	}
	
	private void showCounterList() {
		this.highlightPseudoCode(1);
		this.visualCounterArray = this.language.newIntArray(new Offset(50, 0, this.visualSourceArray, AnimalScript.DIRECTION_NE), new int[1 + this.maximum - this.minimum], "counterArray", null, this.counterArrayProperties);
		this.language.nextStep();
		this.visualMinimum.changeColor(AnimalScript.COLORCHANGE_COLOR, this.textHighlightColor, null, null);
		this.visualCounterArray.highlightCell(0, null, null);
		this.language.nextStep();
		this.visualMinimum.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);
		this.visualCounterArray.unhighlightCell(0, null, null);
		
		this.visualMaximum.changeColor(AnimalScript.COLORCHANGE_COLOR, this.textHighlightColor, null, null);
		this.visualCounterArray.highlightCell(this.visualCounterArray.getLength() - 1, null, null);
		this.language.nextStep();
		this.visualMaximum.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null, null);
		this.visualCounterArray.unhighlightCell(this.visualCounterArray.getLength() - 1, null, null);
		this.highlightPseudoCode(2, false);
	}
	
	private void showCount() {
		this.highlightPseudoCode(3);
		int counterIndex;
		for (int i = 0; i < this.visualSourceArray.getLength(); i++) {
			this.visualSourceArray.highlightCell(i, null, null);
			this.visualSourceArray.highlightElem(i, null, null);
			counterIndex = this.visualSourceArray.getData(i) - this.minimum;
			this.language.nextStep();
			this.highlightPseudoCode(4, false);
			this.visualCounterArray.highlightCell(counterIndex, null, null);
			this.visualCounterArray.highlightElem(counterIndex, null, null);
			this.language.nextStep();
			this.visualCounterArray.put(counterIndex, 1 + this.visualCounterArray.getData(counterIndex), null, null);
			this.visualCounterArray.highlightElem(counterIndex, null, null); //A workaround for a bug: If we change the value, it is beeing unhighlighted.
			this.language.nextStep();
			this.visualSourceArray.unhighlightCell(i, null, null);
			this.visualSourceArray.unhighlightElem(i, null, null);
			this.visualCounterArray.unhighlightCell(counterIndex, null, null);
			this.visualCounterArray.unhighlightElem(counterIndex, null, null);		
		}
		for (int i = 0; i < maximum; i++) {
		  CheckpointUtils.checkpointEvent(this, "visual", new Variable("counterArray",visualCounterArray.getData(i)));///////////////////
		}
	}
	
	private void showSummation() {
		this.highlightPseudoCode(5);
		for (int i = 1; i < this.visualCounterArray.getLength(); i++) { //We start with 1 not 0, as we sum up the current element and its predecessor. 0 has none.
			this.visualCounterArray.highlightCell(i, null, null);
			this.visualCounterArray.highlightElem(i, null, null);
			this.highlightPseudoCode(6);
			this.visualCounterArray.highlightCell(i - 1, null, null);
			this.visualCounterArray.highlightElem(i - 1, null, null);
			this.language.nextStep();
			this.visualCounterArray.put(i, this.visualCounterArray.getData(i - 1) + this.visualCounterArray.getData(i), null, null);
			this.visualCounterArray.highlightElem(i, null, null); //A workaround for a bug: If we change the value, it is beeing unhighlighted.
			this.language.nextStep();
			this.visualCounterArray.unhighlightCell(i - 1, null, null);
			this.visualCounterArray.unhighlightElem(i - 1, null, null);
			this.visualCounterArray.unhighlightCell(i, null, null);
			this.visualCounterArray.unhighlightElem(i, null, null);
		}
		for (int i = 0; i < this.visualCounterArray.getLength(); i++) {
		  CheckpointUtils.checkpointEvent(this, "visual", new Variable("sumArray",visualCounterArray.getData(i)));////////////
		}
	}
	
	private void showTargetList() {
		this.highlightPseudoCode(7);
		this.visualTargetArray = this.language.newIntArray(new Offset(50, 0, this.visualCounterArray, AnimalScript.DIRECTION_NE), new int[this.visualSourceArray.getLength()], "targetArray", null, this.targetArrayProperties);
	}
	
	private void showFillTargetList() {
		int counterIndex, targetIndex;
		this.highlightPseudoCode(8);
		for (int i = this.visualSourceArray.getLength() - 1; i >= 0; i--) {
			this.visualSourceArray.highlightCell(i, null, null);
			this.visualSourceArray.highlightElem(i, null, null);
			counterIndex = this.visualSourceArray.getData(i) - this.minimum;
			CheckpointUtils.checkpointEvent(this, "count", new Variable("wert",visualCounterArray.getData(counterIndex)));
			
			this.language.nextStep();
			this.highlightPseudoCode(9, false);
			this.visualCounterArray.highlightCell(counterIndex, null, null);
			this.visualCounterArray.highlightElem(counterIndex, null, null);
			
			targetIndex = this.visualCounterArray.getData(counterIndex) - 1;
			this.language.nextStep();//targetIndex
			this.highlightPseudoCode(10);
			this.visualTargetArray.highlightCell(targetIndex, null, null);
			this.visualTargetArray.highlightElem(targetIndex, null, null);
			this.language.nextStep();
			this.highlightPseudoCode(11);
			CheckpointUtils.checkpointEvent(this, "sort", new Variable("sourceWert",visualSourceArray.getData(i)), new Variable ("index1",targetIndex));
			this.visualTargetArray.put(targetIndex, this.visualSourceArray.getData(i), null, null);
			this.visualTargetArray.highlightElem(targetIndex, null, null); //A workaround for a bug: If we change the value, it is beeing unhighlighted.
			this.language.nextStep();
			this.visualTargetArray.unhighlightCell(targetIndex, null, null);
			this.visualTargetArray.unhighlightElem(targetIndex, null, null);
			this.visualSourceArray.unhighlightCell(i, null, null);
			this.visualSourceArray.unhighlightElem(i, null, null);
			this.highlightPseudoCode(12);		
			this.visualCounterArray.put(counterIndex, this.visualCounterArray.getData(counterIndex) - 1, null, null);
			
			this.visualCounterArray.highlightElem(counterIndex, null, null); //A workaround for a bug: If we change the value, it is beeing unhighlighted.
			this.language.nextStep();
			this.visualCounterArray.unhighlightCell(counterIndex, null, null);
			this.visualCounterArray.unhighlightElem(counterIndex, null, null);
			
		}
		for (int i = 0; i < this.visualSourceArray.getLength(); i++) {
		  CheckpointUtils.checkpointEvent(this, "visual", new Variable("targetArray",visualTargetArray.getData(i)));////////////
		}
	}
	
	private void highlightPseudoCode(int rowIndex, boolean nextStep) {
		this.pseudoCodeText.unhighlight(this.currentHighlight);
		this.pseudoCodeText.highlight(rowIndex);
		this.highlightArrows[this.currentHighlight].hide();
		this.highlightArrows[rowIndex].show();
		this.currentHighlight = rowIndex;
		if (nextStep)
			this.language.nextStep();
	}
	
	private void highlightPseudoCode(int rowIndex) {
		this.highlightPseudoCode(rowIndex, true);
	}
	
//	 //Just for debugging purpose.
//	private void makeWindowBorder() {
//		RectProperties titleBoxProperties = new RectProperties();
//		titleBoxProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
//		titleBoxProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
//		this.language.newRect(new Coordinates(0, 0), new Coordinates(this.getWindowSizeX(), this.getWindowSizeY()), "titleBox", null, titleBoxProperties);
//	}
	
	public String generate(AnimationPropertiesContainer properties, Hashtable<String, Object> primitives) {
		String nameSourceArray        = "Liste der zu sortierenden Zahlen.",                     errorSourceArray = "Source array is not specified!";
		String nameMaximum            = "Groesste Zahl, die in der Liste vorkommen kann.",       errorMaximum = "Greatest possible number is not specified!";
		String nameMinimum            = "Kleinste Zahl, die in der Liste vorkommen kann.",       errorMinimum = "Smallest possible number is not specified!";
		String nameUseCodeMarker      = "Die aktuelle Codezeile mit einem Pfeil markieren?",     errorUseCodeMarker = "If a codemarker should be used, is not specified!";
		String nameTextHighlightColor = "Farbe zum Hervorheben von Text.",                      errorTextHighlightColor = "The color for highlighting text is not specified!";
		String nameCodeHighlightColor = "Farbe zum Hervorheben von Pseudocode.",                errorCodeHighlightColor = "The color for highlighting code is not specified!";
		String nameCellHighlightColor = "Hintergrundfarbe zum Hervorheben von Text in Listen.", errorCellHighlightColor = "The background color for highlighted text in lists is not specified!";
		String nameElemHighlightColor = "Farbe zum Hervorheben von Text in Listen.",            errorElemHighlightColor = "The color for highlighted text in lists is not specified!";
		
		this.sourceArray                = (int[])  getParameter(primitives, nameSourceArray,        errorSourceArray);
		this.maximum                    = (Integer)getParameter(primitives, nameMaximum,            errorMaximum);
		this.minimum                    = (Integer)getParameter(primitives, nameMinimum,            errorMinimum);
		this.useCodeMarker              = (Boolean)getParameter(primitives, nameUseCodeMarker,      errorUseCodeMarker);
		this.textHighlightColor         = (Color)  getParameter(primitives, nameTextHighlightColor, errorTextHighlightColor);
		this.codeHighlightColor         = (Color)  getParameter(primitives, nameCodeHighlightColor, errorCodeHighlightColor);
		this.arrayCellHighlightColor    = (Color)  getParameter(primitives, nameCellHighlightColor, errorCellHighlightColor);
		this.arrayElementHighlightColor = (Color)  getParameter(primitives, nameElemHighlightColor, errorElemHighlightColor);
		
		if (this.sourceArray.length == 0) {
			this.sourceArray = new int[]{0};
			System.err.println("The list is empty. I added a zero to prevent trouble!");
		}
		for (int elem : this.sourceArray) {
			if (elem < this.minimum) {
				this.minimum = elem;
				System.err.println("There is at least one element in the list that is smaller than the lower bound!\nI corrected the lower bound.");
			}
			if (elem > this.maximum) {
				this.maximum = elem;
				System.err.println("There is at least one element in the list that is greater than the upper bound!\nI corrected the upper bound.");
			}
		}
		
		return this.generate();
	}
	
	private Object getParameter(Hashtable<String, Object> values, String key, String errorMessage) {
		if ((! values.containsKey(key)) || (values.get(key) == null)) {
			throw new RuntimeException(errorMessage);
		}
		return values.get(key);
	}
	
	private int getWindowSizeX() {
		return this.windowSizeX;
	}
	
	private int getWindowSizeY() {
		return this.windowSizeY;
	}
	
	public String getCodeExample() {
		String result = "";
		for (int i = 0; i < pseudoCodeString.length; i++) {
			for (int j = 0; j < pseudoCodeIndention[i]; j++)
				result += "  ";
			result += "- ";
			result += (pseudoCodeString[i] + "\n");
		}
		return result;
	}
	
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}
	
	public String getDescription() {
		return "Erkl&auml;rt und veranschaulicht Counting-Sort.";
	}
	
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}
	
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}
	
	public String getName() {
		return "Das Sortierverfahren Counting-Sort";
	}
	
	public String getAlgorithmName() {
		return "Counting Sort";
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

  public String getAnimationAuthor() {
    return "Jan Stolzenburg";
  }  
  public void init() {
    // nothing to be done here
  }

	
}
