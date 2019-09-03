package generators.network.dns.helper;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 *
 * The final result of a DNS Query is always an ip address encapsulated in this class.
 */
public class DNSResultAddress implements DNSResult {
	// the ip address
	private String ip;
	
	/**
	 * Create a new result.
	 * 
	 * @param myIP The ip address returned by the server.
	 */
	DNSResultAddress(String myIP) {
		this.ip = myIP;
	}
	
	/**
	 * Get the address returned by the server.
	 * 
	 * @return The ip address
	 */
	public String getIP() {
		return this.ip;
	}
}
