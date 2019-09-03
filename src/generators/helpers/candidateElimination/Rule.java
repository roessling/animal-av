package generators.helpers.candidateElimination;

import generators.misc.CandidateElimination;

import java.util.Set;
import java.util.TreeSet;

public class Rule implements Comparable<Rule> {

    private boolean isMaxSpecialized = false;

    private String[] conditions;

    private Rule(boolean isMaxSpecialized) {
	int conditionNumber = CandidateElimination.getNumberOfConditions();
	conditions = new String[conditionNumber];

	if (isMaxSpecialized)
	    this.isMaxSpecialized = true;
	else {
	    this.isMaxSpecialized = false;

	    for (int i = 0; i < conditions.length; i++) {
		conditions[i] = "?";
	    }
	}
    }

    public Rule(String[] newConditions) {
	conditions = newConditions;
	isMaxSpecialized = false;
    }

    public boolean classify(Example example) {
	if (isMaxSpecialized) {
	    return false;
	}
	for (int i = 0; i < conditions.length; i++) {
	    String condition = conditions[i];

	    if (condition.equals("?"))
		continue;

	    String featureValue = example.getFeatureValue(i);
	    if (!condition.equals(featureValue))
		return false;
	}
	return true;
    }

    public static Set<Rule> initSSet() {
	Set<Rule> sSet = new TreeSet<Rule>();
	sSet.add(new Rule(true));
	return sSet;
    }

    public static Set<Rule> initGSet() {
	Set<Rule> gSet = new TreeSet<Rule>();
	gSet.add(new Rule(false));
	return gSet;
    }

    public Rule generalize(Example e) {
	if (isMaxSpecialized)
	    return new Rule(e.getFeatureValues());

	String[] newConditions = conditions.clone();
	for (int i = 0; i < newConditions.length; i++) {
	    if (newConditions[i].equals("?"))
		continue;
	    if (newConditions[i].equals(e.getFeatureValue(i)))
		continue;
	    newConditions[i] = "?";
	}

	return new Rule(newConditions);
    }

    public Set<Rule> specialize(Example e) {
	if (isMaxSpecialized) {
	    Set<Rule> newSet = new TreeSet<Rule>();
	    newSet.add(this);
	    return newSet;
	}

	for (int i = 0; i < conditions.length; i++) {
	    if (conditions[i].equals(e.getFeatureValue(i))) {
		Set<Rule> newSet = new TreeSet<Rule>();
		newSet.add(new Rule(true));
		return newSet;
	    }
	}

	Set<Rule> newSet = new TreeSet<Rule>();
	for (int i = 0; i < conditions.length; i++) {
	    for (String featureValue : CandidateElimination.getFeatureValues(i)) {
		if (featureValue.equals(e.getFeatureValue(i))) {
		    continue;
		} else {
		    String[] newConditions = conditions.clone();
		    newConditions[i] = featureValue;
		    newSet.add(new Rule(newConditions));
		}
	    }
	}

	return newSet;
    }

    @Override
    public int compareTo(Rule o) {
	if (o.equals(this))
	    return 0;
	else
	    return -1;
    }

    @Override
    public String toString() {
	if (isMaxSpecialized) {
	    String out = "<";
	    for (int i = 0; i < conditions.length; i++) {
		if (i < 1)
		    out += "{}";
		else
		    out += ", {}";
	    }
	    out += ">";
	    return out;
	}

	String out = "<";
	for (int i = 0; i < conditions.length; i++) {
	    if (conditions[i].equals("?")) {
		if (i < 1)
		    out += "?";
		else
		    out += ", ?";
	    } else if (i < 1)
		out += conditions[i];
	    else
		out += ", " + conditions[i];
	}
	out += ">";
	return out;
    }

    public boolean isMoreSpecial(Set<Rule> gSet) {
	for (Rule rule : gSet) {
	    if (rule.equals(this))
		continue;
	    boolean ruleIsMoreSpecial = true;
	    for (int i = 0; i < conditions.length; i++) {
		if (!conditions[i].equals(rule.conditions[i])
			&& !rule.conditions[i].equals("?")) {
		    ruleIsMoreSpecial = false;
		    break;
		}
	    }

	    if (ruleIsMoreSpecial)
		return true;
	}
	return false;
    }

    public boolean isMoreGenerell(Set<Rule> sSet) {
	if (isMaxSpecialized)
	    return true;
	for (Rule rule : sSet) {
	    if (rule.equals(this))
		continue;
	    boolean ruleIsMoreGenerell = true;
	    for (int i = 0; i < conditions.length; i++) {
		if (!conditions[i].equals(rule.conditions[i])
			&& !conditions[i].equals("?")) {
		    ruleIsMoreGenerell = false;
		    break;
		}
	    }

	    if (ruleIsMoreGenerell)
		return true;
	}
	return false;
    }

}
