package generators.compression.shannon_fano.guielements.nodearray;

import generators.compression.shannon_fano.guielements.tree.AbstractNode;
import generators.compression.shannon_fano.style.ShannonFanoStyle;

import java.util.ArrayList;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayProperties;
import algoanim.util.Offset;

public class NodeArray {

	private Language lang;
	private ShannonFanoStyle style;

	private StringArray nodeArrayHeadCol;
	private ArrayList<NodeArrayElement> elements = new ArrayList<NodeArrayElement>();
	private ArrayList<Primitive> alignmentElements = new ArrayList<Primitive>();

	public NodeArray(Language lang, Offset initialOffset, ShannonFanoStyle style) {

		this.lang = lang;
		this.style = style;

		// create head column
		nodeArrayHeadCol = lang.newStringArray(initialOffset, new String[] { "node no.", "frequency", "probability" },
				"nodeArrayCol0", null, (ArrayProperties) style.getProperties(ShannonFanoStyle.ARRAY_FIRST_COL));

		// create an alignment element for this column
		StringArray alignmentElement = lang.newStringArray(initialOffset, new String[] { "node no.", "frequency",
				"probability" }, "nodeArrayCol0AlignmentElement", null,
				(ArrayProperties) style.getProperties(ShannonFanoStyle.ARRAY_FIRST_COL));
		alignmentElement.hide();

		// insert alignment element
		alignmentElements.add(alignmentElement);
	}

	public int size() {
		return elements.size();
	}

	public NodeArrayElement getElement(int id) {
		return elements.get(id);
	}

	public StringArray getHeadElement() {
		return nodeArrayHeadCol;
	}

	/**
	 * Shows all elements.
	 */
	public void show() {
		nodeArrayHeadCol.show();
		for (NodeArrayElement element : elements) {
			element.show();
		}
	}

	/**
	 * Hides all elements.
	 */
	public void hide() {
		nodeArrayHeadCol.hide();
		for (NodeArrayElement element : elements) {
			element.hide();
		}
	}

	/**
	 * Shows the specified element.
	 * 
	 * @param id
	 *            The id of the corresponding node
	 */
	public void show(int id) {
		for (NodeArrayElement element : elements) {
			if (element.getID() == id) {
				element.show();
				break;
			}
		}
	}

	/**
	 * Hides the specified element.
	 * 
	 * @param id
	 *            The id of the corresponding node
	 */
	public void hide(int id) {
		for (NodeArrayElement element : elements) {
			if (element.getID() == id) {
				element.hide();
				break;
			}
		}
	}

	/**
	 * Layouts the node array elements.
	 */
	public void layout() {
		Offset nextPosition = new Offset(0, 0, nodeArrayHeadCol, AnimalScript.DIRECTION_NE);
		int count = 0;
		for (NodeArrayElement element : elements) {
			if (element.isVisible()) {
				element.getStringArray().moveTo(null, null, nextPosition, null, null);
				nextPosition = new Offset(0, 0, alignmentElements.get(count), AnimalScript.DIRECTION_NE);
				count++;
			}
		}
	}

	/**
	 * Highlights the specified array element.
	 * 
	 * @param id
	 *            The id of the corresponding node
	 */
	public void highlightElement(int id) {
		elements.get(id).highlight();
	}

	/**
	 * Unhighlights the specified array element.
	 * 
	 * @param id
	 *            The id of the corresponding node
	 */
	public void unhighlightElement(int id) {
		elements.get(id).unhighlight();
	}

	/**
	 * Inserts a new element into the node array.
	 */
	public void insertElement(AbstractNode node) {

		// create and insert element
		Primitive leftNeighbor;
		if (elements.size() == 0) {
			leftNeighbor = nodeArrayHeadCol;
		} else {
			leftNeighbor = elements.get(elements.size() - 1).getStringArray();
		}
		NodeArrayElement newElement = new NodeArrayElement(node, leftNeighbor, lang, style);
		elements.add(newElement);

		// create an alignment element for this position
		createAlignmentElement(newElement);
	}

	/**
	 * Creates an alignment element for this position.
	 * 
	 * @param originalElement
	 *            The element that is used to obtain the bounds from
	 */
	private void createAlignmentElement(NodeArrayElement originalElement) {
		Offset upperLeft = new Offset(0, 0, originalElement.getStringArray(), AnimalScript.DIRECTION_NW);
		Offset lowerRight = new Offset(0, 0, originalElement.getStringArray(), AnimalScript.DIRECTION_SE);
		Rect alignmentElement = lang.newRect(upperLeft, lowerRight, "nodeArrayElement" + originalElement.getID()
				+ "AlignRect", null);

		alignmentElement.hide();

		// insert alignment element
		alignmentElements.add(alignmentElement);
	}
}
