package generators.helpers;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;

import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class ListElement
{
	private Language lang;
	private static TextProperties textProps;
	private static RectProperties rectProps;
	private static boolean initialised = false;
	
	private ListElement next;
	private PolylineHandler nextPointer;
	private ListElement prev;

	private ListElement nextRun;
	private LinkedList<ListElement> pointedAt;
	private PolylineHandler nextRunPointer;
	
	public int num;
	private Coordinates ul;
	private Coordinates lr;
	private Text text;
	private Rect rect;
	private static final int height = 16;
	private static final int heightSpace = 2;
	private static final int widthPerPlace = 10;
	private static final int widthSpace = 2;
	private int width;

	private int index;
	private boolean same;
	
	public ListElement(Language lang, Coordinates upperLeft, int num, String name)
	{
		init();
		this.lang = lang;
		int places = 0;
		int checkPlaces = num;
		if(checkPlaces < 0) places++;
		while(checkPlaces != 0)
		{
			places++;
			checkPlaces /= 10;
		}
		if(places == 0) places++;
		width = widthPerPlace*places+widthSpace*2;
		ul = new Coordinates(upperLeft.getX()-width/2, upperLeft.getY()-height/2);
		lr = new Coordinates(ul.getX()+width, ul.getY()+height+heightSpace*2);
		rect = lang.newRect(ul, lr, name+"rect", null, rectProps);
		text = lang.newText(new Coordinates(ul.getX()+widthSpace, ul.getY()+heightSpace-2), String.valueOf(num), name+"text", null, textProps);
		pointedAt = new LinkedList<ListElement>();
		this.num = num;
		same = false;
	}
	
	public void moveTo(Node n)
	{
		Coordinates to = (Coordinates)n;
		moveTo(to.getX(), to.getY());
	}
	
	public void moveTo(int x, int y)
	{
		int xdif = x-ul.getX();
		int ydif = y-ul.getY();
		moveBy(xdif, ydif);
	}
	
	public void moveBy(int x, int y)
	{
		text.moveBy("translate", x, y, null, null);
		rect.moveBy("translate", x, y, null, null);
		ul = new Coordinates(ul.getX()+x, ul.getY()+y);
		lr = new Coordinates(lr.getX()+x, lr.getY()+y);
		if(next != null) nextPointer.point(this, next);
		if(prev != null) prev.nextPointer.point(prev, this);
		if(nextRun != null) nextRunPointer.point(this, nextRun);
		if(!pointedAt.isEmpty())
		{
			for(ListElement le : pointedAt)
			{
				le.nextRunPointer.point(le, this);
			}
		}
	}
	
	public void setNext(ListElement le)
	{
		if(next != null)
		{
			if(next.prev == this)next.prev = null;
			next = le;
			nextPointer.point(this, le);
		}
		else
		{
			if(le == next) return;
			next = le;
			nextPointer = new PolylineHandler(lang, this, next, rect.getName()+"next");
		}
		next.prev = this;
		checkSame();
	}
	
	public void setNextRun(ListElement le)
	{
		if(nextRun != null)
		{
			nextRun.pointedAt.remove(this);
			nextRun = le;
			nextRunPointer.point(this, le);
		}
		else
		{
			if(le == nextRun) return;
			nextRun = le;
			nextRunPointer = new PolylineHandler(lang, this, nextRun, rect.getName()+"nRun");
			nextRunPointer.changeColor();
		}
		nextRun.pointedAt.add(this);
		checkSame();
	}
	
	private void checkSame()
	{
		if(next == nextRun)
		{
			if(same) return;
			nextPointer.same();
			nextRunPointer.same();
			same = true;
			return;
		}
		else if(same)
		{
			nextPointer.unsame();
			nextRunPointer.changeColor();
		}
		same = false;
	}
	
	public Node getUpperLeft()
	{
		return ul;
	}
	
	public Node getUpperRight()
	{
		return new Coordinates(lr.getX(), ul.getY());
	}
	
	public Node getLowerRight()
	{
		return lr;
	}
	
	public Node getLowerLeft()
	{
		return new Coordinates(ul.getX(), lr.getY());
	}
	
	public void highlight()
	{
		text.changeColor("", Color.BLUE, null, null);
		rect.changeColor("FillColor", Color.RED, null, null);
	}
	
	public void unhighlight()
	{
		text.changeColor("", Color.BLACK, null, null);
		rect.changeColor("FillColor", Color.LIGHT_GRAY, null, null);		
	}
	
	private static void init()
	{
		if(!initialised)
		{
			rectProps = new RectProperties();
			textProps = new TextProperties();
			initialised = true;
			rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
			rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
			rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
			textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
			textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 16));
			textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 4);
		}
	}
	
	public ListElement getNextRun()
	{
		return nextRun;
	}
	
	public ListElement getNext()
	{
		return next;
	}
	
	public int getWidth()
	{
		return width;
	}

	public Node getLeft()
	{
		return new Coordinates(ul.getX(), ul.getY()+height/2);
	}

	public Node getRight()
	{
		return new Coordinates(lr.getX(), ul.getY()+height/2);
	}
	
	public int getIndex()
	{
		return index;
	}
	
	public void setIndex(int i)
	{
		index = i;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void hide()
	{
		rect.hide();
		text.hide();
		nextPointer.hide();
		nextRunPointer.hide();
	}
}
