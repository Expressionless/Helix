package helix.particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import helix.game.Data;
import helix.game.particle.emitter.Flame;
import helix.utils.math.HelixRandom;
import helix.utils.math.Point;

public class Emitter {

	// No. of particles / second
	// private int density;

	// type of particle and info about particle
	private ParticleType particleType;
	private int duration;
	private float minSize;
	private float maxSize;

	// px per frame
	private float acceleration;
	private float direction;

	// Seconds per particle
	private float interval;
	private float timer = -1;

	private Point pos;
	private int depth;
	
	private HelixRandom numGenerator;
	private SpriteBatch spriteBatch;
	
	private Data gameData;

	protected Point spread;
	
	private final List<Particle> particles = new ArrayList<Particle>();

	public Emitter(Data gameData, Float x, Float y, ParticleType particle, Float duration, Integer density, Float max_speed,
			Float direction, Float min_size, Float max_size) {
		init(gameData, x, y, particle, duration, density, max_speed, direction, min_size, max_size);
	}

	public Emitter(Data gameData, Float x, Float y, ParticleType particle) {
		init(gameData, x, y, particle);
	}

	public Emitter(Data gameData, float x, float y, ParticleType particle, float duration, int density) {
		init(gameData, x, y, particle, duration, density);
	}

	public void init(Data gameData, float x, float y, ParticleType particle, float duration, int density) {
		init(gameData, x, y, particle, duration, density, Constants.DEFAULT_ACCELERATION, Constants.DIR_UP, Constants.DEFAULT_MIN_SIZE,
				Constants.DEFAULT_MAX_SIZE);
	}

	public void init(Data gameData, float x, float y, ParticleType particle) {
		init(gameData, x, y, particle, GameConstants.FPS * 3f, 1);
	}

	// Initialize the object
	public void init(Data gameData, float x, float y, ParticleType particle, float duration, int density, float acceleration,
			float direction, float min_size, float max_size) {
		this.gameData = gameData;
		this.particleType = particle;
		this.duration = (int) (duration * GameConstants.FPS);
		this.acceleration = acceleration;
		this.direction = direction;
		this.minSize = min_size;
		this.maxSize = max_size;
		this.numGenerator = new HelixRandom();

		spread = new Point(0, 0);		
		
		interval = GameConstants.FPS / density;
		
		this.pos = new Point(x, y);
	}

	public void tick() {
		// Check if should spawn a particle
		if (timer == -1) {
			particles.add(createParticle());
			timer = interval;
		} else if (timer > -1)
			timer -= 1;

		//Collections.sort(particles, (Comparator<Particle>) (Particle p1, Particle p2) -> (int) (p1.getDepth() - p2.getDepth()));
		
		for (Particle particle : particles)
			if (particle != null)
				particle.step();

	}

	public void render(float climate, float lighting) {
		preDraw();
		for (Particle particle : particles) {
			if (particle != null)
				particle.render(climate, lighting);
		}
	}

	public void preDraw() {
		particles.removeIf(new Predicate<Particle>() {
			@Override
			public boolean test(Particle p) {
				return p == null;
			}
		});
	}
	
	public Particle createParticle() {
		switch (particleType) {
			case FLAME:
				float p_size = numGenerator.getRandInRange(minSize, maxSize);
				Random r = new Random();
				float spread_x = r.nextFloat() * spread.getX() - spread.getX() / 2;
				float spread_y = r.nextFloat() * spread.getY() - spread.getY() / 2;
				float x = pos.getX() + spread_x;
				float y = pos.getY() + spread_y;
				Particle p = new Flame(this, x, y, direction, acceleration, duration, p_size);
				return p;
			default:
				return null;
		}
	}

	// Getters and Setters
	public Point getPos() {
		return pos;
	}

	public int getDepth() {
		return depth;
	}

	public List<Particle> getParticles() {
		return particles;
	}

	public ParticleType getParticleType() {
		return particleType;
	}

	public void setParticle_type(ParticleType particle_type) {
		this.particleType = particle_type;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public float getMin_size() {
		return minSize;
	}

	public void setMin_size(float min_size) {
		this.minSize = min_size;
	}

	public float getMax_size() {
		return maxSize;
	}

	public void setMax_size(float max_size) {
		this.maxSize = max_size;
	}

	public float getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}

	public float getDirection() {
		return direction;
	}

	public void setDirection(float direction) {
		this.direction = direction;
	}

	public float getInterval() {
		return interval;
	}

	public void setInterval(float interval) {
		this.interval = interval;
	}

	public void setPos(Point pos) {
		this.pos = pos;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public Point getSpread() {
		return spread;
	}
	
	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}
}