package generators.sorting.swapsort;

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
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class SwapSort2 implements Generator {
  protected Language            lang;
  private ArrayProperties       arrayProps;
  private ArrayMarkerProperties ami;
  private SourceCode            sc;
  private static final String[] DESCRIPTION = {
      "        Die Idee von Swap-Sort ist, von jedem Element eines Arrays A(1..n) die Anzahl m",
      "der kleineren Werte (die in A sind) zu zählen und das Element dann mit dem Element",
      "in A( m+1 ) zu vertauschen. Somit ist sichergestellt, dass das ausgetauschte Element",
      "bereits an der richtigen, also endgültigen Stelle steht.",
      "        Nachteil dieses Algorithmus ist, dass jedes Element nur einmal vorkommen darf,",
      "da sonst keine Terminierung erfolgt." };

  public SwapSort2() {
  }

  public void init() { // initializethemainelements
    // Generate a new Language instance for content creation
    // Parameter: Animation title, author, width, height
    lang = new AnimalScript("Swap-Sort", "Lu,Zheng Yin,Yanan Yang,Yang", 640,
        480);
    // Activate stepcontrol
    lang.setStepMode(true);
    // Text t = lang.newText(new Coordinates(10, 100),DESCRIPTION[0], "des",
    // null);
    lang.addLine("text \"des1\"\"" + DESCRIPTION[0]
        + " \"(20,35) color (0,0,0) depth 1 font SansSerif size 20 bold\n");
    lang.addLine("text \"des2\"\"" + DESCRIPTION[1]
        + " \"(20,70) color (0,0,0) depth 1 font SansSerif size 20 bold\n");
    lang.addLine("text \"des3\"\"" + DESCRIPTION[2]
        + " \"(20,105) color (0,0,0) depth 1 font SansSerif size 20 bold\n");
    lang.addLine("text \"des4\"\"" + DESCRIPTION[3]
        + " \"(20,140) color (0,0,0) depth 1 font SansSerif size 20 bold\n");
    lang.addLine("text \"des5\"\"" + DESCRIPTION[4]
        + " \"(20,175) color (0,0,0) depth 1 font SansSerif size 20 bold\n");
    lang.addLine("text \"des6\"\"" + DESCRIPTION[5]
        + " \"(20,210) color (0,0,0) depth 1 font SansSerif size 20 bold\n}\n");
    lang.addLine("{\nhideAll\n");
    // create array properties with default values
    arrayProps = new ArrayProperties();
    // Redefine properties: border red, filled with gray
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK); // colorred
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // filled
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW); // fillcolorgray
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    ami = new ArrayMarkerProperties();
    ami.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    ami.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");

  }

  public void swapSort(int[] inputArray) {
    IntArray array = lang.newIntArray(new Coordinates(650, 100), inputArray,
        "array", null, arrayProps);
    Timing defaultTiming = new TicksTiming(15);
    Timing changeTiming = new TicksTiming(30);
    lang.addLine("text \"des7\" \"kleineren Werte: \" (600,150) color (0,0,0) depth 1 font SansSerif size 20 \n");
    lang.addLine("text \"des8\" \"Start Werte:\" (600,180) color (0,0,0) depth 1 font SansSerif size 20 \n");

    showSourceCode(); // showthesourcecode
    sc.highlight(0); // methodhead
    lang.nextStep(); // to show array without markers
    sc.toggleHighlight(0, 1);
    ArrayMarker i = lang.newArrayMarker(array, 0, "i", null, ami);

    lang.nextStep(); // to show imarker
    sc.toggleHighlight(1, 2);
    i.move(0, null, defaultTiming);
    lang.nextStep();
    while (i.getPosition() < array.getLength() - 1) {
      int kleinere = countSmallerOnes(inputArray, i.getPosition());
      array.highlightElem(kleinere + i.getPosition(), null, defaultTiming);
      sc.unhighlight(2);
      sc.unhighlight(3);
      sc.unhighlight(5);
      sc.unhighlight(6);
      sc.unhighlight(7);
      sc.unhighlight(10);
      sc.highlight(3);
      sc.highlight(14);
      lang.addLine("hide \"des7\"");
      lang.addLine("hide \"des8\"");
      lang.addLine("text \"des7\" \"kleineren Werte: " + kleinere
          + "\" (600,150) color (0,0,0) depth 1 font SansSerif size 20 \n");
      lang.addLine("text \"des8\" \"Start Werte: " + i.getPosition()
          + "\" (600,180) color (0,0,0) depth 1 font SansSerif size 20 \n");

      lang.nextStep();
      sc.toggleHighlight(3, 4);
      sc.unhighlight(14);
      lang.nextStep();
      if (kleinere > 0) {

        // int tmp = inputArray[i.getPosition()];
        // inputArray[i.getPosition()] = inputArray[i.getPosition() +
        // min.getPosition()];
        // inputArray[i.getPosition() + min.getPosition()] = tmp;
        sc.toggleHighlight(4, 5);
        sc.highlight(6);
        sc.highlight(7);
        array.unhighlightElem(kleinere + i.getPosition(), null, defaultTiming);
        array.swap(i.getPosition(), i.getPosition() + kleinere, null,
            changeTiming);
        lang.nextStep();

      } else {
        sc.unhighlight(3);
        sc.unhighlight(4);
        sc.highlight(10);
        array.unhighlightElem(kleinere + i.getPosition(), null, defaultTiming);
        // startwert++;
        array.highlightCell(i.getPosition(), null, defaultTiming);
        i.move(i.getPosition() + 1, null, defaultTiming);
        lang.nextStep();
      }
    }
    array.highlightCell(i.getPosition(), null, defaultTiming);
    lang.addLine("text \"des8\" \"Der Algorithmus ist fertig!\" (550,500) color (0,0,0) depth 1 font SansSerif size 22 bold\n");
  }

  private int countSmallerOnes(final int[] countHere, final int index) {
    int counter = 0;
    for (int i = index + 1; i < countHere.length; i++) {
      if (countHere[index] > countHere[i]) {
        counter++;
      }
    }
    return counter;
  }

  public void showSourceCode() {
    // first, set the visual properties for the source code
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 18));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    // now, create the source code entity
    sc = lang.newSourceCode(new Coordinates(40, 50), "sourceCode", null,
        scProps);
    // adda codeline
    // parameters: codeitself; name(canbenull); indentationlevel; displayoptions
    sc.addCodeLine("public void sort(int[] sortMe) {", null, 0, null);
    sc.addCodeLine("int startwert = 0;", null, 1, null);
    sc.addCodeLine("while (startwert < sortMe.length - 1) {", null, 1, null);
    sc.addCodeLine("int kleinere = countSmallerOnes(sortMe, startwert);", null,
        2, null);
    sc.addCodeLine("if (kleinere > 0) {", null, 2, null);
    sc.addCodeLine("int tmp = sortMe[startwert];", null, 3, null);
    sc.addCodeLine("sortMe[startwert] = sortMe[startwert + kleinere];", null,
        3, null);
    sc.addCodeLine("sortMe[startwert + kleinere] = tmp;", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("else{", null, 2, null);
    sc.addCodeLine("startwert++;", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("}", null, 0, null);
    sc.addCodeLine(
        "private int countSmallerOnes(final int[] countHere, final int index) {",
        null, 0, null);
    sc.addCodeLine("int counter = 0;", null, 1, null);
    sc.addCodeLine("for (int i = index + 1; i < countHere.length; i++) {",
        null, 1, null);
    sc.addCodeLine("if (countHere[index] > countHere[i]) {", null, 2, null);
    sc.addCodeLine("counter++;", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("return counter;", null, 1, null);
    sc.addCodeLine("}", null, 0, null);
  }

  public String getCodeExample() {
    return "public class SwapSorter {\n" + "public void sort(int[] sortMe) {\n"
        + "int startwert = 0;\n" + "while (startwert < sortMe.length - 1) {\n"
        + "int kleinere = countSmallerOnes(sortMe, startwert);\n"
        + "if (kleinere > 0) {\n" + "int tmp = sortMe[startwert];\n"
        + "sortMe[startwert] = sortMe[startwert + kleinere];\n"
        + "sortMe[startwert + kleinere] = tmp;\n" + "}\n" + "else{\n"
        + "startwert++;\n" + "}\n" + "}\n" + "}\n" +

        "private int countSmallerOnes(final int[] countHere, final int index) {\n"
        + "int counter = 0;\n"
        + "for (int i = index + 1; i < countHere.length; i++) {\n"
        + "if (countHere[index] > countHere[i]) {\n" + "counter++;\n" + "}\n"
        + "}\n" + "return counter;\n" + "}";
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getDescription() {
    return "Die Idee von Swap-Sort ist, von jedem Element eines Arrays A(1..n) die Anzahl m "
        + "der kleineren Werte (die in A sind) zu zählen und das Element dann mit dem Element "
        + "in A( m+1 ) zu vertauschen. Somit ist sichergestellt, dass das ausgetauschte Element "
        + "bereits an der richtigen, also endgültigen Stelle steht.\n "
        + "Nachteil dieses Algorithmus ist, dass jedes Element nur einmal vorkommen darf, "
        + "da sonst keine Terminierung erfolgt.";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
 }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getName() {
    return "Swap Sort";
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    init(); // ensure all properties are set up :-)
    int[] arrayData = (int[]) primitives.get("array");
    // adapt the COLOR to whatever the user chose
    // you could do this for all properties if you wanted to...
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("array", AnimationPropertiesKeys.COLOR_PROPERTY));
    // call the selection sort method
    swapSort(arrayData);
    return lang.toString();
  }

  public String getAlgorithmName() {
    return "Swap Sort";
  }

  public String getOutputLanguage() {
    // TODO Auto-generated method stub
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public String getAnimationAuthor() {
    return "Zheng Lu, Yanan Yin, Yang Yang";
  }
}
