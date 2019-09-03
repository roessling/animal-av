package animal.exchange;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import translator.AnimalTranslator;
import animal.main.Animal;
import animal.main.Animation;
import animalscript.core.AnimalScriptParser;

/**
 * Imports an animation from a ZIP file as specified in {@link AnimalZipFormat}.
 * 
 * @author SimonSprankel ( <a
 *         href="mailto:sprankel@rbg.informatik.tu-darmstadt.de"
 *         >sprankel@rbg.informatik.tu-darmstadt.de</a> )
 */
public class AnimalZipImporter extends AnimationImporter {

  /**
   * Imports the animation from the ZIP file specified by a filename.
   * 
   * @param in
   *          ignored in this function!
   * @param filename
   *          the filename of the ZIP file
   */
  @Override
  public Animation importAnimationFrom(InputStream in, String filename) {
    InputStream animationInputStream = null;
    try {
      ZipFile zipFile = new ZipFile(filename);
      prepareFiles(zipFile);
      animationInputStream = new FileInputStream(
          AnimalZipFormat.ANIMATION_FILENAME);
    } catch (IOException e) {
      e.printStackTrace();
    }
    AnimalScriptParser animalScriptParser = Animal.getAnimalScriptParser(true);
    Animation anim = animalScriptParser.importAnimationFrom(
        animationInputStream, null, true);
    StringBuilder fileContents = AnimalScriptParser.fileContents;
    Animal.get().setAnimalScriptCode(fileContents.toString());

    return anim;
  }

  /**
   * Reads the asu and the interaction definition file from the given ZIP file
   * respecting the included manifest file and unpacks them.
   * 
   * @param zipFile
   *          the ZIP file which includes manifest, the asu and interaction
   *          definition file
   * @throws IOException
   */
  @SuppressWarnings("resource")
  private void prepareFiles(ZipFile zipFile) throws IOException {
    // get and check the manifest file
    ZipEntry zipEntry = zipFile.getEntry(AnimalZipFormat.MANIFEST_FILENAME);
    if (zipEntry == null) {
      throw new IOException("The given zip file has to include a '"
          + AnimalZipFormat.MANIFEST_FILENAME + "' file!");
    }
    // get the filename of the animation definition file
    Scanner scanner = new Scanner(zipFile.getInputStream(zipEntry));
    String animationDefinition = "";
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine().trim();
      if (line.startsWith("animationDefinitionFile=")) {
        animationDefinition = line.substring(24);
      }
    }
    scanner.close();
    if (animationDefinition.equals("")) {
      throw new IOException(
          "The manifest must reference an animation definition file which exists in the ZIP file!");
    }

    // write the animation definition file
    int size;
    byte[] buffer = new byte[2048];
    zipEntry = zipFile.getEntry(animationDefinition);
    BufferedInputStream bis = new BufferedInputStream(
        zipFile.getInputStream(zipEntry));
    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(
        animationDefinition), buffer.length);
    while ((size = bis.read(buffer, 0, buffer.length)) != -1) {
      bos.write(buffer, 0, size);
    }
    bos.flush();

    // get the filename of the referenced interaction definition file
    scanner = new Scanner(new FileInputStream(animationDefinition));
    String interactionDefinition = "";
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine().trim();
      if (line.startsWith("interactionDefinition")) {
        int beginIndex = line.indexOf('"');
        int endIndex = line.lastIndexOf('"');
        interactionDefinition = line.substring(beginIndex + 1, endIndex);
      }
    }

    // write the interaction definition file
    zipEntry = zipFile.getEntry(interactionDefinition);
    bis = new BufferedInputStream(zipFile.getInputStream(zipEntry));
    bos = new BufferedOutputStream(new FileOutputStream(interactionDefinition),
        buffer.length);
    while ((size = bis.read(buffer, 0, buffer.length)) != -1) {
      bos.write(buffer, 0, size);
    }
    bos.flush();
    bos.close();
    bis.close();
    scanner.close();
  }
  
  public AnimalZipImporter() {
    // do nothing here
  }

  @Override
  public String getDefaultExtension() {
    return "zip";
  }

  @Override
  public String getFormatDescription() {
    return AnimalTranslator.translateMessage("animalZipImportDescription");
  }

  @Override
  public String getMIMEType() {
    return "animation/animal-zip";
  }

}
