package generators.helpers.kdTree;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

/**
 * @author mateusz
 */
public class VisualPointLists extends PointLists {

    private StringArray stringArray;
    private Language lang;
    @SuppressWarnings("unused")
    private Graph graph;
    @SuppressWarnings("unused")
    private KDNode kdtree;
    private Text message;
    private Timing textDelay = new TicksTiming(25);

    /**
     * This constructor instantiates a VisualPointLists object.
     * 
     * @param pointList
     */
    public VisualPointLists(int[][] pointList,
	    AnimationPropertiesContainer animProps, Language lang) {
	super(pointList);
	this.lang = lang;
	String startMessage = "Starting with a unsorted list of points.";
	this.stringArray = new StringArray("pointLists", animProps, lang);
	this.message = lang.newText(new Coordinates(300, 400), startMessage,
		"message", null);
    }

    /**
     * @see generators.helpers.kdTree.PointLists#displayCurrentPointLists(generators.helpers.kdTree.PointLists.Point[][],
     *      boolean)
     */
    @Override
    protected void displayCurrentPointLists(Point[][] currentPointListArray,
	    boolean isXplane) {
	String[] result = new String[this.nrOfPoints * 2 + 1];

	int pos = 0;
	boolean first;
	for (Point[] points : currentPointListArray) {
	    result[pos] = (pos == 0) ? "{" : "},     {";
	    pos++;
	    first = true;
	    for (Point p : points) {
		if (!first)
		    result[pos++] = ", ";
		result[pos++] = p.toString();
		first = false;
	    }
	}
	// post roll
	for (int i = 0; i < result.length; i++) {
	    if (result[i] == null)
		result[i] = "";
	}
	result[pos] = "} ";
	this.stringArray.show(result);
	this.lang.nextStep();
    }

    /**
     * @see generators.helpers.kdTree.PointLists#displayExtractedMedian(generators.helpers.kdTree.PointLists.Point,
     *      int, int)
     */
    @Override
    protected void displayExtractedMedian(Point median, int medianIndex,
	    int offset) {
	int index = (offset + medianIndex) * 2 + 1;
	this.stringArray.highlight(index, index);
	this.message.setText("Extracting median point " + median.toString(),
		this.textDelay, null);
	this.lang.nextStep();
    }

    /**
     * @see generators.helpers.kdTree.PointLists#displayAListIsSorted(int,
     *      boolean)
     */
    @Override
    protected void displayAListIsSorted(int currentPointIndex, boolean isXplane) {
	String plane = (isXplane) ? "x" : "y";
	this.message.setText("Sorted list by " + plane + "-value",
		this.textDelay, null);
	// this.lang.nextStep();
    }

    /**
     * @see generators.helpers.kdTree.PointLists#displayProcessingMoreLists()
     */
    @Override
    protected void displayProcessingMoreLists() {
	this.message.setText("All lists processed in this iteration",
		this.textDelay, null);
    }

    /**
     * @see generators.helpers.kdTree.PointLists#displayFinishedGettingPoints()
     */
    @Override
    protected void displayFinishedGettingPoints() {
	this.message.setText("Tree is build", this.textDelay, null);
    }

    /**
     * 
     */
    public void hide() {
	this.stringArray.hide();
	this.message.hide();
    }
}
