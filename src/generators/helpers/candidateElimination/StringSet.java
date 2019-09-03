package generators.helpers.candidateElimination;

import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Set;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;

/**
 * @author mateusz
 */
public class StringSet {

    // position
    private Coordinates arrayPos;

    // timing
    private TicksTiming highlightDelay = new TicksTiming(50);
    private TicksTiming markerDuration = new TicksTiming(50);

    private String name;
    private StringArray lastArray;
    ArrayMarkerProperties markerProps;
    private Language lang;

    private ArrayMarker marker;

    private static int counter = -1;

    // properties
    private String propId = "SetProperties";
    private ArrayProperties arrayProps;
    private AnimationPropertiesContainer animProps;

    public StringSet(String name, Coordinates arrayPos,
	    AnimationPropertiesContainer animProps, Language lang) {

	this.arrayProps = new ArrayProperties();
	this.animProps = animProps;

	setFromProp(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY);
	setFromProp(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY);

	this.lang = lang;
	this.name = name;
	this.arrayPos = arrayPos;

	this.arrayProps
		.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
	this.arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	this.arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,
		Boolean.TRUE);
	this.arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
		Color.BLACK);

    }

    public void update(Set<Rule> set) {
	if (this.lastArray != null)
	    this.lastArray.hide();

	String[] displayArray = transformToArray(set);

	this.lastArray = this.lang.newStringArray(this.arrayPos, displayArray,
		this.name + counter, null, this.arrayProps);

    }

    private String[] transformToArray(Set<Rule> set) {
	String[] displayArray = new String[set.size() + 2];
	displayArray[0] = this.name + "-Set: {";
	Rule[] rulesToShow = set.toArray(new Rule[set.size()]);
	for (int i = 1; i < displayArray.length - 1; i++) {
	    displayArray[i] = rulesToShow[i - 1].toString();
	}
	displayArray[displayArray.length - 1] = "}";
	return displayArray;
    }

    public void highlight(int position) {
	this.lastArray.highlightCell(position + 1, null, this.highlightDelay);
	this.lang.nextStep();
    }

    public void incrementMarker() {
	this.marker.increment(null, this.markerDuration);
	this.lang.nextStep();
    }

    /**
     * 
     */
    public void showMarker(String label) {
	// marker
	counter++;
	this.markerProps = new ArrayMarkerProperties();
	this.markerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, label);
	this.markerProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
	this.markerProps.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY,
		true);
	this.markerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
		Color.BLACK);
	this.marker = this.lang.newArrayMarker(this.lastArray, 1, this.name
		+ "Marker" + counter, null, this.markerProps);
    }

    /**
     * 
     */
    public void hideMarker() {
	this.marker.hide(this.markerDuration);
    }

    /**
     * Sets the property from the animationPropertyContainer
     * 
     * @param animationPropertyKey
     */
    private void setFromProp(String animationPropertyKey) {
	if (this.animProps == null) {
	    // configure colors manually
	    Color color = (animationPropertyKey
		    .equals(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY)) ? Color.BLACK
		    : Color.ORANGE;
	    this.arrayProps.set(animationPropertyKey, color);
	} else
	    this.arrayProps.set(animationPropertyKey, this.animProps.get(
		    this.propId, animationPropertyKey));
    }
}
