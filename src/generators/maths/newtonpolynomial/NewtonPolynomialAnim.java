package generators.maths.newtonpolynomial;

import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.HashSet;
import java.util.Random;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import animal.main.Animal;

public class NewtonPolynomialAnim {

	private NoRefreshStringMatrixGenerator sMatrixGenerator;

	private Language language;

	private Variables variables;

	private SamplingPoints stuetzstellen;
	private DividedDifferences dividierteDifferenzen;

	private int numberOfSamplingPoints;

	private Text header;
	private Text introduction;
	private Text[] descr;

	private TextProperties captionProperties;
	private TextProperties normalProperties;
	private TextProperties smallProperties;

	private SourceCode newtonInterpolationCode;
	private SourceCodeProperties newtonInterpolationCodeProperties;
	private Rect newtonInterpolationCodeBox;

	private SourceCode dividierteDifferenzenCode;
	private SourceCodeProperties dividierteDifferenzenCodeProperties;
	private Rect dividierteDifferenzenCodeBox;

	private SourceCode interpolationsPolynomCode;
	private SourceCodeProperties interpolationsPolynomCodeProperties;
	private Rect interpolationsPolynomCodeBox;

	private StringMatrix stuetzstellenMatrix;
	private StringMatrix stuetzstellenMatrixGhost;
	private MatrixProperties stuetzstellenMatrixProperties;
	private Text stuetzstellenMatrixDescr;

	private StringMatrix dividierteDifferenzenMatrix;
	private TwoValueView dividierteDifferenzenView;
	private TwoValueCounter dividierteDifferenzenCounter;
	private StringMatrix dividierteDifferenzenMatrixGhost;
	private MatrixProperties dividierteDifferenzenMatrixProperties;
	private Text dividierteDifferenzenMatrixDescr;

	private Text rechnung;
	private TextProperties rechnungProperties;
	private Text rechnungDescr;

	private StringMatrix gammaMatrix;
	private MatrixProperties gammaMatrixProperties;
	private Text gammaMatrixDescr;

	private Text newtonPolynom;
	private TextProperties newtonPolynomProperties;
	private Text newtonPolynomDescr;

	private boolean divisionByZero = false;

	NewtonPolynomialAnim(Language lang, SamplingPoints stuetzstellen,
			SourceCodeProperties newtonInterpolationCodeProperties, MatrixProperties stuetzstellenMatrixProperties,
			MatrixProperties dividierteDifferenzenMatrixProperties,
			SourceCodeProperties dividierteDifferenzenCodeProperties, TextProperties rechnungProperties,
			MatrixProperties gammaMatrixProperties, SourceCodeProperties interpolationsPolynomCodeProperties,
			TextProperties newtonPolynomProperties) {
		language = lang;
		sMatrixGenerator = new NoRefreshStringMatrixGenerator((AnimalScript) language);

		this.stuetzstellen = stuetzstellen;

		this.newtonInterpolationCodeProperties = newtonInterpolationCodeProperties;
		this.stuetzstellenMatrixProperties = stuetzstellenMatrixProperties;
		this.dividierteDifferenzenMatrixProperties = dividierteDifferenzenMatrixProperties;
		this.dividierteDifferenzenCodeProperties = dividierteDifferenzenCodeProperties;
		this.rechnungProperties = rechnungProperties;
		this.gammaMatrixProperties = gammaMatrixProperties;
		this.interpolationsPolynomCodeProperties = interpolationsPolynomCodeProperties;
		this.newtonPolynomProperties = newtonPolynomProperties;
	}

	private void createPrologue() {
		TextProperties headerProperties = new TextProperties();
		headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		header = language.newText(new Coordinates(20, 30),
				"Newtonsche Interpolationsformel mit Schema der dividierten Differenzen", "Header", null,
				headerProperties);

		captionProperties = new TextProperties();
		captionProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 22));
		introduction = language.newText(new Offset(0, 40, header, AnimalScript.DIRECTION_SW),
				"Hintergrundinformationen", "DescrHd", null, captionProperties);

		normalProperties = new TextProperties();
		normalProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));

		smallProperties = new TextProperties();
		smallProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 12));

		descr = new Text[7];
		descr[0] = language
				.newText(
						new Offset(0, 25, introduction, AnimalScript.DIRECTION_SW),
						"Bei der Eingabe von n + 1 paarweise verschiedenen Stützstellen (x_{0}, y_{0}), ..., (x_{n}, y_{n}) liefert die Newtonsche Interpolationsformel",
						"Descr[0]", null, normalProperties);
		descr[1] = language.newText(new Offset(0, 10, descr[0], AnimalScript.DIRECTION_SW),
				"ein Polynom p_{n}(x) n-ten Grades, für das p_{n}(x_{i}) = y_{i} gilt.", "Descr[1]", null,
				normalProperties);
		descr[2] = language.newText(new Offset(0, 20, descr[1], AnimalScript.DIRECTION_SW),
				"Das Polynom p_{n}(x) entspricht dabei der Newtonschen Darstellung, d.h. es ist wie folgt aufgebaut:",
				"Descr[2]", null, normalProperties);
		descr[3] = language
				.newText(
						new Offset(0, 10, descr[2], AnimalScript.DIRECTION_SW),
						"p_{n}(x) = γ_{0} + γ_{1} * (x - x_{0}) + γ_{2} * (x - x_{0}) * (x - x_{1}) + ... + γ_n * (x - x_{0}) * (x - x_{1}) * ... * (x - x_{n - 1})",
						"Descr[3]", null, normalProperties);
		descr[4] = language
				.newText(
						new Offset(0, 20, descr[3], AnimalScript.DIRECTION_SW),
						"Dabei bezeichnet man f_{x_{0}, ..., x_{i}} := γ_{i} als die i-te dividierte Differenz. Diese Differenzen lassen sich über folgende Rekursion effizient berechnen:",
						"Descr[4]", null, normalProperties);
		descr[5] = language.newText(new Offset(0, 20, descr[4], AnimalScript.DIRECTION_SW),
				"f_{x_{i}} = y_{i} für i = 0, ..., n", "Descr[5]", null, normalProperties);
		descr[6] = language
				.newText(
						new Offset(0, 10, descr[5], AnimalScript.DIRECTION_SW),
						"f_{x_{j}, ..., x_{j + i}} = (f_{x_{j + 1}, ..., x_{j + i}} - f_{x_{j}, ..., x_{j + i - 1}}) / (x_{j + i} - x_{j}) für i = 1, ..., n und j = 0, ..., n - i",
						"Descr[6]", null, normalProperties);
	}

	private void createCode() {
		// add code box
		RectProperties codeBoxProperties = new RectProperties();
		codeBoxProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		codeBoxProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(236, 236, 236));

		newtonInterpolationCodeBox = language.newRect(new Offset(0, 35, rechnung, AnimalScript.DIRECTION_SW),
				new Offset(250, 190, rechnung, AnimalScript.DIRECTION_SW), "NewtonInterpolationCodeBox", null,
				codeBoxProperties);
		dividierteDifferenzenCodeBox = language.newRect(new Offset(10, 0, newtonInterpolationCodeBox,
				AnimalScript.DIRECTION_NE), new Offset(560, 0, newtonInterpolationCodeBox, AnimalScript.DIRECTION_SE),
				"DividierteDifferenzenCodeBox", null, codeBoxProperties);
		dividierteDifferenzenCodeBox.hide();
		interpolationsPolynomCodeBox = language.newRect(new Offset(10, 0, newtonInterpolationCodeBox,
				AnimalScript.DIRECTION_NE), new Offset(390, 0, newtonInterpolationCodeBox, AnimalScript.DIRECTION_SE),
				"InterpolationsPolynomCodeBox", null, codeBoxProperties);
		interpolationsPolynomCodeBox.hide();

		// add main code group
		newtonInterpolationCode = language.newSourceCode(new Offset(10, 0, newtonInterpolationCodeBox,
				AnimalScript.DIRECTION_NW), "NewtonInterpolationCodeCode", null, newtonInterpolationCodeProperties);

		newtonInterpolationCode.addCodeLine("NewtonInterpolation(stützstellen)", null, 0, null);
		newtonInterpolationCode.addCodeLine("γ = DividierteDifferenzen(stützstellen)", null, 2, null);
		newtonInterpolationCode.addCodeLine("p_{" + String.valueOf(numberOfSamplingPoints - 1)
				+ "}(x) = Interpolationspolynom(γ)", null, 2, null);
		newtonInterpolationCode.addCodeLine("return p_{" + String.valueOf(numberOfSamplingPoints - 1) + "}(x)", null,
				2, null);

		// add dividierte Differenzen code group
		dividierteDifferenzenCode = language.newSourceCode(new Offset(10, 0, dividierteDifferenzenCodeBox,
				AnimalScript.DIRECTION_NW), "DividierteDifferenzenCode", null, dividierteDifferenzenCodeProperties);

		dividierteDifferenzenCode.addCodeLine("DividierteDifferenzen(stützstellen)", null, 0, null);
		dividierteDifferenzenCode.addCodeLine("for i = 0 to " + String.valueOf(numberOfSamplingPoints - 1), null, 2,
				null);
		dividierteDifferenzenCode.addCodeLine("f_{x_{i}} = y_{i}", null, 4, null);

		dividierteDifferenzenCode.addCodeLine("for i = 1 to " + String.valueOf(numberOfSamplingPoints - 1), null, 2,
				null);
		dividierteDifferenzenCode.addCodeLine("for j = 0 to " + String.valueOf(numberOfSamplingPoints - 1) + " - i",
				null, 4, null);
		dividierteDifferenzenCode
				.addCodeLine(
						"f_{x_{j}, ..., x_{j + i}} = (f_{x_{j + 1}, ..., x_{j + i}} - f_{x_{j}, ..., x_{j + i - 1}}) / (x_{j + i} - x_{j})",
						null, 6, null);
		dividierteDifferenzenCode.addCodeLine(
				"return f_{x_{0}}, ..., f_{x_{0}, ..., x_{" + String.valueOf(numberOfSamplingPoints - 1)
						+ "}} as γ_{0}, ..., γ_{" + String.valueOf(numberOfSamplingPoints - 1) + "}", null, 2, null);
		dividierteDifferenzenCode.hide();

		// add Interpolationspolynom code group
		interpolationsPolynomCode = language.newSourceCode(new Offset(10, 0, interpolationsPolynomCodeBox,
				AnimalScript.DIRECTION_NW), "InterpolationsPolynomCode", null, interpolationsPolynomCodeProperties);

		interpolationsPolynomCode.addCodeLine("Interpolationspolynom(γ)", null, 0, null);
		interpolationsPolynomCode.addCodeLine("p_{" + String.valueOf(numberOfSamplingPoints - 1) + "}(x) = γ_0", null,
				2, null);
		interpolationsPolynomCode.addCodeLine("for i = 1 to " + String.valueOf(numberOfSamplingPoints - 1), null, 2,
				null);
		interpolationsPolynomCode.addCodeLine("p_{" + String.valueOf(numberOfSamplingPoints - 1) + "}(x) = p_{"
				+ String.valueOf(numberOfSamplingPoints - 1) + "}(x) + γ_{i} * (x - x_{0}) * ... * (x - x_{i - 1})",
				null, 4, null);
		interpolationsPolynomCode.addCodeLine("return p_{" + String.valueOf(numberOfSamplingPoints - 1) + "}(x)", null,
				2, null);
		interpolationsPolynomCode.hide();
	}

	private void createPrimitives() {
		stuetzstellenMatrixProperties = new MatrixProperties();
		determineGridCellWidth(stuetzstellen.getLongestString(), stuetzstellenMatrixProperties);
		stuetzstellenMatrix = new StringMatrix(sMatrixGenerator, new Offset(0, 40, header, AnimalScript.DIRECTION_SW),
				stuetzstellen.toStringMatrix(), "StuetzstellenMatrix", null, stuetzstellenMatrixProperties);
		stuetzstellenMatrix.hide();
		stuetzstellenMatrixGhost = new StringMatrix(sMatrixGenerator, new Offset(0, 40, header,
				AnimalScript.DIRECTION_SW), stuetzstellen.toStringMatrix(), "StuetzstellenMatrixGhost", null,
				stuetzstellenMatrixProperties);
		stuetzstellenMatrixGhost.hide();
		stuetzstellenMatrixDescr = language.newText(new Offset(0, -20, stuetzstellenMatrix, AnimalScript.DIRECTION_NW),
				"stützstellen", "StuetzStellenMatrixDescr", null, normalProperties);
		stuetzstellenMatrixDescr.hide();

		dividierteDifferenzenMatrixProperties = new MatrixProperties();
		determineGridCellWidth(dividierteDifferenzen.getLongestString(), dividierteDifferenzenMatrixProperties);
		dividierteDifferenzenMatrix = new StringMatrix(sMatrixGenerator, new Offset(50, 5, stuetzstellenMatrix,
				AnimalScript.DIRECTION_NE), new String[2 * numberOfSamplingPoints][2 * numberOfSamplingPoints],
				"DividierteDifferenzenMatrix", null, dividierteDifferenzenMatrixProperties);
		dividierteDifferenzenMatrix.hide();
		dividierteDifferenzenMatrixGhost = new StringMatrix(sMatrixGenerator, new Offset(50, 5, stuetzstellenMatrix,
				AnimalScript.DIRECTION_NE), new String[2 * numberOfSamplingPoints][2 * numberOfSamplingPoints],
				"DividierteDifferenzenMatrixGhost", null, dividierteDifferenzenMatrixProperties);
		dividierteDifferenzenMatrixGhost.hide();
		dividierteDifferenzenMatrixDescr = language.newText(new Offset(0, -20, dividierteDifferenzenMatrix,
				AnimalScript.DIRECTION_NW), "dividierte Differenzen", "DividierteDifferenzenMatrixDescr", null,
				normalProperties);
		dividierteDifferenzenMatrixDescr.hide();

		dividierteDifferenzenMatrix.put(0, 0, "x_{i}", null, null);
		dividierteDifferenzenMatrix.put(0, 1, "f_{x_{i}}", null, null);

		rechnung = language.newText(new Offset(0, 50, stuetzstellenMatrix, AnimalScript.DIRECTION_SW), "", "Rechnung",
				null, rechnungProperties);
		rechnung.hide();
		rechnungDescr = language.newText(new Offset(0, -20, rechnung, AnimalScript.DIRECTION_NW), "Rechnung",
				"RechnungDescr", null, normalProperties);
		rechnungDescr.hide();

		gammaMatrixProperties = new MatrixProperties();
		gammaMatrix = new StringMatrix(sMatrixGenerator, new Offset(50, 5, dividierteDifferenzenMatrix,
				AnimalScript.DIRECTION_NE), new String[2][numberOfSamplingPoints], "GammaMatrix", null,
				gammaMatrixProperties);
		gammaMatrix.hide();
		gammaMatrixDescr = language.newText(new Offset(0, -20, gammaMatrix, AnimalScript.DIRECTION_NW), "γ",
				"GammaMatrixDescr", null, normalProperties);
		gammaMatrixDescr.hide();

		newtonPolynom = language.newText(new Offset(0, 0, rechnung, AnimalScript.DIRECTION_NW), "", "NewtonPolynom",
				null, newtonPolynomProperties);
		newtonPolynom.hide();
		newtonPolynomDescr = language.newText(new Offset(0, -20, rechnung, AnimalScript.DIRECTION_NW),
				"Newtonsches Interpolationspolynom", "NewtonPolynomDescr", null, normalProperties);
		newtonPolynomDescr.hide();
	}

	private void createCounter() {
		dividierteDifferenzenCounter = language.newCounter(dividierteDifferenzenMatrix);
		CounterProperties counterProperty = new CounterProperties();
		dividierteDifferenzenView = language.newCounterView(dividierteDifferenzenCounter, new Offset(50, 0,
				dividierteDifferenzenMatrix, AnimalScript.DIRECTION_E), counterProperty, true, true);
		dividierteDifferenzenView.hide();
	}

	private String getConcreteCalculation(int i, int j) {
		StringBuilder builder = new StringBuilder();
		builder.append("(" + dividierteDifferenzen.getBracketedDividierteDifferenz(j + 1, i - 1) + " - "
				+ dividierteDifferenzen.getBracketedDividierteDifferenz(j, i - 1) + ")");
		builder.append(" / ");
		builder.append("(" + stuetzstellen.getBracketedStuetzstelle(j + i, 0) + " - "
				+ stuetzstellen.getBracketedStuetzstelle(j, 0) + ")");

		return builder.toString();
	}

	private void makeStep(int i, int j) {
		StringBuilder rechnungBuilder = new StringBuilder();

		rechnungBuilder.append("f_{{");
		for (int k = j; k < j + i; k++) {
			rechnungBuilder.append("x_{" + k + "}, ");
		}
		rechnungBuilder.append("x_{" + String.valueOf(j + i) + "}} = ");

		rechnung.setText(rechnungBuilder.toString(), null, null);
		rechnung.show();
		rechnungDescr.show();

		dividierteDifferenzenMatrixGhost.put(2 * j + 1, 2 * i, "→", new TicksTiming(100), null);
		dividierteDifferenzenMatrixGhost.put(2 * j + 2, 2 * i, "↗", new TicksTiming(100), null);
		dividierteDifferenzenMatrixGhost.highlightCell(2 * j + 1, 2 * i + 1, new TicksTiming(200), null);

		language.nextStep();

		rechnungBuilder.append("(f_{");
		for (int k = j + 1; k < j + i; k++) {
			rechnungBuilder.append("x_{" + k + "}, ");
		}
		rechnungBuilder.append("x_{" + String.valueOf(j + i) + "}} - ");

		rechnung.setText(rechnungBuilder.toString(), null, null);

		dividierteDifferenzenMatrix.getElement(2 * j + 3, 2 * i - 1);
		dividierteDifferenzenMatrixGhost.highlightCell(2 * j + 3, 2 * i - 1, new TicksTiming(200), null);

		language.nextStep();

		rechnungBuilder.append("f_{");
		for (int k = j; k < j + i - 1; k++) {
			rechnungBuilder.append("x_{" + k + "}, ");
		}
		rechnungBuilder.append("x_{" + String.valueOf(j + i - 1) + "}}) / ");

		rechnung.setText(rechnungBuilder.toString(), null, null);

		dividierteDifferenzenMatrix.getElement(2 * j + 1, 2 * i - 1);
		dividierteDifferenzenMatrixGhost.highlightCell(2 * j + 1, 2 * i - 1, new TicksTiming(200), null);

		language.nextStep();

		rechnungBuilder.append("(x_{" + String.valueOf(j + i) + "} - ");

		dividierteDifferenzenMatrix.getElement(2 * (j + i) + 1, 0);
		dividierteDifferenzenMatrixGhost.highlightCell(2 * (j + i) + 1, 0, new TicksTiming(200), null);

		rechnung.setText(rechnungBuilder.toString(), null, null);

		language.nextStep();

		rechnungBuilder.append("x_{" + j + "}) = ");

		rechnung.setText(rechnungBuilder.toString(), null, null);

		dividierteDifferenzenMatrix.getElement(2 * j + 1, 0);
		dividierteDifferenzenMatrixGhost.highlightCell(2 * j + 1, 0, new TicksTiming(200), null);

		String concreteCalc = getConcreteCalculation(i, j);
		if ((i == numberOfSamplingPoints - 1 && j == 0) || (i == 1 && j == 1)) {
			language.nextStep();

			MultipleChoiceQuestionModel multipleChoiceDividedDifference = new MultipleChoiceQuestionModel(
					"DividierteDifferenzfrage" + i + "_" + j);
			multipleChoiceDividedDifference
					.setPrompt("Durch Einsetzen der konkreten Zahlen erhält man folgende Rechnung für die dividierte Differenz:");

			multipleChoiceDividedDifference.addAnswer(concreteCalc, 10, "Das ist richtig!");
			HashSet<String> answers = new HashSet<String>();
			answers.add(concreteCalc);

			String[] divDiffs = new String[] { dividierteDifferenzen.getBracketedDividierteDifferenz(j + 1, i - 1),
					dividierteDifferenzen.getBracketedDividierteDifferenz(j, i - 1) };
			String[] stStelle = new String[] { stuetzstellen.getBracketedStuetzstelle(j + i, 0),
					stuetzstellen.getBracketedStuetzstelle(j, 0) };
			String wrongAnswer = "Das ist leider falsch. Versuche den Wert anhand der Rechnung abzuleiten.";

			String trap = "(" + stStelle[0] + " - " + stStelle[1] + ") / (" + divDiffs[0] + " - " + divDiffs[1] + ")";
			if (!answers.contains(trap)) {
				multipleChoiceDividedDifference.addAnswer(trap, 0, wrongAnswer);
				answers.add(trap);
			}
			trap = "(" + stStelle[1] + " - " + stStelle[0] + ") / (" + divDiffs[1] + " - " + divDiffs[0] + ")";
			if (!answers.contains(trap)) {
				multipleChoiceDividedDifference.addAnswer(trap, 0, wrongAnswer);
				answers.add(trap);
			}
			trap = "(" + stStelle[0] + " - " + divDiffs[0] + ") / (" + stStelle[1] + " - " + divDiffs[1] + ")";
			if (!answers.contains(trap)) {
				multipleChoiceDividedDifference.addAnswer(trap, 0, wrongAnswer);
				answers.add(trap);
			}
			trap = "(" + stStelle[1] + " - " + divDiffs[0] + ") / (" + stStelle[0] + " - " + divDiffs[1] + ")";
			if (!answers.contains(trap)) {
				multipleChoiceDividedDifference.addAnswer(trap, 0, wrongAnswer);
				answers.add(trap);
			}

			// create some random numbers to make up for discarded entries
			Random rnd = new Random();
			int rndLen = stStelle[0].length();
			while (answers.size() < 5) {
				trap = "(" + rnd.nextDouble() * Math.pow(10, rndLen) + " - " + rnd.nextDouble() * Math.pow(10, rndLen)
						+ ") / (" + rnd.nextDouble() * Math.pow(10, rndLen) + " - " + rnd.nextDouble()
						* Math.pow(10, rndLen) + ")";

				if (!answers.contains(trap)) {
					multipleChoiceDividedDifference.addAnswer(trap, 0, wrongAnswer);
					answers.add(trap);
				}
			}

			language.addMCQuestion(multipleChoiceDividedDifference);
		}

		language.nextStep();

		rechnungBuilder.append(concreteCalc);

		rechnungBuilder.append(" = ");

		rechnung.setText(rechnungBuilder.toString(), null, null);

		language.nextStep();

		if (dividierteDifferenzen.isValid(i, j)) {
			rechnungBuilder.append(dividierteDifferenzen.getDividierteDifferenz(j, i));
			rechnung.setText(rechnungBuilder.toString(), null, null);
			dividierteDifferenzenMatrix.put(2 * j + 1, 2 * i + 1, dividierteDifferenzen.getDividierteDifferenz(j, i),
					null, null);
			dividierteDifferenzenMatrixGhost.put(2 * j + 1, 2 * i + 1,
					dividierteDifferenzen.getDividierteDifferenz(j, i), null, null);
		} else {
			divisionByZero = true;

			language.hideAllPrimitives();

			language.newText(
					new Coordinates(20, 30),
					"Die Newtonformel liefert für die gegebenen Stützstellen kein gültiges Ergebnis, da an dieser Stelle eine Division durch Null auftritt. Bitte geben Sie andere Stützstellen ein.",
					"Fehlermeldung", null);
		}
	}

	private void createEpilogue() {
		language.hideAllPrimitives();
		header.show();

		Text epilogue = language.newText(new Offset(0, 40, header, AnimalScript.DIRECTION_SW),
				"Anmerkungen zur Komplexität", "epilogue", null, captionProperties);

		Text[] complexity = new Text[5];
		complexity[0] = language
				.newText(
						new Offset(0, 25, epilogue, AnimalScript.DIRECTION_SW),
						"Der Berechnungsaufwand der dividierten Differenzen liegt in O(n²), da man am Aufbau der Schleifen ablesen kann, dass n + n * ((n + 1) / 2) + n Schritte benötigt werden.",
						"complexity[0]", null, normalProperties);
		complexity[1] = language
				.newText(
						new Offset(0, 20, complexity[0], AnimalScript.DIRECTION_SW),
						"Das entstandene Polynom lässt sich mit dem sogenannten Horner-Schema mit einem Aufwand von nur O(n) effizient auswerten.",
						"complexity[1]", null, normalProperties);

		complexity[2] = language.newText(new Offset(0, 20, complexity[1], AnimalScript.DIRECTION_SW),
				"Der Vorteil dieses Verfahrens gegenüber der Interpolationsformel von Lagrange ist folgender:",
				"complexity[2]", null, normalProperties);
		complexity[3] = language
				.newText(
						new Offset(0, 5, complexity[2], AnimalScript.DIRECTION_SW),
						"Bei der Hinzunahnme einer neuen Stützstelle müssen nur n zusätzliche dividierte Differenzen berechnet werden, um das bestehende Polynom erweitern zu können.",
						"complexity[3]", null, normalProperties);
		complexity[4] = language
				.newText(
						new Offset(0, 5, complexity[3], AnimalScript.DIRECTION_SW),
						"Bei Lagrange hingegen müsste das komplette Polynom neu aufgebaut werden, was einen Aufwand erfordert, der in O(n²) liegt.",
						"complexity[4]", null, normalProperties);
	}

	public void calc() {
		numberOfSamplingPoints = stuetzstellen.getLength();

		dividierteDifferenzen = new DividedDifferences(stuetzstellen);

		createPrologue();

		language.nextStep("Hintergrundinformationen");

		// hide introduction
		language.hideAllPrimitives();
		header.show();

		// create elements
		createPrimitives();

		variables = language.newVariables();
		variables.openContext();

		createCode();
		createCounter();

		language.nextStep("Initialisierung der Animation");

		newtonInterpolationCode.highlight(0);
		stuetzstellenMatrix.show();
		stuetzstellenMatrixGhost.show();
		stuetzstellenMatrixDescr.show();

		newtonInterpolationCode.toggleHighlight(0, 0, false, 1, 0, new TicksTiming(200), null);

		dividierteDifferenzenCodeBox.show(new TicksTiming(275));
		dividierteDifferenzenCode.show(new TicksTiming(325));

		language.nextStep("Initialisierung der dividierten Differenzen");

		dividierteDifferenzenCode.highlight(0);

		dividierteDifferenzenMatrix.show();
		dividierteDifferenzenMatrixGhost.show();
		dividierteDifferenzenView.show();
		dividierteDifferenzenMatrixDescr.show();

		StringBuilder lineBuilder;
		for (int i = 1; i <= numberOfSamplingPoints; i++) {
			lineBuilder = new StringBuilder();
			lineBuilder.append("swapGridValues ");
			lineBuilder.append("\"" + stuetzstellenMatrixGhost.getName());
			lineBuilder.append("[" + String.valueOf(2 * i - 1) + "]");
			lineBuilder.append("[" + 0 + "]" + "\"");
			lineBuilder.append(" and ");
			lineBuilder.append("\"" + dividierteDifferenzenMatrix.getName());
			lineBuilder.append("[" + String.valueOf(2 * i - 1) + "]");
			lineBuilder.append("[" + 0 + "]" + "\"");
			lineBuilder.append("after 200 ticks within 200 ticks");
			language.addLine(lineBuilder);

			dividierteDifferenzenMatrix.put(0, 0, "x_{i}", new TicksTiming(400), null);
		}

		language.nextStep("1. Iteration der Initialisierung");

		dividierteDifferenzenCode.unhighlight(0);

		variables.declare("int", "i", "1");
		for (int i = 1; i <= numberOfSamplingPoints; i++) {
			dividierteDifferenzenCode.highlight(1);
			variables.set("i", String.valueOf(i - 1));

			dividierteDifferenzenCode.toggleHighlight(1, 0, false, 2, 0, new TicksTiming(100), null);

			if (i == numberOfSamplingPoints) {
				FillInBlanksQuestionModel fillInInitialisierung = new FillInBlanksQuestionModel("Initialisierungsfrage");
				fillInInitialisierung.setPrompt("Welcher Wert wird f_{x_{" + String.valueOf(i - 1) + "}} zugewiesen?");
				fillInInitialisierung.addAnswer(stuetzstellen.getStuetzstelle(i - 1, 1), 10, "Das ist richtig!");
				fillInInitialisierung.addAnswer(stuetzstellen.getStuetzstelle(i - 1, 1).replace(",", "."), 10,
						"Das ist richtig!");
				language.addFIBQuestion(fillInInitialisierung);

				language.nextStep();
			}

			lineBuilder = new StringBuilder();
			lineBuilder.append("swapGridValues ");
			lineBuilder.append("\"" + stuetzstellenMatrixGhost.getName());
			lineBuilder.append("[" + String.valueOf(2 * i - 1) + "]");
			lineBuilder.append("[" + 1 + "]" + "\"");
			lineBuilder.append(" and ");
			lineBuilder.append("\"" + dividierteDifferenzenMatrix.getName());
			lineBuilder.append("[" + String.valueOf(2 * i - 1) + "]");
			lineBuilder.append("[" + 1 + "]" + "\"");
			lineBuilder.append("after 200 ticks within 200 ticks");
			language.addLine(lineBuilder);

			dividierteDifferenzenMatrix.put(0, 0, "x_{i}", new TicksTiming(200), null);

			if (i == numberOfSamplingPoints) {
				language.nextStep("1. Iteration der Berechnung");
			} else {
				language.nextStep(String.valueOf(i + 1) + ". Iteration der Initialisierung");
			}

			dividierteDifferenzenCode.unhighlight(2);
		}
		variables.discard("i");

		dividierteDifferenzenCode.unhighlight(2, 0, false, null, null);

		variables.declare("int", "i", "1");
		variables.declare("int", "j", "0");
		int ticks;
		int iterationsCounter = 2;
		for (int i = 2; i <= numberOfSamplingPoints; i++) {
			ticks = 200;
			dividierteDifferenzenCode.highlight(3);

			for (int j = 0; j <= numberOfSamplingPoints - i; j++) {
				dividierteDifferenzenCode.toggleHighlight(3, 0, false, 4, 0, new TicksTiming(ticks), null);
				dividierteDifferenzenCode.toggleHighlight(4, 0, false, 5, 0, new TicksTiming(ticks + 200), null);

				variables.set("i", String.valueOf(i - 1));
				variables.set("j", String.valueOf(j));

				language.nextStep();

				makeStep(i - 1, j);

				if (divisionByZero) {
					return;
				}

				if (i == numberOfSamplingPoints && j == 0) {
					language.nextStep();
				} else {
					language.nextStep(iterationsCounter + ". Iteration der Berechnung");
					iterationsCounter++;
				}

				for (int k = 0; k < 2 * numberOfSamplingPoints; k++) {
					dividierteDifferenzenMatrixGhost.unhighlightCellColumnRange(k, 0, 2 * numberOfSamplingPoints - 1,
							null, null);
				}

				ticks = 0;
				dividierteDifferenzenCode.unhighlight(5);
				rechnung.hide();
				rechnungDescr.hide();
			}

			dividierteDifferenzenCode.unhighlight(4);
		}

		variables.discard("i");
		variables.discard("j");

		dividierteDifferenzenCode.highlight(6);

		dividierteDifferenzenMatrixGhost.put(1, 1, stuetzstellen.getStuetzstelle(0, 1), null, null);

		gammaMatrix.show(new TicksTiming(200));
		gammaMatrixDescr.show(new TicksTiming(200));

		for (int i = 0; i < numberOfSamplingPoints; i++) {
			gammaMatrix.put(0, i, "γ_{" + i + "}", null, new TicksTiming(200));
			language.addLine("swapGridValues \"" + dividierteDifferenzenMatrix.getName() + "[1]["
					+ String.valueOf(2 * i + 1) + "]\" and \"" + gammaMatrix.getName() + "[1][" + i
					+ "]\" after 400 ticks within 200 ticks");
		}

		dividierteDifferenzenCounter.accessInc(numberOfSamplingPoints);

		language.nextStep("Initialisierung des Polynoms");

		dividierteDifferenzenCode.unhighlight(6);
		dividierteDifferenzenCode.hide(new TicksTiming(75));
		dividierteDifferenzenCodeBox.hide(new TicksTiming(125));

		newtonInterpolationCode.toggleHighlight(1, 0, false, 2, 0, new TicksTiming(200), null);

		interpolationsPolynomCodeBox.show(new TicksTiming(275));
		interpolationsPolynomCode.show(new TicksTiming(325));

		interpolationsPolynomCode.highlight(0, 0, false, new TicksTiming(400), null);

		interpolationsPolynomCode.toggleHighlight(0, 0, false, 1, 0, new TicksTiming(600), null);

		newtonPolynom.show();

		StringBuilder newtonPolynomBuilder = new StringBuilder("p_{" + String.valueOf(numberOfSamplingPoints - 1)
				+ "}(x) = " + dividierteDifferenzen.getBracketedDividierteDifferenz(0, 0));

		gammaMatrix.highlightCell(1, 0, new TicksTiming(700), null);

		newtonPolynom.setText(newtonPolynomBuilder.toString(), new TicksTiming(800), null);
		newtonPolynomDescr.show(new TicksTiming(800));

		language.nextStep("1. Iteration der Polynomerstellung");

		gammaMatrix.unhighlightCell(1, 0, null, null);
		interpolationsPolynomCode.unhighlight(1);

		variables.declare("int", "i", "1");
		for (int i = 1; i < numberOfSamplingPoints; i++) {
			variables.set("i", String.valueOf(i));
			interpolationsPolynomCode.highlight(2);
			newtonPolynomBuilder.append(" + " + dividierteDifferenzen.getBracketedDividierteDifferenz(0, i)
					+ " * (x - " + stuetzstellen.getBracketedStuetzstelle(0, 0) + ") ");
			stuetzstellenMatrixGhost.highlightCell(1, 0, new TicksTiming(300), null);
			for (int j = 1; j < i; j++) {
				newtonPolynomBuilder.append("* (x - " + stuetzstellen.getBracketedStuetzstelle(j, 0) + ") ");
				stuetzstellenMatrixGhost.highlightCell(2 * j + 1, 0, new TicksTiming(300), null);
			}
			interpolationsPolynomCode.toggleHighlight(2, 0, false, 3, 0, new TicksTiming(200), null);
			gammaMatrix.highlightCell(1, i, new TicksTiming(300), null);
			newtonPolynom.setText(newtonPolynomBuilder.toString(), new TicksTiming(400), null);
			if (i == numberOfSamplingPoints - 1) {
				language.nextStep();
			} else {
				language.nextStep(String.valueOf(i + 1) + ". Iteration der Polynomerstellung");
			}
			interpolationsPolynomCode.unhighlight(3);
			gammaMatrix.unhighlightCell(1, i, null, null);
			stuetzstellenMatrixGhost.unhighlightCellRowRange(0, 2 * numberOfSamplingPoints - 1, 0, null, null);
		}
		variables.discard("i");

		interpolationsPolynomCode.toggleHighlight(3, 4);
		interpolationsPolynomCode.unhighlight(4, 0, false, new TicksTiming(200), null);
		newtonInterpolationCode.toggleHighlight(2, 0, false, 3, 0, new TicksTiming(400), null);

		language.nextStep();

		MultipleChoiceQuestionModel multipleChoiceComplexity = new MultipleChoiceQuestionModel("Komplexitätsfrage");
		multipleChoiceComplexity
				.setPrompt("Angenommen, die Berechnung einer einzelnen dividierten Differenz liegt in O(1), dann besitzt die vorgestellte Newtonformel folgende Komplexität:");
		multipleChoiceComplexity.addAnswer("O(n)", 0,
				"Das ist leider falsch. Schaue dir noch einmal die Schleifenstruktur an.");
		multipleChoiceComplexity
				.addAnswer("O(n log n)", 0,
						"Das ist leider falsch. Versuche die Komplexitätsklasse anhand der Struktur des Pseudocodes abzuleiten");
		multipleChoiceComplexity.addAnswer("O(n²)", 10, "Das ist richtig!");
		multipleChoiceComplexity.addAnswer("O(n³)", 0,
				"Das ist leider falsch. Schaue dir noch einmal die Schleifenstruktur an.");
		language.addMCQuestion(multipleChoiceComplexity);

		language.nextStep("Hinweise zur Komplexität");

		createEpilogue();
	}

	private void determineGridCellWidth(String longestString, MatrixProperties matrixProperties) {
		FontMetrics fm = Animal.getConcreteFontMetrics((Font) matrixProperties
				.get(AnimationPropertiesKeys.FONT_PROPERTY));

		sMatrixGenerator.setCellWidth(2 * fm.stringWidth(longestString) + 5);
	}

}
