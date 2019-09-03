package generators.helpers;
import java.awt.Color;

import algoanim.primitives.Circle;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

/**
 * @author Atilla Yalcin(1240034) , Mustafa Tuerkmen(1094578)
 * @version 1.0 2008-07-28
 *
 */
public class KnotenAYMT {
	public Text t;
	public Circle c;
	public Coordinates from,to,co;

	public KnotenAYMT(int x,int y){
		//Festlegung der Koordinaten
		co=new Coordinates(x,y);
		
	}
	
	
	public void einfuegen(int i,Language l){
		
		int x=co.getX();
		int y=co.getY();
		
		CircleProperties cprop=new CircleProperties();
		cprop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		cprop.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		cprop.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		
		//um die Linien zu zeichenen werden anfang und endpunkt berechnet
		from=new Coordinates(x,y+20);
		to=new Coordinates(x,y-20);
		
		c=l.newCircle(new Coordinates(x,y), 20, ""+cprop.hashCode(), null, cprop);
		TextProperties tprop=new TextProperties();
		t=l.newText(new Coordinates(x-5,y-5),""+i,""+c.hashCode(),null,tprop);
		
	}
	public void hightlight(){
		c.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE,null, null);
	}
	public void unhightlight(){
	   c.changeColor(AnimationPropertiesKeys.FILL_PROPERTY,Color.LIGHT_GRAY,null, null);
	}
	public void markout(){
		c.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED,null, null);
	}

}
