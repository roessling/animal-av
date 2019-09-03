package gfgaa.gui.graphs;

/** Communication interface<br>
  * This interface is designed for the communication between
  * an "Create a graph by editing the adjacency matrix" - panel
  * and the GUI.
  *
  * @author S. Kulessa
  * @version 0.97c
  */
public interface MatrixPanelInterface {

   /** (data method)<br>
     * Returns the adjacencymatrix representation of the
     * currently containend graph.
     *
     * @return      Adjacencymatrix
     */
   int[][] getAdjacencyMatrix();
}
