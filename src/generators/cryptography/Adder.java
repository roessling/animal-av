package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class Adder implements Generator {
  private Language             lang;
  private SourceCodeProperties sourceCode;
  private ArrayProperties      inputArray;
  private ArrayProperties      outputArray;
  private int[]                arrayB;
  private int[]                arrayA;

  // static
  private TextProperties       headerProps;
  private SourceCode           src;

  public void init() {
    lang = new AnimalScript("Binary finite field arithmetics: addition",
        "Lukas Strassel, Stefan Wegener", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    inputArray = (ArrayProperties) props.getPropertiesByName("inputArray");
    outputArray = (ArrayProperties) props.getPropertiesByName("outputArray");
    arrayB = (int[]) primitives.get("arrayB");
    arrayA = (int[]) primitives.get("arrayA");
    if (arrayA.length != arrayB.length || !checkArrayValues(arrayA)
        || !checkArrayValues(arrayB)) {
      this.printError();
    } else {
      this.motivation();
      this.fields();
      this.fieldOrder();
      this.fieldExample();
      this.additionExample();
      this.pseudoCode();
      this.add(arrayA, arrayB);
      this.summary();
    }

    return lang.toString();
  }

  public boolean checkArrayValues(int[] array) {
    for (int value : array) {
      if (value != 0 && value != 1) {
        return false;
      }
    }
    return true;
  }

  public void printError() {
    header("Error");
    src = lang.newSourceCode(new Offset(0, 0, "subheader",
        AnimalScript.DIRECTION_SW), "motivation", null, sourceCode);
    this.extendSrc("The given input-arrays need to have the same lenght");
    this.extendSrc("and only accept 0 and 1 as values!");
    lang.nextStep();
    lang.hideAllPrimitives();
  }

  public String getName() {
    return "Binary finite field arithmetics: addition";
  }

  public String getAlgorithmName() {
    return "Advanced Encryption Standard (AES)";
  }

  public String getAnimationAuthor() {
    return "Lukas Strassel, Stefan Wegener";
  }

  public String getDescription() {
    return "<h1>Binary field arithmetics: addition</h1>"
        + "\n"
        + "<h2>Motivation</h2>"
        + "\n"
        + "<p>Finite field (also called Galois field) arithmetic"
        + "\n"
        + "is an important concept for understanding cryptography."
        + "\n"
        + "The Rijndael cipher (better known as Advanced"
        + "\n"
        + "Encyption Standard [AES]) is using finite field"
        + "\n"
        + "arithmetics and also elliptic curve cryptography"
        + "\n"
        + "needs concepts from this mathematical field of abstract"
        + "\n"
        + "algebra.</p>"
        + "\n"
        + "\n"
        + "<h2>Description</h2>"
        + "\n"
        + "<p>Addition in binary fields is a regular addition of"
        + "\n"
        + "binary polynomials (which is nothing else then"
        + "\n"
        + "a polynomwise XOR-Operation)</p>"
        + "\n"
        + "\n"
        + "<h2>Hint</h2>"
        + "\n"
        + "<p>The input arrays (arrayA,arrayB) need to have the same length and only accept zero and one as input values</p>"
        + "\n" + "\n";
  }

  public String getCodeExample() {
    return "For i from 0 to n-1 do" + "\n" + "       C[i]=A[i] ⊕ B[i]" + "\n"
        + "Return(c)";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  public void extendSrc(String text) {
    src.addCodeLine(text, null, 0, null);
  }

  public void header(String text) {
    headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    lang.newText(new Coordinates(20, 30),
        "Binary Field Arithmetics - Addition", "mainHeader", null, headerProps);
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Offset(-5, -5, "mainHeader", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "mainHeader", "SE"), "hRect", null, rectProps);
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 18));
    lang.newText(new Offset(5, 5, "hRect", AnimalScript.DIRECTION_SW), text,
        "subheader", null, headerProps);
  }

  public void motivation() {
    header("Motivation");
    src = lang.newSourceCode(new Offset(0, 0, "subheader",
        AnimalScript.DIRECTION_SW), "motivation", null, sourceCode);
    this.extendSrc("Finite field (also called Galois field) arithmetic");
    this.extendSrc("is an important concept for understanding cryptography.");
    this.extendSrc("The Rijndael cipher (better known as Advanced");
    this.extendSrc("Encyption Standard [AES]) is using finite field");
    this.extendSrc("arithmetics and also elliptic curve cryptography");
    this.extendSrc("needs concepts from this mathematical field of abstract");
    this.extendSrc("algebra.");
    lang.nextStep("Motivation");
    lang.hideAllPrimitives();
  }

  public void fields() {
    header("Fields");
    src = lang.newSourceCode(new Offset(0, 0, "subheader",
        AnimalScript.DIRECTION_SW), "fields", null, sourceCode);
    this.extendSrc("Fields are abstractions of number-systems, e. g.");
    this.extendSrc("real numbers or rational numbers.");
    this.extendSrc("They consist of a set F with two operations:");
    this.extendSrc("    - addition (+) and");
    this.extendSrc("    - multiplication (*)");
    this.extendSrc("");
    this.extendSrc("For those who like to have a deeper knowledge:");
    this.extendSrc("The operations + and * have to fullfil the following");
    this.extendSrc("properties in a field F:");
    this.extendSrc("   (i)   (F, +) is an abelian group");
    this.extendSrc("   (ii)  (F\\{0}, *) is an abelian group");
    this.extendSrc("   (iii) The distributive law holds: (x+y)*z = (x*z)+(y*z)");
    this.extendSrc("If the set F has a finite number of elements, F is");
    this.extendSrc("called finite.");
    lang.nextStep("Fields");
    lang.hideAllPrimitives();
  }

  public void fieldOrder() {
    header("Order of Finite Fields");
    src = lang.newSourceCode(new Offset(0, 0, "subheader",
        AnimalScript.DIRECTION_SW), "fieldOrder", null, sourceCode);
    this.extendSrc("The order of finite fields describes the number of");
    this.extendSrc("elements in the field.");
    this.extendSrc("A field of order i ist denoted as Fᵢ.");
    lang.nextStep();
    lang.newText(new Offset(0, 20, "fieldOrder", AnimalScript.DIRECTION_SW),
        "Binary fields", "subsub", null, headerProps);
    src = lang.newSourceCode(new Offset(0, 0, "subsub",
        AnimalScript.DIRECTION_SW), "binary", null, sourceCode);
    src.addCodeLine("Fields that have an order of 2ⁱ are called", null, 0, null);
    src.addCodeLine("binary fields.", null, 0, null);
    src.addCodeLine("A common way to represent elements of binary", null, 0,
        null);
    src.addCodeLine("fields is the polynomial basis representation:", null, 0,
        null);
    src.addCodeLine("F₂ᵢ = {aᵢ₋₁xⁱ⁻¹ + aᵢ₋₂xⁱ⁻² + ... + a₁x + a₀: aᵢ ∈ {0,1}}",
        null, 0, null);
    lang.nextStep("Order of Finite Fields");
    lang.hideAllPrimitives();
  }

  public void fieldExample() {
    header("Example of elements in a finite field");
    src = lang.newSourceCode(new Offset(0, 0, "subheader",
        AnimalScript.DIRECTION_SW), "fieldexample", null, sourceCode);
    this.extendSrc("Let our binary finite field be from order 2³");
    this.extendSrc("The number of elements is 8 (= 2³).");
    this.extendSrc("The field consists of the following elements");
    this.extendSrc("(in polynomial notation):");
    this.extendSrc("");
    this.extendSrc("0         1       X");
    this.extendSrc("");
    this.extendSrc("X+1     X²      X²+1");
    this.extendSrc("");
    this.extendSrc("X²+X    X²+X+1");
    lang.nextStep("Example of elements in a finite field");
    lang.hideAllPrimitives();
  }

  public void additionExample() {
    header("Addition in binary fields");
    src = lang.newSourceCode(new Offset(0, 0, "subheader",
        AnimalScript.DIRECTION_SW), "additionexample", null, sourceCode);
    this.extendSrc("Addition in binary fields is a regular addition of");
    this.extendSrc("binary polynomials (which is nothing else then");
    this.extendSrc("a polynomwise XOR-Operation)");
    this.extendSrc("");
    this.extendSrc("Example:");
    this.extendSrc("When we take the finite field with order 2³");
    this.extendSrc("and want to add X+1 with X²+1 our");
    this.extendSrc("result is X²+X:");
    this.extendSrc("");
    this.extendSrc("         X + 1");
    this.extendSrc(" X² +        1");
    this.extendSrc("____________");
    this.extendSrc(" X² + X");
    lang.nextStep("Addition in binary fields");
    lang.hideAllPrimitives();
  }

  public void pseudoCode() {
    header("Addition in pseudocode");
    src = lang.newSourceCode(new Offset(0, 0, "subheader",
        AnimalScript.DIRECTION_SW), "pseudoCode", null, sourceCode);
    this.extendSrc("The representation of polynoms in a");
    this.extendSrc("computer-programm is made with arrays.");
    this.extendSrc("To represent a polynom of oder 2ⁿ the array");
    this.extendSrc("needs to have size n.");
    this.extendSrc("The elements of an array have index 0 to n-1");
    this.extendSrc(" ");
    this.extendSrc("The algorithm is:");
    src = lang.newSourceCode(new Offset(0, 0, "pseudoCode",
        AnimalScript.DIRECTION_SW), "psCodebox", null, sourceCode);
    src.addCodeLine("For i from 0 to n-1 do", null, 0, null);
    src.addCodeLine("      C[i]=A[i] ⊕ B[i]", null, 0, null);
    src.addCodeLine("Return(c)", null, 0, null);

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    lang.newRect(new Offset(-10, -10, "psCodebox", AnimalScript.DIRECTION_NW),
        new Offset(10, 10, "psCodebox", "SE"), "codeRect", null, rectProps);
    lang.nextStep("Addition in pseudocode");
    lang.hideAllPrimitives();
  }

  public void summary() {
    header("Summary");
    src = lang.newSourceCode(new Offset(0, 0, "subheader",
        AnimalScript.DIRECTION_SW), "summary", null, sourceCode);
    this.extendSrc("This version of binary field addition has a");
    this.extendSrc("runtime of O(n) where n denotes the");
    this.extendSrc("exponent n of the fields order 2ⁿ");
    this.extendSrc("");
    this.extendSrc("The algorithm is very easy, but important");
    this.extendSrc("when you have to work with finite fields.");
    lang.nextStep("Summary");
    lang.hideAllPrimitives();
  }

  public void add(int[] a, int[] b) {
    header("Interactive example");
    String[] c = new String[a.length];
    Arrays.fill(c, "-");

    IntArray ia = lang.newIntArray(new Coordinates(330, 180), a, "arrayA",
        null, inputArray);

    IntArray ib = lang.newIntArray(new Coordinates(330, 220), b, "arrayB",
        null, inputArray);

    StringArray ic = lang.newStringArray(new Coordinates(330, 260), c,
        "arrayC", null, outputArray);

    ArrayMarkerProperties arrayPMProps = new ArrayMarkerProperties();
    arrayPMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    arrayPMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

    ArrayMarker pointerN = lang.newArrayMarker(ia, 0, "pointerN", null,
        arrayPMProps);

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.BOLD, 20));
    lang.newText(new Coordinates(300, 180), "A:", "indexA", null, textProps);
    lang.newText(new Coordinates(300, 220), "B:", "indexB", null, textProps);
    lang.newText(new Coordinates(300, 260), "C:", "indexC", null, textProps);

    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    src = lang.newSourceCode(new Offset(10, 10, "subheader",
        AnimalScript.DIRECTION_SW), "sourceCode", null, scProps);
    src.addCodeLine("For i from 0 to n-1 do", null, 0, null);
    src.addCodeLine("      C[i]=A[i] ⊕ B[i]", null, 0, null);
    src.addCodeLine("Return(c)", null, 1, null);

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    lang.newRect(new Offset(-10, -10, "sourceCode", AnimalScript.DIRECTION_NW),
        new Offset(10, 10, "sourceCode", "SE"), "codeRect", null, rectProps);

    Text text = lang.newText(new Coordinates(20, 310), "", "arrayToText", null,
        textProps);
    String textString;
    String[] hights = { "⁰", "¹", "²", "³", "⁴", "⁵", "⁶", "⁷", "⁸", "⁹" };
    lang.nextStep("Interactive example");
    //
    for (int x = c.length - 1; x >= 0; x--) {
      pointerN.move(x, null, null);
      src.highlight(0);

      lang.nextStep();
      src.highlight(1);
      src.unhighlight(0);
      c[x] = Integer.toString(a[x] ^ b[x]);
      ia.highlightCell(x, new TicksTiming(0), new TicksTiming(0));
      ib.highlightCell(x, new TicksTiming(0), new TicksTiming(0));
      ic.highlightCell(x, new TicksTiming(0), new TicksTiming(0));
      ic.put(x, c[x], new TicksTiming(0), new TicksTiming(0));
      textString = "Current interpretation of array C: ";
      for (int z = x; z <= c.length - 1; z++) {
        if ((a[z] ^ b[z]) != 0) {
          textString += "x" + hights[Math.abs(c.length - 1 - z)];
          if (z != c.length - 1) {
            textString += '+';
          }
        }
      }
      text.setText(textString, new TicksTiming(0), new TicksTiming(0));

      lang.nextStep();
      src.unhighlight(1);
      ia.unhighlightCell(x, new TicksTiming(0), new TicksTiming(0));
      ib.unhighlightCell(x, new TicksTiming(0), new TicksTiming(0));
      ic.unhighlightCell(x, new TicksTiming(0), new TicksTiming(0));

      // verlängere Stringhandle
    }
    src.highlight(0);

    lang.nextStep();
    src.unhighlight(0);
    src.highlight(2);
    lang.hideAllPrimitives();
  }
}