/**
 * 
 */
package extras.animalsense.ui;

import java.util.List;

import extras.animalsense.evaluate.Question;
import extras.lifecycle.common.AbstractListener;
import extras.lifecycle.common.AnimationStepBean;

/**
 * @author Mihail Mihaylov
 *
 */
public interface ExerciseView extends AbstractListener {
	
	public void updateView();
	
	public void hide();
	
	public void addListener(ExerciseViewListener evl);

	public abstract void setTitle(String title);

	public abstract void setSubTitle(String subTitle);

	public abstract void setDescription(String description);

	public abstract void setQuestionsList(List<Question> questionsList);
	
	public abstract void setAnswer(Question question, String answer);
	
	public abstract void visualize(Question question, String scriptContent, List<AnimationStepBean> animationSteps);

}
