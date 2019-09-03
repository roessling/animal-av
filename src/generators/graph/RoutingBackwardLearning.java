/*
 * RoutingBackwardLearning.java
 * Amon Ditzinger, Dirk Schumacher, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.graph.helpersGraph.Graph;
import generators.graph.helpersGraph.Node;
import generators.graph.helpersGraph.Packet;
import interactionsupport.models.MultipleChoiceQuestionModel;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;

public class RoutingBackwardLearning implements ValidatingGenerator {
    private Language lang;
    private SourceCodeProperties sourceCodeHighlightColor;
    private String startNode;
    
    public RoutingBackwardLearning() {
    	init();
    }

    public void init(){
        lang = new AnimalScript("Routing mit Backward Learning ", "Amon Ditzinger, Dirk Schumacher", 800, 600);
        lang.setStepMode(true);

    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        sourceCodeHighlightColor = (SourceCodeProperties)props.getPropertiesByName("sourceCodeHighlightColor");
        startNode = (String)primitives.get("startNode");
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        animation();
        lang.finalizeGeneration();
        return lang.toString();
    }
    
    @Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
    	startNode = (String)primitives.get("startNode");
    	char testChar = startNode.charAt(0);
    	boolean returnVal = false;
    	char[] allowedChars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'};
    	for(int i = 0; i< allowedChars.length; i++) {
    		if(testChar == allowedChars[i]) {
    			returnVal = true;
    			break;
    		}
    	}
    	if(!returnVal) throw new IllegalArgumentException("Eingegebener Startknoten ist nicht korrekt! (Erlaubt sind A-I).");
    	
		return returnVal;
	}
    
    public int nodeToInt(Node n) {
    	char c = n.name;
    	return (int) (c) - 65;
    }
    
    public Node getNode(Graph g, char c) {
		for(int i = 0; i < g.nodes.size(); i++) if(g.nodes.get(i).name == c) return g.nodes.get(i);
		return null;
	}
    
    public void animation() {
    	Graph g = Graph.createLargeDefaultGraph();
    	g.nodes.get(3).routingTable.put('A', 3);
    	g.nodes.get(3).routingTable.put('B', 3);
    	g.nodes.get(3).routingTable.put('C', 1);
    	g.nodes.get(3).routingTable.put('D', 0);
    	g.nodes.get(3).routingTable.put('E', 2);
    	g.nodes.get(3).routingTable.put('F', 1);
    	g.nodes.get(3).routingTable.put('G', 2);
    	g.nodes.get(3).routingTable.put('H', 4);
    	g.nodes.get(3).routingTable.put('I', 3);
    	
    	g.nodes.get(5).routingTable.put('A', 3);
    	g.nodes.get(5).routingTable.put('B', 5);
    	g.nodes.get(5).routingTable.put('C', 2);
    	g.nodes.get(5).routingTable.put('D', 1);
    	g.nodes.get(5).routingTable.put('E', 2);
    	g.nodes.get(5).routingTable.put('F', 0);
    	g.nodes.get(5).routingTable.put('G', 3);
    	g.nodes.get(5).routingTable.put('H', 1);
    	g.nodes.get(5).routingTable.put('I', 2);
    	
    	char c = this.startNode.charAt(0);
    	backwardLearning(g, c);
    }
    
    public void setColor(algoanim.primitives.Graph g, Color c, int except) {
    	for(int i = 0; i < 9; i++) {
    		if(i == except) continue;
    		g.setNodeHighlightFillColor(i, c, null, null);
    	}
    }
    
    @SuppressWarnings("unused")
	public void backwardLearning(Graph graph, char startNode) {
    	
    	RectProperties titleRecPr = new RectProperties();
        titleRecPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        titleRecPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.lightGray);
        Rect titleRec = lang.newRect(new Coordinates(35,75), new Coordinates(435, 120) , "titleRec", null, titleRecPr);
        
        TextProperties titlePr = new TextProperties();
        titlePr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 25));
        Text title = lang.newText(new Coordinates(40, 80), "Routing: Backward Learning", "Titel", null, titlePr);
    	
    	
    	SourceCode intro = lang.newSourceCode(new Coordinates(40, 150), "intro", null);
        intro.addCodeLine("Der nachfolgende Algorithmus, Routing mit Backward Learning, ist ein adaptiver, isolierter Routing Algortihmus.", null, 0, null);
        intro.addCodeLine("Das bedeutet, dass die Knoten ihre Routing-Tabellen nicht durch Routing Advertisements erstellen. Stattdessen lernen", null, 0, null);
        intro.addCodeLine("sie selbst die Wege und Distanzen durch einkommende Pakete. Dabei ist der Absender-Knoten", null, 0, null);
        intro.addCodeLine("und der Hop-Counter wichtig, der hierbei als Distanzma� dient.", null, 0, null);
        intro.addCodeLine("Kommt ein Paket an, wird es analysiert und dann gibt es drei moegliche Faelle:", null, 0, null);
        intro.addCodeLine("1. Es gibt keinen Eintrag fuer den Absenderknote -> Erstelle einen neuen Eintrag.", null, 0, null);
        intro.addCodeLine("2. Es gibt bereits einen Eintrag und die neue Distanz(Hop-Counter) ist geringer -> Update den Eintrag", null, 0, null);
        intro.addCodeLine("3. Es gibt bereits einen Eintrag und die neue Distanz(Hop-Counter) ist groe�er  -> Tue nichts.", null, 0, null);
        
        lang.nextStep("Intro");
        intro.hide();
        
        SourceCode legend = lang.newSourceCode(new Coordinates(40, 150), "legend", null);
        legend.addCodeLine("In der Animation werden folgende Farben zum Hervorheben der Knoten des Graphen verwendet." , null, 0, null);
        legend.addCodeLine("Der Knoten von dem die Pakete verschickt werden, wird zun�chst Rot markiert. Alle Knoten,", null, 0, null);
        legend.addCodeLine("die vom Startknoten (ROT) Pakete erhalten werden blau markiert.", null, 0, null);
        legend.addCodeLine("Auch die Pfade zu den blauen Knoten werden blau markiert. Nachdem alle Pakete verschickt wurden,", null, 0, null);
        legend.addCodeLine("ist der Sender-Knoten nicht mehr markiert.", null, 0, null);
        legend.addCodeLine("Nun werden der Reihe nach die Pakete der blau markierten Knoten abgearbeitet.", null, 0, null);
        legend.addCodeLine("Dazu �ndert der gerade aktiv bearbeitete Knoten seine Farbe von Blau zu Rot. Die blaue Kante ist nicht mehr markiert.", null, 0, null);
        legend.addCodeLine("Nachdem alle blauen Knoten abgearbeitet wurde, ist kein Knoten mehr markiert", null, 0, null);
        legend.addCodeLine("und der Algorithmus startet von vorne und markiert den n�chsten Knoten rot.", null, 0, null);
        legend.addCodeLine("Ein Knoten sendet nur dann Paktete an seine Nachbarn, wenn diese noch nie Pakete erhalten haben.", null, 0, null);
        legend.addCodeLine("Somit kann es vorkommen, dass ein Knoten zum Senden rot markiert wird und direkt wieder unmarkiert wird, da er nichts sendet.", null, 0, null);
        legend.addCodeLine("", null, 0, null);
        legend.addCodeLine("Hinweis: Um die Funktionsweise des Algortihmus besser zeigen zu k�nnen,", null, 0, null);
        legend.addCodeLine("haben die Knoten D und F schon gef�llte Routing-Tabellen.", null, 0, null);
        legend.addCodeLine("So wird sichergestellt, dass nicht immer neue Werte in die Tabellen geschrieben werden, sondern auch alte aktualisiert werden.", null, 0, null);
        
        lang.nextStep();
        legend.hide();
       
        SourceCodeProperties scProps = this.sourceCodeHighlightColor;
        if(scProps == null) {
          scProps = new SourceCodeProperties();
          scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        }
          scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
          scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
              new Font("Monospaced", Font.PLAIN, 12));

          scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        
          
        SourceCode sc = lang.newSourceCode(new Coordinates(40, 140), "Code", null, scProps);
        sc.addCodeLine("BackwardLearning(packet):", null, 0, null);  //0
        sc.addCodeLine("", null, 0, null);  //1
        sc.addCodeLine("// Extrahiere die wichtigen Paketinformationen", null, 1, null);  //2
        sc.addCodeLine("sourceNode = paket.getSourceNode()", null, 1, null);  //3
        sc.addCodeLine("predecessorNode = paket.getPredecessorNode()", null, 1, null);  //4
        sc.addCodeLine("newDistance = paket.getHopCounter()", null, 1, null);  //5
        sc.addCodeLine("", null, 0, null);  //6
        sc.addCodeLine("// Schaue, ob bereits ein Eintrag des SourceNodes besteht", null, 1, null);  //7
        sc.addCodeLine("if(sourceNode in routingTable):", null, 1, null);  //8
        sc.addCodeLine("// Es existiert ein Eintrag, schaue ob neue Route besser ist", null, 2, null);  //9
        sc.addCodeLine("distance = routingTable.getDistance(sourceNode)", null, 2, null);  //10
        sc.addCodeLine("if(newDistance < distance):", null, 2, null);  //11
        sc.addCodeLine("// Neue Distanz geringer, update den Tabelleneintrag", null, 3, null);  //12
        sc.addCodeLine("routingTable.updateEntry(sourceNode, predecessorNode, newDistance)", null, 3, null);  //13
        sc.addCodeLine("", null, 0, null);  //14
        sc.addCodeLine("// Neuer Eintrag nicht besser, update nicht", null, 2, null);  //15
        sc.addCodeLine("else:", null, 2, null);  //16
        sc.addCodeLine("skip", null, 3, null);  //17
        sc.addCodeLine("", null, 0, null);  //18
        sc.addCodeLine("// Es existiert kein Eintrag, also erstelle einen neuen", null, 1, null);  //19
        sc.addCodeLine("else:", null, 1, null);  //20
        sc.addCodeLine("routingTable.makeNewEntry(sourceNode, predecessorNode, newDistance)", null, 2, null);  //21
        sc.addCodeLine("", null, 0, null);  //22
        sc.addCodeLine("", null, 0, null);  //23
        sc.addCodeLine("// Sende Paket weiter", null, 1, null);  //24
        sc.addCodeLine("sendPacket(packet)", null, 1, null);  //25
        
        
    	int[][] adjacencyMatrix = new int[9][9];
    	adjacencyMatrix[0] = new int[] {0, 1, 1, 0, 0, 0, 0, 0, 0};
    	adjacencyMatrix[1] = new int[] {1, 0, 1, 0, 0, 0, 0, 0, 0};
    	adjacencyMatrix[2] = new int[] {1, 1, 0, 1, 0, 0, 0, 0, 0};
    	adjacencyMatrix[3] = new int[] {0, 0, 1, 0, 1, 1, 0, 0, 0};
    	adjacencyMatrix[4] = new int[] {0, 0, 0, 1, 0, 0, 1, 0, 0};
    	adjacencyMatrix[5] = new int[] {0, 0, 0, 1, 0, 0, 0, 1, 1};
    	adjacencyMatrix[6] = new int[] {0, 0, 0, 0, 1, 0, 0, 0, 1};
    	adjacencyMatrix[7] = new int[] {0, 0, 0, 0, 0, 1, 0, 0, 0};
    	adjacencyMatrix[8] = new int[] {0, 0, 0, 0, 0, 1, 1, 0, 0};
    	
    	Coordinates a = new Coordinates(650, 140);
    	Offset b = new Offset(100, 0, a, AnimalScript.DIRECTION_E);
    	Offset c = new Offset(50, 50, a, AnimalScript.DIRECTION_SE);
    	Offset d = new Offset(50, 100, a, AnimalScript.DIRECTION_SE);
    	Offset e = new Offset(0, 150, a, AnimalScript.DIRECTION_S);
    	Offset f = new Offset(100, 150, a, AnimalScript.DIRECTION_SE);
    	Offset g = new Offset(0, 200, a, AnimalScript.DIRECTION_S);
    	Offset h = new Offset(100, 200, a, AnimalScript.DIRECTION_SE);
    	Offset I = new Offset(0, 250, a, AnimalScript.DIRECTION_S);
    	
    	algoanim.util.Node[] nodes = new algoanim.util.Node[] {a, b, c, d, e, f, g, h, I};
    	
    	GraphProperties graphPr = new GraphProperties();
    	graphPr.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.WHITE);
    	
    	algoanim.primitives.Graph gr = lang.newGraph("Graph", adjacencyMatrix, nodes, new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I"}, null, graphPr);
    	for(int m = 0; m < 9; m++) for(int n = 0; n < 9; n++) gr.setEdgeHighlightPolyColor(m, n, Color.BLUE, null, null);
    	TextProperties textPackagePr = new TextProperties();
        textPackagePr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 15));
        Text incomingPackage = lang.newText(new Offset(450, 10, title, AnimalScript.DIRECTION_E), "Einkommendes Paket:", "incomingPackage", null, textPackagePr);
      
        Text sourceNodeText = lang.newText(new Offset(452, 30, title, AnimalScript.DIRECTION_SE), "Absender-Knoten", "sourceNodeText", null);
        Text predecessorText = lang.newText(new Offset(605, 30, title, AnimalScript.DIRECTION_SE), "Vorg�nger-Knoten", "predecessorText", null);
        Text hopCounterText = lang.newText(new Offset(755, 30, title, AnimalScript.DIRECTION_SE), "Hop-Counter", "hopCounterText", null);
      
        Rect packageRect1 = lang.newRect(new Offset(450, 50, title, AnimalScript.DIRECTION_SE), new Offset(600, 70, title, AnimalScript.DIRECTION_SE), "packageRect1", null);
        Rect packageRect2 = lang.newRect(new Offset(600, 50, title, AnimalScript.DIRECTION_SE), new Offset(750, 70, title, AnimalScript.DIRECTION_SE), "packageRect2", null);
        Rect packageRect3 = lang.newRect(new Offset(750, 50, title, AnimalScript.DIRECTION_SE), new Offset(900, 70, title, AnimalScript.DIRECTION_SE), "packageRect3", null);
      
        Text packageText1 = lang.newText(new Offset(520, 55, title, AnimalScript.DIRECTION_SE), "", "packageText1", null);
        Text packageText2 = lang.newText(new Offset(670, 55, title, AnimalScript.DIRECTION_SE), "", "packageText2", null);
        Text packageText3 = lang.newText(new Offset(820, 55, title, AnimalScript.DIRECTION_SE), "", "packageText3", null);
    	
    	Text routingTable = lang.newText(new Offset(450, 150, title, AnimalScript.DIRECTION_SE), "Routing Tabelle", "RT", null, textPackagePr);

    	Text ziel = lang.newText(new Offset(477, 190, title, AnimalScript.DIRECTION_SE), "Ziel", "Ziel", null);
    	Text distance = lang.newText(new Offset(520, 190, title, AnimalScript.DIRECTION_SE), "Distance", "Distance", null);
    	
    	Polyline down1 = lang.newPolyline(new algoanim.util.Node[] {new Offset(467, 190, title, AnimalScript.DIRECTION_SE),  new Offset(467, 390, title, AnimalScript.DIRECTION_SE)}, "down1", null);
    	Polyline down2 = lang.newPolyline(new algoanim.util.Node[] {new Offset(507, 190, title, AnimalScript.DIRECTION_SE),  new Offset(507, 390, title, AnimalScript.DIRECTION_SE)}, "down1", null);
    	Polyline right = lang.newPolyline(new algoanim.util.Node[] {new Offset(440, 205, title, AnimalScript.DIRECTION_SE),  new Offset(640, 205, title, AnimalScript.DIRECTION_SE)}, "down1", null);
    	
    	Text tableX = lang.newText(new Offset(450, 190, title, AnimalScript.DIRECTION_SE), "", "tableX", null);
    	
    	Text zielA = lang.newText(new Offset(480, 207, title, AnimalScript.DIRECTION_SE), "A", "A", null);
    	Text zielB = lang.newText(new Offset(480, 227, title, AnimalScript.DIRECTION_SE), "B", "B", null);
    	Text zielC = lang.newText(new Offset(480, 247, title, AnimalScript.DIRECTION_SE), "C", "C", null);
    	Text zielD = lang.newText(new Offset(480, 267, title, AnimalScript.DIRECTION_SE), "D", "D", null);
    	Text zielE = lang.newText(new Offset(480, 287, title, AnimalScript.DIRECTION_SE), "E", "E", null);
    	Text zielF = lang.newText(new Offset(480, 307, title, AnimalScript.DIRECTION_SE), "F", "F", null);
    	Text zielG = lang.newText(new Offset(480, 327, title, AnimalScript.DIRECTION_SE), "G", "G", null);
    	Text zielH = lang.newText(new Offset(480, 347, title, AnimalScript.DIRECTION_SE), "H", "H", null);
    	Text zielI = lang.newText(new Offset(480, 367, title, AnimalScript.DIRECTION_SE), "I", "I", null);
    	
    	Text distanceA = lang.newText(new Offset(520, 207, title, AnimalScript.DIRECTION_SE), "", "distanceA", null);
    	Text distanceB = lang.newText(new Offset(520, 227, title, AnimalScript.DIRECTION_SE), "", "distanceB", null);
    	Text distanceC = lang.newText(new Offset(520, 247, title, AnimalScript.DIRECTION_SE), "", "distanceC", null);
    	Text distanceD = lang.newText(new Offset(520, 267, title, AnimalScript.DIRECTION_SE), "", "distanceD", null);
    	Text distanceE = lang.newText(new Offset(520, 287, title, AnimalScript.DIRECTION_SE), "", "distanceE", null);
    	Text distanceF = lang.newText(new Offset(520, 307, title, AnimalScript.DIRECTION_SE), "", "distanceF", null);
    	Text distanceG = lang.newText(new Offset(520, 327, title, AnimalScript.DIRECTION_SE), "", "distanceG", null);
    	Text distanceH = lang.newText(new Offset(520, 347, title, AnimalScript.DIRECTION_SE), "", "distanceH", null);
    	Text distanceI = lang.newText(new Offset(520, 367, title, AnimalScript.DIRECTION_SE), "", "distanceI", null);
    	
    	lang.nextStep("Visualisierung");
    	
		LinkedList<Node> fifo = new LinkedList<>();
		Node n = getNode(graph, startNode);
		n.via = n;
		fifo.add(n);
		
		while(fifo.size() != 0) {
			Node no = fifo.getFirst();
			fifo.removeFirst();
			no.visited = true;
			
			setColor(gr, Color.RED, -1);
			gr.highlightNode(nodeToInt(no), null, null);
			setColor(gr, Color.BLUE, nodeToInt(no));
			lang.nextStep("Knoten " + no.name + " wird betrachtet");
			
			sc.highlight(25);
			int lastDistance = no.routingTable.get(n.name);
			int i;
		
			//gebe Nachbarn Pakete
			for(i = 0; i < no.neighbours.size(); i++) {
				Node nb = no.neighbours.get(i).neighbour;
				if(nb.visited) continue;
				else nb.visited = true;
				gr.highlightEdge(nodeToInt(no), nodeToInt(nb), null, null);
				gr.highlightNode(nodeToInt(nb), null, null);
				lang.nextStep();
				
				Packet p = new Packet(no, lastDistance + no.neighbours.get(i).distance);
				nb.packet = p;
				fifo.add(nb);
			}
			sc.unhighlight(25);
			//arbeite Pakete ab
			for(i = 0; i < graph.nodes.size(); i++) {
				Node node = graph.nodes.get(i);
				Packet p = node.packet;
				HashMap<Character, Integer> table = node.routingTable;
				if(p != null) {
					gr.unhighlightNode(nodeToInt(no), null, null);
					gr.unhighlightEdge(nodeToInt(no), nodeToInt(node), null, null);
					gr.setNodeHighlightFillColor(nodeToInt(node), Color.RED, null, null);
					
					tableX.setText(Character.toString(node.name), null, null);
					if(!(node.routingTable.get('A') > 1000)) distanceA.setText(Integer.toString(node.routingTable.get('A')), null, null);
					if(!(node.routingTable.get('B') > 1000)) distanceB.setText(Integer.toString(node.routingTable.get('B')), null, null);
					if(!(node.routingTable.get('C') > 1000)) distanceC.setText(Integer.toString(node.routingTable.get('C')), null, null);
					if(!(node.routingTable.get('D') > 1000)) distanceD.setText(Integer.toString(node.routingTable.get('D')), null, null);
					if(!(node.routingTable.get('E') > 1000)) distanceE.setText(Integer.toString(node.routingTable.get('E')), null, null);
					if(!(node.routingTable.get('F') > 1000)) distanceF.setText(Integer.toString(node.routingTable.get('F')), null, null);
					if(!(node.routingTable.get('G') > 1000)) distanceG.setText(Integer.toString(node.routingTable.get('G')), null, null);
					if(!(node.routingTable.get('H') > 1000)) distanceH.setText(Integer.toString(node.routingTable.get('H')), null, null);
					if(!(node.routingTable.get('I') > 1000)) distanceI.setText(Integer.toString(node.routingTable.get('I')), null, null);
					
					lang.nextStep();
					sc.highlight(0);
					lang.nextStep();
					sc.unhighlight(0);
					
					sc.highlight(2);
					lang.nextStep();
					sc.unhighlight(2);
					
					sc.highlight(3);
					packageText1.setText(Character.toString(n.name), null, null);
					lang.nextStep();
					sc.unhighlight(3);
					
					sc.highlight(4);
					packageText2.setText(Character.toString(p.lastNode.name), null, null);
					lang.nextStep();
					sc.unhighlight(4);
					
					sc.highlight(5);
					
					packageText3.setText(Integer.toString(p.sumDistance), null, null);
					lang.nextStep();
					sc.unhighlight(5);
					
					sc.highlight(7);
					sc.highlight(8);
					
					int tmp = 0;
					
					switch (n.name) {
						case 'A': 
							zielA.changeColor(null, Color.RED, null, null);
							distanceA.changeColor(null, Color.RED, null, null);
							tmp = node.routingTable.get('A');
							break;
						case 'B': 
							zielB.changeColor(null, Color.RED, null, null);
							distanceB.changeColor(null, Color.RED, null, null);
							tmp = node.routingTable.get('B');
							break;
						case 'C': 
							zielC.changeColor(null, Color.RED, null, null);
							distanceC.changeColor(null, Color.RED, null, null);
							tmp = node.routingTable.get('C');
							break;
						case 'D': 
							zielD.changeColor(null, Color.RED, null, null);
							distanceD.changeColor(null, Color.RED, null, null);
							tmp = node.routingTable.get('D');
							break;
						case 'E': 
							zielE.changeColor(null, Color.RED, null, null);
							distanceE.changeColor(null, Color.RED, null, null);
							tmp = node.routingTable.get('E');
							break;
						case 'F': 
							zielF.changeColor(null, Color.RED, null, null);
							distanceF.changeColor(null, Color.RED, null, null);
							tmp = node.routingTable.get('F');
							break;
						case 'G': 
							zielG.changeColor(null, Color.RED, null, null);
							distanceG.changeColor(null, Color.RED, null, null);
							tmp = node.routingTable.get('G');
							break;
						case 'H': 
							zielH.changeColor(null, Color.RED, null, null);
							distanceH.changeColor(null, Color.RED, null, null);
							tmp = node.routingTable.get('H');
							break;
						case 'I': 
							zielI.changeColor(null, Color.RED, null, null);
							distanceI.changeColor(null, Color.RED, null, null);
							tmp = node.routingTable.get('I');
							break;
					}
					
					lang.nextStep();
					sc.unhighlight(7);
					sc.unhighlight(8);
					boolean neu = false;
					boolean better = false, worse = false;
					if(tmp > 1000) {
						neu = true;
						sc.highlight(19);
						lang.nextStep();
						sc.unhighlight(19);
						
						sc.highlight(20);
						sc.highlight(21);
					}
					else {
						sc.highlight(9);
						
						lang.nextStep();
						sc.unhighlight(9);
						sc.highlight(10);
						
						lang.nextStep();
						sc.unhighlight(10);
						sc.highlight(11);
						lang.nextStep();
						sc.unhighlight(11);
						
						if(tmp > p.sumDistance) {
							better = true;
							sc.highlight(12);
							lang.nextStep();
							sc.unhighlight(12);
							sc.highlight(13);
						}
						else {
							worse = true;
							sc.highlight(15);
							lang.nextStep();
							sc.unhighlight(15);
							sc.highlight(16);
							sc.highlight(17);
						}
					}
					
					if(table.get(n.name) > p.sumDistance) {
						table.put(n.name, p.sumDistance);
						node.via = p.lastNode;
						if(!(node.routingTable.get('A') > 1000)) distanceA.setText(Integer.toString(node.routingTable.get('A')), null, null);
						if(!(node.routingTable.get('B') > 1000)) distanceB.setText(Integer.toString(node.routingTable.get('B')), null, null);
						if(!(node.routingTable.get('C') > 1000)) distanceC.setText(Integer.toString(node.routingTable.get('C')), null, null);
						if(!(node.routingTable.get('D') > 1000)) distanceD.setText(Integer.toString(node.routingTable.get('D')), null, null);
						if(!(node.routingTable.get('E') > 1000)) distanceE.setText(Integer.toString(node.routingTable.get('E')), null, null);
						if(!(node.routingTable.get('F') > 1000)) distanceF.setText(Integer.toString(node.routingTable.get('F')), null, null);
						if(!(node.routingTable.get('G') > 1000)) distanceG.setText(Integer.toString(node.routingTable.get('G')), null, null);
						if(!(node.routingTable.get('H') > 1000)) distanceH.setText(Integer.toString(node.routingTable.get('H')), null, null);
						if(!(node.routingTable.get('I') > 1000)) distanceI.setText(Integer.toString(node.routingTable.get('I')), null, null);
						lang.nextStep();
					}
					if(neu) {
						sc.unhighlight(20);
						sc.unhighlight(21);
					}
					else if(better) {
						sc.unhighlight(13);
					}
					else if(worse) {
						sc.unhighlight(16);
						sc.unhighlight(17);
					}
					
					switch (n.name) {
					case 'A': 
						zielA.changeColor(null, Color.BLACK, null, null);
						distanceA.changeColor(null, Color.BLACK, null, null);
						break;
					case 'B': 
						zielB.changeColor(null, Color.BLACK, null, null);
						distanceB.changeColor(null, Color.BLACK, null, null);
						break;
					case 'C': 
						zielC.changeColor(null, Color.BLACK, null, null);
						distanceC.changeColor(null, Color.BLACK, null, null);
						break;
					case 'D': 
						zielD.changeColor(null, Color.BLACK, null, null);
						distanceD.changeColor(null, Color.BLACK, null, null);
						break;
					case 'E': 
						zielE.changeColor(null, Color.BLACK, null, null);
						distanceE.changeColor(null, Color.BLACK, null, null);
						break;
					case 'F': 
						zielF.changeColor(null, Color.BLACK, null, null);
						distanceF.changeColor(null, Color.BLACK, null, null);
						break;
					case 'G': 
						zielG.changeColor(null, Color.BLACK, null, null);
						distanceG.changeColor(null, Color.BLACK, null, null);
						break;
					case 'H': 
						zielH.changeColor(null, Color.BLACK, null, null);
						distanceH.changeColor(null, Color.BLACK, null, null);
						break;
					case 'I': 
						zielI.changeColor(null, Color.BLACK, null, null);
						distanceI.changeColor(null, Color.BLACK, null, null);
						break;
					}
					lang.nextStep();
					distanceA.setText("", null, null);
					distanceB.setText("", null, null);
					distanceC.setText("", null, null);
					distanceD.setText("", null, null);
					distanceE.setText("", null, null);
					distanceF.setText("", null, null);
					distanceG.setText("", null, null);
					distanceH.setText("", null, null);
					distanceI.setText("", null, null);
				}
				gr.unhighlightNode(nodeToInt(node), null, null);
			}
			
			//L�sche alle Pakete
			for(i = 0; i < graph.nodes.size(); i++) graph.nodes.get(i).packet = null;
		}
		lang.nextStep("Fragen");
		MultipleChoiceQuestionModel question1 = new MultipleChoiceQuestionModel("Frage1");
		question1.setNumberOfTries(1);
		question1.setPrompt("Wenn die neue Distanz (Hop-Counters) gr��er der aktuellen Distanz ist, was wird gemacht?");
		question1.addAnswer("Der Wert der Tabelle wird geupdated.", 0, "Leider falsch, der neue Wert hat keinen Einfluss.");
		question1.addAnswer("Es wird ein neuer Eintrag erstellt.", 0, "Leider falsch, der neue Wert hat keinen Einfluss.");
		question1.addAnswer("Der neue Wert hat keinen Einfluss auf die Routing-Tabelle.",1, "Richtig!");
		lang.addMCQuestion(question1);
		lang.nextStep();
		
		MultipleChoiceQuestionModel question2 = new MultipleChoiceQuestionModel("Frage2");
		question2.setNumberOfTries(1);
		question2.setPrompt("Wenn die neue Distanz (Hop-Counters) kleiner der aktuellen Distanz ist, was wird gemacht?");
		question2.addAnswer("Der Wert der Routing-Tabelle wird geupdated auf den neuen Wert.", 1, "Richtig!");
		question2.addAnswer("Es wird ein neuer Eintrag erstellt.", 0, "Leider falsch, der Wert der Routing-Tabelle wird geupdated. ");
		question2.addAnswer("Der neue Wert hat keinen Einfluss auf die Routing-Tabelle.",0, "Leider falsch, der Wert der Routing-Tabelle wird geupdated.");
		lang.addMCQuestion(question2);
		lang.nextStep();
		
		MultipleChoiceQuestionModel question3 = new MultipleChoiceQuestionModel("Frage3");
		question3.setNumberOfTries(1);
		question3.setPrompt("Wenn die neue Distanz (Hop-Counters) gleich der aktuellen Distanz ist, was wird gemacht?");
		question3.addAnswer("Der Wert der Tabelle wird geupdated.", 0, "Leider falsch, der neue Wert hat keinen Einfluss.");
		question3.addAnswer("Es wird ein neuer Eintrag erstellt.", 0, "Leider falsch, der neue Wert hat keinen Einfluss.");
		question3.addAnswer("Der neue Wert hat keinen Einfluss auf die Routing-Tabelle.",1, "Richtig!");
		lang.addMCQuestion(question3);
		lang.nextStep();
		
		
		lang.hideAllPrimitives();
		title.show();
		titleRec.show();
		
		SourceCode outro = lang.newSourceCode(new Coordinates(40, 150), "intro", null);
        outro.addCodeLine("Sie haben nun gesehen, wie Routing mit Backward Learning anahand der, in den Paketen enthaltenden Infos", null, 0, null);
        outro.addCodeLine("die Routing-Tabelle erlernen kann. Ihnen ist vielleicht aufgefallen, dass sie teilweise nicht aktuelle", null, 0, null);
        outro.addCodeLine("Werte enthalten. Dies kann zum Beispiel dadurch geschehen, dass es Knoten gab, die einer bessere Route", null, 0, null);
        outro.addCodeLine("boten, aber dann ausgefallen sind.", null, 0, null);
        outro.addCodeLine("Um gegen die nicht aktuellen Werte in den Routing-Tabellen vorzugehen, muessen sie periodisch geloescht werden.", null, 0, null);
        outro.addCodeLine("Hier ist wichtig wie oft man das macht. Loescht man sie zu oft, so kann dies die Performance des Routing", null, 0, null);
        outro.addCodeLine("negativ Beeinflussen. Loescht man sie zu selten ist die Gefahr der falschen Eintraege groe�er.", null, 0, null);
        outro.addCodeLine("Es muss ein guter Ausgleich gefunden werden um die optimale Performanz zu bieten.", null, 0, null);
        outro.addCodeLine("Vielen Dank fuer Ihre Aufmerksamkeit!", null, 0, null);
        
        lang.nextStep("Outro");
        outro.hide();
        
        MultipleChoiceQuestionModel question4 = new MultipleChoiceQuestionModel("Frage4");
        question4.setNumberOfTries(1);
		question4.setPrompt("Wie wird gegen nicht mehr aktuelle Werte in den Routing-Tabellen vorgegeangen?");
		question4.addAnswer("Der Algortihmus merkt das von alleine und loescht sie.", 0, "Leider falsch, die Routing-Tabellen werden periodisch geloescht.");
		question4.addAnswer("Nichts, sie haben keinen Einfluss.", 0, "Leider falsch, die Routing-Tabellen werden periodisch geloescht.");
		question4.addAnswer("Die Routing-Tabellen werden periodisch geloescht.",1, "Richtig!");
		lang.addMCQuestion(question4);

	}
    
    public String getName() {
        return "Routing mit Backward Learning ";
    }

    public String getAlgorithmName() {
        return "Routing: Backward Learning";
    }

    public String getAnimationAuthor() {
        return "Amon Ditzinger, Dirk Schumacher";
    }

    public String getDescription(){
        return "Die nachfolgende Animation soll ihnen Routing mit Backward Learning pr&auml;sentieren. "
 +"\n"
 +"\n"
 +"Dies ist ein adaptiver isolierter Routing Algorithmus. Dabei werden die Routing-Tabelle nicht anhand von "
 +"\n"
 +"Routing-Advertisements erstellt, sondern die Knoten lernen diese durch die eingehenden Pakete."
 +"\n"
 +" "
 +"\n"
 +"Ein Knoten schaut sich beim Empfangen eines Paketes den Absenderknoten und den aktuellen Hop-Counter an und "
 +"\n"
 +"vergleicht, ob dieser in seiner Routing-Tabelle enthalten ist. Ist dies nicht der Fall,  speichert er die neue Route."
 +"\n"
 +"Ist jedoch schon ein Eintrag vorhanden,  wird anhand des Hop-Counters des aktuellen Paketes entschieden, ob die "
 +"\n"
 +"neue Route k&uuml;rzer ist. Ist dies der Fall, wird der Eintrag aktualisiert. "
 +"\n"
 +"\n"
 +"Zur besseren Verst&auml;ndlichkeit enthalten die Pakete nur den Absender-Knoten, den V&ouml;rg&auml;ngerknoten"
 +"\n"
 +"und den Hop-Counter. Zudem sendet der Anfangsknoten ein Paket an jeweils alle anderen Knoten. ";
    }

    public String getCodeExample(){
        return "BackwardLearning(packet):"
 +"\n"
 +"	// Extrahiere die wichtigen Paketinformationen"
 +"\n"
 +"	sourceNode = paket.getSourceNode()"
 +"\n"
 +" 	predecessorNode = paket.getPredecessorNode()"
 +"\n"
 +"	newDistance = paket.getHopCounter()"
 +"\n"
 +"\n"
 +"	// Schaue, ob bereits ein Eintrag des SourceNodes besteht"
 +"\n"
 +" 	if(sourceNode in routingTable):"
 +"\n"
 +"		// Es existiert ein Eintrag, schaue ob neue Route besser ist"
 +"\n"
 +"		distance = routingTable.getDistance(sourceNode)"
 +"\n"
 +"		if(newDistance < distance):"
 +"\n"
 +"			// Neue Distanz geringer, update den Tabelleneintrag"
 +"\n"
 +"			routingTable.updateEntry(sourceNode, predecessorNode, newDistance)"
 +"\n"
 +"		"
 +"\n"
 +"		// Neuer Eintrag nicht besser, update nicht"
 +"\n"
 +"		else: "
 +"\n"
 +"			skip"
 +"\n"
 +"\n"
 +"	// Es existiert kein Eintrag, also erstelle einen neuen"
 +"\n"
 +"	else:"
 +"\n"
 +"		routingTable.makeNewEntry(sourceNode, predecessorNode, newDistance)"
 +"\n"
 +"	"
 +"\n"
 +"	// Sende das Paket weiter"
 +"\n"
 +"	sendPacket(packet)"
 +"\n";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}