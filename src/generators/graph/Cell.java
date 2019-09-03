package generators.graph;

import java.awt.Color;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Square;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;


public class Cell {

	private int id;
	private CellSet myCellSet;
	private Square grafic;
	private Language lang = MazeKruskal.lang;
	private Node position;
	private int size;
	
	private boolean isVisible = MazeKruskal.showCellIndex;
	
	private SquareProperties props = new SquareProperties() {{
		set(AnimationPropertiesKeys.DEPTH_PROPERTY, 8);
		set(AnimationPropertiesKeys.COLOR_PROPERTY, MazeKruskal.disconnectedCellColor);
		set(AnimationPropertiesKeys.FILL_PROPERTY, MazeKruskal.disconnectedCellColor);
		set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	}};
	
	private TextProperties textProps = new TextProperties() {{
		set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
		set(AnimationPropertiesKeys.HIDDEN_PROPERTY, !MazeKruskal.showCellIndex);		
		set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.ORANGE);
		set(AnimationPropertiesKeys.DEPTH_PROPERTY, 7);
	}};
	
	public Cell(int id, Node position, int size){
		this.id = id;
		this.position = position;
		this.size = size;
		this.myCellSet = new CellSet("Cell List of Cell " + id);
		myCellSet.add(this);
		draw();		
		
		if(isVisible)	
			lang.newText(new Offset(0, -7, grafic, AnimalScript.DIRECTION_C), "" + id, id + " Cell Number", null, textProps);
	}
	
	public void draw(){
		grafic = lang.newSquare(position, size, "" + id, null, props);
	}
	
	
	public CellSet getCellSet(){
		return myCellSet;
	}
	
	public void setCellSet(CellSet cl){
		myCellSet = cl;
	}
	
	
	public Square getGrafic(){
		return grafic;
	}
	
	public int getId(){
		return id;
	}
	

	
	public boolean hasLeftNeighbor(int hsize){
		return (id % hsize) != 0;
	}
	
	public boolean hasTopNeighbor(int hsize){
		return id >= hsize;
	}
	
}
