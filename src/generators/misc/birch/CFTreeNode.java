package generators.misc.birch;

import algoanim.primitives.StringArray;
import generators.helpers.Pair;

public class CFTreeNode {
    private boolean isLeaf;
    private ClusterFeature[] keys;
    private CFTreeNode[] children;
    private StringArray representationTreeNode;
    private int count = 0;

    CFTreeNode(int BL, boolean isLeaf) {
        keys = new ClusterFeature[BL + 1];
        children = new CFTreeNode[BL + 1];
        this.isLeaf = isLeaf;
    }

    boolean isLeaf() {
        return isLeaf;
    }

    ClusterFeature[] getKeys() {
        return keys;
    }

    CFTreeNode[] getChildren() {
        return children;
    }

    int getCount() {
        return count;
    }

    void setRepresentationTreeNode(StringArray representationTreeNode){
        this.representationTreeNode = representationTreeNode;
    }

    public StringArray getRepresentationTreeNode() {
        return representationTreeNode;
    }

    void addKey(ClusterFeature cf) {
        keys[count] = cf;
        count++;
    }

    void addKey(ClusterFeature cf, CFTreeNode tn) {
        children[count] = tn;
        addKey(cf);
    }

    int findNearest(ClusterFeature cf) {
        float minDist = Float.MAX_VALUE;
        int minIdx = -1;

        for (int i = 0; i < count; i++) {
            float dist = keys[i].getDistanceTo(cf);
            if (dist < minDist) {
                minDist = dist;
                minIdx = i;
            }
        }

        return minIdx;
    }

    Pair<Integer, Integer> findWidestAway() {
        float maxDist = 0;
        int firstIdx = -1, secondIdx = -1;

        for (int i = 0; i < count - 1; i++) {
            for (int j = i + 1; j < count; j++) {
                float dist = keys[i].getDistanceTo(keys[j]);

                if (dist > maxDist) {
                    maxDist = dist;
                    firstIdx = i;
                    secondIdx = j;
                }
            }
        }

        return new Pair<>(firstIdx, secondIdx);
    }

    int getHeight() {
        if (isLeaf)
            return 1;
        else
            return 1 + children[0].getHeight();
    }

    void highlight(){
        if (representationTreeNode != null)
            representationTreeNode.highlightCell(0, getCount() - 1, null, null);
    }

    void unhighlight(){
        if(representationTreeNode != null)
            representationTreeNode.unhighlightCell(0,getCount()-1,null,null);
    }
}
