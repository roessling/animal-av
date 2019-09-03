/*
 * TestAnimal.java
 * Christian Dreger, 2016 for the Animal project at TU Darmstadt.
 */
package generators.misc;

import generators.backtracking.CSP;
import generators.cryptography.BB84;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import java.util.Random;

import javax.swing.JOptionPane;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Circle;
import algoanim.primitives.DoubleArray;
import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.IntArray;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Polygon;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.graph.BidirectionalDijkstra2;
import generators.graph.EdmondsAlgorithm;
import generators.graph.Tarjan;
import generators.graph.bronkerbosch.BronKerboschWithoutPivoting;
import generators.hashing.Adler32;
import generators.hashing.OpenAddressingHashing;
import generators.helpers.NodeElem;
import generators.misc.processScheduling.ProcessSchedulingEDF;
import generators.searching.interpolatedsearching.InterpolationSearchWrapper;
import generators.tree.TreeLabeling;
import interactionsupport.models.MultipleChoiceQuestionModel;
import generators.misc.processScheduling.ProcessEDF;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.bbcode.Graph;
import algoanim.exceptions.NotEnoughNodesException;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import algorithm.search.InterpolationSearch;
import animal.animator.Move;
import animal.main.Animal;

@SuppressWarnings("unused")
public class AAABBBCCC implements ValidatingGenerator {
    private Language lang;
	private String[][] stringMatrix;
	private int maxTime;
    private RectProperties rect_properties_currentTime;
    private RectProperties rect_properties_wait;
    private RectProperties rect_properties_execute;
    private SourceCodeProperties sourceCodeProperties;
    
    
    public static void main(String[] args){
//      Generator g = new BB84();
//      Generator g = new EDF_preemptive_Scheduling();
      Generator g = new AAABBBCCC();
      g.init();
//      Animal.startAnimationFromAnimalScriptCode(g.generate(null, null));
      Animal.startGeneratorWindow(g);
    }
    
    
    public AAABBBCCC() {
      System.err.println("AAABBBCCC");
    }

    public void init(){
        lang = new AnimalScript(getName(), getAnimationAuthor(), 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {

//    	testForumInput();
      

//      testStringMatrix();
//    	testIntMatrix();
//    	testDoubleMatrix();
//    	testQuestions();
		testGraph();
//    	testStringArray();
//    	testIntArray();
//    	testDoubleArray();

    	boolean testFileImport = false;
    	if(testFileImport){
        	String temp = "";
			try {
				FileReader fr = new FileReader("D:\\AnimalTest2.txt");
				BufferedReader br = new BufferedReader(fr);
				String s;
				while((s = br.readLine()) != null) {
					temp = temp + s + "\n";
				}
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			temp = temp + "\n";
      return temp;
      //return lang.toString()+"\n"+temp;
    	}
    	
        return lang.toString();
    }
    
    private void testForumInput(){
//    	String[][] sMatrix = 
//			{{"0","1","2","3","4","5","6","7","8"},
//			{"0","1","2","3","4","5","6","7","8"},
//			{"0","1","2","3","4","5","6","7","8"},
//			{"0","1","2","3","4","5","6","7","8"},
//			{"0","1","2","3","4","5","6","7","8"},
//			{"0","1","2","3","4","5","6","7","8"},
//			{"0","1","2","3","4","5","6","7","8"},
//			{"0","1","2","3","4","5","6","7","8"},
//			{"0","1","2","3","4","5","6","7","8"},
//			};
//
//    	MatrixProperties smp = new MatrixProperties();
//      smp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
//      smp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("serif",Font.BOLD,18));
//      smp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
//      smp.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
//        StringMatrix sm = lang.newStringMatrix(new Coordinates(100, 100), sMatrix, "TestMatrix", null, smp);
//        lang.nextStep();
//        sm.setGridHighlightFillColor(0, 0, Color.RED, null, null);
//        sm.highlightCellColumnRange(1, 0, sMatrix[1].length-1, null, null);
//        lang.nextStep();
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        lang.nextStep();
        int id = 0;
        MultipleChoiceQuestionModel name = new MultipleChoiceQuestionModel("Derp"+(id++));
        name.setPrompt("Wohin geht der Aufzug?");
        name.addAnswer("quesiton"+(id++), "Hoch", 1, "Richtig");
        name.addAnswer("quesiton"+(id++), "Runter", 0, "Falsch");
        lang.addMCQuestion(name);
        lang.finalizeGeneration();
    }
    
    private void testStringMatrix() {
    	String[][] sMatrix = 
			{{"0","1","2","3","4","5","6","7","8"},
			{"0","1","2","3","4","5","6","7","8"},
			{"0","1","2","3","4","5","6","7","8"},
			{"0","1","2","3","4","5","6","7","8"},
			{"0","1","2","3","4555","5","6","7","8"},
			{"0","1","2","3","4","5","6","7","8"},
			{"0","1","2","3","4","5","6","7","8"},
			{"0","1","2","3","4","5","6","7","8"},
			{"0","1","2","3","4","5","6","7","8"},
			};

    	MatrixProperties smp = new MatrixProperties();
    	smp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
    	smp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("serif",Font.BOLD,18));
    	smp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    	smp.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
        StringMatrix sm = lang.newStringMatrix(new Coordinates(100, 100), sMatrix, "TestMatrix", null, smp);
        Text text = lang.newText(new Coordinates(10, 10), "Test1", "TestText", null);
        lang.nextStep();
        text.setText("Test2", null, null);
        sm.put(4, 4, "Hallo", null, null);
        sm.setGridFont(4, 4, new Font("serif",Font.BOLD,35), null, null);
        sm.swap(0, 0, 1, 1, null, null);
        sm.setGridHighlightFillColor(0, 0, Color.RED, null, null);
        for (int i = 0; i < sMatrix.length; i++) {
			for (int j = 0; j < sMatrix[i].length; j++) {
				if((i+j)%2==0){
					sm.highlightCell(i, j, null, null);
				}
			}
		}
        for (int i = 0; i < sMatrix.length; i++) {
			for (int j = 0; j < sMatrix[i].length; j++) {
				if((i+j)%2==1){
					sm.highlightElem(i, j, null, null);
				}
			}
		}
        for (int i = 0; i < sMatrix.length; i++) {
			for (int j = 0; j < sMatrix[i].length; j++) {
				if(i>2 && i<6 && j>2 && j<6){
			        sm.setGridHighlightBorderColor(i, j, Color.RED, null, null);
			        sm.setGridBorderColor(i, j, Color.BLUE, null, null);
				}
			}
		}
        lang.nextStep();
        text.setText("Test3", null, null);
        lang.nextStep();
        text.hide();
        text.setText("Test4", null, null);
        sm.hide();
//        for (int i = 0; i < sMatrix.length; i++) {
//			for (int j = 0; j < sMatrix[i].length; j++) {
//				if((i+j)%2==0){
//					sm.unhighlightCell(i, j, null, null);
//				}
//			}
//		}
//        for (int i = 0; i < sMatrix.length; i++) {
//			for (int j = 0; j < sMatrix[i].length; j++) {
//				if((i+j)%2==1){
//					sm.unhighlightElem(i, j, null, null);
//				}
//			}
//		}
//        sm.setGridBorderColor(4, 4, Color.GREEN, null, null);
//        lang.nextStep();
//        sm.setGridBorderColor(4, 5, Color.GREEN, null, null);
//        lang.nextStep();
//        lang.hideAllPrimitivesExcept(sm);
//        lang.nextStep();
//        text.show();
//        sm.show();
    }
    
    private void testIntMatrix() {
    	int[][] iMatrix = {{0,1,2,3,4,5,6,7,8},{0,1,2,3,4,5,6,7,8}};

    	MatrixProperties smp = new MatrixProperties();
    	smp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
        IntMatrix im = lang.newIntMatrix(new Coordinates(100, 100), iMatrix, "TestMatrix", null, smp);
        for (int i = 0; i < iMatrix.length; i++) {
			for (int j = 0; j < iMatrix[i].length; j++) {
				if((i+j)%2==0){
					im.highlightCell(i, j, null, null);
				}
			}
		}
        im.setGridBorderColor(0, 4, Color.GREEN, null, null);
        lang.nextStep();
        im.setGridBorderColor(0, 5, Color.GREEN, null, null);
        im.put(0, 4, 0, null, null);
        lang.nextStep();
        im.swap(0, 4, 1, 5, null, null);
    }
    
    private void testDoubleMatrix() {
    	double[][] dMatrix = {{0.1,1.1,2.1,3.1,4.1,5.1,6.1,7.1,8.1},{0.1,1.1,2.1,3.1,4.1,5.1,6.1,7.1,8.1}};

    	MatrixProperties smp = new MatrixProperties();
    	smp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
    	DoubleMatrix dm = lang.newDoubleMatrix(new Coordinates(100, 100), dMatrix, "TestMatrix", null, smp);
        for (int i = 0; i < dMatrix.length; i++) {
			for (int j = 0; j < dMatrix[i].length; j++) {
				if((i+j)%2==0){
					dm.highlightCell(i, j, null, null);
				}
			}
		}
        dm.setGridBorderColor(0, 4, Color.GREEN, null, null);
        lang.nextStep();
        dm.setGridBorderColor(0, 5, Color.GREEN, null, null);
    }

	private void testQuestions() {
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		
    	lang.newText(new Coordinates(50, 100), "Test", "TestText", null);
				
		MultipleChoiceQuestionModel q1 = new MultipleChoiceQuestionModel("MC");
		q1.setPrompt("Was ist 1 + 1?"); 
	
		q1.addAnswer("3", 0, "Falsch!");
		q1.addAnswer("2", 1, "Richtig!"); 
		q1.addAnswer("1", 0, "Falsch!"); 
	
		q1.setNumberOfTries(1); 
	
		lang.addMCQuestion(q1);
		lang.nextStep();
	 	
		MultipleChoiceQuestionModel q2 = new MultipleChoiceQuestionModel("MC2");
		q2.setPrompt("Was ist 2 + 2?"); 
	
		q2.addAnswer("3 Hallo", 0, "Falsch!");
		q2.addAnswer("2", 1, "Richtig du King!"); 
		q2.addAnswer("1", 0, "Falsch du Noob!"); 
	
		q2.setNumberOfTries(1); 
		lang.addMCQuestion(q2);
		
		lang.finalizeGeneration();
	}

	private void testGraph() {
		Node node1 = new Coordinates(100, 100);
		Node node2 = new Coordinates(300, 300);
		Node node3 = new Coordinates(100, 300);
		NodeElem elem1 = new NodeElem(0, node1);
		NodeElem elem2 = new NodeElem(1, node2);
		NodeElem elem3 = new NodeElem(2, node3);
		Node[] graphNodes = {node1, node2, node3};
		String[] graphNames = {"1", "2", "3"};
		int[][] adjaMatrix = new int[3][3];
		adjaMatrix[0][1] = -5;
		adjaMatrix[0][2] = 1;
		GraphProperties graphProps = new GraphProperties();
		graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, false);
		graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
		graphProps.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.BLACK);
		graphProps.set(AnimationPropertiesKeys.NODECOLOR_PROPERTY, Color.WHITE);
		graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);
		lang.newText(new Coordinates(100, 100), "TestTest", "TestName", null);
		algoanim.primitives.Graph graph = lang.newGraph("graph", adjaMatrix, graphNodes, graphNames, null, graphProps);
//		graph.moveTo(AnimalScript.DIRECTION_SE, "translate", new Offset(100, 100, "TestName", AnimalScript.DIRECTION_NW), null, null);
		graph.setNodeHighlightTextColor(node2, Color.RED, null, null);
		try {
      Polygon line1 = lang.newPolygon(new Node[]{graph.getNode(1),graph.getNode(2)}, "TestPolyGon", null);
      Polyline line2 = lang.newPolyline(new Node[]{graph.getNode(1),graph.getNode(2)}, "TestPolyLine", null);
      Node[] nodelist = line2.getNodes();
      for(Node n : nodelist) {
        System.out.println(((Coordinates)n).getX());
      }
      lang.nextStep();
      line2.moveBy("translate #1", 10, 10, null, null);
    } catch (NotEnoughNodesException e) {
      e.printStackTrace();
    }
		lang.nextStep();
		adjaMatrix[0][2] = 10;
//		adjaMatrix[1][2] = -18;
    adjaMatrix[0][1] = 0;
		graph.setAdjacencyMatrix(adjaMatrix);
		graph.hideEdge(0, 1, null, null);
		graph.setEdgeWeight(node2, node3, "2.02", null, null);//new TicksTiming(300)
		
		graph.highlightNode(node2, null, null);
		graph.setNodeHighlightFillColor(node2, Color.BLUE, null, null);
		graph.highlightEdge(node2, node3, null, null);
		graph.highlightEdge(node3, node1, null, null);
		graph.highlightNode(node3, null, null);

    graph.translateNodes(new int[]{1,0}, new Coordinates(200, 200), null, null);
//    graph.translateNode(0, new Coordinates(10,10), null, null);
		graph.setNodeRadius(node2, 50, null, null);
		
		lang.nextStep();
		graph.setNodeHighlightTextColor(node2, Color.ORANGE, null, null);
		graph.setNodeHighlightFillColor(node2, Color.YELLOW, null, null);
		graph.highlightNode(node1, null, null);
		graph.setEdgeHighlightPolyColor(node2, node3, Color.RED, null, null);
		graph.setEdgeHighlightTextColor(node2, node3, Color.RED, null, null);

		lang.nextStep();
		graph.unhighlightNode(node2, null, null);
		graph.setNodeLabel(node3, "Test", null, null);
		graph.setNodeRadiusAuto(node2, null, null);
		
		lang.nextStep();
		graph.highlightNode(node2, null, null);
		graph.unhighlightEdge(node2, node3, null, null);
		graph.setNodeLabel(node3, "Test3", null, null);
	}

	private void testStringArray() {
    	String[] arrayOrg = {"0","1","2","3","4","5","6","7","8"};
    	ArrayProperties ap = new ArrayProperties();
    	ap.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.YELLOW);
    	ap.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
    	ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    	ap.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    	ap.set(AnimationPropertiesKeys.DIRECTION_PROPERTY, true);
    	StringArray array = lang.newStringArray(new Coordinates(100, 100), arrayOrg, "TestArray", null, ap);
    	
    	ArrayMarkerProperties amp = new ArrayMarkerProperties();
    	amp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "Hier");
    	ArrayMarker am = lang.newArrayMarker(array, 4, "ArrayMarker", null, amp);

    	array.put(4, "TraTra", null, null);
    	array.swap(0, 4, null, null);
    
    	lang.nextStep();
    	
    	array.showIndices(false, null, null);
    	am.moveBeforeStart(null, null);
    	array.put(5, "Was los", null, null);
    	array.highlightCell(1, null, null);
    	
    	lang.nextStep();
    	
    	array.setFillColor(0, 4, Color.ORANGE, null, null);
    	array.showIndices(true, null, null);
    	array.put(2, "blabla", null, null);
    	array.swap(1, 4, null, null);
    	array.put(0, "Alles ok", null, null);
    	
    	lang.nextStep();
    	
    	lang.hideAllPrimitives();
	}

	private void testIntArray() {
    	int[] arrayOrg = {0,1,2,3,4,5,6,7,8};
    	ArrayProperties ap = new ArrayProperties();
    	ap.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.YELLOW);
    	ap.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
    	ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    	ap.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
//    	ap.set(AnimationPropertiesKeys.DIRECTION_PROPERTY, true);
    	IntArray array = lang.newIntArray(new Coordinates(100, 100), arrayOrg, "TestArray", null, ap);
    	
    	ArrayMarkerProperties amp = new ArrayMarkerProperties();
    	amp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "Hier");
    	ArrayMarker am = lang.newArrayMarker(array, 4, "ArrayMarker", null, amp);

    	array.put(4, 8888, null, null);
    	array.swap(0, 4, null, null);
    
    	lang.nextStep();
    	
    	array.showIndices(false, null, null);
    	am.moveBeforeStart(null, null);
    	array.put(5, 1235321, null, null);
    	array.highlightCell(1, null, null);
    	
    	lang.nextStep();
    	
    	array.setFillColor(0, 4, Color.ORANGE, null, null);
    	array.showIndices(true, null, null);
    	array.put(2, 205, null, null);
    	array.swap(1, 4, null, null);
    	array.put(0, 611, null, null);
	}

	private void testDoubleArray() {
    	double[] arrayOrg = {0.1,1.1,2.1,3.1,4.1,5.1,6.1,7.1,8.1};
    	ArrayProperties ap = new ArrayProperties();
    	ap.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.YELLOW);
    	ap.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
    	ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    	ap.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
//    	ap.set(AnimationPropertiesKeys.DIRECTION_PROPERTY, true);
    	DoubleArray array = lang.newDoubleArray(new Coordinates(100, 100), arrayOrg, "TestArray", null, ap);
    	
    	ArrayMarkerProperties amp = new ArrayMarkerProperties();
    	amp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "Hier");
    	ArrayMarker am = lang.newArrayMarker(array, 4, "ArrayMarker", null, amp);

    	array.put(4, 8888.8888, null, null);
    	array.swap(0, 4, null, null);
    
    	lang.nextStep();
    	
    	array.showIndices(false, null, null);
    	am.moveBeforeStart(null, null);
    	array.put(5, 1235321.88, null, null);
    	array.highlightCell(1, null, null);
    	
    	lang.nextStep();
    	
    	array.setFillColor(0, 4, Color.ORANGE, null, null);
    	array.showIndices(true, null, null);
    	array.put(2, 205.88, null, null);
    	array.swap(1, 4, null, null);
    	array.put(0, 611.888, null, null);
	}
    
    private String getDescriptionLines(){
    	return "AAABBBCCC";
    }

    public String getName() {
        return "AAABBBCCC";
    }

    public String getAlgorithmName() {
        return "AAABBBCCC";
    }

    public String getAnimationAuthor() {
        return "AAABBBCCC";
    }

    public String getDescription(){
        return "AAABBBCCC";
    }

    public String getCodeExample(){
        return "AAABBBCCC";
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
        return Generator.JAVA_OUTPUT;
    }

    private static final String SOURCE_CODE =
			"AAABBBCCC"; // 20

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
        return true;
    }

}