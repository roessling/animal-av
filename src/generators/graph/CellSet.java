package generators.graph;

import java.awt.Color;
import java.util.LinkedList;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntArray;
import algoanim.primitives.Square;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;


@SuppressWarnings("serial")
public class CellSet extends LinkedList<Cell>{

	static int counter = 0;
	private int number;
	
	private StringArray grafic;
	private ArrayProperties props = new ArrayProperties() {{
		set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, MazeKruskal.cellListHighlightColor);
	}};
	
	
	private Language lang = MazeKruskal.lang;
	private Coordinates position;
	private String id;
	
	public CellSet(String id){
		super();
		this.id = id;
		this.number = counter; 
		counter++;
	}
	
	public void draw(Coordinates position){
		grafic = lang.newStringArray(
				position, 
				this.toStringArray(), 
				id, 
				null,
				props);
		grafic.showIndices(false, null, null);
		this.position = position;
	}
	
	public String[] toStringArray(){
		String[] result = new String[this.size()];
		int counter = 0;
		for(Cell c: this){
			result[counter] = String.valueOf(c.getId());
			counter++;
		}		
		return result;
	}
	
	public void combine(CellSet cs){
		this.addAll(cs);
		for(Cell c: cs){
			c.setCellSet(this);
		}
		cs.getGrafic().hide();
		grafic.hide();
		draw(position);		
		
		
		
		for(int i = MazeKruskal.cellSets.size() - 1; i > cs.getNumber() && i > 0; i--){
			CellSet cs1 = MazeKruskal.cellSets.get(i);
			cs1.setPosition(new Coordinates(cs1.getPosition().getX(), cs1.getPosition().getY()-28));
			cs1.getGrafic().moveBy(null, 0, -28, null, null);
			cs1.setNumber(cs1.getNumber() - 1);
		}
		
		MazeKruskal.cellSets.remove(cs);
	}
	
	public void highlight(){
		grafic.highlightCell(0, this.size() - 1, null, null);
	}
	
	public void unhighlight(){
		grafic.unhighlightCell(0, this.size() - 1, null, null);
	}
	
	public StringArray getGrafic(){
		return grafic;
	}
	
	public Coordinates getPosition(){
		return position;
	}
	
	public void setPosition(Coordinates pos){
		this.position = pos;
	}
	
	public int getNumber(){
		return number;
	}
	
	public void setNumber(int n){
		this.number = n;
	}
	
	public String toString(){
		String result = "\n";
		for(Cell c: this){
			result = result + c.getId() + "  ";
		}
		result = result + "  \t\t" + position.toString();
		return result;
	}
	
}
