package generators.maths.fixpointinteration;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.maths.fixpointinteration.mathterm.Term;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class FPIGenerator implements ValidatingGenerator {
	private Language lang;
	private Utility util;
	private Graph2D graph;
	private Table table;

	private Text headerText;
	private Rect headerRect;
	private SourceCode code;
	private Text infoText;
	
	private int iterationCounter;
	private boolean maxIterationsHit;
	
    private int lineOffset;
    private SourceCodeProperties sourceCodeProperties;
    private PolylineProperties tableLineProperties;
    private int legendWidth;
    private int tableColumnXWidth;
    private double x0;
    private int maxIterationSteps;
    private int arrowLength;
    private String fixPointFunction;
    private int height;
    private int tableRowHeight;
    private RectProperties headerBoxProperties;
    private int tableLeftMargin;
    private double epsilon;
    private int legendLeftMargin;
    private int arrowWidth;
    private TextProperties tableTextProperties;
    private int legendTopMargin;
    private int segmentLenght;
    private int legendInnerMargin;
    private PolylineProperties diagonalLineProperties;
    private int pixelPerCm;
    private int tableColumnIterationWidth;
    private int width;
    private TextProperties descriptionTextProperties;
    private PolylineProperties fpiLineProperties;
    private TextProperties infoTextProperties;
    private int legendColorLineLength;
    private TextProperties headerTextProperties;
    private int legendEntryHeight;
    private double markerStep;
    private int originY;
    private int originX;
    private double numberStep;
    private PolylineProperties functionLineProperties;
    private int gap;
    private double evaluationStepSize;
    private int graphRightMargin;
    private RectProperties legendBoxProperties;
    private TextProperties legendTextProperties;
    private double functionMarginFactor;
    
    /************
     * Auto-generated code
     ************/

    public void init(){
        lang = new AnimalScript("Fixpunktiteration", "Simon Bunten, Martin Oehler", 800, 600);
		util = new Utility(lang);
		lang.setStepMode(true);
		
		headerText = null;
		headerRect = null;
    }
    
	@Override
	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		fixPointFunction = (String)primitives.get("fixPointFunction");
		try {
			Term.parse(fixPointFunction);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Error beim Parsen der Fixpunktfunktion: " + e.getMessage() + "\n" + fixPointFunction);
		} 
        maxIterationSteps = (Integer)primitives.get("maxIterationSteps");
		if (maxIterationSteps < 1 ) {
			throw new IllegalArgumentException("maxIterationSteps has to be greater than 0");
		}
        graphRightMargin = (Integer)primitives.get("marginGraphTable");
        if (graphRightMargin < 1 ) {
			throw new IllegalArgumentException("marginGraphTable has to be greater than 0");
		}
        width = (Integer)primitives.get("width");
        height = (Integer)primitives.get("height");
        if (width < 100 || height < 100) {
			throw new IllegalArgumentException("graph width and height can't be lower than 100");
		}
        arrowLength = (Integer)primitives.get("arrowLength");
        arrowWidth = (Integer)primitives.get("arrowWidth");
        if (arrowLength < 1 || arrowWidth < 1) {
			throw new IllegalArgumentException("arrowLength and arrowWidth have to be greater than 0");
		}
        segmentLenght = (Integer)primitives.get("segmentLenght");
        gap = (Integer)primitives.get("gap");
        if (segmentLenght < 1 || gap < 1) {
			throw new IllegalArgumentException("segmentLenght and gap have to be greater than 0");
		}
        functionMarginFactor = (Double)primitives.get("functionMarginFactor");
        if (functionMarginFactor < 1 ) {
			throw new IllegalArgumentException("functionMarginFactor has to be greater or equal than 1");
		}
        legendLeftMargin = (Integer)primitives.get("legendLeftMargin");
        legendTopMargin = (Integer)primitives.get("legendTopMargin");
        legendWidth = (Integer)primitives.get("legendWidth");
        legendInnerMargin = (Integer)primitives.get("legendInnerMargin");
        legendColorLineLength = (Integer)primitives.get("legendColorLineLength");
        legendEntryHeight = (Integer)primitives.get("legendEntryHeight");
        if (legendLeftMargin < 0 || legendTopMargin < 0 || legendInnerMargin < 0) {
			throw new IllegalArgumentException("legend margins have to be non-negative.");
		}
        if (legendWidth < 1 || legendEntryHeight < 1) {
			throw new IllegalArgumentException("legend width and entry height have to be greater than 0");
		}
        if (legendColorLineLength < 0) {
        	throw new IllegalArgumentException("legendColorLineLength has to be non-negative.");
        }
        tableColumnIterationWidth = (Integer)primitives.get("tableColumnIterationWidth");
        tableColumnXWidth = (Integer)primitives.get("tableColumnXWidth");
        tableRowHeight = (Integer)primitives.get("tableRowHeight");
        tableLeftMargin = (Integer)primitives.get("tableLeftMargin");
        if (tableColumnIterationWidth < 0 || tableColumnXWidth < 0 || tableRowHeight < 0 || tableLeftMargin < 0) {
			throw new IllegalArgumentException("table configuration values have to be non-negative.");
		}
		return true;
	}

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        lineOffset = (Integer)primitives.get("lineOffset");
        sourceCodeProperties = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProperties");
        tableLineProperties = (PolylineProperties)props.getPropertiesByName("tableLineProperties");
        legendWidth = (Integer)primitives.get("legendWidth");
        tableColumnXWidth = (Integer)primitives.get("tableColumnXWidth");
        x0 = (double)primitives.get("x0");
        maxIterationSteps = (Integer)primitives.get("maxIterationSteps");
        arrowLength = (Integer)primitives.get("arrowLength");
        fixPointFunction = (String)primitives.get("fixPointFunction");
        height = (Integer)primitives.get("height");
        tableRowHeight = (Integer)primitives.get("tableRowHeight");
        headerBoxProperties = (RectProperties)props.getPropertiesByName("headerBoxProperties");
        tableLeftMargin = (Integer)primitives.get("tableLeftMargin");
        epsilon = (double)primitives.get("epsilon");
        legendLeftMargin = (Integer)primitives.get("legendLeftMargin");
        arrowWidth = (Integer)primitives.get("arrowWidth");
        tableTextProperties = (TextProperties)props.getPropertiesByName("tableTextProperties");
        legendTopMargin = (Integer)primitives.get("legendTopMargin");
        segmentLenght = (Integer)primitives.get("segmentLenght");
        legendInnerMargin = (Integer)primitives.get("legendInnerMargin");
        diagonalLineProperties = (PolylineProperties)props.getPropertiesByName("diagonalLineProperties");
        tableColumnIterationWidth = (Integer)primitives.get("tableColumnIterationWidth");
        width = (Integer)primitives.get("width");
        descriptionTextProperties = (TextProperties)props.getPropertiesByName("descriptionTextProperties");
        fpiLineProperties = (PolylineProperties)props.getPropertiesByName("fpiLineProperties");
        infoTextProperties = (TextProperties)props.getPropertiesByName("infoTextProperties");
        legendColorLineLength = (Integer)primitives.get("legendColorLineLength");
        headerTextProperties = (TextProperties)props.getPropertiesByName("headerTextProperties");
        legendEntryHeight = (Integer)primitives.get("legendEntryHeight");
        functionLineProperties = (PolylineProperties)props.getPropertiesByName("functionLineProperties");
        gap = (Integer)primitives.get("gap");
        graphRightMargin = (Integer)primitives.get("marginGraphTable");
        legendBoxProperties = (RectProperties)props.getPropertiesByName("legendBoxProperties");
        legendTextProperties = (TextProperties)props.getPropertiesByName("legendTextProperties");
        functionMarginFactor = (Double)primitives.get("functionMarginFactor");
        
        findFpiBounds(fixPointFunction);
        
		graph = new Graph2D(lang, util, 50, 100 + 3*legendEntryHeight+10 + 10, originX, originY, width, height, pixelPerCm);
		table = new Table(lang, graph.getWidth() + graph.getPosX()+graphRightMargin, 350);
        
        util.setLineOffset(lineOffset);
        table.setLeftMargin(tableLeftMargin);
        table.setRowHeight(tableRowHeight);
        table.setLineProperties(tableLineProperties);
        table.setTextProperties(tableTextProperties);
        
        graph.setDLGap(gap);
        graph.setDLSegmentLength(segmentLenght);
        graph.setGraphArrowLength(arrowLength);
        graph.setGraphArrowWidth(arrowWidth);
        graph.setLegendColorLineLength(legendColorLineLength);
        graph.setLegendEntryHeight(legendEntryHeight);
        graph.setLegendInnerMargin(legendInnerMargin);
        graph.setLegendLeftMargin(legendLeftMargin);
        graph.setLegendRectangleProperties(legendBoxProperties); 
        graph.setLegendTextProperties(legendTextProperties); 
        graph.setLegendTopMargin(legendTopMargin);
        graph.setEvaluationStepsize(evaluationStepSize);
        
        animate(fixPointFunction);
        
        return lang.toString();
    }

	public String getName() {
        return "Fixpunktiteration";
    }

    public String getAlgorithmName() {
        return "Fixpunktiteration";
    }

    public String getAnimationAuthor() {
        return "Simon Bunten, Martin Oehler";
    }

    public String getDescription(){
        return "Eine Fixpunktiteration (oder auch Fixpunktverfahren) ist ein numerisches Verfahren zur n&auml;herungsweisen "
 +"Bestimmung der L&ouml;sung einer Gleichung. Dazu muss die Gleichung zuerst in eine Fixpunktgleichung umgewandelt"
 +"werden. Diese hat die Form: &#966;(x) = x.<br><br>"
 +"Anschließend wird ein Startwert x<sub>0</sub> gewählt und in die Fixpunktfunktion eingesetzt: x<sub>1</sub> = &#966;(x<sub>0</sub>)."
 +"Jeder weitere Schritt wird durch x<sub>k+1</sub> = &#966;(x<sub>k</sub>), k &#8712; &#8469; berechnet. Unter geeigneten Zusatzvorraussetzungen"
 +"n&auml;hert sich die Folge der L&ouml;sung für die Gleichung &#966;(x) = x und damit der L&ouml;sung der urspr&uuml;nglichen Gleichung."
 +"Der Algorithmus endet, wenn ein Schritt keine große Ver&auml;nderung mehr bringt (|x<sub>k+1</sub> - x<sub>k</sub>| &lt; &#949;) oder die "
 +"maximale Anzahl an Iterationen erreicht wurde.<br> "
 +"Aussagen über Existenz, Eindeutigkeit und Konvergenz gibt der Banachsche Fixpunktsatz.<br><br>"
 +"Jede Gleichung f(x) = g(x) kann trivial in eine Fixpunktgleichung durch &#966;(x) = f(x) - g(x) + x &uuml;bersetzt "
 +"werden.<br><br>"
 +"<a href=\"http://de.wikipedia.org/wiki/Fixpunktiteration\">http://de.wikipedia.org/wiki/Fixpunktiteration</a><br>"
 +"<a href=\"http://de.wikipedia.org/wiki/Fixpunktsatz_von_Banach\">http://de.wikipedia.org/wiki/Fixpunktsatz_von_Banach</a><br>"
 +"<a href=\"http://de.wikipedia.org/wiki/Numerische_Mathematik\">http://de.wikipedia.org/wiki/Numerische_Mathematik</a><br>";
    }

    public String getCodeExample(){
        return "x[0] = x_0"
 +"\n"
 +"k = 0"
 +"\n"
 +"while (k &lt; maxIterations)"
 +"\n"
 +"    x[k+1] = f(x[k])"
 +"\n"
 +"    k++"
 +"\n"
 +"    if (|x[k] - x[k-1]| &lt; epsilon)"
 +"\n"
 +"        break;"
 +"\n"
 +"	";
    }

    public String getFileExtension(){
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
    
    /***********
     * Generator Code
     ***********/
    
    public FPIGenerator() {

	}

	public String getAnimalScript() {
		return lang.toString();
	}
	
	private double min;
	private double max;
	
	private double[] numberSteps = {0.1, 0.2, 0.5, 1, 2, 5, 10};
    private void findFpiBounds(String functionString) {
		Term f = Term.parse(functionString);
		fpiBoundsTest(f);
		placeOrigin();
		if (Math.abs(max) > Math.abs(min)) {
			if (originY < width-originX) {
				pixelPerCm = (int) (originY / (Math.abs(max) * functionMarginFactor));
			} else {
				pixelPerCm = (int) ((width - originX) / (Math.abs(max) * functionMarginFactor));
			}
		} else {
			if (originX < height-originY) {
				pixelPerCm = (int) (originX / (Math.abs(min) * functionMarginFactor));
			} else {
				pixelPerCm = (int) ((height - originY) / (Math.abs(min) * functionMarginFactor));
			}
		}
		evaluationStepSize = 1.0/pixelPerCm;
		numberStep = findNumberStep();
		markerStep = numberStep / 2;
	}
    
    private void placeOrigin() {
		if (min >= 0) {
			originX = 0;
			originY = height;
		} else if (max <= 0) {
			originY = 0;
			originX = width;
		} else {
	    	double distance = Math.abs(max-min);
    		double part = Math.abs(min)/distance;
    		originX = (int)(part*width);
    		originY = height - (int)(part*height);
		}
	}

	private double findNumberStep() {
    	double numberStepCalc = 40.0 / pixelPerCm;
    	if (numberStepCalc < numberSteps[0]) {
    		return numberSteps[0];
    	}
    	for (int i = 0; i < numberSteps.length-1; i++) {
    		if (numberSteps[i] <= numberStepCalc && numberStepCalc <= numberSteps[i+1]) {
    			double diffI = Math.abs(numberSteps[i]-numberStepCalc);
    			double diffI1 = Math.abs(numberSteps[i+1]-numberStepCalc);
    			if (diffI <= diffI1) {
    				return numberSteps[i];
    			} else {
    				return numberSteps[i+1];
    			}
    		}
    	}
    	return numberSteps[numberSteps.length-1];
    }

	private void fpiBoundsTest(Term f) {
		double xk = x0;
		double xkprev = x0;
		min = x0;
		max = x0;
		iterationCounter = 0;
		while (iterationCounter < maxIterationSteps) {
			xkprev = xk;
			xk = f.evaluate(xk);
			if (xk > max) {
				max = xk;
			} else if( xk < min) {
				min = xk;
			}
			iterationCounter++;
			if (Math.abs(xk-xkprev) < epsilon) {
				break;
			}
		}
	}

	private void animate(String functionString) {
		// 1. Display basic information
		showHeader();
		showDescription();
		// 3. main algorithm with graph
		graph.drawCoordinateSystem(markerStep, numberStep);
		Term f = Term.parse(functionString);
		graph.drawFunctionDotted(Term.parse("x"), diagonalLineProperties);
		graph.drawFunction(f, functionLineProperties);
		initTable();
		initLegend(f);
		initSourceCode(f);
		initInformationText();
		lang.nextStep("Initialer Graph");
		double result = fpi(f);
		lang.hideAllPrimitives();
		// 4. final slide
		showHeader();
		showFinalSlide(result);
	}

	private void showHeader() {
		if (headerText != null && headerRect != null) {
			headerText.show();
			headerRect.show();
			return;
		}
		// Header
		headerTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(Font.SANS_SERIF, Font.BOLD, 24));
		headerText = lang.newText(new Coordinates(20, 30), "Fixpunktiteration",
				"header", null, headerTextProperties);
		headerBoxProperties
				.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		headerRect = lang.newRect(new Offset(-5, -5, "header",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "header",
				AnimalScript.DIRECTION_SE), "hRect", null,
				headerBoxProperties);
	}
	
	private final String DESCRIPTION_1 = 
			"Eine Fixpunktiteration (oder auch Fixpunktverfahren) ist ein numerisches Verfahren zur näherungsweisen\n"+
			"Bestimmung der Lösung einer Gleichung. Dazu muss die Gleichung zuerst in eine Fixpunktgleichung umgewandelt\n"+
			"werden. Diese hat die Form: φ(x) = x.\n" +
			"\n" +
			"Anschließend wird ein Startwert x_0 gewählt und in die Fixpunktfunktion eingesetzt: x_1 = φ(x_0).\n" +
			"Jeder weitere Schritt wird durch x_(k+1) = φ(x_k), k ∈ ℕ berechnet. Unter geeigneten Zusatzvorraussetzungen\n" +
			"nähert sich die Folge der Lösung für die Gleichung φ(x) = x und damit der Lösung der ursprünglichen Gleichung.\n" +
			"Der Algorithmus endet, wenn ein Schritt keine große Veränderung mehr bringt (|x_(k+1) - x_k| < ε oder die\n" +
			"maximale Anzahl an Iterationen erreicht wurde.\n" +
			"Aussagen über Existenz, Eindeutigkeit und Konvergenz gibt der Banachsche Fixpunktsatz.\n" +
			"\n" +
			"Jede Gleichung f(x) = g(x) kann trivial in eine Fixpunktgleichung durch φ(x) = f(x) - g(x) + x übersetzt \n" +
			"werden.\n" +
			"\n" +
			"http://de.wikipedia.org/wiki/Fixpunktiteration\n" +
			"http://de.wikipedia.org/wiki/Fixpunktsatz_von_Banach\n" +
			"http://de.wikipedia.org/wiki/Numerische_Mathematik"; 
	private final String DESCRIPTION_2 = 
			"Existenz und Eindeutigkeit\n" +
			"\n" +
			"Sei φ:[a,b] → [a,b] ⊂ ℝ eine stetig differenzierbare Fixpunktiterationsfunktion mit φ(a) > a, φ(b) < b und\n " +
			"φ' ≠ 1 für alle x aus (a,b). Dann existiert genau ein Fixpunkt x* aus (a,b) mit φ(x*) = x*.\n" +
			"\n" +
			"Für den Beweis siehe: http://de.wikipedia.org/wiki/Fixpunktiteration#Beweis\n";
	
	private void showDescription() {
		Node position = new Offset(-5, 50, "hRect", "SW");
		Text[] description1 = util.drawText(DESCRIPTION_1, position,
				descriptionTextProperties);
		lang.nextStep("Beschreibung Teil 1");
		for (Text t : description1) {
			t.hide();
		}
		Text[] description2 = util.drawText(DESCRIPTION_2, position,
				descriptionTextProperties);
		lang.nextStep("Beschreibung Teil 2");
		for (Text t : description2) {
			t.hide();
		}
	}
		
	private void initLegend(Term f) {
		graph.addLegendEntry((Color)functionLineProperties.get(AnimationPropertiesKeys.COLOR_PROPERTY), "y = " + f.toString());		
		graph.addLegendEntry((Color)diagonalLineProperties.get(AnimationPropertiesKeys.COLOR_PROPERTY), "y = x");
		graph.addLegendEntry((Color)fpiLineProperties.get(AnimationPropertiesKeys.COLOR_PROPERTY), "Fixpunktiteration");
		graph.updateLegend(50, 100, legendWidth);
	}

	private void initTable() {
		table.addColumn("Iteration (k)", tableColumnIterationWidth);
		table.addColumn("x_k", tableColumnXWidth);
		table.drawHeader();
	}

	
	private void initSourceCode(Term f) {
		String sourceCodeString = 
				"φ(x) = " + f.toString() + "\n" + 
				"x[0] = " + Double.toString(x0) + "\n" +
				"k = 0\n" +
				"ε = " + Double.toString(epsilon) + "\n" +
				"maxIterations = " + maxIterationSteps + "\n" +
				"while (k < maxIterations)\n" +
					"\tx[k+1] = φ(x[k])\n" +
					"\tk++\n" +
					"\tif (|x[k] - x[k-1]| < ε)\n" +
						"\t\tbreak\n";
		code = lang.newSourceCode(new Coordinates(graph.getPosX() + graph.getWidth() + graphRightMargin, 125), "sourceCode", null, sourceCodeProperties);
		code.addMultilineCode(sourceCodeString, "sourceCodeLabel", null);
	}
	
	private double fpi(Term f) {
		display("Initialisiere die Fixpunktfunktion φ(x) = " + f.toString() + ".");
		code.highlight(0);
		lang.nextStep();
		display("Setze den Startwert auf x[0] = " + x0 + " und den Zähler k auf 0.");
		code.toggleHighlight(0, 1);
		code.highlight(2);
		double xk = x0;
		double xkprev = x0;
		iterationCounter = 0;
		maxIterationsHit = false;
		table.addRow(Integer.toString(iterationCounter), Double.toString(xk));
		lang.nextStep();
		display("Initialisiere die Abbruchbedingungen.");
		code.toggleHighlight(1,3);
		code.toggleHighlight(2,4);
		lang.nextStep();
		display("Maximale Iterationen erreicht? " + iterationCounter + " < " + maxIterationSteps + " ?");
		code.unhighlight(3);
		code.toggleHighlight(4,5);
		lang.nextStep();
		while (iterationCounter < maxIterationSteps) {
			display("Berechne den nächsten Iterationsschritt x[" + (iterationCounter+1) + "] = φ(x[" + iterationCounter + "])..");
			code.toggleHighlight(5,6);
			xkprev = xk;
			xk = f.evaluate(xk);
			iterationCounter++;
			table.addRow(Integer.toString(iterationCounter), Double.toString(xk));
			graph.drawLine(xkprev, xkprev, xkprev, xk, fpiLineProperties);
			lang.nextStep(iterationCounter + ". Iteration");
			display(".. und erhöhe den Zähler k auf " + iterationCounter + ".");
			code.toggleHighlight(6,7);
			graph.drawLine(xkprev, xk, xk, xk, fpiLineProperties);
			lang.nextStep();
			display("Überprüfe die Abbruchbedingung: " + Math.abs(xk-xkprev) + " < " + epsilon + " ?");
			code.toggleHighlight(7,8);
			lang.nextStep();
			if (Math.abs(xk-xkprev) < epsilon) {
				display("Abbruchbedingung erfüllt.");
				code.toggleHighlight(8,9);
				lang.nextStep("Epsilon-Abbruchbedingung erfüllt.");
				break;
			}
			display("Maximale Iterationen erreicht? " + iterationCounter + " < " + maxIterationSteps + " ?");
			code.toggleHighlight(8,5);
			lang.nextStep();
			
		}
		if (!(iterationCounter < maxIterationSteps)) {
			display("Maximale Anzahl an Iterationen erreicht. Der Algorithmus wird abgebrochen.");
			lang.nextStep("Maximale Iterationen erreicht.");
			maxIterationsHit = true;
		}
		display("Der Algorithmus wurde nach " + iterationCounter + " Iterationen beendet.");
		code.unhighlight(9);
		code.unhighlight(5);
		lang.nextStep("Algorithmus beendet");
		return xk;
	}
	private void initInformationText() {
		infoText = util.drawText("", graph.getPosX() + graph.getWidth() + graphRightMargin, 100, infoTextProperties)[0];
	}
	private void display(String text) {
		infoText.setText(text, null, null);
	}
	
	private void showFinalSlide(double result) {
		String text = 
				"Das Ergebnis der Fixpunktiteration ist: " +  Double.toString(result) + ".\n" +
				"Der Algorithmus wurde nach " + Integer.toString(iterationCounter) + " Iterationen beendet, da ";
		if (maxIterationsHit) {
			text += "die maximale Anzahl an Iterationen (" + maxIterationSteps + ") erreicht wurde.";
		} else {
			text += "das Epsilon-Kriterium (|x[k] - x[k-1]| < ε) erfüllt wurde.";
		}
		Node position = new Offset(-5, 50, "hRect", "SW");
		util.drawText(text, position,
				descriptionTextProperties);
		lang.nextStep("Zusammenfassung");	
	}
}