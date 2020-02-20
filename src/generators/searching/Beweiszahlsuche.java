/*
 * Beweiszahlsuche.java
 * Joelle Heun, Jan Philipp Wagner, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.Graph;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.main.Animal;

public class Beweiszahlsuche implements ValidatingGenerator {
    private Language lang;
    private SourceCodeProperties sourceCode;
    private int[][]  adjacencymatrix;
  
  //Nodestructure for the algorithm
  	public class NodePNS {
  		Boolean isRoot; 
  		int depth; //Depth in the tree
  		int value; // 0 = ?, 1 = +; 2 = -
  		int pn; //Proofnumber
  		int dpn; // Disproofnumber
  		int type; // 0 = OR, 1 = AND
  		Boolean expanded;
  		int[] children; //index of the children nodes
  		int parent; //index of the parent node
  		int index;
  		int xPos;
  		int yPos;

  		public NodePNS(Boolean isRoot, int depth, int value, int pn, int dpn, int type, Boolean expanded,
  				int[] children, int parent, int index, int xPos, int yPos) {
  			this.isRoot = isRoot;
  			this.depth = depth;
  			this.value = value;
  			this.pn = pn;
  			this.dpn = dpn;
  			this.type = type;
  			this.expanded = expanded;
  			this.children = children;
  			this.parent = parent;
  			this.index = index;
  			this.xPos = xPos;
  			this.yPos = yPos;
  		}
  	}
    
    public void init(){
        lang = new AnimalScript("Beweiszahlsuche", "Joelle Heun, Jan Philipp Wagner", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        adjacencymatrix = (int[][])primitives.get("adjacencymatrix");

        final int[][] am = new int[adjacencymatrix.length][adjacencymatrix.length];
        for(int i = 0; i < am.length; i++) {
        	for(int h = 0; h < am.length; h++)
				am[i][h] = adjacencymatrix[i][h];
			}
        
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

		// **** PROPERTIES **** //

		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 20));
		tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		
		GraphProperties graphProps = new GraphProperties();
		graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, false);
		graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		
		TextProperties numberProps = new TextProperties();
		numberProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 17));
		numberProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		
		SourceCodeProperties scPNSProps = new SourceCodeProperties();
		scPNSProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scPNSProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 15));
		scPNSProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);

		// **** INTRODUCTION **** //
		
		//Headline
		Rect banner = lang.newRect(new Coordinates(40, 40), new Coordinates(340, 80), "Banner", null, rp);
		banner.changeColor("fillColor", Color.ORANGE, null, null);
		Text headline = lang.newText(new Coordinates(50, 50), "Proof Number Search", "Proof Number Search", null, tp);
		headline.setFont(new Font("Monospaced", Font.PLAIN, 25), null, null);

		//Introduction text
		lang.newText(new Coordinates(40, 85),
				"Die Proof Number Search oder auch Beweiszahlsuche, ist ein best-first Suchalgorithmus",
				"Introduction1", null, tp);
		lang.newText(new Coordinates(40, 105), "zum Finden des besten Pfades in einem Spielbaum.", "Introduction2",
				null, tp);
		lang.newText(new Coordinates(40, 125), "Der Algorithmus wurde von Victor Allis urspruenglich zur Loesung",
				"Introduction3", null, tp);
		lang.newText(new Coordinates(40, 145), "der Spiele 'Vier gewinnt' und 'Qubic' entwickelt.", "Introduction4",
				null, tp);

		lang.nextStep("Explanation");

		//List of Primitives that should not hide
		List<Primitive> primitiveList = new ArrayList<Primitive>();
		primitiveList.add(headline);
		primitiveList.add(banner);

		lang.hideAllPrimitivesExcept(primitiveList);
		
		// **** EXPLANATION **** //

		//Create example graph
		int[][] admat = new int[3][3];
		admat[0][1] = 1;
		admat[0][2] = 1;
		admat[1][0] = 1;
		admat[2][0] = 1;

		Node[] nodes = new Node[3];
		nodes[0] = new Coordinates(800, 400);
		nodes[1] = new Coordinates(600, 500);
		nodes[2] = new Coordinates(1000, 500);

		String[] nodeLabels = new String[3];
		nodeLabels[0] = "?";
		nodeLabels[1] = "+";
		nodeLabels[2] = "-";

		Graph example = lang.newGraph("graph", admat, nodes, nodeLabels, null, graphProps);

		for (Node node : nodes) {
			example.setNodeRadius(node, 25, null, null);
		}

		//Proof- and Disproofnumbers of example graph
		lang.newText(new Coordinates(840, 370), "1", "ProofNumber 1", null, numberProps);
		lang.newText(new Coordinates(840, 430), "1", "DisproofNumber 1", null, numberProps);
		lang.newText(new Coordinates(640, 470), "0", "ProofNumber 2", null, numberProps);
		lang.newText(new Coordinates(640, 530), "INF", "DisproofNumber 2", null, numberProps);
		lang.newText(new Coordinates(1040, 470), "INF", "ProofNumber 3", null, numberProps);
		lang.newText(new Coordinates(1040, 530), "0", "DisproofNumber 3", null, numberProps);

		//Indication for OR and AND nodes
		lang.newText(new Coordinates(1200, 400), "OR", "OR Knoten", null, tp);
		lang.newText(new Coordinates(1200, 500), "AND", "AND Knoten", null, tp);

		//Explanation of graph setup
		lang.newText(new Coordinates(40, 85),
				"Der Baum ist nach Zuegen der 2 Spieler aufgebaut. Ein OR Knoten ist also der Spielzustand nach einem Zug von Spieler 2 und ein AND Knoten", "Explanation1",
				null, tp);
		lang.newText(new Coordinates(40, 105),
				"der Speilzustand nach einem Zug von Spieler 1. Ziel des Algorithmus ist es, den besten Pfad fuer Spieler 1 zu finden. Dabei aehnelt dieser stark der Apha-Beta-Suche.", "Explanation2",
				null, tp);
		lang.newText(new Coordinates(40, 125),
				"Im Gegensatz zu anderen Best-First-Algorithmen benoetigt die Beweiszahlsuche keine domaenenabhaengige heuristische Bewertungsfunktion, um den vielversprechendsten Knoten zu bestimmen.", "Explanation3",
				null, tp);
		lang.newText(new Coordinates(40, 145),
				"Die Entscheidung fuer den vielversprechensten Knoten haengt einmal von dem Verzweigungsfaktor jedes internen Knotens ab, sowie den Proof- und Disproofnumbers der Leafnodes ", "Explanation4",
				null, tp);
		lang.newText(new Coordinates(40, 180),
				"Eine Proofnumber ist die minimale Anzahl der Kindknoten die True sein muessen (also einen Gewinn darstellen) damit der Knoten selbst True ist.", "Explanation4",
				null, tp);
		lang.newText(new Coordinates(40, 200),
				"Eine Disproofnumber ist die minimale Anzahl der Kindknoten die False sein muessen (also eine Niederlage darstellen) damit der Knoten selbst False ist. ", "Explanation5",
				null, tp);
		lang.newText(new Coordinates(40, 235),
				"Der Algorithmus traversiert also den Baum und entscheidet abhaengig von den Proof- und Diproofnumbers",
				"Explanation6", null, tp);
		lang.newText(new Coordinates(40, 255),
				"sowie ob der aktuelle Knoten ein OR oder AND Knoten ist fuer den besten Knoten. Dabei werden Kindknoten immer wenn moeglich expanded",
				"Explanation7", null, tp);
		lang.newText(new Coordinates(40, 275), "und die Proof- und Disproofnumbers der Vorangegangenen Knoten mittels Backpropagation enstprechend aktualisiert. ",
				"Explanation8", null, tp);

		lang.nextStep();
		
		lang.hideAllPrimitivesExcept(primitiveList);

		// Parse adjacency matrix
		NodePNS[] graph = new NodePNS[adjacencymatrix.length];

		for (int i = 0; i < graph.length; i++) {
			graph[i] = new NodePNS(false, 0, 0, 1, 1, 0, false, null, 0, i, 0, 0);
		}

		//Setting variables of Nodes
		for (int i = 0; i < graph.length; i++) {
			
			//Is node root?
			if (i == 0)
				graph[i].isRoot = true;

			//Setting value of node
			graph[i].value = adjacencymatrix[i][i];

			//Setting children of node
			int counter = 0;
			int counterI = 0;
			for (int j = i + 1; j < graph.length; j++) {
				if (adjacencymatrix[i][j] == 1)
					counter++;
			}
			
			int[] index = new int[counter];
			for (int j = i + 1; j < graph.length; j++) {
				if (adjacencymatrix[i][j] == 1) {
					index[counterI] = j;
					counterI++;
				}
			}

			graph[i].children = index;

			//Setting parent of node
			if (!graph[i].isRoot) {
				for (int j = 0; j < i; j++) {
					if (adjacencymatrix[i][j] == 1)
						graph[i].parent = j;
				}
			}

			//Setting depth and type of node
			if (graph[i].isRoot)
				graph[0].depth = 0;
			if (graph[i].children != null) {
				for (int child : graph[i].children) {

					graph[child].depth = graph[i].depth + 1;
				}
			}
			if (graph[i].depth % 2 == 0) {
				graph[i].type = 0;
			} else {
				graph[i].type = 1;
			}
		}

		// Positioning nodes of new graph
		Node[] nodesNew = new Node[graph.length];
		int countN = 1;

		for (NodePNS node : graph) {
			int x = 0;
			int y = 0;

			if (node.isRoot) {
				nodesNew[0] = new Coordinates(1000, 40);
				node.xPos = 1000;
				node.yPos = 40;
			}

			int stepwidth = 0;
			int start = 0;

			if (node.children.length == 0)
				continue;
			if (node.children.length > 1) {
				stepwidth = (400/(node.depth+1))/(node.children.length -1);
				start = ((400/(node.depth+1))/2);
			}

			for (int i = 0; i < node.children.length; i++) {
				y = 40 + ((node.depth + 1) * 100);
				x = ((node.xPos - start) + (i * stepwidth));

				graph[countN].xPos = x;
				graph[countN].yPos = y;
				nodesNew[countN] = new Coordinates(x, y);
				countN++;
			}
		}

		String[] nodeLabelsNew = new String[graph.length];
		for (int i = 0; i < nodeLabelsNew.length; i++) {
			nodeLabelsNew[i] = "";
		}

		for (int i = 0; i < adjacencymatrix.length; i++) {
			am[i][i] = 0;
		}

		Graph g = lang.newGraph("graph", am, nodesNew, nodeLabelsNew, null, graphProps);
		
		//Hide all nodes except the root
		int[] hide1 = new int[nodesNew.length - 1];
		for (int i = 1; i <= hide1.length; i++) {
			hide1[i - 1] = i;
		}
		g.hideNodes(hide1, Timing.INSTANTEOUS, Timing.FAST);
		
		//Add graph to List of Primitives that should not hide
		primitiveList.add(g);

		//Setup the proof- and disproof numbers
		Text[] numbers = new Text[graph.length * 2];
		int j = 0;
		for (int i = 0; i < graph.length; i++) {
			numbers[j] = lang.newText(new Coordinates(graph[i].xPos + 20, graph[i].yPos - 20),
					displayPN(graph[i].pn), "pn", null, numberProps);
			j++;
			numbers[j] = lang.newText(new Coordinates(graph[i].xPos + 20, graph[i].yPos + 20),
					displayPN(graph[i].dpn), "dpn", null, numberProps);
			j++;
		}

		//Hide all the proof- and disproof numbers 
		for (Text number : numbers) {
			number.hide();
		}

		//Set the color for the node highlight
		for (int i = 0; i < nodesNew.length; i++) {
			g.setNodeHighlightFillColor(i, Color.ORANGE, null, null);
		}
		
		//Setting the Indicators for OR and AND nodes
		Text[] nodeType = new Text[graph[graph.length-1].depth+1];
		for(int i = 0; i < graph[graph.length-1].depth+1 ; i++) {
			if(i%2 == 0) {
				Text type = lang.newText(new Coordinates(1400, (40+i*100)), "OR", "OR Knoten", null);
				nodeType[i] = type;
				type.hide();
				primitiveList.add(type);
			}else{
				Text type = lang.newText(new Coordinates(1400, (40+i*100)), "AND", "AND Knoten", null);
				nodeType[i] = type;
				type.hide();
				primitiveList.add(type);
			}
		}
		
		//Create source code for pns
		SourceCode scPNS = lang.newSourceCode(new Coordinates(40, 85), "sourceCode", null, scPNSProps);

		scPNS.addCodeLine("Node PNS(Node root){", null, 0, null);
		scPNS.addCodeLine("  Node current = root;", null, 0, null);
		scPNS.addCodeLine("  Node mostProvingNode;", null, 0, null);
		scPNS.addCodeLine("", null, 0, null);
		scPNS.addCodeLine("  //setup for root node", null, 0, null);
		scPNS.addCodeLine("  evaluate(root);", null, 0, null);
		scPNS.addCodeLine("  setProofAndDisproofNumber(root);", null, 0, null);
		scPNS.addCodeLine("", null, 0, null);
		scPNS.addCodeLine("  //actual algorithm", null, 0, null);
		scPNS.addCodeLine("  while((root.proof != 0 && root.disproof != 0) &&", null, 0, null);
		scPNS.addCodeLine("        (oldCurrent != current || current.hasChildren)){", null, 0, null);
		scPNS.addCodeLine("    mostProvingNode = selectMostProvingNode(current);", null, 0, null);
		scPNS.addCodeLine("    expandNode(mostProvingNode);", null, 0, null);
		scPNS.addCodeLine("    current = updateAncestors(mostProvingNode, root);", null, 0, null);
		scPNS.addCodeLine("  }", null, 0, null);
		scPNS.addCodeLine("  return mostProvingNode;", null, 0, null);
		scPNS.addCodeLine("}", null, 0, null);
		
		Text currentVar = lang.newText(new Coordinates(700,40), "", "current", null);
	    Text mostProvingVar = lang.newText(new Coordinates(700,60), "", "mostProving", null);
	    nodeType[0].show();
	    int questionCounter = 0;
		
	    //**** ALGORITHM ****//
	    
	    scPNS.highlight(0);
	    
		lang.nextStep("Initialization");

		scPNS.toggleHighlight(0, 1);
		g.highlightNode(0, null, null);
	
		NodePNS current = graph[0];
		currentVar.setText("current node: " + String.valueOf(current.index), null, null);

		lang.nextStep();
		
		scPNS.toggleHighlight(1, 2);
		
		NodePNS mostProving = null;
		mostProvingVar.setText("most proving node: ", null, null);
		
		lang.nextStep();
		
		scPNS.toggleHighlight(2, 5);
		
		lang.nextStep();
		
		g.setNodeLabel(0, evaluate(graph[0]), Timing.INSTANTEOUS, Timing.FAST);
		
		lang.nextStep();
		
		scPNS.toggleHighlight(5, 6);
		
		lang.nextStep();
		
		setProofAndDisproofNumbers(graph[0], g, scPNSProps, numbers, graph, primitiveList);
		
		for (int i = 0; i < numbers.length; i++) {
			primitiveList.add(numbers[i]);
		}
		
		lang.hideAllPrimitivesExcept(primitiveList);
		
		scPNS.show();
		currentVar.show();
		mostProvingVar.show();
		
		lang.nextStep("Searching");
		
		scPNS.toggleHighlight(6, 9);
		
		NodePNS oldCurrent = null; 
		
		int counter = 0;
		while((graph[0].pn != 0 && graph[0].dpn != 0) && (oldCurrent != current || current.children.length != 0)){
			
			questionCounter++;
			
			lang.nextStep();
			
			scPNS.toggleHighlight(9, 11);

			FillInBlanksQuestionModel bestNode = new FillInBlanksQuestionModel("node"+Integer.toString(counter));
			bestNode.setPrompt("welchen Knoten wird der Algorithmus als den nächsten besten Knoten auswählen? Bitte gebe nur die Nummer des Knotens an.");
			bestNode.addAnswer("ans", selectMostProvingNodeQuestion(current, g, scPNSProps, numbers, graph, primitiveList), 1, "Richtig, der nächste Knoten der nun expandiert wird ist der Knoten " + selectMostProvingNodeQuestion(current, g, scPNSProps, numbers, graph, primitiveList));
			lang.addFIBQuestion(bestNode);
			
			lang.nextStep();
			
			mostProving = selectMostProvingNode(current, g, scPNSProps, numbers, graph, primitiveList);
			
			lang.nextStep();
			
			lang.hideAllPrimitivesExcept(primitiveList);
			
			scPNS.show();
			currentVar.show();
			mostProvingVar.setText("most proving node: " + String.valueOf(mostProving.index), null, null);
			mostProvingVar.show();
			
			lang.nextStep();
			
			scPNS.toggleHighlight(11, 12);
			
			lang.nextStep();
			
			expandNode(mostProving, g, scPNSProps, numbers, graph, primitiveList, nodeType);
			
			lang.hideAllPrimitivesExcept(primitiveList);
			scPNS.show();
			currentVar.show();
			mostProvingVar.show();
			
			lang.nextStep();
			
			scPNS.toggleHighlight(12, 13);
			
			lang.nextStep();
			
			oldCurrent = current;			
			current = updateAnchestors(mostProving, g, scPNSProps, numbers, graph, primitiveList);
	
			lang.hideAllPrimitivesExcept(primitiveList);
			g.unhighlightNode(current.index, null, null);
			scPNS.show();
			mostProvingVar.show();
			currentVar.setText("current node: " + String.valueOf(current.index), null, null);;
			currentVar.show();
			
			lang.nextStep();
			
			scPNS.toggleHighlight(13, 9);
			counter++;
		}
			
		lang.nextStep();
		
		scPNS.toggleHighlight(9, 15);
		
		lang.nextStep("Result");
		
		lang.hideAllPrimitivesExcept(primitiveList);
		
		if(graph[0].dpn != 0) {
			lang.newText(new Coordinates(40, 85), "wie man sehen kann an der Rueckgabe,", "Ergebnis", null, tp);
			lang.newText(new Coordinates(40, 105), "ist der beste Pfad fuer den Spieler 1 der zum Knoten " + String.valueOf(mostProving.index), "Ergebnis", null, tp);
			
			g.setEdgeHighlightPolyColor(mostProving.index, mostProving.parent ,Color.RED, null, null);
			g.highlightEdge(mostProving.index, 0, null, null);
			
		}else if(graph[0].dpn == 0) {
			lang.newText(new Coordinates(40, 85), "Da der Knoten nun die Disproofnumber 0 hat bedeutet das,", "Ergebnis loss", null,tp);
			lang.newText(new Coordinates(40, 105), "es handelt sich um einen sogennanten 'forced loss'", "Ergebnis loss", null,tp);
			lang.newText(new Coordinates(40, 125), "Das heißt, egal wie sich Spieler 1 entscheidet, verliert er.", "Ergebnis loss", null,tp);
		}
		
		lang.finalizeGeneration();
		
        return lang.toString();
    }

    public NodePNS updateAnchestors(NodePNS node, Graph g, SourceCodeProperties sp, Text[] numbers, NodePNS[] graph, List<Primitive> hide) {

		lang.hideAllPrimitivesExcept(hide);

		//Create source code for updateAnchestors
		SourceCode scUpdateAnchestors = lang.newSourceCode(new Coordinates(40, 85), "sourceCode", null, sp);

		scUpdateAnchestors.addCodeLine("Node updateAncestors(Node node, Node root){", null, 0, null);
		scUpdateAnchestors.addCodeLine("  while(true){", null, 0, null);
		scUpdateAnchestors.addCodeLine("    int oldProof = node.proof;", null, 0, null);
		scUpdateAnchestors.addCodeLine("    int oldDisproof = node.disproof,", null, 0, null);
		scUpdateAnchestors.addCodeLine("", null, 0, null);
		scUpdateAnchestors.addCodeLine("    setProofAndDisproofNumber(node);", null, 0, null);
		scUpdateAnchestors.addCodeLine("", null, 0, null);
		scUpdateAnchestors.addCodeLine("    //no changes on the path", null, 0, null);
		scUpdateAnchestors.addCodeLine("    if(oldProof == node.proof && oldDisproof == node.disproof){", null, 0, null);
		scUpdateAnchestors.addCodeLine("      return node;", null, 0, null);
		scUpdateAnchestors.addCodeLine("    }", null, 0, null);
		scUpdateAnchestors.addCodeLine("", null, 0, null);
		scUpdateAnchestors.addCodeLine("    //Delete (dis)proved trees", null, 0, null);
		scUpdateAnchestors.addCodeLine("    if(node.proof == 0 || node.disproof == 0){", null, 0, null);
		scUpdateAnchestors.addCodeLine("      node.deleteSubtree();", null, 0, null);
		scUpdateAnchestors.addCodeLine("    }", null, 0, null);
		scUpdateAnchestors.addCodeLine("", null, 0, null);
		scUpdateAnchestors.addCodeLine("    //node is the root", null, 0, null);
		scUpdateAnchestors.addCodeLine("    if(node == root){", null, 0, null);
		scUpdateAnchestors.addCodeLine("      return node;", null, 0, null);
		scUpdateAnchestors.addCodeLine("    }", null, 0, null);
		scUpdateAnchestors.addCodeLine("", null, 0, null);
		scUpdateAnchestors.addCodeLine("    //do it all again with parent of node ", null, 0, null);
		scUpdateAnchestors.addCodeLine("    node = node.parent;", null, 0, null);
		scUpdateAnchestors.addCodeLine("  }", null, 0, null);
		scUpdateAnchestors.addCodeLine("}", null, 0, null);

		scUpdateAnchestors.highlight(0);
		g.highlightNode(node.index, null, null);
		Text oldProofVar = lang.newText(new Coordinates(700, 40), "", "oldproof", null);
		Text oldDisProofVar = lang.newText(new Coordinates(700, 60), "", "olddisproof", null);
		int oldProof;
		int oldDisproof;

		lang.nextStep();

		scUpdateAnchestors.toggleHighlight(0, 1);

		lang.nextStep();

		while(true) {
			
			scUpdateAnchestors.toggleHighlight(1, 2);
			
			oldProof = node.pn;
			oldProofVar.setText("old proof number: " + displayPN(oldProof), null, null);
			
			lang.nextStep();
			
			scUpdateAnchestors.toggleHighlight(2, 3);
			
			oldDisproof = node.dpn;
			oldDisProofVar.setText("old disproof number: " + displayPN(oldDisproof), null, null);
			
			lang.nextStep();
			
			scUpdateAnchestors.toggleHighlight(3, 5);
			
			lang.nextStep();
			
			setProofAndDisproofNumbers(node, g, sp, numbers, graph, hide);
			
			lang.hideAllPrimitivesExcept(hide);
			scUpdateAnchestors.show();
			oldProofVar.show();
			oldDisProofVar.show();
		
			lang.nextStep();
			
			scUpdateAnchestors.toggleHighlight(5, 8);
			
			lang.nextStep();
			
			if(oldProof == node.pn && oldDisproof == node.dpn) {
				
				scUpdateAnchestors.toggleHighlight(8, 9);
				
				lang.nextStep();
				
				return node;	
			}
			
			scUpdateAnchestors.toggleHighlight(8, 13);
			
			lang.nextStep();
			
			if(node.pn == 0 || node.dpn == 0){
				
				scUpdateAnchestors.toggleHighlight(13, 14);
				
				lang.nextStep();
				
				for(int child: node.children){
				g.hideEdge(g.getNode(node.index), g.getNode(child), null, null);
				}
				
				node.children = new int[0];
				
				lang.nextStep();
				
				scUpdateAnchestors.unhighlight(14);
			}
			
			scUpdateAnchestors.unhighlight(13);
			scUpdateAnchestors.highlight(18);
			
			lang.nextStep();
			
			if(node.isRoot) {
				
				scUpdateAnchestors.toggleHighlight(18, 19);
				
				lang.nextStep();
				
				return node;
			}
		
			scUpdateAnchestors.toggleHighlight(18, 23);
			g.unhighlightNode(node.index, null, null);
			g.highlightNode(node.parent, null, null);
			node = graph[node.parent];
			
			lang.nextStep();
			
			scUpdateAnchestors.toggleHighlight(23, 1);
			
			lang.nextStep();
		}
	}	
		
	public void expandNode(NodePNS node, Graph g, SourceCodeProperties sp, Text[] numbers, NodePNS[] graph, List<Primitive> hide, Text[] nodeType) {

		lang.hideAllPrimitivesExcept(hide);

		//Create source code for expandNode
		SourceCode scExpandNode = lang.newSourceCode(new Coordinates(40, 85), "sourceCode", null, sp);

		scExpandNode.addCodeLine("void expandNode (Node node){", null, 0, null);
		scExpandNode.addCodeLine("  generateChildren(node);", null, 0, null);
		scExpandNode.addCodeLine("  foreach(child c of node){", null, 0, null);
		scExpandNode.addCodeLine("    evaluate(c);", "EN", 0, null);
		scExpandNode.addCodeLine("    setProofAndDisproofNumbers(c);", null, 0, null);
		scExpandNode.addCodeLine("    if((node.type == OR && c.proof == 0) || (node.type == AND && c.disproof == 0)){", null, 0, null);
		scExpandNode.addCodeLine("      break; //ignoring other children because there aren't important ", null, 0, null);
		scExpandNode.addCodeLine("    }", null, 0, null);
		scExpandNode.addCodeLine("  }", null, 0, null);
		scExpandNode.addCodeLine("  node.expanded = true;", null, 0, null);
		scExpandNode.addCodeLine("}", null, 0, null);

		scExpandNode.highlight(0);
		g.highlightNode(node.index, null, null);

		lang.nextStep();

		scExpandNode.toggleHighlight(0, 1);
				
		if(node.children.length > 0) {
			nodeType[graph[node.children[0]].depth].show();
		}
		
		for (int child : node.children) {
			g.showNode(child, null, null);

			lang.nextStep();
		}

		scExpandNode.toggleHighlight(1, 2);
		
		lang.nextStep();
		
		int counter = 0;

		for (int child : node.children) {
			
			counter++;
			
			scExpandNode.unhighlight(2);
			scExpandNode.unhighlight(5);
			scExpandNode.highlight(3);
			
			g.setNodeLabel(child, evaluate(graph[child]), null, null);

			lang.nextStep();

			scExpandNode.toggleHighlight(3, 4);
			
			lang.nextStep();
			
			setProofAndDisproofNumbers(graph[child], g, sp, numbers, graph, hide);
			g.unhighlightNode(child, null, null);

			lang.hideAllPrimitivesExcept(hide);
			scExpandNode.show();
			
			lang.nextStep();
			
			scExpandNode.toggleHighlight(4, 5);
			
			if((node.type == 0 && graph[child].pn == 0) || (node.type == 1 && graph[child].dpn ==  0)) {
				
				lang.nextStep();
				
				scExpandNode.toggleHighlight(5, 6);
				int[] childrenNew = new int[counter];
				for(int i = 0; i < counter; i++) {
					childrenNew[i] = node.children[i];
				}
				
				node.children = childrenNew;
				
				break;
			}
			
			lang.nextStep();
		}
		
		scExpandNode.toggleHighlight(5, 9);
		node.expanded = true;
		
		lang.nextStep();		
	}

	public NodePNS selectMostProvingNode(NodePNS node, Graph g, SourceCodeProperties sp, Text[] numbers,
			NodePNS[] graph, List<Primitive> hide) {

		lang.hideAllPrimitivesExcept(hide);

		//Create source code for selectMostProvingNode
		SourceCode scSelectMostProvingNode = lang.newSourceCode(new Coordinates(40, 85), "sourceCode", null, sp);

		scSelectMostProvingNode.addCodeLine("Node selectMostProvingNode(Node node){", null, 0, null);
		scSelectMostProvingNode.addCodeLine("  while(node.expanded){", null, 0, null);
		scSelectMostProvingNode.addCodeLine("    c = node.children //selecting first child of node", null, 0, null);
		scSelectMostProvingNode.addCodeLine("", null, 0, null);
		scSelectMostProvingNode.addCodeLine("    if(node.type == OR){", null, 0, null);
		scSelectMostProvingNode.addCodeLine("      while(c.proof != node.proof){", null, 0, null);
		scSelectMostProvingNode.addCodeLine("        c = c.sibling;", null, 0, null);
		scSelectMostProvingNode.addCodeLine("      }", null, 0, null);
		scSelectMostProvingNode.addCodeLine("    }else if(node.type == AND){", null, 0, null);
		scSelectMostProvingNode.addCodeLine("      while(c.disproof != node.disproof){", null, 0, null);
		scSelectMostProvingNode.addCodeLine("        c = c.sibling;", null, 0, null);
		scSelectMostProvingNode.addCodeLine("      }", null, 0, null);
		scSelectMostProvingNode.addCodeLine("    }", null, 0, null);
		scSelectMostProvingNode.addCodeLine("    node = c;", null, 0, null);
		scSelectMostProvingNode.addCodeLine("  }", null, 0, null);
		scSelectMostProvingNode.addCodeLine("  return node;", null, 0, null);
		scSelectMostProvingNode.addCodeLine("}", null, 0, null);
		
		scSelectMostProvingNode.highlight(0);
		g.highlightNode(node.index, null, null);
		NodePNS c;
		
		lang.nextStep();

		scSelectMostProvingNode.toggleHighlight(0, 1);
		
		lang.nextStep();
		
		while(node.expanded && node.children.length > 0) {
			
			scSelectMostProvingNode.toggleHighlight(1, 2);
			
			c = graph[node.children[0]];
			
			lang.nextStep();
			
			scSelectMostProvingNode.toggleHighlight(2, 4);
			
			lang.nextStep();
			
			if(node.type == 0) {
				
				scSelectMostProvingNode.toggleHighlight(4, 5);
				
				lang.nextStep();
				
				while(c.pn != node.pn) {
					
					scSelectMostProvingNode.toggleHighlight(5, 6);
					c = graph[c.index + 1];
					
					lang.nextStep();
					
					scSelectMostProvingNode.toggleHighlight(6, 5);
					
					lang.nextStep();
				}	
			}else {
				
				scSelectMostProvingNode.toggleHighlight(4, 8);
				
				lang.nextStep();
				
				scSelectMostProvingNode.toggleHighlight(8, 9);
				
				lang.nextStep();
				
				while(c.dpn != node.dpn) {
					
					scSelectMostProvingNode.toggleHighlight(9, 10);
					c = graph[c.index + 1];
					
					lang.nextStep();
					
					scSelectMostProvingNode.toggleHighlight(10, 9);
					
					lang.nextStep();
				}
			}
			
			scSelectMostProvingNode.unhighlight(5);
			scSelectMostProvingNode.unhighlight(9);
			scSelectMostProvingNode.highlight(13);
			g.unhighlightNode(node.index, null, null);
			
			node = c;
			
			g.highlightNode(node.index, null, null);
			
			lang.nextStep();
			
			scSelectMostProvingNode.toggleHighlight(13, 1);
			
			lang.nextStep();
		}
		
		scSelectMostProvingNode.toggleHighlight(1, 15);
		
		return node;
	}
	
	public void setProofAndDisproofNumbers(NodePNS node, Graph g, SourceCodeProperties sp, Text[] numbers,
			NodePNS[] graph, List<Primitive> hide) {

		lang.hideAllPrimitivesExcept(hide);

		//Create source code for setProofAndDisproofNumbers
		SourceCode scSetProofAndDisproofNumber = lang.newSourceCode(new Coordinates(40, 85), "sourceCode", null, sp);

		scSetProofAndDisproofNumber.addCodeLine("void setProofAndDisproofNumber(Node node){", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("  if(node.expanded){ //internal node", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("    if(node.type == AND){", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("      node.proof = 0;", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("      node.disproof = INFINITY;", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("      foreach(child c of node){", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("        node.proof = node.proof + c.proof;", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("        if(c.disproof < node.disproof){", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("          node.disproof = c.disproof;", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("        }", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("      }", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("    }else if(node.type == OR){", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("      node.proof = INFINITY;", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("      node.disproof = 0;", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("      foreach(child c of node){", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("        node.disproof = node.disproof + c.disproof;", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("        if(c.proof < node.proof){", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("          node.proof = c.proof;", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("        }", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("      }", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("    }", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("  }else if(!node.expanded){ //leaf ", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("    switch(node.value){", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("      case FALSE:", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("        node.proof = INFINITY;", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("        node.disproof = 0;", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("      case TRUE:", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("        node.proof = 0;", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("        node.disproof = INFINITY;", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("      case UNKNOWN:", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("        node.proof = 1;", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("        node.disproof = 1;", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("    }", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("  }", null, 0, null);
		scSetProofAndDisproofNumber.addCodeLine("}", null, 0, null);		
		
		scSetProofAndDisproofNumber.highlight(0);
		g.highlightNode(node.index, null, null);

		lang.nextStep();

		scSetProofAndDisproofNumber.toggleHighlight(0, 1);

		lang.nextStep();

		if (node.expanded && node.children.length > 0) {
			
			scSetProofAndDisproofNumber.toggleHighlight(1, 2);

			lang.nextStep();

			if (node.type == 1) {
				
				scSetProofAndDisproofNumber.toggleHighlight(2, 3);
				
				node.pn = 0;
				numbers[node.index * 2].setText(displayPN(node.pn), null, null);
				numbers[node.index * 2].show();

				lang.nextStep();

				scSetProofAndDisproofNumber.toggleHighlight(3, 4);
				
				node.dpn = 1000; //Ersatz fuer INF aus comparison Gruenden
				numbers[(node.index * 2) + 1].setText(displayPN(node.dpn), null, null);
				numbers[(node.index * 2) + 1].show();

				lang.nextStep();

				scSetProofAndDisproofNumber.toggleHighlight(4, 5);

				lang.nextStep();

				for (int child : node.children) {
					
					scSetProofAndDisproofNumber.unhighlight(5);
					scSetProofAndDisproofNumber.unhighlight(7);
					scSetProofAndDisproofNumber.unhighlight(8);
					scSetProofAndDisproofNumber.highlight(6);
					
					node.pn = node.pn + graph[child].pn;
					numbers[node.index * 2].setText(displayPN(node.pn), null, null);

					lang.nextStep();

					scSetProofAndDisproofNumber.toggleHighlight(6, 7);

					lang.nextStep();

					if (graph[child].dpn < node.dpn) {
						
						scSetProofAndDisproofNumber.toggleHighlight(7, 8);
						
						node.dpn = graph[child].dpn;
						numbers[(node.index * 2) + 1].setText(displayPN(node.dpn), null, null);
						
						lang.nextStep();
					}
				}
				
			}else{
					
				scSetProofAndDisproofNumber.toggleHighlight(2, 11);

				lang.nextStep();

				scSetProofAndDisproofNumber.toggleHighlight(11, 12);
				node.pn = 1000; //Ersatz fuer INF aus comparison Gruenden
				numbers[node.index * 2].setText(displayPN(node.pn), null, null);

				lang.nextStep();

				scSetProofAndDisproofNumber.toggleHighlight(12, 13);
				node.dpn = 0; 
				numbers[(node.index * 2) + 1].setText(displayPN(node.dpn), null, null);

				lang.nextStep();

				scSetProofAndDisproofNumber.toggleHighlight(13, 14);

				lang.nextStep();

				for (int child : node.children) {
					
					scSetProofAndDisproofNumber.unhighlight(14);
					scSetProofAndDisproofNumber.unhighlight(17);
					scSetProofAndDisproofNumber.highlight(15);
					
					node.dpn = node.dpn + graph[child].dpn;
					numbers[(node.index * 2) + 1].setText(displayPN(node.dpn), null, null);

					lang.nextStep();

					scSetProofAndDisproofNumber.toggleHighlight(15, 16);

					lang.nextStep();

					if (graph[child].pn < node.pn) {
						
						scSetProofAndDisproofNumber.toggleHighlight(16, 17);
						node.pn = graph[child].pn;
						numbers[(node.index * 2)].setText(displayPN(node.pn), null, null);

						lang.nextStep();
					}
				}
				
			}
		}else{
			
			scSetProofAndDisproofNumber.toggleHighlight(1, 21);

			lang.nextStep();

			scSetProofAndDisproofNumber.toggleHighlight(21, 22);

			lang.nextStep();

			switch (node.value) {

			case 2:
				
				scSetProofAndDisproofNumber.toggleHighlight(22, 23);
				
				lang.nextStep();
				
				scSetProofAndDisproofNumber.toggleHighlight(23, 24);
				
				node.pn = 1000;
				numbers[node.index * 2].setText(displayPN(node.pn), null, null);
				numbers[node.index * 2].show();

				lang.nextStep();

				scSetProofAndDisproofNumber.toggleHighlight(24, 25);
				
				node.dpn = 0;
				numbers[(node.index * 2) + 1].setText(displayPN(node.dpn), null, null);
				numbers[(node.index * 2) + 1].show();

				lang.nextStep();

				break;

			case 1:
				
				scSetProofAndDisproofNumber.toggleHighlight(22, 26);

				lang.nextStep();

				scSetProofAndDisproofNumber.toggleHighlight(26, 27);
				
				node.pn = 0;
				numbers[node.index * 2].setText(displayPN(node.pn), null, null);
				numbers[node.index * 2].show();

				lang.nextStep();

				scSetProofAndDisproofNumber.toggleHighlight(27, 28);
				
				node.dpn = 1000;
				numbers[(node.index * 2) + 1].setText(displayPN(node.dpn), null, null);
				numbers[(node.index * 2) + 1].show();

				lang.nextStep();

				break;

			case 0:
				
				scSetProofAndDisproofNumber.toggleHighlight(22, 29);

				lang.nextStep();

				scSetProofAndDisproofNumber.toggleHighlight(29, 30);
				
				node.pn = 1;
				numbers[node.index * 2].setText(displayPN(node.pn), null, null);
				numbers[node.index * 2].show();

				lang.nextStep();

				scSetProofAndDisproofNumber.toggleHighlight(30, 31);
				
				node.dpn = 1;
				numbers[(node.index * 2) + 1].setText(displayPN(node.dpn), null, null);
				numbers[(node.index * 2) + 1].show();

				lang.nextStep();

				break;

			default:
				node.pn = 1;
				node.dpn = 1;

				lang.nextStep();
			}
		}
	}
	
	public String selectMostProvingNodeQuestion(NodePNS node, Graph g, SourceCodeProperties sp, Text[] numbers,
			NodePNS[] graph, List<Primitive> hide) {
		
		NodePNS c;
		
		while(node.expanded && node.children.length > 0) {
			c = graph[node.children[0]];
			
			if(node.type == 0) {
				while(c.pn != node.pn) {
					c = graph[c.index + 1];
				}
			}else {
				while(c.dpn != node.dpn) {
					c = graph[c.index + 1];
				}
			}
			node = c;
		}
		return String.valueOf(node.index);
	}


	public String evaluate(NodePNS node) {
		if (node.value == 0) {
			return "?";
		} else if (node.value == 1) {
			return "+";
		} else if (node.value == 2) {
			return "-";
		} else {
			return "?";
		}
	}
	
	public String displayPN(int number) {
		if(number < 1000) {
			return String.valueOf(number);
		}else{
			return "INF";
		}
	}
    
    public String getName() {
        return "Beweiszahlsuche";
    }

    public String getAlgorithmName() {
        return "Beweiszahlsuche";
    }

    public String getAnimationAuthor() {
        return "Joelle Heun, Jan Philipp Wagner";
    }

    public String getDescription(){
        return "Die Proof Number Search oder auch Beweiszahlsuche, ist ein best-first Suchalgorithmus zum Finden des besten Pfades in einem Spielbaum."
 +"\n"
 +"Der Algorithmus wurde von Victor Allis urspruenglich zur Loesung der Spiele Vier gewinnt und Qubic entwickelt.";
    }

    public String getCodeExample(){
        return "Node PNS(Node root){"
 +"\n"
 +"  Node current = root;"
 +"\n"
 +"  Node mostProvingNode;"
 +"\n"
 +"\n"
 +"  evaluate(root); "
 +"\n"
 +"  setProofAndDisproofNumber(root);"
 +"\n"
 +"\n"
 +"  while(root.proof != 0 && root.disproof != 0){"
 +"\n"
 +"    mostProvingNode = selectMostProvingNode(current);"
 +"\n"
 +"    expandNode(mostProvingNode); "
 +"\n"
 +"    current = updateAncestors(mostProvingNode, root); "
 +"\n"
 +"  }"
 +"\n"
 +"  return mostProvingNode;"
 +"\n"
 +"}"
 +"\n";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
			throws IllegalArgumentException {
		
		int[][] matrix = (int[][]) arg1.get("adjacencymatrix");
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix.length; j++) {
				if(matrix[i][j] != 0 &&  matrix[i][j] != 1 && matrix[i][j] != 2) {
					return false;
				}
				if(i != j && matrix[i][j] == 2){
					return false;
				}
			}
		}
		
		return true;
	}
	
	public static void main(String[] args) {
		Generator generator = new Beweiszahlsuche(); // Generator erzeugen
		Animal.startGeneratorWindow(generator);// Animal mit Generator starten
	}
	
}