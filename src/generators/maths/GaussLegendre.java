/*
 * GaussLegendre.java
 * Heiko Reinemuth, Hendrik Pfeiffer, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import java.awt.Color;
import java.awt.Font;
import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Locale;
import java.lang.String;
import static java.math.BigDecimal.*;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.Text;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.generators.Language;
import algoanim.properties.TextProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import translator.Translator;

/**
 * @author Heiko Reinemuth, Hendrik Pfeifer
 * @version 1.0
 *
 */
public class GaussLegendre implements Generator {

	private Language lang;
	private Locale locale;
	private Translator tlr;

	private static final String AUTHORS = "Heiko Reinemuth, Hendrik Pfeifer";
	private static final String SOURCE_CODE = "import java.math.BigDecimal;\n"
			+ "import static java.math.BigDecimal.*;\n\n"
			+ "public class GaussLegendre\n"
			+ "{\n"
			+ "   private static final BigDecimal TWO = new BigDecimal(2);\n"
			+ "   private static final BigDecimal FOUR = new BigDecimal(4);\n\n"
			+ "   public static void main(String[] args)\n"
			+ "   {\n"
			+ "      System.out.println(approxPi(85).toString());\n"
			+ "   }\n\n"
			+ "   public static BigDecimal approxPi(final int decPlcs)\n"
			+ "   {\n"
			+ "            BigDecimal a = ONE;\n"
			+ "            BigDecimal b = ONE.divide(sqrt(TWO, decPlcs), decPlcs, ROUND_HALF_UP);\n"
			+ "            BigDecimal t = ONE.divide(FOUR, decPlcs, ROUND_HALF_UP);\n"
			+ "            BigDecimal p = ONE;\n"
			+ "            BigDecimal tmp = ZERO;\n"
			+ "            BigDecimal apxPi = ZERO;\n\n"
			+ "            while (!a.equals(b))\n"
			+ "            {\n"
			+ "                     tmp = a.add(b).divide(TWO, decPlcs, ROUND_HALF_UP);\n"
			+ "                     b = sqrt(b.multiply(a), decPlcs);\n"
			+ "                     t = t.subtract(p.multiply(a.subtract(tmp).multiply(a.subtract(tmp))));\n"
			+ "                     p = p.multiply(TWO);\n"
			+ "                     a = tmp;\n"
			+ "                     apxPi = a.add(b).multiply(a.add(b)).divide(t.multiply(FOUR), decPlcs, ROUND_HALF_UP);\n"
			+ "            }\n"
			+ "            return apxPi;\n"
			+ "   }\n\n"
			+ "   public static BigDecimal sqrt(BigDecimal value, final int decPlcs)\n"
			+ "   {\n"
			+ "            BigDecimal x0 = ZERO;\n"
			+ "            BigDecimal x1 = new BigDecimal(Math.sqrt(value.doubleValue()));\n\n"
			+ "            while (!x0.equals(x1))\n"
			+ "            {\n"
			+ "                     x0 = x1;\n"
			+ "                     x1 = value.divide(x0, decPlcs, ROUND_HALF_UP);\n"
			+ "                     x1 = x1.add(x0);\n"
			+ "                     x1 = x1.divide(TWO, decPlcs, ROUND_HALF_UP);\n"
			+ "            }\n" + "            return x1;\n" + "   }\n" + "}\n";

	// colors
	private static final Color LAST_COLOR = Color.DARK_GRAY;
	private static final Color CURR_COLOR = Color.GREEN;
	private static final Color PRED_COLOR = Color.LIGHT_GRAY;

	// fonts
	private static final Font TITLE_FONT = new Font(Font.SANS_SERIF, Font.BOLD,
			16);
	private static final Font INFO_FONT = new Font(Font.MONOSPACED, Font.BOLD,
			11);
	private static final Font STD_TEXT_FONT = new Font(Font.MONOSPACED,
			Font.PLAIN, 11);

	// values
	private static final String PI85 = new String(
			"3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280");
	private static final BigDecimal TWO = new BigDecimal(2);
	private static final BigDecimal FOUR = new BigDecimal(4);

	// parameter
	private int decimalPlacesParam;

	// auxiliary variables
	private int correctDigitsLast;
	private int correctDigitsCurr;
	private String correctDigitsCurrStr;
	private String[] apxPiArr;

	// algorithm variables
	private BigDecimal a;
	private BigDecimal b;
	private BigDecimal t;
	private BigDecimal p;
	private BigDecimal tmp;
	private BigDecimal apxPi;

	// primitives
	private Text title;
	private Text interValuesInfoText;
	private Text aValText;
	private Text bValText;
	private Text tValText;
	private Text pValText;
	private Text tmpValText;
	private Text loopConditionInfoText;
	private Text explainText;
	private Text aEqbText;
	private Text interCmpInfoText;
	private Text aCmpText;
	private Text bCmpText;
	private Text tCmpText;
	private Text pCmpText;
	private Text tmpCmpText;
	private Text apxPiCmpInfoText;
	private Text apxPiCmpText;
	private Text apxPiIterLastInfo;
	private Text apxPiIterCurrInfo;
	private Text apxPiIterPredInfo;
	private Text apxPiIter1;
	private Text apxPiIter2;
	private Text apxPiIter3;
	private Text apxPiIter4;
	private Text apxPiIter5;
	private Text apxPiIter6;
	private Text apxPiIter7;
	private Rect algoRect;
	private Rect explainRect;
	private SourceCode infoBefore;
	private SourceCode infoAfter;
	private SourceCode algo;
	private StringArray apxPiArray;
	private ArrayMarker apxPiArrayMarkerLast;
	private ArrayMarker apxPiArrayMarkerCurr;
	private ArrayMarker apxPiArrayMarkerNextPred;
	private ArrayMarker apxPiArrayMarkerCurrPred;

	// properties
	private TextProperties titleProps;
	private TextProperties infoTextProps;
	private TextProperties stdTextProps;
	private TextProperties apxPiIterLastInfoProps;
	private TextProperties apxPiIterCurrInfoProps;
	private TextProperties apxPiIterPredInfoProps;
	private RectProperties rectProps;
	private SourceCodeProperties scProps;
	private ArrayProperties apxPiArrayProps;
	private ArrayMarkerProperties apxPiArrayMarkerLastProps;
	private ArrayMarkerProperties apxPiArrayMarkerCurrProps;
	private ArrayMarkerProperties apxPiArrayMarkerNextPredProps;
	private ArrayMarkerProperties apxPiArrayMarkerCurrPredProps;
	
	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	public GaussLegendre(Locale locale) {
		this.locale = locale;
		tlr = new Translator("generators/maths/gausslegendre", locale);
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	public void init() {
		lang = new AnimalScript(tlr.translateMessage("name"), AUTHORS, 800, 600);
		lang.setStepMode(true);
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		decimalPlacesParam = (int) primitives.get("decimalPlacesParam");

		if (decimalPlacesParam > 85) {
			decimalPlacesParam = 85;
		} else if (decimalPlacesParam < 10) {
			decimalPlacesParam = 10;
		}

		initAuxiliaryVariables();
		initProps();
		start();

		return lang.toString();
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	public String getAlgorithmName() {
		return tlr.translateMessage("name");
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	public String getName() {
		return tlr.translateMessage("name");
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	public String getAnimationAuthor() {
		return AUTHORS;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	public String getDescription() {
		String description = tlr.translateMessage("description0") + " "
				+ tlr.translateMessage("description1") + " "
				+ tlr.translateMessage("description2") + " "
				+ tlr.translateMessage("description3") + " "
				+ tlr.translateMessage("description4") + " "
				+ tlr.translateMessage("description5") + " "
				+ tlr.translateMessage("description6");
		return description;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	public String getCodeExample() {
		return SOURCE_CODE;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	public Locale getContentLocale() {
		return locale;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	private void initAuxiliaryVariables() {
		correctDigitsCurrStr = "";
		correctDigitsLast = 0;
		correctDigitsCurr = 0;
		apxPiArr = new String[decimalPlacesParam + 2];
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	private void initProps() {

		titleProps = new TextProperties();
		infoTextProps = new TextProperties();
		stdTextProps = new TextProperties();
		apxPiIterLastInfoProps = new TextProperties();
		apxPiIterCurrInfoProps = new TextProperties();
		apxPiIterPredInfoProps = new TextProperties();
		rectProps = new RectProperties();
		scProps = new SourceCodeProperties();
		apxPiArrayProps = new ArrayProperties();
		apxPiArrayMarkerLastProps = new ArrayMarkerProperties();
		apxPiArrayMarkerCurrProps = new ArrayMarkerProperties();
		apxPiArrayMarkerNextPredProps = new ArrayMarkerProperties();
		apxPiArrayMarkerCurrPredProps = new ArrayMarkerProperties();

		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, TITLE_FONT);
		titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

		infoTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, INFO_FONT);
		infoTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		stdTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, STD_TEXT_FONT);
		stdTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		apxPiIterLastInfoProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
				INFO_FONT);
		apxPiIterLastInfoProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				LAST_COLOR);

		apxPiIterCurrInfoProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
				INFO_FONT);
		apxPiIterCurrInfoProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				CURR_COLOR);

		apxPiIterPredInfoProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
				INFO_FONT);
		apxPiIterPredInfoProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				PRED_COLOR);

		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, STD_TEXT_FONT);
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		apxPiArrayProps
				.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		apxPiArrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		apxPiArrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,
				Boolean.TRUE);
		apxPiArrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Color.BLACK);
		apxPiArrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.RED);
		apxPiArrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Color.GREEN);

		apxPiArrayMarkerLastProps.set(AnimationPropertiesKeys.LABEL_PROPERTY,
				"i-1");
		apxPiArrayMarkerLastProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				LAST_COLOR);

		apxPiArrayMarkerCurrProps.set(AnimationPropertiesKeys.LABEL_PROPERTY,
				"i");
		apxPiArrayMarkerCurrProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				CURR_COLOR);

		apxPiArrayMarkerNextPredProps.set(
				AnimationPropertiesKeys.LABEL_PROPERTY, "i+1");
		apxPiArrayMarkerNextPredProps.set(
				AnimationPropertiesKeys.COLOR_PROPERTY, PRED_COLOR);

		apxPiArrayMarkerCurrPredProps.set(
				AnimationPropertiesKeys.LABEL_PROPERTY, "i");
		apxPiArrayMarkerCurrPredProps.set(
				AnimationPropertiesKeys.COLOR_PROPERTY, PRED_COLOR);
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++	
	private void start() {

		title = lang.newText(new Coordinates(10, 10),
				tlr.translateMessage("title"), "title", null, titleProps);
		infoBefore = lang.newSourceCode(new Offset(0, 5, "title",
				AnimalScript.DIRECTION_SW), "infoBefore", null, scProps);
		infoBefore.addCodeLine(tlr.translateMessage("description0"), null, 0,
				null);
		infoBefore.addCodeLine(tlr.translateMessage("description1"), null, 0,
				null);
		infoBefore.addCodeLine(tlr.translateMessage("description2"), null, 0,
				null);
		infoBefore.addCodeLine(tlr.translateMessage("description3"), null, 0,
				null);
		infoBefore.addCodeLine(tlr.translateMessage("description4"), null, 0,
				null);
		infoBefore.addCodeLine(tlr.translateMessage("description5"), null, 0,
				null);
		infoBefore.addCodeLine(tlr.translateMessage("description6"), null, 0,
				null);
		infoBefore.addCodeLine(" ", null, 0, null);
		infoBefore.addCodeLine(tlr.translateMessage("note_before0"), null, 0,
				null);
		infoBefore.addCodeLine(tlr.translateMessage("note_before1"), null, 0,
				null);
		infoBefore.addCodeLine(tlr.translateMessage("note_before2"), null, 0,
				null);
	
		// ###################################################
		lang.nextStep(tlr.translateMessage("step0"));

		infoBefore.hide();
		algo = lang.newSourceCode(new Offset(0, 5, "title",
				AnimalScript.DIRECTION_SW), "algo", null, scProps);
		algo.addCodeLine("public BigDecimal approxPi()", null, 0, null); // 0
		algo.addCodeLine("{", null, 0, null); // 1
		algo.addCodeLine("BigDecimal a = 1;", null, 1, null); // 2
		algo.addCodeLine("BigDecimal b = 1 / sqrt(2);", null, 1, null); // 3
		algo.addCodeLine("BigDecimal t = 1 / 4;", null, 1, null); // 4
		algo.addCodeLine("BigDecimal p = 1;", null, 1, null); // 5
		algo.addCodeLine("BigDecimal tmp = 0;", null, 1, null); // 6
		algo.addCodeLine("", null, 0, null); // 7
		algo.addCodeLine("BigDecimal apxPi = 0;", null, 1, null); // 8
		algo.addCodeLine("", null, 0, null); // 9
		algo.addCodeLine("while (a != b)", null, 1, null); // 10
		algo.addCodeLine("{", null, 1, null); // 11
		algo.addCodeLine("tmp = (a + b) / 2;", null, 2, null); // 12
		algo.addCodeLine("b = sqrt(a * b);", null, 2, null); // 13
		algo.addCodeLine("t = t - p * (a - tmp) * (a - tmp);", null, 2, null); // 14
		algo.addCodeLine("p = 2 * p;", null, 2, null); // 15
		algo.addCodeLine("a = tmp;", null, 2, null); // 16
		algo.addCodeLine("", null, 0, null); // 17
		algo.addCodeLine("apxPi = ((a + b) * (a + b)) / (4 * t);", null, 2,
				null); // 18
		algo.addCodeLine("}", null, 1, null); // 19
		algo.addCodeLine("return apxPi;", null, 1, null); // 20
		algo.addCodeLine("}", null, 0, null); // 21
		algoRect = lang.newRect(new Offset(-2, -2, "algo",
				AnimalScript.DIRECTION_NW), new Offset(2, 2, "algo",
				AnimalScript.DIRECTION_SE), "algoRect", null, rectProps);
		explainText = lang
				.newText(
						new Offset(10, 0, "algo", AnimalScript.DIRECTION_NE),
						"                                                                                                                                            ",
						"explainText", null, stdTextProps);
		explainRect = lang.newRect(new Offset(-2, -2, "explainText",
				AnimalScript.DIRECTION_NW), new Offset(2, 2, "explainText",
				AnimalScript.DIRECTION_SE), "explainRect", null, rectProps);

		// ###################################################
		lang.nextStep();

		try {
			approxPi();
		} catch (LineNotExistsException e) {
			e.printStackTrace();
		}

		infoAfter = lang.newSourceCode(new Offset(0, 100, "title",
				AnimalScript.DIRECTION_SW), "infoAfter", null, scProps);
		infoAfter.addCodeLine(tlr.translateMessage("note_after0"), null, 0,
				null);
		infoAfter.addCodeLine(tlr.translateMessage("note_after1"), null, 0,
				null);
		infoAfter.addCodeLine(tlr.translateMessage("note_after2"), null, 0,
				null);
		infoAfter.addCodeLine(tlr.translateMessage("note_after3"), null, 0,
				null);

		// ###################################################
		lang.nextStep(tlr.translateMessage("step4"));

		apxPiIterLastInfo.hide();
		apxPiIterCurrInfo.hide();
		apxPiIterPredInfo.hide();
		apxPiArrayMarkerLast.hide();
		apxPiArrayMarkerCurr.hide();
		apxPiArray.hide();
		apxPiIter1.hide();
		apxPiIter2.hide();
		apxPiIter3.hide();
		apxPiIter4.hide();
		apxPiIter5.hide();
		apxPiIter6.hide();
		apxPiIter7.hide();
		title.hide();
		infoAfter.hide();

		// ###################################################
		lang.nextStep();
	}

	// /+++++++++++++++++++++++++++++++++++++++++++++++++++
	private void approxPi() throws LineNotExistsException {

		int i = 0;
		a = ONE;
		b = ONE.divide(sqrt(TWO), decimalPlacesParam, ROUND_HALF_UP);
		t = ONE.divide(FOUR, decimalPlacesParam, ROUND_HALF_UP);
		p = ONE;
		tmp = ZERO;
		apxPi = ZERO;

		apxPiUpdated();

		interValuesInfoText = lang.newText(new Offset(0, 15, "explainText",
				AnimalScript.DIRECTION_SW),
				tlr.translateMessage("inter_values0") + decimalPlacesParam
						+ " " + tlr.translateMessage("inter_values1"),
				"interValuesInfoText", null, infoTextProps);
		aValText = lang.newText(new Offset(0, 5, "interValuesInfoText",
				AnimalScript.DIRECTION_SW), "  a(" + i + ") = "
				+ bigDeciToStr(a), "aValText", null, stdTextProps);
		explainText.setText(tlr.translateMessage("explain0"), null, null);
		algo.highlight(2, 0, false);
		aValText.changeColor(null, Color.BLUE, null, null);

		// ###################################################
		lang.nextStep(tlr.translateMessage("step1"));

		bValText = lang.newText(new Offset(0, 5, "aValText",
				AnimalScript.DIRECTION_SW), "  b(" + i + ") = "
				+ bigDeciToStr(b), "bValText", null, stdTextProps);
		explainText.setText(tlr.translateMessage("explain1"), null, null);
		algo.toggleHighlight(2, 0, false, 3, 0);
		aValText.changeColor(null, Color.BLACK, null, null);
		bValText.changeColor(null, Color.BLUE, null, null);

		// ###################################################
		lang.nextStep();

		tValText = lang.newText(new Offset(0, 5, "bValText",
				AnimalScript.DIRECTION_SW), "  t(" + i + ") = "
				+ bigDeciToStr(t), "tValText", null, stdTextProps);
		explainText.setText(tlr.translateMessage("explain2"), null, null);
		algo.toggleHighlight(3, 0, false, 4, 0);
		bValText.changeColor(null, Color.BLACK, null, null);
		tValText.changeColor(null, Color.BLUE, null, null);

		// ###################################################
		lang.nextStep();

		pValText = lang.newText(new Offset(0, 5, "tValText",
				AnimalScript.DIRECTION_SW), "  p(" + i + ") = " + p.toString()
				+ "  ", "pValText", null, stdTextProps);
		explainText.setText(tlr.translateMessage("explain3"), null, null);
		algo.toggleHighlight(4, 0, false, 5, 0);
		tValText.changeColor(null, Color.BLACK, null, null);
		pValText.changeColor(null, Color.BLUE, null, null);

		// ###################################################
		lang.nextStep();

		tmpValText = lang.newText(new Offset(0, 5, "pValText",
				AnimalScript.DIRECTION_SW), "tmp(" + i + ") = "
				+ bigDeciToStr(tmp), "tmpValText", null, stdTextProps);
		explainText.setText(tlr.translateMessage("explain4"), null, null);
		algo.toggleHighlight(5, 0, false, 6, 0);
		pValText.changeColor(null, Color.BLACK, null, null);
		tmpValText.changeColor(null, Color.BLUE, null, null);

		// ###################################################
		lang.nextStep();

		apxPiArray = lang.newStringArray(new Offset(0, 70, "algo",
				AnimalScript.DIRECTION_SW), apxPiArr, "apxPiArray", null,
				apxPiArrayProps);
		explainText.setText(tlr.translateMessage("explain5"), null, null);
		algo.toggleHighlight(6, 0, false, 8, 0);
		tmpValText.changeColor(null, Color.BLACK, null, null);

		// ###################################################
		lang.nextStep();

		i++;
		loopConditionInfoText = lang.newText(new Offset(0, 15, "tmpValText",
				AnimalScript.DIRECTION_SW), tlr.translateMessage("loop_cond0"),
				"loopConditionText", null, infoTextProps);
		aEqbText = lang
				.newText(new Offset(0, 5, "loopConditionText",
						AnimalScript.DIRECTION_SW), "a(" + (i - 1) + ") != b("
						+ (i - 1) + ") " + tlr.translateMessage("is") + " "
						+ !a.equals(b), "aEqbText", null, stdTextProps);
		interCmpInfoText = lang.newText(new Offset(0, 15, "aEqbText",
				AnimalScript.DIRECTION_SW), tlr
				.translateMessage("inter_values_cmp0"), "computationText",
				null, infoTextProps);
		tmpCmpText = lang.newText(new Offset(0, 5, "computationText",
				AnimalScript.DIRECTION_SW), "tmp(" + i + ") = [a(" + (i - 1)
				+ ") + b(" + (i - 1) + ")] / 2", "tmpCmpText", null,
				stdTextProps);
		bCmpText = lang.newText(new Offset(0, 5, "tmpCmpText",
				AnimalScript.DIRECTION_SW), "  b(" + i + ") = sqrt[a("
				+ (i - 1) + ") * b(" + (i - 1) + ")]", "bCmpText", null,
				stdTextProps);
		tCmpText = lang.newText(new Offset(0, 5, "bCmpText",
				AnimalScript.DIRECTION_SW), "  t(" + i + ") = t(" + (i - 1)
				+ ") - p(" + (i - 1) + ") * [a(" + (i - 1) + ") - tmp(" + i
				+ ")] * [a(" + (i - 1) + ") - tmp(" + i + ")]", "tCmpText",
				null, stdTextProps);
		pCmpText = lang.newText(new Offset(0, 5, "tCmpText",
				AnimalScript.DIRECTION_SW), "  p(" + i + ") = 2 * p(" + (i - 1)
				+ ")", "pCmpText", null, stdTextProps);
		aCmpText = lang.newText(new Offset(0, 5, "pCmpText",
				AnimalScript.DIRECTION_SW), "  a(" + i + ") = tmp(" + i + ")",
				"aCmpText", null, stdTextProps);
		apxPiCmpInfoText = lang.newText(new Offset(400, 15, "aEqbText",
				AnimalScript.DIRECTION_SW), tlr.translateMessage("apx_pi_cmp0")
				+ i + "):", "apxPiCmpInfoText", null, infoTextProps);
		apxPiCmpText = lang.newText(new Offset(0, 5, "apxPiCmpInfoText",
				AnimalScript.DIRECTION_SW), "apxPi(" + i + ") = [[a(" + i
				+ ") + b(" + i + ")] * [a(" + i + ") + b(" + i
				+ ")]] / [4 * t(" + i + ")]", "apxPiCmpText", null,
				stdTextProps);
		apxPiIterLastInfo = lang.newText(new Offset(0, 5, "apxPiArray",
				AnimalScript.DIRECTION_SW), "", "apxPiIterLastInfo", null,
				apxPiIterLastInfoProps);
		apxPiIterCurrInfo = lang.newText(new Offset(0, 5, "apxPiIterLastInfo",
				AnimalScript.DIRECTION_SW), "", "apxPiIterCurrInfo", null,
				apxPiIterCurrInfoProps);
		apxPiIterPredInfo = lang.newText(new Offset(0, 5, "apxPiIterCurrInfo",
				AnimalScript.DIRECTION_SW), "", "apxPiIterPredInfo", null,
				apxPiIterPredInfoProps);
		apxPiIter1 = lang.newText(new Offset(0, 5, "apxPiIterPredInfo",
				AnimalScript.DIRECTION_SW), "", "apxPiIter1", null,
				stdTextProps);
		apxPiIter2 = lang.newText(new Offset(0, 5, "apxPiIter1",
				AnimalScript.DIRECTION_SW), "", "apxPiIter2", null,
				stdTextProps);
		apxPiIter3 = lang.newText(new Offset(0, 5, "apxPiIter2",
				AnimalScript.DIRECTION_SW), "", "apxPiIter3", null,
				stdTextProps);
		apxPiIter4 = lang.newText(new Offset(0, 5, "apxPiIter3",
				AnimalScript.DIRECTION_SW), "", "apxPiIter4", null,
				stdTextProps);
		apxPiIter5 = lang.newText(new Offset(0, 5, "apxPiIter4",
				AnimalScript.DIRECTION_SW), "", "apxPiIter5", null,
				stdTextProps);
		apxPiIter6 = lang.newText(new Offset(0, 5, "apxPiIter5",
				AnimalScript.DIRECTION_SW), "", "apxPiIter6", null,
				stdTextProps);
		apxPiIter7 = lang.newText(new Offset(0, 5, "apxPiIter6",
				AnimalScript.DIRECTION_SW), "", "apxPiIter7", null,
				stdTextProps);
		algo.unhighlight(8);
		boolean limit = false;

		// ###################################################
		// ###################################################
		while (!a.equals(b)) {

			aEqbText.setText(
					"a(" + (i - 1) + ") != b(" + (i - 1) + ") "
							+ tlr.translateMessage("is") + " " + true, null,
					null);
			if (i == 1) {
				explainText.setText(tlr.translateMessage("explain6") + " " + i,
						null, null);
			} else {
				explainText.setText(
						tlr.translateMessage("apx") + (i - 1)
								+ tlr.translateMessage("explain7") + " "
								+ tlr.translateMessage("explain6") + " " + i,
						null, null);
			}
			algo.toggleHighlight(18, 0, false, 10, 0);
			aEqbText.changeColor(null, Color.MAGENTA, null, null);
			aValText.changeColor(null, Color.MAGENTA, null, null);
			bValText.changeColor(null, Color.MAGENTA, null, null);

			// ###################################################
			lang.nextStep();

			explainText.setText(tlr.translateMessage("explain8"), null, null);
			algo.toggleHighlight(10, 0, false, 12, 0);
			aEqbText.changeColor(null, Color.BLACK, null, null);
			aValText.changeColor(null, Color.BLACK, null, null);
			bValText.changeColor(null, Color.BLACK, null, null);
			tmpCmpText.changeColor(null, Color.RED, null, null);

			// ###################################################
			lang.nextStep();

			tmp = a.add(b).divide(TWO, decimalPlacesParam, ROUND_HALF_UP);
			tmpValText.setText("tmp(" + i + ") = " + bigDeciToStr(tmp) + " "
					+ tlr.translateMessage("tmp_value0") + " a(" + (i - 1)
					+ ") & b(" + (i - 1) + "))", null, null);
			explainText.setText(tlr.translateMessage("explain9"), null, null);
			algo.toggleHighlight(12, 0, false, 13, 0);
			tmpCmpText.changeColor(null, Color.BLACK, null, null);
			tmpValText.changeColor(null, Color.BLUE, null, null);
			bCmpText.changeColor(null, Color.RED, null, null);

			// ###################################################
			lang.nextStep();

			b = sqrt(b.multiply(a));
			bValText.setText(
					"  b(" + i + ") = " + bigDeciToStr(b) + " "
							+ tlr.translateMessage("b_value0") + " a("
							+ (i - 1) + ") & b(" + (i - 1) + "))", null, null);
			explainText.setText(tlr.translateMessage("explain10"), null, null);
			algo.toggleHighlight(13, 0, false, 14, 0);
			tmpValText.changeColor(null, Color.BLACK, null, null);
			bCmpText.changeColor(null, Color.BLACK, null, null);
			bValText.changeColor(null, Color.BLUE, null, null);
			tCmpText.changeColor(null, Color.RED, null, null);

			// ###################################################
			lang.nextStep();

			t = t.subtract(p.multiply(a.subtract(tmp).multiply(a.subtract(tmp))));
			tValText.setText("  t(" + i + ") = " + bigDeciToStr(t), null, null);
			explainText.setText(tlr.translateMessage("explain11"), null, null);
			algo.toggleHighlight(14, 0, false, 15, 0);
			bValText.changeColor(null, Color.BLACK, null, null);
			tCmpText.changeColor(null, Color.BLACK, null, null);
			tValText.changeColor(null, Color.BLUE, null, null);
			pCmpText.changeColor(null, Color.RED, null, null);

			// ###################################################
			lang.nextStep();

			p = p.multiply(TWO);
			pValText.setText("  p(" + i + ") = " + p.toString() + " ", null,
					null);
			explainText.setText(tlr.translateMessage("explain12"), null, null);
			algo.toggleHighlight(15, 0, false, 16, 0);
			tValText.changeColor(null, Color.BLACK, null, null);
			pCmpText.changeColor(null, Color.BLACK, null, null);
			pValText.changeColor(null, Color.BLUE, null, null);
			aCmpText.changeColor(null, Color.RED, null, null);

			// ###################################################
			lang.nextStep();

			a = tmp;
			aValText.setText(
					"  a(" + i + ") = " + bigDeciToStr(a) + " "
							+ tlr.translateMessage("a_value0") + " a("
							+ (i - 1) + ") & b(" + (i - 1) + "))", null, null);
			explainText.setText(
					tlr.translateMessage("explain13") + i
							+ tlr.translateMessage("pi") + ".", null, null);
			algo.toggleHighlight(16, 0, false, 18, 0);
			pValText.changeColor(null, Color.BLACK, null, null);
			aCmpText.changeColor(null, Color.BLACK, null, null);
			aValText.changeColor(null, Color.BLUE, null, null);
			apxPiCmpText.changeColor(null, Color.RED, null, null);

			// ###################################################
			lang.nextStep(tlr.translateMessage("step2") + "(" + i + ")");

			apxPi = a
					.add(b)
					.multiply(a.add(b))
					.divide(t.multiply(FOUR), decimalPlacesParam, ROUND_HALF_UP);
			apxPiUpdated();
			updateApxPiArrayPrimitive();
			if (i == 1) {
				apxPiArrayMarkerLast = lang
						.newArrayMarker(apxPiArray, 0, "apxPiArrayMarkerLast",
								null, apxPiArrayMarkerLastProps);
				apxPiArrayMarkerCurr = lang
						.newArrayMarker(apxPiArray, 0, "apxPiArrayMarkerCurr",
								null, apxPiArrayMarkerCurrProps);
				apxPiArrayMarkerNextPred = lang.newArrayMarker(apxPiArray, 0,
						"apxPiArrayMarkerNextPred", null,
						apxPiArrayMarkerNextPredProps);
			}
			if (i == 2) {
				apxPiArrayMarkerCurrPred = lang.newArrayMarker(apxPiArray, 0,
						"apxPiArrayMarkerCurrPred", null,
						apxPiArrayMarkerCurrPredProps);
			}

			// marker positions
			int markerLastPosition = correctDigitsLast - 1;
			if (markerLastPosition < 0) {
				markerLastPosition = 0;
			}
			int markerCurrPosition = correctDigitsCurr - 1;
			int markerCurrPredPosition = markerLastPosition * 2;
			int markerNextPredPosition = markerCurrPosition * 2;

			// last
			if (markerLastPosition != markerCurrPosition) {
				apxPiArrayMarkerLast.move(markerLastPosition, null, null);
			} else {
				apxPiArrayMarkerLast.hide();
			}
			apxPiIterLastInfo.setText(getApxPiIterLastString(i), null, null);

			// current
			apxPiArrayMarkerCurr.move(markerCurrPosition, null, null);
			apxPiIterCurrInfo.setText(getApxPiIterCurrString(i), null, null);

			// current prediction
			if (i > 1) {
				if (markerCurrPredPosition < decimalPlacesParam + 2) {
					apxPiArrayMarkerCurrPred.move(markerCurrPredPosition, null,
							null);
				} else {
					apxPiArrayMarkerCurrPred.hide();
				}
			}

			// next prediction
			if (markerNextPredPosition < decimalPlacesParam + 2) {
				apxPiArrayMarkerNextPred.move(markerNextPredPosition, null,
						null);
				apxPiIterPredInfo.setText(getApxPiIterPredString(i, false),
						null, null);
			} else {
				if (!limit) {
					apxPiIterPredInfo.setText(getApxPiIterPredString(i, true),
							null, null);
				} else {
					apxPiIterPredInfo.setText("", null, null);
				}
				limit = true;
				apxPiArrayMarkerNextPred.hide();
			}

			if (i == 1) {
				apxPiIter1.setText(getApxPiDigitsString(i), null, null);
			} else if (i == 2) {
				apxPiIter2.setText(getApxPiDigitsString(i), null, null);
			} else if (i == 3) {
				apxPiIter3.setText(getApxPiDigitsString(i), null, null);
			} else if (i == 4) {
				apxPiIter4.setText(getApxPiDigitsString(i), null, null);
			} else if (i == 5) {
				apxPiIter5.setText(getApxPiDigitsString(i), null, null);
			} else if (i == 6) {
				apxPiIter6.setText(getApxPiDigitsString(i), null, null);
			} else if (i == 7) {
				apxPiIter7.setText(getApxPiDigitsString(i), null, null);
			}

			apxPiArray.highlightCell(0, correctDigitsCurr - 1, null, null);
			i++;
			tmpCmpText.setText("tmp(" + i + ") = [a(" + (i - 1) + ") + b("
					+ (i - 1) + ")] / 2", null, null);
			bCmpText.setText("  b(" + i + ") = sqrt[a(" + (i - 1) + ") * b("
					+ (i - 1) + ")]", null, null);
			tCmpText.setText("  t(" + i + ") = t(" + (i - 1) + ") - p("
					+ (i - 1) + ") * [a(" + (i - 1) + ") - tmp(" + i
					+ ")] * [a(" + (i - 1) + ") - tmp(" + i + ")]", null, null);
			pCmpText.setText("  p(" + i + ") = 2 * p(" + (i - 1) + ")", null,
					null);
			aCmpText.setText("  a(" + i + ") = tmp(" + i + ")", null, null);
			apxPiCmpInfoText.setText(tlr.translateMessage("apx_pi_cmp0") + i
					+ "):", null, null);
			apxPiCmpText.setText("apxPi(" + i + ") = [[a(" + i + ") + b(" + i
					+ ")] * [a(" + i + ") + b(" + i + ")]] / [4 * t(" + i
					+ ")]", null, null);
			apxPiCmpText.changeColor(null, Color.BLACK, null, null);

		}

		// ###################################################
		// ###################################################

		aValText.setText("  a(" + (i - 1) + ") = " + bigDeciToStr(a) + " "
				+ tlr.translateMessage("a_value1") + " a(" + 0 + ") & b(" + 0
				+ "))", null, null);
		bValText.setText("  b(" + (i - 1) + ") = " + bigDeciToStr(b) + " "
				+ tlr.translateMessage("b_value1") + " a(" + 0 + ") & b(" + 0
				+ "))", null, null);
		aEqbText.setText(
				"a(" + (i - 1) + ") != b(" + (i - 1) + ") "
						+ tlr.translateMessage("is") + " " + false, null, null);
		explainText.setText(
				tlr.translateMessage("apx") + (i - 1)
						+ tlr.translateMessage("pi") + " "
						+ tlr.translateMessage("explain14"), null, null);
		algo.toggleHighlight(18, 0, false, 10, 0);
		aEqbText.changeColor(null, Color.MAGENTA, null, null);
		aValText.changeColor(null, Color.MAGENTA, null, null);
		bValText.changeColor(null, Color.MAGENTA, null, null);

		// ###################################################
		lang.nextStep();

		loopConditionInfoText.hide();
		aEqbText.hide();
		interCmpInfoText.hide();
		tmpCmpText.hide();
		bCmpText.hide();
		tCmpText.hide();
		pCmpText.hide();
		aCmpText.hide();
		apxPiCmpInfoText.hide();
		apxPiCmpText.hide();
		explainText.setText(tlr.translateMessage("explain15"), null, null);
		algo.toggleHighlight(10, 0, false, 20, 0);
		aEqbText.changeColor(null, Color.BLACK, null, null);
		aValText.changeColor(null, Color.BLACK, null, null);
		bValText.changeColor(null, Color.BLACK, null, null);

		// ###################################################
		lang.nextStep(tlr.translateMessage("step3"));

		interValuesInfoText.hide();
		aValText.hide();
		bValText.hide();
		tValText.hide();
		pValText.hide();
		tmpValText.hide();
		explainText.hide();
		explainRect.hide();
		algo.hide();
		algoRect.hide();
		algo.unhighlight(20);
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	private String getApxPiIterLastString(int iter) {
		String apxPiIterLastString = tlr.translateMessage("apx") + (iter - 1)
				+ tlr.translateMessage("pi") + " "
				+ tlr.translateMessage("last0") + " ";
		if (iter == 1) {
			apxPiIterLastString += 0;
		} else {
			apxPiIterLastString += correctDigitsLast - 1;
		}
		apxPiIterLastString += " " + tlr.translateMessage("last1");
		return apxPiIterLastString;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	private String getApxPiIterCurrString(int iter) {
		String apxPiIterCurrString = tlr.translateMessage("apx") + (iter)
				+ tlr.translateMessage("pi") + " "
				+ tlr.translateMessage("curr0") + " ";
		apxPiIterCurrString += correctDigitsCurr - 1;
		apxPiIterCurrString += " " + tlr.translateMessage("curr1");
		return apxPiIterCurrString;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	private String getApxPiIterPredString(int iter, boolean limit) {
		String apxPiIterPredString = "";
		if (iter > 1) {
			apxPiIterPredString += tlr.translateMessage("pred0") + " "
					+ (correctDigitsLast - 1) + " * 2 = "
					+ (correctDigitsLast - 1) * 2 + " "
					+ tlr.translateMessage("pred1") + " ";
		}
		apxPiIterPredString += tlr.translateMessage("apx") + (iter + 1)
				+ tlr.translateMessage("pi") + " "
				+ tlr.translateMessage("pred2") + " " + (correctDigitsCurr - 1)
				+ " * 2 = " + (correctDigitsCurr - 1) * 2 + " "
				+ tlr.translateMessage("pred3");
		if (limit) {
			apxPiIterPredString += " " + tlr.translateMessage("pred4");
		}
		return apxPiIterPredString;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	private String getApxPiDigitsString(int iter) {
		return tlr.translateMessage("corr_digits") + iter + "): "
				+ correctDigitsCurrStr;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	private String bigDeciToStr(BigDecimal value) {

		String bigDeciStr = "";

		if (value.toString().length() == 1) {
			bigDeciStr = value.toString() + String.valueOf('.');
			for (int i = 0; i < decimalPlacesParam; i++) {
				bigDeciStr = bigDeciStr + String.valueOf('0');
			}
		} else {
			for (int i = 0; i < decimalPlacesParam + 2; i++) {
				bigDeciStr = bigDeciStr
						+ String.valueOf(value.toString().charAt(i));
			}
		}

		return bigDeciStr;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	private void apxPiUpdated() {

		boolean correct = true;
		String apxPiStr = bigDeciToStr(apxPi);
		char c;

		correctDigitsLast = correctDigitsCurr;
		for (int i = correctDigitsCurr; i < decimalPlacesParam + 2; i++) {
			c = apxPiStr.charAt(i);
			apxPiArr[i] = String.valueOf(c);
			if (c != PI85.charAt(i)) {
				correct = false;
			}
			if (correct) {
				correctDigitsCurr++;
				correctDigitsCurrStr = correctDigitsCurrStr + c;
			}
		}
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	private void updateApxPiArrayPrimitive() {

		for (int i = 0; i < decimalPlacesParam + 2; i++) {
			apxPiArray.put(i, apxPiArr[i], null, null);
		}
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	private BigDecimal sqrt(BigDecimal value) {

		BigDecimal x0 = ZERO;
		BigDecimal x1 = new BigDecimal(Math.sqrt(value.doubleValue()));

		while (!x0.equals(x1)) {
			x0 = x1;
			x1 = value.divide(x0, decimalPlacesParam, ROUND_HALF_UP);
			x1 = x1.add(x0);
			x1 = x1.divide(TWO, decimalPlacesParam, ROUND_HALF_UP);
		}

		return x1;
	}
}