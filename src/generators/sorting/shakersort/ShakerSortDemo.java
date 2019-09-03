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
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class ShakerSortDemo implements Generator {

	protected Language lang;
	private ArrayProperties arrayProps;
	private ArrayMarkerProperties ami, amiPlusEins, amj, amjMinusEin;
	private SourceCode sc;
	private IntArray array;
	private SourceCodeProperties scProps;
	int[] original = new int[] { 11, 5, 2, 0, 4, 98, 1, 3, 99, 34 };
	private ArrayMarker i, j, iPlusEins, jMinusEins;
	private Timing defaultTiming = new TicksTiming(30);
	int p = 1;

	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		init();
		if (primitives.get("UserArray") != null) {

			arrayProps = (ArrayProperties) props
					.getPropertiesByName("arrayPro");
			ami = (ArrayMarkerProperties) props.getPropertiesByName("iMarker");
			amj = (ArrayMarkerProperties) props.getPropertiesByName("jMarker");
			original = (int[]) primitives.get("UserArray");
			scProps = (SourceCodeProperties) props
					.getPropertiesByName("sourceCodeProp");

		} else {
			arrayProps = new ArrayProperties();
			arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
					Color.RED);
			arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.CYAN);

			ami = new ArrayMarkerProperties();
			ami.setName("iMarker");
			ami.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
			ami.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");

			amj = new ArrayMarkerProperties();
			amj.setName("jMarker");
			amj.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
			amj.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");

			
			scProps = new SourceCodeProperties();
			scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
			scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
					"Monospaced", Font.PLAIN, 12));
			scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
			scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		}

		// Ueberschrift

		algoanim.primitives.Text header = lang.newText(new Coordinates(20, 10),
				"ShakerSort", "ShakerSort", null);
		header.setFont(new Font("SansSerif", 1, 22), null, null);
		// ----------------------------------
		

		
		sc = lang.newSourceCode(new Coordinates(40, 140), "sourceCode", null,
				scProps);
		sc.addCodeLine("public void shakersort(int[] data) {", null, 0, null);
		sc.addCodeLine("int i, j, p;", null, 1, null);
		sc.addCodeLine("for (p = 1; p <= data.length ; p++) {", null, 1, null);
		sc.addCodeLine("for (i = p - 1; i < data.length - p; i++)", null, 2,
				null);
		sc.addCodeLine("if (a[i] > a[i+1])", null, 3, null);
		sc.addCodeLine("swap(a, i, i + 1);", null, 4, null);
		sc
				.addCodeLine("for (j = a.length - p - 1; j >= p; j--)", null,
						2, null);
		sc.addCodeLine("if (a[j] < a[j-1])", null, 3, null);
		sc.addCodeLine("swap(a, i, i - 1);", null, 4, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("}", null, 0, null);
		
	

		array = lang.newIntArray(new Coordinates(20, 100), original, "array",
				null, arrayProps);

		i = lang.newArrayMarker(array, p - 1, "i", new algoanim.util.Hidden(),
				ami);

		j = lang.newArrayMarker(array, array.getLength() - p - 1, "j",
				new algoanim.util.Hidden(), amj);

		amiPlusEins = new ArrayMarkerProperties();
		amiPlusEins.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
		amiPlusEins.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i+1");

		amjMinusEin = new ArrayMarkerProperties();
		amjMinusEin.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.darkGray);
		amjMinusEin.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j-1");

		iPlusEins = lang.newArrayMarker(array, i.getPosition() + 1, "i+1",
				new algoanim.util.Hidden(), amiPlusEins);

		jMinusEins = lang.newArrayMarker(array, j.getPosition() - 1, "j-1",
				new algoanim.util.Hidden(), amjMinusEin);

		shakersort(original);

		return lang.toString();
	}

	public ShakerSortDemo(Language l) {

		lang = l;
		lang.setStepMode(true);
	}

	public ShakerSortDemo() {
	}

	public void init() {

		lang = new AnimalScript("ShakerSort",
				"Muhammed Arour und Katja Tenenbaum", 640, 480);
		lang.setStepMode(true);
	}

	public void showSourceCode() {



	}

	public void shakersort(int[] inputArray) {

		
		showSourceCode();
		sc.highlight(0);
		lang.nextStep();
		sc.toggleHighlight(0, 1);

		lang.nextStep();
		sc.toggleHighlight(1, 2);
		lang.nextStep();

		// i = lang.newArrayMarker(array, p - 1, "i", new
		// algoanim.util.Hidden(),
		// ami);
		// iPlusEins = lang.newArrayMarker(array, i.getPosition() + 1, "i+1",
		// new algoanim.util.Hidden(), amiPlusEins);
		// j = lang.newArrayMarker(array, inputArray.length - p - 1, "j",
		// new algoanim.util.Hidden(), amj);
		// jMinusEins = lang.newArrayMarker(array, j.getPosition() - 1, "j-1",
		// new algoanim.util.Hidden(), amjMinusEin);

		for (p = 1; p <= array.getLength() / 2; p++) {

			sc.highlight(2);
			j.hide();
			lang.nextStep();

			sc.unhighlight(2);
			for (i.move(p - 1, null, null); i.getPosition() < array.getLength()
					- p; i.increment(null, defaultTiming)) {
				sc.highlight(3);
				i.show();
				lang.nextStep();
				sc.toggleHighlight(3, 4);
				lang.nextStep();
				if (array.getData(i.getPosition()) > array.getData(i
						.getPosition() + 1)) {

					sc.toggleHighlight(4, 5);

					iPlusEins.move(i.getPosition() + 1, null, null);
					iPlusEins.show();
					lang.nextStep();
					array.swap(i.getPosition(), iPlusEins.getPosition(), null,
							defaultTiming);
					lang.nextStep();
					iPlusEins.hide();

				}
				sc.unhighlight(4);
				sc.unhighlight(5);
				lang.nextStep();
				i.hide();

			}
			array.highlightCell(i.getPosition(), null, null);

			for (j.move(array.getLength() - p - 1, null, null); j.getPosition() >= p; j
					.decrement(null, defaultTiming)) {
				sc.highlight(6);
				j.show();
				lang.nextStep();
				sc.toggleHighlight(6, 7);
				lang.nextStep();
				if (array.getData(j.getPosition()) < array.getData(j
						.getPosition() - 1)) {
					sc.toggleHighlight(7, 8);
					jMinusEins.move(j.getPosition() - 1, null, null);
					jMinusEins.show();
					lang.nextStep();
					array.swap(j.getPosition(), jMinusEins.getPosition(), null,
							defaultTiming);
					lang.nextStep();
					jMinusEins.hide();// swap
				}
				sc.unhighlight(7);
				sc.unhighlight(8);
				lang.nextStep();
				j.hide();

			}
			array.highlightCell(j.getPosition(), null, null);

		}

	}
	@Override
	public String getAlgorithmName() {
		return "Shaker Sort";
	}

	@Override
	public String getAnimationAuthor() {
		return "Muhammed Arour, Katja Tenenbaum";
	}

	@Override
	public String getCodeExample() {
		StringBuffer sb = new StringBuffer();
		sb.append("public void shakersort(int[] data) { \n");
		sb.append(" int i, j, p; \n");
		sb.append(" for (p = 1; p &le; data.length ; p++) { \n");
		sb.append("   for (i = p - 1; i &lt; data.length - p; i++) \n");
		sb.append("     if (a[i] &gt; a[i+1]) \n");
		sb.append("       swap(a, i, i + 1); \n");
		sb.append("   for (j = a.length - p - 1; j &ge; p; j--) \n");
		sb.append("     if (a[j] &lt; a[j-1]) \n");
		sb.append("       swap(a, i, i - 1); \n").append("  } \n").append("}");
		return sb.toString();
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
  public String getDescription() {
    StringBuffer desc = new StringBuffer();
    desc.append("Bei ShakerSort handelt es sich um einen stabilen Sortier-Algorithmus.").append("\n");
    desc.append("Das zu sortierende Array wird von vorne nach hinten und umgekehrt durchgegangen \n");
    desc.append("Dabei werden 2 benachbarte Elemente verglichen und gegebenfalls vertauscht, so wird das \n");
    desc.append("gr&ouml;&szlig;te Element des Arrays nach hinten geschoben und das kleinste Element nach vorne.");
    return desc.toString();
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
	public String getName() {
		return "Shaker Sort";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}
}