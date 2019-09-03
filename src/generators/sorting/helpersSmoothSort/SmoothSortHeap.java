package generators.sorting.helpersSmoothSort;

public class SmoothSortHeap {

    private SmoothSortNode root;

    private int numberOfNodes = 0;

    private int numberOfMerges = 0;

    public SmoothSortHeap(int value) {
        this.root = new SmoothSortNode(value);
        this.numberOfNodes++;
    }

    SmoothSortHeap(SmoothSortNode node) {
        this.root = node;
        SmoothSortNode currentNode = node;
        while (currentNode.getLeftChild() != null) {
            numberOfMerges++;
            currentNode = currentNode.getLeftChild();
        }
    }

    public int getOrder() {
        // Tree with only one SmoothSortNode has order of 0
        return numberOfMerges;
    }

    void mergeHeaps(SmoothSortHeap heap, int value) {
        SmoothSortNode node = new SmoothSortNode(value);

        numberOfNodes = numberOfNodes + heap.numberOfNodes + 1;

        // set the children
        node.setLeftChild(this.root);
        node.setRightChild(heap.getRoot());

        // set the count

        this.root = node;

        numberOfMerges++;

        this.sortHeap();
    }

    void sortHeap() {
        sortHeapRecursive(this.root);
    }

    private void sortHeapRecursive(SmoothSortNode root) {

        // if both children are not null check the heap
        if (root.getLeftChild() != null && root.getRightChild() != null) {
            if (root.getLeftChild().getValue() > root.getValue()) {
                // swap values and call recursive with left child
                int tmp = root.getValue();
                root.setValue(root.getLeftChild().getValue());
                root.getLeftChild().setValue(tmp);
                sortHeapRecursive(root.getLeftChild());
            }
            if (root.getRightChild().getValue() > root.getValue()) {
                // swap values and call recursive with right child
                int tmp = root.getValue();
                root.setValue(root.getRightChild().getValue());
                root.getRightChild().setValue(tmp);
                sortHeapRecursive(root.getRightChild());
            }
        }
    }

    public SmoothSortNode getRoot() {
        return root;
    }

    int getNumberOfNodes() {
        return numberOfNodes;
    }
}
