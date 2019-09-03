package gfgaa.gui.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.HashMap;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class GraphReader {

//  public boolean     isDirected;
//  public boolean     isWeighted;
  String             fileName;
//  Node[]             nodes;
//  int[][]            matrix;
//  String[]           labels;
//  int                startX;
//  int                startY;
//
//  int                nrNodes;
//  int                xCoordinates;
//  int                yCoordinates;
//  protected Language lang;
//  GraphProperties    graphProps;
//  Graph              graph;
//  public String      startNodeName;
//  public String      targetNodeName;
  //  public Node        start;
  //  public Node        target;

  public GraphReader(String filename) {
    fileName = filename;
    // readFile();
  }

  protected int parseHeader(StreamTokenizer stok) throws IOException {
    int token = -42;
    token = stok.nextToken();
    if (token != '%')
      System.err.println("expected '%', but received " +token +", " +stok.sval);
    token = stok.nextToken();
    if (token != StreamTokenizer.TT_WORD || !stok.sval.equalsIgnoreCase("graphscript"))
      throw new IOException("Parse Error: '%graphscript' expected in graph definition.");
    token = skipEmptyLines(stok);
//    token = stok.nextToken();
//    if (token == StreamTokenizer.TT_WORD && stok.sval.trim().length() == 0)
//      token = stok.nextToken();
    if (token != StreamTokenizer.TT_WORD || !("graph".equalsIgnoreCase(stok.sval)))
      throw new IOException("Parse Error: expected 'graph SIZE'");
    token = stok.nextToken();
    if (token != StreamTokenizer.TT_NUMBER)
      throw new IOException("Parse Error: expected size for graph");
    int size = (int)stok.nval;
    return size;
  }

  protected int skipEmptyLines(StreamTokenizer stok) throws IOException {
    boolean skip = true;
    int myToken = -42;
    while (skip) {
      myToken = stok.nextToken();
      if (myToken == StreamTokenizer.TT_EOL) {
        skip = true;
      }
      else if (myToken == StreamTokenizer.TT_WORD) {
        String val = stok.sval.trim();
        skip = (val.length() == 0);
      }
      else skip = false;
    }
    return myToken;
  }
  
  public Graph readGraph(String content, boolean fromDisk) {
    int[][] adjacencyMatrix = null;
    GraphProperties graphProperties = new GraphProperties();
    String[] nodeLabels = null;
    Node[] nodes = null;
    Node startNode = null, targetNode = null;
//    int topX = 0, topY = 0;
//    Graph localGraph = null;
//    int graphSize = 0;
//    String startNodeName = null, endNodeName = null;
//    boolean isDirected = false, isWeighted = false;
//    String currentLine;
//    StringTokenizer tokenizer;
//    lang = new AnimalScript("Warshall Animation", "Madieha und Bouchra", 620,
//        480);
//    Graph graph = null;
    Reader r = null;
//    String line = null;
    try {
      if (fromDisk) {
        r = new FileReader(this.fileName);
      } else {
        String newContent = content.replaceAll("\\n", " ");
        r = new StringReader(newContent);
      }
      BufferedReader br = new BufferedReader(r);
//      StringBuffer graphString = new StringBuffer(4096);
//      while ((line = br.readLine()) != null)
//        graphString.append(line).append("\n");
//      br.close();
//      r.close();
      StreamTokenizer stok = new StreamTokenizer(r);
      stok.eolIsSignificant(true);
      int token = 0;

      // extract size and initialize arrays
      int graphSize = parseHeader(stok);
      HashMap<String, Integer> namesToIndex = new HashMap<String, Integer>(graphSize * 3 + 11);

      nodes = new Node[graphSize];
      nodeLabels = new String[graphSize];
      adjacencyMatrix = new int[graphSize][graphSize];
      
      // check if directed or weighted
      token = stok.nextToken();
      boolean isDirected = false, isWeighted = false;
      if (token == StreamTokenizer.TT_WORD && !"graphcoordinates".equalsIgnoreCase(stok.sval)) {
        isDirected = "directed".equalsIgnoreCase(stok.sval);
        isWeighted = "weighted".equalsIgnoreCase(stok.sval);
        if (isDirected) {
          token = stok.nextToken();
          if (token == StreamTokenizer.TT_WORD)
            isWeighted = "weighted".equalsIgnoreCase(stok.sval);
        }
      }
      graphProperties.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, isDirected);
      graphProperties.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, isWeighted);

      if(!"graphcoordinates".equalsIgnoreCase(stok.sval) && (isDirected || isWeighted)) {
        token = skipEmptyLines(stok);
      }
      // check for "graphcoordinates" keyword
      if (token == StreamTokenizer.TT_WORD 
          && "graphcoordinates".equalsIgnoreCase(stok.sval)) {
        // skip "at"
        token = stok.nextToken();
        // retrieve x, y
        token = stok.nextToken();
        @SuppressWarnings("unused")
        int topX = 0, topY = 0;
        if (token == StreamTokenizer.TT_NUMBER)
          topX = (int)stok.nval;
        token = stok.nextToken();
        if (token == StreamTokenizer.TT_NUMBER)
          topY = (int)stok.nval;
      }
      
      String startNodeName = null, endNodeName = null;
      // check for start node
      token = skipEmptyLines(stok);

      if(!"node".equalsIgnoreCase(stok.sval)) {
        if (token == StreamTokenizer.TT_WORD) {
          String value = stok.sval;
          if ("startknoten".equalsIgnoreCase(value)) {
            token = skipEmptyLines(stok);
            if (token == StreamTokenizer.TT_WORD) {
              startNodeName = stok.sval;
            }
          }
        }
        
        // check for start node
        token = skipEmptyLines(stok);
        if (token == StreamTokenizer.TT_WORD) {
          String value = stok.sval;
          if ("zielknoten".equalsIgnoreCase(value)) {
            token = skipEmptyLines(stok);
            if (token == StreamTokenizer.TT_WORD) {
              endNodeName = stok.sval;
            }
          } else stok.pushBack();
        }
      }else {
        stok.pushBack();
      }
      
      int index = 0, x = -1, y = -1;
      while ((token = skipEmptyLines(stok)) != StreamTokenizer.TT_EOF
          && token == StreamTokenizer.TT_WORD && "node".equalsIgnoreCase(stok.sval)) {
        String word = stok.sval;
        if ("node".equalsIgnoreCase(word)) {
          token = stok.nextToken();
          if (token != StreamTokenizer.TT_WORD)
            throw new IOException("Expected node name after keyword 'node'");
          String nodeName = stok.sval;
          nodeLabels[index] = nodeName;
          namesToIndex.put(nodeName, Integer.valueOf(index));
          // skip "at"
          token = stok.nextToken();
          // read x, y
          token = stok.nextToken();
          if (token != StreamTokenizer.TT_NUMBER)
            throw new IOException("expected int value for node " +nodeName +" x");
          x = (int)stok.nval;
          token = stok.nextToken();
          if (token != StreamTokenizer.TT_NUMBER)
            throw new IOException("expected int value for node " +nodeName +" y");
          y = (int)stok.nval;
          nodes[index] = new Coordinates(x, y);
          if (nodeName.equals(startNodeName)) {
            startNode = nodes[index];
          }
          if (nodeName.equals(endNodeName)) {
            targetNode = nodes[index];
          }
          index++;
        } else
          throw new IOException("expected token 'node'");
      }
      stok.pushBack();
      
      // parse edges
      while ((token = skipEmptyLines(stok)) != StreamTokenizer.TT_EOF
          && token == StreamTokenizer.TT_WORD && "edge".equalsIgnoreCase(stok.sval)) {
        String word = stok.sval;
        if ("edge".equalsIgnoreCase(word)) {
          token = stok.nextToken();
          if (token != StreamTokenizer.TT_WORD)
            throw new IOException("Expected edge start name after keyword 'edge'");
          String edgeStartNode = stok.sval;
          token = stok.nextToken();
          if (token != StreamTokenizer.TT_WORD)
            throw new IOException("Expected edge end name after keyword 'edge'");
          String endNode = stok.sval;
          int edgeWeight = 1;
          if (isWeighted) {
            token = stok.nextToken();
            if (token != StreamTokenizer.TT_WORD ||
                !("weight".equalsIgnoreCase(stok.sval)))
              throw new IOException("Expected keyword 'weight' for weighted edge");
            token = stok.nextToken();
            if (token == StreamTokenizer.TT_NUMBER)
              edgeWeight = (int)stok.nval;
            else
              throw new IOException("Edge weight expected for edge " 
                  + edgeStartNode + " -> " +endNode);
          }
          int startIndex = namesToIndex.get(edgeStartNode).intValue();//getNodeByName(startNode);
          int endIndex = namesToIndex.get(endNode).intValue();//getNodeByName(endNode);
          adjacencyMatrix[startIndex][endIndex] = edgeWeight;
        } else
          throw new IOException("expected token 'edge'");
      }
      br.close();
      r.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();

    } catch (IOException e) {
      e.printStackTrace();
    }
    Graph graph = null;
    if (adjacencyMatrix != null && nodes != null) {
      Language lang = new AnimalScript("","",10,10);
      graph = lang.newGraph("graph", adjacencyMatrix, nodes, nodeLabels, null, graphProperties);
      graph.setStartNode(startNode);
      graph.setTargetNode(targetNode);
//      graph.getProperties().set("weighted", isWeighted);
//      graph.getProperties().set("directed", isDirected);
    }

    return graph;
  }
  /*
  public Graph readGraphDeprecated(String content, boolean fromDisk) {
//  public Graph readFile() {
    String eingeleseneZeile;
    StringTokenizer tokenizer;
//    lang = new AnimalScript("Warshall Animation", "Madieha und Bouchra", 620,
//        480);
    graphprops = new GraphProperties();
    graph = null;
    Reader r = null;

    try {
      if (fromDisk) {
        r = new FileReader(this.filename);
      } else
        r = new StringReader(content);
      BufferedReader br = new BufferedReader(r);
          
      eingeleseneZeile = br.readLine();
      eingeleseneZeile = br.readLine();

      while ((eingeleseneZeile = br.readLine()) != null) {

        tokenizer = new StringTokenizer(eingeleseneZeile, " ");

        if (eingeleseneZeile.contains("graphcoordinates")) {
          tokenizer.nextToken().trim();
          tokenizer.nextToken().trim();
          String x = tokenizer.nextToken().trim();

          obenlinksx = Integer.parseInt(x);
          // System.out.println("x1"+obenlinksx);

          String y = tokenizer.nextToken().trim();
          obenlinksy = Integer.parseInt(y);
          // System.out.println("y1"+obenlinksy);

          eingeleseneZeile = br.readLine();
        }

        if (eingeleseneZeile.contains("graph")) {
          tokenizer = new StringTokenizer(eingeleseneZeile, " ");

          tokenizer.nextToken().trim();

          String dire = tokenizer.nextToken().trim();
          NodeAnzahl = Integer.parseInt(dire);
          node = new Node[NodeAnzahl];
          labels = new String[NodeAnzahl];
          matrix = new int[NodeAnzahl][NodeAnzahl];
          // System.out.println("nodeAnzahl"+NodeAnzahl);
          int time = tokenizer.countTokens();
          String s;
          if (time != 0 && (s = tokenizer.nextToken().trim()) != null
              && s.contains("directed")) {

            // System.out.println("toknizer  : ");
            isdirected = true;
            graphprops.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
          }
          // System.out.println("isdirected  "+isdirected);
          time = tokenizer.countTokens();
          if (time != 0 && (s = tokenizer.nextToken().trim()) != null
              && s.contains("weighted")) {

            // System.out.println("toknizer    :"+dire);
            isweighted = true;
            graphprops.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
            // System.out.println("isweighted   "+isweighted);
          }

        }

        if (eingeleseneZeile.contains("startknoten")) {
          tokenizer = new StringTokenizer(eingeleseneZeile, " ");
          tokenizer.nextToken().trim();
          startKnoten = tokenizer.nextToken().trim();
        }

        if (eingeleseneZeile.contains("zielknoten")) {
          tokenizer = new StringTokenizer(eingeleseneZeile, " ");
          tokenizer.nextToken().trim();
          zielKnoten = tokenizer.nextToken().trim();

        }

        else {

          int indexnode = 0;
          while (eingeleseneZeile.contains("node")) {
            tokenizer = new StringTokenizer(eingeleseneZeile, " ");
            tokenizer.nextToken().trim();
            String KnotenName = tokenizer.nextToken().trim();
            labels[indexnode] = KnotenName;
            tokenizer.nextToken().trim();
            String name = tokenizer.nextToken().trim();
            xCoordinates = Integer.parseInt(name) + obenlinksx;
            // System.out.println("xCoordintes  "+xCoordinates);
            name = tokenizer.nextToken().trim();
            yCoordinates = Integer.parseInt(name) + obenlinksy;
            // System.out.println("yCoordintaes"+yCoordinates);

            node[indexnode] = new Coordinates(xCoordinates, yCoordinates);

            if (KnotenName.equals(startKnoten)) {
              start = node[indexnode];
            }
            if (KnotenName.equals(zielKnoten)) {
              ziel = node[indexnode];
            }
            eingeleseneZeile = br.readLine();
            indexnode++;
          }
        }

        int indexnode = 0, indexzielnode = 0;
        while (eingeleseneZeile != null && eingeleseneZeile.contains("edge")) {
          tokenizer = new StringTokenizer(eingeleseneZeile, " ");
          tokenizer.nextToken().trim();

          String name = tokenizer.nextToken().trim();
          indexnode = getNodeByName(name);
          // System.out.println("indexnode " +indexnode);
          name = tokenizer.nextToken().trim();
          indexzielnode = getNodeByName(name);
          // System.out.println("ziele node   "+indexzielenode);
          int time = tokenizer.countTokens();
          String s;
          if (time != 0 && (s = tokenizer.nextToken().trim()) != null
              && s.contains("weight")) {

            name = tokenizer.nextToken().trim();
            int temp = Integer.parseInt(name);
            // Uepruefen, ob die knoten vorhin existieren
            if (indexnode != -1 && indexzielnode != -1) {
              matrix[indexnode][indexzielnode] = temp;
            }
          } else {
            if (indexnode != -1 && indexzielnode != -1)
              matrix[indexnode][indexzielnode] = 1;
          }
          // System.out.println("edge["+indexnode+"]["+indexzielenode+"]  :" +
          // matrix[indexnode][indexzielenode]);
          eingeleseneZeile = br.readLine();

        }
      }

      br.close();

      if (matrix != null && node != null && matrix.length == node.length) {
        graph = lang.newGraph("graph", matrix, node, labels, null, graphprops);
        graph.setStartKnoten(start);
        graph.setZielKnoten(ziel);
        graph.getProperties().set("weighted", isweighted);
        graph.getProperties().set("directed", isdirected);
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();

    } catch (IOException e) {
      e.printStackTrace();
    }

    return graph;
  }

//  public int getNodeByName(String name) {
//    int index = -1;
//    for (int i = 0; i < labels.length; i++) {
//      if (name.equals(labels[i]))
//        index = i;
//    }
//    return index;
//  }*/

  /*
   * public static void main(String []args){ GraphReader t =new
   * GraphReader("dddd.txt");
   * 
   * 
   * }
   */
  public static void main(String[] args) {
    String newInput = "%graphscript\n\ngraph 5 directed weighted\n\ngraphcoordinates at 0 0\nstartknoten A\nzielknoten E\n\nnode A at 66 68\nnode B at 250 73\nnode C at 64 221";
    newInput += "\nnode D at 245 227\nnode E at 305 386\n\nedge A B weight 1\nedge A C weight 2\nedge A D weight 6\nedge B D weight 2\nedge C D weight 2\nedge C E weight 1\nedge D E weight 5";
//    String inputString = "%graphscript\n\ngraph 8\ngraphcoordinates at 600 30\n\nstartknoten S\nnode R at 39 115\nnode S at 140 115\nnode T at 232 116\nnode U at 353 115\nnode V at 39 204\nnode W at 140 210\nnode X at 232 210\nnode Y at 353 210\n\nedge R S\nedge R V\nedge S W\nedge T W\nedge T U\nedge T X\nedge U Y\nedge U X\nedge W X\nedge X Y";
    GraphReader r = new GraphReader("nix");
    Graph graph = r.readGraph(newInput, false);
    System.err.println(graph);
//    Graph graph = r.readGraph(inputString, false);
  }
}
