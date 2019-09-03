package generators.sorting.gnomesort;

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
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class GnomeSortTUS implements Generator {

  private Language lang;

  public GnomeSortTUS() {
  }

  public GnomeSortTUS(Language l) {
    lang = l;
    lang.setStepMode(true);
  }

  private static final String DESCRIPTION =
                                          // von Wikipedia inspiriert
                                          "GnomeSort ist ein einfacher Sortier-Algorithmus.\n"
                                              + " Als Beispiel stelle man sich einen Gartenzwerg (garden gnome) vor,\n"
                                              + "welcher vor Blument\u2202pfen steht, die unterschiedliche Gr\u2202\u00fcen haben d\u00barfen.\n"
                                              + "Die Blument\u2202pfe sind in einer von links nach rechts verlaufenden\n"
                                              + "Reihe aufgestellt. Ganz links steht der Gartenzwerg und m\u2202chte die\n"
                                              + "Blument\u2202pfe von links nach rechts der Gr\u2202\u00fce nach aufsteigend sortieren.\n"
                                              + "Dazu vergleicht er die beiden Blument\u2202pfe, vor denen er grade steht.\n"
                                              + "Stellt er fest, dass sie in der richtigen Reihenfolge sind, so macht\n"
                                              + "er einen Schritt nach rechts. Stellt er hingegen fest, dass die Reihenfolge\n"
                                              + "nicht stimmt, so vertauscht er die beiden Blument\u2202pfe und macht einen Schritt\n"
                                              + "nach links. Dies wiederholt er st\u00a7ndig. Fertig ist er, wenn er am ganz rechts\n"
                                              + "stehenden Blumentopf ankommt und feststellt, dass dieser in der richtigen\n"
                                              + "Reihenfolge steht.";

  private static final String SOURCE_CODE = "void gnomesort() {" // 0
                                              + "\n int i = 0;" // 1
                                              + "\n while (i < arraysize) {" // 2
                                              + "\n  if (i == 0  ||  Eintrag i-1 <= Eintrag i) {" // 3
                                              + "\n   i++" // 4
                                              + "\n  }" // 5
                                              + "\n  else {" // 6
                                              + "\n   Tausche Eintrag i-1 und i;" // 7
                                              + "\n   i--;" // 8
                                              + "\n   }" // 9
                                              + "\n  }" // 10
                                              + "\n }";              // 11

  public void sort(int[] a, AnimationPropertiesContainer aprops) {

    // erstelle Titel
    TextProperties txprops = new TextProperties();
    txprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 18));
    txprops.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        aprops.get("title", AnimationPropertiesKeys.COLOR_PROPERTY));
    lang.newText(new Coordinates(100, 20), "GnomeSort", "title", null, txprops);

    lang.nextStep();

    // Erstelle Quellcode
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, aprops.get(
        "sourceCode", AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        aprops.get("sourceCode", AnimationPropertiesKeys.COLOR_PROPERTY));
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        aprops.get("sourceCode", AnimationPropertiesKeys.FONT_PROPERTY));
    scProps.set(AnimationPropertiesKeys.SIZE_PROPERTY,
        aprops.get("sourceCode", AnimationPropertiesKeys.SIZE_PROPERTY));

    SourceCode sc = lang.newSourceCode(new Coordinates(40, 160), "sourceCode",
        null, scProps);

    sc.addCodeLine("void gnomesort() {", null, 0, null);
    sc.addCodeLine("int i = 0;", null, 1, null);
    sc.addCodeLine("while (i < arraylaenge) {", null, 1, null);
    sc.addCodeLine("if (i == 0  ||  Eintrag i-1 <= Eintrag i) {", null, 2, null);
    sc.addCodeLine("i++;", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("else {", null, 2, null);
    sc.addCodeLine("Tausche Eintrag i-1 und i;", null, 3, null);
    sc.addCodeLine("i--;", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("}", null, 0, null);

    lang.nextStep();

    // Erstelle Array
    ArrayProperties arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        aprops.get("array", AnimationPropertiesKeys.COLOR_PROPERTY));
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
        aprops.get("array", AnimationPropertiesKeys.FILL_PROPERTY));
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,
        aprops.get("array", AnimationPropertiesKeys.FILLED_PROPERTY));
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
        aprops.get("array", AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY));
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        aprops.get("array", AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY));
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        aprops.get("array", AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY));

    IntArray ia = lang.newIntArray(new Coordinates(20, 120), a, "intArray",
        null, arrayProps);

    lang.nextStep();

    try {
      // starte GnomeSort
      gnomeSort(a.length, ia, sc);
    } catch (LineNotExistsException e) {
      e.printStackTrace();

    }
  }

  private void gnomeSort(int size, IntArray heap, SourceCode sc)
      throws LineNotExistsException {

    sc.highlight(0);

    lang.nextStep();

    sc.toggleHighlight(0, 1);

    ArrayMarkerProperties arrayJMProps = new ArrayMarkerProperties();
    arrayJMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "  i");
    arrayJMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    ArrayMarker jMarker = lang.newArrayMarker(heap, 0, "i", null, arrayJMProps);

    int i = 0, temp;

    lang.nextStep();

    sc.toggleHighlight(1, 2);

    while (i < size) {

      lang.nextStep();

      sc.toggleHighlight(2, 3);

      if (i == 0 || heap.getData(i - 1) <= heap.getData(i)) {

        lang.nextStep();

        sc.toggleHighlight(3, 4);

        i++;

        if (i < size)
          jMarker.move(i, null, null);
        // else
        // jMarker.moveOutside(null, null);

        lang.nextStep();

        sc.toggleHighlight(4, 2);
      } else {
        lang.nextStep();

        sc.toggleHighlight(3, 6);

        lang.nextStep();

        sc.toggleHighlight(6, 7);

        heap.highlightCell(i, null, null);
        heap.highlightCell(i - 1, null, null);

        lang.nextStep();

        // heap.swap(i, i-1, null, null);

        temp = heap.getData(i);
        heap.put(i, heap.getData(i - 1), null, null);
        heap.put(i - 1, temp, null, null);

        lang.nextStep();

        sc.toggleHighlight(7, 8);
        heap.unhighlightCell(i, null, null);
        heap.unhighlightCell(i - 1, null, null);

        i--;

        jMarker.move(i, null, null);

        lang.nextStep();

        sc.toggleHighlight(8, 2);

      }

    }

    sc.toggleHighlight(2, 11);

  }

  public String getAlgorithmName() {
    return "GnomeSort";
  }

  public String getCodeExample() {

    return SOURCE_CODE;
  }

  public Locale getContentLocale() {

    return Locale.GERMANY;
  }

  public String getDescription() {

    return DESCRIPTION;
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {

    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getName() {

    return "GnomeSort";
  }

  public String getOutputLanguage() {

    return Generator.PSEUDO_CODE_OUTPUT;
  }

  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    // TODO Auto-generated method stub
    // extract array parameter
    int[] myArray = (int[]) arg1.get("array");

    // create language instance
    lang = new AnimalScript("GnomeSort Animation", "hiuig", 640, 480);
    GnomeSortTUS s = new GnomeSortTUS(lang);
    // active step mode
    lang.setStepMode(true);

    // sort the array
    s.sort(myArray, arg0);

    // return the output
    return lang.toString();
  }

  public String getAnimationAuthor() {
    return "Daniel Thies, Dominik Ulrich, Jörg Schmalfuß";
  }

  public void init() {
    // nothing to be done here
  }

}
