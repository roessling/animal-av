/*
 * Created on 18.06.2007 by Guido Roessling (roessling@acm.org>
 */
package generators.searching.straightselection;

import generators.searching.helpers.AbstractStringSearchingAlgorithm;

import java.util.Locale;

import algoanim.primitives.Text;

public abstract class AbstractStraightStringSelection extends
    AbstractStringSearchingAlgorithm {

  protected Text result    = null;

  protected Text valueText = null;

  public AbstractStraightStringSelection(String aResourceName, Locale aLocale) {
    super(aResourceName, aLocale);
  }

  public String getAlgorithmName() {
    return translator.translateMessage("algoName");
  }
}