package generators.maths.newtonpolynomial;

import java.text.DecimalFormat;

public class SamplingPoints {

	private double[][] stuetzstellen;
	private DecimalFormat f = new DecimalFormat("#0.#####");

	public SamplingPoints(double[][] stuetzstellen) {
		this.stuetzstellen = stuetzstellen;
	}

	public double[][] getStuetzstellen() {
		return stuetzstellen;
	}

	public int getLength() {
		return stuetzstellen.length;
	}

	public String getStuetzstelle(int i, int j) {
		return f.format(stuetzstellen[i][j]);
	}

	public String getBracketedStuetzstelle(int i, int j) {
		double sStelle = stuetzstellen[i][j];
		String result = f.format(sStelle);

		if (sStelle < 0) {
			return "(" + result + ")";
		}

		return result;
	}

	public String[][] toStringMatrix() {
		String[][] result = new String[2 * stuetzstellen.length][2];

		for (int i = 0; i < stuetzstellen.length; i++) {
			result[2 * i + 1][0] = f.format(stuetzstellen[i][0]);
			result[2 * i + 1][1] = f.format(stuetzstellen[i][1]);

			result[2 * i][0] = "";
			result[2 * i][1] = "";
		}

		result[0][0] = "x_{i}";
		result[0][1] = "y_{i}";

		return result;
	}

	public String getLongestString() {
		String result = "x_{i}";
		String temp;

		for (int i = 0; i < stuetzstellen.length; i++) {
			for (int j = 0; j < 2; j++) {
				temp = getStuetzstelle(i, j);
				if (temp.length() > result.length()) {
					result = temp;
				}
			}
		}

		return result;
	}
}
