package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.CircleProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class IterativeTiefensuche implements Generator {
  private Language             lang;
  private String               baum;
  private String               ziel;
  private TextProperties       text;
  private SourceCodeProperties sourceCode;
  private PolylineProperties   polyline;
  private CircleProperties     circle;
  private Color                durchlaufeneFarbe;
  private Color                stackFarbe;
  private Color                zielFarbe;
  private Color                aktuelleFarbe;
  private Knoten               Ergebnis;
  private StringArray          st;
  private ArrayList<Text>      te         = new ArrayList<Text>();
  private static Knoten[]      k;
  private static Polyline[]    p;
  private Knoten               zielKnoten = null;

  public void init() {
    lang = new AnimalScript("Iterative Tiefensuche",
        "Jannik Schildknecht und Sergej Alexeev", 800, 600);
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    baum = (String) primitives.get("baum");
    ziel = (String) primitives.get("ziel");
    text = (TextProperties) props.getPropertiesByName("text");
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    polyline = (PolylineProperties) props.getPropertiesByName("polyline");
    circle = (CircleProperties) props.getPropertiesByName("circle");
    durchlaufeneFarbe = (Color) primitives.get("durchlaufeneFarbe");
    stackFarbe = (Color) primitives.get("stackFarbe");
    zielFarbe = (Color) primitives.get("zielFarbe");
    aktuelleFarbe = (Color) primitives.get("aktuelleFarbe");

    // Ablauf der Animation
    davor(text);
    SourceCode s = danach(text, sourceCode);

    int anzahlKnoten = bestimmeBaumgroesse(baum);

    k = zeichneBaum(anzahlKnoten, baum, ziel, circle, text);
    p = generierungDerLinien(k, polyline, anzahlKnoten - 1);
    lang.nextStep();

    if (IT(k, zielKnoten, s)) {
      hideThings();
      s.hide();
      epilog1(ziel);
    } else {
      hideThings();
      s.hide();
      epilog2(ziel);
    }

    lang.finalizeGeneration();
    return lang.toString();
  }

  private int bestimmeBaumgroesse(String baum) {
    int x = 0;

    String baum2 = baum;
    while (baum2.length() != 0) {
      if (baum2.charAt(0) == '(') {
        baum2 = baum2.substring(1);
      } else if (baum2.charAt(0) == ')') {
        baum2 = baum2.substring(1);
      } else if (baum2.charAt(0) == ' ') {
        baum2 = baum2.substring(1);
      } else {
        baum2 = baum2.substring(2);
        x++;
      }
    }

    return x;
  }

  private void hideThings() {

    for (int i = 0; i < k.length; i++) {
      k[i].getKreis().hide();
    }

    for (int i = 0; i < p.length; i++) {
      p[i].hide();
    }

    st.hide();

    for (int i = 0; i < te.size(); i++) {
      te.get(i).hide();
    }
  }

  private void epilog2(String z) {
    lang.newText(new Coordinates(20, 100), "Der Zielknoten " + z
        + " wurde nicht gefunden, da er nicht im Baum ist.", "e1", null);
    lang.newText(new Coordinates(20, 130),
        "Die Iterationstiefe der maximalen Baumtiefe wurde dabei ausgenutzt.",
        "e1", null);
    lang.newText(
        new Coordinates(20, 160),
        "Jeder Knoten im Baum wurde bis zu der vorgegebenen Iterationstiefe mindestens einmal abgesucht.",
        "e1", null);
  }

  private void epilog1(String z) {
    te.add(lang.newText(new Coordinates(20, 100), "Der Zielknoten " + z
        + " wurde erfolgreich gefunden.", "e1", null));
    te.add(lang
        .newText(
            new Coordinates(20, 130),
            "Eventuell wurden dabei nicht alle Knoten durchsucht. Befand sich der Knoten",
            "e1", null));
    te.add(lang
        .newText(
            new Coordinates(20, 160),
            "in der Mitte des Baumes und hatte im linken Teilbaum eine hohe Tiefe, war dieser",
            "e1", null));
    te.add(lang.newText(new Coordinates(20, 190),
        "Algothmus effektiver als Tiefen- oder Breitensuche.", "e1", null));
  }

  private Polyline[] generierungDerLinien(Knoten[] k, PolylineProperties prop,
      int a) {
    Polyline[] p = new Polyline[a];
    for (int i = 0; i < k.length - 1; i++) {
      Node[] nodeP = {
          new Coordinates(k[i].getCoor().getX(), k[i].getCoor().getY() - 15),
          new Coordinates(k[i].getVaKoor().getX(), k[i].getVaKoor().getY() + 15) };
      p[i] = lang.newPolyline(nodeP, "p" + i, null, prop);

    }
    return p;

  }

  public Knoten[] zeichneBaum(int anzahlKnoten, String baum, String ziel,
      CircleProperties c, TextProperties t) {
    // Benoetigte Parameter
    List<Integer> vaterID = new ArrayList<Integer>();
    List<Integer> mittelwert = new ArrayList<Integer>();
    List<Integer> brueder = new ArrayList<Integer>();
    int tiefe = 0;
    Knoten[] k = new Knoten[anzahlKnoten];
    int y = 100;
    int x = 1200;

    // Schleife zum parsen des Strings und zum erstellen des Baumes
    // Die Knoten muessen aus 2 Zahlen bestehen
    int i;
    String baum2 = baum;
    for (i = 0; baum2.length() != 2;) {

      // Knotenname
      String name = baum2.substring(baum2.length() - 2, baum2.length());
      String letztesElement = baum2.substring(baum2.length() - 1,
          baum2.length());

      // Parsen des Strings

      // Man kommt in eine tiefere Ebene - Neue VaterID, mittelwert und
      // bruederAnzahl werden erstellt alte
      // bleiben erhalten.
      if (letztesElement.equals(")")) {
        vaterID.add((int) (100 * Math.random()));
        tiefe++;
        brueder.add(1);
        mittelwert.add(x);
        y = y + 100;
        baum2 = baum2.substring(0, baum2.length() - 1);

        // man kommt wieder eine Ebene hoch. VaterID wird geloescht alte
        // wird wieder aktuell -setzung der neuen x-Koordinate

      } else if (letztesElement.equals("(")) {

        x = (mittelwert.get(mittelwert.size() - 1) / (brueder.get(brueder
            .size() - 1)));
        mittelwert.remove(mittelwert.size() - 1);
        brueder.remove(brueder.size() - 1);
        tiefe--;
        y = y - 100;
        k = bestimmePapa(k, x, y, vaterID.get(vaterID.size() - 1));
        vaterID.remove(vaterID.size() - 1);
        baum2 = baum2.substring(0, baum2.length() - 1);

        // Anzahl der BruederKnoten wird erhoeht - Mittelwert wird
        // erhoeht
      } else if (letztesElement.equals(" ")) {
        x = x - 200 + y * 1 / 2;
        mittelwert.set(mittelwert.size() - 1,
            mittelwert.get(mittelwert.size() - 1) + x);
        brueder.set(brueder.size() - 1, brueder.get(brueder.size() - 1) + 1);
        baum2 = baum2.substring(0, baum2.length() - 1);

        // Knoten wird gesetzt
      } else if (ziel.equals(name)) {
        zielKnoten = new Knoten(lang.newCircle(new Coordinates(x, y), 15, name,
            null, c), tiefe, name, new Coordinates(x, y), null,
            vaterID.get(vaterID.size() - 1));
        te.add(lang.newText(new Coordinates(x - 7, y - 7), name, name, null, t));
        k[i] = zielKnoten;
        i++;
        baum2 = baum2.substring(0, baum2.length() - 2);
      } else {

        k[i] = new Knoten(lang.newCircle(new Coordinates(x, y), 15, name, null,
            c), tiefe, name, new Coordinates(x, y), null, vaterID.get(vaterID
            .size() - 1));

        i++;
        te.add(lang.newText(new Coordinates(x - 7, y - 7), name, name, null, t));
        baum2 = baum2.substring(0, baum2.length() - 2);
      } // Zielknoten setzen...

    }

    String name = baum2.substring(baum2.length() - 2, baum2.length());

    k[i] = new Knoten(lang.newCircle(new Coordinates(x, y), 15, name, null, c),
        tiefe, name, new Coordinates(x, y), null, 0);
    te.add(lang.newText(new Coordinates(x - 7, y - 7), name, name, null, t));
    Ergebnis = k[i];
    return k;
  }

  private Knoten[] bestimmePapa(Knoten[] k, int x, int y, Integer vaterID) {
    for (int i = 0; i < k.length - 1; i++) {
      try {

        if (k[i].getVaterID() == vaterID) {
          k[i].setVaKoor(new Coordinates(x, y));
        }
      } catch (Exception e) {
        break;
      }
    }
    return k;
  }

  public void davor(TextProperties text) {
    // Ueberschrift der Funktion
    Text b = lang.newText(new Coordinates(550, 20), "Iterative Tiefensuche",
        "t1", null);

    RectProperties rp = new RectProperties();
    rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);
    rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.gray);
    rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Offset(-5, -5, b, "NW"), new Offset(5, 5, b, "SE"), "r",
        null, rp);

    lang.nextStep();

    // Prolog
    Text t2 = lang
        .newText(
            new Coordinates(20, 70),
            "Bei der iterativen Tiefensuche handelt es sich um die Kombination von den wuenschenswerten Eigenschaften von Tiefensuche (geringer Speicherverbrauch) ",
            "t2", null, text);
    Text t3 = lang
        .newText(
            new Coordinates(20, 100),
            "und Breitensuche (Optimalitaet). Dabei wird die beschraenkte Tiefensuche benutzt, bei der nur bis zu einer bestimmten, vorgegebenen Tiefe alle Knoten erkundet werden.",
            "t3", null, text);
    Text t4 = lang.newText(new Coordinates(20, 120),
        "Es wird mit der Tiefe 0 gestartet.", "t4", null, text);
    Text t5 = lang
        .newText(
            new Coordinates(20, 140),
            "Wird das gesuchte Element nicht gefunden, wird die Tiefe um 1 erhoeht und die Tiefensuche gestartet!",
            "t5", null, text);
    Text t6 = lang
        .newText(
            new Coordinates(20, 160),
            "Falls das gesuchte Element gefundet wird, so wird es zurueckgegeben, ansonsten terminiert der Algorithmus erst dann, wenn die vorgegebene Tiefe ueberschritten wird.",
            "t5", null, text);
    Text t7 = lang.newText(new Coordinates(20, 180),
        "Die Laufzeit des Algorithmus betraegt O(|Kanten|+|Knoten|).", "t5",
        null, text);

    lang.nextStep();

    // Verstecken des Prologs
    t2.hide();
    t3.hide();
    t4.hide();
    t5.hide();
    t6.hide();
    t7.hide();
  }

  public SourceCode danach(TextProperties text, SourceCodeProperties sourceCode) {

    SourceCode sc = lang.newSourceCode(new Coordinates(40, 100), "sourceCode",
        null, sourceCode);

    sc.addCodeLine("public boolean IT(int[] Baum, int Ziel){", null, 0, null);
    sc.addCodeLine("     int IterationsTiefe = 0;", null, 0, null);
    sc.addCodeLine("     while (IterationsTiefe < n) {", null, 0, null);
    sc.addCodeLine(
        "            Ergebnis = Beschraenkte_Tiefensuche(Wurzel, Ziel, IterationsTiefe, Baum);",
        null, 0, null);
    sc.addCodeLine("            if(Ergebnis != 0)", null, 0, null);
    sc.addCodeLine("                   return true;", null, 0, null);
    sc.addCodeLine("            IterationsTiefe = IterationsTiefe + 1;", null,
        0, null);
    sc.addCodeLine("     }", null, 0, null);
    sc.addCodeLine("return false;", null, 0, null);
    sc.addCodeLine("}", null, 0, null);
    sc.addCodeLine(
        "public int Beschraenkte_Tiefensuche(int Knoten, int Ziel, int Tiefe, int[] Baum){",
        null, 0, null);
    sc.addCodeLine("     if (Knoten == Ziel){", null, 0, null);
    sc.addCodeLine("         return Knoten", null, 0, null);
    sc.addCodeLine("     }", null, 0, null);
    sc.addCodeLine("     else {", null, 0, null);
    sc.addCodeLine("          if(aktuelleTiefe(Knoten, Baum) < Tiefe) {", null,
        0, null);
    sc.addCodeLine("                   stack = expand(Knoten, Baum);", null, 0,
        null);
    sc.addCodeLine("                   while (stack.length != 0) {", null, 0,
        null);
    sc.addCodeLine("                        int NaechsterKnoten = pop(stack);",
        null, 0, null);
    sc.addCodeLine(
        "                        Ergebnis = Beschraenkte_Tiefensuche(NaechsterKnoten, Ziel, Tiefe, Baum);",
        null, 0, null);
    sc.addCodeLine("                        if(Ergebnis != 0)", null, 0, null);
    sc.addCodeLine("                               return Ergebnis;", null, 0,
        null);
    sc.addCodeLine("                   }", null, 0, null);
    sc.addCodeLine("          }", null, 0, null);
    sc.addCodeLine("      }", null, 0, null);
    sc.addCodeLine("      return Ergebnis;", null, 0, null);
    sc.addCodeLine("}", null, 0, null);
    lang.nextStep();

    // Legende wird hinzugefuegt
    te.add(lang.newText(new Coordinates(850, 400), "Legende:", "Legende", null,
        text));
    te.add(lang.newText(new Coordinates(950, 400),
        "Aktuell zu durchsuchender Knoten", "Rot", null, text));
    te.get(te.size() - 1).changeColor("Color", aktuelleFarbe, null, null);
    te.add(lang.newText(new Coordinates(950, 420),
        "Gelber Kreis - Aus dem Knoten kommend", "Gelb", null, text));
    te.get(te.size() - 1).changeColor("Color", durchlaufeneFarbe, null, null);
    te.add(lang.newText(new Coordinates(950, 440),
        "Blauer Kreis - Gefundener Zielknoten", "Blau", null, text));
    te.get(te.size() - 1).changeColor("Color", zielFarbe, null, null);
    te.add(lang.newText(new Coordinates(740, 490), "Stack:", "stack", null,
        text));
    te.get(te.size() - 1).changeColor("Color", stackFarbe, null, null);

    lang.nextStep();
    return sc;
  }

  // Programm zur iterativen Tiefensuche
  public boolean IT(Knoten[] Baum, Knoten Ziel, SourceCode sc) {
    sc.highlight(0);
    lang.nextStep();
    int IterationsTiefe = 0;
    sc.toggleHighlight(0, 1);

    TextProperties text = new TextProperties();
    text.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 14));
    Text tiefe = lang.newText(new Coordinates(500, 490), "Iterationstiefe = 0",
        "t5", null, text);

    lang.nextStep();

    // Schleife erledigt das Iterative vorgehen
    while (IterationsTiefe < getHoechsteTiefe(Baum)) {
      // unhighlightet hier mehrere Zeilen, da man von verschiedenen Orten
      // kommen kann. Dies passiert auch unten oefters bei den Aufrufen!
      sc.highlight(2);
      sc.unhighlight(1);
      sc.unhighlight(7);
      lang.nextStep();
      // ToggleHighlight unhighlightet die Zeile 2 und Highleitet Zeile 3
      sc.toggleHighlight(2, 3);

      MultipleChoiceQuestionModel ersteKnote = new MultipleChoiceQuestionModel(
          "Q1");
      ersteKnote.setPrompt("Welcher Knoten wird zuerst besucht?");
      ersteKnote.addAnswer("12", 0,
          "Falsch, es wird zuerst der Wurzelknoten besucht");
      ersteKnote.addAnswer("4", 1,
          "Korrekt, der Wurzelknoten wird zuerst besucht");
      ersteKnote.addAnswer("50", 0,
          "Falsch, es wird zuerst der Wurzelknoten besucht");
      lang.addMCQuestion(ersteKnote);
      lang.nextStep();
      // Aufruf der Tiefensuche
      Ergebnis = Beschraenkte_Tiefensuche(Baum[Baum.length - 1], Ziel,
          IterationsTiefe, Baum, sc);
      sc.toggleHighlight(3, 4);
      sc.unhighlight(25);
      lang.nextStep();
      // Ueberprueft, ob der Zielknoten erreicht wurde.
      if (Ergebnis.equals(Ziel)) {
        sc.toggleHighlight(4, 5);
        lang.nextStep();
        te.add(tiefe);
        return true;
      }
      // Tiefe der Iteration erhoehen
      IterationsTiefe = IterationsTiefe + 1;
      tiefe.setText("Iterationstiefe = " + IterationsTiefe, null, null);
      sc.toggleHighlight(4, 6);
      lang.nextStep();
      sc.toggleHighlight(6, 7);
      lang.nextStep();
    }
    sc.toggleHighlight(7, 8);
    lang.nextStep();
    te.add(tiefe);
    return false;
  }

  private int getHoechsteTiefe(Knoten[] baum) {
    int counter = 0;
    for (int i = 0; i < baum.length; i++) {
      if (counter < baum[i].getTiefe()) {
        counter = baum[i].getTiefe();
      }
    }
    return counter + 1;
  }

  // Programm zur Tiefensuche
  public Knoten Beschraenkte_Tiefensuche(Knoten Knoten, Knoten Ziel, int Tiefe,
      Knoten[] Baum, SourceCode sc) {

    Knoten[] stack = new Knoten[0];

    sc.highlight(10);
    sc.unhighlight(3);
    sc.unhighlight(19);
    // Highlightet den Aktuellen Knoten
    Knoten.getKreis().changeColor("fillColor", aktuelleFarbe, null, null);
    lang.nextStep();
    sc.toggleHighlight(10, 11);
    lang.nextStep();

    // Vergleicht den aktuellen Knoten mit dem Zielknoten
    if (Knoten == Ziel) {
      sc.toggleHighlight(11, 12);
      lang.nextStep();
      return Knoten;
    }
    // Knoten ist nicht das Ziel...
    else {
      sc.toggleHighlight(11, 13);
      lang.nextStep();
      sc.toggleHighlight(13, 14);
      lang.nextStep();

      // Iterationstiefe verhindert hier ggf, dass die Tiefensuche auf den
      // ganzen Baum ausgefuehrt wird
      if (Knoten.getTiefe() < Tiefe) {
        sc.toggleHighlight(14, 15);
        FillInBlanksQuestionModel stackGroesse = new FillInBlanksQuestionModel(
            "Q2");
        stackGroesse.setPrompt("Wie groß ist der Stack?");
        stackGroesse.addAnswer("4", 1,
            "Die Anzahl der Kinder von Wurzelknoten ist 4");
        lang.addFIBQuestion(stackGroesse);
        lang.nextStep();
        stack = expand(Knoten, Baum, stack);
        // initiere Stack
        sc.toggleHighlight(15, 16);
        lang.nextStep();

        // Arbeite die Nachfolgeknoten ab
        while (stack.length != 0) {
          sc.highlight(17);
          sc.unhighlight(16);
          sc.unhighlight(22);

          FillInBlanksQuestionModel nK = new FillInBlanksQuestionModel("Q3");
          nK.setPrompt("Welcher Knoten wird als nächstes besucht?");
          nK.addAnswer("11", 1, "Korrekt, der Kindknoten ganz links");
          lang.addFIBQuestion(nK);

          lang.nextStep();
          Knoten NaechsterKnoten = stack[0];
          stack = pop(stack);
          sc.toggleHighlight(17, 18);
          lang.nextStep();
          sc.toggleHighlight(18, 19);
          lang.nextStep();
          // Setze den Aktuellen Knoten wieder auf Gelb, da er noch
          // nicht abgeschlossen ist
          Knoten.getKreis().changeColor("fillColor", durchlaufeneFarbe, null,
              null);
          Ergebnis = Beschraenkte_Tiefensuche(NaechsterKnoten, Ziel, Tiefe,
              Baum, sc);
          sc.toggleHighlight(19, 20);
          sc.unhighlight(25);
          lang.nextStep();
          // Rueckgabe, falls Knoten gefunden!
          if (Ergebnis.equals(Ziel)) {
            sc.toggleHighlight(20, 21);
            Ergebnis.getKreis().changeColor("fillColor", zielFarbe, null, null);
            lang.nextStep();
            return Ergebnis;
          }
          sc.toggleHighlight(20, 22);
          lang.nextStep();
        }
        sc.toggleHighlight(22, 23);
        lang.nextStep();
      }
      sc.unhighlight(14);
      sc.toggleHighlight(23, 24);
      lang.nextStep();
    }
    sc.toggleHighlight(24, 25);
    Knoten.getKreis().changeColor("fillColor", Color.white, null, null);
    lang.nextStep();

    return Ergebnis;
  }

  private Knoten[] pop(Knoten[] stack) {
    Knoten[] stack2 = new Knoten[stack.length - 1];
    String[] viso = new String[stack.length - 1];
    st.hide();
    for (int i = 1; i < stack.length; i++) {
      stack2[i - 1] = stack[i];
      viso[i - 1] = stack[i].getCircleName();
    }
    if (stack.length - 1 != 0) {
      st = lang.newStringArray(new Coordinates(800, 490), viso, "st", null);
      st.changeColor("fillColor", Color.white, null, null);
    }
    return stack2;
  }

  private Knoten[] expand(Knoten knoten, Knoten[] baum, Knoten[] stack) {
    List<Knoten> soehne = new ArrayList<Knoten>();
    for (int i = 0; i < baum.length - 1; i++) {
      if (baum[i].getVaKoor().getX() == knoten.getCoor().getX()
          && baum[i].getVaKoor().getY() == knoten.getCoor().getY()) {
        soehne.add(baum[i]);
      }
    }
    if (st != null) {
      st.hide();
    }

    Knoten[] NachfolgeKnoten;
    String[] visualisierung;
    try {
      NachfolgeKnoten = new Knoten[stack.length + soehne.size()];
      visualisierung = new String[stack.length + soehne.size()];
      int x = soehne.size() - 1;
      for (Knoten k : soehne) {
        NachfolgeKnoten[x] = k;
        visualisierung[x] = k.getCircleName();
        x--;
      }
    } catch (Exception e) {
      return new Knoten[0];
    }

    if (NachfolgeKnoten.length != stack.length) {
      st = lang.newStringArray(new Coordinates(800, 490), visualisierung, "st",
          null);
      st.changeColor("fillColor", Color.white, null, null);
    } else {
      String[] s = { "" };
      st = lang.newStringArray(new Coordinates(800, 490), s, "st", null);
      st.changeColor("fillColor", Color.white, null, null);
    }

    return NachfolgeKnoten;
  }

  public String getName() {
    return "Iterative Tiefensuche";
  }

  public String getAlgorithmName() {
    return "Iterative Tiefensuche";
  }

  public String getAnimationAuthor() {
    return "Jannik Schildknecht, Sergej Alexeev";
  }

  public String getDescription() {
    String description = "Bei der iterativen Tiefensuche handelt es sich um die Kombination von den wünschenswerten "
        + "\n"
        + "Eigenschaften von Tiefensuche (geringer Speicherverbrauch) und Breitensuche (Optimalität). "
        + "\n"
        + "Dabei wird die beschränkte Tiefensuche benutzt, bei der nur bis zu einer bestimmten, vorgegebenen "
        + "\n"
        + "Tiefe alle Knoten erkundet werden. Es wird mit der Tiefe 0 gestartet. Wird das gesuchte Element nicht "
        + "\n"
        + "gefunden, wird die Tiefe um 1 erhöht und die Tiefensuche gestartet! Falls das gesuchte Element "
        + "\n"
        + "gefundet wird, so wird es zurückgegeben, ansonsten terminiert der Algorithmus erst dann, wenn die "
        + "\n"
        + "vorgegebene Tiefe überschritten wird. Die Laufzeit des Algorithmus beträgt O(|Kanten|+|Knoten|).";
    description = description.replace("ö", "&ouml;").replace("ä", "&auml;")
        .replace("ü", "&uuml;");
    return description;
  }

  public String getCodeExample() {
    return "public boolean IT(Knoten[] Baum, Knoten Ziel, SourceCode sc) {"
        + "\n"
        + "   int IterationsTiefe = 0;"
        + "\n"
        + "   while (IterationsTiefe  getHoechsteTiefe(Baum)) { "
        + "\n"
        + "	     Ergebnis = Beschraenkte_Tiefensuche(Baum[Baum.length - 1], Ziel,"
        + "\n"
        + "			IterationsTiefe, Baum, sc);"
        + "\n"
        + "	     if (Ergebnis.equals(Ziel)) {"
        + "\n"
        + "		    return true;"
        + "\n"
        + "	     }"
        + "\n"
        + "	     IterationsTiefe = IterationsTiefe + 1;"
        + "\n"
        + "   }"
        + "\n"
        + "   return false;"
        + "\n"
        + "}"
        + "\n"
        + "public Knoten Beschraenkte_Tiefensuche(Knoten Knoten, Knoten Ziel,"
        + "\n"
        + " int Tiefe, Knoten[] Baum, SourceCode sc) {"
        + "\n"
        + "  if (Knoten == Ziel) {"
        + "\n"
        + "     return Knoten;"
        + "\n"
        + "  }"
        + "\n"
        + "  else {"
        + "\n"
        + "   if (Knoten.getTiefe()  IterationsTiefe) {"
        + "\n"
        + "	    stack = expand(Knoten, Baum, stack);"
        + "\n"
        + "      while (stack.length != 0) {"
        + "\n"
        + "        Knoten NaechsterKnoten = stack[0];"
        + "\n"
        + "	      stack = pop(stack);"
        + "\n"
        + "	      Ergebnis = Beschraenkte_Tiefensuche(NaechsterKnoten, Ziel, Tiefe, Baum, sc);"
        + "\n" + "	      if (Ergebnis.equals(Ziel)) {" + "\n"
        + "		     return Ergebnis;" + "\n" + "        }" + "\n" + "      }"
        + "\n" + "   }" + "\n" + " }" + "\n" + " return Ergebnis;" + "\n" + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public class Knoten {

    private int         tiefe   = 0;
    private Circle      kreis   = null;
    private String      name    = null;
    private Coordinates koor    = null;
    private int         vaterID = 0;
    private Coordinates vaKoor;

    public Knoten(Circle c, int t, String na, Coordinates co, Coordinates vaKo,
        int vID) {
      this.kreis = c;
      this.tiefe = t;
      this.name = na;
      this.koor = co;
      this.setVaKoor(vaKo);
      this.vaterID = vID;
    }

    public int getTiefe() {
      return tiefe;
    }

    public void setTiefe(int tiefe) {
      this.tiefe = tiefe;
    }

    public Circle getKreis() {
      return kreis;
    }

    public void setKreis(Circle kreis) {
      this.kreis = kreis;
    }

    public String getCircleName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Coordinates getCoor() {
      return koor;
    }

    public void setCoor(Coordinates coor) {
      this.koor = coor;
    }

    public int getVaterID() {
      return vaterID;
    }

    public void setVaterID(int anzahlDerBrueder) {
      this.vaterID = anzahlDerBrueder;
    }

    public Coordinates getVaKoor() {
      return vaKoor;
    }

    public void setVaKoor(Coordinates vaKoor) {
      this.vaKoor = vaKoor;
    }
  }

}