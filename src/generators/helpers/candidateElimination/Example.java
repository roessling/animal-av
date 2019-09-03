package generators.helpers.candidateElimination;

public class Example {

    public Example(String[] featureValues, boolean classification) {
	super();
	this.featureValues = featureValues;
	this.classified = classification;
    }

    public final boolean classified;

    private final String[] featureValues;

    public String getFeatureValue(int i) {
	return this.featureValues[i];
    }

    public String[] getFeatureValues() {
	return this.featureValues;
    }

    @Override
    public String toString() {
	String out = "<";
	for (int i = 0; i < this.featureValues.length; i++) {
	    if (this.featureValues[i].equals("?")) {
		if (i < 1)
		    out += "?";
		else
		    out += ", ?";
	    } else if (i < 1)
		out += this.featureValues[i];
	    else
		out += ", " + this.featureValues[i];
	}
	out += ">";
	return out;
    }
}
