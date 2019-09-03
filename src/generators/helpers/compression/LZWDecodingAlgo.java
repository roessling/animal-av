package generators.helpers.compression;

import java.util.Hashtable;
import java.util.StringTokenizer;

public class LZWDecodingAlgo {
	
	public static void decode(String[] text) {
		String input = code(text);
		StringTokenizer t = new StringTokenizer(input);
		int cnt = t.countTokens();
		String w = "";
		String k= "";
		String result = "";
		int dicCnt = 256;
		// setup dicionary, this time the number is the key and the letters are the value
		Hashtable<Integer, String> dict = new Hashtable<Integer, String>();
		for (int i=0;i<256;i++) {
			dict.put(i, "" + ((char)i));
		}
		
		for (int i=0;i<cnt;i++) {
			int tmp = Integer.parseInt(t.nextToken());
			if (tmp<256) {
				k = "" + (char)tmp;
				if (w.equals("")) {
					w = k;
					continue;
				}
				else if (!dict.contains(w+k)) {
					result += w;
					dict.put(dicCnt, w+k);
					dicCnt++;
					w=k;
				}
			}
			else {  // dict entry
				result += w;
				String add = dict.get(tmp);
				dict.put(dicCnt, w + add.charAt(0));
				dicCnt++;
				w = add;
			}
		}
		result += k;
		System.err.println(result);
	}
	
	public static String code(String[] text) {
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
		result += dict.get(k);
		return result;
	}
}
