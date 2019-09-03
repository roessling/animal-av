package animal.vhdl.vhdlscript;

import java.awt.Point;
import java.util.ArrayList;

import animal.graphics.PTPoint;

public class AnimationTract {
	private ArrayList<WireLane> horizontalLanes;
	private ArrayList<WireLane> verticalLanes;
	private boolean forWire=true;	
	private Point location;
	
	public AnimationTract(){		
		horizontalLanes=new ArrayList<WireLane>();
		verticalLanes=new ArrayList<WireLane>();
	}

	public boolean isForWire() {
		return forWire;
	}

	public void setForWire(boolean isForWire) {
		this.forWire = isForWire;		
	}

	public ArrayList<WireLane> getHorizontalLanes() {
		return horizontalLanes;
	}

	public ArrayList<WireLane> getVerticalLanes() {
		return verticalLanes;
	}
	
	public void addHorizontalLanes(WireLane lane){
		verticalLanes.add(lane);
	}
	
	public void addVerticalLanes(WireLane lane){
		verticalLanes.add(lane);
	}


	public boolean isFreeInVerticalLanes(int number, PTPoint holder) {
		if(verticalLanes==null || verticalLanes.size()<=number)
			return true;
		if(verticalLanes.get(number).getHolder()==holder || verticalLanes.get(number).getHolder()==null)
			return true;
		return false;
	}

	public boolean isFreeInHorizontalLanes(int number, PTPoint holder) {
		if(horizontalLanes==null || horizontalLanes.size()<=number)
			return true;
		if(horizontalLanes.get(number).getHolder()==holder)
			return true;
		return false;
	}

	public void setVerticalLanesHolder(int laneNr, PTPoint holder) {
		if(verticalLanes==null || verticalLanes.size()<=laneNr){
			do{
				verticalLanes.add(new WireLane(null));
			}while(verticalLanes.size()<=laneNr);			
		}
		verticalLanes.get(laneNr).setHolder(holder);
		for(int i=0;i<verticalLanes.size();i++){
			if(i!=laneNr && verticalLanes.get(i).getHolder()==holder){
				verticalLanes.get(i).setHolder(null);
			}
		}
	}

	public void setHorizontalLanesHolder(int laneNr, PTPoint holder) {
		if(horizontalLanes==null || horizontalLanes.size()<=laneNr){
			do{
				horizontalLanes.add(new WireLane(null));
			}while(horizontalLanes.size()<=laneNr);			
		}
		horizontalLanes.get(laneNr).setHolder(holder);
		for(int i=0;i<horizontalLanes.size();i++){
			if(i!=laneNr && horizontalLanes.get(i).getHolder()==holder){
				horizontalLanes.get(i).setHolder(null);
			}
		}		
	}

	public int getHorizontalLanesNr() {
		if(horizontalLanes==null)
			return 0;
		return horizontalLanes.size();
	}

	public int getVerticalLanesNr() {
		if(verticalLanes==null)
			return 0;
		return verticalLanes.size();
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}
	

}
