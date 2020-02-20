package generators.searching;

import java.awt.Color;
import java.awt.Font;

import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolygonProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import translator.Translator;

public class QueensAnnealing {

    private Language lang;

    private ChessBoard chessBoard;

    private RectProperties thermometerProps;

    private SourceCodeProperties sourceCodeProps;

    private PolygonProperties chessboardProps;

    private Translator translator;

    public QueensAnnealing(Language lang, RectProperties thermometerProps, SourceCodeProperties sourceCodeProps, PolygonProperties chessboardProps, Translator translator) {
        this.lang = lang;
        if (lang != null) {
            lang.setStepMode(true);
        }
        this.thermometerProps = thermometerProps;
        this.sourceCodeProps = sourceCodeProps;
        this.chessboardProps = chessboardProps;
        this.translator = translator;

    }

    public Solution find_solution(int numberOfQueens, double inittemp, double coolingRate) {
        Text header = null;
        Text subHeader = null;
        if (lang != null) {
            this.addDescription();
            TextProperties headerProps = new TextProperties();
            headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
            headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 50));
            header = lang.newText(new Coordinates(40, 15), translator.translateMessage("header.main1"), "header", null, headerProps);

            headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
            headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 40));
            subHeader = lang.newText(new Coordinates(65, 75), translator.translateMessage("header.sub1"), "header", null, headerProps);
        }
        chessBoard = new ChessBoard(lang, numberOfQueens, chessboardProps, translator);
        chessBoard.init();
        SourceCode sc = null;
        Thermometer thermometer = null;
        if (lang != null) {

            sc = addSourceCode(chessBoard.getUpperLeft().getY(), chessBoard.getUpperLeft().getX() + 50 + chessBoard.getChessFieldSize() * numberOfQueens, numberOfQueens);
            int size = (int) (numberOfQueens * 2.0) + 42;
            thermometer = new Thermometer(lang, new Coordinates(chessBoard.getUpperLeft().getX() + 750 + (chessBoard.getChessFieldSize() + 15) * numberOfQueens, chessBoard.getUpperLeft().getY() + size * 5), size, inittemp, thermometerProps, translator);
            thermometer.drawThermometer();
            lang.nextStep(translator.translateMessage("stepDescription.init"));

            sc.highlight(1);
        }
        //init
        chessBoard.placeQueens();
        if (lang != null) {
            lang.nextStep();
            sc.unhighlight(1);
            sc.highlight(2);
            chessBoard.showCurrentEnergy();
            lang.nextStep();
            sc.unhighlight(2);
        }
        int currentEnergy;
        int iterations = 1;
        double temp = inittemp;
        //loop
        Text iterationText = null;
        if (lang != null)
        {
            TextProperties textProperties = new TextProperties();
            textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
            textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, ((Font)sc.getProperties().get(AnimationPropertiesKeys.FONT_PROPERTY)).getSize()+2));
            iterationText = lang.newText(new Offset(40,-30, sc.getUpperLeft(), "NW"), String.format("Iteration: %d", iterations), "header", null, textProperties);
        }
        while (temp > 0.00001)//(next != null && next.energy < currentSolution)
        {

            if (lang != null) {
                sc.highlight(3);
                lang.nextStep(String.format(translator.translateMessage("stepDescription.iteration"), iterations));
                iterationText.setText(String.format("Iteration: %d", iterations), null, null);
                sc.unhighlight(3);
                sc.highlight(4);
                lang.nextStep();
                sc.unhighlight(4);
            }
            currentEnergy = chessBoard.getCurrentEnergy();
            StateChange randomSolution = getRandomSolution(chessBoard);
            StateChange next = null;
            if (lang != null) {
                chessBoard.highlightState(randomSolution.moveQueenToRow, randomSolution.queenToChange);
                sc.highlight(6);
                lang.nextStep();
                sc.unhighlight(6);

                sc.highlight(7);
                lang.nextStep();

                sc.unhighlight(7);

                sc.highlight(8);
                lang.nextStep();
                sc.unhighlight(8);
            }
            if (randomSolution.energy < currentEnergy && lang != null) {
                sc.highlight(9);
                next = randomSolution;
            } else {
                if (lang != null) {
                    sc.highlight(10);
                    lang.nextStep();
                    sc.unhighlight(10);
                    sc.highlight(11);
                }
                if (acceptanceProbability(currentEnergy, randomSolution.energy, temp) >= Math.random()) {
                    if (lang != null) {

                        sc.highlight(11);
                    }
                    next = randomSolution;
                }
            }
            if (lang != null) {
                chessBoard.unHighlightState();
            }
            if (next != null) {
                chessBoard.changeState(next);
            }
            if (next != null && next.energy == 0) {
                break;
            }

            if (lang != null) {
                lang.nextStep();
                sc.unhighlight(9);
                sc.unhighlight(11);
            }
            iterations++;
            //temp = (coolingRate / (Math.log(iterations + 1)));
            temp = Math.pow(coolingRate, iterations);
            //temp = inittemp - coolingRate * iterations;
            //System.out.println( temp);
            if (lang != null) {
                thermometer.adjustTemperature(temp);
                sc.highlight(12);
                lang.nextStep();
                sc.unhighlight(12);
            }
            //System.out.println(temp);
        }
        if (lang != null) {

            sc.unhighlight(9);
            sc.unhighlight(11);
            sc.highlight(3);
            lang.nextStep(translator.translateMessage("stepDescription.finalIteration"));
            sc.unhighlight(3);
            sc.highlight(4);
            lang.nextStep();
            sc.unhighlight(4);
            sc.highlight(5);
            lang.nextStep();
            thermometer.hide();
            sc.hide();
            chessBoard.hide();
            header.hide();
            subHeader.hide();

            chessBoard.printBoard();
            System.out.println(String.format("Iterations: %d", iterations));
        }
        Solution sol = new Solution(chessBoard.getCurrentEnergy(), iterations);

        return sol;

    }

    private void addDescription() {
        Text header = this.descriptionPageOne();
        this.descriptionPageTwo();
        header.hide();
    }

    private Text descriptionPageOne() {
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 50));
        Text header = lang.newText(new Coordinates(40, 15), translator.translateMessage("header.mainDescription"), "header", null, headerProps);
        lang.nextStep();

        headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 40));
        Text subHeader = lang.newText(new Coordinates(65, 100), translator.translateMessage("header.sub.description1"), "header", null, headerProps);

        headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 20));

        Text description1 = lang.newText(new Offset(0, 20, subHeader, "SW"), translator.translateMessage("description.1.line1"), "header", null, headerProps);
        Text description2 = lang.newText(new Offset(0, 45, subHeader, "SW"), translator.translateMessage("description.1.line2"), "header", null, headerProps);
        Text description3 = lang.newText(new Offset(0, 70, subHeader, "SW"), translator.translateMessage("description.1.line3"), "header", null, headerProps);
        Text description4 = lang.newText(new Offset(0, 95, subHeader, "SW"), translator.translateMessage("description.1.line4"), "header", null, headerProps);
        Text description5 = lang.newText(new Offset(0, 120, subHeader, "SW"), translator.translateMessage("description.1.line5"), "header", null, headerProps);

        headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 40));
        Text subHeader2 = lang.newText(new Offset(0, 180, subHeader, "SW"), translator.translateMessage("header.sub.description2"), "header", null, headerProps);

        headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 20));

        Text description6 = lang.newText(new Offset(0, 20, subHeader2, "SW"), translator.translateMessage("description.2.line1"), "header", null, headerProps);
        Text description7 = lang.newText(new Offset(0, 45, subHeader2, "SW"), translator.translateMessage("description.2.line2"), "header", null, headerProps);
        Text description8 = lang.newText(new Offset(0, 70, subHeader2, "SW"), translator.translateMessage("description.2.line3"), "header", null, headerProps);
        Text description9 = lang.newText(new Offset(0, 95, subHeader2, "SW"), translator.translateMessage("description.2.line4"), "header", null, headerProps);

        lang.nextStep(translator.translateMessage("stepDescription.Description"));
        subHeader.hide();
        subHeader2.hide();
        description1.hide();
        description2.hide();
        description3.hide();
        description4.hide();
        description5.hide();
        description6.hide();
        description7.hide();
        description8.hide();
        description9.hide();
        return header;
    }

    private void descriptionPageTwo() {
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 40));
        Text subHeader = lang.newText(new Coordinates(65, 100), translator.translateMessage("header.sub.description3"), "header", null, headerProps);

        headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 20));

        Text description1 = lang.newText(new Offset(0, 20, subHeader, "SW"), translator.translateMessage("description.3.line1"), "header", null, headerProps);
        Text description2 = lang.newText(new Offset(0, 45, subHeader, "SW"), translator.translateMessage("description.3.line2"), "header", null, headerProps);
        Text description3 = lang.newText(new Offset(0, 70, subHeader, "SW"), translator.translateMessage("description.3.line3"), "header", null, headerProps);
        Text description4 = lang.newText(new Offset(0, 95, subHeader, "SW"), translator.translateMessage("description.3.line4"), "header", null, headerProps);
        Text description5 = lang.newText(new Offset(0, 120, subHeader, "SW"), translator.translateMessage("description.3.line5"), "header", null, headerProps);
        Text description6 = lang.newText(new Offset(0, 145, subHeader, "SW"), translator.translateMessage("description.3.line6"), "header", null, headerProps);

        headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 40));
        Text subHeader2 = lang.newText(new Offset(0, 180, subHeader, "SW"), translator.translateMessage("header.sub.description4"), "header", null, headerProps);

        headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 20));

        Text description7 = lang.newText(new Offset(0, 20, subHeader2, "SW"), translator.translateMessage("description.4.line1"), "header", null, headerProps);
        Text description8 = lang.newText(new Offset(0, 45, subHeader2, "SW"), translator.translateMessage("description.4.line2"), "header", null, headerProps);
        Text description9 = lang.newText(new Offset(0, 70, subHeader2, "SW"), translator.translateMessage("description.4.line3"), "header", null, headerProps);
        Text description10 = lang.newText(new Offset(0, 95, subHeader2, "SW"), translator.translateMessage("description.4.line4"), "header", null, headerProps);
        Text description11 = lang.newText(new Offset(0, 120, subHeader2, "SW"), translator.translateMessage("description.4.line5"), "header", null, headerProps);
        Text description12 = lang.newText(new Offset(0, 145, subHeader2, "SW"), translator.translateMessage("description.4.line6"), "header", null, headerProps);
        Text description13 = lang.newText(new Offset(0, 170, subHeader2, "SW"), translator.translateMessage("description.4.line7"), "header", null, headerProps);
        Text description14 = lang.newText(new Offset(0, 195, subHeader2, "SW"), translator.translateMessage("description.4.line8"), "header", null, headerProps);
        Text description15 = lang.newText(new Offset(0, 220, subHeader2, "SW"), translator.translateMessage("description.4.line9"), "header", null, headerProps);
        Text description16 = lang.newText(new Offset(0, 245, subHeader2, "SW"), translator.translateMessage("description.4.line10"), "header", null, headerProps);


        lang.nextStep();
        subHeader.hide();
        subHeader2.hide();
        description1.hide();
        description2.hide();
        description3.hide();
        description4.hide();
        description5.hide();
        description6.hide();
        description7.hide();
        description8.hide();
        description9.hide();
        description10.hide();
        description11.hide();
        description12.hide();
        description13.hide();
        description14.hide();
        description15.hide();
        description16.hide();
    }

    private SourceCode addSourceCode(int y_coordinate, int x_coordinate, int numberOfQueens) {
        // first, set the visual properties for the source code
        SourceCodeProperties scProps = new SourceCodeProperties();
        scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
        Font font = (Font) sourceCodeProps.get(AnimationPropertiesKeys.FONT_PROPERTY);
        Font finalFont = new Font(font.getFamily(), Font.PLAIN, font.getSize() + numberOfQueens);
        scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, finalFont);
        scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, sourceCodeProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
        scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, sourceCodeProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY));

        // now, create the source code entity
        SourceCode sc = lang.newSourceCode(new Coordinates(x_coordinate, y_coordinate), "sourceCode",
                null, scProps);

        // Add the lines to the SourceCode object.
        // Line, name, indentation, display dealy
        sc.addCodeLine(translator.translateMessage("sourceCode.line0"), null, 0,
                null); // 0
        sc.addCodeLine(translator.translateMessage("sourceCode.line1"), null, 1, null);
        sc.addCodeLine(translator.translateMessage("sourceCode.line2"), null, 1, null);
        sc.addCodeLine(translator.translateMessage("sourceCode.line3"), null, 1, null); // 3
        sc.addCodeLine(translator.translateMessage("sourceCode.line4"), "currentState", 1, null);
        sc.addCodeLine(translator.translateMessage("sourceCode.line5"), null, 3, null); // 5
        sc.addCodeLine(translator.translateMessage("sourceCode.line6"), null, 2, null); // 6
        sc.addCodeLine(translator.translateMessage("sourceCode.line7"), null, 2, null); // 7
        sc.addCodeLine(translator.translateMessage("sourceCode.line8"), null, 2, null); // 8
        sc.addCodeLine(translator.translateMessage("sourceCode.line9"), null, 3, null); // 9
        sc.addCodeLine(translator.translateMessage("sourceCode.line10"), null, 2, null); // 10
        sc.addCodeLine(translator.translateMessage("sourceCode.line11"), null, 3, null); // 11
        sc.addCodeLine(translator.translateMessage("sourceCode.line12"), null, 2, null); // 12

        return sc;
    }

    private double acceptanceProbability(int currentEnergy, int newEnergy, double temp) {
        if (newEnergy < currentEnergy) {
            return 1.0;
        }
        //System.out.println(Math.exp( -(costFunction(newEnergy)-costFunction(currentEnergy)) / temp));
        return Math.exp(-(costFunction(newEnergy) - costFunction(currentEnergy)) / temp);

    }

    private StateChange getRandomSolution(ChessBoard board) {
        int randomRow = (int) (Math.random() * chessBoard.getNumberOfQueens());
        int randomColumn = (int) (Math.random() * chessBoard.getNumberOfQueens());
        if (chessBoard.queenOnThisPosition(randomRow, randomColumn))//aktueller Zustand
        {
            return getRandomSolution(chessBoard);
        }
        return new StateChange(chessBoard.energyForStateChange(randomRow, randomColumn), randomColumn, randomRow);
    }

    private StateChange getBestState(int[][] chessBoard, Queen[] queens) {
        int maxValue = Integer.MAX_VALUE;
        int bestRow = -1;
        int bestColumn = -1;
        for (int row = 0; row < chessBoard[0].length; row++) {
            for (int column = 0; column < chessBoard.length; column++) {
                if (chessBoard[column][row] < maxValue) {
                    maxValue = chessBoard[column][row];
                    bestRow = row;
                    bestColumn = column;
                }
            }
        }
        if (bestRow < 0) {
            return null;
        } else {
            return new StateChange(maxValue, bestColumn, bestRow);
        }
    }

    private double costFunction(double energy) {
        return energy;
    }
}


