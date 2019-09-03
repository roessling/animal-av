package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
public class Id3 implements Generator{

	protected Language lang;
	private SourceCode sc;
	private SourceCodeProperties scProps;
	private MatrixProperties matrixProperties;
	private MatrixProperties gainMatrixProperties;
	private PolylineProperties polyProps;
	private RectProperties TreeNodeBoxProps;
	private TextProperties TreeNodeTextProps;
	private Text statusText;
	private Text statusText2;

	private ArrayList<ArrayList<TreeNode>> displayNodes;
	
	//used for the algorithm
	/**
	 * Contains the data set
	 */
	private StringMatrix dataSet;
	private StringMatrix gainMatrix;
	private String[][] data;
	/**
	 * Contains all attributes used in the data set and maps the attribute to the column 
	 */
	private HashMap<String, Integer> attributes = new HashMap<>();
	/**
	 * Maps all attributes to it's possible values
	 */
	private HashMap<String, HashSet<String>> attributeValues = new HashMap<>();
	
	public void init(){
		lang = new AnimalScript("ID3 Animation", "Joel Koschier", 640, 480);
		lang.setStepMode(true);
		
		
	}
	
	private void createTitle(){
		TextProperties textProp = new TextProperties();
		textProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Sansserif", Font.BOLD, 24));
		Text titleText = lang.newText(new Coordinates(20, 30), "ID3", "TitleText", null, textProp);
		lang.newRect(new Offset(-5, -5, titleText, AnimalScript.DIRECTION_NW),new Offset(5,5, titleText, AnimalScript.DIRECTION_SE), "", null);
	}
	
	private void createSourceCode(){
		sc = lang.newSourceCode(new Coordinates(40, 50), "sourceCode", null, scProps);
		
		sc.addCodeLine("Hauptalgorithmus:", null, 0, null);
		sc.addCodeLine("1. Wenn alle Eintraege zur selben Klasse gehoeren", null, 0, null);
		sc.addCodeLine("1. Erzeuge ein Blatt mit dieser Klasse und gebe es zurueck", null, 1, null);
		sc.addCodeLine("2. Ansonsten ", null, 0, null);
		sc.addCodeLine("1. Bestimme das Attribut mit dem hoechsten Gain aller ungenutzten Attribute", null, 1, null);
		sc.addCodeLine("2. Erzeuge einen Knoten mit diesem Attribut", null, 1, null);
		sc.addCodeLine("3. Fuer jeden Wert den das Attribut annehmen kann:", null, 1, null);
		sc.addCodeLine("1. Reduziere das Datenset, sodass es nur diesen Wert enthaelt", null, 2, null);
		sc.addCodeLine("2. Rufe den Algorithmus rekursiv auf mit dem reduzierten Datensatz", null, 2, null);
		sc.addCodeLine("3. Setze den resultierenden Baum als Kind unseres neuen Knotens", null, 2, null);
/*
		sc.addCodeLine("", null, 0, null);
		sc.addCodeLine("Gain eines einzelnes Attribut:", null, 0, null);
		sc.addCodeLine("1. Berechne die Entropie E_D des Datensets D", null, 0, null);
		sc.addCodeLine("2. Setze eine Variable sum gleich 0", null, 0, null);
		sc.addCodeLine("3. Fuer jeden Wert den das Attribut annehmen kann", null, 0, null);
		sc.addCodeLine("1. Reduziere das Datenset dass es nur diesen Wert enthaelt", null, 1, null);
		sc.addCodeLine("2. Berechne die Entropie E_Dv des kleineren Datensets Dv", null, 1, null);
		sc.addCodeLine("3. Multipliziere E_Dv mit size(Dv)/size(D)", null, 1, null);
		sc.addCodeLine("4. Addiere E_Dv zu sum", null, 1, null);
		sc.addCodeLine("4. Der Gain ist nun E_D - sum ", null, 0, null);
*/
	}
	
	
	private void generateTree(){
		displayNodes = new ArrayList<ArrayList<TreeNode>>();
		HashSet<Integer> indexes = new HashSet<Integer>();
		
		for(int i = 0; i < dataSet.getNrCols() - 1; i++){
			attributes.put(dataSet.getElement(0, i), i);
			
			HashSet<String> set = new HashSet<String>();
			for(int j = 1 ; j < dataSet.getNrRows(); j++){
				set.add(dataSet.getElement(j, i));
			}
			attributeValues.put(dataSet.getElement(0, i), set);
		}
		
		for(int i = 1; i < dataSet.getNrRows(); i++){
			indexes.add(i);
		}
		lang.nextStep();
		generateTreeRec(indexes, attributes, 0, new ArrayList<String>());

		lang.nextStep();
		
	}
	
	private TreeNode generateTreeRec(HashSet<Integer> indexes, HashMap<String, Integer> currentAttributes, int depth, 
			ArrayList<String> checks){
		
		assert(!indexes.isEmpty());
		updateStatusText(checks);
		//check if all elements belong to the same class
		Iterator<Integer> iter = indexes.iterator();
		String firstClass = dataSet.getElement(iter.next(), dataSet.getNrCols() - 1);
		boolean oneClass = true;
		while(iter.hasNext()){
			if(firstClass.compareTo(dataSet.getElement(iter.next(), dataSet.getNrCols() - 1)) != 0){
				oneClass = false;
				break;
			}
		}
		sc.highlight(1);
		lang.nextStep();
		if(oneClass){
			/*
			lang.newText(
					new Coordinates(100 , 100 + generatedTrees * 30), 
					firstClass , "class" + generatedTrees, null );
			lang.newRect(
					new Offset(-1, -1, "class" + generatedTrees, AnimalScript.DIRECTION_NW), 
					new Offset(1,1, "class" + generatedTrees, AnimalScript.DIRECTION_SE), 
					"rect" + generatedTrees, null
					);
			generatedTrees++;
			*/
			sc.unhighlight(1);
			sc.highlight(2);
			TreeNode returnNode = drawNewNode(depth, firstClass);
			lang.nextStep();
			sc.unhighlight(2);
			return returnNode;
		}
		
		sc.unhighlight(1);
		sc.highlight(3);
		lang.nextStep();
		
		sc.unhighlight(3);
		sc.highlight(4);
		String highestEntropy = getAttributeWithHighestEntropy(currentAttributes, indexes);
		lang.nextStep();
		
		sc.unhighlight(4);
		sc.highlight(5);
		//erzeuge neuen Knoten
		TreeNode thisNode = drawNewNode(depth, highestEntropy);
		lang.nextStep();
		
		HashSet<String> values = attributeValues.get(highestEntropy);
		HashMap<String, Integer> newAttributes = new HashMap<String,Integer>();
		
		//ermittele alle nicht verwendeten Attribute
		for(Entry<String, Integer> entry : currentAttributes.entrySet()){
			if(!entry.getKey().equals(highestEntropy))
				newAttributes.put(entry.getKey(), entry.getValue());
		}
		
		sc.unhighlight(5);
		sc.highlight(6);
		lang.nextStep();
		//iteriere ueber die Werte welche das ausgewaehle Attribut annehmen kann
		int relevantCol = attributes.get(highestEntropy);
		for(String value : values){
			//waehle alle Eintraege aus mit AttributValue = value
			checks.add(highestEntropy + " = " + value);
			updateStatusText(checks);
			sc.unhighlight(6);
			sc.unhighlight(9);
			sc.highlight(7);
			HashSet<Integer> newIndexes = new HashSet<Integer>();
			for(int index : indexes){
				if(dataSet.getElement(index, relevantCol).equals(value))
					newIndexes.add(index);
			}
			for(int i = 1; i < data.length; i++)
				for(int j = 0; j < data[0].length; j++)
					dataSet.put(i, j, "", null, null);
			for(int i :newIndexes){
				for(int j = 0; j < data[0].length; j++){
					dataSet.put(i,j, data[i][j],null,null);
				}
			}
			lang.nextStep();
			
			sc.unhighlight(7);
			sc.highlight(8);
			lang.nextStep();
			sc.unhighlight(8);
			TreeNode child = generateTreeRec(newIndexes, newAttributes, depth+1, checks);
			lang.nextStep();
			for(int i = 1; i < data.length; i++)
				for(int j = 0; j < data[0].length; j++)
					dataSet.put(i, j, "", null, null);
			for(int i : indexes){
				for(int j = 0; j < data[0].length; j++){
					dataSet.put(i,j, data[i][j],null,null);
				}
			}checks.remove(checks.size() - 1);
			sc.highlight(8);
			lang.nextStep();
			
			sc.unhighlight(8);
			sc.highlight(9);
			child.incomingCondition = value;
			//generiere Verbindung im Baum
			thisNode.childNodes.add(child);
			
			Node[] vertices = new Node[2];
			vertices[0] = new Offset(0,0,thisNode.surroundingBox,AnimalScript.DIRECTION_S);
			vertices[1] = new Offset(0,0,child.surroundingBox, AnimalScript.DIRECTION_N);
			Polyline connection = lang.newPolyline(vertices, "", null, polyProps);
			thisNode.conditionTexts.add(
					lang.newText(
							new Offset(0, -20, child.surroundingBox, AnimalScript.DIRECTION_NW),
							child.incomingCondition , "", null));
			thisNode.connections.add(connection);
			lang.nextStep();
		}
		sc.unhighlight(9);
		return thisNode;
	}
	
	private void updateStatusText(ArrayList<String> checks){
		String constr = "";
		if(checks.size() > 0){
			constr += checks.get(0);
			for(int i = 1; i < checks.size(); i++){
				constr += " & " + checks.get(i);
			}
		}
		statusText.setText("Derzeitige Bedingungen:", null, null);
		statusText2.setText(constr, null, null);
	}

	private TreeNode drawNewNode(int depth, String InnerText) {
		
		if(displayNodes.size() <= depth){
			displayNodes.add(new ArrayList<TreeNode>());
		}
		
		ArrayList<TreeNode> currentRow = displayNodes.get(depth);
		TreeNode returnNode = new TreeNode();
		returnNode.innerText = InnerText;
		
		if(depth == 0){
			returnNode.langInnerText = lang.newText(new Coordinates(100, 300), InnerText, "", null);
		}else{
			ArrayList<TreeNode> prevRow = displayNodes.get(depth-1);
			if(currentRow.isEmpty()){
				TreeNode upperNode = prevRow.get(0);
				returnNode.langInnerText = lang.newText(
						new Offset(0,100, upperNode.langInnerText, AnimalScript.DIRECTION_SW), 
						InnerText, "", null, TreeNodeTextProps);
			}else{
				TreeNode nodeLeft = currentRow.get(currentRow.size() - 1);
				returnNode.langInnerText = lang.newText(
						new Offset(30, 0, nodeLeft.langInnerText, AnimalScript.DIRECTION_NE), 
						InnerText, "", null, TreeNodeTextProps);
			}
		}
		returnNode.surroundingBox = lang.newRect(
				new Offset(-5, -5, returnNode.langInnerText, AnimalScript.DIRECTION_NW), 
				new Offset(5,5, returnNode.langInnerText, AnimalScript.DIRECTION_SE), 
				"", null, TreeNodeBoxProps);
		currentRow.add(returnNode);
		
		return returnNode;
	}
	
	private String getAttributeWithHighestEntropy(HashMap<String, Integer> attributes, HashSet<Integer> indexes){
		String bestAttr = "";
		double bestGain = -1;
		String[][] data = new String[attributes.keySet().size() + 1][2];
		data[0][0] = "Attribute";
		data[0][1] = "Gain";
		int i = 1;
		for (String attribute : attributes.keySet()) {
			double currGain = gainForAttribute(attribute, indexes);
			data[i][0] = attribute;
			data[i][1] = Double.toString(currGain);
			if(currGain > bestGain){
				bestAttr = attribute;
				bestGain = currGain;
			}
			i++;
		}
		
		for(int j = 0; j < data.length; j++){
			gainMatrix.put(j, 0, data[j][0], null, null);
			gainMatrix.put(j, 1, data[j][1], null, null);
		}
		for(int j = data.length; j < gainMatrix.getNrRows(); j++){
			gainMatrix.put(j, 0, "", null, null);
			gainMatrix.put(j, 1, "", null, null);
		}
			
		
		gainMatrix.show();
		Text gainText = lang.newText(new Offset(0, 20, gainMatrix, AnimalScript.DIRECTION_SW), "bestes Attribut: "+bestAttr, "", null);
		lang.nextStep();
		gainMatrix.hide();
		gainText.hide();
		return bestAttr;
	}
	
	private double gainForAttribute(String attribute, HashSet<Integer> indexes){
		double entropyAll = entropy(indexes);
		//first lets get the distributions
		int S = indexes.size();
		int attrIndex = attributes.get(attribute);
		Iterator<String> valueIter = attributeValues.get(attribute).iterator();
		double sum = 0;
		while(valueIter.hasNext()){
			String value = valueIter.next();
			Iterator<Integer> indexIter = indexes.iterator();
			HashSet<Integer> attrIndexes = new HashSet<>();
			int Sattr = 0;
			while(indexIter.hasNext()){
				int curIndex = indexIter.next();
				if(value.equals(dataSet.getElement(curIndex, attrIndex))){
					Sattr++;
					attrIndexes.add(curIndex);
				}
			}
			sum += entropy(attrIndexes) * Sattr / S ;
			
		}
		return entropyAll - sum;
		
	}
	
	private double entropy(HashSet<Integer> indexes){
		HashMap<String, Double> entropy = new HashMap<String,Double>();
		
		//first lets count the elements
		int count = 0;
		Iterator<Integer> iter = indexes.iterator();
		while(iter.hasNext()){
			count++;
			String currentClass = dataSet.getElement(iter.next(), dataSet.getNrCols() - 1);
			if(entropy.containsKey(currentClass)){
				entropy.put(currentClass, entropy.get(currentClass) + 1.0);
			}else{
				entropy.put(currentClass, 1.0);
			}
		}
		
		double sum = 0.0;
		for(String c : entropy.keySet()){
			double percent = entropy.get(c) / count;
			sum += percent * (Math.log(percent)/Math.log(2));
		}
		
		return -sum;
	}
	
	public static String[][] getExampleDataset(){
		String[][] data = new String[13][5];
		data[0][0] = "Age";
		data[1][0] = "old";
		data[2][0] = "young";
		data[3][0] = "old";
		data[4][0] = "young";
		data[5][0] = "young";
		data[6][0] = "old";
		data[7][0] = "old";
		data[8][0] = "old";
		data[9][0] = "young";
		data[10][0] = "old";
		data[11][0] = "young";
		data[12][0] = "young";
		
		data[0][1] = "Education";
		data[1][1] = "secondary";
		data[2][1] = "college";
		data[3][1] = "secondary";
		data[4][1] = "college";
		data[5][1] = "secondary";
		data[6][1] = "secondary";
		data[7][1] = "college";
		data[8][1] = "college";
		data[9][1] = "primary";
		data[10][1] = "primary";
		data[11][1] = "secondary";
		data[12][1] = "college";
		
		data[0][2] = "Married";
		data[1][2] = "no";
		data[2][2] = "yes";
		data[3][2] = "yes";
		data[4][2] = "no";
		data[5][2] = "no";
		data[6][2] = "no";
		data[7][2] = "no";
		data[8][2] = "yes";
		data[9][2] = "yes";
		data[10][2] = "yes";
		data[11][2] = "yes";
		data[12][2] = "no";
		
		data[0][3] = "Income";
		data[1][3] = "medium";
		data[2][3] = "low";
		data[3][3] = "medium";
		data[4][3] = "high";
		data[5][3] = "high";
		data[6][3] = "high";
		data[7][3] = "high";
		data[8][3] = "low";
		data[9][3] = "medium";
		data[10][3] = "low";
		data[11][3] = "medium";
		data[12][3] = "medium";
		
		data[0][4] = "Credit?";
		data[1][4] = "yes";
		data[2][4] = "yes";
		data[3][4] = "yes";
		data[4][4] = "yes";
		data[5][4] = "yes";
		data[6][4] = "yes";
		data[7][4] = "yes";
		data[8][4] = "yes";
		data[9][4] = "no";
		data[10][4] = "no";
		data[11][4] = "no";
		data[12][4] = "no";
		return data;
	}
	
	public void writeAnimationToFile(String filename){
		BufferedWriter writer = null;
		try{
			writer = new BufferedWriter(new FileWriter(filename));
			writer.write(lang.toString());
		}catch(IOException e){}
		finally{
			try{
				if(writer != null);
				writer.close();
			}catch(IOException e) {};
		}
	}

	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		
		
		matrixProperties = (MatrixProperties) props.getPropertiesByName("Dataset");
		gainMatrixProperties = (MatrixProperties) props.getPropertiesByName("Gain Matrix");
		scProps = (SourceCodeProperties) props.getPropertiesByName("Source Code");
		TreeNodeBoxProps = (RectProperties) props.getPropertiesByName("Tree Node Box");
		polyProps = (PolylineProperties) props.getPropertiesByName("Tree Node Connection");
		TreeNodeTextProps = (TextProperties) props.getPropertiesByName("Tree Node Text");

		createTitle();
		createSourceCode();
		statusText = lang.newText(new Offset(0, 20, sc, AnimalScript.DIRECTION_SW), "", "", null);
		statusText2 = lang.newText(new Offset(0, 5, statusText, AnimalScript.DIRECTION_SW), "", "", null);
		
		
		String[][] matrix = (String[][]) primitives.get("Dataset");
		data = new String[matrix.length][matrix[0].length];
		for(int i = 0; i < matrix.length; i++)
			for(int j = 0; j < matrix[0].length; j++)
				data[i][j] = matrix[i][j];
		dataSet = lang.newStringMatrix(new Coordinates(600, 50), matrix, "DataSet", null, matrixProperties);
		
		String[][] gainFill = new String[dataSet.getNrCols()][2];
		for(int i = 0; i < gainFill.length; i++)
			for(int j = 0; j < gainFill[0].length; j++)
				gainFill[i][j] = "";
		gainMatrix = lang.newStringMatrix(new Offset(0, 100, dataSet, AnimalScript.DIRECTION_SW), gainFill, "", null, gainMatrixProperties);
		gainMatrix.hide();
		
		generateTree();
		
		return lang.toString();
	}

	@Override
	public String getAlgorithmName() {
		return "ID3";
	}

	@Override
	public String getAnimationAuthor() {
		return "Joel Benedikt Koschier";
	}

	@Override
	public String getCodeExample() {
		return "Hauptalgorithmus:\n"
				+ "1. Wenn alle Eintraege zur selben Klasse gehoeren\n"
				+ "    1. Erzeuge ein Blatt mit dieser Klasse und gebe es zurueck\n"
				+ "2. Ansonsten\n"
				+ "    1. Bestimme das Attribut mit dem hoechsten Gain aller ungenutzten Attribute\n"
				+ "    2. Erzeuge einen Knoten mit diesem Attribut\n"
				+ "    3. Fuer jeden Wert den das Attribut annehmen kann:\n"
				+ "        1. Reduziere den Datensatz, sodass es nur diesen Wert enthaelt\n"
				+ "        2. Rufe den Algorithmus rekursiv auf mit dem reduzierten Datensatz als Parameter\n"
				+ "        3. Setze den resultierenden Baum als Kind unseres neuen Knotens\n";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	@Override
	public String getDescription() {
		return "ID3 ist ein \"Supervised\" Machine Learning Algorithmus,\n"
				+ "welcher aus einem Datensatz einen Entscheidungsbaum generiert.\n"
				+ "Hierzu benoetigt der Algorithmus, neber dem Datensatz der zu lernen gilt, weiterhin\n"
				+ "eine Metrik zur bestimmung des Datengehaltes eines Datensatzes.\n"
				+ "Der ID3 Algorithmus beschreibt mit die simpelste Form, welcher ein\n"
				+ " Entscheidungsbaumgenerierer annehmen kann.\n"
				+ "Verbesserte Ansaetze nutzen zusaetzlich Vorgehensweisen wie Pre- oder Postpruning um die\n"
				+ "Genauigkeit des gelernten Baumes auf allgemeineren Datensaetzen zu erhoehen.\n"
				+ "\n"
				+ "In dieser Implementation des ID3 Algorithmuses wird die Gain Metrik verwendet.\n"
				+ "\n"
				+ "Der Generator nimmt an, das sich in der ersten Reihe des Datensatzes die Titel der Attribute befinden.\n"
				+ "Weiterhin wird angenommen, das sich in der letzten Spalte das zu lernende Attribut befindet.";
	}

	@Override
	public String getFileExtension() {
		return "asu";
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	@Override
	public String getName() {
		return "ID3";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}
	
	class TreeNode{
		public String incomingCondition = "";
		public String innerText = "";
		public ArrayList<TreeNode> childNodes;
		
		public Text langInnerText;
		public Rect surroundingBox;
		public ArrayList<Polyline> connections;
		public ArrayList<Text> conditionTexts;
		
		
		public TreeNode(){
			childNodes = new ArrayList<TreeNode>();
			connections = new ArrayList<Polyline>();
			conditionTexts = new ArrayList<Text>();
		}
	}
}
