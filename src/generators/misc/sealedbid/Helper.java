package generators.misc.sealedbid;

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;

/**
 * @author Sebastian Ritzenhofen, Felix Rauterberg
 * 
 *         This class contains several supporting functions and methods.
 */
public class Helper {

	public static SealedBid sb;
	public static int lastWinnerID;
	private static double probTotalSum;
	private static double probFairShare;
	private static double probItemWinner;
	private static double probPayOrReceive;

	public static boolean tocFairShare;
	public static boolean tocAssignItems;
	public static boolean tocSettleDifference;

	public static MatrixProperties matrixProps;
	public static SourceCodeProperties pseudoCodeProps;

	/*
	 * MATRIX EXTENSIONS
	 */

	/**
	 * First matrix extension.
	 */
	public static void firstMatrixExtension() {

		// step
		sb.lang.nextStep();

		// cleanup
		Helper.unhighlightMatrix();

		// pseudocode
		Helper.setPseudocode(1);

		int startRow = sb.numItems + 1;
		int endRow = startRow + 2;
		int cols = sb.numPlayers + 1;

		// show next needed lines in matrix
		for (int i = startRow; i < endRow; i++) {
			for (int j = 0; j < cols; j++) {
				sb.matrix.setGridTextColor(i, j, (Color) matrixProps.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY),
						null, null);
				sb.matrix.setGridHighlightTextColor(i, j,
						(Color) Helper.matrixProps.get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY), null, null);
			}
		}
	}

	/**
	 * Second matrix extension.
	 */
	public static void secondMatrixExtension() {

		// step
		sb.lang.nextStep();

		// cleanup
		Helper.unhighlightMatrix();
		int cols = sb.numPlayers + 1;

		// pseudocode
		Helper.setPseudocode(4);

		// show next needed lines in matrix
		for (int j = 0; j < cols; j++) {
			if (j > 0)
				sb.matrix.put(sb.numItems + 3, j, "0.00", null, null);
			sb.matrix.setGridTextColor(sb.numItems + 3, j,
					(Color) matrixProps.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY), null, null);
			sb.matrix.setGridHighlightTextColor(sb.numItems + 3, j,
					(Color) Helper.matrixProps.get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY), null, null);
		}
	}

	/**
	 * Third matrix extension.
	 */
	public static void thirdMatrixExtension() {

		// step
		sb.lang.nextStep();

		// cleanup
		Helper.unhighlightMatrix();
		Helper.grayOutMatrixLine(sb.numItems, lastWinnerID);
		int row = sb.numItems + 4;
		int cols = sb.numPlayers + 1;

		// pseudocode
		Helper.setPseudocode(6);

		// show next needed lines in matrix
		for (int j = 0; j < cols; j++) {
			if (j > 0)
				sb.matrix.put(row, j, "", null, null);
			sb.matrix.setGridTextColor(row, j, (Color) matrixProps.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY),
					null, null);
			sb.matrix.setGridHighlightTextColor(row, j,
					(Color) Helper.matrixProps.get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY), null, null);
		}
	}

	/**
	 * Fourth matrix extension.
	 */
	public static void fourthMatrixExtension() {

		// step
		sb.lang.nextStep();

		// cleanup
		Helper.unhighlightMatrix();
		int row = sb.numItems + 5;
		int cols = sb.numPlayers + 1;

		// pseudocode
		Helper.setPseudocode(8);

		// show next needed lines in matrix
		for (int j = 0; j < cols; j++) {
			if (j > 0)
				sb.matrix.put(row, j, "", null, null);
			sb.matrix.setGridTextColor(row, j, (Color) matrixProps.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY),
					null, null);
			sb.matrix.setGridHighlightTextColor(row, j,
					(Color) Helper.matrixProps.get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY), null, null);
		}
	}

	/*
	 * INTERACTIVE QUESTIONS
	 */

	/**
	 * Question about total sum of bids.
	 * 
	 * @param id
	 *            player id
	 * @param sumOfBids
	 *            player's sum of bids
	 */
	public static void questionTotalSum(int id, double sumOfBids) {
		if (!sb.useQuestions)
			return;
		if (Math.random() <= probTotalSum) {
			FillInBlanksQuestionModel qTotalSum = new FillInBlanksQuestionModel("totalSum" + id);
			qTotalSum.setPrompt(
					"Wie viel Wert misst Teilnehmer " + id + " der gesamten Itemmenge bei (Integerwert angeben)?");
			qTotalSum.addAnswer(String.valueOf(sumOfBids), 1,
					"Korrekt! Die Summe aller Gebote von Teilnehmer " + id
							+ " und damit seine Gesamtwertschätzung der Itemmenge beträgt " + sumOfBids
							+ " Geldeinheiten (z.B. Euro)");
			sb.lang.addFIBQuestion(qTotalSum);
			probTotalSum *= 0.7;
		}
	}

	/**
	 * Question about fair share.
	 * 
	 * @param id
	 *            player id
	 * @param fairShare
	 *            fair share of player
	 * @param sumOfBids
	 *            sum of bids of player
	 */
	public static void questionFairShare(int id, double fairShare, double sumOfBids) {
		if (!sb.useQuestions)
			return;
		if (Math.random() < probFairShare) {
			FillInBlanksQuestionModel qFairShare = new FillInBlanksQuestionModel("fairShare" + id);
			qFairShare.setPrompt("Wie hoch ist der Betrag, den Teilnehmer " + id
					+ " als subjektiv fairen Anteil (fair share) der gesamten Itemmenge empfindet (Integerwert angeben)?");
			qFairShare.addAnswer(String.valueOf(fairShare), 1, "Richtig! Teilnehmer " + id
					+ " misst der Itemmenge einen Wert von " + sumOfBids
					+ " Geldeinheiten bei. Geteilt durch die Anzahl der Teilnehmer müsste aus Sicht dieses Teilnehmers ein fairer Anteil "
					+ fairShare + " Geldeinheiten entsprechen");
			sb.lang.addFIBQuestion(qFairShare);
			probFairShare *= 0.7;
		}
	}

	/**
	 * Question about who wins a given item.
	 * 
	 * @param itemIndex
	 *            intex of item
	 * @param highestBidderID
	 *            id of player who is highest bidder for the item
	 */
	public static void questionItemWinner(int itemIndex, int highestBidderID) {
		if (!sb.useQuestions)
			return;
		if (Math.random() < probItemWinner) {
			MultipleChoiceQuestionModel qItemWinner = new MultipleChoiceQuestionModel("itemWinner" + itemIndex);
			qItemWinner.setPrompt("Welcher Teilnehmer wird dieses Item ersteigern?");
			for (int i = 1; i <= sb.numPlayers; i++) {
				if (i == highestBidderID) {
					qItemWinner.addAnswer("Teilnehmer " + i, 1,
							"Richtig! Teilnehmer " + i + " hat das höchste Gebot abgegeben und erhält das Item");
				} else {
					qItemWinner.addAnswer("Teilnehmer " + i, 0,
							"Leider falsch. Teilnehmer " + highestBidderID + " erhält als Höchstbietender das Item");
				}
			}
			sb.lang.addMCQuestion(qItemWinner);
			probItemWinner *= 0.75;
		}
	}

	/**
	 * Question about whether a player has to pay or receive in/from cash register.
	 * 
	 * @param playerID
	 *            player
	 * @param diff
	 *            difference between item value and fair share of player
	 */
	public static void questionPayOrReceive(int playerID, double diff) {
		if (!sb.useQuestions)
			return;
		if (Math.random() < probPayOrReceive) {
			MultipleChoiceQuestionModel qPayOrReceive = new MultipleChoiceQuestionModel("payOrReceive" + playerID);
			qPayOrReceive.setPrompt("Welche Antwort ist richtig? Teilnehmer " + playerID);
			String opt1 = "muss einen Betrag in die Gemeinschaftskasse einzahlen";
			String opt2 = "erhält einen Betrag aus der Gemeinschaftskasse";
			String opt3 = "muss weder einzahlen noch erhält er Geld aus der Gemeinschaftskasse";
			String reason1 = "Da der Teilnehmer einen Wert in Form von Items erhalten hat, der höher ist als sein fairer Anteil, muss er die Differenz in die Gemeinschaftskasse einzahlen";
			String reason2 = "Da der Teilnehmer nicht genug Gegenstandswert erhalten hat, um seinen fairen Anteil zu decken, erhält er die Differenz aus der Gemeinschaftskasse";
			String reason3 = "Der erhaltene Wert des Teilnehmers deckt exakt seinen als fair empfundenen Anteil, sodass er weder einzahlen muss noch Geld erhält";
			if (diff > 0.0) {
				qPayOrReceive.addAnswer(opt1, 1, "Stimmt! " + reason1);
				qPayOrReceive.addAnswer(opt2, 0, "Leider falsch; " + reason1);
				qPayOrReceive.addAnswer(opt3, 0, "Leider falsch; " + reason1);
			} else if (diff < 0.0) {
				qPayOrReceive.addAnswer(opt1, 0, "Leider falsch; " + reason2);
				qPayOrReceive.addAnswer(opt2, 1, "Stimmt! " + reason2);
				qPayOrReceive.addAnswer(opt3, 0, "Leider falsch; " + reason2);
			} else {
				qPayOrReceive.addAnswer(opt1, 0, "Leider falsch; " + reason3);
				qPayOrReceive.addAnswer(opt2, 0, "Leider falsch; " + reason3);
				qPayOrReceive.addAnswer(opt3, 1, "Stimmt! " + reason3);
			}
			sb.lang.addMCQuestion(qPayOrReceive);
			probPayOrReceive *= 0.75;
		}
	}

	/**
	 * Question about what happens with the remaining money in the cash register.
	 */
	public static void questionRemainingMoney() {
		if (!sb.useQuestions)
			return;
		MultipleChoiceQuestionModel qRemainingMoney = new MultipleChoiceQuestionModel("remainingMoney");
		qRemainingMoney.setPrompt("Was geschieht mit dem übrigen Geld in der Gemeinschaftskasse?");
		qRemainingMoney.addAnswer(
				"Der Teilnehmer, der den geringsten Gesamtwert erhalten hat, erhält den übrigen Geldbetrag", 0,
				"Falsch; der Restbetrag wird fair zwischen allen Teilnehmern aufgeteilt");
		qRemainingMoney.addAnswer("Das übrige Geld wird zwischen allen Teilnehmern gleichermaßen aufgeteilt", 1,
				"Richtig!");
		qRemainingMoney.addAnswer(
				"Das geld wird übrig gelassen, da jeder Teilnehmer bereits seinen subjektiv wahrgenommenen fairen anteil erhalten hat",
				0, "Falsch; der Restbetrag wird fair zwischen allen Teilnehmern aufgeteilt");
		sb.lang.addMCQuestion(qRemainingMoney);
	}

	/**
	 * Reset probabilities for the apperance of the quiz questions.
	 */
	public static void resetProbabilities() {
		probTotalSum = 0.6;
		probFairShare = 0.7;
		probItemWinner = 0.7;
		probPayOrReceive = 0.7;
	}

	/*
	 * MISC HELPING FUNCTIONS
	 */

	/**
	 * Format doubles in money-like fashion.
	 * 
	 * @param d
	 *            a double value
	 * @return money representation of given double
	 */
	public static String format(double d) {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
		DecimalFormat df = new DecimalFormat("#.00", otherSymbols);
		return df.format(d);
	}

	/**
	 * Remove all highlightings from matrix.
	 */
	public static void unhighlightMatrix() {
		for (int i = 0; i < sb.matrix.getNrRows(); i++) {
			for (int j = 0; j < sb.matrix.getNrCols(); j++) {

				sb.matrix.unhighlightCell(i, j, null, null);
				sb.matrix.unhighlightElem(i, j, null, null);
			}
		}
	}

	/**
	 * Remove all highlightings from stats.
	 */
	public static void unhighlightStats() {
		for (Text n : sb.playerNameLabels) {
			n.changeColor(null, Color.black, null, null);
		}
	}

	/**
	 * Highlight pseudocode
	 * 
	 * @param line
	 *            line to be highlighted
	 */
	public static void setPseudocode(int line) {
		for (int i = 0; i < sb.code.length(); i++)
			sb.code.unhighlight(i);
		if (line < 0)
			return;
		sb.code.highlight(line);
	}

	/**
	 * Gray out already assigned items in matrix.
	 * 
	 * @param line
	 *            corresponding line
	 * @param except
	 *            the column of the item winner stays 'ungrayed'
	 */
	public static void grayOutMatrixLine(int line, int except) {
		for (int col = 1; col < sb.matrix.getNrCols(); col++) {
			if (col != except) {
				sb.matrix.setGridTextColor(line, col, Color.gray, null, null);
			}
		}
	}

	/**
	 * Reset table of content flags.
	 */
	public static void resetTOC() {
		tocFairShare = false;
		tocAssignItems = false;
		tocSettleDifference = false;
	}
}
