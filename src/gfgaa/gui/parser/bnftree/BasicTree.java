package gfgaa.gui.parser.bnftree;

import gfgaa.gui.parser.BNFNode;
import gfgaa.gui.parser.ParserTreeInterface;
import gfgaa.gui.parser.ParserUnit;
import gfgaa.gui.parser.event.Event_DirectedGraph;
import gfgaa.gui.parser.event.Event_EdgeAction;
import gfgaa.gui.parser.event.Event_NodeAction;
import gfgaa.gui.parser.event.Event_NodeTagCheck;
import gfgaa.gui.parser.event.Event_NodesRangeCheck;
import gfgaa.gui.parser.event.Event_ReadInt;
import gfgaa.gui.parser.event.Event_ReadMatrixRow;
import gfgaa.gui.parser.event.Event_ReadObereEcke;
import gfgaa.gui.parser.event.Event_ReadRowElement;
import gfgaa.gui.parser.event.Event_ReadStartKnoten;
import gfgaa.gui.parser.event.Event_ReadZielKnoten;
import gfgaa.gui.parser.event.Event_WeightedGraph;
import gfgaa.gui.parser.event.basic.Event_Basic_CreateEdge;
import gfgaa.gui.parser.event.basic.Event_Basic_CreateGraph;
import gfgaa.gui.parser.event.basic.Event_Basic_CreateNode;
import gfgaa.gui.parser.event.basic.Event_Basic_CreateObereEcke;
import gfgaa.gui.parser.event.basic.Event_Basic_CreateStartNode;
import gfgaa.gui.parser.event.basic.Event_Basic_CreateZielNode;
import gfgaa.gui.parser.event.basic.Event_Basic_LastEvent;

/** ParserTree class<br>
  * This class represents the BNF Notation to describe basic graphs.
  *
  * @author S. Kulessa
  * @version 0.95
  */
public final class BasicTree implements ParserTreeInterface {

    /** Graph mode root. */
    private BNFNode root1;

    /** Matrix mode root. */
    private BNFNode root2;
 

    /** (constructor)<br>
      * Creates a BNFTree to describe the basic graph bnf.
      */
    public BasicTree() {
    	
    	 root1 = new BNFNode(WORD, 1, "graph",
                 new Event_Basic_CreateGraph(ParserUnit.MODUS_READ_AS_GRAPH));
         root2 = new BNFNode(WORD, 1, "matrix",
                 new Event_Basic_CreateGraph(ParserUnit.MODUS_READ_AS_MATRIX));

         BNFNode knoten1 =
             new BNFNode(NUMBER, 3, "int", new Event_NodesRangeCheck());
         knoten1.newInstance(root1);
         knoten1.newInstance(root2);

         BNFNode knoten2 =
             new BNFNode(WORD, 2, "directed", new Event_DirectedGraph());
         knoten2.newInstance(knoten1);

         BNFNode knoten3 =
             new BNFNode(WORD, 1, "weighted", new Event_WeightedGraph());
         knoten3.newInstance(knoten1);
         knoten3.newInstance(knoten2);

         BNFNode knoten4 = new BNFNode(EOL, 2, "EOL");
         knoten4.newInstance(knoten1);
         knoten4.newInstance(knoten2);
         knoten4.newInstance(knoten3);
         knoten4.newInstance(knoten4);
         
         //Madieha
         
         BNFNode obereEckeKnoten = new BNFNode(WORD, 1, "graphcoordinates",new Event_ReadObereEcke());
         obereEckeKnoten.newInstance(knoten4);
         
         knoten1 = new BNFNode(WORD, 2, "at");
         knoten1.newInstance(obereEckeKnoten);
         
         knoten2= new BNFNode(NUMBER, 2, "int", new Event_ReadInt());
    	 knoten2.newInstance(knoten1);
    	     	 
    	 knoten3= new BNFNode(NUMBER, 2, "int", new Event_ReadInt(1));
    	 knoten3.newInstance(knoten2);
    	 
    	 
    	 BNFNode knotena = new BNFNode(EOL, 4, "EOL",new Event_Basic_CreateObereEcke());
    	 knotena.newInstance(knotena);
    	 knotena.newInstance(knoten3);
    	 knotena.newInstance(knoten2);	 	
 	 	 knotena.newInstance(knoten1);
 	 	 
         
         obereEckeKnoten.newInstance(knotena);
         
         //bouchra StartKnoten
         
         BNFNode startKnoten = new BNFNode(WORD, 1, "startknoten",new Event_ReadStartKnoten());
         startKnoten.newInstance(knotena);
         
         knoten1 = new BNFNode(WORD, 2, "char",new Event_NodeTagCheck());
         knoten1.newInstance(startKnoten);
            	 
    	 BNFNode knotenb = new BNFNode(EOL, 4, "EOL",new Event_Basic_CreateStartNode());
    	 knotenb.newInstance(knotenb); 
    	 knotenb.newInstance(knoten1);
    	 
         startKnoten.newInstance(knotenb);
         /*+*/
         
        
         //bouchra ZielKnoten
         
         BNFNode zielKnoten = new BNFNode(WORD, 1, "zielknoten",new Event_ReadZielKnoten());
         zielKnoten.newInstance(knotenb);
         
         
         knoten1 = new BNFNode(WORD, 1, "char",new Event_NodeTagCheck());
         knoten1.newInstance(zielKnoten);
            	 
    	 knoten4 = new BNFNode(EOL, 3, "EOL", new Event_Basic_CreateZielNode());
    	 knoten4.newInstance(knoten4);
    	 knoten4.newInstance(knoten1);
    	 
         zielKnoten.newInstance(knoten4);
        
         BNFNode nodeKnoten = new BNFNode(WORD, 1, "node",new Event_NodeAction());
         nodeKnoten.newInstance(knotena);
         nodeKnoten.newInstance(knotenb);
         nodeKnoten.newInstance(knoten4);

         knoten1 = new BNFNode(WORD, 3, "char", new Event_NodeTagCheck());
         knoten1.newInstance(nodeKnoten);

         knoten2 = new BNFNode(WORD, 1, "at");
         knoten2.newInstance(knoten1);

         knoten3 = new BNFNode(NUMBER, 1, "int", new Event_ReadInt());
         knoten3.newInstance(knoten2);

         knoten2 = new BNFNode(NUMBER, 2, "int", new Event_ReadInt(1));
         knoten2.newInstance(knoten3);

         knoten3 = new BNFNode(EOL, 5, "EOL", new Event_Basic_CreateNode());
         knoten3.newInstance(knoten1);
         knoten3.newInstance(knoten2);
         knoten3.newInstance(knoten3);

         nodeKnoten.newInstance(knoten3);

         BNFNode eofKnoten = new BNFNode(EOF, 0, "EOF",
                                         new Event_Basic_LastEvent());
         eofKnoten.newInstance(knoten1);
         eofKnoten.newInstance(knoten2);
         eofKnoten.newInstance(knoten3);

         /*+*/

         BNFNode edgeKnoten = new BNFNode(WORD, 1, "edge",
                                          new Event_EdgeAction());
         edgeKnoten.newInstance(knoten3);

         knoten1 = new BNFNode(WORD, 1, "char", new Event_NodeTagCheck(true));
         knoten1.newInstance(edgeKnoten);

         knoten2 = new BNFNode(WORD, 3, "char", new Event_NodeTagCheck(1, true));
         knoten2.newInstance(knoten1);

         knoten1 = new BNFNode(WORD, 1, "weight");
         knoten1.newInstance(knoten2);

         knoten4 = new BNFNode(NUMBER, 2, "int", new Event_ReadInt());
         knoten4.newInstance(knoten1);

         BNFNode knoten5 = new BNFNode(EOL, 3, "EOL",
                                       new Event_Basic_CreateEdge());
         knoten5.newInstance(knoten2);
         knoten5.newInstance(knoten4);
         knoten5.newInstance(knoten5);

         edgeKnoten.newInstance(knoten5);

         eofKnoten.newInstance(knoten2);
         eofKnoten.newInstance(knoten4);
         eofKnoten.newInstance(knoten5);

         /*+*/

         knoten1 = new BNFNode('[', 1, "[", new Event_ReadMatrixRow());
         knoten1.newInstance(knoten3);

         knoten2 = new BNFNode(NUMBER, 2, "int", new Event_ReadRowElement());
         knoten2.newInstance(knoten1);

         knoten4 = new BNFNode('|', 1, "|");
         knoten4.newInstance(knoten2);

         knoten2.newInstance(knoten4);

         knoten3 = new BNFNode(']', 2, "]");
         knoten3.newInstance(knoten2);

         knoten4 = new BNFNode(EOL, 3, "EOL");
         knoten4.newInstance(knoten3);
         knoten4.newInstance(knoten4);

         knoten1.newInstance(knoten4);

         eofKnoten.newInstance(knoten3);
         eofKnoten.newInstance(knoten4);
    }

    /** (parser method)<br>
      * Returns the root node of this parser tree.
      *
      * @return     Root node
      */
    public BNFNode getRoot() {
        return root1;
    }

    /** (parser method)<br>
      * Returns the second root node of this parser tree.
      *
      * @return     Second root node
      */
    public BNFNode getSecondRoot() {
        return root2;
    }
}
