package animal.misc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import translator.AnimalTranslator;
import animal.editor.Editor;
import animal.editor.animators.AnimatorEditor;
import animal.gui.AnimalMainWindow;
import animal.gui.DrawWindow;

/**
 * <code>ObjectSelectionButton</code> is used in Editors to select the Objects
 * to animate or auxiliary objects for an Animator. <br>
 * It displays the number(s) of the object(s) currently selected and can be
 * configured to refer to one or to several objects.
 * <p>
 * Only one button can be pressed at a time.
 * <p>
 * If an ObjectSelectionButton is used in an Editor, this Editor's
 * <code>isOK</code> -method should call OSB.checkObject() to ensure that no
 * objects where deleted meanwhile.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.0 24.08.1998
 */
public class ObjectSelectionButton extends JToggleButton implements
		ActionListener {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 4325851584622507314L;

	/** the currently active(i.e. pressed) button. */
	static ObjectSelectionButton activeButton = null;

	/**
	 * already registered as a listener? OSBs deselect themselves if their
	 * window is closed.
	 */
	private boolean isWindowListener = false;

	/** enable selection of several objects. */
	private boolean multiSelection;

	/** objects currently selected */
	private int[] objectNums = null;

	/**
	 * constructs a new ObjectSelectionButton.
	 * 
	 * @param isMultiSelection
	 *            if true, several objects can be selected.
	 */
	public ObjectSelectionButton(boolean isMultiSelection) {
		super();
		multiSelection = isMultiSelection;
		update(true);
		addActionListener(this);
	}

	/**
	 * checks whether the OSB is in correct mode.
	 */
	private boolean checkMulti(boolean multi) {
		if (multiSelection && !multi) {
//			MessageDisplay.errorMsg(
//					AnimalTranslator.translateMessage("osbCalled1"),
//					MessageDisplay.PROGRAM_ERROR);
			return false;
		}
		if (!multiSelection && objectNums != null && objectNums.length > 1) {
//			MessageDisplay.errorMsg("osbCalled2", 
//					Integer.valueOf(objectNums.length),
//					MessageDisplay.PROGRAM_ERROR);
			return false;
		}
		return true;
	}

	/**
	 * sets the selected objects.
	 * <p>
	 * Called from storeAttributesInto.
	 */
	public void setObjectNums(int[] objectNumArray) {
	  int[] theArray = objectNumArray;
		// treat empty sets or sets that consists only of 0 as null sets
		if (theArray != null && (theArray.length == 0 ||
				(theArray.length == 1 && theArray[0] == 0)))
		  theArray = null;
		boolean changed = theArray != objectNums; // easy comparison
		objectNums = theArray;
		checkMulti(true);
		update(changed);
	}

	/**
	 * sets the selected object.
	 */
	public void setObjectNum(int objectNum) {
		checkMulti(false);
		boolean changed = getObjectNum() != objectNum;
		if (objectNum == 0)
			objectNums = null;
		else
			objectNums = new int[] { objectNum };
		update(changed);
	}

	public int[] getObjectNums() {
		checkMulti(true);
		return objectNums;
	}

	public int getObjectNum() {
		checkMulti(false);
		if (objectNums == null)
			return 0;

		return objectNums[0];
	}

	/**
	 * repaints the button.
	 */
	private void update(boolean changed) {
//		System.err.println("multiSelection: " +multiSelection +" getObjectNums: " + getObjectNums()
//				+ "detail:" + ((getObjectNums() != null) ? getObjectNums().length : -1)
//				+" single:" +getObjectNum());
		boolean multipleSelected = getObjectNums() != null && getObjectNums().length > 1;
		if (multiSelection && multipleSelected)
			setText(AnimalTranslator.translateMessage("osbSelectedMulti",
					getNumArrayString(getObjectNums())));
		else if (getObjectNum() != 0)
//		else if ((!multiSelection && getObjectNum() != 0))
			setText(AnimalTranslator.translateMessage("osbSelectedSingle",
					Integer.valueOf(getObjectNum())));
		else {
			if (multiSelection)
				setText(AnimalTranslator.translateMessage("osbRequestMulti"));
			else
				setText(AnimalTranslator.translateMessage("osbRequestSingle"));
		}
//			setText(AnimalTranslator.translateMessage("osbRequest",
//					(multiSelection ? "(s)" : "")));
		if (changed) {
			ItemEvent e = new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED,
					this, this.isSelected() ? ItemEvent.SELECTED
							: ItemEvent.DESELECTED);
			fireItemStateChanged(e);
		}
		// size may vary, therefore call for new layout
		newLayout();
	}

	/**
	 * returns the AnimatorEditor this OSB is in.
	 */
	private AnimatorEditor getParentEditor() {
		return (AnimatorEditor) SwingUtilities.getAncestorOfClass(
				AnimatorEditor.class, this);
	}

	/**
	 * recalculates the window's layout. The size of an OSB may vary, as the
	 * numbers of the selected objects are displayed.
	 */
	private void newLayout() {
		AnimatorEditor w = getParentEditor();
		if (w != null)
			w.pack();
	}

	/**
	 * deselects the currently active OSB and all objects selected by it. Resets
	 * the selection mode of the DrawWindow.
	 */
	public static void deselectActiveButton() {
		if (activeButton != null) {
			activeButton.setSelected(false);
			// if the previously selected objects are not deselected,
			// they are copied to a newly clicked OSB
      DrawWindow drawWin = AnimalMainWindow.getWindowCoordinator().getDrawWindow(false);
      drawWin.deselectAll();
			activeButton = null;
			drawWin.setExternalSelection(null);
		}
	}

	/**
	 * reacts to button press by setting the ObjectButtonSelectionMode in
	 * DrawCanvas.
	 */
	public void actionPerformed(ActionEvent e) {
		if (isSelected()) {
			if (!isWindowListener) {
				isWindowListener = true;
				getParentEditor().addWindowListener(new OSBAdapter());
			}
			deselectActiveButton();
			activeButton = this;
			AnimalMainWindow.getWindowCoordinator().getDrawWindow(true).setExternalSelection(this);
		} else
			deselectActiveButton();
		update(false); // as no objectNo change occured
	}

	/**
	 * @return true if the object could be set, false if it could not be set
	 *         because the step of the current Animator and the step of
	 *         DrawWindow(where the selection took place) don't match
	 */
	public boolean objectsSelected(int step,
			int[] objects) {
/*
		int thisStep = getParentEditor().getStep();
		if (step != thisStep) {
			JOptionPane.showMessageDialog(getParentEditor(),
					"Objects can only be selected from step " + thisStep
							+ ", not step " + step, "Can't select this object",
					JOptionPane.ERROR_MESSAGE);
					
			deselectActiveButton();
			return false;
		} else {
*/
			setObjectNums(objects);
			return true;
		//}
	}

	/**
	 * if the home window of the OSB is closed, it must be deselected.
	 */
	class OSBAdapter extends WindowAdapter {
		public void windowClosed(WindowEvent e) {
			if (activeButton == ObjectSelectionButton.this)// when shown or
														   // hidden, ensure
														   // that the button is
														   // not pressed down

				deselectActiveButton();
		}
	}

	/**
	 * returns a String containing all numbers included in <i>nums </i> ordered
	 * from smallest to biggest. Areas are returned as x-y, so { 1,3,4,5,8,9,12 }
	 * would return "1,3-5,8-9,12".
	 */
	public static String getNumArrayString(int[] nums) {
		// first sort the array, insertion sort.
	  int[] theArray = nums;
		if (theArray == null) {
		  theArray = new int[1];
		  theArray[0] = 1;
			return "1";
		} 
		Arrays.sort(theArray);
//			for (int a = 1; a < nums.length; a++) {
//				int b = a - 1;
//				int temp = nums[a];
//				while (b >= 0 && temp < nums[b]) {
//					nums[b + 1] = nums[b];
//					b--;
//				}
//				nums[b + 1] = temp;
//			}

		StringBuilder s = new StringBuilder();
		int startNum = -1; // start of current range
		int lastNum = -1; // last num in current range
		for (int a = 0; a <= theArray.length; a++) {
			int newNum = (a < theArray.length ? theArray[a] : -1); // append one
			// dummy
			// required as the check is needed twice but lastNum may change
			// meanwhile
			boolean series = (newNum == lastNum + 1); // continuing a
			// series?
			boolean written = false; // something has been appended to the
			// buffer
			if (series)
				lastNum++;
			else { // at the end, because of the dummy element,
				//           it's never a series
				if (lastNum != -1) {
					if (startNum != lastNum)
						s.append(startNum + "-" + lastNum);
					else
						s.append(startNum);
					written = true;
				}
				startNum = newNum;
				lastNum = newNum;
			}
			if (written && a < theArray.length)
				s.append(',');
		}
		return s.toString();
	}

	public boolean hasMultiSelection() {
		return multiSelection;
	}

	/**
	 * checks whether all objects exist in the Animation. If not, deletes the
	 * non-existing. This method should be called by an Editor's isOK-Method. It
	 * may happen that an object was deleted without notifying the OSB.
	 * 
	 * @return true if all objects exist, false otherwise.
	 */
	public boolean checkObjects() {
		if (objectNums == null)
			return true;
		int deletedItems = 0;
		for (int a = 0; a < objectNums.length; a++) {
			// don't query the Animation, otherwise all new objects will
			// be inserted, even if they're temporary
			if (Editor.getGraphicObject(objectNums[a]) == null) {
				MessageDisplay.message("osbDeleted",
						Integer.valueOf(objectNums[a]));
				deletedItems++;
				objectNums[a] = 0;
			}
		}
		if (deletedItems == 0)
			return true;
		int[] newObjects = new int[objectNums.length - deletedItems];
		for (int a = objectNums.length - 1; a >= 0; a--)
			if (objectNums[a] != 0)
				newObjects[a - deletedItems] = objectNums[a];
			else
				deletedItems--;
		setObjectNums(newObjects);
		return false;
	}
}
