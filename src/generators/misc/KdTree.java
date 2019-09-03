package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.kdTree.Description;
import generators.helpers.kdTree.Title;
import generators.helpers.kdTree.VisualKdTree;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;

public class KdTree implements Generator {

	private Language lang;

	public KdTree() {
		this.lang = new AnimalScript("kd-tree",
				"Christian Brinker & Mateusz Parzonka", 640, 480);
		this.lang.setStepMode(true);
	}

	/**
	 * @see generators.framework.Generator#generate(generators.framework.properties.AnimationPropertiesContainer,
	 *      java.util.Hashtable)
	 */
	@Override
	public String generate(AnimationPropertiesContainer animProps,
			Hashtable<String, Object> primProps) {

		new Title(this.lang);
		Description description = new Description(this.lang);

		this.lang.nextStep();
		description.hide();

		VisualKdTree kdtree = new VisualKdTree(this.lang, animProps, primProps);
		kdtree.buildTree(8);
		kdtree.findNN(7, 4);

		return this.lang.toString();
	}

	/**
	 * @see generators.framework.Generator#getAlgorithmName()
	 */
	@Override
	public String getAlgorithmName() {
		return "kd-tree";
	}

	/**
	 * @see generators.framework.Generator#getAnimationAuthor()
	 */
	@Override
	public String getAnimationAuthor() {
		return "Christian Brinker, Mateusz Parzonka";
	}

	/**
	 * @see generators.framework.Generator#getCodeExample()
	 */
	@Override
	public String getCodeExample() {
		return "0. We start with a list of 2d-points. Then we build up a 2d-tree sorting the list by a coordinate and " +
				"\n   inserting the median point into the tree: it goes right or left depending on whether the point is "
				+ "\n   greater or less than the current node in the split dimension." +  
				"\n1. When we want to find the nearest neighbour of a point, we start with the root node. The algorithm moves"
				+ "\n   down the tree recursively, in the same way that it would if the search point were being inserted."
				+ "\n2. Once the algorithm reaches a leaf node, it saves that node point as the \"current best\""
				+ "\n3. The algorithm unwinds the recursion of the tree, performing the following steps at each node:"
				+ "\n  3.1. If the current node is closer than the current best, then it becomes the current best."
				+ "\n  3.2. The algorithm checks whether there could be any points, on the other side of the splitting plane"
				+ "\n       that are closer to the search point than the current best. In concept, this is done by intersecting"
				+ "\n       the splitting hyperplane with a hypersphere around the search point that has a radius equal to"
				+ "\n       the current nearest distance. Since the hyperplanes are all axis-aligned this is implemented as"
				+ "\n       a simple comparison to see whether the difference between the splitting coordinate of the"
				+ "\n       search point and current node is less than the distance (overall coordinates) from the search"
				+ "\n       point to the current best."
				+ "\n    3.2.1. If the hypersphere crosses the plane, there could be nearer points on the other side of the"
				+ "\n           plane, so the algorithm must move down the other branch of the tree from the current"
				+ "\n           node looking for closer points, following the same recursive process as the entire search."
				+ "\n    3.2.2. If the hypersphere doesn't intersect the splitting plane, then the algorithm continues"
				+ "\n           walking up the tree, and the entire branch on the other side of that node is eliminated."
				+ "\n4. When the algorithm finishes this process for the root node, then the search is complete."
		        + "\n\n                                               (adapted from: http://en.wikipedia.org/wiki/Kd-tree)";

	}

	/**
	 * @see generators.framework.Generator#getContentLocale()
	 */
	@Override
	public Locale getContentLocale() {
		return Locale.US;
	}

	/**
	 * @see generators.framework.Generator#getDescription()
	 */
	@Override
	public String getDescription() {
		return "In computer science, a kd-tree (short for k-dimensional tree) is a space-partitioning "
				+ "data structure for organizing \npoints in a k-dimensional space. kd-trees are a "
				+ "useful data structure for several applications, such as searches \ninvolving a "
				+ "multidimensional search key (e.g. range searches and nearest neighbor searches). "
				+ "kd-trees are a special \ncase of BSP trees. In our generator we support animations "
				+ "of the special case of an 2d-tree, which models points with two dimensions\nThe"
				+ " nearest neighbor (NN) algorithm aims to find the point in the tree which is nearest "
				+ "to a given input point. \nThis search can be done efficiently by using the tree "
				+ "properties to quickly eliminate large portions of the search space. \nSearching "
				+ "for a nearest neighbor in a kd-tree proceeds as follows:";
	}

	/**
	 * @see generators.framework.Generator#getFileExtension()
	 */
	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	/**
	 * @see generators.framework.Generator#getGeneratorType()
	 */
	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	/**
	 * @see generators.framework.Generator#getName()
	 */
	@Override
	public String getName() {
		return "kd-tree (with k=2 and 1NN-search)";
	}

	/**
	 * @see generators.framework.Generator#getOutputLanguage()
	 */
	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	/**
	 * @see generators.framework.Generator#init()
	 */
	@Override
	public void init() {
		// nothing to be done here
	}

}
