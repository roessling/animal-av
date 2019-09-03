package generators.cryptography.rsa;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.math.BigInteger;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Random;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class RSAMO implements Generator {
	
	Random random = new Random(System.currentTimeMillis());
	
	final String[] PseudoCodeLines = {"calculate public and private key", 
			"choose p and q randomly",
			"calculate the RSA-Modul ",
			"calculate the euclidean function of n",
			"choose the public key e randomly",
			"calculate the private key d",
			"encrypt message",
			"decrypt message"};
	
	final String[] PseudoCodeLinesEuclid = 
		   {"Input : a=phi(n), b=public key", 
			"Output: g = gcd(a,b) and private key d",
			"",
			"x2 = 1; x1 = 0; y2 = 0; y1 = 1;", 
			"while b > 1",
			"q = a div b; r = a mod b; x = x2 - qx1; y = y2 - qy1;",
			"a = b; b = r; x2 = x1; x1 = x; y2 = y1; y1 = y;",
			"end while",
			"if y1 < 0 then y1 = y1 + phi(n);",
			"g = b; d = y1"};
	
	final int[] CodeLineIndentionDephts = {1, 2, 2, 2, 2, 2, 1, 1};
	final int[] CodeLineIndentionDephtsEuclid = {1, 1, 1, 1, 1, 2, 2, 1, 1, 1};
	
	final String[][] CodeDescriptions = {
			{"The private and public key ",
				"will be calculates for later ",
				"encryption and decryption."},
			{"The factors p and q are",
				"used for calculating",
				"the keys. Usually they",
				"are primes and should be",
				"stochasticaly independent."},
			{"n = p * q"},
			{"phi(n) = (p - 1) * (q - 1)"},
			{"The division of e and phi(n)",
				"shall be <teilerfremd> and:",
				"1 < e < phi(n)."},			
			{"The private key will be",
				"calculated with the",
				"extended euclidean",
				"algorithmn."},
			{"Encrypt message with public" ,
				"key: c=(m^e) mod n"},
			{"Decrypt message with private",
				"key: m=(c^d) mod n"}}; 
	
	final String[] DescriptionLines = 
		   {"RSA is a asymetric cryptosystem which is used for encryption ",
			"and for digital signatures. p and q are prime numbers, which are",
			"choosen randomly and multiplied resulting in the modulus n. ",
			"By considering the euler function phi(n) the public and",
			"private key is calculated using the euklid alogrithm. Finally",
			"the animation shows the en- and decryption of a chosen mesage m.",
			"If the bitlength of m exceeds the bitlength of the modulus n, the",
			"animation dynamically adapts the systemwide used bitlength for p,q",
			"and n to the bitlength of m.",
			"Try out increasing the bitlength! But don't overstate it! RSA 1024",
			"bit took 20 minutes to parse..."};
	
	//RSA elements
	int BitLength = 0;
	BigInteger m = new BigInteger("0");
	BigInteger p = new BigInteger("0");	
	BigInteger q = new BigInteger("0");
	BigInteger n = new BigInteger("0");
	BigInteger e = new BigInteger("0");	//public key
	BigInteger d = new BigInteger("0");	//private key
	
	//Animal elements
	private Language	lang = null;
	
	Text headerText = null;
	Rect headerRect = null;
	Text textBitLength = null;
	Text textP = null;
	Text textQ = null;
	Text textN = null;
	Text textFN = null;
	Text textE = null;
	Text textD = null;
	Text textM = null;
	Text textEnc = null;
	Text textDec = null;
	
	final int	MaxCodeDescLines = 8;
	Text[]		codeDescription = new Text[MaxCodeDescLines];
	int			currentCodeDescLength = 0;
	
	SourceCode	sourceCode = null;
	SourceCode  sourceCodeEuclid = null;
	int			sourceCodePos = 0;
	Polyline 	hLine = null;
	Polyline 	vLine = null;
	
	
	
	private void initCalculation(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) {
		Node[] verticesH = {new Coordinates(0, 500), new Coordinates(2000, 500)};
		Node[] verticesV = {new Coordinates(550, 500), new Coordinates(550, 1500)};
		hLine = lang.newPolyline(verticesH, "hLine", null);
		vLine = lang.newPolyline(verticesV, "vLine", null);
		
		sourceCode = lang.newSourceCode(new Offset(0, 10, hLine, "W"), "sourceCode",
				null, (SourceCodeProperties) arg0.getPropertiesByName("sourcecodeProp"));
		
		for(int i = 0; i < PseudoCodeLines.length; i++) {
			sourceCode.addCodeLine(PseudoCodeLines[i], null, CodeLineIndentionDephts[i], null);
		}
		
		for(int i = 0; i < MaxCodeDescLines; i++) {
			Node position;
			if(i == 0) {
				position = new Offset(10, 20, vLine, "N");
			}
			else {
				position = new Offset(0, 1, codeDescription[i - 1], "SW");
			}
			
			codeDescription[i] = lang.newText(position, "", "codeDescription" + i, 
					null, (TextProperties) arg0.getPropertiesByName("textProp"));
		}
		
		highlightCodeline(0);
		
		textBitLength = lang.newText(new Offset(0, 80, headerRect, "SW"), "Bit    = " + BitLength, 
				"textBitLength", null, (TextProperties) arg0.getPropertiesByName("textProp"));
		
		lang.nextStep("key calculation");
		
		textP = lang.newText(new Offset(0, 1, textBitLength, "SW"), "", 
				"textP", null, (TextProperties) arg0.getPropertiesByName("textProp"));
		textQ = lang.newText(new Offset(0, 1, textP, "SW"), "", 
				"textQ", null, (TextProperties) arg0.getPropertiesByName("textProp"));
		textN = lang.newText(new Offset(0, 1, textQ, "SW"), "", 
				"textN", null, (TextProperties) arg0.getPropertiesByName("textProp"));
		textFN = lang.newText(new Offset(0, 1, textN, "SW"), "", 
				"textFN", null, (TextProperties) arg0.getPropertiesByName("textProp"));
		textE = lang.newText(new Offset(0, 1, textFN, "SW"), "", 
				"textE", null, (TextProperties) arg0.getPropertiesByName("textProp"));
		textD = lang.newText(new Offset(0, 1, textE, "SW"), "", 
				"textD", null, (TextProperties) arg0.getPropertiesByName("textProp"));
		textM = lang.newText(new Offset(0, 10, textD, "SW"), "", 
				"textM", null, (TextProperties) arg0.getPropertiesByName("textProp"));
		textEnc = lang.newText(new Offset(0, 1, textM, "SW"), "", 
				"textEnc", null, (TextProperties) arg0.getPropertiesByName("textProp"));
		textDec = lang.newText(new Offset(0, 1, textEnc, "SW"), "", 
				"textDec", null, (TextProperties) arg0.getPropertiesByName("textProp"));
	}
	
	private void highlightCodeline(int index) {
		sourceCode.unhighlight(sourceCodePos);
		sourceCode.highlight(index);
		sourceCodePos = index;
		
		//reset description
		for(int i = 0; i < currentCodeDescLength; i++) {
			codeDescription[i].setText("", new TicksTiming(0), new TicksTiming(0));
		}
		
		//set new Description
		currentCodeDescLength = 0;
		for(String descLine : CodeDescriptions[index]) {
			codeDescription[currentCodeDescLength].setText(descLine, new TicksTiming(0), new TicksTiming(0));
			currentCodeDescLength++;
		}
	}
	
	private void showInitScreen(AnimationPropertiesContainer arg0) {
		headerText = lang.newText(new Coordinates(20, 30), getAlgorithmName(), 
				"header", null, (TextProperties) arg0.getPropertiesByName("headerProp"));

		headerRect = lang.newRect(new Offset(-5, -5, headerText, "NW"), new Offset(5, 5, headerText, "SE"), 
				"headerRect", null, (RectProperties) arg0.getPropertiesByName("rectProp"));
		
		Text[] introText = new Text[DescriptionLines.length];
		for(int i = 0; i < DescriptionLines.length; i++) {
			Node position;
			if(i == 0) {
				position = new Offset(0, 100, headerRect, "SW");
			}
			else {
				position = new Offset(0, 5, introText[i - 1], "SW");
			}
			
			introText[i] = lang.newText(position, DescriptionLines[i], "introText" + i, 
					null, (TextProperties) arg0.getPropertiesByName("textProp"));
		}
		lang.nextStep("InitScreen");
		
		for(Text text : introText) {
			text.hide();
		}
	}
	
	private BigInteger extendedEuclid(BigInteger a, BigInteger b, AnimationPropertiesContainer arg0) 
	{
		BigInteger a2 = a;
    BigInteger fn = a2;
		Text headerTextEuclid = lang.newText(new Coordinates(20, 30), "Euclid algorithm", 
				"headerEuclid", null, (TextProperties) arg0.getPropertiesByName("headerProp"));

		Rect headerRectEuclid = lang.newRect(new Offset(-5, -5, headerTextEuclid, "NW"), new Offset(5, 5, headerTextEuclid, "SE"), 
				"headerRectEuclid", null, (RectProperties) arg0.getPropertiesByName("rectProp"));
		
		sourceCodeEuclid = lang.newSourceCode(new Offset(0, 0, headerRectEuclid, "SW"), "sourceCodeEuclid",
				null, (SourceCodeProperties) arg0.getPropertiesByName("sourcecodeProp"));
		
		for(int i = 0; i < PseudoCodeLinesEuclid.length; i++) {
			sourceCodeEuclid.addCodeLine(PseudoCodeLinesEuclid[i], null, CodeLineIndentionDephtsEuclid[i], null);
		}
		
		Node[] verticesHEuclid = {new Offset(-100, 10, sourceCodeEuclid, "SW"), new Offset(2000, 10, sourceCodeEuclid, "SE")};
		Polyline hLineEuclid = lang.newPolyline(verticesHEuclid, "hLineEuclid", null);
		
		BigInteger b2 = b;
    int tableCnt = calcEuclidTable(a2, b2);
		String[][] euclidMatrix = new String[tableCnt][10];
		euclidMatrix[0][0] = "q                ";
		euclidMatrix[0][1] = "r                ";
		euclidMatrix[0][2] = "x                ";
		euclidMatrix[0][3] = "y                ";
		euclidMatrix[0][4] = "a                ";
		euclidMatrix[0][5] = "b                ";
		euclidMatrix[0][6] = "x2               ";
		euclidMatrix[0][7] = "x1               ";
		euclidMatrix[0][8] = "y2               ";
		euclidMatrix[0][9] = "y1               ";
		
		for (int i = 1; i < tableCnt; i++)
		{
			for (int j = 0; j < 10; j++)
			{
				euclidMatrix[i][j] = "";
		
			}
		}
		
		StringMatrix ma = lang.newStringMatrix(new Offset(0, 20, sourceCodeEuclid, "SW"), euclidMatrix, "table", null, (MatrixProperties) arg0.getPropertiesByName("mprop"));
//		BigInteger d = new BigInteger("-1"), 
		BigInteger   q = new BigInteger("-1"), 
		   r = new BigInteger("-1"), 
		   x = new BigInteger("-1"), 
		   y = new BigInteger("-1"), 
		   x2 = new BigInteger("1"), 
		   x1 = new BigInteger("0"), 
		   y2 = new BigInteger("0"), 
		   y1 = new BigInteger("1");

		int matrixCnt = 1;
		ma.put(matrixCnt, 4, exponentenSchreibweise(a2), new TicksTiming(0), new TicksTiming(40));
		ma.put(matrixCnt, 5, exponentenSchreibweise(b2), new TicksTiming(0), new TicksTiming(40));
		ma.put(matrixCnt, 6, exponentenSchreibweise(x2), new TicksTiming(0), new TicksTiming(40));
		ma.put(matrixCnt, 7, exponentenSchreibweise(x1), new TicksTiming(0), new TicksTiming(40));
		ma.put(matrixCnt, 8, exponentenSchreibweise(y2), new TicksTiming(0), new TicksTiming(40));
		ma.put(matrixCnt, 9, exponentenSchreibweise(y1), new TicksTiming(0), new TicksTiming(40));
		sourceCodeEuclid.highlight(0);
		sourceCodeEuclid.highlight(3);
		lang.nextStep("Init euclid-table");
		sourceCodeEuclid.unhighlight(0);
		sourceCodeEuclid.unhighlight(3);
		
		while (b2.compareTo(new BigInteger("1")) == 1)
		{
			q = a2.divide(b2);
			r = a2.mod(b2);
			x = x2.subtract(q.multiply(x1));
			y = y2.subtract(q.multiply(y1));
			a2 = b2;
			b2 = r;
			x2 = x1;
			x1 = x;
			y2 = y1;
			y1 = y;
			ma.put(matrixCnt, 0, exponentenSchreibweise(q), new TicksTiming(0), new TicksTiming(40));
			ma.put(matrixCnt, 1, exponentenSchreibweise(r), new TicksTiming(0), new TicksTiming(40));
			ma.put(matrixCnt, 2, exponentenSchreibweise(x), new TicksTiming(0), new TicksTiming(40));
			System.out.println(y);
			ma.put(matrixCnt, 3, exponentenSchreibweise(y), new TicksTiming(0), new TicksTiming(40));
			sourceCodeEuclid.highlight(5);
			lang.nextStep("Euclid: q,r,x,y");
			sourceCodeEuclid.unhighlight(5);
			matrixCnt++;
			ma.put(matrixCnt, 4, exponentenSchreibweise(a2), new TicksTiming(0), new TicksTiming(40));
			ma.put(matrixCnt, 5, exponentenSchreibweise(b2), new TicksTiming(0), new TicksTiming(40));
			ma.put(matrixCnt, 6, exponentenSchreibweise(x2), new TicksTiming(0), new TicksTiming(40));
			ma.put(matrixCnt, 7, exponentenSchreibweise(x1), new TicksTiming(0), new TicksTiming(40));
			ma.put(matrixCnt, 8, exponentenSchreibweise(y2), new TicksTiming(0), new TicksTiming(40));
			ma.put(matrixCnt, 9, exponentenSchreibweise(y1), new TicksTiming(0), new TicksTiming(40));
			sourceCodeEuclid.highlight(6);
			lang.nextStep("Euclid: a,b,x2,x1,y2,y1");
			sourceCodeEuclid.unhighlight(6);
		}
		d = a2;
		x = x2;
		y = y2;
		
		if (y1.compareTo(new BigInteger("0")) < 0)
		{
			sourceCodeEuclid.highlight(8);
			ma.put(matrixCnt, 9, exponentenSchreibweise(y1.add(fn)), new TicksTiming(0), new TicksTiming(40));
			lang.nextStep("Euclid: add phi(n) to y1");
			sourceCodeEuclid.unhighlight(8);
		}
		sourceCodeEuclid.highlight(1);
		sourceCodeEuclid.highlight(9);
		ma.highlightElem(matrixCnt, 5, new TicksTiming(0), new TicksTiming(40));
		ma.highlightElem(matrixCnt, 9, new TicksTiming(0), new TicksTiming(40));
		lang.nextStep("Euclid: g,d");
		sourceCodeEuclid.unhighlight(1);
		sourceCodeEuclid.unhighlight(9);
		ma.unhighlightElem(matrixCnt, 5, new TicksTiming(0), new TicksTiming(40));
		ma.unhighlightElem(matrixCnt, 9, new TicksTiming(0), new TicksTiming(40));
		
		headerTextEuclid.hide();
		headerRectEuclid.hide();
		sourceCodeEuclid.hide();
		hLineEuclid.hide();
		ma.hide();
		return y1;
	}
	
	public int calcEuclidTable(BigInteger a, BigInteger b)
	{
		int cnt = 3;
		BigInteger r = new BigInteger("-1");
    BigInteger a2 = a, b2 = b;
		
		while (b2.compareTo(new BigInteger("1")) == 1)
		{
				r = a2.mod(b);
        a2 = b;
				b2 = r;
				cnt++;
		}
		return cnt;
		
	}
	
	private String exponentenSchreibweise(BigInteger zahl)
	{
		if (zahl.abs().compareTo(new BigInteger("10000")) < 0)
		{
			return zahl.toString();
		}
		if (zahl.compareTo(new BigInteger("0")) < 0)
		{
			double log10 = Math.log10(zahl.abs().doubleValue());
			int exponent = (int)log10;
			Double mantissa = Math.pow(10, log10  - exponent);
			String tmp = "-" + mantissa.toString().substring(0, 4) + "e+" + exponent;
		    return tmp;
		}
		double log10 = Math.log10(zahl.doubleValue());
		int exponent = (int)log10;
		Double mantissa = Math.pow(10, log10  - exponent);
		String tmp = mantissa.toString().substring(0, 4) + "e+" + exponent;
	    return tmp;
	}
	
	private void hideAll()
	{
		headerText.hide();
		headerRect.hide();
		sourceCode.hide();
		vLine.hide();
		hLine.hide();
		textBitLength.hide();
		textD.hide();
		textE.hide();
		textP.hide();
		textQ.hide();
		textFN.hide();
		textN.hide();
		for(Text line : codeDescription)
		{
			line.hide();
		}
	}
	
	private void showAll()
	{
		headerText.show();
		headerRect.show();
		sourceCode.show();
		vLine.show();
		hLine.show();
		textBitLength.show();
		textD.show();
		textE.show();
		textP.show();
		textQ.show();
		textFN.show();
		textN.show();
		for(Text line : codeDescription)
		{
			line.show();
		}
	}
	
	private void calcNewKeys(AnimationPropertiesContainer arg0) {
		
		p = BigInteger.probablePrime(BitLength, random);
		do {
			q = BigInteger.probablePrime(BitLength, random);
		} while(p.compareTo(q) == 0);
		
		n = p.multiply(q);
		if(m.bitLength() > n.bitLength())
		{
			BitLength = m.bitLength();
			p = BigInteger.probablePrime(BitLength, random);
			do {
				q = BigInteger.probablePrime(BitLength, random);
			} while(p.compareTo(q) == 0);
			
			n = p.multiply(q);
			textBitLength.setText("Bit    = " + BitLength + " changed in due to length of message", new TicksTiming(0), new TicksTiming(40));
		}
		
		//p = new BigInteger("5");
		//q = new BigInteger("7");
		//n = p.multiply(q);
		
		textP.setText("p      = " + p, new TicksTiming(0), new TicksTiming(40));
		textQ.setText("q      = " + q, new TicksTiming(0), new TicksTiming(40));
		
		highlightCodeline(1);
		lang.nextStep("key calculation: p and q");
		
		textN.setText("n      = " + n, new TicksTiming(0), new TicksTiming(40));
		highlightCodeline(2);
		lang.nextStep("key calculation: n");
		
		final BigInteger One = new BigInteger("1");
		BigInteger fn = p.subtract(One).multiply(q.subtract(One));
		textFN.setText("phi(n) = " + fn, new TicksTiming(0), new TicksTiming(40));
		highlightCodeline(3);
		lang.nextStep("key calculation: phi(n)");
		
		while(true) {
			e = BigInteger.probablePrime(BitLength, random);
			//e = new BigInteger("5");
			if(e.gcd(fn).compareTo(One) == 0 
					&& (e.compareTo(fn) < 0)
					&& (e.compareTo(One) > 0)) {
				break;
			}
		}
		textE.setText("e      = " + e, new TicksTiming(0), new TicksTiming(40));
		highlightCodeline(4);
		lang.nextStep("key calculation: public key");
		hideAll();
		
		d = extendedEuclid(fn, e, arg0);
		showAll();
		if (d.compareTo(new BigInteger("0")) == -1)
		{
			d = d.add(fn);
		}
		textD.setText("d      = " + d, new TicksTiming(0), new TicksTiming(40));
		highlightCodeline(5);
		lang.nextStep("key calculation: private key");
	}
	
	private void enDecrypt()
	{
		textM.setText("m      = " + m, new TicksTiming(0), new TicksTiming(40));
		BigInteger encryptedMsg = encrypt(m);
		BigInteger decryptedMsg = decrypt(encryptedMsg);
		lang.nextStep("show m");
		textEnc.setText("Enc    = " + encryptedMsg, new TicksTiming(0), new TicksTiming(40));
		highlightCodeline(6);
		lang.nextStep("encryption");
		textDec.setText("Dec    = " + decryptedMsg, new TicksTiming(0), new TicksTiming(40));
		highlightCodeline(7);
		lang.nextStep("decryption");
		sourceCode.unhighlight(7);
	}
	
	private BigInteger encrypt(BigInteger msg) {
		return msg.modPow(e, n);
	}
	
	private BigInteger decrypt(BigInteger msg) {
		return msg.modPow(d, n);
	}

	@Override
	public String generate(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) {
		init();
		
		m = new BigInteger((String) arg1.get("Message"));
		BitLength = (Integer) arg1.get("BitLength");
		
		showInitScreen(arg0);
		initCalculation(arg0, arg1);		
		calcNewKeys(arg0);
		enDecrypt();
//		System.out.println("BitLength = " + BitLength);
//		System.out.println("p = " + p);
//		System.out.println("q = " + q);
//		System.out.println("n = " + n);
//		System.out.println("e = " + e);
//		System.out.println("d = " + d);
//		System.out.println("encrypt: " + encryptedMsg);
//		System.out.println("decrypt: " + decryptedMsg);
//		System.out.println("Bitlength: " + d.bitLength());

		return lang.toString();
	}

	@Override
	public String getAlgorithmName() {
		return "RSA";
	}

	@Override
	public String getAnimationAuthor() {
		return "Nico Gerwien, Martin Olschowski";
	}

	@Override
	public String getCodeExample() {
		String code = "";
		for(int i = 0; i < PseudoCodeLines.length; i++) {
			for(int j = 0; j < CodeLineIndentionDephts[i]; j++) {
				code += "    ";
			}
			code += PseudoCodeLines[i] + "\n";
		}
		
		return code;
	}

	@Override
	public Locale getContentLocale() {
		return Locale.US;
	}

	@Override
	public String getDescription() {
		String desc = "";
		for(String line : DescriptionLines) {
			desc += line;
		}
		
		return desc;
	}

	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
	}

	@Override
	public String getName() {
		return "RSA";
	}

	@Override
	public String getOutputLanguage() {
		return generators.framework.Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public void init() {
		lang = new AnimalScript(getAlgorithmName(), getAnimationAuthor(), 640, 480);
		lang.setStepMode(true);
	}
}
