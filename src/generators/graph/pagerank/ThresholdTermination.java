/**
 * 
 */
package generators.graph.pagerank;

import algoanim.primitives.Variables;

/**
 * @author marc
 *
 */
public class ThresholdTermination implements TerminationInterface {
	private double threshold;
	
	public ThresholdTermination(double threshold) {
		this.threshold = threshold;
	}
	
	@Override
	public boolean terminate(double[] oldWeights, double[] currentWeights) {
		double sum = 0;
		for (int index = 0; index < oldWeights.length; index++) {
			sum += Math.abs(oldWeights[index] - currentWeights[index]);
		}
		
		return sum <= threshold; 
	}

	@Override
	public String toString(double[] oldWeights, double[] currentWeights) {
		StringBuilder sb = new StringBuilder();
		sb.append("Terminiere? ");
		double sum = 0;
		for (int index = 0; index < oldWeights.length; index++) {
			if (index > 0)
				sb.append(" + ");
			sb.append(String.format("|%.3g - %.3g|", oldWeights[index], currentWeights[index]));
			sum += Math.abs(oldWeights[index] - currentWeights[index]);
		}
		sb.append(" = ");
		sb.append(String.format("%.3g", sum));
		sb.append(" <= ");
		sb.append(String.format("%.3g", threshold));
		sb.append(" ==> ");
		sb.append(sum <= threshold);
		
		return sb.toString();   
	}

	@Override
	public void declareParameters(Variables variables) {
		variables.declare("double", "TerminationThreshold", String.valueOf(threshold));
	}

}
