package generators.helpers.candidateElimination;

import java.awt.Color;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class TableMarker {

    ArrayMarker tableMarker;
    Timing duration = new TicksTiming(30);
    Language lang;

    public TableMarker(Table table, Language lang) {
	this.lang = lang;
	ArrayMarkerProperties arrayJMProps = new ArrayMarkerProperties();
	arrayJMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "e");
	arrayJMProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
	arrayJMProps.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
	arrayJMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	tableMarker = lang.newArrayMarker(table.table[0], 0, "tableMarker",
		null, arrayJMProps);
    }

    public void increment() {
	tableMarker.increment(null, duration);
	lang.nextStep();
    }

    public void decrement() {
	tableMarker.decrement(null, duration);
	lang.nextStep();
    }

    /**
     * 
     */
    public void hide() {
	tableMarker.hide();
    }

    /**
     * 
     */
    public void show() {
	tableMarker.show();
    }
}
