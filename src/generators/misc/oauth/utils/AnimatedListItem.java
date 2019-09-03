package generators.misc.oauth.utils;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;

import java.awt.*;

public class AnimatedListItem{
    public String content;
    public Rect box;
    private RectProperties boxProps;
    private TextProperties boxTextProps;
    private Text boxTextObj;
    private int fontSize = 18;
    private Coordinates upperLeft;
    private int xOffset = 200;
    private int xHighlightOffset = 30;

    public AnimatedListItem(Language lang, int x, int y, int width, int height, String content){
        this.content = content;
        upperLeft = new Coordinates(x+xOffset,y);

        boxProps = new RectProperties();
        boxProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
        boxProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
        boxProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        box = lang.newRect(upperLeft, new Coordinates(x+width+xOffset, y+height), "itemContentBox", null, boxProps);

        boxTextProps = new TextProperties();
        boxTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, fontSize));
        boxTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

        boxTextObj = lang.newText(new Offset(5, -10, box, AnimalScript.DIRECTION_W), this.content, "boxTextObj", null, boxTextProps);

        box.hide();
        boxTextObj.hide();
    }
    public void hide(){
        box.hide();
        boxTextObj.hide();
    }

    public void slideIn(int stepSlideInTimeInMs){
        box.show();
        boxTextObj.show();
        Coordinates target = new Coordinates(upperLeft.getX() - xOffset, upperLeft.getY());
        moveTo(target, stepSlideInTimeInMs);
        //boxTextObj.show(new MsTiming(stepSlideInTimeInMs));
    }

    public void highLight(){
        Coordinates target = new Coordinates(upperLeft.getX() + xHighlightOffset, upperLeft.getY());
        moveTo(target, 200);
        boxTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, fontSize+1));
        boxTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.DARK_GRAY);
        upperLeft = target;
    }

    public void unHighLight(){
        Coordinates target = new Coordinates(upperLeft.getX() - xHighlightOffset, upperLeft.getY());
        moveTo(target, 200);
        boxTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, fontSize));
        boxTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        upperLeft = target;

    }
    public void moveTo(Coordinates target, int duration){

        box.moveBy("translate",
                target.getX()-upperLeft.getX() , target.getY()-upperLeft.getY(),
                new MsTiming(0), new MsTiming(duration));

        boxTextObj.moveBy("translate",
                target.getX()-upperLeft.getX() , target.getY()-upperLeft.getY(),
                new MsTiming(0), new MsTiming(duration));

        upperLeft = new Coordinates(upperLeft.getX() - xOffset, upperLeft.getY());

    }



}
