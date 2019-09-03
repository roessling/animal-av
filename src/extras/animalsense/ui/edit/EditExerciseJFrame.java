package extras.animalsense.ui.edit;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import extras.animalsense.evaluate.Exercise;
import extras.animalsense.serialize.Serializer;
import extras.animalsense.serialize.SerializerException;
import extras.animalsense.serialize.SerializerImpl;
import extras.animalsense.ui.ExerciseChooser;
import extras.animalsense.ui.ExerciseController;
import generators.generatorframe.store.GetInfos;


public class EditExerciseJFrame extends EditExerciseJFrameBase implements ExerciseController, ActionListener, ChangeListener {

	private Exercise exercise;
	private int lastTabbedPaneIndex = -1;
	private String fileName;
	private String saveAsfileName;
	private GetInfos loader;

	/**
	 * 
	 */
	private static final long serialVersionUID = 8250957535532220814L;

	/**
	 * @param exercise
	 * @throws HeadlessException
	 */
	public EditExerciseJFrame(Exercise exercise) throws HeadlessException {
		super();
		this.exercise = exercise;
		getTabbedPane().addChangeListener(this);
		// We force stateChanged
		stateChanged(null);
		loader = GetInfos.getInstance();
	}

	/**
	 * @throws HeadlessException
	 */
	public EditExerciseJFrame() throws HeadlessException {
		this(null);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("close".equals(e.getActionCommand()))
			close();
		else if ("save".equals(e.getActionCommand()))
			save();
		else if ("back".equals(e.getActionCommand()))
			back();
		else if ("nextAlg".equals(e.getActionCommand()))
			next();
		else if ("nextVariables".equals(e.getActionCommand()))
			next();
		else if ("nextDscr".equals(e.getActionCommand()))
			next();
		else if ("nextQst".equals(e.getActionCommand()))
			next();
		else if ("nextFn".equals(e.getActionCommand()))
			next();
		else if ("browse".equals(e.getActionCommand()))
			browse();
		else if ("saveas".equals(e.getActionCommand()))
			saveas();
		else if ("defaultfile".equals(e.getActionCommand()))
			defaultfile();

	}
	
	private void saveas() {
		if (this.saveAsfileName == null || this.saveAsfileName.isEmpty())
			browse();
		
	}

	private void defaultfile() {
//		this.saveAsfileName = "";
//		getSaveLabel().setText(this.saveAsfileName);
	}

	private void browse() {
		ExerciseChooser ec = new ExerciseChooser(this);
		if (ec.save()) {		
			String fileName = ec.getFileName();
			this.saveAsfileName = fileName;
			getSaveLabel().setText(this.saveAsfileName);
			getRadioBtnSaveAs().setSelected(true);
		}
		
	}

	private void algLeave() throws IncorrectDataException {
	    
//		if (getGeneratorsMainPanel().getGenerator() == null)
//			throw new IncorrectDataException("Please, first select a generator.");
	    exercise.setGeneratorName(loader.getGeneratorName());
	    StringBuilder sb = new StringBuilder(256);
//	    sb.append('/').append(loader.getLanguage()).append('/');
//	    sb.append(loader.getCodeLanguage()).append('/');
//	    sb.append(loader.getCategory()).append('/');
//	    sb.append(loader.getName()).append('/');
//	    sb.append(loader.getGeneratorName().replaceAll(" ", "_"));
	    sb.append('/');
	    sb.append(loader.getCategory().split(": ", 2)[1]).append('/');
	    sb.append(loader.getName().split(": ", 2)[1]).append('/');
	    sb.append(loader.getCodeLanguage().split(": ", 2)[1]).append('/');
	    sb.append(loader.getLanguage().split(": ", 2)[1]).append('/');
	    sb.append(loader.getGeneratorName().replaceAll(" ", "_"));
	    exercise.setChainPath(sb.toString());
//		exercise.setGeneratorName(getGeneratorsMainPanel().getGenerator().getClass().getName());
//		exercise.setChainPath(getGeneratorsMainPanel().getChainPath());
	}

	private void varsEnter() {
		getVariablesTable().setVariables(exercise.getInitialVariables());
		getVariablesTable().update();
	}

	private void qstEnter() {
		getQuestionsTable().setExercise(exercise);
		getQuestionsTable().setFrame(this);
		getQuestionsTable().update();
	}

	/**
	 * @throws IncorrectDataException 
	 * 
	 */
	private void descrLeave() throws IncorrectDataException {
		// TODO Make same checks if at least Title and Description are given
		if (getTitleTextArea().getText().isEmpty())
			throw new IncorrectDataException("Please provide a title for the exercise.");
		
		exercise.setTitle(getTitleTextArea().getText());
		exercise.setSubTitle(getSubTitleTextArea().getText());
		exercise.setDescription(getDescriptionTextArea().getText());
	}

	private void dscrEnter() {

		if (getTitleTextArea().getText().isEmpty())
			getTitleTextArea().setText(exercise.getTitle());

		if (getSubTitleTextArea().getText().isEmpty())
			getSubTitleTextArea().setText(exercise.getSubTitle());

		if (getDescriptionTextArea().getText().isEmpty())
			getDescriptionTextArea().setText(exercise.getDescription());

	}

	/**
	 * 
	 */
	private void varsLeave() {
		exercise.setInitialVariables(getVariablesTable().getVariables());
	}

	private void algEnter() {
		
		// Select the current generator
		selectCurrentGenerator();
	}


	private void selectCurrentGenerator() {
		if (exercise.getChainPath() == null)
			return;
//		getGeneratorsMainPanel().setChainPath(exercise.getChainPath());
//		if (getGeneratorsMainPanel().getGenerator() == null) {
//			showErrorMsg("Unable to find the specified generator: "
//					+ exercise.getChainPath());

			// If we fail to set the generator by its chainpath, we try using
			// the generator name
	//		getGeneratorsMainPanel().setGeneratorByName(
	//				exercise.getGeneratorName());
	//	}

	}
	
	private void showErrorMsg(String msg) {
		JOptionPane.showMessageDialog(this, msg, "Question editor",
				JOptionPane.ERROR_MESSAGE);
	}

	private void back() {
		int currentSelIndex = getTabbedPane().getSelectedIndex();
		int index = currentSelIndex - 1;
		getTabbedPane().setSelectedIndex(index);
	}

	private void next() {
		int currentSelIndex = getTabbedPane().getSelectedIndex();
		int index = currentSelIndex + 1;
		getTabbedPane().setSelectedIndex(index);
	}
	
	private void finishEnter() {
		try {
			getFinishPane().setContentType("text/html");
			getFinishPane().setPage(this.getClass().getResource("questionEditorFinish.html"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		boolean fileNameOK = (this.fileName != null) && (!this.fileName.isEmpty());
		if (fileNameOK) {
			getRadioBtnSave().setEnabled(true);
			getRadioBtnSave().setSelected(true);
		} else  {
			getRadioBtnSave().setEnabled(false);
			getRadioBtnSaveAs().setSelected(true);
			
		}
	}

	private void save() {
		String fileName;
		if (getRadioBtnSaveAs().isSelected())
			fileName = this.saveAsfileName;
		else
			fileName = this.fileName;
		
		if (fileName == null || fileName.isEmpty()) {
			showErrorMsg("Please select a name for the file.");
			return;
		}
		
		//fileName = fileName.replaceAll(" ", ""); // Remove spaces
		if (!(fileName.endsWith(".XML") || fileName.endsWith(".xml")))
			fileName = fileName + ".xml";
		Serializer serializer = new SerializerImpl();
		boolean ok = false;
		try {
			serializer.serializeExercise(exercise, fileName);
			ok = true;
		} catch (SerializerException e) {
			e.printStackTrace();
			showErrorMsg("Unable to save as " + fileName + "!");
		}
		
		if (ok) {
			JOptionPane.showMessageDialog(this,
					"Exercise saved as " + fileName, "Question editor",
					JOptionPane.INFORMATION_MESSAGE);
			close();
		}

	}

	private void close() {
		setVisible(false);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				dispose();
			}
		});
	}

	/**
	 * @return the exercise
	 */
	public Exercise getExercise() {
		return exercise;
	}

	/**
	 * @param exercise
	 *            the exercise to set
	 */
	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
	}

	@Override
	public void evaluate(Exercise e) {
		setExercise(e);
		// Schedule a job for the event dispatching thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				setVisible(true);
			}
		});

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		//JTabbedPane tabSource = (JTabbedPane) e.getSource();
		
		// If the user changes to the last tab pane ignore the events
		int i = getTabbedPane().getSelectedIndex();
		if (i == lastTabbedPaneIndex)
			return;
		
		try {
			// Simulate leave and enter
			onTabLeave(lastTabbedPaneIndex);
			onTabEnter(i);
			this.lastTabbedPaneIndex = i;
		} catch (IncorrectDataException e1) {
			// If an error occurs go back to the last valid tab
			getTabbedPane().setSelectedIndex(lastTabbedPaneIndex);
			showErrorMsg(e1.getMessage());
		}
	}
	
	private void onTabEnter(int index) throws IncorrectDataException{
		
		switch (index) {
		case 0:
			// Welcome Tab
			welcomeEnter();
			
			break;
		case 1:
			// Algorithm Tab
			algEnter();
			
			break;
		case 2:
			// Variables Tab
			varsEnter();
			
			break;
		case 3:
			// Description Tab
			dscrEnter();
			
			break;
		case 4:
			// Question Tab
			qstEnter();
			
			break;
			
		case 5:
			// Question Tab
			finishEnter();
			
			break;

		default:
			break;
		}
	}



	/**
	 * 
	 */
	private void welcomeEnter() {
		try {
			getWelcomePane().setContentType("text/html");
			getWelcomePane().setPage(this.getClass().getResource("questionEditorWelcome.html"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void welcomeLeave() {
		
		if (exercise == null) {
			exercise = new Exercise();
			exercise.generateDefault();
		}
	}
	

	
	private void onTabLeave(int index) throws IncorrectDataException {
		switch (index) {
		case 0:
			// Welcome Tab
			welcomeLeave();
			
			break;
		
		
		case 1:
			// Algorithm Tab
			algLeave();
			
			break;
		case 2:
			// Variables Tab
			varsLeave();
			
			break;
		case 3:
			// Description Tab
			descrLeave();
			
			break;

		default:
			break;
		}
		
		
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	

}
