package generators.searching.topk;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.network.helper.ClassName;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import translator.ResourceLocator;
import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.CodeView;
import algoanim.animalscript.addons.InfoBox;
import algoanim.animalscript.addons.Slide;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

	public class ThresholdAlgorithm implements ValidatingGenerator {
	  
	    private boolean TcolumnExsist = false;
		private Language lang;
		private String[][] inputRelation;
		private Text title;
		private SourceCode explanation;
		private SourceCode pseudocode;
		private Node belowTitle;
		private MatrixRepresentation listOfMaps;
		private MatrixRepresentation attributesSeenSoFar =null;
		private MatrixRepresentation TvalueList =null;
		private StringMatrix topK;
		private Map<String, Double>[] mapsArray;
		private int k;
		private Translator translator;
		private String resourceName;
		private Locale locale;
		private List<Primitive> toBeHidden;
		private Rect positioning; 
		private int highlightedLine;
		private double t;
		private double g;
		private Slide titleSlide;
		private double[][] arrayWithOnlyValues;
		private String[] firstColumn;
		
		private SourceCodeProperties srcProps;
		private TextProperties txtProps;
		private MatrixProperties matrixProps;
		private MultipleChoiceQuestionModel tValueQuestion = null;
		private MultipleChoiceQuestionModel gValueQuestion =null;
		private TextProperties h2Props;
		private SourceCodeProperties plainProps;
		private TwoValueView view;
		private TwoValueView view2;
		private CounterProperties counterProps;
		private MultipleChoiceQuestionModel savingQuestion =null;
		
		//questions
		//private static final String Question_finalResult ="What is the final result?";
		
		public ThresholdAlgorithm(String resourceName, Locale l) {
			this.resourceName = ClassName.getPackageAsPath(this) + resourceName; // "resources/FaginsAlgorithm"
			locale = l;
			translator = new Translator(this.resourceName, locale);
		}
		
	    public String getName() {
	        return "Threshold Algorithm";
	    }

	    public String getAlgorithmName() {
	        return "Threshold Algorithm";
	    }

	    public String getAnimationAuthor() {
	        return "Dominik Glenz, Johannes Nachtwey";
	    }
	    
	    public String getDescription() {
			return getFileContent(getResource("DESC", true));
		}

		public String getCodeExample() {
			return getFileContent(getResource("SOURCE", true));
		}
		
		/**
		 * Taken from generators.network.anim.Slide.java by Marc Werner
		 *  
		 * @author Marc Werner 
		 * @param fileName
		 * @return
		 */
		private static String getFileContent(String fileName) {
			StringBuilder fileContent = new StringBuilder();
			try {
				InputStream f = ResourceLocator.getResourceLocator().getResourceStream(fileName);
				BufferedReader br = new BufferedReader(new InputStreamReader(f));
				String thisLine = null;
				while ((thisLine = br.readLine()) != null) {
					thisLine = thisLine.concat("\n");
					fileContent.append(thisLine);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return fileContent.toString();
		}

	    public String getFileExtension(){
	        return "asu";
	    }

	    public Locale getContentLocale() {
	        return this.locale; 
	        }

	    public GeneratorType getGeneratorType() {
	        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
	    }

	    public String getOutputLanguage() {
	        return Generator.PSEUDO_CODE_OUTPUT;
	    }
		
	    public boolean validateInput(AnimationPropertiesContainer arg0,
				Hashtable<String, Object> primitives) throws IllegalArgumentException {
	    	
	    	t=1;
	    	g=0;
			inputRelation = (String[][]) primitives.get("Input Relation");
			k = (Integer) primitives.get("Top-k");
			
			if(k<1)
				throw new IllegalArgumentException(translator.translateMessage("WRONG_K"));
			
			try{
				// start: preparing the array for creating a matrix
				// first column contains only the row name(Typ String) inputRelation are
				// the values which the user have set or which are set by default
				firstColumn = new String[inputRelation.length];
				// contains only the values of the rows
				arrayWithOnlyValues = new double[inputRelation.length][inputRelation[0].length - 1];
				int i = 0;
				for (String[] row : inputRelation) {
					firstColumn[i] = row[0];
					for (int j = 0; j < row.length - 1; j++) {
						arrayWithOnlyValues[i][j] = Double.valueOf(row[j + 1]);
					}
					i++;
				}
				// end: start preparing the array for creating a matrix

			}catch(NumberFormatException e){
				throw new IllegalArgumentException(translator.translateMessage("WRONG_TABLE"));
			}catch (NullPointerException e) {
				throw new IllegalArgumentException(translator.translateMessage("WRONG_TABLE"));
			}
			
			
			srcProps=(SourceCodeProperties) arg0.getPropertiesByName("Source Code Properties");
			
			txtProps= (TextProperties) arg0.getPropertiesByName("Text Properties");
			txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(((Font) txtProps.get("font")).getFamily(),Font.BOLD,15));
			h2Props = (TextProperties) arg0.getPropertiesByName("Headline Properties");
			plainProps=(SourceCodeProperties) arg0.getPropertiesByName("Plain Text Properties");
			matrixProps=(MatrixProperties) arg0.getPropertiesByName("Matrix Properties");
			counterProps=(CounterProperties) arg0.getPropertiesByName("Counter Properties");
			
			return true;
		}
	    
	    /** generates AnimalScript for Threshold Algorithm 
	     * 
	     */
		public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {

			init();
			showExplanationOfThresholdAlgo();
			showMatrixTransformation();
			prepareThreshold();
			executeThreshold();
			showFinalSlide();
			
			
			lang.finalizeGeneration();
			return lang.toString().replace("elementColor", "textColor");
		}
		
		/** initialize AnimalScript 
		 * 
		 */
		public void init() {
			lang = new AnimalScript("Threshold Algorithm [EN]",
					"Johannes Nachtwey, Dominik Glenz", 800, 600);
			lang.setStepMode(true);
			this.highlightedLine = -1;
			translator = new Translator(resourceName, locale);
			toBeHidden = new ArrayList<Primitive>();
			//an diesem Rect erfolgt die Ausrichtung der anderen Objekte
			positioning = lang.newRect(new Coordinates(20, 20), new Coordinates(21,	21), "positioning", null);
			positioning.hide();
			
			lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION); 
			
		}
	    
		/** display the last final slide 
		 * 	The final slide contains some statistics
		 * 
		 */
		private void showFinalSlide() {
			lang.nextStep(translator.translateMessage("STEP_FOUR"));
			for (Primitive p : toBeHidden)
				p.hide();
			view.hide();
			view2.hide();
			
			new Slide(lang, getResource("FINAL", true), positioning.getName(),
					new SearchingStyle(h2Props,plainProps,srcProps,false), k,  attributesSeenSoFar.getNrRows(),inputRelation.length,
					listOfMaps.getCounter().getAccess(),listOfMaps.getNrElements());
		}

		/** contains steps to prepare the threshold algorithm
		 *  loads Pseudocode and explanation
		 *  initialize TvalueList and attributesAlreadySeen
		 */
		private void prepareThreshold() {
			
			pseudocode = CodeView.primitiveFromFile(lang,                             
					getResource("SOURCE", true), "pseudocode1", belowTitle, null,	new SearchingStyle(h2Props,plainProps,srcProps,true));

			toggleCodeHighlight(3);
			listOfMaps.moveTo(AnimalScript.DIRECTION_NE, "translate",	(Primitive) pseudocode, 50, 60);
			lang.nextStep();
			explanation = CodeView.primitiveFromFile(lang,
							getResource("EXPL", true), "explanation", new Offset(0, 50,
							pseudocode, AnimalScript.DIRECTION_SW), null,
							new SearchingStyle(h2Props,plainProps,srcProps,false));

			
			
			lang.nextStep();
			
			// COUNTER HINZUGEFÜGT
			String[] names = { translator.translateMessage("ASSIGNMENTS"),
					translator.translateMessage("ACCESSES") };

			view = lang.newCounterView(listOfMaps.getCounter(), new Offset(0, -60,
							listOfMaps.m, AnimalScript.DIRECTION_NW), counterProps, true,
							true, names);
			// BIS HIERHER
			
			//initialize TvalueList 
			TvalueList = new MatrixRepresentation(lang,	1,
						 new Offset(0, 0, listOfMaps.m,	AnimalScript.DIRECTION_NE), true,matrixProps,true);
			TvalueList.setHeader(0, "T-Value");
			
			//initialize attributesAlreadySeen
			attributesSeenSoFar = new MatrixRepresentation(lang,	listOfMaps.m.getNrCols() + 1,
							new Offset(0, 80, listOfMaps.m,	AnimalScript.DIRECTION_SW), true,matrixProps,false);
		
			view2 = lang.newCounterView(attributesSeenSoFar.getCounter(), new Offset(0,
					20, listOfMaps.m, AnimalScript.DIRECTION_SW), counterProps, true, true,
					names);
			
	     	lang.nextStep(translator.translateMessage("STEP_THREE")); 
		
		}

		/** executes the Threshold algorithm
		 * 
		 */
		private void executeThreshold() {

		int row = 0;//Current row
			
			while (t > g){
				findObjectsInAllListsByRow(row);
				calculateT(row); 
				doRandomAccess();
				calculateGscore();
						
				row++;
				if (row > listOfMaps.m.getNrRows() )
					break;	
						
			}
			displayTopK(k);	
	
		}

		/** calculates the T-Value of a row
		 *  prints the result on screen
		 *  shows explanation of the T-Value
		 * 
		 * @param currentRow - current row to sum up all values
		 */
		private void calculateT(int currentRow) {
			
			toggleCodeHighlight(5);
			t =0;// set back to zero to avoid a running "sum"
			
			for (int column = 0; column < listOfMaps.m.getNrCols(); column++) {
					String entityValue = listOfMaps.getUncounted(currentRow, column).split("\\,")[1];
					t = t + Double.valueOf(entityValue);
			}		
			
			//shows T-Value explanation
			InfoBox ib = new InfoBox(lang, new Offset(0, 30, pseudocode,		
						 AnimalScript.DIRECTION_SW), 9, translator.translateMessage("TCALC"));
			
			if (tValueQuestion == null){
			tValueQuestion = new MultipleChoiceQuestionModel("T-Value");
			tValueQuestion.setPrompt(translator.translateMessage("T_VALUE"));

			tValueQuestion.addAnswer(Double.toString(t-0.5).replace(".",","),0,translator.translateMessage("FALSE"));
			tValueQuestion.addAnswer(Double.toString(t).replace(".",","),1,translator.translateMessage("RIGHT"));
			tValueQuestion.addAnswer(Double.toString(t+0.2).replace(".",",") ,0, translator.translateMessage("FALSE"));
			lang.addMCQuestion(tValueQuestion);

			lang.nextStep();
			}
		
			//set the result in TvalueList
			int i = TvalueList.addNewLine();
			TvalueList.set(i, 0, Double.toString(t));	

	
			
			lang.nextStep();
			ib.hide();
		
		}

		/** finds all objects in given row
		 * 
		 * @param row
		 */
		private void findObjectsInAllListsByRow(int row) {
									
			// loop over rows
				toggleCodeHighlight(4);
				listOfMaps.m.highlightElemColumnRange(row + 1, 0,listOfMaps.m.getNrCols() - 1, null, null);
				
				//loop over columns
				for (int column = 0; column < listOfMaps.m.getNrCols(); column++) {
					String element=listOfMaps.get(row, column);
					String entityName = element.split("\\,")[0];
					String entityValue = element.split("\\,")[1];

					// Wenn entityName schon in randomAccessMatrix vorhanden
					if (attributesSeenSoFar.containsElementInColumn(0, entityName)) {
					
					// +1, damit der Zeilenkopf nicht in die Quere kommt
					attributesSeenSoFar.set(attributesSeenSoFar.findElementRow(entityName, 0),column + 1, entityValue);
				
					} else {
						int rownum = attributesSeenSoFar.addNewLine();
						attributesSeenSoFar.set(rownum, 0, entityName);
						// +1, because of Header
						attributesSeenSoFar.set(rownum, column + 1, entityValue);
					
					}
					lang.nextStep();
				}
				row++;
				
			}
		
		/** do a random access and highlight accessed cells
		 * 
		 */
		private void doRandomAccess() {

			explanation.hide();
			explanation =  CodeView.primitiveFromFile(lang,	getResource("RAND_EXPL", true), "explanation",
						   new Offset(0,50, pseudocode, AnimalScript.DIRECTION_SW), null,	new SearchingStyle(h2Props,plainProps,srcProps,false));
			
			// do random access
			List<String> fullySeen = objectsFullySeen();
			toggleCodeHighlight(6);
			for (int row = 0; row < attributesSeenSoFar.getNrRows(); row++) {
			
				if (!fullySeen.contains(attributesSeenSoFar.get(row, 0))) {
					for (int column = 1; column < attributesSeenSoFar.m.getNrCols(); column++) { 
						if (attributesSeenSoFar.getUncounted(row, column) == " ") {
							
							//Highlighting
							if (listOfMaps.m.getNrCols() >= column){
								
							if (listOfMaps.getRowInColumnByName(column,	attributesSeenSoFar.get(row, 0)) != -1){
								listOfMaps.m.highlightCell(listOfMaps.getRowInColumnByName(column,attributesSeenSoFar.getUncounted(row, 0)),column - 1, null, null);
							listOfMaps.accessInc(1);}
							
							//Highlighting RandomAccessMatrix
							attributesSeenSoFar.m.highlightCell(row+1, 0, null, null);
							attributesSeenSoFar.m.highlightCell(0, column, null, null);
							lang.nextStep();
							
							attributesSeenSoFar.set(row, column, mapsArray[column - 1].get(attributesSeenSoFar.getUncounted(row, 0)).toString());
							lang.nextStep();
							
							//Unhighlighting RandomAccessMatrix
							attributesSeenSoFar.m.unhighlightCell(row+1, 0, null, null);
							attributesSeenSoFar.m.unhighlightCell(0, column, null, null);
	
							}
						}
					}
				}
			}
		}

		/**  calculates the Threshold G Score
		 *   shows explanation of calculation
		 * 
		 */
		private void calculateGscore() {

			//shows G-Value explanation
			InfoBox ib = new InfoBox(lang, new Offset(0, 30, pseudocode,		
					AnimalScript.DIRECTION_SW), 9,
					translator.translateMessage("G_EXPL"));
		
			
			toggleCodeHighlight(7);
			
			Map<String, Double> scoreMap = new HashMap<String, Double>();
			List<String> newcolumn = new ArrayList<String>();
			
			int col = attributesSeenSoFar.m.getNrCols();
			if (TcolumnExsist ==false){ //check: T column set?
				TcolumnExsist=true;
			
			for (int row = 0; row < attributesSeenSoFar.getNrRows(); row++)
				newcolumn.add(Double.toString(attributesSeenSoFar.getRowSum(row)));
			
			col = attributesSeenSoFar.addNewColumn("G-Value");			
			lang.nextStep();
			
			//adds column vales to matrix		
			for (int row = 0; row < attributesSeenSoFar.getNrRows(); row++) {
				attributesSeenSoFar.set(row, col, newcolumn.get(row));
				lang.nextStep();
			}
			}else{
				
				for (int row = 0; row < attributesSeenSoFar.getNrRows(); row++){
					if (attributesSeenSoFar.get(row, attributesSeenSoFar.m.getNrCols()-1) == " ")	{				
					
						
						if (gValueQuestion == null){
							gValueQuestion = new MultipleChoiceQuestionModel("G-Value");
							gValueQuestion.setPrompt(translator.translateMessage("G_VALUE"));
							gValueQuestion.addAnswer(Double.toString(t-0.5).replace(".",","), 0,translator.translateMessage("FALSE"));
							gValueQuestion.addAnswer(Double.toString(attributesSeenSoFar.getRowSum(row)).replace(".",","),1,translator.translateMessage("RIGHT"));
							gValueQuestion.addAnswer(Double.toString(t +0.3).replace(".",","),0,translator.translateMessage("FALSE"));
							lang.addMCQuestion(gValueQuestion);
							lang.nextStep();
							}
						
						attributesSeenSoFar.set(row, attributesSeenSoFar.m.getNrCols()-1, Double.toString(attributesSeenSoFar.getRowSum(row)));
						lang.nextStep();
					}
				}
			}

			for (int row = 0; row < attributesSeenSoFar.getNrRows(); row++){
				
			scoreMap.put(attributesSeenSoFar.get(row, 0), Double.valueOf(attributesSeenSoFar.get(row,
														attributesSeenSoFar.m.getNrCols() - 1)));
			}
			scoreMap = sortByValue(scoreMap); 
			//calculate G value
			g = scoreMap.get(scoreMap.keySet().toArray()[0]);
			
			
			ib.hide();
			}
		
		/** display the final result of K objects
		 * 
		 * @param k
		 */
		private void displayTopK(int k) {

			toggleCodeHighlight(-1);
				
			// prints explanation of final result on screen
			explanation.hide();
			explanation = CodeView.primitiveFromFile(lang,
					getResource("FINAL_EXPL", true), "explanation", new Offset(0,
							50, pseudocode, AnimalScript.DIRECTION_SW), null,
							new SearchingStyle(h2Props,plainProps,srcProps,false));
			
			Map<String, Double> scoreMap = new HashMap<String, Double>();
			// Name //Score
			for (int row = 0; row < attributesSeenSoFar.getNrRows(); row++)
				scoreMap.put(attributesSeenSoFar.get(row, 0),
							 Double.valueOf(attributesSeenSoFar.get(row,
							 attributesSeenSoFar.m.getNrCols() - 1)));

			scoreMap = sortByValue(scoreMap);
			
			String topKArray[][] = new String[k][2];
			for (int row = 0; row < k; row++) {
				topKArray[row][0] = scoreMap.keySet().toArray(new String[1])[row];
				topKArray[row][1] = Double.toString(scoreMap.get(topKArray[row][0]));

			}

			topK = MatrixRepresentation.fastStringMatrix(lang, new Offset(0, 20,
					explanation, AnimalScript.DIRECTION_SW), topKArray,matrixProps);
						
			if (savingQuestion == null) {

				savingQuestion = new MultipleChoiceQuestionModel("Savings");
				savingQuestion.setPrompt(translator.translateMessage("SAVINGS"));

				savingQuestion.addAnswer(
						(inputRelation.length - attributesSeenSoFar.getNrRows() - 1)
								+ "", 0,
						translator.translateMessage("FALSE"));
				savingQuestion.addAnswer(
						(inputRelation.length - attributesSeenSoFar.getNrRows()) + "",
						1, translator.translateMessage("RIGHT"));
				savingQuestion.addAnswer(
						(inputRelation.length - attributesSeenSoFar.getNrRows() + 1)
								+ "", 0,
						translator.translateMessage("FALSE"));
				lang.addMCQuestion(savingQuestion);
			}
			
			
			toBeHidden.add(TvalueList.m);
			toBeHidden.add(topK);
			toBeHidden.add(explanation);
			toBeHidden.add(pseudocode);
			toBeHidden.add(attributesSeenSoFar.m);
			toBeHidden.add(listOfMaps.m);
			toBeHidden.add(title);
			
		}

		/**
		 * prepares the input matrix to print on screen prints the original
		 * (default/user given) matrix on screen
		 */
		private void showMatrixTransformation() {

			title = lang.newText(new Coordinates(10, 10), "Threshold Algorithm","title", null, txtProps);
			belowTitle = new Offset(20, 20, title, AnimalScript.DIRECTION_SW);
			
			// prints the by default/user given Matrix on screen
			MatrixRepresentation originalMatrix = new MatrixRepresentation(lang,inputRelation, belowTitle,matrixProps,true);

			// shows first Matrix and Explanation
			InfoBox explanationFirstMatrix = new InfoBox(lang, new Offset(0, 20,
											 originalMatrix.m, AnimalScript.DIRECTION_SW), 2,
											 translator.translateMessage("MATRIX_EXPL"));
			
			titleSlide.hide();
			lang.nextStep(translator.translateMessage("STEP_ONE"));
			
			
			explanationFirstMatrix.hide();

			// prints the UNSORTED converted Matrix and Explanation on screen
			this.mapsArray = convertMatrixToListOfMaps(arrayWithOnlyValues,	firstColumn);
			listOfMaps = new MatrixRepresentation(lang, mapsArray, new Offset(50,0, originalMatrix.m, AnimalScript.DIRECTION_NE),matrixProps,true);

			// prints the explanation of matrix converting on screen
			InfoBox explanationUNSorted = new InfoBox(lang, new Offset(0, 20,
										  originalMatrix.m, AnimalScript.DIRECTION_SW), 2, getResource("CONVERT_EXPL", true));
			lang.nextStep();
			explanationUNSorted.hide();
			listOfMaps.m.hide();

			// prints the sorted converted Matrix and Explanation on screen
			this.mapsArray = convertMatrixToSortedListOfMaps(arrayWithOnlyValues,firstColumn);
			listOfMaps = new MatrixRepresentation(lang, mapsArray, new Offset(50,0, originalMatrix.m, AnimalScript.DIRECTION_NE),matrixProps,true);

			// prints the explanation of matrix converting on screen
			InfoBox explanationSorted = new InfoBox(lang, new Offset(0, 20,
					originalMatrix.m, AnimalScript.DIRECTION_SW), 2, getResource(
					"SORT_EXPL", true));
			lang.nextStep(translator.translateMessage("STEP_TWO")); 
			originalMatrix.m.hide();
			explanationSorted.hide();
		}

		/**
		 * Unhighlights the previously marked line and highlights the line given by
		 * the parameter
		 * -1 unmark the previously marked line
		 * 
		 * @param newHighlightedLine
		 *            
		 */
		public void toggleCodeHighlight(int newHighlightedLine) {
			
			if(newHighlightedLine == -1){
				pseudocode.unhighlight(highlightedLine);
			}else{
			
				if (highlightedLine != -1)
					pseudocode.unhighlight(highlightedLine);
				pseudocode.highlight(newHighlightedLine);
				this.highlightedLine = newHighlightedLine;
			}
		}

		/**
		 * prints the explanation of Threshold Algo on the screen
		 * 
		 */
		private void showExplanationOfThresholdAlgo() {
			
			 titleSlide = new Slide(lang, getResource("TITLESLIDE", true), positioning.getName(),
					 new SearchingStyle(h2Props,plainProps,srcProps,false), new Object());

			}

		/**
		 * Errechnet aus attributesAlreadySeen die Zeilen, die bereits vollst�ndig
		 * befüllt sind.
		 * 
		 * @return List of all fully seen objects
		 */
		public List<String> objectsFullySeen() {
			List<String> result = new ArrayList<String>();
			for (int row = 0; row < attributesSeenSoFar.getNrRows(); row++) {
				boolean addToResult = true;
				for (int column = 1; column < attributesSeenSoFar.m.getNrCols(); column++)
					if (attributesSeenSoFar.get(row, column) == " ")
						addToResult = false;
				if (addToResult)
					result.add(attributesSeenSoFar.get(row, 0));
			}
			return result;
		}

		/**
		 * convertMatrixToListOfMaps
		 * 
		 * @param inputArrayMatrix
		 *            - a 2 column arrary
		 * @param names
		 *            - String array of //Was steckt da drin?
		 * @return
		 */
		@SuppressWarnings("unchecked")
		private static Map<String, Double>[] convertMatrixToSortedListOfMaps(
				double inputArrayMatrix[][], String[] names) {
			Map<String, Double>[] resultHashMap = new LinkedHashMap[inputArrayMatrix[0].length];
			for (int i = 0; i < inputArrayMatrix[0].length; i++) {
				resultHashMap[i] = new LinkedHashMap<String, Double>();
				for (int j = 0; j < inputArrayMatrix.length; j++)
					resultHashMap[i].put(names[j], inputArrayMatrix[j][i]);
				resultHashMap[i] = sortByValue(resultHashMap[i]);
			}
			return resultHashMap;
		}

		/** converts a matrix (Array) to a list of maps
		 * 
		 * @param inputArrayMatrix
		 * @param names -  the name of the objects
		 * @return a list of maps
		 */
		@SuppressWarnings("unchecked")
		private static Map<String, Double>[] convertMatrixToListOfMaps(
				double inputArrayMatrix[][], String[] names) {
			Map<String, Double>[] resultHashMap = new LinkedHashMap[inputArrayMatrix[0].length];
			for (int i = 0; i < inputArrayMatrix[0].length; i++) {
				resultHashMap[i] = new LinkedHashMap<String, Double>();
				for (int j = 0; j < inputArrayMatrix.length; j++)
					resultHashMap[i].put(names[j], inputArrayMatrix[j][i]);
				resultHashMap[i] = resultHashMap[i];
			}
			return resultHashMap;
		}

		public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
				Map<K, V> map) {
			List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(
					map.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
				public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
					return (o2.getValue()).compareTo(o1.getValue());
				}
			});

			Map<K, V> result = new LinkedHashMap<K, V>();
			for (Map.Entry<K, V> entry : list)
				result.put(entry.getKey(), entry.getValue());
			return result;
		}

		/**
		 * Get a resource by its ID.
		 * 
		 * @param id
		 *            The resource ID
		 * @param languageDependant
		 *            If the resource is language depended i.e. there needs to be a
		 *            translation for the current language set to true. Otherwise
		 *            set to false.
		 * @return The resource
		 */
		protected String getResource(String id, boolean languageDependant) {
			String trans = translator.getResourceBundle().getMessage(id, false);
			String res = new String();
			if (trans == null || trans == "") {
				res = resourceName + "_" + id;
				if (languageDependant) {
					res = res.concat("." + locale);
				}
			} else {
				res = translator.translateMessage(id);
			}
			return res;
		}
}
