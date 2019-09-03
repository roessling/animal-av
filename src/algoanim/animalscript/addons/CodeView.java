package algoanim.animalscript.addons;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import translator.ResourceLocator;
import algoanim.animalscript.addons.bbcode.Code;
import algoanim.animalscript.addons.bbcode.Style;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

public class CodeView {
	
	/**
	 * Creates an Animal SourceCode primitive from a plain text file. 
	 * 
	 * @param l The Language object to add the primitive to
	 * @param file The file containing the source code
	 * @param name The name of the created primitive
	 * @param base The alignment
	 * @param display Display options
	 * @param style The Style used to display the source code
	 * @return The SourceCode primitive
	 */
	public static SourceCode primitiveFromFile(Language l, String file, String name, 
	    Node base, DisplayOptions display, Style style) {
    // use the source code from the file
	  return CodeView.primitiveFromString(l, CodeView.stringFromFile(file), 
	      name, base, display, style);
	}
	
	/*
	 private SourceCode primitiveFromFile2(Language l, String file, String name, 
	      Node base, DisplayOptions display, Style style) {
	    // get the source code from the file
   // create new primitive
	    SourceCode c = l.newSourceCode(base, name, display, 
	        (SourceCodeProperties)style.getProperties(Code.BB_CODE));
	    
	    // get the source code from the file
	    try {
	      InputStream f = ResourceLocator.getResourceLocator().getResourceStream(file);
	      BufferedReader br = new BufferedReader(new InputStreamReader(f));
	      String thisLine = null;
	      
	      // regular expression to capture all tabs at the beginning of a line
	      Pattern pattern = Pattern.compile("(\\t*)[^\\t]+", Pattern.CASE_INSENSITIVE);

	      while ((thisLine = br.readLine()) != null) {
	        // get the indentation level of the current line
	        int indentation = 0;
	        Matcher m = pattern.matcher(thisLine);
	        if (m.matches()) {
	          indentation = m.group(1).length();
	        }

	        // add each line as a new element to the primitive
	        c.addCodeLine(thisLine.trim(), UUID.randomUUID().toString(), indentation, null);
	      }
	    } catch (IOException e) {
	      System.err.println("CodeView.primitiveFromFile(): Could nor read resource or file " + file);
	    }
	    
	    return c;
	  }
	  */

	 /**
   * Creates an Animal SourceCode primitive from a plain text. 
   * 
   * @param l The Language object to add the primitive to
   * @param input The String containing the source code
   * @param name The name of the created primitive
   * @param base The alignment
   * @param display Display options
   * @param style The Style used to display the source code
   * @return The SourceCode primitive
   */
  public static SourceCode primitiveFromString(Language l, String input, String name, Node base, 
      DisplayOptions display, Style style) {
    // create new primitive
    SourceCode c = l.newSourceCode(base, name, display, 
        (SourceCodeProperties)style.getProperties(Code.BB_CODE));
    
    // get the source code from the file
    StringTokenizer stok = new StringTokenizer(input, "\n");
      
    // regular expression to capture all tabs at the beginning of a line
    Pattern pattern = Pattern.compile("(\\t*)[^\\t]+", Pattern.CASE_INSENSITIVE);

    while (stok.hasMoreTokens()) {
      String thisLine = stok.nextToken();
      // get the indentation level of the current line
      int indentation = 0;
      Matcher m = pattern.matcher(thisLine);
      if (m.matches()) {
        indentation = m.group(1).length();
      }

      // add each line as a new element to the primitive
      c.addCodeLine(thisLine.trim(), UUID.randomUUID().toString(), indentation, null);
    }
    
    return c;
  }

	
	/**
	 * Get a string to display in the Code Example part of the animation's description.
	 *
	 * This method escapes basic HTML chars which are not displayed otherwise 
	 * but leaves any real HTML tags as they are.
	 * Can be used directly in generators.framework.Generator.getCodeExample()
	 * 
	 * @param file The source code file's location
	 * @return The escaped content of the file 
	 */
	public static String exampleFromFile(String file) {
		String s = stringFromFile(file);
		
		// replace ampersands
		s = s.replace("&", "&amp;");
		
		// replace brackets
		s = s.replace("<", "&lt;");
		s = s.replace(">", "&gt;");
		
		return s;
	}
	
	 /**
   * Get a string to display in the Code Example part of the animation's description.
   *
   * This method escapes basic HTML chars which are not displayed otherwise 
   * but leaves any real HTML tags as they are.
   * Can be used directly in generators.framework.Generator.getCodeExample()
   * 
   * @param theString String to be used
   * @return The escaped content of the file 
   */
  public static String exampleFromString(String theString) {
    String s = theString;
    
    // replace ampersands
    s = s.replace("&", "&amp;");
    
    // replace brackets
    s = s.replace("<", "&lt;");
    s = s.replace(">", "&gt;");
    
    return s;
  }

	/**
	 * Get a plain string from the content of a file.
	 * 
	 * @param file The file's location
	 * @return The file's content
	 */
	public static String stringFromFile(String file) {
		String s = new String();
		
		// get the source code from the file
		try {
			InputStream f = ResourceLocator.getResourceLocator().getResourceStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(f));
			String thisLine = null;

			while ((thisLine = br.readLine()) != null) {
				s = s.concat(thisLine).concat("\n");
			}			
		} catch (Exception e) {
			System.err.println("CodeView.stringFromFile(): Could not read resource or file '" + file + "'");
		}
		
		return s;
	}
}
