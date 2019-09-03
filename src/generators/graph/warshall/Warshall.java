package generators.graph.warshall;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class Warshall implements ValidatingGenerator {

  private Language             lang;
  private int[][]              adjacentMatrix;
  private TextProperties       text;
  private PolylineProperties   polyline;
  private SourceCodeProperties sourceCode;
  private Color                verbindungsLinieVT;
  private Color                transitiverKnoten;
  private Color                aktuellerKnoten;
  private Color                verbindungsLinieAV;
  private Color                neueVerbindung;
  private Color                verbindungsKnoten;
  private Circle[]             c;
  private Circle[][]           g;
  private Polyline[][]         p;
  private Text[][]             t;
  private Rect[][]             r;
  private List<Text>           te = new ArrayList<Text>();
  private Coordinates[]        n;

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    adjacentMatrix = (int[][]) primitives.get("adjacentMatrix");
    text = (TextProperties) props.getPropertiesByName("text");
    polyline = (PolylineProperties) props.getPropertiesByName("polyline");
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    verbindungsLinieVT = (Color) primitives.get("verbindungsLinieVT");
    transitiverKnoten = (Color) primitives.get("transitiverKnoten");
    aktuellerKnoten = (Color) primitives.get("aktuellerKnoten");
    verbindungsLinieAV = (Color) primitives.get("verbindungsLinieAV");
    verbindungsKnoten = (Color) primitives.get("verbindungsKnoten");
    neueVerbindung = (Color) primitives.get("neueVerbindung");

    // Ablauf der Animation
    davor();

    SourceCode s = danach();
    zeichneGraph(adjacentMatrix, text);

    // Graph und Matrix sind global definiert
    doWarshall(adjacentMatrix, s);
    hideThings();
    s.hide();
    epilog();

    lang.finalizeGeneration();
    return lang.toString();
  }

  private void zeichneGraph(int[][] adjacentMatrix, TextProperties text) {
    int nGon = adjacentMatrix.length;
    n = new Coordinates[nGon];
    Coordinates center = new Coordinates(700, 200);
    int radius;
    c = new Circle[adjacentMatrix.length];
    CircleProperties circle = new CircleProperties();
    circle.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    circle.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
    circle.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

    if (nGon < 6) {
      radius = 50;
    } else {
      radius = 50 * nGon / 4;
    }
    for (int i = 0; i < nGon; i++) {
      double ratio = Math.PI * 2 * i / nGon;
      n[i] = new Coordinates((int) (Math.sin(ratio) * radius + center.getX()),
          (int) (Math.cos(ratio) * radius + center.getY()));
      c[i] = lang.newCircle(n[i], 15, "Kreis" + i, null, circle);
      te.add(lang.newText(new Coordinates(n[i].getX() - 5, n[i].getY() - 5), ""
          + i, "KreisName" + i, null, text));
    }

    g = new Circle[adjacentMatrix.length][adjacentMatrix.length];
    circle.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.black);

    p = new Polyline[nGon][nGon];
    polyline.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    for (int i = 0; i < nGon; i++) {
      for (int j = 0; j < nGon; j++) {
        // Es besteht eine Verbindung
        if (adjacentMatrix[i][j] == 1 && i != j) {
          g[i][j] = lang.newCircle(getCoordinates(n[i], n[j]), 3, "Von" + i
              + " zu" + j, null, circle);
          Node[] node = { n[i], n[j] };
          p[i][j] = lang.newPolyline(node, "p" + i, null, polyline);
        }
      }
    }

    int tx = 1100;
    int ty = 25;
    for (int i = 0; i < nGon; i++) {
      te.add(lang.newText(new Coordinates(tx, ty), "p" + i, "b", null, text));
      tx += 30;
    }
    tx = 1075;
    ty = 50;
    for (int i = 0; i < nGon; i++) {
      te.add(lang.newText(new Coordinates(tx, ty), "p" + i, "b", null, text));
      ty += 30;
    }

    RectProperties rp = new RectProperties();
    rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.white);
    rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
    rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    r = new Rect[nGon][nGon];
    t = new Text[nGon][nGon];
    int mx = 1105;
    int my = 50;
    for (int i = 0; i < nGon; i++) {
      for (int k = 0; k < nGon; k++) {
        t[i][k] = lang.newText(new Coordinates(mx, my), ""
            + adjacentMatrix[i][k], "mt" + i, null, text);
        r[i][k] = lang.newRect(new Offset(-3, -3, t[i][k], "NW"), new Offset(3,
            3, t[i][k], "SE"), "r", null, rp);
        mx += 30;
      }
      my += 30;
      mx = 1105;
    }

  }

  public String getAlgorithmName() {
    return "Warshall";
  }

  public String getAnimationAuthor() {
    return "Jannik Schildknecht, Sergej Alexeev";
  }

  public String getCodeExample() {
    return "for (k = 1; k  n; k++)" + "\n" + "   for (i = 1; i  n; i++)" + "\n"
        + "     if (d[i,k] == 1 && i != k)" + "\n"
        + "       for( j = 1; i  n; j ++)" + "\n"
        + "         if( d[k,j] == 1) " + "\n" + "            d[i,j] = 1";
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getDescription() {
    String description = "Dies ist ein Algorithmus zur Bestimmung der transitiven Hülle eines Graphen. "
        + "\n"
        + "Dazu wird die Matrix benutzt, die alle Verbindungslinien im Graphen anzeigt. Man versucht hierbei "
        + "\n"
        + "die Matrix durchzugehen und alle transitiven Erreichbarkeiten dort einzutragen. Ist dies getan, hat man "
        + "\n"
        + "eine Matrix, die alle Erreichbarkeiten für jeden Knoten enthält. Die Laufzeit des Algorithmus beträgt O(n^3).";
    description.replace("ü", "&uuml;").replace("ä", "&auml;")
        .replace("^3", "&sup3;");
    return description;
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
  }

  public String getName() {
    return "Warshall";
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public void init() {
    lang = new AnimalScript("Warshall",
        "Jannik Schildknecht und Sergej Alexeev", 800, 600);
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
  }

  public boolean validateInput(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) throws IllegalArgumentException {

    int[][] adjMatrix = (int[][]) arg1.get("adjacentMatrix");
    if (adjMatrix.length != adjMatrix[0].length)
      throw new IllegalArgumentException("Die Matrix ist nicht quadratisch!");
    return true;
  }

  private void davor() {
    // Ueberschrift der Funktion
    Text b = lang.newText(new Coordinates(300, 20), "Warshall", "w1", null);

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
            "Beim Warshall Algorithmus geht es darum, eine transitive Huelle eines Graphen zu bilden.",
            "t2", null, text);
    Text t3 = lang
        .newText(
            new Coordinates(20, 100),
            "Dazu wird die Matrix benutzt, die alle Verbindungslinien im Graphen anzeigt.",
            "t3", null, text);
    Text t4 = lang
        .newText(
            new Coordinates(20, 120),
            "Man versucht hierbei die Matrix durchzugehen und alle transitiven Erreichbarkeiten dort einzutragen",
            "t4", null, text);
    Text t5 = lang
        .newText(
            new Coordinates(20, 140),
            "Ist dies getan, hat man eine Matrix, die alle Erreichbarkeiten für jeden Knoten enthaellt.",
            "t5", null, text);
    Text t6 = lang.newText(new Coordinates(20, 160),
        "Die Laufzeit des Algorithmus betraegt O(n^3).", "t5", null, text);

    lang.nextStep("Prolog");

    // Verstecken des Prologs
    t2.hide();
    t3.hide();
    t4.hide();
    t5.hide();
    t6.hide();

  }

  private void doWarshall(int[][] aMatrix, SourceCode s) {
    s.highlight(0);
    lang.nextStep("Algorithmus");
    Polyline transitivePolyline = null;
    Circle transitiveCircle = null;
    for (int i = 0; i < aMatrix.length; i++) {

      s.toggleHighlight(0, 1);
      c[i].changeColor("fillColor", aktuellerKnoten, null, null);
      lang.nextStep("Spalte: " + i);

      for (int j = 0; j < aMatrix[i].length; j++) {

        s.toggleHighlight(1, 2);
        lang.nextStep();

        r[j][i].changeColor("fillColor", verbindungsLinieAV, null, null);
        if (p[j][i] != null) {
          p[j][i].changeColor("Color", verbindungsLinieAV, null, null);
          g[j][i].changeColor("fillColor", verbindungsLinieAV, null, null);
          if (p[i][j] != null)
            p[i][j].changeColor("Color", verbindungsLinieAV, null, null);
        }

        if (i != j)
          c[j].changeColor("fillColor", verbindungsKnoten, null, null);

        s.toggleHighlight(2, 3);
        lang.nextStep();

        if (aMatrix[j][i] == 1 && j != i) {
          for (int k = 0; k < aMatrix[i].length; k++) {

            s.toggleHighlight(3, 4);

            if (aMatrix[j][k] == 1 && i > 1 && j > 1 && i != j) {
              MultipleChoiceQuestionModel ersteEntscheidung = new MultipleChoiceQuestionModel(
                  "Q2");
              ersteEntscheidung
                  .setPrompt("Erweitert der erweiterte Knoten die Erreichbarkeit des ersten Knotens?");
              ersteEntscheidung.addAnswer(
                  "Ja, er erweitert dessen Erreichbarkeit", 0,
                  "Falsch, es bestand schon ein Weg");
              ersteEntscheidung.addAnswer(
                  "Nein, er erweitert dessen Erreichbarkeit nicht", 1,
                  "Richtig, da dieser Weg schon bestand");
              lang.addMCQuestion(ersteEntscheidung);
            }

            if (aMatrix[j][k] == 0 && i != j) {
              MultipleChoiceQuestionModel zweiteEntscheidung = new MultipleChoiceQuestionModel(
                  "Q3");
              zweiteEntscheidung
                  .setPrompt("Erweitert der erweiterte Knoten die Erreichbarkeit des ersten Knotens?");
              zweiteEntscheidung.addAnswer(
                  "Ja, er erweitert dessen Erreichbarkeit", 1,
                  "Richtig, es bestand noch kein Weg");
              zweiteEntscheidung.addAnswer(
                  "Nein, er erweitert dessen Erreichbarkeit nicht", 0,
                  "Falsch, da dieser Weg noch nicht bestand");
              lang.addMCQuestion(zweiteEntscheidung);
            }

            if (i == 3 && j == 4 && aMatrix[j][k] == 0 && aMatrix[i][k] == 1) {
              MultipleChoiceQuestionModel ersterWeg = new MultipleChoiceQuestionModel(
                  "Q1");
              ersterWeg
                  .setPrompt("Welcher Weg zu welchem Knoten wird neu erschlossen?");
              ersterWeg.addAnswer("Der Weg von 4 zu 3", 0,
                  "Falsch, dieser Weg ist bereits existent");
              ersterWeg
                  .addAnswer("Der Weg von 4 zu 0", 1,
                      "Richtig, dieser Weg ist nun ueber den Knoten 4 erschlie?bar");
              ersterWeg.addAnswer("Der Weg von 3 zu 0", 0,
                  "Falsch, dieser Weg ist bereits existent");
              lang.addMCQuestion(ersterWeg);
            }
            lang.nextStep();

            if (j != i && k != i) {
              r[i][k].changeColor("fillColor", verbindungsLinieVT, null, null);
              if (p[i][k] != null && j != k) {
                p[i][k].changeColor("Color", verbindungsLinieVT, null, null);
                g[i][k]
                    .changeColor("fillColor", verbindungsLinieVT, null, null);
                if (p[k][i] != null && j != k)
                  p[k][i].changeColor("Color", verbindungsLinieVT, null, null);
              }
            }

            if (i != k && j != k)
              c[k].changeColor("fillColor", transitiverKnoten, null, null);

            s.toggleHighlight(4, 5);
            lang.nextStep();

            if (aMatrix[i][k] == 1) {

              s.toggleHighlight(5, 6);

              if (i != j && k != i) {
                r[j][k].changeColor("fillColor", neueVerbindung, null, null);

                if (j != k) {
                  Node[] tK = { c[k].getCenter(), c[j].getCenter() };

                  PolylineProperties p = new PolylineProperties();
                  p.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
                  transitivePolyline = lang.newPolyline(tK, "Tk", null, p);
                  transitivePolyline.changeColor("Color", neueVerbindung, null,
                      null);

                  CircleProperties circle = new CircleProperties();
                  circle.set(AnimationPropertiesKeys.COLOR_PROPERTY,
                      Color.black);
                  circle.set(AnimationPropertiesKeys.FILL_PROPERTY,
                      neueVerbindung);
                  circle.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
                  transitiveCircle = lang.newCircle(getCoordinates(n[j], n[k]),
                      3, "Tc", null, circle);
                }
              }
              t[j][k].setText("1", null, null);
              lang.nextStep();
              aMatrix[j][k] = 1;

              if (i != j && k != i) {
                r[j][k].changeColor("fillColor", Color.white, null, null);
                if (j != k) {
                  transitivePolyline.hide();
                  transitiveCircle.hide();
                }
              }
            } else
              s.unhighlight(5);

            if (j != i && k != i) {
              r[i][k].changeColor("fillColor", Color.white, null, null);
              if (p[i][k] != null && j != k) {
                p[i][k].changeColor("Color", Color.black, null, null);
                g[i][k].changeColor("fillColor", Color.black, null, null);
              }
              if (p[k][i] != null && j != k)
                p[k][i].changeColor("Color", Color.black, null, null);
            }
            s.unhighlight(6);
            if (i != k && j != k) {
              c[k].changeColor("fillColor", Color.white, null, null);
            }
          }
        } else
          s.unhighlight(3);
        r[j][i].changeColor("fillColor", Color.white, null, null);
        if (p[j][i] != null) {
          p[j][i].changeColor("Color", Color.black, null, null);
          g[j][i].changeColor("fillColor", Color.black, null, null);
        }
        if (p[i][j] != null)
          p[i][j].changeColor("Color", Color.black, null, null);
        if (i != j) {
          c[j].changeColor("fillColor", Color.white, null, null);
        }
      }
      c[i].changeColor("fillColor", Color.white, null, null);
      lang.nextStep();
    }
    s.unhighlight(2);
    lang.nextStep();
  }

  private SourceCode danach() {

    SourceCode sc = lang.newSourceCode(new Coordinates(40, 100), "sourceCode",
        null, sourceCode);

    sc.addCodeLine("public boolean Wharshall(int[][] adjazenzMatrix){", null,
        0, null);
    sc.addCodeLine("    for(int i = 0; i<adjazenzMatrix.size(); i++)", null, 0,
        null);
    sc.addCodeLine("       for(int j = 0; j<adjazenzMatrix[i].size(); j++)",
        null, 0, null);
    sc.addCodeLine("           if(adjazenzMatrix[j][i] == 1 && i != j)", null,
        0, null);
    sc.addCodeLine(
        "               for(int k = 0; k<adjazenzMatrix[i].size(); k++)", null,
        0, null);
    sc.addCodeLine("                    if(adjazenzMatrix[i][k] == 1)", null,
        0, null);
    sc.addCodeLine("                       adjazenzMatrix[j][k] = 1", null, 0,
        null);

    te.add(lang.newText(new Coordinates(900, 400),
        "Verbindungslinie zwischen aktuellem Knoten und Verbindungsknoten",
        "Legende1", null));
    te.get(te.size() - 1).changeColor("Color", verbindungsLinieAV, null, null);
    te.add(lang.newText(new Coordinates(900, 430),
        "Verbindungslinie zwischen Verbindungsknoten und dem transitiven.",
        "Legende1", null));
    te.get(te.size() - 1).changeColor("Color", verbindungsLinieVT, null, null);
    te.add(lang.newText(new Coordinates(900, 460),
        "Neu generierte(, transitive) Verbindung in der Matrix.", "Legende1",
        null));
    te.get(te.size() - 1).changeColor("Color", neueVerbindung, null, null);

    te.add(lang.newText(new Coordinates(50, 400),
        "Aktueller Knoten, von dem aus die Linien ueberprueft werden.",
        "Legende1", null));
    te.get(te.size() - 1).changeColor("Color", aktuellerKnoten, null, null);
    te.add(lang
        .newText(
            new Coordinates(50, 430),
            "Es wird ueberprueft, ob der Knoten zum ersten Knoten eine Verbindung hat. Gibt es eine, bleibt der Knoten bestehen.",
            "Legende2", null));
    te.get(te.size() - 1).changeColor("Color", verbindungsKnoten, null, null);
    te.add(lang.newText(new Coordinates(50, 460),
        "Sind die beiden ersten Punkte gefunden, wird ueberprueft,",
        "Legende2", null));
    te.get(te.size() - 1).changeColor("Color", transitiverKnoten, null, null);
    te.add(lang
        .newText(
            new Coordinates(50, 475),
            "ob der Knoten durch die Verbindung der ersten beiden Knoten deren Erreichbarkeit erweitert.",
            "Legende3", null));
    te.get(te.size() - 1).changeColor("Color", transitiverKnoten, null, null);

    return sc;
  }

  private void epilog() {
    te.add(lang.newText(new Coordinates(120, 200),
        "Es wurde erfolgreich die Transitive Huelle bestimmt.", "e1", null));
    te.add(lang
        .newText(
            new Coordinates(120, 230),
            "Die 1en in der Matrix zeigen nicht nur die direkten Erreichbarkeiten an, sondern auch die, ueber die dritte Knoten.",
            "e2", null));
    te.add(lang
        .newText(
            new Coordinates(120, 260),
            "Der Bruderalgorithmus Floyd haette nicht nur die Erreichbarkeiten bestimmt sondern auch die optimale Strecke ermittelt,",
            "e3", null));
    te.add(lang
        .newText(new Coordinates(120, 290),
            "diese haette statt den 1en in der Matrix Einzug erhalten.", "e4",
            null));
    lang.nextStep("Epilog");
  }

  private void hideThings() {
    for (int i = 0; i < te.size(); i++) {
      te.get(i).hide();
    }
    if (t != null && r != null) {
      for (int i = 0; i < adjacentMatrix.length; i++) {
        for (int j = 0; j < adjacentMatrix.length; j++) {
          t[i][j].hide();
          r[i][j].hide();
          if (p[i][j] != null) {
            p[i][j].hide();
            g[i][j].hide();
          }
        }
        c[i].hide();
      }
    }
  }

  private Node getCoordinates(Coordinates vonKoordinate,
      Coordinates zuKoordinate) {
    Node ergebnis;
    double a = zuKoordinate.getX() - vonKoordinate.getX();
    double b = zuKoordinate.getY() - vonKoordinate.getY();
    double c = Math.sqrt(a * a + b * b);
    double winkel = Math.acos(b / c);
    if (zuKoordinate.getX() < vonKoordinate.getX()) {
      int x = (int) (zuKoordinate.getX() + Math.sin(winkel) * 18);
      int y = (int) (zuKoordinate.getY() - Math.cos(winkel) * 18);
      ergebnis = new Coordinates(x, y);
    } else {
      int x = (int) (zuKoordinate.getX() - Math.sin(winkel) * 18);
      int y = (int) (zuKoordinate.getY() - Math.cos(winkel) * 18);
      ergebnis = new Coordinates(x, y);
    }

    return ergebnis;
  }

}
