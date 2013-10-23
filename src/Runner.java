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
	Point2D[] windowPoints = {new Point2D.Float(0,0),new Point2D.Float(0,windowHeight),new Point2D.Float(windowWidth,windowHeight),new Point2D.Float(windowWidth,0)};
	Obstructable windowObs = new Obstructable(windowPoints);
	ArrayList<Light> lights = new ArrayList<Light>();

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Runner("Lights"));
		app.setDisplayMode(windowWidth, windowHeight, false);
		app.setSmoothDeltas(true);
		app.setTargetFrameRate(80);
		app.setShowFPS(true);
		app.start();
	}

	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {
		g.setColor(Color.black);
		g.fillRect(0, 0, windowWidth, windowHeight);
		for (Light l : lights) {
			for (Obstructable o : obs) {
				Polygon poly = new Polygon();
				for (Point2D p : o.getVertices()) {
					Ray r = new Ray(l.getLoc(), p);
					l.addRay(r);
					poly.addPoint(cXout((float) p.getX()), cYout((float) p.getY()));

					Point2D intersection = o.rayIntersection(r);
					if (intersection != null) {
						if (r.intersection == null) {
							r.intersection = intersection;
						}
						else if (r.origin.distanceSq(intersection) < r.origin.distanceSq(r.intersection)) {
							r.intersection = intersection;
						}
					}

				}
				g.setColor(Color.green);
				g.fill(poly);
			}
			if (onScreen(l.location)) {
				for (Ray r : l.rays) {
					if (r.intersection==null) {
						r.intersection = windowObs.rayIntersection(r);
					}
				}
			}
			l.sortRays();
			for (int i = 0; i < l.rays.size(); i++) {
				Ray r = l.ray(i);
				
				g.setColor(Color.blue);
				g.drawLine(cXout((float)r.origin.getX()), cYout((float)r.origin.getY()), cXout((float)r.intersection.getX()), cYout((float)r.intersection.getY()));
				
				//Polygon poly = new Polygon();
				//poly.addPoint(cXout((float)r.origin.getX()),cYout((float)r.origin.getY()));
				//poly.addPoint(cXout((float)r.intersection.getX()),cYout((float)r.intersection.getY()));
				Ray r2;
				if (i + 1 >= l.rays.size()) {
					r2 = l.ray(0);
				}
				else {
					r2 = l.ray(i+1);
				}
				//poly.addPoint(cXout((float)r2.intersection.getX()),cYout((float)r2.intersection.getY()));
				//g.setColor(Color.white);
				//g.fill(poly);
			}
			l.clearRays();
		}

	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		lights.add(new Light(new Point2D.Float(windowWidth/2,windowHeight/2)));
		
	}

	@Override
	public void update(GameContainer gc, int arg1) throws SlickException {
		Input in = gc.getInput();
		float x = cXin(in.getMouseX());
		float y = cYin(in.getMouseY());
		lights.get(0).setLocation(new Point2D.Float(x,y));
	}

	
	ArrayList<Point2D> pressedPoints = new ArrayList<Point2D>();
	@Override
	public void mousePressed(int button, int x, int y) {
		if (button == 0) {
			System.out.println("1");
			pressedPoints.add(new Point2D.Float(cXin(x), cYin(y)));
		}
		if (button == 1 && pressedPoints.size() >= 3) {
			System.out.println("2");
			Point2D[] pts = new Point2D[pressedPoints.size()];
			Obstructable o = new Obstructable(pressedPoints.toArray(pts));
			obs.add(o);
			pressedPoints.clear();
			
		}

	}

	@Override
	public void mouseReleased(int button, int x, int y) {

	}
	
	public float cYin(float y)
	{
		return windowHeight-y;
	}
	
	public float cYout(float y)
	{
		return windowHeight-y;
	}
	public float cXin(float x)
	{
		return windowWidth-x;
	}
	public float cXout(float x)
	{
		return windowWidth-x;
	}
	public boolean onScreen(Point2D p) {
		float x = cXout((float)p.getX());
		float y = cYout((float)p.getY());
		if ((x>0)&&(x<windowWidth)) {
			if ((y>0)&&(y<windowHeight)) {
				return true;
			}
		}
		return false;
	}
	
}
