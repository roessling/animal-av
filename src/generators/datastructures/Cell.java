package generators.datastructures;

import java.awt.Color;

import algoanim.primitives.Square;

public class Cell {
	
	enum Type {
	    OBSTACLE,
	    GOAL,
	    START,
	    NORMAL,
	    PATH
	  }

	public int x;
	public int y;
	private int potential;
	public Type type;
	public Square square;
	public Color color;
	
	public Cell(int x, int y, int potential, Square square, Type type) {
		this.x = x;
		this.y = y;
		this.potential = potential;
		this.square = square;
		this.type = type;
	}
	
	public boolean equals(Cell b) {
		return x == b.x && y== b.y;
	}
	
	public void setPotential(int p) {
		potential = p;
	}
	
	public int getPotential() {
		return potential;
	}
}
