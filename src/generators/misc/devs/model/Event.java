package generators.misc.devs.model;

import java.util.Locale;

/**
 * @author Andrej Felde (andrej.felde@stud.tu-darmstadt.de)
 * @author Thomas Hesse (thomas.hesse@stud.tu-darmstadt.de)
 * @since 2013
 */
public class Event implements Comparable<Event> {

	private double	time;
	private char	type;

	public Event(double time, char type) {
		this.time = time;
		this.type = type;
	}

	public double getTime() {
		return this.time;
	}

	public char getType() {
		return this.type;
	}

	@Override
	public int compareTo(Event event) {
		return (time < event.getTime()) ? -1 : (time > event.getTime()) ? 1 : 0;
	}

	/**
	 * Creates the String representation of the event as
	 * 
	 * <pre>
	 * (0.05, A)
	 * </pre>
	 */
	@Override
	public String toString() {
		return "(" + String.format(Locale.CANADA, "%.2f", this.time) + ", " + this.type + ")";
	}

}