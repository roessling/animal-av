/*
 * SkipListInsertion.java
 * Dorian Arnouts, Alexander Peerdeman, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.datastructures;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;

public class SkipListInsertion implements ValidatingGenerator {
	private static Language lang;
	private Locale locale;
	private Translator translator;

	public SkipListInsertion(Locale l) {
		locale = l;
		translator = new Translator("resources/SkipListInsertion", locale);
	}

	public void init() {
		lang = new AnimalScript("Skip List Insertion", "Dorian Arnouts, Alexander Peerdeman", 800, 600);
	}

	public static void main(String[] args) {
		Generator generator = new SkipListInsertion(Locale.US);
		Animal.startGeneratorWindow(generator);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		// Get Primitives
		double p = (double) primitives.get("probability");
		int seed = (Integer) primitives.get("seed");
		int maxHeight = (Integer) primitives.get("maxHeight");
		int[] existingElements = (int[]) primitives.get("existingElements");
		int[] insertElements = (int[]) primitives.get("elementsToInsert");

		// Get Properties
		TextProperties tp = (TextProperties) props.getPropertiesByName("text");
		TextProperties mtp = (TextProperties) props.getPropertiesByName("monoText");
		RectProperties rectP = (RectProperties) props.getPropertiesByName("rectangles");
		RectProperties rectHighP = (RectProperties) props.getPropertiesByName("rectanglesHighlight");
		SourceCodeProperties scp = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
		PolylineProperties pp = (PolylineProperties) props.getPropertiesByName("polyline");
		PolylineProperties hpp = (PolylineProperties) props.getPropertiesByName("polylineHighlightUpdatedArrows");
		PolylineProperties hpwayp = (PolylineProperties) props.getPropertiesByName("polylineHighlightWay");

		SkipListAnimation.generate(lang, translator, p, seed, maxHeight, existingElements, insertElements, tp, mtp, rectP,
				rectHighP, scp, pp, hpp, hpwayp);
		return lang.toString();
	}

	public String getName() {
		return translator.translateMessage("name");
	}

	public String getAlgorithmName() {
		return translator.translateMessage("algoName");
	}

	public String getAnimationAuthor() {
		return "Dorian Arnouts, Alexander Peerdeman";
	}

	public String getDescription() {
		return translator.translateMessage("algoDescription").replace("quot;", "'");
	}

	public String getCodeExample() {
		return "insert(list, searchKey, newValue)" + "\n" + "    local update[1..MaxLevel]" + "\n"
				+ "    x := list -> header" + "\n" + "    for i := list -> level downto 1 do" + "\n"
				+ "        while x -> forward[i] -> key < searchKey do" + "\n" + "            x := x -> forward[i]"
				+ "\n" + "        update[i] := x" + "\n" + "    x := x -> forward[1]" + "\n"
				+ "    if x -> key = searchKey then" + "\n" + "        x -> value := newValue" + "\n" + "    else"
				+ "\n" + "        lvl := randomLevel()" + "\n" + "            if lvl > list -> level then" + "\n"
				+ "                for i := list -> level + 1 to lvl do" + "\n"
				+ "                    update[i] := list -> header" + "\n" + "                list -> level := lvl"
				+ "\n" + "            x := makeNode(lvl, searchKey, value)" + "\n"
				+ "            for i := 1 to level do" + "\n"
				+ "                x -> forward[i] := update[i] -> forward[i]" + "\n"
				+ "                update[i] -> forward[i] := x";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return locale;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_DATA_STRUCTURE);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		double p = (double) primitives.get("probability");
		if (!(0 <= p && p <= 1)) {
			JOptionPane.showMessageDialog(null, translator.translateMessage("probabilityAllowedValues"),
					translator.translateMessage("invalidInput"), JOptionPane.OK_OPTION);
			return false;
		}
		return true;
	}
}

/**
 * @author Dorian Arnouts, Alexander Peerdeman
 */
class SkipListAnimation {
	private double probability;
	private int maxLevel;
	private Language lang;
	private Translator t;

	// current level
	private int level;

	// update vector
	private SkipListNode[] update;

	// head node
	private SkipListNode head;

	// tail node
	private TextRect[] nullTower;

	// created TextRects
	private List<TextRect> textRects;
	// created Polylines
	private List<Polyline> polylines;

	// Style objects
	private PolylineProperties polyProp;
	private PolylineProperties highlightPolyProp;
	private PolylineProperties markWayPolyProp;
	private TextProperties textProps;
	private TextProperties monoTextProps;
	private RectProperties rectProps;
	private TextProperties headerTextProps;
	private RectProperties rectHighProps;
	private RectProperties backgroundRectProps;
	private RectProperties backgroundRectHighProps;
	private SourceCodeProperties sourceCodeProp;

	private Polyline currentPointer;
	private Text currentPointerText;

	private SourceCode code;
	private Text message;
	private Text header;
	private Random rng;

	private int ID = 0; // to name all primitives differently
	private final int currentPointerLength = 25;
	private int verticalDistanceBetweenCells = 35;
	private int verticalStartPosTextRects = 100;
	private int widthOfArrows = 45;

	private boolean performInBackground = true;

	public SkipListAnimation(Language l, Translator t, double p, int s, int h, TextProperties tp, TextProperties mtp, RectProperties rectP,
			RectProperties rectHighP, SourceCodeProperties scp, PolylineProperties pp, PolylineProperties hpp,
			PolylineProperties hpwayp) {
		this.rng = new Random(s);
		this.probability = p;
		this.maxLevel = h;
		this.lang = l;
		this.t = t;

		this.head = new SkipListNode(maxLevel, Integer.MIN_VALUE);
		this.nullTower = new TextRect[maxLevel];
		// initialze List textRects
		this.textRects = new LinkedList<>();
		// initialze List polylines
		this.polylines = new LinkedList<>();
		// initially there is a single level
		this.level = 1;

		this.lang.setStepMode(true);

		initializeStyles(tp, mtp, rectP, rectHighP, scp, pp, hpp, hpwayp);
	}

	public static void generate(Language l, Translator t, double p, int s, int h, int[] existing, int[] insert,
			TextProperties tp, TextProperties mtp, RectProperties rectP, RectProperties rectHighP, SourceCodeProperties scp,
			PolylineProperties pp, PolylineProperties hpp, PolylineProperties hpwayp) {
		SkipListAnimation sla = new SkipListAnimation(l, t, p, s, h, tp, mtp, rectP, rectHighP, scp, pp, hpp, hpwayp);
		sla.insertAll(existing, insert);
	}

	/**
	 * Initializes style objects for later use
	 */
	private void initializeStyles(TextProperties tp, TextProperties mtp, RectProperties rectP, RectProperties rectHighP,
			SourceCodeProperties scp, PolylineProperties pp, PolylineProperties hpp, PolylineProperties hpwayp) {

		// polyline style declarations
		polyProp = pp;
		highlightPolyProp = hpp;
		markWayPolyProp = hpwayp;

		// mono text style declaration
		monoTextProps = mtp;

		// text style declaration

		textProps = tp;

		// rect style declarations
		backgroundRectProps = new RectProperties();
		backgroundRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		backgroundRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				(Color) rectP.getItem(AnimationPropertiesKeys.COLOR_PROPERTY).get());
		backgroundRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
				(Color) rectP.getItem(AnimationPropertiesKeys.COLOR_PROPERTY).get());
		backgroundRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
		backgroundRectHighProps = new RectProperties();
		backgroundRectHighProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				(Color) rectHighP.getItem(AnimationPropertiesKeys.COLOR_PROPERTY).get());
		backgroundRectHighProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
				(Color) rectHighP.getItem(AnimationPropertiesKeys.COLOR_PROPERTY).get());

		rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
				(Color) rectP.getItem(AnimationPropertiesKeys.FILL_PROPERTY).get());
		rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				(Color) rectP.getItem(AnimationPropertiesKeys.COLOR_PROPERTY).get());
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		rectHighProps = new RectProperties();
		rectHighProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				(Color) rectHighP.getItem(AnimationPropertiesKeys.COLOR_PROPERTY).get());
		rectHighProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
				(Color) rectHighP.getItem(AnimationPropertiesKeys.FILL_PROPERTY).get());

		// source code declaration
		sourceCodeProp = scp;
	}

	/**
	 * @param polyline the polyline to unhighlight
	 * @return the unhighlighted polyline
	 */
	private Polyline unhighlight(Polyline polyline) {
		if (polyline != null) {
			polyline.changeColor(null, (Color) polyProp.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
			return polyline;
		}
		return null;

	}

	/**
	 * @param polyline the polyline to highlight
	 * @return the highlighted polyline
	 */
	private Polyline highlight(Polyline polyline) {
		if (polyline != null) {
			polyline.changeColor(null, (Color) markWayPolyProp.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
			return polyline;
		}
		return null;
	}

	/**
	 * @return a random level between 1 and maxLevel. With a probability of .5 it
	 *         mimics a coin flip
	 */
	private int randomLevel() {
		int lvl = 1;
		while (rng.nextDouble() < probability && lvl < maxLevel) {
			lvl++;
		}
		return lvl;
	}

	/**
	 * Returns an offset to the center of one of the textrects sides
	 * 
	 * @param position which side of textrect to use
	 * @param tr       the textrect
	 * @return an offset to the center of one of the textrects sides
	 */
	private Offset getRectAnchor(String position, TextRect tr) {
		return getRectAnchor(position, tr, 0);
	}

	/**
	 * Returns an offset plus an additional offset to the center of one of the
	 * textrects sides
	 * 
	 * @param position which side of textrect to use
	 * @param tr       the textrect
	 * @param offset   number of pixels to offset from the center
	 * @return an offset to the center of one of the textrects sides
	 */
	private Offset getRectAnchor(String position, TextRect tr, int offset) {
		// Offset is created relative to text object
		switch (position) {
		case "EC":
			// east center
			int lrx = ((Offset) tr.background.getLowerRight()).getX();
			return new Offset(lrx + offset, 0, tr.text, AnimalScript.DIRECTION_E);
		case "NC":
			// north center
			int uly = ((Offset) tr.background.getUpperLeft()).getY();
			return new Offset(0, uly + offset, tr.text, AnimalScript.DIRECTION_N);
		case "SC":
			// south center
			int lry = ((Offset) tr.background.getLowerRight()).getY();
			return new Offset(0, lry + offset, tr.text, AnimalScript.DIRECTION_S);
		case "WC":
			// west center
			int ulx = ((Offset) tr.background.getUpperLeft()).getX();
			return new Offset(ulx + offset, 0, tr.text, AnimalScript.DIRECTION_W);
		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Generates a TextRect at specific coordinates
	 * 
	 * @param x     x position
	 * @param y     y position
	 * @param value Text that it should display
	 * @return a TextRect that contains the given text
	 */
	private TextRect generateRectWithText(int x, int y, String value) {
		// text
		Text text = lang.newText(new Coordinates(x, y), value, "Text[" + ID++ + "]", null, monoTextProps);

		// inner rectangle
		int offsetH = 6;
		int offsetV = 4;
		Offset offsetNW = new Offset(-offsetH, -offsetV, text, AnimalScript.DIRECTION_NW);
		Offset offsetSE = new Offset(offsetH, offsetV, text, AnimalScript.DIRECTION_SE);
		Rect rect = lang.newRect(offsetNW, offsetSE, "Rect[" + ID++ + "]", null, rectProps);

		// outer rectangle
		int borderWidth = 1;
		offsetNW = new Offset(-offsetH - borderWidth, -offsetV - borderWidth, text, AnimalScript.DIRECTION_NW);
		offsetSE = new Offset(offsetH + borderWidth, offsetV + borderWidth, text, AnimalScript.DIRECTION_SE);
		Rect background = lang.newRect(offsetNW, offsetSE, "RectBackground[" + ID + "]", null, backgroundRectProps);

		TextRect tr = new TextRect(text, rect, background);
		textRects.add(tr);
		return tr;
	}

	/**
	 * Creates a header within a rectangle as headline for the animation
	 */
	private List<Primitive> createHeader() {
		headerTextProps = new TextProperties();
		headerTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));

		RectProperties headerRectProps = new RectProperties();
		headerRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		headerRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		headerRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		header = lang.newText(new Coordinates(20, 30), t.translateMessage("headerText"), "header", null,
				headerTextProps);

		Rect headerRect = lang.newRect(new Offset(-10, -5, header.getName(), AnimalScript.DIRECTION_NW),
				new Offset(10, 5, "header", AnimalScript.DIRECTION_SE), "hRect", null, headerRectProps);

		List<Primitive> headerPrim = new LinkedList<>();
		headerPrim.add(header);
		headerPrim.add(headerRect);

		return headerPrim;
	}

	/**
	 * Creates source code object which contains the pseudo-code
	 */
	private void createSourceCode() {
		// put it below skip list visualization
		Offset offset = new Offset(0, verticalDistanceBetweenCells + currentPointerLength + 5,
				head.tower[0].getCoordinates(), AnimalScript.DIRECTION_S);

		this.code = lang.newSourceCode(offset, "sourceCode", null, sourceCodeProp);

		String[] lines = { "insert(list, searchKey, newValue)", "	local update[1..MaxLevel]",
				"	x := list -> header", "	for i := list -> level downto 1 do",
				"		while x -> forward[i] -> key < searchKey do", "			x := x -> forward[i]",
				"		update[i] := x", "	x := x -> forward[1]", "	if x -> key = searchKey then",
				"		x -> value := newValue", "	else", "		lvl := randomLevel()",
				"		if lvl > list -> level then", "			for i := list -> level + 1 to lvl do",
				"				update[i] := list -> header", "			list -> level := lvl",
				"		x := makeNode(lvl, searchKey, value)", "		for i := 1 to level do",
				"			x -> forward[i] := update[i] -> forward[i]", "			update[i] -> forward[i] := x" };

		// add lines to a source code object
		for (String line : lines) {
			// as it does not display tabs initially, count them and use "indentation"
			int tabs = 0;
			for (char c : line.toCharArray()) {
				if ("\t".equals(c + "")) {
					tabs++;
				}
			}
			this.code.addCodeLine(line, null, tabs, null);
		}
	}

	/**
	 * Resets the highlighted / marked status of all TextRects, polylines and source
	 * code lines. Used after an insertion is completed.
	 * 
	 * @param all determines if marked TextRects should be unmarked too
	 */
	private void resetColor(boolean all) {
		// begin at head
		SkipListNode current = head;
		// iterate through all SkipListNodes and unhighlight tower and pointers
		while (current != null) {
			for (TextRect textRect : current.tower) {
				if (textRect != null) {
					textRect.unhighlight(backgroundRectProps);
					if (all)
						textRect.unmark(rectProps);
				}
			}
			for (Polyline polyline : current.pointers) {
				if (polyline != null)
					unhighlight(polyline);
			}
			// go to next
			current = current.next[0];
		}
		// unhighlight code
		for (int i = 0; i < code.length(); i++) {
			code.unhighlight(i);
		}
	}

	/**
	 * Entry point for generating animation. First build skip list with existing
	 * elements, then insert remaining elements and visualize these operations.
	 * 
	 * @param existingElements elements already inside skip list before animation
	 *                         starts
	 * @param insertElements   elements to be inserted within the animation
	 */
	void insertAll(int[] existingElements, int[] insertElements) {
		// create the header
		List<Primitive> titlePrimitves = createHeader();

		// first slide is an introduction
		createIntroSlide();

		// set up and place message placeholder
		createMessage(insertElements);

		// Generate starting Position
		for (int i = 0; i < maxLevel; i++) {
			// generate head node
			TextRect headNode = generateRectWithText(50, verticalDistanceBetweenCells * i + verticalStartPosTextRects,
					"head");
			head.tower[maxLevel - i - 1] = headNode;

			// generate tail node
			TextRect nullNode = generateRectWithText(widthOfArrows + 50 + getWidthOfString("Head", monoTextProps),
					verticalDistanceBetweenCells * i + verticalStartPosTextRects, "null");
			nullTower[maxLevel - i - 1] = nullNode;

			// generate initial pointers from head to tail
			createPolylineToSuccessor(head, maxLevel - i - 1);
		}

		// set up source code
		createSourceCode();

		Arrays.sort(existingElements);
		for (int i : existingElements) {
			insert(i);
		}
		// from here generate animation code
		performInBackground = false;
		nextStep();
		for (int i : insertElements) {
			insert(i);
		}

		// hide Primitives except Header
		lang.hideAllPrimitivesExcept(titlePrimitves);
		for (TextRect tr : textRects) {
			tr.text.hide();
			tr.rect.hide();
			tr.background.hide();
		}
		for (Polyline p : polylines) {
			p.hide();
		}

		// last slide is a conclusion
		createConclusionSlide();
	}

	/**
	 * Creates and places a message placeholder at the top of the viewport. Consider
	 * elements to be inserted to avoid overlapping.
	 * 
	 * @param insertElements elements to be inserted
	 */
	private void createMessage(int[] insertElements) {
		// find widest element to place message accordingly, avoiding overlapping.
		int offset = -1;
		for (int i : insertElements) {
			int width = getWidthOfString(Integer.toString(i), textProps);
			if (width > offset) {
				offset = width;
			}
		}
		System.out.println("offset: " + offset);
		// int offset = Arrays.stream(insertElements).map(el ->
		// getWidthOfString(Integer.toString(el), headerTextProps)).max().orElse(0); //
		// create message Text
		message = lang.newText(new Offset(offset + 55, -7, header, AnimalScript.DIRECTION_E), "", "messageText", null,
				textProps);
	}

	/**
	 * Creates introduction slide with algorithm description
	 */
	private void createIntroSlide() {
		SourceCodeProperties scp = new SourceCodeProperties();
		scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		scp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		SourceCode sc = lang.newSourceCode(new Offset(0, 20, header, AnimalScript.DIRECTION_SW), "intro", null, scp);

		// split the description at \n to put it into separate lines
		for (String introLine : t.translateMessage("algoDescription").split("\n")) {
			sc.addCodeLine(introLine.replace("quot;", "'"), "intro", 0, null);
		}

		lang.nextStep(t.translateMessage("introduction"));
		sc.hide();
	}

	/**
	 * Creates conclusion slide
	 */
	private void createConclusionSlide() {
		SourceCodeProperties scp = new SourceCodeProperties();
		scp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		scp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		SourceCode sc = lang.newSourceCode(new Offset(0, 20, header, AnimalScript.DIRECTION_SW), "conclusion", null,
				scp);

		// split the conclusion at \n to put it into separate lines
		for (String ConclusionLine : t.translateMessage("algoConclusion").split("\n")) {
			sc.addCodeLine(ConclusionLine.replace("quot;", "'"), "conclusion", 0, null);
		}

		lang.nextStep(t.translateMessage("conclusion"));
		sc.hide();
	}

	/**
	 * Insert a new element into the skip list. If the key already exists inside the
	 * list the value is going to be overwritten.
	 *
	 * @param searchKey the key to insert the new node at
	 */
	private boolean insert(int searchKey) {
		// show element to be inserted
		message(t.translateMessage("elementToBeInserted"));
		TextRect insertionPreview = showElementToBeInserted(searchKey);
		code.highlight(0);
		nextStep(t.translateMessage("insertActionName", Integer.toString(searchKey)));
		code.unhighlight(0);

		// holds pointers to the node ahead of the one to be inserted
		update = new SkipListNode[maxLevel];
		code.highlight(1);
		nextStep();

		code.unhighlight(1);

		// place current-pointer
		SkipListNode current = this.head;
		currentPointer = null;
		currentPointerText = null;
		updateCurrentPointer(current);
		code.highlight(2);
		nextStep();

		message(t.translateMessage("currentHeight", Integer.toString(this.level)));
		nextStep();

		code.unhighlight(2);
		Polyline lastHighlighted = null;

		// descend to the lowest level. Follow the topmost pointers to insert position
		for (int i = level - 1; i >= 0; i--) {
			if (i == level - 1) {
				message(t.translateMessage("startAtHeadOnHeight", Integer.toString(i + 1)));
			} else {
				message(t.translateMessage("descendLevelInCurrentNode"));
			}
			code.highlight(3);
			unhighlight(lastHighlighted);
			current.tower[i].highlight(backgroundRectHighProps);
			nextStep();

			lastHighlighted = highlight(current.pointers[i]);
			message(t.translateMessage("nextElementIs", nullOrString(current.next[i])));
			nextStep();

			code.highlight(4);

			// follow the pointers on current level towards the insert position
			while (current.next[i] != null && current.next[i].getKey() < searchKey) {
				message(t.translateMessage("compare", Integer.toString(current.next[i].getKey()),
						Integer.toString(searchKey)));
				nextStep();

				code.highlight(5);
				current = current.next[i];
				updateCurrentPointer(current);
				current.tower[i].highlight(backgroundRectHighProps);
				message(t.translateMessage("gotoNextElement", Integer.toString(current.getKey())));
				nextStep();

				message(t.translateMessage("nextElementIs", nullOrString(current.next[i])));
				lastHighlighted = highlight(current.pointers[i]);
				nextStep();
			}
			unhighlight(lastHighlighted);
			if (current.next[i] == null) {
				message(t.translateMessage("nextElementIsNullStop"));
			} else {
				message(t.translateMessage("stopTraversal", current.next[i].toString(), Integer.toString(searchKey)));
			}
			nextStep();

			code.unhighlight(4);
			code.unhighlight(5);
			// save current pointer in update vector
			update[i] = current;
			message(t.translateMessage("savingPointer"));
			markNodeOnLevel(update, i);
			code.highlight(6);
			nextStep();

			code.unhighlight(3);
			code.unhighlight(6);
		}
		message(t.translateMessage("updateVectorComplete"));

		// reset only highlighting, keep TextRects marked
		resetColor(false);
		current = current.next[0];
		updateCurrentPointer(current);
		code.highlight(7);
		nextStep();

		code.unhighlight(7);

		message(t.translateMessage("checkIfExists"));
		code.highlight(8);
		nextStep();

		code.unhighlight(8);
		if (current != null && current.getKey() == searchKey) {
			// if there is already a node with searchKey, overwrite value.
			message(t.translateMessage("elementAlreadyExists"));
			resetColor(true);
			code.highlight(8);
			code.highlight(9);
			nextStep();

			code.unhighlight(8);
			code.unhighlight(9);
			insertionPreview.hide();
			hideCurrentPointer();
			message("");
			return false;
		} else {
			// otherwise create new node and update pointers
			code.highlight(10);

			// determine random level
			int height = randomLevel();
			message(t.translateMessage("determineRandomHeight", Integer.toString(height)));
			code.highlight(11);
			nextStep();

			code.unhighlight(11);
			message(t.translateMessage("checkLevelNewElementHigherThanMax"));
			code.highlight(12);
			nextStep();

			code.unhighlight(12);
			if (height > this.level) {
				// increase vector height
				message(t.translateMessage("yesIncreaseVectorHeight", Integer.toString(level),
						Integer.toString(height - level)));
				nextStep();

				code.highlight(12);
				for (int i = this.level; i < height; i++) {
					code.highlight(13);
					update[i] = this.head;
					markNodeOnLevel(update, i);
					code.highlight(14);
				}
				message(t.translateMessage("forEveryLevelOfNewElement"));
				nextStep();

				code.unhighlight(13);
				code.unhighlight(14);
				this.level = height;
				message(t.translateMessage("updateMaxLevel"));
				code.highlight(15);
				nextStep();

				code.unhighlight(15);
				code.unhighlight(12);
			}

			// create the new node
			SkipListNode newNode = new SkipListNode(maxLevel, searchKey);
			for (int i = 0; i < height; i++) {
				newNode.next[i] = update[i].next[i];
				update[i].next[i] = newNode;
				createTextRectInNodeOnLevel(newNode, i);
				newNode.tower[i].hide();
			}

			// move all succeeding nodes to make space for new node
			moveNodes(newNode);

			// also move currentPointer
			updateCurrentPointer(current);

			// reconnect pointers
			increasePointerLength(height, newNode);
			nextStep();

			// show new node
			newNode.show();
			current = newNode;
			updateCurrentPointer(current);
			message(t.translateMessage("createNewElement", Integer.toString(searchKey)));
			code.highlight(16);
			nextStep();

			code.unhighlight(16);
			message(t.translateMessage("markedPointersNeedUpdating"));
			// mark now incorrect pointers
			for (int i = 0; i < height; i++) {
				markInvalidPointer(i);
			}
			nextStep();

			// correct pointers
			for (int i = 0; i < height; i++) {
				code.highlight(17);
				Polyline p = update[i].pointers[i];

				// update marked pointer on level i to successor of newNode
				correctPointerToSuccessor(i);
				code.highlight(18);
				nextStep();

				code.unhighlight(18);

				// update marked pointer on level i to newNode
				correctPointerToNewNode(i);
				message(t.translateMessage("pointerOnLevelCorrected", Integer.toString(i)));
				hideInvalidPointer(p);
				code.highlight(19);
				nextStep();

				code.unhighlight(19);
			}
			message(t.translateMessage("elementXInsertedSuccess", Integer.toString(searchKey)));
			code.unhighlight(10);
			code.unhighlight(17);
			nextStep();

			// reset everything
			resetColor(true);
			insertionPreview.hide();
			message("");
			hideCurrentPointer();
			nextStep();
		}
		return true;
	}

	/**
	 * Updates position of current-pointer that points to the current node
	 * 
	 * @param cur current node
	 */
	private void updateCurrentPointer(SkipListNode cur) {
		hideCurrentPointer();

		TextRect tr = cur == null ? nullTower[0] : cur.tower[0];
		Node target = getRectAnchor("SC", tr);
		Node source = getRectAnchor("SC", tr, currentPointerLength);

		Node[] nodes = { source, target };
		currentPointer = lang.newPolyline(nodes, "currentPointer", null, polyProp);
		polylines.add(currentPointer);
		currentPointerText = lang.newText(new Offset(-4, 2, currentPointer, AnimalScript.DIRECTION_S), "x",
				"currentPointerText", null, monoTextProps);
	}

	/**
	 * Hides current-pointer
	 */
	private void hideCurrentPointer() {
		if (currentPointer != null)
			currentPointer.hide();
		if (currentPointerText != null)
			currentPointerText.hide();
	}

	/**
	 * Checks if an object actually exists. If not return null as string
	 * 
	 * @param o the object
	 * @return "null" or the object's string representation
	 */
	private String nullOrString(Object o) {
		return (o == null) ? "null" : o.toString();
	}

	/**
	 * Sets the message to display
	 * 
	 * @param messageText the message to display
	 */
	private void message(String messageText) {
		message.setText(messageText, null, null);

	}

	/**
	 * highlights the pointers in a special color to mark them invalid
	 * 
	 * @param i level on which to mark the pointers on
	 */
	private void markInvalidPointer(int i) {
		update[i].pointers[i].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
				(Color) highlightPolyProp.getItem(AnimationPropertiesKeys.COLOR_PROPERTY).get(), null, null);
	}

	/**
	 * Places a corrected pointer to the new successor on level i
	 * 
	 * @param i level on which to create the pointer on
	 */
	private void correctPointerToSuccessor(int i) {
		createPolylineToSuccessor(update[i].next[i], i);
	}

	/**
	 * Places a corrected pointer to the new node on level i
	 * 
	 * @param i level on which to create the pointer on
	 */
	private void correctPointerToNewNode(int i) {
		createPolylineToSuccessor(update[i], i);
	}

	/**
	 * Hides a pointer
	 * 
	 * @param p the pointer to hide
	 */
	private void hideInvalidPointer(Polyline p) {
		p.hide();
	}

	/**
	 * Places a new pointer with increased length, hides the old one. If the given
	 * node is null use head as source of the pointer.
	 * 
	 * @param level the level on which to recreate the pointer
	 * @param node  which contains the old pointer
	 */
	private void increasePointerLength(int level, SkipListNode node) {
		for (int i = 0; i < maxLevel; i++) {
			SkipListNode source = update[i] == null ? head : update[i];
			if (source.pointers[i] != null)
				source.pointers[i].hide();
			SkipListNode target = i < level ? node : source;
			createPolylineTo(source, target.next[i], i);
		}
	}

	/**
	 * Moves nodes by width of a new node
	 * 
	 * @param newNode the node which is made space for
	 */
	private void moveNodes(SkipListNode newNode) {
		int widthOfText = getWidthOfNode(newNode);
		int dx = widthOfText + widthOfArrows;

		SkipListNode current = newNode.next[0];
		while (current != null) {
			moveNode(current, dx);
			current = current.next[0];
		}
		for (int i = 0; i < maxLevel; i++) {
			nullTower[i] = moveTextRect(nullTower[i], dx);
		}
	}

	/**
	 * Marks a node. Used to display update vector
	 * 
	 * @param node the node to mark
	 * @param i    which level of the node to mark
	 */
	private void markNodeOnLevel(SkipListNode[] node, int i) {
		if (node[i] != null && node[i].tower[i] != null) {
			node[i].tower[i].mark(rectHighProps);
		}
	}

	/**
	 * Generates and shows a preview of the element to be inserted
	 * 
	 * @param searchKey key of element to be inserted
	 * @return a TextRect which contains the element to be inserted
	 */
	private TextRect showElementToBeInserted(int searchKey) {
		int x = ((Coordinates) header.getUpperLeft()).getX() + getWidthOfString(header.getText(), headerTextProps) + 37;
		int y = ((Coordinates) header.getUpperLeft()).getY() + 7;
		return generateRectWithText(x, y, searchKey + "");
	}

	/**
	 * Creates a TextRect in a given node on a given level
	 * 
	 * @param newNode node to generate the TextRect in
	 * @param level   level on which to generate the TextRect on
	 */
	private void createTextRectInNodeOnLevel(SkipListNode newNode, int level) {
		TextRect oldTR;
		if (newNode.next[0] == null && level == 0) {
			// next tower is the nullTower && towers are not yet moved
			oldTR = nullTower[level];
		} else if (level == 0) {
			// next tower is NOT the nullTower && towers are not yet moved
			oldTR = newNode.next[0].tower[0];
		} else {
			// newNode on level 0 is yet inserted, the x value is the same
			oldTR = newNode.tower[0];
		}
		int x = ((Coordinates) oldTR.text.getUpperLeft()).getX();
		int y = ((Coordinates) head.tower[level].text.getUpperLeft()).getY();

		TextRect newTR = generateRectWithText(x, y, Integer.toString(newNode.getKey())); // create new TextRec

		newNode.tower[level] = newTR;
	}

	/**
	 * Moves the given SkipListNode TextRects to the right. Also moves the
	 * referenced pointers
	 * 
	 * @param node SkipListNode to be moved to the right
	 * @param dx   amount to move it to the right by
	 */
	private void moveNode(SkipListNode node, int dx) {
		for (int i = 0; i < node.tower.length; i++) {
			node.tower[i] = moveTextRect(node.tower[i], dx);
			movePointer(node.pointers[i], dx);
		}
	}

	/**
	 * Moves (actually moves) the given pointer to the right
	 * 
	 * @param p  Pointer to be moved
	 * @param dx amount to move the pointer to the right by
	 */
	private void movePointer(Polyline p, int dx) {
		if (p != null)
			p.moveBy(null, dx, 0, null, null);
	}

	/**
	 * Deletes / hides the given instance of TextRect and creates a new one at the
	 * desired position
	 * 
	 * @param tr TextRect to be moved
	 * @param dx amount to move it to the right
	 * @return new instance of TextRect at the desired position
	 */
	private TextRect moveTextRect(TextRect tr, int dx) {
		if (tr == null)
			return null;
		Coordinates coords = tr.getCoordinates();
		tr.hide();
		return generateRectWithText(coords.getX() + dx, coords.getY(), tr.text.getText());
	}

	/**
	 * Creates a polyline on the desired level from starting SkipListNode node to
	 * its successor (according to node.next)
	 * 
	 * @param node  Starting node of the to be created polyline
	 * @param level level on which to create the polyline on
	 * @return a new polyline between node and its successor
	 */
	private Polyline createPolylineToSuccessor(SkipListNode node, int level) {
		return createPolylineTo(node, node.next[level], level);
	}

	/**
	 * Creates a polyline on the desired level from starting SkipListNode node to
	 * its successor (according to node.next)
	 * 
	 * @param node  Source node of the to be created polyline
	 * @param to    Target node of the to be created polyline
	 * @param level level on which to create the polyline on
	 * @return a new polyline between Source and Target node
	 */
	private Polyline createPolylineTo(SkipListNode node, SkipListNode to, int level) {
		// get east center anchor of starting TextRect
		Node source = getRectAnchor("EC", node.tower[level]);

		// determine whether the successor is another SkipListNode or the nullTower
		TextRect successor = to == null ? nullTower[level] : to.tower[level];

		// get west center anchor of target TextRect
		Node target = getRectAnchor("WC", successor);

		Node[] nodes = { source, target };

		Polyline line = lang.newPolyline(nodes, "Polyline[" + ID++ + "]", null, polyProp);
		polylines.add(line);
		return node.pointers[level] = line;
	}

	/**
	 * @param node node of which to determine the text width of
	 * @return returns the width of a text in a SkipListNode
	 */
	private int getWidthOfNode(SkipListNode node) {
		return getWidthOfString(Integer.toString(node.getKey()), monoTextProps);
	}

	/**
	 * @param string text of which to determine the width of
	 * @param props
	 * @return returns the width of a text
	 */
	private int getWidthOfString(String string, TextProperties props) {
		FontMetrics metrics = new Canvas().getFontMetrics((Font) props.get(AnimationPropertiesKeys.FONT_PROPERTY));
		return metrics.stringWidth(string);
	}

	/**
	 * Start the next step if performInBackground is false
	 */
	private void nextStep(String message) {
		if (!performInBackground) {
			lang.nextStep(message);
		}
	}

	private void nextStep() {
		nextStep("");
	}
}

/**
 * Implementation of a node inside a skip list
 * 
 * @author Dorian Arnouts, Alexander Peerdeman
 */
class SkipListNode {

	private int key;
	public SkipListNode[] next;

	/**
	 * Contains all TextRects associated with this SkipListNode.
	 */
	public TextRect[] tower;
	public Polyline[] pointers;

	/**
	 * @param key
	 * @param maxLevel
	 */
	SkipListNode(int maxLevel, int key) {
		this.key = key;
		next = new SkipListNode[maxLevel];
		tower = new TextRect[maxLevel];
		pointers = new Polyline[maxLevel];
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String toString() {
		return String.valueOf(key);
	}

	public void show() {
		for (TextRect tr : tower) {
			if (tr != null)
				tr.show();
		}
	}
}

/**
 * Text with a border and background
 * 
 * @author Dorian Arnouts, Alexander Peerdeman
 */
class TextRect {
	public Text text;
	public Rect rect;
	public Rect background;

	public TextRect(Text text, Rect rect, Rect background) {
		this.text = text;
		this.rect = rect;
		this.background = background;
	}

	public void hide() {
		text.hide();
		rect.hide();
		background.hide();
	}

	public void highlight(RectProperties rectBackgroundHighP) {
		background.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
				(Color) rectBackgroundHighP.getItem(AnimationPropertiesKeys.COLOR_PROPERTY).get(), null, null);
		background.changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
				(Color) rectBackgroundHighP.getItem(AnimationPropertiesKeys.FILL_PROPERTY).get(), null, null);
		rect.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
				(Color) rectBackgroundHighP.getItem(AnimationPropertiesKeys.COLOR_PROPERTY).get(), null, null);
	}

	public void mark(RectProperties rectBoxHighP) {
		rect.changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
				(Color) rectBoxHighP.getItem(AnimationPropertiesKeys.FILL_PROPERTY).get(), null, null);
	}

	public void unmark(RectProperties rectBoxP) {
		rect.changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
				(Color) rectBoxP.getItem(AnimationPropertiesKeys.FILL_PROPERTY).get(), null, null);
	}

	public void unhighlight(RectProperties rectBackgroundP) {
		background.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
				(Color) rectBackgroundP.getItem(AnimationPropertiesKeys.COLOR_PROPERTY).get(), null, null);
		background.changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
				(Color) rectBackgroundP.getItem(AnimationPropertiesKeys.COLOR_PROPERTY).get(), null, null);
		rect.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
				(Color) rectBackgroundP.getItem(AnimationPropertiesKeys.COLOR_PROPERTY).get(), null, null);
	}

	public Coordinates getCoordinates() {
		return (Coordinates) text.getUpperLeft();
	}

	public String toString() {
		return "[" + text.getText() + "]";
	}

	public void show() {
		text.show();
		rect.show();
		background.show();
	}
}
