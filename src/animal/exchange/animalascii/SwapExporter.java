/*
 * SwapExporter.java
 * Exporter for a swap animator
 *
 * Created on 23. October 2005, 13:12
 *
 * @see animator.Swap for details
 *
 * @author Michael Schmitt
 * @version 0.2.1
 * @date 2005-11-18
 */

package animal.exchange.animalascii;

import java.io.PrintWriter;

import animal.animator.Animator;
import animal.animator.Swap;

public class SwapExporter extends TimedAnimatorExporter {

	/**
	 * Export this object in ASCII format to the PrintWriter passed in.
	 * 
	 * @param pw
	 *          the PrintWriter to write to
	 * @param animator
	 *          the current Animator object
	 */
	public void exportTo(PrintWriter pw, Animator animator) {
		// 1. write out the info contained in the ancestor
		// note: this ends without a space
		super.exportTo(pw, animator);
		Swap swap = (Swap) animator;

		// 2. append the space to the output
		pw.print(" swapping cells ");

		// 3. write out the cell numbers
		pw.print(swap.getSwapElements()[0]);
		pw.print(" and ");
		pw.print(swap.getSwapElements()[1]);

		// 4. write out the animation type
		pw.print(" with type ");
		pw.print(swap.getAnimationType());

		// 5. write out the length of the ID list
		int[] IDs = swap.exportIDs();
		pw.print(" using ");
		pw.print(IDs.length);

		// 6. write out the used IDs
		pw.print(" IDs");
		for (int i = 0; i < IDs.length; i++) {
			pw.print(' ');
			pw.print(IDs[i]);
		}
		pw.println();
	}
}
