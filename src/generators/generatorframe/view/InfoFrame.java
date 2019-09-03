package generators.generatorframe.view;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class InfoFrame extends JFrame {

	/**
	 * Nora Wester
	 */
	private static final long serialVersionUID = 1L;

	JTextArea text;

	public InfoFrame(String title, String textLines, ImageIcon icon) throws HeadlessException {
		// TODO Auto-generated constructor stub
		super(title);
		setLayout(new BorderLayout());
		Container content = getContentPane();
		
		//JLabel nur anzeigen lassen, wenn es auch ein Icon gibt
		if(icon != null){
			JLabel image = new JLabel(icon);
			image.setPreferredSize(new Dimension(icon.getIconWidth()+10, icon.getIconHeight()));
			image.setMaximumSize(new Dimension(icon.getIconWidth()+10, icon.getIconHeight()));
			image.setMinimumSize(new Dimension(icon.getIconWidth()+10, icon.getIconHeight()));
			image.setBackground(Color.WHITE);
			content.add(image, BorderLayout.WEST);
		}
		
		text = new JTextArea(textLines);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setEditable(false);
		
		//set scroll
		JScrollPane scroll = new JScrollPane(text, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
						ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				
		content.add(scroll, BorderLayout.CENTER);
		
		//set same space around the text
		content.add(Box.createVerticalStrut(20), BorderLayout.NORTH);
		content.add(Box.createVerticalStrut(20), BorderLayout.SOUTH);
		content.add(Box.createHorizontalStrut(20), BorderLayout.EAST);
		
		content.setBackground(Color.WHITE);
		setMinimumSize(new Dimension(400, 200));
		setLocationRelativeTo(null);
		setVisible(true);
	}

  public void setNewText(String string) {
    // TODO Auto-generated method stub
    text.setText(string);
  }
  
  public void setNewText(String text, String title){
    setNewText(text);
    super.setTitle(title);
  }

  /**
   * zooms the listing
   * 
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public void zoom(boolean zoomIn) {
    Font f = text.getFont();
    Dimension dimT = text.getSize();
    Dimension dim = this.getSize();

    if (zoomIn) {
      if (f.getSize() <= 24)
        f = new Font(f.getName(), f.getStyle(), f.getSize() + 2);
      if (dimT.width <= 360)
        dimT.setSize(dimT.getWidth() + 20, dimT.getHeight() + 20);
      if (dim.width <= 360)
        dim.setSize(dim.getWidth() + 20, dim.getHeight() + 20);
    } else {
      if (f.getSize() >= 8)
        f = new Font(f.getName(), f.getStyle(), f.getSize() - 2);

      if (dimT.width >= 40)
        dimT.setSize(dimT.getWidth() - 20, dimT.getHeight() - 20);
      if (dim.width >= 40)
        dim.setSize(dim.getWidth() - 20, dim.getHeight() - 20);
    }
    text.setFont(f);
    text.setSize(dimT);
    this.setSize(dim);
    this.repaint();
  }

}
