package generators.misc.oauth.utils;

import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static generators.misc.oauth.utils.OAuthEntity.ENTITY_DIRECTION.*;

public class OAuthEntity extends ProtocolEntity {

    /**
     * The Name of this Entity. This String will be displayed in a TextBox!
     */
    public String name;

    /**
     * The Animal Text Item for the Label. the variable "name" will be placed as string in this Animal Text
     */
    private Text label;

    /**
     * used for calculating the position on the layout.
     */
    public int id;

    /**
     * The Entity rectangle. It is used to visualise entities in the protocol.
     */
    private Rect bigRect;

    /**
     * Additional rectangle for the Entity. This Box is a visual container for the Animal text Label.
     */
    private Rect textRect;

    /**
     * Animal Properties for the big Rectangle
     */
    private RectProperties entityRectProp;

    /**
     * Animal Properties for the text container Rectangle
     */
    private RectProperties textRectProps;

    /**
     * Animal Text Properties for the Labeltext
     */
    private TextProperties headerProps;

    /**
     * Contains all endpoint bars.
     */
    public Map<ENTITY_DIRECTION, Endpointlist> endpointBars = new HashMap<ENTITY_DIRECTION, Endpointlist>();


    /**
     * upper left base coordinates for the Entity.
     * The visuals and the token endpoint bars are depending on this attribute
     */
    public Coordinates coords;

    /**
     * The width of the Entity.
     * The Visuals and the token endpoint bars are depending on this attribute
     */
    public int width = 150;

    /**
     * The height of the Entity.
     * The Visuals and the token endpoint bars are depending on this attribute
     */
    public int height = 150;

    /**
     * Used by the Layout to determine the appropriate endpoint bar for the arrows.
     */
    public ENTITY_DIRECTION direction = ENTITY_DIRECTION.SOUTH;

    /**
     * Used to generate Animal Script
     */
    private Language lang;

    /**
     * The layout handles the positioning of the Entities and the Arrows between the Entities.
     */
    private ProtocolLayout layout;



    /**
     *
     * @param layout
     * @param name
     */
    public OAuthEntity(ProtocolLayout layout, String name, RectProperties entityRectProp) {
        this.name = name;
        this.layout = layout;
        this.lang = layout.lang;
        this.entityRectProp = entityRectProp;
        // spawn the Entity at the center of the circle
        coords = new Coordinates(layout.X_CENTER, layout.Y_CENTER - layout.radius/2);

        bigRect = lang.newRect(coords, new Coordinates(coords.getX()+width,coords.getY()+height),
                "bigRectOAuthEntity" + id, null, entityRectProp);

        textRect = lang.newRect(new Coordinates(coords.getX(),coords.getY()+10), new Coordinates(coords.getX()+width,coords.getY()+40),
                "whiteRectOAuthEntity"+ id, null, textRectProps);
        // TODO: This is just a workaround.
        Text hideme =  lang.newText(new Coordinates(380,273), "", "", null); //random text so the next line is positioned correctly
        hideme.hide();
        // End of Workaround


        label = lang.newText(new Coordinates(coords.getX(),coords.getY()+18), name,
                "LabelOAuthEntity" + id, null, headerProps);
        setEndpointBars();

        layout.registerEntity(this);


    }

    public void init(){


        textRectProps = new RectProperties();
        textRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        textRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        textRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

        headerProps = new TextProperties();
        Font headerFont =new Font(Font.SANS_SERIF, Font.PLAIN, 22);
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, headerFont);


    }

    /**
     * Sets all Endpoint bars.
     *
     */
    public void setEndpointBars(){
        for(int i = 0; i < 4; i++){
            endpointBars.put(ENTITY_DIRECTION.valueOf(i), new Endpointlist(this, width, ENTITY_DIRECTION.valueOf(i)));
        }
    }


    /**
     * adds an arrow to the best fitting endpoints list. (north, west, east, south)
     */
    public void addArrow(ProtocolArrow arrow, ENTITY_DIRECTION facingDirection){

        endpointBars.get(facingDirection).addArrow(arrow);

    }

    public void registerArrows(){
        for(int i = 0; i < 4; i ++){
            endpointBars.get(ENTITY_DIRECTION.valueOf(i)).registerArrows();
        }
    }

    public void moveTo(Coordinates target, int moveEntityTimeInMs){

        bigRect.moveBy("translate",
                target.getX()-coords.getX() , target.getY()-coords.getY(),
                new MsTiming(0), new MsTiming(moveEntityTimeInMs));

        textRect.moveBy("translate",
                target.getX()-coords.getX() , target.getY()-coords.getY(),
                new MsTiming(0), new MsTiming(moveEntityTimeInMs));

        label.moveBy("translate",
                target.getX()-coords.getX() , target.getY()-coords.getY(),
                new MsTiming(0), new MsTiming(moveEntityTimeInMs));
        coords = target;

        //TODO:  update Arrows as well!!

        setUpDirection();
        for(int i = 0; i < 4; i++){
            Endpointlist curList = endpointBars.get(ENTITY_DIRECTION.valueOf(i));
            curList.update();

        }


    }

    public Coordinates getTokenEndpointBase(ENTITY_DIRECTION dir){
        Coordinates result = new Coordinates(0,0);
        Coordinates tmpCoords = coords;
        int tmpHeight = height;
        int tmpWidth = width;

        switch(dir){
            case NORTH:
                result = new Coordinates(tmpCoords.getX(),tmpCoords.getY());
                break;
            case SOUTH:
                result = new Coordinates(tmpCoords.getX()+tmpWidth,tmpCoords.getY()+tmpHeight);
                break;
            case EAST:
                result = new Coordinates(tmpCoords.getX()+tmpWidth,tmpCoords.getY());
                break;
            case WEST:
                result = new Coordinates(tmpCoords.getX(),tmpCoords.getY()+tmpHeight);
                break;
        }
        return result;
    }


    public void setUpDirection(){
        int x = coords.getX();
        if (id == 0) {
            direction = SOUTH;
            return;
        }
        if (x > layout.X_CENTER) {
            direction = NORTH;
        }
        else if (x < layout.X_CENTER) {
            direction = WEST;

        } else if (x > layout.X_CENTER) {
            direction = EAST;
        }
    }

    public void hide(){
        textRect.hide();
        bigRect.hide();
        label.hide();
    }
    public void show(){
        textRect.show();
        bigRect.show();
        label.show();
    }


    /**
     * For the Calculation of the Direction we need to assign numbers to the directions, so we can calculate
     * (implicitly with the knowledge of the mapping..)
     * ----
     *
     * ... more accurate: we can put logic in other functions
     *
     */
    public enum ENTITY_DIRECTION {

        SOUTH(0), WEST(1), NORTH(2), EAST(3);

        private int dirNum;
        private static Map<Integer, ENTITY_DIRECTION> map = new HashMap<Integer, ENTITY_DIRECTION>();

        static {
            for (ENTITY_DIRECTION dirEnum : ENTITY_DIRECTION.values()) {
                map.put(dirEnum.dirNum, dirEnum);
            }
        }

        ENTITY_DIRECTION(final int dir) { dirNum = dir; }

        public static ENTITY_DIRECTION valueOf(int dirNo) {
            return map.get(dirNo);
        }

        public int getValue(){
            return dirNum;
        }

    }



}
