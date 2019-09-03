package generators.graphics.antialias;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Group;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class Grid {
	static int gridID = 0; 

	private int xRightOffset, yTopOffset, xLeftOffset, yBottomOffset;
	private int xStart, xEnd, yStart, yEnd;
	private int xOffset, yOffset;
	private int cellsize = 40;

	private PolylineProperties axisProps, axisHelperProps;
	private TextProperties legendProps, annotationProps;
	private Text xAxisAnnotation, yAxisAnnotation;
	private Coordinates origin;
	private Polyline xAxis, yAxis;
	private Polyline[] xAxisHelp, yAxisHelp;
	private Text[] xLegend, yLegend;
	private Group gridGroup;
	private Language lang;

	/**
	 * 
	 * @param lang
	 *            The lang Object
	 * @param xStart
	 * @param xEnd
	 * @param yStart
	 * @param yEnd
	 * @param cellsize
	 * @param xOffset
	 * @param yOffset
	 */
	public Grid(Language lang, int xStart, int xEnd, int yStart, int yEnd,
			int cellsize, int xOffset, int yOffset) {
		this.lang = lang;
		this.xStart = xStart;
		this.xEnd = xEnd;
		this.yStart = yStart;
		this.yEnd = yEnd;
		this.cellsize = cellsize;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		++gridID;
		calculateOrigin();
	}
	
	public void swapXY(){
		yAxisAnnotation.moveTo(null, "translate", new Offset(cellsize / 4, 0, "xAxis"
				+ gridID, AnimalScript.DIRECTION_E), new TicksTiming(25), new TicksTiming(50));
		xAxisAnnotation.moveTo(null, "translate", new Offset(-cellsize / 4, -cellsize,
				"yAxis" + gridID, AnimalScript.DIRECTION_N), new TicksTiming(25), new TicksTiming(50));
	}

	/**
	 * Get a {@link Rect} that frames the cell at the given x/y coordinates.
	 * 
	 * @param x
	 * @param y
	 * @param rectProps
	 * @param name
	 * @return
	 */
	public Rect getCellAsRect(int x, int y, RectProperties rectProps, String name) {
		return lang.newRect(new Coordinates(origin.getX() + (x - 1) * cellsize,
				origin.getY() - (y - 1) * cellsize),
				new Coordinates(origin.getX() + (x) * cellsize, origin.getY()
						- (y) * cellsize), name, null, rectProps);
	}

	/**
	 * Get the absolute cell {@link Coordinates} for the given x/y coordinates (upper left coordinates).
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Coordinates getUpperLeftCellCoordinates(int x, int y) {
		return new Coordinates(origin.getX() + (x - 1) * cellsize,
				origin.getY() - y * cellsize);
	}

	/**
	 * Get the absolute cell {@link Coordinates} for the given x/y coordinates (center x and center y coordinates).
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Coordinates getCellAsCoordinates(int x, int y) {
		return new Coordinates(origin.getX() + x * cellsize - cellsize / 2,
				origin.getY() - y * cellsize + cellsize / 2);
	}

	public void drawGrid() {
		setProperties();
		calculateOrigin();
		drawMainAxes();
		drawHelpAxes();
		LinkedList<Primitive> primitives = createPrimitiveList();
		gridGroup = lang.newGroup(primitives, "gridGroup" + gridID);
	}

	public void hide() {
		gridGroup.hide();
	}

	public void show() {
		gridGroup.show();
	}

	/**
	 * Calculates the origin coordinates and offsets around the grid
	 * 
	 * @param xOffset
	 *            , yOffset
	 */
	private void calculateOrigin() {
		xRightOffset = Math.max(xStart, xEnd);
		yTopOffset = Math.max(yStart, yEnd);
		xRightOffset = ((xRightOffset < 0) ? 0 : xRightOffset);
		yTopOffset = ((yTopOffset < 0) ? 0 : yTopOffset);
		xLeftOffset = Math.min(xStart, xEnd);
		yBottomOffset = Math.min(yStart, yEnd);
		xLeftOffset = ((xLeftOffset >= 0) ? 0 : Math.abs(xLeftOffset));
		yBottomOffset = ((yBottomOffset >= 0) ? 0 : Math.abs(yBottomOffset));

		// calculate origin:
		origin = new Coordinates(xOffset + xLeftOffset * cellsize, yOffset
				+ cellsize * (yTopOffset + 2));
	}

	/**
	 * 
	 */
	private void setProperties() {
		// create properties for the axis
		axisProps = new PolylineProperties();
		axisProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		axisProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		// create properties for the axis helpers
		axisHelperProps = new PolylineProperties();
		axisHelperProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Color.LIGHT_GRAY);
		axisHelperProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		// create properties for the legend labels
		legendProps = new TextProperties();
		legendProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		legendProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		legendProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, cellsize / 2));

		annotationProps = new TextProperties();
		annotationProps
				.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		annotationProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		annotationProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, cellsize / 2));
	}

	private void drawMainAxes() {
		// draw x axis:
		xAxis = lang.newPolyline(
				new Coordinates[] {
						new Coordinates(origin.getX() - cellsize - xLeftOffset
								* cellsize, origin.getY()),
						new Coordinates(origin.getX() + cellsize
								* (xRightOffset + 1), origin.getY()) }, "xAxis"
						+ gridID, null);
		// draw x axis annotation:
		xAxisAnnotation = lang.newText(new Offset(cellsize / 4, 0, "xAxis"
				+ gridID, AnimalScript.DIRECTION_E), "x", "xAxisAnnotation"
				+ gridID, null, annotationProps);
		// draw y axis:
		yAxis = lang.newPolyline(
				new Coordinates[] {
						new Coordinates(origin.getX(), origin.getY() + cellsize
								+ yBottomOffset * cellsize),
						new Coordinates(origin.getX(), origin.getY() - cellsize
								* (yTopOffset + 1)) }, "yAxis" + gridID, null);
		// draw y axis annotation:
		yAxisAnnotation = lang.newText(new Offset(-cellsize / 4, -cellsize,
				"yAxis" + gridID, AnimalScript.DIRECTION_N), "y",
				"yAxisAnnotation" + gridID, null, annotationProps);
	}

	private void drawHelpAxes() {
		xAxisHelp = new Polyline[yTopOffset + 1 + yBottomOffset];
		yAxisHelp = new Polyline[xRightOffset + 1 + xLeftOffset];

		xLegend = new Text[xRightOffset + 1 + xLeftOffset];
		yLegend = new Text[yTopOffset + 1 + yBottomOffset];

		// x axis helper; y legend:
		for (int y = -yBottomOffset; y < yTopOffset + 1; y++) {
			xAxisHelp[y + yBottomOffset] = lang.newPolyline(
					new Coordinates[] {
							new Coordinates(origin.getX() - (xLeftOffset + 1)
									* cellsize, origin.getY() - y * cellsize),
							new Coordinates(origin.getX() + cellsize
									* (xRightOffset + 1), origin.getY() - y
									* cellsize) }, "xAxisHelper" + gridID + ""
							+ y, null, axisHelperProps);
			yLegend[y + yBottomOffset] = lang.newText(
					new Coordinates(origin.getX() - cellsize + (cellsize / 4),
							origin.getY() - y * cellsize), "" + y, "yLegend"
							+ gridID + "" + y, null, legendProps);
		}

		// y axis helper, x legend:
		for (int x = -xLeftOffset; x < xRightOffset + 1; x++) {
			yAxisHelp[x + xLeftOffset] = lang.newPolyline(new Coordinates[] {
					new Coordinates(origin.getX() + x * cellsize, origin.getY()
							+ cellsize * (yBottomOffset + 1)),
					new Coordinates(origin.getX() + x * cellsize, origin.getY()
							- cellsize * (yTopOffset + 1)) }, "yAxisHelper"
					+ gridID + "" + x, null, axisHelperProps);
			if (x != 0)
				xLegend[x + xLeftOffset] = lang.newText(
						new Coordinates(origin.getX() + cellsize * (x - 1)
								+ (cellsize / 4), origin.getY()), "" + x,
						"xLegend" + gridID + "" + x, null, legendProps);
		}
	}

	private LinkedList<Primitive> createPrimitiveList() {
		LinkedList<Primitive> primitives = new LinkedList<Primitive>();

		primitives.add(xAxis);
		primitives.add(yAxis);
		primitives.add(xAxisAnnotation);
		primitives.add(yAxisAnnotation);

		for (int x = 0; x < xRightOffset + xLeftOffset + 1; x++) {
			if (yAxisHelp[x] != null)
				primitives.add(yAxisHelp[x]);
			if (xLegend[x] != null)
				primitives.add(xLegend[x]);
		}
		for (int y = 0; y < yTopOffset + yBottomOffset + 1; y++) {
			if (xAxisHelp[y] != null)
				primitives.add(xAxisHelp[y]);
			if (yLegend[y] != null)
				primitives.add(yLegend[y]);
		}

		return primitives;
	}

	public Coordinates getOrigin() {
		return origin;
	}

	public static int getGridID() {
		return gridID;
	}

	public int getxStart() {
		return xStart;
	}

	public int getxEnd() {
		return xEnd;
	}

	public int getyStart() {
		return yStart;
	}

	public int getyEnd() {
		return yEnd;
	}

	public int getxOffset() {
		return xOffset;
	}

	public int getyOffset() {
		return yOffset;
	}

	public int getCellsize() {
		return cellsize;
	}

	public Language getLang() {
		return lang;
	}

	public int getxLeftOffset() {
		return xLeftOffset;
	}

	public int getxRightOffset() {
		return xRightOffset;
	}

	public PolylineProperties getAxisProps() {
		return axisProps;
	}

	public void setAxisProps(PolylineProperties axisProps) {
		this.axisProps = axisProps;
	}

	public PolylineProperties getAxisHelperProps() {
		return axisHelperProps;
	}

	public void setAxisHelperProps(PolylineProperties axisHelperProps) {
		this.axisHelperProps = axisHelperProps;
	}

	public TextProperties getLegendProps() {
		return legendProps;
	}

	public void setLegendProps(TextProperties legendProps) {
		this.legendProps = legendProps;
	}

	public TextProperties getAnnotationProps() {
		return annotationProps;
	}

	public void setAnnotationProps(TextProperties annotationProps) {
		this.annotationProps = annotationProps;
	}

	public Group getGridGroup() {
		return gridGroup;
	}

	public void setCellsize(int cellsize) {
		this.cellsize = cellsize;
	}

	public void setOrigin(Coordinates origin) {
		this.origin = origin;
	}

	public void setLang(Language lang) {
		this.lang = lang;
	}
}