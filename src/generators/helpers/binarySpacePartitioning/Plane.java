package generators.helpers.binarySpacePartitioning;

/**
 * A plane used for polygon splitting and classification during 
 * BSP tree creation and traversal. Mostly self-explanatory.
 */
public class Plane {
	
	private float PLANE_THICKNESS = 0.1f;
	
	private float _distance;
	private Vector _normal;
	
	 Plane(Polygon polygon) {
		  _normal = polygon.calcNormal().calcDirection();
		  _distance = polygon.getPoint(0).dot(_normal);
	 }
	    
	 public HalfSpace classify(Vector point)
	 {
	   float distance = calcDistance(point);
	   if (distance > PLANE_THICKNESS) return HalfSpace.hsPositive;
	   else if (distance < -PLANE_THICKNESS) return HalfSpace.hsNegative;
	   return HalfSpace.hsInsidePlane;
	 }

	 public float calcDistance(Vector point)
	 {
	   return point.dot(_normal) - _distance;
	 }

	 public Vector intersect(Vector point1, Vector point2)
	 {
	   float t = -calcDistance(point1) / (_normal.dot(point2.subtract(point1)));
	   return point1.add(point2.subtract(point1).multiply(t));
	 }
	 
	 public Vector intersects(Vector point1, Vector point2)
	 {
	   float t = -calcDistance(point1) / (_normal.dot(point2.subtract(point1)));
	   return point1.add(point2.subtract(point1).multiply(t));
	 }

	 /**
	  * @see HalfSpace
	  */
	 public HalfSpace classify(Polygon polygon)
	 {
	   int numFrontPoints = 0, numBackPoints = 0;
	   for (int i = 0; i < polygon.getNumPoints(); i++)
	   {
	     switch (classify(polygon.getPoint(i)))
	     {
	       case hsPositive:
	         numFrontPoints++;
	         continue;
	       case hsNegative:
	         numBackPoints++;
        case hsClipped:
          break;
        case hsInsidePlane:
          break;
        default:
          break;
	     }
	   }
	   if ((numFrontPoints > 0) && (numBackPoints > 0)) return HalfSpace.hsClipped;
	   else if (numFrontPoints > 0) return HalfSpace.hsPositive;
	   else if (numBackPoints > 0) return HalfSpace.hsNegative;
	   return HalfSpace.hsInsidePlane;
	 }

	 public boolean split(Polygon polygon, Polygon front, Polygon back)
	 {
		 if (classify(polygon) == HalfSpace.hsClipped) {
			 Vector s = intersect(polygon.getPoint(0), polygon.getPoint(1));
			 
			 if (classify(polygon.getPoint(0)) == HalfSpace.hsPositive) {
				 front.set(new Vector[] {polygon.getPoint(0), s});
				 back.set(new Vector[] {s, polygon.getPoint(1)});
			 } else {
				 back.set(new Vector[] {polygon.getPoint(0), s});
				 front.set(new Vector[] {s, polygon.getPoint(1)});
			 }
			 return true;
		 }
		 return false;
	  
	 }
}
