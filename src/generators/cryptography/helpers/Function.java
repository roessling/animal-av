package generators.cryptography.helpers;

/**
 * a function is a 1 relation
 */
public interface Function {

  /**
   * calculates the given function
   * 
   * @param value
   *          given value
   * 
   * @return solution
   */
  public double calc(double value);
}