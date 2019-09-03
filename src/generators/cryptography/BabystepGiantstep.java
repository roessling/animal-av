package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.math.BigInteger;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;

public class BabystepGiantstep implements Generator {
  private Language lang;
  private Color    BabystepFillColor;
  private int      g;
  private int      prim;
  private Color    arrayMarkerFoundColor;
  private Color    GiantstepFillColor;
  private int      alpha;
  private Color    arrayMarkerSearchColor;
  private Font     BabystepFont;
  private Font     GiantstepFont;
  private Color    GiantstepElementColor;
  private Color    BabystepElementcolor;

  public void init() {
    lang = new AnimalScript("Babystep-Giantstep [DE]",
        "Julian Metzler, Tino Fuhrmann", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    BabystepFillColor = (Color) primitives.get("BabystepFillColor");
    g = (Integer) primitives.get("g");
    prim = (Integer) primitives.get("prim");
    arrayMarkerFoundColor = (Color) primitives.get("arrayMarkerFoundColor");
    GiantstepFillColor = (Color) primitives.get("GiantstepFillColor");
    alpha = (Integer) primitives.get("alpha");
    arrayMarkerSearchColor = (Color) primitives.get("arrayMarkerSearchColor");
    BabystepFont = (Font) primitives.get("BabystepFont");
    GiantstepFont = (Font) primitives.get("GiantstepFont");
    GiantstepElementColor = (Color) primitives.get("GiantstepElementColor");
    BabystepElementcolor = (Color) primitives.get("BabystepElementcolor");

    algo(prim, g, alpha);

    return lang.toString();
  }

  public void algo(int ordnung, int erzeuger, int alp) {
    int x; // x Loesung
    int m = (int) Math.ceil(Math.sqrt(ordnung - 1)); // abrunden fuer m

    // SourceCodeEigenschaften
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // Einleitung
    SourceCode introduction = lang.newSourceCode(new Coordinates(20, 25),
        "sourceCode", null, scProps);
    introduction
        .addCodeLine(
            "Willkommen zur der Lektion Babystep-Giantstep. Hier folgt nun eine visuelle Darstellung.",
            null, 0, null);
    introduction
        .addCodeLine(
            " Der Algorithmus versucht das DL-Problem in angemesser Zeit zu l&oumlsen. ",
            null, 0, null);
    introduction
        .addCodeLine(
            "Der Algorithmus spielt vorallem in der Kryptographie eine wichtige Rolle.",
            null, 0, null);
    introduction.addCodeElement("Viel Spa&szlig damit!", null, 0, null);

    lang.nextStep();
    introduction.hide();

    // SourceCode
    SourceCode sc = lang.newSourceCode(new Coordinates(20, 25), "sourceCode",
        null, scProps);
    sc.addCodeLine("Willkommen bei der Lektion Babystep-Giantstep Algorithmus",
        null, 40000000, null);
    sc.addCodeLine("Die Aufgabe lautet wie folgt:" + erzeuger
        + "^x &equiv; (kongruent)   " + alp + " mod " + ordnung, null, 0, null);
    sc.highlight(0);
    lang.nextStep();
    sc.addCodeLine("1.Schritt: Wurzel aus &radic" + ordnung + "ziehen", null,
        1, null);
    sc.unhighlight(0);
    sc.highlight(1);
    lang.nextStep();
    sc.addCodeLine("Ergebnis ist " + m, null, 2, null);
    sc.toggleHighlight(1, 2);
    lang.nextStep();
    sc.unhighlight(2);
    sc.addCodeLine(
        "Unser Ziel ist x = qm + r, 0 &le r < m, r ist der Rest und q der Qoutient der Division von x ",
        null, 3, null);
    sc.addCodeLine(
        "Mithilfe des Babystep-Giantstep Algorithmus berechnen wir q und r. ",
        null, 4, null);
    lang.nextStep();

    int inverse = inverse(erzeuger, ordnung);
    sc.addCodeLine("Die Menge der Babysteps wird wie folgt berechnet", null, 5,
        null);
    sc.addCodeLine("B={(" + alp + "*" + erzeuger + "^-r, r) : 0 &le r < m",
        null, 6, null);

    sc.addCodeLine("Als Tipp: " + erzeuger
        + "^-r ist hilfreich als inverses davon zu nehmen, n&aumlmlich:  "
        + erzeuger + " /&equiv -1 mod" + ordnung, null, 7, null);
    sc.addCodeLine("Daraufhin muss man einfach das inverse immer auf den "
        + erzeuger + "^-r multiplizieren.", null, 8, null);
    sc.highlight(7);
    sc.addCodeLine(
        "Hinweis: Kommt es bei der Babystepmenge Berechnung zu einer 1 dann l&aumlsst sich x = r setzen. und der Algorithmus terminiert",
        null, 9, null);
    lang.nextStep();

    int[] a = new int[(int) m];
    for (int i = 0; i < m; i++)
      a[i] = i;

    ArrayProperties arrayPropsBaby = new ArrayProperties();

    arrayPropsBaby
        .set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.RED); // color
                                                                        // red
    arrayPropsBaby.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // filled
    arrayPropsBaby.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY); // fill
                                                                           // color
                                                                           // gray

    // Set<String> se = arrayPropsBaby.getAllPropertyNames();
    // arrayPropsBaby.getItem("fillcolor");

    // ArrayMarkerProperties ami = new ArrayMarkerProperties();
    // ami.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.PINK);
    // ami.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");

    // Timing defaultTiming = new TicksTiming(15);

    long zwischen = 0;

    int potenz = inverse;
    int lsg = 0;
    int[] b = new int[(int) m];
    for (int i = 0; i < b.length; i++)
      b[i] = ordnung + 1;

    String fillarray = "";

    BigInteger bigInverse = new BigInteger("" + inverse);
    // BigInteger biHelp = null;
    BigInteger bigzwischen = new BigInteger("" + alp);
    BigInteger bigPotenz = bigInverse;
    BigInteger bigAlp = new BigInteger("" + alp);
    for (int i = 0; i < m; i++) {
      if (zwischen == 1 && bigzwischen.intValue() == 1) {
        lsg = i;
        break;
      }

      if (i == 0) {
        bigzwischen = new BigInteger("" + alp);
        zwischen = alp;
      } else {
        if (i == 1) {
          bigzwischen = bigInverse.modPow(new BigInteger("" + i),
              new BigInteger("" + ordnung));
          zwischen = (int) (Math.pow(inverse, i));
        } else {
          potenz = (potenz * inverse) % ordnung; // muss eingefuehrt werden da
                                                 // der Zahlenbereich von Int
                                                 // nicht ausreicht :(
          bigPotenz.multiply(bigInverse);
          bigPotenz.mod(new BigInteger("" + ordnung));
        }

        // zwischen = zwischen % ordnung;
        zwischen = potenz * alp;
        zwischen = zwischen % ordnung;

        bigzwischen = bigInverse.modPow(new BigInteger("" + i), new BigInteger(
            "" + ordnung));
        bigzwischen = bigzwischen.multiply(bigAlp);
        // bigAlp.multiply(bigPotenz);
        // bigzwischen = bigAlp;
        bigzwischen = bigzwischen.mod(new BigInteger("" + ordnung));
        // bigAlp = new BigInteger("" + alp);
      }
      // zwischen = bigzwischen.longValue();
      // b[i] = (int) zwischen;
      // int test = bigzwischen.intValue();
      b[i] = bigzwischen.intValue();
      // b[i] = zwischen;
      fillarray += "\"" + b[i] + "\" ";

      // Animalscript erstellen, der fuer jeden Babystep einen neuen Array
      // erstellt.....
      if (i != 0) {
        lang.addLine("hide \"babysteps" + (i - 1) + "\"");
        lang.addLine("hide \"r" + (i - 1) + "\"");
      }
      lang.addLine("array \"babysteps"
          + i
          + "\" (50, 400)  color (0, 0, 0) fillColor ("
          + BabystepFillColor.getRed()
          + ", "
          + BabystepFillColor.getGreen()
          + ","
          + " "
          + BabystepFillColor.getBlue()
          + ") elementColor ("
          + BabystepElementcolor.getRed()
          + ", "
          + BabystepElementcolor.getGreen()
          + ","
          + ""
          + BabystepElementcolor.getBlue()
          + ") "
          + "elemHighlight (0, 0, 0) cellHighlight (0, 0, 0) horizontal length "
          + (i + 1) + " " + fillarray + "depth 1 font "
          + BabystepFont.getName() + " size 12");

      lang.addLine("text \"r" + i + "\" \"r        " + (i + 1)
          + "\" offset ( 50 , 325) from \"sourceCode\" NW depth 1");
      lang.nextStep();

    }

    if (zwischen == 1) {
      x = lsg;
    } else {
      lang.addLine("hide \"r" + (m - 1) + "\"");
      sc.addCodeLine("Es kam keine 1 vor, andernfalls berechnet man "
          + erzeuger + "^q*" + m + " f&uumlr q= 1,2,3....", null, 10, null);
      sc.addCodeLine("Bis wir ein Paar in den Babysteps gefunden haben!", null,
          11, null);
      sc.addCodeLine("Suchen......!", null, 12, null);
      sc.toggleHighlight(7, 11);

      // Giantstep
      BigInteger bi = new BigInteger("" + erzeuger);
      BigInteger bigDelta = bi.modPow(new BigInteger("" + m), new BigInteger(""
          + ordnung));
      // System.out.println("delta: " + bigDelta.intValue());

      int[] searchBaby = b;

      BigInteger giant;
      int ja = 0;
      int r = 0;
      int q;
      // IntArray giantstep = lang.newIntArray(new Coordinates(100, 300), z ,
      // "giantsteps", null, arrayPropsBaby);
      int[] helpGiantArray = new int[2000000];
      fillarray = "";
      lang.addLine("arrayMarker \"i\" on \"babysteps" + (m - 1)
          + "\" atIndex 0 label \"suchen\" color ("
          + arrayMarkerSearchColor.getRed() + ", " + ""
          + arrayMarkerSearchColor.getGreen() + ", "
          + arrayMarkerSearchColor.getBlue() + ") depth 1");
      // ArrayMarker i =
      // lang.newArrayMarker("babysteps"+(m-1)+", 0 , "Startsuchen", null);
      sc.toggleHighlight(11, 13);
      lang.nextStep();
      for (q = 1; q < 20000000; q++) { // q ist anscheinend unendlich lang
        giant = bigDelta.modPow(new BigInteger("" + q), new BigInteger(""
            + ordnung));
        // System.out.println(giant.intValue());
        helpGiantArray[q - 1] = giant.intValue(); // fuer api bla

        if (q != 1) {
          lang.addLine("hide \"giantsteps" + (q - 1) + "\"");
        }
        /*
         * if(q>1){ sc.toggleHighlight(13,11);
         * 
         * }
         */
        fillarray += "\"" + giant.intValue() + "\"";
        lang.addLine("array \"giantsteps"
            + q
            + "\" (50, 550)  color (0, 0, 0) fillColor ("
            + GiantstepFillColor.getRed()
            + ", "
            + GiantstepFillColor.getGreen()
            + ","
            + ""
            + GiantstepFillColor.getBlue()
            + ") elementColor ("
            + GiantstepElementColor.getRed()
            + ", "
            + GiantstepElementColor.getGreen()
            + ", "
            + ""
            + GiantstepElementColor.getBlue()
            + ") "
            + "elemHighlight (0, 0, 0) cellHighlight (0, 0, 0) horizontal length "
            + (q) + " " + fillarray + "depth 1 font " + GiantstepFont.getName()
            + " size 12");
        lang.nextStep();
        for (int s = 0; s < searchBaby.length; s++) {
          if (s == 0 && q > 1) {
            lang.addLine("moveArrayMarker \"i\" to position 0    within 15 ticks");
            lang.nextStep();
          }
          // else
          // lang.addLine("moveArrayMarker \"i\" to position "+s+"    within 15 ticks");

          if (giant.intValue() == searchBaby[s]) {
            ja = 1;
            r = s;
            // System.out.println(s);
            // lang.addLine("hide \"i\"");
            lang.addLine("arrayMarker \"j\" on \"babysteps" + (m - 1)
                + "\" atIndex 0 label \"Treffer\" color ("
                + arrayMarkerFoundColor.getRed() + ", " + ""
                + arrayMarkerFoundColor.getGreen() + ", "
                + arrayMarkerFoundColor.getBlue() + ") depth 1");
            lang.addLine("moveArrayMarker \"j\" to position " + s
                + "    within 15 ticks");
            sc.addCodeLine("Gefunden! an Position: " + r, null, 13, null);
            sc.toggleHighlight(13, 14);
            lang.nextStep();
            break;
          }
          // sc.toggleHighlight(11, 13);
          // lang.nextStep();
        }
        if (ja == 1) {
          // System.out.println(giant);
          break;
        }

      }

      sc.addCodeLine("3.Schritt folgende Gleichung l&oumlsen: x= " + q + "*"
          + m + "+" + r, null, 14, null);
      sc.toggleHighlight(14, 15);
      // Berechne das 5^x = 35 mod 353
      x = (int) (q * m + r);

      lang.nextStep();

    }
    sc.addCodeLine("Das Ergebnis lautet: " + erzeuger + "^" + x
        + " /&equiv(kongruent) " + alp + " mod " + ordnung, null, 15, null);
    sc.toggleHighlight(15, 16);

  }

  // groesster gemeinsamer Zaehler
  int[] gcd(int p, int q) {
    if (q == 0)
      return new int[] { p, 1, 0 };
    int[] vals = gcd(q, p % q);
    int d = vals[0];
    int a = vals[2];
    int b = vals[1] - (p / q) * vals[2];
    return new int[] { d, a, b };
  }

  // -1 mod n errechnen
  int inverse(int k, int n) {
    int[] vals = gcd(k, n);
    int d = vals[0];
    int a = vals[1];
    // int b = vals[2];
    if (d > 1) {
      System.out.println("Inverse does not exist.");
      return 0;
    }
    if (a > 0)
      return a;
    return n + a;
  }

  public String getName() {
    return "Babystep-Giantstep [DE]";
  }

  public String getAlgorithmName() {
    return "Babystep-Giantstep";
  }

  public String getAnimationAuthor() {
    return "Julian Metzler, Tino Fuhrmann";
  }

  public String getDescription() {
    return "Willkommen zur der Lektion Babystep-Giantstep. Hier folgt nun eine visuelle Darstellung. Der Algorithmus versucht das "
        + "\n"
        + "DL-Problem in angemesser Zeit zu l&ouml;sen. Der Algorithmus spielt vorallem in der Kryptographie eine wichtige Rolle."
        + "\n"
        + "Viel Spa&szlig; damit!"
        + "\n"
        + "\n"
        + "Hier besch&auml;ftigen wir uns mit dem diskrete Logarithmus der Analog zum gew&ouml;hnlichen Logarithmus aus der"
        + "\n"
        + "Analysis zu sehen kann. Das diskret kann in diesem Zusammenhang etwa wie Ganzzahlig verstanden werden. Die diskrete"
        + "\n"
        + "Exponentiation in einer zyklischen Gruppe bildet eine Umkehrfunktion des diskreten Logarithmus."
        + "\n"
        + "Man versucht folgendes Problem zu l&ouml;sen:"
        + "\n"
        + "\n"
        + "a<sup>x</sup> &equiv; m mod p , wobei p eine Primzahl und bei den gegebenen nat&uuml;rlichen Zahlen a,m."
        + "\n"
        + "Frage ist, also gibt es einen Exponenten x der die L&ouml;sung erfüllt."
        + "\n"
        + "\n"
        + "Anmerkung: Nur in Gruppen, in denen das DL-Problem schwierig zu l&ouml;sen ist, k&ouml;nnen das"
        + "\n"
        + "El-Gamel-Verschl&uuml;ssungsverfahren und viele andere Public-Key-Verfahren sicher sein. Daher ist das DL-Problem"
        + "\n"
        + "von gro&szlig;er Bedeutung in der Kryptogrpahie."
        + "\n"
        + "\n"
        + "Zuerst muss man entscheiden, ob es &uuml;berhaupt einen diskreten Logarithmus gibt. Falls ja, ist das kleinste nicht"
        + "\n"
        + "negative x gesucht."
        + "\n"
        + "\n"
        + "Der Babystep-Giantstep-Algorithmus berechnet den diskreten Logarithmus eines Elements"
        + "\n"
        + "einer zyklischen Gruppe. Der Algorithmus ist zwar in der Laufzeit dem naiven Ausprobieren"
        + "\n"
        + "aller M&ouml;glichkeiten &uuml;berlegen, ist aber dennoch für sehr gro&szlig;e Gruppen praktisch nicht "
        + "\n"
        + "durchf&uuml;hrbar. Der Algorithmus stammt von Daniel Shanks."
        + "\n"
        + "\n"
        + "Zuerst m&uuml;ssen wir "
        + "\n"
        + "	m = &lceil; &radic; n &rceil;"
        + "\n"
        + "berechnen, um folgendes zu l&ouml;sen"
        + "\n"
        + "	x = qm + r , 0 &le; r < m"
        + "\n"
        + "Dabei ist r also der Rest und q ist der Quotient der Division von x durch m. Der"
        + "\n"
        + "Babystep-Giantstep-Alogorithmus berechnet q und r. Dies geschieht folgenderma&szlig;en."
        + "\n"
        + "Es gilt"
        + "\n"
        + "	&gamma;<sup>qm</sup> + r = &gamma;<sup>x</sup> = &alpha;"
        + "\n"
        + "Daraus folgt"
        + "\n"
        + "	(&gamma;<sup>m</sup>)<sup>q</sup> = &alpha;&gamma; <sup>-r</sup>"
        + "\n"
        + "Man berechnet nun zuerst die Menge der Babysteps"
        + "\n"
        + "	B = {(&alpha;&gamma;<sup>-r</sup> , r) : 0 &le; r < m}."
        + "\n"
        + "Findet man ein Paar(1,r), so kann man x = r setzen und hat das DL-Problem gel&ouml;st. "
        + "\n"
        + "Andernfalls bestimmt man"
        + "\n"
        + "	&delta; = &gamma;<sup>m</sup>"
        + "\n"
        + "und pr&uuml;ft, ob f&uumlr q = 1,2,3...... das Gruppenelement &delta;<sup>q</sup> als erste Komponente eines "
        + "\n"
        + "Elementes von B vorkommt, ob also ein Paar(&delta;<sup>q</sup> , r) zu B geh&ouml;rt. Sobald dies der Fall ist, gilt"
        + "\n"
        + "	&alpha;&gamma;<sup>-r</sup> = &delta;<sup>q</sup> = &gamma;<sup>qm</sup>"
        + "\n"
        + "und man hat den diskreten Logarithmus"
        + "\n"
        + "	x = qm + r"
        + "\n"
        + "gefunden. Die Berechnung der Elemente &delta;<sup>q</sup> , q = 1,2,3..... nennt, man Giantsteps. Um die &Uuml;berpr&uuml;fung, ob"
        + "\n"
        + "&delta;<sup>q</sup> als erste Komponente eines Elementes der Babystep-Menge vorkommt, effizient zu gestalten, nimmt man"
        + "\n"
        + "die Elemente dieser Menge in eine Hashtabelle auf, wobei die erste Komponente eines jeden Elementes als Schl&uuml;ssel"
        + "\n"
        + "dient."
        + "\n"
        + "\n"
        + "Parameter:"
        + "\n"
        + " G - eine endliche zyklische Gruppe der Ordnung n"
        + "\n"
        + "&gamma; - Erzeuger dieser Gruppe"
        + "\n"
        + "n - Gruppenordnung"
        + "\n"
        + "x- gesuchte L&ouml;sung"
        + "\n"
        + "\n"
        + "Komplexit&auml;t: Der Babystep-Giantstep-Algorithmus ben&oumltigt &Omicron;(&radic;|G|) Multiplikationen und Vergleiche"
        + "\n"
        + "in G. Er muss &Omicron;(&radic;|G|) viele Elemente in G speichern."
        + "\n"
        + "Der Zeit- und Platzbedarf des Babystep-Giantstep-Algorithmus ist von der Gro&szlig;enordnung &radic;|G|. Ist"
        + "\n"
        + "|G| > 2<sup>160</sup>, so ist der Algorithmus in der Praxis nicht mehr einsetzbar.";
  }

  public String getCodeExample() {
    return "";
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
