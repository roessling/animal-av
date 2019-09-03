package extras.animalsense.ui;

import extras.animalsense.evaluate.Exercise;
import extras.lifecycle.common.Event;


public class ExerciseUpdated implements Event {

	/**
	 * 
	 */
//	private static final long serialVersionUID = -1313648159825244456L;
	
	private Exercise exercise;

	public ExerciseUpdated(Exercise exercise) {
		super();
		this.exercise = exercise;
	}

	/**
	 * @return the exercise
	 */
	public Exercise getExercise() {
		return exercise;
	}

	/**
	 * @param exercise the exercise to set
	 */
	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "[" + exercise + "]";
	}	

}
