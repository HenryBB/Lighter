import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Ray implements Comparable<Ray> {

	Point2D tip; // the tip is an arbitrary point along the ray
	Point2D origin;
	double slope;
	int quadrant; // 1 is ++, 2 is -+, 3 is --, 4 is +-
	ArrayList<Point2D> intersections = new ArrayList<Point2D>();
	Obstructable obs;

	public Ray(Point2D origin) {
		this.origin = origin;
	}

	public Ray(Point2D origin, Point2D newTip) {
		this.origin = origin;
		setTip(newTip);
	}

	private int calculateQuadrant() {
		if ((tip.getY() >= origin.getY()) && (tip.getX() > origin.getX())) {
			return 1;
		} else if ((tip.getY() > origin.getY())
				&& (tip.getX() <= origin.getX())) {
			return 2;
		} else if ((tip.getY() <= origin.getY())
				&& (tip.getX() > origin.getX())) {
			return 3;
		} else {
			return 4;
		}
	}

	public void setTip(Point2D newTip) {
		tip = newTip;
		slope = (origin.getY() - tip.getY()) / (origin.getX() - tip.getX());
		quadrant = calculateQuadrant();

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

	public int getQuadrant() {
		return quadrant;
	}

	@Override
	public int compareTo(Ray ray) {
		// a ray is greater than another ray if it's angle is greater
		// first ray is the ray from which compareTo is being called
		Ray r = ray; // second ray - inputed ray
		int rQuadrant = r.getQuadrant();
		if (quadrant != rQuadrant) {
			if (quadrant > rQuadrant) {
				return 1;
			} // the first ray is greater than the second inputed ray
			else {
				return -1;
			} // otherwise the first ray is less than the second ray
		} else { // if both rays are in the same quadrant, the ray with the
					// larger slope has a bigger angle
			if (slope == r.getSlope()) {
				return 0;
			} // rays are equal if slopes and quadrants are the same
			if (slope > r.getSlope()) {
				return 1;
			} // the slope of the first ray is greater, thus has a larger angle
			else {
				return -1;
			} // otherwise the first ray has a smaller slope and thus has a
				// smaller angle
		}
	}

	public void updateLength() {
		for (Point2D inter : intersections) {
			if (inter != null)
				if (inter.distance(origin) < tip.distance(origin)) {
					tip = inter;
				}
		}
		intersections.clear();
	}
}
