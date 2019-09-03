package generators.cryptography.helpers;

import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalListElementGenerator;
import algoanim.animalscript.AnimalRectGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.primitives.ListElement;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.ListElementGenerator;
import algoanim.primitives.generators.RectGenerator;
import algoanim.primitives.generators.TextGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ListElementProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class DiffieHellmanView {

  private Language              lang;
  private SourceCodeProperties  sourceCode;
  private SourceCode            code;
  private TextGenerator         textGen;
  private TextProperties        prop;
  private Timing                timeWait;
  private ListElementProperties listElementProp;
  private ListElementGenerator  listElementGenerator;

  /*
   * private int p; private int g; private int a; private int b; private int
   * bigA; private int bigB; private int bigKB; private int bigKA;
   */

  public DiffieHellmanView() {
    lang = new AnimalScript("Diffie-Hellman Schlüsselaustausch",
        "Kristijan Madunic", 800, 600);
  }

  public DiffieHellmanView(Language l) {
    lang = l;
    lang.setStepMode(true);
  }

  public void init(AnimationPropertiesContainer props) {
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
  }

  public void setStepMode() {
    lang.setStepMode(true);
  }

  public String langToString() {
    return lang.toString();
  }

  public void initForAlgorithm() {
    textGen = new AnimalTextGenerator(lang);

    Node coodsTitle = new Coordinates(20, 20);
    Timing time = new TicksTiming(0);
    TextProperties propTitle = new TextProperties();
    propTitle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 20));

    new Text(textGen, coodsTitle, "Diffie-Hellman-Schluesselaustausch",
        "title", time, propTitle);

    RectGenerator rectGen = new AnimalRectGenerator(lang);
    Node nwRect = new Coordinates(15, 15);
    Node seRect = new Coordinates(407, 45);
    RectProperties rectProp = new RectProperties();
    rectProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    rectProp.set(AnimationPropertiesKeys.FILL_PROPERTY,
        new Color(192, 192, 192));
    rectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);

    new Rect(rectGen, nwRect, seRect, "titleRect", time, rectProp);

    TextProperties descrProp = new TextProperties();
    descrProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 16));

    Node nodeDecr01 = new Coordinates(15, 50);
    Text descr01 = new Text(
        textGen,
        nodeDecr01,
        "____________________________________________________________________________________",
        "descr01", time, descrProp);

    Node nodeDecr02 = new Coordinates(15, 75);
    Text descr02 = new Text(
        textGen,
        nodeDecr02,
        "Der Diffie-Hellman-Schluesselaustausch ist ein Protokol der Kryptografie.",
        "descr02", time, descrProp);

    Node nodeDecr03 = new Coordinates(15, 100);
    Text descr03 = new Text(
        textGen,
        nodeDecr03,
        "Dieser beschreibt die Moeglichkeit zum sicheren Austausch von symetrischen",
        "descr03", time, descrProp);

    Node nodeDecr04 = new Coordinates(15, 125);
    Text descr04 = new Text(
        textGen,
        nodeDecr04,
        "Schluesseln ueber unsichere Kanaele. Der Algorithmus wurde von Martin Hellman",
        "descr04", time, descrProp);

    Node nodeDecr05 = new Coordinates(15, 150);
    Text descr05 = new Text(textGen, nodeDecr05,
        "gemeinsam mit Whitfield Diffie und Ralph Merkle an der Universitaet",
        "descr05", time, descrProp);

    Node nodeDecr06 = new Coordinates(15, 175);
    Text descr06 = new Text(
        textGen,
        nodeDecr06,
        "von Stanford ( Kalifornien; USA ) entwickelt und 1976 veroeffentlicht. ",
        "descr06", time, descrProp);

    Node nodeDecr07 = new Coordinates(15, 185);
    Text descr07 = new Text(
        textGen,
        nodeDecr07,
        "____________________________________________________________________________________",
        "descr07", time, descrProp);

    Node nodeDecr08 = new Coordinates(15, 220);
    Text descr08 = new Text(
        textGen,
        nodeDecr08,
        "In folgender Ablaufbeschreibung des Protokols sind die Kommunikationspartner",
        "descr08", time, descrProp);

    Node nodeDecr09 = new Coordinates(15, 240);
    Text descr09 = new Text(
        textGen,
        nodeDecr09,
        "Alice und Bob, p ist eine Primzahl und g eine Primitivwurzel mit 1 < g < p - 1:",
        "descr09", time, descrProp);

    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 14));

    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    SourceCode sc;
    if (sourceCode == null) {
      sc = lang.newSourceCode(new Coordinates(15, 270), "code", null, scProps);
    } else {
      sc = lang.newSourceCode(new Coordinates(15, 270), "code", null,
          sourceCode);
    }

    sc.addCodeLine(
        "1. Alice und Bob einigen sich auf eine Primazahl p und Primitivwurzel g",
        null, 1, null);
    sc.addCodeLine("2. Alice waehlt eine Zufallszahl a", null, 1, null);
    sc.addCodeLine("3. Bob waehlt eine Zufallszahl b", null, 1, null);
    sc.addCodeLine("4. Alice berechnet A = g^a mod p", null, 1, null);
    sc.addCodeLine("5. Alice schickt A an Bob", null, 1, null);
    sc.addCodeLine("6. Bob berechnet B = g^b mod p", null, 1, null);
    sc.addCodeLine("7. Bob berechnet K = A^b mod p", null, 1, null);
    sc.addCodeLine("8. Bob schickt B an Alice", null, 1, null);
    sc.addCodeLine("9. Alice berechnet K = B^a mod p", null, 1, null);

    lang.nextStep();

    TextProperties propName = new TextProperties();
    propName.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));

    Node coodsAlice = new Coordinates(70, 90);
    new Text(textGen, coodsAlice, "Alice", "alice", time, propName);

    Node coodsBob = new Coordinates(500, 80);
    new Text(textGen, coodsBob, "Bob", "bob", time, propName);

    RectProperties recProp = new RectProperties();
    recProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

    Node nwRecA = new Coordinates(10, 110);
    Node seRecA = new Coordinates(200, 300);

    new Rect(rectGen, nwRecA, seRecA, "recAlice", time, recProp);

    Node nwRecB = new Coordinates(430, 110);
    Node seRecB = new Coordinates(620, 300);

    new Rect(rectGen, nwRecB, seRecB, "recBob", time, recProp);

    // SourceCode code;
    if (sourceCode == null) {
      code = lang
          .newSourceCode(new Coordinates(15, 320), "code", null, scProps);
    } else {
      code = lang.newSourceCode(new Coordinates(15, 320), "code", null,
          sourceCode);
    }

    code.addCodeLine(
        "1. Alice und Bob einigen sich auf eine Primazahl p und Primitivwurzel g",
        null, 1, null);
    code.addCodeLine("2. Alice waehlt eine Zufallszahl a", null, 1, null);
    code.addCodeLine("3. Bob waehlt eine Zufallszahl b", null, 1, null);
    code.addCodeLine("4. Alice berechnet A = g^a mod p", null, 1, null);
    code.addCodeLine("5. Alice schickt A an Bob", null, 1, null);
    code.addCodeLine("6. Bob berechnet B = g^b mod p", null, 1, null);
    code.addCodeLine("7. Bob berechnet K = A^b mod p", null, 1, null);
    code.addCodeLine("8. Bob schickt B an Alice", null, 1, null);
    code.addCodeLine("9. Alice berechnet K = B^a mod p", null, 1, null);

    sc.hide();

    descr01.hide();
    descr02.hide();
    descr03.hide();
    descr04.hide();
    descr05.hide();
    descr06.hide();
    descr07.hide();
    descr08.hide();
    descr09.hide();

  }

  public void pAndG(int p, int g) {
    lang.nextStep("p und g werden festgelegt");

    listElementGenerator = new AnimalListElementGenerator(lang);
    timeWait = new TicksTiming(250);
    listElementProp = new ListElementProperties();
    listElementProp.set("boxFillColor", new Color(255, 255, 255));
    listElementProp.set("position", 4);
    listElementProp.set("pointerAreaFillColor", new Color(255, 255, 255));
    listElementProp.set("text", " p: " + p);

    Node coodsLe1 = new Coordinates(265, 110);
    new ListElement(listElementGenerator, coodsLe1, 0, null, null, "p",
        timeWait, listElementProp);

    listElementProp.set("text", " g: " + g);

    Node coodsLe2 = new Coordinates(320, 110);
    new ListElement(listElementGenerator, coodsLe2, 0, null, null, "g",
        timeWait, listElementProp);

    code.highlight(0);

  }

  public void random_a(int a) {
    lang.nextStep("a wird bestimmt");

    prop = new TextProperties();
    prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 16));

    Node coodsa = new Coordinates(70, 120);
    new Text(textGen, coodsa, "a = " + a, "a", timeWait, prop);

    code.unhighlight(0);
    code.highlight(1);
  }

  public void random_b(int b) {
    lang.nextStep("b wird bestimmt");

    Node coodsb = new Coordinates(500, 120);
    new Text(textGen, coodsb, "b = " + b, "b", timeWait, prop);

    code.unhighlight(1);
    code.highlight(2);
  }

  public void calcA(int A, int g, int a, int p) {
    lang.nextStep("A wird berechnet");

    Node coodsA = new Coordinates(20, 170);

    new Text(textGen, coodsA, "A = " + g + "^" + a + " mod " + p + " = " + A,
        "A", timeWait, prop);

    code.unhighlight(2);
    code.highlight(3);
  }

  public void sendAtoBob(int A) {
    lang.nextStep("A wird verschickt");

    listElementProp.set("text", " " + A);

    Node coodsLe3 = new Coordinates(205, 165);
    ListElement le3 = new ListElement(listElementGenerator, coodsLe3, 0, null,
        null, "lineA", timeWait, listElementProp);

    code.unhighlight(3);
    code.highlight(4);

    lang.nextStep();

    le3.moveBy("translateWithFixedTip", 195, 0, new TicksTiming(250),
        new TicksTiming(250));

    lang.nextStep();

    le3.hide();

    Node coodsDesA = new Coordinates(300, 165);
    new Text(textGen, coodsDesA, Integer.toString(A), "desA", timeWait, prop);

    Node coodsLineA = new Coordinates(200, 175);
    new Text(textGen, coodsLineA, "------------------------>", "lineA",
        timeWait, prop);
  }

  public void calcB(int B, int g, int b, int p) {
    lang.nextStep("B wird berechnet");

    Node coodsB = new Coordinates(445, 170);

    new Text(textGen, coodsB, "B = " + g + "^" + b + " mod " + p + " = " + B,
        "B", timeWait, prop);

    code.unhighlight(4);
    code.highlight(5);
  }

  public void calcK_B(int KB, int g, int b, int p) {
    lang.nextStep("K wird berechnet");

    Node coodsKB = new Coordinates(445, 220);

    new Text(textGen, coodsKB, "K = " + g + "^" + b + " mod " + p + " = " + KB,
        "kB", timeWait, prop);

    code.unhighlight(5);
    code.highlight(6);
  }

  public void sendBtoAlice(int B) {
    lang.nextStep("B wird verschickt");

    listElementProp.set("text", " " + B);

    Node coodsLe4 = new Coordinates(390, 215);
    ListElement le4 = new ListElement(listElementGenerator, coodsLe4, 0, null,
        null, "lB", timeWait, listElementProp);

    code.unhighlight(6);
    code.highlight(7);

    lang.nextStep();

    le4.moveBy("translateWithFixedTip", -185, 0, new TicksTiming(250),
        new TicksTiming(250));

    lang.nextStep();

    le4.hide();

    Node coodsDesB = new Coordinates(300, 215);
    new Text(textGen, coodsDesB, Integer.toString(B), "desB", timeWait, prop);

    Node coodsLineB = new Coordinates(200, 225);
    new Text(textGen, coodsLineB, "<------------------------", "lineB",
        timeWait, prop);

  }

  public void calcK_A(int KA, int B, int a, int p) {
    lang.nextStep("K wird berechnet");

    Node coodsAK = new Coordinates(20, 220);

    new Text(textGen, coodsAK, "K = " + B + "^" + a + " mod " + p + " = " + KA,
        "aK", timeWait, prop);

    code.unhighlight(7);
    code.highlight(8);

    lang.nextStep("Foglerung...");

    code.unhighlight(8);

    prop.set("color", new Color(0, 0, 255));

    Node coodsEnd1 = new Coordinates(60, 520);
    new Text(textGen, coodsEnd1,
        "Der symmetrische Schluessel ist nun bei beiden Partnern gleich",
        "end2", timeWait, prop);

    Node coodsEnd2 = new Coordinates(20, 540);
    new Text(
        textGen,
        coodsEnd2,
        "und kann fuer die folgende Kommunikation untereinander benutzt werden!",
        "end1", timeWait, prop);

    lang.nextStep();
  }

}
