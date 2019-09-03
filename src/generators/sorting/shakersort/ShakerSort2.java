package generators.sorting.shakersort;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class ShakerSort2 implements generators.framework.Generator {

  private ArrayMarker         downMarker;

  private ArrayMarker         upMarker;

  private ArrayMarker         leftMarker;

  private ArrayMarker         rightMarker;

  private SourceCode          source;

  private SourceCode          intro;

  private Text                swappedText;

  private static final String DESCRIPTION = "Shakersort is a variation of bubble "
                                              + "sort that is both a stable sorting algorithm and a comparison sort. The algorithm differs "
                                              + "from bubble sort in that sorts in both directions each pass through the list. This "
                                              + "sorting algorithm is only marginally more difficult than bubble sort to implement, and "
                                              + "solves the problem with so-called turtles in bubble sort. (taken from wikipedia) ";

  private static final String CODE        = "static void shakerSort(int[] intArray) {"// 0
                                              + "\n boolean swapped;"// 1
                                              + "\n int left = 0; "// 2
                                              + "\n int right = intArray.length - 1;"// 3
                                              + "\n int complete = right;"// 4
                                              + "\n do {"// 5
                                              + "\n 	swapped = false;"// 6
                                              + "\n 	for (int down = right; down > left; down--)"// 7
                                              + "\n 		if (intArray[down] < inAtrray[down - 1]) {"// 8
                                              + "\n 			swap(intArray, down, down-1);"// 9
                                              + "\n 			swapped = true;"// 10
                                              + "\n 			complete = down;"// 11
                                              + "\n 		}"// 12
                                              + "\n 	left = complete;"// 13
                                              + "\n 	for (int up = left; up < right; up++)"// 14
                                              + "\n 		if (intArray[up + 1] < intArray[up]) {"// 15
                                              + "\n 			swap(intArray, up, up+1);"// 16
                                              + "\n 			swapped = true;"// 17
                                              + "\n 			complete = up;"// 18
                                              + "\n 		}"// 19
                                              + "\n 	right = complete;"// 20
                                              + "\n } while (swapped);"// 21
                                              + "\n }";                                                                              // 22

  public void sort(IntArray ia, Language lang) {

    // Intro Page

    ia.hide();
    swappedText.hide();

    intro
        .addCodeLine(
            "Shaker Sort, also known as 'Cocktail Sort' or 'Bidirectional Bubble Sort', is an ",
            null, 0, null);
    intro.addCodeLine("improved version of Bubble Sort.", null, 0, null);
    intro.addCodeLine("", null, 0, null);
    intro
        .addCodeLine(
            "In contrast to Bubble Sort, Shaker Sort doesn't pass through the list from the bottom",
            null, 0, null);
    intro
        .addCodeLine(
            "to the top in every single iteration. Instead it passes alternately through the list",
            null, 0, null);
    intro
        .addCodeLine(
            "from the bottom to the top and from the top to the bottom. Shaker Sort has the same ",
            null, 0, null);
    intro
        .addCodeLine(
            "complexity as Bubble sort - O(n*n) in Landau notation, but it can be slightly faster. ",
            null, 0, null);
    intro
        .addCodeLine(
            "That's because bigger elements can be separated from smaller elements very fast. ",
            null, 0, null);
    intro
        .addCodeLine(
            "While Bubble Sort can only move big elements to the top of the list, Shaker Sort ",
            null, 0, null);
    intro.addCodeLine("will also move small elements to the bottom.", null, 0,
        null);
    intro.addCodeLine("", null, 0, null);
    intro
        .addCodeLine(
            "When passing trough the list, Shaker Sort always compares the current element to ",
            null, 0, null);
    intro
        .addCodeLine(
            "the following element. In case those elements are out of order, both elements will be ",
            null, 0, null);
    intro
        .addCodeLine(
            "exchanged. Hence Shaker Sort works 'in-place'. Elements at the bottom/top of the ",
            null, 0, null);
    intro
        .addCodeLine(
            "list, which are already at their final position, will be skipped in the next pass. Once ",
            null, 0, null);
    intro
        .addCodeLine(
            "Shaker Sort finishes a pass without exchanging any elements, it terminates.",
            null, 0, null);

    lang.nextStep();

    ia.show();
    intro.hide();
    rightMarker.hide();
    leftMarker.hide();
    downMarker.hide();
    upMarker.hide();

    // Create SourceCode

    source.addCodeLine("static void shakerSort(int[] intArray) {", null, 0,
        null); // 0
    source.addCodeLine("boolean swapped;", null, 1, null); // 1
    source.addCodeLine("int left = 0;", null, 1, null); // 2
    source.addCodeLine("int right = intArray.length - 1;", null, 1, null); // 3
    source.addCodeLine("int complete = right;", null, 1, null); // 4
    source.addCodeLine("do {", null, 1, null);// 5
    source.addCodeLine("swapped = false;", null, 2, null);// 6
    source.addCodeLine("for (int down = right; down > left; down--)", null, 2,
        null);// 7
    source.addCodeLine("if (intArray[down] < intArray[down - 1]) {", null, 3,
        null);// 8
    source.addCodeLine("swap(intArray, down, down-1);", null, 4, null);// 9
    source.addCodeLine("swapped = true;", null, 4, null);// 10
    source.addCodeLine("complete = down;", null, 4, null);// 11
    source.addCodeLine("}", null, 3, null);// 12
    source.addCodeLine("left = complete;", null, 2, null);// 13
    source.addCodeLine("for (int up = left; up < right; up++)", null, 2, null);// 14
    source.addCodeLine("if(intArray[up + 1] < intArray[up]) {", null, 3, null);// 15
    source.addCodeLine("swap(inArray,up, up+1);", null, 4, null);// 16
    source.addCodeLine("swapped = true;", null, 4, null);// 17
    source.addCodeLine("complete = up;", null, 4, null);// 18
    source.addCodeLine("}", null, 3, null);// 19
    source.addCodeLine("right = complete;", null, 2, null);// 20
    source.addCodeLine("} while (swapped);", null, 1, null);// 21
    source.addCodeLine("}", null, 0, null);// 22

    lang.nextStep();

    try {
      shakerSort(ia, source, 0, (ia.getLength() - 1), lang);
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
  }

  private void shakerSort(IntArray ia, SourceCode codeSupport, int i, int j,
      Language lang) {// 0

    // -----------------------------------------------------
    codeSupport.highlight(0);
    lang.nextStep();
    codeSupport.unhighlight(0);
    // -----------------------------------------------------

    // -----------------------------------------------------
    codeSupport.highlight(1);
    boolean swapped;// -------1---------
    swappedText.show();
    swappedText.changeColor(null, Color.RED, null, null);

    lang.nextStep();
    swappedText.changeColor(null, Color.BLACK, null, null);
    codeSupport.unhighlight(1);
    // -----------------------------------------------------

    // -----------------------------------------------------
    codeSupport.highlight(2);

    leftMarker.move(0, null, null); // anfangen beim ersten
                                    // Element--------2---------
    leftMarker.show();

    lang.nextStep();
    codeSupport.unhighlight(2);
    // -----------------------------------------------------

    // -----------------------------------------------------
    codeSupport.highlight(3);
    rightMarker.move(ia.getLength() - 1, null, null);// ----------3---------------
    rightMarker.show();

    lang.nextStep();
    codeSupport.unhighlight(3);
    // -----------------------------------------------------

    // -----------------------------------------------------
    codeSupport.highlight(4);
    int complete = rightMarker.getPosition();// ----------4----------------
    ia.highlightCell(complete, null, null);

    lang.nextStep();
    codeSupport.unhighlight(4);
    // -----------------------------------------------------

    // -----------------------------------------------------
    do {// -------------5-------------------
      codeSupport.highlight(5);

      lang.nextStep();
      codeSupport.unhighlight(5);
      // -----------------------------------------------------

      // -----------------------------------------------------
      swappedText.changeColor(null, Color.RED, null, null);
      swapped = false;// ---------6----------------
      swappedText.setText("swapped: " + String.valueOf(swapped), null, null);
      codeSupport.highlight(6);
      lang.nextStep();
      swappedText.changeColor(null, Color.BLACK, null, null);
      codeSupport.unhighlight(6);
      // -----------------------------------------------------

      downMarker.show();

      // -----------------------------------------------------
      for (downMarker.move(rightMarker.getPosition(), null, null); downMarker
          .getPosition() > leftMarker.getPosition(); downMarker.decrement(null,
          null)) {
        // abwärts//-----7-----------

        codeSupport.highlight(7);
        lang.nextStep();
        codeSupport.unhighlight(7);
        // -----------------------------------------------------

        // -----------------------------------------------------
        codeSupport.highlight(8);
        ia.highlightElem(downMarker.getPosition() - 1,
            downMarker.getPosition(), null, null);
        lang.nextStep();
        codeSupport.unhighlight(8);

        if (ia.getData(downMarker.getPosition()) < ia.getData(downMarker
            .getPosition() - 1)) {// ------8--------
          // -----------------------------------------------------

          // -----------------------------------------------------
          codeSupport.highlight(9);
          ia.unhighlightCell(0, ia.getLength() - 1, null, null);

          CheckpointUtils.checkpointEvent(this, "swapDownEvent", new Variable(
              "leftIndex", downMarker.getPosition() - 1), new Variable(
              "rightIndex", downMarker.getPosition()), new Variable("leftVal",
              ia.getData(downMarker.getPosition() - 1)), new Variable(
              "rightVal", ia.getData(downMarker.getPosition())), new Variable(
              "animstep", lang.getStep()));

          ia.swap(downMarker.getPosition(), downMarker.getPosition() - 1, null,
              null);// ----------9---------

          ia.highlightCell(complete, null, null);

          lang.nextStep();
          codeSupport.unhighlight(9);
          // -----------------------------------------------------

          // -----------------------------------------------------
          codeSupport.highlight(10);
          swapped = true; // ----------10---------------
          swappedText
              .setText("swapped: " + String.valueOf(swapped), null, null);
          swappedText.changeColor(null, Color.RED, null, null);

          lang.nextStep();
          swappedText.changeColor(null, Color.BLACK, null, null);
          codeSupport.unhighlight(10);
          // -----------------------------------------------------

          // -----------------------------------------------------
          codeSupport.highlight(11);
          ia.unhighlightCell(complete, null, null);
          complete = downMarker.getPosition(); // -------------11--------------
          ia.highlightCell(complete, null, null);

          lang.nextStep();
          codeSupport.unhighlight(11);
          // -----------------------------------------------------

          // -----------------------------------------------------
        }// --------------12-----------------
        ia.unhighlightElem(downMarker.getPosition() - 1,
            downMarker.getPosition(), null, null);
        codeSupport.highlight(12);
        lang.nextStep();
        codeSupport.unhighlight(12);
        // -----------------------------------------------------

        // -----------------------------------------------------
      }
      downMarker.hide();

      CheckpointUtils.checkpointEvent(this, "ShakeDownFin", new Variable(
          "lCurrentIndex", leftMarker.getPosition()), new Variable("lNewIndex",
          complete));

      leftMarker.move(complete, null, null);// --------------13----------------
      codeSupport.highlight(13);

      lang.nextStep();
      codeSupport.unhighlight(13);
      // -----------------------------------------------------

      upMarker.show();

      // -----------------------------------------------------
      for (upMarker.move(leftMarker.getPosition(), null, null); upMarker
          .getPosition() < rightMarker.getPosition(); upMarker.increment(null,
          null)) {
        // aufwärts----14------

        codeSupport.highlight(14);
        lang.nextStep();
        codeSupport.unhighlight(14);
        // -----------------------------------------------------

        // -----------------------------------------------------
        codeSupport.highlight(15);
        lang.nextStep();
        ia.highlightElem(upMarker.getPosition(), upMarker.getPosition() + 1,
            null, null);
        lang.nextStep();
        codeSupport.unhighlight(15);
        if (ia.getData(upMarker.getPosition() + 1) < ia.getData(upMarker
            .getPosition())) {// ----15------
          // -----------------------------------------------------

          CheckpointUtils.checkpointEvent(this, "swapUpEvent", new Variable(
              "leftIndex", upMarker.getPosition() - 1), new Variable(
              "rightIndex", upMarker.getPosition()),
              new Variable("leftVal", ia.getData(upMarker.getPosition() - 1)),
              new Variable("rightVal", ia.getData(upMarker.getPosition())),
              new Variable("animstep", lang.getStep()));

          // -----------------------------------------------------
          codeSupport.highlight(16);
          ia.unhighlightCell(0, ia.getLength() - 1, null, null);
          ia.swap(upMarker.getPosition(), upMarker.getPosition() + 1, null,
              null);// ------16-----
          ia.highlightCell(complete, null, null);
          lang.nextStep();
          codeSupport.unhighlight(16);
          // -----------------------------------------------------

          // -----------------------------------------------------
          codeSupport.highlight(17);
          swapped = true;// -----------17------------
          swappedText
              .setText("swapped: " + String.valueOf(swapped), null, null);
          swappedText.changeColor(null, Color.RED, null, null);

          lang.nextStep();
          swappedText.changeColor(null, Color.BLACK, null, null);
          codeSupport.unhighlight(17);
          // -----------------------------------------------------

          // -----------------------------------------------------
          codeSupport.highlight(18);
          ia.unhighlightCell(complete, null, null);
          complete = upMarker.getPosition(); // ----------18--------
          ia.highlightCell(complete, null, null);

          lang.nextStep();
          codeSupport.unhighlight(18);
          // -----------------------------------------------------

          // -----------------------------------------------------
        }// ---------19--------------
        codeSupport.highlight(19);
        ia.unhighlightElem(upMarker.getPosition(), upMarker.getPosition() + 1,
            null, null);
        lang.nextStep();
        codeSupport.unhighlight(19);
        // -----------------------------------------------------
      }
      upMarker.hide();

      // -----------------------------------------------------
      codeSupport.highlight(20);

      CheckpointUtils.checkpointEvent(this, "ShakeUpFin", new Variable(
          "rCurrentIndex", rightMarker.getPosition()), new Variable(
          "rNewIndex", complete));

      rightMarker.move(complete, null, null);// -------20--------------

      lang.nextStep();
      codeSupport.unhighlight(20);
      // -----------------------------------------------------

      // -----------------------------------------------------
      codeSupport.highlight(21);
      swappedText.changeColor(null, Color.RED, null, null);

      lang.nextStep();
      swappedText.changeColor(null, Color.BLACK, null, null);
      codeSupport.unhighlight(21);

    } while (swapped);// ----21--------------------
    // -----------------------------------------------------

    // -----------------------------------------------------
    codeSupport.highlight(22);
    lang.nextStep();
    codeSupport.unhighlight(22);
  }// ------------22------------------

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    Language lang = new AnimalScript("ShakerSort Animation", "trickSoft", 640,
        480);

    // Activate step control
    lang.setStepMode(true);

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 24));

    @SuppressWarnings(value = "unused")
    Rect textBox = lang.newRect(new Coordinates(60, 10), new Coordinates(230,
        55), "Box", null, rectProps);

    @SuppressWarnings(value = "unused")
    Text nameText = lang.newText(new Coordinates(82, 28), "ShakerSort",
        "nameLabelOnTop", null, textProps);

    int[] arrayData = (int[]) primitives.get("array");

    IntArray array = lang.newIntArray(new Coordinates(60, 130), arrayData,
        "My Array", null,
        (ArrayProperties) (ArrayProperties) props.getPropertiesByName("Array"));

    downMarker = lang.newArrayMarker(array, arrayData.length, "down", null,
        (ArrayMarkerProperties) props.getPropertiesByName("Down Arrow"));

    upMarker = lang.newArrayMarker(array, arrayData.length, "up", null,
        (ArrayMarkerProperties) props.getPropertiesByName("Up Arrow"));

    leftMarker = lang.newArrayMarker(array, arrayData.length, "l", null,
        (ArrayMarkerProperties) props.getPropertiesByName("Left Arrow"));

    rightMarker = lang.newArrayMarker(array, arrayData.length, "r", null,
        (ArrayMarkerProperties) props.getPropertiesByName("Right Arrow"));

    swappedText = lang.newText(new Coordinates(100, 170), "swapped: ",
        "swappedText", null);

    source = lang.newSourceCode(new Coordinates(60, 190), "Source Code", null,
        (SourceCodeProperties) props.getPropertiesByName("Source Code"));
    intro = lang.newSourceCode(new Coordinates(60, 90), "Intro Text", null,
        (SourceCodeProperties) props.getPropertiesByName("Intro Text"));

    sort(array, lang);
    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Shaker Sort";
  }

  @Override
  public String getCodeExample() {
    return CODE;
  }

  @Override
  public Locale getContentLocale() {
    return Locale.US; // US-English
  }

  @Override
  public String getDescription() {
    return DESCRIPTION;
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT); // Sorting
                                                                 // Algorithm
  }

  @Override
  public String getName() {
    return "Shaker Sort";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public String getAnimationAuthor() {
    return "Daniel Trick, Jonathan Römer, Florian Jung";
  }

  @Override
  public void init() {
    // nothing to be done here
  }

}
