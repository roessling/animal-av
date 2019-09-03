package generators.graph.util;






public class Automat {

	private String[] states;
	private String[][] translationsAdjacency;
	private Bucket[] translationsIntern;
	private String startState;
	private String[] finalStates;
	private String[] alphabet;
	
	/**
	 * 
	 * @param states Menge der Zust�nde
	 * @param finalStates akzeptierende Zust�nde
	 * @param alphabet Alphabet
	 * @param translations Zustands�berg�nge als AdjacencyMatrix
	 * @param startState Startzustand
	 */
	public Automat(String[] states, String[]finalStates, String[] alphabet,
			String[][] translations, String startState) {
		// TODO Auto-generated constructor stub
		this.alphabet = alphabet;
		this.translationsAdjacency = translations;
		this.finalStates = finalStates;
		this.startState = startState;
		this.states = states;
		
		translationsToInternArray();
	}

	//AdjacencyMatrix wird zur besseren Berechnung in ein 
	//Bucket-Array umgewandelt
	private void translationsToInternArray() {
		// TODO Auto-generated method stub
		translationsIntern = new Bucket[states.length];
		for(int i=0; i<states.length; i++){
			Bucket bucket = new Bucket();
			for(int j=0; j<states.length; j++){
				String value = translationsAdjacency[i][j];
				//es kann maximal das komplette Alphabet im Wert enthalten sein
				//einzelne Buchstaben sind mit "," abgetrennt
				//Solange noch "," vorhanden sind, davorstehenden Wert extrahieren 
				//und "value" darum kürzen
				boolean next = false; 
				while(!next){
					//falls kein Wert in diesem Feld eingetragen ist
					//oder ein Wert, der nicht im Alphabet enthalten ist
					boolean in = inAlphabet(value);
					if(value == null || value.isEmpty() || value.compareTo("{}") == 0){
						bucket.setState("{}", states[j]);
						next = true;
					}else{	
					
					int index = value.indexOf(",");
					if(index == -1)
						index = value.indexOf("+");
					
					if(index==-1 && in){
						bucket.setState(value, states[j]);
						next = true;
						}
					else{
						bucket.setState(value.substring(0, index), states[j]);
						value = value.substring(index+1);
					}
				}
				}	
			}   translationsIntern[i] = bucket;
		}   translationToConsol();
	}
	
	//gibt an, ob value im Alphabet enthalten ist 
	private boolean inAlphabet(String value) {
		// TODO Auto-generated method stub
		for(int i=0; i<alphabet.length; i++)
			if(alphabet[i].compareTo(value) == 0)
				return true;
		return false;
	}

	//gibt die interne Form der Translationen auf der Console aus
	private void translationToConsol() {
		// TODO Auto-generated method stub
		for(int i=0; i<translationsIntern.length; i++){
			translationsIntern[i].viewConsol();
			System.out.println("||");
		}
	}

	//einige getter
	public String getStartState(){
		return startState;
	}
	
	public Bucket getTranslationAt(int state){
		return translationsIntern[state];
	}

	public String[] getAlphabet() {
		// TODO Auto-generated method stub
		return alphabet;
	}
	
	public String[] getFinalStates(){
		return finalStates;
	}
	
	public String[] getStates(){
		return states;
	}
	
	//gibt den Wert an der Stelle (i,j) aus der AdjacencyMatrix
	public String getAdjacencyTranslationAt(int i, int j){
		return translationsAdjacency[i][j];
	}
	
	//gibt die Anzahl der Zust�nde zur�ck
	public int getSizeOfStates() {
		// TODO Auto-generated method stub
		return states.length;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public int getIndexOfState(String state) {
		// TODO Auto-generated method stub
		for (int i=0; i<states.length; i++){
			if(states[i].compareTo(state)==0)
				return i;
		}
		return -1;
	}

	public int[] getFinalStatesPosition(){
		int[] temp = new int[finalStates.length];
		for (int i=0; i<finalStates.length; i++){
			temp[i] = getIndexOfState(finalStates[i]);
		}
		return temp;
	}

}
