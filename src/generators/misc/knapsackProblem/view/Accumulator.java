package generators.misc.knapsackProblem.view;

public class Accumulator {
	
	private int value;
	
	public Accumulator(int i) {
		this.value = i;
	}
	
	public void inc() {
		this.value++;
	}
	
	public int getValue() {
		return this.value;
	}

}
