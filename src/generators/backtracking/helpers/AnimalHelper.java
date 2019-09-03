package generators.backtracking.helpers;


import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.List;

import util.AnimalScriptExtension;
import util.SourceCodeExtended;
import algoanim.counter.model.FourValueCounter;
import algoanim.counter.view.FourValueView;
import algoanim.primitives.StringArray;
import algoanim.primitives.Variables;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class AnimalHelper {
	private static AnimalHelper instance = null;
	public static AnimalHelper getInstance() {
		if (instance == null)
			instance = new AnimalHelper();
		return instance;
	}
	
	
	private LinkedList<Field> moves = new LinkedList<Field>();
	private boolean knightCreated = false;
	private AnimalScriptExtension l;
	private SourceCodeExtended sourceSolve = null;
	private StringArray fieldsArr;
	private final String sourceSolveString = "private boolean solve(Board board, Field field, int turn) {"
			+ "\n" + // 0
			"	field.visit(turn);" + "\n" + // 1
			"	if(turn==board.getCols()*board.getRows()-1){" + "\n" + // 2
			"		return true;" + "\n" + // 3
			"	}" + "\n" + // 4
			"	List<Field> fields = field.getNeighboors();" + "\n" + // 5
			"	Collections.sort(fields);" + "\n" + // 6
			"	for(Field neighboor : fields){" + "\n" + // 7
			"		if(!neighboor.isVisited()){" + "\n" + // 8
			"			if(solve(board, neighboor, turn+1)){" + "\n" + // 9
			"				return true;" + "\n" + // 10
			"			}" + "\n" + // 11
			"			board.unvisit(neighboor.getCoords());" + "\n" + // 12
			"		}" + "\n" + // 13
			"	}" + "\n" + // 14
			"	return false;" + "\n" + // 15
			"}"; // 16
	private List<Field> highlighted = new LinkedList<Field>();
	private FourValueCounter movesCounter;
	private FourValueView counterView;
	private Variables variables;
	private boolean declared=false;
	private AnimalHelper() {
		reset();
	}
	public void reset(){
		l = new AnimalScriptExtension("Springerproblem", "Eric Brüggemann, Mohit Makhija", 720, 480, new AnimalStyling());
		l.setStepMode(true);
		sourceSolve = null;
		moves = new LinkedList<Field>();
		knightCreated = false;
		sourceSolve = null;
		declared = false;
		fieldsArr=null;
		variables = l.newVariables();
		if(AnimalStyling.props != null)
			init();
	}
	
	private void init(){
		l.newText("Springerproblem", "header");

		// rectangle "hRect" offset (-5, -5) from "header" NW offset (5, 5) from
		// "header" SE depth 2 filled fillColor white
		l.newRect("headerBox");
		l.newText("Beschreibung des Algorithmus", "descriptionHeader");
		l.addMultiLineText(
				"Bei dem Springerproblem geht es um das Problem, das darin besteht, für einen Springer\n"
						+ "auf einem (Schach-)Brett der Größe n x m unter Einhaltung seiner Zugregeln eine Route zu finden,\n"
						+ "auf der er jedes Feld genau einmal besucht.\n\n"
						+ "Da das Springerproblem ein Spezialfall des Hamiltonpfadproblems ist, welches NP-vollständig ist,\n"
						+ "ist kein effizienter Lösungsalgorithmus bekannt. Eine Möglichkeit das Springerproblem zu lösen\n"
						+ "ist daher ein einfaches Backtracking-Verfahren anzuwenden.\n"
						+ "\n"
						+ "Im Laufe der Zeit gab es viele Ansätze das Springerproblem effizienter zu lösen.\n"
						+ "Die Warnsdorffregel ist eine sehr einfache und sehr effiziente Möglichkeit den Algorhitmus zu optimieren.\n"
						+ "Hier wird immer das Feld als nächstes besucht, welches die wenigsten noch unbesuchten Nachbarn hat.",
				"description");
		l.nextStep();
		
	
		this.addSolveSource();
		createVarGrid();
	}
	public void initCounters(){
		this.movesCounter = new FourValueCounter();
		CounterProperties cp = (CounterProperties) AnimalStyling.getPropsByName("counter");
		String[] names = {"checked fields    ", "moves", "moves undone", "fields updated\\*"};
		counterView = l.newCounterView(movesCounter, new Offset(0, 0, "solutionBoard", "SW"), cp, true, true, names);
		l.addMultiLineText("*Bei jedem Zug wird das aktuelle Feld als besucht markiert\n" +
				"und bei allen Nachbarn der Zähler für unbesuchte Nachbarn um 1 verringert.", "fieldsUpdatedDesc");
	}
	public void addSolveSource() {
		if (this.sourceSolve == null) {
			SourceCodeProperties scProps = (SourceCodeProperties) AnimalStyling.getPropsByName("sourceCode");
			this.sourceSolve = l.newSourceCode(
					new Offset(0,10,"headerBox", "SW"), "solveSource", null,
					scProps);
			this.sourceSolve.addMultilineCode(this.sourceSolveString,
					"sourceSolve", null);
			//this.sourceSolve.show();
		} else {
			this.sourceSolve.show();
		}
	}

	public void createBoard(Board board) {

		int cols = board.getCols();
		int rows = board.getRows();
		// Grid grid = new Grid(new Coordinates(80, 0), cols, rows, 25, l, gp);
		// l.newIntMatrix(board.getDataArray(), "neighboorsMatrix");
		l.addLine("grid "
				+ "\"solutionBoard\""
				+ " offset (20, 0) from \"solveSource\" NE lines "
				+ rows
				+ " columns "
				+ cols
				+ " style table cellWidth 25 cellHeight 25 fillColor white borderColor black highlightBorderColor black align left depth 3 after 20 ticks");
		initCounters();
	}

	private void createVarGrid() {
		l.addLine("grid \"vars\" offset (0, 30) from \"solveSource\" SW lines 4 columns 2 style plain fillColor white font Monospaced align left");
		l.addLine("setGridFont \"vars[0][]\" bold Monospaced refresh");
		l.addLine("setGridValue \"vars[0][0]\" \"variable\" refresh after 20 ticks");
		l.addLine("setGridValue \"vars[0][1]\" \"value\"  refresh after 20 ticks");
	}

	public void forSolved(Field field, int turn) {
		//l.addLine("setGridValue \"vars[1][0]\" \"field\" refresh after 20 ticks");
		//l.addLine("setGridValue \"vars[1][1]\" \"(" + field.getX() + ", "+ field.getY() + ")\"  refresh after 20 ticks");
		//setGridCellValue("vars", 0, 2, "turn");
		//
		//l.addLine("setGridValue \"vars[2][1]\" \"" + field.getTurn() + "\"  refresh after 40 ticks");
                variables.set("field", field.toString());
		variables.set("turn", turn+"");
		highlightGridCell("solutionBoard", field.getX(), field.getY());
		sourceSolve.highlight(10);
		l.nextStep();
		sourceSolve.unhighlight(10);

	}

	public AnimalScriptExtension getLanguage() {
		return l;
	}

	public void getNeighboors(List<Field> fields) {

		/*this.highlighted.addAll(fields);
		String[] fieldsStr = new String[fields.size()];
		int i=0;
		for (Field field : fields) {
			fieldsStr[i++] = "(" + field.getX() + ", "+ field.getY() + ")";
			highlightGridCell("solutionBoard", field.getX(), field.getY());
		}*/

		sourceSolve.highlight(5);
		l.nextStep();
		sourceSolve.unhighlight(5);

		sourceSolve.highlight(6);
                if (fields.isEmpty())
                    l.nextStep();
                    sourceSolve.unhighlight(6);
		/*l.addLine("setGridValue \"vars[1][0]\" \"fields\" refresh");

		ArrayProperties ap = new ArrayProperties();
		ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		ap.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.MONOSPACED,
				Font.PLAIN, 16));
		ap.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);

		fieldsArr = l.newStringArray(new Offset(74, 28, "vars", "NW"),
				fieldsStr, "fields", null, ap);*/



	}

	public void highlightArray(int counter, Field neighboor, List<Field> fields) {
                if (fieldsArr != null)
			fieldsArr.hide();
                this.highlighted.addAll(fields);
		String[] fieldsStr = new String[fields.size()];
		int i=0;
		for (Field field : fields) {
			fieldsStr[i++] = "(" + field.getX() + ", "+ field.getY() + ")";
			highlightGridCell("solutionBoard", field.getX(), field.getY());
		}
                
                l.addLine("setGridValue \"vars[1][0]\" \"fields\" refresh");
		
		ArrayProperties ap = new ArrayProperties();
		ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		ap.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.MONOSPACED,
				Font.PLAIN, 16));
		ap.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);

		fieldsArr = l.newStringArray(new Offset(74, 28, "vars", "NW"),
				fieldsStr, "fields", null, ap);
                
                l.nextStep();
		sourceSolve.unhighlight(6);
		this.movesCounter.assignmentsInc(1);
		sourceSolve.highlight(7);
		highlightGridCell("solutionBoard", neighboor.getX(), neighboor.getY());
		
		
		if (counter > 0)
			fieldsArr.unhighlightCell(counter - 1, Timing.INSTANTEOUS,	Timing.INSTANTEOUS);
		fieldsArr.highlightCell(counter, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		
		l.nextStep();
		sourceSolve.unhighlight(7);

		sourceSolve.highlight(8);
		l.nextStep();
		sourceSolve.unhighlight(8);
	}

	public void highlightGridCell(String name, int x, int y) {
		l.addLine("highlightGridCell \""+ name +"[" + y + "][" + x
				+ "]\" within 50 ticks");
	}

	public void moveKnight(Field target, int turn) {
		this.moves.add(target);
		setGridCellValue("solutionBoard", target.getX(), target.getY(), turn + "");

		if (knightCreated) {
			int y = target.getY() * 33;
			int x = target.getX() * 33;
			StringBuffer out = new StringBuffer();
			out.append("move ").append("\"knight\"").append(" ")
					.append("type \"translate\" ").append("to offset(")
					.append(x).append(", ").append(y).append(")")
					.append("from ").append("\"solutionBoard\"").append(" ")
					.append("NW after 10 ticks within 50 ticks");
			l.addLine(out.toString());
		} else {
			int y = target.getY() * 33 + 33 - 16;
			int x = target.getX() * 33 + 33 - 16;
			knightCreated = true;
			l.newCircle(new Offset(x, y, "solutionBoard", "NW"), 16, "knight", new TicksTiming(40), (CircleProperties) AnimalStyling.getPropsByName("knight"));
		}
	}

	public void newField() {
		sourceSolve.highlight(9);
		l.nextStep();

		sourceSolve.unhighlight(9);
	}

	public void nextStep() {
		l.nextStep();
	}

	public void noSolution() {
		sourceSolve.highlight(15);
		l.nextStep();
		sourceSolve.unhighlight(15);
	}

	public void setGridCellValue(String name, int x, int y, String label) {
		l.addLine("setGridValue \""+ name +"[" + y + "][" + x + "]\" \"" + label + "\"");
	}

	public void showNoSolution() {
		l.nextStep();
		l.hide("solveSource", "vars" , "fieldsUpdatedDesc1", "fieldsUpdatedDesc2");
		counterView.hide();
		l.showInThisStep.addElement("header");
		l.showInThisStep.addElement("headerBox");
		l.addMultiLineText("Es konnte keine Lösung gefunden werden.\n"+
		"Da wir alle Möglichkeiten getestet haben, ist dieses Spielfeld\n" +
		"mit diesem Startfeld nicht lösbar.\n", "conclusion");
		
		
	}

	public void showSolved() {
		l.nextStep();
		l.hide("solveSource", "vars" , "fieldsUpdatedDesc1", "fieldsUpdatedDesc2");
		counterView.hide();
		l.showInThisStep.addElement("header");
		l.showInThisStep.addElement("headerBox");
		l.addMultiLineText(String.format("Der Pfad wurde mit %d Zügen und %d Rücknahmen berechnet.\n" +
				"Bei leichten Variationen der Eingabeparameter können zum Teil\n" +
				"sehr viele Schritte notwendig sein, um eine Lösung zu finden\n" +
				"oder festzustellen, dass es keine gibt.\n" +
				"\n" +
                                "Zu beachten ist jedoch, dass die Warnsdorffregel eine sehr gute \n" + 
                                "Heuristik ist. Durch sie wird in den meisten Fällen eine Lösung \n" + 
				"gefunden, ohne dass Rückschritte gemacht werden müssen. In den \n" +
                                "seltenen Fällen, in denen Rückschritte notwendig werden, geschehen \n" +
                                "die verursachenden Fehler eher zu Beginn. Das führt daher in diesen \n" +
                                "Fällen zu extrem vielen Zügen, bis eine Lösung gefunden wird.\n\n" + 
                                "Die Komplexität des Springerproblems ist in O(8^(n*m))", 
				this.movesCounter.getAccess(), this.movesCounter.getQueueings()) , "solution");
		l.nextStep("Ende");
	}

	public void solved(Field field) {
		if(knightCreated){
			l.hide("knight");
			knightCreated=false;
		}
		highlightGridCell("solutionBoard", field.getX(), field.getY());
		sourceSolve.highlight(3);
		l.nextStep();
		sourceSolve.unhighlight(3);
	}

	public void startSolve(Board board) {
		createBoard(board);
		l.hide("description1", "description2", "description3", "description4",
				"description5", "description6", "description7","description8", "description9", "description10",
				"description11",
				"descriptionHeader");
		l.nextStep("Start des Algorithmus");

	}

	public void unhighlightGridCell(String name, int x, int y) {
		l.addLine("unhighlightGridCell \""+ name +"[" + y + "][" + x + "]\" within 50 ticks");
	}

	public void unvisit(Field neighboor, Field field, int turn) {
		if(!declared){
			variables.declare("string", "field");
			variables.declare("int", "turn");
			declared=true;
		}
		this.movesCounter.queueingsInc(1);
		this.moves.removeLast();
		Field last = this.moves.getLast();
		l.nextStep(String.format("Rückzug %d", movesCounter.getQueueings()));
                
		moveKnight(last, this.moves.size()-1);
                this.moves.removeLast();
		setGridCellValue("solutionBoard", neighboor.getX(), neighboor.getY(), "");
		for (Field neighboor2 : highlighted) {
			unhighlightGridCell("solutionBoard", neighboor2.getX(), neighboor2.getY());
		}
                
                variables.set("field", field.toString());
		variables.set("turn", turn+"");
                l.addLine("setGridValue \"vars[1][0]\" \"\" refresh");
		if (fieldsArr != null)
			fieldsArr.hide();
		sourceSolve.highlight(12);
		l.nextStep();
		sourceSolve.unhighlight(12);
	}

	public void visit(Field field, int turn) {
		if(!declared){
			variables.declare("string", "field");
			variables.declare("int", "turn");
			declared=true;
		}
		variables.set("field", field.toString());
		variables.set("turn", turn+"");
		this.movesCounter.accessInc(1);
		//l.addLine("setGridValue \"vars[1][0]\" \"field\" refresh after 20 ticks");
		//l.addLine("setGridValue \"vars[1][1]\" \"(" + field.getX() + ", "
		//		+ field.getY() + ")\"  refresh after 20 ticks");
		//l.addLine("setGridValue \"vars[2][0]\" \"turn\" refresh after 40 ticks");
		//l.addLine("setGridValue \"vars[2][1]\" \"" + field.getTurn()
		//		+ "\"  refresh after 40 ticks");

		l.addLine("setGridValue \"vars[1][0]\" \"\" refresh");
		if (fieldsArr != null)
			fieldsArr.hide();

		moveKnight(field, turn);
		for (Field neighboor : highlighted) {
			unhighlightGridCell("solutionBoard", neighboor.getX(), neighboor.getY());
		}
		this.highlighted.clear();
		sourceSolve.highlight(1);
		l.nextStep(String.format("Zug %d", movesCounter.getAccess()));
		sourceSolve.unhighlight(1);

		sourceSolve.highlight(2);
		l.nextStep();
		sourceSolve.unhighlight(2);
	}


	public void fieldUpdated() {
		this.movesCounter.unqueueingsInc(1);
	}

}
