package helix.game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Logger;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;

import helix.Constants;
import helix.game.objects.Entity;
import helix.gfx.Animation;
import helix.gfx.Screen;
import helix.gfx.Sprite;
import helix.utils.io.BinaryReader;
import helix.utils.io.BinaryWriter;

/**
 * Basic Data class that stores all the objects, entities and screens
 * 
 * @author bmeachem
 *
 * @see {@link GameObject}, {@link Entity}, {@link Screen}
 */
public abstract class Data {
	protected static Logger log = Logger.getLogger(Data.class.getCanonicalName());

	/**
	 * Keep track of ticks
	 */
	public static int TICKS = 0;

	/**
	 * list of all {@link GameObject}s in the application
	 */
	public final ArrayList<GameObject> objects, objectBuffer;
	/**
	 * list of all {@link Entity}s in the application
	 */
	public final ArrayList<Entity> entities, entityBuffer;
	/**
	 * list of all {@link Screen}s in the application
	 */
	public final ArrayList<Screen> screens;

	/**
	 * Read binary data with this
	 */
	protected BinaryReader reader;

	/**
	 * Write binary data with this Must call {@link BinaryWriter#close} when done
	 * writing
	 */
	protected BinaryWriter writer;

	/**
	 * An AssetManager to manage all assets
	 */
	private AssetManager manager;

	/**
	 * Main Viewport in the application
	 */
	private Viewport viewport;

	/**
	 * Main Camera in the application. Same instance as
	 * {@link helix.game.BaseGame#getCamera()}
	 */
	private Camera camera;

	/**
	 * Reference to the base instance
	 */
	private BaseGame game;

	/**
	 * Create a new Data instance.
	 * 
	 * @param game
	 */
	public Data(BaseGame game) {
		manager = new AssetManager();
		entities = new ArrayList<>();
		entityBuffer = new ArrayList<>();
		objects = new ArrayList<>();
		objectBuffer = new ArrayList<>();
		screens = new ArrayList<>();

		this.game = game;
	}

	/**
	 * To be called in {@link helix.game.BaseGame#create()}
	 */
	protected abstract void init();

	/**
	 * Create a {@link Sprite}
	 * 
	 * @param spriteName - Name of the sprite to add
	 * @param frameCount - no. of frames
	 * @param animTime   - anim time (ms)
	 * @return - a new {@link Sprite}
	 */
	public final Sprite createSprite(String spriteName, int frameCount, float animTime) {
		Texture texture = manager.get(spriteName);
		TextureRegion region = new TextureRegion(texture);
		Animation anim = new Animation(region, spriteName, frameCount, animTime);
		return new Sprite(anim);

	}

	/**
	 * Create a {@link Sprite} with a single frame and no animation time
	 * 
	 * @param spriteName - Name of the sprite to add
	 * @param frameCount - no. of frames
	 * @param animTime   - anim time (ms)
	 * @return - a new {@link Sprite}
	 */
	public final Sprite createSprite(String spriteName) {
		return this.createSprite(spriteName, Constants.SINGLE_FRAME, Constants.NO_ANIM);
	}

	/**
	 * Dispose of all things that need disposing here. This gets run at the end of
	 * {@link Data#disposeCore}
	 */
	protected abstract void dispose();

	/**
	 * Dispose of all entities and objects marked for disposal each tick
	 */
	private void disposeCore() {
		entities.removeIf((Entity entity) -> {
			return entity.willDispose();
		});

		objects.removeIf((GameObject obj) -> {
			return obj.willDispose();
		});

		this.dispose();
	}

	/**
	 * Override this as necessary. Gets called at the end of
	 * {@link Data#update(float)}
	 * 
	 * @param delta - Time since last frame (seconds)
	 */
	protected void step(float delta) {
	};

	/**
	 * Main update loop. Dispose of all entities that need disposing and then sort
	 * entities by {@link Entity#getDepth} Finally, update all objects and then run
	 * {@link Data#step}
	 * 
	 * @param delta - Time since last frame (seconds)
	 */
	public final void update(float delta) {
		TICKS++;

		this.disposeCore();

		entities.sort(new Comparator<Entity>() {

			public int compare(Entity e1, Entity e2) {
				return (int) Math.signum(e2.getDepth() - e1.getDepth());
			}

		});

		if (this.objectBuffer.size() > 0) {
			this.objects.addAll(objectBuffer);
			this.objectBuffer.clear();
		}

		for (GameObject object : objects) {
			object.updateAlarms(delta);
			object.update(delta);
		}

		this.step(delta);
	}

	/**
	 * Gets called at end of {@link Data#render} Override as necessary to draw extra
	 * things through any abstract implementation of this class
	 * 
	 * @param batch - {@link SpriteBatch} to draw with
	 */
	protected void draw(SpriteBatch batch) {
	};

	/**
	 * Render every entity that needs rendering and then call {@link Data#draw}
	 * 
	 * @param batch - {@link SpriteBatch} to draw with
	 */
	public final void render(SpriteBatch batch) {
		if (this.entityBuffer.size() > 0) {
			this.entities.addAll(this.entityBuffer);
			this.entityBuffer.clear();
		}
		
		for (Entity entity : entities) {
			entity.render(batch);
		}
		
		this.draw(batch);
	}

	/**
	 * Begin Reading with the {@link Data#reader} and read in from a specified path
	 * will return before reading if there is an instance of {@link Data#writer} or
	 * {@link Data#reader} already.
	 * 
	 * @param path - path to read in from, relative to the absolute directory
	 */
	public final void beginReading(String path) {
		if (writer != null)
			return;
		if (reader != null)
			return;

		reader = new BinaryReader(path);
	}

	/**
	 * Stop reading from the {@link Data#reader}
	 */
	public final void stopReading() {
		if (reader == null)
			return;
		reader = null;
	}

	/**
	 * Begin writing to a file at some path with the {@link Data#writer} will return
	 * early if already reading or writing.
	 * 
	 * @param path - path to write to
	 */
	public final void beginWriting(String path) {
		if (writer != null)
			return;
		if (reader != null)
			return;

		writer = new BinaryWriter(path);
	}

	/**
	 * Stop writing with the {@link Data#writer} and safely close the stream
	 */
	public final void stopWriting() {
		if (writer == null)
			return;

		writer.close();
		writer = null;
	}
	
	public boolean addObject(GameObject object) {
		if(object == null)
			return false;
		if(object instanceof Entity)
			this.entityBuffer.add((Entity) object);
		this.objectBuffer.add(object);
		
		return true;
	}

	// Getters and Setters	
	public final boolean reading() {
		return (reader != null);
	}

	public final boolean writing() {
		return (writer != null);
	}

	public final AssetManager getManager() {
		return manager;
	}

	public final Viewport getViewport() {
		return viewport;
	}

	public final void setViewport(Viewport viewport) {
		this.viewport = viewport;
	}

	public final Camera getCamera() {
		return camera;
	}

	public final void setCamera(Camera camera) {
		this.camera = camera;
	}

	public final BaseGame getBaseGame() {
		return game;
	}

	public final BinaryReader getReader() {
		return this.reader;
	}

	public final BinaryWriter getWriter() {
		return this.writer;
	}
}
