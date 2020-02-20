/*
 * FloydsTortoiseAndHare.java
 * Christian Kunz, Jonas Winter, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.Graph;
import algoanim.primitives.Polygon;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.NotEnoughNodesException;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.PolygonProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
//import translator.TranslatableGUIElement;
import translator.Translator;

public class FloydsTortoiseAndHare implements ValidatingGenerator {
	private Locale locale;
	private Translator translator;

    private Language lang;
    
    private TextProperties titleProperties;
    private SourceCodeProperties infoTextProperties;
    private SourceCodeProperties sourceCodeProperties;
    private GraphProperties graphProperties;
    private PolygonProperties harePolygonProperties;
    private PolygonProperties tortoisePolygonProperties;
    

    
    private int circleLength;
    private int nonCircleLength;
    private int totalLength;
    
    private FloydsPath path;
    
    private SourceCode introText;
    private SourceCode outroText;
	private Graph graph;
	private SourceCode sourceCode;
    
	private Polygon tortoisePolygon;
	private Polygon harePolygon;
	
	private Variables var;
	private String varCycleAlreadyDetected;
	private String varTortoisePosition;
	private String varHarePosition;
	
	public FloydsTortoiseAndHare(Locale l) {
		locale = l;
		translator = new Translator("resources/FloydTranslatorFile", l);
	}
	
	// Generator functions
    public void init(){
        lang = new AnimalScript(getName(), "Christian Kunz, Jonas Winter", 1200, 800);
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        
        var = lang.newVariables();
        
        this.varCycleAlreadyDetected = this.translator.translateMessage("varCycleAlreadyDetected");
        var.declare("string", this.varCycleAlreadyDetected, "No");
        
        this.varTortoisePosition = this.translator.translateMessage("varTortoisePosition"); 
		var.declare("int", this.varTortoisePosition, "0");
		
		this.varHarePosition = this.translator.translateMessage("varHarePosition");
		var.declare("int", this.varHarePosition, "0");
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	//TranslatableGUIElement guiBuilder = translator.getGenerator();
    	
    	titleProperties = getTitleProperties();
    	infoTextProperties = getInfoTextProperties();
        sourceCodeProperties = (SourceCodeProperties)props.getPropertiesByName("SourceCodeProperties");
        graphProperties = (GraphProperties)props.getPropertiesByName("GraphProperties");
        harePolygonProperties = (PolygonProperties)props.getPropertiesByName("HarePolygonProperties");
        tortoisePolygonProperties = (PolygonProperties)props.getPropertiesByName("TortoisePolygonProperties");
        
        
        circleLength = (Integer)primitives.get("CircleLength");
        nonCircleLength = (Integer)primitives.get("NonCircleLength");
        totalLength = circleLength + nonCircleLength;
        
        path = new FloydsPath(circleLength, nonCircleLength);
        
        initTitle();
        introText = getInfo("intro");
        outroText = getInfo("outro");
		sourceCode = getSourceCode(sourceCodeProperties);
		graph = getGraph(graphProperties);
		
		try {
			tortoisePolygon = getTortoisePolygon(tortoisePolygonProperties);
			harePolygon = getHarePolygon(harePolygonProperties);
		} catch (NotEnoughNodesException e) {
			e.printStackTrace();
		}
        
		visualizeFloydAlgo();
        
		lang.finalizeGeneration();
        return lang.toString();
    }
     
    public String getName() {
    	return this.translator.translateMessage("title");
    }

    public String getAlgorithmName() {
    	return this.translator.translateMessage("algorithmName");
    }

    public String getAnimationAuthor() {
        return "Christian Kunz, Jonas Winter";
    }

    public String getDescription() {
    	String descriptionString = "";
    	
    	for (String descLine : getInfoLines("intro")) {
    		descriptionString = descriptionString + descLine;
    		descriptionString = descriptionString + "\n";
    	}
        return descriptionString;
    }
    
    public String getCodeExample(){
    	String sourceCodeString = "";
    	
    	for (FloydsCodeLine codeLine : getSourceCodeLines()) {
    		for (int i = 0; i < codeLine.getIndentation(); i++) {
    			sourceCodeString = sourceCodeString + "    ";
    		}
    		sourceCodeString = sourceCodeString + codeLine.getCode();
    		sourceCodeString = sourceCodeString +"\n";
    	}
        return sourceCodeString;
    }
    
    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return locale;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		
        circleLength = (Integer)primitives.get("CircleLength");
        nonCircleLength = (Integer)primitives.get("NonCircleLength");
		if (nonCircleLength < 1) {
			return false;
		}
		
		if (circleLength < 2) {
			return false;
		}
		
		return true;
	}

	// Helper functions
	private void initTitle() {
		lang.newText(new Coordinates(50, 50), getName(), "Title", null, titleProperties);
    }
	
	private TextProperties getTitleProperties() {
		TextProperties titleProperties = new TextProperties();
		titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 30));
		titleProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		return titleProperties;
    }
    
    private ArrayList<String> getInfoLines(String infoLineType) {
    	ArrayList<String> lines = new ArrayList<String>() {
    		private static final long serialVersionUID = 1L;
    	};
    	
    	int currentLine = 0;
		while (true) {
			currentLine++;
			String l = this.translator.translateMessage(infoLineType + "Line" + currentLine, null, false);
			if (!l.contains("Invalid Message Key")) {
				lines.add(l);
			}
			else {
				break;
			}
		}
    	return lines;
    }
        
	private ArrayList<FloydsCodeLine> getSourceCodeLines(){
		ArrayList<FloydsCodeLine> sourceCodeLines = new ArrayList<FloydsCodeLine>() {
			private static final long serialVersionUID = 1L;
		};
		
		ArrayList<String> codeLines = getInfoLines("code");
		
		for (int i = 0; i < codeLines.size(); i ++) {
			sourceCodeLines.add(new FloydsCodeLine(codeLines.get(i), Integer.parseInt(this.translator.translateMessage("clIndentation" + (i + 1), null, false))));
		}
		
		return sourceCodeLines;
	}
   
    private SourceCodeProperties getInfoTextProperties() {
    	SourceCodeProperties infoTextProperties = new SourceCodeProperties();
		infoTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));
		infoTextProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		return infoTextProperties;
    }
    
    private SourceCode getInfo(String infoLineType) {
    	SourceCode info = lang.newSourceCode(new Coordinates(50, 80), infoLineType, null, infoTextProperties);
    	
    	for (String line : getInfoLines(infoLineType)) {
    		info.addCodeLine(line, null, 0, null);
    	}
    	info.hide();
    	return info;
    }
        
	private SourceCode getSourceCode(SourceCodeProperties scProps) {
		SourceCode sc = lang.newSourceCode(new Coordinates(50, 100), "SourceCode", null, scProps);
		
		for (FloydsCodeLine codeLine : getSourceCodeLines()) {
			sc.addCodeLine(codeLine.getCode(), null, codeLine.getIndentation(), null);
		}
		sc.hide();

		return sc;
	}
	
	private Graph getGraph(GraphProperties gProps) {		
		// define the edges of the graph
		int[][] graphAdjacencyMatrix = new int[totalLength][totalLength];

		// initialize adjacency matrix with zeros
		for (int i = 0; i < totalLength; i++)
			for (int j = 0; j < totalLength; j++)
				graphAdjacencyMatrix[i][j] = 0;

		// set connections from start to end
		for (int i = 0; i < totalLength - 1; i++) {
			graphAdjacencyMatrix[i][i+1] = 1;
		}

		// set the connection from end to circleStart
		graphAdjacencyMatrix[totalLength - 1][nonCircleLength] = 1;
		
		
		Graph g = lang.newGraph("graph", graphAdjacencyMatrix, path.getNodeArray(), path.getLabelArray(), null, gProps);
		g.hide();
		
		return g;
	}
		
	private Polygon getTortoisePolygon(PolygonProperties tpp) throws NotEnoughNodesException {
		ArrayList<Node> nodesList = new ArrayList<Node>() {

			private static final long serialVersionUID = 1L;

			{
				add(new Coordinates(24, 0)); add(new Coordinates(22, 0)); add(new Coordinates(20, 0)); add(new Coordinates(18, 0)); add(new Coordinates(16, 0)); add(new Coordinates(14, 1)); add(new Coordinates(12, 2)); add(new Coordinates(10, 2)); add(new Coordinates(9, 4)); add(new Coordinates(7, 5)); add(new Coordinates(6, 6)); add(new Coordinates(5, 8)); add(new Coordinates(4, 10)); add(new Coordinates(3, 12)); add(new Coordinates(2, 13)); add(new Coordinates(0, 13)); add(new Coordinates(0, 15)); add(new Coordinates(2, 15)); add(new Coordinates(4, 16)); add(new Coordinates(6, 16)); add(new Coordinates(7, 17)); add(new Coordinates(5, 18)); add(new Coordinates(3, 19)); add(new Coordinates(2, 20)); add(new Coordinates(3, 22)); add(new Coordinates(4, 23)); add(new Coordinates(6, 23)); add(new Coordinates(8, 22)); add(new Coordinates(10, 22)); add(new Coordinates(12, 20)); add(new Coordinates(13, 20)); add(new Coordinates(15, 20)); add(new Coordinates(17, 20)); add(new Coordinates(19, 20)); add(new Coordinates(21, 20)); add(new Coordinates(23, 20)); add(new Coordinates(25, 20)); add(new Coordinates(27, 20)); add(new Coordinates(29, 20)); add(new Coordinates(31, 20)); add(new Coordinates(33, 19)); add(new Coordinates(35, 19)); add(new Coordinates(36, 20)); add(new Coordinates(37, 22)); add(new Coordinates(39, 23)); add(new Coordinates(41, 23)); add(new Coordinates(42, 21)); add(new Coordinates(42, 19)); add(new Coordinates(41, 17)); add(new Coordinates(41, 15)); add(new Coordinates(40, 14)); add(new Coordinates(41, 12)); add(new Coordinates(43, 12)); add(new Coordinates(45, 11)); add(new Coordinates(47, 11)); add(new Coordinates(48, 10)); add(new Coordinates(49, 9)); add(new Coordinates(49, 7)); add(new Coordinates(47, 6)); add(new Coordinates(45, 6)); add(new Coordinates(43, 6)); add(new Coordinates(41, 7)); add(new Coordinates(39, 7)); add(new Coordinates(38, 7)); add(new Coordinates(36, 5)); add(new Coordinates(35, 4)); add(new Coordinates(33, 3)); add(new Coordinates(31, 2)); add(new Coordinates(30, 1)); add(new Coordinates(28, 0)); add(new Coordinates(26, 0)); add(new Coordinates(24, 0));
			}
		};
		Node[] nodeArray = nodesList.toArray(new Node[nodesList.size()]);
		
		Polygon polygon = lang.newPolygon(nodeArray, "Tortoise", null, tpp);
		
		return polygon;
	}
	
	private Polygon getHarePolygon(PolygonProperties hpp) throws NotEnoughNodesException {
		ArrayList<Node> nodesList = new ArrayList<Node>() {

			private static final long serialVersionUID = 1L;

			{
				add(new Coordinates(27, 0)); add(new Coordinates(27, 2)); add(new Coordinates(27, 4)); add(new Coordinates(28, 6)); add(new Coordinates(30, 8)); add(new Coordinates(31, 9)); add(new Coordinates(32, 11)); add(new Coordinates(32, 12)); add(new Coordinates(30, 12)); add(new Coordinates(28, 12)); add(new Coordinates(26, 12)); add(new Coordinates(24, 12)); add(new Coordinates(22, 12)); add(new Coordinates(20, 12)); add(new Coordinates(18, 13)); add(new Coordinates(16, 13)); add(new Coordinates(14, 14)); add(new Coordinates(12, 15)); add(new Coordinates(11, 16)); add(new Coordinates(9, 18)); add(new Coordinates(8, 19)); add(new Coordinates(7, 20)); add(new Coordinates(5, 22)); add(new Coordinates(4, 23)); add(new Coordinates(2, 23)); add(new Coordinates(0, 24)); add(new Coordinates(1, 26)); add(new Coordinates(3, 27)); add(new Coordinates(4, 28)); add(new Coordinates(3, 29)); add(new Coordinates(2, 30)); add(new Coordinates(1, 32)); add(new Coordinates(0, 34)); add(new Coordinates(0, 36)); add(new Coordinates(0, 38)); add(new Coordinates(0, 40)); add(new Coordinates(1, 41)); add(new Coordinates(3, 39)); add(new Coordinates(3, 38)); add(new Coordinates(4, 36)); add(new Coordinates(5, 34)); add(new Coordinates(7, 33)); add(new Coordinates(8, 32)); add(new Coordinates(10, 32)); add(new Coordinates(12, 32)); add(new Coordinates(14, 30)); add(new Coordinates(15, 29)); add(new Coordinates(17, 28)); add(new Coordinates(19, 27)); add(new Coordinates(21, 26)); add(new Coordinates(23, 26)); add(new Coordinates(25, 25)); add(new Coordinates(27, 25)); add(new Coordinates(29, 25)); add(new Coordinates(31, 25)); add(new Coordinates(33, 25)); add(new Coordinates(35, 25)); add(new Coordinates(37, 25)); add(new Coordinates(38, 26)); add(new Coordinates(40, 27)); add(new Coordinates(42, 27)); add(new Coordinates(44, 27)); add(new Coordinates(44, 25)); add(new Coordinates(43, 24)); add(new Coordinates(45, 25)); add(new Coordinates(47, 25)); add(new Coordinates(49, 25)); add(new Coordinates(49, 23)); add(new Coordinates(47, 23)); add(new Coordinates(45, 22)); add(new Coordinates(43, 21)); add(new Coordinates(41, 21)); add(new Coordinates(42, 19)); add(new Coordinates(44, 19)); add(new Coordinates(46, 18)); add(new Coordinates(45, 16)); add(new Coordinates(45, 14)); add(new Coordinates(44, 12)); add(new Coordinates(42, 11)); add(new Coordinates(40, 10)); add(new Coordinates(38, 9)); add(new Coordinates(38, 7)); add(new Coordinates(37, 6)); add(new Coordinates(36, 4)); add(new Coordinates(34, 2)); add(new Coordinates(33, 1)); add(new Coordinates(31, 1)); add(new Coordinates(29, 1)); add(new Coordinates(28, 0));
			}
		};
		Node[] nodeArray = nodesList.toArray(new Node[nodesList.size()]);
		
		Polygon polygon = lang.newPolygon(nodeArray, "Hare", null, hpp);
		
		return polygon;
	}

	// Visualization functions
	private void visualizeFloydAlgo () {
		showIntroText();

		showSourceCode();
		
		showGraph();
		
		highlightFirstLine();
				
		initializeTortoise();
		
		initializeHare();
		
		showSwapTortoiseAndHareQuestion();
		
		int lastHarePosition = detectCycle();
		
		prepareCycleStartDetection();
		
		
		detectStartOfCycle(lastHarePosition);
		
		finishCode();
		
		showErrorProneQuestion();
		
		showOutroText();
	}
	
	private void showIntroText() {
		introText.show();
		lang.nextStep();
		
		introText.hide();
	}
	
	private void showSourceCode() {
		sourceCode.show();
		lang.nextStep();
	}
	
	private void showGraph() {
		graph.show(); 
		lang.nextStep();

	}
		
	private void highlightFirstLine() {
		sourceCode.highlight(0, 0, false);
		lang.nextStep();
	}

	private void initializeTortoise() {
		sourceCode.unhighlight(0, 0, false);
		sourceCode.highlight(1, 0, false);
		sourceCode.highlight(2, 0, false);
		
		tortoisePolygon.moveTo(null, null, path.getUpperTagFromIndex(0), null, null);	
		tortoisePolygon.show();
		
		lang.nextStep();
	}
		
	private void initializeHare() {
		sourceCode.unhighlight(1, 0, false);
		sourceCode.unhighlight(2, 0, false);
		sourceCode.highlight(3, 0, false);
		sourceCode.highlight(4, 0, false);
		
		harePolygon.moveTo(null, null, path.getLowerTagFromIndex(0), null, null);
		harePolygon.show();
		lang.nextStep();
	}
	
	private void showSwapTortoiseAndHareQuestion() {
		MultipleChoiceQuestionModel swapTortoiseAndHare = new MultipleChoiceQuestionModel("SwapTortoiseAndHare");
		swapTortoiseAndHare.setPrompt(this.translator.translateMessage("swapTortoiseAndHare"));
		swapTortoiseAndHare.addAnswer("swapTortoiseAndHareTrue", this.translator.translateMessage("interTrue"), 0, this.translator.translateMessage("swapTortoiseAndHareTrue"));
		swapTortoiseAndHare.addAnswer("swapTortoiseAndHareFalse", this.translator.translateMessage("interFalse"), 100, this.translator.translateMessage("swapTortoiseAndHareFalse"));
		lang.addMCQuestion(swapTortoiseAndHare);
	}
	
	private int detectCycle() {
		sourceCode.unhighlight(3, 0, false);
		sourceCode.unhighlight(4, 0, false);
		
		int tortoisePosition = 0;
		int harePosition = 0;
		
		do {
			// Calculate position
			tortoisePosition = cyclicFunction(tortoisePosition);
			harePosition = cyclicFunction(cyclicFunction(harePosition));
						
			// Head of Do-While Loop
			sourceCode.highlight(5, 0, false);
			lang.nextStep();
			
			// Move tortoise 
			sourceCode.unhighlight(5, 0, false);
			sourceCode.highlight(6, 0, false);
			tortoisePolygon.moveTo(null, null, path.getUpperTagFromIndex(tortoisePosition), null, null);
			var.set(this.varTortoisePosition, String.valueOf(tortoisePosition));
			lang.nextStep();
			
			// Move hare
			sourceCode.unhighlight(6, 0, false);
			sourceCode.highlight(7, 0, false);
			harePolygon.moveTo(null, null, path.getLowerTagFromIndex(harePosition), null, null);
			var.set(this.varHarePosition, String.valueOf(harePosition));
			lang.nextStep();

			sourceCode.unhighlight(7, 0, false);
		} while (tortoisePosition != harePosition);
		
		sourceCode.highlight(5, 0, false);
		
		var.set(this.varCycleAlreadyDetected, "Yes");
		
		lang.nextStep();
		
		return harePosition;
	}
	
	private void prepareCycleStartDetection() {
		// TODO: Erlaeuterung zum Schleifenende
		
		sourceCode.unhighlight(5, 0, false);
		sourceCode.highlight(8, 0, false);		
		lang.nextStep();
		
		sourceCode.unhighlight(8, 0, false);
		sourceCode.highlight(9, 0, false);
		tortoisePolygon.moveTo(null, null, path.getUpperTagFromIndex(0), null, null);
		lang.nextStep();
	}
	
	private void detectStartOfCycle(int lastHarePosition) {
		sourceCode.unhighlight(9, 0, false);
		
		int tortoisePosition = 0;
		int harePosition = lastHarePosition;
		
		while (tortoisePosition != harePosition) {
			// Calculate position
			tortoisePosition = cyclicFunction(tortoisePosition);
			harePosition = cyclicFunction(harePosition);
			
			// Head of While Loop
			sourceCode.highlight(10, 0, false);
			lang.nextStep();
			
			// Move tortoise 
			sourceCode.unhighlight(10, 0, false);
			sourceCode.highlight(11, 0, false);
			tortoisePolygon.moveTo(null, null, path.getUpperTagFromIndex(tortoisePosition), null, null);
			lang.nextStep();
			
			// Move hare
			sourceCode.unhighlight(11, 0, false);
			sourceCode.highlight(12, 0, false);
			harePolygon.moveTo(null, null, path.getLowerTagFromIndex(harePosition), null, null);
			lang.nextStep();
			
			// Increase counter
			sourceCode.unhighlight(12, 0, false);
			sourceCode.highlight(13, 0, false);
			lang.nextStep();
			
			sourceCode.unhighlight(13, 0, false);
		}
		
		sourceCode.highlight(10, 0, false);
		lang.nextStep();
	}
	
	private void finishCode() {
		sourceCode.unhighlight(10, 0, false);
		sourceCode.highlight(14, 0, false);
		lang.nextStep();
		
		sourceCode.unhighlight(14, 0, false);
		sourceCode.highlight(15, 0, false);
		lang.nextStep();
		
		sourceCode.unhighlight(15, 0, false);
		sourceCode.highlight(17, 0, false);
		lang.nextStep();
	}
	
	private void showErrorProneQuestion() {
		MultipleChoiceQuestionModel errorProne = new MultipleChoiceQuestionModel("ErrorProne");
		errorProne.setPrompt(this.translator.translateMessage("errorProne"));
		errorProne.addAnswer("errorProneTrue", this.translator.translateMessage("interTrue"), 0, this.translator.translateMessage("errorProneTrue"));
		errorProne.addAnswer("errorProneFalse", this.translator.translateMessage("interFalse"), 100, this.translator.translateMessage("errorProneFalse"));
		lang.addMCQuestion(errorProne);
	}
	
	private void showOutroText() {
		// Hide everything else
		tortoisePolygon.hide();
		harePolygon.hide();
		graph.hide();
		sourceCode.hide();
		
		outroText.show();
	}
		
	// Cyclic function
	private int cyclicFunction(int input) {
		int funcValue;
		int lastElement = nonCircleLength + circleLength - 1;

		if (input < lastElement) {
			funcValue = input + 1;
		} else {
			funcValue = nonCircleLength;
		}
		return funcValue;
	}
}