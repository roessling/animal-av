/*
 * MD5.java
 * Bekir Bayrak, Halit Ciftci, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.hashing;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.OffsetFromLastPosition;
import algoanim.util.TicksTiming;

public class MD4 implements Generator {
    private Language lang;
    private String Message;
    private int AnimatedRounds;
    
    // Konstante Shift Werte für jeden Durchlauf
 	private static final int[] SHIFT_AMTS = { 
 		3, 7, 11, 19, 3, 7, 11, 19, 3, 7, 11, 19, 3, 7, 11, 19, 
 		3, 5,  9, 13, 3, 5,  9, 13, 3, 5,  9, 13, 3, 5,  9, 13, 
 		3, 9, 11, 15, 3, 9, 11, 15, 3, 9, 11, 15, 3, 9, 11, 15 };

 	private int a0 = 0x67452301;
 	private int b0 = 0xEFCDAB89;
 	private int c0 = 0x98BADCFE;
 	private int d0 = 0x10325476;
 	
 	private String[][] messageInput;
 	private StringMatrix inputMatrix;
    
    private TextProperties bold14Props;
	private TextProperties bold16Props;
	private TextProperties plain12Props;
	
	private SourceCodeProperties scProps;
	private PolylineProperties lineProps;
	
	private RectProperties funcRectProps;
	private RectProperties addRectProps;
	private RectProperties functionRectProps;
	private RectProperties shiftRectProps;
	
	private SourceCode code;
	
	/**** MD5 ****/
	private Text descHeader;
	private Text tBlock;
	private Text tRunde;
	private Text fAI;
	private Text fBI;
	private Text fCI;
	private Text fDI;
	
	private Text fA0I;
	private Text fB0I;
	private Text fC0I;
	private Text fD0I;
	
	private Text fFI;
	private Text fM;
	private Text fK;
	private Text fS;
	private Text fTemp;
	
	private Text a1Result;
	private Text a2Result;
	private Text a3Result;
	
	private Text fAzI;
	private Text fBzI;
	private Text fCzI;
	private Text fDzI;
	
	private Polyline moveLineM;
	
	private Polyline moveLineA0;
	private Polyline moveLineB0;
	private Polyline moveLineC0;
	private Polyline moveLineD0;
	private Polyline moveLineA0Z;
	private Polyline moveLineB0Z;
	private Polyline moveLineC0Z;
	private Polyline moveLineD0Z;
	
	private Polyline moveLineA;
	private Polyline moveLineB;
	private Polyline moveLineC;
	private Polyline moveLineD;
	private Polyline moveLineAZ;
	private Polyline moveLineBZ;
	private Polyline moveLineCZ;
	private Polyline moveLineDZ;
	
	private List<Primitive> roundPrimitives; // Primitives needed per round
	private List<Primitive> blockPrimitives; // Primitives needed per block
	private List<Primitive> md5Primitives; 	 // Primitives needed for the rest
	
	private byte[] total;

    public void init(){
        lang = new AnimalScript("MD4", "Bekir Bayrak, Halit Ciftci", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        Message = (String)primitives.get("Message");//Message = "hello world hello world hello world hello world hello world";
        AnimatedRounds = (Integer)primitives.get("AnimatedRounds");
        funcRectProps = (RectProperties)props.getPropertiesByName("wordRects");
        addRectProps = (RectProperties)props.getPropertiesByName("additionRects");
        shiftRectProps = (RectProperties)props.getPropertiesByName("shiftRect");
        functionRectProps = (RectProperties)props.getPropertiesByName("functionRect");
        bold14Props = (TextProperties)props.getPropertiesByName("text");
        
        roundPrimitives = new ArrayList<Primitive>();
        blockPrimitives = new ArrayList<Primitive>();
        md5Primitives = new ArrayList<Primitive>();
        
        byte[] messageBytes = Message.getBytes();
		int messageLenBytes = messageBytes.length; // Länge der Nachricht
		int numBlocks = ((messageLenBytes + 9) / 64) + 1; // Anzahl Blöcke wenn man 1byte Padding + 8byte NachrichtenLänge berücksichtigt
		int totalLen = numBlocks * 64; // Anzahl Bytes für alle Blöcke
		total = new byte[totalLen];
		System.arraycopy(messageBytes, 0, total, 0, messageLenBytes);
		
		
		TextProperties headerProps = new TextProperties();
	    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
	        Font.SANS_SERIF, Font.BOLD, 24));
	    /********** Bold 16 **********/
	    bold16Props = new TextProperties();
	    bold16Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
	        Font.SANS_SERIF, Font.BOLD, 16));
	    /********** Plain 12 **********/
	    plain12Props = new TextProperties();
	    plain12Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
	        Font.SANS_SERIF, Font.PLAIN, 12));
	    
	    lang.newText(new Coordinates(20, 30), "MD4-Algorithmus", "headerTitle", null, headerProps);
	    
	    RectProperties rectProps = new RectProperties();
	    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
	    lang.newRect(new Offset(-5, -5, "headerTitle", AnimalScript.DIRECTION_NW), new Offset(5, 5, "headerTitle", "SE"), "titleRect", null, rectProps);
	    
	    TextProperties tHeaderProps = new TextProperties();
	    tHeaderProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
	        Font.SANS_SERIF, Font.PLAIN, 16));
	    descHeader = lang.newText(new Offset(0, 10, "titleRect", "SW"), "Beschreibung des Algorithmus",
		        "descHeader", null, tHeaderProps);
	    
	    lineProps = new PolylineProperties();
 		lineProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
 		lineProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
	    
		scProps = (SourceCodeProperties)props.getPropertiesByName("sourceCode");

 		SourceCode description = lang.newSourceCode(new Offset(0, 10, "descHeader", "SW"), "description", null, scProps);
 		description.addCodeLine("Der MD4-Algorithmus (Message-Digest Algorithm 4) ist eine kryptologische Hashfunktion,", null, 0, null);
 		description.addCodeLine("die 1990 von Ronald L. Rivest entwickelt wurde.", null, 0, null);
 		description.addCodeLine("", null, 0, null); // 3
 		description.addCodeLine("Ziel war es unter anderem einen Algorithmus zu entwickeln, der leicht zu implementieren ", null, 0, null); // 4
 		description.addCodeLine("ist und schnell auf 32bit Rechnern läuft.", null, 0, null); // 5
 		description.addCodeLine("", null, 0, null); // 6
 		description.addCodeLine("Der Algorithmus erzeugt aus einem Eingabestring einen 128bit langen Hash.", null, 0, null); // 7
 		description.addCodeLine("", null, 0, null); // 6
 		description.addCodeLine("Die Schritte des Algorithmus im Überblick:", null, 0, null); // 6
 		description.addCodeLine("1) Vorbereitung", null, 1, null); // 6
 		description.addCodeLine("2) Kompression", null, 1, null); // 6
 		description.addCodeLine("3) Ergebnis & Fazit", null, 1, null); // 6

	    lang.nextStep("Einführung");
	    /********************************* NEXT STEP *********************************/
	    
	    description.hide();
	    
	    SourceCode description1 = lang.newSourceCode(new Offset(0, 10, "descHeader", "SW"), "description1", null, scProps);
 		description1.addCodeLine("MD4 teilt die Eingabe in Blöcke von je 512 Bit.", null, 0, null);
 		description1.addCodeLine("Jeder Block wird in 3 Runden mit jeweils 16 Operationen behandelt.", null, 0, null);
 		description1.addCodeLine("", null, 0, null);
 		description1.addCodeLine("Der Algorithmus benutzt 3 nichtlineare Funktionen, die in den jeweiligen Runden eingesetzt werden.", null, 0, null);
 		description1.addCodeLine("Diese Funktionen sind in der", null, 0, null);
 		description1.addCodeLine("1. Runde", null, 1, null);
 		description1.addCodeLine("F(X, Y, Z) = (X AND Y) OR (NOT X AND Z)", null, 2, null);
 		description1.addCodeLine("2. Runde", null, 1, null);
 		description1.addCodeLine("G(X, Y, Z) = (X AND Y) OR (X AND Z) OR (Y AND Z)", null, 2, null);
 		description1.addCodeLine("3. Runde", null, 1, null);
 		description1.addCodeLine("H(X, Y, Z) = X XOR Y XOR Z", null, 2, null);
 		
 		lang.nextStep();
	    /********************************* NEXT STEP *********************************/
 		
 		description1.hide();
 		descHeader.setText("Vorhandene Elemente", null, null);
 		
 		SourceCode description2 = lang.newSourceCode(new Offset(0, 10, "descHeader", "SW"), "description2", null, scProps);
 		description2.addCodeLine("1. Konstanten K:", null, 0, null);
 		description2.addCodeLine("Jede Runde wird zum Zwischenergebnis ein konstanter Wert dazuaddiert.", null, 1, null);
 		description2.addCodeLine("", null, 0, null);
 		description2.addCodeLine("1. Runde", null, 1, null);
 		description2.addCodeLine("K = 0x00000000", null, 2, null);
 		description2.addCodeLine("2. Runde", null, 1, null);
 		description2.addCodeLine("K = 0x6ED9EBA1", null, 2, null);
 		description2.addCodeLine("3. Runde", null, 1, null);
 		description2.addCodeLine("K = 0x5A827999", null, 2, null);
 		description2.addCodeLine("", null, 0, null);
 		description2.addCodeLine("2. Shift Array S:", null, 0, null);
 		description2.addCodeLine("Zusätzlich wird jede Runde das zwischenergebnis um s Einheiten nach links geschoben.", null, 1, null);
 		description2.addCodeLine("Diese vom Algorithmus vorgegebene Zahlenfolge bestimmt dabei, um wieviel Stellen geschoben wird.", null, 1, null);
 		description2.addCodeLine("", null, 0, null);
 		description2.addCodeLine("S[]= {", null, 1, null);
 		description2.addCodeLine("3, 7, 11, 19, 3, 7, 11, 19, 3, 7, 11, 19, 3, 7, 11, 19,", null, 2, null);
 		description2.addCodeLine("3, 5,  9, 13, 3, 5,  9, 13, 3, 5,  9, 13, 3, 5,  9, 13,", null, 2, null);
 		description2.addCodeLine("3, 9, 11, 15, 3, 9, 11, 15, 3, 9, 11, 15, 3, 9, 11, 15}", null, 2, null);
 		description2.addCodeLine("", null, 0, null);
 		description2.addCodeLine("3. Variablen a0, b0, c0, d0:", null, 0, null);
 		description2.addCodeLine("Werden mit konstanten Werten initialisiert.", null, 1, null);
 		description2.addCodeLine("a0 = 0x67452301;", null, 1, null);
 		description2.addCodeLine("b0 = 0xEFCDAB89;", null, 1, null);
 		description2.addCodeLine("c0 = 0x98BADCFE;", null, 1, null);
 		description2.addCodeLine("d0 = 0x10325476;", null, 1, null);
 		
 		lang.nextStep();
	    /********************************* NEXT STEP *********************************/
 		description2.hide();
 		
 		SourceCode description21 = lang.newSourceCode(new Offset(0, 10, "descHeader", "SW"), "description21", null, scProps);
 		description21.addCodeLine("4. Hilfsmethode copyBlockLittleEndian:", null, 0, null);
 		description21.addCodeLine("Unterteilt aktuellen Block in 16 32-bit little-endian Worte", null, 1, null);
 		description21.addCodeLine("", null, 1, null);
 		description21.addCodeLine("int[] copyBlockLittleEndian(byte[] blocks, int blockCount)", null, 1, null);
 		description21.addCodeLine("int[] result = new int[16];", null, 2, null);
 		description21.addCodeLine("int index = blockCount << 6;", null, 2, null);
 		description21.addCodeLine("for (int i = 0; i < 16; i++, index = index + 4) {", null, 2, null);
 		description21.addCodeLine("result[i] =", null, 3, null);
 		description21.addCodeLine("(blocks[index] & 0xFF) |", null, 4, null);
 		description21.addCodeLine("((blocks[index + 1] & 0xFF) <<  8) |", null, 4, null);
 		description21.addCodeLine("((blocks[index + 2] & 0xFF) << 16) |", null, 4, null);
 		description21.addCodeLine("((blocks[index + 3] & 0xFF) << 24)", null, 4, null);
 		description21.addCodeLine("}", null, 2, null);
 		
 		lang.nextStep();
	    /********************************* NEXT STEP *********************************/
 		description21.hide();
 		
 		descHeader.setText("Schritt 1: Vorbereitung", null, null);
 		SourceCode description3 = lang.newSourceCode(new Offset(0, 10, "descHeader", "SW"),
 				"description3", null, scProps);
 		description3.addCodeLine("Die Nachricht wird in aufeinander folgenden 512-Bit-Blöcken verarbeitet.", null, 0, null);
 		description3.addCodeLine("An die Nachricht wird zunächst eine 1, Padding mit Nullen und zuletzt die Originallänge der Nachricht angehängt.", null, 0, null);
 		description3.addCodeLine("Insgesamt muss die Länge der Nachricht plus Padding plus 32 Bit Nachrichtenlänge durch 512 Bit teilbar sein.", null, 0, null);
 		description3.addCodeLine("", null, 0, null);
 		description3.addCodeLine("", null, 0, null);
 		description3.addCodeLine("byte[] appendPaddingAndLength(byte[] message) {", null, 0, null);
 		description3.addCodeLine("int messageLength = message.length;", null, 1, null);
 		description3.addCodeLine("int numBlocks = ((messageLength + 9) / 64) + 1;     // Anzahl Blöcke mit 1byte Padding + 8byte Nachrichtenlänge", null, 1, null);
 		description3.addCodeLine("int totalLength = numBlocks * 64;", null, 1, null);
 		description3.addCodeLine("byte[] result = new byte[totalLength];", null, 1, null);
 		description3.addCodeLine("System.arraycopy(message, 0, result, 0, messageLength);", null, 1, null);
 		description3.addCodeLine("", null, 1, null);
 		description3.addCodeLine("result[messageLength] = (byte) 0x80;", null, 1, null);
 		description3.addCodeLine("long messageLengthBits = (long) messageLength << 3;", null, 1, null);
 		description3.addCodeLine("", null, 1, null);
 		description3.addCodeLine("for (int i = 0; i < 8; i++) {", null, 1, null);
 		description3.addCodeLine("result[totalLength - 8 + i] = (byte) messageLengthBits;", null, 2, null);
 		description3.addCodeLine("messageLengthBits >>>= 8;", null, 2, null);
 		description3.addCodeLine("}", null, 1, null);
 		description3.addCodeLine("return result;", null, 1, null);
 		description3.addCodeLine("}", null, 0, null);
 		
 		lang.nextStep("1. Vorbereitung");
	    /********************************* NEXT STEP *********************************/
 		
 		description3.highlight(5);
 		Text mInput = lang.newText(new Coordinates(700, 20), "Eingabestring: '"+Message+"'", "mInput", null, bold14Props);
 		
 		lang.nextStep();
	    /********************************* NEXT STEP *********************************/
 		
 		description3.unhighlight(5);
 		description3.highlight(6);
 		Text mLength = lang.newText(new Offset(0, 5, "mInput", "SW"), "MessageLength: "+messageLenBytes+" Bytes", "mLength", null, bold14Props);
 		
 		lang.nextStep();
	    /********************************* NEXT STEP *********************************/
 		
 		description3.unhighlight(6);
 		description3.highlight(7);
 		Text mBlocks = lang.newText(new Offset(0, 5, "mLength", "SW"), "NumBlocks: "+numBlocks, "mBlocks", null, bold14Props);
 		
 		lang.nextStep();
	    /********************************* NEXT STEP *********************************/
 		
 		description3.unhighlight(7);
 		description3.highlight(8);
 		Text mTLength = lang.newText(new Offset(0, 5, "mBlocks", "SW"), "TotalLength: "+totalLen+" Bytes", "mTLength", null, bold14Props);
 		
 		lang.nextStep();
	    /********************************* NEXT STEP *********************************/
 		
 		description3.unhighlight(8);
 		description3.highlight(9);
 		Text mResult = lang.newText(new Offset(0, 5, "mTLength", "SW"), "Result", "mResult", null, bold14Props);
 		
 		messageInput = new String[8*numBlocks][8];
 		fillArray(messageInput, "00");
 		
 		MatrixProperties matrixProps = new MatrixProperties();
 		matrixProps.set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, 20);
 		matrixProps.set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, 20);
 		inputMatrix = lang.newStringMatrix(new Offset(0, 5, "mResult", "SW"), messageInput, "inputMatrix", null, matrixProps);
 		moveLineM = lang.newPolyline(new Node[]{ new Offset(0, 0, "inputMatrix", "NW"), new Offset(100, 0, "inputMatrix", "NE") }, "moveLineM", null, lineProps);
 		moveLineM.hide();
 		
 		lang.nextStep();
	    /********************************* NEXT STEP *********************************/
 		
 		description3.unhighlight(9);
 		description3.highlight(10);
 		int count = 0, i = 0, j = 0;
 		for (i = 0; i < (messageBytes.length / 8 + 1); i++) {
			for (j = 0; j < 8; j++) {
				if (count < messageBytes.length) {
					String c = String.format("%02X", messageBytes[count] & 0xFF);
					inputMatrix.put(i, j, c, null, null);
					inputMatrix.highlightElem(i, j, null, null);
					messageInput[i][j] = c;
					count++;
				} else {
					break;
				}
			}
			if (count >= messageBytes.length)
				break;
		}
 		
 		lang.nextStep();
	    /********************************* NEXT STEP *********************************/
 		
 		for (int i0 = 0; i0 <= i; i0++) {
 			for (int j0 = 0; j0 < ((i0 == i) ? j : 8); j0++) {
 				inputMatrix.unhighlightElem(i0, j0, null, null);
 	 		}
 		}
 		
 		description3.unhighlight(10);
 		description3.highlight(12);
 		inputMatrix.put(messageBytes.length / 8, messageBytes.length % 8, "80", null, null);
 		inputMatrix.highlightElem(messageBytes.length / 8, messageBytes.length % 8, null, null);
 		
 		total[messageLenBytes] = (byte) 0x80; // Füge eine 1 Hinzu
 		
 		lang.nextStep();
	    /********************************* NEXT STEP *********************************/
 		
 		long messageLenBits = (long) messageBytes.length << 3;
 		
 		description3.unhighlight(12);
 		description3.highlight(13);
 		inputMatrix.unhighlightElem(messageBytes.length / 8, messageBytes.length % 8, null, null);
 		Text mLengthBits = lang.newText(new Offset(20, 0, "mInput", "NE"), "MessageLengthBits: "+messageLenBits+" Bits",
 		        "mLengthBits", null, bold14Props);
 		
 		lang.nextStep();
	    /********************************* NEXT STEP *********************************/
 		
 		description3.unhighlight(13);
 		description3.highlight(15);
 		
 		lang.nextStep();
	    /********************************* NEXT STEP *********************************/
 		
 		for (i = 0; i < 8; i++) // Die letzten 8 Byte enthalten die Nachrichtenlänge (little endian)
		{
 			description3.unhighlight(15);
 	 		description3.highlight(16);
 			
 	 		total[totalLen - 8 + i] = (byte) messageLenBits;
			inputMatrix.put((numBlocks * 8)-1, i, String.format("%02X", ((byte) messageLenBits) & 0xFF), null, null);
			inputMatrix.highlightElem((numBlocks * 8)-1, i, null, null);
			messageLenBits >>>= 8;
 		
 			lang.nextStep();
 			
 			description3.unhighlight(16);
 	 		description3.highlight(17);
 			inputMatrix.unhighlightElem((numBlocks * 8)-1, i, null, null);
 			mLengthBits.setText("MessageLengthBits: "+messageLenBits, null, null);
 			mLengthBits.changeColor(null, Color.red, null, null);
 			
 			lang.nextStep();
 			
 			mLengthBits.changeColor(null, (Color)bold14Props.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
 			description3.unhighlight(17);
 	 		description3.highlight(15);
 	 		lang.nextStep();
		}
 		
 		description3.unhighlight(15);
	 	description3.highlight(19);
 		lang.nextStep();
	    /********************************* NEXT STEP *********************************/
 		
 		mInput.hide();
 		mLength.hide();
 		mBlocks.hide();
 		mTLength.hide();
 		mResult.hide();
 		mLengthBits.hide();
 		description3.hide();
 		descHeader.setText("Schritt 2: Kompression", null, null);
 		
 		buildMD5Visuals();
 		startMD5Calculation();
        
        return lang.toString();
    }
    
    public void buildMD5Visuals() {
		code = lang.newSourceCode(new Offset(0, 5, "descHeader", "SW"), "code", null, scProps);
 		code.addCodeLine("public byte[] computeMD5(byte[] gesamt) {", null, 0, null);
 		code.addCodeLine("int numBlocks = gesamt.length / 64;", null, 1, null);
 		code.addCodeLine("int[] M = new int[16];", null, 1, null);
 		code.addCodeLine("", null, 1, null);
 		code.addCodeLine("for (int i = 0; i < numBlocks; i++){", null, 1, null);
 		code.addCodeLine("M = copyBlockLittleEndian(gesamt, i);", null, 2, null);
 		code.addCodeLine("int A = a0;", null, 2, null);
 		code.addCodeLine("int B = b0;", null, 2, null);
 		code.addCodeLine("int C = c0;", null, 2, null);
 		code.addCodeLine("int D = d0;", null, 2, null);
 		code.addCodeLine("", null, 2, null);
 		code.addCodeLine("for (int j = 0; j < 48; j++) {", null, 2, null);
 		code.addCodeLine("int F = 0, K = 0, g = j;", null, 3, null);
 		code.addCodeLine("", null, 3, null);
 		code.addCodeLine("if (0 <= j && j <= 15) {", null, 3, null);
 		code.addCodeLine("F = (B & C) | (~B & D);", null, 4, null);
 		code.addCodeLine("} else if (16 <= j && j <= 31) {", null, 3, null);
 		code.addCodeLine("F = (B & D) | (C & ~D);", null, 4, null);
 		code.addCodeLine("K = 0x5A827999;", null, 4, null);
 		code.addCodeLine("g = ((j - 16) / 4) + 4 * (j % 4);", null, 4, null);
 		code.addCodeLine("} else if (32 <= j && j <= 47) {", null, 3, null);
 		code.addCodeLine("F = B ^ C ^ D;", null, 4, null);
 		code.addCodeLine("K = 0x6ED9EBA1;", null, 4, null);
 		code.addCodeLine("int r = j - 32;", null, 4, null);
 		code.addCodeLine("int p = (r % 4 == 0) ? 0 : (r % 4 == 1) ? 8 : (r % 4 == 2) ? 4 : 12;", null, 4, null);
 		code.addCodeLine("r /= 4;", null, 4, null);
 		code.addCodeLine("r = (r == 0) ? 0 : (r == 1) ? 2 : (r == 2) ? 1 : 3;", null, 4, null);
 		code.addCodeLine("g =  r + p;", null, 4, null);
 		code.addCodeLine("}", null, 3, null);
 		code.addCodeLine("int temp = Integer.rotateLeft(A + F + M[g] + K, S[j]);", null, 3, null);
 		code.addCodeLine("A = D;", null, 3, null);
 		code.addCodeLine("D = C;", null, 3, null);
 		code.addCodeLine("C = B;", null, 3, null);
 		code.addCodeLine("B = temp;", null, 3, null);
 		code.addCodeLine("}", null, 2, null);
 		code.addCodeLine("a0 += A;", null, 2, null);
 		code.addCodeLine("b0 += B;", null, 2, null);
 		code.addCodeLine("c0 += C;", null, 2, null);
 		code.addCodeLine("d0 += D;", null, 2, null);
 		code.addCodeLine("}", null, 1, null);
 		code.addCodeLine("return concatenateABCD(a0, b0, c0, d0);", null, 1, null);
 		code.addCodeLine("}", null, 0, null);
 		
 		inputMatrix.moveVia(null, "translate", moveLineM, null, new TicksTiming(200));
 		
 		lang.nextStep();
	    /********************************* NEXT STEP *********************************/
 		
 		tBlock = lang.newText(new Coordinates(500, 20), "Block: 0 / 0", "tBlock", null, bold14Props);
 		tRunde = lang.newText(new Offset(0, 5, "tBlock", "SW"), "Runde: 0 / 48", "tRunde", null, bold14Props);
 		//md5Primitives.add(lang.newText(new Offset(0, 0, "inputMatrix", "NW"), "Message Array", "tMessageArray", null, bold14Props));
 		md5Primitives.add(tBlock);
 		md5Primitives.add(tRunde);
 		
 		/********************** UPPER A RECT **********************/
 		md5Primitives.add(lang.newText(new Offset(5, 20, "tRunde", "S"), "A", "fA", null, bold16Props));
 		fAI = lang.newText(new Offset(-28, 3, "fA", "C"), "", "fAI", null, plain12Props);
 		md5Primitives.add(fAI);
 		md5Primitives.add(lang.newRect(new Offset(-50, -3, "fA", AnimalScript.DIRECTION_NW), new Offset(50, 15, "fA", "SE"), "fARect", null, funcRectProps));
 		/********************** UPPER B RECT **********************/
 		md5Primitives.add(lang.newText(new Offset(50, 7, "fARect", "NE"), "B", "fB", null, bold16Props));
 		fBI = lang.newText(new Offset(-28, 3, "fB", "C"), "", "fBI", null, plain12Props);
 		md5Primitives.add(fBI);
 		md5Primitives.add(lang.newRect(new Offset(-50, -3, "fB", AnimalScript.DIRECTION_NW), new Offset(50, 15, "fB", "SE"), "fBRect", null, funcRectProps));
 		/********************** UPPER C RECT **********************/
 		md5Primitives.add(lang.newText(new Offset(50, 7, "fBRect", "NE"), "C", "fC", null, bold16Props));
 		fCI = lang.newText(new Offset(-28, 3, "fC", "C"), "", "fCI", null, plain12Props);
 		md5Primitives.add(fCI);
 		md5Primitives.add(lang.newRect(new Offset(-50, -3, "fC", AnimalScript.DIRECTION_NW), new Offset(50, 15, "fC", "SE"), "fCRect", null, funcRectProps));
 		/********************** UPPER D RECT **********************/
 		md5Primitives.add(lang.newText(new Offset(50, 7, "fCRect", "NE"), "D", "fD", null, bold16Props));
 		fDI = lang.newText(new Offset(-28, 3, "fD", "C"), "", "fDI", null, plain12Props);
 		md5Primitives.add(fDI);
 		md5Primitives.add(lang.newRect(new Offset(-50, -3, "fD", AnimalScript.DIRECTION_NW), new Offset(50, 15, "fD", "SE"), "fDRect", null, funcRectProps));
 		
 		/********************** a0 RECT **********************/
 		blockPrimitives.add(lang.newText(new Offset(0, 140, "fA", "SW"), "a0", "fA0", null, bold16Props));
 		fA0I = lang.newText(new Offset(-28, 3, "fA0", "C"), "67452301", "fA0I", null, plain12Props);
 		blockPrimitives.add(fA0I);
 		blockPrimitives.add(lang.newRect(new Offset(-50, -3, "fA0", AnimalScript.DIRECTION_NW), new Offset(40, 15, "fA0", "SE"), "fA0Rect", null, funcRectProps));
 		
 		/********************** b0 RECT **********************/
 		blockPrimitives.add(lang.newText(new Offset(50, 7, "fA0Rect", "NE"), "b0", "fB0", null, bold16Props));
 		fB0I = lang.newText(new Offset(-28, 3, "fB0", "C"), "EFCDAB89", "fB0I", null, plain12Props);
 		blockPrimitives.add(fB0I);
 		blockPrimitives.add(lang.newRect(new Offset(-50, -3, "fB0", AnimalScript.DIRECTION_NW), new Offset(40, 15, "fB0", "SE"), "fB0Rect", null, funcRectProps));
 		
 		/********************** c0 RECT **********************/
 		blockPrimitives.add(lang.newText(new Offset(50, 7, "fB0Rect", "NE"), "c0", "fC0", null, bold16Props));
 		fC0I = lang.newText(new Offset(-28, 3, "fC0", "C"), "98BADCFE", "fC0I", null, plain12Props);
 		blockPrimitives.add(fC0I);
 		blockPrimitives.add(lang.newRect(new Offset(-50, -3, "fC0", AnimalScript.DIRECTION_NW), new Offset(40, 15, "fC0", "SE"), "fC0Rect", null, funcRectProps));
 		
 		/********************** d0 RECT **********************/
 		blockPrimitives.add(lang.newText(new Offset(50, 7, "fC0Rect", "NE"), "d0", "fD0", null, bold16Props));
 		fD0I = lang.newText(new Offset(-28, 3, "fD0", "C"), "10325476", "fD0I", null, plain12Props);
 		blockPrimitives.add(fD0I);
 		blockPrimitives.add(lang.newRect(new Offset(-50, -3, "fD0", AnimalScript.DIRECTION_NW), new Offset(40, 15, "fD0", "SE"), "fD0Rect", null, funcRectProps));
 		
 		/***************** a0 Add *****************/
 		blockPrimitives.add(lang.newRect(new Offset(-15, 60, "fA", AnimalScript.DIRECTION_C), new Offset(15, 90, "fA", "C"), "AddA0", null, addRectProps));
 		blockPrimitives.add(lang.newPolyline(new Node[]{ new Offset(0, 0, "fARect", "S"), new Offset(0, 0, "AddA0", "N") }, "lAddA", null, lineProps));
 		blockPrimitives.add(lang.newPolyline(new Node[]{ new Offset(0, 0, "AddA0", "S"), new Offset(0, 0, "fA0Rect", "N") }, "lAddA0", null, lineProps));
 		blockPrimitives.add(lang.newPolyline(new Node[]{ new Offset(10, 0, "fA0Rect", "NW"), new OffsetFromLastPosition(0, -50), new Offset(0, 0, "AddA0", "W") }, "pA0A", null, lineProps));
 		
 		/***************** b0 Add *****************/
 		blockPrimitives.add(lang.newRect(new Offset(-15, 60, "fB", AnimalScript.DIRECTION_C), new Offset(15, 90, "fB", "C"), "AddB0", null, addRectProps));
 		blockPrimitives.add(lang.newPolyline(new Node[]{ new Offset(0, 0, "fBRect", "S"), new Offset(0, 0, "AddB0", "N") }, "lAddB", null, lineProps));
 		blockPrimitives.add(lang.newPolyline(new Node[]{ new Offset(0, 0, "AddB0", "S"), new Offset(0, 0, "fB0Rect", "N") }, "lAddB0", null, lineProps));
 		blockPrimitives.add(lang.newPolyline(new Node[]{ new Offset(10, 0, "fB0Rect", "NW"), new OffsetFromLastPosition(0, -50), new Offset(0, 0, "AddB0", "W") }, "pB0B", null, lineProps));
 		
 		/***************** c0 Add *****************/
 		blockPrimitives.add(lang.newRect(new Offset(-15, 60, "fC", AnimalScript.DIRECTION_C), new Offset(15, 90, "fC", "C"), "AddC0", null, addRectProps));
 		blockPrimitives.add(lang.newPolyline(new Node[]{ new Offset(0, 0, "fCRect", "S"), new Offset(0, 0, "AddC0", "N") }, "lAddC", null, lineProps));
 		blockPrimitives.add(lang.newPolyline(new Node[]{ new Offset(0, 0, "AddC0", "S"), new Offset(0, 0, "fC0Rect", "N") }, "lAddC0", null, lineProps));
 		blockPrimitives.add(lang.newPolyline(new Node[]{ new Offset(10, 0, "fC0Rect", "NW"), new OffsetFromLastPosition(0, -50), new Offset(0, 0, "AddC0", "W") }, "pC0C", null, lineProps));
 		
 		/***************** d0 Add *****************/
 		blockPrimitives.add(lang.newRect(new Offset(-15, 60, "fD", AnimalScript.DIRECTION_C), new Offset(15, 90, "fD", "C"), "AddD0", null, addRectProps));
 		blockPrimitives.add(lang.newPolyline(new Node[]{ new Offset(0, 0, "fDRect", "S"), new Offset(0, 0, "AddD0", "N") }, "lAddD", null, lineProps));
 		blockPrimitives.add(lang.newPolyline(new Node[]{ new Offset(0, 0, "AddD0", "S"), new Offset(0, 0, "fD0Rect", "N") }, "lAddD0", null, lineProps));
 		blockPrimitives.add(lang.newPolyline(new Node[]{ new Offset(10, 0, "fD0Rect", "NW"), new OffsetFromLastPosition(0, -50), new Offset(0, 0, "AddD0", "W") }, "pD0D", null, lineProps));
 		
 		moveLineA0 = lang.newPolyline(new Node[]{ new Offset(0, 0, "fA0I", "NW"), new Offset(0, 0, "fAI", "NW") }, "moveLineA0", null, lineProps);
 		moveLineB0 = lang.newPolyline(new Node[]{ new Offset(0, 0, "fB0I", "NW"), new Offset(0, 0, "fBI", "NW") }, "moveLineB0", null, lineProps);
 		moveLineC0 = lang.newPolyline(new Node[]{ new Offset(0, 0, "fC0I", "NW"), new Offset(0, 0, "fCI", "NW") }, "moveLineC0", null, lineProps);
 		moveLineD0 = lang.newPolyline(new Node[]{ new Offset(0, 0, "fD0I", "NW"), new Offset(0, 0, "fDI", "NW") }, "moveLineD0", null, lineProps);
 		moveLineA0Z = lang.newPolyline(new Node[]{ new Offset(0, 0, "fAI", "NW"), new Offset(0, 0, "fA0I", "NW") }, "moveLineA0Z", null, lineProps);
 		moveLineB0Z = lang.newPolyline(new Node[]{ new Offset(0, 0, "fBI", "NW"), new Offset(0, 0, "fB0I", "NW") }, "moveLineB0Z", null, lineProps);
 		moveLineC0Z = lang.newPolyline(new Node[]{ new Offset(0, 0, "fCI", "NW"), new Offset(0, 0, "fC0I", "NW") }, "moveLineC0Z", null, lineProps);
 		moveLineD0Z = lang.newPolyline(new Node[]{ new Offset(0, 0, "fDI", "NW"), new Offset(0, 0, "fD0I", "NW") }, "moveLineD0Z", null, lineProps);
 		moveLineA0.hide();
 		moveLineB0.hide();
 		moveLineC0.hide();
 		moveLineD0.hide();
 		moveLineA0Z.hide();
 		moveLineB0Z.hide();
 		moveLineC0Z.hide();
 		moveLineD0Z.hide();
 		
 		/***************** 1st Add *****************/
 		roundPrimitives.add(lang.newRect(new Offset(-15, 60, "fA", AnimalScript.DIRECTION_C), 
 				new Offset(15, 90, "fA", "C"), "Add1", null, addRectProps));
 		roundPrimitives.add(lang.newPolyline(new Node[]{ new Offset(0, 0, "fARect", "S"), new Offset(0, 0, "Add1", "N") }, "lAdd1", null, lineProps));
 		
 		a1Result = lang.newText(new Offset(-55, 10, "Add1", "SW"), "00000000",
 		        "a1Result", null, plain12Props);
 		roundPrimitives.add(a1Result);
 		
 		/***************** F Rect *****************/
 		roundPrimitives.add(lang.newText(new Offset(45, 9, "Add1", "NE"), "F",
 		        "fF", null, bold16Props));
 		roundPrimitives.add(lang.newRect(new Offset(-10, -5, "fF", AnimalScript.DIRECTION_NW), new Offset(10, 5, "fF", "SE"), "fFRect", null, functionRectProps));
 		fFI = lang.newText(new Offset(-10, 5, "fFRect", "SW"), "00000000",
 		        "fFI", null, plain12Props);
 		roundPrimitives.add(fFI);
 		roundPrimitives.add(lang.newPolyline(new Node[]{ new Offset(0, 0, "fFRect", "W"), new Offset(0, 0, "Add1", "E") }, "lF", null, lineProps));
 		roundPrimitives.add(lang.newPolyline(new Node[]{ new Offset(0, 0, "fB", "S"), new OffsetFromLastPosition(0, 49), new Offset(0, -15, "fFRect", "E") }, "pBF", null, lineProps));
 		roundPrimitives.add(lang.newPolyline(new Node[]{ new Offset(0, 0, "fC", "S"), new OffsetFromLastPosition(0, 64), new Offset(0, 0, "fFRect", "E") }, "pCF", null, lineProps));
 		roundPrimitives.add(lang.newPolyline(new Node[]{ new Offset(0, 0, "fD", "S"), new OffsetFromLastPosition(0, 79), new Offset(0, 15, "fFRect", "E") }, "pDF", null, lineProps));
 		
 		/***************** 2nd Add *****************/
 		roundPrimitives.add(lang.newRect(new Offset(-15, 60, "Add1", AnimalScript.DIRECTION_C), new Offset(15, 90, "Add1", "C"), "Add2", null, addRectProps));
 		roundPrimitives.add(lang.newPolyline(new Node[]{ new Offset(0, 0, "Add1", "S"), new Offset(0, 0, "Add2", "N") }, "lAdd2", null, lineProps));
 		
 		fM = lang.newText(new Offset(-120, 7, "Add2", "NW"), "M[i]: 00000000",
 		        "fM", null, plain12Props);
 		roundPrimitives.add(fM);
 		roundPrimitives.add(lang.newPolyline(new Node[]{ new Offset(10, 0, "fM", "E"), new Offset(0, 0, "Add2", "W") }, "lM", null, lineProps));
 		
 		a2Result = lang.newText(new Offset(-55, 10, "Add2", "SW"), "00000000",
 		        "a2Result", null, plain12Props);
 		roundPrimitives.add(a2Result);
 		
 		/***************** 3rd Add *****************/
 		roundPrimitives.add(lang.newRect(new Offset(-15, 60, "Add2", AnimalScript.DIRECTION_C), new Offset(15, 90, "Add2", "C"), "Add3", null, addRectProps));
 		roundPrimitives.add(lang.newPolyline(new Node[]{ new Offset(0, 0, "Add2", "S"), new Offset(0, 0, "Add3", "N") }, "lAdd3", null, lineProps));
 		
 		fK = lang.newText(new Offset(-120, 7, "Add3", "NW"), "K: 00000000",
 		        "fK", null, plain12Props);
 		roundPrimitives.add(fK);
 		roundPrimitives.add(lang.newPolyline(new Node[]{ new Offset(10, 0, "fK", "E"), new Offset(0, 0, "Add3", "W") }, "lK", null, lineProps));
 		
 		a3Result = lang.newText(new Offset(-55, 10, "Add3", "SW"), "00000000",
 		        "a3Result", null, plain12Props);
 		roundPrimitives.add(a3Result);
 		
 		/***************** Shift *****************/
 		roundPrimitives.add(lang.newText(new Offset(0, 50, "Add3", "SW"), "<<<", "Shift", null, bold16Props));
 		roundPrimitives.add(lang.newRect(new Offset(-7, -5, "Shift", AnimalScript.DIRECTION_NW), new Offset(7, 5, "Shift", "SE"), "ShiftRect", null, shiftRectProps));
 		roundPrimitives.add(lang.newPolyline(new Node[]{ new Offset(0, 0, "Add3", "S"), new Offset(0, 0, "ShiftRect", "N") }, "lShift", null, lineProps));
 		fS = lang.newText(new Offset(-100, 3, "ShiftRect", "NW"), "S[j]:  0",
 		        "fS", null, plain12Props);
 		roundPrimitives.add(fS);
 		roundPrimitives.add(lang.newPolyline(new Node[]{ new Offset(20, 0, "fS", "E"), new Offset(0, 0, "ShiftRect", "W") }, "lS", null, lineProps));
 		
 		fTemp = lang.newText(new Offset(-100, 10, "ShiftRect", "SW"), "Temp: 00000000",
 		        "fTemp", null, plain12Props);
 		roundPrimitives.add(fTemp);
 		
 		/********************** BOTTOM A RECT **********************/
 		roundPrimitives.add(lang.newText(new Offset(0, 380, "fA", "SW"), "A", "fAz", null, bold16Props));
 		fAzI = lang.newText(new Offset(-28, 3, "fAz", "C"), "", "fAzI", null, plain12Props);
 		roundPrimitives.add(fAzI);
 		roundPrimitives.add(lang.newRect(new Offset(-50, -3, "fAz", AnimalScript.DIRECTION_NW), new Offset(50, 15, "fAz", "SE"), "fAzRect", null, funcRectProps));
 		
 		/********************** BOTTOM B RECT **********************/
 		roundPrimitives.add(lang.newText(new Offset(50, 7, "fAzRect", "NE"), "B", "fBz", null, bold16Props));
 		fBzI = lang.newText(new Offset(-28, 3, "fBz", "C"), "", "fBzI", null, plain12Props);
 		roundPrimitives.add(fBzI);
 		roundPrimitives.add(lang.newRect(new Offset(-50, -3, "fBz", AnimalScript.DIRECTION_NW), new Offset(50, 15, "fBz", "SE"), "fBzRect", null, funcRectProps));
 		
 		/********************** BOTTOM C RECT **********************/
 		roundPrimitives.add(lang.newText(new Offset(50, 7, "fBzRect", "NE"), "C", "fCz", null, bold16Props));
 		fCzI = lang.newText(new Offset(-28, 3, "fCz", "C"), "", "fCzI", null, plain12Props);
 		roundPrimitives.add(fCzI);
 		roundPrimitives.add(lang.newRect(new Offset(-50, -3, "fCz", AnimalScript.DIRECTION_NW), new Offset(50, 15, "fCz", "SE"), "fCzRect", null, funcRectProps));
 		
 		/********************** BOTTOM D RECT **********************/
 		roundPrimitives.add(lang.newText(new Offset(50, 7, "fCzRect", "NE"), "D", "fDz", null, bold16Props));
 		fDzI = lang.newText(new Offset(-28, 3, "fDz", "C"), "", "fDzI", null, plain12Props);
 		roundPrimitives.add(fDzI);
 		roundPrimitives.add(lang.newRect(new Offset(-50, -3, "fDz", AnimalScript.DIRECTION_NW), new Offset(50, 15, "fDz", "SE"), "fDzRect", null, funcRectProps));
 		
 		/********************** POLYLINES **********************/
 		roundPrimitives.add(lang.newPolyline(new Node[]{ new Offset(0, 0, "ShiftRect", "S"), new OffsetFromLastPosition(0, 30), new Offset(0, -20, "fBzRect", "N"), new Offset(0, 0, "fBzRect", "N") }, "pAB", null, lineProps));
 		roundPrimitives.add(lang.newPolyline(new Node[]{ new Offset(0, 0, "fDRect", "S"), new OffsetFromLastPosition(0, 300), new Offset(0, -20, "fAzRect", "N"), new Offset(0, 0, "fAzRect", "N") }, "pDA", null, lineProps));
 		roundPrimitives.add(lang.newPolyline(new Node[]{ new Offset(0, 0, "fCRect", "S"), new OffsetFromLastPosition(0, 300), new Offset(0, -20, "fDzRect", "N"), new Offset(0, 0, "fDzRect", "N") }, "pCD", null, lineProps));
 		roundPrimitives.add(lang.newPolyline(new Node[]{ new Offset(0, 0, "fBRect", "S"), new OffsetFromLastPosition(0, 300), new Offset(0, -20, "fCzRect", "N"), new Offset(0, 0, "fCzRect", "N") }, "pBC", null, lineProps));
 		
 		moveLineA = lang.newPolyline(new Node[]{ new Offset(0, 0, "fAzI", "NW"), new Offset(0, 0, "fAI", "NW") }, "moveLineA", null, lineProps);
 		moveLineB = lang.newPolyline(new Node[]{ new Offset(0, 0, "fBzI", "NW"), new Offset(0, 0, "fBI", "NW") }, "moveLineB", null, lineProps);
 		moveLineC = lang.newPolyline(new Node[]{ new Offset(0, 0, "fCzI", "NW"), new Offset(0, 0, "fCI", "NW") }, "moveLineC", null, lineProps);
 		moveLineD = lang.newPolyline(new Node[]{ new Offset(0, 0, "fDzI", "NW"), new Offset(0, 0, "fDI", "NW") }, "moveLineD", null, lineProps);
 		moveLineAZ = lang.newPolyline(new Node[]{ new Offset(0, 0, "fAI", "NW"), new Offset(0, 0, "fAzI", "NW") }, "moveLineAZ", null, lineProps);
 		moveLineBZ = lang.newPolyline(new Node[]{ new Offset(0, 0, "fBI", "NW"), new Offset(0, 0, "fBzI", "NW") }, "moveLineBZ", null, lineProps);
 		moveLineCZ = lang.newPolyline(new Node[]{ new Offset(0, 0, "fCI", "NW"), new Offset(0, 0, "fCzI", "NW") }, "moveLineCZ", null, lineProps);
 		moveLineDZ = lang.newPolyline(new Node[]{ new Offset(0, 0, "fDI", "NW"), new Offset(0, 0, "fDzI", "NW") }, "moveLineDZ", null, lineProps);
 		moveLineA.hide();
 		moveLineB.hide();
 		moveLineC.hide();
 		moveLineD.hide();
 		moveLineAZ.hide();
 		moveLineBZ.hide();
 		moveLineCZ.hide();
 		moveLineDZ.hide();
 		
 		setVisible(roundPrimitives, false);
	}

	public void startMD5Calculation() {
		code.highlight(0);
		
		lang.nextStep("2. Kompression");
	    /********************************* NEXT STEP *********************************/
		
		int numBlocks = total.length / 64;
		int[] M = new int[16];

		code.unhighlight(0);
		code.highlight(1);
		tBlock.setText("Block: 0 / "+numBlocks, null, null);
		tBlock.changeColor(null, Color.red, null, null);
		
		lang.nextStep();
	    /********************************* NEXT STEP *********************************/
		
		tBlock.changeColor(null, (Color)bold14Props.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
		code.unhighlight(1);
		code.highlight(2);
		
		lang.nextStep();
	    /********************************* NEXT STEP *********************************/
		
		a0 = 0x67452301;
	 	b0 = 0xEFCDAB89;
	 	c0 = 0x98BADCFE;
	 	d0 = 0x10325476;
		
		code.unhighlight(2);
		
		for (int i = 0; i < numBlocks; i++) {
			code.highlight(4);
			tBlock.setText("Block: "+(i+1)+" / "+numBlocks, null, null);
			tBlock.changeColor(null, Color.red, null, null);
			
			lang.nextStep("		"+(i+1)+". Block");
		    /********************************* NEXT STEP *********************************/
			
			tBlock.changeColor(null, (Color)bold14Props.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
			code.unhighlight(4);
			code.highlight(5);
			
			M = copyBlockLittleEndian(total, i);
			
			int gRow = i * 8;
			inputMatrix.highlightElemColumnRange(gRow, 0, 7, null, null);
			inputMatrix.highlightElemColumnRange(gRow + 1, 0, 7, null, null);
			inputMatrix.highlightElemColumnRange(gRow + 2, 0, 7, null, null);
			inputMatrix.highlightElemColumnRange(gRow + 3, 0, 7, null, null);
			inputMatrix.highlightElemColumnRange(gRow + 4, 0, 7, null, null);
			inputMatrix.highlightElemColumnRange(gRow + 5, 0, 7, null, null);
			inputMatrix.highlightElemColumnRange(gRow + 6, 0, 7, null, null);
			inputMatrix.highlightElemColumnRange(gRow + 7, 0, 7, null, null);
			
			lang.nextStep();
		    /********************************* NEXT STEP *********************************/
			
			code.unhighlight(5);
			code.highlight(6);
			code.highlight(7);
			code.highlight(8);
			code.highlight(9);
			
			int a = a0;
			int b = b0;
			int c = c0;
			int d = d0;
			
			fA0I.changeColor(null, Color.red, null, null);
			fB0I.changeColor(null, Color.red, null, null);
			fC0I.changeColor(null, Color.red, null, null);
			fD0I.changeColor(null, Color.red, null, null);
			
			fA0I.moveVia(null, "translate", moveLineA0, null, new TicksTiming(200));
			fB0I.moveVia(null, "translate", moveLineB0, null, new TicksTiming(200));
			fC0I.moveVia(null, "translate", moveLineC0, null, new TicksTiming(200));
			fD0I.moveVia(null, "translate", moveLineD0, null, new TicksTiming(200));
			fAI.setText(toHexString(a0), new TicksTiming(200), null);
			fBI.setText(toHexString(b0), new TicksTiming(200), null);
			fCI.setText(toHexString(c0), new TicksTiming(200), null);
			fDI.setText(toHexString(d0), new TicksTiming(200), null);
			fAI.changeColor(null, Color.red, new TicksTiming(200), null);
			fBI.changeColor(null, Color.red, new TicksTiming(200), null);
			fCI.changeColor(null, Color.red, new TicksTiming(200), null);
			fDI.changeColor(null, Color.red, new TicksTiming(200), null);
			fA0I.setText("", new TicksTiming(200), null);
			fB0I.setText("", new TicksTiming(200), null);
			fC0I.setText("", new TicksTiming(200), null);
			fD0I.setText("", new TicksTiming(200), null);
			fA0I.moveVia(null, "translate", moveLineA0Z, new TicksTiming(200), null);
			fB0I.moveVia(null, "translate", moveLineB0Z, new TicksTiming(200), null);
			fC0I.moveVia(null, "translate", moveLineC0Z, new TicksTiming(200), null);
			fD0I.moveVia(null, "translate", moveLineD0Z, new TicksTiming(200), null);
			
			lang.nextStep();
		    /********************************* NEXT STEP *********************************/
			
			setVisible(blockPrimitives, false);
			fA0I.setText(toHexString(a0), new TicksTiming(200), null);
			fB0I.setText(toHexString(b0), new TicksTiming(200), null);
			fC0I.setText(toHexString(c0), new TicksTiming(200), null);
			fD0I.setText(toHexString(d0), new TicksTiming(200), null);
			fAI.changeColor(null, Color.black, null, null);
			fBI.changeColor(null, Color.black, null, null);
			fCI.changeColor(null, Color.black, null, null);
			fDI.changeColor(null, Color.black, null, null);
			fA0I.changeColor(null, Color.black, null, null);
			fB0I.changeColor(null, Color.black, null, null);
			fC0I.changeColor(null, Color.black, null, null);
			fD0I.changeColor(null, Color.black, null, null);
			code.unhighlight(6);
			code.unhighlight(7);
			code.unhighlight(8);
			code.unhighlight(9);
			
			lang.nextStep();
		    /********************************* NEXT STEP *********************************/
			
			setVisible(roundPrimitives, true);
			
			
			inputMatrix.unhighlightElemColumnRange(gRow, 0, 7, null, null);
			inputMatrix.unhighlightElemColumnRange(gRow + 1, 0, 7, null, null);
			inputMatrix.unhighlightElemColumnRange(gRow + 2, 0, 7, null, null);
			inputMatrix.unhighlightElemColumnRange(gRow + 3, 0, 7, null, null);
			inputMatrix.unhighlightElemColumnRange(gRow + 4, 0, 7, null, null);
			inputMatrix.unhighlightElemColumnRange(gRow + 5, 0, 7, null, null);
			inputMatrix.unhighlightElemColumnRange(gRow + 6, 0, 7, null, null);
			inputMatrix.unhighlightElemColumnRange(gRow + 7, 0, 7, null, null);
			
			/***** BEGINNE FOR SCHLEIFE FÜR RUNDE *****/
			code.highlight(11);
			boolean makeAnimation = true;
			for (int j = 0; j < 48; j++) {
				makeAnimation = j < AnimatedRounds;
				
				tRunde.setText("Runde: "+(j+1)+" / 48", null, null);
				tRunde.changeColor(null, Color.red, null, null);
				if(makeAnimation) {
					lang.nextStep();
				    /********************************* NEXT STEP *********************************/
				}
				
				int f = 0;
				int K = 0;
				int bufferIndex = j;
				
				tRunde.changeColor(null, (Color)bold14Props.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
				code.unhighlight(11);
				code.highlight(12);
				fFI.setText(toHexString(f), null, null);
				fFI.changeColor(null, Color.red, null, null);
				
				if(makeAnimation) {
					lang.nextStep();
				    /********************************* NEXT STEP *********************************/
				}
				
				fFI.changeColor(null, Color.black, null, null);
				code.unhighlight(12);
				
				int fCodeLine = 15;
				if (0 <= j && j <= 15) {
					f = (b & c) | (~b & d);
					K = 0;
					if(makeAnimation) {
						code.highlight(14);
						lang.nextStep();
						code.unhighlight(14);
						fCodeLine = 15;
					}
				} else if (16 <= j && j <= 31) {
					f = (b & c) | (b & d) | (c & d);
					K = 0x5A827999;
					bufferIndex = ((bufferIndex - 16) / 4) + 4 * (bufferIndex % 4);
					if(makeAnimation) {
						code.highlight(16);
						lang.nextStep();
						code.unhighlight(16);
						fCodeLine = 17;
					}
				} else if (32 <= j && j <= 47) {
					f = b ^ c ^ d;
					K = 0x6ED9EBA1;
					int r = bufferIndex - 32;
					int p = (r % 4 == 0) ? 0 : (r % 4 == 1) ? 8 : (r % 4 == 2) ? 4 : 12;
					r /= 4;
					r = (r == 0) ? 0 : (r == 1) ? 2 : (r == 2) ? 1 : 3;
					bufferIndex = r + p;
					
					if(makeAnimation) {
						code.highlight(20);
						lang.nextStep();
						code.unhighlight(20);
						fCodeLine = 21;
					}
				}
				
				fFI.setText(toHexString(f), null, null);
				if(makeAnimation) {
					code.highlight(fCodeLine);
					fFI.changeColor(null, Color.red, null, null);
		
					lang.nextStep();
				    /********************************* NEXT STEP *********************************/
					
					code.unhighlight(fCodeLine);
					fFI.changeColor(null, Color.black, null, null);
					code.highlight(29);
				}
				int a1Res = a + f;
				int a2Res = a1Res + M[bufferIndex];
				int a3Res = a2Res + K;
				
				a1Result.setText(toHexString(a1Res), null, null);
				a1Result.changeColor(null, Color.red, null, null);
				
				if(makeAnimation) lang.nextStep();
				
				fM.setText("M["+bufferIndex+"]: "+toHexString(M[bufferIndex]), null, null);
				fM.changeColor(null, Color.red, null, null);
				
				int row = (bufferIndex / 2) + 8 * i;
				int col = (bufferIndex % 2) * 4;
				inputMatrix.highlightElemColumnRange(row, col, col+3, null, null);
				
				if(makeAnimation) lang.nextStep();
				
				inputMatrix.unhighlightElemColumnRange(row, col, col+3, null, null);
				
				a1Result.changeColor(null, Color.black, null, null);
				fM.changeColor(null, Color.black, null, null);
				a2Result.setText(toHexString(a2Res), null, null);
				a2Result.changeColor(null, Color.red, null, null);
				
				if(makeAnimation) lang.nextStep();
				
				fK.setText("K: "+toHexString(K), null, null);
				fK.changeColor(null, Color.red, null, null);
				
				if(makeAnimation) lang.nextStep();
				
				a2Result.changeColor(null, Color.black, null, null);
				fK.changeColor(null, Color.black, null, null);
				a3Result.setText(toHexString(a3Res), null, null);
				a3Result.changeColor(null, Color.red, null, null);
				
				if(makeAnimation) lang.nextStep();
				
				fS.setText("S["+j+"]: "+SHIFT_AMTS[j], null, null);
				fS.changeColor(null, Color.red, null, null);
				
				if(makeAnimation) lang.nextStep();
				
				a3Result.changeColor(null, Color.black, null, null);
				fS.changeColor(null, Color.black, null, null);
				
				int temp = Integer.rotateLeft(a + f + M[bufferIndex] + K, SHIFT_AMTS[j]);
				
				fTemp.setText("Temp: "+toHexString(temp), null, null);
				fTemp.changeColor(null, Color.red, null, null);
				
				if(makeAnimation) lang.nextStep();
			    /********************************* NEXT STEP *********************************/
				
				a = d;
				
				fK.changeColor(null, Color.black, null, null);
				fS.changeColor(null, Color.black, null, null);
				fTemp.changeColor(null, Color.black, null, null);
				code.unhighlight(29);
				code.highlight(30);
				fAzI.setText(toHexString(a), null, null);
				fAzI.changeColor(null, Color.red, null, null);
				
				if(makeAnimation) {
					lang.nextStep();
				    /********************************* NEXT STEP *********************************/
					fAzI.changeColor(null, Color.black, null, null);
				}
				
				d = c;
				
				code.unhighlight(30);
				code.highlight(31);
				fDzI.setText(toHexString(d), null, null);
				fDzI.changeColor(null, Color.red, null, null);
				
				if(makeAnimation) {
					lang.nextStep();
				    /********************************* NEXT STEP *********************************/
					fDzI.changeColor(null, Color.black, null, null);
				}
				c = b;
				
				code.unhighlight(31);
				code.highlight(32);
				fCzI.setText(toHexString(c), null, null);
				fCzI.changeColor(null, Color.red, null, null);
				
				if(makeAnimation) {
					lang.nextStep();
				    /********************************* NEXT STEP *********************************/
					fCzI.changeColor(null, Color.black, null, null);
				}
				
				b = temp;
				
				code.unhighlight(32);
				
				fBzI.setText(toHexString(b), null, null);
				fBzI.changeColor(null, Color.red, null, null);
				
				if(makeAnimation || j == 47) {
					if (makeAnimation) {
						code.highlight(33);
						
						lang.nextStep();
					    /********************************* NEXT STEP *********************************/
						
						fBzI.changeColor(null, Color.black, null, null);
						code.unhighlight(33);
						code.highlight(11);
					}
					
					fAzI.moveVia(null, "translate", moveLineA, null, new TicksTiming(200));
					fBzI.moveVia(null, "translate", moveLineB, null, new TicksTiming(200));
					fCzI.moveVia(null, "translate", moveLineC, null, new TicksTiming(200));
					fDzI.moveVia(null, "translate", moveLineD, null, new TicksTiming(200));
					fAI.setText(toHexString(a), new TicksTiming(200), null);
					fBI.setText(toHexString(b), new TicksTiming(200), null);
					fCI.setText(toHexString(c), new TicksTiming(200), null);
					fDI.setText(toHexString(d), new TicksTiming(200), null);
					fAzI.setText("", new TicksTiming(200), null);
					fBzI.setText("", new TicksTiming(200), null);
					fCzI.setText("", new TicksTiming(200), null);
					fDzI.setText("", new TicksTiming(200), null);
					fAzI.moveVia(null, "translate", moveLineAZ, new TicksTiming(200), null);
					fBzI.moveVia(null, "translate", moveLineBZ, new TicksTiming(200), null);
					fCzI.moveVia(null, "translate", moveLineCZ, new TicksTiming(200), null);
					fDzI.moveVia(null, "translate", moveLineDZ, new TicksTiming(200), null);
					
					if (j == (AnimatedRounds - 1)) lang.nextStep();
				} else  {
					lang.nextStep();
					tRunde.setText("Runde: "+(j+1)+" / 48", null, null);
					tRunde.changeColor(null, Color.red, null, null);
					fAI.setText(toHexString(a), null, null);
					fBI.setText(toHexString(b), null, null);
					fCI.setText(toHexString(c), null, null);
					fDI.setText(toHexString(d), null, null);
					fAI.changeColor(null, Color.red, null, null);
					fBI.changeColor(null, Color.red, null, null);
					fCI.changeColor(null, Color.red, null, null);
					fDI.changeColor(null, Color.red, null, null);
				}
			}
			
			lang.nextStep();
		    /********************************* NEXT STEP *********************************/
			
			code.unhighlight(11);
			code.highlight(34);
			tRunde.changeColor(null, Color.black, null, null);
			fAI.changeColor(null, Color.black, null, null);
			fBI.changeColor(null, Color.black, null, null);
			fCI.changeColor(null, Color.black, null, null);
			fDI.changeColor(null, Color.black, null, null);
			setVisible(roundPrimitives, false);
			
			lang.nextStep();
		    /********************************* NEXT STEP *********************************/
			
			setVisible(blockPrimitives, true);
			
			lang.nextStep();
		    /********************************* NEXT STEP *********************************/
			
			code.unhighlight(34);
			code.highlight(35);
			code.highlight(36);
			code.highlight(37);
			code.highlight(38);
			fAI.changeColor(null, Color.red, null, null);
			fBI.changeColor(null, Color.red, null, null);
			fCI.changeColor(null, Color.red, null, null);
			fDI.changeColor(null, Color.red, null, null);
			fA0I.changeColor(null, Color.red, null, null);
			fB0I.changeColor(null, Color.red, null, null);
			fC0I.changeColor(null, Color.red, null, null);
			fD0I.changeColor(null, Color.red, null, null);
			
			lang.nextStep();
		    /********************************* NEXT STEP *********************************/
			
			fAI.changeColor(null, Color.black, null, null);
			fBI.changeColor(null, Color.black, null, null);
			fCI.changeColor(null, Color.black, null, null);
			fDI.changeColor(null, Color.black, null, null);
			a0 += a;
			b0 += b;
			c0 += c;
			d0 += d;
			fA0I.setText(toHexString(a0), null, null);
			fB0I.setText(toHexString(b0), null, null);
			fC0I.setText(toHexString(c0), null, null);
			fD0I.setText(toHexString(d0), null, null);
			
			lang.nextStep();
		    /********************************* NEXT STEP *********************************/
			
			code.unhighlight(35);
			code.unhighlight(36);
			code.unhighlight(37);
			code.unhighlight(38);
			code.highlight(39);
			fA0I.changeColor(null, Color.black, null, null);
			fB0I.changeColor(null, Color.black, null, null);
			fC0I.changeColor(null, Color.black, null, null);
			fD0I.changeColor(null, Color.black, null, null);
		}
		
		lang.nextStep();
	    /********************************* NEXT STEP *********************************/
		
		code.unhighlight(39);
		code.highlight(40);
		
		lang.nextStep();
	    /********************************* NEXT STEP *********************************/
		
		String[] hashArray = new String[16];
		int count = 0;
		for (int i = 0; i < 4; i++) // A B C D jeweils als little endian zusammenfügen
		{
			int n = (i == 0) ? a0 : ((i == 1) ? b0 : ((i == 2) ? c0 : d0));
			for (int j = 0; j < 4; j++) {
				hashArray[count++] = String.format("%02X", ((byte) n) & 0xFF);
				n >>>= 8;
			}
		}
		
		/********************** WORD CONTENT **********************/
		Text fA01 = lang.newText(new Offset(0, 20, "fA0Rect", "SW"),hashArray[3], "fA01", null, bold16Props);
		Text fA02 = lang.newText(new Offset(5, 0, "fA01", "NE"), hashArray[2], "fA02", null, bold16Props);
		Text fA03 = lang.newText(new Offset(5, 0, "fA02", "NE"), hashArray[1], "fA03", null, bold16Props);
		Text fA04 = lang.newText(new Offset(5, 0, "fA03", "NE"), hashArray[0], "fA04", null, bold16Props);
		
		Text fB01 = lang.newText(new Offset(20, 0, "fA04", "NE"), hashArray[7], "fB01", null, bold16Props);
		Text fB02 = lang.newText(new Offset(5, 0, "fB01", "NE"), hashArray[6], "fB02", null, bold16Props);
		Text fB03 = lang.newText(new Offset(5, 0, "fB02", "NE"), hashArray[5], "fB03", null, bold16Props);
		Text fB04 = lang.newText(new Offset(5, 0, "fB03", "NE"), hashArray[4], "fB04", null, bold16Props);
		
		Text fC01 = lang.newText(new Offset(20, 0, "fB04", "NE"), hashArray[11], "fC01", null, bold16Props);
		Text fC02 = lang.newText(new Offset(5, 0, "fC01", "NE"), hashArray[10], "fC02", null, bold16Props);
		Text fC03 = lang.newText(new Offset(5, 0, "fC02", "NE"), hashArray[9], "fC03", null, bold16Props);
		Text fC04 = lang.newText(new Offset(5, 0, "fC03", "NE"), hashArray[8], "fC04", null, bold16Props);
		
		Text fD01 = lang.newText(new Offset(20, 0, "fC04", "NE"), hashArray[15], "fD01", null, bold16Props);
		Text fD02 = lang.newText(new Offset(5, 0, "fD01", "NE"), hashArray[14], "fD02", null, bold16Props);
		Text fD03 = lang.newText(new Offset(5, 0, "fD02", "NE"), hashArray[13], "fD03", null, bold16Props);
		Text fD04 = lang.newText(new Offset(5, 0, "fD03", "NE"), hashArray[12], "fD04", null, bold16Props);
		
		md5Primitives.add(fA01);
		md5Primitives.add(fA02);
		md5Primitives.add(fA03);
		md5Primitives.add(fA04);
		md5Primitives.add(fB01);
		md5Primitives.add(fB02);
		md5Primitives.add(fB03);
		md5Primitives.add(fB04);
		md5Primitives.add(fC01);
		md5Primitives.add(fC02);
		md5Primitives.add(fC03);
		md5Primitives.add(fC04);
		md5Primitives.add(fD01);
		md5Primitives.add(fD02);
		md5Primitives.add(fD03);
		md5Primitives.add(fD04);
		
		/********************** HASH **********************/
		lang.newText(new Offset(0, 50, "fA0Rect", "SW"), hashArray[0], "fA01z", null, bold16Props).hide();
		lang.newText(new Offset(5, 0, "fA01z", "NE"), hashArray[1], "fA02z", null, bold16Props).hide();
		lang.newText(new Offset(5, 0, "fA02z", "NE"), hashArray[2], "fA03z", null, bold16Props).hide();
		lang.newText(new Offset(5, 0, "fA03z", "NE"), hashArray[3], "fA04z", null, bold16Props).hide();
		
		lang.newText(new Offset(20, 0, "fA04z", "NE"), hashArray[4], "fB01z", null, bold16Props).hide();
		lang.newText(new Offset(5, 0, "fB01z", "NE"), hashArray[5], "fB02z", null, bold16Props).hide();
		lang.newText(new Offset(5, 0, "fB02z", "NE"), hashArray[6], "fB03z", null, bold16Props).hide();
		lang.newText(new Offset(5, 0, "fB03z", "NE"), hashArray[7], "fB04z", null, bold16Props).hide();
		
		lang.newText(new Offset(20, 0, "fB04z", "NE"), hashArray[8], "fC01z", null, bold16Props).hide();
		lang.newText(new Offset(5, 0, "fC01z", "NE"), hashArray[9], "fC02z", null, bold16Props).hide();
		lang.newText(new Offset(5, 0, "fC02z", "NE"), hashArray[10], "fC03z", null, bold16Props).hide();
		lang.newText(new Offset(5, 0, "fC03z", "NE"), hashArray[11], "fC04z", null, bold16Props).hide();
		
		lang.newText(new Offset(20, 0, "fC04z", "NE"), hashArray[12], "fD01z", null, bold16Props).hide();
		lang.newText(new Offset(5, 0, "fD01z", "NE"), hashArray[13], "fD02z", null, bold16Props).hide();
		lang.newText(new Offset(5, 0, "fD02z", "NE"), hashArray[14], "fD03z", null, bold16Props).hide();
		lang.newText(new Offset(5, 0, "fD03z", "NE"), hashArray[15], "fD04z", null, bold16Props).hide();
		
		Polyline moveLineHA1 = lang.newPolyline(new Node[]{ new Offset(0, 0, "fA04", "NW"), new Offset(0, 0, "fA01z", "NW") }, "moveLineA01", null, lineProps);
		Polyline moveLineHA2 = lang.newPolyline(new Node[]{ new Offset(0, 0, "fA03", "NW"), new Offset(0, 0, "fA02z", "NW") }, "moveLineA02", null, lineProps);
		Polyline moveLineHA3 = lang.newPolyline(new Node[]{ new Offset(0, 0, "fA02", "NW"), new Offset(0, 0, "fA03z", "NW") }, "moveLineA03", null, lineProps);
		Polyline moveLineHA4 = lang.newPolyline(new Node[]{ new Offset(0, 0, "fA01", "NW"), new Offset(0, 0, "fA04z", "NW") }, "moveLineA04", null, lineProps);
		Polyline moveLineHB1 = lang.newPolyline(new Node[]{ new Offset(0, 0, "fB04", "NW"), new Offset(0, 0, "fB01z", "NW") }, "moveLineB01", null, lineProps);
		Polyline moveLineHB2 = lang.newPolyline(new Node[]{ new Offset(0, 0, "fB03", "NW"), new Offset(0, 0, "fB02z", "NW") }, "moveLineB02", null, lineProps);
		Polyline moveLineHB3 = lang.newPolyline(new Node[]{ new Offset(0, 0, "fB02", "NW"), new Offset(0, 0, "fB03z", "NW") }, "moveLineB03", null, lineProps);
		Polyline moveLineHB4 = lang.newPolyline(new Node[]{ new Offset(0, 0, "fB01", "NW"), new Offset(0, 0, "fB04z", "NW") }, "moveLineB04", null, lineProps);
		Polyline moveLineHC1 = lang.newPolyline(new Node[]{ new Offset(0, 0, "fC04", "NW"), new Offset(0, 0, "fC01z", "NW") }, "moveLineC01", null, lineProps);
		Polyline moveLineHC2 = lang.newPolyline(new Node[]{ new Offset(0, 0, "fC03", "NW"), new Offset(0, 0, "fC02z", "NW") }, "moveLineC02", null, lineProps);
		Polyline moveLineHC3 = lang.newPolyline(new Node[]{ new Offset(0, 0, "fC02", "NW"), new Offset(0, 0, "fC03z", "NW") }, "moveLineC03", null, lineProps);
		Polyline moveLineHC4 = lang.newPolyline(new Node[]{ new Offset(0, 0, "fC01", "NW"), new Offset(0, 0, "fC04z", "NW") }, "moveLineC04", null, lineProps);
		Polyline moveLineHD1 = lang.newPolyline(new Node[]{ new Offset(0, 0, "fD04", "NW"), new Offset(0, 0, "fD01z", "NW") }, "moveLineD01", null, lineProps);
		Polyline moveLineHD2 = lang.newPolyline(new Node[]{ new Offset(0, 0, "fD03", "NW"), new Offset(0, 0, "fD02z", "NW") }, "moveLineD02", null, lineProps);
		Polyline moveLineHD3 = lang.newPolyline(new Node[]{ new Offset(0, 0, "fD02", "NW"), new Offset(0, 0, "fD03z", "NW") }, "moveLineD03", null, lineProps);
		Polyline moveLineHD4 = lang.newPolyline(new Node[]{ new Offset(0, 0, "fD01", "NW"), new Offset(0, 0, "fD04z", "NW") }, "moveLineD04", null, lineProps);
		moveLineHA1.hide();
		moveLineHA2.hide();
		moveLineHA3.hide();
		moveLineHA4.hide();
		moveLineHB1.hide();
		moveLineHB2.hide();
		moveLineHB3.hide();
		moveLineHB4.hide();
		moveLineHC1.hide();
		moveLineHC2.hide();
		moveLineHC3.hide();
		moveLineHC4.hide();
		moveLineHD1.hide();
		moveLineHD2.hide();
		moveLineHD3.hide();
		moveLineHD4.hide();
		
		lang.nextStep();
	    /********************************* NEXT STEP *********************************/
		
		fA04.moveVia(null, "translate", moveLineHA1, null, new TicksTiming(50));
		fA03.moveVia(null, "translate", moveLineHA2, new TicksTiming(50), new TicksTiming(50));
		fA02.moveVia(null, "translate", moveLineHA3, new TicksTiming(100), new TicksTiming(50));
		fA01.moveVia(null, "translate", moveLineHA4, new TicksTiming(150), new TicksTiming(50));
		fB04.moveVia(null, "translate", moveLineHB1, new TicksTiming(200), new TicksTiming(50));
		fB03.moveVia(null, "translate", moveLineHB2, new TicksTiming(250), new TicksTiming(50));
		fB02.moveVia(null, "translate", moveLineHB3, new TicksTiming(300), new TicksTiming(50));
		fB01.moveVia(null, "translate", moveLineHB4, new TicksTiming(350), new TicksTiming(50));
		fC04.moveVia(null, "translate", moveLineHC1, new TicksTiming(400), new TicksTiming(50));
		fC03.moveVia(null, "translate", moveLineHC2, new TicksTiming(450), new TicksTiming(50));
		fC02.moveVia(null, "translate", moveLineHC3, new TicksTiming(500), new TicksTiming(50));
		fC01.moveVia(null, "translate", moveLineHC4, new TicksTiming(550), new TicksTiming(50));
		fD04.moveVia(null, "translate", moveLineHD1, new TicksTiming(600), new TicksTiming(50));
		fD03.moveVia(null, "translate", moveLineHD2, new TicksTiming(650), new TicksTiming(50));
		fD02.moveVia(null, "translate", moveLineHD3, new TicksTiming(700), new TicksTiming(50));
		fD01.moveVia(null, "translate", moveLineHD4, new TicksTiming(750), new TicksTiming(50));
		
		lang.nextStep();
	    /********************************* NEXT STEP *********************************/
		
		RectProperties rectProps = new RectProperties();
	    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
	    md5Primitives.add(lang.newRect(new Offset(-5, -5, "fA01z", AnimalScript.DIRECTION_NW), new Offset(5, 5, "fD04z", "SE"), "hashRect", null, rectProps));
	    
	    md5Primitives.add(lang.newText(new Offset(-100, 0, "fA01z", "NW"), "Hashwert:", "hashValue", null, bold16Props));
	    md5Primitives.add(lang.newText(new Offset(0, 30, "hashValue", "SW"), "Eingabe:", "inputS", null, bold16Props));
	    md5Primitives.add(lang.newText(new Offset(0, 30, "fA01z", "SW"), Message, "inputMessage", null, bold16Props));
	    md5Primitives.add(lang.newRect(new Offset(-5, -5, "inputMessage", AnimalScript.DIRECTION_NW), new Offset(5, 5, "inputMessage", "SE"), "hashMessageRect", null, rectProps));
		
	    lang.nextStep();
	    /********************************* NEXT STEP *********************************/
	    
	    showConclusion();
	}
	
	public void showConclusion() {
		setVisible(md5Primitives, false);
	    setVisible(blockPrimitives, false);
	    inputMatrix.hide();
	    code.hide();
	    
		descHeader.setText("Schritt 3: Fazit", null, null);
		SourceCode conclusion = lang.newSourceCode(new Offset(0, 5, "descHeader", "SW"), "conclusion", null, scProps);
		conclusion.addCodeLine("Bereits 1995 wurde ein vollständiger Kollisionsangriff für MD4 veröffentlicht.", null, 0, null);
		conclusion.addCodeLine("Bei einem Kollisionsangriff wird versucht zwei Eingaben zu finden,", null, 0, null);
		conclusion.addCodeLine("sodass md4(eingabe1) = md4(eingabe2)", null, 0, null);
		conclusion.addCodeLine("", null, 0, null);
		conclusion.addCodeLine("Im Jahr 2004 wurde eine Methode veröffentlicht, die es erlaubt eine Kollision mit", null, 0, null);
		conclusion.addCodeLine("weniger als 2^8 MD4 Hash-Operationen zu erzeugen.", null, 0, null);
		conclusion.addCodeLine("", null, 0, null);
		conclusion.addCodeLine("Vom Gebrauch des Algorithmus als kryptologische Hashfunktion wird dennoch mittlerweile abgeraten.", null, 0, null);
		conclusion.addCodeLine("", null, 0, null);
		conclusion.addCodeLine("Weitere Informationen und Links:", null, 0, null);
		conclusion.addCodeLine("http://tools.ietf.org/html/rfc1320", null, 0, null);
		conclusion.addCodeLine("http://tools.ietf.org/rfc/rfc6150.txt", null, 0, null);
		
		lang.nextStep("3. Fazit");
	    /********************************* NEXT STEP *********************************/
	}
	
	public void setVisible(List<Primitive> primitives, boolean visible) {
		for( Primitive p : primitives )
		{
			if (visible) {
				p.show();
			} else {
				p.hide();
			}
		}
	}
	
	public String toHexString(int i) {
		return Integer.toHexString(i).toUpperCase();
	}
	
	public int[] copyBlockLittleEndian(byte[] messages, int blockCount) {
		int[] result = new int[16];
		int index = blockCount << 6; // Gesamt Index
		for (int j = 0; j < 16; j++, index = index + 4) { // Teile aktuellen Block in 16 (32bit) chunks (little endian)
			result[j] = ((messages[index] & 0xFF)
					| ((messages[index + 1] & 0xFF) << 8)
					| ((messages[index + 2] & 0xFF) << 16) | ((messages[index + 3] & 0xFF) << 24));
		}
		return result;
	}
	
	public void fillArray(String[][] arr, String val) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				arr[i][j] = val;
			}
		}
	}

    public String getName() {
        return "MD4 Algorithmus";
    }

    public String getAlgorithmName() {
        return "MD4";
    }

    public String getAnimationAuthor() {
        return "Bekir Bayrak, Halit Ciftci";
    }

    public String getDescription(){
        return "Der MD4-Algorithmus (Message-Digest Algorithm 4) ist eine kryptologische Hashfunktion, die 1990 von Ronald Rivest entwickelt wurde." +
        		"Ziel war es unter anderem einen Algorithmus zu entwickeln, der leicht zu implementieren ist und schnell auf 32bit Rechnern läuft." +
        		"Der Algorithmus erzeugt aus einem Eingabestring einen 128bit langen Hash." +
        		"Mittlerweile gilt der Algorithmus als höchst unsicher und von der Verwendung wird abgeraten.";
    }

    public String getCodeExample(){
        return 
        		"public static byte[] computeMD4(byte[] gesamt) {\n"+
				"	int numBlocks = gesamt.length / 64;\n"+
				"\n"+
				"	int a = 0x67452301;\n"+
				"	int b = 0xEFCDAB89;\n"+
				"	int c = 0x98BADCFE;\n"+
				"	int d = 0x10325476;\n"+
				"\n"+
				"	int[] buffer = new int[16];\n"+
				"\n"+
				"	for (int i = 0; i < numBlocks; i++) {\n"+
				"		buffer = copyBlockLittleEndian(gesamt, i);\n"+
				"\n"+
				"		int originalA = a;\n"+
				"		int originalB = b;\n"+
				"		int originalC = c;\n"+
				"		int originalD = d;\n"+
				"\n"+
				"		for (int j = 0; j < 48; j++) {\n"+
				"			int f = 0, K = 0;\n"+
				"			int bufferIndex = j;\n"+
				"\n"+
				"			if (0 <= j && j <= 15) {\n"+
				"				f = (b & c) | (~b & d);\n"+
				"			} else if (16 <= j && j <= 31) {\n"+
				"				f = (b & c) | (b & d) | (c & d);\n"+
				"				K = 0x5A827999;\n"+
				"				\n"+
				"				bufferIndex = ((bufferIndex - 16) / 4) + 4 * (bufferIndex % 4);\n"+
				"			} else if (32 <= j && j <= 47) {\n"+
				"				f = b ^ c ^ d;\n"+
				"				K = 0x6ED9EBA1;\n"+
				"				int r = bufferIndex - 32;\n"+
				"				int p = (r % 4 == 0) ? 0 : (r % 4 == 1) ? 8 : (r % 4 == 2) ? 4 : 12;\n"+
				"				r /= 4;\n"+
				"				r = (r == 0) ? 0 : (r == 1) ? 2 : (r == 2) ? 1 : 3;\n"+
				"				bufferIndex = r + p;\n"+
				"			}\n"+
				"			int temp = Integer.rotateLeft(a + f + buffer[bufferIndex] + K, SHIFT_AMTS[j]);\n"+
				"			a = d;\n"+
				"			d = c;\n"+
				"			c = b;\n"+
				"			b = temp;\n"+
				"		}\n"+
				"\n"+
				"		a += originalA;\n"+
				"		b += originalB;\n"+
				"		c += originalC;\n"+
				"		d += originalD;\n"+
				"	}\n"+
				"	byte[] md5 = concatenateABCD(a, b, c, d);\n"+
				"	return md5;\n"+
				"}\n";
    }
 
    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_HASHING);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }
    
    public class WordRect {
    	
    	public WordRect() {
    		
    	}
    }

}