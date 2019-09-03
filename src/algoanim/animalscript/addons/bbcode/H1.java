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
 *         BBCode element for creating a level one headline.
 */
public class H1 extends BBCode {
  public static final String BB_CODE = "h1";

  @Override
  public List<Primitive> getPrimitives(String text, String baseIDRef) {
    p.add(l.newText(new Offset(0, 20, baseIDRef, AnimalScript.DIRECTION_SW),
        text, UUID.randomUUID().toString(), null,
        (TextProperties) s.getProperties(H1.BB_CODE)));
    return p;
  }

}
