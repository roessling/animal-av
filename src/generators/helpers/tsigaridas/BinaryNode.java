package generators.helpers.tsigaridas;
import generators.tree.EffectiveBinaryTree;

import java.awt.Color;
import java.awt.Font;

import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
public class BinaryNode {
	
	private   int   contentsNode; 
 	private   BinaryNode  leftNode ; 
 	private  BinaryNode rightNode;
 	private BinaryNode father;
 	private EffectiveBinaryTree.TreeDirection incomingDirection;
 	
 	private Circle nodeCircle;
 	private Text nodeText;
 	public Polyline leftEdge, rightEdge;
 	private int x, y, width;
 	
 	PolylineProperties polyProps = new PolylineProperties();
 	
 	public enum TreeDirection {
		LEFT, RIGHT}
 
 	
 	public   BinaryNode (int key)   { 
 		 contentsNode =   key ;   
 	}
 	
 	 /**
	  * This method sets the key for the node
	  * @param key is the key for the node
	  */
	 public void setKey (int key)   { 
		 contentsNode =   key ; 
	 } 
	 
	 /**
 	  * This method sets the given node on the left side
 	  * @param key is the node for the left side
 	  */
 	 public void setLeft(BinaryNode key){ 
  	 	 leftNode =   key ; 
  	 } 
 	 
	 /**
	  * This method sets the given node on the right side
	  * @param key is the node for the right side
	  */	 
 	 public void setRight(BinaryNode key)  { 
	 	 rightNode =   key ; 
	 } 
 	 
 	 /**  
 	  * @return the key. 
 	  */
 	 public int getKey () { 
  	 	 return contentsNode ; 
 	 } 
 	
 	 /** 
 	  * @return the left node.
 	  */
 	 public BinaryNode getLeft() { 
 		 return   leftNode; 
 	 } 
 
 	 /** 
 	  * @return the right node.
 	  */
 	 public BinaryNode getRight()  { 
 		 return rightNode; 
 	 } 
 	 
 	 public void setIncomingDirection (EffectiveBinaryTree.TreeDirection dir) {
 		 incomingDirection = dir;
 	 }
 	 
 	 public EffectiveBinaryTree.TreeDirection getIncomingDirection (){
 		 return incomingDirection;
 	 }
 	 
 	 public Circle createCircle(Language lang, int key, int newX, int newY,int newWidth, CircleProperties circ) {
// 		lang.nextStep("created " +nodeText.getText());
 		lang.nextStep(); // lang.nextStep("...");
 		
 		
// 		 circ = new CircleProperties();
//		circ.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
//		circ.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 16));
		textProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
	
// 		Text info = lang.newText(new Coordinates(100, 40),
//				"insert("+Integer.toString(key)+")","insert() Text", null,textProps);
		
		if (nodeCircle == null) {
			// nichts gezeichnet - neu machen!
			x = newX;
			y = newY;
			width = newWidth;
			Coordinates coorCircle = new Coordinates(newX, newY);
	
			// Erzeuge einen Kreis!!!
			nodeCircle = lang.newCircle(coorCircle, 20, Integer
					.toString(getKey()), null, circ);
		}
	
		// Option 2: es gibt noch keinen Text, also anlegen
		if (nodeText == null) {
			Coordinates coorNumber = new Coordinates(newX, newY - 10);
	
			// Erzeuge den Text zu den Kreis!!!
			nodeText = lang.newText(coorNumber, Integer.toString(getKey()),
					Integer.toString(getKey()), null, textProps);
		}
		
//		info.hide();
 		return nodeCircle;
 	 }
 		
 	/**
 	 * Animal Language
 	 * @return nodeCircle
 	 */
 	public Circle getNodeCircle() {
 		return nodeCircle;
 	}
 	
 	/**
 	 * setLeftEdge gives the current Node a left Edge
 	 * @param l Animal
 	 * @param leftNode to get his Coordinate 
 	 * @param leftChild Circle 
 	 * @param x Coordinate from father 
 	 * @param y Coordinate from father
 	 * @return Polyline leftEdge 
 	 */
 	public Polyline setLeftEdge (Language l, BinaryNode leftNode ,Circle leftChild, int x, int y){
 		Node[] poly = new Node[2];
 		//To get the left point on the circle with 45 degrees from middle point of the circle
 		//one calculate: xCoordinate: x-(radius / rootOf(2)), yCoordinate: y+(radius / rootOf(2)).
 		poly[0] = new Coordinates(x-14, y + 14);
// 		poly[0] = new Coordinates(x, y + 20);
 		poly[1] = new Coordinates(leftNode.getXcoordinate(), leftNode.getYcoordinate()-20);
		polyProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		leftEdge = l.newPolyline(poly, "leftEdge", null, polyProps);
		return leftEdge;
 	}
 	/**
 	 * setRightEdge gives the current Node a right Edge
 	 * @param l Animal
 	 * @param rightNode to get his Coordinate
 	 * @param rightChild Circle
 	 * @param x Coordinate from father
 	 * @param y Coordinate from father
 	 * @return Polyline rightEdge
 	 */
 	public Polyline setRightEdge (Language l, BinaryNode rightNode ,Circle rightChild, int x, int y){
 		Node[] poly = new Node[2];
 		//To get the right point on the circle with 45 degrees from middle point of the circle
 		//one calculate: xCoordinate: x+(radius / rootOf(2)), yCoordinate: y+(radius / rootOf(2)).
 		poly[0] = new Coordinates(x+14, y + 14);
// 		poly[0] = new Coordinates(x, y + 20);
 		poly[1] = new Coordinates(rightNode.getXcoordinate(), rightNode.getYcoordinate() - 20);
		polyProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		rightEdge = l.newPolyline(poly, "rightEdge", null, polyProps);
 		return rightEdge;
 	}
 	
 	/**
 	 * Method sets for a given node his father
 	 * @param fatherNode
 	 */
 	public void setFather (BinaryNode fatherNode) {
 		father = fatherNode;
 	}
 	/**
 	 * Method returns the father node of a given node
 	 * @return father node
 	 */
 	public BinaryNode getFather () {
 		return father;
 	}
 	
 	/**
 	 * Animal Language
 	 * @return leftEdge
 	 */
 	public Polyline getLeftEdge () {
		return leftEdge;
	}
 	
 	/**
 	 * Animal Language 
 	 * @return rightEdge
 	 */
	public Polyline getRightEdge () {
		return rightEdge;
	}
	/**
	 * Animal Language
	 * @return nodeText
	 */
	public Text getNodeText() {
		return nodeText;
	}
	
	/**
	 * Give a node the x Coordinate
	 * @return x
	 */
	public int getXcoordinate() {
		return x;
	}
	/**
	 * Give a node the y Coordinate
	 * @return y
	 */
	public int getYcoordinate() {
		return y;
	}
	/**
	 * Sets for a node a new x value
	 * @param xCoor
	 */
	public void setX (int xCoor) {
		x = xCoor;
	}
	/**
	 * sets for a node a new y value
	 * @param yCoor
	 */
	public void setY (int yCoor) {
		y = yCoor;
	}
	
	/**
	 * Sets for a node the new width
	 * @param w
	 */
	public void setWidth(int w) {
		width = w;
	}
	/**
	 * Give the the distance/Width of a node 
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

 	 
 	/**
 	  * Check up if a node is a leaf.
 	  * @return true if a node is a Leaf otherwise false.
 	  */
 	 public boolean isLeaf() { 
 
 	 	 if   ( leftNode   ==   null   &&   rightNode  ==   null ) { 
 	 	 	 return  true ; 
 	 	 } 
 	 	 else return  false ; 
 	 }
 	 
 	

	public String toString() {
		Coordinates x = (Coordinates) nodeCircle.getCenter();
		return "BinNode: " + contentsNode + "@(" + x.getX() + ", " + x.getY()
				+ ")";
	}
}
