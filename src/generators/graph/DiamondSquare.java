package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class DiamondSquare implements Generator, ValidatingGenerator {
    private Language lang;
    private TextProperties headerProperties;
    private SourceCodeProperties introTextProperties;
    private MatrixProperties matrixProperties;
    private RectProperties rectangleProperties;
    private SourceCodeProperties stepsTextProperties;
    private Text stepsHeadline;
    private SourceCode introductionText;
    private SourceCode stepsText;
    private IntMatrix intMatrix;
    private TwoValueCounter counter;
    private CounterProperties counterProperties;
    private TwoValueView counterView;
    private MatrixProperties coordsProperties;
    private IntMatrix columnCoords;
    private IntMatrix rowCoords;

    private SourceCode squareCode;
    private SourceCode diamondCode;
    private SourceCodeProperties squareCodeProperties;
    private SourceCodeProperties diamondCodeProperties;

    private int[] edgeValues;
    private int arraySize;
    private int[][] array;
    private int level = 1;
    private int whitespaces = 1;

    @Override
    public void init() {
	lang = new AnimalScript("Diamond-Square", "Felix Mayer, Lulzim Murati",
		800, 600);
	lang.setStepMode(true);
    }

    private void createHeader() {
	Text header = lang.newText(new Coordinates(10, 10), "Diamond-Square",
		"header", null, headerProperties);
	Offset offsetLeft = new Offset(-5, -5, header,
		AnimalScript.DIRECTION_NW);
	Offset offsetRight = new Offset(5, 5, header, AnimalScript.DIRECTION_SE);

	lang.newRect(offsetLeft, offsetRight, "header", null,
		rectangleProperties);
    }

    @Override
    public String generate(AnimationPropertiesContainer props,
	    Hashtable<String, Object> primitives) {
	edgeValues = (int[]) primitives.get("edgeValues");
	matrixProperties = (MatrixProperties) props
		.getPropertiesByName("matrixProperties");
	matrixProperties.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY,
		"table");
	matrixProperties.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
		Color.RED);
	arraySize = (Integer) primitives.get("arraySize");
	headerProperties = (TextProperties) props
		.getPropertiesByName("headerProperties");
	headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		"SansSerif", Font.BOLD, 18));
	introTextProperties = (SourceCodeProperties) props
		.getPropertiesByName("introTextProperties");
	rectangleProperties = (RectProperties) props
		.getPropertiesByName("rectangleProperties");
	stepsTextProperties = (SourceCodeProperties) props
		.getPropertiesByName("stepsTextProperties");

	coordsProperties = new MatrixProperties();
	coordsProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		"SansSerif", Font.PLAIN, 10));
	coordsProperties
		.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	coordsProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
	coordsProperties.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
		Color.LIGHT_GRAY);

	squareCodeProperties = new SourceCodeProperties();
	diamondCodeProperties = new SourceCodeProperties();

	squareCodeProperties.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
		Color.BLUE);
	squareCodeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
		new Font("Monospaced", Font.PLAIN, 12));
	squareCodeProperties.set(
		AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
	squareCodeProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
		Color.BLACK);

	diamondCodeProperties.set(
		AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
	diamondCodeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
		new Font("Monospaced", Font.PLAIN, 12));
	diamondCodeProperties.set(
		AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
	diamondCodeProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
		Color.BLACK);

	array = new int[arraySize][arraySize];
	int[][] columns = new int[1][arraySize];
	int[][] rows = new int[arraySize][1];

	for (int i = 0; i < arraySize; i++) {
	    columns[0][i] = i;
	    rows[i][0] = i;
	}

	createHeader();
	showIntroduction();
	columnCoords = lang.newIntMatrix(new Coordinates(39, 55), columns, "",
		null, coordsProperties);
	rowCoords = lang.newIntMatrix(new Coordinates(12, 82), rows, "", null,
		coordsProperties);
	intMatrix = lang.newIntMatrix(new Coordinates(34, 77), array, "", null,
		matrixProperties);

	showInfoBox();
	showCounter();
	initEdges();
	square(0, 0, arraySize - 1, arraySize - 1);
	diamond(0, 0, arraySize - 1, arraySize - 1);

	lang.nextStep();

	stepsHeadline.hide();
	introductionText.hide();
	stepsText.hide();
	intMatrix.hide();
	counterView.hide();
	columnCoords.hide();
	rowCoords.hide();
	diamondCode.hide();
	squareCode.hide();

	showConclusion();

	String animalScript = lang.toString();
	animalScript = animalScript.replaceAll("refresh", "");
	return animalScript;
    }

    private String printWhitespace() {
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < whitespaces; i++) {
	    sb.append(" ");
	}
	return sb.toString();
    }

    @Override
    public String getName() {
	return "Diamond-Square";
    }

    @Override
    public String getAlgorithmName() {
	return "Diamond-Square";
    }

    @Override
    public String getAnimationAuthor() {
	return "Felix Mayer, Lulzim Murati";
    }

    @Override
    public String getDescription() {
	return "Der Diamond-Square-Algorithmus ist ein rekursiver Algorithmus zur Generierung von zufaelligen "
		+ "Terrainstrukturen. Der Algorithmus startet auf einer quadratischen Karte aus Feldern, von denen nur "
		+ "die vier Eckfelder mit Hoehenwerten initialisiert sind. Nun werden abwechselnd der Square- und der "
		+ "Diamond-Schritt ausgefuehrt, um die Hoehenwerte der restlichen Felder rekursiv zu bestimmen, wobei der "
		+ "Algorithmus bei jedem Rekursionsschritt auf den Quadranten des Eingabequadrats aufgerufen wird. "
		+ "Waehrend im Square-Schritt der Hoehenwert des Quadratmittelpunkts aus dem Durchschnitt der Eckwerte "
		+ "berechnet wird, werden im Diamond-Schritt die Werte der Seitenmittelpunkte aus dem Durchschnitt der "
		+ "Endpunkte der Horizontalen und der Vertikalen durch den jeweiligen Punkt berechnet. Jedem Hoehenwert "
		+ "wird noch ein zufaelliger Offset-Wert hinzugefuegt, um zu vermeiden, dass die Terrainstruktur zu "
		+ "gleichmaessig wird. Das Eingabe-Quadrat muss eine Seitenlaenge von 2^n+1 haben, wobei n eine beliebige "
		+ "natuerlich Zahl ist.";
    }

    @Override
    public String getCodeExample() {
	return "function square(quadrant)\n"
		+ "   wenn groesse(quadrant) <= 1\n" + "      return\n\n"
		+ "   min := getMin(eckwerte(quadrant))\n"
		+ "   max := getMax(eckwerte(quadrant))\n"
		+ "   offset := getOffset(max, min)\n"
		+ "   mean := getMean(eckwerte(quadrant))\n\n"
		+ "function diamond(quadrant)\n"
		+ "   wenn groesse(quadrant) <= 1\n" + "      return\n\n"
		+ "   coords := seitenmittelpunkte(quadrant)\n"
		+ "   berechne hoehenwerte fuer alle seitenmittelpunkte\n"
		+ "   q1,...,q4 := quadrant1,...,quadrant4\n\n"
		+ "   rufe square auf mit jeweils q1 bis q4\n"
		+ "   rufe diamond auf mit jeweils q1 bis q4";
    }

    @Override
    public String getFileExtension() {
	return "asu";
    }

    @Override
    public Locale getContentLocale() {
	return Locale.GERMAN;
    }

    @Override
    public GeneratorType getGeneratorType() {
	return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
    }

    @Override
    public String getOutputLanguage() {
	return Generator.PSEUDO_CODE_OUTPUT;
    }

    // TODO
    private void showSquareCode() {
	squareCodeProperties.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
		Color.BLUE);
	squareCodeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
		new Font("Monospaced", Font.PLAIN, 12));
	squareCodeProperties.set(
		AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
	squareCodeProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
		Color.BLACK);

	squareCode = lang.newSourceCode(new Offset(40, 5, intMatrix,
		AnimalScript.DIRECTION_E), "squareCode", null,
		squareCodeProperties);

	squareCode.addCodeLine("function square(quadrant)", null, 0, null);
	squareCode.addCodeLine("wenn groesse(quadrant) <= 1", null, 1, null);
	squareCode.addCodeLine("return", null, 3, null);
	squareCode.addCodeLine("min := getMin(eckwerte(quadrant))", null, 1,
		null);
	squareCode.addCodeLine("max := getMax(eckwerte(quadrant))", null, 1,
		null);
	squareCode.addCodeLine("offset := getOffset(max, min)", null, 1, null);
	squareCode.addCodeLine("mean := getMean(eckwerte(quadrant))", null, 1,
		null);
    }

    private void showDiamondCode() {
	diamondCodeProperties.set(
		AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
	diamondCodeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
		new Font("Monospaced", Font.PLAIN, 12));
	diamondCodeProperties.set(
		AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
	diamondCodeProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
		Color.BLACK);

	diamondCode = lang.newSourceCode(new Offset(40, 5, intMatrix,
		AnimalScript.DIRECTION_E), "diamondCode", null,
		diamondCodeProperties);

	diamondCode.addCodeLine("function diamond(quadrant)", null, 0, null);
	diamondCode.addCodeLine("wenn groesse(quadrant) <= 1", null, 1, null);
	diamondCode.addCodeLine("return", null, 3, null);
	diamondCode.addCodeLine("coords := seitenmittelpunkte(quadrant)", null,
		1, null);
	diamondCode.addCodeLine("berechne hoehenwerte fuer coords", null, 1,
		null);
	diamondCode.addCodeLine("q1,...,q4 := quadrant1,...,quadrant4", null,
		1, null);
	diamondCode.addCodeLine("rufe square auf mit jeweils q1 bis q4", null,
		1, null);
	diamondCode.addCodeLine("rufe diamond auf mit jeweils q1 bis q4", null,
		1, null);
    }

    private void initEdges() {
	int length = arraySize;
	intMatrix.put(0, 0, edgeValues[0], null, null);
	intMatrix.highlightCell(0, 0, null, null);

	intMatrix.put(0, length - 1, edgeValues[1], null, null);
	intMatrix.highlightCell(0, length - 1, null, null);

	intMatrix.put(length - 1, 0, edgeValues[2], null, null);
	intMatrix.highlightCell(length - 1, 0, null, null);

	intMatrix.put(length - 1, length - 1, edgeValues[3], null, null);
	intMatrix.highlightCell(length - 1, length - 1, null, null);

	lang.nextStep("Initialisierung");
    }

    private void showIntroduction() {
	introductionText = lang.newSourceCode(new Coordinates(20, 40),
		"introduction", null, introTextProperties);
	introductionText
		.addMultilineCode(
			"Der Diamond-Square-Algorithmus ist ein rekursiver Algorithmus zur Generierung von zufaelligen Terrainstrukturen.\n"
				+ "Der Algorithmus startet auf einer quadratischen Karte aus Feldern, von denen nur die vier Eckfelder mit\n"
				+ "Hoehenwerten initialisiert sind. Nun werden abwechselnd der Square- und der Diamond-Schritt ausgefuehrt,\n"
				+ "um die Hoehenwerte der restlichen Felder rekursiv zu bestimmen, wobei der Algorithmus bei jedem\n"
				+ "Rekursionsschritt auf den Quadranten des Eingabequadrats aufgerufen wird.\n"
				+ "Waehrend im Square-Schritt der Hoehenwert des Quadratmittelpunkts aus dem Durchschnitt der Eckwerte berechnet\n"
				+ "wird, werden im Diamond-Schritt die Werte der Seitenmittelpunkte aus dem Durchschnitt der Endpunkte der\n"
				+ "Horizontalen und der Vertikalen durch den jeweiligen Punkt berechnet. Jedem Hoehenwert wird noch ein zufaelliger\n"
				+ "Offset-Wert hinzugefuegt, um zu vermeiden, dass die Terrainstruktur zu gleichmaessig wird. Das Eingabe-Quadrat\n"
				+ "muss eine Seitenlaenge von 2^n+1 haben, wobei n eine beliebige natuerlich Zahl ist.",
			"headline", null);
	lang.nextStep();
	introductionText.hide();
    }

    private void showConclusion() {
	introductionText = lang.newSourceCode(new Coordinates(20, 40),
		"introduction", null, introTextProperties);
	introductionText
		.addMultilineCode(
			"Aufgrund seiner Zufallswerte eignet sich der Diamond-Square-Algorithmus "
				+ "besonders gut zur Generierung moeglichst realistischer Landschaften (z.B. in Computerspielen).\n"
				+ "Dieser ist der am meisten verwendete Algorithmus in diesem Bereich. Weitere Informationen sind zu finden "
				+ "unter: http://en.wikipedia.org/wiki/Diamond-square_algorithm",
			"headline", null);
    }

    private void showCounter() {
	counter = lang.newCounter(intMatrix);
	counterProperties = new CounterProperties();
	counterProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	counterProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);

	Offset offsetField = new Offset(40, 0, intMatrix,
		AnimalScript.DIRECTION_NE);

	counterView = lang.newCounterView(counter, offsetField,
		counterProperties, true, true);
    }

    private void showInfoBox() {
	String currentStep = "Initialisierung der Eckpunkte mit Hoeheninformationen";

	Offset offset = new Offset(-15, 110, intMatrix,
		AnimalScript.DIRECTION_SW);

	stepsHeadline = lang.newText(offset,
		"Aktueller Schritt: Initialisierung", "calcBoxHeader", null,
		headerProperties);

	Offset offsetHeadline = new Offset(0, 7, stepsHeadline,
		AnimalScript.DIRECTION_SW);

	stepsText = lang.newSourceCode(offsetHeadline, "current", null,
		stepsTextProperties);
	stepsText.addCodeLine(currentStep, null, 0, null);
    }

    private int getMin(int a, int b, int c, int d) {
	return Math.min(Math.min(a, b), Math.min(c, d));
    }

    private int getMax(int a, int b, int c, int d) {
	return Math.max(Math.max(a, b), Math.max(c, d));
    }

    private float getOffset(int max, int min) {
	return Math.round(Math.random() * (max / min) / this.level);
    }

    private void square(int x1, int y1, int x2, int y2) {
	if (Math.abs(x1 - x2) <= 1 && Math.abs(y1 - y2) <= 1) {
	    return;
	}

	intMatrix.unhighlightCell(0, 0, null, null);
	intMatrix.unhighlightCell(0, arraySize - 1, null, null);
	intMatrix.unhighlightCell(arraySize - 1, 0, null, null);
	intMatrix.unhighlightCell(arraySize - 1, arraySize - 1, null, null);

	if (level == 1) {
	    showSquareCode();
	}

	int posx1y1 = intMatrix.getElement(y1, x1);
	int posx1y2 = intMatrix.getElement(y2, x1);
	int posx2y1 = intMatrix.getElement(y1, x2);
	int posx2y2 = intMatrix.getElement(y2, x2);

	int min = getMin(posx1y1, posx2y1, posx1y2, posx2y2);
	int max = getMax(posx1y1, posx2y1, posx1y2, posx2y2);

	float offset = getOffset(max, min);

	int mean = Math.round((posx1y1 + posx2y1 + posx1y2 + posx2y2) / 4);

	int xpos = (x1 + x2) / 2;
	int ypos = (y1 + y2) / 2;

	intMatrix.put(ypos, xpos, (int) (mean + offset), null, null);
	intMatrix.highlightCell(ypos, xpos, null, null);

	stepsHeadline.setText("Aktueller Schritt: Square", null, null);

	Offset offsetHeadline = new Offset(0, 7, stepsHeadline,
		AnimalScript.DIRECTION_SW);

	stepsText.hide();
	stepsText = lang.newSourceCode(offsetHeadline, "current", null,
		stepsTextProperties);
	stepsText
		.addMultilineCode(
			"Berechnung und Speicherung der Hoeheninformation an Position ("
				+ xpos
				+ ", "
				+ ypos
				+ "). Sie entspricht dem Durchschnittswert\nder Hoeheninformationen an den Punkten ("
				+ x1 + ", " + y1 + "), (" + x2 + ", " + y1
				+ "), " + "(" + x1 + ", " + y2 + ") und (" + x2
				+ ", " + y2 + ") plus Offset", "", null);

	lang.nextStep(printWhitespace() + "Square Schritt");
	intMatrix.unhighlightCell(ypos, xpos, null, null);
    }

    private int[] getDiamondCoords(int x1, int y1, int x2, int y2) {
	int[] coords = new int[2];
	coords[0] = (x1 + x2) / 2;
	coords[1] = (y1 + y2) / 2;
	return coords;
    }

    /**
     * validates the user input
     */
    @Override
    public boolean validateInput(AnimationPropertiesContainer props,
	    Hashtable<String, Object> primitives)
	    throws IllegalArgumentException {

	int size = (int) primitives.get("arraySize");
	int[] edges = (int[]) primitives.get("edgeValues");

	if (size == 0 || ((size - 1 & size - 2) != 0)) {
	    throw new IllegalArgumentException(
		    "Seitenlaenge muss eine Zweierpotenz + 1 sein!");
	}

	for (int i = 0; i < edges.length; i++) {
	    if (edges[i] < 0 || edges[i] > 99) {
		throw new IllegalArgumentException(
			"Jeder Eckwert muss im Bereich von 0 bis 99 sein!");
	    }
	}

	return true;
    }

    private void diamond(int x1, int y1, int x2, int y2) {
	if (Math.abs(x1 - x2) <= 1 && Math.abs(y1 - y2) <= 1) {
	    return;
	}

	squareCode.hide();

	if (level == 1) {
	    showDiamondCode();
	}

	int[] coords = getDiamondCoords(x1, y1, x2, y2);

	int posx1y1 = intMatrix.getElement(y1, x1);
	int posx1y2 = intMatrix.getElement(y2, x1);
	int posx2y1 = intMatrix.getElement(y1, x2);
	int posx2y2 = intMatrix.getElement(y2, x2);

	// Mitte-Links
	intMatrix.put(coords[1], x1, Math.round((posx1y1 + posx1y2) / 2), null,
		null);

	intMatrix.highlightCell(coords[1], x1, null, null);

	// Mitte-Oben
	intMatrix.put(y1, coords[0], Math.round((posx1y1 + posx2y1) / 2), null,
		null);

	intMatrix.highlightCell(y1, coords[0], null, null);

	// Mitte-Unten
	intMatrix.put(y2, coords[0], Math.round((posx1y2 + posx2y2) / 2), null,
		null);

	intMatrix.highlightCell(y2, coords[0], null, null);

	// Mitte-Rechts
	intMatrix.put(coords[1], x2, Math.round((posx2y1 + posx2y2) / 2), null,
		null);

	intMatrix.highlightCell(coords[1], x2, null, null);

	stepsHeadline.setText("Aktueller Schritt: Diamond", null, null);

	Offset offsetHeadline = new Offset(0, 7, stepsHeadline,
		AnimalScript.DIRECTION_SW);

	stepsText.hide();
	stepsText = lang.newSourceCode(offsetHeadline, "current", null,
		stepsTextProperties);
	stepsText
		.addMultilineCode(
			"Berechnung und Speicherung der Hoeheninformation an Position ("
				+ x1
				+ ", "
				+ coords[1]
				+ "). Sie entspricht dem Durchschnittswert\nder Hoeheninformationen an den Punkten ("
				+ x1 + ", " + y1 + "), (" + x1 + ", " + y2
				+ ") plus Offset", "", null);

	stepsText
		.addMultilineCode(
			"Berechnung und Speicherung der Hoeheninformation an Position ("
				+ coords[0]
				+ ", "
				+ y1
				+ "). Sie entspricht dem Durchschnittswert\nder Hoeheninformationen an den Punkten ("
				+ x1 + ", " + y1 + "), (" + x2 + ", " + y1
				+ ") plus Offset", "", null);

	stepsText
		.addMultilineCode(
			"Berechnung und Speicherung der Hoeheninformation an Position ("
				+ coords[0]
				+ ", "
				+ y2
				+ "). Sie entspricht dem Durchschnittswert\nder Hoeheninformationen an den Punkten ("
				+ x1 + ", " + y2 + "), (" + x2 + ", " + y2
				+ ") plus Offset", "", null);

	stepsText
		.addMultilineCode(
			"Berechnung und Speicherung der Hoeheninformation an Position ("
				+ x2
				+ ", "
				+ coords[1]
				+ "). Sie entspricht dem Durchschnittswert\nder Hoeheninformationen an den Punkten ("
				+ x2 + ", " + y1 + "), (" + x2 + ", " + y2
				+ ") plus Offset", "", null);

	lang.nextStep(printWhitespace() + "Diamond Schritt");
	intMatrix.unhighlightCell(coords[1], x1, null, null);
	intMatrix.unhighlightCell(y1, coords[0], null, null);
	intMatrix.unhighlightCell(y2, coords[0], null, null);
	intMatrix.unhighlightCell(coords[1], x2, null, null);

	level++;
	whitespaces++;

	diamondCode.hide();
	squareCode.show();

	square(x1, y1, (x1 + x2) / 2, (y1 + y2) / 2);
	square((x1 + x2) / 2, y1, x2, (y1 + y2) / 2);
	square(x1, (y1 + y2) / 2, (x1 + x2) / 2, y2);
	square((x1 + x2) / 2, (y1 + y2) / 2, x2, y2);

	squareCode.hide();
	diamondCode.show();

	diamond(x1, y1, (x1 + x2) / 2, (y1 + y2) / 2);
	diamond((x1 + x2) / 2, y1, x2, (y1 + y2) / 2);
	diamond(x1, (y1 + y2) / 2, (x1 + x2) / 2, y2);
	diamond((x1 + x2) / 2, (y1 + y2) / 2, x2, y2);
    }
}

