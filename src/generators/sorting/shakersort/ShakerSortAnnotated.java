package generators.sorting.shakersort;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Text;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class ShakerSortAnnotated extends AnnotatedAlgorithm implements
    Generator {

  /**
   * The concrete language object used for creating output
   */
  private String                comp  = "Compares";
  private String                assi  = "Assignments";
  private String                iter  = "Iterations";
  private String                swapi = "Swaps";
  ArrayProperties               arrayProps;
  // Array markers
  private ArrayMarkerProperties lMarkerProps, rMarkerProps, uMarkerProps,
      dMarkerProps;

  // ArrayProperties arrayProps;
  // private SourceCodeProperties scProps;
  private IntArray              intArray;

  // default timing
  private Timing                defaultTiming;

  // Text objects as counter for Iterations, Comparisons and swaps
  // private Text nrIt, nrSw;

  private Text                  swappedTF;

  /**
	 * 
	 * 
	 */
  public ShakerSortAnnotated() {

  }

  private void internInit(int[] array) {
    defaultTiming = new TicksTiming(15);
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);

    // Rect box =
    lang.newRect(new Coordinates(10, 10), new Coordinates(140, 60), "box",
        null, rectProps);

    TextProperties textProps1 = new TextProperties();
    textProps1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.ROMAN_BASELINE, 20));
    // Text header =
    lang.newText(new Coordinates(20, 30), "Shaker Sort", "header", null,
        textProps1);

    intArray = lang.newIntArray(new Coordinates(380, 120), array, "intArray",
        null, arrayProps);

    TextProperties infoProps = new TextProperties();
    infoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 30));
    infoProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

    TextProperties textProps2 = new TextProperties();
    textProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 14));
    textProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    swappedTF = lang.newText(new Coordinates(20, 60), " ", "lblTF", null,
        textProps2);

    lMarkerProps = new ArrayMarkerProperties();
    lMarkerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "L");
    lMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);

    rMarkerProps = new ArrayMarkerProperties();
    rMarkerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "R");
    rMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.ORANGE);

    uMarkerProps = new ArrayMarkerProperties();
    uMarkerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "UP");
    uMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

    dMarkerProps = new ArrayMarkerProperties();
    dMarkerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "DOWN");
    dMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
  }

  public void shakerSort(int[] array) {
    Timing swapTiming = new TicksTiming(70);
    ArrayMarker udMarker;

    lang.nextStep();
    exec("header");
    lang.nextStep();

    // boolean swapped;
    swappedTF.setText("swapped: ", null, null);
    exec("swappedDec");
    lang.nextStep();

    ArrayMarker lMarker = lang.newArrayMarker(intArray, 0, "L", null,
        lMarkerProps);
    exec("leftDec");
    lang.nextStep();

    ArrayMarker rMarker = lang.newArrayMarker(intArray,
        intArray.getLength() - 1, "R", null, rMarkerProps);

    exec("rightDec");
    vars.set("right", Integer.toString(intArray.getLength() - 1));
    lang.nextStep();

    exec("completeDec");
    vars.set("complete", Integer.toString(rMarker.getPosition()));

    intArray.highlightCell(Integer.parseInt(vars.get("complete")), null, null);
    lang.nextStep();

    do {
      exec("do");
      lang.nextStep();
      swappedTF.setText("swapped: false", null, null);
      swappedTF.changeColor(null, Color.RED, null, null);
      exec("swappedFalse");
      lang.nextStep();

      swappedTF.changeColor(null, Color.BLACK, null, null);
      exec("for1Init");
      // lang.nextStep();
      udMarker = lang.newArrayMarker(intArray, rMarker.getPosition(), "DOWN",
          null, dMarkerProps);
      lang.nextStep();
      vars.set("down", String.valueOf(udMarker.getPosition()));
      // for (;udMarker.getPosition() > lMarker.getPosition();
      // udMarker.decrement(null,defaultTiming)) {

      exec("for1Comp");
      while (Integer.parseInt(vars.get("down")) > lMarker.getPosition()) {

        lang.nextStep();
        exec("if1");
        intArray.highlightElem(udMarker.getPosition() - 1,
            udMarker.getPosition(), null, null);
        lang.nextStep();
        sourceCode.unhighlight(8);
        // if (intArray.getData(udMarker.getPosition()) <
        // intArray.getData(udMarker.getPosition()- 1))
        if (intArray.getData(Integer.parseInt(vars.get("down"))) < intArray
            .getData(Integer.parseInt(vars.get("down")) - 1)) {
          exec("swappCall1");
          intArray.unhighlightCell(Integer.parseInt(vars.get("complete")),
              null, null);
          intArray.swap(udMarker.getPosition(), udMarker.getPosition() - 1,
              null, swapTiming);
          intArray.highlightCell(Integer.parseInt(vars.get("complete")), null,
              null);
          lang.nextStep();
          intArray.unhighlightElem(udMarker.getPosition() - 1,
              udMarker.getPosition(), null, null);
          exec("swappedTrue1");
          swappedTF.setText("swapped: true", null, null);
          swappedTF.changeColor(null, Color.RED, null, null);
          lang.nextStep();
          swappedTF.changeColor(null, Color.BLACK, null, null);
          intArray.unhighlightCell(Integer.parseInt(vars.get("complete")),
              null, null);
          exec("compDown");
          intArray.highlightCell(Integer.parseInt(vars.get("complete")), null,
              null);
          lang.nextStep();
          sourceCode.unhighlight(11);
        }
        intArray.unhighlightElem(udMarker.getPosition() - 1,
            udMarker.getPosition(), null, null);
        // intArray.unhighlightElem(Integer.parseInt(vars.get("complete")) - 1,
        // Integer.parseInt(vars.get("complete")), null, null);
        exec("endOfIf1For1");
        lang.nextStep();
        sourceCode.unhighlight(12);

        udMarker.decrement(null, defaultTiming);
        exec("for1Decr");
        lang.nextStep();
        exec("for1Comp");
      }

      lang.nextStep();
      udMarker.hide();
      sourceCode.unhighlight(7);
      exec("leftComp");
      intArray.highlightCell(lMarker.getPosition(), null, null);
      // intArray.highlightCell(Integer.parseInt(vars.get("left")), null, null);
      lMarker.move(Integer.parseInt(vars.get("complete")), null, defaultTiming);
      lang.nextStep();

      exec("for2Init");
      // lang.nextStep();
      udMarker = lang.newArrayMarker(intArray, lMarker.getPosition(), "UP",
          null, uMarkerProps);
      lang.nextStep();
      // for (; udMarker.getPosition() < rMarker.getPosition();
      // udMarker.increment(null, defaultTiming)) {
      exec("for2Comp");
      lang.nextStep();
      while (Integer.parseInt(vars.get("up")) < Integer.parseInt(vars
          .get("right"))) {

        lang.nextStep();
        exec("if2");
        intArray.highlightElem(udMarker.getPosition(),
            udMarker.getPosition() + 1, null, null);
        // intArray.highlightElem(Integer.parseInt(vars.get("up")),
        // (Integer.parseInt(vars.get("up")) + 1), null, null);
        lang.nextStep();
        sourceCode.unhighlight(15);
        // if (intArray.getData(udMarker.getPosition()+1) <
        // intArray.getData(udMarker.getPosition())) {
        if (intArray.getData(Integer.parseInt(vars.get("up")) + 1) < intArray
            .getData(Integer.parseInt(vars.get("up")))) {
          exec("swappCall2");
          intArray.unhighlightCell(Integer.parseInt(vars.get("complete")),
              null, null);
          // intArray.swap(udMarker.getPosition(), udMarker.getPosition() + 1,
          // null, swapTiming);
          intArray.swap(Integer.parseInt(vars.get("up")),
              Integer.parseInt(vars.get("up")) + 1, null, swapTiming);
          intArray.highlightCell(Integer.parseInt(vars.get("complete")), null,
              null);
          lang.nextStep();

          intArray.unhighlightElem(udMarker.getPosition(),
              udMarker.getPosition() + 1, null, null);
          // intArray.unhighlightElem(Integer.parseInt(vars.get("up")),
          // Integer.parseInt(vars.get("up")) + 1, null, null);

          exec("swappedTrue2");
          swappedTF.setText("swapped: true", null, null);
          swappedTF.changeColor(null, Color.RED, null, null);
          lang.nextStep();
          swappedTF.changeColor(null, Color.BLACK, null, null);
          intArray.unhighlightCell(Integer.parseInt(vars.get("complete")),
              null, null);
          exec("compUp");
          intArray.highlightCell(Integer.parseInt(vars.get("complete")), null,
              null);
          lang.nextStep();
          sourceCode.unhighlight(18);
        }

        intArray.unhighlightElem(udMarker.getPosition(),
            udMarker.getPosition() + 1, null, null);
        // intArray.unhighlightElem(Integer.parseInt(vars.get("up")) ,
        // Integer.parseInt(vars.get("up")) + 1, null, null);
        exec("endOfIf2For2");
        lang.nextStep();
        exec("for2Inc");
        udMarker.increment(null, defaultTiming);
        lang.nextStep();
        exec("for2Comp");
      }

      lang.nextStep();
      udMarker.hide();
      sourceCode.unhighlight(14);
      exec("rightComp");
      intArray.highlightCell(rMarker.getPosition(), null, null);
      rMarker.move(Integer.parseInt(vars.get("complete")), null, defaultTiming);
      lang.nextStep();

      exec("while");
      swappedTF.changeColor(null, Color.RED, null, null);
      lang.nextStep();
      sourceCode.unhighlight(21);
      swappedTF.changeColor(null, Color.BLACK, null, null);
    } while (1 == (Integer.parseInt(vars.get("swapped"))));
    exec("end");
    intArray.highlightCell(0, intArray.getLength() - 1, null, null);
    lang.nextStep();
    sourceCode.unhighlight(22);

  }

  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    init();

    int[] arrayData = (int[]) arg1.get("intArray");
    arrayProps = (ArrayProperties) arg0.getPropertiesByName("array");
    internInit(arrayData);
    shakerSort(arrayData);
    return lang.toString();
  }

  public String getAlgorithmName() {

    return "Shaker Sort";
  }

  public String getAnimationAuthor() {
    return "Amir Naseri, Morteza Emamgholi";
  }

  public String getCodeExample() {

    return "public void shakerSortAnnotated(int[] intArray) {\n"
        + "	boolean swapped;\n" + "	int left = 0;\n"
        + "	int right = intArray.length - 1;\n" + "	int complete = right;\n"
        + "	do {\n" + "		swapped = false;\n"
        + "		for (int down = right; down > left; down--)\n"
        + "			if (intArray[down] < intArray[down - 1]) {\n"
        + "				swap(intArray, down, down-1);\n" + "				swapped = true;\n"
        + "				complete = down;\n" + "			}\n" + "		left = complete;\n"
        + "		for (int up = left; up < right; up++)\n"
        + "			if (intArray[up + 1] < intArray[up]) {\n"
        + "				swap(intArray[up + 1], up, up+1);\n" + "				swapped = true;\n"
        + "				complete = up;\n" + "			}\n" + "		right = complete;\n"
        + "	} while (swapped);\n" + "}";
  }

  public Locale getContentLocale() {

    return Locale.US;
  }

  public String getDescription() {

    return "Animates ShakerSort with Source Code and Highlighting";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {

    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getName() {

    return "Shaker Sort Annotated";
  }

  public String getOutputLanguage() {

    return Generator.JAVA_OUTPUT;
  }

  @Override
  public void init() {
    super.init();

    SourceCodeProperties props = new SourceCodeProperties();
    props.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
    props.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);

    // instantiate source code primitive to work with
    sourceCode = lang.newSourceCode(new Coordinates(20, 100), "sumupCode",
        null, props);

    // internInit(array);

    // setup complexity
    vars.declare("int", comp);
    vars.setGlobal(comp);
    vars.declare("int", assi);
    vars.setGlobal(assi);
    vars.declare("int", iter);
    vars.setGlobal(iter);
    vars.declare("int", swapi);
    vars.setGlobal(swapi);
    Text text = lang.newText(new Coordinates(300, 20), "...", "complexity",
        null);
    TextUpdater tu = new TextUpdater(text);
    tu.addToken("Compares: ");
    tu.addToken(vars.getVariable(comp));
    tu.addToken(" - Assignments: ");
    tu.addToken(vars.getVariable(assi));
    tu.addToken(" - Iterations: ");
    tu.addToken(vars.getVariable(iter));
    tu.addToken(" - Swaps: ");
    tu.addToken(vars.getVariable(swapi));

    tu.update();

    // parsing anwerfen
    parse();

  }

  @Override
  public String getAnnotatedSrc() {
    return "public void shakerSort(int[] intArray) {		@label(\"header\")\n"
        + "  boolean swapped;								@label(\"swappedDec\") @declare(\"int\", \"swapped\")\n"
        + "  int left = 0;									@label(\"leftDec\") @declare(\"int\", \"left\", \"0\") @inc(\""
        + assi
        + "\")\n"
        + "  int right = intArray.length - 1;				@label(\"rightDec\") @declare(\"int\", \"right\") @inc(\""
        + assi
        + "\")\n"
        + "  int complete = right;							@label(\"completeDec\") @declare(\"int\", \"complete\") @inc(\""
        + assi
        + "\")\n"
        + "  do {											@label(\"do\") @inc(\""
        + iter
        + "\")\n"
        + "    swapped = false;								@label(\"swappedFalse\") @set(\"swapped\", \"0\") @inc(\""
        + assi
        + "\")\n"
        + "    for (int down = right;    					@label(\"for1Init\") @declare(\"int\", \"down\")  @inc(\""
        + assi
        + "\")\n"
        + " down > left 									@label(\"for1Comp\") @continue @inc(\""
        + comp
        + "\")\n"
        + "; down--)										@label(\"for1Decr\") @continue @dec(\"down\") @inc(\""
        + assi
        + "\")\n"
        + "      if (intArray[down] < intArray[down - 1]) {    @label(\"if1\") @inc(\""
        + comp
        + "\") @inc(\""
        + iter
        + "\")\n"
        + "        swap(intArray, down, down-1);					@label(\"swappCall1\") @inc(\""
        + swapi
        + "\")  @inc(\""
        + assi
        + "\")\n"
        + "        swapped = true;								@label(\"swappedTrue1\") @set(\"swapped\", \"1\") @inc(\""
        + assi
        + "\")\n"
        + "        complete = down;								@label(\"compDown\") @eval(\"complete\", \"down\") @inc(\""
        + assi
        + "\")\n"
        + "     }												@label(\"endOfIf1For1\")\n"
        + "    left = complete;								@label(\"leftComp\") @eval(\"left\", \"complete\") @inc(\""
        + assi
        + "\")\n"
        + "    for (int up = left; 							@label(\"for2Init\") @declare(\"int\", \"up\") @eval(\"up\", \"left\") @inc(\""
        + assi
        + "\")\n"
        + " up < right									@label(\"for2Comp\") @continue @inc(\""
        + comp
        + "\") \n"
        + "; up++)										@label(\"for2Inc\") @continue @inc(\"up\") @inc(\""
        + assi
        + "\")\n"
        + "      if (intArray[up + 1] < intArray[up]) {		@label(\"if2\") @inc(\""
        + comp
        + "\") @inc(\""
        + iter
        + "\")\n"
        + "        swap(intArray[up + 1], up, up+1);				@label(\"swappCall2\") @inc(\""
        + swapi
        + "\") @inc(\""
        + assi
        + "\")\n"
        + "        swapped = true;								@label(\"swappedTrue2\") @set(\"swapped\", \"1\") @inc(\""
        + assi
        + "\")\n"
        + "        complete = up;								@label(\"compUp\") @eval(\"complete\", \"up\") @inc(\""
        + assi
        + "\")\n"
        + "      }												@label(\"endOfIf2For2\")\n"
        + "    right = complete;								@label(\"rightComp\") @eval(\"right\", \"complete\") @inc(\""
        + assi
        + "\")\n"
        + "  } while (swapped);							@label(\"while\")\n"
        + "}												@label(\"end\")\n";
  }

}
