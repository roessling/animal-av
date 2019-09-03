package generators.misc.machineLearning;
import algoanim.primitives.StringMatrix;

public class QuickSortStringMatrix {
  
  private StringMatrix dataSet; 
  private int num;
  
  QuickSortStringMatrix(StringMatrix d){
    dataSet = d;
  }
  
  public void sort (StringMatrix d) {
    if(d == null || d.getNrRows() == 0) {
      return;
    }
    
    dataSet = d; 
    num = dataSet.getNrRows();
    quickSort(1, num - 1); 
  }
  
  public void quickSort(int low, int high) {
    int i = low;
    int j = high; 
    
    double pivot = Double.parseDouble(dataSet.getElement(low + (high - low)/2, dataSet.getNrCols()-1));
    
    
    while(i <= j) {
      
      while(Double.parseDouble(dataSet.getElement(i, dataSet.getNrCols()-1)) < pivot){
        i++;
      }
      
      while(Double.parseDouble(dataSet.getElement(j, dataSet.getNrCols()-1)) > pivot) {
        j--;
      }
      
      
      if(i <= j ) {
        for(int m = 0; m < dataSet.getNrCols(); m++) {
        dataSet.swap(i, m, j, m, null, null);
        }
        i++;
        j--;
      }
      
    }
    
    if(low < j) {
      quickSort(low, j);
    }
    
    if(i < high) {
      quickSort(i, high);
    }
  }
}
