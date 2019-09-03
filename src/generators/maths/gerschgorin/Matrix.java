package generators.maths.gerschgorin;

import algoanim.primitives.DoubleMatrix;
import algoanim.util.Timing;

import java.awt.*;

/**
 * A matrix which can be used for the gerschgorin calculation.
 * @author Jannis Weil, Hendrik Wuerz
 */
public class Matrix {

    public static Color MAIN_FOCUS_COLOR = Color.YELLOW;
    public static Color SUB_FOCUS_COLOR = Color.LIGHT_GRAY;

    private DoubleMatrix animalMatrix;

    private int mainFocusX = -1;
    private int mainFocusY = -1;
    private int subFocusX = -1;
    private int subFocusY = -1;

    Matrix(DoubleMatrix animalMatrix) {
        this.animalMatrix = animalMatrix;
    }

    public DoubleMatrix getAnimalMatrix() {
        return animalMatrix;
    }

    public void setMainFocus(int x, int y) {
        if(mainFocusX >= 0 && mainFocusY >= 0) {
            animalMatrix.setGridFillColor(mainFocusY, mainFocusX, Color.WHITE, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        }
        if(x >= 0 && y >= 0) {
            animalMatrix.setGridFillColor(y, x, MAIN_FOCUS_COLOR, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        }
        mainFocusX = x;
        mainFocusY = y;
    }

    public void setSubFocus(int x, int y) {
        if(subFocusX >= 0 && subFocusY >= 0) {
            animalMatrix.setGridFillColor(subFocusY, subFocusX, Color.WHITE, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        }
        if(x >= 0 && y >= 0) {
            animalMatrix.setGridFillColor(y, x, SUB_FOCUS_COLOR, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        }
        subFocusX = x;
        subFocusY = y;
    }

    public static void transpose(double[][] matrix) {
        // There have to be elements and the matrix is square (#rows == #columns)
        if(matrix.length == 0 || matrix[0].length != matrix.length) {
            throw new RuntimeException("Only a square matrix is supported");
        }

        int size = matrix.length;
        for(int i = 0; i < size; i++) {
            for(int j = i + 1; j < size; j++) {
                double tmp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = tmp;
            }
        }
    }
}
