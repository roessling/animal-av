package generators.misc.schulzemethod;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.components.ColorChooserComboBox;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.framework.properties.tree.PropertiesTreeModel;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import util.KalnischkiesGrid;
import util.KalnischkiesGrid.GridStyle;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * @author David Kalnischkies <dk1001@rbg.informatik.tu-darmstadt.de>
 * 
 */
public class SchulzeMethod implements ValidatingGenerator {

	/**
	 * The concrete language object used for creating output
	 */
	private Language lang;

	private SourceCodeProperties sourceCodeProp;
	private TextProperties plainProp;
	private TextProperties boldProp;
	private GraphProperties graphProp;
	private MatrixProperties votesListProp;
	private MatrixProperties pairwiseProp;

	@Override
	public void init() {
		this.lang = new AnimalScript(
				"Schulze Method: Condorcet voting to make decisions in Debian and Gentoo",
				getAnimationAuthor(), 0, 0);

		// This initializes the step mode. Each pair of subsequent steps has to
		// be divided by a call of lang.nextStep();
		this.lang.setStepMode(true);
	}

	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		this.graphProp = (GraphProperties) props.getPropertiesByName("digraph");
		this.votesListProp = (MatrixProperties) props
				.getPropertiesByName("voteslist");
		this.pairwiseProp = (MatrixProperties) props
				.getPropertiesByName("pairwise-comparison-matrix");
		// setup font for various descriptions
		this.sourceCodeProp = (SourceCodeProperties) props
				.getPropertiesByName("descriptions");
		this.plainProp = new TextProperties();
		Font plainfont = (Font) this.sourceCodeProp
				.get(AnimationPropertiesKeys.FONT_PROPERTY);
		this.plainProp.set(AnimationPropertiesKeys.FONT_PROPERTY, plainfont);
		this.boldProp = new TextProperties();
		Font boldfont = plainfont.deriveFont(Font.BOLD);
		this.boldProp.set(AnimationPropertiesKeys.FONT_PROPERTY, boldfont);
		// color properties which are technical primitives
		this.colors = new Color[COLORS.values().length];
		this.colors[COLORS.PAIRWINNER.ordinal()] = (Color) primitives
				.get("pairwise-comparison-winner");
		this.colors[COLORS.PAIRLOSER.ordinal()] = (Color) primitives
				.get("pairwise-comparison-loser");
		this.colors[COLORS.VOTEROW.ordinal()] = (Color) primitives
				.get("voteslist-row-candidate");
		this.colors[COLORS.VOTECOL.ordinal()] = (Color) primitives
				.get("voteslist-column-candidate");
		// get primitives
		Map<String, Integer> votes = new HashMap<String, Integer>();
		if ((Boolean) primitives.get("show-example-wikipedia")) {
			votes.put("A > C > B > E > D", 5);
			votes.put("A > D > E > C > B", 5);
			votes.put("B > E > D > A > C", 8);
			votes.put("C > A > B > E > D", 3);
			votes.put("C > A > E > B > D", 7);
			votes.put("C > B > A > D > E", 2);
			votes.put("D > C > E > B > A", 7);
			votes.put("E > B > A > D > C", 8);
		} else if ((Boolean) primitives.get("show-example-tie")) {
			votes.put("A > B > C > D", 3);
			votes.put("D > A > B > C", 2);
			votes.put("D > B > C > A", 2);
			votes.put("C > B > D > A", 2);
		} else
			votes = getVoteList((String[][]) primitives.get("votes"));
		cigarWinner(votes);
		return this.lang.toString();
	}

	private static final String MOTIVATION = "Deciding between multiple options can be hard.\n"
			+ "While many voting systems allow only to vote for the favorite,\n"
			+ "in many situations ranking the candidates would be preferable.\n\n"
			+ "In 1997 Markus Schulze proposed a system which can select the winner based\n"
			+ "on expressed preferences: The Schulze method (also known as Schwartz method)\n\n"
			+ "This method is one of the most used for decision making deployed by free software communities\n"
			+ "like Debian, Gentoo and KDE as well as MTV, Wikipedia and (political) parties of various countries\n\n"
			+ "The central idea of the method is that winning is a transitive property and\n"
			+ "that this transitivity is the solution of a common problem in graphs: widest path";

	private static final String PAIRWISEPREFERENCES = ""
			+ "As a first step, we calculate how often a candidate is directly preferred over another.\n"
			+ "So for each cell in the table above we add up the votes in which the row candidate (##VOTEROW##)\n"
			+ "is preferred over the column candidate (##VOTECOL##).";

	private static final String PAIRWISEWINNER = ""
			+ "We now look at the pairwise preferences and see which candidate wins in this direct competion.\n"
			+ "The cell with the winner is marked ##PAIRWINNER##, the other is marked ##PAIRLOSER##.\n"
			+ "In the event of a draw, we mark both ##PAIRWINNER##.\n";

	private static final String DIGRAPHPAIRWISE = ""
			+ "With the knowledge of which candidates wins directly over which candidate we can\n"
			+ "now draw a graph with the candidates as nodes and the pairwise winner (##PAIRWINNER## cells) as edges.\n"
			+ "The idea is to use this graph in the next step to see transitive winners.";

	private static final String WIDESTPATH = ""
			+ "With the graph we have the problem reduced to a common graph problem: widest paths.\n"
			+ "This is in essence a flow problem in which we want to find the biggest augementing path\n"
			+ "in the graph from each node to all others. Hence, e.g. a Floyd–Warshall variant can be used.\n"
			+ "Regardless of how its found, the weakest link in this path defines the width of the path.\n"
			+ "This width is the (transitive) preference of one candidate over the other and noted in the grid.";

	private static final String WIDESTWINNER = ""
			+ "Similar to the second step, we compare now the pairwise preferences again,\n"
			+ "but this time we have also the transitive preferences in the grid or in other words:\n"
			+ "A candidate can win against another candidate via winning over other candidate(s),\n"
			+ "which in turn win over the candidate in question.\n"
			+ "The winner cell is again marked ##PAIRWINNER##, the other ##PAIRLOSER##.\n";

	private Text subtitle, gridtitle;
	private SourceCode motivation, steps;
	private List<String> candidates;
	private KalnischkiesGrid<Integer> grid;
	private KalnischkiesGrid<String> votesList;
	private Graph digraph;
	private int[][] adjacency_winner, adjacency_widest;
	private int[] winner;

	enum COLORS {
		PAIRWINNER, PAIRLOSER, VOTEROW, VOTECOL
	}

	private Color[] colors;

	@Override
	public String getName() {
		return "Schulze Method: Condorcet voting";
	}

	@Override
	public String getAlgorithmName() {
		return "Schulze Method";
	}

	@Override
	public String getAnimationAuthor() {
		return "David Kalnischkies";
	}

	@Override
	public String getDescription() {
		return (MOTIVATION
				+ "\n\n\n\n"
				+ "As input it is expected that you enter a strict preference (no ties) in the table.\n"
				+ "The first column is how often the preference was voted for, all following are candidates.\n"
				+ "Note that you don't have to name all candidates in a vote:\n"
				+ "If one is missing it means that the candidate is ranked below all others.\n"
				+ "If multiple candidates are missing they are all ranked below the named ones,\n"
				+ "with no preferences between the unnamed candidates.\n\n"
				+ "Beside the default two additional examples are provided which can be enabled with\n" + "with the booleans in the configuration and replace the default animation.\n")
				.replace("\n\n", "<br /><br />");
	}

	@Override
	public String getCodeExample() {
		return "1. Calculate pairwise preferences\n"
				+ "2. Mark pairwise winner\n"
				+ "3. Digraph of pairwise winners\n"
				+ "4. Widest paths in digraph\n"
				+ "5. Mark (transitive) pairwise winner\n"
				+ "6. Interpretation\n";
	}

	@Override
	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public Locale getContentLocale() {
		return Locale.US;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	/**
	 * Decide who is the winner based on the given votes
	 * 
	 * Trivia: The method name is a play on the fig. "close, but no cigar"
	 * 
	 * @param voteList
	 *            the array to be sorted
	 */
	public void cigarWinner(Map<String, Integer> voteList) {
		this.candidates = getCandidateList(voteList);
		intro(voteList);
		pairwisePreferences(voteList);
		markPairwiseWinner();
		digraphPairwiseWinner();
		widestPathInDigraph();
		markWidestWinner();
		interpretation();
	}

	private void interpretation() {
		this.steps.toggleHighlight(5, 6);

		Text announcewinner = this.lang.newText(new Offset(0, 20, "pairwise",
				"SW"), "PLACEHOLDER Winner-Announcement", "announce-winner",
				null, this.plainProp);
		String ranking = getRanking(this.candidates, this.winner,
				announcewinner);

		SourceCode inter = this.lang.newSourceCode(new Offset(0, 10,
				announcewinner, "SW"), "interpretation", null,
				this.sourceCodeProp);
		inter.addCodeLine("The complete ranking is: " + ranking, null, 0, null);
		inter.addCodeLine(
				"(Note that the ranking doesn't need to be one of the votes given)",
				null, 0, null);
		inter.addCodeLine("", null, 0, null);
		inter.addCodeLine("Lookout: How to handle and/or resolve ties.", null,
				0, null);
		inter.addCodeLine("", null, 0, null);
		inter.addCodeLine("More details can be found at:", null, 0, null);
		inter.addCodeLine("https://en.wikipedia.org/wiki/Schulze_method", null,
				0, null);
		this.lang.nextStep();
	}

	private void markWidestWinner() {
		this.steps.toggleHighlight(4, 5);
		this.digraph.hide();

		SourceCode desc = this.lang.newSourceCode(new Offset(0, 20, "steps",
				"SW"), "desc_5", null, this.sourceCodeProp);
		addDescription(desc, WIDESTWINNER);

		for (int row = 0; row < this.candidates.size(); ++row) {
			for (int col = row + 1; col < this.candidates.size(); ++col) {
				int a = this.grid.getElement(row, col);
				int b = this.grid.getElement(col, row);
				if (a > b) {
					this.grid.setGridColor(row, col,
							this.colors[COLORS.PAIRWINNER.ordinal()]);
					this.grid.setGridColor(col, row,
							this.colors[COLORS.PAIRLOSER.ordinal()]);
					++this.winner[row];
				} else if (b > a) {
					this.grid.setGridColor(row, col,
							this.colors[COLORS.PAIRLOSER.ordinal()]);
					this.grid.setGridColor(col, row,
							this.colors[COLORS.PAIRWINNER.ordinal()]);
					++this.winner[col];
				} else {
					this.grid.setGridColor(row, col,
							this.colors[COLORS.PAIRWINNER.ordinal()]);
					this.grid.setGridColor(col, row,
							this.colors[COLORS.PAIRWINNER.ordinal()]);
					++this.winner[row];
					++this.winner[col];
				}
				this.grid.highlightCell(row, col);
				this.grid.highlightCell(col, row);
				this.lang.nextStep();
				this.grid.unhighlightCell(row, col);
				this.grid.unhighlightCell(col, row);
			}
		}
		this.lang.nextStep();
		desc.hide();
	}

	private void widestPathInDigraph() {
		SourceCode desc = this.lang.newSourceCode(new Offset(0, 120, "steps",
				"SW"), "desc_4", null, this.sourceCodeProp);
		addDescription(desc, WIDESTPATH);

		this.steps.toggleHighlight(3, 4);

		// reset grid to look fresh (beside headers of course) again
		this.gridtitle.setText("Weakest link of the strongest path:", null,
				null);
		this.grid.clear();

		// copy the winner adjacency matrix as init for widest path
		this.winner = new int[this.candidates.size()];
		this.adjacency_widest = new int[this.candidates.size()][this.candidates
				.size()];
		for (int row = 0; row < this.candidates.size(); ++row) {
			this.winner[row] = 1; // each candidate wins against itself
			for (int col = 0; col < this.candidates.size(); ++col)
				this.adjacency_widest[row][col] = this.adjacency_winner[row][col];
		}

		// the actual algorithm to determine the widest paths
		for (int row = 0; row < this.candidates.size(); ++row)
			for (int col = 0; col < this.candidates.size(); ++col)
				if (row != col)
					for (int k = 0; k < this.candidates.size(); ++k)
						if (row != k && col != k)
							this.adjacency_widest[col][k] = Math.max(
									this.adjacency_widest[col][k], Math.min(
											this.adjacency_widest[col][row],
											this.adjacency_widest[row][k]));

		// and now the calculation step by step for humans
		for (int row = 0; row < this.candidates.size(); ++row)
			for (int col = 0; col < this.candidates.size(); ++col) {
				if (row == col)
					continue;
				this.grid.highlightCell(row, col);
				this.grid.setGridValue(row, col,
						this.adjacency_widest[row][col]);
				// find (one of) the widest paths for display
				List<Integer> path = breadthFirstSearch(this.adjacency_winner,
						this.adjacency_widest[row][col], row, col);
				int start = -1;
				for (int end : path) {
					this.digraph.highlightNode(end, null, null);
					if (start != -1)
						this.digraph.highlightEdge(start, end, null, null);
					start = end;
				}
				this.lang.nextStep();
				this.grid.unhighlightCell(row, col);
				start = -1;
				for (int end : path) {
					this.digraph.unhighlightNode(end, null, null);
					if (start != -1)
						this.digraph.unhighlightEdge(start, end, null, null);
					start = end;
				}
			}
		desc.hide();
	}

	private void digraphPairwiseWinner() {
		/*
		 * The following code distributes our candidates as points along an
		 * (invisible) circle as it is best suited to show interconnections as
		 * all points will have an edge with most others (usually)
		 */
		SourceCode desc = this.lang.newSourceCode(new Offset(0, 120, "steps",
				"SW"), "desc_3", null, this.sourceCodeProp);
		addDescription(desc, DIGRAPHPAIRWISE);

		this.steps.toggleHighlight(2, 3);
		final double radius = 100;
		final double baserad = (2 * Math.PI) / this.candidates.size();

		// FIXME: Animal bug: NullPointerException
		// algoanim.primitives.Point center = lang.newPoint(new Coordinates(500,
		// 350),
		// "graph-center", null, null);
		// as a workaround, an empty/hidden text works just as well
		Text center = this.lang.newText(new Offset(120, 120, "pairwise", "SW"),
				"", "digraph-center", null);
		center.hide();

		List<Node> nodes = new ArrayList<Node>();
		for (int i = 0; i < this.candidates.size(); ++i) {
			double alpharad = baserad * i;
			nodes.add(getCircleOffset(center, alpharad, radius));
		}
		this.digraph = this.lang.newGraph("strongpath", this.adjacency_winner,
				nodes.toArray(new Node[0]),
				this.candidates.toArray(new String[0]), null, this.graphProp);
		this.lang.nextStep();
		desc.hide();
	}

	private void markPairwiseWinner() {
		// we prepare the adjacency matrix here, even if we don't have a graph
		// yet, so that we don't have to do same looping in the next step
		this.adjacency_winner = new int[this.candidates.size()][this.candidates
				.size()];
		for (int row = 0; row < this.candidates.size(); ++row)
			for (int col = 0; col < this.candidates.size(); ++col)
				this.adjacency_winner[row][col] = 0;

		this.steps.toggleHighlight(1, 2);
		SourceCode desc = this.lang.newSourceCode(new Offset(0, 20, "steps",
				"SW"), "desc_2", null, this.sourceCodeProp);
		addDescription(desc, PAIRWISEWINNER);

		for (int row = 0; row < this.candidates.size(); ++row) {
			for (int col = row + 1; col < this.candidates.size(); ++col) {
				int a = this.grid.getElement(row, col);
				int b = this.grid.getElement(col, row);

				if (a > b) {
					this.grid.setGridColor(row, col,
							this.colors[COLORS.PAIRWINNER.ordinal()]);
					this.grid.setGridColor(col, row,
							this.colors[COLORS.PAIRLOSER.ordinal()]);
					this.adjacency_winner[row][col] = a;
				} else if (b > a) {
					this.grid.setGridColor(row, col,
							this.colors[COLORS.PAIRLOSER.ordinal()]);
					this.grid.setGridColor(col, row,
							this.colors[COLORS.PAIRWINNER.ordinal()]);
					this.adjacency_winner[col][row] = b;
				} else {
					this.grid.setGridColor(row, col,
							this.colors[COLORS.PAIRWINNER.ordinal()]);
					this.grid.setGridColor(col, row,
							this.colors[COLORS.PAIRWINNER.ordinal()]);
					this.adjacency_winner[row][col] = a;
					this.adjacency_winner[col][row] = b;
				}
				this.grid.highlightCell(row, col);
				this.grid.highlightCell(col, row);
				this.lang.nextStep();
				this.grid.unhighlightCell(row, col);
				this.grid.unhighlightCell(col, row);
			}
		}
		this.lang.nextStep();
		desc.hide();
	}

	private void pairwisePreferences(Map<String, Integer> voteList) {
		int votecount = 0;
		for (Integer v : voteList.values())
			votecount += v;
		this.subtitle.setText("" + votecount + " votes:", null, null);
		this.steps.highlight(1);

		SourceCode desc = this.lang.newSourceCode(new Offset(0, 20, this.steps,
				"SW"), "desc_1", null, this.sourceCodeProp);
		addDescription(desc, PAIRWISEPREFERENCES);

		this.gridtitle = this.lang.newText(new Offset(150, -20, "votes", "NE"),
				"Pairwise preferences:", "gridtitle", null, this.plainProp);
		String[] header_row = new String[this.candidates.size()];
		String[] header_col = new String[this.candidates.size()];
		for (int i = 0; i < this.candidates.size(); ++i) {
			header_col[i] = "* > " + this.candidates.get(i);
			header_row[i] = this.candidates.get(i) + " > *";
		}
		int cellSize = 0;
		for (String s : header_row)
			if (cellSize < s.length())
				cellSize = s.length();
		for (String s : header_col)
			if (cellSize < s.length())
				cellSize = s.length();

		this.grid = new KalnischkiesGrid<Integer>(this.lang, "pairwise", new Offset(0,
				20, "gridtitle", "NW"), header_row, header_col, cellSize,
				GridStyle.TABLE, this.pairwiseProp);

		for (int row = 0; row < header_row.length; ++row) {
			for (int col = 0; col < header_col.length; ++col) {
				if (row == col)
					continue;
				this.grid.highlightCell(row, col);
				Integer wincount = 0;

				for (int v = 0; v < this.votesList.getNumberOfRows(); ++v) {
					this.votesList.highlightRow(v);

					int a = this.votesList.findColumn(v,
							this.candidates.get(row));
					int b = this.votesList.findColumn(v,
							this.candidates.get(col));

					if (a != -1)
						this.votesList.setGridColor(v, a,
								this.colors[COLORS.VOTEROW.ordinal()]);
					if (b != -1)
						this.votesList.setGridColor(v, b,
								this.colors[COLORS.VOTECOL.ordinal()]);
					if (a != -1 && a < b)
						wincount += voteList.get(this.votesList.getRow(v, "",
								" "));

					this.grid.setGridValue(row, col, wincount);
					this.lang.nextStep();

					if (a != -1)
						this.votesList.resetGridColor(v, a);
					if (b != -1)
						this.votesList.resetGridColor(v, b);
					this.votesList.unhighlightRow(v);
				}

				this.grid.unhighlightCell(row, col);
			}
		}
		desc.hide();
	}

	private void addDescription(SourceCode sc, String str) {
		for (String line : str
				.replace(
						"##PAIRWINNER##",
						ColorChooserComboBox
								.getStringForColor(this.colors[COLORS.PAIRWINNER
										.ordinal()]))
				.replace(
						"##PAIRLOSER##",
						ColorChooserComboBox
								.getStringForColor(this.colors[COLORS.PAIRLOSER
										.ordinal()]))
				.replace(
						"##VOTEROW##",
						ColorChooserComboBox
								.getStringForColor(this.colors[COLORS.VOTEROW
										.ordinal()]))
				.replace(
						"##VOTECOL##",
						ColorChooserComboBox
								.getStringForColor(this.colors[COLORS.VOTECOL
										.ordinal()])).split("\n")) {
			sc.addCodeLine(line, null, 0, null);
		}
	}

	private void intro(Map<String, Integer> voteList) {
		this.lang.newText(new Coordinates(15, 20), "Schulze method", "title",
				null, this.boldProp);
		this.subtitle = this.lang.newText(new Offset(0, 20, "title", "NW"),
				"Condorcet voting to make decisions in Debian and Gentoo",
				"subtitle", null, this.boldProp);
		this.motivation = this.lang.newSourceCode(new Offset(0, 40, "subtitle",
				"NW"), "motivation", null, this.sourceCodeProp);
		for (String line : MOTIVATION.split("\n")) {
			this.motivation.addCodeLine(line, null, 0, null);
		}
		this.lang.nextStep();

		this.motivation.hide();
		int votecount = 0;
		for (Integer v : voteList.values())
			votecount += v;
		this.subtitle.setText("In our example " + votecount
				+ " voters ranked the " + this.candidates.size()
				+ " candidates in the following patterns:", null, null);
		int row = 0;
		int columns = (this.candidates.size() * 2) - 1;
		String[][] matrix = new String[voteList.size()][columns];
		String[] header_row = new String[voteList.size()];
		for (Entry<String, Integer> vote : voteList.entrySet()) {
			header_row[row] = "" + vote.getValue() + "x";
			int col = 0;
			for (String cand : vote.getKey().split(" > ")) {
				cand = cand.trim();
				if (cand.isEmpty() == true)
					continue;
				if (col != 0) {
					matrix[row][col] = ">";
					++col;
				}
				matrix[row][col] = cand;
				++col;
			}
			++row;
		}
		int longest_cell = 0;
		for (row = 0; row < matrix.length; ++row)
			for (int col = 0; col < matrix[row].length; ++col)
				if (matrix[row][col] != null
						&& longest_cell < matrix[row][col].length())
					longest_cell = matrix[row][col].length();

		this.votesList = new KalnischkiesGrid<String>(this.lang, "votes", new Offset(
				0, 20, "subtitle", "NW"), header_row, columns, longest_cell,
				GridStyle.PLAIN, this.votesListProp);
		this.votesList.setMatrix(matrix);

		this.steps = this.lang.newSourceCode(new Offset(0, 20, "votes", "SW"),
				"steps", null, this.sourceCodeProp);
		this.steps.addCodeLine("Steps:", null, 0, null);
		for (String line : getCodeExample().split("\n")) {
			this.steps.addCodeLine(line, null, 0, null);
		}
		this.lang.nextStep();
	}

	static List<Integer> breadthFirstSearch(int[][] adjacency, int widest,
			int start, int end) {
		/*
		 * standard breath-first search with the slight twist that it will not
		 * travel on all edges but only on those which are wide enough so that
		 * we find (one of) the shortest-widest paths.
		 */
		Queue<Integer> queue = new LinkedList<Integer>();
		Map<Integer, Integer> prev = new HashMap<Integer, Integer>();
		queue.add(start);
		prev.put(start, start);
		while (queue.isEmpty() == false) {
			int current = queue.remove();
			if (current == end) {
				ArrayList<Integer> path = new ArrayList<Integer>();
				path.add(end);
				while (current != start) {
					Integer p = prev.get(current);
					path.add(p);
					current = p;
				}
				Collections.reverse(path);
				return path;
			}
			for (int i = 0; i < adjacency[current].length; ++i)
				if (i != current && prev.containsKey(i) == false
						&& adjacency[current][i] >= widest) {
					prev.put(i, current);
					queue.add(i);
				}
		}
		return null;
	}

	/**
	 * position in distance from center node on a circle
	 * 
	 * @param center
	 *            is the center of the circle
	 * @param alpharad
	 *            is the position on the circle to place on
	 * @param radius
	 *            of the circle
	 * @return position of the point on the circle
	 */
	static Offset getCircleOffset(Primitive center, double pointrad,
			double radius) {
		double chord = 2 * radius * Math.sin((pointrad / 2));
		// height of our new point above the base-edge
		double y = Math.sqrt(2
				* (Math.pow(chord, 2) * Math.pow(radius, 2)
						+ Math.pow(radius, 4) + Math.pow(radius, 2)
						* Math.pow(chord, 2))
				- (Math.pow(chord, 4) + 2 * Math.pow(radius, 4)))
				/ (2 * radius);
		// distance to the cross-point of base-edge and height
		double x = Math.sqrt(Math.pow(radius, 2) - Math.pow(y, 2));

		final String directions[] = { AnimalScript.DIRECTION_NE,
				AnimalScript.DIRECTION_SE, AnimalScript.DIRECTION_SW,
				AnimalScript.DIRECTION_NW };
		int direction = 0;
		double alpharad = pointrad;
		while (alpharad > (0.5 * Math.PI)) {
			++direction;
			alpharad -= (0.5 * Math.PI);
		}
		if (directions[direction] == AnimalScript.DIRECTION_NE
				|| directions[direction] == AnimalScript.DIRECTION_NW) {
			x *= -1;
		}
		if (directions[direction] == AnimalScript.DIRECTION_SW
				|| directions[direction] == AnimalScript.DIRECTION_NW) {
			y *= -1;
		}
		return new Offset((int) y, (int) x, center, AnimalScript.DIRECTION_NW);
	}

	static List<String> getCandidateList(Map<String, Integer> voteList) {
		List<String> candidates = new ArrayList<String>();
		for (String vote : voteList.keySet()) {
			for (String cand : vote.split(" > ")) {
				if (candidates.contains(cand.trim()))
					continue;
				candidates.add(cand.trim());
			}
		}
		Collections.sort(candidates);
		return candidates;
	}

	static String getRanking(List<String> candidates, int[] winner,
			Text announce) {
		String ranking = "";
		for (int w = candidates.size(); w > 0; --w) {
			ArrayList<String> tie = new ArrayList<String>();
			for (int i = 0; i < candidates.size(); ++i)
				if (winner[i] == w)
					tie.add(candidates.get(i));
			if (w == candidates.size()) {
				if (tie.size() == 1) {
					if (announce != null)
						announce.setText(
								tie.get(0)
										+ " wins over every other candidate as indicated by the green row!",
								null, null);
				} else {
					String cand = "";
					for (String c : tie) {
						if (cand.isEmpty() == false)
							cand += ", ";
						cand += c;
					}
					if (announce != null)
						announce.setText(
								"The voting results in a tie between the candidates: "
										+ cand, null, null);
				}
			} else if (tie.isEmpty() == true)
				continue;
			else
				ranking += " > ";
			String rank = "";
			for (String c : tie) {
				if (rank.isEmpty() == false)
					rank += " = ";
				rank += c;
			}
			ranking += rank;
		}
		return ranking;
	}

	static Map<String, Integer> getVoteList(String[][] votes) {
		Map<String, Integer> voteList = new HashMap<String, Integer>();
		for (String vote[] : votes) {
			String ranking = "";
			for (int i = 1; i < vote.length; ++i) {
				if (vote[i].isEmpty() == true)
					continue;
				if (ranking.isEmpty() == false)
					ranking += " > ";
				ranking += vote[i];
			}
			// be nice, ignore empty votes
			if (ranking.isEmpty() == true)
				continue;
			int count = 1;
			String number = null;
			// be nice, if its just one vote, do not require a number
			if (vote[0].isEmpty() == false)
				try {
					if (vote[0].indexOf('x') == -1)
						number = vote[0];
					else
						number = vote[0].substring(0, vote[0].indexOf('x'));
					count = Integer.parseInt(number);
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("Invalid number "
							+ number + " (" + vote[0] + ") for ranking "
							+ ranking + " provided!");
				}
			if (count == 0)
				continue;
			if (voteList.containsKey(ranking))
				voteList.put(ranking, voteList.get(ranking) + count);
			else
				voteList.put(ranking, count);
		}
		return voteList;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		boolean wikipedia = (Boolean) primitives.get("show-example-wikipedia");
		boolean tie = (Boolean) primitives.get("show-example-tie");
		if (wikipedia == true && wikipedia == tie)
			throw new IllegalArgumentException(
					"You can enable only one example at a time.");
		if (wikipedia != tie)
			return true;

		String[][] votes = (String[][]) primitives.get("votes");
		getVoteList(votes);
		return true;
	}

	public static void main(String[] args) {
		SchulzeMethod s = new SchulzeMethod();
		s.init();
		PropertiesTreeModel ptm = new PropertiesTreeModel();
		ptm.loadFromXMLFile(
				s.getClass().getName().replace('.', File.separatorChar)
						+ ".xml", true);
		AnimationPropertiesContainer container = ptm.getPropertiesContainer();
		Hashtable<String, Object> primitives = ptm.getPrimitivesContainer();
		System.out.println(s.generate(container, primitives));
	}
}
