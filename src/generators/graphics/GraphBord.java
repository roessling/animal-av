package generators.graphics;


import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import algoanim.primitives.Polyline;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Timing;


public  class GraphBord extends ScenarioObject {
	
	private boolean primitivesAreVisible = false;
	
	private int lineLabelOffset = 14;
	
	private ArrayList<Polyline> lines;
	
	private Polyline xAxis;
	private Text[] xAxisLabels;
	private Polyline yAxis;
	private Text[] yAxisLabels;

	private Size size;
	private Coordinates upperLeft;
	
	private Text xAxisDescField;
	private Text yAxisDescField;
	private ArrayList<Text> lineDescLabels;
	
	private Timing defaultTiming;
	
	private Language language;

	public Size getSize()
	{
		return size != null? size : new Size(0,0);
	}
	
	public Coordinates getUpperLeft()
	{
		return upperLeft;
	}
	
	public void didRefresh(){
		
	}
	
	public void hidePrimitives(Timing t)
	{
		if (primitivesAreVisible){

    		if (xAxis != null)
    		{
    			xAxis.hide();
    		}
    		if (yAxis != null)
    		{
    			yAxis.hide();
    		}
    		
    		if (lines != null)
    		{
    			for (Polyline line : lines) line.hide();
    		}
    		if (xAxisLabels != null)
    		{
        		for (Text label : xAxisLabels) label.hide();
    		}
    		if (yAxisLabels != null)
    		{
        		for (Text label : yAxisLabels) label.hide();	
    		}
    		if (lineDescLabels != null)
    		{
        		for (Text label : lineDescLabels) label.hide();	
    		}
    		
    		
    		if (this.xAxisDescField != null)
    		{
    			this.xAxisDescField.hide();
    		}
    		if (this.yAxisDescField != null)
    		{
    			this.yAxisDescField.hide();
    		}
		}
		primitivesAreVisible = false;
	}
	
	@Override
	public void hide(){
		super.hide();
		if (primitivesAreVisible){

    		if (xAxis != null)
    		{
    			xAxis.hide();
    		}
    		if (yAxis != null)
    		{
    			yAxis.hide();
    		}
    		
    		if (lines != null)
    		{
    			for (Polyline line : lines) line.hide();
    		}
    		if (xAxisLabels != null)
    		{
        		for (Text label : xAxisLabels) label.hide();
    		}
    		if (yAxisLabels != null)
    		{
        		for (Text label : yAxisLabels) label.hide();	
    		}
    		if (lineDescLabels != null)
    		{
        		for (Text label : lineDescLabels) label.hide();	
    		}
    		
    		
    		if (this.xAxisDescField != null)
    		{
    			this.xAxisDescField.hide();
    		}
    		if (this.yAxisDescField != null)
    		{
    			this.yAxisDescField.hide();
    		}
		}
		primitivesAreVisible = false;
	}
	
	public void showPrimitives(Timing t)
	{
		if (!primitivesAreVisible){
			if (xAxis != null)
    		{
    			xAxis.show(t);
    		}
    		if (yAxis != null)
    		{
    			yAxis.show(t);
    		}
    		
    		if (lines != null)
    		{
    			for (Polyline line : lines) line.show(t);
    		}
    		if (xAxisLabels != null)
    		{
        		for (Text label : xAxisLabels) label.show(t);
    		}
    		if (yAxisLabels != null)
    		{
        		for (Text label : yAxisLabels) label.show(t);	
    		}
    		if (lineDescLabels != null)
    		{
        		for (Text label : lineDescLabels) label.show(t);	
    		}
    		
    		if (this.xAxisDescField != null)
    		{
    			this.xAxisDescField.show(t);
    		}
    		
    		if (this.yAxisDescField != null)
    		{
    			this.yAxisDescField.show(t);
    		}
    		
		}
		primitivesAreVisible = true;
	}
	
	public GraphBord(Language language, Coordinates upperLeft, Size size, String[] xAxisTitles, String xAxisDescription, String[] yAxisTitles, String yAxisDescription, List<Polyline> lines, String[] lineDescriptions, Timing timing)
	{
		super();
		this.lines = new ArrayList<Polyline>();
		this.lineDescLabels = new ArrayList<Text>();

		this.language = language;
		this.upperLeft = upperLeft;
		this.lines.addAll(lines);
		this.size = size;
		
		this.defaultTiming = timing;
		
		this.setupAxisDescription(language, xAxisDescription, yAxisDescription, timing);
		
		this.setupAxis(language, xAxisTitles, yAxisTitles, timing);
		
		this.setupLineDescription(language, lineDescriptions, timing);
		
		primitivesAreVisible = true;
	}
	
	public void setupAxisDescription(Language language, String xAxisDesc, String yAxisDesc, Timing timing)
	{
		TextProperties textFieldsProps = new TextProperties();
        textFieldsProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 14));
        
        int descOffset = 15;
        
        this.xAxisDescField = language.newText(new Coordinates(upperLeft.getX() + size.getWidth() + descOffset, upperLeft.getY() + size.getHeight() + descOffset), xAxisDesc , ("xDescLabel"), timing, textFieldsProps);
        this.yAxisDescField = language.newText(new Coordinates(upperLeft.getX() - descOffset, upperLeft.getY() - descOffset), yAxisDesc , ("yDescLabel"), timing, textFieldsProps);
	}
	
	/**
	 *  Setup the description text objects for the given lines.
	 * 
	 * @param language - the language factory object
	 * @param linesDesc - string array
	 * @param timing - animation timing
	 */
	public void setupLineDescription(Language language, String[] linesDesc, Timing timing)
	{    	
		if (linesDesc == null)
		{
			String[] empty = {};
			linesDesc = empty;
		}
        
        ArrayList<Text> lineDescFields = new ArrayList<Text>();
        for(int i = 0; i < this.lines.size(); i++)
        {
        	String desc = i < linesDesc.length? linesDesc[i] : null;
        	Text text = this.setupLineDescription(this.lines.get(i), desc, lineLabelOffset, timing);
        	
        	if (text != null)
        	{
        		lineDescFields.add(text);
        	}
        }
        
        this.lineDescLabels = lineDescFields;
	}
	
	/**
	 * Setup the description for the given line.
	 * 
	 * @param line - Polyline, the line
	 * @param desc - string, description
	 * @param lineLabelOffset - offset from the line
	 * @param t - animation timing
	 * @return
	 */
	public Text setupLineDescription(Polyline line, String desc, int lineLabelOffset, Timing t)
	{
		// AXIS Labeling
        TextProperties textFieldsProps = new TextProperties();
        textFieldsProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 12));
        
    	String lineName = line.getName();
    	
    	if (desc != null)
    	{
    		lineName = desc;
    	}
    	if (line.getNodes().length > 0)
    	{
        	Coordinates firstNode = (Coordinates)line.getNodes()[0];
        	Text text = language.newText(new Coordinates(firstNode.getX(), firstNode.getY() - lineLabelOffset), lineName, ("lineDesc_"+lineName), null, textFieldsProps);
        	if (!this.isVisible() && !this.needUpdate()){
        		text.hide();
        	}
        	return text;
    	}
    	return null;
	}
	
	/**
	 * Setup the graph axis.
	 * 
	 * @param language - language factory object
	 * @param xAxisTitles - x axis titles
	 * @param yAxisTitles - y axis titles
	 * @param timing - animation timing
	 */
	public void setupAxis(Language language, String[] xAxisTitles, String[] yAxisTitles, Timing timing)
	{
		int xAxisSteps = size.getWidth() / Math.max(1, xAxisTitles.length -1); 
		int yAxisSteps = size.getHeight() / Math.max(1, yAxisTitles.length -1); 

		PolylineProperties gp = new PolylineProperties("LineProps");
    	gp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
		
		// AXIS Labeling
        TextProperties textFieldsProps = new TextProperties();
        textFieldsProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 12));
        
        // X AXIS
        Polyline xAxisLine = null;
        Node[] xAxisLineNodes = new Node[xAxisTitles.length*3];
        for (int i = 0; i < xAxisLineNodes.length; i+=3) 
        {
        	xAxisLineNodes[i] = new Coordinates(upperLeft.getX() + ((i/3)*xAxisSteps), upperLeft.getY() + size.getHeight());
        	xAxisLineNodes[i+1] = new Coordinates(upperLeft.getX() + ((i/3)*xAxisSteps), upperLeft.getY() + size.getHeight()-4);
        	xAxisLineNodes[i+2] = new Coordinates(upperLeft.getX() + ((i/3)*xAxisSteps), upperLeft.getY() + size.getHeight());
        }
		
        xAxisLine = language.newPolyline( xAxisLineNodes, "X_AXIS", timing, gp);
        
        this.xAxis = xAxisLine;

		// X AXIS LABELING
        Text[] xTextFields = new Text[xAxisTitles.length];
        for(int i = 0; i < Math.min(xTextFields.length, xAxisTitles.length); i++) {
        	Text text = language.newText(new Coordinates(upperLeft.getX() + (i*xAxisSteps), upperLeft.getY() + size.getHeight()), xAxisTitles[i], ("xlabel"+i), timing, textFieldsProps);
        	xTextFields[i] =  text;
        }
        
        this.xAxisLabels = xTextFields;
        
        // Y AXIS
        Polyline yAxisLine = null;
     	Node[] yAxisLineNodes = new Node[yAxisTitles.length];
     	int yLabelOffset = 10;
        for (int i = 0; i < yAxisLineNodes.length; i++) 
        	yAxisLineNodes[i] = new Coordinates(upperLeft.getX(), upperLeft.getY() + size.getHeight() - (i*yAxisSteps));
        
     	yAxisLine = language.newPolyline( yAxisLineNodes, "Y_AXIS", timing, gp);
        this.yAxis = yAxisLine;

        
     	// Y AXIS LABELING
     	Text[] yTextFields = new Text[yAxisTitles.length];
        for(int i = 0; i < Math.min(yTextFields.length, yAxisTitles.length); i++) {
        	Text text = language.newText(new Coordinates(upperLeft.getX() - yLabelOffset, upperLeft.getY() + size.getHeight() - (i*yAxisSteps)), yAxisTitles[i], ("ylabel"+i), timing, textFieldsProps);
        	yTextFields[i] = text;
        }
        
        this.yAxisLabels = yTextFields;
	}
	
	/**
	 * 
	 * add a given polyline with description.
	 * 
	 * @param p - polyline
	 * @param lineDesc - string, the line description
	 * @param t - animation timing
	 * @return
	 */
	public Text addPolyline(Polyline p, String lineDesc, Timing t)
	{
		this.lines.add(p);
		Text text = this.setupLineDescription(p, lineDesc, lineLabelOffset, t);
		if (text != null)
		{
			this.lineDescLabels.add(text);
		}
		if (!this.isVisible() && !this.needUpdate()){
			p.hide();
		}
		return text;
	}
	
}