/**
 * 
 */
package extras.animalsense.ui;

import java.util.List;

import extras.animalsense.evaluate.Exercise;
import extras.animalsense.evaluate.Question;
import extras.lifecycle.common.AnimationStepBean;
import extras.lifecycle.query.Result;


/**
 * @author Mihail Mihaylov
 *
 */
public interface ExerciseModel {
	
	public void setExercise(Exercise e);

	public void addListener(ExerciseView view);

	public void setAnswer(Question question, Result result);
	
	public void updateView();

	public void setVisualization(Question question, String contents, List<AnimationStepBean> animationSteps);

}
