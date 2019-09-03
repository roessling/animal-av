
package generators.graph;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import algoanim.animalscript.AnimalGraphGenerator;
import algoanim.animalscript.AnimalGroupGenerator;
import algoanim.animalscript.AnimalPolylineGenerator;
import algoanim.animalscript.AnimalRectGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalSourceCodeGenerator;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.primitives.Graph;
import algoanim.primitives.Group;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.GraphGenerator;
import algoanim.primitives.generators.GroupGenerator;
import algoanim.primitives.generators.PolylineGenerator;
import algoanim.primitives.generators.RectGenerator;
import algoanim.primitives.generators.SourceCodeGenerator;
import algoanim.primitives.generators.TextGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.graph.state.EdgeState;
import generators.graph.state.NodeState;
import generators.graph.state.State;
import generators.graph.utils.PREdge;
import generators.graph.utils.PRGraph;
import generators.graph.utils.PRNode;

/**
 * 
 * @author Steffen Frank Schmidt
 *
 */
public class PushRelabelGenerator implements ValidatingGenerator {
	private AnimalScript lang;
	private String input;

	private PRGraph graph;

	private PrimitiveOrganizer po;

	private PolylineGenerator polylineGenerator;
	private SourceCodeGenerator sourceCodeGenerator;
	private GroupGenerator groupGenerator;
	private RectGenerator rectGenerator;
	private TextGenerator textGenerator;
	private GraphGenerator graphGenerator;

	private DisplayOptions displayOptions;

	private GraphProperties graphProps;
	private PolylineProperties adjMatrixPolylineProps;
	private RectProperties actNodes_props;
	private RectProperties highlight_props;
	private RectProperties sourceCodeBackgroundProps;
	private SourceCodeProperties sourceCodeProps;
	private TextProperties textProps;

	private String[] code;
	private String[] valMatrix_names;
	private int character_width = 7;
	private int character_height = 14;
	private int pseudoCode_x;
	private int pseudoCode_y;
	private int length_max_adj_value;
	private int delta_x_adj_matrix;
	private int delta_y_adj_matrix;
	private int delta_x_val_matrix;
	private int delta_y_val_matrix;
	private int graph_x;
	private int graph_y;
	private Coordinates valMatrix_position;
	private Coordinates adjMatrix_position;

	private State prevState;

	private Color adjMatrix_highlight_color = Color.ORANGE;
	private Color actNode_highlight_color = Color.GREEN;
	private Color sourceCode_highlight_color = Color.RED;
	private Color sourceCode_background_border_color = Color.BLACK;
	private Color sourceCode_background_color = Color.LIGHT_GRAY;
	private Color sourceCode_text_color = Color.BLACK;
	private Color graph_fill_color = Color.WHITE;
	private Color graph_node_highlight_color = Color.GREEN;
	private Color adjMatrix_line_color = Color.BLACK;

	private EdgeState minEdge;

	private String[] introtext;

	private Coordinates maxFlow_position;

	private String[] outrotext;

	/**
	 * initialize
	 */
	public void init() {
		lang = new AnimalScript("Push-Relabel Algorithmus [DE]",
				"Steffen Frank Schmidt", 800, 600);
		lang.setStepMode(true);
		this.po = new PrimitiveOrganizer();
		definePositions();
		defineProperties();
		defineGenerators();
		defineTexts();
	}

	/**
	 * start generation
	 */
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		if (this.validateInput(props, primitives)) {
			ArrayList<State> states = this.graph.runPushRelabel();
			this.generateAnimationCode(states);
			lang.finalizeGeneration();
		}
		return lang.toString();
	}

	/**
	 * returns name
	 */
	public String getName() {
		return "Push-Relabel Algorithmus [DE]";
	}

	/**
	 * returns algorithm name
	 */
	public String getAlgorithmName() {
		return "Push-Relabel-Algorithmus";
	}

	/**
	 * returns author
	 */
	public String getAnimationAuthor() {
		return "Steffen Frank Schmidt";
	}

	/**
	 * returns description
//	 */
//	public String getDescription() {
//		return "Der Push-Relabel Algorithmus berechnet in einem Flussnetzwerk"
//				+ "\n"
//				+ "den maximalen Fluss, der vom Startknoten zum Endknoten fliessen kann"
//				+ " \n"
//				+ "Der Algorithmus besteht aus Operationen die auf dem Fluss ausgeführt"
//				+ "\n"
//				+ "werden, nämlich der Push und der Relabel Operation"
//				+ "\n"
//				+ "Beim Pushen wird Fluss von einem Knoten in einen benachbarten Knoten"
//				+ "\n"
//				+ "unter der Führung eines Netzwerkes, dass durch Relabel Operationen"
//				+ "\n"
//				+ "aufgestellt wurde."
//				;
//	}
//	
	
	
	public String getDescription() {
	return "Die folgende Animation zeigt, wie der Push-Relabel Algorithms funktioniert."
	+ "\n"
	+ "In einem Flussnetzwerk wird mit diesem Algorithmus der maximale Fluss gesucht."
	+ "\n"
	+ "Hierbei wird der Fluss der einzelnen Kanten ausgehend von der Quelle schrittweise lokal erhöht."
	+ "\n"
	+ "Jeder Knoten wird als 'Reservoir' angesehen, das temporär unendlich viel Fluss"
	+ "\n"
	+ "aufnehmen kann, bis er in einem nächsten Schritt zu einem anderen Knoten geleitet"
	+ "\n"
	+ "wird, möglicherweise auch zurück Richtung Quelle. Ist bei einem Knoten der Zufluss"
	+ "\n"
	+ "ungleich dem Abfluss, so ist dieser aktiv. Es gibt zwei wesentliche Operationen auf den Knoten:"
	+ "\n"
	+ "Push: Fluss zu einem neuen Knoten ableiten, welcher genau um eine Höheneinheit niedriger sein muss."
	+ "\n"
	+ "Relabel: Das Niveau des aktuellen Knotens erhöhen, um dann evtl. push anwenden zu können."
	+ "\n"
	+ "Verwalung der Knoten mit Überfluss in einer Warteschlange."
	+ "\n"
	+ "Grundidee: Solange bei einem Knoten angestauter Fluss existiert, gibt es eine Push- oder"
	+ "\n"
	+ "Relabel-Möglichkeit. Die genaue Abfolge von Push und Relabel ist nicht näher spezifiziert." ;
				}

	
	 
	/**
	 * returns code example
	 */
	public String getCodeExample() {
		return 	" 1.    Initialisiere Graph G mit leerem Fluss f und setze Höhenfunktion h für alle Knoten auf 0"
				+ "\n"
				+ " 3.    Setze h(s) = |V|, h(t) = 0"
				+ "\n"
				+ " 4.    Initialisiere leere Warteschlange w"
				+ "\n"
				+ " 5.    Leite Fluss von s ab, unabhängig von h"
				+ "\n"
				+ " 6.    Solange w nicht leer ist"
				+ "\n"
				+ " 7.        Nimm den vordersten Knoten k aus w"
				+ "\n"
				+ " 8.        Solange für k Zufluss ungleich Abfluss"
				+ "\n"
				+ " 9.             Finde mögliche Kanten"
				+ "\n"
				+ " 10.           Selektiere minimale Kante"
				+ "\n"
				+ " 11.           Wenn es Kante (k,u) gibt mit h(k) = h(u) + 1"
				+ "\n"
				+ " 12.               Leite überzähligen Fluss über (k,u) an u ab"
				+ "\n"
				+ " 13.               Erhöhe f um den Flusswert von (k,u) ansonsten"
				+ "\n"
				+ " 15.               Setze u an das Ende von w falls nicht schon enthalten"
				+ "\n"
				+ " 16.               Steigere die Höhe von k, bis es Kante (k,u) mit h(k) = h(u) + 1 gibt"
				+ "\n"
				+ " 17.	Gib f zurück" ;
	}

	/**
	 * returns file extension
	 */
	public String getFileExtension() {
		return "asu";
	}

	/**
	 * returns locale
	 */
	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	/**
	 * returns generator type
	 */
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
	}

	/**
	 * returns output language
	 */
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	/**
	 * validates input
	 */
	@Override
	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		Graph graph = (Graph) primitives.get("graph");

		String startnode = graph.getNodeLabel(graph.getStartNode());
		String endnode = graph.getNodeLabel(graph.getTargetNode());

		LinkedList<PRNode> nodes = new LinkedList<PRNode>();
		HashMap<String, PRNode> nodes_map = new HashMap<String, PRNode>();
		int size = graph.getAdjacencyMatrix().length;
		Coordinates[] coordinates = new Coordinates[size];
		String[] labels = new String[size];
		for (int i = 0; i < size; i++) {
			coordinates[i] = (Coordinates) graph.getNode(i);
			labels[i] = graph.getNodeLabel(i);
			String name = labels[i];
			System.out.println(coordinates[i].getX() + ","
					+ coordinates[i].getY());
			System.out.println(labels[i].toString());
			if (name.toLowerCase().equals(startnode.toLowerCase())) {
				PRNode node = new PRNode(name, "start");
				nodes_map.put(name, node);
				nodes.add(node);
			} else if (name.toLowerCase().equals(endnode.toLowerCase())) {
				PRNode node = new PRNode(name, "end");
				nodes_map.put(name, node);
				nodes.add(node);
			} else {
				PRNode node = new PRNode(name);
				nodes_map.put(name, node);
				nodes.add(node);
			}
		}

		List<PREdge> edges = new ArrayList<PREdge>();
		int[][] matrix = graph.getAdjacencyMatrix();
		for (int zeile = 0; zeile < matrix.length; zeile++) {
			for (int spalte = 0; spalte < matrix[0].length; spalte++) {
				if (matrix[zeile][spalte] != 0) {
					System.out.print(matrix[zeile][spalte] + "\t");
					PRNode start = nodes.get(zeile);
					PRNode end = nodes.get(spalte);
					PREdge edge = new PREdge(start, end, matrix[zeile][spalte]);
					edges.add(edge);
				}
			}
			System.out.println();
		}

		this.graph = new PRGraph(nodes, edges);
		System.out.println(this.graph.toString());
		validateCoordinates(coordinates);
		this.graph.setCoordinates(coordinates);
		return true;
	}

	/**
	 * validate if coordinates are between x and y
	 * 
	 * @param coordinates
	 * @return
	 */
	private boolean validateCoordinates(Coordinates[] coordinates) {
		for (int i = 0; i < coordinates.length; i++) {
			Coordinates coordinate = coordinates[i];
			if (!(coordinate.getX() >= 0 && coordinate.getX() <= 400)) {
				throw new IllegalArgumentException(
						"Coordinates must be between (0,400) for x.");
			}
			if (!(coordinate.getY() >= 100 && coordinate.getY() <= 800)) {
				throw new IllegalArgumentException(
						"Coordinates must be between (100,800) for y.");
			}
		}

		return true;
	}

	/**
	 * define necessary values
	 */
	private void definePositions() {
		graph_x = 100;
		graph_y = 300;
		length_max_adj_value = 100;
		delta_x_adj_matrix = 30;
		delta_y_adj_matrix = 30;
		delta_x_val_matrix = 30;
		delta_y_val_matrix = 30;
		adjMatrix_position = new Coordinates(950, 50);
		valMatrix_position = new Coordinates(950, 310);
		maxFlow_position = new Coordinates(50, 120);
	}

	/**
	 * define intro, outro and code text
	 */
	private void defineTexts() {
		this.code = new String[] { "",
				" 1.    Initialisiere Graph G mit leerem Fluss f",
				"		und setze Höhenfunktion h für alle Knoten auf 0",
				" 3.    Setze h(s) = |V|, h(t) = 0",
				" 4.    Initialisiere leere Warteschlange w",
				" 5.    Leite Fluss von s ab, unabhängig von h",
				" 6.    Solange w nicht leer ist",
				" 7.        Nimm den vordersten Knoten k aus w",
				" 8.        Solange für k Zufluss ungleich", "			Abfluss",
				" 9.             Finde mögliche Kanten",
				" 10.           Selektiere minimale Kante",
				" 11.           Wenn es Kante (k,u) gibt mit h(k) = h(u) + 1",
				" 12.               Leite überzähligen Fluss über",
				"					(k,u) an u ab",
				" 13.               Erhöhe f um den Flusswert von (k,u)", "",
				" 15.               Setze u an das Ende von w",
				"					falls nicht schon enthalten", " 14.           ansonsten",
				" 16.               Steigere die Höhe von k, bis",
				"					es Kante (k,u) mit h(k) = h(u) + 1 gibt",
				" 17.	Gib f zurück" };

		this.introtext = new String[] {
				"Die folgende Animation zeigt, wie der Push-Relabel Algorithms funktioniert.",
				"In einem Flussnetzwerk wird mit diesem Algorithmus der maximale Fluss gesucht.",
				"Hierbei wird der Fluss der einzelnen Kanten ausgehend von der Quelle schrittweise lokal erhöht.",
				"Jeder Knoten wird als 'Reservoir' angesehen, das temporär unendlich viel Fluss",
				"aufnehmen kann, bis er in einem nächsten Schritt zu einem anderen Knoten geleitet",
				"wird, möglicherweise auch zurück Richtung Quelle. Ist bei einem Knoten der Zufluss",
				"ungleich dem Abfluss, so ist dieser aktiv. Es gibt zwei wesentliche Operationen auf den Knoten:",
				"Push: Fluss zu einem neuen Knoten ableiten, welcher genau um eine Höheneinheit niedriger sein muss.",
				"Relabel: Das Niveau des aktuellen Knotens erhöhen, um dann evtl. push anwenden zu können.",
				"Verwalung der Knoten mit Überfluss in einer Warteschlange.",
				"Grundidee: Solange bei einem Knoten angestauter Fluss existiert, gibt es eine Push- oder",
				"Relabel-Möglichkeit. Die genaue Abfolge von Push und Relabel ist nicht näher spezifiziert." };

		this.outrotext = new String[] {
				"Nach Abschluss des Algorithmus ist der maximale Fluss die Summe des Flusses aller",
				"ausgehenden Kanten von s bzw. der eingehenden Kanten von t." };

		valMatrix_names = new String[2];
		valMatrix_names[0] = "Überzähliger Fluss";
		valMatrix_names[1] = "Höhe";
	}

	/**
	 * define the generators
	 */
	private void defineGenerators() {
		this.graphGenerator = new AnimalGraphGenerator(lang);
		this.polylineGenerator = new AnimalPolylineGenerator(lang);
		this.groupGenerator = new AnimalGroupGenerator(lang);
		this.textGenerator = new AnimalTextGenerator(lang);
		this.rectGenerator = new AnimalRectGenerator(lang);
		this.sourceCodeGenerator = new AnimalSourceCodeGenerator(lang);
	}

	/**
	 * define the properties
	 */
	private void defineProperties() {
		// default display options
		this.displayOptions = null;

		// graph properties
		this.graphProps = new GraphProperties();
		this.graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
				graph_fill_color);
		this.graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
		this.graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
		this.graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				graph_node_highlight_color);

		// adjacency matrix polyline properties
		this.adjMatrixPolylineProps = new PolylineProperties();
		this.adjMatrixPolylineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				adjMatrix_line_color);

		// general text properties
		this.textProps = new TextProperties();
		this.textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.BOLD, 14));

		// source code properties
		this.sourceCodeProps = new SourceCodeProperties();
		this.sourceCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
				Color.BLUE);
		this.sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font("SansSerif", Font.PLAIN, 12));
		this.sourceCodeProps.set(AnimationPropertiesKeys.INDENTATION_PROPERTY,
				30);
		this.sourceCodeProps.set(
				AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				sourceCode_highlight_color);
		this.sourceCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				sourceCode_text_color);

		// active nodes properties
		this.actNodes_props = new RectProperties();
		this.actNodes_props.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				sourceCode_background_border_color);
		this.actNodes_props.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		this.actNodes_props.set(AnimationPropertiesKeys.FILL_PROPERTY,
				actNode_highlight_color);

		// table highlight properties
		this.highlight_props = new RectProperties();
		this.highlight_props.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				sourceCode_background_border_color);
		this.highlight_props.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		this.highlight_props.set(AnimationPropertiesKeys.FILL_PROPERTY,
				adjMatrix_highlight_color);

		// source code background properties
		this.sourceCodeBackgroundProps = new RectProperties();
		this.sourceCodeBackgroundProps.set(
				AnimationPropertiesKeys.COLOR_PROPERTY,
				sourceCode_background_border_color);
		this.sourceCodeBackgroundProps.set(
				AnimationPropertiesKeys.FILLED_PROPERTY, true);
		this.sourceCodeBackgroundProps.set(
				AnimationPropertiesKeys.FILL_PROPERTY,
				sourceCode_background_color);
	}

	/**
	 * returns value matrix
	 * 
	 * @param key
	 * @param nodes
	 * @param edges
	 * @return
	 */
	private Group getValueMatrix(String key, List<NodeState> nodes,
			List<EdgeState> edges) {
		po.hide(key);

		Coordinates upperLeftCorner = valMatrix_position;

		int x = upperLeftCorner.getX();
		int y = upperLeftCorner.getY();

		int x_upper_left = upperLeftCorner.getX();
		int y_upper_left = upperLeftCorner.getY();

		int x_lower_right = x_upper_left + 3 * delta_x_val_matrix
				+ (valMatrix_names[0].length() + valMatrix_names[1].length())
				* character_width;

		int y_lower_right = y_upper_left + (nodes.size() + 1)
				* delta_y_val_matrix;

		LinkedList<Primitive> primitives = new LinkedList<Primitive>();

		int i = 0;
		Node[] p_nodes = new Node[2];
		p_nodes[0] = new Coordinates(x_upper_left + i * delta_x_val_matrix,
				y_upper_left);
		p_nodes[1] = new Coordinates(x_upper_left + i * delta_x_val_matrix,
				y_lower_right);
		Polyline p_vertical = getPolyline("valMatrix_vertical_line_" + i,
				p_nodes);

		p_nodes = new Node[2];
		p_nodes[0] = new Coordinates(x_upper_left, y_upper_left + i
				* delta_y_val_matrix);
		p_nodes[1] = new Coordinates(x_lower_right, y_upper_left + i
				* delta_y_val_matrix);
		Polyline p_horizontal = getPolyline("valMatrix_horizontal_line_" + i,
				p_nodes);

		primitives.add(p_vertical);
		primitives.add(p_horizontal);

		for (i = 1; i <= nodes.size(); i++) {
			NodeState node = nodes.get(i - 1);
			int current_start_x = x + character_width;
			int current_start_y = y + i * delta_y_val_matrix
					+ delta_y_val_matrix / 2 - character_height / 2;

			Coordinates position_start = new Coordinates(current_start_x,
					current_start_y);
			Text start_text = getText("valMatrix_start_" + i, node.getName(),
					position_start);

			primitives.add(start_text);

			p_nodes = new Node[2];
			p_nodes[0] = new Coordinates(x_upper_left, y_upper_left + i
					* delta_y_val_matrix);
			p_nodes[1] = new Coordinates(x_lower_right, y_upper_left + i
					* delta_y_val_matrix);
			p_horizontal = getPolyline("valMatrix_horizontal_line_" + i,
					p_nodes);

			primitives.add(p_horizontal);

		}

		for (i = 1; i <= 2; i++) {
			int current_end_x = x + i * delta_x_val_matrix + (i - 1)
					* valMatrix_names[0].length() * character_width
					+ character_width;
			int current_end_y = y;

			Coordinates position_end = new Coordinates(current_end_x,
					current_end_y);
			Text end_text = getText("valMatrix_end_" + i,
					valMatrix_names[i - 1], position_end);

			primitives.add(end_text);

			p_nodes = new Node[2];
			p_nodes[0] = new Coordinates(x_upper_left + i * delta_x_val_matrix
					+ (i - 1) * valMatrix_names[0].length() * character_width,
					y_upper_left);
			p_nodes[1] = new Coordinates(x_upper_left + i * delta_x_val_matrix
					+ (i - 1) * valMatrix_names[0].length() * character_width,
					y_lower_right);
			p_vertical = getPolyline("valMatrix_vertical_line_" + i, p_nodes);

			primitives.add(p_vertical);
		}

		i = 3;
		p_nodes = new Node[2];
		p_nodes[0] = new Coordinates(x_lower_right, y_upper_left);
		p_nodes[1] = new Coordinates(x_lower_right, y_lower_right);
		p_vertical = getPolyline("vertical_line_" + i, p_nodes);

		i = nodes.size() + 1;
		p_nodes = new Node[2];
		p_nodes[0] = new Coordinates(x_upper_left, y_lower_right);
		p_nodes[1] = new Coordinates(x_lower_right, y_lower_right);
		p_horizontal = getPolyline("horizontal_line_" + i, p_nodes);

		primitives.add(p_vertical);
		primitives.add(p_horizontal);

		return getGroup(key, primitives);
	}
	
	/**
	 * Returns adjacency matrix
	 * 
	 * @param key
	 * @param nodes
	 * @param edges
	 * @return
	 */
	private Group getAdjacencyMatrix(String key, List<NodeState> nodes,
			List<EdgeState> edges) {
		po.hide(key);

		Coordinates upperLeftCorner = adjMatrix_position;

		int x = upperLeftCorner.getX();
		int y = upperLeftCorner.getY();

		int x_upper_left = upperLeftCorner.getX();
		int y_upper_left = upperLeftCorner.getY();

		int x_lower_right = x_upper_left + (nodes.size() + 1)
				* delta_x_adj_matrix;
		int y_lower_right = y_upper_left + (nodes.size() + 1)
				* delta_y_adj_matrix;

		LinkedList<Primitive> primitives = new LinkedList<Primitive>();

		int i = 0;
		Node[] p_nodes = new Node[2];
		p_nodes[0] = new Coordinates(x_upper_left + i * delta_x_adj_matrix,
				y_upper_left);
		p_nodes[1] = new Coordinates(x_upper_left + i * delta_x_adj_matrix,
				y_lower_right);
		Polyline p_vertical = getPolyline("adjMatrix_vertical_line_" + i,
				p_nodes);

		primitives.add(p_vertical);

		p_nodes = new Node[2];
		p_nodes[0] = new Coordinates(x_upper_left, y_upper_left + i
				* delta_y_adj_matrix);
		p_nodes[1] = new Coordinates(x_lower_right, y_upper_left + i
				* delta_y_adj_matrix);
		Polyline p_horizontal = getPolyline("adjMatrix_horizontal_line_" + i,
				p_nodes);

		primitives.add(p_horizontal);

		for (i = 1; i <= nodes.size(); i++) {
			NodeState node = nodes.get(i - 1);
			int current_end_x = x + i * delta_x_adj_matrix + character_width;
			int current_end_y = y;
			int current_start_x = x + character_width;
			int current_start_y = y + i * delta_y_adj_matrix
					+ delta_y_adj_matrix / 2 - character_height / 2;

			Coordinates position_start = new Coordinates(current_start_x,
					current_start_y);
			Text start_text = getText("adjMatrix_start_" + i, node.getName(),
					position_start);

			primitives.add(start_text);

			Coordinates position_end = new Coordinates(current_end_x,
					current_end_y);
			Text end_text = getText("end_" + i, node.getName(), position_end);

			primitives.add(end_text);

			p_nodes = new Node[2];
			p_nodes[0] = new Coordinates(x_upper_left + i * delta_x_adj_matrix,
					y_upper_left);
			p_nodes[1] = new Coordinates(x_upper_left + i * delta_x_adj_matrix,
					y_lower_right);
			p_vertical = getPolyline("adjMatrix_vertical_line_" + i, p_nodes);

			p_nodes = new Node[2];
			p_nodes[0] = new Coordinates(x_upper_left, y_upper_left + i
					* delta_y_adj_matrix);
			p_nodes[1] = new Coordinates(x_lower_right, y_upper_left + i
					* delta_y_adj_matrix);
			p_horizontal = getPolyline("adjMatrix_horizontal_line_" + i,
					p_nodes);

			primitives.add(p_vertical);
			primitives.add(p_horizontal);

		}

		i = nodes.size() + 1;

		p_nodes = new Node[2];
		p_nodes[0] = new Coordinates(x_upper_left + i * delta_x_adj_matrix,
				y_upper_left);
		p_nodes[1] = new Coordinates(x_upper_left + i * delta_x_adj_matrix,
				y_lower_right);
		p_vertical = getPolyline("adjMatrix_vertical_line_" + i, p_nodes);

		p_nodes = new Node[2];

		p_nodes[0] = new Coordinates(x_upper_left, y_upper_left + i
				* delta_y_adj_matrix);
		p_nodes[1] = new Coordinates(x_lower_right, y_upper_left + i
				* delta_y_adj_matrix);
		p_horizontal = getPolyline("adjMatrix_horizontal_line_" + i, p_nodes);

		primitives.add(p_vertical);
		primitives.add(p_horizontal);

		return getGroup(key, primitives);
	}

	/**
	 * returns a polyline
	 * 
	 * @param key
	 * @param nodes
	 * @return
	 */
	private Polyline getPolyline(String key, Node[] nodes) {
		String uuid = po.getUUID();
		Polyline p = new Polyline(polylineGenerator, nodes, uuid,
				displayOptions, adjMatrixPolylineProps);
		po.set(p, key, uuid);
		return p;
	}

	/**
	 * returns a text
	 * 
	 * @param name
	 * @param content
	 * @param position
	 * @param textProps
	 * @return
	 */
	private Text getText(String name, String content, Coordinates position,
			TextProperties textProps) {
		po.hide(name);

		String uuid = po.getUUID();

		Text text = new Text(textGenerator, position, content, uuid,
				displayOptions, textProps);

		po.set(text, name, uuid);

		return text;
	}

	/**
	 * returns a text
	 * 
	 * @param name
	 * @param content
	 * @param position
	 * @return
	 */
	private Text getText(String name, String content, Coordinates position) {
		return getText(name, content, position, textProps);
	}

	/**
	 * rendering all states
	 * 
	 * @param state
	 */
	private void render(State state) {

		System.out.println(state.getCaption());
		switch (state.getCaption()) {
		case "START":
			showGraph(state);
			updatePositions(state);
			showCode();
			highlightCode(2);
			highlightCode(3);
			showAdjacencyMatrix(state);
			showValueMatrix(state);
			showMaxFlow(state);
			lang.nextStep();
			unhighlightCode();
			highlightCode(4);
			highlightCode(5);
			lang.nextStep();
			break;
		case "INITIALIZE HEIGHT OF START":
			highlightValueMatrix(state);
			showValueMatrix(state);
			lang.nextStep();
			unhighlightCode();
			unhighlightValueMatrix(state);
			break;
		case "INITIALIZE FLOW OF START":
			highlightAdjMatrix(state);
			showAdjacencyMatrix(state);
			highlightValueMatrix(state);
			showValueMatrix(state);
			highlightCode(6);
			lang.nextStep();
			unhighlightCode();
			break;
		case "GET ACTIVE NODES":
			highlightActiveNodes(state);
			showValueMatrix(state);
			highlightCode(7);
			lang.nextStep();
			unhighlightAdjMatrix(state);
			unhighlightValueMatrix(state);
			break;
		case "CHECK FOR ACTIVE NODES":
			unhighlightCode();
			highlightCode(7);
			unhighlightActiveNode(state);
			lang.nextStep();
			break;
		case "GET ACTIVE NODE":
			highlightCode(8);
			highlightActiveNode(state);
			lang.nextStep();
			unhighlightCode();
			highlightCode(7);
			break;
		case "CHECK ACTIVE NODE":
			checkActiveNode(state);
			unhighlightCode();
			highlightCode(7);
			highlightCode(9);
			highlightCode(10);
			lang.nextStep();
			unhighlightCode();
			highlightCode(7);
			highlightCode(9);
			highlightCode(10);
			break;
		case "GET POSSIBLE EDGES":
			highlightPossibleEdges(state);
			highlightCode(11);
			lang.nextStep();
			unhighlightCode();
			highlightCode(7);
			highlightCode(9);
			highlightCode(10);
			unhighlightPossibleEdges(state);
			break;
		case "GET MIN EDGE":
			highlightMinEdge(state);
			highlightCode(12);
			lang.nextStep();
			unhighlightCode();
			highlightCode(7);
			highlightCode(9);
			highlightCode(10);
			break;
		case "CHECK FOR PUSH OR RELABEL":
			highlightCode(13);
			highlightCode(20);
			lang.nextStep();
			unhighlightMinEdge(state);
			break;
		case "AFTER RELABEL":
			highlightCode(21);
			highlightCode(22);
			highlightValueMatrix(state);
			showValueMatrix(state);
			lang.nextStep();
			unhighlightValueMatrix(state);
			break;
		case "AFTER PUSH":
			unhighlightCode();
			highlightCode(7);
			highlightCode(9);
			highlightCode(10);
			highlightCode(14);
			highlightCode(15);
			highlightValueMatrix(state);
			showValueMatrix(state);
			lang.nextStep();
			unhighlightValueMatrix(state);
			unhighlightCode();
			highlightCode(7);
			highlightCode(9);
			highlightCode(10);
			highlightCode(16);
			highlightAdjMatrix(state);
			showAdjacencyMatrix(state);
			showMaxFlow(state);
			lang.nextStep();
			unhighlightAdjMatrix(state);
			unhighlightCode();
			highlightCode(7);
			highlightCode(9);
			highlightCode(10);
			highlightCode(18);
			highlightCode(19);
			lang.nextStep();
			break;
		case "UPDATE ACTIVE NODES":
			highlightActiveNodes(state);
			highlightValueMatrix(state);
			showValueMatrix(state);
			lang.nextStep();
			unhighlightValueMatrix(state);
			break;
		case "END":
			unhighlightCode();
			highlightCode(23);
			lang.nextStep();
			unhighlightCode();
			break;
		default:
			System.err.println(state.getCaption());
			break;
		}
		showMaxFlow(state);
		prevState = state;
	}

	/**
	 * show title
	 */
	private void showTitle() {
		getText("caption", "Der Push-Relabel Algorithmus", new Coordinates(50,
				50));
	}

	/**
	 * show the maximal flow
	 * 
	 * @param state
	 */
	private void showMaxFlow(State state) {
		if (state.getFlow() != null) {
			getText("maxFlow", "Maximaler Fluss f = "
					+ state.getFlow().toString(), maxFlow_position);
			System.out.println(state.getFlow());
		}
	}

	/**
	 * unhighlight the minimal edge
	 * 
	 * @param state
	 */
	private void unhighlightMinEdge(State state) {
		Graph graph = (Graph) po.get("graph");
		List<NodeState> nodes = state.getNodes();
		graph.unhighlightEdge(nodes.indexOf(minEdge.getStart()),
				nodes.indexOf(minEdge.getEnd()), new MsTiming(100), null);

	}

	/**
	 * highlight the minimal edge
	 * 
	 * @param state
	 */
	private void highlightMinEdge(State state) {
		Graph graph = (Graph) po.get("graph");
		List<NodeState> nodes = state.getNodes();
		EdgeState edge = state.getMinimalEdge();
		this.minEdge = edge;
		graph.highlightEdge(nodes.indexOf(edge.getStart()),
				nodes.indexOf(edge.getEnd()), new MsTiming(100), null);

	}

	/**
	 * unhighlight the possible edges
	 * 
	 * @param state
	 */
	private void unhighlightPossibleEdges(State state) {
		Graph graph = (Graph) po.get("graph");
		List<NodeState> nodes = state.getNodes();
		List<EdgeState> edges = state.getEdges();
		for (EdgeState edge : edges) {
			graph.unhighlightEdge(nodes.indexOf(edge.getStart()),
					nodes.indexOf(edge.getEnd()), new MsTiming(100), null);
		}
	}

	/**
	 * highlight the possible edges
	 * 
	 * @param state
	 */
	private void highlightPossibleEdges(State state) {
		Graph graph = (Graph) po.get("graph");
		List<NodeState> nodes = state.getNodes();
		List<EdgeState> possibleEdges = state.getPossibleEdges();
		for (EdgeState edge : possibleEdges) {
			graph.highlightEdge(nodes.indexOf(edge.getStart()),
					nodes.indexOf(edge.getEnd()), new MsTiming(100), null);
		}
	}

	/**
	 * checking if node is active and highlight or unhighlight
	 * 
	 * @param state
	 */
	private void checkActiveNode(State state) {
		unhighlightActiveNode(state);
		highlightActiveNode(state);
	}

	/**
	 * unhighlight active node
	 * 
	 * @param state
	 */
	private void unhighlightActiveNode(State state) {
		Graph graph = (Graph) po.get("graph");
		for (int i = 0; i < state.getNodes().size(); i++) {
			graph.unhighlightNode(i, new MsTiming(100), null);
		}
	}

	/**
	 * highlight active node
	 * 
	 * @param state
	 */
	private void highlightActiveNode(State state) {
		Graph graph = (Graph) po.get("graph");
		NodeState activeNode = state.getActiveNode();
		if (activeNode != null)
			graph.highlightNode(state.getNodes().indexOf(activeNode),
					new MsTiming(100), null);
	}

	/**
	 * returns group with active highlighted nodes
	 * 
	 * @param state
	 * @return
	 */
	private Group highlightActiveNodes(State state) {
		po.hide("actNodes_highlights");

		List<NodeState> nodes_state = state.getNodes();

		Coordinates upperLeftCorner = valMatrix_position;

		int x = upperLeftCorner.getX();
		int y = upperLeftCorner.getY();

		LinkedList<Primitive> primitives = new LinkedList<Primitive>();

		for (int i = 0; i < nodes_state.size(); i++) {
			NodeState node_state = nodes_state.get(i);

			if (node_state.isActive()) {

				int start_x = x;
				int start_y = y + delta_y_val_matrix + i * delta_y_val_matrix;

				int end_x = start_x + delta_x_val_matrix;
				int end_y = start_y + delta_y_val_matrix;

				Rect rect = getRect("actNodes_highlight_" + i, new Coordinates(
						start_x, start_y), new Coordinates(end_x, end_y),
						actNodes_props);
				primitives.add(rect);
			}
		}
		return getGroup("actNodes_highlights", primitives);

	}

	/**
	 * unhighlight values in valueMatrix
	 * 
	 * @param state
	 */
	private void unhighlightValueMatrix(State state) {
		po.hide("valMatrix_highlights");

	}

	/**
	 * unhighlight values in AdjMatrix
	 * 
	 * @param state
	 */
	private void unhighlightAdjMatrix(State state) {
		po.hide("adjMatrix_highlights");

	}

	/**
	 * highlight values in AdjMatrix
	 * 
	 * @param state
	 */
	private void highlightAdjMatrix(State state) {
		po.hide("adjMatrix_highlights");
		Coordinates upperLeftCorner = adjMatrix_position;

		int x = upperLeftCorner.getX();
		int y = upperLeftCorner.getY();

		List<NodeState> nodes = state.getNodes();
		List<EdgeState> edges = state.getEdges();

		LinkedList<Primitive> primitives = new LinkedList<Primitive>();

		for (int k = 0; k < edges.size(); k++) {
			EdgeState edge = edges.get(k);
			EdgeState edge_prevState = prevState.getEdges().get(k);

			if (edge.getCurrentFlow() != edge_prevState.getCurrentFlow()) {
				int start_index = nodes.indexOf(edge.getStart());
				int end_index = nodes.indexOf(edge.getEnd());

				int start_x = x + delta_x_adj_matrix + end_index
						* delta_x_adj_matrix;
				int start_y = y + delta_y_adj_matrix + start_index
						* delta_y_adj_matrix;
				int end_x = start_x + delta_x_adj_matrix;
				int end_y = start_y + delta_y_adj_matrix;

				Rect rect = getRect("adjMatrix_h_" + k, new Coordinates(
						start_x, start_y), new Coordinates(end_x, end_y),
						highlight_props);

				primitives.add(rect);

			}
		}

		getGroup("adjMatrix_highlights", primitives);

	}

	/**
	 * highlight values in ValueMatrix
	 * returns group of highlights in Value matrix
	 * 
	 * @param state
	 * @return
	 */
	private Group highlightValueMatrix(State state) {
		po.hide("valMatrix_highlights");
		List<NodeState> nodes_state = state.getNodes();
		List<NodeState> nodes_prevState = prevState.getNodes();

		Coordinates upperLeftCorner = valMatrix_position;
		int x = upperLeftCorner.getX();
		int y = upperLeftCorner.getY();

		LinkedList<Primitive> primitives = new LinkedList<Primitive>();

		for (int i = 0; i < nodes_state.size(); i++) {
			NodeState node_state = nodes_state.get(i);
			NodeState node_prevState = nodes_prevState.get(i);
			if (node_state.getHeight() != node_prevState.getHeight()) {

				int start_x = x + 2 * delta_x_val_matrix
						+ valMatrix_names[0].length() * character_width;
				int start_y = y + delta_y_val_matrix + i * delta_y_val_matrix;

				int end_x = start_x + delta_x_val_matrix + character_width
						* valMatrix_names[1].length();
				int end_y = start_y + delta_y_val_matrix;
				Rect rect = getRect("valMatrix_hheight_" + i, new Coordinates(
						start_x, start_y), new Coordinates(end_x, end_y),
						highlight_props);
				primitives.add(rect);
			}
			if (node_state.getStartOrEnd() == null) {

				if (node_state.getFlowExcess() != node_prevState
						.getFlowExcess()) {

					int start_x = x + delta_x_val_matrix;
					int start_y = y + delta_y_val_matrix + i
							* delta_y_val_matrix;

					int end_x = start_x + delta_x_val_matrix + character_width
							* valMatrix_names[0].length();
					int end_y = start_y + delta_y_val_matrix;

					Rect rect = getRect("valMatrix_hflow_" + i,
							new Coordinates(start_x, start_y), new Coordinates(
									end_x, end_y), highlight_props);
					primitives.add(rect);
				}
			}
		}
		return getGroup("valMatrix_highlights", primitives);
	}

	/**
	 * returns a rectangle
	 * 
	 * @param key
	 * @param ul
	 * @param lr
	 * @param rectProps
	 * @return
	 */
	private Rect getRect(String key, Coordinates ul, Coordinates lr,
			RectProperties rectProps) {
		String uuid = po.getUUID();
		Rect rect = new Rect(rectGenerator, ul, lr, uuid, displayOptions,
				rectProps);
		po.set(rect, key, uuid);
		return rect;
	}

	/**
	 * updates positions
	 * 
	 * @param state
	 */
	private void updatePositions(State state) {
		this.length_max_adj_value = 0;
		List<EdgeState> edges = state.getEdges();
		for (int i = 0; i < edges.size(); i++) {
			EdgeState edge = edges.get(i);
			int tmp = edge.getCapacity().toString().length() * 2 + 1;
			if (tmp > length_max_adj_value)
				length_max_adj_value = tmp;
		}
		this.delta_x_adj_matrix = length_max_adj_value * character_width + 2
				* character_width;
		this.delta_y_adj_matrix = 30;
	}

	/**
	 * shows value matrix
	 * 
	 * @param state
	 */
	private void showValueMatrix(State state) {
		getValueMatrix("valMatrix", state.getNodes(), state.getEdges());
		getValueMatrixValues("valMatrixValues", state.getNodes());
	}

	/**
	 * get values of value matrix
	 * returns group
	 * 
	 * @param key
	 * @param nodes
	 * @return
	 */
	private Group getValueMatrixValues(String key, List<NodeState> nodes) {
		po.hide(key);
		Coordinates upperLeftCorner = valMatrix_position;
		int x = upperLeftCorner.getX();
		int y = upperLeftCorner.getY();

		LinkedList<Primitive> primitives = new LinkedList<Primitive>();

		for (int i = 0; i < nodes.size(); i++) {
			NodeState node = nodes.get(i);

			int current_start_x = x + delta_x_val_matrix + character_width;
			int current_start_y = y + delta_y_val_matrix + i
					* delta_y_val_matrix + delta_y_val_matrix / 2
					- character_height / 2;
			String flowexcess = (node.getStartOrEnd() == null) ? node
					.getFlowExcess().toString() : "0";

			Coordinates position_start = new Coordinates(current_start_x,
					current_start_y);
			Text start_text = getText("valMatrix_fvalue_" + i, flowexcess,
					position_start);

			primitives.add(start_text);

			current_start_x = x + 2 * delta_x_val_matrix
					+ (valMatrix_names[0].length() * character_width)
					+ character_width;
			current_start_y = y + delta_y_val_matrix + i * delta_y_val_matrix
					+ delta_y_val_matrix / 2 - character_height / 2;

			position_start = new Coordinates(current_start_x, current_start_y);
			start_text = getText("valMatrix_hvalue_" + i, node.getHeight()
					.toString(), position_start);

			primitives.add(start_text);
		}

		return getGroup(key, primitives);
	}

	/**
	 * show adjacency matrix
	 * 
	 * @param state
	 */
	private void showAdjacencyMatrix(State state) {
		getAdjacencyMatrix("adjMatrix", state.getNodes(), state.getEdges());
		getAdjacencyMatrixValues("adjMatrixValues", state.getNodes(),
				state.getEdges());
	}

	/**
	 * get values of adjacency matrix
	 * returns group
	 * 
	 * @param key
	 * @param nodes
	 * @param edges
	 * @return
	 */
	private Group getAdjacencyMatrixValues(String key, List<NodeState> nodes,
			List<EdgeState> edges) {
		po.hide(key);

		Coordinates upperLeftCorner = adjMatrix_position;

		int x = upperLeftCorner.getX();
		int y = upperLeftCorner.getY();

		LinkedList<Primitive> primitives = new LinkedList<Primitive>();

		for (int k = 0; k < edges.size(); k++) {
			EdgeState edge = edges.get(k);

			int start_index = nodes.indexOf(edge.getStart());
			int end_index = nodes.indexOf(edge.getEnd());

			int current_end_x = x + delta_x_adj_matrix + end_index
					* delta_x_adj_matrix + character_width;
			int current_start_y = y + delta_y_adj_matrix + start_index
					* delta_y_adj_matrix + delta_y_adj_matrix / 2
					- character_height / 2;

			int curFlow = edge.getCurrentFlow();
			int cap = edge.getCapacity();
			String value = curFlow + "/" + cap;

			Coordinates position_value = new Coordinates(current_end_x,
					current_start_y);
			Text value_text = getText("value_" + start_index + "_" + end_index,
					value, position_value);

			primitives.add(value_text);

		}

		return getGroup(key, primitives);

	}

	/**
	 * return graph
	 * 
	 * @param state
	 * @return
	 */
	private Graph showGraph(State state) {
		po.hide("graph");
		String labels[] = state.getNodeLabels();
		int adjmatrix[][] = state.getAdjacencyMatrix();
		Node nodes[] = graph.getCoordinates();
		return getGraph("graph", adjmatrix, nodes, labels, graphProps);
	}

	/**
	 * get a graph
	 * 
	 * @param key
	 * @param adjmatrix
	 * @param nodes
	 * @param labels
	 * @param graphProps
	 * @return
	 */
	private Graph getGraph(String key, int[][] adjmatrix, Node[] nodes,
			String[] labels, GraphProperties graphProps) {
		String uuid = po.getUUID();
		Graph graph = new Graph(graphGenerator, uuid, adjmatrix, nodes, labels,
				displayOptions, graphProps);
		po.set(graph, key, uuid);
		return graph;
	}

	/**
	 * Builds residual graph
	 * 
	 * @param state
	 * @return
	 */
	@SuppressWarnings("unused")
	private Graph buildResidualGraph(State state) {
		String labels[] = state.getNodeLabels();
		int adjmatrix[][] = state.getAdjacencyMatrix(true);
		int x = 100;
		int y = 600;
		Node nodes[] = state.getCoordinates(x, y);
		GraphProperties props = new GraphProperties();
		props.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
		props.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
		props.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
		Graph g = lang.newGraph("huhu", adjmatrix, nodes, labels, null, props);
		return g;
	}

	/**
	 * generating the Animation Code
	 * 
	 * @param states
	 */
	public void generateAnimationCode(ArrayList<State> states) {
		showTitle();
		showIntroText();

		lang.nextStep();

		po.hide("introtext");

		for (int i = 0; i < states.size(); i++) {
			State state = states.get(i);
			render(state);
		}
		lang.nextStep();

		hideAll();
		showOutroText();

		lang.nextStep();
	}

	/**
	 * hiding value matrix, adjacency matrix and the code
	 */
	private void hideAll() {
		po.hide("adjMatrix");
		po.hide("valMatrix");
		po.hide("adjMatrixValues");
		po.hide("valMatrixValues");
		po.hide("codebox");
		po.hide("code");
		po.hide("valMatrix_highlights");
		po.hide("actNodes_highlights");
		// po.hide("maxFlow");
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
			pseudoCode.highlight(i - 1);
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
	 * shows the intro text
	 */
	private void showIntroText() {
		this.pseudoCode_x = 50;
		this.pseudoCode_y = 60;
		String uuid = po.getUUID();
		SourceCode sourceCode = new SourceCode(sourceCodeGenerator,
				new Coordinates(pseudoCode_x, pseudoCode_y), uuid, null,
				sourceCodeProps);
		po.set(sourceCode, "introtext", uuid);
		Pattern linePattern = Pattern.compile("(?<depth>\\t*)(?<code>.*)");
		for (String line : introtext) {
			Matcher matcher = linePattern.matcher(line);
			if (matcher.matches()) {
				int depth = matcher.group("depth").length();
				String code = matcher.group("code").trim();
				sourceCode.addCodeLine(code, null, depth, null);
			}
		}
	}

	/**
	 * shows the outro text
	 */
	private void showOutroText() {
		this.pseudoCode_x = 50;
		this.pseudoCode_y = 60;
		String uuid = po.getUUID();
		SourceCode sourceCode = new SourceCode(sourceCodeGenerator,
				new Coordinates(pseudoCode_x, pseudoCode_y), uuid, null,
				sourceCodeProps);
		po.set(sourceCode, "outrotext", uuid);
		Pattern linePattern = Pattern.compile("(?<depth>\\t*)(?<code>.*)");
		for (String line : outrotext) {
			Matcher matcher = linePattern.matcher(line);
			if (matcher.matches()) {
				int depth = matcher.group("depth").length();
				String code = matcher.group("code").trim();
				sourceCode.addCodeLine(code, null, depth, null);
			}
		}
	}

	/**
	 * shows source code
	 */
	private void showCode() {
		this.pseudoCode_x = 555;
		this.pseudoCode_y = 55;
		Rect rect = new Rect(rectGenerator, new Coordinates(pseudoCode_x - 15,
				pseudoCode_y - 5), new Coordinates(pseudoCode_x + 300,
				pseudoCode_y + 400), po.getUUID(),
				(DisplayOptions) displayOptions, sourceCodeBackgroundProps);
		Text title = getText("title_code", "Der Algorithmus", new Coordinates(
				pseudoCode_x, pseudoCode_y));
		String uuid = po.getUUID();
		SourceCode sourceCode = new SourceCode(sourceCodeGenerator,
				new Coordinates(pseudoCode_x, pseudoCode_y), uuid, null,
				sourceCodeProps);
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
	 * Returns a group
	 * 
	 * @param key
	 * @param primitives
	 * @return
	 */
	private Group getGroup(String key, LinkedList<Primitive> primitives) {
		String uuid = po.getUUID();
		Group group = new Group(groupGenerator, primitives, uuid);
		po.set(group, key, uuid);
		return group;
	}

	/**
	 * returns a graph by input string
	 * 
	 * @param input
	 * @return
	 * @throws IllegalArgumentException
	 */
	public PRGraph parseGraph(String input) throws IllegalArgumentException {

		int pos = input.indexOf("%graphscript");
		if (pos == -1) {
			throw new IllegalArgumentException("No %graphscript");
		}
		input = input.substring(pos + 12);

		pos = input.indexOf("graph");
		if (pos == -1) {
			throw new IllegalArgumentException("No %graphscript");
		}

		input = input.substring(pos + 6);
		pos = input.indexOf(" ");

		int numberOfNodes = Integer.valueOf(input.substring(0, pos));

		System.out.println(numberOfNodes);

		input = input.substring(pos + 1);

		pos = input.indexOf("directed weighted");
		if (pos == -1) {
			throw new IllegalArgumentException(
					"Must be directed weighted graph");
		}

		input = input.substring(pos + 17);

		pos = input.indexOf("graphcoordinates at");
		if (pos == -1) {
			throw new IllegalArgumentException("No Graph coordinates");
		}
		input = input.substring(pos + 20);
		pos = input.indexOf(" ");
		int graphCoorx = Integer.valueOf(input.substring(0, pos));

		input = input.substring(pos + 1);
		pos = input.indexOf("\n");

		int graphCoory = Integer.valueOf(input.substring(0, pos));
		input = input.substring(pos);

		pos = input.indexOf("startknoten");
		if (pos == -1) {
			throw new IllegalArgumentException("No starting node");
		}
		input = input.substring(pos + 12);
		pos = input.indexOf("\n");
		String startnode = String.valueOf(input.substring(0, pos).trim());
		input = input.substring(pos);

		pos = input.indexOf("zielknoten");
		if (pos == -1) {
			throw new IllegalArgumentException("No end node");
		}
		input = input.substring(pos + 11);
		pos = input.indexOf("\n");
		String endnode = String.valueOf(input.substring(0, pos).trim());
		input = input.substring(pos);

		Coordinates[] positions = new Coordinates[numberOfNodes];
		HashMap<String, PRNode> nodes_map = new HashMap<String, PRNode>();
		List<PRNode> nodes = new LinkedList<PRNode>();
		for (int i = 1; i <= numberOfNodes; i++) {
			pos = input.indexOf("node");
			if (pos == -1) {
				throw new IllegalArgumentException("error with the nodes");
			}
			input = input.substring(pos + 5);
			pos = input.indexOf(" ");
			String name = String.valueOf(input.substring(0, pos)).toLowerCase()
					.trim();

			if (name.toLowerCase().equals(startnode.toLowerCase())) {
				PRNode node = new PRNode(name, "start");
				nodes_map.put(name, node);
				nodes.add(node);
			} else if (name.toLowerCase().equals(endnode.toLowerCase())) {
				PRNode node = new PRNode(name, "end");
				nodes_map.put(name, node);
				nodes.add(node);
			} else {
				PRNode node = new PRNode(name);
				nodes_map.put(name, node);
				nodes.add(node);
			}

			input = input.substring(pos + 4);
			pos = input.indexOf(" ");

			int x = Integer.valueOf(input.substring(0, pos).trim());

			input = input.substring(pos + 1);
			pos = input.indexOf("\n");

			int y = Integer.valueOf(input.substring(0, pos).trim());

			positions[i - 1] = new Coordinates(x, y);
		}

		List<PREdge> edges = new ArrayList<PREdge>();
		boolean cond = true;

		do {
			pos = input.indexOf("edge");
			input = input.substring(pos + 5);

			pos = input.indexOf(" ");
			String start = input.substring(0, pos);

			input = input.substring(pos + 1);

			pos = input.indexOf(" ");
			String end = input.substring(0, pos);

			input = input.substring(pos + 1);

			if (input.indexOf("weight") == -1) {
				throw new IllegalArgumentException("no weight key word");
			}

			pos = input.indexOf("weight");
			input = input.substring(pos + 7);

			pos = input.indexOf("\n");

			Integer weight = Integer.valueOf(input.substring(0, pos).trim());

			PREdge edge = new PREdge(nodes_map.get(start.toLowerCase()),
					nodes_map.get(end.toLowerCase()), weight);

			edges.add(edge);

			input = input.substring(pos + 1);

			cond = input.indexOf("edge") != -1;
		} while (cond);
		PRGraph graph = null;
		try {
			graph = new PRGraph(nodes, edges);
			graph.setCoordinates(positions);
		} catch (Exception e) {
			throw new IllegalArgumentException("Graphscript is incorrect.");
		}
		return graph;
	}

}