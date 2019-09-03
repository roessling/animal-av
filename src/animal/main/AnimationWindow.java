package animal.main;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import animal.animator.Animator;
import animal.animator.InteractionElement;
import animal.api.DragAndDropPanelInJScrollPanel;
import animal.graphics.PTGraphicObject;
import animal.gui.AnimalMainWindow;
import animal.gui.GraphicVector;
import animal.gui.JImagePanel;
import animal.misc.XProperties;

/**
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling</a>
 * @version 1.0 13.07.1998
 */
public class AnimationWindow implements ActionListener {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */

	public static final double MAX_SPEED_FACTOR = 10.0;

	public static final int PADDING_SCROLLBAR_RIGHT = 20;

	public static final int PADDING_TOOLBARS_TOP_BOTTOM = 80;

	/** the AnimationState to work with */
	private AnimationState ani = null;

	/**
	 * determines whether a pause is to be done after the next step if they are
	 * linked with a time link. If there is a key link, a pause is done anyway.
	 * will be checked in setStep before the next step is set
	 */
	private boolean pause = false;

	private boolean slideShowMode = false;

	private boolean forwardMode = true;

	private Hashtable pictureTable = new Hashtable();
	/**
	 * ticks elapsed since AnimationStart. Needed for synchronisation of the
	 * Animation
	 */
	private double ticks;

	private Timer timer;

	private double speed = 1;

  private boolean isSlowMode = false;

  private boolean showQuestions = true;
        
  private AnimationWindowView view;

	/**
	 * construct an AnimationWindow. Actual initialization is done in
	 * <code>init</code>.
	 * 
	 * @param animalInstance
	 *          the current Animal instance
	 * @param properties
	 *          the current animation properties
	 * @see #init()
	 */
	public AnimationWindow(Animal animalInstance, XProperties properties) {
      view = new AnimationWindowView(animalInstance, properties, this);
	}

	/**
	 * construct an AnimationWindow. Actual initialization is done in
	 * <code>init</code>.
	 * 
	 * @param animalInstance
	 *          the current Animal instance
	 * @param properties
	 *          the current animation properties
	 * @param aContainer
	 *          the container that contains this component
	 * @see #init()
	 */
	public AnimationWindow(Animal animalInstance, XProperties properties,
			Container aContainer) {
	    view = new AnimationWindowView(animalInstance, properties, aContainer, this);
	}


	/**
	 * initializes the AnimationWindow by adding the control panel and the
	 * AnimationCanvas.
	 */
	public void init() {
	    view.init();
	}
        
	public JScrollPane getScrollPane(){
	    return view.getScrollPane();
  }
        
  public boolean isVisible(){//TODO
//    return true;
//    return view.isVisible();
    return view!=null && view.getUnderContentPane()!=null && view.getUnderContentPane().isVisible();
  }
        
  public int getViewWidth(){
      return view.getWidth();
  }
        
  public int getViewHeight(){
      return view.getHeight();
  }
        
  public void updatePack(){
      view.pack();
  }
  
  public void setSnapshot(){
    if (view!=null && isVisible() && view.getAnimationCanvas()!=null) {
      AnimationCanvas canvas = view.getAnimationCanvas();
      Image currentImage = canvas.getCurrentImage();
      if(currentImage!=null) {
        Dimension dimFrame = new Dimension(500, 500);
        ImageIcon ii = new ImageIcon(currentImage);
//      Dimension dimCanvas = canvas.getDrawingsSize();
        Dimension dimCanvas = new Dimension(ii.getIconWidth(), ii.getIconHeight());
        JImagePanel panelSnapshot = new JImagePanel();
        Image cloneOfImageAtStep = currentImage.getScaledInstance(dimCanvas.width,dimCanvas.height, Image.SCALE_SMOOTH);
        Image imageNewWithCut = null;
        try {
          BufferedImage dest = new BufferedImage(cloneOfImageAtStep.getWidth(null), cloneOfImageAtStep.getHeight(null), BufferedImage.TYPE_INT_RGB);
          Graphics2D g2 = dest.createGraphics();
          g2.drawImage(cloneOfImageAtStep, 0, 0, null);
          g2.dispose();
          File tempFile = File.createTempFile("SnapshotStep"+getStep(), null);
          ImageIO.write(dest.getSubimage(0, 0, canvas.getDrawingsSize().width, canvas.getDrawingsSize().height), "png", tempFile);
          imageNewWithCut  = ImageIO.read(tempFile);
        } catch (IOException e) {}
        if(imageNewWithCut!=null) {
          dimCanvas = canvas.getDrawingsSize();
          panelSnapshot.setImage(imageNewWithCut);
        } else {
          panelSnapshot.setImage(cloneOfImageAtStep);
        }
        panelSnapshot.setMinimumSize(dimCanvas);
        panelSnapshot.setMaximumSize(dimCanvas);
        panelSnapshot.setPreferredSize(dimCanvas);
        panelSnapshot.setBackground(canvas.getBackgroundColor());
        
        JScrollPane scrollPanelSnapshot = new JScrollPane(panelSnapshot);
        scrollPanelSnapshot.setPreferredSize(dimFrame);
        DragAndDropPanelInJScrollPanel listenerDragAndDrop = new DragAndDropPanelInJScrollPanel(panelSnapshot);
        panelSnapshot.addMouseListener(listenerDragAndDrop);
        panelSnapshot.addMouseMotionListener(listenerDragAndDrop);

        JFrame snapshotFrame = new JFrame("Snapshot from: "+view.getTitle()+" at step "+getStep());
        snapshotFrame.setIconImage(view.getIconImage());
        snapshotFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        snapshotFrame.add(scrollPanelSnapshot);
        snapshotFrame.setMinimumSize(new Dimension(100, 100));
        snapshotFrame.setPreferredSize(dimFrame);
        snapshotFrame.pack();
//        snapshotFrame.setResizable(false);
        snapshotFrame.setVisible(true);
        scrollPanelSnapshot.getViewport().setViewPosition(new Point(0,0));
      }
    }
    view.updateCanvas();
  }
        
  public boolean isInitialized(){
      return view.isInitialized();
  }
        
  public void setTitle(String title){
      view.setTitle(title);
  }
	/**
	 * overwritten to initialize the internal Animation. When the window is not
	 * visible, all methods can be called, but they don't make a change until the
	 * window is made visible. Then perform the commands from here.
	 * 
	 * @param isVisible
	 *          if true, ensure the window is visible
	 */
	public void setVisible(boolean isVisible) {
//    view.setVisible(isVisible);
    if (isVisible) {
      if (ani == null) {
        setAnimation(Animation.get());
        setStep(ani.getFirstRealStep(), true);
      }
      setAnimationPlayerHooked(animationPlayerHooked);
    }
	}
	
	private JPanel animationContainer = null;
	private boolean animationPlayerHooked = true;
	public void setAnimationPlayerHooked(boolean hook) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        JPanel mainWindowContentPanel = (JPanel)AnimalMainWindow.getWindowCoordinator().getAnimalMainWindow().getContentPane();
        if(hook) {
          view.getUHookPlayerButton().setText("Unhook Player");
          animationContainer = view.getUnderContentPane();
          view.remove(animationContainer);
          mainWindowContentPanel.add(BorderLayout.CENTER, animationContainer);
          view.setVisible(false);
        } else {
          view.getUHookPlayerButton().setText("Hook Player");
          mainWindowContentPanel.remove(animationContainer);
          view.addPanelFullScreenToWorkContainer(animationContainer);
          view.setVisible(true);
          
        }
        animationPlayerHooked = hook;
        mainWindowContentPanel.updateUI();
        animationContainer.updateUI();
      }
    });
	}
	
	public boolean isAnimationPlayerHooked() {
	  return animationPlayerHooked;
	}

  /**
   * play the current animation step in reverse mode
   */
  public void reversePlay() {  // Not needed anymore
    setSlideShowMode(false);
    forwardMode = false;
    setReverseStep(ani.getStep(), false);
  }

  /**
   * play the current animation animation step in "normal" (forward) mode
   */
  public void playAnimation() {  // Not needed anymore
    setSlideShowMode(false);
    setStep(ani.getNextStep(), false);
  }


	/**
	 * jump to the start of the animation
	 */
	public void startOfAnimation() {  // Needed
		setSlideShowMode(false);
		setStep(ani.getFirstRealStep(), true);
	}

  /**
   * jump to the end of the animation
   */
  public void endOfAnimation() {  // Needed
    setSlideShowMode(false);
    setStep(ani.getLastStep(), true);
  }

  /**
   * play the animation in slide show mode
   */
  public void slideShow() {  // Needed
    setSlideShowMode(true);
    forwardMode = true;
    pause = false;
    setStep(ani.getNextStep(), false);
  }

	/**
	 * play the animation in reverse slide show mode
	 */
	public void reverseSlideShow() {  //Needed
		setSlideShowMode(true);
		forwardMode = false;
		pause = false;

		// show normally request delay now!
		setReverseStep(ani.getStep(), false);
	}

	/**
	 * pause the animation
	 */
	public void pauseAnimation() {  //Needed
		pause = true;
		setSlideShowMode(false);
    view.updateAllToCurStep();
	}

	/**
	 * jump to the next animation step without animating the effects
	 */
	public void forwardAnimation() {  // Needed
		setSlideShowMode(false);
		setStep(ani.getNextStep(), false);
	}

  /**
   * go back one step in the animation
   */
  public void backwardAnimation() {  // Needed
    setSlideShowMode(false);
    setStep(ani.getPrevStep(), true);
  }

	/**
	 * toggle the slide show mode on / off
	 * 
	 * @param mode
	 *          if true, use slide show mode, else do not use it.
	 */
	public void setSlideShowMode(boolean mode) {
		slideShowMode = mode;
	}

	/**
	 * returns the current slide show mode
	 * 
	 * @return true if in slide show mode, else false
	 */
	public boolean getSlideShowMode() {
		return slideShowMode;
	}

	/**
	 * set the current magnification factor to "factor"
	 * 
	 * @param factor
	 *          the target magnification factor
	 */
	public void setMagnification(double factor) {
		view.setMagnification(factor);
	}

	

	/**
	 * retrieves the current magnification factor of the display
	 * 
	 * @return the current magnification factor as a double
	 */
  public double getMagnification() {
    return view.getMagnification();
  }

  public double getMagnificationMaxValue() {
    return view.getMagnificationMaxValue();
  }

  public double getMagnificationMinValue() {
    return view.getMagnificationMinValue();
  }

  public double getMagnificationStepValue() {
    return view.getMagnificationStepValue();
  }

  public JSlider getMagnificationSlider() {
    return view.getMagnificationSlider();
  }

	/**
	 * assigns the current button controller
	 * 
	 * @param controller
	 *          the ButtonController for this instance of the animation window
	 */
	//public void setButtonController(ButtonController controller) {
		//animationControlToolBar.setButtonController(controller);
	//}

	/**
	 * sets the step for the AnimationWindow.
	 * 
	 * @param step
	 *          the target step
	 * @param immediate
	 *          if true, the state of the Animation after executing the step is
	 *          displayed. If false, the Animators of this step are executed
	 *          visibly. In the end, the same state is reached.
	 */
	public void setStep(int step, boolean immediate) {
	  int theStep = step;
		// forwardMode = true;

		if (isVisible() && ani!=null) { // don't work if the window is not visible anyway

			int next = 0;
			if (ani.getAnimation() == null)
				return;
			theStep = ani.getAnimation().verifyStep(theStep);

			if (ani != null) { // prepare the AnimationState object
				ani.setStep(theStep, !immediate);
			}

			AnimalMainWindow.getWindowCoordinator().getAnnotationEditor(false)
					.setCurrentStep(theStep);

			// paint the objects directly...
			if (immediate) {
				view.updateCanvas();
				next = Link.END;
			} else {
				// ...or execute the Animators slowly
				next = nextStep();

				if (next != Link.END) {
					// the next step has to be executed right now because
					// there was a time link between the steps and the
					// pause button was not pressed
				  theStep = next;
				}
			}
			addPicture(step);
	    view.updateAllToCurStep();
		}
	}

	/**
	 * sets the step for the AnimationWindow.
	 * 
	 * @param step
	 *          the target animation step
	 * @param immediate
	 *          if true, the state of the Animation after executing the step is
	 *          displayed. If false, the Animators of this step are executed
	 *          visibly. In the end, the same state is reached.
	 */
	public void setReverseStep(int step, boolean immediate) {
	  int theStep = step;
		forwardMode = false;

		if (isVisible()) { // don't work if the window is not visible anyway

			int next = 0;
			theStep = ani.getAnimation().verifyStep(theStep);

			if (ani != null) { // prepare the AnimationState object
				ani.setStep(theStep, !immediate);
			}

			AnimalMainWindow.getWindowCoordinator().getAnnotationEditor(false)
					.setCurrentStep(theStep);

			// paint the objects directly...
			if (immediate) {
				view.updateCanvas();
				next = Link.END;
			} else {
				// ...or execute the Animators slowly
				next = reversePlayStep();

				if (next != Link.START) {
					// the next step has to be executed right now because
					// there was a time link between the steps and the
					// pause button was not pressed
					setStep(next, true);
				}

				theStep = next;
			}
	    view.updateAllToCurStep();
		}
	}

	/**
	 * returns the current step
	 * 
	 * @return the current animation step
	 */
	public int getStep() {
		return ani.getStep();
	}

	private LinkedList<String> executedQuestions = new LinkedList<String>();
	/**
	 * executes one step by executing all Animators "slowly"(i.e. not at once but
	 * according to their execution time).
	 * 
	 * @return next step to be executed immediately afterwards; Link.END, if none
	 *         such step exists
	 */
	public int nextStep() {
		Vector<Animator> animators; // Animators to execute in this step
		Animator animator; // Animator currently processed
		long time; // for synchronization
		forwardMode = true;

		int step = ani.getStep();

		// required because the playbutton can be pressed by pressing space
		// even when disabled!
    //view.enablePlayButton(false);

		// get the Animators that still have to be processed to reach the next
		// step. They are left by AnimationState.setStep if immediate was false.
		animators = ani.getCurrentAnimators();
		time = System.currentTimeMillis();

		// initialize each Animator by linking it with AnimationWindow's
		// AnimationState object and resetting its start time and ticks
		int initTicks = (int) Math.round(ticks);

		for (int a = 0; a < animators.size(); a++)
			animators.elementAt(a).init(ani, time, initTicks);

		boolean finished = false; // no more Animators to be processed?

		while (!finished) {
			finished = true;
			time = System.currentTimeMillis();

			// ticks++;
			ticks += speed;

			// execute each Animator slowly
			for (int a = 0; a < animators.size();) {
				animator = animators.elementAt(a);

				if (animator != null) {
          if(animator instanceof InteractionElement) {
            String uniqueAnimatorString = animator.getStep()+animator.getAnimatorName()+animator.getFileVersion()+animator.getType();
            if(!executedQuestions.contains(uniqueAnimatorString) && showQuestions) {
              animator.action(time, ticks);
              executedQuestions.add(uniqueAnimatorString);
              setSlideShowMode(false);
            } else {
              animator.setFinished(true);
            }
          } else {
            animator.action(time, ticks);
          }

					// and if this was its last action, remove it from the
					// list of Animators to be processed
					if (animator.hasFinished()) {
						animators.removeElementAt(a);
					} else {
						// if it's not yet removed, it has to be processed once
						// more in the next loop.
						finished = false;
						a++;
					}
				}
			}

			// after all Animators have executed once, display the new state
			view.updateCanvas();
	    view.updateAllToCurStep();
		}

		// check link to the next step
		Link l = ani.getAnimation().getLink(step);

		if (getSlideShowMode() && !pause) {
			int delay = l.getTime();

			if (delay == 0) {
				delay = Animal.getSlideShowDelay();
			}
			
			int targetDelay = (speed < 0.01) ? delay : 
				(int) Math.round(delay / speed);
			
			if (timer == null) {
				timer = new Timer(targetDelay, this);
			} else {
				timer.setDelay(targetDelay);
				timer.setInitialDelay(targetDelay);
			}

			timer.setRepeats(false);
			timer.start();

			return l.getNextStep();
		} 
		switch (l.getMode()) {
		case Link.WAIT_KEY: // do not automatically process the next step
			return Link.END;
			
		case Link.WAIT_TIME:
			
			if (!pause) { // pause button has not been pressed
				int delay = l.getTime();
				
				if (delay == 0) {
					delay = Animal.getSlideShowDelay();
				}
				
				int targetDelay = (speed < 0.01) ? delay : 
					(int) Math.round(delay / speed);
				
				if (timer == null) {
					timer = new Timer(targetDelay, this);
				} else {
					timer.setDelay(targetDelay);
					timer.setInitialDelay(targetDelay);
				}
				
				timer.setRepeats(false);
	      Thread callTimerActionThread = new Thread(){
	        public void run(){
	          while(!timerActionFinished) {
	            try {
	              Thread.sleep(1);
	            } catch (InterruptedException e) {}
	          }
	          timer.start();
	        }
	      };
	      callTimerActionThread.start();
				
				return l.getNextStep();
			} // !pause
			// pause button pressed. Then return that next step
			// must not be processed now
			pause = false;
			
			return Link.END;

			
		case Link.WAIT_CLICK:
			JOptionPane.showMessageDialog(view, l.getClickPrompt());
			
			// disable the controls -- are re-activated by
			// AnimationCanvasMouseListener if
			// the "right" object is hit.
			// enableControls(false);
			return Link.END;
			
			// case Link.REPEAT: // not yet implemented
			// if (pause || !pause) {
			// return l.breakStep;
			// } else {
			// return l.getNextStep();
			// }
			// break;
		} // switch



    view.updateAllToCurStep();
		return Link.END;
	} // nextStep()

	/**
	 * executes one step by executing all Animators "slowly"(i.e. not at once but
	 * according to their execution time).
	 * 
	 * @return next step to be executed immediately afterwards; Link.END, if none
	 *         such step exists
	 */
	public int reversePlayStep() {
		Vector<Animator> animators; // Animators to execute in this step
		Animator animator; // Animator currently processed
		long time; // for synchronization

		int step = ani.getStep();

		// get the Animators that still have to be processed to reach the next
		// step. They are left by AnimationState.setStep if immediate was false.
		animators = ani.getCurrentAnimators();
		time = System.currentTimeMillis();

		int a = 0;
		Link currentLink = ani.getAnimation().getLink(step);
		int lengthOfStep = currentLink.getDurationInTicks();

		int initialTicks = (int) (Math.round(ticks));
		int referenceTicks = initialTicks + lengthOfStep;

		// initialize each Animator by linking it with AnimationWindow's
		// AnimationState object and resetting its start time and ticks
		Animator currentAnimator = null;

		for (a = 0; a < animators.size(); a++) {
			currentAnimator = animators.elementAt(a);
			if (currentAnimator != null) {
				currentAnimator.init(ani, time, initialTicks);
			}
		}

		boolean finished = false; // no more Animators to be processed?

		while (!finished) {
			finished = true;
			time = System.currentTimeMillis();
			referenceTicks -= speed;
			ticks += speed;

			// execute each Animator slowly
			for (a = 0; a < animators.size(); a++) {
				animator = animators.elementAt(a);
				animator.setFinished(false);

				// perform an action on the Animator
				animator.action(time, referenceTicks);
			}

			// after all Animators have executed once, display the new state
			view.updateCanvas();
			finished = initialTicks >= referenceTicks;
		}

		// check link from the previous step
		int prevStep = ani.getPrevStep();

		Link l = ani.getAnimation().getLink(prevStep);

		if (getSlideShowMode() && !pause) {
			int delay = l.getTime();

			if (delay == 0) {
				delay = Animal.getSlideShowDelay();
			}

      int targetDelay = (speed < 0.01) ? delay :
        (int) Math.round(delay / speed);
			if (timer == null) {
				timer = new Timer(targetDelay, this);
			} else {
				timer.setDelay(targetDelay);
	      timer.setInitialDelay(targetDelay);
			}
			timer.setRepeats(false);
			Thread callTimerActionThread = new Thread(){
		    public void run(){
	        while(!timerActionFinished) {
	          try {
	            Thread.sleep(1);
	          } catch (InterruptedException e) {}
	        }
	        timer.start();
		    }
		  };
		  callTimerActionThread.start();

			// return l.getNextStep();
			// Animal.logDebugInfo("Slide show mode... step number: " +prevStep);
			return prevStep;
		} 
		switch (l.getMode()) {
		case Link.WAIT_KEY: // do not automatically process the next step
			
			// return Link.END;
			// Animal.logDebugInfo("*** @switch WAIT_KEY, return " +prevStep);
			return prevStep;
			
		case Link.WAIT_TIME:
			/*
			 * if (!pause) { // pause button has not been pressed
			 * 
			 * if (timer == null) { timer = new Timer(l.getTime(), this); } else {
			 * timer.setDelay(l.getTime()); }
			 * 
			 * timer.setRepeats(false); // timer.setCoalesce(true); timer.start(); //
			 * Animal.logDebugInfo("Next step in reverse direction: " +prevStep);
			 * return prevStep; } // !pause else { // pause button pressed. Then
			 * return that next step // must not be processed now pause = false; //
			 * Animal.logDebugInfo("*** @switch WAIT_TIME, not paused, return "
			 * +prevStep); return prevStep; // return Link.END; }
			 */
			return prevStep;
			// case Link.REPEAT: // not yet implemented
			// if (pause || !pause) {
			// return l.breakStep;
			// } else {
			// return l.getNextStep();
			// }
			// break;
		} // switch


		// Animal.logDebugInfo("*** finally return " +prevStep);
		return prevStep;

		// return Link.END;
	} // nextStep()

	/**
	 * executes one step by executing all Animators "slowly"(i.e. not at once but
	 * according to their execution time).
	 * 
	 * @return next step to be executed immediately afterwards; Link.END, if none
	 *         such step exists
	 */
	public Rectangle determineImageSize() {
		Vector<Animator> animators; // Animators to execute in this step
		Animator animator; // Animator currently processed
		long time; // for synchronization
		Rectangle size = new Rectangle(0, 0, 1, 1);
		int step = Link.START;
		ani.setStep(Link.START, false);

		for (step = ani.getNextStep(); step != Link.END; step = ani.getNextStep()) {
			ani.setStep(step, false);
			animators = ani.getCurrentAnimators();
			time = System.currentTimeMillis();

			// initialize each Animator by linking it with AnimationWindow's
			// AnimationState object and resetting its start time and ticks
			int initialTicks = (int) (Math.round(ticks));
			ticks = initialTicks;

			for (int a = 0; a < animators.size(); a++)
				animators.elementAt(a).init(ani, time, initialTicks);

			boolean finished = false; // no more Animators to be processed?
			int[] currentObjects = null;
			PTGraphicObject currentObject = null;

			while (!finished) {
				finished = true;
				time = System.currentTimeMillis();
				ticks++;

				// execute each Animator slowly
				for (int a = 0; a < animators.size();) {
					animator = animators.elementAt(a);

					// perform an action on the Animator
					animator.action(time, ticks);
					currentObjects = animator.getObjectNums();

					for (int i = 0; i < currentObjects.length; i++) {
						currentObject = ani.getCloneByNum(currentObjects[i]);
						size = SwingUtilities.computeUnion(0, 0, size.width, size.height,
								currentObject.getBoundingBox());
					}

					// and if this was its last action, remove it from the
					// list of Animators to be processed
					if (animator.hasFinished()) {
						animators.removeElementAt(a);
					} else {
						// if it's not yet removed, it has to be processed once
						// more in the next loop.
						finished = false;
						a++;
					}
				}
			}
		}

		return size;
	} // determineImageSize()

	public AnimationCanvas getAnimationCanvas(){
	    return view.getAnimationCanvas();
  }
        
	/**
	 * sets the Animation for the AnimationWindow. If the window is visible, the
	 * current step will be displayed.
	 * 
	 * @param animation
	 *          the new animation
	 */
	void setAnimation(Animation animation) {
		if (!view.isInitialized()) {
			init();
		}

		ani = new AnimationState(animation);
		executedQuestions = new LinkedList<String>();
		view.setAnimationtate(ani);
		
		GraphicVector objects = ani.getCurrentObjects();
		
		//---
		view.setCanvasObject(objects);
		//setStep(ani.getStep(), true);
		setStep(ani.getFirstRealStep(), true);
		ticks = 0;
		ani.reset();
		setVisible(true);
	}

	/**
	 * generates a String label for the step number passed in
	 * 
	 * @param stepNr
	 *          the target step number
	 * @return a String representation of the step number
	 */
	public String setLabelForStep(int stepNr) {
		return String.valueOf(stepNr);
	}

	/**
	 * returns the AnimationCanvas instance on which the elements are painted
	 * 
	 * @return the current AnimationCanvas
	 */
//	public AnimationCanvas getAnimationCanvas() {
//		return animationCanvas;
//	}

	/**
	 * returns the current animation state as an Image
	 * 
	 * @return an image representing the current animation state
	 */
	public Image getCurrentImage() {
		return view.getCurrentImage();
	}

	/**
	 * sets the current display mode to "slow" or "normal"
	 * 
	 * @param isSlow
	 *          if true, activate "slow mode"
	 */
	public void setSlowMode(boolean isSlow) {
		isSlowMode = isSlow;
	}

  public void setSpeed(double speedValue) {
   // System.out.println("speed" + speedValue);
    if (speedValue > 0 && speedValue <= AnimationWindow.MAX_SPEED_FACTOR) {
      speed = speedValue;
    }
  }

  public double getSpeed() {
    return speed;
  }

	/**
	 * determines the current status of the "slow mode" (on or off)
	 * 
	 * @return true if the animation is in "slow mode"
	 */
	public boolean getSlowMode() {
		return isSlowMode;
	}

	public boolean isShowQuestions() {
    return showQuestions;
  }

  public void setShowQuestions(boolean showQuestions) {
    this.showQuestions = showQuestions;
  }

  private boolean timerActionFinished = true;
  /**
	 * handles action events, which are always caused by the animation timer
	 * 
	 * @param actionEvent
	 *          the event to be handled
	 */
	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getSource() == timer) {
		  timerActionFinished = false;
			// boolean continueLoop = !pause;

			if (!pause)// {
			// while (continueLoop)
			{
				// determine current step and next step to show (previous/next step)
        int nextStep = Link.END;
        int curStep = ani.getStep();

				// determine current step and next step to show (previous/next step)
				// int thisStep = ani.getStep();
				// Link l = null;

				if (forwardMode) {
					nextStep = ani.getNextStep();
					// l = ani.getAnimation().getLink(thisStep);
				} else {
//				  while(prevStepToWait!=-1 && prevStepToWait!=ani.getStep()) {
//				    try {
//              Thread.sleep(1);
//            } catch (InterruptedException e) {}
//				    System.out.println("WaitForCurrentStep");
//				  }
//				  curStep = ani.getStep();
          nextStep = ani.getPrevStep();
					// l = ani.getAnimation().getLink(nextStep);
				}

        
				if (forwardMode && (nextStep != Link.END)) {
					setStep(nextStep, false);
				} else if (!forwardMode && (nextStep != Link.START)) {
          setReverseStep(curStep, false);
				}

        if(nextStep == Link.START || nextStep == Link.END ||
            (forwardMode && nextStep == ani.getLastStep())
            || (!forwardMode && nextStep == ani.getFirstRealStep())) {
          pauseAnimation();
          setSlideShowMode(false);
        }
				// continue if not paused and (stepDelay > 0 || slide show mode active)
				// continueLoop = !pause && ((l.getTime() > 0) || getSlideShowMode()) &&
				// (nextStep != Link.START) && (nextStep != Link.END);
			}
      timerActionFinished = true;
		}
	}

	/**
	 * returns the current animation state object
	 * 
	 * @return the current state of the animation
	 */
	public AnimationState getAnimationState() {
		return ani;
	}

	public Rectangle getViewBounds(){
    return view.getBounds();
  }
        
  public void getViewProperties(XProperties properties){
      view.getProperties(properties);
  }

  public void reverseStep() {
    setStep(ani.getStep(), true);
  }

  public void setAnimationWindowLocale(Locale targetLocale) {
    view.changeLocale(targetLocale);
  }

  public AnimationWindowView getAnimationWindowView() {
    return view;
  }

  /**
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public void zoom(boolean zoomIn) {
    if (view != null)
      view.zoom(zoomIn);

  }
  /**
   * saves all steps as image in a hastable with the step as the key
   * 
   * @param step
   */
  public void addPicture(int step) {
     if (view!=null && isVisible() && view.getAnimationCanvas()!=null) {
          AnimationCanvas canvas = view.getAnimationCanvas();
          Image currentImage = canvas.getCurrentImage();
          if(currentImage!=null) {
            Dimension dimFrame = new Dimension(500, 500);
            ImageIcon ii = new ImageIcon(currentImage);
//          Dimension dimCanvas = canvas.getDrawingsSize();
            Dimension dimCanvas = new Dimension(ii.getIconWidth(), ii.getIconHeight());
            JImagePanel panelSnapshot = new JImagePanel();
            Image cloneOfImageAtStep = currentImage.getScaledInstance(dimCanvas.width,dimCanvas.height, Image.SCALE_SMOOTH);
            Image imageNewWithCut = null;
            try {
              BufferedImage dest = new BufferedImage(cloneOfImageAtStep.getWidth(null), cloneOfImageAtStep.getHeight(null), BufferedImage.TYPE_INT_RGB);
              Graphics2D g2 = dest.createGraphics();
              g2.drawImage(cloneOfImageAtStep, 0, 0, null);
              g2.dispose();
          int width = canvas.getDrawingsSize().width;
          if (dest.getWidth() < width)
            width = dest.getWidth();
          int height = canvas.getDrawingsSize().height;
          if (dest.getHeight() < height)
            height = dest.getHeight();
              File tempFile = File.createTempFile("Step"+step, null);
          ImageIO.write(dest.getSubimage(0, 0, width, height), "png", tempFile);
              imageNewWithCut  = ImageIO.read(tempFile);
            } catch (IOException e) {}
            if(imageNewWithCut!=null) {
          pictureTable.put(step, imageNewWithCut);
            }

          }
    }
  }

  /**
   * 
   * @param step
   * @return the picture of the step corresponding to the given step key
   */
  public Image getImageForStep(int step) {
    return (Image) pictureTable.get(step);
  }
}
