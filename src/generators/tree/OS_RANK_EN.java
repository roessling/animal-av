/*
 * OS_RANK.java
 * Florian Breitfelder, Patrick Jattke, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

package generators.tree;

import generators.framework.Generator;

import java.util.Locale;

/**
 * 
 * Generator for English language
 *
 */
public class OS_RANK_EN extends OS_RANK implements Generator{

	public OS_RANK_EN(String languageFilesPath) {
		super(languageFilesPath, Locale.US);
	}
	
    @Override
	public String getName() {
        return "Red-Black Tree: OS-RANK";
    }
    

}