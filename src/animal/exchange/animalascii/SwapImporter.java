/*
 * SwapImporter.java
 * Importer for a swap animator
 *
 * Created on 23. October 2005, 13:14
 *
 * @see animator.Swap for details
 *
 * @author Michael Schmitt
 * @version 0.2.3a
 * @date 2006-02-16
 */

package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.animator.Animator;
import animal.animator.Swap;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;

public class SwapImporter extends TimedAnimatorImporter {
	/**
	 * Export this object in ASCII format to the PrintWriter passed in.
	 * 
   * @param version the version of the ColorChanger to be parsed
	 * @param currentStep the number of the target animation step
	 * @param stok the StreamTokenizer used for parsing the animator
	 */
	public Object importFrom(int version, int currentStep, StreamTokenizer stok) {

		XProperties props = new XProperties();
		/**
		 * set the default swap indices
		 */
		int first = 0, second = 1;
		int type = Swap.POLY;
		int[] idList = new int[1];
		try {
			// 2. set the current step
			props.put(Animator.STEP_LABEL, currentStep);

			// 3. parse the objects concerned
			props.put(Animator.OID_LABEL, ParseSupport.parseObjectIDs(stok, "Swap"));

			// read in the rest, provided we didn't read EOL...
			if (stok.ttype != StreamTokenizer.TT_EOL) {
				// 3.1. parse super attributes
				parseASCIIWithoutIDs(stok, currentStep, props
						.getProperty(Animator.METHOD_LABEL), props);
			}
			// 4. parse keywords "swapping cells"
			ParseSupport.parseMandatoryWord(stok, "Swap cells keyword 'swapping'",
					"swapping");
			ParseSupport.parseMandatoryWord(stok, "Swap cells keyword 'cells'",
					"cells");

			// 5. parse index of the first cell
			first = ParseSupport.parseInt(stok, "First swap cell", 0);

			// 6. parse mandatory word "and"
			ParseSupport.parseMandatoryWord(stok, "Keyord 'and'", "and");

			// 7. parse index of the second cell
			second = ParseSupport.parseInt(stok, "Second swap cell", 0);

			if (version >= 2) {
				// 8. Read in the animation type
				ParseSupport.parseMandatoryWord(stok, "Swap type keyword 'with'",
						"with");
				ParseSupport.parseMandatoryWord(stok, "Swap type keyword 'type'",
						"type");
				type = ParseSupport.parseInt(stok, "Animation type", 0, 1);
			}

			// 9. parse used IDs
			// 9.1. read in length of ID list
			ParseSupport.parseMandatoryWord(stok, "Keyword 'using'", "using");
			idList = new int[ParseSupport.parseInt(stok, "Number of IDs", 5)];

			// 9.2. read in IDs of the swap cells
			ParseSupport.parseMandatoryWord(stok, "Keyword 'IDs'", "IDs");
			for (int i = 0; i < idList.length; i++) {
				idList[i] = ParseSupport.parseInt(stok, "Used IDs", 1);
			}
		} catch (IOException e) {
			MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
		}
		Swap swap = new Swap(props);
		if (idList != null && idList.length > 1) {
			swap.restoreIDs(idList);
		} else {
			MessageDisplay.errorMsg("reassigningSwapCells", MessageDisplay.INFO);
		}
		swap.setObjects(swap.getObjectNums());
		swap.setAnimationType(type);
		swap.setSwapElements(first, second);
		return swap;
	}
}