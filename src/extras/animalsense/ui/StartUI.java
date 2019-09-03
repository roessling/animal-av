package extras.animalsense.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import extras.animalsense.evaluate.Exercise;
import extras.animalsense.serialize.Serializer;
import extras.animalsense.serialize.SerializerException;
import extras.animalsense.serialize.SerializerImpl;
import extras.animalsense.ui.edit.EditExerciseJFrame;
import generators.generatorframe.loading.GeneratorLoader;

public class StartUI extends StartUIBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2551423050254209376L;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seeextras.animalsense.utils.StartUIBase#actionPerformed(java.awt.event.
	 * ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if ("create".equals(e.getActionCommand()))
			create();
		else if ("edit".equals(e.getActionCommand()))
			edit(this);
		else if ("do".equals(e.getActionCommand()))
			doExercise(this);
		else if ("close".equals(e.getActionCommand()))
			close();
	}

	public static void doExercise(Component parent) {
		ExerciseChooser ec = new ExerciseChooser(parent);
		if (ec.open()) {		
			String fileName = ec.getFileName();
			showExercise(fileName, parent);
		}
		
	}

	public static void edit(Component parent) {
		ExerciseChooser ec = new ExerciseChooser(parent);
		if (ec.open()) {		
			String fileName = ec.getFileName();
			editExercise(fileName, parent);
		}
	}

	private void close() {
		this.setVisible(false);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				dispose();
			}
		});
		
	}
	
  static void createAndShowGUI() {
		StartUI animalS = new StartUI();
		animalS.setVisible(true);
		new GeneratorLoader();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Schedule a job for the event dispatching thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				StartUI.createAndShowGUI();
			}
		});
	}
	
	public static void create() {
		Exercise exercise = new Exercise();
		exercise.generateDefault();
		ExerciseController controller = new EditExerciseJFrame();
		// e.g. edit exercise
		controller.evaluate(exercise);
	}
	
	private static void editExercise(String fileName, Component parent) {
		Serializer serializer = new SerializerImpl();
		Exercise exercise = null;
		try {
			exercise = serializer.deserializeExercise(fileName);
		} catch (SerializerException e) {
			e.printStackTrace();
			showErrorMsg("Unable to open " + fileName, parent);
		}	
		EditExerciseJFrame controller = new EditExerciseJFrame();
		controller.setFileName(fileName);
		// e.g. edit exercise
		controller.evaluate(exercise);
	}
	
	/**
	 * Loads an exercise and shows it for solving.
	 * @param fileName of the exercise
	 * @param parent 
	 */
	public static void showExercise(String fileName, Component parent ) {
		Serializer serializer = new SerializerImpl();
		Exercise exercise = null;
		try {
			exercise = serializer.deserializeExercise(fileName);
		} catch (SerializerException e) {
			e.printStackTrace();
			showErrorMsg("Unable to open " + fileName, parent);
		}			
		
		ExerciseController controller = new ExerciseControllerImpl();
		controller.evaluate(exercise);
	}
	
	public static Exercise getExerciseInfos(String fileName){
		Serializer serializer = new SerializerImpl();
		Exercise exercise = null;
		try {
			exercise = serializer.deserializeExercise(fileName);
		} catch (SerializerException e) {
			e.printStackTrace();
		}
		return exercise;
	}
	

	private static void showErrorMsg(String msg, Component parent) {
		JOptionPane.showMessageDialog(parent, msg, "Question editor",
				JOptionPane.ERROR_MESSAGE);
	}

}
