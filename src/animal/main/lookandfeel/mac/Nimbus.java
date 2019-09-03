package animal.main.lookandfeel.mac;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Visual manager special for Mac OS X.
 * @author Mihail Mihaylov
 *
 */
public final class Nimbus {
	
	/**
	 * A private constructor is needed to correspond to the guidelines for an utility class.
	 */
	private Nimbus() {
		super();
	}

	/**
	 * Adapts the US to Mac OS X.
	 * @return true if sucessful
	 */
	public static boolean applyUI() {
		boolean success = false;
		// take the menu bar off the jframe
		System.setProperty("apple.laf.useScreenMenuBar", "true");

		// set the name of the application menu item
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Animal");

		// set the look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			success = true;
		} catch (ClassNotFoundException e) {
			return false; //			e.printStackTrace();
		} catch (InstantiationException e) {
			return false; //			e.printStackTrace();
		} catch (IllegalAccessException e) {
			return false; //			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			return false; //			e.printStackTrace();
		}
		
		return success;
	}
	
	/**
	 * Checks if this LookAndFeel can be applied on the guest OS.
	 * @return true if it can be applied
	 */
	public static boolean canApply() {
		String lcOSName = System.getProperty("os.name").toLowerCase();
		boolean isMacOsX = lcOSName.startsWith("mac os x");
		return isMacOsX;
	}


}
