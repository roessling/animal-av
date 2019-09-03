package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.OffsetFromLastPosition;

public class FermatFact implements ValidatingGenerator {
  /* introduction */
  private SourceCode introductionV;
  private SourceCode introductionV2;
  private SourceCode endingV;
  private SourceCode codeV;
  private Language   lang;

  public boolean validateInput(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {
    int N = (Integer) primitives.get("N");
    System.out.println(N);
    if (N > 1000)
      throw new IllegalArgumentException(
          "WARNUNG! Die Zahl muss kleiner als 1000!");
    else if (N % 2 == 0)
      throw new IllegalArgumentException(
          "WARNUNG! Die Zahl muss ungerade sein!");
    else
      return true;
  }

  public void init() {
    lang = new AnimalScript("Fermat Faktorisierungsmethode",
        "Bastian de Groot, Magnus Brand", 1000, 600);
  }

  private static boolean isSquare(long x) {
    long root = (long) Math.sqrt(x);
    return root * root == x;
  }

  // private static void fermat(long n) {
  // long a = (long)Math.ceil(Math.sqrt(n));
  // long b2 = (long)Math.ceil(Math.pow(a, 2) - n);
  //
  // while(!isSquare(b2)) {
  // b2 += 2 * a + 1;
  // a++;
  // }
  // }

  private int oldLineNumber;

  /* highlights only line number and inserts a step */
  private void breakHL(int lno) {
    codeV.unhighlight(oldLineNumber);
    codeV.highlight(lno);
    oldLineNumber = lno;
    lang.nextStep();
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    int N = (Integer) primitives.get("N");
    // ArcProperties arc = (ArcProperties)props.getPropertiesByName("arc");
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    SourceCodeProperties textProps = new SourceCodeProperties();

    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 15));
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 24));
    headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    Text header = lang.newText(new Coordinates(20, 30),
        "Fermat Faktorisierungsmethode", "header", null, headerProps);
    RectProperties headerRectProps = new RectProperties();
    headerRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
    headerRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    headerRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    Rect headerRect = lang.newRect(new Offset(-5, -5, header, "NW"),
        new Offset(5, 5, header, "SE"), "headerRect", null, headerRectProps);

    RectProperties tableRectProps = new RectProperties();
    headerRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
    headerRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    headerRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    TextProperties miniProps = new TextProperties();
    miniProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 10));
    miniProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    /* sub header */
    TextProperties subheaderProps = new TextProperties();
    subheaderProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 18));
    subheaderProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    Text subHeader = lang.newText(new Offset(0, 60, headerRect, "NW"),
        "Einleitung", "subheader", null, subheaderProps);
    /* code format */
    // SourceCodeProperties codeProps = new SourceCodeProperties("CodeFarben");
    SourceCodeProperties codeProps = (SourceCodeProperties) props
        .getPropertiesByName("CodeFarben");
    // TextProperties actionColor = (TextProperties)
    // props.getPropertiesByName("ActionDisplay");
    TextProperties actionProps = (TextProperties) props
        .getPropertiesByName("ActionDisplay");
    // Font f = (Font)actionProps.get(AnimationPropertiesKeys.FONT_PROPERTY);
    // String fontName = f.getFontName();
    actionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 18));

    // codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new
    // Font("Monospaced", Font.PLAIN, 12));
    // codeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
    // Color.RED);
    // codeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    introductionV = lang.newSourceCode(new OffsetFromLastPosition(0, 60),
        "sourceCode", null, textProps);
    for (String l : description1)
      introductionV.addCodeLine(l, null, 0, null);

    lang.nextStep();
    subHeader.hide();
    introductionV.hide();
    addQuestionAnswer(0);

    Text subHeader2 = lang.newText(new Offset(0, 60, headerRect, "NW"),
        "Erklärung", "subheader2", null, subheaderProps);

    introductionV2 = lang.newSourceCode(new OffsetFromLastPosition(0, 60),
        "sourceCode", null, textProps);
    for (String l : description2)
      introductionV2.addCodeLine(l, null, 0, null);

    lang.nextStep();
    subHeader2.hide();
    introductionV2.hide();

    addQuestionAnswer(1);
    long n = (long) N;
    // long n = 219; for testing

    Text subHeader3 = lang
        .newText(new Offset(0, 60, headerRect, "NW"),
            "Beispiel n = " + String.valueOf(n), "subheader3", null,
            subheaderProps);

    codeV = lang.newSourceCode(new Offset(0, 60, subHeader3, "NW"),
        "sourceCode", null, codeProps);
    for (String l : code)
      codeV.addCodeLine(l, null, 0, null);

    Text actionText = lang.newText(new Offset(500, 60, headerRect, "NW"),
        " ------  ", "actionText", null, actionProps);
    Text aText = lang.newText(new Offset(500, 100, headerRect, "NW"), "a", "a",
        null, subheaderProps);
    Text b2Text = lang.newText(new Offset(40, 0, aText, "NE"), "b2", "b2",
        null, subheaderProps);
    Rect tableRectHorizontal = lang.newRect(new Offset(-5, 5, aText, "SW"),
        new Offset(20, 6, b2Text, "SE"), "headerRect", null, tableRectProps);
    Rect tableRectVertical = lang.newRect(new Offset(-5, -5, b2Text, "SW"),
        new Offset(-4, 400, b2Text, "SW"), "headerRect", null, tableRectProps);
    lang.nextStep();

    ArrayList<Text> aTextList = new ArrayList<Text>();
    ArrayList<Text> b2TextList = new ArrayList<Text>();
    ArrayList<Text> squareTextList = new ArrayList<Text>();
    int listCounter = 0;
    long a = (long) Math.ceil(Math.sqrt(n));
    aTextList.add(lang.newText(new Offset(0, 30, aText, "NW"),
        String.valueOf(a), "a" + String.valueOf(listCounter), null,
        subheaderProps));
    actionText.hide();
    actionText = lang.newText(new Offset(500, 60, headerRect, "NW"), "a = ⌈√"
        + String.valueOf(n) + "⌉", "actionText", null, actionProps);
    breakHL(1);
    long b2 = (long) Math.ceil(Math.pow(a, 2) - n);
    actionText.hide();
    actionText = lang.newText(new Offset(500, 60, headerRect, "NW"), "b2 = "
        + String.valueOf(a) + "² - " + String.valueOf(n), "actionText", null,
        actionProps);
    b2TextList.add(lang.newText(new Offset(0, 30, b2Text, "NW"),
        String.valueOf(b2), "b2_" + String.valueOf(listCounter), null,
        subheaderProps));
    breakHL(2);
    listCounter++;

    while (!isSquare(b2)) {
      actionText.hide();
      actionText = lang.newText(new Offset(500, 60, headerRect, "NW"), "√"
          + String.valueOf(b2) + " = "
          + String.valueOf((double) Math.sqrt(b2)).replace(",", "-"),
          "actionText", null, actionProps);
      squareTextList.add(lang.newText(
          new Offset(80, 0, b2TextList.get(listCounter - 1), "NW"),
          "→ keine Quadratzahl", "sqrt" + String.valueOf(listCounter), null,
          miniProps));
      breakHL(3);

      if (listCounter == 10) {
        for (int i = 0; i < 10; i++) {
          aTextList.get(i).hide();
          b2TextList.get(i).hide();
          // if i < 9:
          squareTextList.get(i).hide();
        }
        aTextList.clear();
        b2TextList.clear();
        squareTextList.clear();
        listCounter = 0;
      }
      a++;
      b2 = a * a - n;
      actionText.hide();
      actionText = lang.newText(new Offset(500, 60, headerRect, "NW"), "a = "
          + String.valueOf(a - 1) + " + 1", "actionText", null, actionProps);
      if (listCounter == 0)
        aTextList.add(lang.newText(new Offset(0, 30, aText, "NW"),
            String.valueOf(a), "a" + String.valueOf(listCounter), null,
            subheaderProps));
      else
        aTextList.add(lang.newText(
            new Offset(0, 30, aTextList.get(listCounter - 1), "NW"),
            String.valueOf(a), "a" + String.valueOf(listCounter), null,
            subheaderProps));
      breakHL(4);
      actionText.hide();
      actionText = lang.newText(new Offset(500, 60, headerRect, "NW"), "b2 = "
          + String.valueOf(a) + "² - " + String.valueOf(n), "actionText", null,
          actionProps);
      if (listCounter == 0)
        b2TextList.add(lang.newText(new Offset(0, 30, b2Text, "NW"),
            String.valueOf(b2), "b2_" + String.valueOf(listCounter), null,
            subheaderProps));
      else
        b2TextList.add(lang.newText(
            new Offset(0, 30, b2TextList.get(listCounter - 1), "NW"),
            String.valueOf(b2), "b2_" + String.valueOf(listCounter), null,
            subheaderProps));
      breakHL(5);
      listCounter++;
      // lang.nextStep();
    }
    actionText.hide();
    actionText = lang.newText(new Offset(500, 60, headerRect, "NW"),
        "√" + String.valueOf(b2) + " = "
            + String.valueOf(Math.sqrt(b2)).replace(",", "-"), "actionText",
        null, actionProps);
    // squareTextList.add(lang.newText(new Offset(40, 0,
    // b2TextList.get(listCounter - 1), "NW"), "→ keine Quadratzahl", "sqrt" +
    // String.valueOf(listCounter), null, miniProps));
    squareTextList.add(lang.newText(
        new Offset(60, 0, b2TextList.get(listCounter - 1), "NW"),
        "→ Quadratzahl", "sqrt" + String.valueOf(listCounter), null,
        subheaderProps));
    breakHL(3);

    Text t1 = lang.newText(new Offset(0, 60, codeV, "SW"),
        "t1 = " + String.valueOf(a) + " - √" + String.valueOf(b2) + " = "
            + String.valueOf((long) (a - Math.sqrt(b2))), "t1", null,
        subheaderProps);
    Text t2 = lang.newText(new Offset(0, 15, t1, "SW"),
        "t2 = " + String.valueOf(a) + " + √" + String.valueOf(b2) + " = "
            + String.valueOf((long) (a + Math.sqrt(b2))), "t2", null,
        subheaderProps);
    Text nText = lang.newText(new Offset(0, 15, t2, "SW"),
        " n = t1 * t2 = " + String.valueOf((long) (a - Math.sqrt(b2))) + " * "
            + String.valueOf((long) (a + Math.sqrt(b2))), "n", null,
        subheaderProps);
    breakHL(6);

    for (int i = 0; i < aTextList.size(); i++) {
      aTextList.get(i).hide();
      b2TextList.get(i).hide();
      squareTextList.get(i).hide();
    }
    codeV.hide();
    actionText.hide();
    aText.hide();
    b2Text.hide();
    tableRectVertical.hide();
    tableRectHorizontal.hide();
    nText.hide();
    t1.hide();
    t2.hide();
    subHeader3.hide();

    Text subHeader4 = lang.newText(new Offset(0, 60, headerRect, "NW"),
        "Abschlussbemerkungen", "subheader4", null, subheaderProps);

    endingV = lang.newSourceCode(new Offset(0, 60, subHeader4, "NW"), "Ending",
        null, codeProps);
    for (String l : ending)
      endingV.addCodeLine(l, null, 0, null);
    lang.nextStep();

    lang.finalizeGeneration();
    return lang.toString();
  }

  private void addQuestionAnswer(int n) {
    MultipleChoiceQuestionModel[] qas = new MultipleChoiceQuestionModel[2];

    MultipleChoiceQuestionModel q1 = new MultipleChoiceQuestionModel(
        "Eigenschaft");
    q1.setPrompt("Welche Eigenschaft nutzt das Fermat Verfahren aus?");
    q1.addAnswer(
        "Es nutz die Eigenschaft, dass ungerade Zahlen als Produkt von zwei Quadraten dargestellt werden kann. a² * b² = n",
        0, "Leider falsch.");
    q1.addAnswer(
        "Es nutz die Eigenschaft, dass ungerade Zahlen als Differenz von zwei Quadraten dargestellt werden kann. a² - b² = n",
        1, "Das ist richtig.");
    qas[0] = q1;

    MultipleChoiceQuestionModel q2 = new MultipleChoiceQuestionModel(
        "Transformation");
    q2.setPrompt("Für das Finden der Teiler wird im letzten Schritt welche binomische Formel angewendet?");
    q2.addAnswer("Die 1. binomische Formel.", 0, "Leider falsch.");
    q2.addAnswer("Die 3. binomische Formel.", 1, "Richtig");
    q2.addAnswer("Die 4. binomische Formel.", 0,
        "Leider falsch. Es gibt keine vierte binomische Formel.");
    qas[1] = q2;

    /* attach */
    lang.addMCQuestion(qas[n]);

  }

  public String getName() {
    return "Fermat Faktorisierungsmethode";
  }

  public String getAlgorithmName() {
    return "Fermat Faktorisierungsmethode";
  }

  public String getAnimationAuthor() {
    return "Bastian de Groot, Magnus Brand";
  }

  private final String[] description1 = {
      "Die Faktorisierungsmethode von Fermat ist ein Algorithmus zur Teilerfindung bei ungeraden ganzen",
      "Zahlen. Das Verfahren wurde 1624 von Pierre de Fermat beschrieben. Heutige Verfahren zum Finden",
      "von Teilern, basieren auf der Faktorisierungsmethode von Fermat. Es nutzt die Eigenschaft",
      "ungerader Zahlen, dass sie auch als Differenz von Quadraten zweier anderer Zahlen dargestellt ",
      "werden kann:", "n = a² - b²"  };

  private final String[] description2 = {
      "Ausgangsformel: n = a² - b²",
      "a² - n = b²",
      "Es wird anfangend von der Wurzel der Zahl, ein Teiler gesucht",
      "(⌈√n⌉ + i)² - n = a² - n = b²",
      "i = {0, 1, 2, ... , n/2 - ⌈√n⌉}",
      "Falls die folgende Formel eine Quadratzahl (b²) hervorbringt, haben wir eine Lösung gefunden:",
      "(⌈√n⌉ + i)² - n",
      "Die Teiler (t1, t2) lassen sich mit dem Umformen der Ausgangsformel",
      "nach der 3. binomischen Formel herausfinden:",
      "n = a² + b² = (a + b)(a - b)", "t1 = (a + b)", "t2 = (a - b)" };

  public String getDescription() {
    String d = "";
    for (String l : description1)
      d += l + "\n";
    // for (String l : description2)
    // d += l + "\n";
    return d;
  }

  public String getCodeExample() {
    String d = "";
    for (String l : code)
      d += l + "\n";
    return d;
  }

  private final String[] ending = {
      "Abschließend ist zu sagen, dass die Fermat Faktorisierungsmethode",
      "nicht sehr effizient ist. Besonders Primzahlen sind sehr langsam",
      "bei der Faktorisierung. Dennoch sollte man die Funktionsweise",
      "dieses Algorithmus verstehen, denn aktuelle Faktorisierungsverfahren",
      "wie das general number field sieve basieren darauf." };

  private final String[] code   = { "def fermat(n):",
      "        a = int(math.ceil(math.sqrt(n)))",
      "        b2 = int((a ** 2) - n)", "        while not is_square(b2):",
      "                a += 1", "                b2 = a * a - n",
      "        return a - math.sqrt(b2), a + math.sqrt(b2)" };

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
  }

  public String getOutputLanguage() {
    return "Python";
  }

  // public static void main(String[] _) {
  // FermatFact gen = new FermatFact();
  // gen.init();
  // Hashtable n = new Hashtable();
  // n.put("N", 231);
  // AnimationPropertiesContainer propsCont = new
  // AnimationPropertiesContainer();
  //
  // try {
  // Writer fw = new OutputStreamWriter(new FileOutputStream(
  // "DPGOutput.asu"), Charset.forName("UTF-8"));
  // fw.write(gen.generate(propsCont, n));
  // } catch (IOException e) {
  // System.err.println("could not write to file");
  // }
  // }

}
