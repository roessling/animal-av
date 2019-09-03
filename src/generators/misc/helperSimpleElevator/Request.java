package generators.misc.helperSimpleElevator;

public class Request {
	public final int source;
	public final int dest;
	public final int time;
	private Passanger pass;
	
	public Request(int source, int dest, int time) {
		this.source = source;
		this.dest = dest;
		this.time = time;
	}
	
	@Override
	public String toString() {
		return "("+source+","+dest+","+time+")";
	}
	
	public void setPassanger(Passanger c) {
		pass = c;
	}
	public Passanger getPassanger() {
		return pass;
	}
}
