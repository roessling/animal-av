package generators.compression.helpers;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Primitive;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Offset;

public class Element {
	
	private char name;
	private int count;
	private String code = new String();
	private int index;

	
	public void increaseCount() {
		count++;
	}
	
	public Element(char name) {
		this.name = name;
	}
	
	public Element(String name) {
		this.name = name.charAt(0);
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getCode() {
		return code;
	}

//	public void setCode(String code) {
//		this.code = code;
//	}

	public String searializeToAnim(int i) {
		this.index = i;
		index++;
		return "setGridValue \"grid1["+index+"][0]\" \""+name+"\" refresh \n" +
				"setGridValue \"grid1["+index+"][1]\" \""+count+"\" refresh \n" +
				"setGridValue \"grid1["+index+"][2]\" \""+code+"\" refresh \n";

		
	}
	
	public String searializeToAnim() {
		return "setGridValue \"grid1["+index+"][0]\" \""+name+"\" refresh \n" +
				"setGridValue \"grid1["+index+"][1]\" \""+count+"\" refresh \n" +
				"setGridValue \"grid1["+index+"][2]\" \""+code+"\" refresh \n";

	}
	
		
	
	private Circle circle;
	
	
	private Text text1;
	
	private Text text2;
	
	public Circle getCircle() {
		return circle;
	}
	
	public Primitive serializeAsList(Language lang, Primitive prev) {
		CircleProperties props = new CircleProperties();
		props.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		props.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(192, 192,
				192));
		
		circle = lang.newCircle(new Offset(30, 20, prev, AnimalScript.DIRECTION_NE), 20, Integer.toString(this.hashCode()), null, props);
		
		text1 = lang.newText(new Offset(14, 10, circle, AnimalScript.DIRECTION_NW), this.toString(), this.toString()+"_list", null);
		
		text2 = lang.newText(new Offset(14, -20, circle, AnimalScript.DIRECTION_NW), Integer.toString(this.count), this.toString()+"_freq", null);
		
		return circle;

	}

	public char getName() {
		return name;
	}

	public void setName(char name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	
	public void addNill() {
		code =code+"0";
		
	}
	
	public void addOne() {
		code =code+"1";
	}
	
	
	public void showNeural() {
		circle.changeColor("fillColor", new Color(192, 192,
				192), null, null);
	}
	
	

	
	public Text showS1Title(Language lang) {
		TextProperties listsProp = new TextProperties();
		listsProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 15));
		
		
		return lang.newText(new Offset(-30,
				53, circle, AnimalScript.DIRECTION_NE), "S1", "S1_t"+this.hashCode(), null, listsProp);

	}
	
	
	public Text showS2Title(Language lang) {
		TextProperties listsProp = new TextProperties();
		listsProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 15));
		
		return lang.newText(new Offset(-30,
				50, circle, AnimalScript.DIRECTION_NE), "S2", "S2_t"+this.hashCode(), null, listsProp);
	}
	
	public void showS1() {
		
		
		circle.changeColor("fillColor",Color.cyan, null, null);
		//circle.getProperties().
		//props.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(23, 200, 234));
	}
	
	public void showS2() {
		circle.changeColor("fillColor",Color.MAGENTA, null, null);
		//props.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(253, 132, 135));
	}
	
	
	@Override
	public String toString() {
		return Character.toString(this.name).toUpperCase();
	}
	
	public String getOutput() {
		return this.toString()+"("+this.code+")";
	}
	
	public void hideAll() {
		this.circle.hide();
		this.text1.hide();
		this.text2.hide();
	}
	
}
