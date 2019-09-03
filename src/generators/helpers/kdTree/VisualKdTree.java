package generators.helpers.kdTree;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.kdTree.PointLists.Point;

import java.util.Hashtable;
import java.util.Random;

import algoanim.primitives.generators.Language;

/**
 * @author mateusz
 */
public class VisualKdTree {

    private Graph graph;
    private KDNode kdtree;
    private Language lang;
    private AnimationPropertiesContainer animProps;
    private Message message;
    private Hashtable<String, Object> primProps;

    private boolean recursive = false;

    /**
     * This constructor instantiates a VisualKdTree object.
     * 
     * @param primProps
     */
    public VisualKdTree(Language lang, AnimationPropertiesContainer animProps,
	    Hashtable<String, Object> primProps) {
	super();
	this.lang = lang;
	this.animProps = animProps;
	this.primProps = primProps;
	this.graph = new Graph(lang, animProps);
	this.message = new Message(this.lang);

    }

    /**
     * The internal point list has the form int[nrOfPoints][2]
     * 
     * @param nrOfPoints
     *            the number of points that is generated.
     */
    public void buildTree(int nrOfPoints) {

	int[][] pointList;
	if ((this.primProps == null)
		|| (Boolean) this.primProps
			.get("flag for random generation of points"))
	    pointList = createRandomPointList(nrOfPoints);
	else
	    pointList = (int[][]) this.primProps.get("list of 2d-points");

	VisualPointLists vp = new VisualPointLists(pointList, this.animProps,
		this.lang);

	// create root
	Point root = vp.getNextPoint();
	this.kdtree = KDNode.createRoot(root.x, root.y);
	updateMessage("Create root.");
	this.graph.drawGraph(this.kdtree);

	Point point;
	while ((point = vp.getNextPoint()) != null) {
	    this.graph.getCircleInsertion().show();
	    insertInTree(this.kdtree, point);
	    this.graph.drawGraph(this.kdtree);
	}
	vp.hide();

    }

    /**
     * @param root
     * @param nextPoint
     */
    private void insertInTree(KDNode kdtree, Point nextPoint) {
	boolean nextPointBigger;
	String plane;
	int p0;
	int p1;

	if (kdtree.isXplane()) {
	    nextPointBigger = (nextPoint.x >= kdtree.xValue);
	    plane = "x";
	    p0 = kdtree.xValue;
	    p1 = nextPoint.x;

	} else {
	    nextPointBigger = (nextPoint.y >= kdtree.yValue);
	    plane = "y";
	    p0 = kdtree.yValue;
	    p1 = nextPoint.y;
	}

	updateMessage("Inserting point " + nextPoint
		+ " into the tree. Comparing " + plane + "-values. "
		+ "Checking if " + p1 + " is greater than or equal to " + p0);

	if (nextPointBigger)
	    if (kdtree.right == null) {
		kdtree.createRightChild(nextPoint.x, nextPoint.y);
		this.graph.getCircleInsertion().hide();
		// this.lang.nextStep();
		updateMessage("Inserting point " + nextPoint
			+ " into the tree. " + p1
			+ " is greater than or equal to " + p0
			+ ", creating new leaf node on right side.");
	    }

	    else {
		updateMessage("Inserting point " + nextPoint
			+ " into the tree. " + p1
			+ " is greater than or equal to " + p0
			+ ", descending tree to the right side.");
		highlight(kdtree, kdtree.right);
		insertInTree(kdtree.right, nextPoint);
	    }
	else {

	    if (kdtree.left == null) {
		kdtree.createLeftChild(nextPoint.x, nextPoint.y);
		this.graph.getCircleInsertion().hide();
		// this.lang.nextStep();
		updateMessage("Inserting point " + nextPoint
			+ " into the tree. " + p1 + " is smaller than " + p0
			+ ", creating new leaf node on left side.");
	    } else {
		updateMessage("Inserting point " + nextPoint
			+ " into the tree. " + p1 + " is smaller than " + p0
			+ ", descending tree to the left side.");
		highlight(kdtree, kdtree.left);
		insertInTree(kdtree.left, nextPoint);
	    }
	}

    }

    /**
     * Finds the nearest leaf.
     * 
     * @param root
     * @param point
     */
    private KDNode findNearestLeaf(KDNode kdtree, Point point) {
	boolean nextPointBigger;
	String plane;
	int p0;
	int p1;

	if (kdtree.isXplane()) {
	    nextPointBigger = (point.x >= kdtree.xValue);
	    plane = "x";
	    p0 = kdtree.xValue;
	    p1 = point.x;

	} else {
	    nextPointBigger = (point.y >= kdtree.yValue);
	    plane = "y";
	    p0 = kdtree.yValue;
	    p1 = point.y;
	}

	updateMessage((this.recursive ? "Recursive descent in subtree with root "
		+ kdtree + ". "
		: "")
		+ "Looking for nearest leaf of "
		+ point
		+ " comparing "
		+ plane
		+ "-values. "
		+ "Checking if "
		+ p1
		+ " is greater than or equal to " + p0 + ".");

	if (nextPointBigger)
	    if (kdtree.right == null) {
		if (!this.recursive)
		    updateMessage("Looking for nearest leaf of " + point + ". "
			    + p1 + " is greater than or equal to " + p0
			    + " but no right subtree.");
		if (kdtree.left == null) {
		    if (!this.recursive)
			updateMessage("Looking for nearest leaf of " + point
				+ ". Arrived in leaf");
		    return kdtree;
		} else {
		    if (!this.recursive)
			updateMessage("Looking for nearest leaf of " + point
				+ ", descending tree to the left side.");
		    highlight(kdtree, kdtree.left);
		    return findNearestLeaf(kdtree.left, point);
		}
	    } else {
		if (!this.recursive)
		    updateMessage("Looking for nearest leaf of " + point + ". "
			    + p1 + " is greater than or equal to " + p0
			    + ", descending tree to the right side.");
		highlight(kdtree, kdtree.right);
		return findNearestLeaf(kdtree.right, point);
	    }
	else {
	    if (kdtree.left == null) {
		if (!this.recursive)
		    updateMessage("Looking for nearest leaf of " + point + ". "
			    + p1 + " is smaller than " + p0
			    + " but no left subtree.");
		if (kdtree.right == null) {
		    if (!this.recursive)
			updateMessage("Looking for nearest leaf of " + point
				+ ". Arrived in leaf");
		    return kdtree;
		} else {
		    if (!this.recursive)
			updateMessage("Looking for nearest leaf of " + point
				+ ", descending tree to the right side.");
		    highlight(kdtree, kdtree.right);
		    return findNearestLeaf(kdtree.right, point);
		}
	    } else {
		if (!this.recursive)
		    updateMessage("Looking for nearest leaf of " + point + ". "
			    + p1 + " is smaller than " + p0
			    + ", descending tree to the left side.");
		highlight(kdtree, kdtree.left);
		return findNearestLeaf(kdtree.left, point);
	    }
	}

    }

    private int[][] createRandomPointList(int nrOfPoints) {

	int[][] result = new int[nrOfPoints][2];
	Random random = new Random();
	for (int i = 0; i < nrOfPoints; i++) {
	    result[i][0] = random.nextInt(10);
	    result[i][1] = random.nextInt(10);
	}
	return result;
    }

    private void updateMessage(String message) {
	this.message.setText(message);
	this.lang.nextStep();
    }

    private void highlight(KDNode source, KDNode dest) {
	if (!this.recursive)
	    this.graph.moveCircleInsertion(dest.id, true);
	this.graph.highlightEdge(source.id, dest.id);
    }

    public void findNN(int x, int y) {
	int[] point = new int[2];

	if (this.primProps == null) {
	    point[0] = x;
	    point[1] = y;
	} else {
	    point = (int[]) this.primProps.get("2d-point for NN-search");
	    if (point.length != 2)
		throw new IllegalArgumentException(
			"2d-point for NN-search must consist of 2 components! Given "
				+ point.length);
	}
	updateMessage("Starting search for nearest neighbour of (" + point[0]
		+ ", " + point[1] + ").");
	findNN(this.kdtree, point[0], point[1]);
    }

    public KDNode findNN(KDNode root, int x, int y) {
	KDNode leaf = findNearestLeaf(root, new Point(x, y));
	if (leaf == root)
	    return root;
	KDNode currentBest = updateCurrentBest(leaf, false);
	KDNode node = leaf;
	double dmin = updateDmin(currentBest.distanceToCoordinates(x, y),
		currentBest, x, y);
	do {
	    KDNode sibling = node.getSibling();
	    node = updateNode(node.pred);
	    if (updateEstimation((node.distanceToCoordinates(x, y) < dmin),
		    node, x, y, dmin)) {
		currentBest = updateCurrentBest(node, true);
		dmin = updateDmin(currentBest.distanceToCoordinates(x, y),
			currentBest, x, y);
	    }
	    if ((sibling != null)
		    && updatePlaneDistEstimation(
			    (node.distanceInPlane(x, y) < dmin), node
				    .distanceInPlane(x, y), node, dmin)) {
		this.recursive = true;
		KDNode subTreeBest = updateFindNN(findNN(sibling, x, y),
			sibling, x, y);
		if (updateEstimation(
			(subTreeBest.distanceToCoordinates(x, y) < dmin),
			subTreeBest, x, y, dmin)) {
		    currentBest = updateCurrentBest(subTreeBest, true);
		    dmin = updateDmin(currentBest.distanceToCoordinates(x, y),
			    currentBest, x, y);
		}
		this.graph.hideCircleSubtreeBest(subTreeBest.id);
	    }

	} while (!node.equals(root));

	if (!this.recursive)
	    updateMessage("Reached root node. Algorithm terminated succesfully. Nearest Neighbour of ("
		    + x + ", " + y + ") is " + currentBest + ".");
	else
	    updateMessage("Reached the root node of the subtree. Tracking back.");
	return currentBest;
    }

    /**
     * @param b
     * @param node
     * @param dmin
     * @return
     */
    private boolean updatePlaneDistEstimation(boolean b, double planeDist,
	    KDNode node, double dmin) {
	updateMessage("The distance to the other "
		+ (node.isXplane() ? "x" : "y")
		+ "-plane is "
		+ (int) planeDist
		+ " and "
		+ (b ? "smaller " : "greater or equals ")
		+ "than the distance to the nearest neighbour. "
		+ (b ? "There could be a nearer neighbour in the sibling-subtree."
			: "There can't be a nearer neighbour in the sibling-subtree."));
	return b;
    }

    private KDNode updateFindNN(KDNode findNNresult, KDNode sibling, int x,
	    int y) {
	this.graph.moveCircleSubtreeBest(findNNresult.id, false);
	this.graph.highlightEdge(sibling.pred.id, sibling.id);
	updateMessage("Recursive descent in subtree yields " + findNNresult
		+ " as nearest neighbour in the subtree with root " + sibling
		+ ".");
	this.recursive = false;
	return findNNresult;
    }

    private boolean updateEstimation(boolean b, KDNode subTreeBest, int x,
	    int y, double dmin) {
	if (!this.recursive) {
	    updateMessage("" + subTreeBest + " is" + (b ? "" : " not")
		    + " nearer to (" + x + ", " + y
		    + ") than the current nearest neighbour.");
	}
	return b;
    }

    private double updateDmin(double distanceToCoordinates, KDNode currentBest,
	    int x, int y) {
	if (!this.recursive) {
	    updateMessage("The distance of the new nearest neighbour is "
		    + distanceToCoordinates + ".");
	}
	return distanceToCoordinates;
    }

    /**
     * @param node
     */
    private KDNode updateNode(KDNode node) {
	if (!this.recursive) {
	    this.graph.moveCircleInsertion(node.id, true);
	    updateMessage("Moving up the tree.");
	}
	return node;
    }

    private KDNode updateCurrentBest(KDNode node, boolean delayed) {
	if (!this.recursive) {
	    this.graph.moveCircleBest(node.id, delayed);
	    updateMessage("Setting new nearest neighbour to " + node.toString()
		    + ".");
	}
	return node;
    }
}
