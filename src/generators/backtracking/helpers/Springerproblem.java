package generators.backtracking.helpers;

import java.util.Collections;
import java.util.List;

import algoanim.primitives.generators.Language;
import util.AnimalScriptExtension;




public class Springerproblem {
	private static AnimalHelper helper = AnimalHelper.getInstance();;
	private static Springerproblem instance = new Springerproblem();
	private static Board board;
	
	public static Language generate(int rows, int cols, int startRow, int startCol) {

		Springerproblem sp = new Springerproblem();
		if(sp.solve(rows, cols, startRow, startCol)){
			helper.showSolved();
		}else{
			helper.showNoSolution();
		}
		
		return helper.getLanguage();
	}
	
	public static Springerproblem getInstance(){
		return instance;
	}
	
	public Board getBoard(){
		return board;
	}
	
	public Springerproblem(){
		Springerproblem.instance=this;
	}
	
	public boolean solve(int rows, int cols, int startRow, int startCol) {
		helper.reset();		
		board= new Board(rows, cols);
		Field start = new Field(startCol, startRow);
		helper.startSolve(board);
		return solve(board, board.getField(start), 0);
	}

	private boolean solve(Board board, Field field, int turn) {
		field.visit(turn);
		helper.visit(field, turn);
		if (turn == board.getCols() * board.getRows() - 1) {
			helper.solved(field);
			return true;
		}

		List<Field> fields = field.getNeighboors();
		Collections.sort(fields);
		helper.getNeighboors(fields);
		int counter = 0;
		for (Field neighboor : fields) {
			helper.highlightArray(counter, neighboor, fields);
			if (!neighboor.isVisited()) {
				helper.newField();
				if (solve(board, neighboor, turn + 1)) {
					helper.forSolved(field, counter);
					return true;
				}
				helper.unvisit(neighboor, field, counter);
				board.unvisit(neighboor);
		
			}
			counter++;
		}
		helper.noSolution();
		return false;
	}

	public AnimalScriptExtension getLanguage() {
		
		return helper.getLanguage();
	}

	public static void reset() {
		helper.reset();
	}

}
