import java.awt.geom.Point2D;
import java.util.ArrayList;


public class Ray implements Comparable{
	
	Point2D tip; //the tip is an arbitrary point along the ray
	Point2D origin;
	double slope;
	ArrayList<Point2D> intersections = new ArrayList<Point2D>();
	
	public Ray(Point2D origin)
	{
		this.origin = origin;
	}
	public void setTip(Point2D newTip) {
		tip = newTip;
		slope = ( origin.getY()-tip.getY() )/( origin.getX()-tip.getX() );
	}
	public double getSlope() {
		return slope;
	}
	public Point2D getOrigin() {
		return origin;
	}
	public Point2D getTip() {
		return tip;
	}
	@Override
	public int compareTo(Object r) {
		Ray ray = (Ray)r;
		if (slope>ray.slope)
			return 1;
		else if (slope==ray.slope)
			return 0;
		else
			return-1;
	}
}
