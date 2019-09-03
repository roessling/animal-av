package generators.sorting.bogosort;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Point;
import algoanim.primitives.Rect;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PointProperties;
import algoanim.properties.RectProperties;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;


public class BarArray {
	Language lang;
	LinkedList<Rect> barList;
	HashMap<String, BarArrayMarker> markers;
	RectProperties elementProperties;
	Rect boundingBox;
		
	Color highlightColor;
	
	int[] list;
	int totalWidth, totalHeight, elementWidth, gapWidth, markerOverlap = 5, maxValue = Integer.MIN_VALUE;
	
	public BarArray(Language lang, RectProperties elementProperties, int[] list) {
		this.lang = lang;
		this.elementProperties = elementProperties;
		barList = new LinkedList<Rect>();
		markers = new HashMap<String,BarArrayMarker>();	
		this.list = list;
		this.highlightColor = Color.CYAN;
	}
	
	public void setHighlightColor(Color color) {
		this.highlightColor = color;
	}
	
	public Rect placeGivenMax(Node upperLeft, int totalWidth, int totalHeight, int maxValue){
		this.maxValue = maxValue;
		return this.place(upperLeft, totalWidth,totalHeight, 3);
	}
	
	public Rect place(Node upperLeft, int totalWidth, int totalHeight){
		return this.place(upperLeft, totalWidth,totalHeight, 3);
	}
	
	public Rect place(Node upperLeft, int totalWidth, int totalHeight, int gapWidth) {
		this.totalWidth = totalWidth;
		this.totalHeight = totalHeight;	
		this.gapWidth = gapWidth;
		this.elementWidth = (int) Math.floor(((double)totalWidth - (list.length-1.0)*gapWidth)/list.length);
		if(this.elementWidth < 4)  {
			this.elementWidth = 4; // Minimum size for appropriate visualization
			this.totalWidth = (elementWidth + gapWidth) * list.length;
		}
		
		// bizarrely, newOffset throws NullmarkerExceptions when using Nodes as reference ... 
		// so we create a useless Point
		PointProperties pp = new PointProperties();
		pp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
		Point upperLeftPoint = lang.newPoint(upperLeft, "upperLeftHelper", null, pp);
		
		RectProperties  bbProbs = new RectProperties();
		bbProbs.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE
				);
		Offset lowerRight = new Offset(totalWidth,totalHeight,upperLeftPoint,AnimalScript.DIRECTION_NE);
		boundingBox = lang.newRect(upperLeft, lowerRight, "BarArrayBoundingBox",null,bbProbs);
		
		place();
		return boundingBox;
	}
	
	private void place() {
		if(this.maxValue == Integer.MIN_VALUE){ 
			for(int i=0;i<list.length;i++){
				if(list[i] > maxValue) maxValue = list[i];
			}
		} 
		int elHeight,elOffsetLeft, elOffsetTop;
		Node elUpperLeft,elLowerRight;
		for(int i=0;i<list.length;i++) {
			elHeight = (int) Math.round(((double)list[i] / (double)maxValue) * ((double)totalHeight-(double)markerOverlap*2));
			elOffsetLeft = i*(elementWidth+gapWidth);
			elOffsetTop = totalHeight-elHeight-markerOverlap;
			elUpperLeft = new Offset(elOffsetLeft,elOffsetTop,boundingBox,AnimalScript.DIRECTION_NW);
			elLowerRight = new Offset(elOffsetLeft+elementWidth,elOffsetTop+elHeight,boundingBox,AnimalScript.DIRECTION_NW);
			barList.add(lang.newRect(elUpperLeft, elLowerRight, "BarArrayElement#"+i, null, elementProperties));
		}
	}
	
	public void moveBy(int dx, int dy, Timing delay, Timing duration) {
		// TODO move all bars + markers to new position
		// first attempt: try to only move the bounding box
		this.boundingBox.moveBy("translate", dx, dy, delay, duration);
		for(Rect r:barList) {
			r.moveBy("translate", dx, dy, delay, duration);
		}
		for(String m:markers.keySet()) {
			markers.get(m).moveBy("translate", dx, dy, delay, duration);
		}
	}
	
	public void hide() {
		for(Rect r : this.barList) {
			r.hide();
		}
		hideMarkers();
	}
	
	public void hide(Timing delay) {
		for(Rect r : this.barList) {
			r.hide(delay);
		}
		hideMarkers(delay);
	}
	public void resetMarkers() {
		for(String m:this.markers.keySet()) {
			this.markers.get(m).reset();
		}
	}
	
	public void hideMarkers(Timing delay) {
		for(String m: this.markers.keySet()) {
			this.markers.get(m).hide(delay);
		}
	}
	
	public void hideMarkers() {
		for(String m: this.markers.keySet()) {
			this.markers.get(m).hide();
		}
	}
	
	public void show() {
		for(Rect r : this.barList){
			r.show();
		}
	}
	
	public void showAll() {
		show();
		for(String m: this.markers.keySet()) {
			this.markers.get(m).show();
		}
	}
	
	public Rect getBoundingBox() {
		return this.boundingBox;
	}
	
	public void swap(int firstElement, int secondElement, Timing delay, Timing duration) {
		Rect a = this.barList.get(firstElement);
		Rect b = this.barList.get(secondElement);
	
		int distance = (secondElement - firstElement) * (elementWidth + gapWidth);
		
		a.moveBy("translate", distance,0, delay, duration);
		b.moveBy("translate", -distance,0, delay, duration);
		
		this.barList.set(firstElement, b);
		this.barList.set(secondElement,a);
	}
	
	public void highlight(int index) { this.highlight(index,null,null); }
	public void highlight(int index, Timing delay, Timing duration) {
		this.highlight(index,this.highlightColor,delay,duration);
	}
	
	public void highlight(int startIndex, int endIndex, Color color) { this.highlight(startIndex, endIndex, color, null, null); }
	public void highlight(int startIndex, int endIndex) { this.highlight(startIndex, endIndex, this.highlightColor, null, null); }
	public void highlight(int startIndex, int endIndex, Color color, Timing delay, Timing duration) {
		for(int i = startIndex;i<=endIndex;i++) {
			this.highlight(i, color, delay, duration);
		}
	}
	
	
	public void highlight(int index, Color color) {	this.highlight(index,color,null,null); }
	public void highlight(int index, Color color, Timing delay, Timing duration) {
		if(index >= this.barList.size() || index < 0) return;
		this.barList.get(index).changeColor("fillColor", color, delay, duration);
		this.barList.get(index).changeColor("color", color, delay, duration);
	}	
	
	public void highlightList() {
		this.boundingBox.changeColor("fillColor", highlightColor, null, null);
	}
	
	public void unhighlightList() {
		this.boundingBox.changeColor("fillColor", Color.WHITE, null, null);
	}
	
	
	public void unhighlight() {
		for(int i = 0;i<barList.size();i++)
			this.unhighlight(i);
	}
	
	public void unhighlight(int index) { this.unhighlight(index, null, null); }
	public void unhighlight(int index, Timing delay, Timing duration) {
		if(index >= this.barList.size() || index < 0) return;
		Color stdColor = (Color) this.elementProperties.get("fillColor");
		this.barList.get(index).changeColor("color", stdColor, delay, duration);
		this.barList.get(index).changeColor("fillColor", stdColor, delay, duration);
	}
	
	
	public void unhighlight(int startIndex, int endIndex) { this.unhighlight(startIndex, endIndex, null, null); }
	public void unhighlight(int startIndex, int endIndex, Timing delay, Timing duration) {
		for(int i = startIndex;i<=endIndex;i++) {
			this.unhighlight(i, delay, duration);
		}
	}
	
	public void addMarker(BarArrayMarker marker) {
		this.markers.put(marker.getName(), marker);
	}
	
	public BarArrayMarker getMarker(String name) {
		return this.markers.get(name);
	}
	
	public int getSlotWidth() {
		return this.gapWidth + this.elementWidth;
	}
	public int getElementWidth() {
		return this.elementWidth;
	}
	public int getHeight() {
		return this.totalHeight;
	}
	public int getWidth() {
		return this.totalWidth;
	}
	public int getLength() {
		return this.list.length;
	}
}
