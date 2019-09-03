package animal.exchange.xaal;

import java.io.PrintWriter;

import animal.animator.Animator;

/**
 * This class supports exporting animators. Provides the common features
 * for exporting a given animator, such as the file version, step number,
 * and animator name. 
 *
 * @author Guido Roessling (roessling@acm.org>
 * @version 0.7 05.09.2007
 */
public class AnimatorExporter implements Exporter {
	/**
	 * Export this object in ASCII format to the PrintWriter passed in.
	 * 
	 * @param pw the PrintWriter to write to
	 * @param animator the current Animator object
	 */
	public void exportTo(PrintWriter pw, Animator animator) {
		// 1. write the file version
		pw.print(animator.getFileVersion());
		pw.print(" Step ");

		// 2. write the step number
		pw.print(animator.getStep());
		pw.print(' ');

		// 3. write out animator name
		pw.print(animator.getAnimatorName());

		int[] objectNums = animator.getObjectNums();
		// 3. write out the numbers of objects worked on
		for (int i = 0; objectNums != null && i < objectNums.length; i++) {
			pw.print(" ");
			pw.print(objectNums[i]);
		}
	}
}
