import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

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
		
	public Point2D rayIntersection(Ray ray) {
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
			if (ray.getSlope()!=lineSlope) { //lines are not parallel - if the lines are colinear it should still not count as an intersection
				//find the b in y=mx+b for both ray and line segment
				double rayConstant = ray.getOrigin().getY() - (ray.getSlope()*ray.getOrigin().getX());
				double lineConstant = l.getY1() - (lineSlope*l.getX1());
				double intersectionX = (rayConstant-lineConstant)/(lineSlope-ray.getSlope());
				//really ugly if statement I know...
				if ( ((intersectionX>ray.getOrigin().getX())
						&& (ray.getOrigin().getX()<=ray.getTip().getX())) 
						|| ((intersectionX<ray.getOrigin().getX()) 
								&& (ray.getOrigin().getX()>ray.getTip().getX())) ) { //within the x bounds of the ray
					double maxX = Math.max(l.getX1(), l.getX2());
					double minX = Math.min(l.getX1(), l.getX2());
					if ((intersectionX>minX) && (intersectionX<maxX)) {
						intersectionPoint = new Point2D.Double(intersectionX,(lineSlope*intersectionX)+lineConstant);
					}
				}
			}
		}
		return intersectionPoint;
	}
	
}
