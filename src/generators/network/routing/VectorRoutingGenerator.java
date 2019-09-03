package generators.network.routing;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.network.AbstractGraphGenerator;
import generators.network.helper.ClassName;
import generators.network.routing.helper.AbstractRoutingFactory;
import generators.network.routing.helper.AbstractRoutingTable;
import generators.network.routing.helper.Network;
import generators.network.routing.helper.Route;
import generators.network.routing.helper.Router;
import generators.network.routing.impl.dvr.DistanceVectorFactory;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.InfoBox;
import algoanim.animalscript.addons.bbcode.NetworkStyle;
import algoanim.primitives.Graph;
import algoanim.primitives.Primitive;
import algoanim.primitives.generators.Language;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 *
 * A generator for all routing methods.
 */
public class VectorRoutingGenerator extends AbstractGraphGenerator {
	/**
	 * the routing method used in this instance 
	 */
	private AbstractRoutingFactory method;
	
	/**
	 * Create a new generator with the default locale and the default routing method
	 */
	public VectorRoutingGenerator() {
		this(Locale.GERMANY, new DistanceVectorFactory());
	}
	
	/**
	 * Create a new generator
	 * 
	 * @param myLocale The locale to be used for translations
	 * @param method The routing method to be used
	 */
	public VectorRoutingGenerator(Locale myLocale, AbstractRoutingFactory method) {
		this.method = method;
		textResource = ClassName.getPackageAsPath(this) + "resources/" + method.getName() + "Generator";
		locale = myLocale;
		translator = new Translator(textResource, locale);
	}
	
	/**
	 * Create a new generator using the given language object.
	 * This constructor is used internally to setup the animation after 
	 * the initial preparation of any primitives.
	 * 
	 * @param lang The language object to work on
	 * @param myLocale The locale to be used for translations
	 * @param method The routing method to be used
	 */
	private VectorRoutingGenerator(Language lang, Locale myLocale, AbstractRoutingFactory method) {
		this(myLocale, method);
		
		s = new NetworkStyle();
		
		l = lang;
		l.setStepMode(true);
	}

	@Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		// get the graph created by the user
		Graph g = getGraphFromPrimitives(primitives); 
		
		// Create a new animation
		init();
		VectorRoutingGenerator anim = new VectorRoutingGenerator(l, locale, method);
		
		// build headline 
		anim.getHeader();
		l.nextStep(translator.translateMessage("LBL_TITLE_SLIDE"));
		
		// build title
		anim.getTitleSlide();
		
		g = anim.getGraph(g);
		
		// create info box for routing messages but hide it for now
		InfoBox i = new InfoBox(l, new Offset(-20, 100, g, AnimalScript.DIRECTION_SW), g.getSize(), translator.translateMessage("ROUTING_UPDATE_INFO_HL"));
		i.hide();
		
		l.nextStep(translator.translateMessage("LBL_ANIM_START"));
		anim.run(g, i);

		return l.toString();
	}
	
	/**
	 * Create the actual routing tables and the animation
	 * 
	 * @param g The graph to build the network from
	 * @param i The info box to display any updates
	 */
	private void run(Graph g, InfoBox i) {
		// build the network from our graph
		Network n = new Network(g, method);
		
		// build the routing tables
		Node position = new Offset(50, 5, g, AnimalScript.DIRECTION_NE);
		for(Router thisRouter : n.getRouters()) {
			// highlight the current router
			thisRouter.highlight(null, null);
			
			Primitive t = thisRouter.createView(l, position, null, s, translator.translateMessage("ROUTING_TABLE_HL", thisRouter.toString()), n.getRouters().size());
			// calculate position for next table
			position = new Offset(-5, 20, t, AnimalScript.DIRECTION_SW);
			
			// unhighlight the current router
			l.nextStep(translator.translateMessage("LBL_CREATE_TABLE", thisRouter.toString()));
			thisRouter.unhighlight(null, null);
		}
		
		l.nextStep();
		
		boolean update = true;
		while(update) {
			// no updates (yet)
			update = false;

			/*
			 * Step 1: send new routes to neighbor
			 */
			for(Router thisRouter : n.getRouters()) {
				// highlight the current router
				thisRouter.highlight(null, null);

				// show current updates in info box
				List<String> infoBoxUpdate = new ArrayList<String>();
				AbstractRoutingTable t = thisRouter.getUpdates();
				for(Router thisTarget : t.getTargets()) {
					Route targetRoute = t.getShortestRoute(thisTarget);
					infoBoxUpdate.add(thisTarget + " via " + targetRoute.toString() + ": " + targetRoute.getCost());
				}
				i.setHeadline(translator.translateMessage("ROUTING_UPDATE_INFO_HL", new Object[]{thisRouter}));
				i.setText(infoBoxUpdate);
				i.show();
				
				l.nextStep();
				
				// send routing updates to each neighbor (if there are any)
				if(t.size() > 0) {
					Set<Router> myNeighbors = thisRouter.getNeighbors();
					for(Router thisNeighbor : myNeighbors) {
						// highlight link over which message is sent
						thisRouter.getLink(thisNeighbor).highlight(null, null);
						
						// highlight the neighbor currently updating
						thisNeighbor.highlight(null, null);
						
						// send route
						thisRouter.sendUpdates(thisNeighbor);
						
						// unhighlight neighbor and link
						l.nextStep(translator.translateMessage("LBL_SEND_UPDATE", thisRouter.toString(), thisNeighbor.toString()));
						thisNeighbor.unhighlight(null, null);
						thisRouter.getLink(thisNeighbor).unhighlight(null, null);
					}
				}
				
				// unhighlight the current router
				thisRouter.unhighlight(null, null);
				
				// hide info box after updates were sent
				i.hide();
			}
			
			// take a break
			l.nextStep();

			/*
			 * Step 2: update routing tables
			 */
			for(Router thisRouter : n.getRouters()) {
				// highlight the current router
				thisRouter.highlight(null, null);
				
				// update routing table
				update = thisRouter.updateTable(true) || update;
				
				// unhighlight router
				l.nextStep(translator.translateMessage("LBL_UPDATE_TABLE", thisRouter.toString()));
				thisRouter.unhighlight(null, null);
			}
			
			/*
			 * Step 3: commit changes so they get visible
			 */
			for(Router thisRouter : n.getRouters()) {
				thisRouter.commitTable();
			}
		}
	}
}
