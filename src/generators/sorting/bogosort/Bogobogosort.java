package generators.sorting.bogosort;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.items.BooleanPropertyItem;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;

public class Bogobogosort implements Generator {
	private final String name = "Bogobogosort";

	/*********** GENERAL *****************/
	private Language lang;
	private int[] rawList;
	private List<Integer> list;
	private int maxIterations, curArray = 0, maxValue = Integer.MIN_VALUE;

	/*********** Visual Elements *********/
	private Text title;
	private TextProperties titleProperties;

	private TextBlock introductionText, discussionText;
	private TextProperties textProperties;

	private SourceCode scMain, scIsSorted;
	private SourceCodeProperties sourceCodeProperties;

	private LinkedList<BarArray> barArrays;
	private RectProperties barArrayRectProperties;

	private InfoBox infoBox;
	private InfoItem infoReads, infoWrites, infoCopies, infoRecursion, infoSorts, infoStep;

	private Color successColor, failureColor, highlightColor;

	/*********** POSITIONING *************/
	/** Standard margin between elements */
	private final int margin = 40;
	private Node titlePosition, topLeftContentPosition;

	@Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		readPrimitives(primitives);
		readProperties(props);

		resetStatistics();
		placeObjects();

		showIntroduction();
		lang.nextStep("Einführung");
		showBogobogosort();
		lang.nextStep("Der Algorithmus");
		bogobogoSort(list);

		showDiscussion();
		lang.nextStep("Diskussion");

		return lang.toString();
	}

	private void resetStatistics() {
		curArray = 0;
		maxValue = Integer.MIN_VALUE;
	}

	private void showIntroduction() {
		hideContent();
		introductionText.show();
	}

	private void showDiscussion() {
		hideContent();
		discussionText.show();
	}

	private void showBogobogosort() {
		hideContent();
		scMain.show();
		scIsSorted.show();
		this.infoBox.show();
		this.barArrays.getFirst().show();
		list = new ArrayList<Integer>(rawList.length);
		for (int i : rawList) {
			list.add(i);
		}
	}

	private void hideContent() {
		boolean hideTitle = (Boolean) ((BooleanPropertyItem) title.getProperties().getItem("hidden")).get();
		this.lang.hideAllPrimitives();
		if (!hideTitle)
			this.title.show();
	}

	public void bogobogoSort(List<Integer> list) {
		animateEnterSort(list);
		infoSorts.increase();
		boolean isSorted = false;
		while (incStepUntilMaxStepsReached() && animateEnterWhileIsSorted(infoRecursion.getValue() == 0)
				&& !(isSorted = bogobogoIsSorted(list)) && !maxStepsReached()) {
			bogobogoShuffle(list, false);
			animateShuffle();
		}
		animateLeaveSort(list, isSorted);

		// Show statistics at the end
		if (infoRecursion.getValue() == 0) {
			this.hideContent();
			TextBlock end = new TextBlock(lang, topLeftContentPosition, textProperties);
			end.setColor(isSorted ? successColor : failureColor);
			String text = "Der Algorithmus wurde nach " + infoStep.getValue() + " Versuchen "
					+ (isSorted ? "beendet." : "abgebrochen.") + "\n" + "Insgesamt wurden " + infoWrites.getValue()
					+ " Schreibzugriffe, " + infoReads.getValue() + " Lesezugriffe und " + infoCopies.getValue()
					+ " Kopiervorgänge ausgeführt.";
			end.addText(text);
			lang.nextStep("Ergebnis");
		}
	}

	private boolean incStepUntilMaxStepsReached() {
		if (infoRecursion.getValue() > 0)
			return true;
		if (infoStep.getValue() >= maxIterations)
			return false;
		infoStep.increase();
		return true;
	}

	public boolean bogobogoIsSorted(List<Integer> list) {
		animateEnterIsSorted();
		if (animateCheckListLength(list.size() <= 1))
			return true;
		List<Integer> copy = bogobogoCopyArray(list);
		infoCopies.increase();
		curArray++;
		animateCopyArray();
		do {
			animateEnterDoWhile();
			animateSortSublist();
			infoRecursion.increase();
			bogobogoSort(copy.subList(0, copy.size() - 1));
			infoRecursion.decrease();
			int prelast = copy.get(copy.size() - 2);
			int last = copy.get(copy.size() - 1);
			infoReads.increaseBy(2);
			if (animateIfLastIsGreatest(prelast <= last))
				break;
			animateStartShuffleCopy();
			bogobogoShuffle(copy, true);
			animateEndShuffleCopy();
		} while (animateLeaveDoWhile(!maxStepsReached()));
		boolean equal = true;
		for (int i = 0; i < copy.size(); i++) {
			infoReads.increaseBy(2);
			if (copy.get(i) != list.get(i)) {
				equal = false;
				break;
			}
		}
		animateReturnEquals(equal);
		curArray--;
		return equal;
	}

	private List<Integer> bogobogoCopyArray(List<Integer> list) {
		// add copies
		Node pos = new Offset(0, 0, this.barArrays.get(curArray).getBoundingBox(), AnimalScript.DIRECTION_NW);
		int[] t = new int[list.size()];
		for (int i = 0; i < list.size(); i++)
			t[i] = list.get(i);
		BarArray ba = new BarArray(lang, barArrayRectProperties, t);
		ba.placeGivenMax(pos, t.length * 10, margin * 2, this.maxValue);
		ba.setHighlightColor(highlightColor);
		ba.hide();
		// overwrite old copies
		if (barArrays.size() > curArray + 1)
			barArrays.set(curArray + 1, ba);
		else
			barArrays.add(ba);
		return new ArrayList<>(list);
	}

	private void bogobogoShuffle(List<Integer> list, boolean copy) {
		Random rnd = new Random();
		int rndPick, tmp;
		// Shuffle with Fisher-Yates Algorithm
		for (int i = list.size() - 1; i > 0; i--) {
			// Pick random Element from the rest of the list
			rndPick = rnd.nextInt(i);

			infoReads.increaseBy(2);
			infoWrites.increaseBy(2);
			tmp = list.get(i);
			list.set(i, list.get(rndPick));
			list.set(rndPick, tmp);
			// swap in visualization
			if (maxStepsReached())
				return;
			BarArray barArray = this.barArrays.get(curArray);
			barArray.unhighlight();
			barArray.swap(i, rndPick, new MsTiming(i * 200), new MsTiming(200));
		}
	}

	private boolean animateCheckListLength(boolean b) {
		this.scIsSorted.highlight("checkListLength");
		nextAlgoStep();
		if (b) {
			this.scIsSorted.highlight("checkListLengthReturn");
			nextAlgoStep();
			this.scIsSorted.unhighlight("checkListLengthReturn");
		}
		this.scIsSorted.unhighlight("checkListLength");
		return b;
	}

	private void animateEnterIsSorted() {
		this.scIsSorted.highlight("enterIsSorted");
		nextAlgoStep();
		this.scIsSorted.unhighlight("enterIsSorted");
	}

	private void animateCopyArray() {
		this.scIsSorted.highlight("copyArray");
		BarArray cpy = barArrays.get(curArray);
		cpy.show();
		int dx = this.barArrays.get(curArray - 1).getWidth() + margin;
		cpy.moveBy(dx, 0, null, new MsTiming(200));
		nextAlgoStep();
		this.scIsSorted.unhighlight("copyArray");
	}

	private void animateEnterDoWhile() {
		this.scIsSorted.highlight("enterDoWhile");
		nextAlgoStep();
		this.scIsSorted.unhighlight("enterDoWhile");
	}

	private void animateSortSublist() {
		this.scIsSorted.highlight("sortSublist");
		nextAlgoStep();
		this.scIsSorted.unhighlight("sortSublist");
	}

	private boolean animateIfLastIsGreatest(boolean b) {
		this.scIsSorted.highlight("ifLastIsGreatest");
		nextAlgoStep();
		if (b) {
			this.scIsSorted.highlight("breakDoWhile");
			this.barArrays.get(curArray).highlight(0, rawList.length - curArray, successColor);
			nextAlgoStep();
			this.scIsSorted.unhighlight("breakDoWhile");
		}
		this.scIsSorted.unhighlight("ifLastIsGreatest");
		return b;
	}

	private void animateStartShuffleCopy() {
		this.scIsSorted.highlight("shuffleCopy");
	}

	private void animateEndShuffleCopy() {
		nextAlgoStep();
		this.scIsSorted.unhighlight("shuffleCopy");
	}

	private boolean animateLeaveDoWhile(boolean b) {
		this.scIsSorted.highlight("leaveDoWhile");
		nextAlgoStep();
		this.scIsSorted.unhighlight("leaveDoWhile");
		return b;
	}

	private void animateReturnEquals(boolean equals) {
		this.scIsSorted.highlight("returnEquals");
		if (equals) {
			this.barArrays.get(curArray).highlight(0, rawList.length - curArray, successColor);
		} else
			this.barArrays.get(curArray).highlight(0, rawList.length - curArray, failureColor);
		nextAlgoStep();
		this.barArrays.get(curArray).unhighlight();
		this.barArrays.get(curArray).hide();
		this.barArrays.remove(curArray);
		this.scIsSorted.unhighlight("returnEquals");
	}

	private boolean maxStepsReached() {
		return infoStep.getValue() > this.maxIterations;
	}

	private void animateEnterSort(List<Integer> list) {
		scMain.highlight("enterSort");
		this.barArrays.get(curArray).highlight(0, rawList.length - infoRecursion.getValue() - 1);
		nextAlgoStep();
		scMain.unhighlight("enterSort");
	}

	private boolean animateEnterWhileIsSorted(boolean createIterationLabel) {
		this.scMain.highlight("enterWhileIsSorted");
		if (!maxStepsReached())
			if (createIterationLabel)
				lang.nextStep("Iteration " + infoStep.getValue());
			else
				lang.nextStep();
		this.scMain.unhighlight("enterWhileIsSorted");
		return true;
	}

	private void animateLeaveSort(List<Integer> list, boolean success) {
		scMain.highlight("leaveSort");
		this.barArrays.get(curArray).highlight(0, rawList.length - infoRecursion.getValue() - 1,
				success ? successColor : failureColor);
		nextAlgoStep();
		scMain.unhighlight("leaveSort");
	}

	private void animateShuffle() {
		scMain.highlight("shuffle");
		int delayMS = (this.barArrays.get(curArray).getLength() - 1) * 200;
		this.barArrays.get(curArray).highlight(0, rawList.length - curArray - 1, highlightColor, new MsTiming(delayMS),
				null);
		nextAlgoStep();
		scMain.unhighlight("shuffle");
	}

	private void nextAlgoStep() {
		if (!maxStepsReached())
			lang.nextStep();
	}

	private void placeSourceCode() {
		scMain = lang.newSourceCode(new Offset(0, margin, barArrays.getFirst().getBoundingBox(),
				AnimalScript.DIRECTION_SW), "sourceCode", null, sourceCodeProperties);
		scMain.addCodeLine("public void sort(List<Integer> list) {", "enterSort", 0, null);
		scMain.addCodeLine("while (!isSorted(list))", "enterWhileIsSorted", 1, null);
		scMain.addCodeLine("Collections.shuffle(list);", "shuffle", 2, null);
		scMain.addCodeLine("}", "leaveSort", 0, null);

		scIsSorted = lang.newSourceCode(new Offset(0, margin, scMain, AnimalScript.DIRECTION_SW), name, null,
				sourceCodeProperties);
		scIsSorted.addCodeLine("public boolean isSorted(List<Integer> list) {", "enterIsSorted", 0, null);
		scIsSorted.addCodeLine("if(list.size() <= 1)", "checkListLength", 1, null);
		scIsSorted.addCodeLine("return true;", "checkListLengthReturn", 2, null);
		scIsSorted.addCodeLine("List<Integer> copy = new ArrayList<>(list);", "copyArray", 1, null);
		scIsSorted.addCodeLine("do {", "enterDoWhile", 1, null);
		scIsSorted.addCodeLine("sort(copy.subList(0, copy.size() - 1));", "sortSublist", 2, null);
		scIsSorted.addCodeLine("if (copy.get(copy.size() -2) <= copy.get(copy.size()-1))", "ifLastIsGreatest", 2, null);
		scIsSorted.addCodeLine("break;", "breakDoWhile", 3, null);
		scIsSorted.addCodeLine("Collections.shuffle(copy);", "shuffleCopy", 2, null);
		scIsSorted.addCodeLine("} " + "while (true);", "leaveDoWhile", 1, null);
		scIsSorted.addCodeLine("return copy.equals(list);", "returnEquals", 1, null);
		scIsSorted.addCodeLine("}", "leaveIsSorted", 0, null);
	}

	/********************************* INITIALIZATION *************************************/
	@Override
	public void init() {
		lang = new AnimalScript(getName(), getAnimationAuthor(), 600, 400);
		lang.setStepMode(true);
	}

	private void readPrimitives(Hashtable<String, Object> primitives) {
		rawList = (int[]) primitives.get("Zahlenliste");
		maxIterations = (Integer) primitives.get("Maximale Versuche");
		successColor = (Color) primitives.get("Farbe bei Erfolg");
		failureColor = (Color) primitives.get("Farbe bei Misserfolg");
		highlightColor = (Color) primitives.get("Farbe für aktuelle Schritte");

	}

	private void readProperties(AnimationPropertiesContainer props) {
		titleProperties = (TextProperties) props.getPropertiesByName("Titel");
		/* Read user defined font and use it to create bold and large fonts */
		Font userFont = (Font) titleProperties.get(AnimationPropertiesKeys.FONT_PROPERTY);
		titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(userFont.getFamily(), Font.BOLD, 32));
		textProperties = (TextProperties) props.getPropertiesByName("Normaler Text");
		barArrayRectProperties = (RectProperties) props.getPropertiesByName("Balken Elemente");
		sourceCodeProperties = (SourceCodeProperties) props.getPropertiesByName("Quelltext");
	}

	/********************************** DRAWING ********************************************/

	private void placeObjects() {
		placeTitle();
		placeIntroduction();
		placeBarArrays();
		placeSourceCode();
		placeStatistics();
		placeDiscussion();
	}

	private void placeBarArrays() {
		barArrays = new LinkedList<BarArray>();
		BarArray ba;
		Node curPos = new Offset(0, margin, this.title, AnimalScript.DIRECTION_SW);

		for (int i : rawList) {
			if (i >= this.maxValue)
				this.maxValue = i;
		}

		ba = new BarArray(lang, barArrayRectProperties, rawList);
		ba.placeGivenMax(curPos, rawList.length * 10, margin * 2, this.maxValue);
		ba.setHighlightColor(highlightColor);
		barArrays.add(ba);
	}

	private void placeStatistics() {
		this.infoBox = new InfoBox(lang, textProperties);
		infoStep = this.infoBox.addInfo("Versuch Nr.", 0);
		infoSorts = this.infoBox.addInfo("Sortieraufrufe");
		infoRecursion = this.infoBox.addInfo("Rekursionstiefe");
		infoReads = this.infoBox.addInfo("Lesezugriffe");
		infoWrites = this.infoBox.addInfo("Schreibzugriffe");
		infoCopies = this.infoBox.addInfo("Kopiervorgänge");
		this.infoBox.place(new Offset(margin * 2, 0, scMain, AnimalScript.DIRECTION_NE));
	}

	private void placeIntroduction() {
		introductionText = new TextBlock(lang, topLeftContentPosition, textProperties);
		introductionText.hide();
		introductionText.addText(getDescription());
	}

	private void placeDiscussion() {
		discussionText = new TextBlock(lang, topLeftContentPosition, textProperties);
		discussionText.hide();
		discussionText.addText(getDiscussion());
	}

	private void placeTitle() {
		titlePosition = new Coordinates(margin, margin);
		title = lang.newText(titlePosition, this.name, "title", null, titleProperties);
		boolean isTitleHidden = (boolean) titleProperties.get("hidden");
		topLeftContentPosition = new Offset(0, isTitleHidden ? margin : 2 * margin, title, AnimalScript.DIRECTION_SW);
	}

	/********************************* GETTER *********************************************/
	@Override
	public String getAlgorithmName() {
		return this.name;
	}

	@Override
	public String getAnimationAuthor() {
		return "Julian Klomp,Milan Schmittner";
	}

	@Override
	public String getCodeExample() {
		return "public void bogobogoSort(List&lt;Integer&gt; list) {" + "<br>" + "&nbsp;while(!isSorted(list))"
				+ "<br>" + "&nbsp;&nbsp;Collections.shuffle(list);" + "<br>" + "&nbsp;}" + "<br>" + "" + "<br>"
				+ "public boolean isSorted(List&lt;Integer&gt; list) {" + "<br>"
				+ "&nbsp;List&lt;Integer&gt; copy = new ArrayList&lt;&gt;(list);" + "<br>" + "&nbsp;do {" + "<br>"
				+ "&nbsp;&nbsp;bogobogoSort(copy.subList(0, copy.size()-1));" + "<br>"
				+ "&nbsp;&nbsp;if(Collections.max(copy.subList(0, copy.size()-1)) == copy.get(copy.size())) " + "<br>"
				+ "&nbsp;&nbsp;&nbsp;break;" + "<br>" + "&nbsp;&nbsp;Collections.shuffle(copy);" + "<br>"
				+ "&nbsp;} while(true);" + "<br>" + "&nbsp;return copy.equals(list);" + "<br>" + "}";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	private String getDiscussion() {
		return "Bei der Analyse von Bogobogosort gibt es noch keine eindeutige Aussage zur Laufzeit.\n"
				+ "Mike Rosulek, Professor an der Oregon State University, berechnete die Laufzeit ohne die Betrachtung von Bogosort \n"
				+ "sondern an Hand der Laufzeiten zum Sortieren und Überprüfen der Liste.\n"
				+ "Er kommt auf eine Laufzeit von  O(n*(n!)^n).\n"
				+ "\n"
				+ "Nathan Collins, ein freiberuflicher Wissenschaftsautor, stellte hingegen eine Rechnung basierend auf der Analyse von Bogosort auf.\n"
				+ "Sein Ergebnis O(n!^(n-k)), wobei k eine beliebige Konstante ist, liefert eine bessere Laufzeit als vorherige Analysen. \n"
				+ "\n"
				+ "Beide Rechnungen zeigen die mit größer werdenden Listen extreme Laufzeit von Bogobogosort auf, \n"
				+ "was den Algorithmus untauglich für einen Praxiseinsatz macht.";
	}

	@Override
	public String getDescription() {
		return "Bogobogosort basiert auf dem indeterministischen Sortieralgorithmus Bogosort. "
				+ "\n"
				+ "Das Grundprinzip ist gleich (die zu sortierende Liste wird so lange geschmischt, bis "
				+ "\n"
				+ "sie zufälligerweise sortiert ist), allerdings wird hier zusätzlich die Überprüfung, "
				+ "\n"
				+ "ob die Liste sortiert ist, möglichst ineffizient gestaltet."
				+ "\n"
				+ "Der rekursive Ansatz zum überprüfen der Liste ist folgender:"
				+ "\n"
				+ "1. Erstelle eine Kopie der übergebenen Liste"
				+ "\n"
				+ "2. Sortiere die ersten n-1 Elemente der Liste mit Bogobogosort, wobei n die Länge der Liste ist."
				+ "\n"
				+ "3. Überprüfe ob das letzte (n-te) Element größer als alle vorherigen ist. Wenn dies nicht der Fall ist, "
				+ "\n"
				+ "   mische die Kopie und gehe zurück zu Schritt 2."
				+ "\n"
				+ "4. Überprüfe ob die Kopie in der selben Reihenfolge wie das Original ist."
				+ "\n"
				+ "Durch diese ineffiziente Überprüfung steigt der Aufwand und die Laufzeit gegenüber Bogosort gewaltig.";
	}

	@Override
	public String getFileExtension() {
		return "asu";
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

}
