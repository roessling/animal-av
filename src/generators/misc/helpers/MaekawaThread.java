package generators.misc.helpers;

import generators.misc.MaekawaAlgorithm;

import java.util.Stack;

public class MaekawaThread implements Runnable {

  private MaekawaAlgorithm algo;
  private Stack<Integer>   votingSetMember;
  private long             delay;
  private Integer          nAccess;

  /**
   * Creates an instance with given main process and a delay
   * 
   * @param algo
   *          the main process
   * @param delay
   *          the delay for simulation
   */
  public MaekawaThread(MaekawaAlgorithm algo, long delay, Integer nAccess,
      Stack<Integer> votingSetMember) {
    this.algo = algo;
    this.votingSetMember = votingSetMember;
    this.delay = delay;
    this.nAccess = nAccess;
  }

  @Override
  public void run() {

    for (int i = 0; i < nAccess; i++) {
      // request access to the critical section
      algo.request(Integer.valueOf(Thread.currentThread().getName()),
          this.votingSetMember);

      // wait in the critical section to simulate the process (thread) does
      // anything there
      try {
        Thread.sleep(this.delay);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      // leave the critical section
      algo.release(Integer.valueOf(Thread.currentThread().getName()),
          this.votingSetMember);
    }

    // tell the main process this process (thread) finished his work
    algo.setFinished(algo.getFinished() + 1);
  }

}
