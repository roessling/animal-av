package htdptl.filter;

import htdptl.stepper.StepEvent;
import htdptl.stepper.Stepper;
import htdptl.visitors.VisitorUtil;

/**
 * The procedure filter is used to remove evaluations of the given procedures,
 * after the given times of evaluations have been observed in the trace. E.g. a
 * ProcedureFilter with procedure=map and times=1 will leave only one evaluation
 * of map in the trace and remove all evaluations that occur afterwards.
 * 
 * 
 */
public class ProcedureFilter extends AbstractFilter {

  private static final int START   = 0;
  private static final int FILTER  = 1;
  private static final int OBSERVE = 2;

  private int              state   = START;

  public ProcedureFilter(String procedure, int times) {
    super(procedure, times);
  }

  public void stepPerformed(StepEvent stepEvent) {

    switch (state) {
      case START:
        start();
        break;
      case OBSERVE:
        if (observer.isPrimitive()) {
          times--;
          observer = null;
          state = START;
        }
        break;
      case FILTER:
        if (observer.isPrimitive()) {
          state = START;
          start();
        }
        break;

    }
  }

  private void start() {
    String operator = VisitorUtil.toCode(stepper.getRedex().getOperator());
    if (operator.equals(procedure)) {
      observe();
      if (times > 0) {
        state = OBSERVE;
      }
      if (times == 0) {
        state = FILTER;
      }
    }
  }

  @Override
  public void setStepper(Stepper stepper) {
    super.setStepper(stepper);
  }

  public boolean skip() {
    String operator = VisitorUtil.toCode(stepper.getRedex().getOperator());
    return state == FILTER && !operator.equals(procedure);
  }

  @Override
  public IFilter clone() {
    return new ProcedureFilter(procedure, times);
  }

}
