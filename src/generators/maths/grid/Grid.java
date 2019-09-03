package generators.maths.grid;

import generators.maths.grid.GridCell;
import generators.maths.grid.GridProperty;

import java.awt.Color;
import java.awt.Point;

import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.util.Coordinates;


public class Grid {

	private Coordinates upperLeft;
	private int width;
	private int height;
	private int CellSize;
	private Language lang;
	
	private Text[] captionTop=null;
	private Text[] captionLeft=null;
	private Text[] captionRight=null;
	private Text[] captionBottom=null;	
	private GridCell[][] grid;
	GridProperty gridProperty;
	
	
	public Grid(Coordinates upperLeft, int width, int height, int CellSize, Language lang, GridProperty gridProperty) {
		this.upperLeft = upperLeft;
		this.width = width;
		this.height = height;
		this.CellSize = CellSize;
		this.lang = lang;

		if (gridProperty == null) {
			this.gridProperty = new GridProperty();
		} else {
			this.gridProperty = gridProperty;
		}
		
		
		
		draw();
	}
	
	private void draw() {	
		
		// Create cells
		grid = new GridCell[width][height];	
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Coordinates upperLeftTmp = new Coordinates(upperLeft.getX() + i*CellSize, upperLeft.getY() + j*CellSize);
				grid[i][j] = new GridCell(upperLeftTmp, CellSize, lang, gridProperty);
			}
		}
		
		// Make border
		if (gridProperty.getBorder()) {
			Coordinates lowerRight = new Coordinates(upperLeft.getX() + CellSize*width, upperLeft.getY() + CellSize*height);
			
			RectProperties rp = new RectProperties();
			rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
			rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, gridProperty.getBorderColor());
			lang.newRect(upperLeft, lowerRight, "",null, rp);
		}
		
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param label
	 */
	public void setLabel(int x, int y, String label) {
		grid[x][y].setLabel(label);
	}
	
	public void setNextLabelInRow(int y, String label) {
		for(int x=0; x<grid.length;x++){
			if(grid[x][y].getLabel() == null){
				grid[x][y].setLabel(label);
				return;
			}
		}
	}
	
	public void setNextLabelInColumn(int x, String label) {
		for(int y=0; y<grid[0].length;y++){
			if(grid[x][y].getLabel() == null){
				grid[x][y].setLabel(label);
				return;
			}
		}
	}
	
	public void highlightLastLabeledCellInRow(int y, Color color, int delay){
		for(int x=0; x<grid.length;x++){
			if(grid[x][y].getLabel() == null){
				grid[x-1][y].highlight(color, delay);
				return;
			}
		}
	}
	
	public void highlightLastLabeledCellInColumn(int x, Color color, int delay){
		for(int y=0; y<grid[0].length;y++){
			if(grid[x][y].getLabel() == null){
				grid[x][y-1].highlight(color, delay);
				return;
			}
		}
	}
	
	public void blinkLastLabeledCellInRow(int y, Color color, int delay){
		for(int x=0; x<grid.length;x++){
			if(grid[x][y].getLabel() == null){
				grid[x-1][y].blink(color, delay);
				return;
			}
		}
	}
	
	public void blinkLastLabeledCellInColumn(int x, Color color, int delay){
		for(int y=0; y<grid[0].length;y++){
			if(grid[x][y].getLabel() == null){
				grid[x][y-1].blink(color, delay);
				return;
			}
		}
	}
	
	
	public void highlightCell (int column, int row, Color color, int delay) {
		grid[column][row].highlight(color, delay);
	}
	
	public void highlightRow(int row, Color color, int delay) {
		for (int i = 0; i < width; i++) {
			grid[i][row].highlight(color, delay);
		}
	}
	
	public void highlightColumn(int column, Color color, int delay) {
		for (int j = 0; j < height; j++) {
			grid[column][j].highlight(color, delay);
		}
	}
	
	public void unhighlightCell (int column, int row, int delay) {
		grid[column][row].unhighlight(delay);
	}
	
	public void unhighlightRow(int row, int delay) {
		for (int i = 0; i < width; i++) {
			grid[i][row].unhighlight(delay);
		}
	}
	
	public void unhighlightColumn(int column, int delay) {
		for (int j = 0; j < height; j++) {
			grid[column][j].unhighlight(delay);
		}
	}
	
	public void unhighlightAll(int delay) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				grid[i][j].unhighlight(delay);
			}
		}
	}
	
	public void blinkCell (int column, int row, Color color, int delay) {
		grid[column][row].blink(color, delay);
	}
	
	
	public void blinkRow(int row, Color color, int delay) {
		for (int i = 0; i < width; i++) {
			grid[i][row].blink(color, delay);
		}
	}
	
	public void blinkColumn(int column, Color color, int delay) {
		for (int j = 0; j < height; j++) {
			grid[column][j].blink(color, delay);
		}
	}
	
	/**
	 * 
	 * @param captions
	 */
	public void setCaptionTop(String[] captions) {
		Point offset = gridProperty.getCaptionOffsetTop();
		if (captionTop == null) {
			captionTop = new Text[width];
		}
		
		for (int i = 0; i < width; i++) {
			Coordinates upperLeftTmp = new Coordinates(upperLeft.getX() + offset.x + i*CellSize, upperLeft.getY() - CellSize + offset.y);
			captionTop[i] = lang.newText(upperLeftTmp, captions[i], "", null, gridProperty.getCaptionProperty(CellSize, CellSize, captions[i]));
		}
	}
	
	/**
	 * 
	 * @param captions
	 */
	public void setCaptionLeft(String[] captions) {
		Point offset = gridProperty.getCaptionOffsetLeft();
		if (captionLeft == null) {
			captionLeft = new Text[height];
		}
		
		for (int j = 0; j < height; j++) {
			Coordinates upperLeftTmp = new Coordinates(upperLeft.getX() - CellSize + offset.x, upperLeft.getY() + j * CellSize + offset.y);
			captionLeft[j] = lang.newText(upperLeftTmp, captions[j], "", null, gridProperty.getCaptionProperty(CellSize, CellSize, captions[j]));
		}	
	}
	
	/**
	 * 
	 * @param captions
	 */
	public void setCaptionRight(String[] captions) {
		Point offset = gridProperty.getCaptionOffsetRight();
		if (captionRight == null) {
			captionRight = new Text[height];
		}
		
		for (int j = 0; j < height; j++) {
			Coordinates upperLeftTmp = new Coordinates(upperLeft.getX() + width * CellSize + offset.x, upperLeft.getY() + j * CellSize + offset.y);
			captionRight[j] = lang.newText(upperLeftTmp, captions[j], "", null, gridProperty.getCaptionProperty(CellSize, CellSize, captions[j]));
		}	
	}
	
	/**
	 * 
	 * @param captions
	 */
	public void setCaptionBottom(String[] captions) {
		Point offset = gridProperty.getCaptionOffsetBottom();
		if (captionBottom == null) {
			captionBottom = new Text[width];
		}
		
		for (int i = 0; i < width; i++) {
			Coordinates upperLeftTmp = new Coordinates(upperLeft.getX() + i*CellSize + offset.x, upperLeft.getY() + height * CellSize + offset.y);
			captionBottom[i] = lang.newText(upperLeftTmp, captions[i], "", null, gridProperty.getCaptionProperty(CellSize, CellSize, captions[i]));
		}
	}
	
}
