package generators.compression.shannon_fano.utils;

public class ShannonFanoCodingUtils {

	public static String[] getStringArray(String string) {
		
		char[] chars = string.toCharArray();
		
		String[] output = new String[chars.length];
		
		for(int i=0; i< chars.length; i++)
			output[i] = Character.toString(chars[i]);
		
		return output;		
	}
}
