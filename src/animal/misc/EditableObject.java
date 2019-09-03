package animal.misc;

import animal.editor.Editor;
import animal.main.Animal;
import animal.main.PropertiedObject;

/**
 * <code>EditableObject</code> is an object that can use an editor.
 * <p>
 * Base class for <code>PTGraphicObject</code>, <code>Animator</code> and
 * <code>Link</code>.
 * <p>
 * Provides all features needed for using an editor.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling</a>
 * @version 1.0 24.08.1998
 */
abstract public class EditableObject extends PropertiedObject implements
		Cloneable {
	/**
	 * the editor currently used.
	 */
	private transient Editor editor;

	/**
	 * clones the object and resets the editor. This must be done as otherwise a
	 * clone would refer to the same editor, but this editor wouldn't refer back
	 * to this object resulting in some "features" difficult to debug.(took me
	 * some time! :-( )
	 */
	public Object clone() {
		try {
			EditableObject o = (EditableObject) super.clone();
			o.resetEditor();
			return o;
		} catch (CloneNotSupportedException i) {
			// this shouldn't happen, since we're Cloneable
			throw new InternalError();
		}
	}

	/**
	 * returns the Editor for this object. if no Editor was specified, the default
	 * Editor is returned. So if an Editor for this class(not for the instance!)
	 * exists, a non-null reference is returned.
	 */
	public Editor getEditor() {
		if (editor == null)
			setDefaultEditor();
		return editor;
	}

	/**
	 * returns(if necessary, creates) a secondary editor for this object.
	 */
	public Editor getSecondaryEditor() {
		if (getEditor() == null)// no editor exists for this class

			return null;
		if (!editor.isPrimaryEditor())// current editor is already secondary

			return editor;

		return editor = editor.getSecondaryEditor(this);
	} // getSecondaryEditor

	/**
	 * relinks the object to its primary editor
	 */
	public void resetEditor() {
		// editor is null if no editor is implemented
		if (getEditor() != null && !editor.isPrimaryEditor()) {
			editor.setVisible(false); // should not be necessary!
			editor.dispose();
			editor = editor.getPrimaryEditor();
		}
	} // resetEditor

	/**
	 * resets the object to its default editor. This is done by determining the
	 * class' name and getting an appropriate editor.
	 */
	public final void setDefaultEditor() {
		String name = getClass().getName();

		int i;
		// remove package name: "animal.graphics.PTPolyline" -> "PTPolyline"
		if ((i = name.lastIndexOf('.')) != -1)
			name = name.substring(i + 1);
		// remove "PT"-prefix(or whatever the prefix is)
		// "PTPolyline" -> "Polyline"
		if (name.startsWith(Animal.GRAPHICOBJECTS_PREFIX))
			name = name.substring(Animal.GRAPHICOBJECTS_PREFIX.length());

		setEditor(Animal.get().getEditor(name));
	}

	public void setEditor(Editor anEditor) {
		editor = anEditor;
	}
}
