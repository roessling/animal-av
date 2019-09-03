package generators.hardware.prefixAdderAnimation.util;

import algoanim.primitives.*;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

/**
 * Created by philipp on 27.07.15.
 */
//TODO rectify wires (or at least make them not so ugly)
public class HwGateDrawer {

    private Language language;
    private TicksTiming instant = new TicksTiming(0);
    private CircleSegProperties circProperties;
    private ArcProperties arcProperties;

    public HwGateDrawer(Language l){
        this.language = l;
        setPropaties();

    }
    private void setPropaties(){
        //circle
        circProperties = new CircleSegProperties("circProp");
        circProperties.set(AnimationPropertiesKeys.ANGLE_PROPERTY, -180);
        //arc
        arcProperties = new ArcProperties("arcProp");
        arcProperties.set(AnimationPropertiesKeys.ANGLE_PROPERTY,-90);
        arcProperties.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, -45);
    }

    /**
     * Draws an And Gate (US ANSI 91-1984 notation)
     * @param center
     * @param rad
     */

    private Node[] drawAnd(Node center, int rad){
        int radHalf = rad/2;
        CircleSeg sg = language.newCircleSeg(center, rad, "circ", instant, circProperties);
        Node[] coords = {   new Offset(0    , rad,sg,"NW"), new Offset(    0, -rad,sg,"NW"),
                            new Offset(2*rad,-rad,sg,"NW"), new Offset(2*rad,  rad,sg,"NW")};
        Polyline polyline = language.newPolyline(coords, "line", instant);
        Node[] pins ={new Offset(radHalf, -rad,sg,"NW"),new Offset(rad+radHalf,-rad,sg,"NW"),
                      new Offset(0,0,sg,"S")};
        return pins;
    }

    /**
     * Draws an Or Gate (US ANSI 91-1984 notation)
     * @param center
     * @param rad
     */
    private Node[] drawOr(Node center, int rad){
        int radHalf = rad/2;
        CircleSeg sg = language.newCircleSeg(center, rad, "circ", instant, circProperties);
        Node[] coordsLeft =  {new Offset(0    , rad,sg,"NW"), new Offset(0    ,-rad,sg,"NW")};
        Node[] coordsRight = {new Offset(2*rad,-rad,sg,"NW"), new Offset(2*rad, rad,sg,"NW")};
        Polyline polylineLeft  = language.newPolyline(coordsLeft , "leftLine" , instant);
        Polyline polylineRight = language.newPolyline(coordsRight, "rightLine", instant);
        int arcRad = (int)(1.5 * rad);
        Arc arc = language.newArc(new Offset(0, -2 * rad, sg, "N"), new Coordinates(arcRad, arcRad),
                "arc", instant, arcProperties);
        Node[] pins ={new Offset(radHalf, -rad+5,sg,"NW"),new Offset(rad+radHalf,-rad+5,sg,"NW"),
                new Offset(0,0,sg,"S")};
        return pins;
    }

    /**
     * Draws an XOR-Gate (US ANSI 91-1984 notation)
     * @param center
     * @param rad
     */
    private   Node[] drawXor(Node center, int rad){
        int radHalf = rad/2;
        CircleSeg sg = language.newCircleSeg(center,rad,"circ",instant, circProperties);
        Node[] coordsLeft =  {new Offset(0    , rad,sg,"NW"), new Offset(0    ,-rad,sg,"NW")};
        Node[] coordsRight = {new Offset(2*rad,-rad,sg,"NW"), new Offset(2*rad, rad,sg,"NW")};
        Polyline polylineLeft  = language.newPolyline(coordsLeft , "leftLine" , instant);
        Polyline polylineRight = language.newPolyline(coordsRight, "rightLine", instant);
        int arcRad = (int)(1.5 * rad);
        Arc arc  = language.newArc(new Offset(0, -2 * rad, sg, "N"), new Coordinates(arcRad, arcRad),
                "arc", instant, arcProperties);
        int secondRadDist = (int)(0.25 * rad);
        Arc arc2 = language.newArc(new Offset(0, -2 * rad - secondRadDist, sg, "N"), new Coordinates(arcRad, arcRad),
                "arc", instant, arcProperties);
        Node[] pins ={new Offset(radHalf, -rad,sg,"NW"),new Offset(rad+radHalf,-rad,sg,"NW"),
                new Offset(0,0,sg,"S")};
        return pins;
    }

    public void drawInputBlock(Square frame, int sideLength){
        //calculation parameters for positions
        int rad = sideLength/8;
        int radHalf = rad/2;
        Offset leftBlockPos    = new Offset(    sideLength / 4, 7 * sideLength / 12, frame, "NW");
        Offset rightBlockPos   = new Offset(3 * sideLength / 4, 7 * sideLength / 12, frame, "NW");
        Offset firstInputPos   = new Offset(    sideLength / 4 - radHalf, 2, frame, "NW");
        Offset secondInputPos  = new Offset(3 * sideLength / 4 + radHalf, 2, frame, "NW");
        Offset firstOutputPos  = new Offset(    sideLength / 4, sideLength - 14, frame, "NW");
        Offset secondOutputPos = new Offset(3 * sideLength / 4, sideLength - 14, frame, "NW");
        //Font
        TextProperties txtProp = new TextProperties("txtProp");
        // Primitives
        Node[] orPins  = drawOr(leftBlockPos, rad);
        Node[] andPins = drawAnd(rightBlockPos, rad);
        Text inputA = language.newText(firstInputPos, "A", "inputA", instant, txtProp);
        Text inputB = language.newText(secondInputPos, "B", "inputB", instant, txtProp);
        Text outputP = language.newText(firstOutputPos,"P","outputP",instant,txtProp);
        Text outputG = language.newText(secondOutputPos, "G", "outputG", instant, txtProp);
        //
        Node AndToGCoord[] = {andPins[2], new Offset(0,-2,outputG,"NW")};
        Polyline AndToG = language.newPolyline(AndToGCoord, "OrToG", instant);
        Node OrToPCoord[] = {orPins[2], new Offset(0,-2,outputP,"NW")};
        Polyline OrToP = language.newPolyline(OrToPCoord,"OrToP",instant);
        //
        Node AtoOrCoord[] = {new Offset(0,2,inputA,"SW"),orPins[0]};
        Polyline AtoOr  = language.newPolyline(AtoOrCoord,"AtoOr",instant);
        Node AtoAndCoord[] = {new Offset(0,2,inputA,"SW"),andPins[0]};
        Polyline AtoAnd = language.newPolyline(AtoAndCoord,"AtoAnd",instant);
        Node BtoAndCoord[] = {new Offset(0,2,inputB,"SW"),andPins[1]};
        Polyline BtoAnd = language.newPolyline(BtoAndCoord,"BtoAnd",instant);
        Node BtoOrCoord[] = {new Offset(0,2,inputB,"SW"),orPins[1]};
        Polyline BtoOr = language.newPolyline(BtoOrCoord, "BtoOr",instant);
        //drawWire(new Offset(0,0,inputA,"S"),20,20);

    }
    public void drawInternalBlock(Square frame, int sideLength){
        int inputMargin = sideLength/4;
        int rad = sideLength/12;
        Offset leftAnd    = new Offset(    sideLength / 5, 7 * sideLength / 12, frame, "NW");
        Offset rightAnd   = new Offset(4 * sideLength / 5, 5 * sideLength / 12, frame, "NW");
        Offset or =         new Offset(3 * sideLength / 5, 9 * sideLength / 12 ,frame, "NW");
        Text inputP1 = language.newText(new Offset(              5,2,frame,"NW"),"P_u","P1",instant);
        Text inputP2 = language.newText(new Offset(  inputMargin+5,2,frame,"NW"),"P_l","P2",instant);
        Text inputG1 = language.newText(new Offset(2 * inputMargin + 5, 2, frame, "NW"), "G_u", "G1", instant);
        Text inputG2 = language.newText(new Offset(3 * inputMargin + 5, 2, frame, "NW"), "G_l", "G2", instant);
        Text outputP = language.newText(new Offset(sideLength / 5, sideLength - 14, frame, "NW"), "P", "outP", instant);
        Text outputG = language.newText(new Offset(3 * sideLength / 5, sideLength - 14, frame, "NW"), "G", "outG", instant);
        Node[] andLeftPins  = drawAnd(leftAnd, rad);
        Node[] andRightPins = drawAnd(rightAnd, rad);
        Node[] orPins       = drawOr(or, rad);
        //
        Node[] p1toLeftAndCoords  = {new Offset(0,2,inputP1,"SW"),andLeftPins[0]};
        Node[] p2toLeftAndCoords  = {new Offset(0,2,inputP2,"SW"),andLeftPins[1]};
        Node[] g1toOrCoords       = {new Offset(0,2,inputG1,"SW"),orPins[0]};
        Node[] g2toRightAndCoords = {new Offset(0,2,inputG2,"SW"),andRightPins[1]};
        Node[] p1toRightAndCoords = {new Offset(0,2,inputP1,"SW"),andRightPins[0]};
        Node[] andToOrCoords      = {andRightPins[2],orPins[1]};
        Node[] andToPoutCoords    = {andLeftPins[2],new Offset(0,-2,outputP,"NW")};
        Node[] orToGoutCoord      = {orPins[2],new Offset(0,-2,outputG,"NW")};
        //
        Polyline p1toLeftAnd  = language.newPolyline(p1toLeftAndCoords,"P1toLeftAnd",instant);
        Polyline p2toLeftAnd  = language.newPolyline(p2toLeftAndCoords,"P2toLeftAnd",instant);
        Polyline g1toOr       = language.newPolyline(g1toOrCoords,"G1toOr",instant);
        Polyline g2toRightAnd = language.newPolyline(g2toRightAndCoords,"G2toRightAnd",instant);
        Polyline p1toRightAnd = language.newPolyline(p1toRightAndCoords,"P1toRightAnd",instant);
        Polyline andToOr      = language.newPolyline(andToOrCoords,"AndToOr",instant);
        Polyline andToP       = language.newPolyline(andToPoutCoords,"AndToP",instant);
        Polyline orToG        = language.newPolyline(orToGoutCoord,"OrToG",instant);
    }
    public void drawOutputBlock(Square frame, int sideLength){
        int rad = sideLength/12;
        int inputMargin = sideLength/3;
        Offset firstXorPos  = new Offset(sideLength/4, 5*sideLength/12,frame,"NW");
        Offset secondXorPos = new Offset(sideLength/2, 9*sideLength/12,frame,"NW");
        Text inputA  = language.newText(new Offset(                  5,2, frame, "NW"), "A", "inA", instant);
        Text inputB  = language.newText(new Offset(    inputMargin + 5,2, frame, "NW"), "B", "inB", instant);
        Text inputG  = language.newText(new Offset(2 * inputMargin + 5,2, frame, "NW"), "G", "inG", instant);
        Text outputS = language.newText(new Offset(sideLength/2,sideLength-14,frame,"NW"), "S", "outS", instant);
        Node[] firstXorPins  = drawXor(firstXorPos ,rad);
        Node[] secondXorPins = drawXor(secondXorPos,rad);
        //
        Node[] AtoXorCoords   = {new Offset(0,2,inputA,"SW"),firstXorPins[0]};
        Node[] BtoXorCoords   = {new Offset(0,2,inputB,"SW"),firstXorPins[1]};
        Node[] XorToXorCoords = {firstXorPins[2],secondXorPins[0]};
        Node[] GtoXorCoords   = {new Offset(0,2,inputG,"SW"),secondXorPins[1]};
        Node[] XorToSCoords   = {secondXorPins[2], new Offset(0,-2,outputS,"NW")};
        //
        Polyline AtoXor   = language.newPolyline(AtoXorCoords  ,"AtoXor",instant);
        Polyline BtoXor   = language.newPolyline(BtoXorCoords  ,"BtoXor",instant);
        Polyline XorToXor = language.newPolyline(XorToXorCoords,"XorToXor",instant);
        Polyline GtoXor   = language.newPolyline(GtoXorCoords, "GtoXor",instant);
        Polyline XorToS   = language.newPolyline(XorToSCoords, "XorToS",instant);
    }
}
