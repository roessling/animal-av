package generators.misc.roundrobin;

import java.awt.Color;

public class Process {
	private int remainingTime;

	private int arrivalTime;

	private int id;

	private String name;

	private Color color;



	public Process(int id, int arrivalTime, int remainingTime, String name, Color color) {
		this.setRemainingTime(remainingTime);
		this.setArrivalTime(arrivalTime);
		this.id = id;
		this.name = name;
		this.color = color;
	}


	public String toString() {
		String p = "ID: " + id + "RemainingTime: " + remainingTime;
		return p;
	}


	public int getID() {
		return id;
	}


	public int getArrivalTime() {
		return arrivalTime;
	}


	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}


	public int getRemainingTime() {
		return remainingTime;
	}


	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}


	public String getName() {
		return name;
	}


	public Color getColor() {
		return color;
	}
}

