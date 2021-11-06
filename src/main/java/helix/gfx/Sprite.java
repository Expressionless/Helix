package helix.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import helix.Constants;
import helix.utils.math.Vector2D;

/**
 * Basic Implementation of a Sprite class
 * Contains:
 * - a main {@link Animation}
 * - name
 * - {@link Rectangle} bounds of the sprite being animated
 * @author Sly
 *
 */
public class Sprite {

	/**
	 * Animation to render
	 */
	private final Animation animation;
	
	/**
	 * Name of the sprite
	 */
	private String name;

	/**
	 * Current scale to render the sprite at. Does not affect the
	 * {@link Sprite#animation} scale
	 */
	private Vector2D scale;

	/**
	 * whether or not the sprite is flipped. Does not affect the {@link Sprite#animation}
	 */
	private boolean flipped = false;

	/**
	 * Bounds relative to top left of sprite (default)
	 */
	private final Rectangle bounds;
	
	/**
	 * Create a new Sprite based off an {@link Animation}
	 * @param animation
	 */
	public Sprite(Animation animation) {
		this.animation = animation;
		if(this.animation.getName().isBlank())
			this.name = Constants.DEFAULT_SPRITE;
		else
			this.name = animation.getName();

		scale = new Vector2D(1, 1);
		bounds = new Rectangle(0, 0, this.animation.getWidth(), this.animation.getHeight());
	}
	
	/**
	 * Create a new Sprite from a {@link TextureRegion}
	 * @param region - region to load from
	 * @param frameCount - number of frames in region
	 * @param duration - animation time (ms)
	 */
	public Sprite(TextureRegion region, int frameCount, int duration) {
		this(new Animation(region, "", frameCount, duration));
	}

	/**
	 * Create a new Sprite from a {@link TextureRegion} with a single frame
	 * @param region - region to load from
	 */
	public Sprite(TextureRegion region) {
		this(region, Constants.SINGLE_FRAME, Constants.NO_ANIM);
	}
	
	/**
	 * Draw the sprite at a location with {@link Color#WHITE}
	 * @param batch - {@link SpriteBatch} to draw in
	 * @param x - x position of sprite to draw at (top left)
	 * @param y - y position of sprite to draw at (top left)
	 */
	public void draw(SpriteBatch batch, float x, float y) {
		this.draw(batch, x, y, Color.WHITE);
	}

	/**
	 * Draw the sprite at a location with a color
	 * @param batch - {@link SpriteBatch} to draw in
	 * @param x - x position of sprite to draw at (top left)
	 * @param y - y position of sprite to draw at (top left)
	 * @param color - {@link Color} to tint the sprite
	 */
	public void draw(SpriteBatch batch, float x, float y, Color color) {
		this.animation.update();
		this.setBounds(x, y, this.animation.getWidth() * scale.getX(), this.animation.getHeight() * scale.getY());
		
		Color last = batch.getColor();
		batch.setColor(color);
		if (flipped) {
			batch.draw(animation.getFrame(), x + animation.getWidth(), y, -animation.getWidth() * scale.getX(),
					animation.getHeight() * scale.getY());
		} else
			batch.draw(animation.getFrame(), x, y, animation.getWidth() * scale.getX(), animation.getHeight() * scale.getY());
		batch.setColor(last);
	}

	/**
	 * Restart the animation
	 */
	public void restart() {
		this.animation.restart();
	}

	/**
	 * Start the animation
	 */
	public void start() {
		this.animation.start();
	}

	/**
	 * Stop the animation
	 */
	public void stop() {
		this.animation.stop();
	}
	
	/**
	 * Restart and then stop the animation
	 * @return
	 */
	public void reset() {
		this.animation.reset();
	}

	// Getters and Setters

	public Rectangle getBounds() {
		return bounds;
	}

	private void setBounds(float x, float y, float width, float height) {
		bounds.x = x;
		bounds.y = y;
		bounds.width = width;
		bounds.height = height;
	}
	
	public TextureRegion getSubImage(int index) {
		return this.animation.getSubImage(index);
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Animation getAnimation() {
		return animation;
	}

	public void flip(boolean flipped) {
		this.flipped = flipped;
	}

	public boolean isFlipped() {
		return flipped;
	}

	public boolean isPlaying() {
		return this.animation.isRunning();
	}

	public float getWidth() {
		return bounds.width;
	}

	public float getHeight() {
		return bounds.height;
	}

	public Vector2D getScale() {
		return scale;
	}

	public void setScale(Vector2D scale) {
		this.scale = scale.copy();
	}
	
	public void setScale(float x, float y) {
		this.setScale(new Vector2D(x, y));
	}
	
	public Sprite copy() {
		Sprite cpy = new Sprite(animation);
		cpy.setName(name);
		return cpy;
	}
	
	@Override
	public String toString() {
		return "Sprite [name=" + this.name 
				+ (scale != null ? ",scale=" + scale.toString() : "")
				+ ",flipped=" + this.flipped
				+ ",dim=[w=" + this.getWidth() + ",h=" + this.getHeight() + "]"
				+ ",anim=" + this.getAnimation().toString()
				+ "]";
	}
}
