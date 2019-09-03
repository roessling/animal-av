import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ImageIconFrame implements ActionListener {
//  private final static String GRAPHICS_PATH = "/graphics";
//  private Class<?>            animalImageDummy;
  private JFrame              frame;
  private Container           cp;
  private JPanel              p;

  public ImageIconFrame() {
    frame = new JFrame("Demo");
//    frame.setSize(480, 480);
    frame.setVisible(true);
    cp = frame.getContentPane();
    p = new JPanel();
    p.setSize(180, 160);
    p.setVisible(true);
    p.setLayout(new GridLayout(0, 8));
    cp.add(p);
    frame.setSize(200, 250);
    // cp.setLayout(new GridLayout(5, 1));
  }

  String[] icons          = new String[] { "Point", "Line", "Polyline",
      "Square", "Rectangle", "Triangle", "Polygon", "Circle", "Ellipse",
      "OpenCircleSegment", "OpenEllipseSegment", "ClosedCircleSegment",
      "ClosedEllipseSegment", "Arc", "Text", "IntArray", "DoubleArray",
      "StringArray", "IntMatrix", "DoubleMatrix", "StringMatrix", "Graph" };
  int      elementCounter = 0;
  Timer    timer;

  public void demo() {
    timer = new Timer(500, this);
    timer.isRepeats();
    timer.start();
  }

  public void actionPerformed(ActionEvent e) {
    elementCounter++;
    if (elementCounter < icons.length) {
      addJLabel("graphics/" + icons[elementCounter] + ".gif");
      // frame.invalidate();
      // p.repaint();
    } else
      timer.stop();
  }

  public void addJLabel(String fname) {
    // ImageIcon icon = getImageIcon("Arc.gif");
    System.err.println(fname);
    try {
      BufferedImage myPicture = ImageIO.read(new File(fname));// "graphics/Arc.gif"));
      JLabel picLabel = new JLabel(new ImageIcon(myPicture));
      p.add(picLabel);
      p.revalidate();
      // p.repaint();
    } catch (IOException e) {
      System.err.println("oops for " + fname + "!");
    }
    // p.prepareImage(icon, p);
  }

  public static void main(String[] args) {
//    SwingUtilities.invokeLater(new ImageIconFrame().demo);

    ImageIconFrame iif = new ImageIconFrame();
    iif.demo();
  }

  // public ImageIcon getImageIcon(String name) {
  // if (name == null || name.length() == 0)
  // return null;
  //
  // ImageIcon icon = null;
  // URL url = null;
  // if (animalImageDummy == null)
  // try {
  // animalImageDummy = Class.forName("graphics.AnimalImageDummy");
  // } catch (ClassNotFoundException cfe) {
  // System.err.println("AnimalImageDummy could not be found!");
  // }
  // // Get current classloader
  // if (animalImageDummy != null) {
  // ClassLoader cl = animalImageDummy.getClassLoader();
  // if (cl != null) {
  //
  // url = cl.getResource("graphics/" +name);
  // if (url != null) {
  // icon = new ImageIcon(url);
  // if (icon != null)
  // return icon;
  // }
  // System.err.println("trying again, this failed... for graphics/" +name);
  //
  // } else System.err.println("ClassLoader failed, null!");
  // }
  // if (animalImageDummy != null) {
  // url = animalImageDummy.getResource(GRAPHICS_PATH + name);
  // if (url != null)
  // System.err.println("URL for image is " +url.toString());
  // else
  // System.err.println("Oops, url is now null for image " +name);
  // }
  // else {
  // System.err.println("Argh for " +name +"!");
  // url = this.getClass().getResource(GRAPHICS_PATH + name);
  // }
  // if (url == null)
  // MessageDisplay.errorMsg("iconNotFound", name +" - 1",
  // MessageDisplay.CONFIG_ERROR);
  // else if ((icon = new ImageIcon(url)) == null)
  // MessageDisplay.errorMsg("iconNotFound", name +" - 2",
  // MessageDisplay.CONFIG_ERROR);
  // else if (icon.getImageLoadStatus() == MediaTracker.ERRORED)
  // MessageDisplay.errorMsg("iconNotFound", name +" - 3",
  // MessageDisplay.CONFIG_ERROR);
  //
  // return icon;
  // }
}
