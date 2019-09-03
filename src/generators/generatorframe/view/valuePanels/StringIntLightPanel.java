package generators.generatorframe.view.valuePanels;


import generators.generatorframe.controller.TextFieldKeyListener;
import generators.generatorframe.view.image.GetIcon;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import translator.Translator;
import animal.main.Animal;

/**
 * 
 * @author Nora Wester
 *
 */

public class StringIntLightPanel extends JPanel implements Translatable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int STRING = 0;
	public static final int INT = 1;
	public static final int DOUBLE = 2;

  private JTextField temp;
  private JTextArea tempA;
	private TextFieldKeyListener fieldListener;
	
	private JButton button;
	private Translator translator = new Translator("GeneratorFrame", Animal.getCurrentLocale());;
	
	public StringIntLightPanel(int type, String key, Object value, boolean prim){
		
		super.setBackground(Color.white);
		super.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JLabel label = new JLabel(key);
		
		super.add(label);
		super.add(Box.createHorizontalStrut(5));
		
		temp = new JTextField();
    temp.setMinimumSize(new Dimension(100, 30));
    temp.setMaximumSize(new Dimension(100, 30));
		if(type == INT){
			temp.setText(Integer.toString((Integer)value));
		}else{
			if(type == DOUBLE){
				temp.setText(Double.toString((Double) value));
			}else{
				temp.setText((String) value);
			}
		}
		fieldListener = new TextFieldKeyListener(type, prim, key);
		temp.addKeyListener(fieldListener);
		
		button = new JButton(translator.translateMessage("changeV"));
		GetIcon get = new GetIcon();
		button.setIcon(get.createRefreshIcon());
		button.setToolTipText(translator.translateMessage("changeVToolTip"));
		button.addActionListener(fieldListener);
		
		if(type == STRING){
		  String text = (String) value;
		  text = text.replace("\\n", System.getProperty("line.separator"));
      tempA = new JTextArea(text);
      tempA.setMinimumSize(new Dimension(100, 30*2));
      tempA.setMaximumSize(new Dimension(100, 30*2));
      tempA.addKeyListener(fieldListener);
      tempA.setBackground(Color.WHITE);
      JScrollPane scrollPane = new JScrollPane(tempA);
      scrollPane.setMinimumSize(new Dimension(100, 30*2));
      scrollPane.setMaximumSize(new Dimension(100, 30*2));
      super.add(scrollPane);
		}else {
	    super.add(temp);
		}
		
		super.add(button);
	}
	
	public void setPosition(int p){
		fieldListener.setPosition(p);
	}
	
	public void changeLocale(){
	  translator.setTranslatorLocale(Animal.getCurrentLocale());
	  button.setText(translator.translateMessage("changeV"));
	  button.setToolTipText(translator.translateMessage("changeVToolTip"));
	}
	
}
