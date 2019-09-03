package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.primitives.Group;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PointProperties;
import algoanim.properties.PolygonProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.exceptions.NotEnoughNodesException;

/**
 * 
 * @author David Sessler
 * 
 */
public class DeBoorAlgorithmus implements Generator {
  private Language             lang;

  private double               t;
  private int                  q;
  private int                  j;

  private Color                basicColor;
  private Color                pointHighlightColor;
  private Color                textHighlightColor;
  private Color                lineHighlightColor;

  private int                  width;
  private int                  height;

  private int                  frameWidth;
  private int                  frameHeight;

  private int[][]              pointArray;
  private double[][]           startPoints;
  private double[]             nodeVector;
  private String[][]           pointMatrix;
  private String[][]           nodeMatrix;

  private int                  gridSize;

  private Group                introGroup;
  private Group                outroGroup;
  private Group                startValuesGroup;
  private Group                utilityFrameGroup;
  private Group                utilityGroup;
  private Group                graphGroup;
  private Group                compGroup;
  private Group                descGroup;
  private Group                formulaGroup;
  private Group                lambdaGroup;
  private Group                calc1Group;
  private Group                calc2Group;
  private Group                jGroup;
  private Group                initialPointTextGroup;
  private Group                simplePointTextGroup;

  private Group                desc1;
  private Text                 desc2;
  private Group                desc3;
  private Text                 desc4;
  private Text                 desc5;

  private ArrayList<Group>     pointGroupList;
  private LinkedList<Polyline> lineList;
  private ArrayList<Primitive> simplePointList;

  private DecimalFormat        f;

  private int                  xPosOffsetBugFixNr;

  /**
   * Tries to fix y position bug by creating and hiding the first reference to
   * an identifier.
   * 
   * @param identifier
   */
  private void fixYPosOffsetBug(String identifier) {
    lang.newText(new Offset(5, 3, identifier, AnimalScript.DIRECTION_NW),
        "fix", identifier + "_offsetFix_" + xPosOffsetBugFixNr++, null).hide();
  }

  /**
   * Sets main frame of the animation.
   */
  private void setFrame() {
    RectProperties frameProps = new RectProperties();
    frameProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    frameProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
    frameProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    lang.newRect(new Coordinates(5, 5), new Coordinates(frameWidth + 5,
        frameHeight + 5), "frame", null, frameProps);

    fixYPosOffsetBug("frame");
    TextProperties titleProps = new TextProperties();
    titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 14));
    lang.newText(new Offset(5, 5, "frame", AnimalScript.DIRECTION_NW),
        "de Boor Algorithmus", "title", null, titleProps);

    RectProperties rectTitleProps = new RectProperties();
    rectTitleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectTitleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectTitleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Offset(-5, -3, "title", AnimalScript.DIRECTION_NW),
        new Offset(5, 3, "title", AnimalScript.DIRECTION_SE), "rectTitle",
        null, rectTitleProps);
  }

  /**
   * Creates IntroFrame and groups it's elements.
   * 
   * @param q
   * @param t
   */
  private void setIntroFrame(int q, double t) {
    LinkedList<Primitive> introList = new LinkedList<Primitive>();

    RectProperties inroFrameProps = new RectProperties();
    inroFrameProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    inroFrameProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    inroFrameProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    introList.add(lang.newRect(new Offset(5, 5, "rectTitle",
        AnimalScript.DIRECTION_SW), new Coordinates(frameWidth, frameHeight),
        "introFrame", null, inroFrameProps));

    TextProperties introTitleProps = new TextProperties();
    introTitleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    introTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 11));
    fixYPosOffsetBug("introFrame");
    introList.add(lang.newText(new Offset(5, 2, "introFrame",
        AnimalScript.DIRECTION_NW), "Intro", "introTitle", null,
        introTitleProps));

    TextProperties introTextProps = new TextProperties();
    introTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    introTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 12));

    TextProperties subscriptProps = new TextProperties();
    subscriptProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    subscriptProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 10));

    TextProperties formelProps = new TextProperties();
    formelProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    formelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 12));

    RectProperties introDescTitleProps = new RectProperties();
    introDescTitleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    introDescTitleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    introDescTitleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    introList.add(lang
        .newRect(new Offset(-5, -3, "introTitle", AnimalScript.DIRECTION_NW),
            new Offset(5, 3, "introTitle", AnimalScript.DIRECTION_SE),
            "rectIntroTitle", null, introDescTitleProps));

    introList
        .add(lang
            .newText(
                new Offset(15, 15, "rectIntroTitle", AnimalScript.DIRECTION_SW),
                "Der de Boor Algorithmus dient dazu, einen beliebigen Punkt S(t) einer B-Splinekurve effizient zu berechnen.",
                "introText0", null, introTextProps));
    introList
        .add(lang
            .newText(
                new Offset(0, 15, "introText0", AnimalScript.DIRECTION_SW),
                "Eine B-Splinekurve vom Grad q mit k inneren Knoten ist durch die Kontrollpunkte b    ... b   und den Knotenvektor",
                "introText1", null, introTextProps));
    introList.add(lang.newText(new Offset(0, 50, "introText1",
        AnimalScript.DIRECTION_SW),
        "auf dem Intervall [a, b] hinreichend bestimmt.", "introText2", null,
        introTextProps));
    introList.add(lang.newText(new Offset(0, 25, "introText2",
        AnimalScript.DIRECTION_SW),
        "Im Folgenden betrachten wir eine B-Splinekurve vom Grad " + q
            + ". Diese wird beispielhaft im Punkt t = " + t + " ausgewertet.",
        "introText3", null, introTextProps));
    introList
        .add(lang
            .newText(
                new Offset(0, 15, "introText3", AnimalScript.DIRECTION_SW),
                "Die Grundidee hinter dem Algorithmus wird durch eine graphische Konstruktion verdeutlicht.",
                "introText4", null, introTextProps));
    introList.add(lang.newText(new Offset(0, 25, "introText4",
        AnimalScript.DIRECTION_SW),
        "Die Auswertung für Kurven höherer Grade funktioniert analog.",
        "introText5", null, introTextProps));

    introList.add(lang.newText(new Offset(448, 3, "introText1",
        AnimalScript.DIRECTION_NW), "-q        k", "subscriptIntro", null,
        subscriptProps));

    introList.add(lang.newText(new Offset(0, 15, "introText1",
        AnimalScript.DIRECTION_SW),
        "x  ≤ ... ≤ x   ≤ x  = a < x  ... x  < x    = b ≤ ... ≤ x",
        "formel1Intro", null, formelProps));
    introList.add(lang.newText(new Offset(0, 5, "formel1Intro",
        AnimalScript.DIRECTION_NW),
        " -q          -1    0        1      k    k+1               k+q+1",
        "formel2Intro", null, formelProps));

    introGroup = lang.newGroup(introList, "introGroup");
  }

  /**
   * Creates OutroFrame and groups it's elements.
   */
  private void setOutroFrame() {
    LinkedList<Primitive> outroList = new LinkedList<Primitive>();

    RectProperties outroFrameProps = new RectProperties();
    outroFrameProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    outroFrameProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    outroFrameProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    Rect outroFrame = lang.newRect(new Offset(5, 5, "rectTitle",
        AnimalScript.DIRECTION_SW), new Coordinates(frameWidth, frameHeight),
        "outroFrame", null, outroFrameProps);

    TextProperties outroTitleProps = new TextProperties();
    outroTitleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    outroTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 11));
    fixYPosOffsetBug("outroFrame");
    Text outroTitle = lang.newText(new Offset(5, 2, "outroFrame",
        AnimalScript.DIRECTION_NW), "Fakten zum de Boor Algorithmus",
        "outroTitle", null, outroTitleProps);

    TextProperties outroTextProps = new TextProperties();
    outroTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    outroTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 12));

    RectProperties outroDescTitleProps = new RectProperties();
    outroDescTitleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    outroDescTitleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    outroDescTitleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    Rect rectOutroTitle = lang
        .newRect(new Offset(-5, -3, "outroTitle", AnimalScript.DIRECTION_NW),
            new Offset(5, 3, "outroTitle", AnimalScript.DIRECTION_SE),
            "rectOutroTitle", null, outroDescTitleProps);

    outroList.add(lang.newText(new Offset(15, 15, "rectOutroTitle",
        AnimalScript.DIRECTION_SW),
        "Abschließend noch Wissenswertes zum de Boor Algorithmus:",
        "outroText0", null, outroTextProps));
    outroList
        .add(lang
            .newText(
                new Offset(0, 25, "outroText0", AnimalScript.DIRECTION_SW),
                " - Der de Boor Algorithmus kann dazu genutzt werden das Kontrollpolygon im Bereich des ausgewerteten Punktes zu verfeinern.",
                "outroText1", null, outroTextProps));
    outroList
        .add(lang
            .newText(
                new Offset(0, 15, "outroText1", AnimalScript.DIRECTION_SW),
                "   Dazu müssen nur die betrachteten Anfangspunkte (erste Spalte im Dreiecksschema) durch die errechneten Punkte",
                "outroText2", null, outroTextProps));
    outroList
        .add(lang
            .newText(
                new Offset(0, 15, "outroText2", AnimalScript.DIRECTION_SW),
                "   (oberer und unterer Rand des Dreiecksschemas) ersetzt und der Knotenvektor angepasst werden.",
                "outroText3", null, outroTextProps));
    outroList.add(lang.newText(new Offset(0, 25, "outroText3",
        AnimalScript.DIRECTION_SW),
        " - Der Algorithmus besitzt eine Komplexität von O(q²).", "outroText4",
        null, outroTextProps));
    outroList
        .add(lang
            .newText(
                new Offset(0, 25, "outroText4", AnimalScript.DIRECTION_SW),
                " - Der Grad q hängt nicht von der Anzahl der Knoten des Kontrollpolygons ab. Dies bietet zum einen eine hohe Effizienz bei der Auswertung",
                "outroText5", null, outroTextProps));
    outroList
        .add(lang
            .newText(
                new Offset(0, 15, "outroText5", AnimalScript.DIRECTION_SW),
                "   und ist andererseits der Grund für gute Lokalitätseigenschaften von B-Splinekurven.",
                "outroText6", null, outroTextProps));

    outroList.add(outroFrame);
    outroList.add(outroTitle);
    outroList.add(rectOutroTitle);
    outroGroup = lang.newGroup(outroList, "outroGroup");
  }

  /**
   * Creates DescriptionFrame and groups it's elements.
   */
  private void setDescriptionFrame() {
    LinkedList<Primitive> descGroupList = new LinkedList<Primitive>();

    RectProperties descFrameProps = new RectProperties();
    descFrameProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    descFrameProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    descFrameProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    Rect descFrame = lang.newRect(new Offset(5, 70, "frame",
        AnimalScript.DIRECTION_NW), new Coordinates(width / 2 + 5,
        height / 2 + 25), "descFrame", null, descFrameProps);

    TextProperties descTitleProps = new TextProperties();
    descTitleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    descTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 11));
    Text descTitle = lang.newText(new Offset(5, 2, "descFrame",
        AnimalScript.DIRECTION_NW), "Beschreibung", "descTitle", null,
        descTitleProps);

    RectProperties rectDescTitleProps = new RectProperties();
    rectDescTitleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectDescTitleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectDescTitleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    Rect rectDescTitle = lang.newRect(new Offset(-5, -3, "descTitle",
        AnimalScript.DIRECTION_NW), new Offset(5, 3, "descTitle",
        AnimalScript.DIRECTION_SE), "rectDescTitle", null, rectDescTitleProps);

    descGroupList.add(descFrame);
    descGroupList.add(descTitle);
    descGroupList.add(rectDescTitle);
    descGroup = lang.newGroup(descGroupList, "descGroup");
  }

  /**
   * Creates CompFrame and groups it's elements.
   */
  private void setCompFrame() {
    LinkedList<Primitive> compGroupList = new LinkedList<Primitive>();

    RectProperties compFrameProps = new RectProperties();
    compFrameProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    compFrameProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    compFrameProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    Rect compFrame = lang.newRect(new Offset(0, 6, "descFrame",
        AnimalScript.DIRECTION_SW),
        new Coordinates(width / 2 + 5, frameHeight), "compFrame", null,
        compFrameProps);

    TextProperties compTitleProps = new TextProperties();
    compTitleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    compTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 11));
    Text compTitle = lang.newText(new Offset(5, 3, "compFrame",
        AnimalScript.DIRECTION_NW), "Rechnung", "compTitle", null,
        compTitleProps);

    RectProperties rectCompTitleProps = new RectProperties();
    rectCompTitleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectCompTitleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectCompTitleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    Rect rectCompTitle = lang.newRect(new Offset(-5, -3, "compTitle",
        AnimalScript.DIRECTION_NW), new Offset(5, 3, "compTitle",
        AnimalScript.DIRECTION_SE), "rectCompTitle", null, rectCompTitleProps);

    compGroupList.add(compFrame);
    compGroupList.add(compTitle);
    compGroupList.add(rectCompTitle);
    compGroup = lang.newGroup(compGroupList, "compGroup");
  }

  /**
   * Creates GraphFrame and the grid and groups it's elements.
   */
  private void setGraphFrame() {
    LinkedList<Primitive> graphGroupList = new LinkedList<Primitive>();

    RectProperties graphFrameProps = new RectProperties();
    graphFrameProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    graphFrameProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    graphFrameProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    Rect graphFrame = lang.newRect(new Offset(6, 0, "descFrame",
        AnimalScript.DIRECTION_NE),
        new Coordinates(frameWidth, height / 2 + 25), "graphFrame", null,
        graphFrameProps);

    TextProperties graphTitleProps = new TextProperties();
    graphTitleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    graphTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 11));
    Text graphTitle = lang.newText(new Offset(5, 3, "graphFrame",
        AnimalScript.DIRECTION_NW), "Graphische Darstellung", "graphTitle",
        null, graphTitleProps);

    RectProperties rectGraphTitleProps = new RectProperties();
    rectGraphTitleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectGraphTitleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectGraphTitleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    Rect rectGraphTitle = lang
        .newRect(new Offset(-5, -3, "graphTitle", AnimalScript.DIRECTION_NW),
            new Offset(5, 3, "graphTitle", AnimalScript.DIRECTION_SE),
            "rectGraphTitle", null, rectGraphTitleProps);

    RectProperties gridProps = new RectProperties();
    gridProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
    gridProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    gridProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    gridProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    Rect grid = lang.newRect(new Offset(5, 5, "rectGraphTitle",
        AnimalScript.DIRECTION_SW), new Offset(-5, -5, "graphFrame",
        AnimalScript.DIRECTION_SE), "grid", null, gridProps);

    int gridHeight = height / 2 - 80; // geschätzt
    int gridWidth = frameWidth / 2 - 18;

    int lineX = gridSize;
    int lineY = gridSize;

    PolylineProperties gridLineProps = new PolylineProperties();
    gridLineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
    while (lineX < gridWidth) {
      graphGroupList.add(lang.newPolyline(new Node[] {
          new Offset(lineX, 0, "grid", AnimalScript.DIRECTION_SW),
          new Offset(lineX, 0, "grid", AnimalScript.DIRECTION_NW) }, "lineX"
          + lineX, null, gridLineProps));
      lineX = lineX + gridSize;
    }

    while (lineY < gridHeight) {
      graphGroupList.add(lang.newPolyline(new Node[] {
          new Offset(0, -lineY, "grid", AnimalScript.DIRECTION_SW),
          new Offset(0, -lineY, "grid", AnimalScript.DIRECTION_SE) }, "lineY"
          + lineY, null, gridLineProps));
      lineY = lineY + gridSize;
    }

    graphGroupList.add(graphFrame);
    graphGroupList.add(graphTitle);
    graphGroupList.add(rectGraphTitle);
    graphGroupList.add(grid);
    graphGroup = lang.newGroup(graphGroupList, "graphGroup");
  }

  /**
   * Creates UtilityFrame and groups it's elements.
   */
  private void setUtilityFrame() {
    LinkedList<Primitive> utilityGroupList = new LinkedList<Primitive>();

    RectProperties utilFrameProps = new RectProperties();
    utilFrameProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    utilFrameProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    utilFrameProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    Rect utilFrame = lang.newRect(new Offset(6, 6, "descFrame",
        AnimalScript.DIRECTION_SE), new Coordinates(frameWidth, frameHeight),
        "utilFrame", null, utilFrameProps);

    TextProperties utilTitleProps = new TextProperties();
    utilTitleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    utilTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 11));
    Text utilTitle = lang.newText(new Offset(5, 3, "utilFrame",
        AnimalScript.DIRECTION_NW), "Dreiecksschema", "utilTitle", null,
        utilTitleProps);

    RectProperties rectUtilTitleProps = new RectProperties();
    rectUtilTitleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectUtilTitleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectUtilTitleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    Rect rectUtilTitle = lang.newRect(new Offset(-5, -3, "utilTitle",
        AnimalScript.DIRECTION_NW), new Offset(5, 3, "utilTitle",
        AnimalScript.DIRECTION_SE), "rectUtilTitle", null, rectUtilTitleProps);

    utilityGroupList.add(utilTitle);
    utilityGroupList.add(rectUtilTitle);
    utilityGroupList.add(utilFrame);
    utilityFrameGroup = lang.newGroup(utilityGroupList, "utilityFrameGroup");
  }

  /**
   * Creates FormulaGroup in the desired location, groups it's elements.
   * 
   * @param offsetX
   * @param offsetY
   * @param identifier
   * @param direction
   */
  private void setFormulaGroup(int offsetX, int offsetY, String identifier,
      String direction) {
    LinkedList<Primitive> formulaGroupList = new LinkedList<Primitive>();

    TextProperties formulaProps = new TextProperties();
    formulaProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    formulaProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 18));
    Text formula = lang.newText(new Offset(offsetX, offsetY, identifier,
        direction), "b =λ (t)*b  +λ (t)*b", "formula", null, formulaProps);

    TextProperties superscriptProps = new TextProperties();
    superscriptProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    superscriptProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 9));

    Text superscript1 = lang
        .newText(new Offset(12, -10, "formula", AnimalScript.DIRECTION_NW),
            "l", "superscript1", null, superscriptProps);
    Text superscript2 = lang
        .newText(new Offset(45, 0, "formula", AnimalScript.DIRECTION_NW), "l",
            "superscript2", null, superscriptProps);
    Text superscript3 = lang.newText(new Offset(111, 0, "formula",
        AnimalScript.DIRECTION_NW), "l-1", "superscript3", null,
        superscriptProps);
    Text superscript4 = lang
        .newText(new Offset(155, 0, "formula", AnimalScript.DIRECTION_NW), "l",
            "superscript4", null, superscriptProps);
    Text superscript5 = lang.newText(new Offset(221, 0, "formula",
        AnimalScript.DIRECTION_NW), "l-1", "superscript5", null,
        superscriptProps);
    Text subscript1 = lang.newText(new Offset(12, 13, "formula",
        AnimalScript.DIRECTION_NW), "i", "subscript1", null, superscriptProps);
    Text subscript2 = lang
        .newText(new Offset(45, 13, "formula", AnimalScript.DIRECTION_NW),
            "0,i", "subscript2", null, superscriptProps);
    Text subscript3 = lang
        .newText(new Offset(111, 13, "formula", AnimalScript.DIRECTION_NW),
            "i-1", "subscript3", null, superscriptProps);
    Text subscript4 = lang
        .newText(new Offset(155, 13, "formula", AnimalScript.DIRECTION_NW),
            "1,i", "subscript4", null, superscriptProps);
    Text subscript5 = lang.newText(new Offset(221, 13, "formula",
        AnimalScript.DIRECTION_NW), "i", "subscript5", null, superscriptProps);

    formulaGroupList.add(formula);
    formulaGroupList.add(superscript1);
    formulaGroupList.add(superscript2);
    formulaGroupList.add(superscript3);
    formulaGroupList.add(superscript4);
    formulaGroupList.add(superscript5);
    formulaGroupList.add(subscript1);
    formulaGroupList.add(subscript2);
    formulaGroupList.add(subscript3);
    formulaGroupList.add(subscript4);
    formulaGroupList.add(subscript5);
    formulaGroup = lang.newGroup(formulaGroupList, "formulaGroup");
  }

  /**
   * Creates LambdaGroup in the desired location, groups it's elements.
   * 
   * @param offsetX
   * @param offsetY
   * @param identifier
   * @param direction
   */
  private void setLambdaGroup(int offsetX, int offsetY, String identifier,
      String direction) {
    LinkedList<Primitive> lambdaGroupList = new LinkedList<Primitive>();

    TextProperties lambdaProps = new TextProperties();
    lambdaProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    lambdaProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 18));

    Text lambda1 = lang.newText(new Offset(offsetX, offsetY, identifier,
        direction), "λ (t)=", "lambda1", null, lambdaProps);
    Text lambda2 = lang.newText(new Offset(3, -12, "lambda1",
        AnimalScript.DIRECTION_NE), " t-x ", "lambda2", null, lambdaProps);
    Text lambda3 = lang.newText(new Offset(0, -3, "lambda2",
        AnimalScript.DIRECTION_SW), "x   -x", "lambda3", null, lambdaProps);
    Text lambda4 = lang.newText(new Offset(120, 0, "lambda1",
        AnimalScript.DIRECTION_NE), "λ (t)=1-λ (t)", "lambda4", null,
        lambdaProps);

    TextProperties superscriptProps = new TextProperties();
    superscriptProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    superscriptProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 9));

    Text superscript6 = lang
        .newText(new Offset(12, -10, "lambda1", AnimalScript.DIRECTION_NW),
            "l", "superscript6", null, superscriptProps);
    Text superscript7 = lang
        .newText(new Offset(12, 0, "lambda4", AnimalScript.DIRECTION_NW), "l",
            "superscript7", null, superscriptProps);
    Text superscript8 = lang
        .newText(new Offset(100, 0, "lambda4", AnimalScript.DIRECTION_NW), "l",
            "superscript8", null, superscriptProps);

    Text subscript6 = lang
        .newText(new Offset(12, 13, "lambda1", AnimalScript.DIRECTION_NW),
            "1,i", "subscript6", null, superscriptProps);
    Text subscript7 = lang.newText(new Offset(45, 13, "lambda2",
        AnimalScript.DIRECTION_NW), "i", "subscript7", null, superscriptProps);
    Text subscript8 = lang.newText(new Offset(12, 13, "lambda3",
        AnimalScript.DIRECTION_NW), "i+q+1-l", "subscript8", null,
        superscriptProps);
    Text subscript9 = lang.newText(new Offset(67, 13, "lambda3",
        AnimalScript.DIRECTION_NW), "i", "subscript9", null, superscriptProps);
    Text subscript10 = lang.newText(new Offset(12, 13, "lambda4",
        AnimalScript.DIRECTION_NW), "0,i", "subscript10", null,
        superscriptProps);
    Text subscript11 = lang.newText(new Offset(100, 13, "lambda4",
        AnimalScript.DIRECTION_NW), "1,i", "subscript11", null,
        superscriptProps);

    PolylineProperties lineProps = new PolylineProperties();
    lineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);

    Polyline fraction1 = lang.newPolyline(new Node[] {
        new Offset(0, 3, "lambda3", AnimalScript.DIRECTION_NW),
        new Offset(5, 3, "lambda3", AnimalScript.DIRECTION_NE) }, "fraction1",
        null, lineProps);

    lambdaGroupList.add(lambda1);
    lambdaGroupList.add(lambda2);
    lambdaGroupList.add(lambda3);
    lambdaGroupList.add(lambda4);
    lambdaGroupList.add(superscript6);
    lambdaGroupList.add(superscript7);
    lambdaGroupList.add(superscript8);
    lambdaGroupList.add(subscript6);
    lambdaGroupList.add(subscript7);
    lambdaGroupList.add(subscript8);
    lambdaGroupList.add(subscript9);
    lambdaGroupList.add(subscript10);
    lambdaGroupList.add(subscript11);
    lambdaGroupList.add(fraction1);
    lambdaGroup = lang.newGroup(lambdaGroupList, "lambdaGroup");
  }

  /**
   * Creates DescriptionGroup in the desired location, groups it's elements in
   * several different groups.
   * 
   * @param offsetX
   * @param offsetY
   * @param identifier
   * @param direction
   */
  private void setDescription(int offsetX, int offsetY, String identifier,
      String direction) {
    LinkedList<Primitive> desc1GroupList = new LinkedList<Primitive>();
    LinkedList<Primitive> desc3GroupList = new LinkedList<Primitive>();

    TextProperties descProps = new TextProperties();
    descProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    descProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));

    Text descText1 = lang.newText(new Offset(offsetX, offsetY, identifier,
        direction), "Zu berechnen: S(t) für ein t∈[x , x    ) mit j∈{0,...,k}",
        "descText1", null, descProps);
    desc2 = lang.newText(new Offset(0, 5, "descText1",
        AnimalScript.DIRECTION_SW), "1. Bestimme j", "desc2", null, descProps);
    Text descText3 = lang.newText(new Offset(0, 5, "desc2",
        AnimalScript.DIRECTION_SW), "2. Setze b  = b  mit i = j - q, ... , j",
        "descText3", null, descProps);
    desc4 = lang.newText(new Offset(0, 5, "descText3",
        AnimalScript.DIRECTION_SW), "3. Berechne für l = 1,..., q sukzessive:",
        "desc4", null, descProps);
    desc5 = lang.newText(new Offset(0, 40, "desc4", AnimalScript.DIRECTION_SW),
        " mit i = j - q + l, ... , j und", "desc5", null, descProps);

    TextProperties descSubscriptProps = new TextProperties();
    descSubscriptProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    descSubscriptProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 10));

    Text descSubscript1 = lang.newText(new Offset(207, 5, "descText1",
        AnimalScript.DIRECTION_NW), "j     j+1", "descSubscript1", null,
        descSubscriptProps);
    Text descSubscript2 = lang.newText(new Offset(70, -3, "descText3",
        AnimalScript.DIRECTION_NW), "0", "descSubscript2", null,
        descSubscriptProps);
    Text descSubscript3 = lang.newText(new Offset(70, 10, "descText3",
        AnimalScript.DIRECTION_NW), "i         i", "descSubscript3", null,
        descSubscriptProps);

    desc1GroupList.add(descText1);
    desc1GroupList.add(descSubscript1);
    desc1 = lang.newGroup(desc1GroupList, "desc1");

    desc3GroupList.add(descText3);
    desc3GroupList.add(descSubscript2);
    desc3GroupList.add(descSubscript3);
    desc3 = lang.newGroup(desc3GroupList, "desc3");
  }

  /**
   * Creates formula in the desired location with given parameters, groups it's
   * elements in two different groups.
   * 
   * @param offsetX
   * @param offsetY
   * @param identifier
   * @param direction
   * @param t
   * @param xi
   * @param x_i
   * @param q
   * @param l
   * @param i
   * @param bi_x
   * @param bi_y
   * @param b_i_x
   * @param b_i_y
   * @param result_x
   * @param result_y
   */
  private void setCalcGroups(int offsetX, int offsetY, String identifier,
      String direction, double t, double xi, double x_i, int q, int l, int i,
      double bi_x, double bi_y, double b_i_x, double b_i_y, double result_x,
      double result_y) {
    LinkedList<Primitive> calc1GroupList = new LinkedList<Primitive>();
    LinkedList<Primitive> calc2GroupList = new LinkedList<Primitive>();

    TextProperties calcProps = new TextProperties();
    calcProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    calcProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 18));

    TextProperties superscriptProps = new TextProperties();
    superscriptProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    superscriptProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 9));

    TextProperties paranthesisProps = new TextProperties();
    paranthesisProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    paranthesisProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 40));

    Text calcLambda1 = lang.newText(new Offset(offsetX, offsetY, identifier,
        direction), "λ (t)=", "calcLambda1", null, calcProps);
    Text calcLambda2 = lang.newText(new Offset(3, -12, "calcLambda1",
        AnimalScript.DIRECTION_NE), "t-x ", "calcLambda2", null, calcProps);
    Text calcLambda3 = lang.newText(new Offset(0, -3, "calcLambda2",
        AnimalScript.DIRECTION_SW), "x -x", "calcLambda3", null, calcProps);
    Text calcLambda4 = lang.newText(new Offset(130, 0, "calcLambda1",
        AnimalScript.DIRECTION_NE), "λ (t)=1-λ (t)", "calcLambda4", null,
        calcProps);

    Text calcSuperscript1 = lang.newText(new Offset(12, -10, "calcLambda1",
        AnimalScript.DIRECTION_NW), Integer.toString(l), "calcSuperscript1",
        null, superscriptProps);
    Text calcSuperscript2 = lang.newText(new Offset(12, 0, "calcLambda4",
        AnimalScript.DIRECTION_NW), Integer.toString(l), "calcSuperscript2",
        null, superscriptProps);
    Text calcSuperscript3 = lang.newText(new Offset(100, 0, "calcLambda4",
        AnimalScript.DIRECTION_NW), Integer.toString(l), "calcSuperscript3",
        null, superscriptProps);
    Text calcSubscript1 = lang.newText(new Offset(12, 13, "calcLambda1",
        AnimalScript.DIRECTION_NW), "1," + Integer.toString(i),
        "calcSubscript1", null, superscriptProps);
    Text calcSubscript2 = lang.newText(new Offset(34, 13, "calcLambda2",
        AnimalScript.DIRECTION_NW), Integer.toString(i), "calcSubscript2",
        null, superscriptProps);
    Text calcSubscript3 = lang.newText(new Offset(12, 13, "calcLambda3",
        AnimalScript.DIRECTION_NW), Integer.toString(i + q + 1 - l),
        "calcSubscript3", null, superscriptProps);
    Text calcSubscript4 = lang.newText(new Offset(45, 13, "calcLambda3",
        AnimalScript.DIRECTION_NW), Integer.toString(i), "calcSubscript4",
        null, superscriptProps);
    Text calcSubscript5 = lang.newText(new Offset(12, 13, "calcLambda4",
        AnimalScript.DIRECTION_NW), "0," + Integer.toString(i),
        "calcSubscript5", null, superscriptProps);
    Text calcSubscript6 = lang.newText(new Offset(100, 13, "calcLambda4",
        AnimalScript.DIRECTION_NW), "1," + Integer.toString(i),
        "calcSubscript6", null, superscriptProps);

    Polyline calcFraction1 = lang.newPolyline(new Node[] {
        new Offset(0, 3, "calcLambda3", AnimalScript.DIRECTION_NW),
        new Offset(7, 3, "calcLambda3", AnimalScript.DIRECTION_NE) },
        "calcFraction1", null);

    String numerator = f.format(t) + "-" + f.format(xi);
    String denominator = f.format(x_i) + "-" + f.format(xi);
    String lambda1 = f.format((t - xi) / (x_i - xi));
    String lambda0 = f.format(1 - (t - xi) / (x_i - xi));

    Text calcLambda5 = lang.newText(new Offset(0, 30, "calcLambda1",
        AnimalScript.DIRECTION_SW), "λ (" + f.format(t) + ")=", "calcLambda5",
        null, calcProps);
    Text calcLambda6 = lang.newText(new Offset(3, -12, "calcLambda5",
        AnimalScript.DIRECTION_NE), numerator, "calcLambda6", null, calcProps);
    Text calcLambda7 = lang
        .newText(new Offset(0, -3, "calcLambda6", AnimalScript.DIRECTION_SW),
            denominator, "calcLambda7", null, calcProps);
    Text calcLambda8 = lang.newText(
        new Offset(6 + Math.max(numerator.length(), denominator.length()) * 11,
            0, "calcLambda5", AnimalScript.DIRECTION_NE), "=" + lambda1,
        "calcLambda8", null, calcProps);
    Text calcLambda9 = lang.newText(new Offset(0, 18, "calcLambda5",
        AnimalScript.DIRECTION_SW), "λ (" + f.format(t) + ")=1-" + lambda1
        + "=" + lambda0, "calcLambda9", null, calcProps);

    Text calcSuperscript4 = lang.newText(new Offset(12, -10, "calcLambda5",
        AnimalScript.DIRECTION_NW), Integer.toString(l), "calcSuperscript4",
        null, superscriptProps);
    Text calcSuperscript5 = lang.newText(new Offset(12, 0, "calcLambda9",
        AnimalScript.DIRECTION_NW), Integer.toString(l), "calcSuperscript5",
        null, superscriptProps);
    Text calcSubscript7 = lang.newText(new Offset(12, 13, "calcLambda5",
        AnimalScript.DIRECTION_NW), "1," + Integer.toString(i),
        "calcSubscript7", null, superscriptProps);
    Text calcSubscript8 = lang.newText(new Offset(12, 13, "calcLambda9",
        AnimalScript.DIRECTION_NW), "0," + Integer.toString(i),
        "calcSubscript8", null, superscriptProps);

    Polyline calcFraction2 = lang.newPolyline(new Node[] {
        new Offset(0, 0, "calcLambda6", AnimalScript.DIRECTION_SW),
        new Offset(Math.max(numerator.length(), denominator.length()) * 11, 0,
            "calcLambda6", AnimalScript.DIRECTION_SW) }, "calcFraction2", null);

    calc1GroupList.add(calcLambda1);
    calc1GroupList.add(calcLambda2);
    calc1GroupList.add(calcLambda3);
    calc1GroupList.add(calcLambda4);
    calc1GroupList.add(calcLambda5);
    calc1GroupList.add(calcLambda6);
    calc1GroupList.add(calcLambda7);
    calc1GroupList.add(calcLambda8);
    calc1GroupList.add(calcLambda9);
    calc1GroupList.add(calcSuperscript1);
    calc1GroupList.add(calcSuperscript2);
    calc1GroupList.add(calcSuperscript3);
    calc1GroupList.add(calcSuperscript4);
    calc1GroupList.add(calcSuperscript5);
    calc1GroupList.add(calcSubscript1);
    calc1GroupList.add(calcSubscript2);
    calc1GroupList.add(calcSubscript3);
    calc1GroupList.add(calcSubscript4);
    calc1GroupList.add(calcSubscript5);
    calc1GroupList.add(calcSubscript6);
    calc1GroupList.add(calcSubscript7);
    calc1GroupList.add(calcSubscript8);
    calc1GroupList.add(calcFraction1);
    calc1GroupList.add(calcFraction2);
    calc1Group = lang.newGroup(calc1GroupList, "calc1Group");

    int calcText2Offset = (3 + lambda0.length()) * 11 + 28
        + Math.max(f.format(bi_x).length(), f.format(bi_y).length()) * 11;
    int calcText3Offset = calcText2Offset + 28 + (1 + lambda1.length()) * 11
        + Math.max(f.format(b_i_x).length(), f.format(b_i_y).length()) * 11;

    Text calcText1 = lang.newText(new Offset(offsetX, offsetY + 10, identifier,
        direction), "b =" + lambda0, "calcText1", null, calcProps);
    Text calcText2 = lang
        .newText(new Offset(calcText2Offset, 0, "calcText1",
            AnimalScript.DIRECTION_NW), "+" + lambda1, "calcText2", null,
            calcProps);
    Text calcText3 = lang.newText(new Offset(calcText3Offset, 0, "calcText1",
        AnimalScript.DIRECTION_NW), "=", "calcText3", null, calcProps);

    Text calcSuperscript6 = lang.newText(new Offset(12, -10, "calcText1",
        AnimalScript.DIRECTION_NW), Integer.toString(l), "calcSuperscript6",
        null, superscriptProps);
    Text calcSubscript9 = lang.newText(new Offset(12, 13, "calcText1",
        AnimalScript.DIRECTION_NW), Integer.toString(i), "calcSubscript9",
        null, superscriptProps);

    int paranthesisOffset1 = 25 + lambda0.length() * 11;
    int paranthesisOffset2 = paranthesisOffset1 + 19
        + Math.max(f.format(bi_x).length(), f.format(bi_y).length()) * 11;
    int paranthesisOffset3 = paranthesisOffset2 + 19 + lambda1.length() * 11;
    int paranthesisOffset4 = paranthesisOffset3 + 19
        + Math.max(f.format(b_i_x).length(), f.format(b_i_y).length()) * 11;
    int paranthesisOffset5 = paranthesisOffset4 + 23;
    int paranthesisOffset6 = paranthesisOffset5 + 19
        + Math.max(f.format(result_x).length(), f.format(result_y).length())
        * 11;

    Text paranthesis1 = lang.newText(new Offset(paranthesisOffset1, 15,
        "calcText1", AnimalScript.DIRECTION_NW), "(", "paranthesis1", null,
        paranthesisProps);
    Text paranthesis2 = lang.newText(new Offset(paranthesisOffset2, -17,
        "calcText1", AnimalScript.DIRECTION_NW), ")", "paranthesis2", null,
        paranthesisProps);
    Text paranthesis3 = lang.newText(new Offset(paranthesisOffset3, -17,
        "calcText1", AnimalScript.DIRECTION_NW), "(", "paranthesis3", null,
        paranthesisProps);
    Text paranthesis4 = lang.newText(new Offset(paranthesisOffset4, -17,
        "calcText1", AnimalScript.DIRECTION_NW), ")", "paranthesis4", null,
        paranthesisProps);
    Text paranthesis5 = lang.newText(new Offset(paranthesisOffset5, -17,
        "calcText1", AnimalScript.DIRECTION_NW), "(", "paranthesis5", null,
        paranthesisProps);
    Text paranthesis6 = lang.newText(new Offset(paranthesisOffset6, -17,
        "calcText1", AnimalScript.DIRECTION_NW), ")", "paranthesis6", null,
        paranthesisProps);

    Text textBi_X = lang.newText(new Offset(paranthesisOffset1 + 22, -31,
        "calcText1", AnimalScript.DIRECTION_NW), f.format(bi_x), "textBi_X",
        null, calcProps);
    Text textBi_Y = lang.newText(new Offset(paranthesisOffset1 + 22, 10,
        "calcText1", AnimalScript.DIRECTION_NW), f.format(bi_y), "textBi_Y",
        null, calcProps);
    Text textB_i_X = lang.newText(new Offset(paranthesisOffset3 + 22, -9,
        "calcText1", AnimalScript.DIRECTION_NW), f.format(b_i_x), "textB_i_X",
        null, calcProps);
    Text textB_i_Y = lang.newText(new Offset(paranthesisOffset3 + 22, 10,
        "calcText1", AnimalScript.DIRECTION_NW), f.format(b_i_y), "textB_i_Y",
        null, calcProps);
    Text textResult_X = lang.newText(new Offset(paranthesisOffset5 + 22, -9,
        "calcText1", AnimalScript.DIRECTION_NW), f.format(result_x),
        "textResult_X", null, calcProps);
    Text textResult_Y = lang.newText(new Offset(paranthesisOffset5 + 22, 10,
        "calcText1", AnimalScript.DIRECTION_NW), f.format(result_y),
        "textResult_Y", null, calcProps);

    calc2GroupList.add(calcText1);
    calc2GroupList.add(calcText2);
    calc2GroupList.add(calcText3);
    calc2GroupList.add(paranthesis1);
    calc2GroupList.add(paranthesis2);
    calc2GroupList.add(paranthesis3);
    calc2GroupList.add(paranthesis4);
    calc2GroupList.add(paranthesis5);
    calc2GroupList.add(paranthesis6);
    calc2GroupList.add(textBi_X);
    calc2GroupList.add(textBi_Y);
    calc2GroupList.add(textB_i_X);
    calc2GroupList.add(textB_i_Y);
    calc2GroupList.add(textResult_X);
    calc2GroupList.add(textResult_Y);
    calc2GroupList.add(calcSuperscript6);
    calc2GroupList.add(calcSubscript9);
    calc2Group = lang.newGroup(calc2GroupList, "calc2Group");
  }

  /**
   * Updates first formula group with new parameters.
   * 
   * @param t
   * @param xi
   * @param x_i
   * @param q
   * @param l
   * @param i
   * @param lambda0
   * @param lambda1
   * @param bi_x
   * @param bi_y
   * @param b_i_x
   * @param b_i_y
   * @param result_x
   * @param result_y
   * @throws IllegalDirectionException
   */
  private void updateCalcGroup1(double t, double xi, double x_i, int q, int l,
      int i, double lambda0, double lambda1, double bi_x, double bi_y,
      double b_i_x, double b_i_y, double result_x, double result_y)
      throws IllegalDirectionException {
    LinkedList<Primitive> calc1GroupList = calc1Group.getPrimitives();

    String numerator = f.format(t) + "-" + f.format(xi);
    String denominator = f.format(x_i) + "-" + f.format(xi);
    String lambda5Text = "λ (" + f.format(t) + ")=";

    Text calcLambda5 = (Text) calc1GroupList.get(4);
    calcLambda5.setText(lambda5Text, null, null);

    Text calcLambda6 = (Text) calc1GroupList.get(5);
    // Alle Offsets am westlichsten Text ausgerichtet, da setText die Bounding
    // Box nicht neu berechnet.
    fixYPosOffsetBug("calcLambda5");
    calcLambda6.moveTo(AnimalScript.DIRECTION_NW, null, new Offset(
        3 + lambda5Text.length() * 11, -12, "calcLambda5",
        AnimalScript.DIRECTION_NW), null, null);
    calcLambda6.setText(numerator, null, null);

    Text calcLambda7 = (Text) calc1GroupList.get(6);
    calcLambda7.moveTo(AnimalScript.DIRECTION_NW, null, new Offset(
        3 + lambda5Text.length() * 11, 11, "calcLambda5",
        AnimalScript.DIRECTION_NW), null, null);
    calcLambda7.setText(denominator, null, null);

    Text calcLambda8 = (Text) calc1GroupList.get(7);
    calcLambda8.moveTo(
        AnimalScript.DIRECTION_NW,
        null,
        new Offset(6 + lambda5Text.length() * 11
            + Math.max(numerator.length(), denominator.length()) * 11, 0,
            "calcLambda5", AnimalScript.DIRECTION_NW), null, null);
    calcLambda8.setText("=" + f.format(lambda1), null, null);

    Text calcLambda9 = (Text) calc1GroupList.get(8);
    calcLambda9.setText("λ (" + f.format(t) + ")=1-" + f.format(lambda1) + "="
        + f.format(lambda0), null, null);

    Text calcSuperscript1 = (Text) calc1GroupList.get(9);
    calcSuperscript1.setText(Integer.toString(l), null, null);

    Text calcSuperscript2 = (Text) calc1GroupList.get(10);
    calcSuperscript2.setText(Integer.toString(l), null, null);

    Text calcSuperscript3 = (Text) calc1GroupList.get(11);
    calcSuperscript3.setText(Integer.toString(l), null, null);

    Text calcSuperscript4 = (Text) calc1GroupList.get(12);
    calcSuperscript4.setText(Integer.toString(l), null, null);

    Text calcSuperscript5 = (Text) calc1GroupList.get(13);
    calcSuperscript5.setText(Integer.toString(l), null, null);

    Text calcSubscript1 = (Text) calc1GroupList.get(14);
    calcSubscript1.setText("1," + Integer.toString(i), null, null);

    Text calcSubscript2 = (Text) calc1GroupList.get(15);
    calcSubscript2.setText(Integer.toString(i), null, null);

    Text calcSubscript3 = (Text) calc1GroupList.get(16);
    calcSubscript3.setText(Integer.toString(i + q + 1 - l), null, null);

    Text calcSubscript4 = (Text) calc1GroupList.get(17);
    calcSubscript4.setText(Integer.toString(i), null, null);

    Text calcSubscript5 = (Text) calc1GroupList.get(18);
    calcSubscript5.setText("0," + Integer.toString(i), null, null);

    Text calcSubscript6 = (Text) calc1GroupList.get(19);
    calcSubscript6.setText("1," + Integer.toString(i), null, null);

    Text calcSubscript7 = (Text) calc1GroupList.get(20);
    calcSubscript7.setText("1," + Integer.toString(i), null, null);

    Text calcSubscript8 = (Text) calc1GroupList.get(21);
    calcSubscript8.setText("0," + Integer.toString(i), null, null);
  }

  /**
   * Updates second formula group with new parameters.
   * 
   * @param l
   * @param i
   * @param lambda0
   * @param lambda1
   * @param bi_x
   * @param bi_y
   * @param b_i_x
   * @param b_i_y
   * @param result_x
   * @param result_y
   * @throws IllegalDirectionException
   */
  private void updateCalcGroup2(int l, int i, double lambda0, double lambda1,
      double bi_x, double bi_y, double b_i_x, double b_i_y, double result_x,
      double result_y) throws IllegalDirectionException {
    LinkedList<Primitive> calc2GroupList = calc2Group.getPrimitives();

    int calcText2Offset = (3 + f.format(lambda0).length()) * 11 + 28
        + Math.max(f.format(bi_x).length(), f.format(bi_y).length()) * 11;
    int calcText3Offset = calcText2Offset + 28
        + (1 + f.format(lambda1).length()) * 11
        + Math.max(f.format(b_i_x).length(), f.format(b_i_y).length()) * 11;

    int paranthesisOffset1 = 25 + f.format(lambda0).length() * 11;
    int paranthesisOffset2 = paranthesisOffset1 + 19
        + Math.max(f.format(bi_x).length(), f.format(bi_y).length()) * 11;
    int paranthesisOffset3 = paranthesisOffset2 + 19
        + f.format(lambda1).length() * 11;
    int paranthesisOffset4 = paranthesisOffset3 + 19
        + Math.max(f.format(b_i_x).length(), f.format(b_i_y).length()) * 11;
    int paranthesisOffset5 = paranthesisOffset4 + 23;
    int paranthesisOffset6 = paranthesisOffset5 + 19
        + Math.max(f.format(result_x).length(), f.format(result_y).length())
        * 11;

    String text1 = "b =" + f.format(lambda0);
    String text2 = "+" + f.format(lambda1);

    Text calcText1 = (Text) calc2GroupList.get(0);
    calcText1.setText(text1, null, null);

    Text calcText2 = (Text) calc2GroupList.get(1);
    calcText2
        .moveTo(AnimalScript.DIRECTION_NW, null, new Offset(calcText2Offset, 0,
            "calcText1", AnimalScript.DIRECTION_NW), null, null);
    calcText2.setText(text2, null, null);

    Text calcText3 = (Text) calc2GroupList.get(2);
    calcText3
        .moveTo(AnimalScript.DIRECTION_NW, null, new Offset(calcText3Offset, 0,
            "calcText1", AnimalScript.DIRECTION_NW), null, null);

    Text paranthesis1 = (Text) calc2GroupList.get(3);
    paranthesis1.moveTo(AnimalScript.DIRECTION_NW, null, new Offset(
        paranthesisOffset1, -17, "calcText1", AnimalScript.DIRECTION_NW), null,
        null);

    Text paranthesis2 = (Text) calc2GroupList.get(4);
    paranthesis2.moveTo(AnimalScript.DIRECTION_NW, null, new Offset(
        paranthesisOffset2, -17, "calcText1", AnimalScript.DIRECTION_NW), null,
        null);

    Text paranthesis3 = (Text) calc2GroupList.get(5);
    paranthesis3.moveTo(AnimalScript.DIRECTION_NW, null, new Offset(
        paranthesisOffset3, -17, "calcText1", AnimalScript.DIRECTION_NW), null,
        null);

    Text paranthesis4 = (Text) calc2GroupList.get(6);
    paranthesis4.moveTo(AnimalScript.DIRECTION_NW, null, new Offset(
        paranthesisOffset4, -17, "calcText1", AnimalScript.DIRECTION_NW), null,
        null);

    Text paranthesis5 = (Text) calc2GroupList.get(7);
    paranthesis5.moveTo(AnimalScript.DIRECTION_NW, null, new Offset(
        paranthesisOffset5, -17, "calcText1", AnimalScript.DIRECTION_NW), null,
        null);

    Text paranthesis6 = (Text) calc2GroupList.get(8);
    paranthesis6.moveTo(AnimalScript.DIRECTION_NW, null, new Offset(
        paranthesisOffset6, -17, "calcText1", AnimalScript.DIRECTION_NW), null,
        null);

    Text textBi_X = (Text) calc2GroupList.get(9);
    textBi_X.moveTo(AnimalScript.DIRECTION_NW, null, new Offset(
        paranthesisOffset1 + 22, -9, "calcText1", AnimalScript.DIRECTION_NW),
        null, null);
    textBi_X.setText(f.format(bi_x), null, null);

    Text textBi_Y = (Text) calc2GroupList.get(10);
    textBi_Y.moveTo(AnimalScript.DIRECTION_NW, null, new Offset(
        paranthesisOffset1 + 22, 10, "calcText1", AnimalScript.DIRECTION_NW),
        null, null);
    textBi_Y.setText(f.format(bi_y), null, null);

    Text textB_i_X = (Text) calc2GroupList.get(11);
    textB_i_X.moveTo(AnimalScript.DIRECTION_NW, null, new Offset(
        paranthesisOffset3 + 22, -9, "calcText1", AnimalScript.DIRECTION_NW),
        null, null);
    textB_i_X.setText(f.format(b_i_x), null, null);

    Text textB_i_Y = (Text) calc2GroupList.get(12);
    textB_i_Y.moveTo(AnimalScript.DIRECTION_NW, null, new Offset(
        paranthesisOffset3 + 22, 10, "calcText1", AnimalScript.DIRECTION_NW),
        null, null);
    textB_i_Y.setText(f.format(b_i_y), null, null);

    Text textResult_X = (Text) calc2GroupList.get(13);
    textResult_X.moveTo(AnimalScript.DIRECTION_NW, null, new Offset(
        paranthesisOffset5 + 22, -9, "calcText1", AnimalScript.DIRECTION_NW),
        null, null);
    textResult_X.setText(f.format(result_x), null, null);

    Text textResult_Y = (Text) calc2GroupList.get(14);
    textResult_Y.moveTo(AnimalScript.DIRECTION_NW, null, new Offset(
        paranthesisOffset5 + 22, 10, "calcText1", AnimalScript.DIRECTION_NW),
        null, null);
    textResult_Y.setText(f.format(result_y), null, null);

    Text calcSuperscript6 = (Text) calc2GroupList.get(15);
    calcSuperscript6.setText(Integer.toString(l), null, null);

    Text calcSubscript9 = (Text) calc2GroupList.get(16);
    calcSubscript9.setText(Integer.toString(i), null, null);
  }

  /**
   * Creates vectors with given parameters in the desired location, groups it's
   * elements.
   * 
   * @param offsetX
   * @param offsetY
   * @param identifier
   * @param direction
   * @param q
   * @param vectorList
   */
  private void setUtility(int offsetX, int offsetY, String identifier,
      String direction, int q, ArrayList<Double> vectorList) {
    LinkedList<Primitive> utilityGroupList = new LinkedList<Primitive>();

    int index = 0;

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 12));
    textProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);

    TextProperties paranthesisProps = new TextProperties();
    paranthesisProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    paranthesisProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 40));

    int paranthesisXOffset = 100;
    int paranthesisYOffset = 40;

    for (int i = 0; i < q + 1; i++) {
      fixYPosOffsetBug(identifier);
      utilityGroupList.add(lang.newText(new Offset(offsetX, offsetY
          + paranthesisYOffset * i, identifier, AnimalScript.DIRECTION_NW),
          "( )", "paranthesis0" + i, null, paranthesisProps));
      fixYPosOffsetBug(identifier);
      utilityGroupList.add(lang.newText(new Offset(offsetX + 36, offsetY - 15
          + paranthesisYOffset * i, identifier, AnimalScript.DIRECTION_NW),
          f.format(vectorList.get(index++)), "vector0" + i + "0", null,
          textProps));
      utilityGroupList.add(lang.newText(new Offset(offsetX + 36, 3 + offsetY
          + paranthesisYOffset * i, identifier, AnimalScript.DIRECTION_NW),
          f.format(vectorList.get(index++)), "vector0" + i + "0", null,
          textProps));
    }
    for (int i = 0; i < q; i++) {
      fixYPosOffsetBug(identifier);
      utilityGroupList.add(lang.newText(new Offset(
          offsetX + paranthesisXOffset, offsetY + paranthesisYOffset / 2
              + paranthesisYOffset * i, identifier, AnimalScript.DIRECTION_NW),
          "( )", "paranthesis1" + i, null, paranthesisProps));
      fixYPosOffsetBug(identifier);
      utilityGroupList.add(lang.newText(new Offset(offsetX + paranthesisXOffset
          + 36, offsetY - 15 + paranthesisYOffset / 2 + paranthesisYOffset * i,
          identifier, AnimalScript.DIRECTION_NW), f.format(vectorList
          .get(index++)), "vector0" + i + "0", null, textProps));
      utilityGroupList.add(lang.newText(new Offset(offsetX + paranthesisXOffset
          + 36, 3 + offsetY + paranthesisYOffset / 2 + paranthesisYOffset * i,
          identifier, AnimalScript.DIRECTION_NW), f.format(vectorList
          .get(index++)), "vector0" + i + "0", null, textProps));
    }
    for (int i = 0; i < q - 1; i++) {
      fixYPosOffsetBug(identifier);
      utilityGroupList.add(lang.newText(new Offset(offsetX + 2
          * paranthesisXOffset, offsetY + paranthesisYOffset
          + paranthesisYOffset * i, identifier, AnimalScript.DIRECTION_NW),
          "( )", "paranthesis1" + i, null, paranthesisProps));
      fixYPosOffsetBug(identifier);
      utilityGroupList.add(lang.newText(new Offset(offsetX + 2
          * paranthesisXOffset + 36, offsetY - 15 + paranthesisYOffset
          + paranthesisYOffset * i, identifier, AnimalScript.DIRECTION_NW),
          f.format(vectorList.get(index++)), "vector0" + i + "0", null,
          textProps));
      utilityGroupList.add(lang.newText(new Offset(offsetX + 2
          * paranthesisXOffset + 36, 3 + offsetY + paranthesisYOffset
          + paranthesisYOffset * i, identifier, AnimalScript.DIRECTION_NW),
          f.format(vectorList.get(index++)), "vector0" + i + "0", null,
          textProps));
    }
    for (int i = 0; i < q - 2; i++) {
      fixYPosOffsetBug(identifier);
      utilityGroupList.add(lang.newText(new Offset(offsetX + 3
          * paranthesisXOffset, offsetY + 3 * paranthesisYOffset / 2
          + paranthesisYOffset * i, identifier, AnimalScript.DIRECTION_NW),
          "( )", "paranthesis1" + i, null, paranthesisProps));
      fixYPosOffsetBug(identifier);
      utilityGroupList.add(lang.newText(new Offset(offsetX + 3
          * paranthesisXOffset + 36, offsetY - 15 + 3 * paranthesisYOffset / 2
          + paranthesisYOffset * i, identifier, AnimalScript.DIRECTION_NW),
          f.format(vectorList.get(index++)), "vector0" + i + "0", null,
          textProps));
      utilityGroupList.add(lang.newText(new Offset(offsetX + 3
          * paranthesisXOffset + 36, 3 + offsetY + 3 * paranthesisYOffset / 2
          + paranthesisYOffset * i, identifier, AnimalScript.DIRECTION_NW),
          f.format(vectorList.get(index++)), "vector0" + i + "0", null,
          textProps));
    }
    utilityGroup = lang.newGroup(utilityGroupList, "utilityGroup");
  }

  /**
   * Creates a point with given coordinates, adds point to simplePointList.
   * 
   * @param x
   * @param y
   * @param name
   * @param color
   */
  private void drawPoint(double x, double y, String name, Color color) {
    PointProperties pointProps = new PointProperties();
    pointProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);

    simplePointList.add(lang.newPoint(new Offset((int) (x * gridSize),
        (int) (-y * gridSize), "grid", AnimalScript.DIRECTION_SW), name, null,
        pointProps));
    simplePointList.add(lang.newRect(new Offset(-1, -1, name,
        AnimalScript.DIRECTION_C), new Offset(1, 1, name,
        AnimalScript.DIRECTION_C), name + "Rect", null, rectProps));
  }

  /**
   * Creates a point with a label, adds point to pointGroupList.
   * 
   * @param x
   * @param y
   * @param name
   * @param label
   */
  private void drawPointGroup(double x, double y, String name, String label) {
    LinkedList<Primitive> pointList = new LinkedList<Primitive>();

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 9));
    textProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, false);

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);

    PointProperties pointProps = new PointProperties();
    pointList.add(lang.newPoint(new Offset((int) (x * gridSize),
        (int) (-y * gridSize), "grid", AnimalScript.DIRECTION_SW), name, null,
        pointProps));

    pointList.add(lang.newRect(new Offset(-2, -2, name,
        AnimalScript.DIRECTION_C), new Offset(2, 2, name,
        AnimalScript.DIRECTION_C), name + "Rect", null, rectProps));

    pointList.add(lang.newText(new Offset(-13, -14, name,
        AnimalScript.DIRECTION_C), label, name + "Text", null, textProps));

    Group pointGroup = lang.newGroup(pointList, name + "Group");
    pointGroupList.add(pointGroup);
  }

  /**
   * Creates a line with given points, adds point to lineList.
   * 
   * @param point1
   * @param point2
   * @param name
   * @param color
   */
  private void drawLine(String point1, String point2, String name, Color color) {
    PolylineProperties lineProps = new PolylineProperties();
    lineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);

    lineList.add(lang.newPolyline(new Node[] {
        new Offset(0, 0, point1, AnimalScript.DIRECTION_C),
        new Offset(0, 0, point2, AnimalScript.DIRECTION_C) }, name, null,
        lineProps));
  }

  /**
   * Creates StartValuesFrame with nodeVector table and given points as vectors,
   * groups it#s elements.
   * 
   * @param q
   * @param t
   * @param nodeMatrix
   * @param pointMatrix
   */
  private void setStartValuesFrame(int q, double t, String[][] nodeMatrix,
      String[][] pointMatrix) {
    LinkedList<Primitive> startValuesList = new LinkedList<Primitive>();

    RectProperties nodeVectorRectProps = new RectProperties();
    nodeVectorRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    nodeVectorRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    nodeVectorRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    PolygonProperties nodeVectorFrameProps = new PolygonProperties();
    nodeVectorFrameProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    nodeVectorFrameProps
        .set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    nodeVectorFrameProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    try {
      startValuesList.add(lang.newPolygon(new Node[] {
          new Offset(5, 5, "rectTitle", AnimalScript.DIRECTION_SW),
          new Offset(5, 5, "rectTitle", AnimalScript.DIRECTION_SE),
          new Offset(5, 5, "rectTitle", AnimalScript.DIRECTION_NE),
          new Offset(-5, 5, "frame", AnimalScript.DIRECTION_NE),
          new Offset(0, -5, "graphFrame", AnimalScript.DIRECTION_NE),
          new Offset(0, -5, "descFrame", AnimalScript.DIRECTION_NW) },
          "nodeVectorFrame", null, nodeVectorFrameProps));
    } catch (NotEnoughNodesException e) {
      e.printStackTrace();
    }

    TextProperties matrixTextProps = new TextProperties();
    matrixTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    matrixTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 12));

    TextProperties paranthesisProps = new TextProperties();
    paranthesisProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, basicColor);
    paranthesisProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 36));

    startValuesList.add(lang.newRect(new Offset(10, 10, "rectTitle",
        AnimalScript.DIRECTION_NE), new Offset(10 + nodeMatrix.length * 25, 60,
        "rectTitle", AnimalScript.DIRECTION_NE), "matrixFrame", null,
        nodeVectorRectProps));

    startValuesList.add(lang.newText(new Offset(15, -25, "nodeVectorFrame",
        AnimalScript.DIRECTION_SW), "q = " + q + ",   t = " + t, "labelQT",
        null, matrixTextProps));

    for (int i = 0; i < nodeMatrix.length; i++) {
      // An dieser Stelle wäre die Verwendung einer StringMatrix sinnvoll
      // gewesen, musste aber wegen Bugs aufgegeben werden. Näheres im
      // Feedbackdokument.
      startValuesList.add(lang.newRect(new Offset(i * 25, 0, "matrixFrame",
          AnimalScript.DIRECTION_NW), new Offset((i + 1) * 25, 25,
          "matrixFrame", AnimalScript.DIRECTION_NW), "matrix0" + i, null,
          nodeVectorRectProps));
      startValuesList.add(lang.newRect(new Offset(i * 25, 25, "matrixFrame",
          AnimalScript.DIRECTION_NW), new Offset((i + 1) * 25, 50,
          "matrixFrame", AnimalScript.DIRECTION_NW), "matrix1" + i, null,
          nodeVectorRectProps));
      if (i == 0)
        fixYPosOffsetBug("matrix00");
      startValuesList.add(lang.newText(new Offset(5, 4, "matrix0" + i,
          AnimalScript.DIRECTION_NW), nodeMatrix[i][0], "matrixText0" + i,
          null, matrixTextProps));
      startValuesList.add(lang.newText(new Offset(8, 5, "matrix1" + i,
          AnimalScript.DIRECTION_NW), nodeMatrix[i][1], "matrixText1" + i,
          null, matrixTextProps));
    }

    for (int i = 0; i < pointMatrix.length; i++) {
      fixYPosOffsetBug("matrixFrame");
      startValuesList.add(lang.newText(new Offset(10 + i * 52, 15,
          "matrixFrame", AnimalScript.DIRECTION_NE), pointMatrix[i][0] + "=",
          "pointMatrixB" + i, null, matrixTextProps));
      startValuesList.add(lang.newText(new Offset(-8, 5, "pointMatrixB" + i,
          AnimalScript.DIRECTION_NE), "()", "paranthesisMatrix" + i, null,
          paranthesisProps));
      startValuesList.add(lang.newText(new Offset(10, -31, "pointMatrixB" + i,
          AnimalScript.DIRECTION_NE), pointMatrix[i][1], "point" + i + "x",
          null, matrixTextProps));
      startValuesList.add(lang.newText(new Offset(10, 9, "pointMatrixB" + i,
          AnimalScript.DIRECTION_NE), pointMatrix[i][2], "point" + i + "y",
          null, matrixTextProps));
    }
    startValuesGroup = lang.newGroup(startValuesList, "startValuesGroup");
  }

  /**
   * Converts double array to String array, adds point names
   * 
   * @param startPoints
   * @param q
   * @return
   */
  private String[][] convertStartPoints(double[][] startPoints, int q) {
    int offset = 0;
    if (q == 2)
      offset = 1;
    String[][] pointMatrix = new String[startPoints.length][3];
    String[] pointNames = { "b₋₃", "b₋₂", "b₋₁", "b₀", "b₁", "b₂", "b₃", "b₄",
        "b₅", "b₆", "b₇" };
    // String[] pointNames = {"b-3", "b-2", "b-1", "b0", "b1", "b2", "b3", "b4",
    // "b5", "b6", "b7"};
    for (int i = 0; i < startPoints.length; i++) {
      pointMatrix[i][0] = pointNames[i + offset];
      pointMatrix[i][1] = f.format(startPoints[i][0]);
      pointMatrix[i][2] = f.format(startPoints[i][1]);
    }
    return pointMatrix;
  }

  /**
   * Converts double array to String array, adds node names
   * 
   * @param nodeVector
   * @param q
   * @return
   */
  private String[][] createNodeMatrix(double[] nodeVector, int q) {
    int offset = 0;
    if (q == 2)
      offset = 1;
    String[][] nodeMatrix = new String[nodeVector.length][2];
    String[] nodeNames = { "X₋₃", "X₋₂", "X₋₁", "X₀", "X₁", "X₂", "X₃", "X₄",
        "X₅", "X₆", "X₇" };
    // String[] nodeNames = {"X-3", "X-2", "X-1", "X0", "X1", "X2", "X3", "X4",
    // "X5", "X6", "X7"};
    for (int i = 0; i < nodeVector.length; i++) {
      nodeMatrix[i][0] = nodeNames[i + offset];
      nodeMatrix[i][1] = f.format(nodeVector[i]);
    }
    return nodeMatrix;
  }

  /**
   * Creates a nodeVector with a given length
   * 
   * @param length
   * @param q
   * @return
   */
  private double[] createNodeVector(int length, int q) {
    int last = 0;
    double[] nodeVector = new double[length];

    for (int i = 0; i < length; i++) {
      if (i < q)
        nodeVector[i] = 0;
      else if (i < length - q - 1)
        nodeVector[i] = last++;
      else
        nodeVector[i] = last;
    }
    return nodeVector;
  }

  /**
   * Calculates j
   * 
   * @param t
   * @param nodeVector
   * @return
   */
  private int calculateJ(double t, double[] nodeVector) {
    int j = -1;
    for (int i = 0; i < nodeVector.length; i++) {
      if (t >= nodeVector[i] && t < nodeVector[i + 1]) {
        j = i;
        break;
      }
    }
    return j;
  }

  /**
   * Calculates all lambdas and vectors with given parameters
   * 
   * @param q
   * @param t
   * @param startPoints
   * @param nodeVector
   * @return
   */
  private double[][][] deBoor(int q, double t, double[][] startPoints,
      double[] nodeVector) {
    double[][][] results = new double[q + 1][startPoints.length][4];
    // j bestimmen
    int j = calculateJ(t, nodeVector);
    // bi beladen
    for (int i = j - q; i < j + 1; i++) {
      results[0][i][2] = startPoints[i][0];
      results[0][i][3] = startPoints[i][1];
    }
    // de Boor steps
    for (int l = 1; l < q + 1; l++) {
      for (int i = j - q + l; i < j + 1; i++) {
        results[l][i][1] = (t - nodeVector[i])
            / (nodeVector[i + q + 1 - l] - nodeVector[i]);
        results[l][i][0] = 1 - results[l][i][1];
        results[l][i][2] = results[l][i][0] * results[l - 1][i - 1][2]
            + results[l][i][1] * results[l - 1][i][2];
        results[l][i][3] = results[l][i][0] * results[l - 1][i - 1][3]
            + results[l][i][1] * results[l - 1][i][3];
      }
    }
    return results;
  }

  /**
   * Converts relevant data from array in list representation
   * 
   * @param q
   * @param j
   * @param results
   * @return
   */
  private ArrayList<Double> convertResultsToList(int q, int j,
      double[][][] results) {
    ArrayList<Double> resultList = new ArrayList<Double>();
    for (int l = 0; l < q + 1; l++) {
      for (int i = j - q + l; i < j + 1; i++) {
        resultList.add(results[l][i][2]);
        resultList.add(results[l][i][3]);
      }
    }
    return resultList;
  }

  /**
   * Draws initial points.
   */
  private void drawInitialPoints() {
    String[][] pointLabels = convertStartPoints(startPoints, q);

    for (int i = 0; i < startPoints.length; i++) {
      drawPointGroup(startPoints[i][0], startPoints[i][1], "point" + i,
          pointLabels[i][0]);
    }
  }

  /**
   * Draws all lines between points in pointGroupList.
   * 
   * @param color
   */
  private void drawLines(Color color) {
    for (int i = 0; i < pointGroupList.size() - 1; i++) {
      drawLine(pointGroupList.get(i).getPrimitives().get(0).getName(),
          pointGroupList.get(i + 1).getPrimitives().get(0).getName(), "line"
              + i + (i + 1), color);
    }
  }

  /**
   * Creates description how to get j.
   */
  private void setJText() {
    LinkedList<Primitive> jGroupList = new LinkedList<Primitive>();

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, textHighlightColor);
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    textProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, false);

    jGroupList.add(lang.newText(new Offset(15, 15, "rectCompTitle",
        AnimalScript.DIRECTION_SW), "Wir nehmen t=" + t
        + ". Somit liegt t zwischen " + nodeMatrix[j][0] + " und "
        + nodeMatrix[j + 1][0] + ".", "JText1", null, textProps));
    jGroupList.add(lang.newText(new Offset(0, 10, "JText1",
        AnimalScript.DIRECTION_SW), "Daraus ergibt sich j=" + (j - q) + ".",
        "JText2", null, textProps));

    jGroup = lang.newGroup(jGroupList, "jGroup");
  }

  /**
   * Creates description to initial points.
   */
  private void setInitialPointsText() {
    LinkedList<Primitive> initialPointTextGroupList = new LinkedList<Primitive>();

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, textHighlightColor);
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    textProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, false);

    initialPointTextGroupList.add(lang.newText(new Offset(15, 15,
        "rectCompTitle", AnimalScript.DIRECTION_SW),
        "Wir betrachten eine Kurve vom Grad q=" + q + ".", "iPText1", null,
        textProps));
    initialPointTextGroupList.add(lang.newText(new Offset(0, 10, "iPText1",
        AnimalScript.DIRECTION_SW),
        "Somit setzen wir die relevanten Kontrollpunkte", "iPText2", null,
        textProps));
    initialPointTextGroupList.add(lang.newText(new Offset(0, 10, "iPText2",
        AnimalScript.DIRECTION_SW), pointMatrix[j - 3][0] + " bis "
        + pointMatrix[j][0] + " in das Dreiecksschema ein.", "iPText3", null,
        textProps));

    initialPointTextGroup = lang.newGroup(initialPointTextGroupList,
        "initialPointTextGroup");
  }

  /**
   * Creates description to simple points.
   */
  private void setSimplePointsText() {
    LinkedList<Primitive> simplePointTextGroupList = new LinkedList<Primitive>();

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, textHighlightColor);
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    textProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, false);

    simplePointTextGroupList.add(lang.newText(new Offset(15, 15,
        "rectCompTitle", AnimalScript.DIRECTION_SW),
        "In diesem abschließenden Schritt wird die B-Spline", "sPText1", null,
        textProps));
    simplePointTextGroupList.add(lang.newText(new Offset(0, 10, "iPText1",
        AnimalScript.DIRECTION_SW),
        "Kurve durch mehrfaches Ausführen des de Boor", "sPText2", null,
        textProps));
    simplePointTextGroupList.add(lang.newText(new Offset(0, 10, "iPText2",
        AnimalScript.DIRECTION_SW),
        "Algorithmus mit einer Schrittweite von 0,1 angedeutet.", "sPText3",
        null, textProps));

    simplePointTextGroup = lang.newGroup(simplePointTextGroupList,
        "initialPointTextGroup");
  }

  private double[][] convertIntArrayToDouble(int[][] intArray) {
    double[][] doubleArray = new double[intArray.length][intArray[0].length];
    for (int i = 0; i < intArray.length; i++) {
      for (int j = 0; j < intArray[i].length; j++) {
        doubleArray[i][j] = (double) intArray[i][j];
      }
    }
    return doubleArray;
  }

  /**
   * Main animation method.
   */
  private void animation() {
    setFrame();
    setIntroFrame(q, t);

    lang.nextStep();

    introGroup.hide();

    setDescriptionFrame();
    setCompFrame();
    setGraphFrame();
    setUtilityFrame();
    setStartValuesFrame(q, t, nodeMatrix, pointMatrix);
    setDescription(15, 35, "descFrame", AnimalScript.DIRECTION_NW);
    setFormulaGroup(15, 15, "desc4", AnimalScript.DIRECTION_SW);
    setLambdaGroup(15, 20, "desc5", AnimalScript.DIRECTION_SW);

    drawInitialPoints();
    drawLines(basicColor);

    desc1.changeColor(null, textHighlightColor, null, null);

    lang.nextStep();

    setJText();
    startValuesGroup
        .getPrimitives()
        .get(3 + j * 4)
        .changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
            pointHighlightColor, null, null);
    startValuesGroup
        .getPrimitives()
        .get(4 + j * 4)
        .changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
            pointHighlightColor, null, null);
    startValuesGroup
        .getPrimitives()
        .get(7 + j * 4)
        .changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
            pointHighlightColor, null, null);
    startValuesGroup
        .getPrimitives()
        .get(8 + j * 4)
        .changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
            pointHighlightColor, null, null);
    desc1.changeColor(null, basicColor, null, null);
    desc2.changeColor(null, textHighlightColor, null, null);

    lang.nextStep();

    double[][][] results = deBoor(q, t, startPoints, nodeVector);

    jGroup.hide();

    setInitialPointsText();
    setUtility(10, 45, "utilFrame", AnimalScript.DIRECTION_NW, q,
        convertResultsToList(q, j, results));
    for (int i = 3 * (q + 1); i < 3 * ((q + 1) * (q + 2)) / 2; i++) {
      utilityGroup.getPrimitives().get(i).hide();
    }

    for (int i = 0; i < 3 * (q + 1); i++) {
      utilityGroup.getPrimitives().get(i)
          .changeColor(null, textHighlightColor, null, null);
    }
    desc2.changeColor(null, basicColor, null, null);
    desc3.changeColor(null, textHighlightColor, null, null);
    startValuesGroup
        .getPrimitives()
        .get(3 + j * 4)
        .changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE, null,
            null);
    startValuesGroup
        .getPrimitives()
        .get(4 + j * 4)
        .changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE, null,
            null);
    startValuesGroup
        .getPrimitives()
        .get(7 + j * 4)
        .changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE, null,
            null);
    startValuesGroup
        .getPrimitives()
        .get(8 + j * 4)
        .changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE, null,
            null);
    for (int i = j - q; i < j + 1; i++) {
      pointGroupList.get(i).getPrimitives().get(1)
          .changeColor(null, pointHighlightColor, null, null);
      pointGroupList.get(i).getPrimitives().get(2)
          .changeColor(null, pointHighlightColor, null, null);
    }

    lang.nextStep();

    desc3.changeColor(null, basicColor, null, null);
    desc4.changeColor(null, textHighlightColor, null, null);
    desc5.changeColor(null, textHighlightColor, null, null);
    for (int i = j - q; i < j + 1; i++) {
      pointGroupList.get(i).getPrimitives().get(1)
          .changeColor(null, basicColor, null, null);
      pointGroupList.get(i).getPrimitives().get(2)
          .changeColor(null, basicColor, null, null);
    }
    for (int i = 0; i < 3 * (q + 1); i++) {
      utilityGroup.getPrimitives().get(i)
          .changeColor(null, basicColor, null, null);
    }
    initialPointTextGroup.hide();
    setCalcGroups(10, 50, "compFrame", AnimalScript.DIRECTION_NW, t, q, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1);
    calc1Group.changeColor(null, textHighlightColor, null, null);
    calc1Group.hide();
    calc2Group.changeColor(null, textHighlightColor, null, null);
    calc2Group.hide();

    int pointIndex = pointGroupList.size();
    int pointIndexOffset = pointGroupList.size();
    int index = 0;
    for (int l = 1; l < q + 1; l++) {
      for (int i = j - q + l; i < j + 1; i++) {

        lambdaGroup.changeColor(null, textHighlightColor, null, null);
        formulaGroup.changeColor(null, basicColor, null, null);

        try {
          updateCalcGroup1(t, nodeVector[i], nodeVector[i + q + 1 - l], q, l, i
              - q, results[l][i][0], results[l][i][1],
              results[l - 1][i - 1][2], results[l - 1][i - 1][3],
              results[l - 1][i][2], results[l - 1][i][3], results[l][i][2],
              results[l][i][3]);
        } catch (IllegalDirectionException e) {
          e.printStackTrace();
        }

        calc2Group.hide();
        calc1Group.show();

        pointGroupList.get(pointIndex - 1).changeColor(null, basicColor, null,
            null);
        utilityGroup.getPrimitives().get(3 * (q + 1) + index - 3)
            .changeColor(null, basicColor, null, null);
        utilityGroup.getPrimitives().get(3 * (q + 1) + index - 2)
            .changeColor(null, basicColor, null, null);
        utilityGroup.getPrimitives().get(3 * (q + 1) + index - 1)
            .changeColor(null, basicColor, null, null);

        lang.nextStep();

        calc1Group.hide();
        lambdaGroup.changeColor(null, basicColor, null, null);
        formulaGroup.changeColor(null, textHighlightColor, null, null);

        try {
          updateCalcGroup2(l, i - q, results[l][i][0], results[l][i][1],
              results[l - 1][i - 1][2], results[l - 1][i - 1][3],
              results[l - 1][i][2], results[l - 1][i][3], results[l][i][2],
              results[l][i][3]);
        } catch (IllegalDirectionException e) {
          e.printStackTrace();
        }

        // drawPoint
        drawPointGroup(results[l][i][2], results[l][i][3], "b" + l + i, "");
        pointGroupList.get(pointIndex).changeColor(null, pointHighlightColor,
            null, null);
        // drawLine
        if ((pointIndex - pointIndexOffset) > 0) {
          for (int n = pointIndexOffset; n < pointIndex; n++) {
            drawLine(pointGroupList.get(n).getPrimitives().get(0).getName(),
                pointGroupList.get(n + 1).getPrimitives().get(0).getName(),
                "line" + +l + i, lineHighlightColor);
          }
          pointIndexOffset++;
        }
        pointIndex++;

        utilityGroup.getPrimitives().get(3 * (q + 1) + index).show();
        utilityGroup.getPrimitives().get(3 * (q + 1) + index + 1).show();
        utilityGroup.getPrimitives().get(3 * (q + 1) + index + 2).show();
        utilityGroup.getPrimitives().get(3 * (q + 1) + index)
            .changeColor(null, textHighlightColor, null, null);
        utilityGroup.getPrimitives().get(3 * (q + 1) + index + 1)
            .changeColor(null, textHighlightColor, null, null);
        utilityGroup.getPrimitives().get(3 * (q + 1) + index + 2)
            .changeColor(null, textHighlightColor, null, null);
        index = index + 3;
        calc2Group.show();

        lang.nextStep();
      }
      pointIndexOffset++;
    }

    calc2Group.hide();

    setSimplePointsText();

    pointGroupList.get(pointIndex - 1)
        .changeColor(null, basicColor, null, null);
    desc4.changeColor(null, basicColor, null, null);
    desc5.changeColor(null, basicColor, null, null);
    formulaGroup.changeColor(null, basicColor, null, null);
    utilityGroup.getPrimitives().get(3 * (q + 1) + index - 3)
        .changeColor(null, basicColor, null, null);
    utilityGroup.getPrimitives().get(3 * (q + 1) + index - 2)
        .changeColor(null, basicColor, null, null);
    utilityGroup.getPrimitives().get(3 * (q + 1) + index - 1)
        .changeColor(null, basicColor, null, null);

    double tStart = 0.1;
    double tEnd = -1;
    for (double node : nodeVector) {
      tEnd = Math.max(tEnd, node);
    }
    tEnd = tEnd - 0.1;
    int steps = (int) ((tEnd - tStart) * 10);

    double[][][] res;
    for (int n = 0; n < steps; n++) {
      res = deBoor(q, tStart + 0.1 * n, startPoints, nodeVector);
      drawPoint(res[q][calculateJ(tStart + 0.1 * n, nodeVector)][2],
          res[q][calculateJ(tStart + 0.1 * n, nodeVector)][3], "simplePoint"
              + (tStart + 0.1 * n), pointHighlightColor);
    }

    lang.nextStep();

    simplePointTextGroup.hide();
    graphGroup.hide();
    compGroup.hide();
    descGroup.hide();
    utilityGroup.hide();
    startValuesGroup.hide();
    utilityFrameGroup.hide();
    lambdaGroup.hide();
    formulaGroup.hide();
    desc1.hide();
    desc2.hide();
    desc3.hide();
    desc4.hide();
    desc5.hide();
    for (Primitive primitive : simplePointList) {
      primitive.hide();
    }
    for (Primitive primitive : pointGroupList) {
      primitive.hide();
    }
    for (Primitive primitive : lineList) {
      primitive.hide();
    }

    setOutroFrame();

    lang.nextStep();

    outroGroup.hide();
  }

  /**
   * initializes main elements
   */
  public void init() {
    lang = new AnimalScript("de Boor Algorithmus", "David Sessler", 800, 600);
  }

  /**
   * Creates an instance of DeBoorAlgorithm with given parameters, starts
   * animation and crates AnimalScript String.
   */
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    pointHighlightColor = (Color) primitives.get("pointHighlightColor");
    t = (Double) primitives.get("t");
    q = (Integer) primitives.get("q");
    pointArray = (int[][]) primitives.get("pointArray");
    textHighlightColor = (Color) primitives.get("textHighlightColor");
    basicColor = (Color) primitives.get("basicColor");
    lineHighlightColor = (Color) primitives.get("lineHighlightColor");

    height = 600;
    width = 800;
    frameWidth = width - 10;
    frameHeight = height - 75;
    f = new DecimalFormat("#0.###");
    xPosOffsetBugFixNr = 0;
    pointGroupList = new ArrayList<Group>();
    lineList = new LinkedList<Polyline>();
    simplePointList = new ArrayList<Primitive>();
    gridSize = 32;
    startPoints = convertIntArrayToDouble(pointArray);
    nodeVector = createNodeVector(startPoints.length + q + 1, q);
    nodeMatrix = createNodeMatrix(nodeVector, q);
    pointMatrix = convertStartPoints(startPoints, q);
    j = calculateJ(t, nodeVector);

    init();
    // Activate step control
    lang.setStepMode(true);
    animation();

    // Zum testen
    System.out.println(lang.toString());

    return lang.toString();
  }

  public String getName() {
    return "de Boor Algorithmus";
  }

  public String getAlgorithmName() {
    return "de Boor Algorithmus";
  }

  public String getAnimationAuthor() {
    return "David Sessler";
  }

  public String getDescription() {
    return "Der de Boor Algorithmus dient dazu, einen beliebigen Punkt S(t) einer B-Splinekurve effizient zu berechnen. "
        + "WICHTIG: Die Struktur der Animation sowie der B-Splines hat Auswirkungen auf die anschlie&szlig;end zu w&auml;hlenden Parameter. "
        + "Bitte w&auml;hlen Sie den Grad q = 2 oder q = 3. "
        + "Die L&auml;nge des PunkteArrays sollte zwischen 3 und 6 sein und die x-y-Werte im Bereich [0,0] bis [9,6] liegen. "
        + "Die auszuwertende Stelle t ist wie folgt zu w&auml;hlen: 0 &#60; t &#60; (PunktArray Länge - q). ";
  }

  public String getCodeExample() {
    return "Zu berechnen: Punkt S(t) f&uuml;r ein t&#949;[x[j], x[j+1]) mit j&#949;{0,...,k} einer B-Splinekurve vom Grad q"
        + "\n"
        + "     1. Bestimme j"
        + "\n"
        + "     2. Setze b[0][i]  = b[i]    mit i = j - q,..., j"
        + "\n"
        + "     3. Berechne für l = 1,..., q sukzessive:"
        + "\n"
        + "          b[l][i] = &#955;[l][0,i](t) * b[l-1][i-1] + &#955;[l][1,i](t) * b[l-1][i]"
        + "\n"
        + "             mit i = j - q + l, ..., j und"
        + "\n"
        + "          &#955;[l][1,i](t) = (t - x[i]) / x[i+q+1-l] - x[i]"
        + "\n"
        + "          &#955;[l][0,i](t) = 1 - &#955;[l][1,i](t)";
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
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  public static void main(String[] args) {
    DeBoorAlgorithmus dba = new DeBoorAlgorithmus();
    Hashtable<String, Object> primitives = new Hashtable<String, Object>();
    primitives.put("t", 2.5);
    primitives.put("q", 3);
    primitives.put("pointHighlightColor", Color.ORANGE);
    primitives.put("textHighlightColor", Color.RED);
    primitives.put("lineHighlightColor", Color.GREEN);
    primitives.put("basicColor", Color.BLACK);
    primitives.put("pointArray", new int[][] { { 1, 1 }, { 4, 1 }, { 2, 4 },
        { 9, 5 }, { 5, 1 }, { 9, 1 } });

    dba.generate(null, primitives);
  }

}