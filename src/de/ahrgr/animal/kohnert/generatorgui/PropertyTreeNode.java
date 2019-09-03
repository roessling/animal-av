/*
 * Created on 04.02.2005
 */
package de.ahrgr.animal.kohnert.generatorgui;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import de.ahrgr.animal.kohnert.asugen.property.Property;

/**
 * @author ek
 */
public class PropertyTreeNode extends DefaultMutableTreeNode {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1011950469560702396L;
	Property property = null;
	String name;
	
	public PropertyTreeNode(Property p, String aName) {
		super();
		property = p;
		name = aName;
		setUserObject(name);
	}
    
    public PropertyTreeNode addProperty(Property p, String key) {
        int i = key.indexOf("/");
        if(i>0) {
            // Unterbaum verwenden
            String key1 = key.substring(0, i);
            String key2 = key.substring(i+1);
            PropertyTreeNode t = findChild(key1);
            if(t == null) t = addProperty(null, key1); // Zwischenelement einfügen
            return t.addProperty(p, key2);
        } 
        PropertyTreeNode n = new PropertyTreeNode(p, key);
        add(n);
        return n;
    }
    
    public String getName() { return name; }
    
		public PropertyTreeNode findChild(String aName) {
        Enumeration<TreeNode> e = children();
        while(e.hasMoreElements()) {
            PropertyTreeNode sub = (PropertyTreeNode)e.nextElement();
            if(sub.getName().equals(aName)) return sub;
        }
        return null;
    }

}
