package generators.network.routing.anim;

import generators.network.routing.helper.Router;

import java.util.UUID;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.bbcode.Matrix;
import algoanim.animalscript.addons.bbcode.Style;
import algoanim.primitives.generators.Language;
import algoanim.properties.MatrixProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 *
 * Displays the routing entries for a router network using vectors as routes.
 * Only one (often the shortest) route is displayed.
 * This is most commonly used in Path Vector Routing.
 */
public class RoutingVectorView extends RoutingView {

	/**
	 * Create a new view
	 * 
	 * @param lang The Language object to add the route to
	 * @param upperLeft The anchor element on the animation frame
	 * @param display Any animal display options for the view
	 * @param style The style object used to create a consistent view
	 * @param headline The headline text of the view
	 * @param size The maximum size of the routing table
	 * @param router The router this view is created for 
	 */
	public RoutingVectorView(Language lang, Node upperLeft, DisplayOptions display, Style style, String headline, int size, Router router) {
		super(lang, upperLeft, display, style, headline, size, router);
		
		String[][] data = new String[size][2];
	
		// fill everything with the empty string (just to have a clean table)
		for(int i = 0; i < data.length; i++) {
			for(int j = 0; j < data[i].length; j++) {
				data[i][j] = "";
			}
		}
		
		// build initial view
		count = 0;
		for(Router thisRouter : myself.getNeighbors()) {
			knownRouters.add(count, thisRouter);
			data[count][0] = thisRouter.toString();
			data[count][1] = myself + " → " + myself.getRoutes().getShortestRoute(thisRouter) + " (" + String.valueOf(myself.getLink(thisRouter).cost) + ")";
			count++;
		}
		m = l.newStringMatrix(new Offset(5, 5, hl, AnimalScript.DIRECTION_SW), data, "RoutingTable" + UUID.randomUUID().toString().replace("-", ""), display, (MatrixProperties)style.getProperties(Matrix.BB_CODE));
	}
	
	@Override
	public void updateView(Router target, Router via, int distance, boolean singleStep) {
		if(!knownRouters.contains(target)) {
			knownRouters.add(count, target);
			m.put(count, 0, target.toString(), null, null);
			count++;
		}
		
		// update table
		m.put(knownRouters.indexOf(target), 1, myself + " → " + via + " → " + via.getRoutes().getShortestRoute(target) + " (" + String.valueOf(distance) + ")", null, null);
		
		// highlight updates
		m.highlightCell(knownRouters.indexOf(target), 1, null, null);
		if(singleStep) {
			l.nextStep();
		}
	}
}
