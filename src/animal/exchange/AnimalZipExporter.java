package animal.exchange;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import translator.AnimalTranslator;
import animal.animator.InteractionElement;
import animal.main.Animal;
import animal.main.Animation;
import animal.main.AnimationListEntry;
import animal.misc.MessageDisplay;

/**
 * This class exports an animation or parts thereof as a ZIP file as specified
 * in {@link AnimalZipFormat}.
 * 
 * @author SimonSprankel ( <a
 *         href="mailto:sprankel@rbg.informatik.tu-darmstadt.de"
 *         >sprankel@rbg.informatik.tu-darmstadt.de</a> )
 */
public class AnimalZipExporter extends AnimationExporter {

  /**
   * Export the animation to a file of the given name. Note that you must set
   * the animation by calling <code>setAnimation</code> <strong>before</strong>
   * you call this method.
   * 
   * @param fileName
   *          the name of the output file to export to.
   * @return true if the operation was successful
   * @see #setAnimation(animal.main.Animation)
   */
  public boolean exportAnimationTo(String fileName) {
    if (animationToExport == null) {
      animationToExport = Animal.get().getAnimation();
    }

    ZipOutputStream out = null;
    filename = fileName;

    String fileExtension = getDefaultExtension();

    if (!filename.endsWith(fileExtension)) {
      filename += ("." + fileExtension);
    }

    try {
      out = new ZipOutputStream(new FileOutputStream(filename));
    } catch (FileNotFoundException fileNotFoundException) {
      MessageDisplay.errorMsg(fileNotFoundException.getMessage(),
          MessageDisplay.RUN_ERROR);
    }

    if (out == null) {
      return false;
    }

    // add the interaction definition file
    boolean interactionSuccess = addInteractionDefinition(out);
    // add the manifest file
    boolean manifestSuccess = addManifestFile(out,
        AnimalZipFormat.ANIMATION_FILENAME);
    // add the animation definition file, the output stream will also be closed
    boolean animationSuccess = addAnimationDefinition(out);

    // has every file been written successfully?
    return manifestSuccess && animationSuccess && interactionSuccess;
  }

  /**
   * Adds the interaction definition file to the given zip output stream.
   * 
   * @param out
   *          the zip file output stream the interaction definition file should
   *          be written to
   * @return whether the interaction definition file has been added successfully
   */
  private boolean addInteractionDefinition(ZipOutputStream out) {
    if (out == null)
      return false;
    Animation animation = Animal.get().getAnimation();
    // get all animators
    AnimationListEntry[] animationListEntries = animation.getAnimatorList();
    String interactionDefinitionFilename = "";
    // find the animator which represents the interaction definition file
    for (AnimationListEntry animationListEntry : animationListEntries) {
      if (animationListEntry.mode == AnimationListEntry.ANIMATOR
          && animationListEntry.animator instanceof InteractionElement) {
        InteractionElement interactionElement = (InteractionElement) animationListEntry.animator;
        if (interactionElement.getInteractionType() == InteractionElement.INTERACTION_DEFINITION) {
          interactionDefinitionFilename = interactionElement.getActionKey();
        }
      }
    }

    // copy the interaction definition file to the zip file
    try {
      InputStream in = new FileInputStream(interactionDefinitionFilename);
      out.putNextEntry(new ZipEntry(interactionDefinitionFilename));
      byte[] buf = new byte[2048];
      int len;
      while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
      }
      out.closeEntry();
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    // if there has not been any error until here, the process was successful
    return true;
  }

  /**
   * Adds the Animal manifest file to the given zip output stream.
   * 
   * @param out
   *          the zip file output stream the manifest file should be written to
   * @param animationDefinition
   *          the filename of the animation definition file
   * @param interactionDefinition
   *          the filename of the interaction definition file
   * @return whether the manifest file has been added successfully
   */
  private boolean addManifestFile(ZipOutputStream out,
      String animationDefinition) {
    // check parameter
    if (out == null || animationDefinition == null)
      return false;
    try {
      // create new zip file entry
      out.putNextEntry(new ZipEntry(AnimalZipFormat.MANIFEST_FILENAME));
      // create a writer in order to write the content to the file
      PrintWriter writer = new PrintWriter(out);
      writer.println("animationDefinitionFile=" + animationDefinition);
      writer.flush();
      out.closeEntry();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * Adds the animation definition file to the given zip output stream.
   * 
   * @param out
   *          the zip file output stream the animation definition file should be
   *          written to
   * @return whether the animation definition file has been added successfully
   */
  private boolean addAnimationDefinition(ZipOutputStream out) {
    if (out == null)
      return false;
    AnimalScriptExporter exporter = new AnimalScriptExporter();
    exporter.setAnimation(animationToExport);
    try {
      out.putNextEntry(new ZipEntry(AnimalZipFormat.ANIMATION_FILENAME));
      exporter.exportAnimationTo(out);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return exporter.exportAnimationTo(out);
  }

  @Override
  public String getDefaultExtension() {
    return "zip";
  }

  @Override
  public String getFormatDescription() {
    return AnimalTranslator.translateMessage("animalZipFormat");
  }

  @Override
  public String getMIMEType() {
    return "animation/animal-zip";
  }

  @Override
  public void init(String format) {
    super.init(format);
    exportCapabilities = EXPORT_STATIC_SNAPSHOT | EXPORT_DYNAMIC_STEP
        | EXPORT_FULL_ANIMATION;
  }

  @Override
  public String toString() {
    return AnimalTranslator.translateMessage("animalZipExportDescription");
  }
}
