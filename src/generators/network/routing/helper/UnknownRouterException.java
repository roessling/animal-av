package generators.network.routing.helper;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 * Thrown if access to a non existing router is requested
 */
public class UnknownRouterException extends Exception {
	private static final long serialVersionUID = 6209569932552818961L;

	@Override
	public String toString() {
		return "This Router Object is not known in the Network.";
	}
}