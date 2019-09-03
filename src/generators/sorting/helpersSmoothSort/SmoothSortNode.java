package generators.sorting.helpersSmoothSort;

public class SmoothSortNode {

    private int value;

    private SmoothSortNode leftChild = null;
    private SmoothSortNode rightChild = null;

    SmoothSortNode(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    void setValue(int value) {
        this.value = value;
    }

    public SmoothSortNode getLeftChild() {
        return this.leftChild;
    }

    public SmoothSortNode getRightChild() {
        return this.rightChild;
    }

    void setLeftChild(SmoothSortNode node) {
        this.leftChild = node;
    }

    void setRightChild(SmoothSortNode node) {
        this.rightChild = node;
    }
}
