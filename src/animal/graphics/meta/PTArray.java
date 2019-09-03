/*
 * PTArray.java
 * The abstract class for a PTArray.
 * Current implementations are PTIntArray and PTStringArray.
 *
 * Created on 14. December 2005, 19:54
 *
 * @author Michael Schmitt
 * @version 0.4.2a
 * @date 2006-02-15
 */

package animal.graphics.meta;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTRectangle;
import animal.graphics.PTText;
import animal.main.Animal;
import animal.main.AnimalConfiguration;
import animal.misc.XProperties;

public abstract class PTArray extends PTGraphicObject implements TextContainer {

	// =========
	// CONSTANTS
	// =========
	protected Font				myFont;

	protected Color[]				outlineColor;
	
	protected Color[]				highlightOutlineColor;
	protected Color highlightOutlineColorDefault = Color.BLACK;;

	protected Color[]				backgroundColor;
	protected Color backgroundColorDefault;

	protected Color[]				fontColor;
	protected Color fontColorDefault;

	protected Color[]				highlightColor;
	protected Color highlightColorDefault;

	protected Color[]				elementHighlightColor;
	protected Color elementHighlightColorDefault;

	protected FontMetrics	fm;														// =
																												// Animal.getConcreteFontMetrics
																												// (myFont);

	/**
	 * The actual StringArray to store the values.
	 */
	protected PTText[]		entry;

	/**
	 * The 'background' of the PTArray, i.e. the cell rectangles
	 */
	protected PTRectangle[]	cells;

	/**
	 * Selects if the indices of the array cells are displayed or not
	 * 
	 * Note that this value is used internally and temporarily changed while a
	 * swap animation is in progress. So don't set it directly, but using the
	 * <code>showIndices (boolean state)</code> method instead!
	 */
	protected boolean			showIndices;

	protected boolean[]			statesDEACTIVATED;
	protected boolean[]			statesACTIVATED;
	protected boolean[]			statesHIGHLIGHTED;
	protected boolean[]			statesELEM_HIGHLIGHTED;

	/**
	 * The origin of the array is the upper left corner.
	 */
	protected Point				origin;

	/**
	 * What part of a swap effect has completed.
	 */
	protected byte				swapPercentage		= 0;

	/**
	 * Initial value for the swapped cells
	 */
	int[]									swap							= { -1, -1 };

	protected int						length;

	/**
	 * true = vertical
	 * false = horizontal
	 */
	protected boolean orientation = false;

	// ===============
	// PROPERTY ACCESS
	// ===============

	/**
	 * Directly set the number of an array cell entry. This is required when
	 * restoring a swap animation and may only be used there!
	 * 
	 * @param index
	 *          the index of the array cell to be modified
	 * @param cellID
	 *          the cell ID to be set
	 */
	public void setNum(int index, int cellID) {
		entry[index].setNum(cellID);
	}

	/**
	 * Set the depth of the PTArray.
	 * 
	 * The smallest possible value is 2 because the cell indices and entries must
	 * have an even smaller depth and the minimum value is 0. A value less then 2
	 * to will be set to 2.
	 */
	public void setDepth(int newDepth) {
	  int effectiveDepth = (newDepth < 2) ? 2 : newDepth;
		super.setDepth(effectiveDepth);
		if (entry != null) {
			for (int i = 0; i < entry.length; i++) {
				if (entry[i] != null)
					entry[i].setDepth(effectiveDepth - 2);
				if (cells[i] != null)
					cells[i].setDepth(effectiveDepth);
			}
		}
	}

	/**
	 * Activates or deactivates an array cell.
	 * 
	 * @param i
	 *          the cell whose state shall be changed
	 * @param state
	 *          the state of the cell true if cell is activated, false if it shall
	 *          be greyed out
	 */
	public void setActivated(int i, boolean state) {
//		if ((0 <= i) && (i < states.length)) {
//			if (state) {
//				states[i] = (byte) (states[i] | ACTIVATED);
//				cells[i].setFillColor(isHighlighted(i) ? getHighlightColor()
//						: getBGColor());
//				entry[i].setColor(isElemHighlighted(i) ? getElemHighlightColor()
//						: getFontColor());
//			} else {
//				states[i] = (byte) (states[i] & ~ACTIVATED);
//				cells[i].setFillColor(Color.GRAY);
//				entry[i].setColor(Color.DARK_GRAY);
//			}
//		}
		if ((0 <= i) && (i < cells.length) && (i < entry.length)) {
			if (state) {
				cells[i].setColor(getCellColor(i));
				cells[i].setFillColor(getCellFillColor(i));
				entry[i].setColor(getElemColor(i));
			} else {
				cells[i].setColor(Color.DARK_GRAY);
				cells[i].setFillColor(Color.GRAY);
				entry[i].setColor(Color.DARK_GRAY);
			}
		}
	}

	/**
	 * Is the current array cell activated or deactivated?
	 * 
	 * @param i
	 *          the index of the array cell
	 */
	public boolean isActivated(int i) {
//		if ((0 <= i) && (i < states.length)) {
//			return ((states[i] & ACTIVATED) == ACTIVATED);
//		}
//		return false;
		if ((0 <= i) && (i < statesACTIVATED.length)) {
			return statesACTIVATED[i];
		}
		return false;
	}

	/**
	 * Changes the highlight state of an array cell.
	 * 
	 * @param i
	 *          the cell whose state shall be changed
	 * @param state
	 *          the state of the cell true if cell shall be highlighted, false if
	 *          not
	 */
	public void setHighlighted(int i, boolean state) {
//		if ((0 <= i) && (i < states.length)) {
//			states[i] = (state ? (byte) (states[i] | HIGHLIGHTED)
//					: (byte) (states[i] & ~HIGHLIGHTED));
//			if (isActivated(i)) {
//				cells[i].setFillColor(state ? getHighlightColor() : getBGColor());
//			}
//		}
		if ((0 <= i) && (i < statesHIGHLIGHTED.length)) {
			statesHIGHLIGHTED[i] = state;
			if(isActivated(i)){
				cells[i].setFillColor(getCellFillColor(i));
			}
		}
	}

	/**
	 * Is the current array cell highlighted?
	 * 
	 * @param i
	 *          the index of the array cell
	 */
	public boolean isHighlighted(int i) {
//		if ((0 <= i) && (i < states.length)) {
//			return ((states[i] & HIGHLIGHTED) == HIGHLIGHTED);
//		}
//		return false;
		if ((0 <= i) && (i < statesHIGHLIGHTED.length)) {
			return statesHIGHLIGHTED[i];
		}
		return false;
	}

	/**
	 * Changes the highlight state of an array entry.
	 * 
	 * @param i
	 *          the entry whose state shall be changed
	 * @param state
	 *          the state of the cell entry true if the entry shall be
	 *          highlighted, false if not
	 */
	public void setElemHighlighted(int i, boolean state) {
//		if ((0 <= i) && (i < states.length)) {
//			states[i] = (state ? (byte) (states[i] | ELEM_HIGHLIGHTED)
//					: (byte) (states[i] & ~ELEM_HIGHLIGHTED));
//			if (isActivated(i)) {
//				entry[i].setColor(state ? getElemHighlightColor() : getFontColor());
//			}
//		}
		if ((0 <= i) && (i < statesELEM_HIGHLIGHTED.length)) {
			statesELEM_HIGHLIGHTED[i] = state;
			if (isActivated(i)) {
				entry[i].setColor(getElemColor(i));
			}
		}
	}

	/**
	 * Is the current array entry highlighted?
	 * 
	 * @param i
	 *          the index of the array cell
	 */
	public boolean isElemHighlighted(int i) {
//		if ((0 <= i) && (i < states.length)) {
//			return ((states[i] & ELEM_HIGHLIGHTED) == ELEM_HIGHLIGHTED);
//		}
//		return false;
		if ((0 <= i) && (i < statesELEM_HIGHLIGHTED.length)) {
			return statesELEM_HIGHLIGHTED[i];
		}
		return false;
	}

	/**
	 * Overwrite method of super class for completeness.
	 * 
	 * Doesn't call super method to avoid double execution of translate method.
	 */
	public void setLocation(Point p) {
		setOrigin(p);
	}

	/**
	 * Set the upper left corner of the array.
	 * 
	 * @param p
	 *          the point to which to set the origin
	 */
	public void setOrigin(Point p) {
		setOrigin(p.x, p.y);
	}

	/**
	 * Set the upper left corner of the array.
	 * 
	 * @param xpos
	 *          the x-coordinate of the origin
	 * @param ypos
	 *          the y-coordinate of the origin
	 */
	public void setOrigin(int xpos, int ypos) {
		translate(xpos - origin.x, ypos - origin.y);
	}

	/**
	 * Store the default size to the properties file.
	 * 
	 * @param n
	 *          the default array size
	 */
	public void setSize(int n) {
		// getProperties().put(mapKey(ARRAY_SIZE), n);
		init(n);
	}

	/**
	 * Retrieve the default size from the properties.
	 */
	public int getSize() {
		if (entry != null)
			return entry.length;
		return 0;
		// return getProperties().getIntProperty(mapKey(ARRAY_SIZE), 1);
	}

	/**
	 * Set color of the cell background.
	 * 
	 * @param c
	 *          a color value
	 */
	public void setBGColor(Color c) {
		// getProperties().put(mapKey(BG_COLOR), c);
		if(c!=null){
			backgroundColorDefault = c;
			for (int i = 0; i < cells.length; i++) {
				if (isActivated(i) && cells[i] != null) {
					backgroundColor[i] = c;
					cells[i].setFillColor(getCellFillColor(i));
				}
			}
		}
	}
	public void setBGColor(int index, Color c) {
		if (c != null && index>=0 && index<length && isActivated(index) && cells[index] != null){
			backgroundColor[index] = c;
			cells[index].setFillColor(getCellFillColor(index));
		}
	}

	/**
	 * Retrieve the background color of the array cells.
	 */
	public Color getBGColor() {
		return backgroundColorDefault;
	}
	
	public Color getBGColor(int i) {
		return backgroundColor[i];
	}

	/**
	 * Set the font of the cell text.
	 * 
	 * @param f
	 *          the font to set
	 */
	public void setFont(Font f) {
		// getProperties().put(reverseMapKey(FONT), f);
		myFont = f;
		fm = Animal.getConcreteFontMetrics(f);
		for (int i = 0; i < entry.length; i++) {
			entry[i].setFont(f);
		}
		updateAllCellsWithCurrentStates();
	}

	/**
	 * Retrieve the currently used font
	 */
	public Font getFont() {
		if (myFont == null)
			myFont = new Font("SansSerif", Font.PLAIN, 12);
		return myFont;
		// return getProperties().getFontProperty(mapKey(FONT), myFont);
	}

	public FontMetrics getFontMetrics() {
		fm = Animal.getConcreteFontMetrics(getFont());
		return fm;
	}

	/**
	 * Set the text to the given color.
	 * 
	 * @param c
	 *          the requested color
	 */
	public void setFontColor(Color c) {
		if (c != null){
			fontColorDefault = c;
			for (int i = 0; i < entry.length; i++) {
				if (isActivated(i) && entry[i]!=null) {
					fontColor[i] = c;
					entry[i].setColor(getElemColor(i));
				}
			}
		}
	}
	
	public void setFontColor(int index, Color c) {
		if (c != null && index>=0 && index<length && isActivated(index) && cells[index] != null){
			fontColor[index] = c;
			entry[index].setColor(getElemColor(index));
		}
	}

	/**
	 * Retrieve the color of the text.
	 */
	public Color getFontColor() {
		if (fontColorDefault == null)
			fontColorDefault = Color.BLACK;
		return fontColorDefault;
	}
	
	public Color getFontColor(int i) {
		return fontColor[i];
	}

	/**
	 * Set the color of the highlighted cells.
	 * 
	 * @param c
	 *          the color that will be applied to highlighted cells
	 */
	public void setHighlightColor(Color c) {
		if (c != null){
			highlightColorDefault = c;
			for (int i = 0; i < cells.length; i++) {
				if (isActivated(i) && cells[i] != null) {
					highlightColor[i] = c;
					cells[i].setFillColor(getCellFillColor(i));
				}
			}
		}
	}
	
	public void setHighlightColorIndex(int index, Color c) {
		if (c != null && index>=0 && index<length && isActivated(index) && cells[index] != null) {
			highlightColor[index] = c;
			cells[index].setFillColor(getCellFillColor(index));
		}
	}

	/**
	 * Get the color of highlighted cells.
	 */
	public Color getHighlightColor() {
		if (highlightColorDefault == null)
			highlightColorDefault = Color.YELLOW;
		return highlightColorDefault;
	}
	
	public Color getHighlightColor(int i) {
		return highlightColor[i];
	}

	/**
	 * Set the color of the highlighted cell entries.
	 * 
	 * @param c
	 *          the color that will be applied to highlighted entries
	 */
	public void setElemHighlightColor(Color c) {
		if(c!=null){
			elementHighlightColorDefault = c;
			for (int i = 0; i < entry.length; i++) {
				elementHighlightColor[i] = c;
				entry[i].setColor(getElemColor(i));
			}
		}
	}
	
	public void setElemHighlightColor(int index, Color c) {
		if (c != null && index>=0 && index<length && isActivated(index) && entry[index] != null) {
			elementHighlightColor[index] = c;
			entry[index].setColor(getElemColor(index));
		}
	}

	/**
	 * Get the color of highlighted cell entries.
	 */
	public Color getElemHighlightColor() {
		return elementHighlightColorDefault;
	}
	
	public Color getElemHighlightColor(int i) {
		return elementHighlightColor[i];
	}

	/**
	 * Set the outline color to the given one.
	 * 
	 * @param c
	 *          the wanted color
	 */
	public void setOutlineColor(Color c) {
		if(c!=null){
			color = c;
			for (int i = 0; i < cells.length; i++) {
			  if (cells[i] != null){
				  outlineColor[i] = c;
				  cells[i].setColor(getCellColor(i));
			  }
			}
		}
	}
	
	public void setOutlineColor(int index, Color c) {
		if(c != null && index>=0 && index<length && isActivated(index) && cells[index] != null){
			  outlineColor[index] = c;
			  cells[index].setColor(getCellColor(index));
		}
	}

	/**
	 * Return the color of the cell outlines.
	 */
	public Color getOutlineColor() {
		if(color==null){
			color = Color.BLACK;
		}
		return color;
	}
	
	public Color getOutlineColor(int i) {
		return outlineColor[i];
	}
	
	
	public void setHighlightOutlineColor(Color c) {
		if(c!=null){
			highlightOutlineColorDefault = c;
			for (int i = 0; i < cells.length; i++) {
			  if (cells[i] != null){
				  highlightOutlineColor[i] = c;
				  cells[i].setColor(getCellColor(i));
			  }
			}
		}
	}
	
	public void setHighlightOutlineColor(int index, Color c) {
		if(c != null && index>=0 && index<length && isActivated(index) && cells[index] != null){
			  highlightOutlineColor[index] = c;
			  cells[index].setColor(getCellColor(index));
		}
	}

	public Color getHighlightOutlineColor() {
		return highlightOutlineColorDefault;
	}
	
	public Color getHighlightOutlineColor(int i) {
		return highlightOutlineColor[i];
	}
	
	public Color getCellFillColor(int i){
		return isHighlighted(i) ? getHighlightColor(i)
				: getBGColor(i);
	}
	
	public Color getCellColor(int i){
		return isHighlighted(i) ? getHighlightOutlineColor(i)
				: getOutlineColor(i);
	}
	
	public Color getElemColor(int i){
		return isElemHighlighted(i) ? getElemHighlightColor(i)
				: getFontColor(i);
	}

	/**
	 * Enter a new content into an array cell without resizing the cell but
	 * adapting the internal stored cell width. This method is provided for the
	 * Put-Animation only!!!
	 * 
	 * @param index
	 *          the index of the cell to be replaced
	 * @param content
	 *          the new content that is to be entered
	 */
	public int put(int index, PTText content) {
		showIndices = indicesShown();
		if ((index >= 0) && (index < entry.length)) {
			// !!! uncomment this on final version!!!
			 content.setFont (myFont);
			 content.setColor (getElemColor(index));
			 content.setLocation(entry[index].getLocation());
			 entry[index] = content;
			 updateAllCellsWithCurrentStates();
			 return 0;
		}
		return -1;
	}
	
	/**
	 * Enter a new content into an array cell without resizing the cell but
	 * adapting the internal stored cell width.
	 * 
	 * @param index
	 *          the index of the cell to be replaced the content
	 * @param content
	 *          the new content that is to be entered
	 */
	public int put(int index, String content) {
		showIndices = indicesShown();
		if ((index >= 0) && (index < entry.length)) {
			entry[index].setText(content);
			updateAllCellsWithCurrentStates();
			return 0;
		}
		return -1;
	}

	/**
	 * Retrieve the content of an array cell.
	 * 
	 * @param index
	 *          the cell which content is requested.
	 */
	// public abstract getValue (int index) {
	// }
	/**
	 * Get the cell content at the specified index
	 * 
	 * @param index
	 *          the index of the requested cell
	 * @return the cell content as a PTGraphicObject
	 */
	public PTText getEntry(int index) {
		if ((index >= 0) && (index < entry.length) && entry[index] != null) {
			entry[index].getNum(true);
			return entry[index];
		}
		return null;
	}

	public String getStringValueAt(int index) {
		PTText textEntry = getEntry(index);
		if (textEntry == null)
			return "";
		return textEntry.getText();
	}

	public abstract void enterStringValueAt(int index, String text);

	/**
	 * Get the cell rectangle at the specified index
	 * 
	 * @param index
	 *          the index of the requested cell
	 */
	public PTRectangle getCell(int index) {
		if ((index >= 0) && (index < entry.length)) {
			cells[index].getNum(true);
			return cells[index];
		}
		return null;
	}

	/**
	 * The bounding box for the whole PTArray, specified by the the top left
	 * point, width and height.
	 */
	public Rectangle getBoundingBox() {
		Rectangle bigBoundingBox = new Rectangle(origin.x, origin.y, 0, 0);
		for (int i = 0; i < cells.length; i++) {
			bigBoundingBox = bigBoundingBox.union(getBoundingBox(i));
		}
		return bigBoundingBox;
	}

	/**
	 * The bounding box for the specified cell.
	 * 
	 * @param i
	 *          the index of the i-th cell
	 */
	public Rectangle getBoundingBox(int i) {
		if ((i >= entry.length) || (i < 0)) {
			return new Rectangle(-1, -1, -1, -1);
		} else {
			// do not change this using cellWidth because cell width is calculated
			// depending on the chosen font of the actual cell
			// which leads to a wrong value when calculating the offset in
			// setFont and enterValue
			return cells[i].getBoundingBox();
		}
	}

	/**
	 * An array of bounding boxes for all the cells of the array, each described
	 * as a rectangle.
	 */
	public Rectangle[] getBoundingBoxes() {
		Rectangle[] bBoxes = new Rectangle[cells.length];
		for (int i = 0; i < cells.length; i++) {
			bBoxes[i] = getBoundingBox(i);
		}
		return bBoxes;
	}

	public Point getArrowPointRight(int i) {
		if ((i >= 0) && (i < entry.length)) {
			Rectangle rec = getBoundingBox(i);
			return new Point(rec.x+rec.width,rec.y+(rec.height/2));
		} else if (i == -1) {
			Rectangle rec = getBoundingBox(cells.length-1);
			return new Point(rec.x+rec.width,rec.y+rec.height+20);
		} else {
			return new Point(-1, -1);
		}
	}
	
	public Point getArrowPointTop(int i) {
		if ((i >= 0) && (i < entry.length)) {
			Rectangle rec = getBoundingBox(i);
			return new Point(rec.x+(rec.width/2),rec.y);
		} else if (i == -1) {
			Rectangle rec = getBoundingBox(cells.length-1);
			return new Point(rec.x+rec.width+20,rec.y);
		}  else {
			return new Point(-1, -1);
		}
	}

	/**
	 * Get the the i-th array cell as the point where
	 * an arrow has to point for marking that cell.
	 * 
	 * @param i
	 *          the i-th cell of the array
	 */
	public Point getArrowPoint(int i) {
		if(orientation){
			return getArrowPointRight(i);
		}else{
			return getArrowPointTop(i);
		}
	}
	
	public Point getIndexPoint(int i) {
		int stringWidth = fm.stringWidth(String.valueOf(i));
		Rectangle rec = getBoundingBox(i);
		if(orientation){
			return new Point(rec.x-stringWidth-5,rec.y+(rec.height/2)-(fm.getHeight()/2));
		}else{
			return new Point(rec.x+(rec.width/2)-(stringWidth/2),rec.y+rec.height+5);
		}
	}

	/**
	 * Translate this PTArray by the given values.
	 * 
	 * @param dx
	 *          the horizontal translation in pixels
	 * @param dy
	 *          the vertical translation in pixels
	 */
	public void translate(int dx, int dy) {
		origin.x += dx;
		origin.y += dy;
		
		updateAllCellsWithCurrentStates();
	}

	/**
	 * Get the portion of the swap effect that's already done
	 */
	public byte getSwapPercentage() {
		return swapPercentage;
	}

	/**
	 * Define how much of a swap effect is already finished
	 * 
	 * @param p
	 *          the part to set (an <code>int</code> value bounded from 0 to 100)
	 */
	public void setSwapPercentage(int p) {
		setSwapPercentage((byte) p);
		// swapPercentage = (byte) Math.max (0, Math.min (100, p));
	}

	/**
	 * Define how much of a swap effect is already finished
	 * 
	 * @param p
	 *          the part to set (as a <code>float</code> value if p lies between 0
	 *          and 1 it is converted to a byte value between 0 and 100 or bounded
	 *          by these values otherwise
	 */
	public void setSwapPercentage(float p) {
		if ((p >= 0f) && (p <= 1)) {
			setSwapPercentage((byte) (100 * p));
		} else {
			setSwapPercentage((byte) p);
		}
		// swapPercentage = ((p >= 0f) && (p <= 1f)) ? (byte) (100*p) : (byte)
		// Math.max (0f, Math.min (100f, p));
	}

	/**
	 * Define how much of a swap effect is already finished. If necessary, restore
	 * original configuration of the PTStringArray.
	 * 
	 * @param p
	 *          the part to set (a <code>byte</code> value bounded from 0 to 100)
	 */
	public void setSwapPercentage(byte p) {
		swapPercentage = (byte) Math.max(0, Math.min(100, p));
	}

	/**
	 * Define, if two cells shall be swapped.
	 * 
	 * @param a
	 *          the first cell index
	 * @param b
	 *          the second cell index
	 * 
	 *          If there is nothing to do, use <code>setSwapCells (-1, -1)</code>.
	 */
	public void setSwapCells(int a, int b) {
	  int theA = a, theB = b;
		if (b != a) {
			if (b < a) {
				int x = a;
				theA = b;
				theB = x;
			}
			if ((theA < 0) || (theB >= entry.length)) {
				swap[0] = -1;
				swap[1] = -1;
			} else {
				swap[0] = theA;
				swap[1] = theB;
			}
		} else {
			swap[0] = -1;
			swap[1] = -1;
		}
	}

	public void setSwapCells(int[] affectedCells) {
		if (affectedCells.length == 2)
			setSwapCells(affectedCells[0], affectedCells[1]);
	}

	// ================
	// INTERNAL METHODS
	// ================

	/**
	 * Executes a swap of cell contents. Especially used by the StringArrayHandler
	 * when two cells are swapped.
	 */
	public void doSwap(int first, int second) {
		if(first==second){
			return;
		}else if(first>second){
			doSwap(second,first);
			return;
		}else if ((first >= 0) && (second < entry.length)) {
			String tmp = entry[first].getText();
			entry[first].setText(entry[second].getText());
			entry[second].setText(tmp);
			updateAllCellsWithCurrentStates();
		}
		showIndices = indicesShown();
	}
	
	/**
	 * true = vertical
	 * false = horizontal
	 * @param vertical
	 */
	public void setOrientation(boolean vertical){
		this.orientation = vertical;
	}

	/**
	 * true = vertical
	 * false = horizontal
	 */
	public boolean getOrientation(){
		return this.orientation;
	}

	/**
	 * Update the default properties for this object
	 * 
	 * @param defaultProperties
	 *          the properties to be updated
	 */
	public void updateDefaults(XProperties defaultProperties) {
		super.updateDefaults(defaultProperties);
		defaultProperties.put(getType() + ".arraySize", getSize());
		defaultProperties.put(getType() + ".bgColor", getBGColor());
		defaultProperties.put(getType() + ".elemHighlightColor",
				getElemHighlightColor());
		Font f = getFont();
		if (f != null) {
			defaultProperties.put(getType() + ".font", f);
			defaultProperties.put(getType() + ".fontColor", getFontColor());
			defaultProperties.put(getType() + ".fontName", f.getFamily());
			defaultProperties.put(getType() + ".fontSize", f.getSize());
			defaultProperties.put(getType() + ".fontStyle", f.getStyle());
		}
		defaultProperties.put(getType() + ".highlightColor", getHighlightColor());
		defaultProperties.put(getType() + ".location", origin);
		defaultProperties.put(getType() + ".outlineColor", getOutlineColor());
		defaultProperties.put(getType() + ".showIndices", indicesShown());
		// rest has to be done in subclasses!
	}

	/**
	 * Offers cloning support by cloning or duplicating the shared attributes
	 * 
	 * @param targetShape
	 *          the shape into which the values are to be copied. Note the
	 *          direction -- it is
	 *          "currentObject.cloneCommonFeaturesInto(newObject)", not vice
	 *          versa!
	 */
	protected void cloneCommonFeaturesInto(PTArray targetShape) {
		// clone features from PTGraphicsObject: color, depth, num, objectName
		super.cloneCommonFeaturesInto(targetShape);

		// clone anything else that is specific to this type
		// and its potential subtypes
		targetShape.backgroundColorDefault = new Color(backgroundColorDefault.getRed(), 
				backgroundColorDefault.getGreen(), backgroundColorDefault.getBlue());
		targetShape.highlightOutlineColorDefault = new Color(highlightOutlineColorDefault.getRed(), 
				highlightOutlineColorDefault.getGreen(), highlightOutlineColorDefault.getBlue());
		targetShape.color = new Color(color.getRed(), 
				color.getGreen(), color.getBlue());
		targetShape.cells = new PTRectangle[cells.length];
		targetShape.elementHighlightColorDefault = new Color(elementHighlightColorDefault.getRed(), 
				elementHighlightColorDefault.getGreen(), elementHighlightColorDefault.getBlue());
		targetShape.entry = new PTText[entry.length];
		targetShape.fm = Animal.getConcreteFontMetrics(fm.getFont());
		targetShape.fontColorDefault = new Color(fontColorDefault.getRed(), 
				fontColorDefault.getGreen(), fontColorDefault.getBlue());
		targetShape.highlightColorDefault = new Color(highlightColorDefault.getRed(), 
				highlightColorDefault.getGreen(), highlightColorDefault.getBlue());
		targetShape.length = length;
		targetShape.myFont = new Font(myFont.getName(), myFont.getStyle(), myFont
				.getSize());
		targetShape.origin = (Point) origin.clone();
		targetShape.showIndices = showIndices;
//		targetShape.states = new byte[states.length];
		targetShape.statesDEACTIVATED = new boolean[statesDEACTIVATED.length];
		targetShape.statesACTIVATED = new boolean[statesACTIVATED.length];
		targetShape.statesHIGHLIGHTED = new boolean[statesHIGHLIGHTED.length];
		targetShape.statesELEM_HIGHLIGHTED = new boolean[statesELEM_HIGHLIGHTED.length];

		targetShape.outlineColor = new Color[outlineColor.length];
		targetShape.highlightOutlineColor = new Color[highlightOutlineColor.length];
		targetShape.backgroundColor = new Color[backgroundColor.length];
		targetShape.fontColor = new Color[fontColor.length];
		targetShape.highlightColor = new Color[highlightColor.length];
		targetShape.elementHighlightColor = new Color[elementHighlightColor.length];
		// targetShape.setDepth(getDepth());
		// targetShape.setColor(new Color(color.getRed(), color.getGreen(),
		// color.getBlue()));
		for (int i = 0; i < entry.length; i++) {
			targetShape.entry[i] = (PTText) entry[i].clone();
			targetShape.cells[i] = (PTRectangle) cells[i].clone();
			targetShape.statesDEACTIVATED[i] = statesDEACTIVATED[i];
			targetShape.statesACTIVATED[i] = statesACTIVATED[i];
			targetShape.statesHIGHLIGHTED[i] = statesHIGHLIGHTED[i];
			targetShape.statesELEM_HIGHLIGHTED[i] = statesELEM_HIGHLIGHTED[i];

			targetShape.outlineColor[i] = outlineColor[i];
			targetShape.highlightOutlineColor[i] = highlightOutlineColor[i];
			targetShape.backgroundColor[i] = backgroundColor[i];
			targetShape.fontColor[i] = fontColor[i];
			targetShape.highlightColor[i] = highlightColor[i];
			targetShape.elementHighlightColor[i] = elementHighlightColor[i];
		}
		targetShape.orientation = orientation;
	}

	/**
	 * Converts the PTArray into a String
	 */
	public String toString() {
	  StringBuilder sb = new StringBuilder(768);
	  sb.append(getType());
//		String toString = new String();
//		toString = getType();
		if (getObjectName() != null)
			sb.append(" '").append(getObjectName()).append("'");
		sb.append(" with ").append(entry.length).append(" entries (");
		for (int c = 0; c < entry.length; c++) {
			sb.append(entry[c].getText());
			if (c == entry.length - 1) {
				sb.append(")");
			} else {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

	/**
	 * Set visibility state of the cell indices
	 * 
	 * @param state
	 *          cell indices shall be shown or not
	 */
	public void showIndices(boolean state) {
		showIndices = state;
		// getProperties().put(mapKey(SHOW_INDICES), state);
	}

	/**
	 * Are the cell indices shown?
	 */
	public boolean indicesShown() {
		return showIndices;
//		 return getProperties().getBoolProperty(mapKey(AnimationPropertiesKeys.SHOW_INDICES), true);
	}

	// ================
	// INTERNAL METHODS
	// ================

	protected abstract PTText createInternalValue(int cellPosition,
			Font targetFont);

	/**
	 * Initialize the default settings
	 */
	protected void init(int nrEntries) {
	  if (origin == null)
	    origin = new Point(10, 10);
//	  System.err.println("new init! size was " +length +", now shall be "+nrEntries);
    int copySize = Math.min(length, nrEntries);
	  boolean mustWork = (copySize != 0 && nrEntries != length);
	  
	  // store copy of previous versions
	  PTText[] oldTexts = entry;
    PTRectangle[] oldCells = cells;
//    byte[] oldStates = states;
    boolean[] oldStatesDEACTIVATED = statesDEACTIVATED;
    boolean[] oldStatesACTIVATED = statesACTIVATED;
    boolean[] oldStatesHIGHLIGHTED = statesHIGHLIGHTED;
    boolean[] oldStatesELEM_HIGHLIGHTED = statesELEM_HIGHLIGHTED;

    Color[] oldoutlineColor = outlineColor;
    Color[] oldhighlightOutlineColor = highlightOutlineColor;
    Color[] oldbackgroundColor = backgroundColor;
    Color[] oldfontColor = fontColor;
    Color[] oldhighlightColor = highlightColor;
    Color[] oldelementHighlightColor = elementHighlightColor;
    
	  // re-initialize arrays
	  entry = new PTText[nrEntries];
	  cells = new PTRectangle[nrEntries];
	  statesDEACTIVATED = new boolean[nrEntries];
	  statesACTIVATED = new boolean[nrEntries];
	  statesHIGHLIGHTED = new boolean[nrEntries];
	  statesELEM_HIGHLIGHTED = new boolean[nrEntries];

	  outlineColor = new Color[nrEntries];
	  highlightOutlineColor = new Color[nrEntries];
	  backgroundColor = new Color[nrEntries];
	  fontColor = new Color[nrEntries];
	  highlightColor = new Color[nrEntries];
	  elementHighlightColor = new Color[nrEntries];
	  if (mustWork) {
		  // copy entries
		  System.arraycopy(oldTexts, 0, entry, 0, copySize);
	      System.arraycopy(oldCells, 0, cells, 0, copySize);
	      System.arraycopy(oldStatesDEACTIVATED, 0, statesDEACTIVATED, 0, copySize);
	      System.arraycopy(oldStatesACTIVATED, 0, statesACTIVATED, 0, copySize);
	      System.arraycopy(oldStatesHIGHLIGHTED, 0, statesHIGHLIGHTED, 0, copySize);
	      System.arraycopy(oldStatesELEM_HIGHLIGHTED, 0, statesELEM_HIGHLIGHTED, 0, copySize);

	      System.arraycopy(oldoutlineColor, 0, outlineColor, 0, copySize);
	      System.arraycopy(oldhighlightOutlineColor, 0, highlightOutlineColor, 0, copySize);
	      System.arraycopy(oldbackgroundColor, 0, backgroundColor, 0, copySize);
	      System.arraycopy(oldfontColor, 0, fontColor, 0, copySize);
	      System.arraycopy(oldhighlightColor, 0, highlightColor, 0, copySize);
	      System.arraycopy(oldelementHighlightColor, 0, elementHighlightColor, 0, copySize);
	  }
	  length = nrEntries;
	  setDepth((getDepth() < 2) ? 2 : getDepth());
	  getFont();
	  copySize = 0; // TODO: Eventuell spaeter anpassen, aber aktuell wird das array ja nie vergr�ssert
	  for (int index = copySize; index < length; index++) {
//	    System.err.println("update elements for position "+index);
	    createInternalBoxAndValue(index);
	  }
	  updateAllCellsWithCurrentStates();
	}

	private void createInternalBoxAndValue(int index) {
		// create new text
		PTText helperText;
		helperText = createInternalValue(index, myFont);
		helperText.setPosition(new Point(-1,-1));
		helperText.setColor(getElemColor(index));
		helperText.setDepth(getDepth() - 2);
		helperText.setObjectSelectable(false);
		entry[index] = helperText;

		// create new box
		PTRectangle helperRect;
		helperRect = new PTRectangle(-1,-1,-1,-1);
		helperRect.setDepth(getDepth());
		helperRect.setFilled(true);
		helperRect.setColor(getCellColor(index));
		helperRect.setFillColor(getCellFillColor(index));
		helperRect.setObjectSelectable(false);
		cells[index] = helperRect;
		//states[index] = ACTIVATED;
		statesDEACTIVATED[index] = false;
		statesACTIVATED[index] = true;
		statesHIGHLIGHTED[index] = false;
		statesELEM_HIGHLIGHTED[index] = false;
	}

	public void initializeWithDefaults(String primitiveName) {
		AnimalConfiguration config = AnimalConfiguration.getDefaultConfiguration();
		backgroundColorDefault = config.getDefaultColor(primitiveName, "bgColor",
				Color.WHITE);
		// int size = config.getDefaultIntValue(primitiveName, "arraySize", 10);
		// if (entry == null) {
		// cells = new PTPolygon[size];
		// entry = new PTText[size];
		// entryPos = new int[size];
		// swap = new int[size];
		// states = new byte[size];
		// }
		elementHighlightColorDefault = config.getDefaultColor(primitiveName,
				"elemHighlightColor", Color.BLUE);
		fontColorDefault = config.getDefaultColor(primitiveName, "fontColor", Color.BLACK);
		highlightColorDefault = config.getDefaultColor(primitiveName, "highlightColor",
				Color.RED);
		String fontName = config.getProperty(primitiveName + ".fontName",
				"SansSerif");
		int fontSize = config.getDefaultIntValue(primitiveName, "fontSize", 14);
		int fontStyle = config.getDefaultIntValue(primitiveName, "fontStyle",
				Font.PLAIN);
		myFont = new Font(fontName, fontStyle, fontSize);
		origin = new Point(10, 10);
		showIndices = config.getDefaultBooleanValue(primitiveName, "showIndices",
				false);
		fm = getFontMetrics();
	}

	/**
	 * Get the width of the array cell at the given index as it should be
	 * depending on the cell content and the currently chosen font for that cell.
	 * Adds an additional space of 10 pixels to the width of the text that is
	 * contained in the visited cell.
	 * 
	 * @param index
	 *          the index of the cell
	 */
	protected int cellWidth(int index) {
		if (entry[index] != null) {
			return entry[index].getBoundingBox().width + 10;
		}
		return 10;
	}
	
	protected void updateAllCellsWithCurrentStates(){
		if(orientation){//vertical
			int maxCellWidth = 0;
			for (int i = 0; i < length; i++) {
				int cellWidth = cellWidth(i);
				if(maxCellWidth < cellWidth){
					maxCellWidth = cellWidth;
				}
			}
			for (int index = 0; index < length; index++) {
				int prevSize = (index > 0) ? cells[index - 1].getNodeAsPoint(3).y : origin.y;
				entry[index].setPosition(new Point(origin.x + 5, prevSize + fm.getAscent() + 5));
				cells[index].setLocation(new Point(origin.x, prevSize));
				cells[index].setWidth(maxCellWidth);
				cells[index].setHeight(fm.getAscent() + fm.getDescent() + 10);
			}
		}else{//horizontal
			for (int index = 0; index < length; index++) {
				int prevSize = (index > 0) ? cells[index - 1].getNodeAsPoint(1).x : origin.x;
				entry[index].setPosition(new Point(prevSize + 5, origin.y + fm.getAscent() + 5));
				cells[index].setLocation(new Point(prevSize, origin.y));
				cells[index].setWidth(cellWidth(index));
				cells[index].setHeight(fm.getAscent() + fm.getDescent() + 10);
			}
		}
	}

	// ================
	// GRAPHICAL OUTPUT
	// ================

	/**
	 * The paint method in which the cells an the contents are drawn on the
	 * screen.
	 */
	public void paint(Graphics g) {
		g.setFont(myFont);
		// int height = fm.getAscent();

		// restore correct cell and entry color
		// if one of the highlight colors was changed in the editor
		// but the editor window was not closed properly
		if (!getEditor().isVisible()) {
			setActivated(0, isActivated(0));
		}

		float[] hsb = Color.RGBtoHSB(getBGColor().getRed(),
				getBGColor().getGreen(), getBGColor().getBlue(), null);
		for (int index = 0; index < entry.length; index++) {
			cells[index].paint(g);
			if (showIndices) {
				PTText idx = new PTText(String.valueOf(index), myFont);
				idx.setLocation(getIndexPoint(index));
				idx.setDepth(getDepth() - 1);
				if (hsb[2] > 0.5) {
					if (hsb[1] > 0.5) {
						idx.setColor(Color.getHSBColor(hsb[0], hsb[1] - 0.1f, hsb[2])
								.darker());
					} else {
						idx.setColor(Color.getHSBColor(hsb[0], hsb[1] + 0.1f, hsb[2])
								.darker());
					}
					// idx.setColor (Color.getHSBColor (hsb[0], hsb[1], (float) (hsb[2] -
					// 0.15)));
				} else {
					if (hsb[1] > 0.5) {
						idx.setColor(Color.getHSBColor(hsb[0], hsb[1] - 0.1f, hsb[2])
								.brighter());
					} else {
						idx.setColor(Color.getHSBColor(hsb[0], hsb[1] + 0.1f, hsb[2])
								.brighter());
					}
					// idx.setColor (Color.getHSBColor (hsb[0], hsb[1], (float) (hsb[2] +
					// 0.15)));
				}
				idx.paint(g);
			}
			entry[index].paint(g);
		}
	}
	
	
	
	
	
	
	
	


//	/**
//	 * Determine the number of pixels that the cells have to be moved which are
//	 * lying between those that are swapped. The value is necessary because of the
//	 * width of the cells differing if they contain different entries. It changes
//	 * dynamically depending on the part of a swap that is already done.
//	 * 
//	 * Positive values imply shifting the involved cells to the right, negative
//	 * values lead to a left shift.
//	 * 
//	 * @param index
//	 *          the cell for which to calculate the offset
//	 * 
//	 * @return the number of pixels that have to be added or subtracted
//	 */
//	protected int swapOffset(int index) {
//		if ((swap[0] < 0) || (swap[1] >= entry.length) || (index <= swap[0])
//				|| (index > swap[1]))
//			return 0;
//
//		return (cellWidth(swap[1]) - cellWidth(swap[0])) * getSwapPercentage()
//				/ 100;
//	}
//
//	/**
//	 * Determine the number of pixels that the right edges of the swapped cells
//	 * have to be moved while the swap effect is executed. The value changes
//	 * dependent on how far the animation has reached.
//	 * 
//	 * Positive values imply additional move to the right, where negative values
//	 * mean reducing the cell width.
//	 * 
//	 * @param index
//	 *          the cell for which to calculate the additional width
//	 * 
//	 * @return the number of pixels the edge has to be moved
//	 */
//	protected int widthOffset(int index) {
//		if (index == swap[0]) {
//			return (cellWidth(swap[1]) - cellWidth(swap[0])) * getSwapPercentage()
//					/ 100;
//		} else if (index == swap[1]) {
//			return (cellWidth(swap[0]) - cellWidth(swap[1])) * getSwapPercentage()
//					/ 100;
//		} else
//			return 0;
//	}
//
//	/**
//	 * Get the point, where the entry of the second cell will be drawn on top of
//	 * the array, while two cell entries are swapped.
//	 * 
//	 * @return the drawing point
//	 */
//	protected Point upperSwapPoint() {
//		PTPolyline pl = new PTPolyline();
//		Rectangle a = getBoundingBox(swap[1]);
//		Rectangle b = getBoundingBox(swap[0]);
//		pl.addNode(new PTPoint(a.x + 5, a.y + fm.getAscent() + 5));
//		pl.addNode(new PTPoint(a.x + 5, a.y - fm.getDescent() - 5));
//		pl.addNode(new PTPoint(b.x + 5, b.y - fm.getDescent() - 5));
//		pl.addNode(new PTPoint(b.x + 5, b.y + fm.getAscent() + 5));
//		return pl.getPointAtLength(pl.getLength() * getSwapPercentage() / 100);
//	}
//
//	/**
//	 * Get the point, where the entry of the first cell will be drawn below the
//	 * bottom of the array, while two cell entries are swapped.
//	 * 
//	 * @return the drawing point
//	 */
//	protected Point lowerSwapPoint() {
//		PTPolyline pl = new PTPolyline();
//		Rectangle a = getBoundingBox(swap[0]);
//		Rectangle b = getBoundingBox(swap[1]);
//		pl.addNode(new PTPoint(a.x + 5, a.y + fm.getAscent() + 5));
//		pl.addNode(new PTPoint(a.x + 5, a.y + a.height + fm.getAscent() + 5));
//		// This is different from the calculation of the upper move
//		// to compensate the movement of the latter array cell
//		// that is swapped
//		pl.addNode(new PTPoint(b.x + b.width - a.width + 5, b.y + b.height
//				+ fm.getAscent() + 5));
//		pl.addNode(new PTPoint(b.x + b.width - a.width + 5, b.y + fm.getAscent()
//				+ 5));
//		return pl.getPointAtLength(pl.getLength() * getSwapPercentage() / 100);
//	}

//	/**
//	 * Translate parts of the array. Used by the swap animation to move the text
//	 * of the swapped cells or the cells that are not swapped.
//	 * 
//	 * @param index
//	 *          [] the indices of the requested cells or cell entries
//	 * @param dx
//	 *          the x-value of the translation
//	 * @param dy
//	 *          the y-value of the translation
//	 * @param text
//	 *          shall the cell content be moved
//	 * @param cell
//	 *          shall the cell outline be moved
//	 */
//	public void translate(int[] index, int dx, int dy, boolean text, boolean cell) {
//		if (index != null) {
//			for (int idx = 0; idx < index.length; idx++) {
//				translate(index[idx], dx, dy, text, cell);
//			}
//		}
//	}
//
//	/**
//	 * Translate one cell of the array together with the corresponding text
//	 * element
//	 * 
//	 * @param index
//	 *          the index of the requested cell or cell entry
//	 * @param dx
//	 *          the x-value of the translation
//	 * @param dy
//	 *          the y-value of the translation
//	 * @param text
//	 *          shall the cell content be moved
//	 * @param cell
//	 *          shall the cell outline be moved
//	 */
//	public void translate(int index, int dx, int dy, boolean text, boolean cell) {
//		if ((index >= 0) && (index < entry.length)) {
//			if (text)
//				entry[index].translate(dx, dy);
//			if (cell)
//				cells[index].translate(dx, dy);
//		}
//	}
//
//	/**
//	 * Change the cell size by moving the left or right edge.
//	 * 
//	 * @param index
//	 *          the chosen cell
//	 * @param dx
//	 *          the number of pixels that shall be translated in x-direction
//	 * @param leftRight
//	 *          which side of the cells has to be moved true means 'move left
//	 *          edge' false means 'move right edge'
//	 */
//	public void resizeCell(int index, int dx, boolean leftRight) {
//		// TODO this used to ask for getNodeCount() == 4
//		if (cells[index].getNodeCount() != 4) { // this should not happen because
//			// all the cells are closed
//			MessageDisplay.errorMsg(translator.AnimalTranslator
//					.translateMessage("nodeCountError"), MessageDisplay.CONFIG_ERROR);
//		} else if (leftRight) { // move left edge
//			//TODO Check if this is OK
//			cells[index].translate(dx, 0);
//			cells[index].setWidth(cells[index].getWidth() - dx);
////			cells[index].translate(new boolean[] { true, false, false, true, true },
////					dx, 0);
//		} else { // move right edge
//			//TODO Check if this is OK
//			cells[index].setWidth(cells[index].getWidth() + dx);
////			cells[index].translate(new boolean[] { false, true, true, false, false },
////					dx, 0);
//		}
//	}
//
//	/**
//	 * Change the height of the cell by moving the bottom edge
//	 * 
//	 * @param index
//	 *          the chosen cell
//	 * @param dy
//	 *          the number of pixels to be translated in vertical
//	 */
//	public void resizeCell(int index, int dy) {
//		// TODO Check - used to test for 5, not 4
//		if (cells[index].getNodeCount() != 4) { // this should not happen because
//			// all the cells are closed
//			MessageDisplay.errorMsg(translator.AnimalTranslator
//					.translateMessage("nodeCountError"), MessageDisplay.CONFIG_ERROR);
//		} else { // move right edge
//			//TODO check if this is OK
//			cells[index].setHeight(cells[index].getHeight() + dy);
////			cells[index].translate(new boolean[] { false, false, true, true, false },
////					0, dy);
//		}
//	}
}