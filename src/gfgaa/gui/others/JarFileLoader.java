package gfgaa.gui.others;

import java.awt.MediaTracker;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.JarFile;

import javax.swing.ImageIcon;

/** Creates a Reader that can read a file inside a JarFile.
  *
  * @author S. Kulessa
  * @version 0.95
  */
public final class JarFileLoader {

    /** Utility class.
      */
    private JarFileLoader() {
    }

    /** (internal method)<br>
      * Loads a resource file out of the the jar-File.
      *
      * @param path         Path to the file
      * @return             Reader object
      */
    public static Reader loadFile(final String path) {
        try {
            if (path.split("jar:").length > 1) {
                String[] str = path.split("!/");
                str[0] = str[0].split("jar:file:/")[1];

                @SuppressWarnings("resource")
                JarFile jarFile = new JarFile(str[0]);
                return new BufferedReader(
                           new InputStreamReader(
                               new BufferedInputStream(
                                       jarFile.getInputStream(
                                       jarFile.getEntry(str[1])))));
            }
            System.err.println("Internal Error - Invalid path: " + path);
        } catch (FileNotFoundException fnfe) {
            System.err.println("Internal Error - Invalid path: " + path);
        } catch (IOException ioe) {
            System.err.println("Internal Error - IOException occured"
                               + " while reading " + path);
        }
        return null;
    }

    /** (internal method)<br>
      * Loads a image out of the the jar-File.
      *
      * @param path         Path to the image
      * @return             ImageIcon object
      */
    public static ImageIcon loadImage(final String path) {
        try {
            ImageIcon icon = new ImageIcon(new URL(path));

            if (icon.getImageLoadStatus() == MediaTracker.ERRORED) {
                System.err.println("Internal Error - Can't load " + path);
                return null;
            }
            return icon;

        } catch (MalformedURLException e) {

            System.err.println("Internal Error - Invalid path: " + path);
            return null;
        }
    }
}
