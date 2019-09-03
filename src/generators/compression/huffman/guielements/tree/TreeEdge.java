package generators.compression.huffman.guielements.tree;

import java.awt.Color;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.util.Offset;
import algoanim.util.Timing;

public class TreeEdge {

	private Polyline edge;
	private Text edgeText;

	public TreeEdge(Polyline edge, Text edgeText) {

		this.edge = edge;
		this.edgeText = edgeText;
	}

	public void hide() {
		edge.hide();
		edgeText.hide();
	}

	public void show() {
		edge.show();
		edgeText.show();
	}

	public void highlight() {

		edge.changeColor("color", Color.RED, null, null);
		edgeText.changeColor("color", Color.RED, null, null);
	}

	public void unhighlight() {

		edge.changeColor("color", Color.BLACK, null, null);
		edgeText.changeColor("color", Color.BLACK, null, null);
	}

	public void moveTo(Circle lowerCircle, TreeNode lowerNode,
			TreeNode upperNode, Language lang, Timing duration) {

		if (upperNode.getxCoordinate() - lowerNode.getxCoordinate() > 0) {
			moveZeroEdgeTo(lowerCircle, lowerNode, upperNode, lang, duration);
		} else {
			moveOneEdgeTo(lowerCircle, lowerNode, upperNode, lang, duration);
		}
	}

	private void moveZeroEdgeTo(Circle lowerCircle, TreeNode lowerNode,
			TreeNode upperNode, Language lang, Timing duration) {

		int relativeY = (upperNode.getLayer() - lowerNode.getLayer())
				* Tree.Y_STEP_SIZE + 11;

		Circle alignDummy = lang.newCircle(new Offset(0, relativeY,
				lowerCircle, AnimalScript.DIRECTION_C), 15, "alignDummy", null);
		alignDummy.hide();
		
		edge.moveTo(null, null, new Offset(0, 0, alignDummy,
				AnimalScript.DIRECTION_C), null, duration);

		int relativeXzero = (upperNode.getxCoordinate() - lowerNode
				.getxCoordinate());
		int relativeYzero = (upperNode.getLayer() - lowerNode.getLayer())
				* Tree.Y_STEP_SIZE;

		edgeText.moveTo(null, null, new Offset((relativeXzero - 11) / 2 - 9,
				(relativeYzero - 15 - 11) / 2 -2, lowerCircle,
				AnimalScript.DIRECTION_C), null, duration);
	}

	private void moveOneEdgeTo(Circle lowerCircle, TreeNode lowerNode,
			TreeNode upperNode, Language lang, Timing duration) {

		int relativeY = (upperNode.getLayer() - lowerNode.getLayer())
				* Tree.Y_STEP_SIZE;
		int relativeX = upperNode.getxCoordinate() - lowerNode.getxCoordinate();
		
		Circle alignDummy = lang.newCircle(new Offset(relativeX, relativeY,
				lowerCircle, AnimalScript.DIRECTION_C), 15, "alignDummy", null);
		alignDummy.hide();
		
		edge.moveTo(null, null, new Offset(11, 11, alignDummy,
				AnimalScript.DIRECTION_C), null, duration);
		
		int relativeXOne = (upperNode.getxCoordinate() - lowerNode
				.getxCoordinate());
		int relativeYOne = (upperNode.getLayer() - lowerNode.getLayer())
				* Tree.Y_STEP_SIZE;
		
		edgeText.moveTo(null, null, new Offset((relativeXOne - 11) / 2 + 11,
				(relativeYOne - 15 - 11) / 2 - 3, lowerCircle,
				AnimalScript.DIRECTION_C), null, duration);
	}
}
