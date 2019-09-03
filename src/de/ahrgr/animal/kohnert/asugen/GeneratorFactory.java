/*
 * Created on 08.02.2005
 */
package de.ahrgr.animal.kohnert.asugen;

/**
 * @author ek
 */
public interface GeneratorFactory {
	
	/**
	 * L&auml;d die Generator-Klasse mit dem angegebenen Namen und 
	 * erzeugt eine Instanz
	 * @param theClass
	 * @return the generator
	 */
	public Generator getGenerator(String theClass);

}
