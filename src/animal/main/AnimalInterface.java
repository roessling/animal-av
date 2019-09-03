package animal.main;

import java.util.Hashtable;

import javax.swing.ImageIcon;

import animal.editor.Editor;
import animal.gui.AnimationOverview;
import animal.gui.DrawWindow;
import animal.misc.XProperties;

public interface AnimalInterface
{
  /**
   * constant for <code>errorMsg</code>. The message to be displayed
   * indicates an error in the environment, which can be fixed by adapting
   * paths etc. without having to work with the sources.
   */
  public static final int CONFIG_ERROR = 3;


  /**
   * display error messages?
   */
  static final boolean DEBUG = true;


  /**
   * the package containing the editors. Required for file-/classname
   * comparisons.
   * Is accessed from Editor to determine the class name without the
   * package name. 
   */
  public static final String EDITOR_PATH = "animal.editor.";


  /**
   * the prefix for all GraphicObject classes.
   * E.g. a point is a PTPoint.
   */
  public static final String GRAPHICOBJECTS_PREFIX = "PT";


  /**
   * constant for <code>errorMsg</code>. The message to be displayed
   * indicates an information for the user or an "error", which can be
   * fixed by Animal itself, e.g. the properties file does not exist. 
   */
  public static final int INFO = 1;


  /**
   * constant for <code>errorMsg</code>. The message to be displayed
   * indicates an error in the implementation which has to be fixed
   * by editing the sources and recompiling.
   */
  public static final int PROGRAM_ERROR = 4;


  /** 
   * header of the properties written by Animal 
   */
  static final String PROPERTY_HEADER = "Animal Properties";


  /**
   * constant for ASCII import/export
   */
  public static final int PROTOCOL_VERSION = 1;


  /**
   * constant for <code>errorMsg</code>. The message to be displayed
   * indicates an error which is due to a wrong user input like
   * an inaccessible file path etc. 
   */
  public static final int RUN_ERROR = 2;


  /**
   * displays a message in the output TextArea in the center of Animal's
   * main window.
   * @param msg the message to be displayed
   * @param priority one of <code>PROGRAM_ERROR, CONFIG_ERROR, RUN_ERROR,
   * INFO</code>
   * <br>
   * <code>PROGRAM_ERRORS</code> are only displayed if the debug flag is
   * set,<br>
   * <code>CONFIG_ERRORS</code> and <code>PROGRAM_ERRORS</code> are
   * highlighted and a beep is emitted.
   */
  public void errorMsg(String msg, int priority);


  /**
   * returns a reference to the Animal object. Some objects require an
   * Animal object but don't contain a variable for this.
   */
  public AnimalInterface get();


  /**
   * return the current AnimationOverview. If none exists, create a new one.
   * @param init if true, initialize the window, otherwise just return
   * a valid(non-null) reference to an AnimationOverview, whether
   * initialized or not.
   * @return the current AnimationOverview, non-null.
   */
  public AnimationOverview getAnimationOverview(boolean init);


  /**
   * return the current AnimationWindow. If none exists, create a new one.
   * @param init if true, initialize the window, otherwise just return
   * a valid(non-null) reference to an AnimationWindow, whether
   * initialized or not.
   * @return the current AnimationWindow, non-null.
   */
  public AnimationWindow getAnimationWindow(boolean init);


  /**
   * return the current <b>DrawWindow</b>. If none exists, create a new one.
   * @param init if true, initialize the window, otherwise just return
   * a valid(non-null) reference to a DrawWindow, whether initialized
   * or not.
   * @return the current DrawWindow, non-null.
   */
  public DrawWindow getDrawWindow(boolean init);


  /**
   * get a certain Editor.
   * @param name the name of the Editor to get, not containing the package
   * name. I.E. just "Polyline", not "animal.editor.PolylineEditor". If
   * the name starts with "PT", this is ommited.
   * @return the Editor for the object. null, if none is found.
   */
  public Editor getEditor(String name);


  /**
   * get all Editors from the file. Create classes for each classname
   * found in the file <code>EDITORS_FILENAME</code>.
   * @return a <b>Hashtable</b> containing all Editors, including
   * <b>GraphicEditors</b>, <b>AnimatorEditors</b>, <b>LinkEditor</b>.
   * is accessed from ObjectPanel.
   */
  public Hashtable<String, Editor> getEditors();


  /**
   * returns the imageIcon with the given name.
   * @return <b>null</b> if the Icon could not be found or read, <br>
   * the Icon otherwise.
   */
  public ImageIcon getImageIcon(String name);


  /**
   * returns the XProperties object. Unlike all other getProperties methods
   * this method does not return any single properties.
   */
  public XProperties getProperties();


  /**
   * automatically load the file that was edited in
   * the last Animal session?
   * @return true if the last file is to be loaded, false if not.
   */
  public boolean isAutoloadLastFile();


  /**
   * displays an information message in the output TextArea in Animal's
   * main window's center. This is just a shortcut for
   * <code>errorMsg(msg,INFO)</code>.
   * @param msg the text to be displayed
   */
  public void message(String msg);
}
