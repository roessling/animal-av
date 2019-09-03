/**
 * 
 */
package generators.graph.pagerank;

import algoanim.primitives.Variables;

/**
 * @author marc
 *
 */
public interface TerminationInterface {
	boolean terminate(double[] oldWeights, double[] currentWeights);
	
	void declareParameters(Variables variables);
	
	String toString(double[] oldWeights, double[] currentWeights);
}
