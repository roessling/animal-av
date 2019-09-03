package generators.misc.hepersGlicko2;

import java.awt.Font;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Offset;
import animal.variables.VariableRoles;

public class VariableManager{
	
	private Map<String, Text> vars = new HashMap<String, Text>();
	private List<String> updatedVars = new ArrayList<String>();
	private Variables avars;
	
	private String prevVar = "hRect";
	private String firstInPrevRow = "hRect";
	private int vcount = 0;
	private Language lang;
	
	private final Font DEFAULT = new Font("SansSerif", Font.PLAIN, 14);
	private final Font BOLD = new Font("SansSerif", Font.BOLD, 14);

	
	public VariableManager(Language lang) {
		this.lang = lang;
		avars = lang.newVariables();
		
	}
	
	public Text add(String name, double init) {
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, DEFAULT);
		
		Offset offset;
		if (vcount % 3 == 0) { //only 3 vars per row
			offset = new Offset(0, 10, firstInPrevRow, "SW");
			if (vcount == 0) offset = new Offset(5, 25, firstInPrevRow, "SW");
			firstInPrevRow = "var_" + name;
		}
		else offset = new Offset(100, 0, prevVar, "NW");
		
		prevVar = "var_" + name;
		vcount++;
		
		
		Text text = lang.newText(offset, name + ": " + init, "var_" + name, null, tp);
		vars.put(name, text);
		
		if (name.endsWith("'")) {
			name = name.replaceAll("'", "new");
		}
		if (name.endsWith("*")) {
			name = name.replaceAll("\\*", "star");
		}
		
		avars.declare("double", "" + name, Double.toString(init), VariableRoles.CONTAINER.toString());
		
		return text;
	}
	
	public void update(String name, double newValue, boolean clearHighlight) {
		update(name, newValue, clearHighlight, "#####.##");
	}
	
	public void update(String name, double newValue, boolean clearHighlight, String format) {
		if (clearHighlight) clearHighlight();
		
		
		vars.get(name).setText(name + ": " + new DecimalFormat(format).format(newValue), null, null);
		vars.get(name).setFont(BOLD, null, null);		
		updatedVars.add(name);
		
		if (name.endsWith("'")) name = name.replaceAll("'", "new");
		if (name.endsWith("*")) name = name.replaceAll("\\*", "star");
		avars.set(name, Double.toString(newValue));
	}
	
	public void clearHighlight() {
		for (String s : updatedVars) {
			vars.get(s).setFont(DEFAULT, null, null);
		}
		updatedVars.clear();
	}
	
	public String getLastVar() {
		return firstInPrevRow;
	}
	
}
