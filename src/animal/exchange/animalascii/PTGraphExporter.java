/*
 * PTGraphExporter.java
 * Exporter for PTStringArrays.
 *
 * Created on 27 September 2006
 *
 * @author Pierre Villette
 */

package animal.exchange.animalascii;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.PrintWriter;

import animal.graphics.PTGraph;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTText;

public class PTGraphExporter extends PTGraphicObjectExporter {
    public void exportTo (PrintWriter pw, PTGraphicObject ptgo) {
	// write out the information of the super object
	PTGraph graph = (PTGraph) ptgo;
	pw.print (graph.getFileVersion ());
	pw.print (" object ");
	pw.print (graph.getNum (false));
	
	// write this object's information
	pw.print (" Graph size ");
	pw.print (graph.length);
	
	pw.print (" origin (");
	Point p = graph.getOrigin ();
	pw.print (p.x);
	pw.print (", ");
	pw.print (p.y);
	pw.print (")");
	
	pw.print (" nodes {");
	for (int i = 0; i < graph.length; i++) {
	    pw.print ("\"");
	    pw.print (PTText.escapeText (graph.getValueNode (i)));
	    pw.print ("\" ");
	    Point p1 = graph.getPTTextNode(i).getLocation();
	    pw.print ("(");
	    pw.print (p1.x);
	    pw.print (", ");
	    pw.print (p1.y);
	    pw.print (") ");
	    Point p2 = graph.getNode(i).getCenter();
	    pw.print ("(");
	    pw.print (p2.x);
	    pw.print (", ");
	    pw.print (p2.y);
	    pw.print (") ");
	    pw.print (graph.getNode(i).getRadius());
	    if (i < graph.length - 1) {
	    	pw.print (" ");
	    }
	}
	
	pw.print("} edges {");
	for (int i = 0; i < graph.length; i++) {
		for (int j = 0; j < graph.length; j++) {
			pw.print ("\"");
			pw.print(PTText.escapeText(graph.getValueEdge(i,j)));
			pw.print ("\"");
			if ((i != graph.length - 1) || (j < graph.length -1)){
				pw.print(" ");
			}
		}
	}
	pw.print("} ");
	
	Font nodeFont = graph.getNodeFont ();
	pw.print ("nodeFont (");
	pw.print (nodeFont.getName ());
	pw.print (", ");
	pw.print (nodeFont.getSize ());
	pw.print (") ");
	
	Font edgeFont = graph.getEdgeFont ();
	pw.print ("edgeFont (");
	pw.print (edgeFont.getName ());
	pw.print (", ");
	pw.print (edgeFont.getSize ());
	pw.print (") ");
	
	Color color = graph.getBGColor ();
	pw.print ("bgColor (");
	pw.print (color.getRed ());
	pw.print (", ");
	pw.print (color.getGreen ());
	pw.print (", ");
	pw.print (color.getBlue ());
	pw.print (") ");
	
	color = graph.getNodeFontColor ();
	pw.print ("nodeFontColor (");
	pw.print (color.getRed ());
	pw.print (", ");
	pw.print (color.getGreen ());
	pw.print (", ");
	pw.print (color.getBlue ());
	pw.print (") ");
	
	color = graph.getEdgeFontColor ();
	pw.print ("edgeFontColor (");
	pw.print (color.getRed ());
	pw.print (", ");
	pw.print (color.getGreen ());
	pw.print (", ");
	pw.print (color.getBlue ());
	pw.print (") ");
	
	color = graph.getOutlineColor ();
	pw.print ("outlineColor (");
	pw.print (color.getRed ());
	pw.print (",");
	pw.print (color.getGreen ());
	pw.print (",");
	pw.print (color.getBlue ());
	pw.print (") ");
	
	color = graph.getHighlightColor ();
	pw.print ("highlightColor (");
	pw.print (color.getRed ());
	pw.print (", ");
	pw.print (color.getGreen ());
	pw.print (", ");
	pw.print (color.getBlue ());
	pw.print (") ");
	
	color = graph.getElemHighlightColor ();
	pw.print ("elementHighlightColor (");
	pw.print (color.getRed ());
	pw.print (", ");
	pw.print (color.getGreen ());
	pw.print (", ");
	pw.print (color.getBlue ());
	pw.print (")");
	
	
	if (graph.hasDirection()){
		pw.print(" direction");
	}
	
	if (graph.hasWeight()){
		pw.print(" weight");
	}
	
	if (graph.indicesShown ()) {
		pw.print (" showIndices");
	}
	
	pw.print (" depth ");
	pw.println (graph.getDepth ());
    }
}