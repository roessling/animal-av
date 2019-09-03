
/*
 * cocktailSort.java
 * Sascha Dutschka, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

package generators.sorting;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import animal.variables.VariableRoles;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

public class CocktailSortDutschka implements Generator {
  private Language             lang;
  private int[]                intArray;
  private SourceCodeProperties sourceCodeProp;
  private ArrayProperties      arrayProp;

  public void init() {
    lang = new AnimalScript("Cocktail Sort [DE]", "Sascha Dutschka", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    intArray = (int[]) primitives.get("intArray");
    sourceCodeProp = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProp");
    arrayProp = (ArrayProperties) props.getPropertiesByName("arrayProp");

    sort(intArray, sourceCodeProp, arrayProp);
    System.out.println("TESasdfasdfasdfdasT");
    return lang.toString();
  }

  public String getName() {
    return "Cocktail Sort";
  }

  public String getAlgorithmName() {
    return "CocktailSort";
  }

  public String getAnimationAuthor() {
    return "Sascha Dutschka";
  }

  public String getDescription() {
    return "<h1>Cocktail Sort</h1><br><br>"
        + "CocktailSort, oder auch bidirectional bubble sort / shaker sort, betrachtet Schrittweise "
        + "das naechste Element des Arrays und vergleicht es mit dem Aktuellen. Wenn das aktuelle Element "
        + "gr��er ist, wird es mit dem folgenden vertauscht. Somit wandert das gr��te Element bis ganz ans Ende der Liste.<br><br>"
        + "Dort angekommen, wird die Richtung gewechselt, und nun das kleinste Element bis an den Anfang transportiert.<br>"
        + "Danach erfolgt wieder ein Richtungswechsel.<br><br>"
        + "Der Algorithmus endet, wenn in einem der Durchg�nge kein swap, also der Tausch zweier Elemente, stattgefunden hat.<br><br>"
        + "Die Anzahl der Vergleiche ist im ersten Durchgang n-1 und nimmt mit jedem weiteren Durchgang um eins ab.";
  }

  public String getCodeExample() {
    return "public void cocktailSort(int[] array)" + "\n" + "{" + "\n"
        + "  Boolean swapped;" + "\n" + "  int begin = -1;" + "\n"
        + "  int end = array.length - 2;" + "\n" + "  int i;" + "\n"
        + "  while (true)" + "\n" + "  {" + "\n" + "    swapped = false;" + "\n"
        + "    begin++;" + "\n" + "    for (i = begin; i <= end; i++)" + "\n"
        + "    {" + "\n" + "      if (array[i] > array[i + 1])" + "\n"
        + "      {" + "\n" + "        swap(array,i,i+1);" + "\n"
        + "        swapped = true;" + "\n" + "      }" + "\n" + "    }" + "\n"
        + "    if (swapped == false)" + "\n" + "    {" + "\n" + "      break;"
        + "\n" + "    }" + "\n" + "    swapped = false;" + "\n" + "    end--;"
        + "\n" + "    for (i = end; i >= begin; i--)" + "\n" + "    {" + "\n"
        + "      if (array[i] > array[i + 1])" + "\n" + "      {" + "\n"
        + "        swap(array,i,i+1);" + "\n" + "        swapped = true;" + "\n"
        + "      }" + "\n" + "    }" + "\n" + "    if (swapped == false)" + "\n"
        + "    {" + "\n" + "      break;" + "\n" + "    }" + "\n" + "  }" + "\n"
        + "}";
  }

  /**
   * Sort the int array passed in
   * 
   * @param a
   *          the array to be sorted
   */
  public void sort(int[] a, SourceCodeProperties sourceCodeProp,
      ArrayProperties arrayProp) {

    // show the header with a heading surrounded by a rectangle
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(Font.SANS_SERIF, Font.BOLD, 24));
    lang.newText(new Coordinates(20, 22), "CocktailSort",
        "header", null, headerProps);
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
        new Color(200, 200, 200));
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(
        new Offset(-8, -3, "header", AnimalScript.DIRECTION_NW),
        new Offset(8, 3, "header", "SE"), "hRect", null, rectProps);

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(Font.SANS_SERIF, Font.PLAIN, 16));
    Text descriptionText1 = lang.newText(new Coordinates(10, 50),
        "CocktailSort, oder auch bidirectional bubble sort / shaker sort, betrachtet Schrittweise das n�chste",
        "description1", null, textProps);
    Text descriptionText2 = lang.newText(
        new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW),
        "Element des Arrays und vergleicht es mit dem Aktuellen. Wenn das aktuelle Element gr��er ist,",
        "description2", null, textProps);
    Text descriptionText3 = lang.newText(
        new Offset(0, 25, "description2", AnimalScript.DIRECTION_NW),
        "wird es mit dem folgenden vertauscht. Somit wandert das gr��te Element bis ganz ans Ende der Liste.",
        "description3", null, textProps);
    Text descriptionText4 = lang.newText(
        new Offset(0, 40, "description3", AnimalScript.DIRECTION_NW),
        "Dort angekommen, wird die Richtung gewechselt, und nun das kleinste Element bis an den Anfang transportiert.",
        "description4", null, textProps);
    Text descriptionText5 = lang.newText(
        new Offset(0, 25, "description4", AnimalScript.DIRECTION_NW),
        "Danach erfolgt ein erneuter ein Richtungswechsel.", "description5",
        null, textProps);
    Text descriptionText6 = lang.newText(
        new Offset(0, 40, "description5", AnimalScript.DIRECTION_NW),
        "Der Algorithmus endet, wenn in einem der Durchg�nge kein swap, also der Tausch zweier Elemente,",
        "description6", null, textProps);
    Text descriptionText7 = lang.newText(
        new Offset(0, 25, "description6", AnimalScript.DIRECTION_NW),
        "stattgefunden hat.", "description7", null, textProps);
    Text descriptionText8 = lang.newText(
        new Offset(0, 40, "description7", AnimalScript.DIRECTION_NW),
        "Die Anzahl der Vergleiche ist im ersten Durchgang n-1 und nimmt mit jedem weiteren Durchgang um eins ab.",
        "description8", null, textProps);

    lang.nextStep();
    descriptionText1.hide();
    descriptionText2.hide();
    descriptionText3.hide();
    descriptionText4.hide();
    descriptionText5.hide();
    descriptionText6.hide();
    descriptionText7.hide();
    descriptionText8.hide();

    // Create Array: coordinates, data, name, display options,
    // default properties

    // first, set the visual properties (somewhat similar to CSS)

    // now, create the IntArray object, linked to the properties
    IntArray ia = lang.newIntArray(new Coordinates(20, 110), a, "intArray",
        null, arrayProp);

    // start a new step after the array was created
    lang.nextStep();

    // Create SourceCode: coordinates, name, display options,
    // default properties

    // first, set the visual properties for the source code

    // now, create the source code entity
    SourceCode sc = lang.newSourceCode(new Coordinates(40, 140), "sourceCode",
        null, sourceCodeProp);

    // Add the lines to the SourceCode object.
    // Line, name, indentation, display delay
    sc.addCodeLine("public void cocktailSort(int[] array){", null, 0, null); // 0
    sc.addCodeLine("Boolean swapped;", null, 1, null); // 1
    sc.addCodeLine("int begin = -1;", null, 1, null); // 2
    sc.addCodeLine("int end = array.length - 2;", null, 1, null); // 3
    sc.addCodeLine("int i;", null, 1, null); // 4
    sc.addCodeLine("while (true){", null, 1, null); // 5
    sc.addCodeLine("swapped = false;", null, 2, null); // 6
    sc.addCodeLine("begin++;", null, 2, null); // 7
    sc.addCodeLine("for (i = begin; i <= end; i++){", null, 2, null); // 8
    sc.addCodeLine("if (array[i] > array[i + 1]){", null, 3, null); // 9
    sc.addCodeLine("swap(array,i,i+1);", null, 4, null); // 10
    sc.addCodeLine("swapped = true;", null, 4, null); // 11
    sc.addCodeLine("}", null, 3, null); // 12
    sc.addCodeLine("}", null, 2, null); // 13
    sc.addCodeLine("if (swapped == false){", null, 2, null); // 14
    sc.addCodeLine("break;", null, 3, null); // 15
    sc.addCodeLine("}", null, 2, null); // 16
    sc.addCodeLine("swapped = false;", null, 2, null); // 17
    sc.addCodeLine("end--;", null, 2, null); // 18
    sc.addCodeLine("for (i = end; i >= begin; i--){", null, 2, null); // 19
    sc.addCodeLine("if (array[i] > array[i + 1]){", null, 3, null); // 20
    sc.addCodeLine("swap(array,i,i+1);", null, 4, null); // 21
    sc.addCodeLine("swapped = true;", null, 4, null); // 22
    sc.addCodeLine("}", null, 3, null); // 23
    sc.addCodeLine("}", null, 2, null); // 24
    sc.addCodeLine("if (swapped == false){", null, 2, null); // 25
    sc.addCodeLine("break;", null, 3, null); // 26
    sc.addCodeLine("}", null, 2, null); // 27
    sc.addCodeLine("}", null, 1, null); // 28
    sc.addCodeLine("}", null, 0, null); // 29

    // lang.nextStep();

    try {
      // Start cocktailSort
      cocktailSortMaker(ia, sc);
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
    sc.hide();
    ia.hide();
    lang.nextStep();
  }

  /**
   * Cocktail-Sort:
   * 
   * @param array
   *          the IntArray to be sorted
   * @param codeSupport
   *          the underlying code instance
   */
  private void cocktailSortMaker(final IntArray array,
      final SourceCode codeSupport) throws LineNotExistsException {

    // Highlight first line
    // Line, Column, use context colour?, display options, duration
    codeSupport.highlight(0, 0, false);
    lang.nextStep();

    // Array, current index, name, display options, properties
    ArrayMarkerProperties arrayIMProps = new ArrayMarkerProperties();
    arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    ArrayMarker iMarker = lang.newArrayMarker(array, 0, "i", null,
        arrayIMProps);

    int comparisons = 0;
    int swaps = 0;
    Boolean swapped = false;
    int begin = -1;
    int end = array.getLength() - 2;
    int i = 0;

    // Variable window:
    Variables varWindow = lang.newVariables();
    varWindow.declare("int", "i", "" + i,
        animal.variables.Variable.getRoleString(VariableRoles.STEPPER));
    varWindow.declare("int", "begin", "" + begin,
        animal.variables.Variable.getRoleString(VariableRoles.FOLLOWER));
    varWindow.declare("int", "end", "" + end,
        animal.variables.Variable.getRoleString(VariableRoles.FOLLOWER));
    varWindow.declare("String", "swapped", "" + swapped,
        animal.variables.Variable.getRoleString(VariableRoles.GATHERER));
    varWindow.declare("int", "comparisons", "" + begin,
        animal.variables.Variable.getRoleString(VariableRoles.FOLLOWER));
    varWindow.declare("int", "swaps", "" + swaps,
        animal.variables.Variable.getRoleString(VariableRoles.FOLLOWER));

    Text comparisonText = lang.newText(
        new Offset(25, -40, "intArray", AnimalScript.DIRECTION_E),
        "Vergleiche: " + comparisons, "compText", null);
    Text swapsText = lang.newText(
        new Offset(0, 9, "compText", AnimalScript.DIRECTION_W),
        "Vertausche: " + swaps, "swapsText", null);
    Text swappedText = lang.newText(
        new Offset(0, 9, "swapsText", AnimalScript.DIRECTION_W),
        "swapped = " + swapped, "swappedText", null);
    Text beginText = lang.newText(
        new Offset(0, 9, "swappedText", AnimalScript.DIRECTION_W),
        "begin = " + begin, "beginText", null);
    Text endText = lang.newText(
        new Offset(0, 9, "beginText", AnimalScript.DIRECTION_W), "end = " + end,
        "endText", null);
    Text breakText = lang.newText(
        new Offset(100, 160, "header", AnimalScript.DIRECTION_SE),
        "Der Algorithmus endet hier, da kein Wert vertauscht wurde.",
        "breakText", null);
    breakText.changeColor(null, new Color(255, 0, 0), null, null);
    breakText.hide();

    codeSupport.unhighlight(0);
    codeSupport.highlight(1);
    codeSupport.highlight(2);
    codeSupport.highlight(3);
    codeSupport.highlight(4);
    lang.nextStep();
    codeSupport.unhighlight(1);
    codeSupport.unhighlight(2);
    codeSupport.unhighlight(3);
    codeSupport.unhighlight(4);
    codeSupport.highlight(5);
    lang.nextStep();
    codeSupport.unhighlight(5);
    while (true) {
      codeSupport.highlight(6);
      swappedText.setText("swapped = " + (swapped = false), null, null);
      varWindow.set("swapped", "" + swapped);
      codeSupport.highlight(7);
      beginText.setText("begin = " + ++begin, null, null);
      varWindow.set("begin", "" + begin);
      array.highlightCell(begin, null, null);
      lang.nextStep();
      codeSupport.unhighlight(6);
      codeSupport.unhighlight(7);
      codeSupport.highlight(8);
      for (i = begin; i <= end; i++) {
        lang.nextStep();
        varWindow.set("i", Integer.toString(i));
        array.unhighlightElem(i - 1, null, null);
        array.highlightElem(i, null, null);
        array.unhighlightCell(i == begin ? i : i - 1, i, null, null);
        array.highlightCell(i, i + 1, null, null);
        iMarker.move(i, null, null);
        lang.nextStep();
        codeSupport.unhighlight(8);
        codeSupport.highlight(9);
        comparisonText.setText("Vergleiche: " + ++comparisons, null, null);
        varWindow.set("comparisons", "" + comparisons);
        lang.nextStep();
        if (array.getData(i) > array.getData(i + 1)) {
          codeSupport.unhighlight(9);
          codeSupport.highlight(10);
          codeSupport.highlight(11);
          array.swap(i, i + 1, null, null);
          swapsText.setText("Vertausche: " + ++swaps, null, null);
          varWindow.set("swaps", "" + swaps);
          swappedText.setText("swapped = " + (swapped = true), null, null);
          varWindow.set("swapped", "" + swapped);
          lang.nextStep();
          codeSupport.unhighlight(10);
          codeSupport.unhighlight(11);
        }
        codeSupport.unhighlight(9);
        codeSupport.highlight(8);
      }
      codeSupport.unhighlight(8);
      codeSupport.highlight(14);
      lang.nextStep();
      if (swapped == false) {
        codeSupport.unhighlight(14);
        codeSupport.highlight(15);
        breakText.show();
        lang.nextStep();
        breakText.hide();
        array.highlightCell(0, array.getLength() - 1, null, null);
        codeSupport.unhighlight(15);
        array.unhighlightElem(0, array.getLength() - 1, null, null);
        iMarker.hide();
        break;
      }
      codeSupport.unhighlight(14);
      codeSupport.highlight(17);
      codeSupport.highlight(18);
      swappedText.setText("swapped = " + (swapped = false), null, null);
      varWindow.set("swapped", "" + swapped);
      array.highlightCell(end, null, null);
      endText.setText("end = " + --end, null, null);
      varWindow.set("end", "" + end);
      lang.nextStep();
      codeSupport.unhighlight(17);
      codeSupport.unhighlight(18);
      codeSupport.highlight(19);
      for (i = end; i >= begin; i--) {
        varWindow.set("i", Integer.toString(i));
        array.unhighlightElem(i + 2, null, null);
        array.highlightElem(i + 1, null, null);
        array.unhighlightCell(i + 1, i == end ? i + 1 : i + 2, null, null);
        array.highlightCell(i, i + 1, null, null);
        iMarker.move(i, null, null);
        lang.nextStep();
        codeSupport.unhighlight(19);
        codeSupport.highlight(20);
        comparisonText.setText("Vergleiche: " + ++comparisons, null, null);
        varWindow.set("comparisons", "" + comparisons);
        lang.nextStep();
        if (array.getData(i) > array.getData(i + 1)) {
          codeSupport.unhighlight(20);
          codeSupport.highlight(21);
          codeSupport.highlight(22);
          array.swap(i, i + 1, null, null);
          swapsText.setText("Vertausche: " + ++swaps, null, null);
          varWindow.set("swaps", "" + swaps);
          swappedText.setText("swapped = " + (swapped = true), null, null);
          varWindow.set("swapped", "" + swapped);
          lang.nextStep();
          codeSupport.unhighlight(21);
          codeSupport.unhighlight(22);
        }
        codeSupport.unhighlight(20);
        codeSupport.highlight(19);
      }
      codeSupport.unhighlight(19);
      codeSupport.highlight(25);
      lang.nextStep();
      if (swapped == false) {
        codeSupport.unhighlight(25);
        codeSupport.highlight(26);
        breakText.show();
        lang.nextStep();
        breakText.hide();
        array.highlightCell(0, array.getLength() - 1, null, null);
        array.unhighlightElem(0, array.getLength() - 1, null, null);
        codeSupport.unhighlight(26);
        iMarker.hide();
        break;
      }
      codeSupport.unhighlight(25);
      codeSupport.highlight(5);
      lang.nextStep();
      codeSupport.unhighlight(5);
    }
    lang.nextStep();
    comparisonText.hide();
    swapsText.hide();
    swappedText.hide();
    beginText.hide();
    endText.hide();

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font(Font.SANS_SERIF, Font.PLAIN, 16));
    lang.newText(new Coordinates(10, 70),
        "Der Cocktailsort hat eine Komplexitaet von n hoch 2", "finalText1",
        null, textProps);
    lang.newText(
        new Offset(0, 25, "finalText1", AnimalScript.DIRECTION_NW),
        "und hat damit kein optimales Laufzeitverhalten.", "finalText2", null,
        textProps);
    lang.newText(
        new Offset(0, 40, "finalText2", AnimalScript.DIRECTION_NW),
        "Bei vorsortierten oder bereits gut sortierten Arrays, verbessert ",
        "finalText3", null, textProps);
    lang.newText(
        new Offset(0, 25, "finalText3", AnimalScript.DIRECTION_NW),
        "sich das Laufzeitverhalten deutlich. ", "finalText4", null, textProps);
    lang.newText(
        new Offset(0, 40, "finalText4", AnimalScript.DIRECTION_NW),
        "Da es sich um ein in-place Verfahren handelt, wird kaum zusaetzlicher Speicherplatz ",
        "finalText5", null, textProps);
    lang.newText(
        new Offset(0, 25, "finalText5", AnimalScript.DIRECTION_NW),
        "fuer den eigentlichen Sortiervorgang benoetigt. ", "finalText6", null,
        textProps);

  }

  public String getFileExtension() {
    return "asu";
  }

  public Locale getContentLocale() {
    return Locale.GERMAN;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}