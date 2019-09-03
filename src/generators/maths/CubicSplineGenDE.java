package generators.maths;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import algoanim.animalscript.addons.Slide;
import algoanim.animalscript.addons.bbcode.DefaultStyle;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PointProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

public class CubicSplineGenDE{
	algoanim.primitives.Text headline;
	protected List<Node> nodes;
	protected Language lang;
	protected PointProperties pointProps;
	protected TextProperties textProps;
	protected PointProperties splineProps;
	protected PolylineProperties lineProps;
	protected PolylineProperties lineProps2;
	protected PolylineProperties axisProps;
	protected Slide sl_0;
	protected Slide sl_1;
	protected Slide sl_2;
	protected int stepwidth = 85;
	protected static int example_nr = 1;
	protected Color color2;

	int xOff = 50; // coordinate system origin
	int yOff = 500;

	protected void init(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		example_nr = (int) primitives.get("pick_example");		
		//für properties	name_propetie = CAST props.getPropertiesByName((name);
		
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		pointProps = new PointProperties();
		pointProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 28));
		

		splineProps = new PointProperties();
		splineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

		lineProps = new PolylineProperties();
		lineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		
		lineProps2 = new PolylineProperties();
		lineProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.ORANGE);

		axisProps = new PolylineProperties();
		axisProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		
		showText(5);

		// Drawing coordinate system
		Node[] xAxis = new Node[2];
		xAxis[0] = new Coordinates(xOff, yOff);
		xAxis[1] = new Coordinates(xOff + 400, yOff);
		lang.newPolyline(xAxis, "xAxis", null, axisProps);

		Node[] yAxis = new Node[2];
		yAxis[0] = new Coordinates(xOff, yOff);
		yAxis[1] = new Coordinates(xOff, yOff - 300);
		lang.newPolyline(yAxis, "yAxis", null, axisProps);

		sl_0 = new Slide(lang, "slide_0DE.txt", "Slide1",
				new DefaultStyle());
		sl_0.hide();
		
		sl_1 = new Slide(lang, "slide_1DE.txt", "Slide1",
				new DefaultStyle());	

	}
	
	protected void showText(int ex_nr){
		switch(ex_nr){
		case 1: lang.newText(new Coordinates(450,200), "Beispiel 1: Wurzelfunktion	8x^0.7", "ex1", null);
				//lang.newText(new Coordinates(450,220), "black: original function", "black", null);
//				lang.newText(new Coordinates(450,240), "red: interpolated function", "red", null);
		break;
		case 2: lang.newText(new Coordinates(450,200), "Beispiel 2: quadratische Funktion	0.001x^2 - 0.05x + 20", "ex2", null);
		break;
		case 3: lang.newText(new Coordinates(450,200), "Beispiel 3: trigonometrische Funktion	150 * |sin(0.03x)|", "ex3", null);
		break;
		case 4: lang.newText(new Coordinates(450,200), "Beispiel 4: Runge's Beispiel	300/(x-150)^2 + 20", "ex4", null);
		break;
		case 5: headline = lang.newText(new Coordinates(1,7	), "Cubic Spline Interpolation", "animName", null, textProps);
		break;
		case 6: lang.nextStep();
			new Slide(lang, "slide_3DE.txt", "Slide3",
				new DefaultStyle());			
		break;
		}
	}

	protected void askQuestion(int nr){
		switch(nr){
		case 1:  TrueFalseQuestionModel q1 = new TrueFalseQuestionModel("q1");
		 q1.setPrompt("Kann man die Genauigkeit der Interpolation durch Erhöhung der Knotenpunkte steigern??");
		 q1.setFeedbackForAnswer(true, "Richtig, durch ehöhen der Knotenzahl wird die Interpolation genauer");
		 q1.setCorrectAnswer(true);
		 q1.setPointsPossible(1);
		 lang.addTFQuestion(q1);
		 lang.nextStep();
		 break;
		case 2: MultipleChoiceQuestionModel q2 = new MultipleChoiceQuestionModel("q2");
		q2.setPrompt("Wo ist die interpolierte Funktion IMMER gleich zur Originalfunktion??");
		q2.addAnswer("An zufälligen Punkten", 0, "Falsch, die Funktionen können zwar an anderen Stellen ausser den Knoten übereinstimmn, dies ist jedoch nicht immer der Fall");
		q2.addAnswer("An den Knotenpunkten", 1, "Richtig, die Funktionswerte sind an den Knotenpunkten identisch");
		q2.addAnswer("Nie!", 0, "Falsch, versuch es nochmal");
		lang.addMCQuestion(q2);
		lang.nextStep();
		}
	}
	
	protected void drawFunction(int nr) { // draws the functions we want to
		lang.newText(new Coordinates(450,220), "Schwarz: Originalfunktion", "black", null);					// interpolate
		askQuestion(1);
		 
		nodes = new ArrayList<Node>();
		if (nr == 1) {
			for (int i = 0; i < 340; i++) {
				nodes.add(new Coordinates(i + xOff, yOff - (int) Math.pow(8 * i, 0.7)));
				// lang.newPoint(nodes.get(i), "function", null, pointProps);
			}
			lang.newPolyline(nodes.toArray(new Node[] {}), "cubic spline", null, axisProps);
		} else if (nr == 2) {
			for (int i = 0; i < 340; i++) {
				nodes.add(new Coordinates(i + xOff, (int) (yOff - ((0.001 * Math.pow(i, 2)) - 0.05 * i + 20))));
			}
			lang.newPolyline(nodes.toArray(new Node[] {}), "cubic spline", null, axisProps);
		} else if (nr == 3) {
			for (int i = 0; i < 340; i++) {
				nodes.add(new Coordinates(i + xOff, (int) (yOff - 150 * Math.abs(Math.sin(0.03 * i)))));
			}
			lang.newPolyline(nodes.toArray(new Node[] {}), "cubic spline", null, axisProps);
		} else if (nr == 4) {
			for (int i = 0; i < 340; i++) {
				nodes.add(new Coordinates(i + xOff, (int) (yOff - ((300 / Math.pow((i - 150), 2)) + 20))));
			}
			lang.newPolyline(nodes.toArray(new Node[] {}), "cubic spline", null, axisProps);
		}

	}

	protected void drawSpline1() {
		List<Node> cubic_nodes = new ArrayList<Node>();
		for (int i = 0; i < stepwidth; i++) {
			Coordinates tmp = new Coordinates(i + xOff,
					(int) (yOff - (-0.000014713 * Math.pow(i, 3) - 0.00000000000006 * Math.pow(i, 2) + 1.2357 * i)));
			cubic_nodes.add(tmp);
		}
		lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps);
		for (int i = stepwidth; i < 2 * stepwidth; i++) {
			Coordinates tmp = new Coordinates(i + xOff,
					(int) (yOff - (0.000014946 * Math.pow(i, 3) - 0.007563 * Math.pow(i, 2) + 1.8786 * i - 18.214)));
			cubic_nodes.add(tmp);
		}
		lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps);
		for (int i = 2 * stepwidth; i < 3 * stepwidth; i++) {
			Coordinates tmp = new Coordinates(i + xOff, (int) (yOff
					- (-0.0000011049 * Math.pow(i, 3) + 0.00062284 * Math.pow(i, 2) + 0.48697 * i + 60.643)));
			cubic_nodes.add(tmp);
		}
		lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps);
		for (int i = 3 * stepwidth; i < 4 * stepwidth; i++) {
			Coordinates tmp = new Coordinates(i + xOff, (int) (yOff
					- (0.00000087232 * Math.pow(i, 3) - 0.00088977 * Math.pow(i, 2) + 0.87269 * i + 27.857)));
			cubic_nodes.add(tmp);
		}
		lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps);
	}

	protected void drawSpline2() {
		List<Node> cubic_nodes = new ArrayList<Node>();
		for (int i = 0; i < stepwidth; i++) {
			Coordinates tmp = new Coordinates(i + xOff, (int) (yOff
					- (0.00000505817 * Math.pow(i, 3) - 0.0000034088 * Math.pow(i, 2) - 0.001255537 * i + 20)));
			cubic_nodes.add(tmp);
		}
		lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps);
		for (int i = stepwidth; i < 2 * stepwidth; i++) {
			Coordinates tmp = new Coordinates(i + xOff, (int) (yOff
					- (-0.000001681 * Math.pow(i, 3) + 0.00171514 * Math.pow(i, 2) - 0.1473322039 * i + 24.138)));
			cubic_nodes.add(tmp);
		}
		lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps);
		for (int i = 2 * stepwidth; i < 3 * stepwidth; i++) {
			Coordinates tmp = new Coordinates(i + xOff, (int) (yOff
					- (0.00000166677 * Math.pow(i, 3) + 0.0000076538 * Math.pow(i, 2) + 0.14294047 * i + 7.69)));
			cubic_nodes.add(tmp);
		}
		lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps);
		for (int i = 3 * stepwidth; i < 4 * stepwidth; i++) {
			Coordinates tmp = new Coordinates(i + xOff, (int) (yOff
					- (-0.0000049858 * Math.pow(i, 3) + 0.005096924 * Math.pow(i, 2) - 1.1548236 * i + 118)));
			cubic_nodes.add(tmp);
		}
		lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps);
	}

	protected void drawSpline3() {
		List<Node> cubic_nodes = new ArrayList<Node>();
		for (int i = 0; i < stepwidth; i++) {
			Coordinates tmp = new Coordinates(i + xOff,
					(int) (yOff - (-0.00000836 * Math.pow(i, 3) + 0.000000031186 * Math.pow(i, 2) + 1.044532 * i)));
			cubic_nodes.add(tmp);
		}
			lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps);
		
		for (int i = stepwidth; i < 2 * stepwidth; i++) {
			Coordinates tmp = new Coordinates(i + xOff, (int) (yOff
					- (-0.00000448 * Math.pow(i, 3) - 0.0009891 * Math.pow(i, 2) + 1.12861197 * i - 2.38226)));
			cubic_nodes.add(tmp);
		}
			lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps);
		
		for (int i = 2 * stepwidth; i < 3 * stepwidth; i++) {
			Coordinates tmp = new Coordinates(i + xOff, (int) (yOff
					- (-0.000004269 * Math.pow(i, 3) - 0.00109826 * Math.pow(i, 2) + 1.147162 * i - 3.4334)));
			cubic_nodes.add(tmp);
		}
			lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps);
		
		for (int i = 3 * stepwidth; i < 4 * stepwidth; i++) {
			Coordinates tmp = new Coordinates(i + xOff,
					(int) (yOff - (0.00001711 * Math.pow(i, 3) - 0.01745665 * Math.pow(i, 2) + 5.31855 * i - 358)));
			cubic_nodes.add(tmp);
		}
			lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps);
		

	}

	protected void drawSpline4() {
		List<Node> cubic_nodes = new ArrayList<Node>();
		for (int i = 0; i < stepwidth; i++) {
			cubic_nodes.add(new Coordinates(i + xOff, (int) (yOff
					- (0.000209356 * Math.pow(i, 3) + 0.0000000298838248 * Math.pow(i, 2) - 1.5126064 * i + 20))));

		}
			lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps);
		
		for (int i = stepwidth; i < 2 * stepwidth; i++) {
			cubic_nodes.add(new Coordinates(i + xOff, (int) (yOff
					- (-0.0005582855 * Math.pow(i, 3) + 0.19574885 * Math.pow(i, 2) - 18.151256 * i + 491))));
		}
			lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps);
		
		for (int i = 2 * stepwidth; i < 3 * stepwidth; i++) {
			cubic_nodes.add(new Coordinates(i + xOff,
					(int) (yOff - (0.00055828 * Math.pow(i, 3) - 0.37370244 * Math.pow(i, 2) + 78.655467 * i - 4994))));
		}
			lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps);
		
		for (int i = 3 * stepwidth; i < 4 * stepwidth; i++) {
			cubic_nodes.add(new Coordinates(i + xOff,
					(int) (yOff - (-0.000209357 * Math.pow(i, 3) + 0.2135442 * Math.pow(i, 2) - 71.09243 * i + 7734))));
		}
			lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps);
		

	}
	
	protected void drawBadSpline1(){
		List<Node> cubic_nodes = new ArrayList<Node>();
		for (int i = 0; i < 2*stepwidth; i++) {
			Coordinates tmp = new Coordinates(i + xOff,
					(int) (yOff - (-0.0000055 * Math.pow(i, 3) + 1.87 * i)));
			cubic_nodes.add(tmp);
		}
			lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps2);
		
		for (int i = 2*stepwidth; i < 4*stepwidth; i++) {
			Coordinates tmp = new Coordinates(i + xOff, (int) (yOff
					- (0.0000055 * Math.pow(i, 3) - 0.0056 * Math.pow(i, 2) + 2.8 * i - 54)));
			cubic_nodes.add(tmp);
		}
			lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps2);
		
	}
	
	protected void drawBadSpline2(){
		List<Node> cubic_nodes = new ArrayList<Node>();
		for (int i = 0; i < 2*stepwidth; i++) {
			Coordinates tmp = new Coordinates(i + xOff,
					(int) (yOff - (0.000003 * Math.pow(i, 3) + 0.031 * i + 20)));
			cubic_nodes.add(tmp);
		}
			lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps2);
		
		for (int i = 2*stepwidth; i < 4*stepwidth; i++) {
			Coordinates tmp = new Coordinates(i + xOff, (int) (yOff
					- (-0.000003 * Math.pow(i, 3) + 0.0031 * Math.pow(i, 2) - 0.49 * i + 50)));
			cubic_nodes.add(tmp);
		}
			lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps2);
		
	}

	protected void drawBadSpline3(){
		List<Node> cubic_nodes = new ArrayList<Node>();
		for (int i = 0; i < 2*stepwidth; i++) {
			Coordinates tmp = new Coordinates(i + xOff,
					(int) (yOff - (-0.0000000051 * Math.pow(i, 3) + 0.078 * i)));
			cubic_nodes.add(tmp);
		}
			lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps2);
		
		for (int i = 2*stepwidth; i < 4*stepwidth; i++) {
			Coordinates tmp = new Coordinates(i + xOff, (int) (yOff
					- (0.0000000051 * Math.pow(i, 3) - 0.0000052 * Math.pow(i, 2) + 0.079 * i - 0.05)));
			cubic_nodes.add(tmp);
		}
			lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps2);
		
	}
	
	protected void drawBadSpline4(){
		List<Node> cubic_nodes = new ArrayList<Node>();
		for (int i = 0; i < 2*stepwidth; i++) {
			Coordinates tmp = new Coordinates(i + xOff,
					(int) (yOff - (20)));
			cubic_nodes.add(tmp);
		}
			lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps2);
		
		for (int i = 2*stepwidth; i < 4*stepwidth; i++) {
			Coordinates tmp = new Coordinates(i + xOff, (int) (yOff
					- (20)));
			cubic_nodes.add(tmp);
		}
			lang.newPolyline(cubic_nodes.toArray(new Node[] {}), "spline", null, lineProps2);
		
	}
	public void cubSplineInterpol(int nr) {

		sl_1.hide();
		sl_2 = new Slide(lang, "/Users/mariuss/Dropbox/Dropbox_workspace/CubicSplineGen/Slides/slide_2DE.txt", "Slide2", new DefaultStyle());
		lang.newText(new Coordinates(450,240), "Rot: interpolierte Funktion (mit 5 Knoten)", "red", null);
		lang.newText(new Coordinates(450,260), "Orange: interpolierte Funktion (mit 3 Konten)", "orange", null);
		lang.newText(new Coordinates(450,320), "Der Fehler lässt sich nach oben abschätzen mit: |f(x)-s(x)| <= h_max/h_min * sup|f''''(x)| * h_max^4", "error", null);


		if (nr == 1) {
			this.drawSpline1();
			this.drawBadSpline1();
		}
		else if (nr == 2){
			this.drawSpline2();
			this.drawBadSpline2();
		}
		else if (nr ==3){
			this.drawSpline3();
			this.drawBadSpline3();
		}
		else if (nr ==4){
			this.drawSpline4();
			this.drawBadSpline4();
		}
	}
	
	public void showEndScreen(){
		//lang.nextStep();
		//showText(5);	
		lang.hideAllPrimitivesExcept(headline);
		showText(6);	
		
	}

	public String run(AnimationPropertiesContainer props, Hashtable<String, Object> primitives, Language lang) {
		
		this.lang = lang;
		init(props, primitives);
		showText(example_nr);
		drawFunction(example_nr);
		cubSplineInterpol(example_nr);
		askQuestion(2);		
		showEndScreen();	
		lang.finalizeGeneration();
		return lang.toString();
	}

}
