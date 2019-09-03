package generators.misc.machineLearning;
import java.util.ArrayList;
import java.util.Locale;

import algoanim.primitives.StringMatrix;

public abstract class AbstractInstanceBasedGenerator extends AbstractMachineLearning{
  
  
  public AbstractInstanceBasedGenerator(String resourceName, Locale locale) {
    super(resourceName, locale);
  }

  /**
   * get all column indices for the numerical attributes
   * @param dataset all examples
   * @return list with indices
   */
  public ArrayList<Integer> getNumericalIndices(String[][] dataset) {
    
    ArrayList<Integer> indices = new ArrayList<Integer>();
    
     for(int j = 0; j < dataset[0].length-1; j++) {
       if(isNumericAttribute(dataset[dataset.length-1][j])) {
         indices.add(j);
       }
     }
     return indices;
   }
   
  /**
   * get all column indices for the nominal attributes
   * @param dataset all examples
   * @return list with indices
   */
   public ArrayList<Integer> getNominalIndices(String[][] dataset) {
     
     ArrayList<Integer> indices = new ArrayList<Integer>();
     
      for(int j = 0; j < dataset[0].length-1; j++) {
        if(!isNumericAttribute(dataset[dataset.length-1][j])) {
          indices.add(j);
        }
      }
      return indices;
    }
   
   
   /**
    * classify an example in StringMatrix with only one example
    * @param example examples that need to be classified
    * @param classification yes or no
    */
   public void classify(StringMatrix example, String classification) {
     example.put(1, example.getNrCols()-1, classification, null, null);
     example.highlightCell(1, example.getNrCols()-1, null, null);
   }
   
   
   /**
    * add a new column with the headline "Distance" and prepare the cells with an underline
    * @param dataset dataset without the distance column
    * @return dataset with the distance column
    */
   public String[][] addDistanceColumn(String[][] dataset){
     
     int height = dataset.length;
     int width = dataset[0].length;
     
     String[][] data = new String[height][width+1];
     
     data[0][width] = "Distance";
     for(int i = 0; i < height; i++) {
       for(int j = 0; j < width; j++) {
         data[i][j] = dataset[i][j];
       }
       if(i!=0)
       data[i][width] = "_______";
     }
     
     return data;
   }
   
}
