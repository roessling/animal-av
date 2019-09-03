package animal.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import translator.AnimalTranslator;
import animal.gui.AnimalMainWindow;
import animal.gui.AnimationOverview;
import animal.gui.DrawWindow;

public class TimeLineWindow extends AnimalFrame implements
		ListSelectionListener {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -2979784584485924348L;

	private TimeLineEntry[] entries;

  private JList<Object> labelList;
  private JList<Object> labelListCopy;
  private Font              defaultFont      = new Font("Dialog", 0, 14);
  private JLabel            text;

	Animation anim;

	int currentIndex;

  public TimeLineWindow(Animal animalInstance, Animation a) {
		super(animalInstance, 
				AnimalConfiguration.getDefaultConfiguration().getProperties());
		setTitle(AnimalTranslator.translateMessage("tlwTitle"));
//		setTitle("Time Line Window");
		anim = a;

		labelList = AnimalTranslator.getGUIBuilder().generateJList("timeLineList",
				null, null, ListSelectionModel.SINGLE_SELECTION, this, -1);
    labelListCopy = AnimalTranslator.getGUIBuilder().generateJList("timeLineList",
        null, null, ListSelectionModel.SINGLE_SELECTION, this, -1);
		updateList(a);
		JScrollPane scrollPane = new JScrollPane(labelList);

    text = AnimalTranslator.getGUIBuilder().generateJLabel("timeLineList.label",
        null);
		getContentPane().add(
        text, BorderLayout.NORTH);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		pack();
	}

	public void updateList(Animation a) {
		Vector<TimeLineEntry> headers = new Vector<TimeLineEntry>(30);
		if (a == null) {
			TimeLineEntry entry = new TimeLineEntry("start", 1);
      labelList.setListData(new TimeLineEntry[] { entry });
      labelListCopy.setListData(new TimeLineEntry[] { entry });
			return;
		}
		AnimationListEntry[] listEntries = a.getAnimatorList();
		for (int i = 0; i < listEntries.length; i++)
			if (listEntries[i].mode == AnimationListEntry.STEP
					&& listEntries[i].link.getLinkLabel() != null
					&& listEntries[i].link.getLinkLabel().length() > 0)
				headers.addElement(new TimeLineEntry(
						listEntries[i].link.getLinkLabel(), listEntries[i].link.getStep()));
		entries = new TimeLineEntry[headers.size()];
		headers.copyInto(entries);
    labelList.setListData(entries);
    labelListCopy.setListData(entries);
		repaint();
	}

	public void valueChanged(ListSelectionEvent e) {
    int targetElement = -1;
    if (e.getSource() == labelListCopy && !e.getValueIsAdjusting()) {
      Animal.get();
      targetElement = labelListCopy.getSelectedIndex();
    }
		if (e.getSource() == labelList && !e.getValueIsAdjusting()) {
			Animal.get();
      targetElement = labelList.getSelectedIndex();
		}
    if (targetElement > -1) {
      labelList.setSelectedIndex(targetElement);
      labelListCopy.setSelectedIndex(targetElement);
      int targetStep = entries[targetElement].getStep();
      DrawWindow dw = AnimalMainWindow.WINDOW_COORDINATOR
          .getDrawWindow(false);
      if (dw != null && dw.isVisible())
        dw.setStep(targetStep);
      AnimationWindow animationWindow = AnimalMainWindow.WINDOW_COORDINATOR
          .getAnimationWindow(false);
      if (animationWindow != null)
        animationWindow.setStep(targetStep, true);
      AnimationOverview ao = AnimalMainWindow.WINDOW_COORDINATOR
          .getAnimationOverview(false);
      if (ao != null && ao.isVisible())
        ao.setStep(targetStep, true);
    }
	}
	
	
	public JList<Object> getLabelListCopy(){
	  return labelListCopy;
	}

  /**
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public void zoom(boolean zoomIn) {

    Dimension dim = this.getSize();

    if (zoomIn) {
      if (defaultFont.getSize() < 24) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() + 2);
      }
      if (dim.getWidth() < 1000) {
        dim.setSize(dim.getWidth() + 40, dim.getHeight() + 40);
      }

    } else {
      if (defaultFont.getSize() > 10) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() - 2);
      }
      if (dim.getWidth() > 200) {
        dim.setSize(dim.getWidth() - 40, dim.getHeight() - 40);
      }



    }

    
    if (labelList != null) {
      labelList.setFont(defaultFont);
      setFont(labelList.getComponents());
      
    }

    if (labelListCopy != null) {
      labelListCopy.setFont(defaultFont);
      setFont(labelListCopy.getComponents());
    }

    if (text != null)
      text.setFont(defaultFont);


    this.setSize(dim);

  }

  public void setFont(Component[] comp) {
    // System.out.println("setFileChooserFont : " + comp.length);
    for (int i = 0; i < comp.length; i++) {
      if (comp[i] instanceof Container)
        setFont(((Container) comp[i]).getComponents());
      try {
        comp[i].setFont(defaultFont);
      } catch (Exception e) {
      }
    }
  }



}
