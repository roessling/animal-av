package generators.misc.birch;

public class Vector {
    private float x, y;

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Vector add(Vector other) {
        return new Vector(x + other.x, y + other.y);
    }

    public Vector sub(Vector other) {
        return new Vector(x - other.x, y - other.y);
    }

    public Vector mulComponents(float a) {
        return new Vector(x * a, y * a);
    }

    public Vector mulComponents(Vector other) {
        return new Vector(x * other.x, y * other.y);
    }

    public Vector divComponents(float a) {
        return new Vector(x / a, y / a);
    }

    public Vector sqrComponents() {
        return new Vector(x * x, y * y);
    }

    public Vector sqrtComponents() {
        return new Vector((float) Math.sqrt(x), (float) Math.sqrt(y));
    }

    public float euclidNorm() {
        return (float) Math.sqrt(x * x + y * y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
