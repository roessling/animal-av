package generators.tree.helpers;

/**
 *
 * @author Mohit
 */
public class BinaryTreeNode {
    private static int num = 0;
    private int id;

    private BinaryTreeNode left;
    private BinaryTreeNode right;
    
    private int x;
    private int y;
    
    public BinaryTreeNode() {
        id = num++;
    }
    public static BinaryTreeNode parse(String[][] tree) throws IllegalArgumentException {
        StringBuilder strTree = new StringBuilder();
        for (int i = 0; i < tree.length; i++) {
            strTree.append(tree[i][0]);
            strTree.append("\n");
        }
        return parse(strTree.toString());
    }
    public static BinaryTreeNode parse(String tree) throws IllegalArgumentException {
        String[] leaves = tree.split("\n");
        BinaryTreeNode root = new BinaryTreeNode();
        for (int i = 0; i < leaves.length; i++) {
            if (leaves[i].trim().startsWith("%") || leaves[i].trim().startsWith("#") || leaves[i].trim().isEmpty()) {
                continue;
            }
            String[] path = leaves[i].split("\\.");
            BinaryTreeNode p = root;
            if (path[0].equals("root")){
                for (int j = 1; j < path.length; j++)  {
                    if ("left".equals(path[j])) {
                            if (p.getLeft() == null) {
                                p.setLeft(new BinaryTreeNode());
                            }
                            p = p.getLeft();
                    } else if ("right".equals(path[j])) {
                            if (p.getRight() == null) {
                                p.setRight(new BinaryTreeNode());
                            }
                            p = p.getRight();
                    } else{
                            throw new IllegalArgumentException("Fehler in Zeile " + (i+1) + " aufgetreten");
                    }
                }
            }
        }
        
        return root;
    }
    
    public boolean hasLeftChild() {
        return left != null;
    }
    public boolean hasRightChild() {
        return right != null;
    }
    
    public int getID() {
        return id;
    }
    
    public BinaryTreeNode getLeft() {
        return left;
    }

    public void setLeft(BinaryTreeNode left) {
        this.left = left;
    }

    public BinaryTreeNode getRight() {
        return right;
    }

    public void setRight(BinaryTreeNode right) {
        this.right = right;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
