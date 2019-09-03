package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntArray;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.variables.IntegerVariable;

public class RabinKryptosystem implements Generator {
  private Language             lang;
  private ArrayProperties      Ergebnisarray;
  private TextProperties       Kommentartexte;
  private TextProperties       Titel;
  private int                  q;
  private int                  p;
  private SourceCodeProperties Erklaerungen;
  private int                  M;
  private int                  n;
  private TextProperties       Abschnittstitel;
  private int                  C;
  private long                 M1;
  private long                 M2;
  private int                  y1;
  private int                  y2;
  private int                  r;
  private int                  rn;
  private int                  s;
  private int                  sn;

  public void init() {
    lang = new AnimalScript("Rabin Kryptosystem",
        "Emre Cakmak, Franklin Labang", 800, 600);
    lang.setStepMode(true);

    final ArrayProperties Ergebnisarray = new ArrayProperties();
    Ergebnisarray.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 18));
    Ergebnisarray.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    Ergebnisarray.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    Ergebnisarray.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    Ergebnisarray.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
        Color.BLACK);
    Ergebnisarray
        .set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    Ergebnisarray.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.YELLOW);

    final TextProperties Titel = new TextProperties();
    Titel.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));

    final TextProperties Kommentartexte = new TextProperties();
    Kommentartexte.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 12));

    final TextProperties Abschnittstitel = new TextProperties();
    Abschnittstitel.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 18));

    final SourceCodeProperties Erklaerungen = new SourceCodeProperties();
    Erklaerungen.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    Erklaerungen.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 14));
    Erklaerungen
        .set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    Erklaerungen.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
  }

  public int[][][][][] ExtendetEuklid(int p, int q) {
    int[][][][][] zwvundu = new int[10][10][10][10][10];
    int i = 2;
    int quotient, r = 0, k, t, u, v;
    u = t = 1;
    v = k = 0;
    int p2 = p;
    zwvundu[1][0][0][0][0] = p2;
    int q2 = q;
    zwvundu[0][1][0][0][0] = q2;
    while (q2 > 0) {
      quotient = p2 / q2;
      r = p2 - quotient * q2;
      p2 = q2;
      q2 = r;
      zwvundu[i][0][0][0][0] = p2;
      zwvundu[0][i][0][0][0] = q2;
      zwvundu[0][0][i - 1][0][0] = quotient;
      r = u - quotient * k;
      u = k;
      k = r;
      zwvundu[0][0][0][i][0] = u;
      r = v - quotient * t;
      v = t;
      t = r;
      zwvundu[0][0][0][0][i] = v;
      i++;
    }
    zwvundu[0][0][0][9][0] = v;
    zwvundu[0][0][0][0][9] = u;
    i = 1;

    return zwvundu;
  }

  public void rabin() {

    Ergebnisarray.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 18));
    Titel.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));
    Kommentartexte.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 12));
    Abschnittstitel.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 18));

    RectProperties rectProps1 = new RectProperties();
    rectProps1.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    rectProps1.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps1.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);

    PolylineProperties polyProps1 = new PolylineProperties();
    polyProps1.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);

    // step 1
    Text title = lang.newText(new Coordinates(20, 30), "Rabin Kryptosystem",
        "title", null, Titel);
    lang.newRect(new Offset(-10, -10, title, AnimalScript.DIRECTION_NW),
        new Offset(10, 10, title, AnimalScript.DIRECTION_SE), "Rechteck", null,
        rectProps1);

    this.n = this.p * this.q;
    this.C = ((int) Math.pow(this.M, 2)) % this.n;

    M1 = (Math.round((double) Math.pow(C, (Float.valueOf(p + 1) / 4)))) % p;
    M2 = (Math.round((double) Math.pow(C, (Float.valueOf(q + 1) / 4)))) % q;

    int zwp;
    boolean swapped = false;
    if (p < q) {
      zwp = p;
      p = q;
      q = zwp;
      swapped = true;
    }
    int[][][][][] array1 = ExtendetEuklid(p, q);
    if (swapped == true) {
      zwp = q;
      q = p;
      p = zwp;
      swapped = false;
    }

    y1 = array1[0][0][0][9][0];
    y2 = array1[0][0][0][0][9];

    r = (int) (((y1 * p * M2) + (y2 * q * M1)) % n % n % n % n);

    // falls negative Ergebnisse
    if (r < 0) {
      r = r + n;
    }

    rn = (n - r) % n;

    s = (int) (((y1 * p * M2) - (y2 * q * M1)) % n);

    if (s < 0) {
      s = s + n;
    }

    sn = (n - s) % n;

    IntegerVariable pintvar = new IntegerVariable(this.p);
    IntegerVariable qintvar = new IntegerVariable(this.q);
    IntegerVariable Mintvar = new IntegerVariable(this.M);
    IntegerVariable nintvar = new IntegerVariable(this.n);
    IntegerVariable Cintvar = new IntegerVariable(this.C);
    IntegerVariable M1intvar = new IntegerVariable((int) this.M1);
    IntegerVariable M2intvar = new IntegerVariable((int) this.M2);
    IntegerVariable y1intvar = new IntegerVariable(this.y1);
    IntegerVariable y2intvar = new IntegerVariable(this.y2);
    IntegerVariable rintvar = new IntegerVariable(this.r);
    IntegerVariable rnintvar = new IntegerVariable(this.rn);
    IntegerVariable sintvar = new IntegerVariable(this.s);
    IntegerVariable snintvar = new IntegerVariable(this.sn);

    String einl = "Einleitung";
    lang.nextStep(einl);

    // step 2
    SourceCode Einleitung = lang.newSourceCode(new Coordinates(20, 100),
        "Einleitung", null, Erklaerungen);

    // Line, name, indentation, display dealy
    Einleitung
        .addCodeLine(
            "Das Rabin Kryptopsystem ist ein asymmetrisches Verschlüsselungsverfahren, ",
            null, 0, null);
    Einleitung.addCodeLine(
        "welches einen oeffentlichen Schluessel (zum verschluesseln)", null, 0,
        null);
    Einleitung.addCodeLine(
        "und einen privaten Schluessel (zum entschluesseln) verwendet. ", null,
        0, null);
    Einleitung.addCodeLine("   																		", null, 0, null);
    Einleitung.addCodeLine(
        "Die Sicherheit basiert auf dem Faktorisierungsproblem", null, 0, null);
    Einleitung.addCodeLine("und ist deswegen mit RSA verwandt.", null, 0, null);
    Einleitung.addCodeLine("   																		", null, 0, null);
    Einleitung.addCodeLine(
        "Der Hauptunterschied zu RSA ist die Beweisbarkeit der Sicherheit.",
        null, 0, null);
    Einleitung.addCodeLine("   																		", null, 0, null);
    Einleitung.addCodeLine(
        "In der Praxis findet das Rabin-Kr.system jedoch keine Anwendung,",
        null, 0, null);
    Einleitung.addCodeLine(
        "da die Entschluesselung nicht eindeutig und es angreifbar ist.", null,
        0, null);

    lang.nextStep();

    // step 3

    Einleitung.hide(new TicksTiming(1));
    Text tA = lang.newText(new Coordinates(20, 110),
        "Algorithmus in einzelnen Schritten", "tA", null, Abschnittstitel);

    SourceCode Algo = lang.newSourceCode(new Coordinates(40, 130), "Algo",
        null, Erklaerungen);

    Algo.addCodeLine("1.) Wähle 2 möglichst große Primzahlen p, q", null, 0,
        null);
    Algo.addCodeLine("												", null, 0, null);
    Algo.addCodeLine("2.) Setze n = p * q", null, 0, null);
    Algo.addCodeLine("												", null, 0, null);
    Algo.addCodeLine("3.) Kongruenzbedingung", null, 0, null);
    Algo.addCodeLine(" 			p ≡ q ≡ 3 (mod 4) erfüllt?", null, 2, null);
    Algo.addCodeLine("												", null, 0, null);
    Algo.addCodeLine("4.) Verschlüsselung", null, 0, null);
    Algo.addCodeLine("			C = M² mod n ", null, 2, null);
    Algo.addCodeLine("											", null, 0, null);
    Algo.addCodeLine("5.) Entschlüsselung", null, 0, null);
    Algo.addCodeLine("				M₁ = C^((p+1)/4) (mod p)", null, 2, null);
    Algo.addCodeLine("			und M₂ = C^((q+1)/4) (mod q)", null, 1, null);
    Algo.addCodeLine("												", null, 0, null);
    Algo.addCodeLine(" 			5.1.) bestimme y₁ und y₂ mit", null, 1, null);
    Algo.addCodeLine("						y₁ * p + y₂ * q = 1", null, 3, null);
    Algo.addCodeLine("												", null, 0, null);
    Algo.addCodeLine(" 			5.2.) bestimme 4 Quadratwurzeln: r, -r, s, -s mit",
        null, 1, null);
    Algo.addCodeLine("						r  = (y₁ * p * M₂ + y₂ * q * M₁)", null, 3, null);
    Algo.addCodeLine("						-r = n - r", null, 3, null);
    Algo.addCodeLine("						s  = (y₁ * p * M₂ - y₂ * q * M₁)", null, 3, null);
    Algo.addCodeLine("						-s = n - s", null, 3, null);

    String init = "Initialisierung";
    lang.nextStep(init);

    // step 4

    Algo.highlight(0);
    Text t1 = lang.newText(new Coordinates(680, 110), "Schlüsselerzeugung",
        "t1", null, Abschnittstitel);

    SourceCode Schluessel = lang.newSourceCode(new Coordinates(700, 130),
        "Schluessel", null, Erklaerungen);

    Schluessel.addCodeLine("Man wählt 2 möglichst große Primzahlen p und q, ",
        null, 0, null);
    Schluessel.addCodeLine("für die eine bestimmte Kongruenzbedingung gilt",
        null, 0, null);
    Schluessel.addCodeLine("											 ", null, 0, null);
    Schluessel.addCodeLine("=> Beispiel:   Setze p = " + pintvar + ", q =  "
        + qintvar, null, 0, null);
    Schluessel.addCodeLine("										", null, 0, null);
    Schluessel.addCodeLine("										 ", null, 0, null);
    Schluessel.addCodeLine(
        "Dann erhält man den oeffentlichen Schluessel: Setze n = p * q", null,
        0, null);
    Schluessel.addCodeLine("Der private Schluessel ist ein Tupel aus (p, q) ",
        null, 0, null);
    Schluessel.addCodeLine("											 ", null, 0, null);
    Schluessel.addCodeLine("=> n = " + pintvar + "*" + qintvar + ", also n =  "
        + nintvar, null, 0, null);
    Schluessel.addCodeLine("											", null, 0, null);
    Schluessel.addCodeLine("HINWEIS: ", null, 0, null);
    Schluessel.addCodeLine("Wer also nur n kennt, kann nur verschluesseln. ",
        null, 0, null);
    Schluessel.addCodeLine(
        "Wer aber p und q kennt, kann zusätzlich entschluesseln. ", null, 0,
        null);

    Polyline Polyline1 = lang.newPolyline(new Node[] {
        new Coordinates(50, 650), new Coordinates(500, 650) }, "Polyline1",
        null, polyProps1);
    Polyline Polyline2 = lang.newPolyline(new Node[] {
        new Coordinates(105, 620), new Coordinates(105, 680) }, "Polyline2",
        null, polyProps1);
    Polyline Polyline3 = lang.newPolyline(new Node[] {
        new Coordinates(155, 620), new Coordinates(155, 680) }, "Polyline3",
        null, polyProps1);
    Polyline Polyline4 = lang.newPolyline(new Node[] {
        new Coordinates(205, 620), new Coordinates(205, 680) }, "Polyline4",
        null, polyProps1);
    Polyline Polyline5 = lang.newPolyline(new Node[] {
        new Coordinates(255, 620), new Coordinates(255, 680) }, "Polyline5",
        null, polyProps1);
    Polyline Polyline6 = lang.newPolyline(new Node[] {
        new Coordinates(305, 620), new Coordinates(305, 680) }, "Polyline6",
        null, polyProps1);
    Polyline Polyline7 = lang.newPolyline(new Node[] {
        new Coordinates(355, 620), new Coordinates(355, 680) }, "Polyline7",
        null, polyProps1);
    Polyline Polyline8 = lang.newPolyline(new Node[] {
        new Coordinates(405, 620), new Coordinates(405, 680) }, "Polyline8",
        null, polyProps1);
    Polyline Polyline9 = lang.newPolyline(new Node[] {
        new Coordinates(455, 620), new Coordinates(455, 680) }, "Polyline9",
        null, polyProps1);

    Schluessel.highlight(0);
    Schluessel.highlight(1);
    Schluessel.highlight(3);
    Schluessel.highlight(4);

    lang.nextStep();

    // step 5

    Text ptext = lang.newText(new Coordinates(80, 630), "p", "p", null,
        Kommentartexte);
    Text qtext = lang.newText(new Coordinates(130, 630), "q", "q", null,
        Kommentartexte);
    Text pwert01 = lang.newText(new Coordinates(80, 660),
        String.valueOf(pintvar), "pwert01", null, Kommentartexte);
    Text qwert01 = lang.newText(new Coordinates(130, 660),
        String.valueOf(qintvar), "qwert01", null, Kommentartexte);

    lang.nextStep();

    // step 6

    Algo.unhighlight(0);
    Schluessel.unhighlight(0);
    Schluessel.unhighlight(1);
    Schluessel.unhighlight(2);
    Schluessel.unhighlight(3);
    Schluessel.unhighlight(4);
    Algo.highlight(2);
    Schluessel.highlight(6);
    Schluessel.highlight(7);
    Schluessel.highlight(8);
    Schluessel.highlight(9);

    lang.nextStep();

    // step 7

    Text ntext = lang.newText(new Coordinates(180, 630), "n", "n", null,
        Kommentartexte);
    Text nwert01 = lang.newText(new Coordinates(180, 660),
        String.valueOf(nintvar), "nwert01", null, Kommentartexte);

    lang.nextStep();

    // step 8

    Schluessel.hide(new TicksTiming(1));
    t1.hide(new TicksTiming(1));
    Algo.unhighlight(2);
    Schluessel.unhighlight(6);
    Schluessel.unhighlight(7);
    Schluessel.unhighlight(8);
    Schluessel.unhighlight(9);
    Algo.highlight(4);
    Algo.highlight(5);

    Text t2 = lang.newText(new Coordinates(680, 110), "Kongruenzbedingung",
        "t2", null, Abschnittstitel);

    SourceCode Kongr = lang.newSourceCode(new Coordinates(700, 130), "Kongr",
        null, Erklaerungen);

    Kongr.addCodeLine("Es muss gelten p ≡ 3 (mod 4) und q ≡ 3 (mod 4)", null,
        0, null);
    Kongr.addCodeLine("												", null, 0, null);
    Kongr.addCodeLine("=> Es gilt 7 ≡ 3 (mod 4) und 11 ≡ 3 (mod 4)", null, 0,
        null);
    Kongr.addCodeLine("													", null, 0, null);
    Kongr.addCodeLine("HINWEIS:", null, 0, null);
    Kongr.addCodeLine("Durch diese Bedingung wird die Entschlüsselung", null,
        0, null);
    Kongr.addCodeLine("vereinfacht und beschleunigt", null, 0, null);

    Kongr.highlight(0);
    Kongr.highlight(1);
    Kongr.highlight(2);

    lang.nextStep();

    // step 9

    Kongr.hide(new TicksTiming(1));
    t2.hide(new TicksTiming(1));
    Kongr.unhighlight(0);
    Kongr.unhighlight(1);
    Kongr.unhighlight(2);
    Algo.unhighlight(4);
    Algo.unhighlight(5);
    Algo.highlight(7);
    Algo.highlight(8);

    Text t3 = lang.newText(new Coordinates(680, 110), "Verschluesselung", "t3",
        null, Abschnittstitel);

    SourceCode Verschl = lang.newSourceCode(new Coordinates(700, 130),
        "Verschl", null, Erklaerungen);

    Verschl
        .addCodeLine(
            "Die Verschluesselung erfolgt nun mit dem oeffentlichen Schluessel mit",
            null, 0, null);
    Verschl.addCodeLine("C = M² (mod n)", null, 0, null);
    Verschl.addCodeLine("									", null, 0, null);
    Verschl.addCodeLine("=> Sei M = " + Mintvar, null, 0, null);
    Verschl.addCodeLine("=> C = " + Mintvar + "² (mod " + nintvar + ")"
        + ", also C = " + Cintvar, null, 0, null);
    Verschl.addCodeLine("														", null, 0, null);
    Verschl.addCodeLine("HINWEIS:", null, 0, null);
    Verschl
        .addCodeLine(
            "Der zu verschluesselnde Klartext M kann beliebig zwischen 0 und n-1 sein.",
            null, 0, null);
    Verschl.addCodeLine("Für einen bestimmten Geheimtext C gibt es", null, 0,
        null);
    Verschl.addCodeLine("genau 4 verschiedene M, da es für C", null, 0, null);
    Verschl.addCodeLine("genau vier verschiedene Quadratwurzeln mod n gibt",
        null, 0, null);

    Verschl.highlight(0);
    Verschl.highlight(1);

    lang.nextStep();

    // step 10

    Text Mtext = lang.newText(new Coordinates(230, 630), "M", "M", null,
        Kommentartexte);
    Text Mwert01 = lang.newText(new Coordinates(230, 660),
        String.valueOf(Mintvar), "Mwert01", null, Kommentartexte);
    Verschl.highlight(3);

    lang.nextStep();

    // step 11

    Text Ctext = lang.newText(new Coordinates(280, 630), "C", "C", null,
        Kommentartexte);
    Text Cwert01 = lang.newText(new Coordinates(280, 660),
        String.valueOf(Cintvar), "Cwert01", null, Kommentartexte);
    Verschl.unhighlight(3);
    Verschl.highlight(4);

    lang.nextStep();

    // step 12

    Verschl.hide(new TicksTiming(1));
    t3.hide(new TicksTiming(1));
    Verschl.unhighlight(0);
    Verschl.unhighlight(1);
    Verschl.unhighlight(2);
    Verschl.unhighlight(3);
    Verschl.unhighlight(4);
    Algo.unhighlight(7);
    Algo.unhighlight(8);
    Algo.highlight(10);
    Algo.highlight(11);
    Algo.highlight(12);

    Text t4 = lang.newText(new Coordinates(680, 110), "Entschluesselung", "t4",
        null, Abschnittstitel);

    SourceCode Entschl = lang.newSourceCode(new Coordinates(700, 130),
        "Entschl", null, Erklaerungen);

    Entschl
        .addCodeLine(
            "Bei der Entschluesselung seien nun C und r bekannt und M sei gesucht mit ",
            null, 0, null);
    Entschl.addCodeLine("M² ≡ c mod r", null, 0, null);
    Entschl.addCodeLine("  								", null, 0, null);
    Entschl.addCodeLine("Achtung: Für zusammengesetzte r (wie also unser n)",
        null, 0, null);
    Entschl.addCodeLine(
        "exisitiert kein effizientes Verfahren für das Ausrechnen von M", null,
        0, null);
    Entschl.addCodeLine("  								", null, 0, null);
    Entschl.addCodeLine(
        "Wenn aber r aus zwei Primzahlen (wie also unser p und q) besteht,",
        null, 0, null);
    Entschl.addCodeLine("kann der chinesische Restsatz angewendet werden,",
        null, 0, null);
    Entschl.addCodeLine(
        "welches hier nicht näher erläutert, sondern direkt verwendet wird.",
        null, 0, null);
    Entschl.addCodeLine("  								", null, 0, null);
    Entschl
        .addCodeLine(
            "Wir benutzen für die Entschluesselung nun also folgende Formeln mit den privaten Schlüsseln",
            null, 0, null);
    Entschl.addCodeLine("M₁ = √C (mod p) und M₂ = √C (mod q)", null, 0, null);
    Entschl.addCodeLine("									", null, 0, null);
    Entschl.addCodeLine("weiter gilt durch die Kongruenzbedingung:", null, 0,
        null);
    Entschl.addCodeLine("M₁ = C^((p+1)/4) (mod p)", null, 0, null);
    Entschl.addCodeLine("M₂ = C^((q+1)/4) (mod q)", null, 0, null);
    Entschl.addCodeLine("						", null, 0, null);
    Entschl.addCodeLine(" => M₁ = " + Cintvar + "^ ((" + pintvar
        + "+1)/4) mod " + pintvar + ", also M₁ = " + M1intvar, null, 0, null);
    Entschl.addCodeLine(" => M₂ = " + Cintvar + "^ ((" + qintvar
        + "+1)/4) mod " + qintvar + ", also M₂ = " + M2intvar, null, 0, null);

    Entschl.highlight(0);
    Entschl.highlight(1);

    lang.nextStep();

    // step 13

    Entschl.unhighlight(0);
    Entschl.unhighlight(1);

    Text M1text = lang.newText(new Coordinates(330, 630), "M₁", "M₁", null,
        Kommentartexte);
    Text M1wert01 = lang.newText(new Coordinates(330, 660),
        String.valueOf(M1intvar), "M₁wert01", null, Kommentartexte);

    Entschl.highlight(17);

    lang.nextStep();

    // step 14

    Entschl.unhighlight(17);

    Text M2text = lang.newText(new Coordinates(380, 630), "M₂", "M₂", null,
        Kommentartexte);
    Text M2wert01 = lang.newText(new Coordinates(380, 660),
        String.valueOf(M2intvar), "M₂wert01", null, Kommentartexte);

    Entschl.highlight(18);

    String iteration = "Iteration";
    lang.nextStep(iteration);

    // step 15

    Entschl.hide(new TicksTiming(1));
    t4.hide(new TicksTiming(1));
    Algo.unhighlight(10);
    Algo.unhighlight(11);
    Algo.unhighlight(12);
    Algo.highlight(14);
    Algo.highlight(15);

    Text t5 = lang.newText(new Coordinates(680, 110),
        "Erweiterter euklidischer Algorithmus", "t5", null, Abschnittstitel);

    Erklaerungen.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.BLUE);

    SourceCode Euklidalgo = lang.newSourceCode(new Coordinates(700, 130),
        "Euklidalgo", null, Erklaerungen);

    Euklidalgo.addCodeLine(
        "Nun werden y₁ und y₂ gesucht, für die gilt:    y₁ * p + y₂ * q = 1",
        null, 0, null);
    Euklidalgo.addCodeLine("									", null, 0, null);
    Euklidalgo.addCodeLine("									", null, 0, null);
    Euklidalgo.addCodeLine("Pseudocode Erw-Euklid:", null, 0, null); // 0
    Euklidalgo.addCodeLine("									", null, 0, null);
    Euklidalgo.addCodeLine("IF q = 0 THEN return (p, 1, 0)", null, 2, null);
    Euklidalgo.addCodeLine("ELSE DO", null, 4, null); // 3
    Euklidalgo.addCodeLine("(d, y₁, y₂) := Erw-Euklid (q, p mod q)", null, 6,
        null); // 4
    Euklidalgo.addCodeLine("return (d, y₂, y₁ - [p/q] * l)", null, 6, null);
    Euklidalgo.addCodeLine(" 			", null, 0, null);
    Euklidalgo.addCodeLine(" 			", null, 0, null);
    Euklidalgo.addCodeLine(
        "Der Algorithmus Erw-Euklid(a, b) liefert (d, y₁, y₂)", null, 0, null);

    Erklaerungen
        .set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);

    lang.nextStep();

    // step 16

    Euklidalgo.highlight(5);

    Polyline PolylineE1 = lang.newPolyline(new Node[] {
        new Coordinates(680, 610), new Coordinates(1080, 610) }, "PolylineE1",
        null, polyProps1);
    Polyline PolylineE10 = lang.newPolyline(new Node[] {
        new Coordinates(680, 630), new Coordinates(1080, 630) }, "PolylineE10",
        null, polyProps1);
    Polyline PolylineE11 = lang.newPolyline(new Node[] {
        new Coordinates(680, 650), new Coordinates(1080, 650) }, "PolylineE11",
        null, polyProps1);
    Polyline PolylineE12 = lang.newPolyline(new Node[] {
        new Coordinates(680, 670), new Coordinates(1080, 670) }, "PolylineE12",
        null, polyProps1);
    Polyline PolylineE13 = lang.newPolyline(new Node[] {
        new Coordinates(680, 670), new Coordinates(1080, 670) }, "PolylineE13",
        null, polyProps1);
    Polyline PolylineE14 = lang.newPolyline(new Node[] {
        new Coordinates(680, 690), new Coordinates(1080, 690) }, "PolylineE14",
        null, polyProps1);
    Polyline PolylineE15 = lang.newPolyline(new Node[] {
        new Coordinates(680, 710), new Coordinates(1080, 710) }, "PolylineE15",
        null, polyProps1);
    Polyline PolylineE16 = lang.newPolyline(new Node[] {
        new Coordinates(680, 730), new Coordinates(1080, 730) }, "PolylineE16",
        null, polyProps1);
    Polyline PolylineE2 = lang.newPolyline(new Node[] {
        new Coordinates(760, 580), new Coordinates(760, 750) }, "PolylineE2",
        null, polyProps1);
    Polyline PolylineE3 = lang.newPolyline(new Node[] {
        new Coordinates(840, 580), new Coordinates(840, 750) }, "PolylineE3",
        null, polyProps1);
    Polyline PolylineE4 = lang.newPolyline(new Node[] {
        new Coordinates(920, 580), new Coordinates(920, 750) }, "PolylineE4",
        null, polyProps1);
    Polyline PolylineE5 = lang.newPolyline(new Node[] {
        new Coordinates(1000, 580), new Coordinates(1000, 750) }, "PolylineE5",
        null, polyProps1);

    Text p2 = lang.newText(new Coordinates(720, 580), "p", "p2", null,
        Kommentartexte);
    Text q2 = lang.newText(new Coordinates(800, 580), "q", "q2", null,
        Kommentartexte);
    Text quo = lang.newText(new Coordinates(880, 580), "[p / q]", "[p/q]",
        null, Kommentartexte);
    Text y102 = lang.newText(new Coordinates(960, 580), "y₁", "y₁02", null,
        Kommentartexte);
    Text y202 = lang.newText(new Coordinates(1040, 580), "y₂", "y₂02", null,
        Kommentartexte);
    Text pwert1 = lang.newText(new Coordinates(720, 610),
        String.valueOf(array1[1][0][0][0][0]), "pwert1", null, Kommentartexte);
    Text qwert1 = lang.newText(new Coordinates(800, 610),
        String.valueOf(array1[0][1][0][0][0]), "qwert1", null, Kommentartexte);
    Text quotientwert1 = lang.newText(new Coordinates(880, 610),
        String.valueOf(array1[0][0][1][0][0]), "quotientwert1", null,
        Kommentartexte);

    lang.nextStep();

    // step 17

    Euklidalgo.unhighlight(5);
    Euklidalgo.highlight(7);

    lang.nextStep();

    // step 18

    Text pwert2 = lang.newText(new Coordinates(720, 630),
        String.valueOf(array1[2][0][0][0][0]), "pwert2", null, Kommentartexte);
    Text qwert2 = lang.newText(new Coordinates(800, 630),
        String.valueOf(array1[0][2][0][0][0]), "qwert2", null, Kommentartexte);
    Text quotientwert2 = lang.newText(new Coordinates(880, 630),
        String.valueOf(array1[0][0][2][0][0]), "quotientwert2", null,
        Kommentartexte);
    Euklidalgo.unhighlight(7);
    Euklidalgo.highlight(5);

    lang.nextStep();

    // step 19

    Euklidalgo.unhighlight(5);
    Euklidalgo.highlight(7);

    lang.nextStep();

    // step 20

    Text pwert3 = lang.newText(new Coordinates(720, 650),
        String.valueOf(array1[3][0][0][0][0]), "pwert3", null, Kommentartexte);
    Text qwert3 = lang.newText(new Coordinates(800, 650),
        String.valueOf(array1[0][3][0][0][0]), "qwert3", null, Kommentartexte);
    Text quotientwert3 = lang.newText(new Coordinates(880, 650),
        String.valueOf(array1[0][0][3][0][0]), "quotientwert3", null,
        Kommentartexte);
    Euklidalgo.unhighlight(7);
    Euklidalgo.highlight(5);

    lang.nextStep();

    // step 21

    Euklidalgo.unhighlight(5);
    Euklidalgo.highlight(7);

    lang.nextStep();

    // step 22

    Text pwert4 = lang.newText(new Coordinates(720, 670),
        String.valueOf(array1[4][0][0][0][0]), "pwert4", null, Kommentartexte);
    Text qwert4 = lang.newText(new Coordinates(800, 670),
        String.valueOf(array1[0][4][0][0][0]), "qwert4", null, Kommentartexte);
    Text quotientwert4 = lang.newText(new Coordinates(880, 670),
        String.valueOf(array1[0][0][4][0][0]), "quotientwert4", null,
        Kommentartexte);
    Euklidalgo.unhighlight(7);
    Euklidalgo.highlight(5);

    lang.nextStep();

    // step 23

    Euklidalgo.unhighlight(5);
    Euklidalgo.highlight(7);

    lang.nextStep();

    // step 24

    int y11zahl = 1;
    int y21zahl = 0;
    int y12zahl = y21zahl;
    int y22zahl = y11zahl - (array1[0][0][4][0][0] * y12zahl);
    int y13zahl = y22zahl;
    int y23zahl = y12zahl - (array1[0][0][3][0][0] * y13zahl);
    int y14zahl = y23zahl;
    int y24zahl = y13zahl - (array1[0][0][2][0][0] * y14zahl);
    int y15zahl = y24zahl;
    int y25zahl = y14zahl - (array1[0][0][1][0][0] * y15zahl);

    Kommentartexte.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 12));
    Text pwert5 = lang.newText(new Coordinates(720, 690),
        String.valueOf(array1[5][0][0][0][0]), "pwert5", null, Kommentartexte);
    Kommentartexte.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 12));
    Text qwert5 = lang.newText(new Coordinates(800, 690),
        String.valueOf(array1[0][5][0][0][0]), "qwert5", null, Kommentartexte);
    Text quotientwert5 = lang.newText(new Coordinates(880, 690), " ",
        "quotientwert5", null, Kommentartexte);
    Euklidalgo.unhighlight(7);
    Euklidalgo.highlight(5);

    lang.nextStep();

    // step 25

    Text y11 = lang.newText(new Coordinates(960, 690), String.valueOf(y11zahl),
        "y₁1", null, Kommentartexte);
    Text y21 = lang.newText(new Coordinates(1020, 690),
        String.valueOf(y21zahl), "y₂1", null, Kommentartexte);
    Euklidalgo.unhighlight(5);
    Euklidalgo.highlight(8);

    lang.nextStep();

    // step 26

    Text y12 = lang.newText(new Coordinates(960, 670), String.valueOf(y21zahl),
        "y₁2", null, Kommentartexte);
    Text y22 = lang.newText(new Coordinates(1020, 670),
        String.valueOf(y22zahl), "y₂2", null, Kommentartexte);

    lang.nextStep();

    // step 27

    Text y13 = lang.newText(new Coordinates(960, 650), String.valueOf(y22zahl),
        "y₁3", null, Kommentartexte);
    Text y23 = lang.newText(new Coordinates(1020, 650),
        String.valueOf(y23zahl), "y₂3", null, Kommentartexte);

    lang.nextStep();

    // step 28

    Text y14 = lang.newText(new Coordinates(960, 630), String.valueOf(y23zahl),
        "y₁4", null, Kommentartexte);
    Text y24 = lang.newText(new Coordinates(1020, 630),
        String.valueOf(y24zahl), "y₂4", null, Kommentartexte);

    lang.nextStep();

    // step 29

    Kommentartexte.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 12));

    Text y15 = lang.newText(new Coordinates(960, 610), String.valueOf(y24zahl),
        "y₁5", null, Kommentartexte);
    Text y25 = lang.newText(new Coordinates(1020, 610),
        String.valueOf(y25zahl), "y₂5", null, Kommentartexte);

    Kommentartexte.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 12));

    Text y1text = lang.newText(new Coordinates(430, 630), "y₁", "y₁", null,
        Kommentartexte);
    Text y1wert01 = lang.newText(new Coordinates(430, 660),
        String.valueOf(y24zahl), "y₁wert01", null, Kommentartexte);
    Text y2text = lang.newText(new Coordinates(480, 630), "y₂", "y₂", null,
        Kommentartexte);
    Text y2wert01 = lang.newText(new Coordinates(480, 660),
        String.valueOf(y25zahl), "y₂wert01", null, Kommentartexte);

    Kommentartexte.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    Kommentartexte.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 14));
    Text erg = lang.newText(new Coordinates(700, 400),
        "=> Der Algo liefert y₁ = " + y24zahl + " und y₂ = " + y25zahl, "erg",
        null, Kommentartexte);
    Kommentartexte.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    Kommentartexte.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 12));

    lang.nextStep();

    // step 30

    Algo.unhighlight(15);
    Algo.unhighlight(14);
    Euklidalgo.hide(new TicksTiming(1));
    p2.hide(new TicksTiming(1));
    q2.hide(new TicksTiming(1));
    y102.hide(new TicksTiming(1));
    y202.hide(new TicksTiming(1));
    quo.hide(new TicksTiming(1));
    PolylineE10.hide(new TicksTiming(1));
    PolylineE11.hide(new TicksTiming(1));
    PolylineE12.hide(new TicksTiming(1));
    PolylineE13.hide(new TicksTiming(1));
    PolylineE14.hide(new TicksTiming(1));
    PolylineE15.hide(new TicksTiming(1));
    PolylineE16.hide(new TicksTiming(1));
    PolylineE1.hide(new TicksTiming(1));
    PolylineE2.hide(new TicksTiming(1));
    PolylineE3.hide(new TicksTiming(1));
    PolylineE4.hide(new TicksTiming(1));
    PolylineE5.hide(new TicksTiming(1));
    erg.hide(new TicksTiming(1));
    pwert1.hide(new TicksTiming(1));
    pwert2.hide(new TicksTiming(1));
    pwert3.hide(new TicksTiming(1));
    pwert4.hide(new TicksTiming(1));
    pwert5.hide(new TicksTiming(1));
    qwert1.hide(new TicksTiming(1));
    qwert2.hide(new TicksTiming(1));
    qwert3.hide(new TicksTiming(1));
    qwert4.hide(new TicksTiming(1));
    qwert5.hide(new TicksTiming(1));
    y11.hide(new TicksTiming(1));
    y12.hide(new TicksTiming(1));
    y13.hide(new TicksTiming(1));
    y14.hide(new TicksTiming(1));
    y15.hide(new TicksTiming(1));
    y21.hide(new TicksTiming(1));
    y22.hide(new TicksTiming(1));
    y23.hide(new TicksTiming(1));
    y24.hide(new TicksTiming(1));
    y25.hide(new TicksTiming(1));
    quotientwert1.hide(new TicksTiming(1));
    quotientwert2.hide(new TicksTiming(1));
    quotientwert3.hide(new TicksTiming(1));
    quotientwert4.hide(new TicksTiming(1));
    quotientwert5.hide(new TicksTiming(1));

    Algo.highlight(16);
    Algo.highlight(17);
    Algo.highlight(18);
    Algo.highlight(19);
    Algo.highlight(20);
    Algo.highlight(21);

    SourceCode Quadratwurzel1 = lang.newSourceCode(new Coordinates(700, 130),
        "Quadratwurzel1", null, Erklaerungen);

    Quadratwurzel1.addCodeLine(
        "Jetzt werden die 4 Quadratwurzeln +r, -r, +s und -s berechnet.", null,
        0, null);
    Quadratwurzel1.addCodeLine(
        "Der gesuchte Klartext M ist eines der 4 Quadratwurzeln.", null, 0,
        null);
    Quadratwurzel1.addCodeLine("Welches Ergebnis jedoch das richtige ist,",
        null, 0, null);
    Quadratwurzel1.addCodeLine("ist nicht eindeutig und muss erraten werden.",
        null, 0, null);
    Quadratwurzel1.addCodeLine("", null, 0, null);
    Quadratwurzel1.addCodeLine("														", null, 0, null);
    Quadratwurzel1.addCodeLine(
        "Die Quadratwurzeln liegen in der Menge {0, ... , n-1}", null, 0, null);

    lang.nextStep();

    // step 31

    // //////////////////////////////////////////////////////////

    // int[] ergebnis = {64, 13, 20, 57};
    int[] ergebnis = new int[4];

    IntArray ergebnisfeld = lang.newIntArray(new Coordinates(700, 675),
        ergebnis, "ergebnisfeld", null, Ergebnisarray);

    SourceCode Ergebnismenge = lang.newSourceCode(new Coordinates(700, 630),
        "Ergebnismenge", null, Erklaerungen);

    Ergebnismenge.addCodeLine("Der Klartext ist ein Wert aus der Menge", null,
        0, null);
    Ergebnismenge.addCodeLine("							", null, 0, null);
    Ergebnismenge.addCodeLine("							", null, 0, null);
    Ergebnismenge.addCodeLine("							", null, 0, null);
    Ergebnismenge.addCodeLine(
        "und muss leider erraten werden, in diesem Fall ist das Ergebnis die "
            + Mintvar, null, 0, null);

    lang.nextStep();

    // step 32

    Quadratwurzel1.hide(new TicksTiming(1));

    SourceCode Quadratwurzel2 = lang.newSourceCode(new Coordinates(700, 130),
        "Quadratwurzel2", null, Erklaerungen);

    Quadratwurzel2.addCodeLine("1.)    r  = (y₂ * p * M₂ + y₁ * q * M₁) mod n",
        null, 0, null);
    Quadratwurzel2.addCodeLine("							", null, 0, null);
    Quadratwurzel2.addCodeLine("=>   r = (" + y1intvar + " * " + pintvar
        + " * " + M2intvar + ") + (" + y2intvar + " * " + qintvar + " * "
        + M1intvar + ") mod " + nintvar, null, 0, null);
    Quadratwurzel2.addCodeLine("=>   r = " + rintvar, null, 0, null);

    Quadratwurzel2.highlight(3);
    ergebnisfeld.highlightCell(0, null, null);
    ergebnisfeld.put(0, r, null, null);

    lang.nextStep();

    // step 33

    SourceCode Quadratwurzel3 = lang.newSourceCode(new Coordinates(700, 260),
        "Quadratwurzel3", null, Erklaerungen);

    Quadratwurzel3.addCodeLine("2.)   -r = n - r", null, 0, null);
    Quadratwurzel3.addCodeLine("							", null, 0, null);
    Quadratwurzel3.addCodeLine("=>   -r = " + nintvar + " - " + rintvar, null,
        0, null);
    Quadratwurzel3.addCodeLine("=>   -r = " + rnintvar, null, 0, null);

    Quadratwurzel3.highlight(3);
    ergebnisfeld.unhighlightCell(0, null, null);
    ergebnisfeld.highlightCell(1, null, null);
    ergebnisfeld.put(1, rn, null, null);

    lang.nextStep();

    // step 34

    SourceCode Quadratwurzel4 = lang.newSourceCode(new Coordinates(700, 390),
        "Quadratwurzel4", null, Erklaerungen);

    Quadratwurzel4.addCodeLine("3.)    s  = (y₂ * p * M₂ - y₁ * q * M₁) mod n",
        null, 0, null);
    Quadratwurzel4.addCodeLine("							", null, 0, null);
    Quadratwurzel4.addCodeLine("=>    s = (" + y1intvar + " * " + pintvar
        + " * " + M2intvar + ") - (" + y2intvar + " * " + qintvar + " * "
        + M1intvar + ") mod " + nintvar, null, 0, null);
    Quadratwurzel4.addCodeLine("=>    s = " + sintvar, null, 0, null);

    Quadratwurzel4.highlight(3);
    ergebnisfeld.unhighlightCell(1, null, null);
    ergebnisfeld.highlightCell(2, null, null);
    ergebnisfeld.put(2, s, null, null);

    lang.nextStep();

    // step 35

    SourceCode Quadratwurzel5 = lang.newSourceCode(new Coordinates(700, 520),
        "Quadratwurzel5", null, Erklaerungen);

    Quadratwurzel5.addCodeLine("3.)    -s = n - s", null, 0, null);
    Quadratwurzel5.addCodeLine("							", null, 0, null);
    Quadratwurzel5.addCodeLine("=>    -s = " + nintvar + " - " + sintvar, null,
        0, null);
    Quadratwurzel5.addCodeLine("=>    -s = " + snintvar, null, 0, null);

    Quadratwurzel5.highlight(3);
    ergebnisfeld.unhighlightCell(2, null, null);
    ergebnisfeld.highlightCell(3, null, null);
    ergebnisfeld.put(3, sn, null, null);

    String fazit = "Fazit";
    lang.nextStep(fazit);

    // step 36

    ergebnisfeld.hide(new TicksTiming(1));
    pwert01.hide(new TicksTiming(1));
    qwert01.hide(new TicksTiming(1));
    nwert01.hide(new TicksTiming(1));
    Mwert01.hide(new TicksTiming(1));
    Cwert01.hide(new TicksTiming(1));
    M1wert01.hide(new TicksTiming(1));
    M2wert01.hide(new TicksTiming(1));
    y1wert01.hide(new TicksTiming(1));
    y2wert01.hide(new TicksTiming(1));
    t4.hide(new TicksTiming(1));
    tA.hide(new TicksTiming(1));
    t5.hide(new TicksTiming(1));
    Entschl.hide(new TicksTiming(1));
    Quadratwurzel1.hide(new TicksTiming(1));
    Quadratwurzel2.hide(new TicksTiming(1));
    Quadratwurzel3.hide(new TicksTiming(1));
    Quadratwurzel4.hide(new TicksTiming(1));
    Quadratwurzel5.hide(new TicksTiming(1));
    Ergebnismenge.hide(new TicksTiming(1));
    Algo.hide(new TicksTiming(1));
    Polyline1.hide(new TicksTiming(1));
    Polyline2.hide(new TicksTiming(1));
    Polyline3.hide(new TicksTiming(1));
    Polyline4.hide(new TicksTiming(1));
    Polyline5.hide(new TicksTiming(1));
    Polyline6.hide(new TicksTiming(1));
    Polyline7.hide(new TicksTiming(1));
    Polyline8.hide(new TicksTiming(1));
    Polyline9.hide(new TicksTiming(1));
    ptext.hide(new TicksTiming(1));
    qtext.hide(new TicksTiming(1));
    ntext.hide(new TicksTiming(1));
    Mtext.hide(new TicksTiming(1));
    M1text.hide(new TicksTiming(1));
    M2text.hide(new TicksTiming(1));
    Ctext.hide(new TicksTiming(1));
    y1text.hide(new TicksTiming(1));
    y2text.hide(new TicksTiming(1));

    SourceCode Zusammenfassung = lang.newSourceCode(new Coordinates(40, 100),
        "Zusammenfassung", null, Erklaerungen);

    Zusammenfassung.addCodeLine("Vorteile:", null, 0, null);
    Zusammenfassung
        .addCodeLine(
            " => Das Rabin Kryptosystem gilt insgesamt als sicherer als das vergleichbare RSA,",
            null, 0, null);
    Zusammenfassung.addCodeLine("da die Sicherheit sich zeigen lässt.", null,
        0, null);
    Zusammenfassung
        .addCodeLine(
            "RSA beruht ebenfalls auf dem Faktorisierungsproblem, ist aber nicht beweisbar.",
            null, 0, null);
    Zusammenfassung.addCodeLine("	", null, 0, null);
    Zusammenfassung
        .addCodeLine(
            "=> Bei der Verschlüsselung wird erst quadriert und dann modulo gerechnet.",
            null, 0, null);
    Zusammenfassung.addCodeLine(
        "Das erhöht die Effizienz der Verschlüsselung.", null, 0, null);
    Zusammenfassung.addCodeLine("	", null, 0, null);
    Zusammenfassung.addCodeLine("	", null, 0, null);
    Zusammenfassung.addCodeLine("Nachteile:", null, 0, null);
    Zusammenfassung
        .addCodeLine(
            "=> Bei der Entschlüsselung werden neben dem tatsächlichen Klartext noch 3 weitere Ergebnisse geliefert.",
            null, 0, null);
    Zusammenfassung.addCodeLine(
        "Der tatsächliche Klartext muss also erraten werden.", null, 0, null);
    Zusammenfassung.addCodeLine(
        "Dies gilt als das Hauptproblem vom Rabin Kryptosystem", null, 0, null);
    Zusammenfassung.addCodeLine("					", null, 0, null);
    Zusammenfassung.addCodeLine(
        "=> Mögliche Angriffsmöglichkeit durch 'chosen-ciphertext attack'.",
        null, 0, null);
    Zusammenfassung
        .addCodeLine(
            "Dabei wählt sich der Angreifer einen beliebigen Klartext und kann somit den privaten Schlüssel brechen.",
            null, 0, null);

    Zusammenfassung.highlight(0);
    Zusammenfassung.highlight(9);

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    Ergebnisarray = (ArrayProperties) props
        .getPropertiesByName("Ergebnisarray");
    Kommentartexte = (TextProperties) props
        .getPropertiesByName("Kommentartexte");
    Titel = (TextProperties) props.getPropertiesByName("Titel");
    q = (Integer) primitives.get("q");
    p = (Integer) primitives.get("p");
    Erklaerungen = (SourceCodeProperties) props
        .getPropertiesByName("Erklaerungen");
    M = (Integer) primitives.get("M");
    Abschnittstitel = (TextProperties) props
        .getPropertiesByName("Abschnittstitel");

    Erklaerungen.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("Erklaerungen", AnimationPropertiesKeys.COLOR_PROPERTY));

    // TextProperties Titel = new TextProperties();
    // Titel.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
    // Font.BOLD, 24) );

    this.rabin();
    return lang.toString();
  }

  public String getName() {
    return "Rabin Kryptosystem";
  }

  public String getAlgorithmName() {
    return "Rabin Kryptosystem";
  }

  public String getAnimationAuthor() {
    return "Emre Cakmak, Franklin Labang";
  }

  public String getDescription() {
    return "Das Rabin Kryptopsystem ist ein asymmetrisches Verschl&uuml;sselungsverfahren,"
        + "\n"
        + "welches einen &ouml;ffentlichen Schl&uuml;ssel (zum verschl&uuml;sseln)"
        + "\n"
        + "und einen privaten Schl&uuml;ssel (zum entschl&uuml;sseln) verwendet."
        + "\n"
        + "\n"
        + "Die Sicherheit basiert auf dem Faktorisierungsproblem"
        + "\n"
        + "und ist deswegen mit RSA verwandt."
        + "\n"
        + "\n"
        + "Der Hauptunterschied zu RSA ist die Beweisbarkeit der Sicherheit."
        + "\n"
        + "\n"
        + "In der Praxis findet das Rabin-Kryptosystem jedoch keine Anwendung,"
        + "\n"
        + "da die Entschl&uuml;sselung nicht eindeutig und es angreifbar ist.";
  }

  public String getCodeExample() {
    return "IF q = 0 THEN return (p, 1, 0)	" + "\n" + "    ELSE DO" + "\n"
        + "        (d, y₁, y₂) := Erw-Euklid (q, p mod q)" + "\n"
        + "        return (d, y₂, y₁ - [p/q] * l)";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}