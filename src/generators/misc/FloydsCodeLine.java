package generators.misc;

public class FloydsCodeLine {
	private String Code;
	private int Indentation;
	
	public FloydsCodeLine (String c, int i) {
		Code = c;
		Indentation = i;
	}
	
	public String getCode() {
		return Code;
	}
	
	public int getIndentation() {
		return Indentation;
	}
}
