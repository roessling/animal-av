package generators.sorting;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class SlowSort extends AnnotatedAlgorithm implements Generator {

  private Language    lang         = null;
  private IntArray    array        = null;
  private ArrayMarker arrayMarkerI = null;
  private ArrayMarker arrayMarkerJ = null;
  private ArrayMarker arrayMarkerM = null;
  private Text        textI        = null;
  private Text        textJ        = null;
  private Text        textM        = null;
  private String      comp         = "Compares";
  private String      assi         = "Assignments";
  private String      rek          = "Recursion";

  @Override
  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    init();

    int[] arrayValues = (int[]) arg1.get("arrayValues");

    // header
    Text headerText = lang
        .newText(new Coordinates(20, 30), "SlowSort", "header", null,
            (TextProperties) arg0.getPropertiesByName("headerProp"));

    Rect headerRect = lang.newRect(new Offset(-5, -5, headerText, "NW"),
        new Offset(5, 5, headerText, "SE"), "headerRect", null,
        (RectProperties) arg0.getPropertiesByName("rectProp"));

    lang.nextStep();

    // init all other elements
    array = lang.newIntArray(new Offset(0, 150, headerRect, "NW"), arrayValues,
        "array", null, (ArrayProperties) arg0.getPropertiesByName("arrayProp"));

    arrayMarkerI = lang.newArrayMarker(array, 0, "arrayMarkerI", null,
        (ArrayMarkerProperties) arg0.getPropertiesByName("arrayMarkerPropI"));
    arrayMarkerJ = lang.newArrayMarker(array, arrayValues.length - 1,
        "arrayMarkerJ", null,
        (ArrayMarkerProperties) arg0.getPropertiesByName("arrayMarkerPropJ"));
    arrayMarkerM = lang.newArrayMarker(array, 0, "arrayMarkerM", null,
        (ArrayMarkerProperties) arg0.getPropertiesByName("arrayMarkerPropM"));

    TextProperties textProp = (TextProperties) arg0
        .getPropertiesByName("textProp");
    textProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, arrayMarkerI
        .getProperties().get(AnimationPropertiesKeys.COLOR_PROPERTY));
    textI = lang.newText(new Offset(100, 0, array, "NE"), "i = 0", "textI",
        null, textProp);
    textProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, arrayMarkerJ
        .getProperties().get(AnimationPropertiesKeys.COLOR_PROPERTY));
    textJ = lang.newText(new Offset(0, 10, textI, "SW"), "j = "
        + (arrayValues.length - 1), "textJ", null, textProp);
    textProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, arrayMarkerM
        .getProperties().get(AnimationPropertiesKeys.COLOR_PROPERTY));
    textM = lang.newText(new Offset(0, 10, textJ, "SW"), "m = 0", "textM",
        null, textProp);

    // start algo
    lang.nextStep("start algorithmn");
    exec("slowSort");
    slowSort(0, arrayValues.length - 1);
    return lang.toString();
  }

  private void slowSort(int i, int j) {
    moveArraymarkers(i, j, -1);

    if (i >= j) {
      exec("if1");
      lang.nextStep();
      return;
    }

    int m = (i + j) / 2;
    exec("m");
    moveArraymarkers(-1, -1, m);
    lang.nextStep();

    exec("rek1");
    lang.nextStep();
    slowSort(i, m);
    moveArraymarkers(i, j, m);

    exec("rek2");
    lang.nextStep();
    slowSort(m + 1, j);
    moveArraymarkers(i, j, m);

    exec("if2");
    if (array.getData(j) < array.getData(m)) {
      exec("swap");
      array.swap(j, m, new TicksTiming(0), new TicksTiming(40));
      lang.nextStep();
    }

    exec("rek3");
    lang.nextStep();
    slowSort(i, j - 1);
    exec("end");
    moveArraymarkers(i, j, m);
  }

  private void moveArraymarkers(int i, int j, int m) {
    // note: "lang.nextStep(1);" is normally not necessary
    // but without it the array markers are jumping out of bound
    // (seems like an animal bug)

    lang.nextStep(1);
    if (i >= 0) {
      textI.setText("i = " + i, new TicksTiming(0), new TicksTiming(40));
      arrayMarkerI.move(i, new TicksTiming(0), new TicksTiming(80));
    }

    if (j >= 0) {
      textJ.setText("j = " + j, new TicksTiming(0), new TicksTiming(40));
      arrayMarkerJ.move(j, new TicksTiming(0), new TicksTiming(50));
    }

    if (m >= 0) {
      textM.setText("m = " + m, new TicksTiming(0), new TicksTiming(40));
      arrayMarkerM.move(m, new TicksTiming(0), new TicksTiming(20));
    }
    lang.nextStep(1);
  }

  /*
   * private void initSourcecodeLines() {
   * sourceCode.addCodeLine("public void slowSort(int[] a, int i, int j) {",
   * null, 0, null); sourceCode.addCodeLine("  if(i >= j) return;", null, 0,
   * null); sourceCode.addCodeLine("  int m = (i+j)/2;", null, 0, null);
   * sourceCode.addCodeLine("  slowSort(a, i, m);", null, 0, null);
   * sourceCode.addCodeLine("  slowSort(a, m+1, j);", null, 0, null);
   * sourceCode.addCodeLine("  if(a[j] < a[m])", null, 0, null);
   * sourceCode.addCodeLine("    swap(a, j, m);", null, 0, null);
   * sourceCode.addCodeLine("  slowSort(a, i, j-1);", null, 0, null);
   * sourceCode.addCodeLine("}", null, 0, null); }
   */

  @Override
  public String getAlgorithmName() {
    return "Slow Sort";
  }

  @Override
  public String getAnimationAuthor() {
    return "Nico Gerwien, Martin Olschowski";
  }

  /*
   * @Override public String getCodeExample() { return
   * "public void slowSort(int[] a, int i, int j) {\n if(i >= j) return;\n int m = (i+j)/2;\n slowSort(a, i, m);\n slowSort(a, m+1, j);\n if(a[j] < a[m]) {\n  swap(a, j, m);\n }\n slowSort(a, i, j-1);\n}"
   * ; }
   */

  @Override
  public Locale getContentLocale() {
    return Locale.US;
  }

  @Override
  public String getDescription() {
    return "A simple sorting algorithmn with a very slow recursive mechanismn.";
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  @Override
  public String getName() {
    return "SlowSort";
  }

  @Override
  public String getOutputLanguage() {
    return generators.framework.Generator.JAVA_OUTPUT;
  }

  @Override
  public void init() {
    super.init();

    lang = new AnimalScript(getAlgorithmName(), getAnimationAuthor(), 640, 480);
    lang.setStepMode(true);
    vars.declare("int", comp);
    vars.setGlobal(comp);
    vars.declare("int", assi);
    vars.setGlobal(assi);
    vars.declare("int", rek);
    vars.setGlobal(rek);

    Text text = lang.newText(new Coordinates(300, 20), "...", "complexity",
        null);
    TextUpdater tu = new TextUpdater(text);
    tu.addToken("Compares: ");
    tu.addToken(vars.getVariable(comp));
    tu.addToken(" - Assignments: ");
    tu.addToken(vars.getVariable(assi));
    tu.addToken(" - Recursionsteps: ");
    tu.addToken(vars.getVariable(rek));
    tu.update();

    SourceCodeProperties sourcecodeProp = new SourceCodeProperties(
        "sourcecodeProp");
    sourcecodeProp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);

    sourceCode = lang.newSourceCode(new Coordinates(15, 250), "sumupCode",
        null, sourcecodeProp);
    // parsing anwerfen
    parse();
  }

  @Override
  public String getAnnotatedSrc() {
    return "public void slowSort(int[] a, int i, int j) {	@label(\"slowSort\") \n"
        + " if(i >= j) return;			@label(\"if1\") @inc(\""
        + comp
        + "\") \n"
        + "  int m = (i+j)/2;			@label(\"m\") @inc(\""
        + assi
        + "\")  \n"
        + "  slowSort(a, i, m);			@label(\"rek1\") @inc(\""
        + rek
        + "\")\n"
        + "  slowSort(a, m+1, j);		@label(\"rek2\") @dec(\""
        + comp
        + "\") @inc(\""
        + rek
        + "\")\n"
        + "  if(a[j] < a[m])			@label(\"if2\") @inc(\""
        + comp
        + "\")\n"
        + "    swap(a, j, m);			@label(\"swap\") @inc(\""
        + assi
        + "\")\n"
        + "  slowSort(a, i, j-1);		@label(\"rek3\") @inc(\""
        + rek
        + "\")\n"
        + "}							@label(\"end\")\n";
  }
}
