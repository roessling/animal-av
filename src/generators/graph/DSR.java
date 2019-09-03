package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JOptionPane;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Hidden;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;

public class DSR implements ValidatingGenerator {
	private String lastNode;
    private String firstNode;
	private Language lang;
	private int[][] adjazenz;
	private Graph graph;
	private Node[] nodes;
	private Node[] nodes_pre;
	private Node[] nodes_pre_corner;
	private String[] labels;
	private Node startNode = new Offset(-75,150, "Graph",AnimalScript.DIRECTION_NW);
	private Node startNode_corner = new Offset(75,200,"Graph",AnimalScript.DIRECTION_NW);
	private int[] visited;
	private ArrayList<Node> boxPositions;
	private ArrayList<HashMap<Rect, Text>> boxes;
	private HashMap<Rect, Text> copyWinner;
	private ArrayList<Integer> boxDrop;
	private ArrayList<StringBuffer> boxBuffer;
	private TextProperties typProps = new TextProperties();
	private TextProperties undefined = new TextProperties();
	private TextProperties trash = new TextProperties();
	private TextProperties winner = new TextProperties();
	private int origin;
	private int target;
	private AnimationPropertiesContainer props;
	private GraphProperties graphProb;
	private int createdBoxes;
	private Text createdBoxesInfo;	
    private SourceCodeProperties textCode;
    private SourceCodeProperties infoTexte;
    private Color farbeSieger;
    private Color farbeVerlierer;
    private TextProperties TextBoxen;
    private TextProperties headerProps = new TextProperties();
	private Text movedBoxesInfo;
	private int movedBoxes;
	private Text infoHeader;
	private int floodingStep;
	private Text floodingInfo;
	
 
	public void init(){
        lang = new AnimalScript("Dynamic Source Routing", "Maurice Wendt, Dominik Gopp", 800, 600);
		this.lang.setStepMode(true);
		boxPositions = new ArrayList<Node>();
		boxDrop = new ArrayList<>();
		boxes = new ArrayList<HashMap<Rect,Text>>();
		boxBuffer = new ArrayList<>();
		boxBuffer.add(new StringBuffer(""));
		createdBoxes=0;
		movedBoxes=-1;
		floodingStep=0;
    }
	public Node getNode(String label) {
		for (int i = 0; i < nodes.length; i++) {
			if(labels[i].equals(label))
				return nodes[i];
		}
		return null;
	}
	
  
	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
	    textCode = (SourceCodeProperties)props.getPropertiesByName("TextCode");
	    farbeSieger = (Color)primitives.get("FarbeSieger");
	    farbeVerlierer = (Color)primitives.get("FarbeVerlierer");
	    TextBoxen = (TextProperties)props.getPropertiesByName("TextBoxen");
	    headerProps = (TextProperties)props.getPropertiesByName("TextHeader");
	    infoTexte = (SourceCodeProperties) props.getPropertiesByName("infoTexte");
	    
	    Font f = (Font) headerProps.get("font");
	    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(f.getName(), Font.BOLD, 24));
  		f= (Font) textCode.get("font");
  		textCode.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(f.getName(),f.getStyle(),14));
  		this.props = props;
  		graphProb = (GraphProperties) props.getPropertiesByName("graph");
  		
 
  		changeToUndirected();
  		nodes = new Node[graph.getAdjacencyMatrix().length];
  		labels = new String[graph.getAdjacencyMatrix().length];
  		nodes_pre = new Node[nodes.length];
		nodes_pre_corner = new Node[nodes.length];
		
		Coordinates current;
		int minLeft = 1000;
		int minTop = 1000;
		for (int i = 0; i < graph.getAdjacencyMatrix().length; i++) {
			current = (Coordinates) graph.getNode(i);
			if(current.getX()<minLeft)
				minLeft = current.getX();
			if(current.getY()<minTop)
				minTop = current.getY();
		}
		int changeX = (minLeft < 75) ? 75-minLeft : 0; 
		int changeY = (minTop < 50) ? 50-minTop : 0;
  		for (int i = 0; i < graph.getAdjacencyMatrix().length; i++) {
			labels[i]=graph.getNodeLabel(i);
			current = (Coordinates) graph.getNode(i);
			nodes[i] = new Coordinates(current.getX()+changeX, current.getY()+changeY);
			nodes_pre[i] = new Coordinates(current.getX()+changeX-70, current.getY()+changeY+30);
			nodes_pre_corner[i] = new Coordinates(current.getX()+changeX+80, current.getY()+changeY+80);
		}
  		for (int i = 0; i < labels.length; i++) {
			if(graph.getNode(i).equals(graph.getStartNode()))
				firstNode = labels[i];
			if(graph.getNode(i).equals(graph.getTargetNode()))
				lastNode = labels[i];
		}
  		graph=lang.newGraph("routing_graph",graph.getAdjacencyMatrix(),nodes,labels,null,graphProb);
  		graph.hide();
  		visited = new int[adjazenz.length];
  		start(firstNode, lastNode);
        return lang.toString();
    }

	public AnimationProperties getProperty(String className ) {
  		for (AnimationProperties animationProperties : props) {
			switch (className) {
			case "GraphProperties":
				if(animationProperties instanceof GraphProperties)
					return (GraphProperties) animationProperties;
				
				break;

			default:
				break;
			}
		}
  		return null;
  	}
	public String toString(){
		return lang.toString();
	}
	
	public void start(String start, String goal) {
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 14));
		scProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		
		lang.newText(new Coordinates(25, 15), "DSR - Dynamic Source Routing", "header", null, headerProps);
		
		SourceCode infoText = lang.newSourceCode(new Offset(55, 120, "header", AnimalScript.DIRECTION_NW), "infotext", null, infoTexte);
		RectProperties rectPropInfo = new RectProperties();
		rectPropInfo.set("fillColor", Color.LIGHT_GRAY);
		rectPropInfo.set("filled", true);
		rectPropInfo.set("depth", 1);
		infoText.addMultilineCode(" Dynamic-Source-Routing (DSR) ist ein Routingprotokoll, welches überwiegend im Rahmen \n von Ad-Hoc-Netzwerken (z.B. Funknetzwerke mit zwei oder mehr mobilen Endgeräten) verwendet \n wird. Solche Netzwerke haben einen stark dynamischen Ursprung, da ihre Endgeräte nicht an \n festen Positionen gebunden sind und die Verbindung zwischen diesen nicht festgelegt ist. Das \n Routing-Protokoll kann jedoch auch problemlos auf statischen Netzwerken angewendet werden. \n \n Dynamic-Source-Routing ist aufgrund seiner Realisierung für Netzwerke bis zu 200 Knoten \n sinnvoll realisierbar. Höhere Skalierungen sind zwar möglich, erfordern jedoch \n eine Anpassung des Algorithmus. \n \n Eine wesentliche Besonderheit bei DSR ist, dass die Wegfindung durch einen Knoten \n (Source)selbst angestoßen wird. Man bezeichnet daher das Routing auch als reaktiv, da es \n nur dann stattfindet, wenn die entsprechende Routinginformation explizit benötigt wird. \n \n Zur Pfaderkennung wird Flooding verwendet. Dieses Verfahren kann in großen Netzwerken \n eine enorme Menge an Paketen verursachen, welche häufig nicht benötigt werden. Diesen \n Nachteil nimmt man jedoch in Kauf, da der Aufwand nur beim Verbindungsaufbau bewältigt \n werden muss und keine weiteren Informationen über das Netzwerk zur Verfügung stehen, die \n eine zuverlässige Optimierung ermöglichen. \n \n Während dem Flooding-Prozess sammeln alle beteiligten Teilnehmer die Informationen aus \n den Paketen, welche sie empfangen, um daraus wertvolle Hinweise über den Aufbau des \n Netzes zu erlangen. Häufig können dadurch Routen bereits erkannt werden und spätere \n Routinganfragen vermieden werden.", null, null);		

		Rect infoRect = lang.newRect(new Offset(-10,-10, "infotext", AnimalScript.DIRECTION_NW), new Offset(10,10,"infotext",AnimalScript.DIRECTION_SE), "info",null, rectPropInfo);
		lang.nextStep("Intro");
		infoRect.hide();
		infoText.hide();
		SourceCode sourceCodeRREQ = lang.newSourceCode(new Offset(550, 70, "header", AnimalScript.DIRECTION_NW), "code1", null, textCode);
		sourceCodeRREQ.addCodeLine(" 1. Gegeben ist ein Graph mit n Knoten. Die Topologie ist den Knoten nicht ", null, 0, null);
		sourceCodeRREQ.addCodeLine("    bekannt, sie kennen nur ihre Nachbarn.", null, 0, null);
		sourceCodeRREQ.addCodeLine(" 2. Ein Knoten "+start+" benötigt eine Route zu einem anderen Knoten "+goal+".", null, 0, null);
		sourceCodeRREQ.addCodeLine(" 3. Er erstellt dafür ein Route-Request-Paket RREQ mit einer eindeutigen ", null, 0, null);
		sourceCodeRREQ.addCodeLine("    ID, der Quelle, dem Ziel und seinem Namen als initialen Pfad", null, 0, null);
		sourceCodeRREQ.addCodeLine(" 4. Dieses Paket schickt er an alle seine Nachbarn.", null, 0, null);
		sourceCodeRREQ.addCodeLine(" 5. Wenn ein Knoten ein Paket erhält", null, 0, null);
		sourceCodeRREQ.addCodeLine("          a) Wenn er bereits ein Paket aus diesem RREQ erhalten hat", null, 0, null);
		sourceCodeRREQ.addCodeLine("                 i) verwirft er das Paket, das Paket wird als Verlierer ", null, 0, null);
		sourceCodeRREQ.addCodeLine("                    markiert (Farbe " +getColorNameFromRgb(farbeVerlierer.getRed(), farbeVerlierer.getGreen(), farbeVerlierer.getBlue())+")", null, 0, null);
		sourceCodeRREQ.addCodeLine("          b) Wenn er noch kein Paket aus diesem RREQ erhalten hat", null, 0, null);
		sourceCodeRREQ.addCodeLine("                 i)  hängt er seinen Namen an den bisherigen Pfad an", null, 0, null);
		sourceCodeRREQ.addCodeLine("                 ii) Wenn er nicht der Zielknoten ist:", null, 0, null);
		sourceCodeRREQ.addCodeLine("                       1. leitet er das erhaltene Paket oder ein dupliziertes", null, 0, null);
		sourceCodeRREQ.addCodeLine("                          Paket mit neuer ID an alle Nachbarn weiter, gehe zu 5.", null, 0, null);
		sourceCodeRREQ.addCodeLine("                 iii) Wenn er der Zielknoten ist:", null, 0, null);
		sourceCodeRREQ.addCodeLine("                       1. leitet er keine Pakete weiter und startet den ", null, 0, null);
		sourceCodeRREQ.addCodeLine("                          Antwort-Prozess. Das Paket wird als Gewinner-Paket ", null, 0, null);
		sourceCodeRREQ.addCodeLine("                          markiert (Farbe "+getColorNameFromRgb(farbeSieger.getRed(), farbeSieger.getGreen(), farbeSieger.getBlue())+")", null, 0, null);

		
		SourceCode sourceCodeRREP = lang.newSourceCode(new Offset(550, 70, "header", AnimalScript.DIRECTION_NW), "code2", null, textCode);
		sourceCodeRREP.addCodeLine(" Wenn der Route-Request-Prozess beendet ist, das Paket also am Zielknoten ", null, 0, null);
		sourceCodeRREP.addCodeLine(" angekommen ist, beginnt der Route-Respone-Prozess.", null, 0, null);
		sourceCodeRREP.addCodeLine("", null, 0, null);
		sourceCodeRREP.addCodeLine(" 1. Erstelle ein Route-Respone-Paket (RREP) mit getauschten Start- und ", null, 0, null);
		sourceCodeRREP.addCodeLine("    Zielknoten. Der Pfad bleibt als Antwort bestehen.", null, 0, null);
		sourceCodeRREP.addCodeLine(" 2. Sende das Paket zum nächsten Knoten links vom aktuellen Knoten ", null, 0, null);
		sourceCodeRREP.addCodeLine("    im Pfadstring", null, 0, null);
		sourceCodeRREP.addCodeLine(" 3. Wenn ein Knoten ein RREP-Paket erhält:", null, 0, null);
		sourceCodeRREP.addCodeLine("          a) Wenn er der Zielknoten ist:", null, 0, null);
		sourceCodeRREP.addCodeLine("                 i) Der Algorithmus ist beendet", null, 0, null);
		sourceCodeRREP.addCodeLine("          b) Wenn er nicht der Zielknoten ist", null, 0, null);
		sourceCodeRREP.addCodeLine("                 i) Springe zu 2.", null, 0, null);
		sourceCodeRREP.hide();
		graph.show();
		lang.nextStep();
		
		
		
		boolean finished = false;
	    
	    typProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 15));
	    undefined.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 15));
	    undefined.set("color", Color.BLACK);
	    trash.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 15));
	    winner.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 15));
	    trash.set("color", farbeVerlierer);
	    winner.set("color",farbeSieger);
	    
		// Graph	
		
		lang.newText(new Coordinates(420,400), "", "Graph", null, headerProps);
		infoHeader = lang.newText(new Offset(200,60, "Graph",AnimalScript.DIRECTION_NW), "Allgemeine Informationen: ", "infoHeader", null, headerProps);
		createdBoxesInfo = lang.newText(new Offset(200,100, "Graph",AnimalScript.DIRECTION_NW), "Erstellte Pakete: "+createdBoxes , "erstellteBoxen"+createdBoxes, null, TextBoxen);
		movedBoxesInfo = lang.newText(new Offset(200,120, "Graph",AnimalScript.DIRECTION_NW), "Bewegte Pakete: 0" , "bewegteBoxen"+movedBoxes, null, TextBoxen);
		floodingInfo = lang.newText(new Offset(200,140, "Graph",AnimalScript.DIRECTION_NW), "Anzahl der Flooding-Schritte: 0" , "flooding"+floodingStep, null, TextBoxen);

		setUpGraph();
	
		
		this.origin=(graph.getStartNode()!=null) ? getNodeIndex(graph.getStartNode(),1) : getNodeIndex(start);
		this.target=(graph.getTargetNode()!=null) ? getNodeIndex(graph.getTargetNode(),1) : getNodeIndex(goal);
		
		sourceCodeRREQ.highlight(0);
		sourceCodeRREQ.highlight(1);
		lang.nextStep();		
		
		sourceCodeRREQ.unhighlight(0);
		sourceCodeRREQ.unhighlight(1);
		
		sourceCodeRREQ.highlight(2);
		
		graph.highlightNode(origin, new Timing(0) {			
			@Override
			public String getUnit() {
				return "ticks";
			}
		}, null);
		graph.highlightNode(target, new Timing(0) {			
			@Override
			public String getUnit() {
				return "ticks";
			}
		}, null);
		
		
		lang.nextStep();
		sourceCodeRREQ.unhighlight(2);
		sourceCodeRREQ.highlight(3);
		sourceCodeRREQ.highlight(4);
		
		HashMap<Rect, Text> firstBox = boxes.get(createBox(-1,null));
		
		// Info
		Polyline typPoly = lang.newPolyline(new Node[]{new Offset (4,4, "rrqBox0", AnimalScript.DIRECTION_NW), new Offset(-20,-20,  "rrqBox0", AnimalScript.DIRECTION_NW)},"typPoly", null);
		Polyline originPoly = lang.newPolyline(new Node[]{new Offset (6,4, "originBox0", AnimalScript.DIRECTION_NW), new Offset(6,-20,  "originBox0", AnimalScript.DIRECTION_NW)},"originPoly", null);
		Polyline targetPoly = lang.newPolyline(new Node[]{new Offset (-6, 4, "targetBox0", AnimalScript.DIRECTION_NE), new Offset(20,-20,  "targetBox0", AnimalScript.DIRECTION_NE)},"targetPoly", null);
		Polyline pathPoly =  lang.newPolyline(new Node[]{new Offset (4, -4, "paketBox0", AnimalScript.DIRECTION_SW), new Offset(-20,20,  "paketBox0", AnimalScript.DIRECTION_SW)},"pathPoly", null);
		Polyline idPoly =  lang.newPolyline(new Node[]{new Offset (4, 4, "idBox0", AnimalScript.DIRECTION_NW), new Offset(-10,-20,  "idBox0", AnimalScript.DIRECTION_NW)},"idPoly", null);
		Text typText_info = lang.newText(new Offset (-20,-20, "typPoly",AnimalScript.DIRECTION_NW), "Pakettyp", "typText_info", null, TextBoxen);
		Text originText_info = lang.newText(new Offset (-20,-17, "originPoly",AnimalScript.DIRECTION_NW), "Quelle", "originText_info", null, TextBoxen);
		Text targetText_info = lang.newText(new Offset (20,-17, "targetPoly",AnimalScript.DIRECTION_NW), "Ziel", "targetText_info", null, TextBoxen);
		Text pathText_info = lang.newText(new Offset (-20,24, "pathPoly",AnimalScript.DIRECTION_NW), "Pfad", "pathText_info", null, TextBoxen);
		Text idText_info = lang.newText(new Offset(-20,-17 ,"idPoly", AnimalScript.DIRECTION_NW),"Paket-ID", "idText_info", null, TextBoxen);
		
		lang.nextStep();
		
		
		//HideInfo
		typPoly.hide();
		originPoly.hide();
		targetPoly.hide();
		pathPoly.hide();
		idPoly.hide();
		typText_info.hide();
		originText_info.hide();
		targetText_info.hide();
		pathText_info.hide();
		idText_info.hide();
		
		moveBox(origin,firstBox,0,boxBuffer.get(0),true);
		
		int notMoved;
		int i;
		int listBoxSize;
		int winnerBox=-1;
		int first=0;
		boolean firstStep = true;
		while(!finished) {
			if(first++==0)
				lang.nextStep("Request");
			else {
				lang.nextStep();
			}
			floodingStep++;
			if(firstStep) {
				sourceCodeRREQ.unhighlight(3);
				sourceCodeRREQ.unhighlight(4);
				sourceCodeRREQ.highlight(5);
				firstStep=false;
			}
			else {
				sourceCodeRREQ.unhighlight(5);
				for (int j = 6; j <=18; j++) {
					sourceCodeRREQ.highlight(j);
				}
			}
			listBoxSize = boxes.size();
			notMoved=0;
			
			for (int j = 0; j < listBoxSize; j++) {
				i=0;
				int boxIndex= getNodeIndex(boxPositions.get(j),2);
				HashMap<Integer, StringBuffer> neighbours = boxDrop.get(j) >0 ? new HashMap<Integer,StringBuffer>() : findNeighbours(boxIndex,boxBuffer.get(j));
				if(boxDrop.get(j)==1) 
				{
					boxDrop.set(j, 2);
				}
				else if(boxDrop.get(j) ==2)
				{
					hideBox(boxes.get(j));
				}
				if(neighbours.isEmpty() && boxIndex != target)
				{
					changeColor(boxes.get(j),trash);
					notMoved++;
					
				}
				else if(boxIndex == target && (winnerBox < 0 || winnerBox == j))
				{
					notMoved++;
					
					hideBox(boxes.get(j));
					if(winnerBox <0) {
						copyWinner = copyBox(j,boxIndex, boxes.get(j));
						changeColor(copyWinner, winner);
					}
					winnerBox=j;
				}
				else if(boxIndex == target)
				{
					notMoved++;
					changeColor(boxes.get(j),trash);
				}
				
				for (Map.Entry<Integer,StringBuffer> entry : neighbours.entrySet()) {
					if(i>0){
						int newBox = createBox(boxIndex, boxes.get(j));
						boxBuffer.add(moveBox(entry.getKey(), boxes.get(newBox),newBox,entry.getValue(),true));
					}
					else {
						
						StringBuffer newBuffer = moveBox(entry.getKey(),boxes.get(j),j,entry.getValue(),true);
						boxBuffer.set(j, newBuffer);
					}
					i++;
				}
				if(notMoved==listBoxSize)
					finished=true;
				if(finished)
					floodingStep--;
				floodingInfo.hide();
				floodingInfo = lang.newText(new Offset(200,140, "Graph",AnimalScript.DIRECTION_NW), "Anzahl der Flooding-Schritte: "+floodingStep , "flooding"+floodingStep, null, TextBoxen);
			}
			
		}
		lang.nextStep();
		hideBox(copyWinner);
		changeColor(boxes.get(winnerBox),winner);
		showBox(boxes.get(winnerBox));
		sourceCodeRREQ.hide();
		sourceCodeRREP.show();
		for (int j = 0; j < boxes.size(); j++) {
			if(j!=winnerBox)
				hideBox(boxes.get(j));
		}
		sourceCodeRREP.highlight(0);
		sourceCodeRREP.highlight(1);
		lang.nextStep("Response");
		
		sourceCodeRREP.unhighlight(0);
		sourceCodeRREP.unhighlight(1);
		sourceCodeRREP.highlight(3);
		sourceCodeRREP.highlight(4);
		StringBuffer oldBuffer = new StringBuffer(boxBuffer.get(winnerBox));
		changeType(boxes.get(winnerBox),winnerBox);
		ArrayList<Integer> path = new ArrayList<Integer>();
		while(boxBuffer.get(winnerBox).length()!=0) {
			int nextDest = getNext(boxBuffer.get(winnerBox),winnerBox);	
			path.add(nextDest);
		}
		for (int j = 0; j < path.size(); j++) {
			if(j+1<path.size())
				graph.highlightEdge(nodes[path.get(j)], nodes[path.get(j+1)], new Timing(0) {			
					@Override
					public String getUnit() {
						return "ticks";
					}
				}, null);
		}
		for (int j = 0; j< path.size();j++) {
			moveBox(path.get(j), boxes.get(winnerBox), winnerBox, oldBuffer,false);
			if(j>0 && j<path.size()-1) {
				sourceCodeRREP.unhighlight(3);
				sourceCodeRREP.unhighlight(4);
				sourceCodeRREP.highlight(5);
				sourceCodeRREP.highlight(6);
				sourceCodeRREP.highlight(7);
				sourceCodeRREP.highlight(10);
				sourceCodeRREP.highlight(11);
			}
			else if(j== path.size()-1){
				sourceCodeRREP.unhighlight(10);
				sourceCodeRREP.unhighlight(11);
				sourceCodeRREP.highlight(8);
				
			}
			lang.nextStep();
		}
		sourceCodeRREP.unhighlight(5);
		sourceCodeRREP.unhighlight(6);
		sourceCodeRREP.unhighlight(7);
		sourceCodeRREP.highlight(9);
		lang.nextStep();
		
		hideBox(boxes.get(winnerBox));
		infoHeader.hide();
		movedBoxesInfo.hide();
		createdBoxesInfo.hide();
		floodingInfo.hide();
		graph.hide();
		sourceCodeRREP.hide();
		SourceCode outroText = lang.newSourceCode(new Offset(55, 120, "header", AnimalScript.DIRECTION_NW), "outroText", null, infoTexte);
		outroText.addMultilineCode(" Der größte Aufwand bei diesem Algorithmus ist der Flooding-Prozess. Da die Knoten initial \n jedoch keinerlei Informationen über das Netzwerk besitzen, gibt es zunächst keine \n Möglichkeiten der Optimierung. Im späteren Verlauf des Algorithmus, können die Knoten das \n Flooding anhand der protokollierten Routen optimieren und ihre Anfragen entlang dieser \n Routen versenden. Da dynamische Netzwerke jedoch häufig der Ausfälle von Knoten oder\n Kanten verändert werden, sind diese Optimierungsansätze fehleranfällig. Aus diesem Grund \n wird der Flooding-Prozess häufig nicht optimiert und der erhöhte Aufwand damit in Kauf \n genommen. Man kann hierbei sagen, dass das Netzwerk sich bei jeder Routinganfrage neu \n erfinden muss. \n \n Die Komplexität des Algorithmus ist mit der Komplexität des Floodings vergleichbar. Die \n längste Laufzeit eines Paketes ist die längste Verkettung von Kanten ohne einen Zirkel zu \n bilden. Sei n die Anzahl die Knoten, so kann ein Paket maximal über n verschiedene Knoten \n entlang einer Kette verschickt werden. Da das Flooding gleichzeitig passiert, ist der längste \n Pfad auch der kritische Pfad und bildet damit die Laufzeit ab. \n \n Zusätzlich zum Flooding muss noch ein Antwort-Paket vom Zielknoten an den Initiator-Knoten \n gesendet werden. Auch hier kann die Kette maximal eine Länge von n-Knoten erreichen. \n Damit ergibt sich eine lineare Laufzeit von O(n) für die Wegfindung zwischen zwei Knoten. \n ", null, null);		
		infoRect.show();
		lang.nextStep("Outro");
	}

	private int getNodeIndex(String start) {
		for (int i = 0; i < labels.length; i++) {
			if(labels[i].equals(start)){
				return i;
			}
		}
		return -1;
	}

	private void changeType(HashMap<Rect, Text> hashMap,int index) {
		for (Entry<Rect,Text> entry : hashMap.entrySet()) {
			if(entry.getValue().getName().contains("typText")){
				entry.getValue().setText("RREP", new Timing(0) {			
					@Override
					public String getUnit() {
						return "ticks";
					}
				}, null);
			}
			else if (entry.getValue().getName().equals("originText"+index)) {
				entry.getValue().setText(labels[target], new Timing(0) {			
					@Override
					public String getUnit() {
						return "ticks";
					}
				}, null);
			}
			else if(entry.getValue().getName().equals("targetText"+index)) {
				entry.getValue().setText(labels[origin], new Timing(0) {			
					@Override
					public String getUnit() {
						return "ticks";
					}
				}, null);
			}
		}	
	}

	private int getNext(StringBuffer stringBuffer, int index) {
		int result = getNodeIndex(String.valueOf(stringBuffer.charAt(stringBuffer.length()-1)));
		if(stringBuffer.length()>3)
			boxBuffer.set(index, new StringBuffer(stringBuffer.substring(0, stringBuffer.length()-3)));
		else
			boxBuffer.set(index, new StringBuffer());
		return result;
	}

	private void changeColor(HashMap<Rect, Text> hashMap, TextProperties property) {
		for (Map.Entry<Rect, Text> entry : hashMap.entrySet()) {
			entry.getValue().changeColor(null, (Color) property.get("color"), new Timing(0) {			
				@Override
				public String getUnit() {
					return "ticks";
				}
			}, null);
		}
	}
	
	private void hideBox(HashMap<Rect,Text> box) {
		for (Entry<Rect, Text> entry : box.entrySet()) {
			entry.getKey().hide();
			entry.getValue().hide();
		}
	}
	private void showBox(HashMap<Rect,Text> box) {
		for (Entry<Rect, Text> entry : box.entrySet()) {
			entry.getKey().show();
			entry.getValue().show();
		}
	}

	private int getNodeIndex(Node node, int type) {
		switch (type) {
		case 1:
			for (int i = 0; i < nodes.length; i++) {
				if(nodes[i] == node)
					return i;
			}
			break;
		case 2:
			for (int i = 0; i < nodes_pre.length; i++) {
				if(nodes_pre[i] == node)
					return i;
			}
			break;	
		default:
			break;
		}
		return -1;
	}
	
	private HashMap<Integer, StringBuffer> findNeighbours(int currentNodeIndex, StringBuffer buffer) {
		
		int lastNodeIndex= getLastVisited(buffer);
		HashMap<Integer,StringBuffer> neighbours = new HashMap<Integer,StringBuffer>();
		if(currentNodeIndex==target)
			return neighbours;
		int[] edges = graph.getEdgesForNode(nodes[currentNodeIndex]);
		for (int i = 0; i < edges.length; i++) {
			if(edges[i]>0 && i!=currentNodeIndex && i != lastNodeIndex) {
				neighbours.put(i, new StringBuffer(buffer.toString()));
			}
		}
		return neighbours;
	}
	
	private int getLastVisited(StringBuffer buffer) {
		String[] array = buffer.toString().trim().split(", ");
		return buffer.length()==1 ? -1 : getNodeIndex(array[array.length-2]);
	}

	private StringBuffer moveBox(int target,HashMap<Rect,Text> box,int index, StringBuffer stringBuffer, boolean append){
		movedBoxesInfo.hide();
		movedBoxes++;
		movedBoxesInfo = lang.newText(new Offset(200,120, "Graph",AnimalScript.DIRECTION_NW), "Bewegte Pakete: "+movedBoxes , "bewegteBoxen"+movedBoxes, null, TextBoxen);
		
		if(append) {
			if(!stringBuffer.toString().isEmpty())
				stringBuffer.append(", ").append(labels[target]);
			else
				stringBuffer.append(labels[target]);
		}
		Node target_node = nodes_pre[target];
		Polyline move_poly = lang.newPolyline(new Node[]{boxPositions.get(index),target_node}, "move_poly",new Hidden());
		boxPositions.set(index,target_node);
		
		for (Map.Entry<Rect, Text> entry : box.entrySet()) {
			if(entry.getValue().getName().contains("path"))
				entry.getValue().setText(stringBuffer.toString(), null, null);
			entry.getKey().moveVia(AnimalScript.DIRECTION_SW, "translate", move_poly, null, new Timing(100) {			
				@Override
				public String getUnit() {
					return "ticks";
				}
			});
			entry.getValue().moveVia(AnimalScript.DIRECTION_SW, "translate", move_poly, null, new Timing(100) {			
				@Override
				public String getUnit() {
					return "ticks";
				}
			});
			
		}
		if(++visited[target]>1) {
			boxDrop.set(index, boxDrop.get(index)+1);
		}
			
		
		
		return stringBuffer;
	}
	
	private HashMap<Rect, Text> copyBox(int index,int boxNodeIndex, HashMap<Rect, Text> oldBox) {
		HashMap<Rect,Text> newBox = new HashMap<>();
		Node topleft =nodes_pre[boxNodeIndex];
		Node bottomright =nodes_pre_corner[boxNodeIndex];
		int countBoxes=boxes.size();
		RectProperties rectProp = new RectProperties();
		rectProp.set("fillColor", Color.WHITE);
		rectProp.set("filled", true);
		
		Rect testrect = lang.newRect(topleft, bottomright, "paketBox"+countBoxes, null,rectProp);
		newBox.put(testrect, lang.newText(new Offset(5,-20,"paketBox"+countBoxes,AnimalScript.DIRECTION_SW), String.valueOf(boxBuffer.get(index)), "pathText"+countBoxes, null, TextBoxen));
		newBox.put(lang.newRect(new Offset(0,0,"paketBox"+countBoxes,AnimalScript.DIRECTION_NW), new Offset(50, 25, "paketBox"+countBoxes, AnimalScript.DIRECTION_NW), "rrqBox"+countBoxes,null), lang.newText(new Offset(4,2,"rrqBox"+countBoxes,AnimalScript.DIRECTION_NW), "RREQ", "typText"+countBoxes, null, TextBoxen));
		newBox.put(lang.newRect(new Offset(0,0,"rrqBox"+countBoxes,AnimalScript.DIRECTION_NE), new Offset(50,25, "rrqBox"+countBoxes, AnimalScript.DIRECTION_NE), "idBox"+countBoxes, null),lang.newText(new Offset(8,2,"idBox"+countBoxes,AnimalScript.DIRECTION_NW),"ID:" + index,"UNIQUE_ID",null,TextBoxen));
		newBox.put(lang.newRect(new Offset(0,0,"idBox"+countBoxes,AnimalScript.DIRECTION_NE), new Offset(25,25, "idBox"+countBoxes, AnimalScript.DIRECTION_NE), "originBox"+countBoxes, null), lang.newText(new Offset(8,2,"originBox"+countBoxes,AnimalScript.DIRECTION_NW), labels[origin], "originText"+countBoxes, null, TextBoxen));
		newBox.put(lang.newRect(new Offset(0,0,"originBox"+countBoxes,AnimalScript.DIRECTION_NE), new Offset(25,25, "originBox"+countBoxes, AnimalScript.DIRECTION_NE), "targetBox"+countBoxes, null), lang.newText(new Offset(8,2,"targetBox"+countBoxes,AnimalScript.DIRECTION_NW), labels[target], "targetText"+countBoxes, null, TextBoxen));
		boxPositions.add(new Offset(0,0, "paketBox"+countBoxes, AnimalScript.DIRECTION_NW));

		return newBox;
	}
	
	private int createBox(int pre_index, HashMap<Rect, Text> oldBox){
		createdBoxes++;
		createdBoxesInfo.hide();
		createdBoxesInfo = lang.newText(new Offset(200,100, "Graph",AnimalScript.DIRECTION_NW), "Erstellte Pakete: "+createdBoxes , "erstelltePakete"+createdBoxes, null, TextBoxen);
		/*
		 * GesamteBox  -  Pfad
		 * RRQ-Box - TypText
		 * Origin-Box - Startknoten
		 * Target-Nox  - Zielknoten
		 */
		int countBoxes = boxes.size();
		Node topleft = (pre_index >= 0) ? nodes_pre[pre_index] : startNode;
		Node bottomright = (pre_index >= 0) ? nodes_pre_corner[pre_index] : startNode_corner;
		RectProperties rectProp = new RectProperties();
		rectProp.set("fillColor", Color.WHITE);
		rectProp.set("filled", true);
		if(countBoxes==0) {
			
			HashMap<Rect, Text> firstBox = new HashMap<Rect,Text>();
			Rect testrect = lang.newRect(topleft, bottomright, "paketBox"+countBoxes, null,rectProp);
			firstBox.put(testrect, lang.newText(new Offset(5,-20,"paketBox"+countBoxes,AnimalScript.DIRECTION_SW), labels[origin]+", ... , "+labels[target], "pathText"+countBoxes, null, TextBoxen));
			firstBox.put(lang.newRect(new Offset(0,0,"paketBox"+countBoxes,AnimalScript.DIRECTION_NW), new Offset(50, 25, "paketBox"+countBoxes, AnimalScript.DIRECTION_NW), "rrqBox"+countBoxes,null), lang.newText(new Offset(4,2,"rrqBox"+countBoxes,AnimalScript.DIRECTION_NW), "RREQ", "typText"+countBoxes, null, TextBoxen));
			firstBox.put(lang.newRect(new Offset(0,0,"rrqBox"+countBoxes,AnimalScript.DIRECTION_NE), new Offset(50,25, "rrqBox"+countBoxes, AnimalScript.DIRECTION_NE), "idBox"+countBoxes, null),lang.newText(new Offset(8,2,"idBox"+countBoxes,AnimalScript.DIRECTION_NW),"ID:" + countBoxes,"UNIQUE_ID",null,TextBoxen));
			firstBox.put(lang.newRect(new Offset(0,0,"idBox"+countBoxes,AnimalScript.DIRECTION_NE), new Offset(25,25, "idBox"+countBoxes, AnimalScript.DIRECTION_NE), "originBox"+countBoxes, null), lang.newText(new Offset(8,2,"originBox"+countBoxes,AnimalScript.DIRECTION_NW), labels[origin], "originText"+countBoxes, null, TextBoxen));
			firstBox.put(lang.newRect(new Offset(0,0,"originBox"+countBoxes,AnimalScript.DIRECTION_NE), new Offset(25,25, "originBox"+countBoxes, AnimalScript.DIRECTION_NE), "targetBox"+countBoxes, null), lang.newText(new Offset(8,2,"targetBox"+countBoxes,AnimalScript.DIRECTION_NW), labels[target], "targetText"+countBoxes, null, TextBoxen));
			boxPositions.add(new Offset(0,0, "paketBox"+countBoxes, AnimalScript.DIRECTION_NW));
			boxes.add(firstBox);
			boxDrop.add(0);
		}
		
		else {
			String oldPath = null;
			HashMap<Rect, Text> newBox = new HashMap<Rect,Text>();
			for (Map.Entry<Rect, Text> entry : oldBox.entrySet()) {
				if(entry.getValue().getName().equals("pathText"))
					oldPath = entry.getValue().getText();
			}
			Rect testrect = lang.newRect(topleft, bottomright, "paketBox"+countBoxes, null,rectProp);
			newBox.put(testrect, lang.newText(new Offset(5,-20,"paketBox"+countBoxes,AnimalScript.DIRECTION_SW), labels[origin]+", ... , "+labels[target], "pathText"+countBoxes, null, TextBoxen));
			newBox.put(lang.newRect(new Offset(0,0,"paketBox"+countBoxes,AnimalScript.DIRECTION_NW), new Offset(50, 25, "paketBox"+countBoxes, AnimalScript.DIRECTION_NW), "rrqBox"+countBoxes,null), lang.newText(new Offset(4,2,"rrqBox"+countBoxes,AnimalScript.DIRECTION_NW), "RREQ", "typText"+countBoxes, null, TextBoxen));
			newBox.put(lang.newRect(new Offset(0,0,"rrqBox"+countBoxes,AnimalScript.DIRECTION_NE), new Offset(50,25, "rrqBox"+countBoxes, AnimalScript.DIRECTION_NE), "idBox"+countBoxes, null),lang.newText(new Offset(8,2,"idBox"+countBoxes,AnimalScript.DIRECTION_NW),"ID:" + countBoxes,"UNIQUE_ID",null,TextBoxen));
			newBox.put(lang.newRect(new Offset(0,0,"idBox"+countBoxes,AnimalScript.DIRECTION_NE), new Offset(25,25, "idBox"+countBoxes, AnimalScript.DIRECTION_NE), "originBox"+countBoxes, null), lang.newText(new Offset(8,2,"originBox"+countBoxes,AnimalScript.DIRECTION_NW), labels[origin], "originText"+countBoxes, null, TextBoxen));
			newBox.put(lang.newRect(new Offset(0,0,"originBox"+countBoxes,AnimalScript.DIRECTION_NE), new Offset(25,25, "originBox"+countBoxes, AnimalScript.DIRECTION_NE), "targetBox"+countBoxes, null), lang.newText(new Offset(8,2,"targetBox"+countBoxes,AnimalScript.DIRECTION_NW), labels[target], "targetText"+countBoxes, null, TextBoxen));
			boxPositions.add(new Offset(0,0, "paketBox"+countBoxes, AnimalScript.DIRECTION_NW));
			boxes.add(newBox);
			boxDrop.add(0);
		}
		return countBoxes;	
	}
	
	private void setUpGraph() {
		nodes = new Node[graph.getSize()];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = graph.getNode(i);
		}
		nodes_pre = new Node[nodes.length];
		Coordinates current;
		for (int i = 0; i < nodes.length; i++) {
			current = (Coordinates) nodes[i];
			nodes_pre[i] = new Coordinates(current.getX()-70, current.getY()+30);
		}
	}
	
	public String getName() {
        return "Dynamic Source Routing";
    }

    public String getAlgorithmName() {
        return "Dynamic Source Routing";
    }

    public String getAnimationAuthor() {
        return "Maurice Wendt, Dominik Gopp";
    }

    public String getDescription(){
        return "Dynamic-Source-Routing (DSR) ist ein Routingprotokoll, welches &uuml;berwiegend im Rahmen von Ad-Hoc-Netzwerken (z.B. Funknetzwerke mit zwei oder mehr mobilen Endgeräten)  verwendet wird. Solche Netzwerke haben einen stark dynmaischen Ursprung, da ihre Endger&auml;te nicht an festen Positionen gebunden sind und die Verbindung zwischen diesen nicht festgelegt ist. Das Routing-Protokoll kann jedoch auch problemlos auf statischen Netzwerken angewendet werden."+

"Dynamic-Source-Routing ist aufgrund seiner Realisierung f&uuml;r Netzwerke bis zu 200 Knoten sinnvoll nutzbar. H&ouml;here Skalierungen sind zwar m&ouml;glich, erfordern jedoch eine Anpassung des Algorithmus."+

"Eine wesentliche Besonderheit bei DSR ist, dass die Wegfindung durch einen Knoten (Source) selbst angesto&szlig;en wird. Man bezeichnet daher das Routing auch als reaktiv, da es nur dann stattfindet, wenn die entsprechende Routinginformation explizit ben&ouml;tigt wird."+

"Zur Pfaderkennung wird Flooding verwendet. Dieses Verfahren kann in gro&szlig;en Netzwerken eine enorme Menge an Paketen verursachen, welche h&auml;ufig nicht ben&ouml;tigt werden. Diesen Nachteil nimmt man jedoch in Kauf, da der Aufwand nur beim Verbindungsaufbau bew&auml;ltigt werden muss und keine weiteren Informationen &uuml;ber das Netzwerk zur Verf&uuml;gung stehen, die eine zuverl&auml;ssige Optimierung erm&ouml;glichen."+

"W&auml;hrend dem Flooding-Prozess sammeln alle beteiligten Teilnehmer die Informationen aus den Paketen, welche sie empfangen, um daraus wertvolle Hinweise &uuml;ber den Aufbau des Netzes zu erlangen. H&auml;ufig k&ouml;nnen dadurch Routen bereits erkannt werden und sp&auml;tere Routinganfragen vermieden werden.";
    }

    public String getCodeExample(){
        return "";
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
        return Generator.JAVA_OUTPUT;
    }
    
    private String getColorNameFromRgb(int r, int g, int b) {
		ArrayList<ColorName> colorList = initColorList();
		ColorName closestMatch = null;
		int minMSE = Integer.MAX_VALUE;
		int mse;
		for (ColorName c : colorList) {
			mse = c.computeMSE(r, g, b);
			if (mse < minMSE) {
				minMSE = mse;
				closestMatch = c;
			}
		}
 
		if (closestMatch != null) {
			return closestMatch.getName();
		} else {
			return "No matched color name.";
		}
	}
    
    private ArrayList<ColorName> initColorList() {
		ArrayList<ColorName> colorList = new ArrayList<ColorName>();
		colorList.add(new ColorName("AliceBlue", 0xF0, 0xF8, 0xFF));
		colorList.add(new ColorName("AntiqueWhite", 0xFA, 0xEB, 0xD7));
		colorList.add(new ColorName("Aqua", 0x00, 0xFF, 0xFF));
		colorList.add(new ColorName("Aquamarine", 0x7F, 0xFF, 0xD4));
		colorList.add(new ColorName("Azure", 0xF0, 0xFF, 0xFF));
		colorList.add(new ColorName("Beige", 0xF5, 0xF5, 0xDC));
		colorList.add(new ColorName("Bisque", 0xFF, 0xE4, 0xC4));
		colorList.add(new ColorName("Black", 0x00, 0x00, 0x00));
		colorList.add(new ColorName("BlanchedAlmond", 0xFF, 0xEB, 0xCD));
		colorList.add(new ColorName("Blue", 0x00, 0x00, 0xFF));
		colorList.add(new ColorName("BlueViolet", 0x8A, 0x2B, 0xE2));
		colorList.add(new ColorName("Brown", 0xA5, 0x2A, 0x2A));
		colorList.add(new ColorName("BurlyWood", 0xDE, 0xB8, 0x87));
		colorList.add(new ColorName("CadetBlue", 0x5F, 0x9E, 0xA0));
		colorList.add(new ColorName("Chartreuse", 0x7F, 0xFF, 0x00));
		colorList.add(new ColorName("Chocolate", 0xD2, 0x69, 0x1E));
		colorList.add(new ColorName("Coral", 0xFF, 0x7F, 0x50));
		colorList.add(new ColorName("CornflowerBlue", 0x64, 0x95, 0xED));
		colorList.add(new ColorName("Cornsilk", 0xFF, 0xF8, 0xDC));
		colorList.add(new ColorName("Crimson", 0xDC, 0x14, 0x3C));
		colorList.add(new ColorName("Cyan", 0x00, 0xFF, 0xFF));
		colorList.add(new ColorName("DarkBlue", 0x00, 0x00, 0x8B));
		colorList.add(new ColorName("DarkCyan", 0x00, 0x8B, 0x8B));
		colorList.add(new ColorName("DarkGoldenRod", 0xB8, 0x86, 0x0B));
		colorList.add(new ColorName("DarkGray", 0xA9, 0xA9, 0xA9));
		colorList.add(new ColorName("DarkGreen", 0x00, 0x64, 0x00));
		colorList.add(new ColorName("DarkKhaki", 0xBD, 0xB7, 0x6B));
		colorList.add(new ColorName("DarkMagenta", 0x8B, 0x00, 0x8B));
		colorList.add(new ColorName("DarkOliveGreen", 0x55, 0x6B, 0x2F));
		colorList.add(new ColorName("DarkOrange", 0xFF, 0x8C, 0x00));
		colorList.add(new ColorName("DarkOrchid", 0x99, 0x32, 0xCC));
		colorList.add(new ColorName("DarkRed", 0x8B, 0x00, 0x00));
		colorList.add(new ColorName("DarkSalmon", 0xE9, 0x96, 0x7A));
		colorList.add(new ColorName("DarkSeaGreen", 0x8F, 0xBC, 0x8F));
		colorList.add(new ColorName("DarkSlateBlue", 0x48, 0x3D, 0x8B));
		colorList.add(new ColorName("DarkSlateGray", 0x2F, 0x4F, 0x4F));
		colorList.add(new ColorName("DarkTurquoise", 0x00, 0xCE, 0xD1));
		colorList.add(new ColorName("DarkViolet", 0x94, 0x00, 0xD3));
		colorList.add(new ColorName("DeepPink", 0xFF, 0x14, 0x93));
		colorList.add(new ColorName("DeepSkyBlue", 0x00, 0xBF, 0xFF));
		colorList.add(new ColorName("DimGray", 0x69, 0x69, 0x69));
		colorList.add(new ColorName("DodgerBlue", 0x1E, 0x90, 0xFF));
		colorList.add(new ColorName("FireBrick", 0xB2, 0x22, 0x22));
		colorList.add(new ColorName("FloralWhite", 0xFF, 0xFA, 0xF0));
		colorList.add(new ColorName("ForestGreen", 0x22, 0x8B, 0x22));
		colorList.add(new ColorName("Fuchsia", 0xFF, 0x00, 0xFF));
		colorList.add(new ColorName("Gainsboro", 0xDC, 0xDC, 0xDC));
		colorList.add(new ColorName("GhostWhite", 0xF8, 0xF8, 0xFF));
		colorList.add(new ColorName("Gold", 0xFF, 0xD7, 0x00));
		colorList.add(new ColorName("GoldenRod", 0xDA, 0xA5, 0x20));
		colorList.add(new ColorName("Gray", 0x80, 0x80, 0x80));
		colorList.add(new ColorName("Green", 0x00, 0x80, 0x00));
		colorList.add(new ColorName("GreenYellow", 0xAD, 0xFF, 0x2F));
		colorList.add(new ColorName("HoneyDew", 0xF0, 0xFF, 0xF0));
		colorList.add(new ColorName("HotPink", 0xFF, 0x69, 0xB4));
		colorList.add(new ColorName("IndianRed", 0xCD, 0x5C, 0x5C));
		colorList.add(new ColorName("Indigo", 0x4B, 0x00, 0x82));
		colorList.add(new ColorName("Ivory", 0xFF, 0xFF, 0xF0));
		colorList.add(new ColorName("Khaki", 0xF0, 0xE6, 0x8C));
		colorList.add(new ColorName("Lavender", 0xE6, 0xE6, 0xFA));
		colorList.add(new ColorName("LavenderBlush", 0xFF, 0xF0, 0xF5));
		colorList.add(new ColorName("LawnGreen", 0x7C, 0xFC, 0x00));
		colorList.add(new ColorName("LemonChiffon", 0xFF, 0xFA, 0xCD));
		colorList.add(new ColorName("LightBlue", 0xAD, 0xD8, 0xE6));
		colorList.add(new ColorName("LightCoral", 0xF0, 0x80, 0x80));
		colorList.add(new ColorName("LightCyan", 0xE0, 0xFF, 0xFF));
		colorList.add(new ColorName("LightGoldenRodYellow", 0xFA, 0xFA, 0xD2));
		colorList.add(new ColorName("LightGray", 0xD3, 0xD3, 0xD3));
		colorList.add(new ColorName("LightGreen", 0x90, 0xEE, 0x90));
		colorList.add(new ColorName("LightPink", 0xFF, 0xB6, 0xC1));
		colorList.add(new ColorName("LightSalmon", 0xFF, 0xA0, 0x7A));
		colorList.add(new ColorName("LightSeaGreen", 0x20, 0xB2, 0xAA));
		colorList.add(new ColorName("LightSkyBlue", 0x87, 0xCE, 0xFA));
		colorList.add(new ColorName("LightSlateGray", 0x77, 0x88, 0x99));
		colorList.add(new ColorName("LightSteelBlue", 0xB0, 0xC4, 0xDE));
		colorList.add(new ColorName("LightYellow", 0xFF, 0xFF, 0xE0));
		colorList.add(new ColorName("Lime", 0x00, 0xFF, 0x00));
		colorList.add(new ColorName("LimeGreen", 0x32, 0xCD, 0x32));
		colorList.add(new ColorName("Linen", 0xFA, 0xF0, 0xE6));
		colorList.add(new ColorName("Magenta", 0xFF, 0x00, 0xFF));
		colorList.add(new ColorName("Maroon", 0x80, 0x00, 0x00));
		colorList.add(new ColorName("MediumAquaMarine", 0x66, 0xCD, 0xAA));
		colorList.add(new ColorName("MediumBlue", 0x00, 0x00, 0xCD));
		colorList.add(new ColorName("MediumOrchid", 0xBA, 0x55, 0xD3));
		colorList.add(new ColorName("MediumPurple", 0x93, 0x70, 0xDB));
		colorList.add(new ColorName("MediumSeaGreen", 0x3C, 0xB3, 0x71));
		colorList.add(new ColorName("MediumSlateBlue", 0x7B, 0x68, 0xEE));
		colorList.add(new ColorName("MediumSpringGreen", 0x00, 0xFA, 0x9A));
		colorList.add(new ColorName("MediumTurquoise", 0x48, 0xD1, 0xCC));
		colorList.add(new ColorName("MediumVioletRed", 0xC7, 0x15, 0x85));
		colorList.add(new ColorName("MidnightBlue", 0x19, 0x19, 0x70));
		colorList.add(new ColorName("MintCream", 0xF5, 0xFF, 0xFA));
		colorList.add(new ColorName("MistyRose", 0xFF, 0xE4, 0xE1));
		colorList.add(new ColorName("Moccasin", 0xFF, 0xE4, 0xB5));
		colorList.add(new ColorName("NavajoWhite", 0xFF, 0xDE, 0xAD));
		colorList.add(new ColorName("Navy", 0x00, 0x00, 0x80));
		colorList.add(new ColorName("OldLace", 0xFD, 0xF5, 0xE6));
		colorList.add(new ColorName("Olive", 0x80, 0x80, 0x00));
		colorList.add(new ColorName("OliveDrab", 0x6B, 0x8E, 0x23));
		colorList.add(new ColorName("Orange", 0xFF, 0xA5, 0x00));
		colorList.add(new ColorName("OrangeRed", 0xFF, 0x45, 0x00));
		colorList.add(new ColorName("Orchid", 0xDA, 0x70, 0xD6));
		colorList.add(new ColorName("PaleGoldenRod", 0xEE, 0xE8, 0xAA));
		colorList.add(new ColorName("PaleGreen", 0x98, 0xFB, 0x98));
		colorList.add(new ColorName("PaleTurquoise", 0xAF, 0xEE, 0xEE));
		colorList.add(new ColorName("PaleVioletRed", 0xDB, 0x70, 0x93));
		colorList.add(new ColorName("PapayaWhip", 0xFF, 0xEF, 0xD5));
		colorList.add(new ColorName("PeachPuff", 0xFF, 0xDA, 0xB9));
		colorList.add(new ColorName("Peru", 0xCD, 0x85, 0x3F));
		colorList.add(new ColorName("Pink", 0xFF, 0xC0, 0xCB));
		colorList.add(new ColorName("Plum", 0xDD, 0xA0, 0xDD));
		colorList.add(new ColorName("PowderBlue", 0xB0, 0xE0, 0xE6));
		colorList.add(new ColorName("Purple", 0x80, 0x00, 0x80));
		colorList.add(new ColorName("Red", 0xFF, 0x00, 0x00));
		colorList.add(new ColorName("RosyBrown", 0xBC, 0x8F, 0x8F));
		colorList.add(new ColorName("RoyalBlue", 0x41, 0x69, 0xE1));
		colorList.add(new ColorName("SaddleBrown", 0x8B, 0x45, 0x13));
		colorList.add(new ColorName("Salmon", 0xFA, 0x80, 0x72));
		colorList.add(new ColorName("SandyBrown", 0xF4, 0xA4, 0x60));
		colorList.add(new ColorName("SeaGreen", 0x2E, 0x8B, 0x57));
		colorList.add(new ColorName("SeaShell", 0xFF, 0xF5, 0xEE));
		colorList.add(new ColorName("Sienna", 0xA0, 0x52, 0x2D));
		colorList.add(new ColorName("Silver", 0xC0, 0xC0, 0xC0));
		colorList.add(new ColorName("SkyBlue", 0x87, 0xCE, 0xEB));
		colorList.add(new ColorName("SlateBlue", 0x6A, 0x5A, 0xCD));
		colorList.add(new ColorName("SlateGray", 0x70, 0x80, 0x90));
		colorList.add(new ColorName("Snow", 0xFF, 0xFA, 0xFA));
		colorList.add(new ColorName("SpringGreen", 0x00, 0xFF, 0x7F));
		colorList.add(new ColorName("SteelBlue", 0x46, 0x82, 0xB4));
		colorList.add(new ColorName("Tan", 0xD2, 0xB4, 0x8C));
		colorList.add(new ColorName("Teal", 0x00, 0x80, 0x80));
		colorList.add(new ColorName("Thistle", 0xD8, 0xBF, 0xD8));
		colorList.add(new ColorName("Tomato", 0xFF, 0x63, 0x47));
		colorList.add(new ColorName("Turquoise", 0x40, 0xE0, 0xD0));
		colorList.add(new ColorName("Violet", 0xEE, 0x82, 0xEE));
		colorList.add(new ColorName("Wheat", 0xF5, 0xDE, 0xB3));
		colorList.add(new ColorName("White", 0xFF, 0xFF, 0xFF));
		colorList.add(new ColorName("WhiteSmoke", 0xF5, 0xF5, 0xF5));
		colorList.add(new ColorName("Yellow", 0xFF, 0xFF, 0x00));
		colorList.add(new ColorName("YellowGreen", 0x9A, 0xCD, 0x32));
		return colorList;
		
	}
    
    class ColorName 
    {
    	public int r, g, b;
		public String name;
 
		public ColorName(String name, int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
			this.name = name;
		}
 
		public int computeMSE(int pixR, int pixG, int pixB) {
			return (int) (((pixR - r) * (pixR - r) + (pixG - g) * (pixG - g) + (pixB - b)
					* (pixB - b)) / 3);
		}
 
		public int getR() {
			return r;
		}
 
		public int getG() {
			return g;
		}
 
		public int getB() {
			return b;
		}
 
		public String getName() {
			return name;
		}
    }
    private void changeToUndirected() {
  		adjazenz = graph.getAdjacencyMatrix();
  		for (int i = 0; i < adjazenz.length; i++) {
			for (int j = 0; j < adjazenz[i].length; j++) {
				adjazenz[j][i] = (adjazenz[i][j]==1) ? adjazenz[i][j] : 0; 
			}
		}
  	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> primitives) throws IllegalArgumentException {
		graph = (Graph)primitives.get("graph");
		GraphProperties graphProb = graph.getProperties();
		if((boolean) graphProb.get("directed") ) {
    		JOptionPane.showMessageDialog(null, "Die Richtung der Kanten ist für die Darstellung und den vorgestellten Algorithmus nicht relevant. \nBitte geben Sie daher einen ungerichteten Graph ein." , "Fehlerhafte Eingabe",JOptionPane.WARNING_MESSAGE);
			return false;
		}
		else if((boolean) graphProb.get("weighted")) {
    		JOptionPane.showMessageDialog(null, "Die Gewichtung der Kanten ist für die Darstellung und den vorgestellten Algorithmus nicht relevant. \nBitte geben Sie daher einen ungewichteten Graph ein." , "Fehlerhafte Eingabe",JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}
}