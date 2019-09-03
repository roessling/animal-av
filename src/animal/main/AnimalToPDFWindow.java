package animal.main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import animal.gui.WindowCoordinator;
import animal.misc.XProperties;
import animalToPDF.PDFWriter;

/**
 * The controls and infos for the Animal to PDF functionality.
 * 
 * @author Marian Hieke
 */

public class AnimalToPDFWindow implements ActionListener {

	private TextInputWindow inputWindow;
	private boolean inputInstanciated = false;
	private boolean menuHooked = true;
	private LinkedList<Image> images = new LinkedList<Image>();
	private CreatePDFWindowCoordinator createWindow;
	int zoomCounter = 0;
	private PDFWriter writer;

	/**
	 * adds the current state of the aniamtion as a new page to the pdf
	 */
	public void addPage() {
		addPage(this.getStep() - 1);

	}

	/**
	 * adds the current state of the aniamtion as a new page to the pdf
	 */
	public void addPageAt(int x) {

		AnimationCanvas animCanvas = this.getCanvas();
		if (x <= 0)
			return;

		Image img = this.getPicture(this.getStep());
		this.images.add(x, img);

	}

	/**
	 * adds the current state of the aniamtion as a new page to the pdf
	 */
	public void addPage(int x) {

		AnimationCanvas animCanvas = this.getCanvas();
		if (x < 0)
			return;

		Image img = this.getPicture(x + 1);
		this.images.add(img);


	}

	/**
	 * removes the last added page from the PDF
	 */
	public void removePage() {
		this.images.removeLast();

	}



	/**
	 * removes page number x from the PDF
	 * 
	 * @param x
	 *            the number of the page which should get romeved
	 */
	public void removePage(int x) {
		if (images.size() > x)
		this.images.remove(x);

	}

	/**
	 * adds the last x steps of the animation to the PDF
	 * 
	 * @param x
	 *            the last x steps get added to the PDF
	 */
	public void addLastSteps(int x) {
		if (x <= 0)
			return;

		int start = this.getStep() + 1 - x;
		if(start <0) 
			start = 0;
		
		for (int i = start; i <= this.getStep(); i++) {
			this.images.add(this.getPicture(i));
		}

	}

	/**
	 * removes the last x steps of the animation to the PDF
	 * 
	 * @param x
	 *            the last x steps get removed to the PDF
	 */
	public void removeLastSteps(int x) {
		if (x <= 0)
			return;


		for (int i = 0; i < x; i++) {

			if (images.size() > 0)
				images.removeLast();

		}

	}

	public void removeFromTo(int x, int y) {
		if (x <= 0 || y <= 0 || y < x)
			return;


		for (int i = x - 1; i <= y - 1; i++) {
			System.out.println("remove: " + (x + i));
			this.images.remove(x - 1);
		}

	}

	/**
	 * add the pages from number X to number Y
	 * 
	 * @param x
	 * @param y
	 */

	public void addFromTo(int x, int y) {
		if (x <= 0 || y <= 0 || y < x)
			return;

		for (int i = x; i <= y; i++) {
			System.out.println("add Page: " + i);
			this.addPage(i - 1);
		}

	}

	/**
	 * adds a new text page to the PDF
	 */
	public void addTextPage() {
		this.openTextInput();


	}

	private AnimalToPDFWindowView view;

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
	public AnimalToPDFWindow(Animal animalInstance, XProperties properties) {

		view = new AnimalToPDFWindowView(animalInstance, properties, this);

		inputWindow = new TextInputWindow(this);
		inputWindow.setSize(800, 630);
		inputWindow.setVisible(false);
		inputInstanciated = true;
		writer = new PDFWriter();

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
	public AnimalToPDFWindow(Animal animalInstance, XProperties properties,
				Container aContainer) {
		view = new AnimalToPDFWindowView(animalInstance, properties, aContainer, this);

			inputWindow = new TextInputWindow(this);
			inputWindow.setSize(800, 630);
		inputWindow.setVisible(false);
			inputInstanciated = true;
		writer = new PDFWriter();

		}

	/**
	 * initializes the AnimationWindow by adding the control panel and the
	 * AnimationCanvas.
	 */
	public void init() {
		view.init();
	}

	/*
	 * public JScrollPane getScrollPane() { return view.getScrollPane(); }
	 */

	public boolean isVisible() {// TODO
		// return true;
		// return view.isVisible();
		return view != null;// && view.getUnderContentPane() != null &&
							// view.getUnderContentPane().isVisible();
	}

	public int getViewWidth() {
		return view.getWidth();
	}

	public int getViewHeight() {
		return view.getHeight();
	}

	public void updatePack() {
		view.pack();
	}

	public Image getPicture(int step) {
		WindowCoordinator winCoord = view.getWindowCoordinator();
		AnimationWindow animView = winCoord.getAnimationWindow(false);
		Image img = animView.getImageForStep(step);

		return img;

	}


	
	  public boolean isInitialized() { return view.isInitialized(); }
	  
	  public void setTitle(String title) { view.setTitle(title); }
	  
	  
	  
	private JPanel animationContainer = null;

	 
	/**
	 * handles action events, which are always caused by the animation timer
	 * 
	 * @param actionEvent the event to be handled
	 */
	public void actionPerformed(ActionEvent actionEvent) {

	}



	public Rectangle getViewBounds() {
		return view.getBounds();
	}

	public void getViewProperties(XProperties properties) {
		view.getProperties(properties);
	}



	public void setAnimationWindowLocale(Locale targetLocale) {
		view.changeLocale(targetLocale);
	}

	public AnimalToPDFWindowView getAnimaltoPDFWindowView()
	{
		return view;
	}

	/**
	 * @param zoomIn
	 *            if true zooms in, if false zooms out
	 */
	public void zoom(boolean zoomIn) {
		if (zoomIn) {
			if (zoomCounter < 6)
				zoomCounter++;
		} else {
			if (zoomCounter > -1)
				zoomCounter--;
		}

		if (view != null)
			view.zoom(zoomIn);
		if (inputWindow != null) {
			inputWindow.zoom(zoomIn);
		}

		if (createWindow != null)
			createWindow.zoom(zoomIn);

	}

	// todo

	/**
	 * overwritten to initialize the internal Animation. When the window is not
	 * visible, all methods can be called, but they don't make a change until the
	 * window is made visible. Then perform the commands from here.
	 * 
	 * @param isVisible
	 *            if true, ensure the window is visible
	 */
	public void setVisible(boolean isVisible) {
		// view.setVisible(isVisible);
		/*
		 * if (isVisible) { if (ani == null) { setAnimation(Animation.get());
		 * setStep(ani.getFirstRealStep(), true); }
		 * setAnimationPlayerHooked(animationPlayerHooked);
		 */ }

	/**
	 * 
	 * @return return the displayed AnimationCancas
	 */

	public AnimationCanvas getCanvas() {
		WindowCoordinator winCoord = view.getWindowCoordinator();
		AnimationWindow animView = winCoord.getAnimationWindow(false);
		AnimationCanvas animCanvas = animView.getAnimationCanvas();
		return animCanvas;
		
	}

	public int getStep() {
		WindowCoordinator winCoord = view.getWindowCoordinator();
		AnimationWindow animView = winCoord.getAnimationWindow(false);
		return animView.getStep();
	}

	public void openTextInput() {

		if (!inputInstanciated) {
			inputWindow = new TextInputWindow(this);
			//inputWindow.setSize(960, 730);
			inputWindow.setBounds(10, 10, 880, 640);
			inputWindow.setVisible(true);
			inputInstanciated = true;
		} else {
			if (inputWindow != null)
				inputWindow.setVisible(true);
			 inputWindow.setBounds(10, 10, 880, 640);
		}


	}

	public void setAnimationPlayerHooked(boolean hook) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JPanel mainWindowContentPanel = (JPanel) AnimalMainWindow.getWindowCoordinator().getAnimalMainWindow()
						.getContentPane();
				if (hook) {
					view.getUHookPlayerButton().setText("Unhook PDF Menu");
					AnimalMainWindow.getWindowCoordinator().getAnimalMainWindow().hookPDFMenu();
					AnimalMainWindow.getWindowCoordinator().getAnimalMainWindow()
							.setPreferredSize(new Dimension(1200, 700));
					AnimalMainWindow.getWindowCoordinator().getAnimalMainWindow()
							.setMinimumSize(new Dimension(1200, 700));
					AnimalMainWindow.getWindowCoordinator().getAnimalMainWindow().setSize(new Dimension(1200, 700));
					view.setVisible(false);
				} else {
					AnimalMainWindow.getWindowCoordinator().getAnimalMainWindow()
							.setPreferredSize(new Dimension(800, 700));
					AnimalMainWindow.getWindowCoordinator().getAnimalMainWindow()
							.setMinimumSize(new Dimension(800, 700));
					AnimalMainWindow.getWindowCoordinator().getAnimalMainWindow()
							.setSize(new Dimension(800, 700));
					view.getUHookPlayerButton().setText("Hook PDF Menu");
					mainWindowContentPanel
							.remove(AnimalMainWindow.getWindowCoordinator().getAnimalMainWindow().getPDFPanel());
					JPanel pdfPanel = AnimalMainWindow.getWindowCoordinator().getAnimalMainWindow().getPDFPanel();

					Dimension dimPanel = new Dimension(315, 700);
					pdfPanel.setSize(dimPanel);
					pdfPanel.setPreferredSize(dimPanel);
					pdfPanel.setMaximumSize(dimPanel);
					pdfPanel.setMinimumSize(dimPanel);
					view.addPanelFullScreenToWorkContainer(pdfPanel);
					view.setVisible(true);
					Dimension viewDim = new Dimension(340, 700);
					view.setSize(viewDim);
					view.setPreferredSize(viewDim);
					view.setMaximumSize(viewDim);
					view.setMinimumSize(viewDim);

				}
				menuHooked = hook;
				mainWindowContentPanel.updateUI();
				if (animationContainer != null)
					animationContainer.updateUI();
			}
		});
	}

	public void setAnimationPlayerHookedHide(boolean hook) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JPanel mainWindowContentPanel = (JPanel) AnimalMainWindow.getWindowCoordinator().getAnimalMainWindow()
						.getContentPane();
				if (hook) {
					view.getUHookPlayerButton().setText("Unhook PDF Menu");
					AnimalMainWindow.getWindowCoordinator().getAnimalMainWindow().hookPDFMenu();
					AnimalMainWindow.getWindowCoordinator().getAnimalMainWindow()
							.setPreferredSize(new Dimension(1200, 700));
					AnimalMainWindow.getWindowCoordinator().getAnimalMainWindow()
							.setMinimumSize(new Dimension(1200, 700));
					AnimalMainWindow.getWindowCoordinator().getAnimalMainWindow().setSize(new Dimension(1200, 700));
					view.setVisible(false);
				} else {
					AnimalMainWindow.getWindowCoordinator().getAnimalMainWindow()
							.setPreferredSize(new Dimension(800, 700));
					AnimalMainWindow.getWindowCoordinator().getAnimalMainWindow()
							.setMinimumSize(new Dimension(800, 700));
					AnimalMainWindow.getWindowCoordinator().getAnimalMainWindow().setSize(new Dimension(800, 700));
					view.getUHookPlayerButton().setText("Hook PDF Menu");
					mainWindowContentPanel
							.remove(AnimalMainWindow.getWindowCoordinator().getAnimalMainWindow().getPDFPanel());
					JPanel pdfPanel = AnimalMainWindow.getWindowCoordinator().getAnimalMainWindow().getPDFPanel();

					Dimension dimPanel = new Dimension(315, 700);
					pdfPanel.setSize(dimPanel);
					pdfPanel.setPreferredSize(dimPanel);
					pdfPanel.setMaximumSize(dimPanel);
					pdfPanel.setMinimumSize(dimPanel);
					view.addPanelFullScreenToWorkContainer(pdfPanel);
					// view.setVisible(true);
					Dimension viewDim = new Dimension(340, 700);
					view.setSize(viewDim);
					view.setPreferredSize(viewDim);
					view.setMaximumSize(viewDim);
					view.setMinimumSize(viewDim);

				}
				menuHooked = hook;
				mainWindowContentPanel.updateUI();
				if (animationContainer != null)
					animationContainer.updateUI();
			}
		});
	}



	public boolean isMenuHooked() {
		return menuHooked;
	}

	public void addTextAsPage(String text) {

		Image img = inputWindow.getImage();
		images.add(img);


	}

	public void printPDF(String directory, boolean rotate, int width, int height) {



		String[] parts = directory.split(".");
		int length = parts.length - 1;
		if (length < 0)
			length = 0;

		if (parts.length > 0) {
		if (!parts[length].equals("pdf"))
			directory = directory + ".pdf";
		} else {
			directory = directory + ".pdf";
		}

		if (writer != null)
			try {
				writer.writePDF(directory, images, rotate, width, height);
			} catch (IOException e) {
				System.out.println("write failed");
				e.printStackTrace();
			}


	}

	public void createPDF() {
		if (images.size() > 0)
			createWindow = new CreatePDFWindowCoordinator(this, images, zoomCounter);

	}

	public void hideMenu() {
		view.hideMenu();

	}

	public void hideMenu(boolean hide) {
		view.hideMenu(hide);

	}

	



}
