package generators.sorting.bubblesort;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Text;
import algoanim.primitives.updater.ArrayMarkerUpdater;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class AnnotatedBubbleSort extends AnnotatedAlgorithm implements
    Generator {

  private String      comp          = "Compares";
  private String      assi          = "Assignments";

  private int[]       arrayData;
  private IntArray    array         = null;
  private ArrayMarker iMarker, jMarker;
  private Timing      defaultTiming = new TicksTiming(100);

  private ArrayMarkerUpdater amuI, amuJ;

  @Override
  public String getAnimationAuthor() {
    return "Sebastian Proksch"; // <sproksch[at]rbg.informatik.tu-darmstadt.de>";
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public String getAnnotatedSrc() {
    return "int[] sort(int[] arr) {				@label(\"header\")\n"
        + "	int i, j;						@label(\"vars_marker\") @declare(\"int\", \"i\") @declare(\"int\", \"j\")\n"
        + "	boolean swapped = true;			@label(\"vars_swapped\") @declare(\"int\", \"swapped\", \"1\") @inc(\""
        + assi
        + "\")\n"
        + "	for(i = arr.length;				@label(\"oForInit\") @inc(\""
        + assi
        + "\")\n"
        + "swapped				@label(\"oForComp1\") @continue @inc(\""
        + comp
        + "\")\n"
        + "               && i > 0;			@label(\"oForComp2\") @continue @inc(\""
        + comp
        + "\")\n"
        + "                    i--) {		@label(\"oForDec\") @continue @dec(\"i\") @inc(\""
        + assi
        + "\")\n"
        + "		swapped = false;			@label(\"resetSwapped\") @set(\"swapped\", \"0\") @inc(\""
        + assi
        + "\")\n"
        + "		for(j = 1;					@label(\"iForInit\") @set(\"j\", \"1\") @inc(\""
        + assi
        + "\")\n"
        + "       j<i;						@label(\"iForComp\") @continue @inc(\""
        + comp
        + "\")\n"
        + "         j++) {					@label(\"iForInc\") @continue @inc(\"j\") @inc(\""
        + assi
        + "\")\n"
        + "			if(arr[j-1] > arr[j]) {	@label(\"if\") @inc(\""
        + comp
        + "\")\n"
        + "				swap(arr, j-1, j);	@label(\"swap\") @inc(\""
        + assi
        + "\") @inc(\""
        + assi
        + "\") @inc(\""
        + assi
        + "\")\n"
        + "				swapped = true;		@label(\"setSwapped\") @set(\"swapped\", \"1\") @inc(\""
        + assi
        + "\")\n"
        + "			}						@label(\"ifEnd\")\n"
        + "		}							@label(\"iForEnd\")\n"
        + "	}								@label(\"oForEnd\")\n"
        + "	return arr;						@label(\"return\")\n"
        + "}								@label(\"end\")\n";
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
    // System.out.println(annotatedSrc);

    // setup array
    ArrayProperties iap = new ArrayProperties();
    iap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    iap.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.LIGHT_GRAY);
    array = lang.newIntArray(new Coordinates(20, 70), arrayData, "array", null,
        iap);

    ArrayMarkerProperties ampI = new ArrayMarkerProperties();
    ampI.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    ampI.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    iMarker = lang.newArrayMarker(array, 0, "iMarker", null, ampI);
    amuI = new ArrayMarkerUpdater(iMarker, null, defaultTiming,
        array.getLength() - 1);

    ArrayMarkerProperties ampJ = new ArrayMarkerProperties();
    ampJ.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    ampJ.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
    jMarker = lang.newArrayMarker(array, 0, "jMarker", null, ampJ);
    amuJ = new ArrayMarkerUpdater(jMarker, null, defaultTiming,
        array.getLength() - 1);

    // setup complexity
    vars.declare("int", comp);
    vars.setGlobal(comp);
    vars.declare("int", assi);
    vars.setGlobal(assi);

    Text text = lang.newText(new Coordinates(300, 20), "...", "complexity",
        null);
    TextUpdater tu = new TextUpdater(text);
    tu.addToken("Compares: ");
    tu.addToken(vars.getVariable(comp));
    tu.addToken(" - Assignments: ");
    tu.addToken(vars.getVariable(assi));
    tu.update();

    // parsing anwerfen
    parse();
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    // fetch Array Data from Framework
    arrayData = (int[]) primitives.get("Array Data");

    init();
    sort();

    return lang.toString();
  }

  private void sort() {
    exec("header");
    lang.nextStep();

    exec("vars_marker");
    amuI.setVariable(vars.getVariable("i"));
    amuJ.setVariable(vars.getVariable("j"));
    lang.nextStep();

    exec("vars_swapped");
    lang.nextStep();

    exec("oForInit");
    vars.set("i", String.valueOf(array.getLength()));
    lang.nextStep();
    exec("oForComp1");
    lang.nextStep();

    exec("oForComp2");
    lang.nextStep();
    if (Integer.parseInt(vars.get("i")) > 0) {
      while (!"0".equals(vars.get("swapped"))
          && Integer.parseInt(vars.get("i")) > 0) {
        exec("resetSwapped");
        lang.nextStep();

        exec("iForInit");
        lang.nextStep();
        exec("iForComp");
        lang.nextStep();
        while (Integer.parseInt(vars.get("j")) < Integer
            .parseInt(vars.get("i"))) {

          exec("if");
          lang.nextStep();

          int j = Integer.parseInt(vars.get("j"));
          if (array.getData(j - 1) > array.getData(j)) {
            exec("swap");
            array.swap(j - 1, j, null, defaultTiming);
            lang.nextStep();

            exec("setSwapped");
            lang.nextStep();
          }

          exec("iForInc");
          lang.nextStep();
          exec("iForComp");
          lang.nextStep();
        }

        exec("oForDec");
        array.highlightCell(Integer.parseInt(vars.get("i")), null, null);
        lang.nextStep();

        exec("oForComp1");
        lang.nextStep();

        if (!"0".equals(vars.get("swapped"))) {
          exec("oForComp2");
          lang.nextStep();
        }
      }
    }

    exec("return");
    // highlight all cells on end if not
    for (int i = 0; i < array.getLength(); i++)
      array.highlightCell(i, null, null);
  }

  @Override
  public String getAlgorithmName() {
    return "Bubble Sort";
  }

  /*
   * @Override public String getCodeExample() { return
   * "int[] sort(int[] arr) {\n" + "  int i, j;\n" +
   * "  boolean swapped = true;\n" +
   * "  for(i = arr.length; swapped && i > 0; i--) {\n" +
   * "    swapped = false;\n" + "    for(j = 1; j<i; j++) {\n" +
   * "      if(arr[j-1] > arr[j]) {\n" + "        swap(arr, j-1, j);\n" +
   * "        swapped = true;\n" + "      }\n" + "    }\n" + "  }\n" +
   * "  return arr;\n" + "}\n"; }
   */
  @Override
  public Locale getContentLocale() {
    return Locale.US;
  }

  @Override
  public String getDescription() {
    return "This bubblesort implementation represents the third example for the usage of the new annotationsystem.";
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  @Override
  public String getName() {
    return "BubbleSort [annotation based]";
  }
}
