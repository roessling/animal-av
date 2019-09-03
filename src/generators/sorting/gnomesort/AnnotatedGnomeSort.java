package generators.sorting.gnomesort;

import generators.AnnotatedAlgorithm;
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
import algoanim.primitives.updater.ArrayMarkerUpdater;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class AnnotatedGnomeSort extends
		AnnotatedAlgorithm implements Generator {

	private static final String DESCRIPTION = "Hier folgt eine Beschreibung,"
			+ "die über zwei Zeilen geht...!";

	private static final String SOURCE_CODE = "// Java-Code für GnomeSort\n"
			+ "public void GnomeSort(int[] array)\n" + "und so weiter...";

	// public Language animalScript; // siehe "AnnotatedAlgorithm"
	// private SourceCode sc; // siehe "AnnotatedAlgorithm"

	private IntArray ia;
	private ArrayMarker pi, pj;
	private ArrayMarkerUpdater pui, puj;
	private Text df;
	private Text ij;

	private int[] array;
	private ArrayProperties aProps;
	private Timing defaultTiming = new TicksTiming(50);
	private String noCompare = "Compares";
	private String noAssign = "Assignments";

	public AnnotatedGnomeSort() {
	}

	public String generate(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) {

		// Parameter
		array = (int[]) arg1.get("intArray");
		aProps = (ArrayProperties) arg0.getPropertiesByName("arrayProps");

		// Init
		super.init();
		localInit();

		// Parsen
		parse();

		// Sortieren
		sort();

		// AnimalScript
		System.out.println(lang.toString());
		return lang.toString();
	}

	public String getAlgorithmName() {
		return "GnomeSort";
	}

	public String getAnimationAuthor() {
		return "Markus Vogel";
	}

	public String getCodeExample() {
		return SOURCE_CODE;
	}

	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(generators.framework.GeneratorType.GENERATOR_TYPE_SORT);
	}

	public String getName() {
		return "GnomeSort mit Annotations";
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	public void init() {
	  // nothing to be done here
	}
	public void localInit() {

		this.lang = new AnimalScript("Aufgabe 8.1", "Markus Vogel", 640, 480);
		this.lang.setStepMode(true);

		createTitle("Aufgabe 8.1: GnomeSort");
		sourceCode = createSourceCode();
		ia = createArray();
		pi = createPointer("pi", "i");
		pui = createPointerUpdater(pi);
		pj = createPointer("pj", "j");
		puj = createPointerUpdater(pj);
		df = createDirectionFlag();
		ij = createPointerInfo();

		// setup complexity
		vars.declare("int", noCompare);
		vars.setGlobal(noCompare);
		vars.declare("int", noAssign);
		vars.setGlobal(noAssign);

		Text text = lang.newText(new Coordinates(300, 20), "...", "complexity",
				null);
		TextUpdater tu = new TextUpdater(text);
		tu.addToken("Compares: ");
		tu.addToken(vars.getVariable(noCompare));
		tu.addToken(" - Assignments: ");
		tu.addToken(vars.getVariable(noAssign));
		tu.update();
	}

	public void sort() {

		// begin sorting
		exec("sort");
		lang.nextStep();

		// int i = 0;
		exec("var_i");
		pui.setVariable(vars.getVariable("i"));
		ij.setText("i=" + Integer.parseInt(vars.get("i")) + ",  j=?", null,
				null);
		lang.nextStep();

		// int j = 1;
		exec("var_j");
		puj.setVariable(vars.getVariable("j"));
		ij.setText("i=" + Integer.parseInt(vars.get("i")) + ",  j="
				+ Integer.parseInt(vars.get("j")), null, null);
		lang.nextStep();

		// int direction = 1;
		exec("var_direction");
		df.setText("Direction: >>> Right >>>", null, null);
		df.changeColor(null, new Color(208, 0, 0), null, null);
		lang.nextStep();
		df.changeColor(null, new Color(0, 0, 0), null, null);

		// while (j < arr.length) {
		exec("while");
		lang.nextStep();
		while (Integer.parseInt(vars.get("j")) < ia.getLength()) {

			// if(array[i] > array[j]) {
			exec("if1");
			ia.highlightCell(Integer.parseInt(vars.get("i")), Integer
					.parseInt(vars.get("j")), null, null);
			lang.nextStep();
			if (ia.getData(Integer.parseInt(vars.get("i"))) > ia
					.getData(Integer.parseInt(vars.get("j")))) {

				// swap (array, i, j);
				exec("swap");
				ia.highlightElem(Integer.parseInt(vars.get("i")), Integer
						.parseInt(vars.get("j")), null, null);
				ia.swap(Integer.parseInt(vars.get("i")), Integer.parseInt(vars
						.get("j")), defaultTiming, defaultTiming);
				lang.nextStep();

				// direction = -1;
				exec("setDirLeft");
				df.setText("Direction: <<< Left <<<", null, null);
				df.changeColor(null, new Color(208, 0, 0), null, null);
				lang.nextStep();
				df.changeColor(null, new Color(0, 0, 0), null, null);
			}

			// Markierung entfernen
			ia.unhighlightCell(Integer.parseInt(vars.get("i")), Integer
					.parseInt(vars.get("j")), null, null);
			ia.unhighlightElem(Integer.parseInt(vars.get("i")), Integer
					.parseInt(vars.get("j")), null, null);

			// if (direction == 1) {
			exec("if2");
			lang.nextStep();
			if (Integer.parseInt(vars.get("direction")) == 1) {

				// i++;
				exec("iInc1");
				pui.setVariable(vars.getVariable("i"));
				ij.setText("i=" + Integer.parseInt(vars.get("i")) + ",  j="
						+ Integer.parseInt(vars.get("j")), null, null);
				lang.nextStep();

				// j++;
				exec("jInc1");
				puj.setVariable(vars.getVariable("j"));
				ij.setText("i=" + Integer.parseInt(vars.get("i")) + ",  j="
						+ Integer.parseInt(vars.get("j")), null, null);
				lang.nextStep();

			} else {
				exec("else2");
				lang.nextStep();

				// i--;
				exec("iDec");
				pui.setVariable(vars.getVariable("i"));
				ij.setText("i=" + Integer.parseInt(vars.get("i")) + ",  j="
						+ Integer.parseInt(vars.get("j")), null, null);
				lang.nextStep();

				// j--;
				exec("jDec");
				puj.setVariable(vars.getVariable("j"));
				ij.setText("i=" + Integer.parseInt(vars.get("i")) + ",  j="
						+ Integer.parseInt(vars.get("j")), null, null);
				lang.nextStep();
			}

			// if (i < 0) {
			exec("if3");
			lang.nextStep();
			if (Integer.parseInt(vars.get("i")) < 0) {

				// i++;
				exec("iInc2");
				pui.setVariable(vars.getVariable("i"));
				ij.setText("i=" + Integer.parseInt(vars.get("i")) + ",  j="
						+ Integer.parseInt(vars.get("j")), null, null);
				lang.nextStep();

				// j++;
				exec("jInc2");
				puj.setVariable(vars.getVariable("j"));
				ij.setText("i=" + Integer.parseInt(vars.get("i")) + ",  j="
						+ Integer.parseInt(vars.get("j")), null, null);
				lang.nextStep();

				// direction = 1;
				exec("setDirRight");
				df.setText("Direction: >>> Right >>>", null, null);
				df.changeColor(null, new Color(208, 0, 0), null, null);
				lang.nextStep();
				df.changeColor(null, new Color(0, 0, 0), null, null);
			}

			exec("while");
			lang.nextStep();
			System.out.println("j="+Integer.parseInt(vars.get("j")));
		}

		// end sorting
		exec("sortEnd");
		completed();
		System.out.println("--- sorting completed ---");
		lang.nextStep();
	}

	private ArrayMarker createPointer(String name, String label) {
		ArrayMarkerProperties ptrProps = new ArrayMarkerProperties();
		ptrProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, label);
		ptrProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		return lang.newArrayMarker(ia, 0, name, null, ptrProps);
	}

	private ArrayMarkerUpdater createPointerUpdater(ArrayMarker am) {
		return new ArrayMarkerUpdater(am, null, null, array.length - 1);
	}

	private Text createDirectionFlag() {
		TextProperties dfProps = new TextProperties();
		dfProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		dfProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 20));
		return lang.newText(new Coordinates(200, 650),
				"Direction: <<< Unknown >>>", "directionFlag", null, dfProps);
	}

	private Text createPointerInfo() {
		TextProperties piProps = new TextProperties();
		piProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		piProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 20));
		return lang.newText(new Coordinates(200, 700), "i=?,  j=?",
				"PointerInfo", null, piProps);
	}

	private void completed() {
		// sourceCode.unhighlight(2);
		TextProperties props = new TextProperties();
		props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
				Font.BOLD, 20));
		lang.newText(new Coordinates(20, 750),
				"Sortierung mit GnomeSort abgeschlossen. =)", "complete", null,
				props);
	}

	private void createTitle(String title) {
		// Titel erstellen
		TextProperties tProps = new TextProperties();
		tProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		tProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 20));
		lang.newText(new Coordinates(20, 30), title, "titel", null, tProps);

		// Nächster Schritt ----------------------------------------------------
		lang.nextStep();
	}

	private SourceCode createSourceCode() {

		// Source-Code erstellen
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 16));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode sc = lang.newSourceCode(new Coordinates(20, 100),
				"codeGnomeSort", null, scProps);

		// initiate source code primitive
		sourceCode = lang.newSourceCode(new Coordinates(20, 20), "code", null);
		/*
		 * // Add the lines to the SourceCode object. // Line, name,
		 * indentation, display dealy
		 * sc.addCodeLine("* Erstelle zu sortierendes Array", null, 0, null); //
		 * 0 sc.addCodeLine("* Gehe zu erstem Element", null, 0, null); // 1 sc
		 * .addCodeLine(
		 * "* Wiederhole, solange oberes Ende nicht erreicht ist (p2 < n)",
		 * null, 0, null); // 2 sc .addCodeLine(
		 * " |    * Prüfe, ob benachbarte Elemente vertauscht werden müssen",
		 * null, 0, null); // 3
		 * sc.addCodeLine(" |     |    * Vertausche benachbarte Elemente", null,
		 * 0, null); // 4
		 * sc.addCodeLine(" |     |    * Setze die Suchrichtung auf Links",
		 * null, 0, null); // 5
		 * sc.addCodeLine(" |    * Gehe zu nächstem Element", null, 0, null); //
		 * 6 sc.addCodeLine(" |    * Prüfe, unteres Ende erreicht ist (p1 < 0)",
		 * null, 0, null); // 7
		 * sc.addCodeLine(" |     |    * Setze die Suchrichtung auf Rechts",
		 * null, 0, null); // 8
		 * sc.addCodeLine(" |     |    * Gehe zu nachstem Element", null, 0,
		 * null); // 9
		 */
		// Nächster Schritt ----------------------------------------------------
		// lang.nextStep();

		return sc;
	}

	private IntArray createArray() {

		// Markiere Zeile
		// sourceCode.highlight(0);

		// Array erstellen
		// ArrayProperties aProps = new ArrayProperties();
		// aProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		// aProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		// aProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		// aProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
		// Color.BLACK);
		// aProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
		// Color.RED);
		// aProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
		// Color.YELLOW);
		ia = lang.newIntArray(new Coordinates(20, 650), array, "Array", null,
				aProps);
		return ia;
	}

	@Override
	public String getAnnotatedSrc() {
		return "int[] sort(int[] array) {			@label(\"sort\")\n"
				+ "   int i = 0;					@label(\"var_i\") @declare(\"int\", \"i\", \"0\") @inc(\""
				+ noAssign
				+ "\")\n"
				+ "   int j = 1;					@label(\"var_j\") @declare(\"int\", \"j\", \"1\") @inc(\""
				+ noAssign
				+ "\")\n"
				+ "   int direction = 1;			@label(\"var_direction\") @declare(\"int\", \"direction\", \"1\") @inc(\""
				+ noAssign
				+ "\")\n"
				+ "   while (j < array.length) {   	@label(\"while\") @inc(\""
				+ noCompare
				+ "\")\n"
				+ "      if (array[i] > array[j]) {	@label(\"if1\") @inc(\""
				+ noCompare
				+ "\")\n"
				+ "    	    swap(array, i, j);		@label(\"swap\") @inc(\""
				+ noAssign
				+ "\") @inc(\""
				+ noAssign
				+ "\") @inc(\""
				+ noAssign
				+ "\")\n"
				+ "    	    direction = -1;			@label(\"setDirLeft\") @set(\"direction\", \"-1\") @inc(\""
				+ noAssign
				+ "\")\n"
				+ "      }							@label(\"if1End\")\n"
				+ "      if (direction == 1) {		@label(\"if2\") @inc(\""
				+ noCompare
				+ "\")\n"
				+ "    	    i++;					@label(\"iInc1\") @inc(\"i\") @inc(\""
				+ noAssign
				+ "\")\n"
				+ "    	    j++;					@label(\"jInc1\") @inc(\"j\") @inc(\""
				+ noAssign
				+ "\")\n"
				+ "      }							@label(\"if2End\")\n"
				+ "      else {						@label(\"else2\")\n"
				+ "         i--;					@label(\"iDec\") @dec(\"i\") @inc(\""
				+ noAssign
				+ "\")\n"
				+ "         j--;					@label(\"jDec\") @dec(\"j\") @inc(\""
				+ noAssign
				+ "\")\n"
				+ "      }							@label(\"else2End\")\n"
				+ "      if (i < 0) {				@label(\"if3\") @inc(\""
				+ noCompare
				+ "\")\n"
				+ "    	    i++;					@label(\"iInc2\") @inc(\"i\") @inc(\""
				+ noAssign
				+ "\")\n"
				+ "         j++;					@label(\"jInc2\") @inc(\"j\") @inc(\""
				+ noAssign
				+ "\")\n"
				+ "    	    direction = 1;			@label(\"setDirRight\") @set(\"direction\", \"1\") @inc(\""
				+ noAssign
				+ "\")\n"
				+ "      }							@label(\"if3End\")\n"
				+ "   }								@label(\"whileEnd\")\n"
				+ "}								@label(\"sortEnd\")\n";
	}
}