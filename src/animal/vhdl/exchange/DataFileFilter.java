package animal.vhdl.exchange;

import java.io.File;

import javax.swing.filechooser.FileFilter;
/**
 * 
 * @author Lu,Zheng
 *
 */
public final class DataFileFilter extends FileFilter {

	private String extension;

	private String description;

	public DataFileFilter(String extension, String description) {
		super();
		this.extension = extension;
		this.description = description;
	}
	
	public boolean accept(File f) {
		if (f != null) {
			if (f.isDirectory()) {
				return true;
			}
			String extension = getExtension(f);
			if (extension != null && extension.equalsIgnoreCase(this.extension)) {
				return true;
			}
		}
		return false;
	}
	
	public String getDescription() {
		return description;
	}
	
	private String getExtension(File f) {
		if (f != null) {
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if (i > 0 && i < filename.length() - 1) {
				return filename.substring(i + 1).toLowerCase();
			}
		}
		return null;
	}
}
