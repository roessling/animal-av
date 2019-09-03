package generators.compression.huffman;

import generators.compression.huffman.animators.ComplexityAnimator;
import generators.compression.huffman.animators.DecodingAnimator;
import generators.compression.huffman.animators.HuffmanAlgorithmAnimator;
import generators.compression.huffman.animators.IntroductionAnimator;
import generators.compression.huffman.animators.ResultAnimator;
import generators.compression.huffman.guielements.EncodingTable;
import generators.compression.huffman.guielements.distributiontable.DistributionTable;
import generators.compression.huffman.guielements.priorityqueue.PQInsertCounter;
import generators.compression.huffman.guielements.tree.Tree;
import generators.compression.huffman.style.HuffmanStyle;
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

public class HuffmanCoding implements Generator {

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

	private HuffmanStyle huffmanStyle;

	public void init() {
		lang = new AnimalScript("Huffman Coding",
				"Sebasian Fach, Peter Kl&ouml;ckner", 800, 600);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		inputString = (String) primitives.get("inputString");

		headlineProps = (TextProperties) props.getPropertiesByName("headline");
		sourceProps = (SourceCodeProperties) props
				.getPropertiesByName("sourceCode");
		arrayPropsFirstCol = (ArrayProperties) props
				.getPropertiesByName("headColumns");
		arrayPropsRest = (ArrayProperties) props.getPropertiesByName("columns");
		noProps = (TextProperties) props.getPropertiesByName("nodeNumbers");

		animate();

		return lang.toString();
	}

	public String getName() {
		return "Huffman Coding";
	}

	public String getAlgorithmName() {
		return "Huffman Coding";
	}

	public String getAnimationAuthor() {
		return "Sebasian Fach, Peter Kl&ouml;ckner";
	}

	public String getDescription() {
		return "Huffman Coding is an algorithm for lossless data compression. It generates the most efficient symbol-by-symbol coding."
				+ "\n"
				+ "Symbol-by-symbol means that every symbol in the input is mapped to an (in this case variable length) code."
				+ "\n"
				+ "The idea of Huffman Coding is that less frequent symbols should be mapped to codes of shorter length."
				+ "\n"
				+ "The core algorithm starts with a distribution table, which contains input symbols mapped to their probability of occurance."
				+ "\n"
				+ "Then it generates a binary tree, whose leaves contain the input symbols. The path from the root of the tree to one symbol represents the code of the symbol."
				+ "\n"
				+ "Each edge is one bit of the code, whereby edges to left child nodes represent a zero and edges to right child nodes represent a one."
				+ "\n"
				+ "Using Landau symbols, the worst case running time of Huffman Coding is &Omicron;(n * log(n)).";
	}

	public String getCodeExample() {
		return "1. For each symbol create a leaf node" + "\n"
				+ "    and add it to the priority queue." + "\n" + "\n"
				+ "2. While there is more than one node in the queue:" + "\n"
				+ "\n" + "    1. Remove the two nodes of highest priority"
				+ "\n" + "        (lowest probability) from the queue" + "\n"
				+ "    2. Create a new node and add these two nodes" + "\n"
				+ "        as children. The probability is equal to the" + "\n"
				+ "        sum of the two nodes' probabilities." + "\n"
				+ "    3. Add the new node to the queue." + "\n" + "\n"
				+ "3. The remaining node is the root node of the tree.";
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

		huffmanStyle = new HuffmanStyle();
		huffmanStyle.setProperties(HuffmanStyle.HEADLINE, headlineProps);
		huffmanStyle.setProperties(HuffmanStyle.SOURCECODE, sourceProps);
		huffmanStyle.setProperties(HuffmanStyle.ARRAY_FIRST_COL,
				arrayPropsFirstCol);
		huffmanStyle.setProperties(HuffmanStyle.ARRAY_REST, arrayPropsRest);
		huffmanStyle.setProperties(HuffmanStyle.NUMBER, noProps);
		huffmanStyle.fillDefaultValues();

		ResourceBundle messages = ResourceBundle.getBundle(
				"generators/compression/huffman/MessagesBundle",
				getContentLocale());

		headline = lang.newText(new Coordinates(20, 30), "Huffman coding",
				"headline", null, headlineProps);

		IntroductionAnimator introAnimator = new IntroductionAnimator(lang,
				huffmanStyle, messages, headline, inputString);
		introAnimator.animate();

		DistributionTable distrTable = introAnimator.getDistrTable();
		HuffmanAlgorithmAnimator huffmanAnimator = new HuffmanAlgorithmAnimator(
				lang, huffmanStyle, messages, headline, distrTable);
		huffmanAnimator.animate();

		EncodingTable encTable = huffmanAnimator.getEncTable();
		ResultAnimator resultAnimator = new ResultAnimator(lang, huffmanStyle,
				messages, headline, inputString, encTable,
				distrTable.getSymbols());
		resultAnimator.animate();

		PQInsertCounter insertCounter = huffmanAnimator.getInsertCounter();
		ComplexityAnimator complexityAnimator = new ComplexityAnimator(lang,
				huffmanStyle, messages, headline, insertCounter);
		complexityAnimator.animate();

		Tree tree = huffmanAnimator.getTree();
		String encoding = resultAnimator.getEncodedInputString();
		DecodingAnimator decodingAnimator = new DecodingAnimator(lang,
				huffmanStyle, messages, headline, tree, encoding);
		decodingAnimator.animate();
	}
}