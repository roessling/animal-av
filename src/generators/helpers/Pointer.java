package generators.helpers;

import algoanim.primitives.generators.Language;
import algoanim.util.Coordinates;

public class Pointer extends PolylineHandler
{
	private boolean top;
	public Pointer(Language lang, ListElement le, boolean top)
	{
		this(lang, 
				new Coordinates(
						((Coordinates)le.getLowerRight()).getX()-le.getWidth()/2,
						(top)?((Coordinates)le.getUpperLeft()).getY():((Coordinates)le.getLowerRight()).getY())
		, top);
	}
	
	public Pointer(Language lang, Coordinates c, boolean top)
	{
		super(lang,
				new Coordinates(c.getX(), c.getY()-((top)? 30: -30)),
				new Coordinates(c.getX(), c.getY()),
				((top)? "upper_ptr": "lower_ptr"));
		this.top = top;
	}

	public void pointTo(ListElement le)
	{
		Coordinates c;
		if(top)
			c = (Coordinates)le.getUpperLeft();
		else
			c = (Coordinates)le.getLowerLeft();
		moveTo(c.getX()+le.getWidth()/2, c.getY());
	}
	
	
}
