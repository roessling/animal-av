/*
 * Created on 18.06.2007 by Guido Roessling (roessling@acm.org>
 */
package generators.searching.helpers;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.helpers.AnimatedStringArrayAlgorithm;

import java.util.Hashtable;
import java.util.Locale;

import translator.Translator;
import algoanim.primitives.Text;
import algoanim.properties.AnimationProperties;

public abstract class AbstractStringSearchingAlgorithm extends
    AnimatedStringArrayAlgorithm implements Generator {

  protected Text result    = null;

  protected Text valueText = null;

  public AbstractStringSearchingAlgorithm(String aResourceName, Locale aLocale) {
    resourceName = aResourceName;
    locale = aLocale;
    init();
  }

  public void init() {
    translator = new Translator(resourceName, locale);
    primitiveProps = new Hashtable<String, AnimationProperties>(59);
    localType = new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
    contentLocale = locale;
  }

  /**
   * hides the array, code, and number of steps taken from the display
   */
  protected void hideNrStepsArrayCode() {
    super.hideNrStepsArrayCode();
    if (result != null)
      result.hide();
    if (valueText != null)
      valueText.hide();
    if (array != null)
      array.hide();
  }

  /**
   * getContentLocale returns the target Locale of the generated output Use e.g.
   * Locale.US for English content, Locale.GERMANY for German, etc.
   * 
   * @return a Locale instance that describes the content type of the output
   */
  public Locale getContentLocale() {
    return locale;
  }

  public String getAnimationAuthor() {
    return "Guido Rößling";
  }

}