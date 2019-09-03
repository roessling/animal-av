package generators.sorting.swapsort;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;

public class SwapSort3 implements generators.framework.Generator {

  private Language lang;

  private String   sourceCode = ""
                                  + "0. public void swapSort(int[] array) {"
                                  + "\n"
                                  + "1.   int startIndex = 0;"
                                  + "\n"
                                  + "2.   while(startIndex < array.length - 1) {"
                                  + "\n"
                                  + "3.     int smaller = countSmallerOnes(array , startIndex);"
                                  + "\n"
                                  + "4.     if(smaller > 0) {"
                                  + "\n"
                                  + "5.       swap(startIndex , startIndex + smaller);"
                                  + "\n"
                                  + "6.     }"
                                  + "\n"
                                  + "7.     else {"
                                  + "\n"
                                  + "8.       startIndex++;"
                                  + "\n"
                                  + "9.    }"
                                  + "\n"
                                  + "10. }"
                                  + "\n"
                                  + "11.}"
                                  + "\n"
                                  + "12."
                                  + "\n"
                                  + "13.private int countSmallerOnes(final int[] array, final int index) {"
                                  + "\n"
                                  + "14.  int counter = 0;"
                                  + "\n"
                                  + "15.  for (int i = index + 1; i < array.length; i++) {"
                                  + "\n"
                                  + "16.    if(array[index] > array[i]){"
                                  + "\n" + "17.      counter++;" + "\n"
                                  + "18.    }" + "\n" + "19.  }" + "\n"
                                  + "20.  return counter;" + "\n" + "21.}";

  public SwapSort3() {
    lang = new AnimalScript(getAlgorithmName(), getAnimationAuthor(), 720, 480);
    lang.setStepMode(true);
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    init();

    SourceCode sourceCode = lang.newSourceCode(new Coordinates(40, 140),
        "sourceCode", null, (SourceCodeProperties) props
            .getPropertiesByName("sourceCode"));
    addLinesToSourceCode(sourceCode);

    lang.newRect(new Coordinates(250, 50), new Coordinates(700, 100), "r1",
        null);
    lang.newRect(new Coordinates(250, 50), new Coordinates(480, 100), "r1",
        null);

    lang.newText(new Coordinates(300, 50), "Amount of smaller elements", "t1",
        null);
    lang.newText(new Coordinates(500, 58), "Amount of SWAP operations", "t2",
        null);

    IntArray array = lang.newIntArray(new Coordinates(20, 100),
        (int[]) primitives.get("IntArray"), "IntArray", null,
        (ArrayProperties) props.getPropertiesByName("IntArray"));

    ArrayMarker marker_1 = lang.newArrayMarker(array, 0, "startIndex", null,
        (ArrayMarkerProperties) props.getPropertiesByName("startIndex"));

    ArrayMarker marker_2 = lang.newArrayMarker(array, 0, "smaller", null,
        (ArrayMarkerProperties) props.getPropertiesByName("smaller"));

    try {
      boolean doubled = false;
      for (int i = 0; i < array.getLength(); i++)
        for (int j = i + 1; j < array.getLength(); j++) {
          if (array.getData(i) == array.getData(j)) {
            doubled = true;
            break;
          }
        }

      if (!doubled)
        sort(array, sourceCode, marker_1, marker_2);
      else
        throw new IllegalArgumentException(
            "All elements in array must be unique");
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }

    return lang.toString();
  }

  private void sort(IntArray ia, SourceCode sc, ArrayMarker marker_1,
      ArrayMarker marker_2) {

    Text amountOfSwap = lang.newText(new Coordinates(570, 75), "0", "t4", null);
    Text amountOfEl = lang.newText(new Coordinates(370, 78), "0", "t3", null);
    Integer swapcount = 0;

    marker_1.hide();
    marker_2.hide();

    lang.nextStep();
    sc.highlight(0);

    lang.nextStep();
    sc.toggleHighlight(0, 1);
    marker_1.show();

    lang.nextStep();
    sc.toggleHighlight(1, 2);

    int startWert = 0;
    ia.highlightCell(startWert, new TicksTiming(10), null);
    while (startWert < ia.getLength()- 1) {
      lang.nextStep();
      sc.toggleHighlight(2, 3);

      int smaller = countSmallerOnes(startWert, ia, sc, marker_1, marker_2,
          amountOfEl);

      lang.nextStep();
      sc.toggleHighlight(3, 4);
      if (smaller > 0) {
        lang.nextStep();
        sc.toggleHighlight(4, 5);

        lang.nextStep();
        ia.swap(startWert, startWert + smaller, new TicksTiming(10), null);
        swapcount++;
        amountOfSwap.setText(swapcount.toString(), new TicksTiming(10), null);

        ia.highlightElem(startWert + smaller, new TicksTiming(10), null);
        ia.unhighlightCell(startWert + smaller, null, new TicksTiming(10));
        ia.highlightCell(startWert, null, new TicksTiming(10));
        lang.nextStep();
        sc.toggleHighlight(5, 6);

        lang.nextStep();
        sc.toggleHighlight(6, 10);
      } else {
        lang.nextStep();
        sc.toggleHighlight(4, 7);

        lang.nextStep();
        sc.toggleHighlight(7, 8);
        marker_1.move(startWert + 1, new TicksTiming(10), null);
        lang.nextStep();
        ia.unhighlightCell(startWert, null, new TicksTiming(10));
        ia.highlightCell(startWert + 1, null, new TicksTiming(10));
        lang.nextStep();
        sc.toggleHighlight(8, 9);

        startWert++;

        lang.nextStep();
        sc.toggleHighlight(9, 10);
      }
      lang.nextStep();
      sc.toggleHighlight(10, 2);
    }
    lang.nextStep();
    sc.toggleHighlight(2, 10);

    lang.nextStep();
    sc.toggleHighlight(10, 11);

    lang.nextStep();
    sc.unhighlight(11);
  }

  private int countSmallerOnes(int index, IntArray ia, SourceCode sc,
      ArrayMarker marker_1, ArrayMarker marker_2, Text t) {
    Integer counter = 0;
    t.setText("0", null, new TicksTiming(10));

    lang.nextStep();
    sc.highlight(13);

    lang.nextStep();
    sc.toggleHighlight(13, 14);

    lang.nextStep();
    sc.toggleHighlight(14, 15);

    lang.nextStep();

    for (int i = index + 1; i < ia.getLength(); i++) {
      lang.nextStep();
      marker_2.move(i, new TicksTiming(10), null);
      marker_2.show();

      lang.nextStep();
      sc.toggleHighlight(15, 16);
      ia.highlightCell(i, null, new TicksTiming(10));
      lang.nextStep();

      if (ia.getData(index) > ia.getData(i)) {
        counter++;
        t.setText(counter.toString(), null, new TicksTiming(10));
        sc.toggleHighlight(16, 17);
        lang.nextStep();
        sc.toggleHighlight(17, 18);
      } else
        sc.toggleHighlight(16, 18);

      lang.nextStep();
      sc.toggleHighlight(18, 19);
      ia.unhighlightCell(i, null, new TicksTiming(10));
      lang.nextStep();
      sc.toggleHighlight(19, 15);
    }
    lang.nextStep();
    sc.toggleHighlight(15, 20);

    lang.nextStep();
    sc.toggleHighlight(20, 21);

    lang.nextStep();
    sc.unhighlight(21);
    return counter;
  }

  private void addLinesToSourceCode(SourceCode sc) {
    sc.addCodeLine("public void swapSort(int[] array) {", null, 0, null);
    sc.addCodeLine("int startIndex = 0;", null, 1, null);
    sc.addCodeLine("while(startIndex < array.length - 1) {", null, 1, null);
    sc.addCodeLine("int smaller = countSmallerOnes(array , startIndex);", null,
        2, null);
    sc.addCodeLine("if(smaller > 0) {", null, 2, null);
    sc.addCodeLine("swap(startIndex , startIndex + smaller);", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("else {", null, 2, null);
    sc.addCodeLine("startIndex++;", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("}", null, 0, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine(
        "private int countSmallerOnes(final int[] array, final int index) {",
        null, 0, null);
    sc.addCodeLine("int counter = 0;", null, 1, null);
    sc.addCodeLine("for (int i = index + 1; i < array.length; i++) {", null, 1,
        null);
    sc.addCodeLine("if(array[index] > array[i]){", null, 2, null);
    sc.addCodeLine("counter++;", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("return counter;", null, 1, null);
    sc.addCodeLine("}", null, 0, null);
  }

  @Override
  public String getAlgorithmName() {
    return "Swap Sort";
  }

  @Override
  public String getCodeExample() {
    return sourceCode;
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return " Die Idee von Swap-Sort ist, von jedem Element eines Arrays A(1..n) die Anzahl m der kleineren Werte (die in A sind) zu zählen und das Element dann mit dem Element in A(m+1) zu vertauschen. Somit ist sichergestellt, dass das ausgetauschte Element bereits an der richtigen, also endgültigen Stelle steht.Nachteil dieses Algorithmus ist, dass jedes Element nur einmal vorkommen darf, da sonst keine Terminierung erfolgt. ";
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
    return "SwapSort3";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  public String getAnimationAuthor() {
    return "Enkh-Amgalan Ganbaatar, Martin Tjokrodiredjo";
  }

  public void init() {
    lang = new AnimalScript(getAlgorithmName(), getAnimationAuthor(), 720, 480);
    lang.setStepMode(true);
  }

  public Language getLang() {
    return lang;
  }
}
