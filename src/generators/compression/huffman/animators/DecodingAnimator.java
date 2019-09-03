package generators.compression.huffman.animators;

import generators.compression.huffman.guielements.tree.Tree;
import generators.compression.huffman.guielements.tree.TreeNode;
import generators.compression.huffman.style.HuffmanStyle;
import generators.compression.huffman.utils.HuffmanCodingUtils;

import java.util.ResourceBundle;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.MsTiming;
import algoanim.util.Offset;

public class DecodingAnimator extends ChapterAnimator {

	private static final String CHAPTER_LABEL = "Decompression";

	private Text headline;
	private SourceCode decodeText1;
	private SourceCode decodeText2;
	private StringArray inputArray;
	private ArrayMarker arrayMarker;
	private Text decompressedLabel;
	private Text decompressed;
	private Tree tree;
	private String encoding;

	public DecodingAnimator(Language lang, HuffmanStyle huffmanStyle,
			ResourceBundle messages, Text headline, Tree tree, String encoding) {

		super(lang, huffmanStyle, messages, CHAPTER_LABEL);

		this.headline = headline;
		this.tree = tree;
		this.encoding = encoding;
	}

	@Override
	public void animate() {

		super.animate();

		showIntroText();

		animateDecoding();
	}

	private void showIntroText() {

		decodeText1 = lang.newSourceCode(new Offset(0, 30, headline,
				AnimalScript.DIRECTION_SW), "decodeText1", null,
				(SourceCodeProperties) huffmanStyle
						.getProperties(HuffmanStyle.SOURCECODE));
		String text = messages.getString("decodeText1");
		decodeText1.addCodeLine(text, "", 0, null);

		lang.nextStep();

		decodeText2 = lang.newSourceCode(new Offset(0, 5, decodeText1,
				AnimalScript.DIRECTION_SW), "decodeText2", null,
				(SourceCodeProperties) huffmanStyle
						.getProperties(HuffmanStyle.SOURCECODE));
		text = messages.getString("decodeText2part1");
		decodeText2.addCodeLine(text, "", 0, null);
		text = messages.getString("decodeText2part2");
		decodeText2.addCodeLine(text, "", 0, null);

		lang.nextStep();
	}

	private void animateDecoding() {

		String[] bits = HuffmanCodingUtils.getStringArray(encoding);

		inputArray = lang.newStringArray(new Offset(0, 45, decodeText2,
				AnimalScript.DIRECTION_SW), bits, "inputArray", null,
				(ArrayProperties) huffmanStyle
						.getProperties(HuffmanStyle.TRAVERSE_ARRAY));

		arrayMarker = lang.newArrayMarker(inputArray, 0, "iMarker", null,
				(ArrayMarkerProperties) huffmanStyle
						.getProperties(HuffmanStyle.MARKER));

		decompressedLabel = lang.newText(new Offset(0, 20, inputArray, AnimalScript.DIRECTION_SW),
				"Decompressed input: ", "decompressedLabel", null,
				(TextProperties) huffmanStyle
						.getProperties(HuffmanStyle.PLAINTEXT));

		decompressed = lang.newText(new Offset(5, 0, decompressedLabel,
				AnimalScript.DIRECTION_NE), "", "decompressed", null,
				(TextProperties) huffmanStyle
						.getProperties(HuffmanStyle.PLAINTEXT));

		lang.nextStep();
		
		tree.show();

		tree.moveTo(decompressedLabel, new MsTiming(1000));

		lang.nextStep();

		TreeNode root = tree.getRoot();

		String decoding = "";

		TreeNode node = root;
		for (int i = 0; i < bits.length; i++) {

			String bit = bits[i];

			if (node.getLeftNode() == null && node.getRightNode() == null) {

				int id = node.getID();
				String symbol = tree.getSymbol(id);
				decoding += symbol;
				decompressed.setText(decoding, null, null);
				tree.unHighlightPath(id);
				node = root;
				i -= 1;
			} else if (bit.equals("0")) {

				node = node.getLeftNode();
				tree.highlightPath(node.getID());
				arrayMarker.increment(null, null);
			} else {

				node = node.getRightNode();
				tree.highlightPath(node.getID());
				arrayMarker.increment(null, null);
			}

			lang.nextStep();
		}
		
		int id = node.getID();
		String symbol = tree.getSymbol(id);
		decoding += symbol;
		decompressed.setText(decoding, null, null);
		tree.unHighlightPath(id);
		node = root;
	}
}
