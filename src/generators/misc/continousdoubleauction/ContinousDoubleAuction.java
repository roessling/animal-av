package generators.misc.continousdoubleauction;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class ContinousDoubleAuction implements ValidatingGenerator {
	private Language lang;
	private RectProperties bids;
	private RectProperties asks;
	private RectProperties marketOrderBuy;
	private RectProperties marketOrderSell;
	private RectProperties borderProps;
	private Text descriptionText;
	private TextProperties titleProps;
	private TextProperties infoTextProps;
	private TextProperties outroTextProps;
	private TextProperties orderAndOrderBookProperties;
	private TextProperties resultProps;
	private TextProperties introductionProperties;
	private SourceCodeProperties codeProps;
	private SourceCode codeTrade;
	private List<AuctionElement> orders;
	private List<Text> resultTexts;
	private List<AuctionElement> buyElements;
	private List<AuctionElement> askElements;
	private List<Coordinates> buyPositions;
	private List<Coordinates> askPositions;

	private RectProperties getRectProperties(
			Triple<Double, Double, String> order) {
		switch (order.getY()) {
		case "BL":
			return bids;
		case "SL":
			return asks;
		case "BM":
			return marketOrderBuy;
		case "SM":
			return marketOrderSell;
		default:
			throw new RuntimeException("Unknown order type '" + order.getY()
					+ "'");
		}
	}

	private Comparator<AuctionElement> ascComp = new Comparator<AuctionElement>() {

		@Override
		public int compare(AuctionElement ae1, AuctionElement ae2) {
			double ae1Value = Double.parseDouble(ae1.getText().getText());
			double ae2Value = Double.parseDouble(ae2.getText().getText());

			if (ae1Value > ae2Value)
				return 1;
			else if (ae1Value < ae2Value)
				return -1;

			return 0;
		}
	};

	private Comparator<AuctionElement> descComp = new Comparator<AuctionElement>() {

		@Override
		public int compare(AuctionElement ae1, AuctionElement ae2) {
			double ae1Value = Double.parseDouble(ae1.getText().getText());
			double ae2Value = Double.parseDouble(ae2.getText().getText());

			if (ae1Value > ae2Value)
				return -1;
			else if (ae1Value < ae2Value)
				return 1;

			return 0;
		}
	};

	public void init() {
		lang = new AnimalScript(getName(), getAnimationAuthor(), 1400, 1050);
		lang.setStepMode(true);
	}

	private int createDoubleArray() {
		int startX = 160;

		// Limited order buy
		lang.newText(new Coordinates(20, 42), "Buy limited order (Bids):", "",
				null, this.orderAndOrderBookProperties);
		lang.newRect(new Coordinates(startX, 42), new Coordinates(startX + 20,
				60), "buyTitle", null, this.bids);
		// Limited order sell
		lang.newText(new Coordinates(20, 62), "Sell limited order (Asks):", "",
				null, this.orderAndOrderBookProperties);
		lang.newRect(new Coordinates(startX, 62), new Coordinates(startX + 20,
				80), "sellTitle", null, this.asks);
		// Market order buy
		lang.newText(new Coordinates(20, 82), "Market order buy:", "", null,
				this.orderAndOrderBookProperties);
		lang.newRect(new Coordinates(startX, 82), new Coordinates(startX + 20,
				100), "marketOrderBuyTitle", null, this.marketOrderBuy);
		// Market order sell
		lang.newText(new Coordinates(20, 102), "Market order sell:", "", null,
				this.orderAndOrderBookProperties);
		lang.newRect(new Coordinates(startX, 102), new Coordinates(startX + 20,
				120), "marketOrderSellTitle", null, this.marketOrderSell);

		startX = 187;
		int startY = 42;

		int i = 0;
		for (AuctionElement ae : orders) {
			Triple<Double, Double, String> order = ae.getOrder();
			int xAddition = (i % 10) * 47;
			int yAddition = (i / 10) * 27;
			Rect rect = lang.newRect(new Coordinates(startX + xAddition, startY
					+ yAddition), new Coordinates(startX + 40 + xAddition,
					startY + 20 + yAddition), "buyRect" + i, null,
					getRectProperties(order));
			Text text = lang
					.newText(new Coordinates(startX + 5 + xAddition, startY + 2
							+ yAddition), order.getY().equals("SM")
							|| order.getY().equals("BM") ? order.getY()
							: String.valueOf(order.getX().doubleValue()),
							"buyText" + i, null,
							this.orderAndOrderBookProperties);
			ae.setRectangle(rect);
			ae.setProp(getRectProperties(order));
			ae.setText(text);
			i++;
		}

		return startY += ((orders.size() / 10) + 1) * 25 + 25;
	}

	private void introduceAlgorithm() {
		lang.nextStep("Introduction");

		int startX = 50;
		int startY = 175;

		// adapt position depending on set property
		if ((Boolean) this.introductionProperties.get("centered"))
			startX += 210;

		Text introductionHeaderText = lang.newText(new Coordinates(startX,
				startY), "Introduction", "introductionHeaderText", null,
				this.introductionProperties);

		Text introductionText1a = lang
				.newText(
						new Coordinates(startX, startY + 35),
						"The Continuous Double Auction (CDA) is a mechanism to match buyers and sellers of a",
						"introductionText", null, this.introductionProperties);
		Text introductionText1b = lang
				.newText(
						new Coordinates(startX, startY + 50),
						"particular good, and to determine the prices at which trades are executed.",
						"introductionText", null, this.introductionProperties);
		lang.nextStep();

		Text introductionText2a = lang
				.newText(
						new Coordinates(startX, startY + 70),
						"Traders can place limit orders in the form of bids (buy orders) and asks (sell orders).",
						"introductionText", null, this.introductionProperties);
		Text introductionText2b = lang.newText(new Coordinates(startX,
				startY + 85),
				"Outstanding orders are maintained in an order book.",
				"introductionText", null, this.introductionProperties);
		lang.nextStep();

		Text introductionText3a = lang
				.newText(
						new Coordinates(startX, startY + 105),
						"Traders may place a market order to buy or sell immediately at the market price, which is",
						"introductionText", null, this.introductionProperties);
		Text introductionText3b = lang.newText(new Coordinates(startX,
				startY + 120),
				"determined by the set of orders in the order book.",
				"introductionText", null, this.introductionProperties);
		lang.nextStep();

		Text introductionText4a = lang
				.newText(
						new Coordinates(startX, startY + 140),
						"Trades are executed whenever a new limit order comes in and the highest bid exceeds or is",
						"introductionText", null, this.introductionProperties);

		Text introductionText4b = lang
				.newText(
						new Coordinates(startX, startY + 155),
						"equal to the highest ask price, or else when a new market order comes in and the",
						"introductionText", null, this.introductionProperties);

		Text introductionText4c = lang
				.newText(
						new Coordinates(startX, startY + 170),
						"order book contains orders with which the market order can be matched.",
						"introductionText", null, this.introductionProperties);

		lang.nextStep();

		Text introductionText5a = lang.newText(new Coordinates(startX,
				startY + 195), "The following notation will be used:",
				"introductionText", null, this.introductionProperties);
		Text introductionText5b = lang.newText(new Coordinates(startX,
				startY + 215),
				"    >0B - limited buy order with price >0 (BL)",
				"introductionText", null, this.introductionProperties);
		Text introductionText5c = lang.newText(new Coordinates(startX,
				startY + 230),
				"    <0S - limited sell order with price <0 (SL)",
				"introductionText", null, this.introductionProperties);
		Text introductionText5d = lang.newText(new Coordinates(startX,
				startY + 245), "      0B - marked order buy (BM)",
				"introductionText", null, this.introductionProperties);

		Text introductionText5e = lang.newText(new Coordinates(startX,
				startY + 260), "      0S - marked order sell (SM)",
				"introductionText", null, this.introductionProperties);

		// adapt position depending on set property
		if ((Boolean) this.introductionProperties.get("centered"))
			startX -= 210;
		Rect innerborder = lang.newRect(new Coordinates(startX - 5, startY),
				new Coordinates(startX + 510, startY + 285),
				"introductionBorderRectangle", new TicksTiming(50));
		Rect outerborder = lang.newRect(
				new Coordinates(startX - 7, startY - 2), new Coordinates(
						startX + 512, startY + 287),
				"introductionBorderRectangle", new TicksTiming(50));
		lang.nextStep();

		// move introduction
		startX = 680;
		startY = 15;

		introductionHeaderText.moveTo(null, "translate", new Coordinates(
				startX, startY), Timing.INSTANTEOUS, new TicksTiming(100));
		introductionText1a.moveTo(null, "translate", new Coordinates(startX,
				startY + 35), Timing.INSTANTEOUS, new TicksTiming(100));
		introductionText1b.moveTo(null, "translate", new Coordinates(startX,
				startY + 50), Timing.INSTANTEOUS, new TicksTiming(100));
		introductionText2a.moveTo(null, "translate", new Coordinates(startX,
				startY + 70), Timing.INSTANTEOUS, new TicksTiming(100));
		introductionText2b.moveTo(null, "translate", new Coordinates(startX,
				startY + 85), Timing.INSTANTEOUS, new TicksTiming(100));
		introductionText3a.moveTo(null, "translate", new Coordinates(startX,
				startY + 105), Timing.INSTANTEOUS, new TicksTiming(100));
		introductionText3b.moveTo(null, "translate", new Coordinates(startX,
				startY + 120), Timing.INSTANTEOUS, new TicksTiming(100));
		introductionText4a.moveTo(null, "translate", new Coordinates(startX,
				startY + 140), Timing.INSTANTEOUS, new TicksTiming(100));
		introductionText4b.moveTo(null, "translate", new Coordinates(startX,
				startY + 155), Timing.INSTANTEOUS, new TicksTiming(100));
		introductionText4c.moveTo(null, "translate", new Coordinates(startX,
				startY + 170), Timing.INSTANTEOUS, new TicksTiming(100));
		introductionText5a.moveTo(null, "translate", new Coordinates(startX,
				startY + 195), Timing.INSTANTEOUS, new TicksTiming(100));
		introductionText5b.moveTo(null, "translate", new Coordinates(startX,
				startY + 215), Timing.INSTANTEOUS, new TicksTiming(100));
		introductionText5c.moveTo(null, "translate", new Coordinates(startX,
				startY + 230), Timing.INSTANTEOUS, new TicksTiming(100));
		introductionText5d.moveTo(null, "translate", new Coordinates(startX,
				startY + 245), Timing.INSTANTEOUS, new TicksTiming(100));
		introductionText5e.moveTo(null, "translate", new Coordinates(startX,
				startY + 260), Timing.INSTANTEOUS, new TicksTiming(100));

		innerborder.moveTo(null, "translate", new Coordinates(startX - 5,
				startY), Timing.INSTANTEOUS, new TicksTiming(100));
		outerborder.moveTo(null, "translate", new Coordinates(startX - 7,
				startY - 2), Timing.INSTANTEOUS, new TicksTiming(100));
	}

	private void showTrade() {
		if ((Boolean) codeTrade.getProperties().get("hidden")) {
			descriptionText.moveBy("translate", 0, 190, Timing.INSTANTEOUS,
					new TicksTiming(50));
			codeTrade.show(new TicksTiming(50));
			codeTrade.getProperties().set("hidden", false);
			lang.nextStep();
		}
	}

	private void createResultText(int yResultOffset, int startX, int startY,
			Triple<Double, Double, String> buy,
			Triple<Double, Double, String> sell, Double price) {
		int i = resultTexts.size();
		if (sell != null) {
			// Transaction
			description("Do transaction where a tripple <buy, sell, price> is produced. A sell or buy is presented as (arrival time, price, order type).");
			resultTexts.add(lang.newText(new Coordinates(startX + 160, startY
					+ 30 + (i % 10) * 30 + yResultOffset), "<" + buy + ", "
					+ sell + ", " + price + ">", "resultText", new TicksTiming(
					50), this.resultProps));
		} else if (price != null) {
			// Market order
			description("Do transaction where a pair <order, price> is produced. The order is presented as (arrival time, price, order type).");
			resultTexts.add(lang.newText(new Coordinates(startX + 160, startY
					+ 30 + (i % 10) * 30 + yResultOffset), "<" + buy + ", "
					+ price + ">", "resultText", new TicksTiming(50),
					this.resultProps));
		} else {
			// No Transaction
			description("No transaction possible for given order. The order is presented as (arrival time, price, order type).");
			resultTexts.add(lang.newText(new Coordinates(startX + 160, startY
					+ 30 + (i % 10) * 30 + yResultOffset), "<" + buy
					+ " not possible>", "resultText", new TicksTiming(50),
					this.resultProps));
		}
	}

	private void createResultText(int yResultOffset, int startX, int startY,
			Triple<Double, Double, String> order, double price) {
		createResultText(yResultOffset, startX, startY, order, null, price);
	}

	private void createResultText(int yResultOffset, int startX, int startY,
			Triple<Double, Double, String> order) {
		createResultText(yResultOffset, startX, startY, order, null, null);
	}

	private void trade(int startX, int startY, int yResultOffset) {
		lang.nextStep();
		codeTrade.highlight(0);
		lang.nextStep();
		codeTrade.unhighlight(0);
		codeTrade.highlight(1);
		description("Check if the order book contains at least one bid and one ask.");
		lang.nextStep();
		if (buyElements.size() > 0 && askElements.size() > 0) {
			codeTrade.unhighlight(1);
			codeTrade.highlight(2);
			description("Check whether the highest bid price is greater than the highest ask price.");
			lang.nextStep();
			codeTrade.unhighlight(2);
			if (!(buyElements.get(0).getOrder().getX() >= askElements
					.get(askElements.size() - 1).getOrder().getX())) {
				codeTrade.highlight(8);
				lang.nextStep();
			}
		} else {
			codeTrade.unhighlight(1);
			codeTrade.highlight(8);
			lang.nextStep();
			codeTrade.unhighlight(8);
		}
		while (askElements.size() > 0
				&& buyElements.size() > 0
				&& buyElements.get(0).getOrder().getX() >= askElements
						.get(askElements.size() - 1).getOrder().getX()) {
			codeTrade.highlight(3);
			codeTrade.highlight(4);
			description("Transaction price is the price of the first"
					+ " bid or ask order depending on whether the bid or ask order were placed first. The earliest price is used.");
			double price = askElements.get(0).getOrder().getT() < buyElements
					.get(0).getOrder().getT() ? askElements.get(0).getOrder()
					.getX() : buyElements.get(0).getOrder().getX();

			// Highlight matching orders with a rectangle
			Rect borderRect = lang.newRect(new Coordinates(buyPositions.get(0)
					.getX() - 3, buyPositions.get(0).getY() - 3),
					new Coordinates(askPositions.get(0).getX() + 43,
							askPositions.get(0).getY() + 43), "borderRect",
					Timing.INSTANTEOUS, borderProps);
			lang.nextStep();

			codeTrade.unhighlight(3);
			codeTrade.unhighlight(4);
			codeTrade.highlight(5);
			createResultText(yResultOffset, startX, startY, buyElements.get(0)
					.getOrder(), askElements.get(0).getOrder(), price);

			lang.nextStep("Remove first bid and aks from order book.");
			codeTrade.unhighlight(5);

			borderRect.hide();

			codeTrade.highlight(6);
			codeTrade.highlight(7);
			buyElements.remove(0).hideElement();
			askElements.remove(0).hideElement();
			buyPositions.remove(buyPositions.size() - 1);
			askPositions.remove(askPositions.size() - 1);
			description("Remove first bid and ask from order book.");
			lang.nextStep(100);
			// sort list of bids descending
			this.sortListOfElements(buyElements, buyPositions, this.descComp);
			// sort list of asks ascending
			this.sortListOfElements(askElements, askPositions, this.ascComp);

			lang.nextStep();
			codeTrade.unhighlight(6);
			codeTrade.unhighlight(7);
			codeTrade.highlight(8);
			lang.nextStep();
		}
		codeTrade.unhighlight(8);
		codeTrade.highlight(9);
		lang.nextStep();
		codeTrade.unhighlight(9);
		lang.nextStep();
	}

	private void description(String description) {
		descriptionText.setText(description, Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);
	}

	private void continousDoubleAuction(int startY) {
		// show source code
		SourceCode animalSourceCode = initSourceCode(startY);

		// add description text
		int xDescriptionText = 20;
		if ((Boolean) this.infoTextProps.get("centered"))
			xDescriptionText += 400;
		this.descriptionText = lang.newText(new Coordinates(xDescriptionText,
				startY + 350), "Continously recieve orders.",
				"descriptionText", null, this.infoTextProps);

		// store elements and positions in a list
		Rect elementRect;
		Text elementText;
		buyElements = new ArrayList<AuctionElement>();
		askElements = new ArrayList<AuctionElement>();
		buyPositions = new ArrayList<Coordinates>();
		askPositions = new ArrayList<Coordinates>();
		resultTexts = new ArrayList<Text>();

		// add orders
		int startX = 400;
		startY += 20;
		int xAddition;
		int yAddition;
		int yResultOffset = 100;

		lang.newText(new Coordinates(startX + 12, startY), "Order book", "",
				null, this.orderAndOrderBookProperties);
		startY += 15;
		lang.newText(new Coordinates(startX + 10, startY), "Bid", "", null,
				this.orderAndOrderBookProperties);
		lang.newText(new Coordinates(startX + 60, startY), "Ask", "", null,
				this.orderAndOrderBookProperties);
		startY += 30;

		lang.newText(new Coordinates(startX + 110, startY + yResultOffset),
				"Results:", "resultText", null, this.resultProps);
		lang.nextStep("Continuous Double Auction Algorithm");

		Rect selectionRect = null;

		for (AuctionElement ae : orders) {
			Triple<Double, Double, String> order = ae.getOrder();
			animalSourceCode.highlight("ORDER");
			// Highlight current incoming order
			if (selectionRect == null) {
				selectionRect = lang.newRect(new Coordinates(ae.getUpperLeft()
						.getX() - 3, ae.getUpperLeft().getY() - 3),
						new Coordinates(ae.getLowerRight().getX() + 3, ae
								.getLowerRight().getY() + 3), "borderRect",
						Timing.INSTANTEOUS, borderProps);
			} else {
				selectionRect.moveTo("E", "translate", new Coordinates(ae
						.getUpperLeft().getX() - 3,
						ae.getUpperLeft().getY() - 3), Timing.INSTANTEOUS,
						new TicksTiming(50));
			}
			lang.nextStep();
			animalSourceCode.unhighlight("ORDER");
			switch (order.getY()) {
			case "BL":
				animalSourceCode.highlight("BL");
				description("Limited buy order arrived (BL).");
				lang.nextStep("New limited buy order");
				animalSourceCode.unhighlight("BL");
				animalSourceCode.highlight("BL1");
				description("Add new limited buy oder (bid) to order book using a decending sorted quene.");
				xAddition = 0;
				yAddition = buyElements.size() * 45;
				elementRect = lang.newRect(new Coordinates(startX + xAddition,
						startY + yAddition), new Coordinates(startX + 40
						+ xAddition, startY + 40 + yAddition), "sellRect"
						+ buyElements.size(), null, getRectProperties(order));
				elementText = lang.newText(new Coordinates(startX + 5
						+ xAddition, startY + 15 + yAddition),
						String.valueOf(order.getX().doubleValue()), "sellText"
								+ buyElements.size(), null,
						this.orderAndOrderBookProperties);
				buyElements.add(new AuctionElement(elementRect, elementText,
						this.getRectProperties(order), true, order));
				buyPositions.add((Coordinates) elementRect.getUpperLeft());
				lang.nextStep();
				animalSourceCode.unhighlight("BL1");
				animalSourceCode.highlight("BL2");
				showTrade();

				// sort list of bids descending
				this.sortListOfElements(buyElements, buyPositions, descComp);

				trade(startX, startY, yResultOffset);
				animalSourceCode.unhighlight("BL2");
				break;
			case "SL":
				animalSourceCode.highlight("SL");
				description("Limited sell order arrived (SL).");
				lang.nextStep("New limited buy sell");
				animalSourceCode.unhighlight("SL");
				animalSourceCode.highlight("SL1");
				description("Add new limited sell oder (ask) to order book using an acending sorted quene.");
				startX = startX + 50;
				xAddition = 0;
				yAddition = askElements.size() * 45;
				elementRect = lang.newRect(new Coordinates(startX + xAddition,
						startY + yAddition), new Coordinates(startX + 40
						+ xAddition, startY + 40 + yAddition), "sellRect"
						+ askElements.size(), null, getRectProperties(order));
				elementText = lang.newText(new Coordinates(startX + 5
						+ xAddition, startY + 15 + yAddition),
						String.valueOf(order.getX().doubleValue()), "sellText"
								+ askElements.size(), null,
						this.orderAndOrderBookProperties);
				askElements.add(new AuctionElement(elementRect, elementText,
						this.getRectProperties(order), true, order));
				askPositions.add((Coordinates) elementRect.getUpperLeft());
				lang.nextStep();
				animalSourceCode.unhighlight("SL1");
				animalSourceCode.highlight("SL2");
				showTrade();

				// sort list of asks ascending
				this.sortListOfElements(askElements, askPositions, this.ascComp);
				startX = startX - 50;

				trade(startX, startY, yResultOffset);
				animalSourceCode.unhighlight("SL2");
				break;
			case "BM":
				animalSourceCode.highlight("BM");
				description("New buy marked order arrived (BM).");
				lang.nextStep("New buy marked order");
				animalSourceCode.unhighlight("BM");
				animalSourceCode.highlight("BM1");
				lang.nextStep();
				animalSourceCode.unhighlight("BM1");
				if (askElements.size() > 0) {
					animalSourceCode.highlight("BM2");
					description("Transaction price is the price of the first ask.");
					// Highlight first sell with a rectangle
					Rect borderRect = lang.newRect(
							new Coordinates(askPositions.get(0).getX() - 3,
									askPositions.get(0).getY() - 3),
							new Coordinates(askPositions.get(0).getX() + 43,
									askPositions.get(0).getY() + 43),
							"borderRect", Timing.INSTANTEOUS, borderProps);
					lang.nextStep();

					animalSourceCode.unhighlight("BM2");
					animalSourceCode.highlight("BM3");
					createResultText(yResultOffset, startX, startY, order,
							askElements.get(0).getOrder().getX());
					lang.nextStep();

					animalSourceCode.unhighlight("BM3");
					animalSourceCode.highlight("BM4");
					lang.nextStep();

					borderRect.hide();
					askElements.remove(0).hideElement();
					askPositions.remove(askPositions.size() - 1);
					description("Delete first ask.");
					lang.nextStep(50);
					// sort list of bids descending
					this.sortListOfElements(askElements, askPositions,
							this.ascComp);
					lang.nextStep();
					animalSourceCode.unhighlight("BM4");
				} else {
					animalSourceCode.highlight("BM5");
					createResultText(yResultOffset, startX, startY, order);
					lang.nextStep();
					animalSourceCode.unhighlight("BM5");
				}
				break;
			case "SM":
				animalSourceCode.highlight("SM");
				description("New sell marked order arrived (SM).");
				lang.nextStep("New sell marked order");
				animalSourceCode.unhighlight("SM");
				animalSourceCode.highlight("SM1");
				lang.nextStep();
				animalSourceCode.unhighlight("SM1");
				if (buyElements.size() > 0) {
					animalSourceCode.highlight("SM2");
					description("Transaction price is the price of the first bid.");
					// Highlight first bid with a rectangle
					Rect borderRect = lang.newRect(
							new Coordinates(buyPositions.get(0).getX() - 3,
									buyPositions.get(0).getY() - 3),
							new Coordinates(buyPositions.get(0).getX() + 43,
									buyPositions.get(0).getY() + 43),
							"borderRect", Timing.INSTANTEOUS, borderProps);
					lang.nextStep();

					animalSourceCode.unhighlight("SM2");
					animalSourceCode.highlight("SM3");
					createResultText(yResultOffset, startX, startY, order,
							buyElements.get(0).getOrder().getX());
					lang.nextStep();

					animalSourceCode.unhighlight("SM3");
					animalSourceCode.highlight("SM4");
					borderRect.hide();
					buyElements.remove(0).hideElement();
					buyPositions.remove(buyPositions.size() - 1);
					description("Delete first bid.");
					lang.nextStep(50);
					// sort list of bids descending
					this.sortListOfElements(buyElements, buyPositions,
							this.descComp);
					lang.nextStep();
					animalSourceCode.unhighlight("SM4");
				} else {
					animalSourceCode.highlight("SM5");
					createResultText(yResultOffset, startX, startY, order);
					lang.nextStep();
					animalSourceCode.unhighlight("SM5");
				}
				break;
			default:
				throw new RuntimeException("...");
			}
		}
		animalSourceCode.highlight("ORDER");
		description("No more orders to process therefore finish the algorithm. "
				+ "Normally the algorithm would continue to run.");
		lang.nextStep();
		animalSourceCode.unhighlight("ORDER");
		animalSourceCode.highlight("END");
		lang.nextStep();

		if (!(Boolean) this.outroTextProps.get("hidden"))
			this.showOutro();
	}

	private void showOutro() {
		lang.hideAllPrimitives();

		if (!(Boolean) this.titleProps.get("hidden"))
			this.setTitle();

		int startX = 50;
		if ((Boolean) this.outroTextProps.get("centered"))
			startX = 400;

		lang.newText(
				new Coordinates(startX, 80),
				"The algorithm presented is based on that used in the Iowa Electronic Markets.",
				"outro", null, this.outroTextProps);
		lang.newText(
				new Coordinates(startX, 115),
				"There are many deviations of CDA e.g. to consider the amount of a single order ",
				"outro", null, this.outroTextProps);
		lang.newText(
				new Coordinates(startX, 130),
				"(see http://www.sci.brooklyn.cuny.edu/~parsons/projects/mech-design/publications/cda.pdf).",
				"outro", null, this.outroTextProps);

		lang.newText(new Coordinates(startX, 195), "Related algorithms:",
				"outro", null, this.outroTextProps);
		lang.newText(new Coordinates(startX, 210),
				"- Demand Auction (1 seller and m potential buyers)", "outro",
				null, this.outroTextProps);
		lang.newText(
				new Coordinates(startX, 225),
				"- Double Auction (bids and sells are collected first, followed buy a matching phase)",
				"outro", null, this.outroTextProps);
		lang.newText(
				new Coordinates(startX, 240),
				"- Periodic Double Auction (fixed time intervals or predefined dates for clearing)",
				"outro", null, this.outroTextProps);

		lang.nextStep("Outro");
	}

	private void sortListOfElements(List<AuctionElement> elements,
			List<Coordinates> positions,
			Comparator<? super AuctionElement> comperator) {
		elements.sort(comperator);

		for (int i = 0; i < elements.size(); i++) {
			elements.get(i).setPosition(positions.get(i));
		}
	}

	private void getArrayData(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		asks = (RectProperties) props
				.getPropertiesByName("Ask Rectangle Properties");
		bids = (RectProperties) props
				.getPropertiesByName("Bid Rectangle Properties");
		marketOrderBuy = (RectProperties) props
				.getPropertiesByName("Market Order Buy Rectangle Properties");
		marketOrderSell = (RectProperties) props
				.getPropertiesByName("Market Order Sell Rectangle Properties");
		orders = new ArrayList<AuctionElement>();

		String[] orderArray = (String[]) primitives.get("Order Array");
		for (int i = 0; i < orderArray.length; i++) {
			String priceString = orderArray[i];
			String type = null;
			if (orderArray[i].endsWith("B")) {
				priceString = priceString.replace("B", "");
				type = "BM";
			} else if (orderArray[i].endsWith("S")) {
				priceString = priceString.replace("S", "");
				type = "SM";
			}
			double price = Double.parseDouble(priceString);
			if (price > 0) {
				type = "BL";
			} else if (price < 0) {
				type = "SL";
			}
			price = Math.abs(price);
			Triple<Double, Double, String> order = new Triple<Double, Double, String>(
					i + 1.0, price, type);
			AuctionElement ae = new AuctionElement(order);
			orders.add(ae);
		}
	}

	private void getTextProperties(AnimationPropertiesContainer props) {
		this.titleProps = (TextProperties) props
				.getPropertiesByName("Title Properties");
		this.titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.BOLD, 24));
		this.codeProps = (SourceCodeProperties) props
				.getPropertiesByName("Code Properties");
		this.infoTextProps = (TextProperties) props
				.getPropertiesByName("Info Text Properties");
		this.resultProps = (TextProperties) props
				.getPropertiesByName("Result Properties");
		this.introductionProperties = (TextProperties) props
				.getPropertiesByName("Introduction Properties");
		this.outroTextProps = (TextProperties) props
				.getPropertiesByName("Outro Properties");
		this.orderAndOrderBookProperties = (TextProperties) props
				.getPropertiesByName("Order and Order Book Text Properties");
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		this.getArrayData(props, primitives);

		// rectangle for selection
		borderProps = new RectProperties();
		borderProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		borderProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);

		this.getTextProperties(props);

		if (!(Boolean) this.titleProps.get("hidden"))
			this.setTitle();

		if (!(Boolean) this.introductionProperties.get("hidden"))
			this.introduceAlgorithm();

		int startY = this.createDoubleArray();
		this.continousDoubleAuction(startY);
		return lang.toString();
	}

	private void setTitle() {
		int xCoordinate = 10;
		if ((Boolean) this.titleProps.get("centered"))
			xCoordinate += 300;

		lang.newText(new Coordinates(xCoordinate, 15), getName(), "AlgoTitle",
				null, this.titleProps);
	}

	private SourceCode initSourceCode(int startY) {
		// now, create the source code entity
		SourceCode code = lang.newSourceCode(new Coordinates(10, startY),
				"sourceCode", null, this.codeProps);

		// Add the lines to the SourceCode object.
		// Line, name, indentation, display dealy

		code.addCodeLine(
				"for (Triple<Double, Double, String> order : orders) {",
				"ORDER", 0, null);
		code.addCodeLine("if (order.getY().equals(\\\"BL\\\")) {", "BL", 1,
				null);
		code.addCodeLine("asks.add(order);", "BL1", 2, null);
		code.addCodeLine("trade();", "BL2", 2, null);
		code.addCodeLine("} else if (order.getY().equals(\\\"SL\\\")) {", "SL",
				1, null);
		code.addCodeLine("bids.add(order);", "SL1", 2, null);
		code.addCodeLine("trade();", "SL2", 2, null);
		code.addCodeLine("} else if (order.getY().equals(\\\"BM\\\")) {", "BM",
				1, null);

		code.addCodeLine("if (asks.size() > 0) { ", "BM1", 2, null);
		code.addCodeLine("price = asks.first().getX();", "BM2", 3, null);
		code.addCodeLine("printResult(order, asks.first(), price);", "BM3", 3,
				null);
		code.addCodeLine("asks.remove(asks.first());", "BM4", 3, null);
		code.addCodeLine("} else { ... }", "BM5", 2, null);

		code.addCodeLine("} else if (order.getY().equals(\\\"SM\\\"))) {",
				"SM", 1, null);
		code.addCodeLine("if (bids.size() > 0) {", "SM1", 2, null);
		code.addCodeLine("price = bids.first().getX();", "SM2", 3, null);
		code.addCodeLine("printResult(bids.first(), order, price);", "SM3", 3,
				null);
		code.addCodeLine("bids.remove(bids.first());", "SM4", 3, null);

		code.addCodeLine("} else { ... }", "SM5", 2, null);

		code.addCodeLine("}", null, 1, null);
		code.addCodeLine("}", "END", 0, null);
		code.addCodeLine("", null, 0, null);

		codeTrade = lang.newSourceCode(new Coordinates(10, startY + 350),
				"sourceCode", null, this.codeProps);
		codeTrade.addCodeLine("public void trade() {", null, 0, null);
		codeTrade.addCodeLine("while (bids.size() > 0 && asks.size() > 0 &&",
				null, 1, null);
		codeTrade.addCodeLine("bids.first().getX() >= asks.last().getX()) {",
				null, 2, null);
		codeTrade.addCodeLine(
				"price = bids.first().getT() < asks.first().getT() ?", null, 2,
				null);
		codeTrade.addCodeLine("bids.first().getX() : asks.first().getX();",
				null, 3, null);
		codeTrade.addCodeLine(
				"printResult(bids.first(), asks.first(), price);", null, 2,
				null);
		codeTrade.addCodeLine("bids.remove(bids.first());", null, 2, null);
		codeTrade.addCodeLine("asks.remove(asks.first());", null, 2, null);
		codeTrade.addCodeLine("}", null, 1, null);
		codeTrade.addCodeLine("}", null, 0, null);
		codeTrade.getProperties().set("hidden", true);
		codeTrade.hide();
		return code;
	}

	public String getName() {
		return "Continous Double Auction";
	}

	public String getAlgorithmName() {
		return "Continouss Double Auction";
	}

	public String getAnimationAuthor() {
		return "Marco Ballhausen, Deborah Buth";
	}

	public String getDescription() {
		return "The Continuous Double Auction (CDA) is a mechanism to match buyers and sellers of a particular good, and to determine the prices at which trades are executed."
				+ "Traders can place limit orders in the form of bids (buy orders) and asks (sell orders). Outstanding orders are maintained in an order book."
				+ "Traders may place a market order to buy or sell immediately at the market price, which is determined by the set of orders in the order book."
				+ "Trades are executed whenever a new limit order comes in and the highest bid exceeds"
				+ " or is equal to the highest ask price, or else when a new market order comes in and the order book contains orders with which the market order can be matched."
				+ "<br/><br /> Further information can be found here: "
				+ "<a href=\\\"http://isites.harvard.edu/fs/docs/icb.topic781759.files/11.2%20CDA.pdf\\\">"
				+ "http://isites.harvard.edu/fs/docs/icb.topic781759.files/11.2%20CDA.pdf</a>";
	}

	private final String LF = "\n";

	public String getCodeExample() {
		return "// Continously process Orders with attributes (time, amount, type)"
				+ LF
				+ "for (Triple&lt;Double, Double, Double&gt; order : orders) {"
				+ LF
				+ "  // buy limited order"
				+ LF
				+ "  if (order.getY().equals(\\\"BL\\\")) {"
				+ LF
				+ "    // asks are sorted ascending"
				+ LF
				+ "    asks.add(order);"
				+ LF
				+ "    trade();"
				+ LF
				+ "  // sell limited order"
				+ LF
				+ "  } else if (order.getY().equals(\\\"SL\\\")) {"
				+ LF
				+ "    // bids are sorted descending"
				+ LF
				+ "    bids.add(order);"
				+ LF
				+ "    trade();"
				+ LF
				+ "  // buy marked order"
				+ LF
				+ "  } else if (order.getY().equals(\\\"BM\\\")) {"
				+ LF
				+ "    if (asks.size() > 0) {"
				+ LF
				+ "      price = asks.first.getX();"
				+ LF
				+ "      printResult(order, asks.first(), price);"
				+ LF
				+ "      aks.remove(asks.first());"
				+ LF
				+ "    } else { ... }"
				+ LF
				+ "  // sell market order"
				+ LF
				+ "  } else if (order.getY().equals(\\\"SM\\\")) {"
				+ LF
				+ "    if (bids.size() > 0) {"
				+ LF
				+ "      price = bids.first().getX();"
				+ LF
				+ "      printResult(bids.first(), order, price);"
				+ LF
				+ "      bids.remove(bids.first());"
				+ LF
				+ "    } else { ... }" + LF + "  }" + LF + "}";
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
		return Generator.JAVA_OUTPUT;
	}

	private int showConfirmDialog(String title, String message) {
		return JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(),
				message, title, JOptionPane.YES_NO_OPTION);
	}

	private String validateAutoCorrection(String s) {
		String correction = s.substring(0, s.length() - 1);
		int userChoice = showConfirmDialog("Auto correction",
				"We detected a potential typo in your input. You typed '" + s
						+ "' and it was automatically modified to '"
						+ correction
						+ "'. Do you want to continue with this value?");
		if (userChoice == 1) {
			throw new IllegalArgumentException("Unrecognized number '" + s
					+ "'");
		}
		return correction;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		try {
			String[] orderArray = (String[]) primitives.get("Order Array");
			for (int i = 0; i < orderArray.length; i++) {
				String s = orderArray[i];
				char lastChar = s.charAt(s.length() - 1);
				if (lastChar == 'B' || lastChar == 'S') {
					double val = Double.parseDouble(s.substring(0,
							s.length() - 1));
					if (val != 0) {
						return false;
					}
				} else if (lastChar == 'D' || lastChar == 'F'
						|| lastChar == 'L') {
					orderArray[i] = validateAutoCorrection(s);
					primitives.put("Order Array", orderArray);
				} else {
					try {
						Double.parseDouble(s);
					} catch (NumberFormatException e) {
						Double.parseDouble(s.substring(0, s.length() - 1));
						orderArray[i] = validateAutoCorrection(s);
						primitives.put("Order Array", orderArray);
					}
				}
			}
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}