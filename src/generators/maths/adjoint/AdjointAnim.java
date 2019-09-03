package generators.maths.adjoint;

import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import animal.main.Animal;

public class AdjointAnim {

	private NoRefreshStringMatrixGenerator sMatrixGenerator;

	private Language language;

	private Text header;
	private Text introduction;
	private Text[] descr;

	private TextProperties captionProperties;
	private TextProperties normalProperties;
	private TextProperties smallProperties;

	private PolylineProperties polylineProperties;

	private Variables variables;

	private Rect codeBox;
	private SourceCode mainCode;
	private SourceCodeProperties mainSourceCodeProperties;
	private SourceCode minorCode;
	private SourceCodeProperties minorSourceCodeProperties;

	private Text counterI;
	private Text counterJ;

	private MatrixProperties matrixProperties;

	private StringMatrix eingabematrix;
	private Text eingabeMatrixDescr;

	private StringMatrix unterdeterminanteMatrixBIG;
	private StringMatrix unterdeterminanteMatrixBIGGhost;

	private StringMatrix unterdeterminanteMatrixSMALL;
	private Text unterdeterminanteMatrixSMALLDescr;

	private Polyline unterdeterminanteArrow;

	private Text unterdeterminante;
	private Text unterdeterminanteDescr;

	private Polyline signedUnterdeterminanteArrow;
	private Text signUnterdeterminante;

	private Text signedUnterdeterminante;
	private Text signedUnterdeterminanteDescr;

	private Polyline kofaktormatrixArrow;

	private Matrix cofactors;
	private StringMatrix kofaktormatrix;
	private Text kofaktormatrixDescr;

	private TwoValueCounter inputCounter;
	private TwoValueView inputView;

	private int normalCellWidth;
	private int kofaktorCellWidth;

	private StringMatrix adjunkte;
	@SuppressWarnings("unused")
	private Text adjunkteDescr;

	private int wrongUntermatrixQuestions;
	private int realUntermatrixQuestions;

	AdjointAnim(Language lang, SourceCodeProperties mainCodeProperties, SourceCodeProperties minorCodeProperties,
			MatrixProperties matrixProperties) {
		language = lang;
		sMatrixGenerator = new NoRefreshStringMatrixGenerator((AnimalScript) language);

		this.mainSourceCodeProperties = mainCodeProperties;
		this.minorSourceCodeProperties = minorCodeProperties;

		this.matrixProperties = matrixProperties;
	}

	private int determineGridCellWidth(Matrix m) {
		String[][] sMatrix = m.toStringMatrix();

		FontMetrics fm = Animal.getConcreteFontMetrics((Font) matrixProperties
				.get(AnimationPropertiesKeys.FONT_PROPERTY));
		int max = fm.stringWidth("10");
		for (int i = 0; i < sMatrix.length; ++i) {
			for (int j = 0; j < sMatrix[0].length; ++j) {
				int currentWidth = fm.stringWidth(sMatrix[i][j]);

				max = Math.max(max, currentWidth);
			}
		}

		return 2 * max + 5;
	}

	private void createPrologue() {
		TextProperties headerProperties = new TextProperties();
		headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		header = language.newText(new Coordinates(20, 30), "Berechnung der Adjunkten einer Matrix", "Header", null,
				headerProperties);

		captionProperties = new TextProperties();
		captionProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 22));
		introduction = language.newText(new Offset(0, 40, header, AnimalScript.DIRECTION_SW),
				"Hintergrundinformationen", "DescrHd", null, captionProperties);

		normalProperties = new TextProperties();
		normalProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));

		descr = new Text[3];
		descr[0] = language
				.newText(
						new Offset(0, 25, introduction, AnimalScript.DIRECTION_SW),
						"Als Adjunkte einer quadratischen Matrix bezeichnet man die Transponierte der Kofaktormatrix, welche aus den vorzeichengewichteten Unterdeterminanten besteht.",
						"Descr[0]", null, normalProperties);
		descr[1] = language.newText(new Offset(0, 10, descr[0], AnimalScript.DIRECTION_SW),
				"adj(A) = Cof(A)^T mit A ∈ ℝ^(n×n)", "Descr[1]", null, normalProperties);
		descr[2] = language
				.newText(
						new Offset(0, 10, descr[1], AnimalScript.DIRECTION_SW),
						"Sie kann bespielsweise dafür verwendet werden, um die Inverse einer regulären quadratischen Matrix zu berechnen.",
						"Descr[2]", null, normalProperties);

		smallProperties = new TextProperties();
		smallProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 12));

		polylineProperties = new PolylineProperties();
		polylineProperties.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
	}

	private void createCode() {
		// add main code group
		mainCode = language.newSourceCode(new Offset(5, 85, eingabematrix, AnimalScript.DIRECTION_SW), "MainCode",
				null, mainSourceCodeProperties);

		mainCode.addCodeLine("Adjunktenberechnung(eingabematrix)", null, 0, null);
		mainCode.addCodeLine("for i = 1 to n", null, 2, null);
		mainCode.addCodeLine("for j = 1 to n", null, 4, null);
		mainCode.addCodeLine("unterdeterminante = Unterdeterminante(eingabematrix, i, j)", null, 6, null);
		mainCode.addCodeLine("signedUD = (-1)^(i + j) * unterdeterminante", null, 6, null);
		mainCode.addCodeLine("kofaktormatrix[i][j] = signedUD", null, 6, null);
		mainCode.addCodeLine("adjunkte = Transponiere kofaktormatrix", null, 2, null);
		mainCode.addCodeLine("return adjunkte", null, 2, null);

		// add minor code group
		minorCode = language.newSourceCode(new Offset(0, 20, mainCode, AnimalScript.DIRECTION_SW), "MinorCode", null,
				minorSourceCodeProperties);

		minorCode.addCodeLine("Unterdeterminante(eingabematrix, i, j)", null, 0, null);
		minorCode.addCodeLine("m = Streiche Zeile i und Spalte j aus eingabematrix", null, 2, null);
		minorCode.addCodeLine("return Determinante(m)", null, 2, null);

		// add code box
		RectProperties codeBoxProperties = new RectProperties();
		codeBoxProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		codeBoxProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(236, 236, 236));
		codeBoxProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
		codeBox = language.newRect(new Offset(0, 85, eingabematrix, AnimalScript.DIRECTION_SW), new Offset(100, 10,
				minorCode, AnimalScript.DIRECTION_SE), "CodeBox", null, codeBoxProperties);
	}

	private void createPrimitives(Matrix input) {
		sMatrixGenerator.setCellWidth(normalCellWidth);

		eingabematrix = new StringMatrix(sMatrixGenerator, new Offset(40, 50, header, AnimalScript.DIRECTION_SW),
				input.toStringMatrix(), "Eingabematrix", null, matrixProperties);
		eingabeMatrixDescr = language.newText(new Offset(0, -30, eingabematrix, AnimalScript.DIRECTION_NW),
				"eingabematrix", "EingabematrixDescr", null, smallProperties);

		counterI = language.newText(new Offset(-40, 0, eingabematrix, AnimalScript.DIRECTION_W), "i = ", "CounterI",
				null);

		counterJ = language.newText(new Offset(0, 10, eingabematrix, AnimalScript.DIRECTION_S), "j = ", "CounterJ",
				null);

		unterdeterminanteMatrixBIG = new StringMatrix(sMatrixGenerator, new Offset(0, 0, eingabematrix,
				AnimalScript.DIRECTION_NW), new String[input.getLength()][input.getLength()],
				"UnterdeterminanteMatrixBIG", null, matrixProperties);
		unterdeterminanteMatrixBIG.hide();

		// Add an invisible, empty matrix to the second position of unterdeterminanteMatrixBIG to be able to compute the
		// right position for unterdeterminanteMatrixSMALL. This is necessary because of the "move bug" that prevents us
		// from getting the right position for unterdeterminanteMatrixSMALL after moving unterdeterminanteMatrixBIG from
		// its first position to its second.
		unterdeterminanteMatrixBIGGhost = new StringMatrix(sMatrixGenerator, new Offset(50, 0, eingabematrix,
				AnimalScript.DIRECTION_NE), new String[input.getLength()][input.getLength()],
				"UnterdeterminanteMatrixBIGGhost", null, matrixProperties);
		unterdeterminanteMatrixBIGGhost.hide();

		unterdeterminanteMatrixSMALL = new StringMatrix(sMatrixGenerator, new Offset(50, 0,
				unterdeterminanteMatrixBIGGhost, AnimalScript.DIRECTION_NE),
				new String[input.getLength() - 1][input.getLength() - 1], "UnterdeterminanteMatrixSMALL", null,
				matrixProperties);
		unterdeterminanteMatrixSMALL.hide();
		unterdeterminanteMatrixSMALLDescr = language.newText(new Offset(0, -30, unterdeterminanteMatrixSMALL,
				AnimalScript.DIRECTION_NW), "m", "UnterdeterminanteMatrixSMALLDescr", null);
		unterdeterminanteMatrixSMALLDescr.hide();

		unterdeterminante = language.newText(
				new Offset(80, 0, unterdeterminanteMatrixSMALL, AnimalScript.DIRECTION_NE),
				cofactors.getLongestString(), "unterdeterminante", null);
		unterdeterminante.hide();
		unterdeterminanteDescr = language.newText(new Offset(-45, -30, unterdeterminante, AnimalScript.DIRECTION_N),
				"Unterdeterminante", "UnterdeterminanteDescr", null);
		unterdeterminanteDescr.hide();

		unterdeterminanteArrow = language.newPolyline(new Offset[] {
				new Offset(5, 0, unterdeterminanteMatrixSMALL, AnimalScript.DIRECTION_E),
				new Offset(-5, 0, unterdeterminante, AnimalScript.DIRECTION_W) }, "UnterdeterminanteArrow", null,
				polylineProperties);
		unterdeterminanteArrow.hide();

		signedUnterdeterminante = language.newText(new Offset(120, 0, unterdeterminante, AnimalScript.DIRECTION_NE),
				cofactors.getLongestString(), "SignedUnterdeterminante", null);
		signedUnterdeterminante.hide();
		signedUnterdeterminanteDescr = language.newText(new Offset(-20, -30, signedUnterdeterminante,
				AnimalScript.DIRECTION_N), "signedUD", "SignedUnterdeterminanteDescr", null);
		signedUnterdeterminanteDescr.hide();

		signedUnterdeterminanteArrow = language.newPolyline(new Offset[] {
				new Offset(15, 0, unterdeterminante, AnimalScript.DIRECTION_E),
				new Offset(-5, 0, signedUnterdeterminante, AnimalScript.DIRECTION_W) }, "SignedUnterdeterminanteArrow",
				null, polylineProperties);
		signedUnterdeterminanteArrow.hide();
		signUnterdeterminante = language.newText(new Offset(-40, 5, signedUnterdeterminanteArrow,
				AnimalScript.DIRECTION_N), "", "SignUnterdeterminante", null);
		signedUnterdeterminante.hide();

		sMatrixGenerator.setCellWidth(kofaktorCellWidth);
		kofaktormatrix = new StringMatrix(sMatrixGenerator, new Offset(100, 0, signedUnterdeterminante,
				AnimalScript.DIRECTION_NE), new String[input.getLength()][input.getLength()], "Kofaktormatrix", null,
				matrixProperties);
		kofaktormatrixDescr = language.newText(new Offset(0, -30, kofaktormatrix, AnimalScript.DIRECTION_NW),
				"kofaktormatrix", "KofaktormatrixDescr", null, smallProperties);

		kofaktormatrixArrow = language.newPolyline(new Offset[] {
				new Offset(15, 0, signedUnterdeterminante, AnimalScript.DIRECTION_E),
				new Offset(-5, 0, kofaktormatrix, AnimalScript.DIRECTION_W) }, "KofaktormatrixArrow", null,
				polylineProperties);
		kofaktormatrixArrow.hide();

		sMatrixGenerator.setCellWidth(normalCellWidth);
	}

	private void createCounter() {
		inputCounter = language.newCounter(eingabematrix);
		CounterProperties counterProperty = new CounterProperties();
		inputView = language.newCounterView(inputCounter, new Offset(0, 35, eingabematrix, AnimalScript.DIRECTION_SW),
				counterProperty, true, true);
	}

	private void makeStep(Matrix input, List<Primitive> keepThese, int i, int j) {
		language.hideAllPrimitives();
		for (Primitive p : keepThese) {
			p.show();
		}
		language.hideInThisStep.add(unterdeterminanteMatrixBIG.getName());
		language.hideInThisStep.add(unterdeterminanteMatrixSMALL.getName());
		inputView.show();
		if (cofactors.getLength() > 4) {
			inputView.hideBar();
		}

		for (int counter = 0; counter < input.getLength(); counter++) {
			eingabematrix.unhighlightCellColumnRange(counter, 0, input.getLength() - 1, null, null);
			kofaktormatrix.unhighlightCellColumnRange(counter, 0, input.getLength() - 1, null, null);
		}

		mainCode.unhighlight(5);

		int ticks = 200;

		if (i == 0 && j == 0) {
			mainCode.highlight(0);

			mainCode.toggleHighlight(0, 0, false, 1, 0, new TicksTiming(200), null);
			counterI.setText("i = 1", new TicksTiming(200), null);
			variables.declare("int", "i", "1");

			mainCode.toggleHighlight(1, 0, false, 2, 0, new TicksTiming(400), null);
			counterJ.setText("j = 1", new TicksTiming(400), null);
			variables.declare("int", "j", "1");

			mainCode.toggleHighlight(2, 0, false, 3, 0, new TicksTiming(600), null);

			ticks += 400;
		} else if (j == 0) {
			mainCode.highlight(1);
			String iValue = String.valueOf(i + 1);
			counterI.setText("i = " + iValue, null, null);
			variables.set("i", iValue);

			mainCode.toggleHighlight(1, 0, false, 2, 0, new TicksTiming(200), null);
			String jValue = String.valueOf(j + 1);
			counterJ.setText("j = " + jValue, new TicksTiming(200), null);
			variables.set("j", jValue);

			mainCode.toggleHighlight(2, 0, false, 3, 0, new TicksTiming(400), null);

			ticks += 200;
		} else {
			mainCode.highlight(2);
			String jValue = String.valueOf(j + 1);
			counterJ.setText("j = " + jValue, null, null);
			variables.set("j", jValue);

			mainCode.toggleHighlight(2, 0, false, 3, 0, new TicksTiming(200), null);
		}

		eingabematrix.highlightCell(i, j, new TicksTiming(ticks), null);
		kofaktormatrix.highlightCell(i, j, new TicksTiming(ticks), null);

		language.nextStep();

		minorCode.highlight(0);
		unterdeterminanteMatrixBIG = new StringMatrix(sMatrixGenerator, new Offset(0, 0, eingabematrix,
				AnimalScript.DIRECTION_NW), input.toStringMatrix(), "UnterdeterminanteMatrixBIG", null,
				matrixProperties);
		unterdeterminanteMatrixBIG.show();
		try {
			unterdeterminanteMatrixBIG.moveTo(null, null, new Offset(50, 0, eingabematrix, AnimalScript.DIRECTION_NE),
					null, new TicksTiming(200));
		} catch (IllegalDirectionException e) {
			e.printStackTrace();
		}

		minorCode.toggleHighlight(0, 0, false, 1, 0, new TicksTiming(400), null);

		// ask if the following matrix is the correct submatrix
		if ((i + j > 0) && (realUntermatrixQuestions + wrongUntermatrixQuestions < 2)) {
			language.nextStep();
			String subMatrix;
			boolean correct;
			if ((Math.random() < 0.5d) || (wrongUntermatrixQuestions > realUntermatrixQuestions)) {
				// real submatrix
				correct = true;
				realUntermatrixQuestions++;
				subMatrix = input.getUntermatrix(i + 1, j + 1).toDisplay();
			} else {
				// wrong submatrix
				correct = false;
				wrongUntermatrixQuestions++;
				subMatrix = input.getTransponierteMatrix().getUntermatrix(i + 1, j + 1).toDisplay();
			}

			TrueFalseQuestionModel tfSubMatrix = new TrueFalseQuestionModel("tfSubmatrix" + i + "_" + j, correct, 10);
			tfSubMatrix.setPrompt("Ist " + subMatrix
					+ " die Untermatrix, die durch Streichen der i-ten Zeile und j-ten Spalte hervorgeht?");
			language.addTFQuestion(tfSubMatrix);
			language.nextStep();
		}

		unterdeterminanteMatrixBIG.highlightCellColumnRange(i, 0, input.getLength() - 1, new TicksTiming(400), null);
		unterdeterminanteMatrixBIG.highlightCellRowRange(0, input.getLength() - 1, j, new TicksTiming(400), null);

		for (int counter = 0; counter < input.getLength(); counter++) {
			unterdeterminanteMatrixBIG.put(i, counter, "", new TicksTiming(600), null);
			unterdeterminanteMatrixBIG.put(counter, j, "", new TicksTiming(600), null);
		}

		unterdeterminanteMatrixSMALL = new StringMatrix(sMatrixGenerator, new Offset(50, 0,
				unterdeterminanteMatrixBIGGhost, AnimalScript.DIRECTION_NE), input.getUntermatrix(i + 1, j + 1)
				.toStringMatrix(), "UnterdeterminanteMatrixSMALL", null, matrixProperties);
		inputCounter.accessInc((input.getLength() - 1) * (input.getLength() - 1));
		unterdeterminanteMatrixSMALL.hide();
		unterdeterminanteMatrixSMALL.show(new TicksTiming(800));
		unterdeterminanteMatrixSMALLDescr.show(new TicksTiming(800));

		double unterdeterminanteValue = input.calcUnterdeterminante(i + 1, j + 1);
		String unterdeterminanteText = NumberFormat.getInstance().format(unterdeterminanteValue);

		// ask for the determinant of the submatrix if it can be calculated easily
		if ((input.getLength() <= 4) && ((i == 0 && j == 1) || (i == input.getLength() - 1 && j == 0))) {
			language.nextStep();
			FillInBlanksQuestionModel fibUnterdeterminante = new FillInBlanksQuestionModel("fibUnterdeterminante" + i
					+ "_" + j);
			fibUnterdeterminante.setPrompt("Wie lautet die Unterdeterminante?");
			fibUnterdeterminante.addAnswer(unterdeterminanteText, 10, "Das ist richtig!");
			language.addFIBQuestion(fibUnterdeterminante);
		}
		language.nextStep();

		minorCode.toggleHighlight(1, 2);

		unterdeterminante.setText(unterdeterminanteText, null, null);
		unterdeterminante.show();
		unterdeterminanteDescr.show();
		unterdeterminanteArrow.show();

		language.nextStep();

		minorCode.unhighlight(2);
		mainCode.toggleHighlight(3, 4);

		String kofaktorValue = NumberFormat.getInstance().format(unterdeterminanteValue * Math.pow(-1, i + 1 + j + 1));
		signedUnterdeterminante.setText(kofaktorValue, null, null);
		signedUnterdeterminante.show();
		signedUnterdeterminanteDescr.show();
		signedUnterdeterminanteArrow.show();
		signUnterdeterminante.setText("*(-1)^(" + String.valueOf(i + 1) + " + " + String.valueOf(j + 1) + ")", null,
				null);
		signUnterdeterminante.show();

		language.nextStep();

		mainCode.toggleHighlight(4, 5);
		kofaktormatrixArrow.show();
		kofaktormatrix.put(i, j, kofaktorValue, null, null);

		if ((i == input.getLength() - 1) && (j == input.getLength() - 1)) {
			language.nextStep("Gewinnung der Adjunkten");
		} else {
			language.nextStep(String.valueOf(i * cofactors.getLength() + j + 2) + ". Iteration");
		}
	}

	private void createEpilogue() {
		Text epilogue = language.newText(new Offset(0, 40, header, AnimalScript.DIRECTION_SW),
				"Anmerkungen zur Komplexität", "epilogue", null, captionProperties);

		Text[] complexity = new Text[3];
		complexity[0] = language
				.newText(
						new Offset(0, 25, epilogue, AnimalScript.DIRECTION_SW),
						"Gegeben eine n×n quadratische Matrix, so muss für insgesamt n² Einträge der Kofaktor berechnet werden.",
						"complexity[0]", null, normalProperties);
		complexity[1] = language
				.newText(
						new Offset(0, 10, complexity[0], AnimalScript.DIRECTION_SW),
						"Die Komplexität des eigentlichen Algorithmus liegt somit in O(n²). Allerdings muss die Berechnung der Unterdeterminanten je nach gewähltem Verfahren noch berücksichtigt werden,",
						"complexity[1]", null, normalProperties);
		complexity[2] = language
				.newText(
						new Offset(0, 0, complexity[1], AnimalScript.DIRECTION_SW),
						"so dass sich zum Beispiel bei der Verwendung des Laplaceschen Entwicklungssatzes (O(n!)) eine Gesamtkomplexität von O(n²(n-1)!) ergeben würde.",
						"complexity[2]", null, normalProperties);
	}

	public void calc(Matrix input) {
		realUntermatrixQuestions = 0;
		wrongUntermatrixQuestions = 0;
		cofactors = input.calculateCofactors();
		normalCellWidth = determineGridCellWidth(input);
		kofaktorCellWidth = determineGridCellWidth(cofactors);

		createPrologue();

		language.nextStep();

		// hide introduction
		introduction.hide();
		descr[0].hide();
		descr[1].hide();
		descr[2].hide();

		// create elements
		createPrimitives(input);

		variables = language.newVariables();
		variables.openContext();

		createCode();
		createCounter();

		List<Primitive> keepThese = new ArrayList<Primitive>();
		keepThese.add(header);
		keepThese.add(codeBox);
		keepThese.add(mainCode);
		keepThese.add(minorCode);
		keepThese.add(counterI);
		keepThese.add(counterJ);
		keepThese.add(eingabematrix);
		keepThese.add(eingabeMatrixDescr);
		keepThese.add(kofaktormatrix);
		keepThese.add(kofaktormatrixDescr);

		language.nextStep("1. Iteration");

		for (int i = 0; i < input.getLength(); i++) {
			for (int j = 0; j < input.getLength(); j++) {
				makeStep(input, keepThese, i, j);
			}
		}

		mainCode.toggleHighlight(5, 6);

		sMatrixGenerator.setCellWidth(kofaktorCellWidth);

		adjunkte = new StringMatrix(sMatrixGenerator, new Offset(0, 100, kofaktormatrix, AnimalScript.DIRECTION_SW),
				new String[input.getLength()][input.getLength()], "Adjunkte", null, matrixProperties);
		adjunkteDescr = language.newText(new Offset(0, -30, adjunkte, AnimalScript.DIRECTION_NW), "adjunkte",
				"AdjunkteDescr", null);

		sMatrixGenerator.setCellWidth(normalCellWidth);

		int ticks = 200;
		StringBuilder lineBuilder;
		for (int i = 0; i < input.getLength(); i++) {
			for (int j = 0; j < input.getLength(); j++) {
				kofaktormatrix.getElement(i, j);
				lineBuilder = new StringBuilder();
				lineBuilder.append("swapGridValues ");
				lineBuilder.append("\"" + kofaktormatrix.getName());
				lineBuilder.append("[" + i + "]");
				lineBuilder.append("[" + j + "]" + "\"");
				lineBuilder.append(" and ");
				lineBuilder.append("\"" + adjunkte.getName());
				lineBuilder.append("[" + j + "]");
				lineBuilder.append("[" + i + "]" + "\"");
				lineBuilder.append(" after " + ticks + " ticks within 200 ticks");

				language.addLine(lineBuilder);
			}
			ticks += 200;
		}

		mainCode.toggleHighlight(6, 0, false, 7, 0, new TicksTiming(ticks), null);

		language.nextStep();
		MultipleChoiceQuestionModel mcComplexity = new MultipleChoiceQuestionModel("questionComplexity");
		mcComplexity
				.setPrompt("Angenommen, die Berechnung einer Determinanten läge in O(1), dann besitzt der vorgestellte Algorithmus zur Adjunktenberechnung folgende Komplexität:");
		mcComplexity.addAnswer("O(n)", 0, "Das ist leider falsch. Schaue dir noch einmal die Schleifenstruktur an.");
		mcComplexity
				.addAnswer("O(n log n)", 0,
						"Das ist leider falsch. Versuche die Komplexitätsklasse anhand der Struktur des Pseudocodes abzuleiten.");
		mcComplexity.addAnswer("O(n²)", 10, "Das ist richtig!");
		mcComplexity.addAnswer("O(n³)", 0, "Das ist leider falsch. Schaue dir noch einmal die Schleifenstruktur an.");
		language.addMCQuestion(mcComplexity);

		language.nextStep("Anmerkungen zur Komplexität");

		language.hideAllPrimitives();

		language.hideInThisStep.add(eingabematrix.getName());
		language.hideInThisStep.add(unterdeterminanteMatrixBIG.getName());
		language.hideInThisStep.add(unterdeterminanteMatrixSMALL.getName());
		language.hideInThisStep.add(kofaktormatrix.getName());
		language.hideInThisStep.add(adjunkte.getName());

		header.show();

		createEpilogue();
	}

}
