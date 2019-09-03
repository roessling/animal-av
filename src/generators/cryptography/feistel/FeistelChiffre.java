package generators.cryptography.feistel;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;

/**
 * @author Moritz Kulessa <mori.k@web.de>
 * @version 1.0
 * @since 2014-07-01
 */
public class FeistelChiffre implements ValidatingGenerator {

	private Language lang;
	private Painter painter;

	private Expression fFunction;
	private Expression kFunction;

	private String fFunctionString;
	private String kFunctionString;
	private int n;
	private int blockSize;
	private int[] k0;

	public void init() {
		lang = new AnimalScript("Feistel-Chiffre Verschlüsselung [DE]",
				"Moritz Kulessa<mori.k@web.de>", 800, 600);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		int[] m = (int[]) primitives.get("message");
		fFunctionString = (String) primitives.get("f-Function");
		kFunctionString = (String) primitives.get("k-Function");
		n = (Integer) primitives.get("n");
		blockSize = (Integer) primitives.get("blockSize");
		k0 = (int[]) primitives.get("k0");

		init();

		lang.setStepMode(true);

		painter = new Painter(lang, props);

		long kModulo = (1 << k0.length);
		long fModulo = (1 << (blockSize / 2));

		JexlEngine jexl = new JexlEngine();
		fFunction = jexl.createExpression(fFunctionString);
		kFunction = jexl.createExpression(kFunctionString);

		fFunctionString += " mod " + fModulo;
		kFunctionString += " mod " + kModulo;

		// Arraygröße anpassen
		int[] temp = m;
		int fill = (blockSize - (m.length % blockSize)) % blockSize;
		m = new int[m.length + fill];
		System.arraycopy(temp, 0, m, fill, temp.length);
		for (int i = 0; i < fill; i++) {
			m[i] = 0;
		}

		painter.paintStartScreen();
		lang.nextStep("Zeige Einleitung an.");
		painter.paintWorkScreen(arrayToString(m), kFunctionString,
				fFunctionString, n);
		lang.nextStep("Zeige Visualisierung des Algorithmus an.");
		encryptFunction(m, k0, n, blockSize);
		painter.paintEndScreen();

		lang.finalizeGeneration();

		return lang.toString();
	}

	public String getName() {
		return "Feistel-Chiffre Verschlüsselung [DE]";
	}

	public String getAlgorithmName() {
		return "Feistel-Chiffre";
	}

	public String getAnimationAuthor() {
		return "Moritz Kulessa";
	}

	public String getDescription() {
		return "Die Feistel-Chiffre ist ein Chriffriersystem, bei dem der Klartext blockweise verschlüsselt wird."
				+ "\n"
				+ "Die Klartextblöcke werden vor der Verschlüsselung erst nochmal in eine linke und eine rechte "
				+ "\n"
				+ "Blockhälfte aufgeteilt. Die neu berechneten Blockhälfte werden nach einer Runde wie folgt bestimmt:"
				+ "\n"
				+ "\n"
				+ "Li+1 = Ri"
				+ "\n"
				+ "Ri+1 = F(Ri,Ki) xor Li		(F = nicht lineare Funktion)"
				+ "\n"
				+ "\n"
				+ "Dieser Algorithmus wird n Runden lang wiederholt, bis man schließlich das Chiffrat des Blockes erhält,"
				+ "\n"
				+ "indem man einfach den letzten berechneten linken Blockteil mit dem letzten berechneten rechten"
				+ "\n"
				+ "Blockteil konkateniert. Dies wird solange wiederholt, bis alle Klartextblöcke verschlüsselt sind.";
	}

	public String getCodeExample() {
		return "for each block do" + "\n" + "	L0 = linker Blockteil;" + "\n"
				+ "	R0 = rechter Blockteil;" + "\n" + "	for i = 0,...,n do"
				+ "\n" + "		Ki = (i==0) ? K0 : k(Ki);" + "\n" + "		Li = Ri;"
				+ "\n" + "		Ri = f(Ri, ki) xor Li;" + "\n" + "	end" + "\n"
				+ "	cj = Li ° Ri;" + "\n" + "end" + "\n"
				+ "c = c1 ° c2 ° ... ° cj;";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	/**
	 * funktion zur schlüsselberechnung
	 * 
	 * @param k
	 * @return
	 */
	private int[] computeNextKey(int[] k) {

		JexlContext context = new MapContext();
		context.set("k", arr2num(k));

		long result = (long) kFunction.evaluate(context);

		return num2arr(result, k.length);
	}

	/**
	 * Funktion f
	 * 
	 * @param r
	 * @param k
	 * @return
	 */
	private int[] f(int[] r, int[] k) {

		JexlContext context = new MapContext();
		context.set("r", arr2num(r));
		context.set("k", arr2num(k));

		long result = (long) fFunction.evaluate(context);

		return num2arr(result, r.length);
	}

	private int[] encryptFunction(int[] m, int[] k0, int n, int blockSize) {

		int currentArrayPosition = 0;
		int[] encryptMsg = new int[m.length];
		int[] currentBlock = new int[blockSize];
		int[] currentL = new int[blockSize / 2];
		int[] currentR = new int[blockSize / 2];
		int[] ki = k0;
		int[] temp;
		int[] f;
		int[] currentChiff = new int[blockSize];
		int numberOfBlock = 1;

		String encryptMsgString = "";

		while (currentArrayPosition < m.length) {

			System.arraycopy(m, currentArrayPosition, currentBlock, 0,
					blockSize);

			painter.highlightSourceCode(8, 0);
			painter.highlightSourceCode(0, 0);
			painter.setCurrentBlock(arrayToString(currentBlock));
			painter.removeValues(0);
			lang.nextStep("Bestimme nächsten Block");
			painter.removeHighlight();

			// hole L und R
			System.arraycopy(m, currentArrayPosition, currentL, 0,
					currentL.length);
			System.arraycopy(m, currentArrayPosition + currentL.length,
					currentR, 0, currentR.length);

			painter.highlightSourceCode(0, 1);
			painter.highlightSourceCode(2, 2);
			painter.setLi(0, arrayToString(currentL));
			painter.setRi(0, arrayToString(currentR));
			painter.addProtocolL(0, arrayToString(currentL));
			painter.addProtocolR(0, arrayToString(currentR));
			lang.nextStep("Bestimme L und R");
			painter.removeHighlight();
			painter.unhighlightProtocol(0);

			for (int i = 0; i < n; i++) {

				painter.highlightSourceCode(6, 3);
				painter.highlightSourceCode(1, 3);
				painter.highlightSourceCode(2, 3);
				painter.removeValues(i);
				painter.setLi(i, arrayToString(currentL));
				painter.setRi(i, arrayToString(currentR));
				if (i == 0) {
					painter.removeHighlight();
				}
				lang.nextStep("Führe Algorithmus für " + numberOfBlock
						+ " Block aus. Runde: " + (i + 1) + "/" + n);
				painter.removeHighlight();

				ki = (i == 0) ? k0 : computeNextKey(ki);

				painter.highlightSourceCode(3, 4);
				painter.setKi(i, arrayToString(ki));
				lang.nextStep();
				painter.removeHighlight();

				temp = currentL;
				currentL = currentR;

				painter.highlightSourceCode(4, 5);
				painter.highlightPathForResultL();
				painter.setLi1(i + 1, arrayToString(currentL));
				painter.addProtocolL(i + 1, arrayToString(currentL));
				lang.nextStep();
				painter.removeHighlight();
				painter.unhighlightProtocol(i + 1);

				painter.highlightSourceCode(5, 6);
				painter.highlightPathForF();
				lang.nextStep();
				painter.removeHighlight();

				f = f(currentR, ki);
				painter.highlightF();
				painter.setF(i, arrayToString(f));
				lang.nextStep();
				painter.removeHighlight();

				painter.highlightPathForXor();
				lang.nextStep();
				painter.removeHighlight();

				currentR = xor(f, temp);

				painter.highlightXor();
				painter.highlightPathForResultR();
				painter.setRi1(i + 1, arrayToString(currentR));
				painter.addProtocolR(i + 1, arrayToString(currentR));
				lang.nextStep();
				painter.removeHighlight();
				painter.unhighlightProtocol(i + 1);
			}

			System.arraycopy(currentL, 0, currentChiff, 0, currentL.length);
			System.arraycopy(currentR, 0, currentChiff, currentR.length,
					currentR.length);

			painter.highlightSourceCode(6, 8);
			painter.setCurrentChiff(arrayToString(currentChiff));
			lang.nextStep();
			painter.removeHighlight();

			encryptMsgString = encryptMsgString + arrayToString(currentChiff);
			System.arraycopy(currentChiff, 0, encryptMsg, currentArrayPosition,
					currentChiff.length);

			painter.unHighlightSourceCode(8);
			painter.setChiffM(encryptMsgString);
			lang.nextStep("Falls noch ein Block vorhanden ist, führe Algorithmus für nächsten Block aus. Ansonsten zeige End-Screen an.");
			painter.removeHighlight();
			painter.removeValuesProtocol(n);
			painter.removeTextCurrentChiff();

			currentArrayPosition += blockSize;
			numberOfBlock++;
		}

		return encryptMsg;
	}

	/**
	 * This method prints the array
	 * 
	 * @param array
	 * @return a string representation of the array
	 */
	private String arrayToString(int[] array) {

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < array.length; i++) {
			sb.append(array[i]);
		}
		return sb.toString();
	}

	/**
	 * This method transforms an array of bits into a number
	 * 
	 * @param arr
	 *            , the array with bits
	 * @return the number illustrated through the bits
	 */
	public static long arr2num(int[] arr) {
		long result = 0;
		for (int i = arr.length - 1; i >= 0; i--) {
			assert (arr[i] == 0 || arr[i] == 1);
			if (arr[i] == 1) {
				result += Math.pow(2, arr.length - i - 1);
			}
		}
		return result;
	}

	/**
	 * This method transforms a number into an array of bits with the given
	 * length
	 * 
	 * @param num
	 *            , the number
	 * @param length
	 *            , the length of the array
	 * @return an array of bits
	 */
	public static int[] num2arr(long num, int length) {

		int[] result = new int[length];
		int position = length - 1;
		long num2 = num;
		while (num2 != 0 && position >= 0) {
			if ((num2 & 1) == 1) {
				result[position] = 1;
			} else {
				result[position] = 0;
			}
			num2 >>= 1;
			position -= 1;
		}

		return result;
	}

	/**
	 * This method returns the exclusive-or of bit1 with bit2
	 * 
	 * @param bit1
	 *            , one bit
	 * @param bit2
	 *            , one bit
	 * @return (bit1 xor bit2)
	 */
	public static int xor(int bit1, int bit2) {
		assert (bit1 == 0 || bit1 == 1);
		assert (bit2 == 0 || bit2 == 1);
		return !(bit1 == bit2) ? 1 : 0;
	}

	/**
	 * This method returns the bitwise exclusive-or of the arrays bitStr1 and
	 * bitStr2
	 * 
	 * @param bitStr1
	 *            , array of bits
	 * @param bitStr2
	 *            , array of bits
	 * @return (bitStr1[i] xor bitStr2[i])
	 */
	public static int[] xor(int[] bitStr1, int[] bitStr2) {
		int[] result = new int[bitStr1.length];
		for (int i = 0; i < bitStr1.length; i++) {
			result[i] = xor(bitStr1[i], bitStr2[i]);
		}
		return result;
	}

	public static void main(String[] args) {

		FeistelChiffre c = new FeistelChiffre();

		Hashtable<String, Object> primitives = new Hashtable<String, Object>();
		primitives.put("message", new int[] { 1, 0, 1, 1, 0, 1, 0, 0 });
		primitives.put("k0", new int[] { 1, 0 });
		primitives.put("n", 5);
		primitives.put("blockSize", 4);
		primitives.put("f-Function", "r*r + k*k");
		primitives.put("k-Function", "k+1");

		AnimationPropertiesContainer props = new AnimationPropertiesContainer();

		TextProperties text1 = new TextProperties("defaultText");
		text1.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.CYAN);
		props.add(text1);
		TextProperties text2 = new TextProperties("headlineText");
		text2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		props.add(text2);
		TextProperties text3 = new TextProperties("boldText");
		text3.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
		props.add(text3);
		PolylineProperties polyline = new PolylineProperties("polyline");
		polyline.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		props.add(polyline);
		MatrixProperties matrix = new MatrixProperties("matrix");
		props.add(matrix);
		SourceCodeProperties sourceCode = new SourceCodeProperties("sourceCode");
		props.add(sourceCode);

		// c.validateInput(props, primitives);

		System.out.println(c.generate(props, primitives));

	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		boolean error = false;

		int blockSize = (int) primitives.get("blockSize");
		fFunctionString = (String) primitives.get("f-Function");
		kFunctionString = (String) primitives.get("k-Function");

		if ((blockSize % 2) == 1) {
			error = true;
			JOptionPane.showMessageDialog(JOptionPane.getRootFrame(),
					"Blockgröße ist nicht durch 2 teilbar!", "Fehler",
					JOptionPane.ERROR_MESSAGE);
		}

		JexlContext fContext = new MapContext();
		fContext.set("r", 1);
		fContext.set("k", 1);

		JexlContext kContext = new MapContext();
		kContext.set("k", 1);

		JexlEngine jexl = new JexlEngine();

		try {
			fFunction = jexl.createExpression(fFunctionString);
		} catch (Exception e) {
			error = true;
			JOptionPane
					.showMessageDialog(
							JOptionPane.getRootFrame(),
							"Funktion f konnte nicht evaluiert werden! Bitte benutzen sie eine andere Funktion!",
							"Fehler", JOptionPane.ERROR_MESSAGE);
		}

		try {
			kFunction = jexl.createExpression(kFunctionString);
		} catch (Exception e) {
			error = true;
			JOptionPane
					.showMessageDialog(
							JOptionPane.getRootFrame(),
							"Funktion k konnte nicht evaluiert werden! Bitte benutzen sie eine andere Funktion ein!",
							"Fehler", JOptionPane.ERROR_MESSAGE);
		}

		return !error;
	}
}
