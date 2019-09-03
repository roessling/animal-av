/*
 * SobelFilter.java
 * Robert Hahn, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graphics;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
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
import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Hidden;
import algoanim.util.MsTiming;

public class SobelFilter implements ValidatingGenerator {
	private Language lang;
	private int[][] srcImage;
	private SobelFilterGenerator sobelFilterImpl;
	private int[][] kernel;

	public void init() {
		lang = new AnimalScript("Sobelfilter", "Robert Hahn", 800, 600);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		srcImage = (int[][]) primitives.get("srcImage");
		kernel = (int[][]) primitives.get("kernel");

		sobelFilterImpl = new SobelFilterGenerator(lang, props, primitives);
		sobelFilterImpl.convolute(srcImage, kernel);
		return lang.toString().replace("refresh", "").replace("color (0, 0, 0) textColor (0, 0, 0) fillColor (0, 0, 0) highlightTextColor (0, 0, 0) highlightBackColor (0, 0, 0) depth 1", "");
	}

	public String getName() {
		return "Sobel filter";
	}

	public String getAlgorithmName() {
		return "Sobel filter";
	}

	public String getAnimationAuthor() {
		return "Robert Hahn, Max Mehltretter";
	}

	public String getDescription() {
		return "The Sobel operator is used in image processing and computer vision and creates an image which emphasizes edges and transitions.";
	}

	public String getCodeExample() {
		return "public class SobelFilter {" + "\n" + "\n" + "	private static final int[][] xKernel = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };" + "\n"
				+ "	private static final int[][] yKernel = { { 1, 2, 1 }, { 0, 0, 0 }, { -1, -2, -1 } };" + "\n" + "\n" + "	private static int[][] sobel(int src[][]) {" + "\n"
				+ "		int[][] Gx = convolute(src, xKernel);" + "\n" + "		int[][] Gy = convolute(src, yKernel);" + "\n" + "		int[][] G = merge(Gx, Gy);" + "\n" + "		return G;" + "\n" + "	}" + "\n"
				+ "	" + "\n" + "	private static int[][] merge(int Gx[][], int Gy[][]) {" + "\n" + "		int height = Gx.length;" + "\n" + "		int width = Gx[0].length;" + "\n"
				+ "		int[][] G = new int[height][width];" + "\n" + "		for (int y = 0; y < height - 1; y++) {" + "\n" + "			for (int x = 0; x < width - 1; x++) {" + "\n" + "				int valGx = Gx[y][x];"
				+ "\n" + "				int valGy = Gy[y][x];" + "\n" + "				G[y][x] = (int) Math.sqrt(valGx * valGx + valGy * valGy);" + "\n" + "			}" + "\n" + "		}" + "\n" + "		return G;" + "\n" + "	}"
				+ "\n" + "\n" + "	private static int[][] convolute(int[][] src, int[][] kernel) {" + "\n" + "		int srcHeight = src.length;" + "\n" + "		int srcWidth = src[0].length;" + "\n"
				+ "		int kernelSize = kernel.length;" + "\n" + "\n" + "		int[][] dst = new int[srcHeight][srcWidth];" + "\n" + "		copyBorder(src, dst, kernelSize);" + "\n" + "\n"
				+ "		for (int y = 1; y < srcHeight - 1; y++) {" + "\n" + "			for (int x = 1; x < srcWidth - 1; x++) {" + "\n" + "				dst[y][x] = applyFilter(src, kernel, kernelSize, y, x);" + "\n"
				+ "			}" + "\n" + "		}" + "\n" + "\n" + "		return dst;" + "\n" + "	}" + "\n" + "\n" + "	private static int applyFilter(int[][] src, int[][] kernel, int kernelSize," + "\n"
				+ "			int y, int x) {" + "\n" + "		int borderSize = (int) Math.floor(kernelSize / 2d);" + "\n" + "		int value = 0;" + "\n"
				+ "		for (int i = y - borderSize; i < y + borderSize + 1; i++) {" + "\n" + "			for (int u = x - borderSize; u < x + borderSize + 1; u++) {" + "\n" + "				value += (src[i][u])" + "\n"
				+ "						* kernel[i - y + borderSize][u - x + borderSize];" + "\n" + "			}" + "\n" + "		}" + "\n" + "		return (int) (value);" + "\n" + "	}" + "\n" + "\n"
				+ "	private static void copyBorder(int[][] src, int[][] dst, int kernelSize) {" + "\n" + "		int borderSize = (int) Math.floor(kernelSize / 2d);" + "\n" + "		// top border" + "\n"
				+ "		for (int y = 0; y < borderSize; y++) {" + "\n" + "			for (int x = 0; x < src[y].length; x++) {" + "\n" + "				dst[y][x] = src[y][x];" + "\n" + "			}" + "\n" + "		}" + "\n"
				+ "		// bottom border" + "\n" + "		for (int y = src.length - 1; y > src.length - borderSize - 1; y--) {" + "\n" + "			for (int x = 0; x < src[y].length; x++) {" + "\n"
				+ "				dst[y][x] = src[y][x];" + "\n" + "			}" + "\n" + "		}" + "\n" + "		// left border" + "\n" + "		for (int y = 0; y < src.length; y++) {" + "\n"
				+ "			for (int x = 0; x < borderSize; x++) {" + "\n" + "				dst[y][x] = src[y][x];" + "\n" + "			}" + "\n" + "		}" + "\n" + "		// right border" + "\n"
				+ "		for (int y = 0; y < src.length; y++) {" + "\n" + "			for (int x = src[y].length - 1; x > src[y].length - 1 - borderSize; x--) {" + "\n" + "				dst[y][x] = src[y][x];" + "\n"
				+ "			}" + "\n" + "		}" + "\n" + "	}" + "\n" + "\n" + "}" + "\n";
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
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1) throws IllegalArgumentException {
		int[][] srcImage = (int[][]) arg1.get("srcImage");
		int[][] kernel = (int[][]) arg1.get("kernel");
		if (kernel.length != kernel[0].length ) {
			JOptionPane.showMessageDialog(null, "The kernel should be a square..", "Invalid kernel", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if (kernel.length != 3 && kernel.length != 5) {
			JOptionPane.showMessageDialog(null, "The kernel should be either a 3x3 or 5x5 matrix.", "Invalid kernel", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if (srcImage.length < kernel.length || srcImage[0].length < kernel.length) {
			JOptionPane.showMessageDialog(null, "The source image should not be smaller than the kernel.", "Invalid source image", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	public static class SobelFilterGenerator {

		private class StartPage {
			public StartPage(Language lang) {
				List<Primitive> prims = new ArrayList<>();
//				Font headerFont = new Font("SansSerif", Font.BOLD, 25);
//				Font headlineFont = new Font("SansSerif", Font.BOLD, 13);
//				Font accentFont = new Font("SansSerif", Font.BOLD, 10);
//				TextProperties headerProp = new TextProperties();
//				headerProp.set(AnimationPropertiesKeys.FONT_PROPERTY, headerFont);
//
//				TextProperties accentProp = new TextProperties();
//				accentProp.set(AnimationPropertiesKeys.FONT_PROPERTY, accentFont);
//
//				TextProperties headlineProp = new TextProperties();
//				headlineProp.set(AnimationPropertiesKeys.FONT_PROPERTY, headlineFont);
				SourceCodeProperties sourceProps = new SourceCodeProperties();
				sourceProps.set(AnimationPropertiesKeys.FONT_PROPERTY, contentFont);
				sourceProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, contentText.get(AnimationPropertiesKeys.COLOR_PROPERTY));
				sourceProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, contentText.get(AnimationPropertiesKeys.COLOR_PROPERTY));
				
				prims.add(lang.newText(new Coordinates(20, 20), "Sobel filter", "t1", null, titleText));
				SourceCode newSourceCode = lang.newSourceCode(new Coordinates(20, 35), "t2", null,sourceProps);
				newSourceCode.addCodeLine("The sobel filter is often used in the computer vision to", null, 0, null);
				newSourceCode.addCodeLine("detect edges in images. The filter is applied to the image", null, 0, null);
				newSourceCode.addCodeLine("with a convolution.", null, 0, null);
				newSourceCode.addCodeLine("The algorithm from the convolution will compute the derivative", null, 0, null);
				newSourceCode.addCodeLine("of the image depending on the kernel which was used. The sobel", null, 0, null);
				newSourceCode.addCodeLine("filter is used with a 3x3 kernel.", null, 0, null);
				prims.add(newSourceCode);

				prims.add(lang.newText(new Coordinates(20, 160), "Arguments", "t1", null, headlineText));
				prims.add(lang.newText(new Coordinates(20, 180), "srcImage", "t1", null, contentText));
				newSourceCode = lang.newSourceCode(new Coordinates(140, 170), "t2", null, sourceProps);
				newSourceCode.addCodeLine("The sobel filter operates on an image and thus you have", null, 0, null);
				newSourceCode.addCodeLine("to pass an image to it. The derivative of the image will", null, 0, null);
				newSourceCode.addCodeLine("be calculated.", null, 0, null);
				newSourceCode.addCodeLine("The image should at least have a size of 3x3.", null, 0, null);
				prims.add(newSourceCode);

				prims.add(lang.newText(new Coordinates(20, 250), "kernel", "t1", null, contentText));
				newSourceCode = lang.newSourceCode(new Coordinates(140, 240), "t2", null, sourceProps);
				newSourceCode.addCodeLine("The kernel is a matrix which is applied to the image", null, 0, null);
				newSourceCode.addCodeLine("with a convolution. Depending on the kernel you can", null, 0, null);
				newSourceCode.addCodeLine("compute the x- or y-derivative. ", null, 0, null);
				prims.add(newSourceCode);

				lang.nextStep();
				for (Primitive p : prims) {
					p.hide();
				}
			}

		}

		private class EndPage {
			public EndPage(Language lang) {
				SourceCodeProperties sourceProps = new SourceCodeProperties();
				sourceProps.set(AnimationPropertiesKeys.FONT_PROPERTY, contentFont);
				sourceProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, contentText.get(AnimationPropertiesKeys.COLOR_PROPERTY));
				sourceProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, contentText.get(AnimationPropertiesKeys.COLOR_PROPERTY));
				
				List<Primitive> prims = new ArrayList<>();

				prims.add(lang.newText(new Coordinates(20, 20), "Sobel filter", "t1", null, headlineText));
				SourceCode newSourceCode = lang.newSourceCode(new Coordinates(20, 35), "t2", null, sourceProps);
				newSourceCode.addCodeLine("Additions: " + adds, null, 0, null);
				newSourceCode.addCodeLine("Multiplications: " + muls, null, 0, null);
				newSourceCode.addCodeLine("Reads: " + reads, null, 0, null);
				newSourceCode.addCodeLine("Writes: " + writes, null, 0, null);
				newSourceCode.addCodeLine("", null, 0, null);
				newSourceCode.addCodeLine("Complexity: O(mn) per pixel with a m x n kernel", null, 0, null);
				prims.add(newSourceCode);

				prims.add(lang.newText(new Coordinates(20, 160), "Alternatives", "t1", null, headlineText));
				newSourceCode = lang.newSourceCode(new Coordinates(20, 170), "t2", null, sourceProps);
				newSourceCode.addCodeLine("- Line Detection", null, 0, null);
				newSourceCode.addCodeLine("- Frei-Chen edge detector", null, 0, null);
				prims.add(newSourceCode);

				lang.nextStep();
				for (Primitive p : prims) {
					p.hide();
				}
			}

		}

		private int adds;
		private int muls;
		private int reads;
		private int writes;
		public static final int[][] xKernel = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };
		public static final int[][] yKernel = { { 1, 2, 1 }, { 0, 0, 0 }, { -1, -2, -1 } };
		private static final int MATRIX_CELL_SIZE = 20;
		private Language lang;
		// private SourceCode scSobel;
		// private SourceCode scMerge;
		private SourceCode scCopyBorder;
		private SourceCode scApplyFilter;
		private SourceCode scConvolute;
		private Text signatureCopyBorder;
		private Text signatureApplyFilter;
		private Text signatureConvolute;
		// private Text signatureMerge;
		// private Text signatureSobel;
		private IntMatrix dstMatrix;
		private Text textY;
		private Text textX;
		private Text textBorderSize;
		private MatrixProperties matrixProp;
		private Text textKernelSize;
		private IntMatrix srcMatrix;
		// private IntMatrix appliedKernel;
		private IntMatrix kernelMatrix;
		private Text textCalc;
		private boolean skipCopyBorderProcedure;
		private boolean skipApplyFilterProcedure;
		private boolean skipBorderSizeCalculation;
		private AnimationProperties srcImageMatrixProps;
		private AnimationProperties dstImageMatrixProps;
		private AnimationProperties kernelMatrixProps;
		private SourceCodeProperties sourcecodeHighlightColorProps;
		private TextProperties titleText;
		private TextProperties contentText;
		private TextProperties headlineText;
		private Font titleFont;
		private Font contentFont;
		private Font headlineFont;
		private Text textBorder;

		public SobelFilterGenerator(Language lang, AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
			this.lang = lang;



			titleText = (TextProperties) props.getPropertiesByName("titleText");
			contentText = (TextProperties) props.getPropertiesByName("contentText");
			headlineText = (TextProperties) props.getPropertiesByName("headlineText");
			titleFont = new Font("SansSerif", Font.BOLD, 25);
			contentFont = new Font("SansSerif", Font.PLAIN, 13);
			headlineFont = new Font("SansSerif", Font.BOLD, 16);
			titleText.set(AnimationPropertiesKeys.FONT_PROPERTY, titleFont);
			contentText.set(AnimationPropertiesKeys.FONT_PROPERTY, contentFont);
			headlineText.set(AnimationPropertiesKeys.FONT_PROPERTY, headlineFont);
			
			new StartPage(lang);
			skipCopyBorderProcedure = (boolean) primitives.get("skipCopyBorderProcedure");
			skipApplyFilterProcedure = (boolean) primitives.get("skipApplyFilterProcedure");
			skipBorderSizeCalculation = (boolean) primitives.get("skipBorderSizeCalculation");
			srcImageMatrixProps = props.getPropertiesByName("srcImageMatrix");
			dstImageMatrixProps = props.getPropertiesByName("dstImageMatrix");
			kernelMatrixProps = props.getPropertiesByName("kernelMatrix");
			sourcecodeHighlightColorProps = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
			
			srcImageMatrixProps.set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, MATRIX_CELL_SIZE);
			srcImageMatrixProps.set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, MATRIX_CELL_SIZE);
			dstImageMatrixProps.set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, MATRIX_CELL_SIZE);
			dstImageMatrixProps.set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, MATRIX_CELL_SIZE);
			kernelMatrixProps.set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, MATRIX_CELL_SIZE);
			kernelMatrixProps.set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, MATRIX_CELL_SIZE);


			createSourcecode();
		}


		private int getMatrixHeight(IntMatrix matrix) {
			return matrix.getNrRows() * MATRIX_CELL_SIZE;
		}

		private int getMatrixWidth(IntMatrix matrix) {
			return matrix.getNrCols() * MATRIX_CELL_SIZE;
		}
		public int[][] convolute(int[][] src, int[][] kernel) {
			int yOffset = 100;
			int xOffset = 800;
			
			lang.newText(new Coordinates(xOffset, yOffset), "src", "srcText", null, contentText);
			yOffset += 20;
			int srcMatrixY = yOffset;
			int srcMatrixX = xOffset;
			srcMatrix = lang.newIntMatrix(new Coordinates(xOffset, yOffset), src, "src", null, (MatrixProperties) srcImageMatrixProps);
			xOffset += getMatrixWidth(srcMatrix) + 150;
			yOffset -= 20;
			lang.newText(new Coordinates(xOffset, yOffset), "kernel", "kernelText", null, contentText);
			yOffset += 20;
			int kernelMatrixY = yOffset;
			int kernelMatrixX = xOffset;
			kernelMatrix = lang.newIntMatrix(new Coordinates(xOffset, yOffset), kernel, "kernel", null, (MatrixProperties) kernelMatrixProps);
			yOffset += (getMatrixHeight(srcMatrix) + 100) * 2;
			xOffset -= getMatrixWidth(srcMatrix) + 150;
			

			MethodManager.showMethod(scConvolute);
			lang.nextStep();
			// MethodManager.hideMethod(scSobel);
			MethodManager.highlight(scConvolute, 0);
			signatureConvolute.changeColor(null, (Color) sourcecodeHighlightColorProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
			lang.nextStep();
			int srcHeight = src.length;
			lang.newText(new Coordinates(srcMatrixX + getMatrixWidth(srcMatrix) + (srcMatrix.getNrCols() * 9), 30 + (int) (srcMatrixY + getMatrixHeight(srcMatrix) / 2f)), "srcHeight: " + src[0].length, "srcWHeightText", null, contentText);
			MethodManager.highlight(scConvolute, 1);
			lang.nextStep();
			int srcWidth = src[0].length;
			lang.newText(new Coordinates(srcMatrixX + 30, srcMatrixY + getMatrixHeight(srcMatrix) + srcMatrix.getNrRows() * 9), "srcWidth: " + src.length, "srcWidthText", null, contentText);
			int srcMatrixWidthY = srcMatrixY + getMatrixHeight(srcMatrix) + srcMatrix.getNrRows() * 9;
			MethodManager.highlight(scConvolute, 2);
			lang.nextStep();
			int kernelSize = kernel.length;
			textKernelSize = lang.newText(new Coordinates(kernelMatrixX + 10, kernelMatrixY + getMatrixHeight(kernelMatrix) + kernelMatrix.getNrRows() * 9), "kernelSize: " + kernelSize, "kernelSize", null, contentText);
			MethodManager.highlight(scConvolute, 3);
			lang.nextStep();
			yOffset = srcMatrixWidthY + 40;
			lang.newText(new Coordinates(xOffset, yOffset), "dst", "dstText", null, contentText);
			int[][] dst = new int[srcHeight][srcWidth];
			yOffset += 20;
			dstMatrix = lang.newIntMatrix(new Coordinates(xOffset, yOffset), dst, "dst", null, (MatrixProperties) dstImageMatrixProps);
			yOffset += getMatrixHeight(dstMatrix)  + 9 * dstMatrix.getNrRows() + 80;
			

			textBorderSize = lang.newText(new Coordinates(xOffset, yOffset - 20), "y: not set", "asdasdz", new Hidden(), contentText);
			textX = lang.newText(new Coordinates(xOffset, yOffset), "x: not set", "x", null, contentText);
			textY = lang.newText(new Coordinates(xOffset, yOffset + 20), "y: not set", "y", null, contentText);
			textCalc = lang.newText(new Coordinates(xOffset, yOffset + 40), "y: not set", "adawdawd", new Hidden(), contentText);
			
			MethodManager.highlight(scConvolute, 4);
			lang.nextStep();

			MethodManager.highlight(scConvolute, 5);
			copyBorderComplexity(src, dst, kernelSize);
			if (skipCopyBorderProcedure) {
				lang.nextStep();
				copyBorderClean(src, dst, kernelSize);
			} else {
				lang.nextStep();
				copyBorder(src, dst, kernelSize);
				MethodManager.highlight(scCopyBorder, 0);
				for (int y = 0; y < src.length; y++) {
					for (int x = 0; x < src[y].length; x++) {
						dstMatrix.unhighlightCell(y, x, null, null);
						srcMatrix.unhighlightCell(y, x, null, null);
					}
				}
				MethodManager.showMethod(scConvolute);
				MethodManager.hideMethod(scCopyBorder);
			}
			MethodManager.highlight(scConvolute, 6);
			lang.nextStep();
			for (int y = 1; y < srcHeight - 1; y++) {
				for (int x = 1; x < srcWidth - 1; x++) {
					applyFilterComplexity(src, kernel, kernelSize, y, x);
					reads = reads + 5 + 2;
					writes += 2;
					adds++;
					adds++;
				}
				writes += 2;
				reads += 2;
				adds++;
				adds++;
			}
			int borderSize = (int)(kernelSize / 2d);
			for (int y = borderSize; y < srcHeight - borderSize; y++) {
				textY.setText("y: " + y, null, null);
				for (int x = borderSize; x < srcWidth - borderSize; x++) {
					MethodManager.highlight(scConvolute, 7);
					lang.nextStep("Compute pixel: (y = " + y + ", x = " + x + ")");
					textX.setText("x: " + x, null, null);
					MethodManager.highlight(scConvolute, 8);
					if (skipApplyFilterProcedure) {
						dstMatrix.highlightCell(y, x, null, null);
						dst[y][x] = applyFilterClean(src, kernel, kernelSize, y, x);
						dstMatrix.put(y, x, dst[y][x], null, null);
					} else {
						dstMatrix.highlightCell(y, x, null, null);
						dst[y][x] = applyFilter(src, kernel, kernelSize, y, x);
						dstMatrix.put(y, x, dst[y][x], null, null);
						MethodManager.highlight(scApplyFilter, 0);
						MethodManager.hideMethod(scApplyFilter);
						MethodManager.showMethod(scConvolute);
					}
					lang.nextStep();
				}
			}
			signatureCopyBorder.changeColor(null, Color.black, null, null);
			MethodManager.highlight(scConvolute, 11);
			lang.nextStep("Finished");
			scApplyFilter.hide();
			scConvolute.hide();
			scCopyBorder.hide();

			signatureApplyFilter.hide();
			signatureConvolute.hide();
			signatureCopyBorder.hide();
			new EndPage(lang);
			return dst;
		}

		private void applyFilterComplexity(int[][] src, int[][] kernel, int kernelSize, int y, int x) {
			int borderSize = (int) Math.floor(kernelSize / 2d);
			writes += 1;
			muls += 1;
			reads += 1;

			adds += 1;
			for (int i = y - borderSize; i < y + borderSize + 1; i++) {
				adds += 1;
				for (int u = x - borderSize; u < x + borderSize + 1; u++) {
					adds += 1;
					adds += 1;
					adds += 1;
					reads++;
					reads++;
					reads++;
					reads++;
					reads++;
					reads++;
					reads++;
					reads++;
					reads++;
					reads++;
					reads++;
					reads++;
					reads++;
					reads++;
					adds += 1;
					writes += 3;
				}
				writes += 2;
				adds += 1;
			}
		}

		private int applyFilterClean(int[][] src, int[][] kernel, int kernelSize, int y, int x) {
			int borderSize = (int) Math.floor(kernelSize / 2d);
			int value = 0;
//			for(int i = -1 * borderSize; i <= borderSize; i++) {
//				for(int u = -1 * borderSize; u <= borderSize; u++) {
//					value += (src[y + i])
//				}	
//			}
			
			for (int i = y - borderSize; i < y + borderSize + 1; i++) {
				for (int u = x - borderSize; u < x + borderSize + 1; u++) {
					value += (src[i][u]) * kernel[i - y + borderSize][u - x + borderSize];
				}
			}
			return (int) (value);
		}

		private int applyFilter(int[][] src, int[][] kernel, int kernelSize, int y, int x) {
			MethodManager.showMethod(scApplyFilter);
			for (int v = 0; v < src.length; v++) {
				for (int z = 0; z < src[y].length; z++) {
					srcMatrix.unhighlightCell(v, z, null, null);
				}
			}
			for (int v = 0; v < kernelSize; v++) {
				for (int z = 0; z < kernelSize; z++) {
					kernelMatrix.unhighlightCell(v, z, null, null);
				}
			}
			lang.nextStep();
			MethodManager.hideMethod(scConvolute);
			MethodManager.highlight(scApplyFilter, 0);
			signatureApplyFilter.changeColor(null, (Color) sourcecodeHighlightColorProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
			lang.nextStep();
			int borderSize = (int) Math.floor(kernelSize / 2d);
			MethodManager.highlight(scApplyFilter, 2);
			lang.nextStep();
			int value = 0;
			MethodManager.highlight(scApplyFilter, 3);
			lang.nextStep();
			String message = "dst[" + y + "][" + x + "] = ( ";
			textCalc.setText(message, null, null);
			textCalc.show();
			lang.nextStep();

			for (int i = y - borderSize; i < y + borderSize + 1; i++) {
				MethodManager.highlight(scApplyFilter, 4);
				lang.nextStep();
				for (int u = x - borderSize; u < x + borderSize + 1; u++) {
					MethodManager.highlight(scApplyFilter, 5);
					lang.nextStep();
					MethodManager.highlight(scApplyFilter, 6);
					srcMatrix.highlightCell(i, u, null, null);
					textCalc.setText(message + "(" + (src[i][u]) + " * ...", null, null);
					lang.nextStep();
					kernelMatrix.highlightCell(i - y + borderSize, u - x + borderSize, null, null);
					message += "(" + (src[i][u]) + " * " + (kernel[i - y + borderSize][u - x + borderSize]) + ")";
					if (i + 1 >= y + borderSize + 1 && u + 1 >= x + borderSize + 1) {
						message += ")";
						textCalc.setText(message, null, null);
					} else {
						message += " + ";
						textCalc.setText(message + "...", null, null);
					}
					lang.nextStep();
					value += (src[i][u]) * kernel[i - y + borderSize][u - x + borderSize];
				}
			}
			textCalc.setText(message + " = " + value, null, null);
			MethodManager.highlight(scApplyFilter, 9);
			lang.nextStep();
			signatureApplyFilter.changeColor(null, Color.black, null, null);
			return (int) (value);
		}

		private void copyBorderComplexity(int[][] src, int[][] dst, int kernelSize) {
			reads++;
			writes++;
			muls++;
			int borderSize = (int) Math.floor(kernelSize / 2d);
			// top border
			for (int y = 0; y < borderSize; y++) {
				for (int x = 0; x < src[y].length; x++) {
					reads += 5;
					writes++;

					reads++;
					writes++;
					adds++;
				}
				reads++;
				reads++;
				writes++;
				writes++;
				adds++;
			}
			// bottom border
			for (int y = src.length - 1; y > src.length - borderSize - 1; y--) {
				for (int x = 0; x < src[y].length; x++) {
					reads += 5;
					writes++;

					reads++;
					writes++;
					adds++;
				}
				reads++;
				reads++;
				writes++;
				writes++;
				adds++;
			}
			// left border
			for (int y = borderSize; y < src.length - borderSize; y++) {
				for (int x = 0; x < borderSize; x++) {
					reads += 5;
					writes++;

					reads++;
					writes++;
					adds++;
				}
				reads++;
				reads++;
				writes++;
				writes++;
				adds++;
			}
			// right border
			for (int y = borderSize; y < src.length - borderSize; y++) {
				for (int x = src[y].length - 1; x > src[y].length - 1 - borderSize; x--) {
					reads += 5;
					writes++;

					reads++;
					writes++;
					adds++;
				}
				reads++;
				reads++;
				writes++;
				writes++;
				adds++;
			}
		}

		private void copyBorderClean(int[][] src, int[][] dst, int kernelSize) {
			int borderSize = (int) Math.floor(kernelSize / 2d);
//			textBorderSize = lang.newText(new Coordinates(800, 255 + 170), "borderSize: floor(kernelSize / 2)", "borderSize", null, contentText);
			textBorderSize.setText("borderSize: " + borderSize, null, null);
			// top border
			for (int y = 0; y < borderSize; y++) {
				for (int x = 0; x < src[y].length; x++) {
					dst[y][x] = src[y][x];
					dstMatrix.put(y, x, dst[y][x], null, null);
				}
			}
			// bottom border
			for (int y = src.length - 1; y > src.length - borderSize - 1; y--) {
				for (int x = 0; x < src[y].length; x++) {
					dst[y][x] = src[y][x];
					dstMatrix.put(y, x, dst[y][x], null, null);
				}
			}
			// left border
			for (int y = borderSize; y < src.length - borderSize; y++) {
				for (int x = 0; x < borderSize; x++) {
					dst[y][x] = src[y][x];
					dstMatrix.put(y, x, dst[y][x], null, null);
				}
			}
			// right border
			for (int y = borderSize; y < src.length - borderSize; y++) {
				for (int x = src[y].length - 1; x > src[y].length - 1 - borderSize; x--) {
					dst[y][x] = src[y][x];
					dstMatrix.put(y, x, dst[y][x], null, null);
				}
			}
		}

		private void copyBorder(int[][] src, int[][] dst, int kernelSize) {
			MethodManager.showMethod(scCopyBorder);
			lang.nextStep();
			MethodManager.hideMethod(scConvolute);
			signatureCopyBorder.changeColor(null, (Color) sourcecodeHighlightColorProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
			MethodManager.highlight(scCopyBorder, 0);
			lang.nextStep();
			int borderSize = (int) Math.floor(kernelSize / 2d);
			textBorderSize.setText("borderSize: floor(kernelSize / 2)", null, null);
			MethodManager.highlight(scCopyBorder, 1);
			if (!skipBorderSizeCalculation) {
				lang.nextStep();
				textBorderSize.setText("borderSize: floor(" + kernelSize + " / 2)", null, null);
				lang.nextStep();
				textBorderSize.setText("borderSize: floor(" + ((int) ((kernelSize / 2f) * 100f) / 100f) + ")", null, null);
				lang.nextStep();
			}
			textBorderSize.setText("borderSize: " + borderSize, null, null);
			lang.nextStep();
			// top border
			for (int y = 0; y < borderSize; y++) {
				textY.setText("y: " + y, null, null);
				MethodManager.highlight(scCopyBorder, 3);
				lang.nextStep();
				for (int x = 0; x < src[y].length; x++) {
					textX.setText("x: " + x, null, null);
					MethodManager.highlight(scCopyBorder, 4);
					lang.nextStep();
					dst[y][x] = src[y][x];
					dstMatrix.highlightCell(y, x, null, null);
					srcMatrix.highlightCell(y, x, null, null);
					dstMatrix.put(y, x, dst[y][x], null, null);
					MethodManager.highlight(scCopyBorder, 5);
				}
				lang.nextStep();
			}
			// bottom border
			for (int y = src.length - 1; y > src.length - borderSize - 1; y--) {
				textY.setText("y: " + y, null, null);
				MethodManager.highlight(scCopyBorder, 9);
				lang.nextStep();
				for (int x = 0; x < src[y].length; x++) {
					textX.setText("x: " + x, null, null);
					MethodManager.highlight(scCopyBorder, 10);
					lang.nextStep();
					dst[y][x] = src[y][x];
					dstMatrix.highlightCell(y, x, null, null);
					srcMatrix.highlightCell(y, x, null, null);
					dstMatrix.put(y, x, dst[y][x], null, null);
					MethodManager.highlight(scCopyBorder, 11);
					lang.nextStep();
				}
			}
			// left border
			for (int y = borderSize; y < src.length - borderSize; y++) {
				textY.setText("y: " + y, null, null);
				MethodManager.highlight(scCopyBorder, 15);
				lang.nextStep();
				for (int x = 0; x < borderSize; x++) {
					textX.setText("x: " + x, null, null);
					MethodManager.highlight(scCopyBorder, 16);
					lang.nextStep();
					dst[y][x] = src[y][x];
					dstMatrix.highlightCell(y, x, null, null);
					srcMatrix.highlightCell(y, x, null, null);
					dstMatrix.put(y, x, dst[y][x], null, null);
					MethodManager.highlight(scCopyBorder, 17);
					lang.nextStep();
				}
			}
			// right border
			for (int y = borderSize; y < src.length - borderSize; y++) {
				textY.setText("y: " + y, null, null);
				MethodManager.highlight(scCopyBorder, 21);
				lang.nextStep();
				for (int x = src[y].length - 1; x > src[y].length - 1 - borderSize; x--) {
					textX.setText("x: " + x, null, null);
					MethodManager.highlight(scCopyBorder, 22);
					lang.nextStep();
					dst[y][x] = src[y][x];
					dstMatrix.highlightCell(y, x, null, null);
					srcMatrix.highlightCell(y, x, null, null);
					dstMatrix.put(y, x, dst[y][x], null, null);
					MethodManager.highlight(scCopyBorder, 23);
					lang.nextStep();
				}
			}
			signatureCopyBorder.changeColor(null, Color.black, null, null);
		}

		private void createSourcecode() {
			TextProperties signature = new TextProperties();
			signature.set(AnimationPropertiesKeys.COLOR_PROPERTY, sourcecodeHighlightColorProps.get(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY));
			signature.set(AnimationPropertiesKeys.FONT_PROPERTY, sourcecodeHighlightColorProps.get(AnimationPropertiesKeys.FONT_PROPERTY));
			
			int xOffset = 0;
			int yOffset = 0;
			signatureConvolute = lang.newText(new Coordinates(20 + xOffset, 80 + yOffset), "private static int[][] convolute(int[][] src, int[][] kernel) { ... }", "signature_convolute", null, signature);
			scConvolute = lang.newSourceCode(new Coordinates(20 + xOffset, 64 + yOffset), "convolute", null, sourcecodeHighlightColorProps);
			scConvolute.addCodeLine("private static int[][] convolute(int[][] src, int[][] kernel) {", null, 0, null);
			scConvolute.addCodeLine("	int srcHeight = src.length;", null, 1, null);
			scConvolute.addCodeLine("	int srcWidth = src[0].length;", null, 1, null);
			scConvolute.addCodeLine("	int kernelSize = kernel.length;", null, 1, null);
			scConvolute.addCodeLine("	int[][] dst = new int[srcHeight][srcWidth];", null, 1, null);
			scConvolute.addCodeLine("	copyBorder(src, dst, kernelSize);", null, 1, null);
			scConvolute.addCodeLine("	for (int y = 1; y < srcHeight - 1; y++) {", null, 1, null);
			scConvolute.addCodeLine("		for (int x = 1; x < srcWidth - 1; x++) {", null, 2, null);
			scConvolute.addCodeLine("			dst[y][x] = applyFilter(src, kernel, kernelSize, y, x);", null, 3, null);
			scConvolute.addCodeLine("		}", null, 2, null);
			scConvolute.addCodeLine("	}", null, 1, null);
			scConvolute.addCodeLine("	return dst;", null, 1, null);
			scConvolute.addCodeLine("}", null, 0, null);
			scConvolute.hide();
			MethodManager.register(new Method(scConvolute, signatureConvolute, 220));

			signatureApplyFilter = lang.newText(new Coordinates(20 + xOffset, 110 + yOffset), "private static int applyFilter(int[][] src, int[][] kernel, int kernelSize, int y, int x) { ... }",
					"signature_applyFilter", null, signature);
			scApplyFilter = lang.newSourceCode(new Coordinates(20 + xOffset, 94 + yOffset), "applyFilter", null, sourcecodeHighlightColorProps);
			scApplyFilter.addCodeLine("private static int applyFilter(int[][] src, int[][] kernel, int kernelSize,", null, 0, null);
			scApplyFilter.addCodeLine("		int y, int x) {", null, 2, null);
			scApplyFilter.addCodeLine("	int borderSize = (int) Math.floor(kernelSize / 2d);", null, 1, null);
			scApplyFilter.addCodeLine("	int value = 0;", null, 1, null);
			scApplyFilter.addCodeLine("	for (int i = y - borderSize; i < y + borderSize + 1; i++) {", null, 1, null);
			scApplyFilter.addCodeLine("		for (int u = x - borderSize; u < x + borderSize + 1; u++) {", null, 2, null);
			scApplyFilter.addCodeLine("			value += (src[i][u]) * kernel[i - y + borderSize][u - x + borderSize];", null, 3, null);
			scApplyFilter.addCodeLine("		}", null, 2, null);
			scApplyFilter.addCodeLine("	}", null, 1, null);
			scApplyFilter.addCodeLine("	return (int) (value);", null, 1, null);
			scApplyFilter.addCodeLine("}", null, 0, null);
			scApplyFilter.hide();
			MethodManager.register(new Method(scApplyFilter, signatureApplyFilter, 170));

			signatureCopyBorder = lang.newText(new Coordinates(20 + xOffset, 140 + yOffset), "private static void copyBorder(int[][] src, int[][] dst, int kernelSize) { ... }",
					"signature_copyBorder", null, signature);
			scCopyBorder = lang.newSourceCode(new Coordinates(20 + xOffset, 124 + yOffset), "copyBorder", null, sourcecodeHighlightColorProps);
			scCopyBorder.addCodeLine("private static void copyBorder(int[][] src, int[][] dst, int kernelSize) {", null, 0, null);
			scCopyBorder.addCodeLine("	int borderSize = (int) Math.floor(kernelSize / 2d);", null, 1, null);
			scCopyBorder.addCodeLine("	// top border", null, 1, null);
			scCopyBorder.addCodeLine("	for (int y = 0; y < borderSize; y++) {", null, 1, null);
			scCopyBorder.addCodeLine("		for (int x = 0; x < src[y].length; x++) {", null, 2, null);
			scCopyBorder.addCodeLine("			dst[y][x] = src[y][x];", null, 3, null);
			scCopyBorder.addCodeLine("		}", null, 2, null);
			scCopyBorder.addCodeLine("	}", null, 1, null);
			scCopyBorder.addCodeLine("	// bottom border", null, 1, null);
			scCopyBorder.addCodeLine("	for (int y = src.length - 1; y > src.length - borderSize - 1; y--) {", null, 1, null);
			scCopyBorder.addCodeLine("		for (int x = 0; x < src[y].length; x++) {", null, 2, null);
			scCopyBorder.addCodeLine("			dst[y][x] = src[y][x];", null, 3, null);
			scCopyBorder.addCodeLine("		}", null, 2, null);
			scCopyBorder.addCodeLine("	}", null, 1, null);
			scCopyBorder.addCodeLine("	// left border", null, 1, null);
			scCopyBorder.addCodeLine("	for (int y = borderSize; y < src.length; y++) {", null, 1, null);
			scCopyBorder.addCodeLine("		for (int x = 0; x < borderSize; x++) {", null, 2, null);
			scCopyBorder.addCodeLine("			dst[y][x] = src[y][x];", null, 3, null);
			scCopyBorder.addCodeLine("		}", null, 2, null);
			scCopyBorder.addCodeLine("	}", null, 1, null);
			scCopyBorder.addCodeLine("	// right border", null, 1, null);
			scCopyBorder.addCodeLine("	for (int y = borderSize; y < src.length - borderSize; y++) {", null, 1, null);
			scCopyBorder.addCodeLine("		for (int x = src[y].length - 1; x > src[y].length - 1 - borderSize; x--) {", null, 2, null);
			scCopyBorder.addCodeLine("			dst[y][x] = src[y][x];", null, 3, null);
			scCopyBorder.addCodeLine("		}", null, 2, null);
			scCopyBorder.addCodeLine("	}", null, 1, null);
			scCopyBorder.addCodeLine("}", null, 0, null);
			scCopyBorder.hide();
			MethodManager.register(new Method(scCopyBorder, signatureCopyBorder, 100));

			lang.nextStep("Start");
		}

		private static class MethodManager {

			private static HashMap<SourceCode, Integer> highlights = new HashMap<>();

			private static List<Method> methods = new ArrayList<>();

			private static void register(Method method) {
				methods.add(method);
			}

			public static void highlight(SourceCode sourceCode, int lineNumber) {
				if (highlights.containsKey(sourceCode)) {
					sourceCode.unhighlight(highlights.get(sourceCode));
				}
				sourceCode.highlight(lineNumber);
				highlights.put(sourceCode, lineNumber);
			}

			public static void showMethod(SourceCode sourceCode) {
				int duration = 500;
				Method method = null;
				for (int i = 0; i < methods.size(); i++) {
					if (methods.get(i).sourceCode != sourceCode && method == null)
						continue;
					else if (method == null) {
						method = methods.get(i);
						continue;
					}
					methods.get(i).signature.moveBy("translate", 0, method.height, null, new MsTiming(duration));
					methods.get(i).sourceCode.moveBy("translate", 0, method.height, null, new MsTiming(duration));
				}
				method.sourceCode.show(new MsTiming(duration));
				method.signature.hide(new MsTiming(duration));
			}

			public static void hideMethod(SourceCode sourceCode) {
				int duration = 500;
				Method method = null;
				for (int i = 0; i < methods.size(); i++) {
					if (methods.get(i).sourceCode != sourceCode && method == null)
						continue;
					else if (method == null) {
						method = methods.get(i);
						continue;
					}
					methods.get(i).signature.moveBy("translate", 0, -1 * method.height, null, new MsTiming(duration));
					methods.get(i).sourceCode.moveBy("translate", 0, -1 * method.height, null, new MsTiming(duration));
				}
				method.sourceCode.hide();
				method.signature.show();
				method.signature.changeColor(null, Color.black, null, null);
			}
		}

		private class Method {
			private SourceCode sourceCode;
			private Primitive signature;
			private int height;

			private Method(SourceCode sourceCode, Primitive signature, int height) {
				this.sourceCode = sourceCode;
				this.signature = signature;
				this.height = height;
			}
		}

	}
}