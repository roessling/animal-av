package gfgaa.gui.graphs;

/** Communication interface<br>
  * This interface is designed for the communication between
  * an "Create node and edges" - panel and the GUI.
  *
  * @author S. Kulessa
  * @version 0.97c
  */
public interface KantenPanelInterface {

    /** (internal display method)<br>
      * Updates the x-axis and y-axis position fields.
      *
      * @param node        The node thats position has changed
      */
    void refreshNodePosition(final AbstractNode node);

    /** (internal display method)<br>
      * Updaes the components of the panel.
      */
    void refreshPanelComponents();
}
