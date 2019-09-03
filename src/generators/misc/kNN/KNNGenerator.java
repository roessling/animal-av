/*
 * KNNGenerator.java
 * Jan Rehbein, Marius Rettberg-Päplow, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc.kNN;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.generators.Language;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;

import javax.swing.JOptionPane;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.ArrayProperties;


public abstract class KNNGenerator implements ValidatingGenerator {


	protected Language lang;
    private SourceCodeProperties sourceCodeProperty;
    private String[] trainingDataLabels;
    private int[] trainingDataXCoords;
    private int[] trainingDataYCoords;
    private String[][] trainingPoints;
    private CircleProperties classificationCircleProperty;
    private RectProperties rectVisualisationAreaProperty;
    private int pointToClassifyX;
    private int pointToClassifyY;
    private int k;
    private ArrayProperties distanceArrayProperty;
    private PolylineProperties distanceLineProperty;
    
    private TextProperties labelTextProperty;
    private TextProperties headLineTextProperty;
    private TextProperties headLineSubTextProperty;
    private TextProperties headLineSubSubTextProperty;
    private int trainingPointCount;
    protected int trainingPointClassesCount;
    private boolean boolRandomizePoints;
    private boolean boolRandomizeClassificationPoint;
    private int[] upperLeftVisualizationArea;
    private int[] lowerRightVisualizationArea;
    private Point upperLeftPointVisArea;
	private Point lowerRightPointVisArea;
    private Point upperLeftBorderVisArea;
	private Point lowerRightBorderVisArea;
	private String distanceMetric;

        
    public abstract void init();

    public void initializeProperties(AnimationPropertiesContainer props) {
    	// properties
        rectVisualisationAreaProperty = (RectProperties)props.getPropertiesByName("visualisationArea");
        sourceCodeProperty = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        classificationCircleProperty = (CircleProperties)props.getPropertiesByName("classificationCircle");
        distanceArrayProperty = (ArrayProperties)props.getPropertiesByName("distanceVector");
        distanceLineProperty = (PolylineProperties)props.getPropertiesByName("distanceLine");

        // header besitzen ein, für alle header gültiges, Property und werden im Quellcode mit verschiedenen größen versehen. 
        labelTextProperty = (TextProperties)props.getPropertiesByName("text");
        headLineTextProperty = labelTextProperty;
		headLineSubTextProperty = labelTextProperty;
		headLineSubSubTextProperty = labelTextProperty;
        


    }
    
    public void initializePrimitives(Hashtable<String, Object> primitives) {
        upperLeftVisualizationArea = (int[])primitives.get("upper left X/Y");
        lowerRightVisualizationArea = (int[])primitives.get("lower right X/Y");
    	
        //Standardwerte
        upperLeftPointVisArea = new Point(upperLeftVisualizationArea[0], upperLeftVisualizationArea[1],"upperLeft");	
    	lowerRightPointVisArea= new Point(lowerRightVisualizationArea[0], lowerRightVisualizationArea[1],"lowerRight");

    	upperLeftBorderVisArea = new Point(upperLeftVisualizationArea[0]+10, upperLeftVisualizationArea[1]+10, "upperLeftBorder");
    	lowerRightBorderVisArea = new Point(lowerRightVisualizationArea[0]-10, lowerRightVisualizationArea[1]-10, "lowerRightBorder");


        k = (Integer)primitives.get("k-Nearest-Neighbors to check");
        distanceMetric = "euclid"; // Standard ist euclid, Abfrage nach Metrik kommt bei jedem Lauf und überschreibt diesen Wert. 
        boolRandomizeClassificationPoint = (boolean)primitives.get("randomize classification point");

        
        if(boolRandomizeClassificationPoint){
        	pointToClassifyX = randomizeCoordinate(upperLeftBorderVisArea.X, lowerRightBorderVisArea.X);
        	pointToClassifyY = randomizeCoordinate(upperLeftBorderVisArea.Y, lowerRightBorderVisArea.Y);
        }
        else {
            pointToClassifyX = (Integer)primitives.get("X");
            pointToClassifyY = (Integer)primitives.get("Y");
        }
        
        //new eine Matrix 0=labels, 1=XCoord, 2=YCoord
        trainingPoints = (String[][])primitives.get("training points");
        boolRandomizePoints = (boolean)primitives.get("randomize training points");
        trainingPointClassesCount = (Integer)primitives.get("training point types count");
        
        if (boolRandomizePoints) {
        	trainingPointCount = (Integer)primitives.get("training point count");
            trainingDataLabels = randomizePointsLabels(trainingPointCount, trainingPointClassesCount);
            trainingDataXCoords = randomizePointCoords(trainingPointCount, upperLeftBorderVisArea.X, lowerRightBorderVisArea.X);
            trainingDataYCoords = randomizePointCoords(trainingPointCount, upperLeftBorderVisArea.Y, lowerRightBorderVisArea.Y);
        }
        else {
        	trainingPointCount = trainingPoints[0].length;
            trainingDataLabels = matrixToLabel(trainingPoints);
            trainingDataXCoords = matrixToXCoord(trainingPoints);
            trainingDataYCoords = matrixToYCoord(trainingPoints);
            trainingPointClassesCount = countTrainingPointClasses();
        }
    }
    
    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
    	//System.out.println("generate method");
    	initializeProperties(props);
        Point[] PointsDATA = makePoints(trainingDataXCoords,trainingDataYCoords,trainingDataLabels);
        //start 
        
        KNN s = new KNN(lang, PointsDATA, k, distanceMetric);
        s.setProperties(labelTextProperty, sourceCodeProperty, classificationCircleProperty, rectVisualisationAreaProperty, distanceArrayProperty, distanceLineProperty, headLineTextProperty, headLineSubTextProperty, headLineSubSubTextProperty);
        s.setPrimitives(new Point(upperLeftPointVisArea.X, upperLeftPointVisArea.Y, upperLeftPointVisArea.type), new Point(lowerRightPointVisArea.X, lowerRightPointVisArea.Y,lowerRightPointVisArea.type), trainingPointClassesCount);
        setClassficationRules(s);

		s.classify(new Point(pointToClassifyX,pointToClassifyY,"p"));

		//lang.finalizeGeneration(); // wofür ?
		
        return lang.toString();
    }
    
    private Point[] makePoints(int[] trainX, int[] trainY, String[] trainLabels){
    	int length = trainX.length;
    	if (trainY.length != length || trainLabels.length != length)
    		return null;
    	Point[] out = new Point[length];
    	for(int i = 0; i<length; i++){
    		//if(isValid(trainX[i], trainY[i], trainLabels[i]))
    		out[i] = new Point(trainX[i], trainY[i], trainLabels[i]);
    	}
    	return out;
    }
    
    @Override
    public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
    	//System.out.println("validateInput method");
    	String errorMessage;
    	boolean boolError = false; // immer ! wirft exception wenn was nicht in ordnung ist.
    	initializePrimitives(primitives);
    	
    	// immer nach Metric fragen
    	//do {
    		distanceMetric = (String) JOptionPane.showInputDialog(null, "Choose distance metric", "The distance metric parameter", 
    				JOptionPane.QUESTION_MESSAGE, null, KNN.METRICS, KNN.METRICS[0]);
    		
    	//}while(!Arrays.asList(KNN.METRICS).contains(distanceMetric));
    	
    	
        if (!boolRandomizePoints) {
        	// prüfe ob Punkte innerhalb der Grenzen liegen
            for (int i=0; i<trainingPointCount; i++) {
        		while((trainingDataXCoords[i]<(upperLeftBorderVisArea.X)) || (trainingDataXCoords[i]>(lowerRightBorderVisArea.X))) {
    				errorMessage = "ERROR: training data point " + String.valueOf(i+1) + ": X coordinate has to be between " + String.valueOf(upperLeftBorderVisArea.X) + " and " + String.valueOf(lowerRightBorderVisArea.X) + ".\n";
    				errorMessage = errorMessage.concat("Please correct your X coordinate: ");
    				trainingDataXCoords[i] = correctInvalidIntegerProperty(errorMessage, String.valueOf(trainingDataXCoords[i]));
        		}
        		while((trainingDataYCoords[i]<(upperLeftBorderVisArea.Y)) || (trainingDataYCoords[i]>(lowerRightBorderVisArea.Y))) {
    				errorMessage = "ERROR: training data point " + String.valueOf(i+1) + ": Y coordinate has to be between " + String.valueOf(upperLeftBorderVisArea.Y) + " and " + String.valueOf(lowerRightBorderVisArea.Y) + ".\n";
    				errorMessage = errorMessage.concat("Please correct your Y coordinate: ");
    				trainingDataYCoords[i] = correctInvalidIntegerProperty(errorMessage, String.valueOf(trainingDataYCoords[i]));
        		}
            }
        }
        
        // prüfe k, k>=1 und k<=trainingPointCount
        // k = (Integer)primitives.get("k-Nearest-Neighbors to check");
		while(k<1 || k>trainingPointCount || (k%2==0)) {
        	errorMessage = "ERROR: k has to be uneven, between 1 and the count of the training points (" + String.valueOf(trainingPointCount) + ").\n";
			errorMessage = errorMessage.concat("Please correct your value of k-next-neighbours: ");
			k = correctInvalidIntegerProperty(errorMessage, String.valueOf(k));
		}
 
   		while((pointToClassifyX < (upperLeftBorderVisArea.X)) || (pointToClassifyX>(lowerRightBorderVisArea.X))) {
			errorMessage = "ERROR: classification point: X-coordinate has to be between " + String.valueOf(upperLeftBorderVisArea.X) + " and " + String.valueOf(lowerRightBorderVisArea.X) + ".\n";
			errorMessage = errorMessage.concat("Please correct your value of point to classify X-coordinate : ");
			pointToClassifyX = (correctInvalidIntegerProperty(errorMessage, String.valueOf(pointToClassifyX)));
			
		}
		while((pointToClassifyY<(upperLeftBorderVisArea.Y)) || (pointToClassifyY>(lowerRightBorderVisArea.Y))) {
    		errorMessage = "ERROR: classification point: Y-coordinate has to be between " + String.valueOf(upperLeftBorderVisArea.Y) + " and " + String.valueOf(lowerRightBorderVisArea.Y) + ".\n";
			errorMessage = errorMessage.concat("Please correct your value of point to classify Y-coordinate : ");
			pointToClassifyY = (correctInvalidIntegerProperty(errorMessage, String.valueOf(pointToClassifyY)));
		}

        
        // prüfe random anzahl typen max 25
		while(boolRandomizePoints && trainingPointClassesCount>25) {
        	errorMessage = "ERROR: with activated randomization of training points, training point types count has to be smaller than 26 (Alphabet).\n";
        	errorMessage = errorMessage.concat("Please correct your value of training point types: ");
			trainingPointClassesCount = (correctInvalidIntegerProperty(errorMessage, String.valueOf(trainingPointClassesCount)));
		}
		
		while(boolRandomizePoints && trainingPointClassesCount>=trainingPointCount) {
        	errorMessage = "ERROR: with activated randomization of training points, training point types count hast to be smaller than training point count.\n";
        	errorMessage = errorMessage.concat("Training points count is " + String.valueOf(trainingPointCount) + ".\n");
        	errorMessage = errorMessage.concat("Please correct your value of training point types or training points (in the next input window): ");
			trainingPointClassesCount = (correctInvalidIntegerProperty(errorMessage, String.valueOf(trainingPointClassesCount)));

			errorMessage = "ERROR: with activated randomization of training points, training point types count hast to be smaller than training point count.\n";
        	errorMessage = errorMessage.concat("Training point types count is " + String.valueOf(trainingPointCount) + ".\n");
        	errorMessage = errorMessage.concat("Please correct your value of training points: ");
			trainingPointCount = (correctInvalidIntegerProperty(errorMessage, String.valueOf(trainingPointCount)));
		}


		if(!boolRandomizePoints){
			boolean correctedValue = false;
			for(int i=0; i<trainingPointCount; i++) {
				while(trainingDataLabels[i].length()!=1) {
		        	errorMessage = "ERROR: training points labels minimum and maximum is one character.\n";
		        	errorMessage = errorMessage.concat("Training point label is " + String.valueOf(trainingDataLabels[i]) + ".\n");
		        	errorMessage = errorMessage.concat("Please correct your value of the training point label: ");
		        	trainingDataLabels[i] = correctInvalidStringProperty(errorMessage, String.valueOf(trainingDataLabels[i]));
		        	correctedValue = true;
				}
			}
			if(correctedValue)
				trainingPointClassesCount= countTrainingPointClasses();
		}

    	// es gibt keine Fehler mehr, da nicht mehr abgebrochen werden kann, außer per Exception (abbrechen button führt auch zu Exception...
    	return !boolError;
    }
    
    // returns the count of different label types / classes
	private int countTrainingPointClasses() {
		HashSet<String> tempNoDuplicateLabels = new HashSet<String>();
		for(int i=0; i<trainingDataLabels.length; i++)
			tempNoDuplicateLabels.add(trainingDataLabels[i]);
		System.out.println("points:" + trainingDataLabels.length + "; classes:" + tempNoDuplicateLabels.size());
		return tempNoDuplicateLabels.size();
	}

	private String[] matrixToLabel(String[][] matrix){
    	int size = matrix[0].length;
    	String[] pointLabels = new String[size];
    	for (int i = 0; i < size; i++) {
    		pointLabels[i] = matrix[0][i];
		}
    	return pointLabels;
    }

    private int[] matrixToXCoord(String[][] matrix){
    	int size = matrix[1].length;
    	int [] pointData = new int[size];
    	for (int i = 0; i < size; i++) {
    		pointData[i] = Integer.parseInt(matrix[1][i]);
		}
    	return pointData;
    }

    private int[] matrixToYCoord(String[][] matrix){
    	int size = matrix[2].length;
    	int [] pointData = new int[size];
    	for (int i = 0; i < size; i++) {
    		pointData[i] = Integer.parseInt(matrix[2][i]);
		}
    	return pointData;
    }
    
    private String[] randomizePointsLabels(int size, int differentTypesCount) {
    	String[] labels = new String[size];
    	char startChar = 'A';
    	Random r = new Random();

    	for (int i = 0; i<size;i++) {
    		labels[i] = String.valueOf((char)(r.nextInt(differentTypesCount) + startChar));
    	}
    	return labels;
    }
    
    private int[] randomizePointCoords(int size, int from, int to) {
    	int[] coords = new int[size];
    	
    	for (int i = 0; i<size;i++) {
    		coords[i] = randomizeCoordinate(from, to);
    	}
    	
    	return coords;
    }
    
    private int randomizeCoordinate(int from, int to) {
    	Random r = new Random();
    	return r.nextInt(to-from) + from;
    }
    
    private void showErrorWindow(String message) {
    	//System.out.println("Aufruf showErrorWindow mit message: " + message);
		JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), message, "Fehler", JOptionPane.ERROR_MESSAGE);
	}
    
    // Dialog to correct integer value
    private int correctInvalidIntegerProperty(String messageText, String initialValue) throws IllegalArgumentException {
    	String input = JOptionPane.showInputDialog(null, messageText, initialValue);
    	int returnvalue = Integer.parseInt(initialValue);
    	if (input==null) {
    		//user abbruch
    		throw new IllegalArgumentException("User Abbruch, null Eingabe!");
    	}
    	else {
			try {
				returnvalue = Integer.parseInt(input);
			}
			catch ( NumberFormatException e )
			{
				String message = String.valueOf(input) + " kann man nicht in eine Zahl konvertieren!"; 
				System.err.println(message);
				showErrorWindow(message);
			}
    	}

    	return returnvalue;
    }

    // Dialog to correct integer value
    private String correctInvalidStringProperty(String messageText, String initialValue) throws IllegalArgumentException {
    	String returnvalue = JOptionPane.showInputDialog(null, messageText, initialValue);
    	return returnvalue;
    }
    
    public abstract void setClassficationRules(KNN s);
    
    public abstract String getName(); 

    public String getAlgorithmName() {
        return "k-Nearest-Neighbor Classifier";
    }

    public String getAnimationAuthor() {
        return "Jan Rehbein, Marius Rettberg-Päplow";
    }

    public abstract String getDescription();

    public abstract String getCodeExample();
    
    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

}