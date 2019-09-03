package generators.sorting.bogosort;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;

public class InfoItem {
	private String name;
	private Integer value;
	private Node position;
	private Text vName;
	private Text vValue;
	private TextProperties textProperties;
	private Language lang;
	private int gapSpace = 10;

	public InfoItem(Language lang, String name, TextProperties textProperties) {
		this.lang = lang;
		this.name = name;
		this.textProperties = textProperties;
	}
	
	public InfoItem(Language lang, String name, Integer value, TextProperties textProperties) {
		this(lang, name, textProperties);
		this.value = value;
	}
	
	public void updateValue(Integer value) {
		this.value = value;
		update();
	}
	
	public void increaseBy(Integer amount) {
		this.value += amount;
		update();
	}
	public void decreaseBy(Integer amount) {
		this.value -= amount;
		update();
	}
	
	private void update(){
		this.vValue.setText(String.valueOf(value), null, null);
	}
	
	public void increase() {
		this.increaseBy(1);
	}
	
	public void decrease() {
		this.decreaseBy(1);
	}
	
	public String getName() { return this.name; }
	public Integer getValue() { return this.value; }
	
	public Text getVName() { return this.vName; }
	public Text getVValue() { return this.vValue; }
	
	public void place(Node position) {
		this.position = position;
		this.vName = lang.newText(position, this.name+": ", this.name, null,this.textProperties);
		this.vValue = lang.newText(new Offset(gapSpace,0,this.vName,AnimalScript.DIRECTION_NE), String.valueOf(this.value), this.name + "_value", null,this.textProperties);
	}

	public void show() {
		this.vName.show();
		this.vValue.show();
	}
	
	public Node getPosition() { return this.position; }
}
