package generators.misc.sealedbid;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * @author Sebastian Ritzenhofen, Felix Rauterberg
 * 
 *         This class does the pre- and postproduction for the actual animation
 *         like introduction/outro texts and the overall display of the
 *         animation layout.
 */
public class Setup {

	/**
	 * Reference on sealed bid instance.
	 */
	public static SealedBid sb;

	/**
	 * Creates intro text.
	 */
	public static void generateSealedBidDescription() {

		/*
		 * SEALED BID DESCRIPTION
		 */

		sb.header = sb.lang.newText(new Coordinates(20, 30), "Sealed Bid", "header", null);
		sb.header.setFont(sb.headerFont, null, null);

		// generate the description text
		sb.algoDescs = new ArrayList<Text>();
		sb.algoDescs.add(sb.lang.newText(new Offset(0, 20, "header", "SW"),
				"In verschiedenen Situationen - etwa bei einer Erbschaft oder Haushaltsaufloesung - kann es dazu kommen, dass eine Menge von Gegenstaenden unter einer Gruppe von",
				"algoDesc1", null));
		sb.algoDescs.add(sb.lang.newText(new Offset(0, 3, "algoDesc1", "SW"),
				"Menschen aufgeteilt werden muss. Hierbei moechte natuerlich jeder Teilnehmer einen fairen Anteil der aufzuteilenden Menge erhalten. Die Sealed Bid Methode von Knaster",
				"algoDesc2", null));
		sb.algoDescs.add(sb.lang.newText(new Offset(0, 3, "algoDesc2", "SW"),
				"ist ein Algorithmus, der dies ermoeglicht, indem er fuer eine beliebige diskrete Menge und beliebig viele Teilnehmer eine faire Aufteilung der Menge generiert. Wie aber",
				"algoDesc3", null));
		sb.algoDescs.add(sb.lang.newText(new Offset(0, 3, "algoDesc3", "SW"),
				"kann man eine solche 'faire' Verteilung gewaehrleisten?", "algoDesc4", null));

		sb.algoDescs.add(sb.lang.newText(new Offset(0, 15, "algoDesc4", "SW"),
				"Das Verfahren, dessen woertliche Uebersetzung etwa 'verdecktes Gebot' bedeutet, beruht auf einem Versteigerungs-/Auktionsprinzip:",
				"algoDesc5", null));
		sb.algoDescs.add(sb.lang.newText(new Offset(0, 3, "algoDesc5", "SW"),
				"Jeder Teilnehmer gibt zunaechst fuer alle Gegenstaende/Items der aufzuteilenden Menge ein fuer die anderen nicht sichtbares Gebot ab, das dem subjektiv geschaetzten Wert",
				"algoDesc6", null));
		sb.algoDescs.add(sb.lang.newText(new Offset(0, 3, "algoDesc6", "SW"),
				"dieses Teilnehmers fuer das betroffene Item entspricht. Anschliessend werden die Gebote aller Teilnehmer aufgedeckt, und fuer jeden Teilnehmer T wird zunaechst ermittelt,",
				"algoDesc7", null));
		sb.algoDescs.add(sb.lang.newText(new Offset(0, 3, "algoDesc7", "SW"),
				"welcher Wert aus Sicht von T einem fairen Anteil (fair share) entspricht; dazu betrachtet man die Summe aller Gebote von T, die seiner Wertschaetzung der gesamten zu",
				"algoDesc8", null));
		sb.algoDescs.add(sb.lang.newText(new Offset(0, 3, "algoDesc8", "SW"),
				"verteilenden Menge entspricht, und dividiert diese Summe durch die Anzahl aller Teilnehmer. Jeder Teilnehmer T wird sich fair behandelt fuehlen, wenn er mindestens",
				"algoDesc9", null));
		sb.algoDescs.add(sb.lang.newText(new Offset(0, 3, "algoDesc9", "SW"),
				"diesen fairen Anteil erhaelt, der ja auf seiner eigenen Wertschaetzung der Menge beruht.",
				"algoDesc10", null));
		sb.algoDescs.add(sb.lang.newText(new Offset(0, 3, "algoDesc10", "SW"),
				"Im naechsten Schritt werden alle Items der Menge an den jeweils Hoechstbietenden verteilt. Dabei koennen natuerlich einige Teilnehmer weniger oder mehr bekommen, als",
				"algoDesc11", null));
		sb.algoDescs.add(sb.lang.newText(new Offset(0, 3, "algoDesc11", "SW"),
				"ihnen ihrer Meinung nach zusteht. Um nun zu gewaehrleisten, dass alle ihren fairen Anteil erzielen, zahlen alle Teilnehmer die Differenz zwischen dem, was sie in Form",
				"algoDesc12", null));
		sb.algoDescs.add(sb.lang.newText(new Offset(0, 3, "algoDesc12", "SW"),
				"von Gegenstaenden erhalten haben und ihrem als fair empfundenen Anteil in eine Gemeinschaftskasse. Teilnehmer, die mehr Gegenstaende erhalten haben, als ihnen (ihrer",
				"algoDesc13", null));
		sb.algoDescs.add(sb.lang.newText(new Offset(0, 3, "algoDesc13", "SW"),
				"eigenen Schaetzung nach) zusteht, zahlen also in die Kasse ein, und Teilnehmer, die zu wenig erhalten haben, erhalten Geld aus der Kasse, um auf ihren fairen Anteil zu",
				"algoDesc14", null));
		sb.algoDescs.add(sb.lang.newText(new Offset(0, 3, "algoDesc14", "SW"),
				"kommen. Nach diesem Schritt ist eine faire Aufteilung erzielt: jeder Teilnehmer hat (in Form von Gegenstaenden und/oder Geld) einen Wert erhalten, der dem entspricht,",
				"algoDesc15", null));
		sb.algoDescs.add(sb.lang.newText(new Offset(0, 3, "algoDesc15", "SW"),
				"was er selbst durch seine Schaetzung der Items als fair bewertet hat.", "algoDesc16", null));
		sb.algoDescs.add(sb.lang.newText(new Offset(0, 3, "algoDesc16", "SW"),
				"Haeufig kommt es bei dem Verfahren dazu, dass am Ende noch Geld in der Gemeinschaftskasse uebrig ist - in diesem Fall wird der Restbetrag gleichmaessig zwischen allen",
				"algoDesc17", null));
		sb.algoDescs.add(sb.lang.newText(new Offset(0, 3, "algoDesc17", "SW"),
				"Teilnehmern aufgeteilt, sodass am Schluss sogar mehr fuer die Teilnehmer herausspringt als ihr fairer Anteil.",
				"algoDesc18", null));

		sb.algoDescs.add(sb.lang.newText(new Offset(0, 15, "algoDesc18", "SW"),
				"Die folgende Animation zur Darstellung der Sealed Bid-Methode setzt sich aus drei Komponenten zusammen:",
				"algoDesc19", null));
		sb.algoDescs.add(sb.lang.newText(new Offset(0, 3, "algoDesc19", "SW"),
				"Eine Beschreibung des Ablaufs in Form von Pseudocode ermoeglicht es mitzuverfolgen, in welchem Schritt des Algorithmus man sich gerade befindet. Passend zum",
				"algoDesc20", null));
		sb.algoDescs.add(sb.lang.newText(new Offset(0, 3, "algoDesc20", "SW"),
				"Pseudocode visualisiert eine sich sukzessiv aufbauende Tabelle die Durchfuehrung der Methode fuer die konkret angegebenen Parameter (also fuer die angegebenen Items und",
				"algoDesc21", null));
		sb.algoDescs.add(sb.lang.newText(new Offset(0, 3, "algoDesc21", "SW"),
				"zugehoerigen Schaetzungen der Teilnehmer). Schliesslich fasst eine Statistik-Tabelle nochmals alle wichtigen Werte der Teilnehmer kompakt zusammen, die sich im Laufe des",
				"algoDesc22", null));
		sb.algoDescs.add(sb.lang.newText(new Offset(0, 3, "algoDesc22", "SW"),
				"Algorithmus ergeben (z.B. erhaltenen Wert in Form von Items, erhaltenen Geldwert oder insgesamt erhaltenen Wert).",
				"algoDesc23", null));
		sb.algoDescs.add(sb.lang.newText(new Offset(0, 15, "algoDesc23", "SW"),
				"Anmerkung: Der Einfachheit halber koennen fuer die Gebote/Bids nur ganzzahlige Werte (Integer) angegeben werden.",
				"algoDesc24", null));
		// set font to description elements
		for (Text desc : sb.algoDescs) {
			desc.setFont(sb.textFont, null, null);
		}
	}

	/**
	 * Creates general algorithm layout/perspective.
	 */
	public static void generateSealedBidPerspective() {

		/*
		 * CLEANUP
		 */

		sb.lang.nextStep("Einleitung");
		sb.lang.hideAllPrimitivesExcept(sb.header);
		Helper.resetProbabilities();
		Helper.resetTOC();

		/*
		 * PSEUDO CODE
		 */

		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 16));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				Helper.pseudoCodeProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Helper.pseudoCodeProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));

		sb.code = sb.lang.newSourceCode(new Offset(20, 20, "header", "S"), "pseudoCode", null, scProps);
		sb.code.addCodeLine("01. reveal bids of all participants", null, 0, null);
		sb.code.addCodeLine("02. for each participant:", null, 0, null);
		sb.code.addCodeLine("03.     calculate item set value (total sum of bids)", null, 0, null);
		sb.code.addCodeLine("04.     calculate fair share (total sum / number of participants)", null, 0, null);
		sb.code.addCodeLine("05. for each item:", null, 0, null);
		sb.code.addCodeLine("06.     assign to highest bidder", null, 0, null);
		sb.code.addCodeLine("07. for each participant:", null, 0, null);
		sb.code.addCodeLine("08.     pay/receive difference 'received item value - fair share'", null, 0, null);
		sb.code.addCodeLine("09. split remaining money in register between all participants", null, 0, null);

		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		sb.codeRect = sb.lang.newRect(new Offset(-5, -5, sb.code, "NW"), new Offset(5, 5, sb.code, "SE"), "codeRect",
				null, rectProps);

		/*
		 * LABELS
		 */

		// create player stats header
		sb.playerStatsLabel = sb.lang.newText(new Offset(0, 50, "pseudoCode", "SW"), "Teilnehmerstatistik",
				"playerStats", null);
		sb.playerStatsLabel.setFont(sb.matrixHeaderFont, null, null);

		// create register header
		sb.registerLabel = sb.lang.newText(new Offset(400, 0, "playerStats", "NE"), "Kasse: 0.00", "register", null);
		sb.registerLabel.setFont(sb.matrixHeaderFont, null, null);

		// initialize lists for different player stats
		String previousOffset = "playerStats";
		sb.playerNameLabels = new ArrayList<Text>();
		sb.playerSumLabels = new ArrayList<Text>();
		sb.playerFairShareLabels = new ArrayList<Text>();
		sb.playerItemLabels = new ArrayList<Text>();
		sb.playerMoneyLabels = new ArrayList<Text>();
		sb.playerTotalLabels = new ArrayList<Text>();

		// for each player
		for (int i = 0; i < sb.numPlayers; i++) {

			// get name and id of player
			String name = sb.players.get(i).NAME;
			int id = sb.players.get(i).ID;

			// create labels
			Text playerLabel = sb.lang.newText(new Offset(0, 20, previousOffset, "SW"), name + ":", name, null);
			Text sumLabel = sb.lang.newText(new Offset(0, 10, name, "SW"), "Subj. Gesamtwert: 0.00", "sum" + id, null);
			Text fairShareLabel = sb.lang.newText(new Offset(0, 10, "sum" + id, "SW"), "Subj. fairer Anteil: 0.00",
					"fairShare" + id, null);
			Text itemLabel = sb.lang.newText(new Offset(200, 0, "sum" + id, "NW"), "Erh. Wert (Items): 0.00",
					"items" + id, null);
			Text moneyLabel = sb.lang.newText(new Offset(0, 10, "items" + id, "SW"), "Erh. Wert (Geld): 0.00",
					"money" + id, null);
			Text totalLabel = sb.lang.newText(new Offset(200, 0, "items" + id, "NW"), "Erh. Wert (Gesamt): 0.00",
					"total" + id, null);

			// remember last name for next offset
			previousOffset = "fairShare" + id;

			// set fonts
			playerLabel.setFont(sb.textFont, null, null);
			sumLabel.setFont(sb.statsFont, null, null);
			fairShareLabel.setFont(sb.statsFont, null, null);
			itemLabel.setFont(sb.statsFont, null, null);
			moneyLabel.setFont(sb.statsFont, null, null);
			totalLabel.setFont(sb.statsFont, null, null);

			// store labels
			sb.playerNameLabels.add(playerLabel);
			sb.playerSumLabels.add(sumLabel);
			sb.playerFairShareLabels.add(fairShareLabel);
			sb.playerItemLabels.add(itemLabel);
			sb.playerMoneyLabels.add(moneyLabel);
			sb.playerTotalLabels.add(totalLabel);
		}

		/*
		 * MATRIX
		 */

		// create matrix
		int rows = sb.numItems + 1;
		int rowsExtension = 5;
		int columns = sb.numPlayers + 1;

		String[][] matrix = new String[rows + rowsExtension][columns];
		matrix[0][0] = "Items";

		// fill line headers ( = items)
		for (int i = 1; i < rows; i++)
			matrix[i][0] = String.valueOf(sb.items.get(i - 1).NAME);

		// fill extended line headers
		matrix[rows][0] = "Subj. Gesamtwert";
		matrix[rows + 1][0] = "Subj. fairer Anteil";
		matrix[rows + 2][0] = "Erhaltener Wert";
		matrix[rows + 3][0] = "Zahlung in Kasse";
		matrix[rows + 4][0] = "Kassenueberschuss";

		// fill column headers ( = players)
		for (int j = 1; j < columns; j++)
			matrix[0][j] = String.valueOf(sb.players.get(j - 1).NAME);

		// fill bids of all players for all items
		for (int i = 1; i < rows; i++) {
			for (int j = 1; j < columns; j++) {
				matrix[i][j] = String.valueOf(Helper.format(sb.players.get(j - 1).bid(sb.items.get(i - 1))));
			}
		}

		// create StringMatrix
		sb.matrix = sb.lang.newStringMatrix(new Offset(100, -10, "pseudoCode", "NE"), matrix, "matrix", null);

		// set properties
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				sb.matrix.setGridTextColor(i, j,
						(Color) Helper.matrixProps.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY), null, null);
				sb.matrix.setGridHighlightTextColor(i, j,
						(Color) Helper.matrixProps.get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY), null, null);
			}
		}

		// set fonts for basic matrix elements
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {

				// headlines
				if (i == 0) {
					sb.matrix.setGridFont(i, j, sb.matrixHeaderFont, null, null);
				}

				// normal elements
				else {
					sb.matrix.setGridFont(i, j, sb.textFont, null, null);

					// hide elements at first
					if (j != 0)
						sb.matrix.setGridTextColor(i, j, Color.white, null, null);
				}
			}
		}

		// set fonts for extension matrix elements
		for (int i = rows; i < rows + rowsExtension; i++) {
			for (int j = 0; j < columns; j++) {

				// headlines
				if (j == 0) {
					sb.matrix.setGridFont(i, j, sb.matrixHeaderFont, null, null);
				}

				// normal elements
				else {
					sb.matrix.put(i, j, "", null, null);
					sb.matrix.setGridFont(i, j, sb.textFont, null, null);
				}

				// hide all extension elements at first
				sb.matrix.setGridTextColor(i, j, Color.white, null, null);
			}
		}
	}

	/**
	 * Creates outro text.
	 */
	public static void generateOutroText() {

		// step
		sb.lang.nextStep();

		// cleanup
		Helper.unhighlightMatrix();
		Helper.setPseudocode(-1);
		Helper.resetProbabilities();
		Helper.resetTOC();

		// step
		sb.lang.nextStep();
		sb.lang.hideAllPrimitivesExcept(sb.header);

		// generate the description text
		sb.outroDescs = new ArrayList<Text>();
		sb.outroDescs.add(sb.lang.newText(new Offset(0, 20, "header", "SW"),
				"Die Darstellung zeigt, dass das Sealed Bid Verfahren von Knaster die proportionale Aufteilung von diskreten Mengen unter beliebig vielen Teilnehmern auf systematische",
				"outroDesc1", null));
		sb.outroDescs.add(sb.lang.newText(new Offset(0, 3, "outroDesc1", "SW"),
				"und nachvollziehbare Art und Weise ermoeglicht. Wie geplant erhaelt jeder Teilnehmer garantiert den Anteil der Gesamtmenge (in Form von Gegenstaenden und/oder Geld), den",
				"outroDesc2", null));
		sb.outroDescs.add(sb.lang.newText(new Offset(0, 3, "outroDesc2", "SW"),
				"er als seiner eigenen Schaetzung nach fair erachtet; in den allermeisten Anwendungen von Sealed Bid wird zudem ein Rest in der Gemeinschaftskasse verbleiben, sodass am",
				"outroDesc3", null));
		sb.outroDescs.add(sb.lang.newText(new Offset(0, 3, "outroDesc3", "SW"),
				"Ende fuer jeden Teilnehmer sogar mehr Gesamtwert entsteht als es seiner eigenen Schaetzung nach angemessen waere.",
				"outroDesc4", null));

		sb.outroDescs.add(sb.lang.newText(new Offset(0, 15, "outroDesc4", "SW"),
				"Abschliessende Anmerkungen zum Verfahren: Eine Kernkomponente von Knasters Sealed Bid Methode ist die Gemeinschaftskasse, aus der Teilnehmer, die durch das Ersteigern",
				"outroDesc5", null));
		sb.outroDescs.add(sb.lang.newText(new Offset(0, 3, "outroDesc5", "SW"),
				"von Gegenstaenden nicht auf ihren subjektiv als fair empfundenen Anteil gekommen sind, die ihnen zustehende Differenz in Form von Geld erhalten. Dieses Geld wird durch",
				"outroDesc6", null));
		sb.outroDescs.add(sb.lang.newText(new Offset(0, 3, "outroDesc6", "SW"),
				"die Einzahlung anderer Teilnehmer generiert, die wiederum mehr Gegenstandswert erhalten haben als ihnen ihrer eigenen Schaetzung nach zusteht. Mit einem Induktions-",
				"outroDesc7", null));
		sb.outroDescs.add(sb.lang.newText(new Offset(0, 3, "outroDesc7", "SW"),
				"beweis laesst sich zeigen, dass die Differenz der Kasse nach Abrechnung aller Teilnehmer niemals negativ ist; somit ist garantiert, dass die Betraege derjenigen",
				"outroDesc8", null));
		sb.outroDescs.add(sb.lang.newText(new Offset(0, 3, "outroDesc8", "SW"),
				"Teilnehmer, die einen Anspruch auf Geld aus der Gemeinschaftskasse haben, durch die Einzahlungen anderer Teilnehmer gedeckt sind.",
				"outroDesc9", null));
		sb.outroDescs.add(sb.lang.newText(new Offset(0, 3, "outroDesc9", "SW"),
				"Interessant ist auch, dass das Verfahren zwar proportional faire Aufteilungen generiert, dabei allerdings keine Neidfreiheit garantiert. Dies kann man sich leicht an",
				"outroDesc10", null));
		sb.outroDescs.add(sb.lang.newText(new Offset(0, 3, "outroDesc10", "SW"),
				"einem Beispiel ueberlegen: Erhalten zwei Teilnehmer A und B bei einer Sealed Bid Auktion keine Gegenstaende, wird ihnen ihr jeweiliger fairer Anteil in Geldform",
				"outroDesc11", null));
		sb.outroDescs.add(sb.lang.newText(new Offset(0, 3, "outroDesc11", "SW"),
				"zugewiesen; hat nun Teilnehmer A aufgrund hoeherer Gebote einen hoeheren fairen Anteil, wird er mehr Geld erhalten als B, und B wird A trotz seines fairen Anteils",
				"outroDesc12", null));
		sb.outroDescs.add(sb.lang.newText(new Offset(0, 3, "outroDesc12", "SW"), "beneiden.", "outroDesc13", null));

		// set font to description elements
		for (Text desc : sb.outroDescs) {
			desc.setFont(sb.textFont, null, null);
		}

		// step
		sb.lang.nextStep("Fazit");
		sb.lang.hideAllPrimitivesExcept(sb.header);
	}
}
