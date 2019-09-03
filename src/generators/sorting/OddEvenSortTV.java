package generators.sorting;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;

/**
 * @author Tsvetoslava Vateva
 * 
 */

public class OddEvenSortTV extends AnnotatedAlgorithm implements
    generators.framework.Generator {

  private String                       comp        = "Compares";
  private String                       assi        = "Assignments";

  private int[]                        arrToSort;                  // the array
                                                                    // to be
                                                                    // sorted
  private AnimationPropertiesContainer container;
  private Hashtable<String, Object>    primitives;
  private ArrayProperties              arrProperties;
  private IntArray                     arrayToSort = null;         // the
                                                                    // visualisation
                                                                    // of the
                                                                    // array to
                                                                    // be sorted
  private ArrayMarkerProperties        arrayOddProps;
  private ArrayMarkerProperties        arrayEvenProps;
  private ArrayMarker                  oddMarker;
  private ArrayMarker                  evenMarker;
  private SourceCodeProperties         titelProperties;
  private SourceCodeProperties         props;
  private SourceCodeProperties         endProperties;
  private SourceCode                   titel;
  private SourceCode                   SortedF;
  private SourceCode                   SortedT;
  private SourceCode                   end;

  /**
   * Default constructor
   * 
   * @param array
   *          - the array to be sorted
   */

  // set the properties specified in the xml-file
  private void setProperties() {
    titelProperties = (SourceCodeProperties) container
        .getPropertiesByName("titel");
    props = (SourceCodeProperties) container.getPropertiesByName("source");
    endProperties = (SourceCodeProperties) container.getPropertiesByName("end");
    arrayOddProps = (ArrayMarkerProperties) container
        .getPropertiesByName("markerOdd");
    arrayEvenProps = (ArrayMarkerProperties) container
        .getPropertiesByName("markerEven");
    arrProperties = (ArrayProperties) container.getPropertiesByName("array");
  }

  public String getAlgorithmName() {
    return "Odd-Even-Sort";
  }

  public String getAnimationAuthor() {
    return "Tsvetoslava Vateva";
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getDescription() {
    return "Implementierung des Sortierverfahrens Odd-Even Sort. Zwei Zeiger sind verwendet um die sortierten "
        + "durchzuführen. Der erste Zeiger zeigt immer die Elemente an ungeraden Stellen "
        + "und die zweite- die Elemente an geraden Stellen. In jeder Interation werden zwei benachbarte "
        + "Elemente verglichen und falls notwendig getauscht. Eine Variable bezeichnet ob in einer Iteration "
        + "Elemente getauscht wurden. Wenn keine Elemente mehr getauscht werden, dann "
        + "ist das Array sortiert.";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getName() {
    return "OddEvenSort";
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  /*
   * the sorting algorithm and the animation details
   * 
   * @param arrayToSort- the array that is to be sorted
   */
  private void oddEvenSort(IntArray arrayToSort) {

    // highlight the name of the function in the PseudoCode
    exec("header");

    lang.nextStep();
    boolean sorted = false;
    SortedF = lang.newSourceCode(new Coordinates(420, 50), "end", null,
        endProperties);
    SortedF.addCodeLine("sorted=false", "sorted", 0, null);
    SortedT = lang.newSourceCode(new Coordinates(420, 50), "end", null,
        endProperties);
    SortedT.addCodeLine("sorted=true", "sorted", 0, null);
    SortedT.hide();

    // highlight sorted = false
    exec("resetSorted0");

    lang.nextStep();
    while (!sorted) {
      // highlight while
      exec("while");

      lang.nextStep();
      if (!sorted) {
        SortedF.hide();
        SortedT.show();
      }
      sorted = true;
      // highlight sorted = true;
      exec("setSorted");

      // for-initialisation highlight
      lang.nextStep();
      exec("for1_1");

      oddMarker = lang.newArrayMarker(arrayToSort, 1, "i", null, arrayOddProps);
      for (int i = 1; i < arrToSort.length - 1; i = i + 2) {
        lang.nextStep();
        // for-comparison highlight
        exec("for1_2");

        lang.nextStep();
        // if highlight
        exec("if1");

        arrayToSort.highlightCell(i, null, null);
        arrayToSort.highlightCell((i + 1), null, null);

        lang.nextStep();
        if (arrToSort[i] > arrToSort[i + 1]) {
          arrayToSort.swap(i, (i + 1), null, new MsTiming(450));
          // swap highlight
          exec("swap1");
          lang.nextStep();
          if (sorted) {
            SortedT.hide();
            SortedF.show();
          }
          sorted = false;
          // sorted = false highlight
          exec("resetSorted1");

        }
        if (i + 1 < arrToSort.length - 1)
          // moving the pointer to the array
          oddMarker.move((i + 2), new MsTiming(150), null);

        arrayToSort.unhighlightCell(i, null, null);
        arrayToSort.unhighlightCell((i + 1), null, null);
        lang.nextStep();
        // for-increment highlight
        exec("for1_3");

      }

      lang.nextStep();
      // for1-cond highlight
      exec("for1_2");

      lang.nextStep();
      // for2-initialisation highlight
      exec("for2_1");
      evenMarker = lang.newArrayMarker(arrayToSort, 0, "j", null,
          arrayEvenProps);

      for (int i = 0; i < arrToSort.length - 1; i = i + 2) {
        lang.nextStep();
        // for2-comparison highlight
        exec("for2_2");
        lang.nextStep();
        // if2 highlight
        exec("if2");
        arrayToSort.highlightCell(i, null, null);
        arrayToSort.highlightCell((i + 1), null, null);

        lang.nextStep();
        if (arrToSort[i] > arrToSort[i + 1]) {
          arrayToSort.swap(i, (i + 1), null, new MsTiming(450));
          // swap2 highlight
          exec("swap2");
          lang.nextStep();
          if (sorted) {
            SortedT.hide();
            SortedF.show();
          }
          sorted = false;
          // sorte2 = false highlight
          exec("resetSorted2");

        }
        if (i + 1 < arrToSort.length - 1) {
          // moving the pointer to the array
          evenMarker.move((i + 2), new MsTiming(150), null);
        }
        lang.nextStep();
        // for2-increment highlight
        exec("for2_3");

        arrayToSort.unhighlightCell(i, null, null);
        arrayToSort.unhighlightCell((i + 1), null, null);

      }
      lang.nextStep();
      // for2-cond highlight
      exec("for2_2");

      lang.nextStep();
      // while highlight
      exec("while");
      evenMarker.hide();
      oddMarker.hide();
    }
    end = lang.newSourceCode(new Coordinates(400, 250), "end", null,
        endProperties);
    // return highlight
    exec("return");
    end.addCodeLine("The array is sorted", "sorted", 0, null);
  }

  /**
   * This function defines the properties of the animation and calls the sorting
   * algorithm
   */
  public void initLocal() {

    super.init();
    // initialisation
    titelProperties = new SourceCodeProperties();
    props = new SourceCodeProperties();
    endProperties = new SourceCodeProperties();
    arrayOddProps = new ArrayMarkerProperties();
    arrayEvenProps = new ArrayMarkerProperties();
    arrProperties = new ArrayProperties();
    arrToSort = (int[]) (primitives.get("intArray"));

    setProperties();

    titel = lang.newSourceCode(new Coordinates(400, 25), "title", null,
        titelProperties);
    titel.addCodeLine("Odd-Even Sort", "OddEvenSort", 0, null);

    // add sourceCode and swapSourceCode in one step
    sourceCode = lang.newSourceCode(new Coordinates(5, 5), "sourceCode", null,
        props);

    // arrProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
    // Color.LIGHT_GRAY);
    arrayToSort = lang.newIntArray(new Coordinates(370, 150), arrToSort,
        "arrayToSort", null, arrProperties);

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

    parse();
    // call oddEvenSort-function in a new animation step
    lang.nextStep();
    oddEvenSort(arrayToSort);

  }

  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {

    primitives = arg1;
    container = arg0;
    initLocal(); // creates the visual structures and calls the sorting function
    return lang.toString();

  }

  public String getAnnotatedSrc() {

    String buffer = new String();
    buffer = buffer + "OddEvenSort(int []arr) 									@label(\"header\")\n";
    buffer = buffer
        + "	 sorted = false; 										@label(\"resetSorted0\") @declare(\"int\", \"sorted\", \"1\") @inc(\""
        + assi + "\")\n";
    buffer = buffer + "		while not sorted; 									@label(\"while\")\n";
    buffer = buffer
        + "			sorted = true; 									@label(\"setSorted\")@set(\"sorted\", \"1\") @inc(\""
        + assi + "\")\n";
    buffer = buffer + "			   for ( x = 1; 						 		@label(\"for1_1\") @inc(\""
        + assi + "\") \n";
    buffer = buffer
        + "						x < list.length-1; 					@label(\"for1_2\") @continue @inc(\""
        + comp + "\") \n";
    buffer = buffer
        + "							x += 2)							@label(\"for1_3\") @continue @inc(\"" + assi
        + "\") \n";
    buffer = buffer
        + "			      if (list[x] > list[x+1])					@label(\"if1\")@inc(\"" + comp
        + "\")\n";
    buffer = buffer
        + "       			 swap list[x] and  list[x+1]; 			@label(\"swap1\")\n";
    buffer = buffer
        + "        			 sorted = false;						@label(\"resetSorted1\")@set(\"sorted\", \"0\") @inc(\""
        + assi + "\")\n";
    buffer = buffer + "			   for ( x = 0; 						 		@label(\"for2_1\") @inc(\""
        + assi + "\") \n";
    buffer = buffer
        + "						x < list.length-1; 					@label(\"for2_2\") @continue @inc(\""
        + comp + "\") \n";
    buffer = buffer
        + "							x += 2)							@label(\"for2_3\") @continue @inc(\"" + assi
        + "\") \n";
    buffer = buffer
        + "      		  if (list[x] > list[x+1])					@label(\"if2\")@inc(\""
        + comp + "\")\n";
    buffer = buffer
        + "        			 swap list[x] and  list[x+1]; 			@label(\"swap2\")\n ";
    buffer = buffer
        + "        			 sorted = false;						@label(\"resetSorted2\")@set(\"sorted\", \"0\") @inc(\""
        + assi + "\")\n";
    buffer = buffer + " 	return arr;											@label(\"return\")\n";

    return buffer;
  }

}