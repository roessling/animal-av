package generators.graph;

import java.awt.Color;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Square;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;


public class Wall {

	static int counter = 0;
	private int number;
	
	private String id;
	private Cell cell1;
	private Cell cell2;
	private Square grafic;
	private Text text;
	private Language lang = MazeKruskal.lang;
	private Node position;
	private int size;
	
	private boolean textVisible = MazeKruskal.showWallIndex;
	
	private SquareProperties props = new SquareProperties() {{
		set(AnimationPropertiesKeys.DEPTH_PROPERTY, 7);
		set(AnimationPropertiesKeys.COLOR_PROPERTY, MazeKruskal.wallColor);
		set(AnimationPropertiesKeys.FILL_PROPERTY, MazeKruskal.wallColor);
		set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	}};
	
	private TextProperties textProps = new TextProperties() {{
		set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
		set(AnimationPropertiesKeys.HIDDEN_PROPERTY, !MazeKruskal.showWallIndex);
		set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.ORANGE);
		set(AnimationPropertiesKeys.DEPTH_PROPERTY, 6);
	}};

	
	public Wall(String id, Cell cell1, Cell cell2, Node position, int size){
		this.cell1 = cell1;
		this.cell2 = cell2;
		this.id = id;
		this.position = position;
		this.size = size;
		this.number = counter; 
		counter++;
		draw();
		if(textVisible)
			text = lang.newText(new Offset(0, -7, grafic, AnimalScript.DIRECTION_C), "" + id, id + " Wall Number", null, textProps);

	}
	
	public void draw(){
		grafic = lang.newSquare(position, size, id, null, props);
	}
	

	public void highlightCells(){
		cell1.getGrafic().changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, MazeKruskal.cellHighlightColor, null, null);
		cell2.getGrafic().changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, MazeKruskal.cellHighlightColor, null, null);
		cell1.getGrafic().changeColor(AnimationPropertiesKeys.FILL_PROPERTY, MazeKruskal.cellHighlightColor, null, null);
		cell2.getGrafic().changeColor(AnimationPropertiesKeys.FILL_PROPERTY, MazeKruskal.cellHighlightColor, null, null);
		this.getGrafic().changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, MazeKruskal.wallHighlightColor, null, null);
		this.getGrafic().changeColor(AnimationPropertiesKeys.FILL_PROPERTY, MazeKruskal.wallHighlightColor, null, null);
	}
	
	public void unhighlightCells(){
		cell1.getGrafic().changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, MazeKruskal.connectedCellColor, null, null);
		cell2.getGrafic().changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, MazeKruskal.connectedCellColor, null, null);
		cell1.getGrafic().changeColor(AnimationPropertiesKeys.FILL_PROPERTY, MazeKruskal.connectedCellColor, null, null);
		cell2.getGrafic().changeColor(AnimationPropertiesKeys.FILL_PROPERTY, MazeKruskal.connectedCellColor, null, null);
	}
	
	public Square getGrafic(){
		return grafic;
	}
	
	public String getId(){
		return id;
	}
	
	public int getNumber(){
		return number;
	}

	public Cell getCell1() {
		return cell1;
	}

	public Cell getCell2() {
		return cell2;
	}

	public void destroy() {
		grafic.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, MazeKruskal.connectedCellColor, null, null);
		grafic.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, MazeKruskal.connectedCellColor, null, null);
	}

}
