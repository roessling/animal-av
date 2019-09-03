package generators.cryptography.rsa;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

//import algoanim.variables.Variable;

public class RSA_XML implements Generator {
  private Language             lang;
  private TextProperties       textColor;
  private PolylineProperties   polyline;
  private int                  q;
  private boolean              Anfaenger;
  private int                  p;
  private SourceCodeProperties sourceCodeColor;
  private int                  Nachricht;
  private int                  phi;
  private Boolean              eIsValid;
  private int                  n;
  private int                  e;
  private int                  d;
  Color                        SourceColor = Color.black,
      sourceHighlight = Color.red, descColor = Color.BLUE,
      tableColor = Color.BLACK, resultColor = Color.BLUE;

  private Text                 describtion1, describtion2 /*
                                                           * ,klartext,pText,qText
                                                           * ,
                                                           * nText,phiText,eText
                                                           * ,dText,mText,cText,
                                                           * klarErg
                                                           */, pErg, qErg,
      nErg, phiErg, eErg, dErg, cErg, mErg;
  // private SourceCode sc ;
  private SecureRandom         random;
  // private BigInteger we;
  Variables                    vars;

  public void init() {
    lang = new AnimalScript("RSA Algorithmus [DE]",
        "Nkepseu Tchassep Soule,Ibrahim Alyahya", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    textColor = (TextProperties) props.getPropertiesByName("textColor");
    polyline = (PolylineProperties) props.getPropertiesByName("polyline");
    sourceCodeColor = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeColor");

    if (primitives.get("q") instanceof String)
      q = Integer.valueOf((String) primitives.get("q"));
    else
      q = (Integer) primitives.get("q");

    if (primitives.get("Anfaenger") instanceof String)
      Anfaenger = Boolean.getBoolean((String) primitives.get("Anfaenger"));
    else
      Anfaenger = (Boolean) primitives.get("Anfaenger");

    if (primitives.get("p") instanceof String)
      p = Integer.valueOf((String) primitives.get("p"));
    else
      p = (Integer) primitives.get("p");

    if (primitives.get("Nachricht") instanceof String)
      Nachricht = Integer.valueOf((String) primitives.get("Nachricht"));
    else
      Nachricht = (Integer) primitives.get("Nachricht");

    if (primitives.get("e") instanceof String)
      e = Integer.valueOf((String) primitives.get("e"));
    else
      e = (Integer) primitives.get("e");

    eIsValid = checkIfeIsValid();

    CheckpointUtils.checkpointEvent(this, "eIsValidEvent", new Variable(
        "eIsValid", eIsValid));

    calc(p, q, Nachricht, Anfaenger);
    return lang.toString();
  }

  private Boolean checkIfeIsValid() {
    if ((1 < e) && (e < phi) && (ggt(e, phi) == 1))
      return true;
    else
      return false;
  }

  private static int ggt(int zahl1, int zahl2) {
    int z1 = zahl1, z2 = zahl2;
    while (z2 != 0) {
      if (z1 > z2) {
        z1 = z1 - z2;
      } else {
        z2 = z2 - z1;
      }
    }
    return z1;
  }

  public String getName() {
    return "RSA Algorithmus [DE]";
  }

  public String getAlgorithmName() {
    return "RSA";
  }

  public String getAnimationAuthor() {
    return "Nkepseu Tchassep Soule, Ibrahim Alyahya";
  }

  public String getDescription() {
    return "RSA ist ein asymmetrisches Kryptosystem, das sowohl zur Verschluesselung als auch zur digitalen Signatur verwendet werden kann."
        + "\n"
        + "[1] Es verwendet ein Schluesselpaar, bestehend aus einem privaten Schluessel,der zum Entschluesseln oder Signieren von Daten"
        + "\n"
        + " verwendet wird, und einem oeffentlichen Schluessel, mit dem man verschluesselt oder Signaturen prüft."
        + "\n"
        + "Der private Schluessel wird geheimgehalten und kann nur mit extrem hohem Aufwand aus dem oeffentlichen Schluessel berechnet werden.";
  }

  public String getCodeExample() {
    return "1. Waehle zwei grosse Primzahlen p,q" + "\n" + "2. Setze n = p * q"
        + "\n" + "3. Berechne: phi(n) = (p - 1) * (q - 1)" + "\n"
        + "4. Waehle eine Primzahl e, mit:" + "\n" + "gcd(e,phi(n)) = 1" + "\n"
        + "5. Berechne d > 0, mit:" + "\n" + "e * d = 1 (mod phi(n))" + "\n"
        + "6. Verschluesselung:  c = m ^ e mod n" + "\n"
        + "7. Entschluesselung: m = c ^ d mod n";
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
    return Generator.JAVA_OUTPUT;
  }

  public void calc(int myp, int q, int m, boolean stufe) {
    BigInteger pBig = new BigInteger(String.valueOf(myp));
    BigInteger qBig = new BigInteger(String.valueOf(q));
    int myP = myp, myQ = q;
    
    if (!pBig.isProbablePrime(pBig.bitLength()))
      myP = BigInteger.probablePrime(String.valueOf(m).getBytes().length, random)
          .intValue();
    if (!qBig.isProbablePrime(qBig.bitLength()))
      myQ = BigInteger.probablePrime(String.valueOf(m).getBytes().length + 1,
          random).intValue();

    if (myP == myQ || myP * myQ < m) {

      // q = BigInteger.probablePrime(String.value);
      // p = BigInteger.probablePrime(String.valueOf(m).getBytes().length,
      // random).intValue();f(m).getBytes().length()+1, random).intValue();
    }

    TextProperties titleProps = new TextProperties();
    titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 15));

    algoanim.primitives.Text title = lang.newText(new Coordinates(20, 20),
        "RSA Algorithmus", "title", null, titleProps);

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(192, 192,
        192));
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    lang.newRect(new Offset(-5, -5, title, "NW"), new Offset(
        5, 5, title, "SE"), "titleRect", null, rectProps);

    vars = lang.newVariables();
    vars.declare("String", "M");
    vars.declare("String", "p");
    vars.declare("String", "q");
    vars.declare("String", "n");
    vars.declare("String", "phi");
    vars.declare("String", "e");
    vars.declare("String", "d");
    vars.declare("String", "c");
    vars.declare("String", "m");

    SourceCodeProperties legendeProps = new SourceCodeProperties();
    // legendeProps.set(AnimationPropertiesKeys.SIZE_PROPERTY, true);
    legendeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 15));
    SourceCode Beschreibung = lang.newSourceCode(new Coordinates(40, 50),
        "Beschreibung", null, legendeProps);

    if (stufe) {
      Beschreibung.addCodeLine(
          "RSA ist ein asymmetrisches Kryptosystem, das zur Verschluesselung",
          null, 0, null);
      Beschreibung
          .addCodeLine(
              "verwendet werden kann.Es verwendet ein Schluesselpaar, bestehend aus ",
              null, 0, null);
      Beschreibung
          .addCodeLine(
              "einem digitalen privaten Schluessel, der zum Entschluesseln von Daten verwendet wird, ",
              null, 0, null);
      Beschreibung
          .addCodeLine(
              "und einem digitalen oeffentlichen Schluessel, mit dem man einen Klartext verschluesselt. ",
              null, 0, null);
      Beschreibung
          .addCodeLine(
              "Der private Schluessel wird geheimgehalten und kann nur mit extrem hohem Aufwand  ",
              null, 0, null);
      Beschreibung
          .addCodeLine("aus dem oeffentlichen Schluessel berechnet werden.  ",
              null, 0, null);
      Beschreibung.addCodeLine(" ", null, 0, null);
      Beschreibung.addCodeLine("Begriffe ", null, 3, null);
      Beschreibung.addCodeLine(" ", null, 0, null);
      Beschreibung
          .addCodeLine(
              "Verschluesselung ist eine Funktion, die lesbare Daten in eine nur mit der Entschluesselung lesbare Form umwandelt",
              null, 0, null);
      Beschreibung.addCodeLine("Oeffentliche Schluessel : n und e,", null, 0,
          null);
      Beschreibung.addCodeLine("Privater Schluessel : d ", null, 0, null);
      Beschreibung
          .addCodeLine(
              "Nachricht : m  || Chriffrat : c, die Ausgabe, nachdem man die Nachricht mit verschluesselt",
              null, 0, null);
      Beschreibung.addCodeLine("Andere Zahlen : phi, p und q", null, 0, null);
    } else {
      Beschreibung
          .addCodeLine(
              "Da Sie schon Profi sind , koennen wir direkt mit der Berechnung anfangen.",
              null, 0, null);
      Beschreibung.addCodeLine(
          "Aber wir werden nur die Zahlen (Integer) verschluesseln.", null, 0,
          null);
      Beschreibung.addCodeLine("Nur als Erinnerung ,Hier sind die begriffe:",
          null, 0, null);
      Beschreibung.addCodeLine("", null, 0, null);
      Beschreibung.addCodeLine("Begriffe ", null, 3, null);
      Beschreibung.addCodeLine(" ", null, 0, null);
      Beschreibung.addCodeLine("Oeffentliche Schluessel : n und e ", null, 0,
          null);
      Beschreibung.addCodeLine("Privater Schluessel : d ", null, 0, null);
      Beschreibung
          .addCodeLine("Nachricht : m  || Chriffrat : c", null, 0, null);
      Beschreibung.addCodeLine("Andere Zahlen : phi, p und q", null, 0, null);
    }

    lang.nextStep();
    // start

    Beschreibung.hide();

    // now, create the source code entity
    SourceCode sc = lang.newSourceCode(new Coordinates(40, 50), "sourceCode",
        null, sourceCodeColor);

    // Add the lines to the SourceCode object.
    // Line, name, indentation, display dealy
    sc.addCodeLine("1. Waehle zwei grosse Primzahlen p,q", null, 0, null); // 0
    sc.addCodeLine("2. Setze n = p * q", null, 0, null); // 1
    sc.addCodeLine("3. Berechne: phi(n) = (p - 1) * (q - 1)", null, 0, null); // 2
    sc.addCodeLine("4. Waehle eine Primzahl e, mit:", null, 0, null); // 3
    sc.addCodeLine("gcd(e,phi(n)) = 1", null, 2, null); // 4
    sc.addCodeLine("5. Berechne d > 0, mit:", null, 0, null); // 5
    sc.addCodeLine("e * d = 1 (mod phi(n))", null, 4, null); // 6
    sc.addCodeLine("6. Verschluesselung:", null, 0, null); // 7
    sc.addCodeLine("c = m ^ e mod n", null, 4, null); // 8
    sc.addCodeLine("7. Entschluesselung", null, 0, null); // 9
    sc.addCodeLine("m = c ^ d mod n", null, 4, null); // 10

//    int w = 10;
//    String descr1 = "";
//    String descr2 = "";
    // / minus 20
    describtion1 = lang.newText(new Coordinates(50, 250),
        "M entspricht den Klartext,dass Sie am Anfang gegeben haben",
        "describtion1", null, textColor);
    describtion2 = lang.newText(new Coordinates(50, 270), " ", "describtion2",
        null, textColor);

    // TextProperties textColor = new TextProperties();
    // textColor.set(AnimationPropertiesKeys.CENTERED_PROPERTY, false);
    // textColor.set(AnimationPropertiesKeys.COLOR_PROPERTY, tableColor);
    // textColor.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    // "Monospaced", Font.PLAIN, 14));

    // TextProperties textColor = new TextProperties();
    // textColor.set(AnimationPropertiesKeys.COLOR_PROPERTY, resultColor);
    // textColor.set(AnimationPropertiesKeys.CENTERED_PROPERTY, false);
    // textColor.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    // "Monospaced", Font.PLAIN, 14));

    lang.newPolyline(new Node[] { new Coordinates(50, 305),
        new Coordinates(50, 530) }, "lineH0", null, polyline);
    lang.newPolyline(new Node[] { new Coordinates(80, 305),
        new Coordinates(80, 530) }, "lineH1", null, polyline);
    lang.newPolyline(new Node[] { new Coordinates(200, 305),
        new Coordinates(200, 530) }, "lineH2", null, polyline);

    lang.newPolyline(new Node[] { new Coordinates(50, 305),
        new Coordinates(200, 305) }, "lineV0", null, polyline);
    lang.newText(new Coordinates(60, 310), "M", "klartext", null, textColor);
    lang.newText(new Coordinates(90, 310), String.valueOf(m), "klartext", null,
        textColor);
    vars.set("M", String.valueOf(m));
    lang.newPolyline(new Node[] { new Coordinates(50, 330),
        new Coordinates(200, 330) }, "lineV0", null, polyline);
    lang.newText(new Coordinates(60, 335), "p", "pText", null, textColor);
    pErg = lang.newText(new Coordinates(90, 335), "", "pText", null, textColor);
    lang.newPolyline(new Node[] { new Coordinates(50, 355),
        new Coordinates(200, 355) }, "lineV1", null, polyline);
    lang.newText(new Coordinates(60, 360), "q", "qText", null, textColor);
    qErg = lang.newText(new Coordinates(90, 360), "", "qErg", null, textColor);
    lang.newPolyline(new Node[] { new Coordinates(50, 380),
        new Coordinates(200, 380) }, "lineV2", null, polyline);
    lang.newText(new Coordinates(60, 385), "n", "nText", null, textColor);
    nErg = lang.newText(new Coordinates(90, 385), "", "nErg", null, textColor);
    lang.newPolyline(new Node[] { new Coordinates(50, 405),
        new Coordinates(200, 405) }, "lineV3", null, polyline);
    lang.newText(new Coordinates(55, 410), "phi", "phiText", null, textColor);
    phiErg = lang.newText(new Coordinates(90, 410), "", "phiErg", null,
        textColor);
    lang.newPolyline(new Node[] { new Coordinates(50, 430),
        new Coordinates(200, 430) }, "lineV4", null, polyline);
    lang.newText(new Coordinates(60, 435), "e", "eText", null, textColor);
    eErg = lang.newText(new Coordinates(90, 435), "", "eErg", null, textColor);
    lang.newPolyline(new Node[] { new Coordinates(50, 455),
        new Coordinates(200, 455) }, "lineV5", null, polyline);
    lang.newText(new Coordinates(60, 460), "d", "dText", null, textColor);
    dErg = lang.newText(new Coordinates(90, 460), "", "dErg", null, textColor);
    lang.newPolyline(new Node[] { new Coordinates(50, 480),
        new Coordinates(200, 480) }, "lineV6", null, polyline);
    lang.newText(new Coordinates(60, 485), "c", "cText", null, textColor);
    cErg = lang.newText(new Coordinates(90, 485), "", "cErg", null, textColor);
    lang.newPolyline(new Node[] { new Coordinates(50, 505),
        new Coordinates(200, 505) }, "lineV7", null, polyline);
    lang.newText(new Coordinates(60, 510), "m", "mText", null, textColor);
    mErg = lang.newText(new Coordinates(90, 510), "", "mErg", null, textColor);
    lang.newPolyline(new Node[] { new Coordinates(50, 530),
        new Coordinates(200, 530) }, "lineV8", null, polyline);
    // text minus 20

    // Kalkul von "n"
    lang.nextStep();
    // Step 1

    sc.highlight(0);

    // if(stufe){
    describtion1.setText("Man waehlt hier zwei beliebigen unterschiedlichen",
        null, null);
    describtion2.setText("Primzahlen p und q", null, null);

    pErg.setText(String.valueOf(myP), null, null);
    qErg.setText(String.valueOf(myQ), null, null);
    vars.set("p", String.valueOf(myP));
    vars.set("q", String.valueOf(myQ));
    // System.out.println("p = " + p + " q = " + q);

    // Next Step Berechnung von PHI
    // if(stufe)
    lang.nextStep();
    // Step 2

    sc.unhighlight(0);
    sc.highlight(1);

    // if(stufe){
    describtion1.setText("Man berechnet n = " + myP + " * " + myQ, null, null);
    describtion2.setText("", null, null);

    n = myp * q;

    CheckpointUtils.checkpointEvent(this, "nEvent", new Variable("n", n));

    nErg.setText(String.valueOf(n), null, null);
    vars.set("n", String.valueOf(n));

    // Waehlen Exponenten e
    // if(stufe)
    lang.nextStep();
    // Step 3

    sc.unhighlight(1);
    sc.highlight(2);

    phi = (myP - 1) * (myQ - 1);

    CheckpointUtils.checkpointEvent(this, "phiEvent", new Variable("phi", phi));

    // if(stufe){
    describtion1.setText("Man berechnet die eulersche Funktion phi(" + n + ")",
        null, null);
    describtion2
        .setText("die hilft bei der Berechnung von e und d", null, null);

    phiErg.setText(String.valueOf(phi), null, null);
    vars.set("phi", String.valueOf(phi));

    // Waehlen Exponenten d
    // if(stufe)
    lang.nextStep();
    // Step 4

    sc.unhighlight(2);
    sc.highlight(3);
    sc.highlight(4);

    // if(stufe){
    describtion1.setText("Man waehlt eine beliebige Primzahl e und achtet",
        null, null);
    describtion2.setText("darauf, dass e und " + phi + " teilerfremd sind",
        null, null);

    // wenn kein gültiges e als Parameter angegeben wurde, bestimme es selbst!
    if (!eIsValid)
      e = getPublicKey(phi);

    CheckpointUtils.checkpointEvent(this, "eEvent", new Variable("e", e));

    eErg.setText(String.valueOf(e), null, null);// System.out.println("E = " +
                                                // e);
    vars.set("e", String.valueOf(e));

    // Präsentation der Werten
    // if(stufe)
    lang.nextStep();
    // Step 5

    sc.unhighlight(3);
    sc.unhighlight(4);
    sc.highlight(5);
    sc.highlight(6);

    // if(stufe){
    describtion1.setText("Oeffentliche Schluessel sind bereit: n = " + n
        + " und e = " + e, null, null);
    describtion2.setText("Jetze berechnet man d als inverse von e", null, null);

    // / Hier soll ich den text anpassen .
    d = (getPrivateKey(e, phi, stufe)) % phi;

    CheckpointUtils.checkpointEvent(this, "dEvent", new Variable("d", d));

    dErg.setText(String.valueOf(d), null, null);// System.out.println("D = " +
                                                // d);
    vars.set("d", String.valueOf(d));

    // Verschluesselung

    lang.nextStep();
    // Step 6

    sc.unhighlight(5);
    sc.unhighlight(6);
    sc.highlight(7);
    sc.highlight(8);

    describtion1
        .setText("Die Nachricht kann verschluesselt werden", null, null);
    describtion2.setText("c = " + m + " ^ " + e + " mod " + n, null, null);

    int c = exponantialFunct(m, e, n);

    CheckpointUtils.checkpointEvent(this, "cEvent", new Variable("c", c));

    cErg.setText(String.valueOf(c), null, null);// System.out.println("C = " +
                                                // c);
    vars.set("c", String.valueOf(c));

    // Entschluesselung
    // if(stufe)
    lang.nextStep();
    // Step 7

    sc.unhighlight(7);
    sc.unhighlight(8);

    // if(stufe){
    sc.highlight(9);
    sc.highlight(10);

    int mm = exponantialFunct(c, d, n);

    CheckpointUtils.checkpointEvent(this, "mEvent", new Variable("m", mm));

    // if(stufe){
    describtion1.setText("Die Nachricht wird wieder entschluesselt,", null,
        null);
    describtion2.setText("m = " + c + " ^ (" + d + ") mod " + n, null, null);

    mErg.setText(String.valueOf(mm), null, null);
    vars.set("m", String.valueOf(mm));

    // Fertig
    lang.nextStep();
    sc.unhighlight(9);
    sc.unhighlight(10);

    describtion1.setText("Am Ende vergleicht man seine Nachricht m und", null,
        null);
    describtion2.setText(" die ver-und-entschlluesselte M .setResult((" + m
        + " == " + mm + ")? true : true); // ;-)", null, null);

  }

  /**
	 * 
	 */
  public int getPublicKey(int phi) {
    // System.out.println("Public : phi = "+ phi);
    random = new SecureRandom();
    BigInteger e = new BigInteger("2");
    // Wahl von e sodass es kleiner als PhiN ist .
    BigInteger ggT = BigInteger.ZERO;
    for (; !ggT.equals(BigInteger.ONE);) {
      e = e.add(BigInteger.ONE);
      BigInteger tempP = new BigInteger(String.valueOf(phi)), tempE = e.abs(), q, r, s, t, u, v;
      u = BigInteger.ONE;
      t = BigInteger.ONE;
      v = BigInteger.ZERO;
      s = BigInteger.ZERO;
      while (tempE.compareTo(BigInteger.ZERO) == 1) {
        q = tempP.divide(tempE);
        r = tempP.subtract(q.multiply(tempE));
        tempP = tempE.abs();
        tempE = r.abs();
        r = u.subtract(q.multiply(s));
        u = s.abs();
        s = r.abs();
        r = v.subtract(q.multiply(t));
        v = t.abs();
        t = r.abs();
      }
      ggT = tempP.abs();
    }
    // System.out.println("323: phi = " + phi + " e = " + e);
    return e.intValue();
  }

  public int getPrivateKey(int e, int phi, boolean stufe) {
    if (stufe)
      return EuklidAlgo(phi, e);
    else
      return EuklidAlgoFuerPro(phi, e);

  }

  /**
   * Diese Funktion berechnet die Exponentialfunktion x^y mod n
   */
  public int exponantialFunct(int x, int y, int a) {
    BigInteger result;
    BigInteger xBig = new BigInteger(String.valueOf(x));
    BigInteger yBig = new BigInteger(String.valueOf(y));
    BigInteger aBig = new BigInteger(String.valueOf(a));
    result = xBig.modPow(yBig, aBig);
    // System.out.println(x + " ^ " + y + " mod " + a + " = " +
    // result.intValue());
    return result.intValue();
  }

  /**
   * 
   * Diese Funktion berechnet den gr&ouml;&szlig;ten gemeinsamen Teiler von a
   * und b und speichert das Ergebnis d ab. Zus&auml;tzlich werden die Variablen
   * u und v der Gleichung ax + by = d mit Werten belegt. Ausgabe : A
   */

  public int EuklidAlgo(int aa, int bb) {
    // SourceCodeProperties euklProps = new SourceCodeProperties();

    // SourceCode EukligAlgo = lang.newSourceCode(new Coordinates(600,10),
    // "EukligAlgo", null,euklProps);
    // TextProperties ergProps = new TextProperties();
    // int tableX = 65, tableY = 270;

    // SourceCodeProperties SourceCodeColor = new SourceCodeProperties();
    // SourceCodeColor.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
    // Color.RED);
    // SourceCodeColor.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    // "Monospaced", Font.PLAIN, 14));
    SourceCode EukligAlgo = lang.newSourceCode(new Coordinates(600, 10),
        "EukligAlgo", null, sourceCodeColor);
    EukligAlgo.addCodeLine("Pseudo Code Erweiterter Euklidischer Algorithmus",
        null, 0, null);
    EukligAlgo.addCodeLine("Input : a=phi(n), b=Oeffentlicher Schluessel",
        null, 0, null);
    EukligAlgo.addCodeLine("Output: g = gcd(a,b) and private key d", null, 0,
        null);
    EukligAlgo.addCodeLine("", null, 1, null);
    EukligAlgo.addCodeLine("x2 = 1; x1 = 0; y2 = 0; y1 = 1;", null, 1, null);
    EukligAlgo.addCodeLine("while b > 0", null, 1, null);
    EukligAlgo.addCodeLine(
        "q = a div b; r = a mod b; x = x2 - qx1; y = y2 - qy1;", null, 2, null);
    EukligAlgo.addCodeLine("a = b; b = r; x2 = x1; x1 = x; y2 = y1; y1 = y;",
        null, 2, null);
    EukligAlgo.addCodeLine("end while", null, 0, null);
    EukligAlgo.addCodeLine("d = a, x = x2, y = (y1 < 0)? y2 += phi(n) : y2;",
        null, 0, null);

    EukligAlgo.highlight(0);
    lang.nextStep();

    // TextProperties textColor = new TextProperties();
    // textColor.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    // textColor.set(AnimationPropertiesKeys.CENTERED_PROPERTY, false);
    // textColor.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    // "Monospaced", Font.PLAIN, 14));
    int tableX = 65, tableY = 270;

    lang.newPolyline(new Node[] { new Coordinates(600, tableY),
        new Coordinates(600 + tableX * 10, tableY) }, "lineH0", null, polyline);
    lang.newText(new Coordinates(600 + tableX / 2, tableY), "q", "q", null,
        textColor);
    lang.newText(new Coordinates(600 + tableX * 1 + tableX / 2, tableY), "r",
        "r", null, textColor);
    lang.newText(new Coordinates(600 + tableX * 2 + tableX / 2, tableY), "x",
        "x", null, textColor);
    lang.newText(new Coordinates(600 + tableX * 3 + tableX / 2, tableY), "y",
        "y", null, textColor);
    lang.newText(new Coordinates(600 + tableX * 4 + tableX / 2, tableY), "a",
        "a", null, textColor);
    lang.newText(new Coordinates(600 + tableX * 5 + tableX / 2, tableY), "b",
        "b", null, textColor);
    lang.newText(new Coordinates(600 + tableX * 6 + tableX / 2, tableY), "x2",
        "x2", null, textColor);
    lang.newText(new Coordinates(600 + tableX * 7 + tableX / 2, tableY), "x1",
        "x1", null, textColor);
    lang.newText(new Coordinates(600 + tableX * 8 + tableX / 2, tableY), "y2",
        "y2", null, textColor);
    lang.newText(new Coordinates(600 + tableX * 9 + tableX / 2, tableY), "y1",
        "y1", null, textColor);
    lang.newPolyline(new Node[] { new Coordinates(600, tableY + 25),
        new Coordinates(600 + tableX * 10, tableY + 25) }, "lineH1", null,
        polyline);

    // System.out.println("euk(" + aa + " , " + bb + " )");
    int a = aa, b = bb, x1, x2, y2, y1, q, r, x, y, row = 1;
    if (b == 0) {
      d = a;
      x = 1;
      y = 0;
    } else {

      x2 = 1;
      x1 = 0;
      y2 = 0;
      y1 = 1;
      while (b > 0) {
        if (row == 1) {
          EukligAlgo.highlight(1);
          lang.newText(new Coordinates(600 + tableX * 4 + tableX / 2, tableY
              + row * 25), String.valueOf(a), "a" + row, null, textColor);
          lang.newText(new Coordinates(600 + tableX * 5 + tableX / 2, tableY
              + row * 25), String.valueOf(b), "b" + row, null, textColor);
          lang.nextStep();
          EukligAlgo.unhighlight(1);
          EukligAlgo.highlight(4);
          lang.newText(new Coordinates(600 + tableX * 6 + tableX / 2, tableY
              + row * 25), String.valueOf(x2), "x2" + row, null, textColor);
          lang.newText(new Coordinates(600 + tableX * 7 + tableX / 2, tableY
              + row * 25), String.valueOf(x1), "x1" + row, null, textColor);
          lang.newText(new Coordinates(600 + tableX * 8 + tableX / 2, tableY
              + row * 25), String.valueOf(y2), "y2" + row, null, textColor);
          lang.newText(new Coordinates(600 + tableX * 9 + tableX / 2, tableY
              + row * 25), String.valueOf(y1), "y1" + row, null, textColor);
          lang.nextStep();
          EukligAlgo.unhighlight(4);
        } else {
          lang.newText(new Coordinates(600 + tableX * 4 + tableX / 2, tableY
              + row * 25), String.valueOf(a), "a" + row, null, textColor);
          lang.newText(new Coordinates(600 + tableX * 5 + tableX / 2, tableY
              + row * 25), String.valueOf(b), "b" + row, null, textColor);
          lang.newText(new Coordinates(600 + tableX * 6 + tableX / 2, tableY
              + row * 25), String.valueOf(x2), "x2" + row, null, textColor);
          lang.newText(new Coordinates(600 + tableX * 7 + tableX / 2, tableY
              + row * 25), String.valueOf(x1), "x1" + row, null, textColor);
          lang.newText(new Coordinates(600 + tableX * 8 + tableX / 2, tableY
              + row * 25), String.valueOf(y2), "y2" + row, null, textColor);
          lang.newText(new Coordinates(600 + tableX * 9 + tableX / 2, tableY
              + row * 25), String.valueOf(y1), "y1" + row, null, textColor);
        }
        if (row == 1) {
          EukligAlgo.highlight(5);
          EukligAlgo.highlight(6);
          EukligAlgo.highlight(7);
          EukligAlgo.highlight(8);
        }

        q = a / b;
        r = a % b;
        x = x2 - q * x1;
        y = y2 - q * y1;
        a = b;
        b = r;
        x2 = x1;
        x1 = x;
        y2 = y1;
        y1 = y;

        lang.newText(new Coordinates(600 + tableX / 2, tableY + row * 25),
            String.valueOf(q), "q" + row, null, textColor);
        lang.newText(new Coordinates(600 + tableX * 1 + tableX / 2, tableY
            + row * 25), String.valueOf(r), "r" + row, null, textColor);
        lang.newText(new Coordinates(600 + tableX * 2 + tableX / 2, tableY
            + row * 25), String.valueOf(x), "x" + row, null, textColor);
        lang.newText(new Coordinates(600 + tableX * 3 + tableX / 2, tableY
            + row * 25), String.valueOf(y), "y" + row, null, textColor);
        lang.nextStep();
        row++;

      }

      EukligAlgo.unhighlight(5);
      EukligAlgo.unhighlight(6);
      EukligAlgo.unhighlight(7);
      EukligAlgo.unhighlight(8);
      EukligAlgo.highlight(9);

      d = a;
      x = x2;
      y = (y2 < 0) ? y2 + phi : y2;

      lang.nextStep();
      EukligAlgo.unhighlight(9);
      EukligAlgo.unhighlight(0);

    }
    // System.out.println("eukalgo");
    // System.out.println(aa + " * " + x + " + " + bb + " * " + y + " = " + d);
    return y;
  }

  public int EuklidAlgoFuerPro(int aa, int bb) {
    // System.out.println("euk(" + aa + " , " + bb + " )");
    int a = aa, b = bb, x1, x2, y2, y1, q, r, x, y;
    if (b == 0) {
      d = a;
      x = 1;
      y = 0;
    } else {
      x2 = 1;
      x1 = 0;
      y2 = 0;
      y1 = 1;
      while (b > 0) {
        q = a / b;
        r = a % b;
        x = x2 - q * x1;
        y = y2 - q * y1;
        a = b;
        b = r;
        x2 = x1;
        x1 = x;
        y2 = y1;
        y1 = y;
      }
      d = a;
      x = x2;
      y = (y2 < 0) ? y2 + phi : y2;
    }
    // System.out.println("eukalgo");
    // System.out.println(aa + " * " + x + " + " + bb + " * " + y + " = " + d);
    return y;
  }

}