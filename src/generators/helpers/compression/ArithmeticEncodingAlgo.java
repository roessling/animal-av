package generators.helpers.compression;

import java.util.Vector;

public class ArithmeticEncodingAlgo {
	
	public static class Range {
		
	  protected char letter;
		protected float start;
		protected float end;
//		private int frequency;
		
		public Range(char letter, float start, float end) {
			this.letter = letter;
			this.start = start;
			this.end = end;
		}
	}

	public static void encode(String[] text) {
		//##################################################################################
		// set up frequencys
		float[] letters = new float[256];
		for (int i=0; i<text.length;i++) {
			letters[new Integer(text[i].charAt(0))]++;
		}
		float[] frequency = new float[256];
		for (int i=0;i<letters.length;i++) {
			frequency[i] = letters[i]/text.length;
		}
		// put them sequentially into a vector
		float big = 0;
		int index = 0;
		Vector<Range> ranges = new Vector<Range>(0,1);
		
		for (int i=0; i<text.length;i++) {
			for (int j=0;j<frequency.length;j++) {
				if (frequency[j] > big) {
					big = frequency[j];
					index = j;
				}
			}
			if (!ranges.isEmpty() && big > 0) 
			  ranges.add(new Range((char)index,ranges.lastElement().end,ranges.lastElement().end + big));
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
		for (int i=0; i<text.length;i++) {
			tmp = text[i].charAt(0);
			for (int j=0;j<ranges.size();j++) {
				if (tmp == ranges.elementAt(j).letter) {
					foo = start + (end-start)*ranges.elementAt(j).start;
					end = foo + (end-start)*(ranges.elementAt(j).end-ranges.elementAt(j).start);
					start = foo;
				}
			}
		}
	}
}
