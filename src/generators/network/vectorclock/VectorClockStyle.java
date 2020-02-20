package generators.network.vectorclock;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;

import algoanim.animalscript.addons.bbcode.Code;
import algoanim.animalscript.addons.bbcode.Plain;
import algoanim.animalscript.addons.bbcode.Style;
import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;

public class VectorClockStyle implements Style{
	
	private HashMap<String, AnimationProperties> map;
	
	public VectorClockStyle() {
		map = new HashMap<String, AnimationProperties>();
		
		// plain text properties
		SourceCodeProperties pp = new SourceCodeProperties();
		pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		pp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		map.put(Plain.BB_CODE, pp);
		
		// source code properties
		SourceCodeProperties plainProps = new SourceCodeProperties();
		plainProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		plainProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		map.put(Code.BB_CODE, plainProps);
	}

	@Override
	public AnimationProperties getProperties(String primitive) {
		return map.get(primitive);
	}
	
}