package generators.maths.simplex;

import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    public static double[][] intMatrixToDoubleMatrix(int[][] intMatrix) {
        double[][] doubleMatrix = new double[intMatrix.length][];
        for (int i = 0; i < intMatrix.length; i++) {
            doubleMatrix[i] = new double[intMatrix[i].length];
            for (int j = 0; j < intMatrix[i].length; j++) {
                doubleMatrix[i][j] = intMatrix[i][j];
            }
        }
        return doubleMatrix;
    }

    public static double[] intArrayToDoubleArray(int[] intArray) {
        double[] doubleArray = new double[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            doubleArray[i] = intArray[i];
        }
        return doubleArray;
    }

    public static void highlightColumn(StringMatrix mat, int colIndex) {
        mat.highlightCellRowRange(0, mat.getNrRows() - 1, colIndex, null, null);
    }

    public static void highlightRow(StringMatrix mat, int rowIndex) {
        mat.highlightCellColumnRange(rowIndex, 0, mat.getNrCols() - 1, null, null);
    }

    public static void unhighlightColumn(StringMatrix mat, int colIndex) {
        //mat.unhighlightCellRowRange(0, mat.getNrRows() - 1, colIndex, null, null);
        for (int i = 0; i < mat.getNrRows(); i++) {
            mat.unhighlightCell(i, colIndex, null, null);
        }
    }

    public static void unhighlightRow(StringMatrix mat, int rowIndex) {
        //mat.unhighlightCellColumnRange(rowIndex, 0, mat.getNrCols() - 1, null, null);
        for (int i = 0; i < mat.getNrCols(); i++) {
            mat.unhighlightCell(rowIndex, i, null, null);
        }
    }

    public static void unhighlightAll(StringMatrix mat) {
        for (int i = 0; i < mat.getNrRows(); i++) {
            unhighlightRow(mat, i);
        }
    }

    public static void highlightCode(SourceCode code, String... lines) {
        for (int i = 0; i < code.length(); i++) {
            code.unhighlight(i);
        }

        for (String line : lines) {
            code.highlight(line);
        }
    }

    private static String[] subscripts = new String[] {"₀", "₁", "₂", "₃", "₄", "₅", "₆", "₇", "₈", "₉"};

    public static String getXVariable(int subscriptIndex) {
        StringBuilder variable = new StringBuilder();
        while (subscriptIndex > 0) {
            int digit = subscriptIndex % 10;
            variable.append(subscripts[digit]);
            subscriptIndex /= 10;
        }
        variable.append("x");
        return variable.reverse().toString();
    }

    public static List<String> insertLineBreaks(List<String> inputLines, int lineWidth) {
        List<String> output = new ArrayList<>();
        for (String line : inputLines) {
            StringBuilder sb = new StringBuilder(line);

            int i = 0;
            while ((i = sb.indexOf(" ", i + lineWidth)) != -1) {
                sb.replace(i, i + 1, "\n");
            }

            output.addAll(Arrays.asList(sb.toString().split("\n")));
        }
        return output;
    }

    public static void updateResultsTable(StringMatrix mat, List<String[]> results) {
        int maxSize = mat.getNrRows() - 1;
        int startOffset = Integer.max(0, results.size() - maxSize);

        for (int row = 0; row < results.size() && row < maxSize; row++) {
            for (int col = 0; col < mat.getNrCols(); col++) {
                mat.put(row + 1, col, results.get(startOffset + row)[col], null, null);
            }
        }
    }

    public static <T> void clearMatrix(T[][] mat, T val) {
        for (int row = 0; row < mat.length; row++) {
            for (int col = 0; col < mat[row].length; col++) {
                mat[row][col] = val;
            }
        }
    }
}
