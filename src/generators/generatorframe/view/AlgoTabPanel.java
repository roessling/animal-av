package generators.generatorframe.view;



import generators.generatorframe.controller.ButtonActionListener;
import generators.generatorframe.view.image.GetIcon;

import java.awt.BorderLayout;
import java.awt.Color;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;





import javax.swing.AbstractButton;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;


import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import translator.TranslatableGUIElement;


/**
 * 
 * @author Nora Wester
 *
 */


public class AlgoTabPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//GetInfos algo;
	JTabbedPane tabbedPane;
	JComponent primitives;
	JComponent properties;
	JComponent description;
	JComponent script;
	
	ButtonActionListener listener;
	
	int width;
	boolean first = true;
	
	TranslatableGUIElement trans;
	JLabel ghost;
	
  // new global variables

  private AbstractButton    start;
  private AbstractButton    export;
  private AbstractButton    back;
  private JPanel            buttons;
  private int               zoomCounter      = 0;
  private JPanel            exportPanel;

	/**
	 * 
	 * @author Nora Wester
	 * 
	 */
	
	public AlgoTabPanel(int width, TranslatableGUIElement trans){
		super();

		super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		super.setBackground(Color.white);
		this.width = width;
		this.trans = trans;
		listener = new ButtonActionListener();
		ghost = trans.generateJLabel("description");

		
	}
	
	private void setContent(){
		((TabPanelDescription) description).setContent();
		tabbedPane.setSelectedIndex(0);
		((TabPanelPrimitives) primitives).setContent();
		((TabPanelProperties) properties).setContent();
		((TabPanelScript) script).setContent();

	}
	
	public void changeInfos(){
		if(first){
			init();
			first = false;
		}else{
			setContent();
		}	
	}
	
//	public void change(int index){
//		
//		((TabPanelDescription) description).setTextArea(index);
//		
//	}
	
	private void init() {
		// TODO Auto-generated method stub
		//init Tabs
		tabbedPane = new JTabbedPane();
		description = new TabPanelDescription(width, trans);
		tabbedPane.addTab(ghost.getText(), description);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		
//		JPanel white = new JPanel();
//		white.setBackground(Color.WHITE);
//		JScrollPane scroll = new JScrollPane(white, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
//				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//primitives = new TabPanelPrimitives(algo, width, scroll);
		
		
		primitives = new TabPanelPrimitives(width, trans);
		tabbedPane.addTab("Primitives", primitives);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		
		properties = new TabPanelProperties(width, trans);
		tabbedPane.addTab("Properties", properties);
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
		
		script = new TabPanelScript(width, trans);
    tabbedPane.addTab("AnimalScript", script);
    tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
//		JComponent save = new TabPanelSave(width);
//		tabbedPane.addTab("save", save);
//		tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
		
		tabbedPane.setTabPlacement(JTabbedPane.TOP);
		super.add(tabbedPane);
		
		//init three Buttons on a extra Panel
		//one for starting the animation
		//one to go back zu the list
		//one for saving the current generator
    buttons = new JPanel();
		buttons.setBackground(Color.white);
		
    buttons.setPreferredSize(new Dimension(width, 40));
    buttons.setMaximumSize(new Dimension(width, 40));

		buttons.setLayout(new BorderLayout());
		
		GetIcon get = new GetIcon();
		
    start = trans.generateJButton("start");
		start.setName("start");
		start.setHorizontalTextPosition(SwingConstants.LEFT);
		start.setIcon(get.createForwardIcon(true));
    start.setPreferredSize(new Dimension(120, 40));
		//set Listener
		start.addActionListener(listener);

//		buttons.add(start);
		
    back = trans.generateJButton("back");
		back.setName("back");
		back.setIcon(get.createBackIcon(true));
		//setListener
		back.addActionListener(new ActionListener(){

      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        goBack();
      }
		  
		});

//		buttons.add(back);

    exportPanel = new JPanel();
    exportPanel.setBackground(Color.white);
    exportPanel.setLayout(new BorderLayout());
   


    export = trans.generateJButton("export");
    export.setName("export");
    export.setIcon(get.createExportIcon());
    export.addActionListener(listener);

    JLabel ghostLabel1 = new JLabel();
    ghostLabel1.setText(
        "                            ");

    JLabel ghostLabel2 = new JLabel();
    ghostLabel2.setText(
        "                            ");
    exportPanel.add(ghostLabel1, BorderLayout.WEST);
    exportPanel.add(ghostLabel2, BorderLayout.EAST);
    exportPanel.add(export, BorderLayout.CENTER);

    
    buttons.add(exportPanel, BorderLayout.CENTER);

		buttons.add(back, BorderLayout.WEST);
	//	buttons.add(save);
	//	buttons.add(Box.createHorizontalStrut(20));
		buttons.add(start, BorderLayout.EAST);
		
		super.add(buttons);

    for (int i = 0; i < zoomCounter; i++) {
      /*
       * ((TabPanelDescription) description).zoom(true); ((TabPanelPrimitives)
       * primitives).zoom(true); ((TabPanelProperties) properties).zoom(true);
       * ((TabPanelScript) script).zoom(true);
       */
      this.zoom(true, true);
    }

    for (int i = 0; i > zoomCounter; i--) {/*
                                            * ((TabPanelDescription)
                                            * description).zoom(false);
                                            * ((TabPanelPrimitives)
                                            * primitives).zoom(false);
                                            * ((TabPanelProperties)
                                            * properties).zoom(false);
                                            * ((TabPanelScript)
                                            * script).zoom(false);
                                            */
      this.zoom(false, true);
    }
	}

	public void goBack(){
	  Component parent = getParent();
	  while(!(parent instanceof GeneratorFrame))
	    parent = parent.getParent();
  
	  ((GeneratorFrame)parent).goBack();
	}
	
	public void setTab(String name){
	  
//	  if(name.compareTo("script") == 0){
//	    tabbedPane.setSelectedIndex(1);
//	    return;
//	  }
	  if(name.compareTo("primitives") == 0){
	    tabbedPane.setSelectedIndex(1);
	    return;
	  }
	  if(name.compareTo("properties") == 0){
	    tabbedPane.setSelectedIndex(2);
	    return;
	  }
	  if(name.compareTo("descriptionT") == 0){
	    tabbedPane.setSelectedIndex(0);
	  }
	}
	
  public void changeLocale() {
    // TODO Auto-generated method stub
    if(tabbedPane != null){
      tabbedPane.setTitleAt(0, ghost.getText());
      ((TabPanelDescription) description).changeLocale();
      ((TabPanelPrimitives) primitives).changeLocale();
      ((TabPanelProperties) properties).changeLocale();
      ((TabPanelScript) script).setLocale();
    }
  }

  public void arrayChanged() {
    // TODO Auto-generated method stub
    ((TabPanelPrimitives) primitives).update();
  }
	
  /**
   * zooms the tab in
   * 
   * @param zoomIn
   *          if true zooms in, if flase zooms out
   */
  public void zoom(boolean zoomIn, boolean byPassZoomCounter) {
    Font fNull = this.getFont();
    Font f1;

    Dimension dimBut = new Dimension(0, 0);
    Dimension dimPane = new Dimension(0, 0);

    if (tabbedPane != null) {
      dimPane = tabbedPane.getSize();
      f1 = tabbedPane.getFont();
    }else {
      f1 =fNull;
    }
    
    if (buttons != null) {
      dimBut = buttons.getSize();
    }

    Dimension dim = this.getSize();

    if (zoomIn) {
      if (!byPassZoomCounter && zoomCounter < 6)
        zoomCounter++;
      if (f1.getSize() < 24)
          f1 = new Font(f1.getName(), f1.getStyle(), f1.getSize() + 2);


      if (dim.width <= 960)
        dim.setSize(dim.getWidth() + 20, dim.getHeight() + 20);
      if (dimBut.width <= 960)
        dimBut.setSize(dimBut.getWidth() + 52, dimBut.getHeight() + 5);
      if (dimPane.width <= 960)
        dimPane.setSize(dimPane.getWidth() + 20, dimPane.getHeight() + 20);

    } else {
      if (!byPassZoomCounter && zoomCounter > -1)
        zoomCounter--;

      if (f1.getSize() > 10)
        f1 = new Font(f1.getName(), f1.getStyle(), f1.getSize() - 2);
      

      if (dim.width >= 900)
        dim.setSize(dim.getWidth() - 20, dim.getHeight() - 20);
      if (dimBut.width >= 900)
        dimBut.setSize(dimBut.getWidth() - 52, dimBut.getHeight() - 5);
      if (dimPane.width >= 900)
      dimPane.setSize(dimPane.getWidth() - 20, dimPane.getHeight() - 20);
    }

    if (tabbedPane != null) {
      tabbedPane.setFont(f1);
      tabbedPane.setSize(dimPane);
      tabbedPane.repaint();
    }

    if (buttons != null) {
      if (dimBut.getWidth() < 641 + zoomCounter * 52)
        dimBut.setSize(641 + zoomCounter * 52, dimBut.getHeight());
      if (dimBut.getHeight() < 30 + zoomCounter * 5)
        dimBut.setSize(dimBut.getWidth(), 30 + zoomCounter * 5);
      buttons.setPreferredSize(dimBut);
      buttons.setMaximumSize(dimBut);
      buttons.setSize(dimBut);
      buttons.repaint();

    }

    
    if (start != null) {
      f1 = start.getFont();
      dimBut = start.getSize();
      if (zoomIn) {
        if (f1.getSize() < 24)
          f1 = new Font(f1.getName(), f1.getStyle(), f1.getSize() + 2);
        if (dimBut.getWidth() < 200)
          dimBut.setSize(dimBut.getWidth() + 15, dimBut.getHeight() + 5);

      } else {
        if (f1.getSize() > 10)
          f1 = new Font(f1.getName(), f1.getStyle(), f1.getSize() - 2);
        if (dimBut.getWidth() > 120)
          dimBut.setSize(dimBut.getWidth() - 15, dimBut.getHeight() - 5);
      }
      start.setFont(f1);
      if (dimBut.getWidth() < 120 + zoomCounter * 15)
        dimBut.setSize(120 + zoomCounter * 15, dimBut.getHeight());
      if (dimBut.getHeight() < 30 + zoomCounter * 5)
        dimBut.setSize(dimBut.getWidth(), 30 + zoomCounter * 5);
      start.setPreferredSize(dimBut);
      start.setSize(dimBut);
      exportPanel.setPreferredSize(dimBut);
      exportPanel.setSize(dimBut);

    }

    if (back != null) {
           back.setFont(f1);
      back.setPreferredSize(dimBut);
      back.setSize(dimBut);
    }

    if (export != null) {
      export.setFont(f1);
      if (dimBut.getWidth() < 120 + zoomCounter * 15)
        dimBut.setSize(120 + zoomCounter * 15, dimBut.getHeight());
      if (dimBut.getHeight() < 30 + zoomCounter * 5)
        dimBut.setSize(dimBut.getWidth(), 30 + zoomCounter * 5);
      export.setPreferredSize(dimBut);
      export.setSize(dimBut);
    }


    if (properties != null) {
      ((TabPanelProperties) properties).zoom(zoomIn, this.zoomCounter);
    }
    
    if (description != null) {
      ((TabPanelDescription) description).zoom(zoomIn, this.zoomCounter);
    }

  if(primitives!=null)
  {
      ((TabPanelPrimitives) primitives).zoom(zoomIn, this.zoomCounter);
  }
    if (script != null) {
      ((TabPanelScript) script).zoom(zoomIn);
    }
    this.setSize(dim);
    this.repaint();

  }

}
