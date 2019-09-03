package generators.compression.huffman.pregeneration;

import java.util.ArrayList;
import java.util.HashMap;

public class PreGenerator {

	private char[] chars;
	private int[] frequencies;
	private int sumFrequencies;
	/**
	 * the nodes (ordered by id starting with 1 and ending with the size of the
	 * list) of the precalculated tree
	 */
	private ArrayList<PreGenTreeNode> preCalculatedTree;

	public PreGenerator(char[] chars, int[] frequencies, int sumFrequencies) {
		this.chars = chars;
		this.frequencies = frequencies;
		this.sumFrequencies = sumFrequencies;
	}

	public void preGenerate() {

		ArrayList<PreGenTreeNode> nodes = new ArrayList<PreGenTreeNode>();

		ArrayList<PreGenPQElement> pq = new ArrayList<PreGenPQElement>();

		for (int i = 0; i < chars.length; i++) {

			PreGenPQElement newElement = new PreGenPQElement(i + 1, frequencies[i],
					frequencies[i] / (float) sumFrequencies);
			insertInPriorityQueue(newElement, pq);
			nodes.add(new PreGenTreeNode(i + 1, frequencies[i], null, null, null));
		}

		int i = pq.size() + 1;
		while (pq.size() > 1) {

			PreGenPQElement e1 = pq.remove(0);
			PreGenPQElement e2 = pq.remove(0);

			int freq = e1.getFrequency() + e2.getFrequency();
			float prob = freq / (float) sumFrequencies;

			PreGenTreeNode newNode = new PreGenTreeNode(i, freq, null,
					nodes.get(e1.getId() - 1), nodes.get(e2.getId() - 1));
			nodes.add(newNode);

			nodes.get(e1.getId() - 1).setParent(newNode);
			nodes.get(e2.getId() - 1).setParent(newNode);

			PreGenPQElement newElement = new PreGenPQElement(i, freq, prob);
			insertInPriorityQueue(newElement, pq);

			i++;
		}

		// the huffman tree generation is finished
		preCalculatedTree = nodes;
	}

	private void insertInPriorityQueue(PreGenPQElement element,
			ArrayList<PreGenPQElement> pq) {

		int insertionPoint = 0;
		if (!(pq.size() == 0)) {
			int insertFreq = element.getFrequency();
			insertionPoint = pq.size();
			for (int j = 0; j < pq.size(); j++) {

				float currentFreq = pq.get(j).getFrequency();

				if (insertFreq < currentFreq) {
					insertionPoint = j;
					break;
				}
			}
		}
		pq.add(insertionPoint, element);
	}

	public HashMap<Integer, Integer> getBottomNodePositions() {

		// determine positioning of the bottom nodes
		PreGenTreeNode root = preCalculatedTree.get(preCalculatedTree.size() - 1);
		ArrayList<Integer> inOrder = new ArrayList<Integer>();
		inOrderTraverse(root, inOrder);

		HashMap<Integer, Integer> bottomIdToPosition = new HashMap<Integer, Integer>();
		int position = 1;
		for (Integer id : inOrder) {
			if (preCalculatedTree.get(id - 1).getLeftNode() == null) {
				bottomIdToPosition.put(id, position);
				position++;
			}
		}
		return bottomIdToPosition;
	}

	private void inOrderTraverse(PreGenTreeNode node, ArrayList<Integer> inOrder) {

		inOrder.add(node.getId());
		if (node.getLeftNode() != null) {
			inOrderTraverse(node.getLeftNode(), inOrder);
		}
		if (node.getRightNode() != null) {
			inOrderTraverse(node.getRightNode(), inOrder);
		}
	}

	public int getTreeHeight() {

		PreGenTreeNode root = preCalculatedTree.get(preCalculatedTree.size() - 1);
		return calculateTreeHeight(root);
	}

	private int calculateTreeHeight(PreGenTreeNode node) {

		if (node == null) {
			return -1;
		} else {
			return Math.max(calculateTreeHeight(node.getLeftNode()),
					calculateTreeHeight(node.getRightNode())) + 1;
		}
	}

	public ArrayList<Integer> getLeftChildLeaves(int parentID) {

		PreGenTreeNode parent = preCalculatedTree.get(parentID - 1);

		return getLeaves(parent.getLeftNode());
	}

	public ArrayList<Integer> getRightChildLeaves(int parentID) {

		PreGenTreeNode parent = preCalculatedTree.get(parentID - 1);
		
		return getLeaves(parent.getRightNode());
	}

	private ArrayList<Integer> getLeaves(PreGenTreeNode node) {

		PreGenTreeNode leftChild = node.getLeftNode();
		PreGenTreeNode rightChild = node.getRightNode();

		ArrayList<Integer> leaves = new ArrayList<Integer>();

		if (leftChild == null && rightChild == null) {
			leaves.add(node.getId());
		}
		
		if (leftChild != null) {
			ArrayList<Integer> leftLeaves = getLeaves(leftChild);
			leaves.addAll(leftLeaves);
		}

		if (rightChild != null) {
			ArrayList<Integer> rightLeaves = getLeaves(rightChild);
			leaves.addAll(rightLeaves);
		}

		return leaves;
	}
}
