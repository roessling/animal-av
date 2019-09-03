package generators.network.dns.anim;

import java.awt.Color;
import java.awt.Font;
import java.util.UUID;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.MultiPrimitiveAnim;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 *
 * Create a new query result view within the animation.
 */
public abstract class DNSResultAnim extends MultiPrimitiveAnim {
	/**
	 * Possible result types.
	 */
	protected enum resultType {A, NS};
	
	/**
	 * Create a new result animation
	 * 
	 * @param lang The Language object the element is added to
	 * @param server The server item's label in the animation
	 * @param client The client item's label in the animation
	 * @param result The actual query result (depends on the result type)
	 * @param type The result type
	 */
	DNSResultAnim(Language lang, String server, String client, String result, resultType type) {
		super(lang);

		UUID uid = UUID.randomUUID();

		// arrow properties
		PolylineProperties lineProps = new PolylineProperties();
		lineProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		
		// text label properties
		TextProperties labelProps;
		labelProps = new TextProperties();
		labelProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		labelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		
		// add arrow
		Node[] points = {new Offset(0, 0, server, AnimalScript.DIRECTION_W), new Offset(0, 0, client, AnimalScript.DIRECTION_E)};
		p.add(l.newPolyline(points, "localQueryArrow" + uid, null, lineProps));
		
		// add label
		p.add(l.newText(new Offset(25, 19, "localQueryArrow" + uid, AnimalScript.DIRECTION_SW), "TYPE: " + type, "lblLocalQueryType" + uid, null, labelProps));
		p.add(l.newText(new Offset(0, -13, "lblLocalQueryType" + uid, AnimalScript.DIRECTION_NW), "NAME: " + result, "lblLocalQueryName" + uid, null, labelProps));
		p.add(l.newText(new Offset(0, 1, "lblLocalQueryType" + uid, AnimalScript.DIRECTION_SW), "CLASS: IN", "lblLocalQueryClass" + uid, null, labelProps));
	}	
}
