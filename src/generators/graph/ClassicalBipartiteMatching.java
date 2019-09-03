/*
 * classicalBipartiteMatching.java
 * Markus Lehmann, Martin M\u00fcller, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Locale;

import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class ClassicalBipartiteMatching implements ValidatingGenerator {
    private Language lang;
    private SourceCodeProperties sourceCode;
    private Graph gr, grBackup;
    private Text text, header;
    private Text introduction1, introduction2, introduction3, introduction4, introduction5;
    private Text introduction6, introduction7, introduction8, introduction9, introduction10;
    private Text end1, end2, end3, end4, pathText;
    private SourceCode sc;
    private Variables vars;
    private ArrayList<NodeElement> nodes;
    private int numNodes, matchingSize, numberNodesA, numberNodesB; 
    private int[] arrayPositions;
    private int[][] adj;
    private String pathString;
    private static String introductionString1 = "Der Classical Bipartite Matching Algorithmus findet auf einem bipartiten Graph ein g\u00fcltiges kardinalit\u00e4ts-maximales Matching. ";
    private static String introductionString2 = "Das Matching ist eine Teilmenge der Kanten des Algorithmus. Damit es g\u00fcltig ist, darf jeder Knoten des Graphen h\u00f6chstens ";
    private static String introductionString3 = "an eine gematchte Kante angrenzen. Kardinalit\u00e4ts-maximal bedeutet, dass der Graph keine weiteren Matching-Kanten mehr ";
    private static String introductionString4 = "erlaubt, da sonst kein g\u00fcltiges Matching mehr m\u00f6glich w\u00e4re.";
    private static String introductionString5 = "Der Algorithmus startet mit einem bipartiten Graphen, dessen Kanten alle ungematcht sind. Es wird nun solange ein erh\u00f6hender ";
    private static String introductionString6 = "Pfad gesucht, bis kein solcher Pfad mehr gefunden wird, dann bricht der Algorithmus ab. Bei einem erh\u00f6henden Pfad wird die ";
    private static String introductionString7 = "Anzahl der gematchten Kanten im Graph erh\u00f6ht. Ein solcher Pfad muss immer von einem freien Knoten, also einem Knoten, der nur ";
    private static String introductionString8 = "\u00fcber ungematchte Kanten erreichbar ist, zu einem anderen solchen freien Knoten f\u00fchren. Wenn ein solcher Pfad gefunden wurde, ";
    private static String introductionString9 = "wird das Matching entlang dieses Pfades erh\u00f6ht. Das bedeutet, dass bisher ungematchte Kanten gematcht werden und bisher ";
    private static String introductionString10 = "gematchte Kanten wieder ungematcht werden. Dies wird solange durchgef\u00fchrt, bis es keinen erh\u00f6henden Pfad mehr gibt.";
    
    public void init(){
        lang = new AnimalScript("Classical Bipartite Matching Algorithm", "Markus Lehmann, Martin M\u00fcller", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        if (validateInput(props, primitives))
        	algo();
        return lang.toString();
    }
    
    public void algo() {
        numNodes = numberNodesA + numberNodesB;
        matchingSize = 0;

        // set properties for graphs and source code
    	GraphProperties graphProps = new GraphProperties();
    	graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, Boolean.FALSE);
    	graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);
    	graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, Boolean.FALSE);
    	graphProps.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.BLACK);
    	graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.WHITE);
    	GraphProperties graphPropsBackup = new GraphProperties();
    	graphPropsBackup.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, Boolean.FALSE);
    	graphPropsBackup.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    	graphPropsBackup.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, Boolean.FALSE);
    	graphPropsBackup.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.BLACK);
    	graphPropsBackup.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.WHITE);
    	
    	TextProperties tProps = new TextProperties();
        tProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif", Font.BOLD, 24));
        
        // now, create the source code entity
        sc = lang.newSourceCode(new Coordinates(450, 100), "sourceCode",
            null, sourceCode);
        
        sc.addCodeLine("public void classicalBipartiteMatchingAlgo() {", null, 0, null); //0
        sc.addCodeLine("while() {", null, 1, null); //1
        sc.addCodeLine("//Der Algorithmus w\u00e4hlt einen freien Knoten", null, 2, null); //2
        sc.addCodeLine("//und sucht von diesem aus mittels einer ", null, 2, null); //3
        sc.addCodeLine("//modifizierten DFS nach einem erh\u00f6henden Pfad, ", null, 2, null); //4
        sc.addCodeLine("//also einem Pfad, auf dem sich Matching-Kanten ", null, 2, null); //5
        sc.addCodeLine("//und nicht gematchte Kanten abwechseln sowie ", null, 2, null); //6
        sc.addCodeLine("//Start- und Zielknoten frei sind.", null, 2, null); //7
        sc.addCodeLine("Path p = DFS(exposedNode); //Pfad, Start- und Zielknoten rot markiert",null,2,null); //8
        sc.addCodeLine("if (p == null)", null, 2, null); //9
        sc.addCodeLine("break;", null, 3, null); //10
        sc.addCodeLine("//Invertiere die Matching-Eigenschaft der Pfadkanten", null, 2, null); //11
        sc.addCodeLine("//Gr\u00fcne Kanten geh\u00f6ren zum Matching", null, 2, null); //12
        sc.addCodeLine("for (Edge e : path)", null, 2, null); //13
        sc.addCodeLine("e.matched = !e.matched;", null, 3, null); //14
        sc.addCodeLine("}", null, 1, null); //15
        sc.addCodeLine("}", null, 0, null); //16

        text = lang.newText(new Coordinates(250,450), "Matching cardinality: 0", "matchingCardinality", null);
        header = lang.newText(new Coordinates(50,20), "Classical Bipartite Matching Algorithm", "Header",null, tProps);
        introduction1 = lang.newText(new Coordinates(50,140), introductionString1, "introduction1", null);
        introduction2 = lang.newText(new Coordinates(50,165), introductionString2, "introduction2", null);
        introduction3 = lang.newText(new Coordinates(50,180), introductionString3, "introduction3", null);
        introduction4 = lang.newText(new Coordinates(50,195), introductionString4, "introduction4", null);
        introduction5 = lang.newText(new Coordinates(50,225), introductionString5, "introduction5", null);
        introduction6 = lang.newText(new Coordinates(50,240), introductionString6, "introduction6", null);
        introduction7 = lang.newText(new Coordinates(50,255), introductionString7, "introduction7", null);
        introduction8 = lang.newText(new Coordinates(50,270), introductionString8, "introduction8", null);
        introduction9 = lang.newText(new Coordinates(50,285), introductionString9, "introduction9", null);
        introduction10 = lang.newText(new Coordinates(50,300), introductionString10, "introduction10", null);
        pathText = lang.newText(new Coordinates(250,500), "", "pathText", null);
        vars = lang.newVariables();
        vars.declare("String", "p");
        vars.declare("int", "MatchingSize");
        
        // create nodes
        Coordinates[] cords = new Coordinates[numNodes];
        
    	for (int i = 0; i < numberNodesA; i++) {
        	cords[i] = new Coordinates(50, 70 + i*40);
        }
        for (int i = 0; i < numberNodesB; i++) {
        	cords[i + numberNodesA] = new Coordinates(350, 70 + i * 40);
        }

        adj = new int[numNodes][numNodes];
        
        // add edges
        for (int i = 0; i < numberNodesA; i++) {
        	for (int j = numberNodesA; j < numNodes; j++) {
        		adj[i][j] = (int) (Math.random() * 2);
        		adj[j][i] = adj[i][j];
        	}
        }
        
        
        // create a random selection order of nodes in the algorithm
        arrayPositions = new int[numNodes];
        for (int i = 0; i < numNodes; i++) {
        	arrayPositions[i] = i;
        }

        int swap;
        double rand;
        
        for (int i = 0; i < numNodes; i++) {
        	rand = Math.random();
        	swap = arrayPositions[i];
        	arrayPositions[i] = arrayPositions[(int)(rand*(numNodes - i) + i)];
        	arrayPositions[(int)(rand*(numNodes - i) + i)] = swap;
        }
        
        nodes = new ArrayList<NodeElement>();  
        
        String[] nodeNames = new String[numNodes];
        for (int i = 0; i < numNodes; i++) {
        	nodeNames[i] = String.valueOf((char)(0x0041 + i));
        }
        	
        gr = lang.newGraph("Graph",adj,cords, nodeNames, null,graphProps);
        grBackup = lang.newGraph("GraphBackup",adj,cords, nodeNames, null, graphPropsBackup);
        grBackup.hide();
        
        // initialize node elements
        for (int i = 0; i < numNodes; i++) {
        	nodes.add(new NodeElement(i));
        }
        
        // initialize edge elements
        for (int i = 0; i < numberNodesA; i++) {
        	for (int j = numberNodesA; j < numNodes; j++) {
        		if (adj[i][j] == 1) {
        			EdgeElement e1 = new EdgeElement(i,j,false);
        			nodes.get(i).addEdge(e1);
        			nodes.get(i).exposed = true;
        			EdgeElement e2 = new EdgeElement(j,i,false);
        			nodes.get(j).addEdge(e2);
        			nodes.get(j).exposed = true;
        			e1.invers = e2;
        			e2.invers = e1;
        		}
        	}
        }
        
        // start the algorithm  
        startAlgo();
      }
      
      /**
       * starts and processes the classical bipartite matching algorithm
       */
    public void startAlgo() {
	  	sc.hide();
	  	gr.hide();
	  	grBackup.hide();
	  	text.hide();
	  	vars.set("p", "()");
	  	vars.set("MatchingSize", "0");
	  	lang.nextStep("Einleitung");
	  	introduction1.hide();
	  	introduction2.hide();
	  	introduction3.hide();
	  	introduction4.hide();
	  	introduction5.hide();
	  	introduction6.hide();
	  	introduction7.hide();
	  	introduction8.hide();
	  	introduction9.hide();
	  	introduction10.hide();
	  	sc.show();
	  	gr.show();
	  	text.show();
	    sc.highlight(1);
	    sc.highlight(15);
	    lang.nextStep("Starte Algorithmus");
	    sc.unhighlight(1);
	    sc.unhighlight(15);
	    ArrayList<EdgeElement> path = findAugmentingPath();
	    int index = 1;
	    
	    while (path != null) {
	    	grBackup.show();
	    	gr.hide();
	    	pathString = new String("(");
	    	for (EdgeElement e : path) {
	    		pathString += gr.getNodeLabel(e.start) + " -> " + gr.getNodeLabel(e.end) + ", ";
	    		grBackup.highlightEdge(e.start, e.end, null, null);
	    	}
	    	pathString = pathString.substring(0, pathString.length()-2) + ")";
	    	vars.set("p", pathString);
	    	pathText.setText("p = " + pathString, null, null);
		    gr.highlightNode(path.get(0).start, null, null);
		    gr.highlightNode(path.get(path.size()-1).end, null, null);
		    grBackup.highlightNode(path.get(0).start, null, null);
		    grBackup.highlightNode(path.get(path.size()-1).end, null, null);
	    	sc.highlight(2);
	    	sc.highlight(3);
	    	sc.highlight(4);
	    	sc.highlight(5);
	    	sc.highlight(6);
	    	sc.highlight(7);
	    	sc.highlight(8);
		    lang.nextStep("Finde n\u00e4chsten Pfad");
		    gr.unhighlightNode(path.get(0).start, null, null);
		    gr.unhighlightNode(path.get(path.size()-1).end, null, null);
		    grBackup.unhighlightNode(path.get(0).start, null, null);
		    grBackup.unhighlightNode(path.get(path.size()-1).end, null, null);
		    gr.show();
		    sc.unhighlight(2);
		    sc.unhighlight(3);
	    	sc.unhighlight(4);
	    	sc.unhighlight(5);
	    	sc.unhighlight(6);
	    	sc.unhighlight(7);
	    	sc.unhighlight(8);
		    for (EdgeElement e : path) {
		    	grBackup.unhighlightEdge(e.start, e.end, null, null);
	    		e.matched = !e.matched;
	    		e.invers.matched = !e.invers.matched;
	    		if (e.matched) {
	    			gr.highlightEdge(e.start, e.end, null,null);	    			
	    		}
	    		else {
	    			gr.unhighlightEdge(e.start, e.end, null,null);		
				}
	    		nodes.get(e.end).exposed = false;
	    		nodes.get(e.start).exposed = false;
		    }
		    grBackup.hide();
		    sc.highlight(11);
		    sc.highlight(12);
		    sc.highlight(13);
		    sc.highlight(14);
		    matchingSize++;
		    vars.set("MatchingSize", String.valueOf(matchingSize));
		    text.setText("Matching cardinality: " + matchingSize, null, null);
		    lang.nextStep("Erh\u00f6he Matching entlang des Pfades");
		    sc.unhighlight(11);
		    sc.unhighlight(12);
		    sc.unhighlight(13);
		    sc.unhighlight(14);
	    	index++;
	        path = findAugmentingPath();
	    }
	    sc.highlight(2);
    	sc.highlight(3);
    	sc.highlight(4);
    	sc.highlight(5);
    	sc.highlight(6);
    	sc.highlight(7);
    	sc.highlight(8);
    	pathString = new String("p = ()");
    	vars.set("p", "()");
    	pathText.setText(pathString, null, null);
	    lang.nextStep("Finde n\u00e4chsten Pfad");
	    sc.unhighlight(2);
	    sc.unhighlight(3);
    	sc.unhighlight(4);
    	sc.unhighlight(5);
    	sc.unhighlight(6);
    	sc.unhighlight(7);
    	sc.unhighlight(8);
	    sc.highlight(9);
	    sc.highlight(10);
	    text.setText("Final matching cardinality: " + matchingSize, null, null);
	    lang.nextStep("Kein neuer Pfad gefunden. Algorithmus terminiert");	  
	    sc.hide();
	    pathText.hide();
	    end1 = lang.newText(new Coordinates(450,200), "Nun gibt es keinen erh\u00f6henden Pfad mehr, daher ", "end1", null);
	    end2 = lang.newText(new Coordinates(450,215), "terminiert der Algorithmus und das Matching ist ", "end2", null);
	    end3 = lang.newText(new Coordinates(450,230), "g\u00fcltig und kardinalit\u00e4ts-maximal mit Kardinalit\u00e4t " + String.valueOf(matchingSize) + ".", "end3", null);
	    end4 = lang.newText(new Coordinates(450,245), "Jeder Knoten ist h\u00f6chstens \u00fcber eine Matching-Kante erreichbar.", "end4", null);
	    lang.nextStep("Schluss");
    }     
    
      /**
       * finds an augmenting path in the graph
       * @return augmenting path as a ArrayList of EdgeElements
       */
      public ArrayList<EdgeElement> findAugmentingPath() {
    	  for (int j = 0; j < numNodes; j++) {
    		  NodeElement n = nodes.get(arrayPositions[j]);
    		  if (n.exposed) {
    			  n.matched = true;
    			  for (NodeElement node : nodes) {
    				  node.currentArc = 0;
    			  }
    			  NodeElement t = getPath(n);
    			  ArrayList<EdgeElement> path = new ArrayList<EdgeElement>();
    			  if (t != null) {
    				  NodeElement cur = t;
    				  do {
    					  for (int i = 0; i < cur.edges.size(); i++) {
    						  if (cur.pred.index == cur.edges.get(i).end) {
    							  path.add(cur.edges.get(i));
    						  }
    					  }
    					  cur = cur.pred;
    				  } while (cur.index != n.index);
    				  for (NodeElement node : nodes) {
    					  node.pred = null;
    				  }
    				  return path;
    					
    			  }
    		  }
    	  }
    	  
    	  return null;
    	  
      }
      
      /**
       * searches a path from an exposed node to another one
       * @param n exposed start node n
       * @return exposed end node of the path or null if there is not such a path
       */
      public NodeElement getPath(NodeElement n) {
    	  if (n.currentArc == n.edges.size())
    		  return null;
    	  EdgeElement e = n.edges.get(n.currentArc);
    	  NodeElement head = nodes.get(e.end);
    	  if (head.pred == null) {
    		  if (n.matched && !e.matched) {
    			  head.pred = n;
    			  if (head.exposed) {
    				  return head;			  
    			  }
    			  head.matched = false;
    			  NodeElement result = getPath(head);
    			  if (result != null)
    				  return result;
    		  }	  
    		  if (!n.matched && e.matched) {
    			  head.pred = n;
    			  if (head.exposed) {
    				  return head;			  
    			  }
    			  head.matched = true;
    			  NodeElement result = getPath(head);
    			  if (result != null)
    				  return result;
    		  }
    	  }
    	  n.currentArc++;
    	  return getPath(n);
    	  }


    public String getName() {
        return "Classical Bipartite Matching Algorithm";
    }

    public String getAlgorithmName() {
        return "Classical Bipartite Matching";
    }

    public String getAnimationAuthor() {
        return "Markus Lehmann, Martin M\u00fcller";
    }

    public String getDescription(){
    	return "Der Classical Bipartite Matching Algorithmus findet auf einem bipartiten Graphen " + 
	    "ein g&uuml;ltiges kardinalit&auml;ts-maximales Matching. Das Matching ist eine Teilmenge der Kanten des " + 
	    "Algorithmus. Damit es g&uuml;ltig ist, darf jeder Knoten des Graphen h&ouml;chstens zu einer gematchten " + 
	    "Kante inzident sein. Kardinalit&auml;ts-maximal bedeutet, dass der Graph keine weiteren Matching-Kanten" + 
	    "mehr erlaubt, da sonst kein g&uuml;ltiges Matching mehr m&ouml;glich w&auml;re. Der Algorithmus startet mit einem" + 
	    "bipartiten Graphen, dessen Kanten alle ungematcht sind. Es wird nun solange ein erh&ouml;hender Pfad " + 
	    "gesucht, bis kein solcher Pfad mehr gefunden wird, dann bricht der Algorithmus ab. Bei einem " + 
	    "erh&ouml;henden Pfad wird die Anzahl der gematchten Kanten im Graph erh&ouml;ht. Ein solcher Pfad muss immer " + 
	    "von einem freien Knoten, also einem Knoten, der zu keiner gematchten Kante inzident ist, zu einem " + 
	    "anderen solchen freien Knoten f&uuml;hren. Wenn ein solcher Pfad gefunden wurde, wird das Matching entlang " + 
	    "dieses Pfades erh&auml;ht. Das bedeutet, dass bisher ungematchte Kanten gematcht werden und bisher " + 
	    "gematchte Kanten wieder ungematcht werden. Dies wird solange durchgef&uuml;hrt, bis es keinen erh&ouml;henden " + 
	    "Pfad mehr gibt.";
     }

    public String getCodeExample(){
        return "public void classicalBipartiteMatching() {"
 +"\n"
 +"  while() {	"
 +"\n"
 +"    Search augmenting path p"
 +"\n"
 +"    if (p == null)"
 +"\n"
 +"      break;"
 +"\n"
 +"    Augment matching along p"
 +"\n"
 +"  }"
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
        return Generator.PSEUDO_CODE_OUTPUT;
    }

	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) throws IllegalArgumentException {
		numberNodesA = (Integer)arg1.get("numberNodesA");
        numberNodesB = (Integer)arg1.get("numberNodesB");
		if (numberNodesA > 0 && numberNodesB > 0 && numberNodesA + numberNodesB < 27)
			return true;
		return false;
	}

}


class NodeElement{
	int index;
	ArrayList<EdgeElement> edges;
	boolean exposed;
	NodeElement pred;
	boolean matched;
	int currentArc;
	
	NodeElement(int i) {
		index = i;
		edges = new ArrayList<EdgeElement>();
		exposed = true;
		pred = null;
		matched = false;
	}
	
	void addEdge(EdgeElement e) {
		edges.add(0, e);
	}
}

class EdgeElement{
	int start, end;
	boolean matched;
	EdgeElement invers;
	
	EdgeElement(int start, int end, boolean matched) {
		this.start = start;
		this.end = end;
		this.matched = matched;
	}
}
