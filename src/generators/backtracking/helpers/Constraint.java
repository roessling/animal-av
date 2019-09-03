package generators.backtracking.helpers;
import java.util.ArrayList;

public class Constraint {
	private int id;
	private String person;
	private ArrayList<String> disallowed;
	private ArrayList<String> allowed;
	
	/**
	 * Constructor
	 * @param name for the specific constraint
	 */
	public Constraint(String name){
		id = 42;
		person = name;
		disallowed = new ArrayList<String>();
		allowed = new ArrayList<String>();
	}
	
	/**
	 * Constructor
	 * @param name for the specific constraint
	 * @param id for the specific constraint
	 */
	public Constraint(String name, int id){
		this.id = id;
		person = name;
		disallowed = new ArrayList<String>();
		allowed = new ArrayList<String>();
	}
	
	/**
	 * Getter method
	 * @returns id
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Setter method
	 * @param id the new id
	 */
	public void changeID(int id) {
		this.id = id;
	}
	
	/**
	 * Getter method
	 * @returns disallowed
	 */
	public ArrayList<String> getDisallowed() {
		return disallowed;
	}
	
	/**
	 * Getter method
	 * @returns allowed
	 */
	public ArrayList<String> getAllowed() {
		return allowed;
	}
	
	/**
	 * Adds a Allowed-constraint. 
	 * @param neighbors Neighbors that must sit next to the constraint-specific person
	 */
	public void addAllowed(ArrayList<String> neighbors) {
		for(String s : neighbors)
			addAllowed(s);
	}
	
	/**
	 * Adds a Allowed-constraint. 
	 * @param neighbor Neighbor that must sit next to the constraint-specific person
	 */
	public void addAllowed(String neighbor) {
		if(!allowed.contains(neighbor) && !disallowed.contains(neighbor))
			allowed.add(neighbor);
	}
	
	/**
	 * Adds a Allowed-constraint. 
	 * @param neighbors Neighbors that must sit next to the constraint-specific person
	 */
	public void addDisallowed(ArrayList<String> neighbors) {
		for(String s : neighbors)
			addDisallowed(s);
	}
	
	/**
	 * Adds a Disallowed-constraint. 
	 * @param neighbor Neighbor that can't sit next to the constraint-specific person
	 */
	public void addDisallowed(String neighbor) {
		if(!disallowed.contains(neighbor) && !allowed.contains(neighbor))
			disallowed.add(neighbor);
	}	
	
	/**
	 * Returns the constraint-specific person
	 * @return name of the constraint-specific person
	 */
	public String getName() {
		return person;
	}
	
	/**
	 * Checks if the given person is constraint-consistent
	 * @param person the person to check
	 * @returns 0 if the given person can't sit next to the constraint-specific person
	 * @returns 1 if the given person must sit next to the constraint-specific person (first choice)
	 * @returns 2 if the given person can sit next to the constraint-specific person (second choice)
	 */	
	public int consistent(String person) {
		if(allowed == null && disallowed == null)
			return 0;
		
		if(allowed.contains(person) && !disallowed.contains(person)) // first choice
			return 1;
		if(!disallowed.contains(person)) // second choice
			return 2;
		
		return 0;
	}
	
	/**
	 * @Override
	 * new toString method, string built of Name and Allowed- / Disallowed-members
	 */
	@Override
	public String toString() {
		String s = "\tName: " + person;
		s += "\nID: " + id;
		s += "\n\tAllowed: ";
		
		for(String asdf : allowed)
		{
			s += "\n\t\t" + asdf;
		}
		
		s += "\n\tDisallowed: ";
		
		for(String asdf : disallowed)
		{
			s += "\n\t\t" + asdf;
		}
		
		s += "\n";
		
		return s;
	}
	
	public String toConstraintString() {
		if(allowed.size() == 0 && disallowed.size() == 0)
			return "(" + id + ") " + person + " kann neben jedem sitzen.";
		
		String value = "(" + id + ") " + person;
		if(allowed.size() != 0)
			value += " sitzt neben ";
		for(String s : allowed)
			value += s + ", ";
		if(disallowed.size() != 0)
			value += "aber nicht neben ";
		for(String s : disallowed)
			value += s + ", ";
		
		return value.substring(0, value.lastIndexOf(","));
	}
}
