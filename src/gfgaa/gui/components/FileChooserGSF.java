package gfgaa.gui.components;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/** FileChooser for .gsf files.
  *
  * @author S. Kulessa
  * @version 0.97c
  */
public class FileChooserGSF extends JFileChooser {

    /**
   * 
   */
  private static final long serialVersionUID = 8255127101534168608L;

    /** (Constructor)<br>
      * Creates a new FileChooserGSF object.
      */
    public FileChooserGSF() {

        // .gsf File Filter
        setFileFilter(new FileFilter() {

            /** Gibt die Dateiendung die der Filter fordert zurück
              *
              * @return Description     Beschreibung der Dateiendung
              */
            public String getDescription() {
                return "GraphScriptFile (.gsf)";
            };

            /** Methode die aufgerufen wird um zu testen
              * ob der Filter die Datei akzeptiert.
              *
              * @param f    File
              * @return     true | false
              */
            public boolean accept(final File f) {

                // Directories immer akzeptieren
                if (f.isDirectory()) {
                    return true;
                }

                int p = f.getName().indexOf(".");

                // Falls keine Dateiendung
                if (p == -1) {
                    return false;
                }

                // Überprüfen der Endung
                return f.getName().substring(p).equals(".gsf");
            }
        });
    }
}
