package util.text;

import java.awt.Color;

import algoanim.properties.TextProperties;

public class EnumFormat {

	protected String form;
	protected int indent, preIndent;
	protected double innerDistFactor, sizeFactor;
	protected TextProperties props;
	protected Color color;
	protected int index;
	
	public EnumFormat(String form, int indent, double sizeFactor, TextProperties props, Color color, double innerDistFactor, int preIndent) {
		this.form = form;
		this.indent = indent;
		this.sizeFactor = sizeFactor;
		this.color = color;
		this.innerDistFactor = innerDistFactor;
		this.preIndent = preIndent;
		this.props = props;
		index = 0;
	}

}
