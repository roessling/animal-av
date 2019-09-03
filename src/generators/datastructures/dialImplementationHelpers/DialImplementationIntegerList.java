package generators.datastructures.dialImplementationHelpers;

import java.util.LinkedList;

public class DialImplementationIntegerList {

    private LinkedList<Integer> list;

    DialImplementationIntegerList() {
        this.list = new LinkedList<>();
    }

    void add(Integer e) {
        this.list.add(e);
    }

    Integer getFirst() {
        return this.list.isEmpty() ? null : this.list.getFirst();
    }

    Integer removeFirst() {
        return this.list.removeFirst();
    }

    boolean isEmpty() {
        return this.list.isEmpty();
    }

    int[] getArray() {
        int[] output = new int[this.list.size()];
        for(int i = 0; i < this.list.size(); i++)
            output[i] = this.list.get(i);
        return output;
    }

    int length() {
        return this.list.size();
    }

    public String toString() {
        return this.list.toString();
    }
}
