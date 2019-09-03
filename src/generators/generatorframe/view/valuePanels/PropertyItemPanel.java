package generators.generatorframe.view.valuePanels;

import generators.generatorframe.loading.ValuePanelLoader;
import generators.generatorframe.store.GetInfos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * 
 * @author Nora Wester
 *
 */



public class PropertyItemPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	GetInfos algo;
	private Vector<Translatable> list = new Vector<Translatable>();

	public PropertyItemPanel(String selected, int position) {
		// TODO Auto-generated constructor stub
		//this.props = props;
		algo = GetInfos.getInstance();
		
		Vector<String> itemName = algo.getPropItemNames(selected);
		super.setLayout(new BorderLayout());
		super.setBackground(Color.white);
		//super.add(Box.createVerticalStrut(30));
		JLabel label = new JLabel(selected);
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		super.add(label, BorderLayout.NORTH);
		super.add(Box.createHorizontalStrut(30), BorderLayout.WEST);
		//super.add(Box.createVerticalStrut(10));
		JPanel spaceP = new JPanel();
		spaceP.setBackground(Color.white);
		//spaceP.setLayout(new BoxLayout(spaceP, BoxLayout.Y_AXIS));
		int number = 0;
		super.add(spaceP, BorderLayout.CENTER);
		algo.setAnimationProperty(selected);
		for(int i=0; i<itemName.size(); i++){
			if(algo.editable(itemName.get(i))){
				String name = itemName.get(i);
				if(name.compareTo("name") != 0){
				  number++;
				  Object value = algo.getPropValue(name);
				
				  JPanel pane = ValuePanelLoader.getLightPanel(value, name, position);
				  if(pane instanceof Translatable){
				    list.add((Translatable)pane);
				  }
				  spaceP.add(pane);
				}
			}
		}
		spaceP.setLayout(new GridLayout(number, 1));
	}

	public void changeLocale(){
	  for(int i=0; i<list.size(); i++){
	    list.get(i).changeLocale();
	  }
	}

}
