package generators.network.helpersMarzullo;

import java.util.ArrayList;

public class Marzullo_Tuple {
	private double position;
	private int edge;
	
	public static ArrayList<Marzullo_Tuple> intervall(double center, double offset) {
		ArrayList<Marzullo_Tuple> intervall = new ArrayList<Marzullo_Tuple>();
		intervall.add( new Marzullo_Tuple(center - offset, -1));
		intervall.add( new Marzullo_Tuple(center + offset, 1));
		return intervall;
	}
	
	public Marzullo_Tuple(double x, int y){
		position = x;
		edge = y;
	}
	
	public double pos() {
		return position;
	}
	
	public int edge() {
		return edge;
	}
	
	public int compare(Marzullo_Tuple t, boolean overlap0) {
		if(position == t.pos()){
			int temp = edge - t.edge();
			if(overlap0)
				temp *= -1;
			return temp;
		}else {
			if(position < t.pos())
				return -1;
			else
				return 1;
		}
	}
}
