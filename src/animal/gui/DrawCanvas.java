package animal.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import translator.AnimalTranslator;
import animal.editor.Editor;
import animal.editor.animators.AnimatorEditor;
import animal.editor.graphics.meta.GraphicEditor;
import animal.exchange.animalscript.AnimatorExporter;
import animal.graphics.PTGraphicObject;
import animal.main.Animal;
import animal.main.AnimalConfiguration;
import animal.main.Animation;
import animal.main.Link;
import animal.misc.EditableObject;
import animal.misc.MSMath;
import animal.misc.MessageDisplay;
import animal.misc.ObjectSelectionButton;
import animal.misc.ScalableGraphics;
import animal.misc.XProperties;

/**
 * DrawCanvas is the component placed in the Center of the DrawWindow. It is the
 * canvas where the user can draw, edit and select GraphicObjects. It reacts to
 * MouseEvents, provides Snap and Grid.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido R&ouml;&szlig;ling </a>
 * @version 1.0 18.07.1998
 */
public class DrawCanvas extends JPanel {
  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 8193616156057204529L;

  /**
   * the minimum size(either horizontally or vertically) that a selection area
   * needs. If a selection area is smaller that this, it is not considered. This
   * is to not to react to small mouse moves between pressing and releasing the
   * mouse.
   */
  private static final int MIN_AREA = 20;

  /**
   * image used to paint on. If a new graphic object is created or an existing
   * one is edited, <i>background </i> contains all other objects so that the
   * new / edited object can be updated quickly, without having to redraw all
   * other objects.
   */
  Image background;

  private static int drawButton = InputEvent.BUTTON1_MASK;

  static int finishButton = InputEvent.BUTTON2_MASK;

  static int cancelButton = InputEvent.BUTTON3_MASK;

  private ScalableGraphics sg = new ScalableGraphics();

  private Font              defaultFont       = new Font("Dialog", 0, 14);

  /** the current primary Editor, null if no primary Editor is selected. */
  GraphicEditor curEditor;

  /**
   * difference between lastDragPos and the closest grid point. Used to ensure
   * that every point that was on the grid before the drag operation will be on
   * the grid after the drag operation.
   */
  Point dragDiff;

  /** the object currently being dragged. */
  GraphicVector dragObjects = null;

  /** the point where dragging a GraphicObject started. */
  Point lastDragPos;

  /** the DrawWindow this DrawCanvas is in */
  DrawWindow drawWindow;

  /** distance between the grid's points/lines */
  private int grid = 0;

  /** the grid's color */
  private Color gridColor;

  /** Point to be set is valid */
  boolean isPossible = false;

  /**
   * a warning is currently being displayed. This is required as otherwise their
   * would be a multitude of warning windows when trying to edit a GraphicObject
   * that cannot be edited.
   */
  private boolean isWarning;

  /** user has the possibility to select several objects at once. */
  boolean multiSelection = false;

  /** index of currently edited(inserted) point, 0 is first point */
  int num = 0;

  private int[] explicitlyVisible = null;

  /** the Vector of all GraphicObjects currently visible */
  GraphicVector objects = new GraphicVector();

  /** the button which is currently responsible for selecting objects. */
  ObjectSelectionButton objectSelectionButton;

  /** internal counter to determine how often the background was painted. */
//  private int paintBackgroundCounter = 0;

  /** internal counter to determine how often the Image was redrawn */
//  private int paintCount = 0;

  /** grid is displayed as points or lines(default). */
  private boolean pointGrid = false;

  /**
   * Resulting image to be displayed. A triple buffering is used to avoid
   * flickering. This buffer is the final buffer. All other buffers are painted
   * into this one before it is displayed. For some(unknown) reason, the Swing
   * double buffering included in JComponent doesn't work here. When a new
   * object is drawn, the image will flicker.
   */
  Image image;

  /** user is currently selecting objects */
  boolean selection = false;

  /**
   * the selection area, made up by the current mouse position and the point
   * where dragging the mouse with button pressed started. SelectionArea is
   * ignored, if it is less than <i>MIN_AREA </i> pixels wide or high. If null,
   * no area selection is active.
   */
  Rectangle selectionArea = null;

  /** Point at which area selection started. */
  Point selStart;

  /** use snap? */
  private boolean snap;

  /** when an object is selected, show the Editors */
  boolean useEditors = false;

  private Color backgroundColor = Color.white;
  private DrawCanvasMouseListener canVasMouseListener;

  /**
   * initializes the DrawCanvas by registering its listeners and setting its
   * properties.
   * 
   * @param props
   *          the <b>properties </b> object to read the properties from.
   */
  public DrawCanvas(DrawWindow aDrawWindow, XProperties props) {
    super();
    drawWindow = aDrawWindow;
    addComponentListener(new DrawCanvasComponentListener());
    canVasMouseListener = new DrawCanvasMouseListener();
    addMouseListener(canVasMouseListener);
    addMouseMotionListener(new DrawCanvasMouseMotionListener());

    setProperties(props);
  }

  /**
   * writes the DrawCanvas properties, i.e. Grid and Snap properties
   * 
   * @param props
   *          the <b>properties </b> object to write to.
   */
  public void getProperties(XProperties props) {
    props.put("drawCanvas.Grid", grid);
    props.put("drawCanvas.pointGrid", pointGrid);
    props.put("drawCanvas.GridColor", gridColor);
    props.put("drawCanvas.BackgroundColor", backgroundColor);
    props.put("drawCanvas.Snap", snap);
  }

  /**
   * reads the DrawCanvas properties, i.e. Grid and Snap properties
   * 
   * @param props
   *          the <b>properties </b> object to read from.
   */
  private void setProperties(XProperties props) {
    setGrid(props.getIntProperty("drawCanvas.Grid", 0), props
        .getBoolProperty("drawCanvas.pointGrid"), props.getColorProperty(
        "drawCanvas.GridColor", Color.black));
    setBackgroundColor(props.getColorProperty("drawCanvas.BackgroundColor",
        Color.white));
    setSnap(props.getBoolProperty("drawCanvas.Snap"));
  }

  /*****************************************************************************
   * Editor related methods
   ****************************************************************************/

  /**
   * show the current primary Editor. This method is called when a DrawWindow
   * that was iconified, is shown again. Called by DrawWindow.setVisible
   */
  void curEditorSetVisible(boolean b) {
    if (curEditor != null)
      curEditor.setVisible(b);
  }

  /**
   * selects a new primary GraphicEditor. The old GraphicEditor(if it existed)
   * is hidden and the new one shown. If a GraphicObject was in the process of
   * being created, it is cancelled. If an <b>ObjectSelectionButton </b> was
   * pressed, it is deselected. The statusline in the south of the DrawWindow is
   * updated to display an information message how to work with the new editor.
   * called by Editor.close.
   * 
   * @param editor
   *          the new GraphicEditor
   */
  public void setGraphicEditor(GraphicEditor editor) {
    if (curEditor != null) {
      curEditor.setVisible(false);
      // set curEditor to null, because the Object last edited by
      // curEditor would not be painted if curEditor still existed
      curEditor = null;
      // redraw background to insert the Object painted last
      repaintAll();
    }
    if (editor != null) {
      ObjectSelectionButton.deselectActiveButton();
      drawWindow.getDrawControlsToolbar().setSelection(false);
      // drawWindow.getInternPanel().setSelection(false);
    } else // if no editor is present nor selection by OSB, turn selection
    // on
    // avoid recursive calls from setSelection by checking if
    // selection was already on.
    if (objectSelectionButton == null && !selection)
      drawWindow.getDrawControlsToolbar().setSelection(true, multiSelection,
          useEditors, null);
    // drawWindow.getInternPanel().setSelection(true, multiSelection,
    // useEditors, null);
    curEditor = editor;
    String msg;
    if (curEditor == null || (msg = curEditor.getStatusLineMsg()) == null)
      setSelectionMessage();
    else
      drawWindow.setStatusLineText(msg);
    // setSelection(false) must not be called from here, as in this case
    // selection by OSB is not possible!
    // Show the Editor only if the DrawWindow is visible.
    // The DrawCanvas seems to be visible even if the DrawWindow isn't.
    if (drawWindow.isVisible() && curEditor != null)
      curEditor.setVisible(true);
    num = 0;
  }

  public GraphicEditor getGraphicEditor() {
    return curEditor;
  }

  /**
   * creates a new GraphicObject from the current GraphicEditor(if one exists).
   */
  void createGraphicObject() {
    if (curEditor != null)
      curEditor.createObject();
    // reset point counter, otherwise we would "start" creating a new
    // object with its num-th point
    num = 0;
    repaintAll();
  }

  /*****************************************************************************
   * get and set methods.
   ****************************************************************************/

  /**
   * return the Vector of all objects in this step. Required by DrawWindow and
   * Editor.
   */
  public GraphicVector getObjects() {
    return objects;
  }

  /**
   * returns whether snap is used.
   */
  public boolean isSnap() {
    return snap;
  }

  /**
   * sets the GraphicObjects in this step. Called by DrawWindow.
   */
  void setObjects(GraphicVector graphicObjects) {
    objects.resetAllEditors();

    objects = graphicObjects;
    // copy the selectionmode from the previous GraphicVector
    objects.setSelectionMode(selection, multiSelection, useEditors);
    // if Editor.apply is pressed and a OSB is active,
    // the DrawCanvas is updated and receives
    // a new GraphicVector. So assert the selected objects after this
    // are the same than before
    if (objectSelectionButton != null)
      select(objectSelectionButton.getObjectNums(), true, true);
    // (Re-)display elements
    createGraphicObject();
  }

  /**
   * sets whether to show temporary objects
   */
  void setShowTempObjects(boolean b) {
    objects.setShowTempObjects(b);
    repaintAll();
  }

  /**
   * sets whether to use snap.
   */
  void setSnap(boolean b) {
    this.snap = b;
  }

  public void setBackgroundColor(Color c) {
    backgroundColor = c;
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }

  /*****************************************************************************
   * painting methods
   ****************************************************************************/

  /**
   * repaints the DrawCanvas with triple buffering. At first, its
   * background(containing all objects not currently being edited) is drawn,
   * then the selected objects are drawn atop, then the currently created
   * object, then the selection area(drawn as a round rectangle). All these are
   * drawn into a background image which is then copied onto screen to avoid
   * flickering.
   * 
   * @param g
   *          the Graphics context.
   */
  @Override
  public void paint(Graphics g) {
    sg.setGraphics(g);
    // if curComponent, also paint the current object
//    paintCount++;
    if (image != null) { // beim Initialisieren noch nicht vorhanden!
      Graphics rg = image.getGraphics();
      Graphics usedGraphics = rg;
      if (sg.getMagnification() != 1) {
        sg.setGraphics(rg);
        usedGraphics = sg;
      }
      // paint all objects not currenty being edited
      usedGraphics.drawImage(background, 0, 0, this);
      objects.drawSelected(usedGraphics);
      // if a primary editor is active, show the component it edits.
      if (curEditor != null && curEditor.isVisible())
        curEditor.paintObject(usedGraphics);
      if (explicitlyVisible != null) {
        GraphicVectorEntry[] entries = objects.convertToArray();
        for (int i = 0; i < explicitlyVisible.length; i++) {
          entries[explicitlyVisible[i]].go.paint(usedGraphics);
        }
      }
      // paint the selection area(if one exists) as a round rectangle
      if (selectionArea != null) {
        usedGraphics.setColor(Color.gray);
        usedGraphics.drawRoundRect(selectionArea.x, selectionArea.y,
            selectionArea.width, selectionArea.height, MIN_AREA, MIN_AREA);
      }
      // copy all graphic data to screen.
      g.drawImage(image, 0, 0, this);
      usedGraphics.dispose();
    }
  }

  /**
   * paint the background, i.e. the image containing all objects not currently
   * being created or edited. Selected objects are painted.
   */
  void paintBackground() {
    paintBackground(true);
  }

  /**
   * paint the background, i.e. the image containing all objects not currently
   * being created or edited.
   * 
   * @param paintSelected
   *          if true, also draw the selected objects into the background. This
   *          is required if an object is dragged, as then only this object is
   *          considered being edited and all other objects, even the selected
   *          ones, are in the background.
   */
  private void paintBackground(boolean paintSelected) {
//    paintBackgroundCounter++;
    if (background != null) {
      Graphics g = background.getGraphics();
      Graphics usedGraphics = g;
      if (sg.getMagnification() != 1) {
        sg.setGraphics(g);
        usedGraphics = sg;
      }
      // clear the background's background
      usedGraphics.setColor(backgroundColor); // Color.white);
      Dimension size = new Dimension(DrawCanvas.ensureLegalSize(getSize()));
      Animation animation = Animal.get().getAnimation();
      if (animation != null) {
        Dimension animationSize = animation.getSize();
        if (animationSize.width > size.width)
          size.width = animationSize.width;
        if (animationSize.height > size.height)
          size.height = animationSize.height;
      }
      size.width *= sg.getMagnification();
      size.height *= sg.getMagnification();
      drawWindow.validateScrollingPane(size);
      usedGraphics.fillRect(0, 0, size.width, size.height);
      drawGrid(g);
      if (paintSelected) {
        if (!selection) {
          // creating a new object, then draw all objects except the
          // newly created one.
          if (curEditor != null)
            objects.drawAllBut(usedGraphics, (PTGraphicObject) curEditor
                .getCurrentObject());
          else
            objects.drawAllBut(usedGraphics, (PTGraphicObject) null);
        } else // selecting or dragging an object.
        if (dragObjects != null)
          objects.drawAllBut(usedGraphics, dragObjects);
        else
          objects.drawAllBut(usedGraphics, (PTGraphicObject) null);
      } else
        // not paintSelected
        objects.drawUnselected(usedGraphics);
      // ??? g.drawImage(image, 0, 0, this);
      g.dispose();
    }
  }

  /**
   * repaints all GraphicObjects. The background is painted with all selected
   * objects included. called by InternPanel and OptionDialog
   */
  public void repaintAll() {
    paintBackground();
    repaint();
  }

  /**
   * overwritten to avoid flickering when clearing the background. The canvas is
   * painted completely, so it doesn't need to have its background cleared.
   */
  public void update(Graphics g) {
    paint(g);
  }

  /*****************************************************************************
   * Grid related methods
   ****************************************************************************/

  /**
   * applies the grid to a coordinate. If the grid or the snap is turned off,
   * the given coordinate is returned unchanged. Otherwise it is rounded to the
   * closest grid coordinate. E.g. if the grid is set to 10 and snap is on,
   * coordinates 0-4 are rounded to 0, 5-14 -> 10, 15-24 -> 20 etc.
   * 
   * @see #applyGrid(java.awt.Point)
   */
  private int applyGrid(int a) {
    if (!snap || grid == 0)
      return a;

    return (a + grid / 2) / grid * grid;
  }

  /**
   * applies the grid to a point.
   * 
   * @see #applyGrid(int)
   */
  Point applyGrid(Point p) {
    return new Point(applyGrid(p.x), applyGrid(p.y));
  }

  /**
   * draws the grid into the given Graphics context.
   * 
   * @see #setGrid
   */
  private void drawGrid(Graphics g) {
    // if grid==0, no grid is to be drawn.
    if (grid > 0) {
      g.setColor(gridColor);
      if (!pointGrid) {
        // draw a line grid, to be preferred before pointgrid,
        // as complexity of line grid is O(x+y), while complexity
        // of point grid is O(x*y)
        for (int x = 0; x < getSize().width; x += grid)
          g.drawLine(x, 0, x, getSize().height);
        for (int y = 0; y < getSize().height; y += grid)
          g.drawLine(0, y, getSize().width, y);
      } else { // draw a point grid
        for (int x = 0; x < getSize().width; x += grid)
          for (int y = 0; y < getSize().height; y += grid)
            g.drawLine(x, y, x, y);
      } // if lineGrid
    } // if grid > 0
  } // drawGrid

  /**
   * returns the grid's width, i.e. the distance of the grid's lines/points.
   */
  public int getGrid() {
    return grid;
  }

  /**
   * returns the grid's color. <br>
   * Called by OptionDialog.
   */
  public Color getGridColor() {
    return gridColor;
  }

  /**
   * returns whether the grid is a point grid <br>
   * Called by OptionDialog.
   */
  public boolean isPointGrid() {
    return pointGrid;
  }

  /**
   * sets the grid's attributes
   * 
   * @param theGrid
   *          the width of the grid, i.e. the distance of the grid's
   *          lines/points.
   * @param thePointGrid
   *          if true, only the nodes(i.e. the intersection of the lines) are
   *          drawn
   * @param theGridColor
   *          the color of the grid.
   */
  void setGrid(int theGrid, boolean thePointGrid, Color theGridColor) {
    grid = theGrid;
    pointGrid = thePointGrid;
    gridColor = theGridColor;
    createGraphicObject();
  }

  /**
   * set the grid's width, i.e. the distance of the grid's lines/points. All
   * other parameters of the grid remain untouched.
   */
  public void setGrid(int theGrid) {
    setGrid(theGrid, pointGrid, gridColor);
  }

  /**
   * sets the grid's color. <br>
   * Called by OptionDialog.
   */
  public void setGridColor(Color theGridColor) {
    setGrid(grid, pointGrid, theGridColor);
  }

  /**
   * sets the grid to be a point grid <br>
   * Called by OptionDialog.
   */
  public void setPointGrid(boolean isAPointGrid) {
    setGrid(grid, isAPointGrid, gridColor);
  }

  /*****************************************************************************
   * Object selection and editing
   ****************************************************************************/

  /**
   * Sets the mode for the following selection.
   * 
   * @param aSelection
   *          enables/disables selection.(If turned to false, the Editors are
   *          disposed, so they lose their location.)
   * @param isMultiSelection
   *          if true, several objects can be selected, if false, only one
   *          object can be selected at once. If changed to true, the currently
   *          selected object remains selected, if changed to false, only the
   *          topmost selected objects remains selected, all others are
   *          deselected When <i>multiSelection </i> is off, a mouse click
   *          selects a new Object and the old one is deselected. When
   *          <i>multiSelection </i> is on, one mouse click selects an object
   *          and the next click on the same object deselects it again. All
   *          other Objects remain untouched. Thus it is possible to select
   *          several objects. <br>
   *          Another possibility with <i>multiSelection </i> is the
   *          <b>Selection Area </b>. Dragging the mouse with button pressed
   *          will show a rectangle. All GraphicObjects in this rectangle will
   *          be selected once the mouse button is released. A GraphicObject is
   *          "in" a rectangle, if the bounding box intersects with the
   *          rectangle(probably not the best, but at least a solution).
   * @param usesEditors
   *          if true show the Editors of all selected objects.
   */
  public void setSelection(boolean aSelection, boolean isMultiSelection,
      boolean usesEditors, ObjectSelectionButton theObjectSelectionButton) {
    selection = aSelection;
    multiSelection = isMultiSelection;
    useEditors = usesEditors;
    objectSelectionButton = theObjectSelectionButton;

    objects.setSelectionMode(selection, multiSelection, useEditors);
    if (selection) {
      // when turning selection on, a newly created GraphicObject is
      // either cancelled(if it was not yet entered into the
      // GraphicVector) or it is made a permanent member of the
      // GraphicVector. Setting the Editor to <b>null<b> avoids
      // <b>not</b> drawing the object when the background is painted
      // for the next time.
      setGraphicEditor(null);
    }
    if (objectSelectionButton != null) {
      // if an object is selected by an ObjectSelectionButton and this
      // object is visible now, select it!
      if (objectSelectionButton.getObjectNums() != null)
        select(objectSelectionButton.getObjectNums(), true, true);
      // else do not use the already selected ones for the osb, because
      // then they would be introduced into the animation, making them
      // non-temporary! Instead, just deselect everything.
      else {
        objects.deselectAll();
        repaintAll();
      }
    } else {
      // draw background if objectSelectionButton has been deselected
      repaintAll();
      ObjectSelectionButton.deselectActiveButton();
    }
    setSelectionMessage();
  }

  /**
   * selects a group of GraphicObjects given by their number. If an
   * ObjectSelectionButton is active and waiting for objects to be selected,
   * this button is also updated.
   * 
   * @param nums
   *          the numbers of the GraphicObjects.
   * @param doSelect
   *          if true, the objects are marked selected in the <i>objects </i>
   *          GraphicVector. Therefore, if this has been done before,
   *          <i>doSelect </i> may be false.
   * @param isOSBInit
   *          if true, select the objects and don't toggle them
   */
  private void select(int[] nums, boolean doSelect, boolean isOSBInit) {
    if (doSelect)
      objects.select(nums, isOSBInit);
    paintBackground(false);
    repaint();
    // if an object is selected and an objectSelectionButton is waiting
    // for a selection, notify this button.
    if (objectSelectionButton != null)
      objectSelectionButton.objectsSelected(drawWindow.getStep(), objects
          .getSelectedObjects());
  }

  /**
   * selects a GraphicObject given by its numeric ID
   */
  public void select(int nums, boolean doSelect) {
    select(new int[] { nums }, doSelect, false);
  }

  public void makeVisible(int[] nums) {
    explicitlyVisible = nums;
  }

  public void makeVisible(int targetNum) {
    makeVisible(new int[] { targetNum });
  }

  /**
   * selects the GraphicObject at the given point, if one exists.
   */
  void select(Point p) {
    select(objects.select(p, 10), false);
  }

  /**
   * moves all currently selected objects in front of the unselected objects.
   */
  void moveToFront() {
    objects.moveToFront();
    repaintAll();
  }

  /**
   * moves all currently selected objects behind the unselected objects.
   */
  void moveToBack() {
    objects.moveToBack();
    repaintAll();
  }

  /**
   * deletes the selected objects(if possible).
   * 
   * @see GraphicVector#deleteSelected
   */
  void deleteSelected() {
    PTGraphicObject[] o = objects.deleteSelected();
    getUndoAdapter().delete(o);
    repaintAll();
  }

  /*****************************************************************************
   * Event handling
   ****************************************************************************/

  /**
   * reacts to mouse moves or drags. If an object is being created, the new
   * point is dragged. Or, an object or one of its ChangePoints may be dragged
   * 
   */
  void drag(Point p) {
    Point selectedPoint = p;
    if (!selection) {
      // a new object is being created, then drag the new point
      if (num > 0 && curEditor != null) {
        selectedPoint = applyGrid(p);
        isPossible = curEditor.nextPoint(num + 1, selectedPoint);
        paint(getGraphics());
      }
    } else { // working on selected object
      if (dragObjects != null) {
        boolean animatedExists = false;
        for (int a = 0; a < dragObjects.getSize(); a++)
          if (dragObjects.elementAt(a).mode == GraphicVectorEntry.ANIMATED) {
            animatedExists = true;
            break;
          }
        if (animatedExists) {
          // no editing possible on this object
          // only open one warning window at a time. otherwise, with
          // every drag message, the MessageDialog is shown
          if (!isWarning) {
            dragObjects = null;
            // to make it work on DEC which
            // produced a MessageDialog with every mouse move
            // event, even when dragObject was dropped
            // looks funny but is required
            isWarning = true;
            JOptionPane.showMessageDialog(this, AnimalTranslator
                .translateMessage("modified"), AnimalTranslator
                .translateMessage("noMod"), JOptionPane.INFORMATION_MESSAGE);
            isWarning = false;
          }
        } else { // dragging an object

          if (num <= 0) { // MovePoint
            // calculate the closest grid point. Cf. printed
            // documentation
            selectedPoint = applyGrid(MSMath.sum(p, dragDiff));
            // how much has the object been dragged since the last
            // call? */
            Point d = MSMath.diff(selectedPoint, lastDragPos);
            if (!d.equals(new Point(0, 0))) {
              for (int a = 0; a < dragObjects.getSize(); a++)
                dragObjects.elementAt(a).go.translate(d.x, d.y);
            }
          } else { // ChangePoint
            // ChangePoints are put into the grid, even if they were
            // not in it before.
            selectedPoint = applyGrid(p);
            for (int a = 0; a < dragObjects.getSize(); a++)
              ((GraphicEditor) dragObjects.elementAt(a).go.getEditor())
                  .nextPoint(num, selectedPoint);
          }
          // reset lastDragPos to current position, by this enabling
          // correct calculation of the translation in the next call.
          lastDragPos = selectedPoint;
          repaint();
        } // is Editable
      } // dragObjects != null
      else if (selStart != null) { // selecting an area
        selectionArea = getRectangle(selStart, selectedPoint);
        repaint();
      }
    }
  }

  /**
   * only reacts to resizing the DrawCanvas by getting new background and
   * buffering images.
   */
  class DrawCanvasComponentListener extends ComponentAdapter {
    /**
     * if the DrawCanvas is resized, make new Images for background and
     * buffering.
     */
    public void componentResized(ComponentEvent e) {
      updateComponentImages();
    }
  } // DrawCanvasComponentListener
  
  public void updateComponentImages() {
    if(drawWindow.isVisible()) {
      // cannot request Images of size 0.
      Dimension size = ensureLegalSize(getSize());
      background = createImage(size.width, size.height);
      image = createImage(size.width, size.height);
      paintBackground();
    }
  }

  Point dragStart;

  /**
   * reacts to mouse pressed and released events.
   */
  class DrawCanvasMouseListener extends MouseAdapter implements ActionListener {
    JPopupMenu popup;

    JMenu animateMenu;

    JMenuItem cloneItem, editItem, showIDItem, showCodeItem, deleteItem,
        deselectItem;

    JMenuItem appendStepItem, prependStepItem;

    Hashtable<String, JMenuItem> animatorMenuItems = new Hashtable<String, JMenuItem>(
        23);

    private JMenuItem generateItem(String label, int key,
        ActionListener listener, JComponent menu, boolean followedBySeparator) {
      JMenuItem menuItem = AnimalTranslator.getGUIBuilder().generateJMenuItem(
          label);
      // new JMenuItem(label, key);
      // String try = Translator.translateMessage(label.toLowerCase()
      // +".toolTipText");
      // menuItem.setToolTipText(toolTipMessage);
      // menuItem.setMnemonic(key);
      menuItem.addActionListener(listener);
      if ((menu instanceof JMenu) || (menu instanceof JPopupMenu)) {
        menu.add(menuItem);
        if (followedBySeparator)
          menu.add(new JSeparator());
      }
      return menuItem;
    }

    public DrawCanvasMouseListener() {
      popup = new JPopupMenu("Object Actions");
      editItem = generateItem("editItem", KeyEvent.VK_E, this, popup, false);

      animateMenu = AnimalTranslator.getGUIBuilder().generateJMenu(
          "animateMenu");
      // animateMenu.setMnemonic(KeyEvent.VK_A);
      popup.add(new JSeparator());
      JMenuItem dummy = null;
      Enumeration<String> e = Animal.get().getEditors().keys();
      int animatorNr = 0;
      // Vector<String> elems = new Vector<String>(60,20);
      while (e.hasMoreElements()) {
        String value = e.nextElement();
        // elems.add(e.nextElement());
        // }
        // String[] array = new String[elems.size()];
        // elems.copyInto(array);
        // Arrays.sort(array);
        // for (String value: array) {
        if (!value.equals("Show")
            && Animal.get().getEditor(value) instanceof AnimatorEditor) {
          dummy = generateItem(value + "Editor." + value.toLowerCase(),
              animatorNr++, this, animateMenu, false);
          dummy.setActionCommand(value);
          animatorMenuItems.put(value, dummy);
        }
      }
      popup.add(animateMenu);

      deselectItem = generateItem("deselect", KeyEvent.VK_D, this, popup, true);
      cloneItem = generateItem("cloneItem", KeyEvent.VK_C, this, popup, true);
      deleteItem = generateItem("deleteItem", KeyEvent.VK_D, this, popup, true);
      showIDItem = generateItem("showID", KeyEvent.VK_I, this, popup, false);
      showCodeItem = generateItem("animalScriptCode", KeyEvent.VK_O, this,
          popup, true);
      prependStepItem = generateItem("prependStep", KeyEvent.VK_P, this, popup,
          false);
      appendStepItem = generateItem("appendStep", KeyEvent.VK_A, this, popup,
          false);
    }

    public void actionPerformed(ActionEvent evt) {
      int[] currentlySelected = getObjects().getSelectedObjects();
      Object source = evt.getSource();
      if (source == prependStepItem) {
        int prevStep = Animation.get().prependStep(drawWindow.getStep());
        if (prevStep != Link.END)
          drawWindow.setStep(prevStep);
        return;
      } else if (source == appendStepItem) {
        int nextStep = Animation.get().appendStep(drawWindow.getStep());
        if (nextStep != Link.END)
          drawWindow.setStep(nextStep);
        return;
      }
      if (currentlySelected.length == 0)
        return;
      if (source == editItem) {
        getObjects().deselectAll();
        objects.setSelectionMode(selection, true, true);
        getObjects().select(currentlySelected, false);
        useEditors = false;
      } else if (animatorMenuItems.contains(source)) {
        Editor editor = Animal.get().getEditor(evt.getActionCommand());
        EditableObject a = editor.createObject();
        AnimatorEditor se = (AnimatorEditor) a.getSecondaryEditor();
        se.setProperties(AnimalConfiguration.getDefaultConfiguration()
            .getProperties());
        se.objectSB.setObjectNums(currentlySelected);
        se.setVisible(true);
      } else if (source == deselectItem) {
        getObjects().deselectAll();
        repaintAll(); // to remove the selectionArea Rectangle
        if (selectionArea != null) {
          selStart = null;
          selectionArea = null;
        }
      } else if (source == cloneItem) {
        drawWindow.getDrawCanvas().cloneSelectedObjects();
      } else if (source == showIDItem) {
        int nrSelected = currentlySelected.length;
        // new approach
        StringBuilder sb = new StringBuilder(266);
        for (int i = 0; i < nrSelected; i++)
          sb.append(
              AnimalTranslator.translateMessage("showID", new Object[] {
                  Integer.valueOf(getObjects().elementAt(i).go.getNum(false)),
                  getObjects().elementAt(i).go.toString() })).append("\n");
        MessageDisplay.message(sb.toString());
        JOptionPane.showMessageDialog(drawWindow, sb.toString());
        // for (int i = 0; i < nrSelected; i++)
        // MessageDisplay.message("showID",
        // new Object[] {
        // Integer.valueOf(getObjects().elementAt(i).go.getNum(false)),
        // getObjects().elementAt(i).go.toString() });
      } else if (source == showCodeItem) {
        try {
          int[] selectedIDs = getObjects().getSelectedObjects();
          AnimatorExporter ex = new AnimatorExporter();
          if (drawWindow != null)
            drawWindow.writeBack();
          Vector<PTGraphicObject> ptgos = Animal.get().getAnimation()
              .getGraphicObjects();
          AnimatorExporter.setGraphicObjects(ptgos);
          MessageDisplay.message("animalScriptCode", ex
              .exportUsedObjects(selectedIDs));
        } catch (Exception e) {
          MessageDisplay.message("animalScriptCodeExc", e.getMessage());
        }
      } else if (source == deleteItem)
        deleteSelected();
    }

    public void mousePressed(MouseEvent evt) {
      double magFactor = getMagnification();
      Point clickedPoint = new Point((int) Math.round(evt.getX() / magFactor),
          (int) Math.round(evt.getY() / magFactor));

      checkForPopup(evt, clickedPoint);
      if (selection) {
        // when the mouse leaves the drawCanvas, a new mousePressed
        // event is generated without having sent a mouseReleased
        // event before! So a new object would be selected, the old
        // one would lose the focus, but not the link to the editor!
        // To prevent this behaviour, check if an object is currently
        // dragged.
        if (dragObjects == null) {
          GraphicVectorEntry dragObject = objects
              .getObjectAtEditPoint(clickedPoint);

          if (dragObject == null) {
            /*
             * no EditPoint selected, then select the object found there.
             */
            select(clickedPoint);

            // Start an area selection, if possible.
            if (multiSelection)
              selStart = clickedPoint;
          } else { // clicked an EditPoint of a selected Object, so
            // make this the dragObject. Mark start and difference
            // to next grid point.
            dragObject.go.getEditor().linkToEditor(dragObject.go);
            // which point was clicked?
            num = objects.getEditPointNum();
            if (num > 0) {// ChangePoint
              dragObjects = new GraphicVector();
              dragObjects.addElement(dragObject);
            } else
              dragObjects = objects.getSelectedObjectsVector();
            // at what position?
            Point p = objects.getEditPointPos();
            // must store a copy(!) of the EditPoint.
            // If not and the EditPoint is one of the points
            // of the object(like arc's center),
            // then dragStart will be translated, too, and no
            // change will be detectable!
            dragStart = new Point(p);
            lastDragPos = applyGrid(p);
            dragDiff = MSMath.diff(lastDragPos, p);
            repaintAll();
          }
        }
      } else // not selecting, mouse was pressed for defining a point of a
      // new GraphicObject.
      if (curEditor != null) {
        // must be handled here, because a mouseClicked event is only
        // generated if the mouse is not moved between pressing and
        // releasing.
        // disadvantages: doubleClick sometimes results in
        // doubleClick + Click!
        // e.g. when doubleclicking a file in the FileDialog, a click is
        // also handled here!
        // finish if the middle button has been pressed or a double
        // click has been done.
        boolean finish = evt.getModifiers() == finishButton;
        boolean cancel = evt.getModifiers() == cancelButton;
        // | evt.getClickCount() == 2; // up to now only for Polyline!
        // double clicks are too difficult to evaluate, as
        // sometimes they cause a single and a double click event
        if (cancel)// SwingUtilities.isRightMouseButton(evt))
        {
          // right Mouse Button pressed => cancel
          // reset the Editor.
          num = 0;
          createGraphicObject(); // instead of reset
          return;
        } else if (num == 0) { // first point selected
          createGraphicObject();
          isPossible = curEditor.nextPoint(num + 1, applyGrid(clickedPoint));
        }
        // don't append the following if-statement with "else if"
        // because if a GraphicObject needs only one Point, it must run
        // through both statements at once!(e.g. PTPoint)
        // if middle mouse button is pressed or left button is double
        // clicked or enough points are set, finish the object.
        if (finish || (isPossible && ++num == curEditor.pointsNeeded())) {
          // finished object!
          num = 0;
          PTGraphicObject go = (PTGraphicObject) curEditor.getCurrentObject();
          objects.addElement(go, GraphicVectorEntry.CREATED);
          getUndoAdapter().insert(go);
          // tell the DrawWindow that a change has occured
          drawWindow.setChanged();
        }
        requestFocus(); // to return focus from Editor
      } // not selection
    } // mousePressed

    private void checkForPopup(MouseEvent evt, Point clickedPoint) {
      if (evt.isPopupTrigger() && selection)
        popup.show(evt.getComponent(), clickedPoint.x, clickedPoint.y);
    }

    public void mouseReleased(MouseEvent evt) {
      double magFactor = getMagnification();
      Point clickedPoint = new Point((int) Math.round(evt.getX() / magFactor),
          (int) Math.round(evt.getY() / magFactor));
      checkForPopup(evt, clickedPoint);
      if (dragObjects != null) {
        // an object that was dragged is to be dropped now.

        // notify the undo manager
        // consider dragDiff if and only if MovePoint, not ChangePoint
        Point to = num > 0 ? lastDragPos : MSMath.diff(lastDragPos, dragDiff);
        int[] theObjects = new int[dragObjects.getSize()];
        for (int a = 0; a < dragObjects.getSize(); a++) {
          PTGraphicObject go = dragObjects.elementAt(a).go;
          theObjects[a] = go.getNum(true);
          if (go.getEditor().isPrimaryEditor()) {
            // dragObject didn't open an Editor
            go.getEditor().linkToEditor(null);
            // dragObject must be reset before repainting
            // background!
            // Otherwise it wouldn't be painted.
          }
        }
        getUndoAdapter().move(theObjects, num, dragStart, to);
        dragObjects = null;
        paintBackground();
        drawWindow.setChanged();
      }
      // in either case, a change has taken place
      // } // dragObject != null
      else if (selStart != null) {
        // not dragging, but finished selecting an area.
        // Point now = evt.getPoint();

        objects.selectArea(getRectangle(clickedPoint, selStart));
        int currentDWStep = drawWindow.getStep();
        if (objectSelectionButton != null)
          objectSelectionButton.objectsSelected(currentDWStep, // drawWindow.
                                                                // getStep(),
              objects.getSelectedObjects());
        selStart = null;
        selectionArea = null;
        repaintAll(); // to remove the selectionArea Rectangle
      }
    } // mouseReleased

  } // DrawCanvasMouseListener

  /**
   * reacts to mouseMoved and mouseDragged events by simply calling
   * DrawCanvas.drag which then handles all that is necessary.
   */
  class DrawCanvasMouseMotionListener extends MouseMotionAdapter {
    public void mouseMoved(MouseEvent evt) {
      drag(evt.getPoint());
    }

    public void mouseDragged(MouseEvent evt) {
      drag(evt.getPoint());
    }
  } // DrawCanvasMouseMotionListener

  /**
   * create the smallest rectangle that contains <i>a </i> and <i>b </i>. Of
   * course, then <i>a </i> and <i>b </i> are edges of the rectangle.
   * 
   * @return the rectangle as described above, <br>
   *         <b>null </b>, if the rectangle is too small, i.e. both width and
   *         height are smaller than <i>MIN_AREA </i>.
   */
  Rectangle getRectangle(Point a, Point b) {
    int left = Math.min(a.x, b.x);
    int upper = Math.min(a.y, b.y);
    int right = Math.max(a.x, b.x);
    int lower = Math.max(a.y, b.y);
    Rectangle r = new Rectangle(left, upper, right - left, lower - upper);
    if (r.width < MIN_AREA && r.height < MIN_AREA)
      return null;

    return r;
  }

  /**
   * ensures that width and height are at least 1, as createImage throws an
   * IllegalArgumentException if either width or height are <= 0.
   */
  public static Dimension ensureLegalSize(Dimension size) {
    if (size.width < 1)
      size.width = 1;
    if (size.height < 1)
      size.height = 1;
    return size;
  }

  private AnimalUndoAdapter undoAdapter;

  AnimalUndoAdapter getUndoAdapter() {
    if (undoAdapter == null)
      undoAdapter = new AnimalUndoAdapter(this, drawWindow.getInternPanel());
    return undoAdapter;
  }

  Image getImage() {
    return image;
  }

  /**
   * sets the statusline message to selection mode, depending on whether single
   * or multi selection is on.
   */
  void setSelectionMessage() {
    drawWindow.setStatusLineText(AnimalTranslator
        .translateMessage((multiSelection) ? "selectObjects1"
            : "selectObjects2"));
  }

  /**
   * clones the selected objects, i.e. makes copies and places them somewhat
   * southeast of the originals. The new objects are selected instead of the old
   * ones, such that several copies can be made. Cloning can be undone.
   */
  public void cloneSelectedObjects() {
    int[] selected = objects.getSelectedObjects();
    PTGraphicObject[] newObjects = new PTGraphicObject[selected.length];
    for (int a = 0; a < selected.length; a++) {
      PTGraphicObject go = (PTGraphicObject) objects.getGVEByNum(selected[a]).go
          .clone();
      go.translate(20, 20);
      go.resetNum();
      newObjects[a] = go;
      selected[a] = go.getNum(true); // mit neuer Nummer ueberschreiben
      objects.addElement(go, GraphicVectorEntry.CREATED);
    }
    getUndoAdapter().insert(newObjects);
    objects.deselectAll();
    objects.select(selected, true);
    repaintAll();
  }

  public static void setMouseType(int newDrawButton, int newFinishButton,
      int newCancelButton) {
    DrawCanvas.drawButton = newDrawButton;
    DrawCanvas.finishButton = newFinishButton;
    DrawCanvas.cancelButton = newCancelButton;
  }

  public static String translateCancelButton() {
    return translateButton(cancelButton);
  }

  public static String translateDrawButton() {
    return translateButton(drawButton);
  }

  public static String translateFinishButton() {
    return translateButton(finishButton);
  }

  public static String translateButton(int button) {
    switch (button) {
    case InputEvent.BUTTON1_MASK:
      return AnimalTranslator.translateMessage("leftMB");
    case InputEvent.BUTTON2_MASK:
      return AnimalTranslator.translateMessage("middleMB");
    case InputEvent.BUTTON3_MASK:
      return AnimalTranslator.translateMessage("rightMB");
    }
    return String.valueOf(button);
  }

  /**
   * sets the magnification.
   */
  double getMagnification() {
    return sg.getMagnification();
  }

  /**
   * sets the magnification.
   */
  void setMagnification(double mag) {
    // sg.setColor(backgroundColor);
    // sg.fillRect(0, 0, 50, 50);
    Graphics tmpGraphics = image.getGraphics();
    tmpGraphics.setColor(backgroundColor);
    tmpGraphics.fillRect(0, 0, getSize().width, getSize().height);
    // paintBackground(false);
    sg.setMagnification(mag);
    repaintAll(); // All();
    // repaint();
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

    if (canVasMouseListener != null) {

      if (canVasMouseListener.popup != null) {
        setFileChooserFont(canVasMouseListener.popup.getComponents());

      }
      if (canVasMouseListener.animateMenu != null) {
        setFileChooserFont(canVasMouseListener.animateMenu.getMenuComponents());

      }

    }

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

} // DrawCanvas

