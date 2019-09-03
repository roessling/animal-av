package animal.misc;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class ColoredSquare implements Icon {
	Color color;

	public ColoredSquare(Color c) {
		this.color = c;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		Color oldColor = g.getColor();
		g.setColor(color);
		g.fill3DRect(x, y, getIconWidth(), getIconHeight(), true);
		g.setColor(oldColor);
	}

	public int getIconWidth() {
		return 12;
	}

	public int getIconHeight() {
		return 12;
	}

	public void changeColor(Color c) {
		color = c;
	}
}
