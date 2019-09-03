package generators.misc.oauth.utils;

/**
 * Created by Vincent on 04/07/16.
 */

import algoanim.primitives.Polyline;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import generators.misc.oauth.utils.OAuthEntity.ENTITY_DIRECTION;

import java.awt.*;
import java.util.LinkedList;

/**
 * Arrow with Label. All Animal parts of this Object can be hidden and shown with one command.
 * Creates an arrow from start to end Coordinates.
 */
public class ProtocolArrow implements Comparable<ProtocolArrow>{
    public OAuthEntity source, target;
    public Coordinates labelPos;
    private PolylineProperties arrowProp;
    private PolylineProperties arrowShaftProp;

    private TextProperties labelTextProps;
    public Text textLabel;

    private String strLabel = "";
    private String strDesc = "";

    private LinkedList<Polyline> arrow = new LinkedList<>();

    public Coordinates start, end;
    public ProtocolLayout protocol;
    public ENTITY_DIRECTION startBar, endBar;
    public int id;

    public ProtocolArrow(ProtocolLayout protocol, OAuthEntity source, OAuthEntity target, String strLabel, String strDesc, int id) {
        init();
        this.id = id;
        this.strLabel = strLabel;
        this.strDesc = strDesc;
        this.source = source;
        this.target = target;
        this.protocol = protocol;

        calculateStartAndEndBars();

    }


    private void calculateStartAndEndBars(){

        double curMin = Double.MAX_VALUE;

        for (int i = 0; i < 4; i++){
            Endpointlist curBarFromSource = source.endpointBars.get(ENTITY_DIRECTION.valueOf(i));
            Coordinates sourceBarCenter = curBarFromSource.getCenterOfBar(ENTITY_DIRECTION.valueOf(i));
            for (int j = 0; j < 4; j++) {
                Endpointlist curBarFromTarget = target.endpointBars.get(ENTITY_DIRECTION.valueOf(j));
                Coordinates targetBarCenter = curBarFromTarget.getCenterOfBar(ENTITY_DIRECTION.valueOf(j));

                double curDistance = getDistance(sourceBarCenter,targetBarCenter);

                if (curDistance < curMin){
                    startBar = ENTITY_DIRECTION.valueOf(i);
                    endBar = ENTITY_DIRECTION.valueOf(j);
                    curMin = curDistance;
                }
            }
         }
        // Something went wrong. Look here! This is Dirty.. but i want to know where the problem is
        // TODO: refactor Errorhandling..
        if(curMin == Double.MAX_VALUE)
            throw new NullPointerException();

    }



    private double getDistance(Coordinates a, Coordinates b){
        int x1 = a.getX();
        int x2 = b.getX();
        int y1 = a.getY();
        int y2 = b.getY();
        return Math.sqrt( Math.pow(x1 - x2,2) + Math.pow(y1 - y2,2));
    }

    //TODO update if entity moved
    public void createArrow(){

            Polyline ar = protocol.lang.newPolyline(new Node[]{start, end}, "", null, arrowShaftProp);
            ar.hide();
            arrow.add(ar);


            int labelX = start.getX() +  (end.getX() - start.getX() ) / 2;
            int labelY = start.getY() + (end.getY() - start.getY()) / 2 ;
            Coordinates labelPos = new Coordinates(labelX, labelY);
            textLabel = protocol.lang.newText(labelPos, strLabel,"",null,labelTextProps);
            textLabel.hide();
          //  arrow.add(protocol.lang.newPolyline(new Node[]{middle, end}, source.id + target.id + "arrow3", null, arrowProp));

    }

    public void update(){
        for(Polyline ar : arrow ){
            ar.hide();
            arrow.remove(ar);
        }
        createArrow();
    }



    public void init(){
        arrowProp = new PolylineProperties();
        arrowShaftProp = new PolylineProperties();
        arrowProp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, Boolean.TRUE);
        arrowProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        arrowShaftProp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, Boolean.TRUE);
        arrowShaftProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        arrowShaftProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);


        labelTextProps = new TextProperties();
        labelTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 18));
        labelTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    }

    public void hide(){
        for (Polyline pl : arrow)
            pl.hide();
        textLabel.hide();
    }

    public void show(){
        for (Polyline pl : arrow)
            pl.show();
        textLabel.show();


    }

    /**
     * sorts on ids of the source and target.
     * @param
     * @return
     */
    @Override
    public int compareTo(ProtocolArrow ps) {

        int targetId1 = target.id;
        int targetId2 = ps.target.id;

        int sourceId1 = source.id;
        int sourceId2 = ps.source.id;

        int distance1 = Math.abs(targetId1 - sourceId1);
        int distance2 = Math.abs(targetId2 - sourceId2);


        if(distance1 > distance2 ){
            return 1;
        }
        else if(distance1 == distance2){



            return 0;
        }else
            return -1;
    }

    public String getStrLabel() {
        return strLabel;
    }

    public void setStrLabel(String strLabel) {
        this.strLabel = strLabel;
    }

    public String getStrDesc() {
        return strDesc;
    }

    public void setStrDesc(String strDesc) {
        this.strDesc = strDesc;
    }
}

