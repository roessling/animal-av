package generators.maths.grid;

import generators.maths.grid.GridProperty;

import java.awt.Color;

import algoanim.primitives.Square;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
public class GridCell {

	private Coordinates upperLeft;
	private int size;
	private Language lang;
	private Square graphicObj;
	private Text label;
	private GridProperty gridProperty;
	
	public GridCell(Coordinates upperLeft, int size, Language lang , GridProperty gridProperty) {
		this.lang = lang;
		this.size = size;
		this.upperLeft = upperLeft;
		this.gridProperty = gridProperty;
		
		graphicObj = lang.newSquare(upperLeft, size+1, "",null, gridProperty.getCellProperty());
	}
	
	public void setLabel(String label) {
		if (this.label != null) {this.label.hide();}
		Coordinates upperLeftTmp = new Coordinates(upperLeft.getX() + 2, upperLeft.getY() + 2);
		this.label = lang.newText(upperLeftTmp, label, "", null, gridProperty.getTextProperty(size, size, label));
	}
	
	public void highlight(Color color, int delay) {		
		graphicObj.changeColor("fillColor", color,new MsTiming(delay), null);
	}
	
	public void unhighlight(int delay) {
		Color orgColor = (Color) graphicObj.getProperties().get(AnimationPropertiesKeys.FILL_PROPERTY);
		graphicObj.changeColor("fillColor", orgColor,new MsTiming(delay), null);
	}
	
	public void blink(Color color, int delay) {		
		Color orgColor = (Color) graphicObj.getProperties().get(AnimationPropertiesKeys.FILL_PROPERTY);
		graphicObj.changeColor("fillColor", color,new MsTiming(delay), null);
		graphicObj.changeColor("fillColor", orgColor,new MsTiming(delay + 500), null);
		graphicObj.changeColor("fillColor", color,new MsTiming(delay+1000), null);
		graphicObj.changeColor("fillColor", orgColor,new MsTiming(delay+1500), null);
		graphicObj.changeColor("fillColor", color,new MsTiming(delay+2000), null);
		graphicObj.changeColor("fillColor", orgColor,new MsTiming(delay+2500), null);
	}

	public Text getLabel() {
		return label;
	}
	
	

}
