package generators.tree.rbtree_helper;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Offset;

/**
 * Represents a node object with an inner text (circleText; "key|value" of node)
 * and a text below the circle (circleSubText; variable name of node in source
 * code)
 *
 */
public class DisplayCircle {

	private Circle circle;
	private Offset offset;

	private SourceCode circleText;
	private String circleTextString;
	private SourceCode circleSubText;
	private String circleSubTextString;
	private Language lang;
	private final int RADIUS = 30;

	private SourceCodeProperties nodeTextProps;

	public DisplayCircle(Language l, CircleProperties circleProperties,
			Offset offset, String circleText, String circleSubText) {
		this.lang = l;

		this.nodeTextProps = new SourceCodeProperties();
		this.nodeTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 14));
		this.nodeTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Color.BLACK);

		this.setupCircle(circleText, circleSubText, offset, circleProperties);
	}

	/**
	 * Exchanges the position of the current node with the position of the given
	 * node otherCircle
	 * 
	 * @param otherCircle
	 *            The node which interchanges the position with the current node
	 */
	public void exchange(DisplayCircle otherCircle) {

		this.hide();
		otherCircle.hide();

		// Save position of other node
		Offset tempOffset = otherCircle.getOffset();

		// Modify circle
		otherCircle.setupCircle(otherCircle.getCircleText(),
				generateSubText(otherCircle.getCircleSubTextString()),
				this.offset, otherCircle.getCircle().getProperties());

		// Modify this
		this.setupCircle(this.getCircleText(),
				generateSubText(this.getCircleSubTextString()), tempOffset,
				this.getCircle().getProperties());
	}

	/**
	 * Generates the new subtext depending on the current position <br>
	 * - y.p.left -> y.p.right <br>
	 * - y.p.right -> y.p.left <br>
	 * - else: -> no change, return current subtext
	 * 
	 * @param subText
	 *            Text of the old position
	 * @return Text of the new position
	 */
	private String generateSubText(String subText) {
		String tempSubText = "";
		if (subText.equals("y.p.left")) {
			tempSubText = "y.p.right";
		} else if (subText.equals("y.p.right")) {
			tempSubText = "y.p.left";
		} else {
			tempSubText = subText;
		}
		return tempSubText;
	}

	public Circle getCircle() {
		return circle;
	}

	public String getCircleText() {
		return this.circleTextString;
	}

	public String getCircleSubTextString() {
		return this.circleSubTextString;
	}

	public Language getLanguageObject() {
		return this.lang;
	}

	public Offset getOffset() {
		return this.offset;
	}

	/**
	 * Generates a new circle (node) with the given parameters
	 * 
	 * @param circleText
	 *            The text which should be shown in the circle
	 * @param circleSubText
	 *            The text which should be shown below the circle
	 * @param offset
	 *            The position of the circle as an Offset
	 * @param circleProperties
	 *            The properties (color, font style, etc.) of the circle
	 */
	protected void setupCircle(String circleText, String circleSubText,
			Offset offset, CircleProperties circleProperties) {
		this.offset = offset;
		this.circle = this.lang.newCircle(offset, RADIUS, "null", null,
				circleProperties);
		setCircleText(circleText);
		this.circleSubText = lang.newSourceCode(new Offset(-2
				- (3 * circleSubText.length()), -15, this.circle,
				AnimalScript.DIRECTION_S), "null", null, nodeTextProps);
		this.circleSubTextString = circleSubText;
		this.circleSubText.addCodeLine(circleSubText, "circleSubText", 0, null);
	}

	/**
	 * Sets a new circleText to the node
	 * 
	 * @param circleText
	 *            The new text to set to the node
	 */
	public void setCircleText(String circleText) {
		if (this.circleText != null) {
			this.circleText.hide();
		}
		this.circleText = lang.newSourceCode(
				new Offset(-2 - (3 * circleText.length()), -55, this.circle,
						AnimalScript.DIRECTION_S), "null", null, nodeTextProps);
		this.circleText.addCodeLine(circleText, null, 0, null);
		this.circleTextString = circleText;
	}

	/**
	 * Hides the whole circle - including circleText and circleSubText
	 */
	public void hide() {
		this.circle.hide();
		this.circleSubText.hide();
		this.circleText.hide();
	}

	/**
	 * Checks the red-black tree properties(color) and changes the color if
	 * necessary
	 * 
	 * @param node
	 *            node to be checked
	 */
	public void rbColorFixup(Node node) {
		if (node.isRed()) {
			this.circle.changeColor("fillColor", Color.RED, null, null);
		} else {
			this.circle.changeColor("fillColor", Color.GRAY, null, null);
		}
	}

	/**
	 * Sets a new circleSubtext to the node
	 * 
	 * @param newSubtext
	 *            The new text to set to the node
	 */
	public void setCircleSubtext(String newSubtext) {
		if (this.circleSubText != null) {
			this.circleSubText.hide();
		}
		this.circleSubText = lang.newSourceCode(
				new Offset(-2 - (3 * newSubtext.length()), -15, this.circle,
						AnimalScript.DIRECTION_S), "null", null, nodeTextProps);
		this.circleSubText.addCodeLine(newSubtext, null, 0, null);
		this.circleSubTextString = newSubtext;
	}

}
