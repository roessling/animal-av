package algoanim.primitives.generators;

import java.util.Vector;

/**
 * Implements functionality which is common for all generators of all
 * languages. In particular this applies to the interface to the associated 
 * language object.
 * 
 * @author Jens Pfau, Stephan Mehlhase, Rolly der kleine Hamster
 */
public abstract class Generator implements GeneratorInterface {
	/**
	 * the associated <code>Language</code> object.
	 */
	protected Language lang;
	
	/**
	 * the valid directions.
	 */
	private Vector<String> directions = new Vector<String>();
	
	/**
	 * Stores the related Language object.
	 * @param aLang the Language object, which is associated to. 
	 * this Generator.
	 */
	public Generator(Language aLang) {
		lang = aLang;
		createDirectionArray();
	}
	
	/**
	 * @see algoanim.primitives.generators.GeneratorInterface
	 * #getLanguage()
	 */
	public Language getLanguage() {
		return lang;
	}
	
	/**
	 * Creates the private array directions, which is used, to determine if
	 * a directions name is a valid one. Please use 
	 * isValidDirection(String direction) for this and don't access the array 
	 * directly.
	 */
	private void createDirectionArray() {
		this.directions = this.lang.validDirections();
	}
	
	/**
	 * Checks if a given name is already used.
	 * @param name the name you want to give to an element.
	 * @return true if the name is already taken by another element, false
	 *  if it is free.
	 */
	protected boolean isNameUsed(String name) {
		return lang.isNameUsed(name);
	}
	
	/**
	 * Checks if a given direction name is a valid one.
	 * Works case insensitive.
	 * @param direction the name of the direction you want to use.
	 * @return true if the direction has a valid name, false if not, please 
	 * choose another in this case.
	 */
	protected boolean isValidDirection(String direction) {
		return directions.contains(direction.toUpperCase());
	}
	
}
