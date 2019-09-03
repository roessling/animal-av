package generators.misc.machineLearning;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import algoanim.primitives.StringMatrix;

public abstract class AbstractDistanceFunction<T> {
  
  /**
   * calculate the distance between two values
   * @param a value 1
   * @param b value 2
   * @return distance
   */
public abstract double calc(T a, T b);

/**
 * get the name of the distance function
 * @return name
 */
public abstract String getName();

/**
 * get a description/formula of the distance function
 * @return description
 */
public abstract String getDescription();

/**
 * get the formula filled with two values
 * @param a value 1
 * @param b value 2
 * @return formula
 */
public abstract String getFormulaWithValues(T a, T b); 

/**
 * round up to d decimal digits
 * @param v value
 * @param d number of digits
 * @return rounded value
 */
public double round(double v, int d) {
  return Math.round(v * Math.pow(10.0,d)) / Math.pow(10.0,d);
}

}
