package generators.helpers.compression;

import java.util.Hashtable;

public class LZWalgo {

	public static void lzw(String[] text) {
		String w = "";
		String k = "";
		String result = "";
		int cnt = 256;
		// setup dicionary
		Hashtable<String, Integer> dict = new Hashtable<String, Integer>();
		for (int i=0;i<256;i++) {
			dict.put("" + ((char)i), i);
		}
		
		for (int i=0;i<text.length;i++) {
			k = text[i];
			if (dict.containsKey(w+k)) w = w+k;
			else {
				result += dict.get(w) + " ";
				dict.put(w+k, cnt);
				cnt++;
				w = k;
			}
		}
		System.out.println(result);
	}	
}
