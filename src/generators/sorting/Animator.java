package generators.sorting;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import algoanim.animalscript.AnimalGroupGenerator;
import algoanim.animalscript.AnimalPolylineGenerator;
import algoanim.animalscript.AnimalRectGenerator;
import algoanim.animalscript.AnimalSourceCodeGenerator;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Group;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.SourceCodeGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.MsTiming;
import algoanim.util.Node;

/**
 * 
 * @author Steffen Frank Schmidt
 *
 */
public class Animator {
	private Language lang;

	// variables for storing primitives
	private String uuid;

	// variables for positioning
	private int title_x;
	private int title_y;
	private int nob_x;
	private int nob_y;
	private int elements_x;
	private int elements_y;
	private int maximum_x;
	private int maximum_y;
	private int index_x;
	private int index_y;
	private int buckets_x;
	private int buckets_y;
	private int sequences_title_x;
	private int sequences_title_y;
	private int sequences_x;
	private int sequences_y;
	private int quicksort_x;
	private int quicksort_y;
	private int pivot_x;
	private int pivot_y;
	private int sorted_x;
	private int sorted_y;
	private int output_x;
	private int output_y;
	private int pseudoCode_x;
	private int pseudoCode_y;

	// variables for primitive creation
	private PolylineProperties arrowProperties;
	private RectProperties rectProperties;
	private TextProperties elementTextProps;
	private TextProperties textProps;
	private ArrayProperties arrayProps;
	private Object displayProps;
	private PolylineProperties lineProperties;
	private SourceCodeProperties scProps;
	private RectProperties sc_background_props;
	private TextProperties titleProps;

	// variables for caption texts
	private String text_title;
	private String text_input;
	private String text_bucketsort;
	private String text_quicksort;
	private String text_nob;
	private String text_pivot;
	private String text_output;
	private String text_sequences;
	private String text_sorted_buckets;
	private String code[];
	private String[] text_description;
	private String[] text_final;

	private int[] qs_current_bucket = null;
	private String qs_current_bucket_key = null;

	private int[] input_elements = null;

	private int element_text_height;
	private int element_text_border_distance;
	private int character_width;
	private int pseudo_width;
	private int sequence_distance;
	private int bucket_height;
	private int bucket_distance;

	private AnimalRectGenerator animalRectGenerator;
	private AnimalTextGenerator animalTextGenerator;
	private AnimalGroupGenerator animalGroupGenerator;
	private AnimalPolylineGenerator animalPolylineGenerator;
	private SourceCodeGenerator animalSourceCodeGenerator;

	private Color element_highlight_color;
	private Color element_normal_color;
	private Color element_border_color;
	private Color code_highlight_color;
	private Color sc_background_border_color;
	private Color sc_background_color;
	private Color sequence_highlight_color;
	private Color sequence_normal_color;

	private Integer input_max_element;
	private Integer bucketsort_nob;
	private Coordinates position_last_sorted_bucket;
	private LinkedList<String> sequences;
	private LinkedList<Coordinates> prevSequence;

	private PrimitiveOrganizer po;

	/**
	 * Constructor
	 * 
	 * @param lang
	 */
	public Animator(Language lang) {

		this.lang = lang;
		this.lang.setStepMode(true);

		this.po = new PrimitiveOrganizer();

		this.initializeTexts();
		this.initializePositions();
		this.initializeColors();
		this.initializeProps();
		this.initializeGenerators();

		this.element_text_height = 20;
		this.element_text_border_distance = 3;
		this.character_width = 7;
		this.pseudo_width = 3;
		this.sequence_distance = 15;
		this.bucket_distance = 15;
		this.bucket_height = 30;

	}

	/**
	 * Returns data
	 * 
	 * @param array
	 * @return
	 */
	private int[] getData(Integer[] array) {
		int[] data = new int[array.length];
		for (int j = 0; j < array.length; j++) {
			data[j] = array[j];
		}
		return data;
	}

	/**
	 * Generates rectangle shaped field with number / one element of the input
	 * array
	 * 
	 * @param key
	 * @param element
	 * @param i
	 * @param rectUpperLeft
	 * @param rectLowerRight
	 * @param red
	 * @return
	 */
	private Group getElement(String key, String element,
			Coordinates rectUpperLeft, Coordinates rectLowerRight, Color red) {

		Coordinates textUpperLeft = new Coordinates(rectUpperLeft.getX()
				+ element_text_border_distance, rectUpperLeft.getY());
		String groupName = key;
		String rectName = key + "_rect";
		String textName = key + "_text";

		uuid = po.getUUID();
		this.rectProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, red);
		Rect rect = new Rect(animalRectGenerator, rectUpperLeft,
				rectLowerRight, uuid, null, rectProperties);
		po.set(rect, rectName, uuid);

		Text text = getText(textName, element, textUpperLeft, elementTextProps);

		LinkedList<Primitive> primitives = new LinkedList<Primitive>();
		primitives.add(rect);
		primitives.add(text);

		Group group = getGroup(groupName, primitives);

		return group;
	}

	/**
	 * Returns element array / bucket
	 * 
	 * @param string
	 * @param elems
	 * @param position
	 * @return
	 */
	private Group getElementArray(String string, int[] elems,
			Coordinates position) {
		return getElementArray(string, elems, position, sequence_normal_color);
	}

	/**
	 * Generates elements array / input array
	 * 
	 * @param key
	 * @param elems
	 * @param pos
	 * @param red
	 * @return
	 */
	private Group getElementArray(String key, int[] elems, Coordinates pos,
			Color red) {
		String[] elemStrings = new String[elems.length];
		for (int i = 0; i < elems.length; i++) {
			elemStrings[i] = String.valueOf(elems[i]);
		}

		int lastUpperX = pos.getX();
		int lastElementLength = 0;
		int[] myUpperX = new int[elemStrings.length];
		int[] myLength = new int[elemStrings.length];

		LinkedList<Primitive> primitives = new LinkedList<Primitive>();

		for (int i = 0; i < elemStrings.length; i++) {
			String groupName = key + "_element_" + i;
			myUpperX[i] = lastUpperX + lastElementLength + pseudo_width;
			myLength[i] = 5 + elemStrings[i].length() * character_width;

			Coordinates rectUpperLeft = new Coordinates(myUpperX[i], pos.getY());
			Coordinates rectLowerRight = new Coordinates(myUpperX[i]
					+ myLength[i], pos.getY() + element_text_height);

			Group elementObject = getElement(groupName, elemStrings[i],
					rectUpperLeft, rectLowerRight, red);

			lastUpperX = myUpperX[i];
			lastElementLength = myLength[i];

			primitives.add(elementObject);
		}

		Group group = getGroup(key, primitives);

		return group;
	}
	
	/**
	 * Returns group
	 * 
	 * @param key
	 * @param primitives
	 * @return
	 */
	private Group getGroup(String key, LinkedList<Primitive> primitives) {
		po.hide(key);
		uuid = po.getUUID();
		Group group = new Group(animalGroupGenerator, primitives, uuid);
		po.set(group, key, uuid);
		return group;

	}
	
	/**
	 * Return movable polyline
	 * 
	 * @param key
	 * @param nodes
	 * @param props
	 * @return
	 */
	private Polyline getMovablePolyline(String key, Coordinates[] nodes,
			PolylineProperties props) {
		return new Polyline(animalPolylineGenerator, nodes, key, null,
				arrowProperties);
	}

	/**
	 * Return polyline
	 * 
	 * @param key
	 * @param nodes
	 * @return
	 */
	private Polyline getPolyline(String key, Coordinates[] nodes) {
		uuid = po.getUUID();
		Polyline p = new Polyline(animalPolylineGenerator, nodes, uuid, null,
				lineProperties);
		po.set(p, key, uuid);
		return p;

	}
	
	/**
	 * Return text
	 * 
	 * @param key
	 * @param text
	 * @param position
	 * @param textProps
	 * @return
	 */
	private Text getText(String key, String text, Coordinates position,
			TextProperties textProps) {
		po.hide(key);
		uuid = po.getUUID();
		Text text_object = new Text(animalTextGenerator, position, text, uuid,
				(algoanim.util.DisplayOptions) displayProps, textProps);
		po.set(text_object, key, uuid);
		return text_object;
	}

	/**
	 * Return text
	 * 
	 * @param key
	 * @param text
	 * @param position
	 * @return
	 */
	private Text getText(String key, String text, Coordinates position) {
		return getText(key, text, position, textProps);
	}

	/**
	 * Return text block
	 * 
	 * @param key
	 * @param text
	 * @param position
	 */
	private void getTextBlock(String key, String[] text,
			Coordinates position) {
		
		uuid = po.getUUID();
		SourceCode sourceCode = new SourceCode(animalSourceCodeGenerator,
				position, uuid, null,
				scProps);
		po.set(sourceCode, key, uuid);
		Pattern linePattern = Pattern.compile("(?<depth>\\t*)(?<code>.*)");
		for (String line : text) {
			Matcher matcher = linePattern.matcher(line);
			if (matcher.matches()) {
				int depth = matcher.group("depth").length();
				String code = matcher.group("code").trim();
				sourceCode.addCodeLine(code, null, depth, null);
			}
		}
	}

	/**
	 * Calculates width of element array
	 * 
	 * @param array
	 * @return
	 */
	private int getWidthOfElementArray(Group array) {
		Coordinates upperLeft = (Coordinates) ((Rect) ((Group) array
				.getPrimitives().getFirst()).getPrimitives().get(0))
				.getUpperLeft();
		Coordinates lowerRight = (Coordinates) ((Rect) ((Group) array
				.getPrimitives().getLast()).getPrimitives().get(0))
				.getLowerRight();

		return lowerRight.getX() - upperLeft.getX();
	}

	/**
	 * Highlights element
	 * 
	 * @param key
	 */
	private void highlight(String key) {
		highlight(key, null);
	}

	/**
	 * Highlight element
	 * 
	 * @param key
	 * @param color
	 */
	private void highlight(String key, Color color) {
		if (color == null)
			color = element_highlight_color;
		po.get(key).changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
				color, null, null);
	}
	
	/**
	 * Highlights element
	 * 
	 * @param key
	 * @param elements
	 * @param element
	 */
	private void highlightElement(String key, int[] elements, Integer element) {
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] == element) {
				highlight(key + "_element_" + i + "_rect");
			}
		}

	}

	/**
	 * Initializes colors
	 */
	private void initializeColors() {
		this.element_highlight_color = Color.CYAN;
		this.element_normal_color = Color.WHITE;
		this.element_border_color = Color.BLUE;
		this.code_highlight_color = Color.RED;
		this.sc_background_border_color = Color.DARK_GRAY;
		this.sc_background_color = Color.LIGHT_GRAY;
		this.sequence_normal_color = Color.DARK_GRAY;
		this.sequence_highlight_color = Color.BLUE;
	}

	/**
	 * Initializes generators
	 */
	private void initializeGenerators() {
		this.animalRectGenerator = new AnimalRectGenerator(this.lang);
		this.animalTextGenerator = new AnimalTextGenerator(this.lang);
		this.animalGroupGenerator = new AnimalGroupGenerator(this.lang);
		this.animalPolylineGenerator = new AnimalPolylineGenerator(this.lang);
		this.animalSourceCodeGenerator = new AnimalSourceCodeGenerator(
				this.lang);
	}

	/**
	 * Initializes positions
	 */
	private void initializePositions() {
		int x = 40;

		this.title_x = x + 10;
		this.title_y = 20;
		this.nob_x = x;
		this.nob_y = title_y + 30;
		this.elements_x = x;
		this.elements_y = nob_y + 30;
		this.maximum_x = x;
		this.maximum_y = elements_y + 40;
		this.index_x = x + 120;
		this.index_y = maximum_y;
		this.buckets_x = x;
		this.buckets_y = maximum_y + 30;
		this.sequences_title_x = x;
		this.sequences_title_y = buckets_y + 40;
		this.sequences_x = x;
		this.sequences_y = sequences_title_y + 20;
		this.quicksort_x = x;
		this.quicksort_y = sequences_y + 120;
		this.pivot_x = x;
		this.pivot_y = quicksort_y + 20;
		this.sorted_x = x;
		this.sorted_y = pivot_y + 100;

		this.output_x = x;
		this.output_y = buckets_y;

		this.pseudoCode_x = x + (text_sorted_buckets.length() + 1)
				* character_width + 100;
		this.pseudoCode_y = 130;
	}

	/**
	 * Initializes props
	 */
	private void initializeProps() {
		this.titleProps = new TextProperties();
		this.titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 20));

		this.textProps = new TextProperties();
		this.textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.BOLD, 14));
		this.displayProps = null;
		this.arrayProps = new ArrayProperties();
		this.arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Color.BLUE);
		this.arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		this.arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		this.arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.RED);
		// SourceCodeProperties
		this.scProps = new SourceCodeProperties();
		this.scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
				Color.BLUE);
		this.scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.PLAIN, 12));
		this.scProps.set(AnimationPropertiesKeys.INDENTATION_PROPERTY, 30);
		this.scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				code_highlight_color);
		this.scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		this.lineProperties = new PolylineProperties();
		this.lineProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Color.WHITE);
		this.lineProperties.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
		this.arrowProperties = new PolylineProperties();
		this.arrowProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Color.BLACK);
		this.arrowProperties
				.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		this.rectProperties = new RectProperties();
		this.rectProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				element_border_color);
		this.rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		this.rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY,
				Color.WHITE);
		this.sc_background_props = new RectProperties();
		this.sc_background_props.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				sc_background_border_color);
		this.sc_background_props.set(AnimationPropertiesKeys.FILLED_PROPERTY,
				true);
		this.sc_background_props.set(AnimationPropertiesKeys.FILL_PROPERTY,
				sc_background_color);
		this.elementTextProps = new TextProperties();
		this.elementTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font("Monospaced", Font.PLAIN, 12));
	}

	/**
	 * Initializes text fragments
	 */
	private void initializeTexts() {
		this.text_title = "Der Hybridsort-Algorithmus";
		this.text_input = "Eingabe: ";
		this.text_bucketsort = "Bucketsort: ";
		this.text_quicksort = "Quicksort: ";
		this.text_nob = "Eimeranzahl: ";
		this.text_pivot = "Pivot: ";
		this.text_output = "Ausgabe: ";
		this.text_sequences = "Zu sortierende Sequenzen: ";
		this.text_sorted_buckets = "Sortierte Eimer:  ";
		this.code = new String[] {
				"",
				" Bucketsort-Teil:",
				"  1.   Finde Maximum(-element) m",
				"  2.   Lege n leere Eimer (buckets) B0 bis B(n-1) an",
				"   3.   Wähle ein Element e",
				"   4.   Berechne (Eimer-)Index i = e / m * (n-1)",
				"   5.   Ordne Element e in Eimer B* mit entsprechendem Index i ein",
				"   6.   Wiederhole 3-5 für alle verbleibenden Elemente",
				"   Quicksort-Teil:",
				"   7.   Wähle einen Eimer B*",
				"   8.   Wähle Pivot-Element",
				"   9.   Setze L-Zeiger auf das erste Element und den R-Zeiger auf das Element links vom Pivot-Element",
				"   10.   Solange der L-Zeiger kleiner R-Zeiger:",
				"			11.   Bewege L-Zeiger schrittweise nach rechts,",
				"					solange das Element auf das L zeigt kleiner gleich dem Pivotelement ",
				"					ist, oder der L-Zeiger den rechten Rand des Intervalls erreicht.",
				"			12.   Bewege R-Zeiger schrittweise nach links,",
				"					solange das Element auf das R zeigt größer gleich dem Pivotelement",
				"					ist, oder der R-Zeiger den linken Rand des Intervalls erreicht.",
				"			13.   Wenn der L-Zeiger links vom R-Zeiger ist, dann tausche die ",
				"					beiden Elemente auf die L und R zeigen.",
				"   14.   Wenn das Element auf das L zeigt größer ist als das Pivotelement, tausche sie.",
				"   15.   Wiederhole 8-14 für alle verbleibenden (Teil-)sequenzen",
				"   16.   Wiederhole 7-15 für alle verbleibenden Eimer",
				"   17.   Ausgabe der sortierten Liste (konkatenierte sortierte Eimer)" };
		this.text_description = new String[] {
				"Dieser Algorithmus sortiert eine Liste von Zahlen durch Bucketsort und Quicksort. Eine Alternative zu Quicksort ist ganz",
				"oft Heapsort. Wir benutzen aber hier Quicksort. Der Algorithmus ist in zwei Teile einzuteilen: Aufteilen der Eingabeelement in n Eimer",
				"und Sortieren der einzelnen Eimer.",
				"",
				"	Für Bucketsort wird zuerst das größte Element der Eingabeliste gesucht. Anschließend wird die Eingabeliste auf",
				"	die n Eimer verteilt. Zur Aufteilung wird der Eimerindex für jedes Element berechnet:",
				"",
				"			(Eimer-)Index i = e / m * (n-1)",
				"",
				"	Nach Aufteilung in die Eimer wird jeder Eimer mittels Quicksort sortiert.",
				"",
				"	Die Sortierung mit Quicksort erfolgt durch Wahl eines Pivotelements (das Element ganz rechts in der zu sortierenden Sequenz) und ",
				"	anschließendes Teilen des Liste anhand des Pivotelements. Dabei werden zwei Zeiger L und R schrittweise über die zusortierende ",
				"	Sequenz geschoben, bis ein Tausch stattfinden muss. Anschließend wird jede Teilsequenz mit Quicksort sortiert. Man beachte, dass ",
				"	einelementige Listen immer sortiert sind.",
				"	Zur besseren Übersicht über die Rekursivität wird ein informeller \"Stack\" mit zu sortierenden Teilsequenzen geführt. Ist dieser", 
				"	leer, ist der Eimer sortiert.",
				"",
				"Nach Sortierung aller Eimer mit Quicksort werden die sortierten Eimer konkateniert und es entsteht eine sortierte Liste, die",
				"die alle Elemente der Eingabeliste enthält.",
				""};
		this.text_final = new String[] {
				"Die Eingabeliste nun sortiert.",
				"",
				"Unter der Annahme, dass die Elemente gleichverteilt und unabhängig sind, ergibt sich im Best-Case eine Laufzeit von",
				"O(n). Für den allgemeinen Fall ergibt sich jedoch eine deutlich schlechtere Laufzeit. Der Worst-Case wird von der",
				"Laufzeit von Quicksort dominiert und ist damit O(n²)."};
	}

	/**
	 * Moves bucket to position and returns next position in row
	 * 
	 * @param key
	 * @param bucket_position
	 * @return
	 */
	private Coordinates moveBucket(String key, Coordinates bucket_position) {
		return this.moveBucket(key, bucket_position, true);
	}
	
	/**
	 * Moves bucket
	 * 
	 * @param key
	 * @param bucket_position
	 * @param bool
	 * @return
	 */
	private Coordinates moveBucket(String key, Coordinates bucket_position,
			boolean bool) {
		Group bucket_object = (Group) po.get(key);
		Coordinates upperLeft = (Coordinates) ((Rect) ((Group) bucket_object
				.getPrimitives().getFirst()).getPrimitives().getFirst())
				.getUpperLeft();
		Node[] nodes = { upperLeft, bucket_position };
		Polyline p = new Polyline(animalPolylineGenerator, nodes, "polyline",
				null, lineProperties);
		try {
			bucket_object.moveVia(null, null, p, new MsTiming(10),
					new MsTiming(1000));
		} catch (IllegalDirectionException e) {
			e.printStackTrace();
		}
		int bucket_width = getWidthOfElementArray(bucket_object);
		Coordinates upperRight = new Coordinates(bucket_position.getX()
				+ bucket_width
				+ ((bool) ? bucket_distance : element_text_border_distance),
				bucket_position.getY());
		return upperRight;

	}

	/**
	 * Unhighlights something
	 * 
	 * @param string
	 */
	private void unhighlight(String string) {
		unhighlight(string, null);
	}
	
	/***
	 * Unhighlights element
	 * 
	 * @param key
	 * @param color
	 */
	private void unhighlight(String key, Color color) {
		if (color == null)
			color = element_normal_color;
		po.get(key).changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
				color, null, null);
	}

	/**
	 * Unhighlights element
	 * 
	 * @param key
	 * @param data
	 * @param color
	 */
	private void unhighlightElement(String key, int[] data, Color color) {
		for (int i = 0; i < data.length; i++) {
			unhighlightElement(key, data, data[i], color);
		}
	}
	
	/**
	 * Unhighlight element
	 * 
	 * @param key
	 * @param elements
	 * @param element
	 * @param color
	 */
	private void unhighlightElement(String key, int[] elements,
			Integer element, Color color) {
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] == element) {
				unhighlight(key + "_element_" + i + "_rect", color);
			}
		}

	}

	/**
	 * Unhighlights element
	 * 
	 * @param string
	 * @param elements2
	 * @param element
	 */
	private void unhighlightElement(String string, int[] elements2,
			Integer element) {
		unhighlightElement(string, elements2, element, null);
	}

	/**
	 * Sets marker on element array and element i
	 * 
	 * @param key
	 * @param element_index
	 * @param name
	 * @return
	 */
	private Group setMarker(String key, int element_index, String name) {
		po.hide(name);

		Group array = (Group) po.get(key);
		Group element = (Group) array.getPrimitives().get(element_index);

		Rect element_rect = (Rect) element.getPrimitives().getFirst();

		Coordinates upperLeft = (Coordinates) element_rect.getUpperLeft();
		Coordinates lowerRight = (Coordinates) element_rect.getLowerRight();

		int middle = upperLeft.getX() + (lowerRight.getX() - upperLeft.getX())
				/ 2;
		Coordinates peak;
		Coordinates end;
		Text text;
		Polyline p;
		if (name == "L") {
			peak = new Coordinates(middle, upperLeft.getY() - 5);
			end = new Coordinates(middle - 10, upperLeft.getY() - 55);
			text = getText(name + "_text", name, new Coordinates(middle - 10,
					end.getY() - 20));
			Coordinates[] nodes = { end, peak };
			p = getMovablePolyline(name + "_arrow", nodes, arrowProperties);
		} else if (name == "R") {
			peak = new Coordinates(middle, lowerRight.getY() + 5);
			end = new Coordinates(middle + 10, lowerRight.getY() + 55);
			text = getText(name + "_text", name, new Coordinates(middle + 10,
					end.getY() + 5));
			Coordinates[] nodes = { end, peak };
			p = getMovablePolyline(name + "_arrow", nodes, arrowProperties);
		} else {
			peak = new Coordinates(middle, lowerRight.getY() + 5);
			end = new Coordinates(middle, lowerRight.getY() + 55);
			text = getText(name + "_text", name,
					new Coordinates(middle, end.getY() + 5));
			Coordinates[] nodes = { end, peak };
			p = getMovablePolyline(name + "_arrow", nodes, arrowProperties);
		}

		LinkedList<Primitive> primitives = new LinkedList<Primitive>();

		primitives.add(text);
		primitives.add(p);

		return getGroup(name, primitives);
	}
	
	private void showCode(int input_width, int bucketsort_nob){
		this.pseudoCode_x += input_width + (bucketsort_nob + 1)
				* bucket_distance + (bucketsort_nob + 1) * bucket_distance;
		showCode(new Coordinates(pseudoCode_x,pseudoCode_y));
	}
	
	/**
	 * shows source code
	 */
	private void showCode(Coordinates position) {
		
		int x = position.getX();
		int y = position.getY();
		
		po.hide("codebox");
		po.hide("code");
		
		Rect rect = new Rect(animalRectGenerator, new Coordinates(
				x - 15, y - 5), new Coordinates(
				x + 570, y + 425), po.getUUID(),
				(DisplayOptions) displayProps, sc_background_props);
		Text title = getText("title_code", "Der Algorithmus", new Coordinates(
				x, y));
		uuid = po.getUUID();
		SourceCode sourceCode = new SourceCode(animalSourceCodeGenerator,
				new Coordinates(x, y), uuid, null,
				scProps);
		po.set(sourceCode, "code", uuid);
		LinkedList<Primitive> primitives = new LinkedList<Primitive>();
		primitives.add(rect);
		primitives.add(title);
		primitives.add(sourceCode);
		getGroup("codebox", primitives);
		Pattern linePattern = Pattern.compile("(?<depth>\\t*)(?<code>.*)");
		for (String line : code) {
			Matcher matcher = linePattern.matcher(line);
			if (matcher.matches()) {
				int depth = matcher.group("depth").length();
				String code = matcher.group("code").trim();
				sourceCode.addCodeLine(code, null, depth, null);
			}
		}
	}

	/**
	 * Highlights bucket
	 * 
	 * @param bucket
	 * @param i
	 */
	public void highlightBucket(Integer[] bucket, Integer i) {
		unhighlightCode();
		highlightCode(9);
		Color color = element_highlight_color;
		if (qs_current_bucket != null)
			unhighlightElement("buckets_" + (i - 1), qs_current_bucket, color);
		int[] data = getData(bucket);

		for (int j = 0; j < data.length; j++) {
			highlight("buckets_" + i + "_element_" + j + "_rect", color);
		}

		qs_current_bucket = data;

		lang.nextStep();
	}

	/**
	 * Highlights code line
	 * 
	 * @param i
	 */
	public void highlightCode(int i) {
		SourceCode pseudoCode = (SourceCode) po.get("code");
		if (i == -1) {
			for (int j = 0; j < code.length; j++) {
				pseudoCode.highlight(j);
			}
		} else {
			pseudoCode.highlight(i);
		}

	}

	/**
	 * Unhighlights Code line or everything
	 * 
	 * @param i
	 */
	public void unhighlightCode() {
		SourceCode pseudoCode = (SourceCode) po.get("code");
		for (int j = 0; j < code.length; j++) {
			pseudoCode.unhighlight(j);
		}
	
	}

	/**
	 * Highlights maximum element
	 * 
	 * @param maximumElement
	 */
	public void highlightMaximumElement(Integer maximumElement) {
		unhighlightCode();
		highlightCode(2);
		this.highlightElement("elements", input_elements, maximumElement);
		getText("maximum_element", "Maximum: " + maximumElement,
				new Coordinates(maximum_x, maximum_y));
		lang.nextStep();
		this.input_max_element = maximumElement;
		this.unhighlightElement("elements", input_elements, maximumElement,
				null);

	}

	/**
	 * Moves element into its bucket by index
	 * 
	 * @param bucketAsList
	 * @param element
	 * @param index
	 */
	public void moveElementIntoBucket(List<Integer> bucketAsList,
			Integer element, Integer index) {

		Coordinates buckets_position = new Coordinates(buckets_x
				+ ("B_".length() + 1) * character_width, buckets_y
				+ bucket_height + index * bucket_height);

		this.highlightElement("elements", this.input_elements, element);
		unhighlightCode();
		highlightCode(4);
		lang.nextStep();

		this.showIndexCalculation(element, index);
		unhighlightCode();
		highlightCode(5);
		lang.nextStep();
		unhighlightCode();
		highlightCode(6);
		int[] data = getData(bucketAsList.toArray(new Integer[bucketAsList
				.size()]));

		String key = "buckets_" + index;

		getElementArray(key, data, buckets_position);

		highlightElement(key, data, element);

		lang.nextStep();

		unhighlightElement(key, data, element);
		unhighlightElement("elements", input_elements, element);
		po.hide("index");
		unhighlightCode();
		highlightCode(7);
		lang.nextStep();
	}

	/**
	 * Moves left pointer to the right
	 * 
	 * @param ivorher
	 * @param inachher
	 */
	public void moveLeftPointerRight(int ivorher, Integer inachher) {
		unhighlightCode();
		highlightCode(12);
		highlightCode(13);
		highlightCode(14);
		highlightCode(15);
		setMarker(qs_current_bucket_key, inachher, "L");
		lang.nextStep();
	}

	/**
	 * Moves Right pointer to the left
	 * 
	 * @param jvorher
	 * @param jnachher
	 */
	public void moveRightPointerLeft(int jvorher, int jnachher) {
		unhighlightCode();
		highlightCode(12);
		highlightCode(16);
		highlightCode(17);
		highlightCode(18);
		setMarker(qs_current_bucket_key, jnachher, "R");
		lang.nextStep();
	}

	/**
	 * Pops sequence from "stack"
	 * 
	 * @param sequence
	 * @param links
	 * @param rechts
	 * @param bool
	 */
	public void popSequence(Integer[] sequence, int links, int rechts,
			boolean bool) {
		if (sequences.size() > 0 && (links < rechts || links < sequence.length)) {
			String name = sequences.getLast();
			po.hide(name);
			sequences.pollLast();
			prevSequence.pollLast();
			if (bool)
				lang.nextStep();
		}
	}

	/**
	 * Pushes sequence to "stack"
	 * 
	 * @param bucket
	 * @param color
	 * @param b
	 */
	public void pushSequence(Integer[] bucket, Color color, boolean b) {
		if (sequences == null || prevSequence == null) {
			this.sequences = new LinkedList<String>();
			this.prevSequence = new LinkedList<Coordinates>();
		}

		if (prevSequence.size() == 0) {
			this.prevSequence.add(new Coordinates(sequences_x, sequences_y));
		}
		String name = "seq_" + sequences.size();
		Group array = getElementArray(name, getData(bucket),
				prevSequence.getLast(), color);
		this.sequences.add(name);
		this.prevSequence.add(new Coordinates(prevSequence.getLast().getX()
				+ getWidthOfElementArray(array) + sequence_distance,
				prevSequence.getLast().getY()));
		if (b)
			lang.nextStep();
	}

	/**
	 * Push sequence to "stack"
	 * 
	 * @param sequence
	 * @param links
	 * @param rechts
	 * @param bool
	 */
	public void pushSequence(Integer[] sequence, int links, int rechts,
			boolean bool) {
		this.pushSequence(sequence, links, rechts, false, bool);
	}

	/**
	 * Pushes sequence to "stack"
	 * 
	 * @param sequence
	 * @param links
	 * @param rechts
	 * @param b
	 * @param bool
	 */
	public void pushSequence(Integer[] sequence, int links, int rechts,
			boolean b, boolean bool) {
		Color color;
		if (b)
			color = sequence_highlight_color;
		else
			color = sequence_normal_color;
		if (links < rechts) {
			Integer[] subsequence = new Integer[rechts - links + 1];
			int j = 0;
			for (int i = 0; i < sequence.length; i++) {
				if (i >= links && i <= rechts) {
					subsequence[j] = sequence[i];
					j++;
				}
			}

			this.pushSequence(subsequence, color, bool);
		} else if (links < sequence.length) {
			Integer[] tmp_bucket = new Integer[1];
			int j = 0;
			for (int i = 0; i < sequence.length; i++) {
				if (i == links) {
					tmp_bucket[j] = sequence[i];
					j++;
				}
			}

			this.pushSequence(tmp_bucket, color, bool);
		}
	}

	/**
	 * Sets L and R markers on element array
	 * 
	 * @param bucket
	 * @param links
	 * @param rechts
	 */
	public void setLRMarkers(int links, int rechts) {
		unhighlightCode();
		highlightCode(11);

		setMarker(qs_current_bucket_key, links, "L");
		setMarker(qs_current_bucket_key, rechts, "R");

		lang.nextStep();
	}

	/**
	 * Shows pivot element
	 * 
	 * @param pivot
	 */
	public void showPivotElement(int pivot) {
		unhighlightCode();
		highlightCode(10);
		getText("pivot", text_pivot + pivot, new Coordinates(pivot_x, pivot_y));
		lang.nextStep();
	}

	/**
	 * Shows and Moves all buckets together
	 * 
	 * @param buckets
	 */
	public void showAllBuckets(List<List<Integer>> buckets) {
		po.hide("maximum_element");
		po.hide("nob_info");
		unhighlightCode();

		Coordinates bucket_position = new Coordinates(buckets_x
				+ text_bucketsort.length() * character_width + character_width,
				buckets_y);
		Coordinates end_position = bucket_position;
		LinkedList<Coordinates> positions = new LinkedList<Coordinates>();
		for (int i = 0; i < buckets.size(); i++) {
			positions.add(end_position);
			po.hide("buckets_name_" + i);
			String key = "buckets_" + i;
			end_position = moveBucket(key, end_position);
		}
		lang.nextStep();
		for (int i = 0; buckets.size() > i; i++) {
			bucket_position = new Coordinates(positions.get(i).getX()
					- pseudo_width, buckets_y);
			List<Integer> bucket = buckets.get(i);
			int[] data = getData(bucket.toArray(new Integer[bucket.size()]));
			getElementArray("buckets_" + i, data, bucket_position);
		}
	}
	
	/**
	 * Show all sorted buckets
	 * 
	 * @param sortedBuckets
	 */
	public void showAllSortedBuckets(List<List<Integer>> sortedBuckets) {
		po.hide("buckets_title");
		po.hide("subsequences_text");
		po.hide("quicksort_text");
		lang.nextStep();
	}
	
	/**
	 * Shows concatenated buckets as list
	 * 
	 * @param sortedBuckets
	 */
	public void showConcatenatedBucketsAsList(List<List<Integer>> sortedBuckets) {
		po.hide("sorted_text");
		getText("output_text", text_output, new Coordinates(output_x, output_y));

		Coordinates bucket_position = new Coordinates(output_x
				+ (text_output.length() + 1) * character_width, output_y);

		Coordinates end_position = bucket_position;
		LinkedList<Coordinates> positions = new LinkedList<Coordinates>();
		int i = 0;
		for (i = 0; sortedBuckets.size() > i; i++) {
			positions.add(end_position);
			String key = "sorted_bucket_" + i;
			end_position = moveBucket(key, end_position, false);
		}
		lang.nextStep();

	}

	/**
	 * Shows empty buckets
	 * 
	 * @param numOfBuckets
	 */
	public void showEmptyBuckets(Integer numOfBuckets,boolean info) {
		unhighlightCode();
		highlightCode(3);
		String key = "buckets";
		this.bucketsort_nob = numOfBuckets - 1;
		LinkedList<Primitive> primitives = new LinkedList<Primitive>();

		Coordinates buckets_title_position = new Coordinates(buckets_x,
				buckets_y);

		Text buckets_title = getText(key + "_title", text_bucketsort,
				buckets_title_position);

		primitives.add(buckets_title);

		for (int i = 0; i < numOfBuckets; i++) {
			Coordinates buckets_name_position = new Coordinates(buckets_x,
					buckets_y + bucket_height + i * bucket_height);

			Text bucket_name = getText(key + "_name_" + i, "B" + i,
					buckets_name_position);

			primitives.add(bucket_name);
		}
		if (info)
			getTextBlock("nob_info",new String[]{"Die Eimeranzahl wurde verringert,","sodass keine leeren Eimer entstehen."},new Coordinates(buckets_x+bucket_height,buckets_y+(numOfBuckets+1)*bucket_height));
		getText("nob_text", text_nob + numOfBuckets, new Coordinates(nob_x,
				nob_y));
		lang.nextStep();

	}
	
	/**
	 * Shows end of quicksort for a bucket
	 *  
	 * @param sortedBuckets
	 * @param bucket
	 * @param j
	 */
	public void showEndOfQuicksort(List<List<Integer>> sortedBuckets,
			Integer[] bucket, int j) {
		this.showPivotAndLRPointers();

		String name = qs_current_bucket_key;
		if (j == 0) {
			getText("sorted_text", text_sorted_buckets, new Coordinates(
					sorted_x, sorted_y));
		}

		Coordinates bucket_position;
		if (position_last_sorted_bucket == null)
			bucket_position = new Coordinates(sorted_x
					+ (text_sorted_buckets.length() + 1) * character_width,
					sorted_y);
		else
			bucket_position = new Coordinates(
					position_last_sorted_bucket.getX(), sorted_y);
		Coordinates end_position = bucket_position;

		this.position_last_sorted_bucket = moveBucket(name, end_position);
		lang.nextStep();
		name = "sorted_bucket_" + j;
		po.hide(qs_current_bucket_key);
		getElementArray(name, getData(bucket),
				new Coordinates(bucket_position.getX() - pseudo_width,
						bucket_position.getY()));

	}

	/**
	 * Show index calculation
	 * @param element
	 * @param index
	 */
	public void showIndexCalculation(Integer element, Integer index) {
		po.hide("index");
		unhighlightCode();
		highlightCode(5);
		getText("index", "i = " + element + " / " + input_max_element + " * "
				+ bucketsort_nob + " = " + index, new Coordinates(index_x,
				index_y));
	}

	/**
	 * Shows Intro
	 */
	public void showIntro() {
		getText("title_text", text_title, new Coordinates(title_x, title_y),
				titleProps);

		getTextBlock("description", text_description, new Coordinates(
				nob_x, nob_y));
		
		showCode(new Coordinates(820,nob_y));

		lang.nextStep();
		po.hide("description");
		po.hide("codebox");
		po.hide("code");
	}

	/**
	 * Shows Outro
	 */
	public void showOutro() {
		lang.nextStep();
		po.hide("codebox");
		po.hide("code");
		getTextBlock("description",text_final, new Coordinates(sequences_title_x,sequences_title_y));
	}

	/**
	 * Moves Pointer Left to Right
	 */
	public void showPointerMovementLeftToRight() {
		unhighlightCode();
		highlightCode(12);
		highlightCode(13);
		highlightCode(14);
		highlightCode(15);
		lang.nextStep();
	}

	/**
	 * Moves Pointer Right to Left
	 */
	public void showPointerMovementRightToLeft() {
		unhighlightCode();
		highlightCode(12);
		highlightCode(16);
		highlightCode(17);
		highlightCode(18);
		lang.nextStep();
	}

	/**
	 * Shows quicksort text infos
	 */
	public void showQuicksortText() {
		Coordinates position_text = new Coordinates(quicksort_x, quicksort_y);
		getText("quicksort_text", text_quicksort, position_text);
		getText("subsequences_text", text_sequences, new Coordinates(
				sequences_title_x, sequences_title_y));
		lang.nextStep();
	}

	/**
	 * Shows start of sorting
	 * 
	 * @param elements
	 * @param numOfBuckets
	 */
	public void showStart(Integer[] elements, Integer numOfBuckets) {

		this.input_elements = getData(elements);
		this.bucketsort_nob = numOfBuckets;

		int[] elems = getData(elements);

		getText("nob_text", text_nob + numOfBuckets, new Coordinates(nob_x,
				nob_y));

		getText("input_text", text_input, new Coordinates(elements_x,
				elements_y));

		Coordinates position = new Coordinates(elements_x + text_input.length()
				* character_width, elements_y);

		Group array = getElementArray("elements", elems, position);
		lang.nextStep();
		
		showCode(getWidthOfElementArray(array), numOfBuckets);

		lang.nextStep();
	}

	/**
	 * Starts with quicksort animation
	 * 
	 * @param bucket
	 * @param i
	 */
	public void showStartOfQuicksort(Integer[] bucket, int i) {

		int[] data = getData(bucket);

		String key = "buckets_" + i;
		Coordinates bucket_position = new Coordinates(quicksort_x
				+ (text_quicksort.length() + 1) * character_width, quicksort_y);
		Coordinates end_position = moveBucket(key, bucket_position);

		for (int j = 0; j < data.length; j++) {
			unhighlight("buckets_" + i + "_element_" + j + "_rect");
		}

		qs_current_bucket_key = key;

		lang.nextStep();

		bucket_position = new Coordinates(
				bucket_position.getX() - pseudo_width, end_position.getY());
		getElementArray("buckets_" + i, data, bucket_position);

	}

	/**
	 * Shows swap of elements
	 * 
	 * @param bucket
	 * @param i
	 * @param j
	 */
	public void showSwapOfElements(Integer[] bucket, Integer i, Integer j) {
		String key = qs_current_bucket_key;
		Group array = (Group) po.get(key);
		Group element_i = (Group) array.getPrimitives().get(i);
		Rect element_i_rect = (Rect) element_i.getPrimitives().getFirst();
		Group element_j = (Group) array.getPrimitives().get(j);
		Rect element_j_rect = (Rect) element_j.getPrimitives().getFirst();

		Coordinates upperLeft_i = (Coordinates) element_i_rect.getUpperLeft();
		Coordinates upperLeft_j = (Coordinates) element_j_rect.getUpperLeft();
		Coordinates lowerRight = (Coordinates) element_j_rect.getLowerRight();

		int middle = upperLeft_i.getX()
				+ (lowerRight.getX() - upperLeft_i.getX()) / 2;

		Coordinates top = new Coordinates(middle, upperLeft_i.getY() - 15);
		Coordinates down = new Coordinates(middle, lowerRight.getY() + 15);
		Polyline pUp;
		Polyline pDown;
		if (upperLeft_i.getX() <= upperLeft_j.getX()) {
			Coordinates[] nodesUp = { upperLeft_i, top, upperLeft_j };
			Coordinates[] nodesDown = { upperLeft_j, down, upperLeft_i };
			pUp = getPolyline("p_up", nodesUp);
			pDown = getPolyline("p_down", nodesDown);
		} else {

			Coordinates[] nodesUp = { upperLeft_j, top, upperLeft_i };
			Coordinates[] nodesDown = { upperLeft_i, down, upperLeft_j };
			pUp = getPolyline("p_up", nodesUp);
			pDown = getPolyline("p_down", nodesDown);
		}

		try {
			element_i.moveVia(null, null, pUp, new MsTiming(10), new MsTiming(
					1000));
			element_j.moveVia(null, null, pDown, new MsTiming(10),
					new MsTiming(1000));
		} catch (IllegalDirectionException e) {
			e.printStackTrace();
		}
		lang.nextStep();
		Coordinates position = (Coordinates) ((Rect) ((Group) array
				.getPrimitives().getFirst()).getPrimitives().getFirst())
				.getUpperLeft();
		array = getElementArray(
				qs_current_bucket_key,
				getData(bucket),
				new Coordinates(position.getX() - pseudo_width, position.getY()));

	}

	/**
	 * Hides LR Pointers
	 */
	public void hideLRPointers() {

		po.hide("L");
		po.hide("R");
	}

	/**
	 * Hides pivot element
	 */
	public void hidePivotElement() {

		po.hide("pivot");
	}

	/**
	 * Unsets pivot element and lr pointers
	 */
	public void showPivotAndLRPointers() {
		this.hidePivotElement();
		this.hideLRPointers();
	}

	public void nextStep() {
		// TODO Auto-generated method stub
		lang.nextStep();
	}

}
