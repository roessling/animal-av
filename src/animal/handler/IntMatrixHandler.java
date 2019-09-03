package animal.handler;

import animal.graphics.meta.PTMatrix;

/**
 * Handler for operations that can be performed on integer matrices.
 */
public class IntMatrixHandler extends MatrixHandler {

	@Override
	protected void setDataAt(int row, int column, String value, PTMatrix matrix) {
		matrix.setElementAt(row, column, value);
		
	}
	
//	public static final String MATRIX_METHOD_SET_TEXT = "setText";
//	
//	public static final String MATRIX_METHOD_SET_HIGHLIGHTED = "setHighlighted";
//	
//	public static final String MATRIX_METHOD_SET_UNHIGHLIGHTED = "setUnhighlighted";
//	
//	public static final String MATRIX_METHOD_SET_ELEMENT_HIGHLIGHTED = "setElemHighlighted";
//	
//	public static final String MATRIX_METHOD_SET_ELEMENT_UNHIGHLIGHTED = "setElemUnhighlighted";
//	
//	public static final String MATRIX_METHOD_SET_VISIBLE = "setVisible";
//	
//	public static final String MATRIX_METHOD_SET_INVISIBLE = "setInvisible";
//	
//	public static final String MATRIX_METHOD_SET_OUTLINED = "setOutlined";
//	
//	public static final String MATRIX_METHOD_SET_NOTOUTLINED = "setNotOutlined";
//	
//	public Vector<String> getMethods(PTGraphicObject ptgo, Object obj) {
//		Vector<String> result = new Vector<String>();
//		
//		PTIntMatrix matrix = null;
//		if(ptgo instanceof PTIntMatrix){
//			matrix = (PTIntMatrix) ptgo;
//		}else
//			return result;
//		if (obj instanceof Color) {
//			result.addElement("color");
//			result.addElement("fillColor");
//			result.addElement("highlightColor");
//			result.addElement("elementHighlightColor");
//		}
//
//		else if (obj instanceof Point) 
//			result.addElement("translate"); 
//		else if (obj instanceof Boolean) {
//			result.addElement("show"); 
//			result.addElement("hide"); 
//			for (int r = 0; r < matrix.getRowCount(); r++) {
//				for (int c = 0; c < matrix.getColumnCount(r); c++) {
//					result.addElement("showCell row: " +r+" column: "+c);
//					result.addElement("hideCell row: " +r+" column: "+c);
//				}
//			}
//			} else if (obj instanceof double[]) {
//			result.addElement("highlight cell elements"); 
//			result.addElement("highlight cells");
//			result.addElement("unhighlight cell elements");
//			result.addElement("unhighlight cells");
//			result.addElement("set cell elements visible");
//			result.addElement("set cells visible");
//			result.addElement("set cell elements invisible");
//			result.addElement("set cells invisible");
//			
//			}else if (obj instanceof IndexedContentProperty){
//				Object property = ((IndexedContentProperty)obj).getProperty();
//					if(property instanceof String) {
//						result.addElement(PTIntMatrix.KIND_OF_OBJECT_CELL+" "+MATRIX_METHOD_SET_TEXT);
//					}
//					else if(property instanceof Double){
//						result.addElement(PTIntMatrix.KIND_OF_OBJECT_CELL+" "+MATRIX_METHOD_SET_HIGHLIGHTED);
//						result.addElement(PTIntMatrix.KIND_OF_OBJECT_CELL+" "+MATRIX_METHOD_SET_UNHIGHLIGHTED);
//						result.addElement(PTIntMatrix.KIND_OF_OBJECT_CELL+" "+MATRIX_METHOD_SET_ELEMENT_HIGHLIGHTED);
//						result.addElement(PTIntMatrix.KIND_OF_OBJECT_CELL+" "+MATRIX_METHOD_SET_ELEMENT_UNHIGHLIGHTED);
//						result.addElement(PTIntMatrix.KIND_OF_OBJECT_CELL+" "+MATRIX_METHOD_SET_VISIBLE);
//						result.addElement(PTIntMatrix.KIND_OF_OBJECT_CELL+" "+MATRIX_METHOD_SET_INVISIBLE);
//						result.addElement(PTIntMatrix.KIND_OF_OBJECT_CELL+" "+MATRIX_METHOD_SET_OUTLINED);
//						result.addElement(PTIntMatrix.KIND_OF_OBJECT_CELL+" "+MATRIX_METHOD_SET_NOTOUTLINED);
//					}
//			}
//		
//		addExtensionMethodsFor(ptgo, obj, result);
//
//		return result;
//	}
//
//	/**
//	 * Transform the requested property change in method calls
//	 * 
//	 * @param ptgo
//	 *          the graphical primitive to modify
//	 * @param e
//	 *          the PropertyChangeEvent that encodes the information which
//	 *          property has to change how
//	 */
//	public void propertyChange(PTGraphicObject ptgo, PropertyChangeEvent e) {
//		// only works for PTIntMatrix
//		PTIntMatrix matrix = null;
//		if (ptgo instanceof PTIntMatrix) {
//			matrix = (PTIntMatrix) ptgo; 
//			String what = e.getPropertyName(); 
//
//			if (what.equalsIgnoreCase("color")) // set color
//				matrix.setColor((Color) e.getNewValue());
//			else if(what.equalsIgnoreCase("fillColor")) // set fill color
//				matrix.setFillColor((Color) e.getNewValue());
//			else if(what.equalsIgnoreCase("textColor")) // set text color
//				matrix.setTextColor((Color) e.getNewValue());
//			else if(what.equalsIgnoreCase("highlightColor")) // set highlight color
//				matrix.setHighlightColor((Color) e.getNewValue());
//			else if(what.equalsIgnoreCase("textColor")) // set elementHighlight color
//				matrix.setElemHighlightColor((Color) e.getNewValue());
//			else if (what.equalsIgnoreCase("translate")) { // move the matrix
//				Point old = (Point) e.getOldValue();
//				Point now = (Point) e.getNewValue();
//				Point diff = MSMath.diff(now, old);
//				// translate by (new - old)
//				matrix.translate(diff.x, diff.y);
//			}else if(what.startsWith(PTIntMatrix.KIND_OF_OBJECT_CELL)&&
//					what.contains("setText")){
//				String [] tokens;
//				tokens = what.split("\\s");
//				if(tokens.length >= 3 && (tokens[2].length()>4)){
//					//cut leading "{[" and last "]}"
//					String[] indexTuples;
//					if(tokens[2].contains("]["))
//					 indexTuples = tokens[2].substring(2, tokens[2].length()-2).split("\\]\\[");
//					 else{
//						 indexTuples = new String[1];
//						 indexTuples[0] = tokens[2].substring(2, tokens[2].length()-2);
//					 }
//					String[]indices;
//					int value;
//					int r;
//					int c;
//					for(int i = 0; i < indexTuples.length;++i){
//						indices = indexTuples[i].split(",");
//						if(indices.length > 1)
//							try {
//								value = Integer.parseInt((String)((IndexedContentProperty)e.getNewValue()).getProperty());
//								r = Integer.parseInt(indices[0]);
//								c = Integer.parseInt(indices[1]);
//								if(r == -1)
//								{
//									for(int row = 0; row < matrix.getRowCount();row++){
//										if(c == -1){
//											for(int column = 0; column < matrix.getColumnCount(row);column++){
//												matrix.setDataAt(row, column, value);
//											}
//										}else{
//											matrix.setDataAt(row, c, value);
//										}
//									}
//								}else if(c == -1){
//									for(int column = 0; column < matrix.getColumnCount(r);column++){
//										matrix.setDataAt(r, column, value);
//									}
//								}else{
//									matrix.setDataAt(r, c, value);
//								}
//							} catch (NumberFormatException exc) {
//								value = 0;
//							}
//					}
//				}
//			}else if(e.getNewValue() instanceof IndexedContentProperty &&
//					((IndexedContentProperty)e.getNewValue()).getProperty() instanceof Double){
//				String [] tokens;
//				tokens = what.split("\\s");
//				if(tokens.length >= 3 && tokens[2].length()>4){
//					//cut leading "{[" and last "]}"
//					String[] indexTuples;
//					if(tokens[2].contains("]["))
//					 indexTuples = tokens[2].substring(2, tokens[2].length()-2).split("\\]\\[");
//					 else{
//						 indexTuples = new String[1];
//						 indexTuples[0] = tokens[2].substring(2, tokens[2].length()-2);
//					 }
//					String[]indices;
//					double value;
//					int r;
//					int c;
//					for(int i = 0; i < indexTuples.length;++i){
//						indices = indexTuples[i].split(",");
//						if(indices.length > 1)
//							try {
//								value = ((Double)((IndexedContentProperty)e.getNewValue()).getProperty());
//								r = Integer.parseInt(indices[0]);
//								c = Integer.parseInt(indices[1]);
//								if(r == -1)
//								{
//									for(int row = 0; row < matrix.getRowCount();row++){
//										if(c == -1){
//											for(int column = 0; column < matrix.getColumnCount(row);column++){
//												handleDoublePropertyChange(row,column,value,tokens[1],matrix);
//											}
//										}else{
//											handleDoublePropertyChange(row,c,value,tokens[1],matrix);
//										}
//									}
//								}else if(c == -1){
//									for(int column = 0; column < matrix.getColumnCount(r);column++){
//										handleDoublePropertyChange(r,column,value,tokens[1],matrix);
//									}
//								}else{
//									handleDoublePropertyChange(r,c,value,tokens[1],matrix);
//								}
//							} catch (NumberFormatException exc) {
//								value = 0;
//							}
//					}
//				}
//			}else if(what.startsWith("setText ")){			
//				try{
//					int rowIndex = what.indexOf("row: ");
//					int columnIndex = what.indexOf("column: ");
//					String parseString = what.substring(rowIndex+"row: ".length(),
//							columnIndex-1);
//					int r = Integer.parseInt(parseString);
//					parseString = what.substring(columnIndex+"column: ".length());
//					int c = Integer.parseInt(parseString);
//					int value = Integer.parseInt((String)e.getNewValue());
//					matrix.setDataAt(r, c, value);
//				}catch(NumberFormatException exc) {
//					matrix.setDataAt(0, 0, 0);
//				}
//			} else { // not handled here; pass up to superclass
//				super.propertyChange(ptgo, e);
//			}
//		}
//	}
//
//	private void handleDoublePropertyChange(int r, int c, double value, String selectedMethod,PTIntMatrix matrix) {
//		if(selectedMethod.equals(MATRIX_METHOD_SET_HIGHLIGHTED)){
//			if(value > 0.95)
//				matrix.setHighlighted(r, c, true);
//			else
//			setFadeColorBackground(r,c,value,matrix.getHighlightColor(),matrix);			
//		}else if(selectedMethod.equals(MATRIX_METHOD_SET_UNHIGHLIGHTED)){
//			if(value > 0.95)
//				matrix.setHighlighted(r, c, false);
//			else
//			setFadeColorBackground(r,c,value,matrix.getFillColor(),matrix);
//		}
//		else if(selectedMethod.equals(MATRIX_METHOD_SET_ELEMENT_HIGHLIGHTED)){
//			if(value > 0.95)
//				matrix.setElementHighlighted(r, c, true);
//			else
//			setFadeColorText(r,c,value,matrix.getElemHighlightColor(),matrix);
//		}
//		else if(selectedMethod.equals(MATRIX_METHOD_SET_ELEMENT_UNHIGHLIGHTED)){
//			if(value > 0.95)
//				matrix.setElementHighlighted(r, c, false);
//			else
//			setFadeColorText(r,c,value,matrix.getTextColor(),matrix);
//		}
//		else if(selectedMethod.equals(MATRIX_METHOD_SET_VISIBLE)){
//			if(value > 0.95)
//				matrix.setVisible(r, c, true);
//			else
//			setFadeColorText(r,c,value,matrix.getTextColor(),matrix);
//		}
//		else if(selectedMethod.equals(MATRIX_METHOD_SET_INVISIBLE)){
//			if(value > 0.95)
//				matrix.setVisible(r, c, false);
//			else
//				setFadeColorText(r,c,value,matrix.getFillColor(),matrix);		
//		}
//		else if(selectedMethod.equals(MATRIX_METHOD_SET_OUTLINED)){
//			if(value > 0.95)
//				matrix.setOutlined(r, c, true);
//			else
//				setFadeColorOutline(r,c,value,matrix.getColor(),matrix);
//		}
//		else if(selectedMethod.equals(MATRIX_METHOD_SET_NOTOUTLINED)){
//			if(value > 0.95)
//				matrix.setOutlined(r, c, false);
//			else
//				setFadeColorOutline(r,c,value,matrix.getFillColor(),matrix);
//		}		
//	}
//
//	private void setFadeColorText(int r, int c, double value, Color endColor,PTIntMatrix matrix) {
//		matrix.setCellTextColor(r,c,calcFadeColor(matrix.getTextColor(r,c),endColor,value));		
//	}
//
//	private void setFadeColorBackground(int r, int c, double value, Color endColor,PTIntMatrix matrix) {
//		matrix.setCellFillColor(r,c,calcFadeColor(matrix.getFillColor(r,c),endColor,value));		
//	}
//	
//	private void setFadeColorOutline(int r, int c, double value, Color endColor,PTIntMatrix matrix) {
//		matrix.setCellColor(r,c,calcFadeColor(matrix.getColor(r,c),endColor,value));		
//	}
//	
//	private Color calcFadeColor(Color startColor, Color endColor, double value) {
//		int r =(int)( startColor.getRed() * (1-value) + endColor.getRed()*value);
//		int g =(int)( startColor.getRed() * (1-value) + endColor.getRed()*value);
//		int b =(int)( startColor.getRed() * (1-value) + endColor.getRed()*value);
//		return new Color(r,g,b);
//	}
}
