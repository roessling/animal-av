package generators.misc.helpersCOBWEB;

import java.util.*;

/**
 * @author Jonas Heinz Mieseler, Thilo George Petry
 *
 */
public class CobwebCategory {

	private ArrayList<int[]> records;				// samples of this category
	private ArrayList<CobwebCategory> children;		// child-categories of this category
//	private CobwebCategory parent;
	
	
	/**
	 * Class constructor
	 * Creates empty Arraylists for records and children of this category
	 * @param	parent the parent category of this category
	 */
	public CobwebCategory() {
		this.records = new ArrayList<int[]>();
		this.children = new ArrayList<CobwebCategory>();
//		this.parent = null;
	}
	
	/** 
	 * @return	samples of this category
	 */
	public ArrayList<int[]> getRecords() {
		return records;
	}
	
	/**
	 * @param records	samples to set for this category
	 */
	public void setRecords(ArrayList<int[]> records) {
		this.records = records;
	}
	
	/**
	 * @param record	one sample to add to this category
	 */
	public void addRecord(int[] record) {
		records.add(record);
	}
	
	/**
	 * @param record the record to be removed from this category
	 */
	public void removeRecord(int[] record) {
		records.remove(record);
	}
	
	/**
	 * @return	child-categories of this category
	 */
	public ArrayList<CobwebCategory> getChildren() {
		return children;
	}
	
	/**
	 * @param children	child-categories to set for this category
	 */
	public void setChildren(ArrayList<CobwebCategory> children) {
		this.children = children;
	}
	
	/**
	 * @param child		one child-categories to add to this category
	 */
	public void addChild(CobwebCategory child) {
		children.add(child);
	}
	
	/**
	 * @return	the parent category of this category
	 */
	/*
	public CobwebCategory getParent() {
		return this.parent;
	}
	*/
	
	/**
	 * @param parent	the category to be set as the new parent category of this category
	 */
	/*
	public void setParent(CobwebCategory parent) {
		this.parent = parent;
	}
	*/
	
	/**
	 * Calculates the "category description" / the category-conditional likelihood for attributes being 1.
	 * 
	 * @return	the category likelihood for attributes being 1
	 */
	public double[] categoryLikelihood1() {
		double[] cl = new double[records.get(0).length];
		for(int[] entry : records) {
			for(int i = 0; i < cl.length; i++) {
				cl[i] = cl[i] + entry[i];
			}
		}
		for(int i = 0; i < cl.length; i++) {
			cl[i] = cl[i] / records.size();
		}
		
		return cl;
	}
	
	/**
	 * Calculates the "category description" / the category-conditional likelihood for attributes being 0.
	 * 
	 * @return	the category likelihood for attributes being 0
	 */
	public double[] categoryLikelihood0() {
		double[] cl = new double[records.get(0).length];
		for(int[] entry : records) {
			for(int i = 0; i < cl.length; i++) {
				cl[i] = cl[i] + entry[i];
			}
		}
		for(int i = 0; i < cl.length; i++) {
			cl[i] = 1 - cl[i] / records.size();
		}
		
		return cl;
	}
	
	/**
	 * Counts all categories connected to this category as children (not only direct children) including this category itself.
	 * 
	 * @return	number of all children categories + 1 (including this category itself)
	 */
	public int countNodes() {
		int n = 1;
		if(this.children.isEmpty()) {
			return n;
		}
		for(CobwebCategory child : this.children) {
			n += child.countNodes();
		}
		return n;
	}
	
	/**
	 * Only usable with the root category.
	 * Calculates the height of the root.
	 * A root with no children has height 1.
	 * 
	 * @return	the height of the root
	 */
	public int calcHeight() {
		if(this.children.isEmpty()) {
			return 1;
		}
		int highestChild = 0;
		int h;
		for(CobwebCategory child : this.children) {
			if((h = child.calcHeight()) > highestChild) {
				highestChild = h;
			}
		}
		return highestChild + 1;
	}
	
	/**
	 * Calculates the depth of a category by counting the number of parent categories.
	 * Root has depth 0.
	 * 
	 * @return	the depth of the category
	 */
	/*
	public int calccDepth() {
		int depth = 0;
		CobwebCategory p = parent;
		while(p != null) {
			depth++;
			p = p.getParent();			
		}
		return depth;
	}
	*/
	
	/**
	 * @param child		category to check if it is contained in this.children
	 * @return			whether child is in this.children or not
	 */
	public boolean isParentOf(CobwebCategory child) {
		if(this.children.contains(child))
			return true;
		else return false;
	}
	
	/**
	 * Creates an ArrayList containing all categories in the subtree of this category, including this category itself.
	 * The List is in order of Depth-first search.
	 * 
	 * @return		ArrayList containing all categories in the subtree of this category
	 */
//	public ArrayList<CobwebCategory> treeToList() {
//		 ArrayList<CobwebCategory> categories =  new  ArrayList<CobwebCategory>();
//		 categories.add(this);
//		 if(this.children.isEmpty()) {
//			 return categories;
//		 }
//		 for(CobwebCategory child : this.children) {
//			 categories.addAll(child.treeToList());
//		 }
//		 return categories;
//	}
	
	/**
	 * Creates an ArrayList containing all categories in the subtree of this category, including this category itself.
	 * The List is in order of breadth-first search.
	 * 
	 * @return		ArrayList containing all categories in the subtree of this category
	 */
	public ArrayList<CobwebCategory> treeToList() {
		ArrayList<CobwebCategory> flatree = new ArrayList<CobwebCategory>();
		ArrayList<CobwebCategory> floor = new ArrayList<CobwebCategory>();
		floor.add(this);
		flatree.addAll(floor);
		ArrayList<CobwebCategory> next = new ArrayList<CobwebCategory>();
		int stop = this.calcHeight();
		for(int i = 1; i < stop; i++) {
			for(CobwebCategory cat : floor) {
				next.addAll(cat.getChildren());
			}
			floor = next;
			flatree.addAll(floor);
			next = new ArrayList<CobwebCategory>();
		}
		return flatree;
	}

	
	/**
	 * Creates an ArrayList containing at position n an ArrayList with all categories of the n-th layer of the subtree of this category.
	 * 
	 * @return		the created ArrayList
	 */
	public ArrayList<ArrayList<CobwebCategory>> layerList() {
		ArrayList<ArrayList<CobwebCategory>> layers = new ArrayList<ArrayList<CobwebCategory>>();
		ArrayList<CobwebCategory> floor = new ArrayList<CobwebCategory>();
		floor.add(this);
		layers.add(floor);
		ArrayList<CobwebCategory> next = new ArrayList<CobwebCategory>();
		int stop = this.calcHeight();
		for(int i = 1; i < stop; i++) {
			for(CobwebCategory cat : floor) {
				next.addAll(cat.getChildren());
			}
			floor = next;
			layers.add(floor);
			next = new ArrayList<CobwebCategory>();
		}
		return layers;
	}
}
