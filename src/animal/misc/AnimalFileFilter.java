package animal.misc;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.filechooser.FileFilter;

/**
 * A convenience implementation of FileFilter that filters out
 * all files except for those type extensions that it knows about.
 *
 * Extensions are of the type ".foo", which is typically found on
 * Windows and Unix boxes, but not on Macinthosh. Case is ignored.
 *
 * Animal - create a new filter that filerts out all files
 * but gif and jpg image files:
 *
 *     JFileChooser chooser = new JFileChooser();
 *     AnimalFileFilter filter = new AnimalFileFilter(
 *                   new String{"gif", "jpg"}, "JPEG & GIF Images")
 *     chooser.addChoosableFileFilter(filter);
 *     chooser.showOpenDialog(this);
 *
 * @version 1.9 04/23/99
 * @author Jeff Dinkins
 */
public class AnimalFileFilter extends FileFilter {
  private Hashtable<String, AnimalFileFilter> filters = null;
  private String description = null;
  private String fullDescription = null;
  private String extension = null;
  private boolean useExtensionsInDescription = true;
  private boolean requiresPrefixAndDirectory = false;
  private String directory = null;
  private String prefix = null;

  /**
    * Creates a file filter. If no filters are added, then all
    * files are accepted.
    *
    * @see #addExtension(java.lang.String)
    */
  public AnimalFileFilter() {
    this.filters = new Hashtable<String, AnimalFileFilter>();
  }
  
  /**
    * Creates a file filter that accepts files with the given extension.
    * Animal: new AnimalFileFilter("jpg");
    *
    * @see #addExtension(java.lang.String)
    */
  public AnimalFileFilter(String anExtension) {
    this(anExtension, null);
  }
  
  /**
    * Creates a file filter that accepts the given file type.
    * Animal: new AnimalFileFilter("jpg", "JPEG Image Images");
    *
    * Note that the "." before the extension is not needed. If
    * provided, it will be ignored.
    *
    * @see #addExtension(java.lang.String)
    */
  public AnimalFileFilter(String anExtension, String aDescription) {
    this();
    if (anExtension != null) 
      addExtension(anExtension);
    extension = anExtension;
    if (aDescription != null) 
      setDescription(aDescription);
  }
  
  /**
    * Creates a file filter that accepts the given file type.
    * Animal: new AnimalFileFilter("jpg", "JPEG Image Images");
    *
    * Note that the "." before the extension is not needed. If
    * provided, it will be ignored.
    *
    * @see #addExtension(java.lang.String)
    */
  public AnimalFileFilter(String mandatoryDirectory, String mandatoryPrefix,
                          String aDescription)
  {
    this();
    directory = mandatoryDirectory;
    prefix = mandatoryPrefix;
    setDescription(aDescription);
    fullDescription = aDescription;
    requiresPrefixAndDirectory = true;
  }

  /**
    * Creates a file filter from the given string array.
    * Animal: new AnimalFileFilter(String {"gif", "jpg"});
    *
    * Note that the "." before the extension is not needed adn
    * will be ignored.
    *
    * @see #addExtension(java.lang.String)
    */
  public AnimalFileFilter(String[] filterSettings) {
    this(filterSettings, null);
  }
  
  /**
    * Creates a file filter from the given string array and description.
    * Animal: new AnimalFileFilter(String {"gif", "jpg"}, "Gif and JPG Images");
    *
    * Note that the "." before the extension is not needed and will be ignored.
    *
    * @see #addExtension(java.lang.String)
    */
  public AnimalFileFilter(String[] filterSettings, String aDescription) {
    this();
    for (int i = 0; i < filterSettings.length; i++) {
      // add filters one by one
      addExtension(filterSettings[i]);
    }
    if (aDescription != null)
      setDescription(aDescription);
  }
  
  /**
    * Return true if this file should be shown in the directory pane,
    * false if it shouldn't.
    *
    * Files that begin with "." are ignored.
    *
    * @see #getExtension
    * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
    */
  public boolean accept(File f) {
    if (f != null) 
    {
      if (f.isDirectory()) 
      {
        return true;
      }
      if (requiresPrefixAndDirectory)
      {
        if (prefix != null && !(f.getName().startsWith(prefix)))
          return false;
        if (directory != null && !(f.getPath().endsWith(directory)))
          return false;
        return true;
      }
      String anExtension = getExtension(f);
      if (anExtension != null && filters.get(getExtension(f)) != null) {
        return true;
      }
    }
    return false;
  }

  
  /**
    * Return the extension portion of the file's name .
    *
    * @see #getExtension
    * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
    */
  public String getExtension(File f) {
    if (f != null) {
      String filename = f.getName();
      int i = filename.lastIndexOf('.');
      if (i>0 && i<filename.length()-1) {
        return filename.substring(i+1).toLowerCase();
      }
    }
    return null;
  }

  /**
    * Return the extension portion of the file's name .
    *
    * @see #getExtension
    * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
    */
  public String getExtension() {
    return extension;
  }

  /**
    * Adds a filetype "dot" extension to filter against.
    *
    * For example: the following code will create a filter that filters
    * out all files except those that end in ".jpg" and ".tif":
    *
    *   AnimalFileFilter filter = new AnimalFileFilter();
    *   filter.addExtension("jpg");
    *   filter.addExtension("tif");
    *
    * Note that the "." before the extension is not needed and will be ignored.
    */
  public void addExtension(String anExtension) {
    if (filters == null) {
      filters = new Hashtable<String, AnimalFileFilter>(5);
    }
    filters.put(anExtension.toLowerCase(), this);
    fullDescription = null;
  }
  

  public String getShortDescription() 
  {
    return description;
  }

  /**
    * Returns the human readable description of this filter. For
    * example: "JPEG and GIF Image Files(*.jpg, *.gif)"
    *
    * @see #setDescription(java.lang.String)
    * @see #setExtensionListInDescription(boolean)
    * @see #isExtensionListInDescription
    * @see javax.swing.filechooser.FileFilter#getDescription
    */
  public String getDescription() {
    if (fullDescription == null) {
      if (description == null || isExtensionListInDescription()) {
        fullDescription = description==null ? " (" : description + "(";
        // build the description from the extension list
        Enumeration<String> extensions = filters.keys();
        if (extensions != null) {
          fullDescription += "." +extensions.nextElement();
          while (extensions.hasMoreElements()) {
            fullDescription += ", " +extensions.nextElement();
          }
        }
        fullDescription += ")";
      } else {
        fullDescription = description;
      }
    }
    return fullDescription;
  }
  
  /**
    * Sets the human readable description of this filter. For
    * example: filter.setDescription("Gif and JPG Images");
    *
    * @see #setExtensionListInDescription(boolean)
    * @see #isExtensionListInDescription
    */
  public void setDescription(String aDescription) {
    description = aDescription;
    fullDescription = null;
  }
  
  /**
    * Determines whether the extension list(.jpg, .gif, etc) should
    * show up in the human readable description.
    *
    * Only relevent if a description was provided in the constructor
    * or using setDescription();
    *
    * @see #getDescription
    * @see #setDescription(java.lang.String)
    * @see #isExtensionListInDescription
    */
  public void setExtensionListInDescription(boolean b) {
    useExtensionsInDescription = b;
    fullDescription = null;
  }
  
  /**
    * Returns whether the extension list(.jpg, .gif, etc) should
    * show up in the human readable description.
    *
    * Only relevent if a description was provided in the constructor
    * or using setDescription();
    *
    * @see #getDescription
    * @see #setDescription(java.lang.String)
    * @see #setExtensionListInDescription(boolean)
    */
  public boolean isExtensionListInDescription() {
    return useExtensionsInDescription;
  }
}
