package generators.misc.machineLearning;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

public abstract class Heuristic {

  /**
   * calculate the heuristic
   * @param a number of positives
   * @param b number of negatives
   * @return heuristic value
   */
  public abstract double calc(double a, double b);
  
  /**
   * get description/formula for the heuristic
   * @return description/forumula
   */
  public abstract String getDescription();
  
  /**
   * get the formula with two specific values
   * @param a number of positives
   * @param b number of negatives
   * @return formula
   */
  public abstract String getFormula(double a, double b);
  
  /**
   * round up to d decimal digits
   * @param v value
   * @param d number of digits
   * @return rounded value
   */
  public double round(double v, int d) {
    return Math.round(v * Math.pow(10.0,d)) / Math.pow(10.0,d);
  }
  
  /**
   * get the row of the conquered matrix with the highest heuristic
   * @param m conquered matrix
   * @return highest heuristic
   */
  public abstract String[] findHighestHeuristic(StringMatrix m);
  
   
}
