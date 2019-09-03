package generators.helpers;

import java.util.Random;

import algoanim.primitives.Circle;
import algoanim.primitives.Text;

public class Point implements Comparable<Point>{
	public float x, y;
	public float angle;
	public String name;
	public Circle circle;
	public Text label;

	public Point(float a, float b, int nr) {
		angle = 0;
		x = a;
		y = b;
		name = new String("P" + nr);
	}

	public Point(int nr) {
		angle = 0;
		Random rnd = new Random();
		x = 300 * rnd.nextFloat() + 500;
		y = 160 * rnd.nextFloat();

		name = new String("P" + nr);
	}

	@Override
	public int compareTo(Point o) {
		if(o.angle < this.angle)
			return 1;
		if(o.angle > this.angle)
			return -1;
		return 0;
	}
	
	@Override
	public String toString()
	{
		return name + " - " + x + " x " + y + " - " + angle;
	}
}