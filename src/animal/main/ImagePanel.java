package animal.main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private Image img;

    public ImagePanel(Image img){
        this.img = img;
		Dimension dim = new Dimension(img.getWidth(null), img.getHeight(null));
		if (dim.getWidth() < 980)
			dim.setSize(980, dim.getHeight());

		if (dim.getWidth() < 600)
			dim.setSize(dim.getWidth(), 600);

		this.setMinimumSize(dim);
		this.setPreferredSize(dim);

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
		g.drawImage(img, 20, 40, this);
    }

	public void setImage(Image img) {
		this.img = img;
		Dimension dim = new Dimension(img.getWidth(null), img.getHeight(null));
		if (dim.getWidth() < 880)
			dim.setSize(880, dim.getHeight());

		if (dim.getWidth() < 540)
			dim.setSize(dim.getWidth(), 540);
		this.setMinimumSize(dim);
		this.setPreferredSize(dim);
    	
    }

}

