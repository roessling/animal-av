package generators.misc.oauth.utils;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.CircleProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;

import java.awt.*;


public class Token {

    private Rect tokenRect;
    private Circle token;
    private Text tokenTextObj;


    private String tokenString = "Access Token";

    private Coordinates position;

    private Language lang;
    TextProperties tokenTextProps;
    CircleProperties tokenProps;
    RectProperties tokenRectProps;

    int tokenMoveTimeInMs;


    public Token(Language lang, String tokenString, TextProperties tokenTextProps, CircleProperties tokenProps, RectProperties tokenRectProps, Coordinates position, int tokenMoveTimeInMs) {

        this.tokenTextProps = tokenTextProps;
        this.tokenProps = tokenProps;
        this.tokenRectProps = tokenRectProps;
        this.tokenString = tokenString;
        this.tokenMoveTimeInMs = tokenMoveTimeInMs;

        this.position = position;
        int fontSize = 16;

        token = lang.newCircle(position, 30, "token", null, tokenProps);

        tokenRect = lang.newRect(new Coordinates(position.getX()-20,position.getY()-10), new Coordinates(position.getX()+100,position.getY()+10), "tokenContent", null,tokenRectProps);

        tokenRect.changeColor("color", Color.LIGHT_GRAY, new MsTiming(0), new MsTiming(0));
        tokenTextObj = lang.newText(new Offset(14, 20, token, AnimalScript.DIRECTION_NW), tokenString, "tokentext", null, tokenTextProps);
    }



    public void changeText(String text){
        this.tokenString = text;
        tokenTextObj.setText(text, new MsTiming(0), new MsTiming(500));
    }

    public void moveTo(Coordinates target){

        token.moveBy("translate",
                target.getX()-position.getX() , target.getY()-position.getY(),
                new MsTiming(0), new MsTiming(tokenMoveTimeInMs));

        tokenTextObj.moveBy("translate",
                target.getX()-position.getX() , target.getY()-position.getY(),
                new MsTiming(0), new MsTiming(tokenMoveTimeInMs));

        tokenRect.moveBy("translate",
                target.getX()-position.getX() , target.getY()-position.getY(),
                new MsTiming(0), new MsTiming(tokenMoveTimeInMs));
    }

    public void hide(){
        token.hide();
        tokenTextObj.hide();
        tokenRect.hide();

    }
    public void show(){
        token.show();
        tokenTextObj.show();
        tokenRect.show();

    }

}