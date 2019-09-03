package generators.tree.trie_helpers;

import java.util.HashMap;

import algoanim.util.Coordinates;

// Tree layout code; based on :
// https://rachel53461.wordpress.com/2014/04/20/algorithm-for-drawing-trees/

public class TrieLayout
{
	private TrieLayoutNode root_node;

	int circle_radius;
	int horizontal_spacing;
	int vertical_spacing;
	private int start_x;
	private int start_y;

	HashMap<String, Coordinates> coordinates;

	public TrieLayout(Trie trie, int circle_radius_, int hor_spacing_, int ver_spacing_, int start_x_)
	{
		circle_radius = circle_radius_;
		horizontal_spacing = hor_spacing_;
		vertical_spacing = ver_spacing_;

		start_x = start_x_;
		start_y = 170;

		root_node = new TrieLayoutNode(null, trie.root_node, 0, 0);
		coordinates = new HashMap<String, Coordinates>();

		root_node.calc_initial_x();

		// move the tree on screen
		HashMap<Integer, Integer> left_contour = new HashMap<Integer, Integer>();
		root_node.get_left_contour(0, left_contour);

		int shift_amount = (get_min_val_map(left_contour) - circle_radius - horizontal_spacing);
		if (shift_amount < 0)
		{
			root_node.x -= shift_amount;
			root_node.mod -= shift_amount;
		}

		root_node.calc_final_position(0);
	}

	static int calc_num_of_children(Trie.TrieNode node_)
	{
		int counter = 0;
		for (Trie.TrieNode child : node_.children) if (child != null) ++counter;
		return counter;
	}

	static int get_max_val_map(HashMap<Integer, Integer> hmap)
	{
		int max = 0;
		for (Integer val : hmap.values()) if (val > max) max = val;
		return max;
	}

	static int get_max_key_map(HashMap<Integer, Integer> hmap)
	{
		int max = 0;
		for (Integer val : hmap.keySet()) if (val > max) max = val;
		return max;
	}

	static int get_min_val_map(HashMap<Integer, Integer> hmap)
	{
		int min = 0;
		for (Integer val : hmap.values()) if (val < min) min = val;
		return min;
	}


	class TrieLayoutNode
	{
		int x;
		int y;
		int mod;
		int i;
		char c;
		TrieLayoutNode[] children;
		TrieLayoutNode parent;

		TrieLayoutNode(TrieLayoutNode parent_, Trie.TrieNode node_, int depth_, int i_)
		{
			int num_of_children = calc_num_of_children(node_);
			children = new TrieLayoutNode[num_of_children];

			x = -1;
			y = depth_;
			mod = 0;
			parent = parent_;
			i = i_;
			c = node_.c;

			int index = 0;
			for (Trie.TrieNode node : node_.children)
			{
				if (node == null) continue;
				children[index] = new TrieLayoutNode(this, node, y + 1, index);
				++index;
			}
		}

		boolean is_leaf()
		{
			return(children.length == 0);
		}

		boolean is_left_most()
		{
			return(i == 0);
		}

		TrieLayoutNode get_left_most_sibling()
		{
			return(parent.children[0]);
		}

		TrieLayoutNode get_left_sibling()
		{
			return(parent.children[i - 1]);
		}

		TrieLayoutNode get_left_most_child()
		{
			return(children[0]);
		}

		TrieLayoutNode get_right_most_child()
		{
			return(children[children.length - 1]);
		}

		void calc_initial_x()
		{
			for (TrieLayoutNode child : children) child.calc_initial_x();

			if (is_leaf())
			{
				if (!is_left_most()) x = get_left_sibling().x + circle_radius + horizontal_spacing;
				else x = 0;
			}
			else if (children.length == 1)
			{
				if (is_left_most()) x = children[0].x;
				else
				{
					x = get_left_sibling().x + circle_radius + horizontal_spacing;
					mod = x - children[0].x;
				}
			}
			else
			{
				TrieLayoutNode left_child = get_left_most_child();
				TrieLayoutNode right_child = get_right_most_child();
				int mid = (left_child.x + right_child.x) / 2;

				if (is_left_most()) x = mid;
				else
				{
					x = get_left_sibling().x + circle_radius + horizontal_spacing;
					mod = x - mid;
				}
			}

			if (children.length > 0 && !is_left_most()) check_for_conflicts();
		}

		void check_for_conflicts()
		{
			int min_distance = horizontal_spacing + circle_radius;
			int shift_value = 0;

			HashMap<Integer, Integer> left_contour = new HashMap<Integer, Integer>();
			get_left_contour(0, left_contour);
			int node_max_level = get_max_key_map(left_contour);

			for (int j = 0; j != i; ++j)
			{
				TrieLayoutNode sibling = parent.children[j];
				HashMap<Integer, Integer> sibling_right_contour = new HashMap<Integer, Integer>();
				sibling.get_right_contour(0, sibling_right_contour);

				int level_limit = Math.min(get_max_key_map(sibling_right_contour), node_max_level);
				for (int level = y; level <= level_limit; ++level)
				{
					int distance = left_contour.get(level) - sibling_right_contour.get(level);
					shift_value = Math.max(shift_value, min_distance - distance);
				}

				if (shift_value > 0)
				{
					x += shift_value;
					mod += shift_value;
					center_nodes_between(parent.get_right_most_child());
				}
			}
		}

		void center_nodes_between(TrieLayoutNode node)
		{
			int left_index = i;
			int right_index = node.i;
			int num_nodes_between = (right_index - left_index) - 1;

			if (num_nodes_between > 0)
			{
				float distance_between_nodes = (float)(x - node.x) / (float)(num_nodes_between + 1);

				int count = 1;
				for (int j = left_index + 1; j < right_index; ++j)
				{
					TrieLayoutNode middle_node = parent.children[j];

					float desired_x = (float)node.x + (float)(distance_between_nodes * count);
					float offset = desired_x - (float)middle_node.x;
					middle_node.x += (int)offset;
					middle_node.mod += (int)offset;

					++count;
				}
				check_for_conflicts();
			}
		}

		void get_left_contour(int mod_sum, HashMap<Integer, Integer> values)
		{
			if (!values.containsKey(y) || (x + mod_sum) < values.get(y)) values.put(y, x + mod_sum);
			for (TrieLayoutNode child : children) child.get_left_contour(mod_sum + mod, values);
		}

		void get_right_contour(int mod_sum, HashMap<Integer, Integer> values)
		{
			if (!values.containsKey(y) || (x + mod_sum) > values.get(y)) values.put(y, x + mod_sum);
			for (TrieLayoutNode child : children) child.get_right_contour(mod_sum + mod, values);
		}

		String gen_key()
		{
			return((parent == null)? " " : (c + parent.gen_key()));
		}

		void calc_final_position(int mod_sum)
		{
			x += mod_sum;
			mod_sum += mod;

			coordinates.put(gen_key(), new Coordinates(x + start_x, y * vertical_spacing + start_y));

			for (TrieLayoutNode child : children) child.calc_final_position(mod_sum);
		}
	}
}
