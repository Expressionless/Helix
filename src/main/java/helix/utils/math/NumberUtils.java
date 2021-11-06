package helix.utils.math;

/**
 * Some helpful utilities to use when working with numbers
 * 
 * @author Sly
 *
 */
public class NumberUtils {

	/**
	 * Clamp a number between a minimum and maximum value
	 * 
	 * @param val - value to clamp
	 * @param min - minimum value
	 * @param max - maximum value
	 * @return - clamped value
	 */
	public static Number clamp(final Number val, final Number min, final Number max) {
		if (val.doubleValue() < min.doubleValue())
			return min;
		else if (val.doubleValue() > max.doubleValue())
			return max;
		else
			return val;
	}

	/**
	 * Loop a number between a minimum and maximum value
	 * 
	 * @param val - value to loop
	 * @param min - minimum value
	 * @param max - maximum value
	 * @return - looped number
	 */
	public static Number loop(final Number val, final Number min, final Number max) {
		if (val.doubleValue() < min.doubleValue())
			return max;
		else if (val.doubleValue() > max.doubleValue())
			return min;
		else
			return val;
	}

	/**
	 * <pre>
	 * Example Call: 
	 * zoom = Utils.lerp(zoom, targetZoom, 0.075f);
	 * </pre>
	 * Linearly interpolate a value
	 * @param val - Value to interpolate
	 * @param target - Target Value to reach
	 * @param change - Percent change by tick (< 1)
	 * @return - Linearly interpolated value
	 */
	public static float lerp(float val, float target, float change) {

		float result = ((val * (1.0f - change)) + (target * change));
		if (Math.abs(val - target) < change)
			return target;
		else
			return result;

	}
}
