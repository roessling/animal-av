package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.animator.Animator;
import animal.animator.TimedShow;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;

public class TimedShowImporter extends TimedAnimatorImporter {
	public Object importFrom(int version, int stepNr, StreamTokenizer stok) {
		XProperties props = new XProperties();
		int currentStep = version;
		try {
			props.put(Animator.METHOD_LABEL, stok.sval);

			// 2. set the current step
			props.put(Animator.STEP_LABEL, stepNr);

			// 3. parse the objects concerned
			props.put(Animator.OID_LABEL, ParseSupport.parseObjectIDs(stok,
					"TimedShow"));

			// read in the rest, provided we didn't read EOL...
			if (stok.ttype != StreamTokenizer.TT_EOL) {
				// 2. parse super attributes
				parseASCIIWithoutIDs(stok, currentStep, props
						.getProperty(Animator.METHOD_LABEL), props);
			}
		} catch (IOException e) {
			MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
		}
		return new TimedShow(props);
	}
}
