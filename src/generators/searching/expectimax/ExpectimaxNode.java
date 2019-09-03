package generators.searching.expectimax;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Text;
import algoanim.util.Coordinates;

public class ExpectimaxNode {	
	private NodeType type;
	private double value; // TODO Init value
	private Point position;
	private ArrayList<ExpectimaxNode> children;
	private ArrayList<Double> chances;
	private Primitive primitive;
	private Text valueText;
	
	private ArrayList<Polyline> lines;
	
	public Polyline getLine(int index) {
		if (index >= 0 && index < lines.size()) {
			return lines.get(index);
		} else {
			return null;
		}
	}

	public void addLine(Polyline line) {
		if (line != null) {
			lines.add(line);
		}
	}
	
	public Text getValueText() {
		return valueText;
	}

	public void setValueText(Text valueText) {
		this.valueText = valueText;
	}

	public ExpectimaxNode(NodeType t) {
		type = t;
		children = new ArrayList<ExpectimaxNode>();
		chances = new ArrayList<Double>();
		position = new Point(0,0);
		lines = new ArrayList<Polyline>();
	}
	
	public ExpectimaxNode(double value) {
		type = NodeType.LEAF;
		this.value = value;
		position = new Point(0,0);
	}
	
	public NodeType getType() {
		return type;
	}
	
	public Double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
	public void setPosition(int x, int y) {
		position.x = x;
		position.y = y;
	}
	
	public Coordinates getPosition() {
		return new Coordinates(position.x, position.y);
	}
	
	public void addChild(ExpectimaxNode node) {
		children.add(node);
	}
	public void addChildWithChance(Double chance, ExpectimaxNode node) {
		if (type == NodeType.CHANCE) {
			chances.add(chance);
			children.add(node);
		}
	}
	
	public ArrayList<ExpectimaxNode> getChildren() {
		return children;
	}
	public ExpectimaxNode getChild(int id) {
		if (id >= 0 && id < children.size()) {
			return children.get(id);
		} else {
			return null;
		}
	}
	public Double getChance(int id) {
		if (id >= 0 && id < chances.size()) {
			return chances.get(id);
		} else {
			return 0.0;
		}
	}
	public int childCount() {
		return children.size();
	}
	
	public void setPrimitive(Primitive primitive) {
		this.primitive = primitive;
	}
	
	public void changeFillColor(Color color) {
		if (primitive != null) {
			primitive.changeColor("fillColor", color, null, null);
		}
	}
	
	public void changeColor(Color color) {
		if (primitive != null) {
			primitive.changeColor("color", color, null, null);
		}
	}
	
	public void changeValueTextColor(Color color) {
		if (valueText != null) {
			valueText.changeColor("color", color, null, null);
		}
	}
	
	public int getChildIndex(ExpectimaxNode child) {
		for (int i = 0; i < children.size(); i++) {
			if (child == children.get(i)) {
				return i;
			}
		}
		return -1;
	}
}
