package gfgaa.gui.others;

import gfgaa.gui.components.SPanel;

/** Panel Manager<br>
  * Contains all main panels of the gui.
  *
  * @author Simon Kulessa
  * @version 1.0
  */
public class PanelManager {

    /** Internal array position of the MainPanel. */
    public static final int PANEL_MAIN = 0;

    /** Internal array position of the EditGraphPanel. */
    public static final int PANEL_EDITGRAPH = 1;

    /** Internal array position of the PreviewPanel. */
    public static final int PANEL_PREVIEW = 2;

    /** Internal array position of the CreateGraphPanel. */
    public static final int PANEL_CREATEGRAPH = 3;

    /** Internal array position of the KantenPanel. */
    public static final int PANEL_KANTEN = 4;

    /** Internal array position of the MatrixPanel. */
    public static final int PANEL_MATRIX = 5;

    /** Internal array position of the GraphScriptPanel. */
    public static final int PANEL_GRAPHSCRIPT = 6;

    /** Internal array position of the ParserPanel. */
    public static final int PANEL_PARSER = 7;

    /** Internal array position of the BNFDescriptionPanel. */
    public static final int PANEL_BNFDESCRIPTION = 8;

    /** Reference to every used panel. */
    private SPanel[] allMyPanels;

    /** (constructor)<br>
      * Creates a new panel manager.
      */
    public PanelManager() {
    	this.allMyPanels = new SPanel[9];
    }

    /** (internal data method)<br>
      * Adds the speciefied panel to the main class.
      *
      * @param pos          internal panel identification
      * @param panel        new panel
      */
    public void addPanel(final int pos, final SPanel panel) {
        this.allMyPanels[pos] = panel;
    }

    /** (internal data method)<br>
      * Returns the specified panel.
      *
      * @param pos          internal panel identification
      * @return             specified panel
      */
    public SPanel getPanel(final int pos) {
        return this.allMyPanels[pos];
    }
}
