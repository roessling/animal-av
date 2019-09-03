package generators.graphics.antialias;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Group;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

/**
 * 
 * @author Robert Cibulla, Peter Schauberger
 *
 */
@SuppressWarnings("unused")
public class DDAGenerator implements Generator {
	private Language lang;

	// User adjustable:
	private int xStart, yStart, xEnd, yEnd;
	private int cellsize = 30;
	private Color highlightColor = new Color(0, 150, 0);
	private Color lineColor = new Color(0, 150, 0);

	// other
	private double m, b;
	private int count_x = 20, count_y = 330, count = 0;
	private final double eps = 0.00000001;
	private boolean showCounter;

	// Properties
	private TextProperties auxTextProps, titleProps, labelProps;
	private RectProperties rectProps, titleFrameProps, cursorProps,
			filledRectProps, codeFrameProps, counterProps;
	private SourceCodeProperties introProps, srcProps;

	// Primitives

	private Rect auxCalculation, calculatedVariableFrame, lineVariableFrame,
			titleFrame, cursor, ddaCodeFrame, preconditionsFrame, counter;
	private Rect[] lineRects;
	private SourceCode intro, preconditions, dda, outro;
	private Polyline line;
	private Grid grid;
	private Text xStartText, xEndText, yStartText, yEndText, mText, bText,
			xText, yText, pixelText, title, counterLabel;

	// intro string
	public static final String INTRO = "In der Computergraphik ist ein Problem, dass die interne Darstellung von Linien \n "
			+ "- also Geradenabschnitten - von der internen funktionalen Darstellung \n "
			+ "auf die Pixelansicht des Bildschirms übertragen werden muss. \n \n"
			+ "Um dieses Problem zu lösen, kann eine Hardware- oder Software Implementierung des\n"
			+ "DDA - Digital Differntial Analyzer genutzt "
			+ "werden, um eine lineare Interpolation zwischen den\n"
			+ "Start - und Endpunkten der Linien durchzuführen.\n"
			+ "Dies nennt sich rasterisieren der Linie - also das Abbilden auf ein Punkteraster. \n \n"
			+ "Schritt 1:\n"
			+ "Der DDA stellt sicher dass er auf den Daten arbeiten kann, \n"
			+ "wenn nicht werden die Werte so getauscht dass er von links nach rechts arbeiten kann.\n \n"
			+ "Schritt 2:\n"
			+ "Der Algorithmus setzt einen Bildpunkt nach dem nächsten, \n"
			+ "bis zum Endpunkt der zu zeichnenden Linie.";

	// outro string
	public static final String OUTRO = "Wie man sieht müssen für jedes Pixel, welches der Algorithmus setzt \n"
			+ "drei floating-point Operationen (Multiplikation, Addition und Runden) pro Schleifendurchlauf berechnet werden.\n"
			+ "Wie der Counter unten zeigt ist die Anzahl der FP-Operationen ein linearer Aufwand O(3n). \n"
			+ "n wird dabei durch die Pixellänge der Linie in x-Richtung bestimmt - also n = (xEnd - xStart) nach allen Vorbedingungen.\n \n"
			+ "Gerade bei hohen Auflösungen und vielen Linien ist dies nicht sehr effizient und\n"
			+ "es sollten effizientere Algorithmen in Betracht gezogen werden (z.B. der Bresenham Algorithmus).";

	// source code:
	public static final String DDA_SRC = "public void dda(){\n"
			+ "\tdouble m = (y_end - y_start) / (x_end - x_start);\n"
			+ "\tdouble b = y_start-m*x_start;\n"
			+ "\tfor(x = x_start; x < x_end + 1; x++){\n"
			+ "\t\tdouble y = m*x + b;\n" + "\t\tzeichne(x, Math.round(y));\n"
			+ "\t}\n" + "}";

	public static final String PRECONDITIONS_SRC = "public void vorbedingungen(){\n"
			+ "\tif(m > 1 || m < -1){\n"
			+ "\t\tint temp = x_start;\n"
			+ "\t\tx_start = y_start;\n"
			+ "\t\ty_start = temp;\n"
			+ "\t\ttemp = y_end;\n"
			+ "\t\ty_end = x_end;\n"
			+ "\t\tx_end = temp;\n"
			+ "\t}\n"
			+ "\tif(x_end < x_start){\n"
			+ "\t\tint temp = x_start;\n"
			+ "\t\tx_start = x_end;\n"
			+ "\t\tx_end = temp;\n"
			+ "\t\ttemp = y_start;\n"
			+ "\t\ty_start = y_end;\n" + "\t\ty_end = temp;\n" + "\t}\n}";

	/**
	 * 
	 * @param lang
	 */
	public DDAGenerator() {

	}

	public DDAGenerator(Language lang) {
		this.lang = lang;
	}

	/**
	 * generate the slides
	 */
	public void start() {
		// initialize
		initialize();
		// create intro slides
		intro();

		// draw initial grid and line
		grid.drawGrid();
		line.show();

		// calculate and draw the preconditions
		calculatePreconditions();

		// calculate and draw the algorithm
		calculateAlgorithm();

		// create outro slides
		outro();
	}

	/**
	 * initialize properties, rectangles, SourceCodes etc.
	 */
	public void initialize() {
		lang = new AnimalScript("Digital differential analyzer",
				"Peter Schauberger, Robert Cibulla", 1920, 1080);
		lang.setStepMode(true);

		count = 0;

		// Properties: =======================================================
		auxTextProps = new TextProperties();
		auxTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		auxTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 16));

		titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 24));

		labelProps = new TextProperties();
		labelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN | Font.ITALIC, 15));

		rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		filledRectProps = new RectProperties();
		filledRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		filledRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, lineColor);
		filledRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);

		srcProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				highlightColor);

		// Texts: ============================================================
		title = lang.newText(new Coordinates(20, 30),
				"Digital differential analyzer", "title", null, titleProps);
		counterLabel = lang.newText(new Coordinates(20, 300),
				"Anzahl Floating Point Operationen:" + count, "title", null,
				labelProps);
		counterLabel.hide();
		// SourceCodes: ======================================================
		intro = lang.newSourceCode(new Coordinates(20, 75), "intro", null,
				introProps);
		intro.addMultilineCode(INTRO, null, null);

		preconditions = lang.newSourceCode(new Coordinates(20, 75),
				"preconditions", null, srcProps);
		preconditions.addMultilineCode(PRECONDITIONS_SRC, null, null);

		dda = lang
				.newSourceCode(new Coordinates(20, 75), "dda", null, srcProps);
		dda.addMultilineCode(DDA_SRC, null, null);
		dda.hide();

		outro = lang.newSourceCode(new Coordinates(20, 75), "outro", null,
				introProps);
		outro.addMultilineCode(OUTRO, null, null);
		outro.hide();

		// Rects
		titleFrame = lang
				.newRect(
						new Offset(-5, -5, "title", AnimalScript.DIRECTION_NW),
						new Offset(5, 5, "title", AnimalScript.DIRECTION_SE),
						"titleFrame", null, titleFrameProps);

		auxCalculation = lang.newRect(new Coordinates(15, 420),
				new Coordinates(500, 520), "auxCalculation", null, rectProps);

		lineVariableFrame = lang.newRect(new Offset(0, 10, "auxCalculation",
				AnimalScript.DIRECTION_SW), new Offset(100, 110,
				"auxCalculation", AnimalScript.DIRECTION_SW),
				"lineVariableFrame", null, codeFrameProps);

		calculatedVariableFrame = lang.newRect(new Offset(10, 0,
				"lineVariableFrame", AnimalScript.DIRECTION_NE), new Offset(
				200, 100, "lineVariableFrame", AnimalScript.DIRECTION_NE),
				"calculatedVariablesFrame", null, codeFrameProps);

		preconditionsFrame = lang.newRect(new Offset(-5, -5, "preconditions",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "preconditions",
				AnimalScript.DIRECTION_SE), "preconditionsFrame", null,
				codeFrameProps);

		ddaCodeFrame = lang.newRect(new Offset(-5, -5, "dda",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "dda",
				AnimalScript.DIRECTION_SE), "ddaCodeFrame", null,
				codeFrameProps);

		counter = lang.newRect(new Coordinates(count_x, count_y),
				new Coordinates(count_x + count * 5, count_y + 20), "Counter",
				null, counterProps);

		// initial grid
		grid = new Grid(lang, xStart, xEnd, yStart, yEnd, cellsize, 550, 0);

		// initial line;
		line = calculateLine();
		line.hide();

		// show/hide
		intro.hide();
		preconditions.hide();
		dda.hide();
		counter.hide();
	}

	/**
	 * create the intro slides.
	 */
	private void intro() {
		// Slide 1
		intro.show();

		lang.nextStep("Intro");

		// Slide 2
		intro.hide();
	}

	/**
	 * This method is used to calculate the precondition steps.
	 */
	private void calculatePreconditions() {
		// first preconditions slide:
		preconditions.show();
		preconditionsFrame.show();
		SourceCode auxText1 = lang.newSourceCode(new Offset(10, -5,
				"auxCalculation", AnimalScript.DIRECTION_NW), "auxText1", null,
				srcProps);
		SourceCode auxText2 = lang.newSourceCode(new Offset(10, -5,
				"auxCalculation", AnimalScript.DIRECTION_NW), "auxText2", null,
				srcProps);
		setLineVariables();

		auxText2.hide();
		auxCalculation.show();
		lineVariableFrame.show();

		lang.nextStep();

		// second preconditions slide:
		preconditions.highlight(1);

		auxText1.addCodeLine("m = (y_end - y_start) / (x_end - x_start)", null,
				0, null);

		lang.nextStep("Vorbedingungen I");
		// third preconditions slide:

		if (xEnd - xStart < eps) {
			auxText1.addCodeLine(
					"xEnd - xStart = 0.0 -> keine Steigung, setze m = 0", null,
					0, null);
			m = 0.0;
		} else {
			m = (((double) (yEnd - yStart)) / ((double) (xEnd - xStart)));
		}
		auxText1.addCodeLine("m = " + m, null, 0, null);

		// fourth preconditions slide:
		lang.nextStep();
		if (((yEnd - yStart) >= (xEnd - xStart) || (yEnd - yStart) <= (xEnd - xStart))
				&& (xEnd - xStart) > eps) {
			auxText1.addCodeLine("m ist nicht in [-1,1] - tausche x und y",
					null, 0, null);

			// fifth alternative 1 slides:
			lang.nextStep();

			int temp = xStart;
			preconditions.unhighlight(1);
			preconditions.highlight(2);
			lang.nextStep();

			// sixth alternative 1 slides:
			preconditions.unhighlight(2);
			preconditions.highlight(3);

			xStart = yStart;
			refreshLineVariables();
			xStartText.changeColor("", highlightColor, null, null);
			lang.nextStep();

			// seventh alternative 1 slides:
			preconditions.unhighlight(3);
			preconditions.highlight(4);
			xStartText.changeColor("", Color.BLACK, null, null);
			yStartText.changeColor("", highlightColor, null, null);
			yStart = temp;
			refreshLineVariables();

			lang.nextStep();

			// eighth alternative 1 slides:
			preconditions.unhighlight(4);
			preconditions.highlight(5);
			yStartText.changeColor("", Color.BLACK, null, null);
			temp = xEnd;
			lang.nextStep();

			// ninth alternative 1 slides:
			preconditions.unhighlight(5);
			preconditions.highlight(6);
			xEndText.changeColor("", highlightColor, null, null);
			xEnd = yEnd;
			refreshLineVariables();
			lang.nextStep();

			// tenth alternative 1 slides:
			preconditions.unhighlight(6);
			preconditions.highlight(7);
			xEndText.changeColor("", Color.BLACK, null, null);
			yEndText.changeColor("", highlightColor, null, null);
			yEnd = temp;
			refreshLineVariables();
			lang.nextStep();

			// eleventh alternative 1 slides:
			yEndText.changeColor("", Color.BLACK, null, null);
			preconditions.unhighlight(7);
			m = (((double) (yEnd - yStart)) / ((double) (xEnd - xStart)));
			auxText1.addCodeLine("m nach tausch: m = " + m, null, 0, null);

			// hide old grid, create new grid and set gridID to 1
			// (to avoid conflicting names in AnimalScript)
			grid.hide();
			grid = new Grid(lang, xStart, xEnd, yStart, yEnd, cellsize, 550, 0);

			grid.drawGrid();
			line.hide();
			line = calculateLine();
		} else {
			auxText1.addCodeLine("m ist in [-1,1] - kein Tausch notwendig.",
					null, 1, null);

		}

		lang.nextStep();
		preconditions.unhighlight(1);
		preconditions.highlight(9);
		auxText1.hide();

		auxText2.addCodeLine("x_end = " + xEnd, null, 0, null);
		auxText2.addCodeLine("x_start = " + xStart, null, 0, null);
		lang.nextStep("Vorbedingungen II");
		preconditions.highlight(9);

		if (xEnd >= xStart) {
			auxText2.addCodeLine(xEnd + " >= " + xStart
					+ ", keine �nderung notwendig", null, 0, null);
			lang.nextStep();
			preconditions.unhighlight(9);
		} else {
			auxText2.addCodeLine(xEnd + " < " + xStart
					+ ", tausche Anfangs- und Endwerte.", null, 0, null);

			lang.nextStep();
			preconditions.unhighlight(9);
			preconditions.highlight(10);
			int temp = xStart;

			lang.nextStep();
			preconditions.unhighlight(10);
			preconditions.highlight(11);
			xStart = xEnd;
			xStartText.changeColor("", highlightColor, null, null);
			refreshLineVariables();

			lang.nextStep();
			preconditions.unhighlight(11);
			preconditions.highlight(12);
			xEnd = temp;
			xStartText.changeColor("", Color.BLACK, null, null);
			xEndText.changeColor("", highlightColor, null, null);
			refreshLineVariables();

			lang.nextStep();
			preconditions.unhighlight(12);
			preconditions.highlight(13);
			temp = yStart;
			xEndText.changeColor("", Color.BLACK, null, null);

			lang.nextStep();
			preconditions.unhighlight(13);
			preconditions.highlight(14);
			yStart = yEnd;
			yStartText.changeColor("", highlightColor, null, null);
			refreshLineVariables();

			lang.nextStep();
			preconditions.unhighlight(14);
			preconditions.highlight(15);
			yEnd = temp;
			yStartText.changeColor("", Color.BLACK, null, null);
			yEndText.changeColor("", highlightColor, null, null);
			refreshLineVariables();

			lang.nextStep();
			preconditions.unhighlight(15);
			yEndText.changeColor("", Color.BLACK, null, null);
		}

		auxText1.hide();
		auxText2.hide();
		preconditions.hide();
		preconditionsFrame.hide();
	}

	/**
	 * Calculate and draw DDA
	 */
	private void calculateAlgorithm() {

		double y;

		dda.show();
		ddaCodeFrame.show();
		setCalculatedVariables();
		if (showCounter) {
			counterLabel.show();
			counter.show();
		}
		dda.highlight(3);

		xText = lang.newText(new Offset(5, 5, "auxCalculation",
				AnimalScript.DIRECTION_NW), "x = ?", "xText", null,
				auxTextProps);
		yText = lang.newText(new Offset(0, 5, "xText",
				AnimalScript.DIRECTION_SW), "y = ?", "yText", null,
				auxTextProps);

		pixelText = lang.newText(new Offset(0, 5, "yText",
				AnimalScript.DIRECTION_SW), "Pixel (?, ?)", "pointText", null,
				auxTextProps);

		cursor = grid.getCellAsRect(xStart, yStart, cursorProps, "cursor");
		lineRects = new Rect[grid.getxLeftOffset() + grid.getxRightOffset() + 1];

		for (int x = xStart; x < xEnd + 1; x++) {
			dda.highlight(3);
			dda.unhighlight(5);
			xText.setText("x = " + x, null, null);
			yText.setText("y = ?", null, null);
			pixelText.setText("Pixel (?, ?)", null, null);
			lang.nextStep("Algorithmus x = " + x);
			dda.highlight(4);
			y = m * x + b;

			if (showCounter) {
				counter.hide();
				count++;
				counter = lang.newRect(new Coordinates(count_x, count_y),
						new Coordinates(count_x + (++count * 5), count_y + 20),
						"RoundCounter", null, counterProps);
				counterLabel.setText("Anzahl Floating Point Operationen:"
						+ count, null, null);
				counter.show();
			}

			yText.setText("y = " + y, null, null);
			lang.nextStep();
			dda.unhighlight(4);
			dda.highlight(5);
			pixelText.setText("zeichne Pixel (" + x + ", " + Math.round(y)
					+ ")", null, null);

			if (showCounter) {
				counter.hide();
				counter = lang.newRect(new Coordinates(count_x, count_y),
						new Coordinates(count_x + (++count * 5), count_y + 20),
						"RoundCounter", null, counterProps);
				counterLabel.setText("Anzahl Floating Point Operationen:"
						+ count, null, null);
				counter.show();
			}

			// cursor movement animation
			cursor.moveTo(null, "translate",
					grid.getUpperLeftCellCoordinates(x, (int) Math.round(y)),
					new TicksTiming(25), new TicksTiming(50));

			lang.nextStep();
			lineRects[x + grid.getxLeftOffset()] = grid.getCellAsRect(x,
					(int) Math.round(y), filledRectProps, "");
			lang.nextStep();

		}
		Group algoGroup = lang.newGroup(getAlgoPrimitives(), "algoGroup");
		algoGroup.hide();
		grid.hide();
	}

	/**
	 * show the outro
	 */
	private void outro() {
		outro.show();
		cursor.hide();
		lang.nextStep("Outro");
	}

	/**
	 * calculate and draw the line
	 * 
	 * @return
	 */
	private Polyline calculateLine() {
		line = lang.newPolyline(
				new Coordinates[] { grid.getCellAsCoordinates(xStart, yStart),
						grid.getCellAsCoordinates(xEnd, yEnd) }, "line", null);
		return line;
	}

	private void setLineVariables() {
		if (xStartText != null)
			xStartText.hide();
		if (yStartText != null)
			yStartText.hide();
		if (xEndText != null)
			xEndText.hide();
		if (yEndText != null)
			yEndText.hide();

		xStartText = lang.newText(new Offset(5, 5, "lineVariableFrame",
				AnimalScript.DIRECTION_NW), "x_start = " + xStart, "vXStart",
				null);
		yStartText = lang.newText(new Offset(0, 5, "vXStart",
				AnimalScript.DIRECTION_SW), "y_start = " + yStart, "vYStart",
				null);
		xEndText = lang.newText(new Offset(0, 5, "vYStart",
				AnimalScript.DIRECTION_SW), "x_end = " + xEnd, "vXEnd", null);
		yEndText = lang.newText(new Offset(0, 5, "vXEnd",
				AnimalScript.DIRECTION_SW), "y_end = " + yEnd, "vYEnd", null);
	}

	private void refreshLineVariables() {
		xStartText.setText("x_start = " + xStart, null, null);
		yStartText.setText("y_start = " + yStart, null, null);
		xEndText.setText("x_end = " + xEnd, null, null);
		yEndText.setText("y_end = " + yEnd, null, null);
	}

	private void setCalculatedVariables() {
		if (mText != null)
			mText.hide();
		if (bText != null)
			bText.hide();

		calculatedVariableFrame.show();

		m = (((double) (yEnd - yStart)) / ((double) (xEnd - xStart)));

		// Dirty hack to prevent weird behavior for dots (division by zero)
		if (Double.isNaN(m)) {
			m = 0.0;
		}
		b = yStart - m * xStart;

		dda.highlight(1);
		mText = lang.newText(new Offset(5, 5, "calculatedVariablesFrame",
				AnimalScript.DIRECTION_NW), "m = " + m, "mText", null);
		mText.changeColor("", highlightColor, null, null);

		lang.nextStep();
		dda.unhighlight(1);
		dda.highlight(2);
		bText = lang.newText(new Offset(0, 5, "mText",
				AnimalScript.DIRECTION_SW), "b = " + b, "bText", null);
		bText.changeColor("", highlightColor, null, null);
		mText.changeColor("", Color.BLACK, null, null);

		lang.nextStep();
		dda.unhighlight(2);
		bText.changeColor("", Color.BLACK, null, null);
	}

	private LinkedList<Primitive> getAlgoPrimitives() {
		LinkedList<Primitive> algoGroup = new LinkedList<Primitive>();
		algoGroup.add(auxCalculation);
		algoGroup.add(calculatedVariableFrame);
		algoGroup.add(lineVariableFrame);
		algoGroup.add(cursor);
		algoGroup.add(ddaCodeFrame);

		algoGroup.add(dda);
		algoGroup.add(line);

		algoGroup.add(xStartText);
		algoGroup.add(xEndText);
		algoGroup.add(yStartText);
		algoGroup.add(yEndText);
		algoGroup.add(mText);
		algoGroup.add(bText);
		algoGroup.add(xText);
		algoGroup.add(yText);
		algoGroup.add(pixelText);

		for (Primitive prim : lineRects) {
			if (prim != null)
				algoGroup.add(prim);
		}
		return algoGroup;
	}

	public String getName() {
		return "Digital Diferential Analyzer";
	}

	public String getAlgorithmName() {
		return "Digital Diferential Analyzer";
	}

	public String getAnimationAuthor() {
		return "Robert Cibulla, Peter Schauberger";
	}

	// oe = &ouml; ss = &szlig;
	public String getDescription() {
		return "Der Digital Differential Analyzer (DDA) Algorithmus berechnet\n"
				+ "f&uuml;r einen Anfangs und Endpunkt eine Linie auf einem Raster\n"
				+ "die beide Punkte verbindet.\n"
				+ "Hier wird die einfachste Implementierung gezeigt.\n"
				+ "Durch Verwendung von Flie&szlig;komma-Additionen, -Multiplikationen sowie -Rundungen\n"
				+ "ist der Algorithmus allerdings nicht besonders effizient solange keine Flie&szlig;komma-Einheit bereit steht, die solche Operationen schnell ausf&uuml;hren kann.\n"
				+ "Dieser Algorithmus ist allerings gut f&uuml;r die Hardware Implementierung geeignet und \n"
				+ "kann f&uuml;r maximalen Durchsatz in einer Pipeline ausgef&uuml;hrt werden.";
	}

	public String getCodeExample() {
		return "public void vorbedingungen(){\n"
				+ "\t\\\\Steigung m berechnen. Ist sie au&szlig;erhalb des Bereichs: X und Y tauschen \n"
				+ "\tif(m > 1 || m < -1){\n" + "\t\tint temp = x_start;\n"
				+ "\t\tx_start = y_start;\n" + "\t\ty_start = temp;\n"
				+ "\t\ttemp = y_end;\n" + "\t\ty_end = x_end;\n"
				+ "\t\tx_end = temp;\n" + "\t}\n"
				+ "\t\\\\ Ist Endpunkt gr&ouml;&szlig;er Anfangspunkt (der X-Achse)\n"
				+ "\tif(x_end <= x_start){\n" + "\t\tint temp = x_start;\n"
				+ "\t\tx_start = x_end;\n" + "\t\tx_end = temp;\n"
				+ "\t\ttemp = y_start;\n" + "\t\ty_start = y_end;\n"
				+ "\t\ty_end = temp;\n" + "\t}\n} \n \n"
				+ "public void dda(){\n"
				+ "\t\\\\gleichbleibende Steigung m berechnen\n"
				+ "\tdouble m = (y_end - y_start) / (x_end - x_start);\n"
				+ "\tdouble b = y_start - m * x_start;\n"
				+ "\tfor(x = x_start; x < x_end + 1; x++){\n"
				+ "\t\tdouble y = m*x + b;\n"
				+ "\t\tzeichne(x, Math.round(y));\n" + "\t}\n" + "}";
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		xStart = (Integer) primitives.get("xStart");
		yStart = (Integer) primitives.get("yStart");
		xEnd = (Integer) primitives.get("xEnd");
		yEnd = (Integer) primitives.get("yEnd");
		cellsize = Math.abs((Integer) primitives.get("cellsize"));
		highlightColor = (Color) primitives.get("highlightColor");
		lineColor = (Color) primitives.get("lineColor");
		showCounter = (Boolean) primitives.get("showCounter");
		srcProps = (SourceCodeProperties) props.get(0);
		cursorProps = (RectProperties) props.get(1);
		introProps = (SourceCodeProperties) props.get(2);
		titleFrameProps = (RectProperties) props.get(3);
		counterProps = (RectProperties) props.get(4);
		codeFrameProps = (RectProperties) props.get(5);
		
		Font font = (Font) introProps.get(AnimationPropertiesKeys.FONT_PROPERTY);
		Font toset = new Font(font.getName(), font.getStyle(), (int) introProps.get(AnimationPropertiesKeys.SIZE_PROPERTY));
		
		introProps.set(AnimationPropertiesKeys.FONT_PROPERTY, toset);

		start();

		return lang.toString();
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public void init() {

	}
}
