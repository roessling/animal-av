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
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;

public class ShakerSortTimB implements generators.framework.Generator {
	IntArray array = null;
	private TicksTiming timing = new TicksTiming(55);
	SourceCode sc;

	private Language lang = null;
	AnimationPropertiesContainer props = new AnimationPropertiesContainer();
	Hashtable<String, Object> ht = new Hashtable<String, Object>();
	Text infobox, direction;
	ArrayMarker marker1 = null, marker2 = null;
	Font font = null;

	private static final String DESCRIPTION = "Der Begriff Shakersort bezeichnet einen stabilen Sortieralgorithmus, der eine Menge von linear angeordneten Elementen (z. B. Zahlen) der Gr&ouml;&szlig;e nach sortiert. Weitere Namen f&uuml;r diesen Algorithmus sind Cocktailsort, Shearsort  oder BiDiBubbleSort (bidirektionales Bubblesort).";
	private int[] inputArray = { 7, 4, 2, 3, 1, 12, 14 };
	ArrayMarkerProperties amp1 = new ArrayMarkerProperties(),
			amp2 = new ArrayMarkerProperties();
	ArrayProperties arrayProps = null;

	public ShakerSortTimB() {
		init();
	}

	protected String getAlgorithmDescription() {
		return DESCRIPTION;
	}

	public String getName() {
		return "Shaker Sort";
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public IntArray sort(int[] a, final int l, final int r) {
	  int myR = r, myL = l;
		font = new Font("SansSerif", Font.PLAIN, 22);

		Text header = lang.newText(new Coordinates(20, 50), "ShakerSort",
				"header", null);
		header.setFont(font, null, null);
		header.show();

		infobox = lang.newText(new Coordinates(20, 200), "", "infobox", null);
		infobox.setFont(font, null, null);
		infobox.show();

		direction = lang.newText(new Coordinates(220, 50), "Direction ->",
				"direction", null);
		direction.setFont(font, null, null);
		direction.show();

		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 12));

		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		sc = lang.newSourceCode(new Coordinates(60, 240), "sourceCode", null,
				scProps);

		sc.addCodeLine("shakersort(int[] a, int l, int r) {", null, 0, null);
		sc.addCodeLine("while (l < r) do {", null, 1, null);
		sc.addCodeLine("shakeUp(a, l, r); r--;", null, 2, null);
		sc.addCodeLine("shakeDown(a, l, r); l++;", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("}", null, 0, null);
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine("void shakeUp(int[] a, int l, int r) {", null, 0, null);
		sc.addCodeLine("for (int j = l; j <= r - 1; j++) {", null, 1, null);
		sc.addCodeLine("if (a[j] > a[j+1]) {", null, 2, null);
		sc.addCodeLine("exchange(a[j], a[j + 1]);", null, 3, null);
		sc.addCodeLine("}", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("}", null, 0, null);
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine("void shakeDown(int[] a, int l, int r) {", null, 0, null);
		sc.addCodeLine("for (int j = r - 1; j >= l; j--) {", null, 1, null);
		sc.addCodeLine("if (a[j] > a[j + 1]) then {", null, 2, null);
		sc.addCodeLine("exchange(a[j], A[j + 1]);", null, 3, null);
		sc.addCodeLine("}", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("}", null, 0, null);

		// now, create the IntArray object, linked to the properties
		array = lang.newIntArray(new Coordinates(20, 150), a,
				"intArray", null, arrayProps);

		amp1.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
		marker1 = lang.newArrayMarker(array, 0, "marker1", null, amp1);

		amp2.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
		marker2 = lang.newArrayMarker(array, 0, "marker2", null, amp2);

		while (myL < myR) {
			sc.highlight(1);
			lang.nextStep("direction right");
			direction.setText("Direction ->", null, null);
//			array.highlightCell(l, null, null);
			sc.highlight(2);
			sc.unhighlight(3);
			shaker1(myL, myR);
			myR--;
			lang.nextStep("direction left");
			direction.setText("Direction <-", null, null);
//			array.highlightCell(r, null, null);
			sc.unhighlight(2);
			sc.highlight(3);
			shaker2(myL, myR);
			myL++;
		}
		lang.nextStep();
		
		for(int z = 0; z < array.getLength(); z++)
			array.highlightCell(z, null, null);
		
		infobox.setText("Finish", null, null);
		return array;
	}

	private void shaker1(int l, int r) {
		lang.nextStep();
		sc.highlight(8);
		sc.unhighlight(17);
		sc.unhighlight(16);

		for (int j = l; j < r; j++) {
			lang.nextStep();
			sc.highlight(9);
			sc.unhighlight(10);
			marker1.move(j, null, null);
			marker2.move(j + 1, null, null);
			infobox.setText("compare cell " + (j + 1) + " with " + (j + 2), null,
					null);

			if (array.getData(j) > array.getData(j + 1)) {
				lang.nextStep();
				sc.highlight(10);
				array.swap(j, j + 1, null, timing);
				infobox.setText("swap cell " + (j + 1) + " with " + (j + 2), null,
						null);
			}
			lang.nextStep();
			sc.unhighlight(10);
		}
		array.highlightCell(r, null, null);
	}

	private void shaker2(int l, int r) {
		lang.nextStep();
		sc.highlight(16);
		sc.unhighlight(9);
		sc.unhighlight(8);
		for (int j = r; j >= l; j--) {
			lang.nextStep();
			sc.highlight(17);
			sc.unhighlight(18);
			marker1.move(j, null, null);
			marker2.move(j + 1, null, null);
			infobox
					.setText("compare cell " + (j + 1) + " with " + (j + 2), null,
							null);
			if (array.getData(j) > array.getData(j + 1)) {
				lang.nextStep();
				sc.highlight(18);
				array.swap(j, j + 1, null, timing);
				infobox.setText("swap cell " + (j + 1) + " with " + (j + 2), null,
						null);
			}
			lang.nextStep();
			sc.unhighlight(18);
		}
		array.highlightCell(l, null, null);
	}

	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		init();
		
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Color.LIGHT_GRAY);
		
		if (primitives.get("intArray") != null) {
			inputArray = (int[]) primitives.get("intArray");
			arrayProps = (ArrayProperties) props.getPropertiesByName("array");
			amp1 = (ArrayMarkerProperties) props
					.getPropertiesByName("arrayMarker");
			amp2 = amp1;
		}

		sort(inputArray, 0, inputArray.length - 1);

		return lang.toString();
	}

	@Override
	public String getAlgorithmName() {
		return "Shaker Sort";
	}

	@Override
	public String getAnimationAuthor() {
		return "Sinem Emeröz, Tim Biedenkapp";
	}

	@Override
	public String getCodeExample() {
		return "shakersort(int[] a, int l, int r) {\n"
				+ " while(l &lt;  r) do {\n" + "    shaker1(a, l, r);r--;\n"
				+ "    shaker2(a, l, r);l++;\n" + "  }\n" + "}\n" + ""
				+ "shaker1(int[] a, int l, int r) {\n"
				+ "  for(j:=l j &lt; r-1; r++) {\n"
				+ "    if (a[j] &gt;  a[j+1]) then {\n"
				+ "      exchange(a[j], a[j+1]);\n" + "    }\n" + "  }\n"
				+ "}\n" + "\n" + "shaker2(int[] a, int l, int r) {\n"
				+ "  for(j = r-1; j >= 1; j--) {\n"
				+ "    if (a[j] &gt;  a[j+1]) then {\n"
				+ "      exchange(a[j], a[j+1]);\n" + "    }\n" + "  }\n"
				+ "}\n";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	@Override
	public String getOutputLanguage() {
		return JAVA_OUTPUT;

	}

	@Override
	public void init() {
		lang = new AnimalScript("ShakerSort Animation",
				"Sinem Emeroez, Tim Biedenkapp", 640, 480);

		lang.setStepMode(true);

		arrayProps = new ArrayProperties();
		props.add(arrayProps);
	}

}