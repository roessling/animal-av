/*
 * OS_SELECT_DE.java
 * Florian Breitfelder, Patrick Jattke, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

package generators.tree;

import generators.framework.Generator;

import java.util.Locale;

/**
 * 
 * Generator for German language
 *
 */
public class OS_SELECT_DE extends OS_SELECT implements Generator{

	public OS_SELECT_DE(String languageFilesPath) {
		super(languageFilesPath, Locale.GERMANY);
	}
	
    @Override
	public String getName() {
        return "Rot-Schwarz-Baum: OS-SELECT";
    }

}