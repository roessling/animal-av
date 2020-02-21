/*
 * Node.java
 * Hanna Holtdirk, Robin Satter, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.searching.sss;

import java.util.ArrayList;
import java.lang.StringBuilder;

public class Node {
    enum Type {
        MIN_NODE, MAX_NODE, LEAF
    }
    private final String name;
    private final Type type;
    private final int value;
    private final Node parent;
    private ArrayList<Node> children;
    private int index;

    /*
       Constructor for terminal nodes.
     */
    public Node(String name, int value, Node parent){
        this.name = name;
        this.type = Type.LEAF;
        this.value = value;
        this.children = null;
        this.parent = parent;
    }

    /*
        Constructor for max and min nodes.
     */
    public Node(String name, Type type, Node parent){
        this.name = name;
        this.type = type;
        this.value = -1;
        this.children = new ArrayList<Node>();
        this.parent = parent;
    }

    public String getName() { return name; }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public Type getType(){
        return type;
    }

    public Node getParent(){
        return parent;
    }

    public ArrayList<Node> getChildren(){
        return children;
    }

    public Node getChild(int i) {
        return children.get(i);
    }

    public int numChildren() {
        if(children == null) return 0;
        return children.size();
    }

    public int getValue(){
        return value;
    }

    public boolean isLastChild(){
        ArrayList<Node> siblings = this.parent.getChildren();
        return siblings.get(siblings.size()-1).equals(this);
    }

    public Node getRightSibling() {
        ArrayList<Node> siblings = this.parent.getChildren();
        for(int i=0; i < siblings.size(); i++) {
            if(siblings.get(i).equals(this))
                return siblings.get(i+1);
        }
        return null;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    @Override
    public String toString() {
        return name;
    }

    public static int numNodes(Node n) {
        if(n.getChildren() == null)
            return 1;
        else {
            int val = 1;
            for(Node child: n.getChildren())
                val += numNodes(child);
            return val;
        }
    }

    public static int getDepth(Node n) {
        if(n.getChildren() == null)
            return 1;
        else {
            return 1 + getDepth(n.getChildren().get(0));
        }
    }
}
