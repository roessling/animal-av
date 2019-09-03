package animal.gui;

import java.util.List;

import javax.swing.JOptionPane;

import animal.exchange.AnimationImporter;
import animal.main.Animal;
import animal.main.Animation;
import animal.main.Link;
import extras.animalsense.evaluate.Question;
import extras.animalsense.ui.ExerciseView;
import extras.animalsense.ui.ExerciseViewListener;
import extras.lifecycle.common.AnimationStepBean;
import extras.lifecycle.common.Event;

public class AnimalVisualizer implements ExerciseView {

	private static final String LINKLABELPREFIX = ">>";

	public void visualize(Question question, String animalScriptContent, List<AnimationStepBean> animationSteps) {
//	System.err.println("Animate this: " + animationSteps);
		// Get current Animal instance and set the script
		
		
		if (Animal.getFileChooser() == null) {
			String msg = "Animal is not started!";
			JOptionPane.showMessageDialog(null, msg, "Question editor",	JOptionPane.ERROR_MESSAGE);
			return;
		}
			
		// Import script
		String format = "animation/animalscript";
		AnimationImporter animationImporter = null;
		animationImporter = AnimationImporter.getImporterFor(format);
		Animation animation = null;
		String animContent = animalScriptContent;
		if (animationImporter != null) {
			animation = animationImporter.importAnimationFrom(null, animContent);
		}
		
		// Get current Animal instance and set the script
		Animal myAnimalInstance = Animal.get();
		if (animation != null) {
			myAnimalInstance.setAnimation(animation);
			myAnimalInstance.setAnimalScriptCode(animContent);
		}
		
		for (AnimationStepBean asb : animationSteps) {
			Link link = animation.getLink(asb.getStep());
			link.setLinkLabel(LINKLABELPREFIX + asb.getLabel());
		}
		
		// Update TimeLineWindow
		AnimalMainWindow.getWindowCoordinator().getTimeLineWindow(true).updateList(animation);
		
		// Force TimeLine Window to show
		AnimalMainWindow.getWindowCoordinator().showTimeLineWindow();
	}

	@Override
	public void addListener(ExerciseViewListener evl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAnswer(Question question, String answer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDescription(String description) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setQuestionsList(List<Question> questionsList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSubTitle(String subTitle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTitle(String title) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEvent(Event event) {
		// TODO Auto-generated method stub
		
	}


}
