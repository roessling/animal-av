package generators.cryptography;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;

public class Main {

	public static void main(String[] args) {
		DES des = new DES(Locale.GERMAN);
		ArrayProperties arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, Font.SANS_SERIF);
		arrayProps
				.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.RED);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Color.YELLOW);
		SourceCodeProperties sourceCodeProps = new SourceCodeProperties();
		sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				Color.MAGENTA);
		TextProperties textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		des.init(arrayProps, sourceCodeProps, textProps);
		des.start(
				"0000111100001111000011110000111100001111000011110000111100001111",
				"0000111100001111000011110000111100001111000011110000111100001111");
		System.out.print(des.lang.toString());
	}
}
