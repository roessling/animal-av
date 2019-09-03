/**
 * 
 */
package extras.animalsense.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import animal.gui.AnimalVisualizer;
import extras.animalsense.evaluate.Question;
import extras.animalsense.evaluate.QuestionEvaluator;
import extras.animalsense.evaluate.QuestionEvaluatorImpl;
import extras.animalsense.evaluate.Exercise;
import extras.animalsense.simulate.GeneratorEvaluationBean;
import extras.animalsense.ui.show.ExerciseTextPaneView;
import extras.animalsense.ui.show.SetUpAndVisualize;
import extras.animalsense.ui.show.SetUpVariablesEvent;
import extras.lifecycle.common.AnimationStepBean;
import extras.lifecycle.common.Event;
import extras.lifecycle.query.Result;

/**
 * @author Mihail Mihaylov
 * 
 */
public class ExerciseControllerImpl implements ExerciseController,
		ExerciseViewListener {
	Exercise exercise;
	ExerciseModel model;
	QuestionEvaluator questionEvaluator;

	/*
	 * (non-Javadoc)
	 * @see extras.animalsense.ui.ExerciseController#evaluate(extras.animalsense.evaluate.Exercise)
	 */
	@Override
	public void evaluate(Exercise e) {
		this.exercise = e;
		initialize();

		model.updateView();
	}

	private void initialize() {
		model = new ExerciseModelImpl();
		ExerciseView view = new ExerciseTextPaneView();
		questionEvaluator = new QuestionEvaluatorImpl();
		ExerciseView visualizer = new AnimalVisualizer();
		
		// Add the views to the model
		model.addListener(view);
		model.addListener(visualizer);

		// Listen to user inputs
		view.addListener(this);

		model.setExercise(exercise);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onEvent(Event event) {
		if (event instanceof SetUpAndVisualize) {
			SetUpAndVisualize suavEvent = (SetUpAndVisualize) event;

			// We set up the question and evaluate it
			GeneratorEvaluationBean geb = questionEvaluator
					.setUpEvaluateAndVisualize(exercise, suavEvent
							.getQuestion(), suavEvent.getVariables());

			// We inform our model
			model.setAnswer(suavEvent.getQuestion(), geb.getResult());

			List<AnimationStepBean> animationSteps = new ArrayList<AnimationStepBean>();
			if (geb.getResult().getData() instanceof Collection<?>) {
				animationSteps
						.addAll((Collection<? extends AnimationStepBean>) geb
								.getResult().getData());
			}

			String contents = geb.getScript();
			Question question = suavEvent.getQuestion();

			model.setVisualization(question, contents, animationSteps);
		} else if (event instanceof SetUpVariablesEvent) {
			SetUpVariablesEvent suvEvent = (SetUpVariablesEvent) event;

			// We set up the question and evaluate it
			Result result = questionEvaluator.setUpAndEvaluate(exercise,
					suvEvent.getQuestion(), suvEvent.getVariables());

			// We inform our model
			model.setAnswer(suvEvent.getQuestion(), result);
		}

	}


	
}
