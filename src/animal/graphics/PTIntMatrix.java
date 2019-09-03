package animal.graphics;

import java.util.Vector;

import animal.graphics.meta.FillablePrimitive;
import animal.graphics.meta.IndexableContentContainer;
import animal.graphics.meta.PTMatrix;
import animal.misc.MessageDisplay;

public class PTIntMatrix extends PTMatrix implements FillablePrimitive,
	IndexableContentContainer{	

	// =====================================================================
	// Public Constants
	// =====================================================================
	
	public static final String TYPE_LABEL = "IntMatrix";

		
	private Vector<Vector<Integer>> vdata = new Vector<Vector<Integer>>();
		
		
	// ======================================================================
	// Constructors
	// ======================================================================
	
	public PTIntMatrix(){
		this(1,1);		
	}
	
	public PTIntMatrix(int[][] newdata){
		super();
		if(newdata != null && newdata[0]!= null){
			init();	
			setSize(newdata.length,newdata[0].length);
		//set data
			for(int r =0;r<newdata.length;++r){
				for(int c =0;c<newdata[r].length;++c){			
					vdata.get(r).set(c, new Integer(newdata[r][c]));
				}
			}
			updateTextualRepresentation();
		}
	}
	
	public PTIntMatrix(int rowcnt, int columncnt) {
		super();
		init();
		this.setSize(rowcnt, columncnt);		
	}


	// ======================================================================
	// Attribute accessing
	// ======================================================================
	public int getFileVersion() {
		return 1; 
	}
	

	@Override
	public String getType() {
		return TYPE_LABEL;
	}
	
	public int getRowCount(){
		return vdata.size();
	}

	public int getColumnCount(int row){
		if(row >= 0 && row < getRowCount())
			return vdata.get(row).size();
		else
			return 0;
	}
		
	@Override
	public String[] handledKeywords() {
		return new String[]{"IntMatrix"};
	}

	public String getElementAt(int r, int c){
		if(indicesAreInRange(r,c))
			return String.valueOf(vdata.get(r).get(c));
		return String.valueOf(0);
	}
	
	public int getDataAt(int r, int c){
		if(indicesAreInRange(r,c))
			return vdata.get(r).get(c);
		return 0;
	}
	
	public void setDataAt(int r, int c, int value){
		if( indicesAreInRange(r,c)){
			vdata.get(r).set(c, value);
			setTextDataAt(r,c,String.valueOf(value));
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
	
	public void setDiagonalData(int r, int c, int value, boolean down){
		int maxcolumncount = this.getMaxColumnCount();
		int rowcount = this.getRowCount();
		if(r >= 0 && r < rowcount && c >= 0 && c < maxcolumncount){
			if(down){
				for(int x = c,  y = r; x < maxcolumncount && y < rowcount; ++x,++y ){
					if(x < getColumnCount(y)){
						setDataAt(y,x,value);
						setTextDataAt(y,x,String.valueOf(value));
					}
				}
			}else{			
				for(int x = c,  y = r; x < maxcolumncount && y >= 0; ++x,--y ){
					if(x < getColumnCount(y)){
						setDataAt(y,x,value);
						setTextDataAt(y,x,String.valueOf(value));
					}
				}
			}
		}
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
        PTIntMatrix targetShape = new PTIntMatrix(this.getRowCount(), this.getMaxColumnCount());

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
       * @param targetShape the shape into which the values are to be copied. Note
       * the direction -- it is "currentObject.cloneCommonFeaturesInto(newObject)", 
       * not vice versa!
       */
      protected void cloneCommonFeaturesInto(PTIntMatrix targetShape) {
        // clone features from PTGraphicsObject: color, depth, num, objectName
        super.cloneCommonFeaturesInto(targetShape);

        // clone anything else that is specific to this type
        // and its potential subtypes
        int rowcount = getRowCount();
        int columncount;
        targetShape.setRowCount(rowcount);
        for(int r =0;r<rowcount;++r){
            columncount = this.getColumnCount(r);
            targetShape.setColumnCount(r, columncount);
            for(int c =0;c<columncount;++c){
              targetShape.setDataAt(r, c, this.getDataAt(r, c));
            }
        }               

      }

	
	protected void updateTextualRepresentation() {
		for(int r = 0; r < vdata.size();++r){
			for(int c= 0; c < vdata.get(r).size();++c){
				vTextData.get(r).get(c).setText(String.valueOf(vdata.get(r).get(c)));
			}
		}		
	}
	


	@Override
	protected void addDataItem(int rowIndex) {
		if(rowIndex >= 0 && rowIndex < this.vdata.size())
		this.vdata.get(rowIndex).add(0);		
	}

	@Override
	protected void addDataRow() {
		this.vdata.add(new Vector<Integer>());		
	}

	@Override
	protected PTMatrix getPreClone() {
		return new PTIntMatrix();
	}

	@Override
	protected void setDataRowCount(int value) {
		this.vdata.setSize(value);		
	}

	@Override
	protected void shrinkDataRow(int rowIndex, int value) {
		if(rowIndex >= 0 && rowIndex < this.getRowCount())
			this.vdata.get(rowIndex).setSize(value);
	}

	@Override
	protected void setDataOfSpecialType(int r, int c, String value) {
		try {
			Integer intval = Integer.valueOf(value);
			this.vdata.get(r).set(c, intval);
		} catch (NumberFormatException e) {
			MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
		}		
	}



}
