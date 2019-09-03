package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class EloZahl implements Generator, ValidatingGenerator {

    DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
    DecimalFormat dFormat;

    // ========= Visualization variables ========= //
    
    protected String countryCode;

    protected Language lang;

    private SourceCodeProperties textProps, calcBoxInfoProps;

    private TextProperties headerProps;

    private RectProperties rectProps;

    private MatrixProperties matrixProps, infoBoxProps;

    private StringMatrix matrix, infoBox;

    private Text infoBoxHeader, tableHeader, calcBoxHeader;

    private SourceCode calcBoxInfoText;

    private Rect infoBoundingBox, tableBoundingBox;

    private boolean stepwise;
    
    // ========= Algorithms variables ========= //

    private int ra, rb;

    private float sa;

    private final int k = 10;

	 private float exp;

	 private float denominator;

	 private float expectedScore;

    // ========= Data ========= //

    private String[][] table;

    private String[] steps;
    
    int[] startScores;
    
    int[][] games;
    
    String[] teamNames = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", 
    					   "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
    
    // ========= Text variables ========= //
    
    String headerText;
    String[] descriptionText = new String[13];
    String[] outroText = new String[5];
    String[] infoText = new String[4];
    String currentStep;
    String[] stepsText = new String[7];
    String matchNumber;
    String eloOld;
    String eloNew;
    String expVal;
    String descriptionText1;
    String codeExampleText;
    String[] errorText = new String[5];
    String labelIntro;
    String labelRound;
    String labelOutro;

    public EloZahl(String countryCode) {
    	this.countryCode = countryCode;
    	
    	if (countryCode.equals("EN")) {
    		headerText = "Calculation of the Elo rating";
    		descriptionText[0] = "The Elo rating system was originally invented to evaluate the strength of chess players but is nowadays also";
    		descriptionText[1] = "used in many other disciplines, like e.g. football. The Elo rating of a player or team is calculated as follows:";
    		descriptionText[2] = "";
    		descriptionText[3] = "Every player is associated with a number called the Elo rating. This number is incremented or decremented after";
    		descriptionText[4] = "every game, depending on whether the player wins or loses. In the first step, for each player an expectancy value";
    		descriptionText[5] = "is calculated depending on the Elo ratings of both players, which is between 0 and 1. It represents the probability";
    		descriptionText[6] = "of a win of the respective player. After the game, each player gets either 1 point for a win, 0 points for a loss or";
    		descriptionText[7] = "0.5 points for a draw. Then the expectancy value is subtracted from this number, which results in a number between";
    		descriptionText[8] = "-1 and 1 which in turn, is multiplied by a factor k. Depending on whether the resulting value is positive or negative,";
    		descriptionText[9] = "it is added to (in case of a win) or subtracted from (in case of a loss) the original Elo rating. In case of a draw it";
    		descriptionText[10] = "depends on the size of the expectancy value whether a player's Elo rating increases or decreases.";
    		descriptionText[11] = "";
    		descriptionText[12] = "";
    		outroText[0] = "Players/teams who newly enter the rating system are assigned an estimated Elo rating.";
    		outroText[1] = "Negative Elo numbers are also possible. In practice however, they are not used, such that";
    		outroText[2] = "0 is seen as the lower bound.";
    		outroText[3] = "";
    		outroText[4] = "Further details about the elo rating system can be found on http://en.wikipedia.org/wiki/Elo_rating_system";
    		infoText[0] = "Round 1";
    		infoText[1] = "Elo (old):";
    		infoText[2] = "Exp";
    		infoText[3] = "Elo (new)";
    		currentStep = "Current step"; 
    		stepsText[0] = "Selection of two teams";
    		stepsText[1] = "Team 1 is ? with an Elo rating of ?";
    		stepsText[2] = "Team 2 is ? with an Elo rating of ?";
    		stepsText[3] = "Calculation of the Expectancies for ? and ?:\n";
    		stepsText[4] = "Outcome of match between ? and ?:\nS(?) = 1 -> Win for Team ?\nS(?) = 0 -> Loss for Team ?";
    		stepsText[5] = "Outcome of match between ? and ?:\nS(?) = S(?) = 0.5 -> Draw";
    		stepsText[6] = "Calculation of new Elo ratings for ? and ?:\n";
    		matchNumber = "Round ";
    		eloOld = "Elo(old";
    		eloNew = "Elo(new";
    		expVal = "Exp";
    		descriptionText1 = "The Elo rating system was originally invented to evaluate the strength of chess players but is nowadays also\n"
        		+ "used in many other disciplines, like e.g. football. The Elo rating of a player or team is calculated as follows:\n\n"
        		+ "Every player is associated with a number called the Elo rating. This number is incremented or decremented after\n"
        		+ "every game, depending on whether the player wins or loses. In the first step, for each player an expectancy value\n"
        		+ "is calculated depending on the Elo ratings of both players, which is between 0 and 1. It represents the probability\n"
        		+ "of a win of the respective player. After the game, each player gets either 1 point for a win, 0 points for a loss or\n"
        		+ "0.5 points for a draw. Then the expectancy value is subtracted from this number, which results in a number between\n"
        		+ "-1 and 1 which in turn, is multiplied by a factor k. Depending on whether the resulting value is positive or negative,\n"
        		+ "it is added to (in case of a win) or subtracted from (in case of a loss) the original Elo rating. In case of a draw it\n"
        		+ "depends on the size of the expectancy value whether a player's Elo rating increases or decreases.\n"
    			+ "<b><u>Notes for input of simulation data:</u></b>\n"
    			+ "The matrix inputData contains one column per team and each row represents one match (except the first one).\n" 
    			+ "The first row contains the initial Elo rankings of the teams. In the rows below there have to be specified two\n"
    			+ "competing teams. Insert 2 for a win and 1 for a loss. In case of a draw both teams get 1. All teams which are not\n"
    			+ "playing in the corresponding match, get 0. In each match there are two teams playing. Thus, each row must contain\n"
    			+ "exactely two non-zero values.";
    		codeExampleText = "Calculate new Elo rating of team A:\n\n"
    				+ "   R_A := old Elo number of player A\n"
    				+ "   R_B := old Elo number of player B\n"
    				+ "   E_A = 1/[1 + 10^((R_B - R_A)/400)]\n\n"
    				+ "   If won:\n" + "      return R_A + k*(1 - E_A)\n\n"
    				+ "   If lost:\n" + "      return R_A - k*E_A\n\n"
    				+ "   Else:\n" + "      return R_A + k*(0.5 - E_A)\n";
    		errorText[0] = "Minimum number of teams is 2 (input matrix must contain at least two columns)";
    		errorText[1] = "Minimum number of matches is 1 (input matrix must contain at least two rows)";
    		errorText[2] = "Please only provide 0, 1 or 2 in matches (not playing = 0; loss = 1; win = 2; draw = both 1 or both 2)";
    		errorText[3] = "Please do not provide negative numbers!";
    		errorText[3] = "During each match exactely two teams have to play (for all rows except first row: Provide two non-zero values)";
    		labelIntro = "Introduction";
    		labelRound = "Round ";
    		labelOutro = "Conclusion";
    	}
    	
    	else {
    		headerText = "Berechnung der Elo-Zahl";
    		descriptionText[0] = "Das Elo-Verfahren wurde urspruenglich zur Bewertung der Spielstaerke von Schachspielern entwickelt und wird";
    		descriptionText[1] = "mittlerweile auch in einigen anderen Sportarten, wie z.B. beim Fussball verwendet. Die Elo-Zahl eines Spielers";
    		descriptionText[2] = "bzw. eines Teams wird nach folgendem Prinzip ermittelt:";
    		descriptionText[3] = "";
    		descriptionText[4] = "Jedem Spieler wird eine sogenannte Elo-Zahl zugeordnet. Diese wird nach jedem Spiel entweder erhoeht oder";
    		descriptionText[5] = "verringert. Treten zwei Spieler gegeneinander an, so wird fuer jeden zunaechst ein Erwartungswert, basierend";
    		descriptionText[6] = "auf den bisherigen Elo-Zahlen beider Spieler berechnet. Dieser liegt zwischen 0 und 1 und impliziert eine";
    		descriptionText[7] = "Wahrscheinlichkeit fuer einen Sieg des betreffenden Spielers. Nach Ende des Spiels erhalten die Spieler";
    		descriptionText[8] = "1 Punkt fuer einen Sieg, 0 Punkte fuer eine Niederlage und 1/2 Punkt fuer ein Unentschieden. Hiervon wird";
    		descriptionText[9] = "nun der jeweilige Erwartungswert abgezogen. Der sich daraus ergebende Wert liegt zwischen -1 und 1 und ";
    		descriptionText[10] = "wird anschliessend mit einem Faktor k multipliziert. Je nach Vorzeichen dieses Produkts wird nun dieser ";
    		descriptionText[11] = "Wert von der alten Elo-Zahl abgezogen (Niederlage) oder hinzuaddiert (Sieg). Im Falle eines Unentschiedens";
    		descriptionText[12] = "haengt es von der Hoehe des Erwartungswertes ab, ob die Elo-Zahl eines Spielers verringert oder erhoeht wird.";
    		outroText[0] = "Spieler oder Teams, die neu in das Rating-Verfahren einsteigen, bekommen eine geschaetzte Elo-Zahl zugewiesen.";
    		outroText[1] = "Negative Elo-Zahlen sind ebenfalls zulaessig, diese werden jedoch in der Praxis meist vermieden, sodass 0 als der";
    		outroText[2] = "niedrigste Wert angesehen wird.";
    		outroText[3] = "";
    		outroText[4] = "Weitere Details zur Elo-Zahl sind zu finden unter: http://de.wikipedia.org/wiki/Elo-Zahl";
    		infoText[0] = "Spiel Nr. 1";
    		infoText[1] = "Elo (alt):";
    		infoText[2] = "EW";
    		infoText[3] = "Elo (neu)";
    		currentStep = "Aktueller Schritt";	
    		stepsText[0] = "Auswahl zweier Teams";
    		stepsText[1] = "Team 1 ist ? mit einem Elo-Wert von ?";
    		stepsText[2] = "Team 2 ist ? mit einem Elo-Wert von ?";
    		stepsText[3] = "Berechnung der Erwartungswerte fuer ? und ?:\n";
    		stepsText[4] = "Ausgang der Partie zwischen ? und ?:\nS(?) = 1 -> Sieg fuer Team ?\nS(?) = 0 -> Niederlage fuer Team ?";
    		stepsText[5] = "Ausgang der Partie zwischen ? und ?:\nS(?) = S(?) = 0.5 -> Unentschieden";
    		stepsText[6] = "Berechnung der neuen Elo-Werte fuer ? und ?:\n";
    		matchNumber = "Spiel Nr. ";
    		eloOld = "Elo(alt";
    		eloNew = "Elo(neu";
    		expVal = "EW";
    		descriptionText1 = "Das Elo-Verfahren wurde urspruenglich zur Bewertung der Spielstaerke von Schachspielern entwickelt und wird\n"
    				+ "mittlerweile auch in einigen anderen Sportarten, wie z.B. beim Fussball (http://www.eloratings.net/world.html) verwendet.\n"
    				+ "Die Elo-Zahl eines Spielers bzw. eines Teams wird nach folgendem Prinzip ermittelt:\n\n"
    				+ "Jedem Spieler wird eine sogenannte Elo-Zahl zugeordnet. Diese wird nach jedem Spiel entweder erhoeht oder\n"
    				+ "verringert. Treten zwei Spieler gegeneinander an, so wird fuer jeden zunaechst ein Erwartungswert, basierend\n"
    				+ "auf den bisherigen Elo-Zahlen beider Spieler berechnet. Dieser liegt zwischen 0 und 1 und impliziert eine\n"
    				+ "Wahrscheinlichkeit fuer einen Sieg des betreffenden Spielers. Nach Ende des Spiels erhalten die Spieler\n"
    				+ "1 Punkt fuer einen Sieg, 0 Punkte fuer eine Niederlage und 1/2 Punkt fuer ein Unentschieden. Hiervon wird\n"
    				+ "nun der jeweilige Erwartungswert abgezogen. Der sich daraus ergebende Wert liegt zwischen -1 und 1 und\n"
    				+ "wird anschliessend mit einem Faktor k multipliziert. Je nach Vorzeichen dieses Produkts wird nun dieser\n"
    				+ "Wert von der alten Elo-Zahl abgezogen (Niederlage) oder hinzuaddiert (Sieg). Im Falle eines Unentschiedens\n"
    				+ "haengt es von der Hoehe des Erwartungswertes ab, ob die Elo-Zahl eines Spielers verringert oder erhoeht wird.\n"
    				+ "<b><u>Hinweise zur Eingabe von Simulationsdaten:</u></b>\n"
    				+ "In der Matrix inputData steht jede Spalte fuer ein Team, jede Zeile (außer der ersten) steht fuer ein Spiel.\n"
    				+ "In der ersten Zeile wird der Start-Elo-Wert jedes Teams eingetragen. In den Zeilen darunter spielen je zwei Teams\n"
    				+ "gegeneinander, wobei fuer den Gewinner eine 2 und fuer den verlierer eine 1 eingetragen wird. Bei Unentschieden bekommen\n"
    				+ "beide Teams eine 1. Eine 0 bedeutet, dass das betreffende Team in dieser Runde nicht spielt. Pro Runde muessen genau zwei\n"
    				+ "Teams gegeneinander antreten, d.h. pro Zeile gibt es zwei von 0 verschiedene Werte.";
    		codeExampleText = "Berechne neue Elo-Zahl von Team A:\n\n"
    				+ "   R_A := alte Elo-Zahl von Spieler A\n"
    				+ "   R_B := alte Elo-Zahl von Spieler B\n"
    				+ "   E_A = 1/[1 + 10^((R_B - R_A)/400)]\n\n"
    				+ "   Falls gewonnen:\n" + "      return R_A + k*(1 - E_A)\n\n"
    				+ "   Falls verloren:\n" + "      return R_A - k*E_A\n\n"
    				+ "   Sonst:\n" + "      return R_A + k*(0.5 - E_A)\n";
    		errorText[0] = "Mindestanzahl an Teams ist 2 (Inputmatrix muss mindestens zwei Spalten enthalten)";
    		errorText[1] = "Mindestanzahl an Spielen ist 1 (Inputmatrix muss mindestens zwei Zeilen enthalten)";
    		errorText[2] = "Fuer Spiele bitte nur Werte 0, 1 oder 2 angeben (spielt nicht = 0, verliert = 1, gewinnt = 2, unentschieden = beide 1 oder beide 2)";
    		errorText[3] = "Bitte keine negativen Werte angeben!";
    		errorText[3] = "In jedem Spiel muessen genau zwei Teams gegeneinander antreten (ab Zeile 2: pro Zeile genau zwei Werte != 0 angeben)";
    		labelIntro = "Einleitung";
    		labelRound = "Spiel Nr. ";
    		labelOutro = "Fazit";
    	}
    }
    
    @Override
    public void init() {
		dfs.setDecimalSeparator('.');
		dFormat = new DecimalFormat("0.000", dfs);
		lang = new AnimalScript(headerText, "Felix Mayer, Lulzim Murati", 800, 600);
		lang.setStepMode(true);		
		stepwise = true;
    }

    /*
     * Generates the intro text
     */
    public void createIntro() {
	Text header = lang.newText(new Coordinates(20, 10),
		headerText, "header", null, headerProps);
	Offset offsetLeft = new Offset(-5, -5, header,
		AnimalScript.DIRECTION_NW);
	Offset offsetRight = new Offset(5, 5, header, AnimalScript.DIRECTION_SE);

	lang.newRect(offsetLeft, offsetRight, "header", null, rectProps);

	SourceCode intro = lang.newSourceCode(new Coordinates(20, 40),
		"introText", null, textProps);
	intro.addCodeLine(descriptionText[0], null, 0, null);
	intro.addCodeLine(descriptionText[1], null, 0, null);
	intro.addCodeLine(descriptionText[2], null, 0, null);
	intro.addCodeLine(descriptionText[3], null, 0, null);
	intro.addCodeLine(descriptionText[4], null, 0, null);
	intro.addCodeLine(descriptionText[5], null, 0, null);
	intro.addCodeLine(descriptionText[6], null, 0, null);
	intro.addCodeLine(descriptionText[7], null, 0, null);
	intro.addCodeLine(descriptionText[8], null, 0, null);
	intro.addCodeLine(descriptionText[9], null, 0, null);
	intro.addCodeLine(descriptionText[10], null, 0, null);
	intro.addCodeLine(descriptionText[11], null, 0, null);
	intro.addCodeLine(descriptionText[12], null, 0, null);
	
	lang.nextStep(labelIntro);
	intro.hide();
    }

    /*
     * Generates the conclusion text
     */
    public void createConclusion() {
	infoBox.hide();
	calcBoxInfoText.hide();
	matrix.hide();
	infoBoxHeader.hide();
	calcBoxHeader.hide();
	tableHeader.hide();
	infoBoundingBox.hide();
	tableBoundingBox.hide();
	SourceCode outro = lang.newSourceCode(new Coordinates(20, 40),
		"introText", null, textProps);

	outro.addCodeLine(outroText[0],	null, 0, null);
	outro.addCodeLine(outroText[1],
			null, 0, null);
	outro.addCodeLine(outroText[2], null, 0, null);
	outro.addCodeLine(outroText[3], null, 0, null);
	outro.addCodeLine(outroText[4],	null, 0, null);
    }

    /**
     * initializes a new table
     */
    public void initTable() {
	tableHeader = lang.newText(new Coordinates(50, 50), "Elo-Ranking",
		"tableHeader", null, headerProps);

		table = new String[startScores.length][3];
	
		for (int i = 0; i < table.length; i++) {
		    table[i] = new String[] { String.valueOf(i + 1),
			    teamNames[i], String.valueOf(startScores[i]) };
		}
		matrix = lang.newStringMatrix(new Coordinates(50, 100), table, "",
			null, matrixProps);
	
		Offset offsetLeft = new Offset(-5, -5, matrix,
			AnimalScript.DIRECTION_NW);
		Offset offsetRight = new Offset(5, 5, matrix, AnimalScript.DIRECTION_SE);
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
		tableBoundingBox = lang.newRect(offsetLeft, offsetRight,
			"tableBoundingBox", null, rectProps);
    }

    /*
     * Updates the Elo ranking table
     */
    public void updateTable(int[] currentGame, int round) {
	lang.nextStep(labelRound + round);
	Timing t = new TicksTiming(10);

	// determine names of current teams and result of match
	String teamA = ""; 
	String teamB = "";
	String winner = "";		// "" means a draw
	String loser = "";
	int j = 0;
	for (int i = 0; i < currentGame.length; i++) {
		if (currentGame[i] > 0) {
			switch (j) {
				case 0: teamA = teamNames[i]; 
						j++;
						if (currentGame[i] == 2) winner = teamA;
						break;
				case 1: teamB = teamNames[i]; 
						j++;
						if (currentGame[i] == 2) 
							if (winner.equals("")) winner = teamB;
							else winner = "";
						break;
				case 2: break;
			}
		}
	}
	if (winner.equals(teamA)) loser = teamB;
	else if (winner.equals(teamB)) loser = teamA;
		
	// determine posistion of teams in table and their elos
	int posA = getTeamRank(teamA);
	int posB = getTeamRank(teamB);
	String eloA = matrix.getElement(posA, 2);
	String eloB = matrix.getElement(posB, 2);
	
	// select teams and write them to info box
	matrix.highlightCellColumnRange(posA, 0, 2, null, t);
	updateTeam(teamA, eloA, 1);
	updateCalcInfo(new String[] { teamA, eloA }, 1, false);
	lang.nextStep();
	
	matrix.highlightCellColumnRange(posB, 0, 2, null, t);
	updateTeam(teamB, eloB, 2);
	updateCalcInfo(new String[] { teamB, eloB }, 2, false);
	lang.nextStep();

	// parse Elo numbers to integer values
	ra = Integer.parseInt(eloA);
	rb = Integer.parseInt(eloB);

	// calculate expected values
	String expectedScoreA = dFormat.format(getExpectedScore());
	String expectedScoreB = dFormat.format(1 - getExpectedScore());
		
	updateCalcInfo(
		new String[] { teamA, teamB, 
				teamA, teamB, teamA,		
				teamA, eloB, eloA,
				teamA, String.valueOf(exp),
				teamA, String.valueOf(denominator),
				teamA, String.valueOf(expectedScore),	  
				teamB, teamA, teamB,
				teamB, eloA, eloB,
				teamB, String.valueOf(-exp),
				teamB, String.valueOf((float) (1 + Math.pow(10, -exp))),
				teamB, String.valueOf((float)1-expectedScore) }, 3, stepwise);
	
	updateValue(expectedScoreA, 1, 2);
	updateValue(expectedScoreB, 2, 2);	
	lang.nextStep();

	// determine game result
	if (winner.equals(teamA)) sa = 1.0f;
	else if (winner.equals(teamB)) sa = 0.0f;
	else sa = 0.5f;

	if (!winner.equals("")) updateCalcInfo(new String[] { teamA, teamB, winner, winner, loser, loser }, 4, stepwise);
	else updateCalcInfo(new String[] { teamA, teamB, teamA, teamB }, 5, stepwise);
	updateValue(String.valueOf(sa), 1, 3);
	updateValue(String.valueOf(1 - sa), 2, 3);
	lang.nextStep();

	// calculate new Elo
	int newEloA = calculate();
	int newEloB = Integer.parseInt(eloB)
		+ (newEloA - Integer.parseInt(eloA)) * (-1);
	
	updateCalcInfo(new String[] { 	teamA, teamB,
									teamA, teamA, String.valueOf(k), teamA, teamA, 
									teamA, String.valueOf(eloA), String.valueOf(k), String.valueOf(sa), String.valueOf(expectedScoreA),
									teamA, String.valueOf(newEloA),
									teamB, teamB, String.valueOf(k), teamB, teamB,
									teamA, String.valueOf(eloA), String.valueOf(k), String.valueOf(1-sa), String.valueOf(expectedScoreB),
									teamB, String.valueOf(newEloB) }, 6, stepwise);
	
	updateValue(String.valueOf(newEloA), 1, 4);
	updateValue(String.valueOf(newEloB), 2, 4);

	// Write new Elo values back to table
	matrix.put(posA, 2, String.valueOf(newEloA), null, null);
	matrix.put(posB, 2, String.valueOf(newEloB), null, null);
	lang.nextStep();

	// Clear info box
	updateTeam("?", "?", 1);
	updateTeam("?", "?", 2);
	updateValue("?", 1, 2);
	updateValue("?", 2, 2);
	updateValue("?", 1, 3);
	updateValue("?", 2, 3);
	updateValue("?", 1, 4);
	updateValue("?", 2, 4);

	updateCalcInfo(null, 0, false);

	// swap table entries
	matrix.unhighlightCellColumnRange(posA, 0, 2, null, t);
	matrix.unhighlightCellColumnRange(posB, 0, 2, null, t);
	sortTable();
    }

    /*
     * Sorts the table
     */
    private void sortTable() {
	boolean sorted = false;
	int a, b, tmp;
	while (!sorted) {
	    sorted = true;
	    for (int i = 0; i < matrix.getNrRows() - 1; i++) {
		a = Integer.parseInt(matrix.getElement(i, 2));
		b = Integer.parseInt(matrix.getElement(i + 1, 2));
		if (a < b) {
		    tmp = a;
		    a = b;
		    b = tmp;
		    matrix.swap(i, 1, i + 1, 1, null, null);
		    matrix.swap(i, 2, i + 1, 2, null, null);
		    sorted = false;
		}
	    }
	}
    }
   
    
    /*
     * Returns the current position of a team in the ranking table
     */
    private int getTeamRank(String teamName) {
    	int rows = matrix.getNrRows();
    	for (int i = 0; i < rows; i++) {
    		String name = matrix.getElement(i, 1);
    		if (name == teamName)
    			return i;
    	}
    	return -99;
    }
    

    /*
     * Initializes the info box
     */
    public void initInfo() {
	infoBoxHeader = lang.newText(new Coordinates(300, 50), infoText[0],
		"infoBoxHeader", null, headerProps);

	String[][] infoData = new String[][] { { "Team:", "?", "?" },
		{ infoText[1], "?", "?", }, { infoText[2], "?", "?", },
		{ "S:", "?", "?", }, { infoText[3], "?", "?", } };
	infoBox = lang.newStringMatrix(matrix.getUpperLeft(), infoData, "",
		null, matrixProps);
	infoBox.moveBy("translate", 200, -5, null, null);

	Offset offsetLeft = new Offset(-5, -5, infoBox,
		AnimalScript.DIRECTION_NW);
	Offset offsetRight = new Offset(100, 25, infoBox,
		AnimalScript.DIRECTION_SE);
	rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
	infoBoundingBox = lang.newRect(offsetLeft, offsetRight,
		"infoBoundingBox", null, rectProps);
	infoBoundingBox.moveBy("translate", 200, -5, null, null);
    }

    // TODO
    public void initCalcInfo() {				
		calcBoxHeader = lang.newText(new Coordinates(250, 300),
			currentStep, "calcBoxHeader", null, headerProps);
	
		steps = new String[] {
				stepsText[0],
				stepsText[1],
				stepsText[2],
				stepsText[3]
			+ expVal + "(?) = 1 / [1 + 10^((" + eloOld + ", ?) - " + eloOld + ", ?)) / 400)]\n"
			+ expVal + "(?) = 1 / [1 + 10^((? - ?) / 400)]\n"
			+ expVal + "(?) = 1 / [1 + 10^(?)]\n"
			+ expVal + "(?) = 1 / ?\n"
			+ expVal + "(?) = ?\n"
			+ expVal + "(?) = 1 / [1 + 10^((" + eloOld + ", ?) - " + eloOld + ", ?)) / 400)]\n"
			+ expVal + "(?) = 1 / [1 + 10^((? - ?) / 400)]\n"
			+ expVal + "(?) = 1 / [1 + 10^(?)]\n"
			+ expVal + "(?) = 1 / ?\n"
			+ expVal + "(?) = ?",
			stepsText[4],
			stepsText[5],
			stepsText[6]
			+ eloNew + ", ?) = " + eloOld + ", ?) + ? * (S(?) - " + expVal + "(?))\n"
			+ eloNew + ", ?) = ? + ? * (? - ?)\n"
			+ eloNew + ", ?) = ?\n"
			+ eloNew + ", ?) = " + eloOld + ", ?) + ? * (S(?) - " + expVal + "(?))\n"
			+ eloNew + ", ?) = ? + ? * (? - ?)\n"
			+ eloNew + ", ?) = ?" };
	
		calcBoxInfoText = lang.newSourceCode(new Coordinates(300, 350),
			"calcInfo", null, calcBoxInfoProps);
	
		Offset offsetLeft = new Offset(-5, -5, calcBoxInfoText,
			AnimalScript.DIRECTION_NW);
		Offset offsetRight = new Offset(200, 25, calcBoxInfoText,
			AnimalScript.DIRECTION_SE);
		Rect boundingBox = lang.newRect(offsetLeft, offsetRight,
			"calcInfoBoundingBox", null, rectProps);
		boundingBox.moveBy("translate", 0, -5, null, null);
    }

					

    /*
     * Update methods for info box
     */
    private void updateTeam(String teamName, String elo, int team) {
		infoBox.put(0, team, teamName, null, null);
		infoBox.put(1, team, elo, null, null);
    }

    private void updateValue(String value, int team, int index) {
    	infoBox.put(index, team, value, null, null);
    }

    private void updateInfoBoxHeader(int i) {
    	infoBoxHeader.setText(matchNumber + String.valueOf(i), null, null);
    }

    // TODO
    private void updateCalcInfo(String[] placeholder, int index, boolean stepwise) {
		String calcInfo = steps[index];
		StringBuilder sb = new StringBuilder();
	
		int i = 0;
		for (int j = 0; j < calcInfo.length(); j++) {
		    if (calcInfo.substring(j, j + 1).equals("?")) {
			sb.append(placeholder[i++]);
		    } else {
			sb.append(calcInfo.substring(j, j + 1));
		    }
		}
		calcBoxInfoText.hide();
		calcBoxInfoText = lang.newSourceCode(new Coordinates(250, 335),
			"calcInfo", null, calcBoxInfoProps);
		
		// Check whether stepwise calculation or not
		if (stepwise) {
			String[] lines = sb.toString().split("\n");
			for (int k = 0; k < lines.length; k++) {
				calcBoxInfoText.addMultilineCode(lines[k] + "\n", "", null);
				if (k < lines.length - 1) lang.nextStep();
			}
		}		
		else calcBoxInfoText.addMultilineCode(sb.toString(), "", null);
    }

    @Override
    public String generate(AnimationPropertiesContainer props,
	    Hashtable<String, Object> primitives) {
		getDescription();
		
		validateInput(props, primitives);
		
		// Preprocess input data for animation
		int[][] inputData = (int[][]) primitives.get("inputData");
		int numTeams = inputData[0].length;
		int numGames = inputData.length - 1;
		startScores = new int[numTeams];
		for (int i = 0; i < numTeams; i++)
			startScores[i] = inputData[0][i];	
		
		// Fetch properties from XML file
		textProps = (SourceCodeProperties) props.getPropertiesByName("textProperties");	
		calcBoxInfoProps = (SourceCodeProperties) props.getPropertiesByName("calculationStepTextProperties");	
		headerProps = (TextProperties) props.getPropertiesByName("headerProperties");	
		//headerProps.set(AnimationPropertiesKeys.SIZE_PROPERTY, 18);
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 18));
			headerProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		matrixProps = (MatrixProperties) props.getPropertiesByName("tableProperties");	
		infoBoxProps = (MatrixProperties) props.getPropertiesByName("tableProperties");	
		rectProps = (RectProperties) props.getPropertiesByName("rectangleProperties");
			
		// initialize animation
		init();
		createIntro();
		initTable();
		initInfo();
		initCalcInfo();
		
		// run animation
		for (int i = 1; i <= numGames; i++) {
			if (i == 3) stepwise = false;		// stepwise calc info only in first two matches
		    updateInfoBoxHeader(i);
		    updateTable(inputData[i], i);
		}
		createConclusion();
		String output = removeRightRefreshTags("StringMatrix1", lang.toString());
		return output;
    }

    @Override
    public String getName() {
    	return "Elo-Zahl";
    }

    @Override
    public String getAlgorithmName() {
    	return "Elo-Zahl";
    }

    @Override
    public String getAnimationAuthor() {
    	return "Felix Mayer, Lulzim Murati";
    }

    @Override
    public String getDescription() {

	return descriptionText1;
    }

    @Override
    public String getCodeExample() {
		return codeExampleText;
    }

    @Override
    public String getFileExtension() {
    	return "asu";
    }

    @Override
    public Locale getContentLocale() {
    	if (this.countryCode.equals("EN"))
    		return Locale.ENGLISH;
    	else return Locale.GERMAN;
    }

    @Override
    public GeneratorType getGeneratorType() {
    	return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    @Override
    public String getOutputLanguage() {
    	return Generator.PSEUDO_CODE_OUTPUT;
    }

    /**
     * validates the user input
     */
    @Override
    public boolean validateInput(AnimationPropertiesContainer props,
	    Hashtable<String, Object> primitives)
	    throws IllegalArgumentException {
    	
    	// Fetch user input data
    	int[][] inputData = (int[][]) primitives.get("inputData");
    	int numTeams = inputData[0].length;
    	int numGames = inputData.length - 1;
    	
    	// Check whether No. of teams >= 2 and No. of matches is >= 1:
    	if (numTeams < 2) 
    		throw new IllegalArgumentException(errorText[0]);
    	if (numGames < 1) 
    		throw new IllegalArgumentException(errorText[1]);
    	
    	// Check whether match values are only 0, 1 or 2:
    	for (int i = 1; i < inputData.length; i++) {
    		for (int j = 0; j < inputData[i].length; j++) {
    			if (inputData[i][j] < 0 || inputData[i][j] > 2)
    				throw new IllegalArgumentException(errorText[2]);
    			
    		}
    	}
    	
    	// Check whether the input matrix doesn't contain negative integers:
    	for (int i = 0; i < inputData.length; i++) {
    		for (int j = 0; j < inputData[i].length; j++) {
    			if (inputData[i][j] < 0)
    				throw new IllegalArgumentException(errorText[3]);
    		}
    	}
    	
    	// Check whether each match has exactly two competing teams:
    	int count = 0;
    	for (int i = 1; i < inputData.length; i++) {
    		count = 0;
    		for (int j = 0; j < inputData[i].length; j++) {
    			if (inputData[i][j] > 0) count++;
    		}
    		if (count != 2)
				throw new IllegalArgumentException(errorText[4]+i+"_"+count);
    	}
    	
    	// No illegal arguments detected:
    	return true;
    }

    // Elo algorithm:

    /**
     * returns expected score for player a: ea = 1 / (1 + 10^(rb - ra)/400)
     * 
     * @return ea
     */
    private float getExpectedScore() {
		exp = (float) (rb - ra) / 400;
		denominator = (float) (1 + Math.pow(10, exp));
		expectedScore = 1 / denominator;
		return expectedScore;
    }

    /**
     * invokes calculation of the new elo score for a player
     * 
     * @return new elo score based on the outcome of a match
     */
    public int calculate() {
		float ea = getExpectedScore();
		return (int) (ra + k * (sa - ea));
    }

    /*
     * Removes all "refresh" tags from the Animal script code which belong to
     * arrays or matrices that should be without such tags (to avoid
     * highlighting problems)
     */
    public String removeRightRefreshTags(String matrix, String input) {
		String output = "";
		String[] lines = input.split("\n");
		for (int i = 0; i < lines.length; i++) {
		    if (lines[i].contains(matrix)) {
			lines[i] = lines[i].replace("refresh", "");
		    }
		}
		for (int i = 0; i < lines.length; i++) {
		    output = output.concat(lines[i]);
		    output = output.concat("\n");
		}
		return output;
    }
}

