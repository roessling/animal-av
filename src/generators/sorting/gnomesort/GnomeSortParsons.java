package generators.sorting.gnomesort;

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
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

/**
 * @author Malcolm Parsons
 */
public class GnomeSortParsons extends AnnotatedAlgorithm implements
    generators.framework.Generator {
  private ArrayProperties      arrayProps;
  private SourceCodeProperties scProps;
  @SuppressWarnings("unused")
  private Text                 header;

  private ArrayMarker          am;

  // array properties
  private Color                arrayElementColor          = Color.BLACK;
  private Color                arraFillColor              = Color.LIGHT_GRAY;
  private Color                arrayElementHighlightColor = Color.YELLOW;
  private Color                arrayCellHighlightColor    = Color.YELLOW;

  // source code properties
  private Color                soureCodeContextColor      = Color.MAGENTA;
  private Color                sourceCodeHighlightColor   = Color.MAGENTA;
  private Color                sourceCodeColor            = Color.GRAY;
  private Font                 sourceCodeFont             = new Font(
                                                              "Monospaced",
                                                              Font.BOLD, 12);

  private Timing               longTiming                 = new TicksTiming(80);
  private Timing               markerApperance            = new TicksTiming(20);
  final int                    linesOfBubbleSortCode      = 11;

  public GnomeSortParsons() {
  }

  /**
   * Create a header
   * 
   * @return created header
   */
  private Text getHeader() {
    Text header = lang.newText(new Coordinates(20, 30), "GnomeSort animated",
        "header", null);
    header.setFont(new Font("SansSerif", Font.BOLD, 24), null, null);
    lang.nextStep();
    return header;
  }

  @Override
  public String getAnnotatedSrc() {
    return "public void sort(int[] array) {  						@label(\"header\")\n"
        + "	int position = 0;											@label(\"initPos\") @declare(\"int\", \"position\", \"0\")\n"
        + "	while (position < array.getLength()) {						@label(\"while\")\n"
        + "		if (position == 0 || (array.getData(position - 1) <= array.getData(position))) {	@label(\"if\")\n"
        + "			position++;											@label(\"incPos\") @inc(\"position\")\n"
        + "		}else {													@label(\"else\")\n"
        + "			swap(array, position, position - 1);				@label(\"swap\")\n"
        + "			position--;											@label(\"decrPos\") @dec(\"position\")\n"
        + "		}														@label(\"endElse\")\n"
        + "	}															@label(\"endWhile\")\n"
        + "}															@label(\"endMethod\")\n";
  }

  /**
   * create array properties an the array
   * 
   * @param array
   *          normal array to be converted
   * @param arg0
   *          Properties of the array (have to be read first)
   * @return created IntArray
   */
  private IntArray getIntArray(int[] myArray, AnimationPropertiesContainer props) {
    arrayProps = new ArrayProperties();
    int[] array = myArray;
    if (array == null) {
      array = new int[] { 4, 3, 2, 5, 9, 8 };
    }

    if (props == null) {
      // use standard values
      arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
          arrayElementColor);
      arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // filled
      arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, arraFillColor);
      arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
          arrayElementHighlightColor);
      arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
          arrayCellHighlightColor);
    } else {
      // use the ones from the generator
      arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
          props.get("arrayProps", AnimationPropertiesKeys.COLOR_PROPERTY));
      arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, props.get(
          "arrayProps", AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY));
      arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
          props.get("arrayProps", AnimationPropertiesKeys.FILL_PROPERTY));
      arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, props.get(
          "arrayProps", AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY));
      arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, props.get(
          "arrayProps", AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));
    }
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // filled

    IntArray initArray = lang.newIntArray(new Coordinates(20, 100), array,
        "intArray", null, arrayProps);
    lang.nextStep();
    return initArray;
  }

  /**
   * Creates and sets source code and its properties
   * 
   * @param arg0
   *          Container holding the properties information
   */
  private void setSourceCodeProps(AnimationPropertiesContainer props) {
    scProps = new SourceCodeProperties();

    if (props == null) {
      scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
          soureCodeContextColor);
      scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, sourceCodeFont);
      scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
          sourceCodeHighlightColor);
      scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, sourceCodeColor);
    } else {
      scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, props.get(
          "sourceCodeProps", AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
      scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, props.get(
          "sourceCodeProps", AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY));
      scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
          props.get("sourceCodeProps", AnimationPropertiesKeys.FONT_PROPERTY));
      scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
          props.get("sourceCodeProps", AnimationPropertiesKeys.COLOR_PROPERTY));
    }
  }

  /**
   * The actual GnomeSort, sorting an array and creating animal script code
   */
  private void gnomeSort(IntArray array) {
    exec("header");
    lang.nextStep();

    exec("initPos");
    lang.nextStep();

    // next algo step
    exec("while");
    while (Integer.parseInt(vars.get("position")) < array.getLength()) {
      exec("while");
      // lang.nextStep();
      lang.nextStep("while-position=" + vars.get("position"));

      // next algo step
      exec("if");
      // Check in order to highlight the two adjacent cell elements for the
      // second statement
      // (left smaller than right)
      if (Integer.parseInt(vars.get("position")) != 0) {
        array.highlightElem(Integer.parseInt(vars.get("position")) - 1,
            Integer.parseInt(vars.get("position")), null, null);
      }
      lang.nextStep();

      if (Integer.parseInt(vars.get("position")) == 0
          || (array.getData(Integer.parseInt(vars.get("position")) - 1) <= array
              .getData(Integer.parseInt(vars.get("position"))))) {
        // extra check for animal code
        if (Integer.parseInt(vars.get("position")) == 0) {
          array.highlightCell(Integer.parseInt(vars.get("position")), null,
              null);
          lang.nextStep();

          array.unhighlightCell(Integer.parseInt(vars.get("position")), null,
              null);
          // only move marker without highlighting cells
          exec("incPos");
          am.increment(null, longTiming);
          lang.nextStep();
        } else { // means (array.getData(position - 1) <=
                 // array.getData(position)) is true
          // highlight cell elements and then move marker on

          array.unhighlightElem(Integer.parseInt(vars.get("position")) - 1,
              Integer.parseInt(vars.get("position")), longTiming, longTiming);
          exec("incPos");
          if (am.getPosition() == array.getLength() - 1) {
            am.moveOutside(null, null);
          } else {
            am.increment(new TicksTiming(longTiming.getDelay() * 2), longTiming);
          }
          lang.nextStep();
        }
      }
      // next algo step
      else { // left larger than right
        exec("else");
        lang.nextStep();

        // next algo step
        exec("swap");
        swap(array, Integer.parseInt(vars.get("position")),
            Integer.parseInt(vars.get("position")) - 1);
        lang.nextStep();

        // next algo step
        // System.out.println("DEBUG POSITON OF MARKER: before decrementing: " +
        // am.getPosition());
        am.decrement(null, longTiming);
        // System.out.println("DEBUG POSITON OF MARKER: after decrementing: " +
        // am.getPosition());
        array.unhighlightElem(Integer.parseInt(vars.get("position")) - 1,
            Integer.parseInt(vars.get("position")), null, null);
        exec("decrPos");
        lang.nextStep();
      }
    }
    // finalize the animation
    exec("while");
    lang.nextStep();
    Text terminate = lang.newText(new Offset(0, 30, array,
        AnimalScript.DIRECTION_S), "GnomeSort terminated", "terminated", null);
    terminate.setFont(new Font("SansSerif", Font.BOLD, 20), null, null);
    terminate.changeColor(null, Color.RED, null, null);
  }

  /**
   * Swaps to elements of an array
   * 
   * @param array
   *          array holding the elements
   * @param left
   *          left element
   * @param right
   *          right element
   */
  private void swap(IntArray array, int left, int right) {
    array.swap(left, right, null, longTiming);
  }

  @Override
  public void init() {
    super.init();
  }

  @Override
  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    int[] arrayData = null;
    if (arg1 != null) {
      arrayData = (int[]) arg1.get("arrayToSort");
    }

    init();

    header = getHeader();
    setSourceCodeProps(arg0);
    sourceCode = lang.newSourceCode(new Coordinates(10, 200), "sumupCode",
        null, scProps);
    IntArray intArray = getIntArray(arrayData, arg0);
    am = lang.newArrayMarker(intArray, 0, "position", markerApperance);

    parse();
    gnomeSort(intArray);
    return lang.toString();
  }

  /*
   * ****************************************************
   * Helper Functions ****************************************************
   */

  @Override
  public String getAlgorithmName() {
    return "Gnome Sort";
  }

  @Override
  public String getAnimationAuthor() {
    return "Malcolm Parsons";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.US;
  }

  @Override
  public String getDescription() {
    return "This Animation explains the GnomeSort Algorithm (annotated)";
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
    return "GnomeSort (annotated)";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}