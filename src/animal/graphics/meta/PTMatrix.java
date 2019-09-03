package animal.graphics.meta;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTRectangle;
import animal.graphics.PTText;
import animal.main.Animal;
import animalscript.extensions.GridMath;
import animalscript.extensions.GridProducer;

public abstract class PTMatrix extends PTGraphicObject implements FillablePrimitive,
	IndexableContentContainer, TextContainer {	
	
	public enum Alignment {LEFT, CENTER,RIGHT};

	// =====================================================================
	// Public Constants
	// =====================================================================
		
	public static final String KIND_OF_OBJECT_CELL = "Matrix.cellIdentifier";
	
	public static final String KIND_OF_OBJECT_ROW = "Matrix.rowIdentifier";
	
	public static final String PRIMARY_DIMENSION_IDENTIFIER = "Matrix.rowIndex";
	
	public static final String SECONDARY_DIMENSION_IDENTIFIER = "Matrix.columnIndex";
		
	// =====================================================================
	// Fields
	// =====================================================================
	
	private Point location = new Point(0, 0);		
		
	protected Vector<Vector<PTText>> vTextData = new Vector<Vector<PTText>>();
	
	private Vector<Vector<PTRectangle>> vCellRectangles = new Vector<Vector<PTRectangle>>();
	
	private PTGraphicObject[] brackets = null;
	
	protected int[] columntextwidths = new int[0];
	
	/**
	 * margin for each Cell from text to rectangle in order top,right,bottom,left
	 */
	private int[] margin = { 5, 5, 5, 5};

	private Alignment[][] textAlignments;
	private Alignment textAlignmentDefault = Alignment.LEFT;
	
	private Alignment rowAlignment = Alignment.LEFT;
	
	protected Font[][] cellFonts;

	protected Color[][] fillColors;
	protected Color[][] cellHighlightColors;
	protected Color[][] elemHighlightColors;
	protected Color[][] textColors;
	protected Color[][] borderColors;
	protected Color[][] borderHighlightColors;

	private Color cellBorderColor = Color.BLACK;
	private Color cellBorderHighlightColor = Color.RED;
	
	private Color fillColor = Color.WHITE;
	
	private Color cellHighlightColor = Color.YELLOW;
	
	private Color elemHighlightColor = Color.RED;
	
	private boolean filled = true;
	
	private Font font = new Font("SansSerif", Font.PLAIN, 20);	
	
	private Color textColor = Color.BLACK;
	
	private boolean layoutChanged = false;
	
	private boolean visualPropertiesChanged = false;	
	
	protected final byte OUTLINED = (byte) 1;
	
	protected final byte HIGHLIGHTED = (byte) 2;

	protected final byte ELEMENT_HIGHLIGHTED = (byte) 4;

	protected final byte VISIBLE = (byte) 8;
	
	protected Vector<Vector<Byte>> vCellStatus = new Vector<Vector<Byte>>();
	
	protected int style = GridProducer.STYLE_PLAIN;

	public PTMatrix() {
		  
	}
	
	protected void init() {		
		this.setDepth(2);		
		layoutChanged = true;
		for(int i = 0; i < this.columntextwidths.length;++i){
			this.columntextwidths[i] = 0;
		}
	}
	
	public void updateFullObject() {
    visualPropertiesChanged = true;
    layoutChanged = true;
	}

	// ======================================================================
	// Attribute accessing
	// ======================================================================
	public int getFileVersion() {
		return 1; 
	}
	
	public void setStyle(int style){
		this.style = style;
		layoutChanged = true;
	}
	
	public byte getCellStatus(int r, int c){
		if(indicesAreInRange(r,c))
			return this.vCellStatus.get(r).get(c);
		return Byte.MAX_VALUE;
	}
	
	public boolean isCellOutlined(int r, int c){
		if(indicesAreInRange(r,c))
			return((this.vCellStatus.get(r).get(c) & OUTLINED) == OUTLINED);
		return true;
	}
	
	public boolean isCellHighlighted(int r, int c){
		if(indicesAreInRange(r,c))
			return((this.vCellStatus.get(r).get(c) & HIGHLIGHTED) == HIGHLIGHTED);
		return true;
	}
	
	public boolean isCellElementHighlighted(int r, int c){
		if(indicesAreInRange(r,c))
			return((this.vCellStatus.get(r).get(c) & ELEMENT_HIGHLIGHTED) == ELEMENT_HIGHLIGHTED);
		return true;
	}
	
	public boolean isCellVisible(int r, int c){
		if(indicesAreInRange(r,c))
			return((this.vCellStatus.get(r).get(c) & VISIBLE) == VISIBLE);
		return true;
	}
	
	@Override
	public String getType() {
		return TYPE_LABEL;
	}
	
	public abstract int getRowCount();

	public abstract int getColumnCount(int row);
	
	public int getMaxColumnCount() {
		int cnt= 0;
		int rowCount = getRowCount();
		for(int r = 0; r < rowCount;++r){
			if(getColumnCount(r) > cnt)
				cnt = getColumnCount(r);
		}
		return cnt;
	}
	
	@Override
	public String[] handledKeywords() {
		return new String[]{"Matrix"};
	}
	
	@Override
	public Rectangle getBoundingBox() {
		int width = 0, height = 0;
		for(int c =0;c<this.columntextwidths.length;++c){			
			width += this.columntextwidths[c];
		}	
		//+1 because rectangles are one unit wider than their width
		//left border at x, right border at x + width
		//(see also void java.awt.Graphics.drawRect(int x, int y, int width, int height))
		width += getMaxColumnCount() * (margin[1]+margin[3]+1);
		for(int r =0;r<getRowCount();++r){
			height += getTextHeight(r,0);
		}
		//+1: reason is the same as for the width
		height += getRowCount() * (margin[0]+margin[2]+1);
		return new Rectangle(location.x,location.y,width,height);
	}
	
	private int getTextHeight(int r, int c) {
		if(indicesAreInRange(r,c))
//		if(c >= 0 && vTextData.get(r).size() > c)
			return this.vTextData.get(r).get(c).getBoundingBox().height;
		else
			return 0;
	}

	public abstract String getElementAt(int r, int c);
		
	public Point getLocation() {
		return location;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public Color getHighlightColor() {
		return cellHighlightColor;
	}
	
	public Color getElemHighlightColor() {
		return elemHighlightColor;
	}

	public Color getBorderColor() {
		return cellBorderColor;
	}

	public Color getBorderHighlightColor() {
		return cellBorderHighlightColor;
	}

	public Font getFont() {
		return font;
	}

	public Color getTextColor() {
		return textColor;
	}
	
	public boolean isFilled() {
		return filled;
	}

	/**
	 * Returns margin of specified direction.
	 * Directions are:  top = 0, right = 1, bottom = 2, left = 3
	 * @param value direction for which margin should be returned
	 * @return margin in pixel
	 */
	public int getMargin(int value){
		return this.margin[value%4];
	}

	public Alignment getRowAlignment() {
		return rowAlignment;
	}

	public void setCellFonts(int r, int c, Font font) {
		cellFonts[r][c] = font;
		for (int i = 0; i < this.getMaxColumnCount(); ++i) {
			this.updateColumnWidth(i);
		}
		visualPropertiesChanged = true;
		layoutChanged = true;
	}
	
	public void setTextColor(int r, int c, Color color) {
		textColors[r][c] = color;
		visualPropertiesChanged = true;
	}
	
	public void setFillColor(int r, int c, Color color) {
		fillColors[r][c] = color;
		visualPropertiesChanged = true;
	}
	
	public void setBorderColor(int r, int c, Color color) {
		borderColors[r][c] = color;
		visualPropertiesChanged = true;
	}

	public void setCellHighlightColor(int r, int c, Color color) {
		cellHighlightColors[r][c] = color;
		visualPropertiesChanged = true;
	}
	
	public void setElemHighlightColor(int r, int c, Color color) {
		elemHighlightColors[r][c] = color;
		visualPropertiesChanged = true;
	}
	
	public void setBorderHighlightColor(int r, int c, Color color) {
		borderHighlightColors[r][c] = color;
		visualPropertiesChanged = true;
	}
	
	public Font getCellFont(int r, int c) {
		return cellFonts[r][c];
	}

	public Color getTextColor(int r, int c) {
		return textColors[r][c];
	}
	
	public Color getFillColor(int r, int c) {
		return fillColors[r][c];
	}
	
	public Color getBorderColor(int r, int c) {
		return borderColors[r][c];
	}

	public Color getCellHighlightColor(int r, int c) {
		return cellHighlightColors[r][c];
	}
	
	public Color getElemHighlightColor(int r, int c) {
		return elemHighlightColors[r][c];
	}
	
	public Color getBorderHighlightColor(int r, int c) {
		return borderHighlightColors[r][c];
	}
	
	public Color getCellColor(int r, int c) {
		if(isCellHighlighted(r, c)){
			return cellHighlightColors[r][c];
		}else{
			return fillColors[r][c];
		}
	}
	
	public Color getElemColor(int r, int c) {
		if(isCellElementHighlighted(r, c)){
			return elemHighlightColors[r][c];
		}else{
			return textColors[r][c];
		}
	}
	
	public Color getCellBorderColor(int r, int c) {
		if(isCellHighlighted(r, c)){
			return borderHighlightColors[r][c];
		}else{
			return borderColors[r][c];
		}
	}

	public Color getColor(int r, int c) {
		if(this.indicesAreInRange(r, c))
			return this.vCellRectangles.get(r).get(c).getColor();
		return this.getColor();
	}
	// ======================================================================
	// Attribute setting
	// ======================================================================
	
	public void setOutlined(int r, int c,boolean value){
		if(indicesAreInRange(r,c)){
			if(value){
				this.vCellStatus.get(r).set(c,new Byte((byte)( vCellStatus.get(r).get(c) | OUTLINED)));
			}else
				this.vCellStatus.get(r).set(c,new Byte((byte)( vCellStatus.get(r).get(c) & ~OUTLINED)));
			layoutChanged = true;
		}
	}
	
	public void setHighlighted(int r, int c,boolean value){
		if(indicesAreInRange(r,c)){
			if(value){
				this.vCellStatus.get(r).set(c,new Byte((byte)( vCellStatus.get(r).get(c) | HIGHLIGHTED)));
			}else
				this.vCellStatus.get(r).set(c,new Byte((byte)( vCellStatus.get(r).get(c) & ~HIGHLIGHTED)));
			layoutChanged = true;
		}
	}
	
	public void setElementHighlighted(int r, int c,boolean value){
		if(indicesAreInRange(r,c)){
			if(value){
				this.vCellStatus.get(r).set(c,new Byte((byte)( vCellStatus.get(r).get(c) | ELEMENT_HIGHLIGHTED)));
			}else
				this.vCellStatus.get(r).set(c,new Byte((byte)( vCellStatus.get(r).get(c) & ~ELEMENT_HIGHLIGHTED)));
			layoutChanged = true;
		}
	}
	
	public void setVisible(int r, int c,boolean value){
		if(indicesAreInRange(r,c)){
			if(value){
				this.vCellStatus.get(r).set(c,new Byte((byte)( vCellStatus.get(r).get(c) | VISIBLE)));
			}else
				this.vCellStatus.get(r).set(c,new Byte((byte)( vCellStatus.get(r).get(c) & ~VISIBLE)));
			layoutChanged = true;
		}
	}

	public void setSize(int r, int c){
		setRowCount(r);		
		for(int rowIndex = 0; rowIndex<r; ++rowIndex){
			setColumnCount(rowIndex,c);
		}
		Alignment[][] newAlignments = new Alignment[r][c];
		for (int i = 0; i < newAlignments.length; i++) {
			for (int j = 0; j < newAlignments[i].length; j++) {
				if(textAlignments!=null && textAlignments.length>=i && textAlignments[i].length>=j){
					newAlignments[i][j] = textAlignments[i][j];
				}else{
					newAlignments[i][j] = textAlignmentDefault;
				}
			}
		}
		textAlignments = newAlignments;

		cellFonts = new Font[r][c];
		
		fillColors = new Color[r][c];
		cellHighlightColors = new Color[r][c];
		elemHighlightColors = new Color[r][c];
		textColors = new Color[r][c];
		borderColors = new Color[r][c];
		borderHighlightColors = new Color[r][c];
	}
	
	public void setColumnCount(int rowIndex, int value) {
		if(value >0 && rowIndex >= 0 && rowIndex < getRowCount()){
			//add some rows to match c
			int oldMaxColumnCount = getMaxColumnCount();
			if(value > oldMaxColumnCount)
				enlargeColumnTextWidthsTo(value);
			int columncount = getColumnCount(rowIndex);
			if(value > columncount){
				addColumns(rowIndex,value-columncount);
				if(this.getRowAlignment() == Alignment.RIGHT){
					//Von rechts wurde aufgefuellt, also haben sich die Daten der Zeile
					//nach links verschoben. Es muss vom neuen Zeilenstart aus,
					//jede Spaltenbreite ueberprueft werden
					for(int i = getMaxColumnCount()-getColumnCount(rowIndex);
					i < getMaxColumnCount();++i)
						updateColumnWidth(i);
				}else if(this.getRowAlignment() == Alignment.CENTER){
					//Alle Zeilen verschieben sich, daher komplett neue 
					//Spaltenbreitenberechnung
					for(int i = 0;i < getMaxColumnCount();++i)
						updateColumnWidth(i);
				}				
			}
			if(value < columncount){
				int columnsToDelete =columncount - value;
				shrinkDataRow(rowIndex,value);
				vTextData.get(rowIndex).setSize(value);
				vCellRectangles.get(rowIndex).setSize(value);
				vCellStatus.get(rowIndex).setSize(value);
				
				if(oldMaxColumnCount > getMaxColumnCount())
					shrinkColumnTextWidthsTo(getMaxColumnCount());				
				if(getRowAlignment() == Alignment.LEFT)
					for(int i = columncount;
					i < columncount+columnsToDelete;++i)
						updateColumnWidth(i);
				else if(getRowAlignment() == Alignment.RIGHT)
					//es muss ueber alle Spalten iteriert werden, die
					//die alte Zeile belegte
					for(int i = oldMaxColumnCount-getColumnCount(rowIndex)-columnsToDelete;
					i < getMaxColumnCount();++i)
						updateColumnWidth(i);
				else if(getRowAlignment() == Alignment.CENTER){
					//es muss ueber alle Spalten iteriert werden, die
					//die alte Zeile belegte
					int columnStartIndex;
					if((oldMaxColumnCount-getColumnCount(rowIndex)-columnsToDelete)%2 == 0){
						columnStartIndex = (oldMaxColumnCount-getColumnCount(rowIndex)-columnsToDelete)/2;
					}else{
						columnStartIndex = (oldMaxColumnCount-getColumnCount(rowIndex)-columnsToDelete-1)/2;
					}
					for(int i = columnStartIndex;i < getMaxColumnCount();++i)
						updateColumnWidth(i);
				}				
			}
			layoutChanged = true;
		}
	}
		
	

	public void setRowCount(int value) {
		if(value > 0){
			//add some rows to match r
			int rowcount = getRowCount();
			if(value > rowcount){
				int columnCnt = getMaxColumnCount();
				for(int i = rowcount; i < value;++i){
					addRow(columnCnt);					
				}
				layoutChanged = true;
			}
			//delete some rows to match r
			if(value < rowcount){
				setDataRowCount(value);
				vTextData.setSize(value);
				vCellRectangles.setSize(value);
				vCellStatus.setSize(value);
			}
		}
	}
	/**
	 * Sets the elements of the matrix diagonally down or up to the right of starting index c.
	 * 
	 * @param r starting row index
	 * @param c starting column index
	 * @param value value to set the diagonal to
	 * @param down true if direction is down to the right. false for up to the right
	 */
	
	public void setDiagonalElements(int r, int c, String value, boolean down){
		int maxcolumncount = this.getMaxColumnCount();
		int rowcount = this.getRowCount();
		if(r >= 0 && r < rowcount && c >= 0 && c < maxcolumncount){
			if(down){
				for(int x = c,  y = r; x < maxcolumncount && y < rowcount; ++x,++y ){
					if(x < getColumnCount(y)){
						setDataOfSpecialType(y,x,value);
						setTextDataAt(y,x,value);
					}
				}
			}else{			
				for(int x = c,  y = r; x < maxcolumncount && y >= 0; ++x,--y ){
					if(x < getColumnCount(y)){
						setDataOfSpecialType(y,x,value);
						setTextDataAt(y,x,value);
					}
				}
			}
		}
	}
	
	public void setToIdentity(){
		int maxcolumncount = getMaxColumnCount();
		for(int r = 0; r < getRowCount();++r){
			this.setColumnCount(r, maxcolumncount);
			for(int c = 0; c < maxcolumncount; ++c)
				setElementAt(r,c,"0");			
		}		
		this.setDiagonalElements(0, 0, "1", true);
	}

	public void setElementAt(int r, int c, String value){
		if(indicesAreInRange(r,c)){
			setDataOfSpecialType(r,c,value);
			setTextDataAt(r,c,value);
		}
	}

	public void setTextDataAt(int r, int c, String value) {
//		int oldWidth = Animal.getStringWidth(String.valueOf(vTextData.get(r).get(c).getText()),
//				vTextData.get(r).get(c).getFont());
	  if (r >= 0 && c >= 0) { // concrete cell (row, column given)
	    setInternalTextDataAt(r, c, value); // simply set it
	  } else if (r == -1 && c == -1) {
	    for (int i = 0; i < getRowCount(); i++) {
	      setTextDataAt(i, c, value); // do this for all rows and lines
	    }
	  }
	  else if (r == -1) {
	    for (int i = 0; i < getRowCount(); i++)
	      setInternalTextDataAt(i, c, value);
	  }
	  else if (c == -1) {
	    for (int i = 0; i < getColumnCount(r); i++)
	      setInternalTextDataAt(r, i, value);
	  }
	}
	
	public void setInternalTextDataAt(int r, int c, String value) {  
		int oldWidth = 	vTextData.get(r).get(c).getBoundingBox().width;
		vTextData.get(r).get(c).setText(value);
		if(oldWidth == columntextwidths[c] && 
				Animal.getStringWidth(value,
						vTextData.get(r).get(c).getFont()) <
				columntextwidths[c]){
			updateColumnWidth(c);
		}else{
			setColumnWidth(vTextData.get(r).get(c).getText(),
					vTextData.get(r).get(c).getFont(),r, c);
		}		
		layoutChanged = true;
	}
	
	public void swapTextData(int r1, int c1, int r2, int c2){
		String value1 = getElementAt(r1, c1);
		String value2 = getElementAt(r2, c2);
		setTextDataAt(r1, c1, value2);
		setTextDataAt(r2, c2, value1);
	}

	public void setCellStatus(byte b,int r, int c){
		if(indicesAreInRange(r,c)){
			this.vCellStatus.get(r).set(c,b);
			this.visualPropertiesChanged = true;
		}
	}


	public void setLocation(Point location) {
		this.location = location;
		this.updateLocations();
	}

	public void setDepth(int depth) {
		if(depth > 1){
			super.setDepth(depth);
			visualPropertiesChanged = true;
		}
	}

	public void setColor(Color color) {
		super.setColor(color);
		visualPropertiesChanged = true;
	}

	public void setFillColor(Color fillcolor) {
		this.fillColor = fillcolor;
		for (int i = 0; i < fillColors.length; i++) {
			for (int j = 0; j < fillColors[i].length; j++) {
				fillColors[i][j] = fillcolor;
			}
		}
		visualPropertiesChanged = true;
	}

	public void setHighlightColor(Color highlightColor) {
		this.cellHighlightColor = highlightColor;
		for (int i = 0; i < cellHighlightColors.length; i++) {
			for (int j = 0; j < cellHighlightColors[i].length; j++) {
				cellHighlightColors[i][j] = highlightColor;
			}
		}
		visualPropertiesChanged = true;
	}
	
	public void setElemHighlightColor(Color highlightColor) {
		this.elemHighlightColor = highlightColor;
		for (int i = 0; i < elemHighlightColors.length; i++) {
			for (int j = 0; j < elemHighlightColors[i].length; j++) {
				elemHighlightColors[i][j] = highlightColor;
			}
		}
		visualPropertiesChanged = true;
	}

	public void setFont(Font font) {
		this.font = font;
		//update all column width since width depends on font
		for(int r = 0; r < getRowCount();++r)
			for(int c = 0; c < getColumnCount(r);++c){
				this.vTextData.get(r).get(c).setFont(font);
				cellFonts[r][c] = font;
			}
		int maxcolumncount = this.getMaxColumnCount();
		for(int c = 0; c < maxcolumncount; ++c)
			this.updateColumnWidth(c);
		layoutChanged = true;
	}

	public void setTextColor(Color textcolor) {
		this.textColor = textcolor;
		for (int i = 0; i < textColors.length; i++) {
			for (int j = 0; j < textColors[i].length; j++) {
				textColors[i][j] = textcolor;
			}
		}
		visualPropertiesChanged = true;
	}

	public void setBorderColor(Color color) {
		this.cellBorderColor = color;
		for (int i = 0; i < borderColors.length; i++) {
			for (int j = 0; j < borderColors[i].length; j++) {
				borderColors[i][j] = color;
			}
		}
		visualPropertiesChanged = true;
	}
	public void setBorderHighlightColor(Color color) {
		this.cellBorderHighlightColor = color;
		for (int i = 0; i < borderHighlightColors.length; i++) {
			for (int j = 0; j < borderHighlightColors[i].length; j++) {
				borderHighlightColors[i][j] = color;
			}
		}
		visualPropertiesChanged = true;
	}
	
	public void setFilled(boolean filled) {		
		this.filled = filled;
		visualPropertiesChanged = true;
	}
	
	public void setMargin(int direction,int value){
		if(value >=0){
			this.margin[direction%4]= value;
			layoutChanged = true;
		}
	}
	
	public void setTextAlignment(Alignment textAlignment) {
		this.textAlignmentDefault = textAlignment;
		for (int i = 0; i < textAlignments.length; i++) {
			for (int j = 0; j < textAlignments[i].length; j++) {
				setTextAlignment(textAlignment, i, j);
			}
		}
	}

	public void setTextAlignment(Alignment textAlignment, int r, int c) {
		textAlignments[r][c] = textAlignment;
		layoutChanged = true;
	}
	public Alignment getAlignment(String textAlignment) {
		if (textAlignment.equalsIgnoreCase("left"))
			return Alignment.LEFT;
	    else if (textAlignment.equalsIgnoreCase("right"))
			return Alignment.RIGHT;
	    else if (textAlignment.equalsIgnoreCase("center"))
			return Alignment.CENTER;
	    else
	    	return null;
	}

	public void setRowAlignment(Alignment rowAlignment) {
		this.rowAlignment = rowAlignment;
		layoutChanged = true;
	}

	public void setCellTextColor(int r, int c, Color endColor) {
		if(this.indicesAreInRange(r, c))
			this.vTextData.get(r).get(c).setColor(endColor);		
	}

	public void setCellFillColor(int r, int c, Color Color) {
		if(this.indicesAreInRange(r, c))
			this.vCellRectangles.get(r).get(c).setFillColor(Color);
	}

	public void setCellColor(int r, int c, Color Color) {
		if(this.indicesAreInRange(r, c))
			this.vCellRectangles.get(r).get(c).setColor(Color);				
	}
	
	public void setPosition(Point node) {
		this.location = node;
		layoutChanged = true;
	}
	
	// ======================================================================
	// Drawing
	// ======================================================================
	
	@Override
	public void paint(Graphics g) {	
		if(visualPropertiesChanged){
			updateCellProperties();
			visualPropertiesChanged = false;
		}
		if(layoutChanged){
			updateLocations();
			layoutChanged = false;
		}
		int rowcount = getRowCount();
		int columncount;
		for(int r =0;r<rowcount;++r){
			columncount = getColumnCount(r);
			for(int c =0;c<columncount;++c){
				if( isFilled()||((this.vCellStatus.get(r).get(c) & OUTLINED) == OUTLINED)||
						(this.vCellStatus.get(r).get(c) & VISIBLE) == VISIBLE) 
					if(style==GridProducer.STYLE_TABLE){
						this.vCellRectangles.get(r).get(c).paint(g);
					}else if(isCellHighlighted(r, c)){
						this.vCellRectangles.get(r).get(c).setColor(Color.WHITE);
						this.vCellRectangles.get(r).get(c).paint(g);
					}
				//direct coded to avoid index test in isCellVisible()
				if((this.vCellStatus.get(r).get(c) & VISIBLE) == VISIBLE)
					this.vTextData.get(r).get(c).paint(g);
				
				if(style==GridProducer.STYLE_MATRIX){
					if(brackets == null){
						calcBrackets();
					}
					for (int i = 0; brackets!=null && i < brackets.length ; i++) {
						brackets[i].paint(g);
					}
				}
			}
		}	
	}	


	@Override
	public void translate(int delta_x, int delta_y) {
		location.translate(delta_x, delta_y);
		int rowcount = getRowCount();
		int columncount;
		for(int r =0;r<rowcount;++r){
			columncount = getColumnCount(r);
			for(int c =0;c<columncount;++c){
				this.vCellRectangles.get(r).get(c).translate(delta_x, delta_y);
				this.vTextData.get(r).get(c).translate(delta_x, delta_y);
			}
		}
	}

    
      /**
       * Offers cloning support by cloning or duplicating the shared attributes
       * 
       * @param targetShape the shape into which the values are to be copied. Note
       * the direction -- it is "currentObject.cloneCommonFeaturesInto(newObject)", 
       * not vice versa!
       */
      protected void cloneCommonFeaturesInto(PTMatrix targetShape) {
        // clone features from PTGraphicsObject: color, depth, num, objectName
        super.cloneCommonFeaturesInto(targetShape);

        // clone anything else that is specific to this type
        // and its potential subtypes
        int rowcount = getRowCount();
        int columncount;
        targetShape.setRowCount(rowcount);
        for(int r =0;r<rowcount;++r){
            columncount = getColumnCount(r);
            targetShape.setColumnCount(r, columncount);
            for(int c =0;c<columncount;++c){
                targetShape.setOutlined(r, c, isCellOutlined(r,c));
                targetShape.setHighlighted(r, c, isCellHighlighted(r,c));
                targetShape.setElementHighlighted(r, c, isCellElementHighlighted(r,c));
                targetShape.setVisible(r, c, isCellVisible(r,c));
                targetShape.setElementAt(r, c, this.vTextData.get(r).get(c).getText());
            }
        }       
        if (location != null)
            targetShape.setLocation(new Point(location.x,location.y));
//        targetShape.setColor(createColor(getColor()));
//        targetShape.setDepth(getDepth());
//        targetShape.setObjectName(getObjectName());
//        targetShape.setNum(getNum(false));
        targetShape.setFilled(isFilled());
        targetShape.setFillColor(createColor(getFillColor()));
        targetShape.setHighlightColor(createColor(getHighlightColor()));
        targetShape.setElemHighlightColor(createColor(getElemHighlightColor()));
        targetShape.setFont(new Font(getFont().getFamily(), getFont().getStyle(),
            getFont().getSize()));
        targetShape.setTextColor(createColor(getTextColor()));
        targetShape.setMargin(0, getMargin(0));
        targetShape.setMargin(1, getMargin(1));
        targetShape.setMargin(2, getMargin(2));
        targetShape.setMargin(3, getMargin(3));
        targetShape.setRowAlignment(getRowAlignment());
        
        for (int r = 0; r < textAlignments.length; r++) {
			for (int c = 0; c < textAlignments[r].length; c++) {
				targetShape.setTextAlignment(textAlignments[r][c], r, c);
				
				targetShape.setFillColor(r, c, createColor(fillColors[r][c]));
				targetShape.setCellHighlightColor(r, c, createColor(cellHighlightColors[r][c]));
				targetShape.setElemHighlightColor(r, c, createColor(elemHighlightColors[r][c]));
				targetShape.setTextColor(r, c, createColor(textColors[r][c]));
				targetShape.setBorderColor(r, c, createColor(borderColors[r][c]));
				targetShape.setBorderHighlightColor(r, c, createColor(borderHighlightColors[r][c]));
				targetShape.setCellFonts(r, c, new Font(cellFonts[r][c].getFamily(), cellFonts[r][c].getStyle(), cellFonts[r][c].getSize()));
				
			}
		}
        targetShape.setStyle(style);
      }
    
//    /**
//	 * clones the matrix.
//	 * Subclasses must clone their data and copy it to the returned object
//	 * Uses getNewInstance() to return a clone of the right type
//	 */
//	public Object clone() {
//		PTMatrix matrix = getPreClone();
//		int rowcount = getRowCount();
//		int columncount;
//		matrix.setRowCount(rowcount);
//		for(int r =0;r<rowcount;++r){
//			columncount = this.getColumnCount(r);
//			matrix.setColumnCount(r, columncount);
//			for(int c =0;c<columncount;++c){
//				matrix.setOutlined(r, c, isCellOutlined(r,c));
//				matrix.setHighlighted(r, c, isCellHighlighted(r,c));
//				matrix.setElementHighlighted(r, c, isCellElementHighlighted(r,c));
//				matrix.setVisible(r, c, isCellVisible(r,c));
//			}
//		}		
//		if(this.location != null)
//			matrix.setLocation(new Point(this.location.x,this.location.y));
//		matrix.setDepth(getDepth());
//		matrix.setObjectName(getObjectName());
//		matrix.setNum(getNum(false));
//		matrix.setFilled(this.isFilled());
//		matrix.setColor(createColor(getColor()));
//		matrix.setFillColor(createColor(this.getFillColor()));
//		matrix.setHighlightColor(createColor(this.getHighlightColor()));
//		matrix.setElemHighlightColor(createColor(this.getElemHighlightColor()));
//		matrix.setFont(this.getFont());
//		matrix.setTextColor(createColor(this.getTextColor()));
//		matrix.setMargin(0, this.getMargin(0));
//		matrix.setMargin(1, this.getMargin(1));
//		matrix.setMargin(2, this.getMargin(2));
//		matrix.setMargin(3, this.getMargin(3));
//		matrix.setRowAlignment(this.getRowAlignment());
//		matrix.setTextAlignment(this.getTextAlignment());
//		return matrix;
//	}	

	//=====
	//Misc
	//=====
	/**
	 * Sets the count of elements of row 'rowIndex' to 'value' in 
	 * the special data type vector of the subclass
	 */
	protected abstract void shrinkDataRow(int rowIndex, int value);
	/**
	 * Sets the row count to 'value' of 
	 * the special data type vector of the subclass
	 */
	protected abstract void setDataRowCount(int value) ;
	
	/**
	 * Returns an instance of the subclass of PTMatrix with cloned
	 * vector of the special data type.
	 * @return an instance of the subclass
	 */
	protected abstract PTMatrix getPreClone();
	
	/**
	 * Adds a new row to the special data type vector of the subclass
	 */	
	protected abstract void addDataRow();
	/**
	 * Adds a new data element to row 'rowIndex'
	 * of the special data type vector of the subclass
	 */	
	protected abstract void addDataItem(int rowIndex);
	
	/**
	 * Parses value to the special data type and sets value of row 'r'
	 * and column 'c' of the special data type vector to the parsed value.
	 * @param r row index to set data for
	 * @param c column index to set data for
	 * @param value to set
	 */
	protected abstract void setDataOfSpecialType(int r, int c, String value);
	
	private void setColumnWidth(String textstring, Font font,int row, int column) {
		if(this.getRowAlignment() == Alignment.LEFT){
			if(column < this.columntextwidths.length){
				int width = Animal.getStringWidth(textstring,font); 
				if(width > this.columntextwidths[column]){
					this.columntextwidths[column] = width;
				}
			}
		}
		else if(this.getRowAlignment() == Alignment.RIGHT){
			if(column < this.columntextwidths.length){
				int columnStartIndex = getMaxColumnCount()-getColumnCount(row);
				if(Animal.getStringWidth(textstring,font) > this.columntextwidths[columnStartIndex+column]){
					this.columntextwidths[columnStartIndex+column] = Animal.getStringWidth(textstring,font);
				}
			}
		}
		else if(this.getRowAlignment() == Alignment.CENTER){
			int maxColumnCnt = getMaxColumnCount();
			int columnStartIndex;
			//onGrid
			if((maxColumnCnt-getColumnCount(row))%2 == 0){
				columnStartIndex = (maxColumnCnt-getColumnCount(row))/2;
				if(Animal.getStringWidth(textstring,font) > this.columntextwidths[columnStartIndex+column]){
					this.columntextwidths[columnStartIndex+column] = Animal.getStringWidth(textstring,font);
				}
			}else{
				columnStartIndex = (maxColumnCnt-getColumnCount(row)-1)/2;
				int textWidth =Animal.getStringWidth(textstring,font);
				if(textWidth > this.columntextwidths[columnStartIndex+column]){
					this.columntextwidths[columnStartIndex+column] = Animal.getStringWidth(textstring,font);
				}
				if(textWidth > this.columntextwidths[columnStartIndex+column+1]){
					this.columntextwidths[columnStartIndex+column+1] = Animal.getStringWidth(textstring,font);
				}
			}
			
		}		
	}
	
	private void enlargeColumnTextWidthsTo(int value) {
		int[] newwidths = new int[value];
		for(int i = 0; i < this.columntextwidths.length;++i){
			newwidths[i] = this.columntextwidths[i];
		}
		for(int i = this.columntextwidths.length; i < value;++i){
			newwidths[i] = 0;		
		}
		this.columntextwidths = newwidths;
	}
	
	private void shrinkColumnTextWidthsTo(int value) {
		int[] newwidths = new int[value];
		for(int i = 0; i < value;++i){
			newwidths[i] = this.columntextwidths[i];
		}
		this.columntextwidths = newwidths;
	}
	/**
	 * Adds a new row as last row with columnCnt columns
	 * @param columnCnt the Count of columns to be added to the new row
	 */
	private void addRow(int columnCnt) {
		addDataRow();		
		Vector<PTText> tmpPTTextRowVector = new Vector<PTText>();
		vTextData.add(tmpPTTextRowVector);
		Vector<PTRectangle> tmpPTRectangleRowVector = new Vector<PTRectangle>();
		vCellRectangles.add(tmpPTRectangleRowVector);
		Vector<Byte> tmpByteRowVector = new Vector<Byte>();
		vCellStatus.add(tmpByteRowVector);
		addColumns(getRowCount()-1,columnCnt);
	}
	
	/**
	 * Adds columnCount columns to row rowIndex
	 * @param rowIndex the index of the row to which the columns chould be added
	 * @param columnCount the count of columns to be added.
	 */
	
	private void addColumns(int rowIndex, int columnCount) {	
		if(columnCount > 0){
			PTText tmpText;
			int oldColumnCount = getColumnCount(rowIndex);
			for(int c =0;c<columnCount ;++c){
				addDataItem(rowIndex);
				tmpText = new PTText();
				tmpText.setText("0");
				tmpText.setFont(this.font);
				vTextData.get(rowIndex).add(tmpText);
				setColumnWidth("0",
						tmpText.getFont(),rowIndex, c+oldColumnCount);
				vCellRectangles.get(rowIndex).add( new PTRectangle());
				vCellStatus.get(rowIndex).add(new Byte((byte)9));
			}
		}
	}


	private void updateLocations() {
		int x = location.x, y = location.y;
		int cellheight=0,cellwidth=0;
		int maxColumnCnt = getMaxColumnCount();
		boolean onGrid = true;
					
		int columnStartIndex = 0;

		for(int r =0;r<getRowCount();++r){
			//calculate rowstart
			if(getRowAlignment() == Alignment.RIGHT){					
				columnStartIndex = maxColumnCnt-getColumnCount(r);
				for(int i = 0; i < columnStartIndex;++i){
					x += this.columntextwidths[i];
				}
				x += columnStartIndex*(margin[3]+margin[1]+1);
			}else if(getRowAlignment() == Alignment.CENTER){
				if((maxColumnCnt-getColumnCount(r))%2 == 0){
					columnStartIndex = (maxColumnCnt-getColumnCount(r))/2;
					onGrid = true;
				}else{
					columnStartIndex = (maxColumnCnt-(getColumnCount(r)+1))/2;
					onGrid = false;
					//Die Zellen der Zeile liegen zwischen zwei "Rasterspalten"
					//Es muss daher die Haelfte der Spalte[columnStartIndex]
					//addiert werden
					x += this.columntextwidths[columnStartIndex]/2 + margin[3];
				}
				for(int i = 0; i < columnStartIndex;++i){
					x += this.columntextwidths[i];
				}
				x += columnStartIndex*(margin[3]+margin[1]+1);

			}
			cellheight = 0;
			for(int c =0;c<getColumnCount(r);++c){
				int temp = vTextData.get(r).get(c).getBoundingBox().height + margin[0];
				if(cellheight<temp){
					cellheight = temp;
				}
			}
			cellheight += margin[2];
			for(int c =0;c<getColumnCount(r);++c){
//				cellheight = vTextData.get(r).get(c).getBoundingBox().height + margin[0];
				int currentcellheight = vTextData.get(r).get(c).getBoundingBox().height + margin[0] + margin[2];
				int tempMoveDown = (cellheight-currentcellheight)/2;
				//callculate cellwidth
				if(this.getRowAlignment() == Alignment.CENTER && !onGrid){
					cellwidth = margin[3] + this.columntextwidths[c+columnStartIndex]/2+
					this.columntextwidths[c+columnStartIndex+1]/2+margin[1];
					if(this.textAlignments[r][c] == Alignment.LEFT)
						vTextData.get(r).get(c).setLocation(new Point(x+margin[3],y+margin[0]+tempMoveDown));
					else if(this.textAlignments[r][c] == Alignment.CENTER)
						vTextData.get(r).get(c).setLocation(new Point(x+margin[3]+
								(this.columntextwidths[c+columnStartIndex]/2+this.columntextwidths[c+columnStartIndex+1]/2
										-Animal.getStringWidth(vTextData.get(r).get(c).getText(),
										vTextData.get(r).get(c).getFont()))/2,y+margin[0]+tempMoveDown));
					else
						vTextData.get(r).get(c).setLocation(new Point(x+margin[3]+
								(this.columntextwidths[c+columnStartIndex]/2+this.columntextwidths[c+columnStartIndex+1]/2
										-Animal.getStringWidth(vTextData.get(r).get(c).getText(),
										vTextData.get(r).get(c).getFont())),y+margin[0]+tempMoveDown));
				}else{
					cellwidth = margin[3] + this.columntextwidths[c+columnStartIndex]+margin[1];
					if(this.textAlignments[r][c] == Alignment.LEFT)
						vTextData.get(r).get(c).setLocation(new Point(x+margin[3],y+margin[0]+tempMoveDown));
					else if(this.textAlignments[r][c] == Alignment.CENTER)
						vTextData.get(r).get(c).setLocation(new Point(x+margin[3]+
								(this.columntextwidths[c+columnStartIndex]-Animal.getStringWidth(
										vTextData.get(r).get(c).getText(),
										vTextData.get(r).get(c).getFont()))/2,y+margin[0]+tempMoveDown));
					else
						vTextData.get(r).get(c).setLocation(new Point(x+margin[3]+
								(this.columntextwidths[c+columnStartIndex]-Animal.getStringWidth(
										vTextData.get(r).get(c).getText(),
										vTextData.get(r).get(c).getFont())),y+margin[0]+tempMoveDown));
				}				
//				cellheight += margin[2];
				this.vCellRectangles.get(r).get(c).setLocation(new Point(x,y));
				this.vCellRectangles.get(r).get(c).setHeight(cellheight);
				this.vCellRectangles.get(r).get(c).setWidth(cellwidth);
				//+1 wegen Ueberlappung der Rechtecke
				x += cellwidth +1;
			}
			x = location.x ;
			//+1 wegen Ueberlappung der Rechtecke
			y += cellheight+1;
		}


		if(style==GridProducer.STYLE_MATRIX){
			calcBrackets();
		}
		
	}
	
	private void calcBrackets(){
	      // we need six elements to build the brackets (four arcs and two lines)
	    brackets = new PTGraphicObject[6];
	    Rectangle rec = getBoundingBox();

	    GridMath.calculateBracketPositionsWitoutMargin(brackets, (int) rec.getWidth(), (int) rec.getHeight(), location, style);
	      for (int part = 0; part < 6; part++) {
		        brackets[part].setColor(textColor);
		        brackets[part].setDepth(depth);
		        brackets[part].setObjectName(getObjectName() + ".bracket[" + part + "]");
//		        brackets[part].setFwArrow(false);
		 }
	}

	private void updateCellProperties() {
		for(int r =0;r<getRowCount();++r){
			for(int c =0;c<getColumnCount(r);++c){
				this.vCellRectangles.get(r).get(c).setFillColor(getCellColor(r, c));
				this.vTextData.get(r).get(c).setColor(getElemColor(r, c));
				this.vTextData.get(r).get(c).setFont(getCellFont(r, c));
				this.vCellRectangles.get(r).get(c).setFilled(isCellHighlighted(r, c));
				this.vCellRectangles.get(r).get(c).setColor(getCellBorderColor(r, c));
				
				this.vCellRectangles.get(r).get(c).setDepth(this.depth);
				this.vTextData.get(r).get(c).setDepth(this.depth-2);
				
				updateColumnWidth(c);
				
//				if(isCellHighlighted(r,c) && !isCellOutlined(r,c))
//					this.vCellRectangles.get(r).get(c).setColor(this.getHighlightColor());
//				else if(!isCellOutlined(r,c))
//					this.vCellRectangles.get(r).get(c).setColor(this.getFillColor());
//				else
//					this.vCellRectangles.get(r).get(c).setColor(this.getColor());
//				this.vCellRectangles.get(r).get(c).setDepth(this.depth);
//				if(isCellHighlighted(r,c))
//					this.vCellRectangles.get(r).get(c).setFillColor(this.getHighlightColor());
//				else
//					this.vCellRectangles.get(r).get(c).setFillColor(this.getFillColor());
//				this.vCellRectangles.get(r).get(c).setFilled(this.filled);
//					if(isCellElementHighlighted(r,c))
//						vTextData.get(r).get(c).setColor(this.getElemHighlightColor());
//					else
//						vTextData.get(r).get(c).setColor(this.textColor);
//				vTextData.get(r).get(c).setDepth(this.depth-2);
			}
		}
		
	}
	
	/**
	 * Updates the PTText elements text values, from the special
	 * data type vector of the subclass
	 */
	protected abstract void updateTextualRepresentation();
	
	private void updateColumnWidth(int c) {
		if(c < columntextwidths.length){
			columntextwidths[c] = 0;
			if(this.getRowAlignment() == Alignment.LEFT){
				for(int r =0; r < this.getRowCount();++r){
					if(c < this.getColumnCount(r))
						setColumnWidth(vTextData.get(r).get(c).getText(),
								vTextData.get(r).get(c).getFont(), r, c);
				}
			}else if(this.getRowAlignment() == Alignment.RIGHT){
				int maxColumnCount = getMaxColumnCount();
				//ueber alle Zeilen iterieren
				for(int r =0; r < this.getRowCount();++r){
					int columnIndexIntoData = this.getColumnCount(r)-maxColumnCount+c;
					//bei den Zeilen, die bei Rechtsausrichtung die entsprechende Spalte enthalten
					if( columnIndexIntoData >=0){
						setColumnWidth(vTextData.get(r).get(columnIndexIntoData).getText(),
								vTextData.get(r).get(columnIndexIntoData).getFont(), r, columnIndexIntoData);
					}
					//update durchfuehren
				}
			}else if(this.getRowAlignment() == Alignment.CENTER){
				int maxColumnCount = getMaxColumnCount();
				int columnIndexIntoData;
				//ueber alle Zeilen iterieren
				for(int r =0; r < this.getRowCount();++r){
					if((maxColumnCount-getColumnCount(r))%2 == 0){
						columnIndexIntoData =c- (maxColumnCount-getColumnCount(r))/2;
					}else{
						columnIndexIntoData =c- (maxColumnCount-getColumnCount(r)-1)/2;
					}
					//bei den Zeilen, die bei Rechtsausrichtung die entsprechende Spalte enthalten
					if( columnIndexIntoData >=0 && columnIndexIntoData < getColumnCount(r)){
						setColumnWidth(vTextData.get(r).get(columnIndexIntoData).getText(),
								vTextData.get(r).get(columnIndexIntoData).getFont(), r, columnIndexIntoData);
					}
				}
			}
		}	
	}
	
	public void discard() {
		for(int r = 0; r < getRowCount();++r){
			for(int c= 0; c < getColumnCount(r);++c){
				vCellRectangles.get(r).get(c).discard();
				vTextData.get(r).get(c).discard();
			}
		}	
		location = null;
		vTextData = null;
		vCellRectangles = null;
		columntextwidths = null;
		margin = null;
		super.discard();
	}
	
	protected boolean indicesAreInRange(int r, int c) {
		return r >=0 && r < this.getRowCount() && c>=0 && c < this.getColumnCount(r);
	}
	
	
	/**
	 * Return a string describing the object
	 * 
	 * @return The String representation of the PTArc
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(getType()+" ");
		if (getObjectName() != null)
			sb.append("\"").append(getObjectName()).append("\" ");
		sb.append("count of rows ").append(getRowCount());
		sb.append("columns per row (r,c) ");
		for(int r = 0; r < this.getRowCount();++r){
			sb.append(" (").append(r).append(",").append(this.getColumnCount(r)).append(") ");
		}
		return sb.toString();
	}
//---------------------------------------------------------------
// IndexableObjectContainer methods
//---------------------------------------------------------------
	
	public String[] getDimensionIdentifiers(String kindOfObject) {
		if(kindOfObject.equals(KIND_OF_OBJECT_CELL)){
			String[] tmpArray = new String[2];
			tmpArray[0] = PRIMARY_DIMENSION_IDENTIFIER;
			tmpArray[1] = SECONDARY_DIMENSION_IDENTIFIER;
			return tmpArray;
		}else if(kindOfObject.equals(KIND_OF_OBJECT_ROW)){
			String[] tmpArray = new String[1];
			tmpArray[0] = PRIMARY_DIMENSION_IDENTIFIER;
			return tmpArray;
		}
		return null;
	}

	public int getDimensionLength(String kindOfObject,
			String dimensionIdentifier, Vector<Integer> indices) {
		if(kindOfObject.equals(KIND_OF_OBJECT_CELL)&& dimensionIdentifier.equals(PRIMARY_DIMENSION_IDENTIFIER))
			return this.getRowCount();
		else if(kindOfObject.equals(KIND_OF_OBJECT_CELL)&&
				dimensionIdentifier.equals(SECONDARY_DIMENSION_IDENTIFIER)&&
				indices.size()>0 && indices.get(0) >= 0 && indices.get(0) < this.getRowCount())
			return this.getColumnCount(indices.get(0));
		else if(kindOfObject.equals(KIND_OF_OBJECT_ROW) && dimensionIdentifier.equals(PRIMARY_DIMENSION_IDENTIFIER)){
			return this.getRowCount();
		}
		return 0;
	}

	public String[] getKindsOfObjects() {
//		String[] tmpArray = new String[1];
//		tmpArray[0] = KIND_OF_OBJECT_CELL;
		String[] tmpArray = new String[2];
		tmpArray[0] = KIND_OF_OBJECT_CELL;
		tmpArray[1] = KIND_OF_OBJECT_ROW;
		return tmpArray;
	}


}
