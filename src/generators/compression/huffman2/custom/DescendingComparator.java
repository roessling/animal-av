package generators.compression.huffman2.custom;

import java.util.Comparator;

import generators.compression.huffman2.Node.TreeNode;

public class DescendingComparator implements Comparator<TreeNode> {
	@Override
	public int compare(TreeNode lhs, TreeNode rhs) {
		if (lhs.frequency - rhs.frequency < 0) {
			return 1;
		} else if (lhs.frequency - rhs.frequency > 0) {
			return -1;
		} else {
			return 0;
		}
	}
}
