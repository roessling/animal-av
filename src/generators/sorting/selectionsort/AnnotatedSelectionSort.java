package generators.sorting.selectionsort;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Font;
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
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class AnnotatedSelectionSort extends AnnotatedAlgorithm implements
    Generator {

  private String      comp          = "Compares";
  private String      assi          = "Assignments";

  private int[]       arrayData;
  private IntArray    array         = null;
  private ArrayMarker iMarker, jMarker;
  private Timing      defaultTiming = new TicksTiming(10);

  private ArrayMarkerUpdater amuI, amuJ;

  @Override
  public String getAnnotatedSrc() {
    return "public void sort(int[] array) {		@label(\"header\")\n"
        + "	int i, j, min;					@label(\"vars_marker\") @declare(\"int\", \"i\") @declare(\"int\", \"j\") @declare(\"int\", \"min\")\n"
        + "	for(i = 0;						@label(\"iForInit\") @inc(\""
        + assi
        + "\") @set(\"i\", \"0\") \n"
        + "       	i < array.length;		@label(\"iForComp\") @continue @inc(\""
        + comp
        + "\")\n"
        + "                    i++) {		@label(\"iForInc\")  @continue @inc(\"i\") @inc(\""
        + assi
        + "\")\n"
        + "     min = i;                    @label(\"minInit\")  @inc(\""
        + assi
        + "\") \n"
        + "		for(j = i+1;				@label(\"jForInit\") @inc(\""
        + assi
        + "\")\n"
        + "        j < array.lenght;		@label(\"jForComp\") @continue @inc(\""
        + comp
        + "\")\n"
        + "         j++) {					@label(\"jForInc\")  @continue @inc(\"j\") @inc(\""
        + assi
        + "\")\n"
        + "			if(array[j] < array[min]) {	@label(\"if1\")   @inc(\""
        + comp
        + "\")\n"
        + "             min = j;			@label(\"minReset\") @inc(\""
        + assi
        + "\") \n"
        + "			}						@label(\"if1End\")\n"
        + "		}							@label(\"jForEnd\")\n"
        + "     if(i != min) {				@label(\"if2\")  @inc(\""
        + comp
        + "\") \n"
        + "        swap(i,min);				@label(\"swap\") @inc(\""
        + assi
        + "\") @inc(\""
        + assi
        + "\") @inc(\""
        + assi
        + "\") \n"
        + "     }							@label(\"if2End\") \n"
        + "	}								@label(\"iForEnd\")\n"
        + "	return array;					@label(\"return\")\n"
        + "}								@label(\"end\")\n";
  }

  @Override
  public String getCodeExample() {
    return "public int[] selectionSort(int[] array) {\n"
        + "  int i , j, min; \n"
        + "  for (int i = 0; i < array.length-1; i++) {\n" + "    min = i;\n"
        + "    for (int j = i + 1; j < array.length; j++) {\n"
        + "      if (a[j] < a[min]) {\n" + "        min = j;\n" + "      }\n"
        + "    }\n" + "    if(i != min) { \n" + "      swap(i,min);\n"
        + "    } \n" + "  }\n" + "}";
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    arrayData = (int[]) primitives.get("intArray");
    super.init();

    TextProperties tProp = new TextProperties();
    tProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 25));

    // Text head =
    lang.newText(new Coordinates(20, 30), "Selection Sort", "...", null, tProp);

    sourceCode = lang.newSourceCode(new Coordinates(20, 145), "sourceCode",
        null, (SourceCodeProperties) props.getPropertiesByName("sourceCode"));
    array = lang.newIntArray(new Coordinates(20, 120), arrayData, "intArray",
        null, (ArrayProperties) props.getPropertiesByName("intArray"));

    iMarker = lang.newArrayMarker(array, 0, "i", null,
        (ArrayMarkerProperties) props.getPropertiesByName("iMarker"));
    jMarker = lang.newArrayMarker(array, 0, "j", null,
        (ArrayMarkerProperties) props.getPropertiesByName("jMarker"));

    amuI = new ArrayMarkerUpdater(iMarker, null, defaultTiming,
        array.getLength() - 1);
    amuJ = new ArrayMarkerUpdater(jMarker, null, defaultTiming,
        array.getLength() - 1);

    vars.declare("int", comp);
    vars.setGlobal(comp);
    vars.declare("int", assi);
    vars.setGlobal(assi);

    Text text = lang.newText(new Coordinates(150, 120), "...", "complexity",
        null);

    TextUpdater tu = new TextUpdater(text);
    tu.addToken("Compares: ");
    tu.addToken(vars.getVariable(comp));
    tu.addToken(" - Assignments: ");
    tu.addToken(vars.getVariable(assi));
    tu.update();

    parse();

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

    exec("iForInit");
    lang.nextStep();

    exec("iForComp");
    lang.nextStep();

    while (Integer.parseInt(vars.get("i")) < array.getLength()) {
      exec("minInit");
      vars.set("min", vars.get("i"));
      array.highlightCell(Integer.parseInt(vars.get("min")), null, null);
      lang.nextStep();

      exec("jForInit");
      vars.set("j", String.valueOf(Integer.parseInt(vars.get("i")) + 1));
      lang.nextStep();

      exec("jForComp");
      lang.nextStep();

      while (Integer.parseInt(vars.get("j")) < array.getLength()) {
        exec("if1");
        lang.nextStep();

        if (array.getData(Integer.parseInt(vars.get("j"))) < array
            .getData(Integer.parseInt(vars.get("min")))) {
          exec("minReset");
          lang.nextStep();

          array.unhighlightCell(Integer.parseInt(vars.get("min")), null, null);
          vars.set("min", vars.get("j"));
          array.highlightCell(Integer.parseInt(vars.get("min")), null, null);
        }
        exec("if1End");
        lang.nextStep();

        exec("jForInc");
        lang.nextStep();

        exec("jForComp");
        lang.nextStep();
      }
      exec("jForEnd");
      lang.nextStep();

      exec("if2");
      lang.nextStep();

      if (Integer.parseInt(vars.get("i")) != Integer.parseInt(vars.get("min"))) {
        exec("swap");
        lang.nextStep();

        array.swap(Integer.parseInt(vars.get("i")),
            Integer.parseInt(vars.get("min")), this.defaultTiming, null);
        lang.nextStep();
      }
      exec("if2End");
      lang.nextStep();

      exec("iForInc");
      lang.nextStep();

      exec("iForComp");
      lang.nextStep();
    }
    exec("iForEnd");
    lang.nextStep();

    exec("return");
    lang.nextStep();

    exec("end");
    lang.nextStep();
  }

  @Override
  public String getAlgorithmName() {
    return "Selection Sort";
  }

  @Override
  public String getAnimationAuthor() {
    return "Enkh-Amgalan Ganbaatar, Martin Tjokrodiredjo";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return "Selection sort is a sorting algorithm, "
        + "specifically an in-place comparison sort. "
        + "It has O(n2) complexity, making it inefficient on large lists, "
        + "and generally performs worse than the similar insertion sort. "
        + "Selection sort is noted for its simplicity, "
        + "and also has performance advantages over more "
        + "complicated algorithms in certain situations."
        + " source: www.en.wikipedia.org";
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  @Override
  public String getName() {
    return "Selection Sort [Annotated]";
  }

  @Override
  public String getOutputLanguage() {
    return JAVA_OUTPUT;
  }

}