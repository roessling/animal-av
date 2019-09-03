package algoanim.animalscript.addons.bbcode;

import java.util.List;
import java.util.UUID;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Offset;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 * BBCode element for creating source code listings.
 */
public class Code extends MultilineBBCode {
	public static final String BB_CODE = "code";

	@Override
	public List<Primitive> getPrimitives(String text, String baseIDRef) {
		SourceCode source = l.newSourceCode(new Offset(0, 5, baseIDRef, AnimalScript.DIRECTION_SW), 
		    UUID.randomUUID().toString(), null, (SourceCodeProperties)s.getProperties(Code.BB_CODE));
		
		// add each single line to the element
		String[] elems = brSplit(text);
		for (int i = 0; i < elems.length; i++) {			
			source.addCodeLine(elems[i], "", 0, null);
		}
		
		p.add(source);
		return p;
	}
}
