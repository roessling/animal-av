package animal.exchange;

import java.io.InputStream;

import translator.AnimalTranslator;
import animal.main.Animal;
import animal.main.Animation;
import animalscript.core.AnimalScriptParser;

/**
 * This class provides an import filter for AnimalScript to Animal. It will
 * basically forward the import to the internal <code>
 * animalscript.core.AnimalScriptParser</code> class.
 * 
 * @author <a href="mailto:roessling@acm.org">Guido R&ouml;&szlig;ling</a>
 * @version 1.0 2000-10-04
 * @see animalscript.core.AnimalScriptParser
 */
public class AnimalScriptImporter extends AnimationImporter {

	/**
	 * Determine if file is compressed
	 */
	private boolean isCompressed = false;
	
	public StringBuilder fileContents = null;

	/**
	 * creates a new AnimalScript importer
	 */
	public AnimalScriptImporter() {
		// supportedTypes = new String[] { "animation/animalscript",
		// "animation/animalscript-compressed" };
	}

	/**
	 * Set the format name requested and adjust the import capabilities
	 * 
	 * @param format
	 *          the name of the actual format requested
	 */
	public void init(String format) {
		super.init(format);
		isCompressed = format.endsWith("-compressed");
	}

	/**
	 * Import the animation to a file of the given name. Note that you must set
	 * the animation by calling <code>setAnimation</code> <strong>before
	 * </STRON> you call this method.
	 * 
	 * Use <code>importAnimationFrom(System.out)</code> instead if you want the
	 * output to be given on the terminal.
	 * 
	 * @param filename
	 *          the name of the output file to import to.
	 * @return the animation that was imported
	 */
	public Animation importAnimationFrom(String filename) {
		AnimalScriptParser animalScriptParser = Animal.getAnimalScriptParser(true);

		animalScriptParser.setCompressed(isCompressed);
		Animation anim = animalScriptParser.programImport(filename, !(filename
				.startsWith("%")));
		fileContents = AnimalScriptParser.fileContents;
		Animal.get().setAnimalScriptCode(fileContents.toString());
		return anim;
	}

	/**
	 * Import the animation to a file of the given name. Note that you must set
	 * the animation by calling <code>setAnimation</code> <strong>before
	 * </STRON> you call this method.
	 * 
	 * Use <code>importAnimationFrom(System.out)</code> instead if you want the
	 * output to be given on the terminal.
	 * 
	 * @param in
	 *          an input stream. If not null, reads the animation from this stream
	 * @param filename
	 *          a file name that either describes the name of the already opened
	 *          input stream, or contains the animation text itself
	 * @return the animation that was imported
	 */
	public Animation importAnimationFrom(InputStream in, String filename) {
		// AnimalScriptParser animalScriptParser = new AnimalScriptParser();
	  AnimalScriptParser animalScriptParser = Animal.getAnimalScriptParser(true);
//		AnimalScriptParser animalScriptParser = Animal.getAnimalScriptParser(false);
		animalScriptParser.setCompressed(isCompressed);
		Animation anim = animalScriptParser.importAnimationFrom(in, filename, true);
		fileContents = AnimalScriptParser.fileContents;
		Animal.get().setAnimalScriptCode(fileContents.toString());
		return anim;
	}

	/**
	 * returns the default file extension
	 * 
	 * @return the default file extension, either "asc" for compressed input or
	 *         "asu" for uncompressed input
	 */
	public String getDefaultExtension() {
		return (isCompressed) ? "asc" : "asu";
	}

	/**
	 * returns the format description String
	 * 
	 * @return the format description String, in this case the result of invoking
	 *         toString()
	 * @see #toString()
	 */
	public String getFormatDescription() {
		return toString();
	}

	/**
	 * returns the MIME type describing the input format handled by this parser
	 * 
	 * @return either "animation/animalscript-compressed" for compressed input, or
	 *         "animation/animalscript" for uncompressed input
	 */
	public String getMIMEType() {
		return "animation/animalscript" + ((isCompressed) ? "-compressed" : "");
	}

	/**
	 * Use this method to provide a short(single line) description of the importer
	 * 
	 * @return a String describing this importer
	 */
	public String toString() {
		return AnimalTranslator.translateMessage("animalScriptImportDescription");
	}
}
