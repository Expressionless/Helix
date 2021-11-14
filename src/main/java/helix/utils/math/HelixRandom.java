package helix.utils.math;

/**
 * Random class that extends on {@link java.util.Random}
 * @author Sly
 *
 */
public class HelixRandom extends java.util.Random {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2395750591777588150L;

	public HelixRandom() {
		super();
	}
	
	public HelixRandom(Long seed) {
		super(seed);
	}
	
	/**
	 * 
	 * @param min
	 * @param max
	 * @return A random int between the min and max values
	 */
	
	public int getRandInRange(int min, int max) {
		int variation = max - min;
		if(variation > 0)
			return min + nextInt(variation);
		else return min;
	}
	
	public float getRandInRange(float min, float max) {
		float variation = max - min;
		if(variation > 0) {
			return min + nextFloat() * variation;
		}
		else return min;
	}
	
}
