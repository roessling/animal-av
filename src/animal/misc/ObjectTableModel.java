package animal.misc;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import translator.AnimalTranslator;
import animal.animator.Animator;
import animal.animator.Show;
import animal.animator.TimedShow;
import animal.graphics.PTGraphicObject;
import animal.gui.AnimalMainWindow;
import animal.gui.DrawWindow;
import animal.gui.GraphicVectorEntry;
import animal.main.Animation;
import animal.main.AnimationState;

public class ObjectTableModel extends AbstractTableModel {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -4525246143284609287L;

	private static final String[] columnNames = new String[] {
			AnimalTranslator.translateMessage("otmID"),
			AnimalTranslator.translateMessage("otmType"),
			AnimalTranslator.translateMessage("otmVisible"),
			AnimalTranslator.translateMessage("otmInfo") };

	private Object[][] data = null;

	private int step = -1;

	private Animation animation;

	private Hashtable<String, Boolean> currentlyVisible = new Hashtable<String, Boolean>(
			503);

	public ObjectTableModel() {
		// do nothing
	}

	public void setAnimation(Animation anim) {
		animation = anim;
	}

	public void setStep(int stepNr) {
		if (step != stepNr) {
			step = stepNr;
			updateDataForStep();
		}
	}

	public void setObjects(Vector<PTGraphicObject> objects) {
		if (objects != null) {
			int pos, nr = objects.size();
			if (data == null)
				data = new Object[nr][columnNames.length];
			PTGraphicObject ptgo = null;
			for (pos = 0; pos < nr; pos++) {
				ptgo = objects.elementAt(pos);
				if (ptgo != null) {
					int ptgoNum = ptgo.getNum(false);
					data[pos][0] = String.valueOf(ptgoNum);
					data[pos][1] = ptgo.getType();
					data[pos][2] = Boolean.FALSE;
					data[pos][3] = ptgo.toString();
				}
			}
			updateDataForStep();
		}
	}

	public void updateDataForStep() {
		AnimationState stateNow = AnimalMainWindow.getWindowCoordinator()
				.getDrawWindow(false).getAnimationState();
		GraphicVectorEntry[] gves = stateNow.getCurrentObjects().convertToArray();
		currentlyVisible.clear();
		int pos, nr = gves.length;
		for (pos = 0; pos < nr; pos++)
			currentlyVisible.put(String.valueOf(gves[pos].go.getNum(false)),
					Boolean.TRUE);
		for (pos = 0; pos < data.length; pos++)
			if (currentlyVisible.containsKey(data[pos][0]))
				data[pos][2] = Boolean.TRUE;
			else
				data[pos][2] = Boolean.FALSE;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		if (data == null)
			return 0;
		return data.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		if (data == null || data.length < row || data[row].length < col)
			return null;
		return data[row][col];
	}

	/*
	 * JTable uses this method to determine the default renderer/ editor for each
	 * cell. If we didn't implement this method, then the last column would
	 * contain text ("true"/"false"), rather than a check box.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col) {
		return (col == 2);
	}

	/*
	 * Don't need to implement this method unless your table's data can change.
	 */
	public void setValueAt(Object value, int row, int col) {
		// determine whether there is an explicit animator for this object
		int i, chosenID = -1, targetIndex = 0;
		int[] animatorOIDs = null;
		Vector<Animator> helper = animation.getAnimatorsAtStep(step);
		Animator currentAnimator = null;
		boolean fittingAnimatorFound = false;
		int nrAnimatedObjects = 0;

		for (i = 0; i < helper.size() && !fittingAnimatorFound; i++) {
			currentAnimator = helper.elementAt(i);
			if (currentAnimator != null
					&& (currentAnimator instanceof Show || currentAnimator instanceof TimedShow)) {
				try {
					chosenID = Integer.parseInt((String) data[row][0]);
				} catch (NumberFormatException e) {
					AnimalTranslator.translateMessage("currentAnimator", data[row][0]);
				}
				if (currentAnimator instanceof Show
						|| currentAnimator instanceof TimedShow) {
					animatorOIDs = currentAnimator.getObjectNums();
					nrAnimatedObjects = animatorOIDs.length;

					for (int j = 0; j < animatorOIDs.length && !fittingAnimatorFound; j++) {
						fittingAnimatorFound = (animatorOIDs[j] == chosenID);
						if (fittingAnimatorFound)
							targetIndex = j;
					}
				}
			}
		}
		if (fittingAnimatorFound) {
			// case 1: animator works on exactly one object; then drop it!
			if (animatorOIDs.length == 1) {
				animation.deleteAnimator(currentAnimator);
			}
			// case 2: animator works on multiple object; excise the chosen one!
			int[] modifiedOIDs = new int[nrAnimatedObjects - 1];
			if (targetIndex > 0)
				System.arraycopy(animatorOIDs, 0, modifiedOIDs, 0, targetIndex);
			if (targetIndex != nrAnimatedObjects - 1)
				System.arraycopy(animatorOIDs, targetIndex + 1, modifiedOIDs,
						targetIndex, nrAnimatedObjects - targetIndex - 1);
			currentAnimator.setObjectNums(modifiedOIDs);
		} else {
			boolean showMode = ((Boolean) value).booleanValue();
			TimedShow ts = new TimedShow(step, chosenID, 0, (showMode) ? "show"
					: "hide", showMode);
			animation.insertAnimator(ts);
		}
		DrawWindow drawWin = AnimalMainWindow.getWindowCoordinator().getDrawWindow(
				false);
		drawWin.setChanged();
		drawWin.writeBack();

		data[row][col] = value;
		fireTableCellUpdated(row, col);
		// updateDataForStep();
	}
}
