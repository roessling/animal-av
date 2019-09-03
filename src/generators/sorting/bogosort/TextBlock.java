package generators.sorting.bogosort;

import java.awt.Color;
import java.util.LinkedList;
import java.util.UUID;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;

public class TextBlock {
	Language lang;
	TextProperties textProperties;
	LinkedList<Text> lines;
	Node position;
	boolean hidden = false;
	
	public TextBlock(Language lang, Node pos, TextProperties textProperties) {
		this.lang = lang;
		this.textProperties = textProperties;
		this.lines = new LinkedList<Text>();
		this.position = pos;
	}

	public void addLine(String line) {
		if(this.lines.size() == 0) 
			newLine(this.position,line);
		else
			newLine(new Offset(0, 5, this.lines.getLast(), AnimalScript.DIRECTION_SW),line);		
	}
		
	private void newLine(Node position, String line) {
		Text ret = lang.newText(position, line, UUID.randomUUID().toString(), null,
				textProperties);
		if (hidden)
			ret.hide();
		this.lines.add(ret);
	}
	

	public void addText(String text) {
		String[] lines = text.split("\n");
		for(String line : lines) {
			this.addLine(line);
		}
	}
	
	public void hide() { this.hide(null); }
	public void hide(Timing delay) {
		for(Text t : lines) 
			t.hide(delay);
	}
	
	public void show() { this.show(null); }
	public void show(Timing delay) {
		for(Text t : lines) 
			if(delay == null)
				t.show();
			else
				t.show(delay);
	}
	
	public void setColor( Color newColor) {
		this.textProperties.set("color", newColor);
	}
}
