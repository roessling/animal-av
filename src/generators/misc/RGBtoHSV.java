/*
 * RGBtoHSV.java
 * Hendrik Pfeiffer, Heiko Reinemuth, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Locale;

import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.DoubleArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

/**
 * @author Hendrik Pfeifer, Heiko Reinemuth
 * @version 1.0
 *
 */
public class RGBtoHSV implements Generator {

	private Language lang;
	private Locale locale;
	private Translator tlr;

	private static final String AUTHORS = "Hendrik Pfeifer, Heiko Reinemuth";
	private static final String SOURCE_CODE = "public static double[] RGBtoHSV(double r, double g, double b) {" // 0
			+ "\n double h, s, v;" // 1
			+ "\n double min, max, delta;" // 2
			+ "\n min = Math.min(Math.min(r, g), b);" // 3
			+ "\n max = Math.max(Math.max(r, g), b);" // 4
			+ "\n v = max / 255;" // 5
			+ "\n delta = max - min;" // 6
			+ "\n if( max != 0 )" // 7
			+ "\n  s = (delta / max) / 100;" // 8
			+ "\n else {" // 9
			+ "\n  s= 0;" // 10
			+ "\n  h = -1;" // 11
			+ "\n  return new double[] {h,s,v};" // 12
			+ "\n }" // 13
			+ "\n if( r == max )" // 14
			+ "\n  h = ( g - b ) / delta;" // 15
			+ "\n else if ( g == max )" // 16
			+ "\n  h = 2 + ( b - r ) / delta;" // 17
			+ "\n else h = 4 + ( r - g ) / delta;" // 18
			+ "\n  h *= 60;" // 19
			+ "\n if( h < 0 )" // 20
			+ "\n  h += 360;" // 21
			+ "\n return new double[] {h,s,v};" // 22
			+ "\n }";

	// fonts
	private static final Font HEADER_FONT = new Font(Font.SANS_SERIF,
			Font.BOLD, 16);
	private static final Font INFO_FONT = new Font(Font.MONOSPACED, Font.BOLD,
			12);
	private static final Font STD_TEXT_FONT = new Font(Font.MONOSPACED,
			Font.PLAIN, 11);

	private Variables varList;

	// primitives
	private Text title;
	private Text rgbText;
	private Text hsvText;
	private Text maxText;
	private Text minText;
	private Text deltaText;
	private Text rgbCurrentText;
	private Text hsvCurrentText;
	private Text explainText;
	private Rect algoRect;
	private Rect explainRect;
	private Rect rgbRect;
	private Rect hsvRect;
	private SourceCode algo;
	private SourceCode infoBefore;
	private SourceCode infoAfter;
	private DoubleArray da1;
	private DoubleArray da2;

	// properties
	private TextProperties titleProps;
	private TextProperties infoTextProps;
	private TextProperties stdTextProps;
	private RectProperties rectProps;
	private SourceCodeProperties scProps;
	private ArrayProperties arrayProps;

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	public RGBtoHSV(Locale locale) {
		this.locale = locale;				
		tlr = new Translator("generators/misc/rgbtohsv", locale);
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	public void init() {
		lang = new AnimalScript(tlr.translateMessage("name"), AUTHORS, 800, 600);
		lang.setStepMode(true);				
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		double r = (double) primitives.get("r");
		double g = (double) primitives.get("g");
		double b = (double) primitives.get("b");
				
		if (r > 255) {
			r = 255;
		} else if (r < 0) {
			r = 0;
		}
		
		if (g > 255) {
			g = 255;
		} else if (g < 0) {
			g = 0;
		}
				
		if (b > 255) {
			b = 255;
		} else if (b < 0) {
			b = 0;
		}

		initProps();
		start(r, g, b);

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
				+ tlr.translateMessage("description6") + " "
				+ tlr.translateMessage("description7");
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
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	private void initProps() {

		titleProps = new TextProperties();
		infoTextProps = new TextProperties();
		stdTextProps = new TextProperties();
		arrayProps = new ArrayProperties();
		rectProps = new RectProperties();
		scProps = new SourceCodeProperties();

		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, HEADER_FONT);
		titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

		infoTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, INFO_FONT);
		infoTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		stdTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, STD_TEXT_FONT);
		stdTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Color.BLUE);
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.RED);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Color.YELLOW);

		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, STD_TEXT_FONT);
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	private void hidePrimitives() {

		title.hide();
		rgbText.hide();
		hsvText.hide();
		maxText.hide();
		minText.hide();
		deltaText.hide();
		rgbCurrentText.hide();
		hsvCurrentText.hide();
		explainText.hide();
		algoRect.hide();
		explainRect.hide();
		rgbRect.hide();
		hsvRect.hide();
		algo.hide();
		infoBefore.hide();
		da1.hide();
		da2.hide();
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	private double[] start(double r, double g, double b) {

		double[] rgb = new double[] { r, g, b };
		double[] hsv = new double[] { 0, 0, 0 };
		float[] hsbvals = Color.RGBtoHSB((int) r, (int) g, (int) b, null);
		double min, max, delta;

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
		infoBefore.addCodeLine(tlr.translateMessage("description7"), null, 0,
				null);

		varList = lang.newVariables();
		varList.declare("double", "r", String.valueOf(rgb[0]), "red");
		varList.declare("double", "g", String.valueOf(rgb[1]), "green");
		varList.declare("double", "b", String.valueOf(rgb[2]), "blue");

		// ###################################################
		lang.nextStep(tlr.translateMessage("step0"));

		infoBefore.hide();
		algo = lang.newSourceCode(new Offset(0, 5, "title",
				AnimalScript.DIRECTION_SW), "algo", null, scProps);
		algo.addCodeLine(
				"public static double[] RGBtoHSV(double r, double g, double b) {",
				null, 0, null);
		algo.addCodeLine("double h, s, v;", null, 1, null);
		algo.addCodeLine("double min, max, delta;", null, 1, null);
		algo.addCodeLine("min = Math.min(Math.min(r, g), b);", null, 1, null);
		algo.addCodeLine("max = Math.max(Math.min(r, g), b);", null, 1, null);
		algo.addCodeLine("v = (max / 255);", null, 1, null);
		algo.addCodeLine("delta = max - min;", null, 1, null);
		algo.addCodeLine("if( max != 0 )", null, 1, null);
		algo.addCodeLine("s = (delta / max) / 100;", null, 2, null);
		algo.addCodeLine("else {", null, 1, null);
		algo.addCodeLine("s = 0;", null, 2, null);
		algo.addCodeLine("h = -1;", null, 2, null);
		algo.addCodeLine("return new double[]{h,s,v};", null, 2, null);
		algo.addCodeLine("}", null, 2, null);
		algo.addCodeLine("if( r == max )", null, 1, null);
		algo.addCodeLine("h = ( g - b ) / delta;", null, 2, null);
		algo.addCodeLine("else if ( g == max )", null, 1, null);
		algo.addCodeLine("h = 2 + ( b - r ) / delta;", null, 2, null);
		algo.addCodeLine("else h = 4 + ( r - g ) / delta;", null, 1, null);
		algo.addCodeLine("h *= 60;", null, 1, null);
		algo.addCodeLine("if( h < 0 )", null, 1, null);
		algo.addCodeLine("h += 360;", null, 2, null);
		algo.addCodeLine("return new double[]{h,s,v};", null, 1, null);
		algo.addCodeLine("}", null, 1, null);
		algoRect = lang.newRect(new Offset(-2, -2, "algo",
				AnimalScript.DIRECTION_NW), new Offset(2, 2, "algo",
				AnimalScript.DIRECTION_SE), "algoRect", null, rectProps);
		explainText = lang
				.newText(
						new Offset(10, 0, "algo", AnimalScript.DIRECTION_NE),
						"                                                                                                   ",
						"explainText", null, infoTextProps);
		explainRect = lang.newRect(new Offset(-2, -2, "explainText",
				AnimalScript.DIRECTION_NW), new Offset(2, 2, "explainText",
				AnimalScript.DIRECTION_SE), "explainRect", null, rectProps);

		minText = lang.newText(new Offset(0, 40, explainText,
				AnimalScript.DIRECTION_SW), "max = 0", "min", null);
		maxText = lang.newText(new Offset(0, 20, minText,
				AnimalScript.DIRECTION_SW), "min = 0", "max", null);
		deltaText = lang.newText(new Offset(0, 20, maxText,
				AnimalScript.DIRECTION_SW), "delta = 0", "delta", null);
		rgbText = lang.newText(new Offset(0, 40, deltaText,
				AnimalScript.DIRECTION_SW), tlr.translateMessage("rgbValues"),
				"rgb", null);
		hsvText = lang.newText(new Offset(0, 20, rgbText,
				AnimalScript.DIRECTION_SW), tlr.translateMessage("hsvValues"),
				"hsv", null);

		// arrays for RGB and HSV color values
		da1 = lang.newDoubleArray(new Offset(20, 0, rgbText,
				AnimalScript.DIRECTION_NE), rgb, "doubleArrayRGB", null,
				arrayProps);
		da2 = lang.newDoubleArray(new Offset(20, 0, hsvText,
				AnimalScript.DIRECTION_NE), hsv, "doubleArrayHSV", null,
				arrayProps);

		// text for the current color box
		rgbCurrentText = lang.newText(new Offset(60, 0, da1,
				AnimalScript.DIRECTION_NE), tlr.translateMessage("rgbColor"),
				"rgbCurrent", null);
		hsvCurrentText = lang.newText(new Offset(0, 20, rgbCurrentText,
				AnimalScript.DIRECTION_SW), tlr.translateMessage("hsvColor"),
				"hsvCurrent", null);

		// boxes to show the current colors
		rgbRect = lang.newRect(new Offset(30, 0, rgbCurrentText,
				AnimalScript.DIRECTION_NE), new Offset(60, 0, rgbCurrentText,
				AnimalScript.DIRECTION_SE), "rgbrect", null, rectProps);
		rgbRect.changeColor("fillColor",
				Color.getHSBColor(hsbvals[0], hsbvals[1], hsbvals[2]), null,
				null);
		hsvRect = lang.newRect(new Offset(0, 35, rgbRect,
				AnimalScript.DIRECTION_NW), new Offset(0, 35, rgbRect,
				AnimalScript.DIRECTION_SE), "hsvrect", null, rectProps);
		hsvRect.changeColor("fillColor", Color.getHSBColor((float) hsv[0],
				(float) hsv[1], (float) hsv[2]), null, null);

		algo.highlight(1);
		explainText.setText(tlr.translateMessage("explain0"), null, null);
		varList.declare("double", "h", "0", "hue");
		varList.declare("double", "s", "0", "saturation");
		varList.declare("double", "v", "0", "value");

		// ###################################################
		lang.nextStep(tlr.translateMessage("step1"));

		algo.unhighlight(1);
		algo.highlight(2);
		explainText.setText(tlr.translateMessage("explain1"), null, null);
		varList.declare("double", "min", "0", "minimum");
		varList.declare("double", "max", "0", "maximum");
		varList.declare("double", "delta", "0", "delta ");

		// ###################################################
		lang.nextStep();

		algo.unhighlight(2);
		algo.highlight(3);
		explainText.setText(tlr.translateMessage("explain2"), null, null);
		min = Math.min(Math.min(r, g), b);
		min = roundTo2Digits(min);
		varList.set("min", String.valueOf(min));
		minText.setText("min = " + min, null, null);
		minText.changeColor(null, Color.red, null, null);

		// ###################################################
		lang.nextStep(tlr.translateMessage("step2"));

		minText.changeColor(null, Color.black, null, null);
		algo.unhighlight(3);
		algo.highlight(4);
		explainText.setText(tlr.translateMessage("explain3"), null, null);
		max = Math.max(Math.max(r, g), b);
		max = roundTo2Digits(max);
		varList.set("max", String.valueOf(max));
		maxText.setText("max = " + max, null, null);
		maxText.changeColor(null, Color.red, null, null);

		// ###################################################
		lang.nextStep(tlr.translateMessage("step3"));

		maxText.changeColor(null, Color.black, null, null);
		algo.unhighlight(4);
		algo.highlight(5);
		explainText.setText(tlr.translateMessage("explain4"), null, null);
		hsv[2] = max / 255;
		hsv[2] = roundTo2Digits(hsv[2]);
		varList.set("v", String.valueOf(hsv[2]));
		da2.put(2, hsv[2], null, null);
		da2.highlightCell(2, null, null);
		hsvRect.changeColor("fillColor", Color.getHSBColor(
				(float) (hsv[0] / 360), (float) hsv[1], (float) hsv[2]), null,
				null);

		// ###################################################
		lang.nextStep(tlr.translateMessage("step4"));

		algo.unhighlight(5);
		algo.highlight(6);
		explainText.setText(tlr.translateMessage("explain5"), null, null);
		delta = max - min;
		delta = roundTo2Digits(delta);
		varList.set("delta", String.valueOf(delta));
		deltaText.setText("delta = " + delta, null, null);
		deltaText.changeColor(null, Color.red, null, null);

		// ###################################################
		lang.nextStep(tlr.translateMessage("step5"));

		deltaText.changeColor(null, Color.black, null, null);
		algo.unhighlight(6);
		algo.highlight(7);
		explainText.setText(tlr.translateMessage("explain6"), null, null);

		// ###################################################
		lang.nextStep();

		algo.unhighlight(7);
		if (max != 0) {
			algo.highlight(8);
			explainText.setText(tlr.translateMessage("explain7"), null, null);
			hsv[1] = (delta / max);
			hsv[1] = roundTo2Digits(hsv[1]);
			varList.set("s", String.valueOf(hsv[1]));
			da2.put(1, hsv[1], null, null);
			da2.highlightCell(1, null, null);
			hsvRect.changeColor("fillColor", Color.getHSBColor(
					(float) (hsv[0] / 360), (float) hsv[1], (float) hsv[2]),
					null, null);

			// ###################################################
			lang.nextStep(tlr.translateMessage("step6"));

		} else {
			algo.highlight(9);
			explainText.setText(tlr.translateMessage("explain8"), null, null);

			// ###################################################
			lang.nextStep();

			algo.unhighlight(9);
			algo.highlight(10);
			explainText.setText(tlr.translateMessage("explain9"), null, null);
			hsv[1] = 0;
			varList.set("s", String.valueOf(hsv[1]));
			da2.put(1, hsv[1], null, null);
			da2.highlightCell(1, null, null);
			hsvRect.changeColor("fillColor", Color.getHSBColor(
					(float) (hsv[0] / 360), (float) hsv[1], (float) hsv[2]),
					null, null);

			// ###################################################
			lang.nextStep(tlr.translateMessage("step7"));

			algo.unhighlight(10);
			algo.highlight(11);
			explainText.setText(tlr.translateMessage("explain10"), null, null);
			hsv[0] = -1;
			varList.set("h", String.valueOf(hsv[0]));
			da2.put(0, hsv[0], null, null);
			da2.highlightCell(0, null, null);
			hsvRect.changeColor("fillColor", Color.getHSBColor(
					(float) (hsv[0] / 360), (float) hsv[1], (float) hsv[2]),
					null, null);

			// ###################################################
			lang.nextStep(tlr.translateMessage("step8"));

			algo.unhighlight(11);
			algo.highlight(12);
			explainText.setText(tlr.translateMessage("explain11"), null, null);

			// ###################################################
			lang.nextStep();

			hidePrimitives();

			// ###################################################
			lang.nextStep();
			return hsv;
		}

		algo.unhighlight(8);
		algo.highlight(14);
		explainText.setText(tlr.translateMessage("explain12"), null, null);

		// ###################################################
		lang.nextStep(tlr.translateMessage("step9"));

		algo.unhighlight(14);
		if (r == max) {
			algo.highlight(15);
			explainText.setText(tlr.translateMessage("explain13"), null, null);
			hsv[0] = (g - b) / delta;
			hsv[0] = roundTo2Digits(hsv[0]);
			varList.set("h", String.valueOf(hsv[0]));
			da2.put(0, hsv[0], null, null);
			da2.highlightCell(0, null, null);
			hsvRect.changeColor("fillColor", Color.getHSBColor(
					(float) (hsv[0] / 360), (float) hsv[1], (float) hsv[2]),
					null, null);

			// ###################################################
			lang.nextStep();

		} else {
			algo.highlight(16);
			explainText.setText(tlr.translateMessage("explain14"), null, null);

			// ###################################################
			lang.nextStep();

			if (g == max) {
				algo.unhighlight(16);
				algo.highlight(17);
				explainText.setText(tlr.translateMessage("explain15"), null,
						null);
				hsv[0] = 2 + (b - r) / delta;
				hsv[0] = roundTo2Digits(hsv[0]);
				varList.set("h", String.valueOf(hsv[0]));
				da2.put(0, hsv[0], null, null);
				da2.highlightCell(0, null, null);
				hsvRect.changeColor("fillColor",
						Color.getHSBColor((float) (hsv[0] / 360),
								(float) hsv[1], (float) hsv[2]), null, null);

				// ###################################################
				lang.nextStep();

			} else {
				algo.unhighlight(16);
				algo.highlight(18);
				explainText.setText(tlr.translateMessage("explain16"), null,
						null);
				hsv[0] = 4 + (r - g) / delta;
				hsv[0] = roundTo2Digits(hsv[0]);
				varList.set("h", String.valueOf(hsv[0]));
				da2.put(0, hsv[0], null, null);
				da2.highlightCell(0, null, null);
				hsvRect.changeColor("fillColor",
						Color.getHSBColor((float) (hsv[0] / 360),
								(float) hsv[1], (float) hsv[2]), null, null);

				// ###################################################
				lang.nextStep();
			}
		}

		algo.unhighlight(15);
		algo.unhighlight(17);
		algo.unhighlight(18);
		algo.highlight(19);
		explainText.setText(tlr.translateMessage("explain17"), null, null);
		hsv[0] *= 60;
		hsv[0] = roundTo2Digits(hsv[0]);
		varList.set("h", String.valueOf(hsv[0]));
		da2.put(0, hsv[0], null, null);
		da2.highlightCell(0, null, null);
		hsvRect.changeColor("fillColor", Color.getHSBColor(
				(float) (hsv[0] / 360), (float) hsv[1], (float) hsv[2]), null,
				null);

		// ###################################################
		lang.nextStep();

		algo.unhighlight(19);
		algo.highlight(20);
		explainText.setText(tlr.translateMessage("explain18"), null, null);

		// ###################################################
		lang.nextStep();

		if (hsv[0] < 0) {
			algo.unhighlight(20);
			algo.highlight(21);
			explainText.setText(tlr.translateMessage("explain19"), null, null);
			hsv[0] += 360;
			hsv[0] = roundTo2Digits(hsv[0]);
			varList.set("h", String.valueOf(hsv[0]));
			da2.put(0, hsv[0], null, null);
			da2.highlightCell(0, null, null);
			hsvRect.changeColor("fillColor", Color.getHSBColor(
					(float) (hsv[0] / 360), (float) hsv[1], (float) hsv[2]),
					null, null);

			// ###################################################
			lang.nextStep();

		}

		algo.unhighlight(20);
		algo.unhighlight(21);
		algo.highlight(22);
		da2.highlightCell(0, 1, null, null);
		explainText.setText(tlr.translateMessage("explain20"), null, null);
		hsvRect.changeColor("fillColor", Color.getHSBColor(
				(float) (hsv[0] / 360), (float) hsv[1], (float) hsv[2]), null,
				null);

		// ###################################################
		lang.nextStep(tlr.translateMessage("step10"));

		hidePrimitives();
		
		infoAfter = lang.newSourceCode(new Offset(0, 5, "title",
				AnimalScript.DIRECTION_SW), "infoAfter", null, scProps);
		infoAfter.addCodeLine(tlr.translateMessage("info_after0"), null, 0,
				null);
		infoAfter.addCodeLine(tlr.translateMessage("info_after1"), null, 0,
				null);
		infoAfter.addCodeLine(tlr.translateMessage("info_after2"), null, 0,
				null);
		infoAfter.addCodeLine(tlr.translateMessage("info_after3"), null, 0,
				null);
		infoAfter.addCodeLine(tlr.translateMessage("info_after4"), null, 0,
				null);
		infoAfter.addCodeLine(" ", null, 0,
				null);
		infoAfter.addCodeLine(tlr.translateMessage("info_after5"), null, 0,
				null);
		infoAfter.addCodeLine(tlr.translateMessage("info_after6"), null, 0,
				null);
		infoAfter.addCodeLine(tlr.translateMessage("info_after7"), null, 0,
				null);
		infoAfter.addCodeLine(tlr.translateMessage("info_after8"), null, 0,
				null);
				
		// ###################################################
		lang.nextStep();
		
		infoAfter.hide();
		
		// ###################################################
		lang.nextStep();
		
		return hsv;
	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++++
	private static double roundTo2Digits(double d) {
		double result = Double.valueOf((new DecimalFormat("0.00").format(d))
				.replace(",", "."));
		return result;
	}
}
