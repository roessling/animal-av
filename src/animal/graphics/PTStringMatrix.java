package animal.graphics;

import animal.graphics.meta.PTMatrix;

public class PTStringMatrix extends PTMatrix {
	
	public static final String TYPE_LABEL = "StringMatrix";

	// ======================================================================
	// Constructors
	// ======================================================================
	
	public PTStringMatrix(){
		this(1,1);		
	}
	
	public PTStringMatrix(String[][] newdata){
		super();
		if(newdata != null && newdata[0]!= null){
			init();	
			setSize(newdata.length,newdata[0].length);
		//set data
			for(int r =0;r<newdata.length;++r){
				for(int c =0;c<newdata[r].length;++c){			
					vTextData.get(r).get(c).setText(newdata[r][c]);
				}
			}
		}
	}
	
	public PTStringMatrix(int rowcnt, int columncnt) {
		super();
		init();
		this.setSize(rowcnt, columncnt);	
	}
	
//	public Object clone() {
//		PTStringMatrix matrix = (PTStringMatrix)super.clone();	
//		int rowcount = getRowCount();
//		int columncount;
//		matrix.setRowCount(rowcount);
//		for(int r =0;r<rowcount;++r){
//			columncount = this.getColumnCount(r);
//			matrix.setColumnCount(r, columncount);
//			for(int c =0;c<columncount;++c){
//				matrix.setElementAt(r, c, this.vTextData.get(r).get(c).getText());
//			}
//		}				
//		return matrix;
//	}
      /**
       * Clones the current graphical object. Note that the method will per
       * definition return Object, so the result has to be cast to the appropriate
       * type.
       * 
       * @return a clone of the current object, statically typed as Object.
       */
      public Object clone() {
        // create new object
        PTStringMatrix targetShape = new PTStringMatrix(this.getRowCount(), this.getMaxColumnCount());

        // clone shared attributes
        // from PTGO: color, depth, num, objectName
        cloneCommonFeaturesInto(targetShape);

        // clone anything else that is specific to this type
        // and its potential subtypes
        return targetShape;
      }
//
//      /**
//       * Offers cloning support by cloning or duplicating the shared attributes
//       * 
//       * @param targetShape the shape into which the values are to be copied. Note
//       * the direction -- it is "currentObject.cloneCommonFeaturesInto(newObject)", 
//       * not vice versa!
//       */
//      protected void cloneCommonFeaturesInto(PTStringMatrix targetShape) {
//        // clone features from PTGraphicsObject: color, depth, num, objectName
//        super.cloneCommonFeaturesInto(targetShape);
//
//        // clone anything else that is specific to this type
//        // and its potential subtypes
////        int rowcount = getRowCount();
////      int columncount;
////      matrix.setRowCount(rowcount);
////      for(int r =0;r<rowcount;++r){
////          columncount = this.getColumnCount(r);
////          matrix.setColumnCount(r, columncount);
////          for(int c =0;c<columncount;++c){
////              matrix.setElementAt(r, c, this.vTextData.get(r).get(c).getText());
////          }
////      }               
//      }
      
	@Override
	/**
	 * Stringmatrix uses PTText vectors of PTMatrix to save ist content.
	 * so in this method happens nothing.
	 */	
	protected void addDataItem(int rowIndex) {

	}

	/**
	 * Stringmatrix uses PTText vectors of PTMatrix to save ist content.
	 * so in this method happens nothing.
	 */	
	@Override
	protected void addDataRow() {

	}

	/**
	 * Stringmatrix uses PTText vectors of PTMatrix to save ist content.
	 * so the size of these vetors is returned.
	 */	
	@Override
	public int getColumnCount(int row) {
		return this.vTextData.get(row).size();
	}

	@Override
	public String getElementAt(int r, int c) {
		if(this.indicesAreInRange(r, c))
			return vTextData.get(r).get(c).getText();
		return vTextData.get(0).get(0).getText();
	}

	@Override
	protected PTMatrix getPreClone() {
		return new PTStringMatrix();
	}

	@Override
	public int getRowCount() {
		return this.vTextData.size();
	}

	@Override
	public String getType() {
		return TYPE_LABEL;
	}
	
	@Override
	public String[] handledKeywords() {
		return new String[]{"StringMatrix"};
	}
	/**
	 * Stringmatrix uses PTText vectors of PTMatrix to save ist content.
	 * so in this method happens nothing.
	 */	
	@Override
	protected void setDataOfSpecialType(int r, int c, String value) {

	}

	/**
	 * Stringmatrix uses PTText vectors of PTMatrix to save ist content.
	 * so in this method happens nothing.
	 */	
	@Override
	protected void setDataRowCount(int value) {

	}

	/**
	 * Stringmatrix uses PTText vectors of PTMatrix to save ist content.
	 * so in this method happens nothing.
	 */	
	@Override
	protected void shrinkDataRow(int rowIndex, int value) {

	}

	/**
	 * Stringmatrix uses PTText vectors of PTMatrix to save ist content.
	 * so in this method happens nothing.
	 */	
	@Override
	protected void updateTextualRepresentation() {

	}

	public int getFileVersion() {
		return 1; 
	}
}
