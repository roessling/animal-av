package algoanim.animalscript;

import java.util.Iterator;
import java.util.LinkedList;

import algoanim.primitives.Group;
import algoanim.primitives.Primitive;
import algoanim.primitives.generators.GroupGenerator;
import algoanim.primitives.generators.Language;

/**
 * @see algoanim.primitives.generators.GroupGenerator
 * @author jens
 */
public class AnimalGroupGenerator extends AnimalGenerator implements
		GroupGenerator {
	private static int count = 1;

	/**
	 * @param aLang
	 *          the associated <code>Language</code> object.
	 */
	public AnimalGroupGenerator(Language aLang) {
		super(aLang);
	}

	/**
	 * @see algoanim.primitives.generators.GroupGenerator
	 *      #create(algoanim.primitives.Group)
	 */
	public void create(Group g) {
		String name = g.getName();
		if (name == null || name == "" || this.isNameUsed(name)) {
			g.setName("Group" + AnimalGroupGenerator.count);
			name = g.getName();
			AnimalGroupGenerator.count++;
		}
		lang.addItem(g);

		StringBuilder def = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
		def.append("group \"").append(name).append("\" ");
		LinkedList<Primitive> primitives = g.getPrimitives();
		if (primitives != null) {
			Iterator<Primitive> i = primitives.listIterator();
			while (i.hasNext()) {
				Primitive p = i.next();
				def.append("\"").append(p.getName()).append("\" ");
			}
		}
		lang.addLine(def);
	}

	/**
	 * @see algoanim.primitives.generators.GroupGenerator #remove(
	 *      algoanim.primitives.Group,
	 *      algoanim.primitives.Primitive)
	 */
	public void remove(Group g, Primitive p) {
	  StringBuilder sb = new StringBuilder(128);
	  sb.append("remove \"").append(g.getName()).append("\" \"");
	  sb.append(p.getName()).append("\"");
	  lang.addLine(sb.toString());
	}
}
