package generators.network.vectorclock;

import java.util.LinkedHashMap;

import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;

/**
 * Class that represents an event of a process
 * time: 	When did event happen?
 * process: At which process?
 * event: 	What type of event?
 */
public class ProcessEvent implements Comparable<ProcessEvent>{
	public Integer id;
	public Integer time;
	public String process;
	public EventType type;
	public String sender;
	public ProcessEvent otherEvent; // reference to a message sent or message received event
	public LinkedHashMap<String, Integer> sentClockVector;
	public LinkedHashMap<String, Integer> computedClockVector;
	
	// ANIMAL
	public Circle circle;
	public Polyline messageArrow;
	public StringMatrix sentClockVectorGrid;
	public StringMatrix computedClockVectorGrid;
	public Text nameLabel;
	
	public ProcessEvent(int time, String process, EventType type) {
		this.time = time;
		this.process = process;
		this.type = type;
		this.messageArrow = null;
		this.sentClockVectorGrid = null;
	}
	
	public int compareTo(ProcessEvent ev) {
		return this.time.compareTo(ev.time);
	}
}