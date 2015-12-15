package flyerGame.ui;

import java.util.ArrayList;

import engine.game.InputManager;
import engine.render.RenderLayer;
import engine.render.RenderLayer.Renderable;
import engine.ui.Button;
import flyerGame.engineExtension.Resources;

/**
 * Template class for all GUI Pages
 * All buttons added to the buttons List
 * and all the Renderable add to 
 * renderablesToAdd will be automatically
 * activated and deactivated according to
 * the Gui state (Enable/Disable)
 * @author L2k-nForce
 */
public abstract class Gui {
	
	private RenderLayer renderLayer = new RenderLayer();
	protected ArrayList<Button> buttons = new ArrayList<Button>();
	protected ArrayList<Renderable> renderablesToAdd = new ArrayList<Renderable>();
	
	public Gui() {
	}
	
	/**
	 * This method MUST be called at the end of
	 * the override constructor. This method adds
	 * all the {@link Renderable} into the renderLayer
	 * and registers all the {@link Button}'s {@link InputManager.ScreenMouseListener}
	 * into the {@link InputManager}
	 */
	protected void postConstrutorConfig() {
		for(Renderable readable: renderablesToAdd){
			renderLayer.addRenderable(readable);
		}
		for(Button button : buttons){
			InputManager.addScreenMouseListener(button.getScreenMouseListener());
			renderLayer.addRenderable(button);
		}
		setEnable(false);
		renderLayer.setFade(true);
	}

	/**
	 * Sets the state of the GUI (true/false)
	 * (Enable/Disable)
	 * @param set
	 */
	public void setEnable(boolean set) {
		renderLayer.setVisible(set);
		for(Button button : buttons)
			button.getScreenMouseListener().setActive(set);
	}
	/**
	 * @return the state of the GUI (true/false)
	 * (Enable/Disable)
	 */
	public boolean isEnable() {
		return renderLayer.isVisible();
	}

	/**
	 * @return renderLayer with all of the {@link Renderable}
	 * added.
	 */
	public RenderLayer getRenderLayer() {
		return renderLayer;
	}

	/**
	 * This method is called when an update to the {@link Resources}
	 * fields have been made that affects the rendering.
	 */
	public abstract void updateRenderableStates();
}
