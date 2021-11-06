package helix.gfx;

import com.badlogic.gdx.ScreenAdapter;

import helix.game.BaseGame;
import helix.game.Data;

/**
 * Basic implementation of {@link ScreenAdapater}
 * Contains references to {@link Data} and {@link BaseGame}
 * @author Sly
 *
 */
public abstract class Screen extends ScreenAdapter {
	
	// Screens are less dynamic than game object (or at least should be) 
	// so we can treat them as such
	private static Long ID_NEXT = 0L;
	public final Long id = ID_NEXT++;
	
	private boolean initialized = false;
	
	/**
	 * Basic Step event of the screen. Called before {@link Screen#draw}
	 * @param delta - Time since last update (seconds)
	 */
	protected abstract void step(float delta);
	
	/**
	 * Basic Draw event of the screen. Called after {@link Screen#step}
	 * @param delta - Time since last update (seconds)
	 */
	protected abstract void draw(float delta);
	
	protected abstract void create();
	
	/**
	 * reference to main {@link Data}
	 */
	private Data data;
	
	/**
	 * reference to main {@link BaseGame}
	 */
	private BaseGame game;
	
	/**
	 * Create a new Screen and link it to a {@link BaseGame}
	 * @param game - Game to link to 
	 */
	public Screen(BaseGame game) {
		this.game = game;
		this.data = game.getData();
	}
	
	/**
	 * Initialize the Screen (Can only be called once per instance)
	 */
	public void init() {
		if(initialized)
			return;
		
		create();
		initialized = true;
	}
	
	@Override
	public void show() {
		this.init();
	}
	
	@Override
	public void render(float delta) {
		this.step(delta);
		this.draw(delta);
	}
	
	// Getters and Setters
	public BaseGame getGame()  {
		return game;
	}
	
	public Data getData() {
		return data;
	}
	
	public boolean isInitialized() {
		return initialized;
	}
}
