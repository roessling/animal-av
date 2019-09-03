package animal.misc;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class LabeledAnimator implements Icon {
	ImageIcon icon;
	String label;

	public LabeledAnimator(ImageIcon imageIcon, String theLabel) {
		icon = imageIcon;
		label = theLabel;
	}

	public void paintIcon(Component c,
			Graphics g, int x, int y) {
		g.fill3DRect(x, y, getIconWidth(), getIconHeight(),
				true);
	}

	public int getIconWidth() {
		if (icon == null)
			return 0;
		return icon.getIconWidth();
	}

	public int getIconHeight() {
		if (icon == null)
			return 0;		return icon.getIconHeight();
	}
}
