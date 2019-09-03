package generators.maths.fixpointinteration;

import java.awt.Color;

public class GraphLegendEntry {
	private final Color color;
	private final String name;
	
	public GraphLegendEntry(Color color, String name) {
		this.color = color;
		this.name = name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public String getName() {
		return name;
	}

}
