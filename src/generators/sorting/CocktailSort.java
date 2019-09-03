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
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class CocktailSort implements Generator {
  protected Language      lang;

  private ArrayProperties arrayProps;

  public void init() {
    // initialize the main elements
    // generate a new language instance for content creation
    // parameter: animation title, author, width, height
    lang = new AnimalScript("Cocktailsort Animation", "Kristina Reiß", 640, 480);
    // activate step control
    lang.setStepMode(true);
    // create array properties with default values
    arrayProps = new ArrayProperties();
    // redefined properties: border red, filled with yellow
    /*
     * arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.RED);
     * arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
     * arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
     */
    // marker for left: black with label left
    amLeft = new ArrayMarkerProperties();
    amLeft.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    amLeft.set(AnimationPropertiesKeys.LABEL_PROPERTY, "left");
    // marker for right: blue with label right
    amRight = new ArrayMarkerProperties();
    amRight.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    amRight.set(AnimationPropertiesKeys.LABEL_PROPERTY, "right");
    // marker for finish: green with label finish
    amFinish = new ArrayMarkerProperties();
    amFinish.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
    amFinish.set(AnimationPropertiesKeys.LABEL_PROPERTY, "finish");
    // marker for i: orange with label i
    ami = new ArrayMarkerProperties();
    ami.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.ORANGE);
    ami.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    // marker for j: pink with label j
    amj = new ArrayMarkerProperties();
    amj.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.PINK);
    amj.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
  }

  // declare the marker properties
  private ArrayMarkerProperties amLeft, amRight, amFinish, ami, amj;

  public void showSourceCode() {
    // first,set the visual properties for the source code
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        12, Font.PLAIN));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    // now create the source code entity
    SourceCode sc = lang.newSourceCode(new Coordinates(40, 140), "sourceCode",
        null, scProps);
    sc.addCodeLine("public void CocktailSort(int[] inputArray){", null, 0, null);
    sc.addCodeLine("int left, right, finish, i, j;", null, 1, null);
    sc.addCodeLine("for(i = right; i>= left; i--){", null, 1, null);
    sc.addCodeLine("if(array[i].compareTo(array[i - 1]) < 0)", null, 2, null);
    sc.addCodeLine("finish = i;", null, 3, null);
    sc.addCodeLine("for(j = left; j <= right; j++)", null, 3, null);
    sc.addCodeLine("if(array[j].compareTo(array[j - 1]) < 0)", null, 4, null);
    sc.addCodeLine("finish = j;", null, 5, null);
    sc.addCodeLine("swap(inputArray, i, j); //swap", null, 3, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("}", null, 0, null);
  }

  private SourceCode sc; // to ensure it is visible outside the method...

  public void sort(int[] inputArray) {
    // wrap int[] to IntArray instance
    IntArray array = lang.newIntArray(new Coordinates(10, 30), inputArray,
        "array", null, arrayProps);
    // declare a default duration for swap effects
    // declare a default duration for swap effects
    Timing defaultTiming = new TicksTiming(15);
    showSourceCode(); // show the source code
    sc.highlight(0); // method head
    lang.nextStep();// to show array without markers
    sc.toggleHighlight(0, 1); // jump from header to int...
    boolean exchange;
    // to start at the second element
    // int left = 1;
    // int right = array.getLength() - 1;
    // int finish = right;
    ArrayMarker left = lang.newArrayMarker(array, 1, "left", null);
    lang.nextStep();// to show left marker
    ArrayMarker right = lang.newArrayMarker(array, array.getLength() - 1,
        "right", null);
    lang.nextStep(); // to show right marker
    ArrayMarker finish = lang.newArrayMarker(array, right.getPosition(),
        "finish", null);
    lang.nextStep();// to show finish marker
    ArrayMarker i = lang.newArrayMarker(array, 0, "i", null, ami);
    lang.nextStep(); // to show i marker
    ArrayMarker j = lang.newArrayMarker(array, 0, "j", null, amj);
    lang.nextStep(); // to show j marker
    sc.unhighlight(1); // unhighlight the declarations
    do {
      exchange = false;
      // downstairs
      for (i.move(right.getPosition(), null, defaultTiming); i.getPosition() >= left
          .getPosition(); i.decrement(null, defaultTiming)) {
        sc.highlight(2); // now at line 2(for loop)
        lang.nextStep(); // for decrement
        sc.toggleHighlight(2, 3);
        if (array.getData(i.getPosition()) - array.getData(i.getPosition() - 1) < 0) {
          lang.nextStep();
          sc.toggleHighlight(3, 4);
          exchange = true;
          finish.move(i.getPosition(), null, defaultTiming);
          lang.nextStep(); // move finish
          sc.unhighlight(4);
          // final E temp = array.getData(i - 1);
          // array.getData(i - 1) = array.getData(i);
          // array.getData(i) = temp;
        } else { // added for highlighting to ensure "if" is no longer
                 // highlighted
          lang.nextStep();
          sc.unhighlight(3);
        }

      }
      sc.highlight(5);
      array.swap(i.getPosition(), (i.getPosition() - 1), null, defaultTiming);
      lang.nextStep(); // change step
      sc.unhighlight(5);
      finish.increment(defaultTiming, defaultTiming);
      left.exchange(finish);
      // upstairs
      for (j.move(left.getPosition(), null, defaultTiming); j.getPosition() <= right
          .getPosition(); j.increment(null, defaultTiming)) {
        sc.highlight(6);
        lang.nextStep(); // for increment
        if (array.getData(j.getPosition()) - array.getData(j.getPosition() - 1) < 0) {
          lang.nextStep();
          sc.toggleHighlight(6, 7);
          exchange = true;
          finish.move(j.getPosition(), null, defaultTiming);
          lang.nextStep();// move finish
          sc.unhighlight(7);
          // final E temp = array.getData(j - 1);
          // array.getData(j - 1) = array.getData(j);
          // array.getData(j) = temp;
        } else {
          lang.nextStep();
          sc.unhighlight(6);
        }

      }
      sc.highlight(8);
      array.swap(j.getPosition(), j.getPosition() - 1, null, defaultTiming);
      lang.nextStep(); // change step
      sc.unhighlight(8);
      finish.decrement(defaultTiming, defaultTiming);
      right.exchange(finish);
    } while (exchange);
  }

  public String getCodeExample() {
    return "Straightforward CocktailSort Algorithm";
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public String getDescription() {
    return "Animates CocktailSort with Source Code + Highlighting";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getName() {
    return "CocktailSort";
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    init(); // ensure all properties are set up
    int[] arrayData = (int[]) primitives.get("array");
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        props.get("array", AnimationPropertiesKeys.COLOR_PROPERTY));
    sort(arrayData);
    return lang.toString();
  }

  public String getAlgorithmName() {
    return "CocktailSort";
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public String getAnimationAuthor() {
    return "Kristina Reiß";
  }

}
