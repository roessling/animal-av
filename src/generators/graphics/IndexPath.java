package generators.graphics;

public class IndexPath
{
	private int section;
	private int rowsCount;
	
	public int getSection()
	{
		return section;
	}
	public int getRowsCount()
	{
		return rowsCount;
	}
	
	public IndexPath(int section, int rowsCount)
	{
		this.section = section;
		this.rowsCount = rowsCount;
	}
	
}