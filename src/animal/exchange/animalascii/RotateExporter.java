package animal.exchange.animalascii;

import java.io.PrintWriter;

import animal.animator.Animator;
import animal.animator.Rotate;

public class RotateExporter extends TimedAnimatorExporter {
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
		Rotate rotate = (Rotate) animator;

		// 2. append the space to the output
		pw.print(" centered on ");

		// 3. write out the object number of the rotation center
		pw.print(rotate.getCenterNum());
		pw.print(" by ");

		// 5. write out the degrees of the rotation
		pw.print(rotate.getDegrees());
		pw.println(" degrees");
	}
}
