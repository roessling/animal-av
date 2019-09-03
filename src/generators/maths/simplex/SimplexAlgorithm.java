package generators.maths.simplex;

import java.util.stream.DoubleStream;

/**
 * Utils and procedures regarding the Simplex algorithm.
 */
public class SimplexAlgorithm {

    public static boolean stateIsOptimal(SimplexState state) {
        return DoubleStream.of(state.getC()).allMatch(d -> d >= 0);
    }

    public static int findPivotColumnIndex(SimplexState state) {
        double[] c = state.getC();
        double min = c[0];
        int minIndex = 0;
        for (int i = 1; i < c.length; i++) {
            if (c[i] < min) {
                min = c[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    public static double[] calculateImprovementVector(SimplexState state, int pivotColumnIndex) {
        double[] improvementVector = new double[state.getRowCount() - 1];

        for (int i = 0; i < state.getRowCount() - 1; i++) {
            double pivotColumnValue = state.getValueAt(i, pivotColumnIndex);
            if (pivotColumnValue == 0) {
                improvementVector[i] = Double.POSITIVE_INFINITY;
            }
            else if (pivotColumnValue < 0) {
                // Negative a_it get ignored.
                improvementVector[i] = Double.NaN;
            }
            else {
                double bValue = state.getValueAt(i, state.getColCount() - 1);
                improvementVector[i] = bValue / pivotColumnValue;
            }
        }

        return improvementVector;
    }

    public static boolean modelIsUnbounded(double[] improvementVector) {
        return DoubleStream.of(improvementVector).allMatch(d -> Double.isNaN(d) || Double.isInfinite(d));
    }

    public static int findPivotRowIndex(double[] improvementVector) {
        double min = improvementVector[0];
        int minIndex = 0;
        for (int i = 1; i < improvementVector.length; i++) {
            if (Double.isNaN(min) || Double.isInfinite(min) || improvementVector[i] < min) {
                min = improvementVector[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    public static double getPivotElement(SimplexState state, int pivotRow, int pivotColumn) {
        return state.getValueAt(pivotRow, pivotColumn);
    }

    public static SimplexState replaceBaseVariable(SimplexState state, int pivotRow, int pivotColumn) {
        SimplexState updatedState = new SimplexState(state);
        int[] baseVars = updatedState.getBasicVariables();
        baseVars[pivotRow] = pivotColumn + 1;
        return updatedState;
    }

    public static SimplexState updatePivotRow(SimplexState state, int pivotRow, int pivotColumn) {
        SimplexState updatedState = new SimplexState(state);

        double pivotElement = state.getValueAt(pivotRow, pivotColumn);

        for (int i = 0; i < state.getColCount(); i++) {
            double updatedVal = state.getValueAt(pivotRow, i) / pivotElement;
            updatedState.setValueAt(pivotRow, i, updatedVal);
        }

        return updatedState;
    }

    public static SimplexState updateAllRows(SimplexState state, int pivotRow, int pivotColumn) {
        state = updatePivotRow(state, pivotRow, pivotColumn);
        SimplexState updatedState = new SimplexState(state);

        for (int i = 0; i < state.getRowCount(); i++) {
            if (i == pivotRow) {
                continue;
            }

            double rowFactor = state.getValueAt(i, pivotColumn);

            for (int j = 0; j < state.getColCount(); j++) {
                double updatedVal = state.getValueAt(i, j) - (rowFactor * state.getValueAt(pivotRow, j));
                updatedState.setValueAt(i, j, updatedVal);
            }
        }

        return updatedState;
    }

    public static double[] getCurrentBasicSolution(SimplexState state) {
        double[] solution = new double[state.getColCount() - 1];

        int row = 0;
        for (int basicVariable : state.getBasicVariables()) {
            solution[basicVariable - 1] = state.getValueAt(row++, state.getColCount() - 1);
        }

        return solution;
    }
}
