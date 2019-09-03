package generators.network.dns.helper;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 *
 * A root name server can only return entries for a top level domain.
 * This might either be the ip address of an entry or more likely a
 * link to another name server which is responsible for resolving the domain
 */
public class RootNameServer extends NameServer {
	
	/**
	 * Create a new root name server
	 */
	public RootNameServer() {
		domain = ".";
	}

	@Override
	public DNSResult query(String hostname) {
		int index = hostname.lastIndexOf(".");
		if(index >= 0) {
			String dom = hostname.substring(index + 1); 
			return new IterativeNameServer(dom);
		} else {
			String res = new String();
			try {
				InetAddress ip;
				ip = InetAddress.getByName(hostname);
				res = ip.getHostAddress();
			} catch (UnknownHostException e) {
				res = randomAddress();
			}
			return new DNSResultAddress(res);

		}
	}

	/**
	 * The domain for root name server is fixed. So no need actually to set it.
	 */
	@Override
	public final void setDomain(String myDomain) {
		// domain for root name server is fixed
	}
}
