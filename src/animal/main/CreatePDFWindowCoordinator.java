package animal.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class CreatePDFWindowCoordinator {

	private AnimalToPDFWindow controller;
	private LinkedList<Image> images;
	private CreatePDFWindow window;
	private int pageNumber = 0;

	public CreatePDFWindowCoordinator(AnimalToPDFWindow controller, LinkedList<Image> images, int zoomCounter) {


		this.controller = controller;
		this.images = images;
		window = new CreatePDFWindow(this);
		window.setSize(1080, 780); 
		window.setVisible(true);
		
		if(zoomCounter >0) {
			for(int i = 0; i <zoomCounter; i++)
				window.zoom(true);
			
		}else {
			if(zoomCounter <0)
				for (int i = 0; i > zoomCounter; i--)
					window.zoom(false);
		}

	}

	public void createPDF(String directory, boolean rotate, int width, int height) {
		controller.printPDF(directory, rotate, width, height);

	}

	public void nextImage() {
		if (pageNumber < images.size() - 1) {

			pageNumber++;
			System.out.println("next: " + pageNumber);
			window.showImage(images.get(pageNumber));
			window.setPageNumber(pageNumber + 1);
		}

	}

	public void previousPage() {
		if (pageNumber > 0) {
			pageNumber--;
			window.showImage(images.get(pageNumber));
			window.setPageNumber(pageNumber + 1);
		}

	}

	public void removePage() {


		if (pageNumber + 1 >= images.size()) {

			BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
			Graphics2D ig2 = image.createGraphics();

			ig2.setBackground(Color.WHITE);
			ig2.clearRect(0, 0, 400, 400);

			window.showImage(image);
			images.remove(pageNumber);
			
		} else {
			window.showImage(images.get(pageNumber + 1));
			images.remove(pageNumber);
		}


	}

	public void hide() {
		window.setVisible(false);

	}

	public Image firstImage() {

		return images.getFirst();
	}

	/**
	 * 
	 * @param zoomIn
	 */
	public void zoom(boolean zoomIn) {

		if (window != null)
			window.zoom(zoomIn);

	}

}
