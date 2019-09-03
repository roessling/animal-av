package animal.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import translator.AnimalTranslator;
import translator.TranslatableGUIElement;
import animal.main.AnimationWindow;
import animal.main.Link;
import animal.main.ObjectPanel;
import animal.misc.MessageDisplay;
import animal.misc.ObjectSelectionButton;

public class DrawControlsToolbar extends JToolBar implements ActionListener,
		ItemListener {
	
	private static final String[] DEFAULT_MAGS = new String[] { "50%", "71%",
		"100%", "141%", "200%" };

	/**
	 * 
	 */
	private static final long serialVersionUID = 2090022041469078494L;
	protected DrawWindow drawingWindow = null;
	private JComboBox<String> choiceGrid;
	protected AbstractButton snapButton;
	protected AbstractButton showTempObjectsButton;
  protected AbstractButton      repaintButton;
	protected AbstractButton undoButton;
	protected AbstractButton redoButton;
	protected AbstractButton cloneButton;
	protected AbstractButton deleteButton;
	protected AbstractButton writeBackButton;
	protected AbstractButton runStepButton;
	protected AbstractButton prevStepButton;
	protected AbstractButton nextStepButton;
//	protected AbstractButton optionButton;
	protected JTextField stepTF;
	protected JComboBox<String> magnificationCB;
	protected ObjectPanel objectPanel;
  private Font                  defaultFont      = new Font("Dialog", 0, 14);
  private int                   height           = 20;
  private int                   width            = 20;
	
	
	/**
	 * used to store the state of multiSelectionButton before external selection
	 * by an ObjectSelectionButton was started. This will be restored when the
	 * ObjectSelectionButton is unselected.
	 */
	private boolean oldMultiSelection;

	/**
	 * used to store the state of useEditorsButton before external selection by
	 * an ObjectSelectionButton was started. This will be restored when the
	 * ObjectSelectionButton is unselected.
	 */
	private boolean oldUseEditors;

	
	public DrawControlsToolbar(DrawWindow aDrawWindow, ObjectPanel objects) {
		super();
		drawingWindow = aDrawWindow;
		TranslatableGUIElement generator = AnimalTranslator.getGUIBuilder();

		// build select / edit buttons
		// TODO: goes to WEST
//		add(buildSelectEditComponents(generator));

		// add some space
		addSeparator();
		
		// add clone and delete
		add(buildCloneDeleteButtons(generator));
		
		// add undo / redo facility
		add(buildUndoRedoButtons(generator));

		// add update buttons: write back and run
		add(buildUpdateButtons(generator));
		
		// add some space
		addSeparator();

		// build grid component
		add(buildGridBox(generator));
		
		// build display options
		add(buildDisplayComponents(generator));
				
		// add some space
		addSeparator();
		
//		
//		// add some space
//		addSeparator();
//		
//		
//		
//		// add some space
//		addSeparator();
//		
//		
//		// add some space
//		addSeparator();
		
		// add navigation elements
		add(buildNavigationElements(generator));
		// second row: selection modes
//		selectionButton = generator.generateJButton(
//				"selectionOnOff", null, false, this);
//		registerComponent(selectionButton, gridBagLayout, false);


		// activate the correct buttons.
		// must not be done before the second row is inserted as these buttons
		// are enabled/disabled according to selection mode.
		setSelection(false, false, false, null);

		addSeparator();
		objectPanel = objects;
		add(objectPanel);
//	   if (objectPanel == null)
//	      objectPanel = new ObjectPanel(Animal.get(), drawingWindow, 
//	      		drawingWindow.getProperties(
//	      				AnimalConfiguration.getDefaultConfiguration().getProperties()), 
//	      				true);
	    
	}

	private Box buildNavigationElements(TranslatableGUIElement generator) {
//		Box contentBox = generator.generateBorderedBox(BoxLayout.LINE_AXIS, 
//				"navigation");
		Box contentBox = new Box(BoxLayout.LINE_AXIS);
		prevStepButton = generator.generateJButton("prevStep",
				null, false, this);
		contentBox.add(prevStepButton);

		stepTF = generator.generateJTextField("stepChoice", null,
				3, "1");
		stepTF.setMinimumSize(new Dimension(30, 20));
		stepTF.addActionListener(this);
		contentBox.add(stepTF);

		nextStepButton = generator.generateJButton("nextStep",
				null, false, this);
		contentBox.add(nextStepButton);
		return contentBox;
	}
	
	private Box buildCloneDeleteButtons(TranslatableGUIElement generator) {
//		Box contentBox = generator.generateBorderedBox(BoxLayout.LINE_AXIS,
//				"cloneDelete");
		Box contentBox = new Box(BoxLayout.LINE_AXIS);
		cloneButton = generator.generateJButton("clone", null,
				false, this);
		contentBox.add(cloneButton);
		
		deleteButton = generator.generateJButton("delete", null,
				false, this);
		contentBox.add(deleteButton);
		
		return contentBox;
	}
	
	private Box buildUpdateButtons(TranslatableGUIElement generator) {
//		Box contentBox = generator.generateBorderedBox(BoxLayout.LINE_AXIS, 
//				"updateBL");
		Box contentBox = new Box(BoxLayout.LINE_AXIS);

		writeBackButton = generator.generateJButton("writeBack",
				null, false, this);
		contentBox.add(writeBackButton);

		runStepButton = generator.generateJButton("runStep", null,
				false, this);
		contentBox.add(runStepButton);
		return contentBox;
	}
	
	private Box buildUndoRedoButtons(TranslatableGUIElement generator) {
//		Box contentBox = generator.generateBorderedBox(BoxLayout.LINE_AXIS, 
//				"undoRedo");
		Box contentBox = new Box(BoxLayout.LINE_AXIS);

		undoButton = generator.generateJButton("undo", null,
				false, this);
		contentBox.add(undoButton);

		redoButton = generator.generateJButton("redo", null,
				false, this);
		contentBox.add(redoButton);
		return contentBox;
	}
	
//	
//	private Box buildSelectEditComponents(TranslatableGUIElement generator) {
////		Box contentBox = generator.generateBorderedBox(BoxLayout.LINE_AXIS, 
////		"selectEditBL");
//		Box contentBox = new Box(BoxLayout.LINE_AXIS);
//
//		multiSelectionButton = generator.generateJButton("multiSelect", 
//				null, true, this);
//		contentBox.add(multiSelectionButton);
//
//		useEditorsButton = generator.generateJButton("showEdit",
//				null, true, this);
//		contentBox.add(useEditorsButton);
//		
//		return contentBox;
//	}
	
	private Box buildDisplayComponents(TranslatableGUIElement generator) {
//		Box contentBox = generator.generateBorderedBox(BoxLayout.LINE_AXIS, 
//				"displayBL");
		Box contentBox = new Box(BoxLayout.LINE_AXIS);

		showTempObjectsButton = generator.generateJButton(
				"showTempMode", null, true, this);
		showTempObjectsButton.setSelected(true);
		contentBox.add(showTempObjectsButton);
		
		repaintButton = generator.generateJButton("repaint", null,
				false, this);
		contentBox.add(repaintButton);
		
		magnificationCB = generator.generateJComboBox(
				"magnificationBox", null, DEFAULT_MAGS, DEFAULT_MAGS[2]);
		contentBox.add(magnificationCB);
		magnificationCB.addActionListener(this);
//		optionButton = generator.generateJButton("options", null,
//				false, this);
//		contentBox.add(optionButton);

		return contentBox;
	}
	
	private Box buildGridBox(TranslatableGUIElement generator) {
//		Box gridBox = generator.generateBorderedBox(BoxLayout.LINE_AXIS, 
//		"gridBL");
		Box contentBox = new Box(BoxLayout.LINE_AXIS);

//		JLabel gridLabel = generator.generateJLabel("grid");
//		add(gridLabel);

		choiceGrid = generator.generateJComboBox("gridBox", null,
				new String[] { "0", "5", "10", "20", "25", "50" }, String
				.valueOf(drawingWindow.getDrawCanvas().getGrid()));
		contentBox.add(choiceGrid);

		snapButton = generator.generateJButton("snapMode", null,
				true, this);
		snapButton.setSelected(drawingWindow.getDrawCanvas().isSnap());
		contentBox.add(snapButton);
		choiceGrid.addItemListener(this);
		return contentBox;
	}
	
	public void actionPerformed(ActionEvent e) {
		Object object = e.getSource();
//		if (object == optionButton)
//			OptionDialog.getOptionDialog(Animal.get()).setVisible(true);
		// react to all three selection mode buttons at once
//		else 
//			if (//object == selectionButton ||
//				object == multiSelectionButton
//				|| object == useEditorsButton) {
//			setSelection(true, multiSelectionButton.isSelected(),
//					useEditorsButton.isSelected(), null);
//		}
		// work on selected objects
//		else 
			if (object == magnificationCB) {
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
			drawingWindow.getDrawCanvas().setMagnification(factor);
		} else if (object == deleteButton)
			drawingWindow.getDrawCanvas().deleteSelected();
		// toggle display options
		else if (object == snapButton)
			drawingWindow.getDrawCanvas().setSnap(snapButton.isSelected());
		else if (object == showTempObjectsButton)
			drawingWindow.getDrawCanvas().setShowTempObjects(
					showTempObjectsButton.isSelected());
		else if (object == cloneButton)
			drawingWindow.getDrawCanvas().cloneSelectedObjects();
		// commands to DrawWindow
		else if (object == writeBackButton) {
			drawingWindow.setChanged();
			drawingWindow.writeBack();
		} else if (object == repaintButton)
			drawingWindow.getDrawCanvas().repaintAll();
		else if (object == undoButton)
			drawingWindow.getDrawCanvas().getUndoAdapter().undo();
		else if (object == redoButton)
			drawingWindow.getDrawCanvas().getUndoAdapter().redo();
		// navigation
		else if (object == prevStepButton) {
			drawingWindow.setStep(drawingWindow.getAnimationState().getPrevStep());
		} else if (object == nextStepButton) {
			int next = drawingWindow.getAnimationState().getNextStep();
			if (next != Link.END)
				drawingWindow.setStep(next);
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
		    drawingWindow.writeBack();
		    // ... and show the current step in the AnimationWindow
		    animWin.setStep(drawingWindow.getAnimationState().getStep(), false);
		} else if (object == stepTF) {
		    // try to extract a valid number from the step TextField
		    // and to make this the step to be set.
		    // This fails, if the TextField's content is not a valid
		    // number or not the number of a valid step.
		    try {
		        if (!(drawingWindow.setStep(Integer.parseInt(stepTF.getText()))))
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
		}	}

	public void itemStateChanged(ItemEvent e) {
		String grid = "";
		if (e.getSource() == choiceGrid)
		  grid = (String)choiceGrid.getSelectedItem();
//		grid = (String) ((JComboBox<String>) e.getSource()).getSelectedItem();
		if (grid.equals("none"))
			grid = "0";
		drawingWindow.getDrawCanvas().setGrid(Integer.parseInt(grid));
	}
	
	/**
	 * sets the buttons' state according to the selection mode. Notification
	 * Order is: InternPanel -> DrawCanvas -> GraphicVector
	 */
	void setSelection(boolean selection, boolean multiSelection,
			boolean useEditors, ObjectSelectionButton osb) {
		boolean enableButtons = selection && (osb == null);
		// selectionButton.setSelected(enableButtons);

		// all the following buttons are only enabled if selection is on
		//TODO the following must be disabled as they do not exist...
//		multiSelectionButton.setEnabled(enableButtons);
//		useEditorsButton.setEnabled(enableButtons);
		deleteButton.setEnabled(enableButtons);
		// it's either selection or creating new objects
		if (selection)
			drawingWindow.getObjectPanel().setCurrentEditor(null);
		// propagate selection mode to drawCanvas
		drawingWindow.getDrawCanvas().setSelection(selection, multiSelection,
				useEditors, osb);
	}

	/**
	 * turns selection on/off. <br>
	 * called by ObjectPanel.
	 */
	void setSelection(boolean selection) {
		setSelection(selection, true, true, null);
	}
	
	/**
	 * updates the text field showing the selected step.
	 * 
	 * @param step the step to change to
	 */
	public void setStep(int step) {
		stepTF.setText(String.valueOf(step));
	}

	/**
	 * turn selection by an ObjectSelectionButton on/off. If turned on, the
	 * current selection mode is stored and selection set to single selection
	 * without editors. If turned on, restore the old selection mode.
	 */
	void setExternalSelection(ObjectSelectionButton osb) {
		if (osb != null) {
//			oldMultiSelection = multiSelectionButton.isSelected();
//			oldUseEditors = useEditorsButton.isSelected();
			setSelection(true, osb.hasMultiSelection(), false, osb);
		} else
			setSelection(true, oldMultiSelection, oldUseEditors, null);
	}

  /**
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public void zoom(boolean zoomIn) {
    Image img;
    Image newimg;
    ImageIcon icon;

    if (zoomIn) {
      if (defaultFont.getSize() < 24) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() + 2);
      }
      if (height < 30) {
        height = height + 5;
        width = width + 5;
      }
    } else {
      if (defaultFont.getSize() > 10) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() - 2);
      }
      if (height > 10) {
        height = height - 5;
        width = width - 5;
      }

    }

    if (choiceGrid != null)
      choiceGrid.setFont(defaultFont);

    if (snapButton != null) {
      img = ((ImageIcon) snapButton.getIcon()).getImage();
      newimg = img.getScaledInstance(width, height,
          java.awt.Image.SCALE_SMOOTH);
      icon = new ImageIcon(newimg);
      snapButton.setIcon(icon);

    }
    
    if (showTempObjectsButton != null) {
      img = ((ImageIcon) showTempObjectsButton.getIcon()).getImage();
      newimg = img.getScaledInstance(width, height,
          java.awt.Image.SCALE_SMOOTH);
      icon = new ImageIcon(newimg);
      showTempObjectsButton.setIcon(icon);
    }
    

    if (repaintButton != null) {
      img = ((ImageIcon) repaintButton.getIcon()).getImage();
      newimg = img.getScaledInstance(width, height,
          java.awt.Image.SCALE_SMOOTH);
      icon = new ImageIcon(newimg);
      repaintButton.setIcon(icon);

    }

    
    if (undoButton != null) {
      img = ((ImageIcon) undoButton.getIcon()).getImage();
      newimg = img.getScaledInstance(width, height,
          java.awt.Image.SCALE_SMOOTH);
      icon = new ImageIcon(newimg);
      undoButton.setIcon(icon);
    }

    if (redoButton != null) {
      img = ((ImageIcon) redoButton.getIcon()).getImage();
      newimg = img.getScaledInstance(width, height,
          java.awt.Image.SCALE_SMOOTH);
      icon = new ImageIcon(newimg);
      redoButton.setIcon(icon);
    }

    if (cloneButton != null) {
      img = ((ImageIcon) cloneButton.getIcon()).getImage();
      newimg = img.getScaledInstance(width, height,
          java.awt.Image.SCALE_SMOOTH);
      icon = new ImageIcon(newimg);
      cloneButton.setIcon(icon);
    }


    if (deleteButton != null) {
      img = ((ImageIcon) deleteButton.getIcon()).getImage();
      newimg = img.getScaledInstance(width, height,
          java.awt.Image.SCALE_SMOOTH);
      icon = new ImageIcon(newimg);
      deleteButton.setIcon(icon);
    }

    if (writeBackButton != null) {
      img = ((ImageIcon) writeBackButton.getIcon()).getImage();
      newimg = img.getScaledInstance(width, height,
          java.awt.Image.SCALE_SMOOTH);
      icon = new ImageIcon(newimg);
      writeBackButton.setIcon(icon);
    }

    if (runStepButton != null) {
      img = ((ImageIcon) runStepButton.getIcon()).getImage();
      newimg = img.getScaledInstance(width, height,
          java.awt.Image.SCALE_SMOOTH);
      icon = new ImageIcon(newimg);
      runStepButton.setIcon(icon);
    }

    if (prevStepButton != null) {
      img = ((ImageIcon) prevStepButton.getIcon()).getImage();
      newimg = img.getScaledInstance(width, height,
          java.awt.Image.SCALE_SMOOTH);
      icon = new ImageIcon(newimg);
      prevStepButton.setIcon(icon);
    }
    if (nextStepButton != null) {
      img = ((ImageIcon) nextStepButton.getIcon()).getImage();
      newimg = img.getScaledInstance(width, height,
          java.awt.Image.SCALE_SMOOTH);
      icon = new ImageIcon(newimg);
      nextStepButton.setIcon(icon);
    }

    if (stepTF != null)
      stepTF.setFont(defaultFont);

    if (magnificationCB != null)
      magnificationCB.setFont(defaultFont);

  }

 
 
  



}
