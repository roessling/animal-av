package animal.editor;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import translator.AnimalTranslator;
import translator.ResourceLocator;
import animal.main.Animal;
import animal.main.AnimalConfiguration;
import animal.main.AnimalFrame;
import animal.misc.AnimalFileChooser;
import animal.misc.MessageDisplay;
import animal.misc.XProperties;

public class AnnotationEditor extends AnimalFrame implements ActionListener {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -8325459076725440472L;

	private String baseAnnotateURL = "http://www.animal.ahrgr.de/Anims/annotation.php";

	private int currentStep = 1;

	private String currentUser = null;

	private AnimalConfiguration animalConfig = null;

	private AnimalFileChooser fileChooser = null;

	private JTextArea foreignComments = null;

	private boolean hasChanged = false;

	private AbstractButton mergeButton = null;

	private XProperties props = null;

	private AbstractButton reloadButton = null;

	private AbstractButton saveAsButton = null;

	private AbstractButton undoButton = null;

	private JTextArea userComments = null;

	public AnnotationEditor(Animal animalInstance, XProperties properties,
			String userID, AnimalConfiguration animalConfiguration) {
		super(animalInstance, properties);
		setCurrentUserID(userID);
		animalConfig = animalConfiguration;
		fileChooser = new AnimalFileChooser(animalConfig);
		fileChooser.resetFilters();
		fileChooser.addFilter("ann", "Annotation File");
		workContainer().setLayout(new BorderLayout());
		JPanel textPanels = new JPanel();
		textPanels.setLayout(new GridLayout(2, 1));
		userComments = new JTextArea(60, 6);
		foreignComments = new JTextArea(60, 6);
		foreignComments.setEditable(false);

		textPanels.add(foreignComments);
		textPanels.add(userComments);
		workContainer().add(BorderLayout.CENTER, textPanels);

		JPanel buttonPanel = new JPanel();
		reloadButton = AnimalTranslator.getGUIBuilder().generateJButton(
				"reloadAnn", null, false, this);
		mergeButton = AnimalTranslator.getGUIBuilder().generateJButton("mergeAnn",
				null, false, this);
		saveAsButton = AnimalTranslator.getGUIBuilder().generateJButton(
				"saveAnnAs", null, false, this);
		undoButton = AnimalTranslator.getGUIBuilder().generateJButton("undo", null,
				false, this);

		buttonPanel.add(reloadButton);
		buttonPanel.add(mergeButton);
		buttonPanel.add(saveAsButton);
		buttonPanel.add(undoButton);
		workContainer().add(BorderLayout.SOUTH, buttonPanel);
		setSize(480, 320);
	}

	public AnnotationEditor(Animal animalInstance, XProperties properties,
			String userID, XProperties useThese, 
			AnimalConfiguration animalConfiguration) {
		this(animalInstance, properties, userID, animalConfiguration);
		setAnnotations(useThese);
	}

	private XProperties getAnnotations() {
		if (props == null) {
			props = new XProperties();
		}
		return props;
	}

	public String getBaseAnimName() {
		String basicName = animalConfig.getCurrentFilename();
		if (basicName == null)
			return "demo";
		int lastDotPos = basicName.lastIndexOf(".");
		return basicName.substring(0, lastDotPos);
	}

	public int getCurrentStep() {
		if (currentStep < 1)
			return 1;
		return currentStep;
	}

	public String getCurrentUserID() {
		if (currentUser == null)
			return "dummy";
		return currentUser;
	}

	public boolean hasChanged() {
		return hasChanged;
	}

	public void resetChange() {
		hasChanged = false;
	}

	public void setChanged(boolean newState) {
		hasChanged = newState;
	}

	public void setCurrentStep(int stepNr) {
		storeCurrentEdits();
		currentStep = stepNr;
		showMessagesForStep();
		setTitle(AnimalTranslator.translateMessage("annotationWindowLabel",
				new String[] { String.valueOf(stepNr) }));
	}

	public void setCurrentUserID(String aUserID) {
		currentUser = aUserID;
	}

	public void setAnnotations(XProperties useThese) {
		if (useThese != null)
			props = useThese;
	}

	public void showMessagesForStep() {
		int step = getCurrentStep();
		String[] keysForStep = getAnnotations().getKeys(String.valueOf(step));
		StringBuilder userCommentBuffer = new StringBuilder(1024);
		StringBuilder foreignCommentBuffer = new StringBuilder(1024);
		if (keysForStep != null) {
			String comparator = (new StringBuilder(String.valueOf(step)).append(".")
					.append(getCurrentUserID())).toString();
			for (int n = keysForStep.length, i = 0; i < n; i++) {
				String entry = keysForStep[i];
				if (entry.startsWith(comparator))
					userCommentBuffer.append(getAnnotations().getProperty(entry)).append(
							MessageDisplay.LINE_FEED);
				else
					foreignCommentBuffer.append(strip(entry)).append(
							getAnnotations().getProperty(entry)).append(MessageDisplay.LINE_FEED);
			}
			userComments.setText(userCommentBuffer.toString());
			foreignComments.setText(foreignCommentBuffer.toString());
		}
	}

	public void loadAnnotations() {
		setAnnotations(new XProperties(rawLoadAnnotations(getBaseAnimName()
				+ ".ann")));
	}

	private Properties rawLoadAnnotations(String filename) {
		ResourceLocator resourceLocator = ResourceLocator.getResourceLocator();
		Properties loadedProps = new Properties();
		InputStream is = resourceLocator.getResourceStream(filename);
		try {
			if (is != null)
				loadedProps.load(is);
		} catch (Exception e) {
			MessageDisplay.errorMsg(AnimalTranslator.translateMessage("couldNotLoadAnnotations",
					new String[] { filename}),
					MessageDisplay.INFO);
		}
		setChanged(false);
		return loadedProps;
	}

	public void mergeAnnotations(String filename) {
		Properties compareAnnotations = rawLoadAnnotations(filename);

		// compare keys: if new, add; if exists and not identical, ask
		Enumeration<?> elementList = compareAnnotations.propertyNames();
		String key = null, message = null;
		while (elementList.hasMoreElements()) {
			key = (String) elementList.nextElement();
			message = compareAnnotations.getProperty(key);
			if (!props.containsKey(key))
				props.put(key, message);
			else if (!(message.equals(props.getProperty(key))))
				props.put(key, props.getProperty(key) + MessageDisplay.LINE_FEED + message);
		}
		showMessagesForStep();
		setChanged(true);
	}

	public void saveAnnotationsAs() {
		String filename = fileChooser.openForFilenameChoice(this);
		if (!filename.endsWith(".ann"))
			filename += ".ann";
		saveAnnotations(filename);
	}

	public void saveAnnotations(String filename) {
		try {
			props.list(System.out);
			props.store(new FileOutputStream(filename), "Animal Annotations by "
					+ getCurrentUserID());
			setChanged(false);
			StringBuilder annotationURL = new StringBuilder(1024);
			long animationUID = Animal.get().getAnimation().getAnimationUID();
			annotationURL.append(baseAnnotateURL).append("?auid=").append(animationUID);
			annotationURL.append(";user=").append(getCurrentUserID()).append(
					";comment=");
			annotationURL.append("FIXED_TEXT");
			MessageDisplay.errorMsg(annotationURL.toString(),
					MessageDisplay.DEBUG_MESSAGE);
		} catch (Exception e) {
			MessageDisplay.errorMsg(AnimalTranslator.translateMessage("couldNotSaveAnnotations",
					new String[] { filename }),
					MessageDisplay.RUN_ERROR);
		}
	}

	private String strip(String entry) {
		if (entry == null)
			return "";
		StringBuilder result = new StringBuilder(60);
		result.append("[").append(entry.substring(entry.indexOf(".") + 1)).append(
				"] ");
		return result.toString();
	}

	private void storeCurrentEdits() {
		// setCurrentStep(animal.getAnimationWindow(false).getStep());
		// assemble the entries and place them in the repository
		String input = userComments.getText();
		if (input != null && input.length() > 1) {
			StringBuilder buf = new StringBuilder(1024);
			buf.append(String.valueOf(getCurrentStep())).append(".").append(
					getCurrentUserID());
			getAnnotations().put(buf.toString(), userComments.getText());
			setChanged(true);
		}
	}

	public void actionPerformed(ActionEvent evt) {
		// must store the edits somewhere!
		// storeCurrentEdits();

		if (evt.getSource() == reloadButton) {
			loadAnnotations();
		} else if (evt.getSource() == mergeButton) {
			String filename = fileChooser.openForFilenameChoice(this);
			mergeAnnotations(filename);
		} else if (evt.getSource() == saveAsButton) {
			saveAnnotationsAs();
		}
		// undoButton is implicit -- refresh automatically drops last edits!
		showMessagesForStep();
	}
}
