package generators.misc.machineLearning;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AssociationRule<T> {

  private LinkedList<T>             itemset;
  private LinkedHashSet<LinkedList<T>> left;
  private LinkedHashSet<LinkedList<T>> right;

  public AssociationRule() {
    this.itemset = new LinkedList<T>();
    this.left = new LinkedHashSet<LinkedList<T>>();
    this.right = new LinkedHashSet<LinkedList<T>>();
  }

  public AssociationRule(LinkedList<T> elements) {
    this.itemset = elements;
    this.left = new LinkedHashSet<LinkedList<T>>();
    this.right = new LinkedHashSet<LinkedList<T>>();
  }

  public void put(T element) {
    itemset.add(element);
  }

  public LinkedList<T> getItemset() {
    return itemset;
  }

  // https://stackoverflow.com/questions/1670862/obtaining-a-powerset-of-a-set-in-java/14818944#14818944
  public void createAssociationRules() {

    LinkedList<T> list = new LinkedList<T>(itemset);
    int n = list.size();

    LinkedList<LinkedList<T>> left = new LinkedList<LinkedList<T>>();
    LinkedList<LinkedList<T>> right = new LinkedList<LinkedList<T>>();

    for (int i = 0; i < (1 << n); i++) {
      LinkedList<T> leftElement = new LinkedList<T>();
      LinkedList<T> rightElement = new LinkedList<T>(itemset);
      for (int j = 0; j < n; j++) {
        if ((i >> j) % 2 == 1) {
          leftElement.add(list.get(j)); 
        }
        
      left.add(leftElement);
      
      for(int k = 0; k < rightElement.size(); k++) {
        if(leftElement.contains(rightElement.get(k))) {
          rightElement.remove(k);
        }
      }
      right.add(rightElement);
      }
      
      if(!leftElement.isEmpty() && !rightElement.isEmpty()) {
      this.left.add(leftElement);
      this.right.add(rightElement);
      }
    }
  }
 
  public String print() {
    return itemset.toString();
  }
 
  public LinkedHashSet<LinkedList<T>> getLeft(){
    return this.left;
  }
  
  public LinkedHashSet<LinkedList<T>> getRight(){
    return this.right;
  }

}
