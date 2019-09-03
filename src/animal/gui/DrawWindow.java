package animal.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JLabel;
import javax.swing.JScrollPane;

import animal.main.Animal;
import animal.main.AnimalFrame;
import animal.main.Animation;
import animal.main.AnimationState;
import animal.main.Link;
import animal.main.ObjectPanel;
import animal.misc.ObjectSelectionButton;
import animal.misc.XProperties;
import animal.vhdl.gui.VHDLObjectToolbar;

/**
 * The window used to draw and edit GraphicObjects. Contains the DrawCanvas, an
 * ObjectPanel for the GraphicObjects, and a statusline.
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido
 *         R&ouml;&szlig;ling</a>
 * @version 1.0 13.07.1998
 */
public class DrawWindow extends AnimalFrame {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long   serialVersionUID = -9218704082369265694L;

  /** the current AnimationState */
  private AnimationState      animationState;

  /** GraphicObjects have changed since last call towriteBack */
  private boolean             changed;
  private JScrollPane         sp;

  /** the DrawWindow's DrawCanvas */
  private DrawCanvas          drawCanvas;

  /** the DrawWindow's InternPanel */
  private InternPanel         internPanel;

  /** the DrawWindow's ObjectPanel */
  private ObjectPanel         objectPanel;

  /** the statusline in the south of the DrawWindow */
  private JLabel              statusLine;
  private DrawControlsToolbar controls;
  private Font                defaultFont      = new Font("Dialog", 0, 14);

  /**
   * constructs the DrawWindow. Initialization is done in <code>init</code>.
   * 
   * @see #init
   */
  public DrawWindow(Animal animalInstance, XProperties properties) {
    super(animalInstance, properties);
  }

  /**
   * Initializes the DrawWindow by adding DrawCanvas, Statusline,
   * GraphicObjectPanel and InternPanel.
   * <p>
   * 
   * For the need to split the constructor into constructor + init, cf.
   * AnimationOverview.
   * 
   * @see AnimationOverview#init
   */
  public void init() {
    super.init();
    setTitle("Draw Window");
    /*
     * statusline must be added before DrawCanvas, as DrawCanvas sets the
     * statusline
     */
    workContainer().add(BorderLayout.SOUTH, statusLine = new JLabel());
    if (sp == null) {
      sp = new JScrollPane();
      sp.getViewport().add(getDrawCanvas());
    }
    workContainer().add(BorderLayout.CENTER, sp);
    controls = new DrawControlsToolbar(this, getObjectPanel());
    workContainer().add(BorderLayout.NORTH, controls);
    workContainer().add(BorderLayout.WEST, new ObjectToolbar(this));
    //===================================================//
    //update by Lu,Zheng 20.10.2008 insert VHDL Toolbar
    //===================================================//
    workContainer().add(BorderLayout.EAST, new VHDLObjectToolbar(this));
    // workContainer().add(BorderLayout.WEST, getInternPanel());
    // workContainer().add(BorderLayout.NORTH, getObjectPanel());

    setAnimation(Animation.get());
    // setAnimation must be done before setProperties, because it sets
    // drawCanvas' objects

    setProperties(props);

    // initially, no GraphicEditor is selected
    getDrawCanvas().setGraphicEditor(null);

    // must not be called before both DrawCanvas and InternPanel are
    // initialized
    getDrawCanvas().getUndoAdapter();
    
    this.addComponentListener(new ComponentListener() {
      @Override
      public void componentShown(ComponentEvent e) {
        getDrawCanvas().updateComponentImages();
      }

      @Override
      public void componentHidden(ComponentEvent e) {}

      @Override
      public void componentMoved(ComponentEvent e) {}

      @Override
      public void componentResized(ComponentEvent e) {}
    });
    
  } // init()

  /**
   * makes the window visible, showing its step. This is done here to enable
   * calling all methods without actually executing them(not necessary if
   * nothing is visible). Not until the window is shown, the results of such
   * actions are visible.
   */
  public void setVisible(boolean b) {
    if (b) { // also happens when an iconified DrawWindow is shown again
      setStep(getStep());
      getDrawCanvas().curEditorSetVisible(true);
      getDrawCanvas().getObjects().setAllEditorsVisible();
    } else {
      // hide all Editors also
      // hide the primary editor
      getDrawCanvas().curEditorSetVisible(false);
      // and reset all secondary editors, hiding them
      getDrawCanvas().getObjects().resetAllEditors();
    }
    super.setVisible(b);
  }

  Image getImage() {
    return drawCanvas.getImage();
  }

  public void validateScrollingPane(Dimension size) {
    sp.invalidate();
  }

  /**
   * writes the DrawWindow's properties including DrawCanvas' and ObjectPanel's
   * properties.
   */
  public void getProperties(XProperties properties) {
    Rectangle b = getBounds();
    if (b.width + 11 == properties.getIntProperty("drawWindow.width", 320)
        && b.height + 8 == properties.getIntProperty("drawWindow.height", 200)) {
      b.width += 11;
      b.height += 8;
    }
    props.put("drawWindow.x", b.x);
    props.put("drawWindow.y", b.y);
    props.put("drawWindow.width", b.width);
    props.put("drawWindow.height", b.height);
    if (drawCanvas != null)
      drawCanvas.getProperties(properties);
    if (objectPanel != null)
      objectPanel.getProperties(properties);
  }

  /**
   * sets the DrawWindow's properties(bounds)
   */
  void setProperties(XProperties properties) {
    Rectangle r = new Rectangle(properties.getIntProperty("drawWindow.x", 50),
        properties.getIntProperty("drawWindow.y", 50), properties
            .getIntProperty("drawWindow.width", 400), properties
            .getIntProperty("drawWindow.height", 200));
    setBounds(r);
  }

  /**
   * deselects all objects. Called by ObjectSelectionButton.
   */
  public void deselectAll() {
    getDrawCanvas().getObjects().deselectAll();
  }

  /**
   * returns the current AnimationState.
   * <p>
   * required by InternPanel
   */
  public AnimationState getAnimationState() {
    return animationState;
  }

  /**
   * returns the DrawWindow's DrawCanvas.
   * 
   * @see DrawCanvas
   */
  public DrawCanvas getDrawCanvas() {
    if (drawCanvas == null)
      drawCanvas = new DrawCanvas(this, props);
    return drawCanvas;
  }

  public DrawControlsToolbar getDrawControlsToolbar() {
    if (controls == null)
      controls = new DrawControlsToolbar(this, getObjectPanel());
    return controls;
  }

  /**
   * returns the DrawWindow's InternPanel.
   * 
   * @see InternPanel
   */
  InternPanel getInternPanel() {
    if (internPanel == null)
      internPanel = new InternPanel(this);
    return internPanel;
  }

  /**
   * returns the DrawWindow's ObjectPanel containing buttons for each Animator.
   * 
   * @see ObjectPanel
   */
  public ObjectPanel getObjectPanel() {
    if (objectPanel == null)
      objectPanel = new ObjectPanel(animal, this, props, true);
    return objectPanel;
  }

  public void installPrimitiveToolBar() {
    getObjectPanel().installEditors(Animal.get());
    // animatorToolBar = getAnimatorPanel().getToolBar();
    // animatorToolBar.getAccessibleContext().setAccessibleName("Animators");
    // animatorToolBar.setFloatable(true);
    repaint();
  }

  /**
   * returns the current step.
   * 
   * @return returns the current step in the AnimationState
   */
  public int getStep() {
    if (animationState != null)
      return animationState.getStep();
    return 0;
  }

  /**
   * notifies the DrawWindow of a new Animation
   */
  public void setAnimation(Animation animation) {
    // this.animation = animation;
    animationState = new AnimationState(animation);
    setStep(getStep());
  }

  /**
   * sets the <i>changed</i> flag to indicate that someone has changed the set
   * of GraphicObjects(i.e. inserted, deleted or edited a GraphicObject). This
   * is useful as <code>writeBack</code> only has to write back changes if
   * changes exist.
   */
  public void setChanged() {
    changed = true;
  }

  /**
   * enables or disables selection by an <b>ObjectSelectionButton</b>.
   * 
   * @param osb
   *          <b>null</b> if selection is to be disabled,<br>
   *          not <b>null</b> if <i>osb</i> is the button that wants to select
   *          an object.
   */
  public void setExternalSelection(ObjectSelectionButton osb) {
    getInternPanel().setExternalSelection(osb);
  }

  /**
   * sets the text of the statusline.
   */
  void setStatusLineText(String msg) {
    if (statusLine != null)
      statusLine.setText(msg);
  }

  /**
   * sets the step in the DrawWindow.
   */
  public boolean setStep(int step) {
    int theStep = step;
    /*
     * to avoid overhead when the window is not visible. But because of this,
     * setStep must be called from setVisible.
     */
    if (isInitialized()) {
      // never allow drawing in step 0!
      if (animationState == null || animationState.getAnimation() == null)
        return false;
      theStep = animationState.getAnimation().verifyStep(theStep);
      if (theStep == Link.START)
        theStep = animationState.getAnimation().getNextStep(theStep);
      boolean stepChanged = theStep != getStep();
      // must writeBack even if step didn't change, as Objects may have been
      // added!
      writeBack();
      // no good step :-(
      if (!animationState.setStep(theStep, false))
        return false;
      // InternPanel must be notified as it contains a TextField for the step.
      internPanel.setStep(theStep);
      controls.setStep(theStep);
      // get the current GraphicObjects and display it in the DrawCanvas
      drawCanvas.setObjects(animationState.getCurrentObjects());
      // if the step is changed, the object selected by the currently
      // active ObjectSelectionButton may no longer be visible, so
      // unselect the ObjectSelectionButton
      if (stepChanged) {
        drawCanvas.getUndoAdapter().reset();
        ObjectSelectionButton.deselectActiveButton();
        AnimalMainWindow.getWindowCoordinator().getAnimationOverview(false)
            .setStep(theStep, false);
      }
    }
    return true;
  }

  /**
   * get GraphicObjects. This may be necessary if Animators have been inserted
   * or deleted etc.
   */
  public void update() {
    if (animationState != null)
      setStep(getStep());
  }

  /**
   * writes all objects back to the Animation(but only if there are objects to
   * be written back).
   * 
   * @return true if actually something was written back(at least potentially,
   *         i.e. something was changed <br>
   *         false otherwise.
   */
  public boolean writeBack() {
    if (drawCanvas != null && changed) {
      changed = false;
      drawCanvas.getUndoAdapter().reset();
      if (animationState == null || animationState.getAnimation() == null)
        return false;
      animationState.getAnimation().putObjectsAtStep(animationState.getStep(),
          drawCanvas.getObjects());
      // update the AnimationOverview(new objects may have been inserted)
      AnimalMainWindow.getWindowCoordinator().getAnimationOverview(false)
          .initList(true);
      // calling recursively, but only once, as no new change occurs.
      update();
      return true;
    }
    return false;
  }

  /**
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public void zoom(boolean zoomIn) {
    if (zoomIn) {
      if (defaultFont.getSize() < 24) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() + 2);
      }
    } else {
      if (defaultFont.getSize() > 10) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() - 2);
      }
    }
      
      if(statusLine != null) {
      defaultFont = new Font(statusLine.getFont().getFontName(),
          statusLine.getFont().getStyle(), defaultFont.getSize());
        statusLine.setFont(defaultFont);
      }
    
    if(drawCanvas != null) {
      drawCanvas.zoom(zoomIn);
    }

    if (controls != null) {
      controls.zoom(zoomIn);
    }

  }
}
