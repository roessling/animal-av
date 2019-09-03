package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;

public class OPTICS implements Generator {
	private Language lang;
	private List<P> db;
	private CircleProperties ordered_circ_prop;
	private Integer circle_radius;
	private CircleProperties auswertung_circ_prop_highlight;
	private CircleProperties auswertung_circ_prop;
	private RectProperties auswertung_bar_prop_highlight;
	private RectProperties auswertung_bar_prop;
	private PolylineProperties currdist_propRED;
	private boolean draw_reachabilty = true;
	private boolean draw_cluster_creation = true;
	private boolean drawReachabilityNetwork_immediatly = true;
	private CircleProperties circCore_prop = new CircleProperties();
	private LinkedList<Circle> minpts_circles = new LinkedList<Circle>();
	private SourceCode scOptics;
	private SourceCode scUpdate;
	private boolean drawTempCoredist;
	private int limit_red_line;

	@SuppressWarnings("serial")
	private class P extends Point {
		public P(int x, int y) {
			super(x, y);
		}

		@Override
		public String toString() {
			return "[" + x + "," + y + "] \td:" + reachability_distance + "\n";
		}

		static final double UNDEFINED = java.lang.Double.MAX_VALUE;
		double reachability_distance = UNDEFINED;
		boolean processed = false;
		public Circle reachabilityCircle = null;
		public Polyline predLine;
		public P pred;
	}

	private static PriorityQueue<P> newSeeds() {
		// initial size (1st parameter) must not be zero!!
		// (although one cannot know which size is needed)
		return new PriorityQueue<P>(1, new Comparator<P>() {
			@Override
			public int compare(P arg0, P arg1) {
				return (arg0.reachability_distance < arg1.reachability_distance ? -1 : 1);
			}
		});
	}

	/**
	 * test, if nth next point is close enough to core
	 * 
	 * @param p
	 * @param eps
	 * @param minpts
	 * @return
	 */
	private double core_dist(final P p, double eps, int minpts) {
		int nTh_closest = minpts;
		circCore_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
		circCore_prop.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.red);
		circCore_prop.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		// corepoint
		minpts_circles.add(lang.newCircle(new Coordinates(p.x, p.y), circle_radius - 1, "", null, circCore_prop));

		// do not modify the parameter
		ArrayList<P> all = new ArrayList<P>(db);
		all.remove(p);
		// sorted list ordered by dist(p) ?
		Collections.sort(all, new Comparator<P>() {
			@Override
			public int compare(P arg0, P arg1) {
				return (p.distance(arg0) < p.distance(arg1) ? -1 : 1);
			}
		});
		P p_nth = all.get(nTh_closest);
		double coredist = p.distance(p_nth);
		{
			// show cohesion next to center
			for (int i = 0; i < Math.min(all.size(), nTh_closest); i++) {
				P curr = all.get(i);
				minpts_circles.add(lang.newCircle(new Coordinates(curr.x, curr.y), circle_radius / 2, "", null, circCore_prop));
			}
		}
		// coredist umkreis
		circCore_prop.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
		minpts_circles.add(lang.newCircle(new Coordinates(p.x, p.y), (int) Math.round(coredist), "coredist", null, circCore_prop));
		return coredist;
	}

	/**
	 * get all points inside the epsilon-environment of SELF
	 * 
	 * @param self
	 * @param eps
	 * @return unordered list of points
	 */
	private List<P> neighbors(P self, double eps) {
		LinkedList<P> nachbarn = null;
		if (eps == Double.MAX_VALUE) {
			nachbarn = new LinkedList<P>(db);
		} else {
			nachbarn = new LinkedList<P>();
			for (P p2 : db)
				if (self.distance(p2) < eps)
					nachbarn.add(p2);
		}
		// never ever the point itself should be its own neighbor
		nachbarn.remove(self);
		return nachbarn;
	}

	private LinkedList<P> optics(final List<P> p_db, final double eps, final int minpts) {
		lang.nextStep();

		this.db = p_db;
		// INIT
		for (P p : db)
			p.reachability_distance = P.UNDEFINED;

		// normal list, ordered just by the time of appending
		LinkedList<P> orderedList = new LinkedList<P>();
		scOptics.toggleHighlight(0, 3);
		lang.nextStep();
		for (P p : db) {
			// IF is not in pseudocode, because inside for-loop
			if (!p.processed) {
				scOptics.toggleHighlight(3, 4);
				scOptics.highlight(5);
				scOptics.highlight(6);
				scOptics.highlight(7);
				lang.nextStep();
				final List<P> n = neighbors(p, eps);
				p.processed = true;
				orderedList.add(p);
				// anfangspunkt ist beliebig. Bei großen epsilon wird nur ein einziger Punkt auf diese Art in die
				// orderedList eingefügt.
				PriorityQueue<P> seeds = newSeeds();

				scOptics.unhighlight(4);
				scOptics.unhighlight(5);
				scOptics.unhighlight(6);
				scOptics.unhighlight(7);
				scOptics.highlight(8);
				lang.nextStep();
				if (core_dist(p, eps, minpts) != P.UNDEFINED) {
					scOptics.toggleHighlight(8, 9);
					update(n, p, seeds, eps, minpts);

					scOptics.toggleHighlight(9, 10);
					lang.nextStep();
					// care for right use of priority queue!
					for (P q = seeds.poll(); !seeds.isEmpty(); q = seeds.poll()) {
						scOptics.toggleHighlight(10, 11);
						scOptics.highlight(12);
						scOptics.highlight(13);
						lang.nextStep();
						final List<P> n_ = neighbors(q, eps);
						q.processed = true;
						orderedList.add(q);
						scOptics.unhighlight(11);
						scOptics.unhighlight(12);
						scOptics.toggleHighlight(13, 14);
						lang.nextStep();
						if (core_dist(q, eps, minpts) != P.UNDEFINED) {
							scOptics.toggleHighlight(14, 15);
							update(n_, q, seeds, eps, minpts);
							scOptics.unhighlight(15);
						}
						scOptics.unhighlight(14);
					}
					scOptics.unhighlight(10);
				}
				scOptics.unhighlight(8);
				if (p.reachabilityCircle != null)
					p.reachabilityCircle.hide();
			}
			scOptics.highlight(3);
		}
		//		System.out.println(orderedList);
		return orderedList;
	}

	private void update_reachability_for(P p, double value, P core) {
		p.reachability_distance = value;
		p.pred = core;

		if (drawReachabilityNetwork_immediatly) {
			Coordinates[] points = { new Coordinates(p.x, p.y), new Coordinates(core.x, core.y) };
			PolylineProperties lineProp = new PolylineProperties();
			lineProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
			if (p.predLine != null)
				p.predLine.hide();
			p.predLine = lang.newPolyline(points, "", null, lineProp);
		}
		if (draw_reachabilty) {
			// ln oder sin alternativ?
			float s = Math.min(1, (float) (limit_red_line / p.reachability_distance));
			// (float) (1 - 1 / p.reachability_distance);

			// any kind of blue
			float farbwert = (float) (210 / 360.0);
			if (p.processed)
				farbwert = (float) (60 / 360.0);// yellow
			Color c = Color.getHSBColor(farbwert, s, 1);

			ordered_circ_prop = new CircleProperties();
			ordered_circ_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
			ordered_circ_prop.set(AnimationPropertiesKeys.FILL_PROPERTY, c);
			ordered_circ_prop.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			if (p.reachabilityCircle != null)
				p.reachabilityCircle.hide();

			p.reachabilityCircle = lang.newCircle(new Coordinates(p.x, p.y), circle_radius, "", null, ordered_circ_prop);
		}
	}

	private void update(final List<P> n, final P core, PriorityQueue<P> seeds, final double eps, final int minpts) {
		scUpdate.show();
		scUpdate.highlight(1);
		lang.nextStep();

		double coredist = core_dist(core, eps, minpts);
		scUpdate.toggleHighlight(1, 2);
		lang.nextStep();
		for (P o : n) {
			scUpdate.toggleHighlight(2, 3);
			lang.nextStep();
			if (!o.processed) {
				scUpdate.toggleHighlight(3, 4);
				lang.nextStep();

				// not part of the algorithm
				if (coredist < core.distance(o) && drawTempCoredist) {
					Coordinates[] dist_line_coordinates = { new Coordinates(core.x, core.y), new Coordinates(o.x, o.y) };
					Polyline dist_line = lang.newPolyline(dist_line_coordinates, "", null, currdist_propRED);
					lang.nextStep();
					dist_line.hide();
				}

				// reachabilty eines punktes entspricht der ihres clusters, deshalb: max(coredist, dist(p,o))
				double new_reach_dist = Math.max(coredist, core.distance(o));
				scUpdate.toggleHighlight(4, 5);
				lang.nextStep();
				if (o.reachability_distance == P.UNDEFINED) {
					scUpdate.toggleHighlight(5, 6);
					scUpdate.highlight(7);
					lang.nextStep();
					scUpdate.unhighlight(6);
					scUpdate.unhighlight(7);

					update_reachability_for(o, new_reach_dist, core);
					seeds.offer(o);
				} else {
					// check for improvement
					scUpdate.toggleHighlight(5, 8);
					lang.nextStep();
					scUpdate.unhighlight(8);

					if (new_reach_dist < o.reachability_distance) {
						scUpdate.highlight(9);
						scUpdate.highlight(10);
						update_reachability_for(o, new_reach_dist, core);
						{
							// Seeds.move-up(o, new-reach-dist)
							seeds.remove(o);
							seeds.offer(o);
						}
						scUpdate.unhighlight(9);
						scUpdate.unhighlight(10);
					}
				}
			}
			scUpdate.unhighlight(3);
		}

		scUpdate.hide();
		for (Circle c : minpts_circles)
			c.hide();
	}

	/**
	 * @return some test data
	 */
	private List<P> makeClouds(int[][] cloudSpec) {
		ArrayList<P> l = new ArrayList<P>();
		for (int i = 0; i < cloudSpec.length; i++) {
			l.addAll(newCloud(cloudSpec[i][0], cloudSpec[i][1], cloudSpec[i][2], cloudSpec[i][3]));
		}
		Collections.shuffle(l);
		return l;
	}

	private List<P> newCloud(int centerX, int centerY, int cloud_radius, int count) {
		if (draw_cluster_creation) {
			CircleProperties cluster_prop = new CircleProperties();
			cluster_prop.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
			cluster_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
			cluster_prop.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
			lang.newCircle(new Coordinates(centerX, centerY), cloud_radius, "", null, cluster_prop);
			cluster_prop.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			// lang.newCircle(new Coordinates(centerX, centerY), 3, "", null, cluster_prop);
		}

		List<P> l = new LinkedList<P>();
		for (int i = 0; i < count; i++) {
			// random * max. radius * 2
			double dist = (int) Math.floor(Math.random() * cloud_radius);
			double deg = (int) Math.floor(Math.random() * 360);
			double x_ = Math.sin(deg) * dist;
			double y_ = Math.cos(deg) * dist;

			int x = centerX + (int) Math.floor(x_);
			int y = centerY + (int) Math.floor(y_);
			P p = new P(x, y);
			l.add(p);
		}
		return l;
	}

	@Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> thePrimitives) {
	  Hashtable<String, Object> primitives = thePrimitives;
		int chart_start_x = 50;
		int chart_start_y = 380;
		limit_red_line = 20;
		SourceCodeProperties code_properties = new SourceCodeProperties();

		// falls mit MAIN gestartet => prepare primitives
		if (primitives == null) {
			primitives = new Hashtable<String, Object>();
			primitives.put("min_pts", new Integer(4));
			primitives.put("circle_radius", 4);
			primitives.put("drawTempCoredist", false);
			primitives.put("drawClusteringWithSteps", false);
			primitives.put("drawReachabilityNetwork_immediately", false);
			currdist_propRED = new PolylineProperties();
			currdist_propRED.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
			int[][] cluster = { { 70, 70, 60, 10 }, // oben links
					{ 210, 130, 50, 10 } // unten rechts
					, { 460, 120, 100, 15 } }; // oben rechts

			primitives.put("db", cluster);
			code_properties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.red);
		} else {
			code_properties = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
			limit_red_line = (int)(((Double) primitives.get("limit_red_line")).doubleValue());
		}
		{
			// INIT
			// Store the language object
			lang = new AnimalScript("OPTICS", getAnimationAuthor(), 1000, 800);
			// This initializes the step mode. Each pair of subsequent steps has
			// to
			// be divdided by a call of lang.nextStep();
			lang.setStepMode(true);
			scOptics = lang.newSourceCode(new Coordinates(20, chart_start_y), "optics", null, code_properties);
			scOptics.addCodeLine("OPTICS(DB, eps, MinPts)", "optics_head", 0, null);
			scOptics.addCodeLine("for each point p of DB", "optics_init", 1, null);
			scOptics.addCodeLine("p.reachability-distance = UNDEFINED", "optics_init", 2, null);
			scOptics.addCodeLine("for each unprocessed point p of DB", "optics_for", 1, null);
			scOptics.addCodeLine("N = getNeighbors(p, eps)", "optics_processed", 2, null);
			scOptics.addCodeLine("mark p as processed", "optics_processed", 2, null);
			scOptics.addCodeLine("output p to the ordered list", "optics_processed", 2, null);
			scOptics.addCodeLine("Seeds = empty priority queue", "optics_processed", 2, null);
			scOptics.addCodeLine("if (core-distance(p, eps, Minpts) != UNDEFINED)", "optics_coredist", 2, null);
			scOptics.addCodeLine("update(N, p, Seeds, eps, Minpts)", "optics_update", 3, null);
			scOptics.addCodeLine("for each next q in Seeds", "optics_forseeds", 3, null);
			scOptics.addCodeLine("N' = getNeighbors(q, eps)", "optics_forprocessed", 4, null);
			scOptics.addCodeLine("mark q as processed", "optics_forprocessed", 4, null);
			scOptics.addCodeLine("output q to the ordered list", "optics_forprocessed", 4, null);
			scOptics.addCodeLine("if (core-distance(q, eps, Minpts) != UNDEFINED)", "optics_inner_coredist", 4, null);
			scOptics.addCodeLine("update(N', q, Seeds, eps, Minpts)", "optics_inner_update", 5, null);
			scOptics.highlight(0);

			scUpdate = lang.newSourceCode(new Coordinates(400, chart_start_y), "optics", null, code_properties);
			scUpdate.addCodeLine("update(N, p, Seeds, eps, Minpts)", "update_head", 0, null);
			scUpdate.addCodeLine("coredist = core-distance(p, eps, MinPts)", "update_", 1, null);
			scUpdate.addCodeLine("for each o in N", "update_", 1, null);
			scUpdate.addCodeLine("if (o is not processed)", "update_", 2, null);
			scUpdate.addCodeLine("new-reach-dist = max(coredist, dist(p,o))", "update_", 3, null);
			scUpdate.addCodeLine("if (o.reachability-distance == UNDEFINED)", "update_", 3, null);
			scUpdate.addCodeLine("o.reachability-distance = new-reach-dist", "update_", 4, null);
			scUpdate.addCodeLine("Seeds.insert(o, new-reach-dist)", "update_", 4, null);
			scUpdate.addCodeLine("else if (new-reach-dist < o.reachability-distance)", "update_", 3, null);
			scUpdate.addCodeLine("o.reachability-distance = new-reach-dist", "update_", 4, null);
			scUpdate.addCodeLine("Seeds.move-up(o, new-reach-dist)", "update_", 4, null);
			scUpdate.hide();
		}
		circle_radius = (Integer) primitives.get("circle_radius");
		drawTempCoredist = (Boolean) primitives.get("drawTempCoredist");
		//		System.err.println("x");
		drawReachabilityNetwork_immediatly = (Boolean) primitives.get("drawReachabilityNetwork_immediately");

		//		System.err.println("y");
		int[][] cluster = (int[][]) primitives.get("db");
		for (int i = 0; i < cluster.length; i++)
			limit_red_line = (cluster[i][2] > limit_red_line ? cluster[i][2] : limit_red_line);
		// create points and draw
		List<P> punkte = makeClouds(cluster);
		for (P p : punkte)
			lang.newCircle(new Coordinates(p.x, p.y), circle_radius, "", null);
		LinkedList<P> result = optics(punkte, Double.MAX_VALUE, (Integer) primitives.get("min_pts"));

		if (!drawReachabilityNetwork_immediatly) {
			PolylineProperties line_prop = new PolylineProperties();
			line_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.lightGray);
			for (P p : result) {
				if (p.pred != null) {
					Coordinates[] points = { new Coordinates(p.x, p.y), new Coordinates(p.pred.x, p.pred.y) };
					lang.newPolyline(points, "", null, line_prop);
				}
			}
		}

		scOptics.hide();
		scUpdate.hide();

		Color auswertung_curr_color = Color.blue;
		auswertung_circ_prop_highlight = new CircleProperties();
		auswertung_bar_prop_highlight = new RectProperties();
		{ // properties for circle normal + highlight
			auswertung_circ_prop_highlight.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
			auswertung_circ_prop_highlight.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.black);
			auswertung_circ_prop_highlight.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

			auswertung_circ_prop = new CircleProperties();
			auswertung_circ_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
			auswertung_circ_prop.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);

			// properties for bar normal + highlight
			auswertung_bar_prop = new RectProperties();
			auswertung_bar_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, auswertung_curr_color);
			auswertung_bar_prop.set(AnimationPropertiesKeys.FILL_PROPERTY, auswertung_curr_color);
			auswertung_bar_prop.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		}

		{
			auswertung_bar_prop_highlight.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
			auswertung_bar_prop_highlight.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.red);
			auswertung_bar_prop_highlight.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			lang.newRect(new Coordinates(chart_start_x, chart_start_y - limit_red_line), new Coordinates(chart_start_x + 600, chart_start_y + 2
					- limit_red_line), "", null, auswertung_bar_prop_highlight);
			auswertung_bar_prop_highlight.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
			auswertung_bar_prop_highlight.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.black);
			auswertung_bar_prop_highlight.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			lang.newRect(new Coordinates(chart_start_x, chart_start_y), new Coordinates(chart_start_x + 600, chart_start_y + 3), "", null,
					auswertung_bar_prop_highlight);
			lang.newText(new Coordinates(chart_start_x - 30, chart_start_y + 11), "Erreichbarkeitsdistanz, relativ zum nächsten Kernpunkt", "", null);
		}
		{
			P first = result.getFirst();
			if (first.reachability_distance == P.UNDEFINED)
				first.reachability_distance = 0;
		}

		// prepare painting of points + corresponding value
		auswertung_bar_prop_highlight.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.black);
		auswertung_bar_prop_highlight.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		auswertung_bar_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, auswertung_curr_color);
		auswertung_bar_prop.set(AnimationPropertiesKeys.FILL_PROPERTY, auswertung_curr_color);
		auswertung_bar_prop.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		auswertung_circ_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, auswertung_curr_color);
		auswertung_circ_prop.set(AnimationPropertiesKeys.FILL_PROPERTY, auswertung_curr_color);
		auswertung_circ_prop.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

		// colorize clusters and paint reachability-chart
		int delta = 600 / punkte.size();
		int bar_width = delta - 2;
		boolean inValley_prevIteration = true;
		for (P p : result) {
			if (p.reachabilityCircle != null) {
				p.reachabilityCircle.hide();
				p.reachabilityCircle = null;
			}
			// detect valleys
			if (p.reachability_distance > limit_red_line) {
				inValley_prevIteration = false;
				auswertung_bar_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.lightGray);
				auswertung_circ_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.lightGray);
				auswertung_bar_prop.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.lightGray);
				auswertung_circ_prop.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.lightGray);
			} else if (!inValley_prevIteration) {
				// am now in a valley, but not prev iteration => need next color
				inValley_prevIteration = true;
				auswertung_curr_color = nextColor(auswertung_curr_color);
				auswertung_bar_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, auswertung_curr_color);
				auswertung_circ_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, auswertung_curr_color);
				auswertung_bar_prop.set(AnimationPropertiesKeys.FILL_PROPERTY, auswertung_curr_color);
				auswertung_circ_prop.set(AnimationPropertiesKeys.FILL_PROPERTY, auswertung_curr_color);
			}

			Circle circle = lang.newCircle(new Coordinates(p.x, p.y), circle_radius, "", null, auswertung_circ_prop_highlight);

			Coordinates lowerright = new Coordinates(chart_start_x + bar_width, chart_start_y);
			Coordinates upperleft = new Coordinates(chart_start_x, chart_start_y - (int) Math.round(p.reachability_distance));
			Rect rect = lang.newRect(upperleft, lowerright, "", null, auswertung_bar_prop_highlight);

			if ((Boolean) primitives.get("drawClusteringWithSteps"))
				lang.nextStep();
			rect.hide();
			circle.hide();

			lang.newCircle(new Coordinates(p.x, p.y), circle_radius, "", null, auswertung_circ_prop);
			lang.newRect(upperleft, lowerright, "", null, auswertung_bar_prop);
			chart_start_x += delta;
		}
		return lang.toString();
	}

	/**
	 * generate another full color different from the current
	 * 
	 * @param c
	 * @return
	 */
	private Color nextColor(Color c) {
		float[] f = new float[3];
		float h = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), f)[0];
		// addiere 40° => 1/9 von 360°
		h += 1 / 4.5;
		return Color.getHSBColor(h, 1, 1);
	}

	@Override
	public String getAlgorithmName() {
		return "OPTICS - Ordering Points To Identify the Clustering Structure";
	}

	@Override
	public String getAnimationAuthor() {
		return "Max Zeller, Florian Brams";
	}

	@Override
	public String getCodeExample() {
		return "Wir hatten leider keine Zeit mehr.";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.US;
	}

	@Override
	public String getDescription() {
		return "A nice Algorithm to cluster some data (in contrast to DBSCAN it can recognize different distances between the points of clusters).";
	}

	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	@Override
	public String getName() {
		return getAlgorithmName();
	}

	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public void init() {
	}
}
