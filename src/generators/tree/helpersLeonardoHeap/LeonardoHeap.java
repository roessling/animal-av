package generators.tree.helpersLeonardoHeap;

import algoanim.properties.AnimationPropertiesKeys;
import generators.tree.LeonardoHeapGenerator;

import java.util.ArrayList;
import java.util.List;

public class LeonardoHeap {

    private List<LeonardoHeapHeap> heapList = new ArrayList<>();

    /**
     * Adds a heap to the Leonardo heap. We are adding a heap, because a single node can also be a heap.
     *
     * @param heap  The given heap that is going to be added.
     */
    public void addHeap(LeonardoHeapHeap heap) {
        if (heapList.size() > 1 && (heapList.get(heapList.size() - 2).getOrder() - heapList.get(heapList.size() - 1).getOrder()) <= 1) {
            LeonardoHeapGenerator.variables.set("AnzahlDerHeaps", Integer.toString(Integer.parseInt(LeonardoHeapGenerator.variables.get("AnzahlDerHeaps")) - 1));
            LeonardoHeapGenerator.threeCases.highlight("firstCase");
            LeonardoHeapGenerator.threeCases.highlight("firstCase1");
            // merge the last and the heap before
            heapList.get(heapList.size() - 2).mergeHeaps(heapList.get(heapList.size() - 1), heap.getRoot().getValue());
            // last heap is in merge function removed
        } else {
            LeonardoHeapGenerator.variables.set("AnzahlDerHeaps", Integer.toString(Integer.parseInt(LeonardoHeapGenerator.variables.get("AnzahlDerHeaps")) + 1));
            LeonardoHeapGenerator.threeCases.highlight("secondCase");
            heapList.add(heap);
        }
    }

    public void sortHeapListByRoot(List<LeonardoHeapHeap> heapList) {
        for (int j = heapList.size() - 1; j >= 0; j--) {
            int currentMax = j;

            // get the highest root of all heaps
            for (int i = 0; i < j; i++) {
                if (heapList.get(j).getRoot().getValue() < heapList.get(i).getRoot().getValue()) {
                    currentMax = i;
                }
            }

            // only swap the roots if the maximum value is not already the most right element
            if (currentMax != j) {
                LeonardoHeapGenerator.threeCases.highlight("thirdCase");
                // swap the largest and the last value of the heap list
                int lastValue = heapList.get(j).getRoot().getValue();
                int largestValue = heapList.get(currentMax).getRoot().getValue();
                heapList.get(j).getRoot().setValue(largestValue);
                heapList.get(currentMax).getRoot().setValue(lastValue);
                // highlight the nodes
                heapList.get(currentMax).getRoot().circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, LeonardoHeapGenerator.selectColor, null, null);
                heapList.get(j).getRoot().circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, LeonardoHeapGenerator.selectColor, null, null);
                LeonardoHeapGenerator.lang.nextStep();
                LeonardoHeapGenerator.drawLeoHeap(LeonardoHeapGenerator.leoHeap.getHeapList(), LeonardoHeapGenerator.rootCoords);
                heapList.get(currentMax).getRoot().circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, LeonardoHeapGenerator.switchColor, null, null);
                heapList.get(j).getRoot().circle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, LeonardoHeapGenerator.switchColor, null, null);
                LeonardoHeapGenerator.lang.nextStep();
                // sort both heaps
                heapList.get(currentMax).sortHeap();
                heapList.get(j).sortHeap();
                LeonardoHeapGenerator.drawLeoHeap(LeonardoHeapGenerator.leoHeap.getHeapList(), LeonardoHeapGenerator.rootCoords);
                LeonardoHeapGenerator.lang.nextStep();
            }
        }
    }

    public List<LeonardoHeapHeap> getHeapList() {
        return heapList;
    }
}
