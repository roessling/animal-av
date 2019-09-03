package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleSelectionQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class DBSCAN implements Generator {
  private static class nPoint {
    public Point            p;
    public int              status;
    public int              cluster;
    public Circle           c;
    public Circle           c1;
    public CircleProperties cp;
    public CircleProperties cp1;

    public nPoint(int x, int y, Language lang, int eps, int zoom) {
      p = new Point(x, y);
      status = 0;
      cluster = 0;
      cp = new CircleProperties();
      cp1 = new CircleProperties();
      cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      cp1.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
      c = lang.newCircle(new Coordinates(startYAchse + (x * 2), startXAchse
          - (y * 2)), 2 * 2, "P" + name + "", null, cp);
      c1 = lang.newCircle(new Coordinates(startYAchse + (x * 2), startXAchse
          - (y * 2)), eps * 2 - 1, "P" + name + "", null, cp1);
      c1.hide();
      name++;
    }

  }

  private int                  first;
  private Language             lang;
  private int                  eps;
  private int[][]              intPoints;
  private int                  zoom        = 2;
  private ArrayProperties      amProps;
  private int                  minPts;
  private SourceCodeProperties codeProps;
  private SourceCodeProperties codeProps1;
  private SourceCodeProperties codeProps2;
//  private TextProperties       textProps;

  // internevariablen
  public static int            startYAchse = 400;
  public static int            startXAchse = 200;
  public SourceCode            code;
  public SourceCode            code1;
  public SourceCode            code2;
  public SourceCode            legend;
  public StringArray           arr;
  private Text                 textNbrs;
//  private HashSet<nPoint>      clearnPts;
  public static int            name        = 0;
  public Color[]               cc          = { Color.GRAY, Color.RED,
      Color.BLUE, Color.MAGENTA, Color.CYAN, Color.ORANGE, Color.PINK };

  Polyline                     pl;
  ArrayList<Polyline>          polyline;
  static Text                  title;
  Rect                         titleRect;

  public void init() {
    lang = new AnimalScript("DBSCAN", "Daniel Dieth und Benedikt Wartusch",
        800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    eps = (Integer) primitives.get("eps");
    intPoints = (int[][]) primitives.get("intPoints");
    amProps = (ArrayProperties) props.getPropertiesByName("amProps");
    minPts = (Integer) primitives.get("minPts");
    codeProps = (SourceCodeProperties) props.getPropertiesByName("codeProps");
    codeProps1 = (SourceCodeProperties) props.getPropertiesByName("codeProps1");
    codeProps2 = (SourceCodeProperties) props.getPropertiesByName("codeProps2");
//    textProps = (TextProperties) props.getPropertiesByName("textProps");

    run();

    HashSet<nPoint> p = new HashSet<nPoint>();
//    clearnPts = new HashSet<nPoint>();

    for (int i = 0; i < intPoints[0].length; i++) {
      p.add(new nPoint(intPoints[0][i], intPoints[1][i], lang, eps, minPts));
    }
    showKoordiantes(p);
    dbscan(p, eps, minPts);

    outro(p);
    lang.nextStep("Zusammenfassung / Fragen");

    // Fragen
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    MultipleSelectionQuestionModel algoQ1 = new MultipleSelectionQuestionModel(
        "question1");
    algoQ1.setPrompt("Ist die Auswahl der Cluster deterministisch?");
    algoQ1
        .addAnswer(
            "Nein",
            1,
            "Richtig. Die Punkte werden von dem Algorithmus nicht-deterministisch einem der moeglichen Cluster zugeordnet");
    algoQ1
        .addAnswer(
            "Ja",
            0,
            "Falsch. Die Punkte werden von dem Algorithmus nicht-deterministisch einem der moeglichen Cluster zugeordnet");
    lang.addMSQuestion(algoQ1);
    MultipleSelectionQuestionModel algoQ2 = new MultipleSelectionQuestionModel(
        "question2");
    algoQ2
        .setPrompt("Was ist die Grundidee des Algorithmus?(Tipp: Schau nach dem Namen)");
    algoQ2
        .addAnswer(
            "Dichteverbundenheit",
            1,
            "Richtig. Durch die Dichteverbundenheit koennen potenzielle Cluster erkanntwerden und zusammengefuegt werden");
    algoQ2
        .addAnswer(
            "Clustererkennung",
            0,
            "Falsch. Das ist zwar das Ziel, aber nicht die Grundidee die zur Erreichung des Ziels benoetigt wird");
    lang.addMSQuestion(algoQ2);
    MultipleSelectionQuestionModel algoQ3 = new MultipleSelectionQuestionModel(
        "question3");
    algoQ3.setPrompt("Wie heissen die Knoten die grau makiert werden?");
    algoQ3
        .addAnswer(
            "Noise",
            1,
            "Richtig. Die Punkte werden als Noise bezeichnet, da sie keinem Cluster zugeordnet werden konnten. Dies bedeutet nun, dass diese Knoten im spaeteren Verlauf ignoriert werden(Bsp. wenn ein anderer Algorithmus mit den Daten weiterarbeitet)");
    algoQ3
        .addAnswer("Vertex", 0,
            "Falsch. Vertex ist das englische Wort fuer Knoten. Richtig waere hier Noise");
    lang.addMSQuestion(algoQ3);

    lang.finalizeGeneration();

    return lang.toString();
  }

  public String getName() {
    return "DBSCAN";
  }

  public String getAlgorithmName() {
    return "DBSCAN";
  }

  public String getAnimationAuthor() {
    return "Daniel Dieth, Benedikt Wartusch";
  }

  public String getDescription() {
    return "<h1> DBSCAN</h1>"
        + "\n"
        + "\n"
        + "DBSCAN (Density-Based Spatial Clustering of Applications with Noise, etwa: Dichtebasierte r&auml;umliche Clusteranalyse mit Rauschen) ist ein von Martin Ester, Hans-Peter Kriegel, Joerg Sander und Xiaowei Xu entwickelter Data-Mining-Algorithmus zur Clusteranalyse. Er ist einer der meist zitierten Algorithmen in diesem Bereich. Der Algorithmus arbeitet dichtebasiert und ist in der Lage, mehrere Cluster zu erkennen. Rauschpunkte werden dabei ignoriert und separat zurueckgeliefert."
        + "\n"
        + "</p>"
        + "\n"
        + "Die Grundidee des Algorithmus ist der Begriff der &quot;Dichteverbundenheit&quot;. Zwei Objekte gelten als dichte-verbunden, wenn es eine Kette von dichten Objekten (&quot;Kernobjekte&quot;, mit mehr als minPts Nachbarn) gibt, die diese Punkte miteinander verbinden. Die durch dieselben Kernobjekte miteinander verbundenen Objekte bilden einen &quot;Cluster&quot;. Objekte, die nicht Teil eines dichte-verbundenen Clusters sind, werden als &quot;Rauschen&quot; (Noise) bezeichnet.";
  }

  public String getCodeExample() {
    return "DBSCAN(D, eps, MinPts)" + "\n" + "   C = 0" + "\n"
        + "   for each unvisited point P in dataset D" + "\n"
        + "      mark P as visited" + "\n"
        + "      NeighborPts = regionQuery(P, eps)" + "\n"
        + "      if sizeof(NeighborPts) &lt; MinPts" + "\n"
        + "         mark P as NOISE" + "\n" + "      else" + "\n"
        + "         C = next cluster" + "\n"
        + "         expandCluster(P, NeighborPts, C, eps, MinPts)" + "\n"
        + "          " + "\n" + "expandCluster(P, NeighborPts, C, eps, MinPts)"
        + "\n" + "   add P to cluster C" + "\n"
        + "   for each point P' in NeighborPts " + "\n"
        + "      if P' is not visited" + "\n" + "         mark P' as visited"
        + "\n" + "         NeighborPts' = regionQuery(P', eps)" + "\n"
        + "         if sizeof(NeighborPts') &gt;= MinPts" + "\n"
        + "            NeighborPts = NeighborPts joined with NeighborPts'"
        + "\n" + "      if P' is not yet member of any cluster" + "\n"
        + "         add P' to cluster C" + "\n" + "          " + "\n"
        + "regionQuery(P, eps)" + "\n"
        + "   return all points within P's eps-neighborhood (including P)"
        + "\n";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  /**
   * @param plist
   *          Menge von Knoten
   * @param eps
   *          Minimalanzahl um neues Cluster zu werden !Umkreisceck!
   * @param minPts
   *          Minimalanzahl an Verdundenheiten fuer Punkt im Cluster
   */
  public void dbscan(HashSet<nPoint> plist, int eps, int minPts) {
    code.highlight(2);
    int cNr = 0;
    code.toggleHighlight(2, 3);
    lang.nextStep("Start DBSCAN");
    for (nPoint nPoint : plist) {
      if (nPoint.cluster == 0) {
        code.toggleHighlight(3, 4);
        lang.nextStep();
        if (nPoint.status == 0) {
          code.toggleHighlight(4, 5);
          // ######################################
          nPoint.c.changeColor("fillColor", Color.YELLOW, null, null);
          nPoint.c1.changeColor("color", Color.YELLOW, null, null);
          nPoint.c1.show();
          // ######################################
          lang.nextStep();
          nPoint.status = 1;// visited
          HashSet<nPoint> NeighborPts = regionQuery(plist, nPoint, eps, cNr);
          // ######################################
          if (NeighborPts.size() >= minPts) {
            nPoint.c.changeColor("fillColor", cc[cNr + 1], null, null);
            nPoint.c1.changeColor("color", cc[cNr + 1], null, null);
            nPoint.c1.show();
            legend.addCodeLine("Cluster " + cNr + " :       "
                + getColor(cc[cNr + 1]), null, 3, null);
          }
          // ######################################
          code.toggleHighlight(5, 6);
          lang.nextStep();
          nPoint.c1.hide();
          code.toggleHighlight(6, 7);
          lang.nextStep();
          if (NeighborPts.size() < minPts) {
            code.toggleHighlight(7, 8);
            // ######################################
            nPoint.c.changeColor("fillColor", cc[0], null, null);
            nPoint.c1.changeColor("color", cc[0], null, null);
            nPoint.c1.show();
            // ######################################
            lang.nextStep("Noise Gefunden: (" + nPoint.p.x + ";" + nPoint.p.y
                + ")");
            code.unhighlight(8);
            // verstecken von Umkreis Noise
            nPoint.c1.hide();

            nPoint.cluster = -1;
          } else {
            code.toggleHighlight(7, 10);
            lang.nextStep("Faerbe alle neuen Punkte fuer Cluster[" + (cNr + 1)
                + "] um Punkt: (" + nPoint.p.x + ";" + nPoint.p.y + ") ein");
            cNr++;
            code.toggleHighlight(10, 11);
            lang.nextStep();
            // verstecken von Umkreis
            nPoint.c1.hide();

            expandCluster(nPoint, NeighborPts, cNr, eps, minPts);
            code.unhighlight(11);
          }
        }
      }
    }
  }

  /**
   * @param p
   * @param neighborPts
   * @param eps
   * @param minPts
   */
  public void expandCluster(nPoint p, HashSet<nPoint> neighborPts, int cNr,
      int eps, int minPts) {
    code1.highlight(1);
    lang.nextStep();
    p.cluster = cNr;
    code1.toggleHighlight(1, 2);
    lang.nextStep();
    for (nPoint nPoint : neighborPts) {

      code1.toggleHighlight(2, 3);
      lang.nextStep();
      if (nPoint.status == 0) { // noch unbesucht
        code1.toggleHighlight(3, 4);
        lang.nextStep();
        nPoint.status = 1; // visited
        // ######################################
        nPoint.c.changeColor("fillColor", cc[cNr], null, null);
        nPoint.c1.changeColor("color", cc[cNr], null, null);
        nPoint.c1.show();

        // ######################################
        code1.toggleHighlight(4, 5);
        lang.nextStep();
        // verstecken von Umkreis
        nPoint.c1.hide();
        first = 1;
        HashSet<nPoint> neighborPts2 = regionQuery(neighborPts, nPoint, eps,
            cNr);
        first = 0;
        code1.toggleHighlight(5, 6);
        lang.nextStep();
        if (neighborPts2.size() >= minPts) {
          code1.toggleHighlight(6, 7);
          lang.nextStep();
          neighborPts.addAll(neighborPts2);
          code1.unhighlight(7);
        }
        code1.unhighlight(6);
        code1.highlight(8);
        lang.nextStep();
        if (nPoint.cluster == 0) {
          code1.toggleHighlight(8, 9);
          lang.nextStep();
          nPoint.cluster = cNr;
          code1.unhighlight(9);
          lang.nextStep();
        }
      }
      nPoint.c1.hide();
    }
    code1.unhighlight(2);
    lang.nextStep();
  }

  /**
   * @param plist
   * @param p
   * @param eps
   * @param cNr
   */
  public HashSet<nPoint> regionQuery(HashSet<nPoint> plist, nPoint p, int eps,
      int cNr) {
    code2.highlight(1);
    if (first == 1) {
      lang.nextStep("Punkt faerben fuer neues Cluster[" + cNr + "]: (" + p.p.x
          + ";" + p.p.y + ")");
    } else {
      lang.nextStep("Erster Punkt fuer neues Cluster[" + cNr + "] gefunden: ("
          + p.p.x + ";" + p.p.y + ")");
    }
    HashSet<nPoint> temp = new HashSet<nPoint>();
    p.status = 2;

    for (nPoint nP : plist) {
      if (nP.status != 2) {
        double calc = Math.sqrt(Math.pow(nP.p.getX() - p.p.getX(), 2)
            + Math.pow(nP.p.getY() - p.p.getY(), 2));
        if (calc < eps) {
          temp.add(nP);
          // ######################################
          nP.c.changeColor("fillColor", Color.YELLOW, null, null);
          nP.c1.show();
          nP.c1.changeColor("color", Color.YELLOW, null, null);
          lang.nextStep();
          // ######################################
        }
      }
    }
    if (temp.size() > 0) {
      String[] str = new String[temp.size()];
      int i = 0;

      for (nPoint nPoint : temp) {
        str[i] = "Point(" + nPoint.p.x + ";" + nPoint.p.y + ")";
        i++;
      }

      if (arr != null)
        arr.hide();

      arr = lang.newStringArray(new Coordinates(85, 580), str, "arr", null,
          amProps);
    }
    code2.unhighlight(1);
    lang.nextStep();

    return temp;
  }

  private String getColor(Color color) {

    // Color.GRAY,Color.RED, Color.BLUE, Color.MAGENTA,
    // Color.CYAN,Color.ORANGE,Color.PINK
    if (color.equals(Color.GRAY)) {
      return "GRAY";
    } else if (color.equals(Color.RED)) {
      return "RED";
    } else if (color.equals(Color.BLUE)) {
      return "BLUE";
    } else if (color.equals(Color.MAGENTA)) {
      return "MAGENTA";
    } else if (color.equals(Color.CYAN)) {
      return "CYAN";
    } else if (color.equals(Color.ORANGE)) {
      return "ORANGE";
    } else if (color.equals(Color.PINK)) {
      return "PINK";
    } else if (color.equals(Color.YELLOW)) {
      return "YELLOW";
    } else
      return null;
  }

  private void run() {
    title = lang.newText(new Coordinates(20, 30), "DBSCAN", "title", null);
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    titleRect = lang.newRect(new Offset(-5, -5, title, "NW"), new Offset(5, 5,
        title, "SE"), "titleRect", null, rectProps);

    SourceCode description = ShowExplanation(title);

    lang.nextStep("Einfuehrung / Erklaerung");
    description.hide();

    showSourceCode();

  }

  private SourceCode ShowExplanation(Text title) {
    SourceCode description = lang.newSourceCode(new Offset(0, 20, title, "N"),
        "description", null);
    description
        .addCodeLine(
            "Beschreibung des Density-Based Spatial Clustering of Applications with Noise",
            null, 0, null);
    description.addCodeLine("(kurz: DBSCAN)", null, 0, null);
    description.addCodeLine("", null, 0, null);
    description
        .addCodeLine(
            "Die Grundidee des Algorithmus ist der Begriff der 'Dichteverbundenheit'.",
            null, 0, null);
    description
        .addCodeLine(
            "Zwei Objekte gelten als dichte-verbunden, wenn es eine Kette von dichten Objekten",
            null, 0, null);
    description
        .addCodeLine(
            "('Kernobjekte', mit mehr als minPts Nachbarn) gibt, die diese Punkte miteinander",
            null, 0, null);
    description
        .addCodeLine(
            "verbinden. Die durch dieselben Kernobjekte miteinander verbundenen Objekte bilden",
            null, 0, null);
    description
        .addCodeLine(
            "einen 'Cluster'. Objekte, die nicht Teil eines dichte-verbundenen Clusters sind,",
            null, 0, null);
    description.addCodeLine("werden als 'Rauschen' (Noise) bezeichnet.", null,
        0, null);
    description.addCodeLine("In DBSCAN gibt es drei Arten von Punkten:", null,
        0, null);
    description.addCodeLine("1. Kernobjekte, welche selbst dicht sind.", null,
        0, null);
    description
        .addCodeLine(
            "2. Dichte-erreichbare Objekte. Dies sind Objekte, die zwar von einem Kernobjekt des",
            null, 0, null);
    description
        .addCodeLine(
            "   Clusters erreicht werden koennen, selbst aber nicht dicht sind. Anschaulich bilden",
            null, 0, null);
    description.addCodeLine("   diese den Rand eines Clusters.", null, 0, null);
    description.addCodeLine(
        "3. Rauschpunkte, die weder dicht, noch dichte-erreichbar sind.", null,
        0, null);
    description.addCodeLine("", null, 0, null);
    description
        .addCodeLine(
            "Dichte-erreichbare Punkte koennen von mehr als einem Cluster dichte-erreichbar",
            null, 0, null);
    description
        .addCodeLine(
            "sein. Diese Punkte werden von dem Algorithmus nicht-deterministisch einem der",
            null, 0, null);
    description
        .addCodeLine(
            "moeglichen Cluster zugeordnet. Dies impliziert auch, dass Dichteverbundenheit",
            null, 0, null);
    description.addCodeLine(
        "nicht transitiv ist; Dichte-Erreichbarkeit ist nicht symmetrisch.",
        null, 0, null);
    description.addCodeLine("", null, 0, null);
    description.addCodeLine("", null, 0, null);
    description.addCodeLine("", null, 0, null);
    return description;
  }

  private void showSourceCode() {
    Offset codeN = new Offset(0, 20, "title", AnimalScript.DIRECTION_N);

    code = lang.newSourceCode(codeN, "theCode", null, codeProps);
    code.addCodeLine("Algorithmus in Pseudo-Code:", null, 0, null);
    code.addCodeLine("===========================", null, 0, null);
    code.addCodeLine("DBSCAN(D, " + eps + ", " + minPts + ")", null, 0, null);
    code.addCodeLine("C = 0", null, 2, null);
    code.addCodeLine("for each unvisited point P in dataset D", null, 2, null);
    code.addCodeLine("mark P as visited", null, 4, null);
    code.addCodeLine("NeighborPts = regionQuery(P, " + eps + ")", null, 4, null);
    code.addCodeLine("if sizeof(NeighborPts) < MinPts", null, 4, null);
    code.addCodeLine("mark P as NOISE", null, 6, null);
    code.addCodeLine("else", null, 4, null);
    code.addCodeLine("C = next cluster", null, 6, null);
    code.addCodeLine("expandCluster(P, NeighborPts, C, " + eps + ", " + minPts
        + ")", null, 6, null);

    Offset codeN1 = new Offset(0, 270, "title", AnimalScript.DIRECTION_N);

    code1 = lang.newSourceCode(codeN1, "theCode1", null, codeProps1);
    code1.addCodeLine("expandCluster(P, NeighborPts, C, " + eps + ", " + minPts
        + ")", null, 0, null);
    code1.addCodeLine("add P to cluster C", null, 2, null);
    code1.addCodeLine("for each point P' in NeighborPts ", null, 2, null);
    code1.addCodeLine("if P' is not visited", null, 4, null);
    code1.addCodeLine("mark P' as visited", null, 6, null);
    code1.addCodeLine("NeighborPts' = regionQuery(P', eps)", null, 6, null);
    code1.addCodeLine("if sizeof(NeighborPts') >= MinPts", null, 6, null);
    code1.addCodeLine("NeighborPts = NeighborPts joined with NeighborPts'",
        null, 8, null);
    code1.addCodeLine("if P' is not yet member of any cluster", null, 4, null);
    code1.addCodeLine("add P' to cluster C", null, 6, null);

    Offset codeN2 = new Offset(0, 490, "title", AnimalScript.DIRECTION_N);

    code2 = lang.newSourceCode(codeN2, "theCode2", null, codeProps2);
    code2.addCodeLine("regionQuery(P, " + eps + ")", null, 0, null);
    code2.addCodeLine("return all points within P's eps-neighborhood", null, 2,
        null);
  }

  private void showKoordiantes(HashSet<nPoint> p) {

    Node[] L = { new Coordinates(startYAchse, 5),
        new Coordinates(startYAchse, startXAchse),
        new Coordinates(startYAchse + 300, startXAchse) };
    pl = lang.newPolyline(L, "XY-Achse", null);

    Polyline temppl;

    polyline = new ArrayList<Polyline>();
    for (int i = 0; i < 15; i++) {
      Node[] L2 = {
          new Coordinates(startYAchse + i * 10 * zoom, startXAchse - 3),
          new Coordinates(startYAchse + i * 10 * zoom, startXAchse + 3) };
      temppl = lang.newPolyline(L2, "xLine" + i, null);
      polyline.add(temppl);
    }
    for (int i = 0; i < 10; i++) {
      Node[] L2 = {
          new Coordinates(startYAchse - 3, startXAchse - i * 10 * zoom),
          new Coordinates(startYAchse + 3, startXAchse - i * 10 * zoom) };
      polyline.add(lang.newPolyline(L2, "YLine" + i, null));
    }

    Offset sourcelegend = new Offset(350, 200, "title",
        AnimalScript.DIRECTION_N);
    legend = lang.newSourceCode(sourcelegend, "legend", null, codeProps);
    legend.addCodeLine("Legende:", null, 0, null);
    legend.addCodeLine("NeighbourPts: YELLOW", null, 3, null);
    legend.addCodeLine("Noise :           GREY", null, 3, null);

    textNbrs = lang.newText(new Coordinates(10, 578), "NeighborPts :",
        "neighpts", null);
  }

  private void outro(HashSet<nPoint> p) {

    for (nPoint nPoint : p) {
      nPoint.c.hide();
      nPoint.c1.hide();
    }
    pl.hide();
    for (Polyline poly : polyline) {
      poly.hide();
    }

    for (nPoint nPoint : p) {
      nPoint.c1.hide();
    }

    code.hide();
    code1.hide();
    code2.hide();
    legend.hide();
    arr.hide();
    textNbrs.hide();

    codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 13));

    // conclusion
    Offset outroSW = new Offset(5, 30, "title", AnimalScript.DIRECTION_SW);
    SourceCode outro = lang.newSourceCode(outroSW, "outro", null, codeProps);
    outro
        .addCodeLine(
            "DBSCAN ist exakt in Bezug auf die Definition von dichte-verbunden und Noise.",
            null, 0, null);
    outro
        .addCodeLine(
            "Das bedeutet, zwei dichte-verbundene Objekte sind garantiert im selben Cluster,",
            null, 0, null);
    outro.addCodeLine("waehrend Rauschobjekte sicher in Noise sind.", null, 0,
        null);
    outro
        .addCodeLine(
            "Nicht exakt ist der Algorithmus bei nur dichte-erreichbaren Clustern,",
            null, 0, null);
    outro.addCodeLine(
        "diese werden nur einem Cluster zugeordnet, nicht allen moeglichen.",
        null, 0, null);

    outro.addCodeLine("Im Gegensatz beispielsweise zum K-Means-Algorithmus, ",
        null, 0, null);
    outro.addCodeLine(
        "muss nicht im vornherein bekannt sein, wie viele Cluster existieren.",
        null, 0, null);
    outro
        .addCodeLine(
            "Der Algorithmus kann Cluster beliebiger Form (z.B. nicht nur kugelfoermige) erkennen.",
            null, 0, null);
    outro.addCodeLine("", null, 0, null);
    outro.addCodeLine(
        "DBSCAN ist weitgehend deterministisch und reihenfolgeunabhaengig: ",
        null, 0, null);
    outro
        .addCodeLine(
            "Unabhaengig davon, in welcher Reihenfolge Objekte in der Datenbank abgelegt ",
            null, 0, null);
    outro.addCodeLine("oder verarbeitet werden, entstehen dieselben Cluster ",
        null, 0, null);
    outro
        .addCodeLine(
            "(mit der Ausnahme der nur dichte-erreichbaren Nicht-Kern-Objekte und der Cluster-Nummerierung)",
            null, 0, null);
    outro.addCodeLine("", null, 0, null);
    outro
        .addCodeLine(
            "Der Algorithmus kann mit beliebigen Distanzfunktionen und Aehnlichkeitsmassen verwendet werden.",
            null, 0, null);
    outro
        .addCodeLine(
            "Im Gegensatz zum K-Means-Algorithmus ist kein geometrischer Raum notwendig, da kein Mittelpunkt",
            null, 0, null);
    outro.addCodeLine("berechnet werden muss.", null, 0, null);

    Offset comSW = new Offset(0, 20, "outro", AnimalScript.DIRECTION_SW);
    SourceCode complexity = lang.newSourceCode(comSW, "complexity", null,
        codeProps);
    complexity.addCodeLine("DBSCAN selbst ist von linearer Komplexitaet. ",
        null, 0, null);
    complexity.addCodeLine(
        "Jedes Objekt wird im Wesentlichen nur ein mal besucht. ", null, 0,
        null);
    complexity.addCodeLine(
        "Jedoch ist die Berechnung der -Nachbarschaft im Regelfall ", null, 0,
        null);
    complexity
        .addCodeLine(
            "nicht in konstanter Zeit moeglich (ohne entsprechende Vorberechnungen). ",
            null, 0, null);
    complexity.addCodeLine(
        "Ohne die Verwendung von vorberechneten Daten oder einer geeigneten ",
        null, 0, null);
    complexity
        .addCodeLine(
            "Indexstruktur ist der Algorithmus also von quadratischer Komplexitaet.",
            null, 0, null);
    complexity.addCodeLine("", null, 0, null);
    complexity.addCodeLine("", null, 0, null);
    complexity.addCodeLine("Bemerkungen angelehnt an wikipedia.org", null, 0,
        null);
    complexity
        .addCodeLine("http://de.wikipedia.org/wiki/DBSCAN", null, 0, null);

  }

}