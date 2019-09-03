package animal.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import animal.api.AnimationCanvasMouseListener;
import animal.api.AnimationCanvasObjectActionListener;
import animal.api.DragAndDropPanelInJScrollPanel;
import animal.graphics.PTGraphicObject;
import animal.gui.AnimalMainWindow;
import animal.gui.GraphicVector;
import animal.gui.GraphicVectorEntry;
import animal.misc.ScalableGraphics;
import animal.misc.XProperties;

/**
 * The Canvas that shows the graphic objects. Used only by AnimationWindow.
 */
public class AnimationCanvas extends JPanel {

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long serialVersionUID = 5520674258993937191L;

  /** image for double buffering */
  private Image             image            = null;

  /** The GraphicObjects to be displayed */
  private GraphicVector     objects          = null;

  /**
   * the size the Canvas had when painted last. Needed for getting a new double
   * buffering image when the size has changed.
   */
  private Dimension         oldSize          = new Dimension(0, 0);
  private Dimension         referenceSize    = initSize();
  private Dimension         internalSize     = new Dimension(referenceSize);
  
  private Dimension         drawingsSize     = new Dimension(0, 0);

  /** the ScalableGraphics Object that scales the output */
  private ScalableGraphics  sg               = new ScalableGraphics();
  private Color             backgroundColor  = Color.WHITE;
  private Image             backgroundImage  = null;
  private Dimension         explicitSize     = null;

  public AnimationCanvas() {
    super();
    AnimationCanvasMouseListener listener = new AnimationCanvasMouseListener(this);
    addMouseListener(listener);
    addMouseMotionListener(listener);
    addMouseWheelListener(listener);
    DragAndDropPanelInJScrollPanel listenerDragAndDrop = new DragAndDropPanelInJScrollPanel(this);
    addMouseListener(listenerDragAndDrop);
    addMouseMotionListener(listenerDragAndDrop);
    AnimationCanvasObjectActionListener objListener = new AnimationCanvasObjectActionListener(this);
    addMouseListener(objListener);
    addComponentListener(new ComponentAdapter() {
      Dimension oldSize = null;
      public void componentResized(ComponentEvent e) {
        Dimension newSize = e.getComponent().getParent().getSize();
        if(oldSize == null || !oldSize.equals(newSize)) {
          oldSize = newSize;
          repaintNow();
        }
      }
    });
    setVisible(true);
    setBackground(backgroundColor);
  }

  /**
   * sets the magnification.
   */
  public void setMagnification(double mag) {
    sg.setMagnification(mag);
    // internalSize.setSize((int)(referenceSize.width * mag)+20,
    // (int)(referenceSize.height * mag)+20);
    JScrollPane sp = AnimalMainWindow.getWindowCoordinator()
        .getAnimationWindow(false).getScrollPane();
    Dimension size = new Dimension(internalSize.width, internalSize.height);
    if (size.width < getSize().width)
      size.width = getSize().width;
    if (size.height < getSize().height)
      size.height = getSize().height;
    image = createImage(size.width, size.height);
    if (sp != null)
      sp.doLayout();
    repaintNow();
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }

  public double getMagnification() {
    if (sg != null)
      return sg.getMagnification();
    return 1.0;
  }

  public void setBackgroundColor(Color c) {
    backgroundColor = c;
  }

  public Image getBackgroundImage() {
    return backgroundImage;
  }

  public void setBackgroundImage(Image image) {
    backgroundImage = image;
  }

  /**
   * paint the current non-temporary GraphicObjects
   */
  @Override
  public void paint(Graphics g) {
    Dimension size = null;
    // if (internalSize == null || internalSize.width == 0 ||
    // internalSize.height == 0)
    internalSize = initSize();

    // size = new Dimension(DrawCanvas.ensureLegalSize(internalSize));
    // //getSize()));
    // size = getPreferredSize();
    size = internalSize;
    Graphics ig = null;
    // new double buffering image needed?
    /*
     * if (!size.equals(oldSize) || image == null) { System.err.println(
     * "oldSize: " +oldSize +" new size: " +size); image =
     * createImage(size.width, size.height); oldSize = size; }
     */
    if (image == null || !(size.equals(oldSize))) {
      image = createImage(Math.max(1, internalSize.width), Math.max(1, internalSize.height));
      oldSize = size;
    }
    if (image != null) { // maybe window was not yet shown
      ig = image.getGraphics();
      if (getBackgroundImage() == null) {
        ig.setColor(backgroundColor);
        ig.fillRect(0, 0, size.width, size.height);
      } else
        ig.drawImage(getBackgroundImage(), 0, 0, size.width, size.height,
            getBackgroundColor(), this);
      // if magnification is 1, paint directly into the image,
      // otherwise use ScalableGraphics.
      Graphics where;
      if (sg.getMagnification() == 1)
        where = ig;
      else {
        where = ig;
        if (where instanceof java.awt.Graphics2D) {
          ((java.awt.Graphics2D) where).scale(sg.getMagnification(),
              sg.getMagnification());
        }
      }
      if (objects != null && objects.getSize() > 0) {
        int gveSize = objects.getSize();
        GraphicVectorEntry[] gves = objects.convertToArray();
        objects.heapsort(gves);
        // paint this and mark as painted
        // paint all GraphicObjects that are not temporary.
        // Temporary Objects can only be shown in DrawWindow
        // but never in AnimationWindow
        for (int a = gveSize - 1; a >= 0; a--)
          if (!gves[a].isTemporary()) {
            gves[a].go.paint(where);
          }
      }
      // finally copy the image to the screen.
      g.drawImage(image, 0, 0, this);
      ig.dispose();
    }
    
    if(System.currentTimeMillis()-lastPaintedTimeCanvasBoundingCalc >= 500) {
      setAnimationCanvasDimension(); // Make right Size
      lastPaintedTimeCanvasBoundingCalc = System.currentTimeMillis();
    }
    lastPaintedTime = System.currentTimeMillis();
  }

  private long lastPaintedTimeCanvasBoundingCalc = 0;

  public void setAnimationCanvasDimension() {
    AnimationCanvas canvas = this;
    JScrollPane panel = (JScrollPane) canvas.getParent().getParent();
    int widthOffset = 25;
    int heightOffset = 25;
    int topLeftX = Integer.MAX_VALUE;
    int topLeftY = Integer.MAX_VALUE;
    int bottomRightX = Integer.MIN_VALUE;
    int bottomRightY = Integer.MIN_VALUE;
    if (canvas.getObjects() == null) {
      topLeftX = 0;
      topLeftY = 0;
      bottomRightX = 0;
      bottomRightY = 0;
    } else {
      for (GraphicVectorEntry o : canvas.getObjects().convertToArray()) {
        final PTGraphicObject object = o.getGraphicObject();
        Rectangle rec = getRealBoundingBox(object);
        
        topLeftX = 0; // rec.x<point_NW_x ? rec.x : point_NW_x;
        topLeftY = 0; // rec.y<point_NW_y ? rec.y : point_NW_y;
        bottomRightX = rec.x + rec.width > bottomRightX ? rec.x + rec.width
            : bottomRightX;
        bottomRightY = rec.y + rec.height > bottomRightY ? rec.y + rec.height
            : bottomRightY;
      }
    }
    
    Dimension dimDrawings = new Dimension(widthOffset + bottomRightX - topLeftX, heightOffset + bottomRightY - topLeftY);
    setDrawingsSize(dimDrawings);

    int width = 0;
    int sizeDifferenzWidth = (panel.getWidth() - 8)
        - (widthOffset + bottomRightX - topLeftX);
    if (sizeDifferenzWidth >= 0) {
      width = panel.getWidth() - 8;
    } else {
      width = widthOffset + bottomRightX - topLeftX;
    }

    int height = 0;
    int sizeDifferenzHeight = (panel.getHeight() - 8)
        - (heightOffset + bottomRightY - topLeftY);
    if (sizeDifferenzHeight >= 0) {
      height = panel.getHeight() - 8;
    } else {
      height = heightOffset + bottomRightY - topLeftY;
    }

    if (sizeDifferenzHeight < 0 && sizeDifferenzWidth >= 8) {
      width -= 14;
    }
    if (sizeDifferenzWidth < 0 && sizeDifferenzHeight >= 8) {
      height -= 14;
    }

    canvas.setExplicitSize(new Dimension(width, height));
    canvas.repaint();
    canvas.updateUI();
    panel.getParent().repaint();
  }

  public Rectangle getRealBoundingBox(PTGraphicObject object) {
    return getRealBoundingBox(object.getBoundingBox());
  }

  public Rectangle getRealBoundingBox(Rectangle org) {
    Double mag = this.getMagnification();
    if (this.getMagnification() > 0) {
      return new Rectangle((int) Math.round(org.x * mag),
          (int) Math.round(org.y * mag), (int) Math.round(org.width * mag),
          (int) Math.round(org.height * mag));
    } else {
      return org;
    }
  }

  /**
   * repaints the GraphicObjects. A convenience method as no GraphicsContext has
   * to be passed as a parameter.
   */
  void repaintNow() {
    lastPaintedTimeCanvasBoundingCalc -= 500;
    paint(getGraphics());
  }

  public Dimension getCurrentSize() {
    return internalSize;
  }

  public Dimension initSize() {
    if (explicitSize != null)
      return explicitSize;
    XProperties animalProperties = AnimalConfiguration.getDefaultConfiguration()
        .getProperties();
    Dimension targetSize = new Dimension(
        animalProperties.getIntProperty("animationCanvas.width", 640),
        animalProperties.getIntProperty("animationCanvas.width", 480));
    AnimationWindow aWin = AnimalMainWindow.getWindowCoordinator()
        .getAnimationWindow(false);
    if (aWin != null && aWin.getScrollPane() != null) {
      Dimension canvasScreenSize = aWin.getScrollPane().getSize();
      if (canvasScreenSize.width != targetSize.width)
        targetSize.width = canvasScreenSize.width;
      if (canvasScreenSize.height != targetSize.height)
        targetSize.height = canvasScreenSize.height;
    }
    if (getMagnification() > 1) {
      targetSize.width = (int) (targetSize.width * getMagnification());
      targetSize.height = (int) (targetSize.height * getMagnification());
    }
    return targetSize;
  }

  /**
   * sets the GraphicVector to be drawn to <i>objects</i>.
   */
  void setObjects(GraphicVector objects) {
    this.objects = objects;
  }

  public GraphicVector getObjects() {
    return objects;
  }

  /**
   * returns the AnimationWindow's bounds.
   */
  void getProperties(XProperties props) {
    props.put("animationCanvas.BackgroundColor", getBackgroundColor());
  }

  /**
   * reads the DrawCanvas� properties, i.e. Grid and Snap properties
   * 
   * @param props
   *          the <b>properties</b> object to read from.
   */
  void setProperties(XProperties props) {
    setBackgroundColor(
        props.getColorProperty("animationCanvas.BackgroundColor", Color.white));
  }

  public Image getCurrentImage() {
    return image;
  }

  public void invalidateImage() {
    image = null;
  }

  public void setExplicitSize(Dimension d) {
    explicitSize = d;
    setSize(explicitSize);
  }

  public void clearExplicitSize() {
    explicitSize = null;
  }

  @Override
  public Dimension getPreferredSize() {
    return internalSize;
  }

  public Dimension getDrawingsSize() {
    return drawingsSize;
  }

  public void setDrawingsSize(Dimension drawingsSize) {
    this.drawingsSize = drawingsSize;
  }
  private long lastPaintedTime = 0;
  public long getLastPaintedTime() {
    return lastPaintedTime;
  }
}
