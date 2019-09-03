package generators.helpers.kdTree;

import java.awt.Font;

import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Offset;

/**
 * @author mateusz
 */
public class Description {

  algoanim.primitives.SourceCode algoDesc;

  public Description(Language lang) {
    SourceCodeProperties algoDescProps = new SourceCodeProperties(
        "algo_description");
    algoDescProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 12));

    this.algoDesc = lang.newSourceCode(new Offset(0, 20, "title", "SW"),
        "description", null, algoDescProps);

    this.algoDesc
        .addCodeLine(
            "0. We start with a list of 2d-points. Then we build up a 2d-tree sorting the list by a coordinate and",
            null, 0, null);
    this.algoDesc
        .addCodeLine(
            "inserting the median point into the tree: it goes right or left depending on whether the point is",
            null, 1, null);
    this.algoDesc.addCodeLine(
        "greater or less than the current node in the split dimension.", null,
        1, null);
    this.algoDesc
        .addCodeLine(
            "1. When we want to find the nearest neighbour of a point, we start with the root node. The algorithm moves",
            null, 0, null);
    this.algoDesc
        .addCodeLine(
            "down the tree recursively, in the same way that it would if the search point were being inserted.",
            null, 1, null);
    this.algoDesc
        .addCodeLine(
            "2. Once the algorithm reaches a leaf node, it saves that node point as the \\\"current best\\\"",
            null, 0, null);
    this.algoDesc
        .addCodeLine(
            "3. The algorithm unwinds the recursion of the tree, performing the following steps at each node:",
            null, 0, null);
    this.algoDesc
        .addCodeLine(
            "3.1. If the current node is closer than the current best, then it becomes the current best.",
            null, 1, null);
    this.algoDesc
        .addCodeLine(
            "3.2. The algorithm checks whether there could be any points, on the other side of the splitting plane",
            null, 1, null);
    this.algoDesc
        .addCodeLine(
            "that are closer to the search point than the current best. In concept, this is done by intersecting",
            null, 3, null);
    this.algoDesc
        .addCodeLine(
            "the splitting hyperplane with a hypersphere around the search point that has a radius equal to",
            null, 3, null);
    this.algoDesc
        .addCodeLine(
            "the current nearest distance. Since the hyperplanes are all axis-aligned this is implemented as",
            null, 3, null);
    this.algoDesc
        .addCodeLine(
            "a simple comparison to see whether the difference between the splitting coordinate of the",
            null, 3, null);
    this.algoDesc
        .addCodeLine(
            "search point and current node is less than the distance (overall coordinates) from the search",
            null, 3, null);
    this.algoDesc.addCodeLine("point to the current best.", null, 3, null);
    this.algoDesc
        .addCodeLine(
            "3.2.1. If the hypersphere crosses the plane, there could be nearer points on the other side of the",
            null, 2, null);
    this.algoDesc
        .addCodeLine(
            "plane, so the algorithm must move down the other branch of the tree from the current",
            null, 5, null);
    this.algoDesc
        .addCodeLine(
            "node looking for closer points, following the same recursive process as the entire search.",
            null, 5, null);
    this.algoDesc
        .addCodeLine(
            "3.2.2. If the hypersphere doesn't intersect the splitting plane, then the algorithm continues",
            null, 2, null);
    this.algoDesc
        .addCodeLine(
            "walking up the tree, and the entire branch on the other side of that node is eliminated.",
            null, 5, null);
    this.algoDesc
        .addCodeLine(
            "4. When the algorithm finishes this process for the root node, then the search is complete.",
            null, 0, null);
  }

  public void hide() {
    this.algoDesc.hide();
  }

}
