package generators.sorting.helpersSmoothSort;

import java.util.ArrayList;
import java.util.List;

public class SmoothSortLeonardoHeap {

    private List<SmoothSortHeap> heapList = new ArrayList<>();

    /**
     * Adds a heap to the Leonardo heap. We are adding a heap, because a single node can also be a heap.
     *
     * @param heap A given heap which is going to be added to the leonardo heap.
     */
    public void addHeap(SmoothSortHeap heap) {
        if (heapList.size() > 1 && (heapList.get(heapList.size() - 2).getOrder() - heapList.get(heapList.size() - 1).getOrder()) <= 1) {
            // merge the last and the heap before
            heapList.get(heapList.size() - 2).mergeHeaps(heapList.get(heapList.size() - 1), heap.getRoot().getValue());
            heapList.remove(heapList.size() - 1);
        } else {
            heapList.add(heap);
        }
        sortHeapList(heapList);
    }

    private void sortHeapList(List<SmoothSortHeap> heapList) {
        // sort the list with incrementing element count from left to right
        sortHeapListByElements(heapList);
        sortHeapListByRoot(heapList);
    }

    private void sortHeapListByElements(List<SmoothSortHeap> heapList) {
        SmoothSortHeap currentMax = heapList.get(0);

        // iterate the heap list and update the current maximum
        for (int i = 1; i < heapList.size(); i++) {
            if (currentMax.getNumberOfNodes() < heapList.get(i).getNumberOfNodes()) {
                // swap the heaps
                SmoothSortHeap tmp = heapList.get(i);
                heapList.set(i, currentMax);
                heapList.set(i - 1, tmp);
            }
        }
    }

    private void sortHeapListByRoot(List<SmoothSortHeap> heapList) {
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
                // swap the largest and the last value of the heap list
                int lastValue = heapList.get(j).getRoot().getValue();
                int largestValue = heapList.get(currentMax).getRoot().getValue();
                heapList.get(j).getRoot().setValue(largestValue);
                heapList.get(currentMax).getRoot().setValue(lastValue);
                // sort both heaps
                heapList.get(currentMax).sortHeap();
                heapList.get(j).sortHeap();
            }
        }
    }

    public int dequeueHighestValue() {
        // temporary save the last element and delete it
        SmoothSortHeap lastElement = heapList.get(heapList.size() - 1);
        heapList.remove(heapList.size() - 1);

        if (lastElement.getRoot().getLeftChild() != null && lastElement.getRoot().getRightChild() != null) {
            SmoothSortHeap tmpHeap = new SmoothSortHeap(lastElement.getRoot().getLeftChild());
            heapList.add(tmpHeap);
            tmpHeap = new SmoothSortHeap(lastElement.getRoot().getRightChild());
            heapList.add(tmpHeap);
        }

        if (heapList.size() != 0) {
            sortHeapList(heapList);
        }

        return lastElement.getRoot().getValue();
    }

    public List<SmoothSortHeap> getHeapList() {
        return heapList;
    }
}
