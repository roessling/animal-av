package algoanim.animalscript.addons.bbcode;

import java.util.List;
import java.util.UUID;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Primitive;
import algoanim.properties.TextProperties;
import algoanim.util.Offset;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 * BBCode element for creating a copyright notice.
 */
public class Copy extends BBCode {
	public static final String BB_CODE = "copy";

	@Override
	public List<Primitive> getPrimitives(String text, String baseIDRef) {		
		p.add(l.newText(new Offset(0, 20, baseIDRef, AnimalScript.DIRECTION_S), 
		    text, UUID.randomUUID().toString(), null, 
		    (TextProperties)s.getProperties(Copy.BB_CODE)));
		return p;
	}

}
