package animal.exchange;

import translator.AnimalTranslator;
import animal.gui.AnimalMainWindow;
import animal.main.Animal;
import animal.main.AnimalConfiguration;
import animal.main.Animation;
import animal.misc.MessageDisplay;

/**
 * Helpful class that can convert animations from one supported format into another
 *
 * @author Guido Roessling (roessling@acm.org>
 * @version 1.0, 29.07.2004
 */
public class AnimationConverter {
	private AnimalConfiguration animalConfig;
	private Animal animalInstance;
	
  /**
   * creates a new instance of this class, but does nothing else
   */
  public AnimationConverter() {
  	// ensure that Animal is initalized!
  	if (animalInstance == null) {
  		animalInstance = Animal.get();
  		animalInstance.setAutoloadLastFile(false);
  	}
  	
  	// initialize the Animal configuration
  	if (animalConfig == null) {
  		animalConfig = new AnimalConfiguration();
  	}
  	
  	// initialize the main window
    new AnimalMainWindow(animalInstance,
        animalConfig.getProperties(),
        false, true);
		animalConfig.initializeImportFormats();
		animalConfig.initializeExportFormats();
    animalConfig.initializeAllEditors();
  }

  /**
   * converts the given input and output format plus the file name 
   * of the input animation. Forwards the request to the
   * more verbose method.
   * @see #convert(String,String,String,String)
   *
   * @param inputFormat the MIME type of the input file
   * @param outputFormat the MIME type of the output file
   * @param filename the file name of the input file
   * 
   * @return true if the export was successful, else false
   */
  public boolean convert(String inputFormat, String outputFormat,
    String filename) {
    return convert(inputFormat, outputFormat, filename, filename);
  }

  /**
   * converts the given input and output format using the input file name 
   * to the given output file. 
   *
   * @param inputFormat the MIME type of the input file
   * @param outputFormat the MIME type of the output file
   * @param inputFilename the file name of the input file
   * @param outputFilename the filename to be used for the output file
   * 
   * @return true if the export was successful, else false
   */
  public boolean convert(String inputFormat, String outputFormat,
  		String inputFilename, String outputFilename) {
    long animationTimer = System.currentTimeMillis();
    AnimationImporter importer = null;
    AnimationExporter exporter = null;
    int lastDotPosition = inputFilename.lastIndexOf('.');
    String targetFileName = outputFilename;
    importer = AnimationImporter.getImporterFor(inputFormat);
    String effectiveInputFilename = inputFilename;
    String effectiveOutputFilename = outputFilename;
    if (importer != null) {
      exporter = AnimationExporter.getExporterFor(outputFormat);

      if (exporter != null) {
        if (lastDotPosition == -1) {
          effectiveInputFilename += ("." + importer.getDefaultExtension());
        }

        Animation targetAnimation = importer.importAnimationFrom(effectiveInputFilename);

        if (targetAnimation != null) {
          lastDotPosition = effectiveOutputFilename.lastIndexOf('.');

          if (lastDotPosition != -1) {
            effectiveOutputFilename = effectiveOutputFilename.substring(0, lastDotPosition);
          }

          targetFileName = effectiveOutputFilename + "." +
            exporter.getDefaultExtension();
          exporter.setFilename(effectiveOutputFilename);
          MessageDisplay.message("writingToFile ", new String[] {targetFileName});
          exporter.setAnimation(targetAnimation);
          exporter.chooseExportMode();
        }
        else
        	return false;
      }
      else
      	return false;
    }
    else
    	return false;

    MessageDisplay.message("timeTaken", new String[] {String.valueOf(
      (System.currentTimeMillis() - animationTimer))});
    return true;
  }

  /**
   * enables the program to be run from the shell
   *
   * @param args the vector of command line arguments in the form
   * <em>inputFormat outputFormat filename [outputFilename]</em>
   */
  public static void main(String[] args) {
    AnimationConverter converter = new AnimationConverter();
    if (args.length < 3) {
    	MessageDisplay.message(AnimalTranslator.translateMessage("animConvUsage"));
    }
    else if (args.length == 3) {
      converter.convert(args[0], args[1], args[2]);
    }
    else {
      converter.convert(args[0], args[1], args[2], args[3]);
    }
  }
}
