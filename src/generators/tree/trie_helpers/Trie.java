package generators.tree.trie_helpers;

public class Trie
{
	TrieNode root_node;

	public Trie()
	{
		root_node = new TrieNode(' ');
	}

	public void insert(String string)
	{
		TrieNode node = root_node;

		for (int i = 0; i < string.length(); ++i)
		{
			char c = string.charAt(i);
			int c_index = c - 'a';

			if (node.children[c_index] == null)
			{
				node.children[c_index] = new TrieNode(c);
			}
			node = node.children[c_index];
		}

		node.end_of_word = true;
	}

	public boolean search(String string)
	{
		TrieNode node = root_node;

		for (int i = 0; i < string.length(); ++i)
		{
			char c = string.charAt(i);
			int c_index = c - 'a';

			if (node.children[c_index] == null) return false;

			node = node.children[c_index];
		}

		return(node.end_of_word);
	}

	public class TrieNode
	{
		char c;
		boolean end_of_word;
		TrieNode[] children;

		TrieNode(char c_)
		{
			c = c_;
			children = new TrieNode[26];
		}
	}
}
