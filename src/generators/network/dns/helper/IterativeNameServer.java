package generators.network.dns.helper;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 *
 * A iterative name server is responsible for returning an ip address
 * for every hostname within its domain or if the query concerns a subdomain
 * return the address of the name server responsible.
 */
public class IterativeNameServer extends NameServer {

	/**
	 * Create a name server for the specific domain.
	 * 
	 * @param myDomain The domain the name server is responsible for
	 */
	public IterativeNameServer(String myDomain) {
		domain = myDomain;
	}

	@Override
	public DNSResult query(String hostname) {
		// we only take lower case
		String hostname2 = hostname;
    hostname2 = hostname2.toLowerCase();
		
		// get everything but the hostname (everything after the first dot)
		String dom = hostname2.substring(hostname2.indexOf(".") + 1);
		
		String res = new String();

		if(dom.equals(domain)) {
			try {
				InetAddress ip;
				ip = InetAddress.getByName(hostname2);
				res = ip.getHostAddress();
			} catch (UnknownHostException e) {
				res = randomAddress();
			}
			return new DNSResultAddress(res);
		} else {
			// get only the local part of the hostname
			String subDom = hostname2.substring(0, hostname2.length() - this.domain.length() - 1);
			// get the subdomain
			subDom = subDom.substring(subDom.lastIndexOf(".") + 1);
			
			return new IterativeNameServer(subDom.concat(".".concat(this.domain)));
		}
	}
}
