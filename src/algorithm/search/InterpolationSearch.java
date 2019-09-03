/*
 * Created on 02.01.2005
 *
 */
package algorithm.search;

import algorithm.animalTranslator.AnimalTranslator;

/**
 * this class implements the algorithm to create AnimalScript-code for an
 * InterpolationSearch
 * 
 * @author Michael Maur <mmaur@web.de>
 */
public class InterpolationSearch {

	BinarySearch bs;
	public InterpolationSearch() {
		// do nothing
	}

	/**
	 * constructor that saves a pointer to the animalTranslator to be used and
	 * determines wether java-code or pseudo-code will be displayed
	 * 
	 * @param newAT
	 *          AnimalTranslator to be used
	 * @param displayJavaCode
	 *          flag that determines wether java-Code will be displayed instead of
	 *          pseudo-code
	 */
	public InterpolationSearch(AnimalTranslator newAT, boolean displayJavaCode) {
		// creates BinarySearch with interpolation
		bs = new BinarySearch(newAT, true, displayJavaCode);
	}

	/**
	 * initializes the instance of InterpolationSearch by saving the int-array
	 * (should be sorted) and the element to be searched in that array
	 * 
	 * @param intArrayToSort
	 *          array of int's, in which a certain element will be searched
	 *          (sorted, if search shall succeed)
	 * @param toBeSearched
	 *          int - element to be searched in the int-array
	 */
	public void initialize(int[] intArrayToSort, int toBeSearched) {
		bs.initialize(intArrayToSort, toBeSearched);
	}

	/**
	 * causes generation of the animation for the binary search
	 * 
	 * @throws Exception
	 *           in case initialization of the instance has been forgotten
	 */
	public void generateAnimation() throws Exception {
		bs.generateAnimation();
	}

}
