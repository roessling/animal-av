/*
 * Created on 10.11.2004
 */
package algorithm.fileHandler;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;

import animal.misc.MessageDisplay;

/**
 * Implementiert eine Klasse, die die notwendigen Dateioperationen fuer den
 * Codegenerator zur Verfuegung stellt.
 * 
 * @author Michael Maur <mmaur@web.de>
 */
public class FileHandler {

	/**
	 * Stringbuffer, that stores the AnimalScript-Code, to be written to disk
	 * later
	 */
	private StringBuilder sb;

	/**
	 * saves the location to which the animation will be stored
	 */
	private String fileName = "newfile.asu";

	/**
	 * flag, wether the filename has been set, or not
	 */
	private boolean fileNameSet = false;

	/**
	 * sets the filename (location, where animation will be stored)
	 * 
	 * @param newFileName
	 *          location, where animation will be stored
	 */
	public void setFileName(String newFileName) {
		fileName = newFileName;
		fileNameSet = true;
		System.out.println("FileHandler initialized | TargetFile: \"" + newFileName
				+ "\"");
	}

	/**
	 * provides information, wether filename has been set yet
	 * 
	 * @return boolean, wether filename has been set yet
	 */
	public boolean hasFileName() {
		return fileNameSet;
	}

	/**
	 * constructor, that creates new empty Stringbuffer to save the generated code
	 */
	public FileHandler() {
		sb = new StringBuilder(32768);
		sb.setLength(0);
	}

	/**
	 * constructor that creates new empty Stringbuffer to save the generated code
	 * and saves the location, to which the code will be written to disk..
	 * 
	 * @param newFileName
	 *          location, where the animation will be stored
	 */
	public FileHandler(String newFileName) {
		this();
		setFileName(newFileName);
	}

	/**
	 * adds a new Line of Text to the Stringbuffer
	 * 
	 * @param txtOfNewLine
	 *          the Text to be added to the Stringbuffer
	 */
	public void newLine(String txtOfNewLine) {
		add(txtOfNewLine);
	}

	/**
	 * adds a new Line of Text to the Stringbuffer
	 * 
	 * @param txtOfNewLine
	 *          the Text to be added to the Stringbuffer
	 */
	public void add(String txtOfNewLine) {
		sb.append(txtOfNewLine + MessageDisplay.LINE_FEED);
	}

	/**
	 * adds a String to the String-buffer without appending a line-break
	 * 
	 * @param txtFragment
	 *          text to be added to the current line of the Stringbuffer
	 */
	public void append(String txtFragment) {
		sb.append(txtFragment);
	}

	/**
	 * saves the StringBuilder content to disk
	 */
	public void save() {
		if (sb == null) {
			System.err.println("In order to save an algorithm to a file, "
					+ "you have to make sure, the Filehandler has been initialized");
			// throw new notInitializedException();
		} else if (fileNameSet) {
			try {
				if (!fileName.endsWith(".asu")) {
					fileName += ".asu";
				}
				PrintStream ps = new PrintStream(new BufferedOutputStream(
						new FileOutputStream(fileName)));
				ps.print(getAnimationCode());
				ps.close();
				System.out.println("File successfully created");
			} catch (Exception e) {
				System.err.println("Error accured while creating File!");
			}
		} else {
			System.err.println("In order to save an algorithm to a file, "
					+ "you have to make sure, the Filehandler has received a fileName");
			// throw new FileNameNotSetException();
		}
	}

	/**
	 * provides the code stored to the FileHandlers StringBuilder
	 * 
	 * @return the code, that has been stored in the FileHandlers Stringbuffer
	 */
	public String getAnimationCode() {
		return sb.toString();
	}

}
