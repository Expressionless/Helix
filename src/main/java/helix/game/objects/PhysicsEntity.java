package helix.game.objects;

import helix.Constants;
import helix.game.Data;
import helix.utils.math.Point;
import helix.utils.math.Vector2D;

public abstract class PhysicsEntity extends Entity {
	
	private Vector2D acceleration = new Vector2D(0, 0);
	private Vector2D speed = new Vector2D(0, 0);
	
	public PhysicsEntity(Data data, Point pos, float depth) {
		super(data, pos);
	}
	
	/**
	 * Create a basic Physics-enabled Entity
	 * 
	 * @param data - {@link helix.game.Data}
	 * @param pos
	 */
	public PhysicsEntity(Data data, Point pos) {
		this(data, pos, Constants.DEPTH_DEFAULT);
	}
	
	public PhysicsEntity(Data data, float x, float y, float depth) {
		this(data, new Point(x, y));
	}
	
	public PhysicsEntity(Data data, float x, float y) {
		this(data, new Point(x, y));
	}
	
	@Override
	protected void preStep(float delta) {
		this.speed = this.speed.add(acceleration);
		this.updateDepth();
	}
	
	// Getters and Setters
	public Vector2D getAcceleration() {
		return acceleration;
	}
	
	public Vector2D getSpeed() {
		return speed;
	}
}
