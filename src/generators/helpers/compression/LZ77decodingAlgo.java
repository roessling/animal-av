package generators.helpers.compression;

public class LZ77decodingAlgo {
	
	public static String decode(String text) {
		String result = "";
		int index;
		int length;
		char next;
		
		int s = 1;
		
		while (s < text.length()) {
			// parse
			index = Integer.parseInt("" + text.charAt(s));  
			s +=2; // read  ','
			length =Integer.parseInt("" + text.charAt(s));  
			s +=2;// read  ','
			next = text.charAt(s);
			s += 4; // read  ') ('
			
			if (index == 0 && length == 0) result += next;
			else {
				result += result.substring(result.length()-index-1,result.length()-index-1 + length);
				if (text.charAt(s-3) != 'O') result += next; // prevent EOF
			}
		}

		return result;
	}
}
