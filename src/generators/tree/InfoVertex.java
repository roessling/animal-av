package generators.tree;

import algoanim.primitives.Circle;
import algoanim.primitives.Text;
import algoanim.properties.CircleProperties;
import algoanim.properties.TextProperties;

public class InfoVertex {

	private String infoString;
	private Circle infoNodeCircle;
	private Text infoText;
	private int x;
	private int y;
	private TextProperties textProperties;
	
	public String getInfoString() {
		return infoString;
	}
	public void setInfoString(String infoString) {
		this.infoString = infoString;
	}
	public Circle getInfoNodeCircle() {
		return infoNodeCircle;
	}
	public void setInfoNodeCircle(Circle infoNodeCircle) {
		this.infoNodeCircle = infoNodeCircle;
	}
	public Text getInfoText() {
		return infoText;
	}
	public void setInfoText(Text infoText) {
		this.infoText = infoText;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public TextProperties getTextProperties() {
		return textProperties;
	}
	public void setTextProperties(TextProperties textProperties) {
		this.textProperties = textProperties;
	}
	
	
	
	
}
