package generators.helpers.compression;

import java.util.Vector;

public class BurrowsWheelerReTransformationAlgo {

	public static int index;

	public static void decode(String[] textIn) {
		String input = encode(textIn);
		
	
		// prepare L, F, T
		// L the input
		String[] L = new String[input.length()];
		for (int i=0;i<input.length();i++) {
			L[i] = "" + input.charAt(i);
		}
		
		// F alphabetically ordered
		String[] F = new String[input.length()];
		String tmpS = input;
		char tmp = 255;
		
		for (int i=0;i<input.length();i++) {
			tmp = 255;
			for (int j=0;j<tmpS.length();j++) {
				if (tmpS.charAt(j) < tmp) tmp = tmpS.charAt(j);
			}
			F[i] = "" + tmp;
			tmpS = tmpS.replaceFirst("" + tmp, "");	
		}	
		
		// T the transformation Vector
		int[] T = new int[input.length()];
		boolean[] done = new boolean[input.length()];
		for (int i=0;i<L.length;i++) {
			
			for (int j=0;j<F.length;j++) {
				if (L[i].equals(F[j]) && done[j] == false) {
					T[i] = j;
					done[j] = true;
					break;
				}
			}
			
		}
		
		// start at the given index at L to get prefixes of each ketter
		String result = L[index];
//		int cnt = index;
		while (result.length() < input.length()) {
			result = L[T[index]] + result;
			index = T[index];
		}
		
		
		System.err.println("result: " + result);
	}

	private static String encode(String text[]) {

		// fill a vector with all rotations
		Vector<String[]> rotations = new Vector<String[]>(0, 1);
		String[] tmp = text;
		for (int i = 0; i < text.length; i++) {
			rotations.add(rotateLeft(tmp));
			tmp = rotateLeft(tmp);
		}

		// sort the vector
		Vector<String[]> sorted = new Vector<String[]>(0, 1);
		String[] early = rotations.elementAt(0);
		while (!rotations.isEmpty()) {
			for (int i = 0; i < rotations.size(); i++) {
				if (isEarlier(rotations.elementAt(i), early)) {
					early = rotations.elementAt(i);
					index = 1;
				}
			}
			sorted.add(early);
			rotations.removeElement(early);
			index = 0;
			if (!rotations.isEmpty())
				early = rotations.elementAt(0);
		}

		// get the index for the output row
		for (int i = 0; i < sorted.size(); i++) {
			boolean equal = true;
			for (int j = 0; j < text.length; j++) {
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

		for (int i = 0; i < sorted.size(); i++) {
			result += sorted.elementAt(i)[sorted.elementAt(i).length - 1];
		}
		return result;
	}

	public static String[] rotateLeft(String[] text) {
		String[] tmp = new String[text.length];
		for (int i = 0; i < text.length - 1; i++) {
			tmp[i] = text[i + 1];
		}
		tmp[text.length - 1] = text[0];
		return tmp;
	}

	public static boolean isEarlier(String[] text1, String[] text2) {
		int first;
		int second;
		for (int i = 0; i < text1.length; i++) {
			first = new Integer(text1[i].charAt(0));
			second = new Integer(text2[i].charAt(0));
			if (text1[i].equals("."))
				first = Integer.MAX_VALUE;
			if (text2[i].equals("."))
				second = Integer.MAX_VALUE;

			if (first < second)
				return true;
			if (first > second)
				return false;
		}

		// will never happen on different texts with the same size!
		return false;
	}
}
