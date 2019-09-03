package generators.network.graph.anim;

import java.util.UUID;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.bbcode.Style;
import algoanim.primitives.Graph;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.MatrixProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 *
 * The class dislays an adjacency or distance matrix from a given graph.
 */
public class AdjacencyMatrixView {
	private Text hl;
	private IntMatrix m;
	private Language l;
	private Graph g;
	
	/**
	 * Create a new matrix with headline
	 * 
	 * @param lang The Language object to add the matrix to
	 * @param upperLeft The alignment position within the animation frame
	 * @param style The style to be used
	 * @param graph The graph to derive the matrix from
	 * @param headline The headline text
	 * @param headlineSize The headline size. There must be a corresponding style present in s.
	 */
	public AdjacencyMatrixView(Language lang, Node upperLeft, Style style, Graph graph, String headline, String headlineSize) {
		l = lang;
		g = graph;
		
		// create headline
		hl = l.newText(upperLeft, headline, UUID.randomUUID().toString(), null, (TextProperties) style.getProperties(headlineSize));
		
		// create new matrix
		m = l.newIntMatrix(new Offset(7, 7, hl.getName(), AnimalScript.DIRECTION_SW), g.getAdjacencyMatrix(), "matrixViewAdjacancyMatrix" + UUID.randomUUID().toString().replace("-", ""), null, (MatrixProperties) style.getProperties("matrix"));
	}
	
	/**
	 * Change an element in the matrix
	 * 
	 * @param rowId The row number of the element
	 * @param colId The column number of the element
	 * @param value The new value
	 * @param delay Animation delay
	 * @param duration Animation duration
	 */
	public void put(int rowId, int colId, int value, Timing delay, Timing duration) {
		m.put(rowId, colId, value, delay, duration); 
	}

	/**
	 * Change an element in the matrix
	 * 
	 * @param rowNode The graph node used for the row index
	 * @param colNode The graph node used for the column index
	 * @param value The new value
	 * @param delay Animation delay
	 * @param duration Animation duration
	 */
	public void put(Node rowNode, Node colNode, int value, Timing delay, Timing duration) {
		this.put(g.getPositionForNode(rowNode), g.getPositionForNode(colNode), value, delay, duration);
	}
	
	/**
	 * Highlight a cell in the matrix
	 * 
	 * @param rowId The row number of the cell to highlight
	 * @param colId The column number of the element
	 * @param offset Animation delay
	 * @param duration Animation duration
	 */
	public void highlightCell(int rowId, int colId, Timing offset, Timing duration) {
		m.highlightCell(rowId, colId, offset, duration);
	}
	
	/**
	 * Highlight a cell in the matrix
	 * 
	 * @param rowNode The graph node used for the row index
	 * @param colNode The graph node used for the column index
	 * @param offset Animation delay
	 * @param duration Animation duration
	 */
	public void highlightCell(Node rowNode, Node colNode, Timing offset, Timing duration) {
		this.highlightCell(g.getPositionForNode(rowNode), g.getPositionForNode(colNode), offset, duration);
	}

	/**
	 * Remove the highlight from a cell in the matrix
	 * 
	 * @param rowId The row number of the cell to highlight
	 * @param colId The column number of the element
	 * @param offset Animation delay
	 * @param duration Animation duration
	 */
	public void unhighlightCell(int rowId, int colId, Timing offset, Timing duration) {
		m.unhighlightCell(rowId, colId, offset, duration);
	}
	
	/**
	 * Remove the highlight from a cell in the matrix
	 * 
	 * @param rowNode The graph node used for the row index
	 * @param colNode The graph node used for the column index
	 * @param offset Animation delay
	 * @param duration Animation duration
	 */
	public void unhighlightCell(Node rowNode, Node colNode, Timing offset, Timing duration) {
		this.unhighlightCell(g.getPositionForNode(rowNode), g.getPositionForNode(colNode), offset, duration);
	}
	
	/**
	 * Show the matrix in the animation
	 */
	public void show() {
		this.show(null);
	}
	
	/**
	 * Show the matrix in the animation
	 * 
	 * @param timing The animation delay
	 */
	public void show(Timing timing) {
		hl.show(timing);
		m.show(timing);		
	}
	
	/**
	 * Hide the matrix in the animation
	 */
	public void hide() {
		this.hide(null);
	}
	
	/**
	 * Hide the matrix in the animation
	 * 
	 * @param timing The animation delay
	 */
	public void hide(Timing timing) {
		hl.hide(timing);
		m.hide(timing);		
	}
}
