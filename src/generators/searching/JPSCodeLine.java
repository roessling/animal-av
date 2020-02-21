package generators.searching;

public class JPSCodeLine {
	private String Code;
	private int Indentation;
	
	public JPSCodeLine (String c, int i) {
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
