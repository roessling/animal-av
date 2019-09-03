package generators.network.dns.anim;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.UUID;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.MultiPrimitiveAnim;
import algoanim.primitives.Primitive;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 *
 * Shows a query for a host or domain name happening within the domain name system.
 * This could either be between client and name server or between a recursive name 
 * server and another server. 
 */
public class DNSQueryAnim extends MultiPrimitiveAnim {

	/**
	 * Create a new animation element.
	 * 
	 * @param lang The Language object to add the element to
	 * @param client The client item's name in the animation
	 * @param server The server item's name in the animation
	 * @param query The actual query string (host or domain name)
	 */
	public DNSQueryAnim(Language lang, String client, String server, String query) {
		super(lang);
		l = lang;
		p = new ArrayList<Primitive>();

		UUID uid = UUID.randomUUID();
		
		// arrow properties
		PolylineProperties lineProps = new PolylineProperties();
		lineProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		
		//label properties
		TextProperties labelProps;
		labelProps = new TextProperties();
		labelProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		labelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));

		// create the arrow
		Node[] points = {new Offset(0, 0, client, AnimalScript.DIRECTION_E), new Offset(0, 0, server, AnimalScript.DIRECTION_W)};
		p.add(l.newPolyline(points, "localQueryArrow" + uid, null, lineProps));
		
		// create the label
		p.add(l.newText(new Offset(25, 19, "localQueryArrow" + uid, AnimalScript.DIRECTION_SW), "QTYPE: A", "lblLocalQueryType" + uid, null, labelProps));
		p.add(l.newText(new Offset(0, -13, "lblLocalQueryType" + uid, AnimalScript.DIRECTION_NW), "QNAME: " + query, "lblLocalQueryName" + uid, null, labelProps));
		p.add(l.newText(new Offset(0, 1, "lblLocalQueryType" + uid, AnimalScript.DIRECTION_SW), "QCLASS: IN", "lblLocalQueryClass" + uid, null, labelProps));
	}	
}
