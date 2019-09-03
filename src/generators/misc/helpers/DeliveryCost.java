package generators.misc.helpers;

public class DeliveryCost {

  private int cost;
  private int customer;
  private int warehouse;

  public DeliveryCost(int cost, int customer, int warehouse) {
    super();
    this.cost = cost;
    this.customer = customer;
    this.warehouse = warehouse;
  }

  public int getCost() {
    return cost;
  }

  public void setCost(int cost) {
    this.cost = cost;
  }

  public int getCustomer() {
    return customer;
  }

  public void setCustomer(int customer) {
    this.customer = customer;
  }

  public int getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(int warehouse) {
    this.warehouse = warehouse;
  }

}
