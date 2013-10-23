import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class VectorUtil {
	public static double cross(Point2D p1, Point2D p2) {
		// this function takes the cross product of two points, treating them
		// like vectors
		// - the cross product of two vectors is a scalar for a third vector
		// perpendicular to both
		double result = p1.getX() * p2.getY() - p1.getY() * p2.getX();
		return result;
	}

	public static Point2D findIntersection(Line2D r1, Line2D r2) {
		// y=a*x+b
		if (r1.intersectsLine(r2)) {
			float x1 = (float) r1.getX1();
			float y1 = (float) r1.getY1();
			float x2 = (float) r1.getX2();
			float y2 = (float) r1.getY2();

			float x3 = (float) r2.getX1();
			float y3 = (float) r2.getY1();
			float x4 = (float) r2.getX2();
			float y4 = (float) r2.getY2();

			float d = ((x1 - x2) * (y3 - y4)) - ((y1 - y2) * (x3 - x4));
			if (d == 0) // If parallel, defaults to the average location of the
						// lines.
				return new Point2D.Float((x1 + x3) * 0.5f, (y1 + y3) * 0.5f);
			else {
				float a = (x1 * y2) - (y1 * x2);
				float b = (x3 * y4) - (y3 * x4);
				return new Point2D.Float(((a * (x3 - x4)) - ((x1 - x2) * b))
						/ d, ((a * (y3 - y4)) - ((y1 - y2) * b)) / d);
			}
		}
		return null;
	}

}
