package autoGraph;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

/**
 * 
 * @author Marian Hieke
 *Class to represent a Edge of a graph
 */
public class EdgeComp {
	
	private NodeComp nodeA; //the first node belonging to the EdgeComp
	private NodeComp nodeB; //the second node belonging to the EdgeComp
	private VectorComp posA; //Start Vector of the EdgeComp
	private VectorComp posB; //End Vector of the EdgeComp
	
	
	public EdgeComp(NodeComp nodeA, NodeComp B, VectorComp posA, VectorComp posB){
		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.posA = posA;
		this.posB = posB;	
	}
	
	public EdgeComp(NodeComp nodeA, NodeComp nodeB){
		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.posA = new VectorComp(0,0);
		this.posB = new VectorComp(0,0);
		
	}
	
	/**
	 * checks if the given EdgeComp crosses this EdgeComp
	 * @param EdgeComp 
	 * @return crossing : true if the given EdgeComp crosses this EdgeComp
	 */
	public boolean isCrossing(EdgeComp EdgeComp){
		boolean crossing = false;
	
		Line2D.Double line1 = new Line2D.Double(this.posA.getX(), this.posA.getY(), this.posB.getX(), this.posB.getY());
		Line2D.Double line2 = new Line2D.Double(EdgeComp.getPosA().getX(), EdgeComp.getPosA().getY(), EdgeComp.getPosB().getX(), EdgeComp.getPosB().getY());
		crossing = line1.intersectsLine(line2);
	
		
		return crossing;
	}
	
	/**
	 * checks if the given EdgeComp overlaps this EdgeComp
	 * @param EdgeComp
	 * @return overlapping :  true if the given EdgeComp overlaps with this EdgeComp
	 */
	public boolean isOverlappingg(EdgeComp EdgeComp){
		boolean overlapping;
		boolean point1;
		boolean point2 = false;
		boolean point3 = false;
		boolean point4 = false;
		
		Line2D.Double line1 = new Line2D.Double(this.posA.getX(), this.posA.getY(), this.posB.getX(), this.posB.getY());
		Line2D.Double line2 = new Line2D.Double(EdgeComp.getPosA().getX(), EdgeComp.getPosA().getY(), EdgeComp.getPosB().getX(), EdgeComp.getPosB().getY());
		
		point1 = line1.contains(EdgeComp.posA.getX(), posA.getY());
		point2 = line1.contains(EdgeComp.getPosB().getX(), EdgeComp.getPosB().getY());
		point3 = line2.contains(this.posA.getX(), this.posA.getY());
		point3 = line2.contains(this.posB.getX(), this.posB.getY());
		
		overlapping = point1 | point2 | point3 | point4;
		
		return overlapping;
	}
	
	public boolean intersects(NodeComp node, double radius){
		boolean inter = false;
		
		Ellipse2D.Double circle = new Ellipse2D.Double(node.getPosition().getX()-radius, node.getPosition().getY()-radius, radius*2, radius*2);
	
		Line2D.Double line = new Line2D.Double(this.nodeA.getPosition().getX(), this.nodeA.getPosition().getY(), this.nodeB.getPosition().getX(), this.nodeB.getPosition().getY());
		
		if(line.intersects(circle.getBounds2D())){
		
			inter = true;
		}
		return inter;
	}
	
	
	
	/**
	 * 
	 * @return length the length of the EdgeComp
	 */
	public double getLength(){
		double length = 0;
		
		return length;
	}
	
	
	
	/**
	 * 
	 * @return nodeA the first node belonging to the EdgeComp
	 */
	public NodeComp getNodeA(){
		return this.nodeA;
	}
	
	/**
	 * 
	 * @return nodeB the second node belonging to the EdgeComp
	 */
	public NodeComp getNodeB(){
		return this.nodeB;
	}
	
	/**
	 * 
	 * @return posA the start Vector of the EdgeComp
	 */
	public VectorComp getPosA(){
		return this.posA;
	}
	
	/**
	 * 
	 * @return posA the start Vector of the EdgeComp
	 */
	public VectorComp getPosB(){
		return this.posB;
	}
	/**
	 * sets the start Vector to the given Vector
	 * @param posA
	 */
	public void setPosA(VectorComp posA){
		this.posA = posA;
	}

	/**
	 * sets the end Vector to the given Vector
	 * @param posB
	 */
	public void setPosB(VectorComp posB){
		this.posB = posB;
	}
	
	
	/**
	 * computes  posA or posB depending on isPosA
	 * isPosA = true =>compute posA, else compute posB
	 * @param isPosA
	 */
	public void computePosAOrposB(boolean isPosA){
	
		NodeComp node1; 
		NodeComp node2;
		if(isPosA){
			node1 = this.nodeA;
			node2 = this.nodeB;
		}else{
			node1 = this.nodeB;
			node2 = this.nodeA;
		}
		
		VectorComp dir = node2.getPosition().sub(node1.getPosition()); 
		dir = dir.div(dir.norm());
	
		dir = dir.mul(node1.getRadius());
	
		
		if(isPosA){
		
			this.posA = node1.getPosition().add(dir); 
			//node1.setPosition(posA);
		
		}else{
			
			this.posB = node1.getPosition().add(dir);
			//node1.setPosition(posB);
			
		}
		
	}
	
	/**
	 * computes  posA or posB depending on isPosA
	 * isPosA = true =>compute posA, else compute posB
	 * @param isPosA
	 */
	public void computePosAOrposB(boolean isPosA, double zoom){
	
		NodeComp node1; 
		NodeComp node2;
		if(isPosA){
			node1 = this.nodeA;
			node2 = this.nodeB;
		}else{
			node1 = this.nodeB;
			node2 = this.nodeA;
		}
		
		VectorComp dir = node2.getPosition().sub(node1.getPosition());
		dir = dir.div(dir.norm());
		dir = dir.mul(node1.getRadius()*zoom);
		
		if(isPosA){
			this.posA = node1.getPosition().add(dir);
		}else{
			this.posB = node1.getPosition().add(dir);
		}
		
	}
	/**
	 * computes posA and posB
	 */
	public void computeposAandPosB(){
		this.computePosAOrposB(true);
		this.computePosAOrposB(false);
	}
	/**
	 * computes posA and posB
	 */
	public void computeposAandPosB(double zoom){
		this.computePosAOrposB(true, zoom);
		this.computePosAOrposB(false, zoom);
	}
	
	
	public void setNodeA(NodeComp node){
		this.nodeA = node;
	}
	
	public void setNodeB(NodeComp node){
		this.nodeB = node;
	}
	
	public String toString(){
		return "von: " +this.nodeA.getId() +" nach:  " + this.nodeB.getId();
	}
}
