package animal.main;

import generators.framework.Generator;
import generators.framework.GeneratorManager;
import generators.generatorframe.controller.Starter;
import generators.generatorframe.store.SearchLoader;
import generators.generatorframe.view.GeneratorFrame;
import helpers.BugReporter;
import htdptl.gui.HtDPTLWizard;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JOptionPane;

import translator.AnimalTranslator;
import animal.animator.Move;
import animal.animator.TimedShow;
import animal.dialog.HelpWindow;
import animal.editor.Editor;
import animal.exchange.AnimationExporter;
import animal.exchange.AnimationImporter;
import animal.graphics.PTBoxPointer;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;
import animal.graphics.PTPolyline;
import animal.graphics.meta.TextContainer;
import animal.gui.AnimalMainWindow;
import animal.gui.AnimalScriptInputWindow;
import animal.gui.AnimalStartUpProgress;
import animal.gui.AnimationOverview;
import animal.gui.DrawWindow;
import animal.main.lookandfeel.LookAndFeelManager;
import animal.misc.AnimalFileChooser;
import animal.misc.AnimalRepositoryConnector;
import animal.misc.ComponentConfigurer;
import animal.misc.InteractionInterface;
import animal.misc.MessageDisplay;
import animal.misc.QuestionDummy;
import animal.misc.QuestionInterface;
import animal.misc.TrueFalseQuestionHandler;
import animal.misc.XProperties;
import animalscript.core.AnimalScriptParser;
import connect.moodle.MoodleConnect;

/**
 * The main window of Animal. Contains methods for displaying main menu, showing
 * the other windows, file operations
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido R&ouml;&szlig;ling </a>
 * @version 1.0 18.07.1998
 */
public class Animal extends AnimalFrame implements ButtonController,
    InteractionInterface {
  /**
   * Comment for <code>serialVersionUID</code>.
   */
  private static final long                serialVersionUID          = 2614942543305080965L;

  // =================================================================
  // CONSTANTS
  // =================================================================

  /**
   * display error messages?
   */
  private static final boolean             DEBUG                     = false;               // Boolean.getBoolean("debug");

  private static boolean                   LOCALE_INITIALIZED        = false;

  public static boolean                    PREVENT_EDITING           = false;

  private static boolean                   SHOW_PROGRESS_WINDOW      = true;

  public static final String[]             GLOBAL_FONTS              = {
      "Serif", "SansSerif", "Monospaced"                            };

  /**
   * the prefix for all GraphicObject classes. E.g. a point is a PTPoint.
   */
  public static final String               GRAPHICOBJECTS_PREFIX     = "PT";

  /**
   * header of the properties written by Animal
   */
  private static final String              PROPERTY_HEADER           = "Animal Properties";

  /**
   * constant for ASCII import/export
   */
  public static final int                  PROTOCOL_VERSION          = 1;

  private Vector<String>                   resourcesAdded            = new Vector<String>(
                                                                         10, 5);
  // =================================================================
  // STATIC ATTRIBUTES
  // =================================================================

  /**
   * This hashtable holds the references to the registered action objects. It is
   * used -- among other things which may be part of future extensions -- for
   * causing the elements to be translated whenever a new locale is chosen.
   */
  // public static Hashtable actions = new Hashtable(47);

  /**
   * A reference to the only existing Animal. This is used to access non-static
   * members in static functions and to have an access to the current instance
   * of Animal without holding a variable and having to pass a parameter
   */
  private static Animal                    animal                    = null;

  /**
   * FileChooser for animation loading/saving
   */
  private static AnimalFileChooser         animalFileChooser;

  private static AnimalRepositoryConnector animalRepositoryConnector = null;

  /**
   * The current locale, responsible for element translation
   */
  private static Locale                    currentLocale             = Locale.US;

  public static boolean CrypToolMode = false;
  
  private int                              zoomCounter               = 0;

  /**
   * The current date format
   */
  // private static DateFormat dateFormat = DateFormat.getTimeInstance(
  // DateFormat.MEDIUM, Locale.getDefault());
  /**
   * the last time a timedMessage was printed
   */
  private static long                      lastTime;

  // private static HashMap registeredComponents;
  //
  // protected static ResourceLocator resourceLocator = ResourceLocator
  // .getResourceLocator();

  private static int                       slideShowDelay            = 1000;

  protected AnimalScriptInputWindow        scriptInputWindow;

  public static AnimalStartUpProgress   ProgressPanel             = null;

  /**
   * The hashtable of GUI elements that may be translated
   */
  // public static Hashtable translatableGUIElements = new Hashtable(401);

  private static boolean                   animationLoadFinished     = false;

  private static boolean                   ALREADY_GOT_IT            = false;

  // =================================================================
  // ATTRIBUTES
  // =================================================================
  private static AnimalConfiguration       animalConfig              = null;

  private static AnimalScriptParser        animalScriptParser;

  // =================================================================
  // INITIALIZATION
  // =================================================================
  private static InteractionInterface      interactionHandlerFactory = null;

  // private static AnimalGeneratorGUI generatorGUI;
  public static Starter              generatorDemo;

  /**
   * the animation used by Animal. This variable is required and can't be
   * replaced by Animation.get(), as hasChanged etc. would refer to a pending
   * Animation and thus always return true which would result in setAnimation
   * always asking whether to save the changed animation.
   */
  private Animation                        animation;

  /**
   * load the last used file when starting Animal? Otherwise, a new file is
   * generated
   */
  private boolean                          autoloadLastFile          = true;

  private boolean autoHide = false;
  private boolean                          calcBoundingBox           = false;

  /**
   * compress files using GZipStreams when saving? When loading, Animal
   * determines automatically whether or not to use GZipStreams
   */
  private boolean                          compressFiles             = true;

  /** the filename of the file currently edited */
  // private String currentFilename;
  /**
   * the directory to which the last file was written / from which the last file
   * was read. To be used for future write/read operations.
   */
  private String                           defaultDirectory;

  /** a table containing all editors used by Animal */
  // private Hashtable editors = null;

  private boolean                          isCompressed              = true;

  /**
   * "time" when the Animation was saved last. This correspons to the <code>
   * Animation</code> s change counter.
   */
  private int                              lastChange                = -1;

  private AnimalMainWindow                 animalMainWindow;

  // private Hashtable resources = new Hashtable(7);
  
  /**
   * Instance of the Moodle connect class handling the connection to Moodle.
   */
  private MoodleConnect                           moodle                    = null;

  // =================================================================
  // CONSTRUCTORS
  // =================================================================

  /**
   * constructor for Animal. Doesn't do much, not even initialization, which is
   * done in <code>init()</code>. This is done because a reference to Animal is
   * required for initialization, which cannot be assigned to a variable in the
   * class' constructor. Thus, the constructor must be called, the result be
   * assigned to a variable and then the initialization be started. Then, the
   * initialization can refer to the Animal object.
   */
  public Animal() {
    this(false, null);
    // MessageDisplay.addDebugMessage("empty constructor for Animal
    // finished");
  } // Animal()

  /**
   * constructor for Animal. Doesn't do much, not even initialization, which is
   * done in <code>init()</code>. This is done because a reference to Animal is
   * required for initialization, which cannot be assigned to a variable in the
   * class' constructor. Thus, the constructor must be called, the result be
   * assigned to a variable and then the initialization be started. Then, the
   * initialization can refer to the Animal object.
   * 
   * @param appletUsage
   *          if true, mark as running in Applet context.
   * @param codeBase
   *          the base URL for the (potential) applet
   */
  public Animal(boolean appletUsage, URL codeBase) {
    super(null, null, appletUsage);
    // MessageDisplay.addDebugMessage("@Animal(" +appletUsage +", "
    // +codeBase +"): super constructor is now finished.");
    // MessageDisplay.addDebugMessage("set title");
    setTitle("");
    runsInApplet = appletUsage;
    baseURL = codeBase;
    Starter.loadGenerator();
  }

  // =================================================================
  // CLASS METHODS
  // =================================================================
  public static int getStringWidth(String text, Font f) {
    int width = 0;
    FontMetrics fm = getConcreteFontMetrics(f);

    if ((fm != null) && (text != null)) {
      width = fm.stringWidth(text);
    }

    return width;
  }

  @SuppressWarnings("deprecation")
  public static FontMetrics getConcreteFontMetrics(Font f) {
    Graphics graphics = Animal.get().getGraphics();
    // problem: need an Animal instance running before!
    if (graphics != null)
      return graphics.getFontMetrics(f);
    return Toolkit.getDefaultToolkit().getFontMetrics(f);
  }

  /**
   * returns a reference to the Animal object. Some objects require an Animal
   * object but don't contain a variable for this.
   */
  public static Animal get() {
    if (ALREADY_GOT_IT && animal != null) {
      return animal;
    }
    if(Animal.ProgressPanel!=null) Animal.ProgressPanel.addTextWithPercentStart(AnimalStartUpProgress.PROGRESS_LABEL_CreatingnewAnimalinstance);
    // MessageDisplay.addDebugMessage("create new instance");
    if (animal == null) {
      animal = new Animal();
    }
    if(Animal.ProgressPanel!=null) Animal.ProgressPanel.addTextWithPercentEnd(AnimalStartUpProgress.PROGRESS_LABEL_CreatingnewAnimalinstance);

    if(Animal.ProgressPanel!=null) Animal.ProgressPanel.addTextWithPercentStart(AnimalStartUpProgress.PROGRESS_LABEL_LoadingAnimalproperties);
    // MessageDisplay.addDebugMessage("create configuration and load
    // properties");
    animalConfig = getAnimalConfiguration();
    animalConfig.initializeImportFormats();
    animalConfig.initializeExportFormats();

    AnimationExporter.setAnimalConfig(animalConfig);
    AnimationImporter.setAnimalConfig(animalConfig);

    if(Animal.ProgressPanel!=null) Animal.ProgressPanel.addTextWithPercentStart(AnimalStartUpProgress.PROGRESS_LABEL_Initializingdefaultlocale);
    // MessageDisplay.addDebugMessage("initialize default locale");
//    Animal.advanceProgressDisplay(5);
    if (!LOCALE_INITIALIZED) {
      animal.initLocale();
    }

    ALREADY_GOT_IT = true;
    return Animal.animal;
  }

  public void initializeFileChooser() {
    if (!runsInApplet) {
      // MessageDisplay.addDebugMessage("creating AnimalFileChooser...");
      if (animalFileChooser == null) {
        animalFileChooser = new AnimalFileChooser(getAnimalConfiguration());
        // MessageDisplay.addDebugMessage("setting
        // AnimalLoadFilters...");
        animalFileChooser.setAnimalLoadFilters();

        String defDir = getAnimalConfiguration().getProperty(
            "animal.defaultDirectory");

        if (defaultDirectory != null) {
          animalFileChooser.setCurrentDirectory(defDir);
        }
      }
    }
  }

  public AnimalMainWindow getMainWindow() {
    return animalMainWindow;
  }

  public static int getSlideShowDelay() {
    return slideShowDelay;
  }

  public static boolean animationLoadFinished() {
    return animationLoadFinished;
  }

  public static void setAnimationLoadFinished(boolean isFinished) {
    animationLoadFinished = isFinished;
  }

  public static void setSlideShowDelay(int delay) {
    slideShowDelay = delay;
  }

  /**
   * displays a message together with the time passed since the last call to
   * <code>timedMessage</code>. This is useful for taking the time required by
   * methods.
   * 
   * @param text
   *          the text to be displayed
   */
  public static void timedMessage(String text) {
    long now = System.currentTimeMillis();
    MessageDisplay.message(text + " " + (now - lastTime));
    lastTime = now;
  }

  public static InteractionInterface getInteractionHandler() {
    if (interactionHandlerFactory == null) {
      interactionHandlerFactory = Animal.get();
    }

    return interactionHandlerFactory;
  }

  public QuestionInterface getHandlerFor(int interactionType, String questionID) {
    if (interactionType == InteractionInterface.TRUE_FALSE) {
      return new TrueFalseQuestionHandler();
    }

    return new QuestionDummy(
        AnimalTranslator.translateMessage("questionNotYetSupported"));
  }

  public static void setInteractionHandlerFactory(
      InteractionInterface handlerInterface) {
    interactionHandlerFactory = handlerInterface;
  }

  public static AnimalConfiguration getAnimalConfiguration() {
    if (animalConfig == null) {
      animalConfig = new AnimalConfiguration();
    }
    return animalConfig;
  }

  public String getWebRoot() {
    return getAnimalConfiguration().getProperty("animal.webroot",
        "http://www.algoanim.info/Animal2");
  }

  public void initialize(QuestionInterface question, boolean isConstructed,
      String text) {
    if (question instanceof TrueFalseQuestionHandler) {
      TrueFalseQuestionHandler localHandler = (TrueFalseQuestionHandler) question;

      localHandler.SetQuestion(text);
      localHandler.MakePanel();
    }
  }

  public void performQuestionOperation(QuestionInterface question) {
    if (question instanceof TrueFalseQuestionHandler) {
      ((TrueFalseQuestionHandler) question).getComponent().setVisible(true);

      //AnimalMainWindow.getWindowCoordinator().getAnimationWindow(false).enableControls(false);

     // if (actb != null) {
     //   actb.enableControls(false);
     // }
    } else {
      JOptionPane.showMessageDialog(this,
          AnimalTranslator.translateMessage("questionNotYetSupported"));
    }
  }

  private void addGUIResources(Locale l) {
    if (resourcesAdded.contains(l.toString()))
      return;
    String resourceName = "i18n/I18NGUIResources."
        + AnimalTranslator.getTranslator().getCurrentLocale();
    try {
      AnimalTranslator.addResource(resourceName);
    } catch (FileNotFoundException fnfe) {
      System.err.println("Resource not found (ignore for now) " + resourceName);
    }
    resourcesAdded.add(l.toString());

  }

  /**
   * does all the initialization for Animal. For the requirement of this special
   * method, cf. the documentation of the constructor.
   * 
   * @see Animal#get()
   */
  public void init() {
	  if(Animal.ProgressPanel!=null) Animal.ProgressPanel.addTextWithPercentStart(AnimalStartUpProgress.PROGRESS_LABEL_InitializingAnimal);
    addGUIResources(AnimalTranslator.getTranslator().getCurrentLocale());
    // String resourceName = "i18n/I18NGUIResources."
    // + AnimalTranslator.getTranslator().getCurrentLocale();
    // try {
    // AnimalTranslator.addResource(resourceName);
    // } catch(FileNotFoundException fnfe) {
    // System.err.println("Resource not found (ignore for now) " +resourceName);
    // }

    if(Animal.ProgressPanel!=null) Animal.ProgressPanel.addTextWithPercentStart(AnimalStartUpProgress.PROGRESS_LABEL_InstantiateAnimalmainwindow);
    // ensure the AnimalMainWindow is initialized!
    if (animalMainWindow == null) {
      animalMainWindow = new AnimalMainWindow(this, getAnimalConfiguration()
          .getProperties(), false, DEBUG);
    }

    if(Animal.ProgressPanel!=null) Animal.ProgressPanel.addTextWithPercentStart(AnimalStartUpProgress.PROGRESS_LABEL_InitializeAllEditors);
    // already done when creating the animal main window!
    getAnimalConfiguration().initializeAllEditors();

//    Animal.advanceProgressDisplay(10);
    if(Animal.ProgressPanel!=null) Animal.ProgressPanel.addTextWithPercentEnd(AnimalStartUpProgress.PROGRESS_LABEL_InitializeAllEditors);

    // make sure the window is also displayed :-)
    animalMainWindow.setVisible(true);
    
    autoHide = getAnimalConfiguration().getProperties().getBoolProperty("animal.autoHide", false);
    AnimalMainWindow.getWindowCoordinator().hideMenu(autoHide);

    // ensure we know that the window is now initialized!
    initialized = true;
  } // Init

  // =================================================================
  // Animation Setting / Retrieving
  // =================================================================
  public Animation getAnimation() {
    return animation;
  }

  public String getAnimationFont() {
    // return animationFont;
    return getAnimalConfiguration().getProperty("animal.animationFont",
        "SansSerif");
  }

  public void saneFontSizes() {
    Vector<PTGraphicObject> v = animation.getGraphicObjects();
    Font f;
    Font fNeu;
    int newSize;
    PTGraphicObject ptgo = null;
    TextContainer tc = null;
    for (int i = 0; i < v.size(); i++) {
      ptgo = v.elementAt(i);
      if (ptgo != null && ptgo instanceof TextContainer) {
        tc = (TextContainer) ptgo;
        f = tc.getFont();

        if (f != null) {
          newSize = f.getSize();
          newSize -= (newSize % 2);

          // If [0, 7], set to 8
          // If [16, 21], set to 16
          // If [22, ?], set to 24
          if (newSize < 8) {
            newSize = 8;
          } else if ((newSize > 16) && (newSize < 22)) {
            newSize = 16;
          } else if (newSize > 22) {
            newSize = 24;
          }

          fNeu = new Font(f.getName(), f.getStyle(), newSize);
          tc.setFont(fNeu);
        }
      }
    }

    // drawWindow.update();
    AnimalMainWindow.getWindowCoordinator().getDrawWindow(false).update();
    animation.doChange();
  }

  /**
   * set the current Animation. Ask whether or not to save the old Animation if
   * it was changed since the last time it was saved.
   * 
   * return true the new Animation could be set, <br>
   * false setting the new Animation was cancelled in the "save file"-dialog.
   */
  public boolean setAnimation(Animation newAnimation) {
    Animation theAnim = newAnimation;
    // reject most recent changes from drawWindow
    if (AnimalMainWindow.getWindowCoordinator().getDrawWindow(false)
        .isInitialized()) {
      AnimalMainWindow.getWindowCoordinator().getDrawWindow(true).writeBack();
    }

    // when called first, no animation exists(should be obvious :) )
    // save the old Animation?
    if (animation != null) {
      // if ((AnimalMainWindow.getWindowCoordinator().getAnnotationEditor(false)
      // != null)
      // && AnimalMainWindow.getWindowCoordinator().getAnnotationEditor(false)
      // .hasChanged()) {
      // int result = JOptionPane.showConfirmDialog(this, new String[] {
      // AnimalTranslator.translateMessage("annotationsChanged"),
      // AnimalTranslator.translateMessage("annotationSaveQuery") },
      // AnimalTranslator.translateMessage("annotationSave"),
      // JOptionPane.YES_NO_CANCEL_OPTION);
      //
      // if (result == JOptionPane.YES_OPTION) {
      // AnimalMainWindow.getWindowCoordinator().getAnnotationEditor(false)
      // .saveAnnotationsAs();
      // }
      // }

      if (hasChanged()) {
        int result = JOptionPane.showConfirmDialog(this, new String[] {
            AnimalTranslator.translateMessage("animationChanged"),
            AnimalTranslator.translateMessage("animationSaveQuery") },
            AnimalTranslator.translateMessage("animationSave"),
            JOptionPane.YES_NO_CANCEL_OPTION);

        // either the "save-file"-dialog or the FileDialog was
        // cancelled.
        // Then, discard the new Animation and keep the old one.
        // In the "save-file"-dialog:
        // if "yes" is selected the old Animation is saved and the new
        // one set,<p>
        // if "no", the old Animation is not saved but the new one set
        // <p>
        // if "cancel", neither the old Animation is saved nor the new
        // one set.
        if (((result != JOptionPane.YES_OPTION) && (result != JOptionPane.NO_OPTION))
            || ((result == JOptionPane.YES_OPTION) && !AnimationExporter
                .exportAnimation(animation))) {
          theAnim.discard();

          return false; // cancel
        }
      }
    }

    // make the new Animation the one used by Animal.
    if (theAnim == null) {
      theAnim = new Animation();
    }

    theAnim.register();

    // and make it known to Animal(cf. comment on field animation above)
    this.animation = theAnim;

    // notify the windows that the Animation has changed.
    // if (animationWindow != null) {
    AnimalMainWindow.getWindowCoordinator().getAnimationWindow(false)
        .setAnimation(theAnim);
    // }
    if (AnimalMainWindow.getWindowCoordinator().getAnimationOverview(false) != null) {
      AnimalMainWindow.getWindowCoordinator().getAnimationOverview(false)
          .setAnimation(theAnim);
    }

    // update the drawing window
    int firstVerifiedStep = theAnim.verifyStep(theAnim.getLink(Link.START)
        .getNextStep());
    AnimalMainWindow.getWindowCoordinator().getDrawWindow(true)
        .setAnimation(theAnim);
    AnimalMainWindow.getWindowCoordinator().getDrawWindow(false)
        .setStep(firstVerifiedStep);

    AnimalMainWindow.getWindowCoordinator().getAnimationOverview(true)
        .setStep(firstVerifiedStep, true);

    if (AnimalMainWindow.getWindowCoordinator().getTimeLineWindow(false) != null) {
      AnimalMainWindow.getWindowCoordinator().getTimeLineWindow(false)
          .updateList(theAnim);
    }

    // no change. File on disk is the same as in memory
    resetChange();
    setAnimationLoadFinished(true);
    String label = theAnim.getTitle();
    if (label == null || label.length() < 1)
      label = AnimalTranslator.translateMessage("unnamed");
    setFilename(label);
    AnimalMainWindow.getWindowCoordinator().getAnimationWindow(false)
        .startOfAnimation();
    if (newAnimation.getNrAnimators() != 0)
      AnimalMainWindow.getWindowCoordinator().getAnimationWindow(false)
      .setVisible(true);

    AnimalMainWindow.getWindowCoordinator().getAnimationWindow(false).setStep(1, true);
    return true;
  }

  public AnimalScriptInputWindow getScriptInputWindow() {
    if (scriptInputWindow == null) {
      scriptInputWindow = new AnimalScriptInputWindow(animal);
      scriptInputWindow.initScriptingGUI();
      if (zoomCounter > 0) {
        for (int i = 0; i < zoomCounter; i++) {
           scriptInputWindow.zoom(true);
        }

      } else {
        for (int i = 0; i > zoomCounter; i--) {
          scriptInputWindow.zoom(false);
        }
      }
    }
    return scriptInputWindow;
  }

  public void setAnimalScriptCode(String animationContent) {
    getScriptInputWindow().setScriptingContent(animationContent);
  }

  public void requestAnimationSave() {
    int result = 0;

    if (hasChanged()) {
      result = JOptionPane.showConfirmDialog(this, new String[] {
          AnimalTranslator.translateMessage("animationChanged"),
          AnimalTranslator.translateMessage("animationSaveQuery") },
          AnimalTranslator.translateMessage("animationSave"),
          JOptionPane.YES_NO_CANCEL_OPTION);

      // either the "save-file"-dialog or the FileDialog was cancelled.
      // Then, discard the new Animation and keep the old one.
      // In the "save-file"-dialog:
      // if "yes" is selected the old Animation is saved and the new
      // one set,<p>
      // if "no", the old Animation is not saved but the new one set <p>
      // if "cancel", neither the old Animation is saved nor the new
      // one set.
      if (((result != JOptionPane.YES_OPTION) && (result != JOptionPane.NO_OPTION))
          || ((result == JOptionPane.YES_OPTION) && !AnimationExporter
              .exportAnimation(animation))) {
        animation.discard();
      }
    }
  }

  /**
   * sets the current animation font
   * 
   * @param af
   *          the actual font name to be used
   */
  public void setAnimationFont(String af) {
    if (af != null) {
      Object o;

      // animationFont = af;
      getAnimalConfiguration().getProperties().put("animal.animationFont", af);

      Vector<PTGraphicObject> v = animation.getGraphicObjects();
      Font f;
      Font fNeu;
      PTGraphicObject ptt;
      TextContainer tc = null;
      for (int i = 0; i < v.size(); i++) {
        o = v.elementAt(i);

        if (o instanceof TextContainer) {
          ptt = v.elementAt(i);
          tc = (TextContainer) ptt;
          f = tc.getFont();
          fNeu = new Font(af, f.getStyle(), f.getSize());
          tc.setFont(fNeu);
        }
      }

      AnimalMainWindow.getWindowCoordinator().getDrawWindow(false).update();
      animation.doChange();
    }
  }

  public void simpleSetAnimation(Animation localAnimation) {
    // make the new Animation the one used by Animal.
    localAnimation.register();

    // and make it known to Animal(cf. comment on field animation above)
    animation = localAnimation;

    // no change. File on disk is the same as in memory
    resetChange();
  }

  public void testAnimation(String message) {
    Link l = animation.getLink(Link.START);
    AnimationState animationState = new AnimationState(animation);

    for (; (l != null) && (l.getStep() != Link.END); l = animation.getLink(l
        .getNextStep())) {
      animationState.setQuickStep(l.getStep());
    }

    MessageDisplay.errorMsg(message, MessageDisplay.INFO, true);
  }

  // =================================================================
  // Property Management
  // =================================================================

  public static AnimalFileChooser getFileChooser() {
    return animalFileChooser;
  }

  private int retrieveBound(int objectBound, int minValue) {
    return (objectBound < minValue) ? minValue : objectBound;
  }
  
  
  public static void savePropertiesExtern() {
    animal.saveProperties();

  }

  /**
   * gets the properties from all windows and save them to the properties file.
   * Analogous/parallel to <code>loadProperties</code> except properties are
   * called from other windows.
   */
  private boolean saveProperties() {

    XProperties xprops = getAnimalConfiguration().getProperties();
    // properties from windows must only be fetched if these windows
    // actually have properties, i.e. they once were initialized
    DrawWindow drawWindow = AnimalMainWindow.getWindowCoordinator()
        .getDrawWindow(false);

    if ((drawWindow != null) && drawWindow.isInitialized()) {
      drawWindow.getProperties(xprops);
    }

    AnimationWindow animationWindow = AnimalMainWindow.getWindowCoordinator()
        .getAnimationWindow(false);

    if ((animationWindow != null) && animationWindow.isInitialized()) {
      animationWindow.getViewProperties(xprops);
    }

    AnimationCanvas animationCanvas = animationWindow.getAnimationCanvas();

    if (animationCanvas != null) {
      animationCanvas.getProperties(xprops);
    }

    AnimationOverview animOverview = AnimalMainWindow.getWindowCoordinator()
        .getAnimationOverview(false);
    if ((animOverview != null) && animOverview.isInitialized()) {
      animOverview.getProperties(xprops);
    }
    // save the own properties
    Rectangle objectBounds = getBounds();
    xprops.put("animal.x", objectBounds.x);
    xprops.put("animal.y", objectBounds.y);
    xprops.put("animal.width", retrieveBound(objectBounds.width, 400));
    xprops.put("animal.height", retrieveBound(objectBounds.height, 200));

    objectBounds = AnimalMainWindow.getWindowCoordinator().getDrawWindow(false)
        .getBounds();
    xprops.put("drawWindow.x", objectBounds.x);
    xprops.put("drawWindow.y", objectBounds.y);
    xprops.put("drawWindow.width", retrieveBound(objectBounds.width, 400));
    xprops.put("drawWindow.height", retrieveBound(objectBounds.height, 400));
    objectBounds = AnimalMainWindow.getWindowCoordinator()
        .getAnimationOverview(false).getBounds();
    xprops.put("AnimationOverview.x", objectBounds.x);
    xprops.put("AnimationOverview.y", objectBounds.y);
    xprops.put("AnimationOverview.width",
        retrieveBound(objectBounds.width, 400));
    xprops.put("AnimationOverview.height",
        retrieveBound(objectBounds.width, 400));

    objectBounds = AnimalMainWindow.getWindowCoordinator()
        .getAnimationWindow(false).getViewBounds();
    xprops.put("animationWindow.x", objectBounds.x);
    xprops.put("animationWindow.y", objectBounds.y);
    xprops.put("animationWindow.width", retrieveBound(objectBounds.width, 640));
    xprops
        .put("animationWindow.height", retrieveBound(objectBounds.width, 640));

    AnimationCanvas canvas = AnimalMainWindow.getWindowCoordinator()
        .getAnimationWindow(false).getAnimationCanvas();

    if (canvas != null) {
      canvas.setMagnification(1.0);
      xprops.put("animationCanvas.width", canvas.getCurrentSize().width);
      xprops.put("animationCanvas.height", canvas.getCurrentSize().height);
    }

    xprops.put("animal.compressFiles", isCompressed);

    // which windows are visible?
    xprops.put("animal.DrawWindowVisible",
        (drawWindow != null) && drawWindow.isVisible());
    xprops.put("animal.ObjectsWindowVisible", (AnimalMainWindow
        .getWindowCoordinator().getObjectsWindow(false) != null)
        && AnimalMainWindow.getWindowCoordinator().getObjectsWindow(false)
            .isVisible());
    AnimalMainWindow.getWindowCoordinator().getObjectsWindow(false).setVisible(false);
    xprops.put("animal.AnimationWindowVisible", (AnimalMainWindow
        .getWindowCoordinator().getAnimationWindow(false) != null)
        && AnimalMainWindow.getWindowCoordinator().getAnimationWindow(false)
            .isVisible());
    xprops.put("animal.TimeLineWindowVisible", (AnimalMainWindow
        .getWindowCoordinator().getTimeLineWindow(false) != null)
        && AnimalMainWindow.getWindowCoordinator().getTimeLineWindow(false)
            .isVisible());
    xprops.put("animal.AnimationOverviewVisible", (AnimalMainWindow
        .getWindowCoordinator().getAnimationOverview(false) != null)
        && AnimalMainWindow.getWindowCoordinator().getAnimationOverview(false)
            .isVisible());

    // file related properties
    defaultDirectory = animalFileChooser.getCurrentDirectoryName();
    xprops.put("animal.defaultDirectory", defaultDirectory);
    xprops.put("animal.autoloadLastFile", autoloadLastFile);
    xprops.put("animal.autoHide", autoHide);

    if (getAnimalConfiguration().getProperty("animal.fileFormat", "***")
        .equals("***")) {
      getAnimalConfiguration().setCurrentFormat(
          AnimalConfiguration.DEFAULT_FORMAT);
    }

    xprops.put("animal.bBoxOnLoad", calcBoundingBox);

    // finally try to save the properties
    try {
      // xprops.store(new FileOutputStream("src/animal.properties"), PROPERTY_HEADER);
      xprops.store(new FileOutputStream("animal.properties"), PROPERTY_HEADER);
      // xprops.store(new FileOutputStream("/animal.properties"), PROPERTY_HEADER);
      // System.out.println("store properties");
    } catch (IOException e) {
      MessageDisplay.errorMsg("errorSavingProperties", e.getMessage(),
          MessageDisplay.PROGRAM_ERROR);

      return false;
    }

    return true;
  }// saveProperties

  // =================================================================
  // I/O SUPPORT
  // =================================================================

  public String getTime() {
    StringBuilder sb = new StringBuilder();
    GregorianCalendar calendar = new GregorianCalendar();
    sb.append('[').append(calendar.get(Calendar.HOUR_OF_DAY));
    sb.append(':').append(calendar.get(Calendar.MINUTE));
    sb.append(':').append(calendar.get(Calendar.SECOND)).append("] ");

    return sb.toString();
  }

  /**
   * returns whether a change in the Animation has occured since the last time
   * it was saved.
   * 
   * @return true if a change occured, no if the Animation is in the same state
   *         as when it was saved for the last time.
   */
  public boolean hasChanged() {
    return lastChange != animation.getLastChange();
  }

  /**
   * automatically load the file that was edited in the last Animal session?
   * 
   * @return true if the last file is to be loaded, false if not.
   */
  public boolean isAutoloadLastFile() {
    return autoloadLastFile;
  }
  
  /**
   * automatically load the file that was edited in the last Animal session?
   * 
   * @return true if the last file is to be loaded, false if not.
   */
  public boolean isAutoHide() {
    return autoHide;
  }

  /**
   * use file compression for the Animation files?
   * 
   * @return true if files are to be compressed, false if not.
   */
  public boolean isCompressFiles() {
    return compressFiles;
  }

  /**
   * resets the change counter and thus marks the current Animation as recently
   * being saved, i.e. no changes have occured since saving.
   * <p>
   * Unlike in the other files( <strong>DrawWindow </strong> etc.) <code>
   * resetChange</code> and <code>hasChanged</code> have to be two different
   * methods, as the <i>lastChange </i> counter is only the same as in the
   * Animation if the file in memory is not changed compared to the file on
   * disk(resp. to a new file). I.e. not every change is saved when
   * <code>hasChanged()</code> is called, as is the case in the other classes.
   */
  public void resetChange() {
    animation.resetChange();
    AnimalMainWindow.getWindowCoordinator().getDrawWindow(false)
        .setStep(animation.getNextStep(0));
    lastChange = animation.getLastChange();
  }

  /**
   * sets whether to automatically load the file that was edited in the last
   * Animal session.
   * 
   * @param b
   *          true - do so, false don't load but create a new Animation instead.
   */
  public void setAutoloadLastFile(boolean b) {
    autoloadLastFile = b;
  }
  
  /**
   * sets whether to automatically hide the pdf menu Animal session.
   * 
   * @param b true - do so, false don't hide the pdf menu
   */
  public void setAutoHide(boolean b) {
    autoHide = b;
    AnimalMainWindow.getWindowCoordinator().hideMenu(autoHide);
  }
    
  /**
   * sets whether to use file compression for the Animation files
   * 
   * @param b
   *          true - use file compression,
   *          <p>
   *          false - don't use file compression.
   */
  public void setCompressFiles(boolean b) {
    compressFiles = b;
  }

  /**
   * sets the current filename and displays it in the Animal's main window's
   * title bar.
   * 
   * @param filename
   *          the filename to be set. If it is <strong>null </strong>, the title
   *          bar is set to "Animal", otherwise to "Animal - filename"
   */
  public void setFilename(String filename) {
    if (filename != null) {
      // currentFilename = filename;
      setTitle(AnimalTranslator.getTranslator().translateMessage("cc", filename));

      // if (animationWindow != null) {
      AnimalMainWindow.getWindowCoordinator().getAnimationWindow(false)
          .setTitle("Animal Animation: " + filename);

      // }
      // if (drawWindow != null) {
      AnimalMainWindow.getWindowCoordinator().getDrawWindow(false)
          .setTitle("Animation Drawing Window: " + filename);

      // drawWindow.setTitle("Animation: " + filename);
      // }
    } else {
      String versionInfo = AnimalConfiguration.getDefaultConfiguration()
          .getVersionLine("briefVersionInfoLine");

      // set the main window title
      setTitle(AnimalTranslator.getTranslator().translateMessage("cc", versionInfo));
//      setTitle(AnimalTranslator.getTranslator().translateMessage("cc"));
    }
  }

  // =================================================================
  // Animation I/O
  // =================================================================

  /**
   * creates a demo Animation, showing a linked list into which a new element is
   * inserted. Uses a lot of Animal's objects and features.
   */
  public void demoAnimation() { //TODO do nice demoAnimation
//    startAnimationFromAnimalScriptCodeFunc("");
    
    // new Object must be created, s.a.()
    Animation demoAnimation = new Animation();
    // create GraphicObjects
    PTBoxPointer a = new PTBoxPointer(40, 20, "Alpha");
    a.setPointerPosition(PTBoxPointer.POINTER_POSITION_RIGHT);
    a.setTip(0, 240, 30);
    demoAnimation.insertGraphicObject(a);
    demoAnimation.insertGraphicObject(new PTBoxPointer(240, 20, "Gamma"));
    demoAnimation.insertGraphicObject(new PTBoxPointer(200, 200, "Beta"));

    // create Animators for step 1
    demoAnimation.insertAnimator(new TimedShow(1, new int[] { 1, 2 }, 0,
        "show", true));

    Move move = new Move(3, new int[] { 1 }, 15, "setTip",
        demoAnimation.insertGraphicObject(new PTPolyline(
            new PTPoint[] { new PTPoint(240, 30), new PTPoint(300, 100),
                new PTPoint(200, 210) })));
    move.setUnitIsTicks(true);
    demoAnimation.insertAnimator(move);

    move = new Move(3, new int[] { 2 }, 15, "translate",
        demoAnimation.insertGraphicObject(new PTPolyline(new PTPoint[] {
            new PTPoint(0, 0), new PTPoint(60, 0) })));
    move.setUnitIsTicks(true);
    demoAnimation.insertAnimator(move);
    // demoAnimation.insertAnimator(new VariableDeclaration(2, "i",
    // Variable.TYPE_INT, "42", "main22a"));
    // demoAnimation.insertAnimator(new VariableUpdate(3, "i", "4242"));

    move = new Move(3, new int[] { 3 }, 15, "setTip",
        demoAnimation.insertGraphicObject(new PTPolyline(new PTPoint[] {
            new PTPoint(250, 240), new PTPoint(300, 30) })));
    move.setUnitIsTicks(true);
    demoAnimation.insertAnimator(move);

    // create Animators for step 4
    // move two objects along the same line.
    int line = demoAnimation.insertGraphicObject(new PTPolyline(new PTPoint[] {
        new PTPoint(100, 200), new PTPoint(40, 20) }));
    demoAnimation.insertAnimator(new Move(4, new int[] { 3 }, 2000,
        "translateWithFixedTip", line));
    move = new Move(4, new int[] { 1 }, 20, "setTip", line);
    move.setUnitIsTicks(true);
    demoAnimation.insertAnimator(move);

    // insert the links between the steps
    demoAnimation.insertLink(new Link(0, 1));
    demoAnimation.insertLink(new Link(1, 2, 2000));
    demoAnimation.insertLink(new Link(2, 3));
    demoAnimation.insertLink(new Link(3, 4, 1000));
    demoAnimation.insertLink(new Link(4, Link.END));

    if (setAnimation(demoAnimation))
      MessageDisplay.addDebugMessage(AnimalTranslator
          .translateMessage("demoAnimation"));
    setFilename(null);
  } // demoAnimation

  public void openRepository() {
    boolean isOK = false;

    if (animalRepositoryConnector == null) {
      animalRepositoryConnector = new AnimalRepositoryConnector();
    }

    if (animalRepositoryConnector.useRefresh()) {
      isOK = animalRepositoryConnector.readTableEntries();
    }

    if (isOK) {
      animalRepositoryConnector.buildGUI(AnimalTranslator
          .translateMessage("repositoryTitle"));
    }

    animalRepositoryConnector.setVisible(true);
  }

  public void openGenerator() {
    if (generatorDemo == null) {
      generatorDemo = new Starter(this);

      if (zoomCounter > 0) {
        for (int i = 0; i < zoomCounter; i++) {
          generatorDemo.zoom(true);
        }

      } else {
        for (int i = 0; i > zoomCounter; i--) {
          generatorDemo.zoom(false);
        }
      }

    }
    else
      generatorDemo.setVisible(true);
    // if (generatorGUI == null)
    // generatorGUI = new AnimalGeneratorGUI(getCurrentLocale());
    // else
    // generatorGUI.displayWizard();
  }
  
  public Starter getStarter() {
    return generatorDemo;
  }

  /**
   * Set the animation to a new animation (no animators or graphic primitives).
   */
  public void newFile() {
    if (setAnimation(new Animation())) {
      setFilename(null);
      AnimationWindow animWin = getAnimalConfiguration().getWindowCoordinator()
          .getAnimationWindow(false);
      animWin.setTitle(AnimalTranslator.translateMessage("animalAnimation"));
      resetChange();
      MessageDisplay.message(AnimalTranslator.translateMessage("newAnimation"));
    }
  }

  public void reloadFile() {
    AnimationImporter.importAnimation(getAnimalConfiguration()
        .getCurrentFilename(), getAnimalConfiguration().getCurrentFormat());

  }

  public void setBBoxCalculation(boolean determineOnLoad) {
    calcBoundingBox = determineOnLoad;
  }

  // =================================================================
  // Window Management
  // =================================================================

  /**
   * Closes the main window and leaves Animal if possible. If the current
   * Animation has changed, try to save it. Thus, closing the window(and by this
   * leaving Animal) can be aborted by pressing cancel in the "save
   * file"-dialog. If the Animation was unchanged or is saved, save the
   * properties also, close the window and leave Animal.
   */
  public boolean closeWindow() {
    if (!setAnimation(new Animation())) {
      return false;
    }

    saveProperties();
    System.exit(0);

    return true;
  }

  public void showTutorial() {
    HelpWindow helpWindow = new HelpWindow(this, getAnimalConfiguration()
        .getProperties(), AnimalTranslator.translateMessage("tutorial.label"),
        AnimalTranslator.translateMessage("tutorialPath"));
    helpWindow.setVisible(true);
  }

  /**
   * get all Editors from the file. Create classes for each classname found in
   * the file <code>components.dat</code>.
   * 
   * @return a <strong>Hashtable </strong> containing all Editors, including
   *         <strong>GraphicEditors </strong>, <strong>AnimatorEditors
   *         </strong>, <strong>LinkEditor </strong>. is accessed from
   *         ObjectPanel.
   */
  public Hashtable<String, Editor> getEditors() {
    return getAnimalConfiguration().getEditors();
  }

  public static boolean hasComponent(String componentName) {
    return get().getEditors().get(componentName) != null;
  }

  /**
   * get a certain Editor.
   * 
   * @param name
   *          the name of the Editor to get, not containing the package name.
   *          I.E. just "Polyline", not "animal.editor.PolylineEditor". If the
   *          name starts with "PT", this is ommited.
   * @return the Editor for the object. null, if none is found.
   */
  public Editor getEditor(String name) {
    return getEditor(name, true);
  }

  /**
   * get a certain Editor.
   * 
   * @param name
   *          the name of the Editor to get, not containing the package name.
   *          I.E. just "Polyline", not "animal.editor.PolylineEditor". If the
   *          name starts with "PT", this is ommited.
   * @return the Editor for the object. null, if none is found.
   */
  public Editor getEditor(String name, boolean yellOnError) {
    String editorName;

    if (name.startsWith(GRAPHICOBJECTS_PREFIX)) {
      editorName = name.substring(2);
    } else {
      editorName = name;
    }

    Editor result = getEditors().get(editorName);

    if ((result == null) && !runsInApplet && yellOnError) {
      MessageDisplay
          .errorMsg("noEditorFor", name, MessageDisplay.PROGRAM_ERROR);
    }

    return result;
  }

  public void showBoundingBox() {
    MessageDisplay.message(
        "boundingBoxSize",
        new Object[] { Integer.valueOf(getAnimation().getWidth()),
            Integer.valueOf(getAnimation().getHeight()) });
  }

  public void quitAnimal() {
    // if animation has been called by Moodle, send question results to Moodle
    if (moodle != null) {
      moodle.sendResults(getAnimalConfiguration().getInteractionController().getInteractionModels());
    }
    requestAnimationSave();
    //Toolkit.getDefaultToolkit().getSystemEventQueue()
    //    .postEvent(new WindowEvent(Animal.this, WindowEvent.WINDOW_CLOSING));
    MessageDisplay.message(AnimalTranslator.translateMessage(
        "thanksAndBye",
        AnimalConfiguration.getDefaultConfiguration().getVersionLine(
            "versionInfoLine")));
    System.exit(0);
  }

  public static AnimalScriptParser getAnimalScriptParser(boolean createNewParser) {
    if ((animalScriptParser == null) || createNewParser) {
      animalScriptParser = new AnimalScriptParser();
    }
    if (createNewParser)
      animalScriptParser.clearTables();
    return animalScriptParser;
  }

  // =================================================================
  // CONFIGURATION
  // =================================================================

  public void configureComponents() {
    new ComponentConfigurer();
  }

  public static Locale getCurrentLocale() {
    return currentLocale;
  }

  public void initLocale() {
    String localeString = getAnimalConfiguration().getProperty("animal.Locale",
        "en_US");
    LOCALE_INITIALIZED = true;

    if (localeString == null) {
      setAnimalLocale(Locale.US); // Locale.getDefault());
    } else {
      setAnimalLocale(new Locale(localeString.substring(0,
          localeString.indexOf('_')), localeString.substring(localeString
          .indexOf('_') + 1)));
    }
  }

  public void setAnimalLocale(Locale targetLocale) {
    AnimalTranslator.setTranslatorLocale(targetLocale);
    addGUIResources(targetLocale);
    getAnimalConfiguration().getProperties().put("animal.Locale",
        targetLocale.toString());

    if (isInitialized()) {
      MessageDisplay.message(AnimalTranslator.translateMessage("resLoaded"));
      // AnimalTranslator.getResourceBundle().getMessage("resLoaded"));
    }
    currentLocale = targetLocale;
    if (generatorDemo != null)
      generatorDemo.setGeneratorLocale(targetLocale);
    
    if(isInitialized()){
      AnimalMainWindow.getWindowCoordinator().getAnimationWindow(false).setAnimationWindowLocale(targetLocale);
    }
    // if (generatorGUI != null)
    // generatorGUI.setGeneratorLocale(targetLocale);
  }

  public boolean allowButtonAccess(int currentStep) {
    return true;
  }

  protected static void initializeProgressDisplay() {
    Animal.ProgressPanel = new AnimalStartUpProgress("Animal Start-Up");

  }

  protected static void hideProgressDisplay() {
    if (Animal.ProgressPanel != null)
      Animal.ProgressPanel.setInvisible();
  }

  private boolean teacherMode = false;
  
  public boolean isTeacherMode() {
    return teacherMode;
  }

  public void setTeacherMode(boolean teacherMode) {
    this.teacherMode = teacherMode;
  }

  protected void parseArguments(String[] args) {
    if (args == null || args.length == 0)
      return;
    // parse Moodle parameter if JAR has been called by Moodle
    String[] remainingArgs = args;
    if (MoodleConnect.calledByMoodle(args)) {
      moodle = new MoodleConnect();
      remainingArgs = moodle.parseArguments(args);
    }
    boolean explicitlyLoadAnimation = false;
    boolean showOnlyGenerators = false;
    boolean showGenerator = false;
    boolean topicMode = false;
    String schemeWizardPage = "";
    // int generatorIndex = -1;
    int currentPos = 0;
    // String generatorClass = null;
    // int generatorType = -1;
    boolean usesNewGeneratorInterface = false;

    while (currentPos < remainingArgs.length) {
      String currentArgument = remainingArgs[currentPos];
      String caLC = currentArgument.toLowerCase();

      if (caLC.equals("-help"))
        MessageDisplay
            .message(AnimalTranslator.translateMessage("animalUsage"));
      else if (caLC.equals("-teachermode")) {
        MessageDisplay
            .message(AnimalTranslator.translateMessage("teacherMode"));
        setTeacherMode(true);
        // TODO Update all Windows
      } else if (caLC.equals("--logonly")) {
        MessageDisplay.message(AnimalTranslator
            .translateMessage("logOnlyDeprecated"));
      } else if (caLC.startsWith("-locale=")) {
        String lKey = currentArgument
            .substring(currentArgument.indexOf('=') + 1);
        if (lKey.equalsIgnoreCase("de_DE"))
          animal.setAnimalLocale(Locale.GERMANY);
        else if (lKey.equalsIgnoreCase("it_IT"))
          animal.setAnimalLocale(Locale.ITALY);
        else
          animal.setAnimalLocale(Locale.US);
      } else if (caLC.equals("-generate")) {
//        System.err.println("generate...:" );
        if (remainingArgs.length > currentPos + 1) {
          String generatorName = remainingArgs[currentPos + 1];
//          System.err.println("generator Name: " + generatorName);
          // initialize generators
          openGenerator();
          // retrieve generator
          @SuppressWarnings("unused")
          Generator g = GeneratorManager.name2Generator.get(generatorName);
          // reset location of GeneratorWindow
        /**
          if (g != null) {
            DefaultMutableTreeNode dmtn = GeneratorManager.generator2Node.get(g);
            if (dmtn != null)
              generatorDemo.setSelectedNode(dmtn);
          }
         */ 
//"          generators.sorting.HeapSort"
        }
      }
      else if (caLC.startsWith("-generator")) {
        showOnlyGenerators = currentArgument.equalsIgnoreCase("-generatoronly");
        showGenerator = true;
        @SuppressWarnings("unused")
        int nrParams = 1; 
        String[] pathComponents = new String[5];
        // pathComponents[0] = "Generators"; // default: "Generators"
        pathComponents[0] = currentLocale.getLanguage(); // default: current
                                                         // language

        // new interface from here on...
        if (remainingArgs.length > currentPos + 1
            && remainingArgs[currentPos + 1].toLowerCase().startsWith("language=")) {
          currentPos++; // gobble "language=X"
          pathComponents[0] = remainingArgs[currentPos].substring(remainingArgs[currentPos]
              .indexOf('=') + 1);
          usesNewGeneratorInterface = true;
          // MessageDisplay.errorMsg("@0, language="+pathComponents[0],
          // MessageDisplay.RUN_ERROR);
        }
        // new interface!
        if (remainingArgs.length > currentPos + 1
            && remainingArgs[currentPos + 1].toLowerCase().startsWith("codelanguage=")) {
          currentPos++; // gobble "codelanguage=X"
          pathComponents[1] = remainingArgs[currentPos].substring(remainingArgs[currentPos]
              .indexOf('=') + 1);
          nrParams++; // added code language
          usesNewGeneratorInterface = true;
          // MessageDisplay.errorMsg("@1, codeLanguage="+pathComponents[1],
          // MessageDisplay.RUN_ERROR);
        }
        // new interface!
        if (remainingArgs.length > currentPos + 1
            && remainingArgs[currentPos + 1].toLowerCase().startsWith("type=")) {
          currentPos++; // gobble "type=X"
          pathComponents[2] = remainingArgs[currentPos].substring(remainingArgs[currentPos]
              .indexOf('=') + 1);
          nrParams++; // added code language
          usesNewGeneratorInterface = true;
          // MessageDisplay.errorMsg("@2, type="+pathComponents[2],
          // MessageDisplay.RUN_ERROR);
        }
        if (remainingArgs.length > currentPos + 1
            && remainingArgs[currentPos + 1].toLowerCase().startsWith("algorithm=")) {
          currentPos++; // gobble "algorithm=X"
          String helper = remainingArgs[currentPos].substring(remainingArgs[currentPos]
              .indexOf('=') + 1);
          if (helper.startsWith("\"") && helper.endsWith("\""))
            helper = helper.substring(1, helper.length() - 1);
          pathComponents[3] = helper;
          nrParams++; // added code language
          usesNewGeneratorInterface = true;
          // MessageDisplay.errorMsg("@3, algorithm="+pathComponents[3],
          // MessageDisplay.RUN_ERROR);
        }
        if (remainingArgs.length > currentPos + 1
            && remainingArgs[currentPos + 1].toLowerCase().startsWith("generatorname=")) {
          currentPos++; // gobble "algorithm=X"
          String helper = remainingArgs[currentPos].substring(remainingArgs[currentPos]
              .indexOf('=') + 1);
          if (helper.startsWith("\"") && helper.endsWith("\""))
            helper = helper.substring(1, helper.length() - 1);
          nrParams++; // added algorithm name
          pathComponents[4] = helper;
          usesNewGeneratorInterface = true;
          // MessageDisplay.errorMsg("@4, generatorName="+pathComponents[4],
          // MessageDisplay.RUN_ERROR);
        }
        /**
        if (usesNewGeneratorInterface) {
          String[] actualPath = new String[nrParams];
          System.arraycopy(pathComponents, 0, actualPath, 0, nrParams);
          // TreePath tp = new TreePath(actualPath);
          openGenerator();
          generatorDemo.setSelectionPath(actualPath);
          // generatorDemo.valueChanged(new TreeSelectionEvent(this, tp, true,
          // null, null));
        }
		*/
        if (!usesNewGeneratorInterface && remainingArgs.length > currentPos + 2
            && remainingArgs[currentPos + 1].equalsIgnoreCase("-generate")) {
          currentPos += 2; // gobble "-generator" and parameter "topic="
          String key = remainingArgs[currentPos];
          topicMode = key.startsWith("topic=");
          if (topicMode) {
            // String keyVal = key.substring(key.indexOf('=') + 1);
            // if (topicMode) { // is for "topic"
            System.err.println("topicMode is currently not supported, sorry!");
            // generatorType = Integer.valueOf(keyVal).intValue();
            // }
            if (remainingArgs.length > currentPos + 1
                && remainingArgs[currentPos + 1].toLowerCase().startsWith("algorithm=")) {
              currentPos++; // gobble "algorithm=X"
              String localKey = remainingArgs[currentPos].substring(remainingArgs[currentPos]
                  .indexOf('=') + 1);
              if (localKey != null) {
                // generatorClass =
                System.err.println("local key would be "
                    + localKey.substring(localKey.lastIndexOf('.') + 1));
              }
            }
          } else
            currentPos--;
        }
      } else if (currentArgument.equalsIgnoreCase("--cryptool")) {
        PREVENT_EDITING = true;
        CrypToolMode = true;
        if (animalMainWindow != null)
          animalMainWindow.toggleEditingMode(false);
      } else if (currentArgument.startsWith("-schemeWizardPage")) {
        schemeWizardPage = currentArgument.toLowerCase().substring(18);
      } else if (remainingArgs.length >= (2 + currentPos)) {
        if (getAnimalConfiguration().validImportFormat(currentArgument))
          AnimationImporter.importAnimation(remainingArgs[currentPos + 1],
              currentArgument);
        else
          MessageDisplay.addDebugMessage(AnimalTranslator
              .translateMessage("wrongGuessInParamParsing"));
        currentPos++;
        AnimationWindow animWin = AnimalMainWindow.getWindowCoordinator()
            .getAnimationWindow(true);
        animWin.setVisible(true);
        animWin.startOfAnimation();
        explicitlyLoadAnimation = true;
      }
      currentPos++;
    }
    String autoloadLastFileString = getAnimalConfiguration().getProperty(
        "animal.autoloadLastFile", "false");
    autoloadLastFile = autoloadLastFileString.equalsIgnoreCase("true");
    // do we have to load in a previous file?
    if (showGenerator && !usesNewGeneratorInterface) {
      openGenerator();
      if (showOnlyGenerators) {
        // hide all Animal windows
        MessageDisplay.message(AnimalTranslator
            .translateMessage("hideAllCurrentlyOff"));
        // setGlobalWindowVisibility(false);
      }
      if (topicMode) {
        System.err.println("Topic mode is currently not supported, sorry!");
        // int targetIndex = (int) (Math.log(generatorType) / Math.log(2));
        /*
         * generatorGUI.setCategoryIndex(targetIndex); if
         * (generatorGUI.setStateTo(0)) generatorGUI.wiz.showStep(1); else
         * generatorGUI.setCategoryIndex(-1); if (generatorClass != null) {
         * generatorIndex = Arrays.binarySearch(
         * generatorGUI.keysForGeneratorInfos, generatorClass); if
         * (generatorIndex > -1) {
         * generatorGUI.setConcreteGenerator(generatorIndex); if
         * (generatorGUI.setStateTo(1)) generatorGUI.wiz.showStep(2); else
         * generatorGUI.setStateTo(0); } }
         */
      }

    }
    if (!explicitlyLoadAnimation) {
      boolean success = false;
      if (autoloadLastFile)
        success = AnimationImporter.importAnimation(animalConfig
            .getCurrentFilename(), getAnimalConfiguration().getCurrentFormat());
      if (!success)
        newFile();
    }
    if (Animation.get() == null)
      newFile();

    if (schemeWizardPage != "") {
      HtDPTLWizard.instance.next(schemeWizardPage);
      HtDPTLWizard.instance.show();
    }

  }

  /**
   * the standard Java main method. Creates a new Animal object, initializes it
   * and displays its window on the screen.
   * 
   * @param argv
   *          the invocation parameters
   */
  public static void main(String[] argv) {
	  startAnimal(argv);
  }
  
  private static BugReporter bugreporter = null;
  private static void startAnimal(String[] argv){
    File fileBugReportStream = null;
    try { ///// Do BugReportStream /////
      fileBugReportStream = File.createTempFile("BugReportStream"+System.currentTimeMillis(), ".txt");
      FileOutputStream file = new FileOutputStream(fileBugReportStream);
      BugReportStream teeOut = new BugReportStream(file, System.out);
      BugReportStream teeErr = new BugReportStream(file, System.err);
      System.setOut(teeOut);
      System.setErr(teeErr);
    } catch (IOException e) {
      e.printStackTrace();
    }
    setBugreporter(new BugReporter(fileBugReportStream));
    
    LookAndFeelManager.applyNativeLookAndFeel();
      MessageDisplay.initialize(null, true, new XProperties());
      if (Animal.SHOW_PROGRESS_WINDOW && Animal.ProgressPanel == null) {
         Animal.initializeProgressDisplay();
      }
      
      animal = Animal.get();
      animal.initializeFileChooser();
      animal.init();
      if(Animal.ProgressPanel!=null) Animal.ProgressPanel.addTextWithPercentStart(AnimalStartUpProgress.PROGRESS_LABEL_ParseArguments);
      animal.parseArguments(argv);
      if(Animal.ProgressPanel!=null) Animal.ProgressPanel.addTextWithPercentEnd(AnimalStartUpProgress.PROGRESS_LABEL_ParseArguments);
    if(Animal.ProgressPanel!=null) Animal.ProgressPanel.addTextWithPercentStart(AnimalStartUpProgress.PROGRESS_LABEL_FinalStart);
      if (Animation.get() == null)
        animal.newFile();
      if(Animal.ProgressPanel!=null) Animal.ProgressPanel.addTextWithPercentEnd(AnimalStartUpProgress.PROGRESS_LABEL_FinalStart);
    if(Animal.ProgressPanel!=null) Animal.ProgressPanel.addTextWithPercentStart(AnimalStartUpProgress.PROGRESS_LABEL_HideProgressDisplay);
      Animal.hideProgressDisplay();
    if(Animal.ProgressPanel!=null) Animal.ProgressPanel.addTextWithPercentEnd(AnimalStartUpProgress.PROGRESS_LABEL_HideProgressDisplay);
      animalConfig.testIfUpToDate();
      MessageDisplay.message(AnimalTranslator.translateMessage("welcomeGenerators"));
  }
  
  public static void startAnimationFromAnimalScriptCode(String animationContent){
	  startAnimal(null);
	  Animal.get().startAnimationFromAnimalScriptCodeFunc(animationContent);
  }
  
  public static Generator localGenerator = null;
  public static void startGeneratorWindow(Generator generator){
	  localGenerator = generator;
	  startAnimal(null);
	  Animal.get().openGenerator();
	  GeneratorFrame generatorFrame = (GeneratorFrame)generatorDemo.getFrame();
	  SearchLoader.getInstance().setCategorySelected("#Local");
	  generatorFrame.update(SearchLoader.getInstance(), null);
  }

  public static BugReporter getBugReporter() {
    return bugreporter;
  }

  private static void setBugreporter(BugReporter bugreporter) {
    Animal.bugreporter = bugreporter;
  }
  
  private void startAnimationFromAnimalScriptCodeFunc(String animalscriptcode) {
    Animal.get().setAnimalScriptCode(animalscriptcode);
    Animal.get().getScriptInputWindow().doClick_ScriptingInputOK();
  }

  /**
   * zooms all windows in
   */
  public void zoomIn() {
    if (zoomCounter < 6)
      zoomCounter++;
    animalMainWindow.zoom(true);

    if (generatorDemo != null) {
      generatorDemo.zoom(true);
    }
    if (scriptInputWindow != null) {
      scriptInputWindow.zoom(true);
    }

  }

  /**
   * zooms all windows out
   */
  public void zoomOUT() {
    if (zoomCounter > -1)
      zoomCounter--;
    animalMainWindow.zoom(false);

    if (generatorDemo != null) {
      generatorDemo.zoom(false);
    }

    if (scriptInputWindow != null) {
      scriptInputWindow.zoom(false);
    }

  }
  
  public void hideMenu() {
    getMainWindow().getWindowCoordinator().hideMenu();

  }

  // private void setIndividualVisibility(AnimalFrame targetFrame, boolean
  // visibilityMode) {
  // if (targetFrame != null)
  // targetFrame.setVisible(visibilityMode);
  // }
  //
  // private void setGlobalWindowVisibility(boolean visibilityMode) {
  // AnimalFrame currentFrame = null;
  // currentFrame =
  // AnimalMainWindow.getWindowCoordinator().getAnimationOverview(true);
  // setIndividualVisibility(currentFrame, visibilityMode);
  // currentFrame =
  // AnimalMainWindow.getWindowCoordinator().getAnimationWindow(true);
  // setIndividualVisibility(currentFrame, visibilityMode);
  // currentFrame =
  // AnimalMainWindow.getWindowCoordinator().getAnnotationEditor(true);
  // setIndividualVisibility(currentFrame, visibilityMode);
  // currentFrame = AnimalMainWindow.getWindowCoordinator().getDrawWindow(true);
  // setIndividualVisibility(currentFrame, visibilityMode);
  // currentFrame =
  // AnimalMainWindow.getWindowCoordinator().getObjectsWindow(true);
  // setIndividualVisibility(currentFrame, visibilityMode);
  // currentFrame =
  // AnimalMainWindow.getWindowCoordinator().getTimeLineWindow(true);
  // setIndividualVisibility(currentFrame, visibilityMode);
  // }

} // Animals
