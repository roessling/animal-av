package generators.network.routing.impl.dvr;

import generators.network.routing.anim.RoutingTableView;
import generators.network.routing.anim.RoutingView;
import generators.network.routing.helper.AbstractRoutingFactory;
import generators.network.routing.helper.AbstractRoutingTable;
import generators.network.routing.helper.Route;
import generators.network.routing.helper.Router;
import generators.network.routing.helper.SingleHopRoute;
import algoanim.animalscript.addons.bbcode.Style;
import algoanim.primitives.generators.Language;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 *
 * Factory to return the correct classes needed for Distance Vector Routing
 */

public class DistanceVectorFactory extends AbstractRoutingFactory {
	@Override
	public AbstractRoutingTable getNewRoutingTable(Router myself) {
		return new DistanceVectorRoutingTable(myself);
	}	
	
	@Override
	public Route getNewRoute(Object via, int cost) {
		return new SingleHopRoute(via, cost);
	}

	@Override
	public String getName() {
		return "DistanceVectorRouting";
	}

	@Override
	public RoutingView getNewView(Language l, Node upperLeft, DisplayOptions display, Style s, String headline, int size, Router r) {
		return new RoutingTableView(l, upperLeft, display, s, headline, size, r);
	}
}