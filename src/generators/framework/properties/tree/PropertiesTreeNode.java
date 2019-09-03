/*
 * Created on 24.07.2005 by T. Ackermann
 */
package generators.framework.properties.tree;

import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;

import algoanim.properties.AnimationProperties;
import algoanim.properties.CallMethodProperties;

/**
 * This Node stores a reference to an AnimationProperties object.
 *
 * @author T. Ackermann
 */
public class PropertiesTreeNode extends DefaultMutableTreeNode {

	/** we store this because PropertiesTreeNode is serializable */
	private static final long serialVersionUID = 4051045280801764915L;
	
	/** stores the AnimationProperties (for property) */
	private transient AnimationProperties prop = null;
	
	/** stores the name (for item, primitive) */
	private String name = null;
	
	/** stores the label (for item, folder) */
	private String label = null;
	
	/** stores the value (for a primitive) */
	private Object value = null;
	
	/** stores the state (-1 unknown, 0 folder, 1 property, 2 item, 3 primitive) */
	private int iState = -1;
	
	/** show items? */
	private boolean showItems = true;
	
	/** show hidden items? */
	private boolean showHiddenItems = true;
	
	/** stores the description (for primitive) */
	private String description = null;

	
	/**
	 * Constructor
	 * creates a new PropertiesTreeNode-Object.
	 * @param folderLabel The label for the new folder.
	 */
	public PropertiesTreeNode(String folderLabel) {
		super();
		if (folderLabel == null || folderLabel.length() < 1) return;
		this.iState = 0;
		this.label = folderLabel;
	}
	
	/**
	 * Constructor
	 * creates a new PropertiesTreeNode-Object.
	 * @param p The AnimationProperties Object
	 */
	public PropertiesTreeNode(AnimationProperties p) {
		super();
		if (p == null) return;
		this.iState = 1;
		setAnimationProperties(p);
	}
	
	
	/**
	 * Constructor
	 * creates a new PropertiesTreeNode-Object.
	 * @param itemLabel The label for the item.
	 * @param itemName The name of the item.
	 */
	public PropertiesTreeNode(String itemLabel, String itemName) {
		super();
		if (itemLabel == null || itemName == null || itemName.length() < 1) return;
		this.iState = 2;
		this.label = itemLabel;
		this.name = itemName;
	}
	
	
	/**
	 * Constructor
	 * creates a new PropertiesTreeNode-Object.
	 * @param primitiveName The label for the primitive.
	 * @param primitive The value of the primitive.
	 */
	public PropertiesTreeNode(String primitiveName, Object primitive) {
		super();
		if (primitive == null || primitiveName == null ||
			primitiveName.length() < 1) return;
		this.iState = 3;
		this.name = primitiveName;
		this.value = primitive;
		this.description = "";
	}
	public PropertiesTreeNode(String primitiveName, Object primitive, String description) {
		super();
		if (primitive == null || primitiveName == null ||
			primitiveName.length() < 1) return;
		this.iState = 3;
		this.name = primitiveName;
		this.value = primitive;
		this.description = description;
	}

	
	/**
	 * getAnimationProperties
	 * returns the AnimationProperties for this Node.
	 * @return The AnimationProperties for this Node.
	 */
	public AnimationProperties getAnimationProperties() {
		if (this.iState != 1) return null;
		return this.prop;
	}

	
	/**
	 * setAnimationProperties
	 * sets the AnimationProperties for this Node.
	 * @param p The new AnimationProperties for this Node.
	 */
	private void setAnimationProperties(AnimationProperties p) {
		this.prop = p;
		removeAllChildren();
		if (p == null || !this.showItems || p instanceof CallMethodProperties)
			return;
		
		Iterator<String> it = p.getAllPropertyNames().iterator();
		String itemName;
		while (it.hasNext()) {
			itemName = it.next();
			if (itemName.equals("name")) continue;
			if (this.showHiddenItems)
				add(new PropertiesTreeNode(itemName, itemName));
			else 
				if (p.getIsEditable(itemName))
					add(new PropertiesTreeNode(p.getLabel(itemName), itemName));
		}	
	}


	/**
	 * getShowItems
	 * returns true, if currently items are displayed.
	 * @return true, if currently items are displayed.
	 */
	public boolean getShowItems() {
		return this.showItems;
	}

	
	/**
	 * setShowItems
	 * if b is true, then items are displayed.
	 * @param b if true, then items are displayed.
	 */
	public void setShowItems(boolean b) {
		this.showItems = b;
		if (this.iState == 1) setAnimationProperties(this.prop);
	}

	
	/**
	 * getShowHiddenItems
	 * returns true, if currently hidden items are displayed.
	 * @return true, if currently hidden items are displayed.
	 */
	public boolean getShowHiddenItems() {
		return this.showHiddenItems;
	}


	/**
	 * setShowHiddenItems
	 * if b is true, then hidden items are displayed.
	 * @param b if true, then hidden items are displayed.
	 */
	public void setShowHiddenItems(boolean b) {
		this.showHiddenItems = b;
		if (this.iState == 1) setAnimationProperties(this.prop);
	}
	
	
	/**
	 * isFolder
	 * return true if this node represents a folder.
	 * @return true if this node represents a folder.
	 */
	public boolean isFolder() {
		return (this.iState == 0);
	}
	
	
	/**
	 * isProperty
	 * return true if this node represents a property.
	 * @return true if this node represents a property.
	 */
	public boolean isProperty() {
		return (this.iState == 1);
	}
	
	
	/**
	 * isItem
	 * return true if this node represents an item.
	 * @return true if this node represents an item.
	 */
	public boolean isItem() {
		return (this.iState == 2);
	}
	
	
	/**
	 * isPrimitive
	 * return true if the node represents a primitive.
	 * @return True if the node represents a primitive.
	 */
	public boolean isPrimitive() {
		return (this.iState == 3);
	}

	
	/**
	 * getName
	 * retuns the Name if the node is an item or a primitive otherwise null.
	 * @return The Name if the node is an item or a primitive otherwise null.
	 */
	public String getName() {
		if (this.iState != 2 && this.iState != 3) return null;
		return this.name;
	}
	
	
	/**
	 * setName
	 * sets the name to the given newName.
	 * @param newName The new name for this node.
	 */
	public void setName(String newName) {
		if (this.iState != 3 && newName != null && newName.length() > 0 ) return;
		this.name = newName;
	}
	
	
	/**
	 * getLabel
	 * retuns the Label if the node is a folder or an item.
	 * @return The Label if the node is a folder or an item.
	 */
	public String getLabel() {
		if (this.iState != 0 && this.iState != 2) return null;
		return this.label;
	}
	
	
	/**
	 * setLabel
	 * sets the label to the given newLabel.
	 * @param newLabel The new label for this node.
	 */
	public void setLabel(String newLabel) {
		if (this.iState != 0 && this.iState != 2
			&& newLabel != null && newLabel.length() > 0) return;
		this.label = newLabel;
	}


	/**
	 * getValue
	 * retuns the value if the node is a primitive otherwise null.
	 * @return The value if the node is a primitive otherwise null.
	 */
	public Object getValue() {
		if (this.iState != 3) return null;
		return this.value;
	}
	

	/**
	 * setValue
	 * sets the value if the node is a primitive.
	 * @param obj The new value for the primitive.
	 */
	public void setValue(Object obj) {
		if (this.iState != 3 && obj != null) return;
		this.value = obj;
	}

	/**
	 * getDescription
	 * retuns the description if the node is a primitive otherwise null.
	 * @return The description if the node is a primitive otherwise null.
	 */
	public String getDescription() {
		if (this.iState != 3) return null;
		return this.description;
	}
	

	/**
	 * setDescription
	 * sets the description if the node is a primitive.
	 * @param obj The new description for the primitive.
	 */
	public void setDescription(String obj) {
		if (this.iState != 3 && obj != null) return;
		this.description = obj;
	}
    
    public String toString() {
    /** stores the state (-1 unknown, 0 folder, 1 property, 2 item, 3 primitive) */
      StringBuilder sb = new StringBuilder(50);
      switch(iState) {
      case 0:
        sb.append("/").append(getName());
        break;
      case 1:
        sb.append("[P] ").append(prop.toString());
        break;
      case 2: 
        sb.append(getLabel()).append(" -> ").append(getName());
        break;
      case 3:
        sb.append(getName()).append(" -> ").append(getValue());
        break;
      }
      return sb.toString();
    }
}
