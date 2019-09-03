package generators.tree.helpersLeonardoHeap;

import algoanim.primitives.Circle;

public class LeonardoHeapNode {

    private int value;

    private LeonardoHeapNode leftChild = null;
    private LeonardoHeapNode rightChild = null;

    public Circle circle;

    LeonardoHeapNode(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    void setValue(int value) {
        this.value = value;
    }

    public LeonardoHeapNode getLeftChild() {
        return this.leftChild;
    }

    public LeonardoHeapNode getRightChild() {
        return this.rightChild;
    }

    void setLeftChild(LeonardoHeapNode node) {
        this.leftChild = node;
    }

    void setRightChild(LeonardoHeapNode node) {
        this.rightChild = node;
    }
}
