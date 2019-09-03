package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.animator.Animator;
import animal.animator.TimedAnimator;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;
import animalscript.core.AnimalParseSupport;

public class TimedAnimatorImporter extends AnimatorImporter {
	/**
	 * This method is used for parsing the animator from the Stream without IDs.
	 * 
	 * @param stok
	 *          the StreamTokenizer for parsing the stream
	 * @param step
	 *          the target step for this animator
	 * @param type
	 *          the command read in; may be used for toggling activities if the
	 *          animator handles more than only a single command.
	 * @param properties the properties used for parsing attributes
	 */
	public void parseASCIIWithoutIDs(StreamTokenizer stok, 
			 int step, String type,
			XProperties properties) {
		try {
			// 2. parse method
			properties.put(Animator.METHOD_LABEL, AnimalParseSupport.parseText(
					stok, type + " method"));

			// 3. parse total time or ticks used
//			int time = 0;
			if (ParseSupport.parseOptionalWord(stok, type + " keyword 'timed'",
					"timed")) {
				// 4. parse "starting after"
				ParseSupport.parseWord(stok, type + " keyword 'starting'", "starting");
				ParseSupport.parseWord(stok, type + " keyword 'after'", "after");

				// 5. parse delay
				properties.put(TimedAnimator.OFFSET_LABEL, ParseSupport.parseInt(stok,
						type + " delay", 0));

				// 6. parse delay units
				// boolean delayUnits
				String delayMode = ParseSupport.parseWord(stok, type + " delay units");
				properties.put(TimedAnimator.TIME_UNIT_LABEL, !(delayMode
						.equalsIgnoreCase("ms")));
			}

			// 7. parse keyword "within"(duration), if used
			if (ParseSupport.parseOptionalWord(stok, type + " keyword 'within'",
					"within")) {
				// 5. parse duration
				properties.put(TimedAnimator.DURATION_LABEL, ParseSupport.parseInt(
						stok, type + " duration", 0));

				// 6. parse delay units
				// boolean durationUnits
				String durationMode = ParseSupport.parseWord(stok, type
						+ " delay units");
				properties.put(TimedAnimator.TIME_UNIT_LABEL, !(durationMode
						.equalsIgnoreCase("ms")));
			}
		} catch (IOException e) {
			MessageDisplay
					.errorMsg("XXXX" + e.getMessage(), MessageDisplay.RUN_ERROR);
		}
	}
}
