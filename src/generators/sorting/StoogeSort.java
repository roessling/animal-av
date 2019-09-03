package generators.sorting;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;

public class StoogeSort implements generators.framework.Generator {

  protected Language lang;

  protected int      ctr = 0;

  public StoogeSort() {
    init();
  }

  public StoogeSort(Language l) {
    lang = l;
    lang.setStepMode(true);
  }

  private void sort(int[] a, ArrayProperties aprops,
      SourceCodeProperties scprops, TextProperties tp, TextProperties tp2) {

    IntArray ia = lang.newIntArray(new Coordinates(20, 100), a, "intArray",
        null, aprops);

    lang.nextStep();

    Text st = lang.newText(new Coordinates(400, 140), "s = ", "s", null, tp);
    Text et = lang.newText(new Coordinates(400, 160), "e = ", "e", null, tp);
    Text len = lang.newText(new Coordinates(400, 180), "len = ", "len", null,
        tp);
    Text third = lang.newText(new Coordinates(400, 200), "third = ", "third",
        null, tp);
    Text rt = lang.newText(new Coordinates(400, 250), "Rekursionstiefe = 0",
        "rtiefe", null, tp);
    Text zt = lang.newText(new Coordinates(400, 270), "Zurück zur Tiefe ",
        "zt", null, tp2);
    len.hide();
    third.hide();
    zt.hide();

    SourceCode sc = lang.newSourceCode(new Coordinates(40, 140), "sourceCode",
        null, scprops);

    sc.addCodeLine("public void stoogeSort(int[] a)", null, 0, null); // 0
    sc.addCodeLine("{", null, 0, null); // 1
    sc.addCodeLine("stoogeSort(a,0,a.length);", null, 2, null); // 2
    sc.addCodeLine("}", null, 0, null); // 3
    sc.addCodeLine("", null, 0, null); // 4
    sc.addCodeLine("private void stoogeSort(int[] a,int s,int e)", null, 0,
        null); // 5
    sc.addCodeLine("{", null, 0, null); // 6
    sc.addCodeLine("if (a[e-1] < a[s])", null, 1, null); // 7
    sc.addCodeLine("{", null, 1, null); // 8
    sc.addCodeLine("swap(s, e - 1);", null, 2, null); // 9
    sc.addCodeLine("}", null, 1, null); // 10
    sc.addCodeLine("int len = e - s;", null, 1, null); // 11
    sc.addCodeLine("if(len > 2)", null, 1, null); // 12
    sc.addCodeLine("{", null, 1, null); // 13
    sc.addCodeLine("int third=len/3;", null, 2, null); // 14
    sc.addCodeLine("stoogeSort(a,s,e-third);", null, 2, null); // 15
    sc.addCodeLine("stoogeSort(a,s+third,e);", null, 2, null); // 16
    sc.addCodeLine("stoogeSort(a,s,e-third);", null, 2, null); // 17
    sc.addCodeLine("}", null, 1, null); // 18
    sc.addCodeLine("}", null, 0, null); // 19

    lang.nextStep();
    sc.highlight(0);

    lang.nextStep();
    sc.toggleHighlight(0, 1);

    lang.nextStep();
    sc.toggleHighlight(1, 2);

    lang.nextStep();
    sc.toggleHighlight(2, 3);

    lang.nextStep();
    sc.unhighlight(3);

    try {
      stoogeSort(ia, sc, len, et, st, third, rt, zt, 0, ia.getLength(), 0);
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
  }

  private void stoogeSort(IntArray array, SourceCode codeSupport, Text lentxt,
      Text etxt, Text stxt, Text thirdtxt, Text rt, Text zt, int l, int r,
      int tiefe) {

    rt.setText("Rekursionstiefe = " + tiefe, null, null);
    stxt.setText("s = " + l, null, null);
    etxt.setText("e = " + r, null, null);
    zt.hide();
    codeSupport.highlight(5);
    lang.nextStep();

    codeSupport.toggleHighlight(5, 6);
    lang.nextStep();

    codeSupport.toggleHighlight(6, 7);

    array.highlightCell(l, null, null);
    array.highlightCell(r - 1, null, null);
    lang.nextStep();

    if (array.getData(r - 1) < array.getData(l)) {
      codeSupport.toggleHighlight(7, 9);
      CheckpointUtils.checkpointEvent(this, "eventSwap", new Variable("ele1",
          array.getData(r - 1)), new Variable("ele2", array.getData(l)));
      array.swap(l, r - 1, new TicksTiming(25), new TicksTiming(25));
      lang.nextStep();
      array.unhighlightCell(l, null, null);
      array.unhighlightCell(r - 1, null, null);
      codeSupport.toggleHighlight(9, 11);
      lang.nextStep();
    } else {
      array.unhighlightCell(l, null, null);
      array.unhighlightCell(r - 1, null, null);
      codeSupport.toggleHighlight(7, 11);
      lang.nextStep();
    }
    int len = r - l;
    codeSupport.toggleHighlight(11, 12);
    lentxt.setText("len = " + len, null, null);
    lentxt.show();
    lang.nextStep();
    if (len > 2) {
      codeSupport.toggleHighlight(12, 14);
      lang.nextStep();
      ctr++;
      int third = len / 3;
      thirdtxt.setText("third = " + third, null, null);
      thirdtxt.show();
      codeSupport.toggleHighlight(14, 15);
      lang.nextStep();
      codeSupport.unhighlight(15);
      stoogeSort(array, codeSupport, lentxt, etxt, stxt, thirdtxt, rt, zt, l, r
          - third, tiefe + 1);
      rt.setText("Rekursionstiefe = " + tiefe, null, null);
      stxt.setText("s = " + l, null, null);
      etxt.setText("e = " + r, null, null);
      lentxt.setText("len = " + len, null, null);
      thirdtxt.setText("third = " + third, null, null);
      zt.setText("Zurück zur Tiefe " + tiefe + "!", null, null);
      zt.show();
      codeSupport.highlight(16);
      lang.nextStep();
      zt.hide();
      codeSupport.unhighlight(16);
      stoogeSort(array, codeSupport, lentxt, etxt, stxt, thirdtxt, rt, zt, l
          + third, r, tiefe + 1);
      rt.setText("Rekursionstiefe = " + tiefe, null, null);
      stxt.setText("s = " + l, null, null);
      etxt.setText("e = " + r, null, null);
      lentxt.setText("len = " + len, null, null);
      thirdtxt.setText("third = " + third, null, null);
      zt.setText("Zurück zur Tiefe " + tiefe + "!", null, null);
      zt.show();
      codeSupport.highlight(17);
      lang.nextStep();
      zt.hide();
      codeSupport.unhighlight(17);
      stoogeSort(array, codeSupport, lentxt, etxt, stxt, thirdtxt, rt, zt, l, r
          - third, tiefe + 1);
      stxt.setText("s = ", null, null);
      etxt.setText("e = ", null, null);
      lentxt.setText("len = ", null, null);
      thirdtxt.setText("third = ", null, null);
      rt.setText("Rekursionstiefe = 0", null, null);
      zt.setText("Fertig!", null, null);
      zt.show();
    } else {
      codeSupport.toggleHighlight(12, 19);
      lang.nextStep();
      codeSupport.unhighlight(19);
    }
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    init();
    int[] ia = (int[]) primitives.get("Input Data");
    ArrayProperties ap = (ArrayProperties) props
        .getPropertiesByName("arrayProps");
    SourceCodeProperties scp = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProps");
    TextProperties tp = (TextProperties) props.getPropertiesByName("textProps");
    TextProperties tp2 = (TextProperties) props
        .getPropertiesByName("textProps2");

    System.out.println(ia);
    sort(ia, ap, scp, tp, tp2);
    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Stooge Sort";
  }

  @Override
  public String getAnimationAuthor() {
    return "Vyacheslav Polonskyy";
  }

  @Override
  public String getCodeExample() {
    return "// Interface-Methode (um den Aufruf mit den richtigen Startwerten zu erzwingen)"
        + "\n"
        + "public void stoogeSort(int[] a)"
        + "\n"
        + "  {"
        + "\n"
        + "   stoogeSort(a,0,a.length);"
        + "\n"
        + "  }"
        + "\n"
        + "\n"
        + "// Interne Methode"
        + "\n"
        + "private void stoogeSort(int[] a,int s,int e)"
        + "\n"
        + "{"
        + "\n"
        + "   if(a[s] > a[e-1])"
        + "\n"
        + "   {"
        + "\n"
        + "     int temp=a[s];"
        + "\n"
        + "     a[s]=a[e-1];"
        + "\n"
        + "     a[e-1]=temp;"
        + "\n"
        + "   }"
        + "\n"
        + "   int len=e-s;"
        + "\n"
        + "   if(len>2)"
        + "\n"
        + "   {"
        + "\n"
        + "     int third=len/3;   // Zur Erinnerung: Dies ist die (abgerundete) Integer-Division"
        + "\n"
        + "     stoogeSort(a,s,e-third);"
        + "\n"
        + "     stoogeSort(a,s+third,e);"
        + "\n"
        + "     stoogeSort(a,s,e-third);" + "\n" + "   }" + "\n" + "}" + "\n";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return "Sind das erste und das letzte Element nicht in der richtigen Reihenfolge, so werden sie vertauscht."
        + "\n"
        + "Sind mehr als zwei Elemente in der Liste, fortsetzen, ansonsten abbrechen."
        + "\n"
        + "Sortiere die ersten zwei Drittel der Liste."
        + "\n"
        + "Sortiere die letzten zwei Drittel der Liste.";
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
    return "Stooge Sort";
  }

  @Override
  public String getOutputLanguage() {
    return generators.framework.Generator.JAVA_OUTPUT;
  }

  @Override
  public void init() {
    lang = new AnimalScript("Stoogesort Animation", "Vyacheslav Polonskyy",
        640, 480);
    lang.setStepMode(true);
  }
}
