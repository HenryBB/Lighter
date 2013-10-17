import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;


public class Ray {
	
	Line2D line = new Line2D.Float();
	ArrayList<Point2D> intersections = new ArrayList<Point2D>();
	
	public Ray(Line2D l)
	{
		line = l;
	}
}
