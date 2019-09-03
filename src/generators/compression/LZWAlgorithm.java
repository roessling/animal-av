package generators.compression;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class LZWAlgorithm implements Generator {
  Language                     lang;
//  private TextProperties       textDescriptionsAlgo;
  private TextProperties       textWbHeaderProperties;
  private String               eingabe;
  private RectProperties       rectWbHighlightProperties;
  private RectProperties       rectWbProperties;
//  private TextProperties       textOutString;
  TextProperties               textWbEntry;
  private TextProperties       textPropHeader;
  private SourceCodeProperties sourceProperties;
  private TextProperties       textDescriptions;
  private TextProperties       textSucheProp;
//  private TextProperties       textInString;
  private RectProperties       rectHeaderTextProperties;
  Text                         textWbHeader;
  private ArrayList<String>    wb      = new ArrayList<String>();
  ArrayList<wbEintrag> wb_algo = new ArrayList<wbEintrag>();

  public void init() {
    lang = new AnimalScript(
        "Datenkompression mit Lempel-Ziv-Welch-Algorithmus",
        "Natalie Faber <faber@d120.de>, Sascha Weiss <sascha@d120.de>", 800,
        600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
//    textDescriptionsAlgo = (TextProperties) props
//        .getPropertiesByName("textAnimationsBeschreibungen");
    textWbHeaderProperties = (TextProperties) props
        .getPropertiesByName("textWbHeader");
    eingabe = (String) primitives.get("eingabe");
    rectWbHighlightProperties = (RectProperties) props
        .getPropertiesByName("rectWbHighlight");
    rectWbProperties = (RectProperties) props
        .getPropertiesByName("rectWbUnHighlight");
//    textOutString = (TextProperties) props.getPropertiesByName("textAusgabe");
    textWbEntry = (TextProperties) props.getPropertiesByName("textWbEntry");
    textPropHeader = (TextProperties) props.getPropertiesByName("titleProp");
    sourceProperties = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeEigenschaften");
    textDescriptions = (TextProperties) props
        .getPropertiesByName("textBeschreibung");
    textSucheProp = (TextProperties) props.getPropertiesByName("textSuche");
//    textInString = (TextProperties) props.getPropertiesByName("textEingabe");
    rectHeaderTextProperties = (RectProperties) props
        .getPropertiesByName("titelRahmen");

    lang.setStepMode(true);
    lzw(eingabe);

    return lang.toString();
  }

  private void lzw(String s) {
    // String a = "";
    int zeichen_eingabe = s.length();
    int zeichen_ausgabe = 0;

    // textPropHeader = new TextProperties();
    // rectHeaderTextProperties = new RectProperties();
    // textDescriptions = new TextProperties();
    // textDescriptionsAlgo = new TextProperties();

    // TextProperties textInString = new TextProperties();
    // TextProperties textOutString = new TextProperties();
    // TextProperties textSucheProp = new TextProperties();
    // TextProperties textWbHeaderProperties = new TextProperties();

    // textWbEntry = new TextProperties();
    // rectWbProperties = new RectProperties();
    // rectWbHiglightProperties = new RectProperties();

    // SourceCodeProperties sourceProperties = new SourceCodeProperties();

    // sourceProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
    // Color.red);

    // rectWbHiglightProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
    // Color.RED);

    // Header Text
    Text textHeader = lang.newText(new Coordinates(20, 30),
        "Datenkompression mit Lempel-Ziv-Welch-Algorithmus", "header", null,
        textPropHeader);
    // Umrandung unserers Headers
    lang.newRect(new Offset(-5, -5, textHeader, "NW"), new Offset(5, 5,
        textHeader, "SE"), "rectheader", null, rectHeaderTextProperties);

    // Alle Descriptions etc. erstellen
    Text textDesc1 = lang
        .newText(
            new Offset(-5, 20, textHeader, "SW"),
            "Der Lempel-Ziv-Welch-Algorithmus wird zur Reduzierung der Datenmenge verwendet. ",
            "textDesc1", null, textDescriptions);
    Text textDesc2 = lang
        .newText(
            new Offset(0, 0, textDesc1, "SW"),
            "LZW verwendet Wörterbücher in die, die am meisten vorkommenden Zeichenketten gespeichert werden.",
            "textDesc2", null, textDescriptions);
    Text textDesc3 = lang.newText(new Offset(0, 0, textDesc2, "SW"),
        "(Bspw. ist, der, die, das, ein, eine )  ", "textDesc3", null,
        textDescriptions);
    Text textDesc4 = lang
        .newText(
            new Offset(0, 0, textDesc3, "SW"),
            "Einträge die zur Laufzeit in das Wörterbuch eingefügt werden beginnen mit dem Index 256.",
            "textDesc4", null, textDescriptions);
    Text textDesc5 = lang.newText(new Offset(0, 0, textDesc4, "SW"),
        "Für unsere Zwecke sind die vorherigen Einträge zu vernachlässigen. ",
        "textDesc5", null, textDescriptions);
    Text textDesc6 = lang
        .newText(
            new Offset(0, 0, textDesc5, "SW"),
            "In einem Eintrag werden immer der gefundene Eintrag und das nächste Zeichen geseichert.",
            "textDesc6", null, textDescriptions);
    Text textDesc7 = lang
        .newText(
            new Offset(0, 0, textDesc6, "SW"),
            "Einzelne Zeichen sind im Wörterbuch vordefiniert werden jedoch ausgeblendet.",
            "textDesc6", null, textDescriptions);

    // Eingabe String
    Text textIn = lang.newText(new Offset(-5, 20, textHeader, "SW"),
        "Eingabe: " + s, "textIn", null, textDescriptions);

    // Ausgabe String
    Text textOut = lang.newText(new Offset(0, 0, textIn, "SW"), "Ausgabe: ",
        "textOut", null, textDescriptions);

    Text textSuche = lang.newText(new Offset(0, 0, textOut, "SW"),
        "Aktuelles Teilwort: ", "textOut", null, textSucheProp);

    // Wörterbuch Überschrift
    textWbHeader = lang.newText(new Offset(0, 10, textSuche, "SW"),
        "Wörterbuch:", "textWbHeader", null, textWbHeaderProperties);

    // Source Code
    SourceCode source = lang.newSourceCode(new Offset(100, 10, textIn, "E"),
        "source", null, sourceProperties);
    source.addCodeLine("Solange noch Zeichen verfügbar sind", "", 1, null);
    source.addCodeLine(
        "1. Suche das längste Teilwort das im Wörterbuch vorhanden ist", "", 2,
        null);
    source.addCodeLine(
        "2. Überprüfe ob es aus einem oder mehreren Zeichen besteht", "", 2,
        null);
    source.addCodeLine(
        "2 (Einem Zeichen) a) Schreibe dieses Zeichen in die Ausgabe", "", 3,
        null);
    source
        .addCodeLine(
            "2 (Mehreren Zeichen) b) Suche den passenden Eintrag im Wörterbuch und schreibe die ID in die Ausgabe",
            "", 3, null);
    source.addCodeLine(
        "3. Schreibe das Teilwort plus das nächste Zeichen in das Wörterbuch",
        "", 2, null);
    source.addCodeLine("4. Entferne das Teilwort aus der Eingabe", "", 2, null);

    source.hide();

    // Algo Beschreibungen
    Text textAlgo1 = lang.newText(new Offset(0, 20, source, "SW"), " ",
        "textAlgo1", null, textDescriptions);
    Text textAlgo2 = lang.newText(new Offset(0, 0, textAlgo1, "SW"), "  ",
        "textAlgo2", null, textDescriptions);
    Text textAlgo3 = lang.newText(new Offset(0, 0, textAlgo2, "SW"), "  ",
        "textAlgo3", null, textDescriptions);
    Text textAlgo4 = lang.newText(new Offset(0, 0, textAlgo3, "SW"), "  ",
        "textAlgo4", null, textDescriptions);
    Text textAlgo5 = lang.newText(new Offset(0, 0, textAlgo4, "SW"), "  ",
        "textAlgo5", null, textDescriptions);
    Text textAlgo6 = lang.newText(new Offset(0, 0, textAlgo5, "SW"), "  ",
        "textAlgo6", null, textDescriptions);
    Text textAlgo7 = lang.newText(new Offset(0, 0, textAlgo6, "SW"), "  ",
        "textAlgo6", null, textDescriptions);

    textSuche.hide();
    textDesc1.hide();
    textDesc2.hide();
    textDesc3.hide();
    textDesc4.hide();
    textDesc5.hide();
    textDesc6.hide();
    textDesc7.hide();
    textAlgo1.hide();
    textAlgo2.hide();
    textAlgo3.hide();
    textAlgo4.hide();
    textAlgo5.hide();
    textAlgo6.hide();
    textAlgo7.hide();
    textIn.hide();
    textOut.hide();
    textWbHeader.hide();

    lang.nextStep();
    textDesc1.show();
    textDesc2.show();
    textDesc3.show();
    textDesc4.show();
    textDesc5.show();
    textDesc6.show();
    textDesc7.show();
    lang.nextStep("Beschreibung des Algorithmus");

    textDesc1.hide();
    textDesc2.hide();
    textDesc3.hide();
    textDesc4.hide();
    textDesc5.hide();
    textDesc6.hide();
    textDesc7.hide();

    // Fehler Überprüfung
    if (s.length() > 25 || s.length() == 0) {
      // String zu lang
      TextProperties errorProp = new TextProperties();
      errorProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
      errorProp.set(AnimationPropertiesKeys.NAME, "textPropert");
      errorProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          "SansSerif", Font.BOLD, 24));
      errorProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
      lang.newText(new Offset(-10, 90, textHeader, "SW"),
          "Fehler bei der Benutzereingabe:", "error", null, errorProp);
      lang.newText(
          new Offset(-10, 130, textHeader, "SW"),
          "Der String darf maximal 25 Zeichen erhalten und muss mindestens 1 Zeichen enthalten",
          "error2", null, errorProp);
      lang.nextStep();
      return;
    }

    source.show();
    textSuche.show();

    // Eingabe String anzeigen
    textIn.show();
    textOut.show();
    textWbHeader.show();

    String suche = "";
    boolean found = true;
    String ausgabe = "Ausgabe: ";
    int end = 1;
    int pos = 0;
    int temp = 0;
    int durchlauf = 0;

    lang.nextStep("Initalisierung des Algorithmus");
    String myS = s;
    do {
      source.highlight(1);
      do {
        suche = myS.substring(0, end);
        temp = wb_contains(suche);
        if (temp == -1)
          found = false;
        else {
          end++;
          pos = temp;
        }
      } while (found == true);

      // String zum arbeiten
      if (end < 2)
        suche = myS.substring(0, 1);
      else
        suche = myS.substring(0, end - 1);

      textSuche.setText("Aktuelles Teilwort: " + suche, null, null);

      lang.nextStep("Teilwort gefunden: " + suche);
      source.unhighlight(1);
      source.highlight(2);

      if (suche.length() > 1) {
        source.highlight(4);
        // Wörterbuch die ID raussuchen
        wb_algo.get(pos).highlight();

        pos = pos + 256;
        if (suche.length() < 2)
          ausgabe += suche + "";
        else
          ausgabe += " <" + pos + "> ";
        textOut.setText(ausgabe, null, null);
        zeichen_ausgabe++;
        lang.nextStep();
        wb_algo.get(pos - 256).unhighlight();

      } else {
        source.highlight(3);
        pos = pos + 256;
        if (suche.length() < 2)
          ausgabe += suche + "";
        else
          ausgabe += " <" + pos + "> ";
        zeichen_ausgabe++;
        textOut.setText(ausgabe, null, null);
        lang.nextStep();
      }

      source.unhighlight(2);
      source.unhighlight(3);
      source.unhighlight(4);
      source.highlight(5);

      // Ins Wörterbuch eintragen
      if (end < s.length())
        if (end < 2) {
          wb.add(s.substring(0, end + 1));
          wb_algo.add(new wbEintrag(s.substring(0, end + 1), durchlauf,
              rectWbProperties, rectWbHighlightProperties));
          lang.nextStep("   Einfügen ins Wörterbuch von: "
              + s.substring(0, end + 1));
        } else {
          wb_algo.add(new wbEintrag(s.substring(0, end), durchlauf,
              rectWbProperties, rectWbHighlightProperties));
          wb.add(s.substring(0, end));
          lang.nextStep("   Einfügen ins Wörterbuch von: "
              + s.substring(0, end));
        }

      if (end < 2)
        myS = s.substring(end);
      else
        myS = s.substring(end - 1);
      textIn.setText("Eingabe " + myS, null, null);
      source.unhighlight(5);
      source.highlight(6);

      lang.nextStep("   Entferne das Teilwort aus der Eingabe");

      source.unhighlight(6);

      temp = 0;
      found = true;
      pos = 0;
      end = 1;
      durchlauf++;
    } while (myS.length() != 0);
    lang.nextStep("Endfolie");
    textAlgo1.show();
    textAlgo2.show();
    textAlgo3.show();
    textAlgo4.show();
    textAlgo5.show();
    textAlgo6.show();
    textAlgo7.show();

    double kompprozent = (float) 100
        - (zeichen_ausgabe * 100 / zeichen_eingabe);

    textAlgo1.setText("Wie wir sehen ist unser Algorithmus nun beendet.", null,
        null);
    textAlgo2
        .setText(
            "Nun haben wir als Ergebnis eine Komprimierung unseres Eingabe Strings.",
            null, null);
    textAlgo3.setText("Die Eingabe bestand aus " + zeichen_eingabe
        + " Zeichen.", null, null);
    textAlgo4.setText("Die Ausgabe besteht aus " + zeichen_ausgabe
        + " Zeichen.", null, null);
    textAlgo5.setText("Dies entspricht einer Kompession von " + kompprozent
        + " Prozent.", null, null);
    textAlgo6.setText("", null, null);
    textAlgo7.setText("", null, null);
    lang.nextStep();
  }

  public int wb_contains(String s) {
    int position = 0;
    int i = -1;
    while (!wb.isEmpty() && position < wb.size()) {
      if (wb.get(position).length() >= s.length()
          && s.equals(wb.get(position).substring(0, s.length())))
        return position;
      position++;
    }

    return i;
  }

  class wbEintrag {
    private Text wort;
    private Rect rectUnHigh;
    private Rect rectHigh;
    private int  pos;

    public wbEintrag(String wort, int pos, RectProperties rectWbProperties,
        RectProperties rectWbHiProperties) {
      this.pos = pos + 256;
      // Text erstellen sowie die passenden Rahmen
      if (pos == 0)
        this.wort = lang.newText(new Offset(0, 10, textWbHeader, "SW"),
            this.pos + " = " + wort, "wbEintrag" + this.pos, null, textWbEntry);
      else {
        if (pos % 10 == 0)
          this.wort = lang.newText(new Offset(45, 0, wb_algo.get(pos - 10)
              .getWort(), "NE"), this.pos + " = " + wort, "wbEintrag"
              + this.pos, null, textWbEntry);
        else
          this.wort = lang.newText(new Offset(0, 10, wb_algo.get(pos - 1)
              .getWort(), "SW"), this.pos + " = " + wort, "wbEintrag"
              + this.pos, null, textWbEntry);
      }

      this.rectHigh = lang.newRect(new Offset(-2, -2, this.wort, "NW"),
          new Offset(2, 2, this.wort, "SE"), "rectWb" + this.pos, null,
          rectWbHiProperties);

      this.rectUnHigh = lang.newRect(new Offset(-2, -2, this.wort, "NW"),
          new Offset(2, 2, this.wort, "SE"), "rectWb" + this.pos, null,
          rectWbProperties);

      this.rectHigh.hide();

    }

    public void highlight() {
      rectUnHigh.hide();
      rectHigh.show();
    }

    public void unhighlight() {
      rectUnHigh.show();
      rectHigh.hide();
    }

    public Text getWort() {
      return wort;
    }

    public void setWort(Text wort) {
      this.wort = wort;
    }

    public Rect getRectUnHigh() {
      return rectUnHigh;
    }

    public void setRectUnHigh(Rect rectUnHigh) {
      this.rectUnHigh = rectUnHigh;
    }

    public Rect getRectHigh() {
      return rectHigh;
    }

    public void setRectHigh(Rect rectHigh) {
      this.rectHigh = rectHigh;
    }

    public int getPos() {
      return pos;
    }

    public void setPos(int pos) {
      this.pos = pos;
    }

  }

  public String getName() {
    return "Datenkompression mit Lempel-Ziv-Welch-Algorithmus";
  }

  public String getAlgorithmName() {
    return "Lempel-Ziv-Welch-Algorithmus";
  }

  public String getAnimationAuthor() {
    return "Natalie Faber, Sascha Weiss";
  }

  public String getDescription() {
    return "Der Lempel-Ziv-Welch-Algorithmus (kurz LZW-Algorithmus) ist ein häufig bei Grafikformaten zur Datenkompression, also zur Reduzierung der Datenmenge, eingesetzter Algorithmus. Ein Großteil der Funktionsweise dieses Algorithmus wurden 1978 von Abraham Lempel und Jacob Ziv entwickelt und veröffentlicht (LZ78). Einige Detailverbesserungen wurden 1983 von Terry A. Welch gemacht."
        + "\n"
        + "\n"
        + "LZW ist ein verlustfreies Komprimierungsverfahren. Es wird zum Beispiel im 1987 von CompuServe-Mitarbeitern entwickelten Bildformat GIF benutzt und kann optional auch in TIFF eingesetzt werden. Es eignet sich aber für jede Form von Daten, da das eingesetzte Wörterbuch erst zur Laufzeit generiert wird und so unabhängig vom Format ist. LZW ist wohl der bekannteste Vertreter der LZ-Familie.";
  }

  public String getCodeExample() {
    return "Solange noch Zeichen verfügbar sind"
        + "\n"
        + "1. Suche das längste Teilwort das im Wörterbuch vorhanden ist"
        + "\n"
        + "2. Überprüfe ob es aus einem oder mehreren Zeichen besteht"
        + "\n"
        + "2 (Einem Zeichen) a) Schreibe dieses Zeichen in die Ausgabe"
        + "\n"
        + "2 (Mehreren Zeichen) b) Suche den passenden Eintrag im Wörterbuch und schreibe die ID in die Ausgabe	"
        + "\n"
        + "3. Schreibe das Teilwort plus das nächste Zeichen in das Wörterbuch"
        + "\n" + "4. Entferne das Teilwort aus der Eingabe";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_COMPRESSION);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}