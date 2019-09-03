package generators.searching.helpers;

import java.util.ArrayList;
import java.util.List;

public class Message {

  public final static String ELECTION    = "ELECTION";
  public final static String COORDINATOR = "COORDINATOR";

  private String             type;
  private List<Integer>      numbers;

  public Message(String type) {
    this.type = type;
    numbers = new ArrayList<Integer>(10);
  }

  public List<Integer> getContent() {
    return numbers;
  }

  public String getType() {
    return type;
  }

  public void changeType(String newType) {
    type = newType;
  }

  public void addNumer(int number) {
    if (!numbers.contains(numbers))
      numbers.add(number);
  }

  public void clear() {
    numbers = new ArrayList<Integer>(10);
  }

  public int getHighestNumber() {
    int result = -1;
    for (Integer i : numbers)
      if (i > result)
        result = i;
    return result;
  }

  public boolean containsNumber(int number) {
    return numbers.contains(number);
  }

}
