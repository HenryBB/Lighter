import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;

class Light {
	
	Point2D location;
	ArrayList<Ray> rays = new ArrayList<Ray>();
	
	public Light(){}
	public Light(Point2D p) {
		location = p;
	}
	
	public void setLocation(Point2D p)
	{
		location = p;
	}
	public Point2D getLoc() { //gets the light location
		return location;
	}
	public void sortRays() {
		Collections.sort(rays);
	}
	public void addRay(Ray ray) {
		rays.add(ray);
	}
	public Ray ray(int i) {
		return rays.get(i);
	}
	public void clearRays() {
		rays.clear();
	}

}