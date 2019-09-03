package generators.maths.newtonpolynomial;

import java.text.DecimalFormat;

public class DividedDifferences {

	private double[][] dividierteDifferenzen;
	private double[][] stuetzstellen;

	private int length;

	private DecimalFormat f = new DecimalFormat("#0.#####");

	public DividedDifferences(SamplingPoints stuetzstellen) {
		this.stuetzstellen = stuetzstellen.getStuetzstellen();
		length = stuetzstellen.getLength();

		this.dividierteDifferenzen = new double[length][length];
		calc();
	}

	public String getDividierteDifferenz(int i, int j) {
		return f.format(dividierteDifferenzen[i][j]);
	}

	public String getBracketedDividierteDifferenz(int i, int j) {
		double divDiff = dividierteDifferenzen[i][j];
		String result = f.format(divDiff);

		if (divDiff < 0) {
			return "(" + result + ")";
		}

		return result;
	}

	private void calc() {
		for (int i = 0; i < length; i++) {
			dividierteDifferenzen[i][0] = stuetzstellen[i][1];
		}

		for (int i = 1; i < length; i++) {
			for (int j = 0; j < length - i; j++) {
				dividierteDifferenzen[j][i] = (dividierteDifferenzen[j + 1][i - 1] - dividierteDifferenzen[j][i - 1])
						/ (stuetzstellen[j + i][0] - stuetzstellen[j][0]);
			}
		}
	}

	public String getLongestString() {
		String result = "f_{x_{i}}";
		String temp;

		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				temp = getDividierteDifferenz(i, j);
				if (temp.length() > result.length()) {
					result = temp;
				}
			}
		}

		return result;
	}

	public boolean isValid(int i, int j) {
		double tempDivDiff = dividierteDifferenzen[i][j];

		return !(Double.isNaN(tempDivDiff) || Double.isInfinite(tempDivDiff));
	}
}
