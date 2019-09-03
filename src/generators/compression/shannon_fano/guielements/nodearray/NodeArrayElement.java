package generators.compression.shannon_fano.guielements.nodearray;

import generators.compression.shannon_fano.guielements.tree.AbstractNode;
import generators.compression.shannon_fano.style.ShannonFanoStyle;
import generators.compression.shannon_fano.utils.ProbabilityFormatter;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Primitive;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayProperties;
import algoanim.util.Offset;

/**
 * This class represents an element in the node array.
 * 
 * @author Sebastian Fach
 */
public class NodeArrayElement {

	protected Language lang;
	protected ShannonFanoStyle style;

	/**
	 * The corresponding node
	 */
	private AbstractNode node;

	/**
	 * The left neighbor of this element or the head column of the array if this
	 * element is the first one
	 */
	private Primitive leftNeighbor;

	/**
	 * Stores whether the element is currently visible or not
	 */
	protected boolean isVisible;

	/**
	 * The primitive for the element
	 */
	private StringArray stringArray;

	public int getID() {
		return node.getId();
	}

	public StringArray getStringArray() {
		return stringArray;
	}

	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * Constructor
	 * 
	 * @param node
	 *            The node is array element belongs to
	 * @param leftNeighbor
	 *            The left neighbor of this element or the head column of the
	 *            array if this element is the first one
	 * @param lang
	 *            The language
	 * @param style
	 *            The style
	 */
	public NodeArrayElement(AbstractNode node, Primitive leftNeighbor, Language lang, ShannonFanoStyle style) {
		this.node = node;
		this.leftNeighbor = leftNeighbor;
		this.lang = lang;
		this.style = style;
		createPrimitives();
		isVisible = true;
	}

	/**
	 * Creates primitives for the new element.
	 */
	private void createPrimitives() {
		Offset elementPosition = new Offset(0, 0, leftNeighbor, AnimalScript.DIRECTION_NE);
		String[] data = new String[3];
		data[0] = Integer.toString(node.getId());
		data[1] = Integer.toString(node.getFrequency());
		data[2] = ProbabilityFormatter.format(node.getProbability());
		stringArray = lang.newStringArray(elementPosition, data, "nodeArrayElement" + node.getId(), null,
				(ArrayProperties) style.getProperties(ShannonFanoStyle.ARRAY_REST));
	}

	/**
	 * Shows the element.
	 */
	public void show() {
		stringArray.show();
		isVisible = true;
	}

	/**
	 * Hides the element.
	 */
	public void hide() {
		stringArray.hide();
		isVisible = false;
	}

	/**
	 * Highlights the element.
	 */
	public void highlight() {
		stringArray.highlightCell(0, null, null);
		stringArray.highlightCell(1, null, null);
		stringArray.highlightCell(2, null, null);
	}

	/**
	 * Unhighlights the element.
	 */
	public void unhighlight() {
		stringArray.unhighlightCell(0, null, null);
		stringArray.unhighlightCell(1, null, null);
		stringArray.unhighlightCell(2, null, null);
	}

}
