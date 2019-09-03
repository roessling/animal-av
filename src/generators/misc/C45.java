package generators.misc;
import java.awt.Color;
import java.awt.Font;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;

import javax.naming.InitialContext;

import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Timing;
import animal.animator.Move;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.util.Queue;
import java.util.Set;

/**
 * 
 */

/**
 * @author Felix Heller & Hendrik Jöntgen
 *
 */
public class C45 implements Generator {
	/** Abgabehinweise:
		– Zum Einstellen der Farben werden jetzt ausschließlich Properties verwendet (da es eben Sinn macht, dass diese Styling-Settings nicht dort angezeigt werden, wo man die Daten eingibt). Dabei kann es sein, dass wir auch einfach z.B. RectProperties verwendet haben, von dort die Farben extrahieren und diese dann als Text- bzw. Linienfarbe im Entscheidungsbaum verwenden. Das führt das Properties-Konzept zwar ein wenig ad absurdum, andererseits ist das Konzept auch nicht hinreichend durchdacht (sehr unflexibel).
		– <Label>s werden in Animal nicht angezeigt, sonst würde in den Optionen alles klarer werden.
		– Die defaults von highlightBorderColor und elemenentColor werden in Animal beim Eingabetabellen-Styling falsch angezeigt, obwohl in der XML gesetzt.
	 */

	private static final String NAME = "C4.5 Data Mining-Algorithmus";
	private static final String AUTHORS = "Felix Heller & Hendrik Jöntgen";

	// @formatter:off
	private static final String SOURCE = ""
			+ "Attribute targetAttribute = \"[...]\";" 									// 00
			+ "\nc45Step(decisionTree.root);"											// 01
			+ "\n"																		// 02
			+ "\nc45Step(Node node) {" 													// 03
			+ "\n	Data dataSubset = node.subset();" 									// 04
			+ "\n" 																		// 05
			+ "\n	if all values are the same in dataSubset[targetAttribute] {" 		// 06
			+ "\n		node.createLeafNode(dataSubset[targetAttribute].value());" 		// 07
			+ "\n	}" 																	// 08
			+ "\n	else {" 															// 09
			+ "\n		double bestGainRatio;" 											// 10
			+ "\n		Attribute bestAttribute;" 										// 11
			+ "\n		for each Attribute a in node.unusedAttributes() {" 				// 12
			+ "\n			double entropyS = entropy(dataSubset[targetAttribute]);"	// 13
			+ "\n			double infoGain = infoGain(a, entropyS);" 					// 14
			+ "\n			double splitInfo = entropy(dataSubset[a]);" 				// 15
			+ "\n			double gainRatio = infoGain / splitInfo;" 					// 16
			+ "\n			if gainRatio > bestGainRatio {" 							// 17
			+ "\n				bestGainRatio = gainRatio;" 							// 18
			+ "\n				bestAttribute = a;" 									// 19
			+ "\n			}" 															// 20
			+ "\n		}" 																// 21
			+ "\n" 																		// 22
			+ "\n		Node[] newNodes = node.splitOn(bestAttribute);" 				// 23
			+ "\n		node.addChildren(newNodes);" 									// 24
			+ "\n		for each node in newNodes { c45Step(node); }" 					// 25
			+ "\n	}" 																	// 26
			+ "\n}"; 																	// 27
	
	private static final String DESCRIPTION = ""
			+ "Der C4.5-Algorithmus wird als Entscheidungshilfe oder für eine"
			+ "\nKlassifizierung von Datensätzen verwendet."
			+ "\nEr wurde als Erweiterung zum ID3-Algorithmus entwickelt, welcher"
			+ "\nebenfalls in Animal visualisiert wurde."
			+ "\n"
			+ "\nC4.5 erstellt anhand eines gegebenen Datensatzes, welcher eine"
			+ "\nabhängige Variable beinhaltet, einen Entscheidungsbaum. Dabei"
			+ "\nwerden die unterschiedlichen Attribute des Datensatzes betrachtet"
			+ "\nund eine Entscheidung darüber getroffen, ob sie eine Aussagekraft"
			+ "\nüber die abhängige Variable besitzen oder nicht. Hierfür wird die"
			+ "\nsogenannte GainRatio herangezogen, welche sich aus dem InformationGain"
			+ "\nund der SplitInfo eines Attributes berechnen lässt."
			+ "\n"
			+ "\nZur Berechnung werden folgende Formeln benötigt (bessere Darstellung"
			+ "\nder Formeln z.B. auf Wikipedia):"
			+ "\n– Entropy(S) = -p_1 * lb(p_1) - ... - p_n * lb(p_n)"
			+ "\n      mit S = eine Menge von Informationen eines Attributs"
			+ "\n      und p = relativer Anteil einer Ausprägung zur Gesamtheit eines Datensatzes"
			+ "\n      → Maß für die Einheitlichkeit eine Menge von Informationen ist"
			+ "\n "
			+ "\n– InfoGain(S, A) = Entropy(S) - Σ_v∈Values(A) ((|S_v| / |S|) * Entropy(S_v))"
			+ "\n      mit A = Attribut und v = Ausprägungen des Attributs"
			+ "\n      → Maß für die Erklärungskraft eines Attributs A"
			+ "\n "
			+ "\n– SplitInfo(A) = Entropy(S_A)"
			+ "\n      mit S_A = Wertemenge des Attributs A"
			+ "\n      → Maß für die entstehende Baumbreite bei Spaltung über Attribut A"
			+ "\n "
			+ "\n– GainRatio(S, A) = SplitInfo(A) / InfoGain(S, A)"
			+ "\n      → Setzt InfoGain und SplitInfo zueinander in Relation"
			+ "\n"
			+ "\nIm Vergleich zu ID3 bestraft C4.5 mithilfe der SplitInfo Attribute"
			+ "\nmit sehr vielen verschiedenen Ausprägungen, die zu vielen verschiedenen"
			+ "\nTeilbäumen führen würden (sogenanntes Overfitting). Darüber hinaus ist"
			+ "\nes möglich, dass eine minimale GainRatio festgelegt wird, sodass der"
			+ "\nEntscheidungsbaum „gestutzt“ (sog. Pruning) wird.";
	
	private static final String INTRO = ""
			+ "In den folgenden Folien wird der C4.5-Algorithmus visualisiert."
			+ "\n"
			+ "\n" + DESCRIPTION;
	
	private static final String OUTRO = ""
			+ "Wir hoffen, dass aufgrund der eben gezeigten Visualisierung die Arbeitsweise"
			+ "\ndes C4.5 Algorithmus verdeutlicht werden konnte. Anhand der Eingangsdaten"
			+ "\nwurde ein Entscheidungsbaum erstellt, welcher als Entscheidungshilfe oder"
			+ "\nals Klassifikator fungieren kann."
			+ "\n"
			+ "\nNeben dem C4.5-Algorithmus existieren heute zahlreiche andere Verfahren,"
			+ "\nmit welchem eine Klassifizierung einfacher und präziser durchgeführt werden kann."
			+ "\n"
			+ "\nBeispiele hierfür sind: Naive Bayes, Support Vector Machine und Neuronale Netzwerke."
			+ "\n"
			+ "\nDiese Klassifikatoren sind jedoch weitaus komplexer und ihre Klassifizierungs-Regeln"
			+ "\nkönnen nicht leicht erfasst werden. Der Vorteil des C4.5 liegt in dem übersichtlichen"
			+ "\nEntscheidungsbaum."
			+ "\n"
			+ "\nWenn du möchtest, kannst du zum Schluss noch an einem Quiz teilnehmen. Klicke weiter.";
	// @formatter:on

	public static String[][] c45InputData;
	public static LinkedHashMap<String, String[]> c45Input = new LinkedHashMap<>();
	public static String targetAttribute;
	public static double minimumGainRatio = 0.0;
	public static int iteration;
	
	/**
	 * Animal Objects
	 */
	public static Language lang;
	public static StringMatrix dataTable;
	public static MatrixProperties dataTableProps;
	public static Color dataTableTextColor;
	public static Color dataTableCellHighlightBorderColor;
	public static Color dataTableCellHighlightTextColor;
	public static Color dataTableColumnHighlightBorderColor;
	
	public static Code code;
	public static SourceCodeProperties codeProps;
	public static Color codeCommentColor;

	public static TextProperties titleProps;
	public static TextProperties detailsProps;
	
	public static Text iterationTitle;
	public static SourceCode slideText;
	public static SourceCodeProperties slideTextProps;
	public static Text[] detailsText = new Text[] { null, null };
	
	public static Color treeTargetVariableColor;
	public static Color treeNodeAttributeTextColor;
	public static Color treeNodeGainRatioTextColor;
	public static Color treeNodeBgColor;
	public static Color treeNodeBgHighlightColor;
	public static Color treeEdgeLineColor;
	public static Color treeEdgeTextColor;
	
	@Override
	public String generate(AnimationPropertiesContainer styling, Hashtable<String, Object> data) {
		lang = Language.getLanguageInstance(
				AnimationType.ANIMALSCRIPT,
				NAME, 
				AUTHORS,
				1680,
				1050
		);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		lang.setStepMode(true);
		extractStyles(styling);
		extractData(data);
		showStartScreen();
		setupAlgoScreen();
		createC45Animation();
		showEndScreen();
		askQuestions();
		lang.finalizeGeneration();
		return lang.toString();
	}
	
	private void extractData(Hashtable<String, Object> data) {
		c45InputData = (String[][]) data.get("C4.5 Eingabedaten");
		for (int i = 0; i < c45InputData[0].length; i++) {
			String attribute = c45InputData[0][i];
			String[] attributeData = new String[c45InputData.length - 1];
			for (int j = 1; j < c45InputData.length; j++) {
				attributeData[j - 1] = c45InputData[j][i];
			}
			c45Input.put(attribute, attributeData);
		}
		targetAttribute = (String) data.get("C4.5 Zielattribut");
		if (!c45Input.keySet().contains(targetAttribute)) {
			targetAttribute = (String) c45Input.keySet().toArray()[c45Input.keySet().size() - 1];
		}
	}
	
	private void extractStyles(AnimationPropertiesContainer styles) {
		dataTableProps = (MatrixProperties) styles.getPropertiesByName("Eingabetabellen-Styling");
		dataTableTextColor = (Color) dataTableProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		dataTableCellHighlightBorderColor = (Color) dataTableProps.get(AnimationPropertiesKeys.GRID_HIGHLIGHT_BORDER_COLOR_PROPERTY);
		dataTableCellHighlightTextColor = (Color) dataTableProps.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY);
		dataTableColumnHighlightBorderColor = (Color) dataTableProps.get(AnimationPropertiesKeys.GRID_BORDER_COLOR_PROPERTY);
		dataTableProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, dataTableTextColor);
		dataTableProps.set(AnimationPropertiesKeys.GRID_BORDER_COLOR_PROPERTY, Color.WHITE);
		dataTableProps.set(AnimationPropertiesKeys.GRID_HIGHLIGHT_BORDER_COLOR_PROPERTY, Color.WHITE);

		codeProps = (SourceCodeProperties) styles.getPropertiesByName("Codeblock-Styling");

		slideTextProps = (SourceCodeProperties) styles.getPropertiesByName("Intro/Outrotext-Styling");
		slideTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, ((Font) slideTextProps.get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont(14f)); 
		
		titleProps = (TextProperties) styles.getPropertiesByName("Titel-Styling");
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, ((Font) titleProps.get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont(16f).deriveFont(Font.BOLD));
		
		detailsProps = (TextProperties) styles.getPropertiesByName("Rechnungstext-Styling");
		detailsProps.set(AnimationPropertiesKeys.FONT_PROPERTY, ((Font) titleProps.get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont(13f).deriveFont(Font.ITALIC));
		
		treeTargetVariableColor = (Color) styles.getPropertiesByName("Entscheidungsbaum-Zielattributtext-Styling").get(AnimationPropertiesKeys.COLOR_PROPERTY);
		
		ArrayProperties ap = (ArrayProperties) styles.getPropertiesByName("Entscheidungsbaum-Knoten-Styling");
		treeNodeAttributeTextColor = (Color) ap.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		treeNodeGainRatioTextColor = (Color) ap.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY);
		treeNodeBgColor = (Color) ap.get(AnimationPropertiesKeys.FILL_PROPERTY);
		treeNodeBgHighlightColor = (Color) ap.get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY);
		
		RectProperties rp = (RectProperties) styles.getPropertiesByName("Entscheidungsbaum-Kanten-Styling");
		treeEdgeLineColor = (Color) rp.get(AnimationPropertiesKeys.FILL_PROPERTY);
		treeEdgeTextColor = (Color) rp.get(AnimationPropertiesKeys.COLOR_PROPERTY);
	}
	
	private static void showStartScreen() {
		lang.nextStep("Intro");
		iterationTitle = lang.newText(new Coordinates(20, 10), "", "", null, titleProps);
		iterationTitle.setText("C4.5-Algorithmus – Intro", null, null);
		setSlideText(INTRO);
	}
	
	private static void showEndScreen() {
		lang.nextStep("Fazit");
		code.sourceCode.hide();
		code.commentsCode.hide();
		iterationTitle.setText("C4.5-Algorithmus – Fazit", null, null);
		setSlideText(OUTRO);
	}
	
	private static void askQuestions() {
		lang.nextStep("Quiz");
//		int points = 0;
//		Text pointsText = lang.newText(new Coordinates(20, 400), "Erreichte Punktzahl im Quiz: " + points, "", null, titleProps);
		
		MultipleChoiceQuestionModel q1 = new MultipleChoiceQuestionModel("decisionCriteria");
		q1.setPrompt("Was ist das Entscheidungskritierium für die Wahl eines Attributes beim C4.5-Algorithmus?");
		q1.addAnswer("InformationGain", 0, "Falsch, der InformationGain wird beim ID3-Algorithmus als Split-Kriterium herangezogen.");
		q1.addAnswer("Entropy", 0, "Falsch, die Entropie fließt jedoch an verschiedenen Stellen in die Berechnung mit ein.");
		q1.addAnswer("GainRatio", 1, "Richtig!");
		q1.addAnswer("SplitInfo", 0, "Falsch, SplitInfo wird verwendet, um den InformationGain zu normalisieren.");
		lang.addMCQuestion(q1);
		
		lang.nextStep();
//		points += q1.getPointsAchieved();
//		pointsText.setText("Erreichte Punktzahl im Quiz: " + points, null, null);

		MultipleChoiceQuestionModel q2 = new MultipleChoiceQuestionModel("prerequisite");
		q2.setPrompt("Was ist eine Vorausetzung für die Verwendung des Algorithmus?");
		q2.addAnswer("Es müssen mindestens 20 Datensätze vorhanden sein.", 0, "Falsch, prinzipiell gibt es keine vorgeschriebene Anzahl an Datensätzen. Die Erklärungskraft kann natürlich mit einer höheren Anzahl an Datensätzen zunehmen (zwei Datensätze sind sicherlich nicht ausreichend, um Aussagen zu treffen).");
		q2.addAnswer("Es muss eine abhängige Variable existieren.", 1, "Richtig! Irgendwas soll ja gelernt werden.");
		q2.addAnswer("Der Datensatz muss sich um das Thema Segeln drehen.", 0, "Falsch... na komm schon, das wusstest du selbst!");
		q2.addAnswer("Die Sonne muss scheinen.", 0, "Nein - eventuell ist gutes Wetter sogar von Nachteil, weil dann niemand am PC Data Mining betreiben möchte. :)");
		lang.addMCQuestion(q2);
		
		lang.nextStep();
//		points += q2.getPointsAchieved();
//		pointsText.setText("Erreichte Punktzahl im Quiz: " + points, null, null);

		MultipleChoiceQuestionModel q3 = new MultipleChoiceQuestionModel("id3c45diffs");
		q3.setPrompt("Was ist eine Verbesserung gegenüber dem Vorgänger-Algorithmus ID3?");
		q3.addAnswer("Im Gegensatz zu ID3 erstellt C4.5 einen Entscheidungsbaum.", 0, "Falsch, das geschieht auch bei ID3.");
		q3.addAnswer("ID3 unterstützt nur maximal 3 unabhängige Variablen (Attribute).", 0, "Falsch, in der Anzahl der Attribute gibt es keine Unterschiede zwischen ID3 und C4.5. C4.5 ist jedoch flexibler, was die Datenqualität angeht.");
		q3.addAnswer("ID3 funktioniert nur auf Zahlenmengen.", 0, "Nein, ID3 kann auch andere Datensätze klassifizieren.");
		q3.addAnswer("Sogenanntes Overfitting, das zu einem großen Entscheidungsbaum führt, wird verhindert.", 1, "Absolut korrekt! Dies wird durch das Normalisieren von InformationGain mit SplitInfo erreicht.");
		lang.addMCQuestion(q3);
		
		lang.nextStep();
//		points += q3.getPointsAchieved();
//		pointsText.setText("Erreichte Punktzahl im Quiz: " + points + " (Ende!)", null, null);
	}
	
	private static void setSlideText(String text) {
		slideText = lang.newSourceCode(new Coordinates(20, 30), "", null, slideTextProps);
		String[] lines = text.split("\n");
		for (String line : lines) {
			slideText.addCodeLine(line.replace("\"", "\\\""), null, 0, null);
			if (line.equals("")) {
				lang.nextStep();
			}
		}
	}
	
	private static void setupAlgoScreen() {
		lang.nextStep();
		dataTable = lang.newStringMatrix(new Coordinates(860, 40), c45InputData, "dataTable", null, dataTableProps);
		
		lang.newText(new Coordinates(860, 10), "Eingabedaten:", "", null, titleProps);
		
		slideText.hide();
		
		code = new Code(new Coordinates(20, 30), SOURCE);
		code.setComment(0, "→ \"" + targetAttribute + "\"");
		
		detailsText[0] = lang.newText(new Coordinates(20, 500), "", "", null, detailsProps);
		detailsText[1] = lang.newText(new Coordinates(20, 520), "", "", null, detailsProps);
	}
	
	private static void createC45Animation() {		
		C45Tree decisionTree = new C45Tree();
		iteration = 1;
		Queue<C45Tree.Node> queue = new LinkedBlockingQueue<>();
		queue.add(decisionTree.root);
		decisionTree.root.draw();
		code.highlightCodeBlock(0, 1);
		lang.nextStep("Initialisierung");
		
		while (!queue.isEmpty()) {
			iterationTitle.setText("C4.5-Algorithmus – " + iteration + ". Iteration: ", null, null);
			
			C45Tree.Node n = queue.poll();
			n.highlight();
			TableHighlighter.highlightRows(n.getNodeFilter());
			code.setComment(4, "// gelb markierte Einträge");
			code.highlight(4);
			lang.nextStep(iteration + ". Iteration: " + n);
			
			Set<Entry<String, Integer>> targetSubset = countOccurences(getSubset(targetAttribute, n.getNodeFilter())).entrySet();
			boolean allValuesEqual = targetSubset.size() == 1;
			String countComment = "";
			for (Entry<String, Integer> e : targetSubset) {
				countComment += e.getValue() + "x" + e.getKey() + ", ";
			}
			code.highlight(6);
			code.setComment(6, "→ " + allValuesEqual + (countComment.length() >= 2 ? " (" + countComment.substring(0, countComment.length() - 2) + ")" : ""));
			TableHighlighter.highlightColumn(targetAttribute, n.getNodeFilter());
			lang.nextStep();
			if (allValuesEqual) {
				Entry<String, Integer> e = targetSubset.iterator().next();
				n.attribute = e.getKey() + " (" + e.getValue() + ")";
				n.textColor = treeTargetVariableColor;
				code.highlightCodeBlock(false, 7, 8, false);
			}
			
			else {
				code.setComment(4, "// gelb markierte Einträge");
				code.setComment(10, "→ -1");
				code.setComment(11, "→ null");
				code.highlightCodeBlock(10, 11);
				lang.nextStep();
				
				for (String attribute : n.getUnusedAttributes()) {
					code.setComment(12, "a = " + attribute);
					code.highlight(12);
					lang.nextStep();
					double gainRatio = gainRatio(attribute, n.getNodeFilter());
					// System.out.println("GainRatio(" + attribute + ")= " + gainRatio);

					code.setComment(17, round(gainRatio) + " > " + round(n.gainRatio) + " ? → " + (gainRatio > n.gainRatio));
					code.highlight(17);
					
					if (gainRatio > n.gainRatio) {
						code.setComment(18, "→ " + round(gainRatio));
						code.setComment(19, "→ " + attribute);
						code.highlightCodeBlock(false, 18, 20, false);
						lang.nextStep();
						n.gainRatio = gainRatio;
						n.attribute = attribute;
						code.setComment(10, "= " + round(gainRatio));
						code.setComment(11, "= " + attribute);
					}
					else {
						lang.nextStep();
					}
				}
				code.setComment(3, "");
				code.setComment(23, "bestAttribute = " + n.attribute);
				code.highlight(23);
			}
			
			TableHighlighter.unhighlightColumns();
			n.draw();
			lang.nextStep();
			
			if (n.gainRatio > 0 && n.gainRatio >= minimumGainRatio) {
				for (String attributeValue : countOccurences(c45Input.get(n.attribute)).keySet()) {
					C45Tree.Node child = new C45Tree.Node("[?]", -1f);
					n.addChild(attributeValue, child);
					queue.add(child);
				}
				
				for (C45Tree.Node child : n.children.keySet()) {
					child.draw();
				}
				code.highlight(24);
				lang.nextStep();
			}
			/*
			else {
				System.out.println("DAS SOLLTE NICHT PASSIEREN");
				for (Map.Entry<String, Integer> c : countOccurences(getSubset(targetVariable, n.getNodeFilter())).entrySet()) {
					if (c.getValue() > n.gainRatio) {
						n.attribute = c.getKey();
						n.gainRatio = c.getValue();
					}
				}
				// System.out.println("Ende des Baums mit Entscheidung = " + n.attribute + " (" + n.gainRatio + ")");
			}
			*/
			
			iteration++;			
			n.unhighlight();
			code.highlight(25);
			TableHighlighter.unhighlightRows();
			lang.nextStep();
		}
		code.unhighlight(false);
		code.setComment(3, "");
		lang.nextStep("Endergebnis");
	}
	
	/**
	 * Entropy(S) = -p1log2(p1) - p2log(p2) (p1 = Anzahl True, p2 = Anzahl False)
	 */
	public static double entropy(String attributeName, HashMap<String, String> filter) {
		return entropy(attributeName, filter, 0);
	}
	
	public static double entropy(String attributeName, HashMap<String, String> filter, int detailsTextIndex) {
		ArrayList<String> subset = getSubset(attributeName, filter);
		
		// calculate entropy
		double entropy = 0;
		Iterator<Entry<String, Integer>> it = countOccurences(subset).entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, Integer> e = it.next();
			double p = (double) e.getValue() / subset.size();
			entropy -= p * log2(p);
			
			// cosmetics...
			HashMap<String, String> subsetFilter = new HashMap<>(filter);
			subsetFilter.put(attributeName, e.getKey());
			TableHighlighter.highlightColumn(attributeName, subsetFilter);
			DetailsTextHelper.add(e.getValue() + " / " + subset.size() + " * log2(" + e.getValue() + " / " + subset.size() + ")" + (it.hasNext() ? " - " : " = " + round(entropy)), detailsTextIndex);
			lang.nextStep();
		}
		TableHighlighter.unhighlightColumns();
		return entropy;
	}
	
	/**
	 * InfoGain(S, A) = Entropy(S) - Summe(|Sv|/|S| * Entropy(Sv)
	 */
	public static double infoGain(String attributeName, HashMap<String, String> filter) {
		code.highlight(13);
		DetailsTextHelper.set("entropyS = - ");
		double infoGain = entropy(targetAttribute, filter);
		code.setComment(13, "→ " + round(infoGain));
		lang.nextStep();
		
		DetailsTextHelper.set("infoGain = entropyS - ");
		code.highlight(14);
		lang.nextStep();
		
		ArrayList<String> values = getSubset(attributeName, filter);
		Iterator<Entry<String, Integer>> it = countOccurences(values).entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Integer> e = it.next();
			HashMap<String, String> subsetFilter = new HashMap<>(filter);
			subsetFilter.put(attributeName, e.getKey());
			
			TableHighlighter.highlightColumn(attributeName, subsetFilter);
			DetailsTextHelper.add(e.getValue() + " / " + values.size());
			lang.nextStep();
			String etrpyLabel = "entropy(targetAttribute, " + attributeName + " == '" + e.getKey() + "')";
			DetailsTextHelper.add(" * " + etrpyLabel);
			DetailsTextHelper.set(etrpyLabel + " = - ", 1);
			TableHighlighter.borderCells(new String[] { attributeName, targetAttribute }, subsetFilter);
			double etrpy = entropy(targetAttribute, subsetFilter, 1);
			infoGain -= (double) e.getValue() / values.size() * etrpy;
			TableHighlighter.unborderCells();
			DetailsTextHelper.set(DetailsTextHelper.text[0].substring(0, DetailsTextHelper.text[0].length() - etrpyLabel.length()) + round(etrpy) + (it.hasNext() ? " + " : " = " + round(infoGain)));
			lang.nextStep();
			DetailsTextHelper.clear(1);
		}
	
		code.setComment(14, "→ " + round(infoGain));
		lang.nextStep();
		DetailsTextHelper.clear();
		
		return infoGain;
	}
	
	/**
	 * SplitInfo(A) = - Summe((ni/n) * log2(ni / n))
	 */
	public static double splitInfo(String attributeName, HashMap<String, String> filter) {
		code.highlight(15);
		DetailsTextHelper.set("splitInfo = - ");
		double splitInfo = entropy(attributeName, filter);
		code.setComment(15, "→ " + round(splitInfo));
		lang.nextStep();
		DetailsTextHelper.clear();
		return splitInfo;
	}
	
	/**
	 * GainRatio(A) = InfoGain(A) / SplitInfo(A)
	 */
	public static double gainRatio(String attributeName, HashMap<String, String> filter) {
		double infoGain = infoGain(attributeName, filter);
		double splitInfo = splitInfo(attributeName, filter);
		double gainRatio = infoGain / splitInfo;
		DetailsTextHelper.set("gainRatio = infoGain / splitInfo = " + round(infoGain) + " / " + round(splitInfo) + " = " + round(gainRatio));
		code.setComment(16, "→ " + round(gainRatio));
		code.highlight(16);
		lang.nextStep();
		DetailsTextHelper.clear();
		return gainRatio;
	}
	
	private static String round(double d) {
		return new DecimalFormat("0.000").format(d);
	}
	
	private static double log2(double n) {
		return Math.log(n) / Math.log(2);
	}
	
	/**
	 * Gibt eine HashMap zurück, die jeweils die Anzahl der Werte in einem
	 * String Array enthäl (also z.B. für ein Array aus [a, b, b, a, c] wird
	 * eine HashMap mit [a => 2, b => 2, c => 1] zurückgegeben.
	 */
	private static HashMap<String, Integer> countOccurences(String[] values) {
		HashMap<String, Integer> result = new HashMap<>();
		
		// count occurence of each value
		for (String value : values) {
			if (!result.containsKey(value)) {
				result.put(value, 1);
			}
			else {
				result.put(value, result.get(value) + 1);
			}
		}
		return result;
	}
	
	private static HashMap<String, Integer> countOccurences(ArrayList<String> values) {
		String[] arr = new String[values.size()];
		return countOccurences(values.toArray(arr));
	}

	/**
	 * Gibt eine Untermenge aus der Zieldatenmenge zurück, für die die Attribute aus filterCriteria.keys
	 * die Werte aus filterCriteria.values annehmen
	 */
	private static ArrayList<String> getSubset(String targetAttributeName, Map<String, String> filterCriteria) {
		ArrayList<String> result = new ArrayList<>();
		ArrayList<Integer> indices = getSubsetIndices(filterCriteria);
		String[] targetValues = c45Input.get(targetAttributeName);
		for (int i : indices) {
			result.add(targetValues[i]);
		}
		return result;
	}
	
	private static ArrayList<Integer> getSubsetIndices(Map<String, String> filterCriteria) {
		ArrayList<Integer> result = new ArrayList<>();
		for (int i = 0; i < c45Input.get(targetAttribute).length; i++) {
			boolean add = true;
			for (Map.Entry<String, String> filter : filterCriteria.entrySet()) {
				add = add && c45Input.get(filter.getKey())[i].equals(filter.getValue());
			}
			if (add) {
				result.add(i);
			}
		}
		return result;
	}
	
	public static class C45Tree {
		public Node root = new Node("[?]", -1.0);
		
		private static final int POSITION_ROOT_X = 840;
		private static final int POSITION_ROOT_Y = 600;
		private static final int WIDTH = 4300;
		
		public static class Node {
			Node parent;
			String attribute;
			double gainRatio;
			LinkedHashMap<Node, String> children = new LinkedHashMap<>();
			
			private Circle circle;
			private Color circleColor = treeNodeBgColor;
			private Color textColor = treeNodeAttributeTextColor;
			private Text attributeText;
			private Text gainRatioText;
			private Polyline parentEdge;
			private Text parentEdgeText;
			
			public Node(String attribute, double gainRatio) {
				super();
				this.attribute = attribute;
				this.gainRatio = gainRatio;
			}
			
			public void addChild(String value, Node node) {
				children.put(node, value);
				node.parent = this;
			}
			
			public HashMap<String, String> getNodeFilter() {
				Node c = this;
				HashMap<String, String> filter = new HashMap<>();
				for (Node p : getParents()) {
					filter.put(p.attribute, p.children.get(c));
					c = p;
				}
				return filter;
			}

			public ArrayList<String> getUnusedAttributes() {
				ArrayList<String> attributes = new ArrayList<>(c45Input.keySet());
				attributes.remove(targetAttribute);
				for (Node p : getParents()) {
					attributes.remove(p.attribute);
				}
				return attributes;
			}
			
			ArrayList<Node> getParents() {
				Node p = parent;
				ArrayList<Node> result = new ArrayList<>();
				while (p != null) {
					result.add(p);
					p = p.parent;
				}
				return result;
			}
			
			public void highlight() {
				circleColor = treeNodeBgHighlightColor;
				circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, circleColor, null, null);
			}
			
			public void unhighlight() {
				circleColor = treeNodeBgColor;
				circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, circleColor, null, null);
			}
			
			public void draw() {
				if (circle != null) {
					// stupid hack, but unfortunately AnimationPropertiesKeys.CENTERED_PROPERTY is bugged
					// (text will not be centered if changed, so we have to create a complete new circle and hide the old)
					circle.hide();
				}
				
				int[] pos = pos();
				CircleProperties cp = new CircleProperties();
				cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				cp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
				cp.set(AnimationPropertiesKeys.FILL_PROPERTY, circleColor);
				circle = lang.newCircle(new Coordinates(pos[0], pos[1]), 15, "", null, cp);
				
				if (parent != null) {
					int[] parentPos = parent.pos();
					PolylineProperties pp = new PolylineProperties();
					pp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
					pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, treeEdgeLineColor);
					parentEdge = lang.newPolyline(new algoanim.util.Node[] { new Coordinates(parentPos[0], parentPos[1]), new Coordinates(pos[0], pos[1])}, "", null, pp);
					
					TextProperties tp = new TextProperties();
					tp.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
					tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, treeEdgeTextColor);
					parentEdgeText = lang.newText(new Coordinates(parentPos[0] - (parentPos[0] - pos[0]) / 2, parentPos[1] - (parentPos[1] - pos[1]) / 2 - 5), parent.children.get(this), "", null, tp);
				}
				
				TextProperties tp = new TextProperties();
				tp.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
				tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, textColor);
				tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 14));
				attributeText = lang.newText(new Coordinates(pos[0], pos[1] - 5), attribute, "", null, tp);
				
				if (gainRatio >= 0) {
					TextProperties tp2 = new TextProperties();
					tp2.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
					tp2.set(AnimationPropertiesKeys.COLOR_PROPERTY, treeNodeGainRatioTextColor);
					tp2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 12));
					attributeText = lang.newText(new Coordinates(pos[0], pos[1] + 15), "GainRatio = " + round(gainRatio), "", null, tp2);
				}
				
				if (parent == null) {
					// Tree title text
					TextProperties tp3 = new TextProperties();
					tp3.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
					tp3.set(AnimationPropertiesKeys.COLOR_PROPERTY, treeTargetVariableColor);
					tp3.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 16));
					lang.newText(new Coordinates(POSITION_ROOT_X, POSITION_ROOT_Y - 40), targetAttribute, "", null, tp3);
				}
				
				return;
			}
			
			private int[] pos() {
				int[] pos = new int[2];
				int layer = getParents().size();
				int posX = POSITION_ROOT_X;
				int posY = POSITION_ROOT_Y + layer * 100;
				if (parent != null) {
					posX = parent.pos()[0];
					double layerWidth = layerWidth();
					int siblingsCount = parent.children.size() - 1;
					int positionAmongSiblings = new ArrayList<Node>(parent.children.keySet()).indexOf(this);
					double layerPosX = (positionAmongSiblings * 1.0 / siblingsCount * layerWidth) - layerWidth / 2;
					posX += layerPosX;
				}
				pos[0] = posX;
				pos[1] = posY;
				return pos;
			}
			
			private double layerWidth() {
				if (parent == null) {
					return WIDTH * 1.0;
				}
				return parent.layerWidth() / (parent.children.size() + 1);
			}
			
			@Override
			public String toString() {
				Node c = this;
				String result = "";
				for (Node p : getParents()) {
					result = " –" + p.children.get(c) + "–→ [" + c.attribute + "]" + result;
					c = p;
				}
				result = "[" + c.attribute + "]" + result;
				return result;
			}
		}
	}
	
	private static class Code {
		SourceCode sourceCode;
		Coordinates coordinates;
		SourceCode commentsCode;
		int commentsCodeSize;
		HashMap<Integer, String> comments = new HashMap<>();
		HashMap<Boolean, ArrayList<Integer>> highlightedLines = new HashMap<>();
		
		public Code(Coordinates c, String code) {
		    sourceCode = lang.newSourceCode(c, "", null, codeProps);
			String[] codeLines = SOURCE.split("\n");
			for (String codeLine : codeLines) {
				 sourceCode.addCodeLine(codeLine.replace("\"", "\\\""), null, getCodeLevel(codeLine), null);
			}
		    
			this.coordinates = c;
			highlightedLines.put(false, new ArrayList<>());
			highlightedLines.put(true, new ArrayList<>());
			refreshCommentsCode();
		}
		
		public void setComment(int line, String comment) {
			comments.put(line, comment);
			
			if (line < commentsCodeSize) {
				Iterator<Integer> it = comments.keySet().iterator();
				while (it.hasNext()) {
					if (it.next() >= line) {
						it.remove();
					}
				}
				refreshCommentsCode();
				
			}
			
			while (line > commentsCodeSize) {
				commentsCode.addCodeLine("", null, 0, null);
				commentsCodeSize++;
			}

			
			commentsCode.addCodeLine(comment.replace("\"", "\\\""), null, 0, null);
			commentsCodeSize++;
		}
		
		private int getCodeLevel(String codeLine) {
			return codeLine.length() - codeLine.replace("\t", "").length();
		}
		
		private void refreshCommentsCode() {
			if (commentsCode != null) {
				commentsCode.hide();
			}
			
		    SourceCodeProperties cp = new SourceCodeProperties();
		    cp.set(AnimationPropertiesKeys.FONT_PROPERTY, codeProps.get(AnimationPropertiesKeys.FONT_PROPERTY));
		    cp.set(AnimationPropertiesKeys.COLOR_PROPERTY, codeProps.get(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY));
		    commentsCode = lang.newSourceCode(new Coordinates(coordinates.getX() + 455, coordinates.getY()), "", null, cp);
		    commentsCodeSize = 0;
		    
		    for (Map.Entry<Integer, String> comment : comments.entrySet()) {
		    	setComment(comment.getKey(), comment.getValue());
		    }
		}
		
		public void highlight(int lineNo) {
			highlight(false, lineNo, true);
		}
		
		public void highlight(boolean contextColor, int lineNo) {
			highlight(contextColor, lineNo, true);
		}
		
		public void highlight(boolean contextColor, int lineNo, boolean unhighlight) {
			if (unhighlight) {
				unhighlight(contextColor);
			}
			sourceCode.highlight(lineNo, 0, contextColor);
			highlightedLines.get(contextColor).add(lineNo);
		}
		
		public void highlightCodeBlock(int startLineNo, int endLineNo) {
			highlightCodeBlock(false, startLineNo, endLineNo, true);
		}
		
		public void highlightCodeBlock(boolean contextColor, int startLineNo, int endLineNo) {
			highlightCodeBlock(contextColor, startLineNo, endLineNo, true);
		}
		
		public void highlightCodeBlock(boolean contextColor, int startLineNo, int endLineNo, boolean unhighlight) {
			if (unhighlight) {
				unhighlight(contextColor);
			}
			for (int i = startLineNo; i <= endLineNo; i++) {
				highlight(contextColor, i, false);
			}
		}
		
		public void unhighlight(boolean contextColor) {
			for (int i : highlightedLines.get(contextColor)) {
				sourceCode.unhighlight(i);
			}
			highlightedLines.get(contextColor).clear();
		}
	}
	
	private static class TableHighlighter {
		static ArrayList<Integer> highlightedRows = new ArrayList<>();
		static int highlightedColumn;
		static ArrayList<Integer> highlightedColumnsCells = new ArrayList<>();
		static ArrayList<Integer> borderedRows = new ArrayList<>();
		static ArrayList<Integer> borderedCols = new ArrayList<>();
		
		private static void highlightRows(HashMap<String, String> filter) {
			unhighlightRows();
			ArrayList<Integer> indices = getSubsetIndices(filter);
			for (int i : indices) {
				dataTable.highlightCellColumnRange(i + 1, 0, c45Input.size() - 1, null, null);
				highlightedRows.add(i);
			}
		}
		
		private static void highlightColumn(String attribute, HashMap<String, String> filter) {
			unhighlightColumns();
			ArrayList<Integer> indices = getSubsetIndices(filter);
			int col = new ArrayList<String>(c45Input.keySet()).indexOf(attribute);
			dataTable.setGridBorderColor(0, col, dataTableCellHighlightTextColor, null, null);
			for (int i : indices) {
				dataTable.setGridTextColor(i + 1, col, dataTableColumnHighlightBorderColor, null, null);
				highlightedColumnsCells.add(i);
			}
			highlightedColumn = col;
		}
		
		private static void borderCells(String[] attributes, HashMap<String, String> filter) {
			unborderCells();
			ArrayList<Integer> indices = getSubsetIndices(filter);
			for (int i : indices) {
				for (String a : attributes) {
					int col =  new ArrayList<String>(c45Input.keySet()).indexOf(a);
					dataTable.setGridHighlightBorderColor(i + 1, col, dataTableCellHighlightBorderColor, null, null);
					borderedCols.add(col);
				}
				borderedRows.add(i);
			}
		}
		
		private static void unborderCells() {
			for (int i : borderedRows) {
				for (int j : borderedCols) {
					dataTable.setGridHighlightBorderColor(i + 1, j, Color.WHITE, null, null);
				}
			}
			borderedRows.clear();
			borderedCols.clear();
		}
		
		private static void unhighlightColumns() {
			dataTable.setGridBorderColor(0, highlightedColumn, Color.WHITE, null, null);
			for (int i : highlightedColumnsCells) {
				dataTable.setGridTextColor(i + 1, highlightedColumn, dataTableTextColor, null, null);
			}
			highlightedColumnsCells.clear();
		}
		
		private static void unhighlightRows() {		
			for (int i : highlightedRows) {
				dataTable.unhighlightCellColumnRange(i + 1, 0, c45Input.size() - 1, null, null);
			}
			highlightedRows.clear();
		}
	}
	
	private static class DetailsTextHelper {
		private static String[] text = new String[] {"", ""};
		
		public static void set(String text) {
			set(text, 0);
		}
		
		public static void set(String text, int index) {
			DetailsTextHelper.text[index] = text;
			detailsText[index].setText(DetailsTextHelper.text[index], null, null);
		}
		
		public static void add(String text) {
			add(text, 0);
		}
		
		public static void add(String text, int index) {
			set(DetailsTextHelper.text[index] + text, index);
		}
		
		public static void clear() {
			clear(0);
			clear(1);
		}
		
		public static void clear(int index) {
			set("", index);
		}
	}

	@Override
	public String getAlgorithmName() {
		return NAME;
	}

	@Override
	public String getAnimationAuthor() {
		return AUTHORS;
	}

	@Override
	public String getCodeExample() {
		return SOURCE;
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getFileExtension() {
		return "asu";
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public void init() {}
}
