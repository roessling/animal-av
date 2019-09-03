package generators.maths.northwestcornerrule.io;

import generators.maths.northwestcornerrule.io.Reader;


public class StaticDataReader implements Reader {
	
	private int[] supply;
	private int[] demand;
	
	public StaticDataReader(int[] supply, int[] demand) {
		super();
		this.supply = supply;
		this.demand = demand;
	}

	public int[] readSupplyArray(){
		return supply;
	}
	
	public int[] readDemandArray(){
		return demand;
	}

}
