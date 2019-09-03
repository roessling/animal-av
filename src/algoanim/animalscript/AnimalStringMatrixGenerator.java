package algoanim.animalscript;

import java.awt.Color;
import java.awt.Font;

import algoanim.primitives.StringMatrix;
import algoanim.primitives.generators.StringMatrixGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.items.EnumerationPropertyItem;
import algoanim.util.Timing;

/**
 * @see algoanim.primitives.generators.StringMatrixGenerator
 * @author Dr. Guido Roessling (roessling@acm.org>
 * @version 0.4 2007-04-04
 */
public class AnimalStringMatrixGenerator extends AnimalGenerator implements
		StringMatrixGenerator {
	private static int count = 1;
	
	/**
	 * @param as
	 *          the associated <code>Language</code> object.
	 */
	public AnimalStringMatrixGenerator(AnimalScript as) {
		super(as);
	}

	/**
	 * @see algoanim.primitives.generators.StringMatrixGenerator
	 *      #create(algoanim.primitives.StringMatrix)
	 */
	public boolean create(StringMatrix aMatrix) {
		if (isNameUsed(aMatrix.getName()) || aMatrix.getName().equals("")) {
			aMatrix.setName("StringMatrix" + AnimalStringMatrixGenerator.count);
			AnimalStringMatrixGenerator.count++;
		}
		lang.addItem(aMatrix);
		// TODO really OK like this?
//		lang.nextStep(); 

		//grid <id> <nodeDefinition> [lines <n>] [colums <n>]
		//							 [style = (plain|matrix|table|junctions)]
		//                           [cellwidth <n>] [maxcellwidth <n>]
		//                           [fixedcellsize]
		//                           [color <color>] [textcolor <color>] 
		//                           [fillcolor <color>] [highlighttextcolor <color>] 
		//                           [highlightbackcolor <color>]
		//                           [matrixstyle|tablestyle|junctions]
		StringBuilder def = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
		def.append("grid \"").append(aMatrix.getName()).append("\" ");
		def.append(AnimalGenerator.makeNodeDef(aMatrix.getUpperLeft()));
		int nrRows = aMatrix.getNrRows(), nrCols = aMatrix.getNrCols();
		def.append(" lines ").append(nrRows).append(" columns ");
		def.append(nrCols).append(' ');

		/* Properties */
    MatrixProperties matrixProps = aMatrix.getProperties();
    EnumerationPropertyItem styleItem = (EnumerationPropertyItem)matrixProps.getItem(AnimationPropertiesKeys.GRID_STYLE_PROPERTY);
    if (styleItem != null)
      def.append("style ").append(styleItem.getChoice()).append(' ');
    
    final boolean scale = ((Integer) matrixProps.get(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY)) == -1 ||
        ((Integer) matrixProps.get(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY)) == -1;
    if (!scale) {
      addIntProperty(matrixProps, AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, def);
      addIntProperty(matrixProps, AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, def);
    }
		addColorOption(matrixProps, def);
		addColorOption(matrixProps, AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, " elementColor ", def);
    addColorOption(matrixProps, AnimationPropertiesKeys.FILL_PROPERTY, " fillColor ", def);
    addColorOption(matrixProps, AnimationPropertiesKeys.GRID_BORDER_COLOR_PROPERTY, " bordercolor ", def);
    addColorOption(matrixProps, AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, " highlightTextColor ", def);
    addColorOption(matrixProps, AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, " highlightFillColor ", def);
    addColorOption(matrixProps, AnimationPropertiesKeys.GRID_HIGHLIGHT_BORDER_COLOR_PROPERTY, " highlightBorderColor ", def);
    addFontOption(matrixProps, AnimationPropertiesKeys.FONT_PROPERTY, " font ", def);
    addAlignOption(matrixProps, AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, " alignment ", def);
    addIntOption(matrixProps, AnimationPropertiesKeys.DEPTH_PROPERTY, " depth ", def);


		//TODO Display Options!
//		DisplayOptions ado = aMatrix.getDisplayOptions();
//
//		if (ado != null) {
//		  Timing o = ((Timing)ado).getOffset();
//		  if (o != null) {
//		    def.append(" " + AnimalGenerator.makeOffsetTimingDef(o));
//		  }
//		  if (ado.getCascaded() == true) {
//		    def.append(" cascaded");
//		    Timing d = ado.getDuration();
//		    if (d != null) {
//		      def.append(AnimalGenerator.makeDurationTimingDef(d));
//		    }
//		  }
//		}
		lang.addLine(def);
		// generate the elements...
		for (int row = 0; row < nrRows; row++) {
			for (int col = 0; col < nrCols; col++) {
				StringBuilder sb = new StringBuilder(128);
				sb.append("setGridValue \"").append(aMatrix.getName());
				sb.append("[").append(row).append("][").append(col);
				sb.append("]\" \"").append(aMatrix.getElement(row, col));
				sb.append("\"");
				if (scale && row == nrRows - 1 && col == nrCols - 1)
					sb.append(" refresh");
				lang.addLine(sb.toString());
			}
		}
    return scale;
	}

	
	/**
	 * @see algoanim.primitives.generators.StringMatrixGenerator #put(
	 *      algoanim.primitives.StringMatrix, int, int, String,
	 *      algoanim.util.Timing, algoanim.util.Timing)
	 */
	public void put(StringMatrix matrix, int row, int col, 
			String what, Timing delay,
			Timing duration) {
		// setgridvalue "<id>[<line>][<column>]" "<value>" [refresh] 
		// [within...] [after...]
		StringBuilder sb = new StringBuilder(128);
		sb.append("setGridValue \"").append(matrix.getName()).append("[");
		sb.append(row).append("][").append(col).append("]\" \"");
		sb.append(what).append('"');;
		if (matrix.scale) {
	    sb.append(" refresh ");      
    }
		addWithTiming(sb, delay, duration);
	}
	
	/**
	 * @see algoanim.primitives.generators.StringMatrixGenerator#swap(
	 *      algoanim.primitives.StringMatrix, int, int, int, int,
	 *      algoanim.util.Timing, algoanim.util.Timing)
	 */
	public void swap(StringMatrix matrix, int sourceRow, int sourceCol, 
			int targetRow, int targetCol, Timing delay, Timing duration) {
		// swapgridvalues "<id1>[<line1>][<column1>]" and "<id2>[<line2>][<column2>]"
		// [refresh] [within...] [after...]
		StringBuilder sb = new StringBuilder(128);
		sb.append("swapGridValues \"").append(matrix.getName()).append("[");
		sb.append(sourceRow).append("][").append(sourceCol);
		sb.append("]\" and \"").append(matrix.getName()).append("[");
		sb.append(targetRow).append("][").append(targetCol).append("]\"");
    if (matrix.scale) {
      sb.append(" refresh ");      
    }
		addWithTiming(sb, delay, duration);
	}
	
	/**
	 * @see algoanim.primitives.generators.StringMatrixGenerator
	 *      #highlightCell(StringMatrix, int, int, algoanim.util.Timing,
	 *      algoanim.util.Timing)
	 */
	public void highlightCell(StringMatrix matrix, int row, int col,
			Timing offset, Timing duration) {
		// highlightgridcell "<id>[<line>][<column>]" <boolean> [timing]

		StringBuilder sb = new StringBuilder(128);
		sb.append("highlightGridCell \"").append(matrix.getName());
		sb.append("[").append(row).append("][").append(col).append("]\" ");
		addWithTiming(sb, offset, duration);
	}

	/**
	 * @see algoanim.primitives.generators.StringMatrixGenerator
	 *      #highlightCellColumnRange( StringMatrix, int, int, int,
	 *      algoanim.util.Timing,
	 *      algoanim.util.Timing)
	 */
	public void highlightCellColumnRange(StringMatrix matrix, int row, int startCol,
			int endCol, Timing offset, Timing duration) {
	  boolean cleverCode = false;
		// highlightgridcell "<id>[<line>][<column>]" <boolean> [timing]
		if (startCol == 0 && endCol == matrix.getNrCols() - 1 && cleverCode) {
			StringBuilder sb = new StringBuilder(512);
			sb.append("highlightGridCell \"").append(matrix.getName());
			sb.append("[").append(row).append("][]\" ");
			addWithTiming(sb, offset, duration);
		} else {
			for (int col = startCol; col <= endCol; col++) {
				StringBuilder sb = new StringBuilder(512);
				sb.append("highlightGridCell \"").append(matrix.getName());
				sb.append("[").append(row).append("][").append(col).append("]\" ");
				addWithTiming(sb, offset, duration);
			}
		}
	}

	/**
	 * @see algoanim.primitives.generators.StringMatrixGenerator
	 *      #highlightCellRowRange( StringMatrix, int, int, int,
	 *      algoanim.util.Timing,
	 *      algoanim.util.Timing)
	 */
	public void highlightCellRowRange(StringMatrix matrix, int startRow, 
			int endRow, int col, Timing offset, Timing duration) {
    boolean cleverCode = false;
		// highlightgridcell "<id>[<line>][<column>]" <boolean> [timing]
		if (startRow == 0 && endRow == matrix.getNrRows() - 1 && cleverCode) {
			StringBuilder sb = new StringBuilder(512);
			sb.append("highlightGridCell \"").append(matrix.getName());
			sb.append("[][").append(col).append("]\" ");
			addWithTiming(sb, offset, duration);
		} else {
			for (int row = startRow; row <= endRow; row++) {
				StringBuilder sb = new StringBuilder(512);
				sb.append("highlightGridCell \"").append(matrix.getName());
				sb.append("[").append(row).append("][").append(col).append("]\" ");
				addWithTiming(sb, offset, duration);
			}
		}
	}

	/**
	 * @see algoanim.primitives.generators.StringMatrixGenerator
	 *      #highlightElem( StringMatrix, int, int, algoanim.util.Timing,
	 *      algoanim.util.Timing)
	 */
	public void highlightElem(StringMatrix matrix, int row, int col, Timing offset,
			Timing duration) {
		// highlightgridelem "<id>[<line>][<column>]" <boolean> [timing]

		StringBuilder sb = new StringBuilder(128);
		sb.append("highlightGridElem \"").append(matrix.getName());
		sb.append("[").append(row).append("][").append(col).append("]\" ");
		addWithTiming(sb, offset, duration);
	}
	
	/**
	 * @see algoanim.primitives.generators.StringMatrixGenerator
	 *      #highlightElemColumnRange( StringMatrix, int, int, int, 
	 *      algoanim.util.Timing,
	 *      algoanim.util.Timing)
	 */
	public void highlightElemColumnRange(StringMatrix matrix, int row, 
			int startCol, int endCol, Timing offset, Timing duration) {
		for(int i=startCol ; i<endCol ; i++){
			highlightElem(matrix, row, i, offset, duration);
		}
	}

	/**
	 * @see algoanim.primitives.generators.StringMatrixGenerator
	 *      #highlightElemRowRange( StringMatrix, int, int, int, 
	 *      algoanim.util.Timing,
	 *      algoanim.util.Timing)
	 */
	public void highlightElemRowRange(StringMatrix matrix, int startRow, 
			int endRow, int col, Timing offset, Timing duration) {
		for(int i=startRow ; i<endRow ; i++){
			highlightElem(matrix, i, col, offset, duration);
		}
	}


	/**
	 * @see algoanim.primitives.generators.StringMatrixGenerator
	 *      #unhighlightCell( StringMatrix, int, int, algoanim.util.Timing,
	 *      algoanim.util.Timing)
	 */
	public void unhighlightCell(StringMatrix matrix, int row, int col,
			Timing offset, Timing duration) {
		// unhighlightgridcell "<id>[<line>][<column>]" <boolean> [timing]
		StringBuilder sb = new StringBuilder(128);
		sb.append("unhighlightGridCell \"").append(matrix.getName());
		sb.append("[").append(row).append("][").append(col).append("]\" ");
		addWithTiming(sb, offset, duration);
	}

	/**
	 * @see algoanim.primitives.generators.StringMatrixGenerator
	 *      #unhighlightCell( StringMatrix, int, int, 
	 *      algoanim.util.Timing,
	 *      algoanim.util.Timing)
	 */
	public void unhighlightCellColumnRange(StringMatrix matrix, int row, 
			int startCol, int endCol, Timing offset, Timing duration) {
		// unhighlightgridcell "<id>[<line>][<column>]" <boolean> [timing]
		if (startCol == 0 && endCol == matrix.getNrCols() - 1) {
			StringBuilder sb = new StringBuilder(512);
			sb.append("unhighlightGridCell \"").append(matrix.getName());
			sb.append("[").append(row).append("][]\" ");
			addWithTiming(sb, offset, duration);
		} else {
			for (int col = startCol; col <= endCol; col++) {
				StringBuilder sb = new StringBuilder(512);
				sb.append("unhighlightGridCell \"").append(matrix.getName());
				sb.append("[").append(row).append("][").append(col).append("]\" ");
				addWithTiming(sb, offset, duration);
			}
		}
	}

	/**
	 * @see algoanim.primitives.generators.StringMatrixGenerator
	 *      #unhighlightCell( StringMatrix, int, int, 
	 *      algoanim.util.Timing,
	 *      algoanim.util.Timing)
	 */
	public void unhighlightCellRowRange(StringMatrix matrix, int startRow, 
			int endRow, int col, Timing offset, Timing duration) {
		// unhighlightgridcell "<id>[<line>][<column>]" <boolean> [timing]
		if (startRow == 0 && endRow == matrix.getNrRows() - 1) {
			StringBuilder sb = new StringBuilder(512);
			sb.append("unhighlightGridCell \"").append(matrix.getName());
			sb.append("[][").append(col).append("]\" ");
			addWithTiming(sb, offset, duration);
		} else {
			for (int row = startRow; row <= endRow; row++) {
				StringBuilder sb = new StringBuilder(512);
				sb.append("unhighlightGridCell \"").append(matrix.getName());
				sb.append("[").append(row).append("][").append(col).append("]\" ");
				addWithTiming(sb, offset, duration);
			}
		}
	}
	
	/**
	 * @see algoanim.primitives.generators.StringMatrixGenerator
	 *      #unhighlightElem( StringMatrix, int, int, algoanim.util.Timing,
	 *      algoanim.util.Timing)
	 */
	public void unhighlightElem(StringMatrix matrix, int row, int col, 
			Timing offset, Timing duration) {
		// unhighlightgridelem "<id>[<line>][<column>]" <boolean> [timing]
		StringBuilder sb = new StringBuilder(128);
		sb.append("unhighlightGridElem \"").append(matrix.getName());
		sb.append("[").append(row).append("][").append(col).append("]\" ");
		addWithTiming(sb, offset, duration);
	}

	/**
	 * @see algoanim.primitives.generators.StringMatrixGenerator
	 *      #unhighlightElemColumnRange( StringMatrix, int, int, int, algoanim.util.Timing,
	 *      algoanim.util.Timing)
	 */
	public void unhighlightElemColumnRange(StringMatrix matrix, int row, 
			int startCol, int endCol, Timing offset, Timing duration) {
		for(int i=startCol ; i<endCol ; i++){
			unhighlightElem(matrix, row, i, offset, duration);
		}
	}

	/**
	 * @see algoanim.primitives.generators.StringMatrixGenerator
	 *      #unhighlightElemRowRange( StringMatrix, int, int, int, algoanim.util.Timing,
	 *      algoanim.util.Timing)
	 */
	public void unhighlightElemRowRange(StringMatrix matrix, int startRow, 
			int endRow, int col, Timing offset, Timing duration) {
		for(int i=startRow ; i<endRow ; i++){
			unhighlightElem(matrix, i, col, offset, duration);
		}
	}

	@Override
	public void setGridColor(StringMatrix matrix, int row, int col, Color color, String kind, Timing offset, Timing duration) {
		// setgridvalue "<id>[<line>][<column>]" "<value>" [refresh] 
		// [within...] [after...]
		StringBuilder sb = new StringBuilder(128);
		sb.append("setGridColor \"").append(matrix.getName()).append("[");
		sb.append(row).append("][").append(col).append("]\" ");
		sb.append(kind).append(" ("+color.getRed()+","+color.getGreen()+","+color.getBlue()+")");
//		if (matrix.scale) {
//			sb.append(" refresh ");      
//		}
		addWithTiming(sb, offset, duration);
	}

	@Override
	public void setGridFont(StringMatrix matrix, int row, int col, Font font, Timing offset, Timing duration) {
		StringBuilder sb = new StringBuilder(128);
		sb.append("setGridFont \"").append(matrix.getName()).append("[");
		sb.append(row).append("][").append(col).append("]\" ");
	    if (font != null) {
	      //   [font <fontNames>] [size fontSize] [bold] [italic]
	    	sb.append(" font ").append(font.getName()).append(" size ");
	    	sb.append(font.getSize());
	      if (font.isBold())
	    	  sb.append(" bold");
	      if (font.isItalic())
	    	  sb.append(" italic");
	    }
		addWithTiming(sb, offset, duration);
	}
}
