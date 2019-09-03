/*
 * Created on 18.06.2007 by Guido Roessling (roessling@acm.org>
 */
package generators.searching.binarysearching;

import generators.searching.helpers.AbstractStringSearchingAlgorithm;

import java.util.Locale;

public abstract class AbstractBinaryStringSearching extends
    AbstractStringSearchingAlgorithm {

  public AbstractBinaryStringSearching(String aResourceName, Locale aLocale) {
    super(aResourceName, aLocale);
  }

  public String getAlgorithmName() {
    return translator.translateMessage("algoName");
  }
}