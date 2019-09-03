package htdptl.filter;

import htdptl.stepper.StepEvent;
import htdptl.visitors.VisitorUtil;

/**
 * The breakpoint filter is used to shorten recursive procedures in the trace.
 * After a given amount of recursions only the procedure calls are left in the
 * trace and all intermediate steps are removed
 * 
 */
public class BreakpointFilter extends AbstractFilter {

  private int              seen   = 0;

  private static final int START  = 0;
  private static final int FILTER = 1;
  private int              state  = START;

  public BreakpointFilter(String procedure, int times) {
    super(procedure, times);
  }

  @Override
  public void stepPerformed(StepEvent stepEvent) {

    String operator = VisitorUtil.toCode(stepper.getRedex().getOperator());

    switch (state) {
      case START:
        if (operator.equals(procedure)) {
          seen++;

          if (seen >= times + 1) {
            state = FILTER;
            observe();
          }
        }

        break;
      case FILTER:
        if (operator.equals(procedure)) {
          observe();
        }
        else if (observer.isPrimitive()) {
          state = START;
        }
        break;
    }

  }

  @Override
  public boolean skip() {
    String operator = VisitorUtil.toCode(stepper.getRedex().getOperator());
    return state == FILTER && !operator.equals(procedure);
  }

  @Override
  public IFilter clone() {
    return new BreakpointFilter(procedure, times);
  } 
  

}
