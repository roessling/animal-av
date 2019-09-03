package generators.graphics.helpers;

import algoanim.counter.enumeration.PrimitiveEnum;
import algoanim.primitives.ArrayPrimitive;
import algoanim.primitives.generators.GeneratorInterface;
import algoanim.util.DisplayOptions;
import algoanim.util.Timing;

public class CountingArray<T> extends ArrayPrimitive {
  private T[][] data;

  public CountingArray(GeneratorInterface g, DisplayOptions d, T[][] data) {
    super(g, d);
    this.data = data;
  }

  public T get(int i, int j) {
    notifyObservers(PrimitiveEnum.getData);
    return data[i][j];
  }

  public T[] get(int i) {
    notifyObservers(PrimitiveEnum.getData);
    return data[i];
  }

  public int length() {
    return data.length;
  }

  @Override
  public void swap(int arg0, int arg1, Timing arg2, Timing arg3)
      throws IndexOutOfBoundsException {
    // nothing to be done here
  }
}
