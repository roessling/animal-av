package translator;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import animal.misc.MessageDisplay;
import helpers.AnimalReader;

/**
 * This class encapsulates the loading of (almost) arbitrary resources. Resource
 * loading is performed by checking the following approaches:
 * 
 * <ol>
 * <li>Loading using <code>Class.getResourceAsStream()</code></li>
 * <li>Loading using <code>ClassLoader.getSystemResourceAsStream()</code></li>
 * <li>Loading using the <em>codebase</em> as a URL, if it Applet mode</li>
 * <li>Loading using a <code>FileInputStream</code> (not possible for Applets
 * and WebStart applications)</li>
 * </ol>
 * 
 * @author Guido Roessling (roessling@acm.org>
 * @version 0.7 10.06.2005
 */
public class ResourceLocator {
	private static ResourceLocator locator = null;

	/**
	 * (empty) default constructor
	 */
	public ResourceLocator() {
		// do nothing
	}

	/**
	 * returns the current (internal) locator, creating a new one first if needed
	 * 
	 * @return the current ResourceLocator; if none is present, generates one
	 *         first.
	 */
	public static ResourceLocator getResourceLocator() {
		if (locator == null)
			locator = new ResourceLocator();
		return locator;
	}

	/**
	 * Retrieve a resource by its name in String format
	 * 
	 * @param resourceName
	 *          the name of the resource to be loaded
	 * @return an <code>InputStream</code> to the resource. May be null if none
	 *         of the approaches taken can locate the resource.
	 * @see #getResourceStream(String, String, boolean, URL)
	 */
	public InputStream getResourceStream(String resourceName) {
		return getResourceStream(resourceName, null, false, null);
	}

	/**
	 * Retrieve a resource by its name and extension in String format
	 * 
	 * @param resourceName
	 *          the name of the resource to be loaded
	 * @param extension
	 *          the filename extension, important e.g. for I18N applications where
	 *          the filename encodes the locale.
	 * @return an <code>InputStream</code> to the resource. May be null if none
	 *         of the approaches taken can locate the resource.
	 * @see #getResourceStream(String, String, boolean, URL)
	 */
	public InputStream getResourceStream(String resourceName, String extension) {
		return getResourceStream(resourceName, extension, false, null);
	}

	/**
	 * Retrieve a resource by its name in String format, a boolean indicating if
	 * the current environment is an Applet, and the codebase, if so.
	 * 
	 * @param resourceName
	 *          the name of the resource to be loaded
	 * @param runsAsApplet
	 *          indicates if this is an Application (=<code>false</code>) or
	 *          an Applet (=<code>true</code>)
	 * @param codeBaseName
	 *          the URL pointing towards the Applet's codebase, if this is
	 *          actually an Applet (<code>runsAsApplet == true</code>)
	 * @return an <code>InputStream</code> to the resource. May be null if none
	 *         of the approaches taken can locate the resource.
	 * @see #getResourceStream(String, String, boolean, URL)
	 */
	public InputStream getResourceStream(String resourceName,
			boolean runsAsApplet, URL codeBaseName) {
		return getResourceStream(resourceName, null, runsAsApplet, codeBaseName);
	}

	/**
	 * Retrieve a resource by its name and extension plus directory in String
	 * format.
	 * 
	 * @param resourceName
	 *          the name of the resource to be loaded
	 * @param extension
	 *          the filename's extension (if any - else <code>null</code>)
	 * @param directoryName
	 *          the path for the file
	 * @return an <code>InputStream</code> to the resource. May be null if none
	 *         of the approaches taken can locate the resource.
	 * @see #getResourceStream(String, String, boolean, URL)
	 */
	public InputStream getResourceStream(String resourceName, String extension,
			String directoryName) {
		String filename = null;
		if (directoryName == null)
			filename = resourceName;
		else if (directoryName.endsWith(File.separator))
			filename = directoryName + resourceName;
		else
			filename = directoryName + File.separator + resourceName;
		InputStream inStream = getResourceStream(filename, extension);
		if (inStream == null && directoryName != null)
			inStream = getResourceStream(resourceName, extension);
		return inStream;
	}

	/**
	 * Retrieve a resource by its name and extension in String format, a boolean
	 * indicating if the current environment is an Applet, and the codebase, if
	 * so.
	 * 
	 * @param resourceName
	 *          the name of the resource to be loaded
	 * @param extension
	 *          the filename's extension (if any - else <code>null</code>)
	 * @param runsAsApplet
	 *          indicates if this is an Application (=<code>false</code>) or
	 *          an Applet (=<code>true</code>)
	 * @param codeBase
	 *          the URL pointing towards the Applet's codebase, if this is
	 *          actually an Applet (<code>runsAsApplet == true</code>)
	 * @return an <code>InputStream</code> to the resource. May be null if none
	 *         of the approaches taken can locate the resource.
	 */
  public InputStream getResourceStream(String resourceName, String extension,
      boolean runsAsApplet, URL codeBase) {
    StringBuilder filenameBuffer = new StringBuilder(255);
    filenameBuffer.append(resourceName);
    if (extension != null)
      filenameBuffer.append('.').append(extension);
    return AnimalReader.getInputStream(filenameBuffer.toString());
  }
//  public InputStream getResourceStream(String resourceName, String extension,
//      boolean runsAsApplet, URL codeBase) {
//    InputStream inputStream = null;
//    StringBuilder filenameBuffer = new StringBuilder(255);
//    if (runsAsApplet && codeBase != null) {
//      filenameBuffer.append(codeBase.toString());
//    }
//    filenameBuffer.append(resourceName);
//    if (extension != null)
//      filenameBuffer.append('.').append(extension);
//    // String filename = (extension == null) ? resourceName : resourceName +
//    // "." +extension;
//    String filename = filenameBuffer.toString();
//    // did not work -- check 'resource as Stream'!
//    inputStream = getClass().getResourceAsStream(filename);
//    // if (inputStream != null)
//    // System.err.println("loaded by Class: "+resourceName +" / " +inputStream);
//    // else {
//    if (inputStream == null)
//      inputStream = ClassLoader.getSystemResourceAsStream(filename);
//    // if (inputStream != null)
//    // System.err.println("loaded by ClassLoader: "+resourceName +" / "
//    // +inputStream);
//    // }
//    if (runsAsApplet && inputStream == null) {
//      try {
//        URL targetURL = new URL(filename);
//        // System.err.println("created URL: " +targetURL);
//        inputStream = targetURL.openStream();
//        // if (inputStream != null)
//        // System.err.println("loaded by URL: "+resourceName +" / "
//        // +inputStream);
//      } catch (IOException ioExceptionApplet) {
//        Debug
//            .printlnMessage("Sorry, the applet cannot open the resource file for "
//                + filename);
//      }
//    }
//    if (inputStream == null) {
//      try {
//        // First, look for file in local directory!
//        inputStream = new FileInputStream(filename);
//        // System.err.println("loaded by FileInputStream: "+resourceName +" / "
//        // +inputStream);
//      } catch (IOException ioExceptionLocal) {
//        // did not work -- check 'resource as SystemStream'!
////        if (inputStream == null)
////          Debug
////              .printlnMessage("Class, ClassLoader and local file IO cannot allocate file `"
////                  + filename + "'");
//      }
//    }
//    if (inputStream == null) {
//      try {
//        String binURL = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
//        String fileURL = binURL.substring(1, binURL.lastIndexOf("/bin/"))+"/src/"+ filename;
//        // First, look for file in local directory!
//        inputStream = new FileInputStream(fileURL);
//        // System.err.println("loaded by FileInputStream: "+resourceName +" / "
//        // +inputStream);
//      } catch(StringIndexOutOfBoundsException | IOException ioExceptionLocal) {
//        // did not work -- check 'resource as SystemStream'!
////        if (inputStream == null)
////          Debug
////              .printlnMessage("Class, ClassLoader and local file IO cannot allocate file `"
////                  + filename + "'");
//      }
//    }
//    
//    if (inputStream == null && resourceName.startsWith("/")) {
//      return getResourceStream(resourceName.substring(1), extension, runsAsApplet, codeBase);
//    }
//
//    if (inputStream == null)
//      Debug
//          .printlnMessage("Class, ClassLoader and local file IO cannot allocate file `"
//              + filename + "'");
//    
//    // if (inputStream == null)
//    // System.err.println("not loaded at all: "+resourceName +" / "
//    // +inputStream);
//    
//    return inputStream;
//  }
	


  /**
   * returns the imageIcon with the given name.
   * 
   * @return <b>null </b> if the Icon could not be found or read, <br>
   *         the Icon otherwise.
   */
  /** the path for all graphics used by Animal, i.e. icons etc. */
  private static final String GRAPHICS_PATH = "/graphics/";

  public ImageIcon getImageIcon(String name) {
    return getImageIcon(GRAPHICS_PATH,name);
  }
  
  public ImageIcon getImageIcon(String subpath, String name) {
    if (name == null || name.length() == 0)
      return null;

    ImageIcon icon = null;

//    InputStream in = AnimalDataReader.getInputStream(subpath+name);
    InputStream in = getResourceStream(subpath+name);
    
    if(in == null) {
      MessageDisplay.errorMsg("iconNotFound", name + " - 1",
          MessageDisplay.CONFIG_ERROR);
    }else {
      BufferedImage bf;
      try {
        bf = ImageIO.read(in);
        icon = new ImageIcon(bf);
      } catch (IOException e) {
        //e.printStackTrace();
        MessageDisplay.errorMsg("iconNotFound", name + " - 1",
            MessageDisplay.CONFIG_ERROR);
      }
    }
    return icon;
  }
  
  public BufferedReader getBufferedReader(String name) {
    String coding = "UTF-8";
    String nameWithSlash = name.startsWith("/") ? name : "/"+name;
    InputStream in = getResourceStream(nameWithSlash);
    try {
      return new BufferedReader(new InputStreamReader(in,coding));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }
}
