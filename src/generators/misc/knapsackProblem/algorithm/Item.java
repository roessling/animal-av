package generators.misc.knapsackProblem.algorithm;

/**
 * 
 * Represents an item in the knapsack
 *
 */
public class Item {

	private String name;
	private int weight;
	private int value;
	
	public Item(String name, int weight, int value) {
		this.name = name;
		this.weight = weight;
		this.value = value;
	}
	
	public String getName() {
		return this.name;
		
	}
	
	public int getWeight() {
		return this.weight;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public String toString() {
		return this.getName() + " w= " + this.getWeight() + ", v= " + this.getValue();
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Item) {
			return this.name.equals(((Item) other).getName());
		}
		return false;
	}
	
}
