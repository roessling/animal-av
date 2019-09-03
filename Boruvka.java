//package generators.graph.boruvka;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.AnswerModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

/**
 * 
 * @author Ahmed CHarfi und Jihed Ouni
 *
 */
public class Boruvka implements Generator {
	private Language lang;
	// private MatrixProperties matrixProps;
//	private TextProperties text;
	private int[][] adjMatrix;
//	private int numOfNodes;

	/**
	 * Constructor
	 */
	public Boruvka() {
		lang = new AnimalScript("Boruvka [DE]", "Ahmed Charfi , Jihed Ouni",
				800, 600);
		
		lang.setStepMode(true);
	}
/**
 * initialzation
 */
	public void init() {
		lang = new AnimalScript("Boruvka [DE]", "Ahmed Charfi , Jihed Ouni",
				800, 600);

	}
/**
 * 
 */
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		// matrixProps =
		// (MatrixProperties)props.getPropertiesByName("matrixProps");
//    TextProperties textProps = (TextProperties) props.getPropertiesByName("text");
		adjMatrix = (int[][]) primitives.get("adjMatrix");

		if (adjMatrix == null) {
			adjMatrix = getDefaultAdjMatrix(1);
		}

		start();
		findmst(props, primitives);

		return lang.toString();
	}
/**
 * Name 
 */
	public String getName() {
		return "Boruvka [DE]";
	}
/**
 * return Name Algorithm
 */
	public String getAlgorithmName() {
		return "Boruvka Algorithmus";
	}
	/**
	 * Author Namen
	 */

	public String getAnimationAuthor() {
		return "Ahmed Charfi , Jihed Ouni";
	}
/**
 * Description of Algorithm
 */
	public String getDescription() {
		return "Der Algorithmus von Boruvka ist ein  Algorithmus zur Ermittlung eines minimalen Spannbaums"
				+ "\n"
				+ "eines gewichteten, ungerichteten Graphen erw&aumlhnt, der Algorithmus von Boruvka."
				+ "\n"
				+ "Dieser stammt aus dem Jahre 1926 und ist aus Kruskals und Prims Algorithmus hervorgegangen."
				+ "\n"
				+ "Er behandelt den Spezialfall, dass die Kantengewichte paarweise verschieden sind."
				+ "\n"
				+ "\n"
				+ "Der Algorithmus betrachtet anfangs jeden Knoten als Baum bzw. isolierte  Komponente"
				+ "\n"
				+ "In jeder Iteration sucht sich jeder Knoten die Kante mit dem niedrigsten Wert,"
				+ "\n"
				+ "welche die aktuelle Komponente mit einer anderen Komponente verbindet. Diese"
				+ "\n"
				+ "Kante wird dann in den minimalen Spannbaum aufgenommen"
				+ "\n"
				+ "Dabei werden Kanten so hinzugenommen, dass stets zwei Komponenten immer nur"
				+ "\n"
				+ "durch eine Kante verbunden werden und auftretende Kreise aufgel&oumlst werden. Dieser"
				+ "\n"
				+ "Schritt wird solange wiederholt, bis nur noch eine Komponente existiert, die dann"
				+ "\n"
				+ "einen minimalen Spannbaum des Ausgangsgraphen bildet.";
	}
/**
 * code Example
 */
	public String getCodeExample() {
		return "Gegeben sei ein zusammenh&aumlngender, ungerichteter Graph G mit Knotenmenge V = {1,...,n} und"
				+ "\n"
				+ "die Gewichtsfunktion w : E->R , wobei verschiedene Kanten stets verschiedenes Gewicht haben."
				+ "\n"
				+ "\n"
				+ "Als Eingabe erh&aumllt der Algorithmus  den Graphen G mit seiner entsprechenden Gewichtsfunktion w."
				+ "\n"
				+ "Die Ausgabe ist der minimale Spannbaum T von G."
				+ "\n"
				+ "    (1) for i = 1 to n do Vi <-{i} "
				+ "\n"
				+ "    (2) T<- leer ; M <-{V1,..Vn}; "
				+ "\n"
				+ "    (3) while |T| < n-1 do"
				+ "\n"
				+ "    (4)       for U in M do"
				+ "\n"
				+ "    (5)         finde eine Kante e = uv mit u in U,"
				+ "\n"
				+ "                      v not in U und w(e) < w(e)"
				+ "\n"
				+ "                       für alle Kanten e = u v mit u not in U und  v not in U "
				+ "\n"
				+ "    (6)                 finde die Komponente U', die v enthält"
				+ "\n"
				+ "    (7)                 T <- T U {e};"
				+ "\n"
				+ "    (8)         end for"
				+ "\n"
				+ "    (9)          for U in M do v sei EndPunkt von e in V ohne S;"
				+ "\n"
				+ "    (10)           MERGE (U,U')"
				+ "\n"
				+ "    (11) end for" + "\n" + "\n";
	}
/**
 * FileExtension
 */
	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}
/**
 * ContentLocale
 */
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}
/**
 * Generator Type
 */
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
	}
/**
 * OutPut Language
 */
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}
/**
 * start the algorithm
 */
	private void start() {

		SourceCodeProperties scTitle = new SourceCodeProperties();
		scTitle.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scTitle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 18));

		scTitle.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		SourceCode sc = lang.newSourceCode(new Coordinates(90, 100), "title",
				null, scTitle);

		SourceCodeProperties scPresen1 = new SourceCodeProperties();
		scPresen1
				.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scPresen1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 16));

		scPresen1.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				Color.RED);
		scPresen1.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode sc1 = lang.newSourceCode(new Coordinates(90, 120),
				"Presnetation1", null, scPresen1);

		sc.addCodeLine("Der Algorithmus von Boruvka:", null, 0, null);
		sc1.addCodeLine("", null, 0, null);

		sc1.addCodeLine(
				"Der Algorithmus von Boruvka ermittelt einen minimalen Spannbaum in",
				null, 0, null);
		sc1.addCodeLine("einem gewichteten ungerichteten Graphen.", null, 0,
				null);
		sc1.addCodeLine("", null, 0, null);
		sc1.addCodeLine(
				"Der Algorithmus stammt aus dem Jahre 1926 und ist aus Kruskals und Prims Algorithmus hervorgegangen.",
				null, 0, null);
		sc1.addCodeLine(
				"Er behandelt den Spezialfall, dass die Kantengewichte paarweise verschieden sind.",
				null, 0, null); // 0

		lang.nextStep();
		sc1.hide();
		// ///////////////////////////////////////////////////////////////////////

		SourceCode sc2 = lang.newSourceCode(new Coordinates(90, 120),
				"sourceCode", null, scPresen1);

		sc2.addCodeLine(" 		", null, 0, null);

		sc2.addCodeLine(
				"Der Algorithmus betrachtet anfangs jeden Knoten als Baum bzw. isolierte Komponente.",
				null, 0, null);
		sc2.addCodeLine(" ", null, 0, null);
		sc2.addCodeLine(
				"In jeder Iteration sucht der Algorithmus für jeden Knoten die Kante mit der niedrigsten",
				null, 0, null);

		sc2.addCodeLine(
				"Gewichtung, welche die aktuelle Komponente mit einer anderen Komponente verbindet.",
				null, 0, null);
		sc2.addCodeLine(
				"Diese Kante wird dann in den minimalen Spannbaum aufgenommen.",
				null, 0, null); // 0
		sc2.addCodeLine(
				"Dabei werden Kanten so hinzugenommen, dass stets zwei Komponenten immer nur",
				null, 0, null); // 0
		sc2.addCodeLine(
				"durch eine Kante verbunden werden und auftretende Kreise aufgelöst werden. ",
				null, 0, null); // 0
		sc2.addCodeLine(
				"Dieser Schritt wird solange wiederholt, bis nur noch eine Komponente existiert,",
				null, 0, null); // 0
		sc2.addCodeLine(
				"die dann einen minimalen Spannbaum des Ausgangsgraphen bildet.				",
				null, 0, null); // 0

		lang.nextStep();
		sc2.hide();
		// ///////////////////////////////////////////////////////////////////////
		// Pseudo Code

		SourceCodeProperties scPseudCo = new SourceCodeProperties();
		scPseudCo
				.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scPseudCo.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 16));

		scPseudCo.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				Color.RED);
		scPseudCo.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode sc3 = lang.newSourceCode(new Coordinates(90, 120),
				"sourceCode", null, scPseudCo);

		// Add the lines to the SourceCode object.
		// Line, name, indentation, display dealy
		
		
		sc3.addCodeLine("", null, 0, null);
		sc3.addCodeLine("PSEUDO_CODE", null, 0, null);
		sc3.addCodeLine(
				" Gegeben sei ein zusammenhängender, ungerichteter Graph G mit Knotenmenge V = {1,...,n} und",
				null, 0, null);
		sc3.addCodeLine(
				" die Gewichtsfunktion w : E->R , wobei verschiedene Kanten stets verschiedenes Gewicht haben.",
				null, 0, null);

		sc3.addCodeLine("", null, 0, null);
		sc3.addCodeLine(
				" Als Eingabe erhält der Algorithmus  den Graphen G mit seiner entsprechenden Gewichtsfunktion w.",
				null, 0, null);
		sc3.addCodeLine(" Die Ausgabe ist der minimale Spannbaum T von G.",
				null, 0, null);

		sc3.addCodeLine("  (1) for i = 1 to n do Vi <-{i} ;", null, 0, null);
		sc3.addCodeLine("  (2) T<- leer ; M <-{V1,..Vn}; ", null, 0, null);
		sc3.addCodeLine("  (3) while |T| < n-1 do", null, 0, null);
		sc3.addCodeLine("  (4)   for U in M do", null, 0, null); // 0
		sc3.addCodeLine("  (5)     finde eine Kante e = uv mit u in U,", null,
				0, null); // 0

		sc3.addCodeLine("           v not in U und w(e) < w(e' )", null, 0,
				null); // 0

		sc3.addCodeLine(
				"           für alle Kanten e' = u' v' mit u' not in U und  v' not in U ;",
				null, 0, null); // 0

		sc3.addCodeLine("  (6)      finde die Komponente U', die v enthält",
				null, 0, null); // 0
		sc3.addCodeLine("  (7)      T <- T U {e};", null, 0, null); // 0
		sc3.addCodeLine("  (8)   end for", null, 0, null); // 0
		sc3.addCodeLine(
				"  (9)   for U in M do v sei EndPunkt von e in V ohne S;",
				null, 0, null); // 0
		sc3.addCodeLine("  (10)    MERGE (U,U')", null, 0, null); // 0
		sc3.addCodeLine("  (11)  end for", null, 0, null); // 0

		lang.nextStep();
		sc3.hide();

	}
/**
 * fin minmal spannbaum
 * @param props
 * @param primitives
 */
	private void findmst(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		int[][] matrix = (int[][]) primitives.get("adjMatrix");

		if (matrix == null)
			matrix = getDefaultAdjMatrix();

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				
				//System.out.print(matrix[i][j] + "\t");
			}
			//System.out.println();
		}

		boolean isNull = true;
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++)
				if (matrix[i][j] != 0)
					isNull = false;

		if (isNull)
			matrix = getDefaultAdjMatrix();

		GraphProperties graphProps = getDefaultProperties(); // =
																// (GraphProperties)
																// props
		// .getPropertiesByName("matrixProps");

		// create the graph again in order to be able to set the graph
		// properties
		int size = matrix.length;

		Node[] graphNodes = new Node[size];
		String[] nodeLabels = new String[size];

		int startX = 20;
		int startY = 200;
		int x = startX;
		int y = startY;

		for (int i = 0; i < size; i++) {
			graphNodes[i] = new Coordinates(x, y);
			if (i < Math.round(size / 2) -1 ) {
				x = x + 150;
			}

			if (i == Math.round(size / 2) -1  ) {
				y = y + 250;
			}

			if (i > Math.round(size / 2) -1 ) {
				x = x - 150;
			}

		}
		// TO DO: allow user to modify this via props

		// define the names of the nodes
		char startChar = 65;

		for (int i = 0; i < size; i++) {
			nodeLabels[i] = startChar + "";
			startChar++;
		}

		graphProps.setName("Bovurka Algorithm");

		Graph g = lang.newGraph(this.getName(), matrix, graphNodes, nodeLabels,
				null, graphProps);

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if ((i != j) && (matrix[i][j] != 0)
						&& (matrix[i][j] != Integer.MAX_VALUE))
					g.setEdgeWeight(i, j, matrix[i][j], null, null);
			}
		}

		lang.nextStep();
		// ////////

		// sort adjazenzmatrix
		ArrayList<Integer> myListe = new ArrayList<Integer>();
	
		// iterate over all nodes
		int min;
		int jmin = 0;

		for (int i = 0; i < size; i++) {

			// find cheapest edge
			min = Integer.MAX_VALUE;

			for (int j = 0; j < size; j++) {

				if (j == i)
					matrix[i][j] = Integer.MAX_VALUE;

				if (matrix[i][j] < min && matrix[i][j] != 0) {
					min = matrix[i][j];
					jmin = j;
				}
			}
			// we found cheapest edge
			myListe.add(jmin);

		}

		

		// Take edge with minimum weight
		SourceCodeProperties scGraph1 = new SourceCodeProperties();
		scGraph1.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.red);
		scGraph1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 16));

		scGraph1.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scGraph1.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode scG1;

		int counter = 0;
		int sum = 0;

		ArrayList<Integer> listsum = new ArrayList<Integer>();
		ArrayList<Index> mylistsum = new ArrayList<Index>();

		scG1 = lang.newSourceCode(new Coordinates(500, 150), "sourceCode",
				null, scGraph1);

		scG1.addCodeLine("", null, 0, null);

		int mycounter = counter + 1;

		scG1.addCodeLine("Step" + mycounter + ":", null, 0, null);

		scG1.addCodeLine(" Der Algorithmus nimmt in der ersten Iteration jeden Knoten ", null, 0, null);
		scG1.addCodeLine(" unter die Lupe und ermittelt die günstigste Kante.",
				null, 0, null);

		Index myIndex;

		for (int i = 0; i < size; i++) 
		{
			if(i==0 || i ==1)
			{
			
			/*
			MultipleChoiceQuestionModel mc1 = new MultipleChoiceQuestionModel("MC1");
			mc1.addAnswer("anis", 5, "naja");
			mc1.addAnswer("manel", 5, "nama");
			*/
				
			//MultipleSelectionQuestionModel salih = new MultipleSelectionQuestionModel("123 viva l algerie");
			
			
			TrueFalseQuestionModel tf1 = new TrueFalseQuestionModel("TF1", true, 10);
			
//			String question1 = "ushih walla galet";
			
			//lang.addTFQuestion(q1);
			String ques ="choose the right answer";
			tf1.addAnswer(new AnswerModel("1", "wahr",5,"gut"));
			tf1.setGroupID("TF1"); //GR
			
			tf1.setFeedbackForAnswer(true,"gut");
			tf1.setFeedbackForAnswer(false, "naja");
			tf1.setCorrectAnswer(true);
			tf1.setPrompt(ques);
			
			
			
		    lang.addTFQuestion(tf1);
			
			
		//	System.err.println(tf1.getUserAnwerID());
	
			}
			
			
			scG1.addCodeLine(" Die günstigste Kante " + getAsChar(i) + ","
					+ getAsChar(myListe.get(i)) + " hat das Gewicht "
					+ matrix[i][myListe.get(i)], "Zeile3Step1", 0, null);

			myIndex = new Index(matrix[i][myListe.get(i)], i, myListe.get(i));

			scG1.highlight("Zeile3Step1");
			g.highlightEdge(i, myListe.get(i), null, null);
			g.highlightNode(i, null, null);
			g.highlightNode(myListe.get(i), null, null);

			boolean found = false;
			// check that element is not alredy contained
			for (int k = 0; k < mylistsum.size(); k++) {
				if (mylistsum.get(k).val == myIndex.val
						&& mylistsum.get(k).x == myIndex.y
						&& mylistsum.get(k).y == myIndex.x) {
					found = true;
					break;
				}
			}

			if (!found) {
				sum = sum + matrix[i][myListe.get(i)];
				listsum.add(matrix[i][myListe.get(i)]);
				mylistsum.add(myIndex);
			}
			lang.nextStep();

		}
		scG1.addCodeLine(
				" Die günstigste Kanten werden in dem Spannbaum aufgenommen",
				"Zeile4Step1", 0, null);

		List<ArrayList<Integer>> components = getInitialComponents(myListe);

		lang.nextStep();

		scG1.addCodeLine(
				" Die Anzahl der Komponenten ist " + components.size(),
				"Zeile5Step1", 0, null);

		scG1.highlight("Zeile5Step1");

		lang.nextStep();

		scG1.hide();

		counter++;
		lang.nextStep();

		scG1 = lang.newSourceCode(new Coordinates(500, 150), "sourceCode",
				null, scGraph1);

		scG1.addCodeLine("", null, 0, null);

		while (components.size() > 1) {

			mycounter = counter + 1;

			scG1.addCodeLine("Step" + mycounter + ":", null, 0, null);

			scG1.addCodeLine(
					" Die Anzahl der Komponenten ist " + components.size() + ".",
					null, 0, null);
			scG1.addCodeLine(
					" Der Algorithmus versucht je zwei Komponenten mit der günstigsten Kante zu finden. ",
					null, 0, null);

			lang.nextStep();

			// iterate over all components, try to find cheapest edge to other
			// components.
			ArrayList<Integer> comp1, comp2;

			ArrayList<Index> connections = new ArrayList<Index>();

			//for (int i = 0; i < components.size(); i++) {
				//comp1 = components.get(i);
				comp1 = components.get(0);
				Index ind = new Index(0, 0, 0);

				for (int j = 1; j < components.size(); j++) 
				{
						comp2 = components.get(j);
						// for each node in comp1, find chepeast edge to nodes
						// of comp2.
						int curr1, curr2;

						for (int k = 0; k < comp2.size(); k++) {
							curr2 = comp2.get(k);

							for (int l = 0; l < comp1.size(); l++) {
								curr1 = comp1.get(l);
								// check if there is an edge connecting curr 1
								// and curr 2
								if (matrix[curr1][curr2] != 0) {
									ind = new Index(matrix[curr1][curr2],
											curr1, curr2);
									connections.add(ind);
								}
							}
						}
				}		
				
				Collections.sort(connections, ind);
				// get minimal connection
				scG1.addCodeLine(
						" Die günstigste Kante "
								+ getAsChar(connections.get(0).x)
								+ ","
								+ getAsChar(connections.get(0).y)
								+ " mit dem Gewicht "
								+ connections.get(0).val
								+ " wird in dem minimalen Spannbaum aufgenommen.",
						"mefteh", 0, null);

				scG1.highlight("mefteh");
				g.highlightEdge(connections.get(0).x,
						connections.get(0).y, null, null);
				g.highlightNode(connections.get(0).x, null, null);
				g.highlightNode(connections.get(0).y, null, null);

				lang.nextStep();
						
				sum = sum + connections.get(0).val;
				listsum.add(connections.get(0).val);
				mylistsum.add(connections.get(0));

				// unite components.
				this.MergeComponents(connections.get(0).x,
						connections.get(0).y, components);
				counter++;

					//}
				//}

			//}

		}
		g.hide();

		scG1.hide();

		SourceCode scy = lang.newSourceCode(new Coordinates(90, 120),
				"Presnetationsum", null, scGraph1);
		scy.addCodeLine("                        ", null, 0, null);

		scy.addCodeLine(" Wir haben nur noch eine Komponente. ", null, 0, null);
		scy.addCodeLine(
				" Der Algorithmus ist somit fertig.                       ",
				null, 0, null);
		scy.addCodeLine("                        ", null, 0, null);

		int j = 0;
		for (int i = 0; i < listsum.size(); i++) {
			j = i + 1;
			scy.addCodeLine(
					"                " + j + ". Kante   "
							+ getAsChar(mylistsum.get(i).x) + ","
							+ getAsChar(mylistsum.get(i).y) + " : "
							+ mylistsum.get(i).val + " +", null, 0, null);
		}
		scy.addCodeLine("                                ----- ", null, 0, null);
		scy.addCodeLine("Die Länge des minimalen Spannbaum = " + sum, null, 0,
				null);

		lang.nextStep();
		scy.hide();
		SourceCode sc4 = lang.newSourceCode(new Coordinates(90, 120),
				"Presnetation1", null, scGraph1);

		sc4.addCodeLine("", null, 0, null);
		sc4.addCodeLine("", null, 0, null);

		sc4.addCodeLine(
				"Der Algorithmus von Boruvka bestimmt einen minimal Spannbaum mit",
				null, 0, null);
		sc4.addCodeLine("der Zeitkomplexität O(|V*V| log|V|) .", null, 0, null);
		sc4.addCodeLine(
				"Die Suche nach der Kante mit dem geringsten Gewicht, die mit jeder Komponente",
				null, 0, null);
		sc4.addCodeLine(
				"inzident ist, benötigt O(V*V) Vergleiche. Die Anzahl der Komponenten reduziert sich",
				null, 0, null);
		sc4.addCodeLine(
				"dabei in jeder Iteration um den Faktor zwei, die Anzahl der Zusammenhangskomponenten",
				null, 0, null); // 0

		sc4.addCodeLine(
				"wird also mindestens halbiert. Folglich sind O(log |V |) Iterationen nötig, um",
				null, 0, null); // 0

		sc4.addCodeLine("den minimalen Spannbaum zu ermitteln.", null, 0, null); // 0
		sc4.addCodeLine(
				"Somit ergibt sich eine Laufzeit von O(|V*V| log |V |)", null,
				0, null);
		
		lang.finalizeGeneration();
System.err.println(lang.toString());
	}
/**
 * Initial components
 * @param myListe
 * @return
 */
	private List<ArrayList<Integer>> getInitialComponents(
			ArrayList<Integer> myListe) {
//		int numComp = myListe.size();

		/*
		 * //eliminate duplicates for(int i = 0 ; i < myListe.size() ; i++) {
		 * int tmp = myListe.get(i);
		 * 
		 * for(int j = 0 ; j < myListe.size() ; j++) { if(myListe.get(j) == i) {
		 * numComp = numComp - 1; continue; } }
		 * 
		 * }
		 */

		List<ArrayList<Integer>> components = new ArrayList<ArrayList<Integer>>();

		// put each minimum edge in a separate component

		for (int i = 0; i < myListe.size(); i++) {

			ArrayList<Integer> comp = new ArrayList<Integer>();

			int tmp = myListe.get(i);
			comp.add(i);
			comp.add(tmp);

			components.add(comp);
		}

		// eliminate duplicates

		int size = components.size();

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {

				if ((j != i)
						&& (components.get(i).containsAll(components.get(j)))) {

					components.remove(j);
					size = components.size();
				}
			}
		}

		// eliminate duplicates from myListe

		for (int i = 0; i < myListe.size(); i++) {
			if (myListe.get(myListe.get(i)) == i) {
				myListe.set(i, -1);
			}
		}

		MergeComponents(components);

		return components;

	}
/**
 * Merge components
 * @param x
 * @param y
 * @param components
 */
	private void MergeComponents(int x, int y,
			List<ArrayList<Integer>> components) {

		// check for common nodes between minimum edges and unite components
		ArrayList<Integer> comp1, comp2;

		for (int l = 0; l < components.size(); l++) {
			comp1 = components.get(l);

			// search for comp1 which contains x
			if (!comp1.contains(x))
				continue;

			// search for comp2 which contains y
			for (int j = 0; j < components.size(); j++) {
				comp2 = components.get(j);

				// search for comp2 which contains y
				if (!comp2.contains(y) || j ==l)
					continue;

				// if we found the min edge
				if (comp1.contains(x) && comp2.contains(y)) {
					// add all elements of comp2 to comp1
					for (int m = 0; m < comp2.size() ; m++)
					{
						if(!comp1.contains(comp2.get(m)))
							comp1.add(comp2.get(m));
					}

					// remove comp2 from components
					components.remove(j);

					return;
				}
			}
		}

	}

	/**
	 * Merge componenets
	 * @param components
	 */
	private void MergeComponents(List<ArrayList<Integer>> components) {

		// check for common nodes between minimum edges and unite components
		ArrayList<Integer> comp1, comp2;
		int curr; 
		
		for (int l = 0; l < components.size(); l++) {
			comp1 = components.get(l);

			// check if we find a node of comp1 in another component
			for(int k= 0 ; k < comp1.size(); k++)
			{
				curr = comp1.get(k);
		
				for (int j = 0; j < components.size(); j++) 
				{
				
					comp2 = components.get(j);
					
					if ((j == l) || !comp2.contains(curr))
					{
						continue;
					}
					else
					{
						//we need to merge
						comp1.addAll(comp2);
						components.remove(j);
						return;
					}
				}	
			}
		}
	}
/**
 * get char
 * @param ind
 * @return retChar
 */
	public char getAsChar(int ind) {
		char retChar = (char) (65 + ind);
		return retChar;
	}
/**
 * 
 * @param mat
 * @param val
 * @return the index
 */
	public Index getIndex(int[][] mat, int val) {
		Index ind = new Index(-1, -1);

		for (int i = 0; i < mat.length; i++)
			for (int j = 0; j < mat.length; j++) {
				if (mat[i][j] == val) {
					ind = new Index(i, j);
					return ind;
				}
			}
		return ind;
	}
/**
 * main method
 * @param args
 */
	public static void main(String[] args) {
		Boruvka b = new Boruvka();
		b.init();
//    b.start();

		Hashtable<String, Object> primitives = new Hashtable<String, Object>();
		primitives.put("adjMatrix", getDefaultAdjMatrix());
		AnimationPropertiesContainer props = new AnimationPropertiesContainer();
		TextProperties tp = new TextProperties("text");
		props.add(tp);
    b.generate(props,primitives);

		b.findmst(props, primitives);

		System.out.println(b.lang);
	}

	public static GraphProperties getDefaultProperties() {

		GraphProperties graphProps = new GraphProperties();
		graphProps.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.black);
		graphProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.red);
		graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
		graphProps
				.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.red);

		graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				Color.red);
		graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);

		return graphProps;

	}
/**
 * 
 * @param matrix
 * @return the number of edges
 */
	public int getNumEdges(int[][] matrix) {
		int counter = 0;

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (i != j && matrix[i][j] != 0 && i < j) {
					counter++;
				}

			}
		}
		return counter;
	}
	/**
	 * 
	 * @return the defaultMatrix
	 */
	public static int[][] getDefaultAdjMatrix()
	{
		return getDefaultAdjMatrix(3);
	}
	/**
	 * some tests 
	 * @param index
	 * @return the default matrix
	 */
	public static int[][] getDefaultAdjMatrix(int index) {
		int[][] AdjMatrix;
		if (index == 0) {
			AdjMatrix = new int[5][5];
			for (int i = 0; i < AdjMatrix.length; i++) {
				for (int j = 0; j < AdjMatrix.length; j++) {
					if (i == j) {
						AdjMatrix[i][j] = 0;
					}

				}
			}
			AdjMatrix[0][1] = 35;
			AdjMatrix[1][0] = 35;
			AdjMatrix[0][3] = 40;
			AdjMatrix[3][0] = 40;
			AdjMatrix[1][3] = 25;
			AdjMatrix[3][1] = 25;
			AdjMatrix[1][2] = 10;
			AdjMatrix[2][1] = 10;
			AdjMatrix[2][3] = 20;
			AdjMatrix[3][2] = 20;
			AdjMatrix[2][4] = 30;
			AdjMatrix[4][2] = 30;
			AdjMatrix[3][4] = 15;
			AdjMatrix[4][3] = 15;
			//
		}
		else if(index ==1)
		{
			AdjMatrix = new int[6][6];
			for (int i = 0; i < AdjMatrix.length; i++) {
				for (int j = 0; j < AdjMatrix.length; j++) {
					if (i == j) {
						AdjMatrix[i][j] = 0;
					}

				}
			}
			AdjMatrix[0][1] = 5;
			AdjMatrix[1][0] = 5;
			AdjMatrix[1][2] = 7;
			AdjMatrix[2][1] = 7;
			AdjMatrix[2][3] = 6;
			AdjMatrix[3][2] = 6;
			AdjMatrix[3][4] = 8;
			AdjMatrix[4][3] = 8;
			AdjMatrix[4][5] = 4;
			AdjMatrix[5][4] = 4;
			AdjMatrix[5][0] = 9;
			AdjMatrix[0][5] = 9;
			//

			
		}

		else if (index ==2) {
			AdjMatrix = new int[4][4];
			for (int i = 0; i < AdjMatrix.length; i++) {
				for (int j = 0; j < AdjMatrix.length; j++) {
					if (i == j) {
						AdjMatrix[i][j] = 0;
					}

				}
			}
			AdjMatrix[0][1] = 1;
			AdjMatrix[1][0] = 1;
			AdjMatrix[1][2] = 4;
			AdjMatrix[2][1] = 4;
			AdjMatrix[1][3] = 3;
			AdjMatrix[3][1] = 3;
			AdjMatrix[2][3] = 2;
			AdjMatrix[3][2] = 2;
			//
	       }
		else if (index == 3) {
			AdjMatrix = new int[8][8];
			for (int i = 0; i < AdjMatrix.length; i++) {
				for (int j = 0; j < AdjMatrix.length; j++) {
					if (i == j) {
						AdjMatrix[i][j] = 0;
					}

				}
			}
			AdjMatrix[0][1] = 4;
			AdjMatrix[1][0] = 4;
			AdjMatrix[1][2] = 11;
			AdjMatrix[2][1] = 11;
			AdjMatrix[2][3] = 3;
			AdjMatrix[3][2] = 3;
			AdjMatrix[3][4] = 12;
			AdjMatrix[4][3] = 12;
			AdjMatrix[4][5] = 2;
			AdjMatrix[5][4] = 2;
			AdjMatrix[5][6] = 10;
			AdjMatrix[6][5] = 10;
			AdjMatrix[6][7] = 1;
			AdjMatrix[7][6] = 1;
			AdjMatrix[7][0] = 9;
			AdjMatrix[0][7] = 9;
			
			AdjMatrix[2][6] = 7;
			AdjMatrix[6][2] = 7;
			//
		}

		
		
		else  {
			AdjMatrix = new int[3][3];
			for (int i = 0; i < AdjMatrix.length; i++) {
				for (int j = 0; j < AdjMatrix.length; j++) {
					if (i == j) {
						AdjMatrix[i][j] = 0;
					}

				}
			}
			AdjMatrix[0][1] = 24;
			AdjMatrix[1][0] = 24;
			AdjMatrix[0][2] = 3;
			AdjMatrix[2][0] = 3;
			AdjMatrix[1][2] = 14;
			AdjMatrix[2][1] = 14;
			//
	       }
	       

		
		return AdjMatrix;
	}

}
/**
 * class Index
 * @author ahmed
 *
 */
class Index implements Comparator<Index> {
	public int val;
	public int x;
	public int y;
/**
 * 
 * @param a
 * @param b
 */
	public Index(int a, int b) {
		this.x = a;
		this.y = b;
		this.val = 0;
	}
/**
 * 
 * @param val
 * @param a
 * @param b
 */
	public Index(int val, int a, int b) {
		this.val = val;
		this.x = a;
		this.y = b;
	}

	@Override
	/**
	 * 
	 */
	public int compare(Index o1, Index o2) {

		if (o1.val < o2.val)
			return -1;
		else if (o1.val == o2.val)
			return 0;
		else
			return 1;
	}
}
