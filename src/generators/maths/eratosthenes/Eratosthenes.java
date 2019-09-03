package generators.maths.eratosthenes;

import java.awt.Color;
import java.awt.Font;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class Eratosthenes {

	private final static Color COLOR_LIGHT_GREY = new Color(225, 225, 225);
	private final static Color COLOR_LIGHT_RED = new Color(255, 127, 127);
	private final static int GRID_COLUMNS = 10;

	/**
	 * Haupt-Prozedur des Programms
	 */
	public static void main(String[] args) {
		Language lang = new AnimalScript("Sieb des Eratosthenes", "Nicole Brunkhorst, Stefan Rado", 900, 800);
		Eratosthenes eratosthenes = new Eratosthenes(lang);
		eratosthenes.createAnimation(30);

		String script = lang.toString();
		System.out.println(script);

		try {
			FileOutputStream fos = new FileOutputStream("eratosthenesAuto.asu");
			OutputStreamWriter osw = new OutputStreamWriter(fos, Charset.forName("UTF-8"));
			osw.write(script);
			osw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Generator-settable animation properties
	private MatrixProperties tableProperties;
	private Color primeNumberColor = Color.GREEN;
	private Color nonPrimeNumberColor = COLOR_LIGHT_RED;

	// Animal objects
	private Language lang;
	private Text headerSection;
	private Text description1;
	private Text description2;
	private SourceCode intro;
	private SourceCode code;
	private Rect codeBorder;
	private Text variableN;
	private Text variableI;
	private Rect variableBorder;
	//private StringMatrix table;
	private Text outputLabel;
	private SourceCode output;

	// Algorithm state
	private boolean[] isPrimeNumber;
	private int[] highlightedCodeLines;
	private List<Integer> highlightedTableCells;
	private int tableWriteCount;

	public Eratosthenes(Language language) {
		this.lang = language;

		tableProperties = new MatrixProperties();
		tableProperties.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK); // Text-Farbe
		tableProperties.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		tableProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, COLOR_LIGHT_GREY); // Standard-Hintergrundfarbe
		tableProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 12)); // Schriftart
		tableProperties.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center"); // Ausrichtung
		tableProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.RED); // Hervorhebungs-Farbe
		tableProperties.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.BLACK); // Hervorhebungs-Schriftfarbe
		//tableProperties.set(AnimationPropertiesKeys.GRID_BORDER_COLOR_PROPERTY, Color.WHITE);
	}

	public MatrixProperties getTableProperties() {
		return tableProperties;
	}

	public void setTableProperties(MatrixProperties tableProperties) {
		this.tableProperties = tableProperties;
	}

	public Color getPrimeNumberColor() {
		return primeNumberColor;
	}

	public void setPrimeNumberColor(Color primeNumberColor) {
		this.primeNumberColor = primeNumberColor;
	}

	public Color getNonPrimeNumberColor() {
		return nonPrimeNumberColor;
	}

	public void setNonPrimeNumberColor(Color nonPrimeNumberColor) {
		this.nonPrimeNumberColor = nonPrimeNumberColor;
	}

	public void createAnimation(int N) {
		// Cleanup from possible previous runs
		highlightedCodeLines = null;
		highlightedTableCells = new ArrayList<Integer>(N / 2);
		tableWriteCount = 0;

		// Activate step mode
		lang.setStepMode(true);

		// Create steps
		createIntroductionStep();
		createPseudocodeStep();
		createDemonstrationStartStep(N);

		// Initialize
		isPrimeNumber = new boolean[N + 1];
		for (int n = 2; n <= N; n++)
			isPrimeNumber[n] = true;
		createInitializationStep(N);

		// Perform algorithm
		for (int i = 2; i <= N; i++) {
			createAlgoStep(i, N);
		}

		createFinalizationStep(N);
	}

	private void createIntroductionStep() {
		//lang.nextStep("Einleitung");
		lang.addLine("label \"Einleitung\"");

		// Titel-Text
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
		lang.newText(new Coordinates(20, 20), "Sieb des Eratosthenes", "headerTitle", null, tp);

		// Text mit Titel des aktuellen Animations-Abschnittes
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 20));
		headerSection = lang.newText(new Offset(0, 0, "headerTitle", AnimalScript.DIRECTION_SW), "Einleitung", "headerSection", null, tp);

		// Rahmen für Titel und Abschnitts-Name
		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rp.set(AnimationPropertiesKeys.FILL_PROPERTY, COLOR_LIGHT_GREY);
		lang.newRect(new Offset(-5, -5, "headerTitle", AnimalScript.DIRECTION_NW), new Offset(800, 5, "headerSection", AnimalScript.DIRECTION_SW),
				"header", null, rp);

		// Einleitungs-Text
		SourceCodeProperties scp = new SourceCodeProperties();
		scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.PLAIN, 16));
		intro = lang.newSourceCode(new Offset(5, 4, "header", AnimalScript.DIRECTION_SW), "intro", null, scp);

		intro.addCodeLine("Das Sieb des Eratosthenes ist ein Algorithmus zur Bestimmung einer Liste oder Tabelle aller Primzahlen", null, 0, null);
		intro.addCodeLine("kleiner oder gleich einer vorgegebenen Zahl.", null, 0, null);
		intro.addCodeLine("", null, 0, null);
		intro.addCodeLine("Er ist nach dem griechischen Mathematiker Eratosthenes von Kyrene benannt.", null, 0, null);
		intro.addCodeLine("Allerdings hat Eratosthenes, der im 3. Jahrhundert v. Chr. lebte, das Verfahren nicht entdeckt,", null, 0, null);
		intro.addCodeLine("sondern nur die Bezeichnung 'Sieb' für das schon lange vor seiner Zeit bekannte Verfahren eingeführt.", null, 0, null);
		intro.addCodeLine("", null, 0, null);
		intro.addCodeLine("", null, 0, null);
		intro.addCodeLine("Quelle: http://de.wikipedia.org/wiki/Sieb_des_Eratosthenes", null, 0, null);
	}

	private void createPseudocodeStep() {
		lang.nextStep("Pseudocode");

		// Einleitung ausblenden
		intro.hide();

		// Abschnitts-Titel ändern
		headerSection.setText("Pseudocode des Algorithmus", null, null);

		// Beschreibungs-Texte anlegen
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.BOLD, 16));
		description1 = lang.newText(new Offset(5, 20, "header", AnimalScript.DIRECTION_SW), "", "description1", null, tp);
		description2 = lang.newText(new Offset(0, 0, "description1", AnimalScript.DIRECTION_SW), "", "description2", null, tp);

		description1.setText("Dies ist der Algorithmus des Sieb des Eratosthenes in Pseudocode.", null, null);

		// Pseudocode
		SourceCodeProperties scp = new SourceCodeProperties();
		scp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		code = lang.newSourceCode(new Offset(5, 10, "description2", AnimalScript.DIRECTION_SW), "code", null, scp);

		// @formatter:off
		code.addCodeLine("function FindePrimzahlen(Integer N)", null, 0, null);
		code.addCodeLine(  "// Primzahlenfeld initialisieren", null, 1, null);
		code.addCodeLine(  "// Alle Zahlen sind zu Beginn potentielle Primzahlen", null, 1, null);
		code.addCodeLine(  "var Prim: array [2..N] of Boolean = True", null, 1, null);
		code.addCodeLine(  "", null, 0, null);
		code.addCodeLine(  "for i := 2 to N do", null, 1, null);
		code.addCodeLine(    "if Prim[i] then", null, 2, null);
		code.addCodeLine(      "// i ist Primzahl, gib i aus", null, 3, null);
		code.addCodeLine(      "print i", null, 3, null);
		code.addCodeLine(      "", null, 0, null);
		code.addCodeLine(      "// und markiere die Vielfachen als nicht prim, beginnend mit i*i", null, 3, null);
		code.addCodeLine(      "// (denn k*i mit k<i wurde schon als Vielfaches von k gestrichen)", null, 3, null);
		code.addCodeLine(      "for j = i*i to N step i do", null, 3, null);
		code.addCodeLine(        "Prim[j] = False", null, 4, null);
		code.addCodeLine(      "end", null, 3, null);
		code.addCodeLine(    "endif", null, 2, null);
		code.addCodeLine(  "end", null, 1, null);
		code.addCodeLine("end", null, 0, null);
		// @formatter:on

		// Rahmen um Pseudocode
		codeBorder = lang.newRect(new Offset(-5, -5, "code", AnimalScript.DIRECTION_NW), new Offset(5, 5, "code", AnimalScript.DIRECTION_SE), "codeBorder", null);
	}

	/**
	 * Hilfsfunktion zum markieren von Zeilen im Pseudocode. Merkt sich die zuletzt markierten Zeilen und entfernt die Markierung automatisch wieder,
	 * wenn andere Zeilen markiert werden.
	 */
	private void highlightCode(int... lines) {
		if (highlightedCodeLines != null) {
			for (int line : highlightedCodeLines) {
				code.unhighlight(line);
			}
		}
		if (lines != null) {
			for (int line : lines) {
				code.highlight(line);
			}
		}
		highlightedCodeLines = lines;
	}

	private void createDemonstrationStartStep(final int N) {
		lang.nextStep();

		// Abschnitts-Titel ändern
		headerSection.setText("Demonstration für N = " + N, null, null);

		// Beschreibungs-Text ändern
		description1.setText("Wir führen den Algorithmus für N = " + N + " aus.", null, null);

		// Code markieren (Funktion)
		highlightCode(0, 17);

		// Variablen N und i
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		variableN = lang.newText(new Offset(30, 0, "code", AnimalScript.DIRECTION_NE), "N = " + N, "variableN", null, tp);
		variableI = lang.newText(new Offset(25, 0, "variableN", AnimalScript.DIRECTION_NE), "i = ?", "variableI", null, tp);

		// Rahmen um aktuelle Variablen-Werte
		variableBorder = lang.newRect(new Offset(-5, -5, "variableN", AnimalScript.DIRECTION_NW), new Offset(30, 5, "variableI", AnimalScript.DIRECTION_SE),
				"variableBorder", null);

		// Titel für Ausgabe gefundener Primzahlen
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.PLAIN, 16));
		outputLabel = lang.newText(new Offset(0, 25, "code", AnimalScript.DIRECTION_SW), "Ausgabe (Gefundene Primzahlen):", "outputLabel", null, tp);

		// Ausgabe gefundener Primzahlen
		SourceCodeProperties scp = new SourceCodeProperties();
		scp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		output = lang.newSourceCode(new Offset(5, 0, "outputLabel", AnimalScript.DIRECTION_SW), "output", null, scp);
	}

	/**
	 * Hilfsfunktion zur Berechnung des Tabellen-Index für eine bestimmte Zahl
	 */
	private String getTableIndex(int n) {
		int row = (n - 1) / GRID_COLUMNS;
		int col = (n - 1) % GRID_COLUMNS;
		return String.format("table[%d][%d]", row, col);
	}
	
	private String colorToString(Color color) {
		return "(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")";
	}

	@SuppressWarnings("unchecked")
	private void createInitializationStep(final int N) {
		lang.nextStep("Initialisieren");

		// Beschreibung ändern
		description1.setText("Zunächst werden alle Zahlen von 2 bis N aufgeschrieben. Im Code geschieht dies durch die", null, null);
		description2.setText("Initialisierung eines bool-Arrays. Alle Zahlen werden zunächst als potentielle Primzahlen angesehen.", null, null);

		// Code markieren (Initialisieren)
		highlightCode(1, 2, 3);

		// Primzahlentabelle erzeugen und initialisieren
		final int rows = (N + GRID_COLUMNS - 1) / GRID_COLUMNS; // round up

		// XXX: Grid/Matrix support via StringMatrix is currently broken! No way to suppress buggy 'refresh' or call 'changeGridColor'.
		// @formatter:off
		/*
		String[][] numbers = new String[rows][GRID_COLUMNS];
		numbers[0][0] = "";
		for (int n = 2; n <= N; n++) {
			int row = (n - 1) / GRID_COLUMNS;
			int col = (n - 1) % GRID_COLUMNS;
			numbers
		}
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < GRID_COLUMNS; col++) {
				int n = row * 10 + col + 1;
				if (n >= 2 && n <= N)
					numbers[row][col] = String.valueOf(n);
				else
					numbers[row][col] = "";
			}
		}

		StringMatrix table = lang.newStringMatrix(new Offset(0, 20, "variableBorder", AnimalScript.DIRECTION_SW), numbers, "table", null, tableProperties);
		*/

		StringBuilder sb = new StringBuilder("grid \"table\" offset (0, 20) from \"variableBorder\" SW");
		sb.append(" lines ").append(rows);
		sb.append(" columns ").append(GRID_COLUMNS);
		sb.append(" style table");
		sb.append(" textColor ").append(colorToString((Color) tableProperties.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY)));
		sb.append(" fillColor ").append(colorToString((Color) tableProperties.get(AnimationPropertiesKeys.FILL_PROPERTY)));
		sb.append(" borderColor white");
		sb.append(" highlightTextColor ").append(colorToString((Color) tableProperties.get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY)));
		sb.append(" highlightFillColor ").append(colorToString((Color) tableProperties.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY)));

		Font font = (Font) tableProperties.get(AnimationPropertiesKeys.FONT_PROPERTY);
		sb.append(" font ").append(font.getName()).append(" size ").append(font.getSize());
		if (font.isBold())
			sb.append(" bold");
		if (font.isItalic())
			sb.append(" italic");
		sb.append(" align ").append(((Vector<String>)tableProperties.get(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY)).get(0));
		
		lang.addLine(sb);
		// @formatter:on

		lang.addLine("setGridColor \"table[0][0]\" fillColor white");

		for (int n = 2; n <= N; n++) {
			lang.addLine(String.format("setGridValue \"" + getTableIndex(n) + "\" \"%d\"", n));
		}
	}

	/**
	 * Entfernt das Highlight der Tabellenzellen und setzt deren Hintergrund auf ein helleres Rot
	 */
	private void makeCellHighlightPermanent() {
		for (int i : highlightedTableCells) {
			String tableIndex = getTableIndex(i);
			lang.addLine("unhighlightGridCell \"" + tableIndex + "\"");
			lang.addLine(String.format("setGridColor \"%s\" fillColor (%d, %d, %d)", tableIndex, nonPrimeNumberColor.getRed(),
					nonPrimeNumberColor.getGreen(), nonPrimeNumberColor.getBlue()));
		}
		highlightedTableCells.clear();
	}

	private void createAlgoStep(final int i, final int N) {
		lang.nextStep("Prüfe Zahl " + i);

		if (i == 2) {
			// Beschreibung ändern
			description1.setText("Nun werden alle Zahlen von 2 bis N durchlaufen. Jede noch nicht aus der Primzahl-Liste", null, null);
			description2.setText("gestrichene Zahl ist prim. All ihre Vielfachen werden als nicht-prim markiert.", null, null);
		}

		// Vorherige Tabellen-Markierungen in den Hintergrund
		makeCellHighlightPermanent();

		// Code markieren (for-Schleife)
		highlightCode(5, 16);

		// Variable i setzen
		variableI.setText("i = " + i, null, null);

		// Aktuelle Zahl in der Tabelle fett hervorheben
		if (i > 2)
			lang.addLine("setGridFont \"" + getTableIndex(i - 1) + "\" font SansSerif size 12");
		lang.addLine("setGridFont \"" + getTableIndex(i) + "\" font SansSerif size 12 bold");

		lang.nextStep();

		// Code markieren (if Prim)
		highlightCode(6, 15);

		if (isPrimeNumber[i]) {
			// Primzahl gefunden, in Tabelle markieren
			lang.addLine(String.format("setGridColor \"%s\" fillColor (%d, %d, %d)", getTableIndex(i), primeNumberColor.getRed(),
					primeNumberColor.getGreen(), primeNumberColor.getBlue()));

			lang.nextStep();

			// Code markieren (print i)
			highlightCode(7, 8);

			// Gefundene Primzahl ausgeben
			int outputLine = output.addCodeLine(String.valueOf(i), null, 0, null);
			output.highlight(outputLine);

			lang.nextStep();
			output.unhighlight(outputLine);

			// Code markieren (Vielfache markieren)
			highlightCode(10, 11, 12, 13, 14);

			// Vielfache markieren
			int delay = 100;
			for (int j = i * i; j <= N; j += i) {
				isPrimeNumber[j] = false;
				tableWriteCount++;

				lang.addLine("highlightGridCell \"" + getTableIndex(j) + "\" after " + delay + " ms");
				highlightedTableCells.add(j);
				delay += 100;
			}
		}
	}

	private void createFinalizationStep(final int N) {
		lang.nextStep("Ende des Algorithmus");

		makeCellHighlightPermanent();

		lang.addLine("setGridFont \"" + getTableIndex(N) + "\" font SansSerif size 12");

		// Code-Markierungen entfernen
		highlightCode();

		// Abschnitts-Titel ändern
		headerSection.setText("Ende des Algorithmus", null, null);

		// Beschreibung ändern
		description1.setText("Der Algorithmus hat nun terminiert. Die gefundenen Primzahlen wurden unterhalb", null, null);
		description2.setText("des Pseudocodes ausgegeben und sind in der Tabelle grün markiert.", null, null);
		
		
		lang.nextStep("Abschließende Bemerkungen");
		
		// Alles ausblenden
		description1.hide();
		description2.hide();
		code.hide();
		codeBorder.hide();
		variableN.hide();
		variableI.hide();
		variableBorder.hide();
		lang.addLine("hide \"table\"");
		outputLabel.hide();
		output.hide();

		SourceCodeProperties scp = new SourceCodeProperties();
		scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.PLAIN, 16));
		SourceCode outro = lang.newSourceCode(new Offset(5, 4, "header", AnimalScript.DIRECTION_SW), "outro", null, scp);

		outro.addCodeLine("Der Algorithmus liegt in der Komplexitätsklasse O(n log log n).", null, 0, null);
		outro.addCodeLine("", null, 0, null);
		outro.addCodeLine("In unserem Beispiel wurden zur Prüfung von " + N + " Zahlen insgesamt " + tableWriteCount + " Schreibzugriffe", null, 0, null);
		outro.addCodeLine("auf das Prim-Array durchgeführt.", null, 0, null);
		outro.addCodeLine("", null, 0, null);
		outro.addCodeLine("", null, 0, null);
		outro.addCodeLine("Das Sieb des Eratosthenes ist für große Zahlen zu aufwendig. Eine optimierte", null, 0, null);
		outro.addCodeLine("Version stellt das sogenannte Sieb von Atkin dar, das besonders für Zahlen größer", null, 0, null);
		outro.addCodeLine("100 schneller ist.", null, 0, null);
		outro.addCodeLine("", null, 0, null);
		outro.addCodeLine("Um zu entscheiden, ob eine sehr große Zahl eine Primzahl ist, lohnt es sich natürlich nicht, erst alle kleineren", null, 0, null);
		outro.addCodeLine("Primzahlen zu berechnen. Hierzu gibt es verschiedene Primzahltests, in der Praxis findet wohl der Miller-Rabin-Test", null, 0, null);
		outro.addCodeLine("am häufigsten Anwendung.", null, 0, null);
	}
}
