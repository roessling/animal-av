package generators.generatorframe.view;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;


import animal.main.Animal;
import generators.generatorframe.controller.CategoryListSelectionListener;
import generators.generatorframe.controller.SearchKeyListener;
import generators.generatorframe.controller.ToolTipActionListener;
import generators.generatorframe.filter.FilterCoordinator;
import generators.generatorframe.view.image.GetIcon;
import translator.TranslatableGUIElement;
import translator.Translator;


/**
 * 
 * @author Nora Wester
 *
 */

public class Listing extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	TranslatableGUIElement trans;
	Translator translator;
	ToolTipActionListener tip;
	
	String[] category;
	
	JList<String> list;
	JTextField search;

  JLabel                    label;
  JPanel                    north;

  // new global

  private JPanel            s;
  private JPanel            labelInfo;
  private AbstractButton    searchButton;
  private AbstractButton    infoButton;

	public Listing(String[] category, TranslatableGUIElement trans) {
		// TODO Auto-generated constructor stub
		super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		super.setBackground(Color.white);
		this.category = category;
		this.trans = trans;
		tip = new ToolTipActionListener(ToolTipActionListener.SEARCH);
		translator = new Translator("GeneratorFrame", Animal.getCurrentLocale());

		setContent();

	}

	private void setContent() {
		
		//set same space on the top of the list
		super.add(Box.createVerticalStrut(10));
		
    north = new JPanel(new BorderLayout());
    
    north.setMaximumSize(new Dimension(160, 70));
    //original
		//north.setMaximumSize(new Dimension(140, 50));

    north.setBackground(Color.white);
		
    labelInfo = new JPanel(new BorderLayout());
		//labelInfo.setMaximumSize(new Dimension(120, 20));
    // labelInfo.setLayout(new BoxLayout(labelInfo, BoxLayout.X_AXIS));
    // <_____________________________________________________
    labelInfo.setBackground(Color.white);
		
    label = trans.generateJLabel("search");
    label.setPreferredSize(new Dimension(110, 20));
    label.setMaximumSize(new Dimension(110, 20));
		label.setOpaque(true);
		label.setBackground(Color.white);
		
    // labelInfo.add(Box.createRigidArea(new Dimension(10, 20)),
    // BorderLayout.LINE_START);
		
    JLabel buffLabel = new JLabel();
    buffLabel.setText(" ");

    JPanel buffPanel = new JPanel(new BorderLayout());
    buffPanel.setBackground(Color.white);
    buffPanel.add(label, BorderLayout.LINE_END);
    buffPanel.add(buffLabel, BorderLayout.LINE_START);

		
    // labelInfo.add(label, BorderLayout.LINE_START);
    labelInfo.add(buffPanel, BorderLayout.LINE_START);
	//	labelInfo.add(label, BorderLayout.CENTER);
				
		GetIcon getIcon = new GetIcon();
    
		ImageIcon icon = getIcon.createImageIcon();
    infoButton = trans.generateJButton("infoSearch");
		infoButton.setMaximumSize(new Dimension(20, 20));
		infoButton.setPreferredSize(new Dimension(20, 20));
		infoButton.setMinimumSize(new Dimension(20, 20));
		infoButton.setIcon(icon);
		infoButton.setBackground(Color.white);
		
		infoButton.addActionListener(tip);
		//Das Label wird nicht angezeigt und wird nur dafür benutzt, 
		//um mit Translater an den Text zu kommen
//		final JLabel labelS = trans.generateJLabel("infoSearchFrame");
//		
//		infoButton.addActionListener(new ActionListener(){
//
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				// TODO Auto-generated method stub
//			
//				new InfoFrame(infoButton.getText(), labelS.getText(), null);
//			}
//			
//		});

    buffLabel = new JLabel();
    buffLabel.setText(" ");

    buffPanel = new JPanel(new BorderLayout());
    buffPanel.setBackground(Color.white);
    buffPanel.add(infoButton, BorderLayout.LINE_START);
    buffPanel.add(buffLabel, BorderLayout.LINE_END);
    // s.add(search, BorderLayout.LINE_START);
    labelInfo.add(buffPanel, BorderLayout.LINE_END);
    // labelInfo.add(infoButton, BorderLayout.LINE_END);
		
    // labelInfo.add(Box.createHorizontalGlue());
		north.add(labelInfo, BorderLayout.NORTH);
		
//		JPanel p = new JPanel();
//		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
//		p.setBackground(Color.white);
		
		search = new JTextField();
    search.setMaximumSize(new Dimension(110, 30));
    search.setMinimumSize(new Dimension(110, 30));
    search.setPreferredSize(new Dimension(110, 30));
		search.setOpaque(true);
		//set a hint for the user
		search.setToolTipText(translator.translateMessage("tooltipSearchAll"));
		
		SearchKeyListener listen = new SearchKeyListener(FilterCoordinator.TEXT_ALL);
		
		//set Listener
		search.addKeyListener(listen);
		

		
    s = new JPanel(new BorderLayout());
		//s.setLayout(new BorderLayout());
    s.setMaximumSize(new Dimension(140, 40));
		s.setBackground(Color.white);
		
    buffLabel = new JLabel();
    buffLabel.setText(" ");

    buffPanel = new JPanel(new BorderLayout());
    buffPanel.setBackground(Color.white);
    buffPanel.add(search, BorderLayout.LINE_END);
    buffPanel.add(buffLabel, BorderLayout.LINE_START);
    // s.add(search, BorderLayout.LINE_START);
    s.add(buffPanel, BorderLayout.LINE_START);
		
    searchButton = trans.generateJButton("searchButton");
    ImageIcon searchIcon = getIcon.createSearchIcon();
    searchButton.setMaximumSize(new Dimension(25, 25));
    searchButton.setPreferredSize(new Dimension(25, 25));
    searchButton.setMinimumSize(new Dimension(25, 25));
    searchButton.setIcon(searchIcon);
    searchButton.setBackground(Color.white);
    
    searchButton.addActionListener(listen);
    
    buffLabel = new JLabel();
    buffLabel.setText(" ");

    buffPanel = new JPanel(new BorderLayout());
    buffPanel.setBackground(Color.white);
    buffPanel.add(searchButton, BorderLayout.LINE_START);
    buffPanel.add(buffLabel, BorderLayout.LINE_END);

    // s.add(searchButton, BorderLayout.LINE_END);
    s.add(buffPanel, BorderLayout.LINE_END);
	//	p.add(s);
//		p.add(Box.createVerticalGlue());
		
    // north.add(s, BorderLayout.CENTER);

    JPanel buff = new JPanel(new BorderLayout());
    // s.setLayout(new BorderLayout());
    buff.setMaximumSize(new Dimension(140, 10));
    buff.setBackground(Color.white);
    buffLabel = new JLabel();
    buffLabel.setText(" ");
    buff.add(buffLabel, BorderLayout.LINE_START);
    north.add(buff, BorderLayout.CENTER);
    north.add(s, BorderLayout.SOUTH);
		//north.add(Box.createGlue(), BorderLayout.SOUTH);
		super.add(north);
		
		//some space
	//	super.add(Box.createVerticalStrut(10));
		
		//set Panel in the middle containing the list
		JPanel middle = new JPanel();
		middle.setBackground(Color.white);
		middle.setLayout(new BoxLayout(middle, BoxLayout.X_AXIS));
		//set same space on the left of the list
	//	middle.add(Box.createHorizontalStrut(5));
		
		
		Arrays.sort(category);
		
		CategoryListSelectionListener listener = new CategoryListSelectionListener();
		
		list = trans.generateJStringList("categoryList", null, category, ListSelectionModel.SINGLE_SELECTION, listener, -1);
		
	//	list = new JList<String>(category);
	//	list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
	//	list.setToolTipText(translator.translateMessage("tooltipCategory"));
		
		//set Listener
	//	list.addListSelectionListener(new CategoryListSelectionListener());
		
		list.addMouseListener(listener);
		list.addKeyListener(listener);
		
		middle.add(list);
		
		//set same space on the right of the list
	//	middle.add(Box.createHorizontalStrut(10));
		middle.add(Box.createVerticalGlue());
		
		JPanel l = new JPanel();
		l.setLayout(new BoxLayout(l, BoxLayout.Y_AXIS));
		l.add(middle);
		l.add(Box.createVerticalGlue());
		l.setBackground(Color.WHITE);
		
		super.add(l);
		//set same space on the bottom of the list
		super.add(Box.createVerticalStrut(10));
		super.add(Box.createVerticalGlue());
	}
	
	
	public void clear(){
		int index = list.getSelectedIndex();
		list.removeSelectionInterval(index, index);
	}

  public void changeLocale() {
    // TODO Auto-generated method stub
    tip.changeLocale();
    translator.setTranslatorLocale(Animal.getCurrentLocale());
    search.setToolTipText(translator.translateMessage("tooltipSearchAll"));
    
  }

  /**
   * zooms the tab in
   * 
   * @param zoomIn
   *          if true zooms in, if flase zooms out
   */
  public void zoom(boolean zoomIn) {

    Font f1 = list.getFont();
    Font f2 = search.getFont();

    // Dimension dim = this.getSize();
    Dimension labelDim = new Dimension(0, 0);
    Dimension sDim = new Dimension(0, 0);
    Dimension searchDim = new Dimension(0, 0);
    Dimension liDim = new Dimension(0, 0);
    Dimension sBDim = new Dimension(0, 0);
    Dimension dim = this.getSize();


    if (label != null) {
      labelDim = label.getSize();
    }

    if (search != null) {
      searchDim = search.getSize();
    }

    if (s != null) {
      sDim = s.getSize();
    }

    if (labelInfo != null) {

      liDim = labelInfo.getSize();
    }
    if(searchButton != null) {
      sBDim = searchButton.getSize();
    }


    if (zoomIn) {
      if (f1.getSize() < 24)
        f1 = new Font(f1.getName(), f1.getStyle(), f1.getSize() + 2);
      if (f2.getSize() < 24)
        f2 = new Font(f2.getName(), f2.getStyle(), f2.getSize() + 2);
      // if (dim.width <= 220)
      // dim.setSize(dim.getWidth() + 20, dim.getHeight() + 20);

      if (labelDim.height <= 32)
        labelDim.setSize(labelDim.getWidth() + 10, labelDim.getHeight() + 10);
      if (searchDim.height <= 32)
        searchDim.setSize(searchDim.getWidth() + 4, searchDim.getHeight() + 2);

      if (sDim.height <= 34)
        sDim.setSize(sDim.getWidth() + 10, sDim.getHeight() + 2);

      if (liDim.height <= 34)
        liDim.setSize(liDim.getWidth() + 10, liDim.getHeight() + 2);

      if (sBDim.width <= 32)
        sBDim.setSize(sBDim.getWidth() + 2, sBDim.getHeight() + 2);

      if (dim.width <= 200)
        dim.setSize(dim.getWidth() + 10, dim.getHeight() + 10);

    } else {
      if (f1.getSize() > 10)
        f1 = new Font(f1.getName(), f1.getStyle(), f1.getSize() - 2);
      if (f2.getSize() > 10)
        f2 = new Font(f2.getName(), f2.getStyle(), f2.getSize() - 2);
      // if (dim.width >= 180)
      // dim.setSize(dim.getWidth() - 20, dim.getHeight() - 20);

      if (labelDim.width >= 100)
        labelDim.setSize(labelDim.getWidth() - 10, labelDim.getHeight() - 10);

      if (searchDim.width >= 100)
        searchDim.setSize(searchDim.getWidth() - 4, searchDim.getHeight() - 2);
      if (sDim.height >= 30)
        sDim.setSize(sDim.getWidth() - 10, sDim.getHeight() - 2);
      if (liDim.height >= 30)
        liDim.setSize(liDim.getWidth() - 10, liDim.getHeight() - 2);
      if (sBDim.width >= 20)
        sBDim.setSize(sBDim.getWidth() - 2, sBDim.getHeight() - 2);
      if (dim.width >= 160)
        dim.setSize(dim.getWidth() - 10, dim.getHeight() - 10);

    }

    list.setFont(f1);
    if (search != null) {
      search.setFont(f2);
      search.setPreferredSize(searchDim);
      search.setMaximumSize(searchDim);
      search.setSize(searchDim);
    }

    if (s != null) {
      if (sDim.getWidth() < this.getWidth())
        sDim.setSize(this.getWidth(), sDim.getHeight());
      s.setMaximumSize(sDim);
      s.setMinimumSize(sDim);
      s.setPreferredSize(sDim);
      s.setSize(sDim);
    }

    if (labelInfo != null) {
      if (liDim.getWidth() < this.getWidth())
        liDim.setSize(this.getWidth(), liDim.getHeight());
      labelInfo.setMaximumSize(liDim);
      labelInfo.setMinimumSize(liDim);
      labelInfo.setPreferredSize(liDim);
      labelInfo.setSize(liDim);
    }
    
    if (searchButton != null) {
      searchButton.setMaximumSize(sBDim);
      searchButton.setMinimumSize(sBDim);
      searchButton.setPreferredSize(sBDim);
      searchButton.setSize(sBDim);

    }

    if (infoButton != null) {
      infoButton.setMaximumSize(sBDim);
      infoButton.setMinimumSize(sBDim);
      infoButton.setPreferredSize(sBDim);
      infoButton.setSize(sBDim);

    }

    if (label != null) {
      label.setPreferredSize(labelDim);
      label.setMaximumSize(labelDim);
      label.setSize(labelDim);
      label.setFont(f2);

      if (tip != null) {
        if (tip.getInfoframe() != null)
          tip.getInfoframe().zoom(zoomIn);
      }


    }
    /*
     * if (north != null) { Dimension northDim = new Dimension(0, 0);
     * northDim.setSize(sDim.getWidth(), sDim.getHeight() +
     * labelDim.getHeight()); north.setPreferredSize(northDim);
     * north.setMaximumSize(northDim); north.setSize(northDim); }
     */
    // this.setSize(dim);
    this.repaint();

  }

}
