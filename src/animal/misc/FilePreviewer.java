package animal.misc;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

public class FilePreviewer extends JComponent implements PropertyChangeListener {
  /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -4341608962634006886L;
ImageIcon thumbnail = null;
  File f = null;
  
  public FilePreviewer(JFileChooser fc) {
    setPreferredSize(new Dimension(100, 50));
    fc.addPropertyChangeListener(this);
  }

  public void loadImage() {
    if (f != null) {
      ImageIcon tmpIcon = new ImageIcon(f.getPath());
      if (tmpIcon.getIconWidth() > 90) {
        thumbnail = new ImageIcon(
                                  tmpIcon.getImage().getScaledInstance(90, -1, Image.SCALE_DEFAULT));
      } else {
        thumbnail = tmpIcon;
      }
    }
  }

  public void propertyChange(PropertyChangeEvent e) {
    String prop = e.getPropertyName();
    if (prop == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) {
      f = (File) e.getNewValue();
      if (isShowing()) {
        loadImage();
        repaint();
      }
    }
  }

  public void paint(Graphics g) {
    if (thumbnail == null) {
      loadImage();
    }
    if (thumbnail != null) {
      int x = getWidth()/2 - thumbnail.getIconWidth()/2;
      int y = getHeight()/2 - thumbnail.getIconHeight()/2;
      if (y < 0) {
        y = 0;
      }
      
      if (x < 5) {
        x = 5;
      }
      thumbnail.paintIcon(this, g, x, y);
    }
  }
}

