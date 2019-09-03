package animal.exchange;

import java.awt.Rectangle;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.GZIPOutputStream;

import translator.AnimalTranslator;
import algoanim.animalscript.AnimalScript;
import animal.exchange.animalscript2.AnimatorExporter;
import animal.exchange.animalscript2.Exporter;
import animal.exchange.animalscript2.LinkExporter;
import animal.graphics.PTGraphicObject;
import animal.main.Animal;
import animal.main.AnimationListEntry;
import animal.misc.MessageDisplay;

/**
 * This class exports an animation or parts thereof as an AnimalScript file.
 * 
 * @author Guido R&ouml;&szlig;ling ( <A
 *         HREF="mailto:roessling@acm.org">roessling@acm.org </a>)
 * @version 1.1 2000-09-27
 */
public class AnimalScript2Exporter extends AnimationExporter {
	/**
	 * The Animal object used for retrieving the images to export
	 */
	protected Animal animal; // = Animal.get();

	/**
	 * The current file extension
	 */
	private String extension = null;

	/**
	 * Determine if file is compressed
	 */
	private boolean isCompressed = true;

	/**
	 * Export the animation to a file of the given name. Note that you must set
	 * the animation by calling <code>setAnimation</code> <strong>before
	 * </STRON> you call this method.
	 * 
	 * Use <code>exportAnimationTo(System.out)</code> instead if you want the
	 * output to be given on the terminal.
	 * 
	 * @param fileName
	 *            the name of the output file to export to.
	 * @return true if the operation was successful
	 * @see #setAnimation(animal.main.Animation)
	 */
	public boolean exportAnimationTo(String fileName) {
		OutputStream oStream = null;
		filename = fileName;

		String fileExtension = getDefaultExtension();

		if (!filename.endsWith(fileExtension)) {
			filename += ("." + fileExtension);
		}

		try {
			oStream = new BufferedOutputStream(new FileOutputStream(filename));

			if (isCompressed) {
				oStream = new GZIPOutputStream(oStream);
			}
		} catch (FileNotFoundException fileNotFoundException) {
			MessageDisplay.errorMsg(fileNotFoundException.getMessage(),
					MessageDisplay.RUN_ERROR);
		} catch (IOException ioException) {
			MessageDisplay.errorMsg(ioException.getMessage(),
					MessageDisplay.RUN_ERROR);
		}

		if (oStream == null) {
			return false;
		}

		return exportAnimationTo(oStream);
	}

	/**
	 * Export the animation to the OutputStream passed. Note that you must set
	 * the animation by calling <code>setAnimation</code> <strong>before
	 * </strong> you call this method.
	 * 
	 * Use <code>exportAnimationTo(System.out)</code> instead if you want the
	 * output to be given on the terminal.
	 * 
	 * @param oStream
	 *            the OutputStream to export to.
	 * @return true if the operation was successful
	 * @see #setAnimation(animal.main.Animation)
	 */
	public boolean exportAnimationTo(OutputStream oStream) {
		if (animal == null) {
			animal = Animal.get();
			animal.getEditors();
		}
		// re-insert this once it works!
		int i;

		// re-insert this once it works!
		Hashtable<String, Exporter> animalScriptExporters =
			new Hashtable<String, Exporter>(73);
		StringBuilder exportErrors = new StringBuilder();
	
		// prepare output writer
		PrintWriter writer = new PrintWriter(oStream);

		// determine metadata for the animation
		// animation title (if any)
		String title = animationToExport.getTitle();
		if (title == null)
			title = "<Untitled>";
		// animation author (if known)
		String animAuthor = animationToExport.getAuthor();
		if (animAuthor == null)
			animAuthor = "<Anonymous>";

		// bounding box
		Rectangle animationBBox = animationToExport.determineVisualizationSize();
		int width = animationBBox.width, height = animationBBox.height;
		
		// create generation object
		AnimalScript animalScript = new AnimalScript(title, animAuthor,
				width, height);
		animalScript.setStepMode(true);
		

/*
		// 1. Write the protocol version line
		writer.print("%Animal 2");

		Rectangle animationBBox = animationToExport
				.determineVisualizationSize();

		if (!animationBBox.equals(new Rectangle(0, 0, 0, 0))) {
			writer.print(animationBBox.width + "*" + animationBBox.height);
		}

		animationBBox = null;
		writer.print(MessageDisplay.LINE_FEED);

		AnimationListEntry[] localinfo;
		localinfo = animationToExport.getAnimatorList();
*/
		String subName = null;
		String className = null;
		AnimationListEntry[] localinfo = animationToExport.getAnimatorList();

		Vector<PTGraphicObject> allGraphicObjects = animationToExport.getGraphicObjects();
		AnimatorExporter.setGraphicObjects(allGraphicObjects);
//		boolean firstStep = true;
		// iterate all animators
		if (localinfo != null) { // if (localinfo != null)
			for (i = 0; i < localinfo.length; i++) { // for all in localinfo
				AnimationListEntry ali = localinfo[i];
				// TODO Check if this was really useless code
//				if (ali.mode == AnimationListEntry.STEP) {
//					currentStep = ali.link.getStep();
//				}
// END GR
				
				try { // try generating / allocating generator
					if (ali.mode == AnimationListEntry.ANIMATOR) {
						className = ali.animator.getClass().getName();
					} else {
						className = ali.link.getClass().getName();
					}

					// Change this so that the lookup happens in a separate
					// method
					// to allow for an entry point for Animal itself(Menu entry
					// "show Code")
					if (!animalScriptExporters.containsKey(className)) { 
						StringBuilder handlerName = new StringBuilder(
								"animal.exchange.animalscript2.");
						handlerName.append(className.substring(className
								.lastIndexOf('.') + 1));
						handlerName.append("Exporter");
						subName = handlerName.toString();

						Class<?> c = Class.forName(subName);
						Exporter handler = null;

						if (ali.mode == AnimationListEntry.ANIMATOR) {
							handler = (AnimatorExporter) c.newInstance();
						} else {
							handler = (LinkExporter) c.newInstance();
						}

						animalScriptExporters.put(className, handler);
					}

//					String exportString = null;

					if (ali.mode == AnimationListEntry.ANIMATOR) {
						((AnimatorExporter)animalScriptExporters.get(className)).export(
								animalScript, ali.animator);
//						writer.print("  ");
//						exportString = ((AnimatorExporter) animalScriptExporters
//								.get(className)).getExportString(ali.animator);
					} else {
//						if (!firstStep)
							((LinkExporter)animalScriptExporters.get(className)).export(
									animalScript, ali.link);
//						else firstStep = false;
//						exportString = ((LinkExporter) animalScriptExporters
//								.get(className)).getExportString(ali.link);
					}

//					if (exportString != null) {
//						writer.println(exportString);
//					}
				} catch (Exception e) { // catch exceptions
					exportErrors.append(MessageDisplay.LINE_FEED).append(
							AnimalTranslator.translateMessage("exportException",
									new String[] {className, className, e.getMessage()}));
				}
			}
		}

//		localinfo = null;
//		*/
		// TODO write wrapper for export here!
		String exportedContent = animalScript.toString();
		writer.print(exportedContent);
		writer.close();
		MessageDisplay.message("exportStatusLog", new String[] { exportErrors.toString()});
		exportErrors = null;

		return true;
	}

	/**
	 * Return the default extension for this output type
	 * 
	 * @return a string determining the output extension tag for this format.
	 */
	public String getDefaultExtension() {
		return (isCompressed) ? "asc" : "asu";
	}

	/**
	 * Return a short description of this output type
	 * 
	 * @return a string describing this format.
	 * @since 1.1
	 */
	public String getFormatDescription() {
		return AnimalTranslator.translateMessage((isCompressed) 
				? "animalScriptFormatGzip" : "animalScriptFormat");
	}

	/**
	 * Return the MIME type for this output type
	 * 
	 * @return the MIME type of this format.
	 * @since 1.1
	 */
	public String getMIMEType() {
		if (isCompressed) {
			return "animation/animal-ascii-compressed";
		}
		return "animation/animal-ascii";
	}

	/**
	 * Set the format name requested and adjust the export capabilities
	 * 
	 * @param format
	 *            the name of the actual format requested
	 */
	public void init(String format) {
		super.init(format);
		extension = format.substring(format.indexOf('/') + 1);
		isCompressed = extension.endsWith("compressed");
		exportCapabilities = EXPORT_STATIC_SNAPSHOT | EXPORT_DYNAMIC_STEP
				| EXPORT_FULL_ANIMATION;
	}

	/**
	 * Use this method to provide a short(single line) description of the
	 * exporter
	 * 
	 * @return a String describing this exporter
	 */
	public String toString() {
		return AnimalTranslator.translateMessage("animalScriptExportDescription");
	}
}
