/*
 * Created on 20.06.2007 by Guido Roessling (roessling@acm.org>
 */
package generators.helpers;

import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntArray;
import algoanim.primitives.Primitive;
import algoanim.util.Offset;

public abstract class AnimatedIntArrayAlgorithm extends AnimatedAlgorithm {
  protected IntArray array = null;
  protected String   resourceName;
  protected Locale   locale;

  public Primitive installAdditionalComponents(String arrayKey, String codeKey,
      String codeName, int dx, int dy) {
    // install the array
    array = installIntArray(arrayKey);
    // install the source code (if any)
    code = installCodeBlock(codeKey, codeName, new Offset(dx, dy, array,
        AnimalScript.DIRECTION_SW));
    return array;
  }
}
