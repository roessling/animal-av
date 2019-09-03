/*
 * Created on 21.01.2005 by T. Ackermann
 */
package generators.framework;

import java.io.File;
import java.util.Locale;

import javax.swing.filechooser.FileFilter;

/**
 * This filter only shows files with a given Extension.
 *
 * @author T. Ackermann
 */
public class CustomFileFilter extends FileFilter {

	/** stores the Extension for the FileFilter */
	private String myExtension;
	
		
	/**
	 * Constructor
	 * creates a new CustomFileFilter-Object.
	 * @param newExtension The new Extension (like 'xml' or 'asu').
	 */
	public CustomFileFilter(String newExtension) {
		super();
		myExtension = (newExtension != null) ? newExtension : "asu";
//		if (newExtension == null)
//			newExtension = "asu";
		myExtension = newExtension.trim();
		while (myExtension.length() > 1 && myExtension.charAt(0) == '.')
		  myExtension = myExtension.substring(1);
		
		if (myExtension.length() == 0)
		  myExtension = "asu";
		
//		this.myExtension = newExtension;
	}
	

    /**
     * getExtension
     * returns the File Extension of a given file.
     * @param f The File.
     * @return The File Extension of f.
     */
    private static String getExtension(File f) {
        if (f == null) return null;
    	
    	String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase(Locale.US);
        }
        return ext;
    }
	
    /** (non-Javadoc)
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    public boolean accept(File f) {
        if (f == null)
        	return false;
    	
    	if (f.isDirectory()) {
            return true;
        }
    	
    	String ext = getExtension(f);
    	if (ext == null)
    		return false;
    	
        if (ext.equalsIgnoreCase(this.myExtension))
        	return true;
      
        return false;
    }

    
    /** (non-Javadoc)
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    public String getDescription() {
        return "Show only " + this.myExtension + "-Files";
    }
}