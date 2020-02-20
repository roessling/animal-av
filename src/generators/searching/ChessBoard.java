package generators.searching;

import algoanim.exceptions.NotEnoughNodesException;
import algoanim.primitives.Polyline;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolygonProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import translator.Translator;


import java.awt.*;
import java.util.Random;
import java.util.ResourceBundle;

public class ChessBoard {
    private int[][] values;
    private Text[][] textValues;
    private Queen[] queens;
    private int numberOfQueens;

    private int ChessFieldSize = 80;
    private Language lang;
    private Polyline pol;

    private int currentEnergy;
    private Text currentEnergyText;
    private Text highlightedState;

    private Coordinates start = new Coordinates(70, 150);

    private PolygonProperties polProps;

    private Translator translator;

    public ChessBoard(Language lang, int numberOfQueens, PolygonProperties props, Translator translator) {
        this.lang = lang;
        this.numberOfQueens = numberOfQueens;
        this.polProps = props;
        this.translator = translator;
    }

    public void init() {
        if (lang != null) {
            try {
                pol = drawPolyLine();
            } catch (NotEnoughNodesException ex) {

            }
            textValues = new Text[numberOfQueens][numberOfQueens];
            initText();
        }

        values = new int[numberOfQueens][numberOfQueens];
        currentEnergy = 0;
    }

    public void placeQueens() {
        Random rand = new Random();
        queens = new Queen[numberOfQueens];

        for (int i = 0; i < numberOfQueens; i++) {
            int randPos = rand.nextInt(numberOfQueens);
            queens[i] = new Queen(i, randPos);
            if (lang != null) {
                textValues[i][randPos].setText("Q", null, null);
            }
        }
        this.evaluateChessBoard();

    }

    public int evaluateChessBoard() {
        for (int column = 0; column < values.length; column++) {
            for (int row = 0; row < values[column].length; row++) {
                int res = calculateHeuristic(row, column);
                values[column][row] = res;
                if (lang != null) {
                    if (!queenOnThisPosition(row, column)) {
                        textValues[column][row].setText(String.format("%d", res), null, null);
                    }
                }
            }
        }
        currentEnergy = values[0][queens[0].getCurrentRow()];
        if (currentEnergyText != null && lang != null) {
            currentEnergyText.setText(String.format(translator.translateMessage("chessboard.currentEnergy"), currentEnergy), new TicksTiming(100), null);
        }
        return currentEnergy;
    }

    public boolean queenOnThisPosition(int row, int column) {
        return queens[column].getCurrentRow() == row;
    }

    public int energyForStateChange(int row, int column) {
        return values[column][row];
    }

    public void showCurrentEnergy() {
        TextProperties textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font("Monospaced", Font.PLAIN, 12 + numberOfQueens));
        textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, polProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));

        currentEnergyText = lang.newText(new Offset(20,
                        ChessFieldSize * numberOfQueens + 20, pol.getNodes()[0], ""),
                String.format(translator.translateMessage("chessboard.currentEnergy"), currentEnergy), "", null, textProps);

    }

    public void highlightState(int row, int column) {
        highlightedState = textValues[column][row];
        highlightedState.changeColor("", (Color) polProps.get(AnimationPropertiesKeys.FILL_PROPERTY), new TicksTiming(100), new TicksTiming(100));
    }

    public void unHighlightState() {
        highlightedState.changeColor("", (Color) polProps.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
        highlightedState = null;
    }

    public void changeState(StateChange next) {
        if (lang != null) {
            textValues[next.queenToChange][next.moveQueenToRow].setText("Q", new TicksTiming(100), new TicksTiming(200));
            textValues[next.queenToChange][queens[next.queenToChange].getCurrentRow()].setText(String.format("%d", currentEnergy), new TicksTiming(100), new TicksTiming(200));
        }
        queens[next.queenToChange].move(next.moveQueenToRow);
        this.evaluateChessBoard();
    }

    public void printBoard() {
        for (int row = 0; row < values[0].length; row++) {
            for (int column = 0; column < values.length; column++) {
                if (queenOnThisPosition(row, column)) {
                    System.out.print("Q  ");
                } else {
                    int val = values[column][row];
                    if (val >= 10) {
                        System.out.print(String.format("%d ", val));
                    } else {
                        System.out.print(String.format("%d  ", val));
                    }

                }
            }
            System.out.println();
        }
        System.out.println(String.format("current state:%d", currentEnergy));
    }

    public int getCurrentEnergy() {
        return currentEnergy;
    }

    public int getChessFieldSize() {
        return ChessFieldSize;
    }

    public int getNumberOfQueens() {
        return numberOfQueens;
    }

    public Coordinates getUpperLeft() {
        return start;
    }

    private int calculateHeuristic(int row, int column) {
        int sum = 0;
        Queen queen1 = queens[column];
        int currentPosition = queen1.getCurrentRow();
        queen1.move(row);
        for (Queen queen : queens) {
            sum += calcuateAttacks(queen, queens);
        }
        queen1.move(currentPosition);
        return sum;
    }

    private int calcuateAttacks(Queen queen, Queen[] queens) {
        int sum = 0;
        for (int i = queen.getColumn() + 1; i < queens.length; i++) {
            sum += queen.attacks(queens[i]) ? 1 : 0;
        }
        return sum;
    }

    private void initText() {
        TextProperties textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font("Monospaced", Font.PLAIN, 50));
        textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, polProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
        Coordinates start = new Coordinates(70, 150);
        for (int row = 0; row < numberOfQueens; row++) {
            for (int column = 0; column < numberOfQueens; column++) {

                textValues[column][row] = lang.newText(new Offset(column * ChessFieldSize + 16, row * ChessFieldSize + 11, start, ""), "", null, null, textProps);

            }
        }
    }

    private algoanim.primitives.Polyline drawPolyLine() throws NotEnoughNodesException {
        Coordinates[] nodes = new Coordinates[3 + 4 * numberOfQueens];

        nodes[0] = start;
        nodes[1] = new Coordinates(start.getX(), start.getY() + numberOfQueens * ChessFieldSize);
        for (int i = 0; i < numberOfQueens; i++) {
            int pos = i * 2 + 2;
            Coordinates right = new Coordinates(nodes[pos - 1].getX() + ChessFieldSize, nodes[pos - 1].getY());
            nodes[pos] = right;
            Coordinates upDown;
            if (i % 2 == 0) {
                upDown = new Coordinates(right.getX(), right.getY() - numberOfQueens * ChessFieldSize);
            } else {
                upDown = new Coordinates(right.getX(), right.getY() + numberOfQueens * ChessFieldSize);
            }
            nodes[pos + 1] = upDown;
        }
        int pos = 2 + numberOfQueens * 2;
        Coordinates left = new Coordinates(nodes[pos - 1].getX() - numberOfQueens * ChessFieldSize, nodes[pos - 1].getY());
        nodes[pos] = left;
        int rotation = numberOfQueens % 2 == 0 ? 1 : -1;
        for (int i = 0; i < numberOfQueens; i++) {
            int position = pos + 1 + i * 2;
            Coordinates upDown = new Coordinates(nodes[position - 1].getX(), nodes[position - 1].getY() - (ChessFieldSize * rotation));
            nodes[position] = upDown;
            Coordinates rightLeft;
            if (i % 2 == 0) {
                rightLeft = new Coordinates(upDown.getX() + numberOfQueens * ChessFieldSize, upDown.getY());
            } else {
                rightLeft = new Coordinates(upDown.getX() - numberOfQueens * ChessFieldSize, upDown.getY());
            }
            nodes[position + 1] = rightLeft;
        }
        PolylineProperties props = new PolylineProperties();
        props.set(AnimationPropertiesKeys.COLOR_PROPERTY, polProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
        Polyline pol = lang.newPolyline(nodes, "chessboard", null, props);

        return pol;
    }

    public void hide() {
        pol.hide();
        currentEnergyText.hide();
        for (Text[] textArray : textValues) {
            for (Text text : textArray) {
                text.hide();
            }
        }
    }
}
/*

 */