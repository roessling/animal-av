package generators.compression.huffman.animators;

import generators.compression.huffman.coding.Encoder;
import generators.compression.huffman.guielements.EncodingTable;
import generators.compression.huffman.style.HuffmanStyle;

import java.util.ResourceBundle;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Offset;

public class ResultAnimator extends ChapterAnimator {

	private static final String CHAPTER_LABEL = "Resulting Compression";

	private String inputString;

	private Text headline;
	private EncodingTable encTable;
	private Text inputStringLabel;
	private Text inputStringText;
	private SourceCode resultText1;
	private SourceCode encoding;
	private SourceCode resultText2;
	private SourceCode resultText3;
	private SourceCode resultText4;
	private SourceCode resultText5;

	private char[] chars;

	private String encodedInputString;

	public String getEncodedInputString() {
		return encodedInputString;
	}

	public ResultAnimator(Language lang, HuffmanStyle huffmanStyle,
			ResourceBundle messages, Text headline, String inputString,
			EncodingTable encTable, char[] chars) {
		super(lang, huffmanStyle, messages, CHAPTER_LABEL);

		this.headline = headline;
		this.inputString = inputString;

		this.encTable = encTable;

		this.chars = chars;
	}

	@Override
	public void animate() {
		super.animate();

		animateResult();

		doTransition();
	}

	/**
	 * The huffman tree and the encoding table is generated. Now show the actual
	 * encoding of the input string. Furthermore, show how many bits could be
	 * saved compared to a trivial encoding.
	 */
	private void animateResult() {

		StringArray encodingTableHeadCol = encTable.getHeadElement();
		String[] encodingTableHeadData = new String[3];
		encodingTableHeadData[0] = encodingTableHeadCol.getData(0);
		encodingTableHeadData[1] = encodingTableHeadCol.getData(1);

		StringArray offsetReference = lang.newStringArray(new Offset(0, 50,
				headline, AnimalScript.DIRECTION_SW), encodingTableHeadData,
				"alignDummy", null, (ArrayProperties) huffmanStyle
						.getProperties(HuffmanStyle.ARRAY_FIRST_COL));
		offsetReference.hide();

		inputStringLabel = lang.newText(new Offset(0, -15, offsetReference,
				AnimalScript.DIRECTION_SW), "Input string: ",
				"inputStringLabel", null, (TextProperties) huffmanStyle
						.getProperties(HuffmanStyle.PLAINTEXT));

		inputStringText = lang.newText(new Offset(5, 0, inputStringLabel,
				AnimalScript.DIRECTION_NE), inputString, "inputStringText",
				null, (TextProperties) huffmanStyle
						.getProperties(HuffmanStyle.PLAINTEXT));

		resultText1 = lang.newSourceCode(new Offset(0, 5, inputStringLabel,
				AnimalScript.DIRECTION_SW), "resultText1", null,
				(SourceCodeProperties) huffmanStyle
						.getProperties(HuffmanStyle.SOURCECODE));
		resultText1.addCodeLine(messages.getString("resultText1"), "", 0, null);

		lang.nextStep();

		Encoder encoder = new Encoder(encTable);
		encodedInputString = encoder.encode(inputString);
		encoding = lang.newSourceCode(new Offset(0, -13, resultText1,
				AnimalScript.DIRECTION_SW), "encoding", null,
				(SourceCodeProperties) huffmanStyle
						.getProperties(HuffmanStyle.SOURCECODE));
		encoding.addCodeLine(" " + encodedInputString, "", 0, null);

		lang.nextStep();

		resultText2 = lang.newSourceCode(new Offset(0, 5, encoding,
				AnimalScript.DIRECTION_SW), "resultText2", null,
				(SourceCodeProperties) huffmanStyle
						.getProperties(HuffmanStyle.SOURCECODE));
		String text = messages.getString("resultText2part1")
				+ encodedInputString.length()
				+ messages.getString("resultText2part2");
		resultText2.addCodeLine(text, "", 0, null);

		lang.nextStep();

		resultText3 = lang.newSourceCode(new Offset(0, 5, resultText2,
				AnimalScript.DIRECTION_SW), "resultText4", null,
				(SourceCodeProperties) huffmanStyle
						.getProperties(HuffmanStyle.SOURCECODE));
		resultText3.addCodeLine(messages.getString("resultText3_1"), "", 0,
				null);

		int trivialEncodingBits = (int) Math
				.ceil((Math.log(chars.length) / Math.log(2)));

		text = messages.getString("resultText3_2part1") + chars.length
				+ messages.getString("resultText3_2part2") + chars.length
				+ messages.getString("resultText3_2part3")
				+ trivialEncodingBits
				+ messages.getString("resultText3_2part4");
		resultText3.addCodeLine(text, "", 0, null);

		text = messages.getString("resultText3_3part1") + inputString.length()
				+ messages.getString("resultText3_3part2");
		resultText3.addCodeLine(text, "", 0, null);

		lang.nextStep();

		text = messages.getString("resultText4part1") + trivialEncodingBits
				+ messages.getString("resultText4part2") + inputString.length()
				+ messages.getString("resultText4part3")
				+ (trivialEncodingBits * inputString.length())
				+ messages.getString("resultText4part4");
		resultText4 = lang.newSourceCode(new Offset(0, 5, resultText3,
				AnimalScript.DIRECTION_SW), "resultText4", null,
				(SourceCodeProperties) huffmanStyle
						.getProperties(HuffmanStyle.SOURCECODE));
		resultText4.addCodeLine(text, "", 0, null);

		lang.nextStep();

		text = messages.getString("resultText5part1")
				+ (trivialEncodingBits * inputString.length() - encodedInputString
						.length()) + messages.getString("resultText5part2");
		resultText5 = lang.newSourceCode(new Offset(0, 5, resultText4,
				AnimalScript.DIRECTION_SW), "resultText5", null,
				(SourceCodeProperties) huffmanStyle
						.getProperties(HuffmanStyle.SOURCECODE));
		resultText5.addCodeLine(text, "", 0, null);

		lang.nextStep();
	}

	protected void doTransition() {

		encTable.hide();
		resultText1.hide();
		inputStringLabel.hide();
		inputStringText.hide();
		encoding.hide();
		resultText2.hide();
		resultText3.hide();
		resultText4.hide();
		resultText5.hide();
	}
}
