package generators.helpers.compression;

import java.util.StringTokenizer;
import java.util.Vector;

public class LZ78DecodingAlgo {

	public static String decode(String text) {
		// fill the initial dictionary
		Vector<String> dict = new Vector<String>(0,1);
		dict.add("EOF"); // 0
		for (int i=65;i<91;i++) {
			dict.add("" + (char)i);
		}

		String result = "";

		StringTokenizer t = new StringTokenizer(text);
		int cnt = t.countTokens();
		String last = "";

		for (int i=0;i<cnt;i++) {
			String tmp = t.nextToken();
			result += dict.elementAt(Integer.parseInt(tmp));

			if (i>0) {
				dict.add(dict.elementAt(Integer.parseInt(last))+dict.elementAt(Integer.parseInt(tmp)).charAt(0));
			}

			last = tmp;
		}
		return result;
	}
}
