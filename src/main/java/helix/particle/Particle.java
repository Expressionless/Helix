package helix.particle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import helix.game.objects.PhysicsEntity;
import helix.gfx.SpriteSheet;
import helix.utils.math.Vector2D;

public abstract class Particle extends PhysicsEntity {

	protected Emitter emitter;

	protected float size;

	// Timer stuffs
	protected int timer;

	public Particle(Emitter em, SpriteSheet sprite, float x, float y, float direction, float acceleration, int depth,
			int life, float size) {
		super(x, y, sprite, em.getDepth());
		this.emitter = em;
		this.direction = Vector2D.getUnitVector(direction);
		this.acceleration = acceleration;
		this.timer = life;
		this.size = size;

		this.setDepth(depth);
	}

	@Override
	public void initAnimations() {
		
	}
	
	@Override
	public void step() {
		if (timer == -1) {
			int ind = emitter.getParticles().indexOf(this);
			emitter.getParticles().set(ind, null);
		} else if (timer > -1)
			timer -= 1;
		
		float newx = (float) (this.getPos().getX() + speed * Math.cos(Math.toRadians(this.getDirection().getAngle())));
		float newy = (float) (this.getPos().getY() - speed * Math.sin(Math.toRadians(this.getDirection().getAngle())));
		this.getPos().setX(newx);
		this.getPos().setY(newy);
		//System.out.println(pos.getY());
		updateParticle();
	}

	@Override
	public void draw(SpriteBatch sb) {
	}

	public void render(float climate, float lighting) {
		drawParticle(emitter.getSpriteBatch());
	}

	public abstract void updateParticle();

	public abstract void drawParticle(SpriteBatch sb);
	
	// Getters and Setters
	public Emitter getEmitter() {
		return emitter;
	}

}
