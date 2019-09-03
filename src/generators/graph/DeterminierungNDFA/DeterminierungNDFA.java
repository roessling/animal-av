/**
 * DeterminierungNDFA.java
 * Nora Wester, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

package generators.graph.DeterminierungNDFA;

import generators.graph.util.Automat;
import generators.graph.util.Bucket;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Vector;



/**
 * @author NoMa
 *
 */
public class DeterminierungNDFA {

	Automat NDFA;
	//Arbeitsvariable f�r Translationen
	Vector<Bucket> work;
	//Entstehende Liste der Zust�nde
	ArrayList<String> newStates;

	//---
	Demo demo;
	//---
	/**
	 * Konstruktor f�r default-Werte 
	 */
	public DeterminierungNDFA() {
		
		// TODO Auto-generated constructor stub
		NDFA = getDefaultNDFA();
		work = new Vector<Bucket>();
		newStates = new ArrayList<String>();
		demo = new Demo();
	}
	
	private String[][] getOldAdja() {
		// TODO Auto-generated method stub
		int size = NDFA.getSizeOfStates();
		String[][] temp = new String[size][size];
		for(int i=0; i<size; i++)
			for(int j=0; j<size; j++)
				temp[i][j] = NDFA.getAdjacencyTranslationAt(i, j);
		return temp;
	}

	private Automat getDefaultNDFA() {
		String[] states = new String[]{"0","1","2"};
		String[] finalStates = new String[]{"2"};
		String[] alphabet = new String[]{"a","b"};
		
		String[][] translations = new String[][]{{"a,b", "a", ""},
				{"", "", "b"},{"","",""}};
		String startState = "0";
		// TODO Auto-generated method stub
		return new Automat(states, finalStates, alphabet, translations, startState);
	}

	public DeterminierungNDFA(Automat NDFA) {
		this.NDFA = NDFA;
		work = new Vector<Bucket>();
		newStates = new ArrayList<String>();
		demo = new Demo();
	}

	public void start(){
		//---
		demo.init(getOldAdja(), NDFA.getStates(), NDFA.getFinalStatesPosition(), NDFA.getAlphabet(), NDFA.getIndexOfState(NDFA.getStartState()));
		//---
		String[] alphabet = NDFA.getAlphabet();
		PriorityQueue<String> workState = new PriorityQueue<String>();
		workState.add(NDFA.getStartState());
		//---
		demo.setQueueValue(NDFA.getStartState(), false, new int[]{2}, new int[]{0, 1});
		//---
		while(!workState.isEmpty()){
			//---
			demo.setHighlight(new int[]{3}, new int[]{2, 9, 10});
			demo.setQueueValue(null, true, new int[]{4, 5}, new int[]{3});
			//---
			Bucket temp = new Bucket();
			String states = workState.poll();
			newStates.add(states);
			for(int a=0; a<alphabet.length; a++){
				//---
				demo.setHighlight(new int[]{6}, new int[]{4, 5, 9, 10});
				demo.setHighlight(new int[]{7}, new int[]{6});
				//---
					
				String newState = toStateString(calculateNewState(states, alphabet[a]));
				temp.setState(alphabet[a], newState);
				//---
				demo.setValue(newState, new int[]{8}, new int[]{7, 28});
				demo.setHighlight(new int[]{9}, new int[]{8});
				//---
				if(!newStates.contains(newState)){
					//---
					demo.setQueueValue(newState, false, new int[]{10}, new int[]{9});
					//---
					workState.add(newState);
				}	
			}
			
			work.add(temp);
				
			System.out.println(work.size()+" " +"worksize");
		}
		//---
		demo.setHighlight(new int[]{}, new int[]{9, 10});
		//---		
		translationsOnConsole();
	}
	
	private void translationsOnConsole() {
		// TODO Auto-generated method stub
		for(int j=0; j<newStates.size(); j++){
			System.out.println(j + " " + newStates.get(j));
		}
		for(int i=0; i<work.size(); i++){
			work.get(i).viewConsol();
			System.out.println("|");
		}
	}

	//die States aus der ArrayList newState werden in zusammengef�gt zu einem neuen State 
	private String toStateString(ArrayList<String> newState) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer(1024);
		if(newState.size() != 0)
			sb.append(newState.get(0));
		
		for(int i=1; i<newState.size(); i++){
			sb.append(",").append(newState.get(i));
			
		}	
		return sb.toString();
	}

	private ArrayList<String> doThe(String states, String letter) {
		// TODO Auto-generated method stub
		//da nicht jeder State namentlich gleich mit seiner Position
		int index = NDFA.getIndexOfState(states);
		if(index != -1){
			System.out.println(index+" index "+states);
			//---
			demo.setAdjaHigh(index, true, new int[]{23, 24}, new int[]{20, 21, 22, 28});
			demo.goThrowAdja(letter, index);
			//---
			Bucket stateBucket = NDFA.getTranslationAt(index);
			return stateBucket.getStateToLetter(letter);
		}
		return new ArrayList<String>();
	}
	
	private ArrayList<String> calculateNewState(String states, String letter){
		ArrayList<String> statesList = new ArrayList<String>();
		//---
		demo.setHighlight(new int[]{20, 21, 22}, new int[]{});
		//---
		//es sind maximal soviele Zust�nde miteinander verbunden, wie es Zust�nde gibt
    	for(int s=0; s<NDFA.getSizeOfStates(); s++){
		//mehrere Zust�nde sind durch "," getrennt
		int index = states.indexOf(",");
		// ist es nur ein Zustand
		if(index==-1){
			ArrayList<String> temp = insert(statesList, doThe(states, letter));
			//---
			demo.setAdjaHigh(NDFA.getIndexOfState(states), false, new int[]{28}, new int[]{25, 26});
			//---
			return temp;
		}
		// sind es mehrer, dann ersten verarbeiten, rausnehmen und wiederholen
		else{
			statesList = insert(statesList, doThe(states.substring(0, index), letter));
			//---
			demo.setAdjaHigh(NDFA.getIndexOfState(states.substring(0, index)), false, new int[]{}, new int[]{25, 26});
			//---
			states = states.substring(index+1);
		}
	} return statesList;
	}

	//vereinigt die ArrayList base mit der ArrayList excepts ohne Dopplungen
	private ArrayList<String> insert(ArrayList<String> base,
			ArrayList<String> excepts) {
		// TODO Auto-generated method stub
		for(int i=0; i<excepts.size(); i++){
			String temp = excepts.get(i);
			if(!base.contains(temp))
				base.add(temp);
			}
		
		return base;
	}

	//�bersetzt die interne Form der Translationen in eine AdjacencyMatrix 
	private String[][] internToAdjacencyMatrix(){
		int size = newStates.size();
		String[][] adjacency = new String[size][size];
		
		for(int i=0; i<adjacency.length; i++){
			Bucket temp = work.get(i);
			for(int j=0; j<size; j++){
				adjacency[i][j] = temp.getLetterToState(newStates.get(j)); 
			}
		}
		
		return adjacency;
	}
	
	//berechnet eine ArrayList mit den neuen FinalStates
	private ArrayList<String> findNewFinalStates(){
		String[] oldFStates = NDFA.getFinalStates();
		ArrayList<String> newFStates = new ArrayList<String>();
		
		for(int i=0; i<newStates.size(); i++){
			for(int j=0; j<oldFStates.length; j++){
				if(newStates.get(i).contains(oldFStates[j])){
					newFStates.add(newStates.get(i));
				}	
			}
		}
		
		return newFStates;
	}
	
	//gibt den neuen DFA aus
	public Automat getDFA(){
		ArrayList<String> finalStates = findNewFinalStates();
		Automat newDFA = new Automat(newStates.toArray(new String[newStates.size()]), finalStates.toArray(new String[finalStates.size()]), NDFA.getAlphabet(), 
				internToAdjacencyMatrix(), NDFA.getStartState());
		//---
		int size = newDFA.getSizeOfStates();
		String[][] temp = new String[size][size];
		for(int i=0; i<size; i++)
			for(int j=0; j<size; j++)
				temp[i][j] = newDFA.getAdjacencyTranslationAt(i, j);
		demo.finalSlides(temp, newDFA.getStates(), newDFA.getFinalStatesPosition(), newDFA.getIndexOfState(newDFA.getStartState()), newDFA.getAlphabet());
		//---
		return newDFA;
	}
	
	public Demo getDemo(){
		return demo;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DeterminierungNDFA a = new DeterminierungNDFA();
		a.start();
		a.getDFA();
		System.out.println(a.getDemo().lang.toString());
	}

}
