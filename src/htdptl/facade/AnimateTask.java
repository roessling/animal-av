package htdptl.facade;

import htdptl.animation.Animator;
import htdptl.exceptions.StepException;
import htdptl.stepper.Stepper;
import htdptl.traces.TraceManager;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ProgressMonitor;


/**
 * Runnable that is used when processing multiple programs in parallel.
 * 
 */
public class AnimateTask implements Runnable {

  private List<Object>    expressions;
  private Stepper         stepper;
  private Animator        animator;
  private ProgressMonitor monitor;

  public AnimateTask(Animator animator, Stepper stepper,
      ArrayList<Object> expressions, ProgressMonitor monitor) {
    this.animator = animator;
    this.stepper = stepper;
    this.expressions = expressions;
    this.monitor = monitor;
  }

  
  @Override
  public void run() {
    for (int i = 0; i < expressions.size(); i++) {
      monitor.setProgress(i / 100);
      Object exp = expressions.get(i);
      try {
        stepper.setExpression(exp);
        TraceManager traceManager = new TraceManager(stepper);
        traceManager.buildTrace();
        animator.animate(traceManager, i);
      } catch (StepException e) {
        e.printStackTrace();
      }

    }

  }

}