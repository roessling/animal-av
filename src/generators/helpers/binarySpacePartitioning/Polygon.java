package generators.helpers.binarySpacePartitioning;

import java.awt.Color;

import algoanim.exceptions.NotEnoughNodesException;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolygonProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;

/**
 * A polygon used for BSP tree generation. Some methods assume 2D 
 * representation. Can be drawn using animal script. 
 * A 2D polygon used here actually consists of 2 vertices only.
 */
public class Polygon {
	
	private Vector[] _points;
	private algoanim.primitives.Polygon _polygon;
	
	public Polygon(Vector[] points) {
		set(points);
	}
	
	public Polygon() {
	}

	public void set(Vector[] points) {
		_points = points;
	}
	
	public void set(Vector[] points, int length) {
		_points = new Vector[length];
		System.arraycopy(points, 0, _points, 0, length);
	}
	
	public Vector getPoint(int index) {
		return _points[index];
	}
	
	public Vector calcNormal() {
		Vector d = _points[0].subtract(_points[1]);
		return new Vector(-d.getY(), d.getX());
	}
	
	public void draw(Language lang, Color color) {
		draw(lang, color, true);
	} 
	
	public void draw(Language lang, Color color, boolean bold) {
		
		Vector border = new Vector(0, 0);
		if (bold) border = calcNormal().calcDirection().multiply(2.0f);
		
		PolygonProperties polyProps = new PolygonProperties();
		polyProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		polyProps.set(AnimationPropertiesKeys.FILL_PROPERTY, color);
		polyProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
		
		Coordinates[] p1coords = new Coordinates[4];
		
		p1coords[0] = new Coordinates((int) (_points[0].getX() - border.getX()), (int) (_points[0].getY() - border.getY()));
		p1coords[1] = new Coordinates((int) (_points[0].getX() + border.getX()), (int) (_points[0].getY() + border.getY()));
		
		p1coords[2] = new Coordinates((int) (_points[1].getX() + border.getX()), (int) (_points[1].getY() + border.getY()));
		p1coords[3] = new Coordinates((int) (_points[1].getX() - border.getX()), (int) (_points[1].getY() - border.getY()));
			
		try {
			_polygon = lang.newPolygon(p1coords, "Polygon", null, polyProps);
		} catch (NotEnoughNodesException e) {
			e.printStackTrace();
		}
	}
	
	public void highlight(Language lang, Color color) {
		_polygon.changeColor("fillColor", color, new MsTiming(0), new MsTiming(0));
	}
	
	public int getNumPoints() {
		return _points.length;
	}
}
