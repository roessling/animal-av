package generators.backtracking.helpers;

public class Person {
	private String name;
	private Person[] ranking;
	
	public Person(String name){
		this.name = name;
	}
	
	public void createRanking(int n){
		ranking = new Person[n];
	}
	
	public String getName(){
		return name;
	}
	
	public Person getPersonAt(int n){
		return ranking[n];
	}
	
	public void setRanking(int n, Person p){
		ranking[n] = p;
	}
}
