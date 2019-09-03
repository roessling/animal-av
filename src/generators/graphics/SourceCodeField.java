package generators.graphics;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Timing;


abstract public class SourceCodeField extends ScenarioObject{
	
    private Language language;
	
    private int srcCurrentRow = 0;
    private int srcMaxRows = 50;
    private int currentHighlightedScope = -1;
    
    private int lineHeight = 17;
    
    private String srcCurrentMethodname = "main";
    private String sourceCodeAsString;
    
    private Coordinates upperLeft;
    
    private ArrayList<Text> sourceCodeLines;
    private ArrayList<Text> oldSourceCodeLines;
    
    
    private Font font;
    private Color textColor;
    private Color highlightColor;

    
    public SourceCodeField(Language language, Coordinates upperLeft, String sourceCode, String methodName ,int initialRow, int maxRows, int highlightedScope, Timing defaultTiming)
    {
    	super();
    	this.highlightColor = Color.red;
    	this.textColor = Color.black;
    	this.font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    	
    	this.sourceCodeLines = new ArrayList<Text>();
    	this.oldSourceCodeLines = new ArrayList<Text>();
    	this.language = language;
    	this.upperLeft = upperLeft;

    	this.srcMaxRows = maxRows;
    	this.srcCurrentRow = initialRow;
    	this.sourceCodeAsString = sourceCode;
    	this.srcCurrentMethodname = methodName;
    	
		this.showSourceCode(methodName, initialRow, maxRows, highlightedScope, defaultTiming);
    }
    
	public void setFont(Font f)
	{
		if (f != null){
			this.font = f;
			TextProperties textProps = new TextProperties();
			textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, f);
			
			for (Text line : this.sourceCodeLines){
				line.setFont(f, null, null);
			}
		    
		}
	}
	public Font getFont(){
		return this.font;
	}
	public void setTextColor(Color color)
	{
		if (color != null){
			for (Text line : this.sourceCodeLines) {
				TextProperties p = line.getProperties();
				if (p != null){
					Color currentTextColor = (Color) p.get(AnimationPropertiesKeys.COLOR_PROPERTY);
					if (currentTextColor != null && currentTextColor.equals(this.textColor)){
						line.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, color, null, null);
					}
				}
			}
			this.textColor = color;
		}
	}
	public Color getTextColor(){
		return this.textColor;
	}
	
	public void setHighlightColor(Color color)
	{
		if (color != null){
			for (Text line : this.sourceCodeLines) {
				TextProperties p = line.getProperties();
				if (p != null){
					Color currentTextColor = (Color) p.get(AnimationPropertiesKeys.COLOR_PROPERTY);
					if (currentTextColor != null && currentTextColor.equals(this.highlightColor)){
						line.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, color, null, null);
					}
				}
			}
			this.highlightColor = color;
		}
	}
	public Color getHighlightColor()
	{
		return this.highlightColor;
	}
    
    public int getSrcCurrentRow()
    {
    	return this.srcCurrentRow;
    }
    public void setSrcCurrentRow(int s)
    {
    	this.srcCurrentRow = s;
    }
    public int getCurrentHighlightedScope()
    {
    	return this.currentHighlightedScope;
    }
    public void setCurrentHighlightedScope(int s)
    {
    	this.currentHighlightedScope = s;
    }
    public int getSrcMaxRows()
    {
    	return this.srcMaxRows;
    }
    public void setSrcMaxRows(int s)
    {
    	this.srcMaxRows = s;
    }
    
    public String getSrcCurrentMethodname()
    {
    	return this.srcCurrentMethodname;
    }
    public void setSrcCurrentMethodname(String s)
    {
    	this.srcCurrentMethodname = s;
    }
    public String getSourceCodeAsString()
    {
    	return this.sourceCodeAsString;
    }
    
    public Coordinates getUpperLeft()
    {
    	return this.upperLeft;
    }
    
    public Language getLanguage()
    {
    	return this.language;
    }
    
	/**
	 * Highlights the given line.
	 * 
	 * @param lineNumber - int, the line number
	 * @param t - animation timing
	 */
	public void highlight(int lineNumber, Timing t)
	{
		if (lineNumber < this.sourceCodeLines.size())
		{
			Text line = this.sourceCodeLines.get(lineNumber);
			line.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, this.highlightColor, t, null);
		}
	}
    
	/**
	 * Unhighlights the given line.
	 * 
	 * @param lineNumber - int, the line number
	 * @param t - animation timing
	 */
	public void unhighlight(int lineNumber, Timing t)
	{
		if (lineNumber < this.sourceCodeLines.size())
		{
			Text line = this.sourceCodeLines.get(lineNumber);
			line.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, this.textColor, t, null);
		}
	}
	
	
	

	
	/**
	 * Adds the given source code line.
	 * 
	 * Creates a new Text object.
	 * 
	 * @param line - string, the code line
	 * @param t - animation timing
	 */
	public void addSourceCodeLine(String line, Timing t)
	{
  		TextProperties textFieldsProps = new TextProperties();
        textFieldsProps.set(AnimationPropertiesKeys.FONT_PROPERTY, this.font);
        textFieldsProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, this.textColor);
		int lineHeight = Math.max(this.font.getSize() + 4, this.lineHeight);
		int yCoordinate = this.upperLeft.getY() + (this.sourceCodeLines.size() * lineHeight);
		Text codeLine = this.language.newText(new Coordinates(this.upperLeft.getX(), yCoordinate), line, "sourceCodeLine_"+this.sourceCodeLines.size(), t, textFieldsProps);
		this.sourceCodeLines.add(codeLine);
	}
    

    
    /**
     * 
     * Shows the source code for the given specifications
     * 
     * @param methodname - string, method name which should be shown
     * @param initialRow - int, index of first code line after the scope index
     * @param maxRows - int, max number of visible code lines
     * @param highlightedScope - int, the scope index
     * @param defaultTiming - animation timing
     */
	public void showSourceCode(String methodname ,int initialRow, int maxRows, int highlightedScope, Timing defaultTiming)
	{
		if (this.sourceCodeLines.size() != 0 && (this.getSrcCurrentRow() != initialRow || this.getSrcMaxRows() != maxRows || (methodname != null && !this.getSrcCurrentMethodname().equals(methodname)))){
    		this.hidePrimitives(null);
    		this.oldSourceCodeLines.addAll(this.sourceCodeLines);
    		this.sourceCodeLines.clear();
    	}
    	
		this.setSrcCurrentMethodname(methodname != null? methodname : this.getSrcCurrentMethodname());
		
    	this.srcMaxRows = maxRows;
    	this.srcCurrentRow = initialRow;
    	
    	if (this.sourceCodeLines.size() == 0)
    	{
    	    this.generateSourceCodeFromSourceString(this.getUpperLeft(), this.getSourceCodeAsString(), methodname, initialRow, maxRows, highlightedScope, defaultTiming);
            this.setIsVisible(true);
            this.setNeedUpdate(false);
    	    // show newly created lines
    	}
    	
        this.showSourceCode(highlightedScope, defaultTiming);

	}
	
    
    public void showSourceCode(int highlight, Timing t)
    {
    	if (this.getCurrentHighlightedScope() >= 0){
            this.unhighlightScope(this.getCurrentHighlightedScope());
		}
        if (highlight < 0){
            this.setCurrentHighlightedScope( -1 );
        } else {
            this.setCurrentHighlightedScope( highlight );
            this.highlightScope(this.getCurrentHighlightedScope());
        }
    }
    
    public void showPrimitives(Timing t)
    {
    	for (Text line : this.sourceCodeLines){
    		line.show(t);
    	}
    	this.showSourceCode(this.getCurrentHighlightedScope(), t);
    }
    
    public void hidePrimitives(Timing t)
    {
    	for (Text line : this.sourceCodeLines){
    		line.hide();
    	}
    }
    
	public void didRefresh(){
		this.oldSourceCodeLines.clear();
	}
    
	/**
     * Create a source code object from a source string. The source code object will contain just lines from the given method.
     * The initialRow parameter is the first row in the method's code, beginning by the row with containing the method's name.
     *  
     * @param upperLeft - upperLeft Coordinate
     * @param sourceString - the complete source code as string
     * @param methodname - the name of the chosen method
     * @param initialRow - first line of the method
     * @param maxRows - max number of rows
     * @param highlightedScope - scope which should be highlighted
     * @return (SourceCode) source code object
     */
    abstract public void generateSourceCodeFromSourceString(Coordinates upperLeft, String sourceString, String methodname ,int initialRow, int maxRows, int highlightedScope, Timing timing);
   
    abstract public void highlightScope(int level);
    abstract public void unhighlightScope(int level);
}