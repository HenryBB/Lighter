import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
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
		g.setColor(Color.white);
		g.fillRect(0, 0, 600, 600);

		for (Obstructable o : obs) {
			ArrayList<Double> coords = new ArrayList<Double>();
			for (int i = 0; i < o.getVertices().length; i++) {
				coords.add(o.getVertices()[i].getX());
				coords.add(o.getVertices()[i].getY());
			}
			g.setColor(Color.black);
			for (Line2D l2d : o.getLines()) {
				g.drawLine((float) l2d.getX1(), (float) l2d.getY1(),
						(float) l2d.getX2(), (float) l2d.getY2());
			}

			Polygon poly = new Polygon();
			for (Point2D p2d : o.getVertices())
				poly.addPoint((float) p2d.getX(), (float) p2d.getY());
			g.setColor(Color.black);
			g.fill(poly);
			int index = 1;
			g.setColor(Color.red);
			for (Point2D p : o.getVertices()) {
				g.drawString(index + "", (float) p.getX(), (float) p.getY());
				index++;
			}

		}

		g.setColor(Color.black);
		g.drawString("Left click on white space to add vertex...", 30, 100);
		g.drawString("Right click to make the shape from clicked vertices...",
				30, 200);
		g.drawString("Middle click to clear the obstructables list...", 30, 300);
		g.drawString("Do not cross rays or it will look bad :P", 30, 400);

		ArrayList<Ray> rays = new ArrayList<Ray>();
		// upper left ray
		Ray r1 = new Ray(new Point2D.Float(l.location.x, l.location.y));
		// upper right ray
		Ray r2 = new Ray(new Point2D.Float(l.location.x, l.location.y));
		// lower left ray
		Ray r3 = new Ray(new Point2D.Float(l.location.x, l.location.y));
		// lower right ray
		Ray r4 = new Ray(new Point2D.Float(l.location.x, l.location.y));

		r1.setTip(new Point2D.Float(0, 0));
		r2.setTip(new Point2D.Float(600, 0));
		r3.setTip(new Point2D.Float(0, 600));
		r4.setTip(new Point2D.Float(600, 600));
		rays.add(r1);
		rays.add(r2);
		rays.add(r3);
		rays.add(r4);

		for (Obstructable o : obs) {
			for (Point2D vertex : o.getVertices()) {
				Ray ray = new Ray(new Point2D.Float(l.location.x, l.location.y));
				ray.setTip(new Point2D.Float(
						(float) ((vertex.getX() - l.location.x) * 100 + vertex
								.getX()),
						(float) ((vertex.getY() - l.location.y) * 100 + vertex
								.getY())));
				rays.add(ray);

			}
			for (Ray r : rays) {
				g.setColor(Color.black);
				g.drawLine((float) r.origin.getX(), (float) r.origin.getY(),
						(float) r.tip.getX(), (float) r.tip.getY());
			}

		}

		ArrayList<Point2D> intersections = new ArrayList<Point2D>();
		for (Obstructable o : obs) {
			ArrayList<Point2D> obsInters = new ArrayList<Point2D>();
			for (Line2D l : o.lines)
				for (Ray r : rays) {
					Point2D intersection = VectorUtil.findIntersection(
							new Line2D.Float(r.origin, r.tip), l);
					obsInters.add(intersection);
				}
			for (Point2D p : obsInters) {
				intersections.add(p);
			}

		}
		g.setColor(Color.blue);
		for (Point2D p : intersections) {
			g.fillOval((float) p.getX() - 2f, (float) p.getY() - 2f, 4, 4);
		}

		g.setColor(new Color(1f, 1f, 0f, .8f));
		g.fillOval(l.location.x - 15, l.location.y - 15, 30, 30);
	}

	// g.setColor(new Color(1f,1f,0f,.5f));
	// g.fillOval(l.location.x-15, l.location.y-15, 30, 30);

	// for (Ray ray : rays) {
	// Line2D l2d = ray.line;
	// g.setColor(Color.white);
	// g.drawLine((float) l2d.getX1(), (float) l2d.getY1(),
	// (float) l2d.getX2(), (float) l2d.getY2());
	// }

	// Returns 1 if the lines intersect, otherwise 0. In addition, if the lines
	// intersect the intersection point may be stored in the floats i_x and i_y.

	@Override
	public void init(GameContainer arg0) throws SlickException {
		// TODO Auto-generated method stub
		l = new Light();
		lights.add(l);

	}

	@Override
	public void update(GameContainer gc, int arg1) throws SlickException {
		Input in = gc.getInput();
		l.setLocation(new Point(in.getAbsoluteMouseX(), in.getAbsoluteMouseY()));
	}

	ArrayList<Point2D> pressedPoints = new ArrayList<Point2D>();

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button == 0) {
			pressedPoints.add(new Point2D.Float(x, y));
		} else if (button == 1 && pressedPoints.size() > 2) {
			obs.add(new Obstructable(convertListToArray(pressedPoints)));
			pressedPoints.clear();
		} else if (button == 2) {
			obs.clear();
		}

	}

	public Point2D[] convertListToArray(ArrayList<Point2D> pts) {
		Point2D[] array = new Point2D[pts.size()];
		for (int i = 0; i < pts.size(); i++) {
			array[i] = pts.get(i);
		}
		return array;

	}

	@Override
	public void mouseReleased(int button, int x, int y) {

	}

}
