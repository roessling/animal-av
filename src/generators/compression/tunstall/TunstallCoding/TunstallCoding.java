package generators.compression.tunstall.TunstallCoding;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import generators.compression.tunstall.Node.TreeNode;
import generators.compression.tunstall.custom.DescendingComparator;


public class TunstallCoding {

	public static TreeNode buildTree(String text, int dictionarySize) {

		int textLength = text.length();
		char[] input = text.toCharArray();
		Map<Character, Integer> frequencies = new HashMap<>();
		for (char c : input) {
			frequencies.put(c, 0);
		}
		for (char c : input) {
			frequencies.put(c, frequencies.get(c) + 1);
		}
		if (dictionarySize < frequencies.size()) {
			throw new IllegalArgumentException("Dictionary size insufficient.");
		}

		TreeNode root = new TreeNode("", 1, null, null);
		PriorityQueue<TreeNode> leafs = new PriorityQueue<>(new DescendingComparator());

		for (Entry<Character, Integer> e : frequencies.entrySet()) {
			TreeNode leaf = new TreeNode(e.getKey().toString(), ((double) e.getValue()) / ((double) textLength), root,
					null);
			root.children.add(leaf);
			leafs.add(leaf);
		}

		while (leafs.size() < dictionarySize) {
			TreeNode node = leafs.poll();
			for (Entry<Character, Integer> e : frequencies.entrySet()) {
				double freq = (((double) e.getValue()) / ((double) textLength)) * node.frequency;
				TreeNode leaf = new TreeNode(node.label + e.getKey().toString(), freq, node, null);
				node.children.add(leaf);
				leafs.add(leaf);
			}
		}
		return root;
	}

	public static int buildDictionaryInternal(Map<String, Integer> dictionary, TreeNode node, int number) {
		if (!node.isLeaf()) {
			int myNumber = number;
			for (int i = 0; i < node.children.size(); i++) {
				myNumber = buildDictionaryInternal(dictionary, node.children.get(i), myNumber);
			}
			return myNumber;
		} else {
			dictionary.put(node.label, number);
			return number + 1;
		}
	}

	public static Map<String, Integer> buildDictionary(TreeNode root) {
		Map<String, Integer> dictionary = new HashMap<>();
		buildDictionaryInternal(dictionary, root, 0);
		return dictionary;
	}

	public static TunstallModel compress(String text, int maxDictionarySize) {
		if (text == null || text.isEmpty()) {
			throw new IllegalArgumentException("text size is insufficient");
		}
		TreeNode tree = buildTree(text, maxDictionarySize);
		Map<String, Integer> dictionary = buildDictionary(tree);

		int binaryLength = Integer.toBinaryString(dictionary.size() - 1).length();

		Map<String, String> binaryDictionary = new HashMap<>();
		for (Entry<String, Integer> e : dictionary.entrySet()) {
			String s = Integer.toBinaryString(e.getValue());
			for (int i = s.length(); i < binaryLength; i++) {
				s = "0" + s;
			}
			binaryDictionary.put(e.getKey(), s);
		}

		StringBuilder sb = new StringBuilder();
		String c = "";
		for (int i = 0; i < text.length(); i++) {
			c = c + text.charAt(i);
			if (binaryDictionary.containsKey(c)) {
				sb.append(binaryDictionary.get(c));
				c = "";
			}
		}

		TunstallModel model = new TunstallModel();
		model.text = text;
		model.tree = tree;
		model.lookupTable = binaryDictionary;
		model.compressed = sb.toString();

		return model;
	}

	public static String expand(TunstallModel model) {
		Map<String, String> dictionary = model.lookupTable.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

		int length = dictionary.keySet().toArray(new String[0])[0].length();
		String compressed = model.compressed;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < compressed.length(); i = i + length) {
			String a = dictionary.get(compressed.substring(i, i + length));
			sb.append(a);
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(String.format("%.2f", 0.04f));
		String text = "dasdas";
		for (int i = 3; i < text.length(); i++) {
			TunstallModel model = compress(text, i);
			String textR = expand(model);
			if (!text.equals(textR)) {
				System.err.println(i + " " + textR);
			}
			 System.out.println(i);
		}
	}
}
