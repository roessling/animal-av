package animal.exchange;

/**
 * The Animal ZIP file has to contain at least three files. It may contain more
 * files, but it must contain a manifest file with the filename
 * {@value #MANIFEST_FILENAME}. The three mandatory files are:
 * <ul>
 * <li>A file containing the AnimalScript code of the animation. This code
 * should reference the interaction definition file.</li>
 * <li>An interaction definition file which defines the interactions referenced
 * in the AnimalScript code.</li>
 * <li>A file called {@value #MANIFEST_FILENAME} that defines, which animation
 * and interaction definition file should be used.</li>
 * </ul>
 * 
 * @author SimonSprankel ( <a
 *         href="mailto:sprankel@rbg.informatik.tu-darmstadt.de"
 *         >sprankel@rbg.informatik.tu-darmstadt.de</a> )
 */
public class AnimalZipFormat {

  /** the default filename for the manifest file */
  public static final String MANIFEST_FILENAME    = "manifest.txt";

  /** the default filename for the animation definition (asu) file */
  public static final String ANIMATION_FILENAME   = "animation.asu";

}
