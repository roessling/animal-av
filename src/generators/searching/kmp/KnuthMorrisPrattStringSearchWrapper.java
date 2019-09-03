package generators.searching.kmp;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;

import algorithm.animalTranslator.AnimalTranslator;
import algorithm.fileHandler.FileHandler;
import algorithm.stringSearch.KMPStringSearch;

/**
 * This class creates a wrapper for the generation of an Interpolated Search
 * animation. The actual animation generation is based on code by Michael Maur
 * <mmaur@web.de>.
 * 
 * @author Guido Roessling (roessling@acm.org>
 * @version 1.0 28.09.2005
 */
public class KnuthMorrisPrattStringSearchWrapper implements Generator {
  private static final String DESCRIPTION = "Im Unterschied zur direkten Brute-Force Suche eines Textes in einer "
                                              + "Zeichenfolge versucht der Algorithmus von Knuth, Morris und Pratt, "
                                              + "das Wissen über die bereits gelesenen Zeichen auszunutzen. Dazu "
                                              + "wird zunächst für jedes mögliche Eingabezeichen eine 'Verschiebetabelle' "
                                              + "berechnet. Diese gibt für jedes Eingabezeichen an, wie die Suchmaske "
                                              + "über den Text weiterzuschieben ist, wenn das gegebene Zeichen gefunden "
                                              + "wird.";

  private static final String SOURCE_CODE = "fehlt noch.";

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
    return "Knuth-Morris-Pratt-Suche in Strings";
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
    String fullText = (String) primitives.get("text");
    String toSearch = (String) primitives.get("searchFor");
    FileHandler handler = new FileHandler("demo.asu");
    AnimalTranslator animalTrans = new AnimalTranslator(handler);
    // boolean hideCode = ((Boolean)props.get("sourceCode",
    // "hidden")).booleanValue();
    KMPStringSearch kmpSearch = new KMPStringSearch(animalTrans);
    kmpSearch.initialize(fullText, toSearch);
    try {
      kmpSearch.generateAnimation();
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

  public String getAnimationAuthor() {
    return "Michael Maur";
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public String getAlgorithmName() {
    return "Knuth, Morris, Pratt (1977)";
  }

  public void init() {
    // nothing to be done here
  }

}