package gfgaa.gui;

import gfgaa.gui.graphs.GraphEntry;

import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JEditorPane;

/** Filewriter class<br>
  * This Object writes the given graph or the content
  * of the bnf panel into a file.
  *
  * @author S. Kulessa
  * @version 0.97
  */
public final class GraphScriptWriter {

    /** Path of the file to write. */
    private String filename;

    /** GraphScript notation - graph variant flag. */
    public static final int WRITE_AS_GRAPH = 0;

    /** GraphScript notation - matrix variant flag. */
    public static final int WRITE_AS_MATRIX = 1;

    /** (constructor)<br>
      * Creates a new GraphScriptWriter Object.
      *
      * @param filename             Path of the file to write
      */
    public GraphScriptWriter(final String filename) {

        this.filename = filename;
    }

    /** (internal parser method)<br>
      * Writes the given data into the file.
      *
      * @param sData                Text to write into the file
      * @throws IOException         If the file can not be readed
      */
    private void writeFile(final String sData) throws IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(filename);
            writer.write(sData);
        } finally {
            writer.close();
        }
    }

    /** (internal parser method)<br>
      * Writes the description of the given graph into a file.
      *
      * @param entry        Graph type entry
      * @return             Termination value
      */
    public int startWriting(final GraphEntry entry) {
        try {
            writeFile(entry.transfer().toString());
        } catch (IOException ioe) {
            return -2;
        }
        return 0;
    }

    /** (internal parser method)<br>
      * Writes the content of the given panel into a file.
      *
      * @param editor       Bnf input pane
      * @return             Termination value
      */
    protected int startWriting(final JEditorPane editor) {
        try {
            writeFile(editor.getText());
        } catch (IOException ioe) {
            return -2;
        }
        return 0;
    }
}
