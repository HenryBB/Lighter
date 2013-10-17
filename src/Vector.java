
public class Vector {
	double x;
	double y;
	double length;
	
	public Vector(double x, double y)
	{
		this.x=x;
		this.y=y;
		double h = (x*x) + (y*y);
		length = Math.sqrt(h);
	}
	
	public Vector makePerp()
	{
		return new Vector(y,-x);
	}
	
	public boolean equals(Vector v)
	{
		if (v.x == x && v.y == y && v.length == length)
			return true;
		else
			return false;
	}
	
	public Vector add(Vector v)
	{
		double tempX = this.x + v.x;
		double tempY = this.y + v.y;
		Vector vector = new Vector(tempX, tempY);
		
		return vector;
	}
	
	public Vector subtract(Vector v)
	{
		
		double tempX = this.x - v.x;
		double tempY = this.y - v.y;
		Vector vector = new Vector(tempX, tempY);
		return vector;
	}
	
	public double length()
	{		
		return length;
	}
	
	public Vector scale(double s)
	{
		Vector v = new Vector(x*s,y*s);
		return v;
	}
	
	public static Vector zero()
	{
		return new Vector(0,0);
	}
	
	//NOTE: WRONG
	public Vector unitize()
	{
		Vector v = new Vector(x/length,y/length);
		return v;
	}
	
	public double distance(Vector v)
	{
		double x = Math.abs(v.x-this.x);
		double y = Math.abs(v.y-this.y);
		double xs = x*x;
		double ys = y*y;
		double d = Math.sqrt(xs+ys);
		return d;
	}
	
	public static double distance(Vector a, Vector b)
	{
		double x = Math.abs(a.x-b.x);
		double y = Math.abs(a.y-b.y);
		double xs = x*x;
		double ys = y*y;
		double d = Math.sqrt(xs+ys);
		return d;
		
	}
	
	public double dotProduct(Vector other)
	{
		return (x * other.x) + (y * other.y);
	}
	
//	public static Vector findSlope(Vector vector, Vector other, boolean perp)
//	{
//		Vector v = new Vector(0,0);
//		if (perp == false){
//		v.x = vector.x-other.x;
//		v.y = vector.y-other.y;
//		return v;
//		}
//		else
//		{
//		}
//		return null;
//			
//	}
}

//Vector
//to string
//copy
//copyFrom(v)
//set(x,y)
//zero
//length
//add(v)
//addScaled(v,s)
//subtract(v)
//scale(s)
//divide(s)
//unitize()
//dot(v)
//makePerpindicular()