package generators.searching;

import java.util.ArrayList;
import java.util.LinkedList;

public class JPSMaze {
	public JPSNode[][] maze;
	private JPSHeap heap;
	public int maxSizeX, maxSizeY;
	public int[][] mazeArray;
	
	public JPSMaze(int nodeSize, int[][]_mazeArray) {
		this.mazeArray = _mazeArray;
		maze = mazeGenerator(nodeSize);
		
		
		
		this.heap = new JPSHeap();
	}
	
	private JPSNode[][] mazeGenerator(int nodeSize) {
		JPSNode[][] Maze = null;
		
		
		
		
		maxSizeX = mazeArray.length;
		maxSizeY = mazeArray[0].length;
		Maze = new JPSNode[maxSizeX][maxSizeY];
		for (int i = 0; i < maxSizeX ; i++) {
			for (int j = 0; j < maxSizeY ; j++) {
				Maze[i][j] = new JPSNode(i,j, nodeSize);
				
			}
		}
		for (int i = 0; i < maxSizeX; i++) {
			for (int j = 0; j < maxSizeY; j++) {
				if (mazeArray[i][j] == 0) {
	    			Maze[i][j].walkable = true;
		    			
	    		}
				if (mazeArray[i][j] == 1) {
		    		Maze[i][j].walkable = false;
		    			
		   		}
		   		if (mazeArray[i][j] == 2) {
		   			Maze[i][j].start = true;
		    			
		   		}
	    		if (mazeArray[i][j] == 3) {
	    			Maze[i][j].end = true;
	    		}
					
			}
		}
		
		

		
		return Maze;
	}
	
	public LinkedList<float[]> getHeap() {
		return heap.getList();
	} 
	
	public void heapAdd(JPSNode jpsNode){
		float[] tmp = {jpsNode.x,jpsNode.y,jpsNode.f};
		
		heap.add(tmp);
	}
	
	
	
	public ArrayList<JPSNode> pathCreate(JPSNode node){
		ArrayList<JPSNode> trail = new ArrayList<JPSNode>();
		
		while (node.parent!=null){
			try{
				trail.add(0,node);
			}catch (Exception e){}
			node = node.parent;
		}
		return trail;
	}
	/**
	 * @return (int) size of the heap
	 */
	public int heapSize(){
		return heap.getSize();
	}
	/**
	 * @return (Node) takes data from popped float[] and returns the correct node
	 */
	public JPSNode heapPopNode(){
		float[] tmp = heap.pop();
		return getNode((int)tmp[0],(int)tmp[1]);
	}
	
	
	
	public boolean walkable (int x, int y) {
		if (x < maxSizeX && y < maxSizeY && x >= 0 && y >= 0)
			return maze[x][y].walkable;
		return false;
	}
	
	public JPSNode getNode (int x, int y) {
		return maze[x][y];
	}
	
	public float toPointApprox(float x, float y, int tx, int ty){
		return (float) Math.sqrt(Math.pow(Math.abs(x-tx),2) + Math.pow(Math.abs(y-ty), 2));		
	}
	
	public int[] tmpInt (int x, int y){
		int[] tmpIntsTmpInt = {x,y};  //create the tmpInt's tmpInt[]
		return tmpIntsTmpInt;         //return it
	}
	public int[][] getNeighbors(JPSNode node){
		int[][] neighbors = new int[8][2];
		int x = node.x;
		int y = node.y;
		boolean d0 = false; //These booleans are for speeding up the adding of nodes.
		boolean d1 = false;
		boolean d2 = false;
		boolean d3 = false;
		
		if (walkable(x,y-1)){
			neighbors[0] = (tmpInt(x,y-1));
			d0 = d1 = true;
		}
		if (walkable(x+1,y)){
			neighbors[1] = (tmpInt(x+1,y));
			d1 = d2 = true;
		}
		if (walkable(x,y+1)){
			neighbors[2] = (tmpInt(x,y+1));
			d2 = d3 = true;
		}
		if (walkable(x-1,y)){
			neighbors[3] = (tmpInt(x-1,y));
			d3 = d0 = true;
		}
		if (d0 && walkable(x-1,y-1)){
			neighbors[4] = (tmpInt(x-1,y-1));
		}
		if (d1 && walkable(x+1,y-1)){
			neighbors[5] = (tmpInt(x+1,y-1));
		}
		if (d2 && walkable(x+1,y+1)){
			neighbors[6] = (tmpInt(x+1,y+1));
		}
		if (d3 && walkable(x-1,y+1)){
			neighbors[7] = (tmpInt(x-1,y+1));
		}
		return neighbors;
	}
	
}
