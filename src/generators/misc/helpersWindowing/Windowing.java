package generators.misc.helpersWindowing;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;

import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import interactionsupport.models.MultipleChoiceQuestionModel;
public class Windowing {
	
	private Language lang;
	private static String[][] allDataSet;
	private static int numColumn;
	private SourceCodeProperties scProbs;
	private SourceCodeProperties cProbs;
	private TextProperties secondHeaderProbs;
	private TextProperties normalTextProbs;
	private MatrixProperties tableProbs;
	private SourceCode sc;
	private static int lenWindow;
	private StringMatrix dataTable;
	private static Classifiers classifier;
	SourceCode SaC;
	private static Integer numPos;
	private static String[] header;
	private StringMatrix trainDataTable;
	private static int[] windowDataSet;
	private int numWindow = 0;
	private boolean quest = false;
	private int numHeuristicTable = 0;
	
	public static void main(Language lang, int windowLenght, int numTrain, int numCol, String[] headers, String[][]data, int[] testDataSets, SourceCodeProperties sourceCodeProb, MatrixProperties tabellenProb, SourceCodeProperties klassifiziererProb) 
	{
		Language l = lang;
		
		Windowing b = new Windowing(l);
		lenWindow = windowLenght;
		numColumn = numCol;
		
		b.discription();
		
		header = headers;
		//testDataSets =
		allDataSet = data;
		windowDataSet = testDataSets;
		
		b.createProperties(sourceCodeProb, tabellenProb, klassifiziererProb);
		b.initWindowing();
		b.createTrainingsSets();
		b.checkTestData(null);
		b.summary();
		
	}
	
	public Windowing(Language l) 
	{
	    lang = l;
	    // Initialisierung von StepMode 
	    lang.setStepMode(true);
	    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	    
	}
	private String[][] createTable(int len)
	{
		//Tabellen beschriftung 
		String[][] table = new String[len][numColumn];
		for (int i = 0; i < numColumn; i++)
			table[0][i] = header[i];
		for(int i = 0; i < numColumn; i++)
			for(int j = 1; j < len; j++)
				table[j][i] = "";
		return table;
	}
	
	private void createHeader()
	{
		// Überschrift Windowing" wird hinzugefügt
		TextProperties headerProbs = new TextProperties();
		headerProbs.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		headerProbs.set(AnimationPropertiesKeys.FONT_PROPERTY,  new Font("SansSerif", Font.BOLD, 30));
		
		Text header = lang.newText(new Coordinates(20, 20), "Windowing [DE]", "header", null, headerProbs);
		
		RectProperties recProbs = new RectProperties();
		recProbs.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		recProbs.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		recProbs.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(119, 206, 250));
		
		lang.newRect(new Offset(-15, -15, header, "NW"), new Offset(15, 15, header, "SE"),"headlinebg", null, recProbs);
	}
	
	public void discription()
	{
		createHeader();
	
		SourceCodeProperties discrpProbs = new SourceCodeProperties();
		discrpProbs.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		discrpProbs.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLACK);
		discrpProbs.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
		discrpProbs.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		discrpProbs.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 16));
		
		// Beschreibung wird hinzugefügt
		SourceCode discrp = lang.newSourceCode(new Offset(4,25,"headlinebg", "SW"), "description", null, discrpProbs);
		discrp.addCodeLine("Der Windowing-Algorithmus ermöglicht das Erstellen von Klassifizieren ohne den gesamten Datensatz zu verwenden, sodass die Gefahr des Overfitting (Überanpassung an den Datensatz) reduziert wird.", null, 0, null);
		discrp.addCodeLine("", null, 0, null);
		discrp.addCodeLine("Umgesetzt wird dies, indem nur x Einträge (Window) aus dem Datensatz betrachtet werden und darauf ein Klassifizierer trainiert wird. ", null, 0, null);
		discrp.addCodeLine("", null, 0, null);
		discrp.addCodeLine("Nun wird geschaut, ob die Vorhersage des erzeugten Klassifizierers mit den Daten im Datensatz übereinstimmen. Ist dies der Fall, ist der Algorithmus zu Ende.", null, 0, null);
		discrp.addCodeLine("", null, 0, null);
		discrp.addCodeLine("Falls jedoch eine Vorhersage falsch ist, wird der Eintrag, der falsch klassifiziert wurde im Window aufgenommen und erneut ein Klassifizierer trainiert und überprüft.", null, 0, null);
		discrp.addCodeLine("", null, 0, null);
		discrp.addCodeLine("Als Klassifizierer kann man einen beliebigen Algorithmus wählen. Hier wird der Separate-and-Conquer Algorithmus in der Variante des Top-Down Hill Climbing verwendet. Dabei wird ein Datensatz positiv klassifiziert, sobald eine Regel erfüllt ist. ", null, 0, null);
		lang.nextStep("Beschreibung");
	}
	private void createProperties(SourceCodeProperties sourceCodeProb, MatrixProperties tabellenProb, SourceCodeProperties klassifiziererProb)
	{
		// Alle benötigten Properties
		scProbs = sourceCodeProb;
		tableProbs = tabellenProb;
		cProbs = klassifiziererProb;
		
		secondHeaderProbs = new TextProperties();
		secondHeaderProbs.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		secondHeaderProbs.set(AnimationPropertiesKeys.FONT_PROPERTY,  new Font("SansSerif", Font.BOLD, 14));
		
		normalTextProbs = new TextProperties();
		normalTextProbs.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		normalTextProbs.set(AnimationPropertiesKeys.FONT_PROPERTY,  new Font("SansSerif", Font.PLAIN, 14));
	}
	private void initWindowing()
	{
		lang.hideAllPrimitives();
		createHeader();
		
		// SorceCode wird hinzugeügt
		sc = lang.newSourceCode(new Offset(4,25,"headlinebg", "SW"), "listSource", null, scProbs);
		sc.addCodeLine("1. Initialisiere den Window mit 'x' Daten (zufälliges Auswählen ohne Wiederholungen)", null, 0, null);
		sc.addCodeLine("2. Lerne auf den Window ein Klassifizierer", null, 0, null);
		sc.addCodeLine("3. Wenn alle Einträge im Datensatz richtig klassifiziert werden -> return Klassifizierer", null, 0, null);
		sc.addCodeLine("4. Falls ein Eintrag falsch klassifiziert wird -> füge den Eintrag dem Window hinzu und gehe zu 2.", null, 0, null);
		
		lang.newText(new Offset(5, -20, sc, "NW"), "Algorithmus", "headerlistSource", null, normalTextProbs);
		
		lang.newText(new Offset(100, 0, sc, "NE"), "//  Hier: x = " + (lenWindow - 1) , "info", null, normalTextProbs);
		
		// Datensatz wird hinzugeügt
		dataTable = lang.newStringMatrix(new Offset(4, 55, sc, "SW"), allDataSet, "dataset", null, tableProbs);
		for (int i = 0; i < numColumn; i++)
			dataTable.setGridFont(0, i, new Font("SansSerif", Font.BOLD, 14), null, null);		
		
		lang.newText(new Offset(5, -20, dataTable, "NW"), "Datensatz:", "headerDataset", null, secondHeaderProbs);
		
		lang.nextStep("Initialisierung");
	}
	
	public void createTrainingsSets()
	{
		// erstellt und befüllt die Trainssets
		sc.highlight(0);
		
		lang.addLine("line \"mainLine\" offset (85, 0) from \"dataset\" NE offset (85, 150) from \"dataset\" SE");
		
		classifier = new Classifiers();
		
		
			trainDataTable = lang.newStringMatrix(new Offset(35, 0, "mainLine", "NE"), createTable(lenWindow), "windowSet" + numWindow, null, tableProbs);
		for (int j = 0; j < numColumn; j++)
			trainDataTable.setGridFont(0, j, new Font("SansSerif", Font.BOLD, 14), null, null);
		lang.newText(new Offset(5, -20, trainDataTable, "NW"), "Window:" , "headerW", null, secondHeaderProbs);
		for(int l = 0; l < lenWindow-1; l++)
		{
			for (int k = 0 ; k < numColumn; k++)
			{
				trainDataTable.put(l+1, k, allDataSet[windowDataSet[l]][k], null, null);
				dataTable.highlightCell(windowDataSet[l], k, null, null);
			}
			
			lang.nextStep();
			
			for (int k = 0 ; k < numColumn; k++)
			{
				dataTable.unhighlightCell(windowDataSet[l], k, null, null);
			}
			
		}
		visualSepAndCon();
		createClassifier(trainDataTable);
		lang.nextStep("Erzeuge Window");
		
	}
	public void createClassifier(StringMatrix trainDataTable)
	{
		// erstellt ein Kassifizierer für die Trainsets
		sc.highlight(1);
		sc.unhighlight(0);
		sc.unhighlight(3);
				
		
		lang.addLine("Line \"lineCL" + numWindow + "\" offset (0,2) from \"windowSet" + numWindow+"\" S offset (0, 55) from \"windowSet" + numWindow + "\" S fwArrow");
		lang.newText(new Offset(-180, 10, "lineCL" + numWindow, "NW"), "Separate and Conquer", "SaC"+ numWindow, null, normalTextProbs);
		
		makeClassifier(trainDataTable, new Offset(-100,20,"lineCL" + numWindow , "SW"));
		
		lang.newRect(new Offset(-5, -5, SaC, "NW"), new Offset(5, 5, SaC, "SE"),"rec" + numWindow, null);
		lang.newText(new Offset(5, -20, "rec"+ numWindow, "NW"), "Klassifizierer :", "headerKlassifizierer" + numWindow, null, secondHeaderProbs);
	}
	
	public void makeClassifier(StringMatrix trainDataTable, Node position)
	{
		// erstellt ein Kassifizierer
		SepAndCon(trainDataTable);
		
		SaC = lang.newSourceCode(position, "SaC", null, cProbs);
		if(classifier.getNumRule() == 0)
		{
			SaC.addCodeLine("1. false -> Ja", null, 0, null);
		}
		for(int j = 0 ; j < classifier.getNumRule();j++)
		{
			String temp = classifier.getElemOfRule(j, 0).getName();
			for(int k = 1; k < classifier.getLenRule(j); k++)
			{
				temp = temp + " und " + trainDataTable.getElement(0, classifier.getElemOfRule(j, k).getX()) + " = " + classifier.getElemOfRule(j, k).getName();
			}
			SaC.addCodeLine(j+1 + ". " + trainDataTable.getElement(0, classifier.getRule(j).getFirst().getX()) + " = " + temp + " -> Ja", null, 0, null);
		}
		
		
	}
	
	private void checkTestData(Classifiers oldC)
	{
		// Kassifizeren der Testdaten
		sc.highlight(2);
		sc.unhighlight(1);
		
		numPos = 0;
		
		StringMatrix mat;
		Text t = null;
		for(int j = 1 ;j < allDataSet.length; j++)
		{
		
			
			
			
				int classPos = 0;
				for(int k= 0; k <numColumn; k++)
				{
					dataTable.unhighlightCell(j-1, k, null, null);
					dataTable.highlightCell(j, k, null, null);
				}
				String[][] tempMatrix = new String[1][numColumn];
				
				lang.addLine("Line \"line" + "\" offset (0,0) from \"rec" + numWindow + "\" S offset (0, 55) from \"rec" + numWindow + "\" S fwArrow");
				mat = lang.newStringMatrix(new Offset(-150, 10, "line", "SW"), createTable(2) , "dataClass" , null, tableProbs);
				
				for(int l = 0; l < classifier.getClassifier().size() - 1 ; l++)
				{
					boolean ruleCovered = true;
					int m = 0;
					for(m = 0; m < classifier.getLenRule(l); m++)
					{
						boolean elemCover = false;
						for(int n = 0; n < allDataSet[j].length -1; n++)
						{
							tempMatrix[0][n] = allDataSet[j][n];
							if(classifier.getElemOfRule(l, m).getName().equals(tempMatrix[0][n]))
							{
								elemCover = true;
							}
								
						}
						if(!elemCover)
						{
							ruleCovered = false;	
							break;
						}
						
					}
					if(ruleCovered)
					{
						classPos++;
						SaC.highlight(l);
					}
				}
				
				
				
				for (int k = 0; k < numColumn; k++)
					mat.setGridFont(0, k, new Font("SansSerif", Font.BOLD, 14), null, null);
				
				
				
				if(classPos > 0)
				{
					
					tempMatrix[0][numColumn-1] = "Ja";
					numPos++;
					
					
				}
				else
					tempMatrix[0][numColumn-1] = "Nein";
				
				for (int k = 0 ; k < numColumn; k++)
				{
					mat.put(1, k, tempMatrix[0][k], null, null);
				}
				mat.highlightElem(1, numColumn-1, null, null);
				
				if(!allDataSet[j][allDataSet[0].length-1].equals(tempMatrix[0][numColumn-1]))
				{
					
					if(!quest)
					{
						quest = true;
						lang.nextStep();
						MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel("classifi");
						
						question.setPrompt("Was geschieht als nächstes?");
						
						question.addAnswer("Der aktuelle Eintrag wird zum Window hinzufügt", 1, "Richtig!");
						question.addAnswer("Der nächste Eintarg wird überprüft", 0, "Falsch!");
						
						lang.addMCQuestion(question);
						lang.nextStep();
				
					}
					
					t = lang.newText(new Offset(80, 0, mat, "E"),  "//Eintrag wird flasch klassifiziert!", "CLR1", null,normalTextProbs );
					
					lang.nextStep();
					
					sc.highlight(3);
					sc.unhighlight(2);
					
					trainDataTable.hide();
					lang.addLine("hideCode \"lineCL" + numWindow + "\"");
					lang.addLine("hideCode \"SaC" + numWindow + "\"");
					lang.addLine("hideCode \"rec" + numWindow + "\"");
					lang.addLine("hideCode \"line\"");
					lang.addLine("hideCode \"headerKlassifizierer" + numWindow + "\"");
					
					SaC.hide();
					mat.hide();
					t.hide();
					
					lenWindow++;
					numWindow++;
					
					int[] tempSet = windowDataSet;
					windowDataSet = new int[lenWindow];
					for(int k = 0; k <= tempSet.length-1; k++)
						windowDataSet[k] = tempSet[k];
					windowDataSet[lenWindow-2] = j;
					
					trainDataTable = lang.newStringMatrix(new Offset(35, 0, "mainLine", "NE"), createTable(lenWindow), "windowSet"+ numWindow, null, tableProbs);
					
					for (int k = 0; k < numColumn; k++)
						trainDataTable.setGridFont(0, k, new Font("SansSerif", Font.BOLD, 14), null, null);
					lang.newText(new Offset(5, -20, trainDataTable, "NW"), "Window:" , "headerW", null, secondHeaderProbs);
					for(int l = 0; l < lenWindow-1; l++)
					{
						for (int k = 0 ; k < numColumn; k++)
						{
							trainDataTable.put(l+1, k, allDataSet[windowDataSet[l]][k], null, null);
							dataTable.highlightCell(windowDataSet[l], k, null, null);
						}
						
						
						for (int k = 0 ; k < numColumn; k++)
						{
							dataTable.unhighlightCell(windowDataSet[l], k, null, null);
						}
						
					}
					lang.nextStep();
					
					visualSepAndCon();
					lang.nextStep();
					createClassifier(trainDataTable);
					checkTestData(classifier);
					return;
				}
				else 
				{
					if(!quest)
					{
						quest = true;
						lang.nextStep();
						MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel("classifi");
						
						question.setPrompt("Was geschieht als nächstes?");
						
						question.addAnswer("Der aktuelle Eintrag wird zum Window hinzufügt", 0, "Falsch!");
						question.addAnswer("Der nächste Eintarg wird überprüft", 1, "Richtig!");
						
						lang.addMCQuestion(question);
						lang.nextStep();
					}
					 t = lang.newText(new Offset(80, 0, mat, "E"),  "//Eintrag wird richtig klassifiziert!", "CLR", null,normalTextProbs );
					
				}
			
			
			
			lang.nextStep("Klassifiziere " + j + ". Eintrag");
			
				t.hide();
				mat.hide();
				lang.addLine("hide \"line"  +"\"");
				for(int k = 0; k < SaC.length(); k++)
				{
					SaC.unhighlight(k);
				}
			
		}
		
		t = lang.newText(new Offset(30, 0, "rec" + numWindow, "E"),  "//erzeugter Klassifizierer", "CLK", null,normalTextProbs );
		for(int i = 0; i < numColumn; i++)
		{
			dataTable.unhighlightCell(dataTable.getNrRows()-1	, i, null, null);
		}
	}
	
	
	private void summary() 
	{
		lang.nextStep();
		lang.hideAllPrimitives();
		createHeader();
		
		lang.newText(new Offset(5, 30, "header", "SW"), "Zusammenfassung:" , "subHeader", null, secondHeaderProbs);
		
		SourceCode summary = lang.newSourceCode(new Offset(5,0,"subHeader", "SW"), "summary", null, scProbs);
		// Zusammenfassung wird hinzugefügt
		if (lenWindow-1 > 1)
			summary.addCodeLine("- Es wurden " + (lenWindow -1)+ " von "+ (allDataSet.length - 1) + " Einträgen verwendet", null, 0, null);
		else
			summary.addCodeLine("- Es wurde " + (lenWindow -1) + " von "+ (allDataSet.length - 1) + " Eintrag verwendet", null, 0, null);
		summary.addCodeLine("", null, 0, null);
		
		summary.addCodeLine("- Der Klassifizierer hat " + (classifier.getClassifier().size() - 1) + " Regel/n", null, 0, null);
		
		summary.addCodeLine("", null, 0, null);
		
		summary.addCodeLine("- Folgender Klassifizierer wurde erzugt:", null, 0, null);
		
		makeClassifier(trainDataTable, new Offset(5, 10, summary, "SW"));
		
		lang.newRect(new Offset(-5, -5, SaC, "NW"), new Offset(5, 5, SaC, "SE"),"rec" + numWindow+1, null);
		
		lang.nextStep("Fazit");
		lang.finalizeGeneration();

	}
	
	
	private void SepAndCon(StringMatrix trainDataTable)
	{
		// Separate-and-Conquer Algorithmus
		int allPos = 0, classPos = 0;
		classifier = new Classifiers();
		String[][] matrix = stringMatrixToArrayMatrix(trainDataTable);
		for(int j = 0; j < matrix.length; j++)
			if(matrix[j][numColumn-1].equals("Ja"))
				allPos++;
		if(allPos == 0)
			return;
		allPos = 0;
		
		do
		{
			String[][] origMatrix = matrix;
			
			
			LinkedList<Elements> trainList = createDataList(matrix);
			Elements bestElem = findBestElem(trainList);
			
			classifier.addElemToRule(bestElem);
			while (bestElem.getNeg() != 0 )
			{
				int numRows= 0;
				
				for(int j = 0; j < matrix.length; j++)
					if(matrix[j][bestElem.getX()].equals(bestElem.getName()))
						numRows++;
				
				String[][] tempMatrix = new String[numRows][numColumn];
				
				int position = 0;
				for(int j = 0; j < matrix.length; j++)
				{
					boolean wirte = false;
					for(int k = 0; k < matrix[j].length; k++)
					{
						if (matrix[j][k] != null)
							if(matrix[j][bestElem.getX()].equals(bestElem.getName()) && !matrix[j][k].equals(bestElem.getName()))
							{
								wirte = true;
								tempMatrix[position][k] = matrix[j][k];
							}
					}
					if(wirte)
						position++;
				}
				matrix = tempMatrix;
				trainList = createDataList(matrix);
				if(trainList.isEmpty())
					break;
				else
					bestElem = findBestElem(trainList);
				classifier.addElemToRule(bestElem);	
				
			}
			classifier.completeRule();
			
			LinkedList<Integer> position = new LinkedList<Integer>();
			allPos = 0;
			classPos = 0;
			
			for(int j = 0; j < origMatrix.length; j++)
				if(origMatrix[j][numColumn-1].equals("Ja"))
					allPos++;
		
			
			for(int k = 0; k < origMatrix.length; k++)
			{
				for(int l = 0; l < classifier.getNumRule(); l++)
				{
					boolean ruleCovered = true;
					for(int m = 0; m < classifier.getLenRule(l); m++)
					{
						boolean elemCover = false;
						for(int n = 0; n < numColumn -1; n++)
						{
							if(classifier.getElemOfRule(l, m).getName().equals(origMatrix[k][n]))
							{
								elemCover = true;
							}
								
						}
						if(!elemCover)
						{
							ruleCovered = false;	
							break;
						}
						
					}
					if(!ruleCovered)
						position.add(k);
					else
						if(origMatrix[k][numColumn-1].equals("Ja"))
							classPos++;
					}
				}
			if (allPos != classPos)
			{ 
				matrix = new String[position.size()][numColumn];
				int l = 0;
				for(int j : position)
				{ 
					if (l < matrix.length)
					{
						for(int k = 0; k < numColumn; k++)
							matrix[l][k] = origMatrix[j][k];
						l++;
					}
				}
			}
		}while(allPos != classPos);
	}
	
	private String[][] stringMatrixToArrayMatrix(StringMatrix m)
	{
		// stringMatrix wird zu ToArrayMatrix
		String[][] sa = new String[m.getNrRows()-1][numColumn];
		
		for(int i = 1; i < m.getNrRows(); i++)
		{
			for(int j = 0; j < numColumn; j++)
			{
				sa[i-1][j] = m.getElement(i, j);
			}
		}
		
		return sa;
	}
	
	private Elements findBestElem(LinkedList<Elements> trainDataList) 
	{
		// Suche das Element mit der höchsten Heuristik 
		boolean best = true;
		Elements bestElem;
		do
		{
			bestElem = trainDataList.getFirst();;
			for (Elements e : trainDataList)
			{
				if(e.getHeuristic() > bestElem.getHeuristic())
					bestElem = e;
				else if(e.getHeuristic() == bestElem.getHeuristic())
				{
					if(e.getPos() > bestElem.getPos())
						bestElem = e;
				}
			}
		}while(best == false);
		return bestElem;
	}
	
	private LinkedList<Elements> createDataList(String[][] trainDataTable)
	{
		// Erstelle eine Liste mit "Elements" und füge alle Elemente von trainDataTable hinzu (ohne Dopplungen)
		LinkedList<Elements> trainDataList = new LinkedList<Elements>();
		
		boolean exist = false;
		for (int j = 0; j < trainDataTable[0].length-1; j++)
		{
			for(int k = 0; k < trainDataTable.length; k++)
			{
				for(Elements e : trainDataList )
				{	
					if (e.getName().equals(trainDataTable[k][j]))
						exist = true;
				}
				if (!exist && trainDataTable[k][j] != null)
					 trainDataList.add(new Elements(trainDataTable[k][j], j, k));
				else 
					exist = false;
			}
		}
		for (Elements e : trainDataList)
		{
			int pos = 0;
			int neg = 0;
			
			for (int j = 0; j < trainDataTable[0].length-1; j++)
			{
				for(int k = 0; k < trainDataTable.length; k++)
				{
					if(trainDataTable[k][j] != null)
						if (trainDataTable[k][j].equals(e.getName()))
						{
							if ( trainDataTable[k][trainDataTable[0].length-1].equals("Nein"))
								neg++;
							else
								pos++;
						}
				}
			}
			e.setPosAndNeg(pos, neg);
			if(pos != 0)
				e.setheuristic((double) pos/(pos+neg));
			else
				e.setheuristic(0.0);
		}
		return trainDataList;
	}
	private void visualSepAndCon()
	{
		// Separate-and-Conquer Algorithmus visualisiert
		
		sc.highlight(1);
		sc.unhighlight(3);
		sc.unhighlight(0);
		numHeuristicTable++;
		lang.addLine("line \"SaCLine\" offset (85, 0) from \"windowSet"+ numWindow+"\" NE offset (85, 150) from \"windowSet"+ numWindow + "\" SE");
		Text headerSAC = lang.newText(new Offset(35, 0, "SaCLine", "NE"), "Separate-and-Conquer:" , "headerSAC" , null, secondHeaderProbs);
		StringMatrix trainTable = lang.newStringMatrix(new Offset(5, 30, "headerSAC", "SW"), createTable(lenWindow), "TrainSet" + numHeuristicTable, null, tableProbs);
		
		
		
		
		Text headerTD = lang.newText(new Offset(5, -20, trainTable, "NW"), "Trainingsdatensatz:" , "headerTD", null, secondHeaderProbs);
		for(int l = 0; l < lenWindow-1; l++)
		{
			for (int k = 0 ; k < numColumn; k++)
			{
				//TODO testDataSets ändern in array
				trainTable.put(l+1, k, allDataSet[windowDataSet[l]][k], null, null);
				
			}
			
		}
		for (int k = 0; k < numColumn; k++)
			trainTable.setGridFont(0, k, new Font("SansSerif", Font.BOLD, 14), null, null);
		
		int allPos = 0, classPos = 0;
		classifier = new Classifiers();
		String[][] matrix = stringMatrixToArrayMatrix(trainDataTable);
		for(int j = 0; j < matrix.length; j++)
			if(matrix[j][numColumn-1].equals("Ja"))
				allPos++;
		if(allPos == 0)
			return;
		allPos = 0;
		
		StringMatrix HeuristicTable = null;
		
		Text comHeuristic = null;
		Text erkHeader = null;
		SourceCode erk = null;
		Text regelheader = lang.newText(new Offset(0, 30, trainTable, "SW"), "Regelmenge:", "regeheader", null, secondHeaderProbs);
		SourceCode rules = lang.newSourceCode(new Offset(0, 2, regelheader, "SW"), "description", null, scProbs);
		Text headerCon = null;
		do
		{
			String[][] origMatrix = matrix;
			if (HeuristicTable != null)
				HeuristicTable.hide();
			numHeuristicTable++;
			if (trainTable != null)
				trainTable.hide();
			
			LinkedList<Elements> tempList = createDataList(matrix);
			
			String[][] table = new String[tempList.size()+1][5];
			table[0][0] = "Attribut";
			table[0][1] = "Wert";
			table[0][2] = "Pos";
			table[0][3] = "Neg";
			table[0][4] = "Heuristik";
			
			
			
			HeuristicTable = lang.newStringMatrix(new Offset(100, 0, trainTable, "NE"), table, "HeuristicSet"+numHeuristicTable, null, tableProbs);
			
			if(headerCon != null)
				headerCon.hide();
			headerCon = lang.newText(new Offset(5, -20, HeuristicTable, "NW"), "Conquered-Daten:" , "headerTD", null, secondHeaderProbs);
			
			if (comHeuristic != null)
				comHeuristic.hide();
			
			String[][] newTrainMatrix = createTable(matrix.length+1);
			for (int i = 0; i < matrix.length; i++)
			{
				for(int j = 0; j < matrix[i].length; j++)
				{
					newTrainMatrix[i+1][j] =  matrix[i][j];
				}
			}
			
			trainTable = lang.newStringMatrix(new Offset(5, 30, "headerSAC", "SW"), newTrainMatrix, "TrainSet", null, tableProbs);
			
			for (int k = 0; k < 5; k++)
				HeuristicTable.setGridFont(0, k, new Font("SansSerif", Font.BOLD, 14), null, null);
			for (int k = 0; k < numColumn; k++)
				trainTable.setGridFont(0, k, new Font("SansSerif", Font.BOLD, 14), null, null);
			
			LinkedList<Elements> trainList = visualCreateDataList(matrix, HeuristicTable);
			Elements bestElem = findBestElem(trainList);
			for (int k = 0 ; k < HeuristicTable.getNrRows(); k++)
			{
				if(HeuristicTable.getElement(k, 1).equals(bestElem.getName()))
				{
					for( int i = 0; i < 5; i++)
						HeuristicTable.highlightCell(k, i, null, null);
					break;
				}
			}
			
			comHeuristic = lang.newText(new Offset(50, 5, "HeuristicSet"+numHeuristicTable, "NE"), "// Heuristik = (#Pos + #Neg)/#Pos", "comHeuristik", null, normalTextProbs);
			if(erk != null)
				erk.hide();
			
			if(erkHeader != null)
				erkHeader.hide();
			
			erkHeader = lang.newText(new Offset(0, 5, comHeuristic, "SW"), "Erklärung:", "erkl", null, secondHeaderProbs);
			erk = lang.newSourceCode(new Offset(0, 2, erkHeader, "SW"), "description", null, scProbs);
			erk.addCodeLine("Wähle den Attributwert mit der höchsten Heuristik aus (bei mehrfachen Vorkommen der höchsten Heuristik den ersten Attributwert nehmen) und prüfe, ob negative Bespiele abgedeckt werden.", null, 0, null);
			lang.nextStep();
			classifier.addElemToRule(bestElem);
			String temp = classifier.getElemOfRule(classifier.getNumRule()-1, 0).getName();
			if (bestElem.getNeg() == 0)
			{
				erk.addCodeLine("Es werden keine negativen Beispiele abgedeckt.", null, 0, null);
				for(int k = 1; k < classifier.getLenRule(classifier.getNumRule()-1); k++)
				{
					temp = temp + " und " + trainDataTable.getElement(0, classifier.getElemOfRule(classifier.getNumRule()-1, k).getX()) + " = " + classifier.getElemOfRule(classifier.getNumRule()-1, k).getName();
				}
				erk.addCodeLine("Füge " +  trainDataTable.getElement(0, classifier.getRule(classifier.getNumRule()-1).getFirst().getX()) + " = " + temp + " in die Regelmenge hinzu und prüfe ob alle positiven Beispiele abgedeckt sind.", null, 0, null);
				rules.addCodeLine ( trainDataTable.getElement(0, classifier.getRule(classifier.getNumRule()-1).getFirst().getX()) + " = " + temp + " -> Ja", null, 0, null);
				lang.nextStep();
			}
			else
				erk.addCodeLine("Es werden negative Beispiele abgedeckt. Filtere die Trainingsdaten nach der Bedingung " + trainDataTable.getElement(0, classifier.getRule(0).getFirst().getX()) + " = " + temp + " und suche nach einer Verfeinerung der Regel.", null, 0, null);
			while (bestElem.getNeg() != 0 )
			{
				lang.nextStep();
				int numRows= 0;
				
				for(int j = 0; j < matrix.length; j++)
					if(matrix[j][bestElem.getX()].equals(bestElem.getName()))
						numRows++;
				
				String[][] tempMatrix = new String[numRows][numColumn];
				String[][] newTMatrix = createTable(numRows+1);
				
				int position = 0;
				for(int j = 0; j < matrix.length; j++)
				{
					boolean wirte = false;
					for(int k = 0; k < matrix[j].length; k++)
					{
						if (matrix[j][k] != null)
						{
							if(matrix[j][bestElem.getX()].equals(bestElem.getName()) && !matrix[j][k].equals(bestElem.getName()))
							{
								wirte = true;
								tempMatrix[position][k] = matrix[j][k];
								newTMatrix[position+1][k] = matrix[j][k];
							}
							else
								if(matrix[j][bestElem.getX()].equals(bestElem.getName()) && matrix[j][k].equals(bestElem.getName()))
								{
									newTMatrix[position+1][k] = matrix[j][k];
								}
						}
							
					}
					if(wirte)
						position++;
				}
				
				matrix = tempMatrix;
				
				if (trainTable != null)
					trainTable.hide();
				trainTable = lang.newStringMatrix(new Offset(5, 30, "headerSAC", "SW"), newTMatrix, "TrainSet", null, tableProbs);
				if (HeuristicTable != null)
					HeuristicTable.hide();
				for (int k = 0; k < numColumn; k++)
					trainTable.setGridFont(0, k, new Font("SansSerif", Font.BOLD, 14), null, null);
				tempList = createDataList(matrix);
				table = new String[tempList.size()+1][5];
				table[0][0] = "Attribut";
				table[0][1] = "Wert";
				table[0][2] = "Pos";
				table[0][3] = "Neg";
				table[0][4] = "Heuristik";
				
				numHeuristicTable++;
				HeuristicTable = lang.newStringMatrix(new Offset(100, 0, trainTable, "NE"), table, "HeuristicSet"+numHeuristicTable, null, tableProbs);
				for (int k = 0; k < 5; k++)
					HeuristicTable.setGridFont(0, k, new Font("SansSerif", Font.BOLD, 14), null, null);
				if (comHeuristic != null)
					comHeuristic.hide();
				
				trainList = visualCreateDataList(matrix, HeuristicTable);
				
				
				if(trainList.isEmpty())
					break;
				else
					bestElem = findBestElem(trainList);
				
				for (int k = 0 ; k < HeuristicTable.getNrRows(); k++)
				{
					if(HeuristicTable.getElement(k, 1).equals(bestElem.getName()))
					{
						for( int i = 0; i < 5; i++)
							HeuristicTable.highlightCell(k, i, null, null);
						break;
					}
				}
				
				if(erk != null)
					erk.hide();
				
				if(erkHeader != null)
					erkHeader.hide();
				
				comHeuristic = lang.newText(new Offset(50, 5, HeuristicTable, "NE"), "// Heuristik = (#Pos + #Neg)/#Pos", "comHeuristik", null, normalTextProbs);
				erkHeader = lang.newText(new Offset(0, 5, comHeuristic, "SW"), "Erklärung:", "erkl", null, secondHeaderProbs);
				erk = lang.newSourceCode(new Offset(0, 2, erkHeader, "SW"), "description", null, scProbs);
				erk.addCodeLine("Wähle den Attributwert mit der höchsten Heuristik aus (bei mehrfachen Vorkommen der höchsten Heuristik den ersten Attributwert nehmen) und prüfe, ob negative Bespiele abgedeckt werden.", null, 0, null);
				lang.nextStep();
				classifier.addElemToRule(bestElem);	
				temp = classifier.getElemOfRule(classifier.getNumRule()-1, 0).getName();
				if (bestElem.getNeg() == 0)
				{
					erk.addCodeLine("Es werden keine negativen Beispiele abgedeckt.", null, 0, null);
					for(int k = 1; k < classifier.getLenRule(classifier.getNumRule()-1); k++)
					{
						temp = temp + " und " + trainDataTable.getElement(0, classifier.getElemOfRule(classifier.getNumRule()-1, k).getX()) + " = " + classifier.getElemOfRule(classifier.getNumRule()-1, k).getName();
					}
					erk.addCodeLine("Füge " +  trainDataTable.getElement(0, classifier.getRule(classifier.getNumRule()-1).getFirst().getX()) + " = " + temp + " in die Regelmenge hinzu und prüfe ob alle positiven Beispiele abgedeckt sind.", null, 0, null);
					rules.addCodeLine ( trainDataTable.getElement(0, classifier.getRule(classifier.getNumRule()-1).getFirst().getX()) + " = " + temp + " -> Ja", null, 0, null);
					lang.nextStep();
				}
				else
					erk.addCodeLine("Es werden negative Beispiele abgedeckt. Filtere die Trainingsdaten nach der Bedingung " + trainDataTable.getElement(0, classifier.getRule(0).getFirst().getX()) + " = " + temp + " und suche nach einer Verfeinerung der Regel.", null, 0, null);
				
				
			}
			classifier.completeRule();
			
			LinkedList<Integer> position = new LinkedList<Integer>();
			allPos = 0;
			classPos = 0;
			
			for(int j = 0; j < origMatrix.length; j++)
				if(origMatrix[j][numColumn-1].equals("Ja"))
					allPos++;
		
			
			for(int k = 0; k < origMatrix.length; k++)
			{
				for(int l = 0; l < classifier.getNumRule(); l++)
				{
					boolean ruleCovered = true;
					for(int m = 0; m < classifier.getLenRule(l); m++)
					{
						boolean elemCover = false;
						for(int n = 0; n < numColumn -1; n++)
						{
							if(classifier.getElemOfRule(l, m).getName().equals(origMatrix[k][n]))
							{
								elemCover = true;
							}
								
						}
						if(!elemCover)
						{
							ruleCovered = false;	
							break;
						}
						
					}
					if(!ruleCovered)
						position.add(k);
					else
						if(origMatrix[k][numColumn-1].equals("Ja"))
							classPos++;
					}
				}
			if (allPos != classPos)
			{ 
				matrix = new String[position.size()][numColumn];
				int l = 0;
				for(int j : position)
				{ 
					if (l < matrix.length)
					{
						for(int k = 0; k < numColumn; k++)
							matrix[l][k] = origMatrix[j][k];
						l++;
					}
				}
			}
			if(allPos == classPos)
				erk.addCodeLine("Es werden alle positiven Beispiele abgedeckt, somit ist unsere Regelmenge vollständig.", null, 0, null);
			else 
				erk.addCodeLine("Es werden nicht alle positiven Beispiele abgedeckt. Entferne alle Einträge in den Trainingsdaten, die mit der aktuellen Regelmenge abgedeckt sind und suche erneut nach einer Regel.", null, 0, null);
			lang.nextStep();
			
		}while(allPos != classPos);
		
		
		lang.addLine("hideCode \"SaCLine\"");
		trainTable.hide();
		HeuristicTable.hide();
		erk.hide();
		erkHeader.hide();
		comHeuristic.hide();
		rules.hide();
		regelheader.hide();
		headerSAC.hide();
		headerTD.hide();
		headerCon.hide();
	}
	
	private LinkedList<Elements> visualCreateDataList(String[][] trainDataTable, StringMatrix mat)
	{
		// Erstelle eine Liste mit "Elements" und füge alle Elemente von trainDataTable hinzu (ohne Dopplungen)
		LinkedList<Elements> trainDataList = new LinkedList<Elements>();
		
		boolean exist = false;
		for (int j = 0; j < trainDataTable[0].length-1; j++)
		{
			for(int k = 0; k < trainDataTable.length; k++)
			{
				for(Elements e : trainDataList )
				{	
					if (e.getName().equals(trainDataTable[k][j]))
						exist = true;
				}
				if (!exist && trainDataTable[k][j] != null)
					 trainDataList.add(new Elements(trainDataTable[k][j], j, k));
				else 
					exist = false;
			}
		}
		int i = 1;
		for (Elements e : trainDataList)
		{
			int pos = 0;
			int neg = 0;
			
			for (int j = 0; j < trainDataTable[0].length-1; j++)
			{
				for(int k = 0; k < trainDataTable.length; k++)
				{
					if(trainDataTable[k][j] != null)
						if (trainDataTable[k][j].equals(e.getName()))
						{
							if ( trainDataTable[k][trainDataTable[0].length-1].equals("Nein"))
								neg++;
							else
								pos++;
						}
				}
			}
			e.setPosAndNeg(pos, neg);
			if(pos != 0)
				e.setheuristic((double) pos/(pos+neg));
			else
				e.setheuristic(0.0);
			
			for(int j = 0; j < trainDataTable.length; j++)
			{
				for(int k = 0; k < trainDataTable[j].length; k++)
				{
					if(trainDataTable[j][k] != null)
					{
						if(trainDataTable[j][k].equals(e.getName()))
						{
							mat.put(i, 0, allDataSet[0][k], null, null);
							break;
						}
					}
				}
			}
			mat.put(i, 1, e.getName(), null, null);
			mat.put(i, 2, Integer.toString(pos), null, null);
			mat.put(i, 3, Integer.toString(neg), null, null);
			mat.put(i, 4, Double.toString(e.getHeuristic()), null, null);
			i++;
			
		}
		return trainDataList;
	}
			
}
