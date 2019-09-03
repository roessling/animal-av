package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.math.BigInteger;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.Circle;
import algoanim.primitives.CircleSeg;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.CircleSegProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;

public class BabystepGiantstepGenerator implements Generator {

  private Language             lang;

  private ArrayProperties      arrayQProps;
  private ArrayProperties      arrayRProps;
  private SourceCodeProperties explanationProps;
  private SourceCodeProperties scProps;

  private SourceCode           sc;
  private SourceCode           explanation;
  private SourceCode           results;
  private SourceCode           introduction;
  private SourceCode           observe;
  private SourceCode           circleText;

  private Text                 header;
  private int                  result;
  private int                  p;
  private int                  y;
  private int                  z;
  private TextProperties       headerProps;

  private boolean              questions  = true;
  private boolean              circleAnim = true;
  private CircleSeg[]          babySteps;
  private CircleSeg[]          giantSteps;
  private int                  angle;

  private Circle               circle;
  private CircleSeg            circleSeg1;
  private CircleSeg            circleSeg2;
  // private CircleSeg circleSeg3;
  private CircleSeg            circleSeg4;
  private CircleSeg            giant1;
  private CircleSeg            giant2;

  // private CircleSeg giant3;

  /*
   * ============================================================================
   * ================= Only for debugging: private static
   * BabystepGiantstepGenerator bg = new BabystepGiantstepGenerator();
   * ==========
   * ==================================================================
   * =================
   */

  public void init() {
    lang = new AnimalScript("Babystep Giantstep", "Daniel Lehmann,Ji-Ung Lee",
        800, 600);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    lang.setStepMode(true);

    headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 20));
  }

  /**
   * A method for checking, if the given value is a prime. It is written very
   * simple, since after generating the animation this method will not be used
   * any longer.
   * 
   * @param checkVal
   *          the number to check
   * @return true if the number is a prime
   */
  private boolean isPrime(int checkVal) {
    for (int i = 2; i < (int) Math.sqrt(p); i++) {
      if (p % i == 0)
        return false;
    }
    return true;
  }

  /**
   * Only for the circle animation. Takes the array r as input and returns the
   * steplength of one q step.
   * 
   * @param arrayR
   *          the array R,
   * @param basisPowM
   *          a helpful variable
   * @return stepLength the steplength of one q step.
   */
  private int getQStepLength(int[] arrayR, int basisPowM) {
    int[] qSteps = new int[arrayR.length];
    int qResult = 0;
    int rResult = 0;
    qSteps[0] = 1;
    for (int h = 1; h < arrayR.length; h++) {
      qSteps[h] = (qSteps[h - 1] * basisPowM) % p;
      for (int u = 0; u < arrayR.length; u++) {
        if (arrayR[u] == qSteps[h]) {
          rResult = u;
          qResult = h + 1;
        }
      }
    }
    /*
     * ==========================================================================
     * =================== We have the whole circle and we want to start at 90°
     * for the r steps. Then we want to start at 60° for the q steps, so the r
     * steps are 30° ahead. Also the startAngle of the u-th element is u*10
     * added to 30. Every q circle segment has to go the other way around, which
     * sets the result to 360 - (30+10*u). Then we divide through the number of
     * steps. The number of steps is the position number + 1, since the array
     * counter starts at 0.
     * ======================================================
     * =======================================
     */
    int stepLength = 360 - (rResult * 10 + 30);
    stepLength = (int) Math.ceil(stepLength / qResult);
    return stepLength;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    init();
    if (props == null || props.isEmpty()) {
      // This is in case, we want to test without having to generate a new asu
      // file using the generator.
      arrayRProps = new ArrayProperties();
      arrayRProps.set(AnimationPropertiesKeys.CASCADED_PROPERTY, true);
      arrayRProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
          Color.BLACK);
      arrayRProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
      arrayRProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
      arrayRProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
          Color.YELLOW);
      arrayRProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
          Color.BLUE);

      arrayQProps = new ArrayProperties();
      arrayRProps.set(AnimationPropertiesKeys.CASCADED_PROPERTY, true);
      arrayQProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
          Color.BLACK);
      arrayQProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
      arrayQProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
      arrayQProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
          Color.YELLOW);
      arrayQProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
          Color.BLUE);

      explanationProps = new SourceCodeProperties();
      explanationProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
          Color.BLACK);
      explanationProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          "Monospaced", Font.PLAIN, 12));
      explanationProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
          Color.RED);
      explanationProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

      scProps = new SourceCodeProperties();
      scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
      scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
          Font.PLAIN, 12));
      scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
      scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

      p = 353;
      y = 5;
      z = 35;

    } else {

      arrayQProps = (ArrayProperties) props.getPropertiesByName("Q");
      questions = (Boolean) primitives.get("Questions");
      scProps = (SourceCodeProperties) props.getPropertiesByName("pseudoCode");
      explanationProps = (SourceCodeProperties) props
          .getPropertiesByName("explanation");
      arrayRProps = (ArrayProperties) props.getPropertiesByName("R");
      circleAnim = (Boolean) primitives.get("circleAnim");

      p = (Integer) primitives.get("p");
      z = (Integer) primitives.get("z");
      y = (Integer) primitives.get("y");

    }
    header = lang.newText(new Coordinates(10, 20), " ", "Header", null,
        headerProps);
    /*
     * ==========================================================================
     * =================== In case the given number is not a prime, or z or y
     * equals 0 in G, we replace the values. Note: For a good animation with the
     * circle representation the prime should be smaller than 1000. So if the
     * circle animation is set, we will only let p smaller than 1000 pass.
     * ======
     * ====================================================================
     * ===================
     */
    if (!isPrime((Integer) primitives.get("p")) || (z % p == 0) || (y % p == 0)
        || y > p || (circleAnim && p > 1000)) {
      p = 13;
      y = 2;
      z = 5;
      circleAnim = false;

      SourceCodeProperties wrongProps = new SourceCodeProperties();
      wrongProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
      wrongProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          "Monospaced", Font.PLAIN, 12));
      wrongProps
          .set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
      wrongProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

      SourceCode wrong = lang.newSourceCode(new Coordinates(10, 40),
          "wrongValues", null, wrongProps);

      wrong.addCodeLine("Sie haben für p einen ungeeigneten Wert eingegeben.",
          null, 0, null);
      wrong
          .addCodeLine(
              "Bitte geben sie eine Primzahl für p ein, damit die erzeugte Gruppe G die maximale Ordnung hat.",
              null, 0, null);
      wrong
          .addCodeLine(
              "Beachten sie bitte auch, dass der Algorithmus für Werte bei denen gilt: ",
              null, 0, null);
      wrong.addCodeLine("y modulo p = 0     oder     z modulo p = 0, ", null,
          0, null);
      wrong
          .addCodeLine(
              "nicht sinnvoll ist, da die Lösung des DL-Problems im Falle von y modulo p = 0 trivial ist ",
              null, 0, null);
      wrong.addCodeLine("und im Falle von z modulo p = 0 nicht lösbar ist. ",
          null, 0, null);
      wrong.addCodeLine("", null, 0, null);
      wrong
          .addCodeLine(
              "Damit dennoch ein Beispiel gezeigt werden kann, wurden die Werte p, y und z auf folgende gesetzt:.",
              null, 0, null);
      wrong.addCodeLine("p = 13", null, 1, null);
      wrong.addCodeLine("y = 2", null, 1, null);
      wrong.addCodeLine("z = 5", null, 1, null);
      wrong.addCodeLine(
          "Wir wünschen ihnen dennoch viel Spaß mit der Animation!", null, 1,
          null);

      lang.nextStep(); // ------------------------------- next step
                       // ---------------

      wrong.hide();
    } else if (z > p) {
      /*
       * ========================================================================
       * ===================== If y or z are bigger than p, we can make it
       * easier by setting the values to their modulo values.
       * ====================
       * ====================================================
       * =====================
       */
      String zIs = "z = " + z + " modulo " + p + " = ";
      z = z % p;
      zIs = zIs + z;

      SourceCodeProperties badProps = new SourceCodeProperties();
      badProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
      badProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          "Monospaced", Font.PLAIN, 12));
      badProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
      badProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

      SourceCode bad = lang.newSourceCode(new Coordinates(10, 40),
          "wrongValues", null, badProps);

      bad.addCodeLine(
          "Ihre Eingaben waren zwar korrekt, allerdings sollten sie beachten, dass bei Rechnungen in einer ",
          null, 0, null);
      bad.addCodeLine(
          "endlichen, zyklischen Gruppe Werte, die größer als das Erzeugerelement p sind, modulo p gerechnet werden.",
          null, 0, null);
      bad.addCodeLine(
          "Für eine einfachere Berechnung ist wurden y und z auf folgende Werte gesetzt:",
          null, 0, null);
      bad.addCodeLine(zIs, null, 1, null);
      bad.addCodeLine(
          "Das gesuchte x ändert sich allerdings nicht, sodass sie auch für ihre Eingabewerte die korrekte Potenz bekommen.",
          null, 1, null);
      bad.addCodeLine(
          "Wir wünschen ihnen dennoch viel Spaß mit der Animation!", null, 1,
          null);

      lang.nextStep();

      bad.hide();
    } else if (y == z) {
      p = 13;
      y = 2;
      z = 5;
      circleAnim = false;

      SourceCodeProperties uselessProps = new SourceCodeProperties();
      uselessProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
          Color.BLUE);
      uselessProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          "Monospaced", Font.PLAIN, 12));
      uselessProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
          Color.RED);
      uselessProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

      SourceCode useless = lang.newSourceCode(new Coordinates(10, 40),
          "wrongValues", null, uselessProps);

      useless.addCodeLine("Ihr y und z sind identisch!.", null, 0, null);
      useless
          .addCodeLine(
              "In diesem Fall ist die Lösung trivial und daher der Algorithmus nicht notwendig.",
              null, 0, null);
      useless
          .addCodeLine(
              "Beachten sie bitte auch, dass der Algorithmus für Werte bei denen gilt: ",
              null, 0, null);
      useless.addCodeLine("y modulo p = 0     oder     z modulo p = 0, ", null,
          0, null);
      useless
          .addCodeLine(
              "nicht sinnvoll ist, da die Lösung des DL-Problems im Falle von y modulo p = 0 trivial ist ",
              null, 0, null);
      useless.addCodeLine("und im Falle von z modulo p = 0 nicht lösbar ist. ",
          null, 0, null);
      useless.addCodeLine("", null, 0, null);
      useless
          .addCodeLine(
              "Damit dennoch ein Beispiel gezeigt werden kann, wurden die Werte p, y und z auf folgende gesetzt:.",
              null, 0, null);
      useless.addCodeLine("p = 13", null, 1, null);
      useless.addCodeLine("y = 2", null, 1, null);
      useless.addCodeLine("z = 5", null, 1, null);
      useless.addCodeLine(
          "Wir wünschen ihnen dennoch viel Spaß mit der Animation!", null, 1,
          null);

      lang.nextStep(); // ------------------------------- next step
                       // ---------------

      useless.hide();
    }
    babystepGiantstep(p, y, z);
    lang.finalizeGeneration();

    /*
     * ==========================================================================
     * =================== A workaround, because the
     * AnimationPropertiesKeys.STARTANGLE_PROPERTY creates the keyword
     * "startAngle", which seems to be cannot read by animal so we replace it
     * with "starts" which works
     * ================================================
     * =============================================
     */
    String code = lang.toString().replace("startAngle", "starts");
    return code;
  }

  public String getName() {
    return "Babystep Giantstep (Pseudocode)";
  }

  public String getAlgorithmName() {
    return "Babystep-Giantstep";
  }

  public String getAnimationAuthor() {
    return "Daniel Lehmann, Ji-Ung Lee";
  }

  public String getDescription() {
    return "Dies ist die Lektion zum Babystep-Giantstep Algorithmus von Shanks."
        + "\n"
        + "\n"
        + "Der Babystep-Giantstep Algorithmus ist ein Algorithmus, um diskrete Logarithmen (DL) schnell zu l&ouml;sen."
        + "\n"
        + "Ein DL Problem ist meist durch folgende Gleichung gegeben:"
        + "\n"
        + "	(z = y^x) modulo p"
        + "\n"
        + "Hierbei ist p eine Primzahl, welche die Primzahlgruppe G erzeugt."
        + "\n"
        + "Der Babystep-Giantstep Algorithmus bestimmt nun f&uuml;r gegebene z,y und p das gesuchte x."
        + "\n"
        + "\n"
        + "Hinweis: Beim generieren einer Animation haben sie die M&ouml;glichkeit, zus&auml;tzlich zur normalen Darstellung"
        + "\n"
        + "                 des Beispiels eine Visualisierung in Form eines Kreises anzeigen zu lassen. Bitte beachten sie allerdings,"
        + "\n"
        + "                 dass dann die Primzahl kleiner als 1000 sein muss. Bei falschen Werten, werden die Werte auf die Standardwerte"
        + "\n"
        + "                 p = 13 , z = 5 und y = 2 gesetzt."
        + "\n"
        + "\n" + "Viel Spa&szlig; mit der Animation!";
  }

  public String getCodeExample() {
    return "Der Algorithmus besteht aus mehreren Schritten:"
        + "\n"
        + "\n"
        + "1. Bestimme ein m f&uuml;r das gilt: m = ceiling(&radic;(|G|)) . Hierbei ist |G| die Gruppenordnung und ist bei Primzahlgruppen durch p-1 gegeben."
        + "\n"
        + "2. Das gesuchte x l&auml;sst sich als Linearkombination darstellen: x = m*q + r"
        + "\n"
        + "    Es gilt nun q und r zu bestimmen. F&uuml;r beide gilt: 0 &le; q < m und 0 &le; r < m ."
        + "\n"
        + "3. Bestimme die Menge R = {(r, (y^(-r))*z) : 0 &le; r < m}"
        + "\n"
        + "   Tipp: F&uuml;r eine einfachere Rechnung beachte y^(-r) = (y^(-1))^r  ! Nun l&auml;sst sich y^(-1) im vorraus berechnen."
        + "\n"
        + "4. Hat man nun alle Elemente der Menge R, so berechnet man die Menge Q:"
        + "\n"
        + "   4.1 Q = {(q, (y^m)^q) : 0 &le; q < m}"
        + "\n"
        + "          Tipp: Da m &uuml;ber die gesamte Berechnung konstant ist, kann man auch hier y^m bereits im voraus berechnen."
        + "\n"
        + "   4.2 F&uuml;r das berechnete Paar (q, (y^m)^q) sucht man ein (y^(-r))*z), sodass (y^m)^q = (y^(-r))*z) ."
        + "\n"
        + "  4.3 Existiert ein solches (y^(-r))*z), dann ist das aktuelle q und das r aus (y^(-r))*z) die gesuchten q und r."
        + "\n" + "5. Bestimme x aus x = m*q + r ";
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

  /**
   * the Babystep Giantstep Algorithm. Solves equations of the type: 5 = 2^x mod
   * 13 within a cyclic group generated by a given prime
   * 
   * @param prime
   *          , a prime which generates the group
   * @param basis
   *          , the basis; in the example it is 2
   * @param z
   *          , in the example it is the given result 5
   */
  public void babystepGiantstep(int prime, int basis, int z) {
    // Set a result to -1 a value, which can be never reached with the
    // algorithm!
    result = -1;
    int rNumber = 0;
    int qNumber = 0;

    // We set the header to Introduction
    header.setText("Hintergrund DL Probleme", null, null);
    showIntroduction();
    lang.nextStep("Hintergrund DL Probleme"); // ----------- next step
                                              // -----------------

    introduction.hide();
    header.setText("Babystep-Giantstep Algorithmus", null, null);
    showSourceCode();
    showExplanation();
    lang.nextStep("Babystep-Giantstep Algorithmus"); // ---- next step
                                                     // -----------------

    explanation.highlight(2);
    sc.highlight(2);

    // Calculate m. The order of a group generated by a prime is prime-1 .
    int m = (int) Math.ceil(Math.sqrt((double) (prime - 1)));

    lang.nextStep(); // ----------------------------------- next step
                     // -----------------
    explanation.unhighlight(2);
    sc.unhighlight(2);

    explanation.highlight(3);
    explanation.highlight(4);
    sc.highlight(3);
    sc.highlight(4);

    int[] r = new int[m];
    int[] q = new int[m];
    IntArray qi = lang.newIntArray(new Coordinates(550, 390), q, "Q", null,
        arrayQProps);

    // Calculate y^-1 the inverse for R
    BigInteger bigBasis = new BigInteger(Integer.toString(basis));
    BigInteger bigPrime = new BigInteger(Integer.toString(prime));
    int inverse = bigBasis.modInverse(bigPrime).intValue();

    /*
     * ==========================================================================
     * =================== Calculate y^m for easier calculating of Q, because in
     * case the values are too big, do that step by step.
     * ========================
     * =====================================================================
     */
    int basisPowM = 1;
    for (int i = 0; i < m; i++) {
      basisPowM = (basisPowM * bigBasis.intValue()) % prime;
    }

    lang.nextStep(); // ----------------------------------- next step
                     // ------------------
    explanation.unhighlight(3);
    explanation.unhighlight(4);
    sc.unhighlight(3);
    sc.unhighlight(4);

    sc.highlight(5);
    sc.highlight(6);
    sc.highlight(11);
    sc.highlight(12);
    explanation.highlight(5);

    /*
     * ==========================================================================
     * =================== Calculate R; first we set the first element to 1 For
     * that we use the inverse: y^(-i) = (y^-1)^i , but because of small integer
     * numbers we use the value of r[i-1] and calculate al y^-i .
     * ================
     * ==========================================================
     * ===================
     */
    r[0] = 1;
    for (int i = 1; i < m; i++) {
      r[i] = ((inverse * r[i - 1])) % prime;
    }
    for (int o = 0; o < r.length; o++) {
      /*
       * ========================================================================
       * ===================== When storing, we have to multiply all r[i] by the
       * value z: Also: we might use the array r later for analyzing, so we set
       * the correct value there too!
       * ============================================
       * =================================================
       */
      r[o] = r[o] * z % prime;
      // If there is a pair (r, 1), we are done looking for a x
      if (r[o] == 1) {
        result = o;
        break;
      }
    }

    // If the result was already set, we do not have to compute any more!
    if (result >= 0) {
      sc.highlight(7);
      sc.highlight(8);
      sc.highlight(9);
      sc.highlight(10);
      String easyWay = "Also ist unser x = r = " + result;
      showResult();
      results.addCodeLine(
          "Wir haben bereits ein Paar gefunden, für das gilt (r, 1)!", null, 0,
          null);
      results.addCodeLine(easyWay, null, 0, null);
    } else {
      // There was no trivial result, so we calculate a little more.
      IntArray ri = lang.newIntArray(new Coordinates(550, 340), r, "R", null,
          arrayRProps);

      /*
       * ========================================================================
       * ===================== Only for the circle animation. We generate our r
       * circle segments.
       * ========================================================
       * =====================================
       */
      if (circleAnim)
        showRSteps(m);

      /*
       * ========================================================================
       * ===================== We generate a counter for ri. The number of
       * assignments to ri is the same as the length of ri, so we can set it.
       * The values in q dont have to be stored, so there is no need for a
       * counter.
       * ================================================================
       * =============================
       */
      TwoValueCounter counter = lang.newCounter(ri);
      CounterProperties cp = new CounterProperties();
      cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
      TwoValueView view = lang.newCounterView(counter,
          new Coordinates(550, 300), cp, true, true);
      counter.assignmentsInc(ri.getLength());

      lang.nextStep(); // ------------------------------- next step
                       // ---------------

      sc.unhighlight(5);
      sc.unhighlight(6);
      sc.unhighlight(11);
      sc.unhighlight(12);
      explanation.unhighlight(5);

      sc.highlight(13);
      sc.highlight(23);
      explanation.highlight(6);

      /*
       * ========================================================================
       * ===================== Start Calculating Q; here we do the same as for r
       * and use the previous values
       * ============================================
       * =================================================
       */
      q[0] = 1;
      qi.put(0, 1, null, null);

      /*
       * ========================================================================
       * ===================== We initialize steplength, but it is not needed if
       * circleAnim is not wanted. Since we do not calculate q[0], we have to
       * animate it outside the loop. We also have to put in an additional step
       * for the animation.
       * ======================================================
       * =======================================
       */
      int steplength = 0;
      if (circleAnim) {
        steplength = getQStepLength(r, basisPowM);
        showQSteps(0, steplength);
        lang.nextStep(); // ----------------------------------- next step
                         // ---------------
      }
      for (int j = 1; j < m; j++) {
        explanation.unhighlight(8);
        sc.unhighlight(15);
        sc.unhighlight(16);
        sc.unhighlight(17);
        sc.unhighlight(21);

        sc.highlight(14);
        sc.highlight(21);
        explanation.highlight(7);

        q[j] = (q[j - 1] * basisPowM) % prime;
        qi.put(j, q[j], null, null);

        /*
         * ======================================================================
         * ======================= Only for the circle animation. For each step
         * we generate a new q circle segment with the given steplength.
         * ========
         * ==============================================================
         * =======================
         */
        if (circleAnim)
          showQSteps(j, steplength);

        lang.nextStep(); // ----------------------------------- next step
                         // ---------------
        qi.highlightCell(j, null, null);
        // For every calculation in q we increase the number of calucations of
        // the counter by 1!
        counter.assignmentsInc(1);

        for (int k = 0; k < m; k++) {
          sc.unhighlight(14);
          sc.unhighlight(22);
          explanation.unhighlight(7);

          sc.highlight(15);
          sc.highlight(16);
          sc.highlight(17);
          sc.highlight(20);
          explanation.highlight(8);
          ri.highlightCell(k, null, null);

          if (ri.getData(k) == qi.getData(j)) {
            result = (m * j + k) % prime;
            rNumber = k;
            qNumber = j;

            lang.nextStep(); // -------------------------- next step
                             // ---------------
            explanation.unhighlight(8);
            sc.unhighlight(20);
            sc.unhighlight(22);
            sc.highlight(17);
            sc.highlight(18);
            explanation.highlight(9);
            ri.highlightElem(k, null, null);

            break;
          }
          lang.nextStep(); // ------------------------------- next step
                           // ---------------
          ri.unhighlightCell(k, null, null);
          sc.unhighlight(19);
          sc.unhighlight(20);
          sc.unhighlight(21);
          sc.highlight(22);

        }
        // Stop, if result was set
        if (result > 0) {
          qi.highlightElem(j, null, null);
          sc.unhighlight(22);
          explanation.unhighlight(8);
          explanation.highlight(9);
          lang.nextStep();
          break;
        }
        qi.unhighlightCell(j, null, null);
      }
      view.hide();
      ri.hide();
      qi.hide();

      String rIst = "Unser gesuchtes r ist: " + rNumber;
      String qIst = "Unser gesuchtes q ist: " + qNumber;
      String ergebnis = "Somit ist das gesuchte x = " + m + "*" + qNumber
          + " + " + rNumber + " = " + result;
      showResult();
      lang.nextStep(); // ------------------------------- next step
                       // ---------------
      results.addCodeLine(rIst, null, 1, null);

      /*
       * ========================================================================
       * ===================== Only for the circle animation. We hide all
       * unnecessary r circleSegs.
       * ==============================================
       * ===============================================
       */
      if (circleAnim) {
        for (int i = 0; i < m; i++) {
          if (i == rNumber)
            continue;
          babySteps[i].hide();
        }
      }
      lang.nextStep(); // ------------------------------- next step
                       // ---------------
      results.addCodeLine(qIst, null, 1, null);

      /*
       * ========================================================================
       * ===================== Only for the circle animation. We hide all
       * unnecessary q circleSegs.
       * ==============================================
       * ===============================================
       */
      if (circleAnim) {
        for (int i = 0; i < qNumber; i++) {
          giantSteps[i].hide();
        }
      }
      lang.nextStep(); // ------------------------------- next step
                       // ---------------
      explanation.unhighlight(6);
      explanation.unhighlight(9);
      explanation.highlight(10);
      results.addCodeLine(ergebnis, null, 1, null);
    }

    lang.nextStep(); // ------------------------------- next step
                     // ---------------
    results.hide();
    explanation.hide();
    sc.hide();

    /*
     * ==========================================================================
     * =================== Only for the circle animation. We have to hide all
     * for the conclusion.
     * ======================================================
     * =======================================
     */
    if (circleAnim) {
      giantSteps[qNumber].hide();
      babySteps[rNumber].hide();
      circle.hide();
    }

    /*
     * ==========================================================================
     * =================== We now want to make some conclusion and start the
     * Questions section!
     * ========================================================
     * =====================================
     */
    header.setText("Zusammenfassung", null, null);
    showObservations();
    lang.nextStep("Zusammenfassung"); // -------------- next step
                                      // ---------------

    // If the user did not want any questions, we do not have to set them.
    if (questions) {
      MultipleChoiceQuestionModel q1 = new MultipleChoiceQuestionModel(
          "Operationen");
      q1.setPrompt("Durch die Benutzung von Hashtabellen kann man den Babystep-Giantstep Algorithmus"
          + "um einiges optimieren. Wie viele Rechenoperationen braucht der Algorithmus dann maximal?");
      q1.addAnswer("O(p)", 0,
          "Leider falsch! In einer zyklischen Primzahlgruppe ist das ausprobieren "
              + "von p Elementen das selbe wie ein Erzeugen aller Elemente.");
      q1.addAnswer("O(m)", 1,
          "Genau! Es werden maximal m Operationen benötigt.");
      lang.addMCQuestion(q1);

      lang.nextStep(); // ------------------------------- next step
      // ---------------

      MultipleChoiceQuestionModel q2 = new MultipleChoiceQuestionModel(
          "Vergleiche");
      q2.setPrompt("Der Babystep-Giantstep Algorithmus ist ein Time - Memory Tradeoff. "
          + "Wie hoch ist der Speicherbedarf?");
      q2.addAnswer(
          "O(2*m)",
          0,
          "Leider falsch! Es müssen lediglich die Elemente der Menge R gespeichert werden.");
      q2.addAnswer("O(m)", 1,
          "Genau! Die Menge R hat maximal m Elemente, die gespeichert werden müssen.");
      lang.addMCQuestion(q2);

      lang.nextStep(); // ------------------------------- next step
      // ---------------

      MultipleChoiceQuestionModel q3 = new MultipleChoiceQuestionModel(
          "UpperBound");
      q3.setPrompt("Muss die Gruppenordnung einer zyklischen Gruppe bekannt sein, damit der Algorithmus funktioniert?");
      q3.addAnswer(
          "Ja",
          0,
          "Leider falsch! Solange man m nicht zu gering wählt, terminiert der Algorithmus.");
      q3.addAnswer(
          "Nein",
          1,
          "Genau! Im Worst Case muss man allerdings sämtliche Lösungen ausprobieren und endet bei"
              + "einer Brute Force Variante.");
      lang.addMCQuestion(q3);

      lang.nextStep(); // ------------------------------- next step
                       // ---------------

      MultipleChoiceQuestionModel q4 = new MultipleChoiceQuestionModel(
          "Praktikabel");
      q4.setPrompt("Das El-Gamal Verschlüsselungsverfahren basiert auf der Schwierigkeit, DL-Probleme zu lösen."
          + "Angenommen jemand verwendet eine Primzahl der Größenordnung p = 2^50 zur Gruppenerzeugung. "
          + "Ist das darauf basierende El-Gamal Verfahren sicher?");
      q4.addAnswer(
          "Ja",
          0,
          "Leider falsch! Mit einer Gruppenordnung |G| von 2^50 kommt man auf ca. 2^25 Operationen. "
              + "Dies sind ca. 33 Millionen Operationen, die heutige Rechner in akzeptabler Zeit schaffen.");
      q4.addAnswer(
          "Nein",
          1,
          "Genau! Mit einer Gruppenordnung |G| von 2^50 kommt man auf ca. 2^25 Operationen."
              + "Dies sind ca. 33 Millionen Operationen, die heutige Rechner in akzeptabler Zeit schaffen.");
      lang.addMCQuestion(q4);
      lang.nextStep(); // ------------------------------- next step
                       // ---------------
    }

    /*
     * ==========================================================================
     * =================== If the user did not choose to have a circle
     * animation, we still want to show a simple example. Also this animation is
     * shown, when wrong values were set.
     * ========================================
     * =====================================================
     */
    if (!circleAnim) {
      observe.addCodeLine("", null, 0, null);
      observe
          .addCodeLine(
              "Im Folgenden ist nochmals eine Erklärung, wie man sich den Babystep-Giantstep Algorithmus",
              null, 1, null);
      observe.addCodeLine("innerhalb einer zyklischen Gruppe vorstellen kann:",
          null, 1, null);
      showCircle();
      circleText.highlight(0);

      lang.nextStep(); // ------------------------------- next step
                       // ---------------

      circleText.unhighlight(0);
      circleText.highlight(2);
      circleText.highlight(3);
      showBabySteps();

      lang.nextStep(); // ------------------------------- next step
                       // ---------------

      circleText.unhighlight(2);
      circleText.unhighlight(3);
      circleText.highlight(5);
      showGiantSteps();

      lang.nextStep(); // ------------------------------- next step
                       // ---------------

      circleText.unhighlight(5);
      circleSeg1.hide();
      circleSeg2.hide();
      circleSeg4.hide();
      giant1.hide();
      giant2.hide();
      circleText.highlight(7);
      circleText.highlight(8);
      lang.nextStep(); // ------------------------------- next step
                       // ---------------
    }

    lang.hideAllPrimitives();
    showFinal();

  }

  /**
   * A more general summary of the algorithm in 5 steps
   */
  private void showExplanation() {
    /*
     * ==========================================================================
     * =================== Only for the circle animation. We draw the circle if
     * a circleAnimation is wanted
     * ==============================================
     * ===============================================
     */
    if (circleAnim) {
      CircleProperties CircleProps = new CircleProperties();
      CircleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.DARK_GRAY);
      CircleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
      CircleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
      circle = lang.newCircle(new Coordinates(350, 350), 100,
          "zyklische Gruppe G", null, CircleProps);
    }

    explanation = lang.newSourceCode(new Coordinates(450, 40), "sourceCode",
        null, explanationProps);

    explanation.addCodeLine("Der Algorithmus besteht aus mehreren Schritten:",
        null, 0, null); // 0
    explanation.addCodeLine("", null, 0, null); // 1
    explanation
        .addCodeLine(
            "1. Bestimme ein m für das gilt: m = ceiling(sqrt(|G|)) . Hierbei ist |G| die Gruppenordnung.",
            null, 1, null); // 2
    explanation.addCodeLine(
        "2. x lässt sich als Linearkombination darstellen: x = m*q + r", null,
        1, null); // 3
    explanation
        .addCodeLine(
            "   Es gilt nun, q und r zu bestimmen. Für beide gilt: 0 <= q < m und 0 <= r < m .",
            null, 1, null); // 4
    explanation
        .addCodeLine(
            "3. Bestimme die Menge R = {(r, (y^(-r))*z) : 0 <= r < m}. Falls es ein Paar (r, 1) gibt, ist x = r .",
            null, 1, null); // 5
    explanation
        .addCodeLine(
            "4. Hat man nun alle Elemente der Menge R, so berechnet man die Menge Q:",
            null, 1, null); // 6
    explanation.addCodeLine("4.1 Q = {(q, (y^m)^q) : 0 <= q < m}", null, 2,
        null); // 7
    explanation
        .addCodeLine(
            "4.2 Für das berechnete Paar (q, (y^m)^q) sucht man ein (y^(-r))*z), sodass (y^m)^q = (y^(-r))*z) .",
            null, 2, null); // 8
    explanation
        .addCodeLine(
            "4.3 Existiert ein solches (y^(-r))*z), dann ist das aktuelle q und das r aus (y^(-r))*z) die gesuchten q und r.",
            null, 2, null); // 9
    explanation.addCodeLine("5. Bestimme x aus x = m*q + r ", null, 1, null); // 10
  }

  /**
   * Only for the circle animation. A Method for the Circle Representation of
   * the R steps.
   * 
   * @param length
   *          the number of elements of R
   */
  private void showRSteps(int length) {
    /*
     * ==========================================================================
     * =================== We start at 12 o'clock so we set the startAngle to
     * 90° We also choose one angle to be 10°, which represents the length of
     * one circle segment.
     * ======================================================
     * =======================================
     */
    int startAngle = 90;
    angle = 10;
    babySteps = new CircleSeg[length];
    giantSteps = new CircleSeg[length];

    CircleSegProperties BabyProps = new CircleSegProperties();
    BabyProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    BabyProps.set(AnimationPropertiesKeys.ANGLE_PROPERTY, angle);
    BabyProps.set(AnimationPropertiesKeys.CLOCKWISE_PROPERTY, true);
    BabyProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
    BabyProps.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, startAngle);

    /*
     * ==========================================================================
     * =================== We create an array of CircleSegs and hide them to
     * display them later. For each iteration we set the startAngle to 10°
     * further.
     * ==================================================================
     * ===========================
     */
    for (int i = 0; i < length; i++) {
      babySteps[i] = lang.newCircleSeg(new Coordinates(350, 350), 97, "r_" + i,
          null, BabyProps);
      babySteps[i].hide();
      startAngle = 10 * i + 90;
      BabyProps.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, startAngle);
    }
    /*
     * ==========================================================================
     * =================== We show the objects with a delay. Using this
     * workaround, because we could not find any more information about
     * DisplayOptions, but it works.
     * ============================================
     * =================================================
     */
    for (int j = 0; j < length; j++) {
      babySteps[j].show(new TicksTiming(10 * j));
    }
  }

  /**
   * Only for the circle animation. A method for the circle representation of
   * the Q steps.
   * 
   * @param number
   *          the i-th element we want to caluclate and set,
   * @param length
   *          the steplength of one q step.
   */
  private void showQSteps(int number, int length) {
    /*
     * ==========================================================================
     * =================== FIX ME: CLOCKWISE_PROPERTY goes counterclockwise!
     * Since we cannot use COUNTERCLOCKWISE_PROPERTY, we have to do a
     * workaround: We imagine the zero point at 12 o'clock. So we would like to
     * start at 30° from that which would be the same as the startAngle 60 , if
     * we were able to go counterclockwise (in case of Animalclocks, normally it
     * would be clock- wise ;-) ) So as an example for the first element the
     * goal is 60° . So we start at 60-i*steplength. Working on a circle is the
     * same as working in a modulo group with 360 as the generator. So the
     * startAngle we are looking for is at (60-i*steplength)%360 . Since the "%"
     * operator in java is not defined like modulo in mathematical terms and
     * gives us also negative values, we just do a workaround and add 3600 to
     * the value, which is the same as adding a zero to the whole value (so it
     * does nothing) but it prevents negative values. NOTE: We use the array
     * element number which starts at 0, so we have to add 1 to number. Also
     * instead of adding 3600 to 60, we just use 3660.
     * ==========================
     * ===================================================================
     */
    int startAngle = (3660 - ((number + 1) * length)) % 360;

    CircleSegProperties GiantProps = new CircleSegProperties();
    GiantProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    GiantProps.set(AnimationPropertiesKeys.ANGLE_PROPERTY, length);
    GiantProps.set(AnimationPropertiesKeys.CLOCKWISE_PROPERTY, true);
    GiantProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
    GiantProps.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, startAngle);
    giantSteps[number] = lang.newCircleSeg(new Coordinates(350, 350), 103, "q_"
        + number, null, GiantProps);
  }

  /**
   * A mathematical introduction and an explanation why this algorithm works
   */
  private void showIntroduction() {
    SourceCodeProperties IntroductionProps = new SourceCodeProperties();
    IntroductionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 12));
    IntroductionProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.BLACK);
    IntroductionProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

    introduction = lang.newSourceCode(new Coordinates(10, 40), "sourceCode",
        null, IntroductionProps);

    introduction
        .addCodeLine(
            "Herzlich Willkommen zur Lektion des Babystep Giantstep Algorithmus von Shanks!",
            null, 0, null); // 0
    introduction
        .addCodeLine(
            "Im Folgenden wird der mathematische Hintergrund des Algorithmus genauer erklärt.",
            null, 0, null); // 1
    introduction.addCodeLine("", null, 0, null); // 2
    introduction
        .addCodeLine(
            "Der Babystep-Giantstep Algorithmus ist ein Algorithmus, um diskrete Logarithmus (DL) Probleme schnell zu lösen.",
            null, 1, null); // 3
    introduction.addCodeLine(
        "Ein DL Problem ist meist durch folgende Gleichung gegeben:", null, 1,
        null); // 4
    introduction.addCodeLine("(z = y^x) modulo p", null, 2, null);
    introduction.addCodeLine(
        "Hierbei ist p eine Primzahl, welche die Primzahlgruppe G erzeugt.",
        null, 1, null);
    introduction
        .addCodeLine(
            "Der Babystep-Giantstep Algorithmus bestimmt nun für gegebene z,y und p das gesuchte x.",
            null, 1, null);
    introduction
        .addCodeLine(
            "Um das x zu finden, beschreibt man dieses durch eine geeignete Linearkombination:",
            null, 1, null);
    introduction.addCodeLine("x = m*q + r", null, 2, null);
    introduction
        .addCodeLine(
            "Anmerkung: Der Lesbarkeit halber lassen wir das 'modulo p' am Ende jeder Gleichung weg, ",
            null, 2, null);
    introduction
        .addCodeLine(
            "allerdings muss dieses immer dazugedacht werden, da wir uns immer in der zyklischen Gruppe G befinden.",
            null, 2, null);
    introduction.addCodeLine("", null, 0, null);
    introduction.addCodeLine(
        "Das m bestimmt sich durch: m = ceiling(sqrt(|G|)) ", null, 1, null);
    introduction
        .addCodeLine(
            "Für die Gruppenordnung |G| gilt bei zyklischen Primzahlgruppen: |G| = p-1",
            null, 1, null);
    introduction.addCodeLine("", null, 0, null);
    introduction.addCodeLine("Nun gilt für obige Gleichung: z = y^(m*q + r)",
        null, 1, null);
    introduction.addCodeLine("Was äquivalent ist zu: z = y^(m*q) * y^r", null,
        1, null);
    introduction
        .addCodeLine(
            "Somit lässt sich die Gleichung umformen zu: z*y^(-r) = y^(m*q)      (*)",
            null, 1, null);
    introduction.addCodeLine("", null, 0, null);
    introduction
        .addCodeLine(
            "Man bestimmt nun zuerst die Menge der Babysteps. Diese ist gegeben durch: R = {(r, z*y^(-r)) : 0 <= r < m}",
            null, 1, null);
    introduction
        .addCodeLine(
            "Findet man ein Paar (r, 1) dann ist das Problem bereits gelöst und es gilt x = r.",
            null, 1, null);
    introduction.addCodeLine("", null, 0, null);
    introduction
        .addCodeLine(
            "Ansonsten bestimmt man die Menge der Giantsteps. Für diese gilt nun: Q = {(q, y^(m*q)) : 0 <= q < m}",
            null, 1, null);
    introduction
        .addCodeLine(
            "Nach der Bestimmung eines Paares (q, y^(m*q)) sucht man in der Menge R ein Element, für das die Gleichung (*) erfüllt ist.",
            null, 1, null);
    introduction
        .addCodeLine(
            "Hat man dieses gefunden, so kann man sich nun das x mit den jeweils passenden q und r berechnen.",
            null, 1, null);
    introduction.addCodeLine("", null, 0, null);
    introduction
        .addCodeLine(
            "Der Zeit- und Speicheraufwand des Babystep-Giantstep Algorithmus befindet sich in der Größenordnung O(sqrt(|G|)).",
            null, 1, null);
  }

  /**
   * The pseudocode for the animation
   */
  private void showSourceCode() {
    sc = lang.newSourceCode(new Coordinates(10, 40), "sourceCode", null,
        scProps);

    sc.addCodeLine("Pseudocode:", null, 0, null); // 0
    sc.addCodeLine("public int babystepGiantstep(int y, int z, int p){", null,
        0, null); // 1
    sc.addCodeLine("int m = ceil(sqrt(p-1));", null, 1, null); // 2
    sc.addCodeLine("int[] r = new int[m];", null, 1, null); // 3
    sc.addCodeLine("int[] q = new int[m];", null, 1, null); // 4
    sc.addCodeLine("for (i = 0; i < m; ){", null, 1, null); // 5
    sc.addCodeLine("r[i] = ((y^(-i))*z)%p;", null, 2, null); // 6
    sc.addCodeLine("if(r[i]==1){", null, 2, null);
    sc.addCodeLine("int result = i;", null, 3, null);
    sc.addCodeLine("break", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("i++;", null, 2, null); // 7
    sc.addCodeLine("}", null, 1, null); // 8
    sc.addCodeLine("for (j = 0; j < m; ){", null, 1, null); // 9
    sc.addCodeLine("q[i] = ((y^m)^q)%p;", null, 2, null); // 10
    sc.addCodeLine("for ( k = 0; k < m; ){", null, 2, null); // 11
    sc.addCodeLine("if( q[i] == r[k] ){", null, 3, null); // 12
    sc.addCodeLine("int result = m*i + k;", null, 4, null); // 13
    sc.addCodeLine("break;", null, 4, null); // 14
    sc.addCodeLine("}", null, 3, null); // 15
    sc.addCodeLine("k ++;", null, 4, null); // 16
    sc.addCodeLine("}", null, 2, null); // 17
    sc.addCodeLine("j ++;", null, 2, null); // 18
    sc.addCodeLine("}", null, 1, null); // 19
    sc.addCodeLine("}", null, 0, null); // 20
  }

  /**
   * When we are done calculating the arrays q and r, we can calculate the final
   * result
   */
  private void showResult() {
    SourceCodeProperties resultProps = new SourceCodeProperties();
    resultProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
    resultProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 12));
    resultProps
        .set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
    resultProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    results = lang.newSourceCode(new Coordinates(550, 300), "sourceCode", null,
        resultProps);

    results.addCodeLine("Die Ergebnisse sind in diesem Beispiel: ", null, 0,
        null); // 0
    results.addCodeLine("", null, 0, null); // 1
  }

  /**
   * After the example we give another point of view for the algorithm, so we
   * generate some Text
   */
  private void showObservations() {
    SourceCodeProperties ObserveProps = new SourceCodeProperties();
    ObserveProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 14));
    ObserveProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.BLACK);
    ObserveProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    observe = lang.newSourceCode(new Coordinates(10, 40), "sourceCode", null,
        ObserveProps);
    observe
        .addCodeLine(
            "Dies war nun ein ausführliches Beispiel des Babystep-Giantstep Algorithmus.",
            null, 0, null); // 0
    observe.addCodeLine("", null, 0, null); // 1
  }

  /**
   * The explanation of the circle, which is to be shown at the end. Also the
   * circle itself.
   */
  private void showCircle() {

    SourceCodeProperties CircleTextProps = new SourceCodeProperties();
    CircleTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 14));
    CircleTextProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.BLUE);
    CircleTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    circleText = lang.newSourceCode(new Coordinates(450, 200), "sourceCode",
        null, CircleTextProps);

    circleText.addCodeLine(
        "Dies ist die graphische Darstellung der zyklischen Gruppe.", null, 0,
        null); // 0
    circleText.addCodeLine("", null, 0, null); // 1
    circleText.addCodeLine(
        "Durch die Babysteps geht man kleine Schritte innerhalb der Gruppe G.",
        null, 0, null); // 2
    circleText
        .addCodeLine(
            "Wie bereits erklärt, bestimmen sich die Babysteps mit der Formel: y^(-r))*z modulo p",
            null, 0, null); // 3
    circleText.addCodeLine("", null, 0, null); // 4
    circleText
        .addCodeLine(
            "Ist R bestimmt, so kann man nun mit großen Schritten (Giantsteps) vorangehen.",
            null, 0, null); // 5
    circleText.addCodeLine("", null, 0, null); // 6
    circleText
        .addCodeLine(
            "Zeigen nun ein Element aus R und ein Element aus Q auf das gleiche Element der Gruppe G,",
            null, 0, null); // 7
    circleText
        .addCodeLine(
            "so haben wir das passende q und r gefunden und können das x bestimmen",
            null, 0, null); // 8

    CircleProperties CircleProps = new CircleProperties();
    CircleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.DARK_GRAY);
    CircleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
    CircleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    circle = lang.newCircle(new Coordinates(300, 300), 100,
        "zyklische Gruppe G", null, CircleProps);
  }

  /**
   * For the visualization of the baby steps
   */
  private void showBabySteps() {
    /*
     * ==========================================================================
     * =================== For the CircleSegeProperties: Somehow
     * COUNTERCLOCKWISE_PROPERTY is bugged! Only use CLOCKWISE_PROPERTY!!!
     * ======
     * ====================================================================
     * ===================
     */
    CircleSegProperties Baby1 = new CircleSegProperties();
    Baby1.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    Baby1.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, 90);
    Baby1.set(AnimationPropertiesKeys.ANGLE_PROPERTY, 10);
    Baby1.set(AnimationPropertiesKeys.CLOCKWISE_PROPERTY, true);
    Baby1.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);

    CircleSegProperties Baby2 = new CircleSegProperties();
    Baby2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.MAGENTA);
    Baby2.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, 100);
    Baby2.set(AnimationPropertiesKeys.ANGLE_PROPERTY, 10);
    Baby2.set(AnimationPropertiesKeys.CLOCKWISE_PROPERTY, true);
    Baby2.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);

    CircleSegProperties Baby3 = new CircleSegProperties();
    Baby3.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    Baby3.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, 110);
    Baby3.set(AnimationPropertiesKeys.ANGLE_PROPERTY, 10);
    Baby3.set(AnimationPropertiesKeys.CLOCKWISE_PROPERTY, true);
    Baby3.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);

    CircleSegProperties Baby4 = new CircleSegProperties();
    Baby4.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.MAGENTA);
    Baby4.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, 120);
    Baby4.set(AnimationPropertiesKeys.ANGLE_PROPERTY, 10);
    Baby4.set(AnimationPropertiesKeys.CLOCKWISE_PROPERTY, true);
    Baby4.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);

    circleSeg1 = lang.newCircleSeg(new Coordinates(300, 300), 97, "Babystep1",
        null, Baby1);
    lang.nextStep(); // ------------------------------- next step
                     // ---------------
    circleSeg2 = lang.newCircleSeg(new Coordinates(300, 300), 97, "Babystep2",
        null, Baby2);
    lang.nextStep(); // ------------------------------- next step
                     // ---------------
    lang.newCircleSeg(new Coordinates(300, 300), 97, "Babystep3", null, Baby3);
    lang.nextStep(); // ------------------------------- next step
                     // ---------------
    circleSeg4 = lang.newCircleSeg(new Coordinates(300, 300), 97, "Babystep4",
        null, Baby4);
  }

  /**
   * For the visualization of the giant steps
   */
  private void showGiantSteps() {
    /*
     * ==========================================================================
     * =================== Since we can only go clockwise, we have to start the
     * other way around.
     * ========================================================
     * =====================================
     */
    CircleSegProperties Giant1 = new CircleSegProperties();
    Giant1.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    Giant1.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, 320);
    Giant1.set(AnimationPropertiesKeys.ANGLE_PROPERTY, 100);
    Giant1.set(AnimationPropertiesKeys.CLOCKWISE_PROPERTY, true);
    Giant1.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);

    CircleSegProperties Giant2 = new CircleSegProperties();
    Giant2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.CYAN);
    Giant2.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, 220);
    Giant2.set(AnimationPropertiesKeys.ANGLE_PROPERTY, 100);
    Giant2.set(AnimationPropertiesKeys.CLOCKWISE_PROPERTY, true);
    Giant2.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);

    CircleSegProperties Giant3 = new CircleSegProperties();
    Giant3.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    Giant3.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, 120);
    Giant3.set(AnimationPropertiesKeys.ANGLE_PROPERTY, 100);
    Giant3.set(AnimationPropertiesKeys.CLOCKWISE_PROPERTY, true);
    Giant3.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);

    giant1 = lang.newCircleSeg(new Coordinates(300, 300), 103, "Giantstep1",
        null, Giant1);
    lang.nextStep(); // ------------------------------- next step
                     // ---------------
    giant2 = lang.newCircleSeg(new Coordinates(300, 300), 103, "Giantstep2",
        null, Giant2);
    lang.nextStep(); // ------------------------------- next step
                     // ---------------
    lang.newCircleSeg(new Coordinates(300, 300), 103, "Giantstep3", null,
        Giant3);
  }

  /**
   * The final Text to be shown, when all the stuff is done
   */
  private void showFinal() {
    SourceCodeProperties FinalProps = new SourceCodeProperties();
    FinalProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
    FinalProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 15));
    FinalProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
    FinalProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    SourceCode finalText = lang.newSourceCode(new Coordinates(100, 100),
        "sourceCode", null, FinalProps);

    finalText.addCodeLine("Herzlichen Glückwunsch! ", null, 0, null); // 0
    finalText
        .addCodeLine(
            "Hiermit haben sie die Lektion zum Babystep-Giantstep Algorithmus von Shanks abgeschlossen.",
            null, 0, null); // 1
  }

  /*
   * ============================================================================
   * ================= Main Method for only testing public static void
   * main(String[] args) { String output = bg.generate(null, null); String
   * asuPath = "D:\\Uni\\test.asu"; try (FileWriter fw = new
   * FileWriter(asuPath)) { fw.write(output); } catch (IOException e) {
   * e.printStackTrace(); } }
   * ====================================================
   * =========================================
   */

}