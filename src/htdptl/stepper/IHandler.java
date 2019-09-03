package htdptl.stepper;

import htdptl.exceptions.StepException;

public interface IHandler {

	void step(IStepper stepper) throws StepException;

}
