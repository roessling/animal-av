package generators.framework;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class InteractiveActionButton {
  
  private Text textHeadline;
  private Rect rectBox;

  protected InteractiveActionButton(Language lang, Coordinates coordinates, String buttonText, int size, Color colorBG) {
    //headline text
    TextProperties headlineProps = new TextProperties();
    headlineProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, size));
    headlineProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
    textHeadline = lang.newText(coordinates, buttonText, "headline", null, headlineProps);

    // headline rectangle
    RectProperties headlinebgProps = new RectProperties();
    headlinebgProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    headlinebgProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    headlinebgProps.set(AnimationPropertiesKeys.FILL_PROPERTY, colorBG);
    headlinebgProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    headlinebgProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    rectBox = lang.newRect(new Offset(-10, -5, textHeadline, AnimalScript.DIRECTION_NW), new Offset(10, 5, textHeadline, AnimalScript.DIRECTION_SE), "rectHeadlinebg", null, headlinebgProps);
  }
  
  public void hide() {
    textHeadline.hide();
    rectBox.hide();
  }
  
  public Rect getRect() {
    return rectBox;
  }

}
