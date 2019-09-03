package generators.sorting;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;

/**
 * @author Michael Scharf
 * 
 *         Praktikum - Visualisierung & Animation von Algorithmen und
 *         Datenstrukturen Aufgabe 7
 * 
 *         Strand Sort
 * 
 */
public class StrandSort extends AnnotatedAlgorithm implements Generator {

  private Language            lang;
  private SourceCode          sc;

  private String[]            unsortedList;
  private String[]            subList;
  private String[]            sortedList;

  private StringArray         unsorted;
  private StringArray         sub;
  private StringArray         sorted;

  private ArrayMarker         iMarker;
  private ArrayMarker         jMarker;
  private ArrayMarker         kMarker;

  private static final String NAME        = "StrandSort";

  private static final String DESCRIPTION = "Strand Sort entnimmt sortierte Teillisten aus der urspr&uuml;nglichen Liste "
                                              + " und f&uuml;gt diese anschlie&szlig;end wieder zusammen."
                                              + " Der Algorithmus hat eine durchschnittliche Komplexit&auml;t von O(n log n). Das Verfahren arbeitet nicht in-place";

  /**
   * Default constructor
   * 
   * @param l
   *          the conrete language object used for creating output
   */
  public StrandSort(Language l) {

    // Store the language object
    lang = l;

    // This initializes the step mode. Each pair of subsequent steps has to
    // be divdided by a call of lang.nextStep();
    lang.setStepMode(true);

    this.init();
  }

  /**
   * Default constructor
   */
  public StrandSort() {
    this(new AnimalScript("Strand Sort", "Michael Scharf", 640, 480));
  }

  /**
   * Run Strand Sort Algorithm
   */
  public void sort() {
    // Highlight first line
    exec("header");
    lang.nextStep();

    // Highlight next line
    exec("sublist");
    lang.nextStep();

    exec("sorted");
    lang.nextStep();
    exec("while");

    while (unsorted.getLength() > 0 && !unsorted.getData(0).equals(" ")) {
      iMarker.move(0, null, null);
      lang.nextStep();

      exec("clear");
      clearSublist();
      lang.nextStep();

      sub.put(0, unsorted.getData(0), null, null);
      exec("add");
      lang.nextStep();

      removeFromList(0, unsorted);
      exec("remove0");
      lang.nextStep();

      exec("for");
      int last = 0;
      while (Integer.parseInt(vars.get("i")) < unsorted.getLength()
          && !unsorted.getData(Integer.parseInt(vars.get("i"))).equals(" ")) {

        iMarker.move(Integer.parseInt(vars.get("i")), null, null);
        lang.nextStep();

        exec("if");
        lang.nextStep();
        if (Integer.parseInt(unsorted.getData(Integer.parseInt(vars.get("i")))) > Integer
            .parseInt(sub.getData(last))) {
          jMarker.move(++last, null, null);
          sub.put(last, unsorted.getData(Integer.parseInt(vars.get("i"))),
              null, null);
          exec("addLast");
          lang.nextStep();

          exec("removec");
          removeFromList(Integer.parseInt(vars.get("i")), unsorted);
          lang.nextStep();
          exec("endif");
        } else {
          exec("endif");
        }

        lang.nextStep();
        exec("for");

      }
      lang.nextStep();
      String[] data = new String[sub.getLength()];
      for (int i = 0; i < data.length; i++)
        data[i] = sub.getData(i);
      CheckpointUtils.checkpointEvent(this, "sublist",
          new Variable("sub", data));// ////////////////////////
      exec("merge");
      lang.nextStep();
      merge(sorted, sub);
      exec("while");
      lang.nextStep();

    }

    exec("return");
    lang.nextStep();

    exec("end");
  }

  /**
   * This method is used for merging two sorted String Arrays.
   * 
   * @param sorted
   * @param sub
   */
  private void merge(StringArray sorted, StringArray sub) {

    for (int j = 0; j < sub.getLength() && !sub.getData(j).equals(" "); j++) {
      jMarker.move(j, null, null);
      boolean inserted = false;
      int k = 0;
      while (!inserted) {
        kMarker.move(k, null, null);
        lang.nextStep();
        if (sorted.getData(k).equals(" ")) {
          sorted.put(k, sub.getData(j), null, null);
          inserted = true;
        } else if (Integer.parseInt(sorted.getData(k)) > Integer.parseInt(sub
            .getData(j))) {
          insert(sorted, k, sub.getData(j));
          inserted = true;
        }
        k++;
      }

    }

  }

  /**
   * This method inserts a String into a StringArray on a given position. Shifts
   * the element currently at that position (if any) and any subsequent elements
   * to the right.
   * 
   * @param li
   * @param pos
   * @param data
   */
  private void insert(StringArray li, int pos, String data) {
    String temp = data;
    for (int i = pos; i < li.getLength() && !temp.equals(" "); i++) {
      String c = li.getData(i);
      li.put(i, temp, null, null);
      temp = c;
    }
  }

  /**
   * Clears the sublist. every cell gets filled with a blank.
   */
  private void clearSublist() {
    for (int i = 0; i < sub.getLength() && !sub.getData(i).equals(" "); i++) {
      sub.put(i, " ", null, null);

    }
  }

  /**
   * This method removes one specified element form the list. Shifts any
   * subsequent elements to the left.
   * 
   * @param pos
   * @param li
   */
  private void removeFromList(int pos, StringArray li) {
    for (int i = pos; i < li.getLength() && !li.getData(i).equals(" "); i++) {
      if (i == li.getLength() - 1)
        li.put(i, " ", null, null);
      else
        li.put(i, li.getData(i + 1), null, null);

    }
  }

  /**
   * Returns the AlgorithmCode
   * 
   * @return the generated code
   */
  public String getAlgorithmCode() {
    return sc.toString();
  }

  /**
   * Returns the AlgorithmDescription
   * 
   * @return the description
   */
  public String getAlgorithmDescription() {
    return DESCRIPTION;
  }

  /*
   * Returns the CodeExample (non-Javadoc)
   * 
   * @see generator.Generator#getCodeExample()
   */
  public String getAnnotatedSrc() {

    return "public List<Comparable> sort(List<Comparable> unsorted) { @label(\"header\") @highlight(\"end\")   @openContext  @declare(\"int\", \"i\", \"0\") \n"
        + "   List<Comparable> sublist = new LinkedList<Comparable>(); @declare(\"int\", \"j\", \"0\") @label(\"sublist\")\n"
        + "   List<Comparable> sorted = new LinkedList<Comparable>();  @declare(\"int\", \"k\", \"0\") @label(\"sorted\")\n"
        + "   while (!unsorted.isEmpty()) { @label(\"while\")\n"
        + "      sublist.clear(); @label(\"clear\")\n"
        + "      sublist.add(0, unsorted.get(0)); @label(\"add\")\n"
        + "      unsorted.remove(0); @label(\"remove0\") @set(\"i\", \"0\")\n"
        + "      for (Comparable c : unsorted) @label(\"for\") )\n"
        + "         if (!sublist.isEmpty() && c.compareTo(sublist.getLast()) > 0) { @label(\"if\")\n"
        + "            sublist.addLast(c); @label(\"addLast\")\n"
        + "            unsorted.remove(c); @label(\"removec\")\n"
        + "         } @label(\"endif\") @inc(\"i\")\n"
        + "      merge(sorted, sublist); @label(\"merge\")\n"
        + "   } @label(\"endwhile\")\n"
        + "   return sorted; @label(\"return\") \n" + "} @label(\"end\") \n";
  }

  /*
   * (non-Javadoc)
   * 
   * @see generator.Generator#getDescription()
   */
  public String getDescription() {
    return DESCRIPTION;
  }

  /*
   * This method gets called by the Generator. (non-Javadoc)
   * 
   * @see
   * generator.Generator#generate(generator.properties.AnimationPropertiesContainer
   * , java.util.Hashtable)
   */
  public String generate(AnimationPropertiesContainer prop,
      Hashtable<String, Object> values) {

    int[] a = (int[]) values.get("Liste");
    int n = a.length;

    // create the three arrays.
    unsortedList = new String[n];
    subList = new String[n];
    sortedList = new String[n];

    // intialize the arrays
    for (int i = 0; i < n; i++) {
      unsortedList[i] = Integer.toString(a[i]);
      sortedList[i] = " ";
      subList[i] = " ";
    }

    init(prop);

    this.sort();

    return lang.toString();
  }

  /*
   * Retruns the name of the algorithm. (non-Javadoc)
   * 
   * @see generator.Generator#getAlgorithmName()
   */
  @Override
  public String getAlgorithmName() {
    return "Strand Sort";
  }

  /*
   * (non-Javadoc)
   * 
   * @see generator.Generator#getName()
   */
  @Override
  public String getName() {
    return NAME;
  }

  /*
   * (non-Javadoc)
   * 
   * @see generator.Generator#getAnimationAuthor()
   */
  @Override
  public String getAnimationAuthor() {
    return "Michael Scharf";
  }

  /*
   * (non-Javadoc)
   * 
   * @see generator.Generator#getContentLocale()
   */
  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  /*
   * (non-Javadoc)
   * 
   * @see generator.Generator#getFileExtension()
   */
  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  /*
   * (non-Javadoc)
   * 
   * @see generator.Generator#getGeneratorType()
   */
  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  /*
   * (non-Javadoc)
   * 
   * @see generator.Generator#getOutputLanguage()
   */
  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  /*
   * (non-Javadoc)
   * 
   * @see generator.Generator#init()
   */
  public void init(AnimationPropertiesContainer prop) {
    super.init();

    sorted = null;
    unsorted = null;
    sub = null;

    // first, set the visual properties (somewhat similar to CSS)
    ArrayProperties arrayProps = (ArrayProperties) prop
        .getPropertiesByName("Liste");

    // now, create the unsorted array, linked to the properties
    unsorted = lang.newStringArray(new Coordinates(20, 100), unsortedList,
        "unsortedList", null, arrayProps);

    // now, create the temp array, linked to the properties
    sub = lang.newStringArray(new Coordinates(20, 190), subList, "subList",
        null, arrayProps);

    // now, create the sorted array, linked to the properties
    sorted = lang.newStringArray(new Coordinates(20, 280), sortedList,
        "sortedList", null, arrayProps);

    // int the markers
    ArrayMarkerProperties arrayIMProps = new ArrayMarkerProperties();
    arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");

    arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    iMarker = lang.newArrayMarker(unsorted, 0, "i", null, arrayIMProps);

    ArrayMarkerProperties arrayJMProps = new ArrayMarkerProperties();
    arrayJMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");

    arrayJMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    jMarker = lang.newArrayMarker(sub, 0, "j", null, arrayJMProps);

    ArrayMarkerProperties arrayKMProps = new ArrayMarkerProperties();
    arrayKMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "k");

    arrayKMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    kMarker = lang.newArrayMarker(sorted, 0, "k", null, arrayKMProps);

    SourceCodeProperties scProps = (SourceCodeProperties) prop
        .getPropertiesByName("Source");

    // now, create the source code entity
    sourceCode = lang.newSourceCode(new Coordinates(400, 140), "sourceCode",
        null, scProps);

    // parsing anwerfen
    parse();
  }

  /**
   * @return the language instance
   */
  public Language getLanguage() {
    return lang;
  }

}
