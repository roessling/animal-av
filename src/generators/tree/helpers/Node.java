/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package generators.tree.helpers;

import java.util.List;
/**
 *
 * @author M@rkus
 */
public class Node {

	private Node[] children;
	public int[] value;

	public Node(){
                this(new int[]{0,0,-1});
	}
	
	public Node(int[] value) {
		this.value = value;
		children = new Node[4];
	}

	public int getSize() {
		int size = 1;
		for (int i = 0; i < children.length; i++) {
			if (children[i] != null) {
				size += children[i].getSize();
			}
		}
		return size;
	}
        
        public int getHeight(){
            int result = 1;
            int maxHeight = 0;
            for(int i = 0; i < children.length; i++){
                if(children[i] != null)
                   maxHeight = java.lang.Math.max(maxHeight, children[i].getHeight());
            }
            result += maxHeight;
            return result;
        
        }

	public Node[] getChildren() {
		return children;
	}

	public boolean hasChildren() {
		return getSize() > 1;
	}

    void getLabels(List<String> labels) {
        if((value[2] == 0))
        labels.add("?");
        else if((value[2] > 0))
            labels.add(""+value[1]+","+value[0]);
        else
            labels.add("");
        for(int i = 0; i < children.length; i++){
                if(children[i] != null)
                   children[i].getLabels(labels);
            }
    }

    void getAllNodes(List<Node> nodes) {
            nodes.add(this);
        for(int i = 0; i < children.length; i++){
                if(children[i] != null)
                   children[i].getAllNodes(nodes);
            }
    }
}
