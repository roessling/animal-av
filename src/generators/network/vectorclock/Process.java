package generators.network.vectorclock;

import java.util.LinkedHashMap;

import algoanim.primitives.Polyline;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;

/**
 * Class that represents a process
 * clock:		The vector clock of the process
 * clockRow:	The row of the process in all vector clocks
 */
public class Process{
	public int clockRow;
	public LinkedHashMap<String, Integer> clock;
	
	// ANIMAL
	public Text name;
	public Polyline line;
	public StringMatrix clockVector;	// representation of saved vector clock
	public StringMatrix clockVectorSR;	// representation of sent/received vector clock
	
	public Process(String[] processes, int clockRow) {
		this.clockRow = clockRow;
		this.clock = new LinkedHashMap<String, Integer>();
		// create clock with entries for every process, initialize with 0
		for(String s : processes) {
			this.clock.put(s, 0);
		}
	}
}