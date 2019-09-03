package generators.maths.adjoint;

import java.text.NumberFormat;
import java.util.Arrays;

public class Matrix {

	private int[][] matrix;

	private int length;

	public Matrix(int[][] matrix) {
		this.matrix = matrix;
		length = matrix.length;
	}

	public int[][] getMatrix() {
		return matrix;
	}

	public int getLength() {
		return length;
	}

	public Matrix getUntermatrix(int i, int j) {
		int[][] result = new int[length - 1][length - 1];

		boolean greaterI = false;
		boolean greaterJ = false;

		for (int k = 0; k < length - 1; k++) {
			for (int l = 0; l < length - 1; l++) {
				if (k == i - 1) {
					greaterI = true;
				}
				if (l == j - 1) {
					greaterJ = true;
				}

				result[k][l] = matrix[k + (greaterI ? 1 : 0)][l + (greaterJ ? 1 : 0)];
			}
			greaterJ = false;
		}

		return new Matrix(result);
	}

	// code from http://wiki.answers.com/Q/Determinant_of_matrix_in_java
	private int calcDeterminante(int[][] matrix) {
		int result = 0;

		if (matrix.length == 1) {
			result = matrix[0][0];
			return result;
		}

		if (matrix.length == 2) {
			result = matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
			return result;
		}

		for (int i = 0; i < matrix[0].length; i++) {
			int temp[][] = new int[matrix.length - 1][matrix[0].length - 1];

			for (int j = 1; j < matrix.length; j++) {
				System.arraycopy(matrix[j], 0, temp[j - 1], 0, i);
				System.arraycopy(matrix[j], i + 1, temp[j - 1], i, matrix[0].length - i - 1);
			}

			result += matrix[0][i] * Math.pow(-1, i) * calcDeterminante(temp);
		}

		return result;
	}

	public double calcUnterdeterminante(int i, int j) {
		return calcDeterminante(this.getUntermatrix(i, j).getMatrix());
	}

	public Matrix getTransponierteMatrix() {
		int[][] result = new int[length][length];

		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				result[i][j] = matrix[j][i];
			}
		}

		return new Matrix(result);
	}

	public String[][] toStringMatrix() {
		String[][] result = new String[length][length];

		for (int i = 0; i < length; i++) {
			for (int j = 0; j < result.length; j++) {
				result[i][j] = NumberFormat.getInstance().format(matrix[i][j]);
			}
		}

		return result;
	}

	public String toDisplay() {
		return Arrays.deepToString(matrix).replace('[', '{').replace(']', '}');
	}

	public Matrix calculateCofactors() {
		int[][] cofactors = new int[length][length];

		for (int i = 0; i < length; ++i) {
			for (int j = 0; j < length; ++j) {
				cofactors[i][j] = (int) (calcUnterdeterminante(i + 1, j + 1) * Math.pow(-1, i + 1 + j + 1));
			}
		}

		return new Matrix(cofactors);
	}

	public String getLongestString() {
		String result = "";
		String temp;

		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				temp = NumberFormat.getInstance().format(matrix[i][j]);
				if (temp.length() > result.length()) {
					result = temp;
				}
			}
		}

		return result;
	}

}
