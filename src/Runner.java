import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;

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
	final static int windowWidth = 600;
	final static int windowHeight = 600;

	// VARIABLES
	ArrayList<Obstructable> obs = new ArrayList<Obstructable>();
	Point2D[] windowPoints = { new Point2D.Float(0, 0),
			new Point2D.Float(0, windowHeight),
			new Point2D.Float(windowWidth, windowHeight),
			new Point2D.Float(windowWidth, 0) };
	Obstructable windowObs = new Obstructable(windowPoints);
	ArrayList<Light> lights = new ArrayList<Light>();
	boolean showObsInGreen = false;

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Runner("Lights"));
		app.setDisplayMode(windowWidth, windowHeight, false);
		app.setSmoothDeltas(true);
		app.setTargetFrameRate(80);
		app.setShowFPS(true);
		app.setVSync(true);
		app.start();
	}

	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {

		g.setColor(Color.black);
		g.fillRect(0, 0, windowWidth, windowHeight);
		for (Light l : lights) {
			for (Point2D p : windowObs.getVertices()) {
				Ray windowRay = new Ray(l.getLoc(), p);
				windowRay.intersection = p;
				windowRay.obsTip = windowObs;
				l.addRay(windowRay);
			}
			for (Obstructable o : obs) {
				Polygon ObsPoly = new Polygon();
				for (Point2D p : o.getVertices()) {
					Ray r = new Ray(l.getLoc(), p);
					r.obsTip = o;
					l.addRay(r);
					ObsPoly.addPoint(cXout((float) p.getX()),
							cYout((float) p.getY()));
				}
				if (showObsInGreen)
					g.setColor(Color.green);
				else
					g.setColor(Color.black);
				g.fill(ObsPoly);
			}
			for (Ray r : l.rays) {
				for (Obstructable o : obs) {
					Point2D intersection = o.rayIntersection1(r);
					if (intersection != null) {
						if (r.intersection == null) {
							r.intersection = intersection;
							r.obsInter = o;
						} else if (r.origin.distanceSq(intersection) < r.origin
								.distanceSq(r.intersection)) {
							r.intersection = intersection;
							r.obsInter = o;
						}
					}
				}
			}
			if (onScreen(l.location)) {
				for (Ray r : l.rays) {
					if (r.intersection == null) {
						r.intersection = windowObs.rayIntersection1(r);
						r.obsInter = windowObs;
					}
				}
			}
			l.sortRays();
			for (int i = 0; i < l.rays.size(); i++) {
				Ray r = l.ray(i);
				if (r.intersection == null) {
					r.intersection = r.origin;
				}
				// ///////////PROBLEM HERE - RESPONSIBLE FOR RANDOM
				// CRAHSES!!!!!!
				else if (r.origin.distanceSq(r.intersection) < r.origin
						.distanceSq(r.tip)) {
					l.rays.remove(r);
					i--;
				}
			}
			for (int i = 0; i < l.rays.size(); i++) {

				Ray r = l.ray(i);

				g.setColor(Color.white);
				g.drawLine(cXout((float) r.origin.getX()),
						cYout((float) r.origin.getY()),
						cXout((float) r.tip.getX()),
						cYout((float) r.tip.getY()));

				Polygon poly = new Polygon();
				Ray r2;
				if (i + 1 >= l.rays.size()) {
					r2 = l.ray(0);
				} else {
					r2 = l.ray(i + 1);
				}
				poly.addPoint(cXout((float) r.origin.getX()),
						cYout((float) r.origin.getY()));
				Point2D p2;
				Point2D p3;
				if (r.obsTip == r2.obsTip) {
					p2 = r.tip;
					p3 = r2.tip;
				} else if (r.obsTip == r2.obsInter) {
					p2 = r.tip;
					p3 = r2.intersection;
				} else if (r.obsInter == r2.obsTip) {
					p2 = r.intersection;
					p3 = r2.tip;
				} else {
					p2 = r.intersection;
					p3 = r2.intersection;
				}
				poly.addPoint(cXout((float) p2.getX()),
						cYout((float) p2.getY()));
				poly.addPoint(cXout((float) p3.getX()),
						cYout((float) p3.getY()));

				g.setColor(Color.white);
				g.fill(poly);

			}
			l.clearRays();
		}

		// for (Obstructable o : obs)
		// {
		// Polygon poly = new Polygon();
		// for (Point2D p : o.getVertices())
		// {
		// poly.addPoint(cXout((float)p.getX()), cYout((float)p.getY()));
		// }
		// g.setColor(Color.black);
		// g.fill(poly);
		// }

	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		lights.add(new Light(new Point2D.Float(windowWidth / 2,
				windowHeight / 2)));
		Point2D[] testPoints = { new Point2D.Float(0, 0),
				new Point2D.Float(0, windowHeight),
				new Point2D.Float(windowWidth, windowHeight),
				new Point2D.Float(windowWidth, 0) };
		Obstructable testObs = new Obstructable(testPoints);
		Ray testRay = new Ray(new Point2D.Float(50, 50), new Point2D.Float(51,
				60));
		Point2D intersection = testObs.rayIntersection1(testRay);

	}

	@Override
	public void update(GameContainer gc, int arg1) throws SlickException {
		Input in = gc.getInput();
		float x = cXin(in.getMouseX());
		float y = cYin(in.getMouseY());
		lights.get(0).setLocation(new Point2D.Float(x, y));
		if (in.isKeyPressed(Input.KEY_G))
			showObsInGreen = !showObsInGreen;
		if (in.isKeyPressed(Input.KEY_0)) {
			for (int i = 0; i < 30; i++) {

				obs.add(new Obstructable(new Point2D[] {
						new Point2D.Float(i * 20, 200),
						new Point2D.Float(i * 20 + 10, 200),
						new Point2D.Float(i * 20 + 10, 200 + 10),
						new Point2D.Float(i * 20, 200 + 10) }));
			}
		}
		if (in.isKeyPressed(Input.KEY_1)) {
			Point2D[] verts = new Point2D[100];
			int ind = 0;
			for (float theta = 0; theta < 10; theta += .1) {
				Point2D p = new Point2D.Float(cXin((float) (400+100 * Math.cos(theta))),
						cYin((float) (300+100 * Math.sin(theta))));
				verts[ind] = p;
				ind++;
			}
			int index = 0;
			for (Point2D pt : verts) {
				if (pt == null)
					System.out.println(index);
				index++;
			}
			obs.add(new Obstructable(verts));
		}

	}

	ArrayList<Point2D> pressedPoints = new ArrayList<Point2D>();

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button == 0) {
			pressedPoints.add(new Point2D.Float(cXin(x), cYin(y)));
		}
		if (button == 1 && pressedPoints.size() >= 3) {
			Point2D[] pts = new Point2D[pressedPoints.size()];
			Obstructable o = new Obstructable(pressedPoints.toArray(pts));
			obs.add(o);
			pressedPoints.clear();
		}
		if (button == 2) {
			pressedPoints.clear();
			obs.clear();
		}

	}

	@Override
	public void mouseReleased(int button, int x, int y) {

	}

	public float cYin(float y) {
		return windowHeight - y;
	}

	public float cYout(float y) {
		return windowHeight - y;
	}

	public float cXin(float x) {
		return windowWidth - x;
	}

	public float cXout(float x) {
		return windowWidth - x;
	}

	public boolean onScreen(Point2D p) {
		float x = cXout((float) p.getX());
		float y = cYout((float) p.getY());
		if ((x > 0) && (x < windowWidth)) {
			if ((y > 0) && (y < windowHeight)) {
				return true;
			}
		}
		return false;
	}

}
