package generators.misc.processScheduling;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class StringRec {
	
	private Font font = new Font("SansSerif", Font.PLAIN, 16);
	
	private final String id;
	private Language lang;
	private Text text;
	
	private Rect rect;
	
	public StringRec(String id, Language lang){
		this.id = id;
		this.lang = lang;
	}
	
	public Rect getRec(){
		return rect;
	}
	
	public Text getText(){
		return text;
	}
	
	public Rect createRec(Node offset1, Node offset2){
		RectProperties prop = new RectProperties();
		prop.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		prop.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		if(offset1!=null && offset2!=null){
			rect = lang.newRect(offset1, offset2, "StringRec_Rec_"+id, null, prop);
		}
		return rect;
	}
	
	public Text createText(String textString){
		TextProperties prop = new TextProperties();
		prop.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
		prop.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
//		Font f = (Font)prop.get(AnimationPropertiesKeys.FONT_PROPERTY);
		lang.newText(new Coordinates(0, 0), "", "TEMP", null, prop);
		text = lang.newText(new Offset(0, 0-font.getSize()/2-2, getRec().getName(), AnimalScript.DIRECTION_C), textString, "StringRec_String_"+id, null, prop);
		return text;
	}
	
	public void createNewProcessLine(){
		RectProperties prop = new RectProperties();
		prop.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
		prop.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		lang.newRect(new Offset(-3, 0, getRec().getName(), AnimalScript.DIRECTION_NW), new Offset(+3, 0, getRec().getName(), AnimalScript.DIRECTION_SW), "StringRec_RecPL_"+id, null, prop);
	}
	
	public void changeText(String text){
		this.text.setText(text, null, null);
	}
	
	public void changeColor_Text(Color color){
		text.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, color, null, null);
	}
	
	public void changeColor_RectLinien(Color color){
		rect.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, color, null, null);
	}
	
	public void changeColor_RectFill(Color color){
		rect.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, color, null, null);
	}
	
}
