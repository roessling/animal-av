package autoGraph;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

/**
 * 
 * @author Marian Hieke
 *
 */
public class NodeComp {

	private VectorComp pos; //the Vector of the node
	private double radius; //the radius of the node
	private int id; // the id of the node

	private VectorComp disp;
	
	public NodeComp(double radius, int id){
		this.radius = radius;
		this.id = id;
	}
	
	public NodeComp(VectorComp pos, double radius, int id){
		this.pos = pos;
		this.id = id;
		this.radius = radius;

	}
	
	 
	
	/**
	 * checks if the given node overlaps with this node
	 * Attention: with this test the round nodes could maybe not overlap but there bounds do
	 * @param node
	 * @return overlapping : true if the given node 
	 */
	public boolean isOverlapping(NodeComp node){
		boolean overlapping = false;
		int caseX = 0;
		int caseY = 0;
		int caseTmp = 0;
		
		double x1WithRadius;
		double y1WithRadius;
		double x2WithRadius;
		double y2WithRadius;
		
		
		caseX = Double.compare(this.pos.getX(), node.getPosition().getX());
		caseY = Double.compare(this.pos.getY(), node.getPosition().getY());
		
		if(caseX < 0){
			x1WithRadius = this.pos.getX() +this.radius;
			x2WithRadius = node.getPosition().getX() - node.getRadius();
			
			caseTmp = Double.compare(x1WithRadius, x2WithRadius);
			
			if(caseTmp > 0){
				if(caseY < 0){
					y1WithRadius = this.pos.getY() +this.radius;
					y2WithRadius = node.getPosition().getY() - node.getRadius();
					
					caseTmp = Double.compare(y1WithRadius, y2WithRadius);
					if(caseTmp > 0){
						overlapping = true;
					}
				}else{
					if(caseY > 0){
						y1WithRadius = this.pos.getY() - this.radius;
						y2WithRadius = node.getPosition().getY() + node.getRadius();
						
						caseTmp = Double.compare(y1WithRadius, y2WithRadius);
						if(caseTmp < 0){
							overlapping = true;
						}
						
					}else{
						overlapping = true;
					}
				}
			}
			
		}else{
			if(caseX > 0){
				
				x1WithRadius = this.pos.getX() - this.radius;
				x2WithRadius = node.getPosition().getX() + node.getRadius();
				
				caseTmp = Double.compare(x1WithRadius, x2WithRadius);
				
				if(caseTmp  < 0){
					if(caseY < 0){
						y1WithRadius = this.pos.getY() +this.radius;
						y2WithRadius = node.getPosition().getY() - node.getRadius();
						
						caseTmp = Double.compare(y1WithRadius, y2WithRadius);
						if(caseTmp > 0){
							overlapping = true;
						}
					}else{
						if(caseY > 0){
							y1WithRadius = this.pos.getY() - this.radius;
							y2WithRadius = node.getPosition().getY() + node.getRadius();
							
							caseTmp = Double.compare(y1WithRadius, y2WithRadius);
							if(caseTmp < 0){
								overlapping = true;
							}
							
						}else{
							overlapping = true;
						}
					}
				}
				
			}else{
				if(caseY < 0){
					y1WithRadius = this.pos.getY() +this.radius;
					y2WithRadius = node.getPosition().getY() - node.getRadius();
					
					caseTmp = Double.compare(y1WithRadius, y2WithRadius);
					if(caseTmp > 0){
						overlapping = true;
					}
				}else{
					if(caseY > 0){
						y1WithRadius = this.pos.getY() - this.radius;
						y2WithRadius = node.getPosition().getY() + node.getRadius();
						
						caseTmp = Double.compare(y1WithRadius, y2WithRadius);
						if(caseTmp < 0){
							overlapping = true;
						}
						
					}else{
						overlapping = true;
					}
				}
				
			}
		}
		
		
		return overlapping;
	}
	
	
	
	/**
	 * 
	 * @param edge
	 * @return true if at least a part of the node lays on the given edge
	 */
	public boolean laysOnEdge(EdgeComp edge){
		boolean laysOn = false;
		Line2D.Double line = new Line2D.Double(edge.getPosA().getX(), edge.getPosA().getY(),  edge.getPosB().getX(),  edge.getPosB().getY());
	 	Ellipse2D.Double  circle= new Ellipse2D.Double(this.pos.getX()-this.radius, this.pos.getY()-this.radius,this.radius *2, this.radius * 2);
		line.intersects(circle.getBounds2D());
	 	
		return laysOn;
	}
	
	/**
	 * sets the radius to the given value
	 * @param radius
	 */
	public void setRadius(double radius){
		this.radius = radius;
	}
	
	/**  
	 * @return the radius of the node
	 */
	public double getRadius(){
		return this.radius;
	}
	
	/**
	 * sets the position to the given Vector
	 * @param pos
	 */
	public void setPosition(VectorComp pos){
		this.pos = pos;
	}
	
	/**
	 * @return the position of the node
	 */
	public VectorComp getPosition(){
		return this.pos;
	}
	
	/**
	 * sets the Vector to the given Vector
	 * @param pos
	 */
	public void setDisPosition(VectorComp pos){
		this.disp = pos;
	}
	
	/**
	 * @return the Vector of the node
	 */
	public VectorComp getDisPosition(){
		return this.disp;
	}
	
	/**
	 * set the id of the node to the given value
	 * @param id
	 */
	public void setId(int id){
		this.id = id;
	}
	
	/**
	 * 
	 * @return the id of this node
	 */
	public int getId(){
		return this.id;
	}	
	
}
