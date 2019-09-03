package animal.exchange;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;

import animal.exchange.tikz.PTGraphicObjectExporter;
import animal.graphics.PTGraphicObject;
import animal.gui.GraphicVector;
import animal.gui.GraphicVectorEntry;
import animal.main.AnimationState;
import animal.main.Link;
import animal.misc.MessageDisplay;

public class TikZExporter extends AnimationExporter {
	private Hashtable<String, PTGraphicObjectExporter> tikzExporters;

	public boolean exportAnimationTo(String fileName) {
		OutputStream oStream = null;
		try {
			oStream = new BufferedOutputStream(new FileOutputStream(fileName));
		} catch (FileNotFoundException fileNotFoundException) {
			MessageDisplay.errorMsg(fileNotFoundException.getMessage(),
					MessageDisplay.RUN_ERROR);
		}

		if (oStream == null) {
			return false;
		}
		return exportAnimationTo(oStream);
	}


  /**
   * Export the animation to the OutputStream passed. Note that you must set the
   * animation by calling <code>setAnimation</code> <strong>before </strong> you
   * call this method.
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
//	  if (animal == null) {
//		  animal = Animal.get();
//		  animal.getEditors();
//	  }

	// re-insert this once it works!
//	int i;
//
//	// re-insert this once it works!
//	StringBuilder exportErrors = new StringBuilder();
	PrintWriter writer = new PrintWriter(oStream);

	// assemble all graphical primitives
	AnimationState animState = new AnimationState(animationToExport);
	
	GraphicVector objectsAtStep = null;
	int currentStep = animState.getFirstRealStep();
	PTGraphicObjectExporter currentExporter = null;
	while (animState.getNextStep() != Link.END) {
		// bring animation to step currentStep
		animState.setStep(currentStep, true);
		if (exportSteps[currentStep]) {
			// 1. Write the protocol version line
			writer.println();
			writer.print("% animation step: ");
			writer.println(currentStep);
			writer.println("\\begin{tikzpicture}");
			
//			System.err.println("requested to export step " +currentStep);
			objectsAtStep = animState.getCurrentObjects();
			GraphicVectorEntry[] objects = objectsAtStep.convertToArray();
			for (GraphicVectorEntry currentObject : objects) {
				PTGraphicObject ptgo = currentObject.getGraphicObject();
//				System.err.println("@" + currentStep +": " +ptgo.toString());
				String className = ptgo.getClass().getName();
				currentExporter = retrieveExporterForClassName(className);
				if (currentExporter != null)
					currentExporter.exportTo(writer, ptgo);
				else
					writer.println("%" +ptgo.toString());
			}

			// end export of this step
			writer.println("\\end{tikzpicture}");		
		} //else
//			System.err.println("Skipping unneccessary step " +currentStep);
		
		// forward to next step
		currentStep = animState.getNextStep();
	}
	try {
		writer.close();
		oStream.close();
	} catch (IOException ioException) {
		System.err.println("An Error occured trying to close the stream");
	}
//	Rectangle animationBBox = animationToExport.determineVisualizationSize();
//
//	if (!animationBBox.equals(new Rectangle(0, 0, 0, 0))) {
//		writer.print(animationBBox.width + "*" + animationBBox.height);
//	}

//	animationBBox = null;
//	writer.print(MessageDisplay.LINE_FEED);
//
//	localinfo = animationToExport.getAnimatorList();
//	AnimationListEntry[] localinfo;
//
//	String subName = null;
//	String className = null;
//	localinfo = animationToExport.getAnimatorList();
//
//	Vector<PTGraphicObject> allGraphicObjects = animationToExport.getGraphicObjects();
//	AnimatorExporter.setGraphicObjects(allGraphicObjects);

//	// iterate all animators
//	if (localinfo != null) { // if (localinfo != null)
//
//		for (i = 0; i < localinfo.length; i++) { // for all in localinfo
//
//			AnimationListEntry ali = localinfo[i];
//
//			// TODO Check if this was really useless code
//// if (ali.mode == AnimationListEntry.STEP) {
//// currentStep = ali.link.getStep();
//// }
//// END GR
//
//			try { // try generating / allocating generator
//
//				if (ali.mode == AnimationListEntry.ANIMATOR) {
//					className = ali.animator.getClass().getName();
//				} else {
//					className = ali.link.getClass().getName();
//				}
//
//				// Change this so that the lookup happens in a separate
//				// method
//				// to allow for an entry point for Animal itself(Menu entry
//				// "show Code")
//				if (!animalScriptExporters.containsKey(className)) { // if
//					// exporter
//					// not
//					// registered
//
//					StringBuilder handlerName = new StringBuilder(
//					"animal.exchange.animalscript.");
//					handlerName.append(className.substring(className
//							.lastIndexOf('.') + 1));
//					handlerName.append("Exporter");
//					subName = handlerName.toString();
//
//					Class<?> c = Class.forName(subName);
//					Exporter handler = null;
//
//					if (ali.mode == AnimationListEntry.ANIMATOR) {
//						handler = (AnimatorExporter) c.newInstance();
//					} else {
//						handler = (LinkExporter) c.newInstance();
//					}
//
//					animalScriptExporters.put(className, handler);
//				}
//
//				String exportString = null;
//
//				if (ali.mode == AnimationListEntry.ANIMATOR) {
//					writer.print("  ");
//					exportString = ((AnimatorExporter) animalScriptExporters
//							.get(className)).getExportString(ali.animator);
//				} else {
//					exportString = ((LinkExporter) animalScriptExporters
//							.get(className)).getExportString(ali.link);
//				}
//
//				if (exportString != null) {
//					writer.println(exportString);
//				}
//			} catch (Exception e) { // catch exceptions
//				exportErrors.append(MessageDisplay.LINE_FEED).append(
//						AnimalTranslator.translateMessage("exportException",
//								new String[] {className, className, e.getMessage()}));
//			}
//		}
//	}
//
//	localinfo = null;
//	writer.close();
//	MessageDisplay.message("exportStatusLog", new String[] { exportErrors.toString()});
//	exportErrors = null;

	return true;
  }
  
  private PTGraphicObjectExporter retrieveExporterForClassName(String className) {
	  if (tikzExporters.containsKey(className)) {
		  System.err.println("\tretrieved exporter: " 
				  +tikzExporters.get(className).getClass().getName());
		  return tikzExporters.get(className);
	  }
	  
	  // new entry, not read yet...
		StringBuilder handlerName = new StringBuilder("animal.exchange.tikz.");
		handlerName.append(className.substring(className
				.lastIndexOf('.') + 1));
		handlerName.append("Exporter");

		System.err.println("\t\tneed to retrieve handler " +handlerName.toString());
		Class<?> c = null;
		PTGraphicObjectExporter handler = null;
		try {
			c = Class.forName(handlerName.toString());
			handler = (PTGraphicObjectExporter)c.newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (handler != null) {
			System.err.println("\t\treceived " +handler.getClass().getName());
			tikzExporters.put(className, handler);
		}
		return handler;
  }

	public String getDefaultExtension() {
		return "tex";
	}

	public String getFormatDescription() {
		return "TikZ LaTeX format";
	}
	
	public String getMIMEType() {
		return "text/tex-tikz";
	}
	
	/**
	 * Set the format name requested and adjust the export capabilities
	 * 
	 * @param format
	 *            the name of the actual format requested
	 */
	public void init(String format) {
		super.init(format);
		 tikzExporters =
				new Hashtable<String, PTGraphicObjectExporter>(73);
		exportCapabilities = EXPORT_STATIC_SNAPSHOT | EXPORT_INDIVIDUAL_STEPS
				| EXPORT_FULL_ANIMATION;
	}

}