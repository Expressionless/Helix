package helix.utils.math;

/**
 * Basic implementation of a Point class
 * Contains some methods for working with points and basic coordinate storage
 * @author bmeachem
 *
 */
public class Point {

	/**
	 * Coordinates
	 */
	private float x, y;
	
	/**
	 * Create a new Point @ (x, y)
	 * @param x
	 * @param y
	 */
	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Create a new Point @ (x, y)
	 * @param x
	 * @param y
	 */
	public Point(double x, double y) {
		this((float)x, (float)y);
	}
	
	/**
	 * Subtract another point from this one
	 * @param other - other point to sub
	 * @return - a new resultant point of the operation
	 */
	public Point sub(Point other) {		
		return new Point(this.x - other.x, this.y - other.y);
	}
	
	/**
	 * Subtract a Vector2 from this Point
	 * @param other
	 * @return - a new resultant point of the operation
	 */
	public Point sub(Vector2 other) {
		return this.sub(other.toPoint()).copy();
	}
	
	/**
	 * Add a Point to this one
	 * @param other - other point to add
	 * @return - a new resultant point of the operation
	 */
	public Point add(Point other) {
		return new Point(this.x + other.x, this.y + other.y);
	}

	/**
	 * Add a Vector2 to this one
	 * @param other - other Vector2 to add
	 * @return - a new resultant point of the operation
	 */
	public Point add(Vector2 other) {
		return this.add(new Point(other.getX(), other.getY()));
	}
	
	/**
	 * Multiply this Point by a scalar
	 * @param scalar - scalar to multiply x and y by
	 * @return - a new resultant point of the operation
	 */
	public Point multiply(float scalar) {
		this.x *= scalar;
		this.y *= scalar;

		return new Point(this.x * scalar, this.y * scalar);
	}
	
	/**
	 * Return this Point as a Vector2
	 * @return
	 */
	public Vector2 toVector2() {
		return new Vector2(x, y);
	}
	
	/**
	 * Get distance from this point to another
	 * @param other - Other point to get Distance to
	 * @return - (float) distance
	 */
	public float getDistTo(Point other) {
		double disX = Math.pow(other.x - x, 2);
		double disY = Math.pow(other.y - y, 2);
		return (float)Math.pow(disX + disY, 0.5);
	}
	

	/**
	 * Get distance from this point to a set of coordinates
	 * @param x
	 * @param y
	 * @return - (float) distance
	 */
	public float getDistTo(float x, float y) {
		return getDistTo(new Point(x, y));
	}
	// Getters and Setters
	
	public String toString() {
		return "[Point x="+x+"y="+y+"]";
	}
	
	public Point copy() {
		return new Point(x, y);
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}

	public float length() {
		double disX = Math.pow(this.x, 2);
		double disY = Math.pow(this.y, 2);
		
		return (float)Math.pow(disX + disY, 0.5);
	}
}
