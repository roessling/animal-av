package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

/**
 * 
 * @author Steffen Frank Schmidt
 *
 */
public class HybridsortGenerator implements ValidatingGenerator {
	private Language lang;
	private int[] Eingabeliste;
	private int Eimeranzahl;

	public void init() {
		lang = new AnimalScript("Hybridsort [DE]", "Steffen Frank Schmidt",
				1200, 700);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		
		if (validateInput(props, primitives)){
		Eingabeliste = (int[]) primitives.get("Eingabeliste");
		Eimeranzahl = (Integer) primitives.get("Eimeranzahl");

		Hybridsort h = new Hybridsort(lang);

		h.sort(Eingabeliste, Eimeranzahl);
		}
		return lang.toString();
	}

	public String getName() {
		return "Hybridsort [DE]";
	}

	public String getAlgorithmName() {
		return "Hybridsort";
	}

	public String getAnimationAuthor() {
		return "Steffen Frank Schmidt";
	}

	public String getDescription() {
		return "Dieser Algorithmus sortiert eine Liste von Zahlen durch Bucketsort und Quicksort. Eine Alternative zu Quicksort ist ganz oft Heapsort. "
				+ "\n"
				+ "Der Algorithmus ist in zwei Teile einzuteilen: Aufteilen der Eingabeelement in n Eimer und Sortieren der einzelnen Eimer. "
				+ "\n"
				+ "\n"
				+ "Für Bucketsort wird zuerst das größte Element der Eingabeliste gesucht. Anschließend wird die Eingabeliste auf die n Eimer verteilt. Zur Aufteilung "
				+ "\n"
				+ "wird der Eimerindex für jedes Element berechnet:"
				+ "\n"
				+ "	(Eimer-)Index i = e / m * (n-1)"
				+ "\n"
				+ "Nach Aufteilung in die Eimer wird jeder Eimer mittels Quicksort sortiert. Die Sortierung mit Quicksort erfolgt durch Wahl eines Pivotelements "
				+ "\n"
				+ "(Element ganz rechts) und anschließendes Teilen des Liste anhand des Pivotelements. Anschließend wird jede Teilliste mit Quicksort sortiert. "
				+ "\n"
				+ "Man beachte, dass einelementige Listen immer sortiert sind."
				+ "\n"
				+ "\n"
				+ "Nach Sortierung aller Eimer mit Quicksort werden die sortierten Eimer konkateniert und es entsteht eine sortierte Liste, die alle Elemente der Eingabeliste enthält.";
	}

	public String getCodeExample() {
		return "Bucketsort-Teil:"
				+ "\n"
				+ "1.   Finde Maximum(-element) m"
				+ "\n"
				+ "2.   Lege n leere Eimer (buckets) B0 bis B(n-1) an"
				+ "\n"
				+ "3.   Wähle ein Element e"
				+ "\n"
				+ "4.   Berechne (Eimer-)Index i = e / m * (n-1)"
				+ "\n"
				+ "5.   Ordne Element e in Eimer B* mit entsprechendem Index i ein"
				+ "\n"
				+ "6.   Wiederhole 3-5 für alle verbleibenden Elemente"
				+ "\n"
				+ "Quicksort-Teil:"
				+ "\n"
				+ "7.   Wähle einen Eimer B*"
				+ "\n"
				+ "8.   Wähle Pivot-Element"
				+ "\n"
				+ "9.   Setze L- und R-Zeiger"
				+ "\n"
				+ "10. Solange der L-Zeiger links vom R-Zeiger:"
				+ "\n"
				+ "	11. Bewege L-Zeiger schrittweise nach rechts,"
				+ "\n"
				+ "	solange das Element auf das L zeigt kleiner gleich dem Pivotelement"
				+ "\n"
				+ "	ist, oder der L-Zeiger den rechten Rand des Intervalls erreicht."
				+ "\n"
				+ "	12. Bewege R-Zeiger schrittweise nach links,"
				+ "\n"
				+ "	solange das Element auf das R zeigt größer gleich dem Pivotelement"
				+ "\n"
				+ "	ist, oder der R-Zeiger den linken Rand des Intervalls erreicht."
				+ "\n"
				+ "	13.   Wenn der L-Zeiger links vom R-Zeiger ist, dann tausche die"
				+ "\n"
				+ "	beiden Elemente auf die L und R zeigen."
				+ "\n"
				+ "14.   Wenn das Element auf das L zeigt größer ist als das Pivotelement, tausche sie."
				+ "\n"
				+ "15.   Wiederhole 8-14 für alle verbleibenden (Teil-)sequenzen"
				+ "\n"
				+ "16.   Wiederhole 7-15 für alle verbleibenden Eimer"
				+ "\n"
				+ "17.   Ausgabe der sortierten Liste (konkatenierte sortierte Eimer)";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) throws IllegalArgumentException {

		Eingabeliste = (int[]) primitives.get("Eingabeliste");
		Eimeranzahl = (Integer) primitives.get("Eimeranzahl");
		
		if (Eimeranzahl <= 0){
			throw new IllegalArgumentException("Eimeranzahl muss größer 0 sein.");
		}
		
		if (Eingabeliste.length <= 0){
			throw new IllegalArgumentException("Die Liste der Eingabeliste muss größer 0 sein.");
		}
		
		return true;
	}

}