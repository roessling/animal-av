package generators.cryptography.rsa;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.Numerical;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class RSA extends AnnotatedAlgorithm implements Generator {

	private String assi = "Assignments";
	private String comp = "Compares";
	private Text infobox = null;

	AnimationPropertiesContainer props = new AnimationPropertiesContainer();
	Hashtable<String, Object> ht = new Hashtable<String, Object>();

	@Override
	public String getAnimationAuthor() {
		return "Tim Biedenkapp, Sinem Emeröz";
	}

	public String getOutputLanguage() {
	    return Generator.JAVA_OUTPUT;
	}

	public String getAnnotatedSrc() {

		return "p ist die nächste Primzahl zum Startwert																			@label(\"getp\") @declare(\"int\", \"p\")\n"
				+ "q ist die nächste Primzahl zum zweiten Startwert und ungleich der ersten Primzahl								@label(\"getq\") @declare(\"int\", \"q\")\n"
				+ "n = p * q																										@label(\"getn\") @inc(\""+ assi + "\") @declare(\"int\", \"n\")\n"
				+ "fi(n) = (p-1)*(q-1), da p und q jeweils Primzahlen sind															@label(\"getfi\") @inc(\""+ comp+ "\") @declare(\"int\", \"fi\")\n"
				+ "Wähle eine zu fi(N) teilerfremde Zahl e, für die gilt 1 < e < fi(N)       										@label(\"gete\") @declare(\"int\", \"e\") @inc(\""+ comp+ "\")\n"
				+ "Berechne den Entschlüsselungsexponenten d  als Multiplikativ Inverses von e bezüglich des Moduls fi(N) e*d mod 1	@label(\"getd\") @declare(\"int\", \"d\") @inc(\""+ comp+ "\")\n"
				+ "Prüfe, ob e*d mod 1 wirklich gilt																				@label(\"gettest\") @declare(\"int\", \"test\") @inc(\""+ comp+ "\")\n"
				+ "c = m^e mod n																									@label(\"getencrypt\") @inc(\""+ assi+ "\") @declare(\"int\", \"encryptmessage\")\n"
				+ "m = c^d mod n																									@label(\"getdecrypt\") @inc(\""+ assi + "\") @declare(\"int\", \"decryptmessage\")\n";
		}

	/**
	 * initalization method
	 */
	public void init() {
		super.init();

		Font font = new Font("SansSerif", Font.PLAIN, 16);
		
		SourceCodeProperties props = new SourceCodeProperties();
		props.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
		props.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);
		props.set(AnimationPropertiesKeys.FONT_PROPERTY, font);

		
		// instantiate source code primitive to work with
		sourceCode = lang.newSourceCode(new Coordinates(20, 10), "sumupCode",
				null, props);
		

		// parsing anwerfen
		parse();
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		init();

		// init all vars used in the visualization
		vars.declare("int", assi, "0");
		vars.setGlobal(assi);
		vars.declare("int", comp, "0");
		vars.setGlobal(comp);

		// fetch provided parameter from Framework
		int p = 0, q = 0, message = 0;
		if (primitives.get("p") != null)
			p = ((Integer) primitives.get("p")).intValue();
		else
			p = 256;
		
		if (primitives.get("q") != null)
			q = ((Integer) primitives.get("q")).intValue();
		else
			q = 256;
		
		if (primitives.get("message") != null)
			message = ((Integer) primitives.get("message")).intValue();
		else
			message = 256;

		rsa(p, q, message);
		return lang.toString();
	}

	private void rsa(int inputp, int inputq, int message) {
		long p, q, n, fi, e, d, valid;
		vars.declare("int", "p", String.valueOf(inputp));
		vars.declare("int", "q", String.valueOf(inputq));
		vars.declare("int", "message", String.valueOf(message));
		vars.declare("int", "n", "0");
		vars.declare("int", "fi", "0");
		vars.declare("int", "e", "0");
		vars.declare("int", "d", "0");
		vars.declare("int", "test", "0");
		vars.declare("int", "encryptmessage", "0");
		vars.declare("int", "decryptmessage", "0");
		
		lang.nextStep();
		Font font = new Font("SansSerif", Font.PLAIN, 16);
		infobox = lang.newText(new Offset(0, 18, sourceCode, "SW"), "", "infobox", null);
		infobox.setFont(font, null, null);
		infobox.show();

		exec("getp");
		for (p = inputp; !Numerical.isPrime(p); p++)
			;
		
		infobox.setText("Die erste Primzahl P, passend zum Startwert bestimmen", null, null);
		
		vars.set("p", String.valueOf(p));
		lang.nextStep();

		exec("getq");
		for (q = inputq + 2; !Numerical.isPrime(q); q++)
			;
		
		infobox.setText("Die zweite Primzahl Q, passend zum Startwert bestimmen", null, null);
		vars.set("q", String.valueOf(q));
		lang.nextStep();

		exec("getn");
		n = p * q;
		infobox.setText("Das RSA Modul n setzt sich aus den beiden Primzahlen P und Q zusammen", null, null);
		vars.set("n", String.valueOf(n));
		lang.nextStep();

		exec("getfi");
		fi = (p - 1) * (q - 1);
		infobox.setText("Berechne die Eulersche fi-Funktion von N \n Die eulersche fi-Funktion (auch eulersche Funktion genannt) ist eine zahlentheoretische Funktion. Sie gibt für jede natürliche Zahl n an, wie viele positive ganze Zahlen a < n zu ihr teilerfremd sind:", null, null);
		vars.set("fi", String.valueOf(fi));
		lang.nextStep();

		exec("gete");
		for (e = fi / 10; Numerical.gcd(e, fi) != 1; e++)
			;
		infobox.setText("Wähle eine zu fi(N) teilerfremde Zahl e, für die gilt 1 < e < fi(N).", null, null);
		vars.set("e", String.valueOf(e));
		lang.nextStep();

		exec("getd");
		d = Numerical.inverse(e, fi);
		infobox.setText("Berechne den Entschlüsselungsexponenten d als Multiplikativ Inverses von e bezüglich des Moduls fi(N).", null, null);
		vars.set("d", String.valueOf(d));
		lang.nextStep();

		exec("gettest");
		valid = e * d % (fi);
		infobox.setText("Teste ob e und d Teilerfremd sind", null, null);
		vars.set("test", String.valueOf(valid));
		lang.nextStep();

		exec("getencrypt");
		long code = Numerical.power(message, e, n);
		infobox.setText("verschlüsseln der Nachricht durch message ^ e MOD n", null, null);
		vars.set("encryptmessage", String.valueOf(code));
		lang.nextStep();

		exec("getdecrypt");
		long decode = Numerical.power(code, d, n);
		infobox.setText("entschlüsseln der Nachricht durch verschlüsselte message ^ d MOD n", null, null);
		vars.set("decryptmessage", String.valueOf(decode));
		lang.nextStep();

	}

	/**
	 * the locale of this animation
	 */
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	/**
	 * description of this animation
	 */
	public String getDescription() {
	    return "Diese Animation pr&auml;sentiert die RSA-Verschl&uuml;sselung einer gegebenen Nachricht.";
	}

	/**
	 * the type of this animation
	 */
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
	}

	/**
	 * title to get displayed
	 */
	public String getName() {
		return "RSA [annotation based]";
	}

	/**
	 * Name of the Algorithm
	 */
	public String getAlgorithmName() {
		return "RSA";
	}
}