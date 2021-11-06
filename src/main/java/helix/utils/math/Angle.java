package helix.utils.math;

/**
 * Some easy angles (In degrees)
 * @author Sly
 *
 */
public enum Angle {
	TOP_LEFT(135),
	TOP(90),
	TOP_RIGHT(45),
	LEFT(180),
	RIGHT(0),
	BOTTOM_RIGHT(305),
	BOTTOM(270),
	BOTTOM_LEFT(225);
	
	/**
	 * Angle value (int)
	 */
	public int angle;
	
	private Angle(int angle) {
		this.angle = angle;
	}
}
