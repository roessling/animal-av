package generators.misc.ershov;

public class TreeNode {
    private String key;
    private TreeNode left;
    private TreeNode right;
    private int representation;

    public TreeNode(String key, TreeNode left, TreeNode right) {
        this.key = key;
        this.left = left;
        this.right = right;
    }

    /*
     * isLeaf = true, if the node is a leaf
     */
    public boolean isLeaf() {
        return left == null && right == null;
    }

    /*
     * compute the height of the Tree
     */
    public int getHeight() {
        if (left == null & right == null)
            return 1;
        else if (left == null)
            return right.getHeight() + 1;
        else if (right == null)
            return left.getHeight() + 1;
        else
            return Math.max(left.getHeight(), right.getHeight()) + 1;
    }

    public int getNodeCount() {
        if (isLeaf())
            return 1;
        else if (!hasLeft())
            return 1 + right.getNodeCount();
        else if (!hasRight())
            return 1 + left.getNodeCount();
        else
            return 1 + left.getNodeCount() + right.getNodeCount();
    }

    /*
     * compute the count of nodes that can be in the lowest level
     */
    public int getMaxLeafs() {
        return (int) Math.pow(2, getHeight() - 1);
    }

    public boolean hasLeft() {
        return left != null;
    }

    public boolean hasRight() {
        return right != null;
    }

    public String getKey() {
        return key;
    }

    public TreeNode getRight() {
        return right;
    }

    public TreeNode getLeft() {
        return left;
    }

    public int getRepresentation() {
        return representation;
    }

    public void setRepresentation(int representation) {
        this.representation = representation;
    }
}
