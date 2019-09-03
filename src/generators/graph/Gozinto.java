package generators.graph;

import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.Graph;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;

public class Gozinto {
	
	private GozintoHelper helper = new GozintoHelper();
	private int[] amount;
	private int tableX = -785;
	private int tableY = 215;
	private int tableDiffX = 107;
	private int tableDiffY = 28;
	private int[] result;
	private double probability;
	
	private Variables variables;
	private ArrayList<QuestionModel> questions;
	private TwoValueCounter gozListCounter;
	private TwoValueCounter tableCounter;
	private TwoValueView viewGozList;
	private TwoValueView viewCalcTable;
	private Text res;
	private Text gList;
	private Text tabText;
	private Language lang;
	private Graph gozGraph;
	private StringMatrix gozList;
	private StringMatrix calcTable;
	private IntMatrix resVector;
	private MatrixProperties gozintoListProps;
	private MatrixProperties calcProps;
	private MatrixProperties resProps;
	private TextProperties numberProps;
	private Text header;
	private Text demandText;
	private Text resultInfo;
	private ArrayList<Text> numbers;
	private ArrayList<Offset> numberCordinates;
	private Text[] dijCopy;
	private Text[][] calculation;
	private Text vik;
	private Rect headerRect;
	private TextProperties textProps;
	private SourceCode source;
	private SourceCodeProperties sourceProps;
	private SourceCode description;
	private GraphProperties graphProps;
	
	public Gozinto(Language l) {
		lang = l;
		lang.setStepMode(true);
		textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		questions = new ArrayList<QuestionModel>();
		probability = 1;
	}
	
	public Gozinto(Language l, MatrixProperties gozintoList, MatrixProperties calculateTable, MatrixProperties result, SourceCodeProperties sourceCode, double questionProbability) {
		lang = l;
		lang.setStepMode(true);
		textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		questions = new ArrayList<QuestionModel>();
		probability = questionProbability;
		gozintoListProps = gozintoList;
		calcProps = calculateTable;
		resProps = result;
		sourceProps = sourceCode;
	}
	
	public Gozinto() {
		lang = new AnimalScript("Gozinto-Listen-Verfahren", "Timm Lampa,Najim Azizi", 640, 480);
		textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		questions = new ArrayList<QuestionModel>();
		probability = 1;
	}
	
	// Fügt die Fragen einer Liste hinzu
	private void setQuestions() {
		MultipleChoiceQuestionModel algo = new MultipleChoiceQuestionModel("algo");
		algo.setPrompt("Auf welcher Grundlage basiert das Gozinto-Listen-Verfahren?");
		algo.addAnswer("Gozinto-Liste", 0, "Das ist leider Falsch! Der Algorithmus basiert auf den Gozinto-Graphen!");
		algo.addAnswer("Gozinto-Graph", 10, "Richtig!!!");
		algo.addAnswer("Rechentabelle", 0, "Das ist leider Falsch! Der Algorithmus basiert auf den Gozinto-Graphen!");
		algo.addAnswer("Bedarfsmenge", 0, "Das ist leider Falsch! Der Algorithmus basiert auf den Gozinto-Graphen!");
		questions.add(algo);
		
		MultipleChoiceQuestionModel algoUse = new MultipleChoiceQuestionModel("Gozinto-Listen-Verfahren");
		algoUse.setPrompt("Wozu wird das Gozinto-Listen-Verfahren genutzt?");
		algoUse.addAnswer("Um die Gozinto-Liste zu erstellen", 0, "Das ist leider Falsch! Das Gozinto-Listen-Verfahren wird genutzt, um Stücklisten aufzulösen!");
		algoUse.addAnswer("Um den Gozinto-Graphen zu erstellen", 0, "Das ist leider Falsch! Das Gozinto-Listen-Verfahren wird genutzt, um Stücklisten aufzulösen!");
		algoUse.addAnswer("Um den Primärbedarf des Produktes zu errechnen", 0, "Das ist leider Falsch! Das Gozinto-Listen-Verfahren wird genutzt, um Stücklisten aufzulösen!");
		algoUse.addAnswer("Um Stücklisten aufzulösen", 10, "Richtig!!!");
		questions.add(algoUse);
		
		MultipleChoiceQuestionModel gozintoGraph = new MultipleChoiceQuestionModel("GozintoGraph");
		gozintoGraph.setPrompt("Welchen Nutzen hat der Gozinto-Graph?");
		gozintoGraph.addAnswer("Er dient dazu die Fertigungsstruktur und die quantitativen Input-Output-Beziehungen grafisch darzustellen.", 10, "Richtig!!!");
		gozintoGraph.addAnswer("Er dient lediglich als Hilfe, um die Gozinto-Liste aufzustellen.", 0, "Das ist leider Falsch! Der Gozinto-Graph dient dazu die Fertigungsstruktur und die quantitativen Input-Output-Beziehungen grafisch darzustellen!");
		gozintoGraph.addAnswer("Er wird genutzt, um die abgearbeiteten Zwischenprodukte abzuhacken.", 0, "Das ist leider Falsch! Der Gozinto-Graph dient dazu die Fertigungsstruktur und die quantitativen Input-Output-Beziehungen grafisch darzustellen!");
		gozintoGraph.addAnswer("Er dient dazu die Organisation im Unternehmen von der Produktion bis zur Auslieferung grafisch darzustellen.", 0, "Das ist leider Falsch! Der Gozinto-Graph dient dazu die Fertigungsstruktur und die quantitativen Input-Output-Beziehungen grafisch darzustellen!");
		questions.add(gozintoGraph);
		
		MultipleSelectionQuestionModel gozintoNodes = new MultipleSelectionQuestionModel("GozintoNodes");
		gozintoNodes.setPrompt("Was stellen die Knoten des Gozinto-Graphen dar?");
		gozintoNodes.addAnswer("Endprodukte", 10, "Richtig!!!");
		gozintoNodes.addAnswer("Baugruppen", 10, "Richtig!!!");
		gozintoNodes.addAnswer("Einzelteile", 10, "Richtig!!!");
		questions.add(gozintoNodes);
		
		TrueFalseQuestionModel trueQuestion = new TrueFalseQuestionModel("trueQu");
		trueQuestion.setPrompt("Die Bewertung der Pfeile im Gozinto-Graph gibt an, wie viele Einheiten eines Einzelteils bzw. einer Baugruppe direkt in eine übergeordnete Baugruppe bzw. in ein Endprodukt eingehen. Wahr oder falsch?");
		trueQuestion.setCorrectAnswer(true);
		questions.add(trueQuestion);
		
		TrueFalseQuestionModel falseQuestion = new TrueFalseQuestionModel("falseQu");
		falseQuestion.setPrompt("Die Bewertung der Pfeile im Gozinto-Graph gibt an, wie wichtig ein Einzelteil in der Fertigungsstruktur ist. Wahr oder falsch?");
		falseQuestion.setCorrectAnswer(false);
		questions.add(falseQuestion);
	}
	
	// Zeigt die entsprechende Frage an
	private void addQuestion(QuestionModel question) {
		if(question instanceof MultipleChoiceQuestionModel) lang.addMCQuestion((MultipleChoiceQuestionModel)question);
		if(question instanceof MultipleSelectionQuestionModel) lang.addMSQuestion((MultipleSelectionQuestionModel)question);
		if(question instanceof TrueFalseQuestionModel) lang.addTFQuestion((TrueFalseQuestionModel)question);
	}
	
	// Prüft ob eine Frage angezeigt werden soll
	private void showRandomQuestion() {
		if(questions.size() > 0) {
			if(helper.showQuestion(probability)) {
				int randIndex = helper.getRandomQuestion(questions.size()); 
				addQuestion(questions.get(randIndex));
				questions.remove(randIndex);
			}
		}
	}
	
	// Prüft ob die gleiche Anzahl an Bedarfe und dazugehörige Knoten angegeben wurden 
	private boolean checkNeeds(String[] needNames, int[] needs) {
		if(needNames.length != needs.length) return false;
		return true;
	}
	
	// Pürft ob ein Knoten benannt wurde, dessen Name aus höchstens drei Zeichen besteht
	// Die Länge sollte kleiner 3 betragen, damit die Knoten im Graph nicht zu breit werden!
	private boolean checkNodeNames(String nodeLabel) {
		if(nodeLabel.length() > 3 || nodeLabel.length() == 0) return false;
		return true;
	}
	
	// Beginnt den Algorithmus
	public int[] gozinto(int[][] adj, String[] nodeNames, String[] needNames, int[] needs) {
		setQuestions();
		
		// Verarbeitung der Eingabedaten
		ArrayList<GozintoNode> nodes = new ArrayList<GozintoNode>();
		for(int i = 0; i < adj.length; i++) {
			if(!checkNodeNames(nodeNames[i])) {
				Text error = lang.newText(new Coordinates(10, 80), "Es muss ein Name für jeden Knoten angegeben werden, der aus höchstens drei Zeichen besteht!", "errorNodes", null, textProps);
				error.show();
				return null;
			}
			nodes.add(new GozintoNode(nodeNames[i], i+1));
		}
		
		if(!checkNeeds(needNames, needs)) {
			Text error = lang.newText(new Coordinates(10, 80), "Die Anzahl der Bedarfe muss mit der Anzahl der Bedarfsknoten übereinstimmen!", "errorNodes", null, textProps);
			error.show();
			return null;
		}
		
		amount = new int[nodes.size()];
		for(int i = 0; i < needs.length; i++) {
			amount[helper.getIndex(nodes, needNames[i])] = needs[i];
		}
		
		SetProperties();
		source.show();
		GozintoGraph gozintoGraph = createGozintoGraph(nodes, adj);
		
		source.highlight(0);
		lang.nextStep("Erstellen des Gozinto-Graphen");

		gozGraph = generateGraph(gozintoGraph);
		gozGraph.show();
		
		showRandomQuestion();
		lang.nextStep();
		
		source.unhighlight(0);
		source.highlight(1);
		for(int i = 0; i < numbers.size(); i++) {
			numbers.get(i).show();
			lang.nextStep();
		}
		lang.nextStep();
		
		int[][] gozintoList = createGozintoList(gozintoGraph);
		int[][] table = createTable(gozintoGraph, gozintoList);
		result = calculateGozinto(table, gozintoList);
		lang.nextStep("Ergebnis");
		
		showRandomQuestion();
		showEndSlide(gozintoGraph);
		
		return result;
	}
		
	// Erstellt den Gozinto-Graphen
	private GozintoGraph createGozintoGraph(ArrayList<GozintoNode> nodes, int[][] adjMatrix) {
		GozintoGraph graph = new GozintoGraph(nodes);
		for (int i = 0; i < adjMatrix.length; i++) {
			for (int j = 0; j < i; j++) {
				if(adjMatrix[j][i] > 0){
					GozintoNode nodeSource = nodes.get(j);
					GozintoNode nodeDestination = nodes.get(i);

					graph.Insert(nodeSource, nodeDestination, adjMatrix[j][i]);
					graph.Insert(nodeDestination, nodeSource, 0);
				}
			}
		}
		return graph;
	}
	
	// Erstellt die Gozinto-Liste
	private int[][] createGozintoList(GozintoGraph graph) {
		int[][] gozintoList = new int[helper.countEdges(graph.getAdjMatrix())][3];
		source.unhighlight(1);
		source.highlight(2);
	    lang.nextStep("Aufstellen der Gozinto-Liste");
	    
	    generateGozintoList(gozintoList);
	    showRandomQuestion();
	    lang.nextStep();
		
	    int index = 0;
		int firstIndex = helper.findFirstIndex(graph.getAdjMatrix());
		source.highlight(3);
		gozGraph.highlightNode(firstIndex, null, null);
		ArrayList<Integer> highNodes = new ArrayList<Integer>();
		for(int i = 0; i < graph.getAdjMatrix().length; i++) {
			if(graph.getAdjMatrix()[i][firstIndex] > 0) {
				gozGraph.highlightNode(i, null, null);
				gozGraph.highlightEdge(i, firstIndex, null, null);
				highNodes.add(i);
			}
		}
		lang.nextStep();
		
		showRandomQuestion();
		
		for(int i = 0; i < highNodes.size(); i++) {
			gozGraph.unhighlightNode(highNodes.get(i), null, null);
			gozGraph.unhighlightEdge(highNodes.get(i), firstIndex, null, null);
		}
		
		gozGraph.unhighlightNode(firstIndex, null, null);
		
		dijCopy = new Text[gozintoList.length];
		int dijX = -1075;
		
		int jX = -1390;
		
		int iX = -1230;
		
		int y = 215;
		int diffY = 28;
		lang.nextStep();
		
		variables = lang.newVariables();
		variables.declare("int", "jVariable", String.valueOf(firstIndex + 1));
		variables.declare("int", "iVariable", String.valueOf(1));
		source.unhighlight(3);
		source.highlight(4);
		for(int j = firstIndex; j < graph.getAdjMatrix().length; j++) {
			source.unhighlight(6);
			for(int i = 0; i < j; i++) {
				if(graph.getAdjMatrix()[i][j] > 0) {
					source.unhighlight(5);
					variables.set("jVariable", String.valueOf(j + 1));
					variables.set("iVariable", String.valueOf(i + 1));
					gozintoList[index][0] = j + 1;
					gozGraph.highlightNode(j, null, null);
					Text nodeJ = lang.newText(numberCordinates.get(j), numbers.get(j).getText(), "number" + numbers.get(j).getText(), null, numberProps);
					nodeJ.show();
					nodeJ.moveTo(null, "translate", new Offset(jX, y, gozGraph, AnimalScript.DIRECTION_S), null, new MsTiming(1000));
					lang.nextStep();
					
					nodeJ.hide();
					nodeJ.setText("", null, null);
					gozList.put(index + 1, 0, String.valueOf(gozintoList[index][0]), null, null);
					gozintoList[index][1] = i + 1;
					gozGraph.highlightNode(i, null, null);
					Text nodeI = lang.newText(numberCordinates.get(i), numbers.get(i).getText(), "number" + numbers.get(i).getText(), null, numberProps);
					nodeI.show();
					nodeI.moveTo(null, "translate", new Offset(iX, y, gozGraph, AnimalScript.DIRECTION_S), null, new MsTiming(1000));
					lang.nextStep();
					
					nodeI.hide();
					nodeI.setText("", null, null);
					gozList.put(index + 1, 1, String.valueOf(gozintoList[index][1]), null, null);
					lang.nextStep();
					
					gozintoList[index][2] = graph.getAdjMatrix()[i][j];
					dijCopy[index] = lang.newText(new Offset(dijX, y, gozGraph, AnimalScript.DIRECTION_S), String.valueOf(gozintoList[index][2]), "d" + String.valueOf(i) + String.valueOf(j), null, textProps);
					dijCopy[index].hide();
					gozList.put(index + 1, 2, String.valueOf(gozintoList[index][2]), null, null);
					gozGraph.highlightEdge(i, j, null, null);
					y += diffY;
					index++;
					lang.nextStep();
					
					gozGraph.unhighlightNode(j, null, null);
					gozGraph.unhighlightNode(i, null, null);
					gozGraph.unhighlightEdge(i, j, null, null);
					lang.nextStep();
					
					source.highlight(5);
					lang.nextStep();
				}
			}
			source.highlight(6);
			lang.nextStep();
		}
		
		source.unhighlight(5);
		source.unhighlight(6);
		
		return gozintoList;
	}
	
	// Erstellt die Rechentabelle
	private int[][] createTable(GozintoGraph graph, int[][] gozintoList) {
		int[] vi_0 = new int[graph.getNodes().size()];
		for(int i = 0; i < vi_0.length; i++) {
			int counter = 0;
			for(int j = 0; j < gozintoList.length; j++) {
				if(i == gozintoList[j][1] - 1) counter++;
			}
			vi_0[i] = counter;
		}
		
		int width = helper.calcTableWidth(gozintoList);
		int[][] table = new int[vi_0.length][width];
		calculation = new Text[vi_0.length][width];
		source.unhighlight(2);
		source.unhighlight(4);
		source.highlight(7);
		source.highlight(8);
		source.highlight(9);
		source.highlight(10);
		lang.nextStep("Berechnung des Gozinto-Listen-Verfahrens");
		
		generateCalcTable(table, graph);
		
		showRandomQuestion();
		
		int y = tableY;
		
		for(int i = 0; i < table.length; i++) {
			// N_i^k
			table[i][0] = vi_0[i];
			calcTable.put(i + 1, 1, String.valueOf(table[i][0]), null, null);
			calculation[i][0] = lang.newText(new Offset(tableX, y, gozGraph, AnimalScript.DIRECTION_S), String.valueOf(table[i][0]), "V" + String.valueOf(i) + "0", null, textProps);
			calculation[i][0].hide();
			
			// V_i^k
			table[i][1] = amount[i];
			calcTable.put(i + 1, 2, String.valueOf(table[i][1]), null, null);
			calculation[i][1] = lang.newText(new Offset(tableX + tableDiffX, y, gozGraph, AnimalScript.DIRECTION_S), String.valueOf(table[i][1]), "N" + String.valueOf(i) + "1", null, textProps);
			calculation[i][1].hide();
			
			y += tableDiffY;
		}
		lang.nextStep();
		
		return table;
	}
	
	// Führt die Berechnung aus
	private int[] calculateGozinto(int[][] table, int[][] gozintoList) {
		// Gleichung Nik
		Text nik = lang.newText(new Offset(155, 265, header, AnimalScript.DIRECTION_S), "N_i^k", "nik", null, textProps);
		nik.hide();
		
		Text equal1 = lang.newText(new Offset(185, 265, header, AnimalScript.DIRECTION_S), " = ", "e", null, textProps);
		equal1.hide();
		
		Text dot = lang.newText(new Offset(230, 265, header, AnimalScript.DIRECTION_S), " * ", "dot", null, textProps);
		dot.hide();
		
		Text plus = lang.newText(new Offset(280, 265, header, AnimalScript.DIRECTION_S), " + ", "plus", null, textProps);
		plus.hide();
		
		Text equal2 = lang.newText(new Offset(330, 265, header, AnimalScript.DIRECTION_S), " = ", "equal2", null, textProps);
		equal2.hide();
		
		Text resultText = lang.newText(new Offset(355, 265, header, AnimalScript.DIRECTION_S), "", "resultText", null, textProps);
		resultText.hide();
		
		source.unhighlight(7);
		source.unhighlight(8);
		source.unhighlight(9);
		source.unhighlight(10);
		source.highlight(11);
		source.highlight(12);
		source.highlight(13);
		
		vik.show();
		
		int[] result = new int[table.length];
		int[][] resVec = new int[table.length][1];
		resVector = lang.newIntMatrix(new Offset(330, 0, calcTable, AnimalScript.DIRECTION_NE), resVec, "result", null, resProps);

		int x = 265;
		int y = 0 + (32 * resVector.getNrRows() / 2);
		
		res = lang.newText(new Offset(x, y, calcTable, AnimalScript.DIRECTION_NE), "R = ", "res", null, textProps);
		res.setFont(new Font("Monospaced", Font.PLAIN, 30), null, null);
		
		int lastIndex = gozintoList[gozintoList.length - 1][0] - 1;
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		for(int i = 0; i < lastIndex; i++) {
			nodes.add(i);
		}
		
		int[] k = new int[table.length];
		
		for(int s = gozintoList.length - 1; s >= 0; s--) {
			int j = gozintoList[s][0] - 1;
			int i = gozintoList[s][1] - 1;
			int d = gozintoList[s][2];
			
			if(lastIndex != j) {
				source.highlight(16);
				for(int t = 0; t < nodes.size(); t++) {
					table[nodes.get(t)][k[nodes.get(t)] + 2] = table[nodes.get(t)][k[nodes.get(t)]];
					table[nodes.get(t)][k[nodes.get(t)] + 3] = table[nodes.get(t)][k[nodes.get(t)] + 1];
					calcTable.put(nodes.get(t) + 1, k[nodes.get(t)] + 2 + 1, String.valueOf(table[nodes.get(t)][k[nodes.get(t)] + 2]), null, null);
					calcTable.put(nodes.get(t) + 1, k[nodes.get(t)] + 3 + 1, String.valueOf(table[nodes.get(t)][k[nodes.get(t)] + 3]), null, null);
					
					calculation[nodes.get(t)][k[nodes.get(t)] + 2] = lang.newText(new Offset(tableX + tableDiffX * (k[nodes.get(t)] + 2), tableY + tableDiffY * nodes.get(t), gozGraph, AnimalScript.DIRECTION_S), String.valueOf(table[nodes.get(t)][k[nodes.get(t)] + 2]), "V" + String.valueOf(nodes.get(t)) + String.valueOf(k[nodes.get(t)]), null, textProps);
					calculation[nodes.get(t)][k[nodes.get(t)] + 2].hide();
					
					calculation[nodes.get(t)][k[nodes.get(t)] + 3] = lang.newText(new Offset(tableX + tableDiffX * (k[nodes.get(t)] + 2), tableY + tableDiffY * nodes.get(t), gozGraph, AnimalScript.DIRECTION_S), String.valueOf(table[nodes.get(t)][k[nodes.get(t)] + 3]), "N" + String.valueOf(nodes.get(t)) + String.valueOf(k[nodes.get(t)]), null, textProps);
					calculation[nodes.get(t)][k[nodes.get(t)] + 3].hide();
					
					k[nodes.get(t)] += 2;
				}
				nodes = new ArrayList<Integer>();
				for(int t = 0; t < j; t++) {
					nodes.add(t);
				}
				lang.nextStep();
				
				source.unhighlight(16);
			}
			
			if(table[i][k[i]] != 0) {
				table[i][k[i] + 3] = table[j][k[j] + 1] * d + table[i][k[i] + 1];
				
				source.highlight(14);
				
				String first = String.valueOf(table[j][k[j] + 1]);
				String second = String.valueOf(d);
				String third = String.valueOf(table[i][k[i] + 1]);
				String r = String.valueOf(table[i][k[i] + 3]);
				String equation = first + " * " + second + " + " + third + " = " + r;
				
				nik.show();
				equal1.show();
				
				calculation[i][k[i] + 3] = lang.newText(new Offset(tableX + tableDiffX * (k[i] + 3) , tableY + tableDiffY * i, gozGraph, AnimalScript.DIRECTION_S), String.valueOf(table[i][k[i] + 3]), "N" + String.valueOf(i) + String.valueOf(k[i] + 3), null, textProps);
				calculation[i][k[i] + 3].hide();
				resultText.setText(String.valueOf(table[i][k[i] + 3]), null, null);
				
				gozList.highlightElem(s + 1, 0, null, null);
				gozList.highlightElem(s + 1, 1, null, null);
				gozList.getElement(s + 1, 0);
				gozList.getElement(s + 1, 1);
				
				calculation[j][k[j] + 1].show();
				calculation[j][k[j] + 1].moveTo(null, "translate", new Offset(205, 265, header, AnimalScript.DIRECTION_S), null, new MsTiming(1000));
				calcTable.highlightElem(j + 1, k[j] + 1 + 1, null, null);
				calcTable.getElement(j + 1, k[j] + 1 + 1);
				lang.nextStep();
				
				dot.show();
				dijCopy[s].show();
				dijCopy[s].moveTo(null, "translate", new Offset(255, 265, header, AnimalScript.DIRECTION_S), null, new MsTiming(1000));
				
				gozList.highlightElem(s + 1, 2, null, null);
				gozList.getElement(s + 1, 2);
				lang.nextStep();
				
				plus.show();
				calculation[i][k[i] + 1].show();
				calculation[i][k[i] + 1].moveTo(null, "translate", new Offset(305, 265, header, AnimalScript.DIRECTION_S), null, new MsTiming(1000));
				
				calcTable.highlightElem(i + 1, k[i] + 1 + 1, null, null);
				calcTable.getElement(i + 1, k[i] + 1 + 1);
				lang.nextStep();
				
				equal2.show();
				resultText.show();
				lang.nextStep();
				
				resultText.moveTo(null, "translate", new Offset(tableX + tableDiffX * (k[i] + 3) , tableY + tableDiffY * i, gozGraph, AnimalScript.DIRECTION_S), null, new MsTiming(1000));
				lang.nextStep();
				
				resultText.hide();
				resultText = lang.newText(new Offset(355, 265, header, AnimalScript.DIRECTION_S), "", "resultText", null, textProps);
				resultText.hide();
				calcTable.put(i + 1, k[i] + 3 + 1, String.valueOf(table[i][k[i] + 3]), null, null);
				
				calcTable.highlightCell(i + 1, k[i] + 1, null, null);
				calcTable.getElement(i + 1, k[i] + 1);
				
				lang.nextStep();
				calcTable.unhighlightElem(j + 1, k[j] + 1 + 1, null, null);
				calcTable.unhighlightElem(i + 1, k[i] + 1 + 1, null, null);
				
				nik.hide();
				equal1.hide();
				
				calculation[j][k[j] + 1].hide();
				dijCopy[s].hide();
				calculation[i][k[i] + 1].hide();
				
				dot.hide();
				plus.hide();
				equal2.hide();
				lang.nextStep();
				
				source.unhighlight(14);
				source.highlight(15);
				
				table[i][k[i] + 2] = table[i][k[i]] - 1;
				
				String v = String.valueOf(table[i][k[i]]);
				String vr = String.valueOf(table[i][k[i] + 2]);
				equation = v + " - 1 = " + vr;
				vik.setText(equation, null, null);
				
				calculation[i][k[i] + 2] = lang.newText(new Offset(tableX + tableDiffX * (k[i] + 2) , tableY + tableDiffY * i, gozGraph, AnimalScript.DIRECTION_S), String.valueOf(table[i][k[i] + 2]), "V" + String.valueOf(i) + String.valueOf(k[i] + 2), null, textProps);
				calculation[i][k[i] + 2].hide();
				
				calcTable.highlightCell(i + 1, k[i] + 1, null, null);
				calcTable.getElement(i + 1, k[i] + 1);
				lang.nextStep();
				
				calcTable.highlightCell(i + 1, k[i] + 2 + 1, null, null);
				calcTable.getElement(i + 1, k[i] + 2 + 1);
				calcTable.put(i + 1, k[i] + 2 + 1, String.valueOf(table[i][k[i] + 2]), null, null);
				lang.nextStep();
				
				source.unhighlight(15);
				
				gozList.unhighlightElem(s + 1, 0, null, null);
				gozList.unhighlightElem(s + 1, 1, null, null);
				gozList.unhighlightElem(s + 1, 2, null, null);
				
				calcTable.unhighlightCell(i + 1, k[i] + 1, null, null);
				calcTable.unhighlightCell(i + 1, k[i] + 2 + 1, null, null);
				vik.setText("", null, null);
				
				k[i] = k[i] + 2;
				
				nodes.remove(i);
			}
			
			lastIndex = j;
		}
		
		demandText.hide();
		resultInfo = lang.newText(new Offset(-905, 165, gozGraph, AnimalScript.DIRECTION_S), "Die am weitesten rechts stehenden Bedarfe werden in den Bedarfsvektor eingetragen.", "resultInfo", null, textProps);
		resultInfo.show();
		lang.nextStep();
		
		// Eintragen der Ergebnisse in den Ergebnisvektor
		for(int i = 0; i < table.length; i++) {
			for(int j = table[i].length-1; j >= 0; j -= 2) {
				if(table[i][j-1] == 0 && table[i][j] != 0) {
					result[i] = table[i][j];
					resVec[i][0] = result[i];
					
					resVector.put(i, 0, resVec[i][0], null, null);
					calcTable.highlightCell(i + 1, j + 1, null, null);
					
					lang.nextStep();
					
					calcTable.unhighlightCell(i + 1, j + 1, null, null);
					break;
				}
			}
		}
		
		return result;
	}
	
	// Initialisieren der Properties
	private void SetProperties() {
		// Setzen des Headers
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		header = lang.newText(new Coordinates(10, 30), "Gozinto-Listen-Verfahren", "header", null, headerProps);
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		headerRect = lang.newRect(new Offset(-5, -5, "header",
		        AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
		        null, rectProps);
		
		// V_i^k
		vik = lang.newText(new Offset(155, 269, header, AnimalScript.DIRECTION_S), "", "vik", null, textProps);
		vik.hide();
		lang.nextStep("Einleitung");
		
		// Properties für den Gozinto-Graphen
		graphProps = new GraphProperties();
		graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);
		graphProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);
		graphProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.WHITE);
		graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		
		source = lang.newSourceCode(new Offset(0, 29, header, AnimalScript.DIRECTION_SW), "source", null, sourceProps);
		
		// Anlegen der Beschreibung
		description = lang.newSourceCode(new Coordinates(10, 80), "description", null, sourceProps);
		description.addCodeLine("Das Gozinto-Listen-Verfahren wird genutzt, um Stücklisten aufzulösen. Somit kann mit diesem Verfahren ausgehend vom Primärbedarf eines Produktes der Sekundärbedarf anhand der Fertigungsstruktur errechnet werden.", null, 0, null);
		description.addCodeLine("", null, 0, null);
		description.addCodeLine("Dieses Verfahren baut auf einem sogenannten Gozintographen auf. Dieser dient dazu die Fertigungsstruktur und die quantitativen Input-Output-Beziehungen grafisch darzustellen.", null, 0, null);
		description.addCodeLine("", null, 0, null);
		description.addCodeLine("Der Gozintograph ist ein gerichteter, bewerteter Graph, dessen Knoten Endprodukte, Baugruppen und Einzelteile darstellen, während dessen Pfeile als Input-Output-Beziehungen zu verstehen sind.", null, 0, null);
		description.addCodeLine("", null, 0, null);
		description.addCodeLine("Die Bewertung der Pfeile gibt an, wie viele Einheiten eines Einzelteils bzw. einer Baugruppe direkt in eine übergeordnete Baugruppe bzw. in ein Endprodukt eingehen.", null, 0, null);
		
		// Anlegen des Pseudocodes
		source.addCodeLine("1. Erstellen des Gozinto-Graphens.", null, 0, null); // 0
		source.addCodeLine("2. Knoten im Gozinto-Graphen nummerieren.", null, 0, null); // 1
		source.addCodeLine("3. Aufstellung einer Gozinto-Liste in der alle Pfeile systematisch erfasst und nacheinander abgearbeitet werden.", null, 0, null); // 2
		source.addCodeLine("a. Beginne Liste mit dem am niedrigsten indizierten Knoten, in den Pfeile einmünden.", null, 1, null); // 3
		source.addCodeLine("b. Trage in die Tabelle, die in die Eingangsknoten mündenden Ausgangsknoten mit den dazugehörigen Bewertungen, ein.", null, 1, null); // 4
		source.addCodeLine("i. Solange j noch Eingangsknoten besitzt: Gehe zum nächst höheren Index i, welcher ein Ausgangsknoten von j ist.", null, 2, null); // 5
		source.addCodeLine("ii. Solange bis der letzt Knoten erreicht wurde: Gehe zum nächst höheren Index j (j = j + 1).", null, 2, null); // 6
		source.addCodeLine("4. Anlegen einer Rechentabelle, mit", null, 0, null); // 7
		source.addCodeLine("i = Index der Knoten,", null, 1, null); // 8
		source.addCodeLine("V_i = Ausgangsvalenz (Zahl der nicht abgearbeiteten Pfeile, die aus einem Knoten hervorgehen) und", null, 1, null); // 9
		source.addCodeLine("N_i = kumulierter Bedarfsmengenvektor (für N_i^0 ist N_i gleich dem Nettoprimärbedarf (Primärbedarf - Lagerbestand)).", null, 1, null); // 10
		source.addCodeLine("5. Gozinto-Liste von unten nach oben abarbeiten.", null, 0, null); // 11
		source.addCodeLine("Sei k = 1", null, 1, null); // 12
		source.addCodeLine("solange bis alle Ausgangtsknoten abgearbeitet sind", null, 1, null); // 13
		source.addCodeLine("a. N_i^k = N_j^(k-1) * d_ij + N_i^(k-1)", null, 1, null); // 14
		source.addCodeLine("b. V_i^k = V_i^(k-1)", null, 1, null); // 15
		source.addCodeLine("c. wenn neues j gesetzt wird k = k + 1", null, 1, null); // 16
		source.hide();
		
		lang.nextStep();
		description.hide();
	}
	
	// Erstellt die Grafik für den Gozinto-Graph
	private Graph generateGraph(GozintoGraph gozintoGraph) {
		numbers = new ArrayList<Text>();
		numberCordinates = new ArrayList<Offset>();
		String[] labels = new String[gozintoGraph.getNodes().size()];
		int[][] coordinates = GetCoordinates(gozintoGraph.getNodes());
		Node[] graphNodes = new Node[gozintoGraph.getNodes().size()];
		numberProps = new TextProperties();
		numberProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		numberProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		
		for(int i = 0; i < gozintoGraph.getNodes().size(); i++) {
			labels[i] = gozintoGraph.getNodes().get(i).getName();
			graphNodes[i] = new Offset(coordinates[i][0], coordinates[i][1], source, AnimalScript.DIRECTION_NE);
			numbers.add(lang.newText(new Offset(coordinates[i][0], coordinates[i][1] + 11, source, AnimalScript.DIRECTION_NE), String.valueOf(gozintoGraph.getNodes().get(i).getNumber()), String.valueOf(gozintoGraph.getNodes().get(i).getNumber()), null, numberProps));
			numbers.get(i).hide();
			numberCordinates.add(new Offset(coordinates[i][0], coordinates[i][1] + 11, source, AnimalScript.DIRECTION_NE));
		}
		
		Graph graph = lang.newGraph("graph", gozintoGraph.getAdjMatrix(), graphNodes, labels, null, graphProps);
		graph.hide();
		return graph;
	}
	
	// Setzt die Koordinaten der Knoten für den Gozinto-Graphen
	private int[][] GetCoordinates(ArrayList<GozintoNode> nodes) {
		int width = 800;
		
		// Zählt die Anzal der Knoten pro Ebene (Resourcen - Zwischenprodukte - Endprodukt)
		ArrayList<Integer> areas = new ArrayList<Integer>();
		int number = 1;
		for(int i = 1; i < nodes.size(); i++) {
			String name1 = nodes.get(i).getName().substring(0, nodes.get(i).getName().length() - 1);
			String name2 = nodes.get(i-1).getName().substring(0, nodes.get(i-1).getName().length() - 1);
			if(name1.equals(name2)) {
				number++;
			} else {
				areas.add(number);
				number = 1;
			}
		}
		
		areas.add(1); // Endprodukt muss hinzugefügt werden!!!
		
		// Setzen der Koordinaten
		int[][] coordinates = new int[nodes.size()][2];
		int length = 0;
		int y = -60;
		for(int i = 0; i < areas.size(); i++) {
			int widths = width / areas.get(i);
			int center = widths / 2;
			int x = widths - center;
			for(int j = length; j < areas.get(i) + length; j++) {
				coordinates[j][0] = x + 200;
				coordinates[j][1] = y;
				x = x + widths;
			}
			y += 100;
			length += areas.get(i);
		}
		
		return coordinates;
	}
	
	// Erstellt die Gozinto-List (StringMatrix)
	private void generateGozintoList(int[][] gozintoList) {
		String[][] stringList = new String[gozintoList.length + 1][gozintoList[0].length];
		gozList = lang.newStringMatrix(new Offset(-1395, 185, gozGraph, AnimalScript.DIRECTION_S), stringList, "GozintoList", null, gozintoListProps);
		
		gozList.put(0, 0, "j (Eingangsknoten)", null, null);
		gozList.put(0, 1, "i (Ausgangsknoten)", null, null);
		gozList.put(0, 2, "dij (Pfeilbewertung)", null, null);
		
		gozListCounter = lang.newCounter(gozList);
		CounterProperties cp = new CounterProperties();
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
		
		gList = lang.newText(new Offset(-1395, 120, gozGraph, AnimalScript.DIRECTION_S), "Gozinto-Liste:", "gozListText", null, textProps);
		viewGozList = lang.newCounterView(gozListCounter, new Offset(-1285, 115, gozGraph, AnimalScript.DIRECTION_S), cp, true, true);
	}
	
	// Erstellt die Rechentabelle (StringMatrix)
	private void generateCalcTable(int[][] table, GozintoGraph graph) {
		// Anzeigen der Bedarfe
		StringBuilder builder = new StringBuilder("Bedarf an: ");
		for(int i = 0; i < amount.length; i++) {
			if(amount[i] > 0) {
				builder.append(graph.getNodes().get(i).getName() + " = " + String.valueOf(amount[i]) + "; ");
			}
		}
		demandText = lang.newText(new Offset(-905, 165, gozGraph, AnimalScript.DIRECTION_S), builder.toString(), "demand", null, textProps);
		
		// Erstellen der Rechentabelle
		String[][] stringTable = new String[table.length + 1][table[0].length + 1];
		calcTable = lang.newStringMatrix(new Offset(-905, 185, gozGraph, AnimalScript.DIRECTION_S), stringTable, "rechentabelle", null, calcProps);
		
		tableCounter = lang.newCounter(calcTable);
		CounterProperties cp = new CounterProperties();
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
		
		tabText = lang.newText(new Offset(-905, 120, gozGraph, AnimalScript.DIRECTION_S), "Rechentabelle:", "tabText", null, textProps);
		viewCalcTable = lang.newCounterView(tableCounter, new Offset(-795, 115, gozGraph, AnimalScript.DIRECTION_S), cp, true, true);
		
		calcTable.put(0, 0, "i", null, null);
		for(int i = 1; i < stringTable.length; i++) {
			calcTable.put(i, 0, String.valueOf(i), null, null);
		}
		
		int diff = 1;
		for(int i = 1; i < stringTable[0].length; i++) {
			if(i % 2 == 0) {
				diff++;
				calcTable.put(0, i, "N_i^" + String.valueOf(i-diff), null, null);
			} else {
				calcTable.put(0, i, "V_i^" + String.valueOf(i-diff), null, null);
			}
		}
	}
	
	// Zeigt die Abschlussfolie an
	private void showEndSlide(GozintoGraph g){
		source.hide();
		gozList.hide();
		calcTable.hide();
		gozGraph.hide();
		resultInfo.hide();
		resultInfo.setText("", null, null);
		viewGozList.hide();
		viewCalcTable.hide();
		gList.hide();
		tabText.hide();
		
		for(int i = 0; i < numbers.size(); i++) {
			numbers.get(i).hide();
		}
		
		res.hide();
		resVector.hide();
		
		Text resources = lang.newText(new Coordinates(10, 150), "Folgende Mengen an Produkten werden benötigt:", "resources", null, textProps);
		
		String resourceShort = g.getNodes().get(0).getName().substring(0, 0);
		
		for(int i = 0; i < g.getNodes().size(); i++) {
			String temp = g.getNodes().get(i).getName().substring(0, 0);
			if(temp.equals(resourceShort)) {
				lang.newText(new Coordinates(10, 170 + i * 20), "Es werden von " + g.getNodes().get(i).getName() + " " + String.valueOf(result[i]) + " Einheiten benötigt.", "bedarf" + String.valueOf(i), null, textProps);
			}
		}
	}
}
