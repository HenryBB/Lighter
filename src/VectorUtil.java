import java.awt.geom.Point2D;


public class VectorUtil {
	public static double cross(Point2D p1, Point2D p2) {
		//this function takes the cross product of two points, treating them like vectors
		//	- the cross product of two vectors is a scalar for a third vector perpendicular to both
		double result = p1.getX()*p2.getY()-p1.getY()*p2.getX();
		return result;
	}
}
