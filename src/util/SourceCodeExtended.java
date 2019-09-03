package util;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.SourceCodeGenerator;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Timing;


public class SourceCodeExtended extends SourceCode {

	public SourceCodeExtended(SourceCodeGenerator generator, Node upperLeft,
			String name, DisplayOptions display, SourceCodeProperties properties) {
		super(generator, upperLeft, name, display, properties);
	}
	/**
	 * Adds multiple lines of code and uses \t indentation to determine the indentation depth
	 * 
	 * @param code
	 * @param label
	 * @param delay
	 */
	public void addMultilineCode(String code, String label, Timing delay){		
		String[] lines = code.split("\n");
		for(String line : lines){
			int indentation=0;
			while(line.charAt(indentation)=='\t') 
				indentation++;
			line=line.replaceAll("\t+", "");
			this.addCodeLine(line, label, indentation, delay);
		}
	}
}