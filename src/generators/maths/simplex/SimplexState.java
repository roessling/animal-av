package generators.maths.simplex;

import java.util.stream.IntStream;

/**
 * Holds a state of the Simplex algorithm.
 */
public class SimplexState {
    private int rowCount;
    private int colCount;
    private double[] table;
    private int[] basicVariables;

    public SimplexState(double[][] A, double[] b, double[] c) {
        if (!dimensionsAreValid(A, b, c)) {
            throw new IllegalArgumentException("Dimensions do not match");
        }

        int basicVariableCount = b.length;
        int nonbasicVariableCount = c.length - b.length;
        this.basicVariables = IntStream.range(nonbasicVariableCount + 1, nonbasicVariableCount + basicVariableCount + 1).toArray();

        this.rowCount = b.length + 1;
        this.colCount = c.length + 1;

        this.table = new double[rowCount * colCount];

        // Copy the data into the matrix...
        for (int i = 0; i < this.rowCount - 1; i++) {
            for (int j = 0; j < this.colCount - 1; j++) {
                setValueAt(i, j, A[i][j]);
            }
            setValueAt(i, colCount - 1, b[i]);
        }

        for (int i = 0; i < this.colCount - 1; i++) {
            setValueAt(rowCount - 1, i, c[i]);
        }

        setValueAt(rowCount - 1, colCount - 1, 0); // F
    }

    /**
     * Copy constructor.
     *
     * @param that Object to deep-copy.
     */
    public SimplexState(SimplexState that) {
        this.rowCount = that.rowCount;
        this.colCount = that.colCount;
        this.table = that.table.clone();
        this.basicVariables = that.getBasicVariables().clone();
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public int getColCount() {
        return this.colCount;
    }

    public double getValueAt(int row, int column) {
        return this.table[row * colCount + column];
    }

    public double[] getRow(int rowIndex, int elementCount) {
        if (rowIndex >= rowCount) {
            throw new IllegalArgumentException(String.format("Row %d does not exist.", rowIndex));
        }
        if (elementCount > colCount) {
            throw new IllegalArgumentException("Too many elements.");
        }

        double[] row = new double[elementCount];
        for (int i = 0; i < elementCount; i++) {
            row[i] = getValueAt(rowIndex, i);
        }
        return row;
    }

    public double[] getRow(int rowIndex) {
        return getRow(rowIndex, colCount);
    }

    public double[] getC() {
        return getRow(rowCount - 1, colCount - 1);
    }

    public void setValueAt(int row, int column, double value) {
        this.table[row * colCount + column] = value;
    }

    public double getF() { return getValueAt(rowCount - 1, colCount - 1); }

    public int[] getBasicVariables() {
        return basicVariables;
    }

    public static boolean dimensionsAreValid(double[][] A, double[] b, double[] c) {
        if (A.length == 0) {
            return false;
        }

        int rowCount = A.length;
        int columnCount = A[0].length;
        for (double[] row : A) {
            if (row.length != columnCount || row.length == 0) {
                return false;
            }
        }

        if (b.length != rowCount) {
            return false;
        }

        if (c.length != columnCount) {
            return false;
        }

        return true;
    }

    public static boolean isInCanonicalForm(double[][] A, double[] b, double[] c) {
        int rowCount = A.length;
        int columnCount = A[0].length;

        // Check for unit sub-matrix on the right side of A.
        for (int row = 0; row < rowCount; row++) {
            for (int col = columnCount - rowCount; col < columnCount; col++) {
                boolean onDiagonal = col - (columnCount - rowCount) == row;
                if (onDiagonal && A[row][col] != 1) {
                    return false;
                }
                if (!onDiagonal && A[row][col] != 0) {
                    return false;
                }
            }
        }

        for (double b1 : b) {
            if (b1 < 0) {
                return false;
            }
        }

        for (int i = columnCount - rowCount; i < columnCount; i++) {
            if (c[i] != 0) {
                return false;
            }
        }

        return true;
    }
}
