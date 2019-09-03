package generators.helpers;

import java.awt.Color;
import java.util.ArrayList;

import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;

public class MyNode {
	
	private final int self = 0;
	private final int notConnected = Integer.MAX_VALUE;
	private final String[] names = {"A","B","C","D","E","F","G","H","I","J"};
	
	private String name;
	private int index;
	
	private int[][] routingTable;
	private int[] distanceVector;
	private int[] newDistanceVector;
	private ArrayList<MyNode> neighbours;
	
	private Color highlightNew;
	private Color highlightShortest;
	
	/**
	 * 
	 * @param name Name of the node
	 * @param index it index in the graph. must be between 0 and n-1
	 * @param adjazenmatrix a quadratic table which stores all connections
	 */
	public MyNode(String name, int index, int[][] adjazenmatrix, Color highlightNew, Color highlightOld){
		this.name=name;
		this.index = index;
		this.highlightNew = highlightNew;
		this.highlightShortest = highlightOld;
		init(adjazenmatrix);
	}
	
	private void init(int[][] adjazenmatrix) {
		int size = adjazenmatrix.length;
		routingTable = new int[size][size];
		for(int i = 0; i<size;i++) {
			for(int j = 0; j<size;j++) {
				if (i==index || j==index ) routingTable[i][j]=notConnected;
				else if(i==j) {
					routingTable[i][j]=adjazenmatrix[index][i];
				} else {
					routingTable[i][j] = notConnected;
				}
			}
		}
		
		distanceVector = new int[size];
		newDistanceVector=new int[size];
		for(int i=0;i< size;i++) {
			distanceVector[i]=Integer.MAX_VALUE;
			newDistanceVector[i]=Integer.MAX_VALUE;
		}
		firstUpdate();

		
	}

	public void initNeighbours(MyNode[] nodes) {
		neighbours = new ArrayList<MyNode>();
		for(int i=0; i<distanceVector.length;i++ ) {
			if (distanceVector[i]!= notConnected && distanceVector[i] != self) {
				neighbours.add(nodes[i]);
			}
		}
	}

	public int[][] getRoutingTable() {
		return routingTable;
	}

	public String getName() {
		return name;
	}

	public int[] getDistanceVector() {
		return distanceVector;
	}

	public boolean createUpdatedDistanceVector(Language lang, Text statusText, Text calc, String gridName) {
		
		statusText.setText("Update Distance Vector of Node "+ name, null, null);
		int size = newDistanceVector.length; 
		boolean update = false;
		int smallest = Integer.MAX_VALUE;
		int storedIndex = -1;
		for(int i = 0; i < size;i++ ) {
			for(int j=0;j<size; j++) {
				if(index!=j && routingTable[i][j]<smallest) {
					smallest = routingTable[i][j];
					storedIndex=j;
				}
			}
			if(smallest< distanceVector[i] && i!= index) {
				newDistanceVector[i]= smallest;
				lang.addLine("setGridColor \""+ gridName+"["+(i+1)+"]["+(storedIndex+1)+"]\"" + getColorString(highlightNew));
				update=true;
//				System.out.println("UPDATE " + index +" " +i + " "+smallest);
			} else if (smallest != notConnected && smallest == distanceVector[i] && i!=index) {
				newDistanceVector[i]= smallest;
				lang.addLine("setGridColor \""+ gridName+"["+(i+1)+"]["+(storedIndex+1)+"]\"" + getColorString(highlightShortest));
			}
			smallest = Integer.MAX_VALUE;
		}
				
		newDistanceVector[index]= Integer.MAX_VALUE;
		return update;
	}

	private boolean firstUpdate() {
		int size = distanceVector.length; 
		boolean update = false;
		int smallest = Integer.MAX_VALUE;
		for(int i = 0; i < size;i++ ) {
			for(int j=0;j<size; j++) {
				if(index!=j && routingTable[i][j]<smallest) smallest = routingTable[i][j];
			}
			if(smallest< distanceVector[i] && i!= index) {
				distanceVector[i]= smallest;
				update=true;
			}
			smallest = Integer.MAX_VALUE;
		}
		distanceVector[index]= Integer.MAX_VALUE;
		return update;
	}
	

	public void processNeighbourInfo(Language lang, StringMatrix stringMatrix, Text statusText, Text calc, Variables vars) {
		vars.declare("int","ThisRoundRoutingTableUpdates","0"); int count = 0;
		for(MyNode n : neighbours) {			
			statusText.setText("Processing Distance Vector "+ n.getDistanceVectorAsText()+ " from Node " + n.getName(),null,null);
			lang.nextStep();
			int send = n.getIndex();
			int dv[] = n.getDistanceVector();			
			for(int i = 0; i< dv.length;i++) {
				if(i!= index && dv[i]!=Integer.MAX_VALUE) {
					calc.setText("Way to " + names[i] +" via " +n.getName() +" is " + dv[i]+" + " + routingTable[send][send] +" = " + (dv[i]+routingTable[send][send]),null,null);
					if(dv[i]+routingTable[send][send]<routingTable[i][send] && i!=index) {
						lang.nextStep();
						calc.setText("Way to " + i +" via " +n.getName() +" is " + (dv[i]+routingTable[send][send]) +": Update Table",null,null  );
						routingTable[i][send]=dv[i]+routingTable[send][send];
						stringMatrix.put(i+1, send+1, String.valueOf(routingTable[i][send]), null, null);
						lang.addLine("setGridColor \"" + stringMatrix.getName() + "["+(i+1)+"]["+(send+1)+"]\" color (255 ,  0 ,0)");
						vars.set("UpdatesInRoutingTables", String.valueOf(Integer.valueOf(vars.get("UpdatesInRoutingTables"))+1));
						count++;
						vars.set("ThisRoundRoutingTableUpdates", String.valueOf(count));
					}
					lang.nextStep();
				}
				calc.setText("", null,null);
				
			}			
		}		
//		lang.nextStep();
	}

	public int getIndex() {
		return index;
	}

	public ArrayList<MyNode> getNeighbours() {
		return neighbours;
		
	}
	
	public String getDistanceVectorAsText() {
		return distanceVectorToText(distanceVector);
	}
	
	public String getNewDistanceVectorAsText(){
		return distanceVectorToText(newDistanceVector);
	}
	
	private String distanceVectorToText(int[] dv) {
		String s = "[";
		for(int i: dv) {
			switch(i) {
				case self: s+="#, "; break;
				case notConnected:s+="#, "; break;
				default: s+=i+", ";
			}
		}
		s= s.substring(0,s.length()-2);
		s+="]";
		return s;
	}

	public void switchDistanceVector() {
		for(int i=0; i<distanceVector.length;i++) {
			distanceVector[i]=newDistanceVector[i];		
		}
	}
	
	private String getColorString(Color c) {
		return "color (" + c.getRed()+" , " + c.getGreen() + " , "+ c.getBlue()+")";
	}
}
