package animal.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;


import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.metal.MetalSliderUI;

import animal.api.FileDrop;
import animal.exchange.AnimationImporter;
import animal.gui.AnimalMainWindow;
import animal.gui.GraphicVector;
import animal.gui.VariableView;
import animal.gui.WindowCoordinator;
import animal.main.icons.LoadIcon;
import animal.main.lookandfeel.AnimalStepBasicSliderUI;
import animal.misc.MessageDisplay;
import animal.misc.XProperties;
import animalscript.core.AnimalScriptParser;
import generators.framework.Generator;
import generators.framework.InteractiveGenerator;
import generators.generatorframe.store.GetInfos;
import generators.generatorframe.store.SearchLoader;
import generators.generatorframe.view.GeneratorFrame;
import helpers.AnimalReader;
import interactionsupport.parser.AnimalscriptParser;
import translator.Translator;

/**
 * Window to display the Animal to PDF controls and infos.
 * 
 * @author Marian Hieke
 *
 */

public class AnimalToPDFWindowView extends AnimalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private Translator translator;

	private Container workContainer = null;
	private AnimalToPDFWindow controller = null;
	private Font defaultFont = new Font("Dialog", 0, 14);
	private Font buttonFont = new Font("Dialog.bold", 1, 10);
	private Font labelFont = new Font("Dialog.bold", 1, 12);
	private JButton hookingButton;

	private int height = 25;
	private int width = 25;
	private int zoomCounter = 0;
	boolean hideWindow = false;


	private JPanel main = null;

	/**
	   * construct an AnimationWindow. Actual initialization is done in
	   * <code>init</code>.
	   * 
	   * @param animalInstance
	   *          the current Animal instance
	   * @param properties
	   *          the current animation properties
	   * @see #init()
	   */
	public AnimalToPDFWindowView(Animal animalInstance, XProperties properties, AnimalToPDFWindow controller) {
	    super(animalInstance, properties);
	    this.controller = controller;
	    setDependentContainer(getContentPane());
		this.translator = new Translator("AnimationFrame", Animal.getCurrentLocale());
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				hideMenu();
			}
		});
	  }

	/**
	   * construct an AnimationWindow. Actual initialization is done in
	   * <code>init</code>.
	   * 
	   * @param animalInstance
	   *          the current Animal instance
	   * @param properties
	   *          the current animation properties
	   * @param aContainer
	   *          the container that contains this component
	   * @see #init()
	   */
	public AnimalToPDFWindowView(Animal animalInstance, XProperties properties,
			Container aContainer, AnimalToPDFWindow controller) {
	    super(animalInstance, properties);
	    this.controller = controller;
	    setDependentContainer(aContainer);
		this.translator = new Translator("AnimationFrame", Animal.getCurrentLocale());

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				hideMenu();
			}
		});
	  }

	private JButton addButton;
	private JButton addXButton;
	private JTextField addXField;
	private JButton removeButton;
	private JButton removeXButton;
	private JTextField removeXField;
	private JButton addLastXButton;
	private JTextField addLastXField;
	private JButton removeLastXButton;
	private JTextField removeLastXField;
	private JButton addTextPageButton;
	private JTextArea infoArea;
	private JScrollPane infoScroll;
	private JLabel dummyLabel1;
	private JLabel dummyLabel2;
	private JButton addFromXToY;
	private JTextField addFromXField;
	private JTextField addToYField;
	private JButton removeFromXToY;
	private JTextField removeFromXField;
	private JTextField removeToYField;
	private JButton createButton;

	private JPanel addRemovePanel;
	private JPanel addXPanel;
	private JPanel removeXPanel;
	private JPanel addLastPanel;
	private JPanel removeLastPanel;
	private JPanel textPagePanel;
	private JPanel createPanel;
	private JPanel addFromToPanel;
	private JPanel removeFromToPanel;
	private JPanel hookingPanel;
	Dimension buttonSize = new Dimension(160, 30);
	Dimension buttonSize2 = new Dimension(280, 30);

	LinkedList<JButton> buttons = new LinkedList<JButton>();
	LinkedList<JButton> buttons2 = new LinkedList<JButton>();





	private Dimension buttonMinSize = new Dimension(30, 40);

	/**
	 * initializes the AnimationWindow by adding the control panel and the
	 * AnimationCanvas.
	 */
	public void init() {
		super.init();
		super.pack();
		super.setLocationRelativeTo(null);

		setTitle("Animal to PDF");
		workContainer().setLayout(new GridBagLayout());



		Dimension dimFrame = new Dimension(420, 700);
		setBounds(805, 20, dimFrame.width, dimFrame.height);
		setMinimumSize(new Dimension(200, 600));
		setPreferredSize(dimFrame);


		main = new JPanel();

		main.setPreferredSize(dimFrame);
		main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));
		dummyLabel1 = new JLabel();
		dummyLabel1.setBackground(Color.WHITE);
		dummyLabel1.setForeground(Color.WHITE);
		dummyLabel2 = new JLabel();
		dummyLabel2.setBackground(Color.WHITE);
		dummyLabel2.setForeground(Color.WHITE);
		
		Dimension labelDim = new Dimension(400, 20);


		Dimension textDim = new Dimension(290, 100);


		Dimension fieldSize = new Dimension(60, 30);
		Dimension panelDim = new Dimension(420, 40);
		dummyLabel1.setPreferredSize(labelDim);
		dummyLabel1.setMinimumSize(labelDim);
		dummyLabel1.setMaximumSize(labelDim);

		dummyLabel2.setPreferredSize(labelDim);
		dummyLabel2.setMinimumSize(labelDim);
		dummyLabel2.setMaximumSize(labelDim);
		main.add(dummyLabel1);

		addRemovePanel = new JPanel();
		addRemovePanel.setLayout(new BorderLayout(0, 0));
		addXPanel = new JPanel();
		addXPanel.setLayout(new BorderLayout(0, 0));

		removeXPanel = new JPanel();
		removeXPanel.setLayout(new BorderLayout(0, 0));

		hookingPanel = new JPanel();
		hookingPanel.setPreferredSize(panelDim);
		hookingPanel.setMinimumSize(panelDim);
		hookingPanel.setMaximumSize(panelDim);

		hookingButton = new JButton("Unhook");
		hookingButton.setToolTipText("unhooks the PDF menu from the main window");
		hookingButton.setFocusPainted(false);
		hookingButton.setFont(buttonFont);
		hookingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.setAnimationPlayerHooked(!controller.isMenuHooked());

			}
		});

		buttons.add(hookingButton);
		hookingPanel.add(hookingButton);
		main.add(hookingPanel);

		main.add(addRemovePanel);
		this.add(main);

		addRemovePanel.setPreferredSize(panelDim);
		addRemovePanel.setMinimumSize(panelDim);
		addRemovePanel.setMaximumSize(panelDim);

		addButton = new JButton("add page");
		addButton.setToolTipText("adds the current page to the list of pages");
		addButton.setFocusPainted(false);
		addButton.setFont(buttonFont);
		// addButton.setSize(buttonSize);
		addButton.setMinimumSize(buttonSize);
		addButton.setMaximumSize(buttonSize);
		addButton.setPreferredSize(buttonSize);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// controller.setAnimationPlayerHooked(!controller.isAnimationPlayerHooked());
				controller.addPage();

			}
		});
		
		buttons.add(addButton);

		addRemovePanel.add(addButton, BorderLayout.CENTER);
		// addPanel.setSize(300, 50);

		addXField = new JTextField();
		addXField.setPreferredSize(fieldSize);
		addXField.setMinimumSize(fieldSize);
		addXField.setMaximumSize(fieldSize);
		addXField.setText("X");
		addXPanel.add(addXField, BorderLayout.LINE_START);

		addXButton = new JButton("add page at X");
		addXButton.setToolTipText("adds the current page at number X in the list of pages");
		addXButton.setFocusPainted(false);
		addXButton.setFont(buttonFont);
		addXButton.setSize(buttonSize);
		addXButton.setMinimumSize(buttonSize);
		addXButton.setMaximumSize(buttonSize);
		addXButton.setPreferredSize(buttonSize);
		addXButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int x = 0;
				if (isNumeric(addXField.getText()))
					x = Integer.valueOf(addXField.getText());
				controller.addPageAt(x - 1);

			}
		});

		buttons.add(addXButton);

		addXPanel.add(addXButton, BorderLayout.LINE_END);
		
		addXPanel.setPreferredSize(panelDim);
		addXPanel.setMinimumSize(panelDim);
		addXPanel.setMaximumSize(panelDim);
		removeXPanel.setPreferredSize(panelDim);
		removeXPanel.setMinimumSize(panelDim);
		removeXPanel.setMaximumSize(panelDim);

		main.add(addXPanel);






		removeButton = new JButton("remove page");
		removeButton.setToolTipText("removes the last page of the list of pages");
		removeButton.setFocusPainted(false);
		removeButton.setFont(buttonFont);
		removeButton.setMinimumSize(buttonSize);
		removeButton.setMaximumSize(buttonSize);
		removeButton.setPreferredSize(buttonSize);
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.removePage();
			}
		});
		
		buttons.add(removeButton);
		JLabel dummy = new JLabel();


		addRemovePanel.add(removeButton, BorderLayout.LINE_END);
		
		removeXField = new JTextField();
		removeXField.setPreferredSize(fieldSize);
		removeXField.setMinimumSize(fieldSize);
		removeXField.setMaximumSize(fieldSize);
		removeXField.setText("X");

		removeXButton = new JButton("remove X");
		removeXButton.setToolTipText("removes the page at number X in the list of pages");
		removeXButton.setFocusPainted(false);
		removeXButton.setFont(buttonFont);
		removeXButton.setSize(buttonSize);
		removeXButton.setMinimumSize(buttonSize);
		removeXButton.setMaximumSize(buttonSize);
		removeXButton.setPreferredSize(buttonSize);
		removeXButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int x = 0;
				if (isNumeric(removeXField.getText()))
					x = Integer.valueOf(removeXField.getText());
				controller.removePage(x - 1);
			}
		});

		buttons.add(removeXButton);
		removeXPanel.add(removeXButton, BorderLayout.LINE_END);


		removeXPanel.add(removeXField, BorderLayout.LINE_START);

		main.add(removeXPanel);

		addLastXField = new JTextField();
		addLastXField.setPreferredSize(fieldSize);
		addLastXField.setMinimumSize(fieldSize);
		addLastXField.setMaximumSize(fieldSize);
		addLastXField.setText("X");

		addLastXButton = new JButton("add last X");
		addLastXButton.setToolTipText("adds the last X pages to the list of pages");
		addLastXButton.setFocusPainted(false);
		addLastXButton.setFont(buttonFont);
		addLastXButton.setSize(buttonSize);
		addLastXButton.setMinimumSize(buttonSize);
		addLastXButton.setMaximumSize(buttonSize);
		addLastXButton.setPreferredSize(buttonSize);
		addLastXButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int x = 0;
				if (isNumeric(addLastXField.getText()))
					x = Integer.valueOf(addLastXField.getText());
				controller.addLastSteps(x);
			}
		});

		buttons.add(addLastXButton);

		addLastPanel = new JPanel();
		addLastPanel.setLayout(new BorderLayout(0, 0));

		addLastPanel.setPreferredSize(panelDim);
		addLastPanel.setMinimumSize(panelDim);
		addLastPanel.setMaximumSize(panelDim);

		addLastPanel.add(addLastXButton, BorderLayout.LINE_END);

		addLastPanel.add(addLastXField, BorderLayout.LINE_START);
		main.add(addLastPanel);

		removeLastXField = new JTextField();
		removeLastXField.setPreferredSize(fieldSize);
		removeLastXField.setMinimumSize(fieldSize);
		removeLastXField.setMaximumSize(fieldSize);
		removeLastXField.setText("X");

		removeLastXButton = new JButton("remove last X");
		removeLastXButton.setToolTipText("removes the last X pages from the list of pages");
		removeLastXButton.setFocusPainted(false);
		removeLastXButton.setFont(buttonFont);
		removeLastXButton.setSize(buttonSize);
		removeLastXButton.setMinimumSize(buttonSize);
		removeLastXButton.setMaximumSize(buttonSize);
		removeLastXButton.setPreferredSize(buttonSize);
		removeLastXButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int x = 0;
				if (isNumeric(removeLastXField.getText()))
					x = Integer.valueOf(removeLastXField.getText());
				controller.removeLastSteps(x);

			}
		});

		buttons.add(removeLastXButton);

		removeLastPanel = new JPanel();
		removeLastPanel.setLayout(new BorderLayout(0, 0));
		removeLastPanel.setPreferredSize(panelDim);
		removeLastPanel.setMinimumSize(panelDim);
		removeLastPanel.setMaximumSize(panelDim);

		removeLastPanel.add(removeLastXButton, BorderLayout.LINE_END);



		removeLastPanel.add(removeLastXField, BorderLayout.LINE_START);
		main.add(removeLastPanel);


		addFromXField = new JTextField();
		addFromXField.setPreferredSize(fieldSize);
		addFromXField.setMinimumSize(fieldSize);
		addFromXField.setMaximumSize(fieldSize);
		addFromXField.setText("X");

		addToYField = new JTextField();
		addToYField.setPreferredSize(fieldSize);
		addToYField.setMinimumSize(fieldSize);
		addToYField.setMaximumSize(fieldSize);
		addToYField.setText("Y");

		addFromXToY = new JButton("add X to Y");
		addFromXToY.setToolTipText("adds the pages from X to Y to the list of pages");
		addFromXToY.setFocusPainted(false);
		addFromXToY.setFont(buttonFont);
		addFromXToY.setSize(buttonSize);
		addFromXToY.setMinimumSize(buttonSize);
		addFromXToY.setMaximumSize(buttonSize);
		addFromXToY.setPreferredSize(buttonSize);
		addFromXToY.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int x = 0;
				if (isNumeric(addFromXField.getText()))
					x = Integer.valueOf(addFromXField.getText());
				int y = 0;
				if (isNumeric(addToYField.getText()))
					y = Integer.valueOf(addToYField.getText());

				controller.addFromTo(x, y);

			}
		});

		buttons.add(addFromXToY);

		addFromToPanel = new JPanel();
		addFromToPanel.setLayout(new BorderLayout(0, 0));
		addFromToPanel.setPreferredSize(panelDim);
		addFromToPanel.setMinimumSize(panelDim);
		addFromToPanel.setMaximumSize(panelDim);

		addFromToPanel.add(addFromXToY, BorderLayout.LINE_END);
		addFromToPanel.add(addFromXField, BorderLayout.LINE_START);
		addFromToPanel.add(addToYField, BorderLayout.CENTER);
		main.add(addFromToPanel);

		removeFromXField = new JTextField();
		removeFromXField.setPreferredSize(fieldSize);
		removeFromXField.setMinimumSize(fieldSize);
		removeFromXField.setMaximumSize(fieldSize);
		removeFromXField.setText("X");

		removeToYField = new JTextField();
		removeToYField.setPreferredSize(fieldSize);
		removeToYField.setMinimumSize(fieldSize);
		removeToYField.setMaximumSize(fieldSize);
		removeToYField.setText("Y");

		removeFromXToY = new JButton("remove X to Y");
		removeFromXToY.setToolTipText("removes the pages from X to Y from the list of pages");
		removeFromXToY.setFocusPainted(false);
		removeFromXToY.setFont(buttonFont);
		removeFromXToY.setSize(buttonSize);
		removeFromXToY.setMinimumSize(buttonSize);
		removeFromXToY.setMaximumSize(buttonSize);
		removeFromXToY.setPreferredSize(buttonSize);
		removeFromXToY.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int x = 0;
				if (isNumeric(removeFromXField.getText()))
					x = Integer.valueOf(removeFromXField.getText());
				int y = 0;
				if (isNumeric(removeToYField.getText()))
					y = Integer.valueOf(removeToYField.getText());

				controller.removeFromTo(x, y);

			}
		});

		buttons.add(removeFromXToY);

		removeFromToPanel = new JPanel();
		removeFromToPanel.setLayout(new BorderLayout(0, 0));
		removeFromToPanel.setPreferredSize(panelDim);
		removeFromToPanel.setMinimumSize(panelDim);
		removeFromToPanel.setMaximumSize(panelDim);

		removeFromToPanel.add(removeFromXToY, BorderLayout.LINE_END);
		removeFromToPanel.add(removeFromXField, BorderLayout.LINE_START);
		removeFromToPanel.add(removeToYField, BorderLayout.CENTER);
		main.add(removeFromToPanel);


		addTextPageButton = new JButton("add text page");
		addTextPageButton.setToolTipText("opens a window for creating and adding a text page");
		addTextPageButton.setFocusPainted(false);
		addTextPageButton.setFont(buttonFont);
		addTextPageButton.setMinimumSize(buttonSize2);
		addTextPageButton.setMaximumSize(buttonSize2);
		addTextPageButton.setPreferredSize(buttonSize2);
		addTextPageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.addTextPage();
			}
		});

		buttons2.add(addTextPageButton);

		textPagePanel = new JPanel();
		textPagePanel.setLayout(new BorderLayout(0, 0));
		textPagePanel.add(addTextPageButton, BorderLayout.CENTER);
		textPagePanel.setPreferredSize(panelDim);
		textPagePanel.setMinimumSize(panelDim);
		textPagePanel.setMaximumSize(panelDim);

		main.add(textPagePanel);

		createButton = new JButton("create PDF");
		createButton.setToolTipText("opens the editor window for creating the final pdf");
		createButton.setFocusPainted(false);
		createButton.setFont(buttonFont);
		createButton.setMinimumSize(buttonSize2);
		createButton.setMaximumSize(buttonSize2);
		createButton.setPreferredSize(buttonSize2);
		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.createPDF();
			}
		});

		buttons2.add(createButton);

		createPanel = new JPanel();
		createPanel.setLayout(new BorderLayout(0, 0));
		createPanel.add(createButton, BorderLayout.CENTER);
		createPanel.setPreferredSize(panelDim);
		createPanel.setMinimumSize(panelDim);
		createPanel.setMaximumSize(panelDim);

		main.add(createPanel);
		String info = "You can use this menu to create a PDF." + "\r\n"
				+ "You can add steps of an animation as" + "\r\n" + "a page or add a text page." + "\r\n"
				+ "By clicking on create pdf you get into" + "\r\n"
				+ "an editor and get an overview of the added pages" + "\r\n" + "and have the option to delete pages"
				+ "\r\n" + "and to print the pages as a pdf-file.";


		infoArea = new JTextArea();
		// infoArea.setPreferredSize(textDim);

		infoArea.setText(info);

		infoScroll = new JScrollPane(infoArea);
		main.add(infoScroll);
		

		this.setVisible(true);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		WindowListener exitListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// controller.setAnimationPlayerHooked(true);
			}
		};
		addWindowListener(exitListener);

		new FileDrop(this, new FileDrop.Listener() {
			@Override
			public void filesDropped(File[] files) {

				if (files.length == 1) {

					String filePath = files[0].getAbsolutePath();
					AnimalScriptParser animalScriptParser = Animal.getAnimalScriptParser(true);
					Animation newAnim = animalScriptParser.importAnimationFrom(filePath, true);
					if (AnimalScriptParser.fileContents.toString().startsWith("%Animal")) {
						String extension = "";
						int i = filePath.lastIndexOf('.');
						if (i > 0) {
							extension = filePath.substring(i + 1);
						}
						if (AnimationImporter.finalizeAnimationLoading(newAnim, filePath, extension)) {
							Animal.get().setAnimalScriptCode(AnimalScriptParser.fileContents.toString());
						}
					} else {
						MessageDisplay.errorMsg("You can only drag&drop a file with AnimalScript-Code!",
								MessageDisplay.RUN_ERROR);
					}
				} else {
					MessageDisplay.errorMsg("You can only drag&drop ONE file!", MessageDisplay.RUN_ERROR);
				}
			}
		});

	}

	public void addPanelFullScreenToWorkContainer(JPanel panel) {
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		workContainer().add(panel, c);
	}



	public void changeLocale(Locale targetLocale) {
		translator.setTranslatorLocale(Animal.getCurrentLocale());

	}

	private <K, V> HashMap<K, V> dictionaryToHashMap(Dictionary<K, V> source) {
		HashMap<K, V> sink = new HashMap<K, V>();
		for (Enumeration<K> keys = source.keys(); keys.hasMoreElements();) {
			K key = keys.nextElement();
			sink.put(key, source.get(key));
		}
		return sink;
	}

	/**
	 * initializes the AnimationWindow's bounds.
	 * 
	 * @param props
	 *            the properties to set (concerns the window coordinates and
	 *            bounds).
	 */
	void setProperties(XProperties properties) {
		setBounds(properties.getIntProperty("animationWindow.x", 50),
				properties.getIntProperty("animationWindow.y", 50),
				properties.getIntProperty("animationWindow.width", 400),
				properties.getIntProperty("animationWindow.height", 200));
	}

	/**
	 * stores the window location into the properties passed in
	 * 
	 * @param props
	 *            the properties in which the window location is to be stored
	 */
	void getProperties(XProperties properties) {
		Rectangle b = getBounds();

		if (((b.width - 10) == properties.getIntProperty("animationWindow.width", 320))
				&& ((b.height - 22) == properties.getIntProperty("animationWindow.height", 200))) {
			b.width -= 10;
			b.height -= 22;
		}

		properties.put("animationWindow.x", b.x);
		properties.put("animationWindow.y", b.y);
		properties.put("animationWindow.width", b.width);
		properties.put("animationWindow.height", b.height);
	}


	private JPanel createEmptyJPanelWithSize(Dimension dim) {
		JPanel panel = new JPanel();
		panel.setPreferredSize(dim);
		panel.setSize(dim);
		panel.setMinimumSize(dim);
		panel.setMaximumSize(dim);
		return panel;
	}


	private ImageIcon getImageIconPlayer(String name) {
		try {
			return new ImageIcon(AnimalReader.getImageIcon(AnimalReader.getInputStreamOnLayer(LoadIcon.class, name))
					.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		} catch (Exception e) {
			return null;
		}
	}

	public boolean isSettingsVisible() {
		return main != null && main.isValid();
	}

	/**
	 * Sets the container that contains this component
	 * 
	 * @param aContainer
	 *            the container for this component
	 */
	public void setDependentContainer(Container aContainer) {
		workContainer = aContainer;
	}



	/**
	 * @param zoomIn
	 *            if true zooms in, if false zooms out
	 */
	public void zoom(boolean zoomIn) {
		


		/****************************************************
		 * 
		 */
		Dimension dim = new Dimension(0, 0);
		Dimension dimPanel = new Dimension(0, 0);

		if (addRemovePanel != null)
			dim = addRemovePanel.getSize();

		boolean zoom = false;

		if (zoomIn) {
			if (zoomCounter < 6) {
				zoomCounter++;

				zoom = true;
			}
			if (defaultFont.getSize() < 24) {
				defaultFont = new Font(defaultFont.getFontName(), defaultFont.getStyle(), defaultFont.getSize() + 2);
			}

			if (labelFont.getSize() < 16) {
				labelFont = new Font(labelFont.getFontName(), labelFont.getStyle(), labelFont.getSize() + 1);
			}


			if (height < 45) {
				height = height + 5;
				width = width + 5;
			}

		} else {
			if (zoomCounter > -1) {
				zoomCounter--;

				zoom = true;
			}
			if (defaultFont.getSize() > 10) {
				defaultFont = new Font(defaultFont.getFontName(), defaultFont.getStyle(), defaultFont.getSize() - 2);
			}

			

			
			if (height > 20) {
				height = height - 5;
				width = width - 5;
			}
			if (labelFont.getSize() > 9) {
				labelFont = new Font(labelFont.getFontName(), labelFont.getStyle(), labelFont.getSize() - 1);
			}

		}

		buttonFont = new Font(buttonFont.getFontName(), buttonFont.getStyle(), defaultFont.getSize() - 2);
		
	
		
		if (zoom) {

		Dimension dimFrame = new Dimension(420 + 40 * zoomCounter, 40 + 2 * zoomCounter);
		setBounds(805, 20, dimFrame.width, dimFrame.height);
		setMinimumSize(new Dimension(200, 600));
		setPreferredSize(dimFrame);
		

		
		dim = new Dimension(320 + 40 * zoomCounter, 700 + 2 * zoomCounter);
		main.setMinimumSize(dim);
		main.setMaximumSize(dim);
		main.setPreferredSize(dim);

			dimPanel = new Dimension(410 + 40 * zoomCounter, 40 + 2 * zoomCounter);

		 if (addRemovePanel != null) {

			addRemovePanel.setMinimumSize(dimPanel);
			addRemovePanel.setMaximumSize(dimPanel);
			addRemovePanel.setPreferredSize(dimPanel);
		}
		 
		 if (addXPanel != null) {

				addXPanel.setMinimumSize(dimPanel);
				addXPanel.setMaximumSize(dimPanel);
				addXPanel.setPreferredSize(dimPanel);
			}
		 
		 if (removeXPanel != null) {

				removeXPanel.setMinimumSize(dimPanel);
				removeXPanel.setMaximumSize(dimPanel);
				removeXPanel.setPreferredSize(dimPanel);
			}
		 
		 if (addLastPanel != null) {

				addLastPanel.setMinimumSize(dimPanel);
				addLastPanel.setMaximumSize(dimPanel);
				addLastPanel.setPreferredSize(dimPanel);
			}
		 
		 
		 if (removeLastPanel != null) {

				removeLastPanel.setMinimumSize(dimPanel);
				removeLastPanel.setMaximumSize(dimPanel);
				removeLastPanel.setPreferredSize(dimPanel);
			}
		 
		 if (textPagePanel != null) {

				textPagePanel.setMinimumSize(dimPanel);
				textPagePanel.setMaximumSize(dimPanel);
				textPagePanel.setPreferredSize(dimPanel);
			}
		 
		 if (createPanel != null) {

				createPanel.setMinimumSize(dimPanel);
				createPanel.setMaximumSize(dimPanel);
				createPanel.setPreferredSize(dimPanel);
			}
		 
		 if (addFromToPanel != null) {

				addFromToPanel.setMinimumSize(dimPanel);
				addFromToPanel.setMaximumSize(dimPanel);
				addFromToPanel.setPreferredSize(dimPanel);
			}
		 
		 if (removeFromToPanel != null) {

				removeFromToPanel.setMinimumSize(dimPanel);
				removeFromToPanel.setMaximumSize(dimPanel);
				removeFromToPanel.setPreferredSize(dimPanel);
			}

		if (hookingPanel != null) {

			hookingPanel.setMinimumSize(dimPanel);
			hookingPanel.setMaximumSize(dimPanel);
			hookingPanel.setPreferredSize(dimPanel);
		}

		if (infoArea != null) {
			Dimension textDim = new Dimension(290 + zoomCounter * 40, 100 + 2 * zoomCounter);
			infoArea.setPreferredSize(textDim);
			infoArea.setMinimumSize(textDim);
			infoArea.setMaximumSize(textDim);

		}

	if(addXField!=null)
	{
		addXField.setFont(defaultFont);
	}

	if(removeXField!=null)
	{
		removeXField.setFont(defaultFont);
	}

	if(addLastXField!=null)
	{
		addLastXField.setFont(defaultFont);
	}if(removeLastXField!=null)
	{
		removeLastXField.setFont(defaultFont);
	}

	if(infoArea!=null)
	{
		infoArea.setFont(defaultFont);
		}

			if (zoomIn) {
				buttonSize.setSize(buttonSize.getWidth() + 5, buttonSize.getHeight() + 2);
				buttonSize2.setSize(buttonSize2.getWidth() + 5, buttonSize2.getHeight() + 2);
			} else {
				buttonSize.setSize(buttonSize.getWidth() - 5, buttonSize.getHeight() - 2);
				buttonSize2.setSize(buttonSize2.getWidth() - 5, buttonSize2.getHeight() - 2);
			}
			for (JButton button : buttons)
				zoomButton(button, buttonFont, buttonSize);
	
			for (JButton button : buttons2)
				zoomButton(button, buttonFont, buttonSize2);


		if (addFromXField != null) {
			addFromXField.setFont(defaultFont);
		}

		if (addToYField != null) {
			addToYField.setFont(defaultFont);
		}

		if (removeFromXField != null) {
			removeFromXField.setFont(defaultFont);
		}

		if (removeToYField != null) {
			removeToYField.setFont(defaultFont);
		}

		}

	}

	private void zoomButton(AbstractButton but, Font f, Dimension buttonDim) {

		if (but.getIcon() != null) {
			Image img = ((ImageIcon) but.getIcon()).getImage();
			Image newimg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(newimg);
			but.setIcon(icon);
		}
		but.setFont(f);

		but.setPreferredSize(buttonDim);
		but.setMinimumSize(buttonDim);
		but.setMaximumSize(buttonDim);

	}

	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}


	public WindowCoordinator getWindowCoordinator() {
		return animal.getMainWindow().getWindowCoordinator();

	}

	public JButton getUHookPlayerButton() {
		return hookingButton;
	}


	public JPanel getMainPanel() {
		return this.main;
	}

	public void hideMenu() {
		hideWindow = !hideWindow;
		if (hideWindow) {
			if (controller.isMenuHooked())
				controller.setAnimationPlayerHookedHide(false);
			else {
				this.setVisible(false);
			}

		} else {
			this.setVisible(true);

			if (!controller.isMenuHooked())
				controller.setAnimationPlayerHooked(true);

		}

	}

	public void hideMenu(boolean hide) {
		hideWindow = hide;
		if (hideWindow) {
			if (controller.isMenuHooked())
				controller.setAnimationPlayerHookedHide(false);
			else {
				this.setVisible(false);
			}
		} else {
			// this.setVisible(true);
			if (!controller.isMenuHooked())
				controller.setAnimationPlayerHooked(true);
		}
	}





}
