package generators.maths.northwestcornerrule;

import generators.maths.northwestcornerrule.Animation;
import algoanim.primitives.generators.Language;

public class Algorithm{

	private Animation myAnimation;
	private int[] supply;
	private int[] demand;
	private Integer[][] base;
	
	public Algorithm(int[] supply, int[] demand, Language animationScript){
		
		this.supply = supply;
		this.demand = demand;
		base = new Integer[demand.length][supply.length];
		myAnimation = new Animation(animationScript);
		
	}
	
	public void animate(){
		
		myAnimation.buildIntroFrame(supply, demand);	
		try{
			validateInput(supply, demand);
			myAnimation.buildDefaultViews(supply, demand);
			startAlgorithm();
			myAnimation.buildLastFrame(base);
		}
		catch(IllegalArgumentException iae){
			myAnimation.buildExceptionFrame("Fehler aufgetreten: " + iae.getMessage());
		}
	}
	
	private void validateInput(int[] angebot, int[] nachfrage) throws IllegalArgumentException{
		if (nachfrage ==null || nachfrage.length == 0){
			throw new IllegalArgumentException("Array an Nachfrage-Werten nicht valide.");			
		}
		if(angebot == null || angebot.length == 0){
			throw new IllegalArgumentException("Array an Angebots-Werten nicht valide.");
		}
		if(sum(nachfrage) != sum(angebot)){
			throw new IllegalArgumentException("Summe der Angebote und Summe der Nachfrage gleichen sich nicht.");
		}
	}
	
	private void startAlgorithm(){
		myAnimation.buildLine0Animation();
		
		int i = 0;
		myAnimation.buildLine2Animation();
		
		int j = 0;
		myAnimation.buildLine3Animation();
		
		while (i < supply.length && j < demand.length){
			myAnimation.buildLine4Animation(i,j);
			
			int x = Math.min(supply[i], demand[j]);
			myAnimation.buildLine6Animation(i, j, supply[i], demand[j], x);
			
			base[j][i] = new Integer(x);
			myAnimation.buildLine7Animation(i, j, x);
			
			supply[i] -= x;
			myAnimation.buildLine8Animation(supply[i], x, i, j);
			
			demand[j] -= x;
			myAnimation.buildLine9Animation(demand[j], x, j);
			
			myAnimation.buildLine10Animation();
			if(supply[i] == 0){
				
				i++;
				myAnimation.buildLine11Animation(i);			
			}
			else{
				myAnimation.buildLine12Animation();
				
				j++;
				myAnimation.buildLine13Animation(j);
			}
		}
	}

	
	// calculate the sum of an array of integers
	private int sum(int[] intArray){
		int sum = 0;
		for(int i: intArray){
			sum += i;
		}
		return sum;
	}

	public String getMyAnimationScript() {
		return myAnimation.getMyAnimationScript().toString();
	}
	
	
	
}
