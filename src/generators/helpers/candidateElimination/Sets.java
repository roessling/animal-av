package generators.helpers.candidateElimination;

import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Set;

import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;

/**
 * @author mateusz
 */
public class Sets {

    // position
    private Coordinates sArrayPos = new Coordinates(50, 500);
    private Coordinates gArrayPos = new Coordinates(50, 550);

    // timing
    private TicksTiming highlightDelay = new TicksTiming(50);

    private StringArray lastGArray;
    private StringArray lastSArray;
    private Language lang;

    // Properties
    private String propId = "SetProperties";
    private ArrayProperties arrayProps;
    private AnimationPropertiesContainer animProps;

    public Sets(Language lang, AnimationPropertiesContainer animProps) {

	this.lang = lang;

	setFromProp(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY);
	setFromProp(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY);

	this.arrayProps = new ArrayProperties();
	this.arrayProps
		.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
	this.arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	this.arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,
		Boolean.TRUE);
	this.arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
		Color.RED);
	// arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
	// "Monospaced", Font.BOLD, 12));

    }

    public void displaySets(Set<Rule> sSet, Set<Rule> gSet) {
	// ANI
	if (this.lastGArray != null)
	    this.lastGArray.hide();
	if (this.lastSArray != null)
	    this.lastSArray.hide();

	String[] displaySArray = transformToArray(sSet);
	String[] displayGArray = transformToArray(gSet);

	this.lastGArray = this.lang.newStringArray(this.gArrayPos,
		displayGArray, "G", null, this.arrayProps);

	this.lastSArray = this.lang.newStringArray(this.sArrayPos,
		displaySArray, "S", null, this.arrayProps);
	this.lang.nextStep();

    }

    private String[] transformToArray(Set<Rule> set) {
	String[] displayArray = new String[set.size() + 2];
	displayArray[0] = "{";
	Rule[] rulesToShow = set.toArray(new Rule[set.size()]);
	for (int i = 1; i < displayArray.length - 1; i++) {
	    displayArray[i] = rulesToShow[i - 1].toString();
	}
	displayArray[displayArray.length - 1] = "}";
	return displayArray;
    }

    public void highlightS(int position) {
	this.lastSArray.highlightCell(position + 1, null, this.highlightDelay);
	this.lang.nextStep();
    }

    public void highlightG(int position) {
	this.lastGArray.highlightCell(position + 1, null, this.highlightDelay);
	this.lang.nextStep();
    }

    /**
     * Sets the property from the animationPropertyContainer
     * 
     * @param animationPropertyKey
     */
    private void setFromProp(String animationPropertyKey) {
	this.arrayProps.set(animationPropertyKey, this.animProps.get(
		this.propId, animationPropertyKey));
    }
}