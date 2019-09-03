import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import translator.TranslatableGUIElement;
import translator.Translator;

public class I18NDemo {
	public int nrTimes = 0; // how often was the button clicked?
	JMenuItem german, english;
  /**
   * create the I18NDemo instance
   * @param targetLocale the Locale used for the output
   */
	public I18NDemo(Locale targetLocale) {
		Translator translator = new Translator("guiI18N", targetLocale);
		buildGUI(translator);
	}
	
	public void buildGUI(final Translator translator) {
		// retrieve the GUI element builder
		TranslatableGUIElement guiBuilder = translator.getGenerator();
		
		// create the window itself with an I18N title
		JFrame aFrame = guiBuilder.generateJFrame("guiDemo");
		aFrame.getContentPane().setLayout(new BorderLayout());
		
		// create a JMenuBar
		JMenuBar menuBar = new JMenuBar();
		// generate the JMenu for this
		JMenu menu = guiBuilder.generateJMenu("fileMenu");
	
		// generate a menu item with parameters (key, useIconIfExists)
		JMenuItem exitItem = guiBuilder.generateJMenuItem("exitItem", true);
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			System.exit(0); // exit
		}});
		// add the item to the JMenu
		menu.add(exitItem);

		// add the menu to the menu bar and add this to the JFrame
    menuBar.add(menu);

    // NEW: toggle language in GUI
		// add a language menu
 		JMenu language = guiBuilder.generateJMenu("languageMenu");
		german = guiBuilder.generateToggleableJMenuItem("german", 
		    null, true, translator.getCurrentLocale().equals(Locale.GERMANY));
		german.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
		    translator.setTranslatorLocale(Locale.GERMANY);
		    german.setSelected(true);
		    english.setSelected(false);
		  }
		});
		language.add(german);

    english = guiBuilder.generateToggleableJMenuItem("english", 
        null, true, translator.getCurrentLocale().equals(Locale.US));
    german.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        translator.setTranslatorLocale(Locale.US);
        english.setSelected(true);
        german.setSelected(false);
     }
    });
    language.add(english);
    menuBar.add(language);
    
		aFrame.setJMenuBar(menuBar);

		JPanel infoPanel = guiBuilder.generateBorderedJPanel("infoPanel");
		aFrame.getContentPane().add(infoPanel, BorderLayout.CENTER);
		// translatable JLabel
		JLabel label = guiBuilder.generateJLabel("clickMe");
		infoPanel.add(label);
    // add the info button
		AbstractButton info = guiBuilder.generateJButton("clickButton");
		info.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.err.println(translator.translateMessage("pressedTimes", 
						String.valueOf(++nrTimes))); // show nr of button press
			}
		});
		infoPanel.add(info); // add the info panel
		
		JPanel exitPanel = // create an exit panel 
			guiBuilder.generateBorderedJPanel("exitPanel");
		aFrame.getContentPane().add(exitPanel, BorderLayout.SOUTH);
		AbstractButton exit = guiBuilder.generateJButton("exitButton");
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.err.println(translator.translateMessage("bye",
					String.valueOf(nrTimes)));
				System.exit(0);
				}			
		});
		exitPanel.add(exit);
		aFrame.pack();
		aFrame.setVisible(true);		
	}
	
	public static void main(String[] args) {
//	new I18NDemo(Locale.US);
		new I18NDemo(Locale.GERMANY);
	}
	
}
