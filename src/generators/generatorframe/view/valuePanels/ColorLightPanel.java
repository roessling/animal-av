package generators.generatorframe.view.valuePanels;

import generators.generatorframe.controller.ColorButtonActionListener;

import java.awt.Color;
import java.awt.Dimension;





import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import animal.misc.ColoredSquare;

/**
 * 
 * @author Nora Wester
 *
 */
public class ColorLightPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ColorButtonActionListener listener;
	
	public ColorLightPanel(String key, Color value, boolean prim){
		
		super.setBackground(Color.white);
		
		
		setMinimumSize(new Dimension(500, 40));
		
		super.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JLabel label = new JLabel(key);
		label.setMinimumSize(new Dimension(100, 40));
		
		super.add(label);
		super.add(Box.createHorizontalStrut(5));
		
		JButton colorB = new JButton();
	//	colorB.setBackground(value);
		colorB.setIcon(new ColoredSquare(value));
		StringBuffer text = new StringBuffer();
		text.append("(").append(value.getRed());
		text.append(", ").append(value.getGreen()).append(", ");
		text.append(value.getBlue()).append(")");
		colorB.setText(text.toString());
		colorB.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		colorB.setMinimumSize(new Dimension(120, 30));
		colorB.setMaximumSize(new Dimension(120, 30));
		listener = new ColorButtonActionListener(prim, key);
		colorB.addActionListener(listener);
		
		super.add(colorB);
	}
	
	public void setPosition(int p){
		listener.setPosition(p);
	}
	
}
