package generators.network.dns.helper;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 * A name server receives queries either from a client or another server and
 * answers them either from a local database or cache or by sending out information
 * to other name server.
 */
public abstract class NameServer implements DNSResult {
	/**
	 * The domain name the server is responsible for.
	 */
	protected String domain;
	
	/**
	 * Query the name server for a specific host or domain name.
	 * 
	 * @param hostname The host or domain to query for
	 * @return The query result
	 */
	public abstract DNSResult query(String hostname);
	
	/**
	 * Set the domain name of the server.
	 * 
	 * @param myDomain The new domain name this server is responsible for
	 */
	public void setDomain(String myDomain) {
		domain = myDomain;
	}
	
	/**
	 * Get the domain name the server is responsible resolving
	 * 
	 * @return The domain name
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Generate a random ip address from the private 10.0.0.0/8 class A network. 
	 * 
	 * @return The ip address generated
	 */
	protected final String randomAddress() {
		StringBuilder address = new StringBuilder();
		
		// set first octet to 10 for an address from a 10.0.0.0/8 network
		address.append(10);
		// generate all other octets randomly
		for(int i = 1; i < 4; i++) {
			address.append(".");
			address.append((int) ((Math.random() * 254) + 1)) ;
		}
		return address.toString();
	}

}
