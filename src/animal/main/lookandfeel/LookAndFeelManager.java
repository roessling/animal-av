/**
 * 
 */
package animal.main.lookandfeel;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import animal.main.lookandfeel.mac.MacOsX;

/**
 * This class is responsible for the adaptation of the user interface to the guest OS.
 * @author Mihail Mihaylov
 *
 */
public final class LookAndFeelManager {
	
	/**
	 * A private constructor is needed to correspond to the guidelines for an utility class.
	 */
	private LookAndFeelManager() {
		super();
	}

	/**
	 * Try to apply the most native look and feel by the running OS.
	 */
	public static void applyNativeLookAndFeel() {
//	  try {
//	    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
////	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//	  } catch(UnsupportedLookAndFeelException ulafe) {
//	    System.err.println("Sorry: " +ulafe.getMessage());
//	  }
//	  catch(Exception e) {
//	    System.err.println("Sorry: " +e.getMessage());
//	  }
//		if (MacOsX.canApply())
//			MacOsX.applyUI();
	}
	
}
