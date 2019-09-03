package generators.misc.helpers;
import java.awt.Color;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.util.Coordinates;
import algoanim.util.Timing;

/**
 * Encapsulates the functionality of a grid.
 * Items can be added in form of grid items.
 * Functionality to print the grid in animal is also existent.
 * @author chollubetz
 *
 */
public class Grid {
	private GridItem[][] content = null;
	private int rows, columns;
	private Position position;
	private int radius, distance;
	
	/**
	 * Creates a new grid.
	 * @param rows the number of rows
	 * @param columns the number of columns
	 * @param position the position where the grid shall be drawn
	 * @param radius the radius of each grid item
	 * @param distance the distance between two grid items
	 */
	public Grid(int rows, int columns, Position position, int radius, int distance) {
		content = new GridItem[rows][columns];
		this.rows = rows;
		this.columns = columns;
		
		this.position = position;
		
		this.radius = radius;
		this.distance = distance;
		
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++)
				content[r][c] = new GridNormalItem(this);
	}
	
	/**
	 * Gets the north-east position.
	 * @return the north-east position
	 */
	public Position getNorthEastPosition() {
		return new Position(position.getX() + 2*radius*columns + (columns-1) * distance, position.getY());
	}
	
	/**
	 * Gets the south-west position.
	 * @return the south-west position
	 */
	public Position getSouthWestPosition() {
		return new Position(position.getX(), position.getY() + 2*radius*rows + (rows-1) * distance);
	}
	
	/**
	 * Places the special position 'source'.
	 * @param position the position to place the source
	 */
	public void placeSourceItem(GridItemPosition position) {
		content[position.row][position.column] = new GridSourceItem(this);
	}
	
	/**
	 * Places the special position 'train'.
	 * @param position the position to place the train
	 */
	public void placeTrainItem(GridItemPosition position) {
		content[position.row][position.column] = new GridTrainItem(this);
	}

	/**
	 * Places a wall.
	 * @param position the position to place a wall
	 */
	public void placeWallItem(GridItemPosition position) {
		content[position.row][position.column] = new GridWallItem(this);
		
	}
	
	/**
	 * Determines all the neighbors of a given grid item
	 * @param gridItem the grid item whose neighbors should be found
	 * @return the neighbors of the given grid item
	 */
	private Set<GridItem> findNeighborsOf(GridItem gridItem) {
		Set<GridItem> result = new HashSet<GridItem>();
		GridItemPosition itemsPosition = gridItem.getPosition();
		//N
		if (itemsPosition.row > 0)
			result.add(content[itemsPosition.row - 1][itemsPosition.column]);
		//S
		if (itemsPosition.row < rows - 1)
			result.add(content[itemsPosition.row + 1][itemsPosition.column]);
		//W
		if (itemsPosition.column > 0)
			result.add(content[itemsPosition.row][itemsPosition.column - 1]);
		//E
		if (itemsPosition.column < columns - 1)
			result.add(content[itemsPosition.row][itemsPosition.column + 1]);
		return result;
	}
	
	/**
	 * Determines all the visitable neighbors of a given grid item
	 * @param gridItem the grid item whose visitable neighbors should be found
	 * @return the visitable neighbors of the given grid item
	 */
	public Set<GridItem> findVisitableNeighborsOf(GridItem gridItem) {
		Set<GridItem> result = new HashSet<GridItem>();
		for (GridItem currentItem : findNeighborsOf(gridItem))
			if (currentItem.getValue() == 0 || currentItem.getValue() == GridItem.TRAIN)
				result.add(currentItem);
		return result;
	}
	
	/**
	 * Determines all the neighbors of a given grid item, whose value equals a given value.
	 * @param gridItem the grid item whose neighbors should be found
	 * @param value the value that the neighbors should equal
	 * @return the neighbors of the given grid item with the given value
	 */
	public Set<GridItem> findNeighborsOfWithValue(GridItem gridItem, int value) {
		Set<GridItem> result = new HashSet<GridItem>();
		for (GridItem currentItem : findNeighborsOf(gridItem))
			if (currentItem.getValue() == value)
				result.add(currentItem);
		return result;
	}
	
	/**
	 * Gets the source item.
	 * @return the source item
	 */
	public GridItem getSource() {
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++)
				if (content[r][c].getValue() == GridItem.SOURCE)
					return content[r][c];
		return null;
	}
	
	/**
	 * Gets the train item.
	 * @return the train item
	 */
	public GridItem getTrain() {
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++)
				if (content[r][c].getValue() == GridItem.TRAIN)
					return content[r][c];
		return null;
	}
	
	/**
	 * Draws the grid to the given language.
	 * @param l the language to draw to
	 */
	public void draw(Language l, CircleProperties cp, Color wallColor) {
		cp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++)
				(content[r][c]).setCircle(l.newCircle(new Coordinates(position.getX() + radius + c*(2*radius+distance), position.getY() + radius + r*(2*radius+distance)), radius, "gridItemR" + r + "C" + c, null, cp));
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++)
				if (content[r][c].getValue() == GridItem.WALL)
					content[r][c].getCircle().changeColor("fillColor", wallColor, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		
	}

	/**
	 * Returns the position of a given grid item.
	 * @param gridItem the grid item, whose position should be determined
	 * @return the position of the given grid item
	 */
	public GridItemPosition getPositionOf(GridItem gridItem) {
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++)
				if (content[r][c] == gridItem)
					return new GridItemPosition(r, c);
		return null;
	}
	
	/**
	 * Returns all the grid items in the grid.
	 * @return the grid items of the grid
	 */
	public List<GridItem> getItems() {
		List<GridItem> result = new LinkedList<GridItem>();
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++)
				result.add(content[r][c]);
		return result;
	}
	
	/**
	 * Hides the grid.
	 */
	public void hide() {
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++){
				if (content[r][c].getText() != null)
					content[r][c].getText().hide();
				content[r][c].getCircle().hide();
			}
	}
}
