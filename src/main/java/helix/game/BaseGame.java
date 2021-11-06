package helix.game;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Set;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;

import helix.annotations.QueueAsset;
import helix.utils.ClassUtils;

/**
 * Basic implementation of {@link com.badlogic.gdx.Game}
 * 
 * @author Sly
 *
 */
public abstract class BaseGame extends Game {

	/**
	 * config for the application
	 */
	public final Lwjgl3ApplicationConfiguration config;
	/**
	 * Application dimensions
	 */
	public final int frameWidth, frameHeight;
	/**
	 * Application title
	 */
	public final String title;

	/**
	 * Application's data object. Keeps track of all the
	 * {@link helix.game.GameObject}s and {@link helix.game.objects.Entity}s and
	 * such
	 */
	private Data data;

	/**
	 * main game camera
	 */
	private OrthographicCamera camera;
	/** Asset loading progress */
	private float progress, lastProgress;

	/**
	 * Ran as the last thing to do on launch
	 */
	protected abstract void start();

	/**
	 * Ran before {@link BaseGame#start}. Used to add {@link helix.game.gfx.Screen}s
	 * to the game
	 */
	protected abstract void addScreens();

	/**
	 * Create a new {@link BaseGame} with specified frame title, height and width
	 * 
	 * @param title       - String
	 * @param frameWidth  - px
	 * @param frameHeight - px
	 */
	public BaseGame(String title, int frameWidth, int frameHeight) {
		config = new Lwjgl3ApplicationConfiguration();
		config.setTitle(title);
		config.setWindowedMode(frameWidth, frameHeight);

		config.setIdleFPS(60);
		config.setForegroundFPS(90);
		config.useVsync(true);
		config.setResizable(false);

		this.title = title;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;

		camera = new OrthographicCamera();
	}

	public final void create() {
		this.addScreens();

		// Load loading screen stuff
		this.getData().getManager().finishLoading();
		this.getData().setCamera(camera);

		this.loadTextures();
		this.data.init();

		try {
			this.setScreen(this.getData().getScreens().get(0));
		} catch (NullPointerException e) {

		}

		this.start();
	}

	/**
	 * Load all assets into the game
	 */
	private void loadTextures() {
		// Queue Resources
		Set<Class<?>> classes;
		try {
			classes = ClassUtils.getClassesFromJarFile();
			if(classes == null)
				classes = ClassUtils.getClasses();
		} catch (IOException e) {
			System.err.println("Failed to get Jar File");
			e.printStackTrace();
			classes = ClassUtils.getClasses();
		}

		for (Class<?> clazz : classes) {
			for (Field texField : clazz.getFields()) {
				if (!texField.isAnnotationPresent(QueueAsset.class))
					continue;

				QueueAsset queueAnnotation = texField.getAnnotation(QueueAsset.class);

				// Attempt to set the texture field to the new value
				Object old;
				try {
					old = texField.get(clazz); // clazz = Mage.class
					texField.set(old, queueAnnotation.ref());
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}

				this.getData().getManager().load(queueAnnotation.ref(), queueAnnotation.type());
			}
		}

		System.out.println("Queued " + this.getData().getManager().getQueuedAssets() + " assets to load");
		// Load Resources
		while (!this.getData().getManager().update()) {
			// Check if progress got updated
			progress = this.getData().getManager().getProgress();
			if (progress != lastProgress) {
				System.out.println("loading: " + this.getData().getManager().getProgress());
				this.lastProgress = progress;
			}
		}

	}

	// Getters and Setters
	public final OrthographicCamera getCamera() {
		return camera;
	}

	public final Data getData() {
		return data;
	}

	public final void setData(Data data) {
		this.data = data;
	}

	@Override
	public void setScreen(Screen s) {
		super.setScreen(s);
	}
	public void setScreen(int num) {
		this.setScreen(this.getData().getScreens().get(num));
	}
	
	public helix.gfx.Screen getScreen(int index) {
		return this.getData().getScreens().get(index);
	}
}
