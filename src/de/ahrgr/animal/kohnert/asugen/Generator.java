/*
 * Created on 28.01.2005
 */
package de.ahrgr.animal.kohnert.asugen;

import java.io.Writer;

import de.ahrgr.animal.kohnert.asugen.property.Property;



/**
 * @author ek
 */
public interface Generator {
    
    public String getGeneratorName();
    public Property[] getProperties();
    public void generateScript(Writer out);

}
