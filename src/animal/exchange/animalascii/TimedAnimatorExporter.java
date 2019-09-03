package animal.exchange.animalascii;

import java.io.PrintWriter;

import animal.animator.Animator;
import animal.animator.TimedAnimator;

public class TimedAnimatorExporter extends AnimatorExporter {
	/**
	 * Export this object in ASCII format to the PrintWriter passed in.
	 * 
	 * @param pw
	 *          the PrintWriter to write to
	 */
	public void exportTo(PrintWriter pw, Animator animator) {
		// 1. write out the info contained in the ancestor
		// note: this ends without a space
		super.exportTo(pw, animator);

		TimedAnimator ta = (TimedAnimator) animator;
		int duration = ta.getDuration(), offset = ta.getOffset();
		boolean unitIsTicks = ta.isUnitIsTicks();

		// 2. print out the space
		pw.print(" by ");

		// 3. print out the method used
		pw.print('\"');
		pw.print(ta.getMethod());
		pw.print('\"');

		// 4. write out the offset
		if (offset != 0) {
			pw.print(" timed starting after ");
			pw.print(offset);

			pw.print((unitIsTicks) ? " ticks" : " ms");
		}

		// 5. write out the duration
		if (duration != 0) {
			pw.print(" within ");
			pw.print(duration);

			// 6. write out the unit
			if (offset != 0 || duration != 0)
				pw.print((unitIsTicks) ? " ticks" : " ms");
		}
	}
}
