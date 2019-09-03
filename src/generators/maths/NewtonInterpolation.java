/*
 * NewtonInterpolation.java
 * Lennard Rüdesheim, Tim Steinke, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.Primitive;
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

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;
import algoanim.animalscript.AnimalRectGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalTextGenerator;

public class NewtonInterpolation implements ValidatingGenerator {
	private Language lang;
//	private String[][] stuetzstellen;

	public void init() {
		lang = new AnimalScript(
				"Newtonsche Interpolationsformel mit Schema der dividierten Differenzen",
				"Lennard Ruedesheim, Tim Steinke", 800, 600);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		String [][] stuetzStellenString = (String[][]) primitives.get("stuetzstellen");
		double[][] stuetzStellen = new double[stuetzStellenString.length][stuetzStellenString[0].length];
		for (int i = 0; i < stuetzStellenString[0].length; ++i) {
			for (int j = 0; j < stuetzStellenString.length; ++j) {
				stuetzStellen[j][i] = Double.parseDouble(stuetzStellenString[j][i]);
			}
        }
		newtonSwap(stuetzStellen);
		lang.finalizeGeneration();
		return lang.toString();
	}

	public void newtonSwap(double[][] stuetz) {
		// m: dimensionaliy/has to be 2
		int m = stuetz[0].length;
		int n = stuetz.length;
		double[][] dArray = new double[n][m];
		for (int i = 0; i < m; ++i) {
			for (int j = 0; j < n; ++j) {
				dArray[j][i] = (double) stuetz[j][i];
			}
		}
		newton(dArray);
	}

	public void newton(double[][] stuetzStellen) {
		final int sampleCount = stuetzStellen.length;
		// header
		AnimalTextGenerator tg = new AnimalTextGenerator(lang);
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(Font.SANS_SERIF, Font.BOLD, 24));
		Text header = new Text(tg, new Coordinates(30, 30), "Newton-Interpolation", "header",
				new TicksTiming(0), headerProps);
		tg.create(header);
		// kasten header
		Rect headerRect = drawYellowRectAround(header, "headerRect", 5);
		// start
		SourceCodeProperties scp = new SourceCodeProperties();
		SourceCode startText = lang.newSourceCode(
				new Offset(0, 20, header, AnimalScript.DIRECTION_SW), "sourceCode", null, scp);
		startText.addCodeLine(
				"Bei der Newtonschen Interpolationen ist für n Werte (x_i,y_i) aus R^2 mit paarweise verschiedenen x_i ",
				null, 0, null);
		startText.addCodeLine(
				"ein Polynom p mit mit höchstens n-ten Grad gesucht, das den Interpolationsbedingungen p(x_i)=y_i, (i=0,...,n) genuegt.",
				null, 0, null);
		startText.addCodeLine(
				"Die dazu benutzten Newton-Koeffizienten [x_0,...,x_i]p (dividierten Differenzen) werden rekursiv wie folgt berechnet:",
				null, 0, null);
		startText.addCodeLine("  ", null, 0, null);
		startText.addCodeLine(
				"[x_i]p:= p(x_i) =y_i, [x_i,...,x_i+k]p=( [x_i+4,...,x_i+k]p -[x_i,...,x_i+k-1]p ) / ( x_i+k - x_i )",
				null, 0, null);
		startText.addCodeLine("  ", null, 0, null);
		startText.addCodeLine(
				"Die gesuchten Koeffizienten für das Polynom lassen sich abschließen aus der oberen Schrägzeile ablesen.",
				null, 0, null);
		
		drawRectAround(startText, "startTextRect", 10);
		lang.nextStep("Einführung");
		ArrayList<Primitive> headerAndRect = new ArrayList<>();
		headerAndRect.add(header);
		headerAndRect.add(headerRect);
		lang.hideAllPrimitivesExcept(headerAndRect);
		lang.nextStep();

		// schritte
		scp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		SourceCode sourceCode = lang.newSourceCode(
				new Offset(0, 20, header, AnimalScript.DIRECTION_SW), "sourceCode", null, scp);
		sourceCode.addCodeLine("1. Berechne rekursiv die dividierte Differenz [x_0, ... ,x_i]",
				null, 0, null);
		sourceCode.addCodeLine(
				"2. Lese die gesuchten Koeffizienten für das Polynom aus der oberen Schrägzeile ab",
				null, 0, null);
		sourceCode.highlight(0);
		drawRectAround(sourceCode, "sourceCodeRect", 10);
		
		lang.nextStep();
		
		// beschriftung
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		// labels for the sample and calculation columns
		Text[] beschriftung = new Text[sampleCount + 1];
		
		beschriftung[0] = lang.newText(
				new Offset(-200, 30, sourceCode, AnimalScript.DIRECTION_S), "x_i", "x_i", null, tp);
		beschriftung[1] = lang.newText(
				new Offset(25, 0, beschriftung[0], AnimalScript.DIRECTION_BASELINE_START),
				"[x_i]p=y_i", "[x_i]p=y_i", null, tp);
		
		//generate the rest of the labels
		for (int i = 0; i < sampleCount - 1; ++i) {
			String currentLabel;
			if (i == 0) {
				currentLabel = String.format("[x_i, x_i+%d]p", i+1);
			} else {
				// if the difference is < 1, put in an ellipsis
				currentLabel = String.format("[x_i, ..., x_i+%d]p", i+1);
			}
			beschriftung[i+2] = lang.newText(
					new Offset(40, 0, beschriftung[i + 1], AnimalScript.DIRECTION_NE),
					currentLabel, currentLabel, null, tp);
		}


		
		// stuetzstellen
		MatrixProperties sp = new MatrixProperties();
		sp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.RED);
		DoubleMatrix stuetz = lang.newDoubleMatrix(
				new Offset(-10, 10, beschriftung[0], AnimalScript.DIRECTION_S), stuetzStellen,
				"stuetzStellen", null, sp);
		drawRectAround(stuetz, "stuetzRect", 3);
		
		
		// dividierteDifferenzen
		MatrixProperties dp = new MatrixProperties();
		dp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.RED);
		// not using standard green to improve visibility on white
		dp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, new Color(0x30C840));
		String[][] dividierteDifferenzen = new String[sampleCount][sampleCount];
		for (int i = 0; i < sampleCount; i++) {
			for (int j = 0; j < sampleCount; j++) {
				dividierteDifferenzen[i][j] = "                             ";
			}
		}
		StringMatrix dividierte = lang.newStringMatrix(
				new Offset(40, 0, beschriftung[1], AnimalScript.DIRECTION_S), dividierteDifferenzen,
				"dividierte", null, dp);
		
		AnimalRectGenerator rg = new AnimalRectGenerator(lang);
		RectProperties rectProps = new RectProperties();
		Rect calcRect = new Rect(rg, new Offset(-10, -6, beschriftung[0], AnimalScript.DIRECTION_NW),
				new Offset(100, 20, dividierte, AnimalScript.DIRECTION_SE), "labelRect", null,
				rectProps);
		rg.create(calcRect);
		
		lang.nextStep();
		
		// Hilfsmatrix
		double[][] matrix = new double[sampleCount][sampleCount];
		
		// initialize the first column with the y values for easier calculation
		for (int i = 0; i < sampleCount; ++i) {
			matrix[i][0] = stuetzStellen[i][1];
		}
		
		// loop for columns of calculation matrix 
		for (int i = 0; i < sampleCount - 1; ++i) {
			// Number of divided differences we have to calculate in this column
			int divDiffCount = sampleCount - (i+1);
			
			// loop for column entries
			for (int j = 0; j < divDiffCount; ++j) {
				// in this cell we calculate divDiff(x_min,...,x_max)
				int min = j;
				int max = j + (i+1);
				
				//current row number is always equal to max
				int rowNum = max;
				
				dividierte.highlightElem(rowNum, i, null, null);
				
				// put the general formula with x_i filled in
				String formula = String.format("%s = %s", genPString(min, max), genDivString(min, max));
				dividierte.put(rowNum, i, formula, null, null);
				
				//questions
				if( Math.random() < 0.7) {
					if(Math.random() < 0.5) {
					TrueFalseQuestionModel valueQuestion = new TrueFalseQuestionModel("value"+j +i);
						valueQuestion.setPrompt("Für die nächste Berechung werden folgende Werte benötigt: " + 
								matrix[rowNum][i]+", " + 
								matrix[rowNum-1][i]+", " + 
								stuetzStellen[max][0]+", " + 
								stuetzStellen[min][0]);
						valueQuestion.setCorrectAnswer(true);
						valueQuestion.setPointsPossible(1);
						lang.addTFQuestion(valueQuestion);
						lang.nextStep();
					}else {
						TrueFalseQuestionModel valueQuestion = new TrueFalseQuestionModel("value"+j +i);
						double a = matrix[rowNum-1][i] +2;
						double b= matrix[rowNum][i] +2;
						valueQuestion.setPrompt("Für die nächste Berechung werden folgende Werte benötigt: " + 
								a+", " + 
								b+", " + 
								stuetzStellen[max][0]+", " + 
								stuetzStellen[min][0]);
						valueQuestion.setCorrectAnswer(false);
						valueQuestion.setPointsPossible(1);
						lang.addTFQuestion(valueQuestion);
						lang.nextStep(); 
					}
				}
				
				
				// highlight important samples for this step
				lang.nextStep("Schritt 1: Berechnung von " + genPString(min, max));
				if (i==0) {
					stuetz.highlightCell(min, 1, null, null);
					stuetz.highlightCell(max, 1, null, null);
				} else {
					dividierte.highlightCell(rowNum-1, i-1, null, null);
					dividierte.highlightCell(rowNum, i-1, null, null);
				}
				lang.nextStep();
				stuetz.highlightCell(min, 0, null, null);
				stuetz.highlightCell(max, 0, null, null);
				lang.nextStep();
				
				// put the formula with the actual values filled in
				String calculation = String.format("%s = (%s - %s) / (%s - %s)",
						genPString(min, max),
						stuetzStellen[max][1],
						stuetzStellen[min][1],
						stuetzStellen[max][0],
						stuetzStellen[min][0]);
				dividierte.put(rowNum, i, calculation, null, null);
				lang.nextStep();
				
				// calculate the divided difference for this cell
				matrix[rowNum][i + 1] = Math.floor(((matrix[rowNum][i] - matrix[rowNum-1][i])
						/ (stuetzStellen[max][0] - stuetzStellen[min][0])) * 100) / 100.0;
				
				String result = String.format("%s = %s",genPString(min, max), matrix[rowNum][i + 1]);
				dividierte.put(rowNum, i, result, null, null);
				lang.nextStep();
				if (i > 0) {
					dividierte.unhighlightCell(rowNum-1, i-1, null, null);
					dividierte.unhighlightCell(rowNum, i-1, null, null);
				}
				dividierte.unhighlightElem(rowNum, i, null, null);
				stuetz.unhighlightCell(min, 1, null, null);
				stuetz.unhighlightCell(max, 1, null, null);
				stuetz.unhighlightCell(min, 0, null, null);
				stuetz.unhighlightCell(max, 0, null, null);
				lang.nextStep();
			}
		}

		// Polynom
		sourceCode.unhighlight(0);
		sourceCode.highlight(1);
		lang.nextStep("Schritt 2: Ablesen der Parameter");
		
		//Question
		MultipleChoiceQuestionModel result = new MultipleChoiceQuestionModel("result");
		result.setPrompt("Was sind die gesuchten Koeffizienten");
		result.setGroupID("nextStep");
		String rKoef="";
		String fKoef="";
		String fKoef2="";
		for (int i = 0; i < sampleCount - 1; ++i) {
			rKoef+= matrix[i][i]+ ", ";
			fKoef+= matrix[i][i]+ ", ";
			fKoef2+= matrix[i][0]+ ", ";
		}
		rKoef += matrix[sampleCount-1][sampleCount-1];
		fKoef += matrix[sampleCount-1][sampleCount-1] +1;
		fKoef2 += matrix[sampleCount-1][sampleCount-1]+2;
		result.addAnswer(rKoef, 2, "Richtig" );
		result.addAnswer(fKoef, 0, "Falsch");
		result.addAnswer(fKoef2, 0, "Falsch" );
		lang.addMCQuestion(result);
		lang.nextStep();
		
		Text polynomBeschreibung = lang.newText(new Offset(0, 20, stuetz, AnimalScript.DIRECTION_SW),
				"Die Koeffizienten für das gesuchte Polynom lassen sich nun aus der oberen Schrägzeile ablesen.",
				"polynom", null, tp);
		
		// build formula strings for the polynome, one with x_i and one with the actual values
		String polynomeFormula = "p(x) = [x_0]p ";
		String polynomeValues  = "= " + stuetzStellen[0][1] + " ";
		
		for (int i = 0; i < sampleCount - 1; ++i) {
			polynomeFormula += String.format("+ %s * ", genPString(0, i + 1));
			polynomeValues  += String.format("+ %s * ", matrix[i+1][i+1]);
			for (int j = 0; j <= i; ++j) {
				polynomeFormula += String.format("(x - x_%d) ", j);
				polynomeValues += String.format("(x - %s) ", stuetzStellen[j][0]);
			}

		}
		
		Text polynom0 = lang.newText(
				new Offset(30, 10, polynomBeschreibung, AnimalScript.DIRECTION_SW),
				polynomeFormula, "polynom0", null, tp);
		lang.nextStep();

		// highlight parameter cells
		stuetz.highlightCell(0, 1, null, null);
		for (int i = 0; i < sampleCount - 1; ++i) {
			dividierte.highlightCell(i + 1, i, null, null);
		}
		Text polynom1 = lang.newText(new Offset(30, 10, polynom0, AnimalScript.DIRECTION_SW),
				polynomeValues, "polynom1", null, tp);
		drawRectAround(polynom1, "resultRect", 3);
		lang.nextStep("Resultat");

		lang.hideAllPrimitivesExcept(headerAndRect);
		SourceCode endText = lang.newSourceCode(
				new Offset(0, 20, header, AnimalScript.DIRECTION_SW), "sourceCode", null, scp);
		endText.addCodeLine("Fazit:", null, 0, null);
		endText.addCodeLine(" ", null, 0, null);
		endText.addCodeLine(" - Mithilfe der Newton-Interpolation haben wir ein Polynom", null, 0,
				null);
		endText.addCodeLine(" ", null, 0, null);
		endText.addCodeLine("      p(x) " + polynomeValues, null, 0, null);
		endText.addCodeLine(" ", null, 0, null);
		endText.addCodeLine("   gefunden, welches alle gegeben Stützstellen ", null, 0, null);
		endText.addCodeLine(" ", null, 0, null);

		String stuetzText = "";
		for (int i = 0; i < stuetzStellen.length; ++i) {
			stuetzText += String.format("(%f, %f)", stuetzStellen[i][0], stuetzStellen[i][1]);
			if (i < stuetzStellen.length -1) stuetzText += ", ";
		}
		endText.addCodeLine("      {(x_i,y_i)} = {" + stuetzText + "}", null, 0, null);
		endText.addCodeLine(" ", null, 0, null);

		endText.addCodeLine("   durchläuft und somit die Anforderungen erfüllt.  ", null, 0, null);
		endText.addCodeLine(" ", null, 0, null);
		endText.addCodeLine(" ", null, 0, null);
		endText.addCodeLine("Komplexität der Newton-Interpolation:", null, 0, null);
		endText.addCodeLine(
				" - Der Berechnungaufwand für die Koeffizienten des Polynoms liegt in O(n^2)", null,
				0, null);
		endText.addCodeLine(
				" - Mithilfe des Horner-Schemas laesst ein Funktionswert des gefundenen Polynoms in O(n) auswerten", null,
				0, null);
		endText.addCodeLine(" ", null, 0, null);
		endText.addCodeLine(" ", null, 0, null);
		endText.addCodeLine("Vorteile der Newton-Interpolation:", null, 0, null);
		endText.addCodeLine(
				"- Bei Nachtraeglichem hinzufuegen von Interpolationsbedingungen kann [x_0, ... , x_(n+1)]p aus einer weiteren Zeile ",
				null, 0, null);
		endText.addCodeLine("der dividierte Differenzen abgelesen werden", null, 0, null);
		endText.addCodeLine("- Sie ist numerisch leicht auszuwerten", null, 0, null);

		drawRectAround(endText, "endTextRect", 10);
		lang.nextStep("Fazit");
	} // END newton()
	
	/**
	 * Generates a (shortened) sequence of indexed x values, e.g. "[x1, ... x4]p"
	 * @param ind1 first index
	 * @param ind2 second index
	 * @return the string "[x_ind1, ..., x_ind2]p" if ind2-ind1 == 1, the ellipsis is ommited
	 * 			and if ind1==ind2, "[x_ind1]p" is returned
	 */
	private String genPString(int ind1, int ind2) {
		if (ind1 == ind2) { 
			return String.format("[x_%d]p", ind1);
		}	else if (ind2 - ind1 == 1 || ind1 - ind2 == 1) {
			return String.format("[x_%d, x_%d]p", ind1, ind2);
		} else {
			return String.format("[x_%d, ..., x_%d]p", ind1, ind2);
		}
	}
	
	/**
	 * Generates the formula for the divided difference of x_ind1 and x_ind2
	 * @param ind1 first index
	 * @param ind2 second indes
	 * @return the generated formula
	 */
	private String genDivString(int ind1, int ind2) {
			return String.format("( %s - %s ) / (x_%d - x_%d)",
					genPString(ind1 + 1, ind2),
					genPString(ind1, ind2 - 1),
					ind2, ind1);
	}


	/**
	 * Draws a rectangle around basePrimitive
	 * 
	 * @param basePrimitive
	 *            the element around which a rectangle should be drawn
	 * @return the created {@link Rect} object
	 */
	@SuppressWarnings("unused")
	private algoanim.primitives.Rect drawRectAround(algoanim.primitives.Primitive basePrimitive, String name) {
		AnimalRectGenerator rg = new AnimalRectGenerator(lang);
		RectProperties rectProps = new RectProperties();
		Rect rect = new Rect(rg, new Offset(0, 0, basePrimitive, AnimalScript.DIRECTION_NW),
				new Offset(0, 0, basePrimitive, AnimalScript.DIRECTION_SE), name, null,
				rectProps);
		rg.create(rect);
		return rect;
	}

	/**
	 * Draws a rectangle around basePrimitive
	 * 
	 * @param basePrimitive
	 *            the element around which a rectangle should be drawn
	 * @param margin
	 *            how much space should be between the object and the rectangle
	 * @return the created {@link Rect} object
	 */
	private algoanim.primitives.Rect drawRectAround(algoanim.primitives.Primitive basePrimitive, String name,
			int margin) {
		AnimalRectGenerator rg = new AnimalRectGenerator(lang);
		RectProperties rectProps = new RectProperties();
		Rect rect = new Rect(rg,
				new Offset(-margin, -margin, basePrimitive, AnimalScript.DIRECTION_NW),
				new Offset(margin, margin, basePrimitive, AnimalScript.DIRECTION_SE), name,
				null, rectProps);
		rg.create(rect);
		return rect;
	}
	
	/**
	 * Draws a rectangle around basePrimitive with color
	 * 
	 * @param basePrimitive
	 *            the element around which a rectangle should be drawn
	 * @param margin
	 *            how much space should be between the object and the rectangle
	 * @return the created {@link Rect} object
	 */
	private algoanim.primitives.Rect drawYellowRectAround(algoanim.primitives.Primitive basePrimitive, String name,
			int margin) {
		AnimalRectGenerator rg = new AnimalRectGenerator(lang);
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, -1);
		Rect rect = new Rect(rg,
				new Offset(-margin, -margin, basePrimitive, AnimalScript.DIRECTION_NW),
				new Offset(margin, margin, basePrimitive, AnimalScript.DIRECTION_SE), name,
				null, rectProps);
		rg.create(rect);
		return rect;
	}

	public String getName() {
		return "Newtonsche Interpolationsformel mit Schema der dividierten Differenzen";
	}

	public String getAlgorithmName() {
		return "Newtonsche Interpolationsformel mit Schema der dividierten Differenzen";
	}

	public String getAnimationAuthor() {
		return "Lennard Ruedesheim, Tim Steinke";
	}

	public String getDescription() {
		return "Bei der Newtonschen Interpolationen ist für n Werte (x_i,y_i) aus R^2 mit paarweise verschiedenen x_i "
				+ "\n"
				+ "ein Polynom p mit mit höchstens n-ten Grad gesucht, das den Interpolationsbedingungen p(x_i)=y_i, (i=0,...,n) "
				+ "\n" + "genügt. " + "\n"
				+ "Die dazu benutzten Newton-Koeffizienten [x_0,...,x_i]p (dividierten Differenzen) werden rekursiv berechnet. "
				+ "\n"
				+ "[x_i]p:= p(x_i) =y_i, [x_i,...,x_i+k]p=( [x_i+4,...,x_i+k]p -[x_i,...,x_i+k-1]p ) / ( x_i+k - x_i )";
	}

	public String getCodeExample() {
		return "1. Berechne rekursiv die dividierte Differenz [x_0, ... ,x_i] " + "\n"
				+ "2. Lese die gesuchten Koeffizienten fuer das Polynom aus der oberen Schrägzeile ab";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
			throws IllegalArgumentException {
		String [][] stuetzStellenString = (String[][]) arg1.get("stuetzstellen");
		//minimum Stuetzstellen
		if(stuetzStellenString.length < 1) throw new IllegalArgumentException("Mindestens eine Stuetzstelle benötigt.");
		//(x,y) Tupel?
		if(stuetzStellenString[0].length != 2) throw new IllegalArgumentException("Spaltenanzahl muss genau 2 sein, Zeilen sind x-y-Tupel.");
		double[][] stuetzStellen = new double[stuetzStellenString.length][stuetzStellenString[0].length];
		for (int i = 0; i < stuetzStellenString[0].length; ++i) {
			for (int j = 0; j < stuetzStellenString.length; ++j) {
				stuetzStellen[j][i] = Double.parseDouble(stuetzStellenString[j][i]);
			}
        }
		//paarweise verschieden?
		for (int i=0; i < stuetzStellen.length-1; i++) {
			for (int k=i+1; k < stuetzStellen.length; k++) {
				if (stuetzStellen[i][0] == stuetzStellen[k][0]) {
					throw new IllegalArgumentException("x-Werte der Stuetzstellen sind nicht paarweise verschieden.");
				}
			}
		}
		return true;
	}

}
