package generators.helpers;
import java.awt.Color;
import java.awt.Font;

import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;


public class DiskProperties {

		private RectProperties rectProps = new RectProperties();
		private TextProperties textProps = new TextProperties();
		
		
		public DiskProperties() {
			// default values
			rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
			rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
			rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
			textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, 
						  new Font("SansSerif", Font.BOLD, 10));	
	
		}
		
		public DiskProperties(RectProperties rp){
			rectProps = rp;
			textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, 
					  new Font("SansSerif", Font.BOLD, 10));
		}
		
		public DiskProperties(Color fillProperty, boolean filledProperty, String fontName, int stil){
			
			rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, fillProperty);
			rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, filledProperty);
			rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
			textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, 
						  new Font(fontName, stil, 10));
		}
		
		public RectProperties getRectProperties(){
			return rectProps;
		}
		
		public TextProperties getTextProperties(){
			return textProps;
		}
		
		public void setFillProperty(Color color){
			rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, color);
		}
		
		public void setFilledProperty(boolean b){
			rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, b);
		}
		
		public void setTextColor(Color color){
			
		}

		
}
