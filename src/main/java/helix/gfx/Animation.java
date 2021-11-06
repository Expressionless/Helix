package helix.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Basic animation to handle and store animation data
 * takes in a {@link TextureRegion} and spits out an animation
 * 
 * 
 * @author bmeachem
 */
public class Animation {
	private static final float MILLISECONDS = 1000;

	private final TextureRegion baseTex;
	private final Array<TextureRegion> frames;
	
	private String name;
	
	private float maxFrameTime, currentFrameTime;
	private int frame, frameCount;
	
	private float animTime;
	
	private boolean looping = true;
	private boolean playing = true;
	
	/**
	 * Create an animation from a {@link TextureRegion}
	 * @param region - {@link TextureRegion} to load from
	 * @param frameCount - Number of Frames
	 * @param animTime - animation time (ms)
	 */
	public Animation(TextureRegion region, String name, int frameCount, float animTime) {
		this.baseTex = region;
		this.frames = new Array<TextureRegion>();
		int frameWidth = region.getRegionWidth() / frameCount;
		for(int i = 0; i < frameCount; i++) {
			frames.add(new TextureRegion(region, i * frameWidth, 0, frameWidth, region.getRegionHeight()));
		}
		
		this.frameCount = frameCount;
		this.maxFrameTime = (animTime / MILLISECONDS) / frameCount;
		
		this.animTime = animTime;
		this.frame = 0;
		this.name = name;
	}
	
	/**
	 * Update the animation as necessary\
	 */
	public void update() {
		if(!playing)
			return;
		
		currentFrameTime += Gdx.graphics.getDeltaTime();
		
		if(currentFrameTime > maxFrameTime) {
			frame++;
			currentFrameTime = 0;
		}
		
		if(frame >= frameCount) {
			if(!looping)
				playing = false;
			frame = 0;
		}
	}
	
	/**
	 * Start this Animation
	 */
	public void start() {
		this.playing = true;
	}
	
	/**
	 * Play the Animation from the start
	 */
	public void restart() {
		frame = 0;
		playing = true;
	}
	
	/**
	 * Stop the Animation
	 */
	public void stop() {
		playing = false;
	}
	
	/**
	 * Reset the Animation
	 */
	public void reset() {
		playing = false;
		frame = 0;
	}
	
	// Getters and Setters

	/**
	 * Set time in millis for how long the animation lasts
	 * @param duration - time (ms)
	 */
	public void setAnimTime(float duration) {
		this.maxFrameTime = (duration / MILLISECONDS) / frameCount;
	}
	
	public TextureRegion getFrame() {
		return frames.get(frame);
	}
	
	public TextureRegion getSubImage(int index) {
		return frames.get(index);
	}
	
	public float getWidth() {
		return frames.get(0).getRegionWidth();
	}
	
	public float getHeight() {
		return frames.get(0).getRegionHeight();
	}

	public String getName() {
		return name;
	}

	public void setLooping(boolean looping) {
		this.looping = looping;
	}

	public TextureRegion getBaseTex() {
		return baseTex;
	}
	
	public Animation copy() {
		return new Animation(new TextureRegion(baseTex.getTexture()), this.name, this.frameCount, this.animTime);
	}
	
	public boolean isRunning() {
		return this.playing;
	}
	
	@Override
	public String toString() {
		return "Animation [name=" + name
				+ ",w=" + this.getWidth()
				+ ",h=" + this.getHeight()
				+ "]";
	}
}
