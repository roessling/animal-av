package animal.gui;

import java.awt.Point;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.UndoableEditSupport;

import animal.editor.graphics.meta.GraphicEditor;
import animal.graphics.PTGraphicObject;
import animal.misc.EditableObject;
import animal.misc.MSMath;

/**
 * AnimalUndoAdapter manages the Undo/Redo functionality of DrawCanvas (and
 * perhaps later more)
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling</a>
 * @version 1.0 03.09.1998
 */
public class AnimalUndoAdapter implements UndoableEditListener {

	/** collection of all events that can be undone */
	private UndoManager undoManager;

	/** dispatcher for the events. Dispatches to "this" */
	private UndoableEditSupport undoSupport;

	DrawCanvas dc;

	private InternPanel ip;

	AnimalUndoAdapter(DrawCanvas aDrawCanvas, InternPanel anInternalPanel) {
		dc = aDrawCanvas;
		ip = anInternalPanel;
		undoManager = new UndoManager();
		undoSupport = new UndoableEditSupport();
		undoSupport.addUndoableEditListener(this);
		refreshUndoRedo();
	}

	/**
	 * called when an action occures that can be undone.
	 */
	public void undoableEditHappened(UndoableEditEvent evt) {
		UndoableEdit edit = evt.getEdit();
		undoManager.addEdit(edit);
		refreshUndoRedo();
	}

	/**
	 * resets the buttons.
	 */
	public void refreshUndoRedo() {
		ip.undoButton.setToolTipText(undoManager.getUndoPresentationName());
		ip.undoButton.setEnabled(undoManager.canUndo());
		ip.redoButton.setToolTipText(undoManager.getRedoPresentationName());
		ip.redoButton.setEnabled(undoManager.canRedo());
	}

	void undo() {
		undoManager.undo();
		refreshUndoRedo();
	}

	void redo() {
		undoManager.redo();
		refreshUndoRedo();
	}

	/**
	 * no action can be undone or redone after calling reset.
	 */
	void reset() {
		undoManager.discardAllEdits();
		refreshUndoRedo();
	}

	/*****************************************************************************
	 * what actions to be undone?
	 ****************************************************************************/

	void move(int[] nums, int editPointNum, Point from, Point to) {
		if (from.equals(to))
			return;
		UndoableEdit edit = new MoveEdit(nums, editPointNum, from, to);
		undoSupport.postEdit(edit);
	}

	void insert(PTGraphicObject object) {
		UndoableEdit edit = new InsertEdit(object);
		undoSupport.postEdit(edit);
	}

	void insert(PTGraphicObject[] objects) {
		UndoableEdit edit = new InsertEdit(objects);
		undoSupport.postEdit(edit);
	}

	void delete(PTGraphicObject[] objects) {
		if (objects != null) {// only if actually objects were deleted
			UndoableEdit edit = new DeleteEdit(objects);
			undoSupport.postEdit(edit);
		}
	}

	/*****************************************************************************
	 * Classes to store the information necessary for undoing.
	 ****************************************************************************/

	class MoveEdit extends AbstractUndoableEdit {
		/**
		 * Comment for <code>serialVersionUID</code>
		 */
		private static final long serialVersionUID = -6684116853542352810L;

		private int[] objectNums;

		private int editPointNum;

		private Point from;

		private Point to;

		MoveEdit(int[] theObjectNums, int theEditPointNum, Point fromPoint, 
				Point toPoint) {
			objectNums = theObjectNums;
			editPointNum = theEditPointNum;
			from = fromPoint;
			to = toPoint;
		}

		public void undo() throws CannotUndoException {
			super.undo();
			for (int a = 0; a < objectNums.length; a++) {
				PTGraphicObject go = dc.getObjects().getGVEByNum(objectNums[a]).go;
				if (editPointNum <= 0) { // MovePoint
					Point diff = new Point(MSMath.diff(from, to));
					go.translate(diff.x, diff.y);
				} else { // ChangePoint
					// if Editor is primary, store the old object it pointed to
					EditableObject oldObject = go.getEditor().getCurrentObject(false);
					// link to the editor,
					go.getEditor().linkToEditor(go);
					// do the change,
					((GraphicEditor) go.getEditor()).nextPoint(editPointNum, from);
					// and restore the old object(only important for primary eds)
					go.getEditor().linkToEditor(oldObject);
				}
			}
			dc.repaintAll();
		}

		public void redo() throws CannotRedoException {
			super.redo();
			for (int a = 0; a < objectNums.length; a++) {
				PTGraphicObject go = dc.getObjects().getGVEByNum(objectNums[a]).go;
				if (editPointNum <= 0) { // MovePoint
					Point diff = new Point(MSMath.diff(from, to));
					go.translate(-diff.x, -diff.y);
				} else { // ChangePoint
					EditableObject oldObject = go.getEditor().getCurrentObject(false);
					go.getEditor().linkToEditor(go);
					((GraphicEditor) go.getEditor()).nextPoint(editPointNum, to);
					go.getEditor().linkToEditor(oldObject);
				}
			}
			dc.repaintAll();
		}

		public String getPresentationName() {
			return "Move";
		}

	} // MoveEdit

	// ***************************************************************

	class InsertEdit extends AbstractUndoableEdit {
		/**
		 * Comment for <code>serialVersionUID</code>
		 */
		private static final long serialVersionUID = -8344678427364013220L;

		PTGraphicObject[] objects;

		boolean cloned;

		InsertEdit(PTGraphicObject object) {
			objects = new PTGraphicObject[] { object };
			cloned = false;
		}

		InsertEdit(PTGraphicObject[] graphicalObjects) {
			objects = graphicalObjects;
			cloned = true;
		}

		public void undo() throws CannotUndoException {
			super.undo();
			if (objects != null)
				for (int a = 0; a < objects.length; a++)
					dc.getObjects().removeElement(objects[a]);
			// delete the object visually
			if (dc.getGraphicEditor() != null)
				dc.getGraphicEditor().createObject();
			dc.repaintAll();
		}

		public void redo() throws CannotRedoException {
			super.redo();
			if (objects != null)
				for (int a = 0; a < objects.length; a++)
					dc.getObjects().addElement(objects[a], GraphicVectorEntry.CREATED);
			dc.repaintAll();
		}

		public String getPresentationName() {
			if (cloned)
				return "Clone";

			return "Insert";
		}

	} // InsertEdit

	// ***************************************************************

	/**
	 * objects that were deleted will be restored only in the DrawCanvas but no
	 * Animator that worked on them will be restored up to now.
	 */
	class DeleteEdit extends AbstractUndoableEdit {
		/**
		 * Comment for <code>serialVersionUID</code>
		 */
		private static final long serialVersionUID = -4671083947785992899L;

		PTGraphicObject[] deletedObjects;

		DeleteEdit(PTGraphicObject[] objects) {
			this.deletedObjects = objects;
		}

		public void undo() throws CannotUndoException {
			super.undo();
			for (int a = 0; a < deletedObjects.length; a++)
				dc.getObjects().addElement(deletedObjects[a],
						GraphicVectorEntry.CREATED);
			dc.repaintAll();
		}

		public void redo() throws CannotRedoException {
			super.redo();
			for (int a = 0; a < deletedObjects.length; a++)
				dc.getObjects().removeElement(deletedObjects[a]);
			dc.repaintAll();
		}

		public String getPresentationName() {
			return "Delete";
		}

	} // DeleteEdit

}
