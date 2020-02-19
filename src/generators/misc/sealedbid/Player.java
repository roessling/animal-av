package generators.misc.sealedbid;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;

/**
 * @author Sebastian Ritzenhofen, Felix Rauterberg
 * 
 *         Objects of this class represent a player/participant in the sealed
 *         bid auction.
 */
public class Player {

	/*
	 * ATTRIBUTES
	 */
	public static SealedBid sb;
	public final int ID;
	public final String NAME;
	private double fairShare;
	private double receivedItemValue;
	private double receivedMoneyValue;
	private List<Double> bids;
	private List<Item> receivedItems;

	/**
	 * Creates a player object.
	 * 
	 * @param id
	 *            player id
	 * @param bids
	 *            array of this player's bids
	 */
	public Player(int id, int[] bids) {
		this.ID = id;
		this.NAME = "Teilnehmer " + ID;
		this.receivedItemValue = 0.0;
		this.receivedMoneyValue = 0.0;
		this.receivedItems = new ArrayList<Item>();
		this.bids = new ArrayList<Double>();
		for (int i : bids) {
			this.bids.add(Double.valueOf(i));
		}
	}

	/**
	 * @return sum of value that this player received from getting items
	 */
	public double getReceivedItemValue() {
		return this.receivedItemValue;
	}

	/**
	 * @return sum of value that this player received by getting money
	 */
	public double getReceivedMoneyValue() {
		return this.receivedMoneyValue;
	}

	/**
	 * @return items this player received
	 */
	public Set<Item> getItems() {
		return new HashSet<Item>(receivedItems);
	}

	/**
	 * @param item
	 *            an item
	 * @return the bid of this player on the given item
	 */
	public double bid(Item item) {
		return bids.get(item.INDEX);
	}

	/**
	 * @param item
	 *            item to be assigned to this player
	 */
	public void assignItem(Item item) {
		receivedItems.add(item);
		receivedItemValue += bids.get(item.INDEX);
	}

	/**
	 * @param amount
	 *            amount of money to be assigned to this player
	 */
	public void assignMoney(double amount) {
		receivedMoneyValue += amount;
	}

	/**
	 * Calculates sum of bids and fair share for this player.
	 * 
	 * @param numPlayers
	 *            number of participating players in the sealed bid auction
	 */
	public void calculateFairShare(int numPlayers) {

		// calculate total value of item set
		double sumOfBids = 0.0;
		for (Double bid : bids) {
			sumOfBids += bid;
		}

		// calculate fair share
		fairShare = sumOfBids / ((double) numPlayers);

		// step
		if (!Helper.tocFairShare) {
			sb.lang.nextStep("Berechnung der (subjektiv) fairen Anteile");
			Helper.tocFairShare = true;
		} else {
			sb.lang.nextStep();
		}

		// cleanup
		Helper.unhighlightMatrix();

		// pseudocode
		Helper.setPseudocode(1);

		// highlight player and value estimations of items
		for (int i = 0; i < sb.numItems + 1; i++) {
			sb.matrix.highlightElem(i, ID, null, null);
		}

		// user interaction
		Helper.questionTotalSum(this.ID, sumOfBids);

		// step
		sb.lang.nextStep();

		// pseudocode
		Helper.setPseudocode(2);

		// set and highlight his total value sum
		Helper.unhighlightMatrix();
		sb.matrix.highlightElem(0, ID, null, null);
		sb.matrix.put(sb.numItems + 1, ID, String.valueOf(Helper.format(sumOfBids)), null, null);
		sb.matrix.setGridHighlightTextColor(sb.numItems + 1, ID,
				(Color) Helper.matrixProps.get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY), null, null);
		sb.matrix.highlightElem(sb.numItems + 1, ID, null, null);
		Text sumOfBidsLabel = sb.playerSumLabels.get(this.ID - 1);
		sumOfBidsLabel.setText("Subj. Gesamtwert: " + Helper.format(sumOfBids), null, null);

		// user interaction
		Helper.questionFairShare(this.ID, this.fairShare, sumOfBids);

		// step
		sb.lang.nextStep();

		// pseudocode
		Helper.setPseudocode(3);

		// set and highlight his fair share
		sb.matrix.unhighlightElem(sb.numItems + 1, ID, null, null);
		sb.matrix.put(sb.numItems + 2, ID, String.valueOf(Helper.format(fairShare)), null, null);
		sb.matrix.setGridHighlightTextColor(sb.numItems + 2, ID,
				(Color) Helper.matrixProps.get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY), null, null);
		sb.matrix.highlightElem(sb.numItems + 2, ID, null, null);
		Text fairShareLabel = sb.playerFairShareLabels.get(this.ID - 1);
		fairShareLabel.setText("Subj. fairer Anteil: " + Helper.format(fairShare), null, null);
	}

	/**
	 * Pay/receive difference to fair share in/from the shared cash register
	 */
	public void settleDifference() {
		double difference = receivedItemValue - fairShare;
		receivedMoneyValue -= difference;

		// pay into shared cash register if value of received items is higher than fair
		// share
		if (difference > 0) {
			CashRegister.pay(difference);
		}

		// withdraw from shared cash register if value of received items is less than
		// fair share
		if (difference < 0) {
			CashRegister.withdraw(Math.abs(difference));
		}

		// step
		if (!Helper.tocSettleDifference) {
			sb.lang.nextStep("Differenzausgleich zum fairen Anteil");
			Helper.tocSettleDifference = true;
		} else {
			sb.lang.nextStep();
		}

		// cleanup
		Helper.unhighlightMatrix();

		// pseudocode
		Helper.setPseudocode(6);

		// highlight player name, fair share and received value
		sb.matrix.highlightElem(0, this.ID, null, null);
		sb.matrix.highlightElem(sb.numItems + 2, this.ID, null, null);
		sb.matrix.highlightElem(sb.numItems + 3, this.ID, null, null);

		// user interaction
		Helper.questionPayOrReceive(this.ID, difference);

		// step
		sb.lang.nextStep();

		// pseudocode
		Helper.setPseudocode(7);

		// put register payment in matrix and highlight it
		Text moneyLabel = sb.playerMoneyLabels.get(this.ID - 1);
		if (difference > 0) {
			sb.matrix.put(sb.numItems + 4, this.ID, "+" + String.valueOf(Helper.format(difference)), null, null);
			sb.matrix.setGridHighlightTextColor(sb.numItems + 4, this.ID,
					(Color) Helper.matrixProps.get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY), null, null);
			moneyLabel.setText("Erh. Wert (Geld): -" + Helper.format(difference), null, null);
		} else {
			sb.matrix.put(sb.numItems + 4, this.ID, String.valueOf(Helper.format(difference)), null, null);
			sb.matrix.setGridHighlightTextColor(sb.numItems + 4, ID,
					(Color) Helper.matrixProps.get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY), null, null);
			moneyLabel.setText("Erh. Wert (Geld): " + Helper.format(-1.0 * difference), null, null);
		}
		sb.matrix.highlightElem(sb.numItems + 4, this.ID, null, null);

		// set register and total received value
		sb.registerLabel.setText("Kasse: " + Helper.format(CashRegister.getBalance()), null, null);
		sb.playerTotalLabels.get(this.ID - 1)
				.setText("Erh. Wert (Gesamt): " + Helper.format(receivedItemValue + receivedMoneyValue), null, null);
	}
}
