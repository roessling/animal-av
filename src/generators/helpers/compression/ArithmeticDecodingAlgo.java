package generators.helpers.compression;

import java.util.Vector;

public class ArithmeticDecodingAlgo {
	
	public static Vector<Range> ranges;
	
	public static class Range {
		
	  protected char letter;
	  protected float start;
	  protected float end;
//		private int frequency;
		
		public Range(char aLetter, float theStart, float theEnd) {
			this.letter = aLetter;
			this.start = theStart;
			this.end = theEnd;
		}
	}
	
	public static void decode(String[] text) {
		// first get the coded number
		String input = "";
		for (int i=0;i<text.length;i++) {
			input += text[i];
		}
		float n = encode(input);
		
		// lets decode it now
		String result = "";
		
		Vector<Range> urRanges = new Vector<Range>(0,1);
		for (int i=0;i<ranges.size();i++) {
			Range r = new Range(ranges.elementAt(i).letter,ranges.elementAt(i).start,ranges.elementAt(i).end);
			urRanges.add(r);
		}
		
		for (int i=0;i<text.length;i++) {
			
			// get the next letter
			int tmp = -1;
			for (int j=0;j<ranges.size();j++) {
				if (n >= ranges.elementAt(j).start && n < ranges.elementAt(j).end) {
					result += ranges.elementAt(j).letter;
					tmp = j;
					break;
				}
			}
			
			// modify ranges
			float start = ranges.elementAt(tmp).start;
			float end = ranges.elementAt(tmp).end;
			
			

			
			for (int k=0;k<ranges.size();k++) {
				float urStart = urRanges.elementAt(k).start;
				float urEnd = urRanges.elementAt(k).end;
				ranges.elementAt(k).start = start + urStart * (end -start);
				ranges.elementAt(k).end = start + urEnd * (end -start);
			}
		}
		
		System.err.println(result);
		

		
	}
	
	private static float encode(String text) {
        // ##################################################################################
		// set up frequencys
		float[] letters = new float[256];
		for (int i=0; i<text.length();i++) {
			letters[new Integer(text.charAt(i))]++;
		}
		float[] frequency = new float[256];
		for (int i=0;i<letters.length;i++) {
			frequency[i] = letters[i]/text.length();
		}
		// put them sequentially into a vector
		float big = 0;
		int index = 0;
		ranges = new Vector<Range>(0,1);
		
		for (int i=0; i<text.length();i++) {
			for (int j=0;j<frequency.length;j++) {
				if (frequency[j] > big) {
					big = frequency[j];
					index = j;
				}
			}
			if (!ranges.isEmpty() && big > 0) ranges.add(new Range((char)index,ranges.lastElement().end,ranges.lastElement().end + big));
			else if (big > 0) ranges.add(new Range((char)index, 0, big));
			frequency[index] = -1;
			big = 0;
			index = 0;
		}
		//##################################################################################
		
		
		// encode
		float foo;
		char tmp;
		float start = 0;
		float end = 1;
		for (int i=0; i<text.length();i++) {
			tmp = text.charAt(i);
			for (int j=0;j<ranges.size();j++) {
				if (tmp == ranges.elementAt(j).letter) {
					foo = start + (end-start)*ranges.elementAt(j).start;
					end = foo + (end-start)*(ranges.elementAt(j).end-ranges.elementAt(j).start);
					start = foo;
				}
			}
		}
		return start;
	}
}
