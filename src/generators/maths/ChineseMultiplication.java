package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class ChineseMultiplication implements Generator {
	public int WINDOW_WIDTH = 800;
	public int WINDOW_HEIGHT = 600;
	public String ANIMATION_AUTHOR = "Artur Seitz, Darjush Siahdohoni";
	public String ALGORITHM_NAME;
	public String DESCRIPTION;
	public String SOURCE_CODE;

	private Language language;
	private Translator translator;
	private Locale lang;

	public static final int wrapperX = 100;
	public static final int wrapperY = 200;
	public static final int distanceBetweenLines = 10;
	public static final int distanceBetweenPower = 180;
	public static final int gap = 30;
	private int height = 1;
	private int width = 1;

	public ChineseMultiplication(Locale l) {
		lang = l;
		translator = new Translator(
				"generators/maths/ChineseMultiplicationLang/chinese_multiplication",
				lang);
		ALGORITHM_NAME = translator.translateMessage("algorithm_name");
		DESCRIPTION = translator.translateMessage("description");
		SOURCE_CODE = translator.translateMessage("source_code");
	}

	public void init() {
		language = new AnimalScript(ALGORITHM_NAME, ANIMATION_AUTHOR,
				WINDOW_WIDTH, WINDOW_HEIGHT);
		language.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		// Title
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 24));
		language.newText(new Coordinates(200, 30), ALGORITHM_NAME, "header",
				null, headerProps);

		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		language.newRect(
				new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "header", AnimalScript.DIRECTION_SE),
				"headerRect", null, rectProps);
		// Properties
		PolylineProperties multiplierProp = (PolylineProperties) props
				.getPropertiesByName("multiplierProp");
		PolylineProperties multiplicandProp = (PolylineProperties) props
				.getPropertiesByName("multiplicandProp");
		PolylineProperties striplineProp = (PolylineProperties) props
				.getPropertiesByName("striplineProp");
		CircleProperties intersectionProp = (CircleProperties) props
				.getPropertiesByName("intersectionProp");
		TextProperties textProps = (TextProperties) props
				.getPropertiesByName("textProps");
		SourceCodeProperties scProp = (SourceCodeProperties) props
				.getPropertiesByName("scProp");
		// Primitives
		Integer multiplier = (Integer) primitives.get("multiplier");
		Integer multiplicand = (Integer) primitives.get("multiplicand");
		// Timing
		Timing defaultTiming = new TicksTiming(10);

		// Start Animation
		language.nextStep();

		LinkedList<Text> textList = new LinkedList<Text>();
		textList.add(language.newText(new Offset(-150, 20, "headerRect",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line1"), "line1", null, textProps));
		textList.add(language.newText(new Offset(0, 10, "line1",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line2"), "line2", null, textProps));
		textList.add(language.newText(new Offset(0, 10, "line2",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line3"), "line3", null, textProps));
		textList.add(language.newText(new Offset(0, 10, "line3",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line4"), "line4", null, textProps));
		textList.add(language.newText(new Offset(0, 10, "line4",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line5"), "line5", null, textProps));
		textList.add(language.newText(new Offset(0, 10, "line5",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line6"), "line6", null, textProps));
		textList.add(language.newText(new Offset(0, 10, "line6",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line7"), "line7", null, textProps));
		textList.add(language.newText(new Offset(0, 10, "line7",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line8"), "line8", null, textProps));
		textList.add(language.newText(new Offset(0, 10, "line8",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line9"), "line9", null, textProps));
		textList.add(language.newText(new Offset(0, 10, "line9",
				AnimalScript.DIRECTION_SW), this.translator
				.translateMessage("line10"), "line10", null, textProps));

		language.nextStep(this.translator.translateMessage("introduction"));

		for (Text text : textList)
			text.hide();

		LinkedList<Primitive> primitive = new LinkedList<Primitive>();

		Text text = language.newText(new Coordinates(40, 70), "", "", null,
				textProps);
		primitive.add(text);

		String x = new Integer(multiplier).toString();
		String y = new Integer(multiplicand).toString();
		if (multiplier > multiplicand) {
			x = new Integer(multiplicand).toString();
			y = new Integer(multiplier).toString();
		}

		int[] a = new int[x.length()];
		int[] b = new int[y.length()];

		for (int i = 0; i < x.length(); i++)
			a[i] = Integer.parseInt("" + x.toString().charAt(i));
		for (int i = 0; i < y.length(); i++)
			b[i] = Integer.parseInt("" + y.toString().charAt(i));

		height = a.length * distanceBetweenPower;
		width = b.length * distanceBetweenPower;

		SourceCode sc = language.newSourceCode(new Coordinates(width
				+ (2 * wrapperX) + 30, 170), "SourceCode", null, scProp);
		sc.addCodeLine(this.translator.translateMessage("approach"), "", 0,
				null);
		sc.addCodeLine(this.translator.translateMessage("step1"), "", 1, null);
		sc.addCodeLine(this.translator.translateMessage("step2"), "", 1, null);
		sc.addCodeLine(this.translator.translateMessage("step3"), "", 1, null);
		sc.addCodeLine(this.translator.translateMessage("step4"), "", 1, null);
		sc.addCodeLine(this.translator.translateMessage("step5"), "", 1, null);
		primitive.add(sc);

		primitive.add(language.newRect(new Offset(-5, -5, "SourceCode",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "SourceCode",
				AnimalScript.DIRECTION_SE), "SourceCodeRect", null, rectProps));

		text.setText(this.translator.translateMessage("line16"), null, null);
		sc.highlight(1);

		LinkedList<Text> multiplierDigitsArray = new LinkedList<Text>();
		multiplierDigitsArray.add(language.newText(new Offset(20, 20, text,
				AnimalScript.DIRECTION_SW), "" + a[0], "multiplier#0", null));
		for (int i = 1; i < a.length; i++) {
			multiplierDigitsArray.add(language.newText(new Offset(5, 0,
					"multiplier#" + (i - 1), AnimalScript.DIRECTION_NE), ""
					+ a[i], "multiplier#" + i, null));
		}
		primitive.addAll(multiplierDigitsArray);

		primitive.add(language.newText(new Offset(5, 0, "multiplier#"
				+ (a.length - 1), AnimalScript.DIRECTION_NE), " * ",
				"MultiplicationSign", null));

		LinkedList<Text> multiplicandDigitsArray = new LinkedList<Text>();
		multiplicandDigitsArray.add(language.newText(new Offset(5, 0,
				"MultiplicationSign", AnimalScript.DIRECTION_NE), "" + b[0],
				"multiplicand#0", null));
		for (int i = 1; i < b.length; i++)
			multiplicandDigitsArray.add(language.newText(new Offset(5, 0,
					"multiplicand#" + (i - 1), AnimalScript.DIRECTION_NE), ""
					+ b[i], "multiplicand#" + i, null));
		primitive.addAll(multiplicandDigitsArray);

		language.nextStep(this.translator.translateMessage("step1"));

		// step1: draw the first number
		primitive.addAll(drawMultiplier(a, multiplierProp,
				multiplierDigitsArray));

		text.setText(this.translator.translateMessage("line17"), null, null);
		sc.unhighlight(1);
		sc.highlight(2);

		language.nextStep(this.translator.translateMessage("step2"));

		// step2: draw the second number
		primitive.addAll(drawMultiplicand(b, multiplicandProp,
				multiplicandDigitsArray));

		text.setText(this.translator.translateMessage("line18"), null, null);
		sc.unhighlight(2);
		sc.highlight(3);

		language.nextStep(this.translator.translateMessage("step3"));

		// step3: draw seperators
		primitive
				.addAll(drawStripline((a.length + b.length - 2), striplineProp));

		language.nextStep();

		text.setText(this.translator.translateMessage("line19"), null, null);
		sc.unhighlight(3);
		sc.highlight(4);

		language.nextStep(this.translator.translateMessage("step4"));

		LinkedList<Text> result = drawIntersections(a, b,
				(a.length + b.length - 2), intersectionProp, primitive);

		text.setText(this.translator.translateMessage("line20"), null, null);
		sc.unhighlight(4);
		sc.highlight(5);

		Text equals = language.newText(
				new Offset(5, 2, multiplicandDigitsArray.getLast(),
						AnimalScript.DIRECTION_NE), " = ", "equals", null);
		primitive.add(equals);

		language.nextStep(this.translator.translateMessage("step5"));

		for (int i = 0; i < result.size() - 1; i++) {
			equals.setText(equals.getText() + " " + result.get(i).getText()
					+ " + ", defaultTiming, null);
			result.get(i).hide();
			language.nextStep();
		}

		equals.setText(equals.getText() + " " + result.getLast().getText(),
				null, null);
		result.getLast().hide();

		language.nextStep();

		equals.setText(equals.getText() + " = " + (multiplier * multiplicand),
				null, null);

		language.nextStep(this.translator.translateMessage("result"));

		for (Primitive p : primitive)
			p.hide();

		comparison(scProp);

		return language.toString();
	}

	private LinkedList<Primitive> drawMultiplier(int[] arr,
			PolylineProperties multiplierProp,
			LinkedList<Text> multiplierDigitsArray) {
		LinkedList<Primitive> result = new LinkedList<Primitive>();
		Font newFont = new Font("SansSerif", Font.PLAIN, 16);
		for (int i = 0; i < arr.length; i++) {
			multiplierDigitsArray.get(i).changeColor(
					"",
					(Color) multiplierProp
							.get(AnimationPropertiesKeys.COLOR_PROPERTY), null,
					null);
			multiplierDigitsArray.get(i).setFont(newFont, null, null);
			int x1 = wrapperX - gap;
			int x2 = x1 + (2 * gap) + width;
			for (int j = 0; j < arr[i]; j++) {
				int y = wrapperY + (i * distanceBetweenPower)
						+ (j * distanceBetweenLines);
				Polyline p = language.newPolyline(new Coordinates[] {
						new Coordinates(x1, y), new Coordinates(x1, y) },
						"multiplierLine" + i, null, multiplierProp);
				p.moveBy("translateNodes 2", Math.abs(x2 - x1), 0, null,
						new MsTiming(1000));
				result.add(p);
			}
			language.nextStep();
		}
		return result;
	}

	private LinkedList<Primitive> drawMultiplicand(int[] arr,
			PolylineProperties multiplicandProp,
			LinkedList<Text> multiplicandDigitsArray) {
		LinkedList<Primitive> result = new LinkedList<Primitive>();
		Font newFont = new Font("SansSerif", Font.PLAIN, 16);
		for (int i = 0; i < arr.length; i++) {
			multiplicandDigitsArray.get(i).changeColor(
					"",
					(Color) multiplicandProp
							.get(AnimationPropertiesKeys.COLOR_PROPERTY), null,
					null);
			multiplicandDigitsArray.get(i).setFont(newFont, null, null);
			int y1 = wrapperY - gap;
			int y2 = y1 + (2 * gap) + height;
			for (int j = 0; j < arr[i]; j++) {
				int x = wrapperX + (i * distanceBetweenPower)
						+ (j * distanceBetweenLines);
				Polyline p = language.newPolyline(new Coordinates[] {
						new Coordinates(x, y1), new Coordinates(x, y1) },
						"multiplicandLine" + i, null, multiplicandProp);
				p.moveBy("translate #2", 0, Math.abs(y2 - y1), null,
						new MsTiming(1000));
				result.add(p);
			}
			language.nextStep();
		}
		return result;
	}

	private LinkedList<Primitive> drawStripline(int counter,
			PolylineProperties striplineProp) {
		LinkedList<Primitive> result = new LinkedList<Primitive>();
		int x1 = wrapperX - gap;
		int y1 = wrapperY + (distanceBetweenPower - 10) + gap;
		int x2 = wrapperX + (distanceBetweenPower - 10) + gap;
		int y2 = wrapperY - gap;
		int multCount = 0;
		for (int i = 0; i < counter; i++) {
			int[][] coordinates = point(x1, y1, x2, y2);
			Polyline p = language.newPolyline(new Coordinates[] {
					new Coordinates(coordinates[0][0], coordinates[0][1]),
					new Coordinates(coordinates[0][0], coordinates[0][1]) },
					"stripline" + multCount, null, striplineProp);
			p.moveBy("translate #2",
					Math.abs(coordinates[0][0] - coordinates[1][0]),
					-Math.abs(coordinates[1][1] - coordinates[0][1]), null,
					new MsTiming(1000));
			y1 += distanceBetweenPower;
			x2 += distanceBetweenPower;
			multCount++;
			result.add(p);
		}
		return result;
	}

	private LinkedList<Text> drawIntersections(int[] a, int[] b, int counter,
			CircleProperties intersectionProp, LinkedList<Primitive> primitive) {
		int k = a.length - 1, l = b.length - 1, exponent = 0;
		LinkedList<Text> result = new LinkedList<Text>();
		while (k >= 0 && l >= 0) {
			int p = k, q = l;
			int count = 0;
			while (q >= 0 && p < a.length) {
				for (int i = 0; i < b[q]; i++) {
					for (int j = 0; j < a[p]; j++) {
						int x = wrapperX + (i * distanceBetweenLines)
								+ (q * distanceBetweenPower);
						int y = wrapperY + (j * distanceBetweenLines)
								+ (p * distanceBetweenPower);
						primitive.add(language.newCircle(new Coordinates(x, y),
								4, "", null, intersectionProp));
						count++;
					}
				}
				p++;
				q--;
			}
			if (k > 0)
				k--;
			else
				l--;

			int c1 = counter - b.length;
			if (c1 > 0)
				result.add(language.newText(
						new Coordinates(width + wrapperX + gap + 25, wrapperY
								- 30 + (distanceBetweenPower * c1)), count
								+ " * 10^" + exponent, "", null));
			else
				result.add(language.newText(new Coordinates(wrapperX
						+ (distanceBetweenPower * counter) + 25, wrapperY - gap
						- 30), count + " * 10^" + exponent, "", null));

			counter--;
			exponent++;
			language.nextStep();
		}

		return result;
	}

	private int[][] point(int x1, int y1, int x2, int y2) {
		int[][] arr = new int[][] { { x1, y1 }, { x2, y2 } };

		int a = Math.abs(x1 - x2);
		int b = Math.abs(y1 - y2);
		double c = Math.sqrt(a * a + b * b);
		double q = (a * a) / c;
		double p = c - q;
		double h = Math.sqrt(p * q);
		double alpha = Math.atan(h / p);
		int maxX = wrapperX + width + gap;
		int maxY = wrapperY + height + gap;

		if (y1 > maxY) {
			arr[0][0] = (int) (x1 + (Math.abs(maxY - y1) * Math.tan(alpha)));
			arr[0][1] = maxY;
		}

		if (x2 > maxX) {
			arr[1][0] = maxX;
			arr[1][1] = (int) (y2 + (Math.abs(maxX - x2) / Math.tan(alpha)));
		}

		return arr;
	}

	private void comparison(SourceCodeProperties scProp) {

		SourceCode sc = language.newSourceCode(new Coordinates(300, 90), "",
				null, scProp);
		sc.addCodeLine(this.translator.translateMessage("line11"), "", 0, null);
		sc.addCodeLine(this.translator.translateMessage("line12"), "", 0, null);
		sc.addCodeLine(this.translator.translateMessage("line13"), "", 0, null);
		sc.addCodeLine(this.translator.translateMessage("line14"), "", 0, null);
		sc.addCodeLine(this.translator.translateMessage("line15"), "", 0, null);

		sc.highlight(0);
		sc.highlight(1);
		sc.highlight(2);

		PolylineProperties einerPolyline = new PolylineProperties();
		einerPolyline.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
		PolylineProperties zehnerPolyline = new PolylineProperties();
		zehnerPolyline.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		PolylineProperties splitPolyline = new PolylineProperties();
		splitPolyline.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

		language.newPolyline(new Coordinates[] { new Coordinates(40, 110),
				new Coordinates(140, 110) }, "", null, einerPolyline);

		language.newPolyline(new Coordinates[] { new Coordinates(40, 180),
				new Coordinates(140, 180) }, "", null, zehnerPolyline);
		language.newPolyline(new Coordinates[] { new Coordinates(40, 190),
				new Coordinates(140, 190) }, "", null, zehnerPolyline);

		language.newPolyline(new Coordinates[] { new Coordinates(50, 100),
				new Coordinates(50, 200) }, "", null, einerPolyline);
		language.newPolyline(new Coordinates[] { new Coordinates(60, 100),
				new Coordinates(60, 200) }, "", null, einerPolyline);

		language.newPolyline(new Coordinates[] { new Coordinates(110, 100),
				new Coordinates(110, 200) }, "", null, zehnerPolyline);
		language.newPolyline(new Coordinates[] { new Coordinates(120, 100),
				new Coordinates(120, 200) }, "", null, zehnerPolyline);
		language.newPolyline(new Coordinates[] { new Coordinates(130, 100),
				new Coordinates(130, 200) }, "", null, zehnerPolyline);

		Polyline s1 = language.newPolyline(new Coordinates[] {
				new Coordinates(40, 150), new Coordinates(90, 100) }, "", null,
				splitPolyline);
		Polyline s2 = language.newPolyline(new Coordinates[] {
				new Coordinates(70, 200), new Coordinates(140, 130) }, "",
				null, splitPolyline);

		Circle c1 = language.newCircle(new Coordinates(55, 110), 15, "", null);
		Circle c2 = language.newCircle(new Coordinates(120, 110), 15, "", null);
		Circle c3 = language.newCircle(new Coordinates(55, 185), 15, "", null);
		Circle c4 = language.newCircle(new Coordinates(120, 185), 15, "", null);

		Text t1 = language.newText(new Coordinates(230, 100), "1", "", null);
		Text t2 = language.newText(new Offset(0, 0, t1,
				AnimalScript.DIRECTION_NE), "2", "", null);

		Text t3 = language.newText(new Offset(-7, 0, t2,
				AnimalScript.DIRECTION_SW), "2", "", null);
		Text t4 = language.newText(new Offset(0, 0, t3,
				AnimalScript.DIRECTION_NE), "3", "", null);

		language.newText(new Offset(-10, -5, t1, AnimalScript.DIRECTION_SW),
				"*", "", null);

		language.newText(new Offset(-10, -5, t1, AnimalScript.DIRECTION_SW),
				"*", "", null);

		Polyline p1 = language.newPolyline(new Offset[] {
				new Offset(-5, 0, t3, AnimalScript.DIRECTION_SW),
				new Offset(5, 0, t4, AnimalScript.DIRECTION_SE) }, "", null);

		Text t7 = language.newText(new Offset(5, 0, p1,
				AnimalScript.DIRECTION_SW), "3", "", null);
		Text t8 = language.newText(new Offset(0, 0, t7,
				AnimalScript.DIRECTION_NE), "6", "", null);

		Text t9 = language.newText(new Offset(-5, 0, t7,
				AnimalScript.DIRECTION_SW), "2", "", null);
		Text t10 = language.newText(new Offset(0, 0, t9,
				AnimalScript.DIRECTION_NE), "4", "", null);

		language.newText(new Offset(-12, -10, t9, AnimalScript.DIRECTION_NW), 
				"+", "", null);

		Polyline p2 = language.newPolyline(new Offset[] {
				new Offset(-5, 0, t9, AnimalScript.DIRECTION_SW),
				new Offset(10, 0, t10, AnimalScript.DIRECTION_SE) }, "", null);

		Text t11 = language.newText(new Offset(5, 0, p2,
				AnimalScript.DIRECTION_SW), "2", "", null);
		Text t12 = language.newText(new Offset(0, 0, t11,
				AnimalScript.DIRECTION_NE), "7", "", null);
		Text t13 = language.newText(new Offset(0, 0, t12,
				AnimalScript.DIRECTION_NE), "6", "", null);

		Circle c5 = language.newCircle(new Offset(-1, 0, t7,
				AnimalScript.DIRECTION_C), 7, "", null);
		Circle c6 = language.newCircle(new Offset(-1, 0, t8,
				AnimalScript.DIRECTION_C), 7, "", null);
		Circle c7 = language.newCircle(new Offset(-1, 0, t9,
				AnimalScript.DIRECTION_C), 7, "", null);
		Circle c8 = language.newCircle(new Offset(-1, 0, t10,
				AnimalScript.DIRECTION_C), 7, "", null);

		c1.hide();
		c2.hide();
		c3.hide();
		c4.hide();
		c5.hide();
		c6.hide();
		c7.hide();
		c8.hide();
		s1.hide();
		s2.hide();

		language.nextStep(this.translator.translateMessage("line11"));
		c4.show();
		c6.show();
		t2.changeColor("", Color.RED, null, null);
		t4.changeColor("", Color.RED, null, null);

		sc.unhighlight(0);
		sc.unhighlight(1);
		sc.unhighlight(2);
		sc.highlight(3);

		language.nextStep();
		c4.hide();
		c6.hide();
		c2.show();
		c5.show();
		t2.changeColor("", Color.BLACK, null, null);
		t1.changeColor("", Color.RED, null, null);

		language.nextStep();
		c2.hide();
		c5.hide();
		c3.show();
		c8.show();
		t1.changeColor("", Color.BLACK, null, null);
		t4.changeColor("", Color.BLACK, null, null);
		t2.changeColor("", Color.RED, null, null);
		t3.changeColor("", Color.RED, null, null);

		language.nextStep();
		c3.hide();
		c8.hide();
		c1.show();
		c7.show();
		t2.changeColor("", Color.BLACK, null, null);
		t1.changeColor("", Color.RED, null, null);

		language.nextStep();
		c1.hide();
		c7.hide();
		s1.show();
		s2.show();
		t1.changeColor("", Color.BLACK, null, null);
		t3.changeColor("", Color.BLACK, null, null);

		language.nextStep();
		Text t14 = language.newText(new Coordinates(35, 120), "2", "", null);
		t11.changeColor("", Color.RED, null, null);
		c1.show();

		sc.unhighlight(3);
		sc.highlight(4);

		language.nextStep();
		t14.hide();
		Text t15 = language.newText(new Coordinates(85, 145), "7", "", null);
		t11.changeColor("", Color.BLACK, null, null);
		t12.changeColor("", Color.RED, null, null);
		c1.hide();
		c2.show();
		c3.show();

		language.nextStep();
		t15.hide();
		language.newText(new Coordinates(140, 165), "6", "", null);
		t12.changeColor("", Color.BLACK, null, null);
		t13.changeColor("", Color.RED, null, null);
		c2.hide();
		c3.hide();
		c4.show();
	}

	public String getAlgorithmName() {
		return ALGORITHM_NAME;
	}

	public String getAnimationAuthor() {
		return ANIMATION_AUTHOR;
	}

	public String getCodeExample() {
		return SOURCE_CODE;
	}

	public Locale getContentLocale() {
		return lang;
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getFileExtension() {
		return "asu";
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	public String getName() {
		return ALGORITHM_NAME;
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}
}