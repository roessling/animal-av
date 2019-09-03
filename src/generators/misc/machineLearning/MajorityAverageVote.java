package generators.misc.machineLearning;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.generators.Language;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Offset;

public class MajorityAverageVote {
  
  private Language lang; 
  private SourceCode sc;
  
  public MajorityAverageVote(Language l, SourceCode s) {
    lang = l;
    sc = s;
  }
  
  
  /**
   * count for each nominal attribute value  the frequency
   * @param filteredExamples  examples with the same class as the example with the missing value
   * @param values attribute values
   * @param indexToClassify column index
   * @param k select k nearest examples
   * @return frequencies for each attribute value
   */
  public int[] countSymbolicValues(StringMatrix filteredExamples, String[] values, int indexToClassify, int k) {
    int[] numOfValues = new int[values.length];
    int counter = 0;
        
    for(int i = 0; i < values.length; i++) {
       
      for(int j = 1; j <= k; j++) {
        if(filteredExamples.getElement(j, indexToClassify).equals(values[i])) {
          counter++;
        }        
      }
      
      numOfValues[i] = counter;
      counter = 0;
    }
    
    return numOfValues;
  }
  
  /**
   * classify nominal attribute value
   * @param m training data
   * @param filteredExamples examples with the same class as the example with the missing value
   * @param values all possible values of the attribute
   * @param indexToClassify column index
   * @param row row index of example with missing value
   * @param k select k nearest examples
   * @return explanation text
   */
  public String classifySymbolic(StringMatrix m, StringMatrix filteredExamples, String[] values, int indexToClassify, int row, int k) {
    
    int[] numOfValues = countSymbolicValues(filteredExamples, values, indexToClassify, k);
    
    int highestIndex = 0;
    int highestVal = Integer.MIN_VALUE;
    for(int i = 0; i < numOfValues.length; i++) {
      if(highestVal < numOfValues[i]) {
        highestVal = numOfValues[i];
        highestIndex = i;
      }
    }
    
    for(int i = 1; i <= k; i++) {
      filteredExamples.highlightCellColumnRange(i, 0, filteredExamples.getNrCols()-2, null, null);
      filteredExamples.highlightCell(i, filteredExamples.getNrCols()-1, null, null);
    }
    
    lang.nextStep();
    
    sc.toggleHighlight(3, 4);
    m.put(row, indexToClassify, values[highestIndex], null, null);
    return values[highestIndex];    
  }
  

  /**
   * classify numerical attribute value
   * @param m training data 
   * @param filteredExamples examples with the same class as the example with the missing value
   * @param indexToClassify column index
   * @param row row index of example with missing value
   * @param k select k nearest examples
   * @return explanation text
   */
  public String classifyNumeric(StringMatrix m, StringMatrix filteredExamples, int indexToClassify, int row, int k) {
    
    double result = 0.0;
    
    double previousVal = Double.MIN_VALUE;
    
    List<Double> results = new LinkedList<Double>();
    double tmp = 0.0;
        
      for(int i = 1; i <= k; i++) {
        
        tmp = Double.parseDouble(filteredExamples.getElement(i, indexToClassify));
        result += tmp;
        results.add(tmp); 
        previousVal = Double.parseDouble(filteredExamples.getElement(i, filteredExamples.getNrCols()-1));
        filteredExamples.highlightCellColumnRange(i, 0, filteredExamples.getNrCols()-2, null, null);
        filteredExamples.highlightCell(i, filteredExamples.getNrCols()-1, null, null);

      }

      lang.nextStep();

      //TODO: ROUND
      result = (result / k);
      
      StringBuilder sb = new StringBuilder(); 
      
      for(int i = 0; i < results.size(); i++) {
        if(i == 0) {
          sb.append("(" + results.get(0)).append(" + ");
        }else if(i == results.size() -1) {
        sb.append(results.get(i)).append(")").append(" / ").append(k).append(" = ").append(result);
        } else  {
         sb.append(results.get(i)).append(" + ");
        }
      }
      
      
      sc.toggleHighlight(3, 4);
      m.put(row, indexToClassify, String.valueOf(round(result,2)), null, null);
      return sb.toString();
  }
  
  public double round(double v, int d) {
    return Math.round(v * Math.pow(10.0,d)) / Math.pow(10.0,d);
  }

}