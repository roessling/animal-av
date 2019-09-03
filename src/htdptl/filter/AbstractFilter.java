package htdptl.filter;

import htdptl.ast.AST;
import htdptl.stepper.Stepper;

/**
 * Abstract class for filters, that are defined with a procedure name and an amount "times".  
 *
 */
public abstract class AbstractFilter implements IFilter {

	protected Stepper stepper;
	protected String procedure;
  protected ExpressionObserver observer;
	protected AST parent;
	protected int index;
	
	protected int times;
	
	public AbstractFilter(String procedure, int times) {
		this.procedure = procedure;
		this.times = times;
	}

	public void setStepper(Stepper stepper) {
		this.stepper = stepper;
	}
	
	public String getProcedure() {
		return procedure;
	}

	public int getTimes() {
		return times;
	}

  @Override
  public abstract IFilter clone();

  protected void observe() {
    parent = stepper.getRedex().getParent();
    if (parent == null) {
      observer = new ExpressionObserver(stepper, stepper
          .newPrimitiveVisitor(), procedure);
    } else {
      index = childIndex(parent, stepper.getRedex());
      observer = new ExpressionObserver(parent, index, stepper
          .newPrimitiveVisitor(), procedure);
    }

  }

  private int childIndex(AST parent, AST child) {
    for (int i = 0; i < parent.numChildren(); i++) {
      if (parent.getChild(i).equals(child)) {
        return i;
      }
    }
    return -1;
  }
  
  public void setProcedure(String procedure) {
    this.procedure = procedure;
  }

  public void setTimes(int times) {
    this.times = times;
  }
  
}
