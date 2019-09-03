package generators.helpers.compression;

import java.util.Hashtable;

public class SequiturAlgo {
	
	public static void compress(String[] inputArray) {
		String input = "";
		for (int i=0;i<inputArray.length;i++) {
			input += inputArray[i];
		}
		input = input.toLowerCase();
		String text = "";
		String tmp;
		String rest;
		int ascii = 65;
		Hashtable<String, String> dict = new Hashtable<String, String>();
		
		for (int i=0;i<input.length();i++) {
			text += input.charAt(i);
			for (int j=0;j<text.length()-1;j++) {
				tmp = "" + text.charAt(j) + text.charAt(j+1);
				rest = "";
				for (int k=0; k<text.length();k++) {
					if (k!=j && k != j+1) rest += text.charAt(k);
				}
				if (rest.contains(tmp)) {
					// for the encoding I use the Ascii value from 160 on and above
					dict.put("" + (char)ascii, tmp);
					text = text.replaceAll(tmp, "" + (char)ascii);
					input = input.replaceAll(tmp, "" + (char)ascii);
					ascii++;
					j=j-2;
					i=i-2;
					break;
				}
			}
		}
		System.out.println(text);
		
	}
}
