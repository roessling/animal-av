package generators.helpers.compression;

import java.util.Vector;

public class LZ78Algo {
	
	public static String code(String[] text) {
		// extract the input
		String input = "";
		for (int i=0;i<text.length;i++) {
			input += text[i];
		}
		input = input.toUpperCase();
		
		// fill the initial dictionary
		Vector<String> dict = new Vector<String>(0,1);
		dict.add("EOF"); // 0
		for (int i=65;i<91;i++) {
			dict.add("" + (char)i);
		}
		
		String result = "";
		for (int i=0;i<input.length();i++) {
			String tmp = "" + input.charAt(i);
			while (dict.contains(tmp) && i+1<input.length()) {
				if (dict.contains(tmp + input.charAt(i+1))) {
					tmp += input.charAt(i+1);
					i++;
				}
				else {
					dict.add(tmp + input.charAt(i+1));
					break;
				}
			}
			result += dict.indexOf(tmp) + " ";
		}
		return result;
	}
}
