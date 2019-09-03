package generators.misc.id3_chi_squared;

import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import translator.Translator;

import java.awt.*;
import java.util.List;

/** Chi
 * class to estimate and visualize the chi-squared test
 */
public class Chi {

    private int[][] observedChart;
    private double[][] expectedChart;
    private int[] classCounter;
    private int[] featureCounter;
    private int counter;
    private double threshold;
    private double chi_square;
    private boolean testSuccess;
    private StringMatrix observedSM;
    private StringMatrix expectedSM;
    private Translator translator;

    public Chi(Translator translator){
        this.translator = translator;
    }

    /** squared_test
     * returns the result of the chi-squared test
     *
     * @param data
     * @param numberOfValues
     * @param numberOfClasses
     * @param orderOfValues
     * @param orderOfClasses
     * @param choice index of the attribute
     * @param threshold if chi-squared > threshhold, the test is succesfull
     * @return result of the chi-squared test
     */
    public boolean squared_test(List<Date> data, int numberOfValues, int numberOfClasses, List<String> orderOfValues, List<String> orderOfClasses, int choice, double threshold){

        this.threshold = threshold;

        observedChart = new int[numberOfClasses][numberOfValues];
        expectedChart = new double[numberOfClasses][numberOfValues];
        classCounter = new int[numberOfClasses]; //for each row
        featureCounter = new int[numberOfValues]; //for each col

        createObservedChart(data, numberOfValues, numberOfClasses, orderOfValues, orderOfClasses, choice);
        createExpectedChart(numberOfClasses,numberOfValues);

        chi_square = 0.0;
        for(int i = 0; i < numberOfClasses; i++) {
            for (int j = 0; j < numberOfValues; j++) {
                //chi_square += (expected - observed) * (expected - observed) / expected;
                if(expectedChart[i][j] != 0){
                    chi_square += (expectedChart[i][j] - observedChart[i][j]) * (expectedChart[i][j] - observedChart[i][j]) / expectedChart[i][j];
                }
            }
        }
        testSuccess = chi_square > threshold;
        return testSuccess;
    }

    /** visualizeTest
     *
     * @param orderOfValues
     * @param orderOfClasses
     * @param choice index of attribute
     * @param xCoords x coordinate where the first text is drawn
     * @param yCoords y coordinate where the first text is drawn
     * @param lang
     */
    public void visualizeTest(List<String> orderOfValues, List<String> orderOfClasses, int choice, int xCoords, int yCoords, Language lang){

        TextProperties tp = new TextProperties();
        tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
        Text infoText = lang.newText(new Coordinates(xCoords,yCoords),"Chi Squared Test","null",null,tp);
        yCoords += 30;

        MatrixProperties mp = new MatrixProperties();
        mp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY,"table");
        String[][] observedArray = intToStringMatrix(observedChart,orderOfValues,orderOfClasses,null,null);
        observedArray[0][0] = translator.translateMessage("observed");
        observedSM = lang.newStringMatrix(new Coordinates(xCoords,yCoords),observedArray,"null",null,mp);
        lang.nextStep();
        observedSM.hide();
        observedArray = intToStringMatrix(observedChart,orderOfValues,orderOfClasses,classCounter,featureCounter);
        observedArray[0][0] = translator.translateMessage("observed");
        observedSM = lang.newStringMatrix(new Coordinates(xCoords,yCoords),observedArray,"null",null,mp);
        lang.nextStep();
        yCoords += 25 * (observedChart.length + 1);
        yCoords += 50;
        tp = new TextProperties();
        tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 14));
        Text formula = lang.newText(new Coordinates(xCoords,yCoords),translator.translateMessage("expected_formula"),"null",null,tp);
        yCoords += 30;

        String[][] expectedArray = new String[expectedChart.length+1][expectedChart[0].length+1];
        addBounds(expectedArray,orderOfValues,orderOfClasses);
        for(int i = 0; i < expectedChart.length; i++) {
            for (int j = 0; j < expectedChart[0].length; j++) {
                expectedArray[i+1][j+1] = "(" + classCounter[i] + " * " + featureCounter[j] + ") / " + counter;
            }
        }
        expectedArray[0][0] = translator.translateMessage("expected");
        expectedSM = lang.newStringMatrix(new Coordinates(xCoords,yCoords), expectedArray,"null",null,mp);
        lang.nextStep();

        expectedSM.hide();
        expectedArray = doubleToStringMatrix(expectedChart,orderOfValues,orderOfClasses);
        expectedArray[0][0] = translator.translateMessage("expected");
        expectedSM = lang.newStringMatrix(new Coordinates(xCoords,yCoords), expectedArray,"null",null,mp);
        lang.nextStep();
        yCoords += 30 * (expectedChart.length + 1);
        String estimationString = translator.translateMessage("chi_formula") + " = ";
        estimationString += DoubleToString.doubleToString(chi_square);
        Text estimation = lang.newText(new Coordinates(xCoords,yCoords),estimationString,"null",null,tp);
        lang.nextStep();
        PseudoCodeID3.highlightCode(9);
        if(testSuccess){
            estimationString += " > ";
        }else{
            estimationString += " <= ";
        }
        estimationString += DoubleToString.doubleToString(threshold) + " (" + translator.translateMessage("threshold") + ")";
        estimation.setText(estimationString,null,null);
        lang.nextStep();
        observedSM.hide();
        expectedSM.hide();
        infoText.hide();
        formula.hide();
        estimation.hide();
    }

    /** intToStringMatrix
     * creates an String matrix out of an int matrix and calls addBounds().
     * The function is called to create "observed" table.
     *
     * @param intMatrix
     * @param orderOfValues
     * @param orderOfClasses
     * @param classCounter
     * @param featureCounter
     * @return String matrix
     */
    private String[][] intToStringMatrix(int[][] intMatrix,List<String> orderOfValues,List<String> orderOfClasses, int[] classCounter, int[] featureCounter){
        String[][] stringMatrix;
        if(classCounter == null && featureCounter == null){
            stringMatrix = new String[intMatrix.length+1][intMatrix[0].length+1];
        }
        else{
            stringMatrix = new String[intMatrix.length+2][intMatrix[0].length+2];
            for(int i = 0; i < classCounter.length; i++){
                stringMatrix[i+1][stringMatrix[0].length-1] = classCounter[i] + "";
            }
            for(int i = 0; i < featureCounter.length; i++){
                stringMatrix[stringMatrix.length-1][i+1] = featureCounter[i] + "";
            }
            stringMatrix[intMatrix.length+1][intMatrix[0].length+1] = counter + "";
            stringMatrix[0][intMatrix[0].length+1] = translator.translateMessage("sum");
            stringMatrix[intMatrix.length+1][0] = translator.translateMessage("sum");
        }

        for(int i = 0; i < intMatrix.length; i++){
            for(int j = 0; j < intMatrix[i].length; j++){
                stringMatrix[i+1][j+1] = intMatrix[i][j] + "";
            }
        }
        addBounds(stringMatrix,orderOfValues,orderOfClasses);
        return stringMatrix;
    }

    /** doubleToStringMatrix
     * creates an String matrix out of an double matrix and calls addBounds().
     * The function is called to create "expected" table.
     *
     * @param doubleMatrix
     * @param orderOfValues
     * @param orderOfClasses
     * @return String matrix
     */
    private String[][] doubleToStringMatrix(double[][] doubleMatrix,List<String> orderOfValues,List<String> orderOfClasses){
        String[][] stringMatrix = new String[doubleMatrix.length+1][doubleMatrix[0].length+1];

        for(int i = 0; i < doubleMatrix.length; i++){
            for(int j = 0; j < doubleMatrix[i].length; j++){
                stringMatrix[i+1][j+1] = DoubleToString.doubleToString(doubleMatrix[i][j]);
            }
        }
        addBounds(stringMatrix,orderOfValues,orderOfClasses);
        return stringMatrix;
    }

    /** addBounds
     * is called to add the name of each class at the beginning of each row
     * and the name of each feature at the beginning of each column
     *
     * @param s String matrix
     * @param orderOfValues
     * @param orderOfClasses
     */
    private void addBounds(String[][] s, List<String> orderOfValues,List<String> orderOfClasses){
        //s[0][0] = "";
        for(int i = 0; i < orderOfClasses.size(); i++){
            s[i+1][0] = orderOfClasses.get(i);
        }
        for(int i = 0; i < orderOfValues.size(); i++){
            s[0][i+1] = orderOfValues.get(i);
        }

    }

    /** createObservedChart
     * each cell of the chart contains how many instances of a given data set with feature i is classified as class j.
     * Also the classCounter and featureCounter is updated
     *
     * @param data
     * @param numberOfValues
     * @param numberOfClasses
     * @param orderOfValues
     * @param orderOfClasses
     * @param choice index of the chosen atttribute
     */
    private void createObservedChart(List<Date> data, int numberOfValues, int numberOfClasses, List<String> orderOfValues, List<String> orderOfClasses, int choice){
        for(Date d: data){
            for(int i = 0; i < numberOfValues; i++){
                if(d.getLiterals().get(choice).equals(orderOfValues.get(i))) {
                    for (int j = 0; j < numberOfClasses; j++){
                        if (d.getLabel().equals(orderOfClasses.get(j))) {
                            observedChart[j][i]++;
                            classCounter[j]++;
                            featureCounter[i]++;
                            counter++;
                        }
                    }
                }
            }
        }
    }

    /** createExpectedChart
     *
     * @param numberOfClasses
     * @param numberOfValues
     */
    private void createExpectedChart(int numberOfClasses, int numberOfValues) {
        for(int i = 0; i < numberOfClasses; i++){
            for(int j = 0; j < numberOfValues; j++){
                expectedChart[i][j] = 1.0 * classCounter[i] * featureCounter[j] / counter;
            }
        }
    }
}
