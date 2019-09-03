package generators.helpers;
import java.awt.Color;

import algoanim.primitives.Circle;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;

public class Knoten {
public Text t;
public Circle c;
public Coordinates from,to,co;
public Knoten(int b,int x,int y,Language l){
		CircleProperties cprop=new CircleProperties();
		cprop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		cprop.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		cprop.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		from=new Coordinates(x+20,y);to=new Coordinates(x-20,y);
		c=l.newCircle(new Coordinates(x,y), 20, ""+cprop.hashCode(), null, cprop);
		TextProperties tprop=new TextProperties();
		t=l.newText(new Coordinates(x-5,y-5),""+b,""+c.hashCode(),null,tprop);
		co=new Coordinates(x,y);
	}
public void moveBy(int x,int y){
		c.moveBy("translate", x, y, new MsTiming(10), new MsTiming(500));
		t.moveBy("translate", x, y, new MsTiming(10), new MsTiming(500));
		from=new Coordinates(from.getX()+x,from.getY()+y);
		to=new Coordinates(to.getX()+x,to.getY()+y);
		co=new Coordinates(co.getX()+x,co.getY()+y);
		//System.out.println("*******************ZZZZZZZZZZZZZZZZZZZ*");
	}
public void hightlight(){
		c.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE,null, null);
	}
public void unhightlight(){
	   c.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY,null, null);
	}
public void markout(){
		c.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED,null, null);
	}
}