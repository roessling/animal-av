/*
 * KNN.java
 * Jan Rehbein, Marius Rettberg-Päplow, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc.kNN;

import generators.graph.helpers.Nodes;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.Slide;
import algoanim.animalscript.addons.bbcode.DefaultStyle;
import algoanim.animalscript.addons.bbcode.Style;
import algoanim.primitives.AdvancedTextSupport;
import algoanim.primitives.Circle;
import algoanim.primitives.DoubleArray;
import algoanim.primitives.Ellipse;
import algoanim.primitives.Polygon;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Square;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Triangle;
import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.TicksTiming;
import animal.animator.SetText;

@SuppressWarnings("unused")
public class KNN {

	// Definition Rechteck Punkte
	private Point upperLeftVisArea; // wird per setProperty durch Generator
									// gesetzt
	private Point lowerRightVisArea;
	int[] hNodes;
	int hNodesClassesCount = 0; // anzahl verschiedener hNodesClasses wird verwendet um die voting Tabelle im majorityVote mit der richtigen Anzahl (unterschiedlicher) Elemente zu initialisieren
	public static final double MAX_VALUE = 999.9; 

	public static final String[] METRICS = new String[]{"euclid", "manhattan", "maximum"};
	public static final String[] STEPS = new String[]{
		"Schritt 1/3 Bestimmen der Distanzen", 
		"Schritt 2/3 Suchen der nächsten Nachbarn",
		"Schritt 3/3 Vergleich der k-nächsten Nachbarn"};

	// circle - Label Offset
	public static final int LabelXOffset = 3;
	public static final int LabelYOffset = 7;
	public static final int circleRadius = 10;
	public static final int xCoordOffset = 5;

	private int k;
	private int mode = 0; // 0: AllOrNothing 1: Weighted
	private boolean weightedVoting = false; //set by method
	private int classCount;
	private String distanceMetric;
	private Language lang;
	private Point[] trainData;
	private Point unclassifiedPoint;
	private Polyline line;
	private int center; // center element
	private String description;

	// Properties
	private TextProperties labelTextP;
	private TextProperties textP;
	private TextProperties headLineSubTextP;
	private TextProperties headLineSubSubTextP;
	// private SourceCodeProperties headlineP;
	private SourceCodeProperties sourceP;
	private CircleProperties circleP;
	private RectProperties rectP;
	private ArrayProperties arrayP;
	private PolylineProperties lineP;
	private MatrixProperties matrixP;

	// possible distance metrics are: euclid, manhattan, maximum
	public static boolean validMetric(String metric) {
		for(String s : METRICS){
			if(metric.equals(s))
				return true;
		}
		return false;
	}

	// returns the distance according to the used metric
	private double calcDistance(Point P1, Point P2) {
		if (distanceMetric.equals("euclid"))
			return euklidDistance(P1, P2);
		else if (distanceMetric.equals("manhattan"))
			return manhattanDistance(P1, P2);
		else if (distanceMetric.equals("maximum"))
			return maximumDistance(P1, P2);
		else
			return 0;
	}

	// returns the euclidean distance
	private double euklidDistance(Point P1, Point P2) {
		return Math.sqrt(Math.pow(P1.X - P2.X, 2) + Math.pow(P1.Y - P2.Y, 2));
	}
	
	// returns the manhattan distance
	private double manhattanDistance(Point P1, Point P2) {
		return Math.abs(P1.X - P2.X) + Math.abs(P1.Y - P2.Y);
	}

	// returns the maximum / infinite distance
	private double maximumDistance(Point P1, Point P2) {
		return Math.max(Math.abs(P1.X - P2.X), Math.abs(P1.Y - P2.Y));
	}

	// returns the index of the smallest element within an double array
	private int minIndex(double[] arr) {
		int index = 0;
		double value = Double.MAX_VALUE;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] < value) {
				index = i;
				value = arr[i];
			}
		}
		return index;
	}

	// returns the vote for a node (weighted vote or 1)
	private double vote(int node){
		double voting = 0;
		if(weightedVoting){
			// Often used weight(dist)=1/dist(node)^2
			double distance=calcDistance(trainData[node], unclassifiedPoint);
			voting = round(600/Math.pow(distance, 2), 6);
			//System.out.println("node:" + String.valueOf(node) + "; distance:" + String.valueOf(distance) + "; voting:"+String.valueOf(voting));
		}
		else {
			voting = 1;
		}
		return voting;		
	}
	
	// rounds a double value to given decimal places
	private double round(double value, int afterComma){
		return ((double) Math.round(value * Math.pow(10, afterComma))) / Math.pow(10, afterComma); 
	}
	
	public KNN(Language l, Point[] data, int k, String metric) {
		this.lang = l;
		this.trainData = data;
		this.k = k;
		this.distanceMetric = metric;
		this.center = data.length;
		lang.setStepMode(true);
		
	}
	
	public void setMode(int mode){
		this.mode = mode;
	}
	
	public void setMatrixP(MatrixProperties mP){
		this.matrixP = mP;
	}
	
	public void setWeightedVoting(boolean boolWeightedVoting){
		this.weightedVoting = boolWeightedVoting;
	}

	public void classify(Point p) {
		this.description = buildDescription();
		AdvancedTextSupport headLineText = lang.newText(new Coordinates(xCoordOffset, 20), "k-Nearest-Neighbor", "Headline", null, textP); 
		headLineText.setFont(((Font) textP.get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont(Font.BOLD, 28), null, null);

		AdvancedTextSupport headLineText2 = lang.newText(new Coordinates(xCoordOffset+270, 20+2), "", "Parameters", null, textP);
		headLineText2.setFont(((Font) textP.get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont(Font.PLAIN, 14), null, null);
		
		AdvancedTextSupport headLineSubText = lang.newText(new Coordinates(xCoordOffset, 20+30), "", "StepInformation", null, headLineSubTextP);
		headLineSubText.setFont(((Font) textP.get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont(Font.PLAIN, 20), null, null);
		
		AdvancedTextSupport headLineSubSubText = lang.newText(new Coordinates(xCoordOffset, 20+30+20), "", "Commentar", null, headLineSubSubTextP);
		headLineSubSubText.setFont(((Font) textP.get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont(Font.BOLD, 16), null, null);
		
		// Überschrift
		Style style = new Style() {
			@Override
			public AnimationProperties getProperties(String arg0) {
				SourceCodeProperties sP = new SourceCodeProperties();
				sP.set(AnimationPropertiesKeys.FONT_PROPERTY, (((Font) textP.get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont(Font.PLAIN, 20)));
				return sP;
			}
		};
		String file = "";
		if(mode == 0)
			file = "generators/misc/kNN/introAoN.txt";
		else if(mode == 1)
			file = "generators/misc/kNN/introMajority.txt";
		System.out.println("File is " + file);
		Slide intro = new Slide(lang, file, "" , style, new Object());
		intro.hide();
		Slide funct = new Slide(lang, "generators/misc/kNN/functions.txt", "", style, new Object());
		funct.hide();
		this.unclassifiedPoint = p;

		// Schritt 1
		
		//Parameter Information einblenden
		headLineText2.setText(this.description, null, null);
		
		// Source Code einblenden
		SourceCode sc = lang.newSourceCode(new Coordinates(xCoordOffset +2 , 100), "sourceCode", null, sourceP);
		sc.addCodeLine("public String kNN-Classification(Point[] data, Point p, int k){", null, 0, null); // 0
		sc.addCodeLine("int sizeOfData = data.length;", null, 1, null); // 1
		sc.addCodeLine("double[] dist = new double[sizeOfData];", null, 1, null); // 2
		sc.addCodeLine("for (int i = 0; i < sizeOfData; i++){", null, 1, null); // 3
		sc.addCodeLine("dist[i] = dist(p,data[i]);", null, 2, null); // 4
		sc.addCodeLine("}", null, 1, null); // 5
		sc.addCodeLine("Point[] kNN = new Point[k];", null, 1, null); // 6
		sc.addCodeLine("for (int i = 0; i < k; k++){", null, 1, null); // 7
		sc.addCodeLine("int index = minIndex(dist);", null, 2, null); // 8
		sc.addCodeLine("kNN[k] = data[index];", null, 2, null); // 9
		sc.addCodeLine("dist[index] = MAX_VALUE;", null, 2, null); // 10
		sc.addCodeLine("}", null, 1, null); // 11
		if(mode == 0){
			sc.addCodeLine("String type = kNN[0].type;", null, 1, null); // 12
			sc.addCodeLine("for (int i = 1; i < k; i++){", null, 1, null); // 13
			sc.addCodeLine("if (kNN[i].type != type)", null, 2, null); // 14
			sc.addCodeLine("return \\\"\\\";", null, 3, null); // 15
			sc.addCodeLine("}", null, 1, null); // 16
			sc.addCodeLine("return type;", null, 1, null); // 17
			sc.addCodeLine("}", null, 0, null); // 18
		}
		else if(mode == 1){
			sc.addCodeLine("HashMap<String,double> typeVotes = newHashMap<String,double>();", null, 1, null); // 12
			sc.addCodeLine("String maxCountLabel;", null, 1, null); // 13 
			sc.addCodeLine("double maxVotes = 0;", null, 1, null); // 14
			sc.addCodeLine("for (int i = 1; i < k; i++){", null, 1, null); // 15
			sc.addCodeLine("String type = kNN[i].type;", null, 2, null); // 16
			sc.addCodeLine("double voting = vote(i);", null, 2, null); // 17
			sc.addCodeLine("if (typeVotes.containsKey(type))", null, 2, null); // 18
			sc.addCodeLine("voting = typeVotes.get(type)+voting;", null, 3, null); // 19
			sc.addCodeLine("typeVotes.put(type, voting);", null, 2, null); // 20
			sc.addCodeLine("if (voting > maxVotes){", null, 2, null); // 21
			sc.addCodeLine("maxVotes = voting;", null, 3, null); // 22
			sc.addCodeLine("maxVotesLabel = type;", null, 3, null); // 23 
			sc.addCodeLine("}", null, 2, null); // 24 
			sc.addCodeLine("}", null, 1, null); // 25
			sc.addCodeLine("return maxVotesLabel;", null, 1, null); // 26 
			sc.addCodeLine("}", null, 0, null); // 27
		}
		

		lang.nextStep();
		// Schritt 2 - Rechteck (Zeichungsebene) und Graph
		Rect rectGraph = lang.newRect(new Coordinates(this.upperLeftVisArea.X, this.upperLeftVisArea.Y), new Coordinates(this.lowerRightVisArea.X, this.lowerRightVisArea.Y), "Box", null, rectP);
		algoanim.primitives.Primitive Drawing[] = drawPrimitives();
		algoanim.primitives.Primitive Labels[] = drawLabels();
		// setze Beschriftung Schritt 1

		lang.nextStep();
		// Schritt 3 - Lege Distance Array an
		sc.highlight(2);
		// - zeichne distance Array
		double[] dist = new double[trainData.length];
		int distanceFieldBaseY = 500;
		if (mode == 1)
			distanceFieldBaseY = 600;
		Text distText = lang.newText(new Coordinates(xCoordOffset, distanceFieldBaseY), "Distance", "Text", null, labelTextP);
		DoubleArray dblADist = lang.newDoubleArray(new Coordinates(xCoordOffset, distanceFieldBaseY + 20), dist, "Dist", null, arrayP);

		
		for (int i = 0; i < trainData.length; i++) {
			sc.highlight(3);
			if(i==0){
				headLineSubText.setText(KNN.STEPS[0], null, null);
				sc.unhighlight(2);
				lang.nextStep(KNN.STEPS[0]);
			}
			else				
				lang.nextStep();
			// Schritt 6,9,12,15,18,21,24
			// markiere node
			Drawing[i].changeColor("fillColor", Color.RED, null, null);

			lang.nextStep();
			// Schritt 7,10,13,16,19,22,25
			sc.unhighlight(3);
			sc.highlight(4);
			dblADist.highlightCell(i, null, null);
			dist[i] = calcDistance(trainData[i], unclassifiedPoint);
			dblADist.put(i, round(dist[i], 1), null, null);

			lang.nextStep();
			// Schritt 8,11,14,17,20,23,26
			Drawing[i].changeColor("fillColor", Color.WHITE, null, null);
			dblADist.unhighlightCell(i, null, null);
			sc.unhighlight(4);
		}


		lang.nextStep(STEPS[1]);
		// Schritt 28
		sc.highlight(7);
		headLineSubText.setText(KNN.STEPS[1], null, null);
		headLineSubSubText.setText("", null, null);
		Point[] kNN = new Point[k];
		hNodes = new int[k];
		int radius = 0;
		for (int i = 0; i < k; i++) {

			lang.nextStep();
			// Schritt 29,33,37
			sc.unhighlight(7);
			sc.highlight(8);
			int index = minIndex(dist);
			dblADist.highlightCell(index, null, null);

			lang.nextStep();
			// Schritt 30,34,38
			sc.unhighlight(8);
			sc.highlight(9);
			hNodes[i] = index;
			Drawing[index].changeColor("fillColor", Color.RED, null, null);
			kNN[i] = trainData[index];

			lang.nextStep();
			// Schritt 31,35,39
			sc.unhighlight(9);
			sc.highlight(10);
			if (i == k - 1)
				// Zwecks Zeichnen des Kreises
				radius = (int) dist[index] + circleRadius;
			dblADist.put(index, MAX_VALUE, null, null);
			drawLineToCenter(index); //zeichnet Linie abhängig von gewählter Metrik
			

			lang.nextStep();
			// Schritt 32,36,40
			sc.unhighlight(10);
			sc.highlight(7);
			dblADist.unhighlightCell(index, null, null);
		}

		lang.nextStep();
		// Schritt 41
		sc.unhighlight(7);
		// Graph Unhighlighten
		for (int i = 0; i < k; i++) {
			Drawing[hNodes[i]]
					.changeColor("fillColor", Color.WHITE, null, null);
		}
		if(distanceMetric.equals("euclid")){
			Circle circ = lang.newCircle(new Coordinates(unclassifiedPoint.X,
					unclassifiedPoint.Y), radius, "Circ", null, circleP);
		}
		lang.nextStep();
		// Schritt 42
		//CLassify
		AdvancedTextSupport LabelCenter = (AdvancedTextSupport) Labels[center];
		headLineSubText.setText(KNN.STEPS[2], null, null);
		headLineSubSubText.setText("", null, null);
		String type = ""; // Result of classify
		StringMatrix voteCounter = null;
		hNodesClassesCount = counthNodesPointClasses();
		lang.nextStep(STEPS[2]);
		String ergebnisText = "";
		// AllOrNothing classify
		if (mode == 0){
			sc.highlight(12);
			Drawing[hNodes[0]].changeColor("fillColor", Color.LIGHT_GRAY, null, null);
			type = kNN[0].type;
			headLineSubSubText.setText("Typ: " + type, null, null);
			for (int i = 1; i < k; i++) {
				// Schritt 43
				lang.nextStep();
				sc.unhighlight(12);
				sc.highlight(13);
	
				// Schritt 44
				lang.nextStep();
				sc.unhighlight(13);
				
				if (!type.equals(kNN[i].type)) {
					sc.highlight(15);
					Drawing[hNodes[i]].changeColor("fillColor", Color.RED, null, null);
					unclassifiedPoint.type = "X";
					Drawing[center].changeColor("fillColor", Color.RED, null, null);
					LabelCenter.setText("", null, null);
					ergebnisText = "Punkt Klassifikation nicht möglich!";
					headLineSubSubText.setText(ergebnisText, null, null);
					break;
				} else {
					// highlight Node
					Drawing[hNodes[i]].changeColor("fillColor", Color.LIGHT_GRAY, null, null);
				}
			}
		} // mode == 0
		// Majority Classifiy
		else if (mode == 1){
			//String[][] votes = new String[2][(k>this.classCount)?classCount:k]; // anzahl elemente entweder k oder classcount
			String[][] votes = new String[2][hNodesClassesCount]; // anzahl elemente entweder k oder classcount
			
			//String[][] votes = new String[2][classCount]; // anzahl elemente entweder k oder classcount
			//char startChar = 'A';  //warum
			for (int i = 0; i < hNodesClassesCount; i++){
				votes[0][i] = ""; 
				votes[1][i] = "";
			}
			int fillCounter = 0;
			voteCounter = lang.newStringMatrix(new Coordinates(500, 550), votes , "Vote", null, matrixP);
			sc.highlight(12);
			sc.highlight(13);
			sc.highlight(14);
			lang.nextStep();
			
			sc.unhighlight(12);
			sc.unhighlight(13);
			sc.unhighlight(14);
			double maxNumber = 0;
			for (int i = 0; i < k; i++){
				sc.highlight(15);
				Drawing[hNodes[i]].changeColor("fillColor", Color.LIGHT_GRAY, null, null);
				headLineSubSubText.setText("", null, null);

				lang.nextStep();
				sc.unhighlight(15);
				sc.highlight(16);
				int ret = 0;
				String votedType = kNN[i].type;
				// Typinfo in subsubtext
				headLineSubSubText.setText("Typ: " + votedType, null, null);

				lang.nextStep();
				sc.unhighlight(16);
				sc.highlight(17);
				double voting = vote(hNodes[i]); //17
				//  voting INFO einblenden (weighted)
				headLineSubSubText.setText("Typ: " + votedType + ", " + votingTextInfo(voting), null, null);
				
				lang.nextStep();
				sc.unhighlight(17);

				for (int j = 0; j < fillCounter; j++){
					if (voteCounter.getElement(0, j).equals(votedType)){
						voteCounter.highlightCell(0, j, null, null);
						sc.highlight(19);
						double oldValue = Double.parseDouble(voteCounter.getElement(1, j));
						double newValue = oldValue + voting;
						lang.nextStep();
						
						sc.unhighlight(19);
						voteCounter.highlightCell(1, j, null, null);
						sc.highlight(20);
						voteCounter.put(1, j, String.valueOf(newValue) , null, null);
						lang.nextStep();
						sc.unhighlight(20);
						sc.highlight(21);
						headLineSubSubText.setText("Prüfe größten Stimmenanteil...", null, null);
						if (newValue > maxNumber){
							
							lang.nextStep();
							sc.unhighlight(21);
							sc.highlight(22);
							sc.highlight(23);
							maxNumber=newValue;
							type = votedType;
							headLineSubSubText.setText("Den größten Stimmenanteil mit: " + maxNumber + " hat Typ: " + String.valueOf(votedType), null, null);
							//voteCounter.unhighlightElemColumnRange(0, 0, fillCounter-1, null, null);
							//voteCounter.highlightElem(0, j, null, null);
							lang.nextStep();
							
							sc.unhighlight(22);
							sc.unhighlight(23);
						}
						voteCounter.unhighlightCell(0, j, null, null);
						voteCounter.unhighlightCell(1, j, null, null);
						ret = 1;
						break;
					}		
				}
				if (ret == 0){
					sc.highlight(20);
					
					voteCounter.put(0, fillCounter, votedType, null, null);
					voteCounter.put(1, fillCounter, String.valueOf(voting), null, null);
					voteCounter.highlightCell(0, fillCounter, null, null);
					voteCounter.highlightCell(1, fillCounter, null, null);
					lang.nextStep();
					
					sc.unhighlight(20);
					if (maxNumber == 0){
						//sc.highlight(22);
						//sc.highlight(23);
						//voteCounter.unhighlightElemColumnRange(0, 0, fillCounter, null, null);
						//voteCounter.highlightElem(0, fillCounter, null, null);
						maxNumber = voting;
						type = votedType;
						//lang.nextStep();
						
						//sc.unhighlight(22);
						//sc.unhighlight(23);
					}
					voteCounter.unhighlightCell(0, fillCounter, null, null);
					voteCounter.unhighlightCell(1, fillCounter, null, null);
					fillCounter++;
				}
			}
			// voteCounter.hide();
		} // mode==1
		
		// headLineSubSubText.setText("", null, null);
		if (unclassifiedPoint.type != "X") {
			unclassifiedPoint.type = type;
			if (mode == 0)
				sc.highlight(17);
			else if (mode == 1)
				sc.highlight(26);
			Drawing[center].changeColor("fillColor", Color.LIGHT_GRAY, null, null);
			LabelCenter.setText(type, null, null);
			ergebnisText = "Punkt Klassifikation erfolgreich! Typ: "+ unclassifiedPoint.type.toString();
			headLineSubSubText.setText(ergebnisText, null, null);
		}
		lang.nextStep("Klassifikation");
		sc.unhighlight(15);
		// Ergebnis nochmal genauer
		// Position mittig (centernode)
		sc.hide();
		// moveBy funktioniert nicht wie erwartet deshalb neues Textfeld
		//headLineSubSubText.moveBy("smooth", 100, 500, null,null);
		headLineSubSubText.setText("", null, null);
		AdvancedTextSupport ergebnisTextfeld1 = lang.newText(new Coordinates(xCoordOffset, unclassifiedPoint.Y - 24), "Ergebnis:", "info", null, textP);
		ergebnisTextfeld1.setFont(((Font) textP.get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont(Font.BOLD, 16), null, null);
		AdvancedTextSupport ergebnisTextfeld2 = lang.newText(new Coordinates(xCoordOffset, unclassifiedPoint.Y), ergebnisText, "info", null, textP);
		ergebnisTextfeld2.setFont(((Font) textP.get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont(Font.BOLD, 16), null, null);
	}

	// generiert votingTextInfo
	private String votingTextInfo(double voting) {
		String temp = "";
		if (weightedVoting) {
			temp = "gewichtete Stimme: " + String.valueOf(voting);
		}
		else {
			temp = "volle Stimme: " + String.valueOf(voting);
		}
		return temp;
	}

	// generiert BeschreibungsInfo
	private String buildDescription() {
		// text: euclidean distance, k=3, all or nothing vote
		// text: euclidean distance, k=3, majority vote, weighted votings
		String tempDescription = "Parameters: ";
		
		if (distanceMetric.equals("euclid"))
			tempDescription = tempDescription.concat("euclidean distance");
		else if (distanceMetric.equals("manhattan"))
			tempDescription = tempDescription.concat("manhattan distance");
		else if (distanceMetric.equals("maximum"))
			tempDescription = tempDescription.concat("maximum distance");

		tempDescription = tempDescription.concat(", k="+ String.valueOf(k));

		if(mode==0)
			tempDescription = tempDescription.concat(", all or nothing vote");
		else if(mode==1)
			tempDescription = tempDescription.concat(", majority vote");
		
		if(weightedVoting)
			tempDescription = tempDescription.concat(", weighted votings");
		
		return tempDescription;
	}

	// zeichnet Nodes in Zeichnungsebene
	private algoanim.primitives.Primitive[] drawPrimitives() {
		// algoanim.primitives. // Circle // Ellipse // Polygon // Square //
		// Triangle // Group
		algoanim.primitives.Primitive[] tempPrimitive = new algoanim.primitives.Circle[trainData.length + 1];
		CircleProperties circlesProperty = new CircleProperties();
		circlesProperty.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		circlesProperty.set(AnimationPropertiesKeys.FILL_PROPERTY,
				Color.LIGHT_GRAY);

		CircleProperties circlePropertyCenter = new CircleProperties();
		circlePropertyCenter.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		circlePropertyCenter.set(AnimationPropertiesKeys.FILL_PROPERTY,
				Color.WHITE);

		for (int i = 0; i < trainData.length; i++) {
			// zeichne Kreise
			tempPrimitive[i] = lang.newCircle(new Coordinates(trainData[i].X,
					trainData[i].Y), circleRadius, "node" + i, null,
					circlesProperty);
		}
		// center
		tempPrimitive[center] = lang.newCircle(new Coordinates(
				unclassifiedPoint.X, unclassifiedPoint.Y), 10, "center", null,
				circlePropertyCenter);

		return tempPrimitive;
	}

	// zeichnet Labels in Nodes
	private algoanim.primitives.Primitive[] drawLabels() {
		algoanim.primitives.Primitive[] tempLabels = new algoanim.primitives.Text[trainData.length + 1];

		for (int i = 0; i < trainData.length; i++) {
			// zeichne Labels
			tempLabels[i] = drawLabel(trainData[i].X, trainData[i].Y,
					trainData[i].type, "label" + i);
		}
		tempLabels[center] = drawLabel(unclassifiedPoint.X,
				unclassifiedPoint.Y, "?", "Center");

		return tempLabels;
	}

	// zeichnet ein Label an Koordinate
	private algoanim.primitives.Primitive drawLabel(int xCoordinate,
			int yCoordinate, String label, String name) {
		algoanim.primitives.Primitive temp = lang.newText(new Coordinates(
				xCoordinate - LabelXOffset, yCoordinate - LabelYOffset), label,
				name, null);
		return temp;
	}

	// ruft die entsprechende anhand der gewählten distanz Bestimmungsmethode die entsprechende Linien Zeichnungs Methode auf
	private Polyline drawLineToCenter(int ObjectNo) {
		Polyline temp=null;
		if(distanceMetric.equals("euclid"))
			temp = drawDirectLineToCenter(ObjectNo);
		else if(distanceMetric.equals("manhattan"))
			temp = drawManhattanToCenter(ObjectNo);
		else if(distanceMetric.equals("maximum"))
			temp = drawMaxToCenter(ObjectNo);
		return temp;
	}
	
	// zeichnet euklidische Entfernung (direkte Linie)
	private Polyline drawDirectLineToCenter(int ObjectNo){
		Node[] nodes = new Node[2];
		nodes[0] = new Coordinates(trainData[ObjectNo].X, trainData[ObjectNo].Y);
		nodes[1] = new Coordinates(unclassifiedPoint.X, unclassifiedPoint.Y);
		Polyline temp = lang.newPolyline(nodes, "line", null, lineP);
		return temp;
	}
	
	// zeichnet manhatten Entfernung (nur horizontale und vertikale Linien)
	private Polyline drawManhattanToCenter(int ObjectNo){
		Node[] nodes = new Node[3];
		nodes[0] = new Coordinates(trainData[ObjectNo].X, trainData[ObjectNo].Y);
		nodes[1] = new Coordinates(unclassifiedPoint.X, trainData[ObjectNo].Y);
		nodes[2] = new Coordinates(unclassifiedPoint.X, unclassifiedPoint.Y);
		return lang.newPolyline(nodes, "line", null, lineP);	
	}
	
	// zeichnet max/infinite Entfernung (max von hor. oder vert. Linie)
	private Polyline drawMaxToCenter(int ObjectNo){
		Node[] nodes = new Node[2];
		nodes[0] = new Coordinates(trainData[ObjectNo].X, trainData[ObjectNo].Y);
		if(Math.abs(trainData[ObjectNo].X - unclassifiedPoint.X) > Math.abs(trainData[ObjectNo].Y - unclassifiedPoint.Y))
			nodes[1] = new Coordinates(unclassifiedPoint.X, trainData[ObjectNo].Y);
		else
			nodes[1] = new Coordinates(trainData[ObjectNo].X, unclassifiedPoint.Y);
		return lang.newPolyline(nodes, "line", null, lineP);
	}

	// setzt übergebene Properties
	public void setProperties(TextProperties textP,
			SourceCodeProperties sourceP, CircleProperties circleP,
			RectProperties rectP, ArrayProperties arrayP,
			PolylineProperties lineP, TextProperties headLine0P,
			TextProperties headLine1P, TextProperties headLine2P) {
		this.labelTextP = textP;
		this.sourceP = sourceP;
		this.circleP = circleP;
		this.rectP = rectP;
		this.arrayP = arrayP;
		this.lineP = lineP;
		this.textP = headLine0P;
		this.headLineSubTextP = headLine1P;
		this.headLineSubSubTextP = headLine2P;
	}

	// setzt übergebene Primitive
	public void setPrimitives(Point upperLeftPointVisArea,
			Point lowerRightPointVisArea, int trainingPointClassesCount) {
		this.upperLeftVisArea = upperLeftPointVisArea;
		this.lowerRightVisArea = lowerRightPointVisArea;
		this.classCount = trainingPointClassesCount;
	}
	
	// returns count of differentPointClasses for hNodes (k-nearest neighbours)
	// used for optimal StringMatrix initialization
	private int counthNodesPointClasses() {
		HashSet<String> tempNoDuplicateLabels = new HashSet<String>();
		for(int i=0; i<hNodes.length; i++)
			tempNoDuplicateLabels.add(trainData[hNodes[i]].type);
		//System.out.println("points:" + hNodes.length + "; classes:" + tempNoDuplicateLabels.size());
		return tempNoDuplicateLabels.size();
	}

	

}
