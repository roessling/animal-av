package animal.main;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import animal.misc.XProperties;
import translator.ResourceLocator;

/**
 * AnimalFrame is the base class for the main windows of Animal: Animal,
 * DrawWindow, AnimationOverview and AnimationWindow. It splits constructing and
 * initializing a window, as there are circular references: some windows need
 * references to the other windows when they are constructed. AnimalFrame uses
 * the Animal logo as an icon(displayed in the window's system menu when running
 * under Win95, displayed when iconified on DECs).
 * 
 * @see #init
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.0 18.07.1998
 */
public class AnimalFrame extends JFrame {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 6104840387066847165L;

	/**
	 * filename of the close image. Used by several windows, so define it here.
	 */
	public static final String CLOSE_ICON_FILENAME = "closeWindow.gif";

	/** a reference to the Animal object. Just for convenience. */
	protected Animal animal;

	/** the program's properties */
	protected XProperties props;

	/** the Animal logo. Has to be loaded only once. */
	private static Image animalImage;

	/** the initialization status of the window */
	boolean initialized = false;

	public static boolean runsInApplet = false;

	public static URL baseURL = null;
	public Class<?> animalImageDummy = null;

	/**
	 * constructs the AnimalFrame by setting its IconImage and registering
	 * itself as WindowListener. The <i>properties</i> are not applied, but only a
	 * reference is stored, so that they can be applied when <code>init</code>
	 * is called.
	 */
	public AnimalFrame(Animal animalInstance, XProperties properties, 
			boolean appletMode) {
		super();
		animal = animalInstance;
		AnimalFrame.runsInApplet = appletMode;
		props = properties;
		setIconImage(getAnimalImage());
		// to prevent Animal from closing the Animal Window unconditionally.
		// This window is only to be closed, if the file is unchanged or
		// is saved etc.
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new AnimalFrameListener());
	}

	/**
	 * constructs the AnimalFrame by setting its IconImage and registering
	 * itself as WindowListener. The <i>properties</i> are not applied, but only a
	 * reference is stored, so that they can be applied when <code>init</code>
	 * is called.
	 */
	public AnimalFrame(Animal animalInstance, XProperties properties) {
		super();
		animal = animalInstance;
//		runsInApplet = Animal.runsInApplet;
		props = properties;
		setIconImage(getAnimalImage());
		// to prevent Animal from closing the Animal Window unconditionally.
		// This window is only to be closed, if the file is unchanged or
		// is saved etc.
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new AnimalFrameListener());
	}

	/**
	 * returns the Animal logo.
	 */
	private Image getAnimalImage() {
		if (AnimalFrame.runsInApplet) {
			return null;
		} 
		if (animalImage == null) {
			ImageIcon ii = getImageIcon("Animal.png");//Elwood.jpg"); //smallAnimal.gif");
			if (ii != null)
				animalImage = ii.getImage();
		}
		return animalImage;
	}

	/**
	 * splitting the constructor into the actual constructor and the method
	 * init() is required, as the Editors and the AnimationOverview are mutually
	 * dependent, i.e. AnimationOverview needs the Editors for initialization
	 * and the AnimatorEditors(derived from Editor) need AnimationOverview for
	 * initialization. This can only be solved, if a valid reference to
	 * AnimationOverview exists before it is initialized. Subclasses of Animal
	 * should call super.init().
	 */
	public void init() {
		initialized = true;
	}

	/**
	 * returns whether the Window has already been initialized
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * the class to handle Animal's window messages. Just react to windowClosing
	 * message by closing the window if possible. Any subclass can overwrite
	 * <code>closeWindow</code> to prevent AnimalFrame from closing it(as done
	 * by <b>Animal </b>).
	 */
	class AnimalFrameListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			if (e.getSource() == AnimalFrame.this)
				AnimalFrame.this.closeWindow();
		} // windowClosing
	} // AnimalFrameListener

	/**
	 * closes the current window as default reaction to windowClosing events. If
	 * a window doesn't want this, it must overwrite this method.
	 * 
	 * @return true if the Window was closed, false otherwise
	 */
	public boolean closeWindow() {
		setVisible(false);
		return true;
	}

	/**
	 * returns the Object, to which add, setLayout etc. calls can be done. This
	 * was a convenience method when changing the program to Swing.
	 */
	public Container workContainer() {
		// return content pane for Swing Components, else <i>this</i>
		return getContentPane();
	}

	/**
	 * returns the imageIcon with the given name.
	 * 
	 * @return <b>null </b> if the Icon could not be found or read, <br>
	 *         the Icon otherwise.
	 */
	public ImageIcon getImageIcon(String name) {
	  return ResourceLocator.getResourceLocator().getImageIcon(name);
//		if (name == null || name.length() == 0)
//			return null;
//
//		ImageIcon icon = null;
//		URL url = null;
//		if (animalImageDummy == null)
//			try {
//				animalImageDummy = Class.forName("graphics.AnimalImageDummy");
//			} catch (ClassNotFoundException cfe) {
//				System.err.println("AnimalImageDummy could not be found!");
//			}
////			Get current classloader
//			if (animalImageDummy != null) {
//				ClassLoader cl = animalImageDummy.getClassLoader();
//				if (cl != null) {
//
//					url = cl.getResource("graphics/" +name);
//					if (url != null) { 
//						icon = new ImageIcon(url);
//						if (icon != null)
//							return icon;
//					}
//					System.err.println("trying again, this failed... for graphics/" +name);
//
//				} else System.err.println("ClassLoader failed, null!");
//			}
//			if (animalImageDummy != null) {
//				url = animalImageDummy.getResource(GRAPHICS_PATH + name);
//				if (url != null)
//				  System.err.println("URL for image is " +url.toString());
//				else
//					System.err.println("Oops, url is now null for image " +name);
//			}
//			else {
//				System.err.println("Argh for " +name +"!"); 
//				url = this.getClass().getResource(GRAPHICS_PATH + name);
//			}
//			if (url == null)
//				MessageDisplay.errorMsg("iconNotFound", name +" - 1",
//						MessageDisplay.CONFIG_ERROR);
//			else {
//			  icon = new ImageIcon(url);
////			  if (icon == null)
////				MessageDisplay.errorMsg("iconNotFound", name +" - 2",
////						MessageDisplay.CONFIG_ERROR);
////			  else 
//			    if (icon.getImageLoadStatus() == MediaTracker.ERRORED) 
//			    MessageDisplay.errorMsg("iconNotFound", name +" - 3",
//			        MessageDisplay.CONFIG_ERROR);
//      }
//
//			return icon;
	}

	public void addNotify() {
		// Record the size of the window prior to calling parents addNotify.
		Dimension d = getSize();

		super.addNotify();

		if (fComponentsAdjusted)
			return;
		// Adjust components according to the insets
		setSize(getInsets().left + getInsets().right + d.width, getInsets().top
				+ getInsets().bottom + d.height);
		Component[] components = getContentPane().getComponents();
		for (int i = 0; i < components.length; i++) {
			Point p = components[i].getLocation();
			p.translate(getInsets().left, getInsets().top);
			components[i].setLocation(p);
		}
		fComponentsAdjusted = true;
	}

	// Used for addNotify check.
	boolean fComponentsAdjusted = false;

}
