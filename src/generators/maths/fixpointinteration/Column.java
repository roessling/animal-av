package generators.maths.fixpointinteration;

public class Column {
	private final String caption;
	private final int width;
	
	public Column(String caption, int width) {
		this.caption = caption;
		this.width = width;
	}
	
	public String getCaption() {
		return caption;
	}
	
	public int getWidth() {
		return width;
	}

}
