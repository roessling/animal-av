package generators.misc;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import algoanim.primitives.Circle;
import algoanim.primitives.Polygon;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolygonProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.main.Animal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.NotEnoughNodesException;

import java.awt.Color;
import java.awt.Font;

public class FriendsOfFriends implements ValidatingGenerator
{
	// Primitives
	private Language lang;
	private double distance;
	private int amountOfStars;
	private int width;
	private int height;
	private int presets;
	private double dropOffPerLevel;
	private int amountOfStepsShown;
	private int groupNamesIteration;
	private int amountOfIterations;
	private boolean useKDTreeToSearch;	// if false, uses linear search
	private boolean useConcaveHull;		// if false, draw a convex hull with giftWrapping
	private double setConcaveHullDigDecision;
	
	// Properties
	private CircleProperties pointsProps;
	private CircleProperties blinkingPoints;
	private CircleProperties halos;
	private SourceCodeProperties scp;
	private PolylineProperties connectionLines;
	private PolygonProperties iterationOneHull;
	private PolygonProperties iterationTwoHull;
	private PolygonProperties iterationThreeHull;
	private TextProperties groupNamesProps;
	private RectProperties fofSourceCodeHighlightRect;
	private RectProperties addAdjSourceCodeHighlightRect;

	// Names
	public static final String ALGORITHM_NAME = "Friends of Friends";
	public static final String AUTHOR_NAME = "Martin Ott";

	// Source Code
	SourceCode sc;
	SourceCode usedAlgorithms;

	// Text
	Text header;
	Text intro1;
	Text intro2;
	Text intro3;
	Text intro4;
	Text intro5;
	Text intro6;
	Text intro7;
	Text intro8;
	Text usingIntro;
	Text outro1;
	Text outro2;
	Text outro3;
	Text searchAlgoText;
    Text hullAlgoText;
    Rect usingRect;

    
	// all points/stars used in the visualization
	public HashSet<Star> stars;
	
	// Properties of stars
	public int starOffset = 10;	// minimum distance between stars
	public int offsetToHeader = starOffset * 3;
	public ArrayList<Circle> starCircles;	// all drawn stars

	// All halos that have been drawn
	public ArrayList<Circle> drawnHalos;

	// Presets: width, height, distance/linking length, amountOfStars
	public static final int[] PRESET_1 = new int[]
		{ 80, 30, 2, 800 };
	public static final int[] PRESET_2 = new int[]
		{ 120, 50, 8, 200 };
	public static final int[] PRESET_3 = new int[]
		{ 100, 80, 2, 2500 };
	public static final int[] PRESET_4 = new int[]
		{ 30, 30, 4, 100 };
	public static final int[] PRESET_5 = new int[]
		{ 70, 30, 4, 400 };
	public static final int[] PRESET_6 = new int[]
		{ 30, 70, 3, 300 };
	public static final ArrayList<int[]> ALL_PRESETS = new ArrayList<int[]>()
	{	private static final long serialVersionUID = 1L;
		{
			add(PRESET_1);
			add(PRESET_2);
			add(PRESET_3);
			add(PRESET_4);
			add(PRESET_5);
			add(PRESET_6);
		}
	};
	
	// Keeps drawn lines so they can be erased
	ArrayList<Polyline> lines = new ArrayList<Polyline>();
	
	// Gets incremented after every visualization. stops visualization after certain amount of steps have been reached
	// otherwise we'd have 3000+ steps
	public static int counter = 0;
	
	// Rectangles aroundthe upper and lower source code parts to highlight them
	private Rect fofHighlightRect;
	private Rect addAdjHighlightRect;

	public static void main(String[] args)
	{
		Generator gen = new FriendsOfFriends();
		Animal.startGeneratorWindow(gen);
	}

	public void init()
	{
		lang = new AnimalScript(ALGORITHM_NAME, AUTHOR_NAME, 800, 600);
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException
	{
		width = (Integer) primitives.get("Width");
		height = (Integer) primitives.get("Height");
		distance = (double) primitives.get("Linking Length");
		amountOfStars = (int) primitives.get("Amount of points");
		presets = (int) primitives.get("Presets");
		amountOfStepsShown = (int) primitives.get("Steps per iteration");
		amountOfIterations = (int) primitives.get("Amount of iterations");
		setConcaveHullDigDecision = (double) primitives.get("Concave Hull dig-decider");

		// catch wrong input
		if (width < 3 || width > 120) return false;
		if (height < 3 || height > 80) return false;
		if (distance < 1.0 || distance > 20.0) return false;
		if (amountOfStars < 0 || amountOfStars > 5000) return false;
		if (presets < 0 || presets > ALL_PRESETS.size()) return false;
		if (amountOfStepsShown < 1 || amountOfStepsShown > 50) return false;
		if (amountOfIterations < 1 || amountOfIterations > 3) return false;
		if (setConcaveHullDigDecision <= 0 || setConcaveHullDigDecision > 5) return false;
		
		return true;
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
	{
		width = (Integer) primitives.get("Width");
		height = (Integer) primitives.get("Height");
		distance = (double) primitives.get("Linking Length");
		amountOfStars = (int) primitives.get("Amount of points");
		presets = (int) primitives.get("Presets");
		amountOfStepsShown = (int) primitives.get("Steps per iteration");
		groupNamesIteration = (int) primitives.get("Draw group names for iteration");
		amountOfIterations = (int) primitives.get("Amount of iterations");
		useKDTreeToSearch = (boolean) primitives.get("use KDTree");
		useConcaveHull = (boolean) primitives.get("use Concave Hull");
		setConcaveHullDigDecision = (double) primitives.get("Concave Hull dig-decider");
		
		pointsProps = (CircleProperties) props.getPropertiesByName("Points");
		blinkingPoints = (CircleProperties) props.getPropertiesByName("Blinking Points");
		connectionLines = (PolylineProperties) props.getPropertiesByName("Connection Lines");
		scp = (SourceCodeProperties) props.getPropertiesByName("Source Code");
		halos = (CircleProperties) props.getPropertiesByName("Halos");
		iterationOneHull = (PolygonProperties) props.getPropertiesByName("Hull in Iteration 1");
		iterationTwoHull = (PolygonProperties) props.getPropertiesByName("Hull in Iteration 2");
		iterationThreeHull = (PolygonProperties) props.getPropertiesByName("Hull in Iteration 3");
		groupNamesProps = (TextProperties) props.getPropertiesByName("Group Names");
		fofSourceCodeHighlightRect = (RectProperties) props.getPropertiesByName("FoF SourceCode Highlight");
		addAdjSourceCodeHighlightRect = (RectProperties) props.getPropertiesByName("addAdjacent SourceCode Highlight");
		
		// sets font size cause that cna't be set with the .xml for some reason
		groupNamesProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.ITALIC, 5));


		// load presets if chosen by user
		if (presets != 0)
		{
			int[] chosen = ALL_PRESETS.get(presets - 1);
			width = chosen[0];
			height = chosen[1];
			distance = chosen[2];
			amountOfStars = chosen[3];
		}
		
		// The amount the linking length gets smaller every iteration
		dropOffPerLevel = distance / 4D;
		
		/* ----- Variable init done ----- */

		// init
		lang = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, ALGORITHM_NAME, AUTHOR_NAME, 640, 480);
		stars = new HashSet<Star>();
		starCircles = new ArrayList<Circle>();
		drawnHalos = new ArrayList<Circle>();
		lang.setStepMode(true);
		makeHeaderAndIntroTexts();
		lang.nextStep("Intro");

		// Fade intro text and generate and draw random points
		intro1.hide();
		intro2.hide();
		intro3.hide();
		intro4.hide();
		intro5.hide();
		intro6.hide();
		intro7.hide();
		intro8.hide();
		usingIntro.hide();
		initStars();
		drawStars();
		
		// init and show source codes
		lang.nextStep();
		sc = lang.newSourceCode(getStarPosition(width < 25 ? 25 : width+2, -1), "sourceCode", null, scp);
		usedAlgorithms = lang.newSourceCode(getStarPosition(width < 25 ? 25 : width+2, -7), "usedAlgorithmsSourceCode", null, scp);
		initSelectedAlgoVisuals();
		initSourceCode();
		initHighlightLines();
		
		// Get search strategy selected by user
		ISearchStrategy strategy;
		if(useKDTreeToSearch) strategy = new KDNearestNeighbourSearch();
		else strategy = new LinearSearch();
		
		// Get or decide on dig decider for concave hull algorithm
		double concaveHullDigDecider = 1;
		if(setConcaveHullDigDecision > 0 && setConcaveHullDigDecision <= 5) concaveHullDigDecider = setConcaveHullDigDecision;
		else
		{
			if(distance < 2) concaveHullDigDecider = 5;
			else if(distance < 3) concaveHullDigDecider = 5;
			else if(distance < 4) concaveHullDigDecider = 4;
			else if(distance < 5) concaveHullDigDecider = 3;
			else if(distance < 6) concaveHullDigDecider = 2;
			else if(distance < 7) concaveHullDigDecider = 1.5;
			else if(distance < 8) concaveHullDigDecider = 1;
			else concaveHullDigDecider = 1;
		}
		
		// Get hull algorithm selected by user
		IHullAlgorithms hullDrawer;
		if(useConcaveHull) hullDrawer = new ConcaveHull(concaveHullDigDecider);
		else hullDrawer = new GiftWrapping();
		
		/** Run Friends of Friends with selected inputs
		* distance = linking length
		* strategy = selected nearest neighbour search (KD-Tree or linear search)
		* hullDrawer = selected way to draw the hull (Concave or Convex)
		*/
		runSteps(distance, strategy, hullDrawer);

		// Done : finish with outro
		lang.nextStep();
		makeOutro();
		lang.nextStep("Outro");

		lang.nextStep("Done");
		return lang.toString();
	}
	

	/**
	 * Sets all needed variables for running FoF and calls it 1-3 times as selected.
	 * @param dist			The linking length
	 * @param searchStrat	The nearest neighbour search strategy
	 * @param hullAlgo		The algorithm used to draw the hulls of the groups
	 */
	public void runSteps(double dist, ISearchStrategy searchStrat, IHullAlgorithms hullAlgo)
	{
		PolygonProperties color;
		boolean drawGroupIDs;
		
		// Run FoF 1-3 times
		for(int i=0;i<amountOfIterations;i++)
		{
			// Decide hull color for this iteration
			if(i == 0) color = iterationOneHull;
			else if(i == 1) color = iterationTwoHull;
			else color = iterationThreeHull;
			
			// Decide for which iteration to draw the group names
			drawGroupIDs = false;
			if(i == 0 && groupNamesIteration == 1) drawGroupIDs = true;
			if(i == 1 && groupNamesIteration == 2) drawGroupIDs = true;
			if(i == 2 && groupNamesIteration == 3) drawGroupIDs = true;
			
			// Put current iteration start into ToC
			if(i == 0) lang.nextStep("First Iteration");
			else if(i == 1) lang.nextStep("Second Iteration");
			else lang.nextStep("Third Iteration");
			
			// After the first iteration only half the amount of steps get shown
			// Also resets the stars back to unvisited so we can use them again
			if(i != 0) { amountOfStepsShown = (int)(amountOfStepsShown / 2); resetStars(); }
			
			// Call FoF
			runOneIteration(dist - dropOffPerLevel * i, searchStrat, hullAlgo, amountOfIterations-i+1, color, true, drawGroupIDs);
		}
	}
	
	/**
	 * Runs one iteration of the Friends of Friends algorithm. Then draws the hulls of all groups.
	 * @param dist				The linking length
	 * @param searchStrat		The Nearest Neighbour search strategy
	 * @param hullAlgo			The algorithm to draw the hull with
	 * @param depth				The depth to draw the hulls in, so they end up above the one form the last iteration
	 * @param hullProperties	Color for the hull
	 * @param visualize			If it should draw lines, blink stars, etc
	 * @param drawGroupIDs		If it should draw the group IDs after finishing
	 */
	public void runOneIteration(double dist, ISearchStrategy searchStrat, IHullAlgorithms hullAlgo, int depth, PolygonProperties hullProperties, boolean visualize, boolean drawGroupIDs)
	{
		// Execute FoF
		HashMap<Integer, HashSet<Star>> groups = runFoF(dist, searchStrat, visualize);
		
		// Draw the hull
		usedAlgorithms.highlight(1);
		drawHullPolygon(groups, hullAlgo, hullProperties, depth, drawGroupIDs);
		if(depth == 4) lang.nextStep("Iteration 1 hull drawn");
		else if(depth == 3) lang.nextStep("Iteration 2 hull drawn");
		else if(depth == 2) lang.nextStep("Iteration 3 hull drawn");
		else lang.nextStep("Iteration hull drawn");
		
		usedAlgorithms.unhighlight(1);
	}
	
	// Sets all stars back to unvisited. Used when FoF gets run multiple times.
	private void resetStars()
	{
		for(Star s : stars)
		{
			s.visited = false;
		}
	}
	
	// Initializes the two rectangles around the source code
	private void initHighlightLines()
	{		
		fofHighlightRect = lang.newRect(new Offset(-2, -2, "sourceCode", AnimalScript.DIRECTION_NW), new Offset(2, 13*21, "sourceCode", AnimalScript.DIRECTION_NE), "fofHighlightRect", null, fofSourceCodeHighlightRect);
		addAdjHighlightRect = lang.newRect(new Offset(0, 5, "fofHighlightRect", AnimalScript.DIRECTION_SW), new Offset(2, 2, "sourceCode", AnimalScript.DIRECTION_SE), "addAdjHighlightRect", null, addAdjSourceCodeHighlightRect);
		
		fofHighlightRect.hide();
		addAdjHighlightRect.hide();
	}
	
	/**
	 * Iterates over all hulls and draws them as polygons.
	 * @param hulls				The hulls to draw
	 * @param hullAlgo			The algorithm to draw the hulls with (implements IHullAlgorithm)
	 * @param props				Color of the hull
	 * @param depth				Depth to draw the hull in, so later hulls can go on top
	 * @param drawGroupNames	If true, draws the group name for the current hull in the middle
	 */
	private void drawHullPolygon(HashMap<Integer, HashSet<Star>> hulls, IHullAlgorithms hullAlgo, PolygonProperties props, int depth, boolean drawGroupNames)
	{
		Iterator<Entry<Integer, HashSet<Star>>> it = hulls.entrySet().iterator();
		while(it.hasNext())
		{
			Entry<Integer, HashSet<Star>> entry = it.next();
			ArrayList<Star> hull = hullAlgo.constructHull(entry.getValue());
			Polygon pory = drawPorygon(hull, props, depth);
			if(drawGroupNames)
			{
				if(pory == null)
				{
					Star current = hull.get(0);
					lang.newText(getStarPosition(current.x, current.y), "(" + entry.getKey() + ")", entry.getKey()+"alone : groupID", null, groupNamesProps);
				} else
				{
					lang.newText(new Offset(0, 0, pory.getName(), AnimalScript.DIRECTION_C), "(" + entry.getKey() + ")", pory.getName()+"groupID", null, groupNamesProps);
				}
			}
		}
	}
	
	/**
	 * Draws the hull polygon for drawHullPolygon.
	 * @param hull		The points to draw the hull for
	 * @param props		Color of the hull
	 * @param depth		Depth of the hull
	 * @return			Returns the constructed polygon so drawHullPolygon can draw the group name in the middle of it. Null if it failed to construct a polygon from the points.
	 */
	private Polygon drawPorygon(ArrayList<Star> hull, PolygonProperties props, int depth)
	{
		if(hull.size() < 2) return null;	// don't draw anything for 1-star groups
		
		// draw polygon
		Node[] positions = new Node[hull.size()];
		for(int i=0;i<hull.size();i++)
		{
			Star current = hull.get(i);
			positions[i] = getStarPosition(current.x, current.y);
		}
		
		try
		{
			return lang.newPolygon(positions, "hull"+props.get(AnimationPropertiesKeys.COLOR_PROPERTY).toString(), null, props);
		} catch (NotEnoughNodesException e)
		{
			System.out.println("positions didn't have enough nodes: "+hull.size());
			e.printStackTrace();
			return null;
		}
	}

	/** 
	 * Randomly generates as many points as selected by the user.
	 * Since it adds the stars into a set there can be no duplicates.
	 * Duplicates defined as x and y coordinates are equal (see Star.equals).
	 */
	private void initStars()
	{
		Random rng = new Random();
		for (int i = 0; i < amountOfStars; i++)
		{
			int x = rng.nextInt(width);
			int y = rng.nextInt(height);
			stars.add(new Star(x, y));
		}
	}

	// Draws all points made by initStars and adds them to the list of drawn stars.
	public void drawStars()
	{
		Iterator<Star> iterator = stars.iterator();
		while (iterator.hasNext())
		{
			Star s = iterator.next();
			starCircles.add(drawStar(s.x, s.y));
		}
	}

	// Draws a point at the given x and y position in my coordinate system
	public Circle drawStar(int x, int y)
	{
		return lang.newCircle(getStarPosition(x, y), 1, "star" + x + "" + y, null, pointsProps);
	}

	// Defines my coordinate system
	public Offset getStarPosition(int x, int y)
	{
		return new Offset(x * starOffset, y * starOffset + offsetToHeader, "header", AnimalScript.DIRECTION_SW);
	}

	// Draws a circle around the given position with the given radius
	// Used to visualize the linking length around a point
	private Circle drawHalo(int x, int y, double radius)
	{
		return lang.newCircle(getStarPosition(x, y), (int) (radius * starOffset), "halo" + x + "" + y, null, halos);
	}
	
	// Draws a point with user-selected color to blink a star
	// Used to visualize what star is currently being evaluated by FoF
	public Circle blinkStar(Star s)
	{
		return lang.newCircle(getStarPosition(s.x, s.y), 1, "blinkStar" + s.x + "" + s.y, null, blinkingPoints);
	}
	
	/**
	 * The FoF algorithm with added highlighting for animal.
	 * Returns a map of all groups (identified by their group id).
	 */
	public HashMap<Integer, HashSet<Star>> runFoF(double linkingLength, ISearchStrategy strat, boolean visualize)
	{
		// All groups generated by FoF get added to this. Gets returned at the end.
		HashMap<Integer, HashSet<Star>> groups = new HashMap<Integer, HashSet<Star>>();
		int groupID = 0;
		counter = 0;
		
		for(Star current : stars)
		{
			if (!current.visited)
			{
				if(visualize && counter < (amountOfStepsShown+1))
				{
					counter++;
					highlightRunFoF(true);
					fofHighlightRect.show();
					Circle halo = drawHalo(current.x, current.y, linkingLength);
					lang.nextStep();
					
					// Add current point to group
					HashSet<Star> thisGroup = new HashSet<Star>();
					thisGroup.add(current);
					
					groupID++;
					current.visited = true;
					current.group = groupID;
					
					highlightRunFoF(false);
					lang.nextStep();
					
					// blink call to addAdjacentStars
					if(counter < amountOfStepsShown)
					{
						sc.highlight(13);
						lang.nextStep();
						sc.unhighlight(13);
						lang.nextStep();
					}
					fofHighlightRect.hide();
					halo.hide();
					
					// Add all adjacent points to the group
					thisGroup.addAll( addAdjacentStars(current, groupID, linkingLength, strat, visualize) ); // add all adjacent stars to this group
					groups.put(groupID, thisGroup);
				} else
				{
					// Add current point to group
					HashSet<Star> thisGroup = new HashSet<Star>();
					thisGroup.add(current);
					
					groupID++;
					current.visited = true;
					current.group = groupID;
					
					// Add all adjacent points to the group
					thisGroup.addAll( addAdjacentStars(current, groupID, linkingLength, strat, visualize) ); // add all adjacent stars to this group
					groups.put(groupID, thisGroup);
				}
			}
		}
		
		// Hide all lines that were drawn
		for(Polyline p : lines)
		{
			p.hide();
		}
		counter = 0;
		return groups;
	}


	/**
	 * Fetches all stars adjacent to the input one and adds them to the group.
	 * @param s					The star to search around of
	 * @param groupID			The group to all all found ones to
	 * @param linkingLength		The linking length to search in
	 * @param strat				The Nearest Neighbour Search strategy
	 * @param visualize			If visualization should happen
	 * @return					All adjacent stars
	 */
	public HashSet<Star> addAdjacentStars(Star s, int groupID, double linkingLength, ISearchStrategy strat, boolean visualize)
	{
		HashSet<Star> ret = new HashSet<Star>();
		
		Set<Star> adjacentStars;
		if(visualize && counter < (amountOfStepsShown+1))
		{
			usedAlgorithms.highlight(0);
			sc.highlight(21);
			adjacentStars = strat.nearestNeighbourSearch(s, linkingLength, stars);	// Use Nearest Neighbour Search
			lang.nextStep();
			usedAlgorithms.unhighlight(0);
			sc.unhighlight(21);
		} else adjacentStars = strat.nearestNeighbourSearch(s, linkingLength, stars);	// Use Nearest Neighbour Search

		
		for(Star current : adjacentStars)
		{
			if(!current.visited)
			{
				current.visited = true;
				current.group = groupID;
				counter++;
				
				if(visualize && counter < (amountOfStepsShown+1))
				{
					addAdjHighlightRect.show();
					Circle circle = drawHalo(s.x, s.y, linkingLength);
					lang.nextStep();
					
					Circle blink = blinkStar(current);
					sc.highlight(27);
					sc.highlight(28);
					lang.nextStep();
					
					sc.unhighlight(27);
					sc.unhighlight(28);
					blink.hide();
					lang.nextStep();
					
					lines.add( drawLine(s, current) );
					lang.nextStep();
					
					circle.hide();
					
					// blink call to addAdjacentStars
					if(counter < amountOfStepsShown)
					{
						sc.highlight(30);
						lang.nextStep();
						sc.unhighlight(30);
						lang.nextStep();
					}
					addAdjHighlightRect.hide();
					lang.nextStep();
				}
				
				ret.add(current);
				// Add adjacent stars of these adjacent stars
				ret.addAll( addAdjacentStars(current, groupID, linkingLength, strat, visualize) );
			}
		}
		return ret;
	}
	
	// Highlights or unhighlights the corresponding source code part of FoF
	public void highlightRunFoF(boolean highlight)
	{
		int start = 7;
		int end = 15;
		
		if(highlight)
		{
			for(int i=start;i<end;i++)
			{
				sc.highlight(i);
			}
		} else
		{
			for(int i=start;i<end;i++)
			{
				sc.unhighlight(i);
			}
		}
	}

	// Draws a line between two points
	public Polyline drawLine(Star one, Star two)
	{		
		return lang.newPolyline(new Offset[] { getStarPosition(one.x, one.y), getStarPosition(two.x, two.y) }, "line" + one.toString() + two.toString(), null, connectionLines);
	}

	/**
	 * This is the source code that's displayed in the animation.
	 */
	public void initSourceCode()
	{
		sc.addCodeLine("// Main method. Iterates over all stars. Delegates group finding to addAdjacentStars.", null, 0, null);	//0
		sc.addCodeLine("public void friendsOfFriends(double linkingLength, Set<Star> stars)", null, 0, null);
		sc.addCodeLine("{", null, 0, null);
		sc.addCodeLine("    int groupID = 0;", null, 0, null);
		sc.addCodeLine("    ", null, 0, null);
		sc.addCodeLine("    for(Star current : stars)", null, 0, null);	// 5
		sc.addCodeLine("    {", null, 0, null);
		sc.addCodeLine("        if(!current.wasVisited())", null, 0, null);
		sc.addCodeLine("        {", null, 0, null);
		sc.addCodeLine("            groupID++;", null, 0, null);
		sc.addCodeLine("            current.setVisited(true);", null, 0, null);
		sc.addCodeLine("            current.group = groupID;", null, 0, null);
		sc.addCodeLine("            ", null, 0, null);
		sc.addCodeLine("            addAdjacentStars(current, groupID, linkingLength, stars);", null, 0, null);	// 13
		sc.addCodeLine("        }", null, 0, null);
		sc.addCodeLine("    }", null, 0, null);
		sc.addCodeLine("};", null, 0, null);
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine("// Helper algorithm for FoF. Calls nearest neighbour search to get all adjacent stars.", null, 0, null);
		sc.addCodeLine("private void addAdjacentStars(Star s, int groupID, double linkingLength, Set<Star> stars)", null, 0, null);		// 19
		sc.addCodeLine("{", null, 0, null);
		sc.addCodeLine("    Set<Star> adjacentStars = nearestNeighbourSearch(s, linkingLength, stars);", null, 0, null);
		sc.addCodeLine("    ", null, 0, null);
		sc.addCodeLine("    for(Star current : adjacentStars)", null, 0, null);		// 23
		sc.addCodeLine("    {", null, 0, null);
		sc.addCodeLine("        if(!current.wasVisited())", null, 0, null);
		sc.addCodeLine("        {", null, 0, null);
		sc.addCodeLine("            current.setVisited(true);", null, 0, null);
		sc.addCodeLine("            current.group = groupID;", null, 0, null);
		sc.addCodeLine("            ", null, 0, null);
		sc.addCodeLine("            addAdjacentStars(current, groupID, linkingLength, stars);", null, 0, null);	// 30
		sc.addCodeLine("        }", null, 0, null);
		sc.addCodeLine("    }", null, 0, null);
		sc.addCodeLine("};", null, 0, null);
	}

	// Sets the header and eight intro texts when first loading the algorithm
	private void makeHeaderAndIntroTexts()
	{
		// Header
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 25));
		header = lang.newText(new Coordinates(20, 30), ALGORITHM_NAME, "header", null, headerProps);
		
	    // Intro texts
	    TextProperties introProperties = new TextProperties();
	    introProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 20));
	    
	    RectProperties rectProps = new RectProperties();
	    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
	    @SuppressWarnings("unused")
		Rect hRect = lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", AnimalScript.DIRECTION_SE), "hRect", null, rectProps);
	    
	    String intro1Text = "- Connects all points within linking length into one group";
	    String intro2Text = "- Goes through 1-3 iterations. Each with a smaller linking length and a different color";
	    String intro3Text = "- To aid understanding, the linking length will be visualized with a circle around the current point";
	    String searchText = useKDTreeToSearch ? "KDTree for Nearest Neighbour Search [O(2N^0.5) in 2D per search]" : "Linear Search for Nearest Neighbour Search [O(2N) in 2D per search]";
	    String using = "Using:";
	    
	    intro1 = lang.newText(new Offset(0, 10, "header", AnimalScript.DIRECTION_SW), intro1Text, "intro1", null, introProperties);
	    intro2 = lang.newText(new Offset(0, 0, "intro1", AnimalScript.DIRECTION_SW), intro2Text, "intro2", null, introProperties);
	    intro3 = lang.newText(new Offset(0, 0, "intro2", AnimalScript.DIRECTION_SW), intro3Text, "intro3", null, introProperties);
	    
	    usingIntro = lang.newText(new Offset(0, 30, "intro3", AnimalScript.DIRECTION_SW), using, "usingIntro", null, introProperties);
	    intro4 = lang.newText(new Offset(10, 0, "usingIntro", AnimalScript.DIRECTION_SW), "Linking length = "+distance, "intro4", null, introProperties);
	    intro5 = lang.newText(new Offset(0, 0, "intro4", AnimalScript.DIRECTION_SW), searchText, "intro5", null, introProperties);
	    intro6 = lang.newText(new Offset(0, 0, "intro5", AnimalScript.DIRECTION_SW), "Size: "+width+" x "+height+" with ~"+amountOfStars+" points", "intro6", null, introProperties);
	    intro7 = lang.newText(new Offset(0, 0, "intro6", AnimalScript.DIRECTION_SW), "Showing "+amountOfStepsShown+" steps in first iteration, half of that afterwards", "intro7", null, introProperties);
	    intro8 = lang.newText(new Offset(0, 0, "intro7", AnimalScript.DIRECTION_SW), "Iterations: "+amountOfIterations, "intro8", null, introProperties);
	}
	
	// Adds the source code that displays which algorithms have been selected by the user
	// KD-Tree or Linear Search
	// Concave Hull or Convex Hull
	// Also draws the green background behind it
	private void initSelectedAlgoVisuals()
	{
	    String searchText = useKDTreeToSearch ? "Using KDTree for Nearest Neighbour Search" : "Using Linear Search for Nearest Neighbour Search";
	    String hullText = useConcaveHull ? "Using Concave Hull with digDecider="+ConcaveHull.decision : "Using Convex Hull";
	    
	    usedAlgorithms.addCodeLine(searchText, null, 0, null);
	    usedAlgorithms.addCodeLine(hullText, null, 0, null);
	    
	    RectProperties rectProps = new RectProperties();
	    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(100, 200, 100));
	    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		usingRect = lang.newRect(new Offset(-5, -5, "usedAlgorithmsSourceCode", AnimalScript.DIRECTION_NW), new Offset(10, 5, "usedAlgorithmsSourceCode", AnimalScript.DIRECTION_SE), "usingInfoBackground", null, rectProps);
	}

	// Sets the two outro texts after the animation is done
	private void makeOutro()
	{
	    TextProperties outroProperties = new TextProperties();
	    outroProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, width >= 80 ? 18 : 13));
	    outroProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.blue);
	    
	    String outro1Text = "In the result all "+amountOfStars+" points are assigned to groups.";
	    String outro2Text = width > 80 ? "This is used in astrophysics to find clusters of stars or dark matter out of millions of 2D or 3D points." : "This is used in astrophysics to find clusters";
	    String outro3Text = "  of stars or dark matter out of millions of 2D or 3D points.";
	    
		outro1 = lang.newText(getStarPosition(0, height+3), outro1Text, "outro1", null, outroProperties);
		outro2 = lang.newText(new Offset(0,  0, "outro1", AnimalScript.DIRECTION_SW), outro2Text, "outro2", null, outroProperties);
		if(width <= 80) outro3 = lang.newText(new Offset(0,  0, "outro2", AnimalScript.DIRECTION_SW), outro3Text, "outro3", null, outroProperties);
	}

	/* ----- ----- ----- Wizard code below ----- ----- ----- */

	public String getName()
	{

		return ALGORITHM_NAME;
	}

	public String getAlgorithmName()
	{
		return ALGORITHM_NAME;
	}

	public String getAnimationAuthor()
	{
		return AUTHOR_NAME;
	}

	public String getDescription()
	{
		return "Traverses a set of points, taking in only one parameter: the linking length.\n" +
		"\n" +
		"The points get sorted into groups. A group consists of all stars within linking length of each other.\n" + 
		"It uses nearest neighbour search to find all points within linking length.\n" +
		"Feel free to check out nearest neighbour search implementations in animal like Tree -> KD-Tree.\n" +
		"\n" +
		"The Friends of Friends algorithm is often used in astrophysics to determine star- or dark matter clusters in 2D and 3D.";
	}

	public String getCodeExample()
	{
		return "// Main method. Iterates over all stars. Delegates group finding to addAdjacentStars." + "\n" +	//0
		"public void friendsOfFriends(double linkingLength, Set<Star> stars)" + "\n" +
		"{" + "\n" +
		"    int groupID = 0;" + "\n" +
		"    " + "\n" +
		"    for(Star current : stars)" + "\n" +	// 5
		"    {" + "\n" +
		"        if(!current.wasVisited())" + "\n" +
		"        {" + "\n" +
		"            groupID++;" + "\n" +
		"            current.setVisited(true);" + "\n" +
		"            current.group = groupID;" + "\n" +
		"            " + "\n" +
		"            addAdjacentStars(current, groupID, linkingLength, stars);" + "\n" +	// 13
		"        }" + "\n" +
		"    }" + "\n" +
		"};" + "\n" +
		"" + "\n" +
		"// Helper algorithm for FoF. Calls nearest neighbour search to get all adjacent stars." + "\n" +
		"private void addAdjacentStars(Star s, int groupID, double linkingLength, Set<Star> stars)" + "\n" +		// 19
		"{" + "\n" +
		"    Set<Star> adjacentStars = nearestNeighbourSearch(s, linkingLength, stars);" + "\n" +
		"    " + "\n" +
		"    for(Star current : adjacentStars)" + "\n" +		// 23
		"    {" + "\n" +
		"        if(!current.wasVisited())" + "\n" +
		"        {" + "\n" +
		"            current.setVisited(true);" + "\n" +
		"            current.group = groupID;" + "\n" +
		"            " + "\n" +
		"            addAdjacentStars(current, groupID, linkingLength, stars);" + "\n" +	// 30
		"        }" + "\n" +
		"    }" + "\n" +
		"};";
	}

	public String getFileExtension()
	{
		return "asu";
	}

	public Locale getContentLocale()
	{
		return Locale.ENGLISH;
	}

	public GeneratorType getGeneratorType()
	{
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
	}

	public String getOutputLanguage()
	{
		return Generator.JAVA_OUTPUT;
	}
}
