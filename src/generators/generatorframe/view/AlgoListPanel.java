package generators.generatorframe.view;

import java.awt.BorderLayout;
import java.awt.Color;


import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import animal.main.Animal;

import translator.Translator;


/**
 * 
 * @author Nora Wester
 *
 */

public class AlgoListPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	private TranslatableGUIElement trans;
	private Translator translator;
	
	JLabel introOne;
	JLabel introTwo;
	JLabel introThree;
  JLabel                    introFour;

  JPanel                    headerPane;
  JLabel                    header;
	
  int                       generatorNumber;
	
	
	
	public AlgoListPanel(int generatorNumber) {
		// TODO Auto-generated constructor stub
		super(new BorderLayout());
		
		this.generatorNumber = generatorNumber;
		this.translator = new Translator("GeneratorFrame", Animal.getCurrentLocale());
		super.setBackground(Color.white);
		setContent(generatorNumber);
	}

	private void setContent(int generatorNumber) {
		// TODO Auto-generated method stub
    headerPane = new JPanel();
		headerPane.setLayout(new BoxLayout(headerPane, BoxLayout.Y_AXIS));
		headerPane.setBackground(Color.WHITE);
		
		headerPane.add(Box.createVerticalStrut(20));
		
    header = new JLabel(" Generator Framework");
		header.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
		headerPane.add(header);
		
		//space
		headerPane.add(Box.createRigidArea(new Dimension(0, 40)));
		
		//some initial text
		introOne = new JLabel(translator.translateMessage("algoListOne"));
		introOne.setBackground(Color.blue);
		headerPane.add(introOne);
		
		
		introTwo = new JLabel(getTextForNum());
		headerPane.add(introTwo);
//		JLabel one = trans.generateJLabel("algoListTwoOne");
//		labelPane.add(one);
//		//TODO number of generators calculating out of ANIMAL
//		JLabel two = new JLabel(Integer.toString(generatorNumber));
//		labelPane.add(two);
//	
//		JLabel three = trans.generateJLabel("algoListTwoTwo");
//		labelPane.add(three);
//		headerPane.add(labelPane);
		introThree = new JLabel(translator.translateMessage("algoListThree"));
		headerPane.add(introThree);
		introFour = new JLabel(translator.translateMessage("algoListFour"));
		headerPane.add(introFour);
		
		//space
		headerPane.add(Box.createVerticalStrut(20));
		
		super.add(headerPane, BorderLayout.CENTER);

		
//		JPanel pane = new JPanel();
//		pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
//		pane.setMaximumSize(new Dimension(100, 50));
//		pane.setBackground(Color.white);
//		
//		super.add(pane, BorderLayout.CENTER);
		super.add(Box.createHorizontalStrut(30), BorderLayout.WEST);
		

	}

	private String getTextForNum(){
	  StringBuffer text = new StringBuffer();
    text.append(translator.translateMessage("algoListTwoOne"));
    text.append(" ");
    text.append(generatorNumber);
    text.append(" ");
    text.append(translator.translateMessage("algoListTwoTwo"));
    
    return text.toString();
	}
	
	public void changeLocale(){
	  translator.setTranslatorLocale(Animal.getCurrentLocale());
	  introOne.setText(translator.translateMessage("algoListOne"));
	  introTwo.setText(getTextForNum());
	  introThree.setText(translator.translateMessage("algoListThree"));
	  introFour.setText(translator.translateMessage("algoListFour"));
	}

  /**
   * zooms the listing
   * 
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public void zoom(boolean zoomIn) {

    Font f = introOne.getFont();
    Font f1 = header.getFont();
    Dimension dimHead = new Dimension(0, 0);
    // Dimension dimH = new Dimension(0, 0);

    if (headerPane != null)
      dimHead = headerPane.getSize();

    /*
     * if (header != null) dimH = header.getSize();
     */

    if (zoomIn) {
      if (f.getSize() < 24)
        f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);

      if (f1.getSize() < 38)
        f1 = new Font(f1.getName(), f1.getStyle(), f1.getSize() + 2);
      if (dimHead.getWidth() <= 1000) {
        dimHead.setSize(dimHead.getWidth() + 20, dimHead.getHeight() + 20);
      }
      /*
       * if (dimH.getWidth() <= 1000) { dimH.setSize(dimH.getWidth() + 20,
       * dimH.getHeight() + 20); }
       */

    } else {
      if (f.getSize() > 10)
        f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);
      if (f1.getSize() > 20)
        f1 = new Font(f1.getName(), f1.getStyle(), f1.getSize() - 2);
      if (dimHead.getWidth() >= 740) {
        dimHead.setSize(dimHead.getWidth() - 20, dimHead.getHeight() - 20);
      }
      /*
       * if (dimHead.getWidth() >= 740) { dimH.setSize(dimH.getWidth() - 20,
       * dimH.getHeight() - 20); }
       */
    }
    introOne.setFont(f);// .setSize(introOne.getSize().getWidth()+100,introOne.getSize().getHeight()+100);
    introTwo.setFont(f);
    introThree.setFont(f);
    introFour.setFont(f);
    header.setFont(f1);
    if (headerPane != null) {
      headerPane.setSize(dimHead);
    }
    /*
     * if (header != null) { header.setSize(dimH); }
     */

  }

}
