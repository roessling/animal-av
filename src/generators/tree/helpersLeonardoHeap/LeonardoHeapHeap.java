package generators.tree.helpersLeonardoHeap;

import algoanim.properties.AnimationPropertiesKeys;
import algoanim.util.Coordinates;
import generators.tree.LeonardoHeapGenerator;

public class LeonardoHeapHeap {

    private LeonardoHeapNode root;

    private int numberOfNodes = 0;

    private int numberOfMerges = 0;

    public LeonardoHeapHeap(int value) {
        this.root = new LeonardoHeapNode(value);
        this.numberOfNodes++;
    }

    public int getOrder() {
        // Tree with only one generators.tree.LeonardoHeap.LeonardoHeapNode has order of 0
        /*for (int i = 0; i < numberOfNodes; i++) {
            int lowerPower = (int) Math.pow(2, i);
            int higherPower = (int) Math.pow(2, i + 1);
            if (numberOfNodes >= lowerPower && numberOfNodes < higherPower) {
                return i;
            }
        }
        return -1;*/
        return numberOfMerges;
    }

    void mergeHeaps(LeonardoHeapHeap heap, int value) {
        LeonardoHeapNode node = new LeonardoHeapNode(value);

        numberOfNodes = numberOfNodes + heap.numberOfNodes + 1;

        // set the children
        node.setLeftChild(this.root);
        node.setRightChild(heap.getRoot());
        LeonardoHeapGenerator.leoHeap.getHeapList().remove(LeonardoHeapGenerator.leoHeap.getHeapList().size() - 1);

        // set the count

        this.root = node;

        numberOfMerges++;

        int orderFirstHeap = LeonardoHeapGenerator.leoHeap.getHeapList().get(0).getOrder();
        LeonardoHeapGenerator.variables.set("TiefeDesErstenHeaps", Integer.toString(orderFirstHeap));

        this.sortHeap();
    }

    void sortHeap() {
        int heapWidth = LeonardoHeapGenerator.getHeapWidth(LeonardoHeapGenerator.leoHeap.getHeapList().get(0));
        LeonardoHeapGenerator.rootCoords = new Coordinates(350 + heapWidth / 2, LeonardoHeapGenerator.rootCoords.getY());
        sortHeapRecursive(this.root);
        LeonardoHeapGenerator.threeCases.unhighlight("thirdCase");
    }

    private void sortHeapRecursive(LeonardoHeapNode root) {

        // if both children are not null check the heap
        if (root.getLeftChild() != null && root.getRightChild() != null) {
            if (root.getLeftChild().getValue() > root.getValue()) {
                LeonardoHeapGenerator.drawLeoHeap(LeonardoHeapGenerator.leoHeap.getHeapList(), LeonardoHeapGenerator.rootCoords);
                LeonardoHeapGenerator.lang.nextStep();
                LeonardoHeapGenerator.threeCases.unhighlight("firstCase");
                LeonardoHeapGenerator.threeCases.unhighlight("firstCase1");
                LeonardoHeapGenerator.threeCases.highlight("thirdCase");
                //LeonardoHeapGeneratorOLD.lang.nextStep();
                root.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, LeonardoHeapGenerator.selectColor, null, null);
                root.getLeftChild().circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, LeonardoHeapGenerator.selectColor, null, null);
                LeonardoHeapGenerator.lang.nextStep();
                // swap values and call recursive with left child
                int tmp = root.getValue();
                root.setValue(root.getLeftChild().getValue());
                root.getLeftChild().setValue(tmp);
                LeonardoHeapGenerator.drawLeoHeap(LeonardoHeapGenerator.leoHeap.getHeapList(), LeonardoHeapGenerator.rootCoords);
                root.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, LeonardoHeapGenerator.switchColor, null, null);
                root.getLeftChild().circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, LeonardoHeapGenerator.switchColor, null, null);
                LeonardoHeapGenerator.lang.nextStep();
                sortHeapRecursive(root.getLeftChild());
            }
            if (root.getRightChild().getValue() > root.getValue()) {
                LeonardoHeapGenerator.drawLeoHeap(LeonardoHeapGenerator.leoHeap.getHeapList(), LeonardoHeapGenerator.rootCoords);
                LeonardoHeapGenerator.lang.nextStep();
                LeonardoHeapGenerator.threeCases.unhighlight("firstCase");
                LeonardoHeapGenerator.threeCases.unhighlight("firstCase1");
                LeonardoHeapGenerator.threeCases.highlight("thirdCase");
                //LeonardoHeapGeneratorOLD.lang.nextStep();
                root.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, LeonardoHeapGenerator.selectColor, null, null);
                root.getRightChild().circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, LeonardoHeapGenerator.selectColor, null, null);
                LeonardoHeapGenerator.lang.nextStep();
                // swap values and call recursive with right child
                int tmp = root.getValue();
                root.setValue(root.getRightChild().getValue());
                root.getRightChild().setValue(tmp);
                LeonardoHeapGenerator.drawLeoHeap(LeonardoHeapGenerator.leoHeap.getHeapList(), LeonardoHeapGenerator.rootCoords);
                root.circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, LeonardoHeapGenerator.switchColor, null, null);
                root.getRightChild().circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, LeonardoHeapGenerator.switchColor, null, null);
                LeonardoHeapGenerator.lang.nextStep();
                sortHeapRecursive(root.getRightChild());
            }
        }
    }

    public LeonardoHeapNode getRoot() {
        return root;
    }

}
