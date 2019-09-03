/*
 * KNearestNeighbor.java
 * Hermann Berket, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import javax.naming.spi.DirStateFactory;

import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.HashSet;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.controllers.InteractionController;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.views.MultipleChoiceQuestionView;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import animal.animator.SetText;
import extras.lifecycle.query.workflow.Calculator;


public class KNearestNeighbor implements Generator {
    private Language lang;
    private SourceCodeProperties sourceCode;
    private ArrayProperties newExampleProps;
    private MatrixProperties trainingDataProps;
    private String[][] trainingData;
    private int k;
    private String[] newExample;
    private TextProperties textProps;
    
    private int calculationNumber = 0;
    
    public void init(){
        lang = new AnimalScript("KNearestNeighbor", "Hermann Berket", 1000, 800);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
    	newExampleProps = (ArrayProperties)props.getPropertiesByName("newExampleProps");
        trainingDataProps = (MatrixProperties)props.getPropertiesByName("trainingDataProps");
        trainingData = (String[][])primitives.get("trainingData");
        k = (Integer)primitives.get("k");
        newExample = (String[])primitives.get("newExample");
        textProps = (TextProperties)props.getPropertiesByName("textProps");
        
        lang.setInteractionType(
        	    Language.INTERACTION_TYPE_AVINTERACTION);
        
        KNN(k,trainingData,newExample,trainingDataProps,newExampleProps,sourceCode, textProps);
        
        lang.finalizeGeneration();
        
        return lang.toString();
    }

    public String getName() {
        return "KNearestNeighbor";
    }

    public String getAlgorithmName() {
        return "KNearestNeighbor";
    }

    public String getAnimationAuthor() {
        return "Hermann Berket";
    }

    public String getDescription(){
        return "K-Neaerst Neigbor algorithms classify a new example by comparing it to all previously seen examples. "
 +"\n"
 +"The classifications of the k most similar previous cases are used for predicting the classification of the"
 +"\n"
 +"current example. "
 +"\n"
 +"The training examples are used for "
 +"\n"
 +" 	- providing a library of sample cases"
 +"\n"
 +"  	- re-scaling the similarity function to maximize performance."
 +"\n"
 +" The algorithm requires three things"
 +"\n"
 +"  	- The set of stored examples"
 +"\n"
 +"  	- Distance Metric to compute distance between examples"
 +"\n"
 +" 	- The value of k, the number of nearest neighbors to retrieve"
 +"\n";
    }

    public String getCodeExample(){
        return "To classify unknown example: "
 +"\n"
 +"         I. Compute distance to other training examples"
 +"\n"
 +"         II. Identify k nearest neighbors"
 +"\n"
 +"         III. Use class labels of nearest neighbors to determine the class label of unknown example";
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
    
    public void KNN(int k, String[][] trainingData, String[] newExample, MatrixProperties mp, ArrayProperties ap,SourceCodeProperties scProps, TextProperties txtProps){
    	lang.setStepMode(true);
    	
    	calculationNumber = 0;
    	SourceCodeProperties introProps = new SourceCodeProperties();
    	Text mainHeader = lang.newText(new Coordinates(0, 0), "K-Nearest-Neighbor Algorithm", "mainHeader", null);
    	mainHeader.setFont(new Font("Monospaced", Font.BOLD, 20), null, null);
    	introProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 15));
    	SourceCode intro = lang.newSourceCode(new Coordinates(10, 10), "sourceCode", null,introProps);
    	
    	intro.addCodeLine("Nearest Neigbor Classifier are used in Machine Learning topics.", null, 0, null);
    	intro.addCodeLine("K-Neaerst Neigbor algorithms classify a new example by comparing it to all previously seen examples. ", null, 0, null);
    	intro.addCodeLine("To do his job, the algorithm gets a new example without label, a training data set, and an Int-Value K.", null, 0, null);
    	intro.addCodeLine("The classifications of the k most similar previous cases are used for predicting the classification ", null, 0, null);
    	intro.addCodeLine("of the new example.", null, 0, null);
    	intro.addCodeLine("", null, 0, null);
    	
    	lang.nextStep();
    	intro.addCodeLine("The training examples are used for ", null, 0, null);
    	lang.nextStep();
    	intro.addCodeLine("- providing a library of sample cases", null, 1, null);
    	lang.nextStep();
    	intro.addCodeLine("- re-scaling the similarity function to maximize performance.", null, 1, null);
    	intro.addCodeLine("", null, 0, null);
    	lang.nextStep();
    	intro.addCodeLine("The algorithm requires three things:", null, 0, null);
    	lang.nextStep();
    	intro.addCodeLine("- The set of stored examples", null, 1, null);
    	lang.nextStep();
    	intro.addCodeLine("- Distance Metric to compute distance between examples", null, 1, null);
    	lang.nextStep();
    	intro.addCodeLine("- The value of k, the number of nearest neighbors to retrieve", null, 1, null);
    	
    	lang.nextStep();
    	
    	intro.hide();
    	intro = null;
    	
    	intro = lang.newSourceCode(new Coordinates(10, 10), "sourceCode", null,introProps);
    	
    	intro.addCodeLine("I: To classify a new example without class label the algorithm has to ", null, 0, null);
    	intro.addCodeLine("   compute the distance to the training example first.", null, 0, null);
    	intro.addCodeLine("   In this case we have nominal values and use a 1/0- Metric to compute the distance.", null, 0, null);
    	intro.addCodeLine("   The metric is very simple; if two attributes of a rule are not equal,", null, 0, null);
    	intro.addCodeLine("   the algorithm increases the distance by 1", null, 0, null);
    	intro.addCodeLine("   This action will be repeatet for every attribute of every ", null, 0, null);
    	intro.addCodeLine("   training example until all distances to the new example are known.", null, 0, null);
    	intro.addCodeLine("", null, 0, null);
    	intro.addCodeLine("", null, 0, null);
    	lang.nextStep();
    	intro.addCodeLine("II: In the next step the algorithm identifies the k training examples ", null, 0, null);
    	intro.addCodeLine("    with the lowest distance to the new example.", null, 0, null);
    	intro.addCodeLine("", null, 0, null);
    	intro.addCodeLine("", null, 0, null);
    	lang.nextStep();
    	
    	intro.addCodeLine("III: As a last step the algorithm decides with", null, 0, null);
    	intro.addCodeLine("     majority vote how to label the new example.", null, 0, null);
    	intro.addCodeLine("     The class label of k nearest neighbors in the training ", null, 0, null);
    	intro.addCodeLine("     data with the highest occurence will be taken.", null, 0, null);
    	intro.addCodeLine("     If the occurences of the labels is equal, the algorithm ", null, 0, null);
    	intro.addCodeLine("     decides randomly how to label the new example.", null, 0, null);
    	lang.nextStep();
    	
    	intro.hide();
    	intro = null;
    	
    	MatrixProperties matrixProps = mp;

    	
    	Text pseudoCodeHeader = lang.newText(new Coordinates(0,20), "Pseudo code", "pseudoCode", null);
    	Text trainingDataHeader = lang.newText(new Coordinates(550,20), "Training Data", "trainingData", null);
    	Text newExampleHeader = lang.newText(new Coordinates(0,150), "New Example", "newExample", null);
    	Text descriptionHeader = lang.newText(new Coordinates(0,250), "Description", "description", null);
    	Text calcHeader = lang.newText(new Coordinates(250, 350), "Calculation","calculations", null);
    	
    	Text description = lang.newText(new Offset(0, 10, descriptionHeader, AnimalScript.DIRECTION_SW), "", "description", null, txtProps);
    	Text description2 = lang.newText(new Offset(0, 10, description, AnimalScript.DIRECTION_SW), "", "description", null, txtProps);
    	Text description3 = lang.newText(new Offset(0, 10, description2, AnimalScript.DIRECTION_SW), "", "description", null, txtProps);
    	Text kHeader = lang.newText(new Offset(0, 10, description3, AnimalScript.DIRECTION_SW), "K-value", "kvalue", null, txtProps);
    	
    	mainHeader.setFont(new Font("Monospaced", Font.BOLD, 20), null, null);
    	trainingDataHeader.setFont(new Font("Monospaced", Font.BOLD, 16), null, null);
    	newExampleHeader.setFont(new Font("Monospaced", Font.BOLD, 16), null, null);
    	pseudoCodeHeader.setFont(new Font("Monospaced", Font.BOLD, 16), null, null);
    	descriptionHeader.setFont(new Font("Monospaced", Font.BOLD, 16), null, null);
    	kHeader.setFont(new Font("Monospaced", Font.BOLD, 16), null, null);
    	calcHeader.setFont(new Font("Monospaced", Font.BOLD, 16), null, null);
    	
    	lang.nextStep();
    	
        // create matrix tables
        StringMatrix tm = lang.newStringMatrix(new Offset(15, 10, trainingDataHeader, AnimalScript.DIRECTION_S), trainingData, "training_data",null,matrixProps);
        
        String[][] calcMatrix = new String[trainingData.length][1];
        calcMatrix[0][0] = "DISTANCE";
        for(int i = 1; i < trainingData.length;i++){
        	calcMatrix[i][0] = " - ";
        }
        
        int indexOfMissingAttribute = 0;
        int missingValues = 0;
        for(int i = 0; i < newExample.length; i++){
        	if(newExample[i].compareTo("?") == 0){
        		indexOfMissingAttribute = i;
        		missingValues++;
        	}
        	
        }
        
        if(missingValues > 1){
        	System.out.println("Es darf nur 1 unbekannten Wert geben");
        	return;
        }
        	
        
        String[] calcs = {"# calculations: ",String.valueOf(calculationNumber)};
        
        StringMatrix calc = lang.newStringMatrix(new Offset(-10, 10, trainingDataHeader, AnimalScript.DIRECTION_SW), calcMatrix, "calculationTable", null, matrixProps);
     
        StringArray example = lang.newStringArray(new Offset(0, 10, newExampleHeader, AnimalScript.DIRECTION_S), newExample, "newExample", null,newExampleProps);
        String[] kVal = {"Value of k:",String.valueOf(k)};
        StringArray kValue = lang.newStringArray(new Offset(0, 10, kHeader, AnimalScript.DIRECTION_S), kVal, "K", null,newExampleProps);
        StringArray calculations = lang.newStringArray(new Offset(0, 50, kHeader, AnimalScript.DIRECTION_S), calcs, "calculations", null,newExampleProps);
        
        example.showIndices(false, null, null);
        kValue.showIndices(false, null, null);
        calculations.showIndices(false, null, null);
        
        SourceCode sc = lang.newSourceCode(new Offset(0, 10, pseudoCodeHeader, AnimalScript.DIRECTION_S), "sourceCode", null, scProps);
        
        sc.addCodeLine("I. Compute distance to other training examples", null, 0, null);
        sc.addCodeLine("II. Identify k nearest neighbors", null, 0, null);
        sc.addCodeLine("III. Use class labels of nearest neighbors to determine ", null, 0, null);
        sc.addCodeLine("	the class label of unknown example", null, 0, null);
        
        lang.nextStep();
        description.setText("First we compute the distance to all other examples.", null, null);
        description2.setText("How many attributes are different to the attributes of the new example.", null, null);
        description3.setText("For the decision that two attributes are different, we use the 1/0-Metric.", null, null);
        
        
        sc.highlight(0);
        example.highlightCell(0, example.getLength()-1, null, null);
        
        lang.nextStep();
        
        for(int i = 1; i < trainingData.length;i++){
        	tm.highlightCellColumnRange(i, 0, trainingData[i].length-1, null, null);
        	calc.put(i, 0, String.valueOf(computeDist(newExample, trainingData[i],calculations)), null, null);
        	
        	lang.nextStep();
        	
        	tm.unhighlightCellColumnRange(i, 0, trainingData[i].length-1, null, null);
        	
        	lang.nextStep();
        }
        
        //identify k-nearest neighbors
        sc.unhighlight(0);
        sc.highlight(1);
        example.unhighlightCell(0, example.getLength()-1, null, null);
        description3.hide();
        description.setText("Now we identify the k = " + k + " nearest neighbors.", null, null);
        description2.setText("If some examples have the same distance, we choose the first one.", null, null);

        int[] neighbors = chooseKNeighbors(k, calc);
        for(int i = 0; i < neighbors.length;i++){
        	calc.highlightCell(neighbors[i], 0, null, null);
        	tm.highlightCellColumnRange(neighbors[i], 0, tm.getNrCols()-1, null, null);
        }
        
        MultipleChoiceQuestionModel q3 = new MultipleChoiceQuestionModel("Question");
		q3.setPrompt("How many training examples are involved in majority voting?");
		q3.addAnswer(k + " - training data examples.",1,"Correct! We want to look on the " +k +" nearest neighbors.");
		q3.addAnswer("all training examples.",0,"The answer is incorrect.");
		lang.addMCQuestion(q3);
        
        lang.nextStep();
        
        sc.unhighlight(1);
        sc.highlight(2);
        sc.highlight(3);
        description.setText("Via majority vote we decide which class the exmaple becomes.", null, null);
        description2.setText("It means that the majority of the nearest neighbors decides which class ", null, null);
        description3.setText("the new example has to be. If there is no majority we decide randomly.", null, null);
        description2.show();
        description3.show();
        
        lang.nextStep();
        
        MultipleChoiceQuestionModel q2 = new MultipleChoiceQuestionModel("Question");
		q2.setPrompt("Now you can see the nearest neighbors and their classes. How do we choose the label for the example?");
		q2.addAnswer("With respect to majority",1,"If we have a mojority of 'no' labeled nearest neighbors, it is correct.");
		q2.addAnswer("Randomly",1,"If the count of nearest neighbors with the same labels is equal, the decision is random");
		
		lang.addMCQuestion(q2);
        
        example.put(indexOfMissingAttribute, majorityVoteing(neighbors, tm, indexOfMissingAttribute), null, null);
        example.highlightCell(indexOfMissingAttribute, null, null);
        
        lang.nextStep();
        
        for(int i = 0; i < neighbors.length;i++){
        	calc.unhighlightCell(neighbors[i], 0, null, null);
        	tm.unhighlightCellColumnRange(neighbors[i], 0, tm.getNrCols()-1, null, null);
        }
        
        sc.unhighlight(2);
        sc.unhighlight(3);
        
		MultipleChoiceQuestionModel q1 = new MultipleChoiceQuestionModel("Question");
		q1.setPrompt("Why does the algorithm need training data?");
		q1.addAnswer("They are used for providing as a library of sample cases.", 1, "Correct!");
		q1.addAnswer("They are used for showing which cases the algorithm knows.", 2, "The answer is incorrect.");
		lang.addMCQuestion(q1);
		
		sc.hide();
		calc.hide();
		description.hide();
		pseudoCodeHeader.hide();
		kValue.hide();
		kHeader.hide();
		description2.setText("", null, null);
		description3.hide();
		descriptionHeader.hide();
		calcHeader.hide();
		
		
		StringArray endCalculations = lang.newStringArray(new Offset(0, 30, example, AnimalScript.DIRECTION_SW), calcs, "endcalcs", null,newExampleProps);
		endCalculations.showIndices(false,null, null);
		endCalculations.put(1, String.valueOf(calculationNumber), null, null);
		calculations.hide();
		
		for(int i = 0; i < neighbors.length;i++){
        	tm.highlightCell(neighbors[i], indexOfMissingAttribute, null, null);
        }
		
		intro = lang.newSourceCode(new Offset(0, 10, mainHeader, AnimalScript.DIRECTION_SW), "sourceCode", null,introProps);
		intro.addCodeLine("Now you can verify that the "+ tm.getElement(0, indexOfMissingAttribute) +" label was taken with ", null, 0, null);
		intro.addCodeLine("respect to the nearest neighbors in the training data set", null, 0, null);
		intro.addCodeLine("or randomly if there is no majority.", null, 0, null);
    }
    
    private int computeDist(String[] rule1, String[] rule2, StringArray calcNumber){
    	lang.nextStep();
    	
    	int dist = 0;
    	String[] currentDistance = {"Distance so far: ", String.valueOf(dist)};
    	StringArray firstRule = lang.newStringArray(new Coordinates(250, 370), rule1, "firstRule", null,newExampleProps);
    	StringArray secondRule = lang.newStringArray(new Coordinates(250, 400), rule2, "secondRule", null,newExampleProps);
    	StringArray distance = lang.newStringArray(new Coordinates(250, 430), currentDistance, "Distance", null,newExampleProps);
    	Text calcDesc = lang.newText(new Coordinates(250, 470), "", "calcDescription", null,textProps);
    	
    	firstRule.showIndices(false, null, null);
    	secondRule.showIndices(false, null, null);
    	distance.showIndices(false, null, null);
    	calcDesc.show();
    	distance.highlightCell(1, null, null);
    	
    	for(int i = 0; i < rule1.length;i++){ 
    		
    		
    		distance.put(1, String.valueOf(dist), null, null);
    		firstRule.highlightCell(i, null, null);
    		secondRule.highlightCell(i, null, null);
    		
    		if(rule1[i].compareTo(rule2[i]) != 0){
    			dist++;
    			calculationNumber++;
            	calcNumber.put(1, String.valueOf(calculationNumber), null, null);
    			calcDesc.setText(firstRule.getData(i)+ " != " + secondRule.getData(i) + " --> dist++", null, null);
    			distance.put(1, String.valueOf(dist), null, null);
    			lang.nextStep();
    		}
    		else {
    			calcDesc.setText(firstRule.getData(i)+ " == " + secondRule.getData(i), null, null);
    			lang.nextStep();
    		}
    	}
    	
    	for(int i = 0; i < firstRule.getLength();i++){
    		firstRule.unhighlightCell(i, null, null);
    		secondRule.unhighlightCell(i, null, null);
    	}
    	
    	lang.nextStep();
    	calcDesc.setText("", null, null);
    	distance.unhighlightCell(1, null, null);
    	firstRule.hide();
    	secondRule.hide();
    	distance.hide();
    	
    	return dist;
    }
    
    private int[] chooseKNeighbors(int k, StringMatrix calc){
    	
    	int[] nearestNeighbors = new int[k];
    	int[] a = new int[calc.getNrRows()];
    	for(int i = 1; i < calc.getNrRows(); i++){
    		a[i] = Integer.parseInt(calc.getElement(i, 0));
    	}
    	
    	for(int i = 0; i< k; i++){
    		nearestNeighbors[i] = a.length-1;
    	}
    	
    	for(int i = 1; i < k+1;i++){
    		for(int j = 1; j < a.length;j++){
    			if (a[i] < a[j] && !contains(nearestNeighbors,i) && a[nearestNeighbors[i-1]] > a[i])
    				nearestNeighbors[i-1] = i;
    			else if (a[i] > a[j] && !contains(nearestNeighbors,j) && a[nearestNeighbors[i-1]] > a[j])
    				nearestNeighbors[i-1] = j;
    		}
    	}
    	
    	return nearestNeighbors;
    }
    
    private boolean contains(int[] arr, int n){
    	boolean contains = false;
    	
    	for(int i = 0; i < arr.length; i++){
    		if(arr[i] == n)
    			return true;
    	}
    	
    	return contains;
    }
    
    private String majorityVoteing(int[] neighbors, StringMatrix tm, int missingIndex){
    	
    	Hashtable<String, Integer> voteTable = new Hashtable<String,Integer>();
    	
    	for(int i = 0; i < neighbors.length; i++){
    		String nextCandidate =  tm.getElement(neighbors[i], missingIndex);
    		if(!voteTable.containsKey(nextCandidate))
    			voteTable.put(nextCandidate, 1);
    		else
    			voteTable.put(nextCandidate, voteTable.get(nextCandidate)+1);
    	}
    	
    	Set<String> keys = voteTable.keySet();
    	Set<String> majorityKeys = voteTable.keySet();
    	
    	
    	for(String k: keys){
    		for(String m: majorityKeys){
    			if(voteTable.get(k) < voteTable.get(m))
    				majorityKeys.remove(k);
    				break;
    		}
    	}
    	
    	String[] mK =  majorityKeys.toArray(new String[majorityKeys.size()]);
    	int rnd = (int)(Math.random())* mK.length + 1;
    	
    	return mK[rnd];
    	
    }

}