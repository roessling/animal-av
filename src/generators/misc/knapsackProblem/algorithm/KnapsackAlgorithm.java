package generators.misc.knapsackProblem.algorithm;

import generators.misc.knapsackProblem.view.SimpleNode;

public class KnapsackAlgorithm {
	
	public static Basket computeRecGraph(SimpleNode node, Basket options, Basket taken, int capacity) {
		System.out.println("");
		if (options.getBasketSize() == 0) {
			System.out.println(taken);
			// reference needed for result basket tagging
			node.setBasket(taken);
			return taken;
		} else {
			Item current = options.grabFirstItem();
			System.out.println("Calling: " + current.getName() + " W: [" + current.getWeight() + "] V:[" + current.getValue() + "] Cap: " + capacity);
			System.out.println(taken);
			if (current.getWeight() <= capacity) {
				// take item
				Basket tmpTaken = taken.copy();
				tmpTaken.addItem(current);
				SimpleNode left = new SimpleNode(tmpTaken);
				node.addLeft(left);
				Basket with = KnapsackAlgorithm.computeRecGraph(left, options.copy(), tmpTaken.copy(), capacity - current.getWeight());
				
				// don't take item
				SimpleNode right = new SimpleNode(taken);
				node.addRight(right);
				Basket without = KnapsackAlgorithm.computeRecGraph(right, options.copy(), taken.copy(), capacity);
				
				return with.getBasketValue() >= without.getBasketValue() ? with: without;
			} else {
				return KnapsackAlgorithm.computeRecGraph(node, options.copy(), taken.copy(), capacity);
			}
		}		
	}
}
