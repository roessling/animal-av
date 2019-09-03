package generators.compression.shannon_fano;

import generators.compression.shannon_fano.animators.ComplexityAnimator;
import generators.compression.shannon_fano.animators.DecodingAnimator;
import generators.compression.shannon_fano.animators.IntroductionAnimator;
import generators.compression.shannon_fano.animators.ResultAnimator;
import generators.compression.shannon_fano.animators.ShannonFanoAlgorithmAnimator;
import generators.compression.shannon_fano.guielements.EncodingTable;
import generators.compression.shannon_fano.guielements.distributiontable.DistributionTable;
import generators.compression.shannon_fano.guielements.nodearray.NodeInsertCounter;
import generators.compression.shannon_fano.guielements.tree.Tree;
import generators.compression.shannon_fano.style.ShannonFanoStyle;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class ShannonFanoCoding implements Generator {

	/**
	 * The concrete language object used for creating output
	 */
	private Language lang;

	/**
	 * all the Properties, which can be set by the user
	 */
	private TextProperties headlineProps;
	private SourceCodeProperties sourceProps;
	private ArrayProperties arrayPropsFirstCol;
	private ArrayProperties arrayPropsRest;
	private TextProperties noProps;

	private Text headline;

	private String inputString;

	private ShannonFanoStyle style;

	public void init() {
		lang = new AnimalScript("Shannon-Fano Coding", "Sebasian Fach, Peter Kl&ouml;ckner", 800, 600);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {

		inputString = (String) primitives.get("inputString");

		headlineProps = (TextProperties) props.getPropertiesByName("headline");
		sourceProps = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
		arrayPropsFirstCol = (ArrayProperties) props.getPropertiesByName("headColumns");
		arrayPropsRest = (ArrayProperties) props.getPropertiesByName("columns");
		noProps = (TextProperties) props.getPropertiesByName("nodeNumbers");

		animate();

		return lang.toString();
	}

	public String getName() {
		return "Shannon-Fano Coding";
	}

	public String getAlgorithmName() {
		return "Shannon-Fano Coding";
	}

	public String getAnimationAuthor() {
		return "Sebasian Fach, Peter Kl&ouml;ckner";
	}

	public String getDescription() {
		return "Shannon-Fano coding is an algorithm for lossless data compression. It generates an efficient symbol-by-symbol coding."
				+ "\n"
				+ "Symbol-by-symbol means that every symbol in the input is mapped to an (in this case variable length) code."
				+ "\n"
				+ "The idea of Shannon-Fano coding is that less frequent symbols should be mapped to codes of shorter length."
				+ "\n"
				+ "The core algorithm starts with a distribution table, which contains input symbols mapped to their probability of occurance."
				+ "\n"
				+ "Then it generates a binary tree, whose leaves contain the input symbols. The path from the root of the tree to one symbol represents the code of the symbol."
				+ "\n"
				+ "Each edge is one bit of the code, whereby edges to left child nodes represent a zero and edges to right child nodes represent a one."
				+ "\n"
				+ "Since the Shannon-Fano algorithm, which has been proposed in 1948, does not always generate an optimal code, David A. Huffman developed a different algorithm in 1952:"
				+ "\n"
				+ "The Huffman algorithm, which works bottom-up instead of top-down (Shannon-Fano) and always produces an optimal tree for any given probabilities."
				+ "\n"
				+ "Using Landau symbols, the worst case complexity of Shannon-Fano coding is &Omicron;(n * log(n)).";
	}

	public String getCodeExample() {
		return "1. For each symbol create a leaf node"
				+ "\n"
				+ "    and sort these nodes according to the frequency, with the most frequently occurring symbols at the left and the least common at the right."
				+ "\n"
				+ "\n"
				+ "2. Divide the list into two parts, so that the total frequency of the left part is as close to the total of the right as possible.";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_COMPRESSION);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	private void animate() {

		lang.setStepMode(true);

		style = new ShannonFanoStyle();
		style.setProperties(ShannonFanoStyle.HEADLINE, headlineProps);
		style.setProperties(ShannonFanoStyle.SOURCECODE, sourceProps);
		style.setProperties(ShannonFanoStyle.ARRAY_FIRST_COL, arrayPropsFirstCol);
		style.setProperties(ShannonFanoStyle.ARRAY_REST, arrayPropsRest);
		style.setProperties(ShannonFanoStyle.NODE_ID, noProps);
		style.fillDefaultValues();

		ResourceBundle messages = ResourceBundle.getBundle("generators/compression/shannon_fano/MessagesBundle",
				getContentLocale());

		headline = lang.newText(new Coordinates(20, 30), "Shannon-Fano coding", "headline", null, headlineProps);

		IntroductionAnimator introAnimator = new IntroductionAnimator(lang, style, messages, headline, inputString);
		introAnimator.animate();

		DistributionTable distrTable = introAnimator.getDistrTable();
		ShannonFanoAlgorithmAnimator shannonFanoAnimator = new ShannonFanoAlgorithmAnimator(lang, style, messages,
				headline, distrTable);
		shannonFanoAnimator.animate();

		EncodingTable encTable = shannonFanoAnimator.getEncTable();
		ResultAnimator resultAnimator = new ResultAnimator(lang, style, messages, headline, inputString, encTable,
				distrTable.getSymbols());
		resultAnimator.animate();

		NodeInsertCounter insertCounter = shannonFanoAnimator.getInsertCounter();
		ComplexityAnimator complexityAnimator = new ComplexityAnimator(lang, style, messages, headline,
				insertCounter);
		complexityAnimator.animate();

		Tree tree = shannonFanoAnimator.getTree();
		String encoding = resultAnimator.getEncodedInputString();
		DecodingAnimator decodingAnimator = new DecodingAnimator(lang, style, messages, headline, tree, encoding);
		decodingAnimator.animate();
	}
}
