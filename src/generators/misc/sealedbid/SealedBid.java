package generators.misc.sealedbid;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;

/**
 * @author Sebastian Ritzenhofen, Felix Rauterberg
 * 
 *         This class contains the main algorithm of the sealed bid auction.
 */
public class SealedBid {

	// sealed bid attributes
	public double cashRegister;
	public int numItems;
	public int numPlayers;
	public List<Item> items;
	public List<Player> players;

	// algoanim attributes
	public Language lang;
	public StringMatrix matrix;
	public SourceCode code;
	public Rect codeRect;
	public Text header;
	public Text playerStatsLabel;
	public Text registerLabel;
	public List<Text> algoDescs;
	public List<Text> animationDescs;
	public List<Text> outroDescs;
	public List<Text> playerNameLabels;
	public List<Text> playerSumLabels;
	public List<Text> playerFairShareLabels;
	public List<Text> playerItemLabels;
	public List<Text> playerMoneyLabels;
	public List<Text> playerTotalLabels;
	public List<StringArray> playerItemLists;
	public boolean useQuestions;

	// Constants
	public final Font textFont = new Font("SansSerif", Font.PLAIN, 16);
	public final Font headerFont = new Font("SansSerif", Font.BOLD, 24);
	public final Font matrixHeaderFont = new Font("SansSerif", Font.BOLD, 16);
	public final Font statsFont = new Font("SansSerif", Font.PLAIN, 12);

	public SealedBid(Language l, boolean useQuestions, String[] items, int[][] bids) {

		// set language reference
		lang = l;
		lang.setStepMode(true);

		// instantiate items and players
		this.numItems = items.length;
		this.numPlayers = bids.length;
		this.items = new ArrayList<Item>();
		this.players = new ArrayList<Player>();
		this.useQuestions = useQuestions;
		for (int i = 0; i < numItems; i++)
			this.items.add(new Item(items[i], i));
		for (int i = 0; i < numPlayers; i++)
			this.players.add(new Player(i + 1, bids[i]));

		// introduce this instance to some other classes
		Setup.sb = this;
		Helper.sb = this;
		Player.sb = this;
	}

	/**
	 * The main sealed bid algorithm
	 */
	public void sealedBid() {

		// reveal sealed bids of all players for all items
		revealSealedBids();

		// calculate the fair share for each player
		Helper.firstMatrixExtension();
		for (Player player : players) {
			player.calculateFairShare(players.size());
		}

		// assign each item to the highest bidder
		Helper.secondMatrixExtension();
		for (Item item : items) {
			assignToHighestBidder(players, item);
		}

		// pay/receive the difference between fairShare and received item value for each
		// player
		Helper.thirdMatrixExtension();
		for (Player player : players) {
			player.settleDifference();
		}

		// divide what is left in the shared cash register between all participants
		Helper.fourthMatrixExtension();
		divideRemainingMoney(players);
	}

	/**
	 * Uncover sealed bids of all participants.
	 */
	private void revealSealedBids() {

		// step
		lang.nextStep();

		// adjust pseudocode
		Helper.setPseudocode(0);

		// step
		lang.nextStep("Aufdecken der Gebote");

		// show bids of all players
		for (int i = 1; i < matrix.getNrRows(); i++) {
			for (int j = 1; j < matrix.getNrCols(); j++) {
				matrix.setGridTextColor(i, j,
						(Color) Helper.matrixProps.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY), null, null);
				matrix.highlightElem(i, j, null, null);
			}
		}
	}

	/**
	 * Assign given item to highest-bidding player.
	 * 
	 * @param players
	 *            list of participating players
	 * @param item
	 *            item to be assigned to highest bidder
	 */
	private void assignToHighestBidder(List<Player> players, Item item) {

		// step
		if (!Helper.tocAssignItems) {
			lang.nextStep("Versteigerung der Items");
			Helper.tocAssignItems = true;
		} else {
			lang.nextStep();
		}

		// find highest bidder for the given item
		Player highestBidder = players.get(0);
		for (Player p : players) {
			if (p.bid(item) > highestBidder.bid(item)) {
				highestBidder = p;
			}
		}

		// assign the item to the highest bidder
		highestBidder.assignItem(item);

		// gray out the other bets from previous round (if not first round)
		if (item.INDEX > 0) {
			Helper.unhighlightMatrix();
			Helper.grayOutMatrixLine(item.INDEX, Helper.lastWinnerID);
		}
		Helper.lastWinnerID = highestBidder.ID;

		// highlight affected item
		matrix.highlightElem(item.INDEX + 1, 0, null, null);

		// pseudocode
		Helper.setPseudocode(4);

		// user interaction
		Helper.questionItemWinner(item.INDEX, highestBidder.ID);

		// step
		lang.nextStep();

		// pseudocode
		Helper.setPseudocode(5);

		// highlight highest bidder and his bid
		matrix.highlightElem(0, highestBidder.ID, null, null);
		matrix.highlightElem(item.INDEX + 1, highestBidder.ID, null, null);

		// step
		lang.nextStep();

		// add received value and highlight all received values and the sum
		matrix.put(numItems + 3, highestBidder.ID, String.valueOf(Helper.format(highestBidder.getReceivedItemValue())),
				null, null);
		matrix.setGridHighlightTextColor(numItems + 3, highestBidder.ID,
				(Color) Helper.matrixProps.get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY), null, null);
		matrix.highlightElem(numItems + 3, highestBidder.ID, null, null);
		for (Item i : items) {
			if (highestBidder.getItems().contains(i)) {
				matrix.highlightElem(i.INDEX + 1, highestBidder.ID, null, null);
			}
		}
		Text itemValLabel = playerItemLabels.get(highestBidder.ID - 1);
		itemValLabel.setText("Erh. Wert (Items): " + Helper.format(highestBidder.getReceivedItemValue()), null, null);
		playerTotalLabels.get(highestBidder.ID - 1).setText(
				"Erh. Wert (Gesamt): "
						+ Helper.format(highestBidder.getReceivedItemValue() + highestBidder.getReceivedMoneyValue()),
				null, null);
	}

	/**
	 * Divide remaining money in shared cash register fairly between all
	 * participants.
	 * 
	 * @param players
	 *            players participating in the auction
	 */
	private void divideRemainingMoney(List<Player> players) {

		// if the cash register is in the plus after everyone has his fair share, divide
		// it amongst all participants
		double remainingMoney = CashRegister.getBalance();
		double share = 0.0;
		if (remainingMoney > 0.0) {
			CashRegister.withdraw(remainingMoney);
			share = remainingMoney / ((double) numPlayers);
			for (Player p : players) {
				p.assignMoney(share);

			}
		}

		// user interaction
		Helper.questionRemainingMoney();

		// step
		lang.nextStep("Aufteilen des Kassenueberschusses");

		// set share for all players in matrix and highlight it
		int row = numItems + 5;
		for (int j = 1; j <= numPlayers; j++) {
			matrix.put(row, j, Helper.format(share), null, null);
			matrix.setGridHighlightTextColor(row, j,
					(Color) Helper.matrixProps.get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY), null, null);
			matrix.highlightElem(row, j, null, null);
			playerTotalLabels.get(j - 1)
					.setText("Erh. Wert (Gesamt): " + (Helper.format(
							players.get(j - 1).getReceivedItemValue() + players.get(j - 1).getReceivedMoneyValue())),
							null, null);
			playerMoneyLabels.get(j - 1).setText(
					"Erh. Wert (Geld): " + Helper.format(players.get(j - 1).getReceivedMoneyValue()), null, null);
		}
		registerLabel.setText("Kasse: 0.00", null, null);
	}

}
