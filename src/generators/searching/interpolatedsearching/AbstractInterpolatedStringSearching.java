/*
 * Created on 18.06.2007 by Guido Roessling (roessling@acm.org>
 */
package generators.searching.interpolatedsearching;

import generators.framework.Generator;
import generators.searching.helpers.AbstractStringSearchingAlgorithm;

import java.util.Locale;

public class AbstractInterpolatedStringSearching extends
    AbstractStringSearchingAlgorithm implements Generator {
  
  public AbstractInterpolatedStringSearching(String aResourceName,
      Locale aLocale) {
    super(aResourceName, aLocale);
  }
  
  public String getAlgorithmName() {
    return translator.translateMessage("theName");
  }
}