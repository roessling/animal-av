/*
 * complete-linkage.java
 * Niclas Dobbertin, Niklas Adam, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;
import java.util.stream.Collectors;

import algoanim.animalscript.AnimalDoubleMatrixGenerator;
import algoanim.animalscript.AnimalGraphGenerator;
import algoanim.animalscript.AnimalRectGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalStringMatrixGenerator;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;


public class CompleteLinkage implements Generator {
	private Language lang;
	private SourceCodeProperties scProps;
	private MatrixProperties matrixProps;
	private GraphProperties gprops;
	private Graph points;
	private SourceCode code;
	private Graph graph;
	private int[][] adjacencyMatrix;
	private int[][] currentTable;
	private String[] nodeNames;
	private AnimalStringMatrixGenerator StringMatGen;
	private StringMatrix startDistanceTable;
	private StringMatrix currentDistanceTable;
	private Text desc0;
	private Text desc1;
	private Text desc2;
	private Text desc3;
	private Text desc4;
	private Cluster[] clusters;
	private TextProperties textProps;
	private AnimalTextGenerator textGen;
	private AnimalGraphGenerator graphGen;
	private DoubleMatrix heightMap;
	private Text originalDistances;
	private Text currentDistances;
	private Text M;
	private Text Height;

	class Tuple<X, Y> { 
		public final X x; 
		public final Y y; 
		public Tuple(X x, Y y) { 
			this.x = x; 
			this.y = y; 
		} 
	} 

	class Cluster {
		public String label;
		public int position;
		public Cluster element1;
		public Cluster element2;
		public Cluster(int position, Cluster e1, Cluster e2) {   
			this.position = position;
			element1 = e1;
			element2 = e2;
			this.label = "("+e1.label+", "+e2.label+")";
		}
		public Cluster(String label, int position) {   
			this.label = label;
			this.position = position;
		}

		public int getDistanceTo(Cluster dest, int[][] dists) {
			int result = -1;
			int a;
			int b;
			if(element1==null && dest.element1==null)
				if(position<dest.position)
					result = dists[position][dest.position];
				else
					result = dists[dest.position][position];
			else if(element1==null && dest.element1!=null){
				a = dest.element1.getDistanceTo(this, dists);
				b = dest.element2.getDistanceTo(this, dists);
				if(a>b)
					result = a;
				else
					result = b;
			}		
			else if(element1!=null) {
				a = element1.getDistanceTo(dest, dists);
				b = element2.getDistanceTo(dest, dists);
				if(a>b)
					result = a;
				else
					result = b;
			}

			return result;
		}


	}


	public void init(){
		lang = new AnimalScript("Complete-linkage clustering", "Niclas Dobbertin, Niklas Adam", 800, 600);
	}

	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
		lang.setStepMode(true);

		scProps = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
		points = (Graph) primitives.get("points");

		StringMatGen = new AnimalStringMatrixGenerator((AnimalScript) lang);

		int i = 0;
		clusters = new Cluster[points.getNodes().length];
		for(Node n : points.getNodes()) {
			clusters[i] = new Cluster(points.getNodeLabel(n), i);
			i++;
		}

		start();
		System.out.println(lang);

		return lang.toString();
	}

	private void start() {
		//Header
		TextProperties headProps = new TextProperties();
		headProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		headProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		textGen = new AnimalTextGenerator((AnimalScript) lang);
		Text header = new Text(textGen, new Coordinates(20, 30), "Complete-linkage clustering", "header", null, headProps);

		//Rectangle
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		AnimalRectGenerator rectGen = new AnimalRectGenerator((AnimalScript) lang);
		Rect rect = new Rect(rectGen, new Offset(-5, -5, header, "NW"), new Offset(5, 5, header, "SE"), "rect", null, rectProps);

		// Description
		textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		textProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		desc0= new Text(textGen, new Offset(0, 5, rect, "SW"), "Complete-linkage clustering is a type of hierarchical","desc", null, textProps);
		desc1 = new Text(textGen, new Offset(0, 2, desc0, "SW"), "agglomerative clustering algorithm which in each step","desc", null, textProps);
		desc2 = new Text(textGen, new Offset(0, 2, desc1, "SW"), "combines the pair of closest clusters into one. In","desc", null, textProps);
		desc3 = new Text(textGen, new Offset(0, 2, desc2, "SW"), "complete-linkage specifically distance is measured by","desc", null, textProps);
		desc4 = new Text(textGen, new Offset(0, 2, desc3, "SW"), "the two most distant elements (one in each cluster).","desc", null, textProps);

		
		// Graph
		gprops = new GraphProperties();
		gprops.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.blue);
		gprops.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.white);
		gprops.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.white);
		graphGen = new AnimalGraphGenerator((AnimalScript) lang);
		graph = new Graph(graphGen, points, null, gprops);
		adjacencyMatrix = graph.getAdjacencyMatrix();
		nodeNames = new String[graph.getSize()];
		for(int i = 0; i<graph.getSize(); i++)
			nodeNames[i] = graph.getNodeLabel(i);
		graph.hide();


		//Matrices
		matrixProps = new MatrixProperties();
		matrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		matrixProps.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
		startDistanceTable = new StringMatrix(StringMatGen, new Offset(10, 0, graph, "NE"), matrixIntToString(adjacencyMatrix), "startDistanceTable", null, matrixProps);
		currentDistanceTable = new StringMatrix(StringMatGen, new Offset(0, 30, startDistanceTable, "SW"), matrixIntToString(adjacencyMatrix), "currentDistanceTable", null, matrixProps);
		startDistanceTable.hide();
		currentDistanceTable.hide();

		//SourceCode
		//code = lang.newSourceCode(new Offset(0, 5, desc0, "SW"), "sourceCode", null, scProps);
		code = lang.newSourceCode(new Offset(10, 0, startDistanceTable, "NE"), "sourceCode", null, scProps);
		code.addCodeLine("1. At the start the disjoint clustering has a height of 0.", null, 0, null);
		code.addCodeLine("2. Find the most similar pair of clusters in the current clustering.", null, 0, null);
		code.addCodeLine("3. Merge clusters (r) and (s) into a single cluster to form the next clustering m. Set the level of this clustering to L(m) = d[(r),(s)]", null, 0, null);
		code.addCodeLine("4. Update the proximity matrix by deleting the rows and columns corresponding to clusters (r) and (s) and adding a row and column corresponding to the newly formed cluster.", null, 0, null);
		code.addCodeLine("    Update the proximities for the new cluster as it is defined as d[(k), (r,s)] = max d[(k),(r)], d[(k),(s)].", null, 0, null);
		code.addCodeLine("5. If all objects are in one cluster, stop. Else, go to step 2", null, 0, null);
		code.hide();

		// Arrays
		AnimalDoubleMatrixGenerator doubleGen = new AnimalDoubleMatrixGenerator((AnimalScript) lang);
		heightMap = new DoubleMatrix(doubleGen, new Offset(0, 10, currentDistanceTable, "SW"), new double[2][adjacencyMatrix.length], "height", null, matrixProps);
		heightMap.hide();
		// Labels
		originalDistances = new Text(textGen, new Offset(0, -18, startDistanceTable, "NW"), "The original distance table", "originalDistances", null, textProps);
		currentDistances = new Text(textGen, new Offset(0, -18, currentDistanceTable, "NW"), "The current distance table", "currentDistances", null, textProps);
		M = new Text(textGen, new Offset(30, 0, heightMap, "NE"), "m", "m", null, textProps);
		Height = new Text(textGen, new Offset(30, 30, heightMap, "NE"), "height", "height", null, textProps);
		originalDistances.hide();
		currentDistances.hide();
		M.hide();
		Height.hide();
		cluster();
	}

	private String[][] matrixIntToString(int[][] in){
		String[][] out = new String[in.length+1][in.length+1];
		out[0][0] = "";
		for(int i = 1; i < out.length; i++)
			for(int j = 1; j < out.length; j++) {
				out[i][j] = Integer.toString(in[i-1][j-1]);
				out[i][0] = clusters[i-1].label;
				out[0][i] = clusters[i-1].label;
			}

		return out;
	}

	private void cluster() {
		lang.nextStep();

		// init
		highlightSwitch(0);
		desc0.hide();
		desc1.hide();
		desc2.hide();
		desc3.hide();
		desc4.hide();
		startDistanceTable.show();
		currentDistanceTable.show();
		currentTable = clone(graph.getAdjacencyMatrix());
		clone(graph.getAdjacencyMatrix());
		graph.getNodes();
		// 1.Begin with the disjoint clustering having level L(0) = 0 and sequence number m = 0
		code.highlight(0);
		heightMap.put(0, 0, 0, null, null);
		heightMap.put(1, 0, 0, null, null);
		
		originalDistances.show();
		currentDistances.show();
		M.show();
		Height.show();
		heightMap.show();
		code.show();
		lang.nextStep();

		StringMatrix temp = null;
		for(int iterator = adjacencyMatrix.length; iterator > 1; iterator--) {
			// 2.find the most similar pair in the current clustering
			code.unhighlight(0);
			code.unhighlight(5);
			code.highlight(1);
			Tuple<Integer, Integer> sPair = findSmallestDistance(currentTable);
			if(sPair.x == -1)
				endAlgo();
			double dist = currentTable[sPair.x][sPair.y];

			currentDistanceTable.highlightCell(sPair.x +1, sPair.y +1, null, null);
			if(temp!=null)
				temp.highlightCell(sPair.x +1, sPair.y +1, null, null);
			lang.nextStep();

			// 3. merge clusters into one
			code.unhighlight(1);
			code.highlight(2);
			currentTable = shrink(sPair, currentTable);
			int m = -(iterator-adjacencyMatrix.length)+1;
			heightMap.put(0, m, m, null, null);
			heightMap.put(1, m, (dist / 2), null, null);
			heightMap.highlightCell(1, m, null, null);

			lang.nextStep();
			
			heightMap.unhighlightCell(1, m, null, null);

			// 4. update proximity matrix
			code.unhighlight(2);
			code.highlight(3);
			code.highlight(4);
			int[][] stepMatrix = clone(currentTable);
			currentDistanceTable.hide();
			StringMatrix stepDistanceTable = null;
			if(temp!=null)
				temp.hide();
			stepDistanceTable = new StringMatrix(StringMatGen, new Offset(0, 30, startDistanceTable, "SW"), matrixIntToString(stepMatrix), "stepMatrix", null, matrixProps);
			temp = stepDistanceTable;
			int highlightRow = sPair.x+1;
			if(stepMatrix.length==2)
				highlightRow--;
			int nrCols = (int)stepDistanceTable.getNrCols();
			stepDistanceTable.highlightCellColumnRange(highlightRow, 0, nrCols-1, null, null);

			lang.nextStep();
			// 5. If all Objects are in one cluster stop else step 2
			code.unhighlight(3);
			code.unhighlight(4);
			code.highlight(5);
			// unhighlightCellColumnRange unhighlights column null in AnimalScript when cell column = number of columns
			stepDistanceTable.unhighlightCellColumnRange(highlightRow, 0, stepMatrix.length-1, null, null);
			stepDistanceTable.unhighlightCell(highlightRow, nrCols-1, null, null);
			lang.nextStep();
		}
	}

	private int[][] shrink(Tuple<Integer, Integer> pair, int[][] in){
		int[][] out = new int[clusters.length-1][clusters.length-1];
		Cluster merge = new Cluster(pair.x, clusters[pair.x], clusters[pair.y]);
		Cluster[] tempC = new Cluster[clusters.length-1];
		int k = 0;
		for(int i = 0; i < clusters.length-1; i++) {
			if(i == pair.y)
				k++;
			tempC[i] = clusters[k];
			if(i == pair.x)
				tempC[i] = merge;
			k++;
		}

		if(out.length == 2) {
			if(pair.x == 0) {
				out[0][1] = merge.getDistanceTo(clusters[1], adjacencyMatrix);
			}else {
				out[0][1] = merge.getDistanceTo(clusters[0], adjacencyMatrix);
			}

		}else {
			int g = 0;
			for(int i = 0; i < out.length; i++) {
				int h = -1;

				if(i==pair.y) {
					g++;
				}
				for(int j = 0; j < out.length; j++) {
					h++;
					//skip column
					if(j==pair.y)
						h++;
					if(i>=j) {
						continue;
					}

					out[i][j] = currentTable[g][h];
					if(i==pair.x) {
						out[i][j] = merge.getDistanceTo(clusters[h], adjacencyMatrix);
					}

				}
				g++;
			}
		}
		String[][] converted = new String[out.length][];
		for(int index = 0; index < out.length; index++) {
			converted[index] = new String[out[index].length];
			for(int subIndex = 0; subIndex < out[index].length; subIndex++){
				converted[index][subIndex] = Integer.toString(out[index][subIndex]);
			}
		}
		String result = Arrays
				.stream(converted)
				.map(Arrays::toString) 
				.collect(Collectors.joining(System.lineSeparator()));
		System.out.println(result);
		clusters = tempC;
		return out;

	}


	private int[][] clone(int[][] in){
		int[][] out = new int[in.length][in.length];
		for(int i = 0; i < in.length; i++)
			for(int j = 0; j < in.length; j++)
				out[i][j] = in[i][j];
		return out;
	}

	private Tuple<Integer, Integer> findSmallestDistance(int[][] matrix) {
		Tuple<Integer, Integer> shortestPair = new Tuple<Integer, Integer>(-1, -1);
		int shortestDist = 0;
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix.length; j++) {
				if(matrix[i][j] != 0 && matrix[i][j] < shortestDist || shortestDist == 0) {
					shortestDist = matrix[i][j];
					shortestPair = new Tuple<Integer, Integer>(i, j);
				}
			}
		}	
		return shortestPair;
	}

	private void endAlgo() {
		System.out.println("AlgoEnd");
	}

	private void highlightSwitch(int primitive) {
		switch(primitive) {
		case(0):	//Edges
			for(int i = 0; i<graph.getSize(); i++)
				for(int j = 0; j<graph.getSize(); j++)
					graph.hideEdge(i, j, null, null);
		break;
		case(1):	//Nodes
			for(int i = 0; i<graph.getSize(); i++)
				graph.unhighlightNode(i, null, null);
		break;	
		case(2):	//Sourcecode
			for(int i=0; i < code.length(); i++) 
				code.unhighlight(i);
		break;
		case(3):	//BlueEdge
			for(int i = 0; i<graph.getSize(); i++)
				for(int j = 0; j<graph.getSize(); j++)
					graph.setEdgeHighlightPolyColor(i, j, Color.BLUE, null, null);
		break;
		case(4):	//RedEdge
			for(int i = 0; i<graph.getSize(); i++)
				for(int j = 0; j<graph.getSize(); j++) {
					graph.highlightEdge(i, j, null, null);
					graph.setEdgeHighlightPolyColor(i, j, Color.RED, null, null);
				}
		break;
		}
	}

	public String getName() {
		return "Complete-linkage clustering";
	}

	public String getAlgorithmName() {
		return "Complete-linkage clustering";
	}

	public String getAnimationAuthor() {
		return "Niclas Dobbertin, Niklas Adam";
	}

	public String getDescription(){
		return   "Complete-linkage clustering is a type of hierarchical "
				+ "agglomerative clustering algorithm which in each step "
				+ "combines the pair of closest clusters into one. In "
				+ "complete-linkage specifically distance is measured by "
				+ "the two most distant elements (one in each cluster)."
				;
	}

	public String getCodeExample(){
		return "1. Begin with the disjoint clustering having level L(0) = 0 and sequence number m = 0."
				+"\n"
				+"2. Find the most similar pair of clusters in the current clustering, say pair (r), (s), according to d[(r),(s)] = max d[(i),(j)] where the maximum is over all pairs of clusters in the current clustering."
				+"\n"
				+"3. Increment the sequence number: m = m + 1. Merge clusters (r) and (s) into a single cluster to form the next clustering m. Set the level of this clustering to L(m) = d[(r),(s)]"
				+"\n"
				+"4. Update the proximity matrix, D, by deleting the rows and columns corresponding to clusters (r) and (s) and adding a row and column corresponding to the newly formed cluster. The proximity between the new cluster, denoted (r,s) and old cluster (k) is defined as d[(k), (r,s)] = max d[(k),(r)], d[(k),(s)]."
				+"\n"
				+"5. If all objects are in one cluster, stop. Else, go to step 2";
	}

	public String getFileExtension(){
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	public static void main(String[] args) {
		CompleteLinkage generator = new CompleteLinkage(); // Generator erzeugen
		Animal.startGeneratorWindow(generator); // Animal mit Generator starten
	}


}
