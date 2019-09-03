package animalToPDF;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class PDFWriter {

	private String directory = ".Animation.pdf";



	public void writePDF(String directory, LinkedList<Image> images, boolean rotate, int pageWidth, int pageHeight)
			throws IOException {

		this.directory = directory;
		try (PDDocument doc = new PDDocument()) {

			PDPage myPage = new PDPage(new PDRectangle((float) pageWidth, (float) pageHeight));
			

			// Image image = images.get(0);
			
			for (Image img : images) {
				myPage =new PDPage(new PDRectangle((float) pageWidth, (float) pageHeight));
				doc.addPage(myPage);
				addPage(doc, myPage, img, rotate, pageWidth, pageHeight);
			}
			

			doc.save(directory);
			doc.close();

			//doc.save(directory);
		} catch (IOException e) {
			System.out.println(e.toString());
		}

	}

	public void addPage(PDDocument doc, PDPage myPage, Image image, boolean rotate, float pageWidth, float pageHeight) {

		try {
			BufferedImage bufferedImage = (BufferedImage) image;
			if (rotate)
				bufferedImage = rotateCCw(bufferedImage);
			PDImageXObject pdImageXObject = LosslessFactory.createFromImage(doc, bufferedImage);
			PDPageContentStream contentStream = new PDPageContentStream(doc, myPage);
			float height = bufferedImage.getHeight();
			height = (float) (height / 1.5);

			float percent = pageHeight / 730;
			height = height * percent;
			float width = bufferedImage.getWidth();
			width = (float) (width / 1.5);
			float percent2 = pageWidth / 900;
			width = width * percent2;
			System.out.println("pageHight: " + pageHeight);
			float startHeight = pageHeight/2;// - height;//800; //* percent - height;
			
			if (rotate)
				startHeight = 10;

			contentStream.drawImage(pdImageXObject, 10, startHeight, width, height);
			contentStream.close();

		} catch (Exception io) {
			System.out.println(" -- fail --" + io);
		}

	}

	public static BufferedImage rotateCw(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		BufferedImage newImage = new BufferedImage(height, width, img.getType());

		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				newImage.setRGB(height - 1 - j, i, img.getRGB(i, j));

		return newImage;
	}

	public static BufferedImage rotateCCw(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		BufferedImage newImage = new BufferedImage(height, width, img.getType());

		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				newImage.setRGB(j, width - 1 - i, img.getRGB(i, j));

		return newImage;
	}
}
