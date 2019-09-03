package generators.sorting.bogosort;

import java.util.LinkedList;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;

public class InfoBox {
	
	private Language lang;
	private TextProperties textProperties;
	
	private Node upperLeft;
	private int lineSpace = 2;
	
	private LinkedList<InfoItem> items;

	public InfoBox(Language lang, TextProperties textProperties) {
		this.lang = lang;
		this.textProperties = textProperties;
		this.items = new LinkedList<InfoItem>();
	}
	
	public InfoItem addInfo(String name) {return this.addInfo(name, 0); }
	public InfoItem addInfo(String name, Integer startValue) {
		InfoItem newItem = new InfoItem(lang,name,startValue,textProperties);
		this.items.add(newItem);
		return newItem;
	}
	
	public void place(Node position) {
		this.upperLeft = position;
		Node curPos = upperLeft;
		for(int i = 0;i<items.size();i++) {
			items.get(i).place(curPos);
			curPos = new Offset(0, lineSpace,items.get(i).getVName(),AnimalScript.DIRECTION_SW);
		}
	}
	
	public void show() {
		for(InfoItem i : items)
			i.show();
	}

}
