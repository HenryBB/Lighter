import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;

public class Runner extends BasicGame {

	public Runner(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	JFrame frame;
	ArrayList<Obstructable> obs = new ArrayList<Obstructable>();
	ArrayList<Light> lights = new ArrayList<Light>();
	Point2D pressedPoint = new Point2D.Double();
	Light l;

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Runner("Lights"));
		app.setDisplayMode(600, 600, false);
		app.setSmoothDeltas(true);
		app.setTargetFrameRate(80);
		app.setShowFPS(true);
		app.start();
	}

	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {
		g.setColor(Color.blue);
		g.fillRect(0, 0, 600, 600);
		for (Obstructable o : obs) {
			g.setColor(Color.black);
			g.drawRect(o.x, o.y, o.width, o.height);
		}

		ArrayList<Ray> rays = new ArrayList<Ray>();
		Line2D l1 = new Line2D.Float();
		for (Obstructable o : obs) {
			rays.add(new Ray(new Line2D.Float(l.location.x, l.location.y,
					(o.x - l.location.x) * 100 + o.x, (o.y - l.location.y)
							* 100 + o.y)));
			rays.add(new Ray(new Line2D.Float(l.location.x, l.location.y,
					((o.x + o.width) - l.location.x) * 100 + o.x + o.width,
					(o.y - l.location.y) * 100 + o.y)));
			rays.add(new Ray(new Line2D.Float(l.location.x, l.location.y,
					(o.x - l.location.x) * 100 + o.x,
					((o.y + o.height) - l.location.y) * 100 + o.y + o.height)));
			rays.add(new Ray(new Line2D.Float(l.location.x, l.location.y,
					((o.x + o.width) - l.location.x) * 100 + o.x + o.width,
					((o.y + o.height) - l.location.y) * 100 + o.y + o.height)));

		}

		g.setColor(Color.red);
		for (Obstructable o : obs) {
			int a = 0, b = 0;
			if (l.location.x >= o.getRect().getMinX()
					&& l.location.x <= o.getRect().getMaxX()
					&& l.location.y >= o.getRect().getMaxY()) {
				a = 3;
				b = 2;

			}
			if (l.location.x >= o.getRect().getMinX()
					&& l.location.x <= o.getRect().getMaxX()
					&& l.location.y <= o.getRect().getMinY()) {
				a = 1;
				b = 0;

			}
			if (l.location.y >= o.getRect().getMinY()
					&& l.location.y <= o.getRect().getMaxY()
					&& l.location.x <= o.getRect().getMinX()) {
				a = 2;
				b = 0;
			}
			if (l.location.y >= o.getRect().getMinY()
					&& l.location.y <= o.getRect().getMaxY()
					&& l.location.x >= o.getRect().getMaxX()) {
				a = 3;
				b = 1;
			}
			if (l.location.y >= o.getRect().getMaxY()
					&& l.location.x >= o.getRect().getMaxX()) {
				a = 2;
				b = 1;
			}
			if (l.location.y >= o.getRect().getMaxY()
					&& l.location.x <= o.getRect().getMinX()) {
				a = 3;
				b = 0;
			}
			if (l.location.y >= o.getRect().getMaxY()
					&& l.location.x <= o.getRect().getMinX()) {
				a = 3;
				b = 0;
			}
			if (l.location.y <= o.getRect().getMinY()
					&& l.location.x <= o.getRect().getMinX()) {
				a = 2;
				b = 1;
			}
			if (l.location.y <= o.getRect().getMinY()
					&& l.location.x >= o.getRect().getMaxX()) {
				a = 3;
				b = 0;
			}

			g.setColor(new Color(.2f, .2f, .2f, .5f));
			g.fill(new Polygon(new float[] { (float) o.getCorners()[a].getX(),
					(float) o.getCorners()[a].getY(),
					(float) o.getCorners()[b].getX(),
					(float) o.getCorners()[b].getY(),
					(float) rays.get(b).line.getX2(),
					(float) rays.get(b).line.getY2(),
					(float) rays.get(a).line.getX2(),
					(float) rays.get(a).line.getY2() }));
			g.setColor(new Color(.2f, .2f, .2f, 1f));
			g.fill(new Polygon(new float[] { (float) o.getCorners()[0].getX(),
					(float) o.getCorners()[0].getY(),
					(float) o.getCorners()[1].getX(),
					(float) o.getCorners()[1].getY(),
					(float) o.getCorners()[3].getX(),
					(float) o.getCorners()[3].getY(),
					(float) o.getCorners()[2].getX(),
					(float) o.getCorners()[2].getY() }));
		}

//		 for (Ray ray : rays) {
//		 Line2D l2d = ray.line;
//		 g.setColor(Color.white);
//		 g.drawLine((float) l2d.getX1(), (float) l2d.getY1(),
//		 (float) l2d.getX2(), (float) l2d.getY2());
//		 }
	}

	// Returns 1 if the lines intersect, otherwise 0. In addition, if the lines
	// intersect the intersection point may be stored in the floats i_x and i_y.

	@Override
	public void init(GameContainer arg0) throws SlickException {
		// TODO Auto-generated method stub
		l = new Light();
		lights.add(l);
		// obs.add(new Obstructable(0, 0, 600, 600));
	}

	@Override
	public void update(GameContainer gc, int arg1) throws SlickException {
		Input in = gc.getInput();
		l.setLocation(new Point(in.getAbsoluteMouseX(), in.getAbsoluteMouseY()));
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button == 0) {
			pressedPoint.setLocation(x, y);
		}
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		if (button == 0) {
			if (pressedPoint != null) {
				if ((int) (x - pressedPoint.getX()) > 0
						&& (int) (y - pressedPoint.getY()) > 0)
					obs.add(new Obstructable((int) pressedPoint.getX(),
							(int) pressedPoint.getY(), (int) (x - pressedPoint
									.getX()), (int) (y - pressedPoint.getY())));
				// }
			}
		}
	}

}
