package generators.misc.hepersGlicko2;

import java.awt.Color;
import java.awt.Font;

import algoanim.primitives.Rect;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import generators.helpers.OffsetCoords;

@SuppressWarnings("unused")
public class Glicko2VisualRating {
	private Node upperLeftCorner;
	private Node lowerLeftCorner;
	//private Glicko2Player[] players;
	//private Node[] rectPos;
	//private Rect[] ratings;
	//private double[] ratingsVar;
	private VisualBar[] players;
	private Language lang;
  private double scale;
	private int min = Integer.MAX_VALUE;
	RectProperties rectProps;
	Color barColor;
	
	class VisualBar {
		public Glicko2Player player;
		public Node pos;
		public Rect currRect;
		public double ratingVariance;
	}
	
	public Glicko2VisualRating(Language lang, Node position, Glicko2Player[] players, Color barColor) {
		upperLeftCorner = position;
		this.players = new VisualBar[players.length];
		for (int i = 0; i < players.length; i++) {
			this.players[i] = new VisualBar();
			this.players[i].player = players[i]; 
		}
		this.lang = lang;
		this.barColor = barColor;
		
		init();
	}
	
	private void init() {
		TextProperties props = new TextProperties();
		props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		
		//Find min and max to position properly
		int max = Integer.MIN_VALUE;
		for (VisualBar p : players) {
			int valueMin = (int)(p.player.getRating() - p.player.getRatingDev());
			int valueMax = (int)(p.player.getRating() + p.player.getRatingDev());
			if (min > valueMin)
				min = valueMin;
			if (max < valueMax)
				max = valueMax;
		}
		
		//Get Positions for the Graph outline
		lowerLeftCorner = new OffsetCoords(upperLeftCorner, 0, 20 * (players.length + 1));
		Node lowerRightCorner = new OffsetCoords(lowerLeftCorner, (max - min)/4 + 20, 0);
		
		//Get Lower Markers
		Node firstMarkerTop = new OffsetCoords(lowerLeftCorner, 20, -5);
		Node firstMarkerBottem = new OffsetCoords(firstMarkerTop, 0, 10);
		lang.newPolyline(new Node[] {firstMarkerTop,  firstMarkerBottem}, "", null);
		Node text = new OffsetCoords(firstMarkerBottem, -13, 5);
		lang.newText(text, min + "", "", null, props);
		
		Node secondMarkerTop = new OffsetCoords(firstMarkerTop, (max-min)/5, 0);
		Node secondMarkerBottom = new OffsetCoords(secondMarkerTop, 0, 10);
		lang.newPolyline(new Node[] {secondMarkerTop,  secondMarkerBottom}, "", null);
		text = new OffsetCoords(secondMarkerBottom, -13, 5);
		lang.newText(text, max + "", "", null, props);
		
		lang.newPolyline(new Node[] { upperLeftCorner, lowerLeftCorner, lowerRightCorner }, "graph", null);
		for (int i = 0; i < players.length; i++) {
			//Draw Row Markers for the player
			Node left = new OffsetCoords(lowerLeftCorner, -5 , -20 * (i+1));
			Node right = new OffsetCoords(left, 10, 0);
			players[i].pos = right;

			lang.newPolyline(new Node[] {left,  right}, "graphMarkerForPlayer" + i, null);
			text = new OffsetCoords(left, -20, -8);
			lang.newText(text, "P" + (i+1), "PlayerText" + 1, null, props);
			
			players[i].ratingVariance = players[i].player.getRatingDev();
			
			//Get Positions for the rectangles
			Node ratingLeft = new OffsetCoords(right, 20 + (int)(players[i].player.getRating() - players[i].ratingVariance - min)/5, -5);
			Node ratingRight = new OffsetCoords(ratingLeft, (int)(players[i].ratingVariance*2)/5, 10);
			
			
			rectProps = new RectProperties();
	    	rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
	    	rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, barColor);
	    	rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
			
			players[i].currRect = lang.newRect(ratingLeft, ratingRight, "", null, rectProps);
		}
	}
	
	public void updatePlayer(Glicko2Player p) {
		//Get Player index
		int i = -1;
		for (int j = 0; j < players.length; j++) {
			if (players[j].player == p) {
				i = j;
				break;
			}
		}
		//Hides old
		players[i].currRect.hide();
		
		//Compute positions for rectangle
		Node ratingLeft = new OffsetCoords(players[i].pos, 20 + (int)(173.7178 * p.rating_new + 1500 - players[i].ratingVariance - min)/5, -5);
		Node ratingRight = new OffsetCoords(ratingLeft, (int)(players[i].ratingVariance*2)/5, 10);
		
		//Save new rectangle
		players[i].currRect = lang.newRect(ratingLeft, ratingRight, "", null, rectProps);	
	}
	
	public void updatePlayerDev(Glicko2Player p) {
		//Get Player index
		int i = -1;
		for (int j = 0; j < players.length; j++) {
			if (players[j].player == p) {
				i = j;
				break;
			}
		}
		//Hides old
		players[i].currRect.hide();
		
		//Update 
		players[i].ratingVariance = 173.7178 * p.rating_deviation_new;
		
		//Compute positions for rectangle
		Node ratingLeft = new OffsetCoords(players[i].pos, 20 + (int)((173.7178 * p.rating_new + 1500 - players[i].ratingVariance - min))/5, -5);
		Node ratingRight = new OffsetCoords(ratingLeft, (int)(players[i].ratingVariance*2)/5, 10);
		
		//Save new rectangle
		players[i].currRect = lang.newRect(ratingLeft, ratingRight, "", null, rectProps);		
	}
	
	public Node getLowerLeft() {
		return lowerLeftCorner;
	}
}