package algoanim.primitives;

import java.util.Iterator;
import java.util.LinkedList;

import algoanim.primitives.generators.GroupGenerator;

/**
 * Extends the API with the opportunity to group <code>Primitive</code>s 
 * to be able to call methods on the whole group.
 * 
 * @author jens
 */
public class Group extends Primitive {
	private GroupGenerator generator = null;
	private LinkedList<Primitive> primitives;
	
	/**
     * Instantiates the <code>Group</code> and calls the create() method
     * of the associated <code>GroupGenerator</code>.	 
	 * @param g the appropriate code <code>Generator</code>.
	 * @param primitiveList the primitives to add to the group.
	 * @param name	 the groups name.	 
	 */
	public Group(GroupGenerator g, LinkedList<Primitive> primitiveList, String name) {
		super(g, null);
		generator = g;
		primitives = new LinkedList<Primitive>();
		
		if (primitiveList != null) {
			Iterator<Primitive> i = primitiveList.listIterator();
			while (i.hasNext()) {
				Primitive p = i.next();
				primitives.add(p);
			}
		}	
		
		setName(name);
		generator.create(this);
	}
	
	/**
	 * Removes a certain <code>Primitive</code> from the group.
	 * @param p the <code>Primitive</code> to remove.
	 */
	public void remove(Primitive p) {
		if(p != null) {
			this.primitives.remove(p);
		}
	}

	/**
	 * Returns the contained <code>Primitive</code>s.
	 * @return the <code>Primitive</code>s belonging to this group.
	 */
	public LinkedList<Primitive> getPrimitives() {
		return primitives;
	}
}
