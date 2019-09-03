package generators.cryptography.helpers;
/**
 * an operation gets 2 arguments
 */
public interface Operation{
	
   /**
    * gets the priority of the given function
    * @return the priority
    */
   public int getPriority();
   
   /**
    * calculates the result
    * 
    * @param num1
    * 			first parameter
    * @param num2
    * 			second parameter
    * 
    * @return solution
    */
   public double calc(double num1, double num2);
}