package generators.misc.helpersBagging;

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
public class Bagging {
	
	private Language lang;
	private static String[][] allDataSet;
	private static String[][] testDataSet;
	private static int numColumn; 
	private static int lenTestDataset;
	private SourceCodeProperties scProbs;
	private SourceCodeProperties cProbs;
	private TextProperties secondHeaderProbs;
	private TextProperties normalTextProbs;
	private MatrixProperties tableProbs;
	private SourceCode sc;
	private static int lenTrainset;
	private static int numTrainset;
	private StringMatrix dataTable;
	private static Classifiers[] classifiers;
	StringMatrix testDataTable;
	SourceCode[] SaC;
	private static Integer[] numPos;
	private static String predicted_class;
	private static String[] header;
	
	public static void main(Language lang, int lenTestData, int lenTrain, int numTrain, int numCol, String[] headers, String[][]data, String[][]testData, int[][] testDataSets, SourceCodeProperties sourceCodeProb, MatrixProperties tabellenProb, SourceCodeProperties klassifiziererProb) 
	{
		Language l = lang;
		
		Bagging b = new Bagging(l);
		
		lenTestDataset = lenTestData;
		lenTrainset = lenTrain;
		numTrainset = numTrain;
		numColumn = numCol;
		
		b.discription();
		
		header = headers;
		
		allDataSet = data;
		
		testDataSet= testData;
		b.createProperties(sourceCodeProb, tabellenProb, klassifiziererProb);
		b.initBagging();
		b.createTrainingsSets(testDataSets);
		b.checkTestData();
		b.ClassificateWOBagging();
		b.summary();
		
	}
	
	public Bagging(Language l) 
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
		// Überschrift "Bagging" wird hinzugefügt
		TextProperties headerProbs = new TextProperties();
		headerProbs.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		headerProbs.set(AnimationPropertiesKeys.FONT_PROPERTY,  new Font("SansSerif", Font.BOLD, 30));
		
		Text header = lang.newText(new Coordinates(20, 20), "Bagging [DE]", "header", null, headerProbs);
		
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
		discrp.addCodeLine("Der Bragging-Algorithmus ermöglicht die Kombination der Ergebnisse von mehreren Klassifizierungsmodellen, sodass präzisere Vorhersagen getroffen werden können.", null, 0, null);
		discrp.addCodeLine("", null, 0, null);
		discrp.addCodeLine("Umgesetzt wird dies indem m Trainingssätze (auch genannt bootstrap) mit x Fällen aus dem Datensatz durch zufälliges Ziehen mit zurücklegen gebildet werden. Auf jeden Trainingssatz wird ein eigener Klassifizierer trainiert.", null, 0, null);
		discrp.addCodeLine("", null, 0, null);
		discrp.addCodeLine("Nun wird für eine Vorhersage die Ergebnisse aller m Klassifizier betracht, d.h. es werden m Vorhersagen gebildet. Die meist vorhergesagte Klasse ist die wahrscheinlichste Vorhersage und damit die Vorhersage des Algorithmus (bei unentschieden ist die Vorhersage 'Nein').", null, 0, null);
		discrp.addCodeLine("", null, 0, null);
		discrp.addCodeLine("Als Klassifizierer kann man ein beliebigen Algorithmus wählen. Hier wird der Separate-and-Conquer Algorithmus in der Variante des Top-Down Hill Climbing verwendet. Dabei wird ein Datensatz positiv klassifiziert, wenn es mehr erfüllte als unerfüllte Regeln vorhanden sind. ", null, 0, null);
		discrp.addCodeLine("", null, 0, null);
		discrp.addCodeLine("Dieser Algorithmus ist im Animal als eigenständiger Algorithmus näher erläutert.", null, 0, null);
		lang.nextStep("Beschreibung");
	}
	private void createProperties(SourceCodeProperties sourceCodeProb, MatrixProperties tabellenProb, SourceCodeProperties klassifiziererProb)
	{
		// Alle benötigten Properties
		scProbs = sourceCodeProb;
		tableProbs = tabellenProb;
		cProbs = klassifiziererProb;
		/*scProbs = new SourceCodeProperties();
		scProbs.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		scProbs.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProbs.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
		scProbs.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		scProbs.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));
		
		cProbs = new SourceCodeProperties();
		cProbs.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		cProbs.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.GREEN);
		cProbs.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
		cProbs.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		cProbs.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));*/
		
		secondHeaderProbs = new TextProperties();
		secondHeaderProbs.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		secondHeaderProbs.set(AnimationPropertiesKeys.FONT_PROPERTY,  new Font("SansSerif", Font.BOLD, 14));
		
		normalTextProbs = new TextProperties();
		normalTextProbs.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		normalTextProbs.set(AnimationPropertiesKeys.FONT_PROPERTY,  new Font("SansSerif", Font.PLAIN, 14));
		
		/*tableProbs = new  MatrixProperties();
		tableProbs.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		tableProbs.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		tableProbs.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		tableProbs.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		tableProbs.set(AnimationPropertiesKeys.GRID_BORDER_COLOR_PROPERTY, Color.BLACK);
		tableProbs.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, new Color(255, 0, 0));
		tableProbs.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(192, 192, 192));
		tableProbs.set(AnimationPropertiesKeys.GRID_HIGHLIGHT_BORDER_COLOR_PROPERTY, Color.BLACK);
		tableProbs.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));
		tableProbs.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
		tableProbs.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);*/
	}
	private void initBagging()
	{
		lang.hideAllPrimitives();
		createHeader();
		
		// SorceCode wird hinzugeügt
		sc = lang.newSourceCode(new Offset(4,25,"headlinebg", "SW"), "listSource", null, scProbs);
		sc.addCodeLine("1. for m=1 to 'Anzahl der Trainingssätze'", null, 0, null);
		sc.addCodeLine("a) fülle den Trainingssatz mit x Daten (zufälliges Auswählen mit Wiederholungen)", null, 1, null);
		sc.addCodeLine("b) lerne auf den Trainingssatz ein Klassifizierer", null, 1, null);
		sc.addCodeLine("2. for m=1 to 'Anzahl der zu klassifizierenden Daten'", null, 0, null);
		sc.addCodeLine("a) klassifiziere den Eintrag mit allen Klassifizierern", null, 1, null);
		sc.addCodeLine("b) klassifiziere die Klasse die am meisten vorhergesagt wurde", null, 1, null);
		
		lang.newText(new Offset(5, -20, sc, "NW"), "Algorithmus", "headerlistSource", null, normalTextProbs);
		
		lang.newText(new Offset(100, 0, sc, "NE"), "// Hier: 'Anzahl der Trainingssätze' = " + numTrainset, "info", null, normalTextProbs);
		lang.newText(new Offset(100, 20, sc, "NE"), "// Hier: x = " + (lenTrainset - 1), "info", null, normalTextProbs);
		lang.newText(new Offset(100, 50, sc, "NE"), "// Hier: 'Anzahl der zu klassifizierenden Daten' = " + (lenTestDataset-1), "info", null, normalTextProbs);
		
		// Datensatz wird hinzugeügt
		dataTable = lang.newStringMatrix(new Offset(4, 55, sc, "SW"), allDataSet, "dataset", null, tableProbs);
		for (int i = 0; i < numColumn; i++)
			dataTable.setGridFont(0, i, new Font("SansSerif", Font.BOLD, 14), null, null);		
		
		lang.newText(new Offset(5, -20, dataTable, "NW"), "Datensatz:", "headerDataset", null, secondHeaderProbs);
		
		// Testdatensatz wird hinzugeügt
		testDataTable = lang.newStringMatrix(new Offset(0, 50, dataTable, "SW"), testDataSet, "testdataset", null, tableProbs);
		for (int i = 0; i < numColumn; i++)
			testDataTable.setGridFont(0, i, new Font("SansSerif", Font.BOLD, 14), null, null);
		
		lang.newText(new Offset(5, -20, testDataTable, "NW"), "Testdatensatz:", "headerTestDataset", null, secondHeaderProbs);
		
		sc.highlight(0);
		
		lang.nextStep("Initialisierung");
	}
	
	public void createTrainingsSets(int[][] testDataSets)
	{
		// erstellt und befüllt die Trainssets
		sc.unhighlight(0);
		sc.highlight(1);
		
		lang.addLine("line \"line\" offset (85, 0) from \"dataset\" NE offset (85, 150) from \"dataset\" SE");
		
		classifiers = new Classifiers[numTrainset +1];
		SaC = new SourceCode[numTrainset+1];
	
		for (int i = 1; i <= numTrainset; i++)
		{
			sc.unhighlight(2);
			sc.highlight(1);
			StringMatrix trainDataTable;
			if (i ==1)
				trainDataTable = lang.newStringMatrix(new Offset(35, 0, "line", "NE"), createTable(lenTrainset), "trainset" + i, null, tableProbs);
			else
				trainDataTable = lang.newStringMatrix(new Offset(100, 0, "trainset" + (i-1), "NE"), createTable(lenTrainset), "trainset" + i, null, tableProbs);
			
			for (int j = 0; j < numColumn; j++)
				trainDataTable.setGridFont(0, j, new Font("SansSerif", Font.BOLD, 14), null, null);
			lang.newText(new Offset(5, -20, trainDataTable, "NW"), "Trainingssatz " + i + ":", "headerT" + i, null, secondHeaderProbs);
			for(int l = 0; l < lenTrainset-1; l++)
			{
				for (int k = 0 ; k < numColumn; k++)
				{
					trainDataTable.put(l+1, k, allDataSet[testDataSets[l][i-1]][k], null, null);
					dataTable.highlightCell(testDataSets[l][i-1], k, null, null);
				}
				
				lang.nextStep();
				
				for (int k = 0 ; k < numColumn; k++)
				{
					dataTable.unhighlightCell(testDataSets[l][i-1], k, null, null);
				}
				
			}
			createClassifier(i, trainDataTable);
			lang.nextStep("Erzeuge Klassifizierer " + i);
		}
	}
	public void createClassifier(int numClassifier, StringMatrix trainDataTable)
	{
		// erstellt ein Kassifizierer für die Trainsets
		sc.unhighlight(1);
		sc.highlight(2);
		
				
		
		lang.addLine("Line \"line"+ numClassifier + "\" offset (0,0) from \"trainset"+ numClassifier +"\" S offset (0, 55) from \"trainset"+ numClassifier + "\" S fwArrow");
		lang.newText(new Offset(-180, 10, "line" + numClassifier, "NW"), "Separate and Conquer", "SaC", null, normalTextProbs);
		
		makeClassifier(numClassifier, trainDataTable, new Offset(-100,20,"line" + numClassifier , "SW"));
		
		lang.newRect(new Offset(-5, -5, SaC[numClassifier-1], "NW"), new Offset(5, 5, SaC[numClassifier-1], "SE"),"rec" + numClassifier, null);
		lang.newText(new Offset(5, -20, "rec" + numClassifier, "NW"), "Klassifizierer " + numClassifier+ ":", "header", null, secondHeaderProbs);
	}
	
	public void makeClassifier(int numClassifier, StringMatrix trainDataTable, Node position)
	{
		// erstellt ein Kassifizierer
		SepAndCon(numClassifier,trainDataTable);
		
		SaC[numClassifier-1] = lang.newSourceCode(position, "SaC" + numClassifier, null, cProbs);
		if(classifiers[numClassifier-1].getNumRule() == 0)
		{
			SaC[numClassifier-1].addCodeLine("1. false -> Ja", null, 0, null);
		}
		for(int j = 0 ; j < classifiers[numClassifier-1].getNumRule();j++)
		{
			String temp = classifiers[numClassifier-1].getElemOfRule(j, 0).getName();
			for(int k = 1; k < classifiers[numClassifier-1].getLenRule(j); k++)
			{
				temp = temp + " und " + classifiers[numClassifier-1].getElemOfRule(j, k).getName();
			}
			SaC[numClassifier-1].addCodeLine(j+1 + ". " + trainDataTable.getElement(0, classifiers[numClassifier-1].getRule(j).getFirst().getX()) + " = " + temp + " -> Ja", null, 0, null);
		}
		
		
	}
	
	private void checkTestData()
	{
		// Kassifizeren der Testdaten
		sc.unhighlight(2);
		sc.highlight(4);
		
		numPos = new Integer[lenTestDataset-1];
		for(int i = 0; i < lenTestDataset-1; i++)
			numPos[i] = 0;
		
		StringMatrix[] mat = new StringMatrix[numTrainset];
		for(int j = 1 ;j < testDataSet.length; j++)
		{
			sc.unhighlight(5);
			sc.highlight(4);
			int numPosClass = 0;
		
			
			
			for(int i = 1; i <= numTrainset; i++)
			{
				int classPos = 0;
				for(int k= 0; k <numColumn; k++)
				{
					testDataTable.unhighlightCell(j-1, k, null, null);
					testDataTable.highlightCell(j, k, null, null);
				}
				String[][] tempMatrix = new String[1][numColumn];
				
				lang.addLine("Line \"line" + i + "\" offset (0,0) from \"rec"+ i + "\" S offset (0, 55) from \"rec"+ i + "\" S fwArrow");
				mat[i-1] = lang.newStringMatrix(new Offset(-150, 10, "line" + i, "SW"), createTable(2) , "dataClass" + i, null, tableProbs);
				
				for(int l = 0; l < classifiers[i-1].getClassifier().size() - 1 ; l++)
				{
					boolean ruleCovered = true;
					int m = 0;
					for(m = 0; m < classifiers[i-1].getLenRule(l); m++)
					{
						boolean elemCover = false;
						for(int n = 0; n < testDataSet[j].length -1; n++)
						{
							tempMatrix[0][n] = testDataSet[j][n];
							if(classifiers[i-1].getElemOfRule(l, m).getName().equals(tempMatrix[0][n]))
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
						SaC[i-1].highlight(l);
					}
				}
				
				
				
				for (int k = 0; k < numColumn; k++)
					mat[i-1].setGridFont(0, k, new Font("SansSerif", Font.BOLD, 14), null, null);
				
				int temp = classifiers[i-1].getClassifier().size() - 1;
				
				
				
				if(classPos > temp/2)
				{
					
					tempMatrix[0][numColumn-1] = "Ja";
					numPosClass++;
					numPos[j-1]++;
				}
				else
					tempMatrix[0][numColumn-1] = "Nein";
				
				for (int k = 0 ; k < numColumn; k++)
				{
					mat[i-1].put(1, k, tempMatrix[0][k], null, null);
				}
				mat[i-1].highlightElem(1, numColumn-1, null, null);
				
				lang.nextStep();
			}
			
			sc.unhighlight(4);
			sc.highlight(5);
			int pointYes = 0;
			int pointNo = 0;
			if(numPosClass > numTrainset/2)
				pointYes = 1;
			else
				pointNo = 1;
			
			if(j == 1)
			{
				MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel("classifi");
				
				question.setPrompt("Wird der erste Testeintrag mit ja oder Nein klassifiziert?");
				if( pointNo == 0)
				{
					question.addAnswer("Ja", pointYes, "Richtig!");
					question.addAnswer("Nein", pointNo, "Falsch!");
				}
				else
				{
					question.addAnswer("Ja", pointYes, "Falsch!");
					question.addAnswer("Nein", pointNo, "Richtig!");
				}
				
				lang.addMCQuestion(question);
				lang.nextStep();
			}
			if(pointYes == 1)
				testDataTable.put(j, numColumn-1, "Ja", null, null);
			else
				testDataTable.put(j, numColumn-1, "Nein", null, null);
			
			testDataTable.highlightElem(j, numColumn-1, null, null);
			lang.nextStep("Klassifiziere " + j+ ". Testeintrag");
			
			for(int k = 0; k < numTrainset; k++)
			{
				mat[k].hide();
				lang.addLine("hide \"line" + (k+1) +"\"");
			}
			for(int i = 1; i <= numTrainset; i++)
			{
				for(int k = 0; k < SaC[i-1].length(); k++)
				{
					SaC[i-1].unhighlight(k);
				}
			}
		}
		
		sc.unhighlight(5);
		
		for(int i = 0; i < numColumn; i++)
		{
			testDataTable.unhighlightCell(testDataTable.getNrRows()-1	, i, null, null);
		}
	}
	
	private void ClassificateWOBagging()
	{
		// Algorithmus ohne Bagging
		lang.addLine("line \"line\" offset (50, -5) from \"trainset" + numTrainset + "\" NE offset (50, 200) from \"trainset" + numTrainset + "\" SE");
		
		lang.newText(new Offset(30,0,"line","N"), "Klassifizierung der Daten direkt auf den Datensatzt ohne Bagging (Separate-and-Conquer direkt auf den Datensatz angewendet):", "orig", null, secondHeaderProbs);
		
		int len = classifiers.length;
		StringMatrix dataTable = lang.newStringMatrix(new Offset(30, 60, "orig", "NW"), allDataSet, "trainset" + len, null, tableProbs);
		
		lang.addLine("Line \"line"+ len + "\" offset (0,0) from \"trainset"+ len +"\" S offset (0, 55) from \"trainset"+ len + "\" S fwArrow");
		lang.newText(new Offset(-180, 10, "line" + len, "NW"), "Separate and Conquer", "SaC", null, normalTextProbs);
		
		for (int i = 0; i < numColumn; i++)
			dataTable.setGridFont(0, i, new Font("SansSerif", Font.BOLD, 14), null, null);
		
		lang.newText(new Offset(5, -20, dataTable, "NW"), "Datensatz:", "headerDataset", null, secondHeaderProbs);
		
		makeClassifier(len, dataTable, new Offset(-100,20,"line" + len , "SW"));
		
		lang.newRect(new Offset(-5, -5, SaC[len-1], "NW"), new Offset(5, 5, SaC[len-1], "SE"),"rec" + (len), null);
		lang.newText(new Offset(5, -20, "rec" + (len), "NW"), "original Klassifizierer:" , "header", null, secondHeaderProbs);
		
		lang.addLine("Line \"line" + len + "\" offset (0,0) from \"rec"+ len + "\" S offset (0, 55) from \"rec"+ len + "\" S fwArrow");
		StringMatrix mat = lang.newStringMatrix(new Offset(-150, 10, "line" + len, "SW"), createTable(testDataSet.length) , "dataClass", null, tableProbs);
		for(int j = 1 ;j < testDataSet.length; j++)
		{
			
				int classPos = 0;
				for(int k= 0; k <numColumn; k++)
				{
					testDataTable.unhighlightCell(j-1, k, null, null);
					testDataTable.highlightCell(j, k, null, null);
				}
				String[][] tempMatrix = new String[1][numColumn];
				
				for(int l = 0; l < classifiers[len-1].getClassifier().size() - 1 ; l++)
				{
					boolean ruleCovered = true;
					int m = 0;
					for(m = 0; m < classifiers[len-1].getLenRule(l); m++)
					{
						boolean elemCover = false;
						for(int n = 0; n < testDataSet[j].length -1; n++)
						{
							tempMatrix[0][n] = testDataSet[j][n];
							if(classifiers[len-1].getElemOfRule(l, m).getName().equals(tempMatrix[0][n]))
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
						SaC[len-1].highlight(l);
					}
				
				for (int i = 0; i < numColumn; i++)
					mat.setGridFont(0, i, new Font("SansSerif", Font.BOLD, 14), null, null);
				
				int temp = classifiers[len-1].getClassifier().size() - 1;
				if(classPos > temp/2)
				{
					tempMatrix[0][numColumn-1] = "Ja";
					predicted_class = "Ja";
				}
				else
				{
					tempMatrix[0][numColumn-1] = "Nein";
					predicted_class = "Nein";
				}
				for (int k = 0 ; k < numColumn; k++)
				{
					mat.put(j, k, tempMatrix[0][k], null, null);
				}
				mat.highlightElem(j, numColumn-1, null, null);
				
			}
			
			
			lang.nextStep("Klassifiziere " + j+ ". Testeintrag ohne Bagging");
			
			
			for(int k = 0; k < SaC[len-1].length(); k++)
			{
				SaC[len-1].unhighlight(k);
			}
		}
		
		
	}
	
	
	private void summary() 
	{
		lang.hideAllPrimitives();
		createHeader();
		
		lang.newText(new Offset(5, 30, "header", "SW"), "Zusammenfassung:" , "subHeader", null, secondHeaderProbs);
		
		SourceCode summary = lang.newSourceCode(new Offset(5,0,"subHeader", "SW"), "summary", null, scProbs);
		// Zusammenfassung wird hinzugefügt
		summary.addCodeLine("Es wurden " + numTrainset + " Klassifizierer erzeugt und verwendet statt nur 1 Klassifizierer", null, 0, null);
		summary.addCodeLine("", null, 0, null);
		
		for(int i = 1; i <= lenTestDataset-1; i++)
		{
			summary.addCodeLine("Beim " + i + ". Testbeispiel:", null, 0, null);
			for(int j = 1; j < classifiers.length; j++)
			{
				summary.addCodeLine("- hat der " + j +". Klassifizierer " + (classifiers[j-1].getClassifier().size() - 1) + " Regel/n", null, 1, null);
			}
			
			String Temp;
			if (numPos[i-1] > (classifiers.length-1)/2 )
				Temp = "Ja";
			else
				Temp = "Nein";
			testDataSet[i][numColumn-1] = Temp;
			summary.addCodeLine("- haben " + numPos[i-1] + " von " + (classifiers.length - 1) + " Klassifizierern 'Ja' vorhergesagt. Deswegen wird '" + Temp + "' klassifiziert", null, 1, null);
			summary.addCodeLine("Ohne Bagging würde das Testbeispiel mit '" + predicted_class +"' klassifiziert werden", null, 1, null);
			summary.addCodeLine("", null, 0, null);
		}
		
		String[][] tempMatrix = new String[lenTestDataset][numColumn + 1];
		
		for(int i = 0; i <= lenTestDataset-1; i++)
		{
			if(i != 0)
				tempMatrix[i][0] = i + ".";
			else
				tempMatrix[0][0] = "";
			for(int j = 1; j <= numColumn; j++)
			{
				tempMatrix[i][j] = testDataSet[i][j-1];
			}
		}
		
		StringMatrix dataTable = lang.newStringMatrix(new Offset(50, 0, summary, "NE"), tempMatrix , "dataClass", null, tableProbs);
		for (int i = 1; i <= numColumn; i++)
			dataTable.setGridFont(0, i, new Font("SansSerif", Font.BOLD, 14), null, null);
		
		for(int i = 1; i < lenTestDataset; i++)
			testDataSet[i][numColumn-1] = "?";
		lang.nextStep("Fazit");
		lang.finalizeGeneration();

	}
	
	
	private void SepAndCon(int i, StringMatrix trainDataTable)
	{
		// Separate-and-Conquer Algorithmus
		int allPos = 0, classPos = 0;
		classifiers[i-1] = new Classifiers();
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
			
			classifiers[i-1].addElemToRule(bestElem);
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
				classifiers[i-1].addElemToRule(bestElem);	
				
			}
			classifiers[i-1].completeRule();
			
			LinkedList<Integer> position = new LinkedList<Integer>();
			allPos = 0;
			classPos = 0;
			
			for(int j = 0; j < origMatrix.length; j++)
				if(origMatrix[j][numColumn-1].equals("Ja"))
					allPos++;
		
			
			for(int k = 0; k < origMatrix.length; k++)
			{
				for(int l = 0; l < classifiers[i-1].getNumRule(); l++)
				{
					boolean ruleCovered = true;
					for(int m = 0; m < classifiers[i-1].getLenRule(l); m++)
					{
						boolean elemCover = false;
						for(int n = 0; n < numColumn -1; n++)
						{
							if(classifiers[i-1].getElemOfRule(l, m).getName().equals(origMatrix[k][n]))
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
	
			
}
