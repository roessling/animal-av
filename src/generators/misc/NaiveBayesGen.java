/*
 * NaiveBayesGen.java
 * Dirk Kleiner, Philipp Schoenig, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import algoanim.properties.AnimationPropertiesKeys;

public class NaiveBayesGen implements ValidatingGenerator {
    private Language lang;
    private String[][] inputMatrix;
    private MatrixProperties matrixProperties;
    private String[] headlines;
    private String[] sought;

    public void init(){
        lang = new AnimalScript("Naive Bayes", "Dirk Kleiner, Philipp Schoenig", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        inputMatrix = (String[][])primitives.get("inputMatrix");
        matrixProperties = (MatrixProperties)props.getPropertiesByName("matrixProperties");
        headlines = (String[])primitives.get("headlines");
        sought = (String[])primitives.get("sought");
        
	    naivebayes animation = new naivebayes(lang);
	    // Create Script for Animal:
		animation.initNB(inputMatrix, headlines, sought, matrixProperties);

        return lang.toString();
    }
    

    public String getName() {
        return "Naive Bayes";
    }

    public String getAlgorithmName() {
        return "Naive Bayes Algorithm";
    }

    public String getAnimationAuthor() {
        return "Dirk Kleiner, Philipp Schoenig";
    }

    public String getDescription(){
        return "The Naive Bayesian classifier is based on Bayes’ theorem with independence assumptions between "
 +"predictors. A Naive Bayesian model is easy to build, with no complicated iterative parameter estimation "
 +"which makes it particularly useful for very large datasets. Despite its simplicity, the Naive Bayesian "
 +"classifier often does surprisingly well and is widely used because it often outperforms more "
 +"sophisticated classification methods. "
 +"\n"
 +"IMPORTANT:"
 +"\n"
 +"You can modify the matrix and the pattern of what you are looking for. For overview and simplicity reasons, make sure that the "
 +"last column of the matrix is either 'Y' or 'N'. The number of columns of 'sought' have to be one column shorter than the matrix "
 +"columns since the last entry of the matrix is the solution column. You can also choose your own headlines "
 +"for your entries. Please make sure that the number of headline columns has the same amount as the number of matrix columns.";


        
    }

    public String getCodeExample(){
        return "\n"
 +"	P(x|c) P(c)"
 +"\n"
 +"P(c|x) =   ____________________"
 +"\n"
 +"	    P(x)"
 +"\n"
 +"\n"
 +"P(c|X) = P(x1|c) * P(x2|c) * ...  * P(xn|c) * P(c)"
 +"\n"
 +"\n"
 +"P(c|x) is the posterior probability of class (c, target) given predictor (x, attributes)"
 +"\n"
 +"P(c) is the prior probability of class."
 +"\n"
 +"P(x|c) is the likelihood which is the probability of predictor given class."
 +"\n"
 +"P(x) is the prior probability of predictor";
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

	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives){
        headlines = (String[])primitives.get("headlines");
        sought = (String[])primitives.get("sought");
        inputMatrix = (String[][])primitives.get("inputMatrix");
        // Check if the input format is correct
        if (headlines.length == sought.length +1 && headlines.length == inputMatrix[0].length)  {
        	return true;
        }else{
        	return false;
        }
	     
	}


}

class naivebayes {

	 /** 
	  * language object used for creating output 
	  **/
	 private static Language lang;
		
	/**
	  * default duration for swap processes
	  */
	 public final static Timing  defaultDuration = new TicksTiming(30);
	 
	/**
	 *  static Strings for description and a quasi-formal description of the algorithm . 
	 */
	 private static final String DESCRIPTION = "The Naive Bayesian classifier is based on Bayes’ theorem with"
	 		+ " independence assumptions between predictors. A Naive Bayesian model is easy to build, with no"
	 		+ " complicated iterative parameter estimation which makes it particularly useful for very large "
	 		+ "datasets. Despite its simplicity, the Naive Bayesian classifier often does surprisingly well and"
	 		+ " is widely used because it often outperforms more sophisticated classification methods.";
	 private static final String SOURCE_CODE = "Naive Bayes SourceCode";
	 
	 
	 /**
	  * Default constructor
	  * 
	  * @param l:  language object used for creating output
	  * @return 
	  */
	 public naivebayes(Language l) {
	   // Store the language object
	   lang = l;
	   // This initializes the step mode. Each pair of subsequent steps has to
	   // be divided by a call of lang.nextStep();
	   lang.setStepMode(true);
	   }
	 
	  /**
	   * Unhighlight all columns and rows in this matrix
	   * @param sm
	   */
	 public static void clearMatrix(StringMatrix sm){
		 int rows = sm.getNrRows();
		 int col = sm.getNrCols();
		 for (int i= 0 ; i< col ; i++){
			 for (int j=0 ; j< rows ; j++){
				 //sm.unhighlightCell(j, i, null, defaultDuration); 
				 sm.setGridColor(j, i, Color.BLACK, defaultDuration, defaultDuration);
			 }
		 }
	 }

	 /**
	  * Initialize default properties for the visualization and start the algorithm Naive Bayes
	  * default properties might differ depending on generator input; TBD)
	  */
	 	public void initNB (String[][] input, String[] headlines, String[] inputq, MatrixProperties matrixProps){
	     // Create Array: coordinates, data, name, display options,
	     
	  	// set the visual properties for the source code
	     SourceCodeProperties scProps = new SourceCodeProperties();
	     scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
	     scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
	     scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

	   	// set the visual properties for the description
	     SourceCodeProperties dcProps = new SourceCodeProperties();
	     dcProps.set(AnimationPropertiesKeys.BOLD_PROPERTY, Boolean.TRUE);
	     dcProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
	     dcProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
	     dcProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	     
	    // set the visual properties for the headline
	     SourceCodeProperties headProps = new SourceCodeProperties();
	     headProps.set(AnimationPropertiesKeys.BOLD_PROPERTY, Boolean.TRUE);
	     headProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
	     headProps.set(AnimationPropertiesKeys.SIZE_PROPERTY, Integer.SIZE);
	     headProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
	     headProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
	     headProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	     headProps.set("font", new Font(Font.SERIF, Font.PLAIN, 20));
	     
	     // Add Headlines to the matrix
	     String[][] inputmod = new String[input.length +1][headlines.length];
	     for (int i = 0; i < input.length; i++){
	    	 for (int j = 0; j < headlines.length; j++){
	    		 inputmod[i+1][j] = input[i][j];
	    		 inputmod[0][j] = headlines[j];
	    	 }
	     }
	     
	     
	     // Show the explaining text for the algorithm and headline
	     SourceCode headline = lang.newSourceCode(new Coordinates (0,0),"headline" , null, headProps);
	     SourceCode desctext = lang.newSourceCode(new Coordinates(10,50), "description", null, dcProps);
	     
	     headline.addCodeLine("NaiveBayes", "header", 1, defaultDuration);
	     
	     desctext.addCodeLine("The Naive Bayesian classifier is based on Bayes’ theorem with independence assumptions between predictors.", "descline0", 0, defaultDuration); //0
	     desctext.addCodeLine("A Naive Bayesian model is easy to build, with no complicated iterative parameter estimation which makes it ", "descline1", 0, defaultDuration); //1
	     desctext.addCodeLine("particularly useful for very large datasets. Despite its simplicity, the Naive Bayesian classifier often does surprisingly", "descline2", 0, defaultDuration); //2
	     desctext.addCodeLine("well and is widely used because it often outperforms more sophisticated classification methods.", "descline3", 0, defaultDuration); //3
	     desctext.addCodeLine("Bayes theorem provides a way of calculating the posterior probability, P(c|x), from P(c), P(x), and P(x|c).","descline4", 0, defaultDuration); //4
	     desctext.addCodeLine("Naive Bayes classifier assume that the effect of the value of a predictor (x) on a given class (c) is independent","descline5", 0, defaultDuration); //5
	     desctext.addCodeLine("of the values of other predictors. This assumption is called class conditional independence.","descline6", 0, defaultDuration); //6
	     desctext.addCodeLine("In this example, we only allow two different variations in the solution row ('Y' and 'N') in order to provide a good overview of the algorithm.", "descline7", 0, defaultDuration); //7
	     
	     lang.nextStep();
	     desctext.hide();
	 	 // create the StringMatrix object with properties "matrixProps"
	 	 StringMatrix sm = lang.newStringMatrix(new Coordinates(490 ,100),inputmod, "stringArray", null, matrixProps);
	 	

	 	 
	 	 



	     // create the source code entity
	  	      
	     SourceCode sformula = lang.newSourceCode(new Coordinates(0,40), "fourmulacode" , null, scProps);

 

	     
	     // add formulasource for Y and N
	     String[] yn = new String[2];
	     yn[0] = "|" + headlines[headlines.length - 1] + "=Y";
	     yn[1] = "|" + headlines[headlines.length - 1] + "=N";
	     

	         sformula.addCodeLine("Sought: P {", null, 0, null); // 0 and 1
	         for (int i = 0; i< headlines.length - 1; i++){
	        	 // Remove the multiplication operator on the last line of formula source
	        	 if (i != headlines.length - 2){
	        		 sformula.addCodeElement(headlines[i] + "=" + inputq[i], "label", 0, null); // + x * (headlines.length - 1)
	        	 } else {
	        		 sformula.addCodeElement(headlines[i] + "=" + inputq[i] + "}", "label", 0, null); // + x * (headlines.length - 1)
	        	 } 
	         }
	     
	     
	     // ValueMatrix
	     String[][] vmatrix = new String[headlines.length + 1][2];
	     vmatrix[0][0] = "P(" + headlines[headlines.length -1] + "=Y)=";
	     vmatrix[0][1] = "P(" + headlines[headlines.length -1] + "=N)=";
	     vmatrix[headlines.length][0] = "Result: P*(Y) = ";
	     vmatrix[headlines.length][1] = "Result: P*(N) = ";
	     
	     for (int j = 1; j < headlines.length; j++){
	    	 vmatrix[j][0] = "P(" + headlines[j-1] + "=" +inputq[j-1]  + yn[0] + ") =";
	    	 vmatrix[j][1] = "P(" + headlines[j-1] + "=" +inputq[j-1]  + yn[1] + ") =";
	     }
	     
	     
	     StringMatrix im = lang.newStringMatrix(new Offset(-490, 0, sm, "offset"),vmatrix, "valueMatrix", null, matrixProps);

	     lang.nextStep();
	     
	     // Call Naive Bayes
	 	NaiveBayes(inputq ,input, sm, im, sformula, headlines);
	 	     
	 	}
	 
	   
	 /**
	  * NaiveBayes: Runs the Naive Bayes algorithm on the input Matrix (String[][]). 
	  * @param input
	  * @param matrix
	  */
	 	public void NaiveBayes (String[] inputData, String[][] matrix, StringMatrix sm, StringMatrix im, SourceCode sformula, String[] headlines){
	 	
	 	// Those variables count how many times the result of our sample is "Yes" or "No" and the probabilities
	 	// for the results. Double is used here for division.
	 	
	 	// nbYes: NaiveBayes Yes - counts how many times the solution is "yes" if the input value is same to the value of the table entry
	 	// nbNo: NaiveBayes No - counts how many times the solution is "no" if the input value is same to the value of the table entry
	 	// pNByes and pNBno - Save the solution for the chances of occurrence of result(Y or N) for the input data P*.
	 	double nbYes = 0;
	 	double pNByes = 0;
	 	double nbNo = 0;
	 	double pNBno = 0;
	 	int cfeat = 0;
	 	int cfeat2 = 0;
	 	
	 	// Create a string for additional features
	 	String [][] cmatrix = new String[3][2];
	 	cmatrix [0][0] = "Number of entries:";
	 	cmatrix[1][0] = "Number of relevant comparisons in this step:";
	 	cmatrix[2][0] = "Total number of all relevant comparisons:";
	 	// Number of entries
	 	cmatrix[0][1] = "" + matrix.length;
	 	// Number of relevant comparisons
	 	cmatrix[1][1] = "" + 0;
	 	// Number of all comparisons
	 	cmatrix[2][1] = "" + 0;
	 	
	 	StringMatrix cm = lang.newStringMatrix(new Offset( -20, -80, sm, "offset"),cmatrix, "counterMatrix", null, im.getProperties());
	 	
	 	// Calculate P* for "Y" and "N" (possible solutions)
	 	// Calculate the frequency of "Y" and "N" in the last column (results of the input matrix samples)
	 	for (int i = 0; i< matrix.length; i++){
	 		if (matrix[i][inputData.length].equalsIgnoreCase("Y")){
	 			nbYes = nbYes +1;
	 			sm.setGridColor(i+1, inputData.length, Color.GREEN, defaultDuration, defaultDuration);
	 			cfeat = cfeat + 1;
	 		}
	 		if (matrix[i][inputData.length].equalsIgnoreCase("N")){
	 			nbNo = nbNo +1;
	 			//sm.highlightElem(i+1, inputData.length, defaultDuration, defaultDuration);
	 			sm.setGridColor(i+1, inputData.length, Color.RED, defaultDuration, defaultDuration);
	 			cfeat = cfeat + 1;
	 		}
	 	}
	 	cfeat2 = cfeat2 + cfeat;
		cm.put(1, 1, ("" + cfeat + ""), defaultDuration, defaultDuration);
		cm.put(2, 1, ("" + cfeat2 + ""), defaultDuration, defaultDuration);
		cfeat = 0;
	 	// Calculate P* for "Y" and "N"
	 	pNByes = (nbYes / matrix.length);
	 	pNBno = (nbNo /matrix.length);
	 	im.put(0, 0, im.getElement(0, 0) + "(" + Math.round(nbYes) + "/" + Math.round(matrix.length) + ")" , defaultDuration, defaultDuration);
	 	im.put(0, 1, im.getElement(0, 1) + "(" + Math.round(nbNo) + "/" + Math.round(matrix.length) + ")" , defaultDuration, defaultDuration);
	 	im.setGridColor(0, 0, Color.GREEN, defaultDuration, defaultDuration);
	 	im.setGridColor(0, 1, Color.RED, defaultDuration, defaultDuration);
	 	lang.nextStep();
	 	clearMatrix(sm);
	 	clearMatrix(im);


	 	// Arrays to save the result for every calculation step. Size is depending on the number or columns in the matrix.
	 	double[] arrayYes = new double[inputData.length];
	 	double[] arrayNo = new double[inputData.length];
	 	// counters for highlighting formulacode
		int startcounter = 1;
		int highlightcounter = startcounter;
	 	
	 	// for each column
	 	for (int i = 0; i < inputData.length; i++){
	 		// step counters, resets for every column
	 		double counterY = 0;
	 		double counterN = 0;
	 		// loop through every row
	 		for (int j = 0; j < matrix.length; j++){
	 			// if the value is equal to the input
	 			if (matrix[j][i].equals(inputData[i])){
	 				// check the result of the respective row
	 				if (matrix[j][inputData.length].equalsIgnoreCase("Y")){
	 					counterY = counterY +1;
	 					//sm.highlightCell(j+1, inputData.length, defaultDuration, defaultDuration);
	 					//sm.highlightCell(j+1, i, defaultDuration, defaultDuration);
	 					sm.setGridColor(j+1, inputData.length, Color.GREEN, defaultDuration, defaultDuration);
	 					sm.setGridColor(j+1, i, Color.GREEN, defaultDuration, defaultDuration);
	 					cfeat = cfeat + 1;
	 				}
	 				if (matrix[j][inputData.length].equalsIgnoreCase("N")){
	 					counterN = counterN+1;
	 					//sm.highlightElem(j+1, inputData.length, defaultDuration, defaultDuration);
	 					//sm.highlightElem(j+1, i, defaultDuration, defaultDuration);
	 					sm.setGridColor(j+1, inputData.length, Color.RED, defaultDuration, defaultDuration);
	 					sm.setGridColor(j+1, i, Color.RED, defaultDuration, defaultDuration);
	 					cfeat = cfeat +1;
	 				}
	 			}
	 		}
	 		// Save the result into the calculation-arrays
	 		arrayYes[i] = (counterY / nbYes);
	 		arrayNo[i] = (counterN / nbNo);
	 		im.put(highlightcounter, 0, im.getElement(highlightcounter, 0) + "(" + Math.round(counterY) + "/" + Math.round(nbYes) + ")" , defaultDuration, defaultDuration);
	 		im.put(highlightcounter, 1, im.getElement(highlightcounter, 1) + "(" + Math.round(counterN) + "/" + Math.round(nbNo) + ")" , defaultDuration, defaultDuration);
	 		im.setGridColor(highlightcounter, 0, Color.GREEN, defaultDuration, defaultDuration);
	 		im.setGridColor(highlightcounter, 1, Color.RED, defaultDuration, defaultDuration);
	 		highlightcounter = highlightcounter + 1;
	 		cfeat2 = cfeat2 + cfeat;
			cm.put(1, 1, ("" + cfeat + ""), defaultDuration, defaultDuration);
			cm.put(2, 1, ("" + cfeat2 + ""), defaultDuration, defaultDuration);
			cfeat = 0;
	 
	 		lang.nextStep();
	 		clearMatrix(sm);
	 		clearMatrix(im);
	 		
	 	}
	 	// Result Calculation variables; default value of 1 due to multiplication operation
		double resultYes = 1;
		double resultNo = 1;
	 	// Sum up all multiplications.
	 	for (int i= 0; i< (inputData.length);i++){
	 		resultYes = resultYes * arrayYes[i];
	 		resultNo = resultNo * arrayNo[i];
	 	}
	 	// Multiplication with P* to calculate the final solution for the probability factor.
	 	resultYes = resultYes * pNByes;
	 	resultNo = resultNo * pNBno;
	 	im.put(highlightcounter, 0, im.getElement(highlightcounter, 0) + resultYes , defaultDuration, defaultDuration);
	 	im.put(highlightcounter, 1, im.getElement(highlightcounter, 1) + resultNo , defaultDuration, defaultDuration);

	 	
	 	// Calculate percentages for the soltions
	 	double chanceYes;
	 	double chanceNo;
	 	
	 	
	 	// Round the solution
	 	chanceYes = Math.round(resultYes *100 / (resultYes + resultNo));
	 	chanceNo = Math.round(resultNo *100 / (resultYes + resultNo));
	 	
	 	//sformula.addCodeLine("Likelihood: P*(Y) = " + chanceYes + "%", null, 0, null);
	 	//sformula.addCodeLine("Likelihood: P*(N) = " + chanceNo + "%", null, 0, null);
	 	
	 	lang.nextStep();
	 	
	 	// Show the final text and explain the solution
	 	sm.hide();
	 	sformula.hide();
	 	im.hide();
	 	cm.hide();
	 	
	 	String seekingString = new String();
	 	for (int i=0; i < headlines.length -1 ; i++){
	 		seekingString = seekingString + headlines[i] + "=" + inputData[i] + "   |    ";
	 	}
	 	

	 	SourceCode finalText = lang.newSourceCode(new Offset(0, 10, sformula, "offset"), "finaltext" , null, sformula.getProperties());
	 	finalText.addCodeLine("Naive Bayes Solution:", null, 0, null);
	 	finalText.addCodeLine("P*(Y) / ( P*(Y) + P*(N) ) is the likelihood for P(Y)", null,0, null);
	 	finalText.addCodeLine("P*(N) / ( P*(Y) + P*(N) ) is the likelihood for P(N).", null,0, null);
	 	finalText.addCodeLine("P*(Y) =" + resultYes, null, 0, null);
	 	finalText.addCodeLine("P*(N) =" + resultNo, null, 0, null);
	 	finalText.addCodeLine(" ", null, 0, null);
	 	finalText.addCodeLine(" ", null, 0, null);
	 	finalText.addCodeLine(" ", null, 0, null);
	 	finalText.addCodeLine("" + resultYes + "", null, 0, null);
	 	finalText.addCodeLine("__________________" + "          = " + chanceYes + "%" , null, 0, null);
	 	finalText.addCodeLine("(" + resultYes + "+" + resultNo + ")", null, 0, null);
	 	finalText.addCodeLine(" ", null, 0, null);
	 	finalText.addCodeLine(" ", null, 0, null);
	 	finalText.addCodeLine(" ", null, 0, null);
	 	finalText.addCodeLine("" + resultNo + "", null, 0, null);
	 	finalText.addCodeLine("__________________" + "          = " + chanceNo + "%" , null, 0, null);
	 	finalText.addCodeLine("(" + resultYes + "+" + resultNo + ")", null, 0, null);
	 	finalText.addCodeLine(" ", null, 0, null);
	 	finalText.addCodeLine(" ", null, 0, null);
	 	finalText.addCodeLine("The likelihood that " + headlines[headlines.length -1] + "= 'Y', given the following case: " + seekingString + " is " + chanceYes + "%.", null, 0, null);
	 	finalText.addCodeLine("The likelihood that " + headlines[headlines.length -1] + "= 'N', given the following case: " + seekingString + " is " + chanceNo + "%.", null, 0, null);
	 	finalText.addCodeLine(" ", null, 0, null);
	 	}
	 

		public static void main(String[] args) throws InstantiationException, IllegalAccessException {

		}

	}