package generators.datastructures;

import java.util.LinkedList;

import animal.main.Link;
import generators.datastructures.Cell.Type;

public class CellGrid {

	Cell[][] cells;
	
	public CellGrid() {
		cells = new Cell[10][10];
	}
	
	public void add(int x, int y, Cell c) {
		cells[x][y] = c;
	}
	
	public Cell get(int x, int y) {
		return cells[x][y];
	}
	
	public Cell getSingleType(Type type) {
		for(int i = 0; i < cells.length; i++) 
			for(int j = 0; j < cells.length; j++) 
				if(cells[i][j].type == type)
					return cells[i][j];
		return null;
	}
	
	public LinkedList<Cell> getMultipleType(Type type) {
		LinkedList<Cell> rightType = new LinkedList<Cell>();
		for(int i = 0; i < cells.length; i++) 
			for(int j = 0; j < cells.length; j++) 
				if(cells[i][j].type == type)
					rightType.add(cells[i][j]);
		return rightType;
	}
	
	
	public int getMax() {
		LinkedList<Cell> list = getMultipleType(Type.NORMAL);
		int max = Integer.MIN_VALUE;
		for(Cell c : list) 
			if(c.getPotential() > max)
				max = c.getPotential();
		return max;
	}
	
	public int getMin() {
		LinkedList<Cell> list = getMultipleType(Type.NORMAL);
		int min = Integer.MAX_VALUE;
		for(Cell c : list) 
			if(c.getPotential() < min)
				min = c.getPotential();
		return min;
	}
	
	
}
