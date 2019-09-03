package generators.misc.oauth.utils;

import algoanim.util.Coordinates;

import java.util.Collections;
import java.util.LinkedList;


import static generators.misc.oauth.utils.OAuthEntity.*;
import static generators.misc.oauth.utils.OAuthEntity.ENTITY_DIRECTION.*;

// TODO: Don't store coordinates. Store ProtocolSteps! sorting all ProtocolSteps based on their other side! :)
public class Endpointlist{

    private LinkedList<ProtocolArrow> arrows;

    private LinkedList<Coordinates> coordinates;

    private int ptrFirst, ptrLast;

    private Coordinates endpointBaseCoordinate;

    private ENTITY_DIRECTION direction;

    private OAuthEntity host;

    private int pxLength;


    public Endpointlist(OAuthEntity host, int pxLength, ENTITY_DIRECTION direction){
        this.direction = direction;
        this.pxLength = pxLength;
        this.host = host;
        endpointBaseCoordinate = host.getTokenEndpointBase(direction);
        arrows = new LinkedList<>();
        coordinates = new LinkedList<>();

    }

    public void update(){
        endpointBaseCoordinate = host.getTokenEndpointBase(direction);

        for(ProtocolArrow ar : arrows){
            ar.update();
        }

    }

    public void addArrow(ProtocolArrow input) {

        // add the new arrow to the arrows list
        arrows.add(input);
    }

    public void registerArrows(){
        // recalculating the arrows placeholder coordinates
        setupCoordinates();
        Collections.sort(arrows);

        // reassign the placeholder coordinates to the arrow endpoints.
        for(ProtocolArrow curArrow : arrows){


            if(host.equals(curArrow.source)){

                if(isFirstLast(curArrow.source, curArrow.target, curArrow.startBar, curArrow.endBar)){
                    curArrow.start = getNextFree();

                }else{
                    curArrow.start = getLastFree();

                }

            }else if(host.equals(curArrow.target)){

                if(isFirstLast(curArrow.source, curArrow.target, curArrow.startBar, curArrow.endBar)){
                    curArrow.end = getLastFree();
                }else{
                    curArrow.end = getNextFree();
                }

            }else
                throw new NullPointerException(); //DEBUG!!!

        }



    }

    private Coordinates getNextFree(){
        Coordinates result = coordinates.get(ptrFirst);
        ptrFirst++;
        return result;
    }

    private Coordinates getLastFree(){
        Coordinates result = coordinates.get(ptrLast);
        ptrLast--;
        return result;
    }

    public void setupCoordinates(){
        if(arrows.isEmpty())
            return;
        int offset =  pxLength / arrows.size();

        int x = endpointBaseCoordinate.getX();
        int y = endpointBaseCoordinate.getY();

        coordinates.clear();
        ptrFirst = 0;
        ptrLast = arrows.size() ;


        for(int i = 0; i <= arrows.size(); i ++){
            coordinates.add(applyOffsetAndGetCoordinates(direction, i*offset, x, y));
        }

    }
    ///
    private Boolean isFirstLast(OAuthEntity source, OAuthEntity target, ENTITY_DIRECTION startBar, ENTITY_DIRECTION endBar){
        if(startBar == NORTH && endBar == EAST){
            return true;
        }else if (startBar == EAST && endBar == SOUTH){
            return true;
        }else if(startBar == SOUTH && endBar == WEST) {
            return true;
        }else if(startBar == SOUTH && endBar == NORTH){
            return true;

        }else if (startBar == EAST && endBar == WEST){
            if(source.id < target.id){
                return false; // fixed crossing lines from east to west (clockwise).
            }
            return true; // fixed crossing lines from east to west (counter clockwise).

        }
        else
            return false;

    }

    public Coordinates getCenterOfBar(ENTITY_DIRECTION dir){
        return applyOffsetAndGetCoordinates(dir, pxLength/2, endpointBaseCoordinate.getX(), endpointBaseCoordinate.getY());
    }



    /**
     * applies the offset to x or y depending on the facingDirection.
     * @return
     */
    private Coordinates applyOffsetAndGetCoordinates(ENTITY_DIRECTION dir, int offset, int x, int y){
        Coordinates result = null;
        switch(dir){
            case SOUTH:
                result = new Coordinates(x - offset, y);
                break;
            case WEST:
                result = new Coordinates(x, y - offset);
                break;

            case NORTH:
                result = new Coordinates(x + offset, y);
                break;

            case EAST:
                result = new Coordinates(x, y + offset);
                break;
        }
        return result;
    }


    public Coordinates getEndpointBaseCoordinate() {
        return endpointBaseCoordinate;
    }

    public void setEndpointBaseCoordinate(Coordinates endpointBaseCoordinate) {
        this.endpointBaseCoordinate = endpointBaseCoordinate;
    }


}