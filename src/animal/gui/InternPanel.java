package animal.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import translator.AnimalTranslator;
import animal.dialog.OptionDialog;
import animal.main.Animal;
import animal.main.AnimationWindow;
import animal.main.Link;
import animal.misc.MessageDisplay;
import animal.misc.ObjectSelectionButton;

/**
 * the InternPanel provides options to configure DrawWindow's behaviour, to
 * enable selection, work on selected objects etc.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.0 18.07.1998
 */
public class InternPanel extends JPanel implements ActionListener, ItemListener {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 104342772853460505L;

	private static final String[] DEFAULT_MAGS = new String[] { "50%", "71%",
			"100%", "141%", "200%" };

	/** the DrawWindow InternPanel is in */
	private DrawWindow drawWindow;

	/* all buttons of the InternPanel */

	/** use Snap */
	private AbstractButton snapButton;

	/** turn selection on */
	private AbstractButton selectionButton;

	/** enable selection of several objects and area selection */
	private AbstractButton multiSelectionButton;

	/** use Editors when objects are selected */
	private AbstractButton useEditorsButton;

	/** delete the selected objects */
	private AbstractButton deleteButton;

	/** enable showing of temporary objects */
	private AbstractButton showTempObjectsButton;

	/** write all changes back to the Animation */
	private AbstractButton writeBackButton;

	/** Combobox to select the magnification */
	private JComboBox<String> magnificationCB;

	/* private JSlider stepSlider; */

	/**
	 * undo the last action, accessed from DrawCanvas
	 */
	AbstractButton undoButton;

	/**
	 * redo the last action that was undone, accessed from DrawCanvas
	 */
	AbstractButton redoButton;

	/** repaint all objects */
	private AbstractButton repaintButton;

	/*
	 * * close the DrawWindow * / private AbstractButton closeButton;
	 */

	/** clone the currently selected GraphicObjects */
	private AbstractButton cloneButton;

	/** show the preferences dialog */
	private AbstractButton optionButton;

	/** the choice for the grid's width */
	private JComboBox<String> choiceGrid;

	/** the text field to set the step for the DrawWindow */
	private JTextField stepTF;

	/** go to previous step */
	private AbstractButton prevStepButton;

	/** go to next step */
	private AbstractButton nextStepButton;

	/** run this step in the AnimationWindow */
	private AbstractButton runStepButton;

	/**
	 * used to store the state of multiSelectionButton before external selection
	 * by an ObjectSelectionButton was started. This will be restored when the
	 * ObjectSelectionButton is deselected.
	 */
	private boolean oldMultiSelection;

	/**
	 * used to store the state of useEditorsButton before external selection by
	 * an ObjectSelectionButton was started. This will be restored when the
	 * ObjectSelectionButton is deselected.
	 */
	private boolean oldUseEditors;

	/**
	 * initializes the InternPanel by entering all Buttons etc. into a
	 * <b>GridBagLayout </b>.
	 */
	InternPanel(DrawWindow aDrawWindow) {
		super();
		drawWindow = aDrawWindow;
		setLayout(new BorderLayout());
		//    JPanel topPanel = new JPanel();

		GridBagLayout gridBagLayout;
		gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);

		JLabel gridLabel = AnimalTranslator.getGUIBuilder().generateJLabel("grid");

		//      insert the grid width Choice together with a label at top of the
		//      InternPanel
		GridBagConstraints gbc;
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(0, 0, 0, 0);
		gridBagLayout.setConstraints(gridLabel, gbc);
		add(gridLabel);

		choiceGrid = AnimalTranslator.getGUIBuilder().generateJComboBox("gridBox", null,
				new String[] { "0", "5", "10", "20", "25", "50" }, String
						.valueOf(drawWindow.getDrawCanvas().getGrid()));
		Insets defaultInsets = new Insets(0, 0, 0, 0);

		gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = defaultInsets;
		gridBagLayout.setConstraints(choiceGrid, gbc);
		choiceGrid.addItemListener(this);
		add(choiceGrid);

		snapButton = AnimalTranslator.getGUIBuilder().generateJButton("snapMode", null,
				true, this);
		snapButton.setSelected(drawWindow.getDrawCanvas().isSnap());
		registerComponent(snapButton, gridBagLayout, false);

		showTempObjectsButton = AnimalTranslator.getGUIBuilder().generateJButton(
				"showTempMode", null, true, this);
		showTempObjectsButton.setSelected(true);
		registerComponent(showTempObjectsButton, gridBagLayout, false);

		repaintButton = AnimalTranslator.getGUIBuilder().generateJButton("repaint", null,
				false, this);
		registerComponent(repaintButton, gridBagLayout, true);

		// second row: selection modes
		selectionButton = AnimalTranslator.getGUIBuilder().generateJButton(
				"selectionOnOff", null, false, this);
		registerComponent(selectionButton, gridBagLayout, false);

		multiSelectionButton = AnimalTranslator.getGUIBuilder().generateJButton(
				"multiSelect", null, true, this);
		registerComponent(multiSelectionButton, gridBagLayout, false);

		useEditorsButton = AnimalTranslator.getGUIBuilder().generateJButton("showEdit",
				null, true, this);
		registerComponent(useEditorsButton, gridBagLayout, true);

		// fourth row: commands to DrawWindow
		undoButton = AnimalTranslator.getGUIBuilder().generateJButton("undo", null,
				false, this);
		registerComponent(undoButton, gridBagLayout, false);

		redoButton = AnimalTranslator.getGUIBuilder().generateJButton("redo", null,
				false, this);
		registerComponent(redoButton, gridBagLayout, false);

		deleteButton = AnimalTranslator.getGUIBuilder().generateJButton("delete", null,
				false, this);
		registerComponent(deleteButton, gridBagLayout, true);

		// activate the correct buttons.
		// must not be done before the second row is inserted as these buttons
		// are enabled/disabled according to selection mode.
		setSelection(false, false, false, null);

		cloneButton = AnimalTranslator.getGUIBuilder().generateJButton("clone", null,
				false, this);
		registerComponent(cloneButton, gridBagLayout, false);

		writeBackButton = AnimalTranslator.getGUIBuilder().generateJButton("writeBack",
				null, false, this);
		registerComponent(writeBackButton, gridBagLayout, false);

		runStepButton = AnimalTranslator.getGUIBuilder().generateJButton("runStep", null,
				false, this);
		registerComponent(runStepButton, gridBagLayout, true);

		prevStepButton = AnimalTranslator.getGUIBuilder().generateJButton("prevStep",
				null, false, this);
		registerComponent(prevStepButton, gridBagLayout, false);

		stepTF = AnimalTranslator.getGUIBuilder().generateJTextField("stepChoice", null,
				3, "1");
		stepTF.addActionListener(this);
		registerComponent(stepTF, gridBagLayout, false);

		nextStepButton = AnimalTranslator.getGUIBuilder().generateJButton("nextStep",
				null, false, this);
		registerComponent(nextStepButton, gridBagLayout, true);

		magnificationCB = AnimalTranslator.getGUIBuilder().generateJComboBox(
				"magnificationBox", null, DEFAULT_MAGS, DEFAULT_MAGS[2]);
		registerComponent(magnificationCB, gridBagLayout, false);
		magnificationCB.addActionListener(this);
		optionButton = AnimalTranslator.getGUIBuilder().generateJButton("options", null,
				false, this);
		registerComponent(optionButton, gridBagLayout, true);
	}

	public void registerComponent(JComponent component, GridBagLayout layouter,
			boolean lastElementInRow) {
		GridBagConstraints gbc = new GridBagConstraints();
		if (lastElementInRow)
			gbc.gridwidth = GridBagConstraints.REMAINDER;
		else
			gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		layouter.setConstraints(component, gbc);
		add(component);
	}

	/**
	 * adds one button to the InternPanel.
	 * 
	 * @param toggle
	 *            if true, button is a JToggleButton, else it's a JButton
	 * @param filename
	 *            name of the Icon file
	 * @param tip
	 *            Tooltip to display when the mouse is over the button
	 * @param lastInRow
	 *            button is the last button in its row. After it, start a new
	 *            row
	 * @return the button created
	 * /
	private AbstractButton addButton(boolean toggle, String filename,
			String tip, boolean lastInRow) {
		AbstractButton result;
		// make a button of requested type
		if (toggle)
			result = new JToggleButton(drawWindow.getImageIcon(filename));
		else
			result = new JButton(drawWindow.getImageIcon(filename));

		// set its options
		result.setMargin(new Insets(0, 0, 0, 0));
		result.setToolTipText(tip);

		// it doesn't matter which button was pressed last, so don't display
		// the focus.
		result.setFocusPainted(false);

		// insert it correctly into the GridBagLayout
		GridBagConstraints gbc = new GridBagConstraints();
		if (lastInRow)
			gbc.gridwidth = GridBagConstraints.REMAINDER;
		else
			gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		((GridBagLayout) getLayout()).setConstraints(result, gbc);

		// make it work :)
		result.addActionListener(this);
		add(result);

		// and finally return it
		return result;
	}
*/
	/**
	 * sets the buttons' state according to the selection mode. Notification
	 * Order is: InternPanel -> DrawCanvas -> GraphicVector
	 */
	void setSelection(boolean selection, boolean multiSelection,
			boolean useEditors, ObjectSelectionButton osb) {
		boolean enableButtons = selection && (osb == null);
		// selectionButton.setSelected(enableButtons);

		// all the following buttons are only enabled if selection is on
		multiSelectionButton.setEnabled(enableButtons);
		useEditorsButton.setEnabled(enableButtons);
		deleteButton.setEnabled(enableButtons);
		// it's either selection or creating new objects
		if (selection)
			drawWindow.getObjectPanel().setCurrentEditor(null);
		// propagate selection mode to drawCanvas
		drawWindow.getDrawCanvas().setSelection(selection, multiSelection,
				useEditors, osb);
	}

	/**
	 * turns selection on/off. <br>
	 * called by ObjectPanel.
	 */
	void setSelection(boolean selection) {
		setSelection(selection, multiSelectionButton.isSelected(),
				useEditorsButton.isSelected(), null);
	}

	/**
	 * turn selection by an ObjectSelectionButton on/off. If turned on, the
	 * current selection mode is stored and selection set to single selection
	 * without editors. If turned on, restore the old selection mode.
	 */
	void setExternalSelection(ObjectSelectionButton osb) {
		if (osb != null) {
			oldMultiSelection = multiSelectionButton.isSelected();
			oldUseEditors = useEditorsButton.isSelected();
			setSelection(true, osb.hasMultiSelection(), false, osb);
		} else
			setSelection(true, oldMultiSelection, oldUseEditors, null);
	}

	/**
	 * reacts to buttons pressed or depressed(kind of a psychiatrist, uh?). Just
	 * the standard thing.
	 */
	public void actionPerformed(ActionEvent e) {
		Object object = e.getSource();
		if (object == optionButton)
			OptionDialog.getOptionDialog(Animal.get()).setVisible(true);
		// react to all three selection mode buttons at once
		else if (object == selectionButton || object == multiSelectionButton
				|| object == useEditorsButton) {
			setSelection(true, multiSelectionButton.isSelected(),
					useEditorsButton.isSelected(), null);
		}
		// work on selected objects
		else if (object == magnificationCB) {
			String s = (String) magnificationCB.getSelectedItem();
			s = s.substring(0, s.length() - 1);
			int i;
			try {
				i = Integer.parseInt(s);
			} catch (NumberFormatException e2) {
				MessageDisplay.errorMsg("illegalMagnification", s,
						MessageDisplay.PROGRAM_ERROR);
				i = 1;
			}
			double factor = 1d / 100 * i;
			drawWindow.getDrawCanvas().setMagnification(factor);
		} else if (object == deleteButton)
			drawWindow.getDrawCanvas().deleteSelected();
		// toggle display options
		else if (object == snapButton)
			drawWindow.getDrawCanvas().setSnap(snapButton.isSelected());
		else if (object == showTempObjectsButton)
			drawWindow.getDrawCanvas().setShowTempObjects(
					showTempObjectsButton.isSelected());
		else if (object == cloneButton)
			drawWindow.getDrawCanvas().cloneSelectedObjects();
		// commands to DrawWindow
		else if (object == writeBackButton) {
			drawWindow.setChanged();
			drawWindow.writeBack();
		} else if (object == repaintButton)
			drawWindow.getDrawCanvas().repaintAll();
		else if (object == undoButton)
			drawWindow.getDrawCanvas().getUndoAdapter().undo();
		else if (object == redoButton)
			drawWindow.getDrawCanvas().getUndoAdapter().redo();
		// navigation
		else if (object == prevStepButton) {
			drawWindow.setStep(drawWindow.getAnimationState().getPrevStep());
		} else if (object == nextStepButton) {
			int next = drawWindow.getAnimationState().getNextStep();
			if (next != Link.END)
				drawWindow.setStep(next);
		} else if (object == runStepButton) {
		    //  when showing another window, the current window loses the focus.
		    //  So if the AnimationWindow is already visible, don't show it,
		    //  if it is visible, show it, but return the focus.
		    AnimationWindow animWin = AnimalMainWindow.getWindowCoordinator().getAnimationWindow(false);
		    if (!animWin.isVisible()) {
		        animWin.setVisible(true);
		        requestFocus();
		    }
		    // write back changed data...
		    drawWindow.writeBack();
		    // ... and show the current step in the AnimationWindow
		    animWin.setStep(drawWindow.getAnimationState().getStep(), false);
		} else if (object == stepTF) {
		    // try to extract a valid number from the step TextField
		    // and to make this the step to be set.
		    // This fails, if the TextField's content is not a valid
		    // number or not the number of a valid step.
		    try {
		        if (!(drawWindow.setStep(Integer.parseInt(stepTF.getText()))))
		            // not the fine English art, but works :)
		            
		            throw new NumberFormatException();
		    } catch (NumberFormatException ex) {
		        JOptionPane.showMessageDialog(
		        		this,
		        		AnimalTranslator.translateMessage("notAValidStep",
		        				stepTF.getText()),
		        		AnimalTranslator.translateMessage("illegalStepNumberFormat"),
		        		JOptionPane.ERROR_MESSAGE);
		    }
		}
	}

	/**
	 * reacts to changes in the grid width choice by setting DrawCanvas' grid
	 * width.
	 */
	public void itemStateChanged(ItemEvent e) {
		String grid = "";
		System.err.print("GRID " +grid);
		if (e.getSource() == choiceGrid)
		  grid = (String)choiceGrid.getSelectedItem();
		System.err.println("..." +grid);
//		grid = (String) ((JComboBox) e.getSource()).getSelectedItem();
		if (grid.equals("none"))
			grid = "0";
		drawWindow.getDrawCanvas().setGrid(Integer.parseInt(grid));
	} // itemStateChanged

	/**
	 * sets the InternPanel's step, i.e. shows it in the step TextField.
	 */
	void setStep(int step) {
		stepTF.setText(String.valueOf(step));
	}
}
