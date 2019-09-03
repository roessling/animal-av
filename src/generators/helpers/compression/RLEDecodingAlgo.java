package generators.helpers.compression;


public class RLEDecodingAlgo {

	public static String decode(String[] text) {
		// extract the input
		String input = "";
		for (int i=0; i<text.length;i++) {
			input += text[i];
		}

		String result = "";
		String tmp = "";
		int cnt = 0;
		for (int i=0;i<input.length();i++) {
			while (input.charAt(i) > 47 && input.charAt(i) < 58) {
				tmp += input.charAt(i);
				i++;
			}
			if (tmp.length() > 0) {
				cnt = Integer.parseInt(tmp);
				for (int j=0;j<cnt;j++) {
					result += input.charAt(i);
				}
			}
			else result += input.charAt(i);
			tmp = "";
			cnt = 0;
		}
		return result;
	}
}
