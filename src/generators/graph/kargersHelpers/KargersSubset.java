package generators.graph.kargersHelpers;

public class KargersSubset {
    public int parent;
    public int rank;

    public KargersSubset() {
    }

    // constructor of subset
    public KargersSubset(int parent, int rank) {
        this.parent = parent;
        this.rank = rank;
    }

    // find the subset of an element i
    // return only the root node of the subset in which the node is in
    public int find(KargersSubset[] subsetArray, int i) {
        if (subsetArray[i].parent != i) {
            subsetArray[i].parent = find(subsetArray, subsetArray[i].parent);
        }
        return subsetArray[i].parent;
    }

    // union of two subsets (union by rank means, that the
    // "heavier" one becomes the parent of the other)
    public void union(KargersSubset[] subsetArray, int x, int y) {
        int rootOfX = find(subsetArray, x);
        int rootOfY = find(subsetArray, y);

        //do union by rank
        if (subsetArray[rootOfX].rank < subsetArray[rootOfY].rank) {
            subsetArray[rootOfX].parent = rootOfY;
        }
        else if (subsetArray[rootOfX].rank > subsetArray[rootOfY].rank) {
            subsetArray[rootOfY].parent = rootOfX;
        }

        // if ranks are same, choose one as root and increment its rank
        else {
            subsetArray[rootOfY].parent = rootOfX;
            subsetArray[rootOfY].rank++;
        }
    }
}
