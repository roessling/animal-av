package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Locale;

import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class DreiBlockHeuristik implements ValidatingGenerator {
  private Language             lang;
  private RectProperties       palettenProperties;
  // private RectProperties PackstueckProperties;
  private SourceCodeProperties sourceCodeProperties;
  private int                  BSkal;
  private int                  bSkal;
  private int                  LSkal;
  private int                  lSkal;

  private int                  B;
  private int                  b;
  private int                  L;
  private int                  l;

  int                          bestI      = -1;
  int                          bestAnzahl = 0;
  boolean                      optimal;

  int                          bestSpaltenBlock1;
  int                          bestSpaltenBlock2;
  int                          bestSpaltenBlock3;
  int                          bestHoeheBlock1;
  int                          bestHoeheBlock2;
  int                          bestHoeheBlock3;

  ArrayList<Double>            prozentAusnutzung;
  double                       skalierungsFaktor;

  String                       titleBest;

  public void init() {
    lang = new AnimalScript(
        "3-Block Heuristik zum Loesen von 2-dimensoinalen Packingproblemen",
        "Marlene Utz,Martin Hameister,Lea Rausch", 800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    // PackstueckProperties = (RectProperties) props
    // .getPropertiesByName("PackstueckRechteckProperties");
    palettenProperties = (RectProperties) props
        .getPropertiesByName("PalettenRechteckProperties");
    sourceCodeProperties = (SourceCodeProperties) props
        .getPropertiesByName("SourceCodeProperties");
    B = (Integer) primitives.get("B");
    L = (Integer) primitives.get("L");
    b = (Integer) primitives.get("b");
    l = (Integer) primitives.get("l");
    skalieren();
    // BSkal=B;
    // LSkal=L;
    // lSkal=l;
    // bSkal=b;
    lang.setStepMode(true);

    bestAnzahl = 0;
    bestHoeheBlock1 = 0;
    bestHoeheBlock2 = 0;
    bestHoeheBlock3 = 0;
    bestSpaltenBlock1 = 0;
    bestSpaltenBlock2 = 0;
    bestSpaltenBlock3 = 0;

    skalierungsFaktor = 1;
    optimal = false;

    DreiBlockAnimieren(this.lang);

    return lang.toString();
  }

  void skalieren() {
    if (L < 200) {
      skalierungsFaktor = 200 / L;
      if (B * skalierungsFaktor > 360) {
        skalierungsFaktor = 360 / B;
      }
    } else if (L > 320) {
      skalierungsFaktor = 200 / L;
      if (B * skalierungsFaktor > 360) {
        skalierungsFaktor = 360 / B;
      }
    } else {
      skalierungsFaktor = 1;
    }

    LSkal = (int) (L * skalierungsFaktor);
    BSkal = (int) (B * skalierungsFaktor);
    bSkal = (int) (b * skalierungsFaktor);
    lSkal = (int) (l * skalierungsFaktor);

  }

  int abRunden(double zahl) {
    int temp = (int) zahl;
    return temp;
  }

  void checkOpt() {
    int rest = (LSkal * BSkal) - (bestAnzahl * (lSkal * bSkal));
    if (rest < (lSkal * bSkal)) {
      optimal = true;
    } else {
      optimal = false;
    }

  }

  int ggt(int B, int b, int L, int l) {
    int Bb = ggt(B, b);
    int Ll = ggt(L, l);

    return ggt(Ll, Bb);
  }

  int ggt(int theB, int theA) {
    int a = theA, b = theB;
    int h;
    while (b != 0) {
      h = a % b;
      a = b;
      b = h;
    }
    return a;

  }

  void DreiBlock(SourceCode sc, Language lang, Rect Rec, Text bText, Text header) {

    lang.nextStep();

    // Hilfsvariablen zum Speichern der Werte aus der aktuellen Iteration
    int tempAnzahl;
    int tempSpaltenBlock1;
    int tempSpaltenBlock2;
    int tempSpaltenBlock3;
    int tempHoeheBlock1;
    int tempHoeheBlock2;
    int tempHoeheBlock3;

    // Text zur Ausgabe der Werte des aktuellen Blocks
    TextProperties textProp = new TextProperties();
    Text IterationText = lang.newText(new Offset(20, 30, header,
        AnimalScript.DIRECTION_SW), "", "IterationText", null, textProp);
    Text AnzahlText = lang.newText(new Offset(20, 60, header,
        AnimalScript.DIRECTION_SW), "", "AnzahlText", null, textProp);
    Text AnzahlBestText = lang.newText(new Offset(50, 0, bText,
        AnimalScript.DIRECTION_NE), "Aktuell hoechste Anzahl = 0",
        "AnzahlBestText", null, textProp);
    Text Block1Text = lang.newText(new Offset(10, 0, AnzahlText,
        AnimalScript.DIRECTION_NE), "", "Block1Text", null, textProp);
    Text Block2Text = lang.newText(new Offset(10, 0, AnzahlText,
        AnimalScript.DIRECTION_NE), "", "Block2Text", null, textProp);
    Text Block3Text = lang.newText(new Offset(10, 0, AnzahlText,
        AnimalScript.DIRECTION_NE), "", "Block3Text", null, textProp);

    sc.highlight(0);
    RectProperties RecProp = new RectProperties();

    // ArrayLists zum Speichern der in der Iteration erzeugten Rechtecke
    ArrayList<ArrayList<Rect>> listeBlock1 = new ArrayList<ArrayList<Rect>>();
    ArrayList<ArrayList<Rect>> listeBlock2 = new ArrayList<ArrayList<Rect>>();
    ArrayList<ArrayList<Rect>> listeBlock3 = new ArrayList<ArrayList<Rect>>();

    for (int i = 0; i <= abRunden(LSkal / lSkal); i++) {
      lang.nextStep(i + "te-Iteration");
      sc.unhighlight(0);
      sc.unhighlight(16);
      sc.highlight(1);
      sc.unhighlight(17);

      lang.nextStep();
      sc.unhighlight(1);
      sc.highlight(2);
      tempAnzahl = 0;
      AnzahlText.hide();
      IterationText.hide();
      IterationText = lang.newText(new Offset(20, 30, header,
          AnimalScript.DIRECTION_SW), "Iteration = " + i, "IterationText" + i,
          null, textProp);
      for (int j = 0; j < listeBlock1.size(); j++) {
        for (int k = 0; k < listeBlock1.get(j).size(); k++)
          listeBlock1.get(j).get(k).hide();
      }
      for (int j = 0; j < listeBlock2.size(); j++) {
        for (int k = 0; k < listeBlock2.get(j).size(); k++)
          listeBlock2.get(j).get(k).hide();
      }
      for (int j = 0; j < listeBlock3.size(); j++) {
        for (int k = 0; k < listeBlock3.get(j).size(); k++)
          listeBlock3.get(j).get(k).hide();
      }
      listeBlock1.clear();
      listeBlock2.clear();
      listeBlock3.clear();

      AnzahlText.hide();
      AnzahlText = lang.newText(new Offset(20, 60, header,
          AnimalScript.DIRECTION_SW), "Aktuelle Anzahl = 0", "AnzahlText" + i,
          null, textProp);
      Block1Text = lang.newText(new Offset(20, 0, AnzahlText,
          AnimalScript.DIRECTION_NE), "Block 1: ", "Block1TextBlock" + i, null,
          textProp);
      tempSpaltenBlock1 = 0;
      tempSpaltenBlock2 = 0;
      tempSpaltenBlock3 = 0;
      tempHoeheBlock1 = 0;
      tempHoeheBlock2 = 0;
      tempHoeheBlock3 = 0;

      lang.nextStep();
      sc.unhighlight(2);
      sc.highlight(4);
      // Block 1
      tempSpaltenBlock1 = i;
      Block1Text.hide();
      if (tempSpaltenBlock1 == 1) {
        Block1Text = lang.newText(new Offset(20, 0, AnzahlText,
            AnimalScript.DIRECTION_NE), "Block 1: " + tempSpaltenBlock1
            + " Spalte", "Block1TextSp" + i, null, textProp);
      } else {
        Block1Text = lang.newText(new Offset(20, 0, AnzahlText,
            AnimalScript.DIRECTION_NE), "Block 1: " + tempSpaltenBlock1
            + " Spalten", "Block1TextSp" + i, null, textProp);
      }

      lang.nextStep();
      sc.unhighlight(4);
      sc.highlight(5);
      tempHoeheBlock1 = abRunden(BSkal / bSkal);
      Block1Text.hide();
      if (tempSpaltenBlock1 == 1) {
        Block1Text = lang.newText(new Offset(20, 0, AnzahlText,
            AnimalScript.DIRECTION_NE), "Block 1: " + tempSpaltenBlock1
            + " Spalte der Hoehe " + tempHoeheBlock1, "Spalten1Text" + i, null,
            textProp);
      } else {
        Block1Text = lang.newText(new Offset(20, 0, AnzahlText,
            AnimalScript.DIRECTION_NE), "Block 1: " + tempSpaltenBlock1
            + " Spalten der Hoehe " + tempHoeheBlock1, "Spalten1Text" + i,
            null, textProp);
      }
      lang.nextStep();
      sc.unhighlight(5);
      sc.highlight(6);
      AnzahlText.hide();
      tempAnzahl = tempAnzahl + (tempSpaltenBlock1 * tempHoeheBlock1);
      AnzahlText = lang.newText(new Offset(20, 60, header,
          AnimalScript.DIRECTION_SW), "Aktuelle Anzahl = " + tempAnzahl,
          "AnzahlText" + i, null, textProp);

      for (int j = 1; j <= tempSpaltenBlock1; j++) {
        ArrayList<Rect> tempList = new ArrayList<Rect>();
        for (int k = 1; k <= tempHoeheBlock1; k++) {
          tempList.add(lang.newRect(new Offset(0, k * (-bSkal), Rec,
              AnimalScript.DIRECTION_SW), new Offset(j * lSkal, 0, Rec,
              AnimalScript.DIRECTION_SW), "Rec1" + j + "," + k, null, RecProp));
        }
        listeBlock1.add(tempList);

      }

      lang.nextStep();

      // Block 2
      Block1Text.hide();
      Block2Text = lang.newText(new Offset(20, 0, AnzahlText,
          AnimalScript.DIRECTION_NE), "Block 2: ", "Block2TextBlock" + i, null,
          textProp);
      sc.unhighlight(6);

      lang.nextStep();
      sc.highlight(8);
      tempSpaltenBlock2 = abRunden((LSkal - i * lSkal) / bSkal);
      Block2Text.hide();
      if (tempSpaltenBlock2 == 1) {
        Block2Text = lang.newText(new Offset(20, 0, AnzahlText,
            AnimalScript.DIRECTION_NE), "Block 2: " + tempSpaltenBlock2
            + " Spalte", "Block2TextSp" + i, null, textProp);
      } else {
        Block2Text = lang.newText(new Offset(20, 0, AnzahlText,
            AnimalScript.DIRECTION_NE), "Block 2: " + tempSpaltenBlock2
            + " Spalten", "Block2TextSp" + i, null, textProp);
      }
      lang.nextStep();
      sc.unhighlight(8);
      sc.highlight(9);
      tempHoeheBlock2 = abRunden(BSkal / lSkal);
      Block2Text.hide();
      if (tempSpaltenBlock2 == 1) {
        Block2Text = lang.newText(new Offset(20, 0, AnzahlText,
            AnimalScript.DIRECTION_NE), "Block 2: " + tempSpaltenBlock2
            + " Spalte der Hoehe " + tempHoeheBlock2, "Block2Text" + i, null,
            textProp);
      } else {
        Block2Text = lang.newText(new Offset(20, 0, AnzahlText,
            AnimalScript.DIRECTION_NE), "Block 2: " + tempSpaltenBlock2
            + " Spalten der Hoehe " + tempHoeheBlock2, "Block2Text" + i, null,
            textProp);
      }
      lang.nextStep();
      sc.unhighlight(9);
      sc.highlight(10);
      AnzahlText.hide();
      tempAnzahl = tempAnzahl + tempSpaltenBlock2 * tempHoeheBlock2;
      AnzahlText = lang.newText(new Offset(20, 60, header,
          AnimalScript.DIRECTION_SW), "Aktuelle Anzahl = " + tempAnzahl,
          "AnzahlText" + i, null, textProp);

      for (int j = 1; j <= tempSpaltenBlock2; j++) {
        ArrayList<Rect> tempList = new ArrayList<Rect>();
        for (int k = 1; k <= tempHoeheBlock2; k++) {
          tempList.add(lang.newRect(new Offset(i * lSkal, k * (-lSkal), Rec,
              AnimalScript.DIRECTION_SW), new Offset(j * bSkal + i * lSkal, 0,
              Rec, AnimalScript.DIRECTION_SW), "Rec1" + j + "," + k, null,
              RecProp));
        }
        listeBlock2.add(tempList);

      }

      lang.nextStep();
      // Block 3
      Block2Text.hide();
      sc.unhighlight(10);
      Block3Text = lang.newText(new Offset(20, 0, AnzahlText,
          AnimalScript.DIRECTION_NE), "Block 3: ", "Block3TextSp" + i, null,
          textProp);

      lang.nextStep();
      sc.highlight(12);
      tempSpaltenBlock3 = abRunden((LSkal - i * lSkal) / lSkal);
      Block3Text.hide();
      if (tempSpaltenBlock3 == 1) {
        Block3Text = lang.newText(new Offset(20, 0, AnzahlText,
            AnimalScript.DIRECTION_NE), "Block 3: " + tempSpaltenBlock3
            + " Spalte", "Block3TextSp" + i, null, textProp);
      } else {
        Block3Text = lang.newText(new Offset(20, 0, AnzahlText,
            AnimalScript.DIRECTION_NE), "Block 3: " + tempSpaltenBlock3
            + " Spalten", "Block3TextSp" + i, null, textProp);
      }
      lang.nextStep();
      sc.unhighlight(12);
      sc.highlight(13);
      tempHoeheBlock3 = abRunden((BSkal - (abRunden(BSkal / lSkal) * lSkal))
          / bSkal);
      Block3Text.hide();
      if (tempSpaltenBlock3 == 1) {
        Block3Text = lang.newText(new Offset(20, 0, AnzahlText,
            AnimalScript.DIRECTION_NE), "Block 3: " + tempSpaltenBlock3
            + " Spalte der Hoehe " + tempHoeheBlock3, "Block3Text" + i, null,
            textProp);
      } else {
        Block3Text = lang.newText(new Offset(20, 0, AnzahlText,
            AnimalScript.DIRECTION_NE), "Block 3: " + tempSpaltenBlock3
            + " Spalten der Hoehe " + tempHoeheBlock3, "Block3Text" + i, null,
            textProp);
      }
      lang.nextStep();
      sc.unhighlight(13);
      sc.highlight(14);
      AnzahlText.hide();
      tempAnzahl = tempAnzahl + tempSpaltenBlock3 * tempHoeheBlock3;
      AnzahlText = lang.newText(new Offset(20, 60, header,
          AnimalScript.DIRECTION_SW), "Aktuelle Anzahl = " + tempAnzahl,
          "AnzahlText" + i, null, textProp);

      for (int j = 1; j <= tempSpaltenBlock3; j++) {
        ArrayList<Rect> tempList = new ArrayList<Rect>();
        for (int k = 1; k <= tempHoeheBlock3; k++) {
          tempList.add(lang.newRect(new Offset(i * lSkal, -tempHoeheBlock2
              * lSkal + k * (-bSkal), Rec, AnimalScript.DIRECTION_SW),
              new Offset(i * lSkal + j * lSkal, -tempHoeheBlock2 * lSkal, Rec,
                  AnimalScript.DIRECTION_SW), "Rec1" + j + "," + k, null,
              RecProp));
        }
        listeBlock3.add(tempList);

      }

      lang.nextStep();
      Block3Text.hide();
      sc.unhighlight(14);
      sc.highlight(16);

      // double proz=(L*B)/(l*b*tempAnzahl);
      // prozentAusnutzung.add(proz);

      if (tempAnzahl > bestAnzahl) {
        AnzahlBestText.hide();
        AnzahlBestText = lang.newText(new Offset(50, 0, bText,
            AnimalScript.DIRECTION_NE), "Aktuell hoechste Anzahl = "
            + tempAnzahl, "AnzahlBestText" + i, null, textProp);

        lang.nextStep();
        sc.unhighlight(16);
        sc.highlight(17);
        bestI = i;
        bestAnzahl = tempAnzahl;
        bestSpaltenBlock1 = tempSpaltenBlock1;
        bestSpaltenBlock2 = tempSpaltenBlock2;
        bestSpaltenBlock3 = tempSpaltenBlock3;
        bestHoeheBlock1 = tempHoeheBlock1;
        bestHoeheBlock2 = tempHoeheBlock2;
        bestHoeheBlock3 = tempHoeheBlock3;
      }

    }

    lang.nextStep();
    sc.unhighlight(16);
    sc.highlight(19);

    lang.nextStep();
    sc.unhighlight(19);
    sc.highlight(20);
    checkOpt();

    lang.nextStep();
    for (int j = 0; j < listeBlock1.size(); j++) {
      for (int k = 0; k < listeBlock1.get(j).size(); k++)
        listeBlock1.get(j).get(k).hide();
    }
    for (int j = 0; j < listeBlock2.size(); j++) {
      for (int k = 0; k < listeBlock2.get(j).size(); k++)
        listeBlock2.get(j).get(k).hide();
    }
    for (int j = 0; j < listeBlock3.size(); j++) {
      for (int k = 0; k < listeBlock3.get(j).size(); k++)
        listeBlock3.get(j).get(k).hide();
    }

    AnzahlBestText.hide();
    AnzahlText.hide();
    IterationText.hide();

  }

  void DreiBlockAnimieren(Language lang) {

    TextProperties headerVisProp = new TextProperties();
    Font headerFont = new Font("SansSerif", 1, 24);
    headerVisProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    headerVisProp.set(AnimationPropertiesKeys.FONT_PROPERTY, headerFont);

    Text header = lang.newText(new Coordinates(20, 30),
        "Drei-Block Heuristik zum Loesen von 2-Dimensionale Packingproblemen",
        "header", null, headerVisProp);

    RectProperties headerRecProp = new RectProperties();
    headerRecProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
    headerRecProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    headerRecProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    lang.newRect(new Offset(-5, -5, header, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, header, AnimalScript.DIRECTION_SE), "hRect", null,
        headerRecProp);

    TextProperties textPropBeschreibung = new TextProperties();
    Font beschrebungFont = new Font("SansSerif", 1, 18);
    textPropBeschreibung.set(AnimationPropertiesKeys.FONT_PROPERTY,
        beschrebungFont);

    lang.nextStep("Einleitung");

    // Algo Beschreibung

    String beschreibungString = getDescription();

    String[] beschreibungSplit = beschreibungString.split("\n");
    Text[] beschreibung = new Text[beschreibungSplit.length];

    for (int i = 0; i < beschreibungSplit.length; i++) {
      beschreibung[i] = lang.newText(new Offset(20, (80 + i * 25), header,
          AnimalScript.DIRECTION_SW), beschreibungSplit[i],
          ("Beschreibung" + i), null, textPropBeschreibung);
    }

    lang.nextStep();
    for (int i = 0; i < beschreibungSplit.length; i++) {
      beschreibung[i].hide();
    }

    // Ausgangsdaten und Rechteck
    TextProperties textProp = new TextProperties();
    Text LText = lang.newText(new Offset(20, 90, header,
        AnimalScript.DIRECTION_SW), "L = " + L, "LText", null, textProp);
    Text BText = lang.newText(new Offset(10, 0, LText,
        AnimalScript.DIRECTION_NE), "B = " + B, "BText", null, textProp);
    Text lText = lang.newText(new Offset(10, 0, BText,
        AnimalScript.DIRECTION_NE), "l = " + l, "lText", null, textProp);
    Text bText = lang.newText(new Offset(10, 0, lText,
        AnimalScript.DIRECTION_NE), "b = " + b, "bText", null, textProp);

    RectProperties RecProp = palettenProperties;
    RecProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    Rect Rec = lang.newRect(new Offset(20, 120, header,
        AnimalScript.DIRECTION_SW), new Offset(20 + LSkal, 120 + BSkal, header,
        AnimalScript.DIRECTION_SW), "Rec", null, RecProp);
    int ggT = ggt(BSkal, bSkal, LSkal, lSkal);
    ArrayList<Rect> RecListL = new ArrayList<Rect>();
    for (int i = 0; i < LSkal / ggT; i++) {
      RecListL.add(lang.newRect(new Offset(i * ggT, 0, Rec,
          AnimalScript.DIRECTION_NW), new Offset((i + 1) * ggT, BSkal, Rec,
          AnimalScript.DIRECTION_NW), "RecL" + i, null, RecProp));
    }

    // ArrayList<Rect> RecListB = new ArrayList<Rect>();
    for (int i = 0; i < BSkal / ggT; i++) {
      RecListL.add(lang.newRect(new Offset(0, i * ggT, Rec,
          AnimalScript.DIRECTION_NW), new Offset(LSkal, (i + 1) * ggT, Rec,
          AnimalScript.DIRECTION_NW), "RecL" + i, null, RecProp));
    }

    lang.nextStep();
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    SourceCode sc = lang.newSourceCode(new Offset(250, -80, bText,
        AnimalScript.DIRECTION_NE), "sourceCode", null, sourceCodeProperties);

    sc.addCodeLine("public void DreiBlockHeuristik() {", null, 0, null);// 0
    sc.addCodeLine("for (int i = 0; i <= abRunden(L / l); i++) {", null, 1,
        null);// 1
    sc.addCodeLine("tempAnzahl = 0;", null, 2, null);// 2
    sc.addCodeLine("", null, 2, null);// 3

    sc.addCodeLine("tempSpaltenBlock1 = i;", null, 2, null);// 4
    sc.addCodeLine("tempHoeheBlock1 = abRunden(B / b);", null, 2, null);// 5
    sc.addCodeLine(
        "tempAnzahl = tempAnzahl + (tempSpaltenBlock1 * tempHoeheBlock1);",
        null, 2, null);// 6
    sc.addCodeLine("", null, 2, null);// 7

    sc.addCodeLine("tempSpaltenBlock2 = abRunden((L - i * l) / b);", null, 2,
        null);// 8
    sc.addCodeLine("tempHoeheBlock2 = abRunden(B / l);", null, 2, null);// 9
    sc.addCodeLine(
        "tempAnzahl = tempAnzahl + (tempSpaltenBlock2 * tempHoeheBlock2);",
        null, 2, null);// 10
    sc.addCodeLine("", null, 2, null);// 11

    sc.addCodeLine("tempSpaltenBlock3 =  abRunden((L - i * l) / l);", null, 2,
        null);// 12
    sc.addCodeLine(
        "tempHoeheBlock3 =abRunden((B - (abRunden(B / l) * l)) / b);", null, 2,
        null);// 13
    sc.addCodeLine(
        "tempAnzahl = tempAnzahl + (tempSpaltenBlock3 * tempHoeheBlock3);",
        null, 2, null);// 14
    sc.addCodeLine("", null, 2, null);// 15

    sc.addCodeLine("if (tempAnzahl > bestAnzahl) {", null, 2, null);// 16
    sc.addCodeLine("safeBestSol();", null, 3, null);// 17
    sc.addCodeLine("}", null, 2, null);// 18

    sc.addCodeLine("}", null, 1, null);// 19
    sc.addCodeLine("checkOpt();", null, 1, null);// 20
    sc.addCodeLine("}", null, 0, null);// 21

    DreiBlock(sc, lang, Rec, bText, header);

    lang.nextStep("Fazit");
    sc.hide();
    lText.hide();
    bText.hide();
    LText.hide();
    BText.hide();

    lang.newText(new Offset(20, 80, header, AnimalScript.DIRECTION_SW),
        "Das beste gefundene Packing ist hier abgebildet.", "AbschlussText",
        null, textProp);

    ArrayList<ArrayList<Rect>> listeBlock1 = new ArrayList<ArrayList<Rect>>();
    ArrayList<ArrayList<Rect>> listeBlock2 = new ArrayList<ArrayList<Rect>>();
    ArrayList<ArrayList<Rect>> listeBlock3 = new ArrayList<ArrayList<Rect>>();
    RectProperties RecProbBlock = new RectProperties();
    for (int j = 1; j <= bestSpaltenBlock1; j++) {
      ArrayList<Rect> tempList = new ArrayList<Rect>();
      for (int k = 1; k <= bestHoeheBlock1; k++) {
        tempList.add(lang.newRect(new Offset(0, k * (-bSkal), Rec,
            AnimalScript.DIRECTION_SW), new Offset(j * lSkal, 0, Rec,
            AnimalScript.DIRECTION_SW), "Rec1" + j + "," + k, null,
            RecProbBlock));
      }
      listeBlock1.add(tempList);

    }

    for (int j = 1; j <= bestSpaltenBlock2; j++) {
      ArrayList<Rect> tempList = new ArrayList<Rect>();
      for (int k = 1; k <= bestHoeheBlock2; k++) {
        tempList.add(lang.newRect(new Offset(bestI * lSkal, k * (-lSkal), Rec,
            AnimalScript.DIRECTION_SW), new Offset(j * bSkal + bestI * lSkal,
            0, Rec, AnimalScript.DIRECTION_SW), "Rec2" + j + "," + k, null,
            RecProbBlock));
      }
      listeBlock2.add(tempList);

    }

    for (int j = 1; j <= bestSpaltenBlock3; j++) {
      ArrayList<Rect> tempList = new ArrayList<Rect>();
      for (int k = 1; k <= bestHoeheBlock3; k++) {
        tempList.add(lang.newRect(new Offset(bestI * lSkal, -bestHoeheBlock2
            * lSkal + k * (-bSkal), Rec, AnimalScript.DIRECTION_SW),
            new Offset(bestI * lSkal + j * lSkal, -bestHoeheBlock2 * lSkal,
                Rec, AnimalScript.DIRECTION_SW), "Rec3" + j + "," + k, null,
            RecProbBlock));
      }
      listeBlock3.add(tempList);

    }

    if (optimal) {
      lang.newText(
          new Offset(0, 80, Rec, AnimalScript.DIRECTION_SW),
          "Das Gefundene Packing ist Optimal. Der noch zur Verfuegung stehende Platz ist kleiner, als der von einem Packstueck benoetigte Platz.",
          "AbschlussTextOpt", null, textProp);

    }

    System.out.println(lang);

  }

  public String getName() {
    return "3-Block Heuristik zum Loesen von 2-dimensoinalen Packingproblemen";
  }

  public String getAlgorithmName() {
    return "3-Block Heuristik";
  }

  public String getAnimationAuthor() {
    return "Marlene Utz, Martin Hameister, Lea Rausch";
  }

  public String getDescription() {
    return "\nDie 3-Block Heuristik ist eine Heuristik fuer 2 Dimensionale Packingprobleme. "
        + "\n"
        + "Dabei ist eine Palette mit Massen L und B gegeben, auf der gleichgrosse Packstuecke mit den Massen l und b platziert "
        + "\n"
        + "werden sollen.   "
        + "\n"
        + "Es werden abgerundet(L/l) viele Packings erzeugt und die Variante mit der hoechsten Anzahl ausgegeben. "
        + "\n"
        + "Dabei geht die Heuristik wie folgt vor: In der i-ten Iteration werden i Spalten mit maximal moeglicher Hoehe im Laengsformat gepackt."
        + "\n"
        + "Daneben werden soviel Spalten wie moeglich mit maximal moeglicher Hoehe im Querformat gepackt. "
        + "\n"
        + "Auf die Querspalten werden so viele Spalten wie moeglich mit maximal moeglicher Hoehe im Laegsformat gepackt.\n"
        + "HINWEIS: Um eine verständliche Animation zu erhalten ist es ratsam ein Längen-Breitenverhältnis zwischen 5:9 und 8:9 zu wählen. Auf die Palette wird ein Raster gelegt, dessen Maße sich aus dem Größten gemeinsamen Teiler aller skalierten Werte ergibt. ";
  }

  public String getCodeExample() {
    return "public void DreiBlockHeuristik() {" + "\n" + "\n"
        + "  for (int i = 0; i <= abRunden(L / l); i++) { " + "\n"
        + "	tempAnzahl = 0;" + "\n" + " " + "\n" + "	tempSpaltenBlock1 = i;"
        + "\n" + "	tempHoeheBlock1 = abRunden(B / b);" + "\n"
        + "	tempAnzahl = tempAnzahl + (tempSpaltenBlock1 * tempHoeheBlock1);"
        + "\n" + "  " + "\n"
        + "	tempSpaltenBlock2 = abRunden((L - i * l) / b);" + "\n"
        + "	tempHoeheBlock2 = abRunden(B / l);" + "\n"
        + "	tempAnzahl = tempAnzahl + (tempSpaltenBlock2 * tempHoeheBlock2);"
        + "\n" + "  " + "\n"
        + "	tempSpaltenBlock3 =  abRunden((L - i * l) / l);" + "\n"
        + "	tempHoeheBlock3 =abRunden((B - (abRunden(B / l) * l)) / b);" + "\n"
        + "	tempAnzahl = tempAnzahl + (tempSpaltenBlock3 * tempHoeheBlock3);"
        + "\n" + "  " + "\n" + "	if (tempAnzahl > bestAnzahl) {" + "\n"
        + "		safeBestSol();" + "\n" + "	}" + "\n" + "  }" + "\n"
        + "  checkOpt();" + "\n" + "}";
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

  @Override
  public boolean validateInput(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) throws IllegalArgumentException {

    if ((L < l && L < b) || (B < b && B < l)) {
      throw new IllegalArgumentException("Hier passt kein Packstück.");
    }
    // if(L<200){
    // double tempD=200/L;
    // tempD=tempD*10;
    // int tempI= (int) tempD;
    // skalierungsFaktor=tempI/10.0;
    // if(B*skalierungsFaktor<=320){
    // LSkal=(int)(L*skalierungsFaktor);
    // BSkal=(int)(B*skalierungsFaktor);
    // bSkal=(int)(b*skalierungsFaktor);
    // lSkal=(int)(l*skalierungsFaktor);
    // return true;
    // }else return false;
    // }else if(L>350){
    // double tempD=200/L;
    // tempD=tempD*10;
    // int tempI= (int) tempD;
    // skalierungsFaktor=tempI/10.0;
    // if(B*skalierungsFaktor<=320){
    // return true;
    // }else return false;
    //
    // }else{
    // skalierungsFaktor=1;
    // return true;
    // }
    return true;
  }

}