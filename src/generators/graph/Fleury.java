package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.SourceCodeProperties;
import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

public class Fleury implements Generator {
    private Language lang;
	int[][]  dfsMatrix;
	int[][]  fleuryMatrix;
	   int      rootNode = 0;
	   int      NNodes;
	   int counter = 0;
	   boolean[] visited; 
	   int startPoint;
	   int endPoint;
	   
	   
	   int[][] startMatrix;
	   int[][] nodeCoordinates;
	   Node[] startNodes;
	   String[] startNames;
	   GraphProperties graphProps;
	   SourceCodeProperties scProps;
	   
	   	private static final String DESCRIPTION =
				"Fleury ist einer der ältesten Algorithmen zum Auffinden von "
				+ "Eulerkreisen und Eulerpfaden."
				+ "Ein Eulerkreis beschreibt einen Pfad durch einen Grafen,"
				+ "bei dem man jede Kante genau einmal durchläuft."
				+ "Fleury orientiert sich hierbei an sogenannten Bridges (Brücken)."
				+ "Eine Brücke beschreibt eine Kante, deren Löschung eine Spaltung des Grafen zur Folge hätte."
				+ "Der Algorithmus geht bevorzugt über nicht-Brücken-Kanten und geht nur über eine Brücke,"
				+ "fallse es keine andere Möglichkeit gibt."
				+"Anschließend wird die besuchte Kante aus der Liste der vorhandenen Kanten "
				+"entfernt, da sie nur einmal betreten werden soll.";
		
		
		private static final String SOURCE_CODE =
"public void fleury(int[][] matrix, int v){"
 +"\n"
 +"ArrayList<Integer> bridges = new ArrayList<Integer>();"
 +"\n"
 +" for (int w = 0; w < matrix[v].length; w++){"
 +"\n"
 +"  if (matrix[v][w] > 0){"
 +"\n"
 +"   if (isBridge(matrix, v, w))"
 +"\n"
 +"    Bridges.add(w);"
 +"\n"
 +"   else{"
 +"\n"
 +"    bridges.clear();"
 +"\n"
 +"    matrix[v][w] = 0;"
 +"\n"
 +"    fleury(matrix, w);"
 +"\n"
 +"    return;"
 +"\n"
 +"   }"
 +"\n"
 +"  }"
 +"\n"
 +" }"
 +"\n"
 +" if (!bridges.isEmpty()){"
 +"\n"
 +"  matrix[v][brigdes.get(0)] = 0;"
 +"\n"
 +"  fleury(matrix, brigdes.get(0));"
 +"\n"
 +" }"
 +"\n"
 +"}";

    public void init(){
        lang = new AnimalScript("Fleury", "Denis Tuerkpencesi", 800, 600);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
 		   startMatrix = (int[][]) primitives.get("AdjMatrix");
		   nodeCoordinates = (int[][]) primitives.get("NodeMatrix");
		   startNodes= new Node[nodeCoordinates.length];
		   createNodes();
		   startNames = new String[nodeCoordinates.length];
		   
		   scProps = (SourceCodeProperties) props.getPropertiesByName("SourceCode");
		   //graphProps = (GraphProperties) props.getPropertiesByName("graph");
		   
		   
		   start(startMatrix);
        lang.finalizeGeneration();
        return lang.toString();
    }
	
	public void createNodes(){
		   for(int i = 0; i<nodeCoordinates.length; i++){
			   startNodes[i] = new Coordinates(nodeCoordinates[i][0],nodeCoordinates[i][1]);
		   }
	   }
	   
	   
	public boolean isBridge(int[][]  mat, int v, int w){
   
	int i, j, count1, count2;

    NNodes = mat.length;
    

    dfsMatrix = new int[NNodes][NNodes];
    visited = new boolean[NNodes];

    clearVisited();

    for ( i=0; i < NNodes; i++)
       for ( j=0; j < NNodes; j++)
         dfsMatrix[i][j] = mat[i][j];
	
    
    
    dfs(w);
    count1 = counter;
    clearVisited();
    
    dfsMatrix[v][w] = 0;
    
    
    dfs(w);
    count2 = counter;
    clearVisited();
    
    if(count1 == count2)	
	  return false;
    else
    	return true;
    
    
}

public void dfs(int i)
{
   int j;

   visited[i] = true;

   
   counter++;

   for ( j = 0; j < NNodes; j++ )
   {
	 if ( dfsMatrix[i][j] > 0 && !visited[j] )
         dfs(j);
   }
}

public void clearVisited()
{
   int i;
   counter = 0;
   for (i = 0; i < visited.length; i++)
      visited[i] = false;
}

public void printNode(int n)
{
 //System.out.println(n);
}


public void startFleury(int start, int[][] mat, Graph graph, Text darstText, SourceCode sc, String bridges, String kanten, Text kantenText, Text bridgesText){
	fleuryMatrix = new int[mat.length][mat.length];
	
    for (int i=0; i < mat.length; i++)
        for (int j=0; j < mat[i].length; j++)
           fleuryMatrix[i][j] = mat[i][j];
    
    fleury(start, graph, darstText,"{}", sc, bridges, kanten, kantenText, bridgesText,0);
	
}


public void fleury(int v, Graph graph, Text darstText, String darstString, SourceCode sc, String bridgeString, String kantenString, Text kantenText, Text bridgesText,int counterZ){
	counterZ++;
	sc.highlight(0);
	endPoint = v;
	//Darstellung

	lang.nextStep("Kante "+counterZ);
	
	sc.unhighlight(0);
	sc.highlight(1);
	ArrayList<Integer> bridges = new ArrayList<Integer>();
	bridges.clear();
	lang.nextStep();
	
	sc.unhighlight(1);
	sc.highlight(2);
	
	//System.out.println(v+"->");
	for(int w = 0; w<fleuryMatrix.length; w++){

		if(fleuryMatrix[v][w] != 0){
			lang.nextStep();
			sc.unhighlight(2);
			sc.highlight(3);
			
			kantenString = "("+v+","+w+")";
			kantenText.setText(kantenString, null, null);
			
			lang.nextStep();
			sc.unhighlight(3);
			sc.highlight(4);
		if(isBridge(fleuryMatrix, v, w) && fleuryMatrix[v][w] != 0 || targetIsEmpty(w)&& fleuryMatrix[v][w] != 0){
			lang.nextStep();
			sc.unhighlight(2);
			sc.unhighlight(3);
			sc.unhighlight(4);
			sc.highlight(5);
			
			kantenText.setText("", null, null);
			bridgeString = bridgeString.substring(0, bridgeString.length()-1)+(bridgeString.length()>3?",":"")+ "("+v+","+w+")}";
			bridgesText.setText(bridgeString, null, null);
			
			bridges.add(w);
			lang.nextStep();
			sc.unhighlight(5);
			sc.highlight(2);
			//System.out.println("bridge");
		}
		
		if(!isBridge(fleuryMatrix, v, w) && fleuryMatrix[v][w] != 0 && !targetIsEmpty(w)){
			lang.nextStep();
			sc.unhighlight(4);
			sc.unhighlight(5);
			sc.highlight(6);
			lang.nextStep();
			sc.unhighlight(6);
			sc.highlight(7);
			lang.nextStep();
			sc.unhighlight(7);
			sc.highlight(8);
			fleuryMatrix[v][w] = 0;
			lang.nextStep();
			sc.unhighlight(8);
			sc.highlight(9);
			lang.nextStep();
			sc.unhighlight(9);
			//System.out.println("no bridge");
			graph.highlightEdge(v, w, null, null);
			
			//darstText.setText(darstString, null, null);
			bridges.clear();
			bridgesText.setText("{}", null, null);
			bridgeString = "{}";
			
			kantenText.setText("", null, null);

			darstString = darstString.substring(0, darstString.length()-1)+(darstString.length()>3?",":"")+"("+v+","+w+")}";
			darstText.setText(darstString, null, null);
			
			fleury(w, graph,darstText,darstString, sc, bridgeString, kantenString, kantenText, bridgesText, counterZ);
			
			return;
		}
		}
		
	}
	lang.nextStep();
	sc.unhighlight(2);
	sc.unhighlight(3);
	sc.unhighlight(5);
	sc.highlight(11);
	lang.nextStep();
	sc.unhighlight(11);
	sc.highlight(12);
	lang.nextStep();
	sc.unhighlight(12);
	sc.highlight(13);
	lang.nextStep();
	sc.unhighlight(13);
	sc.highlight(14);
	
	if(!bridges.isEmpty()){			
		if(fleuryMatrix[v][bridges.get(0)] != 0){
			lang.nextStep();
			sc.unhighlight(14);
			sc.highlight(15);
			//printMatrix(fleuryMatrix);
		fleuryMatrix[v][bridges.get(0)] = 0;
		lang.nextStep();
		sc.unhighlight(15);
		sc.highlight(16);
		lang.nextStep();
		sc.unhighlight(16);
		//System.out.println("blubb");
		graph.highlightEdge(v, bridges.get(0), null, null);
		
		darstText.setText(darstString, null, null);
		kantenText.setText("", null, null);
		bridgesText.setText("{}", null, null);
		bridgeString = "{}";
		darstString = darstString.substring(0, darstString.length()-1)+(darstString.length()>3?",":"")+"("+v+","+bridges.get(0)+")}";
		darstText.setText(darstString, null, null);
		
		fleury(bridges.get(0),graph,darstText,darstString,sc,bridgeString, kantenString, kantenText, bridgesText,counterZ);}
	}else{
		lang.nextStep();
		sc.unhighlight(14);
		sc.highlight(18);	
	}
}


public boolean targetIsEmpty(int v){
	for(int w = 0; w<fleuryMatrix[v].length; w++){
		if(fleuryMatrix[v][w]>0)
			return false;
	}
	
	return true;
}


public void printMatrix(int[][] matrix){
	for(int x=0; x<matrix.length; x++){
		System.out.print("\n");
		for(int y = 0; y<matrix[x].length; y++)
			System.out.print(matrix[x][y]+" ");
	}
}



public void start(int[][] matrix){
	//uberschrift rec
	Color colorLightGreen = new Color(100,149,237);
    RectProperties uberRec = new RectProperties();
    uberRec.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    uberRec.set(AnimationPropertiesKeys.FILL_PROPERTY, colorLightGreen);
    uberRec.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    //uberRec.set(AnimationPropertiesKeys.BORDER_PROPERTY, );
    
    Rect uberRectangle = lang.newRect(new Coordinates(0,40), new Coordinates(830,80), "horst",null, uberRec);
	
    //Einstellungen für den Text
	TextProperties textProps = new TextProperties();
	textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	textProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, false);
	textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, 
			new Font("SansSerif", Font.PLAIN, 36));
	
	//Überschrift
	Text aText = lang.newText(new Coordinates(10, 56), "Fleury", 
			"text", null, textProps);
	
	//IntroText
	TextProperties firstTextProps = new TextProperties();
	firstTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
	firstTextProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, false);
	firstTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, 
			new Font("SansSerif", Font.PLAIN, 16));
	
	Text introText1 = lang.newText(new Coordinates(10, 110), "Fleury ist einer der ältesten Algorithmen zum Auffinden von ", 
			"text", null, firstTextProps);
	Text introText2 = lang.newText(new Coordinates(10, 150), "Eulerkreisen und Eulerpfaden.", 
			"text", null, firstTextProps);
	Text introText3 = lang.newText(new Coordinates(10, 170), "Ein Eulerkreis beschreibt einen Pfad durch einen Graphen,", 
			"text", null, firstTextProps);
	Text introText4 = lang.newText(new Coordinates(10, 190), "bei dem man jede Kante genau einmal durchläuft.", 
			"text", null, firstTextProps);
	Text introText5 = lang.newText(new Coordinates(10, 210), "Fleury orientiert sich hierbei an sogenannten Bridges (Brücken).", 
			"text", null, firstTextProps);
	Text introText6 = lang.newText(new Coordinates(10, 230), "Eine Brücke beschreibt eine Kante, deren Löschung eine Spaltung des Graphen zur Folge hätte.", 
			"text", null, firstTextProps);
	Text introText7 = lang.newText(new Coordinates(10, 250), "Der Algorithmus geht bevorzugt über nicht-Brücken-Kanten und geht nur über eine Brücke,", 
			"text", null, firstTextProps);
	Text introText8 = lang.newText(new Coordinates(10, 270), "fallse es keine andere Möglichkeit gibt.", 
			"text", null, firstTextProps);
	Text introText9 = lang.newText(new Coordinates(10, 290), "Anschließend wird die besuchte Kante aus der Liste der vorhandenen Kanten ", 
			"text", null, firstTextProps);
	Text introText10 = lang.newText(new Coordinates(10, 310), "entfernt, da sie nur einmal betreten werden soll.", 
			"text", null, firstTextProps);
	
	lang.nextStep();
	
	introText1.hide();
	introText2.hide();
	introText3.hide();
	introText4.hide();
	introText5.hide();
	introText6.hide();
	introText7.hide();
	introText8.hide();
	introText9.hide();
	introText10.hide();
	

	
	//properties
	graphProps = new GraphProperties();
	graphProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
	graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
	graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
	graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
	graphProps.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.BLUE);
	graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.WHITE);
	
	Node[] nodes;
	String[] names;
	Graph graph;
	
	nodes = startNodes;
	names = new String[startNames.length];
	
	for(int i = 0; i<names.length; i++)
		names[i]=String.valueOf(i);
	
	if(validValue(matrix, nodes, names)){
		graph = lang.newGraph("demoGraph", matrix, nodes, names, 
				null, graphProps);
	}else{
		
		names = new String[4];
		
		for(int i = 0; i<names.length; i++)
			names[i]=String.valueOf(i);
		
		nodes = new Node[4];
		nodes[0] = new Coordinates(60, 150);
		nodes[1] = new Coordinates(220, 150);
		nodes[2] = new Coordinates(60, 300);
		nodes[3] = new Coordinates(220, 300);
		
		matrix = new int[][] {
				{0, 1, 1, 0},
				{1, 0, 0, 1},
				{1, 0, 0, 1},
				{0, 1, 1, 0}
		};
		
		graph = lang.newGraph("demoGraph", matrix, nodes, names, 
				null, graphProps);
				
	}
	
	
	TextProperties textAddProps = new TextProperties();
	textAddProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	textAddProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, false);
	textAddProps.set(AnimationPropertiesKeys.FONT_PROPERTY, 
			new Font("SansSerif", Font.PLAIN, 18));
	
	//Beschriftung unten
	Text charText = lang.newText(new Coordinates(180, 590), "{}", 
			"text2", null, textAddProps);
	Text kantenText = lang.newText(new Coordinates(170, 500), "", 
			"text2", null, textAddProps);
	Text bridgesListText = lang.newText(new Coordinates(110, 540), "{}", 
			"text2", null, textAddProps);
	
	//Beschriftung unten
	Text actKnotText = lang.newText(new Coordinates(40, 500), "Aktuelle Kante: ", 
			"text2", null, textAddProps);
	//Beschriftung unten
	Text bridgesText = lang.newText(new Coordinates(40, 540), "Bridges: ", 
			"text2", null, textAddProps);
	//Beschriftung unten
	Text solutionText = lang.newText(new Coordinates(40, 590), "Ergebnissequenz: ", 
			"text2", null, textAddProps);
	
	//zugehörige Strings
	String bridges = "";
	String kanten = "";
	
	
	//sourceCode
    //scProps = new SourceCodeProperties();
    //scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    //scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", 
      //  Font.PLAIN, 12));

    //scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, 
      //  Color.RED);   
    //scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    
	SourceCode sc = lang.newSourceCode(new Coordinates(400, 100), "sourceCode",
	        null, scProps);
	sc.addCodeLine("public void fleury(int[][] matrix, int v){", null, 0, null);//0
	sc.addCodeLine("ArrayList<Integer> bridges = new ArrayList<Integer>();", null, 0, null);//1
	sc.addCodeLine("for (int w = 0; w < matrix[v].length; w++){", null, 1, null);//2
	sc.addCodeLine("if (matrix[v][w] > 0){", null, 2, null);//3
	sc.addCodeLine("if (isBridge(matrix, v, w))", null, 3, null);//4
	sc.addCodeLine("bridges.add(w);", null, 4, null);//5
	sc.addCodeLine("else{", null, 3, null);//6
	sc.addCodeLine("bridges.clear();", null, 4, null);//7
	sc.addCodeLine("matrix[v][w] = 0;", null, 4, null);//8
	sc.addCodeLine("fleury(matrix, w);", null, 4, null);//9
	sc.addCodeLine("return;", null, 4, null);//10
	sc.addCodeLine("}", null, 3, null);//11
	sc.addCodeLine("}", null, 2, null);//12
	sc.addCodeLine("}", null, 1, null);//13
	sc.addCodeLine("if (!bridges.isEmpty()){", null, 1, null);//14
	sc.addCodeLine("matrix[v][brigdes.get(0)] = 0;", null, 2, null);//15
	sc.addCodeLine("fleury(matrix, brigdes.get(0));", null, 2, null);//16
	sc.addCodeLine("}", null, 1, null);//17
	sc.addCodeLine("}", null, 0, null);//18


//	
	startPoint = oddVertices(matrix);
	//startPoint = 0;
	startFleury(startPoint, matrix, graph, charText, sc, bridges, kanten,kantenText, bridgesListText);
	
	
	int failCount = 0;
	for(int x = 0; x < fleuryMatrix.length; x++){
		for(int y = 0; y< fleuryMatrix[x].length; y++){
			if (fleuryMatrix[x][y] != 0)
			failCount++;
		}
	}
	
	Text outputText = lang.newText(new Coordinates(350, 540), "", 
			"text2", null, textAddProps);
	
	if(failCount != 0){
		outputText.setText("Es wurde weder ein EulerPfad noch ein Eulerkreis gefunden", null, null);
	}else{
		if(startPoint == endPoint)
			outputText.setText("Es wurde ein Eulerkreis ab Punkt "+startPoint+" gefunden.", null, null);
		else
			outputText.setText("Es wurde ein Eulerpfad von Punkt "+startPoint+" nach Punkt "+endPoint+" gefunden.", null, null);
	}
	
	lang.nextStep();
	
	outputText.hide();
	graph.hide();
	sc.hide();
	charText.hide();
	bridgesListText.hide();
	kantenText.hide();
	actKnotText.hide();
	bridgesText.hide();
	solutionText.hide();
	 introText1 = lang.newText(new Coordinates(10, 110), "Nicht jeder Graph kann einen EulerPfad oder Eulerkreis bilden. ", 
			"text", null, firstTextProps);
	 introText2 = lang.newText(new Coordinates(10, 150), "Um einen Eulerkreis bilden zu können darf der Graph ausschließlich", 
			"text", null, firstTextProps);
	 introText3 = lang.newText(new Coordinates(10, 170), "Knoten mit gerader Kantenfolge haben.", 
			"text", null, firstTextProps);
	 introText4 = lang.newText(new Coordinates(10, 190), "Für die Bildung eines Eulerpfades dürfen nur genau zwei Knoten", 
			"text", null, firstTextProps);
	 introText5 = lang.newText(new Coordinates(10, 210), "eine ungerade Kantenfolge aufweisen.", 
			"text", null, firstTextProps);
	 introText6 = lang.newText(new Coordinates(10, 230), "Zudem muss genau einer dieser Knoten eine gerade Anazhl an ausgehenden Kanten besitzen", 
			"text", null, firstTextProps);
	 introText7 = lang.newText(new Coordinates(10, 250), "und der andere eine gerade Anzahl an eingehenden Kanten.", 
			"text", null, firstTextProps);
	 introText8 = lang.newText(new Coordinates(10, 270), "Ausserdem muss ein einem der ungeraden Knoten starten.", 
			"text", null, firstTextProps);
	 introText9 = lang.newText(new Coordinates(10, 290), "Fleury gilt als nicht sehr effizient.", 
			"text", null, firstTextProps);

		introText1.show();
	introText2.show();
	introText3.show();
	introText4.show();
	introText5.show();
	introText6.show();
	introText7.show();
	introText8.show();
	introText9.show();
	

lang.nextStep();	
			
		
	
}

public boolean validValue(int[][] matrix, Node[] nodes, String[] names){
	if(matrix.length != matrix[0].length){
		
		return false;
	}
		
	
	if(matrix.length != nodes.length){
		
		return false;
	}
	
	if(matrix.length != names.length){
		
		return false;
	}
	
	return true;
}


////////////ungerade knoten finden
public int oddVertices(int[][] adj){
	
	
	for(int i = 0; i< adj.length; i++){
		int count1 = 0;
		int count2 = 0;
		for(int j = 0; j < adj[i].length; j++){
			if(adj[i][j] != 0)
				count1++;
		}
		
		for(int k = 0; k<adj.length; k++){
			if(adj[k][i] != 0)
				count2++;
		}
		
		
		if((count1+count2) % 2 != 0 && count1 > count2)
			return i;
	}
	
	return 0;		
	
}

    public String getName() {
        return "Fleury";
    }

    public String getAlgorithmName() {
        return "Fleury";
    }

    public String getAnimationAuthor() {
        return "Denis Tuerkpencesi";
    }

    public String getDescription(){
        return 	"Fleury ist einer der ältesten Algorithmen zum Auffinden von "
				+ "Eulerkreisen und Eulerpfaden."
				+ "Ein Eulerkreis beschreibt einen Pfad durch einen Grafen,"
				+ "bei dem man jede Kante genau einmal durchläuft."
				+ "Fleury orientiert sich hierbei an sogenannten Bridges (Brücken)."
				+ "Eine Brücke beschreibt eine Kante, deren Löschung eine Spaltung des Grafen zur Folge hätte."
				+ "Der Algorithmus geht bevorzugt über nicht-Brücken-Kanten und geht nur über eine Brücke,"
				+ "fallse es keine andere Möglichkeit gibt."
				+"Anschließend wird die besuchte Kante aus der Liste der vorhandenen Kanten "
				+"entfernt, da sie nur einmal betreten werden soll.";
    }

    public String getCodeExample(){
        return "public void fleury(int[][] matrix, int v){"
 +"\n"
 +"ArrayList<Integer> bridges = new ArrayList();"
 +"\n"
 +" for(int w = 0; w < matrix[v].length; w++){"
 +"\n"
 +"  if(matrix[v][w] > 0){"
 +"\n"
 +"   if(isBridge(matrix, v, w))"
 +"\n"
 +"    Bridges.add(w);"
 +"\n"
 +"   else{"
 +"\n"
 +"    bridges.clear();"
 +"\n"
 +"    matrix[v][w] = 0;"
 +"\n"
 +"    fleury(matrix, w);"
 +"\n"
 +"    return;"
 +"\n"
 +"   }"
 +"\n"
 +"  }"
 +"\n"
 +" }"
 +"\n"
 +" if(!bridges.isEmpty()){"
 +"\n"
 +"  matrix[v][brigdes.get(0)] = 0;"
 +"\n"
 +"  fleury(matrix, brigdes.get(0));"
 +"\n"
 +" }"
 +"\n"
 +"}";
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

}