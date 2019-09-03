package animal.gui;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class JImagePanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private Image imageBG = null;
	
	private Graphics g = null;
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	    g.drawImage(imageBG, 0, 0, null);
	    this.g = g;
	}
	
	public void setImage(Image imageBG){
		this.imageBG = imageBG;
		Container tempPanel = this;
		do{
			if(g!=null){
				paint(g);
				paintComponent(g);
			}
			tempPanel.repaint();
			tempPanel.validate();
			tempPanel.revalidate();
			tempPanel = tempPanel.getParent();
		}while(tempPanel!=null);
	}
}
