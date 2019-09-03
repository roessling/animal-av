package generators.helpers.tsigaridas;
import generators.tree.HeapTree;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class HeapTests  {
	HeapTree h;
	@Before
	public void setup() {
//		Language l = new AnimalScript("Binary Search Tree Algorithm", "Ioannis Tsigaridas", 1024, 768);
		
		h = new HeapTree();
	}
	
	@Test
	public void easyInsert () {
//		Test1
		int tree[] = {2, 9, 56, 77, 12, 13, 96, 17, 56, 44};
		h.createTree(tree);
		
		Assert.assertEquals(2, h.getRoot().getKey());
		//the root has left and right edge.
		Assert.assertNotNull(h.getRoot().getLeft());
		Assert.assertNotNull(h.getRoot().getRight());
		
		Assert.assertEquals(9, h.getRoot().getLeft().getKey());
		Assert.assertEquals(56, h.getRoot().getRight().getKey());

		
		//9
		HeapNode node_9 = h.getRoot().getLeft();
		Assert.assertNotNull(node_9.getLeft());
		Assert.assertNotNull(node_9.getRight());
		
		Assert.assertEquals(77, node_9.getLeft().getKey());
		Assert.assertEquals(12, node_9.getRight().getKey());

		//56
		HeapNode node_56 = h.getRoot().getRight();
		Assert.assertNotNull(node_56.getLeft());
		Assert.assertNotNull(node_56.getRight());
		
		Assert.assertEquals(13, node_56.getLeft().getKey());
		Assert.assertEquals(96, node_56.getRight().getKey());
		
		//77
		HeapNode node_77 = node_9.getLeft();
		Assert.assertNotNull(node_77.getLeft());
		Assert.assertNotNull(node_77.getRight());
		
		Assert.assertEquals(17, node_77.getLeft().getKey());
		Assert.assertEquals(56, node_77.getRight().getKey());
		
		//12
		HeapNode node_12 = node_9.getRight();
		Assert.assertNotNull(node_12.getLeft());
		Assert.assertNull(node_12.getRight());
		
		Assert.assertEquals(44, node_12.getLeft().getKey());
		
		//13
		HeapNode node_13 = node_56.getLeft();
		Assert.assertNull(node_13.getLeft());
		Assert.assertNull(node_13.getRight());
		
		//96
		HeapNode node_96 = node_56.getRight();
		Assert.assertNull(node_96.getLeft());
		Assert.assertNull(node_96.getRight());
		
		//17
		HeapNode node_17 = node_77.getLeft();
		Assert.assertNull(node_17.getLeft());
		Assert.assertNull(node_17.getRight());
		
		//96
		HeapNode node_56s = node_77.getRight();
		Assert.assertNull(node_56s.getLeft());
		Assert.assertNull(node_56s.getRight());
		
		//17
		HeapNode node_44 = node_12.getLeft();
		Assert.assertNull(node_44.getLeft());
		Assert.assertNull(node_44.getRight());
		
	
	}
	
}


