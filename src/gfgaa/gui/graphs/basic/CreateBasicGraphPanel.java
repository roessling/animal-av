package gfgaa.gui.graphs.basic;

import gfgaa.gui.GraphAlgController;
import gfgaa.gui.MessageHandler;
import gfgaa.gui.components.SComponent;
import gfgaa.gui.graphs.CreateGraphPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * Panel class<br>
 * Panel contains elements to change the attributes of the graph and to save,
 * load, delete and transfer a graph.
 * 
 * @author S. Kulessa
 * @version 0.95
 */
public final class CreateBasicGraphPanel extends CreateGraphPanel {

  /**
   * 
   */
  private static final long serialVersionUID = -3893378280870054814L;

  /**
   * (constructor)<br>
   * Creates a new CreateGraphPanel.
   * 
   * @param mainclass
   *          Reference to the projects mainclass
   */
  public CreateBasicGraphPanel(final GraphAlgController mainclass) {
    super(mainclass);

    add(createClearButton());
    changeLanguageSettings(mainclass.getLanguageSettings());
  }

  /**
   * (Panel construction method) Creates a button that allows the deleting of
   * the current used graph.
   * 
   * @return Delete button
   */
  protected JButton createClearButton() {
    clearGraph = new JButton();
    clearGraph.setBounds(45, 340, 200, 25);
    add(new SComponent(clearGraph, new String[] { "Neuen Graphen erstellen",
        "Create a new Graph" }, new String[] { "Löscht den aktuellen Graph.",
        "Deletes the current used graph." }));

    clearGraph.addActionListener(new ActionListener() {

      public void actionPerformed(final ActionEvent e) {
        if (mainclass.showUserMessage(MessageHandler.MESSAGE_DATA_MAY_BE_LOST)) {

          mainclass.getGraphDatabase().setGraph(
              new Graph(gerichtet.isSelected(), gewichtet.isSelected()));

          refreshTransferSaveButton();

          // mainclass.getModel().recreateAnimation();
          mainclass.repaint();
        }
      }
    });
    return clearGraph;
  }
}
