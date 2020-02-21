package generators.network.chandylamport;

import algoanim.primitives.Polyline;
import algoanim.primitives.Text;

public class Marker {

	public enum MarkerState{
		NOT_SENT,SENT,RECEIVED
	}
	
	private Text markerText;
	private Polyline markerLine;
	private MarkerState state;
	
	public Marker(Text markerText, Polyline markerLine) {
		this.markerText = markerText;
		this.markerLine = markerLine;
		this.state = MarkerState.NOT_SENT;
	}

	public Text getMarkerText() {
		return markerText;
	}

	public void setMarkerText(Text markerText) {
		this.markerText = markerText;
	}

	public Polyline getMarkerLine() {
		return markerLine;
	}

	public void setMarkerLine(Polyline markerLine) {
		this.markerLine = markerLine;
	}

	public MarkerState getState() {
		return state;
	}

	public void setState(MarkerState state) {
		this.state = state;
	}
	
}
