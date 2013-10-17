import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Obstructable {

	int x, y, width, height;

	public Obstructable(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public Rectangle2D getRect() {
		return new Rectangle2D.Double(x, y, width, height);
	}

	public Point2D[] getCorners() {
		return new Point2D[] { new Point2D.Float(x, y),
				new Point2D.Float(x + width, y),
				new Point2D.Float(x, y + height),
				new Point2D.Float(x + width, y + height) };
	}
}
