/*
 * TreeSort.java
 * Patrick Weber, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class TreeSort implements Generator, ValidatingGenerator {
	private static Language lang;
	private static ArrayProperties Zwischenspeichereigenschaften;
	private static ArrayProperties Ergebnisarrayeigenschaften;
	private static CircleProperties Baumeigenschaften;
	private static String[] Zwischenspeicher;

	static double[] m;

	public static StringArray sA;
	public static StringArray rA;
	public static ArrayMarkerProperties currentP;
	public static ArrayMarker current;

	static int xo = 100;
	static int yo = 150;
	static int length = 200;
	static int width = 400;
	static int step = 30;

	static Coordinates origin = new Coordinates(xo, yo);
	static Coordinates eox = new Coordinates(xo + width, yo);
	static Coordinates eoy = new Coordinates(xo, yo + length);

	static Circle[] points;
	static Text[] labels;

	static DecimalFormat f = new DecimalFormat("0.##");

	public static TextProperties circleLabelP;
	public static CircleProperties circleP;
	public static CircleProperties circleHLP;
	public static PolylineProperties plP;

	public static Text counterText;
	public static Text title;

	public static TextProperties titleP;
	public static SourceCodeProperties myscP;

	public static SourceCode mysc;
	public static SourceCode Conclusion;
	public static SourceCode description;

	public void init() {
		lang = new AnimalScript("TreeSort", "Patrick Weber", 800, 600);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		Zwischenspeichereigenschaften = (ArrayProperties) props.getPropertiesByName("Zwischenspeichereigenschaften");
		Ergebnisarrayeigenschaften = (ArrayProperties) props.getPropertiesByName("Ergebnisarrayeigenschaften");
		Baumeigenschaften = (CircleProperties) props.getPropertiesByName("Baumeigenschaften");
		Zwischenspeicher = (String[]) primitives.get("Zwischenspeicher");

		lang.setStepMode(true); // Schrittmodus aktivieren
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		showIntro();

		Sort();

		showOutro();

		lang.finalizeGeneration();

		// try {
		// return new String(lang.toString().getBytes("UTF-8"), "ISO-8859-1");
		// // Umlaute-Fix
		// } catch (UnsupportedEncodingException e) {
		// return "Error";
		// }
		return lang.toString();
	}

	public static double[] Sort() {
		int counterAcc = 0;
		int counterAss = 0;
		int noAskedQuestions = 0;
		

		MultipleChoiceQuestionModel dummy = new MultipleChoiceQuestionModel("Min");
		dummy.setGroupID("Minimum");
		dummy.setNumberOfTries(3);

		int n = Zwischenspeicher.length;

		double[][] m = new double[(2 * n - 1)][2]; // for calc
		double[] Sorted = new double[n]; // for result

		points = new Circle[m.length];
		labels = new Text[m.length];

		for (int i = 0; i < m.length; i++) {
			m[i][1] = i + 1;
		}

		mysc.highlight(5);
		mysc.highlight(6);
		mysc.highlight(7);
		mysc.highlight(8);

		// Werte ans Ende von m schreiben
		for (int i = 1; i <= n; i++) {
			m[n - 1 + i - 1][0] = Double.parseDouble(Zwischenspeicher[i - 1]); // Wert
			m[n - 1 + i - 1][1] = n + i - 1; // Position
		}

		String[] StringgL = toStringArray(m);

		// Erzeugen eines StringArray-Objects zur Visualisierung des Arrays;
		// nutzt die Properties
		sA = lang.newStringArray(new Coordinates(20, 40), StringgL, "array", null, Zwischenspeichereigenschaften);
		sA.showIndices(false, null, null);
		lang.newText(new Offset(0, 10, sA, AnimalScript.DIRECTION_SW), "Zwischenspeicher", "0", null);

		lang.nextStep();
		mysc.unhighlight(5);
		mysc.unhighlight(6);
		mysc.unhighlight(7);
		mysc.unhighlight(8);

		// Counter
		counterText = lang.newText(new Offset(0, -50, sA, AnimalScript.DIRECTION_SW), "test", "l1", null);

		updateCounterview(counterAcc, counterAss);

		// In diesem Array wird das Ergebnis abgelegt
		rA = lang.newStringArray(new Coordinates(20, 450), toStringArray(Sorted), "result_array", null,
				Ergebnisarrayeigenschaften);
		rA.showIndices(false, null, null);
		lang.newText(new Offset(0, -50, rA, AnimalScript.DIRECTION_SW), "Sortiertes Array", "1", null);

		CreateAndDrawTree(m);

		length = 90 + 50 * ((int) (Math.log(n) / Math.log(2)));
		//width = (int) (10 + 50 * Math.pow(2.0, Math.ceil(Math.log(n) / Math.log(2))));

		Coordinates uL = new Coordinates(origin.getX() - 10, origin.getY() - 10);
		Coordinates lR = new Coordinates(origin.getX() + width + 10, origin.getY() + length + 10);

		// Properties für das Rect erstellen
		RectProperties RectP = new RectProperties();
		RectP.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		RectP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		RectP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);

		lang.newRect(uL, lR, null, null, RectP);

		lang.nextStep("Erste Iteration");

		for (int i = n - 1; i >= 1; i--) {
			int leftchild = (2 * i);
			int rightchild = (2 * i) + 1;
			int parent = i;

			highlightIt(points[leftchild - 1]);
			highlightIt(points[rightchild - 1]);
			highlightIt(points[parent - 1]);

			sA.highlightCell((2 * i) - 1, null, null);
			sA.highlightCell(2 * i, null, null);
			sA.highlightCell(i - 1, null, null);

			mysc.highlight(9);
			mysc.highlight(10);
			mysc.highlight(11);
			mysc.highlight(12);

			counterAcc++;
			counterAcc++;
			updateCounterview(counterAcc, counterAss);

			m[i - 1][0] = min(m[(2 * i) - 1], m[2 * i])[0]; // Wert
			m[i - 1][1] = min(m[(2 * i) - 1], m[2 * i])[1]; // Position

			if ((Math.random() > 0.5) & (noAskedQuestions < 3)) {
				noAskedQuestions++;
				
				lang.nextStep();
				// TODO Fix NumberOfTries
				MultipleChoiceQuestionModel findMin = new MultipleChoiceQuestionModel("Min" + i);
				findMin.setGroupID("Minimum");
				findMin.setNumberOfTries(3);

				findMin.setPrompt("Was ist hier das Minimum?");
				if (m[(2 * i) - 1][0] == m[i - 1][0]) { // Minimum im leftchild
					findMin.addAnswer(m[(2 * i) - 1][0] + "", 1,
							"Richtig, der kleinere Wert (" + m[(2 * i) - 1][0] + ") stand an ursprünglich "
									+ f.format(m[(2 * i) - 1][1]) + ". Stelle im Zwischenspeicher.");
					findMin.addAnswer(m[(2 * i) - 1][1] + "", 0,
							"Falsch. (" + f.format(m[(2 * i) - 1][1])
									+ ") steht für die ursprüngliche Position des kleineren Wertes ("
									+ m[(2 * i) - 1][0] + ") im Zwischenspeicher.");
					findMin.addAnswer(m[2 * i][0] + "", 0, "Falsch, das ist der größere Wert.");
					findMin.addAnswer(m[2 * i][1] + "", 0,
							"Falsch, das ist die ursprüngliche Position des größeren Wertes.");

				} else { // Minimum im rightchild
					findMin.addAnswer(m[(2 * i) - 1][0] + "", 0, "Falsch, das ist der größere Wert.");
					findMin.addAnswer(m[(2 * i) - 1][1] + "", 0,
							"Falsch, das ist die ursprüngliche Position des größeren Wertes.");
					findMin.addAnswer(m[2 * i][0] + "", 1, "Richtig, der kleinere Wert (" + m[(2 * i)][0]
							+ ") stand an ursprünglich " + f.format(m[2 * i][1]) + ". Stelle im Zwischenspeicher.");
					findMin.addAnswer(m[2 * i][1] + "", 0,
							"Falsch. (" + f.format(m[(2 * i)][1])
									+ ") steht für die ursprüngliche Position des kleineren Wertes (" + m[(2 * i)][0]
									+ ") im Zwischenspeicher.");

				}

				lang.addMCQuestion(findMin);
			}
			
			lang.nextStep();

			sA.hide();
			sA = lang.newStringArray(sA.getUpperLeft(), toStringArray(m), "array", null, Zwischenspeichereigenschaften);
			sA.showIndices(false, null, null);

			sA.highlightCell(i - 1, null, null);

			CreateAndDrawTree(m);
			highlightIt(points[parent - 1]);
			counterAss++;
			updateCounterview(counterAcc, counterAss);

			lang.nextStep();
			unhighlightIt(points[parent - 1]);
			sA.unhighlightCell(i - 1, null, null);

			mysc.unhighlight(9);
			mysc.unhighlight(10);
			mysc.unhighlight(11);
			mysc.unhighlight(12);

		}
		lang.nextStep("Zweite Iteration");

		for (int j = 0; j < n; j++) {
			rA.hide();
			counterAss++;
			counterAcc++;
			updateCounterview(counterAcc, counterAss);
			Sorted[j] = m[0][0]; // Wert
			int i = (int) m[0][1]; // Position
			rA = lang.newStringArray(rA.getUpperLeft(), toStringArray(Sorted), "result_array", null,
					Ergebnisarrayeigenschaften);
			rA.showIndices(false, null, null);

			rA.highlightCell(j, null, null);
			sA.highlightCell(0, null, null);
			highlightIt(points[0]);
			mysc.highlight(14);

			lang.nextStep();
			unhighlightIt(points[0]);
			rA.unhighlightCell(j, null, null);
			sA.unhighlightCell(0, null, null);
			mysc.unhighlight(14);

			sA.highlightCell(i - 1, null, null);
			highlightIt(points[i - 1]);

			m[i - 1][0] = (double) Integer.MAX_VALUE;
			mysc.highlight(16);

			lang.nextStep();
			sA.hide();
			sA = lang.newStringArray(sA.getUpperLeft(), toStringArray(m), "array", null, Zwischenspeichereigenschaften);
			sA.showIndices(false, null, null);
			sA.highlightCell(i - 1, null, null);

			CreateAndDrawTree(m);
			highlightIt(points[i - 1]);
			counterAss++;
			updateCounterview(counterAcc, counterAss);

			lang.nextStep();

			sA.unhighlightCell(i - 1, null, null);
			unhighlightIt(points[i - 1]);
			mysc.unhighlight(16);

			for (i = (int) (i / 2.0); i >= 1; i = (int) (i / 2.0)) {
				int leftchild = (2 * i);
				int rightchild = (2 * i) + 1;
				int parent = i;

				highlightIt(points[leftchild - 1]);
				highlightIt(points[rightchild - 1]);
				highlightIt(points[parent - 1]);

				sA.highlightCell((2 * i) - 1, null, null);
				sA.highlightCell(2 * i, null, null);
				sA.highlightCell(i - 1, null, null);

				mysc.highlight(17);
				mysc.highlight(18);
				mysc.highlight(19);
				mysc.highlight(20);

				lang.nextStep();

				counterAcc++;
				counterAcc++;
				updateCounterview(counterAcc, counterAss);
				m[i - 1][0] = min(m[(2 * i) - 1], m[2 * i])[0]; // Wert
				m[i - 1][1] = min(m[(2 * i) - 1], m[2 * i])[1]; // Position

				sA.hide();
				sA = lang.newStringArray(sA.getUpperLeft(), toStringArray(m), "array", null,
						Zwischenspeichereigenschaften);
				sA.showIndices(false, null, null);
				sA.highlightCell(i - 1, null, null);

				CreateAndDrawTree(m);
				highlightIt(points[parent - 1]);
				counterAss++;
				updateCounterview(counterAcc, counterAss);

				lang.nextStep();
				unhighlightIt(points[parent - 1]);
				sA.unhighlightCell(i - 1, null, null);

				mysc.unhighlight(17);
				mysc.unhighlight(18);
				mysc.unhighlight(19);
				mysc.unhighlight(20);

			}

		}
		lang.nextStep();
		return Sorted;
	}

	public static String[] toStringArray(double[][] IN_Array) {
		String[] result = new String[IN_Array.length];

		for (int i = 0; i < result.length; i++) {
			if (IN_Array[i][0] == Integer.MAX_VALUE) {
				result[i] = "--";
			} else {
				result[i] = "(" + f.format(IN_Array[i][0]) + "|" + f.format(IN_Array[i][1]) + ")";
			}
		}

		return result;
	}

	public static String[] toStringArray(double[] IN_Array) {
		String[] result = new String[IN_Array.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = f.format(IN_Array[i]);
		}

		return result;
	}

	public static void CreateAndDrawTree(double[][] m) {
		if (labels != null) {
			for (int i = 0; i < labels.length; i++) {
				if (labels[i] != null) {
					labels[i].hide();
				}
			}
		}

		// Erzeugen des Baumes
		TreeSortNode root = new TreeSortNode();
		root = createTree(toStringArray(m), root, 0);

		// Einzeichnen des Baumes
		recursivelyDrawTree(root, new Coordinates(origin.getX() + (width / 2), origin.getY() + 20), width / 4);
	}

	public static TreeSortNode createTree(String[] IN_sa, TreeSortNode root, int i) {
		if (i < IN_sa.length) {
			TreeSortNode tmp = new TreeSortNode(IN_sa[i], i);
			root = tmp;
			root.left = createTree(IN_sa, root.left, 2 * i + 1);
			root.right = createTree(IN_sa, root.right, 2 * i + 2);
		}
		return root;
	}

	public static void recursivelyDrawTree(TreeSortNode morseTreeNode, Coordinates startPoint, int width) {
		circleLabelP = new TextProperties();
		circleLabelP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		circleLabelP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 10));
		circleLabelP.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
		circleLabelP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

		circleP = new CircleProperties();
		circleP.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		circleP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		circleP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		circleP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		plP = new PolylineProperties();
		plP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

		int vertDistance = 50;

		// Create left child node
		if (morseTreeNode.left != null) {

			Coordinates childPosition = new Coordinates(startPoint.getX() - width, startPoint.getY() + vertDistance);
			Polyline line = lang.newPolyline(new Node[] { startPoint, childPosition }, ".", null, plP);
			recursivelyDrawTree(morseTreeNode.left, childPosition, width / 2);

		}
		// Create right child node
		if (morseTreeNode.right != null) {
			Coordinates childPosition = new Coordinates(startPoint.getX() + width, startPoint.getY() + vertDistance);
			Polyline line = lang.newPolyline(new Node[] { startPoint, childPosition }, ".", null, plP);
			recursivelyDrawTree(morseTreeNode.right, childPosition, width / 2);

		}

		// create Circle objects in the end so they are on top of the lines.
		morseTreeNode.circle = lang.newCircle(startPoint, 20, morseTreeNode.data, null, circleP);
		// Store Circle in points Array
		points[morseTreeNode.NoInArr] = morseTreeNode.circle; 
		labels[morseTreeNode.NoInArr] = lang.newText(morseTreeNode.circle.getCenter(), morseTreeNode.data,
				morseTreeNode.data, null, circleLabelP);
	}

	public static void highlightIt(Circle toHL) {

		toHL = lang.newCircle(toHL.getCenter(), toHL.getRadius(), toHL.getName(), null, Baumeigenschaften);

	}

	public static void unhighlightIt(Circle toUHL) {
		toUHL = lang.newCircle(toUHL.getCenter(), toUHL.getRadius(), toUHL.getName(), null, circleP);

	}

	public static double[] min(double[] a, double[] b) {
		if (a[0] < b[0]) { // Vergleich der Werte
			return a;
		}
		return b;
	}

	public static void updateCounterview(int counterAcc, int counterAss) {
		String message = "Sortieren erfolgt mit " + counterAcc + " Zugriffen und " + counterAss + " Zuweisungen.";
		counterText.setText(message, null, null);
	}

	public void showOutro() {
		lang.hideAllPrimitives();
		title.show();

		String concl = "TreeSort bietet eine einfache und nachvollziehbare Möglichkeit\n"
				+ "ein Datenfeld zu sortieren. Wichtig ist dabei, dass es für die \n"
				+ "Elemente des Arrays eine totale Ordnungsrelation gibt, also zu\n"
				+ "zwei gegebenen Werten stets das Minimum bestimmt werden kann.\n"
				+ "Die optimale Laufzeit beträgt O(n*logn), es wird aber O(n)\n"
				+ "zusätzlicher Speicher benötigt. Der Sortieralgorithmus ist zudem\n"
				+ "nicht stabil und arbeitet out-of-place.\n";

		Conclusion = lang.newSourceCode(new Coordinates(50, 60), "Conclusion", null);
		Conclusion.addMultilineCode(concl, "0", null);

		lang.nextStep("Fazit");

	}

	public void showIntro() {
		titleP = new TextProperties();
		titleP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		titleP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
		title = lang.newText(new Coordinates(800, 10), "TreeSort", "Title", null, titleP);

		String desc = "Der hier umgesetze TreeSort-Algorithmus legt zunächst ein Array an, das doppelt so lang wie das"
				+ "\n"
				+ "zu sortierende ist. Das unsortierte Array wird dann in der zweiten Hälfte des neu angelegten Array"
				+ "\n" + "gespeichert." + "\n"
				+ "Nun kann aus diesem Array ein binärer Baum konstruiert werden, das Array entspricht " + "\n"
				+ "dabei einer Level-by-Level-Traversierung des Baumes. Das hat den Effekt, dass das unsortierte "
				+ "\n" + "Array nun die Blätter des Baumes bildet. " + "\n"
				+ "Es wird nun in jedem Iterationsschritt das Minimum des aktuellen Baumes ermittelt, extrahiert \n"
				+ "und dann im Baum gelöscht um das nächste Minimum finden zu können. Nacheinander wird damit das \n"
				+ "sortierte Array als Sequenz der Minima konstruiert. \n"
				+ "Der Sortieralgorithmus wurde 1962 von Robert Floyd eingeführt und ist Vorgänger von Heapsort.";

		description = lang.newSourceCode(new Coordinates(50, 60), "0", null);
		description.addMultilineCode(desc, "0", null);

		lang.nextStep("Einleitung");

		addSC();

		lang.nextStep();
		description.hide();

//		FillInBlanksQuestionModel algoYear = new FillInBlanksQuestionModel("year");
//		algoYear.setPrompt("In welchem Jahr wurde der Algorithmus eingeführt?");
//		algoYear.addAnswer("1962", 1, "Richtig. Im Jahre 1962 von Robert Floyd.");
//		lang.addFIBQuestion(algoYear);
//
//		lang.nextStep();
//
//		FillInBlanksQuestionModel algoDescendant = new FillInBlanksQuestionModel("descendant");
//		algoDescendant.setPrompt("Welcher Algorithmus ist Nachfolger von TreeSort?");
//		algoDescendant.addAnswer("Heapsort", 1, "Richtig. TreeSort ist Vorgänger von Heapsort.");
//		lang.addFIBQuestion(algoDescendant);

	}

	public static void addSC() {
		myscP = new SourceCodeProperties();
		myscP.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);

		mysc = lang.newSourceCode(new Coordinates(origin.getX() + width + 120, origin.getY() - 50), "sc", null, myscP);

		mysc.addCodeLine("public static double[] Sort(double[] Unsorted) {", "", 0, null); // line
																							// 00
		mysc.addCodeLine("    int n = Unsorted.length;", "", 1, null);
		mysc.addCodeLine("    double[][] m = new double[(2 * n - 1)][2];", "", 1, null);
		mysc.addCodeLine("    double[] Sorted = new double[n];", "", 1, null);
		mysc.addCodeLine("", "", 1, null);
		mysc.addCodeLine("    for (int i = 1; i <= n; i++) {", "", 1, null); // line
																				// 05
		mysc.addCodeLine("        m[n - 1 + i - 1][0] = Unsorted[i - 1]; //Wert", "", 2, null);
		mysc.addCodeLine("        m[n - 1 + i - 1][1] = n + i - 1; // Position", "", 2, null);
		mysc.addCodeLine("    }", "", 1, null);
		mysc.addCodeLine("    for (int i = n - 1; i >= 1; i--) {", "", 1, null); // line
																					// 09
		mysc.addCodeLine("        m[i - 1][0] = min(m[2 * i - 1], m[2 * i])[0]; //Wert", "", 2, null);
		mysc.addCodeLine("        m[i - 1][1] = min(m[2 * i - 1], m[2 * i])[1]; // Position", "", 2, null);
		mysc.addCodeLine("    }", "", 1, null);
		mysc.addCodeLine("    for (int j = 0; j < n; j++) {", "", 1, null); // line
																			// 13
		mysc.addCodeLine("        Sorted[j] = m[0][0]; // Wert", "", 2, null);
		mysc.addCodeLine("        int i = (int) m[0][1]; // Position", "", 2, null);
		mysc.addCodeLine("        m[i - 1][0] = (double) Integer.MAX_VALUE;", "", 2, null);
		mysc.addCodeLine("        for (i = (int) (i / 2.0); i >= 1; i = (int) (i / 2.0)) {", "", 2, null);
		mysc.addCodeLine("            m[i - 1][0] = min(m[2 * i - 1], m[2 * i])[0]; //Wert", "", 3, null); // line
																											// 18
		mysc.addCodeLine("            m[i - 1][1] = min(m[2 * i - 1], m[2 * i])[1]; // Position", "", 3, null);
		mysc.addCodeLine("        }", "", 2, null);
		mysc.addCodeLine("    }", "", 1, null);
		mysc.addCodeLine("    return Sorted;", "", 1, null); // line 22
		mysc.addCodeLine("}", "", 0, null);
	}

	public String getName() {
		return "TreeSort";
	}

	public String getAlgorithmName() {
		return "TreeSort";
	}

	public String getAnimationAuthor() {
		return "Patrick Weber";
	}

	public String getDescription() {
		return "Der hier umgesetze TreeSort-Algorithmus legt zunächst ein Array an, das doppelt so lang wie das" + "\n"
				+ "zu sortierende ist. Das unsortierte Array wird dann in der zweiten Hälfte des neu angelegten Array"
				+ "\n" + "gespeichert." + "\n"
				+ "Nun kann aus diesem Array ein binärer Baum konstruiert werden, das Array entspricht " + "\n"
				+ "dabei einer Level-by-Level-Traversierung des Baumes. Das hat den Effekt, dass das unsortierte "
				+ "\n" + "Array nun die Blätter des Baumes bildet. " + "\n"
				+ "Es wird nun in jedem Iterationsschritt das Minimum des aktuellen Baumes ermittelt, extrahiert \n"
				+ "und dann im Baum gelöscht um das nächste Minimum finden zu können. Nacheinander wird damit das \n"
				+ "sortierte Array als Sequenz der Minima konstruiert. \n"
				+ "Der Sortieralgorithmus wurde 1962 von Robert Floyd eingeführt und ist Vorgänger von Heapsort.";
	}

	public String getCodeExample() {
		return "public static double[] Sortbackup(double[] Unsorted) {" + "\n" + "	int n = Unsorted.length;" + "\n"
				+ "\n" + "	double[][] m = new double[(2 * n - 1)][2]; // for calc" + "\n"
				+ "	double[] Sorted = new double[n]; // for result" + "\n" + "\n" + "	for (int i = 1; i <= n; i++) {"
				+ "\n" + "		m[n - 1 + i - 1][0] = Unsorted[i - 1]; // Wert" + "\n"
				+ "		m[n - 1 + i - 1][1] = n + i - 1; // Position" + "\n" + "	}" + "\n"
				+ "	for (int i = n - 1; i >= 1; i--) {" + "\n"
				+ "		m[i - 1][0] = min(m[2 * i - 1], m[2 * i])[0]; // Wert" + "\n"
				+ "		m[i - 1][1] = min(m[2 * i - 1], m[2 * i])[1]; // Position" + "\n" + "	}" + "\n"
				+ "	for (int j = 0; j < n; j++) {" + "\n" + "		Sorted[j] = m[0][0]; // Wert" + "\n"
				+ "		int i = (int) m[0][1]; // Position" + "\n" + "		m[i - 1][0] = (double) Integer.MAX_VALUE;"
				+ "\n" + "		for (i = (int) (i / 2.0); i >= 1; i = (int) (i / 2.0)) {" + "\n"
				+ "			m[i - 1][0] = min(m[2 * i - 1], m[2 * i])[0]; // Wert" + "\n"
				+ "			m[i - 1][1] = min(m[2 * i - 1], m[2 * i])[1]; // Position" + "\n" + "		}" + "\n"
				+ "	}" + "\n" + "	return Sorted;" + "\n" + "}";
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

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		// Prüfen der Array-Eingaben in Zwischenspeicher

		try {
			Zwischenspeicher = (String[]) primitives.get("Zwischenspeicher");
			for (int i = 0; i < Zwischenspeicher.length; i++) {
				Double.parseDouble(Zwischenspeicher[i]);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}