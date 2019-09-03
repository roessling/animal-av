/*
 * Created on 24.11.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package animal.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import animal.exchange.AnimationImporter;
import animal.main.Animal;
import animal.main.AnimalConfiguration;
import animal.main.AnimalFrame;
import animal.misc.ComponentConfigurer;
import animal.misc.MessageDisplay;
import animal.misc.XProperties;
import translator.AnimalTranslator;
import translator.ExtendedAction;
import translator.ResourceLocator;

/**
 * @author guido
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class AnimalMainWindow extends AnimalFrame implements WindowListener {
	private static JTextArea outputArea;

	private JPanel pdfPanel;
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 4339204989666063741L;

	/**
	 * the (static) component for coordinating the "main" windows
	 */
	public static WindowCoordinator WINDOW_COORDINATOR;

	private Animal animalInstance;

	protected Vector<String> languages = null;

  private int                              zoomCounter                = 0;

	/**
	 * This Hashtable holds the references to the registered action objects. It is
	 * used -- among other things which may be part of future extensions -- for
	 * causing the elements to be translated whenever a new locale is chosen.
	 */
	public Hashtable<String, ExtendedAction> actions = new Hashtable<String, ExtendedAction>(
			47);

	private JMenu languageMenu = null;

	private static final String DEFAULT_LANGUAGES_FILENAME = "languages.default";

	private static final String LANGUAGES_FILENAME = "languages.dat";

	private static ResourceLocator resourceLocator = ResourceLocator
			.getResourceLocator();

	private static JToolBar toolBar = null;
	private static MainMenuBar menuBar = null;

	/**
	 * creates a new AnimalMainWindow instance based on the parameters
	 * 
	 * @param animalParam
	 *          the current Animal instance
	 * @param propsParam
	 *          the current Animal properties
	 * @param appletMode
	 *          if true, the system runs as an applet
	 */
	public AnimalMainWindow(Animal animalParam, XProperties propsParam,
			boolean appletMode, boolean debugMode) {
		super(animalParam, propsParam, appletMode);
		// ensure "animal" is set!

		if (animalParam == null)
			animalInstance = Animal.get();
		else
			animalInstance = animalParam;

		// create and store the local window coordinator
		WINDOW_COORDINATOR = new WindowCoordinator(animalInstance, this);

		// set the locale to USA
		setAnimalLocale(Animal.getCurrentLocale());// Locale.US);

		String versionInfo = AnimalConfiguration.getDefaultConfiguration()
				.getVersionLine("briefVersionInfoLine");

		// set the main window title
		setTitle(AnimalTranslator.getTranslator().translateMessage("cc", versionInfo));
		    //"Animal Control Center [" + versionInfo + "]");

		// generate the output area
		outputArea = new JTextArea();

		JScrollPane outputAreaScrollPane = new JScrollPane(outputArea);
		// ... and add it to the window's center with a ScrollPane
		workContainer().add(BorderLayout.SOUTH, outputAreaScrollPane);

		// make sure users cannot write on it!
		outputArea.setEditable(false);

		MessageDisplay.initialize(outputArea, debugMode, props);

		// initialize the application toolBar
		toolBar = new MainToolBar("Animal", animalInstance);

		// add the toolBar to the window
		workContainer().add(BorderLayout.NORTH, toolBar);

		// create the window's menu bar
		menuBar = new MainMenuBar("menuBar", this, animalInstance);

		// now install the menuBar on the window
		setJMenuBar(menuBar);

		ensureSensibleBounds(props);
		addWindowListener(this);

		outputAreaScrollPane.setBorder(new TitledBorder(BorderFactory.createLoweredBevelBorder(), "Console", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, null, new Color(0, 0, 0)));
//		outputAreaScrollPane.setBackground(Color.WHITE);
    outputAreaScrollPane.setMinimumSize(new Dimension(700, 120));
    outputAreaScrollPane.setPreferredSize(new Dimension(700, 120));

    setPreferredSize(new Dimension(1200, 700));
    setMinimumSize(new Dimension(1200, 700));
    if (!this.getWindowCoordinator().animalToPDFWindowVisible()) {
      this.getWindowCoordinator().getAnimalToPDFWindow(true);
      hookPDFMenu();
          
    
    }else {
      this.getWindowCoordinator().getAnimalToPDFWindow(false);
      hookPDFMenu();
    }
	}
	
	public void hookPDFMenu() {
    pdfPanel = this.getWindowCoordinator().getAnimalToPDFWindow(false).getAnimaltoPDFWindowView().getMainPanel();
    Dimension dimPanel = new Dimension(325, 700);
    pdfPanel.setSize(dimPanel);
    pdfPanel.setPreferredSize(dimPanel);
    pdfPanel.setMaximumSize(dimPanel);
    pdfPanel.setMinimumSize(dimPanel);

    workContainer().add(BorderLayout.LINE_END,
        pdfPanel);
    this.getWindowCoordinator().getAnimalToPDFWindow(false).getAnimaltoPDFWindowView().setVisible(false);
  }

  public JPanel getPDFPanel() {
    return this.pdfPanel;

  }

	/**
	 * ensures that "sensible" bounds as used
	 * 
	 * @param propsParam
	 *          the properties that specify the bounds. Values will be overridden
	 *          if the window position is awkward, e.g. x, y &lt; 0, width &lt;
	 *          480, height &lt; 320.
	 */
	public void ensureSensibleBounds(XProperties propsParam) {
		int x, y, w, h;
		x = propsParam.getIntProperty("animal.x", 100);
		if (x < 0)
			x = 10;
		y = propsParam.getIntProperty("animal.y", 100);
		if (y < 0)
			y = 10;
		w = propsParam.getIntProperty("animal.width", 480);
		if (w < 200)
			w = 480;
		h = propsParam.getIntProperty("animal.height", 320);
		if (h < 50)
			h = 320;

		// finally, assign the bounds!
		setBounds(x, y, w, h);
	}

	/**
	 * generates the language menu "from scratch"
	 */
	public void generateLanguageMenu() {
		String baseCode = null;
		XProperties languageProperties = retrieveLanguageDefinitions();
		String[] entries = languageProperties.getKeysWithSuffix(".label");
		int nrElems = entries.length;
		if (languages == null)
			languages = new Vector<String>(20);
		languages.removeAllElements();
		for (int i = 0; i < nrElems; i++) {
			baseCode = entries[i].substring(0, entries[i].indexOf('.'));
			String localeString = languageProperties
					.getProperty(baseCode + ".locale");
			addLanguageSupportEntry(localeString, languageProperties
					.getProperty(baseCode + ".label"), languageProperties
					.getProperty(baseCode + ".icon"), languageProperties
					.getProperty(baseCode + ".toolTipText"));
		}
	}

	/**
	 * provides access to the output area for messages of all kinds
	 * 
	 * @return the output area on which text may be posted
	 */
	public static JTextArea getOutputArea() {
		return outputArea;
	}

	public static void changeFontSize(int size) {
	  if (size >= 8 && size <= 100) {
	    Font f = outputArea.getFont();
	    Font newFont = new Font(f.getFontName(), Font.PLAIN, size);
      outputArea.setFont(newFont);
	  }
	}
	
 	public static JToolBar getToolBar() {
		return toolBar;
	}

	public static JMenuBar menuBar() {
		return menuBar;
	}

	/**
	 * Adds a new language entry
	 * 
	 * @param localeString
	 *          the key for the locale, e.g. "en_US"
	 * @param label
	 *          the label to use, e.g. "English"
	 * @param iconName
	 *          the name of the icon to display
	 * @param toolTipText
	 *          the tool tip text to display
	 */
	public void addLanguageSupportEntry(String localeString, String label,
			String iconName, String toolTipText) {
	  String theTooltip = toolTipText;
		StringTokenizer stok = new StringTokenizer(localeString, " _");
		String baseCode = stok.nextToken();
		Locale targetLocale = new Locale(baseCode, stok.nextToken());
		if (theTooltip == null)
		  theTooltip = label;
		String languageKey = label + ";" + localeString + ";" + iconName + ";"
				+ theTooltip;
		if (!languages.contains(languageKey))
			languages.addElement(languageKey);
		ExtendedAction helper = new ExtendedAction(label, iconName, theTooltip,
				"setAnimalLocale", this, new Object[] { targetLocale },
				AnimalTranslator.getTranslator());
		actions.put(localeString, helper);
		if (languageMenu == null)
			languageMenu = AnimalTranslator.getGUIBuilder().generateJMenu("locale",
					null);
		languageMenu.add(helper);
	}

	/**
	 * updates the language menu by recreating all entries
	 * 
	 */
	public void updateLanguageMenu() {
		languageMenu.removeAll();
		generateLanguageMenu();
		// if (languages != null)
		// for (int i = 0; i < languages.size(); i++) {
		// languageMenu.add((Action) languages.elementAt(i));
		// }
	}

	/**
	 * removes the chosen language support
	 * 
	 * @param languageKey
	 *          the key of the language support to remove
	 */
	public void removeLanguageSupportEntry(String languageKey) {
		if (actions.containsKey(languageKey)) {
			languages.removeElement(actions.get(languageKey));
			actions.remove(languageKey);
		}
		updateLanguageMenu();
	}

	public void toggleEditingMode(boolean allowsEditing) {
	  if (menuBar != null)
	    ((MainMenuBar)menuBar).allowEditing(allowsEditing);
	}
	/**
	 * add the given language, if not already is supported
	 * 
	 * @param languageKey
	 *          the key for the "new" language
	 */
	public void addLanguage(String languageKey) {
		if (!languages.contains(languageKey))
			languages.addElement(languageKey);
	}

	/**
	 * checks if this language is supported
	 * 
	 * @param languageKey
	 *          the key for the chosen language
	 * @return true if the language is currently supported
	 */
	public boolean supportsLanguage(String languageKey) {
		return languages.contains(languageKey);
	}

	/**
	 * retrieve the array of language keys
	 * 
	 * @return the array of language keys
	 */
	public String[] getLanguageKeys() {
		// if the languages is still null, generate it!
		if (languages == null)
			languages = new Vector<String>(20);

		// if no languages are known, provide an appropriate entry!
		if (languages.size() == 0)
			languages
					.addElement("English;en US;en_US.gif;Translate system into English");

		// store the number of elements to prevent unnecessary lookups
		int nrElems = languages.size();

		// allocate the result String array
		String[] keys = new String[nrElems];

		// iterate all entries...
		for (int i = 0; i < nrElems; i++) {
			// ... to generate and appropriate entry
			keys[i] = languages.elementAt(i);
		}

		// finally, return the keys
		return keys;
	}

	/**
	 * retrieve the list of supported languages from disk
	 * 
	 * @return a XProperties object encapsulating the language information
	 */
	public XProperties retrieveLanguageDefinitions() {
		Properties localProps = null;
		try {
			Properties defaultProps = new Properties();
			InputStream is = resourceLocator.getResourceStream(
					DEFAULT_LANGUAGES_FILENAME, runsInApplet, baseURL);
			if (is != null) {
				defaultProps.load(new BufferedInputStream(is));
				is.close();
			}
			localProps = new XProperties(defaultProps);
			is = resourceLocator.getResourceStream(LANGUAGES_FILENAME, runsInApplet,
					baseURL);
			if (is != null) {
				localProps.load(new BufferedInputStream(is));
				is.close();
			}
		} catch (IOException e) {
			MessageDisplay.errorMsg("langSupportKeyLoadFailed",
					new String[] { DEFAULT_LANGUAGES_FILENAME },
					MessageDisplay.CONFIG_ERROR);
		}
		return new XProperties(localProps);
	}

	/**
	 * set the current locale to "targetLocale"
	 * 
	 * @param targetLocale
	 *          the chosen locale, e.g. Locale.US
	 */
	public void setAnimalLocale(Locale targetLocale) {
		AnimalTranslator.setTranslatorLocale(targetLocale);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	public void windowActivated(WindowEvent arg0) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	public void windowClosed(WindowEvent arg0) {
		MessageDisplay.message(AnimalTranslator.translateMessage("thanksAndBye",
				AnimalConfiguration.getDefaultConfiguration().getVersionLine(
						"versionInfoLine")));
		animal.requestAnimationSave();
		// TODO check -- welche Threads laufen noch?
		System.exit(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	public void windowClosing(WindowEvent arg0) {
		// user pressed the "X" button in order to quit the application
	  // quitAnimal will quit the whole application after a few checks
	  animal.quitAnimal();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public static WindowCoordinator getWindowCoordinator() {
		return WINDOW_COORDINATOR;
	}

	/**
	 * @return the animalInstance
	 */
	public Animal getAnimalInstance() {
		return animalInstance;
	}
	
  /**
   * zoom the window in or out
   * 
   * @param zoomIn
   */
  public void zoom(boolean zoomIn) {
    Font f = outputArea.getFont();
    int size = f.getSize();


    if (zoomIn) {
      if (zoomCounter < 6) {
        zoomCounter++;
      } else {
        return;

      }
      size = size + 2;
    } else {
      if (zoomCounter > -1) {
        zoomCounter--;
      } else {
        return;
      }

      size = size - 2;
    }

    if (size >= 10 && size <= 24)
      changeFontSize(size);

    Dimension windowDim = this.getSize();

    if (zoomIn) {
      if (windowDim.width <= 1400)
        windowDim.setSize(windowDim.getWidth() + 92,
            windowDim.getHeight() + 42);

    } else {
      if (windowDim.width >= 620)
        windowDim.setSize(windowDim.getWidth() - 92,
            windowDim.getHeight() - 42);

    }
    this.setSize(windowDim);

    f = new Font(f.getName(), f.getStyle(), size);

    this.setFont(f);
    ((MainToolBar) toolBar).buildToolBarZoomed(zoomIn);
    setUIFont(new javax.swing.plaf.FontUIResource(f.getFontName(), f.getStyle(),
        f.getSize()));
    this.menuBar.zoom(zoomIn);

    getWindowCoordinator().getDrawWindow(false).zoom(zoomIn);
    
    getWindowCoordinator().getAnimationWindow(false).zoom(zoomIn);

    AnimationImporter.zoom(zoomIn);
    this.repaint();

  }
  
  public static void setUIFont (javax.swing.plaf.FontUIResource f){
    java.util.Enumeration keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements()) {
      Object key = keys.nextElement();
      Object value = UIManager.get (key);
      if (value instanceof javax.swing.plaf.FontUIResource)
        UIManager.put (key, f);
      }
    } 

  /*
   * public static void installFont(Font defaultFont) { UIDefaults u =
   * UIManager.getDefaults(); java.util.Enumeration<Object> keys = u.keys();
   * while (keys.hasMoreElements()) { Object key = keys.nextElement(); if
   * (u.get(key) instanceof javax.swing.plaf.MenuItemUI) { u.put(key,
   * defaultFont); } } }
   */



}
