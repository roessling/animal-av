package generators.graphics;


public class Size {
	private int width = 0;
	private int height = 0;
	
	public int getHeight()
	{
		return height;
	}
	public int getWidth()
	{
		return width;
	}
	public Size(int w, int h)
	{
		this.height = h;
		this.width = w;
	}
}
