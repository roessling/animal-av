package generators.framework.properties;

import gfgaa.gui.GraphScriptPanel;

import java.awt.Color;
import java.awt.Font;
import java.util.Iterator;
import java.util.Vector;

import algoanim.properties.AnimationProperties;
import algoanim.properties.Visitor;
import algoanim.properties.items.AnimationPropertyItem;
import algoanim.properties.items.BooleanPropertyItem;
import algoanim.properties.items.ColorPropertyItem;
import algoanim.properties.items.DoublePropertyItem;
import algoanim.properties.items.EnumerationPropertyItem;
import algoanim.properties.items.FontPropertyItem;
import algoanim.properties.items.IntegerPropertyItem;
import algoanim.properties.items.StringPropertyItem;


/**
 * This class is used to return the AnimationPropertyItem as an XML-String,
 * that can be read by LoadFromXML.
 * 
 * @author T. Ackermann
 */
public class GetAsXMLVisitor implements Visitor {
	
	/** buffer is used to efficiently store the XML representation of an Item */
	private StringBuilder buffer;
	
	
	/**
	 * getPropertiesAsXML
	 * returns the given AnimationProperties Object serialized as an XML. This
	 * XML is used to store an AnimationPropertiesContainer into an *.apc file.
	 * @param ap The AnimationProperties Object.
	 * @param l The current level of indentation.
	 * @return The XML String of the serialized given AnimationProperties
	 * Object.
	 */
	public String getPropertiesAsXML(AnimationProperties ap, int l) {
		StringBuilder bigBuffer = new StringBuilder(1024);
		for(int i=0;i<l;i++) bigBuffer.append("\t");
		bigBuffer.append("<AnimationProperties type=\"");
		bigBuffer.append(replaceEntitiesForXML(
				getSimpleName(ap.getClass().getName())));	
		bigBuffer.append("\">\n");
		
		// for every AnimationPropertyItem...
		Vector<String> items = ap.getAllPropertyNamesVector();
		Iterator<String> it = items.iterator();
		String itemName;
		AnimationPropertyItem item;
		while (it.hasNext()) {
			itemName = it.next();
			item = ap.getItem(itemName);
			
			// append attribute (type)
			for(int i=0;i<l;i++) bigBuffer.append("\t");
			bigBuffer.append("\t<AnimationPropertyItem type=\"");
			bigBuffer.append(replaceEntitiesForXML(
				getSimpleName(item.getClass().getName())));
			bigBuffer.append("\">\n");
			
			// append name
			for(int i=0;i<l;i++) bigBuffer.append("\t");
			bigBuffer.append("\t\t<name>");
			bigBuffer.append(replaceEntitiesForXML(itemName));
			bigBuffer.append("</name>\n");
					
			
			
			// append isEditable
			for(int i=0;i<l;i++) bigBuffer.append("\t");
			bigBuffer.append("\t\t<isEditable value=");
			if (ap.getIsEditable(itemName))
				bigBuffer.append("\"true\" />\n");
			else
				bigBuffer.append("\"false\" />\n");
			
			
			// append label
			for(int i=0;i<l;i++) bigBuffer.append("\t");
			bigBuffer.append("\t\t<label>");
			bigBuffer.append(replaceEntitiesForXML(
					ap.getLabel(itemName)));
			bigBuffer.append("</label>\n");
			
			// now append the value
			for(int i=0;i<l;i++) bigBuffer.append("\t");
			bigBuffer.append("\t\t<value>");
			item.accept(this);
			bigBuffer.append(this.buffer.toString());
			bigBuffer.append("</value>\n");
			
			for(int i=0;i<l;i++) bigBuffer.append("\t");
			bigBuffer.append("\t</AnimationPropertyItem>\n");
		}
		
		for(int i=0;i<l;i++) bigBuffer.append("\t");
		bigBuffer.append("</AnimationProperties>\n");
		return bigBuffer.toString();
	}
	
	
	/**
	 * getPrimitiveAsXML
	 * returns the given Primitive as XML String.
	 * @param name The Name of the Primitive.
	 * @param value The Primitive Value.
	 * @param l The current level of indentation.
	 * @return The given Primitive as XML String.
	 */
	public String getPrimitiveAsXML(String name, Object value, int l) {
		StringBuilder buf = new StringBuilder(256);
		for(int i=0;i<l;i++) buf.append("\t");
		buf.append("<Primitive type=\"");
		buf.append(getPrimitiveTypeAsString(value));	
		buf.append("\">\n");
		
		for(int i=0;i<l;i++) buf.append("\t");
		buf.append("\t<name>");
		buf.append(replaceEntitiesForXML(name));
		buf.append("</name>\n");
		
		for(int i=0;i<l;i++) buf.append("\t");
		buf.append("\t<value>");
		buf.append(getPrimitiveValueAsString(value));
		buf.append("</value>\n");
		
		for(int i=0;i<l;i++) buf.append("\t");
		buf.append("</Primitive>\n");
		return buf.toString();
	}
	public String getPrimitiveAsXML(String name, Object value, String description, int l) {
		StringBuilder buf = new StringBuilder(256);
		for(int i=0;i<l;i++) buf.append("\t");
		buf.append("<Primitive type=\"");
		buf.append(getPrimitiveTypeAsString(value));	
		buf.append("\">\n");
		
		for(int i=0;i<l;i++) buf.append("\t");
		buf.append("\t<name>");
		buf.append(replaceEntitiesForXML(name));
		buf.append("</name>\n");

		for(int i=0;i<l;i++) buf.append("\t");
		buf.append("\t<value>");
		buf.append(getPrimitiveValueAsString(value));
		buf.append("</value>\n");

		for(int i=0;i<l;i++) buf.append("\t");
		buf.append("\t<description>");
		buf.append(replaceEntitiesForXML(description));
		buf.append("</description>\n");
		
		for(int i=0;i<l;i++) buf.append("\t");
		buf.append("</Primitive>\n");
		return buf.toString();
	}
	
	
	/**
	 * getPrimitiveTypeAsString
	 * returns a String representing the Type of the given Primitive value.
	 * @param value The Primitive value.
	 * @return A String representing the Type of the given Primitive value.
	 */
	private static String getPrimitiveTypeAsString(Object value) {
		if (value == null) throw new IllegalArgumentException("null cannot be a Primitive!");
		if (value instanceof String) return "String";
		if (value instanceof Integer) return "int";
		if (value instanceof Boolean) return "boolean";
		if (value instanceof Double) return "double";
		if (value instanceof int[]) return "intArray";
		if (value instanceof double[]) return "doubleArray";
		if (value instanceof String[]) return "StringArray";
		if (value instanceof Color) return "Color";
		if (value instanceof Font) return "Font";
		if (value instanceof int[][]) return "intMatrix";
		if (value instanceof double[][]) return "doubleMatrix";
		if (value instanceof String[][]) return "StringMatrix";
		if(value instanceof GraphScriptPanel) return "Graph";
		throw new IllegalArgumentException("Unknown Primitive Type!");
	}
	
	
	/**
	 * getPrimitiveValueAsString
	 * returns the given Primitive value as an XML String.
	 * @param value The Primitive value.
	 * @return The given Primitive value as an XML String.
	 */
	private static String getPrimitiveValueAsString(Object value) {
		if (value == null) throw new IllegalArgumentException("null cannot be a Primitive!");
		if (value instanceof String) return getStringAsXML((String)value);
		if (value instanceof Integer) return getIntegerAsXML((Integer)value);
		if (value instanceof Boolean) return getBooleanAsXML((Boolean)value);
		if (value instanceof Double) return getDoubleAsXML((Double)value);
    if (value instanceof double[]) return getDoubleArrayAsXML((double[])value);
		if (value instanceof int[]) return getIntArrayAsXML((int[])value);
		if (value instanceof String[]) return getStringArrayAsXML((String[])value);
		if (value instanceof Color) return getColorAsXML((Color)value);
		if (value instanceof Font) return getFontAsXML((Font)value);
    if (value instanceof double[][]) return getDoubleMatrixAsXML((double[][])value);
		if (value instanceof int[][]) return getIntMatrixAsXML((int[][])value);
    if (value instanceof String[][]) return getStringMatrixAsXML((String[][])value);
		if(value instanceof GraphScriptPanel) return getGraphAsXML((GraphScriptPanel) value);
		throw new IllegalArgumentException("Unknown Primitive Type!");
	}
	
	
	/** (non-Javadoc)
	 * @see algoanim.properties.Visitor#visit(algoanim.properties.items.StringPropertyItem)
	 */
	public void visit(StringPropertyItem spi) {
		this.buffer = new StringBuilder(getStringAsXML((String)spi.get()));
	}
	
    /** (non-Javadoc)
     * @see algoanim.properties.Visitor#visit(algoanim.properties.items.EnumerationPropertyItem)
     */
	public void visit(EnumerationPropertyItem epi) {
	  buffer = new StringBuilder(getVectorAsXML(epi.get()));
	}
    
	/** (non-Javadoc)
	 * @see algoanim.properties.Visitor#visit(algoanim.properties.items.IntegerPropertyItem)
	 */
	public void visit(IntegerPropertyItem ipi) {
		this.buffer = new StringBuilder(getIntegerAsXML((Integer)ipi.get()));
	}
	
	
	/** (non-Javadoc)
	 * @see algoanim.properties.Visitor#visit(algoanim.properties.items.BooleanPropertyItem)
	 */
	public void visit(BooleanPropertyItem bpi) {
		this.buffer = new StringBuilder(getBooleanAsXML((Boolean)bpi.get()));	
	}

	
	/** (non-Javadoc)
	 * @see algoanim.properties.Visitor#visit(algoanim.properties.items.DoublePropertyItem)
	 */
	public void visit(DoublePropertyItem dpi) {
		this.buffer = new StringBuilder(getDoubleAsXML((Double)dpi.get()));
	}
	
	
	/** (non-Javadoc)
	 * @see algoanim.properties.Visitor#visit(algoanim.properties.items.ColorPropertyItem)
	 */
	public void visit(ColorPropertyItem cpi) {
		this.buffer = new StringBuilder(getColorAsXML((Color)cpi.get()));
	}

	
	/** (non-Javadoc)
	 * @see algoanim.properties.Visitor#visit(algoanim.properties.items.FontPropertyItem)
	 */
	public void visit(FontPropertyItem fpi) {
		this.buffer =  new StringBuilder(getFontAsXML((Font)fpi.get()));
	}
	
	
	/**
	 * getStringAsXML
	 * returns the given String as XML.
	 * @param str The String.
	 * @return The given String as XML.
	 */
	private static String getStringAsXML(String str) {
		StringBuilder buf = new StringBuilder(23);
		buf.append("<String>");
    str = str.replace(System.getProperty("line.separator"), "\\n");
    str = str.replace("\n", "\\n");
		buf.append(replaceEntitiesForXML(str));
		buf.append("</String>");
		return buf.toString();
	}
	
	
	/**
	 * getIntegerAsXML
	 * returns the given Integer as XML.
	 * @param i The Integer.
	 * @return The given Integer as XML.
	 */
	private static String getIntegerAsXML(Integer i) {
		StringBuilder buf = new StringBuilder(23);
		buf.append("<int>");
		buf.append(i.toString());
		buf.append("</int>");
		return buf.toString();
	}
	
	
	/**
	 * getBooleanAsXML
	 * returns the given Boolean as XML.
	 * @param b The Boolean.
	 * @return The given Boolean as XML.
	 */
	private static String getBooleanAsXML(Boolean b) {
		StringBuilder buf = new StringBuilder(23);
		buf.append("<boolean value=\"");
		buf.append(b.toString());
		buf.append("\" />");
		return buf.toString();
	}
	
	
	/**
	 * getDoubleAsXML
	 * returns the given Double as XML.
	 * @param d The Double.
	 * @return The given Double as XML.
	 */
	private static String getDoubleAsXML(Double d) {
		StringBuilder buf = new StringBuilder(23);
		buf.append("<double>");
		buf.append(d.toString());
		buf.append("</double>");
		return buf.toString();
	}
	
	 // Madieha
  private static String getGraphAsXML(GraphScriptPanel panel) {
    if (panel == null)
      throw new IllegalArgumentException("Graph cannot be empty!");
    StringBuilder buf = new StringBuilder(23);
    buf.append("<Graph>");
      buf.append(panel.input.getText());
    buf.append("</Graph>");
    return buf.toString();
  }
  
	/**
	 * getIntArrayAsXML
	 * returns the given int[] as XML.
	 * @param intArray The int[].
	 * @return The given int[] as XML.
	 */
	private static String getIntArrayAsXML(int[] intArray) {
		if (intArray == null || intArray.length < 1)
			throw new IllegalArgumentException("intArrays cannot be empty!");
		StringBuilder buf = new StringBuilder(23);
		buf.append("<intArray>");
		for (int i=0; i<intArray.length-1; i++) {
			buf.append(intArray[i]);
			buf.append(", ");
		}
			
		buf.append(intArray[intArray.length-1]);
		buf.append("</intArray>");
		return buf.toString();
	}
	/**
	 * getIntMatrixAsXML
	 * returns the given int[][] as XML.
	 * @param intMatrix The int[][].
	 * @return The given int[][] as XML.
	 */
	private static String getIntMatrixAsXML(int[][] intMatrix) {
		if (intMatrix == null || intMatrix.length < 1)
			throw new IllegalArgumentException("intMatrix cannot be empty!");
		StringBuilder buf = new StringBuilder(256);
		buf.append("<intMatrix>");
		int nrRows = intMatrix.length, nrCols = 0;
		for (int i=0; i<nrRows; i++) {
//			buf.append('[');
			if (intMatrix[i] == null)
				nrCols = 0;
			else { 
				nrCols = intMatrix[i].length;
				for (int j = 0; j < nrCols - 1; j++) {
					buf.append(intMatrix[i][j]).append(", ");
				}
				buf.append(intMatrix[i][nrCols - 1]);
			}
//			buf.append(']');
			if (i < nrRows - 1)
				buf.append("; ");
		}
		buf.append("</intMatrix>");
		return buf.toString();
	}
	
	 /**
   * getDoubleArrayAsXML
   * returns the given double[] as XML.
   * @param doubleArray The double[].
   * @return The given double[] as XML.
   */
  private static String getDoubleArrayAsXML(double[] doubleArray) {
    if (doubleArray == null || doubleArray.length < 1)
      throw new IllegalArgumentException("intArrays cannot be empty!");
    StringBuilder buf = new StringBuilder(23);
    buf.append("<doubleArray>");
    for (int i=0; i<doubleArray.length-1; i++) {
      buf.append(doubleArray[i]);
      buf.append(", ");
    }
      
    buf.append(doubleArray[doubleArray.length-1]);
    buf.append("</doubleArray>");
    return buf.toString();
  }
  
  /**
   * getDoubleMatrixAsXML
   * returns the given double[][] as XML.
   * @param doubleMatrix The double[][].
   * @return The given double[][] as XML.
   */
  private static String getDoubleMatrixAsXML(double[][] doubleMatrix) {
    if (doubleMatrix == null || doubleMatrix.length < 1)
      throw new IllegalArgumentException("intMatrix cannot be empty!");
    StringBuilder buf = new StringBuilder(256);
    buf.append("<doubleMatrix>");
    int nrRows = doubleMatrix.length, nrCols = 0;
    for (int i=0; i<nrRows; i++) {
//      buf.append('[');
      if (doubleMatrix[i] == null)
        nrCols = 0;
      else { 
        nrCols = doubleMatrix[i].length;
        for (int j = 0; j < nrCols - 1; j++) {
          buf.append(doubleMatrix[i][j]).append(", ");
        }
        buf.append(doubleMatrix[i][nrCols - 1]);
      }
//      buf.append(']');
      if (i < nrRows - 1)
        buf.append("; ");
    }
    buf.append("</doubleMatrix>");
    return buf.toString();
  }
	
	/**
	 * getStringMatrixAsXML
	 * returns the given String[][] as XML.
	 * @param intMatrix The String[][].
	 * @return The given String[][] as XML.
	 */
	private static String getStringMatrixAsXML(String[][] theMatrix) {
		if (theMatrix == null || theMatrix.length < 1)
			throw new IllegalArgumentException("StringMatrix cannot be empty!");
		StringBuilder buf = new StringBuilder(256);
		buf.append("<StringMatrix>");
		int nrRows = theMatrix.length, nrCols = 0;
		for (int i=0; i<nrRows; i++) {
//			buf.append('[');
			if (theMatrix[i] == null)
				nrCols = 0;
			else { 
				nrCols = theMatrix[i].length;
				for (int j = 0; j < nrCols - 1; j++) {
					buf.append(theMatrix[i][j]).append(", ");
				}
				buf.append(theMatrix[i][nrCols - 1]);
			}
//			buf.append(']');
			if (i < nrRows - 1)
				buf.append("; ");
		}
		buf.append("</StringMatrix>");
		return buf.toString();
	}
	
	/**
	 * getStringArrayAsXML
	 * returns the given String[] as XML.
	 * @param stringArray The String[].
	 * @return The given String[] as XML.
	 */
	private static String getStringArrayAsXML(String[] stringArray) {
		if (stringArray == null || stringArray.length < 1)
			throw new IllegalArgumentException("StringArrays cannot be empty!");
		StringBuilder buf = new StringBuilder(23);
		buf.append("<StringArray>");
		for (int i=0; i<stringArray.length-1; i++) {
			buf.append(stringArray[i]);
			buf.append(", ");
		}
			
		buf.append(stringArray[stringArray.length-1]);
		buf.append("</StringArray>");
		return buf.toString();
	}
    /**
     * getVectorAsXML
     * returns the given String[] as XML.
     * @param stringArray The String[].
     * @return The given String[] as XML.
     */
    private static String getVectorAsXML(Vector<String> vector) {
        if (vector == null || vector.size() < 1)
            throw new IllegalArgumentException("StringVectors cannot be empty!");
        StringBuilder buf = new StringBuilder(23);
        buf.append("<Enumeration>");
        for (int i=0; i<vector.size()-1; i++) {
            buf.append(vector.elementAt(i));
            buf.append(", ");
        }
            
        buf.append(vector.elementAt(vector.size()-1));
        buf.append("</Enumeration>");
        return buf.toString();
    }
	
	/**
	 * getColorAsXML
	 * returns the given Color as XML.
	 * @param c The Color.
	 * @return The given Color as XML.
	 */
	private static String getColorAsXML(Color c) {
		StringBuilder buf = new StringBuilder(29);
		buf.append("<Color>(");
		buf.append(Integer.toString(c.getRed()));
		buf.append(", ");
		buf.append(Integer.toString(c.getGreen()));
		buf.append(", ");
		buf.append(Integer.toString(c.getBlue()));
		buf.append(")</Color>");
		return buf.toString();
	}
	
	
	/**
	 * getFontAsXML
	 * returns the given Font as XML.
	 * @param f The Font.
	 * @return The given Font as XML.
	 */
	private static String getFontAsXML(Font f) {
		StringBuilder buf = new StringBuilder(23);
		buf.append("<Font>");
		// later we can save size and fontname and more...
		buf.append(f.getFamily());
		buf.append("</Font>");
		return buf.toString();
	}
	
	
	/**
	 * replaceEntitiesForXML
	 * replaces all Characters in the given String, so that the returned String
	 * can be written to an XML File. The replaced Characters are:
	 * <, >, &, ', "
	 * @param str The String that should be replaced.
	 * @return The given String with replaced illegal Characters.
	 */
	public static String replaceEntitiesForXML(String str) {
		String ret = new String(str);
		ret = ret.replaceAll("&", "&amp;");
		ret = ret.replaceAll("<", "&lt;");
		ret = ret.replaceAll(">", "&gt;");
		ret = ret.replaceAll("'", "&apos;");
		ret = ret.replaceAll("\"", "&quot;");
		return ret;
	}
	
	
	/**
	 * getSimpleName
	 * returns only the last part of a class name. Fore example
	 * "java.lang.String" becomes "String".
	 * @param str The long class name.
	 * @return The simple class name.
	 */
	public static String getSimpleName(String str) {
		if (str == null || str.length() == 0) return "";
		int from = str.lastIndexOf(".");
		if (from == -1) return str;
		return str.substring(from + 1);
	}
	
	
	/**
	 * getXMLString
	 * returns the last visited Item as an XML String.
	 * @return The last visited Item as an XML String.
	 */
	public String getXMLString() {
		return this.buffer.toString();
	}
}
