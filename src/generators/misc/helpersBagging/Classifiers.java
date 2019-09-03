package generators.misc.helpersBagging;

import java.util.LinkedList;

public class Classifiers 
{
	private LinkedList<LinkedList<Elements>> classifier;
	private static int numRule;
	
	public Classifiers() {
		classifier = new LinkedList<LinkedList<Elements>>();
		numRule = 0;
	}
	
	public LinkedList<LinkedList<Elements>> getClassifier() {
		return classifier;
	}
	
	public LinkedList<Elements> getRule(int i) {
		return classifier.get(i);
	}
	
	public int getLenRule(int i) {
		return classifier.get(i).size();
	}
	
	public Elements getElemOfRule(int i, int j) {
		return classifier.get(i).get(j);
	}
	
	public int getNumRule() {
		return numRule;
	}
	
	public void setClassifiers(LinkedList<LinkedList<Elements>> classifiers) {
		this.classifier = classifiers;
	}
	
	public void  addElemToRule(Elements rule) {
		if(classifier.isEmpty())
			classifier.add(new LinkedList<Elements>());
		numRule =  classifier.size();
		classifier.get(numRule - 1).add(rule);	
	}
	
	public void completeRule()
	{
		numRule =  classifier.size();
		classifier.add(new LinkedList<Elements>());
	}
}
