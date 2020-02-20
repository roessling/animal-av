/*
 * SealedBidGenerator.java
 * Sebastian Ritzenhofen, Felix Rauterberg, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc.sealedbid;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

public class SealedBidGenerator implements ValidatingGenerator {
	private Language lang;
	private int[][] playerBids;
	private String[] items;
	private boolean useQuestions;

	public void init() {
		lang = new AnimalScript("Knaster's Sealed Bid Methode ", "Sebastian Ritzenhofen, Felix Rauterberg", 800, 600);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {

		// read input
		useQuestions = (boolean) primitives.get("useQuestions");
		playerBids = (int[][]) primitives.get("playerBids");
		items = (String[]) primitives.get("items");
		Helper.matrixProps = (MatrixProperties) props.get(0);
		Helper.pseudoCodeProps = (SourceCodeProperties) props.get(1);

		// create sealed bid instance
		SealedBid sealedBid = new SealedBid(lang, useQuestions, items, playerBids);

		// show sealed bid description
		Setup.generateSealedBidDescription();

		// generate sealed bid perspective
		Setup.generateSealedBidPerspective();

		// execute sealed bid algorithm
		sealedBid.sealedBid();

		// show outro text
		Setup.generateOutroText();

		// finalize generation
		lang.finalizeGeneration();

		// return built lang string
		return lang.toString();
	}

	public String getName() {
		return "Knaster's Sealed Bid Methode ";
	}

	public String getAlgorithmName() {
		return "Sealed Bid";
	}

	public String getAnimationAuthor() {
		return "Sebastian Ritzenhofen, Felix Rauterberg";
	}

	public String getDescription() {
		return "Die Sealed Bid Methode von Knaster ist ein Algorithmus zum fairen Aufteilen einer diskreten Menge"
				+ "\n"
				+ "unter beliebig vielen Teilnehmern. Bei diesem Verfahren geben die Teilnehmer zunächst verdeckte "
				+ "\n"
				+ "Gebote auf alle Elemente der zu teilenden Menge ab. Anhand dieser Gebote lässt sich für jeden "
				+ "\n"
				+ "Teilnehmer ein Wert errechnen, der dessen subjektiv wahrgenommene faire Aufteilung repräsentiert."
				+ "\n" + "Anschließend können die Gegenstände der Menge den jeweils Höchstbietenden zugeteilt werden; "
				+ "\n"
				+ "Teilnehmer, die aus ihrer Sicht zu viel oder zu wenig Wert in Form von Gegenständen erhalten haben, "
				+ "\n" + "kompensieren dies durch die Einzahlung in (bzw. Auszahlung aus) einer gemeinsamen Kasse.";
	}

	public String getCodeExample() {
		return "// assign each item to the highest bidder" + "\n" + "for (Item item : items) {" + "\n"
				+ " assignToHighestBidder(players, item);" + "\n" + "}" + "\n" + "\n"
				+ "for (Player player : players) {" + "\n" + "	" + "\n"
				+ "	// calculate the fair share for each player (from his point of view)" + "\n"
				+ "	player.calculateFairShare(players.size());" + "\n" + "\n"
				+ "	// pay/receive the difference between fairShare and received item value " + "\n"
				+ "	player.settleDifference();" + "\n" + "}" + "\n" + "\n"
				+ "// divide what is left in the shared cash register between all participants" + "\n"
				+ "divideRemainingMoney(players);";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer properties, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {

		String[] testItems = (String[]) primitives.get("items");
		int[][] testBids = (int[][]) primitives.get("playerBids");

		// only non-negative bids are valid
		for (int[] row : testBids) {
			for (int elem : row) {
				if (elem < 0) {
					return false;
				}
			}
		}

		// there must be at least two bidders
		if (testBids.length < 2) {
			return false;
		}

		// there must be at least one item
		if (testItems.length <= 0) {
			return false;
		}

		// number of items in item list and bid array must match
		for (int[] rows : testBids) {
			if (rows.length != testItems.length) {
				return false;
			}
		}

		return true;
	}

}