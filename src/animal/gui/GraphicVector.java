package animal.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

import javax.swing.JOptionPane;

import translator.AnimalTranslator;
import animal.editor.graphics.meta.GraphicEditor;
import animal.graphics.PTGraphicObject;
import animal.main.Animation;
import animal.misc.EditPoint;
import animal.misc.MSMath;

/**
 * contains all currently available <code>PTGraphicObject</code> s and
 * information about them, like which objects are selected etc. Can perform
 * certain operations on all or on the selected objects. The topmost object has
 * the highest index. The entries of this Vector are of type
 * <b>GraphicVectorEntry </b>.
 * 
 * @see GraphicVectorEntry
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.0 18.07.1998
 */
public class GraphicVector {

    /** the size of the edit points */
    private static final int EDIT_POINT_SIZE = 6;

    /**
     * currently selected EditPoint. Required as getObjectAtEditPoint cannot
     * return both a GraphicVectorEntry and an EditPoint.
     */
    private EditPoint editPoint;

    /** the Vector containing all GraphicObjects */
    private Vector<GraphicVectorEntry> graphicObjects = null;

    /** allow selection of multiple PTObjects */
    private boolean multiSelection = false;

    /**
     * show objects that are only temporary, i.e. used by an Animator as
     * temporary objects.
     */
    private boolean showTempObjects = true;

    /** display Editors when object is selected. */
    private boolean useEditors = true;

    /**
     * initializes the GraphicVector.
     */
    public GraphicVector() {
        graphicObjects = new Vector<GraphicVectorEntry>();
    }

    /***************************************************************************
     * element access
     **************************************************************************/

    /**
     * adds a GraphicObject into the GraphicVector.
     * 
     * @param go
     *            the GraphicObject to be added
     * @param mode
     *            the mode of the GraphicObject. If <i>mode </i> is TEMPORARY,
     *            the object is set to be a temporary object.
     */
    public void addElement(PTGraphicObject go, int mode) {
        if (go != null) {
            GraphicVectorEntry gve;
            if (mode == GraphicVectorEntry.TEMPORARY) {
                // check if the object is already contained.
                if ((gve = getGVEByNum(go.getNum(true))) != null) {
                    // if so, set it to be a temporary object
                    gve.setTemporary(true);
                    return;
                }
            }
            gve = new GraphicVectorEntry(go, false, mode);
            graphicObjects.addElement(gve);
        }
    }

    public void addElement(GraphicVectorEntry gve) {
        graphicObjects.addElement(gve);
    }

    /**
     * returns the entry at position <i>index </i> as a <b>GraphicVectorEntry
     * </b>. This is just a wrapper.
     */
    public GraphicVectorEntry elementAt(int index) {
        if (graphicObjects != null && graphicObjects.size() > index)
            return graphicObjects.elementAt(index);
        return null;
    }

    /**
     * returns the entry at position <i>index </i> as a <b>GraphicVectorEntry
     * </b>. This is just a wrapper.
     */
    public GraphicVectorEntry[] convertToArray() {
        GraphicVectorEntry[] elems = new GraphicVectorEntry[graphicObjects
                .size()];
        graphicObjects.copyInto(elems);
        return elems;
    }

    /**
     * Heapsort sortiert ueber einen Heap, d.h. eine spezielle Baumstruktur
     * Komplexitaet: O(n log n)
     */
    public void heapsort(GraphicVectorEntry[] gve) {
        int k, n = gve.length - 1;
        if (gve.length > 1) {
            for (k = (n - (gve.length % 2)) >>> 1; k >= 0; k--) {
                downheap(gve, k, n);
            }
            while (n > 0) {
                GraphicVectorEntry tmp = gve[0];
                gve[0] = gve[n];
                gve[n] = tmp;
                n--;
                downheap(gve, 0, n); // re-establish Heap
            }
        }
    }

    public void downheap(GraphicVectorEntry[] gve, int innerNode, int nrElems) {
        int leftSon = (innerNode << 1) + 1, rightSon = leftSon + 1;
        int maxPos = leftSon;
        if (leftSon <= nrElems) {
            if (rightSon <= nrElems) {
                if (gve[rightSon].mustBeDrawnBefore(gve[leftSon]))
                    maxPos = rightSon; // max of both descendants
            }
            if (gve[maxPos].mustBeDrawnBefore(gve[innerNode])) {
                GraphicVectorEntry tmp = gve[innerNode];
                gve[innerNode] = gve[maxPos];
                gve[maxPos] = tmp;
                downheap(gve, maxPos, nrElems);
            }
        }
    }

    /**
     * returns the entry at position <i>index </i> as a <b>PTGraphicObject </b>.
     * This is just a wrapper.
     */
    public PTGraphicObject getGraphicObject(int index) {
    	if (elementAt(index) != null)
    		return elementAt(index).go;
    	return null;
    }

    /**
     * returns the GraphicVectorEntry containing the GraphicObject with number
     * <i>num </i>.
     * 
     * @return null, if the GraphicObject could not be found, the corresponding
     *         GraphicVectorEntry otherwise.
     */
    public GraphicVectorEntry getGVEByNum(int num) {
        GraphicVectorEntry gve;
        for (int i = 0; i < graphicObjects.size(); i++)
            if ((gve = elementAt(i)).go.getNum(false) == num)
                return gve;
        return null;
    }

    /**
     * removes all GraphicObjects from the GraphicVector. Called from
     * AnimationState.reset.
     */
    public void removeAllElements() {
        deselectAll(); // hide the editors
        graphicObjects.removeAllElements();
    }

    /**
     * removes the GraphicObject from the GraphicVector required by Show
     * 
     * @param go
     *            the GraphicObject to be removed.
     */
    public boolean removeElement(PTGraphicObject go) {
        for (int i = 0; i < graphicObjects.size(); i++)
            if (getGraphicObject(i) == go) {
                if (graphicObjects != null && graphicObjects.size() > i) {
                    graphicObjects.removeElementAt(i);
                    return true;
                }
            }
        return false;
    }

    /***************************************************************************
     * object selection and deselection
     **************************************************************************/

    /**
     * deselects all objects.
     */
    void deselectAll() {
        for (int a = 0; a < graphicObjects.size(); a++)
            elementAt(a).setSelected(false, useEditors);
    }

    /**
     * deselects all objects but the topmost. This is required for turning
     * multiSelection off, as then only the topmost selected object remains
     * selected.
     */
    void deselectAllButTop() {
        boolean topFound = false;
        // iterate from top to bottom
        for (int a = graphicObjects.size() - 1; a >= 0; a--) {
            GraphicVectorEntry e = elementAt(a);
            if (e.isSelected()) {
                if (topFound)
                    // if one selected object was already found, that one was
                    // the topmost, so this one may be deselected
                    e.setSelected(false, useEditors);
                else
                    topFound = true;
            }
        }
    }

    /**
     * return the number of the EditPoint hit when getObjectAtEditPoint was
     * called the last time.
     */
    int getEditPointNum() {
        return editPoint.num;
    }

    /**
     * return the position of the EditPoint hit when getObjectAtEditPoint was
     * called the last time.
     */
    Point getEditPointPos() {
        return new Point(editPoint.p);
    }

    /**
     * Get the PTGraphicObject that has an EditPoint at Point p. The position
     * and number of the Editpoint can be determined afterwards by calling
     * getEditPointPos and getEditPointNum
     * 
     * @see getEditPointPos
     * @see getEditPointNum
     */
    GraphicVectorEntry getObjectAtEditPoint(Point p) {
        // iterate GraphicObjects from top to bottom
        for (int b = getSize() - 1; b >= 0; b--) {
            GraphicVectorEntry gve = elementAt(b);
            // only EditPoints of selected objects are drawn, so only consider
            // selected objects.
            if (gve.selected) {
                EditPoint[] ep = ((GraphicEditor) gve.go.getEditor())
                        .getEditPoints(gve.go);

                // iterate EditPoints twice.
                // First consider the ChangePoints, then consider the Move-
                // Points. If a ChangePoint and a MovePoint coincide and only
                // the MovePoint is considered, the ChangePoint can never be
                // changed as it moves together with the MovePoint. But if
                // the ChangePoint is considered first, it can be moved
                // away from the MovePoint, then the object can be moved, then
                // the ChangePoint can be moved on top of the MovePoint again.
                for (int mode = 0; mode < 2; mode++) {
                    for (int a = 0; a < ep.length; a++) {
                        if (mode == 0 && ep[a].num > 0) { // ChangePoint
                            // distance is maximum-metric, as EditPoint is
                            // Rectangle
                            if (Math.max(Math.abs(ep[a].p.x - p.x), Math
                                    .abs(ep[a].p.y - p.y)) < EDIT_POINT_SIZE / 2) {
                                editPoint = ep[a];
                                return gve;
                            }
                        }
                        if (mode == 1 && ep[a].num <= 0) { // MovePoint
                            // hit? distance is Euklidian, as EditPoint is
                            // Circle
                            if (MSMath.dist(ep[a].p, p) < EDIT_POINT_SIZE) {
                                editPoint = ep[a];
                                return gve;
                            }
                        } // num?
                    } // for EditPoints
                } // for mode
            } // selected?
        } // for b
        // still no matching GraphicObject found
        return null;
    } // hitEditPoint

    /**
     * Select the object next to <i>p </i>, but not more distant than
     * <i>tolerance </i> pixels. If several objects have distance 0, the topmost
     * one is picked. As selected objects are placed atop of nonselected in the
     * DrawCanvas, they are considered first.
     * 
     * @return the number of the GraphicObject if actually an object was
     *         (de-)selected, <br>
     *         -1 otherwise, i.e. no object was found within the neighborhood of
     *         p.
     */
    int select(Point p, int tolerance) {
        int min = Integer.MAX_VALUE; // the smallest distance;
        int dist; // the distance of the current object
        GraphicVectorEntry gve; // for iteration
        GraphicVectorEntry next = null; // the object closest to p.

        /*
         * in the first run, only the selected objects are considered, in the
         * second run, only the nonselected ones.
         */
        for (int sel = 1; sel < 3; sel++) {
            // iterate from top to bottom.
            for (int a = getSize() - 1; a >= 0; a--) {
                gve = elementAt(a);
                if (gve.getGraphicObject().isObjectSelectable()) {
                  if ((sel == 1 && gve.isSelected())
                      || (sel == 2 && !gve.isSelected())) {
                    // the Editor is needed for getting the distance.
                    GraphicEditor e = ((GraphicEditor) gve.go.getEditor());
                    if (e != null) {
                      dist = e.getMinDist(gve.go, p);
                      if (dist < min) {
                        min = dist;
                        next = gve;
                      }
                    }
                  }
                } else
                  System.err.println("Object is internal - ignoring: " + gve);
            }
        }

        // no object found at all or object too far away.
        if (min > tolerance || next == null) {
            return -1;
        }

        // the object found is selected...
        select(next);

        // and its number returned.
        return next.go.getNum(true);
    } // select

    /**
     * select the objects contained in <i>objectNums </i>.
     * 
     * @param isOSBInit
     *            if true the objects are selected and not toggled.
     */
    void select(int[] objectNums, boolean isOSBInit) {
        if (objectNums == null)
            return;
        // if isOSBInit, all objects in this Vector are selected and
        // normally no objects would be deselected. But if an OSB is
        // clicked and an object was already selected, this object would
        // remain selected(and thus be added to the other OSB's objects).
        // This is only a good result, if no object was
        // selected before with this OSB. Otherwise, deselect all objects
        // first to assure that *only* the objects selected in the OSB
        // are selected in the DrawWindow.
        if (isOSBInit && objectNums != null && objectNums.length != 0)
            deselectAll();
        for (int a = 0; a < objectNums.length; a++) {
            GraphicVectorEntry gve = getGVEByNum(objectNums[a]);
            if (isOSBInit) {
                if (gve != null)
                    // may be null if OSB for an Animator from a different step
                    // is active
                    gve.setSelected(true, false /* ? */);
            } else
                select(gve);
        }
    }

    /**
     * selects the GraphicObject contained in the GraphicVectorEntry according
     * to the current selection mode, i.e. if multiSelection is not turned on,
     * all other objects are deselected and the new one selected, if
     * multiSelection is turned on, the object's selection state is toggled.
     */
    void select(GraphicVectorEntry gve) {
        if (!multiSelection) {
            deselectAll();
            if (gve != null)
                gve.setSelected(true, useEditors);
        } else {
            if (gve != null)
                gve.toggleSelected(useEditors);
        }
    }

    /**
     * selects all objects "in" the area <i>r </i>. An object is to be selected
     * if its bounding box and <i>r </i> intersect.
     */
    public void selectArea(Rectangle r) {
        if (r != null)
            for (int i = 0; i < getSize(); i++) {
                GraphicVectorEntry gve = elementAt(i);
                Rectangle bb = gve.go.getBoundingBox();
                if (r.intersects(bb))
                    // other possibility, for total containment of objects:
                    // if (r.contains(r.x,r.y) &&
                    //     r.contains(r.x+r.width,r.y+r.height))
                    gve.setSelected(true, useEditors);
            }
    }

    /**
     * Sets the mode for the following selection. <br>
     * 
     * @param usesEditors
     *            if toggled, the Editors of all selected objects are
     *            shown/hidden.
     * @param isMultiSelection
     *            if toggled, all objects are deselected except for the topmost
     *            selected(which doesn't matter when toggled from false to true,
     *            as then only one object was selected anyway).
     * @see DrawCanvas#setSelection
     */
    public void setSelectionMode(boolean select, boolean isMultiSelection,
    		boolean usesEditors) {
		if (!select) {
			deselectAll();
			resetAllEditors();
		}
		if (usesEditors != useEditors)
			setAllEditorsVisible(usesEditors);
		if (isMultiSelection != multiSelection)
			deselectAllButTop();
		multiSelection = isMultiSelection;
		useEditors = usesEditors;
    }

    /***************************************************************************
		 * painting
		 **************************************************************************/

    /**
     * draws all objects(if allowed) except for <i>dontDrawThis </i> into the
     * Graphic context <i>g </i>.
     * 
     * @see #isTempAllowed
     */
    void drawAllBut(Graphics g, PTGraphicObject dontDrawThis) {
        GraphicVectorEntry[] gves = convertToArray();
        heapsort(gves);
        for (int a = gves.length - 1; a >= 0; a--)
            if (gves[a].go != dontDrawThis && isTempAllowed(gves[a]))
                gves[a].go.paint(g);
    }

    /**
     * draws all objects(if allowed) except for the objects found in
     * <i>dontDrawThis </i> into the Graphic context <i>g </i>.
     * 
     * @see #isTempAllowed
     */
    void drawAllBut(Graphics g, GraphicVector dontDrawThis) {
        GraphicVectorEntry gve;
        for (int a = 0; a < getSize(); a++) {
            gve = elementAt(a);
            if (!dontDrawThis.contains(gve) && isTempAllowed(gve))
                gve.go.paint(g);
        }
    }

    /**
     * draws all objects that are selected and their EditPoints into the
     * GraphicContext.
     * 
     * @see animal.editor.graphics.meta.GraphicEditor#getEditPoints
     */
    void drawSelected(Graphics g) {
        GraphicVectorEntry[] gves = convertToArray();
        heapsort(gves);
        for (int b = gves.length - 1; b >= 0; b--) {
            GraphicVectorEntry gve = gves[b];
            if (gve.selected && isTempAllowed(gve)) {
                // first paint the object itself
                gve.go.paint(g);
                // then paint the EditPoints
                EditPoint[] ep = ((GraphicEditor) gve.go.getEditor())
                        .getEditPoints(gve.go);
                g.setColor(Color.black);
                for (int a = 0; a < ep.length; a++) {
                    if (ep[a].num <= 0)// for MovePoints, a Circle is drawn

                        g.drawOval(ep[a].p.x - 3, ep[a].p.y - 3,
                                EDIT_POINT_SIZE, EDIT_POINT_SIZE);
                    else
                        // for ChangePoints, a Rectangle is drawn

                        g.drawRect(ep[a].p.x - 3, ep[a].p.y - 3,
                                EDIT_POINT_SIZE, EDIT_POINT_SIZE);
                }
                // only check BoundingBoxes
                g.setColor(Color.gray);
                Rectangle r = gve.go.getBoundingBox();
                g.drawRect(r.x, r.y, r.width, r.height);
            }
        }
    } // drawSelection

    /**
     * draws all objects that are not selected into the Graphic context.
     */
    void drawUnselected(Graphics g) {
        for (int b = 0; b < getSize(); b++) {
            GraphicVectorEntry gve = elementAt(b);
            if (!gve.selected && isTempAllowed(gve))
                gve.go.paint(g);
        }
    } // drawUnselected

    /**
     * checks whether an object may be painted or not.
     * 
     * @return true if the object is to be shown, either because it's not
     *         temporary or because temporary Objects are to be drawn also.
     */
    private boolean isTempAllowed(GraphicVectorEntry gve) {
        return showTempObjects || !gve.isTemporary();
    }

    /***************************************************************************
     * operations on selected objects
     **************************************************************************/

    /**
     * deletes the selected objects if possible. If one of the objects is
     * animated(i.e. Animators exist that work on this object) the user is asked
     * whether he wants to delete all objects and their Animators, only the
     * unanimated objects or no objects at all.
     * 
     * @return an array containing all deleted objects.
     */
    PTGraphicObject[] deleteSelected() {
        Vector<PTGraphicObject> deleted = new Vector<PTGraphicObject>();
        int choice = 0; // by default, delete everything
        boolean animatedObjectsExist = false;
        GraphicVectorEntry gve;
        // iterate from top to bottom to check whether one of the selected
        // objects is animated.
        for (int a = getSize() - 1; a >= 0; a--) {
            gve = elementAt(a);
            Animation anim = Animation.get();
            int num = gve.go.getNum(false);
            if (gve.isSelected()
                    &&
                    // does an Animator exist on this object or is it used as a
                    // temporary object?
                    (anim.getAnimator(0, num, null) != null || 
                    		anim.isUsedTemporarily(num, 0))) {
                animatedObjectsExist = true;
                // if one animated object is found, cancel search
                break;
            }
        }
        if (animatedObjectsExist) {
            // query the user what to do
            String[] options = new String[] { 
            		AnimalTranslator.translateMessage("selectAll"),
            		AnimalTranslator.translateMessage("selectUnanimated"),
            		AnimalTranslator.translateMessage("selectNothing") };
            choice = JOptionPane.showOptionDialog(
            		AnimalMainWindow.getWindowCoordinator().getDrawWindow(false),
            		AnimalTranslator.translateMessage("deleteWithSelected"),
            		AnimalTranslator.translateMessage("deleteAnimators"), 0,
            		JOptionPane.QUESTION_MESSAGE, null, options,
            		options[0]);
            // Window closed or "delete nothing" selected
            if (choice == -1 || choice == 2)
                return null;
        }
        // now do the actual deletion
        for (int a = getSize() - 1; a >= 0; a--) {
            gve = elementAt(a);
            // delete the object if it is selected, no Animator exists or
            // the Animators are to be deleted.
            if (gve.selected
                    && (Animation.get().getAnimator(0, gve.go.getNum(false),
                            null) == null || choice == 0)) {
                // if a secondary editor exists,close it
                if (!gve.go.getEditor().isPrimaryEditor())
                    gve.go.getEditor().close();
                // for all selected objects, whether animated or not, delete
                // the GraphicVector entry...
                graphicObjects.removeElementAt(a);
                // ...and the Animation entry.
                Animation.get().deleteGraphicObject(gve.go.getNum(false));
                // and insert it into the Vector of deleted Objects
                deleted.addElement(gve.go);
            }
        }
        if (animatedObjectsExist)
            AnimalMainWindow.getWindowCoordinator().getAnimationOverview(false).initList(true);
        PTGraphicObject[] result = new PTGraphicObject[deleted.size()];
        deleted.copyInto(result);
        return result;

    } // deleteSelected

    /**
     * Move the selected graphic objects behind the unselected ones. The order
     * between the selected objects is preserved.
     */
    void moveToBack() {
        int counter;
        int a = getSize() - 1;
        // now iterate from top to bottom, but analogous to "moveToFront",
        // so cf. the "moveToFront" documentation
        for (counter = getSize() - 1; counter >= 0; counter--) {
            GraphicVectorEntry gve = elementAt(a);
            if (gve.selected) {
                graphicObjects.removeElementAt(a);
                graphicObjects.insertElementAt(gve, 0);
            } else
                a--;
        }
    }

    /**
     * Move the selected graphic objects before the unselected ones. The
     * internal order between the selected objects is preserved.
     */
    void moveToFront() {
        int counter; // count the GraphicObjects already checked
        int a = 0; // index of GraphicObject currently being checked
        // check each GraphicObject exactly once.
        for (counter = 0; counter < getSize(); counter++) {
            // but for accessing the Vector, use "a" instead
            GraphicVectorEntry gve = elementAt(a);
            if (gve.selected) {
                // move the Object to the end. After this, the object that
                // was at index a+1 now has index "a" and therefore this
                // index must be checked again
                graphicObjects.removeElementAt(a);
                graphicObjects.addElement(gve);
            } else
                // only increase the index if no object was found(s.a.)
                a++;
        }
    }

    /***************************************************************************
     * other methods
     **************************************************************************/

    /**
     * returns the number of GraphicObjects in the GraphicVector.
     */
    public int getSize() {
        return graphicObjects.size();
    }

    /**
     * removes temporary objects from the GraphicVector. Temporary objects are
     * only visible for one step.
     */
    public void nextStep() {
        for (int i = getSize() - 1; i >= 0; i--) {
            GraphicVectorEntry e = elementAt(i);
            if (e.isTemporary())
                graphicObjects.removeElementAt(i);
        }
    }

    /**
     * resets the Editors of all objects to their default primary editors.
     */
    public void resetAllEditors() {
        for (int a = 0; a < getSize(); a++) {
            elementAt(a).go.resetEditor();
        }
    }

    /**
     * show or hide all Editors according to the current <i>useEditors </i>
     * mode.
     */
    public void setAllEditorsVisible() {
        setAllEditorsVisible(useEditors);
    }

    /**
     * show or hide all Editors of the selected objects.
     * 
     * @param show
     *            if true, show the editors, otherwise hide them.
     */
    public void setAllEditorsVisible(boolean show) {
        for (int a = 0; a < getSize(); a++) {
            GraphicVectorEntry gve = elementAt(a);
            if (gve.selected)
                gve.setEditorVisible(show);
        }
    }

    /**
     * sets whether temporary objects are to be shown.
     */
    public void setShowTempObjects(boolean b) {
        showTempObjects = b;
    }

    /**
     * returns an int array of all selected objects.
     */
    public int[] getSelectedObjects() {
        int[] selected = new int[getSize()]; // big enough!
        int counter = 0;
        for (int a = 0; a < graphicObjects.size(); a++)
            if (elementAt(a).isSelected())
                selected[counter++] = elementAt(a).go.getNum(true);
        int[] result = new int[counter];
        for (int a = 0; a < counter; a++)
            result[a] = selected[a];
        return result;
    }

    public GraphicVector getSelectedObjectsVector() {
        GraphicVector result = new GraphicVector();
        for (int a = 0; a < graphicObjects.size(); a++) {
            GraphicVectorEntry gve = elementAt(a);
            if (gve.isSelected())
                result.addElement(gve);
        }
        return result;
    }

    public boolean contains(GraphicVectorEntry go) {
        return graphicObjects.contains(go);
    }

} // GraphicVector
