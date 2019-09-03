package generators.helpers.compression;

public class MTFalgo {
	//	 move to front transfprmation
	//   usually in use after a BWT
	
	public static String transform(String[] text) {
		
		// create a alphabetically sorted string of the letters
		String letters = "";
		char small = 0;
		
		for (int i=0;i<text.length;i++) {
			char tmp = 256;
			for (int j=0;j<text.length;j++) {
				if (text[j].charAt(0) > small && text[j].charAt(0) < tmp) {
					tmp = text[j].charAt(0);
				}
			}
			small = tmp;
			if (small != 256) letters += small;
		}
			
		// iterate over the input, apend the index of the current letter, und move it to the front
		String result = "";
		for (int i=0; i<text.length;i++) {
			result += letters.indexOf(text[i]);
			letters = text[i] + letters.replace(text[i], "");
		}
		return result;
	}
}
