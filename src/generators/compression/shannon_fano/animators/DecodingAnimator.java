package generators.compression.shannon_fano.animators;

import generators.compression.shannon_fano.guielements.tree.AbstractNode;
import generators.compression.shannon_fano.guielements.tree.Tree;
import generators.compression.shannon_fano.guielements.tree.TreeNode;
import generators.compression.shannon_fano.style.ShannonFanoStyle;
import generators.compression.shannon_fano.utils.ShannonFanoCodingUtils;

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

	/**
	 * Constructor
	 * 
	 * @param lang
	 * @param shannonFanoStyle
	 * @param messages
	 * @param headline
	 * @param tree
	 * @param encoding
	 */
	public DecodingAnimator(Language lang, ShannonFanoStyle shannonFanoStyle,
			ResourceBundle messages, Text headline, Tree tree, String encoding) {

		super(lang, shannonFanoStyle, messages, CHAPTER_LABEL);

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
				(SourceCodeProperties) style
						.getProperties(ShannonFanoStyle.SOURCECODE));
		String text = messages.getString("decodeText1");
		decodeText1.addCodeLine(text, "", 0, null);

		lang.nextStep();

		decodeText2 = lang.newSourceCode(new Offset(0, 5, decodeText1,
				AnimalScript.DIRECTION_SW), "decodeText2", null,
				(SourceCodeProperties) style
						.getProperties(ShannonFanoStyle.SOURCECODE));
		text = messages.getString("decodeText2part1");
		decodeText2.addCodeLine(text, "", 0, null);
		text = messages.getString("decodeText2part2");
		decodeText2.addCodeLine(text, "", 0, null);

		lang.nextStep();
	}

	private void animateDecoding() {
		
		String[] bits = ShannonFanoCodingUtils.getStringArray(encoding);

		inputArray = lang.newStringArray(new Offset(0, 45, decodeText2,
				AnimalScript.DIRECTION_SW), bits, "inputArray", null,
				(ArrayProperties) style
						.getProperties(ShannonFanoStyle.TRAVERSE_ARRAY));

		arrayMarker = lang.newArrayMarker(inputArray, 0, "iMarker", null,
				(ArrayMarkerProperties) style
						.getProperties(ShannonFanoStyle.MARKER));

		decompressedLabel = lang.newText(new Offset(0, 20, inputArray, AnimalScript.DIRECTION_SW),
				"Decompressed input: ", "decompressedLabel", null,
				(TextProperties) style
						.getProperties(ShannonFanoStyle.PLAINTEXT));

		decompressed = lang.newText(new Offset(5, 0, decompressedLabel,
				AnimalScript.DIRECTION_NE), "", "decompressed", null,
				(TextProperties) style
						.getProperties(ShannonFanoStyle.PLAINTEXT));

		lang.nextStep();
		
		tree.show();

		tree.moveBy(-505, 60, new MsTiming(1000));

		lang.nextStep();

		AbstractNode root = tree.getRoot();

		String decoding = "";

		AbstractNode node = root;
		for (int i = 0; i < bits.length; i++) {

			String bit = bits[i];

			if (node instanceof TreeNode) {
				
				TreeNode treeNode = (TreeNode) node;

				if (treeNode.isLeaf()) {
					decoding += treeNode.getSymbol();
					decompressed.setText(decoding, null, null);
					tree.unHighlightPath(node.getId());
					node = root;
					i--;
				} else if (bit.equals("0")) {
					node = node.getLeftNode();
					tree.highlightPath(node.getId());
					arrayMarker.increment(null, null);
				} else {
					node = node.getRightNode();
					tree.highlightPath(node.getId());
					arrayMarker.increment(null, null);
				}

				lang.nextStep();
			
			} else {
				i++;
			}
		}
		
		decoding += ((TreeNode) node).getSymbol();
		decompressed.setText(decoding, null, null);
		tree.unHighlightPath(node.getId());
		node = root;
	}
}
