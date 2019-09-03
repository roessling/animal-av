package generators.tree.binarySearchTree.binaryTree;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Random;

import algoanim.animalscript.AnimalCircleGenerator;
import algoanim.animalscript.AnimalGroupGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.primitives.Circle;
import algoanim.primitives.Group;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;
import algoanim.util.Timing;

public class Knoten {
	private Knoten left, right;
	private Group node;
	int key;
	private Polyline leftLine, rightLine;
	private Coordinates currentCoords;
	
	public Knoten(Language lang, Coordinates coords, int key, int radius, CircleProperties circleProps, TextProperties labelProps){
		this.key = key;
		this.currentCoords = coords;
		Random r = new Random();
		int count = r.nextInt();
		LinkedList<Primitive> groupList = new LinkedList<Primitive>();
		String name = "Circle" + count;
		Circle circle = new Circle(new AnimalCircleGenerator(lang), coords, radius, name, null, circleProps);
		name = "Text" + count;
		Offset offset = new Offset(0, -8, circle, AnimalScript.DIRECTION_C);
		labelProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
		Text text = new Text(new AnimalTextGenerator(lang), offset, Integer.toString(key), name, null, labelProps);
		groupList.add(circle);
		groupList.add(text);
		name = "Node"+count;
		this.node = new Group(new AnimalGroupGenerator(lang), groupList, name);
	}

	/**
	 * Setzt den rechten Kindknoten des aktuellen Knotens.
	 * @param child Der neue Kindknoten.
	 * @param line Eine {@see  algoanim.primitives.Polyline} zwischen dem aktuellen Knoten und dem neuen Kindknoten.
	 */
	public void setRight(Knoten child, Polyline line){
		this.right = child;
		this.rightLine = line;
	}
	
	/**
	 * Setzt den linken Kindknoten des aktuellen Knotens.
	 * @param child Der neue Kindknoten.
	 * @param line Eine {@see  algoanim.primitives.Polyline} zwischen dem aktuellen Knoten und dem neuen Kindknoten.
	 */
	public void setLeft(Knoten child, Polyline line){
		this.left = child;
		this.leftLine = line;
	}
	
	
	public Knoten getLeft(){
		return left;
	}
	
	public Knoten getRight(){
		return right;
	}
	
	public Polyline getLeftLine(){
		return this.leftLine;
	}
	
	public Polyline getRightLine(){
		return this.rightLine;
	}

	public int getKey() {
		return this.key;
	}
	
	public Coordinates getCoordinates(){
		return this.currentCoords;
	}
	
	/**
	 * Verändert die Füllfarbe des Knotens.
	 * @param color Die neue Farbe.
	 */
	public void changeColor(Color color){
		this.getCircle().changeColor(AnimalScript.COLORCHANGE_FILLCOLOR, color, null, null);
	}
	
	
	/**
	 * Versteckt den Knoten.
	 */
	public void hide(){
		this.node.hide();
	}
	
	/**
	 * Versteckt den Knoten mit Verzögerung.
	 * @param delay Die Verzögerung.
	 */
	public void hide(Timing delay){
		this.node.hide(delay);
	}
	
	/**
	 * Zeigt den Knoten.
	 */
	public void show(){
		this.node.show();
	}
	
	private Circle getCircle(){
		Primitive circ = this.node.getPrimitives().get(0);
		if(circ instanceof Circle){
			return (Circle) circ;
		}
		else
			return (Circle)this.node.getPrimitives().get(1);
	}
	
	public int getRadius(){
		return this.getCircle().getRadius();
	}
	
	/**
	 * Verschiebt den Knoten zu den angegeben Koordinaten. 
	 * @param coords Die neuen Koordinaten des Knotens.
	 */
	public void move(Coordinates coords){
		int radius = this.getRadius();
		Coordinates myCoords = this.getCoordinates();
		if(!(myCoords.getX() == coords.getX() && myCoords.getY() == coords.getY())){
			Coordinates corner = new Coordinates(coords.getX() - radius, coords.getY() - radius);
			node.moveTo(AnimalScript.DIRECTION_C, "translate", corner, new MsTiming(200), new MsTiming(300));
			this.currentCoords = coords;
		}
	}
}
