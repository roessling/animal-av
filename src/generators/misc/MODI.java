/*
 * MODI.java
 * Florian Klotzsch, Neda Mesbah, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;

import javax.print.attribute.SupportedValuesAttribute;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.variables.Variable;
import animal.variables.VariableRoles;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

;

public class MODI implements Generator {
	private Language lang;
	private TextProperties untertitelEigenschaften;
	private TextProperties titelEigenschaften;
	private int[][] kosten;
	private TextProperties infoFensterEigenschaften;
	private int[] angebotsmengen;
	private MatrixProperties matrixEigenschaften;
	private int[][] basisvariablen;
	private SourceCodeProperties textEigenschaften;
	private int[] nachfragemengen;
	private boolean sindFragenErlaubt;

	private Text title;
	private Text costMatrixTitle;
	private Text transportGraphTitle;
	private Text algoStepsTitle;
	private Text basicVartitle;
	private Text linEqTitle;
	private TextProperties subtitleProps;
	private SourceCode description;
	private SourceCode pseudoCode;
	private SourceCode inputErrorDesc;
	private SourceCodeProperties inputErrorDescProps;
	private SourceCode algoStepsDesc;
	private SourceCodeProperties scProps;
	private StringMatrix animalCostMatrix;
	private MatrixProperties maProps;
	private StringMatrix animalBasicVariablesMatrix;
	private Graph transportGraph;
	private GraphProperties transportGraphProps;
	private int[][] adjacencyMatrix;
	private String[] nodeDescriptions;
	private Node[] nodePositions;
	private MatrixProperties linSysProps;
	private StringMatrix linSys;
	private Text infoWindow;
	private TextProperties infoWindowProps;
	private SourceCode graphDesc;
	private int[] supply;
	private int[] demand;
	private int[][] costMatrix;
	private int[][] basicVariablesMatrix;
	private Point[] basicVariables;
	private Text outlookTitle;
	private SourceCode outlook;

	private int[] dualVariablesSupplier;
	private int[] dualVariablesDemander;
	private int numberOfBasicVariables;
	private boolean[][] isPositionBasicVariable;
	private Point[] kreis;
	private LinkedList<LinkedList<Point>> blockiert;
	private Point[] cycleVariables;
	private int numberCycleElem;
	private int count;
	private int tGraphCount;
	private Variables v;
	private Color highlightColor;
	private Font font;
	private Color elemColor;

	public void init() {
		lang = new AnimalScript("MODI Methode [DE]",
				"Florian Klotzsch, Neda Mesbah", 800, 600);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		untertitelEigenschaften = (TextProperties) props
				.getPropertiesByName("untertitelEigenschaften");
		titelEigenschaften = (TextProperties) props
				.getPropertiesByName("titelEigenschaften");
		kosten = (int[][]) primitives.get("kosten");
		infoFensterEigenschaften = (TextProperties) props
				.getPropertiesByName("infoFensterEigenschaften");
		angebotsmengen = (int[]) primitives.get("angebotsmengen");
		matrixEigenschaften = (MatrixProperties) props
				.getPropertiesByName("matrixEigenschaften");
		basisvariablen = (int[][]) primitives.get("basisvariablen");
		textEigenschaften = (SourceCodeProperties) props
				.getPropertiesByName("textEigenschaften");
		nachfragemengen = (int[]) primitives.get("nachfragemengen");
		sindFragenErlaubt = (boolean) primitives.get("sindFragenErlaubt");
		highlightColor = (Color) props.get("matrixEigenschaften",
				"cellHighlight");
		font = (Font) props.get("textEigenschaften", "font");
		elemColor = (Color) props.get("textEigenschaften", "color");

		int[] a = new int[angebotsmengen.length];
		int[] n = new int[nachfragemengen.length];
		int[][] k = new int[kosten.length][kosten[0].length];
		int[][] b = new int[basisvariablen.length][basisvariablen[0].length];

		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[0].length; j++) {
				b[i][j] = basisvariablen[i][j];
			}
		}

		for (int i = 0; i < a.length; i++) {
			a[i] = angebotsmengen[i];
		}

		for (int i = 0; i < n.length; i++) {
			n[i] = nachfragemengen[i];
		}

		for (int i = 0; i < k.length; i++) {
			for (int j = 0; j < k[0].length; j++) {
				k[i][j] = kosten[i][j];
			}
		}
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		modi(a, n, k, b);

		lang.finalizeGeneration();

		return lang.toString();
	}

	public String getName() {
		return "MODI Methode [DE]";
	}

	public String getAlgorithmName() {
		return "MODI-Method";
	}

	public String getAnimationAuthor() {
		return "Florian Klotzsch, Neda Mesbah";
	}

	public String getDescription() {
		return "MODI Methode"
				+ "\n"
				+ "\n"
				+ "Die MODI Methode (Modifizierte Distributionsmethode) ist ein Verfahren aus dem Gebiet"
				+ "\n"
				+ "des Operations Research, mit dem ein Standard-Transportproblem optimal gelöst werden"
				+ "\n"
				+ "kann."
				+ "\n"
				+ "Beim Transportproblem werden bestimmte Mengen eines homogenen Gutes von mehreren "
				+ "\n"
				+ "Angebotsorten zu mehreren Nachfrageorten transportiert, wobei die Tranportkosten pro"
				+ "\n"
				+ "Mengeneinheit zwischen den Standorten bekannt sind. Ebenfalls bekannt sind die"
				+ "\n"
				+ "Angebots- und Nachfragemengen. Gesucht ist ein kostenminimaler Transportplan, bei"
				+ "\n"
				+ "dem alle Bedarfe befriedigt und alle Angebote ausgeschöpft werden."
				+ "\n"
				+ "\n"
				+ "Voraussetzungen:"
				+ "\n"
				+ "1. Es muss eine zulässige Basislösung vorliegen, die mit einem Eröffnungsverfahren"
				+ "\n"
				+ "    (wie der Vogelschen Approximationsmethode oder der Spaltenminimummethode)"
				+ "\n"
				+ "    ermittelt werden kann."
				+ "\n"
				+ "2. Die Angebotsmenge muss der Nachfragemenge entsprechen."
				+ "\n"
				+ "3. Das Angebot muss vollständig aufgebraucht und die Nachfrage vollständig erfüllt "
				+ "\n"
				+ "    werden. D.h. die Transportmengen müssen den Angebots- und Nachfragemengen"
				+ "\n"
				+ "    entsprechen."
				+ "\n"
				+ "4. Alle Angebots-, Nachfrage- und Transportmengen sowie die Transportkosten müssen"
				+ "\n"
				+ "    größer oder gleich null sein."
				+ "\n"
				+ "\n"
				+ "Für einen Anbieter A_i stellt die Variable x_ij die transportierte Menge zu dem Nachfrager"
				+ "\n"
				+ "N_j  dar. Die Angebots- und Nachfragemengen werden mit a_i, bzw. n_j bezeichnet. Die"
				+ "\n"
				+ "entsprechenden Kosten für eine transportierte Einheit werden mit c_ij beschrieben."
				+ "\n"
				+ "Gesucht ist damit ein Transportplan, bei dem die Gesamtkosten für die Summe aus dem"
				+ "\n"
				+ "Produkt zwischen x_ij und c_ij für alle Anbieter und Nachfrager minimal sind"
				+ "\n"
				+ "(min f = ∑ cij * xij für alle i = 1,…,m und j = 1,…,n ). Das Transportproblem liegt somit in "
				+ "\n"
				+ "der Form eines linearen Optimierungs-Problems vor."
				+ "\n"
				+ "Die Modi Methode macht sich zu Nutze, dass sich jedes lineare Problem in ein duales"
				+ "\n"
				+ "Problem transformieren lässt. Hierbei wird das Minimierungsverfahren zu einem"
				+ "\n"
				+ "Maximierungsverfahren (max z = ∑ a_i * u_i + ∑ n_j * u_j). Aus den hierbei eingeführten "
				+ "\n"
				+ "Dualvariablen u_i und v_j bilden sich auch die Nebenbedingungen, dass die Summe aus "
				+ "\n"
				+ "diesen kleiner oder gleich den entsprechenden Transportkosten c_ij sind. Diese "
				+ "\n"
				+ "Nebenbedingungen werden für jede Basisvariable aufgestellt und das damit erhaltene "
				+ "\n"
				+ "Gleichungssystem aufgelöst. Anschließend werden mit den Dualvariablen die reduzierten"
				+ "\n"
				+ "Kosten r_ij berechnet, die aussagen, um wie viel sich die Gesamtkosten reduzieren lassen "
				+ "\n"
				+ "würden, wenn die transportierten Mengen umverteilt werden, so dass eine Einheit von A_i "
				+ "\n"
				+ "nach N_i transportiert wird (Ein negativer Wert sagt dabei aus, dass sich die Kosten um "
				+ "\n"
				+ "den entsprechenden Wert reduzieren lassen). "
				+ "\n"
				+ "Sind alle reduzierten Kosten größer oder gleich null, wurde ein kostenminimaler "
				+ "\n"
				+ "Transportplan gefunden. Ist dies nicht der Fall, wird die Nichtbasisvariable x_ij mit den "
				+ "\n"
				+ "geringsten reduzierten Kosten (größter negativer Wert) in die Basis aufgenommen "
				+ "\n"
				+ "(als neue Basisvariable). "
				+ "\n"
				+ "Dadurch entsteht im zum Transportproblem zugehörigen Transportgraph ein Kreis. "
				+ "\n"
				+ "Ausgehend von der neuen Basisvariable wird nun, von jeder Basisvariable entlang des "
				+ "\n"
				+ "Kreises, abwechselnd der Wert d abgezogen und auf die nächste Basisvariable addiert. "
				+ "\n"
				+ "Der Wert d ist dabei der kleinste Wert einer Basisvariable, bei der d subtrahiert werden soll. "
				+ "\n"
				+ "Anschließend wird diese Variable aus der Basis entfernt. Die neue Basisvariable erhält den "
				+ "\n"
				+ "Wert d. Mit diesem Schritt wird die maximal zulässige Transportmenge entlang des Kreises"
				+ "\n"
				+ " so umverteilt, dass die Gesamtkosten reduziert werden. "
				+ "\n"
				+ "Dieses Vorgehen wird so lange wiederholt bis alle berechneten reduzierten Kosten größer"
				+ "\n"
				+ "oder gleich 0 sind und somit der kostenminimale Transportplan gefunden wurde."
				+ "\n"
				+ "        ⇒ Für vertiefende Informationen der einzelnen Themen siehe auch die Links unten."
				+ "\n"
				+ "\n"
				+ "Hinweis: Nichtbasisvariablen werden beim Input über die Basisvariablen als negative Werte"
				+ "         angegeben!"
				+ "\n"
				+ "\n"
				+ "Modi Methode: https://de.wikipedia.org/wiki/MODI-Methode"
				+ "\n"
				+ "Transportproblem: https://de.wikipedia.org/wiki/Transportproblem"
				+ "\n"
				+ "Lineare Optimierung: https://de.wikipedia.org/wiki/Lineare_Optimierung#Dualit.C3.A4t"
				+ "\n"
				+ "Dualität: https://de.wikipedia.org/wiki/Dualit%C3%A4t_%28Mathematik%29";
	}

	public String getCodeExample() {
		return "Schritte des Verfahrens"
				+ "\n"
				+ "0. Prüfe, ob es sich bei den angegebenen Werten um eine zulässige Basislösung handelt"
				+ "\n"
				+ "   (Überprüfen der Voraussetzungen). "
				+ "\n"
				+ "Falls nein, keine Lösung. Falls ja, Schritt I. "
				+ "\n"
				+ "I. Bilde und löse lineares Gleichungssystem u_i + v_j = c_ij mit allen i,j, deren x_ij"
				+ "\n"
				+ "   Basisvariable ist. Das lineare Gleichungssystem enthält m+n Variablen u_i und v_j"
				+ "\n"
				+ "  (bei m Angebotsmengen und n Nachfragemengen). Da es m + n - 1 Gleichungen gibt,"
				+ "\n"
				+ "   hat das LGS einen Freiheitsgrad. Daher kann man eine Variable u_i oder v_j = 0 setzen "
				+ "\n"
				+ "   Die Auflösung wird vereinfacht, wenn man diejenige Variable wählt, die am häufigsten"
				+ "\n"
				+ "   im linearen Gleichungssystem vorkommt). "
				+ "\n"
				+ "II. Berechne reduzierte Kosten r_ij = c_ij - u_i - v_j für alle Nichtbasisvariablen. "
				+ "\n"
				+ "III. Sind alle r_ij >= 0 wurde die optimale Lösung schon gefunden --> Stop. Sonst fahre"
				+ "\n"
				+ "     mit Schritt IV fort. "
				+ "\n"
				+ "IV. Basistausch: Aufnahme der NBV x_pq mit den kleinsten reduzierten Kosten in die Basis."
				+ "\n"
				+ "     Dadurch entsteht genau ein Kreis im Transportgraph. "
				+ "\n"
				+ "Die maximal zulässige Transportmenge wird entlang des Kreises umverteilt. "
				+ "\n"
				+ "V. Beginne mit der neuen zulässigen Basislösung bei Schritt I."
				+ "\n";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	// Methode zum Initialiseren des Inputs und
	// Steuern des Ablaufs
	public void modi(int[] supplier, int[] demander, int[][] costs,
			int[][] basicVar) {

		// Titel in Animal
		TextProperties titleProps = titelEigenschaften;
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 16));
		title = lang.newText(new Coordinates(10, 10), "Modi-Methode", "title",
				null, titleProps);

		initializeAnimalProperties();
		
		inputErrorDesc = lang.newSourceCode(new Offset(0, 10, title,
				AnimalScript.DIRECTION_SW), "errorMessage", null, scProps);
		
		count = 0;
		tGraphCount = 0;
		// Initialisiere die Eingabeparameter
		supply = supplier;
		demand = demander;
		costMatrix = costs;
		basicVariablesMatrix = basicVar;
		
		//Führe Code nur aus wenn richtige Eingabeparameter übergeben werden
		if(supply != null && supply.length > 0 && demand != null && demand.length > 0 && costMatrix != null && costMatrix.length > 0 && basicVariablesMatrix != null && basicVariablesMatrix.length > 0 && supply.length == costMatrix.length && supply.length == basicVariablesMatrix.length && demand.length == costMatrix[0].length && demand.length == basicVariablesMatrix[0].length){
		
		dualVariablesSupplier = new int[supply.length];
		dualVariablesDemander = new int[demand.length];

		// Zähle übergebene Basisvariablen
		numberOfBasicVariables = supply.length + demand.length - 1;
		String basVar = "{";
		int l = 0;
		for (int i = 0; i < basicVar.length; i++) {
			for (int j = 0; j < basicVar[i].length; j++) {
				if (basicVar[i][j] >= 0) {
					l++;
				}
			}
		}
		
		//Positionen der Basisvariablen in Liste abspeichern
		basicVariables = new Point[l];
		int k = 0;
		for (int i = 0; i < basicVar.length; i++) {
			for (int j = 0; j < basicVar[i].length; j++) {
				if (basicVar[i][j] >= 0) {
					basicVariables[k] = new Point(i, j);
					if (k < supply.length + demand.length - 2) {
						basVar += "x_" + (i + 1) + (j + 1) + ", ";
					} else {
						basVar += "x_" + (i + 1) + (j + 1) + "}";
					}
					k++;
				}
			}
		}
		
		// Nichtbasisvariablen den Wert 0 zuweisen
		for (int i = 0; i < basicVariablesMatrix.length; i++) {
			for (int j = 0; j < basicVariablesMatrix[i].length; j++) {
				if(basicVariablesMatrix[i][j] < 0) {
					basicVariablesMatrix[i][j] = 0;
				}
			}
		}

		isPositionBasicVariable = new boolean[supply.length][demand.length];

		// NBV als false markieren
		for (int i = 0; i < isPositionBasicVariable.length; i++) {
			for (int j = 0; j < isPositionBasicVariable[i].length; j++) {
				isPositionBasicVariable[i][j] = false;
			}
		}
		// NBV als true markieren
		for (Point p : basicVariables) {
			isPositionBasicVariable[p.x][p.y] = true;
		}
		//Wenn Inputwerte stimmen, werden Schritte des Algo ausgeführt
		if (isInputCorrect()) {

			//Beschreibungstext
			setDescription();

			//Baisvariablen als Variable im Variablenfenster
			v = lang.newVariables();
			v.declare("String", "Basisvariablen", basVar);

			// Initilaisieren aller Primitive in Animal
			// Tabellen, Graph, usw.
			initializeAlgoAnimal();
			// Counter auf Basisvariablenmatrix setzen
			TwoValueCounter counter = lang
					.newCounter(animalBasicVariablesMatrix);
			CounterProperties cp = new CounterProperties();
			cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);

			TwoValueView view = lang.newCounterView(counter, new Coordinates(
					830, 20), cp, true, true);

			//Berechnen der Dualvariablenwerte durch Auflösen des LGS
			computeDualVariables();
			//Berechnung der reduzierten Kosten
			computeReducedCosts();
			algoStepsDesc.highlight(4, 0, true, null, null);
			for (int i = 0; i < isPositionBasicVariable.length; i++) {
				for (int j = 0; j < isPositionBasicVariable[i].length; j++) {
					if (!isPositionBasicVariable[i][j]) {
						animalBasicVariablesMatrix.highlightCell(i + 1, j + 1,
								null, null);
					}
				}
			}
			// So lange negative reduzierte Kosten existieren
			// werden die Schritte des Algo wiederholt
			while (existsNegativeBasicVariable()) {
				algoStepsDesc.highlight(6, 0, true, null, null);
				infoWindow.setText("Es existieren negative reduzierte Kosten.",
						null, null);

				if (count == 0 && sindFragenErlaubt) {
					// Zweiter Fragenblock
					Random rand = new Random();
					int randomInt = rand.nextInt(3);
					String question;
					boolean correctAnswer;
					String feedbackForTrue;
					String feedbackForFalse;
					switch (randomInt) {
					case 0:
						question = "Betrachte die Tabelle Basisvariablen/Reduzierte Kosten. Wurde die optimale Lösung bereits gefunden?";
						correctAnswer = false;
						feedbackForTrue = "Die Antwort ist leider falsch. Es gibt reduzierte Kosten, die kleiner als 0 sind.";
						feedbackForFalse = "Korrekte Antwort. Es gibt reduzierte Kosten, die kleiner als 0 sind.";
						break;
					case 1:
						Point p = basicVariables[0];
						question = "Nach dem momentanen Transportplan werden "
								+ basicVariablesMatrix[p.x][p.y]
								+ " Einheiten von Anbieter " + (p.x + 1)
								+ " zu Nachfrager " + (p.y + 1)
								+ " transportiert.";
						correctAnswer = true;
						feedbackForTrue = "Korrekte Antwort. Siehe Tabelle Basisvariablen/Reduzierte Kosten.";
						feedbackForFalse = "Die Antwort ist leider falsch. Siehe Tabelle Basisvariablen/Reduzierte Kosten.";
						break;
					case 2:
						question = "Im nächsten Schritt findet ein Basistausch statt. Wahr oder Falsch";
						correctAnswer = true;
						feedbackForTrue = "Korrekte Antwort. Es gibt negative reduzierte Kosten, deshalb findet im nächsten Schritt ein Basistausch statt, um die Gesamtkosten weiter zu optimieren.";
						feedbackForFalse = "Die Antwort ist leider falsch. Es gibt negative reduzierte Kosten, deshalb findet im nächsten Schritt ein Basistausch statt, um die Gesamtkosten weiter zu optimieren.";
						break;
					default:
						question = "Betrachte die Tabelle Basisvariablen/Reduzierte Kosten. Wurde die optimale Lösung bereits gefunden?";
						correctAnswer = false;
						feedbackForTrue = "Die Antwort ist leider falsch. Es gibt reduzierte Kosten, die kleiner als 0 sind.";
						feedbackForFalse = "Korrekte Antwort. Es gibt reduzierte Kosten, die kleiner als 0 sind.";
						break;
					}
					TrueFalseQuestionModel redCostQ = new TrueFalseQuestionModel(
							"redCostQ");
					redCostQ.setPrompt(question);
					redCostQ.setCorrectAnswer(correctAnswer);
					redCostQ.setPointsPossible(5);
					redCostQ.setFeedbackForAnswer(true, feedbackForTrue);
					redCostQ.setFeedbackForAnswer(false, feedbackForFalse);
					lang.addTFQuestion(redCostQ);
				}
				lang.nextStep();
				// Austausch der Basisvariablen(Umverteilung entlang des Kreises)
				basicVariableExchange();
				count++;
				// Fange Algo wieder von vorne an
				computeDualVariables();
				computeReducedCosts();

			}
			algoStepsDesc.highlight(5, 0, true, null, null);
			infoWindow
					.setText(
							"Alle reduzierten Kosten größer oder gleich 0 --> optimale Lösung gefunden!",
							null, null);
			lang.nextStep();

			// Anzeigen der Lösung
			String solution = "Optimale Lösung: Gesamtkosten =  ";
			for (int i = 0; i < basicVariables.length; i++) {
				if (i == basicVariables.length - 1) {
					solution += "x_" + (basicVariables[i].x + 1)
							+ (basicVariables[i].y + 1) + " * " + "c_"
							+ (basicVariables[i].x + 1)
							+ (basicVariables[i].y + 1) + " = ";
				} else {
					solution += "x_" + (basicVariables[i].x + 1)
							+ (basicVariables[i].y + 1) + " * " + "c_"
							+ (basicVariables[i].x + 1)
							+ (basicVariables[i].y + 1) + " + ";
				}
			}
			int fValue = 0;
			for (int i = 0; i < basicVariables.length; i++) {
				fValue += basicVariablesMatrix[basicVariables[i].x][basicVariables[i].y]
						* this.costMatrix[basicVariables[i].x][basicVariables[i].y];
				if (i != 0) {
					solution += " + ";
				}
				solution += basicVariablesMatrix[basicVariables[i].x][basicVariables[i].y]
						+ " * "
						+ this.costMatrix[basicVariables[i].x][basicVariables[i].y];
			}
			solution += " = " + fValue;
			v.declare("int", "Gesamtkosten", "" + fValue);
			infoWindow.setText(solution, null, null);

			animalBasicVariablesMatrix.put(supply.length + 1,
					demand.length + 1, String.valueOf(fValue), null, null);
			animalBasicVariablesMatrix.highlightCell(supply.length + 1,
					demand.length + 1, null, null);
			lang.nextStep("Ergebnisse");
			animalBasicVariablesMatrix.hide();
			animalCostMatrix.hide();
			costMatrixTitle.hide();
			transportGraphTitle.hide();
			algoStepsTitle.hide();
			basicVartitle.hide();
			linEqTitle.hide();
			algoStepsDesc.hide();
			transportGraph.hide();
			linSys.hide();
			infoWindow.hide();
			graphDesc.hide();
			// Angebotstabelle für den Ausblick
			Text supplyTitle;
			supplyTitle = lang.newText(new Offset(0, 20, title,
					AnimalScript.DIRECTION_SW), "Angebotsmenge", "supplyTitle",
					null, subtitleProps);
			String[][] supplyMatrix = new String[2][supply.length + 1];
			int supplySum = 0;
			for (int i = 0; i < supply.length; i++) {
				supplyMatrix[0][i] = "A" + (i + 1);
				supplyMatrix[1][i] = "" + supply[i];
				supplySum += supply[i];
			}
			supplyMatrix[0][supply.length] = "∑";
			supplyMatrix[1][supply.length] = "" + supplySum;
			StringMatrix supplyMatrixAni;
			supplyMatrixAni = lang.newStringMatrix(new Offset(0, 10,
					supplyTitle, AnimalScript.DIRECTION_SW), supplyMatrix,
					"supplyMatrix", null, maProps);
			// Nachfragetabelle für den Ausblick
			Text demandTitle;
			demandTitle = lang.newText(new Offset(0, 20, supplyMatrixAni,
					AnimalScript.DIRECTION_SW), "Nachfragemenge",
					"demandTitle", null, subtitleProps);
			String[][] demandMatrix = new String[2][demand.length + 1];
			int demandSum = 0;
			for (int i = 0; i < demand.length; i++) {
				demandMatrix[0][i] = "N" + (i + 1);
				demandMatrix[1][i] = "" + demand[i];
				demandSum += demand[i];
			}
			demandMatrix[0][demand.length] = "∑";
			demandMatrix[1][demand.length] = "" + demandSum;
			StringMatrix demandMatrixAni;
			demandMatrixAni = lang.newStringMatrix(new Offset(0, 10,
					demandTitle, AnimalScript.DIRECTION_SW), demandMatrix,
					"demandMatrix", null, maProps);
			// Tabelle mit transportierten Mengen für Ausblick
			Text transValTitle;
			transValTitle = lang.newText(new Offset(0, 20, demandMatrixAni,
					AnimalScript.DIRECTION_SW), "Transportierte Menge",
					"transValTitle", null, subtitleProps);
			String[][] transValMatrix = new String[supply.length + 2][demand.length + 2];
			for (int i = 1; i < transValMatrix[0].length - 1; i++) {
				transValMatrix[0][i] = "N" + i;
			}
			for (int i = 1; i < transValMatrix.length - 1; i++) {
				transValMatrix[i][0] = "A" + i;
			}

			for (int i = 0; i < isPositionBasicVariable.length; i++) {
				for (int j = 0; j < isPositionBasicVariable[i].length; j++) {
					if (isPositionBasicVariable[i][j] == true) {
						transValMatrix[i + 1][j + 1] = String
								.valueOf(basicVariablesMatrix[i][j]);
					} else {
						transValMatrix[i + 1][j + 1] = "0";
					}
				}
			}

			transValMatrix[0][0] = "";
			transValMatrix[0][transValMatrix[0].length - 1] = "∑";
			transValMatrix[transValMatrix.length - 1][0] = "∑";
			for (int i = 0; i < supply.length; i++) {
				transValMatrix[i + 1][transValMatrix[i].length - 1] = ""
						+ supply[i];
			}
			for (int i = 0; i < demand.length; i++) {
				transValMatrix[transValMatrix.length - 1][i + 1] = ""
						+ demand[i];
			}
			transValMatrix[transValMatrix.length - 1][transValMatrix[0].length - 1] = ""
					+ supplySum;

			StringMatrix transValMatrixAni;
			transValMatrixAni = lang.newStringMatrix(new Offset(0, 10,
					transValTitle, AnimalScript.DIRECTION_SW), transValMatrix,
					"transValMatrix", null, maProps);

			for (int i = 0; i < supply.length; i++) {
				supplyMatrixAni.highlightCell(1, i, null, null);
				transValMatrixAni.highlightCell(i + 1,
						transValMatrix[i].length - 1, null, null);
			}

			Text descText;
			descText = lang.newText(new Offset(0, 20, transValMatrixAni,
					AnimalScript.DIRECTION_SW),
					"Die angebotene Menge wird vollständig aufgebraucht!",
					"descText", null, infoWindowProps);

			lang.nextStep();
			for (int i = 0; i < supply.length; i++) {
				supplyMatrixAni.unhighlightCell(1, i, null, null);
				transValMatrixAni.unhighlightCell(i + 1,
						transValMatrix[i].length - 1, null, null);
			}
			for (int i = 0; i < demand.length; i++) {
				demandMatrixAni.highlightCell(1, i, null, null);
				transValMatrixAni.highlightCell(transValMatrix.length - 1,
						i + 1, null, null);
			}
			descText.setText(
					"Die nachgefragte Menge wird vollständig bedient!", null,
					null);
			lang.nextStep();
			for (int i = 0; i < demand.length; i++) {
				demandMatrixAni.unhighlightCell(1, i, null, null);
				transValMatrixAni.unhighlightCell(transValMatrix.length - 1,
						i + 1, null, null);
			}
			supplyMatrixAni.highlightCell(1, supply.length, null, null);
			demandMatrixAni.highlightCell(1, demand.length, null, null);
			transValMatrixAni.highlightCell(transValMatrix.length - 1,
					transValMatrix[0].length - 1, null, null);
			descText.setText(
					"-> Die transportierte Menge entspricht Angebot und Nachfrage!",
					null, null);
			lang.nextStep("Ausblick");
			animalBasicVariablesMatrix.hide();
			animalCostMatrix.hide();
			costMatrixTitle.hide();
			transportGraphTitle.hide();
			algoStepsTitle.hide();
			basicVartitle.hide();
			linEqTitle.hide();
			algoStepsDesc.hide();
			transportGraph.hide();
			linSys.hide();
			infoWindow.hide();
			graphDesc.hide();
			supplyTitle.hide();
			supplyMatrixAni.hide();
			demandTitle.hide();
			demandMatrixAni.hide();
			transValTitle.hide();
			transValMatrixAni.hide();
			descText.hide();

			// Ausblick
			outlookTitle = lang.newText(new Offset(0, 10, title,
					AnimalScript.DIRECTION_SW), "Ausblick", "outlookTitle",
					null, subtitleProps);
			outlook = lang.newSourceCode(new Offset(0, 10, outlookTitle,
					AnimalScript.DIRECTION_SW), "outlook", null, scProps);
			outlook.addCodeLine(solution, null, 0, null);
			outlook.addCodeLine("", null, 0, null);
			outlook.addCodeLine("Anzahl Iterationen: " + count, null, 0, null);
			outlook.addCodeLine("", null, 0, null);
			outlook.addCodeLine(
					"Wie eingangs erwähnt wird zum Durchführen der Modi Methode zu Beginn eine zulässige ",
					null, 0, null);
			outlook.addCodeLine(
					"Basislöung benötigt. Bekannte Verfahren hierfür sind etwa die Nordwesteckenregel, ",
					null, 0, null);
			outlook.addCodeLine(
					"die Spaltenminimummethode oder die Vogelsche Approximationsmethode. Bei der Betrachtung ",
					null, 0, null);
			outlook.addCodeLine(
					"der Komplexität der MODI Methode gilt damit auch zu berücksichtigen, dass diese auch von ",
					null, 0, null);
			outlook.addCodeLine(
					"der Wahl des gewählten Startverfahrens abhängig ist. Die unterschiedlichen Verfahren ",
					null, 0, null);
			outlook.addCodeLine(
					"können unterschiedliche Basislösungen vorgeben, was sich auf die Zahl der Iterationen ",
					null, 0, null);
			outlook.addCodeLine("der MODI Methode auswirken kann. ", null, 0,
					null);
			outlook.addCodeLine(
					"Alternativ zur Modi Methode lässt sich zum Lösen des Transportproblems auch der Simplex ",
					null, 0, null);
			outlook.addCodeLine(
					"Algorithmus anwenden. Allerdings ist die Modi Methode in der Regel performanter, ",
					null, 0, null);
			outlook.addCodeLine(
					"weswegen diese für das Transportproblem vorgezogen wird. Eine weitere Alternative ist ",
					null, 0, null);
			outlook.addCodeLine(
					"die Stepping-Stone Methode. Diese baut ebenfalls auf einer Basislösung der zuvor ",
					null, 0, null);

			outlook.addCodeLine(
					"erwähnten Eröffnungsverfahren auf. Zwar ist diese performanter als der Simplex ",
					null, 0, null);
			outlook.addCodeLine(
					"Algorithmus aber in der Regel ebenfalls weniger performant als die Modi Methode.",
					null, 0, null);

		}
		
		
		} else {
			inputErrorDesc
			.addCodeLine(
					"Die Anzahl der Anbieter und Nachfrager muss größer 0 sein, sowie über alle Eingabeparameter hinweg übereinstimmen.",
					null, 0, null);
		}

	}
/*
 * Berechnen der Dualvariablenwerte
 * durch Auflösen des LGS
 */
	private void computeDualVariables() {

		lang.nextStep();
		algoStepsDesc.unhighlight(10);
		infoWindow.setText("", null, null);
		// nachfolgende for Schleife kann entfernt werden wenn
		// highlightElem funktioniert, so dass Basisvariablen dauerhaft
		// hervorgehoben werden
		for (int i = 0; i < isPositionBasicVariable.length; i++) {
			for (int j = 0; j < isPositionBasicVariable[i].length; j++) {
				if (isPositionBasicVariable[i][j] == true) {
					animalBasicVariablesMatrix.unhighlightElem(i + 1, j + 1,
							null, null);
				}
			}
		}
		String currentCost = "Aktuelle Gesamtkosten =  ";
		for (int i = 0; i < basicVariables.length; i++) {
			if (i == basicVariables.length - 1) {
				currentCost += "x_" + (basicVariables[i].x + 1)
						+ (basicVariables[i].y + 1) + " * " + "c_"
						+ (basicVariables[i].x + 1) + (basicVariables[i].y + 1)
						+ " = ";
			} else {
				currentCost += "x_" + (basicVariables[i].x + 1)
						+ (basicVariables[i].y + 1) + " * " + "c_"
						+ (basicVariables[i].x + 1) + (basicVariables[i].y + 1)
						+ " + ";
			}
		}
		int fValue = 0;
		for (int i = 0; i < basicVariables.length; i++) {
			fValue += basicVariablesMatrix[basicVariables[i].x][basicVariables[i].y]
					* this.costMatrix[basicVariables[i].x][basicVariables[i].y];
			if (i != 0) {
				currentCost += " + ";
			}
			currentCost += basicVariablesMatrix[basicVariables[i].x][basicVariables[i].y]
					+ " * "
					+ this.costMatrix[basicVariables[i].x][basicVariables[i].y];
		}
		currentCost += " = " + fValue;

		v = lang.newVariables();
		v.declare("int", "Gesamtkosten", "" + fValue);

		infoWindow.setText(currentCost, null, null);
		animalBasicVariablesMatrix.highlightCell(supply.length + 1,
				demand.length + 1, null, null);
		animalBasicVariablesMatrix.put(supply.length + 1, demand.length + 1,
				String.valueOf(fValue), null, null);
		lang.nextStep("Aktuelle Gesamtkosten (" + count + ". Iteration)");
		animalBasicVariablesMatrix.unhighlightCell(supply.length + 1,
				demand.length + 1, null, null);

		algoStepsDesc.highlight(0, 0, true, null, null);
		if (count == 0) {
			lang.nextStep("Beginn des Verfahrens - Bestimmung von Dualvariablenwerten ("
					+ (count + 1) + ". Iteration)");
		} else {
			lang.nextStep("Bestimmung von Dualvariablenwerten (" + (count + 1)
					+ ". Iteration)");
		}

		boolean[] varIsKnown = new boolean[supply.length + demand.length - 1];
		for (int i = 0; i < varIsKnown.length; i++) {
			varIsKnown[i] = false;
		}
		// Bestimme Spalte/Zeile mit meisten Basisvariablen
		int mostBasicVariablesPosition = 0;
		boolean mostBasicVariablesInRow = true;
		int maxNumberOfBasicVariables = 0;
		int basicVariablesIndex = 0;
		// Zähle Anzahl der Basisvariablen in jeder Zeile
		for (int i = 0; i < basicVariablesMatrix.length; i++) {
			int numberOfBasicVariables = 0;
			for (int j = 0; j < basicVariablesMatrix[i].length; j++) {
				if (isPositionBasicVariable[i][j] == true) {
					basicVariables[basicVariablesIndex] = new Point(i, j);
					basicVariablesIndex++;
					numberOfBasicVariables++;
				}
			}
			if (numberOfBasicVariables > maxNumberOfBasicVariables) {
				maxNumberOfBasicVariables = numberOfBasicVariables;
				mostBasicVariablesPosition = i;
			}
		}
		// Zähle Anzahl der Basisvariablen in jeder Spalte
		for (int i = 0; i < basicVariablesMatrix[0].length; i++) {
			int numberOfBasicVariables = 0;
			for (int j = 0; j < basicVariablesMatrix.length; j++) {
				if (isPositionBasicVariable[j][i] == true) {
					numberOfBasicVariables++;
				}
			}
			if (numberOfBasicVariables > maxNumberOfBasicVariables) {
				maxNumberOfBasicVariables = numberOfBasicVariables;
				mostBasicVariablesPosition = i;
				mostBasicVariablesInRow = false;
			}
		}

		// Bestimme die Dualvariablenwerte
		boolean isSupplierVariableComputed[] = new boolean[supply.length];
		boolean isDemanderVariableComputed[] = new boolean[demand.length];

		for (int i = 0; i < (supply.length + demand.length - 1); i++) {
			String equotation = "" + (i + 1) + ". " + "u_"
					+ (basicVariables[i].x + 1) + " + " + "v_"
					+ (basicVariables[i].y + 1) + " = "
					+ costMatrix[basicVariables[i].x][basicVariables[i].y];
			linSys.put(i, 0, equotation, null, null);
			linSys.highlightCell(i, 0, null, null);
			animalCostMatrix.highlightCell(basicVariables[i].x + 1,
					basicVariables[i].y + 1, null, null);
			animalBasicVariablesMatrix.highlightCell(basicVariables[i].x + 1,
					demand.length + 1, null, null);
			animalBasicVariablesMatrix.highlightCell(supply.length + 1,
					basicVariables[i].y + 1, null, null);
			lang.nextStep();
			linSys.unhighlightCell(i, 0, null, null);
			animalCostMatrix.unhighlightCell(basicVariables[i].x + 1,
					basicVariables[i].y + 1, null, null);
			animalBasicVariablesMatrix.unhighlightCell(basicVariables[i].x + 1,
					demand.length + 1, null, null);
			animalBasicVariablesMatrix.unhighlightCell(supply.length + 1,
					basicVariables[i].y + 1, null, null);

		}
		algoStepsDesc.highlight(1, 0, true, null, null);
		// Setze Dualvariable in Zeile/Spalte mit meisten Basisvariablen auf 0
		if (mostBasicVariablesInRow) {
			dualVariablesSupplier[mostBasicVariablesPosition] = 0;
			isSupplierVariableComputed[mostBasicVariablesPosition] = true;

			String infoText = "u_"
					+ (mostBasicVariablesPosition + 1)
					+ " ist die oder eine der häufigsten Variablen im Gleichungssystem";
			infoWindow.setText(infoText, null, null);
			lang.nextStep();
			infoText = "--> u_" + (mostBasicVariablesPosition + 1) + " = 0";
			infoWindow.setText(infoText, null, null);
			lang.nextStep();
			infoText = "Einsetzen in Tabelle und Gleichungsystem";
			infoWindow.setText(infoText, null, null);
			animalBasicVariablesMatrix.put(mostBasicVariablesPosition + 1,
					demand.length + 1, "0", null, null);
			animalBasicVariablesMatrix.highlightCell(
					mostBasicVariablesPosition + 1, demand.length + 1, null,
					null);
			lang.nextStep();
			for (int i = 0; i < basicVariables.length; i++) {
				int basicVarIndex = basicVariables[i].x;
				if (basicVarIndex == mostBasicVariablesPosition) {
					linSys.highlightCell(i, 0, null, null);
					linSys.put(i, 0, "", null, null);
					varIsKnown[i] = true;
					String newEquotation = ""
							+ (i + 1)
							+ ". "
							+ "0"
							+ " + "
							+ "v_"
							+ (basicVariables[i].y + 1)
							+ " = "
							+ costMatrix[basicVariables[i].x][basicVariables[i].y];
					linSys.put(i, 0, "", null, null);
					linSys.put(i, 0, newEquotation, null, null);
					lang.nextStep();
					linSys.unhighlightCell(i, 0, null, null);
					animalBasicVariablesMatrix.unhighlightCell(
							mostBasicVariablesPosition + 1, demand.length + 1,
							null, null);
				}
			}
		} else {
			dualVariablesDemander[mostBasicVariablesPosition] = 0;
			isDemanderVariableComputed[mostBasicVariablesPosition] = true;

			String infoText = "v_"
					+ (mostBasicVariablesPosition + 1)
					+ " ist die oder eine der häufigsten Variablen im Gleichungssystem";
			infoWindow.setText(infoText, null, null);
			lang.nextStep();
			infoText = "--> v_" + (mostBasicVariablesPosition + 1) + " = 0";
			infoWindow.setText(infoText, null, null);
			lang.nextStep();
			infoText = "Einsetzen in Tabelle und Gleichungsystem";
			infoWindow.setText(infoText, null, null);
			animalBasicVariablesMatrix.put(supply.length + 1,
					mostBasicVariablesPosition + 1, "0", null, null);
			animalBasicVariablesMatrix.highlightCell(supply.length + 1,
					mostBasicVariablesPosition + 1, null, null);
			lang.nextStep();
			for (int i = 0; i < basicVariables.length; i++) {
				int basicVarIndex = basicVariables[i].y;
				if (basicVarIndex == mostBasicVariablesPosition) {
					linSys.highlightCell(i, 0, null, null);
					linSys.put(i, 0, "", null, null);
					varIsKnown[i] = true;
					String newEquotation = ""
							+ (i + 1)
							+ ". "
							+ "u_"
							+ (basicVariables[i].x + 1)
							+ " + "
							+ "0"
							+ " = "
							+ costMatrix[basicVariables[i].x][basicVariables[i].y];
					linSys.put(i, 0, "", null, null);
					linSys.put(i, 0, newEquotation, null, null);
					lang.nextStep();
					linSys.unhighlightCell(i, 0, null, null);
					animalBasicVariablesMatrix.unhighlightCell(
							supply.length + 1, mostBasicVariablesPosition + 1,
							null, null);
				}
			}
		}
		algoStepsDesc.unhighlight(1);
		algoStepsDesc.highlight(2, 0, true, null, null);
		lang.nextStep();

		LinkedList<Point> notComputedVariables = new LinkedList<Point>();
		LinkedList<Point> computedVariables = new LinkedList<Point>();
		for (Point p : basicVariables) {
			notComputedVariables.add(p);
		}

		while (notComputedVariables.size() > 0) {
			int pointPos = 0;
			for (Point p : notComputedVariables) {
				if (isSupplierVariableComputed[p.x]
						&& !isDemanderVariableComputed[p.y]) {
					dualVariablesDemander[p.y] = costMatrix[p.x][p.y]
							- dualVariablesSupplier[p.x];
					isDemanderVariableComputed[p.y] = true;
					computedVariables.add(p);
					linSys.highlightCell(pointPos, 0, null, null);
					// lang.nextStep();

					String infoText = "Nach v_" + (p.y + 1) + " auflösen";
					infoWindow.setText(infoText, null, null);
					lang.nextStep();
					String dualVar = "--> v_" + (p.y + 1) + " = "
							+ dualVariablesDemander[p.y];
					infoWindow.setText(dualVar, null, null);
					lang.nextStep();
					infoText = "Einsetzen in Tabelle und Gleichungsystem";
					infoWindow.setText(infoText, null, null);
					animalBasicVariablesMatrix.put(supply.length + 1, p.y + 1,
							String.valueOf(dualVariablesDemander[p.y]), null,
							null);
					animalBasicVariablesMatrix.highlightCell(supply.length + 1,
							p.y + 1, null, null);
					lang.nextStep();
					linSys.unhighlightCell(pointPos, 0, null, null);
					animalBasicVariablesMatrix.unhighlightCell(
							supply.length + 1, p.y + 1, null, null);
					// lang.nextStep();
					for (int i = 0; i < basicVariables.length; i++) {
						int basicVarIndex = basicVariables[i].y;
						if (basicVarIndex == p.y) {
							linSys.highlightCell(i, 0, null, null);
							linSys.put(i, 0, "", null, null);
							String newEquotation;
							if (varIsKnown[i]) {
								newEquotation = ""
										+ (i + 1)
										+ ". "
										+ dualVariablesSupplier[p.x]
										+ " + "
										+ dualVariablesDemander[basicVariables[i].y]
										+ " = "
										+ costMatrix[basicVariables[i].x][basicVariables[i].y];
							} else {
								newEquotation = ""
										+ (i + 1)
										+ ". "
										+ "u_ "
										+ (basicVariables[i].x + 1)
										+ " + "
										+ dualVariablesDemander[basicVariables[i].y]
										+ " = "
										+ costMatrix[basicVariables[i].x][basicVariables[i].y];
								varIsKnown[i] = true;
							}
							linSys.put(i, 0, newEquotation, null, null);
							lang.nextStep();
							linSys.unhighlightCell(i, 0, null, null);
						}
					}

				} else if (!isSupplierVariableComputed[p.x]
						&& isDemanderVariableComputed[p.y]) {
					dualVariablesSupplier[p.x] = costMatrix[p.x][p.y]
							- dualVariablesDemander[p.y];

					isSupplierVariableComputed[p.x] = true;
					computedVariables.add(p);
					linSys.highlightCell(pointPos, 0, null, null);
					String infoText = "Nach u_" + (p.x + 1) + " auflösen";
					infoWindow.setText(infoText, null, null);
					lang.nextStep();
					String dualVar = "--> u_" + (p.x + 1) + " = "
							+ dualVariablesSupplier[p.x];
					infoWindow.setText(dualVar, null, null);
					lang.nextStep();
					infoText = "Einsetzen in Tabelle und Gleichungsystem";
					infoWindow.setText(infoText, null, null);
					animalBasicVariablesMatrix.put(p.x + 1, demand.length + 1,
							String.valueOf(dualVariablesSupplier[p.x]), null,
							null);
					animalBasicVariablesMatrix.highlightCell(p.x + 1,
							demand.length + 1, null, null);
					lang.nextStep();
					linSys.unhighlightCell(pointPos, 0, null, null);
					animalBasicVariablesMatrix.unhighlightCell(p.x + 1,
							demand.length + 1, null, null);
					// lang.nextStep();
					for (int i = 0; i < basicVariables.length; i++) {
						int basicVarIndex = basicVariables[i].x;
						if (basicVarIndex == p.x) {
							linSys.highlightCell(i, 0, null, null);
							linSys.put(i, 0, "", null, null);
							String newEquotation;
							if (varIsKnown[i]) {
								newEquotation = ""
										+ (i + 1)
										+ ". "
										+ dualVariablesSupplier[basicVariables[i].x]
										+ " + "
										+ dualVariablesDemander[p.y]
										+ " = "
										+ costMatrix[basicVariables[i].x][basicVariables[i].y];
							} else {
								newEquotation = ""
										+ (i + 1)
										+ ". "
										+ dualVariablesSupplier[basicVariables[i].x]
										+ " + "
										+ "v_"
										+ (basicVariables[i].y + 1)
										+ " = "
										+ costMatrix[basicVariables[i].x][basicVariables[i].y];
								varIsKnown[i] = true;
							}
							linSys.put(i, 0, newEquotation, null, null);
							lang.nextStep();
							linSys.unhighlightCell(i, 0, null, null);
						}
					}
				} else if (isSupplierVariableComputed[p.x]
						&& isDemanderVariableComputed[p.y]) {
					computedVariables.add(p);
				}
				pointPos++;

			}
			for (Point p : computedVariables) {
				notComputedVariables.remove(p);
			}
			computedVariables.removeAll(computedVariables);
		}
		for (int i = 0; i < (supply.length + demand.length - 1); i++) {
			String equotation = "";
			linSys.put(i, 0, equotation, null, null);
		}
		infoWindow.setText("", null, null);
		algoStepsDesc.unhighlight(0);
		algoStepsDesc.unhighlight(2);

	}

	//Berechne reduzierte Kosten
	private void computeReducedCosts() {
		algoStepsDesc.highlight(3, 0, true, null, null);
		lang.nextStep("Berechnung der reduzierten Kosten (" + (count + 1)
				+ ". Iteration)");
		for (int i = 0; i < basicVariablesMatrix.length; i++) {
			for (int j = 0; j < basicVariablesMatrix[i].length; j++) {
				if (!isPositionBasicVariable[i][j]) {
					basicVariablesMatrix[i][j] = costMatrix[i][j]
							- dualVariablesSupplier[i]
							- dualVariablesDemander[j];
					animalBasicVariablesMatrix.highlightCell(i + 1,
							demand.length + 1, null, null);
					animalBasicVariablesMatrix.highlightCell(supply.length + 1,
							j + 1, null, null);
					animalCostMatrix.highlightCell(i + 1, j + 1, null, null);
					String reducedCostEq = "" + costMatrix[i][j] + " - "
							+ dualVariablesSupplier[i] + " - "
							+ dualVariablesDemander[j] + " = "
							+ basicVariablesMatrix[i][j];
					infoWindow.setText(reducedCostEq, null, null);
					lang.nextStep();
					animalBasicVariablesMatrix.unhighlightCell(i + 1,
							demand.length + 1, null, null);
					animalBasicVariablesMatrix.unhighlightCell(
							supply.length + 1, j + 1, null, null);
					animalCostMatrix.unhighlightCell(i + 1, j + 1, null, null);

					animalBasicVariablesMatrix.put(i + 1, j + 1,
							String.valueOf(basicVariablesMatrix[i][j]), null,
							null);
					animalBasicVariablesMatrix.highlightCell(i + 1, j + 1,
							null, null);
					lang.nextStep();
					animalBasicVariablesMatrix.unhighlightCell(i + 1, j + 1,
							null, null);
					infoWindow.setText("", null, null);
				}
			}
		}
		algoStepsDesc.unhighlight(3);
	}
// Suche nach dem entstandenen Kreis im Transportgraph
	// nach Hinzunahme der neuen Basisvariable beim Basisvariablentausch
	private void findeKreis(Point p, boolean nachbarzeile,
			boolean nachbarspalte, int elemNum) {
		if (p != null) {
			if (blockiert.size() - 1 != elemNum) {
				blockiert.add(new LinkedList<Point>());
			}
			if (!nachbarzeile) {
				for (int i = 0; i < isPositionBasicVariable[p.x].length; i++) {
					if ((isPositionBasicVariable[p.x][i] || (kreis[0].x == p.x && kreis[0].y == i))
							&& (p.y != i)) {
						Point temp = new Point(p.x, i);
						if (blockiert.get(elemNum) != null
								&& !blockiert.get(elemNum).contains(temp)) {
							kreis[elemNum] = temp;
							break;
						}
					}
				}

				if (!(kreis[elemNum] == null)) {

					boolean spalte = false;
					for (int i = 0; i < elemNum; i++) {
						if (kreis[i].y == kreis[elemNum].y) {
							spalte = true;
						}

						blockiert.get(elemNum).add(kreis[i]);
					}

					findeKreis(kreis[elemNum], true, spalte, elemNum + 1);

				} else {
					findeKreis(p, true, nachbarspalte, elemNum);
				}

			} else if (!nachbarspalte) {
				for (int i = 0; i < isPositionBasicVariable.length; i++) {
					if ((isPositionBasicVariable[i][p.y] || (kreis[0].x == i && kreis[0].y == p.y))
							&& (p.x != i)) {
						Point temp = new Point(i, p.y);
						if (blockiert.get(elemNum) != null
								&& !blockiert.get(elemNum).contains(temp)) {
							kreis[elemNum] = temp;
							break;
						}
					}
				}

				if (!(kreis[elemNum] == null)) {
					boolean zeile = false;
					for (int i = 0; i < elemNum; i++) {
						if (kreis[i].x == kreis[elemNum].x) {
							zeile = true;
						}

						blockiert.get(elemNum).add(kreis[i]);
					}

					findeKreis(kreis[elemNum], zeile, true, elemNum + 1);
				} else {
					findeKreis(p, nachbarzeile, true, elemNum);
				}

			} else {

				if (p.y != kreis[0].y) {
					boolean zeile = false;
					boolean spalte = false;
					for (int i = 0; i < elemNum - 2; i++) {
						if (kreis[i].x == kreis[elemNum - 2].x) {
							zeile = true;
						}

						if (kreis[i].y == kreis[elemNum - 2].y) {
							spalte = true;
						}
					}

					kreis[elemNum - 1] = null;

					blockiert.get(elemNum - 1).add(p);

					for (int i = elemNum; i < blockiert.size(); i++) {
						blockiert.remove(i);
					}

					findeKreis(kreis[elemNum - 2], zeile, spalte, elemNum - 1);
				} else {
					numberCycleElem = elemNum;
				}
			}
		}
	}
/*
 * Basisvariablenaustausch, finde Kreis und
 * Umverteilung der Transportmengen entlang des Kreise
 */
	private void basicVariableExchange() {
		// bestimme Nichtbasisvariable mit kleinsten negativen reduzierten
		// Kosten
		Point newBasicVariable = null;
		for (int i = 0; i < basicVariablesMatrix.length; i++) {
			for (int j = 0; j < basicVariablesMatrix[i].length; j++) {
				if (!isPositionBasicVariable[i][j]
						&& basicVariablesMatrix[i][j] < 0) {
					if (newBasicVariable == null
							|| basicVariablesMatrix[i][j] < basicVariablesMatrix[newBasicVariable.x][newBasicVariable.y]) {
						newBasicVariable = new Point(i, j);
					}
				}
			}
		}
		infoWindow.setText("Es existieren negative reduzierte Kosten.", null,
				null);
		for (int i = 0; i < isPositionBasicVariable.length; i++) {
			for (int j = 0; j < isPositionBasicVariable[i].length; j++) {
				if (!isPositionBasicVariable[i][j]) {
					animalBasicVariablesMatrix.unhighlightCell(i + 1, j + 1,
							null, null);
				}
			}
		}
		algoStepsDesc.unhighlight(4);
		algoStepsDesc.unhighlight(6);
		algoStepsDesc.highlight(7, 0, true, null, null);
		algoStepsDesc.highlight(8, 0, true, null, null);
		infoWindow.setText("", null, null);
		animalBasicVariablesMatrix.highlightCell(newBasicVariable.x + 1,
				newBasicVariable.y + 1, null, null);
		lang.nextStep("Basistausch (" + (count + 1) + ". Iteration)");

		algoStepsDesc.unhighlight(8);
		algoStepsDesc.highlight(9, 0, true, null, null);

		kreis = new Point[numberOfBasicVariables + 1];
		kreis[0] = newBasicVariable;
		blockiert = new LinkedList<LinkedList<Point>>();
		blockiert.add(new LinkedList<Point>());
		findeKreis(newBasicVariable, false, false, 1);

		cycleVariables = new Point[numberCycleElem];
		int i = 0;
		for (Point p : kreis) {
			if (p != null) {
				cycleVariables[i] = p;
				i++;
			}
		}

		Point removingBasicVariable = cycleVariables[1];
		int min = basicVariablesMatrix[cycleVariables[1].x][cycleVariables[1].y];
		for (int j = 3; j < cycleVariables.length; j += 2) {
			if (basicVariablesMatrix[cycleVariables[j].x][cycleVariables[j].y] < min) {
				min = basicVariablesMatrix[cycleVariables[j].x][cycleVariables[j].y];
				removingBasicVariable = cycleVariables[j];
			}
		}

		// neue Kante in Animal Graph einfügen
		transportGraph.hide();

		adjacencyMatrix[newBasicVariable.x][newBasicVariable.y + supply.length] = min;
		infoWindow.setText(
				"Aufnahme der neuen Basisvariable in den Transportgraph.",
				null, null);

		transportGraph = lang.newGraph("transportGraph" + tGraphCount,
				adjacencyMatrix, nodePositions, nodeDescriptions, null,
				transportGraphProps);
		tGraphCount++;
		transportGraph.highlightEdge(newBasicVariable.x, newBasicVariable.y
				+ supply.length, null, null);
		transportGraph.highlightEdge(newBasicVariable.y + supply.length,
				newBasicVariable.x, null, null);
		lang.nextStep();
		animalBasicVariablesMatrix.unhighlightCell(newBasicVariable.x + 1,
				newBasicVariable.y + 1, null, null);

		infoWindow.setText(
				"Durch die Aufnahme entsteht ein Kreis im Transportgraph.",
				null, null);

		// higlighten des Kreises im Graph
		for (int j = 1; j < cycleVariables.length; j++) {
			transportGraph.highlightEdge(cycleVariables[j].x,
					cycleVariables[j].y + supply.length, null, null);
		}

		lang.nextStep();
		infoWindow.setText("", null, null);
		for (int j = 0; j < cycleVariables.length; j++) {
			transportGraph.unhighlightEdge(cycleVariables[j].x,
					cycleVariables[j].y + supply.length, null, null);
		}

		// Füge Beschreibung zur Umverteilung im Kreis hinzu
		graphDesc = lang.newSourceCode(new Offset(0, 10, transportGraph,
				AnimalScript.DIRECTION_SW), "graphDesc" + count, null, scProps);
		graphDesc.addCodeLine(
				"Ausgehend von der neuen Kante (Basisvariable) x_"
						+ (newBasicVariable.x + 1) + (newBasicVariable.y + 1)
						+ ", ", null, 0, null);
		graphDesc.addCodeLine("die Anbieter A" + (newBasicVariable.x + 1)
				+ " mit Nachfrager N" + (newBasicVariable.y + 1)
				+ " verbindet, wird bei", null, 0, null);
		graphDesc.addCodeLine(
				"den nachfolgenden Kanten (Basisvariablen) abwechselnd", null,
				0, null);
		graphDesc.addCodeLine(
				"der Wert d subtrahiert und addiert. Der Wert d bildet sich",
				null, 0, null);
		graphDesc.addCodeLine(
				"aus dem Minimum der Werte von denen  d subtrahiert wird.",
				null, 0, null);
		graphDesc.addCodeLine(
				"Die neue Basisvariable erhält schließlich den Wert d.", null,
				0, null);
		graphDesc.addCodeLine(
				"Die Basisvariable mit dem Wert d verlässt die Basis.", null,
				0, null);

		lang.nextStep();
		String subVar = "{";
		String subVarVal = "{";
		String addVar = "{";
		for (int j = 1; j < cycleVariables.length; j++) {
			if (j % 2 == 0) {
				if (j >= (cycleVariables.length - 2)) {
					addVar += "x_" + (cycleVariables[j].x + 1)
							+ (cycleVariables[j].y + 1) + "}";
				} else {
					addVar += "x_" + (cycleVariables[j].x + 1)
							+ (cycleVariables[j].y + 1) + ", ";
				}
			} else if (j % 2 != 0) {
				if (j >= (cycleVariables.length - 2)) {
					subVar += "x_" + (cycleVariables[j].x + 1)
							+ (cycleVariables[j].y + 1) + "}";
					subVarVal += basicVariablesMatrix[cycleVariables[j].x][cycleVariables[j].y]
							+ "}";
				} else {
					subVar += "x_" + (cycleVariables[j].x + 1)
							+ (cycleVariables[j].y + 1) + ", ";
					subVarVal += basicVariablesMatrix[cycleVariables[j].x][cycleVariables[j].y]
							+ ", ";
				}
			}
		}
		graphDesc.addCodeLine("", null, 0, null);
		graphDesc.addCodeLine("Basisvariablen zu denen d hinzuaddiert wird: ",
				null, 0, null);
		graphDesc.addCodeLine(addVar, null, 0, null);
		graphDesc.addCodeLine("Basisvariablen von denen d subtrahiert wird: ",
				null, 0, null);
		graphDesc.addCodeLine(subVar, null, 0, null);
		lang.nextStep();
		graphDesc.addCodeLine("", null, 0, null);
		graphDesc.addCodeLine("d = min" + subVar + " = " + subVarVal, null, 0,
				null);
		graphDesc.addCodeLine("--> d = " + min, null, 0, null);
		// Fragenpaket 3
		if (count == 0 && sindFragenErlaubt) {
			// Zweiter Fragenblock
			Random rand = new Random();
			int randomInt = rand.nextInt(3);
			String question;
			boolean correctAnswer;
			String feedbackForTrue;
			String feedbackForFalse;
			switch (randomInt) {
			case 0:
				question = "Durch die Aufnahme einer neuen Basisvariable in diesem Schritt erhöht sich letztlich die Gesamtzahl der Basisvariablen. Wahr oder Falsch?";
				correctAnswer = false;
				feedbackForTrue = "Die Antwort ist leider falsch. Es verlässt gleichzeitig eine andere Basisvariable die Basis.";
				feedbackForFalse = "Korrekte Antwort, da gleichzeitig eine andere Basisvariable die Basis verlässt.";
				break;
			case 1:
				question = "Die neue Basisvariable wird um den Wert d erhöht. Wahr oder Falsch?";
				correctAnswer = false;
				feedbackForTrue = "Die Antwort ist leider falsch. Die neue Basisvariable nimmt den Wert d an.";
				feedbackForFalse = "Korrekte Antwort. Die neue Basisvariable nimmt den Wert d an.";
				break;
			case 2:
				question = "Die zu entfernende Basisvariable erhält den Wert 0. Wahr oder Falsch?";
				correctAnswer = true;
				feedbackForTrue = "Korrekte Antwort. Der Wert d entspricht dem Wert der zu entfernenden Basisvariable. Da von dieser der Wert d abgezogen wird, erhält sie den Wert 0.";
				feedbackForFalse = "Die Antwort ist leider falsch. Der Wert d entspricht dem Wert der zu entfernenden Basisvariable. Da von dieser der Wert d abgezogen wird, erhält sie den Wert 0.";
				break;
			default:
				question = "Durch die Aufnahme einer neuen Basisvariable in diesem Schritt erhöht sich letztlich die Gesamtzahl der Basisvariablen. Wahr oder Falsch?";
				correctAnswer = false;
				feedbackForTrue = "Die Antwort ist leider falsch. Es verlässt gleichzeitig eine andere Basisvariable die Basis.";
				feedbackForFalse = "Korrekte Antwort, da gleichzeitig eine andere Basisvariable die Basis verlässt.";
				break;
			}
			TrueFalseQuestionModel cycleQuestion = new TrueFalseQuestionModel(
					"cycleQuestion");
			cycleQuestion.setPrompt(question);
			cycleQuestion.setCorrectAnswer(correctAnswer);
			cycleQuestion.setPointsPossible(5);
			cycleQuestion.setFeedbackForAnswer(true, feedbackForTrue);
			cycleQuestion.setFeedbackForAnswer(false, feedbackForFalse);
			lang.addTFQuestion(cycleQuestion);
		}
		lang.nextStep();

		// min Wert entlang des Kreises umverteilen
		for (int j = 1; j < cycleVariables.length; j++) {
			if (j % 2 == 0) {
				infoWindow
						.setText(
								"x_"
										+ (cycleVariables[j].x + 1)
										+ (cycleVariables[j].y + 1)
										+ "_neu = "
										+ "x_"
										+ (cycleVariables[j].x + 1)
										+ (cycleVariables[j].y + 1)
										+ "_alt + "
										+ "d = "
										+ basicVariablesMatrix[cycleVariables[j].x][cycleVariables[j].y]
										+ " + "
										+ min
										+ " = "
										+ (basicVariablesMatrix[cycleVariables[j].x][cycleVariables[j].y] += min),
								null, null);

				animalBasicVariablesMatrix
						.put(cycleVariables[j].x + 1,
								cycleVariables[j].y + 1,
								String.valueOf(basicVariablesMatrix[cycleVariables[j].x][cycleVariables[j].y]),
								null, null);
				animalBasicVariablesMatrix.highlightCell(
						cycleVariables[j].x + 1, cycleVariables[j].y + 1, null,
						null);
				transportGraph.highlightEdge(cycleVariables[j].x,
						cycleVariables[j].y + supply.length, null, null);
				adjacencyMatrix[cycleVariables[j].x][cycleVariables[j].y
						+ supply.length] = basicVariablesMatrix[cycleVariables[j].x][cycleVariables[j].y];
				transportGraph
						.setEdgeWeight(
								cycleVariables[j].x,
								cycleVariables[j].y + supply.length,
								basicVariablesMatrix[cycleVariables[j].x][cycleVariables[j].y],
								null, null);
				lang.nextStep();
				animalBasicVariablesMatrix.unhighlightCell(
						cycleVariables[j].x + 1, cycleVariables[j].y + 1, null,
						null);
				transportGraph.unhighlightEdge(cycleVariables[j].x,
						cycleVariables[j].y + supply.length, null, null);
			} else if (j % 2 != 0) {
				infoWindow
						.setText(
								"x_"
										+ (cycleVariables[j].x + 1)
										+ (cycleVariables[j].y + 1)
										+ "_neu = "
										+ "x_"
										+ (cycleVariables[j].x + 1)
										+ (cycleVariables[j].y + 1)
										+ "_alt - "
										+ "d = "
										+ basicVariablesMatrix[cycleVariables[j].x][cycleVariables[j].y]
										+ " - "
										+ min
										+ " = "
										+ (basicVariablesMatrix[cycleVariables[j].x][cycleVariables[j].y] -= min),
								null, null);
				animalBasicVariablesMatrix
						.put(cycleVariables[j].x + 1,
								cycleVariables[j].y + 1,
								String.valueOf(basicVariablesMatrix[cycleVariables[j].x][cycleVariables[j].y]),
								null, null);
				animalBasicVariablesMatrix.highlightCell(
						cycleVariables[j].x + 1, cycleVariables[j].y + 1, null,
						null);
				transportGraph.highlightEdge(cycleVariables[j].x,
						cycleVariables[j].y + supply.length, null, null);
				adjacencyMatrix[cycleVariables[j].x][cycleVariables[j].y
						+ supply.length] = basicVariablesMatrix[cycleVariables[j].x][cycleVariables[j].y];
				transportGraph
						.setEdgeWeight(
								cycleVariables[j].x,
								cycleVariables[j].y + supply.length,
								basicVariablesMatrix[cycleVariables[j].x][cycleVariables[j].y],
								null, null);
				lang.nextStep();
				animalBasicVariablesMatrix.unhighlightCell(
						cycleVariables[j].x + 1, cycleVariables[j].y + 1, null,
						null);
				transportGraph.unhighlightEdge(cycleVariables[j].x,
						cycleVariables[j].y + supply.length, null, null);
			}
		}
		basicVariablesMatrix[removingBasicVariable.x][removingBasicVariable.y] = 0;
		basicVariablesMatrix[newBasicVariable.x][newBasicVariable.y] = min;
		animalBasicVariablesMatrix.put(newBasicVariable.x + 1,
				newBasicVariable.y + 1, String.valueOf(min), null, null);
		animalBasicVariablesMatrix.highlightCell(newBasicVariable.x + 1,
				newBasicVariable.y + 1, null, null);
		infoWindow.setText("x_" + (newBasicVariable.x + 1)
				+ (newBasicVariable.y + 1) + "_neu = " + "d = " + min, null,
				null);
		lang.nextStep();
		animalBasicVariablesMatrix.unhighlightCell(newBasicVariable.x + 1,
				newBasicVariable.y + 1, null, null);
		animalBasicVariablesMatrix.highlightCell(removingBasicVariable.x + 1,
				removingBasicVariable.y + 1, null, null);
		infoWindow.setText("x_" + (removingBasicVariable.x + 1)
				+ (removingBasicVariable.y + 1) + " verlässt die Basis", null,
				null);
		adjacencyMatrix[removingBasicVariable.x][removingBasicVariable.y
				+ supply.length] = 0;
		adjacencyMatrix[removingBasicVariable.y + supply.length][removingBasicVariable.x] = 0;
		transportGraph.hide();
		transportGraph = lang.newGraph("transportGraph" + tGraphCount,
				adjacencyMatrix, nodePositions, nodeDescriptions, null,
				transportGraphProps);
		tGraphCount++;
		lang.nextStep();
		animalBasicVariablesMatrix.unhighlightCell(removingBasicVariable.x + 1,
				removingBasicVariable.y + 1, null, null);
		isPositionBasicVariable[removingBasicVariable.x][removingBasicVariable.y] = false;
		isPositionBasicVariable[newBasicVariable.x][newBasicVariable.y] = true;
		for (int j = 0; j < basicVariables.length; j++) {
			if (basicVariables[j].x == removingBasicVariable.x
					&& basicVariables[j].y == removingBasicVariable.y) {
				basicVariables[j].x = newBasicVariable.x;
				basicVariables[j].y = newBasicVariable.y;
			}
		}

		algoStepsDesc.unhighlight(7);
		algoStepsDesc.unhighlight(9);
		algoStepsDesc.highlight(10, 0, true, null, null);
		infoWindow.setText("Markierte Zellen sind die neuen Basisvariablen.",
				null, null);
		for (int k = 0; k < isPositionBasicVariable.length; k++) {
			for (int j = 0; j < isPositionBasicVariable[k].length; j++) {
				if (isPositionBasicVariable[k][j] == true) {
					animalBasicVariablesMatrix.highlightElem(k + 1, j + 1,
							null, null);
				}
			}
		}
		String basVar = "{";
		int k = 0;
		for (int j = 0; j < basicVariables.length; j++) {

			if (k < basicVariables.length - 1) {
				basVar += "x_" + (basicVariables[j].x + 1)
						+ (basicVariables[j].y + 1) + ", ";
			} else {
				basVar += "x_" + (basicVariables[j].x + 1)
						+ (basicVariables[j].y + 1) + "}";
			}
			k++;
		}
		// v = lang.newVariables();
		v.declare("String", "Basisvariablen", basVar);
		// v.setRole("Basisvariablen",
		// animal.variables.Variable.getRoleString(VariableRoles.STEPPER));
		graphDesc.hide();

	}

	private boolean existsNegativeBasicVariable() {
		boolean foundNegativeVariable = false;
		for (int i = 0; i < basicVariablesMatrix.length; i++) {
			for (int j = 0; j < basicVariablesMatrix[i].length; j++) {
				if (basicVariablesMatrix[i][j] < 0) {
					foundNegativeVariable = true;
				}
			}
		}

		return foundNegativeVariable;
	}

	/*
	 * Methoden zum Überprüfen der Eingabe
	 */
	private boolean isInputCorrect() {

		

		// Anzahl der Basisvariablen muss Anzahl der Anbieter + Anzahl der
		// Nachfrager - 1 entprechen
		if (basicVariables.length != numberOfBasicVariables) {
			inputErrorDesc
					.addCodeLine(
							"Fehler bei der Eingabe: Die Anzahl der Basisvariablen muss Anzahl der Anbieter + Anzahl der Nachfrager - 1 entprechen!",
							null, 0, null);
			return false;
		}

		if (!supplyEqualsDemand()) {
			inputErrorDesc
					.addCodeLine(
							"Fehler bei der Eingabe: Die Summe der angebotenen Menge muss der nachgefragten Menge entsprechen!",
							null, 0, null);
			inputErrorDesc
					.addCodeLine(
							"Beide müssen zudem größer 0 sein (Wenn es keine Transportmenge gibt, muss auch nichts optimiert werden)!",
							null, 0, null);
			return false;
		}

		if (!sumOfBasicVariablesIsCorrect()) {
			inputErrorDesc
					.addCodeLine(
							"Fehler bei der Eingabe: Die Summe der Basisvariablen muss der Summe der angebotenen Menge bzw. nachgefragten Menge entsprechen!",
							null, 0, null);
			return false;
		}

		if (!noInputValueLessThanZero()) {
			inputErrorDesc
					.addCodeLine(
							"Fehler bei der Eingabe: Bei allen Eingabeparametern darf kein Wert kleiner als 0 sein!",
							null, 0, null);
			return false;
		}

		if (!isBasicVarSumOfSuppliersCorrect()) {
			inputErrorDesc
					.addCodeLine(
							"Fehler bei der Eingabe: Die transportierten Mengen in der Basisvariablenmatrix müssen auch mit den jeweiligen ",
							null, 0, null);
			inputErrorDesc.addCodeLine(
					"Angebotsmengen der Anbieter übereinstimmen.", null, 0,
					null);
			return false;
		}

		if (!isBasicVarSumOfDemandersCorrect()) {
			inputErrorDesc
					.addCodeLine(
							"Fehler bei der Eingabe: Die transportierten Mengen in der Basisvariablenmatrix müssen auch mit den jeweiligen ",
							null, 0, null);
			inputErrorDesc.addCodeLine(
					"Nachfragemengen der Nachfrager übereinstimmen.", null, 0,
					null);
			return false;
		}

		return true;
	}

	/*
	 * Nachfrage muss Angebot entsprechen
	 */
	private boolean supplyEqualsDemand() {
		int sumOfSupply = 0;
		int sumOfDemand = 0;

		for (int i = 0; i < supply.length; i++) {
			sumOfSupply += supply[i];
		}
		for (int i = 0; i < demand.length; i++) {
			sumOfDemand += demand[i];
		}

		if (sumOfSupply == sumOfDemand && sumOfSupply != 0) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Prüft ob die in den Basisvariablen (Transportmengen) angegegeben Werte
	 * mit den jeweiligen Angebotsmengen übereinstimmen ()
	 */
	private boolean isBasicVarSumOfSuppliersCorrect() {
		int[] basicVarSupply = new int[supply.length];
		for (int i = 0; i < basicVarSupply.length; i++) {
			basicVarSupply[i] = 0;
		}
		for (int i = 0; i < isPositionBasicVariable.length; i++) {
			for (int j = 0; j < isPositionBasicVariable[i].length; j++) {
				if (isPositionBasicVariable[i][j]) {
					basicVarSupply[i] += basicVariablesMatrix[i][j];
				}
			}
		}
		for (int i = 0; i < basicVarSupply.length; i++) {
			if (basicVarSupply[i] != supply[i]) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Prüft ob die in den Basisvariablen (Transportmengen) angegegeben Werte
	 * mit den jeweiligen Nachfragemengen übereinstimmen ()
	 */
	private boolean isBasicVarSumOfDemandersCorrect() {
		int[] basicVarDemand = new int[demand.length];
		for (int i = 0; i < basicVarDemand.length; i++) {
			basicVarDemand[i] = 0;
		}
		for (int i = 0; i < isPositionBasicVariable.length; i++) {
			for (int j = 0; j < isPositionBasicVariable[i].length; j++) {
				if (isPositionBasicVariable[i][j]) {
					basicVarDemand[j] += basicVariablesMatrix[i][j];
				}
			}
		}
		for (int i = 0; i < basicVarDemand.length; i++) {
			if (basicVarDemand[i] != demand[i]) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Summe der Basisvariablen muss Summe der Nachfrage/ des Angebots
	 * entprechen
	 */
	private boolean sumOfBasicVariablesIsCorrect() {
		int sumOfBasicVariables = 0;
		int sumOfSupply = 0;
		for (Point p : basicVariables) {
			sumOfBasicVariables += basicVariablesMatrix[p.x][p.y];
		}
		for (int i = 0; i < supply.length; i++) {
			sumOfSupply += supply[i];
		}

		if (sumOfSupply == sumOfBasicVariables) {
			return true;
		} else {
			return false;
		}
	}

	private boolean noInputValueLessThanZero() {

		// Es darf keine negative Basisvariable geben
		if (existsNegativeBasicVariable()) {
			return false;
		}

		// Es darf keine negatives Angebot geben
		for (int i = 0; i < supply.length; i++) {
			if (supply[i] < 0) {
				return false;
			}
		}

		// Es darf keine negative Nachfrage geben
		for (int i = 0; i < demand.length; i++) {
			if (demand[i] < 0) {
				return false;
			}
		}

		for (int i = 0; i < costMatrix.length; i++) {
			for (int j = 0; j < costMatrix[i].length; j++) {
				if (costMatrix[i][j] < 0) {
					return false;
				}
			}
		}

		return true;
	}

	/*
	 * Methoden für Animal
	 */

	private void initializeAlgoAnimal() {

		// Untertitel für Beschreibung der Algoschritte
		algoStepsTitle = lang.newText(new Offset(0, 10, title,
				AnimalScript.DIRECTION_SW), "Schritte des Verfahrens",
				"algoStepsTitle", null, subtitleProps);
		// Pseudocode des Algo
		algoStepsDesc = lang.newSourceCode(new Offset(0, 5, algoStepsTitle,
				AnimalScript.DIRECTION_SW), "sourceCode", null, scProps);
		algoStepsDesc
				.addCodeLine(
						"I. Bestimmung von Dualvariablenwerten: Bilde lineares Gleichungssystem (LGS) u_i + v_j = c_ij mit allen i,j, deren x_ij Basisvariable ist.",
						null, 0, null);
		algoStepsDesc
				.addCodeLine(
						"1. Setze Variable, die am häufigsten im LG vorkommt gleich 0.",
						null, 1, null);
		algoStepsDesc.addCodeLine(
				"2. Bestimme die restlichen Variablen durch Auflösen des LGS.",
				null, 1, null);
		algoStepsDesc
				.addCodeLine(
						"II. Berechnung von reduzierten Kosten: Berechne für alle NBV x_ij die reduzierten Kosten r_ij = c_ij - u_i - v_j.",
						null, 0, null);
		algoStepsDesc.addCodeLine("III. Sind alle r_ij >= 0?", null, 0, null);
		algoStepsDesc.addCodeLine(
				"1. Ja --> Stop: Die optimale Lösung wurde gefunden.", null, 1,
				null);
		algoStepsDesc.addCodeLine("2. Nein --> Fahre fort mit Schritt IV.",
				null, 1, null);
		algoStepsDesc.addCodeLine("IV. Basistausch: ", null, 0, null);
		algoStepsDesc
				.addCodeLine(
						"1. Aufnahme der NBV x_pq mit den kleinsten reduzierten Kosten in die Basis. ",
						null, 1, null);
		algoStepsDesc.addCodeLine(
				"2. Umverteilung der Transportmengen entlang des Kreises.",
				null, 1, null);
		algoStepsDesc
				.addCodeLine(
						"V. Beginne mit der neuen zulässigen Basislösung bei Schritt I.",
						null, 0, null);

		// Untertitel für Kostenmatrix
		costMatrixTitle = lang.newText(new Offset(0, 10, algoStepsDesc,
				AnimalScript.DIRECTION_SW), "Kostenmatrix", "costMatrixTitle",
				null, subtitleProps);

		// Kostenmatrix für Animal
		String[][] costMatrix = new String[supply.length + 1][demand.length + 1];

		costMatrix[0][0] = "";

		for (int i = 1; i < costMatrix[0].length; i++) {
			costMatrix[0][i] = "N" + (i);
		}

		for (int i = 1; i < costMatrix.length; i++) {
			costMatrix[i][0] = "A" + (i);
		}

		for (int i = 0; i < this.costMatrix.length; i++) {
			for (int j = 0; j < this.costMatrix[i].length; j++) {
				costMatrix[i + 1][j + 1] = String
						.valueOf(this.costMatrix[i][j]);

			}
		}
		animalCostMatrix = lang.newStringMatrix(new Offset(0, 10,
				costMatrixTitle, AnimalScript.DIRECTION_SW), costMatrix,
				"costMatrix", null, maProps);

		// Untertitel für Basisvariablenmatrix
		basicVartitle = lang.newText(new Offset(0, 20, animalCostMatrix,
				AnimalScript.DIRECTION_SW), "Basisvariablen/Reduzierte Kosten",
				"basicVarMatrixTitle", null, subtitleProps);

		// Basisvariablenmatrix für Animal
		String[][] basisVariablesMatrix = new String[supply.length + 2][demand.length + 2];

		for (int i = 0; i < basisVariablesMatrix.length; i++) {
			for (int j = 0; j < basisVariablesMatrix[i].length; j++) {
				basisVariablesMatrix[i][j] = "";
			}
		}
		for (int i = 1; i < basisVariablesMatrix[0].length - 1; i++) {
			basisVariablesMatrix[0][i] = "N" + (i);
		}
		for (int i = 1; i < basisVariablesMatrix.length - 1; i++) {
			basisVariablesMatrix[i][0] = "A" + (i);
		}

		for (int i = 0; i < isPositionBasicVariable.length; i++) {
			for (int j = 0; j < isPositionBasicVariable[i].length; j++) {
				if (isPositionBasicVariable[i][j] == true) {
					basisVariablesMatrix[i + 1][j + 1] = String
							.valueOf(basicVariablesMatrix[i][j]);
				} else {
					basisVariablesMatrix[i + 1][j + 1] = "";
				}
			}
		}
		animalBasicVariablesMatrix = lang.newStringMatrix(new Offset(0, 10,
				basicVartitle, AnimalScript.DIRECTION_SW),
				basisVariablesMatrix, "basicVariables", null, maProps);
		// Informationsfenster, z.B. zur Ausgabe des Ergebnisses
		infoWindow = lang.newText(new Offset(0, 20, animalBasicVariablesMatrix,
				AnimalScript.DIRECTION_SW), "", "infoWin", null,
				infoWindowProps);
		infoWindow.setText("Markierte Zellen sind Basisvariablen", null, null);
		for (int i = 0; i < isPositionBasicVariable.length; i++) {
			for (int j = 0; j < isPositionBasicVariable[i].length; j++) {
				if (isPositionBasicVariable[i][j] == true) {
					animalBasicVariablesMatrix.highlightElem(i + 1, j + 1,
							null, null);
				}
			}
		}
		// Untertitel für lineares Gleichungssystem
		linEqTitle = lang.newText(new Offset(0, 20, infoWindow,
				AnimalScript.DIRECTION_SW),
				"Nebenbedingungen des Dualen Problems", "linEqTitle", null,
				subtitleProps);

		// Initialisierung einer StringMatrix für das lin. Gleichungssystem
		String[][] linearSystem = new String[supply.length + demand.length][1];
		for (int i = 0; i < linearSystem.length; i++) {
			linearSystem[i][0] = "";
		}
		linSys = lang.newStringMatrix(new Offset(0, 10, linEqTitle,
				AnimalScript.DIRECTION_SW), linearSystem, "linearSystem", null,
				linSysProps);

		animalBasicVariablesMatrix.put(0, demand.length + 1, "u_i", null, null);
		animalBasicVariablesMatrix.put(supply.length + 1, 0, "v_j", null, null);

		// Transportgraph
		// Adjazenzmatrix
		adjacencyMatrix = new int[supply.length + demand.length][supply.length
				+ demand.length];
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			for (int j = 0; j < adjacencyMatrix[i].length; j++) {
				adjacencyMatrix[i][j] = 0;
			}
		}
		for (int i = 0; i < isPositionBasicVariable.length; i++) {
			for (int j = 0; j < isPositionBasicVariable[i].length; j++) {
				if (isPositionBasicVariable[i][j] == true) {
					adjacencyMatrix[i][j + supply.length] = basicVariablesMatrix[i][j];
					adjacencyMatrix[j + supply.length][i] = basicVariablesMatrix[i][j];
				}
			}
		}

		// Untertitel für Transportgraph
		transportGraphTitle = lang.newText(new Offset(50, 30, algoStepsDesc,
				AnimalScript.DIRECTION_NE), "Transportgraph",
				"transGraphTitle", null, subtitleProps);

		// Positionen der Knoten
		nodePositions = new Node[supply.length + demand.length];
		int y = 20;
		for (int i = 0; i < supply.length; i++) {
			nodePositions[i] = new Offset(0, y, transportGraphTitle,
					AnimalScript.DIRECTION_SW);
			y += 70;
		}
		int x = 200;
		y = 10;
		for (int i = supply.length; i < supply.length + demand.length; i++) {
			nodePositions[i] = new Offset(x, y, transportGraphTitle,
					AnimalScript.DIRECTION_SW);
			y += 70;
		}

		// Beschriftung der Knoten
		nodeDescriptions = new String[supply.length + demand.length];
		for (int i = 0; i < supply.length; i++) {
			nodeDescriptions[i] = "A" + (i + 1);
		}

		for (int i = supply.length; i < supply.length + demand.length; i++) {
			nodeDescriptions[i] = "N" + (i + 1 - supply.length);
		}

		transportGraph = lang.newGraph("transportGraph", adjacencyMatrix,
				nodePositions, nodeDescriptions, null, transportGraphProps);

	}

	private void initializeAnimalProperties() {

		// Properties für Subtitel, z.B. für den Transportgraph
		subtitleProps = untertitelEigenschaften;
		subtitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 14));

		// Properties für Beschreibung eventueller Eingabefehler
		inputErrorDescProps = new SourceCodeProperties();
		inputErrorDescProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
				Color.BLUE);
		inputErrorDescProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
				new Font(Font.SANS_SERIF, Font.PLAIN, 12));

		inputErrorDescProps.set(
				AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		inputErrorDescProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Color.BLACK);

		// Properties für die erklärenden Schritte des Algos
		scProps = textEigenschaften;
		// scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
		// Color.BLUE);
		// scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		// Font.SANS_SERIF, Font.PLAIN, 12));
		//
		// scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
		// Color.RED);
		// scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		// Properties für Kosten- und Basisvariablenmatrix
		maProps = matrixEigenschaften;
		// maProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
		// Color.BLUE);
		// maProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		// Font.SANS_SERIF, Font.PLAIN, 12));
		maProps.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
		// maProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
		// Color.RED);
		// maProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
		// Color.GREEN);
		// maProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK); //
		// Schriftfarbe
		maProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		maProps.set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, 20);
		maProps.set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, 30);

		// Properties für den Transportgraph
		transportGraphProps = new GraphProperties();
		transportGraphProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Color.WHITE);
		transportGraphProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Color.WHITE);
		transportGraphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY,
				Color.BLACK);// Knotenbeschriftung
		transportGraphProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
				Color.WHITE); // Knotenhintergrund
		transportGraphProps
				.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
		transportGraphProps.set(
				AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);

		// Properties für das lineare Gleichungssystem
		linSysProps = new MatrixProperties();
		// linSysProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		// Font.SANS_SERIF, Font.PLAIN, 12));
		// linSysProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		linSysProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		linSysProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		linSysProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "plain");
		linSysProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				highlightColor);
		linSysProps.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
		linSysProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				elemColor);

		// Properties für Inormationsfenster
		infoWindowProps = infoFensterEigenschaften;
		infoWindowProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 14));

	}

	private void setDescription() {
		SourceCodeProperties descriptionProps = textEigenschaften;
		// descriptionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		// Font.SANS_SERIF, Font.PLAIN, 12));
		description = lang.newSourceCode(new Offset(0, 10, title,
				AnimalScript.DIRECTION_SW), "description", null,
				descriptionProps);
		description
				.addCodeLine(
						"Die MODI Methode (Modifizierte Distributionsmethode) ist ein Verfahren aus dem Gebiet des Operations Research, mit dem ein Standard-Transportproblem optimal gelöst werden kann.",
						null, 0, null);
		description
				.addCodeLine(
						"Beim Transportproblem werden bestimmte Mengen eines homogenen Gutes von mehreren Angebotsorten zu mehreren Nachfrageorten transportiert, ",
						null, 0, null);
		description
				.addCodeLine(
						"wobei die Tranportkosten pro Mengeneinheit zwischen den Standorten bekannt sind. Ebenfalls bekannt sind die",
						null, 0, null);
		description
				.addCodeLine(
						"Angebots- und Nachfragemengen. Gesucht ist ein kostenminimaler Transportplan, bei dem alle Bedarfe befriedigt und alle Angebote ausgeschöpft werden.",
						null, 0, null);
		description.addCodeLine("", null, 0, null);
		description.addCodeLine("Voraussetzungen:", null, 0, null);
		description.addCodeLine("", null, 0, null);
		description
				.addCodeLine(
						"1. Es muss eine zulässige Basislösung vorliegen, die mit einem Eröffnungsverfahren (wie der Vogelschen Approximationsmethode oder der Spaltenminimummethode) ermittelt werden kann.",
						null, 0, null);
		description.addCodeLine(
				"2. Die Angebotsmenge muss der Nachfragemenge entsprechen.",
				null, 0, null);
		description
				.addCodeLine(
						"3. Das Angebot muss vollständig aufgebraucht und die Nachfrage vollständig erfüllt werden. D.h. die Transportmengen müssen den Angebots- und Nachfragemengen entsprechen.",
						null, 0, null);
		description
				.addCodeLine(
						"4. Alle Angebots-, Nachfrage- und Transportmengen sowie die Transportkosten müssen größer oder gleich null sein.",
						null, 0, null);
		description.addCodeLine("", null, 0, null);
		description
				.addCodeLine(
						"Für einen Anbieter A_i stellt die Variable x_ij die transportierte Menge zu dem Nachfrager N_j dar. Die Angebots- und Nachfragemengen werden mit a_i, bzw. n_j bezeichnet.",
						null, 0, null);
		description
				.addCodeLine(
						"Die entsprechenden Kosten für eine transportierte Einheit werden mit c_ij beschrieben. Gesucht ist damit ein Transportplan, bei dem die Gesamtkosten für die Summe aus dem",
						null, 0, null);
		description
				.addCodeLine(
						"Produkt zwischen x_ij und c_ij für alle Anbieter und Nachfrager minimal sind (min f = ∑ cij * xij für alle i = 1,…,m und j = 1,…,n ). Das Transportproblem liegt somit in",
						null, 0, null);
		description.addCodeLine(
				"der Form eines linearen Optimierungs-Problems vor.", null, 0,
				null);
		description
				.addCodeLine(
						"Die Modi Methode macht sich zu Nutze, dass sich jedes lineare Problem in ein duales Problem transformieren lässt. Hierbei wird das Minimierungsverfahren zu einem",
						null, 0, null);
		description
				.addCodeLine(
						"Maximierungsverfahren (max z = ∑ a_i * u_i + ∑ n_j * u_j). Aus den hierbei eingeführten Dualvariablen u_i und v_j bilden sich auch die Nebenbedingungen, dass die Summe aus ",
						null, 0, null);
		description
				.addCodeLine(
						"diesen kleiner oder gleich den entsprechenden Transportkosten c_ij sind. Diese Nebenbedingungen werden für jede Basisvariable aufgestellt und das damit erhaltene ",
						null, 0, null);
		description
				.addCodeLine(
						"Gleichungssystem aufgelöst. Anschließend werden mit den Dualvariablen die reduzierten Kosten r_ij berechnet, die aussagen, um wie viel sich die Gesamtkosten reduzieren lassen ",
						null, 0, null);
		description
				.addCodeLine(
						"würden, wenn die transportierten Mengen umverteilt werden, so dass eine Einheit von A_i nach N_i transportiert wird (Ein negativer Wert sagt dabei aus, dass sich die Kosten um ",
						null, 0, null);
		description.addCodeLine("den entsprechenden Wert reduzieren lassen). ",
				null, 0, null);
		description
				.addCodeLine(
						"Sind alle reduzierten Kosten größer oder gleich null, wurde ein kostenminimaler Transportplan gefunden. Ist dies nicht der Fall, wird die Nichtbasisvariable x_ij mit den ",
						null, 0, null);
		description
				.addCodeLine(
						"geringsten reduzierten Kosten (größter negativer Wert) in die Basis aufgenommen (als neue Basisvariable). Dadurch entsteht im zum Transportproblem zugehörigen Transportgraph ein Kreis.",
						null, 0, null);
		description
				.addCodeLine(
						"Ausgehend von der neuen Basisvariable wird nun, von jeder Basisvariable entlang des Kreises, abwechselnd der Wert d abgezogen und auf die nächste Basisvariable addiert.",
						null, 0, null);
		description
				.addCodeLine(
						"Der Wert d ist dabei der kleinste Wert einer Basisvariable, bei der d subtrahiert werden soll. Anschließend wird diese Variable aus der Basis entfernt. Die neue ",
						null, 0, null);
		description
				.addCodeLine(
						"Basisvariable erhält den Wert d. Mit diesem Schritt wird die maximal zulässige Transportmenge entlang des Kreises so umverteilt, dass die Gesamtkosten reduziert werden.",
						null, 0, null);
		description
				.addCodeLine(
						"Dieses Vorgehen wird so lange wiederholt bis alle berechneten reduzierten Kosten größer oder gleich 0 sind und somit der kostenminimale Transportplan gefunden wurde.",
						null, 0, null);

		lang.nextStep("Beschreibung des Algorithmus");
		description.hide();
		description.addCodeLine("Vorgehen:", null, 0, null);

		algoStepsTitle = lang.newText(new Offset(0, 10, title,
				AnimalScript.DIRECTION_SW), "Schritte des Verfahrens",
				"algoStepsTitle", null, subtitleProps);

		description = lang.newSourceCode(new Offset(0, 10, algoStepsTitle,
				AnimalScript.DIRECTION_SW), "pseudoCode", null,
				descriptionProps);
		description
				.addCodeLine(
						"0. Prüfe, ob es sich bei den angegebenen Werten um eine zulässige Basislösung handelt (Überprüfen der Voraussetzungen). ",
						null, 0, null);
		description
				.addCodeLine(
						"Falls nein, keine Lösung. Falls ja, Schritt I. --> Hier bereits geschehen. Man kann mit Schritt I fortfahren.",
						null, 1, null);
		description
				.addCodeLine(
						"I. Bilde und löse lineares Gleichungssystem u_i + v_j = c_ij mit allen i,j, deren x_ij Basisvariable ist. Das lineare Gleichungssystem enthält ",
						null, 0, null);
		description
				.addCodeLine(
						"m+n Variablen u_i und v_j. Da es m + n - 1 Gleichungen gibt, hat das LGS einen Freiheitsgrad. Daher kann man eine Variable u_i oder v_j = 0 setzen ",
						null, 1, null);
		description
				.addCodeLine(
						"(Die Auflösung wird vereinfacht, wenn man diejenige Variable wählt, die am häufigsten im linearen Gleichungssystem vorkommt).",
						null, 1, null);
		description
				.addCodeLine(
						"II. Berechne reduzierte Kosten r_ij = c_ij - u_i - v_j für alle Nichtbasisvariablen. ",
						null, 0, null);
		description
				.addCodeLine(
						"III. Sind alle r_ij >= 0 wurde die optimale Lösung schon gefunden --> Stop. Sonst fahre mit Schritt IV fort.",
						null, 0, null);
		description
				.addCodeLine(
						"IV. Basistausch: Aufnahme der NBV x_pq mit den kleinsten reduzierten Kosten (größter negativer Wert) in die Basis. Dadurch entsteht genau ein Kreis im Transportgraph.",
						null, 0, null);
		description
				.addCodeLine(
						"Die maximal zulässige Transportmenge wird entlang des Kreises umverteilt.",
						null, 1, null);
		description
				.addCodeLine(
						"V. Beginne mit der neuen zulässigen Basislösung bei Schritt I.",
						null, 0, null);
		lang.nextStep("Beschreibung der Algorithmus-Schritte");
		if (sindFragenErlaubt) {
			// Erster Fragenblock
			Random rand = new Random();
			int randomInt = rand.nextInt(5);
			String question;
			String firstAnswer;
			String secondAnswer;
			String thirdAnswer;
			String solutionFirstQ;
			String solutionSecondQ;
			String solutionThirdQ;

			switch (randomInt) {
			case 0:
				question = "Was ist das Ziel der MODI Methode?";
				firstAnswer = "Minimierung der Transportkosten";
				secondAnswer = "Minimierung der Transportmengen";
				thirdAnswer = "Maximierung der Transportkosten	";
				solutionFirstQ = "Korrekte Antwort. Ziel der MODI Methode ist es bei gegebenen Angebots- und Nachfragemengen sowie den Transportkosten je Einheit, einen kostenminimalen Transportplan zu entwickeln.";
				solutionSecondQ = "Die Antwort ist leider falsch. Die richtige Antwort wäre gewesen: Minimierung der Transportkosten. Ziel der MODI Methode ist es bei gegebenen Angebots- und Nachfragemengen sowie den Transportkosten je Einheit, einen kostenminimalen Transportplan zu entwickeln.";
				solutionThirdQ = "Die Antwort ist leider falsch. Die richtige Antwort wäre gewesen: Minimierung der Transportkosten. Ziel der MODI Methode ist es bei gegebenen Angebots- und Nachfragemengen sowie den Transportkosten je Einheit, einen kostenminimalen Transportplan zu entwickeln.";
				break;
			case 1:
				question = "Welche dieser Voraussetzungen muss erfüllt sein, damit die MODI Methode ausgeführt werden kann?";
				firstAnswer = "Es muss eine zulässige Basislösung vorliegen.";
				secondAnswer = "Die Nachfragemenge muss größer als die Angebotsmenge sein.";
				thirdAnswer = "Die Nachfragemenge muss größer als die Transportmenge sein.";
				solutionFirstQ = "Korrekte Antwort. Die MODI Methode benötigt zum Ausführen eine bereits ermittelte Basislösung. Dazu können Verfahren wie die Vogelsche Approximationsmethode oder das Spaltenminimumverfahren eingesetzt werden.";
				solutionSecondQ = "Die Antwort ist leider falsch. Die Nachfragemenge muss genauso groß sein wie die Angebotsmenge. Die richtige Antwort wäre gewesen: Es muss eine zulässige Basislösung vorliegen. Die MODI Methode benötigt zum Ausführen eine bereits ermittelte Basislösung. Dazu können Verfahren wie die Vogelsche Approximationsmethode oder das Spaltenminimumverfahren eingesetzt werden.";
				solutionThirdQ = "Die Antwort ist leider falsch. Die Transportmenge muss genauso groß sein wie dieNachfrage- und Angebotsmenge. Die richtige Antwort wäre gewesen: Es muss eine zulässige Basislösung vorliegen. Die MODI Methode benötigt zum Ausführen eine bereits ermittelte Basislösung. Dazu können Verfahren wie die Vogelsche Approximationsmethode oder das Spaltenminimumverfahren eingesetzt werden.";
				break;
			case 2:
				question = "Was sagen reduzierte Kosten von r_12  = -3  aus?";
				firstAnswer = "Die Gesamtkosten lassen sich durch eine zusätzlich transportierte Einheit von Anbieter 1 zu Nachfrager 2 um 3 Einheiten reduzieren.";
				secondAnswer = "Die Gesamtkosten erhöhen sich bei einer zusätzlich transportierten Einheit von Anbieter 1 zu Nachfrager 2 um 3 Einheiten.";
				thirdAnswer = "Die Gesamtkosten ändern sich bei einer zusätzlich transportierten Einheit von Anbieter 1 zu Nachfrager 2 nicht.";
				solutionFirstQ = "Korrekte Antwort. Negative reduzierte Kosten sagen aus, dass sich die Gesamtkosten um den jeweiligen Betrag für eine Einheit reduzieren lassen.";
				solutionSecondQ = "Die Antwort ist leider falsch. Die richtige Antwort wäre gewesen: Die Gesamtkosten lassen sich durch eine zusätzlich transportierte Einheit von Anbieter 1 zu Nachfrager 2 um 3 Einheiten reduzieren. Negative reduzierte Kosten sagen aus, dass sich die Gesamtkosten um den jeweiligen Betrag für eine Einheit reduzieren lassen.";
				solutionThirdQ = "Die Antwort ist leider falsch. Die richtige Antwort wäre gewesen: Die Gesamtkosten lassen sich durch eine zusätzlich transportierte Einheit von Anbieter 1 zu Nachfrager 2 um 3 Einheiten reduzieren. Negative reduzierte Kosten sagen aus, dass sich die Gesamtkosten um den jeweiligen Betrag für eine Einheit reduzieren lassen.";
				break;
			case 3:
				question = "Die Gesamtkosten lassen sich durch eine zusätzlich transportierte Einheit von Anbieter 2 zu Nachfrager 3 um 4 Einheiten reduzieren. Welche Aussage ist korrekt?";
				firstAnswer = "Reduzierte Kosten r_23 = - 4";
				secondAnswer = "Reduzierte Kosten r_23 =  4 ";
				thirdAnswer = "Reduzierte Kosten r_32 = - 4 ";
				solutionFirstQ = "Korrekte Antwort. Negative reduzierte Kosten sagen aus, dass sich die Gesamtkosten um den jeweiligen Betrag für eine Einheit reduzieren lassen. Der erste Index steht zudem für den Anbieter und der zweite für den Nachfrager.";
				solutionSecondQ = "Die Antwort ist leider falsch. Die richtige Antwort wäre gewesen: Reduzierte Kosten r_23 = - 4. Negative reduzierte Kosten sagen aus, dass sich die Gesamtkosten um den jeweiligen Betrag für eine Einheit reduzieren lassen. Der erste Index steht zudem für den Anbieter und der zweite für den Nachfrager.";
				solutionThirdQ = "Die Antwort ist leider falsch. In diesem Fall müsste die Aussage lauten: Die Gesamtkosten lassen sich durch eine zusätzlich transportierte Einheit von Anbieter 3 zu Nachfrager 2 um 4 Einheiten reduzieren.  Die richtige Antwort wäre gewesen: Reduzierte Kosten r_23 = - 4. Negative reduzierte Kosten sagen aus, dass sich die Gesamtkosten um den jeweiligen Betrag für eine Einheit reduzieren lassen. Der erste Index steht zudem für den Anbieter und der zweite für den Nachfrager.";
				break;
			case 4:
				question = "Wann ist der kostenminimale Transportplan gefunden?";
				firstAnswer = "Wenn alle reduzierten Kosten größer oder gleich 0 sind.";
				secondAnswer = "Wenn alle Nichtbasisvariablen zu Basisvariablen geworden sind. ";
				thirdAnswer = "Wenn alle Basisvariablen größer oder gleich 0 sind.";
				solutionFirstQ = "Korrekte Antwort. Sind alle reduzierten Kosten größer oder gleich 0, lässt sich keine Verbesserung der Kosten mehr erzielen.";
				solutionSecondQ = "Die Antwort ist leider falsch. Die Zahl der Basisvariablen ändert sich während des Verfahrens nicht, da in jedem Durchlauf eine bestehende Basisvariable durch eine neue ersetzt wird. Die richtige Antwort wäre gewesen: Wenn alle reduzierten Kosten größer oder gleich 0 sind. Sind alle reduzierten Kosten größer oder gleich 0, lässt sich keine Verbesserung der Kosten mehr erzielen.";
				solutionThirdQ = "Die Antwort ist leider falsch. Die Basisvariablen entsprechen der transportierten Menge, daher sind die Basisvariablen immer größer oder gleich 0.  Die richtige Antwort wäre gewesen: Wenn alle reduzierten Kosten größer oder gleich 0 sind. Sind alle reduzierten Kosten größer oder gleich 0, lässt sich keine Verbesserung der Kosten mehr erzielen.";
				break;
			default:
				question = "Was ist das Ziel der MODI Methode?";
				firstAnswer = "Minimierung der Transportkosten";
				secondAnswer = "Minimierung der Transportmengen";
				thirdAnswer = "Maximierung der Transportkosten	";
				solutionFirstQ = "Korrekte Antwort. Ziel der MODI Methode ist es bei gegebenen Angebots- und Nachfragemengen sowie den Transportkosten je Einheit, einen kostenminimalen Transportplan zu entwickeln.";
				solutionSecondQ = "Die Antwort ist leider falsch. Die richtige Antwort wäre gewesen: Minimierung der Transportkosten. Ziel der MODI Methode ist es bei gegebenen Angebots- und Nachfragemengen sowie den Transportkosten je Einheit, einen kostenminimalen Transportplan zu entwickeln.";
				solutionThirdQ = "Die Antwort ist leider falsch. Die richtige Antwort wäre gewesen: Minimierung der Transportkosten. Ziel der MODI Methode ist es bei gegebenen Angebots- und Nachfragemengen sowie den Transportkosten je Einheit, einen kostenminimalen Transportplan zu entwickeln.";
				break;
			}
			MultipleChoiceQuestionModel basics = new MultipleChoiceQuestionModel(
					"basics");
			basics.setPrompt(question);
			basics.addAnswer(firstAnswer, 5, solutionFirstQ);
			basics.addAnswer(secondAnswer, 0, solutionSecondQ);
			basics.addAnswer(thirdAnswer, 0, solutionThirdQ);
			lang.addMCQuestion(basics);
		}
		description.hide();
		algoStepsTitle.hide();
	}
}
