package generators.tree;

public class SeparatorWindow {
	public float low_x;
	public float low_y;
	public float high_x;
	public float high_y;

	public SeparatorWindow(float lx, float ly, float hx, float hy) {
		low_x = lx;
		low_y = ly;
		high_x = hx;
		high_y = hy;
	}

	public float get_lx() {
		return low_x;
	}

	public float get_ly() {
		return low_y;
	}

	public float get_hx() {
		return high_x;
	}

	public float get_hy() {
		return high_y;
	}

	@Override
	public String toString() {
		String string_out = new String("lowX: " + low_x + " | lowY: " + low_y
				+ " | highX: " + high_x + " | highY: " + high_y);
		return string_out;
	}
}