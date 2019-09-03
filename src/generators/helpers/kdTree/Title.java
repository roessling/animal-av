package generators.helpers.kdTree;

import java.awt.Font;

import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class Title {

  public Title(Language lang) {
    TextProperties titleProps = new TextProperties("title");
    titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 24));

    lang.newText(new Coordinates(41, 30), "kd-tree", "title", null, titleProps);
  }

}
