package generators.graph;

import java.util.ArrayList;
import java.util.Random;

public class GozintoHelper {

	// Sucht nach einem bestimmten Knoten
	public int getIndex(ArrayList<GozintoNode> nodes, String node) {
		int index = 0;
		for(int i = 0; i < nodes.size(); i++) {
			if(nodes.get(i).getName().equals(node)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	// Zählt die Anzahl der Edges
	public int countEdges(int[][] adjMatrix) {
		int counter = 0;
		for(int j = 0; j < adjMatrix.length; j++) {
			for(int i = 0; i < j; i++) {
				if(adjMatrix[i][j] > 0) counter++;
			}
		}
		return counter;
	}
	
	// Sucht den Knoten, mit dem niedrigsten Index, in den Kanten eingehen
	public int findFirstIndex(int[][] adjMatrix) {
		int index = 0;
		for(int j = 0; j < adjMatrix.length; j++) {
			for(int i = 0; i < j - 1; i++) {
				if(adjMatrix[i][j] > 0) {
					index = j;
					return index;
				}
			}
		}
		return index;
	}
	
	// Erstellt die GozintoList als String Tabelle
	public String[][] ConvertGozintoList(int[][] gozintoList) {
		String[][] result = new String[gozintoList.length + 1][gozintoList[0].length];
		result[0][0] = "j (Eingangsknoten)";
		result[0][1] = "i (Ausgangsknoten)";
		result[0][2] = "dij (Pfeilbewertung)";
		
		for(int i = 0; i < gozintoList.length; i++) {
			result[i+1][0] = String.valueOf(gozintoList[i][0]);
			result[i+1][1] = String.valueOf(gozintoList[i][1]);
			result[i+1][2] = String.valueOf(gozintoList[i][2]);
		}
		return result;
	}
	
	// Konvertiert ein char-Array in ein String-Array
	public String[] convertCharArray(char[] array){
		String[] stringArray = new String[array.length];
		for(int i = 0; i < array.length; i++) {
			stringArray[i] = Character.toString(array[i]);
		}
		
		return stringArray;
	}
	
	// Prüft ob eine Frage angezeigt werden soll oder nicht
	public boolean showQuestion(double probability) {
		Random rand = new Random();
		double value = rand.nextDouble();
		if(value != 0 && value <= probability) return true;
		return false;
	}
	
	// range = exklusive
	// Gibt einen zufälligen Index zurück, damit eine zufällige Frage ausgewählt wird
	public int getRandomQuestion(int range) {
		Random rand = new Random();
		return rand.nextInt(range);
	}
	
	// Berechnet die Breite der Rechentabelle
	public int calcTableWidth(int[][] gozintoList) {
		int last = 0;
		int width = 1;
		if(gozintoList != null && gozintoList.length > 0 && gozintoList[0].length > 0) last = gozintoList[0][0];
		for(int i = 0; i < gozintoList.length; i++) {
			if(last != gozintoList[i][0]) {
				width++;
			}
			last = gozintoList[i][0];
		}
		
		width = (width + 1) * 2;
		return width;
	}
}
