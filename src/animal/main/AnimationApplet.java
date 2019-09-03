package animal.main;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import translator.AnimalTranslator;
import animal.animator.Animator;
import animal.exchange.AnimationImporter;
import animal.gui.GraphicVector;

/**
 * The Window that displays the animation. Contains a Panel for control (play,
 * rewind, forward...) in its south and an AnimationAppletCanvas that contains
 * the GraphicObjects in its center.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling</a>
 * @version 1.0 13.07.1998
 */
public class AnimationApplet extends Applet implements ActionListener,
		ItemListener {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -7916236681433719427L;

	// private static final int PROTOCOL_VERSION = 1;
	/**
	 * filename of the close image. Used by several windows, so define it here.
	 */
	// public static final String CLOSE_ICON_FILENAME = "closeWindow.gif";
	/**
	 * the path for all graphics used by Animal, i.e. icons etc.
	 */
	// private static final String GRAPHICS_PATH = "/graphics/";
	/**
	 * the Animation to work with
	 */
	// private Animation animation = null;
	/**
	 * the AnimationState to work with
	 */
	private AnimationState ani = null;

	/**
	 * AnimationAppletCanvas to display the GraphicObjects.
	 */
	private AnimationAppletCanvas animationCanvas;

	/**
	 * button to close the AnimationApplet
	 */
	// private Button closeButton;
	/**
	 * button to go to the end of the Animation.
	 */
	private Button endButton;

	/**
	 * button to move forward one step.
	 */
	private Button forwardButton;

	// private Animal animal;

	private boolean debugMode = false; // true;

	boolean initialized = false;

	/**
	 * determines whether a pause is to be done after the next step if they are
	 * linked with a time link. If there is a key link, a pause is done anyway.
	 * will be checked in setStep before the next step is set
	 */
	private boolean pause = false;

	/**
	 * button to pause between two steps that are linked with a time link.
	 */
	private Button pauseButton;

	/**
	 * button to animate the current step and go to the next step
	 */
	private Button playButton;

	/**
	 * button to go one step backwards.
	 */
	private Button rewindButton;

	/**
	 * button to reset the Animation to its start.
	 */
	private Button startButton;

	/**
	 * Combobox to select the magnification
	 */
	private Choice magnificationCB;

	/**
	 * ticks elapsed since AnimationStart. Needed for synchronisation of the
	 * Animation
	 */
	private int ticks;

	/**
	 * construct an AnimationApplet. Actual initialization is done in
	 * <code>init</code>.
	 * 
	 * @see #init
	 */
	public AnimationApplet() {
		super();
		System.err.println(getCodeBase());
		// nothing to be done...
		new Animal(true, getCodeBase()); // runs in Applet!
	}

	/**
	 * initializes the AnimationApplet by adding the control panel and the
	 * AnimationAppletCanvas.
	 */
	public void start() {
		setLayout(new BorderLayout(0, 0));
		GridBagLayout gbl = new GridBagLayout();
		Panel controlPanel = new Panel(gbl);
		// GridBagLayout is chosen because all available space is to be distributed.
		// initialize the control panel by adding the buttons
		GridBagConstraints gbc = new GridBagConstraints();
		// don't leave space to the left and right but fill all available space.
		gbc.weightx = 1d / 2;
		// dito within each component
		gbc.fill = GridBagConstraints.BOTH;
		controlPanel.add(startButton = getButton("|<"), gbc);
		controlPanel.add(rewindButton = getButton("<<"), gbc);
		controlPanel.add(pauseButton = getButton("||"), gbc);
		controlPanel.add(playButton = getButton(">"), gbc);
		controlPanel.add(forwardButton = getButton(">>"), gbc);
		controlPanel.add(endButton = getButton(">|"), gbc);

		controlPanel.add(new Label("Magn.: ", Label.CENTER), gbc);
		String hundred = "100%";
		controlPanel.add(magnificationCB = new Choice(), gbc);
		magnificationCB.addItem("50%");
		magnificationCB.addItem("71%");
		magnificationCB.addItem(hundred);
		magnificationCB.addItem("141%");
		magnificationCB.addItem("200%");
		magnificationCB.select(hundred);

		// add AnimationApplet as ActionListener for all buttons
		startButton.addActionListener(this);
		rewindButton.addActionListener(this);
		pauseButton.addActionListener(this);
		playButton.addActionListener(this);
		forwardButton.addActionListener(this);
		endButton.addActionListener(this);
		magnificationCB.addItemListener(this);

		add(BorderLayout.SOUTH, controlPanel);

		// add the AnimationAppletCanvas
		animationCanvas = new AnimationAppletCanvas(this);
		add(BorderLayout.CENTER, animationCanvas);
		initialized = true;
		loadFile(getParameter("Animation"));
		if (ani == null)
			setAnimation(Animation.get());
	}

	/**
	 * returns whether the Window has already been initialized
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * creates a buttons with null Insets, so the buttons can be smaller yet
	 * display their text completely.
	 */
	private Button getButton(String text) {
		Button result = new Button(text);
		return result;
	}

	public void init() {
		super.init();
	}

	/**
	 * overwritten to initialize the internal Animation. When the window is not
	 * visible, all methods can be called, but they don't make a change until the
	 * window is made visible. Then perform the commands from here.
	 */
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b) {
			if (ani == null)
				setAnimation(Animation.get());
			setStep(ani.getStep(), true);
		}
	}

	/**
	 * reacts to buttons from control panel pressed.
	 */
	public void actionPerformed(ActionEvent event) {
		Object object = event.getSource();
		if (object == startButton)
			setStep(0, true);
		else if (object == rewindButton)
			setStep(ani.getPrevStep(), true);
		else if (object == pauseButton)
			pause = true;
		else if (object == playButton)
			setStep(ani.getNextStep(), false);
		else if (object == forwardButton)
			setStep(ani.getNextStep(), true);
		else if (object == endButton)
			setStep(ani.getLastStep(), true);
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == magnificationCB) {
			String s = magnificationCB.getSelectedItem();
			s = s.substring(0, s.length() - 1);
			int i;
			try {
				i = Integer.parseInt(s);
			} catch (NumberFormatException nfe) {
				errorMsg(AnimalTranslator.translateMessage("illegalMagnification",
						s));
				i = 1;
			}
			animationCanvas.setMagnification(1d / 100 * i);
		}
	}

	/**
	 * sets the step for the AnimationApplet.
	 * 
	 * @param immediate
	 *          if true, the state of the Animation after executing the step is
	 *          displayed. If false, the Animators of this step are executed
	 *          visibly. In the end, the same state is reached.
	 */
	void setStep(int step, boolean immediate) {
	  int theStep = step;
		if (isVisible()) { // don't work if the window is not visible anyway
			showStatus("Animation running");
			int next = 0;
			while (next != Link.END) { // Animation not yet at its end
			  theStep = ani.getAnimation().verifyStep(theStep);
				if (ani != null)// prepare the AnimationState object

					ani.setStep(theStep, !immediate);
				// paint the objects directly...
				if (immediate) {
					animationCanvas.repaintNow();
					next = Link.END;
				} else {
					// ...or execute the Animators slowly
					next = nextStep();
					if (next != Link.END)
					  theStep = next;
				}
			} // while next
			boolean notAtStart = theStep != 0;
			boolean notAtEnd = theStep != ani.getLastStep();
			startButton.setEnabled(notAtStart);
			rewindButton.setEnabled(notAtStart);
			forwardButton.setEnabled(notAtEnd);
			endButton.setEnabled(notAtEnd);
			playButton.setEnabled(notAtEnd);
		}
	}

	/**
	 * returns the current step
	 */
	public int getStep() {
		return ani.getStep();
	}

	/**
	 * executes one step by executing all Animators "slowly"(i.e. not at once but
	 * according to their execution time).
	 * 
	 * @return next step to be executed immediately afterwards; Link.END, if none
	 *         such step exists
	 */
	int nextStep() {
		Vector<Animator> animators; // Animators to execute in this step
		Animator animator; // Animator currently processed
		long time; // for synchronization

		int step = ani.getStep();
		// required because the playbutton can be pressed by pressing space
		// even when disabled!
		playButton.setEnabled(false);
		// get the Animators that still have to be processed to reach the next
		// step. They are left by AnimationState.setStep if immediate was
		// false.
		animators = ani.getCurrentAnimators();
		time = System.currentTimeMillis();
		// initialize each Animator by linking it with AnimationApplet's
		// AnimationState object and resetting its start time and ticks
		for (int a = 0; a < animators.size(); a++)
			animators.elementAt(a).init(ani, time, ticks);

		boolean finished = false; // no more Animators to be processed?
		while (!finished) {
			finished = true;
			time = System.currentTimeMillis();
			ticks++;
			// execute each Animator slowly
			for (int a = 0; a < animators.size();) {
				animator = animators.elementAt(a);
				// perform an action on the Animator
				animator.action(time, ticks);
				// and if this was its last action, remove it from the
				// list of Animators to be processed
				if (animator.hasFinished())
					animators.removeElementAt(a);
				else {
					// if it's not yet removed, it has to be processed once
					// more in the next loop.
					finished = false;
					a++;
				}
			}
			// after all Animators have executed once, display the new state
			animationCanvas.repaintNow();
		}
		// check link to the next step
		Link l = ani.getAnimation().getLink(step);
		switch (l.getMode()) {
		case Link.WAIT_KEY: // do not automatically process the next step
			return Link.END;
		case Link.WAIT_TIME:
			if (!pause) { // pause button has not been pressed
				try { // time to have lunch...
					Thread.sleep(l.getTime());
				} catch (InterruptedException e) {
					// this must not happen
				}
				return l.getNextStep();
				// quit = false;
			}// !pause

			pause = false;
			return Link.END;

		} // switch

		playButton.setEnabled(step != ani.getLastStep());
		return Link.END;
	} // nextStep()

	/**
	 * sets the Animation for the AnimationApplet. If the window is visible, the
	 * current step will be displayed.
	 */
	boolean setAnimation(Animation animation) {
		if (!isInitialized())
			init();
		ani = new AnimationState(animation);

		GraphicVector objects = ani.getCurrentObjects();
		animationCanvas.setObjects(objects);
		setStep(ani.getStep(), true);
		ticks = 0;
		ani.reset();
		playButton.setEnabled(true);
		return true;

	}

	public void errorMsg(String msg) {
		showStatus(msg);
		if (debugMode)
			System.err.println("****error: " + msg);
	}

	private boolean loadFile(String filename) {
		return loadFile(filename, AnimalConfiguration.DEFAULT_FORMAT);
	}

	private boolean loadFile(String filename, String targetFormat) {
		AnimationImporter importer = AnimationImporter.getImporterFor(targetFormat);
		if (importer != null) {
			Animation tmpAnimation = importer.importAnimationFrom(filename);
			return setAnimation(tmpAnimation);
		}
		return false;
	}

	public String getAppletInfo() {
		return "Animal Applet frontend(c) 1999 Guido R\u00FC\u00DFling";
	}

	public String[][] getParameterInfo() {
		String[][] info = { // Parameter Name Kind of Value Description
				{ "Animation", "URL", "the animation to be played" } };
		return info;
	}

	/**
	 * displays an information message in the output TextArea in Animal's main
	 * window's center. This is just a shortcut for
	 * <code>errorMsg(msg,INFO)</code>.
	 * 
	 * @param msg
	 *          the text to be displayed
	 */
	public void message(String msg) {
		showStatus(msg);
	}

	/**
	 * Hauptprogramm; legt nur ein neues Objekt an und started activateDebugging.
	 * 
	 * @param args
	 *          Die Parameter des Aufrufs(ignoriert)
	 */
	public static void main(String[] args) {
		java.awt.Frame f = new java.awt.Frame("Animation Applet");

		AnimationApplet animationApplet = new AnimationApplet();

		animationApplet.init();

		animationApplet.start();

		f.add("Center", animationApplet);

		f.setSize(480, 360);
		f.setVisible(true);
	}
}
