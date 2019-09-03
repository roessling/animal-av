package generators.generatorframe.view;

import generators.generatorframe.loading.ValuePanelLoader;
import generators.generatorframe.store.GetInfos;
import generators.generatorframe.view.valuePanels.Notifyable;
import generators.generatorframe.view.valuePanels.Translatable;

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





public class TabPanelPrimitives extends JPanel{

	/**
	 * 
	 * @author Nora Wester
	 * 
	 */
	private static final long serialVersionUID = 1L;

	GetInfos algo;
	int width;
	JSplitPane split;
	NonePanel none;
	JLabel label;
	JList<String> list;
	JPanel center = new JPanel();
  JLabel                    primitiveL;
	
  private NamePanel back;
  // new global variables

  private ButtonPanel       buttonPanel;
	
	public TabPanelPrimitives(int width, TranslatableGUIElement trans) {
		// TODO Auto-generated constructor stub
		algo = GetInfos.getInstance();
		
		this.width = width;
		
		none = new NonePanel(true);
		//none.setBackground(Color.WHITE);
		setMinimumSize(new Dimension(width, 200));
		setBackground(Color.white);
		setLayout(new BorderLayout());
		
		back = new NamePanel(algo.getGeneratorName());
//		label = new JLabel(algo.getName());
//		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
//		
//		JPanel labelPane = new JPanel();
//		labelPane.setBackground(Color.white);
//		labelPane.setMinimumSize(new Dimension(width, 20));
//		labelPane.setMaximumSize(new Dimension(width, 20));
//		
//		labelPane.add(label);
		
		add(back, BorderLayout.NORTH);
		
		split = new JSplitPane();
		
		JPanel labelListPane = new JPanel();
		labelListPane.setBackground(Color.WHITE);
		labelListPane.setLayout(new BoxLayout(labelListPane, BoxLayout.Y_AXIS));
    primitiveL = new JLabel("Primitives");
		primitiveL.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		labelListPane.add(primitiveL);
		
		//some space
		labelListPane.add(Box.createVerticalStrut(10));
		
//		prim = algo.getPrims();
//		
//		String[] keys = new String[prim.size()];
//		keys = prim.keySet().toArray(keys);
		
		//list = new JList<String>(algo.getPrimNameSet());
		list = trans.generateJStringList("primList", null, algo.getPrimNameSet(), ListSelectionModel.SINGLE_SELECTION, 
		    new ListSelectionListener(){

          @Override
          public void valueChanged(ListSelectionEvent arg0) {
            // TODO Auto-generated method stub
            if(!arg0.getValueIsAdjusting()){
              String selected = list.getSelectedValue();
              if(selected != null){
                setContent(selected);
              }
            }
          }
		  
		}, -1);
	//	list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		
		//set Listener
//		list.addListSelectionListener(new ListSelectionListener(){
//
//			@Override
//			public void valueChanged(ListSelectionEvent arg0) {
//				// TODO Auto-generated method stub
//				if(!arg0.getValueIsAdjusting()){
//					String selected = list.getSelectedValue();
//					if(selected != null){
//						setContent(selected);
//					}
//				}
//			}
//
//			
//		});
		
		
		JPanel listPane = new JPanel();
		listPane.setLayout(new BorderLayout());
		listPane.setBackground(Color.white);
		listPane.add(labelListPane, BorderLayout.NORTH);
		listPane.setMinimumSize(new Dimension(width, 50));
		listPane.setMaximumSize(new Dimension(width, 50));
		listPane.add(list, BorderLayout.CENTER);
		JPanel space = new JPanel();
		space.setBackground(Color.WHITE);
		space.add(Box.createRigidArea(new Dimension(10,10)));
		listPane.add(space, BorderLayout.EAST);
	
		JScrollPane scrollP = new JScrollPane(listPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollP, BorderLayout.WEST);
		//split.setLeftComponent(scrollP);
;
		split.setRightComponent(none);
		split.setLeftComponent(null);
		split.setEnabled(false);
	
		add(split, BorderLayout.CENTER);
		
    // new global variables

    buttonPanel = new ButtonPanel("descriptionT", "properties", trans);
    add(buttonPanel, BorderLayout.SOUTH);
  
		
	}
	
	private void setContent(String selected) {
		// TODO Auto-generated method stub
		JPanel middle = new JPanel();
		middle.setLayout(new BorderLayout());
		
		JPanel desPanel = new JPanel(new BorderLayout());
		desPanel.setBackground(Color.white);
		desPanel.add(Box.createVerticalStrut(50), BorderLayout.NORTH);
		desPanel.add(Box.createHorizontalStrut(30), BorderLayout.WEST);
		String description = algo.getPrimDescription(selected)==null ? "" : algo.getPrimDescription(selected);
		JLabel labelDes = new JLabel("<html><head></head><body><b>Description: </b>"+description+"</body></html>");
		desPanel.add(labelDes, BorderLayout.CENTER);
		middle.add(desPanel, BorderLayout.PAGE_START);
		middle.add(Box.createHorizontalStrut(30), BorderLayout.WEST);
		middle.add(Box.createVerticalStrut(50), BorderLayout.SOUTH);
		
		center = ValuePanelLoader.getLightPanel(algo.getPrimValue(selected), selected);
		
		middle.setBackground(Color.WHITE);
		middle.add(center, BorderLayout.CENTER);
		
		JScrollPane scroll = new JScrollPane(middle, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		int position = split.getDividerLocation();
		split.setRightComponent(scroll);
		
		split.setEnabled(true);
		split.setDividerLocation(position);
		split.setEnabled(false);

	}
	
	public void setContent(){
	//	label.setText(algo.getName());
		back.setLabel(algo.getGeneratorName());
		changeTableContent(algo.getPrimNameSet());
	}


	private void changeTableContent(String[] names) {
		// TODO Auto-generated method stub
	  split.setEnabled(true);
	  //split.setRightComponent(null);
    list.setListData(names);
    
  //  int p = split.getDividerLocation();
    
    split.setRightComponent(none);
    //split.setLeftComponent(list);
    
   // split.setDividerLocation(p);
//    list.setListData(names);

    split.setEnabled(false);
		
	}
	
	public void changeLocale(){
	  if(center instanceof Translatable){
	    ((Translatable) center).changeLocale();
	  }
	  
	 // back.localChanged();
	  none.localChanged();
	}

  public void update() {
    // TODO Auto-generated method stub
     if(center instanceof Notifyable){
       String name = ((Notifyable)center).getElementName();
       ((Notifyable)center).resetField(algo.getPrimValue(name));
     }
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

    if (primitiveL != null) {
      f = primitiveL.getFont();
      if (zoomIn) {
        if (f.getSize() < 24)
          f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      } else {
        if (f.getSize() > 10)
          f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      }
      primitiveL.setFont(f);
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
