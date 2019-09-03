package generators.backtracking.helpers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Board {
	
	private FieldMap<Field, Field> board = new FieldMap<Field, Field>();
	private int rows;
	private int cols;
	private int[] xmoves  = {-1, 1,  -2, 2, -2, 2, -1,  1};
	private int[] ymoves  = { -2, -2, -1, -1, 1, 1, 2, 2};
	private LinkedList<Field> moves = new LinkedList<Field>();
	public Board(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		calcNeighboors();
	}
	
	public void calcNeighboors(){
		for(int row=0; row<getRows();row++){
			for(int col =0; col<getCols(); col++){
				Field field = getBoard().getField(col, row);
				List<Field> neighboors = new ArrayList<Field>();
				for(int m=0; m<8;m++){
					int newX = col+xmoves[m];
					int newY = row+ymoves[m];
					if(inField(newX, newY)){
						neighboors.add(getBoard().getField(newX , newY));
					}
				}
				field.setNeighboors(neighboors);
			}
		}
	}
	private boolean inField(int x, int y) {
		if(y < 0 || y >= getRows() 
		|| x < 0 || x >= getCols()){
			return false;
		}
		return true;
	}

	/**
	 * @return the fields
	 */
	public FieldMap<Field, Field> getBoard() {
		return board;
	}

	public Field getField(Field coord){
		return board.get(coord);
	}
	/**
	 * @return the rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * @return the cols
	 */
	public int getCols() {
		return cols;
	}

	public void visit(Field coord, int turn) {
		getBoard().getField(coord).visit(turn);
		this.moves.add(coord);
		
	}
	
	public void unvisit(Field coord) {
		getBoard().getField(coord).unvisit();
		if(this.moves.size()>0)
			this.moves.removeLast();
	}

	public void print() {
		StringBuffer out = new StringBuffer();
		for(int r =0; r<this.rows; r++){
			for(int c = 0; c<this.cols; c++){
				int turn = getBoard().getField(c, r).getTurn();
				if(turn>-1)
					out.append(turn).append("|");
				else
					out.append(" |");
			}
			out.append("\n");
		}
		System.out.println(out);
	}
	
	public void possibleMoves() {
		StringBuffer out = new StringBuffer();
		for(int r =0; r<this.rows; r++){
			for(int c = 0; c<this.cols; c++){
				int possibleMoves = getBoard().getField(new Field(c, r)).getPossibleMoves();
				if(possibleMoves>-1)
					out.append(possibleMoves).append("|");
				else
					out.append(" |");
			}
			out.append("\n");
		}
		System.out.println(out);
	}

	public int[][] getDataArray() {
		int[][] data = new int[rows][cols];
		for(int r =0; r<rows; r++){
			for(int c=0; c<cols; c++){
				data[r][c]=getBoard().get(new Field(c, r)).getTurn();
			}
		}
		return data;
	}
	
	public List<Field> getMoves(){
		return moves;
	}
}
