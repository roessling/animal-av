package extras.animalsense.ui;
import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

public class ExerciseChooser { 
	private Component parent;
	private String fileName;
	
	 /**
	 * @param parent
	 */
	protected ExerciseChooser() {
		this(null);
	}
	
    /**
	 * @param parent
	 */
	public ExerciseChooser(Component parent) {
		super();
		this.parent = parent;
	}


	public boolean open() { 
        final JFileChooser chooser = new JFileChooser("Open exercise"); 
        chooser.setDialogType(JFileChooser.OPEN_DIALOG); 
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        final File file = new File("."); 

        chooser.setCurrentDirectory(file); 
        fileName = null;
        chooser.setVisible(true); 
        final int result = chooser.showOpenDialog(parent); 

        
        if (result == JFileChooser.APPROVE_OPTION) { 
            File selFile = chooser.getSelectedFile(); 
            fileName = selFile.getPath();
        }
        chooser.setVisible(false);
        
        return fileName != null;
    }
	
	public boolean save() { 
        final JFileChooser chooser = new JFileChooser("Save exercise"); 
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        final File file = new File("."); 

        chooser.setCurrentDirectory(file); 
        fileName = null;
        chooser.setVisible(true); 
        final int result = chooser.showSaveDialog(parent); 

        
        if (result == JFileChooser.APPROVE_OPTION) { 
            File selFile = chooser.getSelectedFile(); 
            fileName = selFile.getPath();
        }
        chooser.setVisible(false);
        
        return fileName != null;
    }

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	} 
	
	
} 