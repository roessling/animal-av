package generators.misc.oauth.utils;

import algoanim.primitives.Primitive;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by Vincent on 04/07/16.
 */

public class OAuthUser extends ProtocolEntity{

    private String name = "User";
    private String description;
    private LinkedList<Primitive> vis;
    private Text label;
    private int x,y;
    public Coordinates tokenEndpoint;

    private CircleProperties head_prop    = new CircleProperties();


    public OAuthUser(ProtocolLayout protocol, int x, int y){
        this.x = x;
        this.y = y;
        vis = new LinkedList<Primitive>();
        // Draw the UserIcon. The head is centered at (x,y)
        vis.add(protocol.lang.newCircle(new Coordinates(x, y), 20, "userHead", null, head_prop));
        vis.add(protocol.lang.newPolyline(getPolyLineFor(x, y+20, x, y+80), "userBody", null));
        vis.add(protocol.lang.newPolyline(getPolyLineFor(x, y+30, x+20, y+50), "userRightHand", null));
        vis.add(protocol.lang.newPolyline(getPolyLineFor(x, y+30, x-20, y+50), "userLeftHand", null));
        vis.add(protocol.lang.newPolyline(getPolyLineFor(x, y+80, x-20, y+100), "userLeftFoot", null));
        vis.add(protocol.lang.newPolyline(getPolyLineFor(x, y+80, x+20, y + 100), "userRightFoot", null));


        //headerProps = new TextProperties();
        //headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
             //   Font.SANS_SERIF, Font.PLAIN, 22));

        //label = lang.newText(new Coordinates(x-20,y + 110), name,
         //       "labelPos", null, headerProps);

        tokenEndpoint = new Coordinates(x+40,y+50);

    }
    public void init(){
        // User Head properties
        head_prop.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
        head_prop.set(AnimationPropertiesKeys.FILLED_PROPERTY,  Boolean.TRUE);
        head_prop.set(AnimationPropertiesKeys.FILL_PROPERTY,  Color.WHITE);
        head_prop.set(AnimationPropertiesKeys.DEPTH_PROPERTY,  11);

    }

    private Node[] getPolyLineFor(int x1, int y1, int x2, int y2){
        Node[] vertices = {new Coordinates(x1, y1), new Coordinates(x2, y2)};
        return vertices;
    }

}