package generators.maths.northwestcornerrule;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;

import generators.maths.grid.GridProperty;

public class AnimProps {
	
	public static GridProperty GRID_PROPS;
	public static TextProperties HEADER_PROPS;
	public static RectProperties RECT_HEADER_PROPS;
	public static TextProperties TXT_INTRO_PROPS;
	public static ArrayProperties ARRAY_PROPS;
	public static RectProperties RECT_TEXTBOX_PROPS;
	public static RectProperties RECT_TEXTBOX2_PROPS;
	public static SourceCodeProperties SC_PROPS;
	public static TextProperties TXT_VAR_PROPS;
	public static TextProperties TXT_VAR_PROPS2;
	
	static{
		GRID_PROPS = new GridProperty();
		GRID_PROPS.set(GridProperty.GRID_COLOR, Color.gray);
		GRID_PROPS.set(GridProperty.BORDER, true);
		GRID_PROPS.set(GridProperty.CAPTION_OFFSET_TOP, new Point(0, -5));
		GRID_PROPS.set(GridProperty.CAPTION_OFFSET_LEFT, new Point(-10, 0));
		GRID_PROPS.set(GridProperty.CAPTION_TEXT_SIZE, 26);
		GRID_PROPS.set(GridProperty.CAPTION_COLOR, Color.gray);
		
		RECT_HEADER_PROPS = new RectProperties();
		RECT_HEADER_PROPS.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		RECT_HEADER_PROPS.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.yellow);
		RECT_HEADER_PROPS.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		RECT_TEXTBOX_PROPS = new RectProperties();
		RECT_TEXTBOX_PROPS.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		RECT_TEXTBOX_PROPS.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.red);
		RECT_TEXTBOX_PROPS.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		RECT_TEXTBOX2_PROPS = new RectProperties();
		RECT_TEXTBOX2_PROPS.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		RECT_TEXTBOX2_PROPS.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		RECT_TEXTBOX2_PROPS.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		HEADER_PROPS = new TextProperties();
		HEADER_PROPS.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
	       Font.SANS_SERIF, Font.BOLD, 20));
		
		TXT_INTRO_PROPS = new TextProperties();
		TXT_INTRO_PROPS.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
	        Font.SANS_SERIF, Font.PLAIN, 16));
		TXT_INTRO_PROPS.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
		
		TXT_VAR_PROPS = new TextProperties();
		TXT_VAR_PROPS.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
	        Font.SANS_SERIF, Font.BOLD, 16));
		TXT_VAR_PROPS.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
		
		TXT_VAR_PROPS2 = new TextProperties();
		TXT_VAR_PROPS2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
	        Font.SANS_SERIF, Font.BOLD, 16));
		
		ARRAY_PROPS = new ArrayProperties();
		ARRAY_PROPS.set(AnimationPropertiesKeys.COLOR_PROPERTY,Color.RED); 
		ARRAY_PROPS.set(AnimationPropertiesKeys.FILLED_PROPERTY,true);
		ARRAY_PROPS.set(AnimationPropertiesKeys.FILL_PROPERTY,Color.GRAY);
		
		SC_PROPS = new SourceCodeProperties();
		SC_PROPS.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		SC_PROPS.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));	    
		SC_PROPS.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);   
		SC_PROPS.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
	}

}
