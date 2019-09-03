package animal.exchange;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

import translator.AnimalTranslator;
import animal.animator.Animator;
import animal.animator.Move;
import animal.animator.Rotate;
import animal.animator.Show;
import animal.animator.TimedShow;
import animal.graphics.PTGraphicObject;
import animal.gui.GraphicVector;
import animal.main.Animal;
import animal.main.AnimalConfiguration;
import animal.main.Animation;
import animal.main.AnimationListEntry;
import animal.main.AnimationState;
import animal.main.Link;
import animal.misc.AnimalFileChooser;
import animal.misc.MessageDisplay;

/**
 * This class is the base class for all animation exporters. It provides
 * constants for the different export modes possible and some standard methods.
 * 
 * @author Guido R&ouml;&szlig;ling ( <A
 *         HREF="mailto:roessling@acm.org">roessling@acm.org </a>)
 * @version 1.0 2000-08-23
 */
public abstract class AnimationExporter implements FormatSpecification,
		ActionListener {

	/**
	 * The mapping for exporters that can export a static snapshot
	 */
	public static final int EXPORT_STATIC_SNAPSHOT = 1;

	/**
	 * The mapping for exporters that can export a step including dynamics
	 */
	public static final int EXPORT_DYNAMIC_STEP = 2;

	/**
	 * The mapping for exporters that can export a full dynamic animation
	 */
	public static final int EXPORT_FULL_ANIMATION = 4;

	/**
	 * The mapping for exporters that can export selected individual steps
	 */
	public static final int EXPORT_INDIVIDUAL_STEPS = 8;

	/**
	 * The mapping for exporters that can scale the display
	 */
	public static final int EXPORT_SCALE_DISPLAY = 16;

	/**
	 * The mapping for exporters that can adjust the display speed
	 */
	public static final int EXPORT_ADJUST_SPEED = 32;

	public static AnimalConfiguration animalConfig;

	private AnimationExporter exporter = null;

	private ExportModeChooser exportModeChooser;

	/**
	 * The animation to be exported; stored in an attribute for access reasons
	 */
	protected Animation animationToExport = null;

	/**
	 * The capabilities of the current animation exporter class; default is
	 * "single state"
	 */
	protected int exportCapabilities = EXPORT_STATIC_SNAPSHOT;

	/**
	 * Marks the steps to export
	 */
	protected boolean[] exportSteps = null;

	/**
	 * The type of the current export; default is "single state" mode
	 */
	protected int exportType = EXPORT_STATIC_SNAPSHOT;

	/**
	 * The filename to use for animation export
	 */
	String filename = null;

	/**
	 * The name of the queried format; useful if one class handles multiple
	 * formats
	 */
	String formatName = null;

	/**
	 * set the current exchange state
	 * 
	 * @param newConfig
	 *          the current exchange state
	 */
	public static void setAnimalConfig(AnimalConfiguration newConfig) {
		animalConfig = newConfig;
	}

	/**
	 * The factory method for retrieving the export module for a given format.
	 * Invoke as <code>AnimationExporter exporter; exporter =
	 * AnimationExporter.getExporterFor("myFormat")</code> and perform all
	 * subsequent calls on the <code>exporter</code> returned.
	 * 
	 * @param formatName
	 *          the name of the output format, taken from the configuration file
	 *          <code>components.dat</code>
	 * @return the actual animation exporter object for handling the output.
	 * @throws java.lang.IllegalArgumentException
	 *           if the specified format is not registered(which might be due to a
	 *           missing in the <em>configuration file</em> or is unknown.
	 */
	public static AnimationExporter getExporterFor(String formatName) {
		return animalConfig.getExportHandlerFor(formatName);
	}

	public boolean canAdjustSpeed() {
		return (exportCapabilities & EXPORT_ADJUST_SPEED) == EXPORT_ADJUST_SPEED;
	}

	public boolean canExportStaticSnapshot() {
		return (exportCapabilities & EXPORT_STATIC_SNAPSHOT) == EXPORT_STATIC_SNAPSHOT;
	}

	public boolean canExportDynamicStep() {
		return (exportCapabilities & EXPORT_DYNAMIC_STEP) == EXPORT_DYNAMIC_STEP;
	}

	public boolean canExportFullAnimation() {
		return (exportCapabilities & EXPORT_FULL_ANIMATION) == EXPORT_FULL_ANIMATION;
	}

	public boolean canExportSelectedSteps() {
		return (exportCapabilities & EXPORT_INDIVIDUAL_STEPS) == EXPORT_INDIVIDUAL_STEPS;
	}

	public boolean canScaleDisplay() {
		return (exportCapabilities & EXPORT_SCALE_DISPLAY) == EXPORT_SCALE_DISPLAY;
	}

	/**
	 * Return the methods for animation export supported by this exporter
	 * 
	 * @return the export capabilities of the current exporter
	 */
	public int getExportCapabilities() {
		return exportCapabilities;
	}

	/**
	 * Return the supported formats as a String array
	 * 
	 * @return the supported formats
	 */
	public static String[] getExtensions() {
		return animalConfig.getExportExtensions();
	}

	/**
	 * Return the supported formats as a String array
	 * 
	 * @return the supported formats
	 */
	public String[] getFormatNames() {
		return AnimalConfiguration.getDefaultConfiguration().getExportFormats();
	}

	/**
	 * Export the animation to a file of the given name. Note that you must set
	 * the animation by calling <code>setAnimation</code> <strong>before
	 * </STRON> you call this method.
	 * 
	 * Use <code>exportAnimationTo(System.out)</code> instead if you want the
	 * output to be given on the terminal.
	 * 
	 * @param fileName
	 *          the name of the output file to export to.
	 * @return true if the operation was successful
	 * @see #setAnimation(Animation)
	 */
	public abstract boolean exportAnimationTo(String fileName);

	/**
	 * The animation to export
	 * 
	 * @return the animal.main.Animation object to be exported
	 * @see animal.main.Animation
	 */
	public Animation getAnimation() {
		return animationToExport;
	}

	/**
	 * Set the format name requested and adjust the export capabilities
	 * 
	 * @param format
	 *          the name of the actual format requested
	 */
	public void init(String format) {
		formatName = format;
	}

	/**
	 * Return a formatted representation of the steps to export
	 */
	public String printExportSteps() {
		if (exportSteps == null)
			return AnimalTranslator.translateMessage("noStepsSet");
		StringBuilder sb = new StringBuilder(exportSteps.length << 2);
		sb.append(AnimalTranslator.translateMessage("stepsToExport"));
		boolean currentlyInInterval = false;
		int intervalStart = -1;
		for (int i = 1; i < exportSteps.length; i++)
			if (exportSteps[i])
				if (!currentlyInInterval) {
					sb.append(i);
					intervalStart = i;
					currentlyInInterval = true;
				} else if (currentlyInInterval) {
					if (intervalStart != i - 1)
						sb.append("-").append(i - 1);
					sb.append(" ");
					currentlyInInterval = false;
				}
		return sb.toString();
	}

	public int[] determineExportableGraphicObjects() {
		if (animationToExport == null)
			return null;
		int maxNrOfObjects = animationToExport.getNextGraphicObjectNum();
		int[] mustBeExported = new int[maxNrOfObjects + 1];
		int currentStep = Link.START, i = 0;
		AnimationListEntry[] localinfo = animationToExport.getAnimatorList();
		AnimationState animState = new AnimationState(animationToExport);
		boolean[] exportThese = new boolean[maxNrOfObjects + 1];
		while (currentStep != Link.END) {
			if (exportSteps[currentStep]) {
				GraphicVector objectsVisibleInStep = animState.getCurrentObjects();
				for (int j = 0; j < objectsVisibleInStep.getSize(); j++) {
					PTGraphicObject ptgo = objectsVisibleInStep.elementAt(j)
							.getGraphicObject();
					int currentObject = ptgo.getNum(false);
					exportThese[currentObject] = true;
				}
			}
			// go to next step!
			currentStep = animState.getNextStep();
			animState.setStep(currentStep, true);
		}
		currentStep = Link.START;
		for (i = 0; i < localinfo.length; i++) { // for all in localinfo
			AnimationListEntry ali = localinfo[i];
			if (ali.mode == AnimationListEntry.STEP) {
				currentStep = ali.link.getStep();
				animState.setStep(currentStep, true);
			}
			// must also check for visible objects!
			else if (ali.mode == AnimationListEntry.ANIMATOR) {
				Animator animator = ali.animator;
				if (exportSteps[currentStep]) {
					int baseNum = -1;
					if (animator instanceof Move)
						baseNum = ((Move) animator).getMoveBaseNum();
					else if (animator instanceof Rotate)
						baseNum = ((Rotate) animator).getCenterNum();
					if (baseNum != -1 && mustBeExported[baseNum] == 0)
						mustBeExported[baseNum] = -currentStep;
				}
				if (animator instanceof Show || animator instanceof TimedShow) {
					boolean isShow = (animator instanceof Show) ? ((Show) animator)
							.isShow() : ((TimedShow) animator).isShow();
					int[] objectNums = animator.getObjectNums();
					for (int objectNr = 0; objectNr < objectNums.length; objectNr++)
						if (isShow)
							mustBeExported[objectNums[objectNr]] = currentStep;
						else {
							// check if object was visible in previous export
							// step...!
							int checkStep = mustBeExported[objectNums[objectNr]];
							mustBeExported[objectNums[objectNr]] = (checkStep < currentStep) ? checkStep
									: 0;
						}
				}
			}
		}
		return mustBeExported;
	}

	/**
	 * The setter for the animation to export
	 * 
	 * @param anim
	 *          the animal.main.Animation object to be exported
	 * @see animal.main.Animation
	 */
	public void setAnimation(Animation anim) {
		exporter = this;
		animationToExport = anim;
		int nrOfSteps = animationToExport.getMaxStepNum();
		exportSteps = new boolean[nrOfSteps + 1];
	}

	public void chooseExportMode() {
		exportModeChooser = new ExportModeChooser(exporter);
	}

	/**
	 * Method for setting the animation export mode using one of the constants.
	 * 
	 * @param targetMode
	 *          the export mode. Please use <em>only</em> the following
	 *          constants provided:
	 *          <UL>
	 *          <li>EXPORT_STATIC_SNAPSHOT</li>,
	 *          <li>EXPORT_DYNAMIC_STATE</li>,
	 *          </UL>
	 * @see #EXPORT_STATIC_SNAPSHOT
	 * @see #EXPORT_DYNAMIC_STEP
	 */
	public void setExportMode(int targetMode) throws IllegalArgumentException {
		if ((targetMode & exportCapabilities) == targetMode)
			exportType = targetMode;
		else
			throw new IllegalArgumentException(
					AnimalTranslator.translateMessage("illegalExportMode"));
	}

	/**
	 * Sets up the whole animation for export(all steps)
	 * 
	 * @throws java.lang.IllegalArgumentException
	 *           if the animation is not set yet
	 */
	public void setFullExport() throws IllegalArgumentException {
		if (animationToExport == null || exportSteps == null)
			throw new IllegalArgumentException(
					AnimalTranslator.translateMessage("noAnimSetForExport"));
		if ((exportCapabilities & EXPORT_FULL_ANIMATION) != EXPORT_FULL_ANIMATION)
			throw new IllegalArgumentException(
					AnimalTranslator.translateMessage("noFullExportSupported"));
		for (int i = 1; i < exportSteps.length; i++)
			exportSteps[i] = true;
	}

	/**
	 * Sets the target step interval to export; may be used repeatedly!
	 * 
	 * @param startStep
	 *          the first step of the interval to export.
	 * @param endStep
	 *          the last step of the interval to export.
	 * @throws java.lang.IllegalArgumentException
	 *           if the interval is invalid
	 */
	public void setTargetInterval(int startStep, int endStep)
			throws IllegalArgumentException {
		
		if (animationToExport == null || exportSteps == null)
			throw new IllegalArgumentException(
					AnimalTranslator.translateMessage("noAnimSetForExport"));
		
		if (endStep < startStep || startStep < 0 || endStep >= exportSteps.length)
			throw new IllegalArgumentException(
					AnimalTranslator.translateMessage("illegalExportInterval",
							new String[] {String.valueOf(startStep), String.valueOf(endStep),
							String.valueOf(exportSteps.length - 1) }));					
		for (int i = startStep; i <= endStep; i++)
			exportSteps[i] = true;
	}

	/**
	 * Sets the target step to export, if in EXPORT_SINGLE_STATE or
	 * EXPORT_FULL_STEP mode
	 * 
	 * @param stepNrs
	 *          the numbers of the steps to export
	 * @throws java.lang.IllegalArgumentException
	 *           if the step does not exist in the animation
	 */
	public void setTargetSteps(int[] stepNrs) throws IllegalArgumentException {
		if (animationToExport == null || stepNrs == null || stepNrs.length == 0)
			throw new IllegalArgumentException(
					AnimalTranslator.translateMessage("noAnimOrIntervalInvalid"));
		for (int i = 0; i < stepNrs.length; i++)
			if (stepNrs[i] > 0
					&& animationToExport.verifyStep(stepNrs[i]) != Link.START)
				exportSteps[stepNrs[i]] = true;
	}

	/**
	 * Sets the target step to export, if in EXPORT_SINGLE_STATE or
	 * EXPORT_FULL_STEP mode
	 * 
	 * @param stepNr
	 *          the number of the step to export
	 * @throws java.lang.IllegalArgumentException
	 *           if the step does not exist in the animation
	 */
	public void setTargetStep(int stepNr) throws IllegalArgumentException {
		if (animationToExport == null)
			throw new IllegalArgumentException(
					AnimalTranslator.translateMessage("noAnimSetForExport"));
		if (stepNr > 0 && animationToExport.verifyStep(stepNr) != Link.START) // &&
			// verifyStep!
			exportSteps[stepNr] = true;
	}

	/**
	 * Use this method to provide a short(single line) description of the exporter
	 * 
	 * @return a String describing this exporter
	 */
	public String toString() {
		return AnimalTranslator.translateMessage("notOverridden", 
				new String[] {getClass().getName()});
	}

	/**
	 * Test whether the given format is valid.
	 * 
	 * @param formatName
	 *          the target output format name
	 * @return true if the format is registered
	 */
	public static boolean validFormat(String formatName) {
		return animalConfig.validExportFormat(formatName.toLowerCase());
	}

	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getActionCommand().equalsIgnoreCase("Cancel")) {
			// close the window!
			exportModeChooser.setVisible(false);
			return;
		}
		if (exportModeChooser.exportFullAnimation())
			exporter.setFullExport();
		else
			exporter.setTargetSteps(exportModeChooser.getExportSteps());
		if (exportModeChooser.dynamicStepExport())
			exporter.setExportMode(EXPORT_DYNAMIC_STEP);
		else
			exporter.setExportMode(EXPORT_STATIC_SNAPSHOT);
		exportModeChooser.setVisible(false);
		exportModeChooser.dispose();
		long timeTaken = System.currentTimeMillis();		
		if (exporter instanceof MagnificationAdjustableExporter)
			((MagnificationAdjustableExporter) exporter)
					.setMagnification(exportModeChooser.getMagnification());
		if (exporter instanceof SpeedAdjustableExporter)
			((SpeedAdjustableExporter) exporter).setDisplaySpeed(exportModeChooser
					.getDisplaySpeed());
		exporter.exportAnimationTo(filename);
		AnimationExporter.addExportMessage(filename, exporter, timeTaken);
	}

	public static void addExportMessage(String filename,
			AnimationExporter exporter, long startTime) {
		long timeTaken = System.currentTimeMillis() - startTime;
		StringBuilder sb = new StringBuilder();
		GregorianCalendar calendar = new GregorianCalendar();
		sb.append('[').append(calendar.get(Calendar.HOUR_OF_DAY));
		sb.append(':').append(calendar.get(Calendar.MINUTE));
		sb.append(':').append(calendar.get(Calendar.SECOND));
		sb.append("]"); 
		sb.append(filename);
		String effectiveFilename = filename;
		if (!filename.endsWith(exporter.getDefaultExtension()))
		  effectiveFilename += "." + exporter.getDefaultExtension();
//		sb.append("' (");
		File f = new File(effectiveFilename);
//		sb.append(f.length()).append(" Bytes in ");
//		sb.append(timeTaken).append(" ms).");
		MessageDisplay.message("exportConfirm",
				new String[] { sb.toString(), effectiveFilename, 
				String.valueOf(f.length()), String.valueOf(timeTaken)});
	}

	public void setFilename(String fileName) {
		if (exporter != null)
			exporter.filename = fileName;
		else
			this.filename = fileName;
	}


	public static void startLocalExporting(
			 String inputFileName,
			 String inputFormat, 
			AnimalFileChooser fileChooser) {
		Animal animal = Animal.get();
		Animation targetAnimation = animal.getAnimation();
		String filename = fileChooser.openForExport(animal);
		if (filename != null) {
			String targetFormat = fileChooser.getDescription();
			AnimationExporter exporter = AnimationExporter
					.getExporterFor(targetFormat);
			if (exporter != null) {
				exporter.setFilename(filename);
				exporter.setAnimation(targetAnimation);
				exporter.chooseExportMode();
			}
		}
	}

	/**
	 * Export the current animation in a user-chosen format. Opens a file dialog
	 * for choosing the target file and format, then exports the current
	 * animation.
	 * 
	 * @return true if the export was successful, else false
	 */
	public static boolean exportAnimation(Animation currentAnim) {
		AnimalFileChooser fc = animalConfig.saveFileChooser;
		String filename = "test";

		if (fc != null) {
			filename = fc.openForExport(Animal.get());
		}

		String targetFormat = animalConfig.saveFileChooser.getFormat();

		if ((filename != null) && (targetFormat != null)) {
			return exportAnimation(currentAnim, filename, targetFormat);
		}

		return false;
	}

	/**
	 * Store the animation using the current filename in compressed ASCII format
	 */
	public static boolean saveAnimation(Animation targetAnimation) {
		String filename = animalConfig.getCurrentFilename();
		AnimationExporter exporter = AnimationExporter
				.getExporterFor(AnimalConfiguration.DEFAULT_FORMAT);

		if (exporter != null) {
			if (!(filename.endsWith(exporter.getDefaultExtension()))) {
				if (filename.lastIndexOf('.') == (filename.length() - 4)) {
					filename = filename.substring(0, filename.length() - 4);
				}

				filename += ('.' + exporter.getDefaultExtension());
			}

			exporter.setAnimation(targetAnimation);
			exporter.setExportMode(AnimationExporter.EXPORT_DYNAMIC_STEP);
			exporter.setFullExport();
			animalConfig.setCurrentFormat(AnimalConfiguration.DEFAULT_FORMAT);

			long timeTaken = System.currentTimeMillis();
			exporter.exportAnimationTo(filename);
			AnimationExporter.addExportMessage(filename, exporter, timeTaken);
			AnimationExporter.updateFilenameAndFormat(filename,
					AnimalConfiguration.DEFAULT_FORMAT);
			return true;
		}

		return false;
	}

	private static void updateFilenameAndFormat(String filename, String formatName) {
		if (AnimationExporter.validFormat(formatName)) {
			AnimationExporter exporter = AnimationExporter.getExporterFor(formatName);

			if (exporter != null) {
				animalConfig.setCurrentFilename(filename);
				animalConfig.setCurrentFormat(formatName);
			}
		}
	}

	/**
	 * Export the current animation in the chosen format under the given filename.
	 * 
	 * @return true if the export was successful, else false
	 */
	public static boolean exportAnimation(Animation currentAnimation,
			String filename, String targetFormat) {
		AnimationExporter exporter = AnimationExporter.getExporterFor(targetFormat);
		String effectiveFilename = filename;
		if (exporter != null) {
			if (!(effectiveFilename.endsWith(exporter.getDefaultExtension()))) {
			  effectiveFilename += ("." + exporter.getDefaultExtension());
			}

			exporter.setFilename(effectiveFilename);
			exporter.setAnimation(currentAnimation);
			exporter.chooseExportMode();

			AnimationExporter.updateFilenameAndFormat(effectiveFilename, targetFormat);
			return true;
		}

		return false;
	}
}
