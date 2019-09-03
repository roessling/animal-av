package animal.graphics;

import java.util.Vector;

import animal.graphics.meta.PTMatrix;
import animal.misc.MessageDisplay;

public class PTDoubleMatrix extends PTMatrix {

  public static final String TYPE_LABEL = "DoubleMatrix";

  private Vector<Vector<Double>> vdata = new Vector<Vector<Double>>();

  // ======================================================================
  // Constructors
  // ======================================================================

  public PTDoubleMatrix() {
    this(1, 1);
  }

  public PTDoubleMatrix(double[][] newdata) {
		super();
    if (newdata != null && newdata[0] != null) {
      init();
      setSize(newdata.length, newdata[0].length);
      // set data
      for (int r = 0; r < newdata.length; ++r) {
        for (int c = 0; c < newdata[r].length; ++c) {
          vdata.get(r).set(c, new Double(newdata[r][c]));
        }
      }
      updateTextualRepresentation();
    }
  }

  public PTDoubleMatrix(int rowcnt, int columncnt) {
		super();
		init();
		this.setSize(rowcnt, columncnt);		
  }

  /**
   * Clones the current graphical object. Note that the method will per
   * definition return Object, so the result has to be cast to the appropriate
   * type.
   * 
   * @return a clone of the current object, statically typed as Object.
   */
  public Object clone() {
    // create new object
    PTDoubleMatrix targetShape = new PTDoubleMatrix(this.getRowCount(), this.getMaxColumnCount());

    // clone shared attributes
    // from PTGO: color, depth, num, objectName
    cloneCommonFeaturesInto(targetShape);

    // clone anything else that is specific to this type
    // and its potential subtypes
    return targetShape;
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
  protected void cloneCommonFeaturesInto(PTDoubleMatrix targetShape) {
    // clone features from PTGraphicsObject: color, depth, num, objectName
    super.cloneCommonFeaturesInto(targetShape);

    // clone anything else that is specific to this type
    // and its potential subtypes
    int rowcount = getRowCount();
    int columncount;
    targetShape.setRowCount(rowcount);
    for (int r = 0; r < rowcount; ++r) {
      columncount = this.getColumnCount(r);
      targetShape.setColumnCount(r, columncount);
      for (int c = 0; c < columncount; ++c) {
        targetShape.setDataAt(r, c, this.getDataAt(r, c));
      }
    }
  }

  // public Object clone() {
  // PTDoubleMatrix matrix = (PTDoubleMatrix)super.clone();
  // int rowcount = getRowCount();
  // int columncount;
  // matrix.setRowCount(rowcount);
  // for(int r =0;r<rowcount;++r){
  // columncount = this.getColumnCount(r);
  // matrix.setColumnCount(r, columncount);
  // for(int c =0;c<columncount;++c){
  // matrix.setDataAt(r, c, this.getDataAt(r, c));
  // }
  // }
  // return matrix;
  // }

  @Override
  public int getColumnCount(int row) {
    return this.vdata.get(row).size();
  }

  @Override
  public String getElementAt(int r, int c) {
    if (this.indicesAreInRange(r, c))
      return String.valueOf(vdata.get(r).get(c));
    return String.valueOf(0.0);
  }

  @Override
  public int getRowCount() {
    return vdata.size();
  }

  @Override
  public String getType() {
    return TYPE_LABEL;
  }

  @Override
  public String[] handledKeywords() {
    return new String[] { "DoubleMatrix" };
  }

  @Override
  protected void updateTextualRepresentation() {
    for (int r = 0; r < vdata.size(); ++r) {
      for (int c = 0; c < vdata.get(r).size(); ++c) {
        vTextData.get(r).get(c).setText(String.valueOf(vdata.get(r).get(c)));
      }
    }
  }

  @Override
  protected void addDataItem(int rowIndex) {
    if (rowIndex >= 0 && rowIndex < this.vdata.size())
      this.vdata.get(rowIndex).add(0.0);
  }

  @Override
  protected void addDataRow() {
    this.vdata.add(new Vector<Double>());
  }

  @Override
  protected PTMatrix getPreClone() {
    return new PTDoubleMatrix();
  }

  @Override
  protected void setDataRowCount(int value) {
    this.vdata.setSize(value);
  }

  @Override
  protected void shrinkDataRow(int rowIndex, int value) {
    if (rowIndex >= 0 && rowIndex < this.getRowCount())
      this.vdata.get(rowIndex).setSize(value);
  }

  @Override
  protected void setDataOfSpecialType(int r, int c, String value) {
    try {
      this.vdata.get(r).set(c, Double.valueOf(value));
    } catch (NumberFormatException e) {
      MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
    }
  }

  public int getFileVersion() {
    return 1;
  }

  public double getDataAt(int r, int c) {
    if (indicesAreInRange(r, c))
      return vdata.get(r).get(c);
    return 0.0;
  }

  public void setDataAt(int r, int c, double value) {
    if (indicesAreInRange(r, c)) {
      vdata.get(r).set(c, value);
      setTextDataAt(r, c, String.valueOf(value));
    }
  }

  /**
   * Sets the elements of the matrix diagonally down or up to the right of
   * starting index c.
   * 
   * @param r
   *          starting row index
   * @param c
   *          starting column index
   * @param value
   *          value to set the diagonal to
   * @param down
   *          true if direction is down to the right. false for up to the right
   */

  public void setDiagonalData(int r, int c, double value, boolean down) {
    int maxcolumncount = this.getMaxColumnCount();
    int rowcount = this.getRowCount();
    if (r >= 0 && r < rowcount && c >= 0 && c < maxcolumncount) {
      if (down) {
        for (int x = c, y = r; x < maxcolumncount && y < rowcount; ++x, ++y) {
          if (x < getColumnCount(y)) {
            setDataAt(y, x, value);
            setTextDataAt(y, x, String.valueOf(value));
          }
        }
      } else {
        for (int x = c, y = r; x < maxcolumncount && y >= 0; ++x, --y) {
          if (x < getColumnCount(y)) {
            setDataAt(y, x, value);
            setTextDataAt(y, x, String.valueOf(value));
          }
        }
      }
    }
  }

}
