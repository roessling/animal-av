package util;

import util.SourceCodeExtended;
import generators.backtracking.helpers.AnimalObjectStyle;
import generators.backtracking.helpers.AnimalStyling;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalSourceCodeGenerator;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.MatrixPrimitive;
import algoanim.primitives.generators.SourceCodeGenerator;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;

public class AnimalScriptExtension extends AnimalScript{	  
	 private SourceCodeGenerator sourceGen = null;
	private AnimalStyling styles;
	
	
	public AnimalScriptExtension(String title, String author, int x, int y, AnimalStyling animalStyling) {
		super(title, author, x, y);
		this.styles=animalStyling;
		this.sourceGen = new AnimalSourceCodeGenerator(this);
	}
	public AnimalScriptExtension(String title, String author, int x, int y){
		super(title, author, x, y);
		this.sourceGen = new AnimalSourceCodeGenerator(this);
	}
	
	public void addMultiLineText(String text, String groupname){
		String theText = escape(text);
		int i =1;
		String[] lines = theText.split("\n");
		for (String line : lines){
			if (groupname != null)
				this.newText(line, groupname+(i++));
		}
	}
	
	@Override
	public SourceCodeExtended newSourceCode(Node upperLeft,	String name, DisplayOptions display, SourceCodeProperties properties) {
		return new SourceCodeExtended(this.sourceGen, upperLeft, name, display, properties);
		
	}
	
	private String escape(String string){
		return string.replaceAll("\r\n", "\n")
		.replaceAll("\r", "\n")
		.replaceAll("\025", "\n");
	}

	public Text newText(String text, String name) {
		AnimalObjectStyle style = mergeStyle(this.styles.get(name),this.styles.getDefault(Text.class));
		return super.newText(style.position,
				text, 
				name, 
				style.display, 
				(TextProperties) style.element);
		
		
	}
	public Rect newRect(String name) {
		AnimalObjectStyle style = this.styles.get(name);
		if(style == null)
			style = this.styles.getDefault(Rect.class);
		return super.newRect(style.position, style.position2, name, style.display, (RectProperties) style.element);
		
	}
	/**
	 * Hide all given primitives.
	 * @param primitives List of primitives to hide
	 */
	public void hide(String... primitives){
		for(String element : primitives){
			this.addLine(new StringBuilder("hide \"").append(element).append("\""));
		}
	}
	public IntMatrix newIntMatrix(int[][] dataArray, String name) {
		AnimalObjectStyle style = mergeStyle(this.styles.get(name),this.styles.getDefault(MatrixPrimitive.class));		
		return super.newIntMatrix(style.position, dataArray, name, style.display, (MatrixProperties) style.element);
		
	}

	private AnimalObjectStyle mergeStyle(AnimalObjectStyle style, AnimalObjectStyle defaultStyle){
		
		if(style == null)
			return defaultStyle;
		
		AnimalObjectStyle mergedStyle = new AnimalObjectStyle();
		mergedStyle.position = (style.position == null)? defaultStyle.position : style.position;
		mergedStyle.element = (style.element == null)? defaultStyle.element : style.element;
		mergedStyle.display = (style.display == null)? defaultStyle.display : style.display;
		mergedStyle.position2 = (style.position2 == null)? defaultStyle.position2 : style.position2;
		return mergedStyle;
	}
}
