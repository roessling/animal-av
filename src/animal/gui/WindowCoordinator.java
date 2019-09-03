/*
 * Created on 16.07.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package animal.gui;

import javax.swing.JOptionPane;

import animal.editor.AnnotationEditor;
import animal.main.Animal;
import animal.main.AnimalConfiguration;
import animal.main.AnimalToPDFWindow;
import animal.main.AnimationWindow;
import animal.main.TimeLineWindow;
import animal.misc.HiddenObjectList;

/**
 * @author Guido
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class WindowCoordinator {
	/**
	 * the time line window
	 */
	private TimeLineWindow timeLineWindow;

	/**
	 * the annotation editor window
	 */
	private AnnotationEditor annotationEditor;

	/**
	 * the animation overview window
	 */
	private AnimationOverview animationOverview;

	/**
	 * the animation window
	 */
	private AnimationWindow animationWindow;
	
	/**
   *  the Animal to PDF control window
   */
  
  private AnimalToPDFWindow pdfWindow;
	/**
	 * the drawing window
	 */
	private DrawWindow drawWindow;

	/**
	 * the hidden objects list ("objects window")
	 */
	private HiddenObjectList objectsWindow;

	/**
	 * the time line window
	 */
	private VariableView variableView;

	/**
	 * determine if the animation overview window is visible at the moment
	 */
	private boolean animationOverviewVisible = false;

	/**
	 * determine if the animation window is visible at the moment
	 */
	private boolean animationWindowVisible = false;

	/**
   * determine if the Animal to PDF window is visible at the moment
   */
  private boolean pdfWindowVisible = false;

	/**
	 * a reference to the current Animal instance
	 */
	private Animal animalInstance;

	 /**
	 * a reference to the current Animal main window
	 */
	 private AnimalMainWindow animalMainWindow;

	/**
	 * determine if the annotationwindow is visible at the moment
	 */
	private boolean annotationWindowVisible = false;

	/**
	 * determine if the drawing window is visible at the moment
	 */
	private boolean drawWindowVisible = false;

	/**
	 * determine if the objects window is visible at the moment
	 */
	private boolean objectsWindowVisible = false;

	/**
	 * determine if the time line window is visible at the moment
	 */
	private boolean timeLineWindowVisible = false;

	/**
	 * determine if the variables window is visible at the moment
	 */
	private boolean variableViewVisible = false;

	/**
	 * creates the local window coordinator
	 * 
	 * @param animal
	 *          the current Animal instance
	 * @param animalMainWin
	 *          the current Animal main window
	 */
	public WindowCoordinator(Animal animal, AnimalMainWindow animalMainWin) {
		// store the current Animal instance
		animalInstance = animal;

		// if null, generate a new Animal instance one!
		if (animalInstance == null) {
			animalInstance = Animal.get();
		}

		// // store the current Animal main window
		 animalMainWindow = animalMainWin;
	}

	/**
	 * return the current AnimationOverview. If none exists, create a new one.
	 * 
	 * @param init
	 *          if true, initialize the window, otherwise just return a
	 *          valid(non-null) reference to an AnimationOverview, whether
	 *          initialized or not.
	 * @return the current AnimationOverview, non-null.
	 */
	public AnimationOverview getAnimationOverview(boolean init) {
		if (animationOverview == null) {
			animationOverview = new AnimationOverview(animalInstance,
					AnimalConfiguration.getDefaultConfiguration().getProperties());
		}

		if (init && !animationOverview.isInitialized()) {
			animationOverview.init();
		}

		return animationOverview;
	}

	/**
	 * return the current AnimationWindow. If none exists, create a new one.
	 * 
	 * @param init
	 *          if true, initialize the window, otherwise just return a
	 *          valid(non-null) reference to an AnimationWindow, whether
	 *          initialized or not.
	 * @return the current AnimationWindow, non-null.
	 */
	public AnimationWindow getAnimationWindow(boolean init) {
		if (animationWindow == null) {
			animationWindow = new AnimationWindow(animalInstance, AnimalConfiguration
					.getDefaultConfiguration().getProperties());
		}

		if (init && !animationWindow.isInitialized()) {
			animationWindow.init();
		}

		return animationWindow;
	}

	/**
   * return the AnimalToPDF. If none exists, create a new one.
   * 
   * @param init
   *            if true, initialize the window, otherwise just return a
   *            valid(non-null) reference to an AnimationWindow, whether
   *            initialized or not.
   * @return the current AnimalToPDFWindow, non-null.
   */
  public AnimalToPDFWindow getAnimalToPDFWindow(boolean init) {
    if (pdfWindow == null) {
      pdfWindow = new AnimalToPDFWindow(animalInstance, AnimalConfiguration
          .getDefaultConfiguration().getProperties());
    }

    if (init && !pdfWindow.isInitialized()) {
      pdfWindow.init();
    }

    return pdfWindow;
  }
	/**
	 * return the current TimeLineWindow. If none exists, create a new one.
	 * 
	 * @param init
	 *          if true, initialize the window, otherwise just return a
	 *          valid(non-null) reference to an TimeLineWindow, whether
	 *          initialized or not.
	 * @return the current TimeLineWindow, non-null.
	 */
	public AnnotationEditor getAnnotationEditor(boolean init) {
		if (annotationEditor == null) {
			annotationEditor = new AnnotationEditor(animalInstance,
					AnimalConfiguration.getDefaultConfiguration().getProperties(),
					AnimalConfiguration.getDefaultConfiguration().getProperties()
							.getProperty("Animal.user", "guido"), AnimalConfiguration
							.getDefaultConfiguration());
		}

		return annotationEditor;
	}

	/**
	 * return the current <strong>DrawWindow</strong>. If none exists, create a
	 * new one.
	 * 
	 * @param init
	 *          if true, initialize the window, otherwise just return a
	 *          valid(non-null) reference to a DrawWindow, whether initialized or
	 *          not.
	 * @return the current DrawWindow, non-null.
	 */
	public DrawWindow getDrawWindow(boolean init) {
	  	      
		if (drawWindow == null) {
			drawWindow = new DrawWindow(animalInstance, AnimalConfiguration
					.getDefaultConfiguration().getProperties());
		}

		if (init && !drawWindow.isInitialized()) {
			drawWindow.init();
		}

		return drawWindow;
	}

	/**
	 * return the current ObjectsWindow. If none exists, create a new one.
	 * 
	 * @param init
	 *          if true, initialize the window, otherwise just return a
	 *          valid(non-null) reference to an ObjectsWindow, whether initialized
	 *          or not.
	 * @return the current ObjectsWindow, non-null.
	 */
	public HiddenObjectList getObjectsWindow(boolean init) {
		if (objectsWindow == null) {
			objectsWindow = new HiddenObjectList(animalInstance, animalInstance
					.getAnimation());
		}

		objectsWindow.setStep(getDrawWindow(init).getStep());

		return objectsWindow;
	}
	
	public AnimalMainWindow getAnimalMainWindow() {
	  return animalMainWindow;
	}

	/**
	 * return the current TimeLineWindow. If none exists, create a new one.
	 * 
	 * @param init
	 *          if true, initialize the window, otherwise just return a
	 *          valid(non-null) reference to an TimeLineWindow, whether
	 *          initialized or not.
	 * @return the current TimeLineWindow, non-null.
	 */
	public TimeLineWindow getTimeLineWindow(boolean init) {
		if (timeLineWindow == null) {
			timeLineWindow = new TimeLineWindow(animalInstance, animalInstance
					.getAnimation());
		}

		return timeLineWindow;
	}

	/**
	 * return the current VariableView. If none exists, create a new one.
	 * 
	 * @return the current VariableView, non-null.
	 */
	public VariableView getVariableView() {
		if (variableView == null) {
			variableView = new VariableView(animalInstance, animalInstance
					.getAnimation());
		}

		return variableView;
	}

	/**
	 * determines if the animation overview window is currently visible
	 * 
	 * @return true if the window is Visible, false otherwise
	 */
	public boolean animationOverviewVisible() {
		return animationOverviewVisible;
	}

	/**
	 * determines if the animation window is currently visible
	 * 
	 * @return true if the window is Visible, false otherwise
	 */
	public boolean animationWindowVisible() {
		return animationWindowVisible;
	}

	/**
   * determines if the Animal to PDF window is currently visible
   * 
   * @return true if the window is Visible, false otherwise
   */
  public boolean animalToPDFWindowVisible() {
    return pdfWindowVisible;
  }
	/**
	 * determines if the annotation window is currently visible
	 * 
	 * @return true if the window is Visible, false otherwise
	 */
	public boolean annotationWindowVisible() {
		return annotationWindowVisible;
	}

	/**
	 * determines if the drawing window is currently visible
	 * 
	 * @return true if the window is Visible, false otherwise
	 */
	public boolean drawWindowVisible() {
		return drawWindowVisible;
	}

	/**
	 * determines if the objects window is currently visible
	 * 
	 * @return true if the window is Visible, false otherwise
	 */
	public boolean objectsWindowVisible() {
		return objectsWindowVisible;
	}

	/**
	 * determines if the time line window is currently visible
	 * 
	 * @return true if the window is Visible, false otherwise
	 */
	public boolean timeLineWindowVisible() {
		return timeLineWindowVisible;
	}

	/**
	 * determines if the drawing window is currently visible
	 * 
	 * @return true if the window is Visible, false otherwise
	 */
	public boolean variablesWindowVisible() {
		return variableViewVisible;
	}

	/**
	 * toggles the display of the animation overview window
	 */
	public void showAnimationOverview() {
		getAnimationOverview(true).setVisible(true);
		animationOverview.setStep(1, false);
	}

	/**
	 * toggles the display of the main animation window
	 */
	public void showAnimationWindow() {
		getAnimationWindow(true).setVisible(true);
	}

	/**
	 * toggles the display of the animation overview window
	 */
	public void showAnnotationWindow() {
		getAnnotationEditor(true).setVisible(true);
	}
	/**
   * toggles the display of the Animal to PDF window
   */
  public void showPDFWindow() {
    getAnimalToPDFWindow(true).setVisible(true);
  }

	/**
	 * toggles the display of the animation overview window
	 */
	public void showDrawWindow() {
	  if (!Animal.PREVENT_EDITING)
	    getDrawWindow(true).setVisible(true);
	  else
	    JOptionPane.showMessageDialog(Animal.get(), 
	          "Editing is not possible within CrypTool, please start Animal\n" 
	          +"with java -jar Animal-x.y.z.jar from the command line,\nor by double clicking on the file");
	}

	/**
	 * toggles the display of the animation overview window
	 */
	public void showObjectsWindow() {
		getObjectsWindow(true).setVisible(true);
	}

	/**
	 * toggles the display of the animation overview window
	 */
	public void showTimeLineWindow() {
		getTimeLineWindow(true).setVisible(true);
	}

	/**
	 * toggles the display of the animation overview window
	 */
	public void showVariableView() {
		getVariableView().setVisible(true);
	}

  /**
   * @param zoomIn
   *          if true zooms in, if false zooms out
   */
  public void zoom(boolean zoomIn) {
    if (animationOverview != null) {
      animationOverview.zoom(zoomIn);
    }

    if (timeLineWindow != null) {
      timeLineWindow.zoom(zoomIn);
    }

    if (variableView != null) {
      variableView.zoom(zoomIn);
    }
    if (objectsWindow != null) {
      objectsWindow.zoom(zoomIn);
    }
    
    if (pdfWindow != null) {
      pdfWindow.zoom(zoomIn);
    }

  }
  
  public void hideMenu() {
    pdfWindow.hideMenu();

  }

  public void hideMenu(boolean hide) {
    pdfWindow.hideMenu(hide);

  }
}
