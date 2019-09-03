package generators.generatorframe.view.valuePanels;

//import generators.generatorframe.store.GetInfos;
import gfgaa.gui.GraphAlgController;
import gfgaa.gui.GraphScriptPanel;
import gfgaa.gui.others.LanguageInterface;
import gfgaa.gui.parser.GraphWriter;
import algoanim.primitives.Graph;

import java.awt.Color;





import javax.swing.JPanel;

/**
 * 
 * @author Nora Wester
 *
 */

public class GraphPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public GraphPanel(Object value, String name){
		
		 // GetInfos algo = GetInfos.getInstance();
	    GraphWriter writer = new GraphWriter((Graph)value);
		  StringBuffer script = writer.getScript();
		
	    GraphScriptPanel parts = new GraphScriptPanel(new GraphAlgController(LanguageInterface.LANGUAGE_ENGLISH));
	    
	    
	    try {
	      parts.showGraphData(script.toString());
	    } catch (Exception e) {
	      throw new IllegalArgumentException(script.toString() 
	   		   + " is not a valid value for a Graph!");
	    }
	    
	    super.add(parts);
	    super.setBackground(Color.WHITE);
	      
	}

}
