package generators.graphics.marching_squares;

import graphics.AnimalImageDummy;
import helpers.AnimalReader;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;

public class MarchingSquaresImageHelper {

  private static int maxWidth = 16;
  private static int maxHeight = 11;

  public static BufferedImage getImageFromUrl(String url) throws MalformedURLException {
    BufferedImage image = null;
    URL imageUrl = null;
    try {
      imageUrl = new URL(url);
    } catch (MalformedURLException e) {
      imageUrl = new File(url).toURI().toURL();
    }

    try (InputStream in = imageUrl.openStream()) {
      image = ImageIO.read(in);
      Image scaledImage = getScaledImage(image);

      // Create a buffered image with transparency
      BufferedImage bimage = new BufferedImage(scaledImage.getWidth(null),
          scaledImage.getHeight(null),
          BufferedImage.TYPE_INT_ARGB);

      // Draw the image on to the buffered image
      Graphics2D bGr = bimage.createGraphics();
      bGr.drawImage(scaledImage, 0, 0, null);
      bGr.dispose();
      return bimage;
    } catch (Exception e) {
      System.out.println("Error opening image: " + e.toString());
      System.out.println("Using animal logo instead.");
      try {
        image = ImageIO.read(AnimalReader
            .getInputStreamOnLayer(AnimalImageDummy.class, "Animal.jpg"));
        Image scaledImage = getScaledImage(image);

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(scaledImage.getWidth(null),
            scaledImage.getHeight(null),
            BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(scaledImage, 0, 0, null);
        bGr.dispose();
        return bimage;
      } catch (Exception g) {
        return null;
      }
    }
  }

  public static int[][] getPixelValuesFromUrl(String url) throws MalformedURLException {
    BufferedImage image = getImageFromUrl(url);
    int width = image.getWidth();
    int height = image.getHeight();

    int[][] pixels = new int[height][width];
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        Color color = new Color(image.getRGB(col, row), true);
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        pixels[row][col] = (red + green + blue) / 3;
      }
    }
    return pixels;
  }

  private static Image getScaledImage(Image original) {
    int width = original.getWidth(null);
    int height = original.getHeight(null);

    if (width <= maxWidth && height <= maxHeight) {
      return original;
    }
    // else
    float widthFactor = ((float) width) / maxWidth;
    float heightFactor = ((float) height) / maxHeight;

    if (widthFactor >= heightFactor) {
      width = maxWidth;
      height /= widthFactor;
    }
    else {
      // widthFactor < heightFactor
      height = maxHeight;
      width /= heightFactor;
    }

    return original.getScaledInstance(width, height, Image.SCALE_DEFAULT);
  }

  public static void main(String[] args) throws MalformedURLException {
    String url = "C:\\Users\\pnoth\\Downloads\\titanic.jpg";
    MarchingSquaresImageHelper.getPixelValuesFromUrl(url);
    MarchingSquaresImageHelper.getImageFromUrl(url);
  }
}
