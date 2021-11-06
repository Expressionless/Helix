package helix.game.objects.entity;

import helix.game.objects.Entity;
import helix.utils.math.Vector2D;

/**
 * Basic Collider class that expands on
 * {@link helix.utils.match.Rectangle}
 * @author Sly
 *
 */
public final class Collider extends helix.utils.math.Rectangle {

	private static final long serialVersionUID = -829325656055031009L;
	
	/**
	 * Current Collider offset
	 */
	private Vector2D offset;
	
	/**
	 * Create a new Collider in the top left of the entity, matching the entity's
	 * dimensions
	 * @param entity
	 */
	public Collider(Entity entity) {
		this(entity, ColliderOffset.TOP_LEFT);
	}
	
	/**
	 * Create a new Collider with the specified offset for an entity
	 * @param entity - Entity to attach Collider to
	 * @param offsetType - ColliderOffset to use
	 * 
	 * @see {@link Entity}, {@link ColliderOffset}
	 */
	public Collider(Entity entity, ColliderOffset offsetType) {
		super(entity.getPos().getX(), entity.getPos().getY(), entity.getWidth(), entity.getHeight());
		offset = new Vector2D(0, 0);
		
		switch(offsetType) {
			case BOTTOM:
				offset.setX(this.getWidth() / 2);
				offset.setY(this.getHeight());
				break;
			case BOTTOM_LEFT:
				offset.setX(0);
				offset.setY(this.getHeight());
				break;
			case BOTTOM_RIGHT:
				offset.setX(this.getWidth());
				offset.setY(this.getHeight());
				break;
			case CENTER:
				offset.setX(this.getWidth() / 2);
				offset.setY(this.getHeight() / 2);
				break;
			case CENTER_LEFT:
				offset.setX(0);
				offset.setY(this.getHeight() / 2);
				break;
			case CENTER_RIGHT:
				offset.setX(this.getWidth());
				offset.setY(this.getHeight() / 2);
				break;
			case TOP:
				offset.setX(this.getWidth() / 2);
				offset.setY(0);
				break;
			case TOP_LEFT:
				offset.setX(0);
				offset.setY(0);
				break;
			case TOP_RIGHT:
				offset.setX(this.getWidth());
				offset.setY(0);
				break;
			default:
				offset.setX(0);
				offset.setY(0);
				break;
		}
	}
	
	// Getters and Setters
	
	public Vector2D getOffset() {
		return this.offset;
	}
	
	public float getXOffset() {
		return offset.getX();
	}
	
	public float getYOffset() {
		return offset.getY();
	}
	
	public void setOffset(float x, float y) {
		this.offset.setX(x);
		this.offset.setY(y);
	}
	
	public void setXOffset(float x) {
		this.offset.setX(x);
	}
	
	public void setYOffset(float y) {
		this.offset.setY(y);
	}
	
	public String toString() {
		return "Collider ["
				+ "offset=" + offset.toString()
				+ ",bounds=[w=" + this.getWidth() + ",h=" + this.getHeight()
				+ "]";
	}
}
