package generators.compression.helpers;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ShannonFanoUtils {

	/**
	 * 
	 * @param input
	 */
	public static Set<Character> extractUniqueChars(String input) {
		char[] chars = input.toCharArray();
		
		Set<Character> uniq = new TreeSet<Character>();
		for(int i=0; i< chars.length; i++) {
			uniq.add(chars[i]);
			
		}
		return uniq;
		
	}
	
	
	public static String[] getStringArray(String string) {
	
		char[] chars = string.toCharArray();
		
		String[] output = new String[chars.length];
		
		for(int i=0; i< chars.length; i++)
			output[i] = Character.toString(chars[i]);
		
		return output;
		
	}
	
	
	public static List<Element> sortByWeight (List<Element> input) {
		Collections.sort(input, new WeightComparator());
		return input;
	}
	
	public static int splitArray (List<Element> input) {
		float sum = 0;
		for (Element i : input) {
			sum+=i.getCount();
		}
		sum = sum/2;
		
		float divideSum =0; 
		
		for (Element i : input) {
			divideSum += i.getCount();
			if(divideSum >= sum)
				return input.indexOf(i);
		}
		
		return -1;
		
		
	}
	
	
	public static int getSum(List<Element> input) {
		int sum = 0;
		for (Element i : input) {
			sum+=i.getCount();
		}
		return sum;
	}
}
