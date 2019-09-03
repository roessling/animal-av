package animal.exchange;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.GZIPOutputStream;

import animal.animator.Animator;
import animal.exchange.animalascii.AnimatorExporter;
import animal.exchange.animalascii.Exporter;
import animal.exchange.animalascii.LinkExporter;
import animal.exchange.animalascii.PTGraphicObjectExporter;
import animal.graphics.PTGraphicObject;
import animal.main.Animal;
import animal.main.AnimationListEntry;
import animal.main.Link;
import animal.misc.MessageDisplay;

/**
 * This class exports an animation or parts thereof as an ASCII file.
 * 
 * @author Guido R&ouml;&szlig;ling ( <a
 *         href="mailto:roessling@acm.org">roessling@acm.org </a>)
 * @version 1.2 2007-09-05
 */
public class AnimalASCIIExporter extends AnimationExporter {
  /**
   * The Animal object used for retrieving the images to export
   */
  protected Animal animal;

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
   *          the name of the output file to export to.
   * @return true if the operation was successful
   * @see #setAnimation(animal.main.Animation)
   */
  public boolean exportAnimationTo(String fileName) {
    if (animal == null) {
      animal = Animal.get();
      animal.getEditors();
    }
    OutputStream oStream = null;
    filename = fileName;
    String fileExtension = getDefaultExtension();
    // filename = fileName;
    if (!filename.endsWith(fileExtension))
      filename += "." + fileExtension;
    try {
      oStream = new BufferedOutputStream(new FileOutputStream(filename));
      if (isCompressed)
        oStream = new GZIPOutputStream(oStream);
    } catch (FileNotFoundException fileNotFoundException) {
      MessageDisplay.errorMsg(fileNotFoundException.getMessage(),
          MessageDisplay.RUN_ERROR);
    } catch (IOException ioException) {
      MessageDisplay.errorMsg(ioException.getMessage(),
          MessageDisplay.RUN_ERROR);
    }
    if (oStream == null)
      return false;
    return exportAnimationTo(oStream);
  }

  /**
   * Export the animation to the OutputStream passed. Note that you must set the
   * animation by calling <code>setAnimation</code> <strong>before </strong>
   * you call this method.
   * 
   * Use <code>exportAnimationTo(System.out)</code> instead if you want the
   * output to be given on the terminal.
   * 
   * @param oStream
   *          the OutputStream to export to.
   * @return true if the operation was successful
   * @see #setAnimation(animal.main.Animation)
   */
  public boolean exportAnimationTo(OutputStream oStream) {
    int i; // , n;
    Hashtable<String, Exporter> asciiExporters = new Hashtable<String, Exporter>(
        73);
    PrintWriter writer = new PrintWriter(oStream);

    // 1. Write the protocol version line
    writer.print("# Animal protocol ");
    writer.println(animationToExport.getFileVersion());

    // 2. Write out all graphic objects that are *required* for the
    // operation!
    Vector<PTGraphicObject> allGraphicObjects = animationToExport
        .getGraphicObjects();
    int nrGOs = allGraphicObjects.size();
    for (i = 0; i < nrGOs; i++) {
    	// Check: OK?
    	PTGraphicObject ptgo = allGraphicObjects.elementAt(i);
    	if (ptgo != null && ptgo.isObjectSelectable())
    		exportGraphicObject(ptgo, asciiExporters, writer);
    }

    // 3. Write out the "steps" keyword
    writer.println("STEPS:");

    // export all animators and links
    AnimationListEntry[] localinfo = animationToExport.getAnimatorList();
    if (localinfo != null)
      for (i = 0; i < localinfo.length; i++) { // for all in localinfo
        AnimationListEntry ali = localinfo[i];
        if (ali.mode == AnimationListEntry.ANIMATOR)
          exportAnimator(ali.animator, asciiExporters, writer);
        else
          exportLink(ali.link, asciiExporters, writer);
      }
    // append the number reserved for the next graphic object
    writer.print("Next ");
    writer.println(animationToExport.getNextGraphicObjectNum());
    localinfo = null;
    writer.close();
    return true;
  }

  private void exportLink(Link link,
      Hashtable<String, Exporter> asciiExporters, PrintWriter writer) {
    String className = link.getClass().getName();
    StringBuilder handlerName = new StringBuilder(
        "animal.exchange.animalascii.");
    handlerName.append(className.substring(className.lastIndexOf('.') + 1));
    handlerName.append("Exporter");
    String subName = handlerName.toString();
    try { // try generating / allocating generator
      // if exporter is not registered
      if (!asciiExporters.containsKey(subName)) {
        @SuppressWarnings("unchecked")
        Class<LinkExporter> c = (Class<LinkExporter>) Class.forName(subName);
        LinkExporter handler = c.newInstance();
        asciiExporters.put(subName, handler);
      } // if (not registered exporter)
      ((LinkExporter) asciiExporters.get(subName)).exportTo(writer, link);
    } catch (Exception e) { // catch exceptions
      MessageDisplay.errorMsg(AnimalASCIIImporter.translateMessage(
          "exportException",
          new String[] { subName, className, e.getMessage() }),
          MessageDisplay.RUN_ERROR);
    }
  }

  private void exportAnimator(Animator animator,
      Hashtable<String, Exporter> asciiExporters, PrintWriter writer) {
    String className = animator.getClass().getName();
    StringBuilder handlerName = new StringBuilder(
        "animal.exchange.animalascii.");
    handlerName.append(className.substring(className.lastIndexOf('.') + 1));
    handlerName.append("Exporter");
    String subName = handlerName.toString();
    try { // try generating / allocating generator
      // if exporter is not registered yet
      if (!asciiExporters.containsKey(subName)) {
        @SuppressWarnings("unchecked")
        Class<AnimatorExporter> c = (Class<AnimatorExporter>) Class
            .forName(subName);
        AnimatorExporter handler = c.newInstance();
        asciiExporters.put(subName, handler);
      } // if (not registered exporter)
      ((AnimatorExporter) asciiExporters.get(subName)).exportTo(writer,
          animator);
    } // try generating / retrieving the generator
    catch (Exception e) { // catch exceptions
      MessageDisplay.errorMsg(AnimalASCIIImporter.translateMessage(
          "exportException",
          new String[] { subName, className, e.getMessage() }),
          MessageDisplay.RUN_ERROR);
    }
  }

  private void exportGraphicObject(PTGraphicObject ptgo,
      Hashtable<String, Exporter> asciiExporters, PrintWriter writer) {
    String className = ptgo.getClass().getName();
    String subName = null;
    try { // try adding exporter
      if (!asciiExporters.containsKey(className)) {
        StringBuilder handlerName = new StringBuilder(
            "animal.exchange.animalascii.");
        handlerName.append(className.substring(className.lastIndexOf('.') + 1));
        handlerName.append("Exporter");
        subName = handlerName.toString();
        @SuppressWarnings("unchecked")
        Class<PTGraphicObjectExporter> c = (Class<PTGraphicObjectExporter>) Class
            .forName(subName);
        PTGraphicObjectExporter handler = c.newInstance();
        asciiExporters.put(className, handler);
      } // if no handler registered yet
      PTGraphicObjectExporter localHandler = (PTGraphicObjectExporter) asciiExporters
          .get(className);
      localHandler.exportTo(writer, ptgo);
    } // try
    catch (Exception e) {
      MessageDisplay.errorMsg(AnimalASCIIImporter.translateMessage(
          "exportException",
          new String[] { subName, className, e.getMessage() }),
          MessageDisplay.RUN_ERROR);
    } // try...catch
  }

  /**
   * Return the default extension for this output type
   * 
   * @return a string determining the output extension tag for this format.
   */
  public String getDefaultExtension() {
    return (isCompressed) ? "aml" : "ama";
  }

  /**
   * Return a short description of this output type
   * 
   * @return a string describing this format.
   * @since 1.1
   */
  public String getFormatDescription() {
    return (isCompressed) ? AnimalASCIIImporter
        .translateMessage("asciiFormatGzip") : AnimalASCIIImporter
        .translateMessage("asciiFormat");
  }

  /**
   * Return the MIME type for this output type
   * 
   * @return the MIME type of this format.
   * @since 1.1
   */
  public String getMIMEType() {
    // is this correct..?
    if (isCompressed)
      return "animation/animal-ascii-compressed";

    return "animation/animal-ascii";
  }

  /**
   * Set the format name requested and adjust the export capabilities
   * 
   * @param format
   *          the name of the actual format requested
   */
  public void init(String format) {
    super.init(format);
    extension = format.substring(format.indexOf('/') + 1);
    isCompressed = extension.endsWith("compressed");
    exportCapabilities = EXPORT_STATIC_SNAPSHOT | EXPORT_DYNAMIC_STEP
        | EXPORT_FULL_ANIMATION;
  }

  /**
   * Use this method to provide a short(single line) description of the exporter
   * 
   * @return a String describing this exporter
   */
  public String toString() {
    return AnimalASCIIImporter.translateMessage("asciiExportDescription");
  }
}