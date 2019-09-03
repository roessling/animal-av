package generators.tree;

import java.awt.Font;
import java.util.Vector;

import algoanim.primitives.Circle;
import algoanim.primitives.Primitive;
import algoanim.primitives.Polyline;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

/*
 * a recursive defined Binary Tree, whereas an instance of the class can either be a leaf node with
 * a value of type t or a parent with two BinaryTreeNodes as children.
 * 
 * @author David Steiner
 * @version 1.0
 */
public class KDTreeNode<T, Z> {
	
	public static final int AXIS_X = 0;
	public static final int AXIS_Y = 1; 
	public static final int AXIS_Z = 2; 

	
	KDTreeNode<T, Z> leftSubTree;
	KDTreeNode<T, Z> rightSubTree;
	boolean isLeaf;
	T leaf_value;
	float sep_value;
	int axis;
	
	int subnode = 0; // root = 0; left = 1; right = 2;
	Coordinates upperLeft;
	int width;
	
	//drawing
	private Language lang;
	private Circle circle;
	private Circle highlightCircle;
	private Polyline connectingLine;
	private Text text;
	private int radius;
	private int nCounter;
	
	/**
	 * empty constructor
	 */
	public KDTreeNode(){
		isLeaf = true;
		leftSubTree = null;
		rightSubTree = null;
		axis = 0;
	}

	/**
	 * constructor of a node node which will be displayed
	 * @param upperLeft the upperleft edge for the tree area 
	 * @param width the maximum display width for the subtree
	 */
	public KDTreeNode(Coordinates upperLeft, int width, int subnode){
		this.upperLeft = upperLeft;
		this.width = width;
		this.subnode = subnode;
		leftSubTree = null;
		rightSubTree = null;
		axis = 0;
	}
	
	/*
	 * constructor of a leaf node
	 */
	public KDTreeNode(T leaf){
		leaf_value = leaf;
		isLeaf = true;
	}
	
	/**
	 * draws the node
	 * @param lang
	 * @param radius
	 * @param properties
	 * @param nCounter
	 */
	public void drawNode (Language lang, int radius, CircleProperties circleProperties, TextProperties textProperties, int nCounter) {
		
		this.lang = lang;
		this.radius = radius;
		this.nCounter = nCounter;
		
		Coordinates nodeCenter = new Coordinates(upperLeft.getX() + width/2, upperLeft.getY()+radius);
		
		//draw connecting line
			if (subnode != 0) {
				Node parentNode = new Coordinates(upperLeft.getX()+width, upperLeft.getY()-3*radius); //left
				if (subnode == 2) {
					parentNode = new Coordinates(upperLeft.getX(), upperLeft.getY()-3*radius); //right
				}
				Node [] vertices = {nodeCenter, parentNode};
				PolylineProperties polylineProperties = new PolylineProperties();
				polylineProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
				connectingLine = lang.newPolyline(vertices, "nodeLine" + nCounter, null, polylineProperties);
			}

		//draw circle
			circle = lang.newCircle(nodeCenter, radius, "nodeCircle" + nCounter, null, circleProperties);
			
		//draw text
			int offset = 0;
			if (radius < 5) {
				textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 7));
				offset = 9/2;
			} else if (radius < 7.5) {
				textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 9));
				offset = 12/2;
			} else if (radius < 10) {
				textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 11));
				offset = 15/2;
			} else {// if (radius < 12.5) {
				textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 9));
				offset = 12/2;
			}
			
			String str = "";
			if (isLeaf) {
				int[] leaf_value = (int []) this.leaf_value;
				str = "{" + leaf_value[0] + "," + leaf_value[1] + "}";
			}
			else {
				
				if (axis == AXIS_X) str = "x = " + String.valueOf(sep_value);
				if (axis == AXIS_Y) str = "y = " + String.valueOf(sep_value);
			}
			text = lang.newText(new Coordinates(nodeCenter.getX(), nodeCenter.getY()-offset), str, "nodeText"+nCounter, null, textProperties);
	}
	
	/**
	 * highlights the node
	 * warning: one possible if node is already drawed!
	 */
	public void highlightNode(CircleProperties highlightProperties) {
		if (lang != null) {
			highlightCircle = lang.newCircle(circle.getCenter(), radius, "nodeHighlight" + nCounter, null, highlightProperties);
		}
	}
	
	public void unhighlightNode() {
		highlightCircle.hide();
	}
	
	public void setAsLeft() {
		subnode = 1;
	}
	
	public void setAsRight() {
		subnode = 2;
	}
	
	public void setAsLeaf(T leaf){
		isLeaf = true;
		this.leaf_value = leaf;
	}
	
	public void setAsSeparator(float s_value){
		isLeaf = false;
		sep_value = s_value;
	}
	
	public void setLeftNode(KDTreeNode<T, Z> left){
		leftSubTree = left;
		left.setAsLeft();
		isLeaf = false;
	}
	
	public void setRightNode(KDTreeNode<T, Z> right){
		rightSubTree = right;
		right.setAsRight();
		isLeaf = false;
	}
	
	public void setSeparatorAxis(int a){
		axis = a;
	}
	
	/*
	 * @return the leaf value, or null if the current node is not a leaf
	 */
	public T getLeaf(){
		if(isLeaf){
			return leaf_value;
		}
		return null;
	}
	
	/*
	 * @return the separator value, or null if the current node is a leaf
	 */
	public float getSeparatorValue(){
		if(!isLeaf){
			return sep_value;
		}
		return 0;
	}
	
	/*
	 * @return the left TreeNode or null if the current node is a leaf
	 */
	public KDTreeNode<T, Z> getLeft(){
		if(isLeaf){
			return null;
		} else return leftSubTree;
	}

	/*
	 * @return the right TreeNode or null if the current node is a leaf
	 */
	public KDTreeNode<T, Z> getRight(){
		if(isLeaf){
			return null;
		} else return rightSubTree;
	}
	
	public Vector<Primitive> getPrimitives() {
		Vector<Primitive> primitives = new Vector<Primitive>();
		primitives.add(circle);
		primitives.add(text);
		if(subnode != 0) primitives.add(connectingLine);
		return primitives;
	}
	
	
	/*
	 * return true, if this node is a leaf node
	 */
	public boolean isLeaf(){
		return isLeaf;
	}
	
	
	public int getSeparatorAxis(){
		return axis;
	}
	
	public Coordinates getUpperLeft() {
		return upperLeft;
	}
	
	public int getWidth() {
		return width;
	}
	
	public String toString(boolean switch_symb){
		switch_symb = !switch_symb;
		if(isLeaf){
			if(leaf_value == null){
				return "emtpy";
			} else return leafToString();

		}
		StringBuilder string_out = new StringBuilder("");

		if(switch_symb){
			string_out.append("(");
		} else {
			string_out.append("[");
		}
		string_out.append(leftSubTree.toString(switch_symb));
		string_out.append(" /").append(dimToString()).append(":").append(sep_value).append("\\ ");
		
		string_out.append(rightSubTree.toString(switch_symb));
		if(switch_symb){
			string_out.append(")");
		} else {
			string_out.append("]");
		}
			
		return string_out.toString();
		
	}

	public String toString(Double[] point){
		String string_out = new String(point[0] + "," + point[1]);
		return string_out;
	}
	
	public String toString(int[] point){
		String string_out = new String(point[0] + "," + point[1]);
		return string_out;
	}
	
	public String leafToString(){
		if(leaf_value instanceof Double[]){
			return toString((Double[]) leaf_value);
		} else if(leaf_value instanceof int[]){
			return toString((int[]) leaf_value);
		} else {
			return leaf_value.toString();
		}
	}
	
	
	
	public String dimToString(){
		switch(axis){
		case AXIS_X: return "x";
		case AXIS_Y: return "y";
		case AXIS_Z: return "z";
		}
		return "?";
	}
	
}

