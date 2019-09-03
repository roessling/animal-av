package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
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

public class Multpl implements Generator {
  private Language             lang;
  private ArrayProperties      inputArrayProps;
  private int[]                inputArray;
  private SourceCodeProperties sourceCodeProps;
  private ArrayProperties      outputArrayProps;
  private int[]                outputArray;

  private SourceCode           src;
  private TextProperties       headerProps;

  // private Rect hRect;
  // private Text header;

  public void init() {
    lang = new AnimalScript("Multiplication in GF(2⁸)",
        "Lukas Strassel, Stefan Wegener", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    inputArrayProps = (ArrayProperties) props
        .getPropertiesByName("inputArrayProps");
    inputArray = (int[]) primitives.get("inputArray");
    sourceCodeProps = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProps");
    outputArrayProps = (ArrayProperties) props
        .getPropertiesByName("outputArrayProps");
    outputArray = (int[]) primitives.get("secondInputArray");
    if (inputArray.length != 8 || outputArray.length != 8
        || !checkArrayValues(inputArray) || !checkArrayValues(outputArray)) {
      this.printError();
    } else {
      this.motivation();
      this.fields();
      this.irreduciblePolynomials();
      this.multExample();
      this.pseudoCode();
      lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
      MultipleChoiceQuestionModel xor = new MultipleChoiceQuestionModel("xor");
      xor.setPrompt("What represents the array:= [0, 0, 0, 1, 1, 0, 1, 1]");
      xor.addAnswer(
          "first",
          "Bernoulli polynomial",
          0,
          "False.It represents the 'Irreducible Polynominal' which is needed for the modulo operation.");
      xor.addAnswer(
          "second",
          "some random numbers, which help to feel better while doing boring xor stuff",
          0,
          "False.It represents the 'Irreducible Polynominal' which is needed for the modulo operation.");
      xor.addAnswer("fth", "Irreducible Polynominal", 1, "Correct");
      xor.addAnswer(
          "tth",
          "characteristic polynomial",
          0,
          "False.It represents the 'Irreducible Polynominal' which is needed for the modulo operation.");
      lang.addMCQuestion(xor);
      this.mult(inputArray, outputArray);
      this.summary();
      lang.finalizeGeneration();
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
        AnimalScript.DIRECTION_SW), "motivation", null, sourceCodeProps);
    this.extendSrc("The given input-arrays need to have 8 input elements");
    this.extendSrc("and only accept 0 and 1 as values!");
    lang.nextStep();
    lang.hideAllPrimitives();
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
        AnimalScript.DIRECTION_SW), "motivation", null, sourceCodeProps);
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
        AnimalScript.DIRECTION_SW), "fields", null, sourceCodeProps);
    this.extendSrc("For an introduction into finite fields");
    this.extendSrc("please have a look at the algorithm");
    this.extendSrc("Cryptography -> AES -> Binary finite");
    this.extendSrc("field arithmetics: addition");
    lang.nextStep("Fields");
    lang.hideAllPrimitives();
  }

  public void irreduciblePolynomials() {
    header("Irreducible Polynominals and the AES-Field");
    src = lang.newSourceCode(new Offset(0, 0, "subheader",
        AnimalScript.DIRECTION_SW), "fieldexample", null, sourceCodeProps);
    this.extendSrc("A polynominal is called irreducible if it cannot be written as ");
    this.extendSrc("a product of two other polynominals.");
    this.extendSrc("Irreducible polynomials can be used to construct finite fields.");
    this.extendSrc("All elements of a field constructed by a polynomial f");
    this.extendSrc("are residue classes mod f.");
    this.extendSrc("E.g. if we are in GF(2²) and our irreducible polynomial is");
    this.extendSrc("X² + X + 1, the residual classes are 0, 1, X and X + 1.");
    this.extendSrc("");
    this.extendSrc("AES is using the field GF(2⁸) and the irreducible polynomial:");
    this.extendSrc("X⁸ + X⁴ + X³ + X + 1");
    lang.nextStep("Irreducible Polynominals and the AES-Field");
    lang.hideAllPrimitives();
  }

  public void multExample() {
    header("Multiplication in GF(2⁸) - Example");
    src = lang.newSourceCode(new Offset(0, 0, "subheader",
        AnimalScript.DIRECTION_SW), "fieldexample", null, sourceCodeProps);
    this.extendSrc("You want to multiply X⁷+1 with X²+X+1");
    this.extendSrc("First you can multiply, like you know it:");
    this.extendSrc("X⁷+1 * X²+X = X⁹+X⁸+X²+X");
    this.extendSrc("");
    this.extendSrc("Then, if the order of the new polyomial");
    this.extendSrc("is higher than 7, you have to mod it with");
    this.extendSrc("the irreducible aes-polynom:");
    this.extendSrc("X⁹+X⁸+X²+X mod X⁸+X⁴+X³+X+1");
    this.extendSrc("");
    this.extendSrc("This can be done with polynomdivision:");
    this.extendSrc("X⁹+X⁸+X²+X : X⁸+X⁴+X³+X+1 = X + 1");
    this.extendSrc("X⁹+X⁵+X⁴+X²+X");
    this.extendSrc("______________");
    this.extendSrc("   X⁸+X⁵+X⁴");
    this.extendSrc("   X⁸+X⁴+X³+X+1");
    this.extendSrc("   ______________");
    this.extendSrc("   X⁵+X³+X+1");
    this.extendSrc("");
    this.extendSrc("This is the result of the multiplication");
    this.extendSrc("X⁹+X⁸+X²+X mod X⁸+X⁴+X³+X+1 = X⁵+X³+X+1");

    lang.nextStep("Multiplication in GF(2⁸) - Example");
    lang.hideAllPrimitives();
  }

  public void pseudoCode() {
    header("Multiplication in GF(2⁸) - Pseudocode");
    src = lang.newSourceCode(new Offset(0, 0, "subheader",
        AnimalScript.DIRECTION_SW), "pseudoCode", null, sourceCodeProps);
    this.extendSrc("The representation of polynoms in a");
    this.extendSrc("computer-programm is made with arrays.");
    this.extendSrc("To represent a AES-polynom you need an");
    this.extendSrc("array with size 8.");
    this.extendSrc("The elements of an array have index 0 to n-1");
    this.extendSrc(" ");
    this.extendSrc("In the following pseudo-code ⊕ describes");
    this.extendSrc("the addition of two finite fields represented as arrays");
    this.extendSrc("(for details, this algorithm is also present for animal)");
    this.extendSrc("The << and >> Operator are array-shifts where a zero");
    this.extendSrc("is shifted into the array");
    this.extendSrc("The algorithm is:");
    src = lang.newSourceCode(new Offset(0, 0, "pseudoCode",
        AnimalScript.DIRECTION_SW), "psCodebox", null, sourceCodeProps);
    src.addCodeLine("int[] C = {0}", null, 0, null);
    src.addCodeLine("For i from 0 to 7 do", null, 0, null);
    src.addCodeLine("if(A[0] == 1)", null, 1, null);
    src.addCodeLine("C = C ⊕ B;", null, 2, null);
    src.addCodeLine("if(B[7]==1){", null, 1, null);
    src.addCodeLine("B << 1", null, 2, null);
    src.addCodeLine("B = B ⊕ [0, 0, 0, 1, 1, 0, 1, 1]", null, 2, null);
    src.addCodeLine("}else", null, 1, null);
    src.addCodeLine("B << 1", null, 2, null);
    src.addCodeLine("A >> 1", null, 1, null);
    src.addCodeLine("Return(C)", null, 0, null);

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    lang.newRect(new Offset(-10, -10, "psCodebox", AnimalScript.DIRECTION_NW),
        new Offset(10, 10, "psCodebox", "SE"), "codeRect", null, rectProps);
    lang.nextStep("Multiplication in GF(2⁸) - Pseudocode");
    lang.hideAllPrimitives();
  }

  public void summary() {
    header("Summary");
    src = lang.newSourceCode(new Offset(0, 0, "subheader",
        AnimalScript.DIRECTION_SW), "summary", null, sourceCodeProps);
    this.extendSrc("The algorithm has linear runtime");
    this.extendSrc("(8 Iterations through the loop)");
    this.extendSrc("");
    this.extendSrc("It can be used for the MixColumns");
    this.extendSrc("function in AES.");
    lang.nextStep("Summary");
    lang.hideAllPrimitives();
  }

  public void extendSrc(String text) {
    src.addCodeLine(text, null, 0, null);
  }

  public String getName() {
    return "Multiplication in GF(2⁸)";
  }

  public String getAlgorithmName() {
    return "Advanced Encryption Standard (AES)";
  }

  public String getAnimationAuthor() {
    return "Lukas Strassel, Stefan Wegener";
  }

  public String getDescription() {
    return "<h1>AES: Multiplication in GF(2^8)</h1>"
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
        + "<p>Multiplication in GF(2^8) is a little bit like"
        + "\n"
        + "writing multiplying like we know it from school."
        + "\n"
        + "The main difference is, that you are in a finite"
        + "\n"
        + "field and have to calculate all polynoms mod your"
        + "\n"
        + "irreducible polynom."
        + "\n"
        + "a polynomwise XOR-Operation)</p>"
        + "\n"
        + "\n"
        + "<h2>Hint</h2>"
        + "\n"
        + "<p>The input arrays (arrayA,arrayB) need to have length 8 and only accept zero and one as input values</p>"
        + "\n" + "\n";
  }

  public String getCodeExample() {
    return "int[] C = {0}" + "\n" + "For i from 0 to 7 do" + "\n"
        + "	if(A[0] == 1)" + "\n" + "		C = C ⊕ B;" + "\n" + "	if(B[7]==1){"
        + "\n" + "		B << 1" + "\n" + "		B = B ⊕ [0, 0, 0, 1, 1, 0, 1, 1]"
        + "\n" + "	}else" + "\n" + "		B << 1" + "\n" + "	A >> 1" + "\n"
        + "Return(C)";
  }

  private void changeValue(int[] value, IntArray array) {
    for (int i = 0; i <= 7; i++) {
      array.put(i, value[i], new TicksTiming(0), new TicksTiming(0));
    }
  }

  private int[] xor(int[] p, int[] q) {
    int[] result = new int[p.length];
    for (int r = 0; r < p.length; r++)
      result[r] = p[r] ^ q[r];
    return result;
  }

  private int[] shiftLeft(int[] array) {
    int[] result = new int[8];
    System.arraycopy(array, 1, result, 0, array.length - 1);
    return result;
  }

  private int[] shiftRight(int[] array) {
    int[] result = new int[8];
    System.arraycopy(array, 0, result, 1, array.length - 1);
    return result;
  }

  public void mult(int[] A, int[] B) {
    header("Interactive example");
    int[] C = new int[8];// result array
    Arrays.fill(C, 0);// array mit 0en auffüllen
    /* init arrays */
    int[] a2 = A;
    IntArray ia = lang.newIntArray(new Coordinates(370, 180), a2, "arrayA",
        null, inputArrayProps);
    int[] b2 = B;
    IntArray ib = lang.newIntArray(new Coordinates(370, 220), b2, "arrayB",
        null, inputArrayProps);
    IntArray ic = lang.newIntArray(new Coordinates(370, 300), C, "arrayC",
        null, outputArrayProps);
    /* pointer */
    ArrayMarkerProperties arrayPMProps = new ArrayMarkerProperties();
    arrayPMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    arrayPMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    ArrayMarker pointerN = lang.newArrayMarker(ia, 7, "pointerN", null,
        arrayPMProps);
    /* textprops */
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.BOLD, 20));
    lang.newText(new Coordinates(330, 180), "A:", "indexA", null, textProps);
    lang.newText(new Coordinates(330, 220), "B:", "indexB", null, textProps);
    lang.newText(new Coordinates(330, 300), "C:", "indexC", null, textProps);
    /* code */
    src = lang.newSourceCode(new Offset(10, 10, "subheader",
        AnimalScript.DIRECTION_SW), "code", null, sourceCodeProps);
    src.addCodeLine("int[] C = {0}", null, 0, null);
    src.addCodeLine("For i from 0 to 7 do", null, 0, null);
    src.addCodeLine("if(A[0] == 1)", null, 1, null);
    src.addCodeLine("C = C ⊕ B;", null, 2, null);
    src.addCodeLine("if(B[7]==1){", null, 1, null);
    src.addCodeLine("B << 1", null, 2, null);
    src.addCodeLine("B = B ⊕ [0, 0, 0, 1, 1, 0, 1, 1]", null, 2, null);
    src.addCodeLine("}else", null, 1, null);
    src.addCodeLine("B << 1", null, 2, null);
    src.addCodeLine("A >> 1", null, 1, null);
    src.addCodeLine("Return(C)", null, 0, null);
    /* coderect */
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Offset(-10, -10, "code", AnimalScript.DIRECTION_NW),
        new Offset(10, 10, "code", "SE"), "codeRect", null, rectProps);
    /* dyn animation */
    lang.nextStep("Interactive example");
    src.highlight(0);
    ic.highlightCell(0, 7, new TicksTiming(0), new TicksTiming(0));
    lang.nextStep();
    this.fuckThatShit(ic);
    src.unhighlight(0);
    for (int i = 0; i <= 7; i++) {
      pointerN.move(7 - i, null, null);
      src.highlight(1);
      lang.nextStep();

      src.unhighlight(1);
      src.highlight(2);// if(A[0] == 0)
      ia.highlightCell(7, new TicksTiming(0), new TicksTiming(0));
      lang.nextStep();
      ia.unhighlightCell(7, new TicksTiming(0), new TicksTiming(0));
      src.unhighlight(2);
      src.highlight(3);
      if (a2[7] == 1) {
        ic.highlightCell(0, 7, new TicksTiming(0), new TicksTiming(0));
        ib.highlightCell(0, 7, new TicksTiming(0), new TicksTiming(0));
        lang.nextStep();
        C = this.xor(C, b2);
        changeValue(C, ic);
        this.fuckThatShit(ib);
        lang.nextStep();
        this.fuckThatShit(ic);
      }
      this.fuckThatShit(ib);
      src.unhighlight(3);
      src.highlight(4);
      ib.highlightCell(0, new TicksTiming(0), new TicksTiming(0));
      lang.nextStep();
      src.unhighlight(4);
      ib.unhighlightCell(0, new TicksTiming(0), new TicksTiming(0));
      if (b2[0] == 1) {
        src.highlight(5);
        ib.highlightCell(0, 7, new TicksTiming(0), new TicksTiming(0));
        lang.nextStep();
        b2 = this.shiftLeft(b2);
        changeValue(b2, ib);
        lang.nextStep();
        src.unhighlight(5);
        src.highlight(6);
        b2 = this.xor(b2, new int[] { 0, 0, 0, 1, 1, 0, 1, 1 });
        lang.nextStep();
        // temp.unhighlightCell(0, 7, new TicksTiming(0), new TicksTiming(0));
        changeValue(b2, ib);
        lang.nextStep();
        src.unhighlight(6);
        this.fuckThatShit(ib);
      } else {
        b2 = shiftLeft(b2);
        src.highlight(8);
        ib.highlightCell(0, 7, new TicksTiming(0), new TicksTiming(0));
        lang.nextStep();

        changeValue(b2, ib);
        // temp.unhighlightCell(0, 7, new TicksTiming(0), new TicksTiming(0));
        lang.nextStep();

        src.unhighlight(8);
        this.fuckThatShit(ib);
        // ib.unhighlightCell(0, 7, new TicksTiming(0), new TicksTiming(0));
      }
      src.highlight(9);
      ia.highlightCell(0, 7, new TicksTiming(0), new TicksTiming(0));
      lang.nextStep();
      a2 = this.shiftRight(a2);
      changeValue(a2, ia);
      lang.nextStep();
      this.fuckThatShit(ia);
      // ia.unhighlightCell(0, 7, new TicksTiming(0), new TicksTiming(0));
      src.unhighlight(9);
    }
    src.highlight(10);
    lang.nextStep();
    src.unhighlight(9);
    lang.nextStep();
    lang.hideAllPrimitives();
  }

  public void fuckThatShit(IntArray buggy) {
    for (int i = 0; i <= 7; i++) {
      buggy.unhighlightCell(i, new TicksTiming(0), new TicksTiming(0));
    }
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

}
