package generators.sorting.combsort;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.updater.ArrayMarkerUpdater;
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

public class CombSortWR extends AnnotatedAlgorithm implements Generator {
  private int    arr_len = 0;
  private Text   header;
  private Rect   hbox;
  private Timing time;
  private StringArray sArrayM, sArray;
  private ArrayMarker iMarker, jMarker;

  public CombSortWR() {
    init();
  }

  public void sort(int[] arr) {
    // "CombSort(int[] array) {"
    exec("header");
    show(sArray);
    lang.nextStep();

    // "  boolean swapped = true;"
    exec("v_swp");
    TextUpdater tu = buildSwap();
    lang.nextStep();

    // "  int gap = arr.length;"
    exec("v_gap");
    addGap(tu);
    lang.nextStep();

    // "  do {"
    exec("do");
    lang.nextStep();
    do {
      // "    if(gap > 1) {"
      CheckpointUtils.checkpointEvent(this, "gapValue", new Variable("gaps",
          vars.get("gap")));
      exec("if_gap");
      lang.nextStep();

      if (Integer.parseInt(vars.get("gap")) > 1) {
        // "      gap = (int)(gap / 1.3);"
        exec("v_gap_upd");
        lang.nextStep();
      }

      // "    swapped = false;"
      exec("v_swp_f");
      lang.nextStep();

      // "    for(int i = 0, j = gap; j < arr.length; i++, j++) {"
      exec("forInit");
      show(iMarker, jMarker);
      lang.nextStep();

      exec("forCond");
      lang.nextStep();
      for (int i = 0, j = Integer.parseInt(vars.get("gap")); j < arr.length; i++, j++) {
        // "      if(array[i] > array[j]) {"
        exec("if");
        lang.nextStep();
        if (arr[i] > arr[j]) {
          // "        swap(array[i], array[j]);"
          exec("swap");
          int tmp = arr[i];
          arr[i] = arr[j];
          arr[j] = tmp;
          CheckpointUtils.checkpointEvent(this, "swaps", new Variable("ele1",
              arr[i]), new Variable("ele2", arr[j]));
          sArray.swap(i, j, null, time);
          lang.nextStep();

          // "        swapped = true;"
          exec("v_swp_t");
          lang.nextStep();
        }

        // "    for(int i = 0, j = gap; j < arr.length; i++, j++) {"
        exec("forNext");
        lang.nextStep();

        exec("forCond");
        lang.nextStep();
      }

      // "  } while(gap > 1 || swapped);"
      exec("while");
      hide(iMarker, jMarker);
      // to clean up the hidden MarkerArray:
      for (int i = 0; i < arr.length; i++) {
        sArrayM.put(i, sArray.getData(i), null, null);
      }
      hide(sArrayM); // for some reason it gets unhidden
      lang.nextStep();
    } while (Integer.parseInt(vars.get("gap")) > 1
        || "true ".equals(vars.get("swapped")));

    // "// sort complete"
    exec("end");
    hbox.changeColor("color", new Color(0x00B000), null, time);
    header.changeColor("color", new Color(0x00B000), null, time);
    sArray.highlightCell(0, arr.length - 1, null, null);
    sArray.highlightElem(0, arr.length - 1, null, null);
  }

  private void hide(Primitive... prims) {
    for (Primitive prim : prims) {
      prim.hide();
    }
  }

  private void show(Primitive... prims) {
    for (Primitive prim : prims) {
      prim.show();
    }
  }

  private TextUpdater buildSwap() {
    TextProperties tprops = new TextProperties();
    tprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 18));
    TextUpdater tu = new TextUpdater(lang.newText(new Coordinates(20, 180), "",
        "swp_gap", null, tprops));
    tu.addToken("swapped = ");
    tu.addToken(vars.getVariable("swapped"));
    tu.update();
    return tu;
  }

  private void addGap(TextUpdater tu) {
    tu.addToken("   gap = ");
    tu.addToken(vars.getVariable("gap"));
    tu.update();
  }

  private ArrayMarker buildArrayMarker(String var, ArrayMarkerProperties aprops) {
    vars.declare("int", var);
    ArrayMarker aM = lang.newArrayMarker(sArrayM, 1,
        (String) aprops.get(AnimationPropertiesKeys.LABEL_PROPERTY), null,
        aprops);
    ArrayMarkerUpdater aMU = new ArrayMarkerUpdater(aM, null, time, arr_len);
    aMU.setVariable(vars.getVariable(var));
    aM.hide();
    return aM;
  }

  private void buildArray(int[] arr, ArrayProperties aprops) {
    String[] v_arr = buildFixedWidthArray(arr);
    sArrayM = lang.newStringArray(new Coordinates(20, 140), v_arr, "array",
        null, aprops);
    sArrayM.hide();
    sArray = lang.newStringArray(new Coordinates(20, 140), v_arr, "arrayM",
        null, aprops);
    sArray.hide();
  }

  private String[] buildFixedWidthArray(int[] arr) {
    String[] ret = new String[arr.length + 1];
    ret[arr.length] = " ";
    int maxW = 2;
    for (int i = 0; i < arr.length; i++) {
      String t = "" + arr[i];
      maxW = ((t.length() > maxW) ? t.length() : maxW);
    }
    for (int i = 0; i < arr.length; i++) {
      String t = "" + arr[i];
      int d = maxW - t.length();
      for (int j = 0; j < d; j++) {
        t = " " + t + " ";
      }
      ret[i] = t;
    }
    return ret;
  }

  private void buildSource(SourceCodeProperties sprops) {
    sourceCode = lang.newSourceCode(new Coordinates(20, 200), "source", null,
        sprops);
  }

  private void buildHeader() {
    TextProperties tprops = new TextProperties();
    tprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));
    header = lang
        .newText(new Coordinates(20, 20), NAME, "header", null, tprops);
    RectProperties rprops = new RectProperties();
    rprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    hbox = lang.newRect(new Coordinates(17, 11), new Coordinates(146, 48),
        "hbox", null, rprops);
  }

  @Override
  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    init();
    int[] arr = (int[]) arg1.get("Array Werte");
    arr_len = arr.length;
    time = new TicksTiming(100);
    buildHeader();
    buildSource((SourceCodeProperties) arg0
        .getPropertiesByName("QuellText Darstellung"));
    buildArray(arr,
        (ArrayProperties) arg0.getPropertiesByName("Array Darstellung"));
    iMarker = buildArrayMarker("i",
        (ArrayMarkerProperties) arg0.getPropertiesByName("i Marker"));
    jMarker = buildArrayMarker("j",
        (ArrayMarkerProperties) arg0.getPropertiesByName("j Marker"));
    vars.declare("String", "swapped");
    vars.declare("int", "gap");
    parse();
    lang.nextStep();
    sort(arr);
    return lang.toString();
  }

  private final static String AUTHOR      = "Wilhelm 'sOph' Retz";
  private final static String NAME        = "Comb Sort";
  private final static String DESCRIPTION = "Beim CombSort handelt es sich um eine Variante des Bubblesort Algorithmus. "
                                              + "Dabei werden, anstatt direkt benachbarte, weiter entfernte Elemente mit "
                                              + "einander verglichen und der Abstand zwischen den zu vergleichenden "
                                              + "Elementen mit jedem Schleifendurchlauf verringert. Somit wird erreicht, "
                                              + "dass Elemente schneller an ihre ungefähre Optimalposition gelangen.<br\\>"
                                              + "Das Sortieren endet, sobald ein Durchlauf mit benachbarten Vergleichen "
                                              + "keinen Austausch zur Folge hat.";

  @Override
  public String getAnnotatedSrc() {
    return "public void combSort(int[] arr) {@label(\"header\")\n"
        + " boolean swapped = true;@label(\"v_swp\")@set(\"swapped\", \"true \")\n"
        + " int gap = arr.length;@label(\"v_gap\")@set(\"gap\", \""
        + arr_len
        + "\")\n"
        + " do {@label(\"do\")\n"
        + "  if(gap > 1) {@label(\"if_gap\")\n"
        + "   gap = (int)(gap / 1.3);@label(\"v_gap_upd\")@eval(\"gap\", \"gap / 1.3\")\n"
        + "  }@label(\"none1\")\n"
        + "  swapped = false;@label(\"v_swp_f\")@set(\"swapped\", \"false\")@eval(\"gap\", \"gap\")\n"
        + "  for(int i = 0, j = gap;@label(\"forInit\")@set(\"i\", \"0\")@eval(\"j\", \"gap\")\n"
        + "j < arr.length;@label(\"forCond\")@continue\n"
        + "i++, j++) {@label(\"forNext\")@continue@inc(\"i\")@inc(\"j\")\n"
        + "   if(array[i] > array[j]) {@label(\"if\")\n"
        + "    swap(array[i], array[j]);@label(\"swap\")\n"
        + "    swapped = true;@label(\"v_swp_t\")@set(\"swapped\", \"true \")@eval(\"gap\", \"gap\")\n"
        + "   }@label(\"none2\")\n" + "  }@label(\"none3\")\n"
        + " } while(gap > 1 || swapped);@label(\"while\")\n"
        + "}@label(\"none4\")\n" + "// sort complete@label(\"end\")";
  }

  @Override
  public String getAlgorithmName() {
    return NAME;
  }

  @Override
  public String getAnimationAuthor() {
    return AUTHOR;
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return DESCRIPTION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }
}