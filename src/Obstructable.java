import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Obstructable {
	Point2D[] vertices;
	Line2D[] lines;

	public Obstructable(Point2D[] points,Line2D[] lines) {
		vertices = points;
		this.lines = lines;
	}

	public Point2D[] getVertices() {
		return vertices;
	}
	public Line2D[] getLines() {
		return lines;
	}
	
	public Point2D rayIntersectPoint(Ray ray) {
		Point2D intersectionPoint = null;
		//check if there is no possibility for the line can intersect
		//Cases where the line cannot intersect ever:
		//	- the slope of the line and the ray are the same (parallel)
		//	- the line is on the "other" side of the ray origin
		//		- both of the line's points are below the origin and the ray slope is positive
		//		- both the line's points are
		for (Line2D l : lines) {
			//if (ray.getSlope()!= (l.getY2()-l.getY1())/(l.getX2()-l.getX1()) ) { //lines are not parallel
			//Ray is represented as origin + scale*(ax,ay) or p+t(r)
			//line is represented as point + scale*(qx,qy) or q+u(s) - for this case u is assumed to be 1
			//t = (q - p) × s / (r × s) where x is the cross product
			//if (r x s) is zero the lines are parallel
			//if (q-p) x r is zero the lines are colinear
			//if 0 <= t <= 1 the ray and line intersect, otherwise they don't.
			//the intersection point is p + tr
			double denominator = VectorUtil.cross(ray.getTip(),l.getP2());
			if (denominator !=0) { //lines are not parallel
				Point2D qpDif = new Point2D.Double(l.getP1().getX()-ray.getOrigin().getX(), l.getP1().getY()-ray.getOrigin().getY());
				double numerator = VectorUtil.cross(qpDif,ray.getTip());
				if (numerator !=0 ) { //lines are not colinear
					double t = numerator/denominator;
					if ( (t>=0) && (t<=1) ) { //line and ray intersected
						intersectionPoint = new Point2D.Double(t*ray.getTip().getX(),t*ray.getTip().getY());
					}
				}
				
			}
		}
		return intersectionPoint;
	}
	
}
