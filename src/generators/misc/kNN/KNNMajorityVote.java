/*
 * KNNMajorityVote.java
 * Jan Rehbein, Marius Rettberg-Päplow, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc.kNN;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.Slide;
import algoanim.properties.MatrixProperties;

public class KNNMajorityVote extends KNNGenerator{
	
	private boolean boolWeightedVotingPrimitive;
	private MatrixProperties matrixProperty;
	
	
	public void initializePrimitives(Hashtable<String, Object> primitives) {
		super.initializePrimitives(primitives);
		this.boolWeightedVotingPrimitive = (boolean) primitives.get("weighted voting");
	}
	
	@Override
	public void initializeProperties(AnimationPropertiesContainer props){
		super.initializeProperties(props);
		this.matrixProperty = (MatrixProperties)props.getPropertiesByName("majority matrix");
		
	}

	@Override
	public void init() {
		lang = new AnimalScript("kNN-MajorityVote [DE]", "Jan Rehbein, Marius Rettberg-Päplow", 1024, 868);
	}

	@Override
	public String getName() {
        return "kNN-MajorityVote [DE]";
	}

	@Override
	public String getDescription() {
        //return Slide.getTeaser("generators/misc/kNN/introMajority.txt"); //buggy, konvertiert keine Umlaute
		return "k-Nearest-Neighbor ist ein Verfahren, welches die Problemstellung Klassifikation löst. \n" +
				"Kern dieser Problemstellung ist das Zuordnen eines Messwertes zu einer Klasse mit Hilfe \n" +
				"von bekannten Zuordnungen von Messwerten zu Klassen. \n" +
				"kNN benutzt hierzu die k nächsten Nachbarn des zu Klassifizierenden Messwertes. \n" +
				"In dieser Variante wird der neue Messwert zu der Klasse klassifizert, welche eine \n" +
				"Mehrheit unter den k-Nachbarn erreicht. Es besteht die Möglichkeit, die Stimmen \n" +
				"der Nachbarn abhängig von ihrer Distanz zum neuen Messwert zu gewichten. \n" +
				"Bei diesem Vorgehen erhalten nähere Nachbarn mehr Stimmen und weiter entfernte weniger. \n" +
				"\n" +
				"In diesem Beispiel werden 2D Koordinaten als Messwerte und eine der folgenden \n" +
				"Normen als Abstand zwischen diesen verwendet. \n" +
				"1. Euklidsche Norm : sqrt((x1-x2)²+(y1-y2)²) \n" +
				"2. Manhattan Norm : |x1-x2|+|y1-y2| \n" +
				"3. Maximums Norm : max(|x1-x2|, |y1-y2|) \n" +
				"\n" +
				"kNN wird zum klassifizieren unterschiedlicher Daten verwendet. Ein übliches Vorgehen ist \n" +
				"charakteristische Werte dieser Daten sogenannte Features \n" +
				"in einem Vektor zu speichern und diesen für die Distanz Berechnung zu verwenden. \n";

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
				+"	HashMap<String, double> typeVotes = new HashMap<String, double>();"
				+"\n"
				+"	String maxVoteLabel;"
				+"\n"
				+"	double maxVotes = 0;"
				+"\n"
				+"	for (int i = 1; i < k; i++){"
				+"\n"
				+"		String type = kNN[i].type;"
				+"\n"
				+"		double voting = vote(i);"
				+"\n"
				+"		if (typeVotes.containsKey(type))"
				+"\n"
				+"			voting = typeVotes.get(type)+voting;"
				+"\n"
				+"		typeVotes.put(type, voting);"
				+"\n"
				+"		if (voting > maxVotes){"
				+"\n"
				+"			maxVotes = voting;"
				+"\n"
				+"			maxVoteLabel = type;"
				+"\n"
				+"		}"
				+"\n"
				+"	}"
				+"\n"
				+"	return maxVoteLabel;"
				+"\n"
				+"}";
	}

	@Override
	public void setClassficationRules(KNN s) {
		s.setMode(1);
		s.setMatrixP(matrixProperty);
		s.setWeightedVoting(boolWeightedVotingPrimitive);
	}

}
