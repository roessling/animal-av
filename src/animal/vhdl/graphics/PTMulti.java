package animal.vhdl.graphics;

import animal.graphics.PTPolygon;
import animal.graphics.PTText;

public abstract class PTMulti extends PTVHDLElement {

  protected PTText selText;

  public static int getControlPinAmount(int portAmount) {
    for (int i = 0;; i++) {
      if (Math.pow(2.0, i) >= portAmount)
        return i;
    }
  }

  public String toString(String elementType) {
    StringBuilder result = new StringBuilder(120);
    result.append(elementType).append(' ');

    if (getObjectName() != null)
      result.append("\"").append(getObjectName()).append("\" ");
    if (getInputPins() != null)
      result.append("in ").append(getInputPins().size()).append(" ");
    if (getOutputPins() != null)
      result.append("out ").append(getOutputPins().size()).append(" ");

    return result.toString();

  }

  protected void cloneCommonFeaturesInto(PTVHDLElement targetElement) {
    super.cloneCommonFeaturesInto(targetElement);
    targetElement.setElementBody((PTPolygon) getElementBody().clone());
  }

}
