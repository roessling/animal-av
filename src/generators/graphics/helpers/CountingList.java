package generators.graphics.helpers;

import java.util.ArrayList;

import algoanim.counter.enumeration.PrimitiveEnum;
import algoanim.primitives.ArrayPrimitive;
import algoanim.primitives.generators.GeneratorInterface;
import algoanim.util.DisplayOptions;
import algoanim.util.Timing;

public class CountingList<T> extends ArrayPrimitive {
  private ArrayList<T> list = new ArrayList<T>();

  public CountingList(GeneratorInterface g, DisplayOptions d) {
    super(g, d);
  }

  public boolean contains(Object o) {
    for (int i = 0; i < list.size(); i++)
      if (this.get(i).equals(o))
        return true;

    return false;
  }

  public boolean add(T e) {
    notifyObservers(PrimitiveEnum.put);
    return list.add(e);
  }

  public boolean remove(Object o) {
    return list.remove(o);
  }

  public void clear() {
    list.clear();
  }

  public T remove(int index) {
    return list.remove(index);
  }

  public boolean isEmpty() {
    return list.isEmpty();
  }

  public T get(int i) {
    notifyObservers(PrimitiveEnum.getData);
    return list.get(i);
  }

  @Override
  public void swap(int arg0, int arg1, Timing arg2, Timing arg3)
      throws IndexOutOfBoundsException {
    // TODO Auto-generated method stub

  }

  @Override
  public String toString() {
    String s = "";
    for (T t : list)
      s += t + " ";
    return s;
  }
}
