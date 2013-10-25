import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import org.newdawn.slick.geom.Polygon;

public class Obstructable {
	Point2D[] vertices;
	Line2D[] lines;

	public Obstructable(Point2D[] points) {
		vertices = points;
		this.lines = new Line2D[points.length];
		for (int i=0;i<points.length;i++)
		{
			Line2D line = null;
			if (i<points.length-1)
				line = new Line2D.Float(points[i], points[i+1]);
			else
				line = new Line2D.Float(points[i],points[0]);
			this.lines[i]=line;
		}
	}
	
	public Point2D[] getVertices() {
		return vertices;
	}
	public void calculateLines() { //creates lines connecting each vertex to the next - assumes they are in order
		lines = new Line2D[vertices.length];
		for (int i=0; i<vertices.length; i++) {
			int j=i+1;
			if (j>=vertices.length) {j=0;}
			lines[i] = new Line2D.Float(vertices[i],vertices[j]);
		}
	}
	public Line2D[] getLines() {
		return lines;
	}
	public Polygon getShape()
	{
		Polygon p = new Polygon();
		for (Point2D p2d : getVertices())
			p.addPoint((float) p2d.getX(), (float) p2d.getY());
		return new Polygon();
	}
	
	private double kross(double ax, double ay, double bx, double by) { //the magnitude of a two-dimensional cross product
		return (ax*by)-(ay*bx);
	}
	private double kross(Point2D a, Point2D b) { //the magnitude of a two-dimensional cross product
		return (a.getX()*b.getY())-(a.getY()*b.getX());
	}
	private Point2D pSubtract(Point2D a, Point2D b) {
		return new Point2D.Double(a.getX()-b.getX(),a.getY()-b.getY());
	}
	private Point2D pAddition(Point2D a, Point2D b) {
		return new Point2D.Double(a.getX()+b.getX(),a.getY()+b.getY());
	}
	private Point2D pMultScale(Point2D a, double c) { //multiply point by a scalar
		return new Point2D.Double(a.getX()*c,a.getY()*c);
	}
	
	public Point2D rayLineIntersectionKross(Ray ray,Line2D line) {
		Point2D intersection = null;
		//p+t*r defines the ray - p is the origin, r is the delta to the arbitrary point and t is a scalar
		//	t>=0
		//q+u*s defines the line segment - q is a point, s is the delta to the other point and u is a scalar
		//	0<u<1 - not include equals so the endpoints don't count as intersections
		Point2D p = ray.origin;
		Point2D r = new Point2D.Double(ray.tip.getX()-ray.origin.getX(),ray.tip.getY()-ray.origin.getY());
		Point2D q = line.getP1();
		Point2D s = new Point2D.Double(line.getP2().getX()-line.getP1().getX(),line.getP2().getY()-line.getP1().getY());
		double RkrossS = kross(r,s);
		if (RkrossS!=0) { //if zero the lines are parallel
			double t = kross(pSubtract(q,p),s)/RkrossS;
			double u = kross(pSubtract(q,p),r)/RkrossS;
			if (t>0) { //the scalar follow the direction of the ray
				if ( (u>0) && (u<1) ) { //0<u<1 - not include equals so the endpoints don't count as intersections
					intersection = pAddition(p,pMultScale(r,t));
				}
			}
		}
		return intersection;
	}
	
	public Point2D rayIntersection1(Ray ray) {
		Point2D intersection = null;
		Point2D possibleIntersection;
		double distanceSq = 0;
		int i=0;
		for (i=0; i<lines.length; i++) {
			intersection = rayLineIntersectionKross(ray,lines[i]);
			if (intersection!=null) {
				distanceSq = intersection.distanceSq(ray.origin);
				break;
			}
		}
		if (intersection==null) {return null;}
		for (int j=i; j<lines.length; j++) {
			possibleIntersection = rayLineIntersectionKross(ray,lines[j]);
			if (possibleIntersection!=null) {
				double possibleDistance = possibleIntersection.distanceSq(ray.origin);
				if (possibleDistance<distanceSq) {
					intersection = possibleIntersection;
				}
			}
		}

		return intersection;
	}
	
	public Point2D rayIntersection2(Ray ray) {
		Point2D intersectionPoint = null;
		//check if there is no possibility for the line can intersect
		//Cases where the line cannot intersect ever:
		//	- the slope of the line and the ray are the same (parallel)
		//	- the line is on the "other" side of the ray origin
		//		- both of the line's points are below the origin and the ray slope is positive
		//		- both the line's points are
		for (Line2D l : lines) {
			intersectionPoint = null;
			/*
			This method assumes that both the ray and line segment are infinite lines.
			If infinite lines are not parallel, they must intersect.
			I'm solving for the intersection point, and seeing if the x is within the bounds of both the ray and line segment.
			 */
			double lineSlope = (l.getY2()-l.getY1())/(l.getX2()-l.getX1());
			System.out.println("Line points: "+l.getY2()+" "+l.getY1()+" "+l.getX2()+" "+l.getX1());
			System.out.println("slopes: "+lineSlope + " : " + ray.getSlope());
			if (ray.getSlope()!=lineSlope) { //lines are not parallel - if the lines are colinear it should still not count as an intersection
				System.out.println("not parallel");
				//find the b in y=mx+b for both ray and line segment
				double rayConstant = ray.getOrigin().getY() - (ray.getSlope()*ray.getOrigin().getX());
				double lineConstant = l.getY1() - (lineSlope*l.getX1());
				System.out.println("constants: "+lineConstant + " : " + rayConstant);
				
				double intersectionX = (rayConstant-lineConstant)/(lineSlope-ray.getSlope());
				//really ugly if statement I know...
				if ( ((intersectionX>ray.getOrigin().getX())
						&& (ray.getOrigin().getX()<=ray.getTip().getX())) 
						|| ((intersectionX<ray.getOrigin().getX()) 
								&& (ray.getOrigin().getX()>ray.getTip().getX())) ) { //within the x bounds of the ray
					System.out.println("within bounds for ray "+intersectionX);
					double maxX = Math.max(l.getX1(), l.getX2());
					double minX = Math.min(l.getX1(), l.getX2());
					if ((intersectionX>minX) && (intersectionX<maxX)) {
						System.out.println("within bounds for segment");
						intersectionPoint = new Point2D.Double(intersectionX,(lineSlope*intersectionX)+lineConstant);
						return intersectionPoint;
					}
				}
			}
		}
		return intersectionPoint;
	}
	
}
