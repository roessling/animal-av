package generators.misc.machineLearning;

public class ManhattanDistance extends AbstractDistanceFunction<Double>{

  public double calc(Double a, Double b) {
    return a+b;
  }

public double calc(double[] results) {
    
    double result = 0.0; 
    
    for(int i = 0; i < results.length; i++) {
      result += results[i];
    }
    return round(result,2);
  }
  
public String createCalculation(double[] results) {
  StringBuilder sb = new StringBuilder();
  sb.append(results[0]);
  
  for(int i = 1; i < results.length; i++) { 
    sb.append(" + ").append(results[i]);
  }
  
 return sb.toString();
}

  @Override
  public String getDescription() {
    return "manhattanDistanceDescription";
  }

  public String getFormulaWithValues(Double a, Double b) {
    return "a + b + ...";
  }

  @Override
  public String getName() {
    return "Manhattan Distance";
  }





  
}
