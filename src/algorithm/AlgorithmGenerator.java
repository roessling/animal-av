/*
 * Created on 10.11.2004
 *
 */
package algorithm;

import algorithm.animalTranslator.AnimalTranslator;
import algorithm.fileHandler.FileHandler;
import algorithm.search.BinarySearch;
import algorithm.search.InterpolationSearch;
import algorithm.search.SequentialSearch;
import algorithm.stringSearch.BMStringSearch;
import algorithm.stringSearch.BruteForceStringSearch;
import algorithm.stringSearch.KMPStringSearch;

/**
 * this class implements an AlgorithmGenerator, that can automatically create
 * animations in 'Animal Script' for various algorithms and save them to disk.
 * 
 * @author Michael Maur <mmaur@web.de>
 */
public class AlgorithmGenerator {

	/**
	 * fh is a Filehandler, who does the disk-access to write the animation file -
	 * commands are passed to it one by one.
	 */
	private FileHandler fh;

	/**
	 * at is the AnimalTranslator that generates the Code in Animal Script from
	 * the commands, the Customized CodeGenerators for binarySearch and so on pass
	 * to it.
	 */
	private AnimalTranslator at;

	/**
	 * passes the fileName to the fileHandler
	 * 
	 * @param fileName
	 *          the name of the file, the animation will be saved to
	 */
	public void setFileName(String fileName) {
		fh.setFileName(fileName);
	}

	/**
	 * causes the fileHandler to save the file to Disk
	 */
	public void saveToFile() {
		fh.save();
	}

	/**
	 * causes the fileHandler to save the animation to a file named as the given
	 * parameter
	 * 
	 * @param fileName
	 *          the name of the file, the animation will be saved to
	 */
	public void saveToFile(String fileName) {
		fh.setFileName(fileName);
		saveToFile();
	}

	/**
	 * a constructor that initializes the Algorithmgenerator by creating the
	 * FileHandler and the AnimalTranslator to be used.
	 */
	public AlgorithmGenerator() {
		try {
			fh = new FileHandler();
			at = new AnimalTranslator(fh);
			System.out.println("AlgorithmGenerator initialized");
		} catch (Exception ex) {
			System.out.println("error initializing AlgorithmGenerator:");
			System.out.println("     " + ex.getMessage());
		}
	}

	/**
	 * a constructor that initializes the Algorithmgenerator by creating the
	 * FileHandler and the AnimalTranslator to be used and passes the fileName to
	 * the FileHandler.
	 * 
	 * @param fileName
	 *          the name of the file, the animation will be saved to
	 */
	public AlgorithmGenerator(String fileName) {
		try {
			fh = new FileHandler(fileName);
			at = new AnimalTranslator(fh);
			System.out.println("AlgorithmGenerator initialized");
		} catch (Exception ex) {
			System.out.println("error initializing AlgorithmGenerator:");
			System.out.println("     " + ex.getMessage());
		}
	}

	/**
	 * generates an animation for a binary search, using AnimalTranslator and the
	 * class BinarySearch, that implements the animations structure
	 * 
	 * @param dataArray
	 *          the int-array to be shown in the animation
	 * @param toSearch
	 *          the int-item to be looked for in the binary search
	 */
	public void generateBinarySearch(int[] dataArray, int toSearch,
			boolean displayJava) {
		try {
			BinarySearch binS = new BinarySearch(at, displayJava);
			binS.initialize(dataArray, toSearch);
			binS.generateAnimation();
			System.out.println("BinarySearch-animation created");
		} catch (Exception ex) {
			System.out.println("error creating algorithm for binary search:");
			System.out.println("     " + ex.getMessage());
		}
	}

	public void generateInterpolationSearch(int[] dataArray, int toSearch,
			boolean displayJava) {
		try {
			InterpolationSearch itS = new InterpolationSearch(at, displayJava);
			itS.initialize(dataArray, toSearch);
			itS.generateAnimation();
			System.out.println("InterpolationSearch-animation created");
		} catch (Exception ex) {
			System.out.println("error creating algorithm for interpolation-search:");
			System.out.println("     " + ex.getMessage());
		}
	}

	/**
	 * generates an animation for a sequential search, using AnimalTranslator and
	 * the class SequentialSearch, that implements the animations structure
	 * 
	 * @param dataArray
	 *          the int-array to be shown in the animation
	 * @param toSearch
	 *          the int-item to be looked for in the binary search
	 */
	public void generateSequentialSearch(int[] dataArray, int toSearch,
			boolean displayJava) {
		try {
			SequentialSearch seqS = new SequentialSearch(at, displayJava);
			seqS.initialize(dataArray, toSearch);
			seqS.generateAnimation();
			System.out.println("SequentialSearch-animation created");
		} catch (Exception ex) {
			System.out.println("error creating algorithm for sequential search:");
			System.out.println("     " + ex.getMessage());
		}
	}

	/**
	 * generates an animation for a bruteforce String-search, using
	 * AnimalTranslator and the class BruteForceStringSearch, that implements the
	 * animations structure
	 * 
	 * @param text
	 *          the String, in which toSearch shall be found
	 * @param toSearch
	 *          the SubString to look for in the search
	 */
	public void generateBruteForceStringSearch(String text, String toSearch) {
		try {
			BruteForceStringSearch bfSS = new BruteForceStringSearch(at);
			bfSS.initialize(text, toSearch);
			bfSS.generateAnimation();
			System.out.println("BruteforceStringSearch-animation created");
		} catch (Exception ex) {
			System.out
					.println("error creating algorithm for bruteforceStringSearch:");
			System.out.println("     " + ex.getMessage());
		}
	}

	/**
	 * generates an animation for a KMP String-search, using AnimalTranslator and
	 * the class KMPStringSearch, that implements the animations structure
	 * 
	 * @param text
	 *          the String, in which toSearch shall be found
	 * @param toSearch
	 *          the SubString to look for in the search
	 */
	public void generateKMPStringSearch(String text, String toSearch) {
		try {
			KMPStringSearch kMPSS = new KMPStringSearch(at);
			kMPSS.initialize(text, toSearch);
			kMPSS.generateAnimation();
			System.out.println("KMP-StringSearch-animation created");
		} catch (Exception ex) {
			System.out.println("error creating algorithm for KMP-StringSearch:");
			System.out.println("     " + ex.getMessage());
		}
	}

	/**
	 * generates an animation for a BM-String-search, using AnimalTranslator and
	 * the class BMStringSearch, that implements the animations structure
	 * 
	 * @param text
	 *          the String, in which toSearch shall be found
	 * @param toSearch
	 *          the SubString to look for in the search
	 */
	public void generateBMStringSearch(String text, String toSearch) {
		try {
			BMStringSearch bMSS = new BMStringSearch(at);
			bMSS.initialize(text, toSearch);
			bMSS.generateAnimation();
			System.out.println("BM-StringSearch-animation created");
		} catch (Exception ex) {
			System.out.println("error creating algorithm for BM-StringSearch:");
			System.out.println("     " + ex.getMessage());
		}
	}

}