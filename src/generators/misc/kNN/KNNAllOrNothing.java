/*
 * KNNAllOrNothing.java
 * Jan Rehbein, Marius Rettberg-Päplow, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc.kNN;


import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.Slide;

public class KNNAllOrNothing extends KNNGenerator{
	
	@Override
	public void init() {
		lang = new AnimalScript("kNN-AllOrNothing [DE]", "Jan Rehbein, Marius Rettberg-Päplow", 1024, 768);
	}

	@Override
	public String getName() {
        return "kNN-AllOrNothing [DE]";
	}

	@Override
	public String getDescription() {
        //return Slide.getTeaser("generators/misc/kNN/introAoN.txt"); //buggy, konvertiert keine Umlaute
		return "k-Nearest-Neighbor ist ein Verfahren, welches die Problemstellung Klassifikation löst. \n" +
				"Kern dieser Problemstellung ist das Zuordnen eines Messwertes zu einer Klasse mit Hilfe \n" +
				"von bekannten Zuordnungen von Messwerten zu Klassen. \n" +
				"kNN benutzt hierzu die k nächsten Nachbarn des zu Klassifizierenden Messwertes. \n" +
				"In dieser Variante wird der neue Messwert zu einer Klasse klassifiziert, \n" +
				"falls alle k-Nachbarn auch dieser Klasse angehören. Ansonsten bleibt der Punkt unbestimmt. \n" +
				"\n" +
				"In diesem Beispiel werden 2D Koordinaten als Messwerte und eine der folgenden \n" +
				"Normen als Abstand zwischen diesen verwendet. \n" +
				"  1. Euklidsche Norm : sqrt((x1-x2)²+(y1-y2)²) \n" +
				"  2. Manhattan Norm : |x1-x2|+|y1-y2| \n" +
				"  3. Maximums Norm : max(|x1-x2|, |y1-y2|) \n" +
				"\n" +
				"kNN wird zum klassifizieren unterschiedlicher Daten verwendet. Ein übliches Vorgehen ist \n" +
				"charakteristische Werte dieser Daten sogenannte Features \n" +
				"in einem Vektor zu speichern und diesen für die Distanz Berechnung zu verwenden.";
	}

	@Override
	public String getCodeExample() {
		return "public String kNN-Classification(Point[] data, Point p, int k){"
				+"\n"
				+"	int sizeOfData = data.length;"
				+"\n"
				+"	double[] dist = new double[sizeOfData];"
				+"\n"
				+"	for (int i = 0; i < sizeOfData; i++){"
				+"\n"
				+"		dist[i] = dist(p,data[i]);"
				+"\n"
				+"	}"
				+"\n"
				+"	Point[] kNN = new Point[k];"
				+"\n"
				+"	for (int i = 0; i < k; k++){"
				+"\n"
				+"		int index = minIndex(dist);"
				+"\n"
				+"		kNN[k] = data[index];"
				+"\n"
				+"		dist[index] = MAX_VALUE;"
				+"\n"
				+"	}"
				+"\n"
				+"	String type = kNN[0].type;"
				+"\n"
				+"	for (int i = 1; i < k; i++){"
				+"\n"
				+"		if (kNN[i].type != type)"
				+"\n"
				+"			\"return \" \";"
				+"\n"
				+"	}"
				+"\n"
				+"	return type;"
				+"\n"
				+"}";
	}

	@Override
	public void setClassficationRules(KNN s) {
		return;		
	}

}
