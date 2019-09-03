package generators.misc.schulzemethod;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import algoanim.animalscript.AnimalScript;
import algoanim.util.Offset;

@SuppressWarnings("static-method")
public class SchulzeMethodTest {

	@Test
	public void testCandidateExample2() {
		HashMap<String, Integer> votes = new HashMap<String, Integer>();
		votes.put("A > B > C > D", 3);
		votes.put("D > A > B > C", 2);
		votes.put("D > B > C > A", 2);
		votes.put("C > B > D > A", 2);
		List<String> cand = SchulzeMethod.getCandidateList(votes);
		assertEquals(4, cand.size());
		assertEquals(cand.get(0), "A");
		assertEquals(cand.get(1), "B");
		assertEquals(cand.get(2), "C");
		assertEquals(cand.get(3), "D");
	}

	@Test
	public void testCandidateMissing() {
		HashMap<String, Integer> votes = new HashMap<String, Integer>();
		votes.put("A > C > D", 3);
		votes.put("B > A", 2);
		List<String> cand = SchulzeMethod.getCandidateList(votes);
		assertEquals(4, cand.size());
		assertEquals(cand.get(0), "A");
		assertEquals(cand.get(1), "B");
		assertEquals(cand.get(2), "C");
		assertEquals(cand.get(3), "D");
	}

	@Test
	public void testCircleOffset4Cands() {
		double baserad = (2 * Math.PI) / 4;
		Offset off1 = SchulzeMethod.getCircleOffset(null, baserad, 100);
		assertEquals(off1.getDirection(), AnimalScript.DIRECTION_NW);
		assertEquals(100, off1.getX());
		assertEquals(0, off1.getY());
		Offset off2 = SchulzeMethod.getCircleOffset(null, baserad * 2, 100);
		assertEquals(off2.getDirection(), AnimalScript.DIRECTION_NW);
		assertEquals(0, off2.getX());
		assertEquals(100, off2.getY());
		Offset off3 = SchulzeMethod.getCircleOffset(null, baserad * 3, 100);
		assertEquals(off3.getDirection(), AnimalScript.DIRECTION_NW);
		assertEquals(-100, off3.getX());
		assertEquals(0, off3.getY());
		Offset off4 = SchulzeMethod.getCircleOffset(null, baserad * 4, 100);
		assertEquals(off4.getDirection(), AnimalScript.DIRECTION_NW);
		assertEquals(0, off4.getX());
		assertEquals(-100, off4.getY());
	}

	@Test
	public void testCircleOffset5Cands() {
		double baserad = (2 * Math.PI) / 5;
		// The values might change, but idea should remain stable
		Offset off1 = SchulzeMethod.getCircleOffset(null, baserad, 100);
		assertEquals(off1.getDirection(), AnimalScript.DIRECTION_NW);
		assertEquals(95, off1.getX());
		assertEquals(-30, off1.getY());
		Offset off2 = SchulzeMethod.getCircleOffset(null, baserad * 2, 100);
		assertEquals(off2.getDirection(), AnimalScript.DIRECTION_NW);
		assertEquals(58, off2.getX());
		assertEquals(80, off2.getY());
		Offset off3 = SchulzeMethod.getCircleOffset(null, baserad * 3, 100);
		assertEquals(off3.getDirection(), AnimalScript.DIRECTION_NW);
		assertEquals(-58, off3.getX());
		assertEquals(80, off3.getY());
		Offset off4 = SchulzeMethod.getCircleOffset(null, baserad * 4, 100);
		assertEquals(off4.getDirection(), AnimalScript.DIRECTION_NW);
		assertEquals(-95, off4.getX());
		assertEquals(-30, off4.getY());
		Offset off5 = SchulzeMethod.getCircleOffset(null, baserad * 5, 100);
		assertEquals(off5.getDirection(), AnimalScript.DIRECTION_NW);
		assertEquals(0, off5.getX());
		assertEquals(-100, off5.getY());
	}

	@Test
	public void testRankingAoverB() {
		int[] winner = { 2, 1 };
		List<String> cand = new ArrayList<String>();
		cand.add("A");
		cand.add("B");
		assertEquals("A > B",
				SchulzeMethod.getRanking(cand, winner, null));
	}

	@Test
	public void testRankingBoverA() {
		int[] winner = { 1, 2 };
		List<String> cand = new ArrayList<String>();
		cand.add("A");
		cand.add("B");
		assertEquals("B > A",
				SchulzeMethod.getRanking(cand, winner, null));
	}

	@Test
	public void testRankingABTie() {
		int[] winner = { 2, 2 };
		List<String> cand = new ArrayList<String>();
		cand.add("A");
		cand.add("B");
		assertEquals("A = B",
				SchulzeMethod.getRanking(cand, winner, null));
	}

	@Test
	public void testRankingABandCDTie() {
		int[] winner = { 4, 4, 2, 2 };
		List<String> cand = new ArrayList<String>();
		cand.add("A");
		cand.add("B");
		cand.add("C");
		cand.add("D");
		assertEquals("A = B > C = D",
				SchulzeMethod.getRanking(cand, winner, null));
	}

	@Test
	public void testVoteList() {
		String[][] votes = { { "1x", "A", "B", "C" }, { "0x", "D", "E" },
				{ "", "A", "B", "C" }, { "5x", "C", "B", "A" } };
		Map<String, Integer> list = SchulzeMethod.getVoteList(votes);
		assertEquals(2, list.size());
		assertEquals(2, list.get("A > B > C").intValue());
		assertEquals(5, list.get("C > B > A").intValue());
	}

	@Test
	public void testBFS() {
		// Wikipedia Schulze Example 1 graph
		final int[][] adjacency = { { 0, 0, 26, 30, 0 }, { 25, 0, 0, 33, 0 },
				{ 0, 29, 0, 0, 24 }, { 0, 0, 28, 0, 0 }, { 23, 27, 0, 31, 0 } };
		List<Integer> p;

		p = SchulzeMethod.breadthFirstSearch(adjacency, 30, 0, 3);
		assertEquals(2, p.size());
		assertEquals(0, p.get(0).intValue());
		assertEquals(3, p.get(1).intValue());

		p = SchulzeMethod.breadthFirstSearch(adjacency, 28, 0, 1);
		assertEquals(4, p.size());
		assertEquals(0, p.get(0).intValue());
		assertEquals(3, p.get(1).intValue());
		assertEquals(2, p.get(2).intValue());
		assertEquals(1, p.get(3).intValue());

		p = SchulzeMethod.breadthFirstSearch(adjacency, 24, 0, 4);
		assertEquals(3, p.size());
		assertEquals(0, p.get(0).intValue());
		assertEquals(2, p.get(1).intValue());
		assertEquals(4, p.get(2).intValue());

		p = SchulzeMethod.breadthFirstSearch(adjacency, 25, 4, 0);
		assertEquals(3, p.size());
		assertEquals(4, p.get(0).intValue());
		assertEquals(1, p.get(1).intValue());
		assertEquals(0, p.get(2).intValue());

		p = SchulzeMethod.breadthFirstSearch(adjacency, 28, 4, 1);
		assertEquals(4, p.size());
		assertEquals(4, p.get(0).intValue());
		assertEquals(3, p.get(1).intValue());
		assertEquals(2, p.get(2).intValue());
		assertEquals(1, p.get(3).intValue());
	}

}
