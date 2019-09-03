package generators.helpers.compression;

public class MTFDecodingAlgo {
	
	public static String decode(String text[], String letters) {

				
		String result = "", letter = letters;
		for (int i=0;i<text.length;i++) {
			result += letter.charAt(Integer.parseInt(text[i]));
			letter = letter.charAt(Integer.parseInt(text[i])) 
			+ letter.replace("" + letter.charAt(Integer.parseInt(text[i])), "");
		}
		return result;
	}
}
