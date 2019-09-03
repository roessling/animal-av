package generators.helpers.compression;

public class RLEalgo {
	
	/**
	 * Run length encoding algorithm, compresses a String
	 * of arbitrary lenght.
	 * Actually running on strings without numbers only.
	 *
	 */
	public static void rle(String text) {
		int count = 1;
		char tmp;
		String result = "";
		for (int i=0;i<text.length();i++) {
			tmp = text.charAt(i);
			if (i == text.length()-1) {
				result += tmp;
				break;
			}
			while (text.charAt(i+1) == tmp) {
				i++;
				count++;
			}
			if (count == 1) result += tmp;
			else result = result + (new Integer(count)).toString() + tmp;
			count = 1;
		}
		System.out.println(result);
	}	
}
