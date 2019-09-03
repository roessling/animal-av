/*
 * DegreeCentrality.java
 * Christoph Niese, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import java.awt.Font;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

public class DegreeCentrality implements Generator
{
	public SourceCode createSourceCode()
	{
	    SourceCode sourceCode = lang.newSourceCode(new Coordinates(20, 120), "sourceCode", null, sourceCodeProperties);
	    
	    sourceCode.addCodeLine("double[] DegreeCentralities(Graph graph)", null, 0, null);
	    sourceCode.addCodeLine("{", null, 0, null);
	    sourceCode.addCodeLine("double[] centralities = new double[graph.getSize()];", null, 1, null);
	    sourceCode.addCodeLine("", null, 1, null);
	    sourceCode.addCodeLine("for (int i = 0; i < graph.getSize(); i++)", null, 1, null);
	    sourceCode.addCodeLine("{", null, 1, null);
	    sourceCode.addCodeLine("centralities[i] = getDegree(i) / (double)graph.getSize();", null, 2, null);
	    sourceCode.addCodeLine("}", null, 1, null);
	    sourceCode.addCodeLine("", null, 1, null);
	    sourceCode.addCodeLine("return centralities;", null, 1, null);
	    sourceCode.addCodeLine("}", null, 0, null);
	    
	    return sourceCode;
	}
	
	private void createFrame()
	{
		lang.newRect(new Coordinates(10, 10), new Coordinates(1600, 60), "FrameTitleRect", null);
    	lang.newText(new Coordinates(20, 38), getName(), "FrameTitleText", null)
    	.setFont(new Font("SansSerif", Font.BOLD, 42), null, null);
    	
    	lang.newRect(new Coordinates(10, 70), new Coordinates(600, 800), "FrameLeftRect", null);
    	lang.newRect(new Coordinates(610, 70), new Coordinates(1600, 800), "FrameRightRect", null);
	}
	
	private void createDescription()
	{
		ShowMultilineTextForOneStep(getDescription(), new Coordinates(20, 80), 100, null);
	}
	
	private Graph createGraph()
	{
		Node[] nodes = new Node[templateGraph.getSize()];
	    String[] nodeLabels = new String[templateGraph.getSize()];	    
	    for (int i = 0; i < templateGraph.getSize(); i++)
	    {
	      nodes[i] = templateGraph.getNode(i);
	      nodeLabels[i] = templateGraph.getNodeLabel(i);
	    }
	    
	    Graph newGraph = lang.newGraph(
	    	templateGraph.getName(),
	    	templateGraph.getAdjacencyMatrix(),
	        nodes, nodeLabels,
	        templateGraph.getDisplayOptions(),
	        graphProperties);
	    newGraph.moveBy(null, 620, 80, null, null);
	    
	    return newGraph;
	}
	
    private void highlightNodeAndAllEdges(int nodeID, boolean highlight)
    {    	
    	if (highlight)
		{
    		graph.highlightNode(nodeID, null, null);
		}
		else
		{
			graph.unhighlightNode(nodeID, null, null);
		}
    	
    	int[][] m = graph.getAdjacencyMatrix();
		
		for (int i = 0; i < graph.getSize(); ++i)
		{
			if (m[i][nodeID] != 0)
			{
				if (highlight)
				{
					graph.highlightEdge(nodeID, i, null, null);
				}
				else
				{
					graph.unhighlightEdge(nodeID, i, null, null);
				}
			}
			else if (m[nodeID][i] != 0)
			{
				if (highlight)
				{
					graph.highlightEdge(i, nodeID, null, null);
				}
				else
				{
					graph.unhighlightEdge(i, nodeID, null, null);
				}
			}
		}
    }
    
	private int getDegree(int nodeID)
	{
		int degree = 0;
		int[][] m = graph.getAdjacencyMatrix();
		
		for (int i = 0; i < graph.getSize(); ++i)
		{
			if (m[i][nodeID] != 0 || m[nodeID][i] != 0)
			{
				degree++;
			}
		}
		
		return degree;
	}
	
    private String createScript()
    {
    	createFrame();    	
		createDescription();
		
		SourceCode sourceCode = createSourceCode();		
		graph = createGraph();
		lang.nextStep("Daten");		
				 
	    Text infoText1 = lang.newText(new Coordinates(20, 80), "Lege ein Array für die Ergebnisse an.", "infotext1", null);
		infoText1.setFont(new Font("SansSerif", Font.BOLD, 18), null, null);
		Text infoText2 = lang.newText(new Coordinates(20, 100), "Die Länge ist entsprechend der Anzahl der Knoten gleich " + graph.getSize(), "infotext2", null);
		infoText2.setFont(new Font("SansSerif", Font.BOLD, 18), null, null);
		sourceCode.highlight(2);
		double[] centralities = new double[graph.getSize()];
		Variables v = lang.newVariables();		
		v.declare("string", "centralities");
		v.set("centralities", Arrays.toString(centralities));
		lang.nextStep("Initialisierung");		
	    sourceCode.unhighlight(2);
	    v.declare("int", "i");
	    
	    for (int i = 0; i < graph.getSize(); i++)
	    {
	    	v.set("i", i + "");
	    	sourceCode.highlight(6);
	    	centralities[i] = Math.round(100.0 * getDegree(i) / (double)graph.getSize()) / 100.0;
	    	v.set("centralities", Arrays.toString(centralities));
	    	infoText1.setText("Der Knoten " + graph.getNodeLabel(i) + " hat " + getDegree(i) + " Kanten", null, null);
	    	infoText2.setText("Zentralität = " + getDegree(i) + " / " + graph.getSize() + " = " + centralities[i], null, null);
	    	highlightNodeAndAllEdges(i, true);	    	
			lang.nextStep((i + 1) + ". Iteration");
	    	sourceCode.unhighlight(6);
	    	highlightNodeAndAllEdges(i, false);
	    }
	    
	    infoText1.setText("FERTIG! Die Zentralitäten sind", null, null);
		infoText2.setText(Arrays.toString(centralities), null, null);
	    sourceCode.highlight(9);
	    lang.nextStep("Fazit");
	    return lang.toString();
    }
	
	//// GENERATOR /////////////////////////////////////////////////////////////
	
    private Language lang;

    private Graph templateGraph;
    private Graph graph;
    
    private SourceCodeProperties sourceCodeProperties;    
    private GraphProperties graphProperties;
    
	public DegreeCentrality()
	{
		init();
	}
	
	public void init()
	{
		lang = Language.getLanguageInstance(
				AnimationType.ANIMALSCRIPT,
				"Gradzentralität",
				"Christoph Niese",
				1600, 800);
		lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
    {
    	templateGraph = (Graph)primitives.get("graph");
    	    	
		sourceCodeProperties = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProperties");
		graphProperties = (GraphProperties)props.getPropertiesByName("graphProperties");
		
        return createScript();
    }

    public String getName()
    {
        return "Gradzentralität";
    }

    public String getAlgorithmName()
    {
        return "Gradzentralität";
    }

    public String getAnimationAuthor()
    {
        return "Christoph Niese";
    }

    public String getDescription()
    {
        return
        	"Die Gradzentralität eines Knotens ist die Anzahl seiner Nachbarknoten geteilt durch " +
        	"die Anzahl aller Knoten des Graphs.\n" +
        	"Dieser Wert zeigt an, wie 'wichtig' ein Knoten ist. Z.B. wer in einem sozialen Netzwerk ein " +
        	"'Influencer' ist, der viele Leute mit seinen Posts erreicht, oder auch von wo sich eine " +
        	"Krankheit am schnellsten verbreiten wird.";
    }

    public String getCodeExample()
    {
        return
    		"double[] DegreeCentralities(Graph graph)\n" +
		    "{\n" +
		    "    double[] centralities = new double[graph.getSize()];\n" +
		    "    \n" +
		    "    for (int i = 0; i < graph.getSize(); i++)\n" +
		    "    {\n" +
		    "        centralities[i] = getDegree(i) / (double)graph.getSize();\n" +
		    "    }\n" +
		    "    \n" +
		    "    return centralities;\n" +
		    "}";
    }

    public String getFileExtension()
    {
        return "asu";
    }

    public Locale getContentLocale()
    {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType()
    {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
    }

    public String getOutputLanguage()
    {
        return Generator.JAVA_OUTPUT;
    }
        
    //// UTILITY ///////////////////////////////////////////////////////////////
    
    private void ShowMultilineTextForOneStep(String text, Coordinates coordinates, int maximumLineLength, DisplayOptions displayOptions)
	{
		String[] tokens = text.split("\\s+");
		LinkedList<String> lines = new LinkedList<String>();
		String currentLine = "";
		for (String t : tokens)
		{
			if (currentLine.length() + t.length() < maximumLineLength)
			{
				if (t.charAt(t.length() - 1) == '\n')
				{
					lines.add(currentLine);
					currentLine = "";
				}
				else
				{
					currentLine += t + " ";
				}
			}
			else
			{
				lines.add(currentLine);
				currentLine = t + " ";
			}
		}
		lines.add(currentLine);
		
		int x = coordinates.getX();
		int y = coordinates.getY();
		LinkedList<Text> textPrimitives = new LinkedList<Text>();
		
		for (String l : lines)
		{
			textPrimitives.add(lang.newText(new Coordinates(x, y), l, "DescriptionText" + textPrimitives.size(), displayOptions));
			y += 20;
		}
		
		lang.nextStep("Einleitung");
		
		for (Text t : textPrimitives)
		{
			t.hide();
		}
	}
}
