package generators.misc.birch;

import generators.helpers.Pair;

public class CFTree {

    class SplitResult {
        CFTreeNode firstTN;
        CFTreeNode secondTN;
        ClusterFeature firstCF; // Key
        ClusterFeature secondCF; // Key

        SplitResult(CFTreeNode firstTN, CFTreeNode secondTN, ClusterFeature firstCF, ClusterFeature secondCF) {
            this.firstCF = firstCF;
            this.secondCF = secondCF;
            this.firstTN = firstTN;
            this.secondTN = secondTN;
        }
    }

    private CFTreeNode root;
    private int BL;
    private float threshold;
    private CFNameProvider cNameProvider = new CFNameProvider();
    private CFNameProvider nameProvider = new CFNameProvider();

    public CFTree(int BL, float threshold) {
        this.BL = BL;
        this.threshold = threshold;
    }

    public CFTreeNode getRoot() {
        return root;
    }

    public void setRoot(CFTreeNode root){
        this.root = root;
    }

    CFNameProvider getcNameProvider(){
        return cNameProvider;
    }

    CFNameProvider getNameProvider(){
        return nameProvider;
    }

    public void insert(ClusterFeature cf) {
        if (root == null) {
            root = new CFTreeNode(BL, true);
            root.addKey(cf);
            return;
        }

        insert(cf, root);

        if (root.getCount() > BL) {
            SplitResult result = splitNode(root, null);

            root = new CFTreeNode(BL, false);

            root.addKey(result.firstCF, result.firstTN);
            root.addKey(result.secondCF, result.secondTN);
        }
    }

    /**
     * Precondition: currentNode.getCount() must be less than BL
     *
     * @param cf
     * @param currentNode
     */
    private void insert(ClusterFeature cf, CFTreeNode currentNode) {
        if (currentNode.isLeaf())
            insertIntoLeaf(cf, currentNode);
        else {
            ClusterFeature[] keys = currentNode.getKeys();

            int nearestIdx = currentNode.findNearest(cf);
            CFTreeNode child = currentNode.getChildren()[nearestIdx];

            int childCountPre = child.getCount();

            insert(cf, child);

            if (child.getCount() > BL) {
                SplitResult result = splitNode(child, keys[nearestIdx].getName());

                currentNode.addKey(result.firstCF, result.firstTN);
                keys[nearestIdx] = result.secondCF;
                currentNode.getChildren()[nearestIdx] = result.secondTN;
            } else {
                if (child.getCount() == 1)
                    keys[nearestIdx] = keys[nearestIdx].addClusterFeature(cf, child.getKeys()[0].getName());
                else if (child.getCount() == 2 && childCountPre == 1)
                    keys[nearestIdx] = keys[nearestIdx].addClusterFeature(cf, nameProvider.useName());
                else
                    keys[nearestIdx] = keys[nearestIdx].addClusterFeature(cf, keys[nearestIdx].getName());
            }
        }
    }

    private void insertIntoLeaf(ClusterFeature cf, CFTreeNode currentNode) {
        ClusterFeature[] keys = currentNode.getKeys();

        int nearestIdx = currentNode.findNearest(cf);
        ClusterFeature nearest = keys[nearestIdx];

        ClusterFeature newCF;
        if (nearest.getN() == 1) {
            newCF = nearest.addClusterFeature(cf, "C_" + cNameProvider.getName());
        } else {
            newCF = nearest.addClusterFeature(cf, nearest.getName());
        }

        if (newCF.getRadius() < threshold) {
            keys[nearestIdx] = newCF;
            cNameProvider.useName();
        } else
            currentNode.addKey(cf);
    }

    private SplitResult splitNode(CFTreeNode node, String oldName) {
        ClusterFeature[] cfs = node.getKeys();

        Pair<Integer, Integer> widestAway = node.findWidestAway();

        ClusterFeature first = cfs[widestAway.getX()];
        ClusterFeature second = cfs[widestAway.getY()];

        CFTreeNode firstTN = new CFTreeNode(BL, node.isLeaf());
        CFTreeNode secondTN = new CFTreeNode(BL, node.isLeaf());

        firstTN.addKey(first, node.getChildren()[widestAway.getX()]);
        secondTN.addKey(second, node.getChildren()[widestAway.getY()]);

        ClusterFeature firstCF = first;
        ClusterFeature secondCF = second;

        // verbleibende CFs dem dichtesten der weit entferntesten CFs zuteilen
        for (int i = 0; i < node.getCount(); i++) {
            if (i == widestAway.getX() || i == widestAway.getY()) {
                continue;
            }

            ClusterFeature currentKey = cfs[i];
            if (currentKey.getDistanceTo(first) < currentKey.getDistanceTo(second)) {
                firstTN.addKey(currentKey, node.getChildren()[i]);
                firstCF = firstCF.addClusterFeature(currentKey, null);
            } else {
                secondTN.addKey(currentKey, node.getChildren()[i]);
                secondCF = secondCF.addClusterFeature(currentKey, null);
            }
        }

        boolean oldNameUsed = oldName == null;
        if (firstTN.getCount() > 1) {
            if (!oldNameUsed) {
                firstCF.setName(oldName);
                oldNameUsed = true;
            } else
                firstCF.setName(nameProvider.useName());
        }
        if (secondTN.getCount() > 1) {
            if (!oldNameUsed)
                secondCF.setName(oldName);
            else
                secondCF.setName(nameProvider.useName());
        }

        return new SplitResult(firstTN, secondTN, firstCF, secondCF);
    }
}
