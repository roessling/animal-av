package generators.helpers.compression;

import java.util.TreeSet;

public class HuffmanAlgo {

	public static class Node implements Comparable<Node> {
		int sum;
		char letter;
		public Node left;
		public Node right;
		
		public Node(char letter, int sum) {
			this.letter = letter;
			this.sum = sum;
		}

		public Node(Node left, Node right) {
			// the left side must be the smalles in ascii
			if (left.letter < right.letter) this.letter = left.letter;
			else this.letter = right.letter;
			
			this.sum = left.sum + right.sum;
			this.left = left;
			this.right = right;
		}

		public String getBits(char letter) {
			if (this.letter == letter && this.left == null && this.right == null) return "";
			if (this.left == null && this.right == null) return null;
			if (this.left.getBits(letter) != null) return "0" + this.left.getBits(letter);
			if (this.right.getBits(letter) != null) return "1" + this.right.getBits(letter);
			else return null;
		}
		
		public int compareTo(Node o) {
			// return negative if this obect is smaller, postiv if greater and zero if equal
			
			if (this.sum < ((Node)o).sum) return -1;
			if (this.sum > ((Node)o).sum) return 1;
			// if equal compare the letters!
		  if (this.letter < ((Node)o).letter) return -1;
			if (this.letter > ((Node)o).letter) return 1;
			return 0;
		}
		
		
		
	}
	
	
	public static void compress(String[] text) {
		// extract the input text
		String input = "";
		for (int i=0;i<text.length;i++) {
			input += text[i];
		}
		input = input.toUpperCase();
		
		// get the frequencys of each letter
		int[] list = new int[256];
		for (int i=0; i<text.length;i++) {
			list[new Integer(text[i].charAt(0))]++;
		}

		// create leaves
		TreeSet<Node> nodes = new TreeSet<Node>();
		for (int i=0;i<list.length;i++) {
			if (list[i] > 0) {
				nodes.add(new Node((char)i, list[i]));
			}
		}
		
		// build the final tree
		while (nodes.size() > 1) {
			Node small = (Node)nodes.first();
			nodes.remove(small);
			Node small2 = (Node)nodes.first();
			nodes.remove(small2);
			nodes.add(new Node(small,small2));
		}
		
		// decode
		String result = "";
		for (int i=0;i<input.length();i++) {
			result += nodes.first().getBits(input.charAt(i));
			result += " ";
		}
		
		System.out.println(result);
	}
}
