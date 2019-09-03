package animal.main;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import javax.swing.filechooser.FileFilter;

import animal.editor.Editor;
import animal.editor.graphics.meta.GraphicEditor;
import animal.exchange.AnimationExporter;
import animal.exchange.AnimationImporter;
import animal.graphics.PTGraphicObject;
import animal.gui.AnimalMainWindow;
import animal.gui.AnimalStartUpProgress;
import animal.gui.DrawCanvas;
import animal.gui.WindowCoordinator;
import animal.handler.GraphicObjectHandler;
import animal.handler.GraphicObjectHandlerExtension;
import animal.misc.AnimalFileChooser;
import animal.misc.MessageDisplay;
import animal.misc.XProperties;
import interactionsupport.controllers.InteractionController;
import interactionsupport.models.backend.AnimalEvalBackend;
import interactionsupport.parser.BadSyntaxException;
import translator.AnimalTranslator;
import translator.ResourceLocator;

/**
 * @author guido
 */
public class AnimalConfiguration {
  public static final String DEFAULT_FILENAME = "demo.aml";

  public static final String DEFAULT_FORMAT = "animation/animal-ascii-compressed";

  public static final long MS_PER_DAY = 1000 // convert to seconds
  * 60 // then to minutes
  * 60 // then to hours
  * 24; // then to days!

  private static AnimalConfiguration defaultConfig;

  private InteractionController interactionController;

  private String currentDirectory;

  private String currentFilename;

  private String currentFormat;

  private Properties config;

  private XProperties xConfig;

  private XProperties animalProperties;

  private Font lastFontUsed = new Font("SansSerif", Font.PLAIN, 20);

  // private XProperties defaultProperties;

  private boolean isInitialized;

  protected boolean editorsInitialized = false;

  private String versionInfo;

  private String versionDate;

  public AnimalFileChooser loadFileChooser;

  public AnimalFileChooser saveFileChooser;
  


  private static int                           zoomCounter             = 0;

  /**
   * This Hashtable contains the mappings of all registered import format
   * strings to concrete import handler objects
   */
  private Hashtable<String, String> registeredImportFormats = null;

  /**
   * This Hashtable contains the mappings of all registered import format
   * extensions to concrete import handler objects
   */
  private Hashtable<String, AnimationImporter> defaultImportExtensions = null;

  private Hashtable<String, Editor> editors = null;

  /**
   * This Hashtable contains the mappings of all registered export format
   * strings to concrete export handler objects
   */
  private Hashtable<String, AnimationExporter> registeredExportFormats = null;

  /**
   * This Hashtable contains the mappings of all registered export format
   * extensions to concrete export handler objects
   */
  private Hashtable<String, AnimationExporter> defaultExportExtensions = null;

  public AnimalConfiguration() {
    loadProperties();
    readComponents();
    try {
      this.interactionController = new InteractionController(new AnimalEvalBackend(), true);
    } catch (BadSyntaxException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void readComponents() {
    config = null;
    try {
      InputStream in = ResourceLocator.getResourceLocator().getResourceStream("/components.dat");
      if (in != null) {
        BufferedInputStream bins = new BufferedInputStream(in);
        config = new Properties();
        config.load(bins);
        bins.close();
        in.close();
      }
    } catch (IOException ioex) {
      System.err.println(ioex.getMessage());
    }

    if (config != null) {
      xConfig = new XProperties(config);
      isInitialized = true;
    }
  }

  public void removeExportFilter(String formatName) {
    registeredExportFormats.remove(formatName);
  }

  public void initializePrimitives() {
    Properties primitiveProps = getEntriesForPrefix("primitive");
    if (editors == null)
      editors = new Hashtable<String, Editor>(23);
    if (primitiveProps != null) {
      Enumeration<Object> primitiveKeys = primitiveProps.keys();
      int cutPosition = "primitive.".length();
      int nr = 0;
      while (primitiveKeys.hasMoreElements()) {
        String key = (String) primitiveKeys.nextElement();
        if (Animal.ProgressPanel != null) {
        	Animal.ProgressPanel.addTextWithPercent("..." + key, Animal.ProgressPanel.getTextPercentRange(AnimalStartUpProgress.PROGRESS_LABEL_InitializePrimitives).getPERCENT_FromStates(primitiveProps.size(), nr));
        }
        //@TODO might insert icons into a grid here!
//        ImageIcon i = Animal.get().getImageIcon(key.substring(cutPosition) +".gif");
//        System.err.println((i == null) +" for " +key);
        insertPrimitive(key.substring(cutPosition), primitiveProps
            .getProperty(key), nr++, getProperties());
      }
    }
  }

  public void initializeAnimators() {
    Properties animatorProps = getEntriesForPrefix("animator");
    if (animatorProps != null) {
      Enumeration<Object> animatorKeys = animatorProps.keys();
      int cutPosition = "animator.".length();
      int nr = 0;
      while (animatorKeys.hasMoreElements()) {
        String key = (String) animatorKeys.nextElement();
        if (Animal.ProgressPanel != null) {
        	Animal.ProgressPanel.addTextWithPercent("..." + key, Animal.ProgressPanel.getTextPercentRange(AnimalStartUpProgress.PROGRESS_LABEL_InitializeAnimators).getPERCENT_FromStates(animatorProps.size(), nr));
        }
        insertAnimator(key.substring(cutPosition), animatorProps
            .getProperty(key), nr++, getProperties());
      }
    }
  }

  public void initializeHandlerExtensions() {
    Properties handlerExtProps = getEntriesForPrefix("handlerExtension");
    if (handlerExtProps != null) {
      Enumeration<Object> handlerExtKeys = handlerExtProps.keys();
      int nr = 0;
      while (handlerExtKeys.hasMoreElements()) {
        String key = (String) handlerExtKeys.nextElement();
        if (Animal.ProgressPanel != null) {
        	Animal.ProgressPanel.addTextWithPercent("..." + key, Animal.ProgressPanel.getTextPercentRange(AnimalStartUpProgress.PROGRESS_LABEL_InitializeHandlerExtensions).getPERCENT_FromStates(handlerExtProps.size(), nr));
        }
        insertHandlerExtension(key.substring(key.lastIndexOf(".") + 1),
            handlerExtProps.getProperty(key));
        nr++;
      }
    }
  }

  public void initializeAllEditors() {
    String resourceName = "i18n/I18NGenericEditor."
        + AnimalTranslator.getTranslator().getCurrentLocale();
    try {
      AnimalTranslator.addResource(resourceName);
    } catch (FileNotFoundException fnfe) {
      System.err.println("Resource not found (ignore for now) " + resourceName);
    }
    
    initializePrimitives();
    initializeAnimators();
    initializeHandlerExtensions();
    initializeSteps();
    editorsInitialized = true;
  }

  public void initializeSteps() {
    Properties animatorProps = getEntriesForPrefix("animationStep");
    if (animatorProps != null) {
      Enumeration<Object> animatorKeys = animatorProps.keys();
      int cutPosition = "animationStep.".length();
      int nr = 0;
      while (animatorKeys.hasMoreElements()) {
        String key = (String) animatorKeys.nextElement();
        if (Animal.ProgressPanel != null) {
        	Animal.ProgressPanel.addTextWithPercent("..." + key, Animal.ProgressPanel.getTextPercentRange(AnimalStartUpProgress.PROGRESS_LABEL_InitializeAnimationStep).getPERCENT_FromStates(animatorProps.size(), nr));
        }
        insertAnimationStep(key.substring(cutPosition), animatorProps
            .getProperty(key), nr++, getProperties());
      }
    }
  }

  @SuppressWarnings("unchecked")
  public void insertPrimitive(String componentName, String handlerName,
      int componentNumber, XProperties props) {
    String resourceName = "i18n/I18N"
        + handlerName.substring(handlerName.lastIndexOf('.') + 3) + "Editor."
        + AnimalTranslator.getTranslator().getCurrentLocale();
    try {
      AnimalTranslator.addResource(resourceName);
    } catch (FileNotFoundException fnfe) {
      System.err.println("Resource not found (ignore for now) " + resourceName);
    }

    String editorBaseName = handlerName.replaceFirst(".graphics.PT",
        ".editor.graphics.");
    String editorName = new StringBuilder(editorBaseName).append("Editor")
        .toString();
    try {
      Class<Editor> c = (Class<Editor>) Class.forName(editorName);
      Editor ed = c.newInstance();
      if ((ed != null) && ed instanceof GraphicEditor) {
        GraphicObjectHandler handler = null;
        PTGraphicObject ptgo = null;

        Class<PTGraphicObject> ptgoClass = (Class<PTGraphicObject>) Class
            .forName(handlerName);
        // if this fails, it is uncritical!
        // FIXME
        try {
          Method targetMethod = ptgoClass.getDeclaredMethod(
              "initializeDefaultProperties", new Class[] { props.getClass() });
          targetMethod.invoke(null, new Object[] { props });
        } catch (NoSuchMethodException e) {
          // MessageDisplay.message("methodNotDeclaredIn", new String[] {
          // componentName, e.getMessage() });
        } catch (InvocationTargetException e) {
          MessageDisplay.message("mthdInvFail", new String[] { componentName,
              e.getMessage() });
        }

        ptgo = ptgoClass.newInstance();
        StringBuilder handlerClassName = new StringBuilder(
            editorName.length() + 10);
        handlerClassName.append("animal.handler.").append(componentName)
            .append("Handler");
        @SuppressWarnings("rawtypes")
        Class handlerClass = Class.forName(handlerClassName.toString());
        handler = (GraphicObjectHandler) handlerClass.newInstance();
        // AnimalTranslator.getResourceBundle().addPropertyResource("demo");

        if (ptgo != null) {
          PTGraphicObject.registeredHandlers.put(ptgo.getType(), handler);

          String[] registered = ptgo.handledKeywords();

          for (int i = 0; i < registered.length; i++) {
            PTGraphicObject.registeredTypes.put(registered[i].toLowerCase(),
                handlerName);
          }
        }
      }

      insertEditor(componentNumber, ed);
    } catch (ClassNotFoundException e) {
      // this should be just a configuration problem,
      // as no non-existing class should be listed
      // in the file. So either the file is corrupt
      // or the class really doesn't exist.
      MessageDisplay.errorMsg("classNotFound", new String[] { editorName,
          e.getMessage() }, MessageDisplay.CONFIG_ERROR);
    } catch (InstantiationException e) {
      MessageDisplay.errorMsg("errorInstantiating", new String[] { editorName,
          e.getMessage() }, MessageDisplay.PROGRAM_ERROR);
    } catch (IllegalAccessException e) {
      // the constructor of a class to be instantiated
      // via <code>newInstance()</code> has to be
      // public!
      MessageDisplay.errorMsg("illegalAccessExc", new String[] { editorName,
          e.getMessage() }, MessageDisplay.PROGRAM_ERROR);
    } catch (IllegalArgumentException e) {
      MessageDisplay.errorMsg("illegalArgumentExc", new String[] { editorName,
          e.getMessage() }, MessageDisplay.PROGRAM_ERROR);
    }
  }

  public void insertEditor(int componentNumber, Editor editor) {
    int compNr = componentNumber;
    if (editor != null) {
      editors.put(editor.getName(), editor);
      if (compNr == 0) {
        compNr = getEditors().size();
      }
      editor.setNum(compNr);
    }
  }

  @SuppressWarnings("unchecked")
  public void insertHandlerExtension(
  String key, String className) {
    try {
      Class<GraphicObjectHandlerExtension> c = (Class<GraphicObjectHandlerExtension>) Class
          .forName(className);
      GraphicObjectHandlerExtension handlerExtension = c.newInstance();
      GraphicObjectHandler.insertHandlerExtension(handlerExtension);
    } catch (ClassNotFoundException e) {
      // this should be just a configuration problem,
      // as no non-existing class should be listed
      // in the file. So either the file is corrupt
      // or the class really doesn't exist.
      MessageDisplay.errorMsg("classNotFound", new String[] { className,
          e.getMessage() }, MessageDisplay.CONFIG_ERROR);
    } catch (InstantiationException e) {
      MessageDisplay.errorMsg("errorInstantiating", new String[] { className,
          e.getMessage() }, MessageDisplay.CONFIG_ERROR);
    } catch (IllegalAccessException e) {
      // the constructor of a class to be instantiated
      // via <code>newInstance()</code> has to be
      // public!
      MessageDisplay.errorMsg("illegalAccessExc", new String[] { className,
          e.getMessage() }, MessageDisplay.CONFIG_ERROR);
    } catch (IllegalArgumentException e) {
      MessageDisplay.errorMsg("illegalArgumentExc", new String[] { className,
          e.getMessage() }, MessageDisplay.CONFIG_ERROR);
    }

  }

  @SuppressWarnings("unchecked")
  public void insertAnimator(
  String key, String componentName, int componentNumber,
      
      XProperties props) {

    String resourceName = "i18n/I18N"
        + componentName.substring(componentName.lastIndexOf('.') + 1)
        + "Editor." + AnimalTranslator.getTranslator().getCurrentLocale();
    try {
      AnimalTranslator.addResource(resourceName);
    } catch (FileNotFoundException fnfe) {
      System.err.println("Resource not found (ignore for now) " + resourceName);
    }

    StringBuilder className = new StringBuilder(58);
    // TODO: substring ab letzter Position von "."
    className.append("animal.editor.animators.").append(
        componentName.substring(componentName.lastIndexOf('.') + 1));
    className.append("Editor");
    try {
      Class<Editor> c = (Class<Editor>) Class.forName(className.toString());
      Editor ed = c.newInstance();
      insertEditor(componentNumber, ed);
    } catch (ClassNotFoundException e) {
      // this should be just a configuration problem,
      // as no non-existing class should be listed
      // in the file. So either the file is corrupt
      // or the class really doesn't exist.
      MessageDisplay.errorMsg("classNotFound", new String[] {
          className.toString(), e.getMessage() }, MessageDisplay.CONFIG_ERROR);
    } catch (InstantiationException e) {
      MessageDisplay.errorMsg("errorInstantiating", new String[] {
          className.toString(), e.getMessage() }, MessageDisplay.CONFIG_ERROR);
    } catch (IllegalAccessException e) {
      // the constructor of a class to be instantiated
      // via <code>newInstance()</code> has to be
      // public!
      MessageDisplay.errorMsg("illegalAccessExc", new String[] {
          className.toString(), e.getMessage() }, MessageDisplay.CONFIG_ERROR);
    } catch (IllegalArgumentException e) {
      MessageDisplay.errorMsg("illegalArgumentExc", new String[] {
          className.toString(), e.getMessage() }, MessageDisplay.CONFIG_ERROR);
    }
  }

  @SuppressWarnings("unchecked")
  public void insertAnimationStep(
  String key, String componentName, int componentNumber,
      
      XProperties props) {
    String resourceName = "i18n/I18N"
        + componentName.substring("animal.main.".length()) + "Editor."
        + AnimalTranslator.getTranslator().getCurrentLocale();
    try {
      AnimalTranslator.addResource(resourceName);
    } catch (FileNotFoundException fnfe) {
      System.err.println("Resource not found (ignore for now) " + resourceName);
    }
    StringBuilder className = new StringBuilder(53);
    className.append("animal.editor.");
    className.append(componentName.substring("animal.main.".length()));
    className.append("Editor");
    try {
      Class<Editor> c = (Class<Editor>) Class.forName(className.toString());
      Editor ed = c.newInstance();
      insertEditor(componentNumber, ed);
    } catch (ClassNotFoundException e) {
      // this should be just a configuration problem,
      // as no non-existing class should be listed
      // in the file. So either the file is corrupt
      // or the class really doesn't exist.
      MessageDisplay.errorMsg("classNotFound", new String[] {
          className.toString(), e.getMessage() }, MessageDisplay.CONFIG_ERROR);
    } catch (InstantiationException e) {
      MessageDisplay.errorMsg("errorInstantiating", new String[] {
          className.toString(), e.getMessage() }, MessageDisplay.CONFIG_ERROR);
    } catch (IllegalAccessException e) {
      // the constructor of a class to be instantiated
      // via <code>newInstance()</code> has to be
      // public!
      MessageDisplay.errorMsg("illegalAccessExc", new String[] {
          className.toString(), e.getMessage() }, MessageDisplay.CONFIG_ERROR);
    } catch (IllegalArgumentException e) {
      MessageDisplay.errorMsg("illegalArgumentExc", new String[] {
          className.toString(), e.getMessage() }, MessageDisplay.CONFIG_ERROR);
    }
  }
  /************************************************************/
  /***update by Lu,Zheng***************************************/
  /************************************************************/
  public void insertXProperties(String key,String value){
	  xConfig.put(key, value);
  }
  /************************************************************/
  public Properties getConfiguration() {
    return config;
  }

  public Properties getEntriesForPrefix(String prefixCode) {
    if (xConfig == null) {
      loadProperties();
      readComponents();
//      return new Properties();
    }
    String[] keys = xConfig.getKeys(prefixCode);
    if (keys == null)
      return new Properties();
    Properties prefixProps = new Properties();
    int nrKeys = keys.length;
    for (int i = 0; i < nrKeys; i++)
      prefixProps.put(keys[i], xConfig.getProperty(keys[i]));
    return prefixProps;
  }

  public boolean isInitialized() {
    return isInitialized;
  }

  public Hashtable<String, Editor> getEditors() {
    return editors;
  }

  public static AnimalConfiguration getDefaultConfiguration() {
    if (defaultConfig == null || !defaultConfig.isInitialized()) {
      defaultConfig = new AnimalConfiguration();
      defaultConfig.readComponents();
      // TODO check if this is legal...
      // defaultConfig.initializeAllEditors();
    }
    return defaultConfig;
  }

  public AnimalFileChooser getImportFileChooser() {
    if (loadFileChooser == null) {
      initializeImportFormats();
    }
    return loadFileChooser;
  }

  public void initializeImportFormats() {
    Properties handlers = getEntriesForPrefix("import");
    if (registeredImportFormats == null) {
      registeredImportFormats = new Hashtable<String, String>(29);
    }

    if (defaultImportExtensions == null) {
      defaultImportExtensions = new Hashtable<String, AnimationImporter>(29);
    }
    loadFileChooser = new AnimalFileChooser(this);
    if (zoomCounter > 0) {
      for (int i = 0; i < zoomCounter; i++) {
        loadFileChooser.zoom(true);
      }
    } else {
      for (int i = 0; i > zoomCounter; i--) {
        loadFileChooser.zoom(false);
      }
    }
    loadFileChooser.resetFilters();
    loadFileChooser.chooser.removeChoosableFileFilter(loadFileChooser.chooser
        .getAcceptAllFileFilter());

    if (handlers != null) {
      Enumeration<Object> importKeys = handlers.keys();
      Vector<String> formats = new Vector<String>(37);
      int cutPosition = "import.".length();
      while (importKeys.hasMoreElements()) {
        String key = (String) importKeys.nextElement();
        String formatKey = key.toLowerCase().substring(cutPosition);
        insertImportFilter(formatKey, handlers.getProperty(key));
        formats.add(formatKey);
      }
      String[] supportedFormats = new String[formats.size()];
      formats.copyInto(supportedFormats);

      FileFilter[] filters = loadFileChooser.chooser.getChoosableFileFilters();
      if (filters != null && filters.length > 0)
        loadFileChooser.chooser.setFileFilter(filters[0]);
//      loadFileChooser.chooser.setFileFilter(loadFileChooser.chooser
//          .getChoosableFileFilters()[0]);
    }
  }

  public void initializeExportFormats() {
    Properties handlers = getEntriesForPrefix("export");
    if (registeredExportFormats == null) {
      registeredExportFormats = new Hashtable<String, AnimationExporter>(29);
    }

    if (defaultExportExtensions == null) {
      defaultExportExtensions = new Hashtable<String, AnimationExporter>(29);
    }
    saveFileChooser = new AnimalFileChooser(this);
    if (zoomCounter > 0) {
      for (int i = 0; i < zoomCounter; i++) {
        saveFileChooser.zoom(true);
      }
    } else {
      for (int i = 0; i > zoomCounter; i--) {
        saveFileChooser.zoom(false);
      }
    }
    saveFileChooser.resetFilters();
    saveFileChooser.chooser.removeChoosableFileFilter(loadFileChooser.chooser
        .getAcceptAllFileFilter());

    if (handlers != null) {
      Enumeration<Object> exportKeys = handlers.keys();
      Vector<String> formats = new Vector<String>(37);
      int cutPosition = "export.".length();
      while (exportKeys.hasMoreElements()) {
        String key = (String) exportKeys.nextElement();
        String formatKey = key.toLowerCase().substring(cutPosition);
        insertExportFilter(key.toLowerCase().substring(cutPosition), handlers
            .getProperty(key));
        formats.add(formatKey);
      }
      String[] supportedFormats = new String[formats.size()];
      formats.copyInto(supportedFormats);
    }
    saveFileChooser.chooser.setFileFilter(saveFileChooser.chooser
        .getChoosableFileFilters()[0]);
    AnimationExporter.animalConfig = this;
  }

  @SuppressWarnings("unchecked")
  public void insertImportFilter(String formatName, String handlerName) {
    AnimationImporter handler = null;

    try {
      Class<AnimationImporter> c = (Class<AnimationImporter>) Class
          .forName(handlerName);
      handler = c.newInstance();

      if (handler != null) {
        handler.init(formatName);
        loadFileChooser.addFilter(handler.getDefaultExtension(), formatName);
        registeredImportFormats.put(formatName, handlerName);
        defaultImportExtensions.put(handler.getDefaultExtension(), handler);
      }
    } catch (Exception e) {
      MessageDisplay.errorMsg("missingOrImproperImporter", new String[] {
          handlerName, formatName, e.getMessage() }, MessageDisplay.RUN_ERROR);
    }
  }

  @SuppressWarnings("unchecked")
  public void insertExportFilter(String formatName, String handlerName) {
    AnimationExporter handler = null;
    AnimationExporter result = null;
    try {
      Class<AnimationExporter> c = (Class<AnimationExporter>) Class
          .forName(handlerName);
      result = c.newInstance();
    } catch (Exception e) {
      MessageDisplay.errorMsg("missingOrImproperImporter", new String[] {
          handlerName, formatName, e.getMessage() }, MessageDisplay.RUN_ERROR);
    }
    if (result != null)
      handler = result;
    if (handler != null) {
      handler.init(formatName);
      saveFileChooser.addFilter(handler.getDefaultExtension(), formatName);
      registeredExportFormats.put(formatName, handler);
    }
  }

  public void setInitialized(boolean status) {
    isInitialized = status;
  }

  /**
   * returns the current directory
   * 
   * @return the current directory
   */
  public String getCurrentDirectory() {
    return currentDirectory;
  }

  /**
   * sets the current directory to dir
   * 
   * @param dir
   *          the new directory
   */
  public void setCurrentDirectory(String dir) {
    if (dir != null && dir.length() >= 1)
      currentDirectory = dir;
    else
      currentDirectory = ".";
  }

  /**
   * returns the current filename
   * 
   * @return the current filename
   */
  public String getCurrentFilename() {
    return currentFilename;
  }

  /**
   * sets the current filename to filename
   * 
   * @param filename
   *          the new filename
   */
  public void setCurrentFilename(String filename) {
    if (filename != null && filename.length() >= 1)
      currentFilename = filename;
    else
      currentFilename = DEFAULT_FILENAME;
  }

  /**
   * returns the current format
   * 
   * @return the current format
   */
  public String getCurrentFormat() {
    return currentFormat;
  }

  /**
   * sets the current format to format
   * 
   * @param format
   *          the new format
   */
  public void setCurrentFormat(String format) {
    if (format != null && format.length() >= 1)
      currentFormat = format;
    else
      currentFormat = DEFAULT_FORMAT;
  }

  /**
   * loads the properties from the property file and set the properties of
   * Animal(i.e. which windows are visible, how to handle and where to find
   * files etc.
   * <p>
   * This does not set the properties of the other windows, but just sets the
   * <i>props</i> variable to which other objects can refer by calling
   * <code>Animal.getProperties()</code> when they need to know their
   * properties. For the reason of this, cf. the written documentation
   */
  private boolean loadProperties() {
    InputStream is = null;
    Properties props = new Properties();
    try {
      // at first, load the default properties, i.e. the properties
      // that are used, when no animal.properties file exists.
      // By this, all properties can be reset to their default values
      // by simply deleting "default.properties"

      is = ResourceLocator.getResourceLocator().getResourceStream("/default.properties");
      if (is != null) {
        props.load(is);
      }
    } catch (IOException ioExc) {
      MessageDisplay.errorMsg("couldNotLoadConfig", "default.properties",
          MessageDisplay.INFO);
      return false;
    }

    try {
      
      File f = new File("animal.properties");
      if (f.isFile() && f.canRead()) {
     
          is = new FileInputStream(f);
         
       
      }else {
        is = ResourceLocator.getResourceLocator().getResourceStream("animal.properties");
      }
     
      
       
      if (is != null) {
        props.load(new BufferedInputStream(is));
      }
    } catch (IOException e) {
      MessageDisplay.errorMsg("couldNotLoadConfig", "animal.properties",
          MessageDisplay.INFO);
      return false;
    }
    animalProperties = new XProperties(props);
//    animalProperties.addAllElements(props);

    setCurrentDirectory(animalProperties.getProperty("animal.defaultDirectory"));
    setCurrentFormat(animalProperties.getProperty("animal.fileFormat"));
    setCurrentFilename(animalProperties.getProperty("animal.filename"));

    return true;
  }

  /**
   * returns the XProperties object. Unlike all other getProperties methods this
   * method does not return any single properties.
   */
  public XProperties getProperties() {
    if (animalProperties == null)
      loadProperties();

    return animalProperties;
  }

  public Color getDefaultColor(String primitiveName, String key) {
    return getDefaultColor(primitiveName + "." + key, Color.BLACK);
  }

  public Color getDefaultColor(String primitiveName, String key,
      Color defaultColor) {
    return getDefaultColor(primitiveName + "." + key, defaultColor);
  }

  public Color getDefaultColor(String accessKey) {
    return getDefaultColor(accessKey, Color.BLACK);
  }

  public Color getDefaultColor(String accessKey, Color defaultColor) {
    return animalProperties.getColorProperty(accessKey, defaultColor);
  }

  public boolean getDefaultBooleanValue(String primitiveName, String key) {
    return getDefaultBooleanValue(primitiveName + "." + key, false);
  }

  public boolean getDefaultBooleanValue(String primitiveName, String key,
      boolean defaultValue) {
    return getDefaultBooleanValue(primitiveName + "." + key, defaultValue);
  }

  public boolean getDefaultBooleanValue(String accessKey) {
    return animalProperties.getBoolProperty(accessKey, false);
  }

  public boolean getDefaultBooleanValue(String accessKey, boolean defaultValue) {
    return animalProperties.getBoolProperty(accessKey, defaultValue);
  }

  public Font getDefaultFontValue(String accessKey) {
    return getDefaultFontValue(accessKey, lastFontUsed);
  }

  public Font getDefaultFontValue(String accessKey, Font defaultFont) {
    lastFontUsed = animalProperties.getFontProperty(accessKey, defaultFont);
    return lastFontUsed;
  }

  public Font getDefaultFontValue(String primitiveName, String key) {
    return getDefaultFontValue(primitiveName + "." + key, lastFontUsed);
  }

  public Font getDefaultFontValue(String primitiveName, String key,
      Font defaultValue) {
    return getDefaultFontValue(primitiveName + "." + key, defaultValue);
  }

  public int getDefaultIntValue(String primitiveName, String key) {
    return getDefaultIntValue(primitiveName + "." + key, 1);
  }

  public int getDefaultIntValue(String primitiveName, String key,
      int defaultValue) {
    return getDefaultIntValue(primitiveName + "." + key, defaultValue);
  }

  public int getDefaultIntValue(String accessKey) {
    return animalProperties.getIntProperty(accessKey, 1);
  }

  public int getDefaultIntValue(String accessKey, int defaultValue) {
    return animalProperties.getIntProperty(accessKey, defaultValue);
  }

  public String getProperty(String key) {
    return getProperty(key, null);
  }

  public String getProperty(String key, String fallBackValue) {
    return getProperties().getProperty(key, fallBackValue);
  }

  public String getVersionNumber() {
    if (versionInfo == null) {
      StringBuilder versionInfoBuffer = new StringBuilder(12);
      versionInfoBuffer.append(
          AnimalTranslator.translateMessage("version.major")).append('.');
      versionInfoBuffer.append(
          AnimalTranslator.translateMessage("version.minor")).append('.');
      versionInfoBuffer.append(AnimalTranslator
          .translateMessage("version.micro"));
      versionInfo = versionInfoBuffer.toString();
      versionInfoBuffer = null;
    }
    return versionInfo;
  }

  public String getVersionDate() {
    if (versionDate == null) {
      int year = new Integer(AnimalTranslator.translateMessage("version.year"))
          .intValue();
      int month = new Integer(AnimalTranslator
          .translateMessage("version.month")).intValue();
      int day = new Integer(AnimalTranslator.translateMessage("version.day"))
          .intValue();
      StringBuilder versionDateBuffer = new StringBuilder(20);
      versionDateBuffer.append(year).append('-').append(month);
      versionDateBuffer.append('-').append(day);
      versionDate = versionDateBuffer.toString();
      versionDateBuffer = null;
    }
    return versionDate;
  }

  public String getVersionLine() {
    return getVersionLine("versionInfoLine");
  }

  public String getVersionLine(String key) {
    String day = AnimalTranslator.translateMessage("version.day");
    String month = AnimalTranslator.translateMessage("version.month");
    String year = AnimalTranslator.translateMessage("version.year");
    String major = AnimalTranslator.translateMessage("version.major");
    String minor = AnimalTranslator.translateMessage("version.minor");
    String micro = AnimalTranslator.translateMessage("version.micro");
    return AnimalTranslator.translateMessage(key, new String[] { major, minor,
        micro, day, month, year });
  }

  public WindowCoordinator getWindowCoordinator() {
    return AnimalMainWindow.WINDOW_COORDINATOR;
  }

  /**
   * Test whether the given export extension is valid.
   * 
   * @param extension
   *          the target export extension
   * @return true if the extension is registered
   */
  public boolean validExportExtension(String extension) {
    return defaultExportExtensions.containsKey(extension.toLowerCase());
  }

  /**
   * Test whether the given export format is valid.
   * 
   * @param formatName
   *          the target export format name
   * @return true if the format is registered
   */
  public boolean validExportFormat(String formatName) {
    return registeredExportFormats.containsKey(formatName.toLowerCase());
  }

  /**
   * Test whether the given import extension is valid.
   * 
   * @param extension
   *          the target import extension
   * @return true if the extension is registered
   */
  public boolean validImportExtension(String extension) {
    return defaultImportExtensions.containsKey(extension.toLowerCase());
  }

  /**
   * Test whether the given import format is valid.
   * 
   * @param formatName
   *          the target import format name
   * @return true if the format is registered
   */
  public boolean validImportFormat(String formatName) {
    return registeredImportFormats.containsKey(formatName.toLowerCase());
  }

  /**
   * returns an array of all currently known export filename extensions
   * 
   * @return the set of current port filename extensions
   */
  public String[] getExportExtensions() {
    String[] extensions = new String[defaultExportExtensions.size()];
    Enumeration<String> extensionKeys = defaultExportExtensions.keys();
    int pos = 0;
    while (extensionKeys.hasMoreElements()) {
      String format = extensionKeys.nextElement();
      extensions[pos++] = format;
    }
    return extensions;
  }

  public AnimationExporter getExporterForExtension(String extensionName) {
    return defaultExportExtensions.get(extensionName);
  }

  /**
   * returns an array of all currently known export formats
   * 
   * @return the set of current export formats
   */
  public String[] getExportFormats() {
    String[] formatNames = new String[registeredExportFormats.size()];
    Enumeration<String> exportFormats = registeredExportFormats.keys();
    int pos = 0;
    while (exportFormats.hasMoreElements()) {
      String format = exportFormats.nextElement();
      formatNames[pos++] = format;
    }
    return formatNames;
  }

  /**
   * returns an array of all currently known import filename extensions
   * 
   * @return the set of current import filename extensions
   */
  public String[] getImportExtensions() {
    String[] extensions = new String[defaultImportExtensions.size()];
    Enumeration<String> extensionKeys = defaultImportExtensions.keys();
    int pos = 0;
    while (extensionKeys.hasMoreElements()) {
      String format = extensionKeys.nextElement();
      extensions[pos++] = format;
    }
    return extensions;
  }

  public AnimationImporter getImporterForExtension(String extensionName) {
    return defaultImportExtensions.get(extensionName);
  }

  /**
   * returns an array of all currently known import formats
   * 
   * @return the set of current import formats
   */
  public String[] getImportFormats() {
    String[] formatNames = new String[registeredImportFormats.size()];
    Enumeration<String> importFormats = registeredImportFormats.keys();
    int pos = 0;
    while (importFormats.hasMoreElements()) {
      String format = importFormats.nextElement();
      formatNames[pos++] = format;
    }
    return formatNames;
  }

  /**
   * Return the name of the handler for the given export format
   * 
   * @param formatName
   *          the target export format name
   * @return the name of the registered export handler, if any; else null.
   */
  public AnimationExporter getExportHandlerFor(String formatName) {
    if (validExportFormat(formatName)) {
      return registeredExportFormats.get(formatName.toLowerCase());
    }
    MessageDisplay.errorMsg("unknownFormat", formatName,
        MessageDisplay.RUN_ERROR);

    return null;
  }

  /**
   * Return the name of the handler for the given import format
   * 
   * @param formatName
   *          the target import format name
   * @return the name of the registered import handler, if any; else null.
   */
  public String getImportHandlerFor(String formatName) {
    if (validImportFormat(formatName)) {
      return registeredImportFormats.get(formatName.toLowerCase());
    }
    MessageDisplay.errorMsg("unknownFormat", formatName,
        MessageDisplay.RUN_ERROR);

    return null;
  }

  /**
   * Initialize Animal's display of windows to the values last set in the
   * properties
   */
  public void initDisplay() {
    // TODO: re-embed in Animal!
    // loadFile/newFile must be called before initializing the windows
    // as some of these windows need an Animation.
    // if not autoload or load fails, create a new animation, as an
    // animation must exist!
    /*
     * if (!autoloadLastFile || !AnimationImporter
     * .importAnimation(getCurrentDirectory(), getCurrentFormat())) { newFile(); }
     */
    WindowCoordinator wCoord = AnimalMainWindow.getWindowCoordinator();
    // drawWindowShown = props.getBoolProperty("animal.DrawWindowVisible");
    if (getProperties().getBoolProperty("animal.DrawWindowVisible")) {
      wCoord.getDrawWindow(true).setVisible(true);
    }

    if (getProperties().getBoolProperty("animal.AnimationWindowVisible")) {
      wCoord.getAnimationWindow(true).setVisible(true);
    }

    if (getProperties().getBoolProperty("animal.AnimationOverviewVisible")) {
      wCoord.getAnimationOverview(true).setVisible(true);
    }

    if (getProperties().getBoolProperty("animal.TimeLineWindowVisible")) {
      wCoord.getTimeLineWindow(true).setVisible(true);
    }

    if (getProperties().getBoolProperty("animal.ObjectsWindowVisible")) {
      wCoord.getObjectsWindow(true).setVisible(true);
    }

    if (getProperties().getBoolProperty("animal.AnnotationEditorVisible")) {
      wCoord.getAnnotationEditor(true).setVisible(true);
    }
  }

  public void setMouseType(int draw, int finish, int cancel) {
//    AnimalMainWindow.getWindowCoordinator().getDrawWindow(false)
//        .getDrawCanvas().setMouseType(draw, finish, cancel);
    DrawCanvas.setMouseType(draw, finish, cancel);
  }

  public boolean testIfUpToDate() {
    GregorianCalendar now = new GregorianCalendar();
    int year = new Integer(AnimalTranslator.translateMessage("version.year"))
        .intValue();
    int month = new Integer(AnimalTranslator.translateMessage("version.month"))
        .intValue();
    int day = new Integer(AnimalTranslator.translateMessage("version.day"))
        .intValue();
    StringBuilder localVersionInfo = new StringBuilder(16);
    GregorianCalendar builtDate = new GregorianCalendar(year, month - 1, day);
    long difference = now.getTimeInMillis() - builtDate.getTimeInMillis();
    long nrDays = difference / MS_PER_DAY;
    long nrMonths = nrDays / 30;
    localVersionInfo.append(AnimalTranslator.translateMessage("version.major"))
        .append('.');
    localVersionInfo.append(AnimalTranslator.translateMessage("version.minor"))
        .append('.');
    localVersionInfo.append(AnimalTranslator.translateMessage("version.micro"));
    if (nrMonths >= 6) {
      String message = AnimalTranslator.translateMessage("checkForNewVersion",
          new String[] { localVersionInfo.toString(), String.valueOf(year),
              String.valueOf(month), String.valueOf(day),
              String.valueOf(now.get(Calendar.YEAR)),
              String.valueOf(now.get(Calendar.MONTH) + 1),
              String.valueOf(now.get(Calendar.DAY_OF_MONTH)),
              String.valueOf(nrDays), String.valueOf(nrMonths) });
      MessageDisplay.message(message);
    }
    return true;
  }

  public InteractionController getInteractionController() {
    return interactionController;
  }

  public void setInteractionController(InteractionController interactionController) {
    this.interactionController = interactionController;
  }



  /**
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public void zoom(boolean zoomIn) {

    if (zoomIn) {
      if (zoomCounter < 6)
        zoomCounter++;

    } else {
      if (zoomCounter > -1)
        zoomCounter--;
    }



    if (loadFileChooser != null) {
      loadFileChooser.zoom(zoomIn);
    }

    if (saveFileChooser != null) {
      saveFileChooser.zoom(zoomIn);
    }
  }
}