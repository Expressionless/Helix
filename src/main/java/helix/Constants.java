package helix;

import java.io.File;

import helix.utils.math.NumberUtils;

/**
 * Engine constants
 * @author Sly
 *
 */
public class Constants {
	
	/**
	 * Absolute Path of the root directory
	 */
	public static final String ABS_PATH = new File("").getAbsolutePath();
	
	// Animation Constants
	public static final int SINGLE_FRAME = 1;
	public static final float DEFAULT_SPEED = 15f;
	public static final int NO_ANIM = -1;
	public static final String DEFAULT_SPRITE = "DEFAULT";

	/**
	 *  Minimum Lerp "Change" Distance
	 *  
	 *  @see {@link NumberUtils#lerp}
	 */
	public static final float MIN_LERP_DIST = 0.001f;

	public static final String TEXTURE_FIELD_NAME = "TEXTURE_REF";
	
}
