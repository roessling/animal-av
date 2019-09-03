package animal.main;

import java.io.File;

import translator.AnimalTranslator;
import animal.exchange.AnimationExporter;
import animal.exchange.AnimationImporter;
import animal.misc.MessageDisplay;

public class AnimationTester {
	public static void main(String[] animNames) {
		int startPos = 0;
		if (animNames == null || animNames.length < 1) {
			System.err
					.println("Usage: java animal.main.AnimationTester [-terse|-verbose] [-q|-quick] [-x] format filename1 [filename2...]");
			System.exit(0);
		}
		boolean verboseMode = false, performStepChecking = true;
		if (animNames[0].equalsIgnoreCase("-terse"))
			startPos++;
		if (animNames[startPos].equalsIgnoreCase("-verbose")) {
			startPos++;
			verboseMode = true;
		}
		if (animNames[startPos].equalsIgnoreCase("-quick")
				|| animNames[startPos].equalsIgnoreCase("-q")) {
			performStepChecking = false;
			startPos++;
		}

		if (animNames != null && animNames.length > 1) {
			boolean useConversion = animNames[startPos].equalsIgnoreCase("-x");
			Animal animal = Animal.get();
			Animation animation = null;
			int i = (useConversion) ? startPos + 1 : startPos;
			String formatName = animNames[i++];

			if (verboseMode) {
				MessageDisplay.message("animTesterRequest");
				for (; i < animNames.length; i++)
					MessageDisplay.message(animNames[i] + " ");
				MessageDisplay.message(MessageDisplay.LINE_FEED);
			}
			for (i = (useConversion) ? startPos + 2 : startPos + 1; i < animNames.length; i++) {
				String currentFileName = animNames[i];
				if (currentFileName.endsWith(".zip")) {
	    		// do nothing
				}
				try {
					File file = new File(currentFileName);
					MessageDisplay.message("animTesterFNSize",
							new Object[] {currentFileName, Long.valueOf(file.length())});
					file = null;
					long timeNow = System.currentTimeMillis();
					if (AnimationImporter.importAnimation(currentFileName, formatName)) {
						animation = animal.getAnimation();
						if (animation != null) {
							if (verboseMode)
								MessageDisplay.message("animTestNrSOA",
										new Integer[] {
											Integer.valueOf(animation.getNrAnimationSteps()),
											Integer.valueOf(animation.getNrObjects()),
											Integer.valueOf(animation.getNrAnimators()) });
							if (!performStepChecking)
								animal.testAnimation(
										AnimalTranslator.translateMessage("finishedTest",
												new Object[] {currentFileName, 
												Long.valueOf(System.currentTimeMillis() - timeNow) }));
							else {
								MessageDisplay.message("finishedLoading",
										currentFileName);
								if (verboseMode) {
									MessageDisplay.message("chosenLang",
											animation.getLanguage());
									animation.determineVisualizationSize();
									animal.showBoundingBox();
								}
							}
							if (useConversion)
								AnimationExporter.exportAnimation(animation);
						}
					} else
						MessageDisplay.errorMsg("errorLoadingTestAnim", 
								currentFileName, MessageDisplay.RUN_ERROR);
				} catch (Exception e) {
					MessageDisplay.errorMsg("exceptionDuringTest",e.getMessage(),
							MessageDisplay.RUN_ERROR);
					e.printStackTrace();
				}
			}
			MessageDisplay.message("goodbye");
		} else
			MessageDisplay.message("animTesterUsage");
		System.exit(0);
	}
}