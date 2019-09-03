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
 *         BBCode element for creating a level three headline.
 */
public class H3 extends BBCode {
  public static final String BB_CODE = "h3";

  @Override
  public List<Primitive> getPrimitives(String text, String baseIDRef) {
    p.add(l.newText(new Offset(0, 20, baseIDRef, AnimalScript.DIRECTION_SW),
        text, UUID.randomUUID().toString(), null,
        (TextProperties) s.getProperties(H3.BB_CODE)));
    return p;
  }

}
