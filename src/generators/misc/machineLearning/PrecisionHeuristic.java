package generators.misc.machineLearning;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

public class PrecisionHeuristic extends Heuristic{

  @Override
  public double calc(double a, double b) {
    return a / (a+b);
  }

  public String[] findHighestHeuristic(StringMatrix m) {
    double current = 0.0; 
    double highest = Double.NEGATIVE_INFINITY; 
    int highestHeuristicIndex = 0;

    for(int i = 1; i < m.getNrRows(); i++) {
      current = Double.parseDouble(m.getElement(i,4));
     
      if(current > highest) {
        highest = current;
        highestHeuristicIndex = i; 
      }
      
      if(current == highest) {
        if(Double.parseDouble(m.getElement(i,2)) > Double.parseDouble(m.getElement(highestHeuristicIndex,2))) {
          highestHeuristicIndex = i; 
          highest = current;
        }
      }
    }
    
    m.highlightCellColumnRange(highestHeuristicIndex, 0, m.getNrCols()-2, null, null);
    m.highlightCell(highestHeuristicIndex, m.getNrCols()-1, null, null);
    
    return new String[] {m.getElement(highestHeuristicIndex,0), 
        m.getElement(highestHeuristicIndex,1), 
        m.getElement(highestHeuristicIndex,2),
        m.getElement(highestHeuristicIndex,3),
        m.getElement(highestHeuristicIndex,4)};
  }

  @Override
  public String getDescription() {
    return "precisionFormula";
  }
  
  @Override
  public String getFormula(double a, double b) {
   StringBuilder sb = new StringBuilder(); 
   sb.append(a);
   sb.append(" / ");
   sb.append("( ");
   sb.append(a);
   sb.append(" + ");
   sb.append(b);
   sb.append(" )");
   sb.append(" = ");
   sb.append(round(calc(a,b),2));
   return sb.toString();
  }


}
