package generators.helpers.binarySpacePartitioning;

/**
 * A simple 2D vector class.
 */
public class Vector {
	float _x, _y;
	
	public Vector(float x, float y) {
		_x = x;
		_y = y;
	}
	
	public float calcLength()
	{
	  return (float) Math.sqrt(_x*_x + _y*_y);
	}

	public Vector calcDirection()
	{
	  return divide(calcLength());
	}
	
	public Vector subtract(Vector vector) {
		return new Vector(_x - vector.getX(), _y - vector.getY());
	}
	
	public Vector divide(float d) {
		return new Vector(_x / d, _y / d);
	}
	
	public Vector add(Vector vector) {
		return new Vector(_x + vector.getX(), _y + vector.getY());
	}
	
	public float dot(Vector vector) {
		return _x * vector.getX() + _y * vector.getY();
	}
	
	public Vector multiply(float d) {
		return new Vector(_x * d, _y * d);
	}
	
	public Vector cross(Vector vector) {
		return new Vector(_y * vector.getX() - _x * vector.getY(), _x * vector.getY() - _y * vector.getX());
	}

	public float getX() {
		return _x;
	}

	public void setX(float x) {
		_x = x;
	}

	public float getY() {
		return _y;
	}

	public void setY(float y) {
		_y = y;
	}

	public boolean equals(Vector vector) {
		return (vector.getX() == _x) && (vector.getY() == _y);
	}
}
