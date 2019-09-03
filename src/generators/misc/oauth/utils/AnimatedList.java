package generators.misc.oauth.utils;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.LinkedList;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;

 /**
     * Represents a List of Rectangles with content and fancy methods to slide them in.
     * @author Vincent
     *
     */
    public class AnimatedList{

    	private HashMap<Integer, AnimatedListItem> list;
    	private Coordinates upperLeft, lowestLeft;
    	private int itemWidth, itemHeight;
    	private Language lang;

    	public AnimatedList(Language lang, Coordinates upperLeft, int itemWidth, int itemHeight){
    		
    		this.upperLeft = upperLeft;
    		this.lowestLeft = new Coordinates(upperLeft.getX(), upperLeft.getY());
    		this.itemHeight = itemHeight;
    		this.itemWidth = itemWidth;
    		this.lang = lang;
    		list = new HashMap<Integer, AnimatedListItem>();
    		

    	}	
    	

		public void addContent(int id, String input){
		 	int x,y;
			x = upperLeft.getX();
			y = upperLeft.getY() + (id * itemHeight);
			list.put(id, new AnimatedListItem(lang, x,y, itemWidth, itemHeight, input));
			lowestLeft = new Coordinates(x,y+itemHeight);

		}
    	
    	public void clearContent(){
    		/*
			for(AnimatedListItem i : list)
    			i.hide();
    		lowestLeft = upperLeft;
    		list.clear();
    		*/
    	}

    	
    	/**
    	 * The item from the linked list with the given index will slide in  
    	 * @param
    	 */
    	public void slideInItem(int itemIndex,int stepSlideInTimeInMs){
    		list.get(itemIndex).slideIn(stepSlideInTimeInMs);
    	}
    	/**
    	 * The selected Item will be highlighted 
    	 * @param itemIndex
    	 */
    	public void highLight(int itemIndex){
    		list.get(itemIndex).highLight();
    		
    	}
    	
    	/**
    	 * The selected Item will be unhighlighted
    	 * @param itemIndex
    	 */
    	public void unHighLight(int itemIndex){
    		list.get(itemIndex).unHighLight();
    	}
    	
    	
    	

    }