package generators.graph.pagerank;

import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.graph.util.FileLoader;

public class PageRank implements Generator {
	/**
	 * The animal-language object 
	 */
	private Language lang;

	public void init() {
		this.lang = new AnimalScript("PageRank", "Marc Arndt", 1000, 1000);
		this.lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		// get the primitives
		Graph graph = (Graph) primitives.get("inputGraph");
		boolean danglingNodes = (Boolean) primitives.get("danglingNodes");
		double jumpOperator = (Double) primitives.get("jumpOperator");
		double terminationThreshold = (Double) primitives.get("terminationThreshold");
		
		// get the properties
		GraphProperties graphProperties = (GraphProperties) props.getPropertiesByName("graphProperties");
		
		TextProperties algorithmHeaderProps = (TextProperties) props.getPropertiesByName("algorithmHeaderProperties");
		algorithmHeaderProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,Font.BOLD, 24));
		
		TextProperties descriptionHeaderProps = (TextProperties) props.getPropertiesByName("descriptionHeaderProperties");
		descriptionHeaderProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF,Font.BOLD, 21));
		
		TextProperties normalTextProps = (TextProperties) props.getPropertiesByName("normalTextProperties");
		
		SourceCodeProperties sourceCodeProperties = (SourceCodeProperties) props.getPropertiesByName("sourceCodeProperties");
		
		MatrixProperties matrixProperties = (MatrixProperties) props.getPropertiesByName("matrixProperties");
		
		// set the algorithm specific values
		Text header = lang.newText(new Coordinates(5, 5), "PageRank-Algorithmus (Power Iteration Methode)", "Header", null, algorithmHeaderProps); 
		
		GraphInfoGUI graphInfo = new GraphInfoGUI(lang, header, graph, graphProperties);
		
		PageRankIntroductionGUI introductionGui = new PageRankIntroductionGUI(lang, header, descriptionHeaderProps, normalTextProps);
		PageRankAlgorithmGUI algorithmGui = new PageRankAlgorithmGUI(lang, header, graphInfo, new ThresholdTermination(terminationThreshold), danglingNodes, jumpOperator, matrixProperties, sourceCodeProperties, normalTextProps);
		PageRankResultGUI resultGui = new PageRankResultGUI(lang, header, algorithmGui, matrixProperties, descriptionHeaderProps, normalTextProps);
		
		// show introduction
		introductionGui.show();
		introductionGui.nextStep("Beschreibung");
		introductionGui.hide();
		
		// perform the PageRank algorithm
		algorithmGui.show();
		
		// show endresult
		algorithmGui.hide();
		resultGui.show();
		resultGui.nextStep("Endergebnisse");
		
		// return the script
		return lang.toString();
	}
	
	public String getName() {
		return "PageRank";
	}

	public String getAlgorithmName() {
		return "PageRank-Algorithmus (Power Iteration Methode)";
	}

	public String getAnimationAuthor() {
		return "Marc Arndt";
	}

	public String getDescription() {
		return FileLoader.loadContent("generators/graph/pagerank/description.txt", "Couldn't find description text");
	}

	public String getCodeExample() {
		return FileLoader.loadContent("generators/graph/pagerank/sourcecode.txt", "Couldn't find source code example");	
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}
}
