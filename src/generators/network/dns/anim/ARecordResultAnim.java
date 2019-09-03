package generators.network.dns.anim;

import algoanim.primitives.generators.Language;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 *
 * Result returned if the name server queried is authoritative for the
 * domain and knows the ip address of the host asked for.
 */
public class ARecordResultAnim extends DNSResultAnim {

	/**
	 * Create a new result animation
	 * 
	 * @param lang The Language object the element is added to
	 * @param server The server item's label in the animation
	 * @param client The client item's label in the animation
	 * @param result The actual query result (ip address of the host)
	 */
	public ARecordResultAnim(Language lang, String server, String client, String result) {
		super(lang, server, client, result, resultType.A);
	}
}
