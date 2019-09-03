package generators.maths.vogelApprox;

import java.awt.Color;
import java.awt.Font;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;

public class AnimProps {
	
	
	public static TextProperties HEADER_PROPS;
	public static RectProperties RECT_HEADER_PROPS;
	public static TextProperties TXT_INTRO_PROPS;
	public static RectProperties RECT_TEXTBOX_PROPS;
	public static RectProperties RECT_TEXTBOX2_PROPS;
	public static SourceCodeProperties SC_PROPS;
	public static TextProperties TXT_VAR_PROPS;
	public static TextProperties TXT_VAR_PROPS2;
	
	static{		
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
		
		
		SC_PROPS = new SourceCodeProperties();
		SC_PROPS.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		SC_PROPS.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));	    
		SC_PROPS.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);   
		SC_PROPS.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
	}

}
