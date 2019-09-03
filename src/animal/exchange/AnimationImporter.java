package animal.exchange;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import translator.AnimalTranslator;
import animal.gui.AnimalMainWindow;
import animal.main.Animal;
import animal.main.AnimalConfiguration;
import animal.main.Animation;
import animal.main.AnimationListEntry;
import animal.main.AnimationWindow;
import animal.misc.AnimalFileChooser;
import animal.misc.MessageDisplay;

/**
 * This class is the base class for all animation importers. It provides
 * constants for the different import modes possible and some standard methods.
 * 
 * @author Guido R&ouml;&szlig;ling (<A
 *         HREF="mailto:roessling@acm.org">roessling@acm.org</a>)
 * @version 1.0 2000-08-23
 */
public abstract class AnimationImporter implements FormatSpecification {
	/**
	 * The mapping for exporters that can export a static snapshot
	 */
	public static final int IMPORT_STATIC_SNAPSHOT = 1;

	/**
	 * The mapping for importers that can import a step including dynamics
	 */
	public static final int IMPORT_DYNAMIC_STEP = 2;

	protected static AnimalConfiguration animalConfig = null;

  public static AnimalFileChooser      fileChooser;

	private static AnimationImporter lastImporter = null;

	/**
	 * The animation to be imported; stored in an attribute for access reasons
	 */
	protected Animation animationToImport = null;

	/**
	 * The capabilities of the current animation importer class; default is
	 * "single state"
	 */
	protected int importCapabilities = IMPORT_STATIC_SNAPSHOT;

	/**
	 * Marks the steps to import
	 */
	protected boolean[] importSteps = null;

	/**
	 * The type of the current import; default is "single state" mode
	 */
	protected int importType = IMPORT_STATIC_SNAPSHOT;

	/**
	 * The name of the queried format; useful if one class handles multiple
	 * formats
	 */
	String formatName = null;

  private static int                   zoomCounter            = 0;



	public static AnimalFileChooser getFileChooser() {
    if (animalConfig == null) {
			animalConfig = AnimalConfiguration.getDefaultConfiguration();
      if (zoomCounter > 0) {
        for (int i = 0; i < zoomCounter; i++) {
          animalConfig.zoom(true);
        }
      } else {
        for (int i = 0; i > zoomCounter; i--) {
          animalConfig.zoom(false);
        }
      }

    }
		return animalConfig.getImportFileChooser();
	}

	/**
	 * The factory method for retrieving the import module for a given format.
	 * Invoke as <code>AnimationImporter importer; importer =
	 * AnimationImporter.getImporterFor("myFormat")</code> and perform all
	 * subsequent calls on the <code>importer</code> returned.
	 * 
	 * @param format
	 *          the name of the output format, taken from the configuration file
	 *          <code>components.dat</code>
	 * @return the actual animation importer object for handling the output.
	 * @throws java.lang.IllegalArgumentException
	 *           if the specified format is not registered(which might be due to a
	 *           missing in the <em>configuration file</em> or is unknown.
	 */
	@SuppressWarnings("unchecked")
	public static AnimationImporter getImporterFor(String format) {
		String handlerName = getHandlerFor(format);

		if (handlerName == null) {
			return null;
		}

		AnimationImporter handler = null;

		try {
			Class<AnimationImporter> c = 
				(Class<AnimationImporter>)Class.forName(handlerName);

			if (c != null) {
				handler = c.newInstance();
			}
		} catch (Exception e) {
			MessageDisplay.errorMsg("missingOrImproperImporter",
					new String[] {handlerName, format},
					MessageDisplay.RUN_ERROR);
		}

		if (handler != null) {
			handler.init(format);
		}

		lastImporter = handler;
		return handler;
	}

	public static AnimationImporter getLastImporter() {
		return lastImporter;
	}

	/**
	 * Return the name of the handler for the given format
	 * 
	 * @param formatName
	 *          the target output format name
	 * @return the name of the registered output handler, if any; else null.
	 */
	public static String getHandlerFor(String formatName) {
		if (animalConfig.validImportFormat(formatName)) {
			if (animalConfig != null)
				return animalConfig.getImportHandlerFor(formatName);
			return AnimalConfiguration.getDefaultConfiguration().getImportHandlerFor(
					formatName);
		} 
		MessageDisplay.errorMsg("unknownFormat", 
				new String[] { formatName }, MessageDisplay.RUN_ERROR);
		return null;
	}

	/**
	 * The animation to import
	 * 
	 * @return the animal.main.Animation object to be imported
	 * @see animal.main.Animation
	 */
	public Animation getAnimation() {
		return animationToImport;
	}

	/**
	 * Set the format name requested and adjust the import capabilities
	 * 
	 * @param format
	 *          the name of the actual format requested
	 */
	public void init(String format) {
		formatName = format;
	}

	/**
	 * imports all definitions from the file passed, catching any occurring
	 * IOException.
	 * 
	 * Note that the file's first line must be '% Animal' followed by the version
	 * as an integer and a return.
	 * 
	 * @param filename
	 *          the file to be parsed
	 * @return the animation retrieved from the file
	 */
	public Animation importAnimationFrom(String filename) {
		Animation tmpAnimation = null;
		Animal.setAnimationLoadFinished(false);
		try {
			InputStream in = null;
			if (filename.startsWith("http:") || filename.startsWith("https:")
					|| filename.startsWith("file:")) {
				URL targetURL = new URL(filename);
				in = targetURL.openStream();
			} else
				in = new FileInputStream(filename);
			tmpAnimation = importAnimationFrom(in, filename);
			in.close();
		} catch (IOException e) {
			MessageDisplay.errorMsg("ioErrorImporting",
					new String[] { filename, e.getMessage()},
					MessageDisplay.RUN_ERROR);
			e.printStackTrace();
		}

		return tmpAnimation;
	}

	/**
	 * imports a new animation from the input stream or the file name
	 * 
	 * @param in
	 *          an input stream. If not null, reads the animation from this stream
	 * @param filename
	 *          a file name that either describes the name of the already opened
	 *          input stream, or contains the animation text itself
	 * @return the imported animation
	 */
	public abstract Animation importAnimationFrom(InputStream in, String filename);

	/**
	 * set the current exchange state
	 * 
	 * @param newAnimalConfig
	 *          the current exchange state
	 */
	public static void setAnimalConfig(AnimalConfiguration newAnimalConfig) {
		animalConfig = newAnimalConfig;
	}

//	/**
//	 * Return a formatted representation of the steps to import
//	 * 
//	 * @return the imported animation steps
//	 */
//	public String printImportSteps() {
//		if (importSteps == null) {
//			return "No steps set yet.";
//		}
//
//		StringBuilder sb = new StringBuilder(importSteps.length << 2);
//		sb.append("Steps to import: ");
//
//		boolean currentlyInInterval = false;
//		int intervalStart = -1;
//
//		for (int i = 1; i < importSteps.length; i++)
//			if (importSteps[i]) {
//				if (!currentlyInInterval) {
//					sb.append(i);
//					intervalStart = i;
//					currentlyInInterval = true;
//				} else if (currentlyInInterval) {
//					if (intervalStart != (i - 1)) {
//						sb.append("-").append(i - 1);
//					}
//
//					sb.append(" ");
//					currentlyInInterval = false;
//				}
//			}
//
//		return sb.toString();
//	}

	/**
	 * Use this method to provide a short(single line) description of the importer
	 * 
	 * @return a String describing this importer
	 */
	public String toString() {
		return AnimalTranslator.translateMessage("notOverridden",
				new String[] { getClass().getName() });
	}

	/**
	 * dump the current state of the animation to System.out The output is
	 * identical to the display in the AnimationOverview
	 * 
	 * @see animal.gui.AnimationOverview
	 */
	public void dumpState(Animation anim, boolean showAnimators,
			boolean showObjects) {
		if (showAnimators) {
			AnimationListEntry[] info = anim.getAnimatorList();

			for (int j = 0; j < info.length; j++) {
				if (info[j].mode == AnimationListEntry.ANIMATOR) {
					MessageDisplay.message(info[j].animator.toString());
				} else if (info[j].mode == AnimationListEntry.STEP) {
					MessageDisplay.message(info[j].link.toString());
				}
			}

			if (showObjects) {
				MessageDisplay.message("*****************");
			}

			info = null;
		}

		if (showObjects) {
			for (int i = 0; i < anim.getGraphicObjects().size(); i++) {
				MessageDisplay.message(anim.getGraphicObjects().elementAt(i)
						+ " NUM: "
						+ anim.getGraphicObjects().elementAt(i).getNum(false));
			}
		}
	}

	public static boolean finalizeAnimationLoading(Animation tmpAnimation,
			String filename, String targetFormat) {
		if (tmpAnimation == null) {
			return false;
		}
		Animal animal = Animal.get();

		if (animal.setAnimation(tmpAnimation)) {
			AnimationWindow animWin = AnimalMainWindow.WINDOW_COORDINATOR
					.getAnimationWindow(false);
			animal.resetChange();
			MessageDisplay.message(AnimalTranslator.translateMessage(
					"animImportDone", new String[] { filename, targetFormat }));
			if (!animWin.isVisible())
				MessageDisplay.message(AnimalTranslator
						.translateMessage("animWinReminder"));
			animalConfig.setCurrentFilename(filename);
			animalConfig.setCurrentFormat(targetFormat);
			//animWin.getScrollPane().setScrollPosition(new Point(0, 0));

			/*
			 * TODO: update this entry "appropriately"
			 */
			// getAnimationWindow(false).getMagnificationSlider().setValue(100);
			animWin.setVisible(true);
			animWin.startOfAnimation();
			String animTitle = tmpAnimation.getTitle();
			if (animTitle == null)
				animTitle = filename;
      String animAuthor= tmpAnimation.getAuthor();
      if (animAuthor == null)
        animAuthor = "?";

			animWin.setTitle("Animal Animation: " + animTitle + " (" +animAuthor +")");
			
			int nrLabels = tmpAnimation.nrOfLabels();
			if (nrLabels > 0 && !Animal.CrypToolMode) {
//			  if (Animal.CrypToolMode)
//			    System.err.println("CrypTool " +nrLabels);
			   AnimalMainWindow.WINDOW_COORDINATOR.getTimeLineWindow(false).setVisible(true);
			}

			return true;
		}

		return false;
	}

	/**
	 * Import an animation by picking the target format and file to import.
	 * 
	 * @return true if importing was successful, else false.
	 */
	public static boolean importAnimation() {
		Animal animal = Animal.get();
		String filename = AnimationImporter.getFileChooser().openForImport(animal);
		if (filename == null)
			return false;
		int index = filename.lastIndexOf(System.getProperty("file.separator"));

		if (index != -1) {
			animalConfig.setCurrentDirectory(filename.substring(0, index));
		}

		String targetFormat = AnimationImporter.getFileChooser().getFormat();
		if ((filename != null) && (targetFormat != null)) {
			return importAnimation(filename, targetFormat);
		}
		return false;
	}

	/**
	 * Import an animation by "guessing" the format from the file name.
	 * 
	 * @return true if importing was successful, else false.
	 */
	public static boolean importAnimation(String filename) {
//		if (filename.endsWith(".animal") || filename.endsWith(".amb")
//				|| filename.endsWith(".amz")) {
//			boolean wasSuccessful = loadBinaryFile(filename);
//
//			if (wasSuccessful) {
//				return wasSuccessful;
//			}
//		}

		// otherwise, "guess" it is Animal's native format!
		return importAnimation(filename, AnimalConfiguration.DEFAULT_FORMAT);
	}

//	/**
//	 * loads the file with the given filename or creates a new file.
//	 * 
//	 * @param filename
//	 *          the filename of the file to be loaded. If null or empty, a new
//	 *          Animation is generated.
//	 * @return true if the file could be loaded or a new Animation was created
//	 *         because <i>filename</i> was null or empty.
//	 */
//	static boolean loadBinaryFile(String filename) {
//		Animal animal = Animal.get();
//		InputStream in = null;
//		BufferedInputStream bis = null;
//		// int filetype;
//		boolean zipped = true;
//
//		if ((filename == null) || filename.equals("")) {
//			animal.newFile();
//
//			return true;
//		}
//
//		try {
//			in = new FileInputStream(filename);
//
//			// check if the file is compressed
//			try {
//				in = new GZIPInputStream(in);
//			} catch (IOException e) {
//				// could not open the file as a compressed stream. As it could
//				// be opened before, this must be an uncompressed stream.
//				in.close();
//				zipped = false;
//				in = new FileInputStream(filename);
//			}
//		} catch (IOException e) {
//			MessageDisplay.errorMsg("ioErrorImporting",
//					new String[] { filename, e.getMessage()}, 
//					MessageDisplay.PROGRAM_ERROR);
//		}
//
//		try {
//			bis = new BufferedInputStream(in);
//
//			ObjectInputStream s = new ObjectInputStream(bis);
//
//			// don't work on this.animation, as readObject changes the
//			// Animation and thus would result in a Dialog "File has Changed"
//			// in setAnimation.
//			Animation animation = (Animation) s.readObject();
//
//			// animation.sequentialize();
//			bis.close();
//
//			// setting the animation can be aborted if "cancel" is pressed
//			// in the "save file?"-dialog. */
//			if (animal.setAnimation(animation)) {
//				// message after the file has been loaded
//				StringBuilder sb = new StringBuilder();
//				sb.append(filename + " loaded as ");
//
//				if (zipped) {
//					sb.append("compressed ");
//				}
//
//				sb.append("binary");
//				MessageDisplay.message(sb.toString());
//				animation.resetChange();
//				animal.resetChange();
//			}
//
//			return true;
//		} catch (ClassNotFoundException cnfe) {
//			MessageDisplay.errorMsg(AnimalTranslator.translateMessage(
//					"classNotFoundException", new Object[] { cnfe.getMessage() }),
//					MessageDisplay.RUN_ERROR);
//		} catch (IOException e) {
//			// set up a StreamTokenizer on the file
//			// could not open the file as a binary stream. As it could
//			// be opened before, this must be an ascii stream.
//			try {
//				bis.close();
//				in.close();
//			} catch (IOException e2) {
//			}
//		}
//
//		return false;
//	}

	public static boolean importAnimation(InputStream in, String filename) {
		return importAnimation(in, filename, "animation/animal-ascii-compressed");
	}

	/**
	 * Import an animation of the target format from the given file name.
	 * 
	 * @param filename
	 *          the file name for the animation to be imported
	 * @param targetFormat
	 *          the MIME type of the animation to import
	 * @return true if importing was successful, else false.
	 */
	public static boolean importAnimation(String filename, String targetFormat) {
		return importAnimation(null, filename, targetFormat);
	}

	/**
	 * Import an animation of the target format from the given file name.
	 * 
	 * @param in
	 *          the InputStream from which to read (may also be null)
	 * @param filename
	 *          the file name for the animation to be imported
	 * @param targetFormat
	 *          the MIME type of the animation to import
	 * @return true if importing was successful, else false.
	 */
	public static boolean importAnimation(InputStream in, String filename,
			String targetFormat) {
		Animal.setAnimationLoadFinished(false);
		AnimationImporter importer = AnimationImporter.getImporterFor(targetFormat);

		if (importer != null) {
			Animation tmpAnimation = null;

			if (in == null) {
				tmpAnimation = importer.importAnimationFrom(filename);
			} else {
				tmpAnimation = importer.importAnimationFrom(in, filename);
			}

			return finalizeAnimationLoading(tmpAnimation, filename, targetFormat);
		}

		return false;
	}

  /**
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public static void zoom(boolean zoomIn) {

    if (zoomIn) {
      if (zoomCounter < 6)
        zoomCounter++;

    } else {
      if (zoomCounter > -1)
        zoomCounter--;
    }

    if (animalConfig != null)
      animalConfig.zoom(zoomIn);
  }
}
