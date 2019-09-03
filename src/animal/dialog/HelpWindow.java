package animal.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import translator.AnimalTranslator;
import animal.main.Animal;
import animal.main.AnimalFrame;
import animal.misc.XProperties;

/**
 * HelpWindow (based on the JFC SwingSet-Demo)
 * 
 * @author Guido R&ouml;&szlig;ling (roessling@acm.org>
 * @version 1.1 2000-06-30
 */
public class HelpWindow extends AnimalFrame implements ActionListener {
	// =================================================================
	// ATTRIBUTES
	// =================================================================

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 5122186058499837187L;

	/**
	 * The button for the window close operation
	 */
	private JButton closeButton;

	/**
	 * Used for addNotify check.
	 */
	private boolean fComponentsAdjusted = false;

	/**
	 * The HTMLPanel used for displaying the information
	 */
	private HtmlPanel htmlPanel;

	// =================================================================
	// CONSTRUCTORS
	// =================================================================

	/**
	 * Open a new HelpWindow on the base properties given
	 * 
	 * @param animalInstance
	 *          a reference to the main Animal object
	 * @param properties
	 *          the properties used for displaying, used for looking up the API
	 *          documentation
	 * @param title
	 *          the title of the window
	 * @param urlString
	 *          the base URL string to be used for help
	 */
	public HelpWindow(Animal animalInstance, XProperties properties,
			String title, String urlString) {
		super(animalInstance, properties);

		// setProperties(props);
		setSize(405, 305);

		getContentPane().setLayout(new java.awt.BorderLayout());

		String path = urlString;

		if (urlString == null) {
			path = properties.getProperty("tutorial.path", AnimalTranslator
					.translateMessage("tutorialPath", null));
		}

		htmlPanel = new HtmlPanel();
		htmlPanel.setURL(path);
		// TODO
		getContentPane().add(BorderLayout.CENTER, htmlPanel);
		getContentPane()
				.add(
						BorderLayout.SOUTH,
						closeButton = new JButton(
								getImageIcon(AnimalFrame.CLOSE_ICON_FILENAME)));
		closeButton.addActionListener(this);
		setTitle(title);
	}

	// =================================================================
	// EVENT HANDLING
	// =================================================================

	/**
	 * Listen to the close event
	 * 
	 * @param e
	 *          the ActionEvent
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == closeButton) {
			setVisible(false);
		}
	}

	// =================================================================
	// WINDOW OPERATIONS
	// =================================================================

	/**
	 * Add the notify functionality to listen to mouse clicks on links
	 */
	public void addNotify() {
		// Record the size of the window prior to calling parents addNotify.
		Dimension d = getSize();

		super.addNotify();

		if (fComponentsAdjusted) {
			return;
		}

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

	/**
	 * Toggle the visibilty of the window
	 * 
	 * @param isVisible
	 *          if <code>true</code>, show the window, else hide it
	 */
	public void setVisible(boolean isVisible) {
		if (isVisible) {
			setLocation(50, 50);
		}

		super.setVisible(isVisible);
	}
}
