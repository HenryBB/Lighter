import java.awt.Point;
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
		//Drawing stuff on the screen
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		//This happens at the very beginning
	}

	@Override
	public void update(GameContainer gc, int arg1) throws SlickException {
		//this happens every frame
	}


	ArrayList<Point2D> pressedPoints = new ArrayList<Point2D>();
	@Override
	public void mousePressed(int button, int x, int y) {
		//happens when mouse button is pressed
		if (button == 1)
		{
			pressedPoints.add(new Point2D.Float(x, cY(y)));
		}
		if (button == 2 && pressedPoints.size()>=3)
		{
			Point2D[] pts = new Point2D[pressedPoints.size()];
			Obstructable o = new Obstructable(pressedPoints.toArray(pts));
			obs.add(o);
			pressedPoints.clear();
		}
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		//happens when mouse button is released
	}
	
	public float cY(float y)
	{
		return windowHeight-y;
	}

}
