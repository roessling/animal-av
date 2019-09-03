package extras.animalsense.ui;

import java.util.List;

import javax.swing.event.EventListenerList;

import extras.animalsense.evaluate.Exercise;
import extras.animalsense.evaluate.Question;
import extras.lifecycle.common.AbstractObservable;
import extras.lifecycle.common.AnimationStepBean;
import extras.lifecycle.query.Result;

public class ExerciseModelImpl extends AbstractObservable<ExerciseView> implements ExerciseModel {
	
	Exercise exercise;
	
	public ExerciseModelImpl() {
		super(ExerciseView.class);
	}

	@Override
	public void setExercise(Exercise e) {
		this.exercise = e;
		updateAllListeners();
	}
	
	  /** 
	   * Notifies all {@code AdListener}s that have registered interest for 
	   * notification on an {@code AdEvent}. 
	   * @param event  the {@code AdEvent} object 
	   * @see EventListenerList 
	   */ 
	  protected synchronized void updateAllListeners() 
	  { 
	    for ( ExerciseView l : listenerList.getListeners(ExerciseView.class) ) {
	    	l.setTitle(exercise.getTitle());
	    	l.setSubTitle(exercise.getSubTitle());
	    	l.setDescription(exercise.getDescription());
	    	l.setQuestionsList(exercise.getQuestionsList());
	    }
	    	
	  }

	@Override
	public synchronized void setAnswer(Question question, Result result) {
		  for ( ExerciseView l : listenerList.getListeners(ExerciseView.class) ) {
			  l.setAnswer(question, result.toString());
		  }
		
	}

	@Override
	public void updateView() {
		  for ( ExerciseView l : listenerList.getListeners(ExerciseView.class) ) {
			l.updateView();
		  }
		
	}

	@Override
	public void setVisualization(Question question, String contents,
			List<AnimationStepBean> animationSteps) {
		  for ( ExerciseView l : listenerList.getListeners(ExerciseView.class) ) {
				l.visualize(question, contents, animationSteps);
			  }
		
	}

}
