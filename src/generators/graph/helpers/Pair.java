package generators.graph.helpers;

/**
 * Encapsulates the functionality of a pair of two values.
 * 
 * @author chollubetz
 * 
 * @param <T>
 *          the type of the first item
 * @param <U>
 *          the type of the second item
 */
public class Pair<T extends Comparable<T>, U extends Comparable<U>> implements
    Comparable<Pair<T, U>> {
  T first;
  U second;

  public Pair(T first, U second) {
    this.first = first;
    this.second = second;
  }

  /**
   * Returns the first element of the pair.
   * 
   * @return the first element
   */
  public T getFirst() {
    return first;
  }

  /**
   * Returns the second element of the pair.
   * 
   * @return the second element
   */
  public U getSecond() {
    return second;
  }

  @Override
  public int compareTo(Pair<T, U> o) {
    return first.compareTo(o.getFirst()) + 50 * second.compareTo(o.getSecond());
  }

}
