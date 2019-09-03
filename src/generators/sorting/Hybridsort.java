package generators.sorting;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;

/**
 * 
 * @author Steffen Frank Schmidt
 *
 */
public class Hybridsort {

	private static Animator animator;
	private static boolean info;

	/**
	 * Constructer for hybridsort
	 * 
	 * @param lang
	 */
	public Hybridsort(Language lang) {
		Hybridsort.animator = new Animator(lang);
	}

	/**
	 * Starts application and creates hybridsort.asu
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Language lang = new AnimalScript("Der Hybridsort-Algorithmus",
				"Steffen Frank Schmidt", 1200, 700);
		Hybridsort hybridsorter = new Hybridsort(lang);
		Integer[] elements = getExampleInput();
		int numOfBuckets = 5;
		print(elements);

		Integer[] sortedElements = hybridsorter.sort(elements, numOfBuckets);

		print(sortedElements);

		lang.finalizeGeneration();
		String code = lang.toString();
		try {
			FileOutputStream fos = new FileOutputStream("hybridsort.asu");
			Writer out = new OutputStreamWriter(fos, "UTF8");
			out.write(code);
			out.close();
			System.out.println("A file called \"hybridsort.asu\" was created.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prints elements
	 * 
	 * @param sequence
	 * @return
	 */
	private static String print(Integer[] sequence) {
		if (sequence.length == 0) {
			System.out.println("[]");
			return "[]";
		}
		StringBuffer sb = null;
		for (int i = 0; i < sequence.length; i++) {
			if (sb == null) {
				sb = new StringBuffer();
				sb.append("[").append(sequence[i]);
			} else {
				sb.append(",").append(sequence[i]);
			}
		}
		sb.append("]");
		System.out.println(sb.toString());
		return sb.toString();
	}

	/**
	 * Sort an input of integer values with hybridsort
	 * 
	 * @param input
	 * @param num_of_buckets
	 * @return
	 */
	public Integer[] sort(Integer[] input, Integer num_of_buckets) {
		if (input.length <= 1)
			return input;
		
		if (num_of_buckets <= 0)
			throw new IllegalArgumentException("Eimeranzahl muss größer 0 sein.");
		
		if (input.length <= 0){
			throw new IllegalArgumentException("Die Länge der Eingabeliste muss größer 0 sein.");
		}
		// START
		animator.showIntro();
		animator.showStart(input, num_of_buckets);
		// DIVIDE (GO INTO)
		List<List<Integer>> buckets = null;
		num_of_buckets++;
		do {
			num_of_buckets--;
			buckets = divideIntoBuckets(input, num_of_buckets, false);
		} while (isOneEmpty(buckets) || buckets == null);
		buckets = divideIntoBuckets(input, num_of_buckets, true);
		// SEE ALL THE BUCKETS
		animator.showAllBuckets(buckets);

		List<List<Integer>> sorted_buckets = initializeBuckets(num_of_buckets);
		int i = 0;
		for (Iterator<List<Integer>> bucket_iterator = buckets.iterator(), sorted_bucket_iterator = sorted_buckets
				.iterator(); bucket_iterator.hasNext()
				&& sorted_bucket_iterator.hasNext();) {
			// BUCKET SELECTED
			List<Integer> bucket_as_list = bucket_iterator.next();
			Integer[] bucket = bucket_as_list
					.toArray(new Integer[bucket_as_list.size()]);
			animator.highlightBucket(bucket, i);
			if (i == 0)
				animator.showQuicksortText();
			// QUICKSORT (GO INTO)
			animator.showStartOfQuicksort(bucket, i);
			animator.pushSequence(bucket, 0, bucket.length - 1, true);
			quicksort(bucket, 0, bucket.length - 1);
			animator.unhighlightCode();
			animator.highlightCode(23);
			animator.popSequence(bucket, 0, bucket.length - 1, false);
			animator.showEndOfQuicksort(sorted_buckets, bucket, i);
			List<Integer> sorted_bucket_as_list = sorted_bucket_iterator.next();
			for (int k = 0; k < bucket.length; k++) {
				sorted_bucket_as_list.add(bucket[k]);
			}
			i++;
		}
		// ALL BUCKETS ARE SORTED
		animator.unhighlightCode();
		animator.highlightCode(24);
		animator.showAllSortedBuckets(sorted_buckets);
		input = concatenateBuckets(sorted_buckets);
		// CONCATENATE BUCKETS
		animator.showConcatenatedBucketsAsList(sorted_buckets);
		// animator.printMapping();
		animator.showOutro();
		return input;
	}

	private boolean isOneEmpty(List<List<Integer>> buckets) {
		for (Iterator<List<Integer>> iterator = buckets.iterator(); iterator
				.hasNext();) {
			List<Integer> list = iterator.next();
			if (list.size()==0) {
				info = true;
				return true;
			}
		}
		return false;
	}

	/**
	 * Concatenates sorted buckets for output
	 * 
	 * @param sorted_buckets
	 * @return
	 */
	private static Integer[] concatenateBuckets(
			List<List<Integer>> sorted_buckets) {
		int size = 0;
		for (Iterator<List<Integer>> iterator = sorted_buckets.iterator(); iterator
				.hasNext();) {
			List<Integer> list = iterator.next();
			size += list.size();
		}
		Integer[] elements = new Integer[size];
		int k = 0;
		for (Iterator<List<Integer>> i = sorted_buckets.iterator(); i.hasNext();) {
			List<Integer> bucket = i.next();
			for (Iterator<Integer> j = bucket.iterator(); j.hasNext();) {
				Integer elem = j.next();
				elements[k] = elem;
				k++;
			}
		}
		return elements;
	}

	/**
	 * Divides input into n buckets
	 * 
	 * @param elements
	 * @param num_of_buckets
	 * @return
	 */
	private static List<List<Integer>> divideIntoBuckets(Integer[] elements,
			Integer num_of_buckets, boolean animal) {
		// SELECT MAXIMUM
		Integer max_element = findMaximum(elements);
		if (animal) {
			animator.highlightMaximumElement(max_element);
			animator.showEmptyBuckets(num_of_buckets,info);
		}

		List<List<Integer>> buckets = initializeBuckets(num_of_buckets);
		for (Integer i = 0; i < elements.length; i++) {
			Integer element = (Integer) elements[i];

			// CALCULATE INDEX
			int index = Math.round(((float) (element * (num_of_buckets - 1)))
					/ max_element);
			index = (element * (num_of_buckets - 1) / max_element);
			// MOVE ELEMENT INTO BUCKET
			buckets.get(index).add(element);
			if (animal)
				animator.moveElementIntoBucket(buckets.get(index), element,
						index);
		}
		return buckets;
	}

	/**
	 * Sorts sequence with quicksort
	 * 
	 * @param sequence
	 * @param links
	 * @param rechts
	 */
	private static void quicksort(Integer[] sequence, int links, int rechts) {
		// HIGHLIGHT CURRENT SEQUENCE
		animator.popSequence(sequence, links, rechts, false);
		animator.pushSequence(sequence, links, rechts, true, true);

		if (links < rechts) {

			// SHOW CURRENT PART OF SEQUENCE
			animator.hidePivotElement();
			animator.hideLRPointers();
			animator.showPivotElement(sequence[rechts]);
			animator.setLRMarkers(links, rechts - 1);

			// PRESORT AND DIVIDE
			int teiler = divide(sequence, links, rechts);

			// UNHIGHLIGHT CURRENT SEQUENCE
			animator.popSequence(sequence, links, rechts, false);

			animator.unhighlightCode();
			animator.highlightCode(22);

			animator.pushSequence(sequence, teiler + 1, rechts, false);
			animator.pushSequence(sequence, links, teiler - 1, false);

			// RUN QUICKSORT ON LEFT SUBSEQUENCE
			quicksort(sequence, links, teiler - 1);

			animator.popSequence(sequence, links, teiler - 1, false);

			// SHOW CURRENT PART OF SEQUENCE
			animator.hidePivotElement();
			animator.hideLRPointers();
			animator.unhighlightCode();
			animator.highlightCode(22);

			// RUN QUICKSORT ON RIGHT SUBSEQUENCE
			quicksort(sequence, teiler + 1, rechts);

			animator.hidePivotElement();
			animator.hideLRPointers();
			animator.unhighlightCode();
			animator.highlightCode(22);

			animator.popSequence(sequence, teiler + 1, rechts, false);

		} else if (links < sequence.length) {			
			// SHOW CURRENT PART OF SEQUENCE
			animator.hidePivotElement();
			animator.hideLRPointers();
			animator.showPivotElement(sequence[links]);
			animator.setLRMarkers(links, links);			
			animator.unhighlightCode();
			animator.highlightCode(12);
			animator.nextStep();		
			animator.unhighlightCode();
			animator.highlightCode(21);
			animator.nextStep();
		}
	}

	/**
	 * Divides sequence by presorting with a pivot element
	 * 
	 * @param sequence
	 * @param links
	 * @param rechts
	 * @return int
	 */
	private static int divide(Integer[] sequence, int links, int rechts) {
		Integer l_pointer = links;
		Integer r_pointer = rechts - 1;
		Integer pivot_element = sequence[rechts];
		// SET PIVOT AND POINTER
		do {
			animator.highlightCode(12);
			if (sequence[l_pointer] <= pivot_element && l_pointer < rechts) {
				while (sequence[l_pointer] <= pivot_element
						&& l_pointer < rechts) {
					l_pointer++;
					// MOVE LEFT POINTER
					animator.moveLeftPointerRight(l_pointer - 1, l_pointer);
				}
			} else {
				animator.showPointerMovementLeftToRight();
			}
			if (sequence[r_pointer] >= pivot_element && r_pointer > links) {
				while (sequence[r_pointer] >= pivot_element
						&& r_pointer > links) {
					r_pointer--;
					// MOVE RIGHT POINTER
					animator.moveRightPointerLeft(r_pointer + 1, r_pointer);
				}
			} else {
				animator.showPointerMovementRightToLeft();
			}
			if (l_pointer < r_pointer) {
				swap(sequence, l_pointer, r_pointer);
				// SWAP ELEMENTS
				animator.unhighlightCode();
				animator.highlightCode(12);
				animator.highlightCode(19);
				animator.highlightCode(20);
				animator.showSwapOfElements(sequence, l_pointer, r_pointer);

			}
		} while (l_pointer < r_pointer);

		if (sequence[l_pointer] > pivot_element) {
			swap(sequence, l_pointer, rechts);
			// SWAP ELEMENTS
			animator.unhighlightCode();
			animator.highlightCode(21);
			animator.showSwapOfElements(sequence, l_pointer, rechts);
			animator.showPivotAndLRPointers();

		}
		return l_pointer;
	}

	/**
	 * Swaps two elements
	 * 
	 * @param sequence
	 * @param i
	 * @param j
	 */
	private static void swap(Integer[] sequence, Integer i, Integer j) {
		Integer tmp = sequence[i];
		sequence[i] = sequence[j];
		sequence[j] = tmp;
	}

	/**
	 * Initializes buckets
	 * 
	 * @param num_of_buckets
	 * @return List<List<Integer>>
	 */
	private static List<List<Integer>> initializeBuckets(Integer num_of_buckets) {
		List<List<Integer>> buckets = new ArrayList<List<Integer>>();
		for (Integer i = 0; i < num_of_buckets; i++) {
			List<Integer> bucket = new ArrayList<Integer>();
			buckets.add(bucket);
		}
		return buckets;
	}

	/**
	 * Finds maximum element in sequence
	 * 
	 * @param sequence
	 * @return {@link Integer}
	 */
	private static Integer findMaximum(Integer[] sequence) {
		Integer max_element = Integer.MIN_VALUE;
		for (Integer i = 0; i < sequence.length; i++) {
			Integer element = (Integer) sequence[i];
			if (max_element == Integer.MIN_VALUE || element > max_element)
				max_element = element;
		}
		return max_element;
	}

	/**
	 * Returns example input
	 * 
	 * @return Integer[]
	 */
	private static Integer[] getExampleInput() {
		Integer[] elements = { 43, 23, 42, 34, 87, 29, 78, 120, 118, 45, 33,
				102, 123, 20, 11 };
		return elements;
	}

	/**
	 * Sorts a input by hybridsort
	 * 
	 * @param input
	 * @param nob
	 * @return
	 */
	public Integer[] sort(int[] input, int nob) {
		Integer[] list = new Integer[input.length];
		for (int i = 0; i < input.length; i++) {
			list[i] = input[i];
		}
		return sort(list, nob);
	}
}
