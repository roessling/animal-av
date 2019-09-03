package gfgaa.gui;

import gfgaa.gui.components.SPanel;
import gfgaa.gui.graphs.GraphEntry;
import gfgaa.gui.graphs.basic.CreateBasicGraphPanel;
import gfgaa.gui.graphs.basic.KantenPanel;
import gfgaa.gui.graphs.basic.MatrixPanel;
import gfgaa.gui.others.PanelManager;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

/** This panel contains the three subpanels to edit the graph and
  * the PreviewPanel to show the graph.
  *
  * @author S. Kulessa
  * @version 0.92b
  */
public final class EditGraphPanel extends SPanel {

    /**
   * 
   */
  private static final long serialVersionUID = -3169622283996991032L;

    /** Reference to the projects mainclass. */
    private GraphAlgController mainclass;

    /** SplitPane of this panels. */
    private JSplitPane split;
    
    /** (constructor)<br>
      * Creates a new EditGraphPanel Object and adds the three
      * subpanels (CreateGraphPanel, KantenPanel & MatrixPanel) to it.
      *
      * @param mainclass        Reference to the projects mainclass
      */
    public EditGraphPanel(final GraphAlgController mainclass) {
        setLayout(null);
        this.mainclass = mainclass;
        this.mainclass.addPanel(PanelManager.PANEL_EDITGRAPH, this);
        add(createSplitPane());
        
    }

    private SPanel kanten;
    private SPanel matrix;
    private SPanel vorlage;

    public void changeLanguageSettings(final int languageFlag) {
        if (kanten != null) {
            kanten.changeLanguageSettings(languageFlag);
            matrix.changeLanguageSettings(languageFlag);
            vorlage.changeLanguageSettings(languageFlag);
        }
        super.changeLanguageSettings(languageFlag);
    }

    public void refreshPanelComponents() {

        GraphEntry entry = mainclass.getGraphDatabase().getSelectedEntry();
        kanten = entry.createKantenPanel();
        matrix = entry.createMatrixPanel();
        vorlage = entry.createCreateGraphPanel();

        int flag = mainclass.getLanguageSettings();

        input.removeAll();
        input.add(tabTitels[flag][0], vorlage);
        input.add(tabTitels[flag][1], kanten);
        input.add(tabTitels[flag][2], matrix);
    }

/*+CONSTRUCTION+METHODS+++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** (panel construction method)<br>
      * Creates a split pane.
      *
      * @return     JSplitPane
      */
    private JSplitPane createSplitPane() {
        PreviewPanel previewPanel = new PreviewPanel(mainclass);
        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                previewPanel, createInputTab());
        split.setBounds(20, 15, 750, 485);
        split.setEnabled(false);
        setStaticSplitEnabled(false);

        return split;
    }

    public void paint(final Graphics g) {
        Dimension size = this.getSize();

        split.setLocation((size.width - 750) / 2,
                          (size.height - 485) / 2);

        super.paint(g);
    }

    private JTabbedPane input;
    private String[][] tabTitels;

    /** (panel construction method)<br>
      * Creates a tabbed pane for the three subpanels.
      *
      * @return     JTabbedPane
      */
    private JTabbedPane createInputTab() {
        tabTitels = new String[][] {{"Eigenschaften",
                                     "Bearbeiten",
                                     "Matrix"},
                                    {"Attributes",
                                     "Edit",
                                     "Matrix"}};

        input = new JTabbedPane();
        input.setMinimumSize(new Dimension(290, 500));
        this.setTabbedPane(input, tabTitels);

        SPanel kanten  = new KantenPanel(mainclass);
        SPanel matrix  = new MatrixPanel(mainclass);
        SPanel vorlage = new CreateBasicGraphPanel(mainclass);

        int flag = mainclass.getLanguageSettings();
        input.add(tabTitels[flag][0], vorlage);
        input.add(tabTitels[flag][1], kanten);
        input.add(tabTitels[flag][2], matrix);

        add(kanten, false);
        add(matrix, false);
        add(vorlage, false);

        return input;
    }

/*+INTERNAL+USED+METHODS++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    /** (internal display method)<br>
      * En/disabels the split component.
      *
      * @param enabled      True | False
      */
    public void setStaticSplitEnabled(final boolean enabled) {
        split.setEnabled(enabled);
    }

    /** (internal display method)<br>
      * Minimize the size of the left panel component.
      */
    public void resetSplit() {
        split.setDividerLocation(450);
    }

    /** (internal display method)<br>
      * Maximizes the size of the left panel component.
      */
    public void setMaxSplit() {
        split.setDividerLocation(336);
    }
}
