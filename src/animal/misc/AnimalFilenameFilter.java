package animal.misc;

import java.io.File;
import java.io.FilenameFilter;

public class AnimalFilenameFilter implements FilenameFilter {
	private String filterString = "animal";

	public AnimalFilenameFilter() {
		// nothing to be done here!
	}

	public AnimalFilenameFilter(String filter) {
		// set the filename filter...
		filterString = filter;
	}

	public boolean accept(File dir, String filename) {
		System.err.println(filename + " " + filename.endsWith(filterString));
		return filename.endsWith(filterString);
	}
}
