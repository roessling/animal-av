package generators.maths.gerschgorin;

import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import generators.maths.gerschgorin.coordinatesystem.Circle;
import generators.maths.gerschgorin.coordinatesystem.CoordinateSystem;
import generators.maths.gerschgorin.coordinatesystem.CoordinateSystemConfig;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Renders the animation for a gerschgorin circle.
 * @author Jannis Weil, Hendrik Wuerz
 */
public class Gerschgorin {

    private static final String TITLE = "Gerschgorin-Kreise";

    private static final String DESCRIPTION = "" +
            "Mit Hilfe von Gerschgorin-Kreisen können Eigenwerte von quadratischen Matrizen eingegrenzt werden. \n" +
            "Dafür wird sowohl für die Originalmatrix, wie auch die Transponsierte Matrix berechnet, in welchem \n" +
            "Bereich ein Eigenwert liegen muss. Die finale Eingrenzung ergibt sich aus der Schnittmenge der Werte \n" +
            "aus der originalen sowie der transponierten Matrix. \n" +
            " \n" +
            "Für die Eingrenzung bei einer Matrix muss jeder Eigenwert um das Diagonalelement herum liegen. Der \n" +
            "Radius in dem dies möglich ist, ergibt sich aus der Summe der Beträge der restlichen Wert in der \n" +
            "entsprechenden Matrix Zeile. \n" +
            " \n" +
            "Wenn sich im Ergebnis zwei oder mehr Kreise überlappen, dann lassen sich die Eigenwerte nur auf die \n" +
            "Gesamtfläche eingrenzen. Es ist dabei nicht möglich spezifischere Aussagen über die Position der \n" +
            "einzelnen Eigenwerte zu treffen.";


    /**
     * The pseudo source code
     */
    private static final String[] SOURCE_CODE = new String[]{
            "verarbeiteMatrix(matix) {",
            "    foreach(row in matrix) {",
            "        Zeichne den Kreis-Mittelpunk an der Stelle des Diagonalelements",
            "        foreach(column in row) {",
            "            Erhöhe Kreisradius um den Elementwert",
            "        }",
            "    }",
            "}",
            "verarbeiteMatrix(originalMatrix);",
            "verarbeiteMatrix(transponierteMatrix);",
            "foreach(row in matrix) {",
            "    finalerRadius = Min(KreisAusOriginalMatrix, KreisAusTransponierterMatrix)",
            "}",
            "lokalisiereEigenwerte()"};

    /**
     * The animal object of the displayed pseudo source code
     */
    private static SourceCode sourceCode;

    /**
     * default duration for swap processes
     */
    private final static Timing DEFAULT_DURATION = new TicksTiming(250);

    /**
     * The concrete language object used for creating output
     */
    private Language lang;

    /**
     * The headline in the animation window
     */
    private Text headline;

    /**
     * The index of the currently highlighted line.
     */
    private int currentLineHighlighting = -1;

    /**
     * Animate the passed gerschgorin circle
     *
     * @param l the concrete language object used for creating output
     * @param matrix The matrix whose gerschgorin circles should be calculated
     */
    public Gerschgorin(Language l, int[][] matrix) {
        this(l, mapIntToDouble(matrix));
    }

    /**
     * Maps the passed int matrix to a double matrix.
     * The matrix must be valid (not null, min one entry)
     * @param intMatrix The int matrix which should be converted
     * @return A double matrix with the same values as the int matrix
     */
    private static double[][] mapIntToDouble(int[][] intMatrix) {
        double[][] doubleMatrix = new double[intMatrix.length][intMatrix[0].length];
        IntStream.range(0, intMatrix.length)
                .forEach(row -> IntStream
                        .range(0, intMatrix[row].length)
                        .forEach(column -> doubleMatrix[row][column] = intMatrix[row][column]));
        return doubleMatrix;
    }

    /**
     * Animate the passed gerschgorin circle
     *
     * @param l the concrete language object used for creating output
     * @param matrix The matrix whose gerschgorin circles should be calculated
     */
    public Gerschgorin(Language l, double[][] matrix) {

        // Store the language object
        lang = l;
        // This initializes the step mode. Each pair of subsequent steps has to
        // be divided by a call of lang.nextStep();
        lang.setStepMode(true);

        // Enable question module
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

        // init animation objects
        addHeadline();
        renderIntroduction();
        addSourceCode();

        // Original matrix
        highlight(8); // Highlight init method call for original matrix
        ParsingResult resultOriginal = parse(matrix, 0, "normal", new Color(34,139,34), "Verarbeite originale Matrix");

        lang.nextStep();

        // Transposed matrix
        Matrix.transpose(matrix);
        highlight(9); // Highlight init method call for transposed matrix
        ParsingResult resultTransposed = parse(matrix, 240, "transposed", Color.BLUE, "Verarbeite transponierte Matrix");

        // Merge circles
        highlight(10); // Highlight init method call for transposed matrix
        Circle[][] circles = new Circle[][] { // [FromWhichMatrix][FromWhichRow]
            resultOriginal.getCircles(),
            resultTransposed.getCircles()
        };
        double[] requiredDimension = getRequiredDimension(matrix);
        MergingResult resultMerging = mergeCircleInformation(circles, requiredDimension);
        calculateEigenvalues(resultMerging);

        // Build final page
        lang.nextStep();
        renderSummary(resultOriginal, resultTransposed, resultMerging);

        lang.finalizeGeneration();
    }

    /**
     * Adds the headline Text to the animation window
     */
    private void addHeadline() {
        Font fontHeadline = new Font(Font.SERIF, Font.BOLD, 18);
        TextProperties propHeadline = new TextProperties();
        propHeadline.set("font", fontHeadline);
        headline = lang.newText(new Coordinates(20, 20), TITLE, "headline", null, propHeadline);
    }

    /**
     * Add the pseudo source code to the animation window
     */
    private void addSourceCode() {
        SourceCodeProperties properties = new SourceCodeProperties();
        properties.set("highlightColor", new Color(255, 100, 150));
        sourceCode = lang.newSourceCode(
                new Offset(0, 0, headline, "SW"),
                "SourceCode",
                null, properties);

        sourceCode.addMultilineCode(getAlgorithmCode(), "SourceCode_line", null);
    }

    /**
     * Add the introduction to the animation
     */
    private void renderIntroduction() {
        SourceCode descriptionSourceCode = lang.newSourceCode(
                new Offset(0, 0, headline, "SW"),
                "description",
                null,
                new SourceCodeProperties());
        descriptionSourceCode.addMultilineCode(DESCRIPTION, "introduction", null);
        lang.nextStep("Einführung");
        descriptionSourceCode.hide();
    }

    /**
     * Highlights the passed line of the source code and removes the highlighting for the previous marked line.
     * @param index The line index to be highlighted
     */
    private void highlight(int index) {
        if(currentLineHighlighting >= 0) {
            sourceCode.unhighlight(currentLineHighlighting);
        }
        currentLineHighlighting = index;
        sourceCode.highlight(currentLineHighlighting);
    }

    /**
     * Generates the gerschgorin circles for the passed matrix
     *
     * @param matrixValues The matrix from which the circles are generated
     * @param yOffset The y Offset to place the output
     * @param prefix The prefix used to identify generated objects
     * @param highlighting The color used for this matrix
     * @param tableOfContentsName The name of this step to be printed in the table of contents list
     */
    private ParsingResult parse(double[][] matrixValues, int yOffset, String prefix, Color highlighting, String tableOfContentsName) {

        // There have to be elements and the matrix is square (#rows == #columns)
        if(matrixValues.length == 0 || matrixValues[0].length != matrixValues.length) {
            throw new RuntimeException("Only a square matrix is supported");
        }

        Circle[] circles = new Circle[matrixValues.length];

        // DRAW MATRIX
        MatrixProperties matrixProperties = new MatrixProperties("matrixProperties");
        matrixProperties.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
        matrixProperties.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
        matrixProperties.set(AnimationPropertiesKeys.GRID_BORDER_COLOR_PROPERTY, highlighting);

        int matrixX = 20;
        int matrixY = yOffset + 320;
        int matrixCellSize = 32; // This value is based on experiments. There is no method to get exact data
        Matrix matrix = new Matrix(lang.newDoubleMatrix(
                new Coordinates(matrixX, matrixY),
                matrixValues,
                prefix + "Matrix",
                null,
                matrixProperties));

        // DRAW CIRCLE RADIUS MATRIX
        DoubleMatrix radiusMatrix = lang.newDoubleMatrix(
                new Offset(10, 0, matrix.getAnimalMatrix(), "NE"),
                new double[matrixValues[0].length][1],
                "radiusMatrix",
                null,
                matrixProperties);
        Text radiusMatrixText = lang.newText(new Offset(-20, -17, radiusMatrix, "N"),
                "Radius", prefix + "RadiusMatrix", null);


        // DRAW COORDINATE SYSTEM
        int coordinateSystemX = matrixX + matrixValues[0].length * matrixCellSize + 100;
        double[] requiredDimension = getRequiredDimension(matrixValues);
        CoordinateSystemConfig config = new CoordinateSystemConfig(requiredDimension[0], requiredDimension[1], coordinateSystemX, matrixY, 300, 200, prefix + "coordinateSystem");
        CoordinateSystem coordinateSystem = new CoordinateSystem(lang, config);

        lang.nextStep(tableOfContentsName);

        for(int y = 0; y < matrixValues[0].length; y++) { // loop matrix rows (= circles)

            // Set main focus to diagonal element
            matrix.setMainFocus(y, y);
            highlight(2);
            matrix.setSubFocus(-1, -1);
            Circle circle = coordinateSystem.drawCircle(matrixValues[y][y], 0, 0, prefix + "Circle" + y);
            circle.setRadiusMatrix(radiusMatrix, y);
            circle.highlightRadius(Matrix.SUB_FOCUS_COLOR);

            // Add question for calculated radius in a row
            if(y == 0 && yOffset == 0) { // Only display question for the first iteration and the first matrix
                MultipleChoiceQuestionModel radiusQuestion = new MultipleChoiceQuestionModel("calculateRadiusQuestion");
                final int row = y;
                List<Double> otherValues = IntStream.range(0, matrixValues.length)
                        .filter(column -> column != row)
                        .mapToObj(column -> matrixValues[row][column])
                        .collect(Collectors.toList());
                String otherValuesString = otherValues.stream().map(String::valueOf).collect(Collectors.joining(" "));
                double sum = otherValues.stream().mapToDouble(Double::doubleValue).map(Math::abs).sum();
                double min = otherValues.stream().mapToDouble(Double::doubleValue).map(Math::abs).min().orElse(0);
                double max = otherValues.stream().mapToDouble(Double::doubleValue).map(Math::abs).max().orElse(99);
                radiusQuestion.setPrompt("Wie groß ist der Radius für den Kreis, wenn das Diagonalelement der Matrix den Wert " +
                        matrixValues[y][y] + " hat und ansonsten die folgenden Werte in der Zeile stehen: " + otherValuesString + "?");
                String feedbackSuffix = "Für den Radius werden alle Werte der Zeile bis auf das Diagonalelement betragsmäßig addiert.";
                if (min != sum) { // Answer is not equal to correct solution
                    radiusQuestion.addAnswer("calculateRadiusQuestionAnswer1", String.valueOf(min), 0, "Leider falsch. " + feedbackSuffix);
                }
                radiusQuestion.addAnswer("calculateRadiusQuestionAnswer2", String.valueOf(sum), 0, "Richtig! " + feedbackSuffix);
                if (max != sum) { // Answer is not equal to correct solution
                    radiusQuestion.addAnswer("calculateRadiusQuestionAnswer3", String.valueOf(max), 0, "Leider falsch. " + feedbackSuffix);
                }
                radiusQuestion.addAnswer("calculateRadiusQuestionAnswer4", String.valueOf(sum + matrixValues[row][row]), 0, "Leider falsch. " + feedbackSuffix);
                lang.addMCQuestion(radiusQuestion);
            }

            lang.nextStep();

            for(int x = 0; x < matrixValues.length; x++) { // loop columns in the current row
                if(x == y) continue; // Skip elements on diagonal line

                matrix.setSubFocus(x, y);
                highlight(4);

                // Animate Text movement
                Text text = lang.newText(
                        new Coordinates(matrixX + x * matrixCellSize, matrixY + y * matrixCellSize),
                        String.valueOf(matrixValues[y][x]),
                        prefix + "Text" + x + "_" + y,
                        null);
                text.moveTo("C",
                        "translate",
                        circle.getRealPosition(),
                        Timing.INSTANTEOUS,
                        DEFAULT_DURATION);
                text.hide(DEFAULT_DURATION);

                // Increase circle radius
                double radius = circle.getRadius() + Math.abs(matrixValues[y][x]);
                circle.resize(radius, DEFAULT_DURATION);
                radiusMatrix.put(y, 0, radius, DEFAULT_DURATION, Timing.INSTANTEOUS);

                // Not the last step
                if(x < matrixValues.length - 1 || y < matrixValues[0].length - 1) {
                    lang.nextStep();
                }
            }

            circle.fillColor(highlighting);
            circle.unHighlightRadius();
            circles[y] = circle;
        }

        matrix.setMainFocus(-1, -1);
        matrix.setSubFocus(-1, -1);

        return new ParsingResult(circles, matrix, radiusMatrix, radiusMatrixText, coordinateSystem);

    }

    /**
     * Merges the passed circles together in one coordinate system
     * @param circles Array:
     *                First dimension: 0 or 1 as the two circles which should be merged
     *                Second dimension: Index of circle (length is normally equal to the row count of the matrix
     * @param requiredDimensions The needed size of the new coordinate system.
     */
    private MergingResult mergeCircleInformation(Circle[][] circles, double[] requiredDimensions) {

        if(circles.length != 2) throw new RuntimeException("There have to be exactly two sets of circles");

        if(circles[0].length != circles[1].length) throw new RuntimeException("Each set of circles has to have the same length");

        MergingResult result = new MergingResult();

        CoordinateSystemConfig config = new CoordinateSystemConfig(requiredDimensions[0], requiredDimensions[1], 620, 440, 300, 200, "mergedCoordinateSystem");
        CoordinateSystem coordinateSystem = new CoordinateSystem(lang, config);
        result.setCoordinateSystem(coordinateSystem);

        result.initMergingInfo(lang.newText(new Offset(40, 0, sourceCode, "NE"),
                "Radius der Kreise nach der Vereinigung", "mergedRadiusInfo", null));

        lang.nextStep("Vereinige Kreise");

        int circleCount = circles[0].length;
        Circle[] merged = new Circle[circleCount];

        // Get circle index for questionger
        OptionalInt questionCircleIndex = IntStream.range(0, circles[0].length)
                .filter(i -> circles[0][i].getRadius() != circles[1][i].getRadius())
                .findAny();

        for(int i = 0; i < circleCount; i++) {

            // Ask question
            if (questionCircleIndex.isPresent() && questionCircleIndex.getAsInt() == i) { // There is a pair with different circle radius
                Circle originalCircle = circles[0][questionCircleIndex.getAsInt()];
                Circle transposedCircle = circles[1][questionCircleIndex.getAsInt()];

                MultipleChoiceQuestionModel mergedRadiusQuestion = new MultipleChoiceQuestionModel("mergedRadiusQuestion");
                mergedRadiusQuestion.addAnswer("Answer1", String.valueOf(Math.min(originalCircle.getRadius(), transposedCircle.getRadius())), 0, "Richtig! Der Radius ergibt sich aus dem Minimum der beiden Werte");
                mergedRadiusQuestion.addAnswer("Answer2", String.valueOf(Math.max(originalCircle.getRadius(), transposedCircle.getRadius())), 0, "Leider falsch. Der Eigenwert muss in beiden Kreisen liegen, weshalb das Minimum korrekt ist.");
                mergedRadiusQuestion.setPrompt("Wie groß ist der Radius des vereinigten Kreises," +
                        " wenn der Kreis aus der originalen Matrix den Radius " + originalCircle.getRadius() +
                        " und der Kreis aus der transponierten Matrix den Radius " + transposedCircle.getRadius() + " hat?");
                lang.addMCQuestion(mergedRadiusQuestion);
                lang.nextStep();
            }

            // The bigger circle must move first
            int biggerCircle = circles[0][i].getRadius() > circles[1][i].getRadius() ? 0 : 1;
            Circle first = circles[biggerCircle][i].duplicate();
            Circle second = circles[(biggerCircle + 1) % 2][i].duplicate();

            if(first.getX() != second.getX() || first.getY() != second.getY()) {
                throw new RuntimeException("Centers of circle " + i + " are not equal");
            }

            highlight(11); // Highlight init method call for transposed matrix

            first.highlightRadius(Matrix.SUB_FOCUS_COLOR);
            second.highlightRadius(Matrix.SUB_FOCUS_COLOR);
            first.moveTo(config, DEFAULT_DURATION);
            second.moveTo(config, DEFAULT_DURATION);

            lang.nextStep();
            double mergedRadius = Math.min(first.getRadius(), second.getRadius());
            first.unHighlightRadius();
            second.highlightRadius(Matrix.MAIN_FOCUS_COLOR);
            first.hide(Timing.INSTANTEOUS);
            second.hide(Timing.INSTANTEOUS);
            result.addMergingInfo(lang, "Kreis " + (i+1) + ": Mittelpunkt = (" + first.getX() + "|" + first.getY() + ") Radius = " + mergedRadius);

            merged[i] = coordinateSystem.drawCircle(first.getX(), first.getY(), mergedRadius, "mergeCircle" + i, false);
            merged[i].setIdentifier(i);
            merged[i].fillColor(Color.cyan);
            merged[i].show(Timing.INSTANTEOUS);
            if(i < circleCount - 1) { // This is not the last iteration
                lang.nextStep();
            }
        }

        result.setCircles(merged);
        return result;
    }

    /**
     * Calculates the position of the eigenvalues of the passed circles and prints the result in the animation window.
     * @param mergingResult The result from the merging. This is needed for the circles and the last merging info position.
     */
    private void calculateEigenvalues(MergingResult mergingResult) {

        mergingResult.addMergingInfo(lang, "Position der Eigenwerte");
        highlight(13); // Highlight localisation of eigenvalues method

        lang.nextStep("Lokalisiere Eigenwerte");

        LinkedList<LinkedList<Circle>> groups = generateEigenvalueGroups(mergingResult.getCircles());
        groups.forEach(group -> {
            int amountOfEigenvalues = group.size();
            String listOfCircles = group.stream()
                    .map(Circle::getIdentifier) // The identifier (= index) of the circle
                    .map(i -> i + 1) // Increase because output starts at 1 and not at 0
                    .map(String::valueOf) // toString because collector can only handle String input
                    .collect(Collectors.joining(", "));
            String text;
            if(amountOfEigenvalues == 1) { // Singular
                text = amountOfEigenvalues + " Eigenwert liegt in dem Kreis " + listOfCircles;
            } else { // Plural
                listOfCircles = listOfCircles.substring(0, listOfCircles.lastIndexOf(", "))
                        + listOfCircles.substring(listOfCircles.lastIndexOf(", ")).replace(", ", " und ");
                text = amountOfEigenvalues + " Eigenwerte liegen in den Kreisen " + listOfCircles;
            }
            mergingResult.addMergingInfo(lang, text);
        });
    }

    /**
     * Generates a list of eigenvalue groups.
     * Each entry in the outer list represents a set of eigenvalues, build upon the circles in the inner list.
     * This means each set of eigenvalues contains exactly as many eigenvalues as the inner list has entries.
     * @param circles The circles upon which the eigenvalues should be located.
     * @return A list of eigenvalue sets.
     */
    private LinkedList<LinkedList<Circle>> generateEigenvalueGroups(Circle[] circles) {

        LinkedList<LinkedList<Circle>> groups = new LinkedList<>();

        // Init groups: Each circle is one group
        Arrays.stream(circles).forEach(circle -> groups.add(new LinkedList<>(Collections.singletonList(circle))));

        // Merge groups as long as something changes
        boolean changed = true;
        while (changed) {
            changed = groups.stream()
                    // Look at each group
                    .anyMatch(l1 -> groups.stream()
                            // Look at each other group and merge them if possible
                            .anyMatch(l2 -> mergeIfPossible(l1, l2)));
        }

        // Clean up empty groups (After merges, the second list becomes cleared --> remove those)
        LinkedList<LinkedList<Circle>> finalGroups = new LinkedList<>();
        groups.stream()
                .filter(group -> !group.isEmpty())
                .forEach(finalGroups::add);
        return finalGroups;
    }

    /**
     * Merges the passed lists if the set of circles touches each other.
     * @param l1 The first list of circles
     * @param l2 The second list of circles
     * @return True if a merge was performed, false otherwise
     */
    private boolean mergeIfPossible(LinkedList<Circle> l1, LinkedList<Circle> l2) {
        if(canBeMerged(l1, l2)) {
            l1.addAll(new LinkedList<>(l2));
            l2.clear();
            return true;
        }
        return false;
    }

    /**
     * Checks whether the passed lists of circles can be merged.
     * A merge is possible if there is a circle in l1 which touches a circle of l2
     * @param l1 The first list of circles
     * @param l2 The second list of circles
     * @return True if merge is possible, false otherwise
     */
    private boolean canBeMerged(LinkedList<Circle> l1, LinkedList<Circle> l2) {
        return !l1.equals(l2) && // The lists must not be equal
                l1.stream() // Look at all circles in the first list
                .anyMatch(circle -> { // Has this circle a candidate in the other list which overlaps?
                    return l2.stream().anyMatch(circle::touches);
                });
    }

    /**
     * Calculates the required coordinate system dimensions for the passed matrix.
     * @param matrixValues The matrix on which the gershgorin circles are calculated
     * @return A double array width result[0] == width and result[1] == height
     */
    private double[] getRequiredDimension(double[][] matrixValues) {
        double[] original = getRequiredDimensionForMatrix(matrixValues);
        Matrix.transpose(matrixValues);
        double[] transposed = getRequiredDimensionForMatrix(matrixValues);
        Matrix.transpose(matrixValues); // roll back changes

        // Use the maximum from original and transposed
        return new double[]{
                Math.max(original[0], transposed[0]),
                Math.max(original[1], transposed[1])
        };
    }

    /**
     * Calculates the max values for the passed matrix.
     * @param matrixValues The matrix which should be used
     * @return A double array width result[0] == width and result[1] == height
     */
    private double[] getRequiredDimensionForMatrix(double[][] matrixValues) {

        OptionalDouble maxRadius = IntStream.range(0, matrixValues.length)
                .mapToDouble(row -> // Iterate rows
                        IntStream.range(0, matrixValues.length) // Iterate columns
                                .filter(column -> column != row) // exclude diagonal elements
                                .mapToDouble(column -> matrixValues[row][column]) // get the matrix values
                                .sum() // sum up in this row
                )
                .max();// Get max circle radius over all rows
        double height = maxRadius.orElse(1.0);

        OptionalDouble maxPosition = IntStream.range(0, matrixValues.length)
                .mapToDouble(element -> matrixValues[element][element])
                .max();
        double width = maxPosition.orElse(1.0) + height; // Add radius height to required width to avoid overflow with the circle

        return new double[]{width, height};
    }

    /**
     * Renders the summary at the end of the animation.
     * This will hide the matrices and move the diagrams in a grid layout.
     * @param resultOriginal The result from parsing the original matrix.
     * @param resultTransposed The result from parsing the transposed matrix.
     * @param mergingResult The result from the merging of the two diagrams.
     */
    private void renderSummary(ParsingResult resultOriginal, ParsingResult resultTransposed, MergingResult mergingResult) {
        // Hide objects
        sourceCode.hide();

        resultOriginal.getMatrix().getAnimalMatrix().hide();
        resultOriginal.getRadiusMatrix().hide();
        resultOriginal.getRadiusMatrixText().hide();

        resultTransposed.getMatrix().getAnimalMatrix().hide();
        resultTransposed.getRadiusMatrix().hide();
        resultTransposed.getRadiusMatrixText().hide();

        // Move objects
        resultOriginal.getCoordinateSystem().moveTo(new Coordinates(40, 60), DEFAULT_DURATION);
        resultTransposed.getCoordinateSystem().moveTo(new Coordinates(40, 310), DEFAULT_DURATION);
        mergingResult.getCoordinateSystem().moveTo(new Coordinates(410, 310), DEFAULT_DURATION);

        int mergingInfoX = 410;
        int mergingInfoY = 60;
        int mergingInfoLineHeight = 20;
        LinkedList<Text> mergingInfos = mergingResult.getMergingInfos();
        IntStream.range(0, mergingInfos.size())
                .forEach(index -> mergingInfos.get(index).moveTo("C", "translate",
                        new Coordinates(mergingInfoX, mergingInfoY + index * mergingInfoLineHeight),
                        Timing.INSTANTEOUS, DEFAULT_DURATION));

        lang.nextStep("Ergebnis");
    }

    /**
     * Build the source Code with line breaks based on the SOURCE_CODE field.
     * @return The source code as a String with line breaks.
     */
    private String getAlgorithmCode() {
        return Arrays.stream(SOURCE_CODE).collect(Collectors.joining("\n"));
    }

    public static void main(String[] args) {
        // Create a new language object for generating animation code
        // this requires type, name, author, screen width, screen height
        Language language = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT,
                "Gerschgorin", "Jannis Weil, Hendrik Wuerz", 640, 480);

        double[][] matrix = {
                {1, 0, 0.2, 0},
                {0, 2, 0, -0.1},
                {-0.1, 0, 3, 0},
                {0, 0.1, 0, 4}
        };

        matrix = new double[][]{
                {20, 10, 5},
                {2, 50, 7},
                {10, 0, 60}
        };

        Gerschgorin gerschgorin = new Gerschgorin(language, matrix);

        String code = language.toString();
        System.out.println(code);

        try {
            Files.write(Paths.get("gerschgorin.asu"), code.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
