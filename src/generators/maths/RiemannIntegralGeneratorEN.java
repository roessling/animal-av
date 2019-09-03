package generators.maths;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import algoanim.animalscript.addons.Slide;
import algoanim.animalscript.addons.bbcode.DefaultStyle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.TrueFalseQuestionModel;

public class RiemannIntegralGeneratorEN {

	protected Language lang;
	
	protected PolylineProperties functionProps;
	protected PolylineProperties axisProps;
	protected PolylineProperties scaleProps;
	protected SourceCodeProperties srcProps;
	protected Color hlRect;
	protected Color nlRect;
	protected Color hlInterval;
	protected Color nlInterval;
	
	protected List<Node> nodes;
	protected int[] functionV;
	
	protected List<Primitive> coordObj = new LinkedList<Primitive>();
	protected List<Rect> oberRects = new LinkedList<Rect>();
	protected List<Rect> unterRects = new LinkedList<Rect>();
	protected List<Polyline> Intervals = new LinkedList<Polyline>();
	
	protected int funcNumber = 4;
	protected double a = -10;
	protected double b = 8;
	protected int rectNumber = 10;
	protected double rectWidth;
	
	protected SourceCode src;
	protected Text summe;
	protected Text fläche;
	protected double flächeOber = 0;
	protected double flächeUnter = 0;
	protected Rect current;
	protected Text desc;
	protected Text interval;
	protected Text headline;
	protected Text function;

	int xOff = 250; // coordinate system origin
	int yOff = 250;
	
	protected int qsNr = 1;

	public void init(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {

		a = (double) primitives.get("a");
		b = (double) primitives.get("b");
		rectNumber = (int) primitives.get("rectangleNumber");
		funcNumber = (int) primitives.get("functionNumber");
		
		hlRect = (Color) ((SourceCodeProperties) props.getPropertiesByName("rectangle")).get("highlightColor");
		nlRect = (Color) ((SourceCodeProperties) props.getPropertiesByName("rectangle")).get("color");
		hlInterval = (Color) ((SourceCodeProperties) props.getPropertiesByName("interval")).get("highlightColor");
		nlInterval = (Color) ((SourceCodeProperties) props.getPropertiesByName("interval")).get("color");

		functionProps = (PolylineProperties) props.getPropertiesByName("function");

		axisProps = (PolylineProperties) props.getPropertiesByName("coord");
		scaleProps = axisProps;
		scaleProps.set("bwArrow", false);
		scaleProps.set("fwArrow", false);

		srcProps = (SourceCodeProperties) props.getPropertiesByName("sourceCode");

		src = lang.newSourceCode(new Coordinates(50, 100), "Code", null, srcProps);
		src.addCodeLine("Choosing the interval: ", null, 0, null);
		src.addCodeLine("Drawing the rectangle", null, 0, null);
		src.addCodeLine("Surrface area: ", null, 0, null);
		src.addCodeLine("Addition to the total: ", null, 0, null);
		src.hide();
		
		summe = lang.newText(new Coordinates(145, 144), "", "currentSum", null);
		fläche = lang.newText(new Coordinates(56, 180), "", "gesSum", null);
		current = lang.newRect(new Coordinates(0, 0), new Coordinates(0, 0), "blaa", null);
		interval = lang.newText(new Coordinates(173, 112), "", "interval", null);
		TextProperties head = new TextProperties();
		head.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 35));
		headline = lang.newText(new Coordinates(40,20), "Riemann Integration", "headline", null, head);
		coordObj.add(headline);
		
		fläche.hide();
		summe.hide();
		current.hide();
		interval.hide();
		
		rectWidth = (b - a)/rectNumber;
	}

	/**
	 * Zeigt die Allgemeine Beschreibung für das Riemann Integral
	 */
	public void showDesc() {

		Slide sl = new Slide(lang, "resources/DescriptionEN.txt", "der name", new DefaultStyle());
		sl.hide();
		
		TrueFalseQuestionModel introduction = new TrueFalseQuestionModel("introduction");
		introduction.setPointsPossible(1);
		introduction.setPrompt("Do more rectangles in an interval give a better approximation of the surface area?");
		introduction.setFeedbackForAnswer(true, "True, more rectangles result in a better accuracy.");
		introduction.setFeedbackForAnswer(false, "This is false. More rectangles result in a better accuracy of the surrface area");
		introduction.setCorrectAnswer(true);
		introduction.setNumberOfTries(1);
		lang.addTFQuestion(introduction);
		lang.nextStep();
	}

	/**
	 * Klasse zum Zeichnen des Koordinatensystems
	 */
	public void drawCoord() {
		// Drawing coordinate system
		Node[] xAxis = new Node[2];
		xAxis[0] = new Coordinates(xOff, yOff);
		xAxis[1] = new Coordinates(xOff + 800, yOff);
		coordObj.add(lang.newPolyline(xAxis, "xAxis", null, axisProps));

		Node[] yAxis = new Node[2];
		yAxis[0] = new Coordinates(xOff + 400, yOff + 100);
		yAxis[1] = new Coordinates(xOff + 400, yOff - 200);
		coordObj.add(lang.newPolyline(yAxis, "yAxis", null, axisProps));

		Node[] dist = new Node[2];
		for (int i = -12; i <= 12; i++) {
			dist[0] = new Coordinates(xOff + 400 + i * 29, yOff - 5);
			dist[1] = new Coordinates(xOff + 400 + i * 29, yOff + 5);
			coordObj.add(lang.newPolyline(dist, "xDist" + i, null, scaleProps));
			if(i != 0)
				coordObj.add(lang.newText(new Coordinates(xOff + 397 + i * 29, yOff + 7), "" + i, "" + i, null));
		}

		for (int i = -6; i <= 3; i++) {
			dist[0] = new Coordinates(xOff + 395, yOff + i * 29);
			dist[1] = new Coordinates(xOff + 405, yOff + i * 29);
			coordObj.add(lang.newPolyline(dist, "yDist" + i, null, scaleProps));
			if(i != 0)
				coordObj.add(lang.newText(new Coordinates(xOff + 381, yOff - 7 + i * 29), "" + -1 * i, "" + i, null));
		}
		
		function = lang.newText(new Coordinates(xOff + 350, 10), "Function: " + funcToString(), "functionDefinition", null);
	}

	/**
	 * Klasse zum Zeichnen der Funktion
	 */
	public void drawFunction() {
		functionV = new int[755];
		Node[] nodes = new Node[755];
		// Steps in Pixel, Scaling 1x step = 29 pixels
		for (int i = -377; i <= 377; i++) {
			int x = i + xOff + 400;
			int y = (int) (yOff - function(i / 29d) * 29);
			Node a = new Coordinates(x, y);
			nodes[i + 377] = a;
			functionV[i + 377] = y;
		}
		coordObj.add(lang.newPolyline(nodes, "function", null, functionProps));

		lang.nextStep();
	}

	/**
	 * Die Klasse zum berechnen der Obersumme
	 */
	public void drawOber() {
		src.show();
		desc.setText("Uppersum: ", null, null);
		desc.show();
		lang.nextStep();
		summe.show();
		fläche.show();
		interval.show();
		int left = -1;
		int right = 0;
		
		// Schleife über alle Linkenseiten der Rechtecke
		for (double i =  a; i < b; i += rectWidth) {
			// Schleife um durch die einzelnen Schritte im Code zu gehen
			for (int j = 0; j <= 3; j++) {
				src.highlight(j);
				switch(j) {
				// case 0: Highlighten des aktuellen Intervalls
				case 0: left++;
						right++;
						Intervals.get(left).changeColor("", hlInterval, null, null);
						Intervals.get(right).changeColor("", hlInterval, null, null);
						interval.setText("[" + i + ", " + (i + rectWidth) + "] | Value of the function: ", null, null);
						interval.changeColor("", (Color) srcProps.get("highlightColor"), null, null);
						lang.nextStep();
						questionIntervalOber(function(i), function(i + rectWidth));
						lang.nextStep();
						interval.setText(interval.getText() + (Math.max(function(i), function(i + rectWidth))), null, null);
						break;
				// case 1: Zeichnen des Rechtecks innerhalb des Intervalls
				case 1: current = lang.newRect(
							new Coordinates((int) (xOff + 400 + i * 29), Math.min(
									(int) (yOff - function(i) * 29),
									(int) (yOff - function(i + rectWidth) * 29))),
							new Coordinates((int) (xOff + 400 + (i + rectWidth) * 29), yOff), "rectU" + i, null);
						current.changeColor("", hlRect, null, null);
						break;
				// case 2: Berechnung der Fläche innerhalb des Rechtecks
				case 2: double tmp = (rectWidth * Math.max(function(i), function((i + rectWidth))));
						flächeOber += tmp;
						summe.setText("(Width)" + rectWidth + " * (Height)" + Math.max(function(i), function(i+ rectWidth)) + " = " + tmp, null, null);
						summe.changeColor("", (Color) srcProps.get("highlightColor"), null, null);
						break;
				// case 3: Addieren zur gesamten Summe
				case 3: fläche.setText("Uppersum:       " + flächeOber, null, null);
						fläche.changeColor("", (Color) srcProps.get("highlightColor"), null, null);
						break;
				}
				lang.nextStep();
				// Unhighlighting der Intervals und des SourceCodes
				Intervals.get(left).changeColor("", nlInterval, null, null);
				Intervals.get(right).changeColor("", nlInterval, null, null);
				summe.changeColor("", (Color) srcProps.get("color"), null, null);
				fläche.changeColor("", (Color) srcProps.get("color"), null, null);
				interval.changeColor("", (Color) srcProps.get("color"), null, null);
				src.unhighlight(j);
			}
			current.changeColor("", nlRect, null, null);
			oberRects.add(current);
		}
		QuestionGroupModel intervalOber = new QuestionGroupModel("intervalsOber", 3);
		lang.addQuestionGroup(intervalOber);
		summe.setText("", null, null);
		fläche.setText("", null, null);
		interval.setText("", null, null);
		lang.nextStep();
		
		// hide all Obersummen rects
		for(Rect a : oberRects) {
			a.hide();
		}
	}

	/**
	 * Die Klasse um die Untersumme zu berechnen
	 */
	public void drawUnter() {
		int left = -1;
		int right = 0;
		desc.setText("Lowersum: ", null, null);
		desc.show();
		
		// Schleife über alle Linkenseiten der Rechtecke
		for (double i =  a; i < b; i += rectWidth) {
			// Schleife um durch die einzelnen Schritte im Code zu gehen
			for (int j = 0; j <= 3; j++) {
				src.highlight(j);
				switch(j) {
				// case 0: Highlighten des aktuellen Intervalls
				case 0: left++;
						right++;
						Intervals.get(left).changeColor("", hlInterval, null, null);
						Intervals.get(right).changeColor("", hlInterval, null, null);
						interval.setText("[" + i + ", " + (i + rectWidth) + "] | Value of the function: ", null, null);
						interval.changeColor("", (Color) srcProps.get("highlightColor"), null, null);
						lang.nextStep();
						questionIntervalUnter(function(i), function(i + rectWidth));
						interval.setText(interval.getText() + (Math.min(function(i), function(i + rectWidth))), null, null);
						break;
				// case 1: Zeichnen des Rechtecks innerhalb des Intervalls
				case 1: current = lang.newRect(
						new Coordinates((int) (xOff + 400 + i * 29), Math.max(
								(int) (yOff - function(i) * 29),
								(int) (yOff - function(i + rectWidth) * 29))),
						new Coordinates((int) (xOff + 400 + (i + rectWidth) * 29), yOff), "rectU" + i, null);
						current.changeColor("", hlRect, null, null);
						break;
				// case 2: Berechnung der Fl�che innerhalb des Rechtecks
				case 2: double tmp = (rectWidth * Math.min(function(i), function((i + rectWidth))));
						flächeUnter += tmp;
						summe.setText("(Width)" + rectWidth + " * (Height)" + Math.min(function(i), function(i+ rectWidth)) + " = " + tmp, null, null);
						summe.changeColor("", (Color) srcProps.get("highlightColor"), null, null);
						break;
				// case 3: Addiren zur gesamten Summe
				case 3: fläche.setText("Lowersum:       " + flächeUnter, null, null);
						fläche.changeColor("", (Color) srcProps.get("highlightColor"), null, null);
						break;
				}
				lang.nextStep();
				// Unhighlighting der Intervals und des SourceCodes
				Intervals.get(left).changeColor("", nlInterval, null, null);
				Intervals.get(right).changeColor("", nlInterval, null, null);
				summe.changeColor("", (Color) srcProps.get("color"), null, null);
				fläche.changeColor("", (Color) srcProps.get("color"), null, null);
				interval.changeColor("", (Color) srcProps.get("color"), null, null);
				src.unhighlight(j);
			}
			current.changeColor("", nlRect, null, null);
			unterRects.add(current);
		}
		QuestionGroupModel intervalUnter = new QuestionGroupModel("intervalsUnter", 3);
		lang.addQuestionGroup(intervalUnter);
		// hide everything except the coord system and the function in it
		coordObj.add(function);
		lang.hideAllPrimitivesExcept(coordObj);
		desc.hide();
		lang.nextStep();
	}
	
	/**
	 * Erstellt die Einzeichnung der Intervalle für die Rechtecke.
	 */
	public void makeIntervals() {
		desc = lang.newText(new Coordinates(50, 95), "Now we make the intervals: ", "intvalDesc1", null);
		lang.nextStep();
		int cu = 0;
		Node[] xy = new Node[2];
		for(double i =  a; i <= b; i += rectWidth) {
			// Die Notes f�r die Polyline 20px lang
			xy[0] = new Coordinates((int) (xOff + 400 + i * 29), yOff - 10);
			xy[1] = new Coordinates((int) (xOff + 400 + i * 29), yOff + 10);
			Intervals.add(lang.newPolyline(xy, "ival" + i, null));
			// Polyline f�rben zur besseren Sichbarkeit
			Intervals.get(cu).changeColor("", hlInterval, null, null);
			cu++;
		}
		lang.nextStep();
		desc.hide();
		for(Polyline a : Intervals) {
			a.changeColor("", nlInterval, null, null);
		}
	}
	
	public void oberUnter() {
		Text a = lang.newText(new Coordinates(50, 100), "Addition of upper- and lowersum:", "add", null);
		a.changeColor("", (Color) srcProps.get("highlightColor"), null, null);
		lang.newText(new Coordinates(70, 120), "Uppersum:  " + String.format("%0$,.2f", flächeOber), "ober", null);
		lang.nextStep();
		lang.newText(new Coordinates(70, 140), "Lowersum: " + String.format("%0$,.2f", flächeUnter), "unter", null);
		lang.nextStep();
		lang.newText(new Coordinates(60, 160), "Upper- + lowersum: " + String.format("%0$,.2f", flächeOber + flächeUnter), "gesamt", null);
		lang.nextStep();
		a.changeColor("", (Color) srcProps.get("color"), null, null);
		a = lang.newText(new Coordinates(50, 200), "Building the average: ", "mittel", null);
		a.changeColor("", (Color) srcProps.get("highlightColor"), null, null);
		lang.nextStep();
		lang.newText(new Coordinates(60, 220), "Total / 2 = " + String.format("%0$,.2f", (flächeOber + flächeUnter) / 2), "miit", null);
		lang.nextStep();
		a.changeColor("",(Color) srcProps.get("color"), null, null);
		a = lang.newText(new Coordinates(50, 320), "Thus the Riemann Integral of the function in the interval: [" + this.a + ", " + this.b + "]: ", "somit", null);
		a.changeColor("", (Color) srcProps.get("highlightColor"), null, null);
		Text b = lang.newText(new Coordinates(430, 320), String.format("%0$,.2f", (flächeOber + flächeUnter) / 2), "end", null);
		b.changeColor("", Color.GREEN, null, null);
		lang.nextStep();
		a.changeColor("", (Color) srcProps.get("color"), null, null);
		b.changeColor("", (Color) srcProps.get("color"), null, null);
		lang.nextStep();
	}
	
	public void ending() {
		TrueFalseQuestionModel schnitt = new TrueFalseQuestionModel("schnitt");
		schnitt.setPrompt("In this procedure the average of the upper and lower sum is made, is the approximation more accurate with this?");
		schnitt.setPointsPossible(1);
		schnitt.setCorrectAnswer(true);
		schnitt.setFeedbackForAnswer(true, "Right.");
		schnitt.setFeedbackForAnswer(false, "Sadly this is wrong. The approximation gets more accurate.");
		schnitt.setNumberOfTries(1);
		lang.addTFQuestion(schnitt);
		lang.nextStep();
		
		MultipleChoiceQuestionModel infinity = new MultipleChoiceQuestionModel("infinity");
		infinity.setPrompt("Is there a Mehtod to get the exact surrface area with this algorithm?");
		infinity.addAnswer("No, there is no Method with this algorithm", 0, "This is wrong. With an infinite amount of rectangles you get the exact amount of the surrface area.");
		infinity.addAnswer("With an infinite amount of rectangles", 1, "This answer is right.");
		infinity.setNumberOfTries(1);
		lang.addMCQuestion(infinity);
		lang.nextStep();
		
		TrueFalseQuestionModel negAbs = new TrueFalseQuestionModel("negAbs");
		negAbs.setPrompt("As you might have seen we compare positive and negative values. Would there be a difference in the final result if we compare with absolute values?");
		negAbs.setPointsPossible(1);
		negAbs.setCorrectAnswer(false);
		negAbs.setFeedbackForAnswer(false, "Right, there is no difference if you compare by real values or by absolute values.");
		negAbs.setFeedbackForAnswer(true, "Sadly this is wrong, there is no difference in how you compare.");
		negAbs.setNumberOfTries(1);
		lang.addTFQuestion(negAbs);
		lang.nextStep();
		
		lang.hideAllPrimitivesExcept(headline);
		
		lang.newText(new Coordinates(20, 90), "In a nutshell:", "ending thing", null);
		lang.newText(new Coordinates(20, 120), "The Riemann Integral does not return the correct surrface area, therefore an Error exists:", "ending2", null);
		lang.newText(new Coordinates(20, 150), "Riemann Integral:    " + ((flächeOber + flächeUnter)/2), "ending3", null);
		lang.newText(new Coordinates(20, 165), "Exact surrface area: " + integFunction(), "ending4", null);
		lang.newText(new Coordinates(20, 195), "Error:              ", "ending5", null);
		Text redding = lang.newText(new Coordinates(20, 195), "                    " + ((flächeOber + flächeUnter)/2 - integFunction()), "ending6", null);
		redding.changeColor("", Color.RED, null, null);
		lang.newText(new Coordinates(20, 225), "If you have chosen a good amount of rectangles*, it is very accurate", "ending7", null);
		lang.newText(new Coordinates(20, 255), "*: The amount is relative to the function and the interval value.", "ending8", null);
		
	}

	public String run(AnimationPropertiesContainer props, Hashtable<String, Object> primitives, Language lang) {
		
		this.lang = lang;
		init(props, primitives);
		showDesc();
		drawCoord();
		drawFunction();
		makeIntervals();
		drawOber();
		drawUnter();
		oberUnter();
		ending();
		
		lang.finalizeGeneration();
		return lang.toString();
	}

	/**
	 * Liefert den Funktionswert für den gegebenen x Wert zurück
	 * @param i - der x Wert
	 * @return f(i) - Wobei f abhängig von funcNumber ist.
	 */
	public double function(double i) {
		switch (funcNumber) {
		case 1:
			return -0.05 * Math.pow(i, 2) + 5;
		case 2:
			return 0.1 * (Math.pow((0.38 * i), 3) + Math.pow((0.38 * i), 2) - 15 * (0.38 * i));
		case 3:
			return (1/3d) * i;
		case 4:
			return Math.pow((0.08*i),5) + Math.pow((0.18*i),2);
		case 5:
			return 0.33 * (Math.sin(5 * i) + 1/100d * Math.pow(i, 3)) + 1;
		default:
			return 3;
		}
	}
	
	public String funcToString() {
		switch (funcNumber) {
		case 1:
			return "-0,05x^2 + 5";
		case 2:
			return "0.1 * ((0.38x)^3 + ((0.38x)^2 - 15 * 0.38x)";
		case 3:
			return "1/3 * x";
		case 4:
			return "(0.08x)^5 + (0.19x)^2";
		case 5:
			return "0.33 * (sin(5x) + 1/100 * x^3) + 1";
		default:
			return "3";
		}
	}
	
	public double integFunction() {
		switch(funcNumber) {
		case 1:
			return (5 * b - 0.05/3 * Math.pow(b, 3)) - (5 * a - 0.05/3 * Math.pow(a, 3));
		case 2:
			return (0.0013718 * Math.pow(b, 4) + 0.00481333 * Math.pow(b, 3) -0.285 * Math.pow(b, 2)) - (0.0013718 * Math.pow(a, 4) + 0.00481333 * Math.pow(a, 3) - 0.285 * Math.pow(a, 2));
		case 3:
			return (1/6d * Math.pow(b, 2)) - (1/6d * Math.pow(a, 2));
		case 4:
			return (Math.pow(0.08, 5) * (1/6d) * Math.pow(b, 6) + 0.0120333 * Math.pow(b, 3)) - (Math.pow(0.08, 5) * (1/6d) * Math.pow(a, 6) + 0.0120333 * Math.pow(a, 3));
		case 5:
			return (0.000825 * Math.pow(b, 4) + b - 0.066 * Math.cos(5 * b)) - (0.000825 * Math.pow(a, 4) + a - 0.066 * Math.cos(5 * a));
		default:
			return (3 * b) - (3 * a);
					
		}
	}
	
	public void questionIntervalOber(double left, double right) {
		MultipleChoiceQuestionModel intervalModel = new MultipleChoiceQuestionModel("intervalModel" + qsNr);
		intervalModel.setPrompt("Which value will be chosen?");
		intervalModel.setGroupID("intervalsOber");
		intervalModel.addAnswer(left + "", (left) > (right) ? 1 : 0, (left) > (right) ? "This answer is right. " + left + " will be chosen." : "This answer is wrong, "+ right+"is the right answer.");
		intervalModel.addAnswer(right + "", (left) < (right) ? 1 : 0, (left) < (right) ? "This answer is right. "+ right + " will be chosen." : "This answer is wrong, " + left +"is the right answer.");
		lang.addMCQuestion(intervalModel);
		qsNr++;
	}
	
	public void questionIntervalUnter(double left, double right) {
		MultipleChoiceQuestionModel intervalModel = new MultipleChoiceQuestionModel("intervalModel" + qsNr);
		intervalModel.setPrompt("Which value will be chosen?");
		intervalModel.setGroupID("intervalsUnter");
		intervalModel.addAnswer(left + "", (left) < (right) ? 1 : 0, (left) < (right) ? "This answer is right." + left + " will be chosen." : "This answer is wrong, "+ right+"is the right answer.");
		intervalModel.addAnswer(right + "", (left) > (right) ? 1 : 0, (left) > (right) ? "This answer is right."+ right + " will be chosen." : "This answer is wrong, " + left +"is the right answer.");
		lang.addMCQuestion(intervalModel);
		qsNr++;
	}
}
