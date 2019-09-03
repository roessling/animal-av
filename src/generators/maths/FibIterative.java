package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class FibIterative implements Generator {
  private Language             lang;
//  private SourceCodeProperties sourceCode;
//  private Color                HighlightColor;
  private boolean              xorswap;
  private int                  n;

  public void init() {
    lang = new AnimalScript("Fibonacci Iterativ [DE]",
        "Lars Schulte, Marius Diebel", 800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    lang.setStepMode(true);
//    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
//    HighlightColor = (Color) primitives.get("HighlightColor");
    xorswap = (Boolean) primitives.get("xorswap");
    n = (Integer) primitives.get("n");

    start(n, xorswap);

    return lang.toString();
  }

  public String getName() {
    return "Fibonacci Iterativ [DE]";
  }

  public String getAlgorithmName() {
    return "Fibonacci";
  }

  public String getAnimationAuthor() {
    return "Lars Schulte, Marius Diebel";
  }

  public String getDescription() {
    return "Die Fibonaccifolge ist eine rekursiv definierte Funktion, da jede Fibonacci Zahl die Summe"
        + "\n"
        + "der beiden vorhergehenden Fibonacci Zahlen ist."
        + "\n"
        + "Entsprechend beginnt der iterative Algorithmus mit F(0) = 0 und F(1) = 1	"
        + "\n"
        + "und addiert diese um den nächsten Funktionswert zu berechnen jeweils iterativ auf."
        + "\n"
        + "\n"
        + "Gegenüber einer rekursiven Implementierung ist der iterative Ansatz dabei wesentlich effizienter,"
        + "\n"
        + "da viele redudante Berechnungsschritte entfallen."
        + "\n"
        + "						";
  }

  public String getCodeExample() {
    return "int Fibonacci(int n)" + "\n" + "{" + "\n" + "   int f1 = 0;" + "\n"
        + "   int f2 = 1;" + "\n" + "   int fn;" + "\n"
        + "   for ( int i = 2; i <=n; i++ )" + "\n" + "   {" + "\n"
        + "      fn = f1 + f2;" + "\n" + "      f1 = f2;" + "\n"
        + "      f2 = fn;" + "\n" + "   }" + "\n" + "   return fn;" + "\n"
        + "}  ";
  }

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
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  public void start(int n, boolean xorswap) {
    int f1 = 0;
    int f2 = 1;
    int fn = 1;

    Timing time = new TicksTiming(50);

    TextProperties tp2 = new TextProperties();
    TextProperties tp3 = new TextProperties();

    tp2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SANSSERIF",
        Font.PLAIN, 16));
    tp3.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SANSSERIF",
        Font.BOLD, 16));

    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    lang.newText(new Coordinates(20, 30), "Fibonacci Iterativ", "header", null,
        headerProps);
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", "SE"), "hRect", null, rectProps);

    lang.nextStep();
    Text description1 = lang
        .newText(
            new Coordinates(160, 50),
            "Die Fibonaccifolge ist eine rekursiv definierte Funktion, da jede Fibonacci Zahl die Summe",
            "description1", null, tp2);
    Text description2 = lang.newText(new Offset(0, 20, description1, null),
        "der beiden vorhergehenden Fibonacci Zahlen ist.", "description2",
        null, tp2);
    Text description3 = lang.newText(new Offset(0, 20, description2, null),
        "Die einzigen festgelegten Zahlen sind die F(0) = 0 und F(1) = 1.",
        "description3", null, tp2);
    Text description4 = lang
        .newText(
            new Offset(0, 20, description3, null),
            "Entsprechend beginnt der iterative Algorithmus mit F(0) = 0 und F(1) = 1.",
            "description4", null, tp2);
    Text description5 = lang
        .newText(
            new Offset(0, 20, description4, null),
            "und addiert diese um den nächsten Funktionswert zu berechnen jeweils iterativ auf.",
            "description5", null, tp2);

    lang.nextStep();
    description1.setText("", null, null);
    description2.setText("", null, null);
    description3.setText("", null, null);
    description4.setText("", null, null);
    description5.setText("", null, null);

    Text codeHeader = lang.newText(new Coordinates(10, 120), "Pseudocode",
        "CodeHeader", null, tp2);

    SourceCodeProperties sourceCodeProps = new SourceCodeProperties();
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);

    algoanim.primitives.SourceCode src = lang.newSourceCode(new Offset(0, 10,
        codeHeader, "S"), "code", null, sourceCodeProps);
    if (!xorswap) {
      src.addCodeLine("Fibonacci(int n)", null, 0, null); // 0
      src.addCodeLine("int f1 = 0;", null, 1, null); // 1
      src.addCodeLine("int f2 = 1;", null, 1, null); // 2
      src.addCodeLine("int fn;", null, 1, null); // 3
      src.addCodeLine("for ( int i = 2; i <= n; i++ ){", null, 1, null); // 4
      src.addCodeLine("fn = f1 + f2;", null, 2, null); // 5
      src.addCodeLine("f1 = f2;", null, 2, null); // 6
      src.addCodeLine("f2 = fn;", null, 2, null); // 7
      src.addCodeLine("}", null, 1, null); // 8
      src.addCodeLine("return fn;", null, 1, null); // 9
    } else {
      src.addCodeLine("Fibonacci(int n)", null, 0, null); // 0
      src.addCodeLine("int f1 = 0;", null, 1, null); // 1
      src.addCodeLine("int f2 = 1;", null, 1, null); // 2
      src.addCodeLine("for ( int i = 2; i <= n; i++ ){", null, 1, null); // 3
      src.addCodeLine("f1 = f1 + f2;", null, 2, null); // 4
      src.addCodeLine("f1 = f1 xor f2;", null, 2, null); // 5
      src.addCodeLine("f2 = f1 xor f2;", null, 2, null); // 6
      src.addCodeLine("f1 = f1 xor f2;", null, 2, null); // 7
      src.addCodeLine("}", null, 1, null); // 8
      src.addCodeLine("return f2;", null, 1, null); // 9
    }

    lang.nextStep();
    // Variablen initialisieren - f1
    Text flabel1 = lang.newText(new Coordinates(360, 180), "f1", "flabel1",
        null, tp3);

    Text fdescription1 = lang.newText(new Offset(0, 25, flabel1, null), "0",
        "fdescription1", null, tp3);
    lang.newRect(new Offset(-5, -5, fdescription1, "NW"), new Offset(5, 5,
        fdescription1, "SE"), "fRect1", null);

    src.highlight(1);
    fdescription1.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, null,
        null);
    fdescription1.setText("0", null, null);

    lang.nextStep();
    // f2 initialisieren
    src.unhighlight(1);
    fdescription1.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK,
        null, null);

    Text flabel2 = lang.newText(new Coordinates(420, 180), "f2", "flabel2",
        null, tp3);
    Text fdescription2 = lang.newText(new Offset(0, 25, flabel2, null), "1",
        "fdescription2", null, tp3);
    lang.newRect(new Offset(-5, -5, fdescription2, "NW"), new Offset(5, 5,
        fdescription2, "SE"), "fRect2", null);

    src.highlight(2);
    fdescription2.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, null,
        null);

    if (!xorswap) {
      // initialize fn
      lang.nextStep();
      src.unhighlight(2);
      fdescription2.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK,
          null, null);
      src.highlight(3);

      Text flabel3 = lang.newText(new Coordinates(480, 180), "fn", "flabel3",
          null, tp3);
      Text fdescription3 = lang.newText(new Offset(0, 25, flabel3, null),
          "   ", "fdescription3", null, tp3);
      lang.newRect(new Offset(-5, -5, fdescription3, "NW"), new Offset(5, 5,
          fdescription3, "SE"), "fRect3", null);
      Node fnnode = fdescription3.getUpperLeft();
      Text temp2 = lang.newText(fnnode, "", "ftemp2", null, tp3);
      temp2.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, null, null);

      Node f1node = fdescription1.getUpperLeft();
      Node f2node = fdescription2.getUpperLeft();

      Text temp1 = lang.newText(f2node, "", "ftemp1", null, tp3);
      temp1.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, null, null);
      Text temp3 = lang.newText(f1node, "", "ftemp2", null, tp3);
      temp3.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, null, null);

      /******************************************************************/
      // BEGIN LOOP
      Text clabel = lang.newText(new Coordinates(420, 120), "", "clabel", null,
          tp3);
      Text cdescription = lang.newText(new Offset(0, 25, clabel, null), "    ",
          "cdescription", null, tp3);

      for (int i = 2; i <= n; i++) {
        if (i == 2) {
          lang.nextStep();

          src.unhighlight(3);
          clabel.setText("Iterationen", null, null);

          cdescription.setText(" " + i + " ", null, null);
          lang.newRect(new Offset(-5, -5, cdescription, "NW"), new Offset(5, 5,
              cdescription, "SE"), "cRect", null);
          cdescription.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED,
              null, null);
          src.highlight(4);
        } else {
          lang.nextStep();
          temp2.setText("", null, null);
          src.unhighlight(6);
          src.unhighlight(7);
          fdescription1.changeColor(AnimalScript.COLORCHANGE_COLOR,
              Color.BLACK, null, null);
          fdescription2.changeColor(AnimalScript.COLORCHANGE_COLOR,
              Color.BLACK, null, null);
          src.highlight(4);
          cdescription.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED,
              null, null);
          cdescription.setText(" " + i + " ", null, null);
        }

        /**********************************************************************/
        // fn <- f1 + f2
        lang.nextStep();
        src.unhighlight(4);
        cdescription.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK,
            null, null);
        // pfeil

        temp3 = lang.newText(f1node, f1 + "", "ftemp3", null, tp3);
        temp3
            .changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, null, null);

        temp2 = lang.newText(f1node, f2 + "", "ftemp2", null, tp3);
        temp2
            .changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, null, null);

        temp2.moveTo("S", null, fdescription3.getUpperLeft(),
            Timing.INSTANTEOUS, time);
        temp3.moveTo("S", null, fdescription3.getUpperLeft(),
            Timing.INSTANTEOUS, time);

        temp3.setText("" + (f1 + f2), time, null);
        temp2.setText("", time, null);

        src.highlight(5);
        fn = f1 + f2;
        fdescription3.setText("" + fn, time, null);

        /**********************************************************************/
        // f1 <- f2
        lang.nextStep();
        temp2.setText("", null, null);
        temp3.setText("", null, null);

        src.unhighlight(5);
        fdescription3.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK,
            null, null);

        temp1 = lang.newText(f2node, f2 + "", "ftemp1", null, tp3);
        temp1
            .changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, null, null);

        fdescription1.setText("" + f2, time, null);

        temp1.moveTo("S", null, fdescription1.getUpperLeft(),
            Timing.INSTANTEOUS, time);

        src.highlight(6);
        f1 = f2;

        /********************************************************************************/
        // f2 <- fn
        lang.nextStep();
        temp1.setText("", null, null);
        temp1.moveTo("S", null, f2node, null, null);
        src.unhighlight(6);

        // pfeil

        temp2 = lang.newText(fnnode, fn + "", "ftemp2", null, tp3);
        temp2
            .changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, null, null);

        fdescription2.setText("" + fn, time, null);
        temp2.moveTo("S", null, fdescription2.getUpperLeft(),
            Timing.INSTANTEOUS, time);

        src.highlight(7);
        f2 = fn;
        fdescription2.setText("" + fn, time, null);
      }

      lang.nextStep();
      src.highlight(9);
      src.unhighlight(6);
      src.unhighlight(7);

      fdescription3.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED,
          null, null);
      String ausgabe = "Das Ergebnis für n=" + n + " beträgt somit: " + fn;
      Text resultausgabe = lang.newText(new Offset(0, 40, fdescription1, null),
          ausgabe, "fesultausgabe", null, tp3);
      Text resultausgabe2 = lang.newText(
          new Offset(0, 20, resultausgabe, null),
          "Und die Komplexität des iterativen", "resultausgabe2", null, tp3);
      lang.newText(new Offset(0, 20, resultausgabe2, null),
          "Fibonacci-Algorithmus ist in O(n)", "resultausgabe3", null, tp3);
    } else {
      /*************************************************************************/
      // alternative LOOP branche for xor

      Node f1node = fdescription1.getUpperLeft();
      Node f2node = fdescription2.getUpperLeft();

      Text temp1 = lang.newText(f1node, "", "ftemp1", null, tp3);
      temp1.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, null, null);
      Text temp2 = lang.newText(f2node, "", "ftemp2", null, tp3);
      temp2.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, null, null);

      /******************************************************************/
      // BEGIN LOOP
      Text clabel = lang.newText(new Coordinates(420, 120), "", "clabel", null,
          tp3);
      Text cdescription = lang.newText(new Offset(0, 25, clabel, null), "    ",
          "cdescription", null, tp3);
      Text xor3 = lang.newText(new Offset(0, 40, fdescription1, null), "",
          "xorausgabe3", null, tp3);
      for (int i = 2; i <= n; i++) {
        if (i == 2) {
          lang.nextStep();
          fdescription2.changeColor(AnimalScript.COLORCHANGE_COLOR,
              Color.BLACK, null, null);
          src.unhighlight(2);
          clabel.setText("Iterationen", null, null);

          cdescription.setText(" " + i + " ", null, null);
          lang.newRect(new Offset(-5, -5, cdescription, "NW"), new Offset(5, 5,
              cdescription, "SE"), "cRect", null);
          cdescription.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED,
              null, null);
          src.highlight(3);
        } else {
          lang.nextStep();
          src.unhighlight(6);
          src.unhighlight(7);
          xor3.setText("", null, null);
          fdescription1.changeColor(AnimalScript.COLORCHANGE_COLOR,
              Color.BLACK, null, null);
          fdescription2.changeColor(AnimalScript.COLORCHANGE_COLOR,
              Color.BLACK, null, null);
          src.highlight(3);
          cdescription.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED,
              null, null);
          cdescription.setText(" " + i + " ", null, null);
        }

        /**********************************************************************/
        // f1 <- f1 + f2
        lang.nextStep();
        xor3.setText("", null, null);
        src.unhighlight(3);
        cdescription.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK,
            null, null);
        fdescription1.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED,
            null, null);

        temp1 = lang.newText(f1node, "" + f1, "ftemp1", null, tp3);
        temp1
            .changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, null, null);

        temp2 = lang.newText(f2node, "+" + f2, "ftemp2", null, tp3);
        temp2
            .changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED, null, null);

        temp2.moveTo("S", null, fdescription1.getUpperLeft(),
            Timing.INSTANTEOUS, time);
        temp1.moveTo("S", null, fdescription1.getUpperLeft(),
            Timing.INSTANTEOUS, time);

        temp1.setText("" + (f1 + f2), time, null);
        temp2.setText("", time, null);

        src.highlight(4);
        f1 = f1 + f2;
        fdescription1.setText("" + f1, time, null);

        /**********************************************************************/
        // f1 <- f1 xor f2
        lang.nextStep();
        temp2.setText("", null, null);
        temp1.setText("", null, null);
        temp1.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null,
            null);
        temp2.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK, null,
            null);

        src.unhighlight(4);

        String xor1s = f1 + " xor " + f2 + " = " + (f1 ^ f2);
        src.highlight(5);
        f1 = f1 ^ f2;
        fdescription1.setText("" + f1, null, null);

        Text xor1 = lang.newText(new Offset(0, 40, fdescription1, null), xor1s,
            "xorausgabe1", null, tp3);

        /********************************************************************************/
        // f2 <- f1 xor f2
        lang.nextStep();
        xor1.setText("", null, null);

        src.unhighlight(5);
        fdescription1.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK,
            null, null);
        fdescription2.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED,
            null, null);

        String xor2s = f1 + " xor " + f2 + " = " + (f1 ^ f2);
        Text xor2 = lang.newText(new Offset(0, 40, fdescription1, null), xor2s,
            "xorausgabe2", null, tp3);
        src.highlight(6);
        f2 = f1 ^ f2;
        fdescription2.setText("" + f2, null, null);

        /**********************************************************************/
        // f1 <- f1 xor f2
        lang.nextStep();

        xor2.setText("", null, null);
        src.unhighlight(6);
        fdescription2.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK,
            null, null);
        fdescription1.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED,
            null, null);

        String xor3s = f1 + " xor " + f2 + " = " + (f1 ^ f2);

        xor3 = lang.newText(new Offset(0, 40, fdescription1, null), xor3s,
            "xorausgabe3", null, tp3);

        src.highlight(7);
        f1 = f1 ^ f2;
        fdescription1.setText("" + f1, null, null);

        /********************************************************************************/
      }

      lang.nextStep();
      fdescription2.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.RED,
          null, null);
      fdescription1.changeColor(AnimalScript.COLORCHANGE_COLOR, Color.BLACK,
          null, null);
      xor3.setText("", null, null);
      src.highlight(9);
      src.unhighlight(6);
      src.unhighlight(7);

      String ausgabe = "Das Ergebnis für n=" + n + " beträgt somit: " + f2;
      Text resultausgabe = lang.newText(new Offset(0, 40, fdescription1, null),
          ausgabe, "fesultausgabe", null, tp3);
      Text resultausgabe2 = lang.newText(
          new Offset(0, 20, resultausgabe, null),
          "Und die Komplexität des iterativen", "resultausgabe2", null, tp3);
      lang.newText(
          new Offset(0, 20, resultausgabe2, null),
          "Fibonacci-Algorithmus ist in O(n)", "resultausgabe3", null, tp3);
    }
  }
}