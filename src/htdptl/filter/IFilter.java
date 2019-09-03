package htdptl.filter;

import htdptl.stepper.IStepObserver;
import htdptl.stepper.Stepper;

/**
 * The interface for filters that remove steps out of the trace. The filters are used while creating the trace. 
 *
 */
public interface IFilter extends IStepObserver {

  /**
   * returns true if the current step should be filtered 
   */
	boolean skip();

	void setStepper(Stepper stepper);

  IFilter clone();

  String getProcedure();

  int getTimes();

}
