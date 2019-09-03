/*
 * The main menu bar for the central Animal window
 *
 * @author Guido Roessling (roessling@acm.org>
 * @version 0.91
 */
package animal.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import translator.AnimalTranslator;
import animal.dialog.AboutDialog;
import animal.dialog.AnimInfoDialog;
import animal.dialog.OptionDialog;
import animal.main.Animal;
import animal.misc.XProperties;
import graphics.AnimalImageDummy;
import helpers.AnimalReader;

import java.awt.Component;

/**
 * @author guido
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class MainMenuBar extends JMenuBar {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 1659837246295610623L;

  /**
   * the action command for the "about" menu item
   */
  public static final String ABOUT = "about";

  /**
   * the action command for the "Show AnimalScript Information" menu item
   */
  public static final String ANIMALSCRIPT_DEFINITION = "animalscript";

  /**
   * the action command for the "animation information" menu item
   */
  public static final String ANIMATION_INFORMATION = "animInfo";

  /**
   * the action command for the "animation information" menu item
   */
  public static final String ANIMATION_WINDOW_SIZE = "animWindowSize";

  /**
   * the action command for the "bounding box" menu item
   */
  public static final String BOUNDING_BOX = "boundingBox";

  /**
   * collect garbage now...
   */
  public static final String COLLECT_GARBAGE_NOW = "garbageCollection";

  /**
   * the action command for the "component configuration" menu item
   */
  public static final String COMPONENT_CONFIGURATION = "compConf";

  /**
   * the action command for the "demo animation" menu item
   */
  public static final String DEMO_ANIMATION = "demoAnimation";

  /**
   * the action command for accessing the demos shipped with Animal
   */
  public static final String EXAMPLES = "exampleFiles";
  
  /**
   * the action command for the "export" menu item
   */
  public static final String EXPORT = "export";

  /**
   * the action command for the "export" menu item
   */
  public static final String GENERATOR = "generator";

  /**
   * the action command for the "import" menu item
   */
  public static final String IMPORT = "import";

  /**
   * the action command for the "input scripting" menu item
   */
  public static final String INPUT_SCRIPTING = "inputScripting";

  /**
   * the action command for the "new" menu item
   */
  public static final String NEW = "new";
  public static final String EMPTY = "empty";
  public static final String NEW_OPTIONS = "new-options";

  public final static String CLEAR = "clearToolbar";

  /**
   * the action command for the "open" menu item
   */
  public static final String OPEN = "open";
  /**********************************************************************************/
  /**********************************************************************************/
  /*********write by Lu,Zheng only for VHDL pluging**********************************/
  /**********************************************************************************/
  /**
   * the action commamd for the "open VHDL" menu item
   */
  public static final String OPENVHDL = "openvhdl";
  /**
   * the action command for the "open repository " menu item
   */
  public static final String OPEN_REPOSITORY = "repositoryOpen";
  /**
   * the action command for the "scheme" menu item
   */ 
  public static final String HTDP_TL = "scheme";
  /**
   * the action command for the "preferences" menu item
   */
  public static final String PREFERENCES = "preferences";

  /**
   * the action command for the "print" menu item
   */
  public static final String PRINT = "print";

  /**
   * the action command for the "quit" menu item
   */
  public static final String QUIT = "quit";

  /**
   * the action command for the "show quiz results" menu item
   */
  public static final String QUIZ_RESULTS = "quizResults";

  /**
   * the action command for the "reload" menu item
   */
  public static final String RELOAD = "reload";

  /**
   * the action command for the "save" menu item
   */
  public static final String SAVE = "save";

  /**
   * the action command for the "save as" menu item
   */
  public static final String SAVE_AS = "saveAs";

  /**
   * the action command for the "three button mouse mode" menu item
   */
  public static final String THREE_BUTTON_MOUSE = "mouseLMR";

  /**
   * the action command for the "animation overview" toggle item
   */
  public static final String TOGGLE_ANIMATION_OVERVIEW = "animOverview";

  /**
   * the action command for the "animation window" toggle item
   */
  public static final String TOGGLE_ANIMATION_WINDOW = "animWin";

  /**
   * the action command for the "annotation window" toggle item
   */
  public static final String TOGGLE_ANNOTATION_WINDOW = "annotationWin";

  /**
   * the action command for the "drawing window" toggle item
   */
  public static final String TOGGLE_DRAWING_WINDOW = "drawWin";

  /**
   * the action command for the "objects window" toggle item
   */
  public static final String TOGGLE_OBJECTS_WINDOW = "objectsWin";

  /**
   * the action command for the "time line window" toggle item
   */
  public static final String TOGGLE_TIME_LINE_WINDOW = "timeLineWin";

  /**
   * the action command for the "tutorial" menu item
   */
  public static final String TUTORIAL = "tutorial";

  /**
   * the action command for the "two button mouse mode" item
   */
  public static final String TWO_BUTTON_MOUSE = "mouseLRM";

  /**
   * the action command for the "variable view" item
   */
  public static final String TOGGLE_VARIABLE_VIEW = "GUIResources.variableView";

  

  /**
   * the Animal instance window used for this menu bar
   */
  private Animal animalInstance;

  /**
   * the Animal main window used for this menu bar
   */
  private AnimalMainWindow animalMainWindow;

  /**
   * the animation overview menu item
   */
  private JMenuItem animationOverviewItem;

  /**
   * the animation window menu item
   */
  private JMenuItem animationWindowItem;

  /**
   * the annotation window menu item
   */
  
//  private JMenuItem annotationWindowItem;

  /**
   * the drawing window menu item
   */
  private JMenuItem drawWindowItem;

  /**
   * The menu for "Edit" operations
   */
  protected JMenu editMenu;

  /**
   * The menu for opening files etc.
   */
  protected JMenu fileMenu;

  /**
   * The menu for help setting
   */
  protected JMenu helpMenu;

  /**
   * The menu for language choice
   */
  protected JMenu languageMenu;

  /**
   * determine if a two or a three-button-mouse is used
   */
  private boolean mouseTypeIsThreeButtonMouse = true;

  /**
   * the obejcts window menu item
   */
  private JMenuItem objectsWindowItem;

  /**
   * The menu for option setting
   */
  protected JMenu optionMenu;

  /**
   * the "three button mouse mode" menu item
   */
  private JMenuItem threeButtonMouseItem;

  /**
   * the animation overview menu item
   */
  private JMenuItem timeLineWindowItem;

  /**
   * the "two button mouse mode" menu item
   */
  private JMenuItem twoButtonMouseItem;

  /**
   * the "variable view" menu item
   */
  private JMenuItem variableViewItem;

  private JMenu exerciseMenu;

  private JMenuItem quizResults;
  
  private JMenu              subMenu;

  private JMenuItem          empty;
  private JMenuItem          examples;
  private JMenuItem          generator;
  private EditMenuController editMenuController;
  private HelpMenuController helpMenuController;


  /**
   * Creates the main menu bar
   * 
   * @param title
   *          the title of the main menu bar
   * @param mainWindow
   *          the animal main window instance
   * @param animalObj
   *          the current instance of Animal
   */
  public MainMenuBar(String title, AnimalMainWindow mainWindow, Animal animalObj) {
    super();
    // store the reference to the AnimalMainWindow
    // for which this menu bar serves!
    animalMainWindow = mainWindow;

    // ensure that the Animal instance actually exists!
    if (animalObj == null) {
      animalInstance = Animal.get();
    } else {
      animalInstance = animalObj;
    }

    // build the actual menu bar
    buildMenuBar();
  }

  /**
   * generate the actual menus. This method forwards calls to the others.
   */
  public void buildMenuBar() {
    // configure the JToolBar
    getAccessibleContext().setAccessibleName(
        AnimalTranslator.translateMessage("mainMenuBar"));

    // build the file menu
    buildFileMenu();
    
    // build the exercise menu
    buildExerciseMenu();
//    exerciseMenu = AnimalTranslator.getGUIBuilder().generateJMenu("exercises", null);
//    new ExerciseMenuBuilder(exerciseMenu).buildStudentMenu(animalMainWindow);
//    add(exerciseMenu);

    // build the windows menu
    buildWindowsMenu();

    // build the option menu
    buildOptionMenu();

    // build the language menu
    buildLanguageMenu();

    // build the help menu
    buildHelpMenu();
  }
  
  private void buildExerciseMenu() {
    exerciseMenu = AnimalTranslator.getGUIBuilder().generateJMenu("exercises", null);
    new ExerciseMenuBuilder(exerciseMenu).buildStudentMenu(animalMainWindow);
    add(exerciseMenu);
  }

  /**
   * Generates the file menu with all appropriate entries
   */
  private void buildFileMenu() {
    // generate the file menu
    fileMenu = AnimalTranslator.getGUIBuilder().generateJMenu("file", null);

    // declare the local handler for events from this menu
    FileMenuController fileMenuController = new FileMenuController(
        animalInstance);

    // first group: the menu item group "new"
    // add the "new" entry
//    fileMenu.add(createMenuItem(NEW, fileMenuController));
    subMenu = AnimalTranslator.getGUIBuilder().generateJMenu("new-options");

     empty = createMenuItem(EMPTY, fileMenuController);
    examples = createMenuItem(EXAMPLES, fileMenuController);
    generator = createMenuItem(GENERATOR, fileMenuController);
    // subMenu.add(createMenuItem(EMPTY, fileMenuController));
    // subMenu.add(createMenuItem(EXAMPLES, fileMenuController));
    // subMenu.add(createMenuItem(GENERATOR, fileMenuController));
    subMenu.add(empty);
    subMenu.add(examples);
    subMenu.add(generator);

//    subMenu.add(createMenuItem(OPEN_REPOSITORY, fileMenuController));
    
    fileMenu.add(subMenu);
    
    fileMenu.add(createMenuItem(COLLECT_GARBAGE_NOW, fileMenuController));

    // end the current menu item group
    fileMenu.addSeparator();

    // second group: the menu item group "open"
    // add the "open" entry
    fileMenu.add(createMenuItem(OPEN, fileMenuController));
    fileMenu.add(createMenuItem(OPENVHDL, fileMenuController));

    // add the "reload" entry
    fileMenu.add(createMenuItem(RELOAD, fileMenuController));

    // end the current menu item group
    fileMenu.addSeparator();

    // fouth group: "demo", generator and the repository
    fileMenu.add(createMenuItem(GENERATOR, fileMenuController));

    // add the "open the repository" entry
    fileMenu.add(createMenuItem(EXAMPLES, fileMenuController));
    
//    fileMenu.add(createMenuItem(OPEN_REPOSITORY, fileMenuController));
    
    fileMenu.add(createMenuItem(HTDP_TL, fileMenuController));

//    fileMenu.add(createMenuItem(DEMO_ANIMATION, fileMenuController)); // TODO Maybe add new Demo Animation

    // end the current menu item group
    fileMenu.addSeparator();

    // sixth group: the menu item group "saving"
    // add the "save" entry
    fileMenu.add(createMenuItem(SAVE, fileMenuController));

    // add the "save as" entry
    fileMenu.add(createMenuItem(SAVE_AS, fileMenuController));

    // end the current menu item group
    fileMenu.addSeparator();

    // fifth group: the menu item group "print"
    // add the "print" entry
    fileMenu.add(createMenuItem(PRINT, fileMenuController));
    
    // Clear Console
    fileMenu.addSeparator();
    fileMenu.add(createMenuItem(CLEAR, fileMenuController));

    // end the current menu item group
    fileMenu.addSeparator();

    // // sixth group: the menu item group "import and export"
    // // add the "import" entry
    // fileMenu.add(createMenuItem(IMPORT, fileMenuController));
    //
    // // add the "export" entry
    // fileMenu.add(createMenuItem(EXPORT, fileMenuController));
    //
    // // end the current menu item group
    // fileMenu.addSeparator();

    // last group: the menu item group "quit"
    // add the "quit" entry
    fileMenu.add(createMenuItem(QUIT, fileMenuController));

    // finally, add the file menu to the tool bar!
    add(fileMenu);
  }

  /**
   * Generates the file menu with all appropriate entries
   */
  private void buildWindowsMenu() {
    // generate the menu
    editMenu = AnimalTranslator.getGUIBuilder().generateJMenu("windows", null);

    // instantiate the generic window controller
    WindowCoordinator coordinator = AnimalMainWindow.WINDOW_COORDINATOR;

    // instantiate the local handler for events from this menu
    editMenuController = new EditMenuController(coordinator);

    // animalMainWindow);
    // add a menu item for showing or hiding the drawing window
    drawWindowItem = createMenuItem(TOGGLE_DRAWING_WINDOW, editMenuController);
    if (Animal.PREVENT_EDITING)
      drawWindowItem.setEnabled(false);

    // add the item to the menu
    editMenu.add(drawWindowItem);

    // add a menu item for showing or hiding the animation overview window
    animationOverviewItem = createMenuItem(TOGGLE_ANIMATION_OVERVIEW,
        editMenuController);
    if (Animal.PREVENT_EDITING)
      animationOverviewItem.setEnabled(false);

    // add the item to the menu
    editMenu.add(animationOverviewItem);

    // add a menu item for showing or hiding the animation window
    animationWindowItem = createMenuItem(TOGGLE_ANIMATION_WINDOW,
        editMenuController);

    // add the item to the menu
    editMenu.add(animationWindowItem);

    // add the "input scripting on the fly" entry
    editMenu.add(createMenuItem(INPUT_SCRIPTING, editMenuController));
    if (Animal.PREVENT_EDITING)
      editMenu.setEnabled(false);

    // add a menu item for showing or hiding the time line window
    timeLineWindowItem = createMenuItem(TOGGLE_TIME_LINE_WINDOW,
        editMenuController);

    // add the item to the menu
    editMenu.add(timeLineWindowItem);

    // add a menu item for showing or hiding the object window
    objectsWindowItem = createMenuItem(TOGGLE_OBJECTS_WINDOW,
        editMenuController);
    if (Animal.PREVENT_EDITING)
      objectsWindowItem.setEnabled(false);

    // add the item to the menu
    editMenu.add(objectsWindowItem);

    variableViewItem = createMenuItem(TOGGLE_VARIABLE_VIEW, editMenuController);
    // add the item to the menu
    editMenu.add(variableViewItem);

    // // add a menu item for showing or hiding the annoations window
    // annotationWindowItem = createMenuItem(TOGGLE_ANNOTATION_WINDOW,
    // editMenuController);

    // add the item to the menu
    // editMenu.add(annotationWindowItem);

    // add the edit menu to the menu bar
    add(editMenu);
  }

  /**
   * Generates the options menu with all appropriate entries
   */
  private void buildOptionMenu() {
    // generate the options menu
    optionMenu = AnimalTranslator.getGUIBuilder().generateJMenu("optionsMenu",
        null);

    // generate the concrete controller for this menu
    OptionMenuController optionMenuController = new OptionMenuController(
        animalInstance);

    // add the "preferences" menu item
    optionMenu.add(createMenuItem(PREFERENCES, optionMenuController));

    // add a toggle button for the "3 mouse button" mode
    threeButtonMouseItem = createToggleMenuItem(THREE_BUTTON_MOUSE,
        threeButtonMouseMode(), optionMenuController);

    // add the button to the menu
    optionMenu.add(threeButtonMouseItem);

    // add a toggle button for the "2 mouse button" mode
    twoButtonMouseItem = createToggleMenuItem(TWO_BUTTON_MOUSE,
        !threeButtonMouseMode(), optionMenuController);

    // add the button to the menu
    optionMenu.add(twoButtonMouseItem);

    // merge the "2 button" and "3 button mouse mode" into a button group
    ButtonGroup buttonGroup = new ButtonGroup();
    buttonGroup.add(twoButtonMouseItem);
    buttonGroup.add(threeButtonMouseItem);

    // add the "component configuration" menu
    optionMenu
        .add(createMenuItem(COMPONENT_CONFIGURATION, optionMenuController));

    // add the "determine bounding box" entry
    optionMenu.add(createMenuItem(BOUNDING_BOX, optionMenuController));

    // finally, add the menu itself to the menu bar!
    add(optionMenu);
  }

  /**
   * Generates the languages menu with all appropriate entries
   */
  private void buildLanguageMenu() {
    // generate the language menu itself
    languageMenu = AnimalTranslator.getGUIBuilder().generateJMenu("locale",
        null);

    // retrieve the set of defined (and thus supported) languages
    XProperties languageProperties = animalMainWindow
        .retrieveLanguageDefinitions();
    if (animalMainWindow.languages == null) {
      animalMainWindow.languages = new Vector<String>(20);
    }
    LanguageMenuController languageMenuController = new LanguageMenuController(
        animalInstance);

    // iterate over language keys
    int nrLang = 0;
    boolean isOK = true;

    while (isOK) {
      String localeString = languageProperties.getProperty(String
          .valueOf(nrLang), "n");
      isOK = !localeString.equalsIgnoreCase("n");

      if (isOK) {
        addLanguageSupportEntry(localeString, languageMenuController,
            languageProperties);
      }

      nrLang++;
    }

    // insert the language menu to the tool bar
    add(languageMenu);
  }

  private void addLanguageSupportEntry(String localeString,
      ActionListener listener, XProperties languageProperties) {
    // add a StringTokenizer that helps parsing the element
    StringTokenizer stok = new StringTokenizer(localeString, " _");

    // retrieve the base language code (e.g., "en", "de", "es")
    String baseCode = stok.nextToken();

    // determine if we need to use the full locale string,
    // or just the language key
    String codeToUse = localeString;
    String languageEntry = languageProperties.getProperty(localeString
        + ".label");
    char mnemonic = languageProperties.getProperty(localeString + ".mnemonic")
        .charAt(0);
    String toolTipText = languageProperties.getProperty(localeString
        + ".toolTipText");
    String iconName = languageProperties
        .getProperty(localeString + ".iconName");
    String localeStringInternal = languageProperties.getProperty(localeString
        + ".locale");
    // AnimalTranslator.translateMessage(localeString +".label",
    // null, false);

    if (languageEntry.equalsIgnoreCase(codeToUse)) {
      codeToUse = baseCode;
    }
    JMenuItem menuItem = new JMenuItem(languageEntry);
    menuItem.setMnemonic(mnemonic);
    menuItem.setToolTipText(toolTipText);
    menuItem.addActionListener(listener);
    menuItem.setActionCommand(localeStringInternal);
    if (iconName != null && iconName.length() > 0)
      menuItem.setIcon(AnimalTranslator.getGUIBuilder().getImageIcon(iconName));
    // JMenuItem menuItem = createMenuItem(codeToUse, localeString, listener);

    if (languageMenu == null) {
      languageMenu = AnimalTranslator.getGUIBuilder().generateJMenu("locale",
          null);
    }

    // languageMenu.add(helper);
    languageMenu.add(menuItem);
  }

  /**
   * Generates the help menu with all appropriate entries
   */
  private void buildHelpMenu() {
    // create the "help" menu
    helpMenu = AnimalTranslator.getGUIBuilder().generateJMenu("help", null);

    // generate the concrete controller instance
    helpMenuController = new HelpMenuController();

    // try to assign the menu as the "Help Menu"
    try {
      setHelpMenu(helpMenu);
    } catch (Error e) { // setHelpMenu not yet implemented in JMenuBar
    }

    // add the "about" menu item
    helpMenu.add(createMenuItem(ABOUT, helpMenuController));

    // add the "animation information" menu item
    helpMenu.add(createMenuItem(ANIMATION_INFORMATION, helpMenuController));

    // add the "show AnimalScript information" menu item
    helpMenu.add(createMenuItem(ANIMALSCRIPT_DEFINITION, helpMenuController));

    // add the "tutorial" menu item
//    helpMenu.add(createMenuItem(TUTORIAL, helpMenuController));

    // add the "show animation window size" menu item
    helpMenu.add(createMenuItem(ANIMATION_WINDOW_SIZE, helpMenuController));

    // add the "show quiz results" menu item
    helpMenu.add(
        quizResults = createMenuItem(QUIZ_RESULTS, helpMenuController));
    
    helpMenu.addSeparator();
    JMenuItem buttonSaveBugReportFile = new JMenuItem("Save BugReportFile");
    buttonSaveBugReportFile.setIcon(AnimalReader.getImageIcon(AnimalReader.getInputStreamOnLayer(AnimalImageDummy.class, "debug.png")));
    buttonSaveBugReportFile.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setSelectedFile(new File("BugReportFile.txt"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
        if (fileChooser.showSaveDialog(animalMainWindow) == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          if(file.getName().endsWith(".txt")) {
            Animal.getBugReporter().writeBugReportFileContent(file);
          } else {
            JOptionPane.showMessageDialog(animalMainWindow, "Wrong File Extension", "Dialog",
                JOptionPane.ERROR_MESSAGE);
            buttonSaveBugReportFile.doClick();
          }
        }
      }
    });
    helpMenu.add(buttonSaveBugReportFile);

    // finally, add the help menu itself to the menu bar!
    add(helpMenu);
  }

  /**
   * defines if the three-button mouse mode is active
   * 
   * @param hasThreeButtons
   *          true if the three-button mouse mode is active
   */
  public void setThreeButtonMouseMode(boolean hasThreeButtons) {
    mouseTypeIsThreeButtonMouse = hasThreeButtons;
  }

  /**
   * determines if the three-button mouse mode is active
   * 
   * @return true if the three-button mouse mode is active
   */
  public boolean threeButtonMouseMode() {
    return mouseTypeIsThreeButtonMouse;
  }

  /**
   * creates a new JMenuItem based on the key given
   * 
   * @param key
   *          the I18N key for the new element
   * @param listener
   *          the ActionListener for this menu item
   * @return an appropriately generated JMenuItem
   */
  private JMenuItem createMenuItem(String key, ActionListener listener) {
    // return a menu item for the default controller
    return createMenuItem(key, key, listener);
  }

  /**
   * creates a new JMenuItem based on the key for a specific listener
   * 
   * @param key
   *          the I18N key for the new element
   * @param command
   *          the action command that identifies the element
   * @param listener
   *          the ActionListener for this menu item
   * @return an appropriately generated JMenuItem
   */
  private JMenuItem createMenuItem(String key, String command,
      ActionListener listener) {
    // generate the menu item
    JMenuItem helper = AnimalTranslator.getGUIBuilder().generateJMenuItem(key,
        true);
    // TODO: was "...Item(key, true)"; where true means "useIcon".

    // add the action listener
    helper.addActionListener(listener);

    // set the action command that identifies this component
    helper.setActionCommand(command);

    // // check for an image icon
    // String iconName = Translator.translateMessage(key +".icon");
    // if (iconName != null && iconName.length() > 2)
    // helper.setIcon(AnimalTranslator.getGUIBuilder().getImageIcon(iconName));
    // finally, return it!
    return helper;
  }

  /**
   * creates a new JMenuItem based on the key for a specific listener
   * 
   * @param key
   *          the I18N key for the new element
   * @param initialValue
   *          the initial value for this element
   * @param listener
   *          the ActionListener for this menu item
   * @return an appropriately generated JMenuItem
   */
  private JMenuItem createToggleMenuItem(String key, boolean initialValue,
      ActionListener listener) {
    return createToggleMenuItem(key, initialValue, key, listener);
  }

  /**
   * creates a new JMenuItem based on the key for a specific listener
   * 
   * @param key
   *          the I18N key for the new element
   * @param initialValue
   *          the initial value for this element
   * @param command
   *          the action command that identifies the element
   * @param listener
   *          the ActionListener for this menu item
   * @return an appropriately generated JMenuItem
   */
  private JMenuItem createToggleMenuItem(String key, boolean initialValue,
      String command, ActionListener listener) {
    // generate the menu item
    JMenuItem helper = AnimalTranslator.getGUIBuilder()
        .generateToggleableJMenuItem(key, null, true, initialValue);

    // add the action listener
    helper.addActionListener(listener);

    // set the action command that identifies this component
    helper.setActionCommand(command);

    // finally, return it!
    return helper;
  }
    /**
   * @return the exerciseMenu
   */
  public JMenu getExerciseMenu() {
    return exerciseMenu;
  }

  /**
   * @param exerciseMenu the exerciseMenu to set
   */
  public void setExerciseMenu(JMenu exerciseMenu) {
    this.exerciseMenu = exerciseMenu;
  }
  public void allowEditing(boolean allowsEditing) {
    animationOverviewItem.setEnabled(allowsEditing);
    drawWindowItem.setEnabled(allowsEditing);
    objectsWindowItem.setEnabled(allowsEditing);
  }
  public void setQuizResultState(boolean newState) {
    quizResults.setEnabled(newState);
  }

  /**
   * zooms the menubar
   * 
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public void zoom(boolean zoomIn) {
    Dimension size = this.getSize();
    Dimension menuSize = fileMenu.getSize();
    Font f = fileMenu.getFont();
    int fSize = f.getSize();

    if (zoomIn) {
      size.setSize(size.getWidth() + 4, size.getHeight() + 4);
      menuSize.setSize(menuSize.getWidth() + 4, menuSize.getWidth() + 4);
      fSize = fSize + 2;

    } else {
      size.setSize(size.getWidth() - 4, size.getHeight() - 4);
      menuSize.setSize(menuSize.getWidth() - 4, menuSize.getWidth() - 4);
      fSize = fSize - 2;
    }

    if (fSize >= 10 && fSize <= 24) {

      f = new Font(f.getName(), f.getStyle(), fSize);
      fileMenu.setFont(f);

      for (Component j : fileMenu.getMenuComponents()) {
        j.setFont(f);
      }

      animationOverviewItem.setFont(f);
      animationWindowItem.setFont(f);
      drawWindowItem.setFont(f);
      editMenu.setFont(f);

      for (Component j : editMenu.getMenuComponents()) {
        j.setFont(f);
      }
      helpMenu.setFont(f);
      for (Component j : helpMenu.getMenuComponents()) {
        j.setFont(f);
      }
      languageMenu.setFont(f);
      for (Component j : languageMenu.getMenuComponents()) {
        j.setFont(f);
      }
      objectsWindowItem.setFont(f);
      optionMenu.setFont(f);
      for (Component j : optionMenu.getMenuComponents()) {
        j.setFont(f);
      }

      threeButtonMouseItem.setFont(f);
      timeLineWindowItem.setFont(f);
      twoButtonMouseItem.setFont(f);
      variableViewItem.setFont(f);
      exerciseMenu.setFont(f);

      for (Component j : exerciseMenu.getMenuComponents()) {
        j.setFont(f);
      }

      quizResults.setFont(f);
    }

    /*
     * System.out.println("sub menu size: " + subMenu.getComponentCount()); for
     * (Component c : subMenu.getComponents()) {
     * System.out.println("zoom submenu"); c.setFont(f); }
     */

    if (empty != null) {
      empty.setFont(f);

    }
    if (examples != null)
      examples.setFont(f);

    if (generator != null)
      generator.setFont(f);

    if (editMenuController != null) {
      editMenuController.zoom(zoomIn);
    }

    if (helpMenuController != null) {
      helpMenuController.zoom(zoomIn);
    }
    AboutDialog.getAboutDialog(Animal.get()).zoom(zoomIn);
    AnimInfoDialog.getAnimInfoDialog(Animal.get()).zoom(zoomIn);
    OptionDialog.getOptionDialog(animalInstance).zoom(zoomIn);

    this.setSize(size);
    this.repaint();
  }
}
