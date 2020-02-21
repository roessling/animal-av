/*
 * flashSortWiz.java
 * Sarah Fischer, Anke Unger, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.sorting;

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
import algoanim.primitives.Rect;
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
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

public class FlashSortWiz implements ValidatingGenerator {
	private Language lang;
	private int[] inputArray;
	private ArrayProperties inputArrayProp;
	private Text header;
	private IntArray anl;
	private IntArray ana;
	private Node txtPos;
	Text schleifedurch;
	Rect rect;
	Text name1;
	Text name2;
	int counterKlasseBer;
	TwoValueCounter counterA;
	int counterPerm;
	int counterIs;
	TwoValueView view1;
	TextProperties introProps;
	SourceCodeProperties srcProp;

	public void init() {
		lang = new AnimalScript("Flash Sort mit gleichverteilten Daten", "Sarah Fischer, Anke Unger", 800, 600);
	}

	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		int[] a = (int[]) primitives.get("inputArray");

		if (a.length > 20)
			return false;

		return true;
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		counterPerm = 0;
		counterKlasseBer = 0;
		counterIs = 0;
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		TextProperties headerProps = (TextProperties) props.getPropertiesByName("headerProp");
		RectProperties recProps = (RectProperties) props.getPropertiesByName("headerRectangleProp");
		srcProp = (SourceCodeProperties) props.getPropertiesByName("sourceCodeProp");
		// TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		header = lang.newText(new Coordinates(20, 50), getName(), "header", null, headerProps);
		rect = lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"),
				"hRect", null, recProps);
		lang.nextStep("Introduction");
		introProps = new TextProperties();
		introProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		Text intro = lang.newText(new Offset(0, 100, header, AnimalScript.DIRECTION_SW),
				"Der Vergleich von Elementen fordert Komplexität. So fordert ein vergleichender Sortieralgorithmus minimal eine Komplexität von n * log(n), meist ist sie aber quadratisch.",
				"Introduction1", null, introProps);
		Text intro1 = lang.newText(new Offset(0, 10, intro, AnimalScript.DIRECTION_SW),
				"Daher wäre ein Sortieralgorithmus optimal, der Elemente gar nicht erst vergleicht, sondern Vorwissen verwendet, um eine Liste zu sortieren.",
				"Introduction2", null, introProps);
		Text intro2 = lang.newText(new Offset(0, 10, intro1, AnimalScript.DIRECTION_SW),
				"Flashsort ist solch ein Algorithmus. Als Vorwissen wird das Wissen über die Verteilung der Daten genutzt.",
				"Introduction3", null, introProps);
		Text intro3 = lang.newText(new Offset(0, 10, intro2, AnimalScript.DIRECTION_SW),
				"Am besten funktioniert das bei gleichverteilten Daten. Das heißt, alle Daten kommen etwa gleich oft vor und es gibt keine Häufungen.",
				"Introduction5", null, introProps);
		Text intro4 = lang.newText(new Offset(0, 10, intro3, AnimalScript.DIRECTION_SW),
				"Stell dir vor, es soll eine Liste sortiert werden, die 10 Elemente von 0 bis 20 enthält.",
				"Introduction6", null, introProps);
		Text intro5 = lang.newText(new Offset(0, 10, intro4, AnimalScript.DIRECTION_SW),
				"Bekommst du jetzt eine 10 gezeigt, ohne die restlichen Elemente zu kennen, wirst du sie logischerweise mittig einsortieren.",
				"Introduction7", null, introProps);
		Text intro6 = lang.newText(new Offset(0, 10, intro5, AnimalScript.DIRECTION_SW),
				"Das ist auch die Grundidee von Flash Sort. Die Liste wird in Bereiche eingeteilt, hier Klassen genannt. Im ersten Bereich sind die n kleinsten Elemente. Im letzten die n größten und so weiter.",
				"Introduction8", null, introProps);
		Text intro7 = lang.newText(new Offset(0, 10, intro6, AnimalScript.DIRECTION_SW),
				"Durch eine Variable m kann die Klassenanzahl festgelegt werden. Je mehr Klassen es gibt, desto kleiner sind diese und der Algorithmus kann genauer vorsortieren. Je größer sie sind, desto schneller arbeitet Flash Sort.",
				"Introduction9", null, introProps);
		Text intro12 = lang.newText(new Offset(0, 10, intro7, AnimalScript.DIRECTION_SW),
				"Wo eine Klasse anfängt und endet, merkt sich Flash Sort durch ein Hilfsarray. Darin liegen die Indizes zum letzten freien Platz einer Klasse. Jedes Mal, wenn ein Element hineingelegt wird, wird dieser Index verkleinert.",
				"Introduction12", null, introProps);
		Text intro13 = lang.newText(new Offset(0, 10, intro12, AnimalScript.DIRECTION_SW),
				"Eine Klasse ist voll, wenn der Zeiger auf das Klassenende der vorherigen Klasse zeigt. Für jedes Element wird die Klasse berechnet. Damit lässt sich der Algorithmus auch auf andere Verteilungen anpassen,",
				"Introduction13", null, introProps);
		Text intro14 = lang.newText(new Offset(0, 10, intro13, AnimalScript.DIRECTION_SW),
				"etwa auf eine Normalverteilung. Hier wird die Formel passend zu einer Gleichverteilung verwendet.",
				"Introduction13teil2", null, introProps);
		Text intro8 = lang.newText(new Offset(0, 10, intro14, AnimalScript.DIRECTION_SW),
				"Nachdem festgestellt wurde, in welche Klasse ein Element gehört, wird es einfach an die nächste freie Stelle in dieser gelegt. Das bedeutet, dass die Elemente innerhalb einer Klasse nicht sortiert sind.",
				"Introduction10", null, introProps);
		Text intro9 = lang.newText(new Offset(0, 10, intro8, AnimalScript.DIRECTION_SW),
				"Dafür läuft ganz zum Schluss ein Insertion Sort Algorithhmus über die Liste und sortiert die Elemente innerhalb der Klasse.",
				"Introduction11", null, introProps);
		Text intro10 = lang.newText(new Offset(0, 10, intro9, AnimalScript.DIRECTION_SW),
				"Insertion Sort ist schneller, je besser die Liste schon vorsortiert ist. Bei einer perfekt sortierten Liste entsteht beispielsweise eine lineare Komplexität.",
				"Introduction12", null, introProps);
		Text intro11 = lang.newText(new Offset(0, 10, intro10, AnimalScript.DIRECTION_SW),
				"Aber bei einer komplett unsortierten Liste ist Insertion Sort mit einer quadratischen Komplexität recht langsam. Deswegen ergänzen sich die beiden Algorithmen so gut.",
				"Introduction13", null, introProps);
		MultipleChoiceQuestionModel verteilung = new MultipleChoiceQuestionModel("verteilung");
		verteilung.setPrompt("Wie sollten die Daten verteilt sein, damit der Algorithmus effektiv arbeitet?");
		verteilung.addAnswer("Normalverteilt", 0,
				"Leider falsch, die Daten sollten gleichverteilt sein, damit sie nach der Einteilung in Klassen vorsortiert sind.");
		verteilung.addAnswer("Binomialverteilt", 0,
				"Leider falsch, die Daten sollten gleichverteilt sein, damit sie nach der Einteilung in Klassen vorsortiert sind.");
		verteilung.addAnswer("Bernoulliverteilt", 0,
				"Leider falsch, die Daten sollten gleichverteilt sein, damit sie nach der Einteilung in Klassen vorsortiert sind.");
		verteilung.addAnswer("Gleichverteilt", 1,
				"Richtig. Die Daten sollten gleichverteilt sein, damit sie nach der Einteilung in Klassen vorsortiert sind.");
		lang.addMCQuestion(verteilung);
		// lang.newText(new Offset(0, 10, "Introduction13",
		// AnimalScript.DIRECTION_SW), "", "Introduction9", null);
		// lang.newText(new Offset(0, 10, "Introduction14",
		// AnimalScript.DIRECTION_SW), "", "Introduction9", null);
		// lang.newText(new Offset(0, 10, intro, AnimalScript.DIRECTION_SW), "",
		// "Introduction10", null);
		lang.nextStep();
		intro.hide();
		intro1.hide();
		intro2.hide();
		intro3.hide();// long therm memory, emotion paper, seine bayes folien,
						// (computational approaches), three levels of
		intro4.hide();
		intro5.hide();
		intro6.hide();
		intro7.hide();
		intro8.hide();
		intro9.hide();
		intro10.hide();
		intro11.hide();
		intro12.hide();
		intro13.hide();
		intro14.hide();
		inputArray = (int[]) primitives.get("inputArray");
		inputArrayProp = (ArrayProperties) props.getPropertiesByName("inputArrayProp");
		inputArrayProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		ana = lang.newIntArray(new Offset(60, 100, header, AnimalScript.DIRECTION_SW), inputArray, "inarray", null,
				inputArrayProp);
		name1 = lang.newText(new Offset(-70, -20, ana, AnimalScript.DIRECTION_SW), "Input Array", "inputArray", null);

		// Zeiger Array
		ArrayProperties ap = new ArrayProperties();
		ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		int n = ana.getLength();
		if (n == 1) {
			lang.newText(new Offset(200, 100, header, AnimalScript.DIRECTION_SW),
					"Da die Liste nur ein Element enthält, ist sie bereits sortiert.", "nureinelem", null, introProps);
			return lang.toString();
		}
		int m = n / 2; // Klassenanzahl
		int[] l = new int[m];
		anl = lang.newIntArray(new Offset(30, 30, ana, AnimalScript.DIRECTION_SW), l, "pointerarray", null, ap);
		name2 = lang.newText(new Offset(-70, -20, anl, AnimalScript.DIRECTION_SW), "Index Array", "indizeArray", null);

		insertionSort(flashSort(ana, anl));
		lang.finalizeGeneration();
		// System.out.println(lang.toString());
		return lang.toString();
	}

	public String getName() {
		return "Flash Sort mit gleichverteilten Daten";
	}

	public String getAlgorithmName() {
		return "Flash Sort";
	}

	public String getAnimationAuthor() {
		return "Sarah Fischer, Anke Unger";
	}

	public String getDescription() {
		return "Flash Sort sortiert eine gleichverteilte Liste, indem zur Verteilung passende Klassen" + "\n"
				+ "erstellt werden. Dadurch können die Elemente vorsortiert werden. Ein anschließender Insertion Sort "
				+ "\n" + "Algorithmus sortiert die Elemente innerhalb der Klasse.  ";
	}

	public String getCodeExample() {
		return "//int m wird vorher je nach gewünschter Klassengröße gewählt" + "\n"
				+ "double c = ((double) m - 1) / (max - min);" + "\n" + "\n" + "for (i = 0; i < n; i++) {" + "\n"
				+ "            class = (int) (c * (inputArray[i] - min));" + "\n" + "            indiceArray[class]++;"
				+ "\n" + "        }" + "\n" + "for (class = 1; class < m; class++){" + "\n"
				+ "            indiceArray[class] += indiceArray[class];" + "\n" + "\n" + "        }" + "\n" + "\n"
				+ "while (nmove < n - 1) {" + "\n" + "            while (j > (indiceArray[class] - 1)) {" + "\n"
				+ "                j++;" + "\n" + "                class = (int) (c * (inputArray[j] - min));" + "\n"
				+ "\n" + "            }" + "\n" + "\n" + "            flash = inputArray[j];" + "\n" + "\n"
				+ "while (!(j == indiceArray[class] +1 )) {" + "\n"
				+ "                class = (int) (c * (flash - min));" + "\n" + "\n"
				+ "                hold = inputArray[indiceArray[class] - 1];" + "\n"
				+ "                inputArray[indiceArray[class]] = flash;" + "\n" + "                flash = hold;"
				+ "\n" + "\n" + "                indiceArray[class]--;" + "\n" + "                nmove++;" + "\n"
				+ "            }";
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

	public IntArray flashSort(IntArray a, IntArray l) {
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		ArrayMarkerProperties pProp = new ArrayMarkerProperties();
		pProp.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
		pProp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
		ArrayMarkerProperties cProp = new ArrayMarkerProperties();
		cProp.set(AnimationPropertiesKeys.LONG_MARKER_PROPERTY, true);
		cProp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "berechnete Klasse");
		// IntegerVariable flash = new IntegerVariable();
		// IntegerPropertyItem intProp = new IntegerPropertyItem();
		srcProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		SourceCode src = lang.newSourceCode(new Offset(100, -100, a, AnimalScript.DIRECTION_NE), "sourceCode", null,
				srcProp);
		src.addCodeLine("double c = ((double) m - 1) / (max - min);", "0", 0, null);
		src.addCodeLine("for (i = 0; i < n; i++) {", "1", 0, null);
		src.addCodeLine("            class = (int) (c * (inputArray[i] - min));", "2", 0, null);
		src.addCodeLine("            indiceArray[class]++;", "3", 0, null);
		src.addCodeLine("        }", "5", 0, null);
		src.addCodeLine("for (class = 1; class < m; class++){", "6", 0, null);
		src.addCodeLine("            indiceArray[class] += indiceArray[class - 1];", "7", 0, null);
		src.addCodeLine("        }", "9", 0, null);
		Text klassenaufbau = lang.newText(new Offset(10, 30, src, AnimalScript.DIRECTION_SW),
				"Zuerst wird für jede Klassen gezählt, wie viele Elemente hinzugefügt werden müssen.", "klassenaufbau",
				null, textProps);
		Text klassenaufbauzwei = lang.newText(new Offset(0, 10, klassenaufbau, AnimalScript.DIRECTION_SW),
				"Dafür wird mit einer Formel die jeweils zugehörige Klasse berechnet.", "klassenaufbauzwei", null,
				textProps);
		lang.nextStep();
		int min = a.getData(0);
		int maxi = 0;
		int n = a.getLength();
		int m = n / 2; // Klassenanzahl
		for (int i = 1; i < n; i++) {// min und max finden
			counterKlasseBer++;
			if (a.getData(i) < min) {
				min = a.getData(i);
			}
			if (a.getData(i) > a.getData(maxi))
				maxi = i;
		}

		if (min == a.getData(maxi)) {
			klassenaufbau.hide();
			klassenaufbauzwei.hide();
			Text nurEinElem = lang.newText(new Offset(0, 10, klassenaufbau, AnimalScript.DIRECTION_SW),
					"Da die Liste nur ein Element enthält, ist sie bereits sortiert.", "einElem", null, textProps);
			return a; // Liste enth�lt nur ein elem
		}
		double c = (double) (m - 1) / (a.getData(maxi) - min); // anzahl
																// elemente in
																// Klasse
																// konstanter
		counterA = lang.newCounter(a);
		CounterProperties cp1 = new CounterProperties();
		cp1.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		cp1.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
		view1 = lang.newCounterView(counterA, new Offset(40, 20, src, AnimalScript.DIRECTION_E), cp1, false, true); // teil
		src.highlight(1);
		lang.nextStep();
		int u = 0;
		for (int i = 0; i < n; i++) {

			src.unhighlight(3);
			src.highlight(2);
			a.setHighlightFillColor(i, Color.orange, null, null);
			a.highlightCell(i, null, null);
			l.unhighlightCell(u, null, null);
			counterKlasseBer++;
			u = (int) (c * (a.getData(i) - min));
			lang.nextStep();
			src.unhighlight(2);
			src.highlight(3);
			a.unhighlightCell(i, null, null);
			l.put(u, (l.getData(u) + 1), null, null);// elementeanzahl in Klasse
			l.setHighlightFillColor(u, Color.orange, null, null);
			l.highlightCell(u, null, null);
			lang.nextStep(); // k hochz�hlen
		}
		l.unhighlightCell(u, null, null);
		src.unhighlight(1);
		src.unhighlight(2);
		src.unhighlight(3);
		klassenaufbau.hide();
		klassenaufbauzwei.hide();
		MultipleChoiceQuestionModel gleiDa = new MultipleChoiceQuestionModel("gleida");
		gleiDa.setPrompt("Funkioniert Flash Sort nur auf gleichverteilten Daten?");
		gleiDa.addAnswer("Ja", 0,
				"Leider falsch, um auf anderen Verteilungen arbeiten zu können, muss nur die Klassenberechnung angepasst werden.");
		gleiDa.addAnswer("Nein", 1,
				"Richtig. Um auf anderen Verteilungen arbeiten zu können, muss nur die Klassenberechnung angepasst werden.");
		lang.addMCQuestion(gleiDa);
		Text pointersetzen = lang.newText(new Offset(10, 30, src, AnimalScript.DIRECTION_SW),
				"Nun werden die Werte von links nach rechts aufaddiert und um eins verschoben.",
				"pointersetzen", null, textProps);
		Text pointersetzen2 = lang.newText(new Offset(0, 10, pointersetzen, AnimalScript.DIRECTION_SW),
				"Damit stellen sie die Indizes des oberen Randes einer Klasse dar.",
				"pointersetzen2", null, textProps);
		lang.nextStep();
		src.highlight(5);
		l.put(0, (l.getData(0) - 1), null, null);

		for (int w = 1; w < m; w++) {
			src.highlight(6);
			l.put(w, (l.getData(w) + l.getData(w - 1)), null, null);// pointer
			lang.nextStep();
			src.unhighlight(6);// zu
								// Klassenende
			lang.nextStep();
		}
		src.unhighlight(5);
		src.unhighlight(6);
		// a.swap(maxi, 0, null, null);
		// Klassen bunt machen
		Random r = new Random();
		Float random = (float) 0;
		Float prevR = (float) 0;
		boolean farbAbstand = true;
		for (int i = 0; i < l.getLength(); i++) {
			farbAbstand = true;
			while (farbAbstand) {
				random = r.nextFloat();
				if ((prevR == 0 || random <= prevR - 0.2 || random >= prevR + 0.2) && random >= 0) {
					farbAbstand = false;
					prevR = random;
				}
			}
			Color color = Color.getHSBColor(random, (float) 0.8, (float) 0.9);
			l.setHighlightFillColor(i, color, null, null);
			l.highlightCell(i, null, null);
			if (i == 0) {
				a.setHighlightFillColor(0, l.getData(i), color, null, null);
				a.highlightCell(0, l.getData(i), null, null);
			} else if (l.getData(i - 1) == l.getData(i)) {
				a.setHighlightFillColor(l.getData(i), color, null, null);
			} else {
				a.setHighlightFillColor(l.getData(i - 1) + 1, l.getData(i), color, null, null);
				a.highlightCell(l.getData(i - 1) + 1, l.getData(i), null, null);
			}
		}
		Text klasseBunt = lang.newText(new Offset(10, 20, src, AnimalScript.DIRECTION_SW),
				"Jede Klasse ist in einer anderen Farbe markiert und kann befüllt werden.", "klasseBunt", null,
				textProps);
		Text klasseBunt2 = lang.newText(new Offset(0, 10, klasseBunt, AnimalScript.DIRECTION_SW),
				" Hier erkennt man gut, dass die Indizes im Index Array immer auf das letzte Element einer Klasse zeigen.",
				"klasseBunt2", null, textProps);
		Text klasseBunt3 = lang.newText(new Offset(0, 10, klasseBunt2, AnimalScript.DIRECTION_SW),
				" Als Flash wird das erste Element im Array gewählt.", "klasseBunt2", null, textProps);

		pointersetzen.hide();
		pointersetzen2.hide();
		int move = 0;
		int j = 0;
		int k = m - 1;
		int flash = a.getData(0);
		lang.nextStep();
		klasseBunt.hide();
		klasseBunt2.hide();
		klasseBunt3.hide();
		src.addCodeLine("while (nmove < n - 1) {", "10", 0, null);
		src.addCodeLine("            while (j > (indiceArray[class])) {", "11", 0, null);
		src.addCodeLine("                j++;", "12", 0, null);
		src.addCodeLine("                class = (int) (c * (inputArray[j] - min));", "13", 0, null);
		src.addCodeLine("            }", "15", 0, null);
		src.addCodeLine("            flash = inputArray[j];", "16", 0, null);
		src.addCodeLine("            while (!(j == indiceArray[class]+1)) {", "17", 0, null);
		src.addCodeLine("                class = (int) (c * (flash - min));", "18", 0, null);
		src.addCodeLine("                hold = inputArray[indiceArray[class] - 1];", "19", 0, null);
		src.addCodeLine("                inputArray[indiceArray[class] - 1] = flash;", "20", 0, null);
		src.addCodeLine("                flash = hold;", "21", 0, null);
		src.addCodeLine("                indiceArray[class]--;", "22", 0, null);
		src.addCodeLine("                nmove++;", "23", 0, null);
		src.addCodeLine("            }", "24", 0, null);
		src.addCodeLine("}", "25", 0, null);

		src.highlight("10");

		// ArrayMarker p = lang.newArrayMarker(a, -1, "Flash Index", null,
		// pProp);
		ArrayMarker p2 = lang.newArrayMarker(a, -1, "current", null, cProp);
		p2.hide();
		ArrayMarker pj = lang.newArrayMarker(a, -1, "j", null, pProp);
		pj.hide();
		Text fl = lang.newText(new Offset(5, 300, header, AnimalScript.DIRECTION_NW), "Flash: " + flash, "flash", null,
				textProps);
		Text holdText = lang.newText(new Offset(5, 330, header, AnimalScript.DIRECTION_NW), "", "hold", null,
				textProps);
		holdText.hide();
		boolean bool = false;
		while (move < n - 1) {
			lang.nextStep();
			src.highlight("11");
			klasseBunt.hide();
			klasseBunt2.hide();
			klasseBunt3.hide();
			Text leereKlasse = lang.newText(new Offset(10, 30, src, AnimalScript.DIRECTION_SW),
					"Gesucht wird eine Klasse, die noch nicht voll ist. Elemente, die bereits richtig liegen, werden übersprungen.",
					"leereKlasse", null, textProps);
			if (j > (l.getData(k))) {
				bool = true;
				pj.show();
				fl.hide();
			}
			while (j > (l.getData(k))) { // klasse suchen die noch nicht voll
				src.highlight("12");
				j++;
				pj.move(j, null, null);
				lang.nextStep();
				src.unhighlight("12");
				src.highlight("13");
				counterPerm++;
				k = (int) (c * (a.getData(j) - min));
				lang.nextStep();
				src.unhighlight("13");
			}
			pj.hide();
			src.unhighlight(13);
			src.unhighlight(12);
			src.unhighlight("11");
			leereKlasse.hide();
			src.highlight("16");
			flash = a.getData(j);
			fl.setText("Flash: " + flash, null, null);
			if (bool) {
				fl.show();
			}
			MultipleChoiceQuestionModel dreischr = new MultipleChoiceQuestionModel("dreischr");
			dreischr.setPrompt("Was sind die 3 Schritte bis die Liste sortiert ist?");
			dreischr.addAnswer("Liste teilen, Teillisten sortieren, Listen zusammenfügen", 0,
					"Leider falsch, das sind die Schritte für Merge Sort.");
			dreischr.addAnswer(
					"Daten zufällig mischen, prüfen ob Daten sortiert sind, Schritt 1 und 2 beliebig oft wiederholen ",
					0, "Leider falsch, das sind die Schritte für Bogosort");
			dreischr.addAnswer("Klassen bilden, permutieren, Klassen sortieren", 1,
					"Richtig. Nun kommen wir zum zweiten Schritt: Permutieren.");
			lang.addMCQuestion(dreischr);
			lang.nextStep();

			src.unhighlight("16");
			src.highlight("17");
			klasseBunt.hide();
			klasseBunt2.hide();
			Text permutierenAnfang = lang.newText(new Offset(10, 30, src, AnimalScript.DIRECTION_SW),
					"Die Elemente werden permutiert, bis die Klasse j voll ist.", "permutierenAnfang", null, textProps);
			txtPos = permutierenAnfang.getUpperLeft();
			lang.nextStep();
			while (!(j == l.getData(k) + 1)) { // elemente permutieren bis
				// Anfangsklasse ( j) voll
				permutierenAnfang.hide();
				src.highlight("18");
				src.highlight("19");
				src.highlight("20");
				Text klasseberechnen = lang.newText(new Offset(10, 30, src, AnimalScript.DIRECTION_SW),
						"Die Klasse für Flash wird berechnet und Flash wird an die nächste freie Stelle in dieser gelegt.",
						"klasseberechnen", null, textProps);
				k = (int) (c * (flash - min));// Klasse f�r Flash berechnen
				counterPerm++;
				int hold = a.getData(l.getData(k)); // Element an der Zielstelle
				holdText.show();
				holdText.setText("hold:" + hold, null, null); // speichern
				p2.show();
				p2.move(l.getData(k), null, null);
				lang.nextStep();
				a.put(l.getData(k), flash, null, null);
				a.setHighlightFillColor(l.getData(k), Color.GRAY, null, null);
				a.highlightCell(l.getData(k), null, null);
				// p.move(l.getData(k), null, null);
				lang.nextStep();
				klasseberechnen.hide();
				src.unhighlight("18");
				src.unhighlight("19");
				src.unhighlight("20");
				src.highlight("21");
				Text neuFlash = lang.newText(new Offset(10, 30, src, AnimalScript.DIRECTION_SW),
						"Mit dem ersetzten Element wird nun weiter permutiert.", "neuFlash", null, textProps);
				p2.hide();
				flash = hold; // neu permutieren mit hold
				fl.setText("Flash: " + flash, null, null);
				if (bool) {
					fl.show();
					bool = false;
				}
				holdText.hide();
				lang.nextStep();
				// p.hide();
				neuFlash.hide();
				src.unhighlight("21");
				src.highlight("22");
				Text pointerdec = lang.newText(new Offset(10, 30, src, AnimalScript.DIRECTION_SW),
						"Da sich die Klasse gefüllt hat, wird der Klassenpointer heruntergezählt.", "pointerdec", null,
						textProps);
				l.put(k, l.getData(k) - 1, null, null); // Klassenanzahl runter
				// pointerList.get(k).decrement(null, null); // z�hlen
				lang.nextStep();
				pointerdec.hide();
				src.unhighlight("22");
				src.highlight("23");
				move++;
				lang.nextStep();
				src.unhighlight("23");

			}
			src.unhighlight("17");
		}
		src.unhighlight("10");
		schleifedurch = lang.newText(new Offset(10, 30, src, AnimalScript.DIRECTION_SW),
				"Die Schleife ist durchgelaufen. Alle Elemente wurden entweder einsortiert oder lagen bereits richtig.",
				"schleifedurch", null, textProps);
		a.setHighlightFillColor(0, a.getLength(), Color.GRAY, null, null);
		a.highlightCell(0, a.getLength(), null, null);
		return a;
	}

	public void insertionSort(IntArray a) {
		lang.nextStep();
		schleifedurch.hide();
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		Text insertionSort = lang.newText(txtPos,
				"Nun werden die einzelnen Klassen mit einem Insertion Sort Algorithmus sortiert.", "insertionSort",
				null, textProps);
		int temp;

		for (int i = 1; i < a.getLength(); i++) {
			temp = a.getData(i);
			int j = i;
			while (j > 0 && a.getData(j - 1) > temp) {
				counterIs++;
				a.put(j, a.getData(j - 1), null, null);
				j--;
			}
			a.put(j, temp, null, null);
		}
		lang.nextStep();
		MultipleChoiceQuestionModel endeSor = new MultipleChoiceQuestionModel("endeSor");
		endeSor.setPrompt(
				"Warum ist die Liste am Ende nicht unbedingt korrekt sortiert und muss von Insertion Sort nachsortiert werden?");
		endeSor.addAnswer("Die Elemente wurden nur vorsortiert", 1,
				"Richtig. Flash Sort sortiert für Insertion Sort die Liste vor");
		endeSor.addAnswer("Der Algorithmus funktioniert nicht", 0,
				"Leider falsch, Flash Sort sortiert für Insertion Sort die Liste vor");
		endeSor.addAnswer("Da der Algorithmus mit Wahrscheinlichkeiten arbeitet", 0,
				"Leider falsch, Flash Sort sortiert für Insertion Sort die Liste vor");
		endeSor.addAnswer("Um Komplexität zu sparen werden nur die wichtigen Elemente sortiert", 0,
				"Leider falsch, Flash Sort sortiert für Insertion Sort die Liste vor");
		lang.addMCQuestion(endeSor);
		lang.hideAllPrimitives();
		rect.show();
		name1.show();
		name2.show();
		header.show();
		ana.show();
		anl.show();
		view1.show();
		lang.newText(new Offset(0, 30, anl, AnimalScript.DIRECTION_SW),
				"Wenn nun die eingegebene Liste annähernd gleichverteilt war, konnte sie mit nur geringem Aufwand sortiert werden.",
				"fin1", null, introProps);
		lang.newText(new Offset(0, 10, "fin1", AnimalScript.DIRECTION_SW),
				"Dazu wurden zuerst Minimum und Maximum bestimmt, um die Klassengröße festzulegen. Dann wurde für jedes Element die zugehörige Klasse berechnet.",
				"fin2", null, introProps);
		lang.newText(new Offset(0, 10, "fin2", AnimalScript.DIRECTION_SW),
				"Dadurch konnten die Elemente der Liste permutiert werden, bis jedes Element in der richtigen Klasse lag.",
				"fin3", null, introProps);
		lang.newText(new Offset(0, 10, "fin3", AnimalScript.DIRECTION_SW),
				"Die Klassen wurden durch einen Insertion Sort Sortieralgorithmus sortiert.", "fin4", null, introProps);
		lang.newText(new Offset(0, 10, "fin4", AnimalScript.DIRECTION_SW),
				"Die Elemente des Input Arrays wurden insgesamt " + (counterKlasseBer + counterPerm)
						+ " mal angeschaut.",
				"fin5", null, introProps);
		lang.newText(new Offset(0, 10, "fin5", AnimalScript.DIRECTION_SW),
				"Davon waren " + counterKlasseBer
						+ " mal nötig, um zur Initialisierung des Zeigerarrays die Klassen zu berechnen",
				"fin6", null, introProps);
		lang.newText(new Offset(0, 10, "fin6", AnimalScript.DIRECTION_SW),
				"und " + counterPerm + " mal, um die Elemente an die richtige Stelle zu legen.", "fin10", null,
				introProps);
		lang.newText(new Offset(0, 10, "fin10", AnimalScript.DIRECTION_SW),
				"Insertion Sort musste " + counterIs + " Elemente betrachten, um die Klassen zu sortieren.", "fin7",
				null, introProps);
		lang.newText(new Offset(0, 10, "fin7", AnimalScript.DIRECTION_SW),
				"Wenn du mehrere Daten eingibst, wird dir auffallen, dass Flash Sort besser vorsortieren kann, je gleichverteilter deine Eingabe ist,",
				"fin8", null, introProps);
		lang.newText(new Offset(0, 10, "fin8", AnimalScript.DIRECTION_SW),
				" und Insertion Sort dadurch schneller arbeitet.", "fin9", null, introProps);
		lang.nextStep();

		return;
	}
}