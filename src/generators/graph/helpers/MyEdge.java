package generators.graph.helpers;

public class MyEdge {

  private MyNode node1;
  private MyNode node2;
  private int    weight;

  public MyNode getNode1() {
    return node1;
  }

  public void setNode1(MyNode node1) {
    this.node1 = node1;
  }

  public MyNode getNode2() {
    return node2;
  }

  public void setNode2(MyNode node2) {
    this.node2 = node2;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  public MyEdge(MyNode pNode1, MyNode pNode2, int pWeight) {

    node1 = pNode1;
    node2 = pNode2;
    weight = pWeight;
    weight = pWeight;
    weight = pWeight;

  }

  public MyEdge() {

  }

}