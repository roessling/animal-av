package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import algoanim.primitives.IntMatrix;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class NeedlemanWunsch implements Generator {
  private Language             lang;
  private String               StringX;
  private String               StringY;
  private SourceCodeProperties SourceCodeProps;
  private IntMatrix            matrix;                                    // Animal
                                                                           // Grid
  private int[][]              m;                                         // Java
                                                                           // Matrix
  private Text[]               introText          = new Text[4];          // Animal
                                                                           // Introtexte
  private Text                 monitor;                                   // Anzeigefeld
                                                                           // für
                                                                           // aktuelle
                                                                           // Berechnung
  private String               monitorHeadingText = "Aktueller Schritt: ";

  public void init() {
    lang = new AnimalScript("Needleman-Wunsch [DE]",
        "Rene Schubert,Andre Schubert", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    StringX = (String) primitives.get("StringX");
    StringY = (String) primitives.get("StringY");
    SourceCodeProps = (SourceCodeProperties) props
        .getPropertiesByName("SourceCode");

    intro();
    nw(StringX, StringY);
    outro();

    return lang.toString();
  }

  public String getName() {
    return "Needleman-Wunsch [DE]";
  }

  public String getAlgorithmName() {
    return "Needleman-Wunsch";
  }

  public String getAnimationAuthor() {
    return "Rene Schubert, Andre Schubert";
  }

  public String getDescription() {
    return "Der Needleman-Wunsch-Algorithmus ist ein Verfahren der Bioinformatik."
        + "\n"
        + "Er wird fuer den Vergleich zweier Sequenzen (haeufig zweier DNA- oder Aminosaeuresequenzen) genutzt."
        + "\n"
        + "Hierfuer ermittelt er das globale Alignment, d. h. eine Zuordnung der Teilbereiche einer der Sequenzen auf moeglichst"
        + "\n"
        + "aehnliche Bereiche der anderen, und eine Bewertung der Gesamtaehnlichkeit, den global optimalen Similarity-Score.";
  }

  public String getCodeExample() {
    return "for (int r=1; r<=x.length(); r++) {"
        + "\n"
        + "	for (int c=1; c<=y.length(); c++) {"
        + "\n"
        + "		t[0] = m[r-1][c-1] + ( (x.charAt(r-1) == y.charAt(c-1)) ? 1 : 0 );"
        + "\n" + "		t[1] = Math.max(m[r-1][c], m[r][c-1]);" + "\n"
        + "		m[r][c] = Math.max(t[0], t[1]);" + "\n"
        + "System.out.println(m[x.length()][y.length()]";
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
    return Generator.JAVA_OUTPUT;
  }

  public void intro() {
    // Überschrift
    TextProperties textprop = new TextProperties();
    textprop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));
    lang.newText(new Coordinates(230, 12), "Needleman-Wunsch-Algorithmus",
        "header", null, textprop);

    // Überschrift Hintergrund
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(240, 240,
        240));
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
    rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
    lang.newRect(new Coordinates(0, 0), new Coordinates(800, 35),
        "headerBackground", null, rectProps);

    // Einleitungstext
    introText[0] = lang
        .newText(
            new Coordinates(20, 90),
            "Der Needleman-Wunsch-Algorithmus ist ein Verfahren der Bioinformatik.",
            "intro1", null);
    introText[1] = lang
        .newText(
            new Coordinates(20, 120),
            "Er wird fuer den Vergleich zweier Sequenzen (haeufig zweier DNA- oder Aminosaeuresequenzen) genutzt.",
            "intro2", null);
    introText[2] = lang
        .newText(
            new Coordinates(20, 140),
            "Hierfuer ermittelt er das globale Alignment, d. h. eine Zuordnung der Teilbereiche einer der Sequenzen auf moeglichst",
            "intro3", null);
    introText[3] = lang
        .newText(
            new Coordinates(20, 160),
            "aehnliche Bereiche der anderen, und eine Bewertung der Gesamtaehnlichkeit, den global optimalen Similarity-Score.",
            "intro4", null);

    lang.nextStep("Einfuehrung");

    for (int i = 0; i < introText.length; i++) {
      introText[i].hide();
    }

  }

  public void mon(String s) {
    monitor.setText(s, null, null);
  }

  public void nw(String theX, String theY) {
    String x = theX.toUpperCase(), y = theY.toUpperCase();
    MatrixProperties matrixProps;
    TextProperties monitorProps = new TextProperties();
    TextProperties monitorHeadingProps = new TextProperties();
    TextProperties inputProps = new TextProperties();
    Text monitorHeading;
    Text inputX;
    Text inputY;
    m = new int[x.length() + 1][y.length() + 1]; // Berechnungsmatrix
    int[] t = new int[2]; // Temporäre Werte

    // Alle Einträge der Matrix auf Null setzen
    for (int r = 0; r < x.length(); r++) {
      for (int c = 0; c < y.length(); c++) {
        m[r][c] = 0;
      }
    }

    // Matrix anzeigen
    matrixProps = new MatrixProperties();
    matrixProps
        .set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.BLACK);
    matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    matrixProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.GRAY);
    matrix = lang.newIntMatrix(new Coordinates(50, 120), m, "matrix", null,
        matrixProps);

    inputProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 20));
    inputX = lang.newText(new Offset(50, 0, matrix, AnimalScript.DIRECTION_NE),
        "x = " + x, "inputX", null, inputProps);
    inputY = lang.newText(new Offset(0, 10, inputX, AnimalScript.DIRECTION_SW),
        "y = " + y, "inputy", null, inputProps);

    monitorHeadingProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 14));
    monitorHeading = lang.newText(new Offset(0, 20, inputY,
        AnimalScript.DIRECTION_SW), monitorHeadingText, "formH", null,
        monitorHeadingProps);

    monitorProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 14));
    monitor = lang.newText(new Offset(0, 10, monitorHeading,
        AnimalScript.DIRECTION_SW), "", "form", null, monitorProps);

    // Sourcecode anzeigen
    /*
     * SourceCodeProperties codeProps = new SourceCodeProperties();
     * codeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
     * Color.RED); codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new
     * Font("Monospaced", Font.PLAIN, 16));
     */
    SourceCode code = lang.newSourceCode(new Offset(0, 30, matrix,
        AnimalScript.DIRECTION_SW), "Code", null, SourceCodeProps);
    code.addCodeLine("for (int r=1; r<=x.length(); r++) {", null, 0, null);
    code.addCodeLine("for (int c=1; c<=y.length(); c++) {", null, 1, null);
    code.addCodeLine(
        "t[0] = m[r-1][c-1] + ( (x.charAt(r-1) == y.charAt(c-1)) ? 1 : 0 );",
        null, 2, null);
    code.addCodeLine("t[1] = Math.max(m[r-1][c], m[r][c-1]);", null, 2, null);
    code.addCodeLine("m[r][c] = Math.max(t[0], t[1]);", null, 2, null);
    code.addCodeLine("System.out.println(m[x.length()][y.length()]", null, 0,
        null);

    // Matrix berechnen
    for (int r = 1; r <= x.length(); r++) {
      matrix.unhighlightCell(r - 1, y.length(), null, null);
      matrix.highlightCellColumnRange(r, 0, y.length(), null, null);
      code.unhighlight(4);
      code.highlight(0);
      mon("Zeile waehlen: r=" + r);
      lang.nextStep("Zeile " + r);
      code.unhighlight(0);

      for (int c = 1; c <= y.length(); c++) {
        code.unhighlight(4);
        code.highlight(1);
        matrix.unhighlightCellColumnRange(r, 0, y.length(), null, null);
        matrix.unhighlightCell(r, c - 1, null, null);
        matrix.highlightCell(r, c, null, null);
        mon("Spalte waehlen: c=" + c);
        lang.nextStep();

        t[0] = m[r - 1][c - 1] + ((x.charAt(r - 1) == y.charAt(c - 1)) ? 1 : 0);
        code.unhighlight(1);
        code.highlight(2);
        mon("Zwischenwert berechnen: t[0] = " + m[r - 1][c - 1] + " + ( ("
            + x.charAt(r - 1) + "==" + y.charAt(c - 1) + ") ? 1 : 0 ) = "
            + t[0]);
        lang.nextStep();

        t[1] = Math.max(m[r - 1][c], m[r][c - 1]);
        code.unhighlight(2);
        code.highlight(3);
        mon("Zwischenwert berechnen: t[1] = Math.max(" + m[r - 1][c] + ", "
            + m[r][c - 1] + ") = " + t[1]);
        lang.nextStep();

        m[r][c] = Math.max(t[0], t[1]);
        code.unhighlight(3);
        code.highlight(4);
        matrix.put(r, c, m[r][c], null, null);
        mon("Matrix aktualisieren: m[" + r + "][" + c + "] = Math.max(" + t[0]
            + ", " + t[1] + ") = " + m[r][c]);
        lang.nextStep();
      }
    }
    code.unhighlight(4);
    code.highlight(5);
    mon("Endergebnis = " + m[x.length()][y.length()]);
    lang.nextStep("Ergebnis");

    code.hide();
    matrix.hide();
    monitorHeading.hide();
    monitor.hide();
    inputX.hide();
    inputY.hide();
  }

  public void outro() {
    String[] outroText = {
        "Das Endergebnis gibt an, wie viele gemeinsame Buchstaben in der gleichen Reihenfolge auftauchen.",
        "Der Algorithmus ist auf vielfaeltige Weise erweiterbar um zum Beispiel Unterschiede in Textdateien",
        "finden zu koennen.",
        "",
        "Komplexitaet:",
        "    O(xy), da jeder Eintrag der Matrix genau einmal durchlaufen wird.",
        "",
        "Anmerkungen:",
        "    Dieser Algorithmus ist zwar einfach zu implementieren, es gibt jedoch mittlerweile bessere",
        "    Alternativen, wie z.B. Smith-Waterman, Hirschberg." };
    for (int i = 0; i < outroText.length; i++) {
      lang.newText(new Coordinates(50, 100 + (i * 20)), outroText[i], "outro"
          + i, null);
    }
    lang.nextStep("Schlussbemerkungen");
  }

}