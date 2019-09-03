package generators.cryptography.elgamal;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.variables.IntegerVariable;

public class ElGamalVerfahren implements Generator {
  private Language             lang;
  private TextProperties       Titel;
  private int                  a;
  private int                  b;
  private int                  h;
  private int                  M;
  private int                  B;
  private int                  c;
  private int                  x;
  private int                  Me;
  private SourceCodeProperties Erklaerungen;
  private SourceCodeProperties Quellcode;
  private TextProperties       Kapitelueberschriften;

  public void init() {
    lang = new AnimalScript("ElGamal Verfahren",
        "Emre Cakmak, Franklin Labang", 800, 600);
    lang.setStepMode(true);

    final TextProperties Titel = new TextProperties();
    Titel.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));

    final TextProperties Kapitelueberschriften = new TextProperties();
    Kapitelueberschriften.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 20));

    final SourceCodeProperties Erklaerungen = new SourceCodeProperties();
    Erklaerungen.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    Erklaerungen.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 16));
    Erklaerungen
        .set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    Erklaerungen.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    final SourceCodeProperties Quellcode = new SourceCodeProperties();
    Quellcode.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    Quellcode.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 12));
    Quellcode.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
    Quellcode.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
  }

  public void elgamal() {

    Titel.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));
    Kapitelueberschriften.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 20));
    Erklaerungen.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 16));
    Quellcode.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 12));

    RectProperties rectProps1 = new RectProperties();
    rectProps1.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    rectProps1.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps1.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);

    // step 1
    Text title = lang.newText(new Coordinates(400, 30),
        "ElGamal Ver-Entschluesselung", "title", null, Titel);
    lang.newRect(new Offset(-10, -10, title, AnimalScript.DIRECTION_NW),
        new Offset(10, 10, title, AnimalScript.DIRECTION_SE), "Rechteck", null,
        rectProps1);

    // ///// HIER VARIABLEN INITIALISIEREN
    int minuseins = -1;

    B = (int) (Math.pow(3, b)) % 17;
    h = (int) (Math.pow(3, a)) % 17;
    c = (int) ((Math.pow(h, b)) * M) % 17;
    x = 17 - 1 - a;
    // Me = (int) (Math.pow(Math.pow(B, a), minuseins)*c) % 17;
    Me = (int) ((Math.pow(B, x)) * c) % 17;

    System.out.println((Math.pow(Math.pow(5, 6), minuseins)));

    IntegerVariable aintvar = new IntegerVariable(this.a);
    IntegerVariable bintvar = new IntegerVariable(this.b);
    IntegerVariable Mintvar = new IntegerVariable(this.M);
    IntegerVariable hintvar = new IntegerVariable(this.h);
    IntegerVariable Bintvar = new IntegerVariable(this.B);
    IntegerVariable cintvar = new IntegerVariable(this.c);
    IntegerVariable xintvar = new IntegerVariable(this.x);
    IntegerVariable Meintvar = new IntegerVariable((int) this.Me);

    // ///////////////////////////////

    lang.nextStep();

    // step 2
    SourceCode Einleitung = lang.newSourceCode(new Coordinates(20, 100),
        "Einleitung", null, Erklaerungen);

    // Line, name, indentation, display dealy
    Einleitung
        .addCodeLine(
            "Der ElGamal-Algorithmus ist eine Variation des Diffie-Hellman-Algorithmus .Sein Erfinder Taher ElGamal veroeffentlichte es 1985.",
            null, 0, null);
    Einleitung
        .addCodeLine(
            "Es gehoert, wie zum Beispiel auch das weitausbekannterer RSA-Verfahren, zu den sogenannten asymmetrischen Verfahren.",
            null, 0, null);
    Einleitung.addCodeLine("   																		", null, 0, null);
    Einleitung
        .addCodeLine(
            "Es beruht auf der Grundlage, dass Sender und Empfaenger jeweils zwei Schluessel haben. ",
            null, 0, null);
    Einleitung
        .addCodeLine(
            "Der sogenannte oeffentliche Schluessel (fuer die Verschluesselung) ist jedem zugaenglich,",
            null, 0, null);
    Einleitung
        .addCodeLine(
            "und somit auch jedem bekannt,der private Schluessel (fuer die Entschluesselung) ist nur dem Empfaenger bekannt.",
            null, 0, null);
    Einleitung.addCodeLine("   																		", null, 0, null);
    Einleitung.addCodeLine(
        "In der Praxis findet das ElGamal-Algorithmus viele Anwendung.", null,
        0, null);
    Einleitung
        .addCodeLine(
            "Es dient zum Beispiel der Verschluesselung, zum Schluesselaustausch und zur digitalen / elektronischen Signatur.",
            null, 0, null);

    String Einlei = "Einleitung";
    lang.nextStep(Einlei);

    // step 3

    Einleitung.hide(new TicksTiming(1));
    Text tA = lang
        .newText(new Coordinates(20, 110),
            "Algorithmus in einzelnen Schritten", "tA", null,
            Kapitelueberschriften);

    SourceCode Algo = lang.newSourceCode(new Coordinates(40, 130), "Algo",
        null, Erklaerungen);

    Algo.addCodeLine("1.) Waehle eine Zyklische Gruppe G mit generator g,",
        null, 0, null);
    Algo.addCodeLine("    und einen zuffaelligen Exponent a ,", null, 0, null);
    Algo.addCodeLine("												", null, 0, null);
    Algo.addCodeLine("2.) Setze h = g^a", null, 0, null);
    Algo.addCodeLine("    Oeffentlicher Schluessel g und h.", null, 0, null);
    Algo.addCodeLine("    privater Schluessel a.", null, 0, null);
    Algo.addCodeLine("												", null, 0, null);
    Algo.addCodeLine("3.)  Verschluesselung der Nachricht m", null, 0, null);
    Algo.addCodeLine(" 			 E(m) =(c1,c2):=(g^k,mh^k) ", null, 2, null);
    Algo.addCodeLine(" 			 wobei ein Exponent k zufaellig gewaehlt wird ",
        null, 2, null);
    Algo.addCodeLine("												", null, 0, null);
    Algo.addCodeLine("4.) Entschluesselung der Nachricht m", null, 0, null);
    Algo.addCodeLine("	  D(c1,c2)=c2(c1)^-a ", null, 2, null);
    Algo.addCodeLine("											", null, 0, null);
    Algo.addCodeLine("Bemerkung:", null, 0, null);
    Algo.addCodeLine(
        "	D ist die Entschluesselungsfunktion und E die Verschluesselungsfunktion  ",
        null, 2, null);
    lang.nextStep();

    // step 4

    Algo.hide(new TicksTiming(1));
    tA.hide(new TicksTiming(1));
    Algo.highlight(0);
    Algo.highlight(1);
    Text t7 = lang.newText(new Coordinates(40, 110),
        "Generator eines zyklischen Gruppe bestimmen", "t7", null,
        Kapitelueberschriften);

    SourceCode AlgoPseudo = lang.newSourceCode(new Coordinates(32, 130),
        "AlgoPseudo", null, Erklaerungen);

    AlgoPseudo
        .addCodeLine(
            "Hier nun ein festes Beispiel, wie der Generator g bestimmt werden kann.",
            null, 0, null);
    AlgoPseudo.addCodeLine("										", null, 0, null);
    AlgoPseudo.addCodeLine("Pseudo Code zum bestimmen des generator:", null, 0,
        null);
    AlgoPseudo
        .addCodeLine(
            "Sei g der zu bestimmende Genrator, p=17 und Z17 die Zyglische Gruppe.",
            null, 0, null);
    AlgoPseudo.addCodeLine("											 ", null, 0, null);
    AlgoPseudo.addCodeLine("for(int a=0; a<=p-1; a++) ", null, 0, null);
    AlgoPseudo.addCodeLine("										", null, 0, null);
    AlgoPseudo.addCodeLine("   for(int g=2; a<=p-1; a++)									 ", null, 0,
        null);
    AlgoPseudo.addCodeLine("										", null, 0, null);
    AlgoPseudo
        .addCodeLine(
            "pruefe ob g^a mod n uenterschieldliche Werte erzeugt fuer alle a aus(1...p-1)",
            null, 0, null);
    AlgoPseudo.addCodeLine("                                    ", null, 0,
        null);
    AlgoPseudo.addCodeLine(
        "Wenn ja dann ist g der Genrator und breche ab										 ", null, 0,
        null);
    AlgoPseudo.addCodeLine("                                    ", null, 0,
        null);
    AlgoPseudo
        .addCodeLine(
            "wenn nein dann gibt es kein Generator und somit ist die Gruppe nicht zyklisch",
            null, 0, null);

    String Init = "Initialisierung";
    lang.nextStep(Init);

    // Step 5
    AlgoPseudo.hide(new TicksTiming(1));
    t7.hide(new TicksTiming(1));
    Text t10 = lang.newText(new Coordinates(40, 110),
        "Generator eines zyklischen Gruppe bestimmen", "t10", null,
        Kapitelueberschriften);

    SourceCode AlgoRechnung = lang.newSourceCode(new Coordinates(32, 130),
        "AlgoPseudo", null, Erklaerungen);

    AlgoRechnung.addCodeLine("2^0 mod 17=1   erhoehe a um 1", null, 0, null);
    AlgoRechnung.addCodeLine("2^1 mod 17=2   erhoehe a um 1", null, 0, null);
    AlgoRechnung.addCodeLine("2^2 mod 17=4   erhoehe a um 1", null, 0, null);
    AlgoRechnung.addCodeLine("2^3 mod 17=8   erhoehe a um 1", null, 0, null);
    AlgoRechnung.addCodeLine("2^4 mod 17=16  erhoehe a um 1", null, 0, null);
    AlgoRechnung.addCodeLine("2^5 mod 17=15  erhoehe a um 1", null, 0, null);
    AlgoRechnung.addCodeLine("2^6 mod 17=13  erhoehe a um 1", null, 0, null);
    AlgoRechnung.addCodeLine("2^7 mod 17=9   erhoehe a um 1", null, 0, null);
    AlgoRechnung.addCodeLine(
        "2^8 mod 17=1    Abbruch da die 1 schon gekommen ist", null, 0, null);
    AlgoRechnung.addCodeLine("erhoehe jetzt g um 1 und mache weiter", null, 0,
        null);

    lang.nextStep();

    AlgoRechnung.highlight(0);
    AlgoRechnung.highlight(8);

    lang.nextStep();

    // Step 5
    AlgoRechnung.unhighlight(8);
    // AlgoRechnung.unhighlight(15);
    t10.hide(new TicksTiming(1));
    AlgoRechnung.hide(new TicksTiming(1));
    Text t8 = lang.newText(new Coordinates(40, 110),
        "Generator eines zyklischen Gruppe bestimmen Fortsetzung", "t8", null,
        Kapitelueberschriften);

    SourceCode algoweiter = lang.newSourceCode(new Coordinates(32, 130),
        "algoweiter", null, Erklaerungen);

    algoweiter.addCodeLine("3^0 mod 17=1   erhoehe a um 1", null, 0, null);
    algoweiter.addCodeLine("3^1 mod 17=3   erhoehe a um 1", null, 0, null);
    algoweiter.addCodeLine("3^2 mod 17=9   erhoehe a um 1", null, 0, null);
    algoweiter.addCodeLine("3^3 mod 17=10  erhoehe a um 1", null, 0, null);
    algoweiter.addCodeLine("3^4 mod 17=13  erhoehe a um 1", null, 0, null);
    algoweiter.addCodeLine("3^5 mod 17=5   erhoehe a um 1", null, 0, null);
    algoweiter.addCodeLine("3^6 mod 17=15  erhoehe a um 1", null, 0, null);
    algoweiter.addCodeLine("3^7 mod 17=11  erhoehe a um 1", null, 0, null);
    algoweiter.addCodeLine("3^8 mod 17=16  erhoehe a um 1", null, 0, null);
    algoweiter.addCodeLine("3^9 mod 17=14  erhoehe a um 1", null, 0, null);
    algoweiter.addCodeLine("3^10 mod 17=8  erhoehe a um 1", null, 0, null);
    algoweiter.addCodeLine("3^11 mod 17=7  erhoehe a um 1", null, 0, null);
    algoweiter.addCodeLine("3^12 mod 17=4  erhoehe a um 1", null, 0, null);
    algoweiter.addCodeLine("3^13 mod 17=12  erhoehe a um 1", null, 0, null);
    algoweiter.addCodeLine("3^14 mod 17=2  erhoehe a um 1", null, 0, null);
    algoweiter.addCodeLine("3^15 mod 17=6  erhoehe a um 1", null, 0, null);
    algoweiter.addCodeLine(
        "3^16 mod 17=1  =>  Abbruch somit ist 3 der Genarator", null, 0, null);

    lang.nextStep();

    algoweiter.highlight(16);

    lang.nextStep();

    // Step 5
    algoweiter.unhighlight(16);
    t8.hide(new TicksTiming(1));
    algoweiter.hide(new TicksTiming(1));
    Text t9 = lang.newText(new Coordinates(40, 110), "Anmerkungen", "t9", null,
        Kapitelueberschriften);

    SourceCode Anmerkungen = lang.newSourceCode(new Coordinates(32, 130),
        "Anmerkungen", null, Erklaerungen);

    Anmerkungen
        .addCodeLine(
            "Es gibt dennoch andere Verfahren um das Generator einer Gruppe zu bestimmen.",
            null, 0, null);
    Anmerkungen.addCodeLine(
        "Eine gaengige Loesung ist zum Beispiel die Primfaktorzerlegung.",
        null, 0, null);
    Anmerkungen.addCodeLine(
        "Man zerlegt p in seinen Primfaktoren und und tut das selbe wie oben.",
        null, 0, null);
    Anmerkungen
        .addCodeLine(
            "Also g^a mod p aber in dem Fall ist a element von den Primfaktoren von p",
            null, 0, null);
    Anmerkungen
        .addCodeLine("Vorteil ist dass es effizient ist.", null, 0, null);
    Anmerkungen
        .addCodeLine(
            "Es muss nur wenige Schritte gemacht werden um das generator zu bestimmen.",
            null, 0, null);

    lang.nextStep();

    algoweiter.unhighlight(15);
    Anmerkungen.hide(new TicksTiming(1));
    t9.hide(new TicksTiming(1));

    Text t11 = lang.newText(new Coordinates(40, 60), "JavaCode", "t11", null,
        Kapitelueberschriften);

    SourceCode JavaCode = lang.newSourceCode(new Coordinates(32, 80),
        "JavaCode", null, Quellcode);

    JavaCode.addCodeLine("import java.math.*;", null, 0, null);
    JavaCode.addCodeLine("import java.util.*;", null, 0, null);
    JavaCode.addCodeLine("import java.security.*;", null, 0, null);
    JavaCode.addCodeLine("import java.io.*;", null, 0, null);
    JavaCode.addCodeLine("public class ElGamal{", null, 0, null);
    JavaCode.addCodeLine(
        "public static void main(String[] args) throws IOException {", null, 0,
        null);
    JavaCode.addCodeLine("BigInteger p, g, h, a;", null, 0, null);
    JavaCode.addCodeLine("Random sc = new SecureRandom();", null, 0, null);
    JavaCode.addCodeLine("a = new BigInteger('12345678901234567890');", null,
        0, null);
    JavaCode.addCodeLine("										", null, 0, null);
    JavaCode.addCodeLine("// public key calculation", null, 0, null);
    JavaCode
        .addCodeLine("p = BigInteger.probablePrime(64, sc);", null, 0, null);
    JavaCode.addCodeLine("g = new BigInteger(\"3\");", null, 0, null);
    JavaCode.addCodeLine("h = g.modPow(a, p);", null, 0, null);
    JavaCode.addCodeLine("										", null, 0, null);
    JavaCode.addCodeLine("// Encryption", null, 0, null);
    JavaCode.addCodeLine("String s = Tools.getString();", null, 0, null);
    JavaCode.addCodeLine(
        "BigInteger M = new BigInteger(s) // Wobei m die Message ist die;",
        null, 0, null);
    JavaCode.addCodeLine("BigInteger b = new BigInteger(64, sc);", null, 0,
        null);
    JavaCode.addCodeLine("BigInteger B = g.modPow(b, p);", null, 0, null);
    JavaCode.addCodeLine("BigInteger c = M.multiply(g.modPow(b, p)).mod(p);",
        null, 0, null);
    JavaCode.addCodeLine("", null, 0, null);
    JavaCode.addCodeLine("// Decryption", null, 0, null);
    JavaCode.addCodeLine("BigInteger M = ((B.mod(a)).mod(-1))*c;", null, 0,
        null);
    JavaCode.addCodeLine("}", null, 0, null);
    JavaCode.addCodeLine("   }", null, 0, null);

    String Code = "Code";
    lang.nextStep(Code);

    // Step 5
    Text t1 = lang.newText(new Coordinates(680, 110), "Schluesselerzeugung",
        "t10", null, Kapitelueberschriften);

    SourceCode Schluessel = lang.newSourceCode(new Coordinates(700, 130),
        "Schluessel", null, Erklaerungen);

    Schluessel.addCodeLine(
        "a.) Finden einer sicheren grossen Primzahl p ueber p=2q+1 wobei,",
        null, 0, null);
    Schluessel.addCodeLine(" q ebensfalls eine grosse Primzahl ist ", null, 0,
        null);
    Schluessel.addCodeLine("								  ", null, 0, null);
    Schluessel
        .addCodeLine(
            "=> Als Beispiel waehlen wir die Gruppe, die durch p = 17 und g = 3 beschrieben ist.",
            null, 0, null);
    Schluessel
        .addCodeLine(
            "Random sc= new SecureRandom() ist nur eine Zufallszahl,die nicht berechnet werden kann",
            null, 0, null);
    Schluessel
        .addCodeLine(
            "Das gibt die Anzahl der Bits die p haben sollte, (in unserem Fall 64 Bits) damit es sicher ist.",
            null, 0, null);
    Schluessel.addCodeLine("								  ", null, 0, null);
    Schluessel.addCodeLine("b.) Erzeugen einer primitiven Wurzel g, wobei ",
        null, 0, null);
    Schluessel
        .addCodeLine(
            "    g der multiplikativen Gruppe Z*p angehoeren muss sowie g^q mod p = -1 mod p.",
            null, 0, null);
    Schluessel.addCodeLine("								  ", null, 0, null);
    Schluessel.addCodeLine("=> Als geheimer Schluessel sei a = " + aintvar
        + " gewaehlt. Man berechnet h := g^a => 3^" + aintvar + " ≡ " + hintvar
        + " mod 17.", null, 0, null);
    Schluessel.addCodeLine("								  ", null, 0, null);
    Schluessel.addCodeLine(
        "Der oeffentliche Schluessel ist: p, g, h und der Der private ist a",
        null, 0, null);
    Schluessel.addCodeLine(
        "                                                           ", null, 0,
        null);
    Schluessel.addCodeLine(
        "=> Somit ergibt der oeffentliche Schluessel (p = 17, g = 3, h = "
            + hintvar + ")", null, 0, null);
    Schluessel.addCodeLine("   und der geheime Schluessel a = " + aintvar,
        null, 0, null);
    Schluessel.addCodeLine("								", null, 0, null);
    Schluessel.addCodeLine("HINWEIS:", null, 0, null);
    Schluessel
        .addCodeLine(
            "Die Wahl einer nicht-zyklische Gruppe koennte dann ein Problem sein,.",
            null, 0, null);
    Schluessel.addCodeLine("da es kein Generator geben wuerde.", null, 0, null);
    lang.nextStep();

    // step 6

    Algo.unhighlight(0);
    Schluessel.unhighlight(0);
    Schluessel.unhighlight(1);
    Schluessel.highlight(2);
    Schluessel.highlight(3);
    Schluessel.highlight(4);
    Schluessel.highlight(5);
    JavaCode.highlight(6);
    JavaCode.highlight(7);

    lang.nextStep();

    // Step 7
    Algo.unhighlight(0);
    Algo.unhighlight(1);
    Schluessel.unhighlight(2);
    Schluessel.unhighlight(3);
    Schluessel.unhighlight(4);
    Schluessel.unhighlight(5);
    Schluessel.highlight(7);
    Schluessel.highlight(8);
    JavaCode.highlight(6);
    lang.nextStep();

    // step 8
    JavaCode.unhighlight(6);
    JavaCode.unhighlight(7);
    JavaCode.highlight(10);
    JavaCode.highlight(11);
    JavaCode.highlight(12);
    Schluessel.unhighlight(5);
    Schluessel.unhighlight(6);
    Schluessel.unhighlight(7);
    Schluessel.unhighlight(8);
    Schluessel.highlight(9);
    Schluessel.highlight(14);
    Schluessel.highlight(15);
    Schluessel.highlight(10);
    Schluessel.highlight(12);
    lang.nextStep();

    // step 9
    JavaCode.unhighlight(10);
    JavaCode.unhighlight(11);
    JavaCode.unhighlight(12);
    Schluessel.unhighlight(9);
    Schluessel.unhighlight(14);
    Schluessel.unhighlight(10);
    Schluessel.unhighlight(12);
    Schluessel.unhighlight(15);
    Schluessel.highlight(17);
    Schluessel.highlight(18);
    Schluessel.highlight(19);
    lang.nextStep();

    // Step 10
    JavaCode.unhighlight(10);
    JavaCode.unhighlight(11);
    JavaCode.highlight(13);
    Schluessel.unhighlight(6);
    Schluessel.unhighlight(7);
    Schluessel.unhighlight(8);
    Schluessel.unhighlight(9);
    Schluessel.hide(new TicksTiming(1));
    t1.hide(new TicksTiming(1));
    Text t3 = lang.newText(new Coordinates(680, 110), "Verschluesselung",
        "t10", null, Kapitelueberschriften);

    SourceCode Verschl = lang.newSourceCode(new Coordinates(700, 130),
        "Verschl", null, Erklaerungen);

    Verschl.addCodeLine(
        "Die Verschluesselung erfolgt nun mit dem oeffentlichen Schluessel",
        null, 0, null);
    Verschl
        .addCodeLine("a)  benoetigt wird der oeffentliche Schluessel p, g, h",
            null, 0, null);
    Verschl.addCodeLine("   ", null, 0, null);
    Verschl.addCodeLine("=> Um nun eine Nachricht M = " + Mintvar
        + " zu verschluesseln", null, 0, null);
    Verschl
        .addCodeLine(
            "(Der zu verschluesselnde Klartext M kann beliebig zwischen 0 und p-1 sein.),",
            null, 0, null);
    Verschl.addCodeLine(
        " wird zuerst ein zufaelliger Exponent bestimmt, beispielsweise b = "
            + bintvar, null, 0, null);
    Verschl.addCodeLine("", null, 0, null);
    Verschl.addCodeLine(
        "b)  M wird als Zahl im Bereich {0, 1, ..., p - 1} dargestellt", null,
        0, null);
    Verschl.addCodeLine("", null, 0, null);
    Verschl.addCodeLine("c) finden einer Zahl b aus der Menge {1, ..., p - 2}",
        null, 0, null);
    Verschl.addCodeLine("", null, 0, null);
    Verschl.addCodeLine("d)  berechnen von B = g^b mod p und c = h^b*M mod p",
        null, 0, null);
    Verschl.addCodeLine("     die verschluesselte Nachricht ist C =(B, c)",
        null, 0, null);
    Verschl.addCodeLine("", null, 0, null);
    Verschl.addCodeLine("=> Damit berechnet sich B := g^b => 3^" + bintvar
        + " ≡ " + Bintvar + " mod 17 und ", null, 0, null);
    Verschl.addCodeLine("", null, 0, null);
    Verschl.addCodeLine("c := h^b*M => " + hintvar + "^" + bintvar + " * "
        + Mintvar + " ≡ " + cintvar + " mod 17", null, 0, null);
    Verschl.addCodeLine("Somit ergibt sich der Geheimtext C = (B = " + Bintvar
        + ", c = " + cintvar + ").", null, 0, null);
    lang.nextStep();

    // Step 11
    JavaCode.highlight(13);
    Verschl.unhighlight(0);
    Verschl.unhighlight(1);
    Verschl.unhighlight(2);
    Verschl.highlight(3);
    Verschl.highlight(4);
    Verschl.highlight(5);
    lang.nextStep();

    // step 12
    JavaCode.unhighlight(13);
    JavaCode.highlight(14);
    JavaCode.highlight(15);
    JavaCode.highlight(16);
    Verschl.unhighlight(3);
    Verschl.unhighlight(4);
    Verschl.unhighlight(5);
    Verschl.highlight(7);
    lang.nextStep();

    // step 13
    JavaCode.unhighlight(14);
    JavaCode.unhighlight(15);
    JavaCode.unhighlight(16);
    JavaCode.highlight(17);
    Verschl.unhighlight(7);
    Verschl.highlight(9);
    lang.nextStep();

    // step 13
    JavaCode.unhighlight(17);
    JavaCode.highlight(18);
    JavaCode.highlight(19);
    Verschl.unhighlight(9);
    Verschl.highlight(11);
    Verschl.highlight(12);
    Verschl.highlight(13);
    Verschl.highlight(14);
    Verschl.highlight(16);
    Verschl.highlight(17);
    lang.nextStep();

    // step 14
    JavaCode.unhighlight(18);
    JavaCode.unhighlight(19);
    JavaCode.highlight(21);
    JavaCode.highlight(22);
    Verschl.unhighlight(11);
    Verschl.unhighlight(12);
    Verschl.unhighlight(13);
    Verschl.unhighlight(14);
    Verschl.unhighlight(16);
    Verschl.unhighlight(17);
    Verschl.hide(new TicksTiming(1));
    t3.hide(new TicksTiming(1));
    Text t4 = lang.newText(new Coordinates(680, 110), "Entschluesselung", "t4",
        null, Kapitelueberschriften);

    SourceCode Entschl = lang.newSourceCode(new Coordinates(700, 130),
        "Entschl", null, Erklaerungen);
    Entschl.addCodeLine("a) berechnen von M = (B^x)*c mod p", null, 0, null);
    Entschl.addCodeLine("Wobei x= 17-1-" + aintvar, null, 0, null);
    Entschl.addCodeLine("					", null, 0, null);
    Entschl.addCodeLine(
        "Der zuvor durch Verschlüsselung entstandene Geheimtext 					", null,
        0, null);
    Entschl
        .addCodeLine(
            "wird unter Verwendung des geheimen Schlüssels wieder in den Klartext gewandelt.					",
            null, 0, null);
    Entschl.addCodeLine("					", null, 0, null);
    Entschl.addCodeLine(" => M = (" + Bintvar + "^" + xintvar + ")*" + cintvar
        + " mod 17", null, 0, null);
    Entschl.addCodeLine(" => M = " + Meintvar, null, 0, null);
    Entschl.highlight(0);
    Entschl.highlight(1);
    lang.nextStep();

    // step 15
    Entschl.unhighlight(0);
    Entschl.unhighlight(1);
    Entschl.highlight(6);
    Entschl.highlight(7);
    lang.nextStep();

    // step 17
    Entschl.hide(new TicksTiming(1));
    t4.hide(new TicksTiming(1));
    JavaCode.hide(new TicksTiming(1));
    t11.hide(new TicksTiming(1));

    Text tV = lang.newText(new Coordinates(20, 110), "Vorteile:", "tV", null,
        Kapitelueberschriften);
    SourceCode Zusammenfassung = lang.newSourceCode(new Coordinates(40, 100),
        "Zusammenfassung", null, Erklaerungen);
    Zusammenfassung.addCodeLine("", null, 0, null);
    Zusammenfassung.addCodeLine(
        "Der Vorteil asymmetrischer Verfahren liegt auf der Hand:", null, 0,
        null);
    Zusammenfassung.addCodeLine("Es ist beweisbar.", null, 0, null);
    Zusammenfassung
        .addCodeLine(
            "Es muss kein sicherer Kanal fuer den Schluesseltausch vorhanden sein,",
            null, 0, null);
    Zusammenfassung.addCodeLine("der Austausch kann oeffentlich stattfinden.",
        null, 0, null);
    Zusammenfassung
        .addCodeLine(
            "Ausserdem wird fuer die Kommunikation eine geringere Anzahl von Schluesseln benoetigt",
            null, 0, null);

    String Fazit = "Fazit";
    lang.nextStep(Fazit);

    // step 18
    Zusammenfassung.highlight(2);
    lang.nextStep();

    // step 19
    Zusammenfassung.unhighlight(2);
    Zusammenfassung.highlight(3);
    Zusammenfassung.highlight(4);
    lang.nextStep();

    // step 20
    Zusammenfassung.unhighlight(3);
    Zusammenfassung.unhighlight(4);
    Zusammenfassung.highlight(5);
    // Zusammenfassung.highlight(7);
    lang.nextStep();

    // step 21
    Zusammenfassung.hide(new TicksTiming(1));
    tV.hide(new TicksTiming(1));

    Text tN = lang.newText(new Coordinates(20, 110), "Nachteile:", "tN", null,
        Kapitelueberschriften);
    SourceCode Zusammenfassung2 = lang.newSourceCode(new Coordinates(40, 100),
        "Zusammenfassung2", null, Erklaerungen);

    Zusammenfassung2.addCodeLine(" ", null, 0, null);
    Zusammenfassung2
        .addCodeLine(
            "Als Nachteil ist insbesondere der sehr hohe Aufwand fuer Ver- und Entschluesselung zu sehen. ",
            null, 0, null);
    Zusammenfassung2
        .addCodeLine(
            "So muss bereits der Schluessel - bei gleicher Sicherheit - erheblich laenger sein als bei ",
            null, 0, null);
    Zusammenfassung2
        .addCodeLine(
            "einem symmetrischen Verfahren, was denn auch zum hoeheren Aufwand beitraegt.",
            null, 0, null);
    Zusammenfassung2
        .addCodeLine(
            "Des weiteren entstehen Chiffretexte die laenger sind als die originaeren Klartexte und",
            null, 0, null);
    Zusammenfassung2
        .addCodeLine(
            "die Algorithmen sind in der Regel nur schwer hardwareseitig umzusetzen. ",
            null, 0, null);
    Zusammenfassung2
        .addCodeLine(
            "Als weiteren Nachteil ist gewiss die Unsicherheit zu sehen, dass das zugrundeliegende mathematische",
            null, 0, null);
    Zusammenfassung2.addCodeLine(
        "Problem eben doch einer einfachen Loesung zugefuehrt werden kann.",
        null, 0, null);
    Zusammenfassung2.addCodeLine("", null, 0, null);
    lang.nextStep();

    // step 22
    Zusammenfassung2.highlight(1);
    lang.nextStep();

    // step 23
    Zusammenfassung2.unhighlight(1);
    Zusammenfassung2.highlight(2);
    Zusammenfassung2.highlight(3);
    lang.nextStep();
    // step 24
    Zusammenfassung2.unhighlight(2);
    Zusammenfassung2.unhighlight(3);
    Zusammenfassung2.highlight(4);
    Zusammenfassung2.highlight(5);
    lang.nextStep();
    // step 25
    Zusammenfassung2.unhighlight(4);
    Zusammenfassung2.unhighlight(5);
    Zusammenfassung2.highlight(6);
    Zusammenfassung2.highlight(7);
    lang.nextStep();
    // step 26
    Zusammenfassung2.highlight(6);
    Zusammenfassung2.highlight(7);
    tN.hide(new TicksTiming(1));
    Zusammenfassung2.hide(new TicksTiming(1));
    lang.newText(new Coordinates(20, 110), "Angriffsmoeglichkeiten:", "tB",
        null, Kapitelueberschriften);
    SourceCode Brechen = lang.newSourceCode(new Coordinates(40, 100),
        "Brechen", null, Erklaerungen);
    Brechen.addCodeLine("", null, 0, null);
    Brechen
        .addCodeLine(
            "Das Brechen der ElGamal-Chiffrierung bei bekanntem oeffentlichen Schluessel und Geheimtext",
            null, 0, null);
    Brechen.addCodeLine(
        "ist vergleichbar mit dem Loesen des sog. Diffie-Hellman-Problems.",
        null, 0, null);
    Brechen.addCodeLine(
        "Bis zum heutigen Zeitpunkt ist bekannt, dass das Loesen ", null, 0,
        null);
    Brechen
        .addCodeLine(
            "des Diffie-Hellman-Problems nur ueber die Berechnung diskreter Logarithmen moeglich ist.",
            null, 0, null);
    Brechen.addCodeLine("", null, 0, null);
    Brechen.addCodeLine("", null, 0, null);
    Brechen.addCodeLine(
        "Jedoch ist ElGamal  nicht sicher gegen Chosen-Ciphertext Angriffe. ",
        null, 0, null);
    Brechen
        .addCodeLine(
            "Angreifer kann sich zu selbst gewaehlten Chiffretexten C die Klartexte M erzeugen.",
            null, 0, null);
    lang.nextStep();

    // step 27
    Brechen.highlight(7);
    Brechen.highlight(8);
    lang.nextStep();

    // step 28
    Brechen.unhighlight(7);
    Brechen.unhighlight(8);
    lang.nextStep();

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    Titel = (TextProperties) props.getPropertiesByName("Titel");
    a = (Integer) primitives.get("a");
    b = (Integer) primitives.get("b");
    M = (Integer) primitives.get("M");
    Erklaerungen = (SourceCodeProperties) props
        .getPropertiesByName("Erklaerungen");
    Quellcode = (SourceCodeProperties) props.getPropertiesByName("Quellcode");
    Kapitelueberschriften = (TextProperties) props
        .getPropertiesByName("Kapitelueberschriften");

    elgamal();
    return lang.toString();
  }

  public String getName() {
    return "ElGamal Verfahren";
  }

  public String getAlgorithmName() {
    return "ElGamal";
  }

  public String getAnimationAuthor() {
    return "Emre Cakmak, Franklin Labang";
  }

  public String getDescription() {
    return "Der ElGamal-Algorithmus ist eine Variation des Diffie-Hellman-Algorithmus .Sein Erfinder Taher ElGamal ver&ouml;ffentlichte es 1985."
        + "\n"
        + "Es geh&ouml;rt, wie zum Beispiel auch das weitausbekannterer RSA-Verfahren, zu den sogenannten asymmetrischen Verfahren."
        + "\n"
        + "   									"
        + "\n"
        + "Es beruht auf der Grundlage, dass Sender und Empf&auml;nger jeweils zwei Schl&uuml;ssel haben."
        + "\n"
        + "Der sogenannte &ouml;ffentliche Schl&uuml;ssel (f&uuml;r die Verschl&uuml;sselung) ist jedem zug&auml;nglich,"
        + "\n"
        + "und somit auch jedem bekannt,der private Schl&uuml;ssel (f&uuml;r die Entschl&uuml;sselung) ist nur dem Empf&auml;nger bekannt."
        + "\n"
        + " 									"
        + "\n"
        + "In der Praxis findet das ElGamal-Algorithmus viele Anwendung."
        + "\n"
        + "Es dient zum Beispiel der Verschl&uuml;sselung, zum Sch&uuml;sselaustausch und zur digitalen / elektronischen Signatur.";
  }

  public String getCodeExample() {
    return "import java.math.*;" + "\n" + "import java.util.*;" + "\n"
        + "import java.security.*;" + "\n" + "import java.io.*;" + "\n" + "\n"
        + "public class ElGamal{" + "\n"
        + "public static void main(String[] args) throws IOException {" + "\n"
        + "BigInteger p, g, h, a;" + "\n" + "Random sc = new SecureRandom();"
        + "\n" + "a = new BigInteger(\\'12345678901234567890\\');" + "\n"
        + "										" + "\n" + "// public key calculation" + "\n"
        + "	p = BigInteger.probablePrime(64, sc);" + "\n"
        + "	g = new BigInteger(\\'3\\');" + "\n" + "	h = g.modPow(a, p);"
        + "\n" + "\n" + "// Encryption" + "\n"
        + "	String s = Tools.getString();" + "\n"
        + "	BigInteger M = new BigInteger(s) // Wobei m die Message ist die;"
        + "\n" + "	BigInteger K = new BigInteger(64, sc);" + "\n"
        + "	BigInteger C = M.multiply(g.modPow(K, p)).mod(p);   " + "\n"
        + "	BigInteger B = g.modPow(K, p);       " + "\n" + "	 " + "\n"
        + "// Decryption" + "\n" + "	BigInteger Z = B.modPow(a, p);" + "\n"
        + "	BigInteger d =  crmodp.modInverse(p);" + "\n"
        + "	BigInteger M = d.multiply(C).mod(p);" + "\n" + "}" + "\n" + "  }";
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

}