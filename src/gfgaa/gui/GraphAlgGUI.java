package gfgaa.gui;

import gfgaa.gui.components.SPanel;
import gfgaa.gui.others.JarFileLoader;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/** The GUI object of the project.
  *
  * @author S. Kulessa
  * @version 0.97
  */
public final class GraphAlgGUI extends JFrame {

    /**
   * 
   */
  private static final long serialVersionUID = 8955424210341510262L;

    /** Reference to the projects mainclass. */
    private GraphAlgController mainclass;

    /** Reference to the panels menubar. */
    private GraphAlgMenuBar menu;

    /** Reference to the mainpanel. */
    private SPanel mp;

    /** (Constructor)<br>
      * Creates a new GUI object.
      *
      * @param mainclass       Projects mainclass
      */
    public GraphAlgGUI(final GraphAlgController mainclass) {
        this.mainclass = mainclass;

        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - getSize().width) / 2,
                    (d.height - getSize().height) / 2);

        Container cont = getContentPane();

        mp = new EditGraphPanel(mainclass);
        cont.add(mp, BorderLayout.CENTER);

        menu = new GraphAlgMenuBar(mainclass);
        setJMenuBar(menu);

        this.changeLanguageSettings(mainclass.getLanguageSettings());

        String path = mainclass.getClass().getResource(
                            "MessageHandler.class").toString();
        path = path.substring(0, path.length() - 20);

        ImageIcon icon = JarFileLoader.loadImage(path + "Icons/icon.png");
        if (icon != null) {
            this.setIconImage(icon.getImage());
        }
    }

    /** (internal language method)<br>
      * Returns the current language settings.
      *
      * @return                 Language settings
      */
    public int getLanguageSettings() {
        return mainclass.getLanguageSettings();
    }

    /** (internal language method)<br>
      * Method to controll the language Settings of the components.
      *
      * @param languageFlag     Language identifier
      */
    protected void changeLanguageSettings(final int languageFlag) {
        menu.changeLanguageSettings(languageFlag);
        mp.changeLanguageSettings(languageFlag);

        String[] text = new String[2];
        text[0] = "Generator für Graph Algorithmen Animationen";
        text[1] = "Generator for Graph Algorithm Animations";
        super.setTitle(text[languageFlag]);
    }

    /** (internal language method) <br>
      * Method to controll the visibility of the panels.
      *
      * @param panelId      Array with the panels and subpanels
      *                     that should be displayed
      */
    protected void setTopLevelPanel(final int[] panelId) {
        mp.setTopLevelPanel(panelId);
    }

    /** (internal data method)<br>
      * Returns the used menubar.
      *
      * @return             Reference to the menubar
      */
    protected GraphAlgMenuBar getGraphAlgMenuBar() {
        return menu;
    }

/*+EXTENSION+METHODS++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** (internal extension method)<br>
      * Adds the given MenuItems to the menubar.
      *
      * @param compItems    Component MenuItems
      */
/*    protected void addAlgorithmusComponentMenuEntrys(
                       final JCheckBoxMenuItem[] compItems) {
        menu.addAlgorithmusComponentMenuEntrys(compItems);
    }

    *//** (internal extension method)<br>
      * Creates/Updates the Algorithm Menu Entries.
      *
      * @param db           Algorithm Database
      *//*
    protected void createAlgorithmMenu(final AlgoDataBase db) {
        menu.createAlgorithmMenu(db);
    }*/
}
