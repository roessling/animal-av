package animal.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Stack;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;

import translator.AnimalTranslator;
import animal.animator.Animator;
import animal.editor.Editor;
import animal.main.Animal;
import animal.main.AnimalFrame;
import animal.main.Animation;
import animal.main.AnimationListEntry;
import animal.main.AnimationWindow;
import animal.main.Link;
import animal.main.ObjectPanel;
import animal.misc.XProperties;

/**
 * the window that displays all Animators in sequential order, allows to insert
 * and delete Animators, Steps etc.
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 1.0 13.07.1998
 */
public class AnimationOverview extends AnimalFrame implements ActionListener,
    ListSelectionListener {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = -2479876886704332851L;

  JPopupMenu popup;

  JMenuItem deleteItem, editItem, appendStepItem, prependStepItem;
  JMenuItem cutItem, copyItem, pasteItem;

  /** current Animation */
  private Animation animation;

  /** the JList that contains all AnimatorInfos */
  JList<String> animatorList;

  /** ObjectPanel that contains all AnimatorEditors */
  private ObjectPanel animatorPanel;

  /** the content of the list. */
  private AnimationListEntry[] info = new AnimationListEntry[] {};

  /** for determining need to update when Animation has changed */
  private int lastChange = -1;

  private JToolBar buttonToolBar;

  private JToolBar animatorToolBar;

  private Stack<Object> undoStack = new Stack<Object>();
  private Font                 defaultFont      = new Font("Dialog", 0, 14);
  private int                  height           = 20;
  private int                  width            = 20;

  /**
   * constructs the AnimationOverview. Initialization is done with
   * <code>init</code>.
   * 
   * @see #init
   */
  public AnimationOverview(Animal animalInstance, XProperties properties) {
    super(animalInstance, properties);
  }

  /**
   * initializes the AnimationOverview by adding the button panel, the
   * ObjectPanel and the List.
   */
  public void init() {
    super.init();
    workContainer().setLayout(new BorderLayout(0, 0));

    animatorList = new JList<String>();
    animatorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    animatorList.addMouseListener(new AnimationOverviewMouseAdapter(this));

    animatorList.addListSelectionListener(this);
    JScrollPane scrollPane = new JScrollPane(animatorList);
    workContainer().add(BorderLayout.CENTER, scrollPane);

    popup = AnimalTranslator.getGUIBuilder().generateJPopupMenu(
        "GUIResources.objectActions");
    buttonToolBar = new JToolBar();
    buttonToolBar.getAccessibleContext().setAccessibleName(
        AnimalTranslator.translateMessage("stepOps"));
    buttonToolBar.setFloatable(true);

    AnimalTranslator.getGUIBuilder().insertToMenuAndToolBar("prependStep",
        null, this, null, popup, buttonToolBar);
    AnimalTranslator.getGUIBuilder().insertToMenuAndToolBar("appendStep", null,
        this, null, popup, buttonToolBar);
    AnimalTranslator.getGUIBuilder().insertToToolBar("refresh", null, this,
        null, buttonToolBar);
    AnimalTranslator.getGUIBuilder().insertToMenuAndToolBar("editAnimator",
        null, this, null, popup, buttonToolBar);
    AnimalTranslator.getGUIBuilder().insertToMenuAndToolBar("deleteAnimator",
        null, this, null, popup, buttonToolBar);
//    AnimalTranslator.getGUIBuilder().insertToMenuAndToolBar("cutItem", 
//        null, this, null, popup, buttonToolBar);
//    AnimalTranslator.getGUIBuilder().insertToMenuAndToolBar("copyItem", 
//        null, this, null, popup, buttonToolBar);
//    AnimalTranslator.getGUIBuilder().insertToMenuAndToolBar("pasteItem", 
//        null, this, null, popup, buttonToolBar);
    JLabel l = new JLabel("ToolBar");
    l.setLabelFor(buttonToolBar);

    installAnimatorToolBar();

    workContainer().add(BorderLayout.NORTH, animatorToolBar);
    workContainer().add(BorderLayout.SOUTH, buttonToolBar);
    setTitle(AnimalTranslator.translateMessage("animOverview"));

    setAnimation(Animation.get());
    setProperties(props);
    editItem = new JMenuItem("Edit", KeyEvent.VK_E);
    editItem.addActionListener(this);
    popup.add(editItem);
    popup.add(new JSeparator());
  }

  public void installAnimatorToolBar() {
    // getAnimatorPanel().installEditors(Animal.get());
    animatorToolBar = getAnimatorPanel().getToolBar();
    animatorToolBar.getAccessibleContext().setAccessibleName("Animators");
    animatorToolBar.setFloatable(true);
    repaint();
  }

  public ObjectPanel getAnimatorPanel() {
    if (animatorPanel == null)
      animatorPanel = new ObjectPanel(animal, this, props, false);
    return animatorPanel;
  }

  /**
   * as in AnimationWindow, don't care for changes until the window is made
   * visible
   * 
   * @see AnimationWindow#setVisible
   */
  public void setVisible(boolean b) {
    super.setVisible(b);
    if (b) {
      initList(true);
      valueChanged(null);
    }
  }

  /**
   * reads the AnimationOverview's bounds
   */
  void setProperties(XProperties properties) {
    setBounds(properties.getIntProperty("AnimationOverview.x", 50), properties
        .getIntProperty("AnimationOverview.y", 50), properties.getIntProperty(
        "AnimationOverview.width", 320), properties.getIntProperty(
        "AnimationOverview.height", 200));
  }

  /**
   * writes the AnimationOverviews bounds
   */
  public void getProperties(XProperties properties) {
    Rectangle b = getBounds();
    if (b.width + 11 == properties.getIntProperty("AnimationOverview.width",
        320)
        && b.height + 8 == properties.getIntProperty(
            "AnimationOverview.height", 200)) {
      b.width += 11;
      b.height += 8;
    }
    properties.put("AnimationOverview.x", b.x);
    properties.put("AnimationOverview.y", b.y);
    properties.put("AnimationOverview.width", b.width);
    properties.put("AnimationOverview.height", b.height);
  }

  public void initList() {
    lastChange = -1;
    initList(true);
  }

  /**
   * initializes the list by calling all Animators' <code>.toString</code>-method.
   * This is done when an Animator or Link has changed or is inserted or
   * deleted.
   * 
   * @param getNewList
   *          if true, the Animation is asked for a new list. This is needed if
   *          an Animator or Link is inserted or deleted or a Link is changed(as
   *          Link time needs a line of it's own).
   */
  public void initList(boolean getNewList) {
    if (isInitialized() && animation != null) {
      // if no change in animation, no update needed.
      if (lastChange == animation.getLastChange())
        return;
      lastChange = animation.getLastChange();

      /*
       * remember the currently selected index to restore it after rereading the
       * list. To be done before getAnimatorList because step needs the old
       * list.
       */
      int index = animatorList.getSelectedIndex();
      int step = getStep();
      if (getNewList)
        info = animation.getAnimatorList();
      // if end was selected, select end after rereading.
      boolean endSelected = (index == animatorList.getModel().getSize() - 1);

      if (info != null) {
        // iterate all lines of the list
        for (int i = 0; i < info.length; i++) {
          AnimationListEntry ai = info[i];
          // and if it's an Animator or a link, recalculate its message
          if (ai.mode == AnimationListEntry.ANIMATOR)
            ai.info = ai.animator.toString();
          if (ai.mode == AnimationListEntry.STEP)
            ai.info = ai.link.toString();

        }
        // avoid too much flickering by inserting the items
        // invisibly(it still flickers a lot!)
        animatorList.setVisible(false);
        animatorList.removeAll();
        String[] infos = new String[info.length];
        for (int i = 0; i < info.length; i++)
          infos[i] = info[i].info;

        animatorList.setListData(infos);
        animatorList.setVisible(true);
        repaint();
      }
      // restore the previous selection
      // ItemEvent ie = null;
      ListSelectionEvent ie = null;
      if (index < 0) {
        animatorList.setSelectedIndex(0);
        animatorList.ensureIndexIsVisible(0);
      } else if (index > info.length - 1 || endSelected) {
        animatorList.setSelectedIndex(info.length - 1);
        animatorList.ensureIndexIsVisible(info.length - 1);
      }
      // in every case, keep the step. if possible, also keep the index.
      else {
        animatorList.setSelectedIndex(index);
        animatorList.ensureIndexIsVisible(index
            + (animatorList.getVisibleRowCount() >>> 1));
        if (animation.getLink(step) != null)// only if step exists! if it was
          // deleted, keep the line

          setStep(step, false);
        else // step deleted, so reset the step in DrawWindow, if necessary
        if (step == AnimalMainWindow.getWindowCoordinator()
            .getDrawWindow(false).getStep())
          // ie = new ItemEvent(animatorList, 0, null, 0); // only a dummy
          ie = new ListSelectionEvent(animatorList, 0, 0, false); // only a
        // dummy
      }
      // perform a mouse click on this item and thus set this
      // step in DrawWindow and AnimationWindow(if necessary)
      // and reset the button's labels
      valueChanged(ie);
    }
    // notify DrawWindow of the changes
    AnimalMainWindow.getWindowCoordinator().getDrawWindow(false).update();
  }

  /**
   * sets the AnimationOverviews step by selecting an appropriate line, i.e. a
   * line that contains Link information or an Animator for this step. If an
   * appropriate line was already selected, don't change anything.
   */
  public void setStep(int step, boolean setOtherWindows) {
    int chosenStep = step;
    if (isInitialized()) {
      // even if the step of AnimationOverview has not changed, the
      // internal steps of DrawWindow and AnimationWindow may have
      // changed meanwhile, so reset them!
      if (setOtherWindows) {
        if (chosenStep == Link.END) {
          int h = animatorList.getModel().getSize() - 2;
          while (h >= 0 && info[h].mode != AnimationListEntry.STEP)
            h--;
          if (h >= 0)
            chosenStep = info[h].link.getStep();
        }
        AnimalMainWindow.getWindowCoordinator().getDrawWindow(false).setStep(
            chosenStep);
        AnimalMainWindow.getWindowCoordinator().getAnimationWindow(false)
            .setStep(chosenStep, true);
      }
      // if-statement only relevant when called by external method
      // (i.e. never :-) )
      // to avoid setting the first entry in a step when the currently
      // selected entry belongs to the same step
      if (getStep() == chosenStep)
        return;
      /* select first entry with requested step */
      for (int a = 0; a < info.length; a++) {
        if (info[a].mode == AnimationListEntry.STEP
            && info[a].link.getStep() == chosenStep) {
          animatorList.setSelectedIndex(a);
          animatorList.ensureIndexIsVisible(a);
          return;
        }
      }
      // or, if no appropriate step was found, select the first entry
      // which always exists as all Animations consist of at least one step
      animatorList.setSelectedIndex(0);
      animatorList.ensureIndexIsVisible(0);
    }
  }

  /**
   * returns the current step, i.e. the step of the currently selected Animator
   * or Link.
   */
  public int getStep() {
    return getStepBefore(animatorList.getSelectedIndex());
  }

  /**
   * returns the step <i>line</i> is in.
   */
  int getStepBefore(int line) {
    for (int a = line; a >= 0; a--) {
      if (info[a].mode == AnimationListEntry.STEP) {
        return info[a].link.getStep();
      }
    }
    return Link.START;
  }

  /**
   * returns the step <i>line</i> is in.
   */
  Link getCurrentLineStep(int line) {
    for (int a = line; a >= 0; a--) {
      if (info[a].mode == AnimationListEntry.STEP) {
        return info[a].link;
      }
    }
    return null;
  }

  /**
   * reacts to mouse clicks on the list's items by setting this step in
   * DrawWindow and AnimationWindow and resetting the button's labels
   */
  public void valueChanged(ListSelectionEvent e) {
    // only actually set the step when the item was actually clicked
    // by the user, not by an internal message
    setStep(getStep(), e != null);
  }

  class AnimationOverviewMouseAdapter extends MouseInputAdapter {
    Component component;

    AnimationOverviewMouseAdapter(Component c) {
      component = c;
    }

    public void mousePressed(MouseEvent evt) {
      if (animatorList.getSelectedIndex() != -1 && evt.isPopupTrigger()) {
        popup.show(component, evt.getX(), evt.getY());
        popup.setSelected(editItem);
      }
    }
  }

  /**
   * reacts to double clicks on the list's items by showing the
   * Animator's/Link's Editor. Reacts to buttons pressed by performing the
   * appropriate commands.
   */
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == animatorList || e.getSource() == editItem)
      editLine();
  }

  public void editLine() {
    AnimationListEntry ai = info[animatorList.getSelectedIndex()];
    // if the line contained an Animator...
    if (ai.mode == AnimationListEntry.ANIMATOR) {
      // open its secondary Editor.
      Editor se = ai.animator.getSecondaryEditor();
      if (se != null)
        se.setVisible(true);
    } else if (ai.mode == AnimationListEntry.STEP) {
      Editor se = ai.link.getSecondaryEditor();
      if (se != null)
        se.setVisible(true);
    }
  }

  /**
   * deletes the Animator or Link in the current line.
   */
  public void deleteLine() {
    if (info != null) {
      AnimationListEntry ai = info[animatorList.getSelectedIndex()];
      // Animators can easily be removed...
      if (ai.mode == AnimationListEntry.ANIMATOR) {
        animation.deleteAnimator(ai.animator);
        initList(true);
      }
      if (ai.mode == AnimationListEntry.STEP) {
        // can't delete the end mark(last line of the list)...
        if (ai.link.getStep() == Link.END) {
          JOptionPane.showMessageDialog(this, AnimalTranslator
              .translateMessage("cannotDeleteStartEndStep", "end"),
              AnimalTranslator.translateMessage("cannotDeleteStep"),
              JOptionPane.WARNING_MESSAGE);
        } else if (animation.getStepCount() <= 1) {
          // don't delete the only step either
          JOptionPane.showMessageDialog(this, AnimalTranslator
              .translateMessage("needAtLeastOneStep"), AnimalTranslator
              .translateMessage("cannotDeleteStep"),
              JOptionPane.WARNING_MESSAGE);
        } else if (animation.getAnimator(ai.link.getStep(), 0, null) == null || // i.e.
            // Animator
            // exists
            // in
            // this
            // step
            JOptionPane.showConfirmDialog(this, AnimalTranslator
                .translateMessage("deleteNonEmptyStep", Integer.valueOf(ai.link
                    .getStep())), AnimalTranslator
                .translateMessage("deleteStep"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
          // and only delete a non-empty step if the user agrees to
          // delete all Animators in it
          animation.deleteStep(ai.link.getStep());
        initList(true);
      }
    }
  }

  /**
   * notifies AnimationWindow of a new Animation. Initializes the list.
   */
  public void setAnimation(Animation theAnimation) {
    animation = theAnimation;
    // otherwise when loading one animation and directly the next one,
    // in both cases lastChange will be 4, disabling detection of a change
    lastChange = -1;
    initList(true);
  }

  private Object retrieveCopyOfSelectedElement() {
    int line = animatorList.getSelectedIndex();
    AnimationListEntry o2 = info[line];
//    Object o = animatorList.getSelectedValue();
    
    Object result = null;
    if (o2.mode == AnimationListEntry.STEP)
      result = ((Link)o2.link).clone();
    else
      result = ((Animator)o2.animator).clone();
    return result;
  }
  
  public void copyItem() {
    Object current = retrieveCopyOfSelectedElement();
    undoStack.push(current);
  }
  
  public void cutItem() {
    Object current = retrieveCopyOfSelectedElement();
    undoStack.push(current);
    // drop from list
    deleteLine();
  }
  
  public void pasteItem() {
    if (!undoStack.isEmpty()) {
//      int line = animatorList.getSelectedIndex();
      int step = getStep();
      if (step == Link.START)
        step = animation.getNextStep(step);
      Object o = undoStack.pop();
      if (o instanceof Link) {
         // TODO need to insert the step...
        System.err.println("cannot paste a link (yet)");
      } else {
        Animator animator = (Animator)o;
        animator.setStep(step);
        initList(true);
      }
    }
  }
  /**
   * appends a step after the current step.
   */
  public void appendStep() {
    int step = getStep();
    int newStep = animation.appendStep(step);
    initList(true);
    setStep(newStep, true);
  }

  /**
   * prepends a step before the current step.
   */
  public void prependStep() {
    int step = getStep();
    if (step == Link.START)
      step = animation.getNextStep(step);
    int newStep = animation.newStep(step);
    initList(true);
    setStep(newStep, true);
  }

  /**
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public void zoom(boolean zoomIn) {
    Image img;
    Image newimg;
    ImageIcon icon;
    Dimension dim = this.getSize();
    

    if (zoomIn) {
      if (defaultFont.getSize() < 24) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() + 2);
      }
      if (dim.getWidth() < 1000) {
        dim.setSize(dim.getWidth() + 20, dim.getHeight() + 20);
      }
      if (height < 30) {
        height = height + 5;
        width = width + 5;
      }
    } else {
      if (defaultFont.getSize() > 10) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() - 2);
      }
      if (dim.getWidth() > 400) {
        dim.setSize(dim.getWidth() - 20, dim.getHeight() - 20);
      }

      if (height > 10) {
        height = height - 5;
        width = width - 5;
      }

    }

    if (animatorList!= null)
      animatorList.setFont(defaultFont);


    if (deleteItem != null)
      deleteItem.setFont(defaultFont);

    if (editItem != null)
      editItem.setFont(defaultFont);

    if (appendStepItem != null)
      appendStepItem.setFont(defaultFont);

    if (prependStepItem != null)
      prependStepItem.setFont(defaultFont);

    if (cutItem != null)
      cutItem.setFont(defaultFont);
    if (copyItem != null)
      copyItem.setFont(defaultFont);

    if (pasteItem != null)
      pasteItem.setFont(defaultFont);




    if (popup != null) {
      setFileChooserFont(popup.getComponents());

    }
      if (buttonToolBar != null) {
        for (Component c : buttonToolBar.getComponents()) {
          if (c instanceof javax.swing.AbstractButton) {
            zoomButton((AbstractButton) c);
          }
        }

      }

    if (animatorToolBar != null) {
      for (Component c : animatorToolBar.getComponents()) {
        if (c instanceof javax.swing.AbstractButton) {
          zoomButton((AbstractButton) c);
        }
        if (c instanceof javax.swing.JComboBox)
          c.setFont(defaultFont);
      }

    }


    this.setSize(dim);



}


public void setFileChooserFont(Component[] comp) {
  // System.out.println("setFileChooserFont : " + comp.length);
  for (int i = 0; i < comp.length; i++) {
    if (comp[i] instanceof Container)
      setFileChooserFont(((Container) comp[i]).getComponents());
    try {
      comp[i].setFont(defaultFont);
    } catch (Exception e) {
    }
  }
}


  public void zoomButton(AbstractButton but) {

    Image img = ((ImageIcon) but.getIcon()).getImage();
    Image newimg = img.getScaledInstance(width, height,
        java.awt.Image.SCALE_SMOOTH);
    ImageIcon icon = new ImageIcon(newimg);
    but.setIcon(icon);
  }

}

