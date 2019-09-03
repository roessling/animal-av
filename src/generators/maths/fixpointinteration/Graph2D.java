package generators.maths.fixpointinteration;

import generators.maths.fixpointinteration.mathterm.Term;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.ArrayList;

import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class Graph2D {
	private final Language lang;
	private final Utility util;

	// legend
	private final ArrayList<GraphLegendEntry> legend = new ArrayList<>();
	private final ArrayList<Primitive> legendPrimitives = new ArrayList<>();
	private int legendEntryHeight = 20;
	private int topMargin = 5;
	private int leftMargin = 10;
	private int innerMargin = 10;
	private int colorLineLength = 15;
	private RectProperties legendRectangleProperties = new RectProperties();
	private TextProperties legendTextProperties = new TextProperties();

	// graph properties
	private final int posX;
	private final int posY;
	private final int originX;
	private final int originY;
	private final int width;
	private final int height;
	private final int pixelPerCm;

	private double evalStepSize = 0.05;

	// calculated graph area
	private double minX = 0;
	private double maxX = 0;
	private double maxY = 0;
	private double minY = 0;

	// arrow for coordinate system
	private int arrowLength = 10;
	private int arrowWidth = 5;

	// diagonal line
	private int segmentLength = 10;
	private int gap = 15;

	public Graph2D(Language l, Utility u, int positionX, int positionY,
			int originX, int originY, int width, int height, int scaling) {
		lang = l;
		util = u;
		this.posX = positionX;
		this.posY = positionY;
		this.originX = originX;
		this.originY = originY;
		this.width = width;
		this.height = height;
		this.pixelPerCm = scaling;
	}
	

	public void drawCoordinateSystem(double markerStep, double numberStep) {
		// draw axes
		util.drawLine(posX, posY + originY, posX + width, posY + originY);
		util.drawLine(posX + originX, posY, posX + originX, posY + height);
		new Coordinates(posX + originX, posY + height);

		// draw arrows
		TriangleProperties arrowProperties = new TriangleProperties();
		arrowProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		lang.newTriangle(new Coordinates(posX + width, posY + originY),
				new Coordinates(posX + width - arrowLength, posY + originY
						- arrowWidth), new Coordinates(posX + width
						- arrowLength, posY + originY + arrowWidth), "arrow-x",
				null, arrowProperties);
		lang.newTriangle(
				new Coordinates(posX + originX, posY),
				new Coordinates(posX + originX - arrowWidth, posY + arrowLength),
				new Coordinates(posX + originX + arrowWidth, posY + arrowLength),
				"arrow-y", null, arrowProperties);

		// draw labels
		util.drawText("x", posX + width, posY + originY);
		util.drawText("y", posX + originX - 15, posY - 8);

		// draw numbers and markers
		int markerStepPixel = (int) (Math.floor(pixelPerCm * markerStep));

		int currentX = posX + originX + markerStepPixel;
		BigDecimal number = new BigDecimal(0);
		while (currentX < posX + width) {
			number = number.add(BigDecimal.valueOf(markerStep))
					.stripTrailingZeros();
			if (number.remainder(BigDecimal.valueOf(numberStep)).abs()
					.compareTo(BigDecimal.valueOf(0.001)) < 0) {
				// show number
				drawMarkerDiag(true, number.toPlainString(), currentX, posY
						+ originY);
			} else {
				drawMarkerDiag(false, number.toPlainString(), currentX, posY
						+ originY);
			}
			currentX += markerStepPixel;
		}
		maxX = number.doubleValue();

		currentX = posX + originX - markerStepPixel;
		number = new BigDecimal(0);
		while (currentX > posX) {
			number = number.subtract(BigDecimal.valueOf(markerStep))
					.stripTrailingZeros();
			if (number.remainder(BigDecimal.valueOf(numberStep)).abs()
					.compareTo(BigDecimal.valueOf(0.001)) < 0) {
				// show number
				drawMarkerDiag(true, number.toPlainString(), currentX, posY
						+ originY);
			} else {
				drawMarkerDiag(false, number.toPlainString(), currentX, posY
						+ originY);
			}
			currentX -= markerStepPixel;
		}
		minX = number.doubleValue();
		
		int currentY = posY + originY - markerStepPixel;
		number = new BigDecimal(0);
		while (currentY > posY) {
			number = number.add(BigDecimal.valueOf(markerStep))
					.stripTrailingZeros();
			if (number.remainder(BigDecimal.valueOf(numberStep)).abs()
					.compareTo(BigDecimal.valueOf(0.001)) < 0) {
				// show number
				drawMarkerHor(true, number.toPlainString(), posX + originX,
						currentY);
			} else {
				drawMarkerHor(false, number.toPlainString(), posX + originX,
						currentY);
			}
			currentY -= markerStepPixel;
		}
		maxY = number.doubleValue();

		currentY = posY + originY + markerStepPixel;
		number = new BigDecimal(0);
		while (currentY < posY + height) {
			number = number.subtract(BigDecimal.valueOf(markerStep))
					.stripTrailingZeros();
			if (number.remainder(BigDecimal.valueOf(numberStep)).abs()
					.compareTo(BigDecimal.valueOf(0.001)) < 0) {
				// show number
				drawMarkerHor(true, number.toPlainString(), posX + originX,
						currentY);
			} else {
				drawMarkerHor(false, number.toPlainString(), posX + originX,
						currentY);
			}
			currentY += markerStepPixel;
		}
		minY = number.doubleValue();

	}

	public void drawDiagonalLine() {
		PolylineProperties lineProperties = new PolylineProperties();
		lineProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		drawDiagonalLine(lineProperties);
	}
	
	public void drawDiagonalLine(PolylineProperties lineProperties) {
		int x1 = posX;
		int y1 = posY + height;
		int x2 = posX + width;
		int y2 = posY;

		int currentX = posX;
		while (currentX < posX + width) {
			util.drawLine(currentX, calcPointOnLine(currentX, x1, y1, x2, y2),
					currentX + segmentLength,
					calcPointOnLine(currentX + segmentLength, x1, y1, x2, y2),
					lineProperties);
			currentX += gap;
		}
	}

	private int calcPointOnLine(int x, int x1, int y1, int x2, int y2) {
		int m = (y2 - y1) / (x2 - x1);
		return m * (x - x1) + y1;
	}

	public void drawFunction(Term f) {
		PolylineProperties lineProperties = new PolylineProperties();
		lineProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		drawFunction(f, lineProperties);
	}
	
	public void drawFunction(Term f, PolylineProperties lineProperties) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		int prevPixelX = -1;
		int prevPixelY = -1;
		Double x = (double) minX;
		while (x <= maxX) {
			Double y = f.evaluate(x);
			if (y >= minY && y <= maxY) {
				int pixelX = xToPixelPos(x);
				int pixelY = yToPixelPos(y);
				if (pixelX != prevPixelX || pixelY != prevPixelY) {
					nodes.add(new Coordinates(pixelX, pixelY));
					prevPixelX = pixelX;
					prevPixelY = pixelY;
				}
			}
			x += evalStepSize;
		}
		Node[] nodeArray = new Node[nodes.size()];
		nodes.toArray(nodeArray);
		lang.newPolyline(nodeArray, "function", null, lineProperties);
	}
	
	public void drawFunctionDotted(Term f, PolylineProperties lineProperties) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		int prevPixelX = -1;
		int prevPixelY = -1;
		Double x = (double) minX;
		int counter = 0;
		while (x <= maxX) {
			Double y = f.evaluate(x);
			if (y >= minY && y <= maxY) {
				int pixelX = xToPixelPos(x);
				int pixelY = yToPixelPos(y);
				if (pixelX != prevPixelX || pixelY != prevPixelY) {
					nodes.add(new Coordinates(pixelX, pixelY));
					prevPixelX = pixelX;
					prevPixelY = pixelY;
				}
			}
			x += evalStepSize;
			counter++;
			if (counter == segmentLength) {
				Node[] nodeArray = new Node[nodes.size()];
				nodes.toArray(nodeArray);
				nodes.clear();
				lang.newPolyline(nodeArray, "function", null, lineProperties);
				x += evalStepSize*gap;
				counter = 0;
			}
		}
	}

	public void addLegendEntry(Color color, String name) {
		legend.add(new GraphLegendEntry(color, name));
	}

	public void updateLegend(int x, int y, int width) {
		// hide old legend
		for (Primitive p : legendPrimitives) {
			p.hide();
		}
		legendPrimitives.clear();
		// draw new legend
		legendPrimitives.add(lang.newRect(new Coordinates(x, y),
				new Coordinates(x + width, y
						+ (legendEntryHeight * legend.size()) + 10),
				"legendBox", null, legendRectangleProperties));
		int currentY = y;
		for (GraphLegendEntry e: legend) {
			PolylineProperties lineColor = new PolylineProperties();
			lineColor.set(AnimationPropertiesKeys.COLOR_PROPERTY, e.getColor());
			legendPrimitives.add(util.drawLine(x + leftMargin, currentY+7+topMargin, x + leftMargin + colorLineLength, currentY+7+topMargin, lineColor));
			legendPrimitives.add(util.drawText(e.getName(), x + leftMargin + colorLineLength + innerMargin , currentY+topMargin, legendTextProperties)[0]);
			currentY += legendEntryHeight;
		}
	}

	public Polyline drawLine(double x1, double y1, double x2, double y2,
			PolylineProperties properties) {
		return util.drawLine(xToPixelPos(x1), yToPixelPos(y1), xToPixelPos(x2),
				yToPixelPos(y2), properties);
	}

	public Polyline drawLine(double x1, double y1, double x2, double y2) {
		return util.drawLine(xToPixelPos(x1), yToPixelPos(y1), xToPixelPos(x2),
				yToPixelPos(y2));
	}

	private int xToPixelPos(Double x) {
		return (int) (posX + originX + (x * pixelPerCm));
	}
	
	private double pixelPosToX(int pixelPos) {
		return (double) (pixelPos - posX - originX)/pixelPerCm;
	}
	

	private int yToPixelPos(Double y) {
		return (int) (posY + originY - (y * pixelPerCm));
	}
	
	private double pixelPosToY(int pixelPos) {
		return (double) (pixelPos - posY - originY)/pixelPerCm;
	}

	private void drawMarkerDiag(boolean showNumber, String number, int x, int y) {
		int markerLength;
		if (showNumber) {
			markerLength = 4;
			TextProperties textProps = new TextProperties();
			textProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
			util.drawText(number, x, y + markerLength, textProps);
		} else {
			markerLength = 2;
		}
		util.drawLine(x, y - markerLength, x, y + markerLength);
	}

	private void drawMarkerHor(boolean showNumber, String number, int x, int y) {
		int markerLength;
		if (showNumber) {
			markerLength = 4;
			TextProperties textProps = new TextProperties();
			textProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
			util.drawText(number, x - 13, y - 6, textProps);
		} else {
			markerLength = 2;
		}
		util.drawLine(x - markerLength, y, x + markerLength, y);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getPosX() {
		return posX;
	}
	
	public int getPosY() {
		return posY;
	}
	
	public void setLegendEntryHeight(int legendEntryHeight) {
		this.legendEntryHeight = legendEntryHeight;
	}

	public void setLegendTopMargin(int topMargin) {
		this.topMargin = topMargin;
	}

	public void setLegendLeftMargin(int leftMargin) {
		this.leftMargin = leftMargin;
	}

	public void setLegendInnerMargin(int innerMargin) {
		this.innerMargin = innerMargin;
	}

	public void setLegendColorLineLength(int colorLineLength) {
		this.colorLineLength = colorLineLength;
	}

	public void setLegendRectangleProperties(
			RectProperties legendRectangleProperties) {
		this.legendRectangleProperties = legendRectangleProperties;
	}

	public void setLegendTextProperties(TextProperties legendTextProperties) {
		this.legendTextProperties = legendTextProperties;
	}

	public void setGraphArrowLength(int arrowLength) {
		this.arrowLength = arrowLength;
	}

	public void setGraphArrowWidth(int arrowWidth) {
		this.arrowWidth = arrowWidth;
	}

	public void setDLSegmentLength(int segmentLength) {
		this.segmentLength = segmentLength;
	}

	public void setDLGap(int gap) {
		this.gap = gap;
	}
	
	public void setEvaluationStepsize(double stepSize) {
		evalStepSize = stepSize;
	}

}
