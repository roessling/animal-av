package generators.helpers;

import java.awt.Color;

import algoanim.primitives.Polyline;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class PolylineHandler
{
	private Polyline pl;
	private static PolylineProperties lineProps;
	private static boolean initialised;
	private Coordinates vertices[];
	
	public PolylineHandler(Language lang, ListElement from, ListElement to, String name)
	{
		init();
		vertices = new Coordinates[2];
		Coordinates l1 = (Coordinates)from.getUpperLeft();
		Coordinates l2 = (Coordinates)to.getUpperLeft();
		Node f;
		Node t;
		if(l1.getX() < l2.getX()) //l1 links
		{
			if(l1.getY() < l2.getY()) //l1 oben
			{
				f = from.getLowerRight();
				t = to.getUpperLeft();
			}
			else if(l1.getY() == l2.getY())
			{
				f = from.getRight();
				t = to.getLeft();
			}
			else //l1 unten
			{
				f = from.getUpperRight();
				t = to.getLowerLeft();
			}
		}
		else //l1 rechts
		{
			if(l1.getY() < l2.getY()) //l1 oben
			{
				f = from.getLowerLeft();
				t = to.getUpperRight();
			}
			else if(l1.getY() == l2.getY())
			{
				f = from.getLeft();
				t = to.getRight();
			}
			else //l1 unten
			{
				f = from.getUpperLeft();
				t = to.getLowerRight();
			}			
		}
		pl = lang.newPolyline(new Node[]{f, t}, name+"pl", null, lineProps);
		vertices[0] = (Coordinates)f;
		vertices[1] = (Coordinates)t;
	}
	
	public PolylineHandler(Language lang, Node from, Node to, String name)
	{
		init();
		vertices = new Coordinates[2];
		pl = lang.newPolyline(new Node[]{from, to}, name+"pl", null, lineProps);
		vertices[0] = (Coordinates)from;
		vertices[1] = (Coordinates)to;
	}
	
	public PolylineHandler(Polyline pl)
	{
		this.pl = pl;
		vertices = (Coordinates[])pl.getNodes();
	}
	
	public void point(ListElement from, ListElement to)
	{
		Coordinates l1 = (Coordinates)from.getUpperLeft();
		Coordinates l2 = (Coordinates)to.getUpperLeft();
		Node f;
		Node t;
		if(l1.getX() < l2.getX()) //l1 links
		{
			if(l1.getY() < l2.getY()) //l1 oben
			{
				f = from.getLowerRight();
				t = to.getUpperLeft();
			}
			else if(l1.getY() == l2.getY())
			{
				f = from.getRight();
				t = to.getLeft();
			}
			else //l1 unten
			{
				f = from.getUpperRight();
				t = to.getLowerLeft();
			}
		}
		else //l1 rechts
		{
			if(l1.getY() < l2.getY()) //l1 oben
			{
				f = from.getLowerLeft();
				t = to.getUpperRight();
			}
			else if(l1.getY() == l2.getY())
			{
				f = from.getLeft();
				t = to.getRight();
			}
			else //l1 unten
			{
				f = from.getUpperLeft();
				t = to.getLowerRight();
			}			
		}
		moveNodeTo(0, f);
		moveNodeTo(1, t);
	}
	
	public void moveNodeTo(int num, Node n)
	{
		Coordinates to = (Coordinates)n;
		moveNodeTo(num, to.getX(), to.getY());
	}
	
	public void moveNodeTo(int num, int x, int y)
	{
		moveNodeBy(num, x-vertices[num].getX(), y-vertices[num].getY());
	}
	
	public void moveNodeBy(int num, int x, int y)
	{
		if(x != 0 || y != 0)
		{
			pl.moveBy("translate #"+(num+1), x, y, null, null);
			Coordinates old = vertices[num];
			vertices[num] = new Coordinates(old.getX()+x, old.getY()+y);
		}
		
	}
	
	public void moveTo(Node n)
	{
		Coordinates to = (Coordinates)n;
		moveTo(to.getX(), to.getY());
	}
	
	public void moveTo(int x, int y)
	{
		moveBy(x-vertices[1].getX(), y-vertices[1].getY());
	}
	
	public void moveBy(int x, int y)
	{
		if(x != 0 || y != 0)
		{
			pl.moveBy("translate", x, y, null, null);
			Coordinates old = vertices[0];
			vertices[0] = new Coordinates(old.getX()+x, old.getY()+y);
			old = vertices[1];
			vertices[1] = new Coordinates(old.getX()+x, old.getY()+y);
		}
		
	}
	
	private static void init()
	{
		if(!initialised)
		{
			lineProps = new PolylineProperties();
			lineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
			lineProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
			lineProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 6);
		}
	}
	
	public void changeColor()
	{
		pl.changeColor("", Color.RED, null, null);
	}
	
	public void hide()
	{
		pl.hide();
	}
	
	public void show()
	{
		pl.show();
	}

	public void same()
	{
		pl.changeColor("", Color.GREEN, null, null);
		
	}

	public void unsame()
	{
		pl.changeColor("", Color.BLUE, null, null);
		
	}
    public Polyline getPl()
    {
	return pl;
    }

}
