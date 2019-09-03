package generators.misc.knapsackProblem.algorithm;

import java.util.ArrayList;

public class Basket {
	
	private ArrayList<Item> items;
	
	public Basket() {
		this.items = new ArrayList<Item>();
	}
	
	public Basket(ArrayList<Item> items) {
		this.items = new ArrayList<Item>(items);
	}
	
	public void addItem(Item item) {
		this.items.add(item);
	}
	
	
	public Item getItem(int index) {
		if (index < this.items.size()) {
			return this.items.get(index);
		}
		return null;
	}
	
	public Item grabFirstItem() {
		return this.items.remove(0);
	}
	
	public int getIndex(Item item) {
		int index = -1;
		for (int i = 0; i < this.items.size(); i++) {
			if (this.items.get(i).equals(item)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	public int getBasketSize() {
		return this.items.size();
	}
	
	public int getBasketWeight() {
		return this.items.stream().mapToInt(i -> i.getWeight()).sum();
	}
	
	public int getBasketValue() {
		return this.items.stream().mapToInt(i -> i.getValue()).sum();
	}
	
	public Basket copy() {
		return new Basket(this.items);
	}
	
	public String toString() {
		String print = "[Knapsack: ";
		for (int i = 0; i < this.items.size(); i++) {
			print += this.items.get(i).toString();
			print += " | ";
		}
		print += " total value= " + this.getBasketValue() + "]";
		return print;
	}
	
	public String[] toArray() {
		String[] array = new String[this.items.size() + 1];
		for (int i = 0; i < this.items.size(); i++) {
			array[i] = this.items.get(i).toString();
		}
		array[array.length-1] = "Total: " + "w= " + this.getBasketWeight() + "; v= " + this.getBasketValue();
		return array;
	}

}
