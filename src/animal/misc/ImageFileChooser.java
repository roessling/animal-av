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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * 
 * A demo which makes extensive use of the file chooser.
 * 
 * 1.9 04/23/99
 * 
 * @author Jeff Dinkins
 */
public class ImageFileChooser extends JPanel {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 4726774986152808324L;

	static JFrame frame;

	AnimalFileView fileView;

	FilePreviewer previewer;

	JFileChooser chooser;

	String currentExtension;

	public File chosenFile;

	public String filename;

	Image image = null;

	public ImageFileChooser() {
		chooser = new JFileChooser();
		fileView = new AnimalFileView();
		previewer = new FilePreviewer(chooser);
		chooser.setAccessory(previewer);
	}

	public void setAnimalLoadFilters() {
		chooser.addChoosableFileFilter(new AnimalFileFilter("aml",
				"ASCII (compressed; preferred)"));// (theFilter);
		chooser.addChoosableFileFilter(new AnimalFileFilter("ama",
				"ASCII (uncompressed)"));
		// chooser.addChoosableFileFilter(new AnimalFileFilter("amz",
		// "Compressed Binary"));
		// chooser.addChoosableFileFilter(new AnimalFileFilter("amb",
		// "Uncompressed Binary"));
		chooser.addChoosableFileFilter(new AnimalFileFilter("asc",
				"Compressed Scripting"));
		chooser.addChoosableFileFilter(new AnimalFileFilter("asu",
				"Uncompressed Scripting"));
		// chooser.addChoosableFileFilter(new AnimalFileFilter("animal",
		// "Generic Animal format"));
		chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
		chooser.setFileFilter(chooser.getChoosableFileFilters()[0]);
	}

	public void setAnimalSaveFilters() {
		chooser.addChoosableFileFilter(new AnimalFileFilter("aml",
				"ASCII (compressed; preferred)"));// (theFilter);
		chooser.addChoosableFileFilter(new AnimalFileFilter("ama",
				"ASCII (uncompressed)"));
		// chooser.addChoosableFileFilter(new AnimalFileFilter("amz",
		// "Compressed Binary"));
		// chooser.addChoosableFileFilter(new AnimalFileFilter("amb",
		// "Uncompressed Binary"));
		/*
		 * chooser.addChoosableFileFilter(new AnimalFileFilter("asc", "Compressed
		 * Scripting")); chooser.addChoosableFileFilter(new AnimalFileFilter("asu",
		 * "Uncompressed Scripting")); chooser.addChoosableFileFilter(new
		 * AnimalFileFilter("animal", "Generic Animal format"));
		 */
		chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
		chooser.setFileFilter(chooser.getChoosableFileFilters()[0]);
	}

	public void setImageFilters() {
		chooser.addChoosableFileFilter(new AnimalFileFilter("apf",
				"APF Activated Pseudo-Format"));
		chooser.addChoosableFileFilter(new AnimalFileFilter("bmp",
				"BMP Windows Bitmap format"));
		AnimalFileFilter theFilter = new AnimalFileFilter("jpg", "JPG JPEG format");
		chooser.addChoosableFileFilter(theFilter);
		chooser.addChoosableFileFilter(new AnimalFileFilter("pcx",
				"PCX image format"));
		chooser.addChoosableFileFilter(new AnimalFileFilter("pict",
				"PICT v2 format"));
		chooser.addChoosableFileFilter(new AnimalFileFilter("png",
				"PNG image format"));
		chooser.addChoosableFileFilter(new AnimalFileFilter("psd",
				"PSD Photoshop image format"));
		chooser.addChoosableFileFilter(new AnimalFileFilter("ras",
				"SunRaster format"));
		chooser.addChoosableFileFilter(new AnimalFileFilter("tga",
				"TGA image format"));
		chooser.addChoosableFileFilter(new AnimalFileFilter("xbm",
				"XBM Bitmap format"));
		chooser.addChoosableFileFilter(new AnimalFileFilter("xpm",
				"XPM Pixelmap format"));
		chooser.setFileFilter(theFilter);
	}

	public void setImage(Image theImage) {
		image = theImage;
	}

	public Image openForLoad(JFrame aFrame) {
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		int returnValue = chooser.showDialog(aFrame, null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File theFile = chooser.getSelectedFile();
			if (theFile != null) {
				if (theFile.isDirectory()) {
					JOptionPane.showMessageDialog(aFrame, "You chose this directory: "
							+ chooser.getSelectedFile().getAbsolutePath());
				} else {
					JOptionPane.showMessageDialog(aFrame, "You chose this file: "
							+ chooser.getSelectedFile().getAbsolutePath());
				}

			}
		} else
			JOptionPane.showMessageDialog(frame, "No file was chosen.");

		return null;
	}

	public String openForSave(JFrame aFrame) {
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		int returnValue = chooser.showDialog(aFrame, null);
		System.err.println("Return value: " + returnValue);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File theFile = chooser.getSelectedFile();
			if (theFile != null) {
				if (!theFile.isDirectory()) {
					String aFilename = chooser.getSelectedFile().getAbsolutePath();
					AnimalFileFilter filter = (AnimalFileFilter) chooser.getFileFilter();
					currentExtension = filter.getExtension();
					return aFilename + "." + currentExtension;
				}
			}
		}
		JOptionPane.showMessageDialog(frame, "No file was chosen.");
		return null;
	}

	public String getExtension() {
		return currentExtension;
	}

	class FilePreviewer extends JComponent implements PropertyChangeListener {
		/**
		 * Comment for <code>serialVersionUID</code>
		 */
		private static final long serialVersionUID = 866103572991022625L;

		ImageIcon thumbnail = null;

		File f = null;

		public FilePreviewer(JFileChooser fc) {
			this.setPreferredSize(new Dimension(100, 50));
			fc.addPropertyChangeListener(this);
		}

		public void loadImage() {
			if (f != null) {
				ImageIcon tmpIcon = new ImageIcon(f.getPath());
				if (tmpIcon.getIconWidth() > 90) {
					thumbnail = new ImageIcon(tmpIcon.getImage().getScaledInstance(90,
							-1, Image.SCALE_DEFAULT));
				} else {
					thumbnail = tmpIcon;
				}
			}
		}

		public void propertyChange(PropertyChangeEvent e) {
			String prop = e.getPropertyName();
			if (prop == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) {
				f = (File) e.getNewValue();
				if (this.isShowing()) {
					loadImage();
					this.repaint();
				}
			}
		}

		public void paint(Graphics g) {
			if (thumbnail == null) {
				loadImage();
			}
			if (thumbnail != null) {
				int x = this.getWidth() / 2 - thumbnail.getIconWidth() / 2;
				int y = this.getHeight() / 2 - thumbnail.getIconHeight() / 2;
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
}
