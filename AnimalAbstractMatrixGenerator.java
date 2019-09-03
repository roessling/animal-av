package algoanim.animalscript;

import java.awt.Color;
import java.awt.Font;

import algoanim.primitives.DoubleMatrix;
import algoanim.primitives.MatrixPrimitive;
import algoanim.primitives.generators.DoubleMatrixGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.items.EnumerationPropertyItem;
import algoanim.util.Timing;

/**
 * @see algoanim.primitives.generators.DoubleMatrixGenerator
 * @author Dr. Guido Roessling (roessling@acm.org>
 * @version 0.4 2007-04-04
 */
public class AnimalAbstractMatrixGenerator extends AnimalGenerator {
  private static int count = 1;

  /**
   * @param as
   *          the associated <code>Language</code> object.
   */
  public AnimalAbstractMatrixGenerator(AnimalScript as) {
    super(as);
  }

  /**
   * @see algoanim.primitives.generators.DoubleMatrixGenerator
   *      #create(algoanim.primitives.DoubleMatrix)
   */
  public boolean create(MatrixPrimitive aMatrix) {
    if (isNameUsed(aMatrix.getName()) || aMatrix.getName().equals("")) {
      aMatrix.setName("DoubleMatrix" + AnimalAbstractMatrixGenerator.count);
      AnimalAbstractMatrixGenerator.count++;
    }
    lang.addItem(aMatrix);
    // TO DO really OK like this?
    // lang.nextStep();

    // grid <id> <nodeDefinition> [lines <n>] [colums <n>]
    // [style = (plain|matrix|table|junctions)]
    // [cellwidth <n>] [maxcellwidth <n>]
    // [fixedcellsize]
    // [color <color>] [textcolor <color>]
    // [fillcolor <color>] [highlighttextcolor <color>]
    // [highlightbackcolor <color>]
    // [matrixstyle|tablestyle|junctions]
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
   * @see algoanim.primitives.generators.DoubleMatrixGenerator
   *      #put(algoanim.primitives.DoubleMatrix, int, int, double,
   *      algoanim.util.Timing, algoanim.util.Timing)
   */
  public void put(DoubleMatrix doubleMatrix, int row, int col, double what,
      Timing delay, Timing duration) {
    // setgridvalue "<id>[<line>][<column>]" "<value>" [refresh]
    // [within...] [after...]
    StringBuilder sb = new StringBuilder(128);
    sb.append("setGridValue \"").append(doubleMatrix.getName()).append("[");
    sb.append(row).append("][").append(col).append("]\" \"");
    sb.append(what).append('"');
    if (doubleMatrix.scale) {
      sb.append(" refresh ");      
    }
    addWithTiming(sb, delay, duration);
  }

  /**
   * @see algoanim.primitives.generators.DoubleMatrixGenerator#swap(algoanim.primitives.DoubleMatrix,
   *      int, int, int, int, algoanim.util.Timing, algoanim.util.Timing)
   */
  public void swap(DoubleMatrix doubleMatrix, int sourceRow, int sourceCol,
      int targetRow, int targetCol, Timing delay, Timing duration) {
    // swapgridvalues "<id1>[<line1>][<column1>]" and
    // "<id2>[<line2>][<column2>]"
    // [refresh] [within...] [after...]
    StringBuilder sb = new StringBuilder(128);
    sb.append("swapGridValues \"").append(doubleMatrix.getName()).append("[");
    sb.append(sourceRow).append("][").append(sourceCol);
    sb.append("]\" and \"").append(doubleMatrix.getName()).append("[");
    sb.append(targetRow).append("][").append(targetCol).append("]\"");
    if (doubleMatrix.scale) {
      sb.append(" refresh ");      
    }
    addWithTiming(sb, delay, duration);
  }

  /**
   * @see algoanim.primitives.generators.DoubleMatrixGenerator
   *      #highlightCell(DoubleMatrix, int, int, algoanim.util.Timing,
   *      algoanim.util.Timing)
   */
  public void highlightCell(DoubleMatrix doubleMatrix, int row, int col,
      Timing offset, Timing duration) {
    // highlightgridcell "<id>[<line>][<column>]" <boolean> [timing]

    StringBuilder sb = new StringBuilder(128);
    sb.append("highlightGridCell \"").append(doubleMatrix.getName());
    sb.append("[").append(row).append("][").append(col).append("]\" ");
    addWithTiming(sb, offset, duration);
  }

  /**
   * @see algoanim.primitives.generators.DoubleMatrixGenerator
   *      #highlightCellColumnRange(DoubleMatrix, int, int, int,
   *      algoanim.util.Timing, algoanim.util.Timing)
   */
  public void highlightCellColumnRange(DoubleMatrix doubleMatrix, int row,
      int startCol, int endCol, Timing offset, Timing duration) {
    // highlightgridcell "<id>[<line>][<column>]" <boolean> [timing]
    if (startCol == 0 && endCol == doubleMatrix.getNrCols() - 1) {
      StringBuilder sb = new StringBuilder(512);
      sb.append("highlightGridCell \"").append(doubleMatrix.getName());
      sb.append("[").append(row).append("][]\" ");
      addWithTiming(sb, offset, duration);
    } else {
      for (int col = startCol; col <= endCol; col++) {
        StringBuilder sb = new StringBuilder(512);
        sb.append("highlightGridCell \"").append(doubleMatrix.getName());
        sb.append("[").append(row).append("][").append(col).append("]\" ");
        addWithTiming(sb, offset, duration);
      }
    }
  }

  /**
   * @see algoanim.primitives.generators.DoubleMatrixGenerator
   *      #highlightCellRowRange(DoubleMatrix, int, int, int, algoanim.util.Timing,
   *      algoanim.util.Timing)
   */
  public void highlightCellRowRange(DoubleMatrix doubleMatrix, int startRow,
      int endRow, int col, Timing offset, Timing duration) {
    // highlightgridcell "<id>[<line>][<column>]" <boolean> [timing]
    if (startRow == 0 && endRow == doubleMatrix.getNrRows() - 1) {
      StringBuilder sb = new StringBuilder(512);
      sb.append("highlightGridCell \"").append(doubleMatrix.getName());
      sb.append("[][").append(col).append("]\" ");
      addWithTiming(sb, offset, duration);
    } else {
      for (int row = startRow; row <= endRow; row++) {
        StringBuilder sb = new StringBuilder(512);
        sb.append("highlightGridCell \"").append(doubleMatrix.getName());
        sb.append("[").append(row).append("][").append(col).append("]\" ");
        addWithTiming(sb, offset, duration);
      }
    }
  }

  /**
   * @see algoanim.primitives.generators.DoubleMatrixGenerator
   *      #highlightElem(DoubleMatrix, int, int, algoanim.util.Timing,
   *      algoanim.util.Timing)
   */
  public void highlightElem(DoubleMatrix doubleMatrix, int row, int col,
      Timing offset, Timing duration) {
		// highlightgridelem "<id>[<line>][<column>]" <boolean> [timing]

		StringBuilder sb = new StringBuilder(128);
		sb.append("highlightGridElem \"").append(doubleMatrix.getName());
		sb.append("[").append(row).append("][").append(col).append("]\" ");
		addWithTiming(sb, offset, duration);
	}

  /**
   * @see algoanim.primitives.generators.DoubleMatrixGenerator
   *      #highlightElemColumnRange(DoubleMatrix, int, int, int,
   *      algoanim.util.Timing, algoanim.util.Timing)
   */
  public void highlightElemColumnRange(DoubleMatrix doubleMatrix, int row,
      int startCol, int endCol, Timing offset, Timing duration) {
		for(int i=startCol ; i<endCol ; i++){
			highlightElem(doubleMatrix, row, i, offset, duration);
		}
	}

  /**
   * @see algoanim.primitives.generators.DoubleMatrixGenerator
   *      #highlightElemRowRange(DoubleMatrix, int, int, int, algoanim.util.Timing,
   *      algoanim.util.Timing)
   */
  public void highlightElemRowRange(DoubleMatrix doubleMatrix, int startRow,
      int endRow, int col, Timing offset, Timing duration) {
		for(int i=startRow ; i<endRow ; i++){
			highlightElem(doubleMatrix, i, col, offset, duration);
		}
  }

  /**
   * @see algoanim.primitives.generators.DoubleMatrixGenerator
   *      #unhighlightCell(DoubleMatrix, int, int, algoanim.util.Timing,
   *      algoanim.util.Timing)
   */
  public void unhighlightCell(DoubleMatrix doubleMatrix, int row, int col,
      Timing offset, Timing duration) {
    // unhighlightgridcell "<id>[<line>][<column>]" <boolean> [timing]
    StringBuilder sb = new StringBuilder(128);
    sb.append("unhighlightGridCell \"").append(doubleMatrix.getName());
    sb.append("[").append(row).append("][").append(col).append("]\" ");
    addWithTiming(sb, offset, duration);
  }

  /**
   * @see algoanim.primitives.generators.DoubleMatrixGenerator
   *      #unhighlightCell(DoubleMatrix, int, int, algoanim.util.Timing,
   *      algoanim.util.Timing)
   */
  public void unhighlightCellColumnRange(DoubleMatrix doubleMatrix, int row,
      int startCol, int endCol, Timing offset, Timing duration) {
    // unhighlightgridcell "<id>[<line>][<column>]" <boolean> [timing]
    if (startCol == 0 && endCol == doubleMatrix.getNrCols() - 1) {
      StringBuilder sb = new StringBuilder(512);
      sb.append("unhighlightGridCell \"").append(doubleMatrix.getName());
      sb.append("[").append(row).append("][]\" ");
      addWithTiming(sb, offset, duration);
    } else {
      for (int col = startCol; col <= endCol; col++) {
        StringBuilder sb = new StringBuilder(512);
        sb.append("unhighlightGridCell \"").append(doubleMatrix.getName());
        sb.append("[").append(row).append("][").append(col).append("]\" ");
        addWithTiming(sb, offset, duration);
      }
    }
  }

  /**
   * @see algoanim.primitives.generators.DoubleMatrixGenerator
   *      #unhighlightCell(DoubleMatrix, int, int, algoanim.util.Timing,
   *      algoanim.util.Timing)
   */
  public void unhighlightCellRowRange(DoubleMatrix doubleMatrix, int startRow,
      int endRow, int col, Timing offset, Timing duration) {
    // unhighlightgridcell "<id>[<line>][<column>]" <boolean> [timing]
    if (startRow == 0 && endRow == doubleMatrix.getNrRows() - 1) {
      StringBuilder sb = new StringBuilder(512);
      sb.append("unhighlightGridCell \"").append(doubleMatrix.getName());
      sb.append("[][").append(col).append("]\" ");
      addWithTiming(sb, offset, duration);
    } else {
      for (int row = startRow; row <= endRow; row++) {
        StringBuilder sb = new StringBuilder(512);
        sb.append("unhighlightGridCell \"").append(doubleMatrix.getName());
        sb.append("[").append(row).append("][").append(col).append("]\" ");
        addWithTiming(sb, offset, duration);
      }
    }
  }

  /**
   * @see algoanim.primitives.generators.DoubleMatrixGenerator
   *      #unhighlightElem(DoubleMatrix, int, int, algoanim.util.Timing,
   *      algoanim.util.Timing)
   */
  public void unhighlightElem(DoubleMatrix doubleMatrix, int row, int col,
      Timing offset, Timing duration) {
		// unhighlightgridelem "<id>[<line>][<column>]" <boolean> [timing]
		StringBuilder sb = new StringBuilder(128);
		sb.append("unhighlightGridElem \"").append(doubleMatrix.getName());
		sb.append("[").append(row).append("][").append(col).append("]\" ");
		addWithTiming(sb, offset, duration);
  }

  /**
   * @see algoanim.primitives.generators.DoubleMatrixGenerator
   *      #unhighlightElemColumnRange(DoubleMatrix, int, int, int,
   *      algoanim.util.Timing, algoanim.util.Timing)
   */
  public void unhighlightElemColumnRange(DoubleMatrix doubleMatrix, int row,
      int startCol, int endCol, Timing offset, Timing duration) {
		for(int i=startCol ; i<endCol ; i++){
			unhighlightElem(doubleMatrix, row, i, offset, duration);
		}
  }

  /**
   * @see algoanim.primitives.generators.DoubleMatrixGenerator
   *      #unhighlightElemRowRange(DoubleMatrix, int, int, int,
   *      algoanim.util.Timing, algoanim.util.Timing)
   */
  public void unhighlightElemRowRange(DoubleMatrix doubleMatrix, int startRow, 
			int endRow, int col, Timing offset, Timing duration) {
		for(int i=startRow ; i<endRow ; i++){
			unhighlightElem(doubleMatrix, i, col, offset, duration);
		}
  }

	@Override
	public void setGridColor(DoubleMatrix matrix, int row, int col, Color color, String kind, Timing offset, Timing duration) {
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
	public void setGridFont(DoubleMatrix matrix, int row, int col, Font font, Timing offset, Timing duration) {
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
