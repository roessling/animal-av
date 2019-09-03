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
import algoanim.util.Node;
import algoanim.util.Offset;

import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Generates Infoboxes, Pseudocode and Headings
 * @author vincent
 *
 */
public class ProtocolLayout {

    private Text headingObj;
    private String headingString = "OAuth2.0";

    private Text infoTextObj;

    private Rect infoBox;
    private Rect infoBoxMarker;


    private int textBoxWidth = 500;
    private int textBoxHeight = 600;

    public LinkedList<Text> infoBoxLines;
    private int infoBoxLineCount = 0 ;

    public AnimatedList stepsList;


    public LinkedList<OAuthEntity> entities;

    public LinkedList<ProtocolArrow> protocolArrows;


    public int X_CENTER = 370;
    public int Y_CENTER = 400;
    public int radius = 360;

    /**
     * Only one Layout can exist. So it makes sense to mark this attribute static, in order to get the active Entites
     * Is required in order to get the correct modulus for calculations concerning the circle layout.
     */
    public static int activeEntities = 0;

    public int x,r;

    private TextProperties headingProps = new TextProperties();
    private TextProperties infoTextProps = new TextProperties();
    private RectProperties infoBoxProps = new RectProperties();
    private RectProperties infoBoxMarkerProps = new RectProperties();


    public Language lang;
    private int WIDTH, HEIGHT;
    private int stepSlideInTimeInMs,moveEntityTimeInMs;


    public ProtocolLayout(Language lang, int X_CENTER, int Y_CENTER, int r, int width, int height, RectProperties infoBoxMarkerProps, TextProperties infoTextProps, int stepSlideInTimeInMs ,int moveEntityTimeInMs){
        this.infoBoxMarkerProps = infoBoxMarkerProps;
        this.lang = lang;
        init();

        this.X_CENTER = X_CENTER;
        this.Y_CENTER = Y_CENTER;
        this.radius = r;
        this.WIDTH = width;
        this.HEIGHT = height;

        this.x = X_CENTER + r + 150;
        this.stepSlideInTimeInMs = stepSlideInTimeInMs;
        this.moveEntityTimeInMs = moveEntityTimeInMs;
        this.infoTextProps = infoTextProps;

        infoBoxLines = new LinkedList<Text>();

        //drawInfoBox();

        stepsList = new AnimatedList(lang, new Coordinates(x,10), textBoxWidth,30 );
    }

    public void drawFlowLayout(){
        drawInfoBox();
    }

    public void drawIntroOutroLayout(){
        // TODO: This is just a workaround.
        Text hideme =  lang.newText(new Coordinates(380,273), "", "", null); //random text so the next line is positioned correctly
        hideme.hide();
        // End of Workaround
        headingObj = lang.newText(new Coordinates(5,5), headingString, "IntroOutroHeading", null, headingProps);


        infoBox = lang.newRect(new Coordinates(45,45), new Coordinates(WIDTH ,HEIGHT-150), "infoBox", null, infoBoxProps);
        infoBoxMarker = lang.newRect(new Coordinates(40,40), new Coordinates(WIDTH+5,HEIGHT-145), "infoBox", null, infoBoxMarkerProps);

        // ** Info Text **
    }




    private void drawInfoBox(){

        // TODO: This is just a workaround.
        Text hideme =  lang.newText(new Coordinates(380,273), "", "", null); //random text so the next line is positioned correctly
        hideme.hide();
        // End of Workaround
        headingObj = lang.newText(new Coordinates(5,10), headingString, "BasicLayoutHeading", null, headingProps);


        infoBox = lang.newRect(new Coordinates(x,10), new Coordinates(x+textBoxWidth,10+textBoxHeight), "infoBox", null, infoBoxProps);
        infoBoxMarker = lang.newRect(new Coordinates(x-5,10-5), new Coordinates(x+textBoxWidth+5,10+textBoxHeight+5), "infoBox", null, infoBoxMarkerProps);

        // ** Info Text **

    }


    /**
     * Initializes Animal Properties.
     */
    public void init(){

        // ** Heading **
        headingProps = new TextProperties();
        headingProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 24));
        headingProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

        /*
        infoTextProps = new TextProperties();
        infoTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, 12));
        infoTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
   */
        // ** InfoBox **

        infoBoxProps = new RectProperties();
        infoBoxProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        infoBoxProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        infoBoxProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

        /*
        infoBoxMarkerProps = new RectProperties();
        infoBoxMarkerProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        infoBoxMarkerProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.orange);
        infoBoxMarkerProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 4);
        */

        entities = new LinkedList<OAuthEntity>();
        protocolArrows = new LinkedList<ProtocolArrow>();
        activeEntities = 0;

    }

    public ProtocolArrow getStepWithId(int id){
        for(ProtocolArrow arrow : protocolArrows)
            if(arrow.id == id)
                return arrow;
        return null;

    }

    public void clearText(){

        for(Text line : infoBoxLines)
            line.hide();
        infoBoxLines.clear();
        infoBoxLineCount = 0;
        headingObj.hide();
        drawInfoBox();

    }

    public void clearDetailsOnly(){

        for(Text line : infoBoxLines)
            line.hide();
        infoBoxLines.clear();
        infoBoxLineCount = 0;

    }


    public void clearEntities(){
        lang.hideAllPrimitives();

        protocolArrows.clear();

        entities.clear();
        activeEntities = 0;

    }

    public void setSteps(AnimatedList steps) {
        this.stepsList = steps;
    }

    public void setHeading(String headingString) {
        this.headingString = headingString;
        headingObj.setText(headingString, new MsTiming(0), new MsTiming(0));
    }

    public void addInfoBoxLine(String content){
        addInfoBoxLine(content, infoTextProps);
    }

    public void addInfoBoxLine(String content, TextProperties props){
        Node coords = (infoBoxLineCount == 0) ?
                (Node) new Offset(10, 10, infoBox, AnimalScript.DIRECTION_NW) :
                (Node) new Offset(0, 20, infoBoxLines.get(infoBoxLineCount-1), AnimalScript.DIRECTION_NW);


        infoBoxLines.add(lang.newText(coords, content, "infoboxline", null, props));
        infoBoxLineCount++;
    }

    public void addStepDetailsLine(String content){
        addStepDetailsLine(content, infoTextProps);
    }

    public void addStepDetailsLine(String content, TextProperties props){
        Node coords = (infoBoxLineCount == 0) ?
                (Node) new Offset(10, textBoxHeight/2, infoBox, AnimalScript.DIRECTION_NW) :
                (Node) new Offset(0, 20, infoBoxLines.get(infoBoxLineCount-1), AnimalScript.DIRECTION_NW);


        infoBoxLines.add(lang.newText(coords, content, "infoboxline", null, props));
        infoBoxLineCount++;
    }

    /**
     *
     * @return returns the next free position on the half circle above the center (user head) for a given maximum of entities
     */
    public void registerEntity(OAuthEntity entity) {
        //** Register the new Entity in the linked list and assign an id.


        entity.id = activeEntities;
        activeEntities++;

        entities.add(entity);

        // show the progress! The user should feel the realignment of the Items in the circle.
        lang.nextStep();

        //** Reposition all Entities except for the first one!
        //** the first entity will be displayed in the middle.
        for (int i = 0; i < entities.size(); i++) {


            if (i == 0) {
                Coordinates target = new Coordinates(X_CENTER, Y_CENTER + 50);
                entities.get(i).moveTo(target,moveEntityTimeInMs);
                continue;
            }


            // this is where the magic happens!!! does not look too complicated after all....
            double angle = Math.toRadians(

                    // e.g.: with 4 entities we would have the factors: 1/8, 3/8, 5/8, 7/8
                    // e.g.: with 3 entities we would have the factors: 1/6, 3/6, 5/6
                    // e.g.: with 2 entities we would have the factors: 1/4 and 3/4
                    ((((float) entities.get(i).id * 2f) - 1f) / (((float) activeEntities - 1) * 2f))
                            *
                            180                                        // e.g: 8/8 are 180 degree. 180 is 100%
            );

            int x = (int) (Math.cos(angle) * radius + X_CENTER);
            int y = (int) (Math.sin(angle) * radius * (-1) + Y_CENTER); // AlgoAnim Coordinates are swapped
            Coordinates target = new Coordinates(x, y);

            entities.get(i).moveTo(target, moveEntityTimeInMs);


        }

        for (OAuthEntity curEntity : entities){
            curEntity.setUpDirection();
        }

    }

    public void registerSteps(LinkedList<ProtocolArrow> arrows){

        this.protocolArrows = arrows;
        Collections.sort(protocolArrows);
        for(ProtocolArrow arrow: protocolArrows){
            for(OAuthEntity entity : entities){
                if(arrow.source.equals(entity)){
                    entity.addArrow(arrow, arrow.startBar);
                }else if(arrow.target.equals(entity)){
                    entity.addArrow(arrow, arrow.endBar);
                }
            }


        }
        for(OAuthEntity entity : entities)
            entity.registerArrows();

        for(ProtocolArrow arrow: protocolArrows){
            arrow.update();
        }


        for(ProtocolArrow arrow : protocolArrows){
            stepsList.addContent(arrow.id, arrow.getStrLabel() + " - " + arrow.getStrDesc());
        }


    }


 }


