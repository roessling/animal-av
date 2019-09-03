package generators.network.routing.impl.pvr;

import generators.network.routing.anim.RoutingVectorView;
import generators.network.routing.anim.RoutingView;
import generators.network.routing.helper.AbstractRoutingFactory;
import generators.network.routing.helper.AbstractRoutingTable;
import generators.network.routing.helper.MultiHopRoute;
import generators.network.routing.helper.Route;
import generators.network.routing.helper.Router;
import algoanim.animalscript.addons.bbcode.Style;
import algoanim.primitives.generators.Language;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 *
 * Factory to return the correct classes needed for Path Vector Routing
 */
public class PathVectorFactory extends AbstractRoutingFactory {
	@Override
	public AbstractRoutingTable getNewRoutingTable(Router myself) {
		return new PathVectorRoutingTable(myself);
	}	
	
	@Override
	public Route getNewRoute(Object via, int cost) {
		return new MultiHopRoute(via, cost);
	}

	@Override
	public String getName() {
		return "PathVectorRouting";
	}

	@Override
	public RoutingView getNewView(Language l, Node upperLeft, DisplayOptions display, Style s, String headline, int size, Router r) {
		return new RoutingVectorView(l, upperLeft, display, s, headline, size, r);
	}
}
