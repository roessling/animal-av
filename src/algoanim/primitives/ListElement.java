package algoanim.primitives;

import java.util.LinkedList;

import algoanim.primitives.generators.ListElementGenerator;
import algoanim.properties.ListElementProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;

/**
 * Represents an element of a list, for example a <code>LinkedList</code>.
 * @author Stephan Mehlhase 
 */
public class ListElement extends Primitive {
	private ListElementProperties properties;
	private ListElementGenerator generator = null;
	private ListElement prev, next;
	private Node upperLeft;
	private int pointers;
	
	// list of Nodes, Primitives, nulls
	private LinkedList<Object> ptrLocations;

	/**
     * Instantiates the <code>ListElement</code> and calls the create() 
     * method
     * of the associated <code>ListElementGenerator</code>.	 
	 * @param leg the appropriate code <code>Generator</code>.
	 * @param upperLeftCorner the upper left corner of this 
	 * 	<code>ListElement</code>.
	 * @param nrPointers the number of pointers starting at this
	 * <code>ListElement</code>.
	 * @param pointerLocations [optional] the targets of the pointers starting at 
	 * 	this <code>ListElement</code>.
	 * @param prevElement [optional] the predecessor of this 
	 * 	<code>ListElement</code>.
	 * @param name the name of this <code>ListElement</code>.
	 * @param display [optional] the <code>DisplayOptions</code> of this
	 * <code>ListElement</code>.
	 * @param lp [optional] the properties of this 
	 * 	<code>ListElement</code>.
	 */
	public ListElement(ListElementGenerator leg, Node upperLeftCorner, 
			int nrPointers, LinkedList<Object> pointerLocations, ListElement prevElement,
			String name, DisplayOptions display, ListElementProperties lp) {
      this(leg, upperLeftCorner, nrPointers, pointerLocations, prevElement, null,
          name, display, lp);
    }
    
    /**
     * Instantiates the <code>ListElement</code> and calls the create() 
     * method
     * of the associated <code>ListElementGenerator</code>.  
     * @param leg the appropriate code <code>Generator</code>.
     * @param upperLeftCorner the upper left corner of this 
     *  <code>ListElement</code>.
     * @param nrPointers the number of pointers starting at this
     * <code>ListElement</code>.
     * @param pointerLocations [optional] the targets of the pointers starting at 
     *  this <code>ListElement</code>.
     * @param prevElement [optional] the predecessor of this 
     *  <code>ListElement</code>.
     * @param name the name of this <code>ListElement</code>.
     * @param display [optional] the <code>DisplayOptions</code> of this
     * <code>ListElement</code>.
     * @param lp [optional] the properties of this 
     *  <code>ListElement</code>.
     */
    public ListElement(ListElementGenerator leg, Node upperLeftCorner, 
            int nrPointers, LinkedList<Object> pointerLocations, ListElement prevElement,
            ListElement nextElement, String name, DisplayOptions display, 
            ListElementProperties lp) {
      super(leg, display);
		
		properties = lp;
		upperLeft = upperLeftCorner;
		prev = prevElement;
        next = nextElement;
		
		this.setPointers(nrPointers);
		
		this.generator = leg;
		ptrLocations = pointerLocations;
		this.setName(name);
		this.generator.create(this);
	}
	
	/**
	 * Sets the number of pointers.
	 * @param pointers the number of pointers to create.
	 */
	private void setPointers(int nrPointers) {
		if (nrPointers >= 0 && nrPointers < 256) {
			pointers = nrPointers;
		} else {
			pointers = 0;
		}		
	}
	
	/**
	 * Returns the number of pointers of this <code>ListElement</code>.
	 * @return the number of the pointers of this <code>ListElement</code>.
	 */
	public int getPointers() {
		return pointers;
	}
	
	/**
	 * Sets the targets of the pointers belonging to this
	 * <code>ListElement</code>.
	 * 
	 * @param pointerLocations the pointer targets as a list. each list item
	 * 	may be null, another <code>Primitive</code>
	 * 	or a <code>Node</code>.
	 */
	public void setPointerLocations(LinkedList<Object> pointerLocations) {
		ptrLocations = pointerLocations;
	}
	
	/**
	 * Returns the targets of the pointers.
	 * @return the target of the pointers.
	 */
	public LinkedList<Object> getPointerLocations() {
		return ptrLocations;
	}

	/**
	 * Returns the properties of this <code>ListElement</code>.
	 * @return the properties of this <code>ListElement</code>.
	 */
	public ListElementProperties getProperties() {
		return properties;
	}

	/**
	 * Returns the upper left corner of this <code>ListElement</code>.
	 * @return the upper left corner of this <code>ListElement</code>.
	 */
	public Node getUpperLeft() {
		return upperLeft;		
	}
	
	/**
	 * Returns the predecessor of this <code>ListElement</code>.
	 * @return the predecessor of this <code>ListElement</code>.
	 */
	public ListElement getPrev() {
		return prev;
	}
    
    /**
     * Returns the successor of this <code>ListElement</code>.
     * @return the successor of this <code>ListElement</code>.
     */
    public ListElement getNext() {
        return next;
    }

	
	/**
	 * Targets the pointer specified by <code>linkno</code> to another
	 * <code>ListElement</code>.
	 * @param target the <code>ListElement</code> to target 
	 * 	with this link.
	 * @param linkno the link to change.
	 * @param offset [optional] the offset until the operation is 
	 * started.
	 * @param duration [optional] the duration of this operation.
	 */
	public void link(ListElement target, int linkno, Timing offset, Timing duration) {
		if(linkno <= pointers && linkno >= 0)
			this.generator.link(this, target, linkno, offset, duration);
	}
	
	/**
	 * Removes the pointer specified by <code>linkno</code>.
	 * @param linkno the pointer to remove.
	 * @param offset [optional] the offset until the operation is 
	 * 	started.
	 * @param duration [optional] the duration of this operation.
	 */
	public void unlink(int linkno, Timing offset, Timing duration) {
		if(linkno <= pointers && linkno >= 0)
			this.generator.unlink(this, linkno, offset, duration);
	}
	
	/**
	 * @see algoanim.primitives.Primitive#setName(java.lang.String)
	 */
	public void setName(String newName) {
		properties.setName(newName);
		super.setName(newName);
	}	
}
