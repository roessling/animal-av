package generators.searching.interpolatedsearching;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;

import algorithm.animalTranslator.AnimalTranslator;
import algorithm.fileHandler.FileHandler;
import algorithm.search.InterpolationSearch;

/**
 * This class creates a wrapper for the generation of an Interpolated Search
 * animation. The actual animation generation is based on code by Michael Maur
 * <mmaur@web.de>.
 * 
 * @author Guido Roessling (roessling@acm.org>
 * @version 1.0 28.09.2005
 */
public class InterpolationSearchWrapper implements Generator {
  private static final String DESCRIPTION = "Die Interpolationssuche ist ein sehr effizientes (doppelt "
                                              + "logarithmisches) Verfahren zur Suche eines Wertes in einem sortierten Feld."
                                              + "\nZuerst wird die vermutliche Positionen des gesuchten Elements "
                                              + "berechnet. Dazu wird die Distanz des gesuchte Wert vom Anfangswert "
                                              + "des (Teil)feldes mit der Teilfeldl&auml;nge multipliziert und anschlie&szlig;end "
                                              + "durch die Intervallbreite (letzter Wert - erster Wert) geteilt. Bei "
                                              + "angenommener Gleichverteilung der Werte w&auml;re dies die Zielposition des "
                                              + "gesuchten Elements.<p>"
                                              + "Nun wird diese interpolierte Position des Arrays &uuml;berpr&uuml;ft. "
                                              + "Der Wert an der Position kann kleiner, gr&ouml;&szlig;er oder gleich dem gesuchten Element sein. "
                                              + "Ist er kleiner als das gesuchte Element, muss das gesuchte Element "
                                              + "in der hinteren H&auml;lfte stecken, falls es sich dort &uuml;berhaupt befindet. "
                                              + "Ist er hingegen gr&ouml;&szlig;er, muss nur in der vorderen H&auml;lfte weitergesucht "
                                              + "werden. Die jeweils andere H&auml;lfte muss nicht mehr betrachtet werden. "
                                              + "Ist der Wert gleich dem gesuchten Element, ist die Suche (vorzeitig) beendet."
                                              + "<p>Jede weiterhin zu untersuchende H&auml;lfte wird wieder gleich behandelt: "
                                              + "Das Element an der interpolierten Position liefert wieder die Entscheidung "
                                              + "dar&uuml;ber, wo bzw. ob weitergesucht werden muss."
                                              + "<p>Die L&auml;nge des Suchbereiches schrumpft von Schritt zu Schritt oft drastisch. "
                                              + "Sp&auml;testens wenn der Suchbereich auf ein Element geschrumpft ist, ist die "
                                              + "Suche beendet. Dieses eine Element ist entweder das gesuchte Element, "
                                              + "oder das gesuchte Element kommt nicht vor."
                                              + "<p>Der Algorithmus zur Interpolationssuche wird entweder als Iteration oder "
                                              + "Rekursion implementiert. Hier wurde eine iterative Implementierung gew&auml;hlt.";

  private static final String SOURCE_CODE = "private int binSearchIterativ(int[] a, int x) {"
                                              + "\n  if (a == null || a.length == 0) {"
                                              + "\n    return -1;"
                                              + "\n  }"
                                              + "\n  int l = 0, r = a.length - 1, midElem = "
                                              + "\n    l + ((x - a[l]) * (r - l)) / (a[r] - a[l]);"
                                              + "\n  while (a[midElem] != x && r > l) {"
                                              + "\n    if (x < a[midElem]) {"
                                              + "\n      r = midElem - 1;"
                                              + "\n    } else {"
                                              + "\n      l = midElem + 1;"
                                              + "\n    }"
                                              + "\n    midElem = l + ((x - a[l]) * (r - l)) / (a[r] - a[l]);"
                                              + "\n  }"
                                              + "\n  if (a[midElem] == x) {"
                                              + "\n    return midElem;"
                                              + "\n  } else {"
                                              + "\n    return -1;"
                                              + "\n  }"
                                              + "\n}";

  private GeneratorType       myType      = new GeneratorType(
                                              GeneratorType.GENERATOR_TYPE_SEARCH);

  /**
   * returns the generator type for this animation,
   * <code>GeneratorType.GENERATOR_TYPE_SEARCH</code>
   * 
   * @return <code>GeneratorType.GENERATOR_TYPE_SEARCH</code> as the type for
   *         this animation generator
   * @see generators.framework.GeneratorType#GENERATOR_TYPE_SEARCH
   */
  public GeneratorType getGeneratorType() {
    return myType;
  }

  /**
   * returns the name for this animation generation
   * 
   * @return this generator's name
   */
  public String getName() {
    return "Interpolationssuche";
  }

  /**
   * getDescription returns the Description of this Generator.
   * 
   * @return The Description of this Generator.
   */
  public String getDescription() {
    return DESCRIPTION;
  }

  /**
   * This method is meant to return a short code example. As AnimalScript is not
   * very illustrative, this will instead return the underlying Java code
   * 
   * @see generators.framework.Generator#getCodeExample()
   */
  public String getCodeExample() {
    return SOURCE_CODE;
  }

  /**
   * getFileExtension returns the Extension for the file that is generated by
   * this Generator. This should be "asu" (animal-script-uncompressed), "asc"
   * (animal-script-compressed), "ama" (animal-ascii-uncompressed), "aml"
   * (animal-ascii-compressed), "tex", "txt", "pdf", ...
   * 
   * @return The Extension for the file that is generated by this Generator.
   */
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  /**
   * generate returns the generated String. This method is called by the
   * Generator GUI which passes the AnimationPropertiesContainer and the
   * Hashtable with Primitives with the values that the user changed.
   * 
   * @param props
   *          The AnimationPropertiesContainer with all the values set.
   * @param primitives
   *          The Hashtable with all the Primitives set.
   * @return The generated String.
   */
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    int[] dataArray = (int[]) primitives.get("array");
    int toSearch = ((Integer) primitives.get("searchValue")).intValue();
    FileHandler handler = new FileHandler("demo.asu");
    AnimalTranslator animalTrans = new AnimalTranslator(handler);
    boolean hideCode = ((Boolean) props.get("sourceCode", "hidden"))
        .booleanValue();
    InterpolationSearch interpolationSearch = new InterpolationSearch(
        animalTrans, !hideCode);
    interpolationSearch.initialize(dataArray, toSearch);
    try {
      interpolationSearch.generateAnimation();
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    return handler.getAnimationCode();
  }

  /**
   * getContentLocale returns the target Locale of the generated output Use e.g.
   * Locale.US for English content, Locale.GERMANY for German, etc.
   * 
   * @return a Locale instance that describes the content type of the output
   */
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public String getAnimationAuthor() {
    return "Michael Maur";
  }

  public String getAlgorithmName() {
    return "Interpolationssuche";
  }

  public void init() {
    // nothing to be done here
  }

}