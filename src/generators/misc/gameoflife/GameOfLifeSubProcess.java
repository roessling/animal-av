package generators.misc.gameoflife;

import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import algoanim.util.Coordinates;

public class GameOfLifeSubProcess {
	
	private final List<WorkItem> items;
	private final Iterator<WorkItem> worklist;
	
	private WorkItem currentMatrix;
	private int currentY;
	private int currentX;
	
	private int currentCount;
	
	private List<Coordinates> highlighted;
	
	private Color highlightedColor = Color.RED;
	private Color defaultColor = Color.WHITE;
	
	private boolean[][]b;
	
	/**
	 * Initialize a process with a list of game boards.
	 * 
	 * @param worklist list of game boards (matrices)
	 */
	public GameOfLifeSubProcess(List<WorkItem> worklist){
		this.worklist = worklist.iterator();
		this.items = worklist;
		
		// set default background color
		if (worklist.size()>0) {
			defaultColor = worklist.get(0).matrix.getBackgroundColor();
		}
		
	}
	
	/**
	 * @return current amount of cells alive (already visited) around the current cell
	 */
	public int getCurrentCount(){
		return currentCount;
	}
	
	public int getCurrentRow() {
		return currentMatrix.rowNumber;
	}
	
	public void moveAllTo(String direction, String moveType, Coordinates target){
		for(WorkItem item: items)
			item.matrix.moveTo(direction, moveType, target, null, GameOfLifeParallel.ZERO_DURATION);
	}
	
	public int getMaxNumberOfColons(){
		
		int max = 0;
		
		for(WorkItem item : items)
			if(item.matrix.getNrCols() > max)
				max = item.matrix.getNrCols();
		
		return max;
	}
	
	public int getMaxNumberOfRows(){
		
		int max = 0;
		
		for(WorkItem item : items)
			if(item.matrix.getNrRows() > max)
				max = item.matrix.getNrRows();
		
		return max;
	}
	
	/**
	 * This hides the old matrix and sets the next matrix to visible.
	 * 
	 * @return true if a next matrix exists
	 */
	public boolean nextMatrix(){
		
		if(currentMatrix != null){
			currentMatrix.matrix.hide();
		}
		
		if(worklist.hasNext()){
			
			currentMatrix = worklist.next();
			currentX = -1;

			currentMatrix.matrix.show();
			
			if(currentMatrix.matrix.getNrRows() == 3 || currentMatrix.rowNumber == currentMatrix.originalMatrixRows-1){
				currentY = 1;
			} else {
				currentY = 0;
			}
			currentCount = 0;
			return true;
			
			
		}
		return false;
	}
	
	public boolean nextCell(){
		if(currentX == currentMatrix.matrix.getNrCols())
			return false;
		
		//unhighlight old
		if(currentX != -1)
			currentMatrix.matrix.unhighlightCell(currentY, currentX, null, GameOfLifeParallel.DEFAULT_DURATION);
		
		currentX++;
		if(currentX != currentMatrix.matrix.getNrCols())
			currentMatrix.matrix.highlightCell(currentY, currentX, null, GameOfLifeParallel.DEFAULT_DURATION);
		
		currentCount = 0;
		return true;
		
	}
	
	public void initLookup(){
		highlighted = new LinkedList<Coordinates>();
		b = GameOfLifeParallel.boardToBoolean(currentMatrix.matrix);
	}
	
	/**
	 * This returns true if there is a top left item next to the current item. 
	 * Furthermore this method increases the current counter if the top left item
	 * next to the current item is alive.
	 * 
	 * @return true if there is a top left item next to the current item
	 */
	public boolean lookupLeftTop(){
		if ((currentY - 1) >= 0 && (currentY - 1) < currentMatrix.matrix.getNrRows() && (currentX - 1) >= 0 && (currentX - 1) < currentMatrix.matrix.getNrCols()) {
			
			Coordinates coordinate = new Coordinates(currentX-1, currentY-1); 
			
			if (b[coordinate.getY()][coordinate.getX()]){
				currentCount++;
			}
			
			//board.highlightElem(coordinate.getY(), coordinate.getX(), null, DEFAULT_DURATION);
			currentMatrix.matrix.setBackgroundColor(coordinate.getY(), coordinate.getX(), highlightedColor);
			highlighted.add(coordinate);
			return true;
		}
		return false;
	}
	
	/**
	 * This returns true if there is a upper top item next to the current item. 
	 * Furthermore this method increases the current counter if the upper top item
	 * next to the current item is alive.
	 * 
	 * @return true if there is a upper top item next to the current item
	 */
	public boolean lookupTop(){
		if ((currentY - 1) >= 0 && (currentY - 1) < currentMatrix.matrix.getNrRows() && (currentX) >= 0 && (currentX) < currentMatrix.matrix.getNrCols()) {
			
			Coordinates coordinate = new Coordinates(currentX,currentY-1); 
			
			if (b[coordinate.getY()][coordinate.getX()]){
				currentCount++;
			}
			
			//board.highlightElem(coordinate.getY(), coordinate.getX(), null, DEFAULT_DURATION);
			currentMatrix.matrix.setBackgroundColor(coordinate.getY(), coordinate.getX(), highlightedColor);
			highlighted.add(coordinate);
			return true;
		}
		return false;
	}
	
	/**
	 * This returns true if there is a top right item next to the current item. 
	 * Furthermore this method increases the current counter if the top right item
	 * next to the current item is alive.
	 * 
	 * @return true if there is a top right item next to the current item
	 */
	public boolean lookupRightTop(){
		if ((currentY - 1) >= 0 && (currentY - 1) < currentMatrix.matrix.getNrRows() && (currentX + 1) >= 0 && (currentX + 1) < currentMatrix.matrix.getNrCols()) {
			
			Coordinates coordinate = new Coordinates(currentX + 1, currentY-1); 
			
			if (b[coordinate.getY()][coordinate.getX()]){
				currentCount++;
			}
			
			//board.highlightElem(coordinate.getY(), coordinate.getX(), null, DEFAULT_DURATION);
			currentMatrix.matrix.setBackgroundColor(coordinate.getY(), coordinate.getX(), highlightedColor);
			highlighted.add(coordinate);
			return true;
		}
		return false;
	}
	
	/**
	 * This returns true if there is a right item next to the current item. 
	 * Furthermore this method increases the current counter if the right item
	 * next to the current item is alive.
	 * 
	 * @return true if there is a right item next to the current item
	 */
	public boolean lookupRight(){
		if ((currentY) >= 0 && (currentY) < currentMatrix.matrix.getNrRows() && (currentX + 1) >= 0 && (currentX + 1) < currentMatrix.matrix.getNrCols()) {
			
			Coordinates coordinate = new Coordinates(currentX + 1, currentY); 
			
			if (b[coordinate.getY()][coordinate.getX()]){
				currentCount++;
			}
			
			//board.highlightElem(coordinate.getY(), coordinate.getX(), null, DEFAULT_DURATION);
			currentMatrix.matrix.setBackgroundColor(coordinate.getY(), coordinate.getX(), highlightedColor);
			highlighted.add(coordinate);
			return true;
		}
		return false;
	}
	
	/**
	 * This returns true if there is a top down item next to the current item. 
	 * Furthermore this method increases the current counter if the top down item
	 * next to the current item is alive.
	 * 
	 * @return true if there is a top down item next to the current item
	 */
	public boolean lookupRightBottom(){
		if ((currentY+1) >= 0 && (currentY+1) < currentMatrix.matrix.getNrRows() && (currentX + 1) >= 0 && (currentX + 1) < currentMatrix.matrix.getNrCols()) {
			
			Coordinates coordinate = new Coordinates(currentX + 1, currentY+1); 
			
			if (b[coordinate.getY()][coordinate.getX()]){
				currentCount++;
			}
			
			//board.highlightElem(coordinate.getY(), coordinate.getX(), null, DEFAULT_DURATION);
			currentMatrix.matrix.setBackgroundColor(coordinate.getY(), coordinate.getX(), highlightedColor);
			highlighted.add(coordinate);
			return true;
		}
		return false;
	}
	
	/**
	 * This returns true if there is a bottom item next to the current item. 
	 * Furthermore this method increases the current counter if the bottom item
	 * next to the current item is alive.
	 * 
	 * @return true if there is a bottom item next to the current item
	 */
	public boolean lookupBottom(){
		if ((currentY+1) >= 0 && (currentY+1) < currentMatrix.matrix.getNrRows() && (currentX) >= 0 && (currentX) < currentMatrix.matrix.getNrCols()) {
			
			Coordinates coordinate = new Coordinates(currentX, currentY+1); 
			
			if (b[coordinate.getY()][coordinate.getX()]){
				currentCount++;
			}
			
			//board.highlightElem(coordinate.getY(), coordinate.getX(), null, DEFAULT_DURATION);
			currentMatrix.matrix.setBackgroundColor(coordinate.getY(), coordinate.getX(), highlightedColor);
			highlighted.add(coordinate);
			return true;
		}
		return false;
	}
	
	/**
	 * This returns true if there is a bottom left item next to the current item. 
	 * Furthermore this method increases the current counter if the bottom left item
	 * next to the current item is alive.
	 * 
	 * @return true if there is a bottom left item next to the current item
	 */
	public boolean lookupLeftBottom(){
		if ((currentY+1) >= 0 && (currentY+1) < currentMatrix.matrix.getNrRows() && (currentX-1) >= 0 && (currentX-1) < currentMatrix.matrix.getNrCols()) {
			
			Coordinates coordinate = new Coordinates(currentX-1, currentY+1); 
			
			if (b[coordinate.getY()][coordinate.getX()]){
				currentCount++;
			}
			
			//board.highlightElem(coordinate.getY(), coordinate.getX(), null, DEFAULT_DURATION);
			currentMatrix.matrix.setBackgroundColor(coordinate.getY(), coordinate.getX(), highlightedColor);
			highlighted.add(coordinate);
			return true;
		}
		return false;
	}
	
	/**
	 * This returns true if there is a left item next to the current item. 
	 * Furthermore this method increases the current counter if the left item
	 * next to the current item is alive.
	 * 
	 * @return true if there is a left item next to the current item
	 */
	public boolean lookupLeft(){
		if ((currentY) >= 0 && (currentY) < currentMatrix.matrix.getNrRows() && (currentX-1) >= 0 && (currentX-1) < currentMatrix.matrix.getNrCols()) {
			
			Coordinates coordinate = new Coordinates(currentX-1, currentY); 
			
			if (b[coordinate.getY()][coordinate.getX()]){
				currentCount++;
			}
			
			//board.highlightElem(coordinate.getY(), coordinate.getX(), null, DEFAULT_DURATION);
			currentMatrix.matrix.setBackgroundColor(coordinate.getY(), coordinate.getX(), highlightedColor);
			highlighted.add(coordinate);
			return true;
		}
		return false;
	}
	
	/**
	 * Set background color to default for all cells.
	 */
	public void clearLookup(){
		for(Coordinates coordinate : highlighted){
			currentMatrix.matrix.setBackgroundColor(coordinate.getY(), coordinate.getX(), defaultColor);
		}
	}
	
	public String getNextCellStatus(Set<Integer> g, Set<Integer> t){
		if(currentMatrix.matrix.getElement(currentY, currentX).equals(GameOfLifeParallel.CELL_ALIVE_SYMBOL)){
			
			if(t.contains(currentCount))
				return GameOfLifeParallel.CELL_DEAD_SYMBOL;
			else 
				return GameOfLifeParallel.CELL_ALIVE_SYMBOL;
			
		} else {
			
			if(g.contains(currentCount))
				return GameOfLifeParallel.CELL_ALIVE_SYMBOL;
			else
				return GameOfLifeParallel.CELL_DEAD_SYMBOL;
		}
	}
	
	public Coordinates getCoordinatesOfCurrentCell(){
		return new Coordinates(currentX, currentMatrix.rowNumber);
	}
}
