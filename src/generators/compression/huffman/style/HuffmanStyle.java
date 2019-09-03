package generators.compression.huffman.style;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import algoanim.properties.AnimationProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;

public class HuffmanStyle {

	public static final String HEADLINE = "headline";
	public static final String PLAINTEXT = "plaintext";
	public static final String MARKER = "marker";
	public static final String TRAVERSE_ARRAY = "traverse_array";
	public static final String SOURCECODE = "sourcecode";
	public static final String ARRAY_FIRST_COL = "array_first_col";
	public static final String ARRAY_REST = "array_rest";
	public static final String FREQUENCY = "frequency";
	public static final String CHARACTER = "character";
	public static final String NUMBER = "number";
	public static final String CIRCLE = "circle";
	public static final String INSERT_COUNTER = "insert_counter";

	private Map<String, AnimationProperties> map;

	public HuffmanStyle() {

		map = new HashMap<String, AnimationProperties>();
	}

	public void fillDefaultValues() {

		SourceCodeProperties sourceProps = (SourceCodeProperties) map
				.get(SOURCECODE);
		if (sourceProps == null) {
			sourceProps = new SourceCodeProperties();
		}
		sourceProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.PLAIN, 16));
		map.put(SOURCECODE, sourceProps);

		TextProperties headlineProps = (TextProperties) map.get(HEADLINE);
		if (headlineProps == null) {
			headlineProps = new TextProperties();
		}
		headlineProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.BOLD, 24));
		map.put(HEADLINE, headlineProps);

		ArrayProperties arrayPropsFirstCol = (ArrayProperties) map
				.get(ARRAY_FIRST_COL);
		if (arrayPropsFirstCol == null) {
			arrayPropsFirstCol = new ArrayProperties();
		}
		arrayPropsFirstCol.set(AnimationPropertiesKeys.DIRECTION_PROPERTY,
				Boolean.TRUE);
		arrayPropsFirstCol.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.PLAIN, 16));
		map.put(ARRAY_FIRST_COL, arrayPropsFirstCol);

		ArrayProperties arrayPropsRest = (ArrayProperties) map.get(ARRAY_REST);
		if (arrayPropsRest == null) {
			arrayPropsRest = new ArrayProperties();
		}
		arrayPropsRest.set(AnimationPropertiesKeys.DIRECTION_PROPERTY,
				Boolean.TRUE);
		arrayPropsRest.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.PLAIN, 16));
		map.put(ARRAY_REST, arrayPropsRest);

		TextProperties freqProps = (TextProperties) map.get(FREQUENCY);
		if (freqProps == null) {
			freqProps = new TextProperties();
		}
		freqProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.PLAIN, 18));
		freqProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
		freqProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		map.put(FREQUENCY, freqProps);

		TextProperties charProps = (TextProperties) map.get(CHARACTER);
		if (charProps == null) {
			charProps = new TextProperties();
		}
		charProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.PLAIN, 14));
		charProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
		map.put(CHARACTER, charProps);

		TextProperties noProps = (TextProperties) map.get(NUMBER);
		if (noProps == null) {
			noProps = new TextProperties();
		}
		noProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
		noProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		noProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.PLAIN, 9));
		map.put(NUMBER, noProps);

		CircleProperties circleProps = (CircleProperties) map.get(CIRCLE);
		if (circleProps == null) {
			circleProps = new CircleProperties();
		}
		circleProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		map.put(CIRCLE, circleProps);

		ArrayProperties traverseArrayProps = (ArrayProperties) map
				.get(TRAVERSE_ARRAY);
		if (traverseArrayProps == null) {
			traverseArrayProps = new ArrayProperties();
		}
		traverseArrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.PLAIN, 12));
		traverseArrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Color.BLACK);
		traverseArrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
				Color.WHITE);
		traverseArrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,
				Boolean.TRUE);
		traverseArrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Color.BLACK);
		traverseArrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.RED);
		traverseArrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Color.YELLOW);
		map.put(TRAVERSE_ARRAY, traverseArrayProps);

		TextProperties plainTextProps = (TextProperties) map.get(PLAINTEXT);
		if (plainTextProps == null) {
			plainTextProps = new TextProperties();
		}
		plainTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		plainTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"SansSerif", Font.PLAIN, 16));
		map.put(PLAINTEXT, plainTextProps);

		ArrayMarkerProperties markerProps = (ArrayMarkerProperties) map
				.get(MARKER);
		if (markerProps == null) {
			markerProps = new ArrayMarkerProperties();
		}
		map.put(MARKER, markerProps);

		RectProperties rp = (RectProperties) map.get(INSERT_COUNTER);
		if (rp == null) {
			rp = new RectProperties();
		}
		rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
		rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		map.put(INSERT_COUNTER, rp);
	}

	public AnimationProperties getProperties(String primitive) {
		return map.get(primitive);
	}

	public void setProperties(String primitive, AnimationProperties properties) {
		map.put(primitive, properties);
	}
}
