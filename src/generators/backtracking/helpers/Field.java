package generators.backtracking.helpers;


import java.util.ArrayList;
import java.util.List;



public class Field implements Comparable<Field> {
	private boolean visited = false;
	private int possibleMoves = 0;
	private List<Field> neighboors = new ArrayList<Field>();	
	private int turn = -1;
	private int x;
	private int y;
	
	public Field(int x, int y){
		this.x=x;
		this.y=y;
		visited=false;
	}
	
	@Override
	public int compareTo(Field other) {
		if(this.visited && !other.visited)
			return 1;
		if(!this.visited && other.visited)
			return -1;
		return this.getPossibleMoves()-other.getPossibleMoves();
	}
	
	
	public void visit(int turn){
		this.setVisited(true);
		this.turn=turn;
		for(Field field : this.getNeighboors()){
			field.decPossibleMoves();
		}
	}
	
	public void unvisit(){
		this.turn=-1;
		this.setVisited(false);
		for(Field field : this.getNeighboors()){
			field.incPossibleMoves();
		}
	}
	
	/**
	 * @return the neighboor fields
	 */
	public List<Field> getNeighboors(){
		return neighboors;
	}
	
	
	/**
	 * @param neighboors the neighboors to set
	 */
	public void setNeighboors(List<Field> neighboors) {
		this.possibleMoves=neighboors.size();
		this.neighboors = neighboors;
	}


	public int getPossibleMoves(){
		return possibleMoves;
	}
	
	public void decPossibleMoves(){
		AnimalHelper.getInstance().fieldUpdated();
		possibleMoves--;
	}
	
	public void incPossibleMoves(){
		AnimalHelper.getInstance().fieldUpdated();
		possibleMoves++;
	}
	/**
	 * @return the visited
	 */
	public boolean isVisited() {
		return visited;
	}

	/**
	 * @param visited the visited to set
	 */
	private void setVisited(boolean visited) {
		AnimalHelper.getInstance().fieldUpdated();
		this.visited = visited;
	}

	/**
	 * @return the turn
	 */
	public int getTurn() {
		return turn;
	}
	@Override
	public String toString(){
		StringBuilder string = new StringBuilder();
		string.append("(").append(this.getY()).append(", ").append(this.getX()).append(")");
		return string.toString();
	}
		
	@Override
	/**
	 * calculates the hashCode using x ^ y
	 */
	public int hashCode(){
		return getX() ^ getY();
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj == this) return true;
		if(obj == null) return false;
		if(obj instanceof Field){
			Field other = (Field) obj;
			return other.getY()==this.getY() && other.getX()==this.getX() && this.getClass().equals(other.getClass());
		}
		return false;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
}
