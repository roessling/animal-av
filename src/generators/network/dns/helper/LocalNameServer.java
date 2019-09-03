package generators.network.dns.helper;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 * A local name server always does recursive queries to other name servers.
 * It does not have a domain name as it never resolves any lookups directly (except when cached)
 */
public class LocalNameServer extends NameServer {
	
	/**
	 * @return Always returns null as it can not resolve domains directly.
	 */
	@Override
	public DNSResult query(String hostname) {
		return null;
	}

	@Override
	public final void setDomain(String myDomain) {
		// for a local nameserver no domain can be set
	}
}
