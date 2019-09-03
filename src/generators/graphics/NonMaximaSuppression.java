package generators.graphics;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleSegProperties;
import algoanim.properties.EllipseProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.MsTiming;
import algoanim.util.Node;

public class NonMaximaSuppression implements ValidatingGenerator {

	private static final int MATRIX_CELL_SIZE = 20;

	private class StartPage {
		public StartPage(Language lang) {
			List<Primitive> prims = new ArrayList<>();
//			Font headerFont = new Font("SansSerif", Font.BOLD, 25);
//			Font headlineFont = new Font("SansSerif", Font.BOLD, 13);
//			Font accentFont = new Font("SansSerif", Font.BOLD, 10);
//			TextProperties headerProp = new TextProperties();
//			headerProp.set(AnimationPropertiesKeys.FONT_PROPERTY, headerFont);
//
//			TextProperties accentProp = new TextProperties();
//			accentProp.set(AnimationPropertiesKeys.FONT_PROPERTY, accentFont);
//
//			TextProperties headlineProp = new TextProperties();
//			headlineProp.set(AnimationPropertiesKeys.FONT_PROPERTY, headlineFont);

			SourceCodeProperties sourceProps = new SourceCodeProperties();
			sourceProps.set(AnimationPropertiesKeys.FONT_PROPERTY, contentFont);
			sourceProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, contentText.get(AnimationPropertiesKeys.COLOR_PROPERTY));
			sourceProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, contentText.get(AnimationPropertiesKeys.COLOR_PROPERTY));
			prims.add(lang.newText(new Coordinates(20, 20), "Non Maximum Suppression", "t1", null, titleText));
			SourceCode newSourceCode = lang.newSourceCode(new Coordinates(20, 35), "t2", null, sourceProps);
			newSourceCode.addCodeLine("The non maximum suppression is an edge thinning technique.", null, 0, null);
			newSourceCode.addCodeLine("Its used in computer vision to make edges becomre better ", null, 0, null);
			newSourceCode.addCodeLine("visible by erasing noiseand taking only the strongest contour ", null, 0, null);
			newSourceCode.addCodeLine("of an edge into account.Non maximum suppression is an edge part ", null, 0, null);
			newSourceCode.addCodeLine("of the Harris edge detector which is.one of the best known edge ", null, 0, null);
			newSourceCode.addCodeLine("detectors out there.", null, 0, null);
			prims.add(newSourceCode);

			prims.add(lang.newText(new Coordinates(20, 160), "Prerequisits", "t1", null, headlineText));
			prims.add(lang.newText(new Coordinates(20, 180), "edge detection", "t1", null, contentText));
			newSourceCode = lang.newSourceCode(new Coordinates(140, 170), "t2", null, sourceProps);
			newSourceCode.addCodeLine("In order to understand the non maximum suppression.", null, 0, null);
			newSourceCode.addCodeLine("you should already have a at least a brief understanding", null, 0, null);
			newSourceCode.addCodeLine("of edge detection in computer vision e.g. with Sobel ", null, 0, null);
			newSourceCode.addCodeLine("filter. Any other filter will work as well.", null, 0, null);
			prims.add(newSourceCode);

			prims.add(lang.newText(new Coordinates(20, 250), "image derivative", "t1", null, contentText));
			newSourceCode = lang.newSourceCode(new Coordinates(140, 240), "t2", null, sourceProps);
			newSourceCode.addCodeLine("You should also be familiar with computing derivatives", null, 0, null);
			newSourceCode.addCodeLine("of an image and understand the difference between the", null, 0, null);
			newSourceCode.addCodeLine("x- and y-derivative. This also includes some basic", null, 0, null);
			newSourceCode.addCodeLine("knowledge about the gradient magnitude.", null, 0, null);
			prims.add(newSourceCode);

			prims.add(lang.newText(new Coordinates(20, 340), "Algorithm", "t1", null, headlineText));
			newSourceCode = lang.newSourceCode(new Coordinates(20, 350), "t2", null, sourceProps);
			newSourceCode.addCodeLine("The non maximum suppression receives an image and it's", null, 0, null);
			newSourceCode.addCodeLine("two derivatives in x- and y-direction. It will then compute", null, 0, null);
			newSourceCode.addCodeLine("resulting gradient magnitude and take it as the result image.", null, 0, null);
			newSourceCode.addCodeLine("After this process, each pixel of the result image will be", null, 0, null);
			newSourceCode.addCodeLine("used to determine whether it has to be erased or whether it's", null, 0, null);
			newSourceCode.addCodeLine("gradient magnitude can be used.", null, 0, null);
			newSourceCode.addCodeLine("To do this, NMS will take the gradient magnitude and use it", null, 0, null);
			newSourceCode.addCodeLine("to calculate the direction of the next edge of the observed", null, 0, null);
			newSourceCode.addCodeLine("pixel. From this information the direction to the edge from", null, 0, null);
			newSourceCode.addCodeLine("pixel can be determined. The gradient magnitude of the two", null, 0, null);
			newSourceCode.addCodeLine("neighbour pixels of the observed one will be compared to the", null, 0, null);
			newSourceCode.addCodeLine("magnitude of the observed pixel. If the magintude of the", null, 0, null);
			newSourceCode.addCodeLine("observed one is smaller than one of the other two pixels", null, 0, null);
			newSourceCode.addCodeLine("it will get erased in the result image by overwriting it", null, 0, null);
			newSourceCode.addCodeLine("with zero, else it's gradient magnitude will be kept.", null, 0, null);
			prims.add(newSourceCode);
			lang.nextStep();
			for (Primitive p : prims) {
				p.hide();
			}
		}

	}

	private class EndPage {
		public EndPage(Language lang) {
			for (Primitive p : hideLater) {
				p.hide();
			}
			List<Primitive> prims = new ArrayList<>();
//			Font headerFont = new Font("SansSerif", Font.BOLD, 25);
//			Font headlineFont = new Font("SansSerif", Font.BOLD, 13);
//			Font accentFont = new Font("SansSerif", Font.BOLD, 10);
//			TextProperties headerProp = new TextProperties();
//			headerProp.set(AnimationPropertiesKeys.FONT_PROPERTY, headerFont);
//
//			TextProperties accentProp = new TextProperties();
//			accentProp.set(AnimationPropertiesKeys.FONT_PROPERTY, accentFont);
//
//			TextProperties headlineProp = new TextProperties();
//			headlineProp.set(AnimationPropertiesKeys.FONT_PROPERTY, headlineFont);
			SourceCodeProperties sourceProps = new SourceCodeProperties();
			sourceProps.set(AnimationPropertiesKeys.FONT_PROPERTY, contentFont);
			sourceProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, contentText.get(AnimationPropertiesKeys.COLOR_PROPERTY));
			sourceProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, contentText.get(AnimationPropertiesKeys.COLOR_PROPERTY));
			
			prims.add(lang.newText(new Coordinates(20, 20), "Non Maximum Suppression", "t1", null, titleText));
			SourceCode newSourceCode = lang.newSourceCode(new Coordinates(20, 35), "t2", null, sourceProps);
			newSourceCode.addCodeLine("Additions: " + adds, null, 0, null);
			newSourceCode.addCodeLine("Multiplications: " + muls, null, 0, null);
			newSourceCode.addCodeLine("Reads: " + reads, null, 0, null);
			newSourceCode.addCodeLine("Writes: " + writes, null, 0, null);
			newSourceCode.addCodeLine("", null, 0, null);
			newSourceCode.addCodeLine("Complexity: O(n) per pixel", null, 0, null);
			prims.add(newSourceCode);

			prims.add(lang.newText(new Coordinates(20, 160), "Alternatives", "t1", null, headlineText));
			newSourceCode = lang.newSourceCode(new Coordinates(20, 170), "t2", null, sourceProps);
			newSourceCode.addCodeLine("- Zhang-Suen algorithm", null, 0, null);
			newSourceCode.addCodeLine("- Guo-Hall algorithm", null, 0, null);
			newSourceCode.addCodeLine("Both algorithms are implemented in the OpenCV library, ", null, 0, null);
			newSourceCode.addCodeLine("which is the largest CV library in the world.", null, 0, null);
			prims.add(newSourceCode);

			lang.nextStep();
			for (Primitive p : prims) {
				p.hide();
			}
		}

	}

	private Language lang;
	private int[][] srcImage;
	private SourceCode scConvolute;
	private IntMatrix dxTable;
	private IntMatrix dyTable;
	private IntMatrix resultTable;
	private EllipseProperties circle;
	private PolylineProperties slopeLine;
	private PolylineProperties possibleEdges;
	private MatrixProperties srcImageMatrix;
	private MatrixProperties dstImageMatrix;
	private MatrixProperties xDerivativeMatrix;
	private MatrixProperties yDerivativeMatrix;
	private MatrixProperties gradientMagnitudeMatrix;
	private int adds;
	private int muls;
	private int reads;
	private int writes;
	private List<Primitive> hideLater = new ArrayList<>();
	private TextProperties titleText;
	private TextProperties contentText;
	private TextProperties headlineText;
	private Font titleFont;
	private Font contentFont;
	private Font headlineFont;

	public void init() {
		lang = new AnimalScript("Non-Maxima Suppression", "Robert Hahn", 800, 600);
		lang.setStepMode(true);
	}

	private void newText(Language lang2, Coordinates coordinates, String string, String string2, DisplayOptions object, TextProperties textProp) {
		hideLater.add(lang2.newText(coordinates, string, string2, object, textProp));
	}

	private int getMatrixHeight(IntMatrix matrix) {
		return matrix.getNrRows() * MATRIX_CELL_SIZE;
	}

	private int getMatrixWidth(IntMatrix matrix) {
		return matrix.getNrCols() * MATRIX_CELL_SIZE;
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		circle = (EllipseProperties) props.getPropertiesByName("circle");
		slopeLine = (PolylineProperties) props.getPropertiesByName("slopeLine");
		possibleEdges = (PolylineProperties) props.getPropertiesByName("possibleEdgesLine");
		srcImageMatrix = (MatrixProperties) props.getPropertiesByName("srcImageMatrix");
		dstImageMatrix = (MatrixProperties) props.getPropertiesByName("dstImageMatrix");
		xDerivativeMatrix = (MatrixProperties) props.getPropertiesByName("xDerivativeMatrix");
		yDerivativeMatrix = (MatrixProperties) props.getPropertiesByName("yDerivativeMatrix");
		gradientMagnitudeMatrix = (MatrixProperties) props.getPropertiesByName("gradientMagnitudeMatrix");
		

		titleText = (TextProperties) props.getPropertiesByName("titleText");
		contentText = (TextProperties) props.getPropertiesByName("contentText");
		headlineText = (TextProperties) props.getPropertiesByName("headlineText");
		titleFont = new Font("SansSerif", Font.BOLD, 25);
		contentFont = new Font("SansSerif", Font.PLAIN, 10);
		headlineFont = new Font("SansSerif", Font.BOLD, 13);
		titleText.set(AnimationPropertiesKeys.FONT_PROPERTY, titleFont);
		contentText.set(AnimationPropertiesKeys.FONT_PROPERTY, contentFont);
		headlineText.set(AnimationPropertiesKeys.FONT_PROPERTY, headlineFont);
		

		srcImageMatrix.set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, MATRIX_CELL_SIZE);
		srcImageMatrix.set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, MATRIX_CELL_SIZE);
		dstImageMatrix.set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, MATRIX_CELL_SIZE);
		dstImageMatrix.set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, MATRIX_CELL_SIZE);
		xDerivativeMatrix.set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, MATRIX_CELL_SIZE);
		xDerivativeMatrix.set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, MATRIX_CELL_SIZE);
		yDerivativeMatrix.set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, MATRIX_CELL_SIZE);
		yDerivativeMatrix.set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, MATRIX_CELL_SIZE);
		gradientMagnitudeMatrix.set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, MATRIX_CELL_SIZE);
		gradientMagnitudeMatrix.set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, MATRIX_CELL_SIZE);

		new StartPage(lang);

		int[][] image = (int[][]) primitives.get("srcImage");
		int[][] dX = SobelFilterImpl.convolute(image, SobelFilterImpl.xKernel);
		int[][] dY = SobelFilterImpl.convolute(image, SobelFilterImpl.yKernel);
		int height = dX.length;
		int width = dX[0].length;
		int[][] gradientMagnitude = new int[height][width];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int xVal = dX[y][x];
				int yVal = dY[y][x];
				// Magnitude
				gradientMagnitude[y][x] = (int) (Math.sqrt((xVal * xVal) + (yVal * yVal)));
			}
		}

		
		int textIntent = 10;
		int textMarginBottom = 20;
		int yOffset = 20;
		int xOffset = 600;
		int matrixMarginBottom = (int)(9 * height);
		newText(lang, new Coordinates(20, 20), "Prerequisites", "prerequisites", null, headlineText);
		lang.nextStep("Prerequisites");
		newText(lang, new Coordinates(50, 50), "1.) Take an image", "textChooseImage1", null, contentText);
		newText(lang, new Coordinates((int) (xOffset + textIntent), yOffset), "an image", "textChooseImage2", null, contentText);
		yOffset += textMarginBottom;
		srcTable = lang.newIntMatrix(new Coordinates(xOffset, yOffset), image, "image", null, srcImageMatrix);
		yOffset += getMatrixHeight(srcTable) + matrixMarginBottom;
		lang.nextStep();
		newText(lang, new Coordinates(50, 70), "2.) Compute x- and y-derivatives of the image with a filter (e.g. Sobel)", "textChooseImage2", null, contentText);
		newText(lang, new Coordinates(xOffset + textIntent, yOffset), "x-derivative = Dx", "textChooseImage2", null, contentText);
		yOffset += textMarginBottom;
		dxTable = lang.newIntMatrix(new Coordinates(xOffset, yOffset), dX, "dX", null, xDerivativeMatrix);
		yOffset += getMatrixHeight(srcTable) + matrixMarginBottom;
		newText(lang, new Coordinates(xOffset + textIntent, yOffset), "y-derivative = Dy", "textChooseImage2", null, contentText);
		yOffset += textMarginBottom;
		dyTable = lang.newIntMatrix(new Coordinates(xOffset, yOffset), dY, "dY", null, yDerivativeMatrix);
		yOffset += getMatrixHeight(srcTable) + matrixMarginBottom;
		lang.nextStep();
		newText(lang, new Coordinates(50, 90), "3.) Compute gradient magnitude", "textChooseImage3", null, contentText);
		newText(lang, new Coordinates(50, 110), "    gradient magnitude: sqrt(Dx * Dx + Dy * Dy)", "textChooseImage31", null, contentText);
		int resultImageY = yOffset;
		newText(lang, new Coordinates(xOffset + textIntent, yOffset), "gradient magnitude", "textChooseImage2", null, contentText);
		yOffset += textMarginBottom;
		magnitudeTable = lang.newIntMatrix(new Coordinates(xOffset, yOffset), gradientMagnitude, "gradientMagnitude", null, gradientMagnitudeMatrix);
		yOffset += getMatrixHeight(srcTable) + matrixMarginBottom;
		
		lang.nextStep();
		newText(lang, new Coordinates(20, 140), "Non-Maxima Suppression algorithm non-formal", "2textChooseImage1", null, contentText);
		lang.nextStep("Non-Maxima Suppression");
		newText(lang, new Coordinates(50, 160), "1.) Use gradient magnitude as result image and start operating", "asdasd", null, contentText);
		newText(lang, new Coordinates(50, 180), "    on pixels which are not on one of the four edges", "2textChooseImage1", null, contentText);
		xOffset += getMatrixWidth(magnitudeTable) + 100;
		newText(lang, new Coordinates(xOffset + textIntent, resultImageY), "result image", "textChooseImage2", null, contentText);
		resultImageY+= textMarginBottom;
		resultTable = lang.newIntMatrix(new Coordinates(xOffset, resultImageY), gradientMagnitude, "dstImageMatrix", null, dstImageMatrix);

		lang.nextStep();
		for (int y = 1; y < image.length - 1; y++) {
			for (int x = 1; x < image[y].length - 1; x++) {
				magnitudeTable.highlightCell(y, x, new MsTiming(100 * (((y - 1) * image[y].length) + x)), null);
			}
		}
		lang.nextStep();

		// c =
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				magnitudeTable.unhighlightCell(y, x, null, null);
			}
		}
		for (int y = 1; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {
				List<Primitive> _prims = new ArrayList<>();

				int pointX = 150;
				int pointY = 350;
				int lineWidth = 70;
				float steigung = dY[y][x] / (float) (dX[y][x] == 0 ? 0.00001 : dX[y][x]);
				if (dX[y][x] == 0)
					steigung = 0;
				_prims.add(lang.newText(new Coordinates(80, 200), "1.1.) Select pixel (y: " + y + ", x: " + x + ")", "step1", null, contentText));
				lang.nextStep("\tStart computation for pixel: (y = " + y + ", x = " + x + ")");
				magnitudeTable.highlightCell(y, x, null, null);
				lang.nextStep();
				_prims.add(lang.newText(new Coordinates(80, 220), "1.2.) Determine slope by using derivative-x and -y", "a", null, contentText));
				_prims.add(lang.newText(new Coordinates(80, 240), "      on the same pixel position", "step2", null, contentText));
				lang.nextStep();
				dxTable.highlightCell(y, x, null, null);
				dyTable.highlightCell(y, x, null, null);
				lang.nextStep();
				_prims.add(lang.newText(new Coordinates(80, 260), String.format("      slope = Dy[%d][%d] / Dx[%d][%d] = %d / %d = " + (((int) steigung * 100) / 100)
						+ " (if Dx equals 0 the slope is 0", y, x, y, x, dY[y][x], dX[y][x]), "step3", null, contentText));
				lang.nextStep();

				double angle = Math.toDegrees(Math.atan(dY[y][x] / (float) dX[y][x]));

				int xGrad = dX[y][x];
				int yGrad = dY[y][x];
				int mag = gradientMagnitude[y][x];
				int nMag = gradientMagnitude[y - 1][x];
				int sMag = gradientMagnitude[y + 1][x];
				int wMag = gradientMagnitude[y][x - 1];
				int eMag = gradientMagnitude[y][x + 1];
				int nwMag = gradientMagnitude[y - 1][x - 1];
				int neMag = gradientMagnitude[y - 1][x + 1];
				int swMag = gradientMagnitude[y + 1][x - 1];
				int seMag = gradientMagnitude[y + 1][x + 1];
				adds += 12;
				muls += 1;
				writes += 13;
				reads += 4 + 22 + 11;
				boolean isNegativeAngle = xGrad * yGrad <= 0;
				boolean between0and45 = Math.abs(xGrad) > Math.abs(yGrad);

				EllipseProperties ellipseProps = new EllipseProperties();
				ellipseProps.set("filled", true);
				ellipseProps.set("fillColor", Color.black);
				Coordinates pointPosition = new Coordinates(pointX, pointY);

				PolylineProperties lineProps = new PolylineProperties();
				lineProps.set("color", Color.red);
				Coordinates lineBegin = new Coordinates(pointPosition.getX() - lineWidth, pointPosition.getY() + (int) (lineWidth * steigung));
				Coordinates lineEnd = new Coordinates(pointPosition.getX() + lineWidth, pointPosition.getY() - (int) (lineWidth * steigung));
				_prims.add(lang.newEllipse(pointPosition, new Coordinates(3, 3), "point" + x + "_" + y, null, ellipseProps));
				lang.nextStep();
				Text pointText = lang.newText(new Coordinates(pointPosition.getX() - 10, pointPosition.getY() + 10), "pixel", "step2", null, contentText);
				lang.nextStep();
				pointText.hide();
				int slopeLineWidth = (int) Math.sqrt(Math.pow(lineEnd.getX() - pointPosition.getX(), 2) + Math.pow(lineEnd.getY() - pointPosition.getY(), 2));
				_prims.add(lang.newPolyline(new Node[] { pointPosition, lineEnd }, "redLine", null, slopeLine));
				Text slopeText = lang.newText(new Coordinates(pointPosition.getX() + (lineEnd.getX() - pointPosition.getX()) / 2 - 10, pointPosition.getY()
						+ (lineEnd.getY() - pointPosition.getY()) / 2), "slope", "step2", null, contentText);

				lang.nextStep();
				slopeText.hide();

				_prims.add(lang.newText(new Coordinates(80, 450), "1.3.) The slope defines the direction to the edge. The direction of the edge", "step4", null, contentText));
				_prims.add(lang.newText(new Coordinates(80, 470), "      can be determined by finding the eight of a cirlce the slope is in.", "step4", null, contentText));
				lang.nextStep();
				_prims.add(lang.newEllipse(pointPosition, new Coordinates(lineWidth, lineWidth), "asda", null, circle));
				CircleSegProperties cirlceProps = new CircleSegProperties();

				// ANIMAL BUG TODO
				// int targetAngle = (int) (Math.abs((angle - 0.00001f) / 45f) *
				// 45);
				// cirlceProps.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY,
				// targetAngle);
				// cirlceProps.set(AnimationPropertiesKeys.ANGLE_PROPERTY, 45);
				// System.out.println(cirlceProps.toString());
				// _prims.add(lang.newCircleSeg(pointPosition, 50,
				// "circleSegment", null));
				//
				lang.nextStep();
				_prims.add(lang.newText(new Coordinates(80, 490), "1.4.) The segment's start angle and end angle are the two", "step5", null, contentText));
				_prims.add(lang.newText(new Coordinates(80, 510), "      possible edges", "step5", null, contentText));
				lang.nextStep();

				Primitive n = null;
				Primitive s = null;
				Primitive e = null;
				Primitive w = null;
				Primitive nw = null;
				Primitive ne = null;
				Primitive sw = null;
				Primitive se = null;
				lineProps = possibleEdges;
				int l = 50;
				if (isNegativeAngle) {
					if (between0and45) {
						// 0 to -45
						ne = lang.newPolyline(new Node[] { pointPosition, new Coordinates(pointPosition.getX() + l, pointPosition.getY() - l) }, "ne", null, lineProps);
						sw = lang.newPolyline(new Node[] { pointPosition, new Coordinates(pointPosition.getX() - l, pointPosition.getY() + l) }, "sw", null, lineProps);
						lang.nextStep();
						e = lang.newPolyline(new Node[] { pointPosition, new Coordinates(pointPosition.getX() + l, pointPosition.getY()) }, "e", null, lineProps);
						w = lang.newPolyline(new Node[] { pointPosition, new Coordinates(pointPosition.getX() - l, pointPosition.getY()) }, "w", null, lineProps);
					} else {
						// -45  to -90
						n = lang.newPolyline(new Node[] { pointPosition, new Coordinates(pointPosition.getX(), pointPosition.getY() - l) }, "n", null, lineProps);
						s = lang.newPolyline(new Node[] { pointPosition, new Coordinates(pointPosition.getX(), pointPosition.getY() + l) }, "s", null, lineProps);
						lang.nextStep();
						ne = lang.newPolyline(new Node[] { pointPosition, new Coordinates(pointPosition.getX() + l, pointPosition.getY() - l) }, "ne", null, lineProps);
						sw = lang.newPolyline(new Node[] { pointPosition, new Coordinates(pointPosition.getX() - l, pointPosition.getY() + l) }, "sw", null, lineProps);
					}
				} else {
					if (between0and45) {
						// 0 to 45
						nw = lang.newPolyline(new Node[] { pointPosition, new Coordinates(pointPosition.getX() - l, pointPosition.getY() - l) }, "nw", null, lineProps);
						se = lang.newPolyline(new Node[] { pointPosition, new Coordinates(pointPosition.getX() + l, pointPosition.getY() + l) }, "se", null, lineProps);
						lang.nextStep();
						e = lang.newPolyline(new Node[] { pointPosition, new Coordinates(pointPosition.getX() + l, pointPosition.getY()) }, "e", null, lineProps);
						w = lang.newPolyline(new Node[] { pointPosition, new Coordinates(pointPosition.getX() - l, pointPosition.getY()) }, "w", null, lineProps);
					} else {
						// 45 to 90
						n = lang.newPolyline(new Node[] { pointPosition, new Coordinates(pointPosition.getX(), pointPosition.getY() - l) }, "n", null, lineProps);
						s = lang.newPolyline(new Node[] { pointPosition, new Coordinates(pointPosition.getX(), pointPosition.getY() + l) }, "s", null, lineProps);
						lang.nextStep();
						nw = lang.newPolyline(new Node[] { pointPosition, new Coordinates(pointPosition.getX() - l, pointPosition.getY() - l) }, "nw", null, lineProps);
						se = lang.newPolyline(new Node[] { pointPosition, new Coordinates(pointPosition.getX() + l, pointPosition.getY() + l) }, "se", null, lineProps);
					}
				}

				_prims.add(n);
				_prims.add(s);
				_prims.add(w);
				_prims.add(e);
				_prims.add(nw);
				_prims.add(ne);
				_prims.add(sw);
				_prims.add(se);
				reads += 2;
				writes += 2;
				yGrad = Math.abs(yGrad);
				xGrad = Math.abs(xGrad);

				lang.nextStep();

				_prims.add(lang.newText(new Coordinates(80, 530), "1.5.) One edge represents a vertical, one a horizontal edge", "step5", null, contentText));
				lang.nextStep();// , y, x, xGrad >= yGrad ? ">=" : "<", y, x,
								// xGrad, xGrad > yGrad ? ">=" : "<", yGrad),
								// "step5", null
				_prims.add(lang.newText(new Coordinates(80, 550), "1.6) Compare horizontal (Dx) and vertical derivative (Dy). The larger one", "setp5", null, contentText));
				String edgeType = xGrad >= yGrad ? "horizontal" : "vertical";
				_prims.add(lang.newText(new Coordinates(80, 570), "      determines what edge to use. Result: " + edgeType + " edge", "step5", null, contentText));
				lang.nextStep();
				reads += 1;
				if (isNegativeAngle) {
					reads += 1;
					if (between0and45) {
						// 0  to -45
						if (yGrad <= xGrad) {
							ne.hide();
							sw.hide();
						} else {
							e.hide();
							w.hide();
						}
						reads += 6;
						if (yGrad > xGrad && (mag < neMag || mag < swMag)) {
							gradientMagnitude[y][x] = 0;
							writes++;
						}
						reads += 6;
						if (yGrad <= xGrad && (mag < eMag || mag < wMag)) {
							gradientMagnitude[y][x] = 0;
							writes++;
						}
					} else {
						// -45  to -90
						if (yGrad <= xGrad) {
							n.hide();
							s.hide();
						} else {
							ne.hide();
							sw.hide();
						}
						reads += 6;
						if (yGrad > xGrad && (mag < nMag || mag < sMag)) {
							gradientMagnitude[y][x] = 0;
							writes++;
						}
						reads += 6;
						if (yGrad <= xGrad && (mag < neMag || mag < swMag)) {
							gradientMagnitude[y][x] = 0;
							writes++;
						}
					}
				} else {
					reads += 1;
					if (between0and45) {
						// 0  to 45 
						if (yGrad <= xGrad) {
							nw.hide();
							se.hide();
						} else {
							e.hide();
							w.hide();
						}
						reads += 6;
						if (yGrad > xGrad && (mag < nwMag || mag < seMag)) {
							gradientMagnitude[y][x] = 0;
							writes++;
						}
						reads += 6;
						if (yGrad <= xGrad && (mag < eMag || mag < wMag)) {
							gradientMagnitude[y][x] = 0;
							writes++;
						}
					} else {
						// 45  to 90 
						if (yGrad <= xGrad) {
							n.hide();
							s.hide();
						} else {
							nw.hide();
							se.hide();
						}
						reads += 6;
						if (yGrad > xGrad && (mag < nMag || mag < sMag)) {
							gradientMagnitude[y][x] = 0;
							writes++;
						}
						reads += 6;
						if (yGrad <= xGrad && (mag < nwMag || mag < seMag)) {
							gradientMagnitude[y][x] = 0;
							writes++;
						}
					}
				}
				_prims.add(lang.newText(new Coordinates(80, 590), "1.7.) Compare the gradient magnitude of the current pixel to its neighbours ", "step5", null, contentText));
				_prims.add(lang.newText(new Coordinates(80, 610), "      which lie on remaining edge.", "step5", null, contentText));
				lang.nextStep();
				if (isNegativeAngle) {
					if (between0and45) {
						// 0  to -45
						if (yGrad <= xGrad) {
							magnitudeTable.highlightCell(y, x + 1, null, null);
							magnitudeTable.highlightCell(y, x - 1, null, null);
						} else {
							magnitudeTable.highlightCell(y - 1, x + 1, null, null);
							magnitudeTable.highlightCell(y + 1, x - 1, null, null);
						}
					} else {
						// -45  to -90
						if (yGrad <= xGrad) {
							magnitudeTable.highlightCell(y - 1, x + 1, null, null);
							magnitudeTable.highlightCell(y + 1, x - 1, null, null);
						} else {
							magnitudeTable.highlightCell(y + 1, x, null, null);
							magnitudeTable.highlightCell(y - 1, x, null, null);
						}
					}
				} else {
					if (between0and45) {
						// 0  to 45 
						if (yGrad <= xGrad) {
							magnitudeTable.highlightCell(y, x + 1, null, null);
							magnitudeTable.highlightCell(y, x - 1, null, null);
						} else {
							magnitudeTable.highlightCell(y - 1, x - 1, null, null);
							magnitudeTable.highlightCell(y + 1, x + 1, null, null);
						}
					} else {
						// 45 to 90
						if (yGrad <= xGrad) {
							magnitudeTable.highlightCell(y - 1, x - 1, null, null);
							magnitudeTable.highlightCell(y + 1, x + 1, null, null);
						} else {
							magnitudeTable.highlightCell(y - 1, x, null, null);
							magnitudeTable.highlightCell(y + 1, x, null, null);
						}
					}
				}
				lang.nextStep();
				_prims.add(lang.newText(new Coordinates(80, 630), "1.8.) If a neighbour is larger than the current magnitude, than earse it", "step5", null, contentText));
				_prims.add(lang.newText(new Coordinates(80, 650), "      by override it's value with zero.", "step5", null, contentText));
				resultTable.highlightCell(y, x, null, null);
				resultTable.put(y, x, gradientMagnitude[y][x], null, null);
				lang.nextStep();

				for (int y1 = 0; y1 < height; y1++) {
					for (int x1 = 0; x1 < width; x1++) {
						magnitudeTable.unhighlightCell(y1, x1, null, null);
					}
				}
				for (Primitive p : _prims) {
					if (p == null)
						continue;
					p.hide();
				}
				adds += 1;
				reads += 2;
			}
			adds += 1;
			reads += 2;
		}
		lang.nextStep("Non-Maxima Suppression finished");
		new EndPage(lang);
		return lang.toString().replace("refresh", "")
				.replace("color (0, 0, 0) textColor (0, 0, 0) fillColor (0, 0, 0) highlightTextColor (0, 0, 0) highlightBackColor (0, 0, 0) depth 1", "");
	}

	private int highlightedLine = -1;
	private IntMatrix magnitudeTable;
	private IntMatrix srcTable;

	private void highlight(int line) {
		if (highlightedLine > -1) {
			scConvolute.unhighlight(highlightedLine);
		}
		highlightedLine = line;
		scConvolute.highlight(highlightedLine);
	}

	public int[][] perform(int Dx[][], int Dy[][], Language lang) {
		int height = Dx.length;
		int width = Dx[0].length;
		int halfWindowSize = 1;

		// Calculate gradient magnitude based on derivatives x and y.
		int[][] gradientMagnitude = new int[height][width];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int xVal = Dx[y][x];
				int yVal = Dy[y][x];
				// Magnitude
				gradientMagnitude[y][x] = (int) (Math.sqrt((xVal * xVal) + (yVal * yVal)));
			}
		}

		// Perform non-maxima suppression.
		int line = 0;
		highlight(line++);
		for (int y = halfWindowSize; y < height - halfWindowSize; y++) {
			highlight(line++);
			for (int x = halfWindowSize; x < width - halfWindowSize; x++) {
				magnitudeTable.highlightCell(y, x, null, null);
				highlight(line++);
				int xGrad = Dx[y][x];
				highlight(line++);
				int yGrad = Dy[y][x];
				highlight(line++);
				int mag = gradientMagnitude[y][x];
				highlight(line++);
				int nMag = gradientMagnitude[y - 1][x];
				highlight(line++);
				int sMag = gradientMagnitude[y + 1][x];
				highlight(line++);
				int wMag = gradientMagnitude[y][x - 1];
				highlight(line++);
				int eMag = gradientMagnitude[y][x + 1];
				highlight(line++);
				int nwMag = gradientMagnitude[y - 1][x - 1];
				highlight(line++);
				int neMag = gradientMagnitude[y - 1][x + 1];
				highlight(line++);
				int swMag = gradientMagnitude[y + 1][x - 1];
				highlight(line++);
				int seMag = gradientMagnitude[y + 1][x + 1];
				highlight(line++);
				boolean isNegativeAngle = xGrad * yGrad <= 0;
				highlight(line++);
				boolean between0and45 = Math.abs(xGrad) >= Math.abs(yGrad);
				highlight(line++);
				if (isNegativeAngle) {
					highlight(line++);
					if (between0and45) {
						highlight(line++);
						// 0  to -45
						if (yGrad > xGrad && (mag < neMag || mag < swMag)) {
							highlight(line++);
							gradientMagnitude[y][x] = 0;
							highlight(line++);
						}
						highlight(line++);
						if (yGrad <= xGrad && (mag < eMag || mag < wMag)) {
							highlight(line++);
							gradientMagnitude[y][x] = 0;
							highlight(line++);
						}
						highlight(line++);
					} else {
						// -45  to 90
						highlight(line++);
						if (yGrad > xGrad && (mag < nMag || mag < sMag)) {
							highlight(line++);
							gradientMagnitude[y][x] = 0;
							highlight(line++);
						}
						highlight(line++);
						if (yGrad <= xGrad && (mag < neMag || mag < swMag)) {
							highlight(line++);
							gradientMagnitude[y][x] = 0;
							highlight(line++);
						}
						highlight(line++);
					}
					highlight(line++);
				} else {
					highlight(line++);
					if (between0and45) {
						// 0  to 45 
						highlight(line++);
						if (yGrad > xGrad && (mag < nwMag || mag < seMag)) {
							highlight(line++);
							gradientMagnitude[y][x] = 0;
							highlight(line++);
						}
						highlight(line++);
						if (yGrad <= xGrad && (mag < eMag || mag < wMag)) {
							highlight(line++);
							gradientMagnitude[y][x] = 0;
							highlight(line++);
						}
						highlight(line++);
					} else {
						// 45  to 90 
						highlight(line++);
						if (yGrad > xGrad && (mag < nMag || mag < sMag)) {
							highlight(line++);
							gradientMagnitude[y][x] = 0;
							highlight(line++);
						}
						highlight(line++);
						if (yGrad <= xGrad && (mag < nwMag || mag < seMag)) {
							highlight(line++);
							gradientMagnitude[y][x] = 0;
							highlight(line++);
						}
						highlight(line++);
					}
					highlight(line++);
				}
				highlight(line--);
			}
			highlight(line--);
		}

		return gradientMagnitude;
	}

	public String getName() {
		return "Non-maxima suppression";
	}

	public String getAlgorithmName() {
		return "Non-maxima suppression";
	}

	public String getAnimationAuthor() {
		return "Robert Hahn, Max Mehltretter";
	}

	public String getDescription() {
		return "Non-maximum suppression is an edge thinning technique.";
	}

	public String getCodeExample() {
		return "public int[][] perform(int Dx[][], int Dy[][], int windowSize) {\n" + "int height = Dx.length;\n" + "int width = Dx[0].length;\n"
				+ "int halfWindowSize = (int) Math.floor(windowSize / 2d);\n" + "\n" + "// Calculate gradient magnitude based on derivatives x and y.\n"
				+ "int[][] gradientMagnitude = new int[height][width];\n" + "for (int y = 0; y < height; y++) {\n" + "	for (int x = 0; x < width; x++) {\n"
				+ "		int xVal = Dx[y][x];\n" + "		int yVal = Dy[y][x];\n" + "		// Magnitude\n" + "		gradientMagnitude[y][x] = (int) (Math.sqrt((xVal * xVal) + (yVal * yVal)));\n"
				+ "	}\n" + "}\n" + "\n" + "// Perform non-maxima suppression.\n" + "for (int x = halfWindowSize; x < width - halfWindowSize; x++) {\n"
				+ "	for (int y = halfWindowSize; y < height - halfWindowSize; y++) {\n" + "		int xGrad = Dx[y][x];\n" + "		int yGrad = Dy[y][x];\n"
				+ "		int mag = gradientMagnitude[y][x];\n" + "		int nMag = gradientMagnitude[y - 1][x];\n" + "		int sMag = gradientMagnitude[y + 1][x];\n"
				+ "		int wMag = gradientMagnitude[y][x - 1];\n" + "		int eMag = gradientMagnitude[y][x + 1];\n" + "		int nwMag = gradientMagnitude[y - 1][x - 1];\n"
				+ "		int neMag = gradientMagnitude[y - 1][x + 1];\n" + "		int swMag = gradientMagnitude[y + 1][x - 1];\n" + "		int seMag = gradientMagnitude[y + 1][x + 1];\n"
				+ "		\n" + "		boolean isNegativeAngle = xGrad * yGrad <= 0;\n" + "		xGrad = Math.abs(Dx[y][x]);\n" + "		yGrad = Math.abs(Dy[y][x]);\n"
				+ "		boolean between0and45 = Math.abs(xGrad) > Math.abs(yGrad);\n" + "		if (isNegativeAngle) {\n" + "			if (between0and45) {\n" + "				// 0  to -45  \n"
				+ "				if (yGrad > xGrad && (mag < neMag || mag < swMag)) {\n" + "					gradientMagnitude[y][x] = 0;\n" + "				}\n"
				+ "				if (yGrad <= xGrad && (mag < eMag || mag < wMag)) {\n" + "					gradientMagnitude[y][x] = 0;\n" + "				}\n" + "			} else {\n" + "				// -45  to -90\n"
				+ "				if (yGrad > xGrad && (mag < nMag || mag < sMag)) {\n" + "					gradientMagnitude[y][x] = 0;\n" + "				}\n"
				+ "				if (yGrad <= xGrad && (mag < neMag || mag < swMag)) {\n" + "					gradientMagnitude[y][x] = 0;\n" + "				}\n" + "			}\n" + "		} else {\n"
				+ "			if (between0and45) {\n" + "				// 0  to 45 \n" + "				if (yGrad > xGrad && (mag < nwMag || mag < seMag)) {\n" + "					gradientMagnitude[y][x] = 0;\n"
				+ "				}\n" + "				if (yGrad <= xGrad && (mag < eMag || mag < wMag)) {\n" + "					gradientMagnitude[y][x] = 0;\n" + "				}\n" + "			} else {\n"
				+ "				// 45  to 90 \n" + "				if (yGrad > xGrad && (mag < nMag || mag < sMag)) {\n" + "					gradientMagnitude[y][x] = 0;\n" + "				}\n"
				+ "				if (yGrad <= xGrad && (mag < nwMag || mag < seMag)) {\n" + "					gradientMagnitude[y][x] = 0;\n" + "				}\n" + "			}\n" + "		}\n" + "	}\n" + "}\n"
				+ "return gradientMagnitude;\n" + "}";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1) throws IllegalArgumentException {
		int[][] srcImage = (int[][]) arg1.get("srcImage");
		if (srcImage.length < 3 || srcImage[0].length < 3) {
			JOptionPane.showMessageDialog(null, "The source image should have a size of at least 3x3.", "Invalid source image", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	public static class SobelFilterImpl {

		public static final int[][] xKernel = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };
		public static final int[][] yKernel = { { 1, 2, 1 }, { 0, 0, 0 }, { -1, -2, -1 } };

		public static int[][] convolute(int[][] src, int[][] kernel) {
			int srcHeight = src.length;
			int srcWidth = src[0].length;
			int kernelSize = kernel.length;
			int[][] dst = new int[srcHeight][srcWidth];
			copyBorder(src, dst, kernelSize);
			for (int y = 1; y < srcHeight - 1; y++) {
				for (int x = 1; x < srcWidth - 1; x++) {
					dst[y][x] = applyFilter(src, kernel, kernelSize, y, x);
				}
			}
			return dst;
		}

		private static int applyFilter(int[][] src, int[][] kernel, int kernelSize, int y, int x) {
			int borderSize = (int) Math.floor(kernelSize / 2d);
			int value = 0;
			for (int i = y - borderSize; i < y + borderSize + 1; i++) {
				for (int u = x - borderSize; u < x + borderSize + 1; u++) {
					value += (src[i][u]) * kernel[i - y + borderSize][u - x + borderSize];
				}
			}
			return (int) (value);
		}

		private static void copyBorder(int[][] src, int[][] dst, int kernelSize) {
			int borderSize = (int) Math.floor(kernelSize / 2d);
			// top border
			for (int y = 0; y < borderSize; y++) {
				for (int x = 0; x < src[y].length; x++) {
					dst[y][x] = src[y][x];
				}
			}
			// bottom border
			for (int y = src.length - 1; y > src.length - borderSize - 1; y--) {
				for (int x = 0; x < src[y].length; x++) {
					dst[y][x] = src[y][x];
				}
			}
			// left border
			for (int y = borderSize; y < src.length - borderSize; y++) {
				for (int x = 0; x < borderSize; x++) {
					dst[y][x] = src[y][x];
				}
			}
			// right border
			for (int y = borderSize; y < src.length - borderSize; y++) {
				for (int x = src[y].length - 1; x > src[y].length - 1 - borderSize; x--) {
					dst[y][x] = src[y][x];
				}
			}
		}

	}
}