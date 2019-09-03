package util.text;

public class NumberConverter {
	
	public final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public final String ROMAN = "IVXLCDM";
	
	private int number;
	
	public NumberConverter(int number) {
		setNumber(number);
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		if (number <= 0) throw new IllegalArgumentException("NumberConverter does only allow to convert positive numbers!");
		this.number = number;
	}
	
	public String toAlpha() {
		String result = "";
		int rest = number - 1;
		while (rest >= 0) {
			result = ALPHA.charAt(rest % 26) + result;
			rest = (rest - (rest % 26)) / 26 - 1;
		}
		return result;
	}
	
	public String toRoman() {
		String result = "";
		int rest = number;
		while (rest > 1000) {
			result += "M";
			rest -= 1000;
		}
		if (rest > 100) {
			result += romanPattern(rest / 100, ROMAN.substring(4, 7));
			rest -= 100 * (rest / 100);
		}
		if (rest > 10) {
			result += romanPattern(rest / 10, ROMAN.substring(2, 5));
			rest -= 10 * (rest / 10);
		}
		if (rest > 0) result += romanPattern(rest, ROMAN.substring(0, 3));
		return result;
	}
	
	private String romanPattern(int index, String space) {
		String[] patterns = {".", "..", "...", ".-", "-", "-.", "-..", "-...", ".*"};
		return patterns[index - 1].replace('.', space.charAt(0)).replace('-', space.charAt(1)).replace('*', space.charAt(2));
	}

}
