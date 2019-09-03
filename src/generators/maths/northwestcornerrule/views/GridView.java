package generators.maths.northwestcornerrule.views;


import java.awt.Color;

import generators.helpers.OffsetCoords;
import generators.maths.grid.Grid;
import generators.maths.northwestcornerrule.AnimProps;
import generators.maths.northwestcornerrule.DrawingUtils;
import algoanim.primitives.generators.Language;
import algoanim.util.Coordinates;

public class GridView {
	
	private Language animationScript;
	private DrawingUtils myDrawingUtils;
	
	private Grid basisGrid;
	private Grid rightGrid;
	private Grid bottomGrid;
	
	private static Coordinates CO_HEADER;
	private static Coordinates CO_BASISGRID;
	
	private static String HEADER = "Grid";
	private static String ROW_CAPTION_STARTSWITH = "A";
	private static String COL_CAPTION_STARTSWITH = "N";
	private static String ROW_CAPTION_OVERALL = "ANBIETER";
	private static String COL_CAPTION_OVERALL = "NACHFRAGER";
	private static String ROW_CAPTION_BOTTOM = "Nachgefragte Menge";
	private static String COL_CAPTION_BOTTOM = "Angebotene Menge";
	private static int CELL_SIZE = 40;
	
	static{
		CO_HEADER = new Coordinates(340, 20);
		CO_BASISGRID = new Coordinates(530, 140);
	}
	
	public GridView(Language animationScript, DrawingUtils myDrawingUtils){
		this.animationScript = animationScript;
		this.myDrawingUtils = myDrawingUtils;
	}
	
	public void setupView(int[] supply, int[] demand) {
		
		myDrawingUtils.drawHeader(CO_HEADER, HEADER);
		basisGrid = createBasisGrid(supply.length, demand.length, CO_BASISGRID);
		rightGrid = createRightGrid(supply, demand.length);
		bottomGrid = createBottomGrid(demand, supply.length);
	}
	
	private Grid createBasisGrid(int supplySize, int demandSize, Coordinates co){
		
		// Create Grid
		Grid x = new Grid(co, demandSize, supplySize, CELL_SIZE, animationScript, AnimProps.GRID_PROPS);

		// Create Caption
		String captionTop[] = new String[demandSize];
		for(int i=1; i<=demandSize; i++){
			captionTop[i-1] = COL_CAPTION_STARTSWITH + i;
		}
		x.setCaptionTop(captionTop);
		
		String captionLeft[] = new String[supplySize];
		for(int i=1; i<=supplySize; i++){
			captionLeft[i-1] = ROW_CAPTION_STARTSWITH + i;
		}
		x.setCaptionLeft(captionLeft);
		
		myDrawingUtils.drawTextWithMinorBox(new OffsetCoords(co, -180, +5),ROW_CAPTION_OVERALL);
		myDrawingUtils.drawTextWithMinorBox(new OffsetCoords(co, +5, -85),COL_CAPTION_OVERALL);
		
		return x;
		
	}
	
	public void createLastFrameGrid(Integer[][] base){
		
		Grid x = createBasisGrid( base[0].length,base.length, new Coordinates(200, 350));
		
		for(int i= 0; i<base.length; i++){
			for(int j= 0; j<base[0].length; j++){
				if(base[i][j] != null){
					x.highlightCell(i, j, Color.green, 0);
					x.setLabel(i, j, "" +base[i][j]);
				}
			}
		}
		
	}

	private Grid createRightGrid(int[] supply, int demandSize){
		
		// Create Grid
		Coordinates rGridCo = new Coordinates(calculateRightGridPosition(demandSize), 140);
		Grid x = new Grid(rGridCo, demandSize + 1, supply.length, CELL_SIZE, animationScript, AnimProps.GRID_PROPS);

		// Right values in first column
		for(int i=0; i<supply.length; i++){
			x.setLabel(0, i, ""+supply[i]);
		}
		
		myDrawingUtils.drawTextWithMinorBox(new OffsetCoords(rGridCo, +5, -85),COL_CAPTION_BOTTOM);
		
		return x;
	}
	
	private int calculateRightGridPosition(int demandSize){
		int result = 530 + CELL_SIZE * demandSize + 30;
		return result;
	}
	
	private Grid createBottomGrid(int[] demand, int supplySize){
		
		// Create Grid
		Coordinates bGridCo = new Coordinates(530, calculateBottomGridPosition(supplySize));
		Grid x = new Grid(bGridCo, demand.length, supplySize +1, CELL_SIZE, animationScript, AnimProps.GRID_PROPS);

		// Write values in first row
		for(int j=0; j<demand.length; j++){
			x.setLabel(j, 0, ""+demand[j]);
		}
		
		myDrawingUtils.drawTextWithMinorBox(new OffsetCoords(bGridCo, -180, +5),ROW_CAPTION_BOTTOM);
		
		return x;
	}
	
	private int calculateBottomGridPosition(int supplySize){
		int result = 140 + CELL_SIZE * supplySize + 30;
		return result;
	}
	
	public Grid getBasisGrid() {
		return basisGrid;
	}

	public Grid getRightGrid() {
		return rightGrid;
	}
	
	public Grid getBottomGrid() {
		return bottomGrid;
	}

	
}
