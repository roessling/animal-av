package animal.misc;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JColorChooser;
import javax.swing.JDialog;

import translator.AnimalTranslator;
import animal.gui.AnimalMainWindow;
import animal.main.Animal;
import animal.main.Animation;

public class ColorChooserAction extends AbstractAction {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -3178456060655099841L;

	private Component parent;

	private Color chosenColor = Color.black;

	private String tag;

	private JColorChooser chooser;

	private JDialog colorDialog;

	private PropertyChangeSupport changeSupport;

  private Font                  defaultFont      = new Font("Dialog", 0, 14);

	public ColorChooserAction(Component parentComponent, String label,
			String paramTag, String toolTipText, Color startColor) {
		super(label, new ColoredSquare(startColor));
		parent = parentComponent;
    chosenColor = startColor;
		tag = paramTag;
		putValue(Action.SHORT_DESCRIPTION, toolTipText);
		if (parent instanceof PropertyChangeListener) {
			changeSupport = new PropertyChangeSupport(parent);
			changeSupport.addPropertyChangeListener((PropertyChangeListener) parent);
		}
	}

	public Color getColor() {
		return chosenColor;
	}

	public void setColor(Color c) {
		putValue(Action.SMALL_ICON, new ColoredSquare(c));
		putValue(Action.NAME, ColorChoice.getColorName(c));
		if (parent instanceof PropertyChangeListener) {
			changeSupport.firePropertyChange(tag, chosenColor, c);
			// DO NOT USE APPLY!
			chosenColor = c;
			AnimalMainWindow.getWindowCoordinator().getDrawWindow(false).setChanged();
		} else {
			Animation anim = Animal.get().getAnimation();
			if (anim != null)
				anim.doChange();
		}
		if (parent != null)
			parent.repaint();
	}

	public void actionPerformed(ActionEvent actionEvent) {
		String actionCommand = actionEvent.getActionCommand();
    if (chooser == null) {
	    chooser = new JColorChooser(chosenColor);
	    chooser.addChooserPanel(new AnimalColorChooserPanel());
	    colorDialog = JColorChooser.createDialog(parent, AnimalTranslator
	        .translateMessage("selectColor"), false, chooser, this, this);
		}
		if (actionCommand.equalsIgnoreCase("OK")) {
			Color c = chooser.getColor();
			setColor(c);
		} else if (!actionCommand.equalsIgnoreCase("Cancel"))
      colorDialog.setVisible(true);
	}

  /**
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public void zoom(boolean zoomIn) {
    Dimension dim = new Dimension(0, 0);
    if (chooser != null)
      dim = colorDialog.getSize();

    if (zoomIn) {
      if (defaultFont.getSize() < 24) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() + 2);
      }

      if (dim.getWidth() < 800) {
        dim = new Dimension((int) dim.getWidth() + 40,
            (int) dim.getHeight() + 40);
      }

    } else {
      if (defaultFont.getSize() > 10) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() - 2);
      }

      if (dim.getWidth() > 600) {
        dim = new Dimension((int) dim.getWidth() - 40,
            (int) dim.getHeight() - 40);
      }

    }

    if (chooser != null) {
      Font f = new Font(chooser.getFont().getFontName(),
          chooser.getFont().getStyle(), defaultFont.getSize());
      chooser.setFont(f);
      setFont(chooser.getComponents(), f);

    }

    if (colorDialog != null) {

      Font f = new Font(colorDialog.getFont().getFontName(),
          colorDialog.getFont().getStyle(), defaultFont.getSize());
      colorDialog.setFont(f);
      setFont(colorDialog.getComponents(), f);
      colorDialog.setSize(dim);
    }


  }

  public void setFont(Component[] comp, Font f) {

    for (int i = 0; i < comp.length; i++) {
      if (comp[i] instanceof Container)
        setFont(((Container) comp[i]).getComponents(), f);
      try {
        if (comp[i] instanceof AbstractButton) {

          comp[i].setFont(new Font("Dialog.bold", 1, f.getSize()));
        } else {
        comp[i].setFont(f);
        }
      } catch (Exception e) {
      }
    }
  }
}
