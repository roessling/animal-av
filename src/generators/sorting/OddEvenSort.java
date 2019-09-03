package generators.sorting;

import generators.AnnotatedAlgorithm;
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
import algoanim.util.MsTiming;
import algoanim.util.Offset;

public class OddEvenSort extends AnnotatedAlgorithm implements
    generators.framework.Generator {

  private String                comp = "Compares";

  private String                assi = "Assignments";

  private Language              lang;

  private IntArray              array;

  private ArrayProperties       arrayProps;

  private ArrayMarker           i, j;

  private ArrayMarkerProperties iProps, jProps;

  private SourceCodeProperties  descriptionProps;

  private SourceCode            description;

  private Text                  title, swapped;

  private TextProperties        titleProps, finalTextProps;

  // private Rect rect;

  private String                finalString;

  // SourceCode sourceCode;

  private void setTitleProps() {
    titleProps = new TextProperties();
    titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SERIF,
        Font.BOLD, 24));

    lang.setStepMode(true);
  }

  private void setTitle() {
    RectProperties rectProp = new RectProperties();
    rectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.gray);
    rectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    lang.newRect(new Coordinates(1, 1), new Coordinates(200, 50), "rect", null,
        rectProp);

    title = lang.newText(new Coordinates(8, 8), "Odd-even sort", "title", null,
        titleProps);

  }

  private void setDescription() {
    descriptionProps = new SourceCodeProperties();
    descriptionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SERIF, Font.PLAIN, 18));
    description = lang.newSourceCode(new Offset(2, 12, title, "southwest"),
        "des", null, descriptionProps);
    description
        .addCodeLine(
            "Odd-even sort functions by comparing all (odd, even)-indexed pairs of adjacent elements in the list",
            "0", 0, null);
    description
        .addCodeLine(
            "and if a pair is in the wrong order (the first is larger than the second) the elements are switched.",
            "1", 0, null);
    description
        .addCodeLine(
            "Then it alternates between (odd, even) and (even, odd) steps until the list is sorted.",
            "2", 0, null);
  }

  public OddEvenSort() {
    lang = new AnimalScript("Odd-even sort", "Naseri, Parisay, Gonen", 0, 0);
    lang.setStepMode(true);
    init();
  }

  public void sort(IntArray a) {
    lang.nextStep();
    i = lang.newArrayMarker(array, 0, "i", null, iProps);
    i.hide();
    j = lang.newArrayMarker(array, 0, "j", null, jProps);
    j.hide();

    exec("init");
    swapped = lang.newText(new Offset(10, 40, array, "south"),
        "sorted = false", "sorted", null);
    lang.nextStep();
    exec("while");

    boolean ihide = true;
    boolean jhide = true;
    while (vars.get("swapped").compareTo("0") == 0) {
      lang.nextStep();
      exec("sortedT");
      swapped.setText("sorted = true", new MsTiming(100), null);

      lang.nextStep();
      exec("for1init");
      vars.set("i", "1");
      lang.nextStep();

      while (Integer.parseInt(vars.get("i")) < array.getLength() - 1) {

        i.move(Integer.parseInt(vars.get("i")), null, new MsTiming(200));
        j.move(Integer.parseInt(vars.get("i")) + 1, null, new MsTiming(200));

        if (jhide) {
          j.show();
          jhide = false;
        }
        if (ihide) {
          i.show();
          ihide = false;
        }
        lang.nextStep();
        exec("for1Comp");

        lang.nextStep();
        exec("if1");
        int i = Integer.parseInt(vars.get("i"));
        if (array.getData(i) > array.getData(i + 1)) {
          lang.nextStep();
          exec("swap1");
          array.swap(i, i + 1, null, new MsTiming(200));
          array.highlightCell(i, null, new MsTiming(300));
          array.highlightCell(i + 1, null, new MsTiming(300));
          lang.nextStep();
          array.unhighlightCell(i, null, new MsTiming(300));
          array.unhighlightCell(i + 1, null, new MsTiming(300));
          exec("sortedF1");
          swapped.setText("sorted = false", new MsTiming(100), null);

        }

        lang.nextStep();
        exec("for1inc");

      }
      lang.nextStep();
      if (array.getLength() % 2 == 0) {
        i.move(Integer.parseInt(vars.get("i")), null, new MsTiming(200));
        this.j.hide();
        jhide = true;
      } else {
        this.j.hide();
        jhide = true;
        i.hide();
        ihide = true;
      }

      lang.nextStep();
      exec("for1Comp");

      lang.nextStep();
      exec("for2init");
      vars.set("i", "0");

      while (Integer.parseInt(vars.get("i")) < array.getLength() - 1) {

        this.i.move(Integer.parseInt(vars.get("i")), null, new MsTiming(200));

        j.move(Integer.parseInt(vars.get("i")) + 1, null, new MsTiming(200));
        if (jhide) {
          j.show();
          jhide = false;
        }
        if (ihide) {
          i.show();
          ihide = false;
        }
        lang.nextStep();
        exec("for2Comp");

        lang.nextStep();
        exec("if2");
        int i = Integer.parseInt(vars.get("i"));
        if (array.getData(i) > array.getData(i + 1)) {
          lang.nextStep();
          exec("swap2");
          array.highlightCell(i, null, new MsTiming(300));
          array.highlightCell(i + 1, null, new MsTiming(300));
          array.swap(i, i + 1, null, new MsTiming(200));
          lang.nextStep();
          array.unhighlightCell(i, null, new MsTiming(300));
          array.unhighlightCell(i + 1, null, new MsTiming(300));
          exec("sortedF2");
          swapped.setText("sorted = false", new MsTiming(100), null);

        }

        lang.nextStep();
        exec("for2inc");

      }
      lang.nextStep();
      if (array.getLength() % 2 == 0) {
        this.i.hide();
        ihide = true;
        this.j.hide();
        jhide = true;
      } else {
        i.move(Integer.parseInt(vars.get("i")), null, new MsTiming(200));
        this.j.hide();
        jhide = true;
      }
      lang.nextStep();
      exec("for2Comp");

      lang.nextStep();
      exec("while");
    }

  }

  /**
   * swaps two element of an array
   * 
   * @param a
   * @param b
   */
  public void swap(int a, int b, int[] array) {
    int temp = array[a];
    array[a] = array[b];
    array[b] = temp;
  }

  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {

    arrayProps = (ArrayProperties) arg0.getPropertiesByName("arrayProps");
    iProps = (ArrayMarkerProperties) arg0.getPropertiesByName("i");
    jProps = (ArrayMarkerProperties) arg0.getPropertiesByName("j");
    array = lang.newIntArray(new Offset(300, 0, sourceCode, "east"),
        (int[]) arg1.get("array"), "array", null, arrayProps);

    sort(array);
    lang.nextStep();
    sourceCode.hide();

    finalString = (String) arg1.get("finalText");
    setFinalTextProp();
    setFinleText();
    return lang.toString();

  }

  // properties that will be show at the end of animation
  private void setFinalTextProp() {
    finalTextProps = new TextProperties();
    finalTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SERIF, Font.BOLD, 24));
  }

  // the final text at the end of animation
  private void setFinleText() {

    lang.newText(new Coordinates(5, 120), finalString, "final", null,
        finalTextProps);
  }

  public String getAlgorithmName() {
    return "Odd-Even-Sort";
  }

  public String getAnimationAuthor() {
    return "Ardalan Naseri, Mohsen Parisay, Yanai Gonen";
  }

  @Override
  public String getCodeExample() {

    return "sorted := false " + "\n" + "while (sorted = false)" + "\n"
        + " sorted = true" + "\n"
        + " for i := 1 to i < array.Length -1 ; i + 2" + "\n"
        + "  if array[i] > array [i+1]" + "\n" + "   swap(a[i],a[i+1])" + "\n"
        + "   sorted = false" + "\n"
        + "  for i := 0 to i < array.Length -1 ; i + 2" + "\n"
        + "   if array[i] > array [i+1]" + "\n" + "    swap(a[i],a[i+1])"
        + "\n" + "    sorted := false";

  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public String getDescription() {
    return "It is a comparison sort based on bubble sort with which it shares many characteristics.";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getName() {
    return "Odd-even Sort";
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  public void init() {
    super.init();

    setTitleProps();
    setTitle();
    setDescription();
    lang.nextStep();
    description.hide();

    SourceCodeProperties props = new SourceCodeProperties();
    props.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
    props.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);
    props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SERIF,
        Font.BOLD, 14));

    // instantiate source code primitive to work with
    sourceCode = lang.newSourceCode(new Coordinates(20, 100), "sumupCode",
        null, props);

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

  public String getAnnotatedSrc() {

    return "sorted := false  			    @label(\"init\") @declare(\"int\", \"swapped\")   @inc(\""
        + assi
        + "\")\n"
        + "while (sorted = false)	    @label(\"while\") @inc(\""
        + comp
        + "\")\n"
        + " sorted = true		   	    @label(\"sortedT\") @set(\"swapped\", \"1\") @inc(\""
        + assi
        + "\")\n"
        + " for i := 1 to 		    @label(\"for1init\") @declare(\"int\", \"i\") 	@inc(\""
        + assi
        + "\")\n"
        + "i < array.Length -1 ;	    @label(\"for1Comp\") @continue @inc(\""
        + comp
        + "\")\n"
        + " i + 2 			       	@label(\"for1inc\") @inc(\"i\") @inc(\"i\")  @continue @inc(\""
        + assi
        + "\")\n"

        + "  if array[i] > array [i+1]  @label(\"if1\") @inc(\""
        + comp
        + "\")\n"
        + "   swap(a[i],a[i+1]) 	    @label(\"swap1\") @inc(\""
        + assi
        + "\")"
        + " @inc(\""
        + assi
        + "\")        \n"
        + "   sorted = false       		@label(\"sortedF1\") @set(\"swapped\", \"0\") @inc(\""
        + assi
        + "\")\n"

        + " for i := 0 to 	   	 @label(\"for2init\") @declare(\"int\", \"i\") @inc(\""
        + assi
        + "\")\n"
        + "i < array.Length -1 ;	    @label(\"for2Comp\") @continue @inc(\""
        + comp
        + "\")\n"
        + " i + 2 				@label(\"for2inc\") @inc(\"i\") @inc(\"i\")  @continue @inc(\""
        + assi
        + "\")\n"

        + "  if array[i] > array [i+1]  @label(\"if2\") @inc(\""
        + comp
        + "\")\n"
        + "   swap(a[i],a[i+1]) 	@label(\"swap2\") @inc(\""
        + assi
        + "\")"
        + " @inc(\""
        + assi
        + "\")        \n"
        + "   sorted = false 		@label(\"sortedF2\")  @set(\"swapped\", \"0\") @inc(\""
        + assi + "\")\n";

  }

}