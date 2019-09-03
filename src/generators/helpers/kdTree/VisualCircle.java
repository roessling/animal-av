package generators.helpers.kdTree;

import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;

import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Circle;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

/**
 * @author mateusz
 */
public class VisualCircle {

    Circle circle;
    Language lang;
    AnimationPropertiesContainer animProps;
    CircleProperties circleProps;
    Node[] nodes;
    Color color;
    int depth;

    public VisualCircle(Coordinates rootCoordinates, Node[] nodes,
	    Language lang, AnimationPropertiesContainer animProps, Color color,
	    int depth) {
	super();
	this.lang = lang;
	this.animProps = animProps;
	this.nodes = nodes;
	this.circleProps = new CircleProperties();
	this.color = color;
	this.depth = depth;
	configure();
	this.circle = lang.newCircle(rootCoordinates, 27, "circle", null,
		this.circleProps);
    }

    public void moveToNode(Coordinates coordinate, boolean delayed) {
	Timing nodeHighlightDelay = delayed ? new TicksTiming(50) : null;
	Coordinates node = coordinate;
	int x = node.getX() - 15;
	int y = node.getY() - 20;
	try {
	    this.circle.moveTo("C", "translate", new Coordinates(x, y),
		    nodeHighlightDelay, nodeHighlightDelay);
	} catch (IllegalDirectionException e) {
	    e.printStackTrace();
	}
    }

    private void configure() {
	this.circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	this.circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, this.color);
	this.circleProps
		.set(AnimationPropertiesKeys.DEPTH_PROPERTY, this.depth);

    }

    public void hide() {
	this.circle.hide();
    }

    public void show() {
	this.circle.show();
    }

}
