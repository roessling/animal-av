/*
 * Created on 17.01.2005
 */
package de.ahrgr.animal.kohnert.generatorgui;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import de.ahrgr.animal.kohnert.asugen.EmbeddedPropertiesFilter;
import de.ahrgr.animal.kohnert.asugen.Generator;
import de.ahrgr.animal.kohnert.asugen.GeneratorFactory;
import de.ahrgr.animal.kohnert.asugen.PropertyParser;
import de.ahrgr.animal.kohnert.asugen.property.ColorProperty;
import de.ahrgr.animal.kohnert.asugen.property.EKFontProperty;
import de.ahrgr.animal.kohnert.asugen.property.FormatedTextProperty;
import de.ahrgr.animal.kohnert.asugen.property.Property;
import de.ahrgr.animal.kohnert.asugen.property.TextProperty;

/**
 * @author ek
 */
public class GeneratorGUI extends JFrame implements 
    ActionListener, ListSelectionListener, TreeSelectionListener,
    GeneratorFactory{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 4637276464167824445L;
		static GeneratorGUI instance = null;
    public static final String[] generatorNames = {"Vigenere Encryption", "Vigenere Decryption"};
    public static final String[] generatorClasses = {"de.ahrgr.animal.kohnert.generators.VigenereEncode", "de.ahrgr.animal.kohnert.generators.VigenereDecode"};
    
    protected JPanel buttonpane;
    protected JButton btGenerate;
    protected JButton btExit;
    protected JList<String> propertyList;
    protected JScrollPane  propListSP;
    protected JLabel lbAnimationType;
    
    protected JPanel propertyPane;
    protected JButton btDefaultValue;
    protected JPanel dummyPane;
    protected JTextArea lbPropertyDescription;
    protected JSplitPane splitPane;
    protected PropertyEdit currentPropertyEdit = null;
    
    protected JMenuBar menuBar;
    protected JFileChooser fileChooser;
    
    protected JTree propertyTree;
    protected PropertyTreeNode propertyRootNode;
    
    protected File currentFile = null;
    protected Generator generator;
    Property[] properties;
    
    protected TextPropertyEdit textEdit = null;
    protected ColorPropertyEdit colorEdit = null;
    protected FormatedTextPropertyEdit formatedTextEdit = null;
    protected FontPropertyEdit fontEdit = null;
   
    protected AnimalLink animalLink = null;
    
    public GeneratorGUI() {        
        setTitle("Animal Generator");
        Container p = getContentPane();
        
        buttonpane = new JPanel();
        JButton aBtGenerate = new JButton("Save Animation");
        aBtGenerate.setActionCommand("save");
        aBtGenerate.addActionListener(this);
        JButton aBtExit = new JButton("Exit");        
        aBtExit.setActionCommand("exit");
        aBtExit.addActionListener(this);
        buttonpane.add(aBtGenerate);        
        buttonpane.add(aBtExit);
        lbAnimationType = new JLabel("Vigenere Animation");
        p.add(lbAnimationType, BorderLayout.NORTH);
        p.add(buttonpane, BorderLayout.SOUTH);
        
        propertyTree = new JTree();  
        propertyTree.getSelectionModel().setSelectionMode
        (TreeSelectionModel.SINGLE_TREE_SELECTION);

        propertyTree.addTreeSelectionListener(this);        
        
        /*propertyList = new JList();
        propertyList.addListSelectionListener(this);*/
        propListSP = new JScrollPane(propertyTree,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);            
        propertyPane = new JPanel();
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
                propListSP, propertyPane);
        splitPane.setDividerLocation(200);
        p.add(splitPane, BorderLayout.CENTER);
      //  p.add(propListSP, BorderLayout.WEST);
      //  p.add(propertyPane, BorderLayout.CENTER);
        
        propertyPane.setLayout(new BorderLayout());
        btDefaultValue = new JButton("set to default value");
        btDefaultValue.setActionCommand("defaultvalue");
        btDefaultValue.addActionListener(this);
        propertyPane.add(btDefaultValue, BorderLayout.SOUTH);
        
        dummyPane = new JPanel();
        //propertyPane.add(dummyPane, BorderLayout.CENTER);
        lbPropertyDescription = new JTextArea("no property selected");
        lbPropertyDescription.setBorder(new EmptyBorder(10,10,10,10));
        lbPropertyDescription.setEditable(false);
        lbPropertyDescription.setLineWrap(true);
        propertyPane.add(new JScrollPane(lbPropertyDescription), BorderLayout.NORTH);
        propertyPane.setBorder(new TitledBorder("no property"));
        propertyPane.setPreferredSize(new Dimension(400,500));
        
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        JMenuItem m;
        m = fileMenu.add("New");
        m.setMnemonic(KeyEvent.VK_N);
        m.setActionCommand("new");
        m.addActionListener(this);
        m = fileMenu.add("Load...");
        m.setMnemonic(KeyEvent.VK_L);
        m.setActionCommand("load");
        m.addActionListener(this);
        m = fileMenu.add("Save");
        m.setMnemonic(KeyEvent.VK_S);
        m.setActionCommand("save");
        m.addActionListener(this);
        m = fileMenu.add("Save as...");
        m.setMnemonic(KeyEvent.VK_A);
        m.setActionCommand("saveas");
        m.addActionListener(this);
        
    /*    JMenu toolsMenu = new JMenu("Animation");
        toolsMenu.setMnemonic(KeyEvent.VK_A);
        
        m = toolsMenu.add("show in Animal...");
        m.setMnemonic(KeyEvent.VK_S);
        m.setActionCommand("run");
        m.addActionListener(this);*/
        
        fileMenu.addSeparator();
        m = fileMenu.add("Exit");
        m.setMnemonic(KeyEvent.VK_E);
        m.setActionCommand("exit");
        m.addActionListener(this);
        
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        
        m = helpMenu.add("Animal website");
        m.setMnemonic(KeyEvent.VK_W);
        m.setActionCommand("www");
        m.addActionListener(this);
        
        helpMenu.addSeparator();
        m = helpMenu.add("About AnimalGenerator...");
        m.setMnemonic(KeyEvent.VK_A);
        m.setActionCommand("about");
        m.addActionListener(this);
        
        menuBar.add(fileMenu);
       // menuBar.add(toolsMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
        
        textEdit = new TextPropertyEdit();
        colorEdit = new ColorPropertyEdit();
        formatedTextEdit = new FormatedTextPropertyEdit();
        fontEdit = new FontPropertyEdit();        
        
        pack();
        setSize(600,500);
        
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new ASUFileFilter());
        
 //       testFrame = new JFrame("test");
  //      testFrame.setVisible(true);
    }
    
    public void setPropertyDescription(String s) {
  		// do nothing    	
    }
    
    public void setGenerator(Generator g) {
        properties = g.getProperties();
        int s = properties.length;
        String[] keys = new String[s];
        int i;
        for(i = 0; i< s; i++) keys[i] = properties[i].getKey();
        this.generator = g;
     //   propertyList.setListData(keys);
        
        propertyRootNode = new PropertyTreeNode(null, g.getGeneratorName());
        for(i = 0; i<s; i++) propertyRootNode.addProperty(properties[i], keys[i]);
        propertyTree.setModel(new DefaultTreeModel(propertyRootNode));
        
        if(currentPropertyEdit != null) propertyPane.remove(currentPropertyEdit);
        propertyPane.setBorder(new TitledBorder("No property selected"));
        lbPropertyDescription.setText("no property selected");
        propertyPane.validate();
    }
    

    public void newGenerator() {        
        GeneratorSelectionDialog d = new GeneratorSelectionDialog(generatorNames);
        int r = d.showModal();
        if(r < 0) return; // Kein Generator ausgewählt        
        Generator g = getGenerator(generatorClasses[r]);
        setGenerator(g);
    }
    
    @SuppressWarnings("unchecked")
    public Generator getGenerator(String className) {
    	//if("generators.Vigenere".equals(className)) return new Vigenere();
    	try {
    		Class<Generator> c = (Class<Generator>)Class.forName(className);
    		Generator g = c.newInstance();
    		return g;
    	} catch (InstantiationException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (IllegalAccessException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (ClassNotFoundException e) {
    		// TODO Auto-generated catch block
    		JOptionPane.showMessageDialog(this, 
    				"Sorry. Did not find generator class \""+className+"\"");            
    		e.printStackTrace();
    	}         
    	return null;
    }
    
    /**
     * die aktuell bearbeitete Editier-Komponente speichern lassen
     *
     */
    public void writeProperty() {
        if(currentPropertyEdit != null) currentPropertyEdit.writeProperty();
    }
    
    public void loadFile(File f) {
        PropertyParser p = new PropertyParser();
        try {
            Reader r = new FileReader(f);
            r = new EmbeddedPropertiesFilter(r);
            Generator g = p.parse(r, this);
            if(g != null) {
                setGenerator(g);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {        
        writeProperty();
        if(generator == null) {
            JOptionPane.showMessageDialog(this, 
                    "Please select a generator first");
            return;
        }
        if(animalLink == null) 
        {
            animalLink = new AnimalLink();            
        }
        //animalLink.loadAnimation(generator);
        JOptionPane.showMessageDialog(this, "Sorry. This feature is disabled in this version.");  
    }
    
    public void exit() {
  		// do nothing        
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
        if("exit".equals(arg0.getActionCommand()))
            System.exit(0);
        if("save".equals(arg0.getActionCommand()) || "saveas".equals(arg0.getActionCommand())) {
        	if(generator == null) return;
        	writeProperty();
        	boolean dialog = "saveas".equals(arg0.getActionCommand());
        	if(currentFile == null) dialog = true;
        	boolean generate = false;
        	if(dialog) {
        		int r = fileChooser.showSaveDialog(this);
        		if(r == JFileChooser.APPROVE_OPTION) {
        			generate = true;
        			currentFile = fileChooser.getSelectedFile();
        		}
        	} else generate = true;
        	if(generate) {
                try {
                    Writer w = new FileWriter(currentFile);
                    generator.generateScript(w);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this,
                        "IO error while generating Script:\n\n" + e,
                        "IO Error", JOptionPane.ERROR_MESSAGE);
                }
                
            }
        }
        if("new".equals(arg0.getActionCommand())) {
        	newGenerator();
        }
        if("load".equals(arg0.getActionCommand())) {
        	int r = fileChooser.showOpenDialog(this);
            if(r == JFileChooser.APPROVE_OPTION) {
              //  ASUEmbedFilter.test(fileChooser.getSelectedFile());
                loadFile(fileChooser.getSelectedFile());
            }        	
        }
        if("run".equals(arg0.getActionCommand())) 
            run();
        if("defaultvalue".equals(arg0.getActionCommand())) {
        	if(currentPropertyEdit != null) currentPropertyEdit.setToDefaultValue();
        }
        if("about".equals(arg0.getActionCommand())) {
            JOptionPane.showMessageDialog(this, 
                    "AnimalGenerator 1.0\n\nadd author information here", "About AnimalGenerator",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        if("www".equals(arg0.getActionCommand())) {
            JOptionPane.showMessageDialog(this,
                    "More information about Animal at\n"+
                    "\n"+
                    "http://www.algoanim.info/Animal2/", "Animal WWW", 
                    JOptionPane.INFORMATION_MESSAGE);
        }
            
    }
    
    public void setEditProperty(Property p) {
        if(currentPropertyEdit != null) {
            currentPropertyEdit.writeProperty();
            //testFrame.getContentPane().remove(currentPropertyEdit);
            propertyPane.remove(currentPropertyEdit);
        }
        propertyPane.setVisible(false);
        currentPropertyEdit = null;
        if(p instanceof TextProperty) {
             currentPropertyEdit = textEdit;
             textEdit.setProperty((TextProperty)p);
             //new TextPropertyEdit((TextProperty) p);
        }
        if(p instanceof ColorProperty) {
             currentPropertyEdit = colorEdit;
             colorEdit.setProperty((ColorProperty) p);
             //new ColorPropertyEdit((ColorProperty) p);
        }
        if(p instanceof FormatedTextProperty) {
            currentPropertyEdit = formatedTextEdit;
            formatedTextEdit.setProperty((FormatedTextProperty) p);
            //new FormatedTextPropertyEdit((FormatedTextProperty) p);
        }
        if(p instanceof EKFontProperty) {
        	currentPropertyEdit = fontEdit;
        	fontEdit.setProperty((EKFontProperty)p);
        }
        if(currentPropertyEdit != null) {
            //testFrame.getContentPane().add(currentPropertyEdit, BorderLayout.CENTER);
            //testFrame.validate();
            propertyPane.add(currentPropertyEdit, BorderLayout.CENTER);             
        }
        propertyPane.setBorder(new TitledBorder(p.getKey()));
        lbPropertyDescription.setText(p.getDescription());
        //propertyPane.validate();
        propertyPane.setVisible(true);
        
    }

    /* (non-Javadoc)
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent arg0) {
    	if(arg0.getValueIsAdjusting()) return; // nur letztes Ereignis verarbeiten

        int i = propertyList.getSelectedIndex();
        if(i >= 0) {
        	Property p = properties[i];
            setEditProperty(p);
        }
    }

    public static class ASUFileFilter extends javax.swing.filechooser.FileFilter {
		public boolean accept(File arg0) {
			if(arg0.isDirectory() || arg0.getName().endsWith(".asu")) return true;
			return false;
		}  
		
		public String getDescription() {
			return "Animal Script - Generator file";
		}
	}

    /* (non-Javadoc)
     * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
     */
    public void valueChanged(TreeSelectionEvent arg0) {
        PropertyTreeNode node = (PropertyTreeNode)
            propertyTree.getLastSelectedPathComponent();
        
        if (node == null) return;
        
        if (node.property != null) {
            setEditProperty(node.property);
        }
    }
}
