package generators.maths.vogelApprox.io;

import generators.maths.vogelApprox.io.Reader;


public class StaticDataReader implements Reader {
	
	private int[] supply;
	private int[] demand;
	private int[][] cost;
	
	public StaticDataReader(int[] supply, int[] demand, int[][] cost) {
		super();
		this.supply = supply;
		this.demand = demand;
		this.cost = cost;
	}

	public int[] readSupplyArray(){
		return supply;
	}
	
	public int[] readDemandArray(){
		return demand;
	}

	@Override
	public int[][] readCostArray() {
		return cost;
	}

}
