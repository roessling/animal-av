/*
 * Lagrange.java
 * Lukas Becker, Simon Bohlender, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.InfoBox;
import algoanim.exceptions.NotEnoughNodesException;
import algoanim.primitives.Polygon;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolygonProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.generatorframe.controller.GraphPanelListener;
import generators.maths.function.Function;

public class Lagrange implements Generator {
	private Language lang;
	private String function;
	private double graphXEnd;
	private int[] xStuetz;
	private double graphXStart;
	private double maxY;
	
	private PolylineProperties graphColorProp;
	private PolylineProperties graphInterpolColorProp;
	private SourceCodeProperties scHighlightColor;

	int xSize = 300;
	int ySize = 100;
	int xStart = 20;
	int yStart = 70;

	int xPosYaxis = 0;

	private double stepSize;

	int numPoints;
	int distanceX;
	int distanceY;
	
	int xPosCalc = xStart + xSize + 100;

	private Map<Double, Double> values;
	private Map<Double, Double> interpolValues = new TreeMap<Double, Double>();
	double[][] stuetz;

	int graphCount = 0;
	
	LinkedList<Primitive> graph = new LinkedList<Primitive>();
	LinkedList<Primitive> stuetzList = new LinkedList<Primitive>();
	LinkedList<Primitive> liList = new LinkedList<Primitive>();
	LinkedList<Primitive> pxList = new LinkedList<Primitive>();

	public void init() {
		graphCount = 0;
		xPosYaxis = 0;
		lang = new AnimalScript("Lagrange Interpolation", "Lukas Becker, Simon Bohlender", 800, 600);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		function = (String) primitives.get("Function");
		graphXEnd = (double) primitives.get("xMax");
		xStuetz = (int[]) primitives.get("Stuetzstellen");
		graphXStart = (double) primitives.get("xMin");
		maxY = (double) primitives.get("yRange");
		graphColorProp = (PolylineProperties)props.getPropertiesByName("Farbe Graph");
		graphInterpolColorProp = (PolylineProperties)props.getPropertiesByName("Farbe Interpolations-Graph");
		scHighlightColor = (SourceCodeProperties) props.getPropertiesByName("Source Code");
		
		if (graphXStart < 0) {
			xPosYaxis = (int) (xSize / (Math.abs(graphXEnd) + Math.abs(graphXStart)) * Math.abs(graphXStart));
			System.out.println(xPosYaxis);
		}
		draw();

		return lang.toString();
	}

	public String getName() {
		return "Lagrange Interpolation";
	}

	public String getAlgorithmName() {
		return "Lagrange Interpolation";
	}

	public String getAnimationAuthor() {
		return "Lukas Becker, Simon Bohlender";
	}

	public String getFullDescription(){
		String d = " \nDie Lagrange Interpolation ist ein Verfahren, um ein Polynom zu bestimmen, das genau\n"
			+ "durch eine angegebene Menge von Punkten verlaeuft. \n"
			+ "Durch eine geeignete Wahl von Stuetzstellen kann somit eine unbekannte Funktion angenaehert werden. \n"
			+ "Die Lagrangesche Interpolationsformel bietet den Vorteil, dass ein Interpolationspolynom \n"
			+ "schnell und einfach berechnet werden kann. \n"
			+ "Allerding gibt es zugleich den Nachteil, dass bei einer Hinzunahme weiterer Stuetzstellen \n"
			+ "alle Basisfunktionen l_i erneut berechnet werden muessen. \n"
			+ "Folgende Formeln werden benoetigt, um das Interpolationspolynom zu berechnen:";
		return d;
	}
	
	public String getDescription() {
		return "Die Lagrange Interpolation ist ein Verfahren, um ein Polynom P(x) zu bestimmen, das eine unbekannte "
				+ "\n" + "Funktion an einer gegebenen Menge von Stuetzstellen (Wertepaare aus x und f(x)) annaehert." + "\n"
				+ "Dazu werden in jedem Schritt zunaechst die Lagrangepolynome l_i bestimmt, die im letzten Schritt zum"
				+ "\n" + "Interpolationspolynom P(x) zusammengesetzt werden." + "\n\n"
				+ "In den Primitives koennen folgende Werte gesetzt werden: \n"
				+ "Function: zu interpolierende Funktion (bestehend aus sin(x) cos(x) tan(x) pow(x,y) sqrt(x) pi e ( ) + - * /) \n"
				+ "xMin: x Position, ab der der Graph gezeichnet wird\n"
				+ "xMax: x Position, bis zu der der Graph gezeichnet wird\n"
				+ "Stuetzstellen: Array mit x Werten der Stuetzstellen\n"
				+ "yRange: y Wert, bis zu dem der Graph gezeichnet wird";
	}

	public String getCodeExample() {
		return "calcLagrangeIterpolation(stuetzen)" + "\n" 
				+ "    P(x) = 0" + "\n"
				+ "    for(i in 0 to stuetzen.length-1)" + "\n" 
				+ "        l_i = 1" + "\n"
				+ "        for(j in 0 to stuetzen.length-1)" + "\n" 
				+ "            if(j!=i)" + "\n"
				+ "                l_i *= ((x - stuetzen[j].x))/(stuetzen[i].x - stuetzen[j].x)" + "\n"
				+ "        f_i = stuetzen[i].y" + "\n" 
				+ "        P(x) += f_i * l_i" + "\n" 
				+ "    return P(x)";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	/**
	 * calculates f(x) for each x stuetzstelle
	 * 
	 * @param x
	 *            stuetzstellen
	 */
	public void initStuetzstellen(double[] x) {
		stuetz = new double[2][x.length];
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");

		for (int i = 0; i < x.length; i++) {
			double xShort = x[i];
			xShort = Math.round(1000.0 * xShort) / 1000.0;
			stuetz[0][i] = xShort;
			Function initFunc = new Function(lang, "init");
			initFunc.setParsedFunction(function);
			String[][] replace = { { "x" }, { xShort + "" } };
			double y = initFunc.calculate(replace);
			stuetz[1][i] = Math.round(1000.0 * y) / 1000.0;
		}
	}

	/**
	 * Calculates a resultset for a given formular in order to draw the plot
	 * 
	 * @param f
	 *            function
	 * @return map whith x,y values
	 */
	public Map<Double, Double> calcFunction(String f) {
		f = f.replaceAll(" ", "");
		Map<Double, Double> v = new TreeMap<Double, Double>();
		for (double k = graphXStart; k <= graphXEnd; k += stepSize) {
			Function initFunc = new Function(lang, "init");
			initFunc.setParsedFunction(f);
			String[][] replace = { { "x" }, { k + "" } };
			double y = initFunc.calculate(replace);
			y = Math.round(1000.0 * y) / 1000.0;
			v.put(k, y);
		}
		return v;
	}

	/**
	 * Draws a given Graph
	 * 
	 * @param vals
	 *            x, y values
	 * @param col
	 *            Color of Graph
	 * @param func
	 *            function which is drawn
	 */
	public void drawGraph(Map<Double, Double> vals, PolylineProperties pProb, String func, String label) {
		Set<Entry<Double, Double>> entries = vals.entrySet();
		Iterator<Entry<Double, Double>> it = entries.iterator();
		Entry<Double, Double> entry = it.next();
		double x1 = entry.getKey();
		double y1 = entry.getValue();
		int i = 0;
		while (it.hasNext()) {
			Coordinates start = new Coordinates((int) (xStart + (i * distanceX)),
					(int) (yStart + ySize - y1 * distanceY));
			Entry<Double, Double> entry2 = it.next();
			double x2 = entry2.getKey();
			double y2 = entry2.getValue();
			Coordinates end = new Coordinates((int) (xStart + ((i + 1) * distanceX)),
					(int) (yStart + ySize - y2 * distanceY));
			String name = "line" + i;
			if (yStart + ySize - y2 * distanceY < ySize * 2 + yStart + 20
					&& yStart + ySize - y1 * distanceY < ySize * 2 + yStart + 20 && yStart + ySize - y2 * distanceY > 0
					&& yStart + ySize - y1 * distanceY > 0) {
				Polyline l = lang.newPolyline(new Node[] { start, end }, name, null, pProb);
				graph.add(l);
			}
			x1 = x2;
			y1 = y2;
			i++;
		}

		// draw info box
		PolygonProperties boxProb = new PolygonProperties();
		boxProb.set(AnimationPropertiesKeys.COLOR_PROPERTY, pProb.get(AnimationPropertiesKeys.COLOR_PROPERTY));
		Coordinates upLeft = new Coordinates(xStart + xSize - 50, yStart + graphCount * 30);
		Coordinates upRight = new Coordinates(xStart + xSize - 30 + func.length() * 7, yStart + graphCount * 30);
		Coordinates downLeft = new Coordinates(xStart + xSize - 50, yStart + 25 + graphCount * 30);
		Coordinates downRight = new Coordinates(xStart + xSize - 30 + func.length() * 7, yStart + 25 + graphCount * 30);
		xPosCalc = Math.max(xStart + xSize - 30 + func.length() * 7 + 20, xPosCalc);
		try {
			Polygon p = lang.newPolygon(new Node[] { upLeft, upRight, downRight, downLeft }, "functionPoly", null, boxProb);
			graph.add(p);
		} catch (NotEnoughNodesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TextProperties boxTextProp = new TextProperties();
		boxTextProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, pProb.get(AnimationPropertiesKeys.COLOR_PROPERTY));
		boxTextProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		lang.newText(upLeft, "", "spacer", null);
		Text boxText = lang.newText(new Coordinates(xStart + xSize - 40, yStart + 3 + graphCount * 30), func, "functionText", null, boxTextProp);
		graph.add(boxText);
		lang.nextStep(label);

		graphCount++;
	}

	/**
	 * main function which generates the whole animal script
	 */
	public void draw() {
		drawInitPage();
		stepSize = (graphXEnd - graphXStart) / 125;
		// generate plot points
		values = calcFunction(function);

		numPoints = values.size();
		distanceX = xSize / numPoints;
		distanceY = (int) (ySize / maxY);

		// draw axis
		PolylineProperties yaxProb = new PolylineProperties();
		yaxProb.set(AnimationPropertiesKeys.BWARROW_PROPERTY, true);
		PolylineProperties xaxProb = new PolylineProperties();
		xaxProb.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		Node[] yAxisNodes = new Node[2];
		yAxisNodes[0] = new Coordinates(xStart + xPosYaxis, yStart);
		yAxisNodes[1] = new Coordinates(xStart + xPosYaxis, ySize * 2 + yStart);
		Polyline yAxis = lang.newPolyline(yAxisNodes, "xax", null, yaxProb);
		Node[] xAxisNodes = new Node[2];
		xAxisNodes[0] = new Coordinates(xStart, ySize + yStart);
		xAxisNodes[1] = new Coordinates(xSize + xStart, ySize + yStart);
		Polyline xAxis = lang.newPolyline(xAxisNodes, "yax", null, xaxProb);
		graph.add(yAxis);
		graph.add(xAxis);

		// draw sin
		drawGraph(values, graphColorProp, function, "Graph zeichnen");

		// Draw Stuetzstellen
		initStuetzstellen(Arrays.stream(xStuetz).asDoubleStream().toArray());
		MatrixProperties mat = new MatrixProperties();
		mat.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
		mat.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
		Text stuetzHead = lang.newText(new Coordinates(xStart, ySize * 2 + 20 + yStart), "Stuetzstellen:", "stPoints", null);
		stuetzList.add(stuetzHead);
		
		String[][] stuetzAnzeige = new String[stuetz.length][stuetz[0].length + 1];
		stuetzAnzeige[0][0] = "x";
		stuetzAnzeige[1][0] = "y";
		int length = 0;
		for (int n = 0; n < stuetz[0].length; n++) {
			for (int o = 0; o < stuetz.length; o++) {
				String toStr = String.valueOf(stuetz[o][n]) + "   ";
				stuetzAnzeige[o][n + 1] = toStr;
			}
			length += Math.max(String.valueOf(stuetz[0][n]).length(), String.valueOf(stuetz[1][n]).length()) + 3;
		}
		length = (int)(length * 7.7 + 20);
		xPosCalc = Math.max(xStart + length + 20, xPosCalc);
		StringMatrix stuetzMatrix = lang.newStringMatrix(new Coordinates(xStart, ySize * 2 + yStart + 40), stuetzAnzeige, "stuetz", null, mat);
		stuetzList.add(stuetzMatrix);
		
		Node[] stLine = new Node[2];
		stLine[0] = new Coordinates(xStart - 5, ySize * 2 + yStart + 40);
		stLine[1] = new Coordinates(xStart + length, ySize * 2 + yStart + 40);
		Polyline stuetzBorder = lang.newPolyline(stLine, "xst1", null);
		stuetzList.add(stuetzBorder);
		stLine[0] = new Coordinates(xStart - 5, ySize * 2 + yStart + 67);
		stLine[1] = new Coordinates(xStart + length, ySize * 2 + yStart + 67);
		stuetzBorder = lang.newPolyline(stLine, "xst2", null);
		stuetzList.add(stuetzBorder);
		stLine[0] = new Coordinates(xStart - 5, ySize * 2 + yStart + 94);
		stLine[1] = new Coordinates(xStart + length, ySize * 2 + yStart + 94);
		stuetzBorder = lang.newPolyline(stLine, "xst3", null);
		stuetzList.add(stuetzBorder);
		stLine[0] = new Coordinates(xStart - 5, ySize * 2 + yStart + 40);
		stLine[1] = new Coordinates(xStart - 5, ySize * 2 + 60 + yStart + 34);
		stuetzBorder = lang.newPolyline(stLine, "xst4", null);
		stuetzList.add(stuetzBorder);
		stLine[0] = new Coordinates(xStart + 25, ySize * 2 + yStart + 40);
		stLine[1] = new Coordinates(xStart + 25, ySize * 2 + yStart + 94);
		stuetzBorder = lang.newPolyline(stLine, "xst5", null);
		stuetzList.add(stuetzBorder);
		stLine[0] = new Coordinates(xStart + length, ySize * 2 + yStart + 40);
		stLine[1] = new Coordinates(xStart + length, ySize * 2 + yStart + 94);
		stuetzBorder = lang.newPolyline(stLine, "xst6", null);
		stuetzList.add(stuetzBorder);
		lang.nextStep("Stuetzstellen");

		// Draw Formulars:
		Function px = new Function(lang, "px");
		px.drawFunction("P(x) = \\sum{i=0}{N}\\ \\index{f}{}{i}\\ \\index{l}{}{i}\\ (x)", xStart, ySize * 2 + yStart + 130);
		Function li = new Function(lang, "li");
		li.drawFunction(
				"\\index{l}{}{i}\\ (x) = \\mult{j=0}{j≠i}{n}\\ \\frac{x - \\index{x}{}{j}\\}{\\index{x}{}{i}\\-\\index{x}{}{j}\\}\\",
				xStart, ySize * 2 + yStart + 180);
		lang.nextStep("benoetigte Formeln");

		// Draw Sourcecode
		SourceCode sc = drawCode(xStart, ySize * 2 + yStart + 250, getCodeExample(), true, new Color(220, 220, 220), "sc1");
		lang.nextStep("SourceCode");

		// Generate Interpolation
		calcInterpolation(stuetz, sc, xPosCalc, yStart);
		// Draw Intrpolation
		drawGraph(interpolValues, graphInterpolColorProp, "P(x)", "Interpolations-Graph zeichnen");
		drawSummaryPage();
	}
	
	private void drawSummaryPage(){
		lang.hideAllPrimitives();
		TextProperties headProp = new TextProperties();
		headProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 20));
		lang.newText(new Coordinates(20, 20), "Lagrange Interpolation - Zusammenfassung", "head_summary", null, headProp);
		
		for(Primitive g: graph){
			g.show();
			g.moveBy("translate", 80, 0, new MsTiming(0), new MsTiming(1000));
		}
		for(Primitive s: stuetzList){
			s.show();
			s.moveBy("translate", 70, 0, new MsTiming(0), new MsTiming(1000));
		}
		for(Primitive p: pxList){
			p.show();
			p.moveBy("translate", -350, 315, new MsTiming(0), new MsTiming(1000));
		}
		for(Primitive l: liList){
			l.show();
			l.moveBy("translate", -349, 350, new MsTiming(0), new MsTiming(1000));
		}
		TextProperties sumProp = new TextProperties();
		sumProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		lang.newText(new Coordinates(50+xStart, 350+yStart), "mit folgenden Werten für L[i]:", "text_summary", null, sumProp);
		lang.nextStep("Zusammenfassung");
	}

	private void drawInitPage() {
		TextProperties headProp = new TextProperties();
		headProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 20));
		Text head = lang.newText(new Coordinates(20, 20), "Lagrange Interpolation", "head", null, headProp);
		
		TextProperties standard = new TextProperties();
		standard.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));		
		
		String[] text = getFullDescription().split("\n");
		InfoBox info = new InfoBox(lang, new Coordinates(20, 50), 20, "Einführung");
		info.setText(Arrays.asList(text));
		Function px = new Function(lang, "px");
		px.drawFunction("P(x) = \\sum{i=0}{N}\\ \\index{f}{}{i}\\ \\index{l}{}{i}\\ (x)", 20, 250);
		Function li = new Function(lang, "li");
		li.drawFunction(
				"\\index{l}{}{i}\\ (x) = \\mult{j=0}{j≠i}{n}\\ \\frac{x - \\index{x}{}{j}\\}{\\index{x}{}{i}\\-\\index{x}{}{j}\\}\\",
				20, 305);
		lang.nextStep("Einleitung");
		px.hide();
		li.hide();
		info.hide();
	}

	/**
	 * Generates Animal Steps for visualizing the algorithm
	 * 
	 * @param stuetzen
	 *            stueztstellen
	 * @param sc
	 *            associated sourcecode
	 * @param xpos
	 *            xpos where steps will be drwan
	 * @param ypos
	 *            ypos where steps will be drawn
	 */
	public void calcInterpolation(double[][] stuetzen, SourceCode sc, int xpos, int ypos) {
		int liypos = ypos + 50;

		Function[] lis = new Function[stuetzen[0].length];
		sc.highlight(0);
		lang.nextStep();

		sc.highlight(1);
		sc.unhighlight(0);
		lang.nextStep();

		Function px = new Function(lang, "px1");
		String pxParse = "P(x) =# ";
		px.drawFunction(pxParse, xpos, ypos);
		lang.nextStep();

		sc.highlight(2);
		sc.unhighlight(1);
		lang.nextStep();

		sc.highlight(3);
		sc.unhighlight(2);
		lang.nextStep();

		for (int i = 0; i < stuetzen[0].length; i++) {
			sc.unhighlight(8);
			sc.highlight(3);
			Function li = new Function(lang, "li" + i);
			String liParse = "\\index{l}{}{" + i + "}\\=# 1";
			li.drawFunction(liParse, xpos, liypos);
			lang.nextStep();

			sc.unhighlight(3);
			for (int j = 0; j < stuetzen[0].length; j++) {
				sc.unhighlight(5);
				sc.unhighlight(6);
				sc.highlight(4);
				lang.nextStep();

				if (j != i) {
					sc.unhighlight(4);
					sc.highlight(5);
					lang.nextStep();

					sc.unhighlight(5);
					sc.highlight(6);
					lang.nextStep();

					li.hide();
					li = new Function(lang, "li" + i);
					li.drawFunction(liParse + "\\cdot\\ \\frac{x-\\index{x}{}{" + j + "}\\}{\\index{x}{}{" + i
							+ "}\\ - \\index{x}{}{" + j + "}\\}\\", xpos, liypos);
					lang.nextStep();

					li.hide();
					li = new Function(lang, "li" + i);
					liParse = liParse + "\\cdot\\ \\frac{x-" + stuetzen[0][j] + "}{" + stuetzen[0][i] + " - "
							+ stuetzen[0][j] + "}\\";
					li.drawFunction(liParse, xpos, liypos);
					lang.nextStep();
				} else {
					sc.unhighlight(4);
					sc.highlight(5);
					lang.nextStep();
				}
			}
			lis[i] = li;
			sc.unhighlight(5);
			sc.unhighlight(6);
			sc.highlight(7);
			lang.nextStep();
			double fi = stuetzen[1][i];
			px.hide();
			String pxold = px.getRawFunction();
			String pxParse1 = "";
			if (i == 0) {
				pxParse1 = pxParse + "\\index{f}{}{" + i + "}\\ \\cdot\\ \\index{l}{}{" + i + "}\\";
				pxParse += fi + "\\cdot\\ \\index{l}{}{" + i + "}\\";
			} else {
				pxParse1 = pxParse + "#+#\\index{f}{}{" + i + "}\\ \\cdot\\ \\index{l}{}{" + i + "}\\";
				pxParse += "#+#" + fi + "\\cdot\\ \\index{l}{}{" + i + "}\\";
			}
			px.drawFunction(pxParse1, xpos, ypos);
			lang.nextStep();
			sc.unhighlight(7);
			sc.highlight(8);
			px.hide();
			px = new Function(lang, "px");
			px.drawFunction(pxParse, xpos, ypos);
			liypos += 50;
			lang.nextStep("L["+ i +"] berechnen");
			liList.addAll(li.getElements());
		}
		sc.unhighlight(7);
		sc.unhighlight(8);
		sc.highlight(9);
		calcInterpolGraph(stuetzen, lis, px);
		px.highlight();
		pxList = px.getElements();
		lang.nextStep("Finales Px berechnen");
	}

	/**
	 * calculates P(x) of the generated Interpolation Function for drawing the
	 * graph
	 * 
	 * @param stuetzen
	 *            Stuetzstellen
	 * @param lis
	 *            lagrange polynoms
	 * @param px
	 *            interpolation function
	 */
	private void calcInterpolGraph(double[][] stuetzen, Function[] lis, Function px) {
		String pxFunction = px.getParsed().substring(6);
		String[] liFunctions = new String[lis.length];
		String finalFunction = "";
		for (int i = 0; i < lis.length; i++) {
			liFunctions[i] = lis[i].getParsed().replaceAll("\\(Math.pow\\(.*\\)=", "");
		}

		finalFunction = pxFunction;
		for (int j = 0; j < liFunctions.length; j++) {
			finalFunction = finalFunction.replaceAll("l" + j, "(" + liFunctions[j] + ")");
		}

		interpolValues = calcFunction(finalFunction);
	}

	/**
	 * Draws the Sourcecode
	 * 
	 * @param xpos
	 *            x position
	 * @param ypos
	 *            y position
	 * @param code
	 *            associated sourcecode
	 * @param box
	 *            select if code is surrounded by a box
	 * @param boxcolor
	 *            backround color of the box
	 * @param id
	 *            element name of this block
	 * @return
	 */
	public SourceCode drawCode(int xpos, int ypos, String code, boolean box, Color boxcolor, String id) {
		Coordinates rectUpLeft = new Coordinates(xpos, ypos);
		String[] lines = code.split("\r\n|\r|\n");
		int lineNum = lines.length;
		int longest = 0;
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].length() > longest) {
				longest = lines[i].length();
			}
		}
		Coordinates rectDownRight = new Coordinates((longest + 2) * 7 + 35, ypos + (lineNum + 1) * 17);
		if(stuetz[0].length > 7){
			xPosCalc = Math.max(xPosCalc, (longest + 2) * 7 + 55);
		}
		RectProperties rectProb = new RectProperties();
		rectProb.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProb.set(AnimationPropertiesKeys.FILL_PROPERTY, boxcolor);
		rectProb.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		if (box) {
			Rect rect = lang.newRect(rectUpLeft, rectDownRight, "rectSource", null, rectProb);
		}

		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, scHighlightColor.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode sc = lang.newSourceCode(new Coordinates(xpos + 10, ypos), id, null, scProps);
		for (int k = 0; k < lines.length; k++) {
			String line = lines[k];
			sc.addCodeLine(line, null, 0, null);
		}
		return sc;
	}
}