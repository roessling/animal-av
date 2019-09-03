package generators.generatorframe.view.valuePanels;

import generators.generatorframe.controller.BoolFontActionListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * @author Nora Wester
 *
 */

public class BooleanFontLightPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int BOOLEAN = 0;
	public static final int FONT = 1;
	
	private BoolFontActionListener listener;
	
	public BooleanFontLightPanel(int type, String key, Object value, boolean prim){
		
		super.setBackground(Color.white);
		super.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JLabel label = new JLabel(key);
		
		super.add(label);
		super.add(Box.createHorizontalStrut(5));
		
		JComboBox<String> temp = new JComboBox<String>();
		temp.setMinimumSize(new Dimension(100, 20));
		temp.setMaximumSize(new Dimension(100, 20));
		if(type == BOOLEAN){
			temp.addItem("true || 1");
			temp.addItem("false || 0");
			if((Boolean)value){
				temp.setSelectedIndex(0);
			}else{
				temp.setSelectedIndex(1);
			}
		}else{
			temp.addItem(Font.MONOSPACED);
			temp.addItem(Font.SERIF);
			temp.addItem(Font.SANS_SERIF);
			temp.setSelectedItem(((Font) value).getName());
		}
		
		listener = new BoolFontActionListener(prim, type, key);
		temp.addActionListener(listener);
		super.add(temp);
	}
	
	public void setPosition(int p){
		listener.setPosition(p);
	}
	
}
