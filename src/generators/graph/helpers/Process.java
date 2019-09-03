package generators.graph.helpers;

public class Process {

  private int    id;
  private Status alive;

  public Process(int id) {
    this.id = id;
    this.alive = Status.UNSET;
  }

  public Status getAlive() {
    return this.alive;
  }

  public void setAlive(Status s) {
    this.alive = s;
  }

  public int getId() {
    return id;
  }

}
