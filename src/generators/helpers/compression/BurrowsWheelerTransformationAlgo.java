package generators.helpers.compression;

import java.util.Vector;

public class BurrowsWheelerTransformationAlgo {
	
	public static int index;

	public static void transform(String[] text) {
		
		// fill a vector with all rotations
		Vector<String[]> rotations = new Vector<String[]>(0,1);
		String[] tmp = text;
		for (int i=0;i<text.length;i++) {
			rotations.add(rotateLeft(tmp));
			tmp = rotateLeft(tmp);
		}
		
				
		// sort the vector
		Vector<String[]> sorted = new Vector<String[]>(0,1);
		String[] early = rotations.elementAt(0);
//		int index = 0;
		while (!rotations.isEmpty()) {
			for (int i=0;i<rotations.size();i++) {
				if (isEarlier(rotations.elementAt(i), early)) {
					early = rotations.elementAt(i);
					index = 1;
				}
			}
			sorted.add(early);
			rotations.removeElement(early);
			index = 0;
			if (!rotations.isEmpty()) early = rotations.elementAt(0);
		}
		
		// get the index for the output row
		for (int i=0;i<sorted.size();i++) {
			boolean equal = true;
			for (int j=0;j<text.length; j++) {
				if (text[j] != sorted.elementAt(i)[j]) {
					equal = false;
					break;
				}
			}
			if (equal) {
				index = i;
				break;
			}
		}
		
		// take the last letters in a row
		String result = "";
		
		for (int i=0;i<sorted.size();i++) {
			result += sorted.elementAt(i)[sorted.elementAt(i).length-1];
		}
		System.out.println(result);
	}
	
	public static String[] rotateLeft(String[] text) {
		String[] tmp = new String[text.length];
		for (int i=0; i<text.length-1;i++) {
			tmp[i] = text[i+1];
		}
		tmp[text.length-1] = text[0];
		return tmp;
	}
	
	public static boolean isEarlier(String[] text1, String[] text2) {
		int first;
		int second;
		for (int i=0; i<text1.length;i++) {
			first = new Integer(text1[i].charAt(0));
			second = new Integer(text2[i].charAt(0));
			if (text1[i].equals(".")) return false;
			if (text2[i].equals(".")) return false;
			
			if (first < second) return true;
			if (first > second) return false;
		}
		
		// will never happen on different texts with the same size!
		return false;
	}
}
