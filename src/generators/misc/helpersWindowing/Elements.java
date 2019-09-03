package generators.misc.helpersWindowing;

public class Elements {
	private String name;
	private int x;
	private int y;
	private int pos;
	private int neg;
	private double heuristic;
	
	public Elements(String name, int x,int y)
	{
		this.name = name;
		this.x = x;
		this.y = y;
	}
	
	public void setPosAndNeg(int p, int n)
	{
		pos = p;
		neg = n;
	}
	
	public void setheuristic(double h)
	{
		heuristic = h;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int gety()
	{
		return y;
	}
	
	public int getPos()
	{
		return pos;
	}
	
	public int getNeg()
	{
		return neg;
	}
	
	public double getHeuristic()
	{
		return heuristic;
	}
}
