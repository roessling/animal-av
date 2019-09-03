package generators.misc.helpersSatGenerator;

public class Vector2fSAT {
	public float x;
	public float y;
	
	public Vector2fSAT() {
		this.x = 0f;
		this.y = 0f;
	}
	
	public Vector2fSAT(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2fSAT normalize() {
		float scalar = 1 / length();
		return new Vector2fSAT(scalar * this.x, scalar * this.y);
	}
	
	public float length() {
		return (float)(Math.sqrt(this.x * this.x + this.y * this.y));
	}
	
	public float dot(Vector2fSAT other) {
		return this.x * other.x + this.y * other.y;
	}

	public float dist(Vector2fSAT other) {
		Vector2fSAT diff = this.sub(other);
		return (float)Math.sqrt(diff.x * diff.x + diff.y * diff.y);
	}
	
	public Vector2fSAT add(Vector2fSAT other) {
		return new Vector2fSAT(this.x + other.x, this.y + other.y);
	}
	
	public Vector2fSAT sub(Vector2fSAT other) {
		return new Vector2fSAT(this.x - other.x, this.y - other.y);
	}
	
	public Vector2fSAT mul(float scalar) {
		return new Vector2fSAT(this.x * scalar, this.y * scalar);
	}
	
	@Override
	public String toString() {
		return String.format("(%.2f, %.2f)", x, y);
	}
}
