package generators.graph.util;

import java.util.ArrayList;
import java.util.Vector;

public class Bucket {

	//Inhalte werden im Stil Buchstabe-Zustand abgespeichert, 
	//wobei es zu jedem Buchstaben mehrer Eintr�ge geben kann
	//Buchstabe f�rht zu Zustand
	Vector<String> lS; 
	
	public Bucket() {
		// TODO Auto-generated constructor stub
		lS = new Vector<String>();
		
	}
	
	// neue Buchstaben-State-Kombination hinzuf�gen 
	public void setState(String letter, String state){
		
		StringBuffer temp = new StringBuffer();
		temp.append(letter).append("-").append(state);
		
		lS.add(temp.toString());
		
	}
	
	// gibt eine Liste mit allen States, die "letter" als Ereignis haben
	public ArrayList<String> getStateToLetter(String letter){
		
		ArrayList<String> states = new ArrayList<String>();
		StringBuffer l = new StringBuffer();
		l.append(letter).append("-");
		
		for(int i=0; i<lS.size(); i++){
			String temp = lS.get(i);
			if(temp.startsWith(l.toString())){
				states.add(temp.substring(l.toString().length()));
			}
		}
		
		return states;
	}
	
	//gibt die Translation zu einem speziellen State wieder
	//nur für Automaten in DFA nutzbar
	public String getLetterToState(String state) {
		// TODO Auto-generated method stub
		StringBuffer l = new StringBuffer();
		l.append("-").append(state);
		
		for(int i=0; i<lS.size(); i++){
			String temp = lS.get(i);
			if(temp.endsWith(l.toString())){
				return temp.substring(0,temp.indexOf("-"));
			}
		}
		
		return "{}";
	}
	
	//Inhalt des Bucket wird auf der Console angezeigt
	public void viewConsol(){
		for(int i=0; i<lS.size(); i++)
			System.out.println(lS.get(i) );
	}
	
	//zum Testen
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Bucket b = new Bucket();
		b.setState("a", "k");
		b.setState("k", "sd");
		
		ArrayList<String> a = b.getStateToLetter("a");
		
		for(int i=0; i<a.size(); i++)
			System.out.println(a.get(i));
	}


}
