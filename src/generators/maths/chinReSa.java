/*
 * chinReSa.java
 * Emine Saracoglu, Felix Sternkopf, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Graph;
import algoanim.primitives.IntArray;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
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
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.variables.*;
import algoanim.primitives.Variables;
import interactionsupport.models.*;

public class chinReSa implements Generator {

	private static final String HEADER1 = "Chinesischer Restsatz";
	private static final String DESCRIPTION0 = "Bitte geben Sie eine kleinere Arraylänge an,";
	private static final String DESCRIPTION01 = "da sonst der verfügbare Platz überlaufen wird.";

	private static final String DESCRIPTION1 = "Der Chinesiche Restsatz trifft eine Aussage über simultane Kongruenzen für den Fall, ";
	private static final String DESCRIPTION2 = "dass die Divisoren teilerfremd sind. Seien m_1,..., m_n paarweise teilerfremde natürliche Zahlen, ";
	private static final String DESCRIPTION3 = "dann existiert für jedes Tupel ganzer Zahlen a_1, ..., a_n eine ganze Zahl x, die die folgende ";
	private static final String DESCRIPTION4 = "simultane Kongruenz erfüllt: ";
	private static final String DESCRIPTION5 = "x  \u2261  a_1  (mod m_1)";
	private static final String DESCRIPTION5_1 = "... ";
	private static final String DESCRIPTION5_2 = "x  \u2261  a_n  (mod m_n) ";

	private static final String DESCRIPTION6 = " --> Chinesischer Restsatz kann nicht berechnet werden,";
	private static final String DESCRIPTION7 = " weil ggT != 1";

	private static final String FORMEL1 = "Zu lösende Kongruenzen:";

	private static final String RECHNUNG = "Berechnung der einzelnen Schritte:";
	private static final String ZWISCHENERGEBNIS = "Zwischenergebnisse:";

	private final RectProperties RectRP = new RectProperties();
	private final TextProperties FormularTP = new TextProperties();

	private final TextProperties HeaderTP = new TextProperties();
	private final TextProperties DesciptionTP = new TextProperties();

	private Text header;
	private Text description0;
	private Text description01;
	private Text description1;
	private Text description2;
	private Text description3;
	private Text description4;
	private Text description5;
	private Text description5_1;
	private Text description5_2;

	private Rect rahmen1;
	private Rect rahmen2;
	private Rect rahmen3;

	private SourceCode code;
	private SourceCodeProperties sourceCode = new SourceCodeProperties();

	private Language lang;
	private int arrayLength;
	private int[] a;
	private int[] m;
	private double fragenWahrscheinlichkeit;

	private double random;

	public void init() {
		lang = new AnimalScript("Chinesischer Restsatz [DE]","Emine Saracoglu, Felix Sternkopf", 800, 600);
		lang.setStepMode(true);

		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		if (arrayLength > 3) {

			/*
			 * Falls die angegebene Arraylaenge groesser als 3 ist wir eine
			 * Wahrnung ausgegeben,dass die Berechnung den Rahmen uebersprengt
			 */
			HeaderTP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
					"SansSerif", Font.PLAIN, 30));
			DesciptionTP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
					"SansSerif", Font.PLAIN, 18));

			RectRP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			RectRP.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(68,
					184, 165));
			RectRP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

			header = lang.newText(new Coordinates(60, 50), HEADER1, "Header",
					null, HeaderTP);
			rahmen1 = lang.newRect(new Offset(-5, -5, header, "NW"),
					new Offset(5, 5, header, "SE"), "Rahmen", null, RectRP);
			description0 = lang.newText(new Offset(20, 20, header, "SW"),
					DESCRIPTION0, "Description1", null);
			description0.setFont(new Font("Monospaced", Font.BOLD, 15), null,
					null);
			description0.changeColor("color", Color.RED, null, null);
			description01 = lang.newText(new Offset(0, 10, description0, "SW"),
					DESCRIPTION01, "Description1", null);
			description01.setFont(new Font("Monospaced", Font.BOLD, 15), null,
					null);
			description01.changeColor("color", Color.RED, null, null);
			return;

			/*
			 * Beschreibung des Algorithmuses
			 */
		} else {

			HeaderTP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
					"SansSerif", Font.PLAIN, 30));
			DesciptionTP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
					"SansSerif", Font.PLAIN, 18));

			RectRP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			RectRP.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(68,
					184, 165));
			RectRP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

			header = lang.newText(new Coordinates(60, 50), HEADER1, "Header",
					null, HeaderTP);
			rahmen1 = lang.newRect(new Offset(-5, -5, header, "NW"),
					new Offset(5, 5, header, "SE"), "Rahmen", null, RectRP);

			description1 = lang.newText(new Offset(0, 5, header, "SW"),
					DESCRIPTION1, "Description1", null);
			description2 = lang.newText(new Offset(0, 5, description1, "SW"),
					DESCRIPTION2, "Description2", null);
			description3 = lang.newText(new Offset(0, 5, description2, "SW"),
					DESCRIPTION3, "Description3", null);
			description4 = lang.newText(new Offset(0, 5, description3, "SW"),
					DESCRIPTION4, "Description4", null);

			description5 = lang.newText(new Offset(40, 20, description4, "SW"),
					DESCRIPTION5, "Description5", null);
			description5_1 = lang.newText(new Offset(0, 0, description5, "SW"),
					DESCRIPTION5_1, "Description5_1", null);
			description5_2 = lang.newText(
					new Offset(0, 0, description5_1, "SW"), DESCRIPTION5_2,
					"Description5_2", null);

			RectRP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			RectRP.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(235,
					153, 152));
			RectRP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
			description5.setFont(new Font("Monospaced", Font.BOLD, 13), null,
					null);
			description5_1.setFont(new Font("Monospaced", Font.BOLD, 13), null,
					null);
			description5_2.setFont(new Font("Monospaced", Font.BOLD, 13), null,
					null);

			rahmen2 = lang.newRect(new Offset(-8, -5, description5, "NW"),
					new Offset(40, 5, description5_2, "SE"), "Rahmen", null,
					RectRP);
		}
	}

	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
		arrayLength = (Integer) primitives.get("arrayLength");
        	sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
		a = (int[]) primitives.get("a");
		m = (int[]) primitives.get("m");
		fragenWahrscheinlichkeit = (Double) primitives.get("fragenWahrscheinlichkeit");

		init();

		/*
		 * Angaben fuer den Variablenfenster
		 */

		Variables var = lang.newVariables();
		var.declare("int", "arrayLength");
		var.set("arrayLength", Integer.toString(arrayLength));

		Variables var_m = lang.newVariables();
		var_m.declare("String", "m");
		var_m.set("m", ArrayToString(m));

		Variables var_a = lang.newVariables();
		var_a.declare("String", "a");
		var_a.set("a", ArrayToString(a));

		/*
		 * Aufruf der Hauptmethode
		 */
		ChinReSa();
		lang.finalizeGeneration();

		return lang.toString();
	}

	public String getName() {
		return "Chinesischer Restsatz [DE]";
	}

	public String getAlgorithmName() {
		return "Chinesischer Restsatz ";
	}

	public String getAnimationAuthor() {
		return "Emine Saracoglu, Felix Sternkopf";
	}

	public String getDescription() {
		return "Der Chinesische Restsatz trifft eine Aussage über simultane Kongruenzen für den Fall,"
				+ "\n"
				+ " dass die Divisoren teilerfremd sind. Seien m_1, ..., m_n paarweise teilerfremde natürliche"
				+ "\n"
				+ "Zahlen, dann existiert für jeden Tupel ganzer Zahlen a_1, ..., a_n eine ganze Zahl x,"
				+ "\n"
				+ "die die folgende simultane Kongruenz erfüllt:"
				+ "\n"
				+ "\n"
				+ "x  ≡ a_1 (mod m_1)"
				+ "\n"
				+ "\n"
				+ "..."
				+ "\n"
				+ "\n" + "x  ≡ a_n (mod m_n) "
				+ "\n" + "Hinweis: Während der Animation werden Fragen gestellt."
				+ "\n" + "Die Wahrscheinlichkeit kann zwischen 1.0 und 0.0 gewählt werden."
				+ "\n" + "Dabei entspicht die 1.0 einer 100%-Fragewahrscheinlichkeit. ";
	}

	public String getCodeExample() {
		return " 1. Überprüfung mit ggT, ob die Divisoren"
				+ "\n"
				+ "      paarweise teilerfremd sind:     m_1, ... , m_n"
				+ "\n"
				+ "  2. Die a_i Werte sind gegeben "
				+ "\n"
				+ "  3. Die Zahl M = m_1* ...* m_n berechnen."
				+ "\n"
				+ "  4. Die Zahlen M_1 = M/m_1, ... , M_n = M/m_n berechnen"
				+ "\n"
				+ "  5. Die Zahlen y_1, ... , y_n mit dem erweiterten Euklidischen Algorithmus berechnen:"
				+ "\n"
				+ "       y_i = t --> ggT(M_i, m_i) = (m_i * k) + (t* M_i) = 1"
				+ "\n" + "  6. Berechnen der gesuchten Zahl " + "\n"
				+ "       x' = (a_1* M_1* y_1)+...+(a_n * M_n * y_n)" + "\n"
				+ "  7. Die Zahl x = x' mod M berechnen" + "\n"
				+ "  8. Überprüfen des x Wertes";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}


	/*
	 * Pseudocode der waehrend der Schritte angezeigt und markiert wird zu den
	 * jeweiligen Schritten
	 */
	private void generateDescription() {

		code = lang.newSourceCode(new Coordinates(430, 80), "code", null,
				sourceCode);
		code.addCodeLine(
				"1.)  Überprüfung mit ggT, ob die Divisoren paarweise ", "", 0,
				null);
		code.addCodeLine("teilerfremd sind: m_1, ... , m_n", "", 2, null);
		code.addCodeLine(
				"2.)  Die a_i Werte sind wie auf der linken Seite gegeben ",
				"", 0, null);
		code.addCodeLine("3.)  Die Zahl M = m_1* ...* m_n berechnen.", "", 0,
				null);
		code.addCodeLine(
				"4.)  Die Zahlen M_1 = M/m_1, ... , M_n = M/m_n berechnen", "",
				0, null);
		code.addCodeLine("5.)  Die Zahlen y_1, ... , y_n mit dem", "", 0, null);
		code.addCodeLine(" erweiterten Euklidischen Algorithmus berechnen:",
				"", 2, null);

		code.addCodeLine(
				"y_i = t --> ggT(M_i, m_i) = (m_i * k) + (t* M_i) = 1", "", 2,
				null);
		code.addCodeLine("6.)  Berechnen der gesuchte Zahl ", "", 0, null);
		code.addCodeLine(" x' = (a_1*M_1*y_1)+...+(a_n*M_n*y_n)", "", 2, null);
		code.addCodeLine("7.) Die Zahl x = x' mod M berechnen", "", 0, null);
		code.addCodeLine("8.) Überprüfen des x Wertes", "", 0, null);
	}

	/*
	 * Verkettung von Zahlen in einem Array
	 */

	private String ArrayToString(int[] wQuer) {
		String erg = "" + wQuer[0];
		for (int i = 1; i < wQuer.length; i++) {
			erg = erg + "|" + wQuer[i];
		}
		return erg;
	}

	/*
	 * Berechnung des groeßten gemeinsamen Teilers, der fuer jeden Paar = 1 sein
	 * muss, damit der Chineische Restsatz funktioniert
	 */
	private int GGT(int modA, int modB) {
		int ggt = 0;
		int h = (modA > modB) ? modA : modB;
		ggt = h;

		while (ggt > 1) {
			if ((modA % ggt) == 0 && (modB % ggt) == 0) {
				return ggt;
			}
			ggt--;
		}
		return 1;
	}

	/*
	 * Berechnung der Zahl y_i mit dem erweiterten Euklidischen Algo, der sich
	 * aus (a*k)+(b*l)=1 ergibt: y_i = l_i
	 */
	private int EEA(int Mi, int mi) {
		int rest;
		int k = 0;
		int zwSpL = k;
		int t = 1;
		int zwA = Mi;
		int zwB = mi;
		ArrayList<Integer> q = new ArrayList<Integer>();

		while (zwB > 0) {
			rest = zwA % zwB;
			q.add(zwA / zwB);
			zwA = zwB;
			zwB = rest;
		}
		for (int j = q.size() - 1; j >= 0; j--) {
			k = t - (q.get(j) * k);
			t = zwSpL;
			zwSpL = k;
		}
		return t;
	}

	public void ChinReSa() {
		/*
		 * Initialisierung der Zwischenparameter, die fuer die Nebenrechnungen
		 * benoetigt werden
		 */
		int x = 0;
		int M = 1;
		int[] y = new int[arrayLength];
		int[] M_i = new int[arrayLength];
		lang.nextStep("Einleitung");

		/*
		 * Erstellen der Felder fuer die Berechnungen und die Zwischenergebnisse
		 */

		description1 = lang.newText(new Offset(5, 80, description5_2, "SW"),
				FORMEL1, "Beispiel", null);
		description1.setFont(new Font("Monospaced", Font.CENTER_BASELINE, 13),
				null, null);

		String[] zahlenBeispiel = new String[arrayLength];
		Text[] ZahlenBeispiel = new Text[arrayLength];

		for (int i = 0; i < zahlenBeispiel.length; i++) {
			zahlenBeispiel[i] = "x \u2261 " + a[i] + " ( mod " + m[i] + ")";
			if (i == 0) {
				ZahlenBeispiel[i] = lang.newText(new Offset(20, 10,
						description1, "SW"), zahlenBeispiel[i],
						"ZahlenBeispiel", null);
				ZahlenBeispiel[i].setFont(
						new Font("Monospaced", Font.BOLD, 13), null, null);
			} else {
				ZahlenBeispiel[i] = lang.newText(new Offset(0, 10,
						ZahlenBeispiel[i - 1], "SW"), zahlenBeispiel[i],
						"ZahlenBeispiel", null);
				ZahlenBeispiel[i].setFont(
						new Font("Monospaced", Font.BOLD, 13), null, null);
			}

		}

		RectRP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		RectRP.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(245, 224,
				120));
		RectRP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		rahmen3 = lang.newRect(new Offset(-8, -5, description1, "NW"),
				new Offset(120, 5, ZahlenBeispiel[arrayLength - 1], "SE"),
				"Rahmen", null, RectRP);

		lang.hideAllPrimitives();

		RectRP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		RectRP.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(68, 184,
				165));
		RectRP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		header = lang.newText(new Coordinates(60, 50), HEADER1, "Header", null,
				HeaderTP);
		rahmen1 = lang.newRect(new Offset(-5, -5, header, "NW"), new Offset(5,
				5, header, "SE"), "Rahmen", null, RectRP);

		RectRP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		RectRP.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(235, 153,
				152));
		RectRP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		description5 = lang.newText(new Offset(40, 100, header, "SW"),
				DESCRIPTION5, "Description5", null);
		description5_1 = lang.newText(new Offset(0, 0, description5, "SW"),
				DESCRIPTION5_1, "Description5_1", null);
		description5_2 = lang.newText(new Offset(0, 0, description5_1, "SW"),
				DESCRIPTION5_2, "Description5_2", null);
		description5.setFont(new Font("Monospaced", Font.BOLD, 13), null, null);
		description5_1.setFont(new Font("Monospaced", Font.BOLD, 13), null,
				null);
		description5_2.setFont(new Font("Monospaced", Font.BOLD, 13), null,
				null);

		rahmen2 = lang.newRect(new Offset(-8, -5, description5, "NW"),
				new Offset(40, 5, description5_2, "SE"), "Rahmen2", null,
				RectRP);

		description1 = lang.newText(new Offset(5, 80, description5_2, "SW"),
				FORMEL1, "Beispiel", null);
		description1.setFont(new Font("Monospaced", Font.CENTER_BASELINE, 13),
				null, null);

		for (int i = 0; i < zahlenBeispiel.length; i++) {
			zahlenBeispiel[i] = "x \u2261 " + a[i] + " ( mod " + m[i] + ")";
			if (i == 0) {
				ZahlenBeispiel[i] = lang.newText(new Offset(20, 10,
						description1, "SW"), zahlenBeispiel[i],
						"ZahlenBeispiel", null);
				ZahlenBeispiel[i].setFont(
						new Font("Monospaced", Font.BOLD, 13), null, null);
			} else {
				ZahlenBeispiel[i] = lang.newText(new Offset(0, 10,
						ZahlenBeispiel[i - 1], "SW"), zahlenBeispiel[i],
						"ZahlenBeispiel", null);
				ZahlenBeispiel[i].setFont(
						new Font("Monospaced", Font.BOLD, 13), null, null);
			}

		}
		RectRP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		RectRP.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(245, 224,
				120));
		RectRP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		rahmen3 = lang.newRect(new Offset(-8, -5, description1, "NW"),
				new Offset(120, 5, ZahlenBeispiel[arrayLength - 1], "SE"),
				"Rahmen", null, RectRP);

		Node pos1Line1 = new Coordinates(0, 210);
		Node pos2Line1 = new Coordinates(0, 120);
		Node[] nodes1 = { pos1Line1, pos2Line1 };
		Polyline line1 = lang.newPolyline(nodes1, "line1", null);
		line1.hide();

		Node pos1Description5 = new Coordinates(0, 250);
		Node pos2Description5 = new Coordinates(0, 160);
		Node[] nodesDescription = { pos1Description5, pos2Description5 };
		Polyline lineDescription5 = lang.newPolyline(nodesDescription, "line1",
				null);
		lineDescription5.hide();

		Node pos1Line2 = new Coordinates(0, 300);
		Node pos2Line2 = new Coordinates(0, 160);
		Node[] nodes2 = { pos1Line2, pos2Line2 };
		Polyline line2 = lang.newPolyline(nodes2, "line1", null);
		line2.hide();

		Node pos1Description1 = new Coordinates(0, 300);
		Node pos2Description1 = new Coordinates(0, 160);
		Node[] nodesDescription1 = { pos1Description1, pos2Description1 };
		Polyline lineDescription1 = lang.newPolyline(nodesDescription1,
				"line1", null);
		lineDescription5.hide();

		Node pos1Formel1 = new Coordinates(0, 300);
		Node pos2Formel1 = new Coordinates(0, 160);
		Node[] nodesFormel1 = { pos1Formel1, pos2Formel1 };
		Polyline lineFormel1 = lang.newPolyline(nodesFormel1, "line1", null);
		lineFormel1.hide();

		Node pos1Formel2 = new Coordinates(0, 300);
		Node pos2Formel2 = new Coordinates(0, 160);
		Node[] nodesFormel2 = { pos1Formel2, pos2Formel2 };
		Polyline lineFormel2 = lang.newPolyline(nodesFormel2, "line1", null);
		lineFormel2.hide();

		Node pos1Formel3 = new Coordinates(0, 300);
		Node pos2Formel3 = new Coordinates(0, 160);
		Node[] nodesFormel3 = { pos1Formel3, pos2Formel3 };
		Polyline lineFormel3 = lang.newPolyline(nodesFormel3, "line1", null);
		lineFormel3.hide();

		rahmen2.moveVia("S", "translate", line1, new TicksTiming(50),
				new TicksTiming(100));
		description5.moveVia("S", "translate", lineDescription5,
				new TicksTiming(50), new TicksTiming(100));
		description5_1.moveVia("S", "translate", lineDescription5,
				new TicksTiming(50), new TicksTiming(100));
		description5_2.moveVia("S", "translate", lineDescription5,
				new TicksTiming(50), new TicksTiming(100));
		rahmen3.moveVia("S", "translate", line2, new TicksTiming(50),
				new TicksTiming(100));
		description1.moveVia("S", "translate", lineDescription1,
				new TicksTiming(50), new TicksTiming(100));

		for (int i = 0; i < ZahlenBeispiel.length; i++) {
			ZahlenBeispiel[i].moveVia("S", "translate", lineFormel1,
					new TicksTiming(50), new TicksTiming(100));
		}

		lang.nextStep();

		generateDescription();

		description2 = lang.newText(new Coordinates(25, 305), RECHNUNG,
				"Rechnung", null);

		description3 = lang.newText(new Coordinates(505, 305),
				ZWISCHENERGEBNIS, "Zwischenergebnisse", null);

		description2.setFont(new Font("Monospaced", Font.CENTER_BASELINE, 18),
				null, null);
		description3.setFont(new Font("Monospaced", Font.CENTER_BASELINE, 18),
				null, null);

		RectRP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		RectRP.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(204, 188,
				204));
		RectRP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		Rect rahmenBerechnung = lang.newRect(new Coordinates(20, 300),
				new Coordinates(550, 620), "rahmenBerechnung", null, RectRP);

		Rect rahmenZwErgebnisse = lang.newRect(new Coordinates(500, 300),
				new Coordinates(750, 620), "rahmenZwErgebnisse", null, RectRP);

		lang.nextStep();

		String ggt_erkl = "Der Chinesische Restsatz setzt voraus, dass alle";
		String ggt_erkl1 = "Divisoren (m_1, ... , m_n) paarweise teilerfremd sind:";

		Text ggT_erkl = lang.newText(new Offset(20, 20, description2, "SW"),
				ggt_erkl, "ggT_erkl", null);
		ggT_erkl.setFont(new Font("Monospaced", Font.BOLD, 12), null, null);
		Text ggT_erkl1 = lang.newText(new Offset(0, 10, ggT_erkl, "SW"),
				ggt_erkl1, "ggT_erkl1", null);
		ggT_erkl1.setFont(new Font("Monospaced", Font.BOLD, 12), null, null);
		lang.nextStep("Überprüfung");

		code.highlight(0);
		code.highlight(1);

		String falseNumber;
		int k = arrayLength;
		String[] var = new String[k];
		int nextggTright = 20;
		int nextggTDown = 20;
		Text[] falseNum = new Text[arrayLength];
		Text[] rightNum = new Text[arrayLength];

		for (int i = 0; i < m.length - 1; i++) {
			for (int j = i + 1; j < m.length; j++) {
				if (GGT(m[i], m[j]) == 1) {

					var[k - 1] = "ggT(" + m[i] + "," + m[j] + ") = "
							+ GGT(m[i], m[j]);

					rightNum[k - 1] = lang.newText(new Offset(nextggTright,
							nextggTDown, ggT_erkl1, "SW"), var[k - 1],
							"ggtistEins", null);
					rightNum[k - 1].setFont(new Font("Monospaced", Font.BOLD,
							14), null, null);

					nextggTDown = nextggTDown + 40;
					lang.nextStep();
				} else {

					falseNumber = "ggT(" + m[i] + "," + m[j] + ") = "
							+ GGT(m[i], m[j]);
					falseNum[k - 1] = lang.newText(new Offset(nextggTright,
							nextggTDown, ggT_erkl1, "SW"), falseNumber,
							"ggtNichtEins", null);
					falseNum[k - 1].setFont(new Font("Monospaced", Font.BOLD,
							14), null, null);
					nextggTDown = nextggTDown + 40;
					description4 = lang.newText(new Offset(0, 5,
							falseNum[k - 1], "SW"), DESCRIPTION6,
							"Description6", null);
					description4.setFont(new Font("Monospaced", Font.BOLD, 14),
							null, null);
					description4.changeColor("color", Color.RED, null, null);
					description5 = lang.newText(new Offset(0, 5, description4,
							"SW"), DESCRIPTION7, "Description7", null);
					description5.setFont(new Font("Monospaced", Font.BOLD, 14),
							null, null);
					description5.changeColor("color", Color.RED, null, null);
					return;
				}
				k--;
			}
		}

		ggT_erkl.hide();
		ggT_erkl1.hide();

		if ((arrayLength % 2) == 0) {

			for (int i = 1; i < rightNum.length; i++) {
				rightNum[i].hide();
			}
		} else {
			for (int i = 0; i < rightNum.length; i++) {
				rightNum[i].hide();
			}
		}
		code.unhighlight(0);
		code.unhighlight(1);

		String allg_x_formel = "um das Problem simultaner Kongruenzen lösen zu können,";
		String allg_x_formel1 = "muss eine Zahl x berechnet werden:";

		Text allg_X_formel = lang.newText(
				new Offset(20, 20, description2, "SW"), allg_x_formel,
				"allg_X_formel", null);
		allg_X_formel
				.setFont(new Font("Monospaced", Font.BOLD, 12), null, null);

		Text allg_X_formel1 = lang.newText(new Offset(0, 10, allg_X_formel,
				"SW"), allg_x_formel1, "allg_X_formel1", null);
		allg_X_formel1.setFont(new Font("Monospaced", Font.BOLD, 12), null,
				null);

		lang.nextStep();

		/*
		 * Frage 1 zum Algorithmus
		 */

		random = Math.random();
		if(random <= fragenWahrscheinlichkeit){

		FillInBlanksQuestionModel chinReSaBerechnet = new FillInBlanksQuestionModel(
				"calculate");
		chinReSaBerechnet
				.setPrompt("Der Chinesische Restsatz berechnet __________________");
		chinReSaBerechnet.addAnswer("simultane Kongruenzen", 1, "");
		lang.addFIBQuestion(chinReSaBerechnet);
		}

	
		String x_frml0 = "x = (";
		String x_frml01 = "(";
		String x_frml1 = "a_1 ";
		String x_frml2 = "* ";
		String x_frml3 = "M_1 ";
		String x_frml4 = "* ";
		String x_frml5 = "y_1";
		String x_frml6 = ")";
		String x_frml7 = " +...+ ";
		String x_frml8 = "(";
		String x_frml9 = "a_n ";
		String x_frml10 = "* ";
		String x_frml11 = "M_n ";
		String x_frml12 = "* ";
		String x_frml13 = "y_n";
		String x_frml14 = ")";
		String x_frml14_1 = ") (mod ";
		String x_frml15 = "M";
		String x_frml16 = ")";

		Text x_Frml0 = lang.newText(new Offset(0, 20, allg_X_formel1, "SW"),
				x_frml0, "x_Frml0", null);
		x_Frml0.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
		Text x_Frml01 = lang.newText(new Offset(40, 20, allg_X_formel1, "SW"),
				x_frml01, "x_Frml01", null);
		x_Frml01.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
		Text x_Frml1 = lang.newText(new Offset(50, 20, allg_X_formel1, "SW"),
				x_frml1, "x_Frml1", null);
		x_Frml1.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
		Text x_Frml2 = lang.newText(new Offset(80, 20, allg_X_formel1, "SW"),
				x_frml2, "x_Frml2", null);
		x_Frml2.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
		Text x_Frml3 = lang.newText(new Offset(95, 20, allg_X_formel1, "SW"),
				x_frml3, "x_Frml3", null);
		x_Frml3.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
		Text x_Frml4 = lang.newText(new Offset(125, 20, allg_X_formel1, "SW"),
				x_frml4, "x_Frml4", null);
		x_Frml4.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
		Text x_Frml5 = lang.newText(new Offset(140, 20, allg_X_formel1, "SW"),
				x_frml5, "x_Frml5", null);
		x_Frml5.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
		Text x_Frml6 = lang.newText(new Offset(165, 20, allg_X_formel1, "SW"),
				x_frml6, "x_Frml6", null);
		x_Frml6.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
		Text x_Frml7 = lang.newText(new Offset(175, 20, allg_X_formel1, "SW"),
				x_frml7, "x_Frml7", null);
		x_Frml7.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
		Text x_Frml8 = lang.newText(new Offset(230, 20, allg_X_formel1, "SW"),
				x_frml8, "x_Frml8", null);
		x_Frml8.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
		Text x_Frml9 = lang.newText(new Offset(240, 20, allg_X_formel1, "SW"),
				x_frml9, "x_Frml9", null);
		x_Frml9.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
		Text x_Frml10 = lang.newText(new Offset(270, 20, allg_X_formel1, "SW"),
				x_frml10, "x_Frml10", null);
		x_Frml10.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
		Text x_Frml11 = lang.newText(new Offset(285, 20, allg_X_formel1, "SW"),
				x_frml11, "x_Frml11", null);
		x_Frml11.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
		Text x_Frml12 = lang.newText(new Offset(315, 20, allg_X_formel1, "SW"),
				x_frml12, "x_Frml12", null);
		x_Frml12.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
		Text x_Frml13 = lang.newText(new Offset(330, 20, allg_X_formel1, "SW"),
				x_frml13, "x_Frml13", null);
		x_Frml13.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
		Text x_Frml14 = lang.newText(new Offset(355, 20, allg_X_formel1, "SW"),
				x_frml14, "x_Frml14", null);
		x_Frml14.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
		Text x_Frml14_1 = lang.newText(
				new Offset(365, 20, allg_X_formel1, "SW"), x_frml14_1,
				"x_Frml14_1", null);
		x_Frml14_1.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
		Text x_Frml15 = lang.newText(new Offset(415, 20, allg_X_formel1, "SW"),
				x_frml15, "x_Frml15", null);
		x_Frml15.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
		Text x_Frml16 = lang.newText(new Offset(425, 20, allg_X_formel1, "SW"),
				x_frml16, "x_Frml16", null);
		x_Frml16.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);

		lang.nextStep("Formel für die Berechnung");

		x_Frml1.changeColor("color", Color.CYAN, null, null);
		x_Frml9.changeColor("color", Color.CYAN, null, null);
		code.highlight(2);

		lang.nextStep();

		String[] a_ist = new String[arrayLength];
		Text[] a_Ist = new Text[arrayLength];
		int a_zaehler = 20;

		for (int i = 0; i < a_ist.length; i++) {
			a_ist[i] = "a_" + (i + 1) + " = " + a[i];
			a_Ist[i] = lang.newText(new Offset(20, a_zaehler, x_Frml0, "SW"),
					a_ist[i], "a_i", null);
			a_Ist[i].setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
			a_zaehler = a_zaehler + 20;
		}

		lang.nextStep("1.Berechnung");
		for (int i = 0; i < y.length; i++) {
			a_Ist[i].hide();
		}

		a_zaehler = 20;

		for (int i = 0; i < y.length; i++) {
			a_Ist[i] = lang.newText(new Offset(10, a_zaehler, description3,
					"SW"), a_ist[i], "a_Ist", null);
			a_Ist[i].setFont(new Font("Monospaced", Font.BOLD, 12), null, null);
			a_zaehler = a_zaehler + 20;

		}

		x_Frml1.changeColor("color", Color.BLACK, null, null);
		x_Frml9.changeColor("color", Color.BLACK, null, null);

		x_Frml15.changeColor("color", Color.CYAN, null, null);

		code.unhighlight(2);
		code.highlight(3);
		lang.nextStep();

		sourceCode.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				Color.RED);

		code.unhighlight(2);

		String rechneM = "M = m_1 * ... * m_n";

		Text rechneMwert = lang.newText(new Offset(20, 20, x_Frml0, "SW"),
				rechneM, "rechneMwert", null);
		rechneMwert.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);

		lang.nextStep();

		for (int i = 0; i < m.length; i++) {
			M = M * m[i];
		}

		String wertMist = null;

		for (int i = 0; i < arrayLength; i++) {
			if (i == 0) {
				wertMist = "M = " + m[i];
			}
			if ((i != 0) && (i != (arrayLength - 1))) {
				wertMist = wertMist + " * " + m[i];
			}
			if (i == arrayLength - 1) {
				wertMist = wertMist + " * " + m[i] + " = " + M;
			}
		}

		Variables varM = lang.newVariables();
		varM.declare("int", "M");
		varM.set("M", Integer.toString(M));

		Text wertM = lang.newText(new Offset(20, 20, rechneMwert, "SW"),
				wertMist, "wertM", null);
		wertM.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);

		lang.nextStep("2.Berechnung");
		x_Frml15.changeColor("color", Color.BLACK, null, null);
		x_Frml3.changeColor("color", Color.CYAN, null, null);
		x_Frml11.changeColor("color", Color.CYAN, null, null);

		rechneMwert.hide();
		wertM.hide();

		code.unhighlight(3);
		code.highlight(4);

		wertMist = "M = " + M;
		wertM = lang.newText(new Offset(0, 10, a_Ist[arrayLength - 1], "SW"),
				wertMist, "wertM", null);
		wertM.setFont(new Font("Monospaced", Font.BOLD, 12), null, null);

		lang.nextStep();
		String M_iFormel = "M_i = M / m_i";
		Text M_iFrml = lang.newText(new Offset(20, 20, x_Frml0, "SW"),
				M_iFormel, "M_iFrml", null);
		M_iFrml.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);

		lang.nextStep();
		String[] M_iBerechnen = new String[arrayLength];
		Text[] M_ist = new Text[arrayLength];
		int M_iAbstand = 20;

		for (int i = 0; i < M_i.length; i++) {
			M_i[i] = M / m[i];
		}

		for (int i = 0; i < M_i.length; i++) {
			M_iBerechnen[i] = "M_" + (i + 1) + " : " + M_i[i] + " = " + M
					+ " / " + m[i];
		}

		for (int i = 0; i < M_i.length; i++) {
			M_ist[i] = lang.newText(new Offset(20, M_iAbstand, M_iFrml, "SW"),
					M_iBerechnen[i], "M_i_rechner", null);
			M_ist[i].setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
			M_iAbstand = M_iAbstand + 40;
		}

		lang.nextStep("3.Berechnung");

		/*
		 * Frage 2 zum Algorithmus
		 */
		 random = Math.random();
		if(random <= fragenWahrscheinlichkeit){
		TrueFalseQuestionModel chinReSaBerechnet1 = new TrueFalseQuestionModel(
				"Voraussetzung", false, 1);
		chinReSaBerechnet1
				.setPrompt("Die Vorraussetzung für den Chinesischen Restsatz ist, dass mindestens ein ggT(m_i,m_j) = 1 sein muss?");
		lang.addTFQuestion(chinReSaBerechnet1);
		}

		code.unhighlight(4);
		code.highlight(5);
		code.highlight(6);
		code.highlight(7);

		Variables var_Mi = lang.newVariables();
		var_Mi.declare("String", "Mi");
		var_Mi.set("Mi", ArrayToString(M_i));

		int zahlerfürM_i = 20;
		M_iFrml.hide();
		String[] MiErgebnis = new String[arrayLength];

		for (int i = 0; i < rightNum.length; i++) {
			M_ist[i].hide();
		}

		for (int i = 0; i < M_i.length; i++) {
			MiErgebnis[i] = "M_" + (i + 1) + " = " + M_i[i];
		}

		for (int i = 0; i < M_i.length; i++) {
			M_ist[i] = lang.newText(new Offset(0, zahlerfürM_i, wertM, "SW"),
					MiErgebnis[i], "M_formel", null);
			M_ist[i].setFont(new Font("Monospaced", Font.BOLD, 12), null, null);
			zahlerfürM_i = zahlerfürM_i + 20;

		}
		x_Frml5.changeColor("color", Color.CYAN, null, null);
		x_Frml13.changeColor("color", Color.CYAN, null, null);
		x_Frml3.changeColor("color", Color.BLACK, null, null);
		x_Frml11.changeColor("color", Color.BLACK, null, null);

		lang.nextStep();

		String eeA_l1 = "ggT(M_1 , m_1) = (k * m_1)+(t * M_1) = 1,";
		String eeA_l2 = "   (dabei ist t = y_1) ";
		String eeA_l3 = "...";
		String eeA_l4 = "ggT(M_n , m_n) = (k * m_n)+(t * M_n) = 1 ";
		String eeA_l5 = "   (dabei ist t = y_n)";

		Text eeA_L1 = lang.newText(new Offset(20, 30, x_Frml0, "SW"), eeA_l1,
				"eeA_L1", null);
		eeA_L1.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);

		Text eeA_L2 = lang.newText(new Offset(0, 10, eeA_L1, "SW"), eeA_l2,
				"eeA_L2", null);
		eeA_L2.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);

		eeA_L2.changeColor("color", Color.RED, null, null);

		Text eeA_L3 = lang.newText(new Offset(0, 10, eeA_L2, "SW"), eeA_l3,
				"eeA_L3", null);
		eeA_L3.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);

		Text eeA_L4 = lang.newText(new Offset(0, 10, eeA_L3, "SW"), eeA_l4,
				"eeA_L4", null);
		eeA_L4.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);

		Text eeA_L5 = lang.newText(new Offset(0, 10, eeA_L4, "SW"), eeA_l5,
				"eeA_L5", null);
		eeA_L5.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
		eeA_L5.changeColor("color", Color.RED, null, null);

		lang.nextStep();
		eeA_L1.hide();
		eeA_L2.hide();
		eeA_L3.hide();
		eeA_L4.hide();
		eeA_L5.hide();

		String[] y_i = new String[arrayLength];
		Text[] y_ist = new Text[arrayLength];
		int y_zahler = 20;
		String[] y_Wert = new String[arrayLength];
		Text[] y_wert = new Text[arrayLength];

		for (int i = 0; i < arrayLength; i++) {
			y[i] = EEA(M_i[i], m[i]);
		}

		for (int i = 0; i < y.length; i++) {
			y_i[i] = "ggT(" + M_i[i] + " , " + m[i] + ") = " + "(k * " + m[i]
					+ ") + (" + y[i] + " * " + M_i[i] + ") = 1";
			y_ist[i] = lang.newText(new Offset(20, y_zahler, x_Frml0, "SW"),
					y_i[i], "y_i", null);
			y_ist[i].setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
			y_zahler = y_zahler + 20;
			y_Wert[i] = "--> y_" + (i + 1) + " = " + y[i];
			y_wert[i] = lang.newText(new Offset(20, y_zahler, x_Frml0, "SW"),
					y_Wert[i], "y_wert", null);
			y_wert[i]
					.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
			y_wert[i].changeColor("color", Color.RED, null, null);

			y_zahler = y_zahler + 20;

		}

		lang.nextStep("4.Berechnung");
		int y_zwErg = 20;

		Variables var_yi = lang.newVariables();
		var_yi.declare("String", "yi");
		var_yi.set("yi", ArrayToString(y));

		for (int i = 0; i < y.length; i++) {
			y_ist[i].hide();
			y_wert[i].hide();
		}

		for (int i = 0; i < y.length; i++) {
			y_Wert[i] = "y_" + (i + 1) + " = " + y[i];

			y_wert[i] = lang.newText(new Offset(0, y_zwErg,
					M_ist[arrayLength - 1], "SW"), y_Wert[i], "y_wert", null);
			y_wert[i]
					.setFont(new Font("Monospaced", Font.BOLD, 12), null, null);
			y_zwErg = y_zwErg + 20;

		}

		code.unhighlight(5);
		code.unhighlight(6);
		code.unhighlight(7);
		code.highlight(8);
		code.highlight(9);
		x_Frml01.changeColor("color", Color.CYAN, null, null);
		x_Frml1.changeColor("color", Color.CYAN, null, null);
		x_Frml2.changeColor("color", Color.CYAN, null, null);
		x_Frml3.changeColor("color", Color.CYAN, null, null);
		x_Frml4.changeColor("color", Color.CYAN, null, null);
		x_Frml5.changeColor("color", Color.CYAN, null, null);
		x_Frml6.changeColor("color", Color.CYAN, null, null);
		x_Frml7.changeColor("color", Color.CYAN, null, null);
		x_Frml8.changeColor("color", Color.CYAN, null, null);
		x_Frml9.changeColor("color", Color.CYAN, null, null);
		x_Frml10.changeColor("color", Color.CYAN, null, null);
		x_Frml11.changeColor("color", Color.CYAN, null, null);
		x_Frml12.changeColor("color", Color.CYAN, null, null);
		x_Frml13.changeColor("color", Color.CYAN, null, null);
		x_Frml14.changeColor("color", Color.CYAN, null, null);

		lang.nextStep();
		String x_zwE = null;

		for (int i = 0; i < arrayLength; i++) {
			if (i == 0) {
				x_zwE = "x' = " + "(" + a[i] + " * " + M_i[i] + " * " + y[i]
						+ ") +";
			} else if (i > 0 && i < (arrayLength - 1)) {
				x_zwE = x_zwE + "(" + a[i] + " * " + M_i[i] + " * " + y[i]
						+ ") +";
			} else {
				x_zwE = x_zwE + "(" + a[i] + " * " + M_i[i] + " * " + y[i]
						+ ")";
			}
		}
		Text x_ZwE = lang.newText(new Offset(0, 20, x_Frml0, "SW"), x_zwE,
				"x_ZwE", null);
		x_ZwE.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);

		lang.nextStep();

		for (int j = 0; j < m.length; j++) {
			x = x + (M_i[j] * a[j] * y[j]);
		}

		int xtmp = x;
		String x_rechnen = "x' = " + x;
		Text x_Rechnen = lang.newText(new Offset(0, 20, x_ZwE, "SW"),
				x_rechnen, "x", null);
		x_Rechnen.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);

		lang.nextStep();
		x_ZwE.hide();
		x_Rechnen.hide();

		x_Rechnen = lang.newText(new Offset(0, 20, y_wert[arrayLength - 1],
				"SW"), x_rechnen, "x", null);
		x_Rechnen.setFont(new Font("Monospaced", Font.BOLD, 12), null, null);

		code.unhighlight(8);
		code.unhighlight(9);
		code.highlight(10);

		x_Frml01.changeColor("color", Color.BLACK, null, null);
		x_Frml1.changeColor("color", Color.BLACK, null, null);
		x_Frml2.changeColor("color", Color.BLACK, null, null);
		x_Frml3.changeColor("color", Color.BLACK, null, null);
		x_Frml4.changeColor("color", Color.BLACK, null, null);
		x_Frml5.changeColor("color", Color.BLACK, null, null);
		x_Frml6.changeColor("color", Color.BLACK, null, null);
		x_Frml7.changeColor("color", Color.BLACK, null, null);
		x_Frml8.changeColor("color", Color.BLACK, null, null);
		x_Frml9.changeColor("color", Color.BLACK, null, null);
		x_Frml10.changeColor("color", Color.BLACK, null, null);
		x_Frml11.changeColor("color", Color.BLACK, null, null);
		x_Frml12.changeColor("color", Color.BLACK, null, null);
		x_Frml13.changeColor("color", Color.BLACK, null, null);
		x_Frml14.changeColor("color", Color.BLACK, null, null);

		String x_EndErgFrml = "x = x' (mod M)";
		Text xEndErgFormel = lang.newText(new Offset(20, 20, x_Frml0, "SW"),
				x_EndErgFrml, "xFrml", null);
		xEndErgFormel
				.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);

		lang.nextStep();

		if (x >= 0) {
			x = x % M;
		} else {
			while (x < 0) {
				x = x + M;
			}
		}

		String x_erg = "x = " + x + " \u2261 " + xtmp + " mod " + M
				+ "  --> x = " + x;
		Text x_Erg = lang.newText(new Offset(0, 20, xEndErgFormel, "SW"),
				x_erg, "x_Erg", null);
		x_Erg.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);

		xtmp = x;
		lang.nextStep();
		x_Erg.hide();
		xEndErgFormel.hide();

		x_Erg = lang.newText(new Offset(20, 20, x_Frml0, "SW"), x_erg, "x_Erg",
				null);
		x_Erg.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);

		RectRP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		RectRP.set(AnimationPropertiesKeys.FILL_PROPERTY,
				new Color(255, 234, 0));
		RectRP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		String x_ist_End = " x = " + x;
		Text x_ist_end = lang.newText(new Offset(50, 30, x_Erg, "SW"),
				x_ist_End, "x_ist_end", null);
		x_ist_end.setFont(new Font("Monospaced", Font.BOLD, 18), null, null);
		x_ist_end.changeColor("color", Color.BLUE, null, null);
		rahmen3 = lang.newRect(new Offset(0, -5, x_ist_end, "NW"), new Offset(
				50, 5, x_ist_end, "SE"), "Rahmen3", null, RectRP);

		lang.nextStep();

		/*
		 * Frage 3 zum Algorithmus
		 */
		 random = Math.random();
		if(random <= fragenWahrscheinlichkeit){
		TrueFalseQuestionModel chinReSaBerechnet2 = new TrueFalseQuestionModel(
				"RechnungMi", true, 1);
		chinReSaBerechnet2
				.setPrompt("Die Zahlen M_i lassen sich mit M/m_i berechnen");
		lang.addTFQuestion(chinReSaBerechnet2);
		}

		allg_X_formel.hide();
		allg_X_formel1.hide();
		x_ist_end.hide();
		x_Erg.hide();
		code.unhighlight(10);
		code.highlight(11);

		x_Frml0.hide();
		x_Frml01.hide();
		x_Frml1.hide();
		x_Frml2.hide();
		x_Frml3.hide();
		x_Frml4.hide();
		x_Frml5.hide();
		x_Frml6.hide();
		x_Frml7.hide();
		x_Frml8.hide();
		x_Frml9.hide();
		x_Frml10.hide();
		x_Frml11.hide();
		x_Frml12.hide();
		x_Frml13.hide();
		x_Frml14.hide();
		x_Frml15.hide();
		x_Frml16.hide();
		x_Frml14_1.hide();
		rahmen3.hide();

		String ergPrf = "x Wert Überprüfen mit --> x \u2261 a_i (mod m_i): ";
		Text ergpf = lang.newText(new Offset(20, 20, description2, "SW"),
				ergPrf, "ergpf", null);
		ergpf.setFont(new Font("Monospaced", Font.BOLD, 12), null, null);

		x_ist_end = lang.newText(new Offset(50, 30, ergpf, "SW"), x_ist_End,
				"x_ist_end", null);
		x_ist_end.setFont(new Font("Monospaced", Font.BOLD, 18), null, null);
		x_ist_end.changeColor("color", Color.BLUE, null, null);
		rahmen3 = lang.newRect(new Offset(0, -5, x_ist_end, "NW"), new Offset(
				50, 5, x_ist_end, "SE"), "Rahmen3", null, RectRP);

		String korrekt = "korrekt";
		Text korreKt = null;
		String[] ueberprfn = new String[arrayLength];
		Text[] uberPrf = new Text[arrayLength];
		int x_zahelen = 20;

		lang.nextStep("Endergebnis");

		for (int i = 0; i < m.length; i++) {
			if(i<2){
			ueberprfn[i] = xtmp + " \u2261 " + a[i] + "( mod " + m[i] + ")";

			uberPrf[i] = lang.newText(
					new Offset(0, x_zahelen, x_ist_end, "SW"), ueberprfn[i],
					"uberPrf", null);
			uberPrf[i].setFont(new Font("Monospaced", Font.BOLD, 14), null,
					null);

			korreKt = lang.newText(new Offset(150, x_zahelen, x_ist_end, "SW"),
					korrekt, "Korrekt", null);
			korreKt.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
			x_zahelen = x_zahelen + 20;
			lang.nextStep("Fazit");
			}
			else{
			ueberprfn[i] = xtmp + " \u2261 " + a[i] + "( mod " + m[i] + ")";

			uberPrf[i] = lang.newText(
					new Offset(0, x_zahelen, x_ist_end, "SW"), ueberprfn[i],
					"uberPrf", null);
			uberPrf[i].setFont(new Font("Monospaced", Font.BOLD, 14), null,
					null);

			korreKt = lang.newText(new Offset(150, x_zahelen, x_ist_end, "SW"),
					korrekt, "Korrekt", null);
			korreKt.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
			x_zahelen = x_zahelen + 20;
			lang.nextStep("Fazit");
			}
		}



		/*
		 * Frage 4 zum Algorithmus
		 */
		 random = Math.random();
		if(random <= fragenWahrscheinlichkeit){
		FillInBlanksQuestionModel chinReSaBerechnet4 = new FillInBlanksQuestionModel(
				"yistT");
		chinReSaBerechnet4
				.setPrompt("Welche Zahl stellt beim Erweiterten Euklidischen ALgorithmus die Zahl y_i dar? (ggT(M_i, m_i) = (m_i * k) + (t * M_i) = 1)");
		chinReSaBerechnet4.addAnswer("t", 1, "-korrekt");
		lang.addFIBQuestion(chinReSaBerechnet4);
		}

		lang.hideAllPrimitives();

		HeaderTP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.PLAIN, 30));
		DesciptionTP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.PLAIN, 18));

		RectRP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		RectRP.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(68, 184,
				165));
		RectRP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		header = lang.newText(new Coordinates(60, 50), HEADER1, "Header", null,
				HeaderTP);
		rahmen1 = lang.newRect(new Offset(-5, -5, header, "NW"), new Offset(5,
				5, header, "SE"), "Rahmen", null, RectRP);

		String fazit = "--> Der Chinesische Restsatz wird hauptsächlich in der ";
		String fazit1 = "Kryptographie für den RSA-Algorithmus verwendet, ";
		String fazit3 = "um die Berechnung zu beschleunigen.";

		Text Fazit = lang.newText(new Offset(0, 20, header, "SW"), fazit,
				"Fazit", null);
		Fazit.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);

		Text Fazit1 = lang.newText(new Offset(0, 10, Fazit, "SW"), fazit1,
				"Fazitq", null);
		Fazit1.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);
		Text Fazit3 = lang.newText(new Offset(0, 10, Fazit1, "SW"), fazit3,
				"Fazit3", null);
		Fazit3.setFont(new Font("Monospaced", Font.BOLD, 14), null, null);

	}

}
