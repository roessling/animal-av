/*
 * SquareMatrix.java
 * Simon Breitfelder, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths.gaussjordan;

public class SquareMatrix {
	private float[][] mat;
	private int size;

	public SquareMatrix(int size) {
		// init as unit matrix
		this.size = size;
		this.mat = new float[this.size][this.size];
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				this.mat[i][j] = (i == j ? 1 : 0);
			}
		}
	}

	public SquareMatrix(int[][] mat) {
		this.size = mat.length;
		this.mat = new float[this.size][this.size]; // mat needs to be square
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				this.mat[i][j] = mat[i][j];
			}
		}
	}

	public SquareMatrix(SquareMatrix original) {
		this.size = original.size;
		this.mat = new float[this.size][this.size];
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				this.mat[i][j] = original.mat[i][j];
			}
		}
	}

	public int getSize() {
		return this.size;
	}

	public float get(int i, int j) {
		return this.mat[i][j];
	}

	public String getString(int i, int j) {
		return doubleToString(this.mat[i][j]);
	}

	public void set(int i, int j, float val) {
		this.mat[i][j] = val;
	}

	public int getPivotRow(int startRow, int col) {
		// largest element for column with index row
		int iMax = startRow;
		float max = this.mat[startRow][col];
		for (int i = (startRow + 1); i < this.size; i++) {
			if (Math.abs(this.mat[i][col]) > max) {
				iMax = i;
				max = Math.abs(this.mat[i][col]);
			}
		}
		return iMax;
	}

	public void swapRows(int row1, int row2) {
		if (row1 == row2)
			return;

		float[] tmpRow = this.mat[row1].clone();
		this.mat[row1] = this.mat[row2];
		this.mat[row2] = tmpRow;
	}

	private String doubleToString(double val) {
		val = Math.round(100.0 * val) / 100.0;
		if (Math.abs(val - Math.round(val)) < 0.001)
			return Integer.toString((int) val);
		else
			return Double.toString(val);
	}

	public String[][] getStringArray() {
		String[][] mat = new String[this.size][this.size];
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				mat[i][j] = doubleToString(this.mat[i][j]);
			}
		}
		return mat;
	}

	public double[][] getDoubleArray() {
		double[][] mat = new double[this.size][this.size];
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				mat[i][j] = this.mat[i][j];
			}
		}
		return mat;
	}

	public int[][] getIntArray() {
		int[][] mat = new int[this.size][this.size];
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				mat[i][j] = (int) Math.round(this.mat[i][j]);
			}
		}
		return mat;
	}
}
