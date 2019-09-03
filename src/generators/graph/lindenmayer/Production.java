package generators.graph.lindenmayer;

public class Production {
	private String leftSide;
	private String rightSide;
	
	public Production(String nonTerminal, String replacedBy){
		this.leftSide = nonTerminal;
		this.rightSide = replacedBy;
	}
	
	public String getLeftSide() {
		return leftSide;
	}

	public void setLeftSide(String value) {
		this.leftSide = value;
	}

	public String getRightSide() {
		return rightSide;
	}

	public void setRightSide(String value) {
		this.rightSide = value;
	}
}
