package animal.misc;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import translator.AnimalTranslator;
import translator.ExtendedResourceBundle;
import animal.editor.Editor;
import animal.editor.animators.AnimatorEditor;
import animal.editor.graphics.meta.GraphicEditor;
import animal.exchange.AnimationExporter;
import animal.exchange.AnimationImporter;
import animal.gui.AnimalMainWindow;
import animal.gui.AnimationOverview;
import animal.gui.DrawWindow;
import animal.gui.WindowCoordinator;
import animal.main.Animal;
import animal.main.AnimalConfiguration;
import animalscript.core.AnimalScriptParser;

public class ComponentConfigurer implements ActionListener {
	private final static String ANIMATOR_TAG = "Animator";

	private final static String IMPORT_FILTER_TAG = "ImportFilter";

	private final static String EXPORT_FILTER_TAG = "ExportFilter";

	private final static String LANGUAGE_SUPPORT_TAG = "LanguageSupport";

	private final static String ANIMALSCRIPT_HANDLER_TAG = "AnimalScriptHandler";

	private final static String ANIMALSCRIPT_PREFIX = "animalscript.extensions.";

	private final static String GRAPHIC_OBJECT_TAG = "GraphicObject";

	private final static String internalScriptingPanelKey = "internalScriptingPanel";

	private static HashMap<String, String> coreElements;
	private AnimalConfiguration currentConfiguration;

	private static Hashtable<String, JPanel> guiElements = new Hashtable<String, JPanel>(53);
  private static int                       zoomCounter               = 0;

  private static AnimalFileChooser         fileChooser;
	JTextField addPrimitives, addEffects, addScriptingHandler, addImportFilter,
			addExportFilter, addLanguage;

	String[] elements = null;

	private Vector<String> scriptingHandlers = new Vector<String>(25, 15);

	JButton addLanguageButton;

	JPanel primitivesPanel, effectsPanel, scriptHandlersPanel;

	int lastPos = -1;

	JPanel internalLanguagePanel = new JPanel(new GridLayout(0, 1));

	static {
		coreElements = new HashMap<String, String>(59);

		coreElements.put("Point", GRAPHIC_OBJECT_TAG);
		coreElements.put("Polyline", GRAPHIC_OBJECT_TAG);
		coreElements.put("Text", GRAPHIC_OBJECT_TAG);
		coreElements.put("Arc", GRAPHIC_OBJECT_TAG);
		//    coreElements.put("BoxPointer", GRAPHIC_OBJECT_TAG);

		coreElements.put("TimedShow", ANIMATOR_TAG);
		coreElements.put("ColorChanger", ANIMATOR_TAG);
		coreElements.put("Move", ANIMATOR_TAG);
		coreElements.put("Rotate", ANIMATOR_TAG);
		coreElements.put("Show", ANIMATOR_TAG);

		//    coreElements.put("Link", ANIMATOR_TAG);

		coreElements
				.put("animation/animal-ascii-compressed", IMPORT_FILTER_TAG);
		coreElements.put("animation/animal-ascii", IMPORT_FILTER_TAG);
		coreElements
				.put("animation/animalscript-compressed", IMPORT_FILTER_TAG);
		coreElements.put("animation/animalscript", IMPORT_FILTER_TAG);

		coreElements
				.put("animation/animal-ascii-compressed", EXPORT_FILTER_TAG);
		coreElements.put("animation/animal-ascii", EXPORT_FILTER_TAG);
		coreElements
				.put("animation/animalscript-compressed", EXPORT_FILTER_TAG);
		coreElements.put("animation/animalscript", EXPORT_FILTER_TAG);

		coreElements.put("English [en US]", LANGUAGE_SUPPORT_TAG);

		coreElements.put("animalscript.core.BaseAdminParser",
				ANIMALSCRIPT_HANDLER_TAG);
		coreElements.put("animalscript.core.BaseAnimatorParser",
				ANIMALSCRIPT_HANDLER_TAG);
		coreElements.put("animalscript.core.BaseObjectParser",
				ANIMALSCRIPT_HANDLER_TAG);
		coreElements.put("animalscript.extensions.ArraySupport",
				ANIMALSCRIPT_HANDLER_TAG);
		coreElements.put("animalscript.extensions.CodeSupport",
				ANIMALSCRIPT_HANDLER_TAG);

	}

	private Hashtable<String, JCheckBox> componentsHash = new Hashtable<String, JCheckBox>(91);

	JFrame targetFrame = new JFrame("Component Configuration");

	public ComponentConfigurer() {
		this(AnimalConfiguration.getDefaultConfiguration());
	}
	
	public ComponentConfigurer(AnimalConfiguration currentConfig) {
		targetFrame.getContentPane().setLayout(new BorderLayout());
		JTabbedPane tp = new JTabbedPane();
		primitivesPanel = new JPanel();
		primitivesPanel.setLayout(new BorderLayout());
		primitivesPanel.setBorder(BorderFactory.createTitledBorder(
				primitivesPanel.getBorder(), "Graphic Primitives",
				TitledBorder.CENTER, TitledBorder.TOP));
		effectsPanel = new JPanel();
		effectsPanel.setLayout(new BorderLayout());
		effectsPanel.setBorder(BorderFactory.createTitledBorder(effectsPanel
				.getBorder(), "Animation Effects", TitledBorder.CENTER,
				TitledBorder.TOP));
		Hashtable<String, Editor> knownEditors = Animal.get().getEditors();
//		Enumeration keys = knownEditors.keys();
//		Vector graphicPrimitivesVector = new Vector(20, 10);
//		Vector effectsVector = new Vector(20, 10);
//		Vector scriptingClassesVector = new Vector(20, 10);
//		Object currentKey = null, currentEntry = null;
		int n = knownEditors.size();
		elements = new String[n << 1];

//		Editor editor = null;
//		JCheckBox tmpBox = null;
//		String name = null;
//		boolean isCore = false;
		lastPos = -1;

		addPrimitives = new JTextField(20);
		addPrimitives.addActionListener(this);
		primitivesPanel.add(BorderLayout.NORTH, addPrimitives);
		primitivesPanel.setToolTipText(AnimalTranslator
				.translateMessage("confPrimitives"));
		insertEditors(knownEditors, n, true, primitivesPanel);
		tp.addTab("Primitives", primitivesPanel);

		addEffects = new JTextField(20);
		addEffects.addActionListener(this);
		effectsPanel.add(BorderLayout.NORTH, addEffects);
		effectsPanel.setToolTipText(AnimalTranslator.translateMessage("confEffects"));
		insertEditors(knownEditors, n, false, effectsPanel);
		tp.addTab("Animators", effectsPanel);

		JPanel importFilterPanel = new JPanel();
		importFilterPanel.setLayout(new BorderLayout());

		addImportFilter = new JTextField(20);
		addImportFilter.addActionListener(this);
		importFilterPanel.add(BorderLayout.NORTH, addImportFilter);
		importFilterPanel.setToolTipText(AnimalTranslator
				.translateMessage("confImportFilters"));

		insertImportFiltersTo(importFilterPanel);

		tp.addTab("Import Filter", importFilterPanel);

		JPanel exportFilterPanel = new JPanel();
		exportFilterPanel.setLayout(new BorderLayout());

		addExportFilter = new JTextField(20);
		addExportFilter.addActionListener(this);
		exportFilterPanel.add(BorderLayout.NORTH, addExportFilter);
		exportFilterPanel.setToolTipText(AnimalTranslator
				.translateMessage("confExportFilters"));

		insertExportFiltersTo(exportFilterPanel);

		tp.addTab("Export Filter", exportFilterPanel);

		JPanel languageSupportPanel = new JPanel();
		languageSupportPanel.setLayout(new BorderLayout());

		addLanguageButton = new JButton(AnimalTranslator
				.translateMessage("confLanguages"));
		addLanguageButton.addActionListener(this);
		languageSupportPanel.add(BorderLayout.NORTH, addLanguageButton);
		languageSupportPanel.setToolTipText(AnimalTranslator
				.translateMessage("confLanguages"));

		insertLanguages(languageSupportPanel);

		tp.addTab("Languages", languageSupportPanel);

		scriptHandlersPanel = new JPanel();
		scriptHandlersPanel.setLayout(new BorderLayout());

		scriptHandlersPanel.setToolTipText(AnimalTranslator
				.translateMessage("confScripting"));

		addScriptingHandler = new JTextField(20);
		addScriptingHandler.addActionListener(this);
		scriptHandlersPanel.add(BorderLayout.NORTH, addScriptingHandler);

		guiElements.put("scriptingPanel", scriptHandlersPanel);
		updateScriptingPanel();

		tp.addTab("AnimalScript", scriptHandlersPanel);

		targetFrame.getContentPane().add(BorderLayout.CENTER, tp); //centralPanel);
		JPanel buttonPanel = new JPanel();
		JButton button = new JButton(AnimalTranslator.translateMessage("ok"));
		button.addActionListener(this);
		buttonPanel.add(button);
		button = new JButton(AnimalTranslator.translateMessage("cancel"));
		button.addActionListener(this);
		buttonPanel.add(button);
		targetFrame.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
		targetFrame.pack();
		targetFrame.setVisible(true);
	}

	private void insertCheckBox(JPanel targetPanel, String entryKey,
			String entryToolTipText) {
		JCheckBox tmpBox = new JCheckBox(entryKey);
		tmpBox.setSelected(true);
		tmpBox.setEnabled(!(coreElements.containsKey(entryKey)));
		tmpBox.setToolTipText(entryToolTipText);
		tmpBox.addActionListener(this);
		targetPanel.add(tmpBox);
	}

	public void insertExportFiltersTo(JPanel targetPanel) {
		JPanel internalPanel = new JPanel(new GridLayout(0, 1));
		String[] exportFilterNames =
		    AnimalConfiguration.getDefaultConfiguration().getExportFormats();
		for (int i = 0; i < exportFilterNames.length; i++)
			insertCheckBox(internalPanel, exportFilterNames[i],
					AnimationExporter.getExporterFor(exportFilterNames[i])
							.toString());
		JScrollPane scrollPane = new JScrollPane(internalPanel);
		targetPanel.add("Center", scrollPane);
	}

	public void insertImportFiltersTo(JPanel targetPanel) {
		JPanel internalPanel = new JPanel(new GridLayout(0, 1));
		String[] importFilterNames = 
		    AnimalConfiguration.getDefaultConfiguration().getImportFormats(); 
		for (int i = 0; i < importFilterNames.length; i++)
			insertCheckBox(internalPanel, importFilterNames[i],
					AnimationImporter.getImporterFor(importFilterNames[i])
							.toString());
		JScrollPane scrollPane = new JScrollPane(internalPanel);
		targetPanel.add("Center", scrollPane);
	}

	public void insertLanguages(JPanel targetPanel) {
		System.err.println("mainWindow: " + Animal.get().getMainWindow());
		String[] languageKeys = Animal.get().getMainWindow().getLanguageKeys();
		String currentEntry = null;
		String key = null;
		if (languageKeys != null)
			for (int i = 0; i < languageKeys.length; i++) {
				currentEntry = languageKeys[i];
				StringTokenizer stringTok = new StringTokenizer(currentEntry,
						";");
				key = stringTok.nextToken() + " [" + stringTok.nextToken()
						+ "]";
//				String iconName = stringTok.nextToken();
				insertCheckBox(internalLanguagePanel, key, stringTok
						.nextToken());
			}
		JScrollPane scrollPane = new JScrollPane(internalLanguagePanel);
		targetPanel.add("Center", scrollPane);
	}

	public void updateScriptingPanel() {
		// check if internal panel with GridLayout has been initialized
		boolean panelIsAssociated = guiElements
				.containsKey(internalScriptingPanelKey);
		if (!panelIsAssociated)
			guiElements.put(internalScriptingPanelKey, new JPanel(
					new GridLayout(0, 1)));

		// retrieve internal panel
		JPanel internalPanel = guiElements
				.get(internalScriptingPanelKey);

		// check if handlers have been installed; else do so now
		if (scriptingHandlers == null || scriptingHandlers.size() == 0) {
			String[] handlers = Animal.getAnimalScriptParser(false)
					.getRegisteredHandlers();
			int nrHandlers = handlers.length;
			for (int index = 0; index < nrHandlers; index++)
				scriptingHandlers.addElement(handlers[index]);
		}

		// clear the internal panel
		internalPanel.removeAll();

		// insert each handler to the panel
		if (scriptingHandlers != null && scriptingHandlers.size() > 0) {
			int nrHandlers = scriptingHandlers.size();
			for (int i = 0; i < nrHandlers; i++) {
				String currentHandler = scriptingHandlers.elementAt(i);
				insertCheckBox(internalPanel, currentHandler, currentHandler);
			}
		}

		if (!panelIsAssociated) {
			JScrollPane scrollPane = new JScrollPane(internalPanel);
			JPanel targetPanel = guiElements.get("scriptingPanel");
			targetPanel.add("Center", scrollPane);
		}
		internalPanel.repaint();
	}

	public void insertEditors(Hashtable<String,Editor> editors, int nrElems,
			boolean graphicEditorMode, JPanel targetPanel) {
		JPanel internalPanel = new JPanel();
		internalPanel.setLayout(new GridLayout(0, 1));
		Editor editor = null;
		String name = null;
		JCheckBox tmpBox = null;
		boolean isCore = false;
		for (int i = 0; i < nrElems; i++) {
			Enumeration<Editor> elementList = editors.elements();
			while (elementList.hasMoreElements()) {
				editor = elementList.nextElement();
				if (i == editor.getNum()
						&& ((graphicEditorMode && editor instanceof GraphicEditor)
								|| (!graphicEditorMode && editor instanceof AnimatorEditor))) {
					name = editor.getName();
					elements[i] = name;
					if (i > lastPos)
						lastPos = i;
					tmpBox = new JCheckBox(name);
					isCore = coreElements.containsKey(name);
					tmpBox.setToolTipText(AnimalTranslator.translateMessage(
							(isCore) ? "coreConfMessage" : "compConfMessage",
							new Object[] { name }));
					tmpBox.setEnabled(!isCore);
					tmpBox.setSelected(true);
					if (!isCore)
						tmpBox.addActionListener(this);
					internalPanel.add(tmpBox);
					componentsHash.put(name, tmpBox);
				}
			}
		}
		JScrollPane scrollPane = new JScrollPane(internalPanel);
		targetPanel.add(scrollPane);
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() instanceof JTextField) {
			JTextField targetField = (JTextField) event.getSource();
			boolean isPrimitive = (targetField == addPrimitives);
			if (targetField == addPrimitives || targetField == addEffects) {
				String name = targetField.getText();
				if (componentsHash.containsKey(name)) {
					Object linkedObject = componentsHash.get(name);
					if (linkedObject instanceof JCheckBox)
						((JCheckBox) linkedObject).setSelected(true);
				} else
					addComponent(name, isPrimitive);
			} else if (targetField == addScriptingHandler) {
				// add the scripting handler
				addScriptingHandler(targetField.getText());

				// update the panel
				updateScriptingPanel();
			} else if (targetField == addImportFilter)
				addImportFilter(targetField.getText());
			else if (targetField == addExportFilter)
				System.err.println("add export filter 'animal.exchange."
						+ targetField.getText());

			// in any case, clear the input field
			targetField.setText("");
		} else if (event.getSource() instanceof JButton) {
			String command = event.getActionCommand();
			if (command.equalsIgnoreCase(AnimalTranslator.translateMessage("cancel"))) {
				targetFrame.setVisible(false);
				targetFrame.dispose();
			} else if (command.equalsIgnoreCase(AnimalTranslator
					.translateMessage("ok"))) {
				dumpFiles();
			} else if (command.equalsIgnoreCase(AnimalTranslator
					.translateMessage("confLanguages"))) {
        AnimalFileChooser aFileChooser = 
					new AnimalFileChooser(AnimalConfiguration.getDefaultConfiguration());
        System.out.println("new AnimalFileChooser");
        if (zoomCounter > 0) {
          for (int i = 0; i < zoomCounter; i++) {
            aFileChooser.zoom(true);
          }
        } else {
          for (int i = 0; i > zoomCounter; i--) {
            aFileChooser.zoom(false);
          }

        }
        fileChooser = aFileChooser;
				aFileChooser.setSpecificFilter(null, "AnimalResources",
						"Animal Resource Files", true);
				String filename = aFileChooser.openForStraightLoad(targetFrame);
				ExtendedResourceBundle resource = new ExtendedResourceBundle(
						filename);
				Animal.get().getMainWindow().addLanguageSupportEntry(
						resource.getMessage("language.locale"),
						resource.getMessage("language.label"),
						resource.getMessage("language.icon"),
						resource.getMessage("language.tooltip"));
				insertCheckBox(internalLanguagePanel, resource
						.getMessage("language.label")
						+ " [" + resource.getMessage("language.locale") + "]",
						resource.getMessage("language.tooltip"));
			} else if (event.getSource() instanceof JCheckBox) {
				JCheckBox selectedCheckBox = (JCheckBox) event.getSource();
				System.err.println(selectedCheckBox);
			}
		}
	}

	private void dumpFiles() {
		JFileChooser fileChooser = new JFileChooser();
		int returnValue = fileChooser.showSaveDialog(targetFrame);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File chosenDir = fileChooser.getCurrentDirectory();
			StringBuilder scriptHandlers = new StringBuilder(30);
			JPanel internalScriptingPanel = guiElements
					.get(internalScriptingPanelKey);
			if (internalScriptingPanel != null) {
				Component[] compos = internalScriptingPanel.getComponents();
				if (compos != null) {
					int nrCompos = compos.length;
					for (int compoIndex = 0; compoIndex < nrCompos; compoIndex++)
						if (compos[compoIndex] instanceof JCheckBox) {
							JCheckBox currentBox = (JCheckBox) compos[compoIndex];
							if (currentBox.isSelected())
								scriptHandlers.append(
										currentBox.getActionCommand()).append(MessageDisplay.LINE_FEED);
							else
								AnimalScriptParser
										.removeScriptingHandler(currentBox
												.getActionCommand());
						}
				}
			}
			try {
				DataOutputStream dos = new DataOutputStream(
						new FileOutputStream(new File(fileChooser
								.getCurrentDirectory(), "handlers.dat")));
				dos.writeBytes(scriptHandlers.toString());
				dos.close();
			} catch (IOException e) {
				MessageDisplay.errorMsg(
								AnimalTranslator.translateMessage(
										"compConfigWriteError",
										new Object[] {
												AnimalTranslator.translateMessage("animalScriptHandlers"),
												"handlers.dat",
												chosenDir.getName() }),
								MessageDisplay.RUN_ERROR);
			}
			
			targetFrame.setVisible(false);
			targetFrame.dispose();
		}
	}

	private void addComponent(String name, boolean isGraphicObject) {
		// add the component
        // TODO The following line was commented out.
//		Animal.get().addAnimalComponent(name);
		AnimalConfiguration animalConfig = AnimalConfiguration.getDefaultConfiguration();
		// add a checkbox entry
		JCheckBox newBox = new JCheckBox(name);
		newBox.setSelected(true);
		newBox.addActionListener(this);

		// add entry to hash
		componentsHash.put(name, newBox);
		WindowCoordinator winCoord = AnimalMainWindow.getWindowCoordinator();
		if (isGraphicObject) {
		    // TODO: have to adapt component number "somehow", or make sure that if ==0,
		    // next free number is taken
		  animalConfig.insertPrimitive(name, "animal.graphics." +name, 0,
                  animalConfig.getProperties()); 
			// add checkbox to appropriate panel (primitives)
			primitivesPanel.add(newBox);
			DrawWindow drawWindow = winCoord.getDrawWindow(false);
			drawWindow.installPrimitiveToolBar();
		} else {
			// add checkbox to appropriate panel (effects)
			effectsPanel.add(newBox);
      animalConfig.insertAnimator(name, "animal.animator." +name, 0,
              animalConfig.getProperties()); 
			AnimationOverview animationOverview = winCoord.getAnimationOverview(false);
			animationOverview.installAnimatorToolBar();
		}
	}

	private void addScriptingHandler(String handlerName) {
		StringBuilder className = new StringBuilder(handlerName);
		if (className != null && className.length() > 0) {
			if (!className.toString().startsWith(ANIMALSCRIPT_PREFIX))
				className.insert(0, ANIMALSCRIPT_PREFIX);
			AnimalScriptParser.addScriptingHandler(className.toString());
			scriptingHandlers.addElement(className.toString());
		}
	}

	@SuppressWarnings("unchecked")
	public void addExportFilter(String handlerName) {
	  String theHandlerName = handlerName;
		if (!theHandlerName.startsWith("animal.exchange."))
		  theHandlerName = "animal.exchange." + handlerName;
		try {
			Class<AnimationExporter> importerClass = 
				(Class<AnimationExporter>)Class.forName(theHandlerName);
			Object targetObject = importerClass.newInstance();
			if (targetObject instanceof AnimationExporter) {
				AnimationExporter handler = importerClass.newInstance();
				if (handler != null)
					currentConfiguration.insertExportFilter(handler.getDefaultExtension(),
					    theHandlerName);
			}
		} catch (Exception e) {
			System.err.println("***" + theHandlerName
					+ " does not exist or is not a proper AnimationExporter:");
		}
	}

	@SuppressWarnings("unchecked")
	public void addImportFilter(String handlerName) {
	  String theHandlerName = handlerName;
		if (!theHandlerName.startsWith("animal.exchange."))
		  theHandlerName = "animal.exchange." + handlerName;
		System.err.println("trying to load importer class '" + theHandlerName
				+ "'");
		try {
			Class<AnimationImporter> importerClass = 
				(Class<AnimationImporter>)Class.forName(theHandlerName);
			Object targetObject = importerClass.newInstance();
			if (targetObject instanceof AnimationImporter) {
				AnimationImporter handler = importerClass.newInstance();
				if (handler != null)
				    currentConfiguration.insertImportFilter(handler.getDefaultExtension(),
				        theHandlerName);
			}
		} catch (Exception e) {
			System.err.println("***" + theHandlerName
					+ " does not exist or is not a proper AnimationImporter:");
		}
	}

	public void setVisible(boolean isVisible) {
		if (targetFrame != null)
			targetFrame.setVisible(isVisible);
	}

  /**
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public static void zoom(boolean zoomIn) {

    if (zoomIn) {
      if (zoomCounter < 6)
        zoomCounter++;

    } else {
      if (zoomCounter > -1)
        zoomCounter--;
    }

    if (fileChooser != null)
      fileChooser.zoom(zoomIn);
  }

}