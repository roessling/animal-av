package animal.gui;

import javax.swing.JOptionPane;

import translator.AnimalTranslator;
import animal.graphics.PTGraphicObject;

/**
 * GraphicVectorEntry is an entry for a GraphicVector. It contains a
 * GraphicObject, information about its selection and temporary state. As these
 * variables are accessed very often, they are not private but "as public as
 * necessary". So every object can read and write them without having to call
 * methods.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling</a>
 * @version 1.0 18.07.1998
 */
public class GraphicVectorEntry {

	/*****************************************************************************
	 * mode variables
	 ****************************************************************************/

	/**
	 * indicates that this object was created in this step and has not yet been
	 * inserted into the Animation
	 */
	public static final int CREATED = 1;

	/** the object was not animated between its creation and now */
	public static final int UNTOUCHED = 2;

	/**
	 * the object was created in an earlier step and has been animated meanwhile
	 */
	public static final int ANIMATED = 4;

	/**
	 * the object is a temporary object, i.e. it will be used in this step only
	 * and be discarded afterwards. Animators use temporary objects, e.g. "Move"
	 * to specify the Line along which to move. TEMPORARY is ORed to the other
	 * mode variables, so you have to pass just one parameter.
	 */
	public static final int TEMPORARY = 8;

    /**
     * the object is an internal object: it should not be selectable in the graphic
     * front-end. This is used for composite objects composed of other primitives;
     * it allows the individual animation of components without making their internal
     * elements visible to the outside environment.
     */
    public static final int INTERNAL = 16;

	/**
	 * the graphicObject stored in this GraphicVectorEntry. Used quite often in
	 * DrawWindow and GraphicVector, so don't make this private
	 */
	public PTGraphicObject go = null;

	/*
	 * selection status of the GraphicObject. Used quite often in GraphicVector,
	 * so don't make this private
	 */
	boolean selected = false;

	/**
	 * indicates when the object was created and how long it will exist one of
	 * CREATED, UNTOUCHED, ANIMATED.
	 */
	int mode = 0;

	/**
	 * no-arg constructor required for serialization.
	 */
	public GraphicVectorEntry() {
		// do nothing
	}

	public boolean mustBeDrawnBefore(GraphicVectorEntry other) {
		return (go.getDepth() > other.go.getDepth() || (go.getDepth() == other.go
				.getDepth() && go.getNum(false) < other.go.getNum(false)));
	}

	/**
	 * constructs a new GraphicVectorEntry
	 */
	public GraphicVectorEntry(PTGraphicObject aGO, boolean isSelected, 
			int numericMode) {
		go = aGO;
		selected = isSelected;
		mode = numericMode;
	}

	/**
	 * shows the GraphicObject's secondary Editor, if it was not yet animated.
	 * Animated objects must not show an Editor as their attributes can't be
	 * changed after having been animated.
	 */
	void setEditorVisible(boolean b) {
		if (b && mode == ANIMATED) {
			JOptionPane.showMessageDialog(AnimalMainWindow.getWindowCoordinator()
					.getDrawWindow(false),
					AnimalTranslator.translateMessage("modified"),
					AnimalTranslator.translateMessage("notPossible"),
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		go.getSecondaryEditor().setVisible(b);
	}

	/**
	 * sets the object's selection status. If requested, the Editor is shown.
	 */
	void setSelected(boolean isSelected, boolean showEditors) {
		if (showEditors && isSelected != selected)
			// only if really a change occured
			// Otherwise, all secondary editors are generated, which
			// is superfluous. show/hide corresponding editor
			setEditorVisible(isSelected);
		selected = isSelected;
	}

	/**
	 * returns the object's selection status.
	 */
	boolean isSelected() {
		return selected;
	}

	public int getMode() {
		return mode;
	}
	
	/**
	 * toggles the object's selection status and returns the new status.
	 */
	boolean toggleSelected(boolean showEditors) {
		setSelected(!selected, showEditors);
		return selected;
	}

	/**
	 * returns whether the object is temporary.
	 */
	public boolean isTemporary() {
		return (mode & TEMPORARY) != 0;
	}

	/**
	 * sets whether the object is temporary or not by toggling the TEMPORARY bit
	 * in <i>mode</i>.
	 */
	public void setTemporary(boolean b) {
		if (b)
			mode |= TEMPORARY;
		else {
			mode -= (mode & TEMPORARY);
		}
	}

	/**
	 * indicates that the object is animated.
	 */
	public void setAnimated() {
		mode = ANIMATED;
	}

	public PTGraphicObject getGraphicObject() {
		return go;
	}
}
