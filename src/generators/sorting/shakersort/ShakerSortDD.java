package generators.sorting.shakersort;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class ShakerSortDD implements generators.framework.Generator {

	private Timing defaultTiming = new TicksTiming(20);
	private Language lang;
	private Boolean swapped;
	private SourceCode sc;
	private SourceCode scInit;
	private ArrayProperties arrayProps;
	private SourceCodeProperties scProps;

	@Override
	public String generate(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) {
		init();
		int[] a = (int[]) arg1.get("a");

		Color textColor = (Color) arg1.get("textColor");
		Color codeHighlightColor = (Color) arg1.get("codeHighlightColor");
		Color arraycellHighlightColor = (Color) arg1
				.get("arraycellHighlightColor");

		TextProperties txprop = new TextProperties();
		txprop.set(AnimationPropertiesKeys.COLOR_PROPERTY, textColor);
		txprop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
				Font.BOLD, 20));

		lang.newText(new Coordinates(20, 30), "Shaker Sort", "title", null,
				txprop);

		RectProperties rectProp = new RectProperties();

		rectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.gray);
		rectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
		lang.newRect(new Offset(-5, -5, "title", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "title", AnimalScript.DIRECTION_SE), "rect1",
				null, rectProp);
		txprop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
				Font.PLAIN, 14));

		lang.newText(new Offset(0, 10, "title", AnimalScript.DIRECTION_SW),
				"by Dennis Mueller & Dennis Siebert", "title2", null, txprop);

		arrayProps = setArrayProperties(textColor, arraycellHighlightColor);

		scProps = setSCProperties(textColor, codeHighlightColor);
		// add array properties

		showInitCode(scProps);
		// creation of int Array
		IntArray array = lang.newIntArray(new Offset(0, 75, "InitCode",
				AnimalScript.DIRECTION_SW), a, "intArray", null, arrayProps);

		// set array bounds
		// show initial code segment#
		lang.nextStep();
		scInit.highlight(0);

		lang.nextStep();

		scInit.toggleHighlight(0, 1);
		int links = 1;
		lang.nextStep();

		scInit.toggleHighlight(1, 2);
		int rechts = array.getLength() - 1;
		lang.nextStep();
		scInit.toggleHighlight(2, 3);
		int fertig = rechts;
		lang.nextStep();
		scInit.unhighlight(3);

		// show sortingcode
		showSourceCode(scProps);
		lang.nextStep();

		// set array marker i
		ArrayMarkerProperties ami = new ArrayMarkerProperties();
		ami.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		ami.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
		ArrayMarker i = lang.newArrayMarker(array, rechts, "i", null, ami);
		i.hide();

		// set array marker j
		ArrayMarkerProperties ami2 = new ArrayMarkerProperties();
		ami2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		ami2.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
		ArrayMarker j = lang.newArrayMarker(array, links, "j", null, ami2);
		j.hide();

		sc.highlight(0);
		lang.nextStep();
		sc.toggleHighlight(0, 1);
		lang.nextStep();
		sc.unhighlight(1);
		// actual sorting code
		do {

			sc.highlight(2);
			swapped = false;
			lang.nextStep();

			sc.toggleHighlight(2, 3);
			sc.unhighlight(9);
			j.move(links, null, defaultTiming);
			j.show();
			lang.nextStep();
			for (; j.getPosition() <= rechts; j.increment(null, defaultTiming)) {

				// aufwärts
				array.highlightCell(j.getPosition(), null, null);
				array.highlightCell(j.getPosition() - 1, null, null);
				sc.toggleHighlight(3, 4);
				lang.nextStep();
				if (array.getData(j.getPosition()) < array.getData(j
						.getPosition() - 1)) {

					sc.toggleHighlight(4, 5);
					swapped = true;
					lang.nextStep();

					sc.toggleHighlight(5, 6);
					fertig = j.getPosition();
					lang.nextStep();

					sc.toggleHighlight(6, 7);
					array.swap(j.getPosition() - 1, j.getPosition(), null,
							defaultTiming);
					lang.nextStep();
				}
				sc.unhighlight(4);
				array.unhighlightCell(j.getPosition(), null, null);
				array.unhighlightCell(j.getPosition() - 1, null, null);
				sc.toggleHighlight(7, 8);

				lang.nextStep();
				sc.unhighlight(8);
				sc.highlight(3);
				lang.nextStep();
			}
			sc.unhighlight(3);
			sc.toggleHighlight(8, 9);
			lang.nextStep();

			sc.toggleHighlight(9, 10);
			rechts = fertig - 1;
			lang.nextStep();

			sc.toggleHighlight(10, 11);
			sc.unhighlight(17);
			i.move(rechts, null, defaultTiming);
			i.show();
			lang.nextStep();
			for (; i.getPosition() >= links; i.decrement(null, defaultTiming)) {

				// abwärts
				array.highlightCell(i.getPosition(), null, null);
				array.highlightCell(i.getPosition() - 1, null, null);
				sc.toggleHighlight(11, 12);
				lang.nextStep();
				if (array.getData(i.getPosition()) < array.getData(i
						.getPosition() - 1)) {

					sc.toggleHighlight(12, 13);
					swapped = true;
					lang.nextStep();

					sc.toggleHighlight(13, 14);
					fertig = i.getPosition();
					lang.nextStep();
					sc.toggleHighlight(14, 15);
					array.swap(i.getPosition() - 1, i.getPosition(), null,
							defaultTiming);
					lang.nextStep();
				}
				sc.unhighlight(12);
				array.unhighlightCell(i.getPosition(), null, null);
				array.unhighlightCell(i.getPosition() - 1, null, null);

				sc.toggleHighlight(15, 16);
				lang.nextStep();
				sc.unhighlight(16);
				sc.highlight(11);
				lang.nextStep();
			}
			sc.unhighlight(11);
			sc.toggleHighlight(16, 17);
			lang.nextStep();

			sc.toggleHighlight(17, 18);
			links = fertig + 1;
			lang.nextStep();

			sc.toggleHighlight(18, 19);
			lang.nextStep();
			sc.unhighlight(19);
		} while (swapped);
		sc.highlight(20);
		lang.nextStep();

		return lang.toString();
	}

	private ArrayProperties setArrayProperties(Color textColor,
			Color arraycellHighlightColor) {
		arrayProps = new ArrayProperties();

		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		arrayProps
				.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, textColor);
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.RED);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				arraycellHighlightColor);
		return arrayProps;
	}

	@Override
	public String getAlgorithmName() {
		return "Shaker Sort";
	}

	@Override
	public String getAnimationAuthor() {
		return "Dennis Müller, Dennis Siebert";
	}

	// Code Example
	private final String code = "public void ShakerSort(int[] array){ "
			+ "\n links = 1" + "\n rechts = array.lenght() - 1"
			+ "\n fertig = rechts" + "\n do{" + "\n swapped = false"
			+ "\n for(j = links ; j <= rechts ; j++){"
			+ "\n if(array[j] < aaray[j-1]{" + "\n swapped = true"
			+ "\n fertig = j" + "\n array.swap(array[j-1],array[j]"
			+ "\n } //end if" + "\n } //end for" + "\n rechts = fertig - 1"
			+ "\n " + "\n for(i = rechts ; i >= links ; i++){"
			+ "\n if(array[i] < aaray[i-1]{" + "\n swapped = true"
			+ "\n fertig = i" + "\n array.swap(array[i-1],array[i]"
			+ "\n } //end if" + "\n } //end for" + "\n links = fertig + 1"
			+ "\n } while (swapped)" + "\n } //end programm";

	@Override
	public String getCodeExample() {
		return code;
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		return "ShakerSort with Animation and Highlighting";
	}

	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	// returns new sorting generator
	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	@Override
	public String getName() {
		return "Shaker Sort";
	}

	@Override
	public String getOutputLanguage() {
		return generators.framework.Generator.JAVA_OUTPUT;
	}

	@Override
	public void init() {
		lang = new AnimalScript("ShakerSort", "Dennis Mueller & Dennis Siebert", 640, 480);
		lang.setStepMode(true);
	}

	private SourceCodeProperties setSCProperties(Color text, Color highlight) {
		// adding sourcecode properties
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 12));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, highlight);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, text);
		return scProps;
	}

	private void showInitCode(SourceCodeProperties scProp) {

		scInit = lang.newSourceCode(new Offset(0, 50, "title2",
				AnimalScript.DIRECTION_SW), "InitCode", null, scProp);

		// add sourcecode lines
		scInit.addCodeLine("public void ShakerSort(int[] array){", null, 0,
				null);
		scInit.addCodeLine("links = 1", null, 1, null);
		scInit.addCodeLine("rechts = array.lenght() - 1", null, 1, null);
		scInit.addCodeLine("fertig = rechts ", null, 1, null);
	}

	private void showSourceCode(SourceCodeProperties scProp) {

		// init sourcecode
		sc = lang.newSourceCode(new Offset(0, 25, "intArray",
				AnimalScript.DIRECTION_SW), "sourceCode", null, scProp);

		// building sourcecode
		// add sourcecode lines
		sc.addCodeLine("//start sorting", null, 1, null);
		// w hile loop
		sc.addCodeLine("do{", null, 1, null);
		sc.addCodeLine("swapped = false", null, 2, null);

		// j for loop
		sc.addCodeLine("for(j = links ; j <= rechts ; j++){", null, 3, null);
		sc.addCodeLine("if(array[j] < aaray[j-1]{", null, 4, null);
		sc.addCodeLine("swapped = true", null, 5, null);
		sc.addCodeLine("fertig = j", null, 5, null);
		sc.addCodeLine("array.swap(array[j-1],array[j]", null, 5, null);
		sc.addCodeLine("} //end if", null, 4, null);
		sc.addCodeLine("} //end for", null, 3, null);

		sc.addCodeLine("rechts = fertig - 1", null, 2, null);

		// i for loop
		sc.addCodeLine("for(i = rechts ; i >= links ; i++){", null, 3, null);
		sc.addCodeLine("if(array[i] < aaray[i-1]{", null, 4, null);
		sc.addCodeLine("swapped = true", null, 5, null);
		sc.addCodeLine("fertig = i", null, 5, null);
		sc.addCodeLine("array.swap(array[i-1],array[i]", null, 5, null);
		sc.addCodeLine("} //end if", null, 4, null);
		sc.addCodeLine("} //end for", null, 3, null);

		sc.addCodeLine("links = fertig + 1", null, 2, null);
		sc.addCodeLine("} while (swapped)", null, 1, null);
		sc.addCodeLine("} //end programm", null, 0, null);

	}
}
