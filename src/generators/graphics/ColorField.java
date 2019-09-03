package generators.graphics;

import java.awt.Color;
import java.awt.Font;

import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Timing;

public class ColorField extends ScenarioObject
{
	
	private Language language;
	private Coordinates upperLeft;
	private Coordinates lowerRight;
	
	private Text[] textFields;
	private Rect colorField;
	
	public ColorField(Language language, Coordinates upperLeft, Coordinates lowerRight , String title, Color color){
		super();
		this.language = language;
		this.upperLeft = upperLeft;
		this.lowerRight = lowerRight;
		
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.PLAIN, 12));
		
		RectProperties rp = new RectProperties();
        rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rp.set(AnimationPropertiesKeys.FILL_PROPERTY, color);
        
        this.colorField = language.newRect(upperLeft, lowerRight, "", null, rp);
        
        String[] lines = title.split("\n");
        
        this.textFields = new Text[lines.length];
        
        int i = 0;
        for(String line : lines){
            Text t = language.newText(new Coordinates(upperLeft.getX(), lowerRight.getY() + (i*14)), line, "idt_"+line + "" + i, null, tp);
            this.textFields[i] = t;
            i++;
        }

	}
	
	public Coordinates getUpperleft(){
		return this.upperLeft;
	}
	
	/**
	 * MoveBy
	 * 
	 * @param moveType 	(String) e.g. "translate"
	 * @param x			(int)
	 * @param y			(int)
	 * @param delay		(Timing)
	 * @param duration	(Timing)
	 */
	public void moveBy(String moveType, int x, int y, Timing delay, Timing duration)
	{
		this.upperLeft = new Coordinates(this.upperLeft.getX() + x, this.upperLeft.getY() + y);
		this.lowerRight = new Coordinates(this.lowerRight.getX() + x, this.lowerRight.getY() + y);
		
		for (Text t : textFields)
		{
			t.moveBy(moveType, x, y,delay, duration);
		}
		
		this.colorField.moveBy(moveType, x,y, delay, duration);
	}
	

	@Override
	public void showPrimitives(Timing t) {
		if (this.colorField != null) this.colorField.show(t);
		if (this.textFields != null){
			for(Text text : this.textFields){
				text.show(t);
			}
		}
	}

	@Override
	public void hidePrimitives(Timing t) {
		if (this.colorField != null) this.colorField.hide();
		if (this.textFields != null) {
			for(Text text : this.textFields){
				text.hide();
			}
		}
	}

	@Override
	public void didRefresh() {			
	}
	
	@Override
	public void hide(){
		super.hide();
		if (this.colorField != null) this.colorField.hide();
		if (this.textFields != null) {
			for(Text text : this.textFields){
				text.hide();
			}
		}
	}
}