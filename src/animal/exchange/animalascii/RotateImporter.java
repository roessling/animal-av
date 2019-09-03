package animal.exchange.animalascii;

import java.io.IOException;
import java.io.StreamTokenizer;

import animal.animator.Animator;
import animal.animator.Rotate;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;

public class RotateImporter extends TimedAnimatorImporter {
	/**
	 * Export this object in ASCII format to the PrintWriter passed in.
	 * 
   * @param version the version of the ColorChanger to be parsed
	 * @param stepNr the number of the target animation step
	 * @param stok the StreamTokenizer used for parsing the animator
	 */
	public Object importFrom(int version, int stepNr, StreamTokenizer stok) {
		XProperties props = new XProperties();
		int currentStep = version;
		try {
			// 2. set the current step
			props.put(Animator.STEP_LABEL, stepNr);

			// 3. parse the objects concerned
			props.put(Animator.OID_LABEL, ParseSupport.parseObjectIDs(stok, "Rotate"));

			// read in the rest, provided we didn't read EOL...
			if (stok.ttype != StreamTokenizer.TT_EOL) {
				// 2. parse super attributes
				parseASCIIWithoutIDs(stok, currentStep, props
						.getProperty(Animator.METHOD_LABEL), props);
			}
			// 2. parse keywords "centered on"
			ParseSupport.parseMandatoryWord(stok, "Rotate center key 'centered'",
					"centered");
			ParseSupport.parseMandatoryWord(stok, "Rotate center key 'on'", "on");

			// 3. parse object ID of the rotation center
			props.put(Rotate.CENTER_LABEL, ParseSupport.parseInt(stok,
					"Rotation center object ID"));
			// setCenterNum(Animation.get().mapObjectID(centerNum));

			// 4. parse keywords "by"
			ParseSupport.parseMandatoryWord(stok, "Rotate degree keyword 'by'", "by");

			// 5. parse degrees of rotation
			props.put(Rotate.DEGREES_LABEL, ParseSupport.parseInt(stok,
					"Rotation degrees"));

			// 6. parse keywords "degrees"
			ParseSupport.parseMandatoryWord(stok, "Rotate degree keyword 'degrees'",
					"degrees");
		} catch (IOException e) {
			MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
		}
		return new Rotate(props);
	}
}
