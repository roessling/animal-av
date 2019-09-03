package generators.compression.huffman2.HuffmanCoding;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import generators.compression.huffman2.Node.TreeNode;
import generators.compression.huffman2.custom.AscendingComparator;

import java.util.PriorityQueue;

public class HuffmanCoding {

	private static TreeNode buildTree(HashMap<Character, Integer> freq) {

		// initialize priority queue with singleton trees
		PriorityQueue<TreeNode> pq = new PriorityQueue<TreeNode>(new AscendingComparator());
		for (Entry<Character, Integer> e : freq.entrySet()) {
			pq.add(new TreeNode(e.getKey().toString(), e.getValue().intValue(), null, null, null));
		}

		// merge two smallest trees
		while (pq.size() > 1) {
			TreeNode leftChild = pq.poll();
			TreeNode rightChild = pq.poll();
			double frequency = leftChild.frequency;
			if (rightChild != null) {
				frequency = frequency + rightChild.frequency;
			}
			TreeNode parent = new TreeNode("", frequency, null, leftChild, rightChild);
			leftChild.parent = parent;
			if (rightChild != null) {
				rightChild.parent = parent;
			}
			pq.add(parent);
		}
		return pq.poll();
	}

	private static Map<Character, String> buildCode(TreeNode root) {
		Map<Character, String> lookupTable = new HashMap<>();
		buildCodeInternal(lookupTable, root, "");
		return lookupTable;
	}

	private static void buildCodeInternal(Map<Character, String> lookupTable, TreeNode x, String s) {
		if (!x.isLeaf()) {
			if (x.leftChild != null) {
				buildCodeInternal(lookupTable, x.leftChild, s + '0');
			}
			if (x.rightChild != null) {
				buildCodeInternal(lookupTable, x.rightChild, s + '1');
			}
		} else {
			lookupTable.put(x.label.charAt(0), s);
		}
	}

	public static String expand(HuffmanModel model) {

		// read in Huffman tree from input stream
		TreeNode root = model.tree;

		String compressed = model.compressed;

		StringBuilder sb = new StringBuilder();
		// decode using the Huffman tree
		for (int i = 0; i < compressed.length(); i++) {
			TreeNode x = root;
			while (!x.isLeaf()) {
				char bit = compressed.charAt(i);
				if (bit == '0') {
					x = x.leftChild;
					i++;
				} else {
					x = x.rightChild;
					i++;
				}
			}
			i--;
			sb.append(x.label);
		}
		return sb.toString();
	}

	public static HuffmanModel compress(String text) {
		HuffmanModel model = new HuffmanModel();
		model.input = text;

		char[] input = text.toCharArray();

		// tabulate all frequencies
		HashMap<Character, Integer> freq = new HashMap<>();
		for (int i = 0; i < input.length; i++) {
			freq.put(input[i], 0);
		}
		for (int i = 0; i < input.length; i++) {
			freq.put(input[i], freq.get(input[i]) + 1);
		}

		// create the tree
		TreeNode root = buildTree(freq);
		model.tree = root;

		// create the code table
		Map<Character, String> lookupTable = buildCode(root);
		model.lookupTable = lookupTable;

		// get compressed version
		StringBuilder compressed = new StringBuilder();
		for (int i = 0; i < input.length; i++) {
			compressed.append(lookupTable.get(input[i]));
		}
		model.compressed = compressed.toString();

		// return model
		return model;
	}

	public static void main(String[] args) {
		String text = "MISSISSIPPI";
		HuffmanModel model = compress(text);
		System.out.println(model.compressed.length());
		System.out.println(model.compressed);
		String s = expand(model);
		System.out.println(s);
	}
}
