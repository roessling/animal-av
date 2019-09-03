package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
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
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;

public class SimpleSort implements Generator {
  Language              l;
  ArrayProperties       ap;
  ArrayMarkerProperties lgp; // links von diesem Marker ist alles sortiert
  ArrayMarkerProperties akp; // Zeiger auf das gerade zu bearbeitende Element
  TextProperties        tp;
  SourceCodeProperties  scp;

  public SimpleSort() {
    l = new AnimalScript("SimpleSort", "Eduard Metlewski", 200, 200);
    l.setStepMode(true);
    // Array-Eigenschaften
    ap = new ArrayProperties();
    ap.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLUE);
    ap.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    ap.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.green);
    ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
    // Marker-lgp Eigenschaften
    lgp = new ArrayMarkerProperties();
    lgp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
    lgp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "S");
    // Marker-akp Eigenschaften
    akp = new ArrayMarkerProperties();
    akp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    akp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "A");
    // Text-Eigenschaften
    tp = new TextProperties();
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 16));
    tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.CYAN);
    tp.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
    // SoursCode-Eigenschaften
    scp = new SourceCodeProperties();
    scp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    scp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getName() {
    return "Simple Sort";
  }

  public String getDescription() {
    return "Simple Sort hat eine Komplexit&auml;t von  O(n^2)";
  }

  public String getCodeExample() {
    String s = "for (int i = 0;i &lt; array.langth; i++) {"
        + "\n  for (int j = i + 1; j &lt; array.length; j++){\n"
        + "    if (array[i] &gt; array[j]){\n      	swap(array[i], array[j]);\n"
        + "    }\n  }\n}";
    return s;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  public String getAlgorithmName() {
    return "SimpleSort";
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    // Initialisierung der Animation
    int[] ar = (int[]) primitives.get("a");
    MsTiming dur = new MsTiming(300);
    l.newText(new Coordinates(50, 10), "SimpleSort", "titel", null, tp);
    if (ar.length < 2)
      return null;
    IntArray a = l.newIntArray(new Coordinates(50, 150), ar, "array", null, ap);
    ArrayMarker lm = l.newArrayMarker(a, 0, "links", null, lgp);
    ArrayMarker rm = l.newArrayMarker(a, 1, "rechts", null, akp);
    SourceCode code = l.newSourceCode(new Coordinates(50, 200), "code", null,
        scp);
    code.addCodeLine("0.for(int i=0;i<array.langth;i++)   {", null, 0, null);
    code.addCodeLine("1.      for(int j=i+1;j<array.length;j++)   {", null, 0,
        null);
    code.addCodeLine("2.          if(array[i] > array[j])   {", null, 0, null);
    code.addCodeLine("3.                  ja-->swap(array(i),array(j));", null,
        0, null);
    code.addCodeLine("4.          }", null, 0, null);
    code.addCodeLine("5.      }", null, 0, null);
    code.addCodeLine("6.}", null, 0, null);

    // Algorithmus
    l.nextStep();
    code.highlight(0);
    code.highlight(6);
    for (int i = 0; i < a.getLength(); i++) {
      l.nextStep();
      lm.move(i, null, dur);
      l.nextStep();
      a.highlightElem(i, null, null);
      l.nextStep();
      code.highlight(1);
      code.highlight(5);
      l.nextStep();
      for (int j = i + 1; j < a.getLength(); j++) {
        rm.move(j, null, dur);
        l.nextStep();
        a.highlightElem(j, null, null);
        l.nextStep();
        code.highlight(2);
        code.highlight(4);
        l.nextStep();
        if (a.getData(i) > a.getData(j)) {
          code.highlight(3);

          l.nextStep();
          a.swap(i, j, null, dur);
          l.nextStep();
          code.unhighlight(3);

          l.nextStep();
        }
        code.unhighlight(2);
        code.unhighlight(4);
        l.nextStep();
        a.unhighlightElem(j, null, null);
        l.nextStep();
      }
      l.nextStep();
      code.unhighlight(1);
      code.unhighlight(5);
      l.nextStep();
      a.unhighlightElem(i, null, null);
      a.highlightCell(i, null, null);
    }
    code.hide();
    lm.hide();
    rm.hide();

    return l.toString();
  }

  public String getAnimationAuthor() {
    return "Eduard Metlewski";
  }

  public void init() {
    // TODO Auto-generated method stub

  }

}
