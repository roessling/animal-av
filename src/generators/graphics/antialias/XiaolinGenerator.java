package generators.graphics.antialias;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
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
public class XiaolinGenerator implements Generator, ValidatingGenerator {
	private Language lang;

	// User adjustable:
	private int xStart, yStart, xEnd, yEnd, cellsize;
	private Color highlightColor, lineColor;

	// Properties
	private TextProperties auxTextProps, titleTextProps;
	private RectProperties auxRectProps, titleFrameProps, filledRectProps,
			codeFrameProps, cursorProps;
	private SourceCodeProperties introProps, srcProps;

	// Primitives
	private Rect auxCalculation, lineVariableFrame, titleFrame,
			xiaolinCodeFrame, preconditionsFrame, endpointsFrame, cursor;
	private Rect[] lineRects;
	private SourceCode intro, preconditions, endpoints, xiaolin, explain,
			outro;
	private Polyline line;
	private Grid grid;
	private Text xpxlText, ypxlText, interyText, xGapText, plotText, title;

	// intro / outro strings
	public static final String INTRO = "In der Computergrafik wird Xiaolin Wu's Line Algorithmus dazu benutzt \n"
			+ "um Linien auf einem Raster zu zeichnen. \n"
			+ "Der Algorithmus vermeidet sogenannte Aliasingeffekte - das sind Treppeneffekte, die durch die Auflösung des Bildschirms entstehen.\n"
			+ "Im Vergleich zu anderen Algorithmen die geglättete Linien zeichnen ist \n"
			+ "Xiaolin Wu's Ansatz schnell, im Vergleich zu Verfahren ohne Antialiasing aufgrund des erhöhten Rechenaufwands\n"
			+ "zur Glättung jedoch langsamer. \n \n"
			+ "Vorgehen des Algorithmus: \n \n"
			+ "Zunächst werden die Endpunkte behandelt und gesetzt. \n"
			+ "Anschließend wird aufsteigend jeweils ein Paar von 2 Pixeln gesetzt. \n"
			+ "Die Farbe aller Pixel basiert darauf, wie weit ihre Mittelpunkte \n"
			+ "von der idealen Geraden entfernt sind. \n"
			+ "Je näher daran, desto dunkler die Farbe. \n \n"
			+ "Der Länge des Codes geschuldet wird der Ablauf in die beschriebenen 3 Phasen unterteilt. \n"
			+ "\t - Vorbereitungen\n"
			+ "\t - Setzen der Endpunkte\n"
			+ "\t - Verbinden der Endpunkte\n \n"
			+ "Erläuterung der Hilfsfunktionen:\n"
			+ "\t - swap(var1, var2): Tauschen der Werte var1 und var2.\n"
			+ "\t - round(var): Runden auf nächste ganze Zahl.\n"
			+ "\t - ipart(var): Abschneiden der Nachkommastellen auf ganze Zahl.\n"
			+ "\t - fpart(var): Abschneiden der Vorkommastelle auf Fließkommazahl in [0,1].\n"
			+ "\t - rfpart(var): 1 - fpart(var).\n"
			+ "\t - plot(x,y,color): Zeichne Pixel an Stelle (x,y) mit Farbstärke color";

	public static final String OUTRO = "Durch die Anpassung der Farbintensität mit dem Abstand \n"
			+ "zur Linie wird bei höheren Auflösungen eine schöne \n"
			+ "Darstellung erzielt.\n"
			+ "Das setzen von 2 Pixeln in einem Schleifendurchlauf bringt\n"
			+ "- im Vergleich zu anderen Antialiasingmethoden -\n"
			+ "eine bessere Ausführungsgeschwindigkeit.\n"
			+ "Eine weitere Verbesserungsidee bestünde darin \n"
			+ "die Linie aus immer gleichen Abschnitten zusammenzusetzen \n"
			+ "die aus einer größeren Anzahl Pixel bestehen \n"
			+ "und so die Symmetrie auszunutzen.";

	// source code:
	public static final String PRECONDITIONS_SRC = "public void drawLine(x0,y0,x1,y1){\n"
			+ "\tboolean steep := Math.abs(y1 - y0) > Math.abs(x1 - x0)\n"
			+ "\tif(steep){\n"
			+ "\t\tswap(x0, y0);\n"
			+ "\t\tswap(x1, y1);\n"
			+ "\t}\n"
			+ "\tif(x0 > x1){\n"
			+ "\t\tswap(x0, x1);\n"
			+ "\t\tswap(y0, y1);\n"
			+ "\t}\n"
			+ "\tint dx = x1 - x0;\n"
			+ "\tint dy = y1 - y0;\n" + "\tdouble gradient := dy / dx;\n \n";

	public static final String ENDPOINTS_SRC = "\t// handle first endpoint\n"
			+ "\tint xend = round(x0);\n"
			+ "\tdouble yend = y0 + gradient * (xend - x0);\n"
			+ "\tdouble xgap = rfpart(x0 + 0.5);\n" + "\tint xpxl1 = xend; \n"
			+ "\tint ypxl1 = ipart(yend);\n" + "\tif(steep) {\n"
			+ "\t\tplot(ypxl1,   xpxl1, rfpart(yend) * xgap);\n"
			+ "\t\tplot(ypxl1+1, xpxl1,  fpart(yend) * xgap);\n"
			+ "\t} else {\n"
			+ "\t\tplot(xpxl1, ypxl1  , rfpart(yend) * xgap);\n"
			+ "\t\tplot(xpxl1, ypxl1+1,  fpart(yend) * xgap);\n" + "\t}\n"
			+ "\tdouble intery := yend + gradient; \n \n"
			+ "\t// handle second endpoint\n" + "\txend = round(x1)\n"
			+ "\tyend = y1 + gradient * (xend - x1);\n"
			+ "\txgap = fpart(x1 + 0.5);\n" + "\tint xpxl2 = xend; \n"
			+ "\tint ypxl2 = ipart(yend);\n" + "\tif(steep) {\n"
			+ "\t\tplot(ypxl2  , xpxl2, rfpart(yend) * xgap);\n"
			+ "\t\tplot(ypxl2+1, xpxl2,  fpart(yend) * xgap);\n"
			+ "\t} else {\n"
			+ "\t\tplot(xpxl2, ypxl2,  rfpart(yend) * xgap);\n"
			+ "\t\tplot(xpxl2, ypxl2+1, fpart(yend) * xgap);\n" + "\t}\n \n";

	public static final String XIAOLIN_SRC = "\t// main loop\n"
			+ "\tfor(int x = xpxl1 + 1; x < xpxl2; x++){\n" + "\tif(steep){\n"
			+ "\t\tplot(ipart(intery)  , x, rfpart(intery));\n"
			+ "\t\tplot(ipart(intery)+1, x,  fpart(intery));\n"
			+ "\t} else {\n"
			+ "\t\tplot(x, ipart (intery),  rfpart(intery));\n"
			+ "\t\tplot(x, ipart (intery)+1, fpart(intery));\n" + "\t}\n"
			+ "\tintery = intery + gradient;\n" + "\t}\n" + "}";

	// @Override
	public void init() {
		this.lang = new AnimalScript("Xiaolin's Line Algorithmus",
				"Robert Cibulla, Peter Schauberger", 1920, 1080);
		this.lang.setStepMode(true);
	}

	/**
	 * initialize properties, rectangles, SourceCodes etc.
	 */
	public void initialize() {

		// Properties: =======================================================
		auxTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 14));

		titleTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 24));

		introProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 18));

		// Texts: ============================================================
		title = lang.newText(new Coordinates(20, 30), "Xiaolin-Wu", "title",
				null, titleTextProps);
		// SourceCodes: ======================================================
		intro = lang.newSourceCode(new Coordinates(20, 75), "intro", null,
				introProps);
		intro.addMultilineCode(INTRO, null, null);

		preconditions = lang.newSourceCode(new Coordinates(20, 75),
				"preconditions", null, srcProps);
		preconditions.addMultilineCode(PRECONDITIONS_SRC, null, null);

		endpoints = lang.newSourceCode(new Coordinates(20, 75), "endpoints",
				null, srcProps);
		endpoints.addMultilineCode(ENDPOINTS_SRC, null, null);

		xiaolin = lang.newSourceCode(new Coordinates(20, 75), "xiaolin", null,
				srcProps);
		xiaolin.addMultilineCode(XIAOLIN_SRC, null, null);

		outro = lang.newSourceCode(new Coordinates(20, 75), "outro", null,
				introProps);
		outro.addMultilineCode(OUTRO, null, null);

		// Rects
		titleFrame = lang
				.newRect(
						new Offset(-5, -5, "title", AnimalScript.DIRECTION_NW),
						new Offset(5, 5, "title", AnimalScript.DIRECTION_SE),
						"titleFrame", null, titleFrameProps);

		preconditionsFrame = lang.newRect(new Offset(-5, -5, "preconditions",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "preconditions",
				AnimalScript.DIRECTION_SE), "preconditionsFrame", null,
				codeFrameProps);

		endpointsFrame = lang.newRect(new Offset(-5, -5, "endpoints",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "endpoints",
				AnimalScript.DIRECTION_SE), "endpointsCodeFrame", null,
				codeFrameProps);

		xiaolinCodeFrame = lang.newRect(new Offset(-5, -5, "xiaolin",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "xiaolin",
				AnimalScript.DIRECTION_SE), "xiaolinCodeFrame", null,
				codeFrameProps);

		auxCalculation = lang.newRect(new Offset(0, 10, "endpointsCodeFrame",
				AnimalScript.DIRECTION_SW), new Offset(250, 125,
				"endpointsCodeFrame", AnimalScript.DIRECTION_SW),
				"auxCalculation", null, auxRectProps);

		lineVariableFrame = lang.newRect(new Offset(0, 10, "auxCalculation",
				AnimalScript.DIRECTION_SW), new Offset(250, 150,
				"auxCalculation", AnimalScript.DIRECTION_SW),
				"lineVariableFrame", null, auxRectProps);

		// initial grid
		grid = new Grid(lang, xStart - 1, xEnd + 1, yStart - 1, yEnd + 1,
				cellsize, 550, 0);

		// initial line;
		line = calculateLine();

		// show/hide
		intro.hide();
		preconditions.hide();
		endpoints.hide();
		xiaolin.hide();
		line.hide();
		outro.hide();
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		this.xStart = Math.abs((Integer) primitives.get("xStart"));
		this.yStart = Math.abs((Integer) primitives.get("yStart"));
		this.xEnd = Math.abs((Integer) primitives.get("xEnd"));
		this.yEnd = Math.abs((Integer) primitives.get("yEnd"));
		this.cellsize = Math.abs((Integer) primitives.get("cellsize"));

		titleTextProps = (TextProperties) props.get(0);
		titleFrameProps = (RectProperties) props.get(1);
		introProps = (SourceCodeProperties) props.get(2);
		srcProps = (SourceCodeProperties) props.get(3);
		codeFrameProps = (RectProperties) props.get(4);
		filledRectProps = (RectProperties) props.get(5);
		auxTextProps = (TextProperties) props.get(6);
		auxRectProps = (RectProperties) props.get(7);
		cursorProps = (RectProperties) props.get(8);

		lineColor = (Color) filledRectProps
				.get(AnimationPropertiesKeys.FILL_PROPERTY);
		highlightColor = (Color) srcProps
				.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY);

		// initialize
		initialize();
		// create intro slides
		intro();

		// draw initial grid and line
		grid.drawGrid();
		line.show();

		// calculate and draw the algorithm
		drawXiaolinLine();

		// create outro slides
		outro();
		return lang.toString();
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

	// ==============================================================
	// Xiaolins Algo von Pseudocode in Java �bertragen
	// ==============================================================

	private int ipart(Double x) {
		return x.intValue(); // return 'integer part of x'
	}

	private int round(double x) {
		return (int) Math.round(x); // return ipart(x + 0.5)
	}

	private double fpart(double x) {
		return Math.abs(x % 1); // return 'fractional part of x'
	}

	private double rfpart(double x) {
		return 1 - fpart(x); // return 1 - fpart(x)
	}

	// Set Filled Rectangle Color to identical R,G,B Values brightness
	private void setFilledRectColorDepth(double c) {
		// System.out.println("Color ist = " +c);
		float color = (float) c;
		Color baseColor = lineColor;
		float[] hsbvals = new float[3];
		Color.RGBtoHSB(baseColor.getRed(), baseColor.getGreen(),
				baseColor.getBlue(), hsbvals);
		filledRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(
				Color.HSBtoRGB(hsbvals[0], color, hsbvals[2])));
		if (baseColor.getBlue() == baseColor.getGreen()
				&& baseColor.getGreen() == baseColor.getRed()) {
			int col = Math.round((1 - color) * 255);
			filledRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
					new Color(col, col, col));
		}
	}

	private void refreshVariables(int x, int y, double color) {
		plotText.setText(
				"plot(x = " + x + ", y = " + y + ", color = "
						+ Math.round(100.0 * color) + " %)", null, null);

	}

	private void refreshIntery(double intery) {
		DecimalFormat df = new DecimalFormat("#.###");
		interyText.setText("intery = " + df.format(intery).replace(',', '.'),
				null, null);
	}

	private void refreshXgap(double xgap) {
		DecimalFormat df = new DecimalFormat("#.###");
		xGapText.setText("xgap = " + df.format(xgap), null, null);
	}

	private void refreshxpxl(String string, int xpxl) {
		xpxlText.setText(string + " = " + xpxl, null, null);

	}

	private void refreshypxl(String string, int ypxl) {
		ypxlText.setText(string + " = " + ypxl, null, null);
	}

	private void moveCursor(int x, int y) {
		cursor.moveTo(null, "translate",
				grid.getUpperLeftCellCoordinates(x, (int) Math.round(y)),
				new TicksTiming(25), new TicksTiming(50));
		lang.nextStep();
	}

	private void drawXiaolinLine() {
		preconditions.show();
		preconditionsFrame.show();
		auxCalculation.show();
		lineVariableFrame.show();

		plotText = lang.newText(new Offset(5, 5, "lineVariableFrame",
				AnimalScript.DIRECTION_NW), "plot(x, y, color)", "plotText",
				null, auxTextProps);
		plotText.hide();
		interyText = lang.newText(new Offset(0, 5, "plotText",
				AnimalScript.DIRECTION_SW), "intery = ?", "intery", null,
				auxTextProps);
		interyText.hide();
		xGapText = lang.newText(new Offset(0, 5, "intery",
				AnimalScript.DIRECTION_SW), "xgap = ?", "xGapText", null,
				auxTextProps);
		xGapText.hide();
		xpxlText = lang.newText(new Offset(0, 5, "xGapText",
				AnimalScript.DIRECTION_SW), "xpxl1 = ?", "xpxlText", null,
				auxTextProps);
		xpxlText.hide();
		ypxlText = lang.newText(new Offset(0, 5, "xpxlText",
				AnimalScript.DIRECTION_SW), "ypxl1 = ?", "ypxlText", null,
				auxTextProps);
		ypxlText.hide();

		explain = lang.newSourceCode(new Offset(5, 0, "auxCalculation",
				AnimalScript.DIRECTION_NW), "explain", null, srcProps);

		cursor = grid.getCellAsRect(xStart, yStart, cursorProps, "cursor");

		lineRects = new Rect[grid.getxLeftOffset() + grid.getxRightOffset() + 4];
		boolean steep;
		preconditions.highlight(1);
		if (Math.abs(yEnd - yStart) > Math.abs(xEnd - xStart))
			steep = true;
		else
			steep = false;

		explain.addCodeLine("Steep ist:" + steep, null, 0, null);
		lang.nextStep("Vorbedingungen");

		preconditions.unhighlight(1);
		preconditions.highlight(2);
		lang.nextStep();

		if (steep) {
			// swap(x0, y0)
			preconditions.highlight(3);
			explain.addCodeLine("zu steil: tausche  x0 und y0", null, 0, null);
			int temp = xStart;
			xStart = yStart;
			yStart = temp;
			lang.nextStep();

			// swap(x1, y1)
			preconditions.unhighlight(3);
			preconditions.highlight(4);
			explain.addCodeLine("und tausche x1 und y1", null, 0, null);
			temp = xEnd;
			xEnd = yEnd;
			yEnd = temp;
			lang.nextStep();

			grid.swapXY();
		}
		preconditions.unhighlight(2);
		preconditions.unhighlight(4);

		if (xStart > xEnd) {
			// swap(x0, x1)
			preconditions.highlight(6);
			preconditions.highlight(7);
			explain.addCodeLine("Negative Zahlen: tausche  x0 und x1", null, 0,
					null);
			int temp = xStart;
			xStart = xEnd;
			xEnd = temp;
			lang.nextStep();

			// swap(y0, y1)

			preconditions.unhighlight(7);
			preconditions.highlight(8);
			explain.addCodeLine("und tausche  y0 und y1", null, 0, null);
			temp = yStart;
			yStart = yEnd;
			yEnd = temp;
			lang.nextStep();
		}
		preconditions.unhighlight(6);
		preconditions.unhighlight(8);

		preconditions.highlight(10);
		double dx = xEnd - xStart; // dx := x1 - x0
		explain.addCodeLine("dx = " + xEnd + " - " + xStart + " = " + dx, null,
				0, null);
		lang.nextStep();
		preconditions.unhighlight(10);
		preconditions.highlight(11);
		double dy = yEnd - yStart; // dy := y1 - y0
		explain.addCodeLine("dy = " + yEnd + " - " + yStart + " = " + dy, null,
				0, null);
		lang.nextStep();
		preconditions.unhighlight(11);
		preconditions.highlight(12);
		double gradient = dy / dx; // gradient := dy / dx
		DecimalFormat df = new DecimalFormat("#.###");
		explain.addCodeLine("gradient = dy / dx = "
				+ df.format(gradient).replace(',', '.'), null, 0, null);
		lang.nextStep();

		preconditions.hide();
		preconditionsFrame.hide();

		interyText.show();
		xGapText.show();
		xpxlText.show();
		ypxlText.show();

		// handle first endpoint:
		endpoints.show();
		endpointsFrame.show();

		plotText.show();

		endpoints.highlight(1);
		int xend = round(xStart); // xend := round(x0)
		lang.nextStep("Erste Endpunkte setzen");

		endpoints.unhighlight(1);
		endpoints.highlight(2);
		double yend = yStart + gradient * (xend - xStart); // yend := y0 +
															// gradient *(xend -
															// x0)
		lang.nextStep();

		endpoints.unhighlight(2);
		endpoints.highlight(3);
		double xgap = rfpart(xStart + 0.5); // xgap := rfpart(x0 + 0.5)
		refreshXgap(xgap);

		lang.nextStep();

		endpoints.unhighlight(3);
		endpoints.highlight(4);
		int xpxl1 = xend; // xpxl1 := xend //this will be used in the main loop
		refreshxpxl("xpxl1", xpxl1);
		lang.nextStep();

		endpoints.unhighlight(4);
		endpoints.highlight(5);
		int ypxl1 = ipart(yend); // ypxl1 := ipart(yend)
		refreshypxl("ypxl1", ypxl1);

		lang.nextStep();

		endpoints.unhighlight(5);

		if (steep) { // if steep then
			endpoints.highlight(6);
			endpoints.highlight(7);
			setFilledRectColorDepth(rfpart(yend) * xgap); // Set Rectangle Fill
															// Color
			refreshVariables(xpxl1, ypxl1, rfpart(yend) * xgap);
			moveCursor(ypxl1, xpxl1);
			// plot the pixel at (x, y) with brightness c
			lineRects[ypxl1 + grid.getxLeftOffset()] = grid.getCellAsRect(
					ypxl1, xpxl1, filledRectProps, ""); // plot(ypxl1, xpxl1,
														// rfpart(yend) * xgap)

			lang.nextStep("Algorithmus y= " + (int) Math.round(ypxl1));

			endpoints.unhighlight(7);
			endpoints.highlight(8);
			setFilledRectColorDepth(fpart(yend) * xgap);
			refreshVariables((int) Math.round(ypxl1 + 1),
					(int) Math.round(xpxl1), fpart(yend) * xgap);
			moveCursor((int) Math.round(ypxl1 + 1), (int) Math.round(xpxl1));
			lineRects[(int) Math.round(ypxl1 + 1) + grid.getxLeftOffset()] = grid
					.getCellAsRect((int) Math.round(ypxl1 + 1),
							(int) Math.round(xpxl1), filledRectProps, ""); // plot(ypxl1+1,
																			// xpxl1,
																			// fpart(yend)
																			// *
																			// xgap)
			lang.nextStep("Algorithmus y= " + (int) Math.round(ypxl1 + 1));
			endpoints.unhighlight(6);
			endpoints.unhighlight(8);
		} else {
			endpoints.highlight(9);
			endpoints.highlight(10);
			setFilledRectColorDepth(rfpart(yend) * xgap);
			refreshVariables((int) Math.round(xpxl1), (int) Math.round(ypxl1),
					rfpart(yend) * xgap);
			moveCursor((int) Math.round(xpxl1), (int) Math.round(ypxl1));
			lineRects[(int) Math.round(xpxl1) + grid.getxLeftOffset()] = grid
					.getCellAsRect((int) Math.round(xpxl1),
							(int) Math.round(ypxl1), filledRectProps, ""); // plot(xpxl1,
																			// ypxl1
																			// ,
																			// rfpart(yend)
																			// *
																			// xgap)

			lang.nextStep("Algorithmus x= " + (int) Math.round(xpxl1));

			endpoints.unhighlight(10);
			endpoints.highlight(11);
			setFilledRectColorDepth(fpart(yend) * xgap);
			refreshVariables((int) Math.round(xpxl1),
					(int) Math.round(ypxl1 + 1), fpart(yend) * xgap);
			moveCursor((int) Math.round(xpxl1), (int) Math.round(ypxl1 + 1));
			lineRects[(int) Math.round(xpxl1) + grid.getxLeftOffset()] = grid
					.getCellAsRect((int) Math.round(xpxl1),
							(int) Math.round(ypxl1 + 1), filledRectProps, ""); // plot(xpxl1,
																				// ypxl1+1,
																				// fpart(yend)
																				// *
																				// xgap)
			lang.nextStep("Algorithmus x= " + (int) Math.round(xpxl1));
			endpoints.unhighlight(9);
			endpoints.unhighlight(11);
		}// end if

		endpoints.highlight(13);
		double intery = yend + gradient;
		refreshIntery(intery);
		lang.nextStep();

		// handle second endpoint:
		endpoints.unhighlight(13);
		endpoints.highlight(16);
		xend = round(xEnd); // end := round(x1)
		lang.nextStep("Zweite Endpunkte setzen");

		endpoints.unhighlight(16);
		endpoints.highlight(17);
		yend = yEnd + gradient * (xend - xEnd); // yend := y1 + gradient * (xend
												// -x1)
		lang.nextStep();

		endpoints.unhighlight(17);
		endpoints.highlight(18);
		xgap = fpart(xEnd + 0.5); // xgap := fpart(x1 + 0.5)
		refreshXgap(xgap);
		lang.nextStep();

		endpoints.unhighlight(18);
		endpoints.highlight(19);
		int xpxl2 = xend; // xpxl2 := xend //this will be used in the main loop
		refreshxpxl("xpxl2", xpxl2);
		lang.nextStep();

		endpoints.unhighlight(19);
		endpoints.highlight(20);
		int ypxl2 = ipart(yend); // ypxl2 := ipart(yend)#
		refreshypxl("ypxl2", ypxl2);
		lang.nextStep();

		endpoints.unhighlight(20);

		if (steep) { // if steep then
			endpoints.highlight(21);
			endpoints.highlight(22);

			setFilledRectColorDepth(rfpart(yend) * xgap);
			refreshVariables((int) Math.round(xpxl2), (int) Math.round(ypxl2),
					rfpart(yend) * xgap);
			moveCursor((int) Math.round(ypxl2), (int) Math.round(xpxl2));
			lineRects[(int) Math.round(ypxl2) + grid.getxLeftOffset()] = grid
					.getCellAsRect((int) Math.round(ypxl2),
							(int) Math.round(xpxl2), filledRectProps, ""); // plot(ypxl2,xpxl2,rfpart(yend)*xgap)
			lang.nextStep("Algorithmus y= " + (int) Math.round(ypxl2));

			endpoints.unhighlight(22);
			endpoints.highlight(23);
			setFilledRectColorDepth(fpart(yend) * xgap);
			refreshVariables((int) Math.round(xpxl2),
					(int) Math.round(ypxl2 + 1), fpart(yend) * xgap);
			moveCursor((int) Math.round(ypxl2 + 1), (int) Math.round(xpxl2));
			lineRects[(int) Math.round(ypxl2 + 1) + grid.getxLeftOffset()] = grid
					.getCellAsRect((int) Math.round(ypxl2 + 1),
							(int) Math.round(xpxl2), filledRectProps, ""); // plot(ypxl2+1,xpxl2,fpart(yend)*xgap)
			lang.nextStep("Algorithmus y= " + (int) Math.round(ypxl2 + 1));

			endpoints.unhighlight(23);

		} else { // else
			endpoints.highlight(24);
			endpoints.highlight(25);
			setFilledRectColorDepth(rfpart(yend) * xgap);
			refreshVariables((int) Math.round(xpxl2), (int) Math.round(ypxl2),
					rfpart(yend) * xgap);
			moveCursor((int) Math.round(xpxl2), (int) Math.round(ypxl2));
			lineRects[(int) Math.round(xpxl2) + grid.getxLeftOffset()] = grid
					.getCellAsRect((int) Math.round(xpxl2),
							(int) Math.round(ypxl2), filledRectProps, ""); // plot(xpxl2,ypxl2,rfpart(yend)*xgap
			lang.nextStep("Algorithmus x= " + (int) Math.round(xpxl2));

			endpoints.unhighlight(25);
			endpoints.highlight(26);
			setFilledRectColorDepth(fpart(yend) * xgap);
			refreshVariables((int) Math.round(xpxl2),
					(int) Math.round(ypxl2 + 1), fpart(yend) * xgap);
			moveCursor((int) Math.round(xpxl2), (int) Math.round(ypxl2 + 1));
			lineRects[(int) Math.round(xpxl2) + grid.getxLeftOffset()] = grid
					.getCellAsRect((int) Math.round(xpxl2),
							(int) Math.round(ypxl2 + 1), filledRectProps, ""); // plot(xpxl2,ypxl2+1,fpart(yend)*xgap)

			lang.nextStep("Algorithmus x= " + (int) Math.round(xpxl2));

			endpoints.unhighlight(24);
			endpoints.unhighlight(26);
		}// end if

		endpoints.hide();
		endpointsFrame.hide();
		xpxlText.hide();
		ypxlText.hide();
		xGapText.hide();

		// main loop:
		xiaolin.show();
		xiaolinCodeFrame.show();

		xiaolin.highlight(1);
		for (double x = xpxl1 + 1; x < xpxl2; x++) { // for x from xpxl1 +1 to
														// xpxl2 - 1 do
			if (steep) { // if steep then
				xiaolin.highlight(2);
				xiaolin.highlight(3);
				setFilledRectColorDepth(rfpart(intery));
				refreshVariables((int) Math.round(x), ipart(intery),
						rfpart(intery));
				moveCursor(ipart(intery), (int) Math.round(x));
				lineRects[(int) Math.round(ipart(intery))
						+ grid.getxLeftOffset()] = grid
						.getCellAsRect(ipart(intery), (int) Math.round(x),
								filledRectProps, ""); // plot(ipart(intery),x,rfpart(intery))
				lang.nextStep("Algorithmus y= " + ipart(intery));

				xiaolin.unhighlight(3);
				xiaolin.highlight(4);
				setFilledRectColorDepth(fpart(intery));
				refreshVariables((int) Math.round(x), ipart(intery) + 1,
						fpart(intery));
				moveCursor(ipart(intery) + 1, (int) Math.round(x));
				lineRects[(int) Math.round(ipart(intery) + 1)
						+ grid.getxLeftOffset()] = grid.getCellAsRect(
						ipart(intery) + 1, (int) Math.round(x),
						filledRectProps, ""); // plot(ipart(intery)+1, x,
												// fpart(intery))
				lang.nextStep("Algorithmus y= " + (ipart(intery) + 1));

				xiaolin.unhighlight(2);
				xiaolin.unhighlight(4);
			} else { // else
				xiaolin.highlight(5);
				xiaolin.highlight(6);
				setFilledRectColorDepth(rfpart(intery));
				refreshVariables((int) Math.round(x), ipart(intery),
						rfpart(intery));
				moveCursor((int) Math.round(x), ipart(intery));
				lineRects[(int) Math.round(x) + grid.getxLeftOffset()] = grid
						.getCellAsRect((int) Math.round(x), ipart(intery),
								filledRectProps, ""); // plot(x, ipart (intery),
														// rfpart(intery))
				lang.nextStep("Algorithmus x= " + (int) Math.round(x));

				xiaolin.unhighlight(6);
				xiaolin.highlight(7);
				setFilledRectColorDepth(fpart(intery));
				refreshVariables((int) Math.round(x), ipart(intery) + 1,
						fpart(intery));
				moveCursor((int) Math.round(x), ipart(intery) + 1);
				lineRects[(int) Math.round(x) + grid.getxLeftOffset()] = grid
						.getCellAsRect((int) Math.round(x), ipart(intery) + 1,
								filledRectProps, ""); // plot(x,
														// ipart
														// (intery)+1,
														// fpart(intery))
				lang.nextStep("Algorithmus x= " + (int) Math.round(x));

				xiaolin.unhighlight(5);
				xiaolin.unhighlight(7);
			}// end if

			xiaolin.highlight(9);
			intery = intery + gradient;// intery := intery + gradient
			refreshIntery(intery);
			lang.nextStep();
			xiaolin.unhighlight(9);
		} // end for

		xiaolin.hide();
		xiaolinCodeFrame.hide();
	}

	private void outro() {

		outro.show();

		Text link = lang.newText(new Offset(0, 50, "outro",
				AnimalScript.DIRECTION_SW),
				"http://en.wikipedia.org/wiki/Xiaolin_Wu's_line_algorithm",
				"link", null, auxTextProps);
		Text link2 = lang.newText(new Offset(0, 10, "link",
				AnimalScript.DIRECTION_SW),
				"http://rosettacode.org/wiki/Xiaolin_Wu's_line_algorithm",
				"link2", null, auxTextProps);

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

	// oe = &ouml; ss = &szlig;
	public String getDescription() {
		return "Xiaolin Wu's Line Algorithmus berechnet\n"
				+ "f&uuml;r einen Anfangs- und Endpunkt eine Linie auf einem Raster\n"
				+ "die beide Punkte verbindet und weniger Abs&auml;tze (Treppen) hat. "
				+ "Es werden w&auml;hrend dem Ablauf in jedem Durchlauf zwei Punkte gesetzt\n"
				+ "und ihre Farbe abh&auml;ngig vom Abstand zur idealen Linie eingef&auml;rbt.";
	}

	public String getCodeExample() {
		return "private int ipart(Double x) {\n"
				+ "\treturn x.intValue(); // return 'integer part of x'\n"
				+ "}\n"

				+ "private double round(double x) {\n"
				+ "\treturn Math.round(x);\n" + "}\n"

				+ "private double fpart(double x) {\n"
				+ "\t	return x % 1; // return 'fractional part of x'\n" + "}\n"

				+ "private double rfpart(double x) {\n"
				+ "\treturn 1 - fpart(x); // return 1 - fpart(x)\n" + "}\n"

				+ "public void drawLine(x0,y0,x1,y1){\n"
				+ "\tboolean steep := Math.abs(y1 - y0) > Math.abs(x1 - x0)\n"

				+ "\tif(steep){\n" + "\t\tswap(x0, y0);\n"
				+ "\t\tswap(x1, y1);\n" + "\t}\n" + "\tif(x0 > x1){\n"
				+ "\t\tswap(x0, x1);\n" + "\t\tswap(y0, y1);\n" + "\t}\n"

				+ "\tint dx = x1 - x0;\n" + "\tint dy = y1 - y0;\n"
				+ "\tdouble gradient := dy / dx;\n \n"

				+ "\t// handle first endpoint\n" + "\tint xend = round(x0);\n"
				+ "\tdouble yend = y0 + gradient * (xend - x0);\n"
				+ "\tdouble xgap = rfpart(x0 + 0.5);\n"
				+ "\tint xpxl1 = xend;   \n" + "\tint ypxl1 = ipart(yend);\n"
				+ "\tif(steep) {\n"
				+ "\t\tplot(ypxl1,   xpxl1, rfpart(yend) * xgap);\n"
				+ "\t\tplot(ypxl1+1, xpxl1,  fpart(yend) * xgap);\n"
				+ "\t} else {\n"
				+ "\t\tplot(xpxl1, ypxl1  , rfpart(yend) * xgap);\n"
				+ "\t\tplot(xpxl1, ypxl1+1,  fpart(yend) * xgap);\n" + "\t}\n"
				+ "\tdouble intery := yend + gradient; \n \n"

				+ "\t// handle second endpoint\n"

				+ "\txend = round(x1)\n"
				+ "\tyend = y1 + gradient * (xend - x1);\n"
				+ "\txgap = fpart(x1 + 0.5);\n" + "\tint xpxl2 = xend; \n"
				+ "\tint ypxl2 = ipart(yend);\n" + "\tif(steep) {\n"
				+ "\t\tplot(ypxl2  , xpxl2, rfpart(yend) * xgap);\n"
				+ "\t\tplot(ypxl2+1, xpxl2,  fpart(yend) * xgap);\n"
				+ "\t} else {\n"
				+ "\t\tplot(xpxl2, ypxl2,  rfpart(yend) * xgap);\n"
				+ "\t\tplot(xpxl2, ypxl2+1, fpart(yend) * xgap);\n"
				+ "\t}\n \n"

				+ "\t// main loop\n"
				+ "\tfor(int x = xpxl1 + 1; x < xpxl2; x++){\n"
				+ "\tif(steep){\n"
				+ "\t\tplot(ipart(intery)  , x, rfpart(intery));\n"
				+ "\t\tplot(ipart(intery)+1, x,  fpart(intery));\n"
				+ "\t} else {\n"
				+ "\t\tplot(x, ipart (intery),  rfpart(intery));\n"
				+ "\t\tplot(x, ipart (intery)+1, fpart(intery));\n" + "\t}\n"
				+ "\tintery = intery + gradient;\n" + "\t}\n" + "}";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	public String getName() {
		return "Xiaolin Wu's Line Algorithm";
	}

	public String getAlgorithmName() {
		return "Xiaolin Wu's Line Algorithm";
	}

	public String getAnimationAuthor() {
		return "Robert Cibulla, Peter Schauberger";
	}

	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) throws IllegalArgumentException {
		int yStart = (Integer) arg1.get("yStart");
		int yEnd = (Integer) arg1.get("yEnd");
		int xStart = (Integer) arg1.get("xStart");
		int xEnd = (Integer) arg1.get("xEnd");
		if (yStart < 0 || yEnd < 0 || xStart < 0 || xEnd < 0) {
			JOptionPane
					.showMessageDialog(
							null,
							"Es sind nur Werte im ersten Quadranten erlaubt: xStart, yStart, xEnd, yEnd >= 0");
			return false;
		}
		if (yStart == yEnd && yEnd == xStart && xStart == xEnd) {
			JOptionPane.showMessageDialog(null,
					"Ein Punkt muss nicht Antialiased werden.");
			return false;
		}
		return true;
	}
}
