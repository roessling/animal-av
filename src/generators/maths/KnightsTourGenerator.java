/*
 * KnightsTour.java
 * Dominik Bierwirth, Vladimir Romanenko, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import algoanim.counter.enumeration.PrimitiveEnum;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.*;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.*;
import java.util.*;
import java.util.List;

import algoanim.primitives.generators.Language;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class KnightsTourGenerator implements Generator {

	private final int WINDOW_SIZE_X = 800;
	private final int WINDOW_SIZE_Y = 600;

	private Language lang;

	private int start_x;
	private int start_y;
	private int size_y;
	private int size_x;

	private CircleProperties knight;
	private PolylineProperties cross;

	private int tile_size;
	private SquareProperties white_tiles;
	private SquareProperties black_tiles;

	private TextProperties remaining_moves;
	private SourceCodeProperties description;
	private SourceCodeProperties source_code_prop;

	private Square[][] squares;
	private BooleanMatrix visited;
	private Text[][] prio_texts;
	private Position current;
	private Circle current_marker;

	private List<Polyline> crosses;
	private List<Polyline> route;

	private SourceCode source_code;

	private Text title;

	private IntMatrix moves_left;

	private TwoValueCounter counter;
	private TwoValueCounter visited_counter;

	private Variables var;

	private static class Position {

		int id;
		int x;
		int y;

		private Position(int x, int y, int id) {
			this.x = x;
			this.y = y;
			this.id = id;
		}

		private Position(int x, int y) {
			this(x, y, -1);
		}

		@Override
		public String toString() {
			return "(" + x + "," + y + ")";
		}

		private String full_string() {
			return "(" + x + "," + y + "|" + id + ")";
		}
	}

	private static class BooleanMatrix extends MatrixPrimitive {

		private boolean[][] booleans;

		private BooleanMatrix(int width, int height) {
			super(null, null);
			booleans = new boolean[width][height];
			this.nrCols = width;
			this.nrRows = height;
		}

		private void put(int x, int y, boolean val) {
			this.notifyObservers(PrimitiveEnum.put);
			this.booleans[x][y] = val;
		}

		private void put(Position pos, boolean val) {
			put(pos.x, pos.y, val);
		}

		private boolean get(int x, int y) {
			this.notifyObservers(PrimitiveEnum.getElement);
			return this.booleans[x][y];
		}

		private boolean get(Position pos) {
			return get(pos.x, pos.y);
		}
	}

	public void init() {
		lang = new AnimalScript("Knight's Tour with move-order tiebreak", "Dominik Bierwirth, Vladimir Romanenko", WINDOW_SIZE_X, WINDOW_SIZE_Y);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		start_x = (Integer) primitives.get("start_x");
		start_y = (Integer) primitives.get("start_y");
		size_x = (Integer) primitives.get("size_x");
		size_y = (Integer) primitives.get("size_y");

		knight = (CircleProperties) props.getPropertiesByName("knight");
		cross = (PolylineProperties) props.getPropertiesByName("cross");

		tile_size = (Integer) primitives.get("tile_size");
		white_tiles = (SquareProperties) props.getPropertiesByName("white_tiles");
		black_tiles = (SquareProperties) props.getPropertiesByName("black_tiles");

		remaining_moves = (TextProperties) props.getPropertiesByName("remaining_moves");
		description = (SourceCodeProperties) props.getPropertiesByName("description");
		source_code_prop = (SourceCodeProperties) props.getPropertiesByName("source_code_prop");

		List<String> move_orders = all_move_orders();

		for (int i = 0; i < move_orders.size(); i++) {
			String order = move_orders.get(i);

			var = lang.newVariables();

			var.declare("String", "current", new Position(start_x, start_y).toString(), "Current position of the knight");
			var.declare("String", "order", order, "The MoveOrder to use currently");
			var.declare("String", "moves", "[]", "The potential next positions to move to");

			TextProperties tp_title = new TextProperties();
			tp_title.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
			tp_title.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));

			Coordinates title_coord = new Coordinates(WINDOW_SIZE_X / 2, 0);

			title = lang.newText(title_coord, "Knight's Tour", "title", null, tp_title);

			int title_offset_y = 15;
			int title_offset_x = -title_coord.getX() + WINDOW_SIZE_X / 8;

			make_introduction(title_offset_x, title_offset_y);

			if (make_tour(order, title_offset_x, title_offset_y)) {
				make_summary(i + 1, order);
				break;
			}

			init();
		}

		return lang.toString();
	}

	/**
	 * a method to make an introduction to the algorithm
	 *
	 * @param offset_x the x offset from the title
	 * @param offset_y the y offset from the title
	 */
	private void make_introduction(int offset_x, int offset_y) {

		SourceCode desc = lang.newSourceCode(new Offset(offset_x, offset_y, title, "S"), "start-desc", null, description);
		desc.addMultilineCode("A Knight's Tour is a route on a chessboard where the Knight\n" +
				"touches each square exactly once.\n" +
				"To determine the next square to move to, we use a heuristic:\n" +
				"\t\tthe square that gives the least move options\n" +
				"\t\tafter we move to it is the one we move to next.\n" +
				"Since random tie breaks do not guarantee a solution, we use a deterministic tie-breaker:", "desc-1", null);

		Position demo_start = new Position(2, 2);
		Square[][] order_squares = create_squares(5, 5, desc, 0, 10, "SW", "-demo-sq");
		Text[][] order_texts = create_prio_texts(order_squares, "-demo-moves");
		List<Position> demo_orders = squares_from(demo_start, new BooleanMatrix(5, 5));

		Circle demo_order_circle = set_circle(demo_start, order_squares, "demo_order_circle");

		for (Position p : demo_orders) {
			Text t = order_texts[p.x][p.y];
			t.setText(" " + p.id + " ", null, null);
			t.show();
		}

		SourceCode desc2 = lang.newSourceCode(new Offset(15, 0, order_squares[4][0], "NE"), "start-desc2", null, description);
		desc2.addMultilineCode("These are the id's given to each each relative\n" +
						"position the Knight can move to. A MoveOrder,\n" +
						"for example '13245768', would then assign a\n" +
						"priority to each relative position.\n" +
						"In the following, these ids's are shown\n" +
						"beside the number of moves left from\n" +
						"that square. So if a square has 5 moves\n" +
						"left and the id '3', it would show as '5|3'",
				"desc-2", null);

		lang.nextStep("Introduction");

		// hide the start description
		desc.hide();
		desc2.hide();
		demo_order_circle.hide();
		for (int i = 0; i < order_squares.length; i++) {
			for (int j = 0; j < order_squares[i].length; j++) {
				order_squares[i][j].hide();
				order_texts[i][j].setText("", null, null);
				order_texts[i][j].hide();
			}
		}
	}

	/**
	 * a method to make a summary of a tour
	 *
	 * @param iteration the iteration the summary is for. this is purely used for stating the iteration in text and
	 *                  NOT used for a lookup of actual values
	 * @param order     the MoveOrder the tour used
	 */
	private void make_summary(int iteration, String order) {

		String move_order_place;
		int iteration_ones = iteration % 10;

		if (iteration < 4) {
			if (iteration == 1)
				move_order_place = "first";
			else if (iteration == 2)
				move_order_place = "second";
			else
				move_order_place = "third";
		} else {
			move_order_place = iteration + "";

			switch (iteration_ones) {
				case 1:
					move_order_place += "st";
					break;
				case 2:
					move_order_place += "nd";
					break;
				case 3:
					move_order_place += "rd";
					break;
				default:
					move_order_place += "th";
			}
		}

		double avg_checks = counter.getAccess() * 1.0d / (size_x * size_y);
		double avg_ass = counter.getAssigments() * 1.0d / (size_x * size_y);

		SourceCode end_desc = lang.newSourceCode(source_code.getUpperLeft(), "end_desc", null, description);
		end_desc.addMultilineCode("This is the route the Knight took.\n" +
						"The MoveOrder which led to this tour was: " + order + "\n" +
						"This was the " + move_order_place + " MoveOrder which was tried.\n" +
						"This tour had an average of " + avg_checks + " potential next fields\n" +
						"and visited status checks (each) in each turn.\n" +
						"There were an average of " + avg_ass + " potential next squares which were\n" +
						"compared each turn.",
				"end_desc1", null);

		lang.nextStep("Summary");
	}

	/**
	 * a method to create a Knitght's Tour from start to finish, excluding introduction and summary but including
	 * preparations for the summary such as hiding th source code and the marks fo visited positions
	 *
	 * @param order    the MoveOrder to user for this tour (attempt)
	 * @param offset_x the x offset for the board and the sourcecode from the middle of the title/window
	 * @param offset_y the y offset for the board and the sourcecode from the title
	 * @return whether or not this tour-attempt was successful
	 */
	private boolean make_tour(String order, int offset_x, int offset_y) {

		int num_of_squares = size_x * size_y;

		visited = new BooleanMatrix(size_x, size_y);

		squares = create_squares(size_x, size_y, title, offset_x, offset_y, "S", "-sq");
		prio_texts = create_prio_texts(squares, "-moves");

		/*
		It was decided to not directly animate the IntMatrix containing the moves_left values since most of them are
		irrelevant in any given iteration and would only add to screen clutter.
		the alternative that was used, a matrix of squares with a matrix of textfields, was easier to mold into
		a consistently visually pleasing animation since the squares never change unlike the fields of this Matrix
		plus the fact that this matrix does not contain all the necessary information for a field, like the id
		of their position in a given iteration, which changes every time it comes into relevancy
		not to mention the raw-defined square matrix being easier to address for specific positions in regards to offsetting
		other elements from them (like the crosses, the route and the marker for the knight)
		 */
		moves_left = lang.newIntMatrix(new Coordinates(0, 0), new int[size_x][size_y], "moves-left", null);
		for (int i = 0; i < size_x; i++) {
			for (int j = 0; j < size_y; j++) {
				moves_left.put(i, j, get_neighbors(i, j, visited).size(), null, null);
			}
		}
		moves_left.hide();

		crosses = new ArrayList<>(num_of_squares * 2);
		route = new ArrayList<>(num_of_squares - 1);

		Node source_code_coord = new Offset(15, 0, squares[size_x - 1][0], "NE");
		source_code = lang.newSourceCode(source_code_coord, "code", null, source_code_prop);

		source_code.addCodeLine("while (true) {", "while-head", 0, null);
		source_code.addCodeLine("new_moves = get_moves_from(current_position)", "new-moves", 1, null);
		source_code.addCodeLine("if (new_moves.is_empty())", "break-cond", 1, null);
		source_code.addCodeLine("break", "break", 2, null);
		source_code.addCodeLine("next_position = get_next_move(new_moves)", "next-pos", 1, null);
		source_code.addCodeLine("go_to(next_position)", "go-to", 1, null);
		source_code.addCodeLine("}", "while-closing", 0, null);

		source_code.highlight("while-head", true);
		source_code.highlight("while-closing", true);

		counter = lang.newCounter(moves_left);
		TwoValueView view = lang.newCounterView(counter, new Offset(0, 10, source_code, "SW"), new CounterProperties(), true, false, new String[]{"potential moves assigned:", "potential next fields checked:"});

		visited_counter = lang.newCounter(visited);
		TwoValueView visited_view = lang.newCounterView(visited_counter, new Offset(0, 60, source_code, "SW"), new CounterProperties(), true, false, new String[]{"fields visited", "visited status checked"});

		current = new Position(start_x, start_y);
		current_marker = set_circle(current, "current-marker");
		Circle start_circle = set_circle(current, "start-marker");
		start_circle.hide();
		List<Position> potential_squares = go_to(current);

		lang.nextStep("MoveOrder: " + order);

		while (true) {
			current = next_position(potential_squares, order);

			if (current == null)
				break;

			// move to the new position
			source_code.unhighlight("next-pos");
			source_code.highlight("go-to");
			potential_squares = go_to(current);

			lang.nextStep();
			source_code.unhighlight("go-to");
		}

		view.hide();
		visited_view.hide();

		counter.deactivateCounting();
		visited_counter.deactivateCounting();

		return finish_tour(start_circle);
	}

	/**
	 * a method to to make some final preparations before the summary and to determine if the tour was successful
	 *
	 * @param start_circle the marker for the starting position, will be shown again
	 * @return whether or not all positions have been visited
	 */
	private boolean finish_tour(Circle start_circle) {
		for (Polyline polyline : crosses) {
			polyline.hide();
		}

		for (Polyline polyline : route) {
			polyline.show();
		}

		start_circle.show();
		source_code.hide();

		boolean successful_tour = true;

		for (int i = 0; i < visited.getNrCols(); i++) {
			for (int j = 0; j < visited.getNrRows(); j++) {
				successful_tour = successful_tour && visited.get(i, j);
			}
		}

		return successful_tour;
	}

	/**
	 * a method to move to a specific position
	 *
	 * @param pos the position to move to
	 * @return the list of next potential positions to move to (already done here since we update their move_left entry here)
	 */
	private List<Position> go_to(Position pos) {
		List<Position> next_potential_squares = squares_from(pos);

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (Position p : next_potential_squares) {
			moves_left.put(p.x, p.y, moves_left.getElement(p.x, p.y) - 1, null, null);
			sb.append(p.full_string()).append(", ");
		}

		if (!next_potential_squares.isEmpty())
			sb.delete(sb.length() - 2, sb.length());

		sb.append(']');
		var.set("moves", sb.toString());

		current = pos;
		var.set("current", pos.toString());

		make_cross(pos);
		visited.put(pos, true);
		move_current_circle();

		return next_potential_squares;
	}

	/**
	 * a method to get the next position to move to
	 *
	 * @param potential_squares the squares the knight can potentially move to
	 * @param order             the MoveOrder to user for tie-breaking
	 * @return the next position to move to according to Wansdoff and the MoveOrder. will be null if potential_squares is empty
	 */
	private Position next_position(List<Position> potential_squares, String order) {
		source_code.highlight("new-moves");
		Position next = null;
		int current_min_moves = Integer.MAX_VALUE;

		for (Position pos : potential_squares) {
			int moves_from_pos = moves_left.getElement(pos.x, pos.y);

			squares[pos.x][pos.y].changeColor(null, Color.RED, null, null);

			Text t = prio_texts[pos.x][pos.y];
			String prio_text = moves_from_pos + "|" + pos.id;
			t.setText(prio_text, null, null);
			t.show();

			if (moves_from_pos < current_min_moves) {
				next = pos;
				current_min_moves = moves_from_pos;
			} else if (moves_from_pos == current_min_moves) {
				next = break_tie(pos, next, order);
			}
		}

		lang.nextStep();

		// if there are no moves available, return false
		source_code.unhighlight("new-moves");
		source_code.highlight("break-cond");
		lang.nextStep();

		if (potential_squares.isEmpty()) {
			source_code.highlight("break-cond", true);
			source_code.highlight("break");
			lang.nextStep();

			source_code.unhighlight("break");
			source_code.unhighlight("break-cond", true);
			source_code.unhighlight("while-head", true);
			source_code.unhighlight("while-closing", true);
			return null;
		}

		// determine the next position to move to
		source_code.unhighlight("break-cond");
		source_code.highlight("next-pos");

		Text t = prio_texts[next.x][next.y];
		t.changeColor(null, Color.GREEN, null, null);

		lang.nextStep();

		// remove the highlighting from the potential squares we could move to and hide the text
		for (Position pos : potential_squares) {
			Color old_color = (Color) squares[pos.x][pos.y].getProperties().get(AnimationPropertiesKeys.FILL_PROPERTY);
			squares[pos.x][pos.y].changeColor(null, old_color, null, null);
			prio_texts[pos.x][pos.y].setText("   ", null, null);
			prio_texts[pos.x][pos.y].hide();
		}

		// draw the line between the old position and the new position
		PolylineProperties pp = new PolylineProperties();
		pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		pp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);

		route.add(lang.newPolyline(new Node[]{
						coord(current, "C", 0, 0),
						coord(next, "C", 0, 0)},
				current + "-" + next, null, pp));

		return next;
	}

	private Position break_tie(Position pos1, Position pos2, String order) {
		List<Integer> ids = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

		if (!ids.containsAll(Arrays.asList(pos1.id, pos2.id)))
			throw new IllegalArgumentException("Positions must ave valid ids!");

		int pos1_prio = order.indexOf((char) (pos1.id + '0'));
		int pos2_prio = order.indexOf((char) (pos2.id + '0'));

		if (pos1_prio < pos2_prio)
			return pos1;

		return pos2;
	}

	/**
	 * a method to move the current marker to the current position
	 */
	private void move_current_circle() {
		int r = current_marker.getRadius();
		current_marker.moveTo("C", null, coord(current, "C", -r, -r), null, null);
	}

	/**
	 * a method to make a cross out of to Polylines to mark a position as visited
	 *
	 * @param pos the position to mark
	 */
	private void make_cross(Position pos) {

		crosses.add(lang.newPolyline(new Offset[]{
						coord(pos, "NW", tile_size / 4, tile_size / 4),
						coord(pos, "SE", -tile_size / 4, -tile_size / 4)},
				pos + "cross1", null, cross));

		crosses.add(lang.newPolyline(new Offset[]{
						coord(pos, "NE", -tile_size / 4, tile_size / 4),
						coord(pos, "SW", tile_size / 4, -tile_size / 4)},
				pos + "cross2", null, cross));
	}

	private Circle set_circle(Position pos, Square[][] squares, String id) {
		int radius = tile_size / 8;
		return lang.newCircle(coord(pos, squares, "C", 0, 0), radius, id, null, knight);
	}

	private Circle set_circle(Position pos, String id) {
		return set_circle(pos, squares, id);
	}

	/**
	 * a method to more easily get an offset position from a specific square
	 *
	 * @param x         the x position of the square to offset from
	 * @param y         the y position of the square to offset from
	 * @param squares   the square matrix to get the squares from
	 * @param direction the corner of the square to offset from
	 * @param offset_x  the offset from this corner in x direction
	 * @param offset_y  the offset from this corner in y direction
	 * @return the coordinates from the desired corner of the desired square with the desired offset
	 */
	private Offset coord(int x, int y, Square[][] squares, String direction, int offset_x, int offset_y) {
		return new Offset(offset_x, offset_y, squares[x][y], direction);
	}

	private Offset coord(Position pos, Square[][] squares, String direction, int offset_x, int offset_y) {
		return coord(pos.x, pos.y, squares, direction, offset_x, offset_y);
	}

	private Offset coord(int x, int y, String direction, int offset_x, int offset_y) {
		return coord(x, y, squares, direction, offset_x, offset_y);
	}

	private Offset coord(Position pos, String direction, int offset_x, int offset_y) {
		return coord(pos.x, pos.y, squares, direction, offset_x, offset_y);
	}

	/**
	 * see the explanation at the initialization of the moves_left matrix to see why this approach was chosen instead
	 * of the build-in Matrix/Grid capabilities
	 *
	 * @param width     width of the board
	 * @param height    height of the board
	 * @param offset_x  offset in x direction (in pixel)
	 * @param offset_y  offset in y direction (in pixel)
	 * @param from      the primitve to offset from
	 * @param direction the corner of the primitive to ofset from
	 * @param id_suffix to suffix to append to the id of the created objects
	 * @return the Square matrix representing the tiles for the animation
	 */
	private Square[][] create_squares(int width, int height, Primitive from, int offset_x, int offset_y, String direction, String id_suffix) {
		Square[][] squares = new Square[width][height];

		boolean white = true;
		boolean height_is_even = height % 2 == 0;

		squares[0][0] = lang.newSquare(new Offset(offset_x, offset_y, from, direction), tile_size, "0_0" + id_suffix, null, black_tiles);

		for (int i = 1; i < height; i++) {
			SquareProperties sp = white ? white_tiles : black_tiles;
			squares[0][i] = lang.newSquare(new Offset(0, 0, squares[0][i - 1], "SW"), tile_size, 0 + "_" + i + id_suffix, null, sp);
			white = !white;
		}

		for (int i = 1; i < width; i++) {
			if (height_is_even)
				white = !white;

			for (int j = 0; j < height; j++) {
				SquareProperties sp = white ? white_tiles : black_tiles;

				white = !white;

				String name = i + "_" + j;

				squares[i][j] = lang.newSquare(new Offset(0, 0, squares[i - 1][j], "NE"), tile_size, name + id_suffix, null, sp);
			}
		}

		return squares;
	}

	/**
	 * a method to create text fields for each square
	 * <p>
	 * see the explanation at the initialization of the moves_left matrix to see why this approach was chosen instead
	 * of the build-in Matrix/Grid capabilities
	 *
	 * @param squares   the board of squares to create the text fields to
	 * @param id_suffix to suffix to append to the id of the created objects
	 * @return the matrix of Text objects corresponding to a position each
	 */
	private Text[][] create_prio_texts(Square[][] squares, String id_suffix) {
		Text[][] prio_texts = new Text[squares.length][squares[0].length];

		for (int i = 0; i < prio_texts.length; i++) {
			for (int j = 0; j < prio_texts[i].length; j++) {

				String name = i + "_" + j;

				int font_offset_y = -((Font) remaining_moves.getItem(AnimationPropertiesKeys.FONT_PROPERTY).get()).getSize() / 2;

				prio_texts[i][j] = lang.newText(coord(i, j, squares, "C", 0, font_offset_y), "   ", name + id_suffix, null, remaining_moves);
			}
		}

		return prio_texts;
	}

	private boolean is_valid_position(Position pos, BooleanMatrix visited) {
		return pos.x >= 0 && pos.x < visited.getNrCols() && pos.y >= 0 && pos.y < visited.getNrRows();
	}

	private List<Position> get_neighbors(int x, int y, BooleanMatrix visited) {
		List<Position> neighbors = new LinkedList<>(Arrays.asList(
				new Position(x - 2, y - 1, 7), new Position(x - 2, y + 1, 6), new Position(x + 2, y - 1, 2), new Position(x + 2, y + 1, 3),
				new Position(x - 1, y - 2, 8), new Position(x - 1, y + 2, 5), new Position(x + 1, y - 2, 1), new Position(x + 1, y + 2, 4)));
		neighbors.removeIf(pos -> !is_valid_position(pos, visited));
		return neighbors;
	}

	private List<Position> get_neighbors(Position pos, BooleanMatrix visited) {
		return get_neighbors(pos.x, pos.y, visited);
	}

	/**
	 * @param p       the position to get the new positions from
	 * @param visited the matrix containing the visited-status for each position
	 * @return the list of unvisited and valid Positions the knight can move to from pos
	 */
	private List<Position> squares_from(Position p, BooleanMatrix visited) {
		List<Position> potential_squares = get_neighbors(p, visited);
		potential_squares.removeIf(visited::get);
		return potential_squares;
	}

	private List<Position> squares_from(Position p) {
		return squares_from(p, visited);
	}

	private static List<String> all_move_orders() {
		List<Integer> positions = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
		List<String> orders = new ArrayList<>(40320);

		all_move_orders(new ArrayList<>(8), positions, orders);

		return orders;
	}

	private static void all_move_orders(List<Integer> start, List<Integer> remaining, List<String> acc) {
		if (remaining.isEmpty()) {
			acc.add(list_to_move_order(start));
			return;
		}

		for (Integer integer : remaining) {
			List<Integer> new_start = new ArrayList<>(start);
			new_start.add(integer);
			List<Integer> new_remaining = new ArrayList<>(remaining);
			new_remaining.remove(integer);
			all_move_orders(new_start, new_remaining, acc);
		}
	}

	private static String list_to_move_order(List<Integer> move_order) {
		StringBuilder sb = new StringBuilder(8);
		for (int i : move_order)
			sb.append(i);

		return sb.toString();
	}

	public String getName() {
		return "Knight's Tour with move-order tiebreak";
	}

	public String getAlgorithmName() {
		return "Springerproblem";
	}

	public String getAnimationAuthor() {
		return "Dominik Bierwirth, Vladimir Romanenko";
	}

	public String getDescription() {
		return "A Knight's Tour is a route on a chessboard where the Knight touches each square exactly once."
				+ "\n"
				+ "To determine the next square to move to, we use a heuristic:"
				+ "\n"
				+ "	the square that gives the least move options"
				+ "\n"
				+ "	after we move to it is the one we move to next."
				+ "\n"
				+ "\n"
				+ "Since random tie breaks do not guarantee a solution, we use a deterministic tie-breaker."
				+ "\n"
				+ "We give an ID to each relative position from the Knight and define a move-order which"
				+ "\n"
				+ "gives each of these 8 possible relative positions priority in case of ties."
				+ "\n"
				+ "Not every move-order can solve every board and starting position so we iterate through"
				+ "\n"
				+ "every possible move-order until we get a solution.";
	}

	public String getCodeExample() {
		return "while (true) {"
				+ "\n"
				+ "	new_moves = get_moves_from(current_position)"
				+ "\n"
				+ "	if (new_moves.is_empty())"
				+ "\n"
				+ "		break"
				+ "\n"
				+ "	next_position = get_next_move(new_moves)"
				+ "\n"
				+ "	go_to(next_position)"
				+ "\n"
				+ "}";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}
}