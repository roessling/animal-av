package generators.misc.doubleauction;

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

public class DoubleAuction implements ValidatingGenerator {
	private Language lang;
	private RectProperties sell;
	private RectProperties buy;
	private Text descriptionText;
	private TextProperties titleProps;
	private TextProperties infoTextProps;
	private TextProperties outroTextProps;
	private TextProperties resultProps;
	private TextProperties introductionProperties;
	private SourceCodeProperties codeProps;
	private double[] sellBids;
	private double[] buyBids;

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
		lang.nextStep("Buys and Sells");
		
		lang.newText(new Coordinates(20, 42), "Buy bids:", "", null);
		lang.newRect(new Coordinates(75, 42), new Coordinates(95, 60),
				"buyTitle", null, this.buy);
		lang.newText(new Coordinates(20, 62), "Sell bids:", "", null);
		lang.newRect(new Coordinates(75, 62), new Coordinates(95, 80),
				"sellTitle", null, this.sell);

		int startX = 120;
		int startY = 42;

		for (int i = 0; i < buyBids.length; i++) {
			int xAddition = (i % 10) * 45;
			int yAddition = (i / 10) * 25;
			lang.newRect(
					new Coordinates(startX + xAddition, startY + yAddition),
					new Coordinates(startX + 40 + xAddition, startY + 20
							+ yAddition), "buyRect" + i, null, this.buy);
			lang.newText(new Coordinates(startX + 5 + xAddition, startY + 5
					+ yAddition), String.valueOf(buyBids[i]), "buyText" + i,
					null);
		}

		startY += ((buyBids.length / 10) + 1) * 25 + 10;
		for (int i = 0; i < sellBids.length; i++) {
			int xAddition = (i % 10) * 45;
			int yAddition = (i / 10) * 25;
			lang.newRect(
					new Coordinates(startX + xAddition, startY + yAddition),
					new Coordinates(startX + 40 + xAddition, startY + 20
							+ yAddition), "sellRect" + i, null, this.sell);
			lang.newText(new Coordinates(startX + 5 + xAddition, startY + 5
					+ yAddition), String.valueOf(sellBids[i]), "sellText" + i,
					null);
		}
		
		return startY += ((buyBids.length / 10) + 1) * 25 + 70;
	}

	private void highlightNextLine(SourceCode code, int currentLine, String newDescriptionText) {
		code.unhighlight(currentLine);
		code.highlight(currentLine + 1);
		
		if(newDescriptionText != null)
			this.descriptionText.setText(newDescriptionText, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
	}

	private void introduceAlgorithm() {
		lang.nextStep("Introduction");
		
		int startX = 50;
		int startY = 175;
		
		// adapt position depending on set property
		if((Boolean)this.introductionProperties.get("centered"))
			startX += 210;
			
		Text introductionHeaderText = lang.newText(new Coordinates(startX, startY), "Introduction", "introductionHeaderText", null, this.introductionProperties);		
		lang.nextStep();
		
		Text introductionText1a = lang.newText(new Coordinates(startX, startY + 35), "This algorithm represents the process of buying and", "introductionText", null, this.introductionProperties);
		Text introductionText1b = lang.newText(new Coordinates(startX, startY + 50), "selling goods in an auctioning market.", "introductionText", null, this.introductionProperties);
		lang.nextStep();
		
		Text introductionText2a = lang.newText(new Coordinates(startX, startY + 70), "Therefore potential buyers submit their bids and", "introductionText", null, this.introductionProperties);
		Text introductionText2b = lang.newText(new Coordinates(startX, startY + 85), "potential sellers submit their ask prices to the auctioneer.", "introductionText", null, this.introductionProperties);
		lang.nextStep();
		
		Text introductionText3 = lang.newText(new Coordinates(startX, startY + 110), "Then the auctioneer chooses the clearing price CP.", "introductionText", null, this.introductionProperties);
		lang.nextStep();
		
		Text introductionText4a = lang.newText(new Coordinates(startX, startY + 135), "The clearing price clears the market - so all sellers,", "introductionText", null, this.introductionProperties);
		Text introductionText4b = lang.newText(new Coordinates(startX, startY + 150), "who ask less than CP, sell and all buyers, who bid more", "introductionText", null, this.introductionProperties);
		Text introductionText4c = lang.newText(new Coordinates(startX, startY + 165), "than CP, buy, at this price CP.", "introductionText", null, this.introductionProperties);

		// adapt position depending on set property
		if((Boolean)this.introductionProperties.get("centered"))
			startX -= 210;	
		Rect innerborder = lang.newRect(new Coordinates(startX - 5, startY), new Coordinates(startX + 420, startY + 185), "introductionBorderRectangle", new TicksTiming(50));
		Rect outerborder = lang.newRect(new Coordinates(startX - 7, startY - 2), new Coordinates(startX + 422, startY + 187), "introductionBorderRectangle", new TicksTiming(50));
		lang.nextStep();
		
		// move introduction
		startX = 600;
		startY = 15;
		
		introductionHeaderText.moveTo(null, "translate", new Coordinates(startX, startY), Timing.INSTANTEOUS, new TicksTiming(100));
		introductionText1a.moveTo(null, "translate", new Coordinates(startX, startY + 35), Timing.INSTANTEOUS, new TicksTiming(100));
		introductionText1b.moveTo(null, "translate", new Coordinates(startX, startY + 50), Timing.INSTANTEOUS, new TicksTiming(100));
		introductionText2a.moveTo(null, "translate", new Coordinates(startX, startY + 70), Timing.INSTANTEOUS, new TicksTiming(100));
		introductionText2b.moveTo(null, "translate", new Coordinates(startX, startY + 85), Timing.INSTANTEOUS, new TicksTiming(100));
		introductionText3.moveTo(null, "translate", new Coordinates(startX, startY + 110), Timing.INSTANTEOUS, new TicksTiming(100));
		introductionText4a.moveTo(null, "translate", new Coordinates(startX, startY + 135), Timing.INSTANTEOUS, new TicksTiming(100));
		introductionText4b.moveTo(null, "translate", new Coordinates(startX, startY + 150), Timing.INSTANTEOUS, new TicksTiming(100));
		introductionText4c.moveTo(null, "translate", new Coordinates(startX,startY + 165), Timing.INSTANTEOUS, new TicksTiming(100));
		innerborder.moveTo(null, "translate", new Coordinates(startX - 5, startY), Timing.INSTANTEOUS, new TicksTiming(100));
		outerborder.moveTo(null, "translate", new Coordinates(startX - 7, startY - 2), Timing.INSTANTEOUS, new TicksTiming(100));
	}

	private void doubleAuction(int startY) {
		lang.nextStep();
				
		// show source code
		SourceCode animalSourceCode = initSourceCode(startY);
		animalSourceCode.highlight(0);
		
		// add description text
		int xDescriptionText = 20;
		if((Boolean)this.infoTextProps.get("centered"))
			xDescriptionText += 400;
		this.descriptionText = lang.newText(new Coordinates(xDescriptionText, startY + 480),
				"Create an empty list to store the buys and sells.",
				"descriptionText", null, this.infoTextProps);
		lang.nextStep("Double Auction Algorithm");
				
		highlightNextLine(animalSourceCode, 0, "Now add all sells to the created list.");
		
		// store elements and positions in a list
		Rect elementRect;
		Text elementText;
		List<AuctionElement> auctionElements = new ArrayList<AuctionElement>();
		List<Coordinates> positions = new ArrayList<Coordinates>();

		// add sells
		int startX = 600;
		int xAddition;
		int yAddition;

		for (int i = 0; i < sellBids.length; i++) {
			xAddition = (i / 8) * 60;
			yAddition = (i % 8) * 45;

			elementRect = lang.newRect(new Coordinates(startX + xAddition,
					startY + yAddition), new Coordinates(startX + 40
					+ xAddition, startY + 40 + yAddition), "sellRect" + i,
					new TicksTiming(50), this.sell);
			elementText = lang.newText(new Coordinates(startX + 5 + xAddition,
					startY + 15 + yAddition), String.valueOf(sellBids[i]),
					"sellText" + i, new TicksTiming(50));
			auctionElements.add(new AuctionElement(elementRect, elementText,
					this.sell, false));
			positions.add((Coordinates) elementRect.getUpperLeft());
		}
		
		lang.nextStep("Add Sells to List");

		// adapt description text
		highlightNextLine(animalSourceCode, 1, "Then add all buys to the list.");

		// add buys
		int j;
		startX += (sellBids.length / 8) * 60;
		for (int i = 0; i < buyBids.length; i++) {
			j = i + (sellBids.length % 8);
			xAddition = (j / 8) * 60;
			yAddition = (j % 8) * 45;

			elementRect = lang.newRect(new Coordinates(startX + xAddition,
					startY + yAddition), new Coordinates(startX + 40
					+ xAddition, startY + 40 + yAddition), "buyRect" + i, new TicksTiming(50),
					this.buy);
			elementText = lang.newText(new Coordinates(startX + 5 + xAddition,
					startY + 15 + yAddition), String.valueOf(buyBids[i]),
					"buyText" + i, new TicksTiming(50));
			auctionElements.add(new AuctionElement(elementRect, elementText,
					this.buy, true));
			positions.add((Coordinates) elementRect.getUpperLeft());
		}
		
		lang.nextStep("Add Buys to List");

		// sort list of sells and bids
		highlightNextLine(animalSourceCode, 2, "Now sort all elements in the list.");
		this.sortListOfElements(auctionElements, positions);
		lang.nextStep("Sort List (Descending Order)");

		// determine the clearing price
		highlightNextLine(animalSourceCode, 3, "Determine the clearing price (CP) by selecting the nth entry (n = number of sells in the list).");

		AuctionElement clearingPriceElement = auctionElements
				.get(this.sellBids.length - 1);
		double clearingPrice = Double.parseDouble(clearingPriceElement
				.getText().getText());
		clearingPriceElement.addHighlight();
		lang.newText(new Coordinates(10, startY + 335), "CP = " + clearingPrice,
				"cpText", null);
		lang.nextStep("Determine CP (Clearing Price)");

		// separate buys and sells
		List<AuctionElement> buys = new ArrayList<AuctionElement>();
		List<AuctionElement> sells = new ArrayList<AuctionElement>();
		for (AuctionElement ae : auctionElements) {
			if (ae.isBuy()) {
				buys.add(ae);
			} else {
				sells.add(ae);
			}
		}
		
		// sort buys in descending order 
		highlightNextLine(animalSourceCode, 4, "Sort buys in descending order.");

		// remove highlight of clearing price element
		clearingPriceElement.removeHighlight();
		
		buys.sort(descComp);
		for (int i = 0; i < buys.size(); i++) {
			buys.get(i).setPosition(new Coordinates(10 + i * 60, startY + 360));
		}
		
		lang.nextStep("Sort Buys Descending");

		// sort sells ascending
		highlightNextLine(animalSourceCode, 5, "Sort sells in ascending order.");
		
		// sort sells in ascending order
		sells.sort(ascComp);
		for (int i = 0; i < sells.size(); i++) {
			sells.get(i)
					.setPosition(new Coordinates(10 + i * 60, startY + 410));
		}
		
		lang.nextStep("Sort Sells Ascending");

		// create pairs until one list is empty
		lang.newText(new Coordinates(startX - 65, startY + 50), "Results:",
				"resultText", null, this.resultProps);
		highlightNextLine(animalSourceCode, 6, "Match results by creating pairs until one list is empty, the buy value is smaller than CP or the sell value is greater than CP.");
		lang.nextStep("Match Results");
		
		RectProperties borderProps = new RectProperties();
		borderProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		borderProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);

		Rect borderRect = null;
		Text iText = null;
		
		TextProperties iTextProps = new TextProperties();
		iTextProps.set("color", this.codeProps.get("highlightColor"));
		
		int xResult = startX + 5;
		if((Boolean)this.resultProps.get("centered"))
			xResult += 150;
		
		for (int i = 0; i < buys.size() && i < sells.size(); i++) {
			double buyValue = Double.parseDouble(buys.get(i).getText().getText());
			double sellValue = Double.parseDouble(sells.get(i).getText().getText());
							
			if (buyValue >= clearingPrice && sellValue <= clearingPrice) {
				if(iText == null){
					iText = lang.newText(new Coordinates(17, startY + 460),
							"i = " + i, "iText", null, iTextProps);
				}
				else
				{
					iText.moveBy("translate", 60, 0, null, new TicksTiming(20));
					iText.setText("i = " + i, new TicksTiming(20), null);
				}
				
				animalSourceCode.unhighlight(9);
				animalSourceCode.highlight(7);
				lang.nextStep();
				
				highlightNextLine(animalSourceCode, 7, null);
				lang.nextStep();
				
				if (borderRect == null) {		
					borderRect = lang.newRect(new Coordinates(6, startY + 355),
							new Coordinates(56, startY + 455), "borderRect",
							null, borderProps);

					lang.newText(new Coordinates(xResult + (i / 10) * 150,
							startY + 30 + (i % 10) * 30), "<Buy "
							+ buys.get(0).getText().getText() + ", Sell "
							+ sells.get(0).getText().getText() + ">",
							"resultText", new TicksTiming(50), this.resultProps);
				} else {					
					borderRect.moveBy("translate", 60, 0, null,
							new TicksTiming(20));
					animalSourceCode.unhighlight(9);

					lang.newText(new Coordinates(xResult + (i / 10) * 150,
							startY + 30 + (i % 10) * 30), "<Buy "
							+ buys.get(i).getText().getText() + ", Sell "
							+ sells.get(i).getText().getText() + ">",
							"resultText" + i, new TicksTiming(50),
							this.resultProps);
				}
				
				highlightNextLine(animalSourceCode, 8, null);
				lang.nextStep();
			}
			else
			{
				iText.moveBy("translate", 60, 0, null, new TicksTiming(20));
				iText.setText("i = " + i, new TicksTiming(20), null);
				
				animalSourceCode.unhighlight(9);
				animalSourceCode.highlight(7);
				lang.nextStep();
				
				highlightNextLine(animalSourceCode, 7, null);
				lang.nextStep();
				
				animalSourceCode.unhighlight(8);
				break;
			}
		}
		
		highlightNextLine(animalSourceCode, 9, "Stop matching of buys and sells.");
		lang.nextStep();
		highlightNextLine(animalSourceCode, 10, null);
		lang.nextStep();
		
		if(!(Boolean)this.outroTextProps.get("hidden"))
			this.showOutro();
	}

	private void showOutro(){
		lang.hideAllPrimitives();
		
		if(!(Boolean)this.titleProps.get("hidden"))
			this.setTitle();
		
		int startX = 50;
		if((Boolean)this.outroTextProps.get("centered"))
			startX = 400;
		
		lang.newText(new Coordinates(startX, 80), "This algorithm is mainly used in energy markets and extensions of it in finance"
				+ " and auction markets.", "outro", null, this.outroTextProps);
		lang.newText(new Coordinates(startX, 115), "Through the trading using a clearing price individual optimization with maximizing"
				+ " common welfare is achieved.", "outro", null, this.outroTextProps);
		lang.newText(new Coordinates(startX, 130), "In detail the clearing price maximizes the number of matching deals and", "outro", null, this.outroTextProps);
		lang.newText(new Coordinates(startX, 145), "minimizes costs for all market participants on the long run because the", "outro", null, this.outroTextProps);
		lang.newText(new Coordinates(startX, 165), "clearing price is not the highest possible price.", "outro", null, this.outroTextProps);
		
		lang.newText(new Coordinates(startX, 195), "Related algorithms:", "outro", null, this.outroTextProps);
		lang.newText(new Coordinates(startX, 210), "- Demand Auction (1 seller and m potential buyers)", "outro", null, this.outroTextProps);
		lang.newText(new Coordinates(startX, 225), "- Continuous Double Auction (every bid triggers clearing mechanism)", "outro", null, this.outroTextProps);
		lang.newText(new Coordinates(startX, 240), "- Periodic Double Auction (fixed time intervals or predefined dates for clearing)", "outro", null, this.outroTextProps);
		
		lang.nextStep("Outro");
	}
	
	private void sortListOfElements(List<AuctionElement> elements,
			List<Coordinates> positions) {
		elements.sort(this.descComp);

		for (int i = 0; i < elements.size(); i++) {
			elements.get(i).setPosition(positions.get(i));
		}
	}

	private void getArrayData(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		sell = (RectProperties) props
				.getPropertiesByName("Sell Rectangle Properties");
		buy = (RectProperties) props
				.getPropertiesByName("Buy Rectangle Properties");
	}

	private void getTextProperties(AnimationPropertiesContainer props) {
		this.titleProps = (TextProperties) props
				.getPropertiesByName("Title Properties");
		this.titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 24));
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
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		this.getArrayData(props, primitives);
		this.getTextProperties(props);

		if(!(Boolean)this.titleProps.get("hidden"))
			this.setTitle();
		
		if(!(Boolean)this.introductionProperties.get("hidden"))
			this.introduceAlgorithm();

		int startY = this.createDoubleArray();
		this.doubleAuction(startY);
		return lang.toString();
	}

	private void setTitle() {
		int xCoordinate = 10;
		if((Boolean)this.titleProps.get("centered"))
			xCoordinate += 300;
			
		lang.newText(new Coordinates(xCoordinate, 15), getName(),
			"AlgoTitle", null, this.titleProps);
	}

	private SourceCode initSourceCode(int startY) {
		// now, create the source code entity
		SourceCode code = lang.newSourceCode(new Coordinates(10, startY + 100),
				"sourceCode", null, this.codeProps);

		// Add the lines to the SourceCode object.
		// Line, name, indentation, display delay
		code.addCodeLine(
				"List<Double> combinedList = new LinkedList<Double>();", null,
				0, null);
		code.addCodeLine("combinedList.addAll(Arrays.asList(sellBids));", null,
				0, null);
		code.addCodeLine("combinedList.addAll(Arrays.asList(buyBids));", null,
				0, null);
		code.addCodeLine(
				"Collections.sort(combinedList, Collections.reverseOrder());",
				null, 0, null);

		code.addCodeLine("double cp = combinedList.get(sellBids.length - 1);",
				null, 0, null);
		
		code.addCodeLine("Arrays.sort(buyBids, Collections.reverseOrder());",
				null, 0, null);
		code.addCodeLine("Arrays.sort(sellBids);", null, 0, null);

		code.addCodeLine(
				"for (int i = 0; i < sellBids.length && i < buyBids.length; i++) {",
				null, 0, null);
		code.addCodeLine("if (sellBids[i] <= cp && buyBids[i] >= cp) {", null,
				1, null);
		code.addCodeLine(
				"System.out.println(\\\"<Buy \\\" + buyBids[i] + \\\", Sell \\\" + sellBids[i] + \\\">\\\");",
				null, 2, null);
		code.addCodeLine("}", null, 1, null);
		code.addCodeLine("}", null, 0, null);
		code.addCodeLine("", null, 0, null);

		return code;
	}

	public String getName() {
		return "Double Auction";
	}

	public String getAlgorithmName() {
		return "Double Auction";
	}

	public String getAnimationAuthor() {
		return "Marco Ballhausen, Deborah Buth";
	}

	public String getDescription() {
		return "In an auction market double auction is an algorithm that finds a clearing price that maximizes the number of "
				+ "matching deals at the end of each period of trading. This process is based on participants bids during such a period."
				+ " "
				+ "Bidder enter their bids and sells. Bids and sells are sorted descending to determine the Clearing Price. "
				+ "Afterwards remove all unneeded buys and sells and match the left ones.";
	}

	private final String LF = "\n";

	public String getCodeExample() {
		return "// Sort all bids of buyers and sellers"
				+ LF
				+ "List&lt;Double&gt; combinedList = new LinkedList&lt;Double&gt;();"
				+ LF
				+ "combinedList.addAll(Arrays.asList(sellBids));"
				+ LF
				+ "combinedList.addAll(Arrays.asList(buyBids));"
				+ LF
				+ "Collections.sort(combinedList, Collections.reverseOrder());"
				+ LF
				+ LF
				+ "// Determine Clearing Price (CP)"
				+ LF
				+ "double cp = combinedList.get(sellBids.length - 1);"
				+ LF
				+ LF
				+ "// Determine Matching Bids"
				+ LF
				+ "Arrays.sort(buyBids, Collections.reverseOrder());"
				+ LF
				+ "Arrays.sort(sellBids);"
				+ LF
				+ LF
				+ "for (int i = 0; i < sellBids.length && i < buyBids.length; i++) {"
				+ LF
				+ "  if (sellBids[i] <= cp && buyBids[i] >= cp) {"
				+ LF
				+ "   System.out.println(\"&lt;Buy \" + buyBids[i] + \", Sell \""
				+ LF + "        + sellBids[i] + \"&gt;\");" + LF + "  }" + LF
				+ "}";
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

	@Override
	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		try {
			String[] tempSellBids = (String[]) primitives
					.get("Sell Array");
			this.sellBids = new double[tempSellBids.length];
			for (int i = 0; i < tempSellBids.length; i++) {
				sellBids[i] = Double.parseDouble(tempSellBids[i]);
			}

			String[] tempBuyBids = (String[]) primitives
					.get("Buy Array");
			this.buyBids = new double[tempBuyBids.length];
			for (int i = 0; i < tempBuyBids.length; i++) {
				buyBids[i] = Double.parseDouble(tempBuyBids[i]);
			}

			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}