package generators.graph.helpers;

import java.util.ArrayList;

public class BullyAlgo {

  ArrayList<Process> pool;
  ArrayList<Process> newElec;
  Process            coordinator;

  /*
   * public static void main(String[] args) {
   * 
   * BullyAlgo b = new BullyAlgo(); b.initBullyAlgo(6);
   * 
   * System.out.println("Prozess " + b.coordinator.getId() +
   * " wurde als Koordinator initialisiert!");
   * 
   * String inPool = ""; for (Process p : b.pool) { inPool += p.getId() + " | ";
   * } System.out.println("Pool: { " + inPool.substring(0, inPool.length() - 3)
   * + " }"); b.killCoordinator();
   * 
   * Process theSmartOne = b.someOneRealizeTheCoordinatorisAway();
   * 
   * System.out.println("Prozess " + theSmartOne.getId() +
   * " hat gemerkt das der Koordinator nicht mehr vorhanden ist!");
   * 
   * 
   * 
   * b.newElection(theSmartOne);
   * 
   * }
   */
  // private void newElection(Process theSmartOne) {
  //
  // System.out.println("Wahl wurde von " + theSmartOne.getId() +
  // " gestartet!");
  //
  // this.newElec = new ArrayList<Process>();
  //
  // for (Process p : this.pool) {
  //
  // if (p.getId() > theSmartOne.getId()) {
  //
  // // Simulation ob alle Prozesse antworten ...
  // if (p.getAlive() == Status.UNSET) {
  // Random gen = new Random();
  // int ran = gen.nextInt(2);
  // if (ran == 0) {
  // p.setAlive(Status.DEAD);
  // } else {
  // p.setAlive(Status.ALIVE);
  // }
  // }
  //
  // if (p.getAlive() == Status.DEAD) {
  // System.out.println("Prozess " + p.getId() + " antwortet nicht!");
  // }
  //
  // if (p.getAlive() == Status.ALIVE) {
  // System.out.println("Neue Wahl starten mit " + p.getId() + "!");
  // this.newElec.add(p);
  // }
  //
  // }
  // }
  // if (this.newElec.size() == 0) {
  // System.out.println(theSmartOne.getId()
  // + " ist der neue Koordinator -> Termination");
  // } else {
  // this.newElection(this.newElec.get(0));
  // }
  // }
  //
  // private Process someOneRealizeTheCoordinatorisAway() {
  //
  // Random gen = new Random();
  // return this.pool.get(gen.nextInt(this.pool.size()));
  //
  // }
  //
  // private void killCoordinator() {
  // pool.remove(this.coordinator);
  //
  // }

  public void initBullyAlgo(int numberOfProcesses) {

    pool = new ArrayList<Process>();

    for (int i = 1; i <= numberOfProcesses; i++) {
      pool.add(new Process(i));
    }

    this.coordinator = pool.get(numberOfProcesses - 1);

  }

}
