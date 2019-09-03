/**
 * 
 */
package extras.animalsense.simulate;

/**
 * @author Mihail Mihaylov
 *
 */
public class SimulationException extends Exception {


  /**
   * 
   */
  private static final long serialVersionUID = 4230127963483261280L;

  /**
   * 
   */
  public SimulationException() {
    super();
  }

  /**
   * @param arg0
   * @param arg1
   */
  public SimulationException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  /**
   * @param arg0
   */
  SimulationException(String arg0) {
    super(arg0);
  }

  /**
   * @param arg0
   */
  public SimulationException(Throwable arg0) {
    super(arg0);
  }
  
  

}
