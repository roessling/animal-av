package generators.compression.shannon_fano.animators;

import generators.compression.shannon_fano.guielements.distributiontable.DistributionTable;
import generators.compression.shannon_fano.style.ShannonFanoStyle;
import generators.compression.shannon_fano.utils.ShannonFanoCodingUtils;

import java.util.HashMap;
import java.util.Map;
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
import algoanim.util.MsTiming;
import algoanim.util.Offset;

public class IntroductionAnimator extends ChapterAnimator {

	private static final String CHAPTER_LABEL = "Introduction";

	private String inputString;

	private Text headline;

	private SourceCode introText1;
	private SourceCode introText2;
	private SourceCode introText3;
	private SourceCode introText4;
	private SourceCode introText5;

	private DistributionTable distrTable;

	private StringArray inputArray;

	private ArrayMarker arrayMarker;

	public DistributionTable getDistrTable() {
		return distrTable;
	}

	public IntroductionAnimator(Language lang, ShannonFanoStyle shannonFanoStyle,
			ResourceBundle messages, Text headline, String inputString) {

		super(lang, shannonFanoStyle, messages, CHAPTER_LABEL);

		this.headline = headline;
		this.inputString = inputString;
	}

	@Override
	public void animate() {

		super.animate();

		showIntroText();

		animateDistrTableGeneration();

		showTransitionText();

		doTransition();
	}

	private void showIntroText() {

		introText1 = lang.newSourceCode(new Offset(0, 30, headline,
				AnimalScript.DIRECTION_SW), "introText1", null,
				(SourceCodeProperties) style
						.getProperties(ShannonFanoStyle.SOURCECODE));
		String text = messages.getString("introText1");
		introText1.addCodeLine(text, "", 0, null);

		lang.nextStep();

		introText2 = lang.newSourceCode(new Offset(0, 5, introText1,
				AnimalScript.DIRECTION_SW), "introText2", null,
				(SourceCodeProperties) style
						.getProperties(ShannonFanoStyle.SOURCECODE));
		text = messages.getString("introText2");
		introText2.addCodeLine(text, "", 0, null);

		lang.nextStep();

		introText3 = lang.newSourceCode(new Offset(0, 5, introText2,
				AnimalScript.DIRECTION_SW), "introText3", null,
				(SourceCodeProperties) style
						.getProperties(ShannonFanoStyle.SOURCECODE));
		text = messages.getString("introText3");
		introText3.addCodeLine(text, "", 0, null);

		lang.nextStep();

		introText4 = lang.newSourceCode(new Offset(0, 5, introText3,
				AnimalScript.DIRECTION_SW), "introText4", null,
				(SourceCodeProperties) style
						.getProperties(ShannonFanoStyle.SOURCECODE));
		text = messages.getString("introText4part1");
		introText4.addCodeLine(text, "", 0, null);
		text = messages.getString("introText4part2");
		introText4.addCodeLine(text, "", 0, null);

		lang.nextStep();
	}

	private void animateDistrTableGeneration() {

		inputArray = lang.newStringArray(new Offset(0, 45, introText4,
				AnimalScript.DIRECTION_SW), ShannonFanoCodingUtils
				.getStringArray(inputString), "inputArray", null,
				(ArrayProperties) style
						.getProperties(ShannonFanoStyle.TRAVERSE_ARRAY));

		arrayMarker = lang.newArrayMarker(inputArray, 0, "iMarker", null,
				(ArrayMarkerProperties) style
						.getProperties(ShannonFanoStyle.MARKER));

		distrTable = new DistributionTable(lang, new Offset(0, 30, inputArray,
				AnimalScript.DIRECTION_SW),
				(ArrayProperties) style
						.getProperties(ShannonFanoStyle.ARRAY_FIRST_COL),
				(ArrayProperties) style
						.getProperties(ShannonFanoStyle.ARRAY_REST));

		char[] input = inputString.toCharArray();
		Map<Character, Integer> usedChars = new HashMap<Character, Integer>();

		for (int i = 0; i < input.length; i++) {
			char inputChar = input[i];

			lang.nextStep();
			arrayMarker.increment(null, null);

			if (usedChars.containsKey(inputChar)) {

				distrTable.increaseFreqByOne(inputChar);
				int currentFreq = usedChars.get(inputChar);
				usedChars.put(inputChar, currentFreq + 1);
			} else {

				usedChars.put(inputChar, 1);
				distrTable.insertElement(inputChar);
			}
		}

		lang.nextStep();
	}

	private void showTransitionText() {

		introText5 = lang.newSourceCode(
				new Offset(0, 15, distrTable.getHeadCol(),
						AnimalScript.DIRECTION_SW), "introText5", null,
				(SourceCodeProperties) style
						.getProperties(ShannonFanoStyle.SOURCECODE));
		String text = messages.getString("introText5");
		introText5.addCodeLine(text, "", 0, null);

		lang.nextStep();
	}

	@Override
	protected void doTransition() {

		introText1.hide();
		introText2.hide();
		introText3.hide();
		introText4.hide();
		introText5.hide();
		inputArray.hide();
		arrayMarker.hide();

		distrTable.moveTo(new Offset(250, 0, headline,
				AnimalScript.DIRECTION_NE), new MsTiming(1000));
	}
}
