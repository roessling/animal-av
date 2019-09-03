/*
 * Created on 14.07.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package animal.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JToolBar;

import translator.AnimalTranslator;
import animal.main.Animal;

/**
 * @author guido
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class MainToolBar extends JToolBar {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -7100837857472507441L;

  /**
   * collect garbage now...
   * /
  public static final String COLLECT_GARBAGE_NOW = "garbageCollection";
  */

  /**
   * the constant action command for the "input scripting code" element
   */
  public final static String INPUT = "input";

  /**
   * the constant action command for the "new" element
   */
  public final static String NEW = "new";

  /**
   * the constant action command for the "open" element
   */
  public final static String OPEN = "open";
  public static final String OPENVHDL = "openvhdl";
  /**
   * the constant action command for the "print" element
   */
  public final static String PRINT = "print";

  /**
   * the constant action command for the "reload" element
   */
  public final static String RELOAD = "reload";

  /**
   * the constant action command for the "repository connect" element
   */
  public final static String REPOSITORY = "repository";

  /**
   * the constant action command for the "run animation" element
   */
  public final static String RUN_ANIMATION = "runStep";

  /**
   * the constant action command for the "save" element
   */
  public final static String SAVE = "save";

  /**
   * the constant action command for the "save as" element
   */
  public final static String SAVE_AS = "saveAs";

  /**
   * the constant action command for the "run animation" element
   */
  public final static String SHOW_ANIMATION_WINDOW = "animWin";

  /**
   * the constant action command for the "run animation" element
   */
  public final static String SHOW_DRAWING_WINDOW = "drawWin";
  /**
   * The standard controller for all elements
   */
  private MainToolBarController controller;

  public final static String CLEAR = "clearToolbar";

  public final static String UHookPlayer = "uhookPlayer";

  public final static String    ZOOM_IN               = "zoomIn";

  public static final String    ZOOM_OUT              = "zoomOut";

  public static final String PDF = "pdf";
  private int                   height                = 20;
  private int                   width                 = 20;

  /**
   * creates the main tool bar for Animal
   * 
   * @param title
   *          not used at the moment
   * @param animalInstance
   *          the concrete instance of Animal for which the toolbar is
   *          instantiated.
   */
  public MainToolBar(String title, Animal animalInstance) {
    super(title);

    // generate the controller instance
    controller = new MainToolBarController(animalInstance);

    // huild the actual tool bar
    buildToolBar();
  }

  /**
   * Creates an abstract button to be plugged into the toolbar
   * 
   * @param key
   *          the I18N key for the button
   * @param isToggle
   *          if true, use a toggleable button
   * @param listener
   *          the ActionListener for this element
   * @param command
   *          the actual "actionCommand" to be used - useful to avoid costly
   *          I18N conversions to a "flat" format
   * @return
   */
  private AbstractButton createButton(String key, boolean isToggle,
      ActionListener listener, String command) {
    // create button according to specification!
    AbstractButton helper = AnimalTranslator.getGUIBuilder().generateJButton(
        key, null, isToggle, listener, true);
    helper.setActionCommand(command);
    return helper;
  }

  /**
   * Creates an abstract button to be plugged into the toolbar
   * 
   * @param key
   *          the I18N key for the button
   * @param isToggle
   *          if true, use a toggleable button
   * @param listener
   *          the ActionListener for this element
   * @param command
   *          the actual "actionCommand" to be used - useful to avoid costly
   *          I18N conversions to a "flat" format
   * @param dim
   *          the new dimension for the buttons
   * @return
   */
  private AbstractButton createButton(String key, boolean isToggle,
      ActionListener listener, String command, Dimension dim, Font f) {
    // create button according to specification!
    AbstractButton helper = AnimalTranslator.getGUIBuilder()
        .generateJButton(key, null, isToggle, listener, true);
    if (dim.getHeight() < 28) {
      dim.setSize(28, 28);
    }
    if (dim.getHeight() > 60) {
      dim.setSize(60, 60);
    }
    helper.setPreferredSize(dim);
    helper.setFont(f);
    helper.setActionCommand(command);
    return helper;
  }

  /**
   * build the animation tool bar
   */
  public void buildToolBar() {
    // configure the JToolBar
    getAccessibleContext().setAccessibleName(
        AnimalTranslator.translateMessage("AnimalMainToolbar"));
    setFloatable(true);

    // first row: new
    add(createButton("new", false, controller, NEW));
    addSeparator();

    // second row: open
    add(createButton("open", false, controller, OPEN));
//    add(createButton("garbageCollection", false, controller,
//        COLLECT_GARBAGE_NOW));

    // second row: connect to repository
//    add(createButton("repositoryOpen", false, controller, REPOSITORY));

    // second row: reload
    add(createButton("reload", false, controller, RELOAD));
    addSeparator();

    // second row: input scripting code
    add(createButton("inputScripting", false, controller, INPUT));

    add(createButton("animWin", false, controller, SHOW_ANIMATION_WINDOW));

    add(createButton("drawWin", false, controller, SHOW_DRAWING_WINDOW));
    // add(createButton("runStep", false, controller, RUN_ANIMATION));
    addSeparator();

    // third row: save
    add(createButton("save", false, controller, SAVE));

    // third row: save as
    add(createButton("saveAs", false, controller, SAVE_AS));
    addSeparator();

    // fourth row: print
    add(createButton("print", false, controller, PRINT));
    
    // Clear Console
    addSeparator();
    add(createButton(CLEAR, false, controller, CLEAR));
    
    // (Un-)Hook Player
    addSeparator();
    add(createButton(UHookPlayer, false, controller, UHookPlayer));

    // integrate zoom in
    // Clear Console
    addSeparator();
    add(createButton("zoomIn", false, controller, ZOOM_IN));
    // integrate zoom out
    // Clear Console
    addSeparator();
    add(createButton("zoomOut", false, controller, ZOOM_OUT));
    
    addSeparator();
    add(createButton("pdf", false, controller, PDF));
  }

  /**
   * build the animation tool bar
   * 
   * @param dim
   *          the new dimension for the buttons
   */
  public void buildToolBarZoomed(boolean zoomIn) {

    Dimension prefered = this.getComponent(0).getSize();
    Font f = this.getComponent(0).getFont();


    int fSize = f.getSize();
    if (zoomIn) {
      prefered.setSize(prefered.getWidth() + 4, prefered.getHeight() + 4);
      fSize = fSize + 2;
      if (height < 40) {
        height = height + 5;
        width = width + 5;
      }
    } else {
      prefered.setSize(prefered.getWidth() - 4, prefered.getHeight() - 4);
      fSize = fSize - 2;
      if (height > 15) {
        height = height - 5;
        width = width - 5;
      }
    }

    if (fSize < 10)
      fSize = 10;
    if (fSize > 24)
      fSize = 24;

    f = new Font(f.getName(), f.getStyle(), fSize);

    this.removeAll();
    // configure the JToolBar
    getAccessibleContext().setAccessibleName(
        AnimalTranslator.translateMessage("AnimalMainToolbar"));
    setFloatable(true);

    // first row: new
    AbstractButton but = createButton("new", false, controller, NEW, prefered,
        f);

    Image img = ((ImageIcon) but.getIcon()).getImage();
    Image newimg = img.getScaledInstance(width, height,
        java.awt.Image.SCALE_SMOOTH);
    ImageIcon icon = new ImageIcon(newimg);
    but.setIcon(icon);

    add(but);
    addSeparator();

    // second row: open
   
    but = createButton("open", false, controller, OPEN, prefered, f);

    img = ((ImageIcon) but.getIcon()).getImage();
     newimg = img.getScaledInstance(width, height,
        java.awt.Image.SCALE_SMOOTH);
    icon = new ImageIcon(newimg);
    but.setIcon(icon);

    add(but);
    addSeparator();
    // add(createButton("garbageCollection", false, controller,
    // COLLECT_GARBAGE_NOW));

    // second row: connect to repository
    // add(createButton("repositoryOpen", false, controller, REPOSITORY));

    // second row: reload


    but = createButton("reload", false, controller, RELOAD, prefered, f);

    img = ((ImageIcon) but.getIcon()).getImage();
    newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
    icon = new ImageIcon(newimg);
    but.setIcon(icon);

    add(but);
    addSeparator();

    // second row: input scripting code

    but = createButton("inputScripting", false, controller, INPUT, prefered, f);

    img = ((ImageIcon) but.getIcon()).getImage();
    newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
    icon = new ImageIcon(newimg);
    but.setIcon(icon);

    add(but);
    addSeparator();

    but = createButton("animWin", false, controller, SHOW_ANIMATION_WINDOW,
        prefered, f);

    img = ((ImageIcon) but.getIcon()).getImage();
    newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
    icon = new ImageIcon(newimg);
    but.setIcon(icon);

    add(but);
    addSeparator();

    
    but = createButton("drawWin", false, controller, SHOW_DRAWING_WINDOW,
        prefered, f);

    img = ((ImageIcon) but.getIcon()).getImage();
    newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
    icon = new ImageIcon(newimg);
    but.setIcon(icon);

    add(but);
    addSeparator();
    // add(createButton("runStep", false, controller, RUN_ANIMATION));


    // third row: save

    but = createButton("save", false, controller, SAVE, prefered, f);

    img = ((ImageIcon) but.getIcon()).getImage();
    newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
    icon = new ImageIcon(newimg);
    but.setIcon(icon);

    add(but);
    addSeparator();

    // third row: save as

    but = createButton("saveAs", false, controller, SAVE_AS, prefered, f);

    img = ((ImageIcon) but.getIcon()).getImage();
    newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
    icon = new ImageIcon(newimg);
    but.setIcon(icon);

    add(but);
    addSeparator();

    // fourth row: print

    but = createButton("print", false, controller, PRINT, prefered, f);

    img = ((ImageIcon) but.getIcon()).getImage();
    newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
    icon = new ImageIcon(newimg);
    but.setIcon(icon);

    add(but);
    addSeparator();

    // Clear Console

    but = createButton(CLEAR, false, controller, CLEAR, prefered, f);

    img = ((ImageIcon) but.getIcon()).getImage();
    newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
    icon = new ImageIcon(newimg);
    but.setIcon(icon);

    add(but);
    addSeparator();

    // (Un-)Hook Player

    but = createButton(UHookPlayer, false, controller, UHookPlayer, prefered,
        f);

    img = ((ImageIcon) but.getIcon()).getImage();
    newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
    icon = new ImageIcon(newimg);
    but.setIcon(icon);

    add(but);
    addSeparator();

    // integrate zoom in
    // Clear Console

    but = createButton("zoomIn", false, controller, ZOOM_IN, prefered, f);

    img = ((ImageIcon) but.getIcon()).getImage();
    newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
    icon = new ImageIcon(newimg);
    but.setIcon(icon);

    add(but);
    addSeparator();
    // integrate zoom out
    // Clear Console

    but = createButton("zoomOut", false, controller, ZOOM_OUT, prefered, f);

    img = ((ImageIcon) but.getIcon()).getImage();
    newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
    icon = new ImageIcon(newimg);
    but.setIcon(icon);

    add(but);
    addSeparator();
  }
}