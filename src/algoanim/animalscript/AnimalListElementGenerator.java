package algoanim.animalscript;

import java.util.ListIterator;

import algoanim.primitives.ListElement;
import algoanim.primitives.Primitive;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.ListElementGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ListElementProperties;
import algoanim.util.Node;
import algoanim.util.Timing;

/**
 * @see algoanim.primitives.generators.ListElementGenerator
 * @author Stephan Mehlhase
 */
public class AnimalListElementGenerator extends AnimalGenerator implements
		ListElementGenerator {
	private static int count = 1;

	/**
	 * @param aLang
	 *          the associated <code>Language</code> object.
	 */
	public AnimalListElementGenerator(Language aLang) {
		super(aLang);
	}

	/**
	 * @see algoanim.primitives.generators.ListElementGenerator
	 *      #create(algoanim.primitives.ListElement)
	 */
	public void create(ListElement e) {
		// Check Name, if used already, create a new one silently
		if (this.isNameUsed(e.getName()) || e.getName() == "") {
			e.setName("ListElement" + AnimalListElementGenerator.count);
			AnimalListElementGenerator.count++;
		}
		lang.addItem(e);

		StringBuilder def = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
		def.append("listelement \"").append(e.getName() + "\" ");
		def.append(AnimalGenerator.makeNodeDef(e.getUpperLeft()));

		ListElementProperties props = e.getProperties();

		String text = (String)props.get(AnimationPropertiesKeys.TEXT_PROPERTY);
		if (text != null) {
			def.append(" text \"").append(text).append("\"");
		}

		def.append(" pointers ").append(e.getPointers());
		def.append(" position ");
        Integer o2 = (Integer)props.get(AnimationPropertiesKeys.POSITION_PROPERTY);
        switch(o2.intValue()) {
        case AnimationPropertiesKeys.LIST_POSITION_NONE:
          def.append("none");
          break;
        case AnimationPropertiesKeys.LIST_POSITION_RIGHT:
          def.append("right");
          break;
        case AnimationPropertiesKeys.LIST_POSITION_LEFT:
          def.append("left");
          break;
        case AnimationPropertiesKeys.LIST_POSITION_TOP:
          def.append("top");
          break;
        default:
          def.append("bottom");
        }
        def.append(" ");

		// writes pointer definitions as long as the loop does not reach
		// the number of the assigned pointers and the list of pointer location
		// definitions is not exhausted.
		if (e.getPointerLocations() != null) {
			int i = 1;
			ListIterator<Object> li = e.getPointerLocations().listIterator();
			int pointers = e.getPointers();

			while (i <= pointers && li.hasNext()) {
				Object o = li.next();

				// either use a Node definition as pointer location
				// or a "to targetID" definition, or skip the location for
				// the current pointer, if the entry in the LinkedList is null.
				if (o instanceof Node) {
					def.append(" ptr").append(i).append(" ").append(AnimalGenerator.makeNodeDef((Node) o));
				} else if (o instanceof Primitive) {
					def.append(" ptr").append(i).append(" to \"").append(((Primitive) o).getName()).append("\" ");
				}
				i++;
			}

		}

		if (e.getPrev() != null) {
			def.append(" prev \"").append(e.getPrev().getName()).append("\" ");
		}
        
		if (e.getNext() != null) {
		  def.append(" next \"").append(e.getNext().getName()).append("\" ");
		}

		addColorOption(props, def);
		addColorOption(props, AnimationPropertiesKeys.BOXFILLCOLOR_PROPERTY, " boxFillColor ", def);
    addColorOption(props, AnimationPropertiesKeys.POINTERAREACOLOR_PROPERTY, " pointerAreaColor ", def);
    addColorOption(props, AnimationPropertiesKeys.POINTERAREAFILLCOLOR_PROPERTY, " pointerAreaFillColor ", def);
    addColorOption(props, AnimationPropertiesKeys.TEXTCOLOR_PROPERTY, " textColor ", def);
		addIntOption(props, AnimationPropertiesKeys.DEPTH_PROPERTY, " depth ", def);

		lang.addLine(def);
	}

	/**
	 * @see algoanim.primitives.generators.ListElementGenerator #link(
	 *      algoanim.primitives.ListElement,
	 *      algoanim.primitives.ListElement, int,
	 *      algoanim.util.Timing, algoanim.util.Timing)
	 */
	public void link(ListElement start, ListElement target, int linkNr, Timing t,
			Timing d) {
	  StringBuilder sb = new StringBuilder(128);
	  sb.append("setLink \"").append(start.getName()).append("\" link ");
	  sb.append(linkNr).append(" to \"").append(target.getName());
	  sb.append("\" ");
	  addWithTiming(sb, t, d);
	}

	/**
	 * @see algoanim.primitives.generators.ListElementGenerator #unlink(
	 *      algoanim.primitives.ListElement, int,
	 *      algoanim.util.Timing, algoanim.util.Timing)
	 */
	public void unlink(ListElement start, int linkNr, Timing t, Timing d) {
	  StringBuilder sb = new StringBuilder(128);
    sb.append("clearLink \"").append(start.getName()).append("\" link ");
    sb.append(linkNr);
    addWithTiming(sb, t, d);
	}
}
