package generators.graph.MinimierungDFA;

/**
 * MinimierungDFA.java
 * Nora Wester, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

import generators.graph.util.Automat;
import generators.graph.util.Bucket;

import java.util.ArrayList;
import java.util.Vector;

public class MinimierungDFA {

	private Automat DFA;
	private boolean[][] klassenArray;
	private Demo demo;
	
	//Anzahl der Zust�nde
	int size;
	
	private String[][] newAdjacencyMatrix;
	private String[] newStates;
	private String[] newFinalStates;
	private String newStartState;
	
	public MinimierungDFA() {
		// TODO Auto-generated constructor stub
		DFA = getDefaultDFA();
		size = DFA.getSizeOfStates();
		klassenArray = new boolean[size][size];
		demo = new Demo();
	}
	
	private Automat getDefaultDFA(){
		String[][] translations = new String[][]{{"","b","","a","",""},
				{"a","","","","","b"},
				{"","a","","","","b"},
				{"","b","","","a",""},
				{"","a","","b","",""},
				{"","b","a","","",""}};
		return new Automat(new String[]{"0","1","2","3","4","5"}, new String[]{"0","1","2","4"}, 
				new String[]{"a","b"}, translations, "0");
	}
	
	public MinimierungDFA(Automat DFA){
		this.DFA = DFA;
		size = this.DFA.getSizeOfStates();
		klassenArray = new boolean[size][size];
		demo = new Demo();
	}

	public void start(){
		//---
		demo.init(getOldAdja(), DFA.getStates(), DFA.getFinalStatesPosition(), DFA.getIndexOfState(DFA.getStartState()), DFA.getAlphabet());
		//---
		//erst wird nach Zustandspaaren gesucht, bei denen einer ein Finalstate ist und der andere nicht
		//diese nennt man dann unterscheidbar
		ArrayList<String> finalStates = getFinalStates();
		String[] states = DFA.getStates();
		for(int i=0; i<size-1; i++){
			//---
			demo.moveMarker(i, i-1, i>0, new int[]{2}, new int[]{0, 1, 4, 5, 6});
			//---
			
			for(int j=i+1; j<size; j++){
				//----
				demo.moveMarker(j, j-1, j-1 != i, new int[]{3}, new int[]{2, 4, 5, 6});
				
				demo.setHighlight(new int[]{4}, new int[]{3});
				//----
				if(finalStates.contains(states[i])^finalStates.contains(states[j])){
					klassenArray[i][j] = true;
					klassenArray[j][i] = true;
					//---
					demo.setValue(i, j, new int[]{5,6}, new int[]{4});
					//---
				}	
			}
			//---
			demo.moveMarker(-1, size-1, true, null, null);
			//---
		}
		//---
		demo.moveMarker(-1, size-2, true, new int[]{}, new int[]{4, 5, 6});
		//---
		demo.secondPhase();
		//----
		printOnConsol();
		//nun muss man vergleichen, ob es Zustandspaare gibt, die durch das gleiche Ereignis (Buchstabe)
		//in ein unterscheidbares Zustandspaar  kommen.
		//(q1,a,q4) und (q2,a,q3) da {q3,q4} schon unterscheidbar, ist auch {q1,q2} unterscheidbar
		//Dies solange machen, bis sich nichts mehr �ndert
		String[] alphabet = DFA.getAlphabet();
		boolean run = true;
		int classN = 0;
		while(run){
			//Um die �quivalenzklassen zu trennen
			//gerade == i // ungerade == j
			Vector<Integer> cut = new Vector<Integer>();
			classN++;
			//-----
			demo.setLabel(classN>1, classN, new int[]{8, 9}, new int[]{7, 12, 18, 19, 20, 21, 23, 24});
			demo.changeClass("~"+Integer.toString(classN));
			demo.highAequivClass(size-2, "i", false, classN>1);
			demo.highAequivClass(size-1, "j", false, classN>1);
			//-----
			//solange nichts Neues gefunden; kein Grund noch eine Runde zustarten
			run = false;
			for(int i=0; i<size-1; i++){
				//---
				demo.setHighlight(new int[]{10}, new int[]{8, 9, 12, 18, 19, 20, 21, 23, 24});
				demo.highAequivClass(i, "i", true, true);
				demo.highAequivClass(i-1, "i", false, i != 0);
				demo.highAequivClass(size-1, "j", false, true);
				//---
				Bucket one = DFA.getTranslationAt(i);
				for(int j=i+1; j<size; j++){
					//---
					demo.setHighlight(new int[]{11}, new int[]{10, 12, 18, 19, 20, 21, 22, 24});
					demo.highAequivClass(j, "j", true, true);
					demo.highAequivClass(j-1, "j", false, j-1!=i);
					demo.setHighlight(new int[]{12}, new int[]{11});
					//---
					if(!klassenArray[i][j]){
						int alphaN = 0;
						//---
						demo.setHighlight(new int[]{13}, new int[]{12});
						//---
						Bucket two = DFA.getTranslationAt(j);
						while(alphaN > -1 && alphaN < alphabet.length){
							//---
							demo.setHighlight(new int[]{14}, new int[]{13, 18, 19, 20, 21, 23, 24});
							demo.setHighlight(new int[]{15, 16}, new int[]{14});
							//---
							ArrayList<String> sOne = one.getStateToLetter(alphabet[alphaN]);
							ArrayList<String> sTwo = two.getStateToLetter(alphabet[alphaN]);
							//da dies ein DFA ist, kann man davon ausgehen, sOne und sTwo size = 1 besitzen
							int positionO = DFA.getIndexOfState(sOne.get(0));
							int positionT = DFA.getIndexOfState(sTwo.get(0));
							//---
							demo.goThrow(alphabet[alphaN], i, j);
							demo.setHighlight(new int[]{17}, new int[]{15, 16});
							//---
							boolean isInside = inside(positionO, positionT, cut);
							if(klassenArray[positionO][positionT] && !isInside){
								//----
								demo.setValue(i, j, new int[]{18, 19, 20, 21}, new int[]{17});
								demo.unhighlightAdja(positionO, positionT);
								//----
								klassenArray[i][j] = true;
								klassenArray[j][i] = true;
								cut.add(i);
								cut.add(j);
								run = true;
								alphaN = -1;
		
							}else{
								//----
								if(isInside)
									demo.nextRound();
								demo.setHighlight(new int[]{23, 24}, new int[]{17});
								demo.unhighlightAdja(positionO, positionT);
								//----
								alphaN++;
							}
						
						}
						
					}
					
				}
				
			}
			printOnConsol();
		}
		//---
		demo.setHighlight(new int[]{29}, new int[]{12});
		demo.highAequivClass(size-1, "j", false, true);
		demo.highAequivClass(size-2, "i", false, true);
		//---
		getMinimalDFA();
	}

	private boolean inside(int positionO, int positionT, Vector<Integer> cut) {
		// TODO Auto-generated method stub
		int i = 0;
		while(i<cut.size()-1){
			if((cut.get(i) == positionO)&&cut.get(i+1) == positionT)
				return true;
			else
				i = i+2;
		}
		return false;
	}

	private void printOnConsol() {
		// TODO Auto-generated method stub
		for(int i=0; i<klassenArray.length; i++){
			StringBuffer sb = new StringBuffer();
			for(int j=0; j<klassenArray[i].length; j++){
				sb.append("|");
				if(klassenArray[i][j])
					sb.append("x").append("|");
				else
					sb.append("o").append("|");
			}
			System.out.println(sb.toString());
		}
		System.out.println("--------");
	}

	private void getMinimalDFA() {
		// TODO verschmelzen der nicht unterscheidbaren Zust�nde
		//die zu l�schenden Zust�nde
		ArrayList<Integer> delete = new ArrayList<Integer>();
		//die aufnehmenden Zust�nde
		ArrayList<Integer> get = new ArrayList<Integer>();
		for(int i=1; i<klassenArray.length; i++)
			for(int j=0; j<i; j++){
				if(!klassenArray[i][j]){
					delete.add(i);
					get.add(j);
				}
			}
		//----
		demo.thirdPhase(delete, get);
		//----
		//�berpr�fen, ob in "delete" keine Werte aus "get" enthalten sind
		doSome(delete, get);
		
		String[][] newAdja = getOldAdja();
		//Zustand i wird in Zustand j �berf�hrt ( w�rde aber auch andersherum gehen )
		//die zusammengeh�rigen Paare haben die selbe Position in den zwei ArrayLists
		for(int k=0; k<get.size(); k++){
			int i = delete.get(k);
			int j = get.get(k);
			for(int n=0; n<size; n++){
				String value = DFA.getAdjacencyTranslationAt(n, i);
				boolean isNotAValue = (value == null || value.isEmpty() || value.compareTo("{}") == 0);
				if(!isNotAValue){
					int index = delete.indexOf(n);
					if(index != -1)
						newAdja[get.get(index)][j] = value;
					else
						newAdja[n][j] = value;
				}
			}
			
		}
		newStartState = DFA.getStartState();
		newAdjacencyMatrix = new String[size-delete.size()][size-delete.size()];
		String[] oldStates = DFA.getStates();
		ArrayList<String> temp = new ArrayList<String>();
		//Zust�nde aus der MAtrix l�schen, sowie "states" und "startstate" �berpr�fen
		int ni = 0;
			for(int i=0; i<newAdjacencyMatrix.length; i++){
				int nj=0;
				if(!delete.contains(i+ni)){
					for(int j=0; j<newAdjacencyMatrix[i].length; j++){
						if(!delete.contains(j+nj)){
							newAdjacencyMatrix[i][j] = newAdja[i+ni][j+nj];
						}else{
							nj++;
						}
					}
					temp.add(oldStates[i+ni]);
				}else{
					if(oldStates[i+ni].compareTo(DFA.getStartState()) == 0)
						newStartState = oldStates[get.get(i+ni)];
					
					ni++;
				}
			}
			
			newStates = new String[oldStates.length-delete.size()];
			newStates = temp.toArray(newStates);
			
			//Zust�nde aus "finalStates" l�schen
			
			String[] oldFinalStates = DFA.getFinalStates();
			temp = new ArrayList<String>();
			for(int i=0; i<newStates.length; i++){
				for(int j=0; j<oldFinalStates.length; j++){
					if(oldFinalStates[j].compareTo(newStates[i]) == 0)
						temp.add(oldFinalStates[j]);
				}
			}
			newFinalStates = new String[temp.size()];
			
			newFinalStates = temp.toArray(newFinalStates);
			
	}

	public Automat getNewDFA() {
		// TODO Auto-generated method stub
		Automat newDFA = new Automat(newStates, newFinalStates, DFA.getAlphabet(), newAdjacencyMatrix, newStartState);
		//---
		demo.setFinal(newAdjacencyMatrix, newStates, newDFA.getFinalStatesPosition(), newDFA.getIndexOfState(newStartState), newDFA.getAlphabet());
		//---
		return newDFA;
	}

	private void doSome(ArrayList<Integer> delete, ArrayList<Integer> get) {
		// TODO Auto-generated method stub
		int size = get.size();
		int i = 0;
		while(i<size){
			int index = delete.indexOf(get.get(i));
			if(index != -1){
				get.set(i, get.get(index));
				get.remove(index);
				delete.remove(index);
				size = get.size();
			}
			i++;
		}
	}

	private String[][] getOldAdja() {
		// TODO Auto-generated method stub
		String[][] temp = new String[size][size];
		for(int i=0; i<size; i++)
			for(int j=0; j<size; j++)
				temp[i][j] = DFA.getAdjacencyTranslationAt(i, j);
		return temp;
	}

	private ArrayList<String> getFinalStates() {
		// TODO Auto-generated method stub
		String[] fS = DFA.getFinalStates();
		ArrayList<String> finalStates = new ArrayList<String>();
		for(int i=0; i<fS.length; i++)
			finalStates.add(fS[i]);
		return finalStates;
	}

	public Demo getDemo(){
		return demo;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MinimierungDFA minimierung = new MinimierungDFA();
		minimierung.start();
		minimierung.getNewDFA();
		//System.out.println(minimierung.getDemo().lang.toString());
		
	}

}
