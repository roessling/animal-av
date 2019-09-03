package generators.helpers.candidateElimination;

import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;

import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class Table {

    StringArray[] table;

    // Coordinates & Timing
    private Coordinates position = new Coordinates(60, 100);
    private TicksTiming hightlightDelay = new TicksTiming(30);

    // Properties
    private String propId = "TableProperties";
    private ArrayProperties arrayProps;
    private AnimationPropertiesContainer animProps;

    public Table(String[][] stringTable,
	    AnimationPropertiesContainer animProps, Language lang) {

	this.animProps = animProps;

	// ArrayProperties
	this.arrayProps = new ArrayProperties();

	// Properties from the container
	if (animProps == null) {
	    this.arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
		    Color.BLACK);
	    this.arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
		    Color.BLACK);
	    this.arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
		    Color.GREEN);
	    this.arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
		    Color.RED);
	} else {
	    setFromProp(AnimationPropertiesKeys.COLOR_PROPERTY);
	    setFromProp(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY);
	    setFromProp(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY);
	    setFromProp(AnimationPropertiesKeys.FILL_PROPERTY);
	}

	// Properties set manually
	this.arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,
		Boolean.TRUE);
	this.arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
		Color.BLACK);
	this.arrayProps.set("vertical", true);

	// HeaderProperties
	TextProperties headerProps = new TextProperties("tableHeader");
	headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		"SansSerif", Font.BOLD, 12));

	int rows = stringTable.length;
	int columns = stringTable[0].length - 1;

	// a table consists of i columns
	this.table = new StringArray[columns];
	Text[] header = new Text[columns - 1];

	// Fill table
	for (int i = 0; i < columns; i++) {

	    // extract i-th column
	    String[] columnData = new String[rows];
	    for (int j = 0; j < rows; j++) {
		columnData[j] = " " + stringTable[j][i] + " ";
	    }

	    if (i == 0) {
		this.table[i] = lang.newStringArray(this.position, columnData,
			"col0", null, this.arrayProps);
		header[i] = lang.newText(new Offset(0, -30, "col0", "NW"),
			"Training Examples", "Training Examples", null,
			headerProps);

	    } else {
		this.table[i] = lang.newStringArray(new Offset(0, 0, "col"
			+ (i - 1), "NE"), columnData, "col" + i, null,
			this.arrayProps);
	    }
	}

	// color rows
	for (int i = 0; i < rows; i++) {
	    if (stringTable[i][columns].equals("+"))
		highlightRow(i);
	}

    }

    /**
     * highlights a row in the table.
     * 
     * @param row
     */
    public void highlightRow(int row) {
	for (int i = 0; i < this.table.length; i++) {
	    this.table[i].highlightCell(row, null, this.hightlightDelay);
	}
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
