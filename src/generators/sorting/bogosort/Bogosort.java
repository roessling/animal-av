package generators.sorting.bogosort;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.items.StringPropertyItem;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;

public class Bogosort implements Generator {

	private final String name = "Bogosort";
	/** Standard margin between elements */
	private final int margin = 20;
	/** Offset between upper left {@code Node} of {@link main} to upper left {@code Node} of {@link isSorted}. */
	private int offSetMainNext = 90;

	private Language lang;

	private int[] listToSort;
	private int maxIterations;

	private Text title;
	private Node titleNode;
	private TextProperties titleProperties;

	private TextBlock introductionText, discussionText;
	private TextProperties textProperties;

	private IntArray list;
	private TwoValueCounter counter;
	private CounterProperties counterProperties;
	@SuppressWarnings("unused")
	private TwoValueView counterView;

	private Node topLeftContentNode;
	private ArrayProperties listProperties;

	private ArrayMarker current;
	private ArrayMarkerProperties currentMarkerProperties;
	private ArrayMarker next;
	private ArrayMarker random;
	private ArrayMarkerProperties nextMarkerProperites;

	private BarArray barArray;
	private BarArrayMarker barMarker1, barMarker2;
	private RectProperties barArrayRectProperties, shufflePointerProperties, randomPointerProperties;

	private Text iteration;
	private Node iterationNode;
	private TextProperties iterationProperties;

	private Color successColor, failureColor;

	private int iterationsWithSteppedSubFunctionsLeft;

	private SourceCode main;
	private SourceCode isSorted;
	private SourceCode shuffle;
	private SourceCodeProperties scProps;

	public void init() {
		lang = new AnimalScript(getName(), getAnimationAuthor(), 800, 600);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		readPrimitives(primitives);
		readProperties(props);

		placeTitle();
		showIntroduction();
		lang.nextStep("Einführung");
		hideIntroduction();
		placeObjects();
		createArrayMarkers();

		lang.nextStep("Der Algorithmus");

		hideFunctionSourceCode(isSorted, false);
		hideFunctionSourceCode(shuffle, false);
		normalizeShuffleSourceCodePosition();

		/* Execute algorithm */
		sort();

		showDiscussion();
		lang.nextStep("Diskussion");

		return lang.toString();
	}

	private void readPrimitives(Hashtable<String, Object> primitives) {
		listToSort = (int[]) primitives.get("Zahlenliste");
		maxIterations = (Integer) primitives.get("Maximale Iterationen");
		iterationsWithSteppedSubFunctionsLeft = (Integer) primitives
				.get("Anzahl Iterationen mit Subfunktion-Visualisierung");
		successColor = (Color) primitives.get("Farbe bei Erfolg");
		failureColor = (Color) primitives.get("Farbe bei Misserfolg");
	}

	private void readProperties(AnimationPropertiesContainer props) {
		titleProperties = (TextProperties) props.getPropertiesByName("Titel");
		/* Read user defined font and use it to create bold and large fonts */
		Font userFont = (Font) titleProperties.get(AnimationPropertiesKeys.FONT_PROPERTY);
		titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(userFont.getFamily(), Font.BOLD, 32));
		textProperties = (TextProperties) props.getPropertiesByName("Text");
		listProperties = (ArrayProperties) props.getPropertiesByName("Zahlenliste");
		currentMarkerProperties = (ArrayMarkerProperties) props.getPropertiesByName("Marker #1");
		nextMarkerProperites = (ArrayMarkerProperties) props.getPropertiesByName("Marker #2");
		scProps = (SourceCodeProperties) props.getPropertiesByName("Quelltext");
		barArrayRectProperties = (RectProperties) props.getPropertiesByName("Balken Elemente");
		shufflePointerProperties = (RectProperties) props.getPropertiesByName("Balken Marker #1");
		randomPointerProperties = (RectProperties) props.getPropertiesByName("Balken Marker #2");
		counterProperties = (CounterProperties) props.getPropertiesByName("Zugriffszähler");
	}

	private void placeObjects() {
		placeBarArray();
		placeList();
		placeStatistics();
		placeSourceCodes();

		// Adjust settings, after everything is init and placed
		Color hC = (Color) list.getProperties().get("cellHighlight");
		this.barArray.setHighlightColor(hC);
	}

	private void placeTitle() {
		titleNode = new Coordinates(margin, margin);
		title = lang.newText(titleNode, this.name, "title", null, titleProperties);
		boolean isTitleHidden = (boolean) titleProperties.get("hidden");
		topLeftContentNode = new Offset(0, isTitleHidden ? margin : 2 * margin, title, AnimalScript.DIRECTION_SW);
	}

	private void placeBarArray() {
		this.barArray = new BarArray(lang, barArrayRectProperties, listToSort);
		this.barArray.place(topLeftContentNode, 400, 50);

		this.barMarker1 = new BarArrayMarker(this.lang, "marker1", barArray, shufflePointerProperties);
		this.barMarker2 = new BarArrayMarker(this.lang, "marker2", barArray, randomPointerProperties);
	}

	private void placeList() {
		list = lang.newIntArray(new Offset(margin, 0, this.barArray.getBoundingBox(), AnimalScript.DIRECTION_NE),
				listToSort, "list", null, listProperties);

	}

	private void placeStatistics() {
		iterationNode = new Offset(0, margin, list, AnimalScript.DIRECTION_SW);
		iterationProperties = new TextProperties();
		iterationProperties.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
		iteration = lang.newText(iterationNode, "", "iteration", null, iterationProperties);

		counter = lang.newCounter(list);
		counterView = lang.newCounterView(counter, new Offset(0, margin, iteration, AnimalScript.DIRECTION_SW),
				counterProperties, true, true);
	}

	private void createArrayMarkers() {
		current = lang.newArrayMarker(list, 0, "current", null, currentMarkerProperties);
		next = lang.newArrayMarker(list, 0, "next", null, nextMarkerProperites);
		((StringPropertyItem) nextMarkerProperites.getItem("label")).set("rnd");
		random = lang.newArrayMarker(list, 0, "next", null, nextMarkerProperites);
	}

	private void showIntroduction() {
		String text = "Bogosort versucht die Liste durch zufälliges Mischen zu sortieren.\n"
				+ "Bei jedem Schritt wird das Array neu gemischt und geprüft ob es in der richtigen Reihenfolge ist."
				+ "\n\nBeschreibung:\n" + this.getDescription();
		introductionText = new TextBlock(lang, topLeftContentNode, textProperties);
		introductionText.addText(text);
	}

	private void hideIntroduction() {
		introductionText.hide();
	}

	private void showDiscussion() {
		hideAllPrimitivesExceptTitle();

		String text = "Da Bogosort auf dem zufälligen Mischen basiert, " + "\n"
				+ "ist die Laufzeit dem Zufall überlassen." + "\n"
				+ "Im besten Fall, also wenn die List bereits beim Start sortiert ist, " + "\n"
				+ "hat der Algorithmus eine Laufzeit von O(n). Im Worst-Case Szenario" + "\n"
				+ "terminiert der Algorithmus nie." + "\n"
				+ "Als durschnittliche Laufzeit kann O(n*n!) angenommen werden, " + "\n"
				+ "wobei n! die möglichen Permutation des Arrays darstellt und n die" + "\n"
				+ "die Laufzeit zum überprüfen des Arrays." + "\n" + "Beim Einsatz des Fisher-Yates Mischverfahrens, "
				+ "\n" + "ist die häufigste Operation das Vertauschen der Elemente.";

		discussionText = new TextBlock(lang, topLeftContentNode, textProperties);
		discussionText.addText(text);
	}

	private void hideAllPrimitivesExceptTitle() {
		lang.hideAllPrimitives();
		iteration.hide();
		title.show();
	}

	private void sort() {

		iteration.show();
		int step = 1;

		main.highlight("mainBegin");
		lang.nextStep();
		main.unhighlight("mainBegin");

		updateIteration(step);
		boolean isSorted;
		while (!(isSorted = isSorted(list)) && step < maxIterations) {
			shuffle(step);
			updateIteration(++step);
		}
		main.highlight("mainEnd");
		if (isSorted) {
			iteration.setText("sortiert nach " + step + " Iteration" + (step == 1 ? "" : "en"), null, null);
			iteration.changeColor(null, successColor, null, null);
		} else {
			iteration.setText("nicht sortiert nach maximaler Anzahl von Iterationen (" + step + ") - Abbruch!", null,
					null);
			iteration.changeColor(null, failureColor, null, null);
		}
		lang.nextStep();
	}

	private void updateIteration(int step) {
		iteration.setText("Iteration: " + step, null, null);
		iterationsWithSteppedSubFunctionsLeft--;
	}

	private boolean isSorted(IntArray list) {
		main.highlight("whileIsNotSorted");

		isSorted.highlight("isSortedBegin");
		showFunctionSourceCode(isSorted, stepwiseSubFunctions());

		nextStepIfStepwiseSubFunctions();
		isSorted.unhighlight("isSortedBegin");

		current.move(0, null, null);
		next.move(0, null, null);
		barMarker1.setTo(0);
		barMarker2.setTo(0);

		if (stepwiseSubFunctions()) {
			barMarker1.show();
			current.show();
		}
		list.highlightCell(current.getPosition(), null, null);
		barArray.highlight(current.getPosition(), null, null);
		while (current.getPosition() < list.getLength() - 1) {
			isSorted.highlight("iterateArray");
			nextStepIfStepwiseSubFunctions();
			next.increment(null, null);
			barMarker2.increment();
			isSorted.unhighlight("iterateArray");
			isSorted.highlight("compareEntries");
			if (stepwiseSubFunctions()) {
				next.show();
				barMarker2.show();
			}
			nextStepIfStepwiseSubFunctions();
			isSorted.unhighlight("compareEntries");

			if (list.getData(current.getPosition()) > list.getData(next.getPosition())) {
				list.highlightElem(current.getPosition(), next.getPosition(), null, null);

				isSorted.highlight("returnFalse");
				lang.nextStep();
				barArray.unhighlight(0, current.getPosition());
				list.unhighlightElem(current.getPosition(), next.getPosition(), null, null);
				barArray.unhighlight(current.getPosition(), next.getPosition());
				isSorted.unhighlight("returnFalse");
				unhighlightCells(list, 0, current.getPosition());
				current.hide();
				next.hide();
				barMarker1.hide();
				barMarker2.hide();
				main.unhighlight("whileIsNotSorted");
				hideFunctionSourceCode(isSorted, stepwiseSubFunctions());

				return false;
			}

			next.hide();
			barMarker2.hide();
			current.increment(null, getDefaultTiming());
			barMarker1.increment(null, getDefaultTiming());
			list.highlightCell(current.getPosition(), null, null);
			barArray.highlight(current.getPosition());
		}
		isSorted.highlight("returnTrue");
		lang.nextStep();

		main.unhighlight("whileIsNotSorted");
		hideFunctionSourceCode(isSorted, stepwiseSubFunctions());
		return true;
	}

	private boolean stepwiseSubFunctions() {
		return iterationsWithSteppedSubFunctionsLeft >= 0;
	}

	private void nextStepIfStepwiseSubFunctions() {
		/*
		 * FIXME: Ich finde, wir sollten das immer anzeigen lassen. Das verwirrt mehr, als das es nützlich ist, oder?
		 * Ist vor allem buggy, wenn beim swappen zwischendurch kein lang.nextStep() aufgerufen wird.
		 */
		if (stepwiseSubFunctions())
			lang.nextStep();
	}

	private void shuffle(int step) {
		main.highlight("shuffle");

		shuffle.highlight("shuffleBegin");
		showFunctionSourceCode(shuffle, stepwiseSubFunctions());

		nextStepIfStepwiseSubFunctions();
		shuffle.unhighlight("shuffleBegin");

		// set pointer to the last element
		current.show();
		barMarker1.setTo(list.getLength() - 1);
		barMarker1.show();

		// Shuffle with Fisher-Yates Algorithm
		for (int i = list.getLength() - 1; i > 0; i--) {
			// update visualisations
			shuffle.highlight("shuffleEnterLoop");

			current.move(i, null, null);
			if (i != list.getLength() - 1) {
				// in first iteration 'setTo(..)' is used
				barMarker1.decrement(null, getDefaultTiming());
			}

			nextStepIfStepwiseSubFunctions();

			shuffle.unhighlight("shuffleEnterLoop");
			shuffle.highlight("shufflePickRandom");
			// Pick random Element from the rest of the list
			int rndPick = new Random().nextInt(i);
			random.move(rndPick, null, null);
			random.show();
			barMarker2.setTo(rndPick);
			barMarker2.show();

			nextStepIfStepwiseSubFunctions();

			shuffle.unhighlight("shufflePickRandom");
			shuffle.highlight("suffleComment");
			shuffle.highlight("shuffleSaveToBuf");
			shuffle.highlight("shuffleOverwrite");
			shuffle.highlight("shuffleReadFromBuf");
			// and swap with current position
			list.swap(i, rndPick, null, getDefaultTiming());
			barArray.swap(i, rndPick, null, getDefaultTiming());

			nextStepIfStepwiseSubFunctions();

			barMarker2.hide();
			random.hide();
			shuffle.unhighlight("suffleComment");
			shuffle.unhighlight("shuffleSaveToBuf");
			shuffle.unhighlight("shuffleOverwrite");
			shuffle.unhighlight("shuffleReadFromBuf");
		}

		shuffle.highlight("shuffleEnd");
		lang.nextStep();
		shuffle.unhighlight("shuffleEnd");
		current.hide();
		barMarker1.hide();

		main.unhighlight("shuffle");
		hideFunctionSourceCode(shuffle, stepwiseSubFunctions());
	}

	/**
	 * Helper method because {@link IntArray#unhighlightCell(int, int, algoanim.util.Timing, algoanim.util.Timing)}
	 * seems to actually highlight cells.
	 */
	private void unhighlightCells(IntArray list, int from, int to) {
		for (int i = from; i <= to; i++)
			list.unhighlightCell(i, null, null);
	}

	private void placeSourceCodes() {
		main = placeMainSourceCode();
		isSorted = placeIsSortedSourceCode();
		shuffle = placeShuffleSourceCode();
	}

	private SourceCode placeMainSourceCode() {
		SourceCode sc = lang.newSourceCode(new Offset(0, margin, barArray.getBoundingBox(), AnimalScript.DIRECTION_SW),
				"main", null, scProps);

		sc.addCodeLine("public void bogosort(int[] list) {", "mainBegin", 0, null);
		sc.addCodeLine("while (!isSorted(list))", "whileIsNotSorted", 1, null);
		sc.addCodeLine("shuffle (list);", "shuffle", 2, null);
		sc.addCodeLine("}", "mainEnd", 0, null);

		return sc;
	}

	private SourceCode placeIsSortedSourceCode() {
		SourceCode sc = lang.newSourceCode(new Offset(0, margin, main, AnimalScript.DIRECTION_SW), "isSorted", null,
				scProps);

		sc.addCodeLine("private boolean isSorted(int[] list) {", "isSortedBegin", 0, null);
		sc.addCodeLine("for(int i = 0; i < list.length - 1; i++) {", "iterateArray", 1, null);
		sc.addCodeLine("if(list[i] > list[i+1])", "compareEntries", 2, null);
		sc.addCodeLine("return false;", "returnFalse", 3, null);
		sc.addCodeLine("}", "iterateArrayEnd", 1, null);
		sc.addCodeLine("return true;", "returnTrue", 1, null);
		sc.addCodeLine("}", "isSortedEnd", 0, null);

		return sc;
	}

	private SourceCode placeShuffleSourceCode() {
		SourceCode sc = lang.newSourceCode(new Offset(0, margin, isSorted, AnimalScript.DIRECTION_SW), "shuffle", null,
				scProps);

		sc.addCodeLine("private void shuffle(int[] list) {", "shuffleBegin", 0, null);
		// sc.addCodeLine("int rnd, buf;", "shuffleInitVars", 1, null);
		sc.addCodeLine("for (int i = list.length - 1; i > 0; i--) {", "shuffleEnterLoop", 1, null);
		sc.addCodeLine("int rnd = new Random().nextInt(i);", "shufflePickRandom", 2, null);
		sc.addCodeLine("// swap list[i] with list[rnd]", "suffleComment", 2, null);
		sc.addCodeLine("int buf = list[i];", "shuffleSaveToBuf", 2, null);
		sc.addCodeLine("list[i] = list[rnd];", "shuffleOverwrite", 2, null);
		sc.addCodeLine("list[rnd] = buf;", "shuffleReadFromBuf", 2, null);
		sc.addCodeLine("}", "shuffleEndLoop", 1, null);
		sc.addCodeLine("}", "shuffleEnd", 0, null);

		return sc;
	}

	/**
	 * Hide a subfunction below the main function.
	 */
	private void hideFunctionSourceCode(SourceCode func, boolean fade) {
		Timing timing = fade ? getDefaultTiming() : null;
		func.moveBy(null, 0, -offSetMainNext, null, timing);
		func.hide(timing);
	}

	/**
	 * Hide a subfunction above the main function.
	 */
	private void showFunctionSourceCode(SourceCode func, boolean fade) {
		Timing timing = fade ? getDefaultTiming() : null;
		func.show();
		func.moveBy(null, 0, offSetMainNext, null, timing);
	}

	/**
	 * After initial presentation of source code, move {@link shuffle} to the position of {@link isSorted}. So when
	 * expanding the subfunctions, they will be displayed at the same position.
	 */
	private void normalizeShuffleSourceCodePosition() {
		shuffle.moveTo(AnimalScript.DIRECTION_N, null, isSorted.getUpperLeft(), null, null);
	}

	/**
	 * The default timing for animations.
	 */
	private Timing getDefaultTiming() {
		return new MsTiming(200);
	}

	public String getName() {
		return this.name;
	}

	public String getAlgorithmName() {
		return "Bogosort";
	}

	public String getAnimationAuthor() {
		return "Julian Klomp,Milan Schmittner";
	}

	public String getDescription() {
		return "Bogosort ist ein indeterministischer Sortieralgorithmus, es kann sein, dass er nie terminiert. \n"
				+ "Das liegt daran, dass er in jeder Iterationsstufe die zu sortierende Liste zufällig permutiert (mischt).\n"
				+ "Nach jeder Iteration wird überprüft, ob die Liste sortiert ist; ist das der Fall, terminiert der Algorithmus.\n"
				+ "Bogosort wird auch als Monkeysort oder Stupidsort bezeichnet.";
	}

	public String getCodeExample() {
		return "public void bogosort(int[] list) {" + "\n" + "    while (!isSorted(list))" + "\n"
				+ "        shuffle (list);" + "\n" + "}";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

}