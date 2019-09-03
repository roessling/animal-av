package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Graph;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class BrentsAlgorithm implements Generator {
	private static final int LGND_MAX_SCHRITTE = 0;
	private static final int LGND_HASE = 1;
	private static final int LGND_IGEL = 2;
	private static final int LGND_LAENGE = 3;
	private static final int LGND_START = 4;

	private static final String NAME = "Brent's Algorithmus (Zyklen Findung)";

	private Language lang;
	private int initialValue;
	private int[] theFunction;

	public void init() {
		lang = new AnimalScript("Brent's Algorithmus (Zyklen Findung)",
				"Julian Wulfheide, Denis Caruso, Tim Wimmer", 800, 600);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		initialValue = (Integer) primitives.get("initialValue");
		theFunction = (int[]) primitives.get("theFunction");

		findCycle(theFunction, initialValue);

		return dirtyHack(lang.toString());
	}

	/**
	 * When creating grids using the AnimalScriptAPI, the API creates code that
	 * contains a "elementColor"-Attribute. When this "elementColor"-Attribute
	 * is set, highlighting on that grid does not work. So we have to remove it
	 * "the nasty way".
	 * 
	 * @param in
	 *            The "wrong" AnimalScript
	 * @return the corrected AnimalScript
	 */
	public static String dirtyHack(String in) {
		String original = "grid \"legend\" (350, 60) lines 5 columns 2  color (0, 0, 0) elementColor (0, 0, 0) fillColor (255, 255, 255) highlightTextColor (255, 0, 0) highlightBackColor (0, 0, 0) depth 1";
		String wanted = "grid \"legend\" (350, 60) lines 5 columns 2  color (0, 0, 0) fillColor (255, 255, 255) highlightTextColor (255, 0, 0) highlightBackColor (0, 0, 0) depth 1";

		return in.replace(original, wanted);
	}

	/**
	 * Find cycles in the passed array.
	 * 
	 * @param arr
	 *            The array representing a iterated function, like this:
	 *            <code>arr[i] = f(i)</code>
	 */
	public void findCycle(int[] arr, int x0) {
		int currentIgel;
		int currentHase;
		int max_schritte;
		int laenge;
		int start;

		/*
		 * Properties
		 */

		ArrayProperties seqProps = new ArrayProperties();
		seqProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 19));
		seqProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		seqProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		seqProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		seqProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		seqProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		seqProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Color.YELLOW);

		// Text
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 24));
		headerProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

		// Sourcecode
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 12));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		// "Igel"-Marker
		ArrayMarkerProperties igelProps = new ArrayMarkerProperties();
		igelProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "Igel");
		igelProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.ORANGE);
		igelProps.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);

		// "Hase"-Marker
		ArrayMarkerProperties haseProps = new ArrayMarkerProperties();
		haseProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		haseProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "Hase");

		// Tabulated function
		MatrixProperties funcProps = new MatrixProperties();
		funcProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		funcProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.RED);
		funcProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		funcProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);

		// Legend
		MatrixProperties legendProps = new MatrixProperties();
		legendProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		legendProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		legendProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.RED);

		Graph graph = drawGraph(arr);

		/*
		 * Add as many steps as needed to find a sequence, than add this
		 * sequence two more times
		 */
		List<Integer> seq = new ArrayList<Integer>(arr.length / 2);
		seq.add(x0);
		boolean quit = false;
		int index = 1;
		while (!quit) {
			int next = arr[seq.get(index++ - 1)];
			if (!seq.contains(next)) {
				seq.add(next);
			} else {
				// Add the complete sequence two more time
				List<Integer> actualSeq = new ArrayList<Integer>(seq.subList(
						seq.indexOf(next), seq.size()));
				seq.addAll(actualSeq);
				seq.addAll(actualSeq);

				quit = true;
			}
		}

		int[] sequence = new int[seq.size()];
		for (int i = 0; i < sequence.length; i++) {
			sequence[i] = seq.get(i);
		}

		Text header = lang.newText(new Coordinates(20, 20), NAME, "header",
				null, headerProps);

		RectProperties headerRectProperties = new RectProperties();
		headerRectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY,
				Color.ORANGE);
		headerRectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		headerRectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		lang.newRect(new Offset(-5, -5, header, AnimalScript.DIRECTION_NW),
				new Offset(5, 5, header, AnimalScript.DIRECTION_SE),
				"rectHeader", null, headerRectProperties);

		SourceCode txtAbschluss = lang.newSourceCode(new Coordinates(30, 45),
				"abschluss", null, scProps);

		SourceCode txtEinleitung = lang.newSourceCode(new Coordinates(30, 50),
				"einleitung", null, scProps);

		txtEinleitung
				.addCodeLine(
						"Brent's Algorithmus ist ein Algorithmus zum Auffinden von Zyklen",
						null, 0, null);
		txtEinleitung.addCodeLine(
				"in Zahlsequenzen. Konkret sind hier Sequenzen von Werten von",
				null, 0, null);
		txtEinleitung
				.addCodeLine(
						"iterierten Funktionen gemeint. Das sind Funktionen die immer wieder",
						null, 0, null);
		txtEinleitung
				.addCodeLine(
						"mit dem vorherigen Ergebnis der Funktionsauswertung aufgerufen",
						null, 0, null);
		txtEinleitung.addCodeLine(
				"werden. So eine Funktion lässt sich, mit zugehöriger", null,
				0, null);

		txtEinleitung
				.addCodeLine("Sequenz, z.B. so darstellen:", null, 0, null);

		String[][] strFunction = new String[arr.length + 1][2];
		strFunction[0][0] = "x";
		strFunction[0][1] = "f(x)";
		for (int i = 0; i < arr.length; i++) {
			strFunction[i + 1][0] = String.valueOf(i);
			strFunction[i + 1][1] = String.valueOf(arr[i]);

		}

		// TODO: This needs a header row...
		StringMatrix mtrxFunction = lang.newStringMatrix(new Coordinates(30,
				170), strFunction, "function", null, funcProps);

		IntArray iaSequence = lang.newIntArray(new Coordinates(150, 170),
				sequence, "array", null, seqProps);

		SourceCode desc2 = lang.newSourceCode(new Coordinates(90, 470),
				"desc2", null, scProps);

		desc2.addCodeLine(
				"Der Algorithmus arbeitet lediglich mit 2 Zeigern ('Hase' und",
				null, 0, null);
		desc2.addCodeLine(
				"'Igel'), die mit unterschiedlicher Geschwindigkeit die Sequenz",
				null, 0, null);
		desc2.addCodeLine(
				"durchlaufen. Am Schluss erhält man (wenn vorhanden) die Länge ",
				null, 0, null);
		desc2.addCodeLine("sowie den Startindex des Zyklus.", null, 0, null);
		desc2.addCodeLine("", null, 0, null);

		// --------------------------------------------------------------------
		lang.nextStep("Einleitung");
		// --------------------------------------------------------------------

		try {
			if (graph != null)
				graph.moveTo(AnimalScript.DIRECTION_SE, "translate",
						new Coordinates(420, 260), null, null);
		} catch (IllegalDirectionException e) {
			graph.hide();
		}

		txtEinleitung.hide();
		desc2.hide();

		SourceCode source = lang.newSourceCode(new Coordinates(140, 200),
				"source", null, scProps);

		source.addCodeLine("def brent(f, x0):", null, 0, null);
		source.addCodeLine("max_schritte = laenge = 1", null, 2, null); // 1
		source.addCodeLine("igel = x0", null, 2, null); // 2
		source.addCodeLine("hase = f(x0)", null, 2, null); // 3
		source.addCodeLine("while igel != hase:", null, 2, null); // 4
		source.addCodeLine("if max_schritte == laenge:", null, 4, null); // 5
		source.addCodeLine("igel = hase", null, 6, null); // 6
		source.addCodeLine("max_schritte *= 2", null, 6, null); // 7
		source.addCodeLine("laenge = 0", null, 6, null); // 8
		source.addCodeLine("hase = f(hase)", null, 4, null); // 9
		source.addCodeLine("laenge += 1", null, 4, null); // 10
		source.addCodeLine("", null, 0, null); // 11
		source.addCodeLine("start = 0", null, 2, null); // 12
		source.addCodeLine("igel = hase = x0", null, 2, null); // 13
		source.addCodeLine("for i in range(laenge):", null, 2, null); // 14
		source.addCodeLine("hase = f(hase)", null, 4, null); // 15
		source.addCodeLine("", null, 0, null); // 16
		source.addCodeLine("while igel != hase:", null, 2, null); // 17
		source.addCodeLine("igel = f(igel)", null, 4, null); // 18
		source.addCodeLine("hase = f(hase)", null, 4, null); // 19
		source.addCodeLine("start += 1", null, 4, null); // 20
		source.addCodeLine("", null, 0, null); // 21
		source.addCodeLine("return laenge, start", null, 2, null); // 22

		String[][] strLegend = new String[][] { { "max_schritte:", "-" },
				{ "hase:", "-" }, { "igel:", "-" }, { "laenge:", "-" },
				{ "start:", "-" } };
		StringMatrix legend = lang.newStringMatrix(new Coordinates(350, 60),
				strLegend, "legend", null, legendProps);

		// --------------------------------------------------------------------
		lang.nextStep();
		// --------------------------------------------------------------------

		currentIgel = 0;
		currentHase = 1;
		max_schritte = 1;
		laenge = 1;

		source.highlight(1); // max_schritte = laenge = 1
		source.highlight(2); // igel = x0
		source.highlight(3); // hase = f(x0)

		ArrayMarker markIgel = lang.newArrayMarker(iaSequence, currentIgel,
				"igel", null, igelProps);
		ArrayMarker markHase = lang.newArrayMarker(iaSequence, currentHase,
				"hase", null, haseProps);

		if (graph != null) {
			graph.highlightNode(sequence[currentHase], null, null);
			graph.highlightNode(sequence[currentIgel], null, null);
		}

		legend.put(LGND_MAX_SCHRITTE, 1, String.valueOf(max_schritte), null,
				null);
		legend.put(LGND_HASE, 1, String.valueOf(sequence[currentHase]), null,
				null);
		legend.put(LGND_IGEL, 1, String.valueOf(sequence[currentIgel]), null,
				null);
		legend.put(LGND_LAENGE, 1, String.valueOf(laenge), null, null);

		legend.highlightCellColumnRange(LGND_MAX_SCHRITTE, 0, 1, null, null);
		legend.highlightCellColumnRange(LGND_HASE, 0, 1, null, null);
		legend.highlightCellColumnRange(LGND_IGEL, 0, 1, null, null);
		legend.highlightCellColumnRange(LGND_LAENGE, 0, 1, null, null);

		// --------------------------------------------------------------------
		lang.nextStep("Initialisierung");
		// --------------------------------------------------------------------

		source.unhighlight(1);
		source.unhighlight(2);
		source.unhighlight(3);

		legend.unhighlightCellColumnRange(LGND_MAX_SCHRITTE, 0, 1, null, null);
		legend.unhighlightCellColumnRange(LGND_HASE, 0, 1, null, null);
		legend.unhighlightCellColumnRange(LGND_IGEL, 0, 1, null, null);
		legend.unhighlightCellColumnRange(LGND_LAENGE, 0, 1, null, null);

		boolean firstRunLengthOfCycle = true;

		source.highlight(4); // while igel != hase:
		while (sequence[currentIgel] != sequence[currentHase]) {
			source.highlight(4); // while igel != hase:

			// --------------------------------------------------------------------
			if (firstRunLengthOfCycle) {
				lang.nextStep("Laenge des Zyklus ermitteln");
				firstRunLengthOfCycle = false;
			} else
				lang.nextStep();
			// --------------------------------------------------------------------
			source.unhighlight(4);
			source.highlight(5); // if max_schritte == laenge:

			if (max_schritte == laenge) {
				// --------------------------------------------------------------------
				lang.nextStep();
				// --------------------------------------------------------------------

				source.unhighlight(5);
				source.highlight(6); // igel = hase

				legend.highlightCellColumnRange(LGND_IGEL, 0, 1, null, null);

				if (graph != null)
					graph.unhighlightNode(sequence[currentIgel], null, null);
				currentIgel = currentHase;
				if (graph != null) {
					graph.highlightNode(sequence[currentHase], null, null);
					graph.highlightNode(sequence[currentIgel], null, null);
				}

				markIgel.move(currentIgel, null, null);
				legend.put(LGND_IGEL, 1, String.valueOf(sequence[currentIgel]),
						null, null);

				// --------------------------------------------------------------------
				lang.nextStep();
				// --------------------------------------------------------------------

				source.unhighlight(6);
				source.highlight(7); // max_schritte *= 2

				legend.unhighlightCellColumnRange(LGND_IGEL, 0, 1, null, null);

				legend.highlightCellColumnRange(LGND_MAX_SCHRITTE, 0, 1, null,
						null);

				max_schritte = max_schritte * 2;
				legend.put(LGND_MAX_SCHRITTE, 1, String.valueOf(max_schritte),
						null, null);

				// --------------------------------------------------------------------
				lang.nextStep();
				// --------------------------------------------------------------------

				source.unhighlight(7);
				source.highlight(8); // laenge = 0

				legend.unhighlightCellColumnRange(LGND_MAX_SCHRITTE, 0, 1,
						null, null);

				legend.highlightCellColumnRange(LGND_LAENGE, 0, 1, null, null);

				laenge = 0;
				legend.put(LGND_LAENGE, 1, String.valueOf(laenge), null, null);
			}
			// --------------------------------------------------------------------
			lang.nextStep();
			// --------------------------------------------------------------------

			source.unhighlight(5);
			source.unhighlight(8);
			source.highlight(9); // hase = f(hase)

			legend.unhighlightCellColumnRange(LGND_LAENGE, 0, 1, null, null);

			legend.highlightCellColumnRange(LGND_HASE, 0, 1, null, null);

			if (graph != null)
				graph.unhighlightNode(sequence[currentHase], null, null);

			currentHase++;

			if (graph != null) {
				graph.highlightNode(sequence[currentHase], null, null);
				graph.highlightNode(sequence[currentIgel], null, null);
			}

			markHase.move(currentHase, null, null);
			legend.put(LGND_HASE, 1, String.valueOf(sequence[currentHase]),
					null, null);

			// --------------------------------------------------------------------
			lang.nextStep();
			// --------------------------------------------------------------------

			source.unhighlight(9);
			source.highlight(10); // laenge += 1

			legend.unhighlightCellColumnRange(LGND_HASE, 0, 1, null, null);
			legend.highlightCellColumnRange(LGND_LAENGE, 0, 1, null, null);

			laenge++;
			legend.put(LGND_LAENGE, 1, String.valueOf(laenge), null, null);

			// --------------------------------------------------------------------
			lang.nextStep();
			// --------------------------------------------------------------------
			source.unhighlight(10);
			legend.unhighlightCellColumnRange(LGND_LAENGE, 0, 1, null, null);
		}

		source.highlight(4); // while igel != hase:

		// --------------------------------------------------------------------
		lang.nextStep();
		// --------------------------------------------------------------------

		source.unhighlight(4);
		source.unhighlight(10);
		legend.highlightCellColumnRange(LGND_START, 0, 1, null, null);
		source.highlight(12); // start = 0
		start = 0;
		legend.put(LGND_START, 1, String.valueOf(start), null, null);

		// --------------------------------------------------------------------
		lang.nextStep("Korrekten Zeigerabstand einstellen");
		// --------------------------------------------------------------------

		source.unhighlight(12);
		source.highlight(13); // igel = hase = x0

		legend.unhighlightCellColumnRange(LGND_START, 0, 1, null, null);

		legend.highlightCellColumnRange(LGND_HASE, 0, 1, null, null);
		legend.highlightCellColumnRange(LGND_IGEL, 0, 1, null, null);

		if (graph != null) {
			graph.unhighlightNode(sequence[currentHase], null, null);
			graph.unhighlightNode(sequence[currentIgel], null, null);
		}
		currentIgel = currentHase = 0;
		if (graph != null) {
			graph.highlightNode(sequence[currentHase], null, null);
			graph.highlightNode(sequence[currentIgel], null, null);
		}

		markIgel.move(currentIgel, null, null);
		markHase.move(currentHase, null, null);
		legend.put(LGND_IGEL, 1, String.valueOf(sequence[currentIgel]), null,
				null);
		legend.put(LGND_HASE, 1, String.valueOf(sequence[currentHase]), null,
				null);

		// --------------------------------------------------------------------
		lang.nextStep();
		// --------------------------------------------------------------------

		source.unhighlight(13);

		legend.unhighlightCellColumnRange(LGND_IGEL, 0, 1, null, null);

		Text txtI = lang.newText(new Coordinates(360, 433), "", "i", null);

		for (int i = 0; i < laenge; i++) {
			legend.unhighlightCellColumnRange(LGND_HASE, 0, 1, null, null);
			source.unhighlight(15);
			source.highlight(14); // for i in range(laenge):

			txtI.setText("i = " + i, null, null);

			// --------------------------------------------------------------------
			lang.nextStep();
			// --------------------------------------------------------------------

			source.unhighlight(14);
			source.highlight(15);

			legend.highlightCellColumnRange(LGND_HASE, 0, 1, null, null);

			if (graph != null)
				graph.unhighlightNode(sequence[currentHase], null, null);
			currentHase++;
			if (graph != null) {
				graph.highlightNode(sequence[currentHase], null, null);
				graph.highlightNode(sequence[currentIgel], null, null);
			}

			markHase.move(currentHase, null, null);
			legend.put(LGND_HASE, 1, String.valueOf(sequence[currentHase]),
					null, null);
			// --------------------------------------------------------------------
			lang.nextStep();
			// --------------------------------------------------------------------
		}
		txtI.hide();
		legend.unhighlightCellColumnRange(LGND_HASE, 0, 1, null, null);

		source.unhighlight(15);
		source.highlight(17); // while igel != hase:

		boolean firstRunStartOfCycle = true;

		while (sequence[currentIgel] != sequence[currentHase]) {
			source.highlight(17);

			// --------------------------------------------------------------------
			if (firstRunStartOfCycle) {
				lang.nextStep("Korrekte Startposition einstellen");
				firstRunStartOfCycle = false;
			} else
				lang.nextStep();
			// --------------------------------------------------------------------

			source.unhighlight(17);
			source.highlight(18); // igel = f(igel)

			legend.highlightCellColumnRange(LGND_IGEL, 0, 1, null, null);

			if (graph != null)
				graph.unhighlightNode(sequence[currentIgel], null, null);
			currentIgel++;
			if (graph != null) {
				graph.highlightNode(sequence[currentIgel], null, null);
				graph.highlightNode(sequence[currentHase], null, null);
			}

			markIgel.move(currentIgel, null, null);
			legend.put(LGND_IGEL, 1, String.valueOf(sequence[currentIgel]),
					null, null);

			// --------------------------------------------------------------------
			lang.nextStep();
			// --------------------------------------------------------------------

			legend.unhighlightCellColumnRange(LGND_IGEL, 0, 1, null, null);
			source.unhighlight(18);
			source.highlight(19); // hase = f(hase)
			legend.highlightCellColumnRange(LGND_HASE, 0, 1, null, null);

			if (graph != null)
				graph.unhighlightNode(sequence[currentHase], null, null);
			currentHase++;
			if (graph != null) {
				graph.highlightNode(sequence[currentHase], null, null);
				graph.highlightNode(sequence[currentIgel], null, null);
			}

			markHase.move(currentHase, null, null);
			legend.put(LGND_HASE, 1, String.valueOf(sequence[currentHase]),
					null, null);

			// --------------------------------------------------------------------
			lang.nextStep();
			// --------------------------------------------------------------------

			legend.unhighlightCellColumnRange(LGND_HASE, 0, 1, null, null);
			source.unhighlight(19);
			source.highlight(20); // start += 1
			legend.highlightCellColumnRange(LGND_START, 0, 1, null, null);

			start++;
			legend.put(LGND_START, 1, String.valueOf(start), null, null);

			// --------------------------------------------------------------------
			lang.nextStep();
			// --------------------------------------------------------------------

			legend.unhighlightCellColumnRange(LGND_START, 0, 1, null, null);
			source.unhighlight(20);
		}

		source.highlight(17); // while igel != hase:

		// --------------------------------------------------------------------
		lang.nextStep();
		// --------------------------------------------------------------------

		source.unhighlight(17);
		source.highlight(22); // return laenge, start

		// --------------------------------------------------------------------
		lang.nextStep();
		// --------------------------------------------------------------------

		/*
		 * Abschluss
		 */

		StringBuffer sampleCycleSeq = new StringBuffer();

		if (graph != null)
			graph.hide();
		source.hide();
		markHase.hide();
		markIgel.hide();
		legend.hide();
		mtrxFunction.hide();

		txtAbschluss.addCodeLine("Wir erhalten also das Ergebnis:", null, 0,
				null);
		txtAbschluss.addCodeLine("", null, 0, null);
		txtAbschluss.addCodeLine("start=" + start + ", laenge=" + laenge, null,
				1, null);
		txtAbschluss.addCodeLine("", null, 0, null);
		txtAbschluss.addCodeLine("und finden so den Zyklus: ", null, 0, null);

		for (int i = start; i < start + laenge; i++) {
			sampleCycleSeq.append(sequence[i]).append(", ");
		}
		sampleCycleSeq.append(sampleCycleSeq.toString()).append("...");

		txtAbschluss.addCodeLine("", null, 0, null);
		txtAbschluss.addCodeLine(sampleCycleSeq.toString(), null, 1, null);

		txtAbschluss.addCodeLine("", null, 0, null);
		txtAbschluss.addCodeLine("", null, 0, null);
		txtAbschluss.addCodeLine("", null, 0, null);

		txtAbschluss.addCodeLine(
				"Ein vergleichbarer Alrogithmus ist Folyds\'s Hase und Igel",
				null, 0, null);
		txtAbschluss
				.addCodeLine(
						"Algorithmus. Er arbeitet ebenfalls mit zwei Zeigern, die die Liste",
						null, 0, null);
		txtAbschluss
				.addCodeLine(
						"mit unterschiedlichen Geschwindigkeiten durchlaufen. Der Hase",
						null, 0, null);
		txtAbschluss
				.addCodeLine(
						"springt jede Iteration ein Feld weiter, der Igel hingegen zwei.",
						null, 0, null);

		txtAbschluss.addCodeLine("", null, 0, null);

		txtAbschluss
				.addCodeLine(
						"Beide Algorithmen liegen in O(n). Brent\'s Algorithmus ist jedoch",
						null, 0, null);
		txtAbschluss.addCodeLine(
				"(laut Brent selbst) durchschnittlich 24-36% schneller.", null,
				0, null);

		iaSequence.highlightCell(start, start + laenge - 1, null, null);

		lang.nextStep("Abschluss");
	}

	private Graph drawGraph(int[] arr) {
		if (arr.length >= 10) {
			return null;
		}

		int OFFSET_X = 320;
		int OFFSET_Y = 200;

		GraphProperties props = new GraphProperties();
		props.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
		props.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		props.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);

		String[] labels = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			labels[i] = String.valueOf(i);
		}

		int[][] adjMatrix = new int[arr.length][arr.length];
		for (int i = 0; i < adjMatrix.length; i++)
			for (int j = 0; j < adjMatrix.length; j++)
				adjMatrix[i][j] = (arr[i] == j ? 1 : 0);

		Node[] nodes = new Node[arr.length];

		try {
			nodes[0] = new Coordinates(OFFSET_X, OFFSET_Y);
			nodes[1] = new Coordinates(OFFSET_X + 80, OFFSET_Y + 50);
			nodes[2] = new Coordinates(OFFSET_X + 120, OFFSET_Y + 120);
			nodes[3] = new Coordinates(OFFSET_X + 90, OFFSET_Y + 180);
			nodes[4] = new Coordinates(OFFSET_X + 20, OFFSET_Y + 230);
			nodes[5] = new Coordinates(OFFSET_X - 80, OFFSET_Y + 200);
			nodes[6] = new Coordinates(OFFSET_X - 170, OFFSET_Y + 170);
			nodes[7] = new Coordinates(OFFSET_X - 150, OFFSET_Y + 80);
			nodes[8] = new Coordinates(OFFSET_X - 90, OFFSET_Y + 20);
		} catch (ArrayIndexOutOfBoundsException e) {
			// More Nodes than we needed
		}

		return lang.newGraph("graphFunction", adjMatrix, nodes, labels, null,
				props);
	}

	public String getName() {
		return "Brent's Algorithmus (Zyklen Findung)";
	}

	public String getAlgorithmName() {
		return "Brent's Algorithmus (Zyklen Findung)";
	}

	public String getAnimationAuthor() {
		return "Julian Wulfheide, Denis Caruso, Tim Wimmer";
	}

	public String getDescription() {
		return "Brent's Algorithmus ist ein Algorithmus zum Auffinden von Zyklen in "
				+ "Zahlsequenzen. Konkret sind hier Sequenzen von Werten von"
				+ "iterierten Funktionen gemeint. Das sind Funktionen die immer wieder"
				+ "mit dem vorherigen Ergebnis der Funktionsauswertung aufgerufen"
				+ "werden [f(f(f(f(f(f(x0))))))]."
				+ "\n\n"
				+ "Um eine eigene Funktion einzugeben, tragen Sie bitte Werte in das Array "
				+ "'theFunction' ein. Dabei entspricht das Element an Stelle 1 dem Wert von f(1),"
				+ " das Element an der Stelle 2 dem Wert von f(2), usw. Dabei ist wichtig, dass "
				+ "Definitionsbereich und Wertebereich der Funktion übereinstimmen."
				+ "'initialValue' ist der erste Funktionswert (x0). (Wichtig: Bei einer Funktion"
				+ "mit mehr als 9 Funktionswerten, wird die Funktion nicht mehr als Graph dargestellt.)";
	}

	public String getCodeExample() {
		return "def brent(f, x0):" + "\n" + "    max_schritte = laenge = 1"
				+ "\n" + "    igel = x0" + "\n" + "    hase = f(x0)" + "\n"
				+ "    while igel != hase:" + "\n"
				+ "        if max_schritte == laenge:" + "\n"
				+ "            igel = hase" + "\n"
				+ "            max_schritte *= 2" + "\n"
				+ "            laenge = 0" + "\n" + "        hase = f(hase)"
				+ "\n" + "        laenge += 1" + "\n" + "\n" + "    start = 0"
				+ "\n" + "    igel = hase = x0" + "\n"
				+ "    for i in range(laenge):" + "\n"
				+ "        hase = f(hase)" + "\n" + "\n"
				+ "    while igel != hase:" + "\n" + "        igel = f(igel)"
				+ "\n" + "        hase = f(hase)" + "\n" + "        start += 1"
				+ "\n" + "    return laenge, start\";";
	}

	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

}