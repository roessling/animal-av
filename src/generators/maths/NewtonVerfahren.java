package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;

public class NewtonVerfahren implements ValidatingGenerator {
	private Language lang;
	private double Startwert;
	private double Genauigkeit;
	private String Funktion;
	private int MaximaleAnzahlAnIterationen;
	private String ErsteAbleitung;
	private NewtonMethod nm;
	private PolylineProperties Graph;
	private PolylineProperties Hilfslinien;
	private SourceCodeProperties PseudoCodeEinstellungen;

	public void init() {
		lang = new AnimalScript("Das Newton-Verfahren",
				"Rene Roepke, Daniel Thul", 800, 600);
		nm = new NewtonMethod(lang);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		Startwert = (Double) primitives.get("Startwert");
		Genauigkeit = (Double) primitives.get("Genauigkeit");
		Funktion = (String) primitives.get("Funktion");
		MaximaleAnzahlAnIterationen = (Integer) primitives
				.get("MaximaleAnzahlAnIterationen");
		ErsteAbleitung = (String) primitives.get("ErsteAbleitung");
		PseudoCodeEinstellungen = (SourceCodeProperties)props.getPropertiesByName("PseudoCodeEinstellungen");
		Hilfslinien = (PolylineProperties)props.getPropertiesByName("Hilfslinien");
		Graph = (PolylineProperties)props.getPropertiesByName("Graph");
		if (validateInput(props,primitives)){
				lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
				nm.newton(Funktion, ErsteAbleitung, Startwert, Genauigkeit,
						MaximaleAnzahlAnIterationen, Graph,
						Hilfslinien,PseudoCodeEinstellungen);
				lang.finalizeGeneration();
		}
		return lang.toString();
	}

	public String getName() {
		return "Das Newton-Verfahren";
	}

	public String getAlgorithmName() {
		return "Das Newton-Verfahren";
	}

	public String getAnimationAuthor() {
		return "Rene Röpke, Daniel Thul";
	}

	public String getDescription() {
		return "Die folgende Animation zeigt, wie mit dem Newton-Verfahren iterativ eine Nullstelle einer Funktion grafisch und rechnerisch angenaehert werden kann. "
				+ "\n"
				+ "Die Idee des Newton-Verfahrens besteht darin, dass Funktionen in kleinen Bereichen gut durch ihre Tangenten angenaehert werden."
				+ "\n"
				+ "\n"
				+ "Dabei wird durch die Steigung an der Stelle x die naechste Stelle x' berechnet und sich so an die Nullstelle der Funktion angenaehert."
				+ "\n"
				+ "\n"
				+ "Bei der Wahl des Startwertes kann es jedoch passieren, dass man sich von der gesuchten  Nullstelle entfernt und das Verfahren divergiert."
				+ "\n"
				+ "\n"
				+ "Das Verfahren konvergiert nur bei der Wahl eines geeigneten Startwertes. Der Algorithmus terminiert nach Erreichen der maximalen"
				+ "\n" + "Iterationsanzahl oder der gewaehlten Genauigkeit."
				+ "\n"
				+ "\n"
				+ "Bei der Eingabe der Funktion und ersten Ableitung ist darauf zu achten, dass bei inkorrekter Eingabe der Graph nicht korrekt geplottet werden kann";
	}

	public String getCodeExample() {
		return "1. Setze x in die Vorschrift ein und berechne f(x) und f'(x)"
				+ "\n"
				+ "2. Berechne x'"
				+ "\n"
				+ "3. Pruefe, ob f(x') <= d ist"
				+ "\n"
				+ "    a) Wenn ja, dann weiter mit Schritt 5"
				+ "\n"
				+ "    b) Sonst weiter mit Schritt 4 aus"
				+ "\n"
				+ "4. Pruefe, ob n+1 < m ist,"
				+ "\n"
				+ "    a) Wenn ja, dann erhoehe n um 1 und weiter mit Schritt 1"
				+ "\n" + "    b) Sonst weiter mit Schritt 5 aus" + "\n"
				+ "5. Abbruch: Naeherungsloesung x''";
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

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		Startwert = (Double) primitives.get("Startwert");
		Genauigkeit = (Double) primitives.get("Genauigkeit");
		Funktion = (String) primitives.get("Funktion");
		MaximaleAnzahlAnIterationen = (Integer) primitives
				.get("MaximaleAnzahlAnIterationen");
		ErsteAbleitung = (String) primitives.get("ErsteAbleitung");
		if (MaximaleAnzahlAnIterationen <= 0) {
			throw new IllegalArgumentException(
					"Die maximale Anzahl an Iterationen darf nicht kleiner gleich 0 sein. Bitte korrigieren Sie ihre Eingaben und versuchen Sie es erneut.");
		} else if (!nm.validate(Funktion)) {
			throw new IllegalArgumentException(
					"Die eingegebene Funktion ist nicht im richtigen Format. Bitte korrigieren Sie ihre Eingaben und versuchen Sie es erneut.");
		} else if (!nm.validate(ErsteAbleitung)) {
			throw new IllegalArgumentException(
					"Die eingegebene erste Ableitung ist nicht im richtigen Format. Bitte korrigieren Sie ihre Eingaben und versuchen Sie es erneut.");
		} else
			return true;
	}

}