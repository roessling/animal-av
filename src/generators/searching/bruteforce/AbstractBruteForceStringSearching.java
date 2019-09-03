/*
 * Created on 28.01.2009 by Bjoern Dasbach <bjoern.dasbach@gmx.de>
 */
package generators.searching.bruteforce;

import generators.searching.helpers.AbstractStringSearchingAlgorithm;

import java.util.Locale;

public abstract class AbstractBruteForceStringSearching extends
    AbstractStringSearchingAlgorithm {

  public AbstractBruteForceStringSearching(String aResourceName, Locale aLocale) {
    super(aResourceName, aLocale);
  }

  public String getAlgorithmName() {
    return "Lineare Suche";
  }

  public String getAnimationAuthor() {
    return "Björn Dasbach";
  }
}