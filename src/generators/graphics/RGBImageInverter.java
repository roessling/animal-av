package generators.graphics;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Square;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
//import animal.main.Animal;

/*
 * Inverter.java
 * Niklas Grimm, Nicolai Minter, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import graphics.AnimalImageDummy;
import helpers.AnimalReader;

public class RGBImageInverter implements ValidatingGenerator {
	private Language lang;
	private int maxHeight;
	private int maxWidth;
	private int squareWidth;
	private String inputPicture;
	private SourceCodeProperties scProps;
	private PolylineProperties pp;

	private enum Reader {
		RURL, RFILE, SURL, WRONG
	};

	private Reader fileCorrect = Reader.WRONG;

	/*
	 * (non-Javadoc)
	 * @see generators.framework.ValidatingGenerator#validateInput(generators.framework.properties.AnimationPropertiesContainer, java.util.Hashtable)
	 */
	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		if((Integer) primitives.get("maxHeight") < 5) {
			JOptionPane.showMessageDialog(null, "maxHeight darf nicht kleiner als 5 sein", "Invalid input", JOptionPane.OK_OPTION);
			return false;
		}
		else if((Integer) primitives.get("maxWidth") < 5) {
			JOptionPane.showMessageDialog(null, "maxWidth darf nicht kleiner als 5 sein", "Invalid input", JOptionPane.OK_OPTION);
			return false;
		}	
		else if((Integer) primitives.get("maxHeight") * (Integer) primitives.get("maxWidth") > 2500) {
			JOptionPane.showMessageDialog(null, "Die Maximale Anzahl der Pixel (2500) wurde überschritten. Ihre Anzahl war: " + (Integer) primitives.get("maxHeight") * (Integer) primitives.get("maxWidth") , "Invalid input", JOptionPane.OK_OPTION);
			return false;
		}
		else if((Integer) primitives.get("squareWidth") < 10 ){
			JOptionPane.showMessageDialog(null, "squareWidth darf nicht kleiner als 10 sein", "Invalid input", JOptionPane.OK_OPTION);
			return false;
		}
		else if((Integer) primitives.get("squareWidth") > 60){
			JOptionPane.showMessageDialog(null, "squareWidth darf nicht größer als 60 sein", "Invalid input", JOptionPane.OK_OPTION);
			return false;
		}
		
		return true;
	}
	
	public void init() {
		lang = new AnimalScript("Invert RGB", "Niklas Grimm, Nicolai Minter", 800, 600);
		// This initializes the step mode. Each pair of subsequent steps has to
		// be divdided by a call of lang.nextStep();
		lang.setStepMode(true);
	}

	public RGBImageInverter() {
		init();
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		maxHeight = (Integer) primitives.get("maxHeight");
		squareWidth = (Integer) primitives.get("squareWidth");
		inputPicture = (String) primitives.get("inputPicture");
		maxWidth = (Integer) primitives.get("maxWidth");
		scProps = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
		pp = (PolylineProperties)props.getPropertiesByName("polyline");
		invert();
		return lang.toString();
	}

	public String getName() {
		return "Invert RGB";
	}

	public String getAlgorithmName() {
		return "Invertierung RGB Bild";
	}

	public String getAnimationAuthor() {
		return "Niklas Grimm, Nicolai Minter";
	}

	public String getDescription() {
		return "Der RGB-Inverter geht das Eingabebild Pixel für Pixel durch." + "\n"
				+ "Er rechnet den maximalen Farbwert (255,255,255) minus die R-, G- und B- Werte " + "\n"
				+ "des aktuellen Pixels. R'=255-R  G'=255-G  B'=255-B" + "\n" + "\n"
				+ "Zur verdeutlichung können Sie Bilder in Form von URL oder Dateipfad angeben. " + "\n"
				+ "Die Bilder werden zudem Standardmäßig auf 20x20 runterskaliert oder auf die vom Nutzer eingestellte Größe. "
				+ "Jedoch darf die Gesamtzahl an Pixeln nicht größer 2500 sein."
				+ " Aus performance Gründen raten wir ihnen bis zu maximal 1000 Pixeln.";
	}

	public String getCodeExample() {
		return "public BufferedImage invertAlgo(BufferedImage inputFile) {" + "\n"
				+ "	for (int x = 0; x < inputFile.getWidth(); x++) {" + "\n"
				+ "		for (int y = 0; y < inputFile.getHeight(); y++) {" + "\n"
				+ "			int rgba = inputFile.getRGB(x, y);" + "\n" + "			Color col = new Color(rgba, true);"
				+ "\n" + "			col = new Color(255 - col.getRed(), 255 - col.getGreen(), 255 - col.getBlue());"
				+ "\n" + "			inputFile.setRGB(x, y, col.getRGB());" + "\n" + "		}" + "\n" + "	}" + "\n"
				+ " 	return inputFile;" + "\n" + "}";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	public void invert() {
		BufferedImage inputFile = setPicture();

		if (fileCorrect.equals(Reader.WRONG))
			return;

		RectProperties rp = new RectProperties();
		TextProperties tp = new TextProperties();

		rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 15));
		tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);

		int xin = 30;
		int yin = 0;

		Rect invertRGBR = lang.newRect(new Coordinates(40, 40), new Coordinates(315, 80), "invertRGBR", null, rp);
		invertRGBR.changeColor("fillColor", Color.YELLOW, null, null);

		Text invertRGB = lang.newText(new Coordinates(50, 50), "Invertierung RGB Bild", "invertRGB", null, tp);
		invertRGB.setFont(new Font("Monospaced", Font.PLAIN, 20), null, null);

		Text des1 = lang.newText(new Coordinates(50, 100),
				"Der RGB-Inverter geht das Eingabebild Pixel fÃ¼r Pixel durch.", "des1", null, tp);
		Text des2 = lang.newText(new Coordinates(50, 115),
				"Er rechnet den maximalen Farbwert (255,255,255) minus die R-, G- und B- Werte", "des2", null, tp);
		Text des3 = lang.newText(new Coordinates(50, 130), "des aktuellen Pixels. R'=255-R  G'=255-G  B'=255-B", "des3",
				null, tp);
		
		if(fileCorrect.equals(Reader.WRONG))
			lang.nextStep();

		Text urlNotCorrect = lang.newText(new Coordinates(50, 145),
				"Leider konnten wir ihre URL/File nicht verarbeiten, wir verwenden deshalb ein Standard Bild.",
				"notCorrectUrl", null, tp);

		if (fileCorrect.equals(Reader.RFILE) || fileCorrect.equals(Reader.RURL))
			urlNotCorrect.hide();

		urlNotCorrect.changeColor("Color", Color.red, null, null);

		lang.nextStep("Intro");

		Variables vars = lang.newVariables();
		vars.declare("string", "oldRGB", "(0,0,0)", "Pixels aktueller RGB Wert");
	    vars.declare("string", "newRGB", "(0,0,0)", "Pixels aktueller RGB Wert");
	    vars.declare("int", "x", "0", "Aktueller X Wert");
	    vars.declare("int", "y", "0","Aktueller Y Wert");

		des1.hide();
		des3.hide();
		des2.hide();
		urlNotCorrect.hide();

		SourceCode sc = lang.newSourceCode(new Coordinates(100, 100), "sourceCode", null, scProps);

		// Codeline
		sc.addCodeLine("invert(BufferedImage inputFile){", null, 0, null); // 0
		sc.addCodeLine("for (int x = 0; x < inputFile.getWidth(); x++) {", null, 1, null); // 1
		sc.addCodeLine("for (int y = 0; y < inputFile.getHeight(); y++) {", null, 2, null); // 2
		sc.addCodeLine("int rgba = inputFile.getRGB(x, y);", null, 3, null); // 3
		sc.addCodeLine("Color col = new Color(rgba, true);", null, 3, null); // 4
		sc.addCodeLine("col = new Color(255 - col.getRed(), 255 - col.getGreen(), 255 - col.getBlue());", null, 3,
				null); // 5
		sc.addCodeLine("inputFile.setRGB(x, y, col.getRGB());", null, 3, null); // 6
		sc.addCodeLine("}", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("}", null, 0, null);

		SquareProperties sp = new SquareProperties();
		sp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

		lang.nextStep();

		Map<Integer, Square> notInv = new HashMap<Integer, Square>();
		Map<Integer, Square> Inv = new HashMap<Integer, Square>();

		int tmpi = yin;
		for (int x = 0; x < inputFile.getHeight(); x++) {
			int tmp = xin;
			for (int y = 0; y < inputFile.getWidth(); y++) {

				// get the RGB Value
				int rgba = inputFile.getRGB(y, x);
				Color col = new Color(rgba, true);

				// Create an Square
				Square s = lang.newSquare(new Offset(xin, yin, sc, "NE"), squareWidth, "ni", null, sp);

				// change
				s.changeColor("fillColor", new Color(col.getRed(), col.getGreen(), col.getBlue()), null, null);

				// add Square to Hashmap
				notInv.put(y * inputFile.getHeight() + x, s);

				//
				xin += squareWidth;

			}
			yin += squareWidth;
			xin = tmp;
		}
		Text curPic;
		if(squareWidth < 20)
			curPic = lang.newText(new Offset(0, - 20,notInv.get(0), "NW"),
				"", "curPic", null, tp);
		else curPic = lang.newText(new Offset(0, - squareWidth,notInv.get(0), "NW"),
				"", "curPic", null, tp);
		
		Polyline currentPL = lang.newPolyline(
				new Node[] { new Coordinates(0, 0), new Coordinates(0 + squareWidth - 1, 0),
						new Coordinates(0 + squareWidth - 1, 0 + squareWidth - 1),
						new Coordinates(0, 0 + squareWidth - 1), new Coordinates(0, 0),
						new Coordinates(0 + squareWidth - 1, 0 + squareWidth - 1),
						new Coordinates(0 + squareWidth - 1, 0), new Coordinates(0, 0 + squareWidth - 1) },
				"currentPL", null, pp);
		currentPL.hide();

		lang.nextStep("Berechnung");

		yin = tmpi;

		Square black = lang.newSquare(new Offset(0, squareWidth, sc, "SW"), squareWidth, "black", null, sp);
		black.changeColor("fillColor", Color.BLACK, null, null);
		black.hide();

		for (int x = 0; x < inputFile.getHeight(); x++) {

			sc.highlight(1);
			lang.nextStep("Reihe"+x);
			vars.set("x", ""+ x);

			
			for (int y = 0; y < inputFile.getWidth(); y++) {
				vars.set("y", ""+ y);
				curPic.setText("Aktuell betrachteter Pixel: ("+ x + ", "+ y + ")", null, null);
				// gets the RGB value
				int rgba = inputFile.getRGB(y, x);
				Color col = new Color(rgba, true);
				vars.set("newRGB", "("+ col.getRed() + "," +  col.getGreen() + "," + col.getBlue() + ")");
				col = new Color(255 - col.getRed(), 255 - col.getGreen(), 255 - col.getBlue());

				
				// sc highlight
				sc.toggleHighlight(1, 2);
				sc.highlight(3);
				sc.highlight(4);

				// gets the current square
				Square s = notInv.get(y * inputFile.getHeight() + x);
				// saves the old pos
				Node oldPos = s.getUpperLeft();

				currentPL.show();
				currentPL.moveTo("NW", null, oldPos, null, null);

				// Rechnung
				String rV, gV, bV;

				black.show();

				rV = "Red: 255 - " + col.getRed() + " = " + (255 - col.getRed());
				gV = "Green: 255 - " + col.getGreen() + " = " + (255 - col.getGreen());
				bV = "Blue: 255 - " + col.getBlue() + " = " + (255 - col.getBlue());

				Text minus = lang.newText(new Offset(squareWidth, 0, black, "NE"), "-", "bla", null, tp);
				s.moveTo("E", null, new Offset(squareWidth, 0, minus, "NE"), null, null);
				Text eq = lang.newText(new Offset(squareWidth * 3, 0, minus, "NE"), "=", "bla", null, tp);

				Text t = lang.newText(new Offset(0, squareWidth, black, "SW"), rV, "bla", null, tp);
				Text t1 = lang.newText(new Offset(0, squareWidth, t, "SW"), gV, "bla", null, tp);
				Text t2 = lang.newText(new Offset(0, squareWidth, t1, "SW"), bV, "bla", null, tp);
				
				vars.set("oldRGB", "("+ col.getRed() + "," +  col.getGreen() + "," + col.getBlue() + ")");

				lang.nextStep();

				sc.unhighlight(2);
				sc.unhighlight(3);
				sc.toggleHighlight(4, 5);

				Square newS = lang.newSquare(new Offset(squareWidth, 0, eq, "NE"), squareWidth, "black", null, sp);

				newS.changeColor("fillColor", col, null, null);

				lang.nextStep();

				sc.toggleHighlight(5, 6);

				// moves new Sqaure to old pos
				newS.moveTo("W", null, oldPos, null, null);
				Inv.put(y * inputFile.getHeight() + x, newS);

				s.hide();

				t.hide();
				t1.hide();
				t2.hide();
				black.hide();
				minus.hide();
				eq.hide();

				lang.nextStep();
				sc.unhighlight(6);
			}
		}
		lang.nextStep();
		sc.hide();
		curPic.hide();
		
		xin = 30;
		yin = 0;
		Map<Integer, Square> startPic = new HashMap<Integer, Square>();
		for (int x = 0; x < inputFile.getHeight(); x++) {
			int tmp = xin;
			for (int y = 0; y < inputFile.getWidth(); y++) {

				// get the RGB Value
				int rgba = inputFile.getRGB(y, x);
				Color col = new Color(rgba, true);

				// Create an Square
				Square s = lang.newSquare(new Offset(xin, yin, sc, "NE"), squareWidth, "ni", null, sp);

				// change
				s.changeColor("fillColor", new Color(col.getRed(), col.getGreen(), col.getBlue()), null, null);
				
				startPic.put(y * inputFile.getHeight() + x, s);
				
				//
				xin += squareWidth;

			}
			yin += squareWidth;
			xin = tmp;
		}
		Text out1 = lang.newText(new Coordinates(100,100), "Dies war das Startbild", "out", null,tp);
		lang.nextStep("Outro");
		
		
		for (int x = 0; x < inputFile.getHeight(); x++) {
			for (int y = 0; y < inputFile.getWidth(); y++) {
				Square s = startPic.get(y * inputFile.getHeight() + x);
				s.hide();
			}
		}
		out1.setText("Dies ist das vollstÃ¤ndig invertierte Ergebnisbild," , null, null);
		Text out11 = lang.newText(new Offset(0, squareWidth, out1, "SW"), "mit folgenden Einstellungen:" , "bla", null, tp);
		Text out2 = lang.newText(new Offset(0, squareWidth*2, out11, "SW"), "maxHeight: " + maxHeight, "bla", null, tp);
		Text out3 = lang.newText(new Offset(0, squareWidth, out2, "SW"), "maxWidth: " + maxWidth, "bla", null, tp);
		Text out4 = lang.newText(new Offset(0, squareWidth, out3, "SW"), "squareWidth: " + squareWidth, "bla", null, tp);
		@SuppressWarnings("unused")
    Text out5;
		if(fileCorrect.equals(Reader.RFILE))
			out5 = lang.newText(new Offset(0, squareWidth, out4, "SW"), "Dateipfad: " + inputPicture, "bla", null, tp);
		else if(fileCorrect.equals(Reader.RURL))
			out5 = lang.newText(new Offset(0, squareWidth, out4, "SW"), "URL: " + inputPicture, "bla", null, tp);
		else 
			out5 = lang.newText(new Offset(0, squareWidth, out4, "SW"), "Bild: Ihr angegebener Dateipfad/URL war nicht korrekt, wir haben den Animal Hund verwendet" + inputPicture, "bla", null, tp);
		
		lang.nextStep();
		
	}

	/*
	 * returns a buffered image with the format maxWidth x maxHeight
	 */
	public BufferedImage setPicture() {
		// users url
		String url = inputPicture;

		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(url));
			Image scaledImage = image.getScaledInstance(maxWidth, maxHeight, Image.SCALE_DEFAULT);

			// Create a buffered image with transparency
			BufferedImage bimage = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null),
					BufferedImage.TYPE_INT_ARGB);

			// Draw the image on to the buffered image
			Graphics2D bGr = bimage.createGraphics();
			bGr.drawImage(scaledImage, 0, 0, null);
			bGr.dispose();

			fileCorrect = Reader.RFILE;
			return bimage;
		} catch (Exception e) {
			try {
				URL imageUrl = new URL(url);
				InputStream in = imageUrl.openStream();
				image = ImageIO.read(in);
				in.close();
				Image scaledImage = image.getScaledInstance(maxWidth, maxHeight, Image.SCALE_DEFAULT);

				// Create a buffered image with transparency
				BufferedImage bimage = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null),
						BufferedImage.TYPE_INT_ARGB);

				// Draw the image on to the buffered image
				Graphics2D bGr = bimage.createGraphics();
				bGr.drawImage(scaledImage, 0, 0, null);
				bGr.dispose();

				fileCorrect = Reader.RURL;
				return bimage;
			} catch (Exception f) {
				try {
					image = ImageIO.read(AnimalReader.getInputStreamOnLayer(AnimalImageDummy.class, "Animal.jpg"));
					Image scaledImage = image.getScaledInstance(maxWidth, maxHeight, Image.SCALE_DEFAULT);

					// Create a buffered image with transparency
					BufferedImage bimage = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null),
							BufferedImage.TYPE_INT_ARGB);

					// Draw the image on to the buffered image
					Graphics2D bGr = bimage.createGraphics();
					bGr.drawImage(scaledImage, 0, 0, null);
					bGr.dispose();
					fileCorrect = Reader.SURL;
					return bimage;
				} catch (Exception g) {
					fileCorrect = Reader.WRONG;
					return null;
				}
			}
		}
	}

//	public static void main(String[] args) {
//		Generator generator = new RGBImageInverter();
//		Animal.startGeneratorWindow(generator);
//	}

	

}