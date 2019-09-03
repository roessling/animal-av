/*
 * Point.java
 * Jan Rehbein, Marius Rettberg-Päplow, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc.kNN;

public class Point {
	
	public int X;
	public int Y;
	public String type;
	
	public Point(int X, int Y, String type){
		this.X = X;
		this.Y = Y;
		this.type = type;
	}

}
