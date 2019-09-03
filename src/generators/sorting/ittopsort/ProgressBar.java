package generators.sorting.ittopsort;
import java.awt.Color;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Point;
import algoanim.primitives.Rect;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PointProperties;
import algoanim.properties.RectProperties;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * modelizes a progressbar for AlgoAnim
 * consisting of two rectangles
 * @author Matthias
 *
 */
public class ProgressBar {

	private Language lang;
	
	// properties
	private int height;
	private int width;
	String name;
	
	// primitives
	private Point dummyPoint;
	private Rect primBorder;
	private Rect primFilling;
	private RectProperties propBorder = new RectProperties();
	private RectProperties propFilling = new RectProperties();
	
	// data
	private int counter = 0;
	//private int progress = 0;
	
	// final attributes
	private static final int borderSize = 3; //TODO really? or propertie
	
	public static final int MAX_PROGRESS = 1000;
	
	/**
	 * draws an empty progress bar into the language object
	 * @param l the language
	 * @param upperLeft the node marking the upper left corner
	 * @param width the width of the bar
	 * @param height the height of the bar
	 * @param name the name used in AnimalScript (sub objects have names name_subobject)
	 */
	public ProgressBar(Language l, Node upperLeft, int width, int height, String name) {
		this.lang = l;
		this.name = name;
		this.height = height;
		this.width = width;
		
		// dummy object which marks upperLeft of ProgressBar
		dummyPoint = lang.newPoint(upperLeft, name+"_dummy", null, new PointProperties());
		dummyPoint.hide();
		
		primBorder = lang.newRect(upperLeft, 
				new Offset(width, height, name+"_dummy", AnimalScript.DIRECTION_NW), 
				name+"_border", null, propBorder);
		primFilling = lang.newRect(new Offset(borderSize, borderSize, name+"_dummy", AnimalScript.DIRECTION_NW), 
				new Offset(borderSize, height-2*borderSize, name+"_dummy", AnimalScript.DIRECTION_NW), 
				name+"_filling", null, propFilling);
		
		//TODO move properties??
		propBorder.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		propBorder.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
		
		propFilling.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		propFilling.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
	}

	/**
	 * updates the progress bar
	 * @param progress the progress [0,MAX_PROGRESS]
	 */
	public void setProgress(int progress) {
		if(progress > MAX_PROGRESS || progress < 0) {
			throw new IllegalArgumentException("Progress must be between 0 and "+MAX_PROGRESS);
		}
		
		// hide old filling
		primFilling.hide();
		
		// create new filling
		int progWidth = borderSize + (progress*(width-2*borderSize))/MAX_PROGRESS;
		
		primFilling = lang.newRect(new Offset(borderSize, borderSize, name+"_dummy", AnimalScript.DIRECTION_NW), 
				new Offset(progWidth,
						height-borderSize, name+"_dummy", AnimalScript.DIRECTION_NW), 
				name+"_filling"+counter, null, propFilling);
		counter++;
	}
	
	/**
	 * hides the progress bar
	 */
	public void hide() {
		primBorder.hide();
		primFilling.hide();
	}
	
	/**
	 * shows the progress bar
	 */
	public void show() {
		primBorder.show();
		primFilling.show();
	}
}
