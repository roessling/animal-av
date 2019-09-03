package generators.sorting.bogosort;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.generators.Language;
import algoanim.properties.RectProperties;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;

public class BarArrayMarker {
	private String name;
	private BarArray barArray;
	private Rect marker;
	//private RectProperties markerProperties;
	//private Language lang;
	
	BarArrayMarker(Language lang, String name, BarArray barArray, RectProperties markerProperties) {
		//this.lang = lang;
		this.name = name;
		this.barArray = barArray;
		//this.markerProperties = markerProperties;
		Node upperLeft, lowerRight;
		upperLeft = new Offset(barArray.getElementWidth()/4,0,barArray.getBoundingBox(),AnimalScript.DIRECTION_NW);
		lowerRight = new Offset(barArray.getElementWidth()-barArray.getElementWidth()/4,barArray.getHeight(),barArray.getBoundingBox(),AnimalScript.DIRECTION_NW);
		marker = lang.newRect(upperLeft, lowerRight, "barArrayMarker_"+name, null, markerProperties);
		marker.hide();
		barArray.addMarker(this);
	}
	
	public void linkToBarArray(BarArray barArray) {
		this.barArray = barArray;
	}
	
	public void show() { this.show(null); }
	public void show(Timing delay) {
		marker.show(delay);
	}
	
	public void hide() { this.hide(null); }
	public void hide(Timing delay) {
		marker.hide(delay);
	}
	
	public void increment() { this.increment(null,null); }
	public void increment(Timing delay, Timing duration) {
		this.moveBy(1,delay,duration);
	}
	
	public void decrement() { this.decrement(null,null); }
	public void decrement(Timing delay, Timing duration) {
		this.moveBy(-1, delay, duration);
	}
	
	public void reset() {this.reset(null);}
	public void reset(Timing delay) {
		this.setTo(0, delay, null);
		this.hide(delay);
	}
	
	public void setTo(int index) { this.setTo(index,null,null); }	
	public void setTo(int index, Timing delay, Timing duration) {
		int leftOffset = index * barArray.getSlotWidth() + barArray.getElementWidth()/4;
		Node target = new Offset(leftOffset,0,barArray.getBoundingBox(),AnimalScript.DIRECTION_NW);
		marker.moveTo(AnimalScript.DIRECTION_SW, "translate", target, delay, duration);
	}
	
	public void moveBy(String moveType, int dx, int dy, Timing delay, Timing duration) { marker.moveBy(moveType, dx, dy, delay, duration); }
	public void moveBy(int indexes) {this.moveBy(indexes,null,null); }
	public void moveBy(int indexes, Timing delay, Timing duration) {
		marker.moveBy("translate", indexes * barArray.getSlotWidth(), 0, delay, duration);
	}
	
	public String getName() { return this.name; }

}
