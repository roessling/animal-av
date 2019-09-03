package generators.generatorframe.view;



import generators.generatorframe.store.GetInfos;
import generators.generatorframe.view.valuePanels.PropertyItemPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;










import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import translator.TranslatableGUIElement;


public class TabPanelProperties extends JPanel {

	/**
	 * 
	 */
	
	GetInfos algo;
	int width;
	JSplitPane split;
	NonePanel none;
	JLabel label;
	JList<String> list;
	JPanel center = new JPanel();
  private NamePanel back;
  private JScrollPane scrollP;
  private JLabel            propertiesL;
	
	private static final long serialVersionUID = 1L;

  // new global variables

  private ButtonPanel       buttonPanel;

	/**
	 * 
	 * @author Nora Wester
	 * 
	 */
	
	public TabPanelProperties(int width, TranslatableGUIElement trans) {
		// TODO Auto-generated constructor stub
		algo = GetInfos.getInstance();
		this.width = width;
		
		setMinimumSize(new Dimension(width, 200));
		setBackground(Color.white);
		setLayout(new BorderLayout());
		
		back = new NamePanel(algo.getGeneratorName());
//		label = new JLabel(algo.getName());
//		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		
//		JPanel labelPane = new JPanel();
//		labelPane.setBackground(Color.white);
//		labelPane.setMinimumSize(new Dimension(width, 20));
//		labelPane.setMaximumSize(new Dimension(width, 20));
//		
//		labelPane.add(label);
		
		add(back, BorderLayout.NORTH);
		
		
		JPanel labelListPane = new JPanel();
		labelListPane.setBackground(Color.WHITE);
		labelListPane.setLayout(new BoxLayout(labelListPane, BoxLayout.Y_AXIS));
		
    propertiesL = new JLabel("Properties");
		propertiesL.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		labelListPane.add(propertiesL);
		//System.out.println(propertiesL.getSize().getWidth() + "size");
		
		//some space
		labelListPane.add(Box.createVerticalStrut(10));
		
//		props = algo.getProps();
		split = new JSplitPane();

		list = trans.generateJStringList("propList", null, algo.getPropsNameSet(), ListSelectionModel.SINGLE_SELECTION, 
		    new ListSelectionListener(){
		    
		  @Override
      public void valueChanged(ListSelectionEvent arg0) {
        // TODO Auto-generated method stub
        if(!arg0.getValueIsAdjusting()){
          String selected = list.getSelectedValue();
          if(selected != null){
            setContent(selected, list.getSelectedIndex());
          }
        }
      }
		  
		}, -1);
		list.setLayoutOrientation(JList.VERTICAL);
		
		//set Listener
//		list.addListSelectionListener(new ListSelectionListener(){
//
//			@Override
//			public void valueChanged(ListSelectionEvent arg0) {
//				// TODO Auto-generated method stub
//				if(!arg0.getValueIsAdjusting()){
//					String selected = list.getSelectedValue();
//					setContent(selected, list.getSelectedIndex());
//				}
//			}
//			
//		});
		
		JPanel listPane = new JPanel();
		listPane.setLayout(new BorderLayout());
		listPane.setBackground(Color.white);
		listPane.add(labelListPane, BorderLayout.NORTH);

		listPane.add(list, BorderLayout.CENTER);
		JPanel space = new JPanel();
		space.setBackground(Color.WHITE);
		space.add(Box.createRigidArea(new Dimension(10,10)));
		listPane.add(space, BorderLayout.EAST);
	
		scrollP = new JScrollPane(listPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollP, BorderLayout.WEST);
		//split.setResizeWeight(0.5);
		
		none = new NonePanel(false);
	//	none.setBackground(Color.WHITE);
		split.setRightComponent(none);
		split.setLeftComponent(null);
	
		add(split, BorderLayout.CENTER);

    buttonPanel = new ButtonPanel("primitives", "", trans);
    add(buttonPanel, BorderLayout.SOUTH);
  
		//System.out.println(list.getSize().getWidth() + " vorvorher");
		split.setEnabled(false);
	}
	
	private void setContent(String selected, int position) {
		// TODO Auto-generated method stub
		center = new PropertyItemPanel(selected, position);
		JScrollPane scroll = new JScrollPane(center, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		int p = split.getDividerLocation();
		split.setRightComponent(scroll);
		
		split.setEnabled(true);
		split.setDividerLocation(p);
		split.setEnabled(false);

	}

	public void setContent() {
		// TODO Auto-generated method stub
		//label.setText(algo.getName());
	 
	  back.setLabel(algo.getGeneratorName());
		changeTableContent(algo.getPropsNameSet());
	
	}
	
	private void changeTableContent(String[] names) {
		// TODO Auto-generated method stub	   
    split.setEnabled(true);

    list.setListData(names);
    
 
    split.setRightComponent(none);

    split.setEnabled(false);
		
	}
	
	public void changeLocale(){
	  if(center instanceof PropertyItemPanel){
	    ((PropertyItemPanel)center).changeLocale();
	  }
	  
	//  back.localChanged();
	  none.localChanged();
	}
	
  /**
   * zooms the tab in
   * 
   * @param zoomIn
   *          if true zooms in, if flase zooms out
   */

  public void zoom(boolean zoomIn, int zoomCounter) {


    Font f;
    if (label != null) {
      f = label.getFont();
      if (zoomIn) {
        if (f.getSize() < 24)
          f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      } else {
        if (f.getSize() > 10)
          f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      }
      label.setFont(f);
    }

    if (list != null) {
      f = list.getFont();
      if (zoomIn) {
        if (f.getSize() < 24)
          f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      } else {
        if (f.getSize() > 10)
          f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      }
      list.setFont(f);
    }

    if (propertiesL != null) {
      f = propertiesL.getFont();
      if (zoomIn) {
        if (f.getSize() < 24)
          f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      } else {
        if (f.getSize() > 10)
          f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      }
      propertiesL.setFont(f);
    }

    if (center != null) {
      f = center.getFont();
      if (zoomIn) {
        if (f.getSize() < 24)
          f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      } else {
        if (f.getSize() > 10)
          f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      }
      center.setFont(f);
    }
    if (split != null) {
      f = split.getFont();
      if (zoomIn) {
        if (f.getSize() < 24)
          f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      } else {
        if (f.getSize() > 10)
          f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      }
      split.setFont(f);
    }

    if (back != null) {
      back.zoom(zoomIn);

    }

    if (none != null) {
      none.zoom(zoomIn);

    }

    Dimension dim = this.getSize();

    if (zoomIn) {

      if (dim.getWidth() <= 980) {
        dim.setSize(dim.getWidth() + 20, dim.getHeight() + 20);
      }
    } else {

      if (dim.getWidth() >= 260) {
        dim.setSize(dim.getWidth() - 20, dim.getHeight() - 20);
      }

    }

    if (buttonPanel != null) {
      buttonPanel.zoom(zoomIn, zoomCounter);
    }
    this.setSize(dim);
    this.repaint();

  }
}
