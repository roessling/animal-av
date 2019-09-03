package generators.maths.newtonpolynomial;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class NewtonPolynomialAnimGenerator implements Generator {
	private Language lang;
	private SourceCodeProperties interpolationsPolynomCodeProperties;
	private MatrixProperties gammaMatrixProperties;
	private MatrixProperties stuetzstellenMatrixProperties;
	private TextProperties newtonPolynomProperties;
	private TextProperties rechnungProperties;
	private SourceCodeProperties dividierteDifferenzenCodeProperties;
	private MatrixProperties dividierteDifferenzenMatrixProperties;
	private String[][] stuetzstellenMatrix;
	private SourceCodeProperties newtonInterpolationCodeProperties;

	public void init() {
		lang = new AnimalScript("Newtonsche Interpolationsformel mit Schema der dividierten Differenzen",
				"Christian Weinert, Simon Reuß", 1280, 1024);
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		newtonInterpolationCodeProperties = (SourceCodeProperties) props
				.getPropertiesByName("newtonInterpolationCodeProperties");

		stuetzstellenMatrix = (String[][]) primitives.get("stuetzstellenMatrix");
		stuetzstellenMatrixProperties = (MatrixProperties) props.getPropertiesByName("stuetzstellenMatrixProperties");

		dividierteDifferenzenMatrixProperties = (MatrixProperties) props
				.getPropertiesByName("dividierteDifferenzenMatrixProperties");
		dividierteDifferenzenCodeProperties = (SourceCodeProperties) props
				.getPropertiesByName("dividierteDifferenzenCodeProperties");

		rechnungProperties = (TextProperties) props.getPropertiesByName("rechnungProperties");

		gammaMatrixProperties = (MatrixProperties) props.getPropertiesByName("gammaMatrixProperties");

		interpolationsPolynomCodeProperties = (SourceCodeProperties) props
				.getPropertiesByName("interpolationsPolynomCodeProperties");

		newtonPolynomProperties = (TextProperties) props.getPropertiesByName("newtonPolynomProperties");

		if (stuetzstellenMatrix[0].length != 2) {
			lang.newText(
					new Coordinates(20, 30),
					"Dieser Algorithmus akzeptiert nur zweispaltige Matrizen als Eingabe für die Stützstellen, bitte geben Sie beim nächsten Versuch nur gültige Stützstellen an",
					"Fehlermeldung", null);
		} else {
			try {
				double[][] result = new double[stuetzstellenMatrix.length][2];

				for (int i = 0; i < stuetzstellenMatrix.length; i++) {
					result[i][0] = Double.parseDouble(stuetzstellenMatrix[i][0]);
					result[i][1] = Double.parseDouble(stuetzstellenMatrix[i][1]);
				}

				NewtonPolynomialAnim newtonPolynomialAnim = new NewtonPolynomialAnim(lang, new SamplingPoints(result),
						newtonInterpolationCodeProperties, stuetzstellenMatrixProperties,
						dividierteDifferenzenMatrixProperties, dividierteDifferenzenCodeProperties, rechnungProperties,
						gammaMatrixProperties, interpolationsPolynomCodeProperties, newtonPolynomProperties);
				newtonPolynomialAnim.calc();
			} catch (NumberFormatException e) {
				lang.newText(
						new Coordinates(20, 30),
						"Dieser Algorithmus akzeptiert nur zweispaltige Matrizen als Eingabe für die Stützstellen, bitte geben Sie beim nächsten Versuch nur gültige Stützstellen an",
						"Fehlermeldung", null);
			}
		}

		lang.finalizeGeneration();
		return lang.toString();
	}

	public String getName() {
		return "Newtonsche Interpolationsformel mit Schema der dividierten Differenzen";
	}

	public String getAlgorithmName() {
		return "Newtonsche Interpolationsformel mit Schema der dividierten Differenzen";
	}

	public String getAnimationAuthor() {
		return "Christian Weinert, Simon Reuß";
	}

	public String getDescription() {
		return "Bei der Eingabe von n + 1 paarweise verschiedenen St&uuml;tzstellen (x<sub>0</sub>, y<sub>0</sub>), ..., (x<sub>n</sub>, y<sub>n</sub>) liefert die Newtonsche Interpolationsformel ein Polynom p<sub>n</sub>(x) n-ten Grades, f&uuml;r das p<sub>n</sub>(x<sub>i</sub>) = y<sub>i</sub> gilt."
				+ "<br />"
				+ "Das Polynom p<sub>n</sub>(x) entspricht dabei der Newtonschen Darstellung, d.h. es ist wie folgt aufgebaut:"
				+ "<br />"
				+ "p<sub>n</sub>(x) = &gamma;<sub>0</sub> + &gamma;<sub>1</sub> * (x - x<sub>0</sub>) + &gamma;<sub>2</sub> * (x - x<sub>0</sub>) * (x - x<sub>1</sub>) + ... + &gamma;_n * (x - x<sub>0</sub>) * (x - x<sub>1</sub>) * ... * (x - x<sub>n - 1</sub>) "
				+ "<br />"
				+ "Dabei bezeichnet man f<sub>x_{0}, ..., x_{i}</sub> := &gamma;<sub>i</sub> als die i-te dividierte Differenz. Diese Differenzen lassen sich &uuml;ber folgende Rekursion effizient berechnen:"
				+ "<br />"
				+ "f<sub>x_{i}</sub> = y<sub>i</sub> f&uuml;r i = 0, ..., n"
				+ "<br />"
				+ "f<sub>x_{j}, ..., x_{j + i}</sub> = (f<sub>x_{j + 1}, ..., x_{j + i}</sub> - f<sub>x_{j}, ..., x_{j + i - 1}</sub>) / (x<sub>j + i</sub> - x<sub>j</sub>) f&uuml;r i = 1, ..., n und j = 0, ..., n - i";
	}

	public String getCodeExample() {
		return "NewtonInterpolation(st&uuml;tzstellen)"
				+ "\n"
				+ "	&gamma; = DividierteDifferenzen(st&uuml;tzstellen)"
				+ "\n"
				+ "	p_{n}(x) = Interpolationspolynom(&gamma;)"
				+ "\n"
				+ "	return p_{n}(x)"
				+ "\n"
				+ "\n"
				+ "DividierteDifferenzen(st&uuml;tzstellen)"
				+ "\n"
				+ "	for i = 0 to n"
				+ "\n"
				+ "		f_{x_{i}} = y_{i}"
				+ "\n"
				+ "	for i = 1 to n"
				+ "\n"
				+ "		for j = 0 to n - i"
				+ "\n"
				+ "			f_{x_{j}, ..., x_{j + i}} = (f_{x_{j + 1}, ..., x_{j + i}} - f_{x_{j}, ..., x_{j + i - 1}}) / (x_{j + i} - x_{j})"
				+ "\n" + "	return f_{x_{0}}, ..., f_{x_{0}, ..., x_{n}} as &gamma;_{0}, ..., &gamma;_{n}" + "\n" + "\n"
				+ "Interpolationspolynom(&gamma;)" + "\n" + "	p_{n}(x) = &gamma;_0" + "\n" + "	for i = 1 to n" + "\n"
				+ "		p_{n}(x) = p_{n}(x) + &gamma;_{i} * (x - x_{0}) * ... * (x - x_{i - 1})" + "\n"
				+ "	return p_{n}(x)";
	}

	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

}
