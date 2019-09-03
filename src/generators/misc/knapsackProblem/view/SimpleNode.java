package generators.misc.knapsackProblem.view;

import algoanim.primitives.StringArray;
import generators.misc.knapsackProblem.algorithm.Basket;

public class SimpleNode {

	public int id;
	
	private Basket basket;
	public StringArray basketViz;
	private SimpleNode left;
	private SimpleNode right;
	
	public SimpleNode(Basket basket) {
		this.basket = basket;
	}
	
	public int getWeight() {
		return this.basket.getBasketWeight();
	}
	
	public int getValue() {
		return this.basket.getBasketValue();
	}
	
	public void addLeft(SimpleNode node) {
		this.left = node;
	}
	
	public SimpleNode getLeft() {
		return this.left;
	}
	
	public void addRight(SimpleNode node) {
		this.right = node;
	}
	
	public SimpleNode getRight() {
		return this.right;
	}
	
	public Basket getBasket() {
		return this.basket;
	}
	
	public void setBasket(Basket basket) {
		this.basket = basket;
	}
	
	public boolean isLeaf() {
		return this.left == null && this.right == null;
	}
}
