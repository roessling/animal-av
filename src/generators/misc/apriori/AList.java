package generators.misc.apriori;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;

public class AList<T> {
	protected List<T> entries;
	protected List<Text> texts;
	protected Text title;
	protected Language lang;
	protected String ident;
	protected int count = 0;
	private Color clrHighlight;
	
	protected TextProperties propText;
	protected TextProperties propTitle;
	
	protected Node top;
	
	public AList(Language lang, String ident, Node top, 
			TextProperties propText, TextProperties propTitle, Color clrHighlight) {
		
		this.lang = lang;
		this.ident = ident;
		this.top = top;
		this.propText = propText;
		this.propTitle = propTitle;
		this.clrHighlight = clrHighlight;
		
		texts = new ArrayList<Text>();
		
		
		entries = new ArrayList<T>();
	}
	
	protected void init(Language lang, String ident, Node top, 
			TextProperties propText, TextProperties propTitle, Color clrHighlight) {
		
		this.lang = lang;
		this.ident = ident;
		this.top = top;
		this.propText = propText;
		this.propTitle = propTitle;
		this.clrHighlight = clrHighlight;
		
		texts = new ArrayList<Text>();
	}
	
	public void addEntry(T entry) {
		entries.add(entry);
		String text = cToString(entry);
		addEntryText(text);
	}
	
	protected void addEntryText(String text) {
		if(entries.size() <= texts.size()) {
			texts.get(entries.size()-1).setText(text,
					null, null);
		} else if(entries.size() == 1){
			texts.add(addTextLine(text));
			count++;
		} else {
			texts.add(addTextLine(text));
		}
	}
	
	private Text addTextLine(String text) {
		return addTextLine(text, propText);
	}
	
	private Text addTextLine(String text, TextProperties propText) {
		Text result;
		if(entries.size() == 1)
			result = lang.newText(top, text, ident+count, null, propTitle);
		else 
			result = lang.newText(new Offset(0, 7, texts.get(texts.size()-1).getName(), 
					AnimalScript.DIRECTION_SW),text, ident+count, null, propText);
		count++;
		
		return result;
	}
	
	public void removeEntry(int entry) {
		entries.remove(entry);
		for(int i = entry; i < entries.size(); i++) {
			String newText;
			if(i == entries.size())
				newText = "";
			else
				newText = texts.get(entry+1).getText();
			
			texts.get(entry).setText(newText, null, null);
		}
	}
	
	protected String cToString(T in) {
		return in.toString();
	}
	
	public void setEntry(int entry, T newCont) {
		entries.set(entry, newCont);
		texts.get(entry).setText(cToString(newCont), null, null);
	}
	
	public void highlight(int line) {
		texts.get(line).changeColor(AnimalScript.COLORCHANGE_COLOR, 
				clrHighlight, null, null);
	}
	
	public void unhighlight(int line) {
		texts.get(line).changeColor(AnimalScript.COLORCHANGE_COLOR, 
				Color.BLACK, null, null);
	}
	
	public void disableEntry(int line) {
		texts.get(line).changeColor(AnimalScript.COLORCHANGE_COLOR, 
				Color.LIGHT_GRAY, null, null);
	}
	
	public Node getBottom () {
		if(entries.size() == 0)
			return top;
		else
			return new Offset(0, 20, texts.get(entries.size()-1).getName(), 
					AnimalScript.DIRECTION_SW);
	}
	
	public void hide() {
		for(Text text : texts) {
			text.hide();
		}
	}
}
