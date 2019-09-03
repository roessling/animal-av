package generators.graphics;


import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class DescriptionField extends ScenarioObject{
	
	private ArrayList<Text> descFields;
	
	private String[] descLines;
	
	private Language language;
	
	private Coordinates upperLeft;
	
	private int lineHeight = 20;
	
    private Font font;
    private Color textColor;
	
	public Coordinates getUpperleft(){
		return this.upperLeft;
	}
	
	public void setDescriptionText(String description, Timing defaultTiming)
	{
		this.hidePrimitives(defaultTiming);
		this.descFields.clear();
		
		TextProperties textFieldsProps = new TextProperties();
		textFieldsProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, this.textColor);
        textFieldsProps.set(AnimationPropertiesKeys.FONT_PROPERTY, this.font);
		
		this.descLines = description.split("\n");
		
		int lineHeight = Math.max(this.lineHeight, this.font.getSize()+ 4);
		
		int i = 0;
		for(String line : descLines)
		{
			Text text = this.language.newText(new Coordinates(this.upperLeft.getX(), this.upperLeft.getY() + (lineHeight * i)), "", "DescLine_"+i, null, textFieldsProps);
			this.descFields.add(text);
			text.setText(line, defaultTiming==null? null : new TicksTiming(defaultTiming.getDelay()*i), defaultTiming);
			i++;
		}
	}
	
	public DescriptionField(Language language, Coordinates upperLeft, String description, Timing t)
	{
		super();
		this.font = new Font(Font.SANS_SERIF, Font.PLAIN, 15);
		this.textColor = Color.black;
		this.upperLeft = upperLeft;
		this.language = language;
		this.descFields = new ArrayList<Text>();
		this.setDescriptionText(description, t);
	}
	
	
	public void setFont(Font f)
	{
		if (f != null){
			this.font = f;
			// new Font(Font.SANS_SERIF, Font.BOLD, 24)
			TextProperties textProps = new TextProperties();
			textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, f);
			
			for (Text line : this.descFields){
				line.setFont(f, null, null);
			}
		    
		}
	}
	public Font getFont(){
		return this.font;
	}

	public void setTextColor(Color color)
	{
		if (color != null){
			for (Text line : this.descFields) {
				TextProperties p = line.getProperties();
				if (p != null){
					Color currentTextColor = (Color) p.get(AnimationPropertiesKeys.COLOR_PROPERTY);
					if (currentTextColor != null && currentTextColor.equals(this.textColor)){
						line.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, color, null, null);
					}
				}
			}
			this.textColor = color;
		}
	}
	public Color getTextColor(){
		return this.textColor;
	}
	
	
	
	/**
	 * MoveBy
	 * 
	 * @param moveType 	(String) e.g. "translate"
	 * @param x 		(int)
	 * @param y			(int)
	 * @param delay		(Timing)
	 * @param duration	(Timing)
	 */
	public void moveBy(String moveType, int x, int y, Timing delay, Timing duration)
	{
		this.upperLeft = new Coordinates(this.upperLeft.getX() + x, this.upperLeft.getY() + y);
		
		for (Text t : descFields)
		{
			t.moveBy(moveType, x,y,delay, duration);
		}
		
	}
	
	public void showPrimitives(Timing t)
	{
		for(Text text : this.descFields)
		{
			text.show(t);
		}
	}
	public void hidePrimitives(Timing t)
	{
		for(Text text : this.descFields)
		{
			text.hide();
		}
	}
	public void didRefresh(){
		
	}
}