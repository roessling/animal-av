package generators.searching;

public class JPSHeuristic {
	public enum heuristic {
		euklid
	}
	
	public double distance (JPSNode node1, JPSNode node2, heuristic heurist) {
		if (heurist == heuristic.euklid) {
			return getEuklidDistance(node1, node2);
		}
		
		return 0;
		
	}
	
	private double getEuklidDistance (JPSNode node1, JPSNode node2) {
		int node1X = node1.getX();
		int node1Y = node1.getY();
		int node2X = node2.getX();
		int node2Y = node2.getY();
		
		int xSquare = (node2X - node1X) * (node2X - node1X);
		int ySquare = (node2Y - node1Y) * (node2Y - node1Y);
				
		
		return Math.sqrt(xSquare + ySquare);
		
	}
}
