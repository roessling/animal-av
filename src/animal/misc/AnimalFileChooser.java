package animal.misc;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import translator.AnimalTranslator;
import animal.main.AnimalConfiguration;

/**
 * 
 * A demo which makes extensive use of the file chooser.
 * 
 * 1.9 04/23/99
 * 
 * @author Jeff Dinkins
 */
public class AnimalFileChooser extends JPanel {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 4308537498924801898L;

	static JFrame frame;

	AnimalFileView fileView;

	public AnimalFileFilter exportFilter, importFilter;

	FileFilter currentLoadFilter, currentImageFilter, currentSaveFilter,
			currentScriptFilter, currentExportFilter, currentImportFilter;

	FilePreviewer previewer;

	public JFileChooser chooser;

	String currentExtension, currentFormatTag;

	File defaultDirectory;

	private AnimalConfiguration animalConfig;

	Image image = null;
  private Font                defaultFont      = new Font("Dialog", 0, 14);

  public AnimalFileChooser(AnimalConfiguration config) {
		chooser = new JFileChooser(".");
    setFileChooserFont(chooser.getComponents());
		fileView = new AnimalFileView();
		previewer = new FilePreviewer(chooser);
		chooser.setAccessory(previewer);
		animalConfig = config;
		if (animalConfig == null) {
			//    	MessageDisplay.addDebugMessage("impossible: exchange state still
			// null @ AFileChooser!");
			animalConfig = AnimalConfiguration.getDefaultConfiguration();
			//    	MessageDisplay.errorMsg("exchangeState was
			// null@AnimalFileChooser, now is " +exchangeState,
			//    			MessageDisplay.DEBUG_MESSAGE);
		}
  }

	public void setCurrentDirectory(String directory) {
		chooser.setCurrentDirectory(new File(directory));
	}

	public void setCurrentDirectory(File f) {
		chooser.setCurrentDirectory(f);
	}

	public String getCurrentDirectoryName() {
		if (defaultDirectory == null)
			return ".";
		return defaultDirectory.getName();
	}

	public void setAnimalXMLFilter() {
		AnimalFileFilter theFilter = new AnimalFileFilter("xml", "XML");
		chooser.addChoosableFileFilter(theFilter);
		chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
		chooser.setFileFilter(theFilter);
	}

	public void resetFilters() {
		if (chooser == null)
			chooser = new JFileChooser();
		chooser.resetChoosableFileFilters();
		chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
	}

	public void setAnimalLoadFilters() {
		chooser.resetChoosableFileFilters();
		chooser.addChoosableFileFilter(new AnimalFileFilter("aml",
				"ASCII (compressed; preferred)"));//(theFilter);
		chooser.addChoosableFileFilter(new AnimalFileFilter("ama",
				"ASCII (uncompressed)"));
//		chooser.addChoosableFileFilter(new AnimalFileFilter("amz",
//				"Compressed Binary"));
//		chooser.addChoosableFileFilter(new AnimalFileFilter("amb",
//				"Uncompressed Binary"));
		chooser.addChoosableFileFilter(new AnimalFileFilter("asc",
				"Compressed Scripting"));
		chooser.addChoosableFileFilter(new AnimalFileFilter("asu",
				"Uncompressed Scripting"));
//		chooser.addChoosableFileFilter(new AnimalFileFilter("animal",
//				"Generic Animal format"));
		String[] allFilters = { "aml", "ama", "asc", "asu" };
		chooser.addChoosableFileFilter(new AnimalFileFilter(allFilters,
				"Animal"));

		chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
		if (currentLoadFilter == null)
			currentLoadFilter = chooser.getChoosableFileFilters()[0];
		chooser.setFileFilter(currentLoadFilter);
	}

	public void setAnimalScriptingFilters() {
		chooser.resetChoosableFileFilters();
		chooser.addChoosableFileFilter(new AnimalFileFilter("aml",
				"ASCII (compressed; preferred)"));//(theFilter);
		chooser.addChoosableFileFilter(new AnimalFileFilter("ama",
				"ASCII (uncompressed)"));
		chooser.addChoosableFileFilter(new AnimalFileFilter("asc",
				"Compressed Scripting"));
		chooser.addChoosableFileFilter(new AnimalFileFilter("asu",
				"Uncompressed Scripting"));
		chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
		if (currentScriptFilter == null)
			currentScriptFilter = chooser.getChoosableFileFilters()[2];
		chooser.setFileFilter(currentScriptFilter);
	}

	public void setAnimalSaveFilters() {
		chooser.resetChoosableFileFilters();
		Object[] filters = chooser.getChoosableFileFilters();
//		if (filters != null) {
//			System.err.println("# filters at start of saveFilters: "
//					+ filters.length);
//			for (int i = 0; i < filters.length; i++)
//				System.err.println("START .. Filter #" + i + ": "
//						+ filters[i].toString());
//		}
		chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
		chooser.addChoosableFileFilter(new AnimalFileFilter("aml",
				"ASCII (compressed; preferred)"));//(theFilter);
		chooser.addChoosableFileFilter(new AnimalFileFilter("ama",
				"ASCII (uncompressed)"));
		chooser.addChoosableFileFilter(new AnimalFileFilter("xml", "XML"));
		if (currentSaveFilter == null)
			currentSaveFilter = chooser.getChoosableFileFilters()[0];
		chooser.setFileFilter(currentSaveFilter);
		filters = chooser.getChoosableFileFilters();
		for (int i = 0; i < filters.length; i++)
			System.err.println("Filter #" + i + ": " + filters[i].toString());
	}

	public void setSpecificFilter(String mandatoryDir, String mandatoryPrefix,
			String description, boolean cleanAllFilters) {
		if (cleanAllFilters) {
			chooser.resetChoosableFileFilters();
			chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
		}
		chooser.addChoosableFileFilter(new AnimalFileFilter(mandatoryDir,
				mandatoryPrefix, description));
	}

	public void setImageLoadFilters() {
		chooser.resetChoosableFileFilters();
		chooser.addChoosableFileFilter(new AnimalFileFilter("apf",
				"APF Activated Pseudo-Format"));
		chooser.addChoosableFileFilter(new AnimalFileFilter("bmp",
				"BMP Windows Bitmap format"));
		chooser.addChoosableFileFilter(new AnimalFileFilter("gif",
						"GIF format"));
		chooser.addChoosableFileFilter(new AnimalFileFilter("jpg",
				"JPG JPEG format"));
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
		if (currentImageFilter == null)
			currentImageFilter = chooser.getChoosableFileFilters()[2];
		chooser.setFileFilter(currentImageFilter);
	}

	public void setImageFilters() {
		chooser.resetChoosableFileFilters();
		chooser.addChoosableFileFilter(new AnimalFileFilter("apf",
				"APF Activated Pseudo-Format"));
		chooser.addChoosableFileFilter(new AnimalFileFilter("bmp",
				"BMP Windows Bitmap format"));
		chooser.addChoosableFileFilter(new AnimalFileFilter("jpg",
				"JPG JPEG format"));
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
		if (currentImageFilter == null)
			currentImageFilter = chooser.getChoosableFileFilters()[2];
		chooser.setFileFilter(currentImageFilter);
	}

	public void chooseDefaultFilter() {
		chooser.setFileFilter(chooser.getChoosableFileFilters()[0]);
	}

	public void addFilter(String extension, String formatDescription) {
		chooser.addChoosableFileFilter(new AnimalFileFilter(extension,
				formatDescription));
	}

	public void setImage(Image theImage) {
		image = theImage;
	}

	public String openForImageLoad(JFrame aFrame) {
		setImageLoadFilters();
		String filename = null;
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		setCurrentDirectory(animalConfig.getCurrentFilename());
		int returnValue = chooser.showDialog(aFrame, null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			currentLoadFilter = chooser.getFileFilter();
			defaultDirectory = chooser.getCurrentDirectory();
			File theFile = chooser.getSelectedFile();
			if (theFile != null) {
				if (theFile.isDirectory()) {
					JOptionPane.showMessageDialog(aFrame,
							AnimalTranslator.translateMessage("chosenDir", 
									chooser.getSelectedFile().getAbsolutePath()));
				} else
					filename = theFile.getPath();
			}
		}
		return filename;
	}

	public String openForLoad(JFrame aFrame) {
		setAnimalLoadFilters();
		String filename = null;
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		setCurrentDirectory(animalConfig.getCurrentFilename());
		int returnValue = chooser.showDialog(aFrame, null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			currentLoadFilter = chooser.getFileFilter();
			defaultDirectory = chooser.getCurrentDirectory();
			File theFile = chooser.getSelectedFile();
			if (theFile != null) {
				if (theFile.isDirectory()) {
					JOptionPane.showMessageDialog(aFrame,
							AnimalTranslator.translateMessage("chosenDir", 
									chooser.getSelectedFile().getAbsolutePath()));
				} else
					filename = theFile.getPath();
			}
		}
		return filename;
	}

	public String openForSave(JFrame aFrame) {
		setAnimalSaveFilters();
		return openForExport(aFrame);
	}

	public String openForExport(JFrame aFrame) {
		if (currentExportFilter != null)
			chooser.setFileFilter(currentExportFilter);
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		if (animalConfig.getCurrentFilename() != null)
			setCurrentDirectory(animalConfig.getCurrentFilename());
		int returnValue = chooser.showDialog(aFrame, null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File theFile = chooser.getSelectedFile();
			currentExportFilter = chooser.getFileFilter();
			if (theFile != null) {
				if (!theFile.isDirectory()) {
					defaultDirectory = chooser.getCurrentDirectory();
					String filename = chooser.getSelectedFile()
							.getAbsolutePath();
					exportFilter = (AnimalFileFilter) chooser.getFileFilter();
					currentExtension = exportFilter.getExtension();
					currentFormatTag = exportFilter.getShortDescription();
					return filename; 
				}
			}
		}
//		JOptionPane.showMessageDialog(aFrame, 
//				AnimalTranslator.translateMessage("noFileChosen"));
		return null;
	}

	public String openForStraightLoad(JFrame referenceFrame) {
		int returnValue = chooser.showDialog(referenceFrame, null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File theFile = chooser.getSelectedFile();
			currentImportFilter = chooser.getFileFilter();
			if (theFile != null) {
				if (!theFile.isDirectory()) {
					defaultDirectory = chooser.getCurrentDirectory();
					String filename = chooser.getSelectedFile()
							.getAbsolutePath();
					importFilter = (AnimalFileFilter) chooser.getFileFilter();
					currentExtension = importFilter.getExtension();
					currentFormatTag = importFilter.getShortDescription();
					return filename;
				}
			}
		}
//		JOptionPane.showMessageDialog(referenceFrame,  
//				AnimalTranslator.translateMessage("noFileChosen"));
		return null;
	}

	public String openForImport(JFrame aFrame) {
		if (currentImportFilter != null)
			chooser.setFileFilter(currentImportFilter);
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		setCurrentDirectory(animalConfig.getCurrentFilename());
		int returnValue = chooser.showDialog(aFrame, null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File theFile = chooser.getSelectedFile();
			currentImportFilter = chooser.getFileFilter();
			if (theFile != null) {
				if (!theFile.isDirectory()) {
					defaultDirectory = chooser.getCurrentDirectory();
					String filename = chooser.getSelectedFile()
							.getAbsolutePath();
					importFilter = (AnimalFileFilter) chooser.getFileFilter();
					currentExtension = importFilter.getExtension();
					currentFormatTag = importFilter.getShortDescription();
					return filename;
				}
			}
		}
//		JOptionPane.showMessageDialog(aFrame,  
//				AnimalTranslator.translateMessage("noFileChosen"));
		return null;
	}

	public String openForFilenameChoice(JFrame aFrame) {
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		setCurrentDirectory(animalConfig.getCurrentFilename());
		int returnValue = chooser.showDialog(aFrame, null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File theFile = chooser.getSelectedFile();
			currentImportFilter = chooser.getFileFilter();
			if (theFile != null) {
				if (!theFile.isDirectory()) {
					defaultDirectory = chooser.getCurrentDirectory();
					String filename = chooser.getSelectedFile()
							.getAbsolutePath();
					return filename;
				}
			}
		}
//		JOptionPane.showMessageDialog(aFrame,  
//				AnimalTranslator.translateMessage("noFileChosen"));
		return null;
	}

	public String getExtension() {
		return currentExtension;
	}

	public String getFormat() {
		return currentFormatTag;
	}

	public String getDescription() {
		if (exportFilter == null)
			return "animation/animal-ascii";
		return exportFilter.getShortDescription();
	}

  /**
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public void zoom(boolean zoomIn) {

    if (zoomIn) {
      if (defaultFont.getSize() < 24) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() + 2);
      }
    } else {
      if (defaultFont.getSize() > 10) {
        defaultFont = new Font(defaultFont.getFontName(),
            defaultFont.getStyle(), defaultFont.getSize() - 2);
      }

    }



    if (chooser != null)
      setFileChooserFont(chooser.getComponents());

  }



  public void setFileChooserFont(Component[] comp) {
    // System.out.println("setFileChooserFont : " + comp.length);
    for (int i = 0; i < comp.length; i++) {
      if (comp[i] instanceof Container)
        setFileChooserFont(((Container) comp[i]).getComponents());
      try {
        comp[i].setFont(defaultFont);
      } catch (Exception e) {
      }
    }
  }

}
