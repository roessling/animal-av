package generators.generatorframe.loading;

import generators.generatorframe.view.valuePanels.*;

import java.awt.Color;
import java.awt.Font;




import javax.swing.JPanel;

import algoanim.primitives.Graph;

/**
 * 
 * @author Nora Wester
 *
 */

public class ValuePanelLoader {


	
	public static JPanel getLightPanel(Object value, String label, int position){
		
		JPanel pane;
		
		if(value instanceof Boolean){
			pane = new BooleanFontLightPanel(BooleanFontLightPanel.BOOLEAN, label, value, false);
			((BooleanFontLightPanel)pane).setPosition(position);
			return pane;
		}
		
		if(value instanceof Font){
			pane = new BooleanFontLightPanel(BooleanFontLightPanel.FONT, label, value, false);
			((BooleanFontLightPanel)pane).setPosition(position);
			return pane;
		}
		
		if(value instanceof Color){
			pane = new ColorLightPanel(label, (Color)value, false);
			((ColorLightPanel) pane).setPosition(position);
			return pane;
		}
		
		if(value instanceof String){
			pane = new StringIntLightPanel(StringIntLightPanel.STRING, label, value, false);
			((StringIntLightPanel) pane).setPosition(position);
			return pane;
		}
		
		if(value instanceof Integer){
			pane = new StringIntLightPanel(StringIntLightPanel.INT, label, value, false);
			((StringIntLightPanel) pane).setPosition(position);
			return pane;
		}
		
		if(value instanceof Double){
			pane = new StringIntLightPanel(StringIntLightPanel.DOUBLE, label, value, false);
			((StringIntLightPanel) pane).setPosition(position);
			return pane;
		}
		
		return new JPanel();
	}
	
	public static JPanel getLightPanel(Object value, String label){
		
		if(value instanceof Boolean){
			return new BooleanFontLightPanel(BooleanFontLightPanel.BOOLEAN, label, value, true);
		}
		
		if(value instanceof Font){
			return new BooleanFontLightPanel(BooleanFontLightPanel.FONT, label, value, true);
		}
		
		if(value instanceof Color){
			return new ColorLightPanel(label, (Color)value, true);
		}
		
		if(value instanceof String){
			return new StringIntLightPanel(StringIntLightPanel.STRING, label, value, true);
		}
		
		if(value instanceof Integer){
			return new StringIntLightPanel(StringIntLightPanel.INT, label, value, true);
		}
		
		if(value instanceof Double){
			return new StringIntLightPanel(StringIntLightPanel.DOUBLE, label, value, true);
		}
		
		if(value instanceof Graph){
			return new GraphPanel(value, label);
		}
		
		if(value instanceof String[][])
			return new MatrixLightPanel(true, value, label);
		
		if(value instanceof int[][])
			return new MatrixLightPanel(false, value, label);
		
		if(value instanceof String[])
			return new ArrayLightPanel(true, value, label);
		
		if(value instanceof int[])
			return new ArrayLightPanel(false, value, label);

		//System.out.println(value.getClass().getSimpleName());
		JPanel wrong = new JPanel();
		wrong.setBackground(Color.BLUE);
		return wrong;
	}
}
