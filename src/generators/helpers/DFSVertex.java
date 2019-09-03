package generators.helpers;

import java.awt.Color;

public class DFSVertex {
	private Color color;
	private int visitedTime;
	private int finishedTime;
	private int nr;
	private int x;
	private int y;

	public DFSVertex(int position, int x, int y) {
		color = Color.white;
		visitedTime = 0;
		finishedTime = 0;
		nr = position;
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getVisitedTime() {
		return visitedTime;
	}

	public void setVisitedTime(int visitedTime) {
		this.visitedTime = visitedTime;
	}

	public int getFinishedTime() {
		return finishedTime;
	}

	public void setFinishedTime(int finishedTime) {
		this.finishedTime = finishedTime;
	}

	public void setNr(int nr) {
		this.nr = nr;
	}

	public int getNr() {
		return nr;
	}

}
