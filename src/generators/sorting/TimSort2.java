/*
 * TimSort.java
 * Jessey Widhalm, Kevin Trometer, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Timing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class TimSort2 implements Generator {
	private static Language lang;
	private static int[] intArr;
	private static ArrayProperties arrayProps;
	private static ArrayMarkerProperties amPropsi;
	private static ArrayMarkerProperties amPropsj;
	private static ArrayMarkerProperties amPropsPivot;
	private static ArrayMarkerProperties amPropsK;
	private static ArrayMarkerProperties amPropsLen2;

	private static SourceCode bInsertSortCode;
	private static SourceCode minRunLengthCode;
	private static SourceCode sortCode;
	private static SourceCode cntRnNdMkScndng;
	private static SourceCode mergeForceCollapseCode;
	private static SourceCode mergeCollapseCode;
	private static SourceCode mergeAtCode;
	private static SourceCode description;
	private static SourceCode conclusion;

	private static IntArray intArray;
	private static ArrayMarker am_left;
	private static ArrayMarker am_right;
	private static ArrayMarker am_pivot;
	private static ArrayMarker am_k;
	private static ArrayMarker am_len2;
	private static ArrayMarker am;

	private static Text header;
	private static Text minRunText;
	private static Text helpText;

	private static TextProperties textProps;

	private static boolean currColor = true; // true = c1 und false = c2
	private static Color c1 = Color.ORANGE;
	private static Color c2 = Color.CYAN;

	// Timsort Variables
	private static int MIN_MERGE;

	private static final int MIN_GALLOP = 7;

	private static int minGallop = MIN_GALLOP;

	private static int stackSize = 0;
	private static int[] runBase = new int[40];
	private static int[] runLen = new int[40];

	public TimSort2() {
		init();
	}

	public void init() {
		lang = new AnimalScript("Timsort", "Jessey Widhalm, Kevin Trometer", 800, 600);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		MIN_MERGE = (Integer) primitives.get("minMerge");
		intArr = (int[]) primitives.get("intArray");
		arrayProps = (ArrayProperties) props.getPropertiesByName("array");
		amPropsi = (ArrayMarkerProperties) props.getPropertiesByName("arrayMarker");
		amPropsi.set(AnimationPropertiesKeys.LABEL_PROPERTY, "l");
		amPropsj = new ArrayMarkerProperties("arrayMarker");
		amPropsj.set(AnimationPropertiesKeys.LABEL_PROPERTY, "r");
		amPropsPivot = new ArrayMarkerProperties("arrayMarker");
		amPropsPivot.set(AnimationPropertiesKeys.LABEL_PROPERTY, "p");
		amPropsK = new ArrayMarkerProperties("arrayMarker");
		amPropsK.set(AnimationPropertiesKeys.LABEL_PROPERTY, "k");
		amPropsLen2 = new ArrayMarkerProperties("arrayMarker");
		amPropsLen2.set(AnimationPropertiesKeys.LABEL_PROPERTY, "len2");

		init();

		textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));

		SourceCodeProperties descProps = new SourceCodeProperties();
		descProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		descProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		description = lang.newSourceCode(new Coordinates(20, 60), "description", null, descProps);
		conclusion = lang.newSourceCode(new Coordinates(20, 60), "conclusion", null, descProps);

		helpText = lang.newText(new Coordinates(20, 700), "", "helpText", null, textProps);

		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		header = lang.newText(new Coordinates(20, 20), "Timsort", "header", null, headerProps);
		intArray = lang.newIntArray(Node.convertToNode(new Point(10, 140)), intArr, "Array", null, arrayProps);
		am_left = lang.newArrayMarker(intArray, 0, "left", null, amPropsi);
		am_right = lang.newArrayMarker(intArray, 0, "right", null, amPropsj);
		am_pivot = lang.newArrayMarker(intArray, 0, "pivot", null, amPropsPivot);
		am_k = lang.newArrayMarker(intArray, 0, "pivot", null, amPropsK);
		am_len2 = lang.newArrayMarker(intArray, 0, "pivot", null, amPropsLen2);
		am = lang.newArrayMarker(intArray, 0, "arrayMarker", null);
		am.hide();
		am_left.hide();
		am_right.hide();
		am_pivot.hide();
		am_k.hide();
		am_len2.hide();
		intArray.hide();

		declarateSourceCodes();
		conclusion.hide();


		//Sort Aufruf
		sort(intArray, 0, intArray.getLength());
		helpText.setText("",Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		conclusion.show();
		minRunText.hide();
		sortCode.hide();
		Text textNotSorted = lang.newText(Node.convertToNode(new Point(20, 380)), "Unsortiert:", "Unsortiert", null, textProps);		
		IntArray notSortetIntArray = lang.newIntArray(Node.convertToNode(new Point(110, 380)), intArr, "Array", null, arrayProps);
		Text textSorted = lang.newText(Node.convertToNode(new Point(20, 440)), "Sortiert:", "sortiert", null, textProps);
		intArray.moveBy(null, 100, 290, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		
		// Auf anfang zurücksetzen
		minGallop = MIN_GALLOP;
		stackSize = 0;
		runBase = new int[40];
		runLen = new int[40];
		currColor = true;
		return lang.toString();
	}

	public String getName() {
		return "Timsort";
	}

	public String getAlgorithmName() {
		return "TimSort";
	}

	public String getAnimationAuthor() {
		return "Jessey Widhalm, Kevin Trometer";
	}

	public String getDescription() {
		return "TimSort ist ein adaptiver, stabiler, in-place Sortieralgorithmus,"
				+ "\nwelcher von MergeSort und BinaryInsertionSort abgeleitet ist,"
				+ "\nund 2002 von Tim Peters entwickelt wurde und Python Version 2.3+,"
				+ "\nJava SE 7+ und auf der Android-Plattform, als Standard-Sortieralgorithmus genutzt wird."
				+ "\nEs nutzt vorhandene Strukturen in Listen aus, so dass es nur n-1 Vergleiche"
				+ "\nfür Listen braucht, die bereits sortiert oder in streng absteigender Reihenfolge sind."
				+ "\nUm dies zu tun, geht TimSort die Liste durch und sucht bereits aufsteigend sortierte, oder"
				+ "\nstreng absteigend sortierte Elmente, sogennante \"Runs\"."
				+ "\nWenn ein Run in strikt absteigender Reihenfolge ist, kehrt TimSort ihn um."
				+ "\nWenn ein Lauf weniger Elemente, als eine vorher festgelegte \"minRun\"-Größe,"
				+ "\nhat, verwendet TimSort BinaryInsertionSort, um es auf minRun Elemente aufzustocken."
				+ "\nDiese Runs werden dann zu passenden Zeitpunkten mit MergeSort zusammengeführt.";
	}

	public String getCodeExample() {
		return "static void sort(int[] a, int lo, int hi) {" + "\n    int nRemaining = hi - lo;"
				+ "\n    if (nRemaining < 2)" + "\n        return; " + "\n    if (nRemaining < MIN_MERGE) {"
				+ "\n        int initRunLen = countRunAndMakeAscending(a, lo, hi);"
				+ "\n        binaryInsertionSort(a, lo, hi, lo + initRunLen);" + "\n        return;" + "\n    }"
				+ "\n    int minRun = minRunLength(nRemaining);" + "\n    do {"
				+ "\n        int runLen = countRunAndMakeAscending(a, lo, hi);" + "\n        if (runLen < minRun) {"
				+ "\n            int force = nRemaining <= minRun ? nRemaining : minRun;"
				+ "\n            binaryInsertionSort(a, lo, lo + force, lo + runLen);" + "\n            runLen = force;"
				+ "\n        }" + "\n        pushRun(lo, runLen);" + "\n        mergeCollapse();"
				+ "\n        lo += runLen;" + "\n        nRemaining -= runLen;" + "\n    } while (nRemaining != 0);"
				+ "\n    mergeForceCollapse();" + "\n}";
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

	static void sort(IntArray intArray, int lo, int hi) {
		intArray.show();
		lang.nextStep();
		sortCode.show();
		lang.nextStep();
		int nRemaining = hi - lo;
		sortCode.highlight(1);
		if (nRemaining < 2) {
			helpText.setText(
					"Da der sortierende Array kleiner als 2 ist, ist der Array schon sortiert und der Algorithmus terminiert",
					Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			sortCode.highlight(2);
			sortCode.highlight(3);
			intArray.setFillColor(0, intArray.getLength() - 1, Color.GREEN, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			return; // Array schon sortiert
		}

		// Wenn der array kleiner als min merge ist wird ein "mini-TimSort" ohne
		// merges ausgef�hrt
		if (nRemaining < MIN_MERGE) {
			helpText.setText("Da der Array kleiner als MIN_MERGE ist, wird ein mini Timsort ohne merge ausgefuehrt.",
					Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			sortCode.highlight(4);
			sortCode.highlight(5);
			lang.nextStep();
			sortCode.unhighlight(1);
			sortCode.unhighlight(4);
			helpText.setText(
					"Es wird nach schon sortierten Abschnitten geschaut, welche zu Runs zusammengefasst werden.",
					Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			int initRunLen = countRunAndMakeAscending(intArray, lo, hi);
			for (int i = lo; i < intArray.getLength(); i++) {
				markCellAt(i);
			}
			sortCode.unhighlight(5);
			sortCode.highlight(6);
			lang.nextStep();
			binarySort(intArray, lo, hi, lo + initRunLen);
			am_left.hide();
			am_right.hide();
			am_pivot.hide();
			bInsertSortCode.hide();
			intArray.setFillColor(0, intArray.getLength() - 1, Color.GREEN, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			return;
		}
		sortCode.highlight(9);
		helpText.setText(
				"nRemainig ist die laenge des noch zu sotierenden Abschnitts, auf die minRunLength() aufgerufen wird.",
				Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		lang.nextStep();
		sortCode.unhighlight(1);
		int minRun = minRunLength(nRemaining);
		sortCode.unhighlight(9);
		helpText.show();
		do {
			// Identify next run
			sortCode.highlight(11);
			helpText.setText(
					"Es wird nach schon sortierten Elementen geschaut, welche zu einem Run zusammengefasst werden.",
					Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			int runLen = countRunAndMakeAscending(intArray, lo, hi);
			sortCode.unhighlight(11);
			boolean toSmall = runLen < minRun;
			// If run is short, extend to min(minRun, nRemaining)
			if (runLen < minRun) {
				sortCode.highlight(12);
				sortCode.highlight(13);
				sortCode.highlight(14);
				int force = nRemaining <= minRun ? nRemaining : minRun;
				for (int i = lo; i < lo + force; i++) {
					markCellAt(i);
				}
				helpText.setText("Der Run wird um soviele Elemente erweitert, dass er die laenge minRun hat.",
						Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				lang.nextStep();
				sortCode.unhighlight(12);
				sortCode.unhighlight(13);
				helpText.setText("Der Run wird mit hilfe des BinaryInsertionSorts sortiert.", Timing.INSTANTEOUS,
						Timing.INSTANTEOUS);
				binarySort(intArray, lo, lo + force, lo + runLen);
				sortCode.unhighlight(14);
				am_left.hide();
				am_right.hide();
				am_pivot.hide();
				bInsertSortCode.hide();
				runLen = force;
				sortCode.highlight(15);
				sortCode.highlight(16);
				sortCode.highlight(17);
			}
			if (!toSmall) {
				sortCode.highlight(17);
			}
			helpText.setText("Run wird auf Run-Stack gepusht.", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			pushRun(lo, runLen);
			lang.nextStep();
			if (toSmall) {
				sortCode.unhighlight(15);
				sortCode.unhighlight(16);
			}
			sortCode.unhighlight(17);
			sortCode.highlight(18);
			mergeCollapse();
			sortCode.unhighlight(18);
			// Advance to find next run
			sortCode.highlight(19);
			sortCode.highlight(20);
			sortCode.highlight(21);
			lo += runLen;
			nRemaining -= runLen;
			helpText.setText(
					"Die Anzahl der noch zu bearbeitenden Elemente wird berechnet, sollte es keine geben werden die restlichen Runs gemerged.",
					Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			lang.nextStep();
			sortCode.unhighlight(19);
			sortCode.unhighlight(20);
			sortCode.unhighlight(21);
		} while (nRemaining != 0);
		sortCode.highlight(22);
		// Merge all remaining runs to complete sort
		mergeForceCollapse();
		sortCode.unhighlight(22);
		sortCode.highlight(23);
		intArray.setFillColor(0, intArray.getLength() - 1, Color.GREEN, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		lang.nextStep();
	}

	private static void mergeCollapse() {
		mergeCollapseCode.show();
		mergeCollapseCode.highlight(0);
		mergeCollapseCode.highlight(1);
		helpText.setText("Solange mehr als ein Run auf dem Stack ist, wird gemerged.", Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);
		lang.nextStep();
		while (stackSize > 1) {
			mergeCollapseCode.unhighlight(0);
			mergeCollapseCode.unhighlight(1);
			

			int n = stackSize - 2;
			if (n > 0 && runLen[n - 1] <= runLen[n] + runLen[n + 1]) {
				mergeCollapseCode.highlight(3);
				lang.nextStep();
				boolean ifTrue = false;
				if (runLen[n - 1] < runLen[n + 1]) {
					mergeCollapseCode.highlight(4);
					mergeCollapseCode.highlight(5);
					ifTrue = true;
					n--;
				}
				mergeCollapseCode.highlight(6);
				helpText.setText(
						"Stack-Groesse wird um 2 verkleinert und die richtige Stelle zum mergen an mergeAt() uebergeben.",
						Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				lang.nextStep();
				if (ifTrue) {
					mergeCollapseCode.unhighlight(4);
					mergeCollapseCode.unhighlight(5);
				}
				mergeCollapseCode.unhighlight(3);
				
				sortCode.moveBy(null, -535, 0, Timing.INSTANTEOUS, Timing.MEDIUM);
				mergeCollapseCode.moveBy(null, -535, 0, Timing.INSTANTEOUS, Timing.MEDIUM);
				mergeAt(n);
				sortCode.moveBy(null, 535, 0, Timing.INSTANTEOUS, Timing.MEDIUM);
				mergeCollapseCode.moveBy(null, 535, 0, Timing.INSTANTEOUS, Timing.MEDIUM);
				lang.nextStep();
				mergeCollapseCode.unhighlight(6);
			} else if (runLen[n] <= runLen[n + 1]) {
				mergeCollapseCode.highlight(3);
				mergeCollapseCode.highlight(7);
				lang.nextStep();
				mergeCollapseCode.highlight(8);
				helpText.setText(
						"Stack-Groesse wird um 2 verkleinert und die richtige Stelle zum mergen an mergeAt() uebergeben.",
						Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				lang.nextStep();
				mergeCollapseCode.unhighlight(3);

				mergeCollapseCode.unhighlight(7);
				sortCode.moveBy(null, -535, 0, Timing.INSTANTEOUS, Timing.MEDIUM);
				mergeCollapseCode.moveBy(null, -535, 0, Timing.INSTANTEOUS, Timing.MEDIUM);
				mergeAt(n);
				sortCode.moveBy(null, 535, 0, Timing.INSTANTEOUS, Timing.MEDIUM);
				mergeCollapseCode.moveBy(null, 535, 0, Timing.INSTANTEOUS, Timing.MEDIUM);
				mergeCollapseCode.unhighlight(8);
				lang.nextStep();
			} else {
				mergeCollapseCode.highlight(3);
				mergeCollapseCode.highlight(7);
				mergeCollapseCode.highlight(9);
				lang.nextStep();
				mergeCollapseCode.highlight(10);
				lang.nextStep();
				mergeCollapseCode.unhighlight(3); 
				mergeCollapseCode.unhighlight(7);

				mergeCollapseCode.unhighlight(9);
				mergeCollapseCode.unhighlight(10);
				mergeCollapseCode.hide();
				break; // Invariant is established
			}
			colorRuns();
		}
		colorRuns();
		mergeCollapseCode.hide();
	}

	private static void mergeAt(int i) {
		mergeAtCode.show(Timing.MEDIUM);
		int base1 = runBase[i];
		int len1 = runLen[i];
		int base2 = runBase[i + 1];
		int len2 = runLen[i + 1];

		runLen[i] = len1 + len2;
		boolean ifTrue = false;
		if (i == stackSize - 3) {
			ifTrue = true;
			runBase[i + 1] = runBase[i + 2];
			runLen[i + 1] = runLen[i + 2];
		}
		stackSize--;
		for (int j = 0; j < 11; j++) {
			if (j == 7 && !ifTrue) {
				j += 3l;
			}
			mergeAtCode.highlight(j);
		}
		lang.nextStep();
		for (int j = 0; j < 11; j++) {
			if (j == 7 && !ifTrue) {
				j += 3;
			}
			mergeAtCode.unhighlight(j);
		}
		mergeAtCode.highlight(11);
		helpText.setText("gallopRight sucht mithilfe eines abgewandelten BinarySort nach der  richtigen Stelle zum mergen und uebergibt bei gleichen Werten das rechtere.", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		int k = gallopRight(intArray.getData(base2), intArray, base1, len1, 0, true);
		mergeAtCode.unhighlight(11);
		mergeAtCode.highlight(12);
		mergeAtCode.highlight(13);
		base1 += k;
		len1 -= k;
		if (len1 == 0) {
			helpText.setText("Da der Run keine Elemente enthaelt, wird der Merge abgebrochen.", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			mergeAtCode.highlight(14);
			mergeAtCode.highlight(15);
			lang.nextStep();
			mergeAtCode.unhighlight(12);
			mergeAtCode.unhighlight(13);
			mergeAtCode.unhighlight(14);
			mergeAtCode.unhighlight(15);
			mergeAtCode.hide();
			return;
		}
		mergeAtCode.highlight(16);
		lang.nextStep();
		mergeAtCode.unhighlight(12);
		mergeAtCode.unhighlight(13);
helpText.setText("gallopRight sucht mithilfe eines abgewandelten BinarySort nach der  richtigen Stelle zum mergen und uebergibt bei gleichen Werten das linkere.", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		len2 = gallopLeft(intArray.getData(base1 + len1 - 1), intArray, base2, len2, len2 - 1, true);
		mergeAtCode.unhighlight(16);
		if (len2 == 0) {
			helpText.setText("Da der Run keine Elemente enthaelt, wird der Merge abgebrochen.", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			mergeAtCode.highlight(17);
			mergeAtCode.highlight(18);
			lang.nextStep();
			mergeAtCode.unhighlight(17);
			mergeAtCode.unhighlight(18);
			mergeAtCode.hide();
			return;
		}

		// Merge remaining runs, using tmp array with min(len1, len2) elements
		if (len1 <= len2) {
			helpText.setText("Da der erste Run kleiner gleiche Elemente enthaelt, wird mergeLo zum mergen aufgerufen.", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			mergeAtCode.highlight(19);
			mergeAtCode.highlight(20);
			lang.nextStep();
			mergeAtCode.unhighlight(19);
			mergeLo(base1, len1, base2, len2);
			mergeAtCode.unhighlight(20);
		} else {
			helpText.setText("Da der zweite Run kleinere Elemente enthaelt, wird mergeHi zum mergen aufgerufen.", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			mergeAtCode.highlight(19);
			mergeAtCode.highlight(21);
			mergeAtCode.highlight(22);
			lang.nextStep();
			mergeAtCode.unhighlight(19);
			mergeAtCode.unhighlight(21);
			mergeHi(base1, len1, base2, len2);
			mergeAtCode.unhighlight(22);
		}
		mergeAtCode.hide();
	}

	private static int gallopRight(int key, IntArray intArray2, int base, int len, int hint, boolean gallop) {
		am_k.move(base, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		if (gallop) {
			am_k.show();
		}
		int ofs = 1;
		int lastOfs = 0;
		if (gallop) {
			lang.nextStep();
		}
		if (key < intArray2.getData(base + hint)) {
			// Gallop left until a[b+hint - ofs] <= key < a[b+hint - lastOfs]
			int maxOfs = hint + 1;
			while (ofs < maxOfs && key < intArray2.getData(base + hint - ofs)) {
				lastOfs = ofs;
				ofs = (ofs << 1) + 1;
				if (ofs <= 0) // int overflow
					ofs = maxOfs;
				if (am_k.getPosition() != base + ofs && gallop) {
					am_k.move(base + ofs, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					lang.nextStep();
				}
			}
			if (ofs > maxOfs)
				ofs = maxOfs;

			// Make offsets relative to b
			int tmp = lastOfs;
			lastOfs = hint - ofs;
			ofs = hint - tmp;
			if (am_k.getPosition() != base + ofs && gallop) {
				am_k.move(base + ofs, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				lang.nextStep();
			}
		} else { // a[b + hint] <= key
			// Gallop right until a[b+hint + lastOfs] <= key < a[b+hint + ofs]
			int maxOfs = len - hint;
			while (ofs < maxOfs && key >= intArray2.getData(base + hint + ofs)) {
				lastOfs = ofs;
				ofs = (ofs << 1) + 1;
				if (ofs <= 0) // int overflow
					ofs = maxOfs;
				if (am_k.getPosition() != base + ofs && gallop) {
					am_k.move(base + ofs, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					lang.nextStep();
				}
			}
			if (ofs > maxOfs)
				ofs = maxOfs;
			// Make offsets relative to b
			lastOfs += hint;
			ofs += hint;
			if (am_k.getPosition() != base + ofs && gallop) {
				am_k.move(base + ofs, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				lang.nextStep();
			}
		}

		/*
		 * Now a[b + lastOfs] <= key < a[b + ofs], so key belongs somewhere to
		 * the right of lastOfs but no farther right than ofs. Do a binary
		 * search, with invariant a[b + lastOfs - 1] <= key < a[b + ofs].
		 */
		lastOfs++;
		while (lastOfs < ofs) {
			int m = lastOfs + ((ofs - lastOfs) >>> 1);

			if (key < intArray2.getData(base + m)) {
				ofs = m; // key < a[b + m]
				if (am_k.getPosition() != base + ofs && gallop) {
					am_k.move(base + ofs, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					lang.nextStep();
				}
			} else {
				lastOfs = m + 1; // a[b + m] <= key
				if (am_k.getPosition() != base + ofs && gallop) {
					am_k.move(base + ofs, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					lang.nextStep();
				}
			}
		}
		if (gallop) {
			am_k.hide();
		}
		return ofs;
	}

	private static int gallopLeft(int key, IntArray intArray2, int base, int len, int hint, boolean gallop) {
		am_len2.move(base, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		if (gallop) {
			am_len2.show();
			lang.nextStep();
		}
		int lastOfs = 0;
		int ofs = 1;
		if (key > intArray2.getData(base + hint)) {
			// Gallop right until a[base+hint+lastOfs] < key <= a[base+hint+ofs]
			int maxOfs = len - hint;
			while (ofs < maxOfs && key > intArray2.getData(base + hint + ofs)) {
				lastOfs = ofs;
				ofs = (ofs << 1) + 1;
				if (am_len2.getPosition() != base + ofs && gallop) {
					am_len2.move(base + ofs, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					lang.nextStep();
				}
			}
			if (ofs > maxOfs)
				ofs = maxOfs;

			// Make offsets relative to base
			lastOfs += hint;
			ofs += hint;
			if (am_len2.getPosition() != base + ofs && gallop) {
				am_len2.move(base + ofs, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				lang.nextStep();
			}
		} else { // key <= a[base + hint]
			// Gallop left until a[base+hint-ofs] < key <= a[base+hint-lastOfs]
			final int maxOfs = hint + 1;
			while (ofs < maxOfs && key <= intArray2.getData(base + hint - ofs)) {
				lastOfs = ofs;
				ofs = (ofs << 1) + 1;
				if (ofs <= 0) // int overflow
					ofs = maxOfs;
				if (am_len2.getPosition() != base + ofs && gallop) {
					am_len2.move(base + ofs, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					lang.nextStep();
				}
			}
			if (ofs > maxOfs)
				ofs = maxOfs;

			// Make offsets relative to base
			int tmp = lastOfs;
			lastOfs = hint - ofs;
			ofs = hint - tmp;
			if (am_len2.getPosition() != base + ofs && gallop) {
				am_len2.move(base + ofs, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				lang.nextStep();
			}
		}

		/*
		 * Now a[base+lastOfs] < key <= a[base+ofs], so key belongs somewhere to
		 * the right of lastOfs but no farther right than ofs. Do a binary
		 * search, with invariant a[base + lastOfs - 1] < key <= a[base + ofs].
		 */
		lastOfs++;
		while (lastOfs < ofs) {
			int m = lastOfs + ((ofs - lastOfs) >>> 1);

			if (key > intArray2.getData(base + m)) {
				lastOfs = m + 1; // a[base + m] < key
				if (am_len2.getPosition() != base + ofs && gallop) {
					am_len2.move(base + ofs, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					lang.nextStep();
				}
			} else {
				ofs = m; // key <= a[base + m]
				if (am_len2.getPosition() != base + ofs && gallop) {
					am_len2.move(base + ofs, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					lang.nextStep();
				}
			}
		}
		if (gallop) {
			am_len2.hide();
		}
		return ofs;
	}

	private static void mergeLo(int base1, int len1, int base2, int len2) {
		// Copy first run into temp array
		int[] temp = new int[intArray.getLength()];
		IntArray tmp = lang.newIntArray(Node.convertToNode(new Point(0, 0)), temp, "tmp", null, arrayProps);
		copyIntArray(intArray, base1, tmp, 0, len1);
		tmp.hide();

		int cursor1 = 0; // Indexes into tmp array
		int cursor2 = base2; // Indexes int a
		int dest = base1; // Indexes int a

		am.move(dest, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		am.show();
		lang.nextStep();
		// Move first element of second run and deal with degenerate cases
		intArray.put(dest++, intArray.getData(cursor2++), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		if (--len2 == 0) {
			copyIntArray(tmp, cursor1, intArray, dest, len1);
			am.move(dest + len1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			lang.nextStep();
			return;
		}
		if (len1 == 1) {
			copyIntArray(intArray, cursor2, intArray, dest, len2);
			intArray.put(dest + len2 - 1, tmp.getData(cursor1), Timing.INSTANTEOUS, Timing.INSTANTEOUS);// Last
																										// elt
																										// of
																										// run
																										// 1
																										// to
																										// end
																										// of
			// merge
			am.move(dest + len2, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			lang.nextStep();
			return;
		}

		int tempMinGallop = minGallop; // " " " " "
		outer: while (true) {
			int count1 = 0; // Number of times in a row that first run won
			int count2 = 0; // Number of times in a row that second run won

			/*
			 * Do the straightforward thing until (if ever) one run starts
			 * winning consistently.
			 */
			do {
				if (intArray.getData(cursor2) < tmp.getData(cursor1)) {
					intArray.put(dest++, intArray.getData(cursor2++), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					am.move(dest, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					lang.nextStep();
					count2++;
					count1 = 0;
					if (--len2 == 0) {
						break outer;
					}
				} else {
					intArray.put(dest++, tmp.getData(cursor1++), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					am.move(dest, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					lang.nextStep();
					count1++;
					count2 = 0;
					if (--len1 == 1) {
						break outer;
					}
				}
			} while ((count1 | count2) < tempMinGallop);

			/*
			 * One run is winning so consistently that galloping may be a huge
			 * win. So try that, and continue galloping until (if ever) neither
			 * run appears to be winning consistently anymore.
			 */
			do {
				count1 = gallopRight(intArray.getData(cursor2), tmp, cursor1, len1, 0, false);
				if (count1 != 0) {
					copyIntArray(tmp, cursor1, intArray, dest, count1);
					dest += count1;
					am.move(dest + count1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					lang.nextStep();
					cursor1 += count1;
					len1 -= count1;
					if (len1 <= 1) {// len1 == 1 || len1 == 0
						break outer;
					}
				}
				intArray.put(dest++, intArray.getData(cursor2++), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				am.move(dest, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				lang.nextStep();
				if (--len2 == 0) {
					break outer;
				}
				count2 = gallopLeft(tmp.getData(cursor1), intArray, cursor2, len2, 0, false);
				if (count2 != 0) {
					copyIntArray(intArray, cursor2, intArray, dest, count2);
					dest += count2;
					cursor2 += count2;
					len2 -= count2;
					am.move(dest + count2, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					lang.nextStep();
					if (len2 == 0) {
						break outer;
					}
				}
				intArray.put(dest++, tmp.getData(cursor1++), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				am.move(dest, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				lang.nextStep();
				if (--len1 == 1) {
					break outer;
				}
				tempMinGallop--;
			} while (count1 >= MIN_GALLOP | count2 >= MIN_GALLOP);
			if (tempMinGallop < 0)
				tempMinGallop = 0;
			tempMinGallop += 2; // Penalize for leaving gallop mode
		} // End of "outer" loop
		minGallop = tempMinGallop < 1 ? 1 : tempMinGallop; // Write back to
															// field

		if (len1 == 1)

		{
			copyIntArray(intArray, cursor2, intArray, dest, len2);
			intArray.put(dest + len2, tmp.getData(cursor1), Timing.INSTANTEOUS, Timing.INSTANTEOUS); // Last
																										// elt
																										// of
																										// run
																										// 1
																										// to
																										// end
																										// of
			// merge
			am.move(dest + len2, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			lang.nextStep();
		} else {
			copyIntArray(tmp, cursor1, intArray, dest, len1);
			am.move(dest + len1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			lang.nextStep();
		}
		am.hide();
		tmp.hide();
	}

	private static void mergeHi(int base1, int len1, int base2, int len2) {

		// Copy second run into temp array
		int[] temp = new int[intArray.getLength()];
		IntArray tmp = lang.newIntArray(Node.convertToNode(new Point(10, 160)), temp, "tmp", null, arrayProps);
		copyIntArray(intArray, base2, tmp, 0, len2);
		tmp.hide();

		int cursor1 = base1 + len1 - 1; // Indexes into a
		int cursor2 = len2 - 1; // Indexes into tmp array
		int dest = base2 + len2 - 1; // Indexes into a

		am.move(dest, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		am.show();
		lang.nextStep();

		// Move last element of first run and deal with degenerate cases
		intArray.put(dest--, intArray.getData(cursor1--), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		am.move(dest, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		lang.nextStep();
		if (--len1 == 0) {
			copyIntArray(tmp, 0, intArray, dest - (len2 - 1), len2);
			am.move(dest - (len2 - 1), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			lang.nextStep();
			return;
		}
		if (len2 == 1) {
			dest -= len1;
			cursor1 -= len1;
			copyIntArray(intArray, cursor1 + 1, intArray, dest + 1, len1);
			am.move(dest + 1 - len1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			lang.nextStep();
			intArray.put(dest, tmp.getData(cursor2), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			am.move(dest, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			lang.nextStep();
			return;
		}

		int tempMinGallop = minGallop; // " " " " "
		outer: while (true) {
			int count1 = 0; // Number of times in a row that first run won
			int count2 = 0; // Number of times in a row that second run won

			/*
			 * Do the straightforward thing until (if ever) one run appears to
			 * win consistently.
			 */
			do {
				if (tmp.getData(cursor2) < intArray.getData(cursor1)) {
					intArray.put(dest--, intArray.getData(cursor1--), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					am.move(dest, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					lang.nextStep();
					count1++;
					count2 = 0;
					if (--len1 == 0)
						break outer;
				} else {
					intArray.put(dest--, tmp.getData(cursor2--), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					am.move(dest, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					lang.nextStep();
					count2++;
					count1 = 0;
					if (--len2 == 1)
						break outer;
				}
			} while ((count1 | count2) < tempMinGallop);

			/*
			 * One run is winning so consistently that galloping may be a huge
			 * win. So try that, and continue galloping until (if ever) neither
			 * run appears to be winning consistently anymore.
			 */
			do {
				if (count1 != 0) {
					dest -= count1;
					cursor1 -= count1;
					len1 -= count1;
					copyIntArray(intArray, cursor1 + 1, intArray, dest + 1, count1);
					am.move(dest + 1 - count1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					lang.nextStep();
					if (len1 == 0)
						break outer;
				}
				intArray.put(dest--, tmp.getData(cursor2--), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				am.move(dest, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				lang.nextStep();
				if (--len2 == 1)
					break outer;

				count2 = len2 - gallopLeft(intArray.getData(cursor1), tmp, 0, len2, len2 - 1, false);
				if (count2 != 0) {
					dest -= count2;
					cursor2 -= count2;
					len2 -= count2;
					copyIntArray(tmp, cursor2 + 1, intArray, dest + 1, count2);
					am.move(dest + 1 - count2, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					lang.nextStep();
					if (len2 <= 1) // len2 == 1 || len2 == 0
						break outer;
				}
				intArray.put(dest--, intArray.getData(cursor1--), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				am.move(dest, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				lang.nextStep();
				if (--len1 == 0)
					break outer;
				tempMinGallop--;
			} while (count1 >= MIN_GALLOP | count2 >= MIN_GALLOP);
			if (tempMinGallop < 0)
				tempMinGallop = 0;
			tempMinGallop += 2; // Penalize for leaving gallop mode
		} // End of "outer" loop
		minGallop = tempMinGallop < 1 ? 1 : tempMinGallop; // Write back to
															// field

		if (len2 == 1) {
			dest -= len1;
			cursor1 -= len1;
			copyIntArray(intArray, cursor1 + 1, intArray, dest + 1, len1);
			intArray.put(dest, tmp.getData(cursor2), Timing.INSTANTEOUS, Timing.INSTANTEOUS);// Move
																								// first
																								// elt
																								// of
																								// run2
																								// to
																								// front
																								// of
			// merge
			am.move(dest, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			lang.nextStep();
		} else {
			copyIntArray(tmp, 0, intArray, dest - (len2 - 1), len2);
			am.move(dest - (len2 - 1), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			lang.nextStep();
		}
		am.hide();
		tmp.hide();
	}

	// Alle restlichen runs mergen
	private static void mergeForceCollapse() {
		mergeForceCollapseCode.show();
		mergeForceCollapseCode.highlight(0);
		helpText.setText("Alle vorhandenen Runs werden gemerged, bis nur noch ein Run vorhanden ist", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		if (stackSize <= 1) {
			lang.nextStep();
			mergeForceCollapseCode.unhighlight(0);
		}
		while (stackSize > 1) {
			mergeForceCollapseCode.highlight(1);
			mergeForceCollapseCode.highlight(2);
			boolean ifTrue = false;
			int n = stackSize - 2;
			if (n > 0 && runLen[n - 1] < runLen[n + 1]) {
				mergeCollapseCode.highlight(3);
				mergeCollapseCode.highlight(4);
				ifTrue = true;
				n--;
			}
			mergeForceCollapseCode.highlight(5);
			lang.nextStep();
			if (ifTrue) {
				mergeForceCollapseCode.unhighlight(3);
				mergeForceCollapseCode.unhighlight(4);
			}
			mergeForceCollapseCode.unhighlight(0);
			mergeForceCollapseCode.unhighlight(1);
			mergeForceCollapseCode.unhighlight(2);
			sortCode.moveBy(null, -535, 0, Timing.INSTANTEOUS, Timing.MEDIUM);
			mergeForceCollapseCode.moveBy(null, -535, 0, Timing.INSTANTEOUS, Timing.MEDIUM);
			mergeAt(n);
			sortCode.moveBy(null, 535, 0, Timing.INSTANTEOUS, Timing.MEDIUM);
			mergeForceCollapseCode.moveBy(null, 535, 0, Timing.INSTANTEOUS, Timing.MEDIUM);
			lang.nextStep();
			mergeForceCollapseCode.unhighlight(5);
			colorRuns();
		}
		mergeForceCollapseCode.hide();
	}

	private static int minRunLength(int n) {
		Text rText = lang.newText(Node.convertToNode(new Point(850, 200)), "r = ", "r", null, textProps);
		Text nText = lang.newText(Node.convertToNode(new Point(850, 225)), "n = ", "r", null, textProps);
		minRunText = lang.newText(new Coordinates(850, 250), "minRun = ", "MinRun", null, textProps);
		minRunLengthCode.show();
		minRunLengthCode.highlight(0);
		minRunLengthCode.highlight(1);
		int r = 0; // Becomes 1 if any 1 bits are shifted off
		rText.setText("r = " + r, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		nText.setText("n = " + n, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		minRunText.setText("minRun = " + n, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		helpText.setText("Anhand der Array laenge und minMerge wird jetzt berechnet wie lange ein Run mindestens sein muss.",
				Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		lang.nextStep();
		minRunLengthCode.unhighlight(0);
		minRunLengthCode.unhighlight(1);
		while (n >= MIN_MERGE) {
			minRunLengthCode.highlight(2);
			minRunLengthCode.highlight(3);
			r |= (n & 1);
			rText.setText("r = " + r, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			lang.nextStep();
			minRunLengthCode.unhighlight(2);
			minRunLengthCode.unhighlight(3);
			minRunLengthCode.highlight(4);
			n >>= 1;
			nText.setText("n = " + n, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			lang.nextStep();
			minRunLengthCode.unhighlight(4);
		}
		minRunLengthCode.highlight(6);
		minRunText.setText("minRun = " + n, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		lang.nextStep();
		minRunLengthCode.hide();
		nText.hide();
		rText.hide();
		minRunText.moveBy(null, -550, -190, Timing.INSTANTEOUS, Timing.MEDIUM);
		lang.nextStep();
		return n + r;
	}

	private static int countRunAndMakeAscending(IntArray intArray, int lo, int hi) {
		cntRnNdMkScndng.show();
		cntRnNdMkScndng.highlight(2);
		lang.nextStep();
		int runHi = lo + 1;
		markCellAt(lo);
		if (runHi == hi) {
			cntRnNdMkScndng.highlight(3);
			lang.nextStep();
			cntRnNdMkScndng.unhighlight(2);
			cntRnNdMkScndng.hide();
			return 1; 
		}

		// Find end of run, and reverse range if descending
		if (intArray.getData(runHi++) < intArray.getData(lo)) { // Descending
			cntRnNdMkScndng.unhighlight(2);
			cntRnNdMkScndng.highlight(4);
			
			lang.nextStep();
			cntRnNdMkScndng.unhighlight(4);
			while (runHi < hi && intArray.getData(runHi) < intArray.getData(runHi - 1)) {
				cntRnNdMkScndng.highlight(5);
				cntRnNdMkScndng.highlight(6);
				markCellAt(runHi - 1);
				runHi++;
				lang.nextStep();
			}
			markCellAt(runHi - 1);
			lang.nextStep();
			cntRnNdMkScndng.unhighlight(5);
			cntRnNdMkScndng.unhighlight(6);
			cntRnNdMkScndng.highlight(7);
			reverseRange(intArray, lo, runHi);
			lang.nextStep();
			cntRnNdMkScndng.unhighlight(7);
		} else { // Ascending
			cntRnNdMkScndng.unhighlight(2);
			cntRnNdMkScndng.highlight(4);
			cntRnNdMkScndng.highlight(8);
			lang.nextStep();
			cntRnNdMkScndng.unhighlight(4);
			cntRnNdMkScndng.unhighlight(8);
			
			while (runHi < hi && intArray.getData(runHi) >= intArray.getData(runHi - 1)) {
				cntRnNdMkScndng.highlight(9);
				cntRnNdMkScndng.highlight(10);
				markCellAt(runHi - 1);
				runHi++;
				lang.nextStep();
			}
			markCellAt(runHi - 1);
			lang.nextStep();
			cntRnNdMkScndng.unhighlight(9);
			cntRnNdMkScndng.unhighlight(10);
		}
		cntRnNdMkScndng.highlight(12);
		lang.nextStep();
		cntRnNdMkScndng.unhighlight(12);
		cntRnNdMkScndng.hide();

		return runHi - lo;
	}

	private static void binarySort(IntArray intArray, int lo, int hi, int start) {
		bInsertSortCode.show();
		bInsertSortCode.highlight(0);
		bInsertSortCode.highlight(1);
		bInsertSortCode.highlight(2);
		bInsertSortCode.highlight(3);
		bInsertSortCode.highlight(4);
		boolean init = true;
		if (start == lo)
			start++;

		for (; start < hi; start++) {
			bInsertSortCode.highlight(3);
			bInsertSortCode.highlight(4);
			int pivot = intArray.getData(start);
			am_pivot.move(start, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			am_pivot.show();
			lang.nextStep();
			if (init) {
				bInsertSortCode.unhighlight(0);
				bInsertSortCode.unhighlight(1);
				bInsertSortCode.unhighlight(2);
				init = false;
			}
			bInsertSortCode.unhighlight(3);
			bInsertSortCode.unhighlight(4);
			bInsertSortCode.highlight(5);
			int left = lo;
			am_left.move(lo, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			am_left.show();
			lang.nextStep();
			bInsertSortCode.unhighlight(5);
			bInsertSortCode.highlight(6);
			int right = start;
			am_right.move(start, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			am_right.show();
			lang.nextStep();
			bInsertSortCode.unhighlight(6);
			if (left < right) {
				init = true;
				bInsertSortCode.highlight(8);
				bInsertSortCode.highlight(9);
			}
			while (left < right) {
				int mid = (left + right) >>> 1;
				if (pivot < intArray.getData(mid)) {
					bInsertSortCode.highlight(10);
					bInsertSortCode.highlight(11);
					right = mid;
					am_right.move(right, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					lang.nextStep();
					bInsertSortCode.unhighlight(10);
					bInsertSortCode.unhighlight(11);
				} else {
					bInsertSortCode.highlight(10);
					bInsertSortCode.highlight(12);
					bInsertSortCode.highlight(13);
					left = mid + 1;
					am_left.move(left, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					lang.nextStep();
					bInsertSortCode.unhighlight(10);
					bInsertSortCode.unhighlight(12);
					bInsertSortCode.unhighlight(13);
				}
			}
			if (init) {
				bInsertSortCode.unhighlight(8);
				bInsertSortCode.unhighlight(9);
			}
			bInsertSortCode.highlight(16);
			bInsertSortCode.highlight(17);
			bInsertSortCode.highlight(18);
			int n = start - left;
			copyIntArray(intArray, left, intArray, left + 1, n);
			intArray.put(left, pivot, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			lang.nextStep();
			bInsertSortCode.unhighlight(16);
			bInsertSortCode.unhighlight(17);
			bInsertSortCode.unhighlight(18);
			if ((1 + start) < hi) {
				am_left.hide();
				am_right.hide();
				am_pivot.hide();
			}
		}
	}

	private static void pushRun(int base, int len) {
		runBase[stackSize] = base;
		runLen[stackSize] = len;
		stackSize++;
		colorRuns();
	}

	// Reihenfolge umkehren
	private static void reverseRange(IntArray intArray2, int lo, int hi) {
		hi--;
		while (lo < hi) {
			intArray.swap(lo, hi, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			lo++;
			hi--;
		}
	}

	public static IntArray copyIntArray(IntArray von, int beginnVon, IntArray zu, int beginnZu, int laenge) {
		int[] neu = new int[laenge];
		for (int i = 0; i < neu.length; i++) {
			neu[i] = von.getData(beginnVon + i);
		}
		for (int i = 0; i < laenge; i++) {
			zu.put(beginnZu + i, neu[i], Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		}
		return zu;
	}

	private static void colorRuns() {
		currColor = true;
		for (int i = 0; i < runBase.length; i++) {
			if (runBase[i] != 0 || runLen[i] != 0) {
				if (currColor) {
					intArray.setFillColor(runBase[i], runBase[i] + runLen[i] - 1, c1, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
				} else {
					intArray.setFillColor(runBase[i], runBase[i] + runLen[i] - 1, c2, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
				}
				currColor = !currColor;
				if (i + 1 < runBase.length && runBase[i] + runLen[i] == runBase[i + 1] + runLen[i + 1]) {
					break;
				}
			} else {
				break;
			}
		}
	}

	private static void markCellAt(int pos) {
		if (currColor) {
			intArray.setFillColor(pos, c1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		} else {
			intArray.setFillColor(pos, c2, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		}
	}

	private void declarateSourceCodes() {
		SourceCodeProperties codeProps = new SourceCodeProperties();
		codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		codeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);

		bInsertSortCode = lang.newSourceCode(new Coordinates(550, 180), "Binary Insertion Sort Source Code", null,
				codeProps);
		bInsertSortCode.addCodeLine("private static void binaryInsertionSort(int[] a, int lo, int hi, int start) {",
				null, 0, null);
		bInsertSortCode.addCodeLine("if (start == lo)", null, 1, null);
		bInsertSortCode.addCodeLine("start++;", null, 2, null);
		bInsertSortCode.addCodeLine("for (; start < hi; start++) {", null, 1, null);
		bInsertSortCode.addCodeLine("int p = a[start];", null, 2, null);
		bInsertSortCode.addCodeLine("int l = lo;", null, 2, null);
		bInsertSortCode.addCodeLine("int r = start;", null, 2, null);
		bInsertSortCode.addCodeLine("", null, 0, null);
		bInsertSortCode.addCodeLine("while (l < r) {", null, 2, null);
		bInsertSortCode.addCodeLine("int mid = (l + r) >>> 1;", null, 3, null);
		bInsertSortCode.addCodeLine("if (p < a[mid])", null, 3, null);
		bInsertSortCode.addCodeLine("r = mid;", null, 4, null);
		bInsertSortCode.addCodeLine("else", null, 3, null);
		bInsertSortCode.addCodeLine("l = mid + 1;", null, 4, null);
		bInsertSortCode.addCodeLine("}", null, 2, null);
		bInsertSortCode.addCodeLine("", null, 2, null);
		bInsertSortCode.addCodeLine("int n = start - l;", null, 2, null);
		bInsertSortCode.addCodeLine("System.arraycopy(a, l, a, l + 1, n);", null, 2, null);
		bInsertSortCode.addCodeLine("a[l] = p;", null, 2, null);
		bInsertSortCode.addCodeLine("}", null, 1, null);
		bInsertSortCode.addCodeLine("}", null, 0, null);
		bInsertSortCode.hide();

		minRunLengthCode = lang.newSourceCode(new Coordinates(550, 180), "Min Run Length Source Code", null, codeProps);
		minRunLengthCode.addCodeLine("private static int minRunLength(int n) {", null, 0, null);
		minRunLengthCode.addCodeLine("int r = 0;", null, 1, null);
		minRunLengthCode.addCodeLine("while (n >= MIN_MERGE) {", null, 1, null);
		minRunLengthCode.addCodeLine("r |= (n & 1);", null, 2, null);
		minRunLengthCode.addCodeLine("n >>= 1;", null, 2, null);
		minRunLengthCode.addCodeLine("}", null, 1, null);
		minRunLengthCode.addCodeLine("return n + r;", null, 1, null);
		minRunLengthCode.addCodeLine("}", null, 0, null);
		minRunLengthCode.hide();

		sortCode = lang.newSourceCode(new Coordinates(15, 180), "Main Sort Source Code", null, codeProps);
		sortCode.addCodeLine("static void sort(int[] a, int lo, int hi) {", null, 0, null);
		sortCode.addCodeLine("int nRemaining = hi - lo;", null, 1, null);
		sortCode.addCodeLine("if (nRemaining < 2)", null, 1, null);
		sortCode.addCodeLine("return;", null, 2, null);
		sortCode.addCodeLine("if (nRemaining < MIN_MERGE) {", null, 1, null);
		sortCode.addCodeLine("int initRunLen = countRunAndMakeAscending(a, lo, hi);", null, 2, null);
		sortCode.addCodeLine("binaryInsertionSort(a, lo, hi, lo + initRunLen);", null, 2, null);
		sortCode.addCodeLine("return;", null, 2, null);
		sortCode.addCodeLine("}", null, 1, null);
		sortCode.addCodeLine("int minRun = minRunLength(nRemaining);", null, 1, null);
		sortCode.addCodeLine("do {", null, 1, null);
		sortCode.addCodeLine("int runLen = countRunAndMakeAscending(a, lo, hi);", null, 2, null);
		sortCode.addCodeLine("if (runLen < minRun) {", null, 2, null);
		sortCode.addCodeLine("int force = nRemaining <= minRun ? nRemaining : minRun;", null, 3, null);
		sortCode.addCodeLine("binaryInsertionSort(a, lo, lo + force, lo + runLen);", null, 3, null);
		sortCode.addCodeLine("runLen = force;", null, 3, null);
		sortCode.addCodeLine("}", null, 2, null);
		sortCode.addCodeLine("pushRun(lo, runLen);", null, 2, null);
		sortCode.addCodeLine("mergeCollapse();", null, 2, null);
		sortCode.addCodeLine("lo += runLen;", null, 2, null);
		sortCode.addCodeLine("nRemaining -= runLen;", null, 2, null);
		sortCode.addCodeLine("} while (nRemaining != 0);", null, 1, null);
		sortCode.addCodeLine("mergeForceCollapse();", null, 1, null);
		sortCode.addCodeLine("}", null, 0, null);
		sortCode.hide();

		cntRnNdMkScndng = lang.newSourceCode(new Coordinates(550, 180), "count run and make ascending Source Code",
				null, codeProps);
		cntRnNdMkScndng.addCodeLine("private static int countRunAndMakeAscending(int[] a, int lo, int hi) {", null, 0,
				null);
		cntRnNdMkScndng.addCodeLine("int runHi = lo + 1;", null, 1, null);
		cntRnNdMkScndng.addCodeLine("if (runHi == hi)", null, 1, null);
		cntRnNdMkScndng.addCodeLine("return 1;", null, 2, null);
		cntRnNdMkScndng.addCodeLine("if (a[runHi++] < a[lo]) {", null, 1, null);
		cntRnNdMkScndng.addCodeLine("while (runHi < hi && a[runHi] < a[runHi - 1])", null, 2, null);
		cntRnNdMkScndng.addCodeLine("runHi++;", null, 3, null);
		cntRnNdMkScndng.addCodeLine("reverseRange(a, lo, runHi);", null, 2, null);
		cntRnNdMkScndng.addCodeLine("} else {", null, 1, null);
		cntRnNdMkScndng.addCodeLine("while (runHi < hi && a[runHi] >= a[runHi - 1])", null, 2, null);
		cntRnNdMkScndng.addCodeLine("runHi++;", null, 3, null);
		cntRnNdMkScndng.addCodeLine("}", null, 1, null);
		cntRnNdMkScndng.addCodeLine("return runHi - lo;", null, 1, null);
		cntRnNdMkScndng.addCodeLine("}", null, 0, null);
		cntRnNdMkScndng.hide();

		mergeForceCollapseCode = lang.newSourceCode(new Coordinates(550, 180), "merge force collapse Source Code", null,
				codeProps);
		mergeForceCollapseCode.addCodeLine("private void mergeForceCollapse() {", null, 0, null);
		mergeForceCollapseCode.addCodeLine("while (stackSize > 1) {", null, 1, null);
		mergeForceCollapseCode.addCodeLine("int n = stackSize - 2;", null, 2, null);
		mergeForceCollapseCode.addCodeLine("if (runLen[n - 1] < runLen[n + 1])", null, 2, null);
		mergeForceCollapseCode.addCodeLine("n--;", null, 3, null);
		mergeForceCollapseCode.addCodeLine("mergeAt(n);", null, 2, null);
		mergeForceCollapseCode.addCodeLine("}", null, 1, null);
		mergeForceCollapseCode.addCodeLine("}", null, 0, null);
		mergeForceCollapseCode.hide();

		mergeCollapseCode = lang.newSourceCode(new Coordinates(550, 180), "merge collapse Source Code", null,
				codeProps);
		mergeCollapseCode.addCodeLine("private void mergeCollapse() {", null, 0, null);
		mergeCollapseCode.addCodeLine("while (stackSize > 1) {", null, 1, null);
		mergeCollapseCode.addCodeLine("int n = stackSize - 2;", null, 2, null);
		mergeCollapseCode.addCodeLine("if (n > 0 && runLen[n - 1] <= runLen[n] + runLen[n + 1]) {", null, 2, null);
		mergeCollapseCode.addCodeLine("if (runLen[n - 1] < runLen[n + 1])", null, 3, null);
		mergeCollapseCode.addCodeLine("n--;", null, 4, null);
		mergeCollapseCode.addCodeLine("mergeAt(n);", null, 3, null);
		mergeCollapseCode.addCodeLine("} else if (runLen[n] <= runLen[n + 1]) {", null, 2, null);
		mergeCollapseCode.addCodeLine("mergeAt(n);", null, 3, null);
		mergeCollapseCode.addCodeLine("} else {", null, 2, null);
		mergeCollapseCode.addCodeLine("break", null, 3, null);
		mergeCollapseCode.addCodeLine("}", null, 2, null);
		mergeCollapseCode.addCodeLine("}", null, 1, null);
		mergeCollapseCode.addCodeLine("}", null, 0, null);
		mergeCollapseCode.hide();

		mergeAtCode = lang.newSourceCode(new Coordinates(550, 180), "merge At Source Code", null, codeProps);
		mergeAtCode.addCodeLine("private void mergeAt(int i) {", null, 0, null);
		mergeAtCode.addCodeLine("int base1 = runBase[i];", null, 1, null);
		mergeAtCode.addCodeLine("int len1 = runLen[i];", null, 1, null);
		mergeAtCode.addCodeLine("int base2 = runBase[i + 1];", null, 1, null);
		mergeAtCode.addCodeLine("int len2 = runLen[i + 1];", null, 1, null);
		mergeAtCode.addCodeLine("runLen[i] = len1 + len2;", null, 1, null);
		mergeAtCode.addCodeLine("if (i == stackSize - 3) {", null, 1, null);
		mergeAtCode.addCodeLine("runBase[i + 1] = runBase[i + 2];", null, 2, null);
		mergeAtCode.addCodeLine("runLen[i + 1] = runLen[i + 2];", null, 2, null);
		mergeAtCode.addCodeLine("}", null, 1, null);
		mergeAtCode.addCodeLine("stackSize--;", null, 1, null);
		mergeAtCode.addCodeLine("int k = gallopRight(a[base2], a, base1, len1, 0);", null, 1, null);
		mergeAtCode.addCodeLine("base1 += k;", null, 1, null);
		mergeAtCode.addCodeLine("len1 -= k;", null, 1, null);
		mergeAtCode.addCodeLine("if (len1 == 0)", null, 1, null);
		mergeAtCode.addCodeLine("return;", null, 2, null);
		mergeAtCode.addCodeLine("len2 = gallopLeft(a[base1 + len1 - 1], a, base2, len2, len2 - 1);", null, 1, null);
		mergeAtCode.addCodeLine("if (len2 == 0)", null, 1, null);
		mergeAtCode.addCodeLine("return;", null, 2, null);
		mergeAtCode.addCodeLine("if (len1 <= len2)", null, 1, null);
		mergeAtCode.addCodeLine("mergeLo(base1, len1, base2, len2);", null, 2, null);
		mergeAtCode.addCodeLine("else", null, 1, null);
		mergeAtCode.addCodeLine("mergeHi(base1, len1, base2, len2);", null, 2, null);
		mergeAtCode.addCodeLine("}", null, 0, null);
		mergeAtCode.hide();

		lang.nextStep();
		description.addCodeLine("TimSort ist ein adaptiver, stabiler, in-place Sortieralgorithmus, ", null, 0, null);
		description.addCodeLine("welcher von MergeSort und BinaryInsertionSort abgeleitet ist,", null, 0, null);
		description.addCodeLine("und 2002 von Tim Peters entwickelt wurde und Python Version 2.3+,", null, 0, null);
		description.addCodeLine(
				"Java SE 7+ und auf der Android-Plattform, als Standard-Sortieralgorithmus genutzt wird.", null, 0,
				null);
		lang.nextStep();
		description.addCodeLine("Es nutzt vorhandene Strukturen in Listen aus, so dass es nur n-1 Vergleiche", null, 0,
				null);
		description.addCodeLine(
				"fuer Listen braucht, die bereits sortiert oder in streng absteigender Reihenfolge sind.", null, 0,
				null);
		lang.nextStep();
		description.addCodeLine(
				"Um dies zu tun, geht TimSort die Liste durch und sucht bereits aufsteigend sortierte, oder", null, 0,
				null);
		description.addCodeLine("streng absteigend sortierte Elmente, sogennante Runs.", null, 0, null);
		description.addCodeLine("Wenn ein Run in strikt absteigender Reihenfolge ist, kehrt TimSort ihn um.", null, 0,
				null);
		lang.nextStep();
		description.addCodeLine("Wenn ein Lauf weniger Elemente, als eine vorher festgelegte minRun-Groesse,", null,
				0, null);
		description.addCodeLine("hat, verwendet TimSort BinaryInsertionSort, um es auf minRun Elemente aufzustocken.",
				null, 0, null);
		lang.nextStep();
		description.addCodeLine("Der minRun-Wert wird idealerweise so gewaehlt, dass er folgende drei Punkte erfuellt.",
				null, 0, null);
		lang.nextStep();
		description.addCodeLine(
				"1. Er sollte nicht zu gross sein, da er fuer den BinaryInsertionSort eine Rolle spielt, ", null, 0,
				null);
		description.addCodeLine("und dieser bei kuerzeren Feldern besser wirkt.", null, 0, null);
		lang.nextStep();
		description.addCodeLine("2. Er sollte nicht zu klein sein, da kuerzere Runs mehr merges bedeutet", null, 0,
				null);
		lang.nextStep();
		description.addCodeLine("3. Er sollte moeglichst halb so gross wie der gesamte Array sein, ", null, 0, null);
		description.addCodeLine("da die Runs dann eher gleich gross sind, wovon MergeSort profitiert.", null, 0, null);
		lang.nextStep();
		description.addCodeLine("Diese Runs werden dann zu passenden Zeitpunkten mit MergeSort zusammengefuehrt, ",
				null, 0, null);
		description.addCodeLine("naemlich dann, wenn die Laenge der Runs >= minRun sind. ", null, 0, null);
		lang.nextStep();
		description.addCodeLine("Der von TimSort benutzte MergeSort besitzt allerdings eine kleine Modifikation", null,
				0, null);
		lang.nextStep();
		description.addCodeLine(
				"Waehrend des Merges wird aufgezeichnet, aus welchem Run die Elemente verschoben werden.", null, 0,
				null);
		lang.nextStep();
		description.addCodeLine("Wenn sieben Elemente am Stueck aus dem selben Run verschoben werden", null, 0, null);
		description.addCodeLine("kann man davon ausgehen, dass auch das naechste Element aus diesem Run kommen wird",
				null, 0, null);
		description.addCodeLine("und wechselt in den Gallop-Modus.", null, 0, null);
		lang.nextStep();
		description.addCodeLine("Der funktioniert so, dass er den Run, aus dem die naechste Reihe Daten kommen soll,",
				null, 0, null);
		description.addCodeLine(
				"mit der binaeren Suche durchgeht, um das aktuelle Element in dem anderen Run zu finden.", null, 0,
				null);
		lang.nextStep();
		description.addCodeLine(
				"Wenn die passende Stelle gefunden wurde, koennen alle Elemente am Stueck verschoben werden.", null, 0,
				null);
		lang.nextStep();
		description.addCodeLine("Mit einem Best-Case von O(n) und Worst- und Average-Case von O(n log n)", null, 0,
				null);
		description.addCodeLine(
				"gehoert TimSort zu den effizientesten Sortieralgorithmen und wird zurecht haeufig als Standard verwendet. ",
				null, 0, null);
		description.hide();

		conclusion.addCodeLine("TimSort gehoert zu den effizientesten Sortieralgorithmen.", null, 0, null);
		conclusion.addCodeLine(
				"Zum Vergleich, hier die Komplexitaeten von Timsort und die von ihm genutzten Sortieralgorithmen:",
				null, 0, null);
		conclusion.addCodeLine("BinaryInsertionSort:", null, 0, null);
		conclusion.addCodeLine("Best-Case: O(n)	(bei sortierten Listen)", null, 1, null);
		conclusion.addCodeLine("Worst- und Average-Case: O(n^2)", null, 1, null);
		conclusion.addCodeLine("MergeSort:", null, 0, null);
		conclusion.addCodeLine("Worst-, Best- und Average-Case: O(n log n)", null, 1, null);
		conclusion.addCodeLine("TimSort:", null, 0, null);
		conclusion.addCodeLine("Best-Case: O(n)	(bei sortierten Listen)", null, 1, null);
		conclusion.addCodeLine("Worst- und Average-Case: O(n log n)", null, 1, null);
		conclusion.addCodeLine(
				"Wie man anhand der Komplexitaeten sieht, verbindet TimSort das Beste dieser beiden Sortieralgorithmen.",
				null, 0, null);
	}

}
