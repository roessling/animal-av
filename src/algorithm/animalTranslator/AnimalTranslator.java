/*
 * Created on 10.11.2004
 *
 */
package algorithm.animalTranslator;

import algorithm.animalTranslator.library.AdvancedLibrary;
import algorithm.fileHandler.FileHandler;

/**
 * This Class implements the AnimalTranslator, which provides a set of commands,
 * that allow simplified generation of AnimalScript-code. The created code is
 * directly handed to the fileHandler in use, to be written to disk later.
 * 
 * @author Michael Maur <mmaur@web.de>
 */
public class AnimalTranslator extends AdvancedLibrary {

	/**
	 * The Constructor, that notes down a pointer to the FileHandler to be used
	 * 
	 * @param fileHandlerToUse
	 *          the FileHandler to send the AnimalScript-code to, when a command
	 *          From the AnimalTranslators library is implemented
	 */
	public AnimalTranslator(FileHandler fileHandlerToUse) {
		super(fileHandlerToUse);
	}

}
